package ca.uhn.fhir.jpa.embedded;

import ca.uhn.fhir.jpa.migrate.DriverTypeEnum;
import org.junit.jupiter.api.extension.RegisterExtension;

public class HapiSchemaMigrationH2Test extends BaseHapiSchemaMigrationTest {

	@RegisterExtension
	static HapiEmbeddedDatabasesExtension myEmbeddedServersExtension = 
		HapiEmbeddedDatabasesExtension.forDatabase(DriverTypeEnum.H2_EMBEDDED);

	@Override
	protected HapiEmbeddedDatabasesExtension getEmbeddedDatabasesExtension() {
		return myEmbeddedServersExtension;
	}

	@Override
	protected DriverTypeEnum getDriverType() {
		return DriverTypeEnum.H2_EMBEDDED;
	}
}