package cn.com.louie.jdbc;

import java.io.Closeable;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Louie on 2016/5/23.
 */

/**
 * 执行器
 */
public interface Executor extends Closeable {
    /**
     * 根据Sql返回实体列表
     *
     * @param sql   sql
     * @param parms 参数
     * @param clazz 类型
     * @param <E>
     * @return 实体列表
     * @throws SQLException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public <E> List<E> doQuery(String sql, Object parms, Class clazz) throws SQLException, IllegalAccessException, InstantiationException;

    /**
     * 根据sql更新
     *
     * @param sql   sql语句
     * @param parms 参数
     * @return 执行行数
     */
    public int updateBySQL(String sql, Object parms);

    /**
     * 根据sql插入
     *
     * @param sql   sql语句
     * @param parms 参数
     * @return 主键
     */
    public Object insertBySQL(String sql, Object parms);

    /**
     * 根据sql查询返回map列表
     * @param sql sql语句
     * @param parms 参数
     * @return
     */
    public List Query(String sql, Object parms);

    void close();
}
