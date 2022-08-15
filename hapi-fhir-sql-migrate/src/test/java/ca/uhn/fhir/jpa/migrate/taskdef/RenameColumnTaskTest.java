package ca.uhn.fhir.jpa.migrate.taskdef;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.migrate.JdbcUtils;
import org.flywaydb.core.api.FlywayException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.sql.SQLException;
import java.util.Set;
import java.util.function.Supplier;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class RenameColumnTaskTest extends BaseTest {

	@ParameterizedTest(name = "{index}: {0}")
	@MethodSource("data")
	public void testColumnAlreadyExists(Supplier<TestDatabaseDetails> theTestDatabaseDetails) throws SQLException {
		before(theTestDatabaseDetails);

		executeSql("create table SOMETABLE (PID bigint not null, TEXTCOL varchar(255))");

		RenameColumnTask task = new RenameColumnTask("1", "1");
		task.setTableName("SOMETABLE");
		task.setDescription("Drop an index");
		task.setOldName("myTextCol");
		task.setNewName("TEXTCOL");
		getMigrator().addTask(task);

		getMigrator().migrate();

		assertThat(JdbcUtils.getColumnNames(getConnectionProperties(), "SOMETABLE"), containsInAnyOrder("PID", "TEXTCOL"));
	}

	@ParameterizedTest(name = "{index}: {0}")
	@MethodSource("data")
	public void testForeignKeyColumnAlreadyExists_MySql(Supplier<TestDatabaseDetails> theTestDatabaseDetails) throws SQLException {
		before(theTestDatabaseDetails);

		testForeignKeyColumnAlreadyExists(true);
	}

	private void testForeignKeyColumnAlreadyExists(boolean isMySql) throws SQLException {
		executeSql("create table PARENT (PID bigint not null, TEXTCOL varchar(255), primary key (PID))");
		executeSql("create table CHILD (PID bigint not null, PARENTREF bigint)");
		executeSql("alter table CHILD add constraint FK_MOM foreign key (PARENTREF) references PARENT(PID)");

		assertThat(JdbcUtils.getForeignKeys(getConnectionProperties(), "PARENT", "CHILD"), hasSize(1));

		assertThat(JdbcUtils.getForeignKeysForColumn(getConnectionProperties(), "PARENTREF", "CHILD"), containsInAnyOrder("FK_MOM"));

		RenameColumnTask task = new RenameColumnTask("1", "1");
		task.setTableName("CHILD");
		task.setOldName("myParentRef");
		task.setNewName("PARENTREF");
		task.setSimulateMySQLForTest(isMySql);
		getMigrator().addTask(task);

		getMigrator().migrate();

		assertThat(JdbcUtils.getForeignKeys(getConnectionProperties(), "PARENT", "CHILD"), hasSize(1));

		assertThat(JdbcUtils.getColumnNames(getConnectionProperties(), "CHILD"), containsInAnyOrder("PID", "PARENTREF"));

		assertThat(JdbcUtils.getForeignKeysForColumn(getConnectionProperties(), "PARENTREF", "CHILD"), containsInAnyOrder("FK_MOM"));
	}

	@ParameterizedTest(name = "{index}: {0}")
	@MethodSource("data")
	public void testForeignKeyColumnAlreadyExists_OtherDB(Supplier<TestDatabaseDetails> theTestDatabaseDetails) throws SQLException {
		before(theTestDatabaseDetails);

		testForeignKeyColumnAlreadyExists(false);
	}

	@ParameterizedTest(name = "{index}: {0}")
	@MethodSource("data")
	public void testBothExistDeleteTargetFirst(Supplier<TestDatabaseDetails> theTestDatabaseDetails) throws SQLException {
		before(theTestDatabaseDetails);

		executeSql("create table SOMETABLE (PID bigint not null, TEXTCOL varchar(255), myTextCol varchar(255))");

		RenameColumnTask task = new RenameColumnTask("1", "1");
		task.setTableName("SOMETABLE");
		task.setDescription("Drop an index");
		task.setOldName("myTextCol");
		task.setNewName("TEXTCOL");
		task.setDeleteTargetColumnFirstIfBothExist(true);
		getMigrator().addTask(task);

		getMigrator().migrate();

		Set<String> columnNames = JdbcUtils.getColumnNames(getConnectionProperties(), "SOMETABLE");
		assertThat(columnNames.toString(), columnNames, containsInAnyOrder("PID", "TEXTCOL"));
	}

	@ParameterizedTest(name = "{index}: {0}")
	@MethodSource("data")
	public void testForeignKeyColumnBothExistDeleteTargetFirst_MySql(Supplier<TestDatabaseDetails> theTestDatabaseDetails) throws SQLException {
		before(theTestDatabaseDetails);

		testForeignKeyColumnBothExistDeleteTargetFirst(true);
	}

	private void testForeignKeyColumnBothExistDeleteTargetFirst(boolean isMySql) throws SQLException {
		executeSql("create table PARENT (PARENTID bigint not null, TEXTCOL varchar(255), primary key (PARENTID))");
		executeSql("create table RELATION (RELATIONID bigint not null, TEXTCOL varchar(255), primary key (RELATIONID))");
		executeSql("create table CHILD (PID bigint not null, PARENTREF bigint, NOKREF bigint)");
		executeSql("alter table CHILD add constraint FK_MOM foreign key (PARENTREF) references PARENT(PARENTID)");
		executeSql("alter table CHILD add constraint FK_NOK foreign key (NOKREF) references RELATION(RELATIONID)");

		assertThat(JdbcUtils.getForeignKeys(getConnectionProperties(), "PARENT", "CHILD"), hasSize(1));
		assertThat(JdbcUtils.getForeignKeys(getConnectionProperties(), "RELATION", "CHILD"), hasSize(1));

		assertThat(JdbcUtils.getForeignKeysForColumn(getConnectionProperties(), "PARENTREF", "CHILD"), containsInAnyOrder("FK_MOM"));
		assertThat(JdbcUtils.getForeignKeysForColumn(getConnectionProperties(), "NOKREF", "CHILD"), containsInAnyOrder("FK_NOK"));

		RenameColumnTask task = new RenameColumnTask("1", "1");
		task.setTableName("CHILD");
		task.setOldName("PARENTREF");
		task.setNewName("NOKREF");
		task.setDeleteTargetColumnFirstIfBothExist(true);
		task.setSimulateMySQLForTest(isMySql);
		getMigrator().addTask(task);

		getMigrator().migrate();

		assertThat(JdbcUtils.getForeignKeys(getConnectionProperties(), "RELATION", "CHILD"), hasSize(0));
		assertThat(JdbcUtils.getForeignKeys(getConnectionProperties(), "PARENT", "CHILD"), hasSize(1));

		assertThat(JdbcUtils.getColumnNames(getConnectionProperties(), "CHILD"), containsInAnyOrder("PID", "NOKREF"));

		assertThat(JdbcUtils.getForeignKeysForColumn(getConnectionProperties(), "NOKREF", "CHILD"), containsInAnyOrder("FK_MOM"));

	}

	@ParameterizedTest(name = "{index}: {0}")
	@MethodSource("data")
	public void testForeignKeyColumnBothExistDeleteTargetFirst_OtherDB(Supplier<TestDatabaseDetails> theTestDatabaseDetails) throws SQLException {
		before(theTestDatabaseDetails);

		testForeignKeyColumnBothExistDeleteTargetFirst(false);
	}

	@ParameterizedTest(name = "{index}: {0}")
	@MethodSource("data")
	public void testBothExistDeleteTargetFirstDataExistsInSourceAndTarget(Supplier<TestDatabaseDetails> theTestDatabaseDetails) {
		before(theTestDatabaseDetails);

		executeSql("create table SOMETABLE (PID bigint not null, TEXTCOL varchar(255), myTextCol varchar(255))");
		executeSql("INSERT INTO SOMETABLE (PID, TEXTCOL, myTextCol) VALUES (123, 'AAA', 'BBB')");

		RenameColumnTask task = new RenameColumnTask("1", "1");
		task.setTableName("SOMETABLE");
		task.setDescription("Drop an index");
		task.setOldName("myTextCol");
		task.setNewName("TEXTCOL");
		task.setDeleteTargetColumnFirstIfBothExist(true);
		getMigrator().addTask(task);

		try {
			getMigrator().migrate();
			fail();
		} catch (FlywayException e) {
			assertEquals(Msg.code(47) + "Failure executing task \"Drop an index\", aborting! Cause: java.sql.SQLException: "+ Msg.code(54) + "Can not rename SOMETABLE.myTextCol to TEXTCOL because both columns exist and data exists in TEXTCOL", e.getCause().getCause().getMessage());
		}

	}

	@ParameterizedTest(name = "{index}: {0}")
	@MethodSource("data")
	public void testColumnDoesntAlreadyExist(Supplier<TestDatabaseDetails> theTestDatabaseDetails) throws SQLException {
		before(theTestDatabaseDetails);

		executeSql("create table SOMETABLE (PID bigint not null, myTextCol varchar(255))");

		RenameColumnTask task = new RenameColumnTask("1", "1");
		task.setTableName("SOMETABLE");
		task.setDescription("Drop an index");
		task.setOldName("myTextCol");
		task.setNewName("TEXTCOL");
		getMigrator().addTask(task);

		getMigrator().migrate();

		assertThat(JdbcUtils.getColumnNames(getConnectionProperties(), "SOMETABLE"), containsInAnyOrder("PID", "TEXTCOL"));
	}

	@ParameterizedTest(name = "{index}: {0}")
	@MethodSource("data")
	public void testForeignKeyColumnDoesntAlreadyExist_MySql(Supplier<TestDatabaseDetails> theTestDatabaseDetails) throws SQLException {
		before(theTestDatabaseDetails);

		testForeignKeyColumnDoesntAlreadyExist(true);
	}

	private void testForeignKeyColumnDoesntAlreadyExist(boolean isMySql) throws SQLException {
		executeSql("create table PARENT (PARENTID bigint not null, TEXTCOL varchar(255), primary key (PARENTID))");
		executeSql("create table CHILD (PID bigint not null, PARENTREF bigint)");
		executeSql("alter table CHILD add constraint FK_MOM foreign key (PARENTREF) references PARENT(PARENTID)");

		assertThat(JdbcUtils.getForeignKeys(getConnectionProperties(), "PARENT", "CHILD"), hasSize(1));

		assertThat(JdbcUtils.getForeignKeysForColumn(getConnectionProperties(), "PARENTREF", "CHILD"), containsInAnyOrder("FK_MOM"));

		RenameColumnTask task = new RenameColumnTask("1", "1");
		task.setTableName("CHILD");
		task.setOldName("PARENTREF");
		task.setNewName("MOMREF");
		task.setSimulateMySQLForTest(isMySql);
		getMigrator().addTask(task);

		getMigrator().migrate();

		assertThat(JdbcUtils.getForeignKeys(getConnectionProperties(), "PARENT", "CHILD"), hasSize(1));

		assertThat(JdbcUtils.getColumnNames(getConnectionProperties(), "CHILD"), containsInAnyOrder("PID", "MOMREF"));

		assertThat(JdbcUtils.getForeignKeysForColumn(getConnectionProperties(), "MOMREF", "CHILD"), containsInAnyOrder("FK_MOM"));

	}

	@ParameterizedTest(name = "{index}: {0}")
	@MethodSource("data")
	public void testForeignKeyColumnDoesntAlreadyExist_OtherDB(Supplier<TestDatabaseDetails> theTestDatabaseDetails) throws SQLException {
		before(theTestDatabaseDetails);

		testForeignKeyColumnDoesntAlreadyExist(false);
	}

	@ParameterizedTest(name = "{index}: {0}")
	@MethodSource("data")
	public void testNeitherColumnExists(Supplier<TestDatabaseDetails> theTestDatabaseDetails) {
		before(theTestDatabaseDetails);

		executeSql("create table SOMETABLE (PID bigint not null)");

		RenameColumnTask task = new RenameColumnTask("1", "1");
		task.setTableName("SOMETABLE");
		task.setOldName("myTextCol");
		task.setNewName("TEXTCOL");
		getMigrator().addTask(task);

		try {
			getMigrator().migrate();
			fail();
		} catch (FlywayException e) {
			assertEquals(Msg.code(47) + "Failure executing task \"RenameColumnTask\", aborting! Cause: java.sql.SQLException: "+ Msg.code(56) + "Can not rename SOMETABLE.myTextCol to TEXTCOL because neither column exists!", e.getCause().getCause().getMessage());
		}


	}

	@ParameterizedTest(name = "{index}: {0}")
	@MethodSource("data")
	public void testNeitherColumnExistsButAllowed(Supplier<TestDatabaseDetails> theTestDatabaseDetails) {
		before(theTestDatabaseDetails);

		executeSql("create table SOMETABLE (PID bigint not null)");

		RenameColumnTask task = new RenameColumnTask("1", "1");
		task.setTableName("SOMETABLE");
		task.setOldName("myTextCol");
		task.setNewName("TEXTCOL");
		task.setOkayIfNeitherColumnExists(true);
		getMigrator().addTask(task);

		getMigrator().migrate();
	}

	@ParameterizedTest(name = "{index}: {0}")
	@MethodSource("data")
	public void testBothColumnsExist(Supplier<TestDatabaseDetails> theTestDatabaseDetails) {
		before(theTestDatabaseDetails);

		executeSql("create table SOMETABLE (PID bigint not null, PID2 bigint)");

		RenameColumnTask task = new RenameColumnTask("1", "1");
		task.setTableName("SOMETABLE");
		task.setOldName("PID");
		task.setNewName("PID2");
		getMigrator().addTask(task);

		try {
			getMigrator().migrate();
			fail();
		} catch (FlywayException e) {
			assertEquals(Msg.code(47) + "Failure executing task \"RenameColumnTask\", aborting! Cause: java.sql.SQLException: "+ Msg.code(55) + "Can not rename SOMETABLE.PID to PID2 because both columns exist!", e.getCause().getCause().getMessage());
		}


	}

}
