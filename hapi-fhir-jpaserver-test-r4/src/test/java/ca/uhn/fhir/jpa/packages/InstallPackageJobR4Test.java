package ca.uhn.fhir.jpa.packages;

import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.jpa.test.Batch2JobHelper;
import ca.uhn.fhir.packages.NpmPackageFactory;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.param.UriParam;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.SearchParameter;
import org.hl7.fhir.utilities.npm.NpmPackage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * End-to-end coverage for the INSTALL_PACKAGE batch job
 * ({@link ca.uhn.fhir.batch2.jobs.installpackage.InstallPackageAppCtx}).
 * <p>
 * The individual job steps have unit tests with mocked collaborators, but nothing exercised the job
 * definition itself, so the asynchronous package-loading path was untested. These tests drive the job
 * through {@link IPackageInstallerSvc#installAsynchronously(PackageInstallationSpec)} so the real
 * loader runs: FetchPackageStep calls IHapiPackageCacheManager#installPackage, which resolves the
 * spec's packageUrl via PackageLoaderSvc#loadPackageUrlContents.
 * <p>
 * This class runs with the default package URL allow-list (no IPackageUrlAllowListProvider bean is
 * present, so all URLs are permitted). For allow-list enforcement see
 * {@link InstallPackageJobAllowListR4IT}.
 */
// Created by claude-opus-5
public class InstallPackageJobR4Test extends BaseJpaR4Test {

	private static final String SEARCH_PARAM_URL = "http://example.com/SearchParameter/my-param";

	@Autowired
	private Batch2JobHelper myBatch2JobHelper;

	@Autowired
	private IHapiPackageCacheManager myPackageCacheManager;

	@Test
	void installAsynchronously_withFilePackageUrl_loadsPackageAndInstallsContents(@TempDir Path theTempDir)
			throws IOException {
		// setup
		NpmPackageFactory packageFactory = new NpmPackageFactory(myFhirContext)
				.addResource("SearchParameter", buildSearchParameter());
		Path packagePath = packageFactory.writeToDirectory(theTempDir);

		PackageInstallationSpec spec = new PackageInstallationSpec()
				.setName(packageFactory.getPackageName())
				.setVersion(packageFactory.getPackageVersion())
				.setInstallMode(PackageInstallationSpec.InstallModeEnum.STORE_AND_INSTALL)
				.setFetchDependencies(false)
				.setPackageUrl("file://" + packagePath);

		// execute
		String instanceId = myPackageInstallerSvc.installAsynchronously(spec);

		// verify
		assertThat(instanceId).isNotBlank();

		JobInstance jobInstance = myBatch2JobHelper.awaitJobCompletion(instanceId);
		assertThat(jobInstance.getStatus()).isEqualTo(StatusEnum.COMPLETED);

		// the package was read through the loader and stored, so it now resolves from the cache alone
		NpmPackage cachedPackage = myPackageCacheManager.loadPackageFromCacheOnly(
				packageFactory.getPackageName(), packageFactory.getPackageVersion());
		assertThat(cachedPackage).isNotNull();

		// and its contents were installed
		assertThat(findSearchParametersByUrl().isEmpty()).isFalse();
	}

	/**
	 * This class runs with the permissive default allow-list, so the only way a well-formed URL fails
	 * here is that it cannot be read. The Msg.code assertion pins the failure to FetchPackageStep's
	 * retrieval path so the test cannot pass on an unrelated failure; it deliberately does not try to
	 * distinguish not-found from allow-list rejection, because the job records the same wrapper message
	 * for both. That distinction belongs in a unit test over the allow-list itself.
	 */
	@Test
	void installAsynchronously_withNonExistentPackageUrl_failsJob(@TempDir Path theTempDir) {
		// setup
		String missingPackageUrl = "file://" + theTempDir.resolve("no-such-package-0.1.tgz");

		PackageInstallationSpec spec = new PackageInstallationSpec()
				.setName("no-such-package")
				.setVersion("0.1")
				.setInstallMode(PackageInstallationSpec.InstallModeEnum.STORE_AND_INSTALL)
				.setFetchDependencies(false)
				.setPackageUrl(missingPackageUrl);

		// execute
		String instanceId = myPackageInstallerSvc.installAsynchronously(spec);

		// verify
		JobInstance jobInstance =
				myBatch2JobHelper.awaitJobHasStatus(instanceId, StatusEnum.FAILED, StatusEnum.ERRORED);
		assertThat(jobInstance.getStatus()).isIn(StatusEnum.FAILED, StatusEnum.ERRORED);
		assertThat(jobInstance.getErrorMessage()).contains("HAPI-2916");
	}

	private SearchParameter buildSearchParameter() {
		SearchParameter searchParameter = new SearchParameter();
		searchParameter.setUrl(SEARCH_PARAM_URL);
		searchParameter.setName("My Param");
		searchParameter.setCode("my-param");
		searchParameter.setDescription("My custom search parameter on Patient");
		searchParameter.addBase("Patient");
		searchParameter.setType(Enumerations.SearchParamType.TOKEN);
		searchParameter.setExpression("Patient.identifier");
		searchParameter.setStatus(Enumerations.PublicationStatus.ACTIVE);
		return searchParameter;
	}

	private IBundleProvider findSearchParametersByUrl() {
		IFhirResourceDao<SearchParameter> dao = myDaoRegistry.getResourceDao(SearchParameter.class);
		SearchParameterMap map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(SearchParameter.SP_URL, new UriParam(SEARCH_PARAM_URL));
		return dao.search(map, new SystemRequestDetails());
	}
}
