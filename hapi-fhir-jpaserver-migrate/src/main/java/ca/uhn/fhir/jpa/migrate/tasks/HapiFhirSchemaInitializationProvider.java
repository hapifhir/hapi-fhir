package ca.uhn.fhir.jpa.migrate.tasks;


import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.jpa.migrate.DriverTypeEnum;

import javax.annotation.Nonnull;

public class HapiFhirSchemaInitializationProvider extends BaseSchemaInitializationProvider {

	@Nonnull
	protected String getSchemaInitializationPath(DriverTypeEnum theDriverType)  {
		String initScript;
		switch (theDriverType) {
			case H2_EMBEDDED:
				initScript = "/ca/uhn/hapi/fhir/jpa/docs/database/h2.sql";
				break;
			case DERBY_EMBEDDED:
				initScript = "/ca/uhn/hapi/fhir/jpa/docs/database/derbytenseven.sql";
				break;
			case MYSQL_5_7:
			case MARIADB_10_1:
				initScript = "/ca/uhn/hapi/fhir/jpa/docs/database/mysql57.sql";
				break;
			case POSTGRES_9_4:
				initScript = "/ca/uhn/hapi/fhir/jpa/docs/database/postgresql92.sql";
				break;
			case ORACLE_12C:
				initScript = "/ca/uhn/hapi/fhir/jpa/docs/database/oracle12c.sql";
				break;
			case MSSQL_2012:
				initScript = "/ca/uhn/hapi/fhir/jpa/docs/database/sqlserver2012.sql";
				break;
			default:
				throw new ConfigurationException("No schema initialization script available for driver " + theDriverType);
		}
		return initScript;
	}
}
