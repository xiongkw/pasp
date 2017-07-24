package com.github.pasp.data;

import com.github.pasp.core.Entity;
import com.github.pasp.data.dao.EntityInfo;

public interface IEntityInfoParser {
	EntityInfo parse(Class<? extends Entity<?>> cl);
}