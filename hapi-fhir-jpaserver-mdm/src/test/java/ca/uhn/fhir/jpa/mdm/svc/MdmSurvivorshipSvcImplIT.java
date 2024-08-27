package ca.uhn.fhir.jpa.mdm.svc;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.entity.PartitionEntity;
import ca.uhn.fhir.jpa.mdm.BaseMdmR4Test;
import ca.uhn.fhir.jpa.model.entity.StorageSettings;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.mdm.api.IMdmSurvivorshipService;
import ca.uhn.fhir.mdm.api.MdmLinkSourceEnum;
import ca.uhn.fhir.mdm.api.MdmMatchOutcome;
import ca.uhn.fhir.mdm.model.MdmTransactionContext;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.api.server.storage.TransactionDetails;
import ca.uhn.fhir.rest.server.interceptor.partition.RequestTenantPartitionInterceptor;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MdmSurvivorshipSvcImplIT extends BaseMdmR4Test {

	@Autowired
	private IMdmSurvivorshipService myMdmSurvivorshipService;

	@Test
	public void testRulesOnCreate() {
		Patient p1 = buildFrankPatient();
		Patient p2 = new Patient();

		myMdmSurvivorshipService.applySurvivorshipRulesToGoldenResource(p1, p2, new MdmTransactionContext(MdmTransactionContext.OperationType.CREATE_RESOURCE));

		assertFalse(p2.hasIdElement());
		assertThat(p2.getIdentifier()).isEmpty();
		assertTrue(p2.getMeta().isEmpty());

		assertTrue(p1.getNameFirstRep().equalsDeep(p2.getNameFirstRep()));
		assertNull(p2.getBirthDate());
		assertThat(p2.getTelecom()).hasSize(p1.getTelecom().size());
		assertTrue(p2.getTelecomFirstRep().equalsDeep(p1.getTelecomFirstRep()));
	}

	@Test
	public void testRulesOnMerge() {
		Patient p1 = buildFrankPatient();
		String p1Name = p1.getNameFirstRep().getNameAsSingleString();
		Patient p2 = buildPaulPatient();
		String p2Name = p2.getNameFirstRep().getNameAsSingleString();

		myMdmSurvivorshipService.applySurvivorshipRulesToGoldenResource(p1, p2, new MdmTransactionContext(MdmTransactionContext.OperationType.MERGE_GOLDEN_RESOURCES));

		assertFalse(p2.hasIdElement());
		assertThat(p2.getIdentifier()).isNotEmpty();
		assertTrue(p2.getMeta().isEmpty());

		assertThat(p2.getName()).hasSize(2);
		assertEquals(p2Name, p2.getName().get(0).getNameAsSingleString());
		assertEquals(p1Name, p2.getName().get(1).getNameAsSingleString());
		assertNull(p2.getBirthDate());

		assertThat(p1.getTelecom()).hasSize(p1.getTelecom().size());
		assertTrue(p2.getTelecomFirstRep().equalsDeep(p1.getTelecomFirstRep()));
	}

	@Test
	public void matchingPatientsWith_NON_Numeric_Ids_matches_doesNotThrow_NumberFormatException() {
		final Patient frankPatient1 = buildFrankPatient();
		frankPatient1.setId("patA");
		myPatientDao.update(frankPatient1, new SystemRequestDetails());
		final Patient frankPatient2 = buildFrankPatient();
		frankPatient2.setId("patB");
		myPatientDao.update(frankPatient2, new SystemRequestDetails());
		final Patient goldenPatient = buildFrankPatient();
		myPatientDao.create(goldenPatient, new SystemRequestDetails());

		myMdmLinkDaoSvc.createOrUpdateLinkEntity(goldenPatient, frankPatient1, MdmMatchOutcome.NEW_GOLDEN_RESOURCE_MATCH, MdmLinkSourceEnum.MANUAL, createContextForCreate("Patient"));
		myMdmLinkDaoSvc.createOrUpdateLinkEntity(goldenPatient, frankPatient2, MdmMatchOutcome.NEW_GOLDEN_RESOURCE_MATCH, MdmLinkSourceEnum.MANUAL, createContextForCreate("Patient"));

		myMdmSurvivorshipService.rebuildGoldenResourceWithSurvivorshipRules(goldenPatient, new MdmTransactionContext(MdmTransactionContext.OperationType.UPDATE_LINK));
	}

	@Test
	public void rebuildGoldenResourceWithSurvivorshipRules_usingPatchedResourcesAndPartitions_doesNotThrow() {
		// setup
		boolean isPartitioningEnabled = myPartitionSettings.isPartitioningEnabled();
		boolean isUnnamedPartitionMode = myPartitionSettings.isUnnamedPartitionMode();
		StorageSettings.IndexEnabledEnum indexMissingFields = myStorageSettings.getIndexMissingFields();
		boolean isSearchAllPartitionForMatch = myMdmSettings.getSearchAllPartitionForMatch();
		String goldenResourcePartitionName = myMdmSettings.getGoldenResourcePartitionName();

		myPartitionSettings.setPartitioningEnabled(true);
		myPartitionSettings.setUnnamedPartitionMode(false);
		myStorageSettings.setIndexMissingFields(StorageSettings.IndexEnabledEnum.ENABLED);
		myMdmSettings.setSearchAllPartitionForMatch(true);
		myMdmSettings.setGoldenResourcePartitionName(PARTITION_2);

		MdmTransactionContext transactionContext = new MdmTransactionContext(MdmTransactionContext.OperationType.UPDATE_LINK);

		// register the tenant partition interceptor
		// req'd so partition values will be filled in
		RequestTenantPartitionInterceptor tenantPartitionInterceptor = new RequestTenantPartitionInterceptor();
		myInterceptorRegistry.registerInterceptor(tenantPartitionInterceptor);
		try {
			// create the partitions we need
			PartitionEntity partition1 = new PartitionEntity();
			partition1.setName(PARTITION_1);
			partition1.setId(1);
			partition1.setDescription("first");
			myPartitionLookupSvc.createPartition(partition1, new SystemRequestDetails());
			PartitionEntity partition2 = new PartitionEntity();
			partition2.setName(PARTITION_2);
			partition2.setId(2);
			partition2.setDescription("second");
			myPartitionLookupSvc.createPartition(partition2, new SystemRequestDetails());
			myPartitionLookupSvc.invalidateCaches();

			// create the patients - 2 in part-a, golden in partition golden
			SystemRequestDetails partitionARequestDetails = new SystemRequestDetails();
			partitionARequestDetails.setRequestPartitionId(RequestPartitionId.fromPartitionId(1));
			Patient patient1 = buildJanePatient();
			patient1.setId("Patient/pat-a");
			myPatientDao.update(patient1,
				null,
				true,
				false,
				partitionARequestDetails,
				new TransactionDetails());

			Patient patient2 = buildJanePatient();
			patient2.setId("Patient/pat-b");
			myPatientDao.update(
				patient2,
				null,
				true,
				false,
				partitionARequestDetails,
				new TransactionDetails()
			);

			// manually create our golden resource
			SystemRequestDetails goldenRequestDetails = new SystemRequestDetails();
			goldenRequestDetails.setRequestPartitionId(RequestPartitionId.fromPartitionId(2));
			final Patient goldenPatient = buildJanePatient();
			goldenPatient.setId("pat-gold");
			myPatientDao.update(goldenPatient,
				null,
				true,
				false,
				goldenRequestDetails,
				new TransactionDetails());

			// save our links
			{
				myMdmLinkDaoSvc.createOrUpdateLinkEntity(goldenPatient, patient1, MdmMatchOutcome.NEW_GOLDEN_RESOURCE_MATCH, MdmLinkSourceEnum.AUTO, createContextForCreate("Patient"));
				myMdmLinkDaoSvc.createOrUpdateLinkEntity(goldenPatient, patient2, MdmMatchOutcome.NEW_GOLDEN_RESOURCE_MATCH, MdmLinkSourceEnum.AUTO, createContextForCreate("Patient"));
			}

			// remove link
			{
				Patient patb = new Patient();
				patb.setId(patient2.getId());
				myMdmLinkDaoSvc.createOrUpdateLinkEntity(goldenPatient, patb, MdmMatchOutcome.NO_MATCH, MdmLinkSourceEnum.MANUAL, createContextForUpdate("Patient"));
			}

			// test
			myMdmSurvivorshipService.rebuildGoldenResourceWithSurvivorshipRules(goldenPatient, transactionContext);

			IBundleProvider provider = myPatientDao.search(new SearchParameterMap().setLoadSynchronous(true),
				new SystemRequestDetails().setRequestPartitionId(RequestPartitionId.allPartitions()));
		} finally {
			// reset
			myInterceptorRegistry.unregisterInterceptor(tenantPartitionInterceptor);

			myPartitionSettings.setPartitioningEnabled(isPartitioningEnabled);
			myPartitionSettings.setUnnamedPartitionMode(isUnnamedPartitionMode);
			myStorageSettings.setIndexMissingFields(indexMissingFields);
			myMdmSettings.setSearchAllPartitionForMatch(isSearchAllPartitionForMatch);
			myMdmSettings.setGoldenResourcePartitionName(goldenResourcePartitionName);
		}
	}
}
