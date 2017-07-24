package com.github.pasp.core;

/**
 * 缓存管理器接口
 * <p>
 * 用于获取缓存实例
 * </p>
 * 
 * @author xiongkw
 *
 */
public interface ICacheManager {

	/**
	 * 获取缓存实例
	 * <p>
	 * 获取指定名称的缓存实例
	 * </p>
	 * 
	 * @param name
	 * @return
	 */
	ICache getCache(String name);

}
