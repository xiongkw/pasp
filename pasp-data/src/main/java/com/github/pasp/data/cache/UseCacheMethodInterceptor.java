package com.github.pasp.data.cache;

import java.lang.reflect.Method;

import com.github.pasp.core.ICache;
import com.github.pasp.core.ICacheHandler;
import com.github.pasp.core.IEntityAware;
import com.github.pasp.core.UseCache;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;

public class UseCacheMethodInterceptor extends AbstractCacheAdvice implements MethodInterceptor {

	@Autowired
	private ICacheHandler cacheHandler;

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		if (cacheManager == null) {
			return invocation.proceed();
		}
		Object target = invocation.getThis();
		Method method = invocation.getMethod();
		Class<?> targetClass = AopUtils.getTargetClass(target);
		UseCache anno = getMethodAnnotation(method, targetClass, UseCache.class);
		String cacheName = getCacheName(target, anno);
		if (cacheName == null || cacheName.length() == 0) {
			return invocation.proceed();
		}
		ICache cache = getCache(cacheName);
		if (cache == null) {
			return invocation.proceed();
		}
		String keyStr = anno.key();
		if (keyStr == null || keyStr.length() == 0) {
			Object r = invocation.proceed();
			if (cacheHandler != null) {
				cacheHandler.doHandle(r, cache);
			}
			return r;
		}

		Object key = getCacheKey(invocation, keyStr);
		if (key == null) {
			return invocation.proceed();
		}

		Class<?> entityType = target instanceof IEntityAware ? ((IEntityAware) target).getEntityType() : Object.class;
		Object obj = cache.get(key, entityType);
		if (obj != null) {
			return obj;
		}

		Object result = invocation.proceed();
		if (result != null) {
			cache.put(key, result);
		}
		return result;
	}

	private String getCacheName(Object target, UseCache anno) {
		String cacheName = anno.value();
		if (cacheName == null || cacheName.length() == 0 && target instanceof IEntityAware) {
			cacheName = ((IEntityAware) target).getEntityName();
		}
		return cacheName;
	}

	protected Object getCacheKey(MethodInvocation invocation, String key) {
		Method method = invocation.getMethod();
		Object[] arguments = invocation.getArguments();
		Object root = invocation.getThis();
		return getCacheKey(key, method, arguments, root);
	}

}
