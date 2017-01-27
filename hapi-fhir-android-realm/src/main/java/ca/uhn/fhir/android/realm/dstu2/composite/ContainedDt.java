package ca.uhn.fhir.android.realm.dstu2.composite;

import java.util.ArrayList;
import java.util.List;

import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.annotation.Child;
import io.realm.RealmObject;

public class ContainedDt extends RealmObject {

	@Child(name = "resource", type = IResource.class, order = 0, min = 0, max = Child.MAX_UNLIMITED)
	private List<IResource> myContainedResources;

	public List<IResource> getContainedResources() {
		if (myContainedResources == null) {
			myContainedResources = new ArrayList<IResource>();
		}
		return myContainedResources;
	}

}
