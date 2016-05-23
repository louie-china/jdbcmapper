package cn.com.louie.jdbc;

import java.io.Closeable;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Administrator on 2016/5/23.
 */
public interface Executor extends Closeable {

    public <E> List<E> doQuery(String sql,Object parms,Class clazz) throws SQLException, IllegalAccessException, InstantiationException;

    public int doUpdate(Object o);

}
