package cn.core.mobile.library.database.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import cn.core.mobile.library.database.annotation.Id;
import cn.core.mobile.library.database.annotation.Property;
import cn.core.mobile.library.database.annotation.Transient;

/**
 * 字段操作类
 * Created by dallas on 14-4-13.
 */
public class FieldUtils {
    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    /**
     * 获取指定变量名的getter方法
     *
     * @param clazz     class
     * @param fieldName 变量名
     * @return getter方法
     */
    public static Method getFieldGetterMethod(Class<?> clazz, String fieldName) {
        if (clazz == null) {
            return null;
        }
        if (fieldName == null || fieldName.trim().length() == 0) {
            return null;
        }
        Field field = getFieldByName(clazz, fieldName);
        return getFieldGetterMethod(clazz, field);
    }

    /**
     * 获取指定变量名的setter方法
     *
     * @param clazz     class
     * @param fieldName 变量名
     * @return setter方法
     */
    public static Method getFieldSetterMethod(Class<?> clazz, String fieldName) {
        if (clazz == null) {
            return null;
        }
        if (fieldName == null || fieldName.trim().length() == 0) {
            return null;
        }
        Field field = getFieldByName(clazz, fieldName);
        return getFieldSetterMethod(clazz, field);
    }

    /**
     * 获取指定字段Field对象的getter方法
     *
     * @param clazz Class 对象
     * @param field 字段Field对象
     * @return getter方法
     */
    public static Method getFieldGetterMethod(Class<?> clazz, Field field) {
        if (clazz == null || field == null) {
            return null;
        }
        String fn = field.getName();
        String mn = "get" + fn.substring(0, 1).toUpperCase() + fn.substring(1);
        Method method = null;
        if (field.getType() == boolean.class || field.getType() == Boolean.class) {
            method = getBooleanFieldGetterMethod(clazz, field);
        }
        if (method == null) {
            try {
                method = clazz.getMethod(mn);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        return method;
    }

    /**
     * 获取指定变量名的setter方法
     *
     * @param clazz Class 对象
     * @param field 字段Field对象
     * @return setter方法
     */
    public static Method getFieldSetterMethod(Class<?> clazz, Field field) {
        if (clazz == null || field == null) {
            return null;
        }
        String fn = field.getName();
        String mn = "set" + fn.substring(0, 1).toUpperCase() + fn.substring(1);
        Method method = null;
        if (field.getType() == boolean.class || field.getType() == Boolean.class) {
            method = getBooleanFieldSetterMethod(clazz, field);
        }
        if (method == null) {
            try {
                method = clazz.getMethod(mn, field.getType());
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        return method;
    }


    /**
     * 获取Boolean型字段的getter方法
     *
     * @param clazz Class 对象
     * @param field 字段Field对象
     * @return field对应的Boolean型getter方法
     */

    private static Method getBooleanFieldGetterMethod(Class<?> clazz, Field field) {
        if (clazz == null || field == null) {
            return null;
        }
        String fn = field.getName();
        String mn = "is" + fn.substring(0, 1).toUpperCase() + fn.substring(1);
        if (startWithIs(fn)) {
            mn = fn;
        }
        try {
            return clazz.getMethod(mn);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取Boolean型变量的setter方法
     *
     * @param clazz class
     * @param field 变量名
     * @return field对应的Boolean型setter方法
     */
    private static Method getBooleanFieldSetterMethod(Class<?> clazz, Field field) {
        if (clazz == null || field == null) {
            return null;
        }
        String fn = field.getName();
        String mn = "set" + fn.substring(0, 1).toUpperCase() + fn.substring(1);
        if (startWithIs(fn)) {
            mn = "set" + fn.substring(2, 3).toUpperCase() + fn.substring(3);
        }
        try {
            return clazz.getMethod(mn, field.getType());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取某个字段的值
     *
     * @param entity 类对象
     * @param field  变量field
     * @return 变量field的值
     */
    public static Object getFieldValue(Object entity, Field field) {
        Method getter = getFieldGetterMethod(entity.getClass(), field);
        return invoke(entity, getter);
    }

    /**
     * 获取某个字段的值
     *
     * @param entity    类对象
     * @param fieldName 变量名
     * @return 变量名fieldName的值
     */
    public static Object getFieldValue(Object entity, String fieldName) {
        Method getter = getFieldGetterMethod(entity.getClass(), fieldName);
        return invoke(entity, getter);
    }

    /**
     * 调用对象的setter方法设置指定字段的值。
     *
     * @param entity 类对象
     * @param field  字段Field对象
     * @param value  要设置的值
     */
    public static void setFieldValue(Object entity, Field field, Object value) {
        if (entity == null || field == null) {
            return;
        }
        try {
            Method setter = getFieldSetterMethod(entity.getClass(), field);
            if (setter != null) {
                setter.setAccessible(true);
                Class<?> type = field.getType();
                if (type == String.class) {
                    setter.invoke(entity, value == null ? null : value.toString());
                } else if (type == int.class || type == Integer.class) {
                    setter.invoke(entity, value == null ? 0 : Integer.parseInt(value.toString()));
                } else if (type == float.class || type == Float.class) {
                    setter.invoke(entity, value == null ? 0 : Float.parseFloat(value.toString()));
                } else if (type == long.class || type == Long.class) {
                    setter.invoke(entity, value == null ? 0 : Long.parseLong(value.toString()));
                } else if (type == Date.class || type == java.sql.Date.class) {
                    setter.invoke(entity, value == null ? null : stringToDateTime(value.toString()));
                } else {
                    setter.invoke(entity, value);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 调用对象的setter方法设置指定字段的值。
     *
     * @param entity 类对象
     * @param name   字段名
     * @param value  要设置的值
     */
    public static void setFieldValue(Object entity, String name, Object value) {
        if (entity == null || name == null) {
            return;
        }
        Field field = getFieldByName(entity.getClass(), name);
        setFieldValue(entity, field, value);
    }

    /**
     * 根据字段名获取Field对象
     *
     * @param clazz Class 对象
     * @param name  字段名
     * @return Field对象
     */
    public static Field getFieldByName(Class<?> clazz, String name) {
        Field field = null;
        if (clazz != null && name != null) {
            try {
                field = clazz.getDeclaredField(name);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        return field;
    }

    /**
     * 获取指定字段Field对象的数据表的列名
     *
     * @param field 字段Field对象
     * @return 字段Field对象的数据表的列名
     */
    public static String getColumnByField(Field field) {
        if (field == null) {
            return null;
        }
        Property property = field.getAnnotation(Property.class);
        if (property != null && property.column().trim().length() != 0) {
            return property.column();
        }

        Id id = field.getAnnotation(Id.class);
        if (id != null && id.column().trim().length() != 0) {
            return id.column();
        }

        return field.getName();
    }

    /**
     * 获取指定字段Field对象的属性注解的默认值。
     *
     * @param field 字段Field对象
     * @return 如果有属性注解则返回属性注解的默认值，否则返回null
     */
    public static String getPropertyDefaultValue(Field field) {
        Property property = field.getAnnotation(Property.class);
        if (property != null && property.defaultValue().trim().length() != 0) {
            return property.defaultValue();
        }
        return null;
    }

    /**
     * 判断指定字段是否是静态变量
     *
     * @param field 要判断的字段Field对象
     * @return 如果是静态变量返回true，否则返回false
     */
    public static boolean isStaticField(Field field) {
        return field != null && Modifier.isStatic(field.getModifiers());
    }

    /**
     * 判断指定字段是否已经被标注为非数据库字段
     *
     * @param field 要判断的字段Field对象
     * @return 被标注返回true，否则返回false
     */
    public static boolean isTransientField(Field field) {
        return field != null && field.getAnnotation(Transient.class) != null;
    }

    /**
     * 判断指定字段是否是主键字段
     *
     * @param field 要判断的字段Field对象
     * @return 如果是主键字段返回true，否则返回false
     */
    public static boolean isPrimaryKeyField(Field field) {
        if (field != null) {
            if (field.getAnnotation(Id.class) != null || field.getName().equals("_id")) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断指定字段Field对象是否为基本数据类型
     *
     * @param field 要判断的字段Field对象
     * @return 是基本数据类型返回true，否则返回false
     */
    public static boolean isBaseDateType(Field field) {
        Class<?> clazz = field.getType();
        return clazz.equals(String.class) ||
                clazz.equals(Integer.class) || clazz.equals(int.class) ||
                clazz.equals(Byte.class) || clazz.equals(byte.class) ||
                clazz.equals(Long.class) || clazz.equals(long.class) ||
                clazz.equals(Double.class) || clazz.equals(double.class) ||
                clazz.equals(Float.class) || clazz.equals(float.class) ||
                clazz.equals(Short.class) || clazz.equals(short.class) ||
                clazz.equals(Boolean.class) || clazz.equals(boolean.class) ||
                clazz.equals(Byte[].class) || clazz.equals(byte[].class) ||
                clazz.equals(Character.class) || clazz.equals(char.class);
    }

    /**
     * 获取某个对象执行某个不带参数方法的结果。
     *
     * @param obj    类对象
     * @param method 要执行的方法
     * @return 执行结果
     */
    private static Object invoke(Object obj, Method method) {
        if (obj == null || method == null) {
            return null;
        }
        try {
            return method.invoke(obj);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 某个字符串是否以is开头，并且is之后第一个字母是大写， 比如 isAdmin
     *
     * @param str 字符串
     * @return true or false
     */
    private static boolean startWithIs(String str) {
        return !(str == null) && str.startsWith("is") && Character.isUpperCase(str.charAt(2));
    }

    /**
     * 字符串转换成Date对象
     *
     * @param strDate 要转换的字符串
     * @return Date对象
     */
    public static Date stringToDateTime(String strDate) {
        if (strDate != null) {
            try {
                return SDF.parse(strDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 获取类的所有字段，包括其父类的。
     *
     * @param clazz Class 对象
     * @return 所有字段的数组
     */
    public static List<Field> getAllDeclaredFields(Class<?> clazz) {
        List<Field> result = new ArrayList<Field>();
        if (clazz == null) {
            return result;
        } else {
            Field[] fields = clazz.getDeclaredFields();
            Collections.addAll(result, fields);
            result.addAll(getAllDeclaredFields(clazz.getSuperclass()));
            return result;
        }
    }
}
