package ca.uhn.fhir.jpa.batch2.jobs.term.valueset.preexpand;

import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;

public class LoadAllConceptIdsWorkChunkJson implements IModelJson {

	@JsonProperty("stagingVersionId")
	private String myStagingVersionId;

	public String getStagingVersionId() {
		return myStagingVersionId;
	}

	public void setStagingVersionId(String theStagingVersionId) {
		myStagingVersionId = theStagingVersionId;
	}

}
