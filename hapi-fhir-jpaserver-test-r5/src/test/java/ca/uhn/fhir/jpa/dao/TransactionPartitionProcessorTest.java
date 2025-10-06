package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.executor.InterceptorService;
import ca.uhn.fhir.jpa.dao.r5.FhirSystemDaoTransactionPartitionR5Test;
import ca.uhn.fhir.model.api.StorageResponseCodeEnum;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.test.utilities.ITestDataBuilder;
import ca.uhn.fhir.util.BundleBuilder;
import org.hl7.fhir.instance.model.api.IBaseBundle;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransactionPartitionProcessorTest implements ITestDataBuilder {

	private final FhirContext myFhirContext = FhirContext.forR5Cached();
	private final InterceptorService myInterceptorBroadcaster = new InterceptorService();
	private final FhirSystemDaoTransactionPartitionR5Test.MyTransactionPrePartitionInterceptor myInterceptor = new FhirSystemDaoTransactionPartitionR5Test.MyTransactionPrePartitionInterceptor();
	@Mock
	private TransactionProcessor myTransactionProcessor;
	@Captor
	private ArgumentCaptor<Bundle> myBundleCaptor;
	private TransactionPartitionProcessor<Bundle> mySvc;

	@BeforeEach
	public void beforeEach() {
		myInterceptorBroadcaster.registerInterceptor(myInterceptor);
		mySvc = new TransactionPartitionProcessor<>(myTransactionProcessor, myFhirContext, newSrd(), false, myInterceptorBroadcaster, "transaction");
	}

	@Test
	public void testTwoPartitions() {
		// Setup
		Bundle request = createRequest_Obs0_Patient0_Obs1_Patient1();

		Bundle response0 = new Bundle();
		response0.setType(Bundle.BundleType.TRANSACTIONRESPONSE);
		addResponseEntry(response0, "201", "Patient/PAT-0", StorageResponseCodeEnum.SUCCESSFUL_CREATE);
		addResponseEntry(response0, "201", "Patient/PAT-1", StorageResponseCodeEnum.SUCCESSFUL_CREATE);
		Bundle response1 = new Bundle();
		response1.setType(Bundle.BundleType.TRANSACTIONRESPONSE);
		addResponseEntry(response1, "201", "Observation/OBS-0", StorageResponseCodeEnum.SUCCESSFUL_CREATE);
		addResponseEntry(response1, "201", "Observation/OBS-1", StorageResponseCodeEnum.SUCCESSFUL_CREATE);
		when(myTransactionProcessor.processTransactionAsSubRequest(any(), any(), any(), any(), anyBoolean())).thenReturn(response0, response1);

		myInterceptor.setNextRanges(List.of(1, 3), List.of(0, 2));

		// Test
		Bundle response = mySvc.execute(request);

		// Verify
		verify(myTransactionProcessor, times(2)).processTransactionAsSubRequest(any(), any(), any(), any(), anyBoolean());

		TransactionUtil.TransactionResponse parsedResponse = TransactionUtil.parseTransactionResponse(myFhirContext, request, response);
		assertEquals("Observation/OBS-0", parsedResponse.getStorageOutcomes().get(0).getTargetId().getValue());
		assertEquals("Patient/PAT-0", parsedResponse.getStorageOutcomes().get(1).getTargetId().getValue());
		assertEquals("Observation/OBS-1", parsedResponse.getStorageOutcomes().get(2).getTargetId().getValue());
		assertEquals("Patient/PAT-1", parsedResponse.getStorageOutcomes().get(3).getTargetId().getValue());
	}

	@Test
	public void testEmptyPartitionsIgnored() {
		// Setup
		Bundle request = createRequest_Obs0_Patient0_Obs1_Patient1();

		Bundle response0 = new Bundle();
		response0.setType(Bundle.BundleType.TRANSACTIONRESPONSE);
		addResponseEntry(response0, "201", "Patient/PAT-0", StorageResponseCodeEnum.SUCCESSFUL_CREATE);
		addResponseEntry(response0, "201", "Observation/OBS-0", StorageResponseCodeEnum.SUCCESSFUL_CREATE);
		addResponseEntry(response0, "201", "Patient/PAT-1", StorageResponseCodeEnum.SUCCESSFUL_CREATE);
		addResponseEntry(response0, "201", "Observation/OBS-1", StorageResponseCodeEnum.SUCCESSFUL_CREATE);
		when(myTransactionProcessor.processTransactionAsSubRequest(any(), any(), any(), any(), anyBoolean())).thenReturn(response0);

		myInterceptor.setNextRanges(List.of(), List.of(), List.of(0, 1, 2, 3), List.of());

		// Test
		Bundle response = mySvc.execute(request);

		// Verify
		verify(myTransactionProcessor, times(1)).processTransactionAsSubRequest(any(), any(), myBundleCaptor.capture(), any(), anyBoolean());
		assertEquals(4, myBundleCaptor.getAllValues().get(0).getEntry().size());
	}

	@Test
	public void testInvalidRanges_OverlappingEntry() {
		// Setup
		Bundle request = createRequest_Obs0_Patient0_Obs1_Patient1();

		// Missing entry index 1
		myInterceptor.setNextRanges(List.of(0, 2), List.of(0, 1, 2, 3));

		// Test / Verify
		assertThatThrownBy(()->mySvc.execute(request))
			.hasMessageContaining("Interceptor for Pointcut STORAGE_TRANSACTION_PRE_PARTITION must not return Bundles containing duplicates or entries which were not present in the original Bundle");
	}

	@Test
	public void testInvalidRanges_MissingEntry() {
		// Setup
		Bundle request = createRequest_Obs0_Patient0_Obs1_Patient1();

		// Missing entry index 1
		myInterceptor.setNextRanges(List.of(0, 1), List.of(3));

		// Test / Verify
		assertThatThrownBy(()->mySvc.execute(request))
			.hasMessageContaining("Interceptor for Pointcut STORAGE_TRANSACTION_PRE_PARTITION must include all entries from the original Bundle in the partitioned Bundles");
	}

	@Test
	public void testInvalidRanges_EntryAddedWhichWasNotInOriginalBundle() {
		// Setup
		Bundle request = createRequest_Obs0_Patient0_Obs1_Patient1();

		// Missing entry index 1
		myInterceptor.setNextRanges(List.of(0, 1), List.of(-1, 2, 3));

		// Test / Verify
		assertThatThrownBy(()->mySvc.execute(request))
			.hasMessageContaining("Interceptor for Pointcut STORAGE_TRANSACTION_PRE_PARTITION must not return Bundles containing duplicates or entries which were not present in the original Bundle");
	}



	private Bundle createRequest_Obs0_Patient0_Obs1_Patient1() {
		BundleBuilder requestBuilder = new BundleBuilder(myFhirContext);
		requestBuilder.addTransactionUpdateEntry(buildObservation(withId("OBS-0"), withSubject("Patient/PAT-0"), withStatus("final")));
		requestBuilder.addTransactionUpdateEntry(buildPatient(withId("PAT-0")));
		requestBuilder.addTransactionUpdateEntry(buildObservation(withId("OBS-1"), withSubject("Patient/PAT-1"), withStatus("final")));
		requestBuilder.addTransactionUpdateEntry(buildPatient(withId("PAT-1")));
		Bundle request = requestBuilder.getBundleTyped();
		return request;
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
