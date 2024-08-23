package ca.uhn.fhir.jpa.model.pkspike;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

public class ValueTypeBasedParameterResolver<T> implements ParameterResolver {

	private final T myValue;

	public ValueTypeBasedParameterResolver(T theValue) {
		myValue = theValue;
	}

	@Override
	public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
		return parameterContext.getParameter().getType().isAssignableFrom(myValue.getClass());
	}

	@Override
	public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
		return myValue;
	}
}
