package ca.uhn.fhir.rest.server.method;

import java.lang.reflect.Method;
import java.util.Collection;

/**
 * Capture inputs for initializing any kind of IParameter and to defect that initialization until called.
 */
class ParamInitializationContext {
	private final IParameter myParam;
	private final Class<?> myParameterType;
	private final Class<? extends java.util.Collection<?>> myOuterCollectionType;
	private final Class<? extends java.util.Collection<?>> myInnerCollectionType;

	ParamInitializationContext(
			IParameter theParam,
			Class<?> theParameterType,
			Class<? extends Collection<?>> theOuterCollectionType,
			Class<? extends Collection<?>> theInnerCollectionType) {
		myParam = theParam;
		myParameterType = theParameterType;
		myOuterCollectionType = theOuterCollectionType;
		myInnerCollectionType = theInnerCollectionType;
	}

	public IParameter getParam() {
		return myParam;
	}

	void initialize(Method theMethod) {
		myParam.initializeTypes(theMethod, myOuterCollectionType, myInnerCollectionType, myParameterType);
	}
}
