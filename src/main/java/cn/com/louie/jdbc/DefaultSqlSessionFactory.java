package cn.com.louie.jdbc;

import cn.com.louie.config.Configuration;

/**
 * Created by Louie on 2016/5/23.
 */
public class DefaultSqlSessionFactory implements SqlSessionFactory {
    public SqlSession getSession() {
        final Executor executor= Configuration.getInstans().getExecutor(0);
        return new DefaultSqlSession(executor);
    }

    public SqlSession getSession(boolean cache) {
        final Executor executor= Configuration.getInstans().getExecutor(cache?1:2);
        return new DefaultSqlSession(executor);
    }


}
