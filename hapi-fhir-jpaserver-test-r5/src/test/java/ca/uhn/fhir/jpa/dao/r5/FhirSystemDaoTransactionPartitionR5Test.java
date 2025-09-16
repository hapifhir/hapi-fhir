package ca.uhn.fhir.jpa.dao.r5;

import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.dao.TransactionPrePartitionResponse;
import ca.uhn.fhir.jpa.dao.TransactionUtil;
import ca.uhn.fhir.util.BundleBuilder;
import org.apache.commons.lang3.Range;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.r5.model.Bundle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
		assertEquals("Observation/OBS-0", responseParsed.getStorageOutcomes().get(0).getTargetId().getValue());
		assertEquals(201, responseParsed.getStorageOutcomes().get(0).getStatusCode());
		assertEquals("Patient/PAT-0", responseParsed.getStorageOutcomes().get(0).getTargetId().getValue());
		assertEquals(201, responseParsed.getStorageOutcomes().get(0).getStatusCode());
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
					entries.add(inputBundle.getEntry().get(nextIndex));
				}

				Bundle requestBundle = new Bundle();
				requestBundles.add(requestBundle);
				requestBundle.setType(Bundle.BundleType.TRANSACTION);
				entries.forEach(requestBundle::addEntry);
			}

			return new TransactionPrePartitionResponse(requestBundles);
		}

	}


}


