package ca.uhn.fhir.jpa.dao.r5;

import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.interceptor.model.ReadPartitionIdRequestDetails;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.util.BundleBuilder;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r5.model.Bundle;
import org.hl7.fhir.r5.model.Enumerations;
import org.hl7.fhir.r5.model.Extension;
import org.hl7.fhir.r5.model.Observation;
import org.hl7.fhir.r5.model.Patient;
import org.hl7.fhir.r5.model.Reference;
import org.hl7.fhir.r5.model.StringType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static ca.uhn.fhir.util.HapiExtensions.EXTENSION_AUTO_VERSION_REFERENCES_AT_PATH;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

// Created by claude-sonnet-4-6
public class CrossPartitionReferencesNotAllowedTest extends BaseJpaR5Test {

	static final RequestPartitionId PARTITION_PATIENT = RequestPartitionId.fromPartitionId(1);
	static final RequestPartitionId PARTITION_OBSERVATION = RequestPartitionId.fromPartitionId(2);

	@BeforeEach
	@Override
	public void before() throws Exception {
		super.before();
		myPartitionSettings.setPartitioningEnabled(true);
		myPartitionSettings.setAllowReferencesAcrossPartitions(PartitionSettings.CrossPartitionReferenceMode.NOT_ALLOWED);
		myPartitionSettings.setUnnamedPartitionMode(true);
		myInterceptorRegistry.registerInterceptor(new MyPartitionSelectorInterceptor());
		initResourceTypeCacheFromConfig();
	}

	@AfterEach
	public void after() {
		myPartitionSettings.setPartitioningEnabled(new PartitionSettings().isPartitioningEnabled());
		myPartitionSettings.setAllowReferencesAcrossPartitions(new PartitionSettings().getAllowReferencesAcrossPartitions());
		myPartitionSettings.setUnnamedPartitionMode(new PartitionSettings().isUnnamedPartitionMode());
		myInterceptorRegistry.unregisterInterceptorsIf(t -> t instanceof MyPartitionSelectorInterceptor);
	}

	/**
	 * Bug 8610 — direct DAO path (BaseStorageDao.performAutoVersioning).
	 *
	 * Before the #7778 fix: performAutoVersioning() uses allPartitions() and finds the Patient
	 * across partition boundaries; the subsequent indexing step then fails with a confusing
	 * InvalidRequestException from SearchParamExtractorService.
	 * After the #7778 fix: performAutoVersioning() scopes the lookup to the Observation's write
	 * partition (PARTITION_OBSERVATION) and does not find the Patient. The SMILE-9706 fix then
	 * skips auto-versioning for the unresolvable reference and lets DaoResourceLinkResolver's
	 * referential integrity check reject it with InvalidRequestException / HAPI-1094 — the
	 * canonical "resource not found, specified in path" error for referential integrity
	 * violations. Both fixes preserve the security contract (cross-partition references are
	 * rejected); they differ only in which exception surface reports it.
	 */
	@Test
	void testDirectDaoCreate_withAutoVersionExtension_crossPartitionReference_isRejected() {
		// Patient lands in PARTITION_PATIENT (1) via MyPartitionSelectorInterceptor
		Patient patient = new Patient();
		patient.setActive(true);
		IIdType patientId = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();

		Observation obs = new Observation();
		obs.getMeta().addExtension(new Extension(EXTENSION_AUTO_VERSION_REFERENCES_AT_PATH, new StringType("subject")));
		obs.setStatus(Enumerations.ObservationStatus.FINAL);
		obs.setSubject(new Reference(patientId.getValue()));

		// Cross-partition reference must be rejected. After SMILE-9706, performAutoVersioning
		// defers to DaoResourceLinkResolver which throws the canonical HAPI-1094 referential
		// integrity error — matching the transaction-path sibling test below.
		assertThatThrownBy(() -> myObservationDao.create(obs, mySrd))
			.as("Creation must fail — cross-partition ref is NOT_ALLOWED")
			.isInstanceOf(InvalidRequestException.class)
			.hasMessageContaining("HAPI-1094")
			.hasMessageContaining("Patient/" + patientId.getIdPart());
	}

	/**
	 * Bug 8610 — transaction path (BaseTransactionProcessor + DaoResourceLinkResolver).
	 *
	 * When a transaction bundle entry carries the auto-version-references-at-path extension,
	 * the second-pass auto-versioning in BaseTransactionProcessor used to resolve the referenced
	 * resource using RequestPartitionId.allPartitions(). Additionally, DaoResourceLinkResolver
	 * used the TransactionDetails cache without a partition guard, allowing cross-partition PIDs
	 * pre-fetched by TransactionProcessor to bypass the NOT_ALLOWED check in SearchParamExtractorService.
	 *
	 * After the fix: the transaction is correctly rejected because the Patient is not visible
	 * from PARTITION_OBSERVATION, and no cross-partition resource link is created.
	 */
	@Test
	void testTransactionCreate_withAutoVersionExtension_crossPartitionReference_isRejected() {
		// Patient lands in PARTITION_PATIENT (1)
		Patient patient = new Patient();
		patient.setActive(true);
		IIdType patientId = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();

		// Submit a transaction bundle containing only an Observation (which the interceptor
		// routes to PARTITION_OBSERVATION (2)). The auto-version extension triggers the
		// second-pass auto-versioning in BaseTransactionProcessor.
		BundleBuilder bb = new BundleBuilder(myFhirContext);
		Observation obs = new Observation();
		obs.getMeta().addExtension(new Extension(EXTENSION_AUTO_VERSION_REFERENCES_AT_PATH, new StringType("subject")));
		obs.setStatus(Enumerations.ObservationStatus.FINAL);
		obs.setSubject(new Reference(patientId.getValue()));
		bb.addTransactionCreateEntry(obs);

		// Before the fix: transaction succeeds and stores a cross-partition ResourceLink, which
		// is then exposed by _include queries (information leak across partition boundaries).
		// After the fix: the transaction is rejected because Patient is not in PARTITION_OBSERVATION.
		assertThatThrownBy(() -> mySystemDao.transaction(mySrd, (Bundle) bb.getBundle()))
			.as("Transaction must fail — cross-partition reference is NOT_ALLOWED")
			.isInstanceOf(InvalidRequestException.class);
	}

	// Generated by claude-sonnet-4-6
	public class MyPartitionSelectorInterceptor {

		@Hook(Pointcut.STORAGE_PARTITION_IDENTIFY_CREATE)
		public RequestPartitionId selectPartitionCreate(IBaseResource theResource) {
			return selectPartition(myFhirContext.getResourceType(theResource));
		}

		@Hook(Pointcut.STORAGE_PARTITION_IDENTIFY_READ)
		public RequestPartitionId selectPartitionRead(ReadPartitionIdRequestDetails theDetails) {
			return selectPartition(theDetails.getResourceType());
		}

		@Nonnull
		private static RequestPartitionId selectPartition(String theResourceType) {
			return switch (theResourceType) {
				case "Patient" -> PARTITION_PATIENT;
				case "Observation" -> PARTITION_OBSERVATION;
				default -> throw new InternalErrorException("Unexpected resource type in partition selector: " + theResourceType);
			};
		}
	}
}
