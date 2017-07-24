package com.github.pasp.data.cache;

import com.github.pasp.core.Entity;
import com.github.pasp.core.ICache;
import com.github.pasp.core.ICacheHandler;
import com.github.pasp.core.Page;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class DefaultCacheHandler implements ICacheHandler {

	@Override
	public void doHandle(Object obj, ICache cache) {
		if (obj == null || cache == null) {
			return;
		}
		if (obj instanceof Entity) {
			handleEntity((Entity<?>) obj, cache);
			return;
		}
		if (obj instanceof Collection) {
			handleCollection((Collection<?>) obj, cache);
			return;
		}
		if (obj instanceof Map) {
			handleMap((Map<?, ?>) obj, cache);
			return;
		}
		if (obj instanceof Page) {
			handlePageInfo((Page<?>) obj, cache);
		}

	}

	private void handlePageInfo(Page<?> pageInfo, ICache cache) {
		List<?> list = pageInfo.getList();
		if (list != null) {
			handleCollection(list, cache);
		}
	}

	private void handleEntity(Entity<?> e, ICache cache) {
		cache.put(e.getId(), e);
	}

	private void handleMap(Map<?, ?> map, ICache cache) {
		if (map.size() == 0) {
			return;
		}
		Iterator<?> iterator = map.values().iterator();
		while (iterator.hasNext()) {
			doHandle(iterator.next(), cache);
		}
	}

	private void handleCollection(Collection<?> c, ICache cache) {
		if (c.size() == 0) {
			return;
		}
		for (Object object : c) {
			doHandle(object, cache);
		}
	}
}
