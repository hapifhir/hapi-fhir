package ca.uhn.fhir.model.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import ca.uhn.fhir.model.primitive.BoundCodeDt;
import ca.uhn.fhir.model.primitive.CodeDt;

@Retention(RetentionPolicy.RUNTIME)
@Target(value= {ElementType.TYPE})
public @interface DatatypeDef {

	/**
	 * The defined name of this datatype
	 */
	String name();
	
	/**
	 * Set this to true (default is false) for any types that are
	 * really only a specialization of another type. For example,
	 * {@link BoundCodeDt} is really just a specific type of 
	 * {@link CodeDt} and not a separate datatype, so it should
	 * have this set to true.
	 */
	boolean isSpecialization() default false;
	
}
