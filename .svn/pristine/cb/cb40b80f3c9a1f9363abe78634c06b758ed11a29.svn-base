package com.verizontelematics.indrivemobile.database;

import android.content.ContentValues;

import com.verizontelematics.indrivemobile.database.tables.BaseTable;
import com.verizontelematics.indrivemobile.models.data.BaseData;

import java.lang.reflect.Field;

/**
 * Created by bijesh on 10/10/2014.
 */
public class ContentManagerImpl<T extends BaseData> extends ContentManager {

    @Override
    public ContentValues generateContent(BaseData data) {

        return null;
    }

    public <T extends BaseTable>ContentValues generateContent(BaseData data,Class<T> cls,IgnoreColumn ignoreColumn) {
        ContentValues retValue = new ContentValues();
        try {

            Field[] allFields = cls.getDeclaredFields();
            for (Field field : allFields) {
                String fieldName = field.getName();
                if (fieldName.equals(ignoreColumn.toString()))
                    continue;

                if(fieldName.startsWith(SQLType.COLUMN.toString())){
                    String columnValue = field.get(new Object()).toString();
                    DataContainer container = getDataValue(columnValue,data);
                    if(container != null) {
//                        System.out.println("$$$ class type "+container.cls);
//                        if(container.cls != null && container.value != null) {
                        if (container.cls == String.class) {
//                            System.out.println(columnValue + " class type " + "String");
//                                System.out.println("is value null "+container.value);

                            retValue.put(columnValue, (String)container.value);
                        }
                        else if (container.cls == Double.class || container.cls == double.class) {
//                                System.out.println("its double");
                            if(container.value != null)
                                retValue.put(columnValue, (Double) container.value);
                        }else if (container.cls == Float.class || container.cls == float.class) {
//                                System.out.println("its double");
                            if(container.value != null)
                                retValue.put(columnValue, (Float) container.value);
                        }else if (container.cls == Long.class || container.cls == long.class) {
//                                System.out.println("its double");
                            if(container.value != null)
                                retValue.put(columnValue, (Long) container.value);
                        }else if (container.cls == Integer.class || container.cls == int.class) {
//                                System.out.println("its double");
                            if(container.value != null)
                                retValue.put(columnValue, (Integer) container.value);
                        }else if (container.cls == Short.class || container.cls == short.class) {
//                                System.out.println("its double");
                            if(container.value != null)
                                retValue.put(columnValue, (Short) container.value);
                        }else if (container.cls == Byte.class || container.cls == byte.class) {
//                                System.out.println("its double");
                            if(container.value != null)
                                retValue.put(columnValue, (Byte) container.value);
                        }
//                        }
                    }
//                    retValue.put(columnValue,getDataValue(columnValue,data));
                }
//            retValue.put()
            }
        }catch (IllegalAccessException e){
            e.printStackTrace();
        }
        return retValue;
    }

    private DataContainer getDataValue(String columnValue,BaseData data) throws IllegalAccessException{
        DataContainer retValue = null;
        Class cls = data.getClass();
        Field[] allFields = cls.getDeclaredFields();
        for(Field field:allFields){
            String fieldName = field.getName();
            field.setAccessible(true);
//            System.out.println("field name "+fieldName+" col val "+columnValue);
            if(columnValue.equals(fieldName)){
                Object objVal = field.get(data);
                Class cs = field.getType();
                retValue = new DataContainer(cs,objVal);
//                System.out.println("field name "+fieldName+" col val "+columnValue);
//                System.out.println("value is "+retValue);
            }
        }
        return retValue;
    }
}
