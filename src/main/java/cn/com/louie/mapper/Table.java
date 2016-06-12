package cn.com.louie.mapper;

import java.lang.annotation.*;

/**
 * Created by Louie on 2016/5/4.
 * 表名标注    未标注则取实体名字
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Table {
    String name() default "";
}
