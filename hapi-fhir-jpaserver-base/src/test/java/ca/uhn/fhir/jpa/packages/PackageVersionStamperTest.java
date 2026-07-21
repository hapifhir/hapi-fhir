package ca.uhn.fhir.jpa.packages;

import static org.assertj.core.api.Assertions.assertThat;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.util.UrlUtil;
import java.util.Optional;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.SearchParameter;
import org.junit.jupiter.api.Test;

// Created by Claude Opus 4.6
class PackageVersionStamperTest {

	private static final FhirContext ourCtx = FhirContext.forR4Cached();
	private final PackageVersionStamper myStamper = new PackageVersionStamper(ourCtx);

	@Test
	void stampPackageSource_setsMetaSource() {
		Patient resource = new Patient();
		PackageInstallationSpec spec = new PackageInstallationSpec()
				.setName("my.package")
				.setVersion("1.2.0");

		myStamper.stampPackageSource(resource, spec);

		assertThat(resource.getMeta().getSource()).isEqualTo("my.package|1.2.0");
	}

	@Test
	void stampPackageSource_nullSpec_doesNothing() {
		Patient resource = new Patient();

		myStamper.stampPackageSource(resource, null);

		assertThat(resource.getMeta().getSource()).isNullOrEmpty();
	}

	@Test
	void parsePackageSource_validSource_returnsParts() {
		Patient resource = new Patient();
		resource.getMeta().setSource("my.package|1.2.0");

		Optional<UrlUtil.CanonicalUrlParts> result = myStamper.parsePackageSource(resource);

		assertThat(result).isPresent();
		assertThat(result.get().url()).isEqualTo("my.package");
		assertThat(result.get().versionId()).hasValue("1.2.0");
	}

	@Test
	void parsePackageSource_sourceWithRequestId_returnsParts() {
		Patient resource = new Patient();
		resource.getMeta().setSource("my.package|1.2.0#request-123");

		Optional<UrlUtil.CanonicalUrlParts> result = myStamper.parsePackageSource(resource);

		assertThat(result).isPresent();
		assertThat(result.get().url()).isEqualTo("my.package");
		assertThat(result.get().versionId()).hasValue("1.2.0");
	}

	@Test
	void parsePackageSource_noSource_returnsEmpty() {
		Patient resource = new Patient();

		Optional<UrlUtil.CanonicalUrlParts> result = myStamper.parsePackageSource(resource);

		assertThat(result).isEmpty();
	}

	@Test
	void parsePackageSource_sourceWithoutVersion_returnsEmpty() {
		Patient resource = new Patient();
		resource.getMeta().setSource("my.package");

		Optional<UrlUtil.CanonicalUrlParts> result = myStamper.parsePackageSource(resource);

		assertThat(result).isEmpty();
	}

	@Test
	void isOlderPackageVersion_olderIncoming_returnsTrue() {
		SearchParameter existing = new SearchParameter();
		existing.setId("SearchParameter/sp1");
		existing.getMeta().setSource("my.package|2.0.0");

		PackageInstallationSpec spec = new PackageInstallationSpec()
				.setName("my.package")
				.setVersion("1.0.0");

		assertThat(myStamper.isOlderPackageVersion(spec, existing)).isTrue();
	}

	@Test
	void isOlderPackageVersion_newerIncoming_returnsFalse() {
		SearchParameter existing = new SearchParameter();
		existing.setId("SearchParameter/sp1");
		existing.getMeta().setSource("my.package|1.0.0");

		PackageInstallationSpec spec = new PackageInstallationSpec()
				.setName("my.package")
				.setVersion("2.0.0");

		assertThat(myStamper.isOlderPackageVersion(spec, existing)).isFalse();
	}

	@Test
	void isOlderPackageVersion_sameVersion_returnsFalse() {
		SearchParameter existing = new SearchParameter();
		existing.setId("SearchParameter/sp1");
		existing.getMeta().setSource("my.package|1.0.0");

		PackageInstallationSpec spec = new PackageInstallationSpec()
				.setName("my.package")
				.setVersion("1.0.0");

		assertThat(myStamper.isOlderPackageVersion(spec, existing)).isFalse();
	}

	@Test
	void isOlderPackageVersion_differentPackageName_returnsFalse() {
		SearchParameter existing = new SearchParameter();
		existing.setId("SearchParameter/sp1");
		existing.getMeta().setSource("other.package|5.0.0");

		PackageInstallationSpec spec = new PackageInstallationSpec()
				.setName("my.package")
				.setVersion("1.0.0");

		assertThat(myStamper.isOlderPackageVersion(spec, existing)).isFalse();
	}

	@Test
	void isOlderPackageVersion_noExistingSource_returnsFalse() {
		SearchParameter existing = new SearchParameter();
		existing.setId("SearchParameter/sp1");

		PackageInstallationSpec spec = new PackageInstallationSpec()
				.setName("my.package")
				.setVersion("1.0.0");

		assertThat(myStamper.isOlderPackageVersion(spec, existing)).isFalse();
	}

	@Test
	void isOlderPackageVersion_nullIncomingVersion_returnsFalse() {
		SearchParameter existing = new SearchParameter();
		existing.setId("SearchParameter/sp1");
		existing.getMeta().setSource("my.package|2.0.0");

		PackageInstallationSpec spec = new PackageInstallationSpec()
				.setName("my.package");

		assertThat(myStamper.isOlderPackageVersion(spec, existing)).isFalse();
	}

	@Test
	void isOlderPackageVersion_existingSourceWithRequestId_comparesCorrectly() {
		SearchParameter existing = new SearchParameter();
		existing.setId("SearchParameter/sp1");
		existing.getMeta().setSource("my.package|2.0.0#request-456");

		PackageInstallationSpec spec = new PackageInstallationSpec()
				.setName("my.package")
				.setVersion("1.0.0");

		assertThat(myStamper.isOlderPackageVersion(spec, existing)).isTrue();
	}
}
