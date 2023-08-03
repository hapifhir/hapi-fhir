package ca.uhn.fhir.mdm.model;

import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.hl7.fhir.instance.model.api.IAnyResource;

public class MdmUnduplicateGoldenResourceParams {

	private String myGoldenResourceId;

	private IAnyResource myGoldenResource;

	private String myTargetGoldenResourceId;

	private IAnyResource myTargetGoldenResource;

	private MdmTransactionContext myMdmContext;

	private RequestDetails myRequestDetails;

	public IAnyResource getGoldenResource() {
		return myGoldenResource;
	}

	public void setGoldenResource(IAnyResource theGoldenResource) {
		myGoldenResource = theGoldenResource;
	}

	public IAnyResource getTargetGoldenResource() {
		return myTargetGoldenResource;
	}

	public void setTargetGoldenResource(IAnyResource theTargetGoldenResource) {
		myTargetGoldenResource = theTargetGoldenResource;
	}

	public MdmTransactionContext getMdmContext() {
		return myMdmContext;
	}

	public void setMdmContext(MdmTransactionContext theMdmContext) {
		myMdmContext = theMdmContext;
	}

	public RequestDetails getRequestDetails() {
		return myRequestDetails;
	}

	public void setRequestDetails(RequestDetails theRequestDetails) {
		myRequestDetails = theRequestDetails;
	}

	public String getGoldenResourceId() {
		return myGoldenResourceId;
	}

	public void setGoldenResourceId(String theGoldenResourceId) {
		myGoldenResourceId = theGoldenResourceId;
	}

	public String getTargetGoldenResourceId() {
		return myTargetGoldenResourceId;
	}

	public void setTargetGoldenResourceId(String theTargetGoldenResourceId) {
		myTargetGoldenResourceId = theTargetGoldenResourceId;
	}
}
