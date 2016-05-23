package cn.com.louie.cache;

import java.util.concurrent.locks.ReadWriteLock;

/**
 * Created by Louie on 2016/5/23.
 */
public interface Cache {


    String getId();


    void putObject(Object key, Object value);


    Object getObject(Object key);


    Object removeObject(Object key);


    void clear();

    int getSize();


    ReadWriteLock getReadWriteLock();

}
