package ca.uhn.fhir.model.dstu.resource;

import java.util.List;

import ca.uhn.fhir.model.api.BaseElement;
import ca.uhn.fhir.model.api.IElement;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.dstu.composite.ContainedDt;

@ResourceDef(name="Binary", profile="http://hl7.org/fhir/profiles/Binary", id="binary")
public class Binary  extends BaseElement implements IResource {

	// TODO: implement binary
	
	@Override
	public boolean isEmpty() {
		return true;
	}

	@Override
	public List<IElement> getAllPopulatedChildElements() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ContainedDt getContained() {
		// TODO Auto-generated method stub
		return null;
	}

}
