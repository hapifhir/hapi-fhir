package ca.uhn.fhir.context;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2016 University Health Network
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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBase;

import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;

public abstract class BaseRuntimeDeclaredChildDefinition extends BaseRuntimeChildDefinition {
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

	BaseRuntimeDeclaredChildDefinition(Field theField, Child theChildAnnotation, Description theDescriptionAnnotation, String theElementName) throws ConfigurationException {
		super();
		Validate.notNull(theField, "No field speficied");
		Validate.inclusiveBetween(0, Integer.MAX_VALUE, theChildAnnotation.min(), "Min must be >= 0");
		Validate.isTrue(theChildAnnotation.max() == -1 || theChildAnnotation.max() >= theChildAnnotation.min(), "Max must be >= Min (unless it is -1 / unlimited)");
		Validate.notBlank(theElementName, "Element name must not be blank");

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

		myField.setAccessible(true);
		if (List.class.equals(myField.getType())) {
			// TODO: verify that generic type is IElement
			myAccessor = new FieldListAccessor();
			myMutator = new FieldListMutator();
		} else {
			myAccessor = new FieldPlainAccessor();
			myMutator = new FieldPlainMutator();
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

	private final class FieldListAccessor implements IAccessor {
		@SuppressWarnings("unchecked")
		@Override
		public List<IBase> getValues(Object theTarget) {
			List<IBase> retVal;
			try {
				retVal = (List<IBase>) myField.get(theTarget);
			} catch (Exception e) {
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
			} catch (Exception e) {
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
				List<IBase> retVal = Collections.singletonList((IBase) values);
				return retVal;
			} catch (Exception e) {
				throw new ConfigurationException("Failed to get value", e);
			}
		}
	}

	protected final class FieldPlainMutator implements IMutator {
		@Override
		public void addValue(Object theTarget, IBase theValue) {
			try {
				myField.set(theTarget, theValue);
			} catch (Exception e) {
				throw new ConfigurationException("Failed to set value", e);
			}
		}

		@Override
		public void setValue(Object theTarget, IBase theValue) {
			addValue(theTarget, theValue);
		}
	}

}
