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
import org.apache.commons.lang3.Validate;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public abstract class BaseTableColumnTypeTask<T extends BaseTableTask> extends BaseTableColumnTask<T> {

	private ColumnTypeEnum myColumnType;
	private Map<ColumnTypeEnum, Map<DriverTypeEnum, String>> myColumnTypeToDriverTypeToSqlType = new HashMap<>();
	private Boolean myNullable;
	private Long myColumnLength;

	/**
	 * Constructor
	 */
	BaseTableColumnTypeTask() {
		setColumnType(ColumnTypeEnum.INT, DriverTypeEnum.DERBY_EMBEDDED, "integer");
		setColumnType(ColumnTypeEnum.INT, DriverTypeEnum.MARIADB_10_1, "integer");
		setColumnType(ColumnTypeEnum.INT, DriverTypeEnum.MYSQL_5_7, "integer");
		setColumnType(ColumnTypeEnum.INT, DriverTypeEnum.MSSQL_2012, "int");
		setColumnType(ColumnTypeEnum.INT, DriverTypeEnum.ORACLE_12C, "number(10,0)");
		setColumnType(ColumnTypeEnum.INT, DriverTypeEnum.POSTGRES_9_4, "int4");

		setColumnType(ColumnTypeEnum.LONG, DriverTypeEnum.DERBY_EMBEDDED, "bigint");
		setColumnType(ColumnTypeEnum.LONG, DriverTypeEnum.MARIADB_10_1, "bigint");
		setColumnType(ColumnTypeEnum.LONG, DriverTypeEnum.MYSQL_5_7, "bigint");
		setColumnType(ColumnTypeEnum.LONG, DriverTypeEnum.MSSQL_2012, "bigint");
		setColumnType(ColumnTypeEnum.LONG, DriverTypeEnum.ORACLE_12C, "number(19,0)");
		setColumnType(ColumnTypeEnum.LONG, DriverTypeEnum.POSTGRES_9_4, "int8");

		setColumnType(ColumnTypeEnum.STRING, DriverTypeEnum.DERBY_EMBEDDED, "varchar(?)");
		setColumnType(ColumnTypeEnum.STRING, DriverTypeEnum.MARIADB_10_1, "varchar(?)");
		setColumnType(ColumnTypeEnum.STRING, DriverTypeEnum.MYSQL_5_7, "varchar(?)");
		setColumnType(ColumnTypeEnum.STRING, DriverTypeEnum.MSSQL_2012, "varchar(?)");
		setColumnType(ColumnTypeEnum.STRING, DriverTypeEnum.ORACLE_12C, "varchar2(?)");
		setColumnType(ColumnTypeEnum.STRING, DriverTypeEnum.POSTGRES_9_4, "varchar(?)");

		setColumnType(ColumnTypeEnum.DATE_TIMESTAMP, DriverTypeEnum.DERBY_EMBEDDED, "timestamp");
		setColumnType(ColumnTypeEnum.DATE_TIMESTAMP, DriverTypeEnum.MARIADB_10_1, "datetime(6)");
		setColumnType(ColumnTypeEnum.DATE_TIMESTAMP, DriverTypeEnum.MYSQL_5_7, "datetime(6)");
		setColumnType(ColumnTypeEnum.DATE_TIMESTAMP, DriverTypeEnum.MSSQL_2012, "datetime2");
		setColumnType(ColumnTypeEnum.DATE_TIMESTAMP, DriverTypeEnum.ORACLE_12C, "timestamp");
		setColumnType(ColumnTypeEnum.DATE_TIMESTAMP, DriverTypeEnum.POSTGRES_9_4, "timestamp");
	}

	public ColumnTypeEnum getColumnType() {
		return myColumnType;
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
			Validate.notNull(myColumnLength);
		} else {
			Validate.isTrue(myColumnLength == null);
		}
	}

	@SuppressWarnings("unchecked")
	public T setColumnType(ColumnTypeEnum theColumnType) {
		myColumnType = theColumnType;
		return (T) this;
	}

	protected String getSqlType() {
		String retVal = myColumnTypeToDriverTypeToSqlType.get(myColumnType).get(getDriverType());
		Objects.requireNonNull(retVal);

		if (myColumnType == ColumnTypeEnum.STRING) {
			retVal = retVal.replace("?", Long.toString(getColumnLength()));
		}

		return retVal;
	}

	public boolean isNullable() {
		return myNullable;
	}

	public void setNullable(boolean theNullable) {
		myNullable = theNullable;
	}

	protected String getSqlNotNull() {
		return isNullable() ? " null " : " not null";
	}

	public Long getColumnLength() {
		return myColumnLength;
	}

	public void setColumnLength(int theColumnLength) {
		myColumnLength = (long) theColumnLength;
	}


	public enum ColumnTypeEnum {

		LONG {
			@Override
			public String getDescriptor(Long theColumnLength) {
				Assert.isTrue(theColumnLength == null, "Must not supply a column length");
				return "bigint";
			}
		},
		STRING {
			@Override
			public String getDescriptor(Long theColumnLength) {
				Assert.isTrue(theColumnLength != null, "Must supply a column length");
				return "varchar(" + theColumnLength + ")";
			}
		},
		DATE_TIMESTAMP {
			@Override
			public String getDescriptor(Long theColumnLength) {
				Assert.isTrue(theColumnLength == null, "Must not supply a column length");
				return "timestamp";
			}
		},
		INT {
			@Override
			public String getDescriptor(Long theColumnLength) {
				Assert.isTrue(theColumnLength == null, "Must not supply a column length");
				return "int";
			}
		};


		public abstract String getDescriptor(Long theColumnLength);

	}

}
