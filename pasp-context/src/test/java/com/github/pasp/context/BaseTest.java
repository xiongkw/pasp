package com.github.pasp.context;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:conf/spring/pasp-context.spring.xml" })
public class BaseTest {

	@Ignore
	@Test
	public void testNothing() {

	}

}
