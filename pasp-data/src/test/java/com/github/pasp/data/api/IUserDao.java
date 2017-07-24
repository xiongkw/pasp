package com.github.pasp.data.api;

import com.github.pasp.core.IBaseDao;
import com.github.pasp.data.entity.User;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;

public interface IUserDao extends IBaseDao<User, Long>{

    List<User> queryByRowMapper(String sqlId, RowMapper rowMapper);
}
