package ca.uhn.fhir.batch2.jobs.chunk;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.annotations.VisibleForTesting;

import java.util.ArrayList;
import java.util.List;

public class TypedPidAndVersionListWorkChunkJson implements IModelJson {

	@JsonProperty("requestPartitionId")
	private RequestPartitionId myRequestPartitionId;

	@JsonProperty("ids")
	private List<TypedPidAndVersionJson> myTypedPidAndVersions;

	/**
	 * Constructor
	 */
	public TypedPidAndVersionListWorkChunkJson() {
		super();
	}

	/**
	 * Constructor
	 */
	public TypedPidAndVersionListWorkChunkJson(
			RequestPartitionId theRequestPartitionId, List<TypedPidAndVersionJson> thePids) {
		this();
		setRequestPartitionId(theRequestPartitionId);
		setTypedPidAndVersions(thePids);
	}

	public List<TypedPidAndVersionJson> getTypedPidAndVersions() {
		return myTypedPidAndVersions;
	}

	public void setTypedPidAndVersions(List<TypedPidAndVersionJson> theTypedPidAndVersions) {
		myTypedPidAndVersions = theTypedPidAndVersions;
	}

	public RequestPartitionId getRequestPartitionId() {
		return myRequestPartitionId;
	}

	public void setRequestPartitionId(RequestPartitionId theRequestPartitionId) {
		myRequestPartitionId = theRequestPartitionId;
	}

	@VisibleForTesting
	public void addTypedPidWithNullPartitionForUnitTest(String theResourceType, Long thePid, Long theVersionId) {
		if (myTypedPidAndVersions == null) {
			myTypedPidAndVersions = new ArrayList<>();
		}
		myTypedPidAndVersions.add(new TypedPidAndVersionJson(theResourceType, null, thePid.toString(), theVersionId));
	}
}
