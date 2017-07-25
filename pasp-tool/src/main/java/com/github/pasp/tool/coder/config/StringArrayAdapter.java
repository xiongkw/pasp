package com.github.pasp.tool.coder.config;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class StringArrayAdapter extends XmlAdapter<String, String[]> {

	@Override
	public String[] unmarshal(String v) throws Exception {
		return v.split(",| |;");
	}

	@Override
	public String marshal(String[] v) throws Exception {
		StringBuilder sb = new StringBuilder();
		for (String string : v) {
			sb.append(string).append(",");
		}
		return sb.deleteCharAt(sb.length() - 1).toString();
	}

}
