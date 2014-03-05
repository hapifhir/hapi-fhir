package ca.uhn.fhir.rest.client;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import org.apache.http.client.HttpClient;

public class ClientInvocationHandler implements InvocationHandler {

	private HttpClient myClient;

	public ClientInvocationHandler(HttpClient theClient) {
		myClient = theClient;
	}

	@Override
	public Object invoke(Object theProxy, Method theMethod, Object[] theArgs) throws Throwable {
		// TODO Auto-generated method stub
		return null;
	}

}
