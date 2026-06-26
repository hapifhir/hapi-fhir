package ca.uhn.fhir.jpa.batch2.jobs.term.valueset.preexpand;

import ca.uhn.fhir.model.api.IModelJson;
import ca.uhn.fhir.util.UrlUtil;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public class PreExpandValueSetParameters implements IModelJson {

	@Nonnull
	@JsonProperty("url")
	private String myUrl;

	@Nullable
	@JsonProperty("version")
	private String myVersion;

	@Nullable
	public String getVersion() {
		return myVersion;
	}

	public void setVersion(@Nullable String theVersion) {
		myVersion = theVersion;
	}

	@Nonnull
	public String getUrl() {
		return myUrl;
	}

	public void setUrl(@Nonnull String theUrl) {
		myUrl = theUrl;
	}


	public UrlUtil.CanonicalUrlParts getCanonicalUrl() {
		return new UrlUtil.CanonicalUrlParts(myUrl, myVersion);
	}
}
