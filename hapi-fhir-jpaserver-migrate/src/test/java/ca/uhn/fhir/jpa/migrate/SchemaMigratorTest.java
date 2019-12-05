package ca.uhn.fhir.jpa.migrate;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.jpa.migrate.taskdef.AddTableRawSqlTask;
import ca.uhn.fhir.jpa.migrate.taskdef.BaseTest;
import com.google.common.collect.ImmutableList;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentMatchers;

import java.sql.SQLException;
import java.util.Collections;
import java.util.Properties;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.contains;

public class SchemaMigratorTest extends BaseTest {

	@Test
	public void testMigrationRequired() {
		AddTableRawSqlTask task = new AddTableRawSqlTask("1", "1");
		task.setTableName("SOMETABLE");
		task.addSql(DriverTypeEnum.H2_EMBEDDED, "create table SOMETABLE (PID bigint not null, TEXTCOL varchar(255))");

		SchemaMigrator schemaMigrator = new SchemaMigrator(SchemaMigrator.HAPI_FHIR_MIGRATION_TABLENAME, getDataSource(), new Properties(), ImmutableList.of(task));
		schemaMigrator.setDriverType(DriverTypeEnum.H2_EMBEDDED);

		try {
			schemaMigrator.validate();
			fail();
		} catch (ConfigurationException e) {
			assertEquals("The database schema for " + getUrl() + " is out of date.  Current database schema version is unknown.  Schema version required by application is " + task.getFlywayVersion() + ".  Please run the database migrator.", e.getMessage());
		}

		schemaMigrator.migrate();

		schemaMigrator.validate();
	}


	@Test
	public void testMigrationRequiredNoFlyway() throws SQLException {
		AddTableRawSqlTask task = new AddTableRawSqlTask("1", "1");
		task.setTableName("SOMETABLE");
		task.addSql(DriverTypeEnum.H2_EMBEDDED, "create table SOMETABLE (PID bigint not null, TEXTCOL varchar(255))");

		SchemaMigrator schemaMigrator = new SchemaMigrator(SchemaMigrator.HAPI_FHIR_MIGRATION_TABLENAME, getDataSource(), new Properties(), ImmutableList.of(task));
		schemaMigrator.setDriverType(DriverTypeEnum.H2_EMBEDDED);
		schemaMigrator.setDontUseFlyway(true);

		// Validate shouldn't fail if we aren't using Flyway
		schemaMigrator.validate();

		schemaMigrator.migrate();

		schemaMigrator.validate();

		DriverTypeEnum.ConnectionProperties connectionProperties = DriverTypeEnum.H2_EMBEDDED.newConnectionProperties(getDataSource().getUrl(), getDataSource().getUsername(), getDataSource().getPassword());
		Set<String> tableNames = JdbcUtils.getTableNames(connectionProperties);
		assertThat(tableNames, Matchers.contains("SOMETABLE"));

	}

}
