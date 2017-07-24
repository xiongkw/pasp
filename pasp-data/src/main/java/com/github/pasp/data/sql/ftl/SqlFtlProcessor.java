package com.github.pasp.data.sql.ftl;

import java.io.StringWriter;
import java.text.MessageFormat;

import javax.annotation.PostConstruct;

import com.github.pasp.core.exception.DataAccessException;
import com.github.pasp.data.IErrorConstants;
import com.github.pasp.data.ISqlProcessor;
import com.github.pasp.data.utils.StreamUtils;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateNotFoundException;

/**
 * <p>
 * Sql模板处理器的ftl实现
 * </p>
 * 
 * @author xiongkw
 *
 */
public class SqlFtlProcessor implements ISqlProcessor {
	private Configuration cfg;
	private StringTemplateLoader templateLoader;

	private boolean initialized;

	@PostConstruct
	public synchronized void init() {
		if (initialized) {
			return;
		}
		cfg = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
		cfg.setNumberFormat("#");
		cfg.setSharedVariable("Trim", new TrimDirective());
		cfg.setAutoFlush(false);
		templateLoader = new StringTemplateLoader();
		cfg.setTemplateLoader(templateLoader);
		initialized = true;
	}

	@Override
	public String process(String sqlId, Object model) throws DataAccessException {
		if (!initialized) {
			init();
		}
		StringWriter out = null;
		try {
			Template template = cfg.getTemplate(sqlId);
			out = new StringWriter();
			template.process(model, out);
			out.flush();
			return out.toString();
		} catch (TemplateNotFoundException e) {
			throw new DataAccessException(MessageFormat.format(IErrorConstants.ERR_SQL_FTL_NOT_FOUND, sqlId));
		} catch (Exception e) {
			throw new DataAccessException(MessageFormat.format(IErrorConstants.ERR_SQL_PROCESS, e.getMessage()));
		} finally {
			StreamUtils.closeQuietly(out);
		}
	}

	@Override
	public void putTemplate(String sqlId, String text) {
		if (!initialized) {
			init();
		}
		templateLoader.putTemplate(sqlId, text);
	}

}
