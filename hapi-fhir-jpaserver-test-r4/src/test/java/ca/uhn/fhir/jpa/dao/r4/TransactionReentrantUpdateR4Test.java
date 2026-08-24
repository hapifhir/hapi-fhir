package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.model.entity.ResourceHistoryTable;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.Flag;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Reference;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Reproduces version corruption that occurs when a transaction Bundle updates a Patient at the
 * same time as an interceptor, firing on a different resource in that same transaction, calls
 * {@code dao.update()} on that Patient re-entrantly.
 * <p>
 * Both writes commit, and {@code HFJ_RESOURCE.RES_VER} is left pointing at a version that is
 * not the highest one present in {@code HFJ_RES_VER}.
 */
public class TransactionReentrantUpdateR4Test extends BaseJpaR4Test {

	private static final Logger ourLog = LoggerFactory.getLogger(TransactionReentrantUpdateR4Test.class);

	private static final String PATIENT_ID = "Patient/reentrant-update";
	private static final String FLAG_IDENTIFIER_SYSTEM = "http://tempuri.org";
	private static final String FLAG_IDENTIFIER_VALUE = "f1";

	/**
	 * Reduced form of the reported interceptor. The original stashes the Bundle on
	 * STORAGE_TRANSACTION_PROCESSING so it can resolve a urn:uuid Flag.subject back to a Patient
	 * id; the Flag below uses a literal reference instead, so only the re-entrant update itself
	 * is left.
	 */
	@Interceptor
	private static class ReentrantFlagInterceptor {

		private final IFhirResourceDao<Patient> myPatientDao;
		private int myInvocationCount = 0;

		ReentrantFlagInterceptor(IFhirResourceDao<Patient> thePatientDao) {
			myPatientDao = thePatientDao;
		}

		@Hook(Pointcut.STORAGE_PRESTORAGE_RESOURCE_CREATED)
		public void resourceCreated(IBaseResource theResource, RequestDetails theRequestDetails) {
			if (!(theResource instanceof Flag flag)) {
				return;
			}

			IdType targetId = new IdType(flag.getSubject().getReference()).toUnqualifiedVersionless();
			ourLog.info("Interceptor updating {} from within the Flag pre-storage hook", targetId);

			Patient patient = myPatientDao.read(targetId, theRequestDetails);
			patient.setDeceased(new DateTimeType(new Date()));
			myPatientDao.update(patient, theRequestDetails);

			myInvocationCount++;
		}

		int getInvocationCount() {
			return myInvocationCount;
		}
	}

	@Test
	public void testTransactionUpdate_whenInterceptorUpdatesSameResource_currentVersionMatchesHistory() {
		// Setup - Patient v1
		Patient patient = new Patient();
		patient.setId(PATIENT_ID);
		patient.setActive(true);
		myPatientDao.update(patient, new SystemRequestDetails());

		ReentrantFlagInterceptor interceptor = new ReentrantFlagInterceptor(myPatientDao);
		registerInterceptor(interceptor);

		// Execute - the Patient PUT takes v1 to v2, and the Flag create fires the interceptor,
		// which updates the same Patient again from inside the transaction
		Bundle response = mySystemDao.transaction(new SystemRequestDetails(), buildTransaction());

		// Verify
		assertEquals(1, interceptor.getInvocationCount(),
			"Interceptor never fired, so this test proves nothing");

		assertPatientVersionsAreConsistent(response);
	}

	/**
	 * Control for the test above: the same Bundle with no interceptor registered. This one passes,
	 * which places the corruption on the re-entrant update rather than on the shape of the Bundle.
	 */
	@Test
	public void testTransactionUpdate_withNoInterceptor_currentVersionMatchesHistory() {
		// Setup - Patient v1
		Patient patient = new Patient();
		patient.setId(PATIENT_ID);
		patient.setActive(true);
		myPatientDao.update(patient, new SystemRequestDetails());

		// Execute
		Bundle response = mySystemDao.transaction(new SystemRequestDetails(), buildTransaction());

		// Verify
		assertPatientVersionsAreConsistent(response);
	}

	private void assertPatientVersionsAreConsistent(Bundle theTransactionResponse) {
		String responseLocation =
			theTransactionResponse.getEntry().get(0).getResponse().getLocation();
		long responseVersion = Long.parseLong(new IdType(responseLocation).getVersionIdPart());

		runInTransaction(() -> {
			ResourceTable currentRow = myResourceTableDao.findAll().stream()
				.filter(t -> "Patient".equals(t.getResourceType()))
				.findFirst()
				.orElseThrow(() -> new AssertionError("No Patient row in HFJ_RESOURCE"));

			List<Long> historyVersions = myResourceHistoryTableDao
				.findAllVersionsForResourceIdInOrder(currentRow.getResourceId().toFk())
				.stream()
				.map(ResourceHistoryTable::getVersion)
				.toList();

			long currentVersion = currentRow.getVersion();
			long highestHistoryVersion = historyVersions.stream().mapToLong(Long::longValue).max().orElse(-1L);

			String state = String.format(
				"HFJ_RESOURCE.RES_VER=%d, HFJ_RES_VER rows=%s, transaction response advertised v%d",
				currentVersion,
				historyVersions.stream().map(String::valueOf).collect(Collectors.joining(", ", "[", "]")),
				responseVersion);
			ourLog.info("Final state: {}", state);

			assertTrue(currentVersion >= 2, "Expected the Patient to have been updated - " + state);

			assertEquals(highestHistoryVersion, currentVersion,
				"HFJ_RESOURCE.RES_VER does not point at the newest HFJ_RES_VER row - " + state);

			List<Long> expectedVersions = LongStream.rangeClosed(1, currentVersion).boxed().toList();
			assertEquals(expectedVersions, historyVersions,
				"Version history is not a gapless 1..N sequence - " + state);

			assertEquals(currentVersion, responseVersion,
				"Transaction response advertises a version the database does not hold - " + state);
		});
	}

	/**
	 * Mirrors the reported Bundle: a versioned PUT of the Patient alongside a conditional PUT of
	 * a Flag that references it.
	 */
	private Bundle buildTransaction() {
		Patient patient = new Patient();
		patient.setId(PATIENT_ID);
		patient.setGender(Enumerations.AdministrativeGender.MALE);

		Flag flag = new Flag();
		flag.addIdentifier().setSystem(FLAG_IDENTIFIER_SYSTEM).setValue(FLAG_IDENTIFIER_VALUE);
		flag.setSubject(new Reference(PATIENT_ID));

		Bundle bundle = new Bundle();
		bundle.setType(Bundle.BundleType.TRANSACTION);

		bundle.addEntry()
			.setResource(patient)
			.getRequest()
			.setMethod(Bundle.HTTPVerb.PUT)
			.setUrl(PATIENT_ID)
			.setIfMatch("W/\"1\"");

		bundle.addEntry()
			.setResource(flag)
			.getRequest()
			.setMethod(Bundle.HTTPVerb.PUT)
			.setUrl("Flag?identifier=" + FLAG_IDENTIFIER_SYSTEM + "|" + FLAG_IDENTIFIER_VALUE);

		return bundle;
	}
}
