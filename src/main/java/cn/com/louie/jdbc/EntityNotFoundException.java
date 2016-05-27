package cn.com.louie.jdbc;

/**
 * Created by Louie on 2016/5/26.
 */

/**
 * 实体未找到异常
 */
public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String message) {
        super(message);
    }
}
