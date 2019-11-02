package ca.uhn.fhir.jpa.migrate.taskdef;

import ca.uhn.fhir.jpa.migrate.DriverTypeEnum;
import ca.uhn.fhir.jpa.migrate.JdbcUtils;
import org.junit.Test;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.*;

public class InitializeSchemaTaskTest extends BaseTest {

	@Test
	public void testInitializeTwice() throws SQLException {
		InitializeSchemaTask task = new InitializeSchemaTask("1", "1", t -> getSql());
		getMigrator().addTask(task);
		getMigrator().migrate();
		assertThat(JdbcUtils.getTableNames(getConnectionProperties()), containsInAnyOrder("SOMETABLE"));

		// Second migrate runs without issue
		getMigrator().migrate();
	}

	private List<String> getSql() {
		return Collections.singletonList("create table SOMETABLE (PID bigint not null, TEXTCOL varchar(255))");
	}
}
