package com.github.pasp.core.cache;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.github.pasp.core.ICache;

public class ConcurrentMapCache implements ICache {
	
	private final ConcurrentMap<Object, Object> store = new ConcurrentHashMap<Object,Object>();
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> T get(Object key, Class<T> type) {
		return (T) store.get(key);
	}

	@Override
	public void put(Object key, Object value) {
		this.store.put(key, value);
	}

	@Override
	public void evict(Object key) {
		this.store.remove(key);
	}

	@Override
	public void clear() {
		this.store.clear();
	}

	@Override
	public Set<Object> keys() {
		return this.store.keySet();
	}

}
