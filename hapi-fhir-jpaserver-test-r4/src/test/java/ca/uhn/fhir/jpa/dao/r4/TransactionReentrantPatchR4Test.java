// Created by claude-opus-5
package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.model.entity.ResourceHistoryTable;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.Flag;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Reference;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * GL-8721 follow-up: re-entrant interceptor writes against a <b>PATCH</b> entry in a transaction.
 * <p>
 * Sibling of {@code TransactionReentrantUpdateR4Test}, which covers the same family of defects for
 * full-record {@code PUT} updates. PATCH is a separate shape and a separate code path, and the fix
 * that closed the {@code PUT} hole did not close this one.
 * </p>
 * <p>
 * <b>Two of the three cases below are expected to FAIL.</b> They are deliberate TDD Red, pinning
 * defects the ticket owner has not yet decided how to fix. They assert the desired behaviour, not the
 * current behaviour, so they will go green once GL-8721's PATCH gap is addressed. If you have arrived
 * here because CI is red, these are real defects and not broken tests - see the javadoc on each.
 * </p>
 * <p>
 * The mechanism they share: {@code BaseTransactionProcessor} handles a PATCH entry by calling
 * {@code dao.patchInTransaction(..., thePerformIndexing = false, ...)}. That applies the patch during
 * the first pass, producing a <em>finished document</em>, and defers the real write to the second
 * pass. Anything that changes the resource in between is therefore overwritten by a document computed
 * before it happened.
 * </p>
 */
public class TransactionReentrantPatchR4Test extends BaseJpaR4Test {

	private static final String PATIENT_ID = "Patient/reentrant-patch";
	private static final String REENTRANT_FAMILY_NAME = "REENTRANT";

	/**
	 * Writes the Patient from inside the write pass, by hooking the pointcut that
	 * {@code updateInternal} broadcasts for the resource it is about to write. Fires once, because the
	 * nested {@code dao.update()} broadcasts the same pointcut again.
	 */
	@Interceptor
	private static class ReentrantPatientUpdateInterceptor {

		private final IFhirResourceDao<Patient> myPatientDao;
		private int myInvocationCount = 0;

		ReentrantPatientUpdateInterceptor(IFhirResourceDao<Patient> thePatientDao) {
			myPatientDao = thePatientDao;
		}

		@Hook(Pointcut.STORAGE_PRESTORAGE_RESOURCE_UPDATED)
		public void resourcePreUpdate(RequestDetails theRequestDetails, IBaseResource theOldResource, IBaseResource theNewResource) {
			if (!(theNewResource instanceof Patient) || myInvocationCount > 0) {
				return;
			}
			myInvocationCount++;

			Patient patient = myPatientDao.read(new IdType(PATIENT_ID), theRequestDetails);
			patient.addName().setFamily(REENTRANT_FAMILY_NAME);
			myPatientDao.update(patient, theRequestDetails);
		}

		int getInvocationCount() {
			return myInvocationCount;
		}
	}

	/** Writes the Patient from a Flag create, i.e. while the first pass is still walking entries. */
	@Interceptor
	private static class FlagCreateInterceptor {

		private final IFhirResourceDao<Patient> myPatientDao;
		private int myInvocationCount = 0;

		FlagCreateInterceptor(IFhirResourceDao<Patient> thePatientDao) {
			myPatientDao = thePatientDao;
		}

		@Hook(Pointcut.STORAGE_PRESTORAGE_RESOURCE_CREATED)
		public void resourceCreated(IBaseResource theResource, RequestDetails theRequestDetails) {
			if (!(theResource instanceof Flag)) {
				return;
			}
			myInvocationCount++;

			Patient patient = myPatientDao.read(new IdType(PATIENT_ID), theRequestDetails);
			patient.addName().setFamily(REENTRANT_FAMILY_NAME);
			myPatientDao.update(patient, theRequestDetails);
		}

		int getInvocationCount() {
			return myInvocationCount;
		}
	}

	/**
	 * Test A - <b>EXPECTED TO FAIL</b> until GL-8721's PATCH gap is fixed. Pins a silently lost write.
	 * <p>
	 * A transaction patches {@code Patient.active} while an interceptor, firing from inside the write
	 * pass, adds a name to that same Patient. Both changes touch different fields, so both should
	 * survive. What actually happens is that the interceptor's write lands as v2 and the patch's
	 * document - computed during the first pass, from content that predates the interceptor - is
	 * written over it as v3. The name is gone from the final resource.
	 * </p>
	 * <p>
	 * <b>No {@code If-Match} is sent here on purpose.</b> This defect is a plain lost update and has
	 * nothing to do with preconditions; sending one would only mask it behind the separate conflict
	 * that {@link #testTransactionPatchWithIfMatch_whenInterceptorUpdatesSameResource_throwsVersionConflict()}
	 * covers.
	 * </p>
	 * <p>
	 * Note that the storage invariant is <em>not</em> the problem here and is asserted first to make
	 * that explicit: the version pointer and history rows are consistent. The data is simply lost.
	 * </p>
	 */
	@Disabled("GL-8721 - parked: pins a real, confirmed defect (the interceptor's write is silently lost). "
		+ "Fixing it means applying the patch at write time instead of writing a document computed in pass 1, "
		+ "which was judged too large for this MR. Re-enable when that work is scheduled.")
	@Test
	void testTransactionPatch_whenInterceptorUpdatesSameResource_bothChangesSurvive() {
		// Setup - Patient v1, active=true, no name
		createPatientVersionOne();

		ReentrantPatientUpdateInterceptor interceptor = new ReentrantPatientUpdateInterceptor(myPatientDao);
		registerInterceptor(interceptor);

		// Execute
		mySystemDao.transaction(new SystemRequestDetails(), buildPatchTransaction(false));

		// Verify
		assertThat(interceptor.getInvocationCount())
			.as("Interceptor never fired, so this test proves nothing")
			.isEqualTo(1);

		assertPatientStorageVersionsAreConsistent();

		String versionDump = describeVersionHistory();
		Patient finalPatient = myPatientDao.read(new IdType(PATIENT_ID), new SystemRequestDetails());

		assertThat(finalPatient.getActive())
			.as("The patch's own change should have been applied - %s", versionDump)
			.isFalse();

		assertThat(finalPatient.getName())
			.as("The interceptor's write was overwritten by a patch document computed before it ran, "
				+ "so its change was silently lost - %s", versionDump)
			.extracting(HumanName::getFamily)
			.contains(REENTRANT_FAMILY_NAME);
	}

	/**
	 * Test B - <b>EXPECTED TO FAIL</b> until GL-8721's PATCH gap is fixed. Pins a broken
	 * {@code If-Match} guarantee.
	 * <p>
	 * The client patches with {@code If-Match: W/"1"}, meaning "apply this to v1 and only v1". The
	 * precondition is checked during the first pass and passes, but the interceptor then moves the
	 * Patient to v2 before the real write happens, and nothing re-checks it. The transaction succeeds
	 * and the client is never told that the resource it guarded against had already moved.
	 * </p>
	 * <p>
	 * This is the exact defect that was closed for {@code PUT} entries by re-checking the precondition
	 * at the point of the write; the PATCH path was left out of that change. Only the exception type is
	 * asserted, deliberately - the first-pass check currently reports {@code HAPI-0974} and a fix at the
	 * write may well use a different code, which should not by itself decide this test.
	 * </p>
	 */
	@Disabled("GL-8721 - parked: pins a real, confirmed defect (If-Match is not re-checked at the real write, "
		+ "so the transaction succeeds against a version the client did not authorise). The PUT equivalent was fixed "
		+ "in this MR; the PATCH equivalent was parked with its sibling above. Re-enable when that work is scheduled.")
	@Test
	void testTransactionPatchWithIfMatch_whenInterceptorUpdatesSameResource_throwsVersionConflict() {
		// Setup - Patient v1
		createPatientVersionOne();

		ReentrantPatientUpdateInterceptor interceptor = new ReentrantPatientUpdateInterceptor(myPatientDao);
		registerInterceptor(interceptor);

		// Execute + Verify
		assertThatThrownBy(() -> mySystemDao.transaction(new SystemRequestDetails(), buildPatchTransaction(true)))
			.as("The interceptor moved the Patient past v1 before the patch was written, so If-Match "
				+ "W/\"1\" can no longer be honoured and the transaction must be refused")
			.isInstanceOf(ResourceVersionConflictException.class);

		assertThat(interceptor.getInvocationCount())
			.as("Interceptor never fired, so this test proves nothing")
			.isEqualTo(1);

		runInTransaction(() -> {
			ResourceTable currentRow = findPatientRow();
			assertThat(currentRow.getVersion())
				.as("The whole transaction, including the interceptor's write, should have rolled back")
				.isEqualTo(1L);
			assertThat(historyVersionsOf(currentRow)).containsExactly(1L);
		});
	}

	/**
	 * Ordering control - <b>passes today, and must stay passing.</b> Documents the boundary of the two
	 * defects above.
	 * <p>
	 * Here the interceptor writes the Patient from a Flag create rather than from inside the write
	 * pass. Entries are sorted by verb (DELETE=1, POST=2, PUT=3, PATCH=4), so a PATCH entry is the last
	 * write worked out in the first pass. An interceptor firing on any earlier entry has therefore
	 * already run by the time the patch is computed, and the first pass sees the moved version and
	 * rejects the Bundle with {@code HAPI-0974}.
	 * </p>
	 * <p>
	 * That is why Test A and Test B need an interceptor on
	 * {@code STORAGE_PRESTORAGE_RESOURCE_UPDATED} specifically: it is broadcast from inside the write
	 * pass itself, which is the only way to move the resource after the patch document has been built.
	 * A fix for those two must not turn this case into a different error.
	 * </p>
	 */
	@Test
	void testTransactionPatchWithIfMatch_whenPostEntryInterceptorUpdatesSameResource_throwsVersionConflict() {
		// Setup - Patient v1
		createPatientVersionOne();

		FlagCreateInterceptor interceptor = new FlagCreateInterceptor(myPatientDao);
		registerInterceptor(interceptor);

		// Execute + Verify
		assertThatThrownBy(() -> mySystemDao.transaction(new SystemRequestDetails(), buildPostFlagPlusPatchTransaction()))
			.as("The interceptor fires while the first pass is still walking entries, so the patch is "
				+ "never even computed against a stale version")
			.isInstanceOf(ResourceVersionConflictException.class)
			.hasMessageContaining(Msg.code(974));

		assertThat(interceptor.getInvocationCount())
			.as("Interceptor never fired, so this test proves nothing")
			.isEqualTo(1);
	}

	private void createPatientVersionOne() {
		Patient patient = new Patient();
		patient.setId(PATIENT_ID);
		patient.setActive(true);
		myPatientDao.update(patient, new SystemRequestDetails());
	}

	private Bundle buildPatchTransaction(boolean theWithIfMatch) {
		Bundle bundle = new Bundle();
		bundle.setType(Bundle.BundleType.TRANSACTION);
		Bundle.BundleEntryRequestComponent request = bundle.addEntry()
			.setResource(buildActiveFalsePatch())
			.getRequest()
			.setMethod(Bundle.HTTPVerb.PATCH)
			.setUrl(PATIENT_ID);
		if (theWithIfMatch) {
			request.setIfMatch("W/\"1\"");
		}
		return bundle;
	}

	private Bundle buildPostFlagPlusPatchTransaction() {
		Bundle bundle = new Bundle();
		bundle.setType(Bundle.BundleType.TRANSACTION);

		Flag flag = new Flag();
		flag.setSubject(new Reference(PATIENT_ID));
		bundle.addEntry().setResource(flag).getRequest().setMethod(Bundle.HTTPVerb.POST).setUrl("Flag");

		bundle.addEntry()
			.setResource(buildActiveFalsePatch())
			.getRequest()
			.setMethod(Bundle.HTTPVerb.PATCH)
			.setUrl(PATIENT_ID)
			.setIfMatch("W/\"1\"");

		return bundle;
	}

	private static Parameters buildActiveFalsePatch() {
		Parameters patch = new Parameters();
		Parameters.ParametersParameterComponent operation = patch.addParameter().setName("operation");
		operation.addPart().setName("type").setValue(new CodeType("replace"));
		operation.addPart().setName("path").setValue(new CodeType("Patient.active"));
		operation.addPart().setName("value").setValue(new BooleanType(false));
		return patch;
	}

	/**
	 * Renders the content of every stored version, so that a failure says what was lost rather than
	 * only that a number was wrong. This is the form the defect was originally diagnosed in.
	 */
	private String describeVersionHistory() {
		long currentVersion = runInTransaction(() -> findPatientRow().getVersion());
		StringBuilder dump = new StringBuilder("stored versions: ");
		for (long version = 1; version <= currentVersion; version++) {
			Patient historical =
				myPatientDao.read(new IdType(PATIENT_ID + "/_history/" + version), new SystemRequestDetails());
			String names = historical.getName().stream()
				.map(HumanName::getFamily)
				.collect(Collectors.joining(",", "[", "]"));
			dump.append(String.format("v%d(active=%s, names=%s) ", version, historical.getActive(), names));
		}
		return dump.toString().trim();
	}

	private void assertPatientStorageVersionsAreConsistent() {
		runInTransaction(() -> {
			ResourceTable currentRow = findPatientRow();
			List<Long> historyVersions = historyVersionsOf(currentRow);
			long highestHistoryVersion = historyVersions.stream().mapToLong(Long::longValue).max().orElse(-1L);

			assertThat(currentRow.getVersion())
				.as("HFJ_RESOURCE.RES_VER does not point at the newest HFJ_RES_VER row - history=%s", historyVersions)
				.isEqualTo(highestHistoryVersion);
		});
	}

	/** Must be called from inside {@code runInTransaction}. */
	private ResourceTable findPatientRow() {
		return myResourceTableDao.findAll().stream()
			.filter(t -> "Patient".equals(t.getResourceType()))
			.findFirst()
			.orElseThrow(() -> new AssertionError("No Patient row in HFJ_RESOURCE"));
	}

	private List<Long> historyVersionsOf(ResourceTable theResourceTable) {
		return myResourceHistoryTableDao
			.findAllVersionsForResourceIdInOrder(theResourceTable.getResourceId().toFk())
			.stream()
			.map(ResourceHistoryTable::getVersion)
			.toList();
	}
}
