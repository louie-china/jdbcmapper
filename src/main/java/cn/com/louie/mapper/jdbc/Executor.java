package cn.com.louie.mapper.jdbc;

import java.io.Closeable;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Administrator on 2016/5/23.
 */
public interface Executor extends Closeable {

    public <E> List<E> doQuery(String sql, Object parms, Class clazz) throws SQLException, IllegalAccessException, InstantiationException;

    public int updateBySQL(String sql, Object parms);

    public Object insertBySQL(String sql, Object parms);

    public List Query(String sql, Object parms);

}
