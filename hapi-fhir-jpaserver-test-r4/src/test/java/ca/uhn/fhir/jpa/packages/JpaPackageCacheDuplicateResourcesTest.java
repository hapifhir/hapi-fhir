package ca.uhn.fhir.jpa.packages;

import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import jakarta.annotation.Nullable;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.CanonicalType;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Measure;
import org.hl7.fhir.r4.model.ResourceType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * This test is meant to verify new functionality in JpaPackageCache to mitigate duplicate resources by canonical URL
 * across multiple packages.
 * <p/>
 * It sets up two NPM packages, each with the same duplicated Measure url:
 * <a href='http://example.com/Measure/simple-alpha'>...</a>
 * <p/>
 * The intended workflow is triggered by the suspicion of a duplicate resource across these two packages:
 * <ol>
 *     <li>A client indirectly calls the pre-existing `loadPackageAssetByUrl()` with the canonical URL and complains
 *     they got back the wrong support.</li>
 *     <li>In order to investigate this claim, call `findPackageAssetInfoByUrl()` with the Measure URL and view the
 *     details of one or more packages in the results</li>
 *     <li>If there are in fact multiple results above, for each package ID and optionally version returned above,
 *     query for the resource in question to view its details to determine which resource from which package is
 *     the correct one, and return this feedback to the client.  After this point, the client may realize they
 *     set up their packages with the wrong resources and/or canonical URLs.</li>
 * </ol>
 * <p/>
 * This test exercises a simple version of the scenario above, calling the new methods with parameterized test inputs.
 */
public class JpaPackageCacheDuplicateResourcesTest extends BaseJpaR4Test {

	private static final String VERSION_0_1 = "0.1";
	private static final String VERSION_0_2 = "0.2";
	private static final String VERSION_0_3 = "0.3";
	private static final String SIMPLE_ALPHA_PACKAGE = "simple-alpha";
	private static final String SIMPLE_ALPHA_DUPE_PACKAGE = "simple-alpha-dupe";
	private static final String MEASURE_URL = "http://example.com/Measure/simple-alpha";
	private static final String MEASURE_URL_WITH_VERSION_0_1 = MEASURE_URL + "|" + VERSION_0_1;
	private static final String MEASURE_URL_WITH_VERSION_0_2 = MEASURE_URL + "|" + VERSION_0_2;
	private static final String MEASURE_URL_WITH_VERSION_0_3 = MEASURE_URL + "|" + VERSION_0_3;
	protected static final IdType MEASURE_ID_SIMPLE_ALPHA = new IdType(ResourceType.Measure.name(), "simple-alpha");
	protected static final IdType MEASURE_ID_SIMPLE_ALPHA_DUPE = new IdType(ResourceType.Measure.name(), "simple-alpha-dupe");
	protected static final CanonicalType CANONICAL_URL_LIBRARY = new CanonicalType("http://example.com/Library/simple-alpha");

	@Autowired
	private IHapiPackageCacheManager myPackageCacheManager;

	@BeforeEach
	void beforeEach() throws IOException {
		// set up two packages each with a Measure with the same URL: http://example.com/Measure/simple-alpha

		myPackageCacheManager.installPackage(new PackageInstallationSpec()
			.setName(SIMPLE_ALPHA_PACKAGE)
			.setInstallMode(PackageInstallationSpec.InstallModeEnum.STORE_ONLY)
			.setVersion(VERSION_0_1)
			.setPackageUrl("classpath://packages/simple-alpha-0.1.tgz"));

		// This package is identical to the one above, except for the version of the package and all resources
		myPackageCacheManager.installPackage(new PackageInstallationSpec()
			.setName(SIMPLE_ALPHA_PACKAGE)
			.setInstallMode(PackageInstallationSpec.InstallModeEnum.STORE_ONLY)
			.setVersion(VERSION_0_2)
			.setPackageUrl("classpath://packages/simple-alpha-0.2.tgz"));

		// This package is identical to the one above, except the name of the package and ids of the resources, as the
		// resources and versions are identical to the ones in the very first package
		myPackageCacheManager.installPackage(new PackageInstallationSpec()
			.setName(SIMPLE_ALPHA_DUPE_PACKAGE)
			.setInstallMode(PackageInstallationSpec.InstallModeEnum.STORE_ONLY)
			.setVersion(VERSION_0_1)
			.setPackageUrl("classpath://packages/simple-alpha-dupe-0.1.tgz"));
	}

	@Test
	void findPackageAssetInfoByUrl_duplicateResources() {

		final List<NpmPackageAssetInfoJson> assetInfos =
			myPackageCacheManager.findPackageAssetInfoByUrl(FhirVersionEnum.R4, MEASURE_URL);

		assertThat(assetInfos).isNotNull().isNotEmpty().hasSize(3);

		final NpmPackageAssetInfoJson assetInfo1 = assetInfos.get(0);
		final NpmPackageAssetInfoJson assetInfo2 = assetInfos.get(1);
		final NpmPackageAssetInfoJson assetInfo3 = assetInfos.get(2);

		// Sanity check that we get back two asset infos each with the same measure URL but different package IDs
		assertThat(assetInfo1.getPackageId()).isEqualTo(SIMPLE_ALPHA_PACKAGE);
		assertThat(assetInfo1.getFhirVersion()).isEqualTo(FhirVersionEnum.R4);
		assertThat(assetInfo1.getVersion()).isEqualTo(VERSION_0_1);
		assertThat(assetInfo1.getCanonicalUrl()).isEqualTo(MEASURE_URL);

		assertThat(assetInfo2.getPackageId()).isEqualTo(SIMPLE_ALPHA_PACKAGE);
		assertThat(assetInfo2.getFhirVersion()).isEqualTo(FhirVersionEnum.R4);
		assertThat(assetInfo2.getVersion()).isEqualTo(VERSION_0_2);
		assertThat(assetInfo2.getCanonicalUrl()).isEqualTo(MEASURE_URL);

		assertThat(assetInfo3.getPackageId()).isEqualTo(SIMPLE_ALPHA_DUPE_PACKAGE);
		assertThat(assetInfo3.getFhirVersion()).isEqualTo(FhirVersionEnum.R4);
		assertThat(assetInfo3.getVersion()).isEqualTo(VERSION_0_1);
		assertThat(assetInfo3.getCanonicalUrl()).isEqualTo(MEASURE_URL);

		// Sanity check on the existing loadPackageAssetByUrl() returning a single resource even though there are duplicates
		final IBaseResource resource = myPackageCacheManager.loadPackageAssetByUrl(FhirVersionEnum.R4, MEASURE_URL);

		assertThat(resource).isNotNull().isInstanceOf(Measure.class);

		if (resource instanceof Measure measure) {
			assertThat(measure.getUrl()).isEqualTo(MEASURE_URL);
			assertThat(measure.getStructureFhirVersionEnum()).isEqualTo(FhirVersionEnum.R4);
		} else {
			fail("Expected Measure resource, but got: " + resource.getClass().getName());
		}
	}

	@Test
	void loadPackageAssetsByUrl() {
		final List<IBaseResource> duplicateMeasureResources =
			myPackageCacheManager.loadPackageAssetsByUrl(
				FhirVersionEnum.R4,
				MEASURE_URL,
				PageRequest.of(0, 10));

		assertThat(duplicateMeasureResources).isNotNull().isNotEmpty().hasSize(3);

		final IBaseResource resource1 = duplicateMeasureResources.get(0);
		final IBaseResource resource2 = duplicateMeasureResources.get(1);
		final IBaseResource resource3 = duplicateMeasureResources.get(2);

		assertThat(resource1).isInstanceOf(Measure.class);
		assertThat(resource2).isInstanceOf(Measure.class);
		assertThat(resource3).isInstanceOf(Measure.class);

		if ((resource1 instanceof Measure measure1) &&
			(resource2 instanceof Measure measure2) &&
			(resource3 instanceof Measure measure3)) {
			assertThat(measure1.getUrl()).isEqualTo(MEASURE_URL);
			assertThat(measure1.getIdElement().getIdPart()).isEqualTo(SIMPLE_ALPHA_PACKAGE);
			assertThat(measure1.getVersion()).isEqualTo(VERSION_0_1);
			assertThat(measure2.getUrl()).isEqualTo(MEASURE_URL);
			assertThat(measure2.getIdElement().getIdPart()).isEqualTo(SIMPLE_ALPHA_PACKAGE);
			assertThat(measure2.getVersion()).isEqualTo(VERSION_0_2);
			assertThat(measure3.getUrl()).isEqualTo(MEASURE_URL);
			assertThat(measure3.getIdElement().getIdPart()).isEqualTo(SIMPLE_ALPHA_DUPE_PACKAGE);
			assertThat(measure3.getVersion()).isEqualTo(VERSION_0_1);
		}
	}

	@Test
	void findPackageAssetsNoVersion() {
		final List<IBaseResource> duplicatePackageAssets =
			myPackageCacheManager.findPackageAssets(
				FindPackageAssetRequest.noVersion(
					FhirVersionEnum.R4,
					MEASURE_URL,
					SIMPLE_ALPHA_PACKAGE));

		assertThat(duplicatePackageAssets).isNotNull().isNotEmpty().hasSize(2);

		// LUKETODO:  assertions
	}

	@Test
	void findPackageAssetsWithVersion() {
		final List<IBaseResource> packageAssets =
			myPackageCacheManager.findPackageAssets(
				FindPackageAssetRequest.withVersion(
					FhirVersionEnum.R4,
					MEASURE_URL,
					SIMPLE_ALPHA_PACKAGE,
					VERSION_0_2));

		assertThat(packageAssets).isNotNull().isNotEmpty().hasSize(1);

		// LUKETODO:  assertions
	}

	private static Stream<Arguments> findPackageAsset_duplicateResourcesParams() {
		return Stream.of(
			Arguments.of(
				MEASURE_URL,
				SIMPLE_ALPHA_PACKAGE,
				null,
				MEASURE_ID_SIMPLE_ALPHA,
				SIMPLE_ALPHA_PACKAGE,
				CANONICAL_URL_LIBRARY,
				VERSION_0_1,
				null),
			Arguments.of(
				MEASURE_URL,
				SIMPLE_ALPHA_DUPE_PACKAGE,
				null,
				MEASURE_ID_SIMPLE_ALPHA_DUPE,
				SIMPLE_ALPHA_DUPE_PACKAGE,
				CANONICAL_URL_LIBRARY,
				VERSION_0_1,
				"This measure belongs to package simple-alpha-dupe"),
			Arguments.of(
				MEASURE_URL,
				SIMPLE_ALPHA_PACKAGE,
				VERSION_0_2,
				MEASURE_ID_SIMPLE_ALPHA,
				SIMPLE_ALPHA_PACKAGE,
				CANONICAL_URL_LIBRARY,
				VERSION_0_2,
				null),
			Arguments.of(
				MEASURE_URL,
				SIMPLE_ALPHA_DUPE_PACKAGE,
				VERSION_0_1,
				MEASURE_ID_SIMPLE_ALPHA_DUPE,
				SIMPLE_ALPHA_DUPE_PACKAGE,
				CANONICAL_URL_LIBRARY,
				VERSION_0_1,
				"This measure belongs to package simple-alpha-dupe"),
			Arguments.of(
				MEASURE_URL_WITH_VERSION_0_1,
				SIMPLE_ALPHA_PACKAGE,
				null,
				MEASURE_ID_SIMPLE_ALPHA,
				SIMPLE_ALPHA_PACKAGE,
				CANONICAL_URL_LIBRARY,
				VERSION_0_1,
				null),
			Arguments.of(
				MEASURE_URL_WITH_VERSION_0_1,
				SIMPLE_ALPHA_DUPE_PACKAGE,
				null,
				MEASURE_ID_SIMPLE_ALPHA_DUPE,
				SIMPLE_ALPHA_DUPE_PACKAGE,
				CANONICAL_URL_LIBRARY,
				VERSION_0_1,
				"This measure belongs to package simple-alpha-dupe"),
			Arguments.of(
				MEASURE_URL_WITH_VERSION_0_2,
				SIMPLE_ALPHA_PACKAGE,
				VERSION_0_2,
				MEASURE_ID_SIMPLE_ALPHA,
				SIMPLE_ALPHA_PACKAGE,
				CANONICAL_URL_LIBRARY,
				VERSION_0_2,
				null),
			Arguments.of(
				MEASURE_URL_WITH_VERSION_0_1,
				SIMPLE_ALPHA_DUPE_PACKAGE,
				VERSION_0_1,
				MEASURE_ID_SIMPLE_ALPHA_DUPE,
				SIMPLE_ALPHA_DUPE_PACKAGE,
				CANONICAL_URL_LIBRARY,
				VERSION_0_1,
				"This measure belongs to package simple-alpha-dupe")
		);
	}

	@ParameterizedTest
	@MethodSource("findPackageAsset_duplicateResourcesParams")
	void findPackageAsset_duplicateResources(
			String theCanonicalUrl,
			String thePackageId,
			@Nullable String theVersionId,
			IdType theExpectedId,
			String theExpectedName,
			CanonicalType theExpectedLibraryUrl,
			String theExpectedVersion,
			String theExpectedDescription) {

		final FindPackageAssetRequest request =
			FindPackageAssetRequest.withVersion(
				FhirVersionEnum.R4,
				theCanonicalUrl,
				thePackageId,
				theVersionId);

		final IBaseResource resource = myPackageCacheManager.findPackageAsset(request);

		assertThat(resource).isNotNull().isInstanceOf(Measure.class);

		if (resource instanceof Measure measure) {
			assertThat(measure.getIdElement()).isEqualTo(theExpectedId);
			assertThat(measure.getUrl()).isEqualTo(MEASURE_URL);
			assertThat(measure.getStructureFhirVersionEnum()).isEqualTo(FhirVersionEnum.R4);
			assertThat(measure.getName()).isEqualTo(theExpectedName);
			assertThat(measure.getLibrary()).hasSize(1);
			assertThat(measure.getLibrary().get(0).asStringValue()).isEqualTo(theExpectedLibraryUrl.asStringValue());
			assertThat(measure.getVersion()).isEqualTo(theExpectedVersion);
			assertThat(measure.getDescription()).isEqualTo(theExpectedDescription);
		} else {
			fail("Expected Measure resource, but got: " + resource.getClass().getName());
		}
	}

	private static Stream<Arguments> findPackageAsset_duplicateResources_badInputParams() {
		return Stream.of(
			Arguments.of(
				MEASURE_URL,
				SIMPLE_ALPHA_PACKAGE,
				"non-existent-version",
				MEASURE_ID_SIMPLE_ALPHA,
				SIMPLE_ALPHA_PACKAGE,
				CANONICAL_URL_LIBRARY,
				VERSION_0_1,
				null),
			Arguments.of(
				MEASURE_URL_WITH_VERSION_0_2,
				SIMPLE_ALPHA_PACKAGE,
				"non-existent-version",
				MEASURE_ID_SIMPLE_ALPHA,
				SIMPLE_ALPHA_PACKAGE,
				CANONICAL_URL_LIBRARY,
				VERSION_0_1,
				null),
			Arguments.of(
				MEASURE_URL + "|non-existent-version",
				SIMPLE_ALPHA_PACKAGE,
				null,
				MEASURE_ID_SIMPLE_ALPHA,
				SIMPLE_ALPHA_PACKAGE,
				CANONICAL_URL_LIBRARY,
				VERSION_0_1,
				null),
			Arguments.of(
				MEASURE_URL + "|non-existent-version",
				SIMPLE_ALPHA_PACKAGE,
				VERSION_0_1,
				MEASURE_ID_SIMPLE_ALPHA,
				SIMPLE_ALPHA_PACKAGE,
				CANONICAL_URL_LIBRARY,
				VERSION_0_1,
				null)
		);
	}

	@ParameterizedTest
	@MethodSource("findPackageAsset_duplicateResources_badInputParams")
	void findPackageAsset_duplicateResources_badInput(String theCanonicalUrl, String thePackageId, @Nullable String theVersionId) {
		final FindPackageAssetRequest request =
			FindPackageAssetRequest.withVersion(
				FhirVersionEnum.R4,
				theCanonicalUrl,
				thePackageId,
				theVersionId);

		assertThatExceptionOfType(ResourceNotFoundException.class)
			.isThrownBy(() -> myPackageCacheManager.findPackageAsset(request))
			.withMessage("HAPI-2644:  Could not find asset(s) for FHIR version: R4, canonical URL: %s, package ID: %s and package version: %s"
				.formatted(theCanonicalUrl, thePackageId, Optional.ofNullable(theVersionId).orElse("[none]")));
	}
}
