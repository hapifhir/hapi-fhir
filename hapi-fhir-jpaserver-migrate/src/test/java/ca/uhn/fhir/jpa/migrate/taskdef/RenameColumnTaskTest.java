package ca.uhn.fhir.jpa.migrate.taskdef;

import ca.uhn.fhir.jpa.migrate.JdbcUtils;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.junit.Test;

import java.sql.SQLException;
import java.util.Set;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.*;

public class RenameColumnTaskTest extends BaseTest {

	@Test
	public void testColumnAlreadyExists() throws SQLException {
		executeSql("create table SOMETABLE (PID bigint not null, TEXTCOL varchar(255))");

		RenameColumnTask task = new RenameColumnTask();
		task.setTableName("SOMETABLE");
		task.setDescription("Drop an index");
		task.setOldName("myTextCol");
		task.setNewName("TEXTCOL");
		getMigrator().addTask(task);

		getMigrator().migrate();

		assertThat(JdbcUtils.getColumnNames(getConnectionProperties(), "SOMETABLE"), containsInAnyOrder("PID", "TEXTCOL"));
	}

	@Test
	public void testBothExistDeleteTargetFirst() throws SQLException {
		executeSql("create table SOMETABLE (PID bigint not null, TEXTCOL varchar(255), myTextCol varchar(255))");

		RenameColumnTask task = new RenameColumnTask();
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
	public void testBothExistDeleteTargetFirstDataExistsInSourceAndTarget() throws SQLException {
		executeSql("create table SOMETABLE (PID bigint not null, TEXTCOL varchar(255), myTextCol varchar(255))");
		executeSql("INSERT INTO SOMETABLE (PID, TEXTCOL, myTextCol) VALUES (123, 'AAA', 'BBB')");

		RenameColumnTask task = new RenameColumnTask();
		task.setTableName("SOMETABLE");
		task.setDescription("Drop an index");
		task.setOldName("myTextCol");
		task.setNewName("TEXTCOL");
		task.setDeleteTargetColumnFirstIfBothExist(true);
		getMigrator().addTask(task);

		try {
			getMigrator().migrate();
			fail();
		} catch (InternalErrorException e) {
			assertEquals("Failure executing task \"Drop an index\", aborting! Cause: java.sql.SQLException: Can not rename SOMETABLE.myTextCol to TEXTCOL because both columns exist and data exists in TEXTCOL", e.getMessage());
		}

	}

	@Test
	public void testColumnDoesntAlreadyExist() throws SQLException {
		executeSql("create table SOMETABLE (PID bigint not null, myTextCol varchar(255))");

		RenameColumnTask task = new RenameColumnTask();
		task.setTableName("SOMETABLE");
		task.setDescription("Drop an index");
		task.setOldName("myTextCol");
		task.setNewName("TEXTCOL");
		getMigrator().addTask(task);

		getMigrator().migrate();

		assertThat(JdbcUtils.getColumnNames(getConnectionProperties(), "SOMETABLE"), containsInAnyOrder("PID", "TEXTCOL"));
	}

	@Test
	public void testNeitherColumnExists() {
		executeSql("create table SOMETABLE (PID bigint not null)");

		RenameColumnTask task = new RenameColumnTask();
		task.setTableName("SOMETABLE");
		task.setOldName("myTextCol");
		task.setNewName("TEXTCOL");
		getMigrator().addTask(task);

		try {
			getMigrator().migrate();
			fail();
		} catch (InternalErrorException e) {
			assertEquals("Failure executing task \"RenameColumnTask\", aborting! Cause: java.sql.SQLException: Can not rename SOMETABLE.myTextCol to TEXTCOL because neither column exists!", e.getMessage());
		}


	}

	@Test
	public void testNeitherColumnExistsButAllowed() {
		executeSql("create table SOMETABLE (PID bigint not null)");

		RenameColumnTask task = new RenameColumnTask();
		task.setTableName("SOMETABLE");
		task.setOldName("myTextCol");
		task.setNewName("TEXTCOL");
		task.setAllowNeitherColumnToExist(true);
		getMigrator().addTask(task);

		getMigrator().migrate();
	}

	@Test
	public void testBothColumnsExist() {
		executeSql("create table SOMETABLE (PID bigint not null, PID2 bigint)");

		RenameColumnTask task = new RenameColumnTask();
		task.setTableName("SOMETABLE");
		task.setOldName("PID");
		task.setNewName("PID2");
		getMigrator().addTask(task);

		try {
			getMigrator().migrate();
			fail();
		} catch (InternalErrorException e) {
			assertEquals("Failure executing task \"RenameColumnTask\", aborting! Cause: java.sql.SQLException: Can not rename SOMETABLE.PID to PID2 because both columns exist!", e.getMessage());
		}


	}

}
