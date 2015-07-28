package alt.sqlite2.Interface;

/**
 * Created by root on 7/8/15.
 */
public interface IPrimitiveDataTypeMapper {
    String mapToByteType();
    String mapToShortType();
    String mapToIntType();
    String mapToLongType();
    String mapToFloatType();
    String mapToDoubleType();
    String mapToBooleanType();
    String mapToCharType();
    Class mapFromByteType();
    Class mapFromShortType();
    Class mapFromIntType();
    Class mapFromLongType();
    Class mapFromFloatType();
    Class mapFromDoubleType();
    Class mapFromBooleanType();
    Class mapFromCharType();
}
