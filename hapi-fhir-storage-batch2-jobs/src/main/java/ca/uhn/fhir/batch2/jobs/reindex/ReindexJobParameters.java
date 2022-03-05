package ca.uhn.fhir.batch2.jobs.reindex;

import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.Validate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

public class ReindexJobParameters implements IModelJson {

	@JsonProperty("url")
	@Nullable
	private List<@Pattern(regexp = "^[A-Z][A-Za-z0-9]+\\?.*", message = "If populated, URL must be a search URL in the form '{resourceType}?[params]'") String> myUrl;

	public List<String> getUrl() {
		if (myUrl == null) {
			myUrl = new ArrayList<>();
		}
		return myUrl;
	}

	public ReindexJobParameters addUrl(@Nonnull String theUrl) {
		Validate.notNull(theUrl);
		getUrl().add(theUrl);
		return this;
	}

}
