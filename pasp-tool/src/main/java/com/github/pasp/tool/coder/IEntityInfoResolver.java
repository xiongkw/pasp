package com.github.pasp.tool.coder;

import java.util.List;

import com.github.pasp.tool.coder.config.Config;
import com.github.pasp.tool.coder.model.EntityInfo;

public interface IEntityInfoResolver {
	
	List<EntityInfo> resolve(Config config);
	
}
