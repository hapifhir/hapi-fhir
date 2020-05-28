package ca.uhn.fhir.jpa.migrate.providers;

import ca.uhn.fhir.jpa.migrate.DriverTypeEnum;
import ca.uhn.fhir.jpa.migrate.tasks.SchemaInitializationProvider;
import com.google.common.collect.Maps;

import javax.annotation.Nonnull;
import java.util.Map;

public class SpringBatchSchemaMigrationProvider extends SchemaInitializationProvider {

	private static final Map<DriverTypeEnum, String> ourBatchDriverMap;

	static {
		ourBatchDriverMap = Maps.newHashMap();
		ourBatchDriverMap.put(DriverTypeEnum.H2_EMBEDDED, "schema-h2.sql");
		ourBatchDriverMap.put(DriverTypeEnum.DERBY_EMBEDDED, "schema-derby.sql");
		ourBatchDriverMap.put(DriverTypeEnum.MARIADB_10_1, "schema-mysql.sql");//not sure if correct
		ourBatchDriverMap.put(DriverTypeEnum.MYSQL_5_7, "schema-mysql.sql");//not sure if correct
		ourBatchDriverMap.put(DriverTypeEnum.POSTGRES_9_4, "schema-postgresql.sql");
		ourBatchDriverMap.put(DriverTypeEnum.ORACLE_12C, "schema-oracle10g.sql");//not sure if correct
		ourBatchDriverMap.put(DriverTypeEnum.MSSQL_2012, "schema-mysql.sql");//not sure if correct
	}

	public SpringBatchSchemaMigrationProvider() {
		super("Spring Batch", "/org/springframework/batch/core", "BATCH_JOB_INSTANCE");

	}

	@Nonnull
	@Override
	protected String getInitScript(DriverTypeEnum theDriverType) {
		return ourBatchDriverMap.get(theDriverType);
	}
}
