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
import jakarta.annotation.Nonnull;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class BaseTableTask extends BaseTask {
	private static final Logger ourLog = LoggerFactory.getLogger(BaseTableTask.class);
	private String myTableName;

	private final List<ColumnDriverMappingOverride> myColumnDriverMappingOverrides;

	public BaseTableTask(String theProductVersion, String theSchemaVersion) {
		this(theProductVersion, theSchemaVersion, Collections.emptySet());
	}

	public BaseTableTask(
			String theProductVersion,
			String theSchemaVersion,
			Set<ColumnDriverMappingOverride> theColumnDriverMappingOverrides) {
		super(theProductVersion, theSchemaVersion);
		myColumnDriverMappingOverrides = new ArrayList<>(theColumnDriverMappingOverrides);
	}

	public String getTableName() {
		return myTableName;
	}

	public BaseTableTask setTableName(String theTableName) {
		Validate.notBlank(theTableName);
		myTableName = theTableName;
		return this;
	}

	@Override
	public void validate() {
		Validate.notBlank(myTableName);
	}

	@Override
	protected void generateEquals(EqualsBuilder theBuilder, BaseTask theOtherObject) {
		BaseTableTask otherObject = (BaseTableTask) theOtherObject;
		theBuilder.append(myTableName, otherObject.myTableName);
	}

	protected String getSqlType(ColumnTypeEnum theColumnType, Long theColumnLength) {
		final String retVal = getColumnSqlWithToken(theColumnType);

		Objects.requireNonNull(retVal);

		if (theColumnType == ColumnTypeEnum.STRING) {
			return retVal.replace("?", Long.toString(theColumnLength));
		}

		return retVal;
	}

	@Override
	protected void generateHashCode(HashCodeBuilder theBuilder) {
		theBuilder.append(myTableName);
	}

	@Nonnull
	private String getColumnSqlWithToken(ColumnTypeEnum theColumnType) {
		final List<ColumnDriverMappingOverride> eligibleOverrides = myColumnDriverMappingOverrides.stream()
				.filter(override -> override.getColumnType() == theColumnType)
				.filter(override -> override.getDriverType() == getDriverType())
				.collect(Collectors.toUnmodifiableList());

		if (eligibleOverrides.size() > 1) {
			ourLog.info("There is more than one eligible override: {}.  Picking the first one", eligibleOverrides);
		}

		if (eligibleOverrides.size() == 1) {
			return eligibleOverrides.get(0).getColumnTypeSql();
		}

		if (!ColumnTypeToDriverTypeToSqlType.getColumnTypeToDriverTypeToSqlType()
				.containsKey(theColumnType)) {
			throw new IllegalArgumentException(Msg.code(2449) + "Column type does not exist: " + theColumnType);
		}

		return ColumnTypeToDriverTypeToSqlType.getColumnTypeToDriverTypeToSqlType()
				.get(theColumnType)
				.get(getDriverType());
	}
}
