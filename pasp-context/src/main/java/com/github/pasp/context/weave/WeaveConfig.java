package com.github.pasp.context.weave;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

@XmlRootElement(name = "config")
class WeaveConfig {
	private Excludes excludes;

	@XmlElement(name = "excludes")
	public Excludes getExcludes() {
		return excludes;
	}

	public void setExcludes(Excludes excludes) {
		this.excludes = excludes;
	}

	@XmlRootElement
	public static class Excludes {
		private List<Exclude> excludes;

		@XmlElement(name = "exclude")
		public List<Exclude> getExcludes() {
			return excludes;
		}

		public void setExcludes(List<Exclude> excludes) {
			this.excludes = excludes;
		}
	}

	@XmlRootElement
	public static class Exclude {
		private String pack;

		@XmlValue
		public String getPack() {
			return pack;
		}

		public void setPack(String pack) {
			this.pack = pack;
		}

	}
}
