package ca.uhn.fhir.model.api.annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to mark a field as sensitive, indicating that it should not
 * be displayed or serialized.
 */
@Retention(RetentionPolicy.RUNTIME) // Make this annotation available at runtime.
@Target(ElementType.FIELD) // This annotation can only be applied to fields.
public @interface SensitiveNoDisplay {
}
