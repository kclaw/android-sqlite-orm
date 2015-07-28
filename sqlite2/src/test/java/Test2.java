import java.util.List;

import alt.sqlite2.Annotation.Entity;
import alt.sqlite2.Annotation.EntityId;

/**
 * Created by root on 7/10/15.
 */
@Entity
public class Test2 {
    @EntityId(isAutoIncrement = true)
    public long id;
    public Test3[] test3;
    public List<Test1> mTest1;
}
