/*-
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

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.Objects;

public class TokenCommonResPk implements Serializable {
	private Long myResourceId;
	private Integer myPartitionIdValue;
	private long myHashSystemAndValue;

	public TokenCommonResPk() {
		// nothing
	}

	public Long getResourceId() {
		return myResourceId;
	}

	public void setResourceId(Long theResourceId) {
		myResourceId = theResourceId;
	}

	public void setPartitionIdValue(Integer thePartitionId) {
		myPartitionIdValue = thePartitionId;
	}

	public Integer getPartitionIdValue() {
		return myPartitionIdValue;
	}

	public long getHashSystemAndValue() {
		return myHashSystemAndValue;
	}

	public void setHashSystemAndValue(long theHashSystemAndValue) {
		myHashSystemAndValue = theHashSystemAndValue;
	}

	@Override
	public boolean equals(Object theO) {
		if (this == theO) return true;
		if (!(theO instanceof TokenCommonResPk that)) return false;
		return Objects.equals(myResourceId, that.myResourceId)
				&& Objects.equals(myPartitionIdValue, that.myPartitionIdValue)
				&& myHashSystemAndValue == that.myHashSystemAndValue;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
				.append("id", myResourceId)
				.append("partitionId", myPartitionIdValue)
				.append("hashSystemAndValue", myHashSystemAndValue)
				.toString();
	}

	@Override
	public int hashCode() {
		return Objects.hash(myResourceId, myPartitionIdValue, myHashSystemAndValue);
	}
}
