package com.github.pasp.tool.coder.impl;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;

import com.github.pasp.tool.coder.GenerateContext;
import com.github.pasp.tool.coder.IArchetypeResolver;
import com.github.pasp.tool.coder.ITemplateProcessor;
import com.github.pasp.tool.coder.archetype.Archetype;
import com.github.pasp.tool.coder.config.Config;
import com.github.pasp.tool.util.BeanUtils;
import com.github.pasp.tool.xml.IXmlParser;
import com.github.pasp.tool.xml.XmlParserJaxbImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ArchetypeResolver implements IArchetypeResolver {
	private static Logger logger = LoggerFactory.getLogger(ArchetypeResolver.class);

	private static final String ARCHETYPE_FTL = "/archetype.ftl";

	private IXmlParser parser = new XmlParserJaxbImpl(Archetype.class);

	@Override
	public Archetype resolve(String theme, GenerateContext context, ITemplateProcessor templateProcessor) {
		Map<String, Object> map = new HashMap<String, Object>();
		Config config = context.getConfig();
		map.put("entities", context.getEntityList());
		try {
			BeanUtils.copyProperties(map, config);
			String content = templateProcessor.process(theme + ARCHETYPE_FTL, map);
			ByteArrayInputStream in = new ByteArrayInputStream(content.getBytes());
			return parser.parse(in, Archetype.class);
		} catch (Exception e) {
			logger.error("Resolve archetype failed!", e);
			return null;
		}
	}

}
