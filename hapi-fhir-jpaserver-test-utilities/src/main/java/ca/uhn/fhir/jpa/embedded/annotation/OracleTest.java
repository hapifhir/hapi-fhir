package ca.uhn.fhir.jpa.embedded.annotation;

import ca.uhn.fhir.jpa.embedded.OracleCondition;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@ExtendWith(OracleCondition.class)
public @interface OracleTest {}
