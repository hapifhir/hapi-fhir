package ca.uhn.fhir.batch2.jobs.reindex;

import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.Nullable;
import javax.validation.constraints.Pattern;

public class ReindexJobParameters implements IModelJson {

	@JsonProperty("url")
	@Pattern(regexp = "^[A-Z][A-Za-z0-9]+\\?.*", message = "If populated, URL must be a search URL in the form '{resourceType}?[params]'")
	@Nullable
	private String myUrl;

	public String getUrl() {
		return myUrl;
	}

	public void setUrl(String theUrl) {
		myUrl = theUrl;
	}


}
