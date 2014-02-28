package ca.uhn.fhir.model.api;

import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.primitive.XhtmlDt;

public class BaseResource extends BaseElement implements IResource {

	@Child(name="text", order=0, min=0, max=1)
	private XhtmlDt myText;

	public XhtmlDt getText() {
		if (myText == null) {
			myText = new XhtmlDt();
		}
		return myText;
	}

	public void setText(XhtmlDt theText) {
		myText = theText;
	}
	
}
