package cn.com.louie;

import cn.com.louie.config.Configuration;
import cn.com.louie.jdbc.DefaultSqlSessionFactory;
import cn.com.louie.jdbc.SqlSessionFactory;
import cn.com.louie.mapper.EOUtil;
import cn.com.louie.mapper.Entity;
import cn.com.louie.mapper.PackageScan;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.List;

/**
 * Created by Louie on 2016/5/23.
 */

/**
 * 初始化工厂类
 */
public class SqlSessionFactoryBean implements FactoryBean<SqlSessionFactory>, InitializingBean {
    private String packageScan;
    private DataSource dataSource;
    private boolean useCache;
    private SqlSessionFactory sqlSessionFactory;


    /**
     * 设置数据源
     *
     * @param dataSource
     */
    public void setDataSource(DataSource dataSource) {
        if (dataSource instanceof TransactionAwareDataSourceProxy)
            Configuration.getInstans().dataSource = ((TransactionAwareDataSourceProxy) dataSource).getTargetDataSource();
        else
            Configuration.getInstans().dataSource = dataSource;
    }

    /**
     * 扫描装载实体
     *
     * @param scan
     */
    public void setPackageScan(String scan) {
        PackageScan packageScan = new PackageScan(scan);
        List<String> classes = null;
        try {
            classes = packageScan.getFullyQualifiedClassNameList();
            for (String className : classes) {
                Class clazz = Class.forName(className, true, getClass().getClassLoader());
                if (clazz.isAnnotationPresent(Entity.class)) {
                    Configuration.getInstans().eoutils.put(clazz, new EOUtil(clazz));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    /**
     * 设置2级缓存是否开启
     *
     * @param useCache
     */
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
