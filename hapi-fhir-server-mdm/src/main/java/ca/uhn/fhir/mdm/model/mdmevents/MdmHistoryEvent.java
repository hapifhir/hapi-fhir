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
