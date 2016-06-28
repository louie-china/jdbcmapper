package cn.com.louie.jdbc;

import cn.com.louie.config.Configuration;
import cn.com.louie.mapper.EOUtil;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by Louie on 2016/5/23.
 */

/**
 * 默认的SQLSession
 */
public class DefaultSqlSession implements SqlSession {

    private Executor executor;

    public DefaultSqlSession(Executor executor) {
        this.executor = executor;
    }

    /**
     * 根据主键查实体
     *
     * @param Id    主键
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T load(Object Id, Class<T> clazz) {
        EOUtil eoUtil = Configuration.getInstans().eoutils.get(clazz);
        if (eoUtil == null)
            throw new EntityNotFoundException("需要在实体上加入@Entity标注");
        String sql = eoUtil.buildSelect(eoUtil.primaryKey + "=?");
        return getOne(sql, Id, clazz);
    }

    /**
     * 根据原生SQL查实体
     *
     * @param sql   sql语句
     * @param parms 条件参数赋值，如果sql里带有值的话则传NULL即可
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T findBySQL(String sql, Object parms, Class<T> clazz) {
        return getOne(sql, parms, clazz);
    }

    /**
     * 根据封装的WhereSQL查实体
     *
     * @param whereSql SQL的wehere语句后半段
     * @param parms    参数值
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T findByWhereSQL(String whereSql, Object parms, Class<T> clazz) {
        EOUtil eoUtil = Configuration.getInstans().eoutils.get(clazz);
        if (eoUtil == null)
            throw new EntityNotFoundException("需要在实体上加入@Entity标注");
        String sql = eoUtil.buildSelect(whereSql);
        return getOne(sql, parms, clazz);
    }

    /**
     * 根据原生SQL查实体列表
     *
     * @param sql   sql语句
     * @param parms 参数值
     * @param clazz
     * @param <E>
     * @return
     */

    public <E> List<E> queryBySQL(String sql, Object parms, Class<E> clazz) {
        return getList(sql, parms, clazz);
    }

    /**
     * 根据封装的WhereSQL查实体列表
     *
     * @param whereSql SQL的wehere语句后半段
     * @param parms    参数值
     * @param clazz
     * @param <E>
     * @return
     */
    public <E> List<E> queryByWhereSQL(String whereSql, Object parms, Class<E> clazz) {
        EOUtil eoUtil = Configuration.getInstans().eoutils.get(clazz);
        if (eoUtil == null)
            throw new EntityNotFoundException("需要在实体上加入@Entity标注");
        String sql = eoUtil.buildSelect(whereSql);
        return getList(sql, parms, clazz);
    }

    /**
     * 根据SQL查数据库返回一个List
     *
     * @param sql   sql语句
     * @param parms 参数
     * @return
     */
    public List queryBySQL(String sql, Object parms) {
        return executor.Query(sql, parms);
    }

    /**
     * 根据sql查询返回map
     *
     * @param sql   sql语句
     * @param parms 参数
     * @return
     */
    public Map<String, Object> findBySQL(String sql, Object parms) {
        List list = executor.Query(sql, parms);
        return CollectionUtils.isEmpty(list) ? null : (Map<String, Object>) list.get(0);
    }

    /**
     * 根据原生更新数据
     *
     * @param sql   原生sql
     * @param parms 参数
     * @return
     */
    public int updateBySQL(String sql, Object parms) {
        return executor.updateBySQL(sql, parms);
    }

    /**
     * 根据实体更新 必须设置主键
     *
     * @param t 实体
     * @return
     */
    public int update(Object t) {
        EOUtil eoUtil = Configuration.getInstans().eoutils.get(t.getClass());
        if (eoUtil == null)
            throw new EntityNotFoundException("需要在实体上加入@Entity标注");
        String sql = eoUtil.buildUpdate(null, t);
        return executor.updateBySQL(sql, null);
    }

    /**
     * 根据实体插入数据  自增返回id
     *
     * @param t 实体
     * @return 主键自增值
     */
    public Object insert(Object t) {
        EOUtil eoUtil = Configuration.getInstans().eoutils.get(t.getClass());
        if (eoUtil == null)
            throw new EntityNotFoundException("需要在实体上加入@Entity标注");
        String sql = eoUtil.buildInsert(t);
        if (eoUtil.isGenerate(eoUtil.primaryKey))
            return executor.insertBySQL(sql, null);
        else {
            executor.insertBySQLNoId(sql, null);
            return null;
        }
    }

    /**
     * 根据原生sql插入数据
     *
     * @param sql   原生sql
     * @param parms 参数值
     * @return
     */
    public Object insertBySQL(String sql, Object parms, boolean isG) {
        if (isG)
            return executor.insertBySQL(sql, parms);
        else {
            executor.insertBySQLNoId(sql, parms);
            return null;
        }
    }

    /**
     * 获取连接
     *
     * @return
     */
    public Connection getConnection() {
        try {
            return Configuration.getInstans().dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 调用executor查询
     *
     * @param sql  sql语句
     * @param parm 参数
     * @param c    类型
     * @param <E>
     * @return
     */
    private <E> List<E> getList(String sql, Object parm, Class c) {
        try {
            List list = executor.doQuery(sql, parm, c);
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }

    private <T> T getOne(String sql, Object parm, Class c) {
        try {
            List list = executor.doQuery(sql, parm, c);
            return CollectionUtils.isEmpty(list) ? null : (T) list.get(0);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }


    public void close() {

        this.executor.close();

    }
}
