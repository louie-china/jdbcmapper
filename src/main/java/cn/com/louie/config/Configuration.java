package cn.com.louie.config;

import cn.com.louie.cache.Cache;
import cn.com.louie.cache.PerpetualCache;
import cn.com.louie.cache.TimerCache;
import cn.com.louie.jdbc.CacheExecutor;
import cn.com.louie.jdbc.DefaultExecutor;
import cn.com.louie.jdbc.Executor;
import cn.com.louie.mapper.EOUtil;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/5/23.
 */
public class Configuration {
    public Map<Class, EOUtil> eoutils;
    public Cache cache;
    public DataSource dataSource;
    public boolean useCache = false;
    private static Configuration instans;

    private Configuration() {
        if (eoutils == null)
            eoutils = new HashMap<Class, EOUtil>();
        if (cache == null)
            cache = new TimerCache(new PerpetualCache("globalCache"), 10 * 1000);
    }

    public static Configuration getInstans() {
        if (instans == null)
            instans = new Configuration();
        return instans;
    }

    public Executor getExecutor(int cache) {
        Executor executor = new DefaultExecutor();
        if (cache==1)
            return new CacheExecutor(executor);
        if (useCache && cache == 0)
            return new CacheExecutor(executor);
        return executor;
    }


}
