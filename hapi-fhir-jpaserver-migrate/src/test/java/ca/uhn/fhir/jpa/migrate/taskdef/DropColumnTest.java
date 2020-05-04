package ca.uhn.fhir.jpa.migrate.taskdef;

import ca.uhn.fhir.jpa.migrate.JdbcUtils;
import org.junit.Test;

import java.sql.SQLException;
import java.util.function.Supplier;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;

public class DropColumnTest extends BaseTest {

	public DropColumnTest(Supplier<TestDatabaseDetails> theTestDatabaseDetails) {
		super(theTestDatabaseDetails);
	}

	@Test
	public void testDropColumn() throws SQLException {
		executeSql("create table SOMETABLE (PID bigint not null, TEXTCOL varchar(255))");

		DropColumnTask task = new DropColumnTask("1",  "1");
		task.setTableName("SOMETABLE");
		task.setColumnName("TEXTCOL");
		getMigrator().addTask(task);

		getMigrator().migrate();

		assertThat(JdbcUtils.getColumnNames(getConnectionProperties(), "SOMETABLE"), containsInAnyOrder("PID"));

		// Do it again to make sure there is no error
		getMigrator().migrate();
		getMigrator().migrate();

	}

}
