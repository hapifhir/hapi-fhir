package ca.uhn.fhir.jpa.term;

/*
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

import ca.uhn.fhir.jpa.entity.TermConceptDesignation;

import javax.annotation.Nullable;
import java.util.Collection;

public interface IValueSetConceptAccumulator {

	void addMessage(String theMessage);

	void includeConcept(String theSystem, String theCode, String theDisplay, Long theSourceConceptPid, String theSourceConceptDirectParentPids, @Nullable String theSystemVersion);

	void includeConceptWithDesignations(String theSystem, String theCode, String theDisplay, @Nullable Collection<TermConceptDesignation> theDesignations, Long theSourceConceptPid, String theSourceConceptDirectParentPids, @Nullable String theSystemVersion);

	/**
	 * @return Returns <code>true</code> if the code was actually present and was removed
	 */
	boolean excludeConcept(String theSystem, String theCode);

	@Nullable
	default Integer getCapacityRemaining() {
		return null;
	}

	@Nullable
	default Integer getSkipCountRemaining() {
		return null;
	}

	default boolean isTrackingHierarchy() {
		return true;
	}

	@Nullable
	default void consumeSkipCount(int theSkipCountToConsume) {
		// nothing
	}

	/**
	 * Add or subtract from the total concept count (this is not necessarily the same thing as the number of concepts in
	 * the accumulator, since the <code>offset</code> and <code>count</code> parameters applied to the expansion can cause
	 * concepts to not actually be added.
	 *
	 * @param theAdd   If <code>true</code>, increment. If <code>false</code>, decrement.
	 * @param theDelta The number of codes to add or subtract
	 */
	default void incrementOrDecrementTotalConcepts(boolean theAdd, int theDelta) {
		// nothing
	}

}
