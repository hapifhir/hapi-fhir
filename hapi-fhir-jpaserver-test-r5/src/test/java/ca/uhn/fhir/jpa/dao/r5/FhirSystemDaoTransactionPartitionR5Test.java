package ca.uhn.fhir.jpa.dao.r5;

import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.dao.TransactionPrePartitionResponse;
import ca.uhn.fhir.jpa.dao.TransactionUtil;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.util.BundleBuilder;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.model.Bundle;
import org.hl7.fhir.r5.model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class FhirSystemDaoTransactionPartitionR5Test extends BaseJpaR5Test {

	private final MyTransactionPrePartitionInterceptor myInterceptor = new MyTransactionPrePartitionInterceptor();

	@BeforeEach
	public void before() {
		registerInterceptor(myInterceptor);
	}

	@Test
	public void testSplitBundle() {
		// Setup
		BundleBuilder requestBuilder = new BundleBuilder(myFhirContext);
		requestBuilder.addTransactionUpdateEntry(buildObservation(withId("OBS-0"), withSubject("Patient/PAT-0"), withStatus("final")));
		requestBuilder.addTransactionUpdateEntry(buildPatient(withId("PAT-0")));
		requestBuilder.addTransactionUpdateEntry(buildObservation(withId("OBS-1"), withSubject("Patient/PAT-1"), withStatus("final")));
		requestBuilder.addTransactionUpdateEntry(buildPatient(withId("PAT-1")));
		Bundle request = requestBuilder.getBundleTyped();

		myInterceptor.setNextRanges(List.of(
			List.of(1, 3), List.of(0, 2)
		));

		// Test
		Bundle response = mySystemDao.transaction(newSrd(), request);

		// Verify
		TransactionUtil.TransactionResponse responseParsed = TransactionUtil.parseTransactionResponse(myFhirContext, request, response);
		assertEquals("Observation/OBS-0/_history/1", responseParsed.getStorageOutcomes().get(0).getTargetId().getValue());
		assertEquals(201, responseParsed.getStorageOutcomes().get(0).getStatusCode());
		assertEquals("Patient/PAT-0/_history/1", responseParsed.getStorageOutcomes().get(1).getTargetId().getValue());
		assertEquals(201, responseParsed.getStorageOutcomes().get(0).getStatusCode());
	}

	@Test
	public void testSplitBundle_SecondBundleFails_FirstShouldNotHaveRollbackItemsApplied() {
		// Setup
		BundleBuilder requestBuilder = new BundleBuilder(myFhirContext);
		requestBuilder.addTransactionCreateEntry(buildPatient(withActiveTrue()));
		requestBuilder.addTransactionCreateEntry(buildPatient(withActiveFalse()));
		Bundle request = requestBuilder.getBundleTyped();

		myInterceptor.setNextRanges(List.of(
			List.of(0), List.of(1)
		));

		@Interceptor
		class FailingInterceptor {

			@Hook(Pointcut.STORAGE_PRECOMMIT_RESOURCE_CREATED)
			public void preCreate(IBaseResource theResource) {
				if (theResource instanceof Patient patient) {
					// Only fail for the second Patient in the TX bundle
					if (patient.getActiveElement().getValue().equals(false)) {
						throw new InternalErrorException("Patient cannot be inactive");
					}
				}
			}

		}
		registerInterceptor(new FailingInterceptor());

		// Test
		assertThatThrownBy(()->mySystemDao.transaction(newSrd(), request))
			.isInstanceOf(InternalErrorException.class);

		// Verify

		// ID assignment should be rolled back on the second (failing) patient, but not
		// on the first (succeeding) patient
		Patient p0 = (Patient) request.getEntry().get(0).getResource();
		assertThat(p0.getIdElement().getIdPart()).matches("[0-9]+");
		Patient p1 = (Patient)request.getEntry().get(1).getResource();
		assertNull(p1.getIdElement().getIdPart());
	}


	public static class MyTransactionPrePartitionInterceptor {

		private List<List<Integer>> myNextRanges;

		@SafeVarargs
		public final void setNextRanges(List<Integer>... theNextRanges) {
			myNextRanges = List.of(theNextRanges);
		}

		public final void setNextRanges(List<List<Integer>> theNextRanges) {
			myNextRanges = theNextRanges;
		}

		@Hook(Pointcut.STORAGE_TRANSACTION_PRE_PARTITION)
		public TransactionPrePartitionResponse prePartition(IBaseBundle theInputBundle) {
			List<IBaseBundle> requestBundles = new ArrayList<>();
			Bundle inputBundle = (Bundle) theInputBundle;

			for (List<Integer> nextRange : myNextRanges) {
				List<Bundle.BundleEntryComponent> entries = new ArrayList<>();
				for (int nextIndex : nextRange) {
					Bundle.BundleEntryComponent entry;
					if (nextIndex == -1) {
						entry = new Bundle.BundleEntryComponent();
						entry.setFullUrl("http://foo-123");
					} else {
						entry = inputBundle.getEntry().get(nextIndex);
					}
					entries.add(entry);
				}

				Bundle requestBundle = new Bundle();
				requestBundles.add(requestBundle);
				requestBundle.setType(Bundle.BundleType.TRANSACTION);
				entries.forEach(requestBundle::addEntry);
			}

			return new TransactionPrePartitionResponse().setSplitBundles(requestBundles);
		}

	}


}


