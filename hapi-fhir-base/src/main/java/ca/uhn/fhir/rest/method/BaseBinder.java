package ca.uhn.fhir.rest.method;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.param.CompositeParam;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;

class BaseBinder<T> {
	private Class<? extends IQueryParameterType>[] myCompositeTypes;
	private Constructor<? extends T> myConstructor;
	private final Class<? extends T> myType;

	public BaseBinder(Class<? extends T> theType, Class<? extends IQueryParameterType>[] theCompositeTypes) {
		myType = theType;
		myCompositeTypes = theCompositeTypes;
		
		if (myType.equals(CompositeParam.class)) {
			if (myCompositeTypes.length != 2) {
				throw new ConfigurationException("Search parameter of type " + myType.getName() + " must have 2 composite types declared in parameter annotation, found " + theCompositeTypes.length);
			}
		}
		
		try {
			Class<?>[] types = new Class<?>[myCompositeTypes.length];
			for (int i = 0; i < myCompositeTypes.length; i++) {
				types[i] = myCompositeTypes[i].getClass();
			}
			myConstructor = myType.getConstructor(types);
		} catch (NoSuchMethodException e) {
			throw new ConfigurationException("Query parameter type " + theType.getName() + " has no constructor with types " + Arrays.asList(theCompositeTypes));
		}
	}

	public T newInstance() {
		try {
			final Object[] args = new Object[myCompositeTypes.length];
			for (int i = 0; i < myCompositeTypes.length;i++) {
				args[i] = myCompositeTypes[i];//.newInstance();
			}
			
			T dt = myConstructor.newInstance(args);
			return dt;
		} catch (final InstantiationException e) {
			throw new InternalErrorException(e);
		} catch (final IllegalAccessException e) {
			throw new InternalErrorException(e);
		} catch (final SecurityException e) {
			throw new InternalErrorException(e);
		} catch (final IllegalArgumentException e) {
			throw new InternalErrorException(e);
		} catch (final InvocationTargetException e) {
			throw new InternalErrorException(e);
		}
	}

}
