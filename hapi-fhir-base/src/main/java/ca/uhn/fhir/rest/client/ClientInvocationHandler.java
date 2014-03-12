package ca.uhn.fhir.rest.client;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.client.api.IRestfulClient;
import ca.uhn.fhir.rest.client.exceptions.InvalidResponseException;
import ca.uhn.fhir.rest.client.exceptions.NonFhirResponseException;
import ca.uhn.fhir.rest.common.BaseMethodBinding;
import ca.uhn.fhir.rest.server.Constants;

public class ClientInvocationHandler implements InvocationHandler {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ClientInvocationHandler.class);
	private final Map<Method, BaseMethodBinding> myBindings = new HashMap<Method, BaseMethodBinding>();
	private final HttpClient myClient;
	private final FhirContext myContext;
	private final String myUrlBase;
	private final Map<Method, Object> myMethodToReturnValue=new HashMap<Method, Object>();

	public ClientInvocationHandler(HttpClient theClient, FhirContext theContext, String theUrlBase, Class<? extends IRestfulClient> theClientType) {
		myClient = theClient;
		myContext = theContext;
		myUrlBase = theUrlBase;
		
		try {
			myMethodToReturnValue.put(theClientType.getMethod("getFhirContext"), theContext);
			myMethodToReturnValue.put(theClientType.getMethod("getHttpClient"), theClient);
			myMethodToReturnValue.put(theClientType.getMethod("getServerBase"), theUrlBase);
		} catch (NoSuchMethodException e) {
			throw new ConfigurationException("Failed to find methods on client. This is a HAPI bug!",e);
		} catch (SecurityException e) {
			throw new ConfigurationException("Failed to find methods on client. This is a HAPI bug!",e);
		}
	}

	public void addBinding(Method theMethod, BaseMethodBinding theBinding) {
		myBindings.put(theMethod, theBinding);
	}

	@Override
	public Object invoke(Object theProxy, Method theMethod, Object[] theArgs) throws Throwable {
		Object directRetVal = myMethodToReturnValue.get(theMethod);
		if (directRetVal!=null) {
			return directRetVal;
		}
		
		BaseMethodBinding binding = myBindings.get(theMethod);
		GetClientInvocation clientInvocation = binding.invokeClient(theArgs);
		HttpRequestBase httpRequest = clientInvocation.asHttpRequest(myUrlBase);
		HttpResponse response = myClient.execute(httpRequest);

		Reader reader = createReaderFromResponse(response);
		
		if (ourLog.isTraceEnabled()) {
			String responseString = IOUtils.toString(reader);
			ourLog.trace("FHIR response:\n{}\n{}", response, responseString);
			reader = new StringReader(responseString);
		}
		
		ContentType ct = ContentType.get(response.getEntity());
		
		IParser parser;
		String mimeType = ct.getMimeType();
		if (Constants.CT_ATOM_XML.equals(mimeType)) {
			parser = myContext.newXmlParser();
		} else if (Constants.CT_FHIR_XML.equals(mimeType)) {
			parser = myContext.newXmlParser();
		} else {
			throw new NonFhirResponseException("Response contains non-FHIR content-type: " + mimeType, mimeType, response.getStatusLine().getStatusCode(), IOUtils.toString(reader));
		}

		switch (binding.getReturnType()) {
		case BUNDLE: {
			Bundle bundle = parser.parseBundle(reader);
			switch (binding.getMethodReturnType()) {
			case BUNDLE:
				return bundle;
			case LIST_OF_RESOURCES:
				return bundle.toListOfResources();
			case RESOURCE:
				List<IResource> list = bundle.toListOfResources();
				if (list.size() == 0) {
					return null;
				} else if (list.size() == 1) {
					return list.get(0);
				} else {
					throw new InvalidResponseException("FHIR server call returned a bundle with multiple resources, but this method is only able to returns one.");
				}
			}
			break;
		}
		case RESOURCE: {
			IResource resource = parser.parseResource(reader);
			switch (binding.getMethodReturnType()) {
			case BUNDLE:
				return Bundle.withSingleResource(resource);
			case LIST_OF_RESOURCES:
				return Collections.singletonList(resource);
			case RESOURCE:
				return resource;
			}
			break;
		}
		}

		throw new IllegalStateException("Should not get here!");
	}

	public static Reader createReaderFromResponse(HttpResponse theResponse) throws IllegalStateException, IOException {
		ContentType ct = ContentType.get(theResponse.getEntity());
		Charset charset = ct.getCharset();

		if (charset == null) {
			ourLog.warn("Response did not specify a charset.");
			charset = Charset.forName("UTF-8");
		}

		Reader reader = new InputStreamReader(theResponse.getEntity().getContent(), charset);
		return reader;
	}

}
