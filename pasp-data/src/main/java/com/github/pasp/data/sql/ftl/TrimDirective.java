package com.github.pasp.data.sql.ftl;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateScalarModel;

class TrimDirective implements TemplateDirectiveModel {

	private static final String PARAM_STR = "str";
	private static final String NULL_PARAM = "Directive parameter 'str' can't be null!";

	@SuppressWarnings("rawtypes")
	@Override
	public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body)
			throws TemplateException, IOException {
		if (body != null) {
			if (params == null) {
				throw new TemplateModelException(NULL_PARAM);
			}
			Object trim = params.get(PARAM_STR);
			if (trim == null) {
				throw new TemplateModelException(NULL_PARAM);
			}
			String asString = ((TemplateScalarModel) trim).getAsString();
			StringWriter writer = new StringWriter();
			body.render(writer);
			String str = trim(writer.toString(), asString);
			env.getOut().write(str);
		}
	}

	private String trim(String str, String trim) {
		if (str.trim().startsWith(trim)) {
			str = str.replaceFirst(trim, "");
		}
		if (str.trim().endsWith(trim)) {
			int index = str.lastIndexOf(trim);
			str = str.substring(0, index) + str.substring(index).replace(trim, "");
		}
		return str;
	}

}
