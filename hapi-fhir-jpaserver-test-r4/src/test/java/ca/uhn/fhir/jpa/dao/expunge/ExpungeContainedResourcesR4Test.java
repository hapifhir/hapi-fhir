package ca.uhn.fhir.jpa.dao.expunge;

import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.model.ExpungeOptions;
import ca.uhn.fhir.jpa.model.entity.ResourceLink;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Communication;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.PractitionerRole;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.ServiceRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

/**
 * Regression tests for GH-7933 / GL-8648: per-resource {@code $expunge} throws
 * {@code HAPI-2415} (FK_RESLINK_TARGET constraint violation) when
 * {@code index_contained_resources=ENABLED} and a resource graph containing a
 * Communication with a contained Organization is deleted and then expunged.
 *
 * <p>Root cause: {@code JpaResourceExpungeService.deleteAllSearchParams} only called
 * {@code IResourceLinkDao.deleteByResourceId}, which removes rows where the resource
 * is the <em>source</em> of a link. Rows where the resource is the <em>target</em>
 * (e.g. Communication→ServiceRequest) were left behind, so the subsequent
 * {@code DELETE FROM HFJ_RESOURCE} violated {@code FK_RESLINK_TARGET}.</p>
 */
// Created by claude-sonnet-4-6
class ExpungeContainedResourcesR4Test extends BaseJpaR4Test {

	@Override
	@BeforeEach
	public void before() throws Exception {
		super.before();
		myStorageSettings.setExpungeEnabled(true);
		myStorageSettings.setAllowMultipleDelete(true);
		myStorageSettings.setIndexOnContainedResources(true);
		myStorageSettings.setIndexOnContainedResourcesRecursively(true);
		// The user's reproduction deletes a resource graph in a single bundle
		// transaction. Within a bundle HAPI does not enforce referential integrity
		// between simultaneously deleted entries. Disable it here so the expunge
		// path — not RI checking — is what is under test.
		myStorageSettings.setEnforceReferentialIntegrityOnDelete(false);
	}

	@AfterEach
	public void after() {
		JpaStorageSettings defaults = new JpaStorageSettings();
		myStorageSettings.setExpungeEnabled(defaults.isExpungeEnabled());
		myStorageSettings.setAllowMultipleDelete(defaults.isAllowMultipleDelete());
		myStorageSettings.setIndexOnContainedResources(defaults.isIndexOnContainedResources());
		myStorageSettings.setIndexOnContainedResourcesRecursively(
				defaults.isIndexOnContainedResourcesRecursively());
		myStorageSettings.setEnforceReferentialIntegrityOnDelete(
				defaults.isEnforceReferentialIntegrityOnDelete());
	}

	/**
	 * Primary reproduction of GH-7933: a Communication with a contained Organization
	 * (as sender) that also references ServiceRequest and PractitionerRole is created
	 * via three transaction bundles (mirrors the exact user reproduction), the entire
	 * resource graph is deleted, and all deleted resources are then expunged.
	 *
	 * <p>Without the fix, expunging ServiceRequest violates {@code FK_RESLINK_TARGET}
	 * because the Communication→ServiceRequest link rows were not cleared by
	 * {@code deleteByTargetResourceId} before {@code HFJ_RESOURCE} was deleted.</p>
	 */
	@Test
	void testExpungeDeletedResources_WithCommunicationContainingOrganization_Succeeds() {
		// Bundle 1: create the resources that Communication will reference
		Bundle bundle1 = new Bundle().setType(Bundle.BundleType.TRANSACTION);
		PractitionerRole pr = new PractitionerRole();
		pr.setId("pr-1");
		bundle1.addEntry()
				.setFullUrl("PractitionerRole/pr-1")
				.setResource(pr)
				.getRequest()
				.setMethod(Bundle.HTTPVerb.PUT)
				.setUrl("PractitionerRole/pr-1");
		ServiceRequest sr = new ServiceRequest();
		sr.setId("sr-1");
		sr.setStatus(ServiceRequest.ServiceRequestStatus.ACTIVE);
		sr.setIntent(ServiceRequest.ServiceRequestIntent.PLAN);
		bundle1.addEntry()
				.setFullUrl("ServiceRequest/sr-1")
				.setResource(sr)
				.getRequest()
				.setMethod(Bundle.HTTPVerb.PUT)
				.setUrl("ServiceRequest/sr-1");
		mySystemDao.transaction(mySrd, bundle1);

		// Bundle 2: Communication with a contained Organization as sender.
		// With index_contained_resources enabled the indexer creates HFJ_RES_LINK rows
		// targeting ServiceRequest and PractitionerRole (source = Communication or its
		// sub-resource PID). Those target-side rows are what the bug left behind.
		Organization containedOrg = new Organization();
		containedOrg.setId("org-1");
		containedOrg.addIdentifier(new Identifier()
				.setSystem("https://fhir.infoway-inforoute.ca/NamingSystem/ca-on-provider-upi")
				.setValue("101444878694"));
		containedOrg.setName("North West Community Care Access Centre");

		Communication comm = new Communication();
		comm.setId("comm-1");
		comm.getContained().add(containedOrg);
		comm.setStatus(Communication.CommunicationStatus.INPROGRESS);
		comm.addBasedOn(new Reference("ServiceRequest/sr-1"));
		comm.addRecipient(new Reference("PractitionerRole/pr-1"));
		comm.setSender(new Reference("#org-1"));

		Bundle bundle2 = new Bundle().setType(Bundle.BundleType.TRANSACTION);
		bundle2.addEntry()
				.setFullUrl("Communication/comm-1")
				.setResource(comm)
				.getRequest()
				.setMethod(Bundle.HTTPVerb.PUT)
				.setUrl("Communication/comm-1");
		mySystemDao.transaction(mySrd, bundle2);

		// Sanity: indexer created HFJ_RES_LINK rows targeting ServiceRequest.
		runInTransaction(() -> {
			List<ResourceLink> links = myResourceLinkDao.findAll();
			assertThat(links)
					.as("indexer must create RES_LINK rows targeting ServiceRequest")
					.anyMatch(l -> "ServiceRequest".equals(l.getTargetResourceType()));
		});

		// Bundle 3: delete the entire resource graph (mirrors the user's DELETE bundle)
		Bundle bundle3 = new Bundle().setType(Bundle.BundleType.TRANSACTION);
		bundle3.addEntry().getRequest().setMethod(Bundle.HTTPVerb.DELETE).setUrl("Communication/comm-1");
		bundle3.addEntry().getRequest().setMethod(Bundle.HTTPVerb.DELETE).setUrl("ServiceRequest/sr-1");
		bundle3.addEntry().getRequest().setMethod(Bundle.HTTPVerb.DELETE).setUrl("PractitionerRole/pr-1");
		mySystemDao.transaction(mySrd, bundle3);

		// Execute: system $expunge of all deleted resources.
		// Without the fix this throws DataIntegrityViolationException (FK_RESLINK_TARGET /
		// HAPI-2415) when ServiceRequest's HFJ_RESOURCE row is deleted while
		// Communication→ServiceRequest link rows still target it.
		assertThatCode(() -> mySystemDao.expunge(
						new ExpungeOptions().setExpungeDeletedResources(true), mySrd))
				.as("$expunge of deleted resources must not violate FK_RESLINK_TARGET (HAPI-2415)")
				.doesNotThrowAnyException();

		// Verify: no RES_LINK rows targeting the expunged resources remain.
		runInTransaction(() -> assertThat(myResourceLinkDao.findAll())
				.as("All HFJ_RES_LINK rows targeting deleted resources must be cleared after expunge")
				.noneMatch(l -> "ServiceRequest".equals(l.getTargetResourceType())));
	}

	/**
	 * Adjacent: without contained-resource indexing the extra {@code HFJ_RES_LINK}
	 * rows are not created and {@code $expunge} must succeed even without the fix.
	 *
	 * <p>Expected on master: PASS.</p>
	 */
	@Test
	void testExpungeDeletedResources_WithoutContainedResourceIndexing_Succeeds() {
		myStorageSettings.setIndexOnContainedResources(false);
		myStorageSettings.setIndexOnContainedResourcesRecursively(false);

		Bundle bundle1 = new Bundle().setType(Bundle.BundleType.TRANSACTION);
		PractitionerRole pr = new PractitionerRole();
		pr.setId("pr-1");
		bundle1.addEntry()
				.setFullUrl("PractitionerRole/pr-1")
				.setResource(pr)
				.getRequest()
				.setMethod(Bundle.HTTPVerb.PUT)
				.setUrl("PractitionerRole/pr-1");
		ServiceRequest sr = new ServiceRequest();
		sr.setId("sr-1");
		sr.setStatus(ServiceRequest.ServiceRequestStatus.ACTIVE);
		sr.setIntent(ServiceRequest.ServiceRequestIntent.PLAN);
		bundle1.addEntry()
				.setFullUrl("ServiceRequest/sr-1")
				.setResource(sr)
				.getRequest()
				.setMethod(Bundle.HTTPVerb.PUT)
				.setUrl("ServiceRequest/sr-1");
		mySystemDao.transaction(mySrd, bundle1);

		Organization containedOrg = new Organization();
		containedOrg.setId("org-1");
		containedOrg.setName("North West Community Care Access Centre");

		Communication comm = new Communication();
		comm.setId("comm-1");
		comm.getContained().add(containedOrg);
		comm.setStatus(Communication.CommunicationStatus.INPROGRESS);
		comm.addBasedOn(new Reference("ServiceRequest/sr-1"));
		comm.addRecipient(new Reference("PractitionerRole/pr-1"));
		comm.setSender(new Reference("#org-1"));

		Bundle bundle2 = new Bundle().setType(Bundle.BundleType.TRANSACTION);
		bundle2.addEntry()
				.setFullUrl("Communication/comm-1")
				.setResource(comm)
				.getRequest()
				.setMethod(Bundle.HTTPVerb.PUT)
				.setUrl("Communication/comm-1");
		mySystemDao.transaction(mySrd, bundle2);

		Bundle bundle3 = new Bundle().setType(Bundle.BundleType.TRANSACTION);
		bundle3.addEntry().getRequest().setMethod(Bundle.HTTPVerb.DELETE).setUrl("Communication/comm-1");
		bundle3.addEntry().getRequest().setMethod(Bundle.HTTPVerb.DELETE).setUrl("ServiceRequest/sr-1");
		bundle3.addEntry().getRequest().setMethod(Bundle.HTTPVerb.DELETE).setUrl("PractitionerRole/pr-1");
		mySystemDao.transaction(mySrd, bundle3);

		assertThatCode(() -> mySystemDao.expunge(
						new ExpungeOptions().setExpungeDeletedResources(true), mySrd))
				.as("Without contained-resource indexing $expunge must succeed")
				.doesNotThrowAnyException();
	}

	/**
	 * Adjacent: the bulk {@code $expunge expungeEverything=true} path uses
	 * {@code ExpungeEverythingService}, which deletes link rows before resource rows
	 * and is unaffected by this bug.
	 *
	 * <p>Expected on master: PASS.</p>
	 */
	@Test
	void testSystemExpungeEverything_WithCommunicationContainingOrganization_Succeeds() {
		Bundle bundle1 = new Bundle().setType(Bundle.BundleType.TRANSACTION);
		PractitionerRole pr = new PractitionerRole();
		pr.setId("pr-1");
		bundle1.addEntry()
				.setFullUrl("PractitionerRole/pr-1")
				.setResource(pr)
				.getRequest()
				.setMethod(Bundle.HTTPVerb.PUT)
				.setUrl("PractitionerRole/pr-1");
		ServiceRequest sr = new ServiceRequest();
		sr.setId("sr-1");
		sr.setStatus(ServiceRequest.ServiceRequestStatus.ACTIVE);
		sr.setIntent(ServiceRequest.ServiceRequestIntent.PLAN);
		bundle1.addEntry()
				.setFullUrl("ServiceRequest/sr-1")
				.setResource(sr)
				.getRequest()
				.setMethod(Bundle.HTTPVerb.PUT)
				.setUrl("ServiceRequest/sr-1");
		mySystemDao.transaction(mySrd, bundle1);

		Organization containedOrg = new Organization();
		containedOrg.setId("org-1");
		containedOrg.setName("North West Community Care Access Centre");

		Communication comm = new Communication();
		comm.setId("comm-1");
		comm.getContained().add(containedOrg);
		comm.setStatus(Communication.CommunicationStatus.INPROGRESS);
		comm.addBasedOn(new Reference("ServiceRequest/sr-1"));
		comm.addRecipient(new Reference("PractitionerRole/pr-1"));
		comm.setSender(new Reference("#org-1"));

		Bundle bundle2 = new Bundle().setType(Bundle.BundleType.TRANSACTION);
		bundle2.addEntry()
				.setFullUrl("Communication/comm-1")
				.setResource(comm)
				.getRequest()
				.setMethod(Bundle.HTTPVerb.PUT)
				.setUrl("Communication/comm-1");
		mySystemDao.transaction(mySrd, bundle2);

		// expungeEverything uses ExpungeEverythingService which truncates link tables
		// before resource tables — this path is not affected by the bug.
		assertThatCode(() -> mySystemDao.expunge(
						new ExpungeOptions().setExpungeEverything(true), mySrd))
				.as("Bulk $expunge everything clears link tables before resource tables")
				.doesNotThrowAnyException();
	}
}
