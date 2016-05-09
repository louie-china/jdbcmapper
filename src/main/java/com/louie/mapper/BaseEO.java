package com.louie.mapper;


import net.sf.cglib.beans.BeanCopier;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map.Entry;

/**
 * 抽象mapper
 */
public abstract class BaseEO implements RowMapper {
    private transient EOUtility eoutil = null;
    private BeanCopier bc = null;
    private BaseEO bean = null;

    public BaseEO() {
        if (eoutil == null)
            eoutil = new EOUtility(this);
    }

    protected EOUtility getBeanUtility() {
        try {
            bean = this.getClass().newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        bc = BeanCopier.create(eoutil.bean.getClass(), this.bean.getClass(), false);
        return eoutil;
    }

    /**
     * 构建插入语句
     *
     * @return
     */
    public String insertSQL() {
        return this.getBeanUtility().buildInsert();
    }

    /**
     * 构建更新语句
     * @param whereSQL
     * @return
     */
    public String updateSQL(String whereSQL) {
        return this.getBeanUtility().buildUpdate(whereSQL);
    }

    /**
     * 构建单表查询语句
     * @param whereSQL
     * @return
     */
    public String selectSQL(String whereSQL) {
        return this.getBeanUtility().buildSelect(whereSQL);
    }

    /**
     * spring的rowmapper实现
     *
     * @param resultSet
     * @param i
     * @return
     * @throws SQLException
     */
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        try {
            Iterator e = getBeanUtility().getColums().entrySet().iterator();
            while (e.hasNext()) {
                Entry<String, QType> entry = (Entry) e.next();
                eoutil.setAttributeValue(entry.getKey(), resultSet.getObject(entry.getValue().getKey()));
            }
        } catch (Exception var7) {
            var7.printStackTrace();
        }
        bc.copy(eoutil.bean, bean, null);
        return bean;

    }
}
