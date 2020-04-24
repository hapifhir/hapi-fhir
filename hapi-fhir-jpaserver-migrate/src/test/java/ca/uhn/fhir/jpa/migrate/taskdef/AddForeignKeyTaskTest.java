package ca.uhn.fhir.jpa.migrate.taskdef;

import ca.uhn.fhir.jpa.migrate.JdbcUtils;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.sql.SQLException;
import java.util.function.Supplier;

import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.assertThat;

public class AddForeignKeyTaskTest extends BaseTest {

	public AddForeignKeyTaskTest(Supplier<TestDatabaseDetails> theTestDatabaseDetails) {
		super(theTestDatabaseDetails);
	}

	@Test
	public void testAddForeignKey() throws SQLException {
		executeSql("create table HOME (PID bigint not null, TEXTCOL varchar(255), primary key (PID))");
		executeSql("create table FOREIGNTBL (PID bigint not null, HOMEREF bigint)");
		assertThat(JdbcUtils.getForeignKeys(getConnectionProperties(), "HOME", "FOREIGNTBL"), empty());

		AddForeignKeyTask task = new AddForeignKeyTask("1", "1");
		task.setTableName("FOREIGNTBL");
		task.setColumnName("HOMEREF");
		task.setConstraintName("FK_HOME_FOREIGN");
		task.setForeignColumnName("PID");
		task.setForeignTableName("HOME");
		getMigrator().addTask(task);

		getMigrator().migrate();

		assertThat(JdbcUtils.getForeignKeys(getConnectionProperties(), "HOME", "FOREIGNTBL"), Matchers.contains("FK_HOME_FOREIGN"));

		// Make sure additional calls don't crash
		getMigrator().migrate();
		getMigrator().migrate();
	}


}
