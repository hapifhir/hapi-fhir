package ca.uhn.fhir.jpa.batch2.jobs.term.valueset.preexpand;

import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class CompactConceptsWorkChunkJson implements IModelJson {

	@JsonProperty("concepts")
	private List<Concept> myConcepts;

	public List<Concept> getConcepts() {
		if (myConcepts == null) {
			myConcepts = new ArrayList<>();
		}
		return myConcepts;
	}

	public void addConcept(Integer thePartitionId, long theId, int theOrder) {
		getConcepts().add(new Concept(thePartitionId, theId, theOrder));
	}

	public static class Concept implements IModelJson {

		@JsonProperty("part")
		private Integer myPartitionId;

		@JsonProperty("id")
		private long myId;

		@JsonProperty("ord")
		private int myOrder;

		/**
		 * Constructor
		 */
		public Concept() {
			// nothing
		}

		/**
		 * Constructor
		 */
		public Concept(Integer thePartitionId, long theId, int theOrder) {
			myPartitionId = thePartitionId;
			myId = theId;
			myOrder = theOrder;
		}

		public Integer getPartitionId() {
			return myPartitionId;
		}

		public long getId() {
			return myId;
		}

		public int getOrder() {
			return myOrder;
		}
	}
}
