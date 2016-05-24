package cn.com.louie.mapper.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;

/**
 * Created by Louie on 2016/5/23.
 */
public class PerpetualCache implements Cache {

    private String id;

    private Map<Object, Object> cache = new HashMap<Object, Object>();

    public PerpetualCache(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public int getSize() {
        return cache.size();
    }

    public void putObject(Object key, Object value) {
        cache.put(key, value);
    }

    public Object getObject(Object key) {
        return cache.get(key);
    }

    public Object removeObject(Object key) {
        return cache.remove(key);
    }

    public void clear() {
        cache.clear();
    }

    public ReadWriteLock getReadWriteLock() {
        return null;
    }

    public boolean equals(Object o) {
        if (getId() == null) throw new CacheException("Cache instances require an ID.");
        if (this == o) return true;
        if (!(o instanceof Cache)) return false;

        Cache otherCache = (Cache) o;
        return getId().equals(otherCache.getId());
    }

    public int hashCode() {
        if (getId() == null) throw new CacheException("Cache instances require an ID.");
        return getId().hashCode();
    }

}
