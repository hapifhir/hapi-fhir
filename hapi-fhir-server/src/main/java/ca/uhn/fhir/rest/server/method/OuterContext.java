package ca.uhn.fhir.rest.server.method;

import jakarta.annotation.Nullable;

// LUKETODO:  replace with an Either?
// LUKETODO:  rename
// LUKETODO:  javadoc
class OuterContext {
	@Nullable
	private final IParameter myParamter;

	@Nullable
	private final MethodUtilMutableLoopStateHolder myStateHolder;

	public OuterContext(IParameter myParamter, @Nullable MethodUtilMutableLoopStateHolder myStateHolder) {
		this.myParamter = myParamter;
		this.myStateHolder = myStateHolder;
	}

	@Nullable
	public IParameter getParamter() {
		return myParamter;
	}

	@Nullable
	public MethodUtilMutableLoopStateHolder getStateHolder() {
		return myStateHolder;
	}
}
