package cn.com.louie.cache;

/**
 * Created by Louie on 2016/5/23.
 */

/**
 * 缓存异常类
 */
public class CacheException extends RuntimeException {
    public CacheException() {
    }

    public CacheException(String message) {
        super(message);
    }
}
