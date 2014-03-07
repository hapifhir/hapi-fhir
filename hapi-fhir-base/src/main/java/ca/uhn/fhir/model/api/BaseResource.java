package ca.uhn.fhir.model.api;

import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.primitive.CodeDt;
import ca.uhn.fhir.model.primitive.XhtmlDt;

public class BaseResource extends BaseElement implements IResource {

	@Child(name="language", order=0, min=0, max=Child.MAX_UNLIMITED)
	private CodeDt myLanguage;

	@Child(name="text", order=1, min=0, max=1)
	private XhtmlDt myText;

	public CodeDt getLanguage() {
		return myLanguage;
	}

	public XhtmlDt getText() {
		if (myText == null) {
			myText = new XhtmlDt();
		}
		return myText;
	}

	public void setLanguage(CodeDt theLanguage) {
		myLanguage = theLanguage;
	}

	public void setText(XhtmlDt theText) {
		myText = theText;
	}
	
}
