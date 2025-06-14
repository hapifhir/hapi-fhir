package ca.uhn.fhir.jpa.embedded;

import ca.uhn.fhir.jpa.migrate.DriverTypeEnum;
import org.junit.jupiter.api.extension.RegisterExtension;

public class HapiSchemaMigrationOracleTest extends BaseHapiSchemaMigrationTest {

	@RegisterExtension
	static HapiEmbeddedDatabasesExtension myEmbeddedServersExtension = 
		HapiEmbeddedDatabasesExtension.forDatabase(DriverTypeEnum.ORACLE_12C);

	@Override
	protected HapiEmbeddedDatabasesExtension getEmbeddedDatabasesExtension() {
		return myEmbeddedServersExtension;
	}

	@Override
	protected DriverTypeEnum getDriverType() {
		return DriverTypeEnum.ORACLE_12C;
	}
}