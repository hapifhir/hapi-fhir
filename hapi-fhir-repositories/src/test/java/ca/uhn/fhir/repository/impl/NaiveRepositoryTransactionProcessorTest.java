package ca.uhn.fhir.repository.impl;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.repository.IRepository;
import ca.uhn.fhir.repository.impl.memory.InMemoryFhirRepository;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.util.BundleBuilder;
import ca.uhn.fhir.util.BundleUtil;
import ca.uhn.fhir.util.bundle.BundleResponseEntryParts;
import ca.uhn.fhir.util.bundle.PartsConverter;
import ca.uhn.test.util.LogbackTestExtension;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoSettings;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@MockitoSettings
class NaiveRepositoryTransactionProcessorTest {

	FhirContext myFhirContext = FhirContext.forR4();
	final PartsConverter<BundleResponseEntryParts> myConverter = BundleResponseEntryParts.getConverter(myFhirContext);
	BundleBuilder myBundleBuilder = new BundleBuilder(myFhirContext);
	@RegisterExtension
	LogbackTestExtension myLogbackTestExtension = new LogbackTestExtension(NaiveRepositoryTransactionProcessor.ourLog);

	InMemoryFhirRepository myBaseRepository = InMemoryFhirRepository.emptyRepository(myFhirContext);
	@Mock
	IRepository myMockRepository; // used for testing things not supported by the InMemoryFhirRepository

	NaiveRepositoryTransactionProcessor myTransactionProcessor = new NaiveRepositoryTransactionProcessor(myBaseRepository);


	@Test
	void testEmptyTransactionProcessor() {
		// given
		myBundleBuilder.setType("transaction");
		IBaseBundle transaction = myBundleBuilder.getBundle();

		// when
		IBaseBundle response = myTransactionProcessor.processTransaction(transaction);

	    // then
		assertNotNull(response);
		assertEquals("transaction-response", BundleUtil.getBundleType(myFhirContext, response));
		assertThat(myLogbackTestExtension.getLogMessages()).anySatisfy(s -> assertThat(s).contains("empty bundle"));
	}

	@Test
	void testCreateEntry() {
		// given
		Patient p = new Patient();
		myBundleBuilder.addTransactionCreateEntry(p);
		IBaseBundle transaction = myBundleBuilder.getBundle();

		// when
		IBaseBundle response = myTransactionProcessor.processTransaction(transaction);

		// then
		assertNotNull(response);
		assertEquals("transaction-response", BundleUtil.getBundleType(myFhirContext, response));
		List<BundleResponseEntryParts> responseEntries = getBundleResponseEntryParts(response);
		assertEquals(1, responseEntries.size());
		BundleResponseEntryParts responseEntry0 = responseEntries.get(0);

		IIdType id0 = responseEntry0.resource().getIdElement();
		assertThat(id0).as("Id returned by create").isNotNull();
		assertThat(myBaseRepository.read(Patient.class, id0)).isNotNull();

		assertThat(responseEntry0.resource()).isInstanceOf(Patient.class);
		assertThat(responseEntry0.responseStatus()).startsWith("201");
	}

	@Test
	void testConditionalCreateUnsupported() {
	    // given
		myBundleBuilder.addTransactionCreateEntry(new Patient()).conditional("Patient?identifier=12345");
		var bundle = myBundleBuilder.getBundle();

		assertThrows(UnprocessableEntityException.class, ()-> myTransactionProcessor.processTransaction(bundle));
	}

	@Test
	void testPutEntry() {
		// given
		Patient p = new Patient();
		p.setActive(false);
		IIdType patientId = myBaseRepository.create(p).getId();
		Patient updatePatient = new Patient();
		updatePatient.setActive(true);
		updatePatient.setId(patientId);

		myBundleBuilder.addTransactionUpdateEntry(updatePatient);

		IBaseBundle transaction = myBundleBuilder.getBundle();

		// when
		IBaseBundle response = myTransactionProcessor.processTransaction(transaction);

		// then
		assertThat(myBaseRepository.read(Patient.class, patientId).getActive()).isTrue();

		assertNotNull(response);
		assertEquals("transaction-response", BundleUtil.getBundleType(myFhirContext, response));
		List<BundleResponseEntryParts> responseEntries = getBundleResponseEntryParts(response);
		assertEquals(1, responseEntries.size());
		BundleResponseEntryParts responseEntry0 = responseEntries.get(0);
		assertThat(responseEntry0.resource()).isInstanceOf(Patient.class);
		assertThat(responseEntry0.responseStatus()).startsWith("200");

	}

	@Test
	void testConditionalPutUnsupported() {
		// given
		myBundleBuilder.addTransactionUpdateEntry(new Patient()).conditional("Patient?identifier=12345");
		var bundle = myBundleBuilder.getBundle();

		assertThrows(UnprocessableEntityException.class, ()-> myTransactionProcessor.processTransaction(bundle));
	}

	@Test
	void testDeleteEntry() {
		// given
		Patient p = new Patient();
		p.setActive(false);
		IIdType patientId = myBaseRepository.create(p).getId();

		myBundleBuilder.addTransactionDeleteEntry(patientId);

		IBaseBundle transaction = myBundleBuilder.getBundle();

		// when
		IBaseBundle response = myTransactionProcessor.processTransaction(transaction);

		// then
		assertThrows(ResourceNotFoundException.class, ()-> myBaseRepository.read(Patient.class, patientId), "resource deleted from repository");

		assertNotNull(response);
		assertEquals("transaction-response", BundleUtil.getBundleType(myFhirContext, response));
		List<BundleResponseEntryParts> responseEntries = getBundleResponseEntryParts(response);
		assertEquals(1, responseEntries.size());
		BundleResponseEntryParts responseEntry0 = responseEntries.get(0);
		assertThat(responseEntry0.responseStatus()).startsWith("20");

	}

	@Test
	void testPatchEntry() {
		withMockRepository();

		IIdType patientId = new IdType("Patient", "123");
		Parameters patchParameters = new Parameters();
		myBundleBuilder.addTransactionFhirPatchEntry(patientId, patchParameters);

		IBaseBundle transaction = myBundleBuilder.getBundle();

		Mockito.when(myMockRepository.patch(patientId, patchParameters)).thenReturn(new MethodOutcome(patientId));

		// when
		IBaseBundle response = myTransactionProcessor.processTransaction(transaction);

		// then
		List<BundleResponseEntryParts> responseEntries = getBundleResponseEntryParts(response);
		assertEquals(1, responseEntries.size());

		Mockito.verify(myMockRepository).patch(patientId, patchParameters);
	}

	private void withMockRepository() {
		Mockito.when(myMockRepository.fhirContext()).thenReturn(myFhirContext);
		myTransactionProcessor = new NaiveRepositoryTransactionProcessor(myMockRepository);
	}

	private List<BundleResponseEntryParts> getBundleResponseEntryParts(IBaseBundle response) {
		return BundleUtil.toListOfEntries(myFhirContext, response, myConverter);
	}

}
