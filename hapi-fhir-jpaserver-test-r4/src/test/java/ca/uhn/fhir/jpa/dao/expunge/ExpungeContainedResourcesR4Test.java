package ca.uhn.fhir.jpa.dao.expunge;

import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.model.ExpungeOptions;
import ca.uhn.fhir.jpa.model.entity.ResourceLink;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Reference;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

// Created by claude-opus-4-7
/**
 * Reproduction for GL-8648 / SMILE-12000.
 * <p>
 * When {@link JpaStorageSettings#setIndexOnContainedResources(boolean)} is enabled (with
 * recursive indexing on as well) and a parent resource has a contained child that
 * references another real resource, the contained-resource indexer creates extra
 * {@code HFJ_RES_LINK} rows where the parent is the source and the externally referenced
 * resource is the target. After deleting the target and asking for a per-resource
 * {@code $expunge} of just the target, {@code JpaResourceExpungeService.deleteAllSearchParams}
 * only deletes link rows where the target is the source; rows where it is the link target
 * are left behind. The subsequent {@code DELETE FROM HFJ_RESOURCE} fails the
 * {@code FK_RESLINK_TARGET} referential integrity check and is surfaced as
 * {@code HAPI-2415}.
 * <p>
 * The customer's published stack shows {@code FK_RESLINK_SOURCE} (i.e. the symmetric
 * variant: a residual link row whose {@code SRC} is the resource being deleted). The
 * underlying defect — {@code deleteByResourceId} is one-sided — is the same, so the
 * fix that addresses TARGET-side rows on per-resource expunge will also cover the
 * SOURCE-side variant the customer hit.
 */
class ExpungeContainedResourcesR4Test extends BaseJpaR4Test {

	@Override
	@BeforeEach
	public void before() throws Exception {
		super.before();
		myStorageSettings.setExpungeEnabled(true);
		myStorageSettings.setAllowMultipleDelete(true);
		myStorageSettings.setIndexOnContainedResources(true);
		myStorageSettings.setIndexOnContainedResourcesRecursively(true);
		// The customer's reproduction uses a bundle-delete of a graph of resources;
		// within a bundle, referential integrity on delete is not enforced between
		// the just-deleted siblings. Simulate that here by disabling enforcement so
		// we can target the per-resource expunge bug in isolation.
		myStorageSettings.setEnforceReferentialIntegrityOnDelete(false);
	}

	@AfterEach
	public void after() {
		JpaStorageSettings defaultStorageSettings = new JpaStorageSettings();
		myStorageSettings.setExpungeEnabled(defaultStorageSettings.isExpungeEnabled());
		myStorageSettings.setAllowMultipleDelete(defaultStorageSettings.isAllowMultipleDelete());
		myStorageSettings.setIndexOnContainedResources(defaultStorageSettings.isIndexOnContainedResources());
		myStorageSettings.setIndexOnContainedResourcesRecursively(defaultStorageSettings.isIndexOnContainedResourcesRecursively());
		myStorageSettings.setEnforceReferentialIntegrityOnDelete(defaultStorageSettings.isEnforceReferentialIntegrityOnDelete());
	}

	/**
	 * Primary reproduction: contained-resource indexing creates extra
	 * {@code HFJ_RES_LINK} rows where the parent resource is the source and another
	 * real resource is the target. Per-resource {@code $expunge} of the target
	 * resource fails because {@code JpaResourceExpungeService.deleteAllSearchParams}
	 * only deletes link rows where the resource being expunged is the source, leaving
	 * rows where it is the target behind. The subsequent
	 * {@code DELETE FROM HFJ_RESOURCE} fails the {@code FK_RESLINK_TARGET}
	 * referential integrity check and is surfaced as {@code HAPI-2415}.
	 *
	 * <p>Expected on master: FAIL with HAPI-2415.</p>
	 */
	@Test
	void testExpungeDeletedTargetResource_WhileSourceWithContainedChildrenStillExists_Succeeds() {
		// Setup: a real Patient that the contained resource will reference, plus an
		// Encounter that has a contained Observation pointing at that Patient. With
		// index_contained_resources enabled, the indexer creates HFJ_RES_LINK rows
		// on the Encounter for the contained Observation's subject -> Patient.
		IIdType thePatientId = createPatient(withActiveTrue());

		Observation theContainedObs = new Observation();
		theContainedObs.setId("#obs");
		theContainedObs.setSubject(new Reference(thePatientId.toUnqualifiedVersionless()));

		Encounter theEncounter = new Encounter();
		theEncounter.getContained().add(theContainedObs);
		theEncounter.addReasonReference(new Reference("#obs"));
		myEncounterDao.create(theEncounter, mySrd);

		// Sanity check: the contained-resource indexer created cross-resource links
		// where the Encounter is the source and the Patient is the target.
		runInTransaction(() -> {
			List<ResourceLink> theLinks = myResourceLinkDao.findAll();
			ourLog.info("Resource links after create: {}", theLinks);
			assertThat(theLinks)
					.as("contained-resource indexer should create resource links targeting the Patient")
					.anyMatch(theLink -> "Patient".equals(theLink.getTargetResourceType()));
		});

		// Delete only the target Patient. The Encounter (the source of the link) is
		// still alive, so the HFJ_RES_LINK row targeting Patient is still present.
		myPatientDao.delete(thePatientId, mySrd);

		// Execute: per-resource $expunge of the deleted Patient. This is the
		// JpaResourceExpungeService.expungeCurrentVersionOfResource path described
		// in the RCA. Today this throws PreconditionFailedException with HAPI-2415
		// because deleteAllSearchParams does not remove HFJ_RES_LINK rows where the
		// expunged resource is the target.
		assertThatCode(() -> myPatientDao.expunge(thePatientId, new ExpungeOptions()
				.setExpungeDeletedResources(true)
				.setExpungeOldVersions(true), mySrd))
				.as("Per-resource $expunge of a deleted target must succeed even if a still-alive source has a contained-resource link pointing at it")
				.doesNotThrowAnyException();
	}

	/**
	 * Adjacent: with {@code index_contained_resources=false} the contained-resource
	 * indexer does not create extra link rows, so the residual-link path is not
	 * triggered and per-resource expunge of the target must succeed.
	 *
	 * <p>Expected on master: PASS.</p>
	 */
	@Test
	void testExpungeDeletedTargetResource_WithoutContainedResourceIndexing_Succeeds() {
		myStorageSettings.setIndexOnContainedResources(false);
		myStorageSettings.setIndexOnContainedResourcesRecursively(false);

		IIdType thePatientId = createPatient(withActiveTrue());

		// Without contained-resource indexing the contained Observation never
		// produces an HFJ_RES_LINK row, so deleting and expunging the Patient
		// alone must work even while the Encounter is still alive.
		Observation theContainedObs = new Observation();
		theContainedObs.setId("#obs");
		theContainedObs.setSubject(new Reference(thePatientId.toUnqualifiedVersionless()));

		Encounter theEncounter = new Encounter();
		theEncounter.getContained().add(theContainedObs);
		theEncounter.addReasonReference(new Reference("#obs"));
		myEncounterDao.create(theEncounter, mySrd);

		myPatientDao.delete(thePatientId, mySrd);

		assertThatCode(() -> myPatientDao.expunge(thePatientId, new ExpungeOptions()
				.setExpungeDeletedResources(true)
				.setExpungeOldVersions(true), mySrd))
				.as("Without contained-resource indexing the residual-link path is not triggered")
				.doesNotThrowAnyException();
	}

	/**
	 * Adjacent: the bulk {@code $expunge expungeEverything=true} path is implemented
	 * by {@code ExpungeEverythingService}, which deletes resource-link rows before
	 * resource rows and therefore is not affected by the bug.
	 *
	 * <p>Expected on master: PASS.</p>
	 */
	@Test
	void testSystemExpungeEverything_WithContainedResourceIndexing_Succeeds() {
		IIdType thePatientId = createPatient(withActiveTrue());

		Observation theContainedObs = new Observation();
		theContainedObs.setId("#obs");
		theContainedObs.setSubject(new Reference(thePatientId.toUnqualifiedVersionless()));

		Encounter theEncounter = new Encounter();
		theEncounter.getContained().add(theContainedObs);
		theEncounter.addReasonReference(new Reference("#obs"));
		myEncounterDao.create(theEncounter, mySrd);

		// Bulk path - no DELETE step required for expungeEverything=true.
		assertThatCode(() -> mySystemDao.expunge(new ExpungeOptions()
				.setExpungeEverything(true), mySrd))
				.as("Bulk $expunge everything path orders link deletes before resource deletes")
				.doesNotThrowAnyException();
	}
}
