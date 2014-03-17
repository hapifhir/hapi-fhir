package ca.uhn.fhir.model.dstu.composite;

import java.util.ArrayList;
import java.util.List;

import ca.uhn.fhir.model.api.IElement;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.DatatypeDef;
import ca.uhn.fhir.model.primitive.IdDt;

@DatatypeDef(name = "duration")
public class ContainedDt implements IElement {

	@Child(name = "resource", type = IResource.class, order = 0, min = 0, max = Child.MAX_UNLIMITED)
	private List<IResource> myContainedResources;

	public List<IResource> getContainedResources() {
		if (myContainedResources == null) {
			myContainedResources = new ArrayList<IResource>();
		}
		return myContainedResources;
	}

	public void setContainedResources(List<IResource> theContainedResources) {
		myContainedResources = theContainedResources;
	}

	@Override
	public boolean isEmpty() {
		return myContainedResources == null || myContainedResources.size() == 0;
	}

	@Override
	public void setId(IdDt theId) {
		throw new UnsupportedOperationException();
	}

	@Override
	public IdDt getId() {
		throw new UnsupportedOperationException();
	}
}
