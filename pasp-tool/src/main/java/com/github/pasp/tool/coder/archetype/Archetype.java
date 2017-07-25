package com.github.pasp.tool.coder.archetype;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Archetype {
	private String archetypeName;

	private List<Source> sources;

	private List<Archetype> archetypes;

	private Archetype parent;

	@XmlElement(name = "name")
	public String getArchetypeName() {
		return archetypeName;
	}

	public void setArchetypeName(String archetypeName) {
		this.archetypeName = archetypeName;
	}

	@XmlElement(name = "source")
	public List<Source> getSources() {
		return sources;
	}

	public void setSources(List<Source> sources) {
		this.sources = sources;
	}

	@XmlElement(name = "module")
	public List<Archetype> getArchetypes() {
		return archetypes;
	}

	public void setArchetypes(List<Archetype> archetypes) {
		this.archetypes = archetypes;
	}

	public Archetype getParent() {
		return parent;
	}

	public void setParent(Archetype parent) {
		this.parent = parent;
	}

	public String getArchetypePath() {
		String path = this.archetypeName;
		Archetype _this = this;
		while (_this.parent != null) {
			path = _this.parent.getArchetypeName() + "/" + path;
			_this = _this.parent;
		}
		return path;
	}
}
