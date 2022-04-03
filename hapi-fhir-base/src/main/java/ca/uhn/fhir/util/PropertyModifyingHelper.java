package ca.uhn.fhir.util;

/*-
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IBase;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Helper class for handling updates of the instances that support property modification via <code>setProperty</code>
 * and <code>getProperty</code> methods.
 */
public class PropertyModifyingHelper {

	public static final String GET_PROPERTY_METHOD_NAME = "getProperty";
	public static final String SET_PROPERTY_METHOD_NAME = "setProperty";
	public static final String DEFAULT_DELIMITER = ", ";

	private IBase myBase;

	private String myDelimiter = DEFAULT_DELIMITER;

	private FhirContext myFhirContext;

	/**
	 * Creates a new instance initializing the dependencies.
	 *
	 * @param theFhirContext FHIR context holding the resource definitions
	 * @param theBase        The base class to set properties on
	 */
	public PropertyModifyingHelper(FhirContext theFhirContext, IBase theBase) {
		if (findGetPropertyMethod(theBase) == null) {
			throw new IllegalArgumentException(Msg.code(1771) + "Specified base instance does not support property retrieval.");
		}
		myBase = theBase;
		myFhirContext = theFhirContext;
	}

	/**
	 * Gets the method with the specified name and parameter types.
	 *
	 * @param theObject       Non-null instance to get the method from
	 * @param theMethodName   Name of the method to get
	 * @param theParamClasses Parameters types that method parameters should be assignable as
	 * @return Returns the method with the given name and parameters or null if it can't be found
	 */
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

	/**
	 * Gets all non-blank fields as a single string joined with the delimiter provided by {@link #getDelimiter()}
	 *
	 * @param theFiledNames Field names to retrieve values for
	 * @return Returns all specified non-blank fileds as a single string.
	 */
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
			throw new IllegalStateException(Msg.code(1772) + String.format("Unable to set property %s on %s", thePropertyName, myBase), e);
		}
	}

	/**
	 * Gets property values with the specified name from the provided base class.
	 *
	 * @param thePropertyName Name of the property to get
	 * @return Returns property values converted to string.
	 */
	public List<String> getMultiple(String thePropertyName) {
		Method getPropertyMethod = findGetPropertyMethod(myBase);
		Object[] values;
		try {
			values = (Object[]) getPropertyMethod.invoke(myBase, thePropertyName.hashCode(), thePropertyName, true);
		} catch (Exception e) {
			throw new IllegalStateException(Msg.code(1773) + String.format("Instance %s does not supply property %s", myBase, thePropertyName), e);
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

	/**
	 * Gets the delimiter used when concatenating multiple field values
	 *
	 * @return Returns the delimiter
	 */
	public String getDelimiter() {
		return myDelimiter;
	}

	/**
	 * Sets the delimiter used when concatenating multiple field values
	 *
	 * @param theDelimiter The delimiter to set
	 */
	public void setDelimiter(String theDelimiter) {
		this.myDelimiter = theDelimiter;
	}

	/**
	 * Gets the base instance that this helper operates on
	 *
	 * @return Returns the base instance
	 */
	public IBase getBase() {
		return myBase;
	}
}
