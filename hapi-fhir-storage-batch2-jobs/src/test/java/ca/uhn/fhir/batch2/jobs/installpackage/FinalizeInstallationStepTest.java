package ca.uhn.fhir.batch2.jobs.installpackage;

import ca.uhn.fhir.batch2.api.ChunkExecutionDetails;
import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IJobStepExecutionServices;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.jobs.installpackage.model.InstallationOutcomeJson;
import ca.uhn.fhir.batch2.jobs.installpackage.model.PackageInstallationJobParameters;
import ca.uhn.fhir.batch2.model.ChunkOutcome;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.jpa.packages.PackageInstallOutcomeJson;
import ca.uhn.fhir.jpa.packages.PackageInstallationSpec;
import ca.uhn.fhir.jpa.searchparam.registry.ISearchParamRegistryController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class FinalizeInstallationStepTest {

	public static final String INSTANCE_ID = "instance-id";
	public static final String CHUNK_ID = "chunk-id";
	public static final JobInstance ourTestInstance = JobInstance.fromInstanceId(INSTANCE_ID);

	@InjectMocks
	private FinalizeInstallationStep myStep;

	@Mock
	private IJobDataSink<PackageInstallOutcomeJson> myJobDataSink;
	@Captor
	private ArgumentCaptor<PackageInstallOutcomeJson> myOutcomeCaptor;
	@Mock
	private IJobStepExecutionServices myJobStepExecutionServices;

	@Test
	public void testRun_installOnlyNoDependencies_succeeds() throws Exception {
		// set up
		PackageInstallationSpec installationSpec = new PackageInstallationSpec();
		installationSpec.setInstallMode(PackageInstallationSpec.InstallModeEnum.INSTALL_ONLY);
		installationSpec.setFetchDependencies(false);

		PackageInstallationJobParameters params = new PackageInstallationJobParameters();
		params.setInstallationSpec(installationSpec);

		PackageInstallOutcomeJson expectedOutcome = new PackageInstallOutcomeJson();
		InstallationOutcomeJson  outcome = new InstallationOutcomeJson();
		outcome.getOutcomes().add(expectedOutcome);

		ChunkExecutionDetails<PackageInstallationJobParameters, InstallationOutcomeJson> chunkDetails =
			new ChunkExecutionDetails<>(outcome, params, INSTANCE_ID, CHUNK_ID);
		StepExecutionDetails<PackageInstallationJobParameters, InstallationOutcomeJson> stepDetails =
			new StepExecutionDetails<>(params, outcome, ourTestInstance, new WorkChunk().setId(CHUNK_ID), myJobStepExecutionServices);

		//execute
		ChunkOutcome chunkOutcome = myStep.consume(chunkDetails);
		RunOutcome stepOutcome = myStep.run(stepDetails, myJobDataSink);

		// validate
		assertThat(chunkOutcome.getStatus()).isEqualTo(ChunkOutcome.Status.SUCCESS);
		assertThat(stepOutcome).isEqualTo(RunOutcome.SUCCESS);

		verify(myJobDataSink).accept(myOutcomeCaptor.capture());
		PackageInstallOutcomeJson outcomeJson = myOutcomeCaptor.getValue();
		assertThat(outcomeJson).isEqualTo(expectedOutcome);
	}
}
