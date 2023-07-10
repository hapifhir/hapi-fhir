package ca.uhn.hapi.fhir.cdshooks.api.json;

import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a CDS Hooks Service Response Card
 */
public class CdsServiceResponseCardJson extends BaseCdsServiceJson implements IModelJson {
	@JsonProperty("uuid")
	String myUuid;

	@JsonProperty(value = "summary", required = true)
	String mySummary;

	@JsonProperty("detail")
	String myDetail;

	@JsonProperty(value = "indicator", required = true)
	CdsServiceIndicatorEnum myIndicator;

	@JsonProperty(value = "source", required = true)
	CdsServiceResponseCardSourceJson mySource;

	@JsonProperty("suggestions")
	List<CdsServiceResponseSuggestionJson> mySuggestions;

	@JsonProperty("selectionBehavior")
	String mySelectionBehaviour;

	@JsonProperty("overrideReasons")
	List<CdsServiceResponseCodingJson> myOverrideReasons;

	@JsonProperty("links")
	List<CdsServiceResponseLinkJson> myLinks;

	public String getSummary() {
		return mySummary;
	}

	public CdsServiceResponseCardJson setSummary(String theSummary) {
		mySummary = theSummary;
		return this;
	}

	public CdsServiceIndicatorEnum getIndicator() {
		return myIndicator;
	}

	public CdsServiceResponseCardJson setIndicator(CdsServiceIndicatorEnum theIndicator) {
		myIndicator = theIndicator;
		return this;
	}

	public CdsServiceResponseCardSourceJson getSource() {
		return mySource;
	}

	public CdsServiceResponseCardJson setSource(CdsServiceResponseCardSourceJson theSource) {
		mySource = theSource;
		return this;
	}

	public String getDetail() {
		return myDetail;
	}

	public CdsServiceResponseCardJson setDetail(String theDetail) {
		myDetail = theDetail;
		return this;
	}

	public String getUuid() {
		return myUuid;
	}

	public CdsServiceResponseCardJson setUuid(String theUuid) {
		myUuid = theUuid;
		return this;
	}

	public List<CdsServiceResponseSuggestionJson> getSuggestions() {
		return mySuggestions;
	}

	public void addSuggestion(CdsServiceResponseSuggestionJson theSuggestion) {
		if (mySuggestions == null) {
			mySuggestions = new ArrayList<>();
		}
		mySuggestions.add(theSuggestion);
	}

	public String getSelectionBehaviour() {
		return mySelectionBehaviour;
	}

	public CdsServiceResponseCardJson setSelectionBehaviour(String theSelectionBehaviour) {
		mySelectionBehaviour = theSelectionBehaviour;
		return this;
	}

	public List<CdsServiceResponseCodingJson> getOverrideReasons() {
		return myOverrideReasons;
	}

	public CdsServiceResponseCardJson setOverrideReasons(List<CdsServiceResponseCodingJson> theOverrideReasons) {
		myOverrideReasons = theOverrideReasons;
		return this;
	}

	public List<CdsServiceResponseLinkJson> getLinks() {
		return myLinks;
	}

	public CdsServiceResponseCardJson setLinks(List<CdsServiceResponseLinkJson> theLinks) {
		myLinks = theLinks;
		return this;
	}
}
