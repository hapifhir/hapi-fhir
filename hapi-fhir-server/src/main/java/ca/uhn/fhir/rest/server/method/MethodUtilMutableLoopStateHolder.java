package ca.uhn.fhir.rest.server.method;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.StringJoiner;

// LUKETODO:  javadoc
class MethodUtilMutableLoopStateHolder {
	private final Class<?> myParameterType;
	private final Method myMethodToUse;
	private final Class<? extends Collection<?>> myOuterCollectionType;
	private final Class<? extends Collection<?>> myInnerCollectionType;

	public MethodUtilMutableLoopStateHolder(Class<?> theParameterType, Method theMethodToUse, Class<? extends Collection<?>> theOuterCollectionType, Class<? extends Collection<?>> theInnerCollectionType) {
		myParameterType = theParameterType;
		myMethodToUse = theMethodToUse;
		myOuterCollectionType = theOuterCollectionType;
		myInnerCollectionType = theInnerCollectionType;
	}

	public Class<?> getParameterType() {
		return myParameterType;
	}

	public Method getMethodToUse() {
		return myMethodToUse;
	}

	public Class<? extends Collection<?>> getOuterCollectionType() {
		return myOuterCollectionType;
	}

	public Class<? extends Collection<?>> getInnerCollectionType() {
		return myInnerCollectionType;
	}

	@Override
	public String toString() {
		return new StringJoiner(", ", MethodUtilMutableLoopStateHolder.class.getSimpleName() + "[", "]")
			.add("myParameterType=" + myParameterType)
			.add("myMethodToUse=" + myMethodToUse)
			.add("myOuterCollectionType=" + myOuterCollectionType)
			.add("myInnerCollectionType=" + myInnerCollectionType)
			.toString();
	}
}
