/*
 * #%L
 * HAPI FHIR JPA Model
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.model.entity;

import java.util.Objects;

public class ResIdSpIdAndPartitionId extends IdAndPartitionId {

	private Long myResourceId;

	public ResIdSpIdAndPartitionId() {
		// nothing
	}

	public ResIdSpIdAndPartitionId(Long theResourceId, Long theId, Integer thePartitionId) {
		super(theId, thePartitionId);
		myResourceId = theResourceId;
	}

	public Long getResourceId() {
		return myResourceId;
	}

	public void setResourceId(Long theResourceId) {
		myResourceId = theResourceId;
	}

	@Override
	public boolean equals(Object theO) {
		if (this == theO) {
			return true;
		}
		if (!(theO instanceof ResIdSpIdAndPartitionId that)) {
			return false;
		}
		return Objects.equals(myResourceId, that.myResourceId)
				&& Objects.equals(getId(), that.getId())
				&& Objects.equals(getPartitionIdValue(), that.getPartitionIdValue());
	}

	@Override
	public int hashCode() {
		return Objects.hash(myResourceId, getId(), getPartitionIdValue());
	}
}
