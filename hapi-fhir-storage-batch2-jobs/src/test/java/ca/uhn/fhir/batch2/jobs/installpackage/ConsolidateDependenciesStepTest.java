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
import ca.uhn.fhir.util.JsonUtil;
import jakarta.annotation.Nonnull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Base64;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ConsolidateDependenciesStepTest {

	public static final String INSTANCE_ID = "instance-id";
	public static final String CHUNK_ID = "chunk-id";
	public static final JobInstance ourTestInstance = JobInstance.fromInstanceId(INSTANCE_ID);
	public static final String DEPENDENCY_JOB_ID = "dependency-job-id";
	public static final String DEPENDENCY_JOB_2_ID = "dependency-job-2-id";

	@Mock
	private IJobCoordinator myJobCoordinator;

	private ConsolidateDependenciesStep myStep;

	@Mock
	private IJobDataSink<PackageContentsJson> myJobDataSink;
	@Captor
	private ArgumentCaptor<PackageContentsJson> myPackageContentsCaptor;
	@Captor
	private ArgumentCaptor<String> myStringCaptor;
	@Mock
	private IJobStepExecutionServices myJobStepExecutionServices;

	@BeforeEach
	public void beforeEach() {
		myStep = new ConsolidateDependenciesStep(myJobCoordinator);
	}

	@Test
	public void testRun_noDependencies_succeeds() {
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
	public void testRun_dependenciesNotComplete_throwsException(StatusEnum theStatus) {
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

	@Test
	public void testRun_consolidateDependencies_succeeds() {
		// set up
		PackageInstallationSpec installationSpec = new PackageInstallationSpec();
		installationSpec.setInstallMode(PackageInstallationSpec.InstallModeEnum.INSTALL_ONLY);
		installationSpec.setFetchDependencies(true);

		PackageInstallationJobParameters params = new PackageInstallationJobParameters();
		params.setInstallationSpec(installationSpec);

		String fakePackageContents = "pass through data";
		byte[] encodedBytes = Base64.getEncoder().encode(fakePackageContents.getBytes());

		PackageInstallOutcomeJson report = new PackageInstallOutcomeJson();
		report.getMessage().add("Main package message");

		PackageWithDependenciesJson packageWithDependencies = new PackageWithDependenciesJson();
		packageWithDependencies.setContents(encodedBytes);
		packageWithDependencies.setReport(report);
		packageWithDependencies.setDependencyJobIds(List.of(DEPENDENCY_JOB_ID, DEPENDENCY_JOB_2_ID));

		JobInstance jobInstance1 = createJobInstance(
			DEPENDENCY_JOB_ID,
			params,
			StatusEnum.COMPLETED,
			List.of("SearchParameter", "SearchParameter", "StructureDefinition"));
		JobInstance jobInstance2 = createJobInstance(
			DEPENDENCY_JOB_2_ID,
			params,
			StatusEnum.COMPLETED,
			List.of("SearchParameter", "CodeSystem", "ValueSet"));

		when(myJobCoordinator.getInstance(anyString())).thenReturn(jobInstance1, jobInstance2);

		StepExecutionDetails<PackageInstallationJobParameters, PackageWithDependenciesJson> details =
			new StepExecutionDetails<>(params, packageWithDependencies, ourTestInstance, new WorkChunk().setId(CHUNK_ID), myJobStepExecutionServices);

		// execute
		RunOutcome outcome = myStep.run(details, myJobDataSink);

		// validate
		assertThat(outcome).isEqualTo(RunOutcome.SUCCESS);

		verify(myJobCoordinator, times(2)).getInstance(anyString());

		verify(myJobDataSink).accept(myPackageContentsCaptor.capture());
		PackageContentsJson packageContentsJson = myPackageContentsCaptor.getValue();
		assertThat(packageContentsJson.getContents()).isEqualTo(encodedBytes);
		assertThat(packageContentsJson.getReport().getMessage()).contains(
			"Main package message",
			"Message from dependent job dependency-job-id",
			"Message from dependent job dependency-job-2-id");
		Map<String,Integer> expectedResources = Map.of(
			"SearchParameter", 3,
			"StructureDefinition", 1,
			"CodeSystem", 1,
			"ValueSet", 1);
		assertThat(packageContentsJson.getReport().getResourcesInstalled()).isEqualTo(expectedResources);
	}

	@Test
	public void testRun_dependencyJobFailed_skipAndContinue() {
		// set up
		PackageInstallationSpec installationSpec = new PackageInstallationSpec();
		installationSpec.setName("hl7.fhir.us.core");
		installationSpec.setVersion("8.0.1");
		installationSpec.setInstallMode(PackageInstallationSpec.InstallModeEnum.INSTALL_ONLY);
		installationSpec.setFetchDependencies(true);

		PackageInstallationJobParameters params = new PackageInstallationJobParameters();
		params.setInstallationSpec(installationSpec);

		String fakePackageContents = "pass through data";
		byte[] encodedBytes = Base64.getEncoder().encode(fakePackageContents.getBytes());

		PackageInstallOutcomeJson report = new PackageInstallOutcomeJson();
		report.getMessage().add("Main package message");

		PackageWithDependenciesJson packageWithDependencies = new PackageWithDependenciesJson();
		packageWithDependencies.setContents(encodedBytes);
		packageWithDependencies.setReport(report);
		packageWithDependencies.setDependencyJobIds(List.of(DEPENDENCY_JOB_ID));

		JobInstance jobInstance1 = createJobInstance(
			DEPENDENCY_JOB_ID,
			params,
			StatusEnum.FAILED,
			List.of("SearchParameter", "SearchParameter", "StructureDefinition"));

		when(myJobCoordinator.getInstance(anyString())).thenReturn(jobInstance1);

		StepExecutionDetails<PackageInstallationJobParameters, PackageWithDependenciesJson> details =
			new StepExecutionDetails<>(params, packageWithDependencies, ourTestInstance, new WorkChunk().setId(CHUNK_ID), myJobStepExecutionServices);

		// execute
		RunOutcome outcome = myStep.run(details, myJobDataSink);

		// validate
		assertThat(outcome).isEqualTo(RunOutcome.SUCCESS);

		verify(myJobCoordinator, times(1)).getInstance(anyString());

		verify(myJobDataSink).recoveredError(myStringCaptor.capture());
		assertThat(myStringCaptor.getValue())
			.isEqualTo("Package installation job for hl7.fhir.us.core#8.0.1 terminated in status FAILED");
	}

	@Nonnull
	private static JobInstance createJobInstance(String theInstanceId, PackageInstallationJobParameters theParameters, StatusEnum theStatus, List<String> theResourceTypes) {
		JobInstance jobInstance = new JobInstance();
		jobInstance.setInstanceId(theInstanceId);
		jobInstance.setParameters(JsonUtil.serialize(theParameters));
		jobInstance.setStatus(theStatus);

		PackageInstallOutcomeJson report = new PackageInstallOutcomeJson();
		report.getMessage().add("Message from dependent job " + theInstanceId);
		for (String resourceType : theResourceTypes) {
			report.incrementResourcesInstalled(resourceType);
		}

		jobInstance.setReport(JsonUtil.serialize(report));

		return jobInstance;
	}
}
