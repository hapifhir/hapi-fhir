/*-
 * #%L
 * HAPI FHIR Server - SQL Migration
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.migrate.taskdef;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.migrate.DriverTypeEnum;
import ca.uhn.fhir.jpa.migrate.HapiMigrationException;
import ca.uhn.fhir.jpa.migrate.tasks.api.TaskFlagEnum;
import ca.uhn.fhir.system.HapiSystemProperties;
import jakarta.annotation.Nonnull;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.flywaydb.core.api.MigrationVersion;
import org.intellij.lang.annotations.Language;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class BaseTask {

	public static final String MIGRATION_VERSION_PATTERN = "\\d{8}\\.\\d+";
	private static final Logger ourLog = LoggerFactory.getLogger(BaseTask.class);
	private static final Pattern versionPattern = Pattern.compile(MIGRATION_VERSION_PATTERN);
	private final String myProductVersion;
	private final String mySchemaVersion;
	private final List<ExecuteTaskPrecondition> myPreconditions = new ArrayList<>();
	private final EnumSet<TaskFlagEnum> myFlags = EnumSet.noneOf(TaskFlagEnum.class);
	private final List<ExecutedStatement> myExecutedStatements = new ArrayList<>();
	/**
	 * Whether to check for existing tables
	 * before generating SQL
	 */
	protected boolean myCheckForExistingTables = true;
	/**
	 * Whether to generate the SQL in a 'readable format'
	 */
	protected boolean myPrettyPrint = false;

	private DriverTypeEnum.ConnectionProperties myConnectionProperties;
	private DriverTypeEnum myDriverType;
	private String myDescription;
	private Integer myChangesCount = 0;
	private boolean myDryRun;
	private boolean myTransactional = true;
	private Set<DriverTypeEnum> myOnlyAppliesToPlatforms = new HashSet<>();
	private boolean myNoColumnShrink;

	protected BaseTask(String theProductVersion, String theSchemaVersion) {
		myProductVersion = theProductVersion;
		mySchemaVersion = theSchemaVersion;
	}

	/**
	 * Adds a flag if it's not already present, otherwise this call is ignored.
	 *
	 * @param theFlag The flag, must not be null
	 */
	public BaseTask addFlag(@Nonnull TaskFlagEnum theFlag) {
		myFlags.add(theFlag);
		return this;
	}

	/**
	 * Some migrations can not be run in a transaction.
	 * When this is true, {@link BaseTask#executeSql} will run without a transaction
	 */
	public void setTransactional(boolean theTransactional) {
		myTransactional = theTransactional;
	}

	public void setPrettyPrint(boolean thePrettyPrint) {
		myPrettyPrint = thePrettyPrint;
	}

	public void setOnlyAppliesToPlatforms(Set<DriverTypeEnum> theOnlyAppliesToPlatforms) {
		Validate.notNull(theOnlyAppliesToPlatforms, "theOnlyAppliesToPlatforms must not be null");
		myOnlyAppliesToPlatforms = theOnlyAppliesToPlatforms;
	}

	public String getProductVersion() {
		return myProductVersion;
	}

	public String getSchemaVersion() {
		return mySchemaVersion;
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

	public BaseTask setDescription(String theDescription) {
		myDescription = theDescription;
		return this;
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
		if (!isDryRun()) {
			Integer changes;
			if (myTransactional) {
				changes = getConnectionProperties().getTxTemplate().execute(t -> doExecuteSql(theSql, theArguments));
			} else {
				changes = doExecuteSql(theSql, theArguments);
			}

			myChangesCount += changes;
		}

		captureExecutedStatement(theTableName, theSql, theArguments);
	}

	protected void executeSqlListInTransaction(String theTableName, List<String> theSqlStatements) {
		if (!isDryRun()) {
			Integer changes;
			changes = getConnectionProperties().getTxTemplate().execute(t -> doExecuteSqlList(theSqlStatements));
			myChangesCount += changes;
		}

		for (@Language("SQL") String sqlStatement : theSqlStatements) {
			captureExecutedStatement(theTableName, sqlStatement);
		}
	}

	private Integer doExecuteSqlList(List<String> theSqlStatements) {
		int changesCount = 0;
		for (@Language("SQL") String nextSql : theSqlStatements) {
			changesCount += doExecuteSql(nextSql);
		}

		return changesCount;
	}

	private int doExecuteSql(@Language("SQL") String theSql, Object... theArguments) {
		JdbcTemplate jdbcTemplate = getConnectionProperties().newJdbcTemplate();
		// 0 means no timeout -- we use this for index rebuilds that may take time.
		jdbcTemplate.setQueryTimeout(0);
		try {
			int changesCount = jdbcTemplate.update(theSql, theArguments);
			if (!HapiSystemProperties.isUnitTestModeEnabled()) {
				logInfo(ourLog, "SQL \"{}\" returned {}", theSql, changesCount);
			}
			return changesCount;
		} catch (DataAccessException e) {
			if (myFlags.contains(TaskFlagEnum.FAILURE_ALLOWED)) {
				ourLog.info(
						"Task {} did not exit successfully on doExecuteSql(), but task is allowed to fail",
						getMigrationVersion());
				ourLog.debug("Error was: {}", e.getMessage(), e);
				return 0;
			} else {
				throw new HapiMigrationException(
						Msg.code(61) + "Failed during task " + getMigrationVersion() + ": " + e, e);
			}
		}
	}

	protected void captureExecutedStatement(
			String theTableName, @Language("SQL") String theSql, Object... theArguments) {
		myExecutedStatements.add(new ExecutedStatement(mySchemaVersion, theTableName, theSql, theArguments));
	}

	public DriverTypeEnum.ConnectionProperties getConnectionProperties() {
		return myConnectionProperties;
	}

	public BaseTask setConnectionProperties(DriverTypeEnum.ConnectionProperties theConnectionProperties) {
		myConnectionProperties = theConnectionProperties;
		return this;
	}

	public DriverTypeEnum getDriverType() {
		return myDriverType;
	}

	public BaseTask setDriverType(DriverTypeEnum theDriverType) {
		myDriverType = theDriverType;
		return this;
	}

	public abstract void validate();

	public TransactionTemplate getTxTemplate() {
		return getConnectionProperties().getTxTemplate();
	}

	public JdbcTemplate newJdbcTemplate() {
		return getConnectionProperties().newJdbcTemplate();
	}

	public void execute() throws SQLException {
		if (myFlags.contains(TaskFlagEnum.DO_NOTHING)) {
			ourLog.info("Skipping stubbed task: {}", getDescription());
			return;
		}
		if (!myOnlyAppliesToPlatforms.isEmpty()) {
			if (!myOnlyAppliesToPlatforms.contains(getDriverType())) {
				ourLog.info("Skipping task {} as it does not apply to {}", getDescription(), getDriverType());
				return;
			}
		}

		for (ExecuteTaskPrecondition precondition : myPreconditions) {
			ourLog.debug("precondition to evaluate: {}", precondition);
			if (!precondition.getPreconditionRunner().get()) {
				ourLog.info(
						"Skipping task since one of the preconditions was not met: {}",
						precondition.getPreconditionReason());
				return;
			}
		}
		doExecute();
	}

	protected abstract void doExecute() throws SQLException;

	public String getMigrationVersion() {
		String releasePart = myProductVersion;
		if (releasePart.startsWith("V")) {
			releasePart = releasePart.substring(1);
		}
		String version = releasePart + "." + mySchemaVersion;
		MigrationVersion migrationVersion = MigrationVersion.fromVersion(version);
		return migrationVersion.getVersion();
	}

	@SuppressWarnings("StringConcatenationArgumentToLogCall")
	protected void logInfo(Logger theLog, String theFormattedMessage, Object... theArguments) {
		theLog.info(getMigrationVersion() + ": " + theFormattedMessage, theArguments);
	}

	public void validateVersion() {
		Matcher matcher = versionPattern.matcher(mySchemaVersion);
		if (!matcher.matches()) {
			throw new IllegalStateException(Msg.code(62) + "The version " + mySchemaVersion
					+ " does not match the expected pattern " + MIGRATION_VERSION_PATTERN);
		}
	}

	public void addPrecondition(ExecuteTaskPrecondition thePrecondition) {
		myPreconditions.add(thePrecondition);
	}

	@Override
	public final int hashCode() {
		HashCodeBuilder builder = new HashCodeBuilder();
		generateHashCode(builder);
		return builder.hashCode();
	}

	protected abstract void generateHashCode(HashCodeBuilder theBuilder);

	@Override
	public final boolean equals(Object theObject) {
		if (theObject == null || getClass().equals(theObject.getClass()) == false) {
			return false;
		}
		BaseTask otherObject = (BaseTask) theObject;

		EqualsBuilder b = new EqualsBuilder();
		generateEquals(b, otherObject);
		return b.isEquals();
	}

	protected abstract void generateEquals(EqualsBuilder theBuilder, BaseTask theOtherObject);

	public boolean initializedSchema() {
		return false;
	}

	public boolean isDoNothing() {
		return myFlags.contains(TaskFlagEnum.DO_NOTHING);
	}

	public boolean isHeavyweightSkippableTask() {
		return myFlags.contains(TaskFlagEnum.HEAVYWEIGHT_SKIP_BY_DEFAULT);
	}

	public boolean hasFlag(TaskFlagEnum theFlag) {
		return myFlags.contains(theFlag);
	}

	public static class ExecutedStatement {
		private final String mySql;
		private final List<Object> myArguments;
		private final String myTableName;
		private final String mySchemaVersion;

		public ExecutedStatement(String theSchemaVersion, String theDescription, String theSql, Object[] theArguments) {
			mySchemaVersion = theSchemaVersion;
			myTableName = theDescription;
			mySql = theSql;
			myArguments = theArguments != null ? Arrays.asList(theArguments) : Collections.emptyList();
		}

		public String getSchemaVersion() {
			return mySchemaVersion;
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

		@Override
		public String toString() {
			return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
					.append("tableName", myTableName)
					.append("sql", mySql)
					.append("arguments", myArguments)
					.toString();
		}
	}
}
