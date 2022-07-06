package ca.uhn.fhir.batch2.jobs.parameters;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.Validate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;

public class UrlListJobParameters extends PartitionedJobParameters {
	@JsonProperty("url")
	@Nullable
	private List<@Pattern(regexp = "^[A-Z][A-Za-z0-9]+\\?.*", message = "If populated, URL must be a search URL in the form '{resourceType}?[params]'") String> myUrls;

	public List<String> getUrls() {
		if (myUrls == null) {
			myUrls = new ArrayList<>();
		}
		return myUrls;
	}

	public UrlListJobParameters addUrl(@Nonnull String theUrl) {
		Validate.notNull(theUrl);
		getUrls().add(theUrl);
		return this;
	}
}
