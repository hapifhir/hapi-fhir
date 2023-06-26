package ca.uhn.fhir.jpa.test;

import ca.uhn.fhir.batch2.api.IJobCoordinator;
import ca.uhn.fhir.batch2.api.IJobMaintenanceService;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.StatusEnum;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class Batch2JobHelperTest {


	private static final String JOB_ID = "Batch2JobHelperTest";
	@Mock
	IJobMaintenanceService myJobMaintenanceService;
	@Mock
	IJobCoordinator myJobCoordinator;

	@InjectMocks
	Batch2JobHelper myBatch2JobHelper;
	static JobInstance ourIncompleteInstance = new JobInstance().setStatus(StatusEnum.IN_PROGRESS);
	static JobInstance ourCompleteInstance = new JobInstance().setStatus(StatusEnum.COMPLETED);

	@AfterEach
	void after() {
		verifyNoMoreInteractions(myJobCoordinator);
		verifyNoMoreInteractions(myJobMaintenanceService);
	}

	@Test
	void awaitJobCompletion_inProgress_callsMaintenance() {
		when(myJobCoordinator.getInstance(JOB_ID)).thenReturn(ourIncompleteInstance, ourIncompleteInstance, ourIncompleteInstance, ourCompleteInstance);

		myBatch2JobHelper.awaitJobCompletion(JOB_ID);
		verify(myJobMaintenanceService, times(1)).runMaintenancePass();

	}

	@Test
	void awaitJobCompletion_alreadyComplete_doesNotCallMaintenance() {
		when(myJobCoordinator.getInstance(JOB_ID)).thenReturn(ourCompleteInstance);

		myBatch2JobHelper.awaitJobCompletion(JOB_ID);
		verifyNoInteractions(myJobMaintenanceService);
	}
}
