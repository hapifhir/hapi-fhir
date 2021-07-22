package ca.uhn.fhir.jpa.dao.index;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class IdHelperServiceTest {

	@Test
	public void testReplaceDefault_AllPartitions() {

		IdHelperService svc = new IdHelperService();
		PartitionSettings partitionSettings = new PartitionSettings();
		partitionSettings.setDefaultPartitionId(1);
		svc.setPartitionSettingsForUnitTest(partitionSettings);

		RequestPartitionId outcome = svc.replaceDefault(RequestPartitionId.allPartitions());
		assertSame(RequestPartitionId.allPartitions(), outcome);
	}

	@Test
	public void testReplaceDefault_DefaultPartition() {

		IdHelperService svc = new IdHelperService();
		PartitionSettings partitionSettings = new PartitionSettings();
		partitionSettings.setDefaultPartitionId(1);
		svc.setPartitionSettingsForUnitTest(partitionSettings);

		RequestPartitionId outcome = svc.replaceDefault(RequestPartitionId.defaultPartition());
		assertEquals(1, outcome.getPartitionIds().get(0));
	}

}
