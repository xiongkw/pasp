package com.github.pasp.core;

/**
 * <p>
 * 对象解析接口，用于解析string为对象
 * </p>
 * 
 * @author xiongkw
 *
 */
public interface IObjectMapper {

	<T> T mapper(String content, Class<T> cl);

}
