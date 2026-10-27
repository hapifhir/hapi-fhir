/*
 * #%L
 * HAPI FHIR JPA Server
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
package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.jpa.entity.TermConceptDesignation;
import ca.uhn.fhir.util.UrlUtil;
import jakarta.annotation.Nullable;

import java.util.Collection;

public interface IValueSetConceptAccumulator {

	void addMessage(String theMessage);

	/**
	 * Adds a concept to the expansion.
	 *
	 * @param theSystem        The code system URL, without a version
	 * @param theSystemVersion The code system version, or <code>null</code> if none applies
	 */
	void includeConceptWithDesignations(
			String theSystem,
			String theCode,
			String theDisplay,
			@Nullable Collection<TermConceptDesignation> theDesignations,
			Long theSourceConceptPid,
			String theSourceConceptDirectParentPids,
			@Nullable String theSystemVersion);

	/**
	 * Removes a concept from the expansion, from the code system version the exclude names.
	 * <p>
	 * This is the form callers use. Implementations should override it. The default implementation appends the
	 * version to the system as <code>url|version</code> and calls {@link #excludeConcept(String, String)}, so an
	 * implementation which overrides only that method keeps working unchanged.
	 * </p>
	 *
	 * @param theSystem        The code system URL, without a version
	 * @param theSystemVersion The code system version the exclude names, or <code>null</code> to remove the code
	 *                         whatever its version
	 * @param theCode          The code
	 * @since 9.0.0
	 */
	// Created by Claude Opus 5.5
	default void excludeConcept(String theSystem, @Nullable String theSystemVersion, String theCode) {
		excludeConcept(UrlUtil.toCanonicalUrl(theSystem, theSystemVersion), theCode);
	}

	/**
	 * Removes a concept from the expansion.
	 *
	 * @param theSystem The code system URL, with the version the exclude names appended as <code>url|version</code>
	 * @param theCode   The code
	 * @deprecated Use {@link #excludeConcept(String, String, String)}, which names the code system version as its own
	 * parameter instead of leaving it packed into {@literal theSystem}. Implementations may still override this form:
	 * the version-aware one delegates to it by default.
	 */
	@Deprecated(since = "9.0.0")
	void excludeConcept(String theSystem, String theCode);

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
