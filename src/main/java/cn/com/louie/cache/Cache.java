package cn.com.louie.cache;

import java.util.concurrent.locks.ReadWriteLock;

/**
 * Created by Louie on 2016/5/23.
 */
public interface Cache {

    /**
     * 缓存ID
     *
     * @return
     */
    String getId();

    /**
     * 插入缓存
     *
     * @param key
     * @param value
     */
    void putObject(Object key, Object value);

    /**
     * 获取缓存
     *
     * @param key
     * @return
     */
    Object getObject(Object key);

    /**
     * 删除指定的key
     *
     * @param key
     * @return
     */
    Object removeObject(Object key);

    /**
     * 清除所有缓存
     */
    void clear();

    /**
     * 获取缓存数量
     *
     * @return
     */
    int getSize();

    /**
     * 读写锁
     *
     * @return
     */
    ReadWriteLock getReadWriteLock();

}
