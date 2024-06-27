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
package ca.uhn.fhir.test.utilities;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorService;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.PagingHttpMethodEnum;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.FifoMemoryPagingProvider;
import ca.uhn.fhir.rest.server.IPagingProvider;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.IServerAddressStrategy;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import ca.uhn.fhir.rest.server.provider.HashMapResourceProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.eclipse.jetty.ee10.servlet.ServletApiRequest;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.ConceptMap;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Reference;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RestServerR4Helper extends BaseRestServerHelper implements BeforeEachCallback, AfterEachCallback {
	private final MyRestfulServer myRestServer;
	private static IPagingProvider myPagingProvider = new FifoMemoryPagingProvider(20);

	public RestServerR4Helper() {
		this(false, false);
	}

	private RestServerR4Helper(boolean theInitialize, boolean theTransactionLatchEnabled) {
		super(FhirContext.forR4Cached());
		myRestServer = new MyRestfulServer(myFhirContext, theTransactionLatchEnabled);
		if (theInitialize) {
			try {
				myRestServer.initialize();
			} catch (ServletException e) {
				throw new RuntimeException(Msg.code(2110) + "Failed to initialize server", e);
			}
		}
	}

	public static RestServerR4Helper newWithTransactionLatch() {
		return new RestServerR4Helper(false, true);
	}

	public static RestServerR4Helper newInitialized() {
		return new RestServerR4Helper(true, false);
	}

	@Override
	public void beforeEach(ExtensionContext context) throws Exception {
		startServer(myRestServer);
	}

	@Override
	public void afterEach(ExtensionContext context) throws Exception {
		super.afterEach();
		myRestServer.getInterceptorService().unregisterAllAnonymousInterceptors();
		myRestServer.clearDataAndCounts();
	}

	public List<Bundle> getTransactions() {
		List<IBaseBundle> transactions = myRestServer.getPlainProvider().getTransactions();

		// Make a copy to avoid synchronization issues
		transactions = new ArrayList<>(transactions);

		return transactions
			.stream()
			.map(t -> (Bundle) t)
			.collect(Collectors.toList());
	}

	@Override
	public void clearDataAndCounts() {
		myRestServer.clearDataAndCounts();
	}

	@Override
	public void setFailNextPut(boolean theFailNextPut) {
		myRestServer.setFailNextPut(theFailNextPut);
	}

	@Override
	public List<Object> getInterceptors() {
		return myRestServer.getInterceptorService().getAllRegisteredInterceptors();
	}

	@Override
	public void unregisterInterceptor(Object theInterceptor) {
		myRestServer.getInterceptorService().unregisterInterceptor(theInterceptor);
	}

	@Override
	public void clearCounts() {
		myRestServer.clearCounts();
	}

	@Override
	public long getPatientCountSearch() {
		return myRestServer.getPatientResourceProvider().getCountSearch();
	}

	@Override
	public long getPatientCountDelete() {
		return myRestServer.getPatientResourceProvider().getCountDelete();
	}

	@Override
	public long getPatientCountUpdate() {
		return myRestServer.getPatientResourceProvider().getCountUpdate();
	}

	@Override
	public long getPatientCountRead() {
		return myRestServer.getPatientResourceProvider().getCountRead();
	}

	@Override
	public long getObservationCountSearch() {
		return myRestServer.getObservationResourceProvider().getCountSearch();
	}

	@Override
	public long getObservationCountDelete() {
		return myRestServer.getObservationResourceProvider().getCountDelete();
	}

	@Override
	public long getObservationCountUpdate() {
		return myRestServer.getObservationResourceProvider().getCountUpdate();
	}

	@Override
	public long getObservationCountRead() {
		return myRestServer.getObservationResourceProvider().getCountRead();
	}

	@Override
	public boolean registerInterceptor(Object theInterceptor) {
		return myRestServer.getInterceptorService().registerInterceptor(theInterceptor);
	}

	public void registerProvider(Object theProvider) {
		myRestServer.registerProvider(theProvider);
	}

	public void setExpectedCount(int theCount) {
		myRestServer.getPlainProvider().setExpectedCount(theCount);
	}

	public List<HookParams> awaitExpected() throws InterruptedException {
		return myRestServer.getPlainProvider().awaitExpected();
	}

	@Override
	public HashMapResourceProvider<Observation> getObservationResourceProvider() {
		return myRestServer.getObservationResourceProvider();
	}

	public void setObservationResourceProvider(HashMapResourceProvider<Observation> theResourceProvider) {
		myRestServer.setObservationResourceProvider(theResourceProvider);
	}

	@Override
	public HashMapResourceProvider<Patient> getPatientResourceProvider() {
		return myRestServer.getPatientResourceProvider();
	}

	public void setPatientResourceProvider(HashMapResourceProvider<Patient> theResourceProvider) {
		myRestServer.setPatientResourceProvider(theResourceProvider);
	}

	@Override
	public HashMapResourceProvider<ConceptMap> getConceptMapResourceProvider() {
		return myRestServer.getConceptMapResourceProvider();
	}

	public void setConceptMapResourceProvider(HashMapResourceProvider<ConceptMap> theResourceProvider) {
		myRestServer.setConceptMapResourceProvider(theResourceProvider);
	}

	public void setPagingProvider(IPagingProvider thePagingProvider) {
		myPagingProvider = thePagingProvider;
	}

	@Override
	public IIdType createPatientWithId(String theId) {
		Patient patient = new Patient();
		patient.setId("Patient/" + theId);
		patient.addIdentifier().setSystem("http://foo").setValue(theId);
		return this.createPatient(patient);
	}

	@Override
	public IIdType createPatient(IBaseResource theBaseResource) {
		return myRestServer.getPatientResourceProvider().store((Patient) theBaseResource);
	}

	@Override
	public IIdType createObservationForPatient(IIdType thePatientId) {
		Observation observation = new Observation();
		observation.setSubject(new Reference(thePatientId));
		return this.createObservation(observation);
		//TODO maybe add some data to this obs?
	}

	@Override
	public IIdType createObservation(IBaseResource theBaseResource) {
		return myRestServer.getObservationResourceProvider().store((Observation) theBaseResource);
	}

	public List<String> getRequestUrls() {
		return myRestServer.myRequestUrls;
	}

	public List<String> getRequestVerbs() {
		return myRestServer.myRequestVerbs;
	}

	public List<Map<String, String>> getRequestHeaders() {
		return myRestServer.myRequestHeaders;
	}

	public IInterceptorService getInterceptorService() {
		return myRestServer.getInterceptorService();
	}

	@Override
	public void setServerAddressStrategy(IServerAddressStrategy theServerAddressStrategy) {
		myRestServer.setServerAddressStrategy(theServerAddressStrategy);
	}

	public void executeWithLatch(Runnable theRunnable) throws InterruptedException {
		myRestServer.executeWithLatch(theRunnable);
	}

	public void enableTransactionLatch(boolean theTransactionLatchEnabled) {
		myRestServer.setTransactionLatchEnabled(theTransactionLatchEnabled);
	}

	public void setHttpMethodForPagingRequest(PagingHttpMethodEnum thePagingHttpMethod) {
		myRestServer.setHttpMethodForPagingRequest(thePagingHttpMethod);
	}

	private static class MyRestfulServer extends RestfulServer {
		private final List<String> myRequestUrls = Collections.synchronizedList(new ArrayList<>());
		private final List<String> myRequestVerbs = Collections.synchronizedList(new ArrayList<>());
		private final List<Map<String, String>> myRequestHeaders = Collections.synchronizedList(new ArrayList<>());
		private boolean myFailNextPut;
		private HashMapResourceProvider<Patient> myPatientResourceProvider;
		private HashMapResourceProvider<Observation> myObservationResourceProvider;
		private HashMapResourceProvider<Organization> myOrganizationResourceProvider;
		private HashMapResourceProvider<ConceptMap> myConceptMapResourceProvider;
		private RestServerDstu3Helper.MyPlainProvider myPlainProvider;

		private final boolean myInitialTransactionLatchEnabled;

		private PagingHttpMethodEnum myPagingHttpMethod = PagingHttpMethodEnum.GET;

		public MyRestfulServer(FhirContext theFhirContext, boolean theInitialTransactionLatchEnabled) {
			super(theFhirContext);
			myInitialTransactionLatchEnabled = theInitialTransactionLatchEnabled;
		}

		public RestServerDstu3Helper.MyPlainProvider getPlainProvider() {
			return myPlainProvider;
		}
		protected boolean isTransactionLatchEnabled() {
			if (getPlainProvider() == null) {
				return false;
			}
			return getPlainProvider().isTransactionLatchEnabled();
		}
		public void setTransactionLatchEnabled(boolean theTransactionLatchEnabled) {
			getPlainProvider().setTransactionLatchEnabled(theTransactionLatchEnabled);
		}

		public void setHttpMethodForPagingRequest(PagingHttpMethodEnum thePagingHttpMethod) {
			myPagingHttpMethod = thePagingHttpMethod;
		}

		@Override
		protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			// emulate a GET request invocation in the case of a POST paging request, as POST paging is not supported by FHIR RestServer
			RequestTypeEnum requestType = myPagingHttpMethod == PagingHttpMethodEnum.POST ? RequestTypeEnum.GET : RequestTypeEnum.POST;
			super.handleRequest(requestType, request, response);
		}

		public void executeWithLatch(Runnable theRunnable) throws InterruptedException {
			myPlainProvider.setExpectedCount(1);
			theRunnable.run();
			myPlainProvider.awaitExpected();
		}

		public void setFailNextPut(boolean theFailNextPut) {
			myFailNextPut = theFailNextPut;
		}

		public void clearCounts() {
			for (IResourceProvider next : getResourceProviders()) {
				if (next instanceof HashMapResourceProvider<?> provider) {
					provider.clearCounts();
				}
			}
			if (isTransactionLatchEnabled()) {
				myPlainProvider.clear();
			}
			myRequestUrls.clear();
			myRequestVerbs.clear();
		}

		@Override
		protected void service(HttpServletRequest theReq, HttpServletResponse theResp) throws ServletException, IOException {
			ServletApiRequest request = (ServletApiRequest) theReq;

			Map<String, String> headers = pullOutHeaders(theReq);
			myRequestHeaders.add(headers);
			myRequestVerbs.add(request.getMethod());

			String nextRequestUrl = request.getRequestURI();
			if (request.getQueryString() != null) {
				nextRequestUrl += "?" + request.getQueryString();
			}
			myRequestUrls.add(nextRequestUrl);

			super.service(theReq, theResp);
		}

		private Map<String, String> pullOutHeaders(HttpServletRequest theReq) {
			Enumeration<String> headerNames = theReq.getHeaderNames();
			Map<String, String> headers = new HashMap<>();
			while (headerNames.hasMoreElements()) {
				String headerName = headerNames.nextElement();
				headers.put(headerName, theReq.getHeader(headerName));
			}
			return headers;
		}

		public void clearDataAndCounts() {
			for (IResourceProvider next : getResourceProviders()) {
				if (next instanceof HashMapResourceProvider<?> provider) {
					provider.clear();
				}
			}
			clearCounts();
		}

		public HashMapResourceProvider<Observation> getObservationResourceProvider() {
			return myObservationResourceProvider;
		}

		public void setPatientResourceProvider(HashMapResourceProvider<Patient> theResourceProvider) {
			myPatientResourceProvider.getStoredResources().forEach(theResourceProvider::store);

			unregisterProvider(myPatientResourceProvider);
			registerProvider(theResourceProvider);
			myPatientResourceProvider = theResourceProvider;
		}

		public void setObservationResourceProvider(HashMapResourceProvider<Observation> theResourceProvider) {
			myObservationResourceProvider.getStoredResources().forEach(theResourceProvider::store);

			unregisterProvider(myObservationResourceProvider);
			registerProvider(theResourceProvider);
			myObservationResourceProvider = theResourceProvider;
		}

		public HashMapResourceProvider<Organization> getOrganizationResourceProvider() {
			return myOrganizationResourceProvider;
		}

		public HashMapResourceProvider<ConceptMap> getConceptMapResourceProvider() {
			return myConceptMapResourceProvider;
		}

		public void setConceptMapResourceProvider(HashMapResourceProvider<ConceptMap> theResourceProvider) {
			myConceptMapResourceProvider.getStoredResources().forEach(theResourceProvider::store);

			unregisterProvider(myConceptMapResourceProvider);
			registerProvider(theResourceProvider);
			myConceptMapResourceProvider = theResourceProvider;
		}

		public HashMapResourceProvider<Patient> getPatientResourceProvider() {
			return myPatientResourceProvider;
		}

		@Override
		protected void initialize() throws ServletException {
			super.initialize();

			FhirContext fhirContext = getFhirContext();
			myPatientResourceProvider = new MyHashMapResourceProvider<>(fhirContext, Patient.class);
			registerProvider(myPatientResourceProvider);
			myObservationResourceProvider = new MyHashMapResourceProvider<>(fhirContext, Observation.class);
			registerProvider(myObservationResourceProvider);
			myOrganizationResourceProvider = new MyHashMapResourceProvider<>(fhirContext, Organization.class);
			registerProvider(myOrganizationResourceProvider);
			myConceptMapResourceProvider = new MyHashMapResourceProvider<>(fhirContext, ConceptMap.class);
			registerProvider(myConceptMapResourceProvider);

			myPlainProvider = new RestServerDstu3Helper.MyPlainProvider(myInitialTransactionLatchEnabled);
			registerProvider(myPlainProvider);

			setPagingProvider(myPagingProvider);
		}

		public class MyHashMapResourceProvider<T extends IBaseResource> extends HashMapResourceProvider<T> {
			public MyHashMapResourceProvider(FhirContext theContext, Class<T> theType) {
				super(theContext, theType);
			}

			@Override
			public MethodOutcome update(T theResource, String theConditional, RequestDetails theRequestDetails) {
				if (myFailNextPut) {
					throw new PreconditionFailedException(Msg.code(2111) + "Failed update operation");
				}
				return super.update(theResource, theConditional, theRequestDetails);
			}
		}
	}
}
