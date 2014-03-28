package ca.uhn.fhir.rest.client;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IRestfulClient;
import ca.uhn.fhir.rest.method.BaseMethodBinding;

public class ClientInvocationHandler implements InvocationHandler {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ClientInvocationHandler.class);
	private final Map<Method, BaseMethodBinding> myBindings = new HashMap<Method, BaseMethodBinding>();
	private final HttpClient myClient;
	private final String myUrlBase;
	private final Map<Method, Object> myMethodToReturnValue = new HashMap<Method, Object>();

	public ClientInvocationHandler(HttpClient theClient, FhirContext theContext, String theUrlBase, Class<? extends IRestfulClient> theClientType) {
		myClient = theClient;
		myUrlBase = theUrlBase;

		try {
			myMethodToReturnValue.put(theClientType.getMethod("getFhirContext"), theContext);
			myMethodToReturnValue.put(theClientType.getMethod("getHttpClient"), theClient);
			myMethodToReturnValue.put(theClientType.getMethod("getServerBase"), theUrlBase);
		} catch (NoSuchMethodException e) {
			throw new ConfigurationException("Failed to find methods on client. This is a HAPI bug!", e);
		} catch (SecurityException e) {
			throw new ConfigurationException("Failed to find methods on client. This is a HAPI bug!", e);
		}
	}

	public void addBinding(Method theMethod, BaseMethodBinding theBinding) {
		myBindings.put(theMethod, theBinding);
	}

	@Override
	public Object invoke(Object theProxy, Method theMethod, Object[] theArgs) throws Throwable {
		Object directRetVal = myMethodToReturnValue.get(theMethod);
		if (directRetVal != null) {
			return directRetVal;
		}

		BaseMethodBinding binding = myBindings.get(theMethod);
		BaseClientInvocation clientInvocation = binding.invokeClient(theArgs);

		HttpRequestBase httpRequest = clientInvocation.asHttpRequest(myUrlBase);
		HttpResponse response = myClient.execute(httpRequest);
		try {

			Reader reader = createReaderFromResponse(response);

			if (ourLog.isTraceEnabled()) {
				String responseString = IOUtils.toString(reader);
				ourLog.trace("FHIR response:\n{}\n{}", response, responseString);
				reader = new StringReader(responseString);
			}

			ContentType ct = ContentType.get(response.getEntity());

			String mimeType = ct.getMimeType();

			Map<String, List<String>> headers = new HashMap<String, List<String>>();
			if (response.getAllHeaders() != null) {
				for (Header next : response.getAllHeaders()) {
					String name = next.getName().toLowerCase();
					List<String> list = headers.get(name);
					if (list == null) {
						list = new ArrayList<String>();
						headers.put(name, list);
					}
					list.add(next.getValue());
				}
			}

			return binding.invokeClient(mimeType, reader, response.getStatusLine().getStatusCode(), headers);

		} finally {
			if (response instanceof CloseableHttpResponse) {
				((CloseableHttpResponse) response).close();
			}
		}
	}

	public static Reader createReaderFromResponse(HttpResponse theResponse) throws IllegalStateException, IOException {
		HttpEntity entity = theResponse.getEntity();
		if (entity == null) {
			return new StringReader("");
		}
		Charset charset = null;
		if (entity.getContentType().getElements() != null) {
			ContentType ct = ContentType.get(entity);
			charset = ct.getCharset();
		}
		if (charset == null) {
			ourLog.warn("Response did not specify a charset.");
			charset = Charset.forName("UTF-8");
		}

		Reader reader = new InputStreamReader(theResponse.getEntity().getContent(), charset);
		return reader;
	}

}
