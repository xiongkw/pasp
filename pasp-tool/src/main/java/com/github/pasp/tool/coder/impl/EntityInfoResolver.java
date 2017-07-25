package com.github.pasp.tool.coder.impl;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.github.pasp.tool.coder.IEntityInfoResolver;
import com.github.pasp.tool.coder.config.Jdbc;
import com.github.pasp.tool.coder.config.Module;
import com.github.pasp.tool.coder.config.Table;
import com.github.pasp.tool.util.DBUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.pasp.tool.coder.config.Config;
import com.github.pasp.tool.coder.model.EntityInfo;
import com.github.pasp.tool.coder.model.FieldInfo;

public class EntityInfoResolver implements IEntityInfoResolver {
	private static Logger logger = LoggerFactory.getLogger(EntityInfoResolver.class);

	@Override
	public List<EntityInfo> resolve(Config config) {
		List<EntityInfo> entities = new ArrayList<EntityInfo>();
		List<Module> modules = config.getModules();
		if (modules == null) {
			return entities;
		}
		Connection conn = null;
		try {
			Jdbc jdbc = config.getJdbc();
			conn = DBUtils.getConnection(jdbc.getDriverClass(), jdbc.getUrl(), jdbc.getUsername(), jdbc.getPassword());
			String[] tableNamePrefixes = config.getTableNamePrefixes();
			for (Module module : modules) {
				String moduleName = module.getName();
				List<Table> tables = module.getTables();
				for (Table table : tables) {
					EntityInfo entityInfo = readEntityInfo(table.getName(), conn, tableNamePrefixes,
							config.isIntegerForceLong(), table.getShardingIds(), table.getVersionId());
					entityInfo.setModuleName(moduleName);
					entities.add(entityInfo);
				}
			}
		} catch (Exception e) {
			logger.error("Resolve entity info failed!", e);
		} finally {
			DBUtils.close(conn);
		}
		return entities;
	}

	private EntityInfo readEntityInfo(String tableName, Connection connection, String[] tableNamePrefixes,
			boolean integerForceLong, Set<String> shardingIds, String versionId) throws SQLException {
		EntityInfo entity = new EntityInfo();
		DatabaseMetaData metaData = connection.getMetaData();
		entity.setTableName(tableName);
		entity.setEntityName(caculateEntityName(tableName, tableNamePrefixes));
		/*
		 * 得到主键信息
		 */
		String userName = metaData.getUserName();

		String pkName = null;
		ResultSet primaryKeys = null;
		try {
			primaryKeys = metaData.getPrimaryKeys(null, userName, tableName);
			// 只支持单主键
			if (primaryKeys.next()) {
				pkName = primaryKeys.getString("COLUMN_NAME");
			}
		} finally {
			if (primaryKeys != null) {
				primaryKeys.close();
			}
		}

		/*
		 * 得到表的所有字段信息
		 */
		List<FieldInfo> fields = new ArrayList<FieldInfo>();
		ResultSet columns = null;
		try {
			columns = metaData.getColumns(null, userName, tableName, "%");
			while (columns.next()) {
				String columnName = columns.getString("COLUMN_NAME");
				FieldInfo field = new FieldInfo();
				field.setColumnName(columnName);
				field.setName(caculateFieldName(columnName));
				int dbType = columns.getInt("DATA_TYPE");
				field.setDbType(dbType);
				field.setDbTypeName(columns.getString("TYPE_NAME"));
				field.setLength(columns.getInt("COLUMN_SIZE"));
				field.setScale(columns.getInt("DECIMAL_DIGITS"));
				field.setFullJavaType(JavaTypeResolver.calculateJavaType(dbType, integerForceLong));
				field.setNullable(columns.getInt("NULLABLE") == DatabaseMetaData.columnNoNulls);
				if (isShardingId(shardingIds, columnName)) {
					field.setShardingId(true);
					entity.setShardingBean(true);
				}
				if (columnName.equalsIgnoreCase(versionId)) {
					field.setVersionId(true);
				}
				if (columnName.equals(pkName)) {
					entity.setPk(field);
					continue;
				}
				fields.add(field);
			}
			entity.setFields(fields);
			entity.processImportList();

			return entity;
		} finally {
			if (columns != null) {
				columns.close();
			}
		}
	}

	private boolean isShardingId(Set<String> shardingIds, String columnName) {
		if (shardingIds == null) {
			return false;
		}
		for (String string : shardingIds) {
			if (columnName.equalsIgnoreCase(string)) {
				return true;
			}
		}
		return false;
	}

	private String caculateEntityName(String tableName, String[] tableNamePrefixes) {
		String tableNameUpperCase = tableName.toUpperCase();
		int prefixLength = 0;
		if (tableNamePrefixes != null && tableName.length() != 0) {
			for (String prefix : tableNamePrefixes) {
				int len = prefix.length();
				// 取最优匹配
				if (tableNameUpperCase.startsWith(prefix.toUpperCase()) && len > prefixLength) {
					prefixLength = len;
				}
			}
		}
		tableName = tableName.substring(prefixLength);
		String[] split = tableName.split("_");
		StringBuilder sb = new StringBuilder();
		for (String string : split) {
			sb.append(capitalize(string.toLowerCase()));
		}
		return sb.toString();
	}

	private String caculateFieldName(String columnName) {
		String[] split = columnName.split("_");
		StringBuilder sb = new StringBuilder();
		sb.append(split[0].toLowerCase());
		for (int i = 1; i < split.length; i++) {
			sb.append(capitalize(split[i].toLowerCase()));
		}
		return sb.toString();
	}

	public String capitalize(final String str) {
		int strLen;
		if (str == null || (strLen = str.length()) == 0) {
			return str;
		}

		final char firstChar = str.charAt(0);
		if (Character.isTitleCase(firstChar)) {
			// already capitalized
			return str;
		}

		return new StringBuilder(strLen).append(Character.toTitleCase(firstChar)).append(str.substring(1)).toString();
	}
}
