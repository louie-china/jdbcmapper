package cn.com.louie.jdbc;

import cn.com.louie.cache.Cache;
import cn.com.louie.cache.CacheKey;
import cn.com.louie.cache.PerpetualCache;
import cn.com.louie.config.Configuration;
import cn.com.louie.mapper.EOUtil;
import cn.com.louie.mapper.QType;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/5/23.
 */
public class DefaultExecutor implements Executor {



    public DefaultExecutor() {
    }

    public <E> List<E> doQuery(String sql, Object parms, Class eClass) throws IllegalAccessException, InstantiationException {
        Cache cache = Configuration.getInstans().cache;
        CacheKey cacheKey = new CacheKey(new Object[]{sql, parms});
        if (cache.getObject(cacheKey) != null)
            return (List<E>) cache.getObject(cacheKey);
        Connection connection = null;
        ResultSet resultSet = null;
        PreparedStatement statement = null;
        try {
            connection = Configuration.getInstans().dataSource.getConnection();
            statement = connection.prepareStatement(sql);
            if (parms != null)
                if (parms.getClass().isArray()) {
                    Object[] objects = (Object[]) parms;
                    for (int i = 0; i < objects.length; i++)
                        statement.setObject(i, objects[i]);
                } else
                    statement.setObject(1, parms);
            resultSet = statement.executeQuery();
            Object o = null;
            List list = new ArrayList();
            EOUtil eoUtil = Configuration.getInstans().eoutils.get(eClass.getSimpleName());
            Map<String, QType> map = eoUtil.getColums();
            while (resultSet.next()) {
                o = eClass.newInstance();
                mapRow(map, resultSet, eoUtil, o);
                list.add(o);
            }
            resultSet.close();
            statement.close();
            connection.close();
            cache.putObject(cacheKey, list);
            return list;
        } catch (SQLException e) {
            return null;
        } finally {
            try {
                if (resultSet != null)
                    resultSet.close();
                if (statement != null)
                    statement.close();
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }


    public int doUpdate(Object o) {
        return 0;
    }


    private CacheKey createCacheKey(String sql, Object perms) {
        return new CacheKey(new Object[]{sql, perms});
    }

    private void mapRow(Map<String, QType> map, ResultSet row, EOUtil eoUtil, Object o) {
        for (Map.Entry<String, QType> entry : map.entrySet()) {
            try {
                eoUtil.setAttributeValue(entry.getKey(), row.getObject(entry.getValue().getKey()), o);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void close() throws IOException {

    }


}
