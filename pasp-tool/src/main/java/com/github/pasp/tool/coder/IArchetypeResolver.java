package com.github.pasp.tool.coder;

import com.github.pasp.tool.coder.archetype.Archetype;

public interface IArchetypeResolver {

	Archetype resolve(String theme, GenerateContext context, ITemplateProcessor sourceResolver);

}
