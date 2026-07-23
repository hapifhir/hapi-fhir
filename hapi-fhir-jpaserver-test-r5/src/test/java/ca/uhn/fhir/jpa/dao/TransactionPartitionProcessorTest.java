package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.executor.InterceptorService;
import ca.uhn.fhir.jpa.dao.r5.FhirSystemDaoTransactionPartitionR5Test;
import ca.uhn.fhir.model.api.StorageResponseCodeEnum;
import ca.uhn.fhir.rest.api.server.storage.TransactionDetails;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.test.utilities.ITestDataBuilder;
import ca.uhn.fhir.util.BundleBuilder;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r5.model.Bundle;
import org.hl7.fhir.r5.model.CodeableConcept;
import org.hl7.fhir.r5.model.Coding;
import org.hl7.fhir.r5.model.OperationOutcome;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static ca.uhn.fhir.jpa.test.BaseJpaTest.newSrd;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransactionPartitionProcessorTest implements ITestDataBuilder {

	private final FhirContext myFhirContext = FhirContext.forR5Cached();
	private final InterceptorService myInterceptorBroadcaster = new InterceptorService();
	private final FhirSystemDaoTransactionPartitionR5Test.MyTransactionPrePartitionInterceptor myInterceptor =
			new FhirSystemDaoTransactionPartitionR5Test.MyTransactionPrePartitionInterceptor();
	@Mock
	private TransactionProcessor myTransactionProcessor;
	@Captor
	private ArgumentCaptor<Bundle> myBundleCaptor;
	private TransactionPartitionProcessor<Bundle> mySvc;

	@BeforeEach
	public void beforeEach() {
		myInterceptorBroadcaster.registerInterceptor(myInterceptor);
		mySvc = new TransactionPartitionProcessor<>(myTransactionProcessor, myFhirContext, newSrd(), false, myInterceptorBroadcaster,
				"transaction", new TransactionDetails());
	}

	@Test
	public void testTwoPartitions() {
		// Setup
		Bundle request = createRequest_Obs0_Patient0_Obs1_Patient1();

		Bundle response0 = new Bundle();
		response0.setType(Bundle.BundleType.TRANSACTIONRESPONSE);
		addResponseEntry(response0, "201 Created", "Patient/PAT-0", StorageResponseCodeEnum.SUCCESSFUL_CREATE);
		addResponseEntry(response0, "201 Created", "Patient/PAT-1", StorageResponseCodeEnum.SUCCESSFUL_CREATE);
		Bundle response1 = new Bundle();
		response1.setType(Bundle.BundleType.TRANSACTIONRESPONSE);
		addResponseEntry(response1, "201 Created", "Observation/OBS-0", StorageResponseCodeEnum.SUCCESSFUL_CREATE);
		addResponseEntry(response1, "201 Created", "Observation/OBS-1", StorageResponseCodeEnum.SUCCESSFUL_CREATE);
		when(myTransactionProcessor.processTransactionAsSubRequest(any(), any(), any(), any(), anyBoolean())).thenReturn(response0, response1);

		myInterceptor.setNextRanges(List.of(1, 3), List.of(0, 2));

		// Test
		TransactionPartitionProcessor.PartitionedTransactionResult<Bundle> result = mySvc.execute(request);

		// Verify
		verify(myTransactionProcessor, times(2)).processTransactionAsSubRequest(any(), any(), any(), any(), anyBoolean());

		TransactionUtil.TransactionResponse parsedResponse = TransactionUtil.parseTransactionResponse(myFhirContext, request, result.getResponseBundle());
		assertThat(parsedResponse.getStorageOutcomes().get(0).getTargetId().getValue()).isEqualTo("Observation/OBS-0");
		assertThat(parsedResponse.getStorageOutcomes().get(1).getTargetId().getValue()).isEqualTo("Patient/PAT-0");
		assertThat(parsedResponse.getStorageOutcomes().get(2).getTargetId().getValue()).isEqualTo("Observation/OBS-1");
		assertThat(parsedResponse.getStorageOutcomes().get(3).getTargetId().getValue()).isEqualTo("Patient/PAT-1");

		List<List<IBase>> entriesPerSubBundle = result.getResponseEntriesPerSubBundle();
		assertThat(entriesPerSubBundle).hasSize(2);
		assertResponseLocations(entriesPerSubBundle.get(0), "Patient/PAT-0", "Patient/PAT-1");
		assertResponseLocations(entriesPerSubBundle.get(1), "Observation/OBS-0", "Observation/OBS-1");
	}

	@Test
	public void testEmptyPartitionsIgnored() {
		// Setup
		Bundle request = createRequest_Obs0_Patient0_Obs1_Patient1();

		Bundle response0 = new Bundle();
		response0.setType(Bundle.BundleType.TRANSACTIONRESPONSE);
		addResponseEntry(response0, "201 Created", "Patient/PAT-0", StorageResponseCodeEnum.SUCCESSFUL_CREATE);
		addResponseEntry(response0, "201 Created", "Observation/OBS-0", StorageResponseCodeEnum.SUCCESSFUL_CREATE);
		addResponseEntry(response0, "201 Created", "Patient/PAT-1", StorageResponseCodeEnum.SUCCESSFUL_CREATE);
		addResponseEntry(response0, "201 Created", "Observation/OBS-1", StorageResponseCodeEnum.SUCCESSFUL_CREATE);
		when(myTransactionProcessor.processTransactionAsSubRequest(any(), any(), any(), any(), anyBoolean())).thenReturn(response0);

		myInterceptor.setNextRanges(List.of(), List.of(), List.of(0, 1, 2, 3), List.of());

		// Test
		TransactionPartitionProcessor.PartitionedTransactionResult<Bundle> result = mySvc.execute(request);

		// Verify
		verify(myTransactionProcessor, times(1)).processTransactionAsSubRequest(any(), any(), myBundleCaptor.capture(), any(), anyBoolean());
		assertThat(myBundleCaptor.getAllValues().get(0).getEntry()).hasSize(4);
		assertThat(result.getResponseBundle().getEntry()).hasSize(4);
		assertThat(result.getResponseEntriesPerSubBundle()).hasSize(1);
		assertThat(result.getResponseEntriesPerSubBundle().get(0)).hasSize(4);
	}

	@Test
	public void testInvalidRanges_OverlappingEntry() {
		// Setup
		Bundle request = createRequest_Obs0_Patient0_Obs1_Patient1();

		// Missing entry index 1
		myInterceptor.setNextRanges(List.of(0, 2), List.of(0, 1, 2, 3));

		// Test / Verify
		assertThatThrownBy(() -> mySvc.execute(request))
			.hasMessageContaining("Interceptor for Pointcut STORAGE_TRANSACTION_PRE_PARTITION must not return Bundles containing duplicates or entries which were not present in the original Bundle");
	}

	@Test
	public void testInvalidRanges_MissingEntry() {
		// Setup
		Bundle request = createRequest_Obs0_Patient0_Obs1_Patient1();

		// Missing entry index 1
		myInterceptor.setNextRanges(List.of(0, 1), List.of(3));

		// Test / Verify
		assertThatThrownBy(() -> mySvc.execute(request))
			.hasMessageContaining("Interceptor for Pointcut STORAGE_TRANSACTION_PRE_PARTITION must include all entries from the original Bundle in the partitioned Bundles");
	}

	@Test
	public void testInvalidRanges_EntryAddedWhichWasNotInOriginalBundle() {
		// Setup
		Bundle request = createRequest_Obs0_Patient0_Obs1_Patient1();

		// Missing entry index 1
		myInterceptor.setNextRanges(List.of(0, 1), List.of(-1, 2, 3));

		// Test / Verify
		assertThatThrownBy(() -> mySvc.execute(request))
			.hasMessageContaining("Interceptor for Pointcut STORAGE_TRANSACTION_PRE_PARTITION must not return Bundles containing duplicates or entries which were not present in the original Bundle");
	}

	@Test
	void testPartialFailure_secondSubBundleFails_exceptionContainsSucceededPartitionEntries() {
		// Setup
		Bundle request = createRequest_Obs0_Patient0_Obs1_Patient1();

		Bundle response0 = new Bundle();
		response0.setType(Bundle.BundleType.TRANSACTIONRESPONSE);
		addResponseEntry(response0, "201 Created", "Patient/PAT-0", StorageResponseCodeEnum.SUCCESSFUL_CREATE);
		addResponseEntry(response0, "201 Created", "Patient/PAT-1", StorageResponseCodeEnum.SUCCESSFUL_CREATE);
		when(myTransactionProcessor.processTransactionAsSubRequest(any(), any(), any(), any(), anyBoolean()))
				.thenReturn(response0)
				.thenThrow(new InternalErrorException("Simulated failure on second partition"));

		myInterceptor.setNextRanges(List.of(1, 3), List.of(0, 2));

		// Test / Verify
		assertThatThrownBy(() -> mySvc.execute(request))
				.isInstanceOf(PartitionedTransactionPartialFailureException.class)
				.satisfies(ex -> {
					PartitionedTransactionPartialFailureException partialEx =
							(PartitionedTransactionPartialFailureException) ex;
					List<List<IBase>> committedPerSubBundle = partialEx.getCommittedResponseEntriesPerSubBundle();

					assertThat(committedPerSubBundle).hasSize(1);
					assertResponseLocations(committedPerSubBundle.get(0), "Patient/PAT-0", "Patient/PAT-1");
				})
				.hasCauseInstanceOf(InternalErrorException.class)
				.hasRootCauseMessage("Simulated failure on second partition");
	}

	@Test
	void testPartialFailure_thirdSubBundleFails_exceptionContainsFirstTwoPartitionEntries() {
		BundleBuilder requestBuilder = new BundleBuilder(myFhirContext);
		requestBuilder.addTransactionUpdateEntry(buildObservation(withId("OBS-0"), withSubject("Patient/PAT-0"), withStatus("final")));
		requestBuilder.addTransactionUpdateEntry(buildPatient(withId("PAT-0")));
		requestBuilder.addTransactionUpdateEntry(buildObservation(withId("OBS-1"), withSubject("Patient/PAT-1"), withStatus("final")));
		requestBuilder.addTransactionUpdateEntry(buildPatient(withId("PAT-1")));
		requestBuilder.addTransactionUpdateEntry(buildEncounter(withId("ENC-0"), withStatus("planned")));
		Bundle request = requestBuilder.getBundleTyped();

		Bundle response0 = new Bundle();
		response0.setType(Bundle.BundleType.TRANSACTIONRESPONSE);
		addResponseEntry(response0, "201 Created", "Patient/PAT-0", StorageResponseCodeEnum.SUCCESSFUL_CREATE);
		addResponseEntry(response0, "201 Created", "Patient/PAT-1", StorageResponseCodeEnum.SUCCESSFUL_CREATE);

		Bundle response1 = new Bundle();
		response1.setType(Bundle.BundleType.TRANSACTIONRESPONSE);
		addResponseEntry(response1, "201 Created", "Observation/OBS-0", StorageResponseCodeEnum.SUCCESSFUL_CREATE);
		addResponseEntry(response1, "201 Created", "Observation/OBS-1", StorageResponseCodeEnum.SUCCESSFUL_CREATE);

		when(myTransactionProcessor.processTransactionAsSubRequest(any(), any(), any(), any(), anyBoolean()))
				.thenReturn(response0, response1)
				.thenThrow(new InternalErrorException("Simulated failure on third partition"));

		myInterceptor.setNextRanges(List.of(1, 3), List.of(0, 2), List.of(4));

		// Test / Verify
		assertThatThrownBy(() -> mySvc.execute(request))
				.isInstanceOf(PartitionedTransactionPartialFailureException.class)
				.satisfies(ex -> {
					PartitionedTransactionPartialFailureException partialEx =
							(PartitionedTransactionPartialFailureException) ex;
					List<List<IBase>> committedPerSubBundle = partialEx.getCommittedResponseEntriesPerSubBundle();

					assertThat(committedPerSubBundle).hasSize(2);
					assertResponseLocations(committedPerSubBundle.get(0), "Patient/PAT-0", "Patient/PAT-1");
					assertResponseLocations(committedPerSubBundle.get(1), "Observation/OBS-0", "Observation/OBS-1");
				})
				.hasCauseInstanceOf(InternalErrorException.class)
				.hasRootCauseMessage("Simulated failure on third partition");
	}

	@Test
	void testPartialFailure_firstSubBundleFails_originalExceptionPropagates() {
		// Setup
		Bundle request = createRequest_Obs0_Patient0_Obs1_Patient1();

		when(myTransactionProcessor.processTransactionAsSubRequest(any(), any(), any(), any(), anyBoolean()))
				.thenThrow(new InternalErrorException("Simulated failure on first partition"));

		myInterceptor.setNextRanges(List.of(1, 3), List.of(0, 2));

		assertThatThrownBy(() -> mySvc.execute(request))
				.isInstanceOf(InternalErrorException.class)
				.isNotInstanceOf(PartitionedTransactionPartialFailureException.class)
				.hasMessageContaining("Simulated failure on first partition");
	}

	private Bundle createRequest_Obs0_Patient0_Obs1_Patient1() {
		BundleBuilder requestBuilder = new BundleBuilder(myFhirContext);
		requestBuilder.addTransactionUpdateEntry(buildObservation(withId("OBS-0"), withSubject("Patient/PAT-0"), withStatus("final")));
		requestBuilder.addTransactionUpdateEntry(buildPatient(withId("PAT-0")));
		requestBuilder.addTransactionUpdateEntry(buildObservation(withId("OBS-1"), withSubject("Patient/PAT-1"), withStatus("final")));
		requestBuilder.addTransactionUpdateEntry(buildPatient(withId("PAT-1")));
		return requestBuilder.getBundleTyped();
	}

	@Override
	public IIdType doCreateResource(IBaseResource theResource) {
		throw new UnsupportedOperationException();
	}

	@Override
	public IIdType doUpdateResource(IBaseResource theResource) {
		throw new UnsupportedOperationException();
	}

	@Override
	public FhirContext getFhirContext() {
		return myFhirContext;
	}

	private static void assertResponseLocations(List<IBase> theResponseEntriesForPartition, String... theExpectedLocations) {
		assertThat(theResponseEntriesForPartition)
				.extracting(e -> ((Bundle.BundleEntryComponent) e).getResponse().getLocation())
				.containsExactly(theExpectedLocations);
	}

	private static void addResponseEntry(Bundle theResponseBundle, String theStatusLine, String theLocation, StorageResponseCodeEnum theStorageOutcome) {
		OperationOutcome outcome = new OperationOutcome();
		outcome.addIssue()
			.setSeverity(OperationOutcome.IssueSeverity.INFORMATION)
			.setCode(OperationOutcome.IssueType.INFORMATIONAL)
			.setDetails(new CodeableConcept(new Coding(theStorageOutcome.getSystem(), theStorageOutcome.getCode(), theStorageOutcome.getDisplay())));
		theResponseBundle
			.addEntry()
			.setResponse(new Bundle.BundleEntryResponseComponent()
				.setStatus(theStatusLine)
				.setLocation(theLocation)
				.setOutcome(outcome));
	}
}
