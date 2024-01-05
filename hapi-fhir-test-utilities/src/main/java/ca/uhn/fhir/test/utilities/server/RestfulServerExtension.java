/*-
 * #%L
 * HAPI FHIR Test Utilities
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.test.utilities.server;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.interceptor.api.IAnonymousInterceptor;
import ca.uhn.fhir.interceptor.api.IInterceptorService;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.api.ServerValidationModeEnum;
import ca.uhn.fhir.rest.server.IPagingProvider;
import ca.uhn.fhir.rest.server.IServerAddressStrategy;
import ca.uhn.fhir.rest.server.RestfulServer;
import jakarta.servlet.http.HttpServlet;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class RestfulServerExtension extends BaseJettyServerExtension<RestfulServerExtension> {
	private static final Logger ourLog = LoggerFactory.getLogger(RestfulServerExtension.class);
	private FhirContext myFhirContext;
	private List<Object> myProviders = new ArrayList<>();
	private FhirVersionEnum myFhirVersion;
	private RestfulServer myServlet;
	private List<Consumer<RestfulServer>> myConsumers = new ArrayList<>();
	private Map<String, Object> myRunningServerUserData = new HashMap<>();
	private ServerValidationModeEnum myServerValidationMode = ServerValidationModeEnum.NEVER;
	private IPagingProvider myPagingProvider;

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

	/**
	 * User data map which is automatically cleared when the server is stopped
	 */
	public Map<String, Object> getRunningServerUserData() {
		return myRunningServerUserData;
	}

	@Override
	protected void startServer() throws Exception {
		super.startServer();

		myFhirContext.getRestfulClientFactory().setSocketTimeout((int) (500 * DateUtils.MILLIS_PER_SECOND));
		myFhirContext.getRestfulClientFactory().setServerValidationMode(myServerValidationMode);

		ourLog.info("FHIR server has been started with base URL: {}", getBaseUrl());
	}

	@Override
	protected HttpServlet provideServlet() {
		if (myServlet == null) {
			myServlet = new RestfulServer(myFhirContext);
			myServlet.setDefaultPrettyPrint(true);
			if (myProviders != null) {
				myServlet.registerProviders(myProviders);
			}
			if (myPagingProvider != null) {
				myServlet.setPagingProvider(myPagingProvider);
			}

			myConsumers.forEach(t -> t.accept(myServlet));
		}

		return myServlet;
	}

	@Override
	public void stopServer() throws Exception {
		super.stopServer();
		if (!isRunning()) {
			return;
		}
		myRunningServerUserData.clear();
		myPagingProvider = null;
		myServlet = null;
	}


	private void createContextIfNeeded() {
		if (myFhirVersion != null) {
			myFhirContext = FhirContext.forCached(myFhirVersion);
		}
	}

	/**
	 * Creates a new client for each callof this method
	 */
	public IGenericClient getFhirClient() {
		return myFhirContext.newRestfulGenericClient(getBaseUrl());
	}

	public FhirContext getFhirContext() {
		createContextIfNeeded();
		return myFhirContext;
	}

	public RestfulServer getRestfulServer() {
		return myServlet;
	}

	@Override
	public void beforeEach(ExtensionContext theContext) throws Exception {
		createContextIfNeeded();
		super.beforeEach(theContext);
	}

	public RestfulServerExtension registerProvider(Object theProvider) {
		Validate.notNull(theProvider);
		if (isStarted()) {
			myServlet.registerProvider(theProvider);
		} else {
			myProviders.add(theProvider);
		}
		return this;
	}

	public RestfulServerExtension withServer(Consumer<RestfulServer> theConsumer) {
		if (isStarted()) {
			theConsumer.accept(myServlet);
		} else {
			myConsumers.add(theConsumer);
		}
		return this;
	}

	private boolean isStarted() {
		return myServlet != null;
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

	public RestfulServerExtension withPagingProvider(IPagingProvider thePagingProvider) {
		if (isStarted()) {
			myServlet.setPagingProvider(thePagingProvider);
		} else {
			myPagingProvider = thePagingProvider;
		}
		return this;
	}

	public RestfulServerExtension unregisterInterceptor(Object theInterceptor) {
		return withServer(t -> t.getInterceptorService().unregisterInterceptor(theInterceptor));
	}

	public void unregisterProvider(Object theProvider) {
		withServer(t -> t.unregisterProvider(theProvider));
	}

	public Integer getDefaultPageSize() {
		return myServlet.getDefaultPageSize();
	}

	public void setDefaultPageSize(Integer theInitialDefaultPageSize) {
		myServlet.setDefaultPageSize(theInitialDefaultPageSize);
	}

	public IInterceptorService getInterceptorService() {
		return myServlet.getInterceptorService();
	}

	public RestfulServerExtension registerAnonymousInterceptor(Pointcut thePointcut, IAnonymousInterceptor theInterceptor) {
		return withServer(t -> t.getInterceptorService().registerAnonymousInterceptor(thePointcut, theInterceptor));
	}

	public RestfulServerExtension withDefaultResponseEncoding(EncodingEnum theEncodingEnum) {
		return withServer(t -> t.setDefaultResponseEncoding(theEncodingEnum));
	}

	public RestfulServerExtension setDefaultResponseEncoding(EncodingEnum theEncodingEnum) {
		return withServer(s->myServlet.setDefaultResponseEncoding(theEncodingEnum));
	}

	public RestfulServerExtension setDefaultPrettyPrint(boolean theDefaultPrettyPrint) {
		withServer(s -> s.setDefaultPrettyPrint(theDefaultPrettyPrint));
		return this;
	}

	public RestfulServerExtension setServerAddressStrategy(IServerAddressStrategy theServerAddressStrategy) {
		withServer(s -> s.setServerAddressStrategy(theServerAddressStrategy));
		return this;
	}
}
