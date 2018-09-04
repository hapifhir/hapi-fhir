package ca.uhn.fhir.jpa.migrate.taskdef;

import ca.uhn.fhir.jpa.migrate.JdbcUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.Set;

public class AddColumnTask extends BaseTableColumnTypeTask<AddColumnTask> {

	private static final Logger ourLog = LoggerFactory.getLogger(AddColumnTask.class);


	@Override
	public void execute() throws SQLException {
		Set<String> columnNames = JdbcUtils.getColumnNames(getConnectionProperties(), getTableName());
		if (columnNames.contains(getColumnName())) {
			ourLog.info("Column {} already exists on table {} - No action performed", getColumnName(), getTableName());
			return;
		}

		String type = getSqlType();
		String nullable = getSqlNotNull();
		String sql = "alter table " + getTableName() + " add column " + getColumnName() + " " + type + " " + nullable;
		ourLog.info("Adding column {} of type {} to table {}", getColumnName(), type, getTableName());
		executeSql(sql);
	}

}
