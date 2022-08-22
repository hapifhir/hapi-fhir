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

	public void setCount(IPrimitiveType<Integer> theCount) {
		this.theCount = theCount;
	}

	public void setOffset(IPrimitiveType<Integer> theOffset) {
		this.theOffset = theOffset;
	}

	public void setLastUpdated(DateRangeParam theLastUpdated) {
		this.theLastUpdated = theLastUpdated;
	}

	public void setSort(SortSpec theSort) {
		this.theSort = theSort;
	}

	public void setContent(StringAndListParam theContent) {
		this.theContent = theContent;
	}

	public void setNarrative(StringAndListParam theNarrative) {
		this.theNarrative = theNarrative;
	}

	public void setFilter(StringAndListParam theFilter) {
		this.theFilter = theFilter;
	}

	public void setTypes(StringAndListParam theTypes) {
		this.theTypes = theTypes;
	}
}
