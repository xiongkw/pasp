package com.github.pasp.core.cache;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.github.pasp.core.ICache;
import com.github.pasp.core.ICacheManager;

public class ConcurrentMapCacheManager implements ICacheManager {
	
	private final ConcurrentMap<String, ICache> cacheMap = new ConcurrentHashMap<String, ICache>(16);
	@Override
	public ICache getCache(String name) {
		ICache cache = this.cacheMap.get(name);
		if (cache == null) {
			synchronized (this.cacheMap) {
				cache = this.cacheMap.get(name);
				if (cache == null) {
					cache = new ConcurrentMapCache();
					this.cacheMap.put(name, cache);
				}
			}
		}
		return cache;
	}

}
