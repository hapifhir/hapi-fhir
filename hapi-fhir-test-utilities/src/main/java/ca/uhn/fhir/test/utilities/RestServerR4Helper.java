package ca.uhn.fhir.test.utilities;

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
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorService;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.FifoMemoryPagingProvider;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.IServerAddressStrategy;
import ca.uhn.fhir.rest.server.IncomingRequestAddressStrategy;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import ca.uhn.fhir.rest.server.provider.HashMapResourceProvider;
import org.eclipse.jetty.server.Request;
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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RestServerR4Helper extends BaseRestServerHelper implements BeforeEachCallback, AfterEachCallback {
	protected final MyRestfulServer myRestServer;

	public RestServerR4Helper() {
		this(false);
	}

	public RestServerR4Helper(boolean theInitialize) {
		super(FhirContext.forR4Cached());
		myRestServer = new MyRestfulServer(myFhirContext);
		if(theInitialize){
			try {
				myRestServer.initialize();
			} catch (ServletException e) {
				throw new RuntimeException(Msg.code(2110)+"Failed to initialize server", e);
			}
		}
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
		return myRestServer
			.getPlainProvider()
			.getTransactions()
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

	@Override
	public HashMapResourceProvider<Patient> getPatientResourceProvider() {
		return myRestServer.getPatientResourceProvider();
	}

	@Override
	public HashMapResourceProvider<ConceptMap> getConceptMapResourceProvider() {
		return myRestServer.getConceptMapResourceProvider();
	}

	public void setConceptMapResourceProvider(HashMapResourceProvider<ConceptMap> theResourceProvider) {
		myRestServer.setConceptMapResourceProvider(theResourceProvider);
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
		return  myRestServer.getPatientResourceProvider().store((Patient) theBaseResource);
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
		return  myRestServer.getObservationResourceProvider().store((Observation) theBaseResource);
	}

	public List<String> getRequestUrls() {
		return myRestServer.myRequestUrls;
	}

	public List<String> getRequestVerbs() {
		return myRestServer.myRequestVerbs;
	}

	public void setObservationResourceProvider(HashMapResourceProvider<Observation> theResourceProvider) {
		myRestServer.setObservationResourceProvider(theResourceProvider);
	}

	public List<Map<String, String>> getRequestHeaders() {
		return myRestServer.myRequestHeaders;
	}

	public IInterceptorService getInterceptorService() {
		return myRestServer.getInterceptorService();
	}

	@Override
	public void setServerAddressStrategy(IServerAddressStrategy theServerAddressStrategy){
		myRestServer.setServerAddressStrategy(theServerAddressStrategy);
	}

	private static class MyRestfulServer extends RestfulServer {
		private boolean myFailNextPut;
		private HashMapResourceProvider<Patient> myPatientResourceProvider;
		private HashMapResourceProvider<Observation> myObservationResourceProvider;
		private HashMapResourceProvider<Organization> myOrganizationResourceProvider;
		private HashMapResourceProvider<ConceptMap> myConceptMapResourceProvider;
		private RestServerDstu3Helper.MyPlainProvider myPlainProvider;
		private final List<String> myRequestUrls = Collections.synchronizedList(new ArrayList<>());
		private final List<String> myRequestVerbs = Collections.synchronizedList(new ArrayList<>());
		private final List<Map<String, String>> myRequestHeaders= Collections.synchronizedList(new ArrayList<>());

		public MyRestfulServer(FhirContext theFhirContext) {
			super(theFhirContext);
		}

		public RestServerDstu3Helper.MyPlainProvider getPlainProvider() {
			return myPlainProvider;
		}

		public void setFailNextPut(boolean theFailNextPut) {
			myFailNextPut = theFailNextPut;
		}

		public void clearCounts() {
			for (IResourceProvider next : getResourceProviders()) {
				if (next instanceof HashMapResourceProvider) {
					HashMapResourceProvider provider = (HashMapResourceProvider) next;
					provider.clearCounts();
				}
			}
			myPlainProvider.clear();
			myRequestUrls.clear();
			myRequestVerbs.clear();
		}

		@Override
		protected void service(HttpServletRequest theReq, HttpServletResponse theResp) throws ServletException, IOException {
			Request request = (Request) theReq;

			Map<String, String> headers = pullOutHeaders(theReq);
			myRequestHeaders.add(headers);
			myRequestUrls.add(request.getOriginalURI());
			myRequestVerbs.add(request.getMethod());
			super.service(theReq, theResp);
		}

		private Map<String,String> pullOutHeaders(HttpServletRequest theReq) {
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
				if (next instanceof HashMapResourceProvider) {
					HashMapResourceProvider provider = (HashMapResourceProvider) next;
					provider.clear();
				}
			}
			clearCounts();
		}

		public HashMapResourceProvider<Observation> getObservationResourceProvider() {
			return myObservationResourceProvider;
		}

		public HashMapResourceProvider<Organization> getOrganizationResourceProvider() {
			return myOrganizationResourceProvider;
		}

		public HashMapResourceProvider<ConceptMap> getConceptMapResourceProvider() {
			return myConceptMapResourceProvider;
		}

		public HashMapResourceProvider<Patient> getPatientResourceProvider() {
			return myPatientResourceProvider;
		}

		@Override
		protected void initialize() throws ServletException {
			super.initialize();

			FhirContext fhirContext = getFhirContext();
			myPatientResourceProvider = new MyHashMapResourceProvider(fhirContext, Patient.class);
			registerProvider(myPatientResourceProvider);
			myObservationResourceProvider = new MyHashMapResourceProvider(fhirContext, Observation.class);
			registerProvider(myObservationResourceProvider);
			myOrganizationResourceProvider = new MyHashMapResourceProvider(fhirContext, Organization.class);
			registerProvider(myOrganizationResourceProvider);
			myConceptMapResourceProvider = new MyHashMapResourceProvider(fhirContext, ConceptMap.class);
			registerProvider(myConceptMapResourceProvider);

			myPlainProvider = new RestServerDstu3Helper.MyPlainProvider();
			registerProvider(myPlainProvider);

			setPagingProvider(new FifoMemoryPagingProvider(20));
		}

		public void setObservationResourceProvider(HashMapResourceProvider<Observation> theResourceProvider) {
			myObservationResourceProvider.getStoredResources().forEach(o -> theResourceProvider.store(o));

			unregisterProvider(myObservationResourceProvider);
			registerProvider(theResourceProvider);
			myObservationResourceProvider = theResourceProvider;
		}

		public void setConceptMapResourceProvider(HashMapResourceProvider<ConceptMap> theResourceProvider) {
			myConceptMapResourceProvider.getStoredResources().forEach(c -> theResourceProvider.store(c));

			unregisterProvider(myConceptMapResourceProvider);
			registerProvider(theResourceProvider);
			myConceptMapResourceProvider = theResourceProvider;
		}


		public class MyHashMapResourceProvider<T extends IBaseResource> extends HashMapResourceProvider<T> {
			public MyHashMapResourceProvider(FhirContext theContext, Class theType) {
				super(theContext, theType);
			}

			@Override
			public MethodOutcome update(T theResource, String theConditional, RequestDetails theRequestDetails) {
				if (myFailNextPut) {
					throw new PreconditionFailedException(Msg.code(2111)+"Failed update operation");
				}
				return super.update(theResource, theConditional, theRequestDetails);
			}
		}
	}
}
