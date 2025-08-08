package ca.uhn.fhir.batch2.jobs.chunk;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.model.api.IModelJson;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.annotations.VisibleForTesting;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
	public TypedPidAndVersionListWorkChunkJson(RequestPartitionId theRequestPartitionId, List<TypedPidAndVersionJson> thePids) {
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

	// FIXME: needed?
	public <T extends IResourcePersistentId<?>> List<T> getResourcePersistentIds(
		IIdHelperService<T> theIdHelperService) {
		if (myTypedPidAndVersions.isEmpty()) {
			return Collections.emptyList();
		}

		return myTypedPidAndVersions.stream()
			.map(t -> {
				T retval = t.toPersistentId(theIdHelperService);
				if (t.getVersionId() != null) {
					retval.setVersion(t.getVersionId());
				}
				return retval;
			})
			.collect(Collectors.toList());
	}

	@VisibleForTesting
	public void addTypedPidWithNullPartitionForUnitTest(String theResourceType, Long thePid) {
		getTypedPidAndVersions().add(new TypedPidAndVersionJson(theResourceType, null, thePid.toString(), null));
	}

}
