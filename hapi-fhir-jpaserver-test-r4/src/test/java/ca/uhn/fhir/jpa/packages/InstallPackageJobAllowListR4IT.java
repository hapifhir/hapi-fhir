package ca.uhn.fhir.jpa.packages;

import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.jpa.packages.loader.AllowedUrlPrefix;
import ca.uhn.fhir.jpa.packages.loader.IPackageUrlAllowListProvider;
import ca.uhn.fhir.jpa.packages.loader.PackageLoaderSettings;
import ca.uhn.fhir.jpa.packages.loader.PackageLoaderSvc;
import ca.uhn.fhir.jpa.packages.loader.PackageUrlAllowList;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.jpa.test.Batch2JobHelper;
import ca.uhn.fhir.packages.NpmPackageFactory;
import org.apache.commons.io.FileUtils;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.SearchParameter;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Verifies that the package URL allow-list is enforced on the asynchronous INSTALL_PACKAGE path.
 * <p>
 * The distinction this class exists to draw: in both tests the package is real, valid, and present on
 * disk. The only difference is whether its location is covered by the configured allow-list. A test
 * that merely points at a non-existent URL cannot tell allow-list rejection apart from a failed read,
 * which is why {@link InstallPackageJobR4Test} covers the not-found case separately.
 * <p>
 * The nested {@link AllowListConfig} contributes an {@link IPackageUrlAllowListProvider}, which makes
 * PackageLoaderConfig#loaderSettings build restricted settings rather than the permissive default.
 *
 * This *must* be an IT because Tests run concurrently (and the JVM state of the PackageLoaderSvc
 * is per jvm). We need it to be
 */
// Created by claude-opus-5
@ContextConfiguration(classes = InstallPackageJobAllowListR4IT.AllowListConfig.class)
public class InstallPackageJobAllowListR4IT extends BaseJpaR4Test {

	/**
	 * Cannot be a {@code @TempDir}: the allow-list is read while the Spring context is being built, and
	 * temporary directories are only populated during test-instance post-processing, which is later.
	 * A static initializer is used rather than {@code @BeforeAll} so that the value is set at class load
	 * and does not depend on context loading being lazy. Using a generated name also means this
	 * directory is ours to delete in {@link #deleteAllowedDirectory()}.
	 */
	static final Path ALLOWED_PACKAGE_DIR = createAllowedPackageDir();

	@Autowired
	private Batch2JobHelper myBatch2JobHelper;

	private static Path createAllowedPackageDir() {
		try {
			return Files.createTempDirectory("hapi-allowed-packages");
		} catch (IOException e) {
			fail(e);
			return null;
		}
	}

	@AfterAll
	static void deleteAllowedDirectory() throws IOException {
		FileUtils.deleteDirectory(ALLOWED_PACKAGE_DIR.toFile());
	}

	@AfterEach
	void resetPackageLoaderStatics() {
		// reset to baseline (ie, allow everything), not default
		PackageLoaderSvc.resetSettings();
		PackageLoaderSettings settings = new PackageLoaderSettings(PackageUrlAllowList.allowAll());
		PackageLoaderSvc.initSettings(settings);
	}

	@Test
	void installAsynchronously_whenUrlIsInsideAllowList_completes() throws IOException {
		// setup
		NpmPackageFactory packageFactory = newPackageFactory();
		Path packagePath = packageFactory.writeToDirectory(ALLOWED_PACKAGE_DIR);

		// execute
		String instanceId = myPackageInstallerSvc.installAsynchronously(specFor(packageFactory, packagePath));

		// verify
		JobInstance jobInstance = myBatch2JobHelper.awaitJobCompletion(instanceId);
		assertThat(jobInstance.getStatus()).isEqualTo(StatusEnum.COMPLETED);
	}

	@Test
	void installAsynchronously_whenUrlIsOutsideAllowList_failsJob(@TempDir Path theBlockedDir) throws IOException {
		// setup — identical package, only the location differs
		NpmPackageFactory packageFactory = newPackageFactory();
		Path packagePath = packageFactory.writeToDirectory(theBlockedDir);
		assertThat(packagePath).exists();

		// execute
		String instanceId = myPackageInstallerSvc.installAsynchronously(specFor(packageFactory, packagePath));

		// verify
		JobInstance jobInstance =
				myBatch2JobHelper.awaitJobHasStatus(instanceId, StatusEnum.FAILED, StatusEnum.ERRORED);
		assertThat(jobInstance.getStatus()).isIn(StatusEnum.FAILED, StatusEnum.ERRORED);
		// FetchPackageStep wraps every failure in this one code, so this pins the failure to the
		// retrieval path but cannot show it was the allow-list that rejected it. What makes this test
		// meaningful is the contrast with the sibling test above: same package, same reader, only the
		// location differs.
		assertThat(jobInstance.getErrorMessage()).contains("HAPI-2916");
	}

	private NpmPackageFactory newPackageFactory() {
		SearchParameter searchParameter = new SearchParameter();
		searchParameter.setUrl("http://example.com/SearchParameter/allow-list-param");
		searchParameter.setName("Allow List Param");
		searchParameter.setCode("allow-list-param");
		searchParameter.setDescription("Search parameter used to verify allow-list enforcement");
		searchParameter.addBase("Patient");
		searchParameter.setType(Enumerations.SearchParamType.TOKEN);
		searchParameter.setExpression("Patient.identifier");
		searchParameter.setStatus(Enumerations.PublicationStatus.ACTIVE);

		return new NpmPackageFactory(myFhirContext).addResource("SearchParameter", searchParameter);
	}

	private PackageInstallationSpec specFor(NpmPackageFactory thePackageFactory, Path thePackagePath) {
		return new PackageInstallationSpec()
				.setName(thePackageFactory.getPackageName())
				.setVersion(thePackageFactory.getPackageVersion())
				.setInstallMode(PackageInstallationSpec.InstallModeEnum.STORE_AND_INSTALL)
				.setFetchDependencies(false)
				.setPackageUrl("file://" + thePackagePath);
	}

	@Configuration
	static class AllowListConfig {

		@Bean
		public IPackageUrlAllowListProvider packageUrlAllowListProvider() {
			return new IPackageUrlAllowListProvider() {

				@Override
				public List<AllowedUrlPrefix> getRemotePrefixes() {
					return List.of();
				}

				@Override
				public List<AllowedUrlPrefix> getLocalPrefixes() {
					return List.of(
						new AllowedUrlPrefix(ALLOWED_PACKAGE_DIR.toString(), false));
				}
			};
		}
	}
}
