package ca.uhn.fhir.batch2.jobs.installpackage;

import ca.uhn.fhir.batch2.api.IJobCoordinator;
import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IJobStepExecutionServices;
import ca.uhn.fhir.batch2.api.RetryChunkLaterException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.jobs.installpackage.model.PackageContentsJson;
import ca.uhn.fhir.batch2.jobs.installpackage.model.PackageInstallationJobParameters;
import ca.uhn.fhir.batch2.jobs.installpackage.model.PackageWithDependenciesJson;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.jpa.packages.PackageInstallOutcomeJson;
import ca.uhn.fhir.jpa.packages.PackageInstallationSpec;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Base64;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ConsolidateDependenciesStepTest {

	public static final String INSTANCE_ID = "instance-id";
	public static final String CHUNK_ID = "chunk-id";
	public static final JobInstance ourTestInstance = JobInstance.fromInstanceId(INSTANCE_ID);
	public static final String DEPENDENCY_JOB_ID = "dependency-job-id";

	@Mock
	private IJobCoordinator myJobCoordinator;

	private ConsolidateDependenciesStep myStep;

	@Mock
	private IJobDataSink<PackageContentsJson> myJobDataSink;
	@Captor
	private ArgumentCaptor<PackageContentsJson> myPackageContentsCaptor;
	@Mock
	private IJobStepExecutionServices myJobStepExecutionServices;

	@BeforeEach
	public void beforeEach() {
		myStep = new ConsolidateDependenciesStep(myJobCoordinator);
	}

	@Test
	public void testRun_noDependencies_succeeds() throws Exception {
		// set up
		PackageInstallationSpec installationSpec = new PackageInstallationSpec();
		installationSpec.setInstallMode(PackageInstallationSpec.InstallModeEnum.INSTALL_ONLY);
		installationSpec.setFetchDependencies(true);

		PackageInstallationJobParameters params = new PackageInstallationJobParameters();
		params.setInstallationSpec(installationSpec);

		String fakePackageContents = "pass through data";
		byte[] encodedBytes = Base64.getEncoder().encode(fakePackageContents.getBytes());

		PackageInstallOutcomeJson report = new PackageInstallOutcomeJson();
		report.getMessage().add("Test message");

		PackageWithDependenciesJson packageWithDependencies = new PackageWithDependenciesJson();
		packageWithDependencies.setContents(encodedBytes);
		packageWithDependencies.setReport(report);

		StepExecutionDetails<PackageInstallationJobParameters, PackageWithDependenciesJson> details =
			new StepExecutionDetails<>(params, packageWithDependencies, ourTestInstance, new WorkChunk().setId(CHUNK_ID), myJobStepExecutionServices);

		// execute
		RunOutcome outcome = myStep.run(details, myJobDataSink);

		// validate
		assertThat(outcome).isEqualTo(RunOutcome.SUCCESS);

		verifyNoInteractions(myJobCoordinator);

		verify(myJobDataSink).accept(myPackageContentsCaptor.capture());
		PackageContentsJson packageContentsJson = myPackageContentsCaptor.getValue();
		assertThat(packageContentsJson.getContents()).isEqualTo(encodedBytes);
		assertThat(packageContentsJson.getReport().getMessage()).contains("Test message");
	}

	@ParameterizedTest
	@EnumSource(value = StatusEnum.class, names = {"QUEUED", "IN_PROGRESS", "FINALIZE", "ERRORED"})
	public void testRun_dependenciesNotComplete_throwsException(StatusEnum theStatus) throws Exception {
		// set up
		PackageInstallationSpec installationSpec = new PackageInstallationSpec();
		installationSpec.setInstallMode(PackageInstallationSpec.InstallModeEnum.INSTALL_ONLY);
		installationSpec.setFetchDependencies(true);

		PackageInstallationJobParameters params = new PackageInstallationJobParameters();
		params.setInstallationSpec(installationSpec);

		String fakePackageContents = "pass through data";
		byte[] encodedBytes = Base64.getEncoder().encode(fakePackageContents.getBytes());

		PackageInstallOutcomeJson report = new PackageInstallOutcomeJson();
		report.getMessage().add("Test message");

		PackageWithDependenciesJson packageWithDependencies = new PackageWithDependenciesJson();
		packageWithDependencies.setContents(encodedBytes);
		packageWithDependencies.setReport(report);
		packageWithDependencies.setDependencyJobIds(List.of(DEPENDENCY_JOB_ID));

		JobInstance jobInstance = new JobInstance();
		jobInstance.setInstanceId(DEPENDENCY_JOB_ID);
		jobInstance.setStatus(theStatus);
		when(myJobCoordinator.getInstance(DEPENDENCY_JOB_ID)).thenReturn(jobInstance);

		StepExecutionDetails<PackageInstallationJobParameters, PackageWithDependenciesJson> details =
			new StepExecutionDetails<>(params, packageWithDependencies, ourTestInstance, new WorkChunk().setId(CHUNK_ID), myJobStepExecutionServices);

		// execute
		assertThatThrownBy(() -> myStep.run(details, myJobDataSink)).isInstanceOf(RetryChunkLaterException.class);
	}

}
