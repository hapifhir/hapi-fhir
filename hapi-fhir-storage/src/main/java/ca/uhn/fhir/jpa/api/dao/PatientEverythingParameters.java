/*-
 * #%L
 * HAPI FHIR Storage api
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.api.dao;

import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.StringAndListParam;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

public final class PatientEverythingParameters {
	@Description(
			formalDefinition =
					"Results from this method are returned across multiple pages. This parameter controls the size of those pages.")
	private IPrimitiveType<Integer> myCount;

	@Description(
			formalDefinition =
					"Results from this method are returned across multiple pages. This parameter controls the offset when fetching a page.")
	private IPrimitiveType<Integer> myOffset;

	@Description(shortDefinition = "Only return resources which were last updated as specified by the given range")
	private DateRangeParam myLastUpdated;

	@Description(shortDefinition = "The order in which to sort the results by")
	private SortSpec mySort;

	@Description(
			shortDefinition =
					"Filter the resources to return only resources matching the given _content filter (note that this filter is applied only to results which link to the given patient, not to the patient itself or to supporting resources linked to by the matched resources)")
	private StringAndListParam myContent;

	@Description(
			shortDefinition =
					"Filter the resources to return only resources matching the given _text filter (note that this filter is applied only to results which link to the given patient, not to the patient itself or to supporting resources linked to by the matched resources)")
	private StringAndListParam myNarrative;

	@Description(
			shortDefinition =
					"Filter the resources to return only resources matching the given _filter filter (note that this filter is applied only to results which link to the given patient, not to the patient itself or to supporting resources linked to by the matched resources)")
	private StringAndListParam myFilter;

	@Description(
			shortDefinition =
					"Filter the resources to return only resources matching the given _type filter (note that this filter is applied only to results which link to the given patient, not to the patient itself or to supporting resources linked to by the matched resources)")
	private StringAndListParam myTypes;

	@Description(
			shortDefinition = "If set to true, trigger an MDM expansion of identifiers corresponding to the resources.")
	private boolean myMdmExpand = false;

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

	public boolean getMdmExpand() {
		return myMdmExpand;
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

	public void setMdmExpand(Boolean myMdmExpand) {
		this.myMdmExpand = myMdmExpand;
	}
}
