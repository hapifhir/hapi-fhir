package ca.uhn.fhir.rest.server.method;

import java.util.StringJoiner;

// LUKETODO:  javadoc
// LUKETODO:  do we need this at all anymore?
class MethodUtilMutableLoopStateHolder {
	private final MethodUtilParamInitializationContext myParamContext;

	public MethodUtilMutableLoopStateHolder(MethodUtilParamInitializationContext theParamContext) {
		myParamContext = theParamContext;
	}

	public MethodUtilParamInitializationContext getParamContext() {
		return myParamContext;
	}

	@Override
	public String toString() {
		return new StringJoiner(", ", MethodUtilMutableLoopStateHolder.class.getSimpleName() + "[", "]")
				.add("myParamContext=" + myParamContext)
				.toString();
	}
}
