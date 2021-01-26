package ca.uhn.fhir.interceptor.api;

import javax.annotation.Nonnull;
import java.util.List;

public interface IPointcut {
	@Nonnull
	Class<?> getReturnType();

	@Nonnull
	List<String> getParameterTypes();

	@Nonnull
	String name();

	boolean isShouldLogAndSwallowException(Throwable theException);
}
