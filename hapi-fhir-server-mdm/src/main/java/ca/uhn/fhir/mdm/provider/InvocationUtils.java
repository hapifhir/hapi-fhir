package ca.uhn.fhir.mdm.provider;

import javax.annotation.Nonnull;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class InvocationUtils {

	private InvocationUtils() {
	}

	public static Object newInnerClassInstance(@Nonnull Object theEnclosingClassInstance, @Nonnull String theInnerClassName) {
		Class c = null;
		try {
			c = Class.forName(theEnclosingClassInstance.getClass().getCanonicalName() + "$" + theInnerClassName);
		} catch (ClassNotFoundException e) {
			throw new IllegalArgumentException("Unable to find inner class", e);
		}

		try {
			return c.getConstructor().newInstance();
		} catch (Exception e) {
			throw new IllegalStateException("Unable to instantiate", e);
		}
	}

	public static Enum getInnerEnumLiteral(@Nonnull Object theEnclosingClass, String theEnumName, String theLiteralName) {
		return getEnumValue(theEnclosingClass.getClass().getCanonicalName() + "$" + theEnumName, theLiteralName);
	}

	public static Enum getEnumValue(@Nonnull String theClassName, String theLiteralName) {
		Class c = null;
		try {
			c = Class.forName(theClassName);
		} catch (ClassNotFoundException e) {
			throw new IllegalArgumentException("Unable to find inner enum", e);
		}

		for (Object aConst : c.getEnumConstants()) {
			if (String.valueOf(aConst).equals(theLiteralName)) {
				return (Enum) aConst;
			}
		}
		throw new IllegalArgumentException(String.format("Unable to find enum constant %s in %s", theLiteralName, theLiteralName));
	}

	public static <T> T invoke(@Nonnull Object theInstance, @Nonnull String theMethodName, Object... theArgs) {
		Class[] argTypes = toParamTypes(theArgs);
		Class instanceClass = theInstance.getClass();
		Object retVal = null;
		Method instanceMethod = null;
		for (Method m : instanceClass.getMethods()) {
			if (m.getName().equals(theMethodName) && allAssignableFrom(m.getParameterTypes(), argTypes)) {
				instanceMethod = m;
				break;
			}
		}

		if (instanceMethod == null) {
			try {
				instanceMethod = instanceClass.getMethod(theMethodName, argTypes);
			} catch (NoSuchMethodException e) {
				throw new IllegalArgumentException("Unable to get method " + theMethodName + " on " + theInstance, e);
			}
		}
		try {
			retVal = instanceMethod.invoke(theInstance, theArgs);
		} catch (IllegalAccessException | InvocationTargetException e) {
			throw new IllegalStateException("Unable to invoke method " + theMethodName + " on " + theInstance, e);
		}
		return (T) retVal;
	}

	private static boolean allAssignableFrom(Class[] theTargetTypes, Class[] theTypesToCheck) {
		if (theTargetTypes.length != theTypesToCheck.length) {
			return false;
		}

		for (int i = 0; i < theTargetTypes.length; i++) {
			if (!theTargetTypes[i].isAssignableFrom(theTypesToCheck[i])) {
				return false;
			}
		}
		return true;
	}

	private static Class[] toParamTypes(Object[] theArgs) {
		if (theArgs == null) {
			return new Class[] {};
		}

		Class[] retVal = new Class[theArgs.length];
		for (int i = 0; i < theArgs.length; i++) {
			retVal[i] = theArgs[i].getClass();
		}
		return retVal;
	}

}
