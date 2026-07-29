/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
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
