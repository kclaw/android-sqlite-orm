package alt.sqlite2.Interface;

/**
 * Created by root on 7/11/15.
 */
public interface IRelationship {
    void setRelationshipType(RelationshipType type);
    RelationshipType getRelationshipType();
    void setEntity1(IEntity entity);
    void setEntity2(IEntity entity);
    IEntity getEntity1();
    IEntity getEntity2();
}
