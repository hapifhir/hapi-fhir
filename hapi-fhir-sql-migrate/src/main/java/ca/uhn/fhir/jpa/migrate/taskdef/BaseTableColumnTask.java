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

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public abstract class BaseTableColumnTask extends BaseTableTask {

	protected Map<String, Function<BaseColumnCalculatorTask.MandatoryKeyMap<String, Object>, Object>> myCalculators = new HashMap<>();
	protected String myColumnName;
	//If a concrete class decides to, they can define a custom WHERE clause for the task.
	protected String myWhereClause;

	public BaseTableColumnTask(String theProductVersion, String theSchemaVersion) {
		super(theProductVersion, theSchemaVersion);
	}

	public String getColumnName() {
		return myColumnName;
	}

	public BaseTableColumnTask setColumnName(String theColumnName) {
		myColumnName = theColumnName.toUpperCase();
		return this;
	}

	protected String getWhereClause() {
		if (myWhereClause == null) {
			return getColumnName() + " IS NULL";
		} else {
			return myWhereClause;
		}
	}

	protected void setWhereClause(String theWhereClause) {
		this.myWhereClause = theWhereClause;
	}

	@Override
	public void validate() {
		super.validate();
		Validate.notBlank(myColumnName, "Column name not specified");
	}

	@Override
	protected void generateEquals(EqualsBuilder theBuilder, BaseTask theOtherObject) {
		BaseTableColumnTask otherObject = (BaseTableColumnTask) theOtherObject;
		super.generateEquals(theBuilder, otherObject);
		theBuilder.append(myColumnName, otherObject.myColumnName);
	}

	@Override
	protected void generateHashCode(HashCodeBuilder theBuilder) {
		super.generateHashCode(theBuilder);
		theBuilder.append(myColumnName);
	}

	public BaseTableColumnTask addCalculator(String theColumnName, Function<BaseColumnCalculatorTask.MandatoryKeyMap<String, Object>, Object> theConsumer) {
		Validate.isTrue(myCalculators.containsKey(theColumnName) == false);
		myCalculators.put(theColumnName, theConsumer);
		return this;
	}
}
