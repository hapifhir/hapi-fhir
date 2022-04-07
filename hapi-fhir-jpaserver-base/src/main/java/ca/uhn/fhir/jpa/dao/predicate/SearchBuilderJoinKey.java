package ca.uhn.fhir.jpa.dao.predicate;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class SearchBuilderJoinKey {
	private final SearchBuilderJoinEnum myJoinType;
	private final String myParamName;

	public SearchBuilderJoinKey(String theParamName, SearchBuilderJoinEnum theJoinType) {
		super();
		myParamName = theParamName;
		myJoinType = theJoinType;
	}

	@Override
	public boolean equals(Object theObj) {
		if (!(theObj instanceof SearchBuilderJoinKey)) {
			return false;
		}
		SearchBuilderJoinKey obj = (SearchBuilderJoinKey) theObj;
		return new EqualsBuilder()
			.append(myParamName, obj.myParamName)
			.append(myJoinType, obj.myJoinType)
			.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
			.append(myParamName)
			.append(myJoinType)
			.toHashCode();
	}
}
