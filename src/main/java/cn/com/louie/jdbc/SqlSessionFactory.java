package cn.com.louie.jdbc;

import java.sql.Connection;

/**
 * Created by Administrator on 2016/5/23.
 */
public interface SqlSessionFactory {
    SqlSession getSession();
    SqlSession openSession(Connection connection);
}
