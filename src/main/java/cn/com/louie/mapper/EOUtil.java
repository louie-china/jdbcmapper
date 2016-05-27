package cn.com.louie.mapper;

import org.springframework.util.StringUtils;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Louie on 2016/5/23.
 */
public class EOUtil {

    private PropertyDescriptor[] propertyDescriptors;
    private Class clazz;
    private Map<String, Method> getter;
    private Map<String, Method> setter;
    private Map<String, QType> colums;
    private String insertSQL;
    private String tableName;
    public String primaryKey;
    private String updateSQL;
    private String selectSQL;

    public EOUtil(Class clazz) {
        try {
            this.init(clazz);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化加载
     *
     * @throws IllegalAccessException
     * @throws InstantiationException
     */

    private void init(Class clazz) throws IllegalAccessException, InstantiationException {
        this.clazz = clazz;
        getter = new HashMap();
        setter = new HashMap();
        colums = null;
        insertSQL = null;
        tableName = null;
        primaryKey = null;
        selectSQL = null;
        try {
            propertyDescriptors = Introspector.getBeanInfo(clazz).getPropertyDescriptors();
        } catch (IntrospectionException var3) {
            var3.printStackTrace();
        }
        getColums();
        gettableName();


    }

    /**
     * 获取表名
     * 标注不为空则取标注  否则取类名
     *
     * @return
     */
    public String gettableName() {
        if (tableName != null) {
            return tableName;
        } else {
            Table table = (Table) clazz.getAnnotation(Table.class);
            if (table != null && !StringUtils.isEmpty(table.name())) {
                tableName = table.name();
            } else {
                tableName = clazz.getSimpleName();
            }

            return tableName;
        }
    }

    /**
     * 创建查询语句
     *
     * @param whereSQL
     * @return
     */
    public String buildSelect(String whereSQL) {

        StringBuffer select = new StringBuffer("SELECT ");
        for (Map.Entry<String, QType> entry : getColums().entrySet()) {
            select.append(entry.getValue().getKey() + ", ");
        }
        String slsql = select.toString().indexOf(",") == -1 ? null : select.toString().substring(0, select.toString().lastIndexOf(","));
        if (slsql == null)
            throw new IllegalArgumentException("beans must have an attr");
        else if (StringUtils.isEmpty(whereSQL))
            selectSQL = slsql + " FROM " + gettableName();
        else
            selectSQL = slsql + " FROM " + gettableName() + " WHERE " + whereSQL;
        return selectSQL;
    }

    /**
     * 获取bean的属性字段和数据库字段对应关系以及实体中的数据类型
     *
     * @return
     */
    public Map<String, QType> getColums() {
        if (colums != null) {
            return colums;
        } else {
            colums = new HashMap();
            Field[] fields = clazz.getDeclaredFields();
            Field[] arr$ = fields;
            int len$ = fields.length;
            for (int i$ = 0; i$ < len$; ++i$) {
                Field field = arr$[i$];
                if (field.getAnnotation(Id.class) != null) {
                    primaryKey = field.getName();
                }
                if (!isLazyField(field.getDeclaredAnnotations())) {
                    colums.put(field.getName(), this.getColumn(field.getName()));
                    PropertyDescriptor[] arr$1 = propertyDescriptors;
                    int len$1 = arr$1.length;
                    for (int i$1 = 0; i$1 < len$1; ++i$1) {
                        PropertyDescriptor property = arr$1[i$1];
                        if (field.getName().toLowerCase().equals(property.getName().toLowerCase())) {
                            setter.put(field.getName(), property.getWriteMethod());//加载set方法
                            getter.put(field.getName(), property.getReadMethod());//加载get方法
                        }
                    }
                }
            }

            return colums;
        }
    }

    /**
     * 调用set方法赋值
     *
     * @param attName
     * @param value
     */
    public void setAttributeValue(String attName, Object value,Object o) {
        try {
            Method m = setter.get(attName);
            m.invoke(o, value);
        } catch (Exception var4) {
        }

    }

    /**
     * 判断是否是自增
     *
     * @param field
     * @return
     */
    public boolean isGenerate(String field) {
        try {
            return clazz.getDeclaredField(field).getAnnotation(Generate.class) == null;
        } catch (NoSuchFieldException var3) {
            return true;
        }
    }

    /**
     * 构建插入语句
     *
     * @return
     */
    public String buildInsert(Object o) {
        Map filds = this.getColums();
        StringBuffer insert = new StringBuffer("INSERT INTO " + this.gettableName() + "(");
        StringBuffer values = new StringBuffer(" VALUES (");
        Iterator val = filds.entrySet().iterator();

        while (true) {
            while (true) {
                Map.Entry ins;
                do {
                    do {
                        if (!val.hasNext()) {
                            val = null;
                            if (!values.toString().contains(",")) {
                                throw new IllegalArgumentException("insert must have an attr value");
                            }
                            String val1 = values.toString();
                            val1 = val1.substring(0, val1.length() - 1);
                            String ins1 = insert.toString();
                            ins1 = ins1.substring(0, ins1.length() - 1);
                            insertSQL = ins1 + ")" + val1 + ")";
                            return insertSQL;
                        }
                        ins = (Map.Entry) val.next();
                    } while (this.getAttributeValue((String) ins.getKey(),o) == null);
                } while (!this.isGenerate((String) ins.getKey()));

                insert.append(((QType) ins.getValue()).getKey() + ",");
                if (!isDigital(((QType) ins.getValue()).getType())) {
                    values.append("\'" + this.getAttributeValue(((String) ins.getKey()),o).toString().replaceAll("'", "\\\\'") + "\',");
                } else {
                    values.append(this.getAttributeValue((String) ins.getKey(),o) + ",");
                }
            }
        }
    }

    /**
     * 构建更新语句
     * 没有传入更新条件时默认以id为条件
     *
     * @param whereSQL
     * @return
     */
    public String buildUpdate(String whereSQL,Object o) {
        Map filds = this.getColums();
        StringBuffer update = new StringBuffer("UPDATE " + this.gettableName() + " SET ");
        Iterator upsql = filds.entrySet().iterator();

        while (true) {
            while (true) {
                Map.Entry entry;
                do {
                    do {
                        if (!upsql.hasNext()) {
                            String upsql1 = update.toString().indexOf(",") == -1 ? null : update.toString().substring(0, update.length() - 2);
                            if (StringUtils.isEmpty(whereSQL)) {
                                if (primaryKey == null) {
                                    throw new IllegalArgumentException("update must have an id or WhereSQL");
                                }
                                QType entry1 = (QType) filds.get(primaryKey);
                                if (!isDigital(entry1.getType())) {
                                    updateSQL = upsql1 + " WHERE " + ((QType) filds.get(primaryKey)).getKey() + "=\'" + this.getAttributeValue(primaryKey,o) + "\'";
                                } else {
                                    updateSQL = upsql1 + " WHERE " + ((QType) filds.get(primaryKey)).getKey() + "=" + this.getAttributeValue(primaryKey,o);
                                }
                            } else {
                                updateSQL = upsql1 + " WHERE " + whereSQL;
                            }
                            return updateSQL;
                        }
                        entry = (Map.Entry) upsql.next();
                    } while (this.getAttributeValue((String) entry.getKey(),o) == null);
                } while (entry.getKey().equals(primaryKey));
                if (!isDigital(((QType) entry.getValue()).getType())) {
                    update.append(((QType) entry.getValue()).getKey() + "=\'" + this.getAttributeValue(((String) entry.getKey()),o).toString().replaceAll("'", "\\\\'") + "\', ");
                } else {
                    update.append(((QType) entry.getValue()).getKey() + "=" + this.getAttributeValue((String) entry.getKey(),o) + ", ");
                }
            }
        }
    }

    /**
     * 判断是否为数值类型
     *
     * @param qType
     * @return
     */
    private boolean isDigital(Type qType) {
        return qType.equals(Integer.class)
                || qType.equals(Short.class)
                || qType.equals(Long.class)
                || qType.equals(Double.class)
                || qType.equals(Float.class)
                || qType.equals(Byte.class)
                || qType.equals(byte.class)
                || qType.equals(int.class)
                || qType.equals(short.class)
                || qType.equals(long.class)
                || qType.equals(double.class)
                || qType.equals(float.class);
    }


    /**
     * get方法取值
     *
     * @param attName
     * @return
     */
    public Object getAttributeValue(String attName,Object bean) {
        Object o = null;

        try {
            Object os = null;
            Method m = getter.get(attName);
            o = m.invoke(bean, (Object[]) os);
        } catch (Exception var5) {
        }

        return o;
    }

    /**
     * 获取单列的数据库字段及实体类型
     *
     * @param fieldName
     * @return
     */
    public QType getColumn(String fieldName) {
        String column = null;

        Field f;
        try {
            f = clazz.getDeclaredField(fieldName);
        } catch (SecurityException var6) {
            return null;
        } catch (NoSuchFieldException var7) {
            return null;
        }

        Column am = f.getAnnotation(Column.class);
        if (am != null && !StringUtils.isEmpty(am.name())) {
            column = am.name();
        } else {
            column = fieldName;
        }

        QType qType = new QType(column, f.getGenericType());
        return qType;
    }

    /**
     * 本来是准备做懒加载判断的  现在改成临时字段处理
     *
     * @param annotations
     * @return
     */
    public boolean isLazyField(Annotation[] annotations) {
        Annotation[] arr$ = annotations;
        int len$ = annotations.length;

        for (int i$ = 0; i$ < len$; ++i$) {
            Annotation annotation = arr$[i$];
            if (annotation instanceof Transient) {
                return true;
            }
        }

        return false;
    }
}
