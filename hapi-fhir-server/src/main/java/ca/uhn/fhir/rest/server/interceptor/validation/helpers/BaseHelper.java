package ca.uhn.fhir.rest.server.interceptor.validation.helpers;

import ca.uhn.fhir.context.FhirContext;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IBase;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class BaseHelper {

	public static final String GET_PROPERTY_METHOD_NAME = "getProperty";
	public static final String SET_PROPERTY_METHOD_NAME = "setProperty";
	public static final String DEFAULT_DELIMITER = ", ";

	private IBase myBase;

	private String myDelimiter = DEFAULT_DELIMITER;

	private FhirContext myFhirContext;

	public BaseHelper(IBase theBase, FhirContext theFhirContext) {
		if (findGetPropertyMethod(theBase) == null) {
			throw new IllegalArgumentException("Specified base instance does not support property retrieval.");
		}
		myBase = theBase;
		myFhirContext = theFhirContext;
	}

	protected Method getMethod(Object theObject, String theMethodName, Class... theParamClasses) {
		for (Method m : theObject.getClass().getDeclaredMethods()) {
			if (m.getName().equals(theMethodName)) {
				if (theParamClasses.length == 0) {
					return m;
				}
				if (m.getParameterCount() != theParamClasses.length) {
					continue;
				}
				for (int i = 0; i < theParamClasses.length; i++) {
					if (!m.getParameterTypes()[i].isAssignableFrom(theParamClasses[i])) {
						continue;
					}
				}
				return m;
			}
		}
		return null;
	}

	public String getFields(String... theFiledNames) {
		return Arrays.stream(theFiledNames)
			.map(this::get)
			.filter(s -> !StringUtils.isBlank(s))
			.collect(Collectors.joining(getDelimiter()));
	}

	/**
	 * Gets property with the specified name from the provided base class.
	 *
	 * @param thePropertyName Name of the property to get
	 * @return Returns property value converted to string. In case of multiple values, they are joined with the
	 * specified delimiter.
	 */
	public String get(String thePropertyName) {
		return getMultiple(thePropertyName)
			.stream()
			.collect(Collectors.joining(getDelimiter()));
	}

	/**
	 * Sets property or adds to a collection of properties with the specified name from the provided base class.
	 *
	 * @param thePropertyName Name of the property to set or add element to in case property is a collection
	 */
	public void set(String thePropertyName, String theValue) {
		if (theValue == null || theValue.isEmpty()) {
			return;
		}

		try {
			IBase value = myFhirContext.getElementDefinition("string").newInstance(theValue);
			Method setPropertyMethod = findSetPropertyMethod(myBase, int.class, String.class, value.getClass());
			int hashCode = thePropertyName.hashCode();
			setPropertyMethod.invoke(myBase, hashCode, thePropertyName, value);
		} catch (Exception e) {
			throw new IllegalStateException(String.format("Unable to set property %s on %s", thePropertyName, myBase), e);
		}
	}

	/**
	 * Gets property with the specified name from the provided base class.
	 *
	 * @param thePropertyName Name of the property to get
	 * @return Returns property value converted to string. In case of multiple values, they are joined with the
	 * specified delimiter.
	 */
	public List<String> getMultiple(String thePropertyName) {
		Method getPropertyMethod = findGetPropertyMethod(myBase);
		Object[] values;
		try {
			values = (Object[]) getPropertyMethod.invoke(myBase, thePropertyName.hashCode(), thePropertyName, true);
		} catch (Exception e) {
			throw new IllegalStateException(String.format("Instance %s does not supply property %s", myBase, thePropertyName), e);
		}

		return Arrays.stream(values)
			.map(String::valueOf)
			.filter(s -> !StringUtils.isEmpty(s))
			.collect(Collectors.toList());
	}

	private Method findGetPropertyMethod(IBase theAddress) {
		return getMethod(theAddress, GET_PROPERTY_METHOD_NAME);
	}

	private Method findSetPropertyMethod(IBase theAddress, Class... theParamClasses) {
		return getMethod(theAddress, SET_PROPERTY_METHOD_NAME, theParamClasses);
	}

	public String getDelimiter() {
		return myDelimiter;
	}

	public void setDelimiter(String theDelimiter) {
		this.myDelimiter = theDelimiter;
	}

	public IBase getBase() {
		return myBase;
	}
}
