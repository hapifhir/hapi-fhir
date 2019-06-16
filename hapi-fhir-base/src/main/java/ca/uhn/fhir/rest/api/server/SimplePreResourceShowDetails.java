package ca.uhn.fhir.rest.api.server;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.List;

public class SimplePreResourceShowDetails implements IPreResourceShowDetails {

	private final List<IBaseResource> myResources;
	private final boolean[] mySubSets;

	public SimplePreResourceShowDetails(IBaseResource theResource) {
		this(Lists.newArrayList(theResource));
	}

	public SimplePreResourceShowDetails(List<IBaseResource> theResources) {
		myResources = theResources;
		mySubSets = new boolean[myResources.size()];
	}

	@Override
	public int size() {
		return myResources.size();
	}

	@Override
	public IBaseResource getResource(int theIndex) {
		return myResources.get(theIndex);
	}

	@Override
	public void setResource(int theIndex, IBaseResource theResource) {
		Validate.isTrue(theIndex >= 0, "Invalid index %d - theIndex must not be < 0", theIndex);
		Validate.isTrue(theIndex < myResources.size(), "Invalid index {} - theIndex must be < %d", theIndex, myResources.size());
		myResources.set(theIndex, theResource);
	}

	@Override
	public void markResourceAtIndexAsSubset(int theIndex) {
		Validate.isTrue(theIndex >= 0, "Invalid index %d - theIndex must not be < 0", theIndex);
		Validate.isTrue(theIndex < myResources.size(), "Invalid index {} - theIndex must be < %d", theIndex, myResources.size());
		mySubSets[theIndex] = true;
	}
}
