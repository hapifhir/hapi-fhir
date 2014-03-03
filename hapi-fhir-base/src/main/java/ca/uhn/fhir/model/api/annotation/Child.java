package ca.uhn.fhir.model.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import ca.uhn.fhir.model.api.IElement;
import ca.uhn.fhir.model.api.IValueSetEnumBinder;

@Retention(RetentionPolicy.RUNTIME)
@Target(value= {ElementType.FIELD})
public @interface Child {

	/**
	 * Constant value to supply for {@link #order()} when the order is defined
	 * elsewhere
	 */
	int ORDER_UNKNOWN = -1;
	
	/**
	 * COnstant value to supply for {@link #max()} to indicate '*' (no maximum)
	 */
	int MAX_UNLIMITED = -1;

	String name();
	
	int order();

	int min() default 0;

	int max() default 1;

	Choice choice() default @Choice();
	
	Class<? extends IElement> type() default NoDatatype.class;

	/**
	 * Indicator that the type attribute is not set
	 */
	public class NoDatatype implements IElement
	{
		private NoDatatype() {
			// non instantiable
		}
	}
	
}
