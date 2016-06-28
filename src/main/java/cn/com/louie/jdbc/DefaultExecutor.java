package cn.com.louie.jdbc;

import cn.com.louie.cache.Cache;
import cn.com.louie.cache.CacheKey;
import cn.com.louie.cache.PerpetualCache;
import cn.com.louie.config.Configuration;
import cn.com.louie.mapper.EOUtil;
import cn.com.louie.mapper.QType;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.lang.reflect.Array;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by Louie on 2016/5/23.
 */

/**
 * 默认执行器
 */
public class DefaultExecutor implements Executor {
    private final static Logger logger = Logger.getLogger("DefaultExecutor");

    private Cache localCache;

    public DefaultExecutor() {
        localCache = new PerpetualCache("LocalCache");
    }

    public <E> List<E> doQuery(String sql, Object parms, Class eClass) throws IllegalAccessException, InstantiationException {
        CacheKey cacheKey = new CacheKey(new Object[]{sql, parms});
        List list = (List) localCache.getObject(cacheKey);
        if (!CollectionUtils.isEmpty(list))
            return list;
        Connection connection = null;
        ResultSet resultSet = null;
        PreparedStatement statement = null;
        try {
            connection = Configuration.getInstans().dataSource.getConnection();
            statement = connection.prepareStatement(sql);
            setParms(parms, statement);
            resultSet = statement.executeQuery();
            Object o = null;
            list = new ArrayList();
            EOUtil eoUtil = Configuration.getInstans().eoutils.get(eClass);
            if (eoUtil == null)
                throw new EntityNotFoundException("需要在实体上加入@Entity标注");
            Map<String, QType> map = eoUtil.getColums();
            while (resultSet.next()) {
                o = eClass.newInstance();
                mapRow(map, resultSet, eoUtil, o);
                list.add(o);
            }
            localCache.putObject(cacheKey, list);
            return list;
        } catch (SQLException e) {
            logger.info(e.getMessage());
            e.printStackTrace();
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

    public int updateBySQL(String sql, Object parms) {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = Configuration.getInstans().dataSource.getConnection();
            statement = connection.prepareStatement(sql);
            setParms(parms, statement);
            int count = statement.executeUpdate();
            if (count > 0)
                localCache.clear();
            return count;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        } finally {
            try {
                if (statement != null)
                    statement.close();
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    public Object insertBySQL(String sql, Object parms) {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = Configuration.getInstans().dataSource.getConnection();
            statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            setParms(parms, statement);
            int count = statement.executeUpdate();
            Object res = null;
            if (count > 0) {
                ResultSet resultSet = statement.getGeneratedKeys();
                while (resultSet.next())
                    res = resultSet.getObject(1);
                localCache.clear();
            }
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (statement != null)
                    statement.close();
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    public void insertBySQLNoId(String sql, Object parms) {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = Configuration.getInstans().dataSource.getConnection();
            statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            setParms(parms, statement);
            localCache.clear();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (statement != null)
                    statement.close();
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    public List Query(String sql, Object parms) {
        CacheKey cacheKey = new CacheKey(new Object[]{sql, parms});
        if (localCache.getObject(cacheKey) != null)
            return (List) localCache.getObject(cacheKey);
        Connection connection = null;
        ResultSet resultSet = null;
        PreparedStatement statement = null;
        try {
            connection = Configuration.getInstans().dataSource.getConnection();
            statement = connection.prepareStatement(sql);
            setParms(parms, statement);
            resultSet = statement.executeQuery();
            ResultSetMetaData md = resultSet.getMetaData();
            List list = new ArrayList();
            Map<String, Object> map = null;
            int i = 0;
            while (resultSet.next()) {
                map = new HashMap<String, Object>();
                map.put(md.getColumnName(i), resultSet.getObject(i));
                list.add(map);
                i++;
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

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

    private void setParms(Object parms, PreparedStatement statement) throws SQLException {
        if (parms != null)
            if (parms.getClass().isArray()) {
                for (int i = 1; i <= Array.getLength(parms); i++)
                    statement.setObject(i, Array.get(parms, i - 1));
            } else
                statement.setObject(1, parms);
    }

    public void close() {
        localCache = null;
    }


}
