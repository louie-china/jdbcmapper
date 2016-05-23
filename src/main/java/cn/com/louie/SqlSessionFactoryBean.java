package cn.com.louie;

import cn.com.louie.config.Configuration;
import cn.com.louie.mapper.EOUtil;
import cn.com.louie.mapper.Entity;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;

import javax.sql.DataSource;
import java.io.File;
import java.net.URL;

/**
 * Created by Administrator on 2016/5/23.
 */
public class SqlSessionFactoryBean {
    private String packageScan;
    private DataSource dataSource;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        if (dataSource instanceof TransactionAwareDataSourceProxy)
            Configuration.getInstans().dataSource = ((TransactionAwareDataSourceProxy) dataSource).getTargetDataSource();
        else
            Configuration.getInstans().dataSource = dataSource;
    }

    public void setPackageScan(String scan) throws ClassNotFoundException {
        ClassLoader classLoader = this.getClass().getClassLoader();
        URL url = classLoader.getResource(scan);
        String rootPath = url.getPath().toString();
        File rootFile = new File(rootPath);
        File[] files = rootFile.listFiles();
        for (File f : files) {
            System.err.println(f.getName());
            String className = f.getName().substring(0, f.getName().indexOf(".class"));
            System.out.println(className);
            if (scan != null && scan.trim().length() != 0)
                className = scan + "." + className;
            Class clazz = Class.forName(className);
            if (clazz.isAnnotationPresent(Entity.class)) {
                Configuration.getInstans().eoutils.put(clazz.getSimpleName(), new EOUtil(clazz));
            }
        }
    }
}
