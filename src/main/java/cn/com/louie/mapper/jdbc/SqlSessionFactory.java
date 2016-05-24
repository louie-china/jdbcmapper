package cn.com.louie.mapper.jdbc;

/**
 * Created by Administrator on 2016/5/23.
 */
public interface SqlSessionFactory {
    SqlSession getSession();
    SqlSession getSession(boolean cache);
}
