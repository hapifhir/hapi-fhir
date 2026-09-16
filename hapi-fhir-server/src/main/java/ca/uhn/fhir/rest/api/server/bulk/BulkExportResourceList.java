package ca.uhn.fhir.rest.api.server.bulk;

import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.ArrayList;
import java.util.List;

public class BulkExportResourceList {

	private List<IBaseResource> myResources;

	public List<IBaseResource> getResources() {
		if (myResources == null) {
			myResources = new ArrayList<>();
		}
		return myResources;
	}

	public void setResources(List<IBaseResource> theResources) {
		myResources = theResources;
	}

	public BulkExportResourceList addResource(IBaseResource theResource) {
		getResources().add(theResource);
		return this;
	}
}
