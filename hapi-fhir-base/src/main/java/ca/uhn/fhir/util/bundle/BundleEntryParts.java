package ca.uhn.fhir.util.bundle;

import ca.uhn.fhir.rest.api.RequestTypeEnum;
import org.hl7.fhir.instance.model.api.IBaseResource;

public class BundleEntryParts {
	private final RequestTypeEnum myRequestType;
	private final IBaseResource myResource;
	private final String myUrl;
	private final String myConditionalUrl;

	public BundleEntryParts(RequestTypeEnum theRequestType, String theUrl, IBaseResource theResource, String theConditionalUrl) {
		super();
		myRequestType = theRequestType;
		myUrl = theUrl;
		myResource = theResource;
		myConditionalUrl = theConditionalUrl;
	}

	public RequestTypeEnum getRequestType() {
		return myRequestType;
	}

	public IBaseResource getResource() {
		return myResource;
	}

	public String getConditionalUrl() {
		return myConditionalUrl;
	}

	public String getUrl() {
		return myUrl;
	}
}
