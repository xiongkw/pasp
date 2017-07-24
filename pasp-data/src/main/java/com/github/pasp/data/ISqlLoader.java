package com.github.pasp.data;

import java.io.InputStream;
import java.util.Map;

/**
 * <p>
 * Interface of sql loader
 * </p>
 * 
 * @author xiongkw
 *
 */
public interface ISqlLoader {

	/**
	 * <p>
	 * 加载sql到map<id,sql>
	 * </p>
	 * 
	 * @param in
	 * @return
	 */
	Map<String, String> load(InputStream in);

}
