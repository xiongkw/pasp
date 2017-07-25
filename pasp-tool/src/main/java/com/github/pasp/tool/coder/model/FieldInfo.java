package com.github.pasp.tool.coder.model;

public class FieldInfo {
	private String name;

	private String columnName;

	private String fullJavaType;

	private int dbType;

	private String dbTypeName;

	private String comment;

	private int length;

	private int scale;
	
	private boolean nullable;

	private boolean isVersionId;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public int getDbType() {
		return dbType;
	}

	public void setDbType(int dbType) {
		this.dbType = dbType;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getDbTypeName() {
		return dbTypeName;
	}

	public void setDbTypeName(String dbTypeName) {
		this.dbTypeName = dbTypeName;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public int getScale() {
		return scale;
	}

	public void setScale(int scale) {
		this.scale = scale;
	}

	public String getFullJavaType() {
		return fullJavaType;
	}

	public void setFullJavaType(String fullJavaType) {
		this.fullJavaType = fullJavaType;
	}

	public String getJavaType() {
		if (this.fullJavaType.indexOf(".") < 0) {
			return this.fullJavaType;
		}
		return this.fullJavaType.substring(this.fullJavaType.lastIndexOf(".") + 1);
	}

	public boolean isVersionId() {
		return isVersionId;
	}

	public void setVersionId(boolean isVersionId) {
		this.isVersionId = isVersionId;
	}

	public boolean isNullable() {
		return nullable;
	}

	public void setNullable(boolean nullable) {
		this.nullable = nullable;
	}

}
