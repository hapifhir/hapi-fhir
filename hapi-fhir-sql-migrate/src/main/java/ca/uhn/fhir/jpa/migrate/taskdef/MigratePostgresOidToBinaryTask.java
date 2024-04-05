package ca.uhn.fhir.jpa.migrate.taskdef;

import ca.uhn.fhir.jpa.migrate.DriverTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.Set;

public class MigratePostgresOidToBinaryTask extends BaseTableColumnTask {
	private static final Logger ourLog = LoggerFactory.getLogger(MigratePostgresOidToBinaryTask.class);

	private final String myFromColumName;
	private final String myToColumName;

	public MigratePostgresOidToBinaryTask(
			String theProductVersion,
			String theSchemaVersion,
			String theTableName,
			String theFromColumName,
			String theToColumName) {
		super(theProductVersion, theSchemaVersion);
		myFromColumName = theFromColumName;
		myToColumName = theToColumName;

		setTableName(theTableName);

		setOnlyAppliesToPlatforms(Set.of(DriverTypeEnum.POSTGRES_9_4));
	}

	@Override
	public void validate() {
		super.validate();

		setDescription("Migrating Lob (oid) from colum  " + myFromColumName + " to Binary on colum " + myToColumName
				+ " for table " + getTableName() + " (only affects Postgresql)");
	}

	@Override
	protected void doExecute() throws SQLException {
		String tableName = getTableName().toLowerCase();
		String fromColumName = myFromColumName.toLowerCase();
		String toColumName = myToColumName.toLowerCase();

		executeSql(
				tableName,
				"update " + tableName + " set " + toColumName + " = lo_get(" + fromColumName + ")  where "
						+ fromColumName + " is not null");
	}
}
