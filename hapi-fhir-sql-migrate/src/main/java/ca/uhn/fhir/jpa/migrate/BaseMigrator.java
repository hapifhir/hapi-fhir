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

import ca.uhn.fhir.jpa.migrate.taskdef.BaseTask;
import org.apache.commons.lang3.Validate;
import org.flywaydb.core.api.callback.Callback;

import javax.annotation.Nonnull;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public abstract class BaseMigrator implements IMigrator {
	private final List<BaseTask.ExecutedStatement> myExecutedStatements = new ArrayList<>();
	private boolean myDryRun;
	private boolean myNoColumnShrink;
	private boolean mySchemaWasInitialized;
	private DriverTypeEnum myDriverType;
	private DataSource myDataSource;
	private boolean myStrictOrder;
	private List<Callback> myCallbacks = Collections.emptyList();

	@Nonnull
	public List<Callback> getCallbacks() {
		return myCallbacks;
	}

	public void setCallbacks(@Nonnull List<Callback> theCallbacks) {
		Validate.notNull(theCallbacks);
		myCallbacks = theCallbacks;
	}

	public DataSource getDataSource() {
		return myDataSource;
	}

	public void setDataSource(DataSource theDataSource) {
		myDataSource = theDataSource;
	}

	public boolean isDryRun() {
		return myDryRun;
	}

	public void setDryRun(boolean theDryRun) {
		myDryRun = theDryRun;
	}

	public boolean isNoColumnShrink() {
		return myNoColumnShrink;
	}

	public void setNoColumnShrink(boolean theNoColumnShrink) {
		myNoColumnShrink = theNoColumnShrink;
	}

	public DriverTypeEnum getDriverType() {
		return myDriverType;
	}

	public void setDriverType(DriverTypeEnum theDriverType) {
		myDriverType = theDriverType;
	}

	public boolean isStrictOrder() {
		return myStrictOrder;
	}

	public void setStrictOrder(boolean theStrictOrder) {
		myStrictOrder = theStrictOrder;
	}

	public void addExecutedStatements(List theExecutedStatements) {
		myExecutedStatements.addAll(theExecutedStatements);
	}

	protected StringBuilder buildExecutedStatementsString() {
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
		return statementBuilder;
	}

	public boolean isSchemaWasInitialized() {
		return mySchemaWasInitialized;
	}

	public BaseMigrator setSchemaWasInitialized(boolean theSchemaWasInitialized) {
		mySchemaWasInitialized = theSchemaWasInitialized;
		return this;
	}
}
