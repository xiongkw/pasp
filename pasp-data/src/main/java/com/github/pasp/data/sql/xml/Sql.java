package com.github.pasp.data.sql.xml;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlRootElement
class Sql {

	private String id;

	private String text;

	@XmlAttribute
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@XmlValue
	@XmlJavaTypeAdapter(TrimedStringAdapter.class)
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
