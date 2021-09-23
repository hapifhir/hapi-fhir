package ca.uhn.fhir.jpa.migrate.taskdef;

import ca.uhn.fhir.jpa.migrate.DriverTypeEnum;
import ca.uhn.fhir.jpa.migrate.JdbcUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

public class MigratePostgresTextClobToBinaryClobTask extends BaseTableColumnTask {
	private static final Logger ourLog = LoggerFactory.getLogger(MigratePostgresTextClobToBinaryClobTask.class);

	/**
	 * Constructor
	 */
	public MigratePostgresTextClobToBinaryClobTask(String theProductVersion, String theSchemaVersion) {
		super(theProductVersion, theSchemaVersion);
	}

	@Override
	public void validate() {
		super.validate();
		setDescription("Migrate text clob " + getColumnName() + " from table " + getTableName() + " (only affects Postgresql)");
	}

	@Override
	protected void doExecute() throws SQLException {
		if (getConnectionProperties().getDriverType() != DriverTypeEnum.POSTGRES_9_4) {
			return;
		}

		JdbcUtils.ColumnType columnType = JdbcUtils.getColumnType(getConnectionProperties(), getTableName(), getColumnName());
		if (columnType.getColumnTypeEnum() == ColumnTypeEnum.LONG) {
			ourLog.info("Table {} column {} is already of type LONG, no migration needed", getTableName(), getColumnName());
			return;
		}

		String tempColumnName = getColumnName() + "_m";

		executeSql(getTableName(), "alter table ? add column ? oid", getTableName(), tempColumnName);
		executeSql(getTableName(), "update ? set ? = cast(? as oid) where ? is not null", getTableName(), tempColumnName, getColumnName(), getColumnName());
		executeSql(getTableName(), "alter table ? drop column ?", getTableName(), getColumnName());
		executeSql(getTableName(), "alter table ? rename column ? to ?", getTableName(), tempColumnName, getColumnName());

	}
}
