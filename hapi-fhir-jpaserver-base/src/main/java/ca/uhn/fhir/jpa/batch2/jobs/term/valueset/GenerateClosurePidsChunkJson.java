package ca.uhn.fhir.jpa.batch2.jobs.term.valueset;

import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class GenerateClosurePidsChunkJson implements IModelJson {

	private List<ConceptPid> myConceptPids;

	public List<ConceptPid> getConceptPids() {
		if (myConceptPids == null) {
			myConceptPids = new ArrayList<>();
		}
		return myConceptPids;
	}

	public static class ConceptPid implements IModelJson {

		@JsonProperty("part")
		private Integer myPartitionId;
		@JsonProperty("id")
		private Long myId;

	}

}
