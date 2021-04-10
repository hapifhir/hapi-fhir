package ca.uhn.fhir.jpa.bulk.imp.model;

import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.List;

public class ResourceListChunk {

	private final List<IBaseResource> myResourceList;
	private final JobFileRowProcessingModeEnum myProcessingMode;

	public ResourceListChunk(List<IBaseResource> theResourceList, JobFileRowProcessingModeEnum theProcessingMode) {
		myResourceList = theResourceList;
		myProcessingMode = theProcessingMode;
	}

	public List<IBaseResource> getResourceList() {
		return myResourceList;
	}

	public JobFileRowProcessingModeEnum getProcessingMode() {
		return myProcessingMode;
	}
}
