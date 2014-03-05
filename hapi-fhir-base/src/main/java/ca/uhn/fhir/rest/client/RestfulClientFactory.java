package ca.uhn.fhir.rest.client;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.concurrent.TimeUnit;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.impl.conn.SchemeRegistryFactory;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IRestfulClient;

public class RestfulClientFactory {

	private FhirContext myContext;

	/**
	 * Constructor
	 * 
	 * @param theContext The context
	 */
	public RestfulClientFactory(FhirContext theContext) {
		myContext = theContext;
	}
	
	
	/**
	 * Instantiates a new client instance
	 * 
	 * @param theClientType The client type, which is an interface type to be instantiated
	 * @param theServerBase The URL of the base for the restful FHIR server to connect to
	 * @return A newly created client
	 * @throws ConfigurationException If the interface type is not an interface
	 */
	public <T extends IRestfulClient> T newClient(Class<T> theClientType, String theServerBase) {
		if (!theClientType.isInterface()) {
			throw new ConfigurationException(theClientType.getCanonicalName() + " is not an interface");
		}
		
		PoolingClientConnectionManager connectionManager = new PoolingClientConnectionManager(SchemeRegistryFactory.createDefault(), 5000, TimeUnit.MILLISECONDS);
		HttpClient client = new DefaultHttpClient(connectionManager);

		ClientInvocationHandler theInvocationHandler = new ClientInvocationHandler(client);

		T proxy = instantiateProxy(theClientType, theInvocationHandler);
		
		return proxy;
	}


	@SuppressWarnings("unchecked")
	private <T extends IRestfulClient> T instantiateProxy(Class<T> theClientType, InvocationHandler theInvocationHandler) {
		T proxy = (T) Proxy.newProxyInstance(RestfulClientFactory.class.getClassLoader(), new Class[] {theClientType}, theInvocationHandler);
		return proxy;
	}
	
}
