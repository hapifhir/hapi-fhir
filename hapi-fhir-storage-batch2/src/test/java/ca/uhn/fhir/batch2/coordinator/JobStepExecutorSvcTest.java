package ca.uhn.fhir.batch2.coordinator;

import ca.uhn.fhir.batch2.api.IJobPersistence;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class JobStepExecutorSvcTest {

	@Mock
	private IJobPersistence myJobPersistence;

	@InjectMocks
	private JobStepExecutorSvc myExecutorSvc;

	// TODO - must write tests
}
