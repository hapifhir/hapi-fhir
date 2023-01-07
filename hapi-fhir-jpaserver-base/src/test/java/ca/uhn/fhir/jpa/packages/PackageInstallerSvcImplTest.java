package ca.uhn.fhir.jpa.packages;

import org.elasticsearch.common.inject.Inject;
import org.junit.jupiter.api.Test;

public class PackageInstallerSvcImplTest {

	@Test
	public void testPackageCompatibility() {
		new PackageInstallerSvcImpl().assertFhirVersionsAreCompatible("R4", "R4B");
	}
}
