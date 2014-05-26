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

import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.InstantDt;

public enum ResourceMetadataKeyEnum {

	/**
	 * If present and populated with a date/time (as an instance of {@link InstantDt}),
	 * this value is an indication that the resource is in the deleted state. This key
	 * is only used in a limited number of scenarios, such as POSTing transaction bundles
	 * to a server, or returning resource history.  
	 * <p>
	 * Values for this key are of type <b>{@link InstantDt}</b>
	 * </p>
	 */
	DELETED_AT,

	/**
	 * The value for this key represents a previous ID used to identify
	 * this resource. This key is currently only used internally during
	 * transaction method processing. 
	 * <p>
	 * Values for this key are of type <b>{@link IdDt}</b>
	 * </p>
	 */
	PREVIOUS_ID,

	/**
	 * The value for this key is the bundle entry <b>Published</b> time. This is
	 * defined by FHIR as "Time resource copied into the feed", which is
	 * generally best left to the current time.
	 * <p>
	 * Values for this key are of type <b>{@link InstantDt}</b>
	 * </p>
	 * <p>
	 * <b>Server Note</b>: In servers, it is generally advisable to leave this
	 * value <code>null</code>, in which case the server will substitute the
	 * current time automatically.
	 * </p>
	 * 
	 * @see InstantDt
	 */
	PUBLISHED,

	/**
	 * The value for this key is the list of tags associated with this resource
	 * <p>
	 * Values for this key are of type <b>{@link TagList}</b>
	 * </p>
	 * 
	 * @see TagList
	 */
	TAG_LIST, 
	
	
	/**
	 * The value for this key is the bundle entry <b>Updated</b> time. This is
	 * defined by FHIR as "Last Updated for resource". This value is also used
	 * for populating the "Last-Modified" header in the case of methods that
	 * return a single resource (read, vread, etc.)
	 * <p>
	 * Values for this key are of type <b>{@link InstantDt}</b>
	 * </p>
	 * 
	 * @see InstantDt
	 */
	UPDATED, 
	
	/**
	 * The value for this key is the version ID of the resource object.
	 * <p>
	 * Values for this key are of type <b>{@link IdDt}</b>
	 * </p>
	 * 
	 * @deprecated The {@link IResource#getId()} resource ID will now be populated with the version ID via the {@link IdDt#getUnqualifiedVersionId()} method
	 */
	@Deprecated
	VERSION_ID;

}
