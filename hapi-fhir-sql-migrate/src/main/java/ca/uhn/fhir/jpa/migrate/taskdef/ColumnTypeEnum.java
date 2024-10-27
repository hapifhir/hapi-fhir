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

public enum ColumnTypeEnum {
	LONG,
	STRING,
	DATE_ONLY,
	DATE_TIMESTAMP,
	BOOLEAN,
	FLOAT,
	INT,
	TINYINT,
	BLOB,
	CLOB,
	DOUBLE,

	/**
	 * Unlimited length text, with a column definition containing the annotation:
	 * <code>@Column(length=Integer.MAX_VALUE)</code>
	 */
	TEXT,
	/** Long inline binary */
	BINARY,
	BIG_DECIMAL;
}
