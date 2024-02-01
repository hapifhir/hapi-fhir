package ca.uhn.fhir.model.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to mark a field as sensitive, indicating that it should not
 * be displayed or serialized by jackson. The only way to serialize an object annotated with this annotation is to use
 * {@link ca.uhn.fhir.util.JsonUtil}, as it has a registered filter against this annotation.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface SensitiveNoDisplay {}
