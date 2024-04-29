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
import jakarta.annotation.Nonnull;

public class ForeignKeyContainer {

	/**
	 * The parent table name
	 */
	private String myParentTableName;

	/**
	 * The name of the column in this table that holds the foreign key
	 */
	private String myColumnName;

	/**
	 * The column data type
	 */
	private ColumnTypeEnum myColumnTypeEnum;

	/**
	 * The name of the column in the parent table (that is the foreign key)
	 */
	private String myParentTableColumnName;

	public ForeignKeyContainer(
			String theColumnName,
			ColumnTypeEnum theColumnTypeEnum,
			String theParentTableName,
			String theParentTableColumnName) {
		myColumnName = theColumnName;
		myColumnTypeEnum = theColumnTypeEnum;
		myParentTableName = theParentTableName;
		myParentTableColumnName = theParentTableColumnName;
	}

	public String getParentTableName() {
		return myParentTableName;
	}

	public void setParentTableName(String theParentTableName) {
		myParentTableName = theParentTableName;
	}

	public String getColumnName() {
		return myColumnName;
	}

	public void setColumnName(String theColumnName) {
		myColumnName = theColumnName;
	}

	public String getParentTableColumnName() {
		return myParentTableColumnName;
	}

	public void setParentTableColumnName(String theParentTableColumnName) {
		myParentTableColumnName = theParentTableColumnName;
	}

	public ColumnTypeEnum getColumnTypeEnum() {
		return myColumnTypeEnum;
	}

	public void setColumnTypeEnum(ColumnTypeEnum theColumnTypeEnum) {
		myColumnTypeEnum = theColumnTypeEnum;
	}

	public String generateSQL(@Nonnull DriverTypeEnum theDriverTypeEnum, boolean thePrettyPrint) {
		switch (theDriverTypeEnum) {
			case MYSQL_5_7:
				return String.format(
						"FOREIGN KEY (%s) REFERENCES %s(%s)", myColumnName, myParentTableName, myParentTableColumnName);
			case MSSQL_2012:
			case ORACLE_12C:
				return String.format(
						"%s %s FOREIGN KEY REFERENCES %s(%s)",
						myColumnName, myColumnTypeEnum.name(), myParentTableName, myParentTableColumnName);
			case POSTGRES_9_4:
				return String.format(
						"FOREIGN KEY(%s) REFERENCES %s(%s)", myColumnName, myParentTableName, myParentTableColumnName);
			default:
				throw new UnsupportedOperationException(
						Msg.code(2232) + " SQL Engine " + theDriverTypeEnum.name() + " not supported for foreign key!");
		}
	}
}
