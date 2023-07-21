package ca.uhn.fhir.mdm.model.mdmevents;

import ca.uhn.fhir.model.api.IModelJson;

public class MdmMergeEvent implements IModelJson {

	private MdmEventResource myFromResource;

	private MdmEventResource myToResource;

	public MdmEventResource getFromResource() {
		return myFromResource;
	}

	public void setFromResource(MdmEventResource theFromResource) {
		myFromResource = theFromResource;
	}

	public MdmEventResource getToResource() {
		return myToResource;
	}

	public void setToResource(MdmEventResource theToResource) {
		myToResource = theToResource;
	}
}
