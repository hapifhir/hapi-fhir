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

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.migrate.DriverTypeEnum;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
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
	private DriverTypeEnum.ConnectionProperties myConnectionProperties;
	private DriverTypeEnum myDriverType;
	private String myDescription;
	private int myChangesCount;
	private boolean myDryRun;

	/**
	 * Some migrations can not be run in a transaction.
	 * When this is true, {@link BaseTask#executeSql} will run without a transaction
	 */
	public void setTransactional(boolean theTransactional) {
		myTransactional = theTransactional;
	}

	private boolean myTransactional = true;
	private boolean myDoNothing;
	private List<ExecutedStatement> myExecutedStatements = new ArrayList<>();
	private Set<DriverTypeEnum> myOnlyAppliesToPlatforms = new HashSet<>();
	private boolean myNoColumnShrink;
	private boolean myFailureAllowed;
	private boolean myRunDuringSchemaInitialization;

	protected BaseTask(String theProductVersion, String theSchemaVersion) {
		myProductVersion = theProductVersion;
		mySchemaVersion = theSchemaVersion;
	}

	public boolean isRunDuringSchemaInitialization() {
		return myRunDuringSchemaInitialization;
	}

	/**
	 * Should this task run even if we're doing the very first initialization of an empty schema. By
	 * default we skip most tasks during that pass, since they just take up time and the
	 * schema should be fully initialized by the {@link InitializeSchemaTask}
	 */
	public void setRunDuringSchemaInitialization(boolean theRunDuringSchemaInitialization) {
		myRunDuringSchemaInitialization = theRunDuringSchemaInitialization;
	}

	public void setOnlyAppliesToPlatforms(Set<DriverTypeEnum> theOnlyAppliesToPlatforms) {
		Validate.notNull(theOnlyAppliesToPlatforms);
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
				changes = getConnectionProperties().getTxTemplate().execute(t -> {
					return doExecuteSql(theSql, theArguments);
				});
			} else {
				changes =  doExecuteSql(theSql, theArguments);
			}

			myChangesCount += changes;
		}

		captureExecutedStatement(theTableName, theSql, theArguments);
	}

	private int doExecuteSql(@Language("SQL") String theSql, Object[] theArguments) {
		JdbcTemplate jdbcTemplate = getConnectionProperties().newJdbcTemplate();
		// 0 means no timeout -- we use this for index rebuilds that may take time.
		jdbcTemplate.setQueryTimeout(0);
		try {
			int changesCount = jdbcTemplate.update(theSql, theArguments);
			if (!"true".equals(System.getProperty("unit_test_mode"))) {
				logInfo(ourLog, "SQL \"{}\" returned {}", theSql, changesCount);
			}
			return changesCount;
		} catch (DataAccessException e) {
			if (myFailureAllowed) {
				ourLog.info("Task {} did not exit successfully, but task is allowed to fail", getFlywayVersion());
				ourLog.debug("Error was: {}", e.getMessage(), e);
				return 0;
			} else {
				throw new DataAccessException(Msg.code(61) + "Failed during task " + getFlywayVersion() + ": " + e, e) {
					private static final long serialVersionUID = 8211678931579252166L;
				};
			}
		}
	}

	protected void captureExecutedStatement(String theTableName, @Language("SQL") String theSql, Object[] theArguments) {
		myExecutedStatements.add(new ExecutedStatement(theTableName, theSql, theArguments));
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
		if (myDoNothing) {
			ourLog.info("Skipping stubbed task: {}", getDescription());
			return;
		}
		if (!myOnlyAppliesToPlatforms.isEmpty()) {
			if (!myOnlyAppliesToPlatforms.contains(getDriverType())) {
				ourLog.debug("Skipping task {} as it does not apply to {}", getDescription(), getDriverType());
				return;
			}
		}
		if (!myOnlyAppliesToPlatforms.isEmpty()) {
			if (!myOnlyAppliesToPlatforms.contains(getDriverType())) {
				ourLog.debug("Skipping task {} as it does not apply to {}", getDescription(), getDriverType());
				return;
			}
		}
		doExecute();
	}

	protected abstract void doExecute() throws SQLException;

	protected boolean isFailureAllowed() {
		return myFailureAllowed;
	}

	public void setFailureAllowed(boolean theFailureAllowed) {
		myFailureAllowed = theFailureAllowed;
	}

	public String getFlywayVersion() {
		String releasePart = myProductVersion;
		if (releasePart.startsWith("V")) {
			releasePart = releasePart.substring(1);
		}
		return releasePart + "." + mySchemaVersion;
	}

	protected void logInfo(Logger theLog, String theFormattedMessage, Object... theArguments) {
		theLog.info(getFlywayVersion() + ": " + theFormattedMessage, theArguments);
	}

	public void validateVersion() {
		Matcher matcher = versionPattern.matcher(mySchemaVersion);
		if (!matcher.matches()) {
			throw new IllegalStateException(Msg.code(62) + "The version " + mySchemaVersion + " does not match the expected pattern " + MIGRATION_VERSION_PATTERN);
		}
	}

	public boolean isDoNothing() {
		return myDoNothing;
	}

	public BaseTask setDoNothing(boolean theDoNothing) {
		myDoNothing = theDoNothing;
		return this;
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
		@SuppressWarnings("unchecked")
		BaseTask otherObject = (BaseTask) theObject;

		EqualsBuilder b = new EqualsBuilder();
		generateEquals(b, otherObject);
		return b.isEquals();
	}

	protected abstract void generateEquals(EqualsBuilder theBuilder, BaseTask theOtherObject);

	public boolean initializedSchema() {
		return false;
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
