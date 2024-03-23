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
import ca.uhn.fhir.jpa.migrate.DriverTypeEnum;

import java.util.HashMap;
import java.util.Map;

public final class ColumnTypeToDriverTypeToSqlType {

	private ColumnTypeToDriverTypeToSqlType() {}

	static Map<ColumnTypeEnum, Map<DriverTypeEnum, String>> myColumnTypeToDriverTypeToSqlType = new HashMap<>();

	static {
		setColumnType(ColumnTypeEnum.INT, DriverTypeEnum.H2_EMBEDDED, "integer");
		setColumnType(ColumnTypeEnum.INT, DriverTypeEnum.DERBY_EMBEDDED, "integer");
		setColumnType(ColumnTypeEnum.INT, DriverTypeEnum.MARIADB_10_1, "integer");
		setColumnType(ColumnTypeEnum.INT, DriverTypeEnum.MYSQL_5_7, "integer");
		setColumnType(ColumnTypeEnum.INT, DriverTypeEnum.MSSQL_2012, "int");
		setColumnType(ColumnTypeEnum.INT, DriverTypeEnum.ORACLE_12C, "number(10,0)");
		setColumnType(ColumnTypeEnum.INT, DriverTypeEnum.POSTGRES_9_4, "int4");

		setColumnType(ColumnTypeEnum.TINYINT, DriverTypeEnum.H2_EMBEDDED, "tinyint");
		setColumnType(ColumnTypeEnum.TINYINT, DriverTypeEnum.DERBY_EMBEDDED, "smallint");
		setColumnType(ColumnTypeEnum.TINYINT, DriverTypeEnum.MARIADB_10_1, "tinyint");
		setColumnType(ColumnTypeEnum.TINYINT, DriverTypeEnum.MYSQL_5_7, "tinyint");
		setColumnType(ColumnTypeEnum.TINYINT, DriverTypeEnum.MSSQL_2012, "smallint");
		setColumnType(ColumnTypeEnum.TINYINT, DriverTypeEnum.ORACLE_12C, "number(3,0)");
		setColumnType(ColumnTypeEnum.TINYINT, DriverTypeEnum.POSTGRES_9_4, "int2");

		setColumnType(ColumnTypeEnum.FLOAT, DriverTypeEnum.H2_EMBEDDED, "float");
		setColumnType(ColumnTypeEnum.FLOAT, DriverTypeEnum.DERBY_EMBEDDED, "float");
		setColumnType(ColumnTypeEnum.FLOAT, DriverTypeEnum.MARIADB_10_1, "float");
		setColumnType(ColumnTypeEnum.FLOAT, DriverTypeEnum.MYSQL_5_7, "float");
		setColumnType(ColumnTypeEnum.FLOAT, DriverTypeEnum.MSSQL_2012, "float");
		setColumnType(ColumnTypeEnum.FLOAT, DriverTypeEnum.ORACLE_12C, "float");
		setColumnType(ColumnTypeEnum.FLOAT, DriverTypeEnum.POSTGRES_9_4, "float");

		setColumnType(ColumnTypeEnum.DOUBLE, DriverTypeEnum.H2_EMBEDDED, "double");
		setColumnType(ColumnTypeEnum.DOUBLE, DriverTypeEnum.DERBY_EMBEDDED, "double");
		setColumnType(ColumnTypeEnum.DOUBLE, DriverTypeEnum.MARIADB_10_1, "double precision");
		setColumnType(ColumnTypeEnum.DOUBLE, DriverTypeEnum.MYSQL_5_7, "double precision");
		setColumnType(ColumnTypeEnum.DOUBLE, DriverTypeEnum.MSSQL_2012, "double precision");
		setColumnType(ColumnTypeEnum.DOUBLE, DriverTypeEnum.ORACLE_12C, "double precision");
		setColumnType(ColumnTypeEnum.DOUBLE, DriverTypeEnum.POSTGRES_9_4, "double precision");

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

		setColumnType(ColumnTypeEnum.DATE_ONLY, DriverTypeEnum.H2_EMBEDDED, "date");
		setColumnType(ColumnTypeEnum.DATE_ONLY, DriverTypeEnum.DERBY_EMBEDDED, "date");
		setColumnType(ColumnTypeEnum.DATE_ONLY, DriverTypeEnum.MARIADB_10_1, "date");
		setColumnType(ColumnTypeEnum.DATE_ONLY, DriverTypeEnum.MYSQL_5_7, "date");
		setColumnType(ColumnTypeEnum.DATE_ONLY, DriverTypeEnum.MSSQL_2012, "date");
		setColumnType(ColumnTypeEnum.DATE_ONLY, DriverTypeEnum.ORACLE_12C, "date");
		setColumnType(ColumnTypeEnum.DATE_ONLY, DriverTypeEnum.POSTGRES_9_4, "date");

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
		setColumnType(
				ColumnTypeEnum.CLOB,
				DriverTypeEnum.POSTGRES_9_4,
				"oid"); // the PG driver will write oid into a `text` column
		setColumnType(ColumnTypeEnum.CLOB, DriverTypeEnum.MSSQL_2012, "varchar(MAX)");

		setColumnType(ColumnTypeEnum.TEXT, DriverTypeEnum.H2_EMBEDDED, "clob");
		setColumnType(ColumnTypeEnum.TEXT, DriverTypeEnum.DERBY_EMBEDDED, "clob");
		setColumnType(ColumnTypeEnum.TEXT, DriverTypeEnum.MARIADB_10_1, "longtext");
		setColumnType(ColumnTypeEnum.TEXT, DriverTypeEnum.MYSQL_5_7, "longtext");
		setColumnType(ColumnTypeEnum.TEXT, DriverTypeEnum.ORACLE_12C, "clob");
		setColumnType(ColumnTypeEnum.TEXT, DriverTypeEnum.POSTGRES_9_4, "text");
		setColumnType(ColumnTypeEnum.TEXT, DriverTypeEnum.MSSQL_2012, "varchar(MAX)");

		setColumnType(ColumnTypeEnum.BINARY, DriverTypeEnum.H2_EMBEDDED, "blob");
		setColumnType(ColumnTypeEnum.BINARY, DriverTypeEnum.DERBY_EMBEDDED, "blob");
		setColumnType(ColumnTypeEnum.BINARY, DriverTypeEnum.MARIADB_10_1, "longblob");
		setColumnType(ColumnTypeEnum.BINARY, DriverTypeEnum.MYSQL_5_7, "longblob");
		setColumnType(ColumnTypeEnum.BINARY, DriverTypeEnum.ORACLE_12C, "blob");
		setColumnType(ColumnTypeEnum.BINARY, DriverTypeEnum.POSTGRES_9_4, "bytea");
		setColumnType(ColumnTypeEnum.BINARY, DriverTypeEnum.MSSQL_2012, "varbinary(MAX)");

		setColumnType(ColumnTypeEnum.BIG_DECIMAL, DriverTypeEnum.H2_EMBEDDED, "numeric(38,2)");
		setColumnType(ColumnTypeEnum.BIG_DECIMAL, DriverTypeEnum.DERBY_EMBEDDED, "decimal(31,2)");
		setColumnType(ColumnTypeEnum.BIG_DECIMAL, DriverTypeEnum.MARIADB_10_1, "decimal(38,2)");
		setColumnType(ColumnTypeEnum.BIG_DECIMAL, DriverTypeEnum.MYSQL_5_7, "decimal(38,2)");
		setColumnType(ColumnTypeEnum.BIG_DECIMAL, DriverTypeEnum.ORACLE_12C, "number(38,2)");
		setColumnType(ColumnTypeEnum.BIG_DECIMAL, DriverTypeEnum.POSTGRES_9_4, "numeric(38,2)");
		setColumnType(ColumnTypeEnum.BIG_DECIMAL, DriverTypeEnum.MSSQL_2012, "numeric(38,2)");
	}

	public static Map<ColumnTypeEnum, Map<DriverTypeEnum, String>> getColumnTypeToDriverTypeToSqlType() {
		return myColumnTypeToDriverTypeToSqlType;
	}

	private static void setColumnType(
			ColumnTypeEnum theColumnType, DriverTypeEnum theDriverType, String theColumnTypeSql) {
		Map<DriverTypeEnum, String> columnSqlType =
				myColumnTypeToDriverTypeToSqlType.computeIfAbsent(theColumnType, k -> new HashMap<>());
		if (columnSqlType.containsKey(theDriverType)) {
			throw new IllegalStateException(Msg.code(65) + "Duplicate key: " + theDriverType);
		}
		columnSqlType.put(theDriverType, theColumnTypeSql);
	}

	public static String toBooleanValue(DriverTypeEnum theDriverType, Boolean theBoolean) {
		switch (theDriverType) {
			case H2_EMBEDDED:
			case DERBY_EMBEDDED:
			case POSTGRES_9_4:
				return theBoolean.toString();
			default:
				return theBoolean ? "1" : "0";
		}
	}
}
