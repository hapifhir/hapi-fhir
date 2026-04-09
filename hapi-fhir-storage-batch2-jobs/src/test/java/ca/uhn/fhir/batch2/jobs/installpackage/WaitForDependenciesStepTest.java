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
import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.jpa.packages.PackageInstallOutcomeJson;
import ca.uhn.fhir.jpa.packages.PackageInstallationSpec;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
public class WaitForDependenciesStepTest {

	public static final String INSTANCE_ID = "instance-id";
	public static final String CHUNK_ID = "chunk-id";
	public static final JobInstance ourTestInstance = JobInstance.fromInstanceId(INSTANCE_ID);

	@Mock
	private IJobCoordinator myJobCoordinator;

	private WaitForDependenciesStep myStep;

	@Mock
	private IJobDataSink<PackageContentsJson> myJobDataSink;
	@Captor
	private ArgumentCaptor<PackageContentsJson> myPackageContentsCaptor;
	@Mock
	private IJobStepExecutionServices myJobStepExecutionServices;

	@BeforeEach
	public void beforeEach() {
		myStep = new WaitForDependenciesStep(myJobCoordinator);
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

}
