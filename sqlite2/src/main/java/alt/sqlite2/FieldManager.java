package alt.sqlite2;

import android.util.Log;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import alt.sqlite2.Interface.IFieldManager;

/**
 * Created by root on 7/8/15.
 */
public class FieldManager implements IFieldManager {
    protected Field mField;
    protected Map<Class<?>, Class<?>> PRIMITIVES_TO_WRAPPERS;
    protected Map<Class<?>,String> mDataTypeToType;

    public FieldManager(Field field){
        this.mField = field;
        mDataTypeToType = new HashMap<Class<?>,String>();
        mDataTypeToType.put(String.class,"text");
        mDataTypeToType.put(Long.TYPE,"integer");
        mDataTypeToType.put(Integer.TYPE,"integer");
        PRIMITIVES_TO_WRAPPERS = new HashMap<Class<?>,Class<?>>();
        PRIMITIVES_TO_WRAPPERS.put(boolean.class,Boolean.class);
        PRIMITIVES_TO_WRAPPERS.put(byte.class,Byte.class);
        PRIMITIVES_TO_WRAPPERS.put(char.class, Character.class);
        PRIMITIVES_TO_WRAPPERS.put(double.class, Double.class);
        PRIMITIVES_TO_WRAPPERS.put(float.class, Float.class);
        PRIMITIVES_TO_WRAPPERS.put(int.class, Integer.class);
        PRIMITIVES_TO_WRAPPERS.put(long.class, Long.class);
        PRIMITIVES_TO_WRAPPERS.put(short.class, Short.class);
        PRIMITIVES_TO_WRAPPERS.put(void.class, Void.class);
    }
    @Override
    public String getDataType() {
        System.out.println(mField.getType().getSimpleName());
        System.out.println(List.class.isAssignableFrom(mField.getType()));
        String fieldName = mField.getName()+" ";
        switch(mField.getType().getSimpleName()){
            case "byte":
                return fieldName+mapToByteType();
            case "short":
                return fieldName+mapToShortType();
            case "int":
                return fieldName+mapToIntType();
            case "long":
                return fieldName+mapToLongType();
            case "float":
                return fieldName+mapToFloatType();
            case "double":
                return fieldName+mapToDoubleType();
            case "boolean":
                return fieldName+mapToBooleanType();
            case "char":
                return fieldName+mapToCharType();
            case "byte[]":
                return fieldName+mapToByteArray();
            case "short[]":
                return fieldName+mapToShortArray();
            case "int[]":
                return fieldName+mapToIntArray();
            case "long[]":
                return fieldName+mapToLongArray();
            case "float[]":
                return fieldName+mapToFloatArray();
            case "double[]":
                return fieldName+mapToDoubleArray();
            case "boolean[]":
                return fieldName+mapToBooleanArray();
            case "char[]":
                return fieldName+mapToCharArray();
        }
        if(isAssignableFromListInterface())
            //return fieldName+mapToList();
            return null;
        if(isAssignableFromSetInterface())
            //return fieldName+mapToSet();
            return null;
        return null;
    }

    @Override
    public Class getType() {
        //if(mField.getType().getName().charAt(0)=='[')
          //  return byte[].class;
        //return this.mField.getType().isPrimitive()?PRIMITIVES_TO_WRAPPERS.get(this.mField.getType()):mField.getType();
        return this.mField.getType();
    }

    @Override
    public <T> T getValue(Object object, Class<T> tClass) throws IllegalAccessException {
        //Log.i(this.getClass().getSimpleName(), "getValue(Object,Class),start");

        if(mField.getType().getName().charAt(0)=='['){
            T returnValue = null;
            try {
                String value = "";
                Method method = Arrays.class.getMethod("toString",mField.getType());
                value = (String)method.invoke(object, mField.getType().cast(mField.get(object)));
                returnValue = (T)value.getBytes();
            } catch (NoSuchMethodException e) {
                //Log.e(this.getClass().getSimpleName(),"getValue(Object,Class),NoSuchMethodException");
            } catch (InvocationTargetException e) {
                //Log.e(this.getClass().getSimpleName(), "getValue(Object,Class),InvocationTargetException");
            }catch(Exception e){
                //Log.e(this.getClass().getSimpleName(),"getValue(Object,Class)"+e.getMessage());
            }
            return returnValue;
        }
        return (T)mField.get(object);
    }

    @Override
    public void setValue(Object object, Object value) throws IllegalAccessException {
        if(mField.getType().getName().charAt(0)=='['){
            String sValue = new String((byte[])value);
            String[] splitValue = sValue.substring(1,sValue.length()-1).split(",");
            Object o =  Array.newInstance(mField.getType().getComponentType(), splitValue.length);
            mField.set(object,o);
        }else if(isAssignableFromListInterface()){

        }else if(isAssignableFromSetInterface()){

        }else
            mField.set(object,value);
    }

    @Override
    public boolean isAssignableFromListInterface() {
        return mField.getType().isAssignableFrom(List.class);
    }

    @Override
    public boolean isAssignableFromSetInterface() {
        return mField.getType().isAssignableFrom(Set.class);
    }

    @Override
    public Type getTypeInGenericType() {
        ParameterizedType pt = (ParameterizedType)mField.getGenericType();
        return pt.getActualTypeArguments()[0];
    }

    @Override
    public Class getClassInGenericType() {
        ParameterizedType pt = (ParameterizedType)mField.getGenericType();
        return (Class)pt.getActualTypeArguments()[0];
    }

    @Override
    public boolean isPrimitive() {
        return mField.getType().isPrimitive();
    }

    @Override
    public boolean isArray() {
        return mField.getType().isArray();
    }

    @Override
    public String mapToByteArray() {
        return "BLOB";
    }

    @Override
    public String mapToShortArray() {
        return "BLOB";
    }

    @Override
    public String mapToIntArray() {
        return "BLOB";
    }

    @Override
    public String mapToLongArray() {
        return "BLOB";
    }

    @Override
    public String mapToFloatArray() {
        return "BLOB";
    }

    @Override
    public String mapToDoubleArray() {
        return "BLOB";
    }

    @Override
    public String mapToBooleanArray() {
        return "BLOB";
    }

    @Override
    public String mapToCharArray() {
        return "TEXT";
    }

    @Override
    public Class mapFromByteArray() {
        return byte[].class;
    }

    @Override
    public Class mapFromShortArray() {
        return short[].class;
    }

    @Override
    public Class mapFromIntArray() {
        return null;
    }

    @Override
    public Class mapFromLongArray() {
        return null;
    }

    @Override
    public Class mapFromFloatArray() {
        return null;
    }

    @Override
    public Class mapFromDoubleArray() {
        return null;
    }

    @Override
    public Class mapFromBooleanArray() {
        return null;
    }

    @Override
    public Class mapFromCharArray() {
        return null;
    }

    @Override
    public String mapToList() {
        return "BLOB";
    }

    @Override
    public Class mapFromList() {
        return null;
    }

    @Override
    public String mapToByteType() {
        return "INTEGER";
    }

    @Override
    public String mapToShortType() {
        return "INTEGER";
    }

    @Override
    public String mapToIntType() {
        return "INTEGER";
    }

    @Override
    public String mapToLongType() {
        return "INTEGER";
    }

    @Override
    public String mapToFloatType() {
        return "REAL";
    }

    @Override
    public String mapToDoubleType() {
        return "REAL";
    }

    @Override
    public String mapToBooleanType() {
        return "INTEGER";
    }

    @Override
    public String mapToCharType() {
        return "INTEGER";
    }

    @Override
    public Class mapFromByteType() {
        return null;
    }

    @Override
    public Class mapFromShortType() {
        return null;
    }

    @Override
    public Class mapFromIntType() {
        return null;
    }

    @Override
    public Class mapFromLongType() {
        return null;
    }

    @Override
    public Class mapFromFloatType() {
        return null;
    }

    @Override
    public Class mapFromDoubleType() {
        return null;
    }

    @Override
    public Class mapFromBooleanType() {
        return null;
    }

    @Override
    public Class mapFromCharType() {
        return null;
    }

    @Override
    public String mapToSet() {
        return "BLOB";
    }

    @Override
    public Class mapFromSet() {
        return null;
    }

    @Override
    public String mapToStringClass() {
        return "TEXT";
    }

    @Override
    public Class mapFromString() {
        return null;
    }
}
