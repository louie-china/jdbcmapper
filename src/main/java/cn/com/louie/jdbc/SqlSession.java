package cn.com.louie.jdbc;

import java.io.Closeable;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

/**
 * Created by Louie on 2016/5/23.
 */

/**
 * 执行器
 */
public interface SqlSession extends Closeable {
    /**
     * 根据主键查实体
     * @param Id  主键
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T load(Object Id, Class<T> clazz);

    /**
     * 根据原生SQL查实体
     * @param sql  sql语句
     * @param parms  条件参数赋值，如果sql里带有值的话则传NULL即可
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T findBySQL(String sql, Object parms, Class<T> clazz);

    /**
     * 根据封装的WhereSQL查实体
     * @param whereSql  SQL的wehere语句后半段
     * @param parms 参数值
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T findByWhereSQL(String whereSql, Object parms, Class<T> clazz);

    /**
     * 根据原生SQL查实体列表
     * @param sql sql语句
     * @param parms 参数值
     * @param clazz
     * @param <E>
     * @return
     */
    public <E> List<E> queryBySQL(String sql, Object parms, Class<E> clazz);
    /**
     * 根据封装的WhereSQL查实体列表
     * @param sql  SQL的wehere语句后半段
     * @param parms 参数值
     * @param clazz
     * @param <E>
     * @return
     */
    public <E> List<E> queryByWhereSQL(String sql, Object parms, Class<E> clazz);

    /**
     * 根据SQL查数据库返回一个List
     * @param sql sql语句
     * @param parms 参数
     * @return
     */
    public List queryBySQL(String sql, Object parms);

    /**
     * 根据sql查询返回map
     * @param sql sql语句
     * @param parms 参数
     * @return
     */
    public Map<String, Object> findBySQL(String sql, Object parms);

    /**
     * 根据原生更新数据
     * @param sql  原生sql
     * @param parms 参数
     * @return
     */
    public int updateBySQL(String sql, Object parms);

    /**
     * 根据实体更新 必须设置主键
     * @param t  实体
     * @return
     */
    public int update(Object t);

    /**
     * 根据实体插入数据  自增返回id
     * @param t 实体
     * @return  主键自增值
     */
    public Object insert(Object t);

    /**
     * 根据原生sql插入数据
     * @param sql 原生sql
     * @param parms 参数值
     * @return
     */
    public Object insertBySQL(String sql, Object parms,boolean isG);

    /**
     * 获取连接
     * @return
     */
    Connection getConnection();

    void close();
}
