package ca.uhn.fhir.jpa.migrate;

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
import ca.uhn.fhir.jpa.migrate.taskdef.BaseTask;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class Migrator {

	private static final Logger ourLog = LoggerFactory.getLogger(Migrator.class);
	private DriverTypeEnum myDriverType;
	private String myConnectionUrl;
	private String myUsername;
	private String myPassword;
	private List<BaseTask> myTasks = new ArrayList<>();
	private DriverTypeEnum.ConnectionProperties myConnectionProperties;
	private int myChangesCount;
	private boolean myDryRun;
	private List<BaseTask.ExecutedStatement> myExecutedStatements = new ArrayList<>();
	private boolean myNoColumnShrink;

	public int getChangesCount() {
		return myChangesCount;
	}

	public void setDriverType(DriverTypeEnum theDriverType) {
		myDriverType = theDriverType;
	}

	public void setConnectionUrl(String theConnectionUrl) {
		myConnectionUrl = theConnectionUrl;
	}

	public void setUsername(String theUsername) {
		myUsername = theUsername;
	}

	public void setPassword(String thePassword) {
		myPassword = thePassword;
	}

	public void addTask(BaseTask theTask) {
		myTasks.add(theTask);
	}

	public void setDryRun(boolean theDryRun) {
		myDryRun = theDryRun;
	}

	public void migrate() {
		ourLog.info("Starting migration with {} tasks", myTasks.size());

		myConnectionProperties = myDriverType.newConnectionProperties(myConnectionUrl, myUsername, myPassword);
		try {
			for (BaseTask next : myTasks) {
				next.setDriverType(myDriverType);
				next.setConnectionProperties(myConnectionProperties);
				next.setDryRun(myDryRun);
				next.setNoColumnShrink(myNoColumnShrink);
				try {
					next.execute();
				} catch (SQLException e) {
					String description = next.getDescription();
					if (isBlank(description)) {
						description = next.getClass().getSimpleName();
					}
					String prefix = "Failure executing task \"" + description + "\", aborting! Cause: ";
					throw new InternalErrorException(Msg.code(44) + prefix + e.toString(), e);
				}

				myChangesCount += next.getChangesCount();
				myExecutedStatements.addAll(next.getExecutedStatements());
			}
		} finally {
			myConnectionProperties.close();
		}

		ourLog.info("Finished migration of {} tasks", myTasks.size());

		if (myDryRun) {
			StringBuilder statementBuilder = new StringBuilder();
			String lastTable = null;
			for (BaseTask.ExecutedStatement next : myExecutedStatements) {
				if (!Objects.equals(lastTable, next.getTableName())) {
					statementBuilder.append("\n\n-- Table: ").append(next.getTableName()).append("\n");
					lastTable = next.getTableName();
				}

				statementBuilder.append(next.getSql()).append(";\n");

				for (Object nextArg : next.getArguments()) {
					statementBuilder.append("  -- Arg: ").append(nextArg).append("\n");
				}
			}

			ourLog.info("SQL that would be executed:\n\n***********************************\n{}***********************************", statementBuilder);
		}

	}

	public void addTasks(List<BaseTask> theTasks) {
		theTasks.forEach(this::addTask);
	}

	public void setNoColumnShrink(boolean theNoColumnShrink) {
		myNoColumnShrink = theNoColumnShrink;
	}


}
