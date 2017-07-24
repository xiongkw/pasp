package com.github.pasp.data.sql.parser;

import java.util.ArrayList;
import java.util.List;

public class OracleParser extends SimpleParser {
    
    @Override
    public String getPageSql(String sql) {
        sql = normalize(sql);
        StringBuilder sqlBuilder = new StringBuilder(sql.length() + 120);
        sqlBuilder.append("select * from ( select tmp_page.*, rownum row_id from ( ");
        sqlBuilder.append(sql);
        sqlBuilder.append(" ) tmp_page where rownum <= ? ) where row_id > ?");
        return sqlBuilder.toString();
    }
    
    @Override
    public List<Object> getPageParam(int page, int pageSize) {
        List<Object> params = new ArrayList<Object>();
        params.add(page * pageSize);
        params.add((page - 1) * pageSize);
        return params;
    }
    
}
