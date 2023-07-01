package ca.uhn.fhir.jpa.partition;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class PartitionLookupSvcImplTest {

	@Autowired
	private PartitionLookupSvcImpl myPartitionLookupSvc;
	@Test
	void generateRandomUnusedPartitionId() {
		for (int i = 0; i<10000; i++) {
			int randomUnusedPartitionId = myPartitionLookupSvc.generateRandomUnusedPartitionId();
			assertTrue(randomUnusedPartitionId >= 1);
		}
	}
}
