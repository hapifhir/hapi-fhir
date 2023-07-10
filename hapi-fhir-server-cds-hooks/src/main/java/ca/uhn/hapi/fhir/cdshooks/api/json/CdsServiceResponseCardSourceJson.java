package ca.uhn.hapi.fhir.cdshooks.api.json;

import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a CDS Hooks Service Response Card Source
 */
public class CdsServiceResponseCardSourceJson implements IModelJson {
	@JsonProperty(value = "label", required = true)
	String myLabel;

	@JsonProperty("url")
	String myUrl;

	@JsonProperty("icon")
	String myIcon;

	@JsonProperty("topic")
	CdsServiceResponseCodingJson myTopic;

	public String getLabel() {
		return myLabel;
	}

	public CdsServiceResponseCardSourceJson setLabel(String theLabel) {
		myLabel = theLabel;
		return this;
	}

	public String getUrl() {
		return myUrl;
	}

	public CdsServiceResponseCardSourceJson setUrl(String theUrl) {
		myUrl = theUrl;
		return this;
	}

	public String getIcon() {
		return myIcon;
	}

	public CdsServiceResponseCardSourceJson setIcon(String theIcon) {
		myIcon = theIcon;
		return this;
	}

	public CdsServiceResponseCodingJson getTopic() {
		return myTopic;
	}

	public CdsServiceResponseCardSourceJson setTopic(CdsServiceResponseCodingJson theTopic) {
		myTopic = theTopic;
		return this;
	}
}
