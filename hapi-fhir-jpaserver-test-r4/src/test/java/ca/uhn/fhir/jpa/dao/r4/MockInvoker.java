package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IBaseInterceptorBroadcaster;
import jakarta.annotation.Nonnull;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

class MockInvoker implements IBaseInterceptorBroadcaster.IInvoker {

	private final Function<HookParams, Object> myFunction;

	private MockInvoker(Consumer<HookParams> theRunnable) {
		this(param -> { theRunnable.accept(param); return null; });
	}

	private MockInvoker(Function<HookParams, Object> theFunction) {
		myFunction = theFunction;
	}

	@Override
	public Object invoke(HookParams theParams) {
		return myFunction.apply(theParams);
	}

	@Override
	public int getOrder() {
		return 0;
	}

	@Override
	public Object getInterceptor() {
		return new Object();
	}

	@Override
	public int compareTo(@Nonnull IBaseInterceptorBroadcaster.IInvoker o) {
		return 0;
	}

	public static List<IBaseInterceptorBroadcaster.IInvoker> list(Consumer<HookParams> theRunnable) {
		return List.of(new MockInvoker(theRunnable));
	}

	public static List<IBaseInterceptorBroadcaster.IInvoker> list(Function<HookParams, Object> theRunnable) {
		return List.of(new MockInvoker(theRunnable));
	}

}
