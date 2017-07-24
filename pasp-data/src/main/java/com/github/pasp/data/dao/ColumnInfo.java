package com.github.pasp.data.dao;

import java.util.Objects;

class ColumnInfo {
	private String columnName;

	private String fieldName;

	private Class<?> type;

	private boolean primaryKey;

	private boolean sharding;

	private boolean insertRequired;

	private boolean updateRequired;

	private boolean deleteRequired;

	private boolean selectRequired;

	private boolean versioning;

	public ColumnInfo() {

	}

	public ColumnInfo(String columnName, String fieldName) {
		super();
		this.columnName = columnName;
		this.fieldName = fieldName;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public Class<?> getType() {
		return type;
	}

	public void setType(Class<?> type) {
		this.type = type;
	}

	public boolean isPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(boolean primaryKey) {
		this.primaryKey = primaryKey;
	}

	public boolean isSharding() {
		return sharding;
	}

	public void setSharding(boolean sharding) {
		this.sharding = sharding;
	}

	public boolean isInsertRequired() {
		return insertRequired;
	}

	public void setInsertRequired(boolean insertRequired) {
		this.insertRequired = insertRequired;
	}

	public boolean isUpdateRequired() {
		return updateRequired;
	}

	public void setUpdateRequired(boolean updateRequired) {
		this.updateRequired = updateRequired;
	}

	public boolean isDeleteRequired() {
		return deleteRequired;
	}

	public void setDeleteRequired(boolean deleteRequired) {
		this.deleteRequired = deleteRequired;
	}

	public boolean isSelectRequired() {
		return selectRequired;
	}

	public void setSelectRequired(boolean selectRequired) {
		this.selectRequired = selectRequired;
	}

	public boolean isVersioning() {
		return versioning;
	}

	public void setVersioning(boolean versioning) {
		this.versioning = versioning;
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.columnName, this.fieldName);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof ColumnInfo)) {
			return false;
		}
		ColumnInfo o = (ColumnInfo) obj;
		return StringUtils.equals(this.columnName, o.columnName) && StringUtils.equals(this.fieldName, o.fieldName);
	}

}
