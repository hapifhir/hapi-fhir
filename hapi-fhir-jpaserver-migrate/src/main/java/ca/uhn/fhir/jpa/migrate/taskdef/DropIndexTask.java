package ca.uhn.fhir.jpa.migrate.taskdef;

import ca.uhn.fhir.jpa.migrate.JdbcUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.Set;

public class DropIndexTask extends BaseTask<DropIndexTask> {

	private static final Logger ourLog = LoggerFactory.getLogger(DropIndexTask.class);
	private String myIndexName;
	private String myTableName;

	@Override
	public void validate() {
		Validate.notBlank(myIndexName, "The index name must not be blank");
		Validate.notBlank(myTableName, "The table name must not be blank");

		if (getDescription() == null) {
			setDescription("Drop index " + myIndexName + " on table " + myTableName);
		}
	}

	@Override
	public void execute() throws SQLException {
		Set<String> indexNames = JdbcUtils.getIndexNames(getConnectionProperties(), myTableName);

		if (!indexNames.contains(myIndexName)) {
			ourLog.info("Index {} does not exist on table {} - No action needed", myIndexName, myTableName);
			return;
		}

		ourLog.info("Dropping index {} on table {}", myIndexName, myTableName);

		String sql = null;
		switch (getDriverType()) {
			case MYSQL_5_7:
			case MARIADB_10_1:
				sql = "ALTER TABLE " + myTableName + " DROP INDEX " + myIndexName;
				break;
			case POSTGRES_9_4:
			case DERBY_EMBEDDED:
			case ORACLE_12C:
				sql = "DROP INDEX " + myIndexName;
				break;
			case MSSQL_2012:
				sql = "DROP INDEX " + myTableName + "." + myIndexName;
				break;
		}
		executeSql(sql);

	}


	public DropIndexTask setTableName(String theTableName) {
		myTableName = theTableName;
		return this;
	}

	public DropIndexTask setIndexName(String theIndexName) {
		myIndexName = theIndexName;
		return this;
	}
}
