package cn.com.louie.config;

import cn.com.louie.cache.Cache;
import cn.com.louie.cache.PerpetualCache;
import cn.com.louie.cache.TimerCache;
import cn.com.louie.mapper.EOUtil;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/5/23.
 */
public class Configuration {
    public Map<String, EOUtil> eoutils;
    public Cache cache;
    public DataSource dataSource;
    private static Configuration instans;

    private Configuration() {
        if (eoutils == null)
            eoutils = new HashMap<String, EOUtil>();
        if (cache == null)
            cache=new TimerCache(new PerpetualCache("localCache"),60*60*1000);
    }

    public static Configuration getInstans() {
        if (instans == null)
            instans = new Configuration();
        return instans;
    }



}
