package ca.uhn.fhir.jpa.embedded;

import ca.uhn.fhir.jpa.migrate.DriverTypeEnum;
import org.junit.jupiter.api.extension.RegisterExtension;

public class HapiSchemaMigrationMsSqlTest extends BaseHapiSchemaMigrationTest {

	@RegisterExtension
	static HapiEmbeddedDatabasesExtension myEmbeddedServersExtension = 
		HapiEmbeddedDatabasesExtension.forDatabase(DriverTypeEnum.MSSQL_2012);

	@Override
	protected HapiEmbeddedDatabasesExtension getEmbeddedDatabasesExtension() {
		return myEmbeddedServersExtension;
	}

	@Override
	protected DriverTypeEnum getDriverType() {
		return DriverTypeEnum.MSSQL_2012;
	}
}