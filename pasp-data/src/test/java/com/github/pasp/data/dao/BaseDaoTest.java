package com.github.pasp.data.dao;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.github.pasp.data.api.IUserDao;
import com.github.pasp.data.entity.User;
import org.hsqldb.lib.InOutUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StreamUtils;

import com.github.pasp.core.Page;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:conf/spring/pasp-data.spring.xml",
		"classpath*:conf/spring/pasp-data-test.spring.xml" })
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional
public class BaseDaoTest {
	@Autowired
	private IUserDao userDao;

	@Autowired
	private DataSource dataSource;

	private static boolean initialized = false;

	@Before
	public void before() throws Exception {
		if (initialized) {
			return;
		}
		Statement stmt = dataSource.getConnection().createStatement();
		InputStream in = BaseDaoTest.class.getResourceAsStream("/hsqldb-init.sql");
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		InOutUtil.copy(in, bos);
		stmt.execute(bos.toString());
		stmt.close();
		initialized = true;
	}

	@Test
	public void testInsertAndSelectByPrimaryKey() {
		User user = new User();
		user.setAge(30);
		user.setUserName("Tom");
		user.setRegion(0L);
		Long id = userDao.insert(user);
		assertNotNull(id);
		user.setId(null);
		Long id2 = userDao.insert(user);
		assertNotEquals(id.longValue(), id2.longValue());

		user.setId(id);
		User selectByUser = userDao.selectByPrimaryKey(user);
		assertNotNull(selectByUser);
		assertEquals("Tom", selectByUser.getUserName());
		assertEquals(30, selectByUser.getAge().intValue());
		assertTrue(0 == selectByUser.getRegion());

		User selectById = userDao.selectByPrimaryKey(id);
		assertNotNull(selectById);

	}

	@Test
	public void testUpdateByPrimaryKey() {
		User user = new User();
		user.setAge(30);
		user.setUserName("Tom");
		user.setRegion(1L);
		user.setLock(0L);
		Long id = userDao.insert(user);
		assertNotNull(id);

		user.setUserName("Jerry");
		user.setAge(25);
		user.setLock(0L);
		int r = userDao.updateByPrimaryKey(user);
		assertTrue(r == 1);

		User s = userDao.selectByPrimaryKey(id);
		assertNotNull(s);
		assertEquals("Jerry", s.getUserName());
		assertTrue(1L == s.getLock());
		assertTrue(25 == s.getAge());

		user.setUserName(null);
		user.setLock(1L);
		r = userDao.updateByPrimaryKey(user);
		assertTrue(r == 1);
		s = userDao.selectByPrimaryKey(id);
		assertNotNull(s);
		assertEquals(null, s.getUserName());
		assertTrue(2L == s.getLock());
	}

	@Test
	public void testUpdateSelectiveByPrimaryKey() {
		User user = new User();
		user.setAge(30);
		user.setUserName("Tom");
		user.setRegion(1L);
		user.setLock(100L);
		Long id = userDao.insert(user);
		assertNotNull(id);

		user.setUserName("Jerry");
		user.setAge(25);
		int r = userDao.updateSelectiveByPrimaryKey(user);
		assertTrue(r == 1);

		User s = userDao.selectByPrimaryKey(id);
		assertNotNull(s);
		assertEquals("Jerry", s.getUserName());
		assertTrue(25 == s.getAge());
		assertTrue(101L == s.getLock());

		user.setUserName(null);
		user.setLock(101L);
		r = userDao.updateSelectiveByPrimaryKey(user);
		assertTrue(r == 1);
		s = userDao.selectByPrimaryKey(id);
		assertNotNull(s);
		assertEquals("Jerry", s.getUserName());
		assertTrue(102L == s.getLock());
	}

	@Test
	public void testDeleteByPrimaryKey() {
		User user = new User();
		user.setAge(30);
		user.setUserName("Tom");
		user.setRegion(1L);
		user.setLock(100L);
		Long id = userDao.insert(user);
		assertNotNull(id);

		int r = userDao.deleteByPrimaryKey(user);
		assertTrue(r == 1);

		User s = userDao.selectByPrimaryKey(id);
		assertNull(s);
	}

	@Test
	public void testSelectByExample() {
		assertBatchInsert();

		User example = new User();
		example.setUserName("Tom");
		List<User> list = userDao.selectByExample(example);
		assertTrue(list.size() == 1);
		assertEquals("Tom", list.get(0).getUserName());
	}

	@Test
	public void testJdbcGetSize() {
		assertBatchInsert();

		User example = new User();
		example.setUserName("Tom");
		long size = userDao.count(example);
		assertTrue(size == 1);

		example.setRegion(1L);
		example.setUserName(null);
		size = userDao.count(example);
		assertTrue(size == 2);

		size = userDao.count("Select * from t_user", null);
		assertTrue(size == 2);
	}

	@Test
	public void testJdbcSelectFirstRow() {
		assertBatchInsert();

		User example = new User();
		example.setRegion(1L);
		User s = userDao.selectFirstByExample(example);
		assertNotNull(s);
		assertEquals("Tom", s.getUserName());
		assertEquals(30, s.getAge().intValue());
	}

	@Test
	public void testJdbcBatchInsert() {
		assertBatchInsert();

		User example = new User();
		example.setRegion(1L);
		List<User> l = userDao.selectByExample(example);
		assertNotNull(l);
		assertTrue(2 == l.size());
	}

	@Test
	public void testJdbcBatchUpdate() {
		assertBatchInsert();

		User example = new User();
		example.setRegion(1L);
		List<User> l = userDao.selectByExample(example);
		assertNotNull(l);
		assertTrue(2 == l.size());

		User user3 = l.get(0);
		user3.setUserName("Tomcat");
		User user4 = l.get(1);
		user4.setUserName("Jerry Mouse");
		int[] r = userDao.batchUpdateByPrimaryKey(l);
		assertArrayEquals(new int[] { 1, 1 }, r);

		l = userDao.selectByExample(example);
		assertNotNull(l);
		assertTrue(2 == l.size());
		assertEquals("Tomcat", l.get(0).getUserName());
		assertEquals("Jerry Mouse", l.get(1).getUserName());
	}

	@Test
	public void testJdbcBatchDelete() {
		assertBatchInsert();

		User example = new User();
		example.setRegion(1L);
		List<User> l = userDao.selectByExample(example);
		assertNotNull(l);
		assertTrue(2 == l.size());

		int[] r = userDao.batchDeleteByPrimary(l);
		assertArrayEquals(new int[] { 1, 1 }, r);

		l = userDao.selectByExample(example);
		assertNotNull(l);
		assertTrue(0 == l.size());
	}

	@Test
	public void testUpdateByExample() {
		assertBatchInsert();

		User entity = new User();
		entity.setUserName("Kity");
		entity.setRegion(1L);

		User example = new User();
		example.setRegion(1L);
		int updateByExample = userDao.updateByExample(entity, example);
		assertTrue(2 == updateByExample);

		List<User> l = userDao.selectByExample(example);
		assertNotNull(l);
		assertTrue(2 == l.size());
		assertEquals("Kity", l.get(0).getUserName());
		assertEquals("Kity", l.get(1).getUserName());

		assertTrue(null == l.get(0).getAge());
		assertTrue(null == l.get(1).getAge());
	}

	@Test
	public void testUpdateSelectiveByExample() {
		assertBatchInsert();

		User entity = new User();
		entity.setUserName("Kity");

		User example = new User();
		example.setRegion(1L);
		int updateByExample = userDao.updateSelectiveByExample(entity, example);
		assertTrue(2 == updateByExample);

		List<User> l = userDao.selectByExample(example);
		assertNotNull(l);
		assertTrue(2 == l.size());
		assertEquals("Kity", l.get(0).getUserName());
		assertEquals("Kity", l.get(1).getUserName());

		assertTrue(30 == l.get(0).getAge());
		assertTrue(25 == l.get(1).getAge());
	}

	@Test
	public void testDeleteByExample() {
		assertBatchInsert();

		User example = new User();
		example.setRegion(1L);
		int deleteByExample = userDao.deleteByExample(example);
		assertTrue(2 == deleteByExample);

		List<User> l = userDao.selectByExample(example);
		assertNotNull(l);
		assertTrue(0 == l.size());
	}

	private void assertBatchInsert() {
		ArrayList<User> list = new ArrayList<User>();
		User user = new User();
		user.setAge(30);
		user.setUserName("Tom");
		user.setRegion(1L);
		user.setLock(100L);
		list.add(user);

		User user2 = new User();
		user2.setAge(25);
		user2.setUserName("Jerry");
		user2.setRegion(1L);
		user2.setLock(100L);
		list.add(user2);

		int[] r = userDao.batchInsert(list);
		assertArrayEquals(new int[] { 1, 1 }, r);
	}

	@Test
	public void testJdbcFindPageInfo() {
		assertBatchInsert();

		User entity = new User();
		entity.setAge(25);
		Page<User> page = userDao.jdbcFindPageInfo(entity, 0, 20);
		assertNotNull(page);
		assertTrue(page.getTotal() == 1);
		List<User> list = page.getList();
		assertNotNull(list);
		assertTrue(1 == list.size());
		assertEquals("Jerry", list.get(0).getUserName());
	}
	
	@Test
	public void testGetEntityBySqlId() {
		assertBatchInsert();

		User user = userDao.getBySqlId("getByName", new Object[]{"Tom"});
		assertNotNull(user);
		assertTrue(30==user.getAge());
		assertEquals("Tom", user.getUserName());
	}

	@Test
	public void testCountBySqlId(){
		int count = userDao.countBySqlId("count", null, null);
		assertTrue(count >=0 );
	}

	@Test
	public void testQueryBySqlId() {
		assertBatchInsert();

		User entity = new User();
		entity.setAge(25);
		entity.setUserName("e");
		List<User> list = userDao.queryBySqlId("queryUserDynamic", entity);
		assertNotNull(list);
		assertTrue(1 == list.size());
		assertEquals("Jerry", list.get(0).getUserName());
	}

	@Test
	public void testRowMapper(){
		assertBatchInsert();
		List<User> list = userDao.queryByRowMapper("query", new RowMapper() {
			@Override
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				return null;
			}
		});
		assertTrue(list.size()==0);

	}
	
	@Test
	public void testBlob() throws IOException{
		byte[] img = StreamUtils.copyToByteArray(BaseDaoTest.class.getResourceAsStream("/fileblob/imgBlob.jpg"));
		String txt = StreamUtils.copyToString(BaseDaoTest.class.getResourceAsStream("/fileblob/txtBlob.txt"), Charset.forName("UTF-8"));
		User user = new User();
		user.setAge(30);
		user.setUserName("Tom");
		user.setRegion(1L);
		user.setImage(img);
		user.setRemark(txt);
		Long id = userDao.insert(user);
		User u = userDao.selectByPrimaryKey(id);
		assertEquals(txt, u.getRemark());
		assertArrayEquals(img, u.getImage());
	}
	
}
