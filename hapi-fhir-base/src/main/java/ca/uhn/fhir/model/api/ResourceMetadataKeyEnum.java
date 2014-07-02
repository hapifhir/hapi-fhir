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

import static org.apache.commons.lang3.StringUtils.*;

import java.util.Date;
import java.util.Map;

import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;

public abstract class ResourceMetadataKeyEnum<T> {
		
	/**
	 * If present and populated with a date/time (as an instance of {@link InstantDt}),
	 * this value is an indication that the resource is in the deleted state. This key
	 * is only used in a limited number of scenarios, such as POSTing transaction bundles
	 * to a server, or returning resource history.  
	 * <p>
	 * Values for this key are of type <b>{@link InstantDt}</b>
	 * </p>
	 */
	public static final ResourceMetadataKeyEnum<InstantDt> DELETED_AT = new ResourceMetadataKeyEnum<InstantDt>("DELETED_AT") {
		@Override
		public InstantDt get(IResource theResource) {
			return getInstantFromMetadataOrNullIfNone(theResource.getResourceMetadata(), DELETED_AT);
		}

		@Override
		public void put(IResource theResource, InstantDt theObject) {
			theResource.getResourceMetadata().put(DELETED_AT, theObject);
		}
	};

	/**
	 * The value for this key represents a previous ID used to identify
	 * this resource. This key is currently only used internally during
	 * transaction method processing. 
	 * <p>
	 * Values for this key are of type <b>{@link IdDt}</b>
	 * </p>
	 */
	public static final ResourceMetadataKeyEnum<IdDt> PREVIOUS_ID = new ResourceMetadataKeyEnum<IdDt>("PREVIOUS_ID") {
		@Override
		public IdDt get(IResource theResource) {
			return getIdFromMetadataOrNullIfNone(theResource.getResourceMetadata(), PREVIOUS_ID);
		}

		@Override
		public void put(IResource theResource, IdDt theObject) {
			theResource.getResourceMetadata().put(PREVIOUS_ID, theObject);
		}
	};

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
	public static final ResourceMetadataKeyEnum<InstantDt> PUBLISHED = new ResourceMetadataKeyEnum<InstantDt>("PUBLISHED") {
		@Override
		public InstantDt get(IResource theResource) {
			return getInstantFromMetadataOrNullIfNone(theResource.getResourceMetadata(), PUBLISHED);
		}

		@Override
		public void put(IResource theResource, InstantDt theObject) {
			theResource.getResourceMetadata().put(PUBLISHED, theObject);
		}
	};

	/**
	 * The value for this key is the list of tags associated with this resource
	 * <p>
	 * Values for this key are of type <b>{@link TagList}</b>
	 * </p>
	 * 
	 * @see TagList
	 */
	public static final ResourceMetadataKeyEnum<TagList> TAG_LIST = new ResourceMetadataKeyEnum<TagList>("TAG_LIST") {
		@Override
		public TagList get(IResource theResource) {
			Object retValObj = theResource.getResourceMetadata().get(TAG_LIST);
			if (retValObj == null) {
				return null;
			} else if (retValObj instanceof TagList) {
				if (((TagList) retValObj).isEmpty()) {
					return null;
				} else {
					return (TagList) retValObj;
				}
			}
			throw new InternalErrorException("Found an object of type '" + retValObj.getClass().getCanonicalName() + "' in resource metadata for key " + TAG_LIST.name() + " - Expected " + TagList.class.getCanonicalName());
		}

		@Override
		public void put(IResource theResource, TagList theObject) {
			theResource.getResourceMetadata().put(TAG_LIST, theObject);
		}
	};
	
	
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
	public static final ResourceMetadataKeyEnum<InstantDt> UPDATED = new ResourceMetadataKeyEnum<InstantDt>("UPDATED") {
		@Override
		public InstantDt get(IResource theResource) {
			return getInstantFromMetadataOrNullIfNone(theResource.getResourceMetadata(), UPDATED);
		}

		@Override
		public void put(IResource theResource, InstantDt theObject) {
			theResource.getResourceMetadata().put(UPDATED, theObject);
		}
	}; 
	
	
	/**
	 * The value for this key is the version ID of the resource object.
	 * <p>
	 * Values for this key are of type <b>{@link IdDt}</b>
	 * </p>
	 * 
	 * @deprecated The {@link IResource#getId()} resource ID will now be populated with the version ID via the {@link IdDt#getUnqualifiedVersionId()} method
	 */
	@Deprecated
	public static final ResourceMetadataKeyEnum<IdDt> VERSION_ID = new ResourceMetadataKeyEnum<IdDt>("VERSION_ID") {
		@Override
		public IdDt get(IResource theResource) {
			return getIdFromMetadataOrNullIfNone(theResource.getResourceMetadata(), VERSION_ID);
		}

		@Override
		public void put(IResource theResource, IdDt theObject) {
			theResource.getResourceMetadata().put(VERSION_ID, theObject);
		}
	};
	
	private final String myValue;

	public ResourceMetadataKeyEnum(String theValue) {
		myValue = theValue;
	}

	

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ResourceMetadataKeyEnum<?> other = (ResourceMetadataKeyEnum<?>) obj;
		if (myValue == null) {
			if (other.myValue != null)
				return false;
		} else if (!myValue.equals(other.myValue))
			return false;
		return true;
	}

	public abstract T get(IResource theResource);

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((myValue == null) ? 0 : myValue.hashCode());
		return result;
	}

	public abstract void put(IResource theResource, T theObject);

	@Override
	public String toString() {
		return myValue;
	}

	private String name() {
		return myValue;
	}

		private static IdDt getIdFromMetadataOrNullIfNone(Map<ResourceMetadataKeyEnum<?>, Object> theResourceMetadata, ResourceMetadataKeyEnum<?> theKey) {
			Object retValObj = theResourceMetadata.get(theKey);
			if (retValObj == null) {
				return null;
			} else if (retValObj instanceof String) {
				if (isNotBlank((String) retValObj)) {
					return new IdDt((String) retValObj);
				} else {
					return null;
				}
			} else if (retValObj instanceof IdDt) {
				if (((IdDt) retValObj).isEmpty()) {
					return null;
				} else {
					return (IdDt) retValObj;
				}
			} else if (retValObj instanceof Number) {
				return new IdDt(((Number)retValObj).toString());
			}
			throw new InternalErrorException("Found an object of type '" + retValObj.getClass().getCanonicalName() + "' in resource metadata for key " + theKey.name() + " - Expected " + IdDt.class.getCanonicalName());
		}
		
		private static InstantDt getInstantFromMetadataOrNullIfNone(Map<ResourceMetadataKeyEnum<?>, Object> theResourceMetadata, ResourceMetadataKeyEnum<InstantDt> theKey) {
			Object retValObj = theResourceMetadata.get(theKey);
			if (retValObj == null) {
				return null;
			} else if (retValObj instanceof Date) {
				return new InstantDt((Date) retValObj);
			} else if (retValObj instanceof InstantDt) {
				if (((InstantDt) retValObj).isEmpty()) {
					return null;
				} else {
					return (InstantDt) retValObj;
				}
			}
			throw new InternalErrorException("Found an object of type '" + retValObj.getClass().getCanonicalName() + "' in resource metadata for key " + theKey.name() + " - Expected " + InstantDt.class.getCanonicalName());
		}
		
}
