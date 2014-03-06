package ca.uhn.fhir.rest.client;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.HttpClient;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.common.BaseMethodBinding;

public class ClientInvocationHandler implements InvocationHandler {

	private HttpClient myClient;
	private Map<Method, BaseMethodBinding> myBindings = new HashMap<Method, BaseMethodBinding>();
	private FhirContext myContext;

	public ClientInvocationHandler(HttpClient theClient, FhirContext theContext) {
		myClient = theClient;
		myContext = theContext;
	}

	@Override
	public Object invoke(Object theProxy, Method theMethod, Object[] theArgs) throws Throwable {
		BaseMethodBinding binding = myBindings.get(theMethod);
		ClientInvocation clientInvocation = binding.invokeClient(theArgs);
		return null;
	}

	public void addBinding(Method theMethod, BaseMethodBinding theBinding) {
		myBindings.put(theMethod, theBinding);
	}

}
