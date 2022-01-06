package ca.uhn.fhir.jpa.term;

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

import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.util.FhirVersionIndependentConcept;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.r4.model.ValueSet;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNoneBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

class ExpansionFilter {

	public static final ExpansionFilter NO_FILTER = new ExpansionFilter(null, null);
	private final String myCode;
	private final String mySystem;
	private final List<ValueSet.ConceptSetFilterComponent> myFilters;
	private final Integer myMaxCount;
	/**
	 * Constructor
	 */
	ExpansionFilter(String theSystem, String theCode) {
		this(theSystem, theCode, Collections.emptyList(), null);
	}

	/**
	 * Constructor
	 */
	ExpansionFilter(ExpansionFilter theExpansionFilter, List<ValueSet.ConceptSetFilterComponent> theFilters, Integer theMaxCount) {
		this(theExpansionFilter.getSystem(), theExpansionFilter.getCode(), theFilters, theMaxCount);
	}

	/**
	 * Constructor
	 */
	ExpansionFilter(@Nullable String theSystem, @Nullable String theCode, @Nonnull List<ValueSet.ConceptSetFilterComponent> theFilters, Integer theMaxCount) {
		Validate.isTrue(isNotBlank(theSystem) == isNotBlank(theCode));
		Validate.notNull(theFilters);

		mySystem = theSystem;
		myCode = theCode;
		myFilters = theFilters;
		myMaxCount = theMaxCount;
	}

	public List<ValueSet.ConceptSetFilterComponent> getFilters() {
		return myFilters;
	}

	boolean hasCode() {
		return myCode != null;
	}

	String getCode() {
		return myCode;
	}

	String getSystem() {
		return mySystem;
	}

	/**
	 * Converts the system/code in this filter to a FhirVersionIndependentConcept. This method
	 * should not be called if {@link #hasCode()} returns <code>false</code>
	 */
	@Nonnull
	public FhirVersionIndependentConcept toFhirVersionIndependentConcept() {
		Validate.isTrue(hasCode());

		return new FhirVersionIndependentConcept(mySystem, myCode);
	}

	public Integer getMaxCount() {
		return myMaxCount;
	}

	@Nonnull
	public static ExpansionFilter fromFilterString(@Nullable String theFilter) {
		ExpansionFilter filter;
		if (isNoneBlank(theFilter)) {
			List<ValueSet.ConceptSetFilterComponent> filters = Collections.singletonList(new ValueSet.ConceptSetFilterComponent()
				.setProperty(JpaConstants.VALUESET_FILTER_DISPLAY)
				.setOp(ValueSet.FilterOperator.EQUAL)
				.setValue(theFilter));
			filter = new ExpansionFilter(null, null, filters, null);
		} else {
			filter = ExpansionFilter.NO_FILTER;
		}
		return filter;
	}
}
