package com.github.pasp.coder.config;

import org.junit.Assert;
import org.junit.Test;

import com.github.pasp.tool.coder.archetype.Archetype;
import com.github.pasp.tool.coder.config.Config;
import com.github.pasp.tool.xml.IXmlParser;
import com.github.pasp.tool.xml.XmlParserJaxbImpl;

public class ConfigTest {
	private IXmlParser parser = new XmlParserJaxbImpl(Archetype.class, Config.class);

	@Test
	public void testParseConfig() {
		Config config = parser.parse(ConfigTest.class.getResourceAsStream("/config.xml"), Config.class);
		Assert.assertNotNull(config);
	}

}
