package ca.uhn.fhir.jpa.api.dao;

import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.StringAndListParam;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

public final class FhirResourceDaoPatientQueryParameters {
	private final IPrimitiveType<Integer> theCount;
	private final IPrimitiveType<Integer> theOffset;
	private final DateRangeParam theLastUpdated;
	private final SortSpec theSort;
	private final StringAndListParam theContent;
	private final StringAndListParam theNarrative;
	private final StringAndListParam theFilter;
	private final StringAndListParam theTypes;

	public FhirResourceDaoPatientQueryParameters(IPrimitiveType<Integer> theCount,
																IPrimitiveType<Integer> theOffset,
																DateRangeParam theLastUpdated,
																SortSpec theSort,
																StringAndListParam theContent,
																StringAndListParam theNarrative,
																StringAndListParam theFilter,
																StringAndListParam theTypes) {
		this.theCount = theCount;
		this.theOffset = theOffset;
		this.theLastUpdated = theLastUpdated;
		this.theSort = theSort;
		this.theContent = theContent;
		this.theNarrative = theNarrative;
		this.theFilter = theFilter;
		this.theTypes = theTypes;
	}

	public IPrimitiveType<Integer> getCount() {
		return theCount;
	}

	public IPrimitiveType<Integer> getOffset() {
		return theOffset;
	}

	public DateRangeParam getLastUpdated() {
		return theLastUpdated;
	}

	public SortSpec getSort() {
		return theSort;
	}

	public StringAndListParam getContent() {
		return theContent;
	}

	public StringAndListParam getNarrative() {
		return theNarrative;
	}

	public StringAndListParam getFilter() {
		return theFilter;
	}

	public StringAndListParam getTypes() {
		return theTypes;
	}
}
