package ca.uhn.fhir.context;

import static org.apache.commons.lang3.StringUtils.*;

import java.lang.reflect.Field;
import java.util.Set;

public abstract class BaseRuntimeChildDefinition {

	private Field myField;
	private int myMin;
	private int myMax;
	private String myElementName;

	BaseRuntimeChildDefinition(Field theField, int theMin, int theMax, String theElementName) throws ConfigurationException {
		super();
		if (theField == null) {
			throw new IllegalArgumentException("No field speficied");
		}
		if (theMin < 0) {
			throw new ConfigurationException("Min must be >= 0");
		}
		if (theMax != -1 && theMax < theMin) {
			throw new ConfigurationException("Max must be >= Min (unless it is -1 / unlimited)");
		}
		if (isBlank(theElementName)) {
			throw new ConfigurationException("Element name must not be blank");
		}
		
		myField=theField;
		myMin=theMin;
		myMax=theMax;
		myElementName = theElementName;
	}

	public String getElementName() {
		return myElementName;
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
	
	public abstract Set<String> getValidChildNames();
	
	public abstract BaseRuntimeElementDefinition<?> getChildByName(String theName);
	
}
