package ca.uhn.fhir.jpa.migrate;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.migrate.entity.HapiMigrationEntity;
import ca.uhn.fhir.jpa.migrate.taskdef.AddTableRawSqlTask;
import ca.uhn.fhir.jpa.migrate.taskdef.BaseTask;
import ca.uhn.fhir.jpa.migrate.taskdef.BaseTest;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import org.hamcrest.Matchers;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import javax.annotation.Nonnull;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.function.Supplier;

import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class SchemaMigratorTest extends BaseTest {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(SchemaMigratorTest.class);


	@ParameterizedTest(name = "{index}: {0}")
	@MethodSource("data")
	public void testMigrationRequired(Supplier<TestDatabaseDetails> theTestDatabaseDetails) {
		before(theTestDatabaseDetails);

		SchemaMigrator schemaMigrator = createTableMigrator();

		try {
			schemaMigrator.validate();
			fail();
		} catch (ConfigurationException e) {
			assertThat(e.getMessage(), startsWith(Msg.code(27) + "The database schema for "));
			assertThat(e.getMessage(), endsWith(" is out of date.  Current database schema version is unknown.  Schema version required by application is 1.1.  Please run the database migrator."));
		}

		schemaMigrator.migrate();

		schemaMigrator.validate();
	}


	@ParameterizedTest(name = "{index}: {0}")
	@MethodSource("data")
	public void testRepairFailedMigration(Supplier<TestDatabaseDetails> theTestDatabaseDetails) {
		before(theTestDatabaseDetails);
		SchemaMigrator schemaMigrator = createSchemaMigrator("SOMETABLE", "create fable SOMETABLE (PID bigint not null, TEXTCOL varchar(255))", "1");
		try {
			schemaMigrator.migrate();
			fail();
		} catch (HapiMigrationException e) {
			assertEquals(org.springframework.jdbc.BadSqlGrammarException.class, e.getCause().getCause().getClass());
			MigrationResult failedResult = e.getResult();
			assertEquals(0, failedResult.changes);
			assertEquals(0, failedResult.succeededTasks.size());
			assertEquals(1, failedResult.failedTasks.size());
			assertEquals(0, failedResult.executedStatements.size());

			assertThat(myHapiMigrationDao.findAll(), hasSize(1));
		}
		schemaMigrator = createTableMigrator();

		MigrationResult result = schemaMigrator.migrate();
		assertEquals(0, result.changes);
		assertEquals(1, result.succeededTasks.size());
		assertEquals(0, result.failedTasks.size());
		assertEquals(1, result.executedStatements.size());

		List<HapiMigrationEntity> entities = myHapiMigrationDao.findAll();
		assertThat(entities, hasSize(2));
		assertEquals(1, entities.get(0).getPid());
		assertEquals(false, entities.get(0).getSuccess());
		assertEquals(2, entities.get(1).getPid());
		assertEquals(true, entities.get(1).getSuccess());
	}

	@ParameterizedTest(name = "{index}: {0}")
	@MethodSource("data")
	public void testSkipSchemaVersion(Supplier<TestDatabaseDetails> theTestDatabaseDetails) throws SQLException {
		before(theTestDatabaseDetails);

		AddTableRawSqlTask taskA = new AddTableRawSqlTask("V4_1_0", "20191214.1");
		taskA.setTableName("SOMETABLE_A");
		taskA.addSql(getDriverType(), "create table SOMETABLE_A (PID bigint not null, TEXTCOL varchar(255))");

		AddTableRawSqlTask taskB = new AddTableRawSqlTask("V4_1_0", "20191214.2");
		taskB.setTableName("SOMETABLE_B");
		taskB.addSql(getDriverType(), "create table SOMETABLE_B (PID bigint not null, TEXTCOL varchar(255))");

		AddTableRawSqlTask taskC = new AddTableRawSqlTask("V4_1_0", "20191214.3");
		taskC.setTableName("SOMETABLE_C");
		taskC.addSql(getDriverType(), "create table SOMETABLE_C (PID bigint not null, TEXTCOL varchar(255))");

		AddTableRawSqlTask taskD = new AddTableRawSqlTask("V4_1_0", "20191214.4");
		taskD.setTableName("SOMETABLE_D");
		taskD.addSql(getDriverType(), "create table SOMETABLE_D (PID bigint not null, TEXTCOL varchar(255))");

		MigrationTaskList taskList = new MigrationTaskList(ImmutableList.of(taskA, taskB, taskC, taskD));
		taskList.setDoNothingOnSkippedTasks("4.1.0.20191214.2, 4.1.0.20191214.4");
		SchemaMigrator schemaMigrator = new SchemaMigrator(getUrl(), SchemaMigrator.HAPI_FHIR_MIGRATION_TABLENAME, getDataSource(), new Properties(), taskList, myHapiMigrationStorageSvc);
		schemaMigrator.setDriverType(getDriverType());

		schemaMigrator.migrate();

		DriverTypeEnum.ConnectionProperties connectionProperties = super.getDriverType().newConnectionProperties(getDataSource().getUrl(), getDataSource().getUsername(), getDataSource().getPassword());
		Set<String> tableNames = JdbcUtils.getTableNames(connectionProperties);
		assertThat(tableNames, Matchers.containsInAnyOrder("SOMETABLE_A", "SOMETABLE_C"));
	}

	@Nonnull
	private SchemaMigrator createTableMigrator() {
		return createSchemaMigrator("SOMETABLE", "create table SOMETABLE (PID bigint not null, TEXTCOL varchar(255))", "1");
	}

	@Nonnull
	private SchemaMigrator createSchemaMigrator(String theTableName, String theSql, String theSchemaVersion) {
		AddTableRawSqlTask task = createAddTableTask(theTableName, theSql, theSchemaVersion);
		return createSchemaMigrator(task);
	}

	@Nonnull
	private SchemaMigrator createSchemaMigrator(BaseTask... tasks) {
		MigrationTaskList taskList = new MigrationTaskList(Lists.newArrayList(tasks));
		SchemaMigrator retVal = new SchemaMigrator(getUrl(), SchemaMigrator.HAPI_FHIR_MIGRATION_TABLENAME, getDataSource(), new Properties(), taskList, myHapiMigrationStorageSvc);
		retVal.setDriverType(getDriverType());
		return retVal;
	}

	@Nonnull
	private AddTableRawSqlTask createAddTableTask(String theTableName, String theSql, String theSchemaVersion) {
		AddTableRawSqlTask task = new AddTableRawSqlTask("1", theSchemaVersion);
		task.setTableName(theTableName);
		task.addSql(getDriverType(), theSql);
		return task;
	}
}
