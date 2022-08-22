package ca.uhn.fhir.jpa.api.dao;

import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.StringAndListParam;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

public final class PatientEverythingParameters {
	private IPrimitiveType<Integer> theCount;
	private IPrimitiveType<Integer> theOffset;
	private DateRangeParam theLastUpdated;
	private SortSpec theSort;
	private StringAndListParam theContent;
	private StringAndListParam theNarrative;
	private StringAndListParam theFilter;
	private StringAndListParam theTypes;

	public PatientEverythingParameters(IPrimitiveType<Integer> theCount,
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

	public PatientEverythingParameters() {

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

	public PatientEverythingParameters setCount(IPrimitiveType<Integer> theCount) {
		this.theCount = theCount;
		return this;
	}

	public PatientEverythingParameters setOffset(IPrimitiveType<Integer> theOffset) {
		this.theOffset = theOffset;
		return this;
	}

	public PatientEverythingParameters setLastUpdated(DateRangeParam theLastUpdated) {
		this.theLastUpdated = theLastUpdated;
		return this;
	}

	public PatientEverythingParameters setSort(SortSpec theSort) {
		this.theSort = theSort;
		return this;
	}

	public PatientEverythingParameters setContent(StringAndListParam theContent) {
		this.theContent = theContent;
		return this;
	}

	public PatientEverythingParameters setNarrative(StringAndListParam theNarrative) {
		this.theNarrative = theNarrative;
		return this;
	}

	public PatientEverythingParameters setFilter(StringAndListParam theFilter) {
		this.theFilter = theFilter;
		return this;
	}

	public PatientEverythingParameters setTypes(StringAndListParam theTypes) {
		this.theTypes = theTypes;
		return this;
	}

//	PatientEverythingParameters everythingParams = new PatientEverythingParameters();
//			everythingParams.setCount(theCount);
//			everythingParams.setOffset(theOffset);
//			everythingParams.setLastUpdated(theLastUpdated);
//			everythingParams.setSort(theSortSpec);
//			everythingParams.setContent(toStringAndList(theContent));
//			everythingParams.setNarrative(toStringAndList(theNarrative));
//			everythingParams.setFilter(toStringAndList(theFilter));
//			everythingParams.setTypes(toStringAndList(theTypes));

	public PatientEverythingParameters createFhirResourceDaoPatientQueryParameters() {
		return new PatientEverythingParameters(theCount, theOffset, theLastUpdated, theSort, theContent, theNarrative, theFilter, theTypes);
	}
}
