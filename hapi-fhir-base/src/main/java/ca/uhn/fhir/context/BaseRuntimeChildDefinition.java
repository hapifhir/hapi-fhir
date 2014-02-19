package ca.uhn.fhir.context;

import static org.apache.commons.lang3.StringUtils.*;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ca.uhn.fhir.model.api.IElement;
import ca.uhn.fhir.util.BeanUtils;

public abstract class BaseRuntimeChildDefinition {

	private final IAccessor myAccessor;
	private final String myElementName;
	private final Field myField;
	private final int myMax;
	private final int myMin;
	private final IMutator myMutator;

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

		myField = theField;
		myMin = theMin;
		myMax = theMax;
		myElementName = theElementName;

		// TODO: handle lists (max>0), and maybe max=0?

		if (myMax == 1) {
			Class<?> declaringClass = myField.getDeclaringClass();
			Class<?> targetReturnType = myField.getType();
			try {
				final Method accessor = BeanUtils.findAccessor(declaringClass, targetReturnType, myElementName);
				final Method mutator = BeanUtils.findMutator(declaringClass, targetReturnType, myElementName);
				myAccessor = new IAccessor() {
					@Override
					public List<Object> getValues(Object theTarget) {
						try {
							return Collections.singletonList(accessor.invoke(theTarget));
						} catch (IllegalAccessException e) {
							throw new ConfigurationException("Failed to get value", e);
						} catch (IllegalArgumentException e) {
							throw new ConfigurationException("Failed to get value", e);
						} catch (InvocationTargetException e) {
							throw new ConfigurationException("Failed to get value", e);
						}
					}
				};
				myMutator = new IMutator() {
					@Override
					public void addValue(Object theTarget, Object theValue) {
						try {
							mutator.invoke(theTarget, theValue);
						} catch (IllegalAccessException e) {
							throw new ConfigurationException("Failed to get value", e);
						} catch (IllegalArgumentException e) {
							throw new ConfigurationException("Failed to get value", e);
						} catch (InvocationTargetException e) {
							throw new ConfigurationException("Failed to get value", e);
						}
					}
				};
			} catch (NoSuchFieldException e) {
				throw new ConfigurationException(e);
			}
		} else {

			// replace this with an implementation
			myAccessor = null;
			myMutator = null;

		}

	}

	public IAccessor getAccessor() {
		return myAccessor;
	}

	public abstract BaseRuntimeElementDefinition<?> getChildByName(String theName);

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

	public abstract Set<String> getValidChildNames();

	abstract void sealAndInitialize(Map<Class<? extends IElement>, BaseRuntimeElementDefinition<?>> theClassToElementDefinitions);

	public interface IMutator {
		void addValue(Object theTarget, Object theValue);
	}

	public interface IAccessor {
		List<Object> getValues(Object theTarget);
	}
}
