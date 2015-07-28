package alt.sqlite2.Interface;

/**
 * Created by root on 7/9/15.
 */
public interface IEntityCheck {
    boolean checkForValidEntity(Class cx);
    boolean checkForValidEntityId(Class cx);
    boolean isEntityCreated(Class cx);
    boolean isEntityCreated(String name);
    boolean isJoinTableCreated(Class cx1,Class cx2);
}
