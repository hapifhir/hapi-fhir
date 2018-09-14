package ca.uhn.fhir.jpa.migrate.taskdef;

/*-
 * #%L
 * HAPI FHIR JPA Server - Migration
 * %%
 * Copyright (C) 2014 - 2018 University Health Network
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

public abstract class BaseTask<T extends BaseTask> {

	private static final Logger ourLog = LoggerFactory.getLogger(BaseTask.class);
	private DriverTypeEnum.ConnectionProperties myConnectionProperties;
	private DriverTypeEnum myDriverType;
	private String myDescription;
	private int myChangesCount;
	private boolean myDryRun;

	public boolean isDryRun() {
		return myDryRun;
	}

	public void setDryRun(boolean theDryRun) {
		myDryRun = theDryRun;
	}

	public String getDescription() {
		return myDescription;
	}

	@SuppressWarnings("unchecked")
	public T setDescription(String theDescription) {
		myDescription = theDescription;
		return (T) this;
	}

	public int getChangesCount() {
		return myChangesCount;
	}

	public void executeSql(@Language("SQL") String theSql, Object... theArguments) {
		if (isDryRun()) {
			logDryRunSql(theSql);
			return;
		}

		Integer changes = getConnectionProperties().getTxTemplate().execute(t -> {
			JdbcTemplate jdbcTemplate = getConnectionProperties().newJdbcTemplate();
			int changesCount = jdbcTemplate.update(theSql, theArguments);
			ourLog.info("SQL \"{}\" returned {}", theSql, changesCount);
			return changesCount;
		});

		myChangesCount += changes;

	}

	protected void logDryRunSql(@Language("SQL") String theSql) {
		ourLog.info("WOULD EXECUTE SQL: {}", theSql);
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
}
