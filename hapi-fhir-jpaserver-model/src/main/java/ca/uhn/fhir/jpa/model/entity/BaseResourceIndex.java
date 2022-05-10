package ca.uhn.fhir.jpa.model.entity;

/*-
 * #%L
 * HAPI FHIR JPA Model
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

import org.apache.commons.lang3.ObjectUtils;

import javax.persistence.MappedSuperclass;
import java.io.Serializable;

@MappedSuperclass
public abstract class BaseResourceIndex extends BasePartitionable implements Serializable {

	public abstract Long getId();

	public abstract void setId(Long theId);

	public abstract void calculateHashes();

	public abstract void clearHashes();

	@Override
	public void setPartitionId(PartitionablePartitionId thePartitionId) {
		if (ObjectUtils.notEqual(getPartitionId(), thePartitionId)) {
			super.setPartitionId(thePartitionId);
			clearHashes();
		}
	}

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

	public abstract <T extends BaseResourceIndex> void copyMutableValuesFrom(T theSource);

}
