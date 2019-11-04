package ca.uhn.fhir.jpa.migrate.taskdef;

/*-
 * #%L
 * HAPI FHIR JPA Server - Migration
 * %%
 * Copyright (C) 2014 - 2019 University Health Network
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
import org.intellij.lang.annotations.Language;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class BaseTask<T extends BaseTask> {

	private static final Logger ourLog = LoggerFactory.getLogger(BaseTask.class);
	public static final String MIGRATION_VERSION_PATTERN = "\\d{8}\\.\\d+";
	private static final Pattern versionPattern = Pattern.compile(MIGRATION_VERSION_PATTERN);
	private DriverTypeEnum.ConnectionProperties myConnectionProperties;
	private DriverTypeEnum myDriverType;
	private String myDescription;
	private int myChangesCount;
	private boolean myDryRun;
	private List<ExecutedStatement> myExecutedStatements = new ArrayList<>();
	private boolean myNoColumnShrink;
	private final String myRelease;
	private final String myVersion;

	protected BaseTask(String theRelease, String theVersion) {
		myRelease = theRelease;
		myVersion = theVersion;
	}

	public boolean isNoColumnShrink() {
		return myNoColumnShrink;
	}

	public void setNoColumnShrink(boolean theNoColumnShrink) {
		myNoColumnShrink = theNoColumnShrink;
	}

	public boolean isDryRun() {
		return myDryRun;
	}

	public void setDryRun(boolean theDryRun) {
		myDryRun = theDryRun;
	}

	public String getDescription() {
		if (myDescription == null) {
			return this.getClass().getSimpleName();
		}
		return myDescription;
	}

	@SuppressWarnings("unchecked")
	public T setDescription(String theDescription) {
		myDescription = theDescription;
		return (T) this;
	}

	public List<ExecutedStatement> getExecutedStatements() {
		return myExecutedStatements;
	}

	public int getChangesCount() {
		return myChangesCount;
	}

	/**
	 * @param theTableName This is only used for logging currently
	 * @param theSql       The SQL statement
	 * @param theArguments The SQL statement arguments
	 */
	public void executeSql(String theTableName, @Language("SQL") String theSql, Object... theArguments) {
		if (isDryRun() == false) {
			Integer changes = getConnectionProperties().getTxTemplate().execute(t -> {
				JdbcTemplate jdbcTemplate = getConnectionProperties().newJdbcTemplate();
				int changesCount = jdbcTemplate.update(theSql, theArguments);
				logInfo(ourLog, "SQL \"{}\" returned {}", theSql, changesCount);
				return changesCount;
			});

			myChangesCount += changes;
		}

		captureExecutedStatement(theTableName, theSql, theArguments);
	}

	protected void captureExecutedStatement(String theTableName, @Language("SQL") String theSql, Object[] theArguments) {
		myExecutedStatements.add(new ExecutedStatement(theTableName, theSql, theArguments));
	}

	public DriverTypeEnum.ConnectionProperties getConnectionProperties() {
		return myConnectionProperties;
	}

	public void setConnectionProperties(DriverTypeEnum.ConnectionProperties theConnectionProperties) {
		myConnectionProperties = theConnectionProperties;
	}

	public DriverTypeEnum getDriverType() {
		return myDriverType;
	}

	public void setDriverType(DriverTypeEnum theDriverType) {
		myDriverType = theDriverType;
	}

	public abstract void validate();

	public TransactionTemplate getTxTemplate() {
		return getConnectionProperties().getTxTemplate();
	}

	public JdbcTemplate newJdbcTemnplate() {
		return getConnectionProperties().newJdbcTemplate();
	}

	public abstract void execute() throws SQLException;

	public String getFlywayVersion() {
		String retval = "";
		String releasePart = myRelease;
		if (releasePart.startsWith("V")) {
			releasePart = releasePart.substring(1);
		}
		return releasePart + "." + myVersion;
	}

	protected void logInfo(Logger theLog, String theFormattedMessage, Object... theArguments) {
		theLog.info(getFlywayVersion() + ": " + theFormattedMessage, theArguments);
	}

	public void validateVersion() {
		Matcher matcher = versionPattern.matcher(myVersion);
		if (!matcher.matches()) {
			throw new IllegalStateException("The version " + myVersion + " does not match the expected pattern " + MIGRATION_VERSION_PATTERN);
		}
	}

	public static class ExecutedStatement {
		private final String mySql;
		private final List<Object> myArguments;
		private final String myTableName;

		public ExecutedStatement(String theDescription, String theSql, Object[] theArguments) {
			myTableName = theDescription;
			mySql = theSql;
			myArguments = theArguments != null ? Arrays.asList(theArguments) : Collections.emptyList();
		}

		public String getTableName() {
			return myTableName;
		}

		public String getSql() {
			return mySql;
		}

		public List<Object> getArguments() {
			return myArguments;
		}
	}
}
