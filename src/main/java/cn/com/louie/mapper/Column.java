package cn.com.louie.mapper;

import java.lang.annotation.*;

/**
 * Created by Louie on 2016/5/4.
 * 列名标注   对应数据库列名   未标注则取实体字段名
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Column {
    String name() default "";
}
