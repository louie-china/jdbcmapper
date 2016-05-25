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
 * Created by Administrator on 2016/5/23.
 */
public class DefaultSqlSession implements SqlSession {

    private Executor executor;

    public DefaultSqlSession(Executor executor) {
        this.executor = executor;
    }

    public <T> T load(Object Id, Class<T> clazz) {
        EOUtil eoUtil = Configuration.getInstans().eoutils.get(clazz.getSimpleName());
        String sql = eoUtil.buildSelect(eoUtil.primaryKey + "=?");
        return getOne(sql, Id, clazz);
    }

    public <T> T findBySQL(String sql, Object parms, Class<T> clazz) {
        return getOne(sql, parms, clazz);
    }

    public <T> T findByWhereSQL(String whereSql, Object parms, Class<T> clazz) {
        EOUtil eoUtil = Configuration.getInstans().eoutils.get(clazz.getSimpleName());
        String sql = eoUtil.buildSelect(whereSql);
        return getOne(sql, parms, clazz);
    }

    public <E> List<E> queryBySQL(String sql, Object parms, Class<E> clazz) {
        return getList(sql, parms, clazz);
    }

    public <E> List<E> queryByWhereSQL(String whereSql, Object parms, Class<E> clazz) {
        EOUtil eoUtil = Configuration.getInstans().eoutils.get(clazz.getSimpleName());
        String sql = eoUtil.buildSelect(whereSql);
        return getList(sql, parms, clazz);
    }

    public List queryBySQL(String sql, Object parms) {
        return executor.Query(sql, parms);
    }

    public Map<String, Object> findBySQL(String sql, Object parms) {
        List list= executor.Query(sql, parms);
        return CollectionUtils.isEmpty(list)?null: (Map<String, Object>) list.get(0);
    }

    public int updateBySQL(String sql, Object parms) {
        return executor.updateBySQL(sql,parms);
    }

    public int update(Object t) {
        EOUtil eoUtil= Configuration.getInstans().eoutils.get(t.getClass().getSimpleName());
        String sql=eoUtil.buildUpdate(null,t);
        return executor.updateBySQL(sql,null);
    }

    public Object insert(Object t) {
        EOUtil eoUtil= Configuration.getInstans().eoutils.get(t.getClass().getSimpleName());
        String sql=eoUtil.buildInsert(t);
        return executor.insertBySQL(sql,null);
    }

    public Object insertBySQL(String sql, Object parms) {
        return executor.insertBySQL(sql,parms);
    }


    public Connection getConnection() {
        return null;
    }

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


    public void close(){

            this.executor.close();

    }
}
