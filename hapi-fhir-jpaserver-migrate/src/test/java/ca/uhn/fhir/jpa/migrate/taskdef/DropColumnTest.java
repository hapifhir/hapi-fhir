package ca.uhn.fhir.jpa.migrate.taskdef;

import ca.uhn.fhir.jpa.migrate.JdbcUtils;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.function.Supplier;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.MatcherAssert.assertThat;

public class DropColumnTest extends BaseTest {

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

	@Test
	public void testDropForeignKeyColumn() throws SQLException {
		executeSql("create table PARENT (PID bigint not null, TEXTCOL varchar(255), primary key (PID))");
		executeSql("create table SIBLING (PID bigint not null, TEXTCOL varchar(255), primary key (PID))");
		executeSql("create table CHILD (PID bigint not null, PARENTREF bigint, SIBLINGREF bigint)");
		executeSql("alter table CHILD add constraint FK_MOM foreign key (PARENTREF) references PARENT(PID)");
		executeSql("alter table CHILD add constraint FK_BROTHER foreign key (SIBLINGREF) references SIBLING(PID)");

		assertThat(JdbcUtils.getForeignKeys(getConnectionProperties(), "PARENT", "CHILD"), hasSize(1));
		assertThat(JdbcUtils.getForeignKeys(getConnectionProperties(), "SIBLING", "CHILD"), hasSize(1));

		assertThat(JdbcUtils.getForeignKeysForColumn(getConnectionProperties(), "PARENTREF", "CHILD"), containsInAnyOrder("FK_MOM"));
		assertThat(JdbcUtils.getForeignKeysForColumn(getConnectionProperties(), "SIBLINGREF", "CHILD"), containsInAnyOrder("FK_BROTHER"));

		DropColumnTask task = new DropColumnTask("1",  "1");
		task.setTableName("CHILD");
		task.setColumnName("PARENTREF");
		getMigrator().addTask(task);

		getMigrator().migrate();

		assertThat(JdbcUtils.getColumnNames(getConnectionProperties(), "CHILD"), containsInAnyOrder("PID", "SIBLINGREF"));

		assertThat(JdbcUtils.getForeignKeys(getConnectionProperties(), "PARENT", "CHILD"), empty());
		assertThat(JdbcUtils.getForeignKeys(getConnectionProperties(), "SIBLING", "CHILD"), hasSize(1));

		assertThat(JdbcUtils.getForeignKeysForColumn(getConnectionProperties(), "PARENTREF", "CHILD"), empty());
		assertThat(JdbcUtils.getForeignKeysForColumn(getConnectionProperties(), "SIBLINGREF", "CHILD"), containsInAnyOrder("FK_BROTHER"));

		// Do it again to make sure there is no error
		getMigrator().migrate();
		getMigrator().migrate();

	}

}
