package ca.uhn.fhir.batch2.jobs.reindex;

import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.ArrayList;
import java.util.List;

public class ReindexChunkIds implements IModelJson {

	@JsonProperty("ids")
	private List<Id> myIds;

	public List<Id> getIds() {
		if (myIds == null) {
			myIds = new ArrayList<>();
		}
		return myIds;
	}


	public static class Id implements IModelJson {

		@JsonProperty("type")
		private String myResourceType;
		@JsonProperty("id")
		private String myId;

		public String getResourceType() {
			return myResourceType;
		}

		public Id setResourceType(String theResourceType) {
			myResourceType = theResourceType;
			return this;
		}

		public String getId() {
			return myId;
		}

		public Id setId(String theId) {
			myId = theId;
			return this;
		}

		@Override
		public boolean equals(Object theO) {
			if (this == theO) return true;

			if (theO == null || getClass() != theO.getClass()) return false;

			Id id = (Id) theO;

			return new EqualsBuilder().append(myResourceType, id.myResourceType).append(myId, id.myId).isEquals();
		}

		@Override
		public int hashCode() {
			return new HashCodeBuilder(17, 37).append(myResourceType).append(myId).toHashCode();
		}
	}

}
