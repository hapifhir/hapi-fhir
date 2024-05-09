package ca.uhn.fhir.jpa.mdm.provider;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.entity.PartitionEntity;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.mdm.api.IMdmSettings;
import ca.uhn.fhir.mdm.util.MdmResourceUtil;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MdmProviderCrossPartitionR4Test extends BaseProviderR4Test{
	@Autowired
	private IMdmSettings myMdmSettings;

	private static final String PARTITION_GOLDEN_RESOURCE = "PARTITION-GOLDEN";

	@Override
    @BeforeEach
	public void before() throws Exception {
		super.before();

		myPartitionSettings.setPartitioningEnabled(true);
		myPartitionLookupSvc.createPartition(new PartitionEntity().setId(1).setName(PARTITION_1), null);
		myPartitionLookupSvc.createPartition(new PartitionEntity().setId(2).setName(PARTITION_2), null);
		myPartitionLookupSvc.createPartition(new PartitionEntity().setId(3).setName(PARTITION_GOLDEN_RESOURCE), null);
	}

	@Override
	@AfterEach
	public void after() throws IOException {
		super.after();

		myPartitionSettings.setPartitioningEnabled(false);
		myMdmSettings.setSearchAllPartitionForMatch(false);
		myMdmSettings.setGoldenResourcePartitionName("");
	}


	@Test
	public void testCreateLinkWithMatchResultOnDifferentPartitions() {
		myMdmSettings.setSearchAllPartitionForMatch(true);
		createPatientOnPartition(buildJanePatient(), RequestPartitionId.fromPartitionId(1));

		Bundle result = (Bundle) myMdmProvider.match(buildJanePatient(), new SystemRequestDetails().setRequestPartitionId(RequestPartitionId.fromPartitionId(2)));
		assertThat(result.getEntry()).hasSize(1);
	}

	@Test
	public void testCreateLinkWithMatchResultOnDifferentPartitionsWithoutSearchAllPartition() {
		myMdmSettings.setSearchAllPartitionForMatch(false);
		createPatientOnPartition(buildJanePatient(), RequestPartitionId.fromPartitionId(1));

		Bundle result = (Bundle) myMdmProvider.match(buildJanePatient(), new SystemRequestDetails().setRequestPartitionId(RequestPartitionId.fromPartitionId(2)));
		assertThat(result.getEntry()).isEmpty();
	}

	@Test
	public void testCreateLinkWithResourcesInSpecificPartition(){
		myMdmSettings.setGoldenResourcePartitionName(PARTITION_GOLDEN_RESOURCE);
		myMdmSettings.setSearchAllPartitionForMatch(true);

		assertLinkCount(0);

		Patient jane = createPatientOnPartition(buildJanePatient(), RequestPartitionId.fromPartitionId(1));
		myMdmMatchLinkSvc.updateMdmLinksForMdmSource(jane, createContextForCreate("Patient"));

		assertLinkCount(1);

		RequestDetails requestDetails = new SystemRequestDetails();
		requestDetails.setTenantId(PARTITION_GOLDEN_RESOURCE);
		IBundleProvider searchResult = myPatientDao.search(new SearchParameterMap(), requestDetails);

		assertEquals(searchResult.getAllResources().size(), 1);

		assertTrue(MdmResourceUtil.isGoldenRecord(searchResult.getAllResources().get(0)));
	}
}
