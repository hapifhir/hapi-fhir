package ca.uhn.fhir.batch2.jobs.bulkmodify.base;

import ca.uhn.fhir.batch2.jobs.chunk.TypedPidJson;
import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class BulkModifyResourcesResultsJson implements IModelJson {

	@JsonProperty("unchanged")
	private List<TypedPidJson> myUnchangedPids;
	@JsonProperty("changed")
	private List<TypedPidJson> myChangedPids;
	@JsonProperty("failed")
	private List<TypedPidJson> myFailedPids;

}
