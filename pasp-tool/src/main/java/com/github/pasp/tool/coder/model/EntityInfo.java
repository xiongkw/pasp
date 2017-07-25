package com.github.pasp.tool.coder.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EntityInfo {
	private static final String ARRAY = "[]";

	private static final String JAVA_LANG = "java.lang";

	private String EntityName;

	private String moduleName;

	private String tableName;

	private FieldInfo pk;

	private List<FieldInfo> fields;

	private Set<String> importList = new HashSet<String>();

	private Set<String> entityImportList = new HashSet<String>();
	
	public String getEntityName() {
		return EntityName;
	}

	public void setEntityName(String name) {
		this.EntityName = name;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public FieldInfo getPk() {
		return pk;
	}

	public void setPk(FieldInfo pk) {
		this.pk = pk;
	}

	public List<FieldInfo> getFields() {
		return fields;
	}

	public void setFields(List<FieldInfo> fields) {
		this.fields = fields;
	}

	public Set<String> getImportList() {
		return this.importList;
	}
	
	public void processImportList(){
		if (pk != null) {
			processFieldImport( pk);
		}
		if (fields != null) {
			for (FieldInfo f : fields) {
				processFieldImport(f);
			}
		}
	}

	private void processFieldImport(FieldInfo f) {
		String im = getImport(f.getFullJavaType());
		if (im != null) {
			importList.add(im);
		}
		if (f.isVersionId()) {
			entityImportList.add("javax.persistence.Version");
		}
	}

	private String getImport(String type) {
		if (type.indexOf(".") < 0) {
			return null;
		}
		if (type.startsWith(JAVA_LANG)) {
			return null;
		}
		if (type.endsWith(ARRAY)) {
			type = type.substring(0, type.indexOf(ARRAY));
		}
		return type;
	}

	public Set<String> getEntityImportList() {
		return entityImportList;
	}

	public void setEntityImportList(Set<String> entityImportList) {
		this.entityImportList = entityImportList;
	}

}
