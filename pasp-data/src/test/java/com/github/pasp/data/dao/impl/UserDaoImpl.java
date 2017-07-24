package com.github.pasp.data.dao.impl;

import com.github.pasp.data.api.IUserDao;
import com.github.pasp.data.dao.BaseDao;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.github.pasp.data.entity.User;

import java.util.List;

@Component
public class UserDaoImpl extends BaseDao<User, Long> implements IUserDao {

    @Override
    public List<User> queryByRowMapper(String sqlId, RowMapper rowMapper) {
        return super.queryBySqlId(sqlId, rowMapper, null, null);
    }
}
