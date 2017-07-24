package com.github.pasp.data.dao;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.github.pasp.core.Entity;
import com.github.pasp.core.exception.DataAccessException;
import com.github.pasp.data.ISqlBuilder;
import org.springframework.util.ReflectionUtils;

import com.github.pasp.data.Sql;

public class SqlBuilderImpl implements ISqlBuilder {
    private static final String NULL_ENTITY_INFO = "实体定义不可为空!";
    private static final String NULL_ENTITY = "实体信息不可为空!";

    private Object getProperty(Object entity, String fieldname) {
        if (entity == null) {
            return null;
        }
        Field field = ReflectionUtils.findField(entity.getClass(), fieldname);
        if (field == null) {
            return null;
        }
        if (!field.isAccessible()) {
            field.setAccessible(true);
        }
        return ReflectionUtils.getField(field, entity);
    }

    private static void assertNotNull(Object obj, String message) {
        if (obj == null) {
            throw new DataAccessException(message);
        }
    }

    @Override
    public Sql buildInsertSql(Entity<?> entity, EntityInfo info) {
        String statement = info.getInsertSql();
        Object[] args = getInsertColumnValues(entity, info);
        return new Sql(statement, args);
    }

    private Object[] getInsertColumnValues(Entity<?> entity, EntityInfo info) {
        assertNotNull(entity, NULL_ENTITY);
        assertNotNull(info, NULL_ENTITY_INFO);
        List<ColumnInfo> columnList = info.getColumnList();
        Object[] objs = new Object[columnList.size()];
        for (int i = 0; i < objs.length; i++) {
            ColumnInfo columnInfo = columnList.get(i);
            String fieldName = columnInfo.getFieldName();
            objs[i] = getProperty(entity, fieldName);
            if (columnInfo.isSharding() && columnInfo.isInsertRequired() && objs[i] == null) {
                throw new DataAccessException("分片ID:" + fieldName + "@" + info.getTableName() + "不可为空!");
            }
        }
        return objs;
    }

    @Override
    public Sql buildSelectByPrimaryKeySql(Entity<?> entity, EntityInfo info) {
        String statement = info.getSelectByPrimaryKeySql();
        Object[] args = getSelectColumnValues(entity, info);
        return new Sql(statement, args);
    }

    private Object[] getSelectColumnValues(Entity<?> entity, EntityInfo info) {
        assertNotNull(entity, NULL_ENTITY);
        assertNotNull(info, NULL_ENTITY_INFO);
        List<Object> objs = new ArrayList<Object>();
        ColumnInfo pk = info.getPrimaryKey();
        String pkName = pk.getFieldName();
        Object pkValue = getProperty(entity, pkName);
        String tableName = info.getTableName();
        assertNotNull(pkValue, "主键:" + pkName + "@" + tableName + "不可为空!");
        objs.add(pkValue);
        List<ColumnInfo> shardingColumnList = info.getShardingColumnList();
        for (ColumnInfo columnInfo : shardingColumnList) {
            if (!columnInfo.isSelectRequired()) {
                continue;
            }
            String fieldName = columnInfo.getFieldName();
            Object value = getProperty(entity, fieldName);
            if (value == null) {
                throw new DataAccessException("分片ID:" + fieldName + "@" + tableName + "不可为空!");
            }
            objs.add(value);
        }
        return objs.toArray(new Object[objs.size()]);
    }

    @Override
    public Sql buildUpdateByPrimaryKeySql(Entity<?> entity, EntityInfo entityInfo) {
        String statement = buildUpdateStatement(entity, entityInfo, false);
        Object[] args = getUpdateColumnValues(entity, entityInfo, false);
        return new Sql(statement, args);
    }

    @Override
    public Sql buildDeleteByPrimaryKeySql(Entity<?> entity, EntityInfo info) {
        String statement = info.getDeleteByPrimaryKeySqlWithVersion();
        Object[] args = getDeleteColumnValues(entity, info, true);
        return new Sql(statement, args);
    }

    private Object[] getDeleteColumnValues(Entity<?> entity, EntityInfo info, boolean withVersion) {
        assertNotNull(entity, NULL_ENTITY);
        assertNotNull(info, NULL_ENTITY_INFO);
        List<Object> objs = new ArrayList<Object>();
        ColumnInfo pk = info.getPrimaryKey();
        String pkName = pk.getFieldName();
        Object pkValue = getProperty(entity, pkName);
        String tableName = info.getTableName();
        assertNotNull(pkValue, "主键:" + pkName + "@" + tableName + "不可为空!");
        objs.add(pkValue);
        List<ColumnInfo> shardingColumnList = info.getShardingColumnList();
        for (ColumnInfo columnInfo : shardingColumnList) {
            if (!columnInfo.isDeleteRequired()) {
                continue;
            }
            String fieldName = columnInfo.getFieldName();
            Object value = getProperty(entity, fieldName);
            if (value == null) {
                throw new DataAccessException("分片ID:" + fieldName + "@" + tableName + "不可为空!");
            }
            objs.add(value);
        }
        ColumnInfo versionColumn = info.getVersionColumn();
        if (withVersion && versionColumn != null) {
            String fieldName = versionColumn.getFieldName();
            Object value = getProperty(entity, fieldName);
            if (value == null) {
                throw new DataAccessException("乐观锁版本号:" + fieldName + "@" + tableName + "不可为空!");
            }
            objs.add(value);
        }
        return objs.toArray(new Object[objs.size()]);
    }

    @Override
    public Sql buildUpdateSelectiveByPrimaryKeySql(Entity<?> entity, EntityInfo entityInfo) {
        String statement = buildUpdateStatement(entity, entityInfo, true);
        Object[] args = getUpdateColumnValues(entity, entityInfo, true);
        return new Sql(statement, args);
    }

    private String buildUpdateStatement(Entity<?> entity, EntityInfo entityInfo, boolean selective) {
        assertNotNull(entityInfo, NULL_ENTITY_INFO);
        if (!selective) {
            return entityInfo.getUpdateByPrimaryKeySql();
        }
        List<ColumnInfo> columnList = entityInfo.getColumnList();
        StringBuilder sb = new StringBuilder("UPDATE ").append(entityInfo.getTableName()).append(" SET ");
        int i = 0;
        for (ColumnInfo columnInfo : columnList) {
            if (columnInfo.isPrimaryKey() || columnInfo.isSharding()) {
                continue;
            }
            String columnName = columnInfo.getColumnName();
            if (columnInfo.isVersioning()) {
                if (i++ != 0) {
                    sb.append(", ");
                }
                sb.append(columnName).append("=").append("(").append(columnName).append("+1)");
                continue;
            }
            Object value = getProperty(entity, columnInfo.getFieldName());
            if (value == null) {
                continue;
            }
            if (i++ != 0) {
                sb.append(", ");
            }
            sb.append(columnName).append("=?");
        }
        ColumnInfo pk = entityInfo.getPrimaryKey();
        String tableName = entityInfo.getTableName();
        assertNotNull(pk, "缺少主键定义: " + tableName + "@" + entity.getClass());
        sb.append(" WHERE ").append(pk.getColumnName()).append("=?");
        String pkName = pk.getFieldName();
        Object pkValue = getProperty(entity, pkName);
        assertNotNull(pkValue, "主键:" + pkName + "@" + tableName + "不可为空!");
        List<ColumnInfo> shardingColumnList = entityInfo.getShardingColumnList();
        if (shardingColumnList != null) {
            for (ColumnInfo columnInfo : shardingColumnList) {
                if (!columnInfo.isUpdateRequired()) {
                    continue;
                }
                String fieldName = columnInfo.getFieldName();
                Object value = getProperty(entity, fieldName);
                if (value == null) {
                    throw new DataAccessException("分片ID:" + fieldName + "@" + tableName + "不可为空!");
                }
                sb.append(" AND ").append(columnInfo.getColumnName()).append("=?");
            }
        }
        ColumnInfo versionColumn = entityInfo.getVersionColumn();
        if (versionColumn != null) {
            String fieldName = versionColumn.getFieldName();
            Object value = getProperty(entity, fieldName);
            if (value == null) {
                throw new DataAccessException("乐观锁版本号:" + fieldName + "@" + tableName + "不可为空!");
            }
            sb.append(" AND ").append(versionColumn.getColumnName()).append("=?");
        }
        return sb.toString();
    }

    private Object[] getUpdateColumnValues(Entity<?> entity, EntityInfo entityInfo, boolean selective) {
        assertNotNull(entity, NULL_ENTITY);
        assertNotNull(entityInfo, NULL_ENTITY_INFO);
        List<ColumnInfo> columnList = entityInfo.getColumnList();
        List<Object> objs = new ArrayList<Object>();
        for (ColumnInfo columnInfo : columnList) {
            if (columnInfo.isPrimaryKey() || columnInfo.isSharding() || columnInfo.isVersioning()) {
                continue;
            }
            Object value = getProperty(entity, columnInfo.getFieldName());
            if (!selective || value != null) {
                objs.add(value);
            }
        }
        ColumnInfo pk = entityInfo.getPrimaryKey();
        String tableName = entityInfo.getTableName();
        assertNotNull(pk, "缺少主键定义: " + tableName + "@" + entity.getClass());

        String pkName = pk.getFieldName();
        Object pkValue = getProperty(entity, pkName);
        assertNotNull(pkValue, "主键:" + pkName + "@" + tableName + "不可为空!");
        objs.add(pkValue);

        List<ColumnInfo> shardingColumnList = entityInfo.getShardingColumnList();
        if (shardingColumnList != null) {
            for (ColumnInfo columnInfo : shardingColumnList) {
                if (!columnInfo.isUpdateRequired()) {
                    continue;
                }
                String fieldName = columnInfo.getFieldName();
                Object value = getProperty(entity, fieldName);
                if (value == null) {
                    throw new DataAccessException("分片ID:" + fieldName + "@" + tableName + "不可为空!");
                }
                objs.add(value);
            }
        }
        ColumnInfo versionColumn = entityInfo.getVersionColumn();
        if (versionColumn != null) {
            String fieldName = versionColumn.getFieldName();
            Object value = getProperty(entity, fieldName);
            if (value == null) {
                throw new DataAccessException("乐观锁版本号:" + fieldName + "@" + tableName + "不可为空!");
            }
            objs.add(value);
        }
        return objs.toArray(new Object[objs.size()]);
    }

    @Override
    public Sql buildSelectByExampleSql(Entity<?> entity, EntityInfo entityInfo) {
        assertNotNull(entityInfo, NULL_ENTITY_INFO);
        List<Object> objs = new ArrayList<Object>();
        StringBuilder sb = new StringBuilder("SELECT ");
        StringBuilder where = new StringBuilder(" FROM ").append(entityInfo.getTableName());
        List<ColumnInfo> columnList = entityInfo.getColumnList();
        int i = 0, j = 0;
        for (ColumnInfo columnInfo : columnList) {
            if (i++ != 0) {
                sb.append(", ");
            }
            String columnName = columnInfo.getColumnName();
            sb.append(columnName);
            if (columnInfo.isPrimaryKey()) {
                continue;
            }
            String fieldName = columnInfo.getFieldName();
            Object value = getProperty(entity, fieldName);
            if (value == null) {
                continue;
            }
            if (j == 0) {
                where.append(" WHERE ");
            }
            if (j > 0) {
                where.append(" AND ");
            }
            where.append(columnName).append("=?");
            j++;
            objs.add(value);
        }
        return new Sql(sb.append(where).toString(), objs.toArray(new Object[objs.size()]));
    }

    @Override
    public Sql buildCountByExampleSql(Entity<?> entity, EntityInfo entityInfo) {
        assertNotNull(entityInfo, NULL_ENTITY_INFO);
        List<Object> objs = new ArrayList<Object>();
        StringBuilder sb = new StringBuilder("SELECT COUNT(1) FROM ").append(entityInfo.getTableName());
        List<ColumnInfo> columnList = entityInfo.getColumnList();
        int i = 0;
        for (ColumnInfo columnInfo : columnList) {
            if (columnInfo.isPrimaryKey()) {
                continue;
            }
            String fieldName = columnInfo.getFieldName();
            Object value = getProperty(entity, fieldName);
            if (value == null) {
                continue;
            }
            if (i == 0) {
                sb.append(" WHERE ");
            }
            if (i > 0) {
                sb.append(" AND ");
            }
            sb.append(columnInfo.getColumnName()).append("=?");
            i++;
            objs.add(value);
        }
        return new Sql(sb.toString(), objs.toArray(new Object[objs.size()]));
    }

    @Override
    public Sql buildBatchInsertSql(List<?> entities, EntityInfo entityInfo) {
        assertNotNull(entities, NULL_ENTITY);
        assertNotNull(entityInfo, NULL_ENTITY_INFO);
        String statement = entityInfo.getInsertSql();
        List<Object[]> list = new ArrayList<Object[]>(entities.size());
        for (Object obj : entities) {
            Object[] columnValues = getInsertColumnValues((Entity<?>) obj, entityInfo);
            list.add(columnValues);
        }
        return new Sql(statement, list);
    }

    @Override
    public Sql buildBatchUpdateByPrimaryKeySql(List<?> entities, EntityInfo entityInfo) {
        assertNotNull(entities, NULL_ENTITY);
        assertNotNull(entityInfo, NULL_ENTITY_INFO);
        String statement = entityInfo.getUpdateByPrimaryKeySql();
        List<Object[]> list = new ArrayList<Object[]>(entities.size());
        for (Object obj : entities) {
            Object[] columnValues = getUpdateColumnValues((Entity<?>) obj, entityInfo, false);
            list.add(columnValues);
        }
        return new Sql(statement, list);
    }

    @Override
    public Sql buildBatchDeleteByPrimaryKeySql(List<?> entities, EntityInfo entityInfo) {
        assertNotNull(entities, NULL_ENTITY);
        assertNotNull(entityInfo, NULL_ENTITY_INFO);
        String statement = entityInfo.getDeleteByPrimaryKeySql();
        List<Object[]> list = new ArrayList<Object[]>(entities.size());
        for (Object obj : entities) {
            Object[] columnValues = getDeleteColumnValues((Entity<?>) obj, entityInfo, false);
            list.add(columnValues);
        }
        return new Sql(statement, list);
    }

    @Override
    public Sql buildDeleteByExampleSql(Entity<?> example, EntityInfo entityInfo) {
        assertNotNull(entityInfo, NULL_ENTITY_INFO);
        List<Object> objs = new ArrayList<Object>();
        StringBuilder sb = new StringBuilder("DELETE FROM ").append(entityInfo.getTableName());
        List<ColumnInfo> columnList = entityInfo.getColumnList();
        int i = 0;
        for (ColumnInfo columnInfo : columnList) {
            String columnName = columnInfo.getColumnName();
            if (columnInfo.isPrimaryKey()) {
                continue;
            }
            String fieldName = columnInfo.getFieldName();
            Object value = getProperty(example, fieldName);
            if (value == null) {
                continue;
            }
            if (i == 0) {
                sb.append(" WHERE ");
            }
            if (i++ > 0) {
                sb.append(" AND ");
            }
            sb.append(columnName).append("=?");
            objs.add(value);
        }
        return new Sql(sb.toString(), objs.toArray(new Object[objs.size()]));
    }

    @Override
    public Sql buildUpdateSelectiveByExampleSql(Entity<?> entity, Entity<?> example, EntityInfo entityInfo) {
        return buildUpdateByExampleSql(entity, example, entityInfo, true);
    }

    @Override
    public Sql buildUpdateByExampleSql(Entity<?> entity, Entity<?> example, EntityInfo entityInfo) {
        return buildUpdateByExampleSql(entity, example, entityInfo, false);
    }

    private Sql buildUpdateByExampleSql(Entity<?> entity, Entity<?> example, EntityInfo entityInfo, boolean selective) {
        assertNotNull(entityInfo, NULL_ENTITY_INFO);
        List<Object> setArgs = new ArrayList<Object>();
        List<Object> whereArgs = new ArrayList<Object>();
        StringBuilder sb = new StringBuilder("UPDATE ").append(entityInfo.getTableName()).append(" SET ");
        StringBuilder where = new StringBuilder();
        List<ColumnInfo> columnList = entityInfo.getColumnList();
        int i = 0, j = 0;
        for (ColumnInfo columnInfo : columnList) {
            String columnName = columnInfo.getColumnName();
            if (columnInfo.isPrimaryKey()) {
                continue;
            }
            String fieldName = columnInfo.getFieldName();
            if (columnInfo.isVersioning()) {
                if (i++ != 0) {
                    sb.append(", ");
                }
                sb.append(columnName).append("=").append("(").append(columnName).append("+1)");
            } else if (!columnInfo.isSharding()) {
                Object entityValue = getProperty(entity, fieldName);
                if (!selective || entityValue != null) {
                    if (i++ != 0) {
                        sb.append(", ");
                    }
                    sb.append(columnName).append("=?");
                    setArgs.add(entityValue);
                }
            }
            Object exampleValue = getProperty(example, fieldName);
            if (exampleValue == null) {
                continue;
            }
            if (j == 0) {
                where.append(" WHERE ");
            }
            if (j > 0) {
                where.append(" AND ");
            }
            where.append(columnName).append("=?");
            j++;
            whereArgs.add(exampleValue);
        }
        setArgs.addAll(whereArgs);
        return new Sql(sb.append(where).toString(), setArgs.toArray(new Object[setArgs.size()]));
    }

}
