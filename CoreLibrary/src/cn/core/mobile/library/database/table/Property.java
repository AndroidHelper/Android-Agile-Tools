package cn.core.mobile.library.database.table;

import android.database.Cursor;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by dallas on 14-4-13.
 */
public class Property {
    private String mFieldName;
    private String mColumn;
    private String mDefaultVal;
    private Class<?> mDataType;
    private Field mField;

    private Method mGetMethod;
    private Method mSetMethod;

    /**
     * 执行某个实体类的setter方法
     *
     * @param receiver
     * @param value
     */
    public void setValue(Object receiver, Object value) {
        if (mSetMethod != null) {
            try {
                if (mDataType.equals(String.class)) {
                    mSetMethod.invoke(receiver, value == null ? "" : value.toString());
                } else if (mDataType.equals(Integer.class) || mDataType.equals(int.class)) {
                    mSetMethod.invoke(receiver, value == null ? 0 : Integer.parseInt(value.toString()));
                } else if (mDataType.equals(Byte.class) || mDataType.equals(byte.class)) {
                    mSetMethod.invoke(receiver, value == null ? 0 : Byte.parseByte(value.toString()));
                } else if (mDataType.equals(Long.class) || mDataType.equals(long.class)) {
                    mSetMethod.invoke(receiver, value == null ? 0 : Long.parseLong(value.toString()));
                } else if (mDataType.equals(Double.class) || mDataType.equals(double.class)) {
                    mSetMethod.invoke(receiver, value == null ? 0 : Double.parseDouble(value.toString()));
                } else if (mDataType.equals(Float.class) || mDataType.equals(float.class)) {
                    mSetMethod.invoke(receiver, value == null ? 0 : Float.parseFloat(value.toString()));
                } else if (mDataType.equals(Short.class) || mDataType.equals(short.class)) {
                    mSetMethod.invoke(receiver, value == null ? 0 : Short.parseShort(value.toString()));
                } else if (mDataType.equals(Character.class) || mDataType.equals(char.class)) {
                    mSetMethod.invoke(receiver, value == null ? null : value.toString().charAt(0));
                } else if (mDataType.equals(Boolean.class) || mDataType.equals(boolean.class)) {
                    mSetMethod.invoke(receiver, value != null && "1".equals(value.toString()));
                } else if (mDataType.equals(Byte[].class) || mDataType.equals(byte[].class)) {
                    mSetMethod.invoke(receiver, value);
                } else {
                    mSetMethod.invoke(receiver, value);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        } else {
            try {
                mField.setAccessible(true);
                mField.set(receiver, value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getValue(Object obj) {
        if (obj != null && mGetMethod != null) {
            try {
                return (T) mGetMethod.invoke(obj);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public String getFieldName() {
        return mFieldName;
    }

    public void setFieldName(String fieldName) {
        this.mFieldName = fieldName;
    }

    public String getColumn() {
        return mColumn;
    }

    public void setColumn(String column) {
        this.mColumn = column;
    }

    public String getDefaultVal() {
        return mDefaultVal;
    }

    public void setDefaultVal(String defaultVal) {
        this.mDefaultVal = defaultVal;
    }

    public Class<?> getDataType() {
        return mDataType;
    }

    public void setDataType(Class<?> dataType) {
        this.mDataType = dataType;
    }

    public Field getmField() {
        return mField;
    }

    public void setField(Field field) {
        this.mField = field;
    }

    public Method getGetMethod() {
        return mGetMethod;
    }

    public void setGetterMethod(Method getMethod) {
        this.mGetMethod = getMethod;
    }

    public Method getSetMethod() {
        return mSetMethod;
    }

    public void setSetterMethod(Method setMethod) {
        this.mSetMethod = setMethod;
    }
}
