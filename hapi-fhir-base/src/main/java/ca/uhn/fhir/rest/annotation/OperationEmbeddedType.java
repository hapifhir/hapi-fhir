package ca.uhn.fhir.rest.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// LUKETODO: consider not using this but instead using ONLY the replacemenet for @OperationParam
// LUKETODO:  better name?
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.TYPE})
public @interface OperationEmbeddedType {
	/**
	 * The name of the embedded type
	 */
	// LUKETODO:  default ""???
	String name() default "";

	// LUKETODO:  javadoc:  name of the type
	// LUKETODO:  extends and defaults what????
	//	Class<? extends IBase> type() default IBase.class;
	Class<?> type() default Object.class;
}
