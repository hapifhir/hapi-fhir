package ca.uhn.fhir.jpa.migrate.taskdef;

import ca.uhn.fhir.i18n.Msg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

public class MigrateColumBlobTypeToBinaryTypeTask extends BaseTableColumnTask {
	private static final Logger ourLog = LoggerFactory.getLogger(MigrateColumBlobTypeToBinaryTypeTask.class);

	private final String myFromColumName;
	private final String myToColumName;

	public MigrateColumBlobTypeToBinaryTypeTask(
			String theProductVersion,
			String theSchemaVersion,
			String theTableName,
			String theFromColumName,
			String theToColumName) {
		super(theProductVersion, theSchemaVersion);
		myFromColumName = theFromColumName;
		myToColumName = theToColumName;

		setTableName(theTableName);
	}

	@Override
	public void validate() {
		super.validate();

		setDescription("Migrating BLob (oid) from colum  " + myFromColumName + " to BINARY on colum " + myToColumName
				+ " for table " + getTableName() + " (only affects Postgresql)");
	}

	@Override
	protected void doExecute() throws SQLException {
		String sql = buildSqlStatement();

		executeSql(getTableName(), sql);
	}

	String buildSqlStatement() {
		String tableName = getTableName().toLowerCase();
		String fromColumName = myFromColumName.toLowerCase();
		String toColumName = myToColumName.toLowerCase();

		String retVal;

		switch (getDriverType()) {
			case MYSQL_5_7:
			case DERBY_EMBEDDED:
			case ORACLE_12C:
			case MARIADB_10_1:
			case COCKROACHDB_21_1:
			case H2_EMBEDDED:
			case MSSQL_2012:
				retVal = "update " + tableName + " set " + toColumName + " = " + fromColumName + " where "
						+ fromColumName + " is not null";
				break;
			case POSTGRES_9_4:
				retVal = "update " + tableName + " set " + toColumName + " = lo_get(" + fromColumName + ")  where "
						+ fromColumName + " is not null";
				break;
			default:
				throw new IllegalStateException(Msg.code(2514));
		}

		return retVal;
	}
}
