package ca.uhn.fhir.jpa.migrate.taskdef;

import ca.uhn.fhir.jpa.migrate.DriverTypeEnum;
import ca.uhn.fhir.jpa.migrate.JdbcUtils;
import ca.uhn.fhir.jpa.migrate.tasks.api.ISchemaInitializationProvider;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;

public class InitializeSchemaTask extends BaseTask<InitializeSchemaTask> {
	private static final Logger ourLog = LoggerFactory.getLogger(InitializeSchemaTask.class);
	private final ISchemaInitializationProvider mySchemaInitializationProvider;

	public InitializeSchemaTask(String theRelease, String theVersion, ISchemaInitializationProvider theSchemaInitializationProvider) {
		super(theRelease, theVersion);
		mySchemaInitializationProvider = theSchemaInitializationProvider;
	}

	@Override
	public void validate() {
		// nothing
	}

	@Override
	public void execute() throws SQLException {
		DriverTypeEnum driverType = getDriverType();

		Set<String> tableNames = JdbcUtils.getTableNames(getConnectionProperties());
		String schemaExistsIndicatorTable = mySchemaInitializationProvider.getSchemaExistsIndicatorTable();
		if (tableNames.contains(schemaExistsIndicatorTable)) {
			logInfo(ourLog, "The table {} already exists.  Skipping schema initialization for {}", schemaExistsIndicatorTable, driverType);
			return;
		}

		logInfo(ourLog, "Initializing schema for {}", driverType);

		List<String> sqlStatements = mySchemaInitializationProvider.getSqlStatements(driverType);

		for (String nextSql : sqlStatements) {
			executeSql(null, nextSql);
		}
	}

	@Override
	public boolean equals(Object theO) {
		if (this == theO) return true;

		if (theO == null || getClass() != theO.getClass()) return false;

		InitializeSchemaTask that = (InitializeSchemaTask) theO;

		return new EqualsBuilder()
			.append(mySchemaInitializationProvider, that.mySchemaInitializationProvider)
			.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37)
			.append(mySchemaInitializationProvider)
			.toHashCode();
	}
}
