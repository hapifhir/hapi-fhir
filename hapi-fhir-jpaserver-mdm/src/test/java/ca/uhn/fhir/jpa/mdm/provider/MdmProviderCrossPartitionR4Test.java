package ca.uhn.fhir.jpa.mdm.provider;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.entity.MdmLink;
import ca.uhn.fhir.jpa.entity.PartitionEntity;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.mdm.api.IMdmSettings;
import ca.uhn.fhir.mdm.model.MdmTransactionContext;
import ca.uhn.fhir.mdm.util.MdmResourceUtil;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.server.interceptor.partition.RequestTenantPartitionInterceptor;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.DecimalType;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.UnsignedIntType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MdmProviderCrossPartitionR4Test extends BaseMdmProviderR4Test {
	@Autowired
	private IMdmSettings myMdmSettings;

	private static final String PARTITION_GOLDEN_RESOURCE = "PARTITION-GOLDEN";

	private RequestTenantPartitionInterceptor requestTenantPartitionInterceptor;

	@Override
	@BeforeEach
	public void before() throws Exception {
		super.before();

		myPartitionSettings.setPartitioningEnabled(true);
		myPartitionSettings.setDefaultPartitionId(0);
		myPartitionLookupSvc.createPartition(new PartitionEntity().setId(1).setName(PARTITION_1), null);
		myPartitionLookupSvc.createPartition(new PartitionEntity().setId(2).setName(PARTITION_2), null);
		myPartitionLookupSvc.createPartition(new PartitionEntity().setId(3).setName(PARTITION_GOLDEN_RESOURCE), null);

		requestTenantPartitionInterceptor = new RequestTenantPartitionInterceptor();
		requestTenantPartitionInterceptor.setPartitionSettings(myPartitionSettings);
		myInterceptorRegistry.registerInterceptor(requestTenantPartitionInterceptor);
	}

	@Override
	@AfterEach
	public void after() throws IOException {
		super.after();

		myPartitionSettings.setPartitioningEnabled(false);
		myMdmSettings.setSearchAllPartitionForMatch(false);
		myMdmSettings.setGoldenResourcePartitionName("");
		myInterceptorRegistry.unregisterInterceptor(requestTenantPartitionInterceptor);
	}


	@Test
	public void testCreateLinkWithMatchResultOnDifferentPartitions() {
		myMdmSettings.setSearchAllPartitionForMatch(true);
		createPatientOnPartition(buildJanePatient(), RequestPartitionId.fromPartitionId(1));

		Bundle result = (Bundle) myPatientMatchProvider.match(buildJanePatient(), new SystemRequestDetails().setRequestPartitionId(RequestPartitionId.fromPartitionId(2)));
		assertThat(result.getEntry()).hasSize(1);
	}

	@Test
	public void testCreateLinkWithMatchResultOnDifferentPartitionsWithoutSearchAllPartition() {
		myMdmSettings.setSearchAllPartitionForMatch(false);
		createPatientOnPartition(buildJanePatient(), RequestPartitionId.fromPartitionId(1));

		Bundle result = (Bundle) myPatientMatchProvider.match(buildJanePatient(), new SystemRequestDetails().setRequestPartitionId(RequestPartitionId.fromPartitionId(2)));
		assertThat(result.getEntry()).isEmpty();
	}

	@Test
	public void testCreateLinkWithResourcesInSpecificPartition() {
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

	@ParameterizedTest
	@CsvSource(value = {
		"false,    '',	                  1",  // Golden resource in same partition as source
		"true,     PARTITION-GOLDEN,      3"   // Golden resource in different partition than source
	})
	void testQueryMdmLinkWithPartition(boolean theSearchAllPartitions, String theGoldenResPartitionName, int theGoldenResPartitionId) {
		myMdmSettings.setSearchAllPartitionForMatch(theSearchAllPartitions);
		myMdmSettings.setGoldenResourcePartitionName(theGoldenResPartitionName);

		Patient jane = createPatientOnPartition(buildJanePatient(), RequestPartitionId.fromPartitionId(1));
		MdmTransactionContext mdmContext =
			myMdmMatchLinkSvc.updateMdmLinksForMdmSource(jane, createContextForCreate("Patient"));

		MdmLink mdmLink = (MdmLink) mdmContext.getMdmLinks().get(0);
		assertThat(mdmLink.getSourcePersistenceId().getPartitionId()).isEqualTo(1);
		assertThat(mdmLink.getGoldenResourcePersistenceId().getPartitionId()).isEqualTo(theGoldenResPartitionId);

		myRequestDetails.setTenantId(PARTITION_1);
		Parameters result = (Parameters) myMdmProvider.queryLinks(null, null, null, null, new UnsignedIntType(0),
			new UnsignedIntType(10), new StringType(), myRequestDetails, new StringType("Patient"));

		assertThat(result.getParameter()).hasSize(3);
		assertThat(result.getParameter().get(0).getName()).isEqualTo("self");
		assertThat(result.getParameter().get(1).getName()).isEqualTo("total");
		assertThat(((DecimalType) (result.getParameter().get(1).getValue())).getValueAsInteger()).isEqualTo(1);
		assertThat(result.getParameter().get(2).getName()).isEqualTo("link");
		assertThat(result.getParameter().get(2).getPart()).isNotEmpty();
	}
}
