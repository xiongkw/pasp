package com.github.pasp.tool.coder.impl;

import java.io.StringWriter;

import com.github.pasp.tool.coder.ITemplateProcessor;
import org.apache.commons.io.IOUtils;

import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;

public class TemplateProcessor implements ITemplateProcessor {
	private static final String UTF_8 = "UTF-8";
	private Configuration cfg;

	public TemplateProcessor(TemplateLoader templateLoader) {
		cfg = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
		cfg.setTemplateLoader(templateLoader);
		cfg.setDefaultEncoding(UTF_8);
		cfg.setOutputEncoding(UTF_8);
	}

	public String process(String tpl, Object model) throws Exception {
		StringWriter writer = new StringWriter();
		try {
			Template template = cfg.getTemplate(tpl, "UTF-8");
			template.process(model, writer);
			writer.flush();
			return writer.toString();
		} finally {
			IOUtils.closeQuietly(writer);
		}
	}

}
