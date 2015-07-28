package alt.sqlite2;

import alt.sqlite2.Interface.IEntity;
import alt.sqlite2.Interface.IRelationship;
import alt.sqlite2.Interface.RelationshipType;

/**
 * Created by root on 7/12/15.
 */
public class Relationship implements IRelationship {
    protected RelationshipType mRelationshipType;
    protected IEntity mEntity1;
    protected IEntity mEntity2;

    public Relationship(IEntity entity1,IEntity entity2,RelationshipType relationshipType){
        this.mEntity1 = entity1;
        this.mEntity2 = entity2;
        this.mRelationshipType = relationshipType;
    }
    @Override
    public void setRelationshipType(RelationshipType type) {
        this.mRelationshipType = type;
    }

    @Override
    public RelationshipType getRelationshipType() {
        return this.mRelationshipType;
    }

    @Override
    public void setEntity1(IEntity entity) {
        this.mEntity1 = entity;
    }

    @Override
    public void setEntity2(IEntity entity) {
        this.mEntity2 = entity;
    }

    @Override
    public IEntity getEntity1() {
        return this.mEntity1;
    }

    @Override
    public IEntity getEntity2() {
        return this.mEntity2;
    }
}
