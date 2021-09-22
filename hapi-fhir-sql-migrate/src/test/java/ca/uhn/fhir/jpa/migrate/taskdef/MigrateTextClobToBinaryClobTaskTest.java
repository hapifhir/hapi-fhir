package ca.uhn.fhir.jpa.migrate.taskdef;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.jdbc.core.ConnectionCallback;

import java.io.StringReader;
import java.sql.Clob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Supplier;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class MigrateTextClobToBinaryClobTaskTest extends BaseTest {

	@ParameterizedTest(name = "{index}: {0}")
	@MethodSource("data")
	public void testMigrate(Supplier<TestDatabaseDetails> theTestDatabaseDetails) throws SQLException {
		before(theTestDatabaseDetails);

		executeSql("create table SOMETABLE (PID bigint not null, TEXTCOL clob, NEWCOL blob)");

		// Insert clob data
		getConnectionProperties().getTxTemplate().execute(t -> {
			getConnectionProperties().newJdbcTemplate().execute((ConnectionCallback<Object>) c ->{
				PreparedStatement stmt = c.prepareStatement("insert into SOMETABLE (PID, TEXTCOL) values (1, ?)");
				stmt.setClob(1, new StringReader("this is the text content"));
				stmt.execute();
				return null;
			});
			return null;
		});

		MigrateTextClobToBinaryClobTask task = new MigrateTextClobToBinaryClobTask("1", "1");
		task.setTableName("SOMETABLE");
		task.setPidColumnName("PID");
		task.setColumnName("TEXTCOL");
		task.setNewColumnName("NEWCOL");
		task.validate();
		getMigrator().addTask(task);

		getMigrator().migrate();

		// Verify migrated clob data
		getConnectionProperties().getTxTemplate().execute(t -> {
			getConnectionProperties().newJdbcTemplate().execute((ConnectionCallback<Object>) c ->{
				PreparedStatement stmt = c.prepareStatement("select PID, TEXTCOL, NEWCOL from SOMETABLE");
				ResultSet result = stmt.executeQuery();
				assertTrue(result.next());
				assertEquals(1, result.getLong(1));
				assertNull(result.getClob(2));
				assertFalse(result.next());
				return null;
			});
			return null;
		});

	}


}
