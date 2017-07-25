package com.github.pasp.tool.coder.config;

import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class StringSetAdapter extends XmlAdapter<String, Set<String>> {

	@Override
	public String marshal(Set<String> v) throws Exception {
		StringBuilder sb = new StringBuilder();
		for (String string : v) {
			sb.append(string).append(",");
		}
		return sb.deleteCharAt(sb.length() - 1).toString();
	}

	@Override
	public Set<String> unmarshal(String v) throws Exception {
		Set<String> set = new HashSet<String>();
		String[] split = v.split(",| |;");
		for (String string : split) {
			set.add(string);
		}
		return set;
	}

}
