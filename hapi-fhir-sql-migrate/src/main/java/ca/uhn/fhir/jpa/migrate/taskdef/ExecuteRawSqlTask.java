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
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.intellij.lang.annotations.Language;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExecuteRawSqlTask extends BaseTask {

	private static final Logger ourLog = LoggerFactory.getLogger(ExecuteRawSqlTask.class);
	private Map<DriverTypeEnum, List<String>> myDriverToSqls = new HashMap<>();
	private List<String> myDriverNeutralSqls = new ArrayList<>();

	public ExecuteRawSqlTask(String theProductVersion, String theSchemaVersion) {
		super(theProductVersion, theSchemaVersion);
		setDescription("Execute raw sql");
	}

	public ExecuteRawSqlTask addSql(DriverTypeEnum theDriverType, @Language("SQL") String theSql) {
		Validate.notNull(theDriverType);
		Validate.notBlank(theSql);

		List<String> list = myDriverToSqls.computeIfAbsent(theDriverType, t -> new ArrayList<>());
		list.add(theSql);

		return this;
	}

	public ExecuteRawSqlTask addSql(String theSql) {
		Validate.notBlank("theSql must not be null", theSql);
		myDriverNeutralSqls.add(theSql);

		return this;
	}

	@Override
	public void validate() {
		// nothing
	}

	@Override
	public void doExecute() {
		List<String> sqlStatements = myDriverToSqls.computeIfAbsent(getDriverType(), t -> new ArrayList<>());
		sqlStatements.addAll(myDriverNeutralSqls);

		logInfo(ourLog, "Going to execute {} SQL statements", sqlStatements.size());

		for (String nextSql : sqlStatements) {
			executeSql(null, nextSql);
		}

	}

	@Override
	protected void generateEquals(EqualsBuilder theBuilder, BaseTask theOtherObject) {
		ExecuteRawSqlTask otherObject = (ExecuteRawSqlTask) theOtherObject;
		theBuilder.append(myDriverNeutralSqls, otherObject.myDriverNeutralSqls);
		theBuilder.append(myDriverToSqls, otherObject.myDriverToSqls);
	}

	@Override
	protected void generateHashCode(HashCodeBuilder theBuilder) {
		theBuilder.append(myDriverNeutralSqls);
		theBuilder.append(myDriverToSqls);
	}
}
