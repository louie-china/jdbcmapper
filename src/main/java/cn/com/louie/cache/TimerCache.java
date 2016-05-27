package cn.com.louie.cache;

import java.util.concurrent.locks.ReadWriteLock;

/**
 * Created by Louie on 2016/5/23.
 */

/**
 * 时效缓存
 */
public class TimerCache implements Cache {
    private Cache delegate;
    protected long clearInterval;
    protected long lastClear;

    public TimerCache(Cache delegate, long clearInterval) {
        this.delegate = delegate;
        this.clearInterval = clearInterval;
        this.lastClear = System.currentTimeMillis();
    }

    public void setClearInterval(long clearInterval) {
        this.clearInterval = clearInterval;
    }


    public String getId() {
        return delegate.getId();
    }


    public int getSize() {
        clearWhenStale();
        return delegate.getSize();
    }


    public void putObject(Object key, Object object) {
        clearWhenStale();
        lastClear=System.currentTimeMillis();
        delegate.putObject(key, object);
    }


    public Object getObject(Object key) {
        if (clearWhenStale()) {
            return null;
        } else {
            return delegate.getObject(key);
        }
    }


    public Object removeObject(Object key) {
        clearWhenStale();
        return delegate.removeObject(key);
    }


    public void clear() {
        lastClear = System.currentTimeMillis();
        delegate.clear();
    }


    public ReadWriteLock getReadWriteLock() {
        return null;
    }


    public int hashCode() {
        return delegate.hashCode();
    }


    public boolean equals(Object obj) {
        return delegate.equals(obj);
    }

    private boolean clearWhenStale() {
        if (System.currentTimeMillis() - lastClear > clearInterval) {
            clear();
            return true;
        }
        return false;
    }

}
