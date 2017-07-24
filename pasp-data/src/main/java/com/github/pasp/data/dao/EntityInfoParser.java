package com.github.pasp.data.dao;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

import com.github.pasp.core.Entity;
import com.github.pasp.data.IEntityInfoParser;
import com.github.pasp.data.utils.ColumnUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;


public class EntityInfoParser implements IEntityInfoParser {
	private static Logger logger = LoggerFactory.getLogger(EntityInfoParser.class);

	public EntityInfo parse(Class<? extends Entity<?>> cl) {
		Assert.notNull(cl, "Entity class can't be null!");
		String className = cl.getName();
		Table anno = cl.getAnnotation(Table.class);
		if (anno == null) {
			logger.warn("No annotation (@Table) defined in entity class: {}", className);
			return null;
		}
		EntityInfo entityInfo = parseEntityInfo(cl, anno);
		logger.debug("Entity info of class: {} has been registered!", className);
		return entityInfo;
	}

	private EntityInfo parseEntityInfo(Class<?> cl, Table anno) {
		String tableName = anno.name();
		EntityInfo entityInfo = new EntityInfo(tableName);
		List<ColumnInfo> columnList = new ArrayList<ColumnInfo>();
		entityInfo.setColumnList(columnList);
		List<ColumnInfo> shardingColumnList = new ArrayList<ColumnInfo>();
		entityInfo.setShardingColumnList(shardingColumnList);
		ColumnInfo pk = null;
		ColumnInfo version = null;
		Class<?> clazz = cl;
		while (clazz != null) {
			Field[] declaredFields = clazz.getDeclaredFields();
			if (declaredFields != null) {
				for (Field field : declaredFields) {
					Column columnAnno = field.getAnnotation(Column.class);
					if (columnAnno == null) {
						continue;
					}
					String fieldName = field.getName();
					String columnName = columnAnno.name();
					if (StringUtils.isEmpty(columnName)) {
						columnName = ColumnUtils.parsePropertyName2ColumnName(fieldName);
					}
					ColumnInfo columnInfo = new ColumnInfo(columnName, fieldName);
					if (columnList.contains(columnInfo)) {
						continue;
					}
					columnList.add(columnInfo);
					columnInfo.setType(field.getType());
					boolean pkAnno = field.isAnnotationPresent(Id.class);
					if (pkAnno) {
						columnInfo.setPrimaryKey(pkAnno);
						pk = columnInfo;
						entityInfo.setPrimaryKey(pk);
					}
					Version versionAnno = field.getAnnotation(Version.class);
					if (versionAnno != null) {
						version = columnInfo;
						entityInfo.setVersionColumn(columnInfo);
						columnInfo.setVersioning(true);
					}
				}
			}
			clazz = clazz.getSuperclass();
		}

		if (pk == null) {
			logger.warn("Parse entity info failed, No primary key found for Entity: {}", cl.getName());
			return null;
		}

		String insertSql = parseInsertSql(tableName, columnList);
		entityInfo.setInsertSql(insertSql);

		String updateByPrimaryKeySql = parseUpdateByPrimaryKeySql(tableName, columnList, shardingColumnList);
		entityInfo.setUpdateByPrimaryKeySql(updateByPrimaryKeySql);

		String deleteByPrimaryKeySql = parseDeleteByPrimaryKeySql(tableName, pk, version, shardingColumnList);
		entityInfo.setDeleteByPrimaryKeySql(deleteByPrimaryKeySql);

		String deleteByPrimaryKeySqlWithVersion = parseDeleteByPrimaryKeySqlWithVersion(deleteByPrimaryKeySql, version);
		entityInfo.setDeleteByPrimaryKeySqlWithVersion(deleteByPrimaryKeySqlWithVersion);

		String selectByPrimaryKeySql = parseSelectByPrimaryKeySql(tableName, columnList, shardingColumnList);
		entityInfo.setSelectByPrimaryKeySql(selectByPrimaryKeySql);
		return entityInfo;
	}

	private String parseSelectByPrimaryKeySql(String tableName, List<ColumnInfo> columnList,
			List<ColumnInfo> shardingColumnList) {
		StringBuilder sb = new StringBuilder("SELECT ");
		int i = 0;
		ColumnInfo pk = null;
		for (ColumnInfo columnInfo : columnList) {
			if (columnInfo.isPrimaryKey()) {
				pk = columnInfo;
			}
			if (i++ > 0) {
				sb.append(", ");
			}
			sb.append(columnInfo.getColumnName());
		}
		sb.append(" FROM ").append(tableName).append(" WHERE ").append(pk.getColumnName()).append("=?");
		for (ColumnInfo columnInfo : shardingColumnList) {
			if (columnInfo.isSelectRequired()) {
				sb.append(" AND ").append(columnInfo.getColumnName()).append("=?");
			}
		}
		return sb.toString();
	}

	private String parseDeleteByPrimaryKeySql(String tableName, ColumnInfo pk, ColumnInfo versionColumn,
			List<ColumnInfo> shardingColumnList) {
		if (pk == null) {
			logger.warn("Parse delete by primary key sql failed, No primary key found for table: {}", tableName);
			return null;
		}
		StringBuilder sb = new StringBuilder("DELETE FROM ").append(tableName).append(" WHERE ")
				.append(pk.getColumnName()).append("=?");
		for (ColumnInfo columnInfo : shardingColumnList) {
			if (columnInfo.isDeleteRequired()) {
				sb.append(" AND ").append(columnInfo.getColumnName()).append("=?");
			}
		}
		return sb.toString();
	}

	private String parseDeleteByPrimaryKeySqlWithVersion(String deleteByPrimaryKeySql, ColumnInfo version) {
		if (version == null) {
			return deleteByPrimaryKeySql;
		}
		return new StringBuilder(deleteByPrimaryKeySql).append(" AND ").append(version.getColumnName()).append("=?")
				.toString();
	}

	private String parseUpdateByPrimaryKeySql(String tableName, List<ColumnInfo> columnList,
			List<ColumnInfo> shardingColumnList) {
		StringBuilder sb = new StringBuilder("UPDATE ").append(tableName).append(" SET ");
		int i = 0;
		ColumnInfo pk = null;
		String versionColumn = null;
		for (ColumnInfo columnInfo : columnList) {
			if (columnInfo.isPrimaryKey()) {
				pk = columnInfo;
				continue;
			}
			// shardingId不能update，必须由业务系统开发人员先删除后再插入，所以shardingId和id一样，都是作为条件放在where语句
			// update语句中没有shardingId字段
			if(columnInfo.isSharding()){
				continue;
			}
			if (i++ > 0) {
				sb.append(", ");
			}
			String columnName = columnInfo.getColumnName();
			sb.append(columnName);
			if (columnInfo.isVersioning()) {
				versionColumn = columnName;
				sb.append("=(").append(columnName).append("+1)");
			} else {
				sb.append("=?");
			}
		}
		sb.append(" WHERE ").append(pk.getColumnName()).append("=?");
		for (ColumnInfo columnInfo : shardingColumnList) {
			if (columnInfo.isUpdateRequired()) {
				sb.append(" AND ").append(columnInfo.getColumnName()).append("=?");
			}
		}
		if (versionColumn != null) {
			sb.append(" AND ").append(versionColumn).append("=?");
		}
		return sb.toString();
	}

	private String parseInsertSql(String tableName, List<ColumnInfo> columnList) {
		StringBuilder sb = new StringBuilder("INSERT INTO ").append(tableName).append("(");
		int i = 0;
		for (ColumnInfo columnInfo : columnList) {
			if (i++ > 0) {
				sb.append(", ");
			}
			sb.append(columnInfo.getColumnName());
		}
		sb.append(") VALUES (");
		for (int j = 0; j < i; j++) {
			if (j > 0) {
				sb.append(", ");
			}
			sb.append("?");
		}
		return sb.append(")").toString();
	}
}
