package ca.uhn.fhir.test.utilities.server;

/*-
 * #%L
 * HAPI FHIR Test Utilities
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.api.ServerValidationModeEnum;
import ca.uhn.fhir.rest.server.RestfulServer;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.function.Consumer;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class RestfulServerExtension extends BaseJettyServerExtension<RestfulServerExtension> {
	private static final Logger ourLog = LoggerFactory.getLogger(RestfulServerExtension.class);
	private final List<List<String>> myRequestHeaders = new ArrayList<>();
	private final List<String> myRequestContentTypes = new ArrayList<>();
	private FhirContext myFhirContext;
	private List<Object> myProviders = new ArrayList<>();
	private FhirVersionEnum myFhirVersion;
	private IGenericClient myFhirClient;
	private RestfulServer myServlet;
	private List<Consumer<RestfulServer>> myConsumers = new ArrayList<>();
	private ServerValidationModeEnum myServerValidationMode = ServerValidationModeEnum.NEVER;

	/**
	 * Constructor
	 */
	public RestfulServerExtension(FhirContext theFhirContext, Object... theProviders) {
		Validate.notNull(theFhirContext);
		myFhirContext = theFhirContext;
		if (theProviders != null) {
			myProviders = new ArrayList<>(Arrays.asList(theProviders));
		}
	}

	/**
	 * Constructor: If this is used, it will create and tear down a FhirContext which is good for memory
	 */
	public RestfulServerExtension(FhirVersionEnum theFhirVersionEnum) {
		Validate.notNull(theFhirVersionEnum);
		myFhirVersion = theFhirVersionEnum;
	}

	@Override
	protected void startServer() throws Exception {
		super.startServer();

		myFhirContext.getRestfulClientFactory().setSocketTimeout((int) (500 * DateUtils.MILLIS_PER_SECOND));
		myFhirContext.getRestfulClientFactory().setServerValidationMode(myServerValidationMode);
		myFhirClient = myFhirContext.newRestfulGenericClient("http://localhost:" + getPort());
	}

	@Override
	protected HttpServlet provideServlet() {
		if (myServlet == null) {
			myServlet = new RestfulServer(myFhirContext);
			myServlet.setDefaultPrettyPrint(true);
			myServlet.registerInterceptor(new RestfulServerExtension.ListenerExtension());
			if (myProviders != null) {
				myServlet.registerProviders(myProviders);
			}

			myConsumers.forEach(t -> t.accept(myServlet));
		}

		return myServlet;
	}

	@Override
	protected void stopServer() throws Exception {
		super.stopServer();
		if (!isRunning()) {
			return;
		}
		myFhirClient = null;
	}


	private void createContextIfNeeded() {
		if (myFhirVersion != null) {
			myFhirContext = FhirContext.forCached(myFhirVersion);
		}
	}

	public IGenericClient getFhirClient() {
		return myFhirClient;
	}

	public FhirContext getFhirContext() {
		createContextIfNeeded();
		return myFhirContext;
	}

	public RestfulServer getRestfulServer() {
		return myServlet;
	}

	public List<String> getRequestContentTypes() {
		return myRequestContentTypes;
	}

	public List<List<String>> getRequestHeaders() {
		return myRequestHeaders;
	}

	@Override
	public void beforeEach(ExtensionContext theContext) throws Exception {
		createContextIfNeeded();
		myRequestContentTypes.clear();
		myRequestHeaders.clear();

		super.beforeEach(theContext);
	}

	public RestfulServerExtension registerProvider(Object theProvider) {
		if (myServlet != null) {
			myServlet.registerProvider(theProvider);
		} else {
			myProviders.add(theProvider);
		}
		return this;
	}

	public RestfulServerExtension withServer(Consumer<RestfulServer> theConsumer) {
		if (myServlet != null) {
			theConsumer.accept(myServlet);
		} else {
			myConsumers.add(theConsumer);
		}
		return this;
	}

	public RestfulServerExtension registerInterceptor(Object theInterceptor) {
		return withServer(t -> t.getInterceptorService().registerInterceptor(theInterceptor));
	}

	public RestfulServerExtension withValidationMode(ServerValidationModeEnum theValidationMode) {
		myServerValidationMode = theValidationMode;
		return this;
	}

	public void unregisterAllInterceptors() {
		myServlet.getInterceptorService().unregisterAllInterceptors();
	}

	@Interceptor
	private class ListenerExtension {


		@Hook(Pointcut.SERVER_INCOMING_REQUEST_POST_PROCESSED)
		public void postProcessed(HttpServletRequest theRequest) {
			String header = theRequest.getHeader(Constants.HEADER_CONTENT_TYPE);
			if (isNotBlank(header)) {
				myRequestContentTypes.add(header.replaceAll(";.*", ""));
			} else {
				myRequestContentTypes.add(null);
			}

			java.util.Enumeration<String> headerNamesEnum = theRequest.getHeaderNames();
			List<String> requestHeaders = new ArrayList<>();
			myRequestHeaders.add(requestHeaders);
			while (headerNamesEnum.hasMoreElements()) {
				String nextName = headerNamesEnum.nextElement();
				Enumeration<String> valueEnum = theRequest.getHeaders(nextName);
				while (valueEnum.hasMoreElements()) {
					String nextValue = valueEnum.nextElement();
					requestHeaders.add(nextName + ": " + nextValue);
				}
			}

		}

	}

}
