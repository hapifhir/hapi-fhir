package ca.uhn.fhir.context;

import static org.apache.commons.lang3.StringUtils.*;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ca.uhn.fhir.model.api.IElement;
import ca.uhn.fhir.util.BeanUtils;

public abstract class BaseRuntimeUndeclaredChildDefinition extends BaseRuntimeChildDefinition {

	private final IAccessor myAccessor;
	private final String myElementName;
	private final Field myField;
	private final int myMax;
	private final int myMin;
	private final IMutator myMutator;

	BaseRuntimeUndeclaredChildDefinition(Field theField, int theMin, int theMax, String theElementName) throws ConfigurationException {
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

		myField = theField;
		myMin = theMin;
		myMax = theMax;
		myElementName = theElementName;

		// TODO: handle lists (max>0), and maybe max=0?

		Class<?> declaringClass = myField.getDeclaringClass();
		final Class<?> targetReturnType = myField.getType();
		try {
			final Method accessor = BeanUtils.findAccessor(declaringClass, targetReturnType, myElementName);
			final Method mutator = BeanUtils.findMutator(declaringClass, targetReturnType, myElementName);
			
			if (List.class.isAssignableFrom(targetReturnType)) {
				myAccessor = new ListAccessor(accessor);
				myMutator = new ListMutator(mutator);
			}else {
				myAccessor = new PlainAccessor(accessor);
				myMutator = new PlainMutator(targetReturnType, mutator);
			}
		} catch (NoSuchFieldException e) {
			throw new ConfigurationException(e);
		}

	}

	public IAccessor getAccessor() {
		return myAccessor;
	}

	public String getElementName() {
		return myElementName;
	}

	public Field getField() {
		return myField;
	}

	public int getMax() {
		return myMax;
	}

	public int getMin() {
		return myMin;
	}

	public IMutator getMutator() {
		return myMutator;
	}

	private final class ListMutator implements IMutator {
		private final Method myMutator;

		private ListMutator(Method theMutator) {
			myMutator = theMutator;
		}

		@Override
		public void  addValue(Object theTarget, IElement theValue) {
			@SuppressWarnings("unchecked")
			List<IElement> existingList = (List<IElement>) myAccessor.getValues(theTarget);
			if (existingList == null) {
				existingList = new ArrayList<IElement>();
				try {
					myMutator.invoke(theTarget, existingList);
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

	private final class ListAccessor implements IAccessor {
		private final Method myAccessor;

		private ListAccessor(Method theAccessor) {
			myAccessor = theAccessor;
		}

		@SuppressWarnings("unchecked")
		@Override
		public List<IElement> getValues(Object theTarget) {
			try {
				return (List<IElement>) myAccessor.invoke(theTarget);
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
		private final Class<?> myTargetReturnType;
		private final Method myMutator;

		private PlainMutator(Class<?> theTargetReturnType, Method theMutator) {
			myTargetReturnType = theTargetReturnType;
			myMutator = theMutator;
		}

		@Override
		public void addValue(Object theTarget, IElement theValue) {
			try {
				if (theValue != null && !myTargetReturnType.isAssignableFrom(theValue.getClass())) {
					throw new ConfigurationException("Value for field " + myElementName + " expects type " + myTargetReturnType + " but got " + theValue.getClass());
				}
				myMutator.invoke(theTarget, theValue);
			} catch (IllegalAccessException e) {
				throw new ConfigurationException("Failed to get value", e);
			} catch (IllegalArgumentException e) {
				throw new ConfigurationException("Failed to get value", e);
			} catch (InvocationTargetException e) {
				throw new ConfigurationException("Failed to get value", e);
			}
		}
	}

	private final class PlainAccessor implements IAccessor {
		private final Method myAccessor;

		private PlainAccessor(Method theAccessor) {
			myAccessor = theAccessor;
		}

		@Override
		public List<IElement> getValues(Object theTarget) {
			try {
				return Collections.singletonList((IElement)myAccessor.invoke(theTarget));
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
