package ca.uhn.fhir.jpa.batch2.jobs.term.valueset;

import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PreExpandValueSetResultJson implements IModelJson {

	@JsonProperty("report")
	private String myReport;

}
