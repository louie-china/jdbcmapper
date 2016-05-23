package cn.com.louie.jdbc;

import java.sql.Connection;

/**
 * Created by Administrator on 2016/5/23.
 */
public class DefaultSqlSessionFactory implements SqlSessionFactory {
    public SqlSession getSession() {
//        return new DefaultSqlSession();
        return new DefaultSqlSession(new DefaultExecutor());
    }

    public SqlSession openSession(Connection connection) {

        return null;
    }
}
