/**
 * 
 */
package com.github.pasp.data.dao;

import java.util.List;

/**
 * @author Administrator
 *
 */
public class EntityInfo {

	private String tableName;

	private ColumnInfo primaryKey;

	private ColumnInfo versionColumn;

	private List<ColumnInfo> shardingColumnList;

	private List<ColumnInfo> columnList;

	private String insertSql;

	private String selectByPrimaryKeySql;

	private String updateByPrimaryKeySql;

	private String deleteByPrimaryKeySql;
	
	private String deleteByPrimaryKeySqlWithVersion;

	public EntityInfo() {

	}

	public EntityInfo(String tableName) {
		super();
		this.tableName = tableName;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public ColumnInfo getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(ColumnInfo primaryKey) {
		this.primaryKey = primaryKey;
	}

	public List<ColumnInfo> getShardingColumnList() {
		return shardingColumnList;
	}

	public void setShardingColumnList(List<ColumnInfo> shardingColumnList) {
		this.shardingColumnList = shardingColumnList;
	}

	public ColumnInfo getVersionColumn() {
		return versionColumn;
	}

	public void setVersionColumn(ColumnInfo versionColumn) {
		this.versionColumn = versionColumn;
	}

	public List<ColumnInfo> getColumnList() {
		return columnList;
	}

	public void setColumnList(List<ColumnInfo> columnList) {
		this.columnList = columnList;
	}

	public String getInsertSql() {
		return insertSql;
	}

	public void setInsertSql(String insertSql) {
		this.insertSql = insertSql;
	}

	public String getSelectByPrimaryKeySql() {
		return selectByPrimaryKeySql;
	}

	public void setSelectByPrimaryKeySql(String selectByPrimaryKeySql) {
		this.selectByPrimaryKeySql = selectByPrimaryKeySql;
	}

	public String getUpdateByPrimaryKeySql() {
		return updateByPrimaryKeySql;
	}

	public void setUpdateByPrimaryKeySql(String updateByPrimaryKeySql) {
		this.updateByPrimaryKeySql = updateByPrimaryKeySql;
	}

	public String getDeleteByPrimaryKeySql() {
		return deleteByPrimaryKeySql;
	}

	public void setDeleteByPrimaryKeySql(String deleteByPrimaryKeySql) {
		this.deleteByPrimaryKeySql = deleteByPrimaryKeySql;
	}

	public String getDeleteByPrimaryKeySqlWithVersion() {
		return deleteByPrimaryKeySqlWithVersion;
	}

	public void setDeleteByPrimaryKeySqlWithVersion(String deleteByPrimaryKeySqlWithVersion) {
		this.deleteByPrimaryKeySqlWithVersion = deleteByPrimaryKeySqlWithVersion;
	}

}
