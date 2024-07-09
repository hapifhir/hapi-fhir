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

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

public abstract class BaseTableColumnTypeTask extends BaseTableColumnTask {
	private ColumnTypeEnum myColumnType;
	private Boolean myNullable;
	private Long myColumnLength;

	@Nullable
	private Object myDefaultValue;

	/**
	 * Constructor
	 */
	public BaseTableColumnTypeTask(String theProductVersion, String theSchemaVersion) {
		super(theProductVersion, theSchemaVersion);
	}

	BaseTableColumnTypeTask(
			String theProductVersion,
			String theSchemaVersion,
			ColumnNameCase theColumnNameCase,
			Set<ColumnDriverMappingOverride> theColumnDriverMappingOverrides) {
		super(theProductVersion, theSchemaVersion, theColumnNameCase, theColumnDriverMappingOverrides);
	}

	public ColumnTypeEnum getColumnType() {
		return myColumnType;
	}

	public BaseTableColumnTask setColumnType(ColumnTypeEnum theColumnType) {
		myColumnType = theColumnType;
		return this;
	}

	@Override
	public void validate() {
		super.validate();
		Validate.notNull(myColumnType);
		Validate.notNull(myNullable);

		if (myColumnType == ColumnTypeEnum.STRING) {
			Validate.notNull(
					myColumnLength, "No length specified for " + ColumnTypeEnum.STRING + " column " + getColumnName());
		} else {
			Validate.isTrue(myColumnLength == null);
		}
	}

	protected String getSqlType() {
		return getSqlType(getColumnLength());
	}

	protected String getSqlType(Long theColumnLength) {
		return getSqlType(myColumnType, theColumnLength);
	}

	public boolean isNullable() {
		return myNullable;
	}

	public BaseTableColumnTask setNullable(boolean theNullable) {
		myNullable = theNullable;
		return this;
	}

	protected String getSqlNotNull() {
		return isNullable() ? " null " : " not null";
	}

	public Long getColumnLength() {
		return myColumnLength;
	}

	public BaseTableColumnTypeTask setColumnLength(long theColumnLength) {
		myColumnLength = theColumnLength;
		return this;
	}

	@Nullable
	public Object getDefaultValue() {
		return myDefaultValue;
	}

	@Nonnull
	String buildString(@Nullable Object theValue, Function<Object, String> doIfNull, String theDefaultResult) {
		return Optional.ofNullable(theValue).map(doIfNull).orElse(theDefaultResult);
	}

	public BaseTableColumnTypeTask setDefaultValue(Object theDefaultValue) {
		myDefaultValue = theDefaultValue;
		return this;
	}

	@Override
	protected void generateHashCode(HashCodeBuilder theBuilder) {
		super.generateHashCode(theBuilder);
		theBuilder.append(getColumnTypeName(myColumnType));
		theBuilder.append(myNullable);
		theBuilder.append(myColumnLength);
	}

	@Override
	protected void generateEquals(EqualsBuilder theBuilder, BaseTask theOtherObject) {
		BaseTableColumnTypeTask otherObject = (BaseTableColumnTypeTask) theOtherObject;
		super.generateEquals(theBuilder, otherObject);
		theBuilder.append(getColumnTypeName(myColumnType), getColumnTypeName(otherObject.myColumnType));
		theBuilder.append(myNullable, otherObject.myNullable);
		theBuilder.append(myColumnLength, otherObject.myColumnLength);
	}

	@Nullable
	private Object getColumnTypeName(ColumnTypeEnum theColumnType) {
		if (theColumnType == null) {
			return null;
		}
		return myColumnType.name();
	}
}
