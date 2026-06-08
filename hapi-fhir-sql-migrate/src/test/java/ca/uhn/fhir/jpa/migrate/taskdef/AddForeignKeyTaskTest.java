package ca.uhn.fhir.jpa.migrate.taskdef;

import ca.uhn.fhir.jpa.migrate.JdbcUtils;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.sql.SQLException;
import java.util.List;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;

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


}
