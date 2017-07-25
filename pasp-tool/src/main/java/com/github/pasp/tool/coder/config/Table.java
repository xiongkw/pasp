package com.github.pasp.tool.coder.config;

import java.util.Set;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

public class Table {
	private String name;

	private Set<String> shardingIds;

	private String versionId;

	@XmlValue
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlAttribute
	@XmlJavaTypeAdapter(StringSetAdapter.class)
	public Set<String> getShardingIds() {
		return shardingIds;
	}

	public void setShardingIds(Set<String> shardingIds) {
		this.shardingIds = shardingIds;
	}

	@XmlAttribute
	public String getVersionId() {
		return versionId;
	}

	public void setVersionId(String versionId) {
		this.versionId = versionId;
	}

}
