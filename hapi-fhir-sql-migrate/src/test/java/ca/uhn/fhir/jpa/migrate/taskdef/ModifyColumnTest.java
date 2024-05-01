package ca.uhn.fhir.jpa.migrate.taskdef;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import ca.uhn.fhir.jpa.migrate.DriverTypeEnum;
import ca.uhn.fhir.jpa.migrate.HapiMigrationException;
import ca.uhn.fhir.jpa.migrate.JdbcUtils;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import jakarta.annotation.Nonnull;
import java.sql.SQLException;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.assertj.core.api.Assertions.fail;


public class ModifyColumnTest extends BaseTest {

	@ParameterizedTest(name = "{index}: {0}")
	@MethodSource("data")
	public void testColumnWithJdbcTypeClob(Supplier<TestDatabaseDetails> theTestDatabaseDetails) throws SQLException {
		before(theTestDatabaseDetails);

		if (getDriverType() == DriverTypeEnum.DERBY_EMBEDDED) {
			return;
		}

		executeSql("create table SOMETABLE (TEXTCOL clob)");

		ModifyColumnTask task = new ModifyColumnTask("1", "1");
		task.setTableName("SOMETABLE");
		task.setColumnName("TEXTCOL");
		task.setColumnType(ColumnTypeEnum.STRING);
		task.setNullable(true);
		task.setColumnLength(250);
		getMigrator().addTask(task);

		getMigrator().migrate();

		assertThat(JdbcUtils.getColumnType(getConnectionProperties(), "SOMETABLE", "TEXTCOL")).isEqualTo(new JdbcUtils.ColumnType(ColumnTypeEnum.STRING, 250));
		assertThat(task.getExecutedStatements()).hasSize(1);

		// Make sure additional migrations don't crash
		getMigrator().migrate();
		getMigrator().migrate();

	}

	@ParameterizedTest(name = "{index}: {0}")
	@MethodSource("data")
	public void testColumnAlreadyExists(Supplier<TestDatabaseDetails> theTestDatabaseDetails) throws SQLException {
		before(theTestDatabaseDetails);

		executeSql("create table SOMETABLE (PID bigint not null, TEXTCOL varchar(255), newcol bigint)");

		ModifyColumnTask task = new ModifyColumnTask("1", "1");
		task.setTableName("SOMETABLE");
		task.setColumnName("TEXTCOL");
		task.setColumnType(ColumnTypeEnum.STRING);
		task.setNullable(true);
		task.setColumnLength(300);
		getMigrator().addTask(task);

		getMigrator().migrate();

		assertThat(JdbcUtils.getColumnType(getConnectionProperties(), "SOMETABLE", "TEXTCOL")).isEqualTo(new JdbcUtils.ColumnType(ColumnTypeEnum.STRING, 300));
		assertThat(task.getExecutedStatements()).hasSize(1);

		// Make sure additional migrations don't crash
		getMigrator().migrate();
		getMigrator().migrate();

	}

	@ParameterizedTest(name = "{index}: {0}")
	@MethodSource("data")
	public void testNoShrink_SameNullable(Supplier<TestDatabaseDetails> theTestDatabaseDetails) throws SQLException {
		before(theTestDatabaseDetails);

		executeSql("create table SOMETABLE (PID bigint not null, TEXTCOL varchar(255), newcol bigint)");

		ModifyColumnTask task = new ModifyColumnTask("1", "123456.7");
		task.setTableName("SOMETABLE");
		task.setColumnName("TEXTCOL");
		task.setColumnType(ColumnTypeEnum.STRING);
		task.setNullable(true);
		task.setColumnLength(200);

		getMigrator().setNoColumnShrink(true);
		getMigrator().addTask(task);
		getMigrator().migrate();

		assertThat(task.getExecutedStatements()).isEmpty();
		assertThat(JdbcUtils.getColumnType(getConnectionProperties(), "SOMETABLE", "TEXTCOL")).isEqualTo(new JdbcUtils.ColumnType(ColumnTypeEnum.STRING, 255));

		// Make sure additional migrations don't crash
		getMigrator().migrate();
		getMigrator().migrate();

	}

	@ParameterizedTest(name = "{index}: {0}")
	@MethodSource("data")
	public void testColumnMakeNullable(Supplier<TestDatabaseDetails> theTestDatabaseDetails) throws SQLException {
		before(theTestDatabaseDetails);

		executeSql("create table SOMETABLE (PID bigint not null, TEXTCOL varchar(255) not null)");
		assertFalse(JdbcUtils.isColumnNullable(getConnectionProperties(), "SOMETABLE", "PID"));
		assertFalse(JdbcUtils.isColumnNullable(getConnectionProperties(), "SOMETABLE", "TEXTCOL"));
		assertThat(JdbcUtils.getColumnType(getConnectionProperties(), "SOMETABLE", "PID")).isEqualTo(getLongColumnType(theTestDatabaseDetails));
		assertThat(JdbcUtils.getColumnType(getConnectionProperties(), "SOMETABLE", "TEXTCOL")).isEqualTo(new JdbcUtils.ColumnType(ColumnTypeEnum.STRING, 255));

		// PID
		ModifyColumnTask task = new ModifyColumnTask("1", "1");
		task.setTableName("SOMETABLE");
		task.setColumnName("PID");
		task.setColumnType(ColumnTypeEnum.LONG);
		task.setNullable(true);
		getMigrator().addTask(task);

		// STRING
		task = new ModifyColumnTask("1", "2");
		task.setTableName("SOMETABLE");
		task.setColumnName("TEXTCOL");
		task.setColumnType(ColumnTypeEnum.STRING);
		task.setNullable(true);
		task.setColumnLength(255);
		getMigrator().addTask(task);

		// Do migration
		getMigrator().migrate();

		assertTrue(JdbcUtils.isColumnNullable(getConnectionProperties(), "SOMETABLE", "PID"));
		assertTrue(JdbcUtils.isColumnNullable(getConnectionProperties(), "SOMETABLE", "TEXTCOL"));
		assertThat(JdbcUtils.getColumnType(getConnectionProperties(), "SOMETABLE", "PID")).isEqualTo(getLongColumnType(theTestDatabaseDetails));
		assertThat(JdbcUtils.getColumnType(getConnectionProperties(), "SOMETABLE", "TEXTCOL")).isEqualTo(new JdbcUtils.ColumnType(ColumnTypeEnum.STRING, 255));

		// Make sure additional migrations don't crash
		getMigrator().migrate();
		getMigrator().migrate();


	}

	@ParameterizedTest(name = "{index}: {0}")
	@MethodSource("data")
	public void testNoShrink_ColumnMakeDateNullable(Supplier<TestDatabaseDetails> theTestDatabaseDetails) throws SQLException {
		before(theTestDatabaseDetails);

		executeSql("create table SOMETABLE (PID bigint not null, DATECOL timestamp not null)");
		assertFalse(JdbcUtils.isColumnNullable(getConnectionProperties(), "SOMETABLE", "PID"));
		assertFalse(JdbcUtils.isColumnNullable(getConnectionProperties(), "SOMETABLE", "DATECOL"));
		assertThat(JdbcUtils.getColumnType(getConnectionProperties(), "SOMETABLE", "PID")).isEqualTo(getLongColumnType(theTestDatabaseDetails));
		assertThat(JdbcUtils.getColumnType(getConnectionProperties(), "SOMETABLE", "DATECOL").getColumnTypeEnum()).isEqualTo(ColumnTypeEnum.DATE_TIMESTAMP);

		getMigrator().setNoColumnShrink(true);

		// PID
		ModifyColumnTask task = new ModifyColumnTask("1", "1");
		task.setTableName("SOMETABLE");
		task.setColumnName("PID");
		task.setColumnType(ColumnTypeEnum.LONG);
		task.setNullable(true);
		getMigrator().addTask(task);

		// STRING
		task = new ModifyColumnTask("1", "2");
		task.setTableName("SOMETABLE");
		task.setColumnName("DATECOL");
		task.setColumnType(ColumnTypeEnum.DATE_TIMESTAMP);
		task.setNullable(true);
		getMigrator().addTask(task);

		// Do migration
		getMigrator().migrate();

		assertTrue(JdbcUtils.isColumnNullable(getConnectionProperties(), "SOMETABLE", "PID"));
		assertTrue(JdbcUtils.isColumnNullable(getConnectionProperties(), "SOMETABLE", "DATECOL"));
		assertThat(JdbcUtils.getColumnType(getConnectionProperties(), "SOMETABLE", "PID")).isEqualTo(getLongColumnType(theTestDatabaseDetails));
		assertThat(JdbcUtils.getColumnType(getConnectionProperties(), "SOMETABLE", "DATECOL").getColumnTypeEnum()).isEqualTo(ColumnTypeEnum.DATE_TIMESTAMP);

		// Make sure additional migrations don't crash
		getMigrator().migrate();
		getMigrator().migrate();
	}

	@ParameterizedTest(name = "{index}: {0}")
	@MethodSource("data")
	public void testColumnMakeNotNullable(Supplier<TestDatabaseDetails> theTestDatabaseDetails) throws SQLException {
		before(theTestDatabaseDetails);

		executeSql("create table SOMETABLE (PID bigint, TEXTCOL varchar(255))");
		assertTrue(JdbcUtils.isColumnNullable(getConnectionProperties(), "SOMETABLE", "PID"));
		assertTrue(JdbcUtils.isColumnNullable(getConnectionProperties(), "SOMETABLE", "TEXTCOL"));
		assertThat(JdbcUtils.getColumnType(getConnectionProperties(), "SOMETABLE", "PID")).isEqualTo(getLongColumnType(theTestDatabaseDetails));
		assertThat(JdbcUtils.getColumnType(getConnectionProperties(), "SOMETABLE", "TEXTCOL")).isEqualTo(new JdbcUtils.ColumnType(ColumnTypeEnum.STRING, 255));

		// PID
		ModifyColumnTask task = new ModifyColumnTask("1", "1");
		task.setTableName("SOMETABLE");
		task.setColumnName("PID");
		task.setColumnType(ColumnTypeEnum.LONG);
		task.setNullable(false);
		getMigrator().addTask(task);

		// STRING
		task = new ModifyColumnTask("1", "2");
		task.setTableName("SOMETABLE");
		task.setColumnName("TEXTCOL");
		task.setColumnType(ColumnTypeEnum.STRING);
		task.setNullable(false);
		task.setColumnLength(255);
		getMigrator().addTask(task);

		// Do migration
		getMigrator().migrate();

		assertFalse(JdbcUtils.isColumnNullable(getConnectionProperties(), "SOMETABLE", "PID"));
		assertFalse(JdbcUtils.isColumnNullable(getConnectionProperties(), "SOMETABLE", "TEXTCOL"));
		assertThat(JdbcUtils.getColumnType(getConnectionProperties(), "SOMETABLE", "PID")).isEqualTo(getLongColumnType(theTestDatabaseDetails));
		assertThat(JdbcUtils.getColumnType(getConnectionProperties(), "SOMETABLE", "TEXTCOL")).isEqualTo(new JdbcUtils.ColumnType(ColumnTypeEnum.STRING, 255));

		// Make sure additional migrations don't crash
		getMigrator().migrate();
		getMigrator().migrate();

	}

	@SuppressWarnings("EnumSwitchStatementWhichMissesCases")
	@Nonnull
	private JdbcUtils.ColumnType getLongColumnType(Supplier<TestDatabaseDetails> theTestDatabaseDetails) {
		switch (theTestDatabaseDetails.get().getDriverType()) {
			case H2_EMBEDDED:
				return new JdbcUtils.ColumnType(ColumnTypeEnum.LONG, 64);
			case DERBY_EMBEDDED:
				return new JdbcUtils.ColumnType(ColumnTypeEnum.LONG, 19);
			default:
				throw new UnsupportedOperationException();
		}
	}

	@ParameterizedTest(name = "{index}: {0}")
	@MethodSource("data")
	public void testColumnDoesntAlreadyExist(Supplier<TestDatabaseDetails> theTestDatabaseDetails) throws SQLException {
		before(theTestDatabaseDetails);

		executeSql("create table SOMETABLE (PID bigint, TEXTCOL varchar(255))");

		ModifyColumnTask task = new ModifyColumnTask("1", "1");
		task.setTableName("SOMETABLE");
		task.setColumnName("SOMECOLUMN");
		task.setDescription("Make nullable");
		task.setNullable(true);
		getMigrator().addTask(task);

		getMigrator().migrate();

		assertThat(JdbcUtils.getColumnNames(getConnectionProperties(), "SOMETABLE")).containsExactlyInAnyOrder("PID", "TEXTCOL");
	}

	@ParameterizedTest(name = "{index}: {0}")
	@MethodSource("data")
	public void testFailureAllowed(Supplier<TestDatabaseDetails> theTestDatabaseDetails) throws SQLException {
		before(theTestDatabaseDetails);

		executeSql("create table SOMETABLE (PID bigint, TEXTCOL varchar(255))");
		executeSql("insert into SOMETABLE (TEXTCOL) values ('HELLO')");

		ModifyColumnTask task = new ModifyColumnTask("1", "1");
		task.setTableName("SOMETABLE");
		task.setColumnName("TEXTCOL");
		task.setColumnType(ColumnTypeEnum.LONG);
		task.setNullable(true);
		task.setFailureAllowed(true);
		getMigrator().addTask(task);

		getMigrator().migrate();
		assertThat(JdbcUtils.getColumnType(getConnectionProperties(), "SOMETABLE", "TEXTCOL").getColumnTypeEnum()).isEqualTo(ColumnTypeEnum.STRING);

	}

	@ParameterizedTest(name = "{index}: {0}")
	@MethodSource("data")
	public void testFailureNotAllowed(Supplier<TestDatabaseDetails> theTestDatabaseDetails) {
		before(theTestDatabaseDetails);

		executeSql("create table SOMETABLE (PID bigint, TEXTCOL varchar(255))");
		executeSql("insert into SOMETABLE (TEXTCOL) values ('HELLO')");

		ModifyColumnTask task = new ModifyColumnTask("1", "1");
		task.setTableName("SOMETABLE");
		task.setColumnName("TEXTCOL");
		task.setColumnType(ColumnTypeEnum.LONG);
		task.setNullable(true);
		getMigrator().addTask(task);

		try {
			getMigrator().migrate();
			fail("");
		} catch (HapiMigrationException e) {
			// expected
		}

	}

	@ParameterizedTest(name = "{index}: {0}")
	@MethodSource("data")
	public void dontCompareLengthIfNoneSpecifiedInTask(Supplier<TestDatabaseDetails> theTestDatabaseDetails) throws SQLException {
		before(theTestDatabaseDetails);

		executeSql("create table SOMETABLE (PID bigint, TEXTCOL varchar(255))");

		ModifyColumnTask task = new ModifyColumnTask("1", "1");
		task.setTableName("SOMETABLE");
		task.setColumnName("PID");
		task.setColumnType(ColumnTypeEnum.LONG);
		task.setNullable(true);

		JdbcUtils.ColumnType existingColumnType = JdbcUtils.getColumnType(getConnectionProperties(), "SOMETABLE", "PID");
		assertThat(existingColumnType).isEqualTo(getLongColumnType(theTestDatabaseDetails));
		assertTrue(existingColumnType.equals(task.getColumnType(), task.getColumnLength()));
	}


	@ParameterizedTest(name = "{index}: {0}")
	@MethodSource("data")
	public void testShrinkDoesntFailIfShrinkCannotProceed(Supplier<TestDatabaseDetails> theTestDatabaseDetails) throws SQLException {
		before(theTestDatabaseDetails);

		executeSql("create table SOMETABLE (PID bigint not null, TEXTCOL varchar(10))");
		executeSql("insert into SOMETABLE (PID, TEXTCOL) values (1, '0123456789')");

		ModifyColumnTask task = new ModifyColumnTask("1", "123456.7");
		task.setTableName("SOMETABLE");
		task.setColumnName("TEXTCOL");
		task.setColumnType(ColumnTypeEnum.STRING);
		task.setNullable(true);
		task.setColumnLength(5);

		getMigrator().addTask(task);
		getMigrator().migrate();

		assertThat(task.getExecutedStatements()).hasSize(1);
		assertThat(JdbcUtils.getColumnType(getConnectionProperties(), "SOMETABLE", "TEXTCOL")).isEqualTo(new JdbcUtils.ColumnType(ColumnTypeEnum.STRING, 10));

		// Make sure additional migrations don't crash
		getMigrator().migrate();
		getMigrator().migrate();

	}

}
