package ca.uhn.fhir.jpa.dao.expunge;

import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.model.ExpungeOptions;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.model.entity.ResourceLink;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Communication;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.ServiceRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

// Created by claude-sonnet-4-6
/**
 * Verifies that per-resource {@code $expunge} removes all {@code HFJ_RES_LINK} rows
 * involving the expunged resource — both where it is the link source
 * ({@code FK_RESLINK_SOURCE}) and where it is the link target ({@code FK_RESLINK_TARGET})
 * — before deleting the {@code HFJ_RESOURCE} row, so that neither FK constraint is
 * violated.
 */
class ExpungeResourceLinkR4Test extends BaseJpaR4Test {

	@Override
	@BeforeEach
	public void before() throws Exception {
		super.before();
		myStorageSettings.setExpungeEnabled(true);
		myStorageSettings.setAllowMultipleDelete(true);
		myStorageSettings.setIndexOnContainedResources(true);
		myStorageSettings.setIndexOnContainedResourcesRecursively(true);
		// Allows deleting a resource that is still referenced by another live resource.
		myStorageSettings.setEnforceReferentialIntegrityOnDelete(false);
	}

	@AfterEach
	public void after() {
		JpaStorageSettings theDefaults = new JpaStorageSettings();
		myStorageSettings.setExpungeEnabled(theDefaults.isExpungeEnabled());
		myStorageSettings.setAllowMultipleDelete(theDefaults.isAllowMultipleDelete());
		myStorageSettings.setIndexOnContainedResources(theDefaults.isIndexOnContainedResources());
		myStorageSettings.setIndexOnContainedResourcesRecursively(theDefaults.isIndexOnContainedResourcesRecursively());
		myStorageSettings.setEnforceReferentialIntegrityOnDelete(theDefaults.isEnforceReferentialIntegrityOnDelete());
	}

	/**
	 * Contained-resource indexing creates {@code HFJ_RES_LINK} rows where the parent
	 * resource is the source and the referenced resource is the target. Per-resource
	 * {@code $expunge} of the target must succeed even when the sourcing resource is
	 * still alive.
	 */
	@Test
	void testExpungeDeletedTargetResource_WhileSourceWithContainedChildrenStillExists_Succeeds() {
		IIdType thePatientId = createPatient(withActiveTrue());

		Observation theContainedObs = new Observation();
		theContainedObs.setId("#obs");
		theContainedObs.setSubject(new Reference(thePatientId.toUnqualifiedVersionless()));

		Encounter theEncounter = new Encounter();
		theEncounter.getContained().add(theContainedObs);
		theEncounter.addReasonReference(new Reference("#obs"));
		myEncounterDao.create(theEncounter, mySrd);

		// Verify the contained-resource indexer created a link targeting the Patient.
		runInTransaction(() -> {
			List<ResourceLink> theLinks = myResourceLinkDao.findAll();
			ourLog.info("Resource links after create: {}", theLinks);
			assertThat(theLinks)
					.as("contained-resource indexer should create resource links targeting the Patient")
					.anyMatch(theLink -> "Patient".equals(theLink.getTargetResourceType()));
		});

		// Soft-delete leaves the Encounter→Patient link intact; soft-delete only removes source-side links.
		myPatientDao.delete(thePatientId, mySrd);

		assertThatCode(() -> myPatientDao.expunge(thePatientId, new ExpungeOptions()
				.setExpungeDeletedResources(true)
				.setExpungeOldVersions(true), mySrd))
				.as("Per-resource $expunge of a deleted target must succeed even if a still-alive source has a link pointing at it")
				.doesNotThrowAnyException();
	}

	/**
	 * Residual target-side link injected directly. Per-resource {@code $expunge} of the
	 * targeted resource must succeed even when a live resource has a link pointing at it.
	 */
	@Test
	void testExpungeDeletedServiceRequest_WithResidualTargetSideLinkFromCommunication_Succeeds() {
		ServiceRequest theServiceRequest = new ServiceRequest();
		theServiceRequest.setStatus(ServiceRequest.ServiceRequestStatus.ACTIVE);
		theServiceRequest.setIntent(ServiceRequest.ServiceRequestIntent.PLAN);
		IIdType theSrId = myServiceRequestDao.create(theServiceRequest, mySrd).getId().toUnqualifiedVersionless();

		Organization theContainedOrg = new Organization();
		theContainedOrg.setId("#org-1");
		theContainedOrg.setName("North West Community Care Access Centre");
		Communication theCommunication = new Communication();
		theCommunication.getContained().add(theContainedOrg);
		theCommunication.setStatus(Communication.CommunicationStatus.INPROGRESS);
		theCommunication.setSender(new Reference("#org-1"));
		IIdType theCommId =
				myCommunicationDao.create(theCommunication, mySrd).getId().toUnqualifiedVersionless();

		myServiceRequestDao.delete(theSrId, mySrd);

		// Inject a residual target-side link simulating what background re-indexing produces.
		runInTransaction(() -> {
			ResourceTable commTable = myResourceTableDao
					.findById(JpaPid.fromId(theCommId.getIdPartAsLong()))
					.orElseThrow();
			ResourceLink residualLink = ResourceLink.forLocalReference(
					ResourceLink.ResourceLinkForLocalReferenceParams.instance()
							.setSourcePath("Communication.basedOn")
							.setSourceResource(commTable)
							.setTargetResourceType("ServiceRequest")
							.setTargetResourcePid(theSrId.getIdPartAsLong())
							.setTargetResourceId(theSrId.getIdPart())
							.setUpdated(new Date())
							.setTargetResourcePartitionablePartitionId(commTable.getPartitionId()));
			myResourceLinkDao.save(residualLink);
		});

		// Sanity: residual target-side link must exist before expunge.
		runInTransaction(() -> assertThat(myResourceLinkDao.findAll())
				.as("residual Communication→ServiceRequest target-side link must exist before expunge")
				.anyMatch(l -> "Communication".equals(l.getSourceResourceType())
						&& "ServiceRequest".equals(l.getTargetResourceType())));

		assertThatCode(() -> myServiceRequestDao.expunge(theSrId, new ExpungeOptions()
						.setExpungeDeletedResources(true)
						.setExpungeOldVersions(true), mySrd))
				.as("Per-resource $expunge of deleted ServiceRequest must succeed even when a live "
						+ "Communication has a residual link targeting it (FK_RESLINK_TARGET path)")
				.doesNotThrowAnyException();
	}

	/**
	 * Residual source-side link injected directly. Per-resource {@code $expunge} of the
	 * sourcing resource must remove it.
	 * <p>
	 * H2 does not enforce {@code FK_RESLINK_SOURCE} when {@code PARTITION_ID} is
	 * {@code null}, so the test verifies link cleanup directly rather than expecting
	 * an exception.
	 */
	@Test
	void testExpungeDeletedSourceResource_WithResidualSourceSideLink_CleansUpLink() {
		IIdType thePatientId = createPatient(withActiveTrue());

		Encounter theEncounter = new Encounter();
		theEncounter.setSubject(new Reference(thePatientId.toUnqualifiedVersionless()));
		IIdType theEncounterId = myEncounterDao.create(theEncounter, mySrd).getId().toUnqualifiedVersionless();

		myEncounterDao.delete(theEncounterId, mySrd);

		// Inject a residual source-side link simulating what background re-indexing produces.
		runInTransaction(() -> {
			ResourceTable encounterTable = myResourceTableDao
					.findById(JpaPid.fromId(theEncounterId.getIdPartAsLong()))
					.orElseThrow();
			ResourceLink residualLink = ResourceLink.forLocalReference(
					ResourceLink.ResourceLinkForLocalReferenceParams.instance()
							.setSourcePath("Encounter.subject")
							.setSourceResource(encounterTable)
							.setTargetResourceType("Patient")
							.setTargetResourcePid(thePatientId.getIdPartAsLong())
							.setTargetResourceId(thePatientId.getIdPart())
							.setUpdated(new Date())
							.setTargetResourcePartitionablePartitionId(encounterTable.getPartitionId()));
			myResourceLinkDao.save(residualLink);
		});

		// Sanity: residual source-side link must exist before expunge.
		runInTransaction(() -> assertThat(myResourceLinkDao.findAll())
				.as("residual Encounter→Patient source-side link must exist before expunge")
				.anyMatch(l -> "Encounter".equals(l.getSourceResourceType())
						&& "Patient".equals(l.getTargetResourceType())));

		myEncounterDao.expunge(theEncounterId, new ExpungeOptions()
				.setExpungeDeletedResources(true)
				.setExpungeOldVersions(true), mySrd);

		runInTransaction(() -> assertThat(myResourceLinkDao.findAll())
				.as("expunge must remove residual source-side HFJ_RES_LINK rows")
				.noneMatch(l -> "Encounter".equals(l.getSourceResourceType())));
	}
}
