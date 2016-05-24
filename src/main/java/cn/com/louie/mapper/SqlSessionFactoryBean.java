package cn.com.louie.mapper;

import cn.com.louie.mapper.config.Configuration;
import cn.com.louie.mapper.jdbc.DefaultSqlSessionFactory;
import cn.com.louie.mapper.jdbc.SqlSessionFactory;
import cn.com.louie.mapper.mapper.EOUtil;
import cn.com.louie.mapper.mapper.Entity;
import cn.com.louie.mapper.mapper.PackageScan;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.List;

/**
 * Created by Administrator on 2016/5/23.
 */
public class SqlSessionFactoryBean implements FactoryBean<SqlSessionFactory>, InitializingBean {
    private String packageScan;
    private DataSource dataSource;
    private boolean useCache;
    private SqlSessionFactory sqlSessionFactory;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        if (dataSource instanceof TransactionAwareDataSourceProxy)
            Configuration.getInstans().dataSource = ((TransactionAwareDataSourceProxy) dataSource).getTargetDataSource();
        else
            Configuration.getInstans().dataSource = dataSource;
    }

    public void setPackageScan(String scan) {
        PackageScan packageScan = new PackageScan(scan);
        List<String> classes = null;
        try {
            classes = packageScan.getFullyQualifiedClassNameList();
            for (String className : classes) {
                Class clazz = Class.forName(className, true, getClass().getClassLoader());
                if (clazz.isAnnotationPresent(Entity.class)) {
                    Configuration.getInstans().eoutils.put(clazz.getSimpleName(), new EOUtil(clazz));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void setUseCache(boolean useCache) {
        Configuration.getInstans().useCache = useCache;
    }

    public SqlSessionFactory getObject() throws Exception {
        if (sqlSessionFactory == null)
            afterPropertiesSet();
        return this.sqlSessionFactory;
    }

    public Class<? extends SqlSessionFactory> getObjectType() {
        return this.sqlSessionFactory == null ? SqlSessionFactory.class : this.sqlSessionFactory.getClass();
    }

    public boolean isSingleton() {
        return true;
    }

    public void afterPropertiesSet() throws Exception {
        this.sqlSessionFactory = new DefaultSqlSessionFactory();
    }

    public String getPackageScan() {
        return packageScan;
    }

    public boolean isUseCache() {
        return useCache;
    }

    public SqlSessionFactory getSqlSessionFactory() {
        return sqlSessionFactory;
    }

    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }
}
