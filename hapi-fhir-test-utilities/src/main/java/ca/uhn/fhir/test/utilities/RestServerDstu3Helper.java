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
import ca.uhn.fhir.rest.annotation.Transaction;
import ca.uhn.fhir.rest.annotation.TransactionParam;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.IServerAddressStrategy;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import ca.uhn.fhir.rest.server.provider.HashMapResourceProvider;
import ca.uhn.test.concurrency.IPointcutLatch;
import ca.uhn.test.concurrency.PointcutLatch;
import jakarta.servlet.ServletException;
import org.hl7.fhir.dstu3.model.ConceptMap;
import org.hl7.fhir.dstu3.model.Observation;
import org.hl7.fhir.dstu3.model.Organization;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.dstu3.model.Reference;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public class RestServerDstu3Helper extends BaseRestServerHelper implements IPointcutLatch, BeforeEachCallback, AfterEachCallback {
	protected final MyRestfulServer myRestServer;

	public RestServerDstu3Helper() {
		this(false, false);
	}

	private RestServerDstu3Helper(boolean theInitialize, boolean theTransactionLatchEnabled) {
		super(FhirContext.forDstu3());
		myRestServer = new MyRestfulServer(myFhirContext, theTransactionLatchEnabled);
		if (theInitialize) {
			try {
				myRestServer.initialize();
			} catch (ServletException e) {
				throw new RuntimeException(Msg.code(2252) + "Failed to initialize server", e);
			}
		}
	}

	public static RestServerDstu3Helper newInitialized() {
		return new RestServerDstu3Helper(true, false);
	}

	public static RestServerDstu3Helper newWithTransactionLatch() {
		return new RestServerDstu3Helper(false, true);
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

	@Override
	public void clear() {
		myRestServer.getPlainProvider().clear();
	}

	@Override
	public void setExpectedCount(int theCount) {
		myRestServer.getPlainProvider().setExpectedCount(theCount);
	}

	@Override
	public List<HookParams> awaitExpected() throws InterruptedException {
		return myRestServer.getPlainProvider().awaitExpected();
	}

	public void registerProvider(Object theProvider) {
		myRestServer.registerProvider(theProvider);
	}

	public static class MyPlainProvider implements IPointcutLatch {
		private final PointcutLatch myPointcutLatch = new PointcutLatch("Transaction Counting Provider");
		private final List<IBaseBundle> myTransactions = Collections.synchronizedList(new ArrayList<>());
		private boolean myTransactionLatchEnabled;

		public MyPlainProvider(boolean theTransactionLatchEnabled) {
			this.myTransactionLatchEnabled = theTransactionLatchEnabled;
		}

		@Transaction
		public synchronized IBaseBundle transaction(@TransactionParam IBaseBundle theBundle) {
			if (myTransactionLatchEnabled) {
				myPointcutLatch.call(theBundle);
			}
			myTransactions.add(theBundle);
			return theBundle;
		}

		@Override
		public void clear() {
			if (!myTransactionLatchEnabled) {
				throw new IllegalStateException("Can't call clear() on a provider that doesn't use a latch");
			}
			myPointcutLatch.clear();
		}

		@Override
		public void setExpectedCount(int theCount) {
			if (!myTransactionLatchEnabled) {
				throw new IllegalStateException("Can't call clear() on a provider that doesn't use a latch");
			}
			myPointcutLatch.setExpectedCount(theCount);
		}

		@Override
		public List<HookParams> awaitExpected() throws InterruptedException {
			if (!myTransactionLatchEnabled) {
				throw new IllegalStateException("Can't call clear() on a provider that doesn't use a latch");
			}
			return myPointcutLatch.awaitExpected();
		}

		public List<IBaseBundle> getTransactions() {
			return Collections.unmodifiableList(new ArrayList<>(myTransactions));
		}

		public void setTransactionLatchEnabled(boolean theTransactionLatchEnabled) {
			this.myTransactionLatchEnabled = theTransactionLatchEnabled;
		}

		public boolean isTransactionLatchEnabled() {
			return myTransactionLatchEnabled;
		}
	}

	private static class MyRestfulServer extends RestfulServer {
		private boolean myFailNextPut;
		private HashMapResourceProvider<Patient> myPatientResourceProvider;
		private HashMapResourceProvider<Observation> myObservationResourceProvider;
		private HashMapResourceProvider<Organization> myOrganizationResourceProvider;
		private HashMapResourceProvider<ConceptMap> myConceptMapResourceProvider;
		private MyPlainProvider myPlainProvider;
		private final boolean myInitialTransactionLatchEnabled;

		public MyRestfulServer(FhirContext theFhirContext, boolean theInitialTransactionLatchEnabled) {
			super(theFhirContext);
			myInitialTransactionLatchEnabled = theInitialTransactionLatchEnabled;
		}

		public MyPlainProvider getPlainProvider() {
			return myPlainProvider;
		}

		public <T> T executeWithLatch(Supplier<T> theSupplier) throws InterruptedException {
			myPlainProvider.setExpectedCount(1);
			T retval = theSupplier.get();
			myPlainProvider.awaitExpected();
			return retval;
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
			if (isTransactionLatchEnabled()) {
				getPlainProvider().clear();
			}
		}

		private boolean isTransactionLatchEnabled() {
			if (getPlainProvider() == null) {
				return false;
			}
			return getPlainProvider().isTransactionLatchEnabled();
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

			myPlainProvider = new MyPlainProvider(myInitialTransactionLatchEnabled);
			registerProvider(myPlainProvider);
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
					throw new PreconditionFailedException(Msg.code(2251) + "Failed update operation");
				}
				return super.update(theResource, theConditional, theRequestDetails);
			}
		}
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
	public IIdType createObservationForPatient(IIdType theFirstTargetPatientId) {
		Observation observation = new Observation();
		observation.setSubject(new Reference(theFirstTargetPatientId));
		return this.createObservation(observation);

	}

	@Override
	public IIdType createObservation(IBaseResource theBaseResource) {
		return myRestServer.getObservationResourceProvider().store((Observation) theBaseResource);
	}

	@Override
	protected void setServerAddressStrategy(IServerAddressStrategy theServerAddressStrategy) {
		myRestServer.setServerAddressStrategy(theServerAddressStrategy);
	}

	public void setConceptMapResourceProvider(HashMapResourceProvider<ConceptMap> theResourceProvider) {
		myRestServer.setConceptMapResourceProvider(theResourceProvider);
	}
}
