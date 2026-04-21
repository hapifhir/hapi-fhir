package ca.uhn.fhir.batch2.jobs.installpackage;

import ca.uhn.fhir.batch2.api.IJobCoordinator;
import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IJobStepExecutionServices;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.jobs.installpackage.model.PackageContentsJson;
import ca.uhn.fhir.batch2.jobs.installpackage.model.PackageInstallationJobParameters;
import ca.uhn.fhir.batch2.jobs.installpackage.model.PackageWithDependenciesJson;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.JobInstanceStartRequest;
import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.jpa.batch.models.Batch2JobStartResponse;
import ca.uhn.fhir.jpa.packages.PackageInstallOutcomeJson;
import ca.uhn.fhir.jpa.packages.PackageInstallationSpec;
import ca.uhn.fhir.jpa.packages.util.PackageUtils;
import ca.uhn.fhir.util.JsonUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import java.io.InputStream;
import java.util.Base64;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class InitializeDependenciesStepTest {

	public static final String INSTANCE_ID = "instance-id";
	public static final String CHUNK_ID = "chunk-id";
	public static final JobInstance ourTestInstance = JobInstance.fromInstanceId(INSTANCE_ID);

	@Mock
	private IJobCoordinator myJobCoordinator;

	private InitializeDependenciesStep myStep;

	@Mock
	private IJobDataSink<PackageWithDependenciesJson> myJobDataSink;
	@Captor
	private ArgumentCaptor<PackageWithDependenciesJson> myPackageWithDependenciesCaptor;
	@Captor
	private ArgumentCaptor<String> myStringCaptor;
	@Mock
	private IJobStepExecutionServices myJobStepExecutionServices;
	@Captor
	private ArgumentCaptor<JobInstanceStartRequest> myStartRequestCaptor;

	@BeforeEach
	public void beforeEach() {
		myStep = new InitializeDependenciesStep(myJobCoordinator);
	}

	@Test
	public void testRun_succeeds() throws Exception {
		// set up
		PackageInstallationSpec installationSpec = new PackageInstallationSpec();
		installationSpec.setInstallMode(PackageInstallationSpec.InstallModeEnum.INSTALL_ONLY);
		installationSpec.setFetchDependencies(true);

		PackageInstallationJobParameters params = new PackageInstallationJobParameters();
		params.setInstallationSpec(installationSpec);

		InputStream stream = InitializeDependenciesStepTest.class.getResourceAsStream("usCorePackage.tgz");
		byte[] packageBytes = stream.readAllBytes();
		byte[] encodedBytes = Base64.getEncoder().encode(packageBytes);
		PackageContentsJson packageContentsJson = new PackageContentsJson();
		packageContentsJson.setContents(encodedBytes);
		packageContentsJson.setReport(new PackageInstallOutcomeJson());

		StepExecutionDetails<PackageInstallationJobParameters, PackageContentsJson> details =
			new StepExecutionDetails<>(params, packageContentsJson, ourTestInstance, new WorkChunk().setId(CHUNK_ID), myJobStepExecutionServices);

		when(myJobCoordinator.startInstance(any(), any())).then(new JobIdIncrementor());

		// execute
		RunOutcome outcome = myStep.run(details, myJobDataSink);

		// validate
		assertThat(outcome).isEqualTo(RunOutcome.SUCCESS);

		verify(myJobDataSink).accept(myPackageWithDependenciesCaptor.capture());
		PackageWithDependenciesJson outcomeJson = myPackageWithDependenciesCaptor.getValue();
		assertThat(outcomeJson).isNotNull();
		assertThat(outcomeJson.getContents()).isEqualTo(encodedBytes);
		assertThat(outcomeJson.getDependencyJobIds()).containsExactly(
			"job1", "job2", "job3", "job4", "job5", "job6", "job7");
		assertThat(outcomeJson.getReport().getMessage())
			.contains("Package hl7.fhir.us.core#8.0.1 depends on package hl7.fhir.r4.core#4.0.1");

		verify(myJobCoordinator, times(7)).startInstance(any(), myStartRequestCaptor.capture());
		List<JobInstanceStartRequest> startRequests = myStartRequestCaptor.getAllValues();
		List<PackageUtils.DependentPackage> dependencyNames = startRequests.stream()
			.map(JobInstanceStartRequest::getParameters)
			.map(t -> JsonUtil.deserialize(t, PackageInstallationJobParameters.class))
			.map(PackageInstallationJobParameters::getInstallationSpec)
			.map(t -> new PackageUtils.DependentPackage(t.getName(),t.getVersion()))
			.toList();
		assertThat(dependencyNames).containsExactlyInAnyOrder(
			new PackageUtils.DependentPackage("hl7.fhir.r4.core", "4.0.1"),
			new PackageUtils.DependentPackage("hl7.terminology.r4", "7.0.0"),
			new PackageUtils.DependentPackage("hl7.fhir.uv.extensions.r4", "5.2.0"),
			new PackageUtils.DependentPackage("hl7.fhir.uv.smart-app-launch", "2.2.0"),
			new PackageUtils.DependentPackage("hl7.fhir.uv.sdc", "3.0.0"),
			new PackageUtils.DependentPackage("us.cdc.phinvads", "0.12.0"),
			new PackageUtils.DependentPackage("us.nlm.vsac", "0.24.0"));

		startRequests.stream()
			.map(JobInstanceStartRequest::getParameters)
			.map(t -> JsonUtil.deserialize(t, PackageInstallationJobParameters.class))
			.map(PackageInstallationJobParameters::isDependencyJob)
			.forEach(t -> assertThat(t).isTrue());
	}

	@Test
	public void testRun_fetchDependenciesFalse_succeeds() throws Exception {
		// set up
		PackageInstallationSpec installationSpec = new PackageInstallationSpec();
		installationSpec.setInstallMode(PackageInstallationSpec.InstallModeEnum.INSTALL_ONLY);
		installationSpec.setFetchDependencies(false);

		PackageInstallationJobParameters params = new PackageInstallationJobParameters();
		params.setInstallationSpec(installationSpec);

		InputStream stream = InitializeDependenciesStepTest.class.getResourceAsStream("usCorePackage.tgz");
		byte[] packageBytes = stream.readAllBytes();
		byte[] encodedBytes = Base64.getEncoder().encode(packageBytes);
		PackageContentsJson packageContentsJson = new PackageContentsJson();
		packageContentsJson.setContents(encodedBytes);
		packageContentsJson.setReport(new PackageInstallOutcomeJson());

		StepExecutionDetails<PackageInstallationJobParameters, PackageContentsJson> details =
			new StepExecutionDetails<>(params, packageContentsJson, ourTestInstance, new WorkChunk().setId(CHUNK_ID), myJobStepExecutionServices);

		// execute
		RunOutcome outcome = myStep.run(details, myJobDataSink);

		// validate
		assertThat(outcome).isEqualTo(RunOutcome.SUCCESS);

		verify(myJobDataSink).accept(myPackageWithDependenciesCaptor.capture());
		PackageWithDependenciesJson outcomeJson = myPackageWithDependenciesCaptor.getValue();
		assertThat(outcomeJson).isNotNull();
		assertThat(outcomeJson.getContents()).isEqualTo(encodedBytes);
		assertThat(outcomeJson.getDependencyJobIds()).isEmpty();
		assertThat(outcomeJson.getReport().getMessage()).isEmpty();

		verifyNoInteractions(myJobCoordinator);
	}

	@Test
	public void testRun_noDependenciesToProcess_succeeds() throws Exception {
		// set up
		PackageInstallationSpec installationSpec = new PackageInstallationSpec();
		installationSpec.setInstallMode(PackageInstallationSpec.InstallModeEnum.INSTALL_ONLY);
		installationSpec.setFetchDependencies(true);
		// This is unlikely in production, but a convenient way to simulate a package that has no dependencies
		installationSpec.setDependencyExcludes(List.of(".*"));

		PackageInstallationJobParameters params = new PackageInstallationJobParameters();
		params.setInstallationSpec(installationSpec);

		InputStream stream = InitializeDependenciesStepTest.class.getResourceAsStream("usCorePackage.tgz");
		byte[] packageBytes = stream.readAllBytes();
		byte[] encodedBytes = Base64.getEncoder().encode(packageBytes);
		PackageContentsJson packageContentsJson = new PackageContentsJson();
		packageContentsJson.setContents(encodedBytes);
		packageContentsJson.setReport(new PackageInstallOutcomeJson());

		StepExecutionDetails<PackageInstallationJobParameters, PackageContentsJson> details =
			new StepExecutionDetails<>(params, packageContentsJson, ourTestInstance, new WorkChunk().setId(CHUNK_ID), myJobStepExecutionServices);

		// execute
		RunOutcome outcome = myStep.run(details, myJobDataSink);

		// validate
		assertThat(outcome).isEqualTo(RunOutcome.SUCCESS);

		verify(myJobDataSink).accept(myPackageWithDependenciesCaptor.capture());
		PackageWithDependenciesJson outcomeJson = myPackageWithDependenciesCaptor.getValue();
		assertThat(outcomeJson).isNotNull();
		assertThat(outcomeJson.getContents()).isEqualTo(encodedBytes);
		assertThat(outcomeJson.getDependencyJobIds()).isEmpty();
		// the report contains a lot of nonsense that we don't want to assert on, but we do want to confirm it exists
		assertThat(outcomeJson.getReport()).isNotNull();

		verifyNoInteractions(myJobCoordinator);
	}

	@Test
	public void testRun_dryRun_succeeds() throws Exception {
		// set up
		PackageInstallationSpec installationSpec = new PackageInstallationSpec();
		installationSpec.setInstallMode(PackageInstallationSpec.InstallModeEnum.STORE_AND_INSTALL);
		installationSpec.setFetchDependencies(true);
		installationSpec.setDryRun(true);

		PackageInstallationJobParameters params = new PackageInstallationJobParameters();
		params.setInstallationSpec(installationSpec);

		InputStream stream = InitializeDependenciesStepTest.class.getResourceAsStream("usCorePackage.tgz");
		byte[] packageBytes = stream.readAllBytes();
		byte[] encodedBytes = Base64.getEncoder().encode(packageBytes);
		PackageContentsJson packageContentsJson = new PackageContentsJson();
		packageContentsJson.setContents(encodedBytes);
		packageContentsJson.setReport(new PackageInstallOutcomeJson());

		StepExecutionDetails<PackageInstallationJobParameters, PackageContentsJson> details =
			new StepExecutionDetails<>(params, packageContentsJson, ourTestInstance, new WorkChunk().setId(CHUNK_ID), myJobStepExecutionServices);

		// execute
		RunOutcome outcome = myStep.run(details, myJobDataSink);

		// validate
		assertThat(outcome).isEqualTo(RunOutcome.SUCCESS);

		verify(myJobDataSink).accept(myPackageWithDependenciesCaptor.capture());
		PackageWithDependenciesJson outcomeJson = myPackageWithDependenciesCaptor.getValue();
		assertThat(outcomeJson).isNotNull();
		assertThat(outcomeJson.getContents()).isEqualTo(encodedBytes);
		assertThat(outcomeJson.getDependencyJobIds()).isEmpty();
		assertThat(outcomeJson.getReport().getMessage())
			.contains(
				"Installation would install hl7.fhir.r4.core#4.0.1",
				"Installation would install hl7.terminology.r4#7.0.0",
				"Installation would install hl7.fhir.uv.extensions.r4#5.2.0",
				"Installation would install hl7.fhir.uv.smart-app-launch#2.2.0",
				"Installation would install hl7.fhir.uv.sdc#3.0.0",
				"Installation would install us.cdc.phinvads#0.12.0",
				"Installation would install us.nlm.vsac#0.24.0");

		verify(myJobCoordinator, never()).startInstance(any(), any());
	}

	@Test
	public void testRun_badPackage_skipAndContinue() throws Exception {
		// set up
		PackageInstallationSpec installationSpec = new PackageInstallationSpec();
		installationSpec.setName("hl7.fhir.us.core");
		installationSpec.setVersion("8.0.1");
		installationSpec.setInstallMode(PackageInstallationSpec.InstallModeEnum.INSTALL_ONLY);
		installationSpec.setFetchDependencies(true);

		PackageInstallationJobParameters params = new PackageInstallationJobParameters();
		params.setInstallationSpec(installationSpec);

		byte[] encodedBytes = Base64.getEncoder().encode("This is not a valid NPM package".getBytes());
		PackageContentsJson packageContentsJson = new PackageContentsJson();
		packageContentsJson.setContents(encodedBytes);
		packageContentsJson.setReport(new PackageInstallOutcomeJson());

		StepExecutionDetails<PackageInstallationJobParameters, PackageContentsJson> details =
			new StepExecutionDetails<>(params, packageContentsJson, ourTestInstance, new WorkChunk().setId(CHUNK_ID), myJobStepExecutionServices);

		// execute
		RunOutcome outcome = myStep.run(details, myJobDataSink);

		// validate
		assertThat(outcome).isEqualTo(RunOutcome.SUCCESS);

		verify(myJobDataSink).accept(myPackageWithDependenciesCaptor.capture());
		PackageWithDependenciesJson outcomeJson = myPackageWithDependenciesCaptor.getValue();
		assertThat(outcomeJson).isNotNull();
		assertThat(outcomeJson.getContents()).isEqualTo(encodedBytes);
		assertThat(outcomeJson.getDependencyJobIds()).isEmpty();

		verify(myJobDataSink).recoveredError("Failed to process dependencies for package hl7.fhir.us.core#8.0.1");

		verify(myJobCoordinator, never()).startInstance(any(), any());
	}

	@Test
	public void testRun_cannotLaunchChildJobs_skipAndContinue() throws Exception {
		// set up
		PackageInstallationSpec installationSpec = new PackageInstallationSpec();
		installationSpec.setInstallMode(PackageInstallationSpec.InstallModeEnum.INSTALL_ONLY);
		installationSpec.setFetchDependencies(true);

		PackageInstallationJobParameters params = new PackageInstallationJobParameters();
		params.setInstallationSpec(installationSpec);

		InputStream stream = InitializeDependenciesStepTest.class.getResourceAsStream("usCorePackage.tgz");
		byte[] packageBytes = stream.readAllBytes();
		byte[] encodedBytes = Base64.getEncoder().encode(packageBytes);
		PackageContentsJson packageContentsJson = new PackageContentsJson();
		packageContentsJson.setContents(encodedBytes);
		packageContentsJson.setReport(new PackageInstallOutcomeJson());

		StepExecutionDetails<PackageInstallationJobParameters, PackageContentsJson> details =
			new StepExecutionDetails<>(params, packageContentsJson, ourTestInstance, new WorkChunk().setId(CHUNK_ID), myJobStepExecutionServices);

		when(myJobCoordinator.startInstance(any(), any())).thenThrow(new IllegalStateException());

		// execute
		RunOutcome outcome = myStep.run(details, myJobDataSink);

		// validate
		assertThat(outcome).isEqualTo(RunOutcome.SUCCESS);

		verify(myJobDataSink).accept(myPackageWithDependenciesCaptor.capture());
		PackageWithDependenciesJson outcomeJson = myPackageWithDependenciesCaptor.getValue();
		assertThat(outcomeJson).isNotNull();
		assertThat(outcomeJson.getContents()).isEqualTo(encodedBytes);
		assertThat(outcomeJson.getDependencyJobIds()).isEmpty();

		verify(myJobDataSink, atLeastOnce()).recoveredError(myStringCaptor.capture());
		assertThat(myStringCaptor.getAllValues()).contains("Failed to launch child job for dependency package hl7.fhir.r4.core#4.0.1. Skipping this dependency.");

	}

	private static class JobIdIncrementor implements Answer<Batch2JobStartResponse> {

		private int counter = 1;

		@Override
		public Batch2JobStartResponse answer(InvocationOnMock invocation) {
			Batch2JobStartResponse response = new Batch2JobStartResponse();
			response.setInstanceId("job" + counter++);
			return response;
		}
	}
}
