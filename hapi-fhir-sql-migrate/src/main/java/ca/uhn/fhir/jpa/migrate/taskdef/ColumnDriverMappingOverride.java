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

import ca.uhn.fhir.jpa.migrate.DriverTypeEnum;
import jakarta.annotation.Nonnull;

import java.util.Objects;
import java.util.StringJoiner;

/**
 * Capture a single SQL column type text override to the logic in {@link ColumnDriverMappingOverride}, namely,
 * by column type and driver type.
 * <p/>
 * Several overrides can be passed down together at the same time to override said logic.
 */
public class ColumnDriverMappingOverride {
	private final ColumnTypeEnum myColumnType;
	private final DriverTypeEnum myDriverType;

	private final String myColumnTypeSql;

	public ColumnDriverMappingOverride(
			@Nonnull ColumnTypeEnum theColumnType,
			@Nonnull DriverTypeEnum theDriverType,
			@Nonnull String theColumnTypeSql) {
		myColumnType = theColumnType;
		myDriverType = theDriverType;
		myColumnTypeSql = theColumnTypeSql;
	}

	public ColumnTypeEnum getColumnType() {
		return myColumnType;
	}

	public DriverTypeEnum getDriverType() {
		return myDriverType;
	}

	public String getColumnTypeSql() {
		return myColumnTypeSql;
	}

	@Override
	public boolean equals(Object theO) {
		if (this == theO) {
			return true;
		}
		if (theO == null || getClass() != theO.getClass()) {
			return false;
		}
		ColumnDriverMappingOverride that = (ColumnDriverMappingOverride) theO;
		return myColumnType == that.myColumnType
				&& myDriverType == that.myDriverType
				&& Objects.equals(myColumnTypeSql, that.myColumnTypeSql);
	}

	@Override
	public int hashCode() {
		return Objects.hash(myColumnType, myDriverType, myColumnTypeSql);
	}

	@Override
	public String toString() {
		return new StringJoiner(", ", ColumnDriverMappingOverride.class.getSimpleName() + "[", "]")
				.add("myColumnType=" + myColumnType)
				.add("myDriverType=" + myDriverType)
				.add("myColumnTypeSql='" + myColumnTypeSql + "'")
				.toString();
	}
}
