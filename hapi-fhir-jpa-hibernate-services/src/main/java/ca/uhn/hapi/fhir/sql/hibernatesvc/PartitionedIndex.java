package ca.uhn.hapi.fhir.sql.hibernatesvc;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({})
public @interface PartitionedIndex {

	String name();

	String[] columns();
}
