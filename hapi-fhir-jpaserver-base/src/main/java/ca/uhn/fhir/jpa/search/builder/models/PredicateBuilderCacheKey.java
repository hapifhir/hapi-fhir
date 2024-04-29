/*-
 * #%L
 * HAPI FHIR JPA Server
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
package ca.uhn.fhir.jpa.search.builder.models;

import com.healthmarketscience.sqlbuilder.dbspec.basic.DbColumn;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class PredicateBuilderCacheKey {
	private final DbColumn myDbColumn;
	private final PredicateBuilderTypeEnum myType;
	private final String myParamName;
	private final int myHashCode;

	public PredicateBuilderCacheKey(DbColumn theDbColumn, PredicateBuilderTypeEnum theType, String theParamName) {
		myDbColumn = theDbColumn;
		myType = theType;
		myParamName = theParamName;
		myHashCode = new HashCodeBuilder()
				.append(myDbColumn)
				.append(myType)
				.append(myParamName)
				.toHashCode();
	}

	@Override
	public boolean equals(Object theO) {
		if (this == theO) {
			return true;
		}

		if (theO == null || getClass() != theO.getClass()) {
			return false;
		}

		PredicateBuilderCacheKey that = (PredicateBuilderCacheKey) theO;

		return new EqualsBuilder()
				.append(myDbColumn, that.myDbColumn)
				.append(myType, that.myType)
				.append(myParamName, that.myParamName)
				.isEquals();
	}

	@Override
	public int hashCode() {
		return myHashCode;
	}
}
