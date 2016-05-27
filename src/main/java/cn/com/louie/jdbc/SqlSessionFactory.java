package cn.com.louie.jdbc;

/**
 * Created by Louie on 2016/5/23.
 */

/**
 * Session 工厂
 */
public interface SqlSessionFactory {
    /**
     * 获取默认session
     * @return
     */
    SqlSession getSession();

    /**
     * 获取指定是否缓存的session
     * 缓存优先级+
     * @param cache
     * @return
     */
    SqlSession getSession(boolean cache);
}
