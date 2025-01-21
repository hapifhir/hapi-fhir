package ca.uhn.fhir.rest.server.method;

import java.util.Collection;
import java.util.StringJoiner;

// LUKETODO:  javadoc
class MethodUtilMutableLoopStateHolder {
	private final Class<?> myParameterType;
	private final Class<? extends Collection<?>> myOuterCollectionType;
	private final Class<? extends Collection<?>> myInnerCollectionType;
	private final OperationEmbeddedParameter myOperationEmbeddedParameter;

	public MethodUtilMutableLoopStateHolder(
			Class<?> theParameterType,
			Class<? extends Collection<?>> theOuterCollectionType,
			Class<? extends Collection<?>> theInnerCollectionType,
			OperationEmbeddedParameter theOperationEmbeddedParameter) {
		myParameterType = theParameterType;
		myOuterCollectionType = theOuterCollectionType;
		myInnerCollectionType = theInnerCollectionType;
		myOperationEmbeddedParameter = theOperationEmbeddedParameter;
	}

	public Class<?> getParameterType() {
		return myParameterType;
	}

	public OperationEmbeddedParameter getOperationEmbeddedParameter() {
		return myOperationEmbeddedParameter;
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
				.add("myOuterCollectionType=" + myOuterCollectionType)
				.add("myInnerCollectionType=" + myInnerCollectionType)
				.toString();
	}
}
