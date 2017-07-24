package com.github.pasp.data.sql.parser;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by Administrator on 2017/3/20.
 */
public class MySqlParserTest {
    private MySqlParser parser = new MySqlParser();
    @Test
    public void testGetCountSqlSimple() throws IOException {
        String src = IOUtils.toString(MySqlParserTest.class.getResourceAsStream("count1-src.sql"));
        String result = parser.getCountSql(src);
        Assert.assertEquals(IOUtils.toString(MySqlParserTest.class.getResourceAsStream("count1-tar.sql")), result);
    }

    @Test
    public void testGetCountSqlComplex() throws IOException {
        String src = IOUtils.toString(MySqlParserTest.class.getResourceAsStream("count2-src.sql"));
        String result = parser.getCountSql(src);
        Assert.assertEquals(IOUtils.toString(MySqlParserTest.class.getResourceAsStream("count2-tar.sql")), result);
    }

    @Test
    public void testGetCountSql() throws IOException {
        String src = IOUtils.toString(MySqlParserTest.class.getResourceAsStream("count3-src.sql"));
        String result = parser.getCountSql(src);
        Assert.assertEquals(IOUtils.toString(MySqlParserTest.class.getResourceAsStream("count3-tar.sql")), result);
    }

}
