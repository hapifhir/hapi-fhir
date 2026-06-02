package ca.uhn.fhir.jpa.packages;

import ca.uhn.fhir.implementationguide.ImplementationGuideCreator;
import ca.uhn.fhir.jpa.entity.TermCodeSystem;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.SearchParameter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link IPackageInstallerSvc#install} when the FHIR search index for
 * {@code CodeSystem.version} is out of sync with the term layer's authoritative
 * {@code (url, version) → resource} mapping.
 *
 * <p>The installer locates an existing resource via a {@code (url, version)} FHIR search
 * before deciding between create and update. When that index is stale (post-SP-change with
 * reindex suppressed) or misconfigured (active SP pointing at the wrong path), the search
 * misses, the installer attempts a fresh create, and the term layer rejects it with
 * {@code HAPI-0848} — even though the same {@code (url, version)} pair already exists.
 *
 * <p>Both tests assert the desired contract: the installer should detect the existing
 * resource via the term layer's mapping and route the install to update it.
 */
// Created by claude-sonnet-4-6
class PackageInstallerSvcCodeSystemVersionSearchParamR4Test extends BaseJpaR4Test {

	private static final String CODE_SYSTEM_URL = "http://example.org/CodeSystem/version-search-param";
	private static final String VERSION_ONE = "1";

	/**
	 * A stale {@code version} search index (SP changed, reindex suppressed) must not cause
	 * the installer to create a duplicate {@code (url, version)} resource. The installer is
	 * expected to detect the existing resource via the term layer's authoritative
	 * {@code (url, version) → resource} mapping and route the package install to update it.
	 */
	@Test
	void install_multiVersionPackageWithStaleVersionSearchIndex_succeeds(@TempDir Path theTempDir) throws IOException {
		myStorageSettings.setDefaultSearchParamsCanBeOverridden(true);
		myStorageSettings.setMarkResourcesForReindexingUponSearchParameterChange(false);

		// Install a broken override SP, so resources created next index nothing under "version".
		SearchParameter override = overrideCodeSystemVersionSearchParam("CodeSystem.title");

		// TermCodeSystemVersion("1", A) is written from CodeSystem.version, but
		// HFJ_SPIDX_TOKEN has no "version" row for A.
		createCodeSystem(VERSION_ONE);
		myTerminologyDeferredStorageSvc.saveAllDeferred();

		// Fix the override expression so the SP is correct again. With reindex disabled,
		// A's missing token row stays missing — the stale-index state we want to drive
		// the package install through.
		override.setExpression("CodeSystem.version");
		mySearchParameterDao.update(override, mySrd);
		mySearchParamRegistry.forceRefresh();

		PackageInstallationSpec spec = buildPackageContainingCodeSystem(theTempDir, VERSION_ONE)
			.setVersionPolicy(PackageInstallationSpec.VersionPolicyEnum.MULTI_VERSION);

		assertInstallProducesValidSlot(spec, VERSION_ONE);
	}

	/**
	 * A misconfigured {@code CodeSystem.version} SP (active but with a wrong path) silently
	 * returns empty for every version token query. The installer is expected to detect the
	 * existing resource via the term layer's mapping rather than creating a duplicate.
	 */
	@Test
	void install_multiVersionPackageWhenVersionSearchParamMisconfigured_succeeds(@TempDir Path theTempDir) throws IOException {
		myStorageSettings.setDefaultSearchParamsCanBeOverridden(true);
		// Wrong expression — captures CodeSystem.title (which we never set) instead of
		// CodeSystem.version. The SP is active, so searches don't error out; they just
		// return empty for any version token query.
		overrideCodeSystemVersionSearchParam("CodeSystem.title");

		// Pre-seed AFTER the override so resource A is indexed under the broken SP definition.
		// The term layer still records TermCodeSystemVersion("1", A) because it builds CSV
		// state from CodeSystem.version directly, not via the FHIR search index.
		createCodeSystem(VERSION_ONE);
		myTerminologyDeferredStorageSvc.saveAllDeferred();

		PackageInstallationSpec spec = buildPackageContainingCodeSystem(theTempDir, VERSION_ONE)
			.setVersionPolicy(PackageInstallationSpec.VersionPolicyEnum.MULTI_VERSION);

		assertInstallProducesValidSlot(spec, VERSION_ONE);
	}

	private CodeSystem createCodeSystem(String theVersion) {
		CodeSystem codeSystem = new CodeSystem();
		codeSystem.setUrl(CODE_SYSTEM_URL);
		codeSystem.setVersion(theVersion);
		codeSystem.setStatus(Enumerations.PublicationStatus.ACTIVE);
		codeSystem.setContent(CodeSystem.CodeSystemContentMode.COMPLETE);
		codeSystem.addConcept(new CodeSystem.ConceptDefinitionComponent(new CodeType("foo-" + theVersion)));
		return (CodeSystem) myCodeSystemDao.create(codeSystem, mySrd).getResource();
	}

	private SearchParameter overrideCodeSystemVersionSearchParam(String theExpression) {
		SearchParameter override = new SearchParameter();
		override.setCode("version");
		override.addBase("CodeSystem");
		override.setType(Enumerations.SearchParamType.TOKEN);
		override.setExpression(theExpression);
		override.setStatus(Enumerations.PublicationStatus.ACTIVE);
		SearchParameter created = (SearchParameter) mySearchParameterDao.create(override, mySrd).getResource();
		mySearchParamRegistry.forceRefresh();
		return created;
	}

	/**
	 * Same scenario as {@link #install_multiVersionPackageWhenVersionSearchParamMisconfigured_succeeds}
	 * but with partitioning enabled. Verifies the term-layer fallback works correctly when partition
	 * context is propagated through {@code createRequestDetails()}.
	 */
	@Test
	void install_multiVersionPackageWhenVersionSearchParamMisconfigured_withPartitioningEnabled_succeeds(@TempDir Path theTempDir) throws IOException {
		myStorageSettings.setDefaultSearchParamsCanBeOverridden(true);
		myPartitionSettings.setPartitioningEnabled(true);

		overrideCodeSystemVersionSearchParam("CodeSystem.title");

		createCodeSystem(VERSION_ONE);
		myTerminologyDeferredStorageSvc.saveAllDeferred();

		PackageInstallationSpec spec = buildPackageContainingCodeSystem(theTempDir, VERSION_ONE)
			.setVersionPolicy(PackageInstallationSpec.VersionPolicyEnum.MULTI_VERSION);

		assertInstallProducesValidSlot(spec, VERSION_ONE);
	}

	// Asserts the install completes without throwing and the (url, version) slot is intact in the term layer.
	private void assertInstallProducesValidSlot(PackageInstallationSpec theSpec, String theVersion) {
		myPackageInstallerSvc.install(theSpec);
		runInTransaction(() -> {
			TermCodeSystem tcs = myTermCodeSystemDao.findByCodeSystemUri(CODE_SYSTEM_URL);
			assertThat(tcs).isNotNull();
			assertThat(myTermCodeSystemVersionDao.findByCodeSystemPidAndVersion(tcs.getPid(), theVersion)).isNotNull();
		});
	}

	private PackageInstallationSpec buildPackageContainingCodeSystem(Path theTempDir, String theVersion) throws IOException {
		Path igDir = Files.createDirectory(theTempDir.resolve("ig-v" + theVersion));
		ImplementationGuideCreator igCreator = new ImplementationGuideCreator(myFhirContext, "version.search.param.test", "1.0.0");
		igCreator.setDirectory(igDir);

		CodeSystem packagedCodeSystem = new CodeSystem();
		packagedCodeSystem.setId("packaged-codesystem-v" + theVersion);
		packagedCodeSystem.setUrl(CODE_SYSTEM_URL);
		packagedCodeSystem.setVersion(theVersion);
		packagedCodeSystem.setStatus(Enumerations.PublicationStatus.ACTIVE);
		packagedCodeSystem.setContent(CodeSystem.CodeSystemContentMode.COMPLETE);
		packagedCodeSystem.addConcept(new CodeSystem.ConceptDefinitionComponent(new CodeType("from-package")));
		igCreator.addResourceToIG("codesystem", packagedCodeSystem);

		Path tarball = igCreator.createTestIG();
		return new PackageInstallationSpec()
			.setName(igCreator.getPackageName())
			.setVersion(igCreator.getPackageVersion())
			.setInstallMode(PackageInstallationSpec.InstallModeEnum.STORE_AND_INSTALL)
			.setPackageContents(Files.readAllBytes(tarball));
	}
}
