package ca.uhn.fhir.model.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

import ca.uhn.fhir.model.api.IDatatype;

@Target(value= {ElementType.FIELD})
public @interface Extension {

	String url();
	
	Class<? extends IDatatype> datatype() default NoDatatype.class;
	
	public static class NoDatatype implements IDatatype
	{
		private NoDatatype() {
			// non-instantiable
		}
	}
	
}
