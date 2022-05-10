package ca.uhn.fhir.context;

/*
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

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.util.ParametersUtil;
import ca.uhn.fhir.util.ValidateUtil;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBase;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public abstract class BaseRuntimeDeclaredChildDefinition extends BaseRuntimeChildDefinition {
	private final IAccessor myAccessor;
	private final String myElementName;
	private final Field myField;
	private final String myFormalDefinition;
	private final int myMax;
	private final int myMin;
	private final IMutator myMutator;
	private final String myShortDefinition;
	private String myBindingValueSet;
	private boolean myModifier;
	private boolean mySummary;

	BaseRuntimeDeclaredChildDefinition(Field theField, Child theChildAnnotation, Description theDescriptionAnnotation, String theElementName) throws ConfigurationException {
		super();
		Validate.notNull(theField, "No field specified");
		ValidateUtil.isGreaterThanOrEqualTo(theChildAnnotation.min(), 0, "Min must be >= 0");
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
			myFormalDefinition = ParametersUtil.extractDescription(theDescriptionAnnotation);
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

	public String getBindingValueSet() {
		return myBindingValueSet;
	}

	void setBindingValueSet(String theBindingValueSet) {
		myBindingValueSet = theBindingValueSet;
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

	public boolean isModifier() {
		return myModifier;
	}

	protected void setModifier(boolean theModifier) {
		myModifier = theModifier;
	}

	@Override
	public boolean isSummary() {
		return mySummary;
	}

	private final class FieldListAccessor implements IAccessor {
		@SuppressWarnings("unchecked")
		@Override
		public List<IBase> getValues(IBase theTarget) {
			List<IBase> retVal = (List<IBase>) getFieldValue(theTarget, myField);
			if (retVal == null) {
				retVal = Collections.emptyList();
			}
			return retVal;
		}

	}

	protected final class FieldListMutator implements IMutator {
		@Override
		public void addValue(IBase theTarget, IBase theValue) {
			addValue(theTarget, theValue, false);
		}

		private void addValue(IBase theTarget, IBase theValue, boolean theClear) {
			@SuppressWarnings("unchecked")
			List<IBase> existingList = (List<IBase>) getFieldValue(theTarget, myField);
			if (existingList == null) {
				existingList = new ArrayList<>(2);
				setFieldValue(theTarget, existingList, myField);
			}
			if (theClear) {
				existingList.clear();
				if (theValue == null) {
					return;
				}
			}
			existingList.add(theValue);
		}

		@Override
		public void setValue(IBase theTarget, IBase theValue) {
			addValue(theTarget, theValue, true);
		}
	}

	private final class FieldPlainAccessor implements IAccessor {
		@Override
		public List<IBase> getValues(IBase theTarget) {
			Object values = getFieldValue(theTarget, myField);
			if (values == null) {
				return Collections.emptyList();
			}
			return Collections.singletonList((IBase) values);
		}

		@Override
		public <T extends IBase> Optional<T> getFirstValueOrNull(IBase theTarget) {
			return Optional.ofNullable(((T)getFieldValue(theTarget, myField)));
		}
	}

	protected final class FieldPlainMutator implements IMutator {
		@Override
		public void addValue(IBase theTarget, IBase theValue) {
			setFieldValue(theTarget, theValue, myField);
		}

		@Override
		public void setValue(IBase theTarget, IBase theValue) {
			addValue(theTarget, theValue);
		}
	}

	private static void setFieldValue(IBase theTarget, Object theValue, Field theField) {
		try {
			theField.set(theTarget, theValue);
		} catch (IllegalAccessException e) {
			throw new ConfigurationException(Msg.code(1736) + "Failed to set value", e);
		}
	}

	private static Object getFieldValue(IBase theTarget, Field theField) {
		try {
			return theField.get(theTarget);
		} catch (IllegalAccessException e) {
			throw new ConfigurationException(Msg.code(1737) + "Failed to get value", e);
		}
	}

}
