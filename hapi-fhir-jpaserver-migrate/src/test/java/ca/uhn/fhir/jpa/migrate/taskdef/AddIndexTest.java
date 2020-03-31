package ca.uhn.fhir.jpa.migrate.taskdef;

import ca.uhn.fhir.jpa.migrate.JdbcUtils;
import org.junit.Test;

import java.sql.SQLException;
import java.util.function.Supplier;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.Assert.assertThat;

public class AddIndexTest extends BaseTest {

	public AddIndexTest(Supplier<TestDatabaseDetails> theTestDatabaseDetails) {
		super(theTestDatabaseDetails);
	}

	@Test
	public void testUniqueConstraintAlreadyExists() throws SQLException {
		executeSql("create table SOMETABLE (PID bigint not null, TEXTCOL varchar(255))");
		executeSql("ALTER TABLE SOMETABLE ADD CONSTRAINT IDX_ANINDEX UNIQUE(TEXTCOL)");

		AddIndexTask task = new AddIndexTask("1", "1");
		task.setIndexName("IDX_ANINDEX");
		task.setTableName("SOMETABLE");
		task.setColumns("TEXTCOL");
		task.setUnique(true);
		getMigrator().addTask(task);

		getMigrator().migrate();
		getMigrator().migrate();
		getMigrator().migrate();
		getMigrator().migrate();

		assertThat(JdbcUtils.getIndexNames(getConnectionProperties(), "SOMETABLE"), hasItem("IDX_ANINDEX"));

	}

	@Test
	public void testUniqueIndexAlreadyExists() throws SQLException {
		executeSql("create table SOMETABLE (PID bigint not null, TEXTCOL varchar(255))");
		executeSql("create unique index IDX_ANINDEX on SOMETABLE (PID, TEXTCOL)");
		executeSql("create unique index IDX_DIFINDEX on SOMETABLE (TEXTCOL)");

		AddIndexTask task = new AddIndexTask("1", "1");
		task.setIndexName("IDX_ANINDEX");
		task.setTableName("SOMETABLE");
		task.setColumns("PID", "TEXTCOL");
		task.setUnique(false);
		getMigrator().addTask(task);

		getMigrator().migrate();
		getMigrator().migrate();
		getMigrator().migrate();
		getMigrator().migrate();

		assertThat(JdbcUtils.getIndexNames(getConnectionProperties(), "SOMETABLE"), containsInAnyOrder("IDX_DIFINDEX", "IDX_ANINDEX"));
	}

	@Test
	public void testNonUniqueIndexAlreadyExists() throws SQLException {
		executeSql("create table SOMETABLE (PID bigint not null, TEXTCOL varchar(255))");
		executeSql("create index IDX_ANINDEX on SOMETABLE (PID, TEXTCOL)");
		executeSql("create index IDX_DIFINDEX on SOMETABLE (TEXTCOL)");

		AddIndexTask task = new AddIndexTask("1", "1");
		task.setIndexName("IDX_ANINDEX");
		task.setTableName("SOMETABLE");
		task.setColumns("PID", "TEXTCOL");
		task.setUnique(false);
		getMigrator().addTask(task);

		getMigrator().migrate();
		getMigrator().migrate();
		getMigrator().migrate();
		getMigrator().migrate();

		assertThat(JdbcUtils.getIndexNames(getConnectionProperties(), "SOMETABLE"), containsInAnyOrder("IDX_DIFINDEX", "IDX_ANINDEX"));
	}

	@Test
	public void testIndexDoesntAlreadyExist() throws SQLException {
		executeSql("create table SOMETABLE (PID bigint not null, TEXTCOL varchar(255))");
		executeSql("create unique index IDX_DIFINDEX on SOMETABLE (TEXTCOL)");

		AddIndexTask task = new AddIndexTask("1", "1");
		task.setIndexName("IDX_ANINDEX");
		task.setTableName("SOMETABLE");
		task.setColumns("PID", "TEXTCOL");
		task.setUnique(false);
		getMigrator().addTask(task);

		getMigrator().migrate();

		assertThat(JdbcUtils.getIndexNames(getConnectionProperties(), "SOMETABLE"), containsInAnyOrder("IDX_DIFINDEX", "IDX_ANINDEX"));
	}

}
