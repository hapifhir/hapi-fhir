package ca.uhn.fhir.batch2.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * This annotation should be added to any job parameters model fields
 * that contain a password or other credentials. Data in such a field will
 * be available to step workers, but will not be exposed to outside users/clients.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface PasswordField {
}
