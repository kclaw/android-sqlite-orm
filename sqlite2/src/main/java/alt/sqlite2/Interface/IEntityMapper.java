package alt.sqlite2.Interface;


/**
 * Created by root on 7/11/15.
 */
public interface IEntityMapper {
    String mapToEntity();
    String mapToEntityId();
    String mapToEntityArray(RelationshipType relationshipType);
    String mapToEntityList(RelationshipType relationshipType);
    Class mapFromEntity();
    Class mapFromEntityId();
    Class mapFromEntityArray(RelationshipType relationshipType);
    Class mapFromEntityList(RelationshipType relationshipType);
}
