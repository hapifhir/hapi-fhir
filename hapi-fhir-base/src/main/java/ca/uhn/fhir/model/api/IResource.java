package ca.uhn.fhir.model.api;

/*
 * #%L
 * HAPI FHIR Library
 * %%
 * Copyright (C) 2014 University Health Network
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

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
