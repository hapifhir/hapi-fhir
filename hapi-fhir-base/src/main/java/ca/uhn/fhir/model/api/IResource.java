package ca.uhn.fhir.model.api;

import ca.uhn.fhir.model.dstu.composite.ContainedDt;

public interface IResource extends ICompositeElement {

	/**
	 * Returns the contained resource list for this resource.
	 * <p>
	 * Usage note: HAPI will generally populate and use the resources from this
	 * list automatically (placing inline resources in the contained list when
	 * encoding, and copying contained resources from this list to their
	 * appropriate references when parsing) so it is generally not neccesary to
	 * interact with this list directly.
	 * </p>
	 * TODO: document contained resources and link there
	 */
	public ContainedDt getContained();

}
