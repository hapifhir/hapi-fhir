package ca.uhn.fhir.jpa.packages;

import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import jakarta.annotation.Nullable;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Measure;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

// LUKETODO:  address James' comment
public class JpaPackageCacheDuplicateResourcesTest extends BaseJpaR4Test {

	private static final String VERSION_0_1 = "0.1";
	private static final String VERSION_0_5 = "0.5";
	private static final String SIMPLE_ALPHA_PACKAGE = "simple-alpha";
	private static final String SIMPLE_ALPHA_DUPE_PACKAGE = "simple-alpha-dupe";
	private static final String MEASURE_URL = "http://example.com/Measure/simple-alpha";

	@Autowired
	private IHapiPackageCacheManager myPackageCacheManager;

	@BeforeEach
	void beforeEach() throws IOException {

		myPackageCacheManager.installPackage(new PackageInstallationSpec()
			.setName(SIMPLE_ALPHA_PACKAGE)
			.setInstallMode(PackageInstallationSpec.InstallModeEnum.STORE_ONLY)
			.setVersion(VERSION_0_1)
			.setPackageUrl("classpath://packages/simple-alpha.tgz"));

		myPackageCacheManager.installPackage(new PackageInstallationSpec()
			.setName(SIMPLE_ALPHA_DUPE_PACKAGE)
			.setInstallMode(PackageInstallationSpec.InstallModeEnum.STORE_ONLY)
			.setVersion(VERSION_0_5)
			.setPackageUrl("classpath://packages/simple-alpha-dupe.tgz"));
	}

	@Test
	void findPackageAssetInfoByUrl_duplicateResources() {

		final List<NpmPackageAssetInfoJson> jsons =
			myPackageCacheManager.findPackageAssetInfoByUrl(FhirVersionEnum.R4, MEASURE_URL);

		assertThat(jsons).isNotNull().isNotEmpty().hasSize(2);

		final NpmPackageAssetInfoJson json1 = jsons.get(0);
		final NpmPackageAssetInfoJson json2 = jsons.get(1);

		assertThat(json1.getPackageId()).isEqualTo(SIMPLE_ALPHA_PACKAGE);
		assertThat(json1.getFhirVersion()).isEqualTo(FhirVersionEnum.R4);
		assertThat(json1.getVersion()).isEqualTo(VERSION_0_1);
		assertThat(json1.getCanonicalUrl()).isEqualTo(MEASURE_URL);

		assertThat(json2.getPackageId()).isEqualTo(SIMPLE_ALPHA_DUPE_PACKAGE);
		assertThat(json2.getFhirVersion()).isEqualTo(FhirVersionEnum.R4);
		assertThat(json2.getVersion()).isEqualTo(VERSION_0_5);
		assertThat(json2.getCanonicalUrl()).isEqualTo(MEASURE_URL);

		final IBaseResource resource = myPackageCacheManager.loadPackageAssetByUrl(FhirVersionEnum.R4, MEASURE_URL);

		assertThat(resource).isNotNull().isInstanceOf(Measure.class);

		if (resource instanceof Measure measure) {
			assertThat(measure.getUrl()).isEqualTo(MEASURE_URL);
			assertThat(measure.getStructureFhirVersionEnum()).isEqualTo(FhirVersionEnum.R4);
		} else {
			fail("Expected Measure resource, but got: " + resource.getClass().getName());
		}
	}


	private static Stream<Arguments> findPackageAssets_duplicateResourcesParams() {
		return Stream.of(
			Arguments.of(SIMPLE_ALPHA_PACKAGE, null),
			Arguments.of(SIMPLE_ALPHA_DUPE_PACKAGE, null),
			Arguments.of(SIMPLE_ALPHA_PACKAGE, VERSION_0_1),
			Arguments.of(SIMPLE_ALPHA_DUPE_PACKAGE, VERSION_0_5)
		);
	}

	@ParameterizedTest
	@MethodSource("findPackageAssets_duplicateResourcesParams")
	void findPackageAssets_duplicateResources(String thePackageId, @Nullable String theVersionId) {

		final FindPackageAssetsRequest request =
			FindPackageAssetsRequest.withVersion(
				FhirVersionEnum.R4,
				MEASURE_URL,
				thePackageId,
				theVersionId);

		final IBaseResource resource = myPackageCacheManager.findPackageAssets(request);

		assertThat(resource).isNotNull().isInstanceOf(Measure.class);

		if (resource instanceof Measure measure) {
			assertThat(measure.getUrl()).isEqualTo(MEASURE_URL);
			assertThat(measure.getStructureFhirVersionEnum()).isEqualTo(FhirVersionEnum.R4);
		} else {
			fail("Expected Measure resource, but got: " + resource.getClass().getName());
		}
	}
}
