import alt.sqlite2.Annotation.Entity;
import alt.sqlite2.Annotation.EntityId;

/**
 * Created by root on 7/13/15.
 */
@Entity
public class Test3 {
    @EntityId(isAutoIncrement = false)
    public long id;
    public Test2 test2;
}
