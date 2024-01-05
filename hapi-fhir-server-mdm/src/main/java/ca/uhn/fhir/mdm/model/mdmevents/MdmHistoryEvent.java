/*-
 * #%L
 * HAPI FHIR - Master Data Management
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
package ca.uhn.fhir.mdm.model.mdmevents;

import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class MdmHistoryEvent implements IModelJson {

	/**
	 * List of golden resource ids queried.
	 * Can be empty.
	 */
	@JsonProperty("goldenResourceIds")
	private List<String> myGoldenResourceIds;

	/**
	 * List of source ids queried.
	 * Can be empty.
	 */
	@JsonProperty("sourceIds")
	private List<String> mySourceIds;

	/**
	 * The associated link revisions returned from the search.
	 */
	@JsonProperty("mdmLinkRevisions")
	private List<MdmLinkWithRevisionJson> myMdmLinkRevisions;

	public List<String> getGoldenResourceIds() {
		if (myGoldenResourceIds == null) {
			myGoldenResourceIds = new ArrayList<>();
		}
		return myGoldenResourceIds;
	}

	public void setGoldenResourceIds(List<String> theGoldenResourceIds) {
		myGoldenResourceIds = theGoldenResourceIds;
	}

	public List<String> getSourceIds() {
		if (mySourceIds == null) {
			mySourceIds = new ArrayList<>();
		}
		return mySourceIds;
	}

	public void setSourceIds(List<String> theSourceIds) {
		mySourceIds = theSourceIds;
	}

	public List<MdmLinkWithRevisionJson> getMdmLinkRevisions() {
		if (myMdmLinkRevisions == null) {
			myMdmLinkRevisions = new ArrayList<>();
		}
		return myMdmLinkRevisions;
	}

	public void setMdmLinkRevisions(List<MdmLinkWithRevisionJson> theMdmLinkRevisions) {
		myMdmLinkRevisions = theMdmLinkRevisions;
	}
}
