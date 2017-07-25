package com.github.pasp.tool.coder;

public interface IGenerator {

	void generate(GenerateContext context, ITemplateProcessor sourceResolver, ISourceStorer sourceStorer);

}
