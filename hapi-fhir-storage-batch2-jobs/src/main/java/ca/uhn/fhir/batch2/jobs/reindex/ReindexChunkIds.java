package ca.uhn.fhir.batch2.jobs.reindex;

import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class ReindexChunkIds implements IModelJson {

	@JsonProperty("ids")
	private List<Object> myIds;

	public List<Object> getIds() {
		if (myIds == null) {
			myIds = new ArrayList<>();
		}
		return myIds;
	}

}
