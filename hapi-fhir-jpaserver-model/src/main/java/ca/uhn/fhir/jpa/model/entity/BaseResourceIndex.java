package ca.uhn.fhir.jpa.model.entity;

/*-
 * #%L
 * HAPI FHIR Model
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

@MappedSuperclass
public abstract class BaseResourceIndex implements Serializable {

	@Embedded
	private PartitionId myPartitionId;

	/**
	 * This is here to support queries only, do not set this field directly
	 */
	@SuppressWarnings("unused")
	@Column(name = PartitionId.PARTITION_ID, insertable = false, updatable = false, nullable = true)
	private Integer myPartitionIdValue;

	public PartitionId getPartitionId() {
		return myPartitionId;
	}

	public void setPartitionId(PartitionId thePartitionId) {
		myPartitionId = thePartitionId;
	}

	public abstract Long getId();

	public abstract void setId(Long theId);

	public abstract void calculateHashes();

	/**
	 * Subclasses must implement
	 */
	@Override
	public abstract int hashCode();

	/**
	 * Subclasses must implement
	 */
	@Override
	public abstract boolean equals(Object obj);

}
