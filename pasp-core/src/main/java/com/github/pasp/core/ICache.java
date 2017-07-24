package com.github.pasp.core;

import java.util.Set;

/**
 * 缓存接口
 * 
 * @author xiongkw
 *
 */
public interface ICache {

	/**
	 * 获取缓存对象
	 * <p>
	 * 获取指定key和对象类型的缓存对象
	 * </p>
	 * 
	 * @param key
	 * @param type
	 * @return
	 */
	<T> T get(Object key, Class<T> type);

	/**
	 * 设置缓存对象
	 * 
	 * @param key
	 * @param value
	 */
	void put(Object key, Object value);

	/**
	 * 清除缓存对象
	 * <p>
	 * 指定key清除缓存对象
	 * </p>
	 * 
	 * @param key
	 */
	void evict(Object key);

	/**
	 * 清空缓存
	 */
	void clear();
	
	Set<Object> keys();
	
}
