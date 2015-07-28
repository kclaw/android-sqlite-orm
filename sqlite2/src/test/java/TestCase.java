
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import alt.sqlite2.*;
import alt.sqlite2.Annotation.Entity;
import alt.sqlite2.Annotation.EntityId;
import alt.sqlite2.Interface.IEntity;

/**
 * Created by root on 7/8/15.
 */
public class TestCase {

    public static void main(String argv[]) throws NoSuchFieldException {
        Test5 test = new Test5();
        /*Field field = test.getClass().getField("iambyte");
        FieldManager fieldManager = new FieldManager(field);
        System.out.print(fieldManager.getDataType());*/
        /*SQLiteDBSchemaEntityCreator ii = new SQLiteDBSchemaEntityCreator(Test.class);
        ii.generateDBSchema();
        System.out.println(ii.mSQlDDLStatement);*/
        //System.out.print(ii.checkForValidEntity(Test2.class));
       // System.out.println(test.getClass().isAnnotationPresent(Entity.class));
        System.out.println(new ClassManager().isFieldExist(Test5.class,Test2.class));
       /* Field field = Test3.class.getField("test2List");
            ParameterizedType pt = (ParameterizedType)field.getGenericType();
            String innerClass = pt.getActualTypeArguments()[0].toString().replace("class ", "");
            System.out.println(innerClass); // true*/

//        System.out.println(new ClassManager().findTypeArgumentInGenericTypeField(Test3.class.getField("test2List"))==Test2.class);
        System.out.println(new ClassManager().isGenericTypeFieldExists(Test3.class,List.class,Test2.class));

        DataBase db = new DataBase();
        db.createEntities(new File("/home/lkc/Desktop/entity.jar"));
        SQLiteDataBaseCreator dbc = new SQLiteDataBaseCreator(db);
        String sql = dbc.generateDBSchema();
        System.out.print(sql);
        String[] sqls = sql.split("(?<=;)");
        System.out.println();
        for(int i=0;i<sqls.length;i++)
        System.out.println(sqls[i]);
        IEntity en1 = new alt.sqlite2.Entity("Hello");
        IEntity en2 = new alt.sqlite2.Entity("World");
        IEntity en3 = new alt.sqlite2.Entity("Key");
        IEntity e1 = new JoinTableModel(en1,en2);
        IEntity e2 = new JoinTableModel(en2,en1);
        IEntity e3 = new JoinTableModel(en3,en1);
        System.out.println(e1.equals(e2));
        System.out.println(e3.equals(e1));

        System.out.println(db.findEntityByName("Test1"));
        Test5<String> test5 = new Test5<String>();
        //if(ParameterizedType.class.isAssignableFrom(test5.getClass().getGenericSuperclass()))

       // System.out.println("gjh"+((ParameterizedType)test5.getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
        /*IFileManager fileManager = new FileManager();
        try {
            List<Class<?>> cx = fileManager.findClassesInJarFile(new File("/home/lkc/Desktop/entity.jar"), null);
            for(Class cc:cx)
                System.out.println(cc.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        Test1 test1 = new Test1();
       // SQLiteEntityCommand2<Test1> entityCommand = new SQLiteEntityCommand2<Test1>();
        //if(entityCommand.insertEntity(test1).getClass().isAssignableFrom(Test1.class))
            System.out.print("hihihi");
        Test5 test51 = new Test5();
        test51.id = 56;
        /*SQLiteCommandAdapter commandAdapter = new SQLiteCommandAdapter(test51);
        for(int i=0;i<commandAdapter.getColumnName().length;i++)
            System.out.println(commandAdapter.getColumnName()[i]);
        try {
            System.out.print(commandAdapter.getValueByColumnName("hello", commandAdapter.getTypeByColumnName("hello")));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }*/
        //SQLiteEntityAdapter entityAdapter = new SQLiteEntityAdapter(test51);
        System.out.println(db);
        EntityFieldManager entityFieldManager = new EntityFieldManager(db,Test2.class.getField("mTest1"));
        System.out.println(entityFieldManager.getDataType(db.findEntityByClass(Test2.class)));
        System.out.println(new ClassManager().isArrayFieldExists(Test2.class, Test3.class));
        System.out.println(Test3.class.isAssignableFrom(Test3.class));
        System.out.println(Test2.class.getField("test3").getType().getName().charAt(0)=='[');
      //  System.out.println(entityFieldManager.getEntityTypeInGenericType(Test2.class.getField("mTest1")));
        TypeConverter typeConverter = new TypeConverter();
        short[] shorts = new short[]{0,2,3,4};
        System.out.println(Arrays.toString(typeConverter.convertToByteArray(shorts)));

        Test1 test111 = new Test1();
        Test3 test3331 = new Test3();
        test3331.id = 21;
        Test3 test3332 = new Test3();
        test3332.id = 56;
        Test2 test2221 = new Test2();
        test2221.id = 66;

        test2221.test3 = new Test3[]{test3331,test3332};
        Test2 test2222 = new Test2();
        test2222.id = 77;
        Test2 test2223 = new Test2();
        test2223.id = 88;
        Test1 test11 = new Test1();
        test11.id = 99;
        Test1 test12 = new Test1();
        test12.id = 7;
        Test1 test13 = new Test1();
        test13.id = 3;
        List<Test1> list1 = new ArrayList<>();
        list1.add(test11);
        list1.add(test12);
        list1.add(test13);
        List<Test2> list = new ArrayList<>();
        list.add(test2221);
        list.add(test2222);
        list.add(test2223);
        test2221.mTest1 = list1;
        test111.mtest2list = list;
        Test3 test3 = new Test3();
        test3.id = 6;

        Test2 test2 = new Test2();
        test2.id = 100;
        test3.test2 = test2;
        SQLiteCommandAdapter<Test3> commandAdapter = new SQLiteCommandAdapter<>(db,test3);
        System.out.println(Arrays.toString(commandAdapter.getColumnName()));
        System.out.println(commandAdapter.getTypeByColumnName("test2"));

        SQLiteCommandAdapter<Test2> commandAdapter1 = new SQLiteCommandAdapter<>(db,test2221);
        System.out.println(commandAdapter1.getTypeByColumnName("test3"));

        SQLiteCommandAdapter<Test1> newCommandAdapter = new SQLiteCommandAdapter<>(db,test111);
        try {
            System.out.println(newCommandAdapter.getValueByColumnName("mtest2list", newCommandAdapter.getTypeByColumnName("mtest2list")));
            byte[] longs = (byte[])newCommandAdapter.getValueByColumnName("mtest2list", newCommandAdapter.getTypeByColumnName("mtest2list"));
            System.out.println(Arrays.toString(longs));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        try {
            byte[] result = (byte[])commandAdapter1.getValueByColumnName("test3",commandAdapter1.getTypeByColumnName("test3"));
            System.out.println(Arrays.toString(result));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        SQLiteEntityCommand<Test2> command = new SQLiteEntityCommand<Test2>(db,null);
        command.insertEntity(test2221);
    }
    @Entity
    public static class Test5<T> {
        @EntityId(isAutoIncrement = true)
        public long id;
        public String hello = "stupid";

    }

    public class Test8{
        public List<Test5> test2List;
    }
}
