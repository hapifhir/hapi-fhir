package ca.uhn.fhir.model.api;

import java.util.Map;

import ca.uhn.fhir.model.dstu.composite.ContainedDt;
import ca.uhn.fhir.model.dstu.composite.NarrativeDt;

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
	ContainedDt getContained();

	NarrativeDt getText();
	
	/**
	 * Returns the metadata map for this object, creating it if neccesary. Metadata
	 * entries are used to get/set feed bundle entries, such as the
	 * resource version, or the last updated timestamp.
	 */
	Map<ResourceMetadataKeyEnum, Object> getResourceMetadata();
	
	/**
	 * Sets the metadata map for this object. Metadata
	 * entries are used to get/set feed bundle entries, such as the
	 * resource version, or the last updated timestamp.
	 * 
	 * @throws NullPointerException The map must not be null
	 */
	void setResourceMetadata(Map<ResourceMetadataKeyEnum, Object> theMap);
	
}
