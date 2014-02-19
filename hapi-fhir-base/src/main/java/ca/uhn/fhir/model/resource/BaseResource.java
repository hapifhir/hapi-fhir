package ca.uhn.fhir.model.resource;

import ca.uhn.fhir.model.api.BaseElement;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.annotation.Narrative;
import ca.uhn.fhir.model.datatype.NarrativeDt;

public abstract class BaseResource extends BaseElement implements IResource {

	@Narrative(name="text")
	private NarrativeDt myText;

	public NarrativeDt getText() {
		return myText;
	}

	public void setText(NarrativeDt theText) {
		myText = theText;
	}

//	public abstract void setAllChildValues(List<IElement> theChildren);
//	
//	public abstract List<IElement> getAllChildValues();
}
