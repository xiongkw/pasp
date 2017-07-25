package com.github.pasp.tool.coder.config;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlRootElement
public class Config {
	private Jdbc jdbc;

	private boolean integerForceLong;

	private String[] tableNamePrefixes;

	private String groupId;

	private String artifactId;

	private String version;
	
	private String paspVersion;

	private String basePackage;

	private List<Module> modules;

	private String theme = "default";

	@XmlElement
	public Jdbc getJdbc() {
		return jdbc;
	}

	public void setJdbc(Jdbc jdbc) {
		this.jdbc = jdbc;
	}

	@XmlElement
	public boolean isIntegerForceLong() {
		return integerForceLong;
	}

	public void setIntegerForceLong(boolean integerForceLong) {
		this.integerForceLong = integerForceLong;
	}

	@XmlElement
	@XmlJavaTypeAdapter(StringArrayAdapter.class)
	public String[] getTableNamePrefixes() {
		return tableNamePrefixes;
	}

	public void setTableNamePrefixes(String[] tableNamePrefixes) {
		this.tableNamePrefixes = tableNamePrefixes;
	}

	@XmlElement
	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	@XmlElement
	public String getArtifactId() {
		return artifactId;
	}

	public void setArtifactId(String artifactId) {
		this.artifactId = artifactId;
	}

	@XmlElement
	public String getPaspVersion() {
		return paspVersion;
	}

	public void setPaspVersion(String paspVersion) {
		this.paspVersion = paspVersion;
	}

	@XmlElement
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	@XmlElement(name = "module")
	public List<Module> getModules() {
		return modules;
	}

	public void setModules(List<Module> modules) {
		this.modules = modules;
	}

	@XmlElement
	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	@XmlElement
	public String getBasePackage() {
		return basePackage;
	}

	public void setBasePackage(String basePackage) {
		this.basePackage = basePackage;
	}

}
