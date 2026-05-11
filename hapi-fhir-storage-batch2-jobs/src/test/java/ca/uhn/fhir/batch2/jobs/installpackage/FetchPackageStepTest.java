package ca.uhn.fhir.batch2.jobs.installpackage;

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IJobStepExecutionServices;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.batch2.jobs.installpackage.model.PackageContentsJson;
import ca.uhn.fhir.batch2.jobs.installpackage.model.PackageInstallationJobParameters;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.jpa.packages.IHapiPackageCacheManager;
import ca.uhn.fhir.jpa.packages.IPackageInstallerSvc;
import ca.uhn.fhir.jpa.packages.PackageInstallationSpec;
import org.hl7.fhir.utilities.npm.NpmPackage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FetchPackageStepTest {

	public static final String INSTANCE_ID = "instance-id";
	public static final String CHUNK_ID = "chunk-id";
	public static final JobInstance ourTestInstance = JobInstance.fromInstanceId(INSTANCE_ID);

	@Mock
	private IHapiPackageCacheManager myPackageLoader;
	@Mock
	private IPackageInstallerSvc myPackageInstallerSvc;

	private FetchPackageStep step;

	@Mock
	private IJobDataSink<PackageContentsJson> myJobDataSink;
	@Captor
	private ArgumentCaptor<PackageContentsJson> myPackageContentsCaptor;
	@Mock
	private IJobStepExecutionServices myJobStepExecutionServices;

	@BeforeEach
	public void beforeEach() {
		step = new FetchPackageStep(myPackageLoader, myPackageInstallerSvc);
	}

	@Test
	public void testRun_installOnlyNoDependencies_succeeds() throws Exception {
		// set up
		InputStream stream = FetchPackageStepTest.class.getResourceAsStream("usCorePackage.tgz");
		byte[] packageBytes = stream.readAllBytes();
		NpmPackage npmPackage = NpmPackage.fromPackage(new ByteArrayInputStream(packageBytes));
		when(myPackageLoader.installPackage(any())).thenReturn(npmPackage);

		PackageInstallationSpec theInstallationSpec = new PackageInstallationSpec();
		theInstallationSpec.setInstallMode(PackageInstallationSpec.InstallModeEnum.INSTALL_ONLY);
		theInstallationSpec.setFetchDependencies(false);

		PackageInstallationJobParameters params = new PackageInstallationJobParameters();
		params.setInstallationSpec(theInstallationSpec);

		StepExecutionDetails<PackageInstallationJobParameters, VoidModel> details =
			new StepExecutionDetails<>(params, null, ourTestInstance, new WorkChunk().setId(CHUNK_ID), myJobStepExecutionServices);

		// execute
		RunOutcome outcome = step.run(details, myJobDataSink);

		// validate
		assertThat(outcome).isEqualTo(RunOutcome.SUCCESS);

		verify(myJobDataSink).accept(myPackageContentsCaptor.capture());
		PackageContentsJson contents = myPackageContentsCaptor.getValue();
		assertThat(contents).isNotNull();
		assertThat(contents.getReport()).isNotNull();
		byte[] decodedBytes = Base64.getDecoder().decode(contents.getContents());
		NpmPackage jobPackage = NpmPackage.fromPackage(new ByteArrayInputStream(decodedBytes));

		// assert that the package we got out is equivalent to the package we sent in. They will not be identical at the byte level
		assertThat(jobPackage.title()).isEqualTo(npmPackage.title());
		assertThat(jobPackage.version()).isEqualTo(npmPackage.version());
		assertThat(jobPackage.description()).isEqualTo(npmPackage.description());
		assertThat(jobPackage.getSize()).isEqualTo(npmPackage.getSize());
		assertThat(jobPackage.dependencies()).containsExactlyInAnyOrderElementsOf(npmPackage.dependencies());
		assertThat(jobPackage.getFolders().keySet()).containsExactlyInAnyOrderElementsOf(npmPackage.getFolders().keySet());
		for (String key : jobPackage.getFolders().keySet()) {
			assertThat(jobPackage.list(key)).containsExactlyInAnyOrderElementsOf(npmPackage.list(key));
		}
	}

	@Test
	public void testRun_packageUnavailable_throws() throws Exception {
		// set up
		when(myPackageLoader.installPackage(any())).thenReturn(null);

		PackageInstallationSpec theInstallationSpec = new PackageInstallationSpec();
		theInstallationSpec.setInstallMode(PackageInstallationSpec.InstallModeEnum.INSTALL_ONLY);
		theInstallationSpec.setFetchDependencies(false);

		PackageInstallationJobParameters params = new PackageInstallationJobParameters();
		params.setInstallationSpec(theInstallationSpec);

		StepExecutionDetails<PackageInstallationJobParameters, VoidModel> details =
			new StepExecutionDetails<>(params, null, ourTestInstance, new WorkChunk().setId(CHUNK_ID), myJobStepExecutionServices);

		// execute and validate
		assertThatThrownBy(() -> step.run(details, myJobDataSink)).isInstanceOf(JobExecutionFailedException.class);
	}
}
