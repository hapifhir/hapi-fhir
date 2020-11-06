package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.util.FhirVersionIndependentConcept;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.r4.model.ValueSet;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

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
}
