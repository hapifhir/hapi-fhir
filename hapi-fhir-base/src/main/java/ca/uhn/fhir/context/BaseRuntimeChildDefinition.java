package ca.uhn.fhir.context;

import static org.apache.commons.lang3.StringUtils.*;

import java.lang.reflect.Field;

public class BaseRuntimeChildDefinition {

	private String myElementName;
	private Field myField;
	private int myMin;
	private int myMax;

	BaseRuntimeChildDefinition(Field theField, String theElementName, int theMin, int theMax) throws ConfigurationException {
		super();
		if (theField == null) {
			throw new IllegalArgumentException("No field speficied");
		}
		if (isBlank(theElementName)) {
			throw new ConfigurationException("Element name can not be blank");
		}
		if (theMin < 0) {
			throw new ConfigurationException("Min must be >= 0");
		}
		if (theMax != -1 && theMax < theMin) {
			throw new ConfigurationException("Max must be >= Min (unless it is -1 / unlimited)");
		}
		
		myField=theField;
		myElementName = theElementName;
		myMin=theMin;
		myMax=theMax;
	}

	public int getMin() {
		return myMin;
	}

	public int getMax() {
		return myMax;
	}

	public Field getField() {
		return myField;
	}

	public String getElementName() {
		return myElementName;
	}
	
}
