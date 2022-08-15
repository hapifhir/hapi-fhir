package ca.uhn.fhir.model.api;

import org.hl7.fhir.instance.model.api.IBaseMetaType;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.base.composite.BaseContainedDt;
import ca.uhn.fhir.model.base.composite.BaseNarrativeDt;
import ca.uhn.fhir.model.base.resource.ResourceMetadataMap;
import ca.uhn.fhir.model.primitive.CodeDt;
import ca.uhn.fhir.model.primitive.IdDt;

/**
 * This interface is the parent interface for all FHIR Resource definition classes. Classes implementing this interface should be annotated with the {@link ResourceDef @ResourceDef} annotation.
 * 
 * <p>
 * Note that this class is a part of HAPI's model API, used to define structure classes. Users will often interact with this interface, but should not need to implement it directly.
 * </p>
 */
public interface IResource extends ICompositeElement, org.hl7.fhir.instance.model.api.IBaseResource {

	/**
	 * Returns the contained resource list for this resource.
	 * <p>
	 * Usage note: HAPI will generally populate and use the resources from this list automatically (placing inline resources in the contained list when encoding, and copying contained resources from
	 * this list to their appropriate references when parsing) so it is generally not neccesary to interact with this list directly. Instead, in a server you can place resource instances in reference
	 * fields (such as <code>Patient#setManagingOrganization(ResourceReferenceDt)</code> ) and the resource will be automatically contained. In a client, contained resources will be automatically
	 * populated into their appropriate fields by the HAPI parser.
	 * </p>
	 * TODO: document contained resources and link there
	 */
	BaseContainedDt getContained();

	/**
	 * Returns the ID of this resource. Note that this identifier is the URL (or a portion of the URL) used to access this resource, and is not the same thing as any business identifiers stored within
	 * the resource. For example, a Patient resource might have any number of medical record numbers but these are not stored here.
	 * <p>
	 * This ID is specified as the "Logical ID" and "Version ID" in the FHIR specification, see <a href="http://www.hl7.org/implement/standards/fhir/resources.html#metadata">here</a>
	 * </p>
	 */
	IdDt getId();

	/**
	 * Gets the language of the resource itself - <b>NOTE that this language attribute applies to the resource itself, it is not (for example) the language spoken by a practitioner or patient</b>
	 */
	CodeDt getLanguage();

	/**
	 * Returns a view of the {@link #getResourceMetadata() resource metadata} map.
	 * Note that getters from this map return immutable objects, but the <code>addFoo()</code>
	 * and <code>setFoo()</code> methods may be used to modify metadata. 
	 * 
	 * @since 1.5
	 */
	@Override
	IBaseMetaType getMeta();

	/**
	 * Returns the metadata map for this object, creating it if neccesary. Metadata entries are used to get/set feed bundle entries, such as the resource version, or the last updated timestamp.
	 * <p>
	 * Keys in this map are enumerated in the {@link ResourceMetadataKeyEnum}, and each key has a specific value type that it must use.
	 * </p>
	 * 
	 * @see ResourceMetadataKeyEnum for a list of allowable keys and the object types that values of a given key must use.
	 */
	ResourceMetadataMap getResourceMetadata();

	/**
	 * Returns a String representing the name of this Resource. This return value is not used for anything by HAPI itself, but is provided as a convenience to developers using the API.
	 * 
	 * @return the name of this resource, e.g. "Patient", or "Observation"
	 */
	String getResourceName();

	/**
	 * Returns the FHIR version represented by this structure
	 */
	@Override
	public ca.uhn.fhir.context.FhirVersionEnum getStructureFhirVersionEnum();

	/**
	 * Returns the narrative block for this resource
	 */
	BaseNarrativeDt<?> getText();

	/**
	 * Sets the ID of this resource. Note that this identifier is the URL (or a portion of the URL) used to access this resource, and is not the same thing as any business identifiers stored within the
	 * resource. For example, a Patient resource might have any number of medical record numbers but these are not stored here.
	 * <p>
	 * This ID is specified as the "Logical ID" and "Version ID" in the FHIR specification, see <a href="http://www.hl7.org/implement/standards/fhir/resources.html#metadata">here</a>
	 * </p>
	 */
	void setId(IdDt theId);

	/**
	 * Sets the language of the resource itself - <b>NOTE that this language attribute applies to the resource itself, it is not (for example) the language spoken by a practitioner or patient</b>
	 */
	void setLanguage(CodeDt theLanguage);

	/**
	 * Sets the metadata map for this object. Metadata entries are used to get/set feed bundle entries, such as the resource version, or the last updated timestamp.
	 * 
	 * @throws NullPointerException
	 *            The map must not be null
	 */
	void setResourceMetadata(ResourceMetadataMap theMap);

}
