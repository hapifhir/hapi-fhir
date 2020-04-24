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

import ca.uhn.fhir.jpa.migrate.DriverTypeEnum;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public abstract class BaseTableColumnTypeTask extends BaseTableColumnTask {
	private ColumnTypeEnum myColumnType;
	private Map<ColumnTypeEnum, Map<DriverTypeEnum, String>> myColumnTypeToDriverTypeToSqlType = new HashMap<>();
	private Boolean myNullable;
	private Long myColumnLength;

	/**
	 * Constructor
	 */

	public BaseTableColumnTypeTask(String theProductVersion, String theSchemaVersion) {
		super(theProductVersion, theSchemaVersion);
		setColumnType(ColumnTypeEnum.INT, DriverTypeEnum.H2_EMBEDDED, "integer");
		setColumnType(ColumnTypeEnum.INT, DriverTypeEnum.DERBY_EMBEDDED, "integer");
		setColumnType(ColumnTypeEnum.INT, DriverTypeEnum.MARIADB_10_1, "integer");
		setColumnType(ColumnTypeEnum.INT, DriverTypeEnum.MYSQL_5_7, "integer");
		setColumnType(ColumnTypeEnum.INT, DriverTypeEnum.MSSQL_2012, "int");
		setColumnType(ColumnTypeEnum.INT, DriverTypeEnum.ORACLE_12C, "number(10,0)");
		setColumnType(ColumnTypeEnum.INT, DriverTypeEnum.POSTGRES_9_4, "int4");

		setColumnType(ColumnTypeEnum.FLOAT, DriverTypeEnum.H2_EMBEDDED, "float");
		setColumnType(ColumnTypeEnum.FLOAT, DriverTypeEnum.DERBY_EMBEDDED, "float");
		setColumnType(ColumnTypeEnum.FLOAT, DriverTypeEnum.MARIADB_10_1, "float");
		setColumnType(ColumnTypeEnum.FLOAT, DriverTypeEnum.MYSQL_5_7, "float");
		setColumnType(ColumnTypeEnum.FLOAT, DriverTypeEnum.MSSQL_2012, "float");
		setColumnType(ColumnTypeEnum.FLOAT, DriverTypeEnum.ORACLE_12C, "float");
		setColumnType(ColumnTypeEnum.FLOAT, DriverTypeEnum.POSTGRES_9_4, "float");

		setColumnType(ColumnTypeEnum.LONG, DriverTypeEnum.H2_EMBEDDED, "bigint");
		setColumnType(ColumnTypeEnum.LONG, DriverTypeEnum.DERBY_EMBEDDED, "bigint");
		setColumnType(ColumnTypeEnum.LONG, DriverTypeEnum.MARIADB_10_1, "bigint");
		setColumnType(ColumnTypeEnum.LONG, DriverTypeEnum.MYSQL_5_7, "bigint");
		setColumnType(ColumnTypeEnum.LONG, DriverTypeEnum.MSSQL_2012, "bigint");
		setColumnType(ColumnTypeEnum.LONG, DriverTypeEnum.ORACLE_12C, "number(19,0)");
		setColumnType(ColumnTypeEnum.LONG, DriverTypeEnum.POSTGRES_9_4, "int8");

		setColumnType(ColumnTypeEnum.STRING, DriverTypeEnum.H2_EMBEDDED, "varchar(?)");
		setColumnType(ColumnTypeEnum.STRING, DriverTypeEnum.DERBY_EMBEDDED, "varchar(?)");
		setColumnType(ColumnTypeEnum.STRING, DriverTypeEnum.MARIADB_10_1, "varchar(?)");
		setColumnType(ColumnTypeEnum.STRING, DriverTypeEnum.MYSQL_5_7, "varchar(?)");
		setColumnType(ColumnTypeEnum.STRING, DriverTypeEnum.MSSQL_2012, "varchar(?)");
		setColumnType(ColumnTypeEnum.STRING, DriverTypeEnum.ORACLE_12C, "varchar2(?)");
		setColumnType(ColumnTypeEnum.STRING, DriverTypeEnum.POSTGRES_9_4, "varchar(?)");

		setColumnType(ColumnTypeEnum.DATE_TIMESTAMP, DriverTypeEnum.H2_EMBEDDED, "timestamp");
		setColumnType(ColumnTypeEnum.DATE_TIMESTAMP, DriverTypeEnum.DERBY_EMBEDDED, "timestamp");
		setColumnType(ColumnTypeEnum.DATE_TIMESTAMP, DriverTypeEnum.MARIADB_10_1, "datetime(6)");
		setColumnType(ColumnTypeEnum.DATE_TIMESTAMP, DriverTypeEnum.MYSQL_5_7, "datetime(6)");
		setColumnType(ColumnTypeEnum.DATE_TIMESTAMP, DriverTypeEnum.MSSQL_2012, "datetime2");
		setColumnType(ColumnTypeEnum.DATE_TIMESTAMP, DriverTypeEnum.ORACLE_12C, "timestamp");
		setColumnType(ColumnTypeEnum.DATE_TIMESTAMP, DriverTypeEnum.POSTGRES_9_4, "timestamp");

		setColumnType(ColumnTypeEnum.BOOLEAN, DriverTypeEnum.H2_EMBEDDED, "boolean");
		setColumnType(ColumnTypeEnum.BOOLEAN, DriverTypeEnum.DERBY_EMBEDDED, "boolean");
		setColumnType(ColumnTypeEnum.BOOLEAN, DriverTypeEnum.MSSQL_2012, "bit");
		setColumnType(ColumnTypeEnum.BOOLEAN, DriverTypeEnum.MARIADB_10_1, "bit");
		setColumnType(ColumnTypeEnum.BOOLEAN, DriverTypeEnum.ORACLE_12C, "number(1,0)");
		setColumnType(ColumnTypeEnum.BOOLEAN, DriverTypeEnum.POSTGRES_9_4, "boolean");
		setColumnType(ColumnTypeEnum.BOOLEAN, DriverTypeEnum.MYSQL_5_7, "bit");

		setColumnType(ColumnTypeEnum.BLOB, DriverTypeEnum.H2_EMBEDDED, "blob");
		setColumnType(ColumnTypeEnum.BLOB, DriverTypeEnum.DERBY_EMBEDDED, "blob");
		setColumnType(ColumnTypeEnum.BLOB, DriverTypeEnum.MARIADB_10_1, "longblob");
		setColumnType(ColumnTypeEnum.BLOB, DriverTypeEnum.MYSQL_5_7, "longblob");
		setColumnType(ColumnTypeEnum.BLOB, DriverTypeEnum.ORACLE_12C, "blob");
		setColumnType(ColumnTypeEnum.BLOB, DriverTypeEnum.POSTGRES_9_4, "oid");
		setColumnType(ColumnTypeEnum.BLOB, DriverTypeEnum.MSSQL_2012, "varbinary(MAX)");

		setColumnType(ColumnTypeEnum.CLOB, DriverTypeEnum.H2_EMBEDDED, "clob");
		setColumnType(ColumnTypeEnum.CLOB, DriverTypeEnum.DERBY_EMBEDDED, "clob(100000)");
		setColumnType(ColumnTypeEnum.CLOB, DriverTypeEnum.MARIADB_10_1, "longtext");
		setColumnType(ColumnTypeEnum.CLOB, DriverTypeEnum.MYSQL_5_7, "longtext");
		setColumnType(ColumnTypeEnum.CLOB, DriverTypeEnum.ORACLE_12C, "clob");
		setColumnType(ColumnTypeEnum.CLOB, DriverTypeEnum.POSTGRES_9_4, "text");
		setColumnType(ColumnTypeEnum.CLOB, DriverTypeEnum.MSSQL_2012, "varchar(MAX)");
	}

	public ColumnTypeEnum getColumnType() {
		return myColumnType;
	}

	public BaseTableColumnTask setColumnType(ColumnTypeEnum theColumnType) {
		myColumnType = theColumnType;
		return this;
	}

	private void setColumnType(ColumnTypeEnum theColumnType, DriverTypeEnum theDriverType, String theColumnTypeSql) {
		Map<DriverTypeEnum, String> columnSqlType = myColumnTypeToDriverTypeToSqlType.computeIfAbsent(theColumnType, k -> new HashMap<>());
		if (columnSqlType.containsKey(theDriverType)) {
			throw new IllegalStateException("Duplicate key: " + theDriverType);
		}
		columnSqlType.put(theDriverType, theColumnTypeSql);
	}

	@Override
	public void validate() {
		super.validate();
		Validate.notNull(myColumnType);
		Validate.notNull(myNullable);

		if (myColumnType == ColumnTypeEnum.STRING) {
			Validate.notNull(myColumnLength, "No length specified for " + ColumnTypeEnum.STRING + " column " + getColumnName());
		} else {
			Validate.isTrue(myColumnLength == null);
		}
	}

	protected String getSqlType() {
		return getSqlType(getColumnLength());
	}

	protected String getSqlType(Long theColumnLength) {
		String retVal = myColumnTypeToDriverTypeToSqlType.get(myColumnType).get(getDriverType());
		Objects.requireNonNull(retVal);

		if (myColumnType == ColumnTypeEnum.STRING) {
			retVal = retVal.replace("?", Long.toString(theColumnLength));
		}

		return retVal;
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

	public enum ColumnTypeEnum {

		LONG,
		STRING,
		DATE_TIMESTAMP,
		BOOLEAN,
		FLOAT,
		INT,
		BLOB,
		CLOB;

	}
}
