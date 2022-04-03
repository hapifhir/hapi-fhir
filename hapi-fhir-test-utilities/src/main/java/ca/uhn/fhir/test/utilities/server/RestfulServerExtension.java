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
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.api.ServerValidationModeEnum;
import ca.uhn.fhir.rest.server.IPagingProvider;
import ca.uhn.fhir.rest.server.RestfulServer;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.jupiter.api.extension.ExtensionContext;

import javax.servlet.http.HttpServlet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class RestfulServerExtension extends BaseJettyServerExtension<RestfulServerExtension> {
	private FhirContext myFhirContext;
	private List<Object> myProviders = new ArrayList<>();
	private FhirVersionEnum myFhirVersion;
	private IGenericClient myFhirClient;
	private RestfulServer myServlet;
	private List<Consumer<RestfulServer>> myConsumers = new ArrayList<>();
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

	@Override
	public void beforeEach(ExtensionContext theContext) throws Exception {
		createContextIfNeeded();
		super.beforeEach(theContext);
	}

	public RestfulServerExtension registerProvider(Object theProvider) {
		Validate.notNull(theProvider);
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

	public RestfulServerExtension withPagingProvider(IPagingProvider thePagingProvider) {
		if (myServlet != null) {
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
}
