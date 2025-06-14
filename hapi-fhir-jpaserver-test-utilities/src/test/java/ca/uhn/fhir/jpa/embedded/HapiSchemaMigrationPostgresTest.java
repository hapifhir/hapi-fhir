package ca.uhn.fhir.jpa.embedded;

import ca.uhn.fhir.jpa.migrate.DriverTypeEnum;
import org.junit.jupiter.api.extension.RegisterExtension;

public class HapiSchemaMigrationPostgresTest extends BaseHapiSchemaMigrationTest {

	@RegisterExtension
	static HapiEmbeddedDatabasesExtension myEmbeddedServersExtension = 
		HapiEmbeddedDatabasesExtension.forDatabase(DriverTypeEnum.POSTGRES_9_4);

	@Override
	protected HapiEmbeddedDatabasesExtension getEmbeddedDatabasesExtension() {
		return myEmbeddedServersExtension;
	}

	@Override
	protected DriverTypeEnum getDriverType() {
		return DriverTypeEnum.POSTGRES_9_4;
	}
}