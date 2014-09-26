package cn.core.mobile.library.database.sqlite;

import android.database.Cursor;

import cn.core.mobile.library.database.table.Property;
import cn.core.mobile.library.database.table.TableInfo;


/**
 * Created by dallas on 14-5-3.
 */
public class CursorUtils {
    public static <T> T getEntity(Cursor cursor, TableInfo tableInfo) {

        try {
            if (cursor != null && cursor.getColumnCount() > 0) {
                T entity = (T) tableInfo.getEntityClazz().newInstance();

                for (int i = 0; i < cursor.getColumnCount(); i++) {
                    String column = cursor.getColumnName(i);

                    Property property = tableInfo.propertyMap.get(column);
                    if (property != null) {
                        if (property.getDataType().equals(Byte[].class)
                                || property.getDataType().equals(byte[].class)) {
                            property.setValue(entity, cursor.getBlob(i));
                        } else {
                            property.setValue(entity, cursor.getString(i));
                        }
                    } else {
                        if (tableInfo.getId().getColumn().equals(column)) {
                            tableInfo.getId().setValue(entity, cursor.getString(i));
                        }
                    }

                }
                return entity;
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
