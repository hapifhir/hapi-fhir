package ca.uhn.fhir.jpa.migrate.taskdef;

import ca.uhn.fhir.jpa.migrate.JdbcUtils;
import com.google.common.collect.Lists;
import org.junit.Test;

import java.sql.SQLException;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;

public class AddIndexTest extends BaseTest {

	@Test
	public void testIndexAlreadyExists() throws SQLException {
		executeSql("create table SOMETABLE (PID bigint not null, TEXTCOL varchar(255))");
		executeSql("create unique index IDX_ANINDEX on SOMETABLE (PID, TEXTCOL)");
		executeSql("create unique index IDX_DIFINDEX on SOMETABLE (TEXTCOL)");

		AddIndexTask task = new AddIndexTask();
		task.setIndexName("IDX_ANINDEX");
		task.setTableName("SOMETABLE");
		task.setColumns("PID", "TEXTCOL");
		task.setUnique(false);
		getMigrator().addTask(task);

		getMigrator().migrate();

		assertThat(JdbcUtils.getIndexNames(getConnectionProperties(), "SOMETABLE"), containsInAnyOrder("IDX_DIFINDEX", "IDX_ANINDEX"));
	}

	@Test
	public void testIndexDoesntAlreadyExist() throws SQLException {
		executeSql("create table SOMETABLE (PID bigint not null, TEXTCOL varchar(255))");
		executeSql("create unique index IDX_DIFINDEX on SOMETABLE (TEXTCOL)");

		AddIndexTask task = new AddIndexTask();
		task.setIndexName("IDX_ANINDEX");
		task.setTableName("SOMETABLE");
		task.setColumns("PID", "TEXTCOL");
		task.setUnique(false);
		getMigrator().addTask(task);

		getMigrator().migrate();

		assertThat(JdbcUtils.getIndexNames(getConnectionProperties(), "SOMETABLE"), containsInAnyOrder("IDX_DIFINDEX", "IDX_ANINDEX"));
	}

}
