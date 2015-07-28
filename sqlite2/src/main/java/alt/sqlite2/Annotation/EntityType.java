package alt.sqlite2.Annotation;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by root on 4/4/15.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface EntityType {
    Class type();
    boolean isGenericType() default false;
    boolean isArray() default false;
}