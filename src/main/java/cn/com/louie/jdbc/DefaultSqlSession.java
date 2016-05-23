package cn.com.louie.jdbc;

import cn.com.louie.config.Configuration;
import cn.com.louie.mapper.EOUtil;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

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

    public <T> T findByWhere(String sql, Object parms, Class<T> clazz) {
        return getOne(sql, parms, clazz);
    }

    public <T> T findByWhereSQL(String whereSql, Object parms, Class<T> clazz) {
        EOUtil eoUtil = Configuration.getInstans().eoutils.get(clazz.getSimpleName());
        String sql = eoUtil.buildSelect(whereSql);
        return getOne(sql, parms, clazz);
    }

    public <E> List<E> queryByWhere(String sql, Class<E> clazz) {
        return null;
    }

    public <E> List<E> queryByWhereSQL(String sql, Class<E> clazz) {
        return null;
    }



    public int utpdate(Object t) {
        return 0;
    }

    public int insert(Object t) {
        return 0;
    }

    public Connection getConnection() {
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
    public void close() throws IOException {
   this.executor.close();
    }
}
