package com.github.pasp.data.sql.xml;

import javax.xml.bind.annotation.adapters.XmlAdapter;

class TrimedStringAdapter extends XmlAdapter<String, String> {

	@Override
	public String unmarshal(String v) throws Exception {
		if (v == null) {
			return null;
		}
		return v.trim();
	}

	@Override
	public String marshal(String v) throws Exception {
		return v;
	}

}
