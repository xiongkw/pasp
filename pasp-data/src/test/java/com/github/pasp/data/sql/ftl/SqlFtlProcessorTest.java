package com.github.pasp.data.sql.ftl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.github.pasp.data.ISqlProcessor;

public class SqlFtlProcessorTest {
	private ISqlProcessor sqlProcessor = new SqlFtlProcessor();

	@Test
	public void testSetModel() {
		String tpl = "<#if set??><#list set as c>${c}<#if c_has_next>,</#if></#list></#if>";
		sqlProcessor.putTemplate("test", tpl);
		HashSet<String> set = new HashSet<String>();
		set.add("AAA");
		set.add("BBB");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("set", set);
		String s = sqlProcessor.process("test", map);
		Assert.assertEquals("AAA,BBB", s);
	}
}
