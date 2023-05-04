package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.model.primitive.BooleanDt;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.hl7.fhir.instance.model.api.IBaseBooleanDatatype;
import org.hl7.fhir.instance.model.api.IBaseCoding;
import org.hl7.fhir.validation.BaseValidator;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * We are trying to preserve null behaviour despite IBaseCoding using primitive boolean for userSelected.
 */
public class CodingSpy {
	final Map<Class, Field> mySpies = new HashMap<>();

	/**
	 * Reach into the Coding and pull out the Boolean instead of the boolean.
	 * @param theValue the Coding, or CodingDt
	 * @return the Boolean
	 */
	public Boolean getBooleanObject(IBaseCoding theValue) {
		Field spy = getSpy(theValue);
		try {
			Object o = spy.get(theValue);
			if (o == null) {
				return null;
			}
			if (o instanceof BooleanDt) {
				BooleanDt booleanDt = (BooleanDt) o;
				return booleanDt.getValue();
			}
			if (o instanceof IBaseBooleanDatatype) {
				IBaseBooleanDatatype booleanValue = (IBaseBooleanDatatype) o;
				return booleanValue.getValue();
			}
			if (o instanceof Boolean) {
				return (Boolean) o;
			}
			throw new RuntimeException("unsupported type :" + theValue.getClass().getName());
		} catch (IllegalAccessException theException) {
			// should never happen - all Coding models have this field.
			throw new RuntimeException(theException);
		}
	}

	private Field getSpy(IBaseCoding theValue) {
		return mySpies.computeIfAbsent(theValue.getClass(), k -> getFieldHandle(k));
	}

	private static Field getFieldHandle(Class k) {
		Field result = FieldUtils.getField(k, "userSelected", true);
		if (result == null) {
			result = FieldUtils.getField(k, "myUserSelected", true);
		}
		return result;
	}

}
