package ca.uhn.fhir.jpa.batch2.jobs.term.valueset.preexpand;

import ca.uhn.fhir.model.api.IModelJson;
import ca.uhn.fhir.util.UrlUtil;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PreExpandValueSetParameters implements IModelJson {

	@JsonProperty("url")
	private String myUrl;

	@JsonProperty("version")
	private String myVersion;

	@JsonProperty("id")
	private String myId;

	public String getVersion() {
		return myVersion;
	}

	public void setVersion(String theVersion) {
		myVersion = theVersion;
	}

	public String getUrl() {
		return myUrl;
	}

	public void setUrl(String theUrl) {
		myUrl = theUrl;
	}

	public UrlUtil.CanonicalUrlParts getCanonicalUrl() {
		return new UrlUtil.CanonicalUrlParts(myUrl, myVersion);
	}

	public String getId() {
		return myId;
	}

	public void setId(String theId) {
		myId = theId;
	}
}
