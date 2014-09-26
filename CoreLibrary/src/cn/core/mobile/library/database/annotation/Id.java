package cn.core.mobile.library.database.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @title Id主键配置注解
 * @description 不配置的时候默认找类的id或_id字段作为主键，column不配置的是默认为字段名
 * Created by dallas on 14-4-10.
 */

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Id {
    public String column() default "";
}
