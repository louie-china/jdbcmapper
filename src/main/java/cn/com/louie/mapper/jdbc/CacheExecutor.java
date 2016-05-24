package cn.com.louie.mapper.jdbc;

import cn.com.louie.mapper.cache.Cache;
import cn.com.louie.mapper.cache.CacheKey;
import cn.com.louie.mapper.config.Configuration;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Administrator on 2016/5/24.
 */
public class CacheExecutor implements Executor {

    private Executor baseExecutor;
    private Cache cache;

    public CacheExecutor(Executor baseExecutor) {
        this.cache = Configuration.getInstans().cache;
        this.baseExecutor = baseExecutor;
    }

    public <E> List<E> doQuery(String sql, Object parms, Class clazz) throws SQLException, IllegalAccessException, InstantiationException {
        CacheKey cacheKey = new CacheKey(new Object[]{sql, parms, clazz});
        if (cache.getObject(cacheKey) != null)
            return (List<E>) cache.getObject(cacheKey);
        List<E> list = baseExecutor.doQuery(sql, parms, clazz);
        cache.putObject(cacheKey, list);
        return list;
    }

    public int updateBySQL(String sql, Object parms) {
        int count = baseExecutor.updateBySQL(sql, parms);
        if (count > 0)
            cache.clear();
        return count;
    }

    public Object insertBySQL(String sql, Object parms) {
        Object object = baseExecutor.insertBySQL(sql, parms);
        if (object != null)
            cache.clear();
        return object;
    }

    public List Query(String sql, Object parms) {
        CacheKey cacheKey = new CacheKey(new Object[]{sql, parms});
        if (cache.getObject(cacheKey) != null)
            return (List) cache.getObject(cacheKey);
        List list = baseExecutor.Query(sql, parms);
        cache.putObject(cacheKey, list);
        return list;
    }

    public void close() throws IOException {

    }
}
