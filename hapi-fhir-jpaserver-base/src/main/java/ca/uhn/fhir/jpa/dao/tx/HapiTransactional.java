package ca.uhn.fhir.jpa.dao.tx;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @see HapiTransactionalAspect
 * @since 5.1.0
 */
@Retention(RUNTIME)
@Target({METHOD, TYPE})
public @interface HapiTransactional {
}
