package com.github.pasp.data.sql.xml;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.github.pasp.data.ISqlLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * Sql加载器的jaxb实现
 * </p>
 * 
 * @author xiongkw
 *
 */
public class SqlLoaderJaxbImpl implements ISqlLoader {
	public static ISqlLoader INSTANCE = new SqlLoaderJaxbImpl();

	private static Logger logger = LoggerFactory.getLogger(SqlLoaderJaxbImpl.class);

	private Unmarshaller unmarshaller;

	private boolean initialized;

	@PostConstruct
	public synchronized void init() {
		if (initialized) {
			return;
		}
		try {
			JAXBContext context = JAXBContext.newInstance(SqlList.class);
			unmarshaller = context.createUnmarshaller();
		} catch (JAXBException e) {
			logger.error("Init jaxb sql loader failed!", e);
			throw new RuntimeException(e);
		}
		initialized = true;
	}

	@Override
	public Map<String, String> load(InputStream in) throws RuntimeException {
		if (!initialized) {
			init();
		}
		SqlList sqlList = null;
		try {
			sqlList = (SqlList) unmarshaller.unmarshal(in);
		} catch (JAXBException e) {
			throw new RuntimeException(e);
		}
		if (sqlList == null) {
			return null;
		}
		List<Sql> list = sqlList.getSqlList();
		if (list == null || list.size() == 0) {
			return null;
		}
		Map<String, String> map = new HashMap<String, String>();
		for (Sql sql : list) {
			map.put(sql.getId(), sql.getText());
		}
		return map;
	}

}
