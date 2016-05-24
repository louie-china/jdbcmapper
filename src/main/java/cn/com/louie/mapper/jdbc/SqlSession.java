package cn.com.louie.mapper.jdbc;

import java.io.Closeable;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/5/23.
 */
public interface SqlSession extends Closeable {
    public <T> T load(Object Id, Class<T> clazz);
    public <T> T findBySQL(String sql, Object parms, Class<T> clazz);
    public <T> T findByWhereSQL(String whereSql, Object parms, Class<T> clazz);
    public <E> List<E> queryBySQL(String sql, Object parms, Class<E> clazz);
    public <E> List<E> queryByWhereSQL(String sql, Object parms, Class<E> clazz);
    public List queryBySQL(String sql, Object parms);
    public Map<String,Object> findBySQL(String sql, Object parms);
    public int updateBySQL(String sql, Object parms);
    public int update(Object t);
    public Object insert(Object t);
    public Object insertBySQL(String sql, Object parms);
    Connection getConnection();
    void close() throws IOException;
}
