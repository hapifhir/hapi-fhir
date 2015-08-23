package ca.uhn.fhir.context;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2015 University Health Network
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

import static org.apache.commons.lang3.StringUtils.*;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.text.WordUtils;
import org.hl7.fhir.instance.model.api.IBase;

import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.util.BeanUtils;

public abstract class BaseRuntimeDeclaredChildDefinition extends BaseRuntimeChildDefinition {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(BaseRuntimeDeclaredChildDefinition.class);
	private final IAccessor myAccessor;
	private final String myElementName;
	private final Field myField;
	private final String myFormalDefinition;
	private final int myMax;
	private final int myMin;
	private boolean myModifier;
	private final IMutator myMutator;
	private final String myShortDefinition;
	private boolean mySummary;
	private Boolean ourUseMethodAccessors;

	BaseRuntimeDeclaredChildDefinition(Field theField, Child theChildAnnotation, Description theDescriptionAnnotation, String theElementName) throws ConfigurationException {
		super();
		if (theField == null) {
			throw new IllegalArgumentException("No field speficied");
		}
		if (theChildAnnotation.min() < 0) {
			throw new ConfigurationException("Min must be >= 0");
		}
		if (theChildAnnotation.max() != -1 && theChildAnnotation.max() < theChildAnnotation.min()) {
			throw new ConfigurationException("Max must be >= Min (unless it is -1 / unlimited)");
		}
		if (isBlank(theElementName)) {
			throw new ConfigurationException("Element name must not be blank");
		}

		myField = theField;
		myMin = theChildAnnotation.min();
		myMax = theChildAnnotation.max();
		mySummary = theChildAnnotation.summary();
		myModifier = theChildAnnotation.modifier();
		myElementName = theElementName;
		if (theDescriptionAnnotation != null) {
			myShortDefinition = theDescriptionAnnotation.shortDefinition();
			myFormalDefinition = theDescriptionAnnotation.formalDefinition();
		} else {
			myShortDefinition = null;
			myFormalDefinition = null;
		}

		// TODO: handle lists (max>0), and maybe max=0?

		// TODO: finish implementing field level accessors/mutators
		if (ourUseMethodAccessors == null) {
			try {
				myField.setAccessible(true);
				ourUseMethodAccessors = false;
			} catch (SecurityException e) {
				ourLog.info("Can not use field accessors/mutators, going to use methods instead");
				ourUseMethodAccessors = true;
			}
		}

		if (ourUseMethodAccessors == false) {
			if (List.class.equals(myField.getType())) {
				// TODO: verify that generic type is IElement
				myAccessor = new FieldListAccessor();
				myMutator = new FieldListMutator();
			} else {
				myAccessor = new FieldPlainAccessor();
				myMutator = new FieldPlainMutator();
			}
		} else {
			Class<?> declaringClass = myField.getDeclaringClass();
			final Class<?> targetReturnType = myField.getType();
			try {
				String elementName = myElementName;
				if ("class".equals(elementName.toLowerCase())) {
					elementName = "classElement"; // because getClass() is reserved
				}
				final Method accessor = BeanUtils.findAccessor(declaringClass, targetReturnType, elementName);
				if (accessor == null) {
					StringBuilder b = new StringBuilder();
					b.append("Could not find bean accessor/getter for property ");
					b.append(elementName);
					b.append(" on class ");
					b.append(declaringClass.getCanonicalName());
					throw new ConfigurationException(b.toString());
				}

				final Method mutator = findMutator(declaringClass, targetReturnType, elementName);
				if (mutator == null) {
					StringBuilder b = new StringBuilder();
					b.append("Could not find bean mutator/setter for property ");
					b.append(elementName);
					b.append(" on class ");
					b.append(declaringClass.getCanonicalName());
					b.append(" (expected return type ");
					b.append(targetReturnType.getCanonicalName());
					b.append(")");
					throw new ConfigurationException(b.toString());
				}

				if (List.class.isAssignableFrom(targetReturnType)) {
					myAccessor = new ListAccessor(accessor);
					myMutator = new ListMutator(mutator);
				} else {
					myAccessor = new PlainAccessor(accessor);
					myMutator = new PlainMutator(targetReturnType, mutator);
				}
			} catch (NoSuchFieldException e) {
				throw new ConfigurationException(e);
			}
		}

	}

	@Override
	public IAccessor getAccessor() {
		return myAccessor;
	}

	@Override
	public String getElementName() {
		return myElementName;
	}

	public Field getField() {
		return myField;
	}

	public String getFormalDefinition() {
		return myFormalDefinition;
	}

	@Override
	public int getMax() {
		return myMax;
	}

	@Override
	public int getMin() {
		return myMin;
	}

	@Override
	public IMutator getMutator() {
		return myMutator;
	}

	public String getShortDefinition() {
		return myShortDefinition;
	}

	public BaseRuntimeElementDefinition<?> getSingleChildOrThrow() {
		if (getValidChildNames().size() != 1) {
			throw new IllegalStateException("This child has " + getValidChildNames().size() + " children, expected 1. This is a HAPI bug. Found: " + getValidChildNames());
		}
		return getChildByName(getValidChildNames().iterator().next());
	}

	public boolean isModifier() {
		return myModifier;
	}

	public boolean isSummary() {
		return mySummary;
	}

	private static Method findMutator(Class<?> theDeclaringClass, Class<?> theTargetReturnType, String theElementName) {
		String methodName = "set" + WordUtils.capitalize(theElementName);
		try {
			return theDeclaringClass.getMethod(methodName, theTargetReturnType);
		} catch (NoSuchMethodException e) {
			return null;
		} catch (SecurityException e) {
			throw new ConfigurationException("Failed to scan class '" + theDeclaringClass + "' because of a security exception", e);
		}
	}

	private final class FieldListAccessor implements IAccessor {
		@SuppressWarnings("unchecked")
		@Override
		public List<IBase> getValues(Object theTarget) {
			List<IBase> retVal;
			try {
				retVal = (List<IBase>) myField.get(theTarget);
			} catch (IllegalArgumentException e) {
				throw new ConfigurationException("Failed to get value", e);
			} catch (IllegalAccessException e) {
				throw new ConfigurationException("Failed to get value", e);
			}
			if (retVal == null) {
				retVal = Collections.emptyList();
			}
			return retVal;
		}
	}

	protected final class FieldListMutator implements IMutator {
		@Override
		public void addValue(Object theTarget, IBase theValue) {
			addValue(theTarget, theValue, false);
		}

		private void addValue(Object theTarget, IBase theValue, boolean theClear) {
			try {
				@SuppressWarnings("unchecked")
				List<IBase> existingList = (List<IBase>) myField.get(theTarget);
				if (existingList == null) {
					existingList = new ArrayList<IBase>(2);
					myField.set(theTarget, existingList);
				}
				if (theClear) {
					existingList.clear();
				}
				existingList.add(theValue);
			} catch (IllegalArgumentException e) {
				throw new ConfigurationException("Failed to set value", e);
			} catch (IllegalAccessException e) {
				throw new ConfigurationException("Failed to set value", e);
			}
		}

		@Override
		public void setValue(Object theTarget, IBase theValue) {
			addValue(theTarget, theValue, true);
		}
	}

	private final class FieldPlainAccessor implements IAccessor {
		@Override
		public List<IBase> getValues(Object theTarget) {
			try {
				Object values = myField.get(theTarget);
				if (values == null) {
					return Collections.emptyList();
				}
				List<IBase> retVal = Collections.singletonList((IBase)values);
				return retVal;
			} catch (IllegalArgumentException e) {
				throw new ConfigurationException("Failed to get value", e);
			} catch (IllegalAccessException e) {
				throw new ConfigurationException("Failed to get value", e);
			}
		}
	}

	protected final class FieldPlainMutator implements IMutator {
		@Override
		public void addValue(Object theTarget, IBase theValue) {
			try {
				myField.set(theTarget, theValue);
			} catch (IllegalArgumentException e) {
				throw new ConfigurationException("Failed to set value", e);
			} catch (IllegalAccessException e) {
				throw new ConfigurationException("Failed to set value", e);
			}
		}

		@Override
		public void setValue(Object theTarget, IBase theValue) {
			addValue(theTarget, theValue);
		}
	}

	private static final class ListAccessor implements IAccessor {
		private final Method myAccessorMethod;

		private ListAccessor(Method theAccessor) {
			myAccessorMethod = theAccessor;
		}

		@SuppressWarnings("unchecked")
		@Override
		public List<IBase> getValues(Object theTarget) {
			try {
				return (List<IBase>) myAccessorMethod.invoke(theTarget);
			} catch (IllegalAccessException e) {
				throw new ConfigurationException("Failed to get value", e);
			} catch (IllegalArgumentException e) {
				throw new ConfigurationException("Failed to get value", e);
			} catch (InvocationTargetException e) {
				throw new ConfigurationException("Failed to get value", e);
			}
		}
	}

	private final class ListMutator implements IMutator {
		private final Method myMutatorMethod;

		private ListMutator(Method theMutator) {
			myMutatorMethod = theMutator;
		}

		private void addValue(Object theTarget, boolean theClear, IBase theValue) {
			List<IBase> existingList = myAccessor.getValues(theTarget);
			if (existingList == null) {
				existingList = new ArrayList<IBase>();
				try {
					myMutatorMethod.invoke(theTarget, existingList);
				} catch (IllegalAccessException e) {
					throw new ConfigurationException("Failed to get value", e);
				} catch (IllegalArgumentException e) {
					throw new ConfigurationException("Failed to get value", e);
				} catch (InvocationTargetException e) {
					throw new ConfigurationException("Failed to get value", e);
				}
			}
			if (theClear) {
				existingList.clear();
			}
			existingList.add(theValue);
		}

		@Override
		public void addValue(Object theTarget, IBase theValue) {
			addValue(theTarget, false, theValue);
		}

		@Override
		public void setValue(Object theTarget, IBase theValue) {
			addValue(theTarget, true, theValue);
		}
	}

	private final class PlainAccessor implements IAccessor {
		private final Method myAccessorMethod;

		private PlainAccessor(Method theAccessor) {
			myAccessorMethod = theAccessor;
		}

		@Override
		public List<IBase> getValues(Object theTarget) {
			try {
				return Collections.singletonList((IBase)myAccessorMethod.invoke(theTarget));
			} catch (IllegalAccessException e) {
				throw new ConfigurationException("Failed to get value", e);
			} catch (IllegalArgumentException e) {
				throw new ConfigurationException("Failed to get value", e);
			} catch (InvocationTargetException e) {
				throw new ConfigurationException("Failed to get value", e);
			}
		}
	}

	private final class PlainMutator implements IMutator {
		private final Method myMutatorMethod;
		private final Class<?> myTargetReturnType;

		private PlainMutator(Class<?> theTargetReturnType, Method theMutator) {
			assert theTargetReturnType != null;
			assert theMutator != null;

			myTargetReturnType = theTargetReturnType;
			myMutatorMethod = theMutator;
		}

		@Override
		public void addValue(Object theTarget, IBase theValue) {
			try {
				if (theValue != null && !myTargetReturnType.isAssignableFrom(theValue.getClass())) {
					throw new ConfigurationException("Value for field " + myElementName + " expects type " + myTargetReturnType + " but got " + theValue.getClass());
				}
				myMutatorMethod.invoke(theTarget, theValue);
			} catch (IllegalAccessException e) {
				throw new ConfigurationException("Failed to get value", e);
			} catch (IllegalArgumentException e) {
				throw new ConfigurationException("Failed to get value", e);
			} catch (InvocationTargetException e) {
				throw new ConfigurationException("Failed to get value", e);
			}
		}

		@Override
		public void setValue(Object theTarget, IBase theValue) {
			addValue(theTarget, theValue);
		}
	}

}
