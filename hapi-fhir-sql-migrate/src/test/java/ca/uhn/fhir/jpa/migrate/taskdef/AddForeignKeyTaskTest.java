package ca.uhn.fhir.jpa.migrate.taskdef;

import ca.uhn.fhir.jpa.migrate.HapiMigrationException;
import ca.uhn.fhir.jpa.migrate.JdbcUtils;
import ca.uhn.fhir.jpa.migrate.tasks.api.BaseMigrationTasks;
import ca.uhn.fhir.jpa.migrate.tasks.api.Builder;
import ca.uhn.fhir.util.VersionEnum;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.sql.SQLException;
import java.util.List;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AddForeignKeyTaskTest extends BaseTest {

	@ParameterizedTest(name = "{index}: {0}")
	@MethodSource("data")
	public void testAddForeignKey(Supplier<TestDatabaseDetails> theTestDatabaseDetails) throws SQLException {
		before(theTestDatabaseDetails);

		executeSql("create table HOME (PID bigint not null, TEXTCOL varchar(255), primary key (PID))");
		executeSql("create table FOREIGNTBL (PID bigint not null, HOMEREF bigint)");
		assertThat(JdbcUtils.getForeignKeys(getConnectionProperties(), "HOME", "FOREIGNTBL")).isEmpty();

		AddForeignKeyTask task = new AddForeignKeyTask("1", "1");
		task.setTableName("FOREIGNTBL");
		task.setColumnNames(List.of("HOMEREF"));
		task.setConstraintName("FK_HOME_FOREIGN");
		task.setForeignColumnNames(List.of("PID"));
		task.setForeignTableName("HOME");
		getMigrator().addTask(task);

		getMigrator().migrate();

		assertThat(JdbcUtils.getForeignKeys(getConnectionProperties(), "HOME", "FOREIGNTBL")).containsExactly("FK_HOME_FOREIGN");

		// Make sure additional calls don't crash
		getMigrator().migrate();
		getMigrator().migrate();
	}

	@ParameterizedTest(name = "{index}: {0}")
	@MethodSource("data")
	public void addForeignKey_withDeleteCascade_worksAndIsRerunable(Supplier<TestDatabaseDetails> theTestDatabaseDetails) throws SQLException {
		// setup
		before(theTestDatabaseDetails);

		executeSql("CREATE TABLE CUSTOMERS (ID int not null, NAME varchar(255), primary key (ID))");
		executeSql("CREATE TABLE ORDERS (ID int not null, CUSTOMERID int)");
		// no current fks
		assertThat(JdbcUtils.getForeignKeys(getConnectionProperties(), "CUSTOMERS", "ORDERS")).isEmpty();

		// test
		AddForeignKeyTask task = new AddForeignKeyTask("1", "1");
		task.setTableName("ORDERS");
		task.setColumnNames(List.of("CUSTOMERID"));
		task.setConstraintName("FK_CO_FOREIGN");
		task.setForeignColumnNames(List.of("ID"));
		task.setForeignTableName("CUSTOMERS");
		task.withDeleteCascade();
		getMigrator().addTask(task);

		getMigrator().migrate();

		// validate
		assertThat(JdbcUtils.getForeignKeys(getConnectionProperties(), "CUSTOMERS", "ORDERS"))
			.containsExactly("FK_CO_FOREIGN");

		// Make sure additional calls don't crash
		getMigrator().migrate();
		getMigrator().migrate();
	}

	@ParameterizedTest(name = "{index}: {0}")
	@MethodSource("data")
	public void addForeignKey_withoutDeleteCascadeWhenExistsWith_throws(Supplier<TestDatabaseDetails> theTestDatabaseDetails) {
		// setup
		before(theTestDatabaseDetails);

		executeSql("CREATE TABLE CUSTOMERS (ID int not null, NAME varchar(255), primary key (ID))");
		executeSql("CREATE TABLE ORDERS (ID int not null, CUSTOMERID int)");
		executeSql("ALTER TABLE ORDERS ADD CONSTRAINT FK_CO_FOREIGN FOREIGN KEY (CUSTOMERID) REFERENCES CUSTOMERS (ID) ON DELETE CASCADE");

		// add a migration that adds foreign key but not with delete cascade
		getMigrator()
			.addTasks(
				new MyMigrationTasks(VersionEnum.V3_4_0, false).getTaskList(VersionEnum.V3_3_0, VersionEnum.V3_4_0)
			);

		assertThatThrownBy(() -> getMigrator().migrate())
			.isInstanceOf(HapiMigrationException.class)
			.getCause().hasMessageContaining("Can not add foreign key FK_CO_FOREIGN");
	}

	@ParameterizedTest(name = "{index}: {0}")
	@MethodSource("data")
	public void addForeignKey_multipleMigrationsWithSameFKbutDifferentCascadeOptions_shouldFail(Supplier<TestDatabaseDetails> theTestDatabaseDetails) {
		// setup
		before(theTestDatabaseDetails);

		executeSql("CREATE TABLE CUSTOMERS (ID int not null, NAME varchar(255), primary key (ID))");
		executeSql("CREATE TABLE ORDERS (ID int not null, CUSTOMERID int)");

		// add one migration
		getMigrator()
			.addTasks(
				new MyMigrationTasks(VersionEnum.V3_4_0, false).getTaskList(VersionEnum.V3_3_0, VersionEnum.V3_4_0)
			);
		getMigrator().migrate();

		getMigrator()
			.addTasks(
				new MyMigrationTasks(VersionEnum.V3_5_0, true).getTaskList(VersionEnum.V3_4_0, VersionEnum.V3_5_0)
			);

		assertThatThrownBy(() -> getMigrator().migrate())
			.isInstanceOf(HapiMigrationException.class)
			.getCause()
			.hasMessageContaining("Can not add foreign key FK_CO_FOREIGN");
	}

	@ParameterizedTest(name = "{index}: {0}")
	@MethodSource("data")
	public void addForeignKey_viaBuilderWithDeleteCascade_cascadesOnParentDelete(Supplier<TestDatabaseDetails> theTestDatabaseDetails) throws SQLException {
		// setup
		before(theTestDatabaseDetails);

		executeSql("CREATE TABLE CUSTOMERS (ID int not null, NAME varchar(255), primary key (ID))");
		executeSql("CREATE TABLE ORDERS (ID int not null, CUSTOMERID int)");

		getMigrator().addTasks(new MyMigrationTasks(VersionEnum.V3_5_0, true).getTaskList(VersionEnum.V3_3_0, VersionEnum.V3_5_0));

		getMigrator().migrate();

		// validate
		assertThat(JdbcUtils.getForeignKeys(getConnectionProperties(), "CUSTOMERS", "ORDERS"))
			.containsExactly("FK_CO_FOREIGN");

		// test and make sure it works
		executeSql("INSERT INTO CUSTOMERS (ID, NAME) VALUES (1, 'foo')");
		executeSql("INSERT INTO ORDERS (ID, CUSTOMERID) VALUES (10, 1)");
		executeSql("DELETE FROM CUSTOMERS WHERE ID = 1");
		assertThat(executeQuery("SELECT ID FROM ORDERS")).isEmpty();
	}

	@ParameterizedTest(name = "{index}: {0}")
	@MethodSource("data")
	public void testAddForeignKey_MultipleColumns(Supplier<TestDatabaseDetails> theTestDatabaseDetails) throws SQLException {
		before(theTestDatabaseDetails);

		executeSql("create table HOME (PID1 bigint not null, PID2 bigint not null, TEXTCOL varchar(255), primary key (PID1, PID2))");
		executeSql("create table FOREIGNTBL (PID bigint not null, HOMEREF1 bigint, HOMEREF2 bigint)");
		assertThat(JdbcUtils.getForeignKeys(getConnectionProperties(), "HOME", "FOREIGNTBL")).isEmpty();

		AddForeignKeyTask task = new AddForeignKeyTask("1", "1");
		task.setTableName("FOREIGNTBL");
		task.setColumnNames(List.of("HOMEREF1", "HOMEREF2"));
		task.setConstraintName("FK_HOME_FOREIGN");
		task.setForeignColumnNames(List.of("PID1", "PID2"));
		task.setForeignTableName("HOME");
		getMigrator().addTask(task);

		getMigrator().migrate();

		assertThat(JdbcUtils.getForeignKeys(getConnectionProperties(), "HOME", "FOREIGNTBL")).containsExactly("FK_HOME_FOREIGN");

		// Make sure additional calls don't crash
		getMigrator().migrate();
		getMigrator().migrate();
	}

	private static class MyMigrationTasks extends BaseMigrationTasks<VersionEnum> {

		public MyMigrationTasks(VersionEnum theVersionEnum, boolean theWithDeleteCascade) {
			Builder v = forVersion(theVersionEnum);
			v.onTable("ORDERS")
				.addForeignKey("1", "FK_CO_FOREIGN")
				.toColumn("CUSTOMERID")
				.withDeleteCascade(theWithDeleteCascade)
				.references("CUSTOMERS", "ID");
		}
	}
}
