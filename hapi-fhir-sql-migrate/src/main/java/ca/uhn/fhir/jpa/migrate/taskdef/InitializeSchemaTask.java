package ca.uhn.fhir.jpa.migrate.taskdef;

/*-
 * #%L
 * HAPI FHIR Server - SQL Migration
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

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

public class InitializeSchemaTask extends BaseTask {
	public static final String DESCRIPTION_PREFIX = "Initialize schema for ";
	private static final Logger ourLog = LoggerFactory.getLogger(InitializeSchemaTask.class);
	private final ISchemaInitializationProvider mySchemaInitializationProvider;
	private boolean myInitializedSchema;

	public InitializeSchemaTask(String theProductVersion, String theSchemaVersion, ISchemaInitializationProvider theSchemaInitializationProvider) {
		super(theProductVersion, theSchemaVersion);
		mySchemaInitializationProvider = theSchemaInitializationProvider;
		setDescription(DESCRIPTION_PREFIX + mySchemaInitializationProvider.getSchemaDescription());
	}

	@Override
	public boolean isRunDuringSchemaInitialization() {
		return true;
	}

	@Override
	public void validate() {
		// nothing
	}

	@Override
	public void doExecute() throws SQLException {
		DriverTypeEnum driverType = getDriverType();

		Set<String> tableNames = JdbcUtils.getTableNames(getConnectionProperties());
		String schemaExistsIndicatorTable = mySchemaInitializationProvider.getSchemaExistsIndicatorTable();
		if (tableNames.contains(schemaExistsIndicatorTable)) {
			logInfo(ourLog, "The table {} already exists.  Skipping schema initialization for {}", schemaExistsIndicatorTable, driverType);
			return;
		}

		logInfo(ourLog, "Initializing {} schema for {}", driverType, mySchemaInitializationProvider.getSchemaDescription());

		List<String> sqlStatements = mySchemaInitializationProvider.getSqlStatements(driverType);

		for (String nextSql : sqlStatements) {
			executeSql(null, nextSql);
		}

		if (mySchemaInitializationProvider.canInitializeSchema()) {
			myInitializedSchema = true;
		}

		logInfo(ourLog, "{} schema for {} initialized successfully", driverType, mySchemaInitializationProvider.getSchemaDescription());
	}

	@Override
	public boolean initializedSchema() {
		return myInitializedSchema;
	}

	@Override
	protected void generateEquals(EqualsBuilder theBuilder, BaseTask theOtherObject) {
		InitializeSchemaTask otherObject = (InitializeSchemaTask) theOtherObject;
		theBuilder.append(mySchemaInitializationProvider, otherObject.mySchemaInitializationProvider);
	}

	@Override
	protected void generateHashCode(HashCodeBuilder theBuilder) {
		theBuilder.append(mySchemaInitializationProvider);
	}

	public ISchemaInitializationProvider getSchemaInitializationProvider() {
		return mySchemaInitializationProvider;
	}
}
