package ca.uhn.fhir.rest.server.method;

import jakarta.annotation.Nullable;

import java.util.StringJoiner;

/**
 * Output of {@link EmbeddedParameterConverter} that captures either a NullParameter or a ParamInitializationContext.
 */
class EmbeddedParameterConverterContext {

	@Nullable
	private final NullParameter myNullParameter;

	@Nullable
	private final ParamInitializationContext myParamContext;

	public static EmbeddedParameterConverterContext forParameter(NullParameter theNullParameter) {
		return new EmbeddedParameterConverterContext(theNullParameter, null);
	}

	public static EmbeddedParameterConverterContext forEmbeddedContext(ParamInitializationContext theParamContext) {
		return new EmbeddedParameterConverterContext(null, theParamContext);
	}

	private EmbeddedParameterConverterContext(
			@Nullable NullParameter theNullParameter, @Nullable ParamInitializationContext theParamContext) {

		myNullParameter = theNullParameter;
		myParamContext = theParamContext;
	}

	@Nullable
	public NullParameter getParameter() {
		return myNullParameter;
	}

	@Nullable
	public ParamInitializationContext getParamContext() {
		return myParamContext;
	}

	@Override
	public String toString() {
		return new StringJoiner(", ", EmbeddedParameterConverterContext.class.getSimpleName() + "[", "]")
				.add("myNullParameter=" + myNullParameter)
				.add("myParamContext=" + myParamContext)
				.toString();
	}
}
