package ca.uhn.fhir.util;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

public class BeanUtils {

	public static Method findAccessor(Class<?> theClassToIntrospect, Class<?> theTargetReturnType, String thePropertyName) throws NoSuchFieldException {
		BeanInfo info;
		try {
			info = Introspector.getBeanInfo(theClassToIntrospect);
		} catch (IntrospectionException e) {
			throw new NoSuchFieldException(e.getMessage());
		}
		for (PropertyDescriptor pd : info.getPropertyDescriptors()) {
			if (thePropertyName.equals(pd.getName())) {
				if (theTargetReturnType.isAssignableFrom(pd.getPropertyType())) {
				return pd.getReadMethod();
				}else {
					throw new NoSuchFieldException(theClassToIntrospect + " has an accessor for field " + thePropertyName + " but it does not return type " + theTargetReturnType);
				}
			}
		}
		throw new NoSuchFieldException(theClassToIntrospect + " has no accessor for field " + thePropertyName);
	}

	public static Method findMutator(Class<?> theClassToIntrospect, Class<?> theTargetReturnType, String thePropertyName) throws NoSuchFieldException {
		BeanInfo info;
		try {
			info = Introspector.getBeanInfo(theClassToIntrospect);
		} catch (IntrospectionException e) {
			throw new NoSuchFieldException(e.getMessage());
		}
		for (PropertyDescriptor pd : info.getPropertyDescriptors()) {
			if (thePropertyName.equals(pd.getName())) {
				if (theTargetReturnType.isAssignableFrom(pd.getPropertyType())) {
					return pd.getWriteMethod();
				}else {
					throw new NoSuchFieldException(theClassToIntrospect + " has an mutator for field " + thePropertyName + " but it does not return type " + theTargetReturnType);
				}
			}
		}
		throw new NoSuchFieldException(theClassToIntrospect + " has no mutator for field " + thePropertyName);
	}
}
