package ca.uhn.fhir.batch2.jobs.installpackage;

import ca.uhn.fhir.batch2.api.ChunkExecutionDetails;
import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IJobStepExecutionServices;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.jobs.installpackage.model.PackageContentsJson;
import ca.uhn.fhir.batch2.jobs.installpackage.model.PackageInstallationJobParameters;
import ca.uhn.fhir.batch2.model.ChunkOutcome;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.jpa.packages.IPackageInstallerSvc;
import ca.uhn.fhir.jpa.packages.PackageInstallOutcomeJson;
import ca.uhn.fhir.jpa.packages.PackageInstallationSpec;
import ca.uhn.fhir.jpa.searchparam.registry.ISearchParamRegistryController;
import org.hl7.fhir.utilities.npm.NpmPackage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProcessPackageStepTest {

	public static final String INSTANCE_ID = "instance-id";
	public static final String CHUNK_ID = "chunk-id";
	public static final JobInstance ourTestInstance = JobInstance.fromInstanceId(INSTANCE_ID);

	@Mock
	IPackageInstallerSvc myPackageInstallerSvc;

	@Mock
	private ISearchParamRegistryController mySearchParamRegistryController;

	@Mock
	IValidationSupport myValidationSupport;

	private ProcessPackageStep myStep;

	@Captor
	private ArgumentCaptor<NpmPackage> myNpmPackageCaptor;

	@Mock
	private IJobDataSink<PackageInstallOutcomeJson> myJobDataSink;
	@Captor
	private ArgumentCaptor<PackageInstallOutcomeJson> myInstallationOutcomeCaptor;
	@Mock
	private IJobStepExecutionServices myJobStepExecutionServices;

	@BeforeEach
	public void beforeEach() {
		myStep = new ProcessPackageStep(myPackageInstallerSvc, mySearchParamRegistryController, myValidationSupport);
	}

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
		PackageInstallOutcomeJson expectedReport = new PackageInstallOutcomeJson();

		PackageContentsJson packageContentsJson = new PackageContentsJson();
		packageContentsJson.setContents(Base64.getEncoder().encode(packageBytes));
		packageContentsJson.setReport(expectedReport);
		NpmPackage expectedPackage = NpmPackage.fromPackage(new ByteArrayInputStream(packageBytes));

		ChunkExecutionDetails<PackageInstallationJobParameters, PackageContentsJson> chunkDetails =
			new ChunkExecutionDetails<>(packageContentsJson, params, INSTANCE_ID, CHUNK_ID);
		StepExecutionDetails<PackageInstallationJobParameters, PackageContentsJson> stepDetails =
			new StepExecutionDetails<>(params, packageContentsJson, ourTestInstance, new WorkChunk().setId(CHUNK_ID), myJobStepExecutionServices);

		// execute
		ChunkOutcome chunkOutcome = myStep.consume(chunkDetails);
		RunOutcome runOutcome = myStep.run(stepDetails, myJobDataSink);

		// validate
		assertThat(chunkOutcome.getStatus()).isEqualTo(ChunkOutcome.Status.SUCCESS);
		assertThat(runOutcome).isEqualTo(RunOutcome.SUCCESS);

		verify(myPackageInstallerSvc).installPackage(myNpmPackageCaptor.capture(), eq(installationSpec), any(PackageInstallOutcomeJson.class));
		NpmPackage actualPackage = myNpmPackageCaptor.getValue();
		assertThat(actualPackage).isNotNull();
		assertThat(actualPackage.id()).isEqualTo(expectedPackage.id());
		assertThat(actualPackage.title()).isEqualTo(expectedPackage.title());
		assertThat(actualPackage.getSize()).isEqualTo(expectedPackage.getSize());

		verify(myJobDataSink).accept(myInstallationOutcomeCaptor.capture());
		PackageInstallOutcomeJson outcomeJson = myInstallationOutcomeCaptor.getValue();
		assertThat(outcomeJson).isSameAs(expectedReport);
	}

	@ParameterizedTest
	@CsvSource({"INSTALL_ONLY,1",
	"STORE_AND_INSTALL,1",
	"STORE_ONLY,0"})
	public void testRun_installModes(PackageInstallationSpec.InstallModeEnum theInstallMode, int theExpectedCalls) throws Exception {
		// set up
		PackageInstallationSpec installationSpec = new PackageInstallationSpec();
		installationSpec.setInstallMode(theInstallMode);

		PackageInstallationJobParameters params = new PackageInstallationJobParameters();
		params.setInstallationSpec(installationSpec);

		InputStream stream = ProcessPackageStepTest.class.getResourceAsStream("usCorePackage.tgz");
		byte[] packageBytes = stream.readAllBytes();
		PackageInstallOutcomeJson expectedReport = new PackageInstallOutcomeJson();

		PackageContentsJson packageContentsJson = new PackageContentsJson();
		packageContentsJson.setContents(Base64.getEncoder().encode(packageBytes));
		packageContentsJson.setReport(expectedReport);

		ChunkExecutionDetails<PackageInstallationJobParameters, PackageContentsJson> chunkDetails =
			new ChunkExecutionDetails<>(packageContentsJson, params, INSTANCE_ID, CHUNK_ID);

		// execute
		ChunkOutcome chunkOutcome = myStep.consume(chunkDetails);

		// validate
		assertThat(chunkOutcome.getStatus()).isEqualTo(ChunkOutcome.Status.SUCCESS);

		verify(myPackageInstallerSvc, times(theExpectedCalls)).installPackage(any(), any(), any());
	}

	@Test
	public void testRun_childJob_doesNotReindex() throws Exception {
		// set up
		PackageInstallationSpec installationSpec = new PackageInstallationSpec();
		installationSpec.setInstallMode(PackageInstallationSpec.InstallModeEnum.INSTALL_ONLY);
		installationSpec.setFetchDependencies(false);

		PackageInstallationJobParameters params = new PackageInstallationJobParameters();
		params.setInstallationSpec(installationSpec);
		params.setDependencyJob(true);

		InputStream stream = ProcessPackageStepTest.class.getResourceAsStream("usCorePackage.tgz");
		byte[] packageBytes = stream.readAllBytes();
		PackageContentsJson packageContentsJson = new PackageContentsJson();
		packageContentsJson.setContents(Base64.getEncoder().encode(packageBytes));
		packageContentsJson.setReport(new PackageInstallOutcomeJson());

		StepExecutionDetails<PackageInstallationJobParameters, PackageContentsJson> details =
			new StepExecutionDetails<>(params, packageContentsJson, ourTestInstance, new WorkChunk().setId(CHUNK_ID), myJobStepExecutionServices);

		// execute
		RunOutcome outcome = myStep.run(details, myJobDataSink);

		// validate
		assertThat(outcome).isEqualTo(RunOutcome.SUCCESS);

		verifyNoInteractions(mySearchParamRegistryController);
		verifyNoInteractions(myValidationSupport);
	}

	@Test
	public void testRun_installFails_throwsException() throws Exception {
		// set up
		PackageInstallationSpec installationSpec = new PackageInstallationSpec();
		installationSpec.setName("hl7.fhir.us.core");
		installationSpec.setVersion("8.0.1");
		installationSpec.setInstallMode(PackageInstallationSpec.InstallModeEnum.INSTALL_ONLY);
		installationSpec.setFetchDependencies(false);

		PackageInstallationJobParameters params = new PackageInstallationJobParameters();
		params.setInstallationSpec(installationSpec);

		InputStream stream = ProcessPackageStepTest.class.getResourceAsStream("usCorePackage.tgz");
		byte[] packageBytes = stream.readAllBytes();
		PackageInstallOutcomeJson expectedReport = new PackageInstallOutcomeJson();

		PackageContentsJson packageContentsJson = new PackageContentsJson();
		packageContentsJson.setContents(Base64.getEncoder().encode(packageBytes));
		packageContentsJson.setReport(expectedReport);

		ChunkExecutionDetails<PackageInstallationJobParameters, PackageContentsJson> chunkDetails =
			new ChunkExecutionDetails<>(packageContentsJson, params, INSTANCE_ID, CHUNK_ID);

		doThrow(new IllegalArgumentException()).when(myPackageInstallerSvc).installPackage(any(), any(), any());

		// execute and validate
		assertThatThrownBy(() -> myStep.consume(chunkDetails)).isInstanceOf(JobExecutionFailedException.class);
	}
}
