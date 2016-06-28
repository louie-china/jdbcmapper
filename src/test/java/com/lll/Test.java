package com.lll;

import cn.com.louie.SqlSessionFactoryBean;
import cn.com.louie.config.Configuration;
import cn.com.louie.mapper.*;

import java.util.List;

/**
 * Created by Administrator on 2016/5/9.
 * 这是一个测试类
 */
@Entity
@Table(name = "test")
public class
Test extends BaseEO {
    @Id
    @Generate
    private int id;//测试id标注及自增标注
    @Column(name = "name_louie")
    private String name;//测试column的name标注
    @Column
    private String logs;//测试有标注无值

    private String email;//测试无标注
    @Transient
    private String none;//测试临时字段标注


    public static void main(String[] args) throws Exception {

        SqlSessionFactoryBean factory=new SqlSessionFactoryBean();
        factory.setPackageScan("com.*.xx");

        System.out.println(Configuration.getInstans().eoutils.size()+"xxxxxxxxxxxx");
        EOUtil eoUtil=Configuration.getInstans().eoutils.get(com.lll.xx.Test.class);
        System.out.println(eoUtil.buildSelect(null));
//        System.out.println(eoUtil.gettableName());

//        System.out.println(400/6);
//        System.out.println((400/5.8)/1280);
//        System.out.println(16500*100/71400);


        System.out.println(15000*0.12);

//        com.lll.Test test = new com.lll.Test();
//        test.setId(0);
//        test.setName("louie");
//        test.setEmail("louieluo@foxmail.com");
//        test.setLogs("this is a 'mapper' for spring-jdbc");
//        test.setNone("this is a test");
//        System.out.println(test.selectSQL(null));//生成查询sql  传入参数为条件   详情看下面注释的语句
//        System.out.println(test.updateSQL(null));//生成更新语句  默认以id为条件更新
//        System.out.println(test.insertSQL());//生成插入语句
//
////        JdbcTemplate jdbcTemplate = new JdbcTemplate(null);//This is an example of a JDBC call, so there is no data source
////        List<com.lll.Test> tests = jdbcTemplate.query(test.selectSQL("name=? and logs=?"), new Object[]{"louie", "test"}, test);//This is an example of a query with parameters
////        int count = jdbcTemplate.update(test.insertSQL());//This is an example of an insert
////        count=jdbcTemplate.update(test.updateSQL(null));//This is a default update
////        count=jdbcTemplate.update(test.updateSQL("name=? or logs=?"),new Object[]{"louie","this is a 'mapper' for spring-jdbc"});
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogs() {
        return logs;
    }

    public void setLogs(String logs) {
        this.logs = logs;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNone() {
        return none;
    }

    public void setNone(String none) {
        this.none = none;
    }
}
