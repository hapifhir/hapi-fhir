package ca.uhn.fhir.jpa.api.dao;

import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.StringAndListParam;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

public final class PatientEverythingParameters {
	@Description(formalDefinition="Results from this method are returned across multiple pages. This parameter controls the size of those pages.")
	private IPrimitiveType<Integer> myCount;

	@Description(formalDefinition="Results from this method are returned across multiple pages. This parameter controls the offset when fetching a page.")
	private IPrimitiveType<Integer> myOffset;

	@Description(shortDefinition="Only return resources which were last updated as specified by the given range")
	private DateRangeParam myLastUpdated;

	@Description(shortDefinition="The order in which to sort the rsults by")
	private SortSpec mySort;

	@Description(shortDefinition="Filter the resources to return only resources matching the given _content filter (note that this filter is applied only to results which link to the given patient, not to the patient itself or to supporting resources linked to by the matched resources)")
	private StringAndListParam myContent;

	@Description(shortDefinition="Filter the resources to return only resources matching the given _text filter (note that this filter is applied only to results which link to the given patient, not to the patient itself or to supporting resources linked to by the matched resources)")
	private StringAndListParam myNarrative;

	@Description(shortDefinition = "Filter the resources to return only resources matching the given _filter filter (note that this filter is applied only to results which link to the given patient, not to the patient itself or to supporting resources linked to by the matched resources)")
	private StringAndListParam myFilter;

	@Description(shortDefinition = "Filter the resources to return only resources matching the given _type filter (note that this filter is applied only to results which link to the given patient, not to the patient itself or to supporting resources linked to by the matched resources)")
	private StringAndListParam myTypes;

	public IPrimitiveType<Integer> getCount() {
		return myCount;
	}

	public IPrimitiveType<Integer> getOffset() {
		return myOffset;
	}

	public DateRangeParam getLastUpdated() {
		return myLastUpdated;
	}

	public SortSpec getSort() {
		return mySort;
	}

	public StringAndListParam getContent() {
		return myContent;
	}

	public StringAndListParam getNarrative() {
		return myNarrative;
	}

	public StringAndListParam getFilter() {
		return myFilter;
	}

	public StringAndListParam getTypes() {
		return myTypes;
	}

	public void setCount(IPrimitiveType<Integer> theCount) {
		this.myCount = theCount;
	}

	public void setOffset(IPrimitiveType<Integer> theOffset) {
		this.myOffset = theOffset;
	}

	public void setLastUpdated(DateRangeParam theLastUpdated) {
		this.myLastUpdated = theLastUpdated;
	}

	public void setSort(SortSpec theSort) {
		this.mySort = theSort;
	}

	public void setContent(StringAndListParam theContent) {
		this.myContent = theContent;
	}

	public void setNarrative(StringAndListParam theNarrative) {
		this.myNarrative = theNarrative;
	}

	public void setFilter(StringAndListParam theFilter) {
		this.myFilter = theFilter;
	}

	public void setTypes(StringAndListParam theTypes) {
		this.myTypes = theTypes;
	}
}
