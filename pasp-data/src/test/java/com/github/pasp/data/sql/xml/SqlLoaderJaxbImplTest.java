package com.github.pasp.data.sql.xml;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import com.github.pasp.data.ISqlLoader;
import org.junit.Assert;
import org.junit.Test;

public class SqlLoaderJaxbImplTest {
	private ISqlLoader sqlLoader = new SqlLoaderJaxbImpl();

	@Test
	public void testLoad() throws IOException {
		InputStream in = SqlLoaderJaxbImplTest.class.getResourceAsStream("test.sql.xml");
		Map<String, String> map = sqlLoader.load(in);
		in.close();
		Assert.assertNotNull(map);
		Assert.assertTrue(4 == map.size());

		String sql = map.get("select");
		Assert.assertNotNull(sql);
		Assert.assertEquals("select * from t_users", sql);
		sql = map.get("insert");
		Assert.assertNotNull(sql);
		sql = map.get("update");
		Assert.assertNotNull(sql);
		sql = map.get("delete");
		Assert.assertNotNull(sql);
	}
}
