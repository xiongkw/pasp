package com.github.pasp.tool.coder.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.pasp.tool.coder.archetype.Archetype;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.pasp.tool.coder.GenerateContext;
import com.github.pasp.tool.coder.IArchetypeResolver;
import com.github.pasp.tool.coder.IGenerator;
import com.github.pasp.tool.coder.ISourceStorer;
import com.github.pasp.tool.coder.ITemplateProcessor;
import com.github.pasp.tool.coder.archetype.Source;
import com.github.pasp.tool.coder.config.Config;
import com.github.pasp.tool.coder.model.EntityInfo;
import com.github.pasp.tool.util.BeanUtils;

public class Generator implements IGenerator {
	private static Logger logger = LoggerFactory.getLogger(ArchetypeResolver.class);

	private IArchetypeResolver archetypeResolver = new ArchetypeResolver();

	@Override
	public void generate(GenerateContext context, ITemplateProcessor templateProcessor, ISourceStorer sourceStorer) {
		Config config = context.getConfig();
		Archetype archetype = archetypeResolver.resolve(config.getTheme(), context, templateProcessor);
		generateArcheType(archetype, context, templateProcessor, sourceStorer);
	}

	private void generateArcheType(Archetype archetype, GenerateContext context, ITemplateProcessor templateProcessor,
			ISourceStorer sourceStorer) {
		List<Source> sources = archetype.getSources();
		if (sources == null) {
			return;
		}
		for (Source source : sources) {
			String path = source.getPath();
			try {
				generateSource(source, archetype, context, templateProcessor, sourceStorer);
				context.onSuccess(path);
			} catch (Exception e) {
				logger.warn("Generate source {} failed!", path, e);
				context.onFail(path);
			}
		}
		List<Archetype> children = archetype.getArchetypes();
		if (children != null) {
			for (Archetype child : children) {
				child.setParent(archetype);
				generateArcheType(child, context, templateProcessor, sourceStorer);
			}
		}
	}

	private void generateSource(Source source, Archetype archetype, GenerateContext context,
			ITemplateProcessor templateProcessor, ISourceStorer sourceStorer) throws Exception {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("entities", context.getEntityList());
		Config config = context.getConfig();
		BeanUtils.copyProperties(model, config);
		BeanUtils.copyProperties(model, archetype);
		String entityName = source.getEntity();
		Map<String, EntityInfo> entityMap = context.getEntityMap();
		if (entityName != null) {
			EntityInfo entityInfo = entityMap.get(entityName);
			if (entityInfo != null) {
				BeanUtils.copyProperties(model, entityInfo);
			}
		}
		String path = source.getPath();
		logger.debug("Generating source: {} ...", path);
		String template = config.getTheme() + "/" + source.getTemplate();
		String content = templateProcessor.process(template, model);
		sourceStorer.store(content, archetype.getArchetypePath() + "/" + path);
		logger.debug("Generate source: {} success!", path);
	}

}
