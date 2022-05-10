package ca.uhn.fhir.jpa.migrate.taskdef;

import ca.uhn.fhir.jpa.migrate.JdbcUtils;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.sql.SQLException;
import java.util.function.Supplier;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;

public class DropForeignKeyTaskTest extends BaseTest {


	@ParameterizedTest(name = "{index}: {0}")
	@MethodSource("data")
	public void testDropForeignKey(Supplier<TestDatabaseDetails> theTestDatabaseDetails) throws SQLException {
		before(theTestDatabaseDetails);

		executeSql("create table PARENT (PID bigint not null, TEXTCOL varchar(255), primary key (PID))");
		executeSql("create table CHILD (PID bigint not null, PARENTREF bigint)");
		executeSql("alter table CHILD add constraint FK_MOM foreign key (PARENTREF) references PARENT(PID)");

		assertThat(JdbcUtils.getForeignKeys(getConnectionProperties(), "PARENT", "CHILD"), hasSize(1));

		DropForeignKeyTask task = new DropForeignKeyTask("1", "1");
		task.setTableName("CHILD");
		task.setParentTableName("PARENT");
		task.setConstraintName("FK_MOM");
		getMigrator().addTask(task);

		getMigrator().migrate();

		assertThat(JdbcUtils.getForeignKeys(getConnectionProperties(), "PARENT", "CHILD"), empty());

		// Make sure additional calls don't crash
		getMigrator().migrate();
		getMigrator().migrate();
	}


}
