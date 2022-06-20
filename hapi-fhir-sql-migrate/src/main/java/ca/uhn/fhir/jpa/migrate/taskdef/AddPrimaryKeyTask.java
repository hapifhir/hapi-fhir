package ca.uhn.fhir.jpa.migrate.taskdef;

import ca.uhn.fhir.jpa.migrate.JdbcUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;

public class AddPrimaryKeyTask extends BaseTableTask{
	private static final Logger ourLog = LoggerFactory.getLogger(AddPrimaryKeyTask.class);
	private String myConstraintName;
	private List<String> myColumns;

	public AddPrimaryKeyTask(String theProductVersion, String theSchemaVersion, String theTableName, String theConstraintName) {
		super(theProductVersion, theSchemaVersion);
		setTableName(theTableName);
		myConstraintName = theConstraintName;
	}

	@Override
	protected void doExecute() throws SQLException {
		Set<String> indexNames = JdbcUtils.getIndexNames(getConnectionProperties(), getTableName());
		if (indexNames.contains(myConstraintName)) {
			logInfo(ourLog, "Index {} already exists on table {} - No action performed", myConstraintName, getTableName());
			return;
		}

		logInfo(ourLog, "Going to define a primary key named {} on table {} for columns {}", myConstraintName, getTableName(), myColumns);

		String sql = generateSql();
		String tableName = getTableName();

		try {
			executeSql(tableName, sql);
		} catch (Exception e) {
			if (e.toString().contains("already exists")) {
				ourLog.warn("Constraing {} already exists", myConstraintName);
			} else {
				throw e;
			}
		}
	}

	String generateSql() {
		String columns = String.join(", ", myColumns);
		String sql = "";
		switch (getDriverType()) {
			case MSSQL_2012:
			case COCKROACHDB_21_1:
			case POSTGRES_9_4:
			case ORACLE_12C:
			case H2_EMBEDDED:
			case DERBY_EMBEDDED:
				sql = "ALTER TABLE " + getTableName() + " ADD CONSTRAINT " + myConstraintName + " PRIMARY KEY (" + columns + ")";
				break;
			case MARIADB_10_1:
			case MYSQL_5_7:
				// quote the contraint name
				sql = "ALTER TABLE " + getTableName() + " ADD CONSTRAINT `" + myConstraintName + "` PRIMARY KEY (" + columns + ")";
				break;
		}
		return sql;
	}

	public AddPrimaryKeyTask setColumns(List<String> theColumns) {
		myColumns = theColumns;
		return this;
	}

	public List<String> getColumns() {
		return myColumns;
	}

	public void setConstraintName(String theConstraintName) {
		myConstraintName = theConstraintName;
	}

	@Override
	protected void generateEquals(EqualsBuilder theBuilder, BaseTask theOtherObject) {
		super.generateEquals(theBuilder, theOtherObject);

		AddPrimaryKeyTask otherObject = (AddPrimaryKeyTask) theOtherObject;
		theBuilder.append(myColumns, otherObject.myColumns);
		theBuilder.append(myConstraintName, otherObject.myConstraintName);

	}

	@Override
	protected void generateHashCode(HashCodeBuilder theBuilder) {
		super.generateHashCode(theBuilder);
		theBuilder.append(myColumns);
		theBuilder.append(myConstraintName);
	}

}
