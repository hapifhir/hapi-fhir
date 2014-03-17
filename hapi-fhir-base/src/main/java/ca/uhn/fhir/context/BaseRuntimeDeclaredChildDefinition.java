package ca.uhn.fhir.context;

import static org.apache.commons.lang3.StringUtils.*;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ca.uhn.fhir.model.api.IElement;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.util.BeanUtils;

public abstract class BaseRuntimeDeclaredChildDefinition extends BaseRuntimeChildDefinition {

	private final IAccessor myAccessor;
	private final String myElementName;
	private final Field myField;
	private final int myMax;
	private final int myMin;
	private final IMutator myMutator;
	private final String myShortDefinition;
	private final String myFormalDefinition;

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
		myElementName = theElementName;
		if (theDescriptionAnnotation != null) {
			myShortDefinition = theDescriptionAnnotation.shortDefinition();
			myFormalDefinition = theDescriptionAnnotation.formalDefinition();
		}else {
			myShortDefinition=null;
			myFormalDefinition=null;
		}
		
		// TODO: handle lists (max>0), and maybe max=0?

		Class<?> declaringClass = myField.getDeclaringClass();
		final Class<?> targetReturnType = myField.getType();
		try {
			String elementName = myElementName;
			if ("class".equals(elementName.toLowerCase())) {
				elementName = "classElement"; // because getClass() is reserved
			}
			final Method accessor = BeanUtils.findAccessor(declaringClass, targetReturnType, elementName);
			final Method mutator = BeanUtils.findMutator(declaringClass, targetReturnType, elementName);

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

	public String getShortDefinition() {
		return myShortDefinition;
	}

	public String getFormalDefinition() {
		return myFormalDefinition;
	}

	@Override
	public IAccessor getAccessor() {
		return myAccessor;
	}

	public String getElementName() {
		return myElementName;
	}

	public Field getField() {
		return myField;
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

	public BaseRuntimeElementDefinition<?> getSingleChildOrThrow() {
		if (getValidChildNames().size() != 1) {
			throw new IllegalStateException("This child has " + getValidChildNames().size() + " children, expected 1. This is a HAPI bug. Found: " + getValidChildNames());
		}
		return getChildByName(getValidChildNames().iterator().next());
	}

	private final class ListAccessor implements IAccessor {
		private final Method myAccessorMethod;

		private ListAccessor(Method theAccessor) {
			myAccessorMethod = theAccessor;
		}

		@SuppressWarnings("unchecked")
		@Override
		public List<IElement> getValues(Object theTarget) {
			try {
				return (List<IElement>) myAccessorMethod.invoke(theTarget);
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

		@Override
		public void addValue(Object theTarget, IElement theValue) {
			@SuppressWarnings("unchecked")
			List<IElement> existingList = (List<IElement>) myAccessor.getValues(theTarget);
			if (existingList == null) {
				existingList = new ArrayList<IElement>();
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
			existingList.add(theValue);
		}
	}

	private final class PlainAccessor implements IAccessor {
		private final Method myAccessorMethod;

		private PlainAccessor(Method theAccessor) {
			myAccessorMethod = theAccessor;
		}

		@Override
		public List<IElement> getValues(Object theTarget) {
			try {
				return Collections.singletonList((IElement) myAccessorMethod.invoke(theTarget));
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
		public void addValue(Object theTarget, IElement theValue) {
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
	}

}
