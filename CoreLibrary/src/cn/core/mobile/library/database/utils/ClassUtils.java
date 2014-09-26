package cn.core.mobile.library.database.utils;

import cn.core.mobile.library.database.annotation.Table;

/**
 * Created by dallas on 14-4-13.
 */

public class ClassUtils {

    /**
     * 获得类实例对应的表名
     *
     * @param entity 类对象
     * @return 类对象的对应的表名
     */
    public static String getTableName(Object entity) {
        if (entity == null) {
            throw new RuntimeException("No entity classes available");
        }
        return getTableName(entity.getClass());
    }

    /**
     * 获得类实例对应的表名
     *
     * @param clazz Class 对象
     * @return 实体类对应的表名
     */
    public static String getTableName(Class<?> clazz) {
        if (clazz == null) {
            throw new RuntimeException("No entity classes available");
        }

        Table table = clazz.getAnnotation(Table.class);
        if (table.name() == null || table.name().trim().length() == 0) {
            //当没有注解的时候默认用类的名称作为表名,并把点（.）替换为下划线(_)
            return table.getClass().getName().replace('.', '_');
        }
        return table.name();
    }

}

