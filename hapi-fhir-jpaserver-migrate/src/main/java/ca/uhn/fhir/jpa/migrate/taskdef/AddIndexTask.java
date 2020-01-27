package ca.uhn.fhir.jpa.migrate.taskdef;

/*-
 * #%L
 * HAPI FHIR JPA Server - Migration
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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

import ca.uhn.fhir.jpa.migrate.JdbcUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.util.StringUtils;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class AddIndexTask extends BaseTableTask {

	private static final Logger ourLog = LoggerFactory.getLogger(AddIndexTask.class);
	private String myIndexName;
	private List<String> myColumns;
	private Boolean myUnique;

	public AddIndexTask(String theProductVersion, String theSchemaVersion) {
		super(theProductVersion, theSchemaVersion);
	}

	public void setIndexName(String theIndexName) {
		myIndexName = StringUtils.toUpperCase(theIndexName, Locale.US);
	}

	public void setColumns(List<String> theColumns) {
		myColumns = theColumns;
	}

	public void setUnique(boolean theUnique) {
		myUnique = theUnique;
	}

	@Override
	public void validate() {
		super.validate();
		Validate.notBlank(myIndexName, "Index name not specified");
		Validate.isTrue(myColumns.size() > 0, "Columns not specified for AddIndexTask " + myIndexName + " on table " + getTableName());
		Validate.notNull(myUnique, "Uniqueness not specified");
		setDescription("Add " + myIndexName + " index to table " + getTableName());
	}

	@Override
	public void doExecute() throws SQLException {
		Set<String> indexNames = JdbcUtils.getIndexNames(getConnectionProperties(), getTableName());
		if (indexNames.contains(myIndexName)) {
			logInfo(ourLog, "Index {} already exists on table {} - No action performed", myIndexName, getTableName());
			return;
		}

		logInfo(ourLog, "Going to add a {} index named {} on table {} for columns {}", (myUnique ? "UNIQUE" : "NON-UNIQUE"), myIndexName, getTableName(), myColumns);

		String unique = myUnique ? "unique " : "";
		String columns = String.join(", ", myColumns);
		String sql = "create " + unique + "index " + myIndexName + " on " + getTableName() + "(" + columns + ")";
		String tableName = getTableName();

		try {
			executeSql(tableName, sql);
		} catch (Exception e) {
			if (e.toString().contains("already exists")) {
				ourLog.warn("Index {} already exists", myIndexName);
			} else {
				throw e;
			}
		}
	}

	public void setColumns(String... theColumns) {
		setColumns(Arrays.asList(theColumns));
	}

	@Override
	protected void generateEquals(EqualsBuilder theBuilder, BaseTask theOtherObject) {
		super.generateEquals(theBuilder, theOtherObject);

		AddIndexTask otherObject = (AddIndexTask) theOtherObject;
		theBuilder.append(myIndexName, otherObject.myIndexName);
		theBuilder.append(myColumns, otherObject.myColumns);
		theBuilder.append(myUnique, otherObject.myUnique);

	}

	@Override
	protected void generateHashCode(HashCodeBuilder theBuilder) {
		super.generateHashCode(theBuilder);
		theBuilder.append(myIndexName);
		theBuilder.append(myColumns);
		theBuilder.append(myUnique);
	}
}
