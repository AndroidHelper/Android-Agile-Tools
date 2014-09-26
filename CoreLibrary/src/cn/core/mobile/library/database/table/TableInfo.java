package cn.core.mobile.library.database.table;


import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;

import cn.core.mobile.library.database.exception.DbException;
import cn.core.mobile.library.database.utils.ClassUtils;
import cn.core.mobile.library.database.utils.FieldUtils;

/**
 * Created by dallas on 14-4-13.
 */
public class TableInfo {
    private Class<?> mEntityClazz;
    private String mClassName;
    private String mTableName;
    private Property mId;
    private boolean tableExist;
    private static final HashMap<String, TableInfo> mTableInfoMap = new HashMap<String, TableInfo>();
    public final HashMap<String, Property> propertyMap = new HashMap<String, Property>();

    private TableInfo() {
    }

    /**
     * 根据指定Class对象构建表相关信息
     *
     * @param clazz Class对象
     * @return 表相关信息TableInfo对象
     * @throws DbException
     */
    public static TableInfo get(Class<?> clazz) {
        if (clazz == null)
            throw new DbException("table info get error, because the clazz is null");
        TableInfo tableInfo = mTableInfoMap.get(clazz.getName());
        if (tableInfo == null) {
            tableInfo = new TableInfo();

            tableInfo.mEntityClazz = clazz;
            tableInfo.mTableName = ClassUtils.getTableName(clazz);
            tableInfo.mClassName = clazz.getName();

            List<Field> fieldList = FieldUtils.getAllDeclaredFields(clazz);
            for (Field field : fieldList) {
                // 如果是静态变量则跳过。
                if (FieldUtils.isStaticField(field)) {
                    continue;
                }

                // 如果被标注为瞬时态的字段则跳过
                if (FieldUtils.isTransientField(field)) {
                    continue;
                }

                if (FieldUtils.isBaseDateType(field)) {
                    Property property = new Property();

                    property.setColumn(FieldUtils.getColumnByField(field));
                    property.setFieldName(field.getName());
                    property.setDataType(field.getType());
                    property.setDefaultVal(FieldUtils.getPropertyDefaultValue(field));
                    property.setSetterMethod(FieldUtils.getFieldSetterMethod(clazz, field));
                    property.setGetterMethod(FieldUtils.getFieldGetterMethod(clazz, field));
                    property.setField(field);

                    if (tableInfo.getId() == null && FieldUtils.isPrimaryKeyField(field)) {
                        tableInfo.mId = property;
                    } else {
                        tableInfo.propertyMap.put(property.getColumn(), property);
                    }
                }
            }

            if (tableInfo.getId() == null) {
                throw new DbException("the class[" + clazz + "]'s idField is null , \n you can define _id property or use annotation @Id to solution this exception");
            }

            mTableInfoMap.put(clazz.getName(), tableInfo);

        }

        return tableInfo;
    }

    public String getClassName() {
        return mClassName;
    }

    public String getTableName() {
        return mTableName;
    }

    public Property getId() {
        return mId;
    }

    public boolean isTableExist() {
        return tableExist;
    }

    public void setTableExist(boolean tableExist) {
        this.tableExist = tableExist;
    }

    public Class<?> getEntityClazz() {
        return mEntityClazz;
    }
}
