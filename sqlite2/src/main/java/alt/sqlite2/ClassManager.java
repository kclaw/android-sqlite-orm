package alt.sqlite2;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import alt.sqlite2.Interface.IClassManager;

/**
 * Created by root on 7/10/15.
 */
public class ClassManager implements IClassManager {
    @Override
    public Method findMethodAnnotatedWith(Class cx, Class<? extends Annotation> annotation) {
        Method method[] = cx.getMethods();
        for(int i=0;i<method.length;i++)
            if(method[i].isAnnotationPresent(annotation))
                return method[i];
        return null;
    }

    @Override
    public Field findFieldAnnotatedWith(Class cx, Class<? extends Annotation> annotation) {
        Field field[] = cx.getFields();
        for(int i=0;i<field.length;i++)
            if(field[i].isAnnotationPresent(annotation))
                return field[i];
        return null;
    }



    @Override
    public boolean isFieldNameExists(Class cx,String name) {
        Field field[] = cx.getFields();
        for(int i=0;i<field.length;i++)
            if(field[i].getName().equals(name))
                return true;
        return false;
    }

    @Override
    public boolean isFieldExist(Class cx, Class fieldType) {
        Field field[] = cx.getFields();
        for(int i=0;i<field.length;i++)
            if(field[i].getType().equals(fieldType))
                return true;
        return false;
    }

    @Override
    public boolean isGenericTypeFieldExists(Class cx, Class collectionFieldType, Class genericType) {
        Field[] fields = cx.getFields();
        for(int i=0;i<fields.length;i++)
            if(isFieldExist(cx,collectionFieldType) && findTypeArgumentInGenericTypeField(fields[i])==genericType)
                return true;
        return false;
    }
    @Override
    public boolean isArrayFieldExists(Class cx,Class array){
        Field[] fields = cx.getFields();
        for(int i=0;i<fields.length;i++) {
            System.out.println(fields[i].getType().getName().charAt(0)=='[');
            System.out.println("jj"+array.getSimpleName()+fields[i].getType().getName()+fields[i].getType().getComponentType());
            if (fields[i].getType().getName().charAt(0)=='[' && fields[i].getType().getComponentType().isAssignableFrom(array))
                return true;
        }
        return false;
    }

    @Override
    public Type findTypeArgumentInGenericTypeField(Field field) {
        if(ParameterizedType.class.isAssignableFrom(field.getGenericType().getClass()))
        return ((ParameterizedType)field.getGenericType()).getActualTypeArguments()[0];
        return  null;
    }

    public Class getTypeArgumentInGenericTypeFieldInClass(Field field){
        if(ParameterizedType.class.isAssignableFrom(field.getGenericType().getClass()))
            return (Class)((ParameterizedType)field.getGenericType()).getActualTypeArguments()[0];
        return  null;
    }

    @Override
    public List<Field> findFieldExists(Class cx, Class fieldType) {
        List<Field> matchfieldList = new ArrayList<Field>();
        Field[] fields = cx.getFields();
        for(int i=0;i<fields.length;i++){
            if(fields[i].getType().getSimpleName().equals(fieldType.getSimpleName()))
                matchfieldList.add(fields[i]);
        }
        if(matchfieldList.size()!=0)
            return matchfieldList;
        return null;
    }


}
