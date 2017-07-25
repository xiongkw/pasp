package com.github.pasp.tool.coder.archetype;

import javax.xml.bind.annotation.XmlElement;

public class Source {
	private String path;

	private String template;

	private String entity;

	@XmlElement
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@XmlElement
	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	@XmlElement
	public String getEntity() {
		return entity;
	}

	public void setEntity(String entity) {
		this.entity = entity;
	}

}
