package com.dao;

import com.config.JdbcConfig;
import com.domain.User;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.util.List;

public class IUserDaoImpl {

    QueryRunner runner = new QueryRunner(JdbcConfig.getDataSource());

    public List<User> listUsers(){
        try {
            return runner.query("select * from user", new BeanListHandler<User>(User.class));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
