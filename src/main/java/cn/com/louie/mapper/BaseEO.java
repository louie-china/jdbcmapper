package cn.com.louie.mapper;


import org.springframework.jdbc.core.RowMapper;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map.Entry;

/**
 * 抽象mapper
 */
public abstract class BaseEO implements RowMapper, Serializable,Cloneable {
    private transient EOUtility eoutil = null;

    public BaseEO() {
        if (eoutil == null)
            eoutil = new EOUtility(this);
    }



    /**
     * 构建插入语句
     *
     * @return
     */
    public String insertSQL() {
        return this.eoutil.buildInsert();
    }

    /**
     * 构建更新语句
     *
     * @param whereSQL
     * @return
     */
    public String updateSQL(String whereSQL) {
        return this.eoutil.buildUpdate(whereSQL);
    }

    /**
     * 构建单表查询语句
     *
     * @param whereSQL
     * @return
     */
    public String selectSQL(String whereSQL) {
        return this.eoutil.buildSelect(whereSQL);
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
            Iterator e = eoutil.getColums().entrySet().iterator();
            while (e.hasNext()) {
                Entry<String, QType> entry = (Entry) e.next();
                eoutil.setAttributeValue(entry.getKey(), resultSet.getObject(entry.getValue().getKey()));
            }
        } catch (Exception var7) {
            var7.printStackTrace();
        }
        try {
            return eoutil.bean.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;

    }
}
