package com.github.pasp.context;

import org.junit.Assert;
import org.junit.Test;

public class AppContextTest extends BaseTest{
	@Test
	public void testGetBean(){
		AppContext bean = AppContext.getBean(AppContext.class);
		Assert.assertNotNull(bean);
	}
	
	
}
