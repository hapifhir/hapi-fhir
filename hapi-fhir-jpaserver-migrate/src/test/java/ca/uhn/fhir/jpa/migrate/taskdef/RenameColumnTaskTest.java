package ca.uhn.fhir.jpa.migrate.taskdef;

import ca.uhn.fhir.jpa.migrate.JdbcUtils;
import org.flywaydb.core.api.FlywayException;
import org.junit.Test;

import java.sql.SQLException;
import java.util.Set;
import java.util.function.Supplier;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class RenameColumnTaskTest extends BaseTest {

	public RenameColumnTaskTest(Supplier<TestDatabaseDetails> theTestDatabaseDetails) {
		super(theTestDatabaseDetails);
	}

	@Test
	public void testColumnAlreadyExists() throws SQLException {
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

	@Test
	public void testForeignKeyColumnAlreadyExists_MySql() throws SQLException {
		testForeignKeyColumnAlreadyExists(true);
	}

	private void testForeignKeyColumnAlreadyExists(boolean isMySql) throws SQLException {
		executeSql("create table PARENT (PID bigint not null, TEXTCOL varchar(255), primary key (PID))");
		executeSql("create table CHILD (PID bigint not null, PARENTREF bigint)");
		executeSql("alter table CHILD add constraint FK_MOM foreign key (PARENTREF) references PARENT(PID)");

		assertThat(JdbcUtils.getForeignKeys(getConnectionProperties(), "PARENT", "CHILD"), hasSize(1));

		assertThat(JdbcUtils.getForeignKeysForColumn(getConnectionProperties(), "PARENTREF", "CHILD"), containsInAnyOrder("FK_MOM"));

		RenameColumnTask task = new RenameColumnTask("1",  "1");
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

	@Test
	public void testForeignKeyColumnAlreadyExists_OtherDB() throws SQLException {
		testForeignKeyColumnAlreadyExists(false);
	}

	@Test
	public void testBothExistDeleteTargetFirst() throws SQLException {
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

	@Test
	public void testForeignKeyColumnBothExistDeleteTargetFirst_MySql() throws SQLException {
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

		RenameColumnTask task = new RenameColumnTask("1",  "1");
		task.setTableName("CHILD");
		task.setOldName("PARENTREF");
		task.setNewName("NOKREF");
		task.setDeleteTargetColumnFirstIfBothExist(true);
		task.setSimulateMySQLForTest(isMySql);
		getMigrator().addTask(task);

		getMigrator().migrate();

		assertThat(JdbcUtils.getForeignKeys(getConnectionProperties(), "RELATION", "CHILD"), empty());
		assertThat(JdbcUtils.getForeignKeys(getConnectionProperties(), "PARENT", "CHILD"), hasSize(1));

		assertThat(JdbcUtils.getColumnNames(getConnectionProperties(), "CHILD"), containsInAnyOrder("PID", "NOKREF"));

		assertThat(JdbcUtils.getForeignKeysForColumn(getConnectionProperties(), "NOKREF", "CHILD"), containsInAnyOrder("FK_MOM"));

	}

	@Test
	public void testForeignKeyColumnBothExistDeleteTargetFirst_OtherDB() throws SQLException {
		testForeignKeyColumnBothExistDeleteTargetFirst(false);
	}

	@Test
	public void testBothExistDeleteTargetFirstDataExistsInSourceAndTarget() {
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
			assertEquals("Failure executing task \"Drop an index\", aborting! Cause: java.sql.SQLException: Can not rename SOMETABLE.myTextCol to TEXTCOL because both columns exist and data exists in TEXTCOL", e.getCause().getCause().getMessage());
		}

	}

	@Test
	public void testColumnDoesntAlreadyExist() throws SQLException {
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

	@Test
	public void testForeignKeyColumnDoesntAlreadyExist_MySql() throws SQLException {
		testForeignKeyColumnDoesntAlreadyExist(true);
	}

	private void testForeignKeyColumnDoesntAlreadyExist(boolean isMySql) throws SQLException {
		executeSql("create table PARENT (PARENTID bigint not null, TEXTCOL varchar(255), primary key (PARENTID))");
		executeSql("create table CHILD (PID bigint not null, PARENTREF bigint)");
		executeSql("alter table CHILD add constraint FK_MOM foreign key (PARENTREF) references PARENT(PARENTID)");

		assertThat(JdbcUtils.getForeignKeys(getConnectionProperties(), "PARENT", "CHILD"), hasSize(1));

		assertThat(JdbcUtils.getForeignKeysForColumn(getConnectionProperties(), "PARENTREF", "CHILD"), containsInAnyOrder("FK_MOM"));

		RenameColumnTask task = new RenameColumnTask("1",  "1");
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

	@Test
	public void testForeignKeyColumnDoesntAlreadyExist_OtherDB() throws SQLException {
		testForeignKeyColumnDoesntAlreadyExist(false);
	}

	@Test
	public void testNeitherColumnExists() {
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
			assertEquals("Failure executing task \"RenameColumnTask\", aborting! Cause: java.sql.SQLException: Can not rename SOMETABLE.myTextCol to TEXTCOL because neither column exists!", e.getCause().getCause().getMessage());
		}


	}

	@Test
	public void testNeitherColumnExistsButAllowed() {
		executeSql("create table SOMETABLE (PID bigint not null)");

		RenameColumnTask task = new RenameColumnTask("1", "1");
		task.setTableName("SOMETABLE");
		task.setOldName("myTextCol");
		task.setNewName("TEXTCOL");
		task.setOkayIfNeitherColumnExists(true);
		getMigrator().addTask(task);

		getMigrator().migrate();
	}

	@Test
	public void testBothColumnsExist() {
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
			assertEquals("Failure executing task \"RenameColumnTask\", aborting! Cause: java.sql.SQLException: Can not rename SOMETABLE.PID to PID2 because both columns exist!", e.getCause().getCause().getMessage());
		}


	}

}
