package com.github.pasp.tool.coder;

public interface ITemplateProcessor {
	
	String process(String template, Object model) throws Exception;
	
}
