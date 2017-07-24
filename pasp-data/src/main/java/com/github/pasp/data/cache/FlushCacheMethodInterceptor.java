package com.github.pasp.data.cache;

import java.lang.reflect.Method;

import com.github.pasp.core.FlushCache;
import com.github.pasp.core.ICache;
import com.github.pasp.core.IEntityAware;
import org.springframework.aop.AfterReturningAdvice;


public class FlushCacheMethodInterceptor extends AbstractCacheAdvice implements AfterReturningAdvice {

	@Override
	public void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable {
		if (cacheManager == null) {
			return;
		}
		FlushCache anno = getMethodAnnotation(method, target.getClass(), FlushCache.class);
		String keyStr = anno.key();
		String[] value = anno.value();
		if (keyStr != null && keyStr.length() > 0) {
			flushByKey(value, keyStr, method, args, target);
		} else {
			flushCaches(value, target);
		}
	}

	private void flushCaches(String[] names, Object target) {
		if (names != null && names.length > 0) {
			for (String name : names) {
				clearCache(name);
			}
			return;
		}
		if (target instanceof IEntityAware) {
			String entityName = ((IEntityAware) target).getEntityName();
			if (entityName != null) {
				clearCache(entityName);
			}
		}
	}

	private void clearCache(String name) {
		ICache cache = cacheManager.getCache(name);
		if (cache != null) {
			cache.clear();
		}
	}

	private void flushByKey(String[] value, String keyStr, Method method, Object[] args, Object target) {
		if (value == null || value.length == 0) {
			if (target instanceof IEntityAware) {
				value = new String[] { ((IEntityAware) target).getEntityName() };
			}
		}
		Object key = getCacheKey(keyStr, method, args, target);
		if (key == null) {
			return;
		}
		for (String name : value) {
			evictCache(name, key);
		}
	}

	private void evictCache(String name, Object key) {
		ICache cache = getCache(name);
		if (cache != null) {
			cache.evict(key);
		}
	}

}
