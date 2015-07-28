package alt.sqlite2;

import java.lang.reflect.Field;

import alt.sqlite2.Annotation.Entity;
import alt.sqlite2.Annotation.EntityId;
import alt.sqlite2.Interface.IEntityCheck;

/**
 * Created by root on 7/10/15.
 */
public class EntityChecker implements IEntityCheck {
    @Override
    public boolean checkForValidEntity(Class cx) {
        return cx.isAnnotationPresent(Entity.class);
    }

    @Override
    public boolean checkForValidEntityId(Class cx) {
        for(Field field:cx.getDeclaredFields()){
            if(field.isAnnotationPresent(EntityId.class))
                return true;
        }
        return false;
    }

    @Override
    public boolean isEntityCreated(Class cx) {
        return false;
    }

    @Override
    public boolean isEntityCreated(String name) {
        return false;
    }

    @Override
    public boolean isJoinTableCreated(Class cx1, Class cx2) {
        return false;
    }
}
