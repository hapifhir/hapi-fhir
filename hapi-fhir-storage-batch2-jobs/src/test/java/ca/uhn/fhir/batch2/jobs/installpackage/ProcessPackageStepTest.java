package ca.uhn.fhir.batch2.jobs.installpackage;

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IJobStepExecutionServices;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.jobs.installpackage.model.InstallationOutcomeJson;
import ca.uhn.fhir.batch2.jobs.installpackage.model.PackageContentsJson;
import ca.uhn.fhir.batch2.jobs.installpackage.model.PackageInstallationJobParameters;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.jpa.packages.IPackageInstallerSvc;
import ca.uhn.fhir.jpa.packages.PackageInstallOutcomeJson;
import ca.uhn.fhir.jpa.packages.PackageInstallationSpec;
import org.hl7.fhir.utilities.npm.NpmPackage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ProcessPackageStepTest {

	public static final String INSTANCE_ID = "instance-id";
	public static final String CHUNK_ID = "chunk-id";
	public static final JobInstance ourTestInstance = JobInstance.fromInstanceId(INSTANCE_ID);

	@Mock
	IPackageInstallerSvc myPackageInstallerSvc;

	@InjectMocks
	private ProcessPackageStep myStep;

	@Captor
	private ArgumentCaptor<NpmPackage> myNpmPackageCaptor;

	@Mock
	private IJobDataSink<InstallationOutcomeJson> myJobDataSink;
	@Captor
	private ArgumentCaptor<InstallationOutcomeJson> myInstallationOutcomeCaptor;
	@Mock
	private IJobStepExecutionServices myJobStepExecutionServices;

	@Test
	public void testRun_installOnly_succeeds() throws Exception {
		// set up
		PackageInstallationSpec installationSpec = new PackageInstallationSpec();
		installationSpec.setInstallMode(PackageInstallationSpec.InstallModeEnum.INSTALL_ONLY);
		installationSpec.setFetchDependencies(false);

		PackageInstallationJobParameters params = new PackageInstallationJobParameters();
		params.setInstallationSpec(installationSpec);

		InputStream stream = ProcessPackageStepTest.class.getResourceAsStream("usCorePackage.tgz");
		byte[] packageBytes = stream.readAllBytes();
		PackageContentsJson packageContentsJson = new PackageContentsJson();
		packageContentsJson.setContents(packageBytes);
		NpmPackage expectedPackage = NpmPackage.fromPackage(new ByteArrayInputStream(packageBytes));

		StepExecutionDetails<PackageInstallationJobParameters, PackageContentsJson> details =
			new StepExecutionDetails<>(params, packageContentsJson, ourTestInstance, new WorkChunk().setId(CHUNK_ID), myJobStepExecutionServices);

		// execute
		RunOutcome outcome = myStep.run(details, myJobDataSink);

		// validate
		assertThat(outcome).isEqualTo(RunOutcome.SUCCESS);

		verify(myPackageInstallerSvc).installPackage(myNpmPackageCaptor.capture(), eq(installationSpec), any(PackageInstallOutcomeJson.class));
		NpmPackage actualPackage = myNpmPackageCaptor.getValue();
		assertThat(actualPackage).isNotNull();
		assertThat(actualPackage.id()).isEqualTo(expectedPackage.id());
		assertThat(actualPackage.title()).isEqualTo(expectedPackage.title());
		assertThat(actualPackage.getSize()).isEqualTo(expectedPackage.getSize());

		verify(myJobDataSink).accept(myInstallationOutcomeCaptor.capture());
		InstallationOutcomeJson outcomeJson = myInstallationOutcomeCaptor.getValue();
		assertThat(outcomeJson).isNotNull();
		assertThat(outcomeJson.getOutcomes()).hasSize(1);
	}
}
