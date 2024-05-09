package ca.uhn.fhir.jpa.mdm.svc.candidate;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.entity.MdmLink;
import ca.uhn.fhir.jpa.entity.PartitionEntity;
import ca.uhn.fhir.jpa.mdm.BaseMdmR4Test;
import ca.uhn.fhir.mdm.api.IMdmSettings;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MdmPartitionedGoldenResourceFindingTest extends BaseMdmR4Test {
	@Autowired
	IMdmSettings myMdmSettings;

	/**
	 * These test will do wacky stuff with partition, if you just want to write simple match test put and would
	 * like to not deal with that headache, please see MdmGoldenResourceFindingSvcTest
	 */
	@Test
	public void testNoMatchOnResourcesInDifferentPartition(){
		myMdmSettings.setSearchAllPartitionForMatch(false);
		myPartitionSettings.setPartitioningEnabled(true);
		myPartitionSettings.setUnnamedPartitionMode(false);
		myPartitionLookupSvc.createPartition(new PartitionEntity().setId(1).setName(PARTITION_1), null);
		myPartitionLookupSvc.createPartition(new PartitionEntity().setId(2).setName(PARTITION_2), null);

		Patient jane = createPatientAndUpdateLinksOnPartition(addExternalEID(buildJanePatient(), EID_1), RequestPartitionId.fromPartitionId(1));
		Patient jane2 = createPatientAndUpdateLinksOnPartition(addExternalEID(buildJanePatient(), EID_1), RequestPartitionId.fromPartitionId(2));

		// hack the link into a NO_MATCH
		List<MdmLink> links = myMdmLinkDaoSvc.findMdmLinksBySourceResource(jane);
		assertThat(links).hasSize(1);
		Long janeOriginalGoldenResourceId = links.get(0).getGoldenResourcePersistenceId().getId();


		List<MdmLink> links2 = myMdmLinkDaoSvc.findMdmLinksBySourceResource(jane2);
		assertThat(links2).hasSize(1);
		Long jane2GoldenResourceId = links2.get(0).getGoldenResourcePersistenceId().getId();

		assertThat(jane2GoldenResourceId).isNotEqualTo(janeOriginalGoldenResourceId);
	}

	@Test
	public void testMatchOnResourcesInDifferentPartitionIfSearchAllPartition(){
		myMdmSettings.setSearchAllPartitionForMatch(true);
		myPartitionSettings.setPartitioningEnabled(true);
		myPartitionSettings.setUnnamedPartitionMode(false);
		myPartitionLookupSvc.createPartition(new PartitionEntity().setId(1).setName(PARTITION_1), null);
		myPartitionLookupSvc.createPartition(new PartitionEntity().setId(2).setName(PARTITION_2), null);

		Patient jane = createPatientAndUpdateLinksOnPartition(addExternalEID(buildJanePatient(), EID_1), RequestPartitionId.fromPartitionId(1));
		Patient jane2 = createPatientAndUpdateLinksOnPartition(addExternalEID(buildJanePatient(), EID_1), RequestPartitionId.fromPartitionId(2));

		// hack the link into a NO_MATCH
		List<MdmLink> links = myMdmLinkDaoSvc.findMdmLinksBySourceResource(jane);
		assertThat(links).hasSize(1);
		Long janeOriginalGoldenResourceId = links.get(0).getGoldenResourcePersistenceId().getId();


		List<MdmLink> links2 = myMdmLinkDaoSvc.findMdmLinksBySourceResource(jane2);
		assertThat(links2).hasSize(1);
		Long jane2GoldenResourceId = links2.get(0).getGoldenResourcePersistenceId().getId();

		assertEquals(janeOriginalGoldenResourceId, jane2GoldenResourceId);
	}
}
