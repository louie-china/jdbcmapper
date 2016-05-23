package cn.com.louie.jdbc;

import java.io.Closeable;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

/**
 * Created by Administrator on 2016/5/23.
 */
public interface SqlSession extends Closeable {
    public <T> T load(Object Id,Class<T> clazz);
    public <T> T findByWhere(String sql,Object parms,Class<T> clazz);
    public <T> T findByWhereSQL(String whereSql,Object parms,Class<T> clazz);
    public <E> List<E> queryByWhere(String sql,Class<E> clazz);
    public <E> List<E> queryByWhereSQL(String sql,Class<E> clazz);
    public int utpdate(Object t);
    public int insert(Object t);
    Connection getConnection();
    void close() throws IOException;
}
