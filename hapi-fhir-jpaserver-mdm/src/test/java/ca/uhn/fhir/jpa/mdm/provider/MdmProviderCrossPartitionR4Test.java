package ca.uhn.fhir.jpa.mdm.provider;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.entity.MdmLink;
import ca.uhn.fhir.jpa.entity.PartitionEntity;
import ca.uhn.fhir.mdm.api.IMdmSettings;
import ca.uhn.fhir.mdm.api.MdmConstants;
import ca.uhn.fhir.mdm.util.MessageHelper;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Extension;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.codesystems.MatchGrade;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class MdmProviderCrossPartitionR4Test extends BaseLinkR4Test{
	@Autowired
	private MessageHelper myMessageHelper;
	@Autowired
	private IMdmSettings myMdmSettings;

	private static final String PARTITON_GOLDEN_RESOURCE = "PARTITION-GOLDEN";

	@BeforeEach
	public void before() throws Exception {
		super.before();

		myPartitionSettings.setPartitioningEnabled(true);
		myPartitionLookupSvc.createPartition(new PartitionEntity().setId(1).setName(PARTITION_1), null);
		myPartitionLookupSvc.createPartition(new PartitionEntity().setId(2).setName(PARTITION_2), null);
		myPartitionLookupSvc.createPartition(new PartitionEntity().setId(3).setName(PARTITON_GOLDEN_RESOURCE), null);
	}


	@Test
	public void testCreateLinkWithMatchResultOnDifferentPartitions() {
		myMdmSettings.setSearchAllPartitionForMatch(true);
		Patient jane = buildJanePatient();
		jane.setActive(true);
		createPatientOnPartition(jane, RequestPartitionId.fromPartitionId(1));
		Patient newJane = buildJanePatient();

		Bundle result = (Bundle) myMdmProvider.match(newJane, new SystemRequestDetails().setRequestPartitionId(RequestPartitionId.fromPartitionId(2)));
		assertEquals(1, result.getEntry().size());
	}

	@Test
	public void testCreateLinkWithMatchResultOnDifferentPartitionsWithoutSearchAllPartition() {
		myMdmSettings.setSearchAllPartitionForMatch(false);
		Patient jane = buildJanePatient();
		jane.setActive(true);
		createPatientOnPartition(jane, RequestPartitionId.fromPartitionId(1));
		Patient newJane = buildJanePatient();

		Bundle result = (Bundle) myMdmProvider.match(newJane, new SystemRequestDetails().setRequestPartitionId(RequestPartitionId.fromPartitionId(2)));
		assertEquals(0, result.getEntry().size());
	}

	// TODO LM, make this work and uncomment
//	public void testCreateLinkWithResourcesInSpecificPartition(){
//		myMdmSettings.setGoldenResourcePartitionId(PARTITON_GOLDEN_RESOURCE);
//
//		RequestPartitionId requestPartitionId1 = RequestPartitionId.fromPartitionId(1);
//		Patient patient = createPatientOnPartition(buildPatientWithNameAndId("PatientGiven", "ID.PatientGiven.123"), true, false, requestPartitionId1);
//		StringType patientId = new StringType(patient.getIdElement().getValue());
//
//		assertLinkCount(2);
//	}
}
