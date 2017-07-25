package com.github.pasp.coder.archetype;

import org.junit.Assert;
import org.junit.Test;

import com.github.pasp.tool.coder.archetype.Archetype;
import com.github.pasp.tool.coder.config.Config;
import com.github.pasp.tool.xml.IXmlParser;
import com.github.pasp.tool.xml.XmlParserJaxbImpl;

public class ArchetypeTest {
	private IXmlParser parser = new XmlParserJaxbImpl(Archetype.class, Config.class);

	@Test
	public void testParseArchetype() {
		Archetype archetype = parser.parse(ArchetypeTest.class.getResourceAsStream("/archetype.xml"), Archetype.class);
		Assert.assertNotNull(archetype);
	}
}
