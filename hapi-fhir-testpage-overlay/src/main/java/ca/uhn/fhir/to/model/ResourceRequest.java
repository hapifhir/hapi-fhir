package ca.uhn.fhir.to.model;

import org.springframework.web.bind.annotation.ModelAttribute;

public class ResourceRequest extends HomeRequest {

	private String myUpdateId;
	private String myUpdateVid;

	@ModelAttribute("updateId")
	public String getUpdateId() {
		return myUpdateId;
	}

	@ModelAttribute("updateVid")
	public String getUpdateVid() {
		return myUpdateVid;
	}

	public void setUpdateId(String theUpdateId) {
		myUpdateId = theUpdateId;
	}

	public void setUpdateVid(String theUpdateVid) {
		myUpdateVid = theUpdateVid;
	}

	
}
