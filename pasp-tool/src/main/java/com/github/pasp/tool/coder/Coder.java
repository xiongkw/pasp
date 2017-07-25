package com.github.pasp.tool.coder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.pasp.tool.coder.impl.Generator;
import com.github.pasp.tool.coder.impl.SourceStorer;
import com.github.pasp.tool.coder.impl.TemplateProcessor;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.pasp.tool.coder.config.Config;
import com.github.pasp.tool.coder.impl.ConfigLoader;
import com.github.pasp.tool.coder.impl.EntityInfoResolver;
import com.github.pasp.tool.coder.model.EntityInfo;

import freemarker.cache.ClassTemplateLoader;

public class Coder {
	private static Logger logger = LoggerFactory.getLogger(Coder.class);

	private static String userdir = System.getProperty("user.dir").replaceAll("\\\\", "/");

	private IConfigLoader configLoader = new ConfigLoader(userdir);

	private IGenerator generator = new Generator();

	private ITemplateProcessor templateProcessor = new TemplateProcessor(
			new ClassTemplateLoader(Coder.class.getClassLoader(), "themes"));

	private IEntityInfoResolver entityInfoResolver = new EntityInfoResolver();

	private SourceStorer sourceStorer;

	public void setConfigLoader(IConfigLoader configLoader) {
		this.configLoader = configLoader;
	}

	public void setGenerator(IGenerator generator) {
		this.generator = generator;
	}

	public void setSourceResolver(ITemplateProcessor sourceResolver) {
		this.templateProcessor = sourceResolver;
	}

	public void setEntityInfoResolver(IEntityInfoResolver entityInfoResolver) {
		this.entityInfoResolver = entityInfoResolver;
	}

	public void setSourceStorer(SourceStorer sourceStorer) {
		this.sourceStorer = sourceStorer;
	}

	public void code(String[] args) {
		logger.debug("开始生成代码... ");
		boolean clean = false;
		String outputDir = userdir + "/output";
		String configFile = userdir + "/config.xml";
		for (int i = 0; i < args.length; i++) {
			if ("-clean".equalsIgnoreCase(args[i])) {
				clean = true;
			} else if ("-output".equalsIgnoreCase(args[i])) {
				if (args.length < ++i + 1) {
					logger.error("请输入输入文件目录，默认为当前目录");
					return;
				}
				outputDir = args[i];
			} else if ("-config".equalsIgnoreCase(args[i])) {
				if (args.length < ++i + 1) {
					logger.error("请输入配置文件(参数： -config)");
					return;
				}
				configFile = args[i];
			}
		}
		if (clean) {
			logger.debug("清空输出目录: {}", outputDir);
			try {
				File directory = new File(outputDir);
				if (directory.exists()) {
					FileUtils.cleanDirectory(directory);
				}
			} catch (IOException e) {
				logger.warn("清空输出目录失败！", e);
			}
		}

		try {
			Config config = configLoader.load(configFile);
			if (config == null) {
				logger.error("配置文件: {} 解析失败!", configFile);
				return;
			}
			sourceStorer = new SourceStorer(outputDir);

			Map<String, EntityInfo> entityMap = new HashMap<String, EntityInfo>();

			List<EntityInfo> entities = entityInfoResolver.resolve(config);
			for (EntityInfo entityInfo : entities) {
				entityMap.put(entityInfo.getEntityName(), entityInfo);
			}

			GenerateContext context = new GenerateContext();
			context.setConfig(config);
			context.setEntityList(entities);
			context.setEntityMap(entityMap);

			generator.generate(context, templateProcessor, sourceStorer);

			List<String> fail = context.getFail();
			logger.debug("代码生成完成！成功生成文件：{} 个，失败：{} 个。", context.getSuccess().size(), fail.size());
			if (fail.size() > 0) {
				logger.debug("失败文件: ");
				for (String string : fail) {
					logger.debug(string);
				}
			}
		} catch (FileNotFoundException e) {
			logger.error("配置文件: {} 不存在!", configFile);
		} catch (Exception e) {
			logger.error("代码生成失败!", e);
		}
	}

}
