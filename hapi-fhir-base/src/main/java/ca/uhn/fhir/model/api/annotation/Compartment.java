package ca.uhn.fhir.model.api.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(value= {})
public @interface Compartment {

	/**
	 * This may only be populated on a reference field. On such a field, places the containing
	 * resource in a compartment with the name(s) specified by the given strings, where the compartment
	 * belongs to the target resource. For example, this field could be populated with <code>Patient</code> on 
	 * the <code>Observation.subject</code> field.
	 */
	String name();
	
}
