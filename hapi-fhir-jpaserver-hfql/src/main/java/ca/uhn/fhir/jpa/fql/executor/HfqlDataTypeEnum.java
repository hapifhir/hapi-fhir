/*-
 * #%L
 * HAPI FHIR JPA Server - HFQL Driver
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
package ca.uhn.fhir.jpa.fql.executor;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Types;

public enum HfqlDataTypeEnum {
	STRING(Types.VARCHAR, String.class),
	JSON(Types.VARCHAR, String.class),
	INTEGER(Types.INTEGER, Integer.class),
	BOOLEAN(Types.BOOLEAN, Boolean.class),
	DATE(Types.DATE, Date.class),
	TIMESTAMP(Types.TIMESTAMP_WITH_TIMEZONE, Date.class),
	LONGINT(Types.BIGINT, Long.class),
	TIME(Types.TIME, String.class),
	DECIMAL(Types.DECIMAL, BigDecimal.class);

	private final int mySqlType;

	HfqlDataTypeEnum(int theSqlType, Class<?> theJavaType) {
		mySqlType = theSqlType;
	}

	public int getSqlType() {
		return mySqlType;
	}
}
