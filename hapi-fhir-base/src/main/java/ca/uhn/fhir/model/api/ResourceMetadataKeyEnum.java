package ca.uhn.fhir.model.api;

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

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.model.base.composite.BaseCodingDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.model.valueset.BundleEntrySearchModeEnum;
import ca.uhn.fhir.model.valueset.BundleEntryTransactionMethodEnum;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * Keys in this map refer to <b>resource metadata keys</b>, which are keys used to access information about specific resource instances that live outside of the resource body. Typically, these are
 * data elements which are sent/receieved in HTTP Headers along with read/create resource requests, or properties which can be found in bundle entries.
 * <p>
 * To access or set resource metadata values, every resource has a metadata map, and this class provides convenient getters/setters for interacting with that map. For example, to get a resource's
 * {@link #UPDATED} value, which is the "last updated" time for that resource, use the following code:
 * </p>
 * <p>
 * <code>InstantDt updated = ResourceMetadataKeyEnum.UPDATED.get(resource);</code>
 * <p>
 * <p>
 * To set this value, use the following:
 * </p>
 * <p>
 * <code>InstantDt update = new InstantDt("2011-01-02T11:22:33.0000Z"); // populate with the actual time<br>
 * ResourceMetadataKeyEnum.UPDATED.put(resource, update);</code>
 * </p>
 * <p>
 * Note that this class is not a Java Enum, and can therefore be extended (this is why it is not actually an Enum). Users of HAPI-FHIR are able to create their own classes extending this class to
 * define their own keys for storage in resource metadata if needed.
 * </p>
 */
public abstract class ResourceMetadataKeyEnum<T> implements Serializable {

	/**
	 * If present and populated with a date/time (as an instance of {@link InstantDt}), this value is an indication that the resource is in the deleted state. This key is only used in a limited number
	 * of scenarios, such as POSTing transaction bundles to a server, or returning resource history.
	 * <p>
	 * Values for this key are of type <b>{@link InstantDt}</b>
	 * </p>
	 */
	public static final ResourceMetadataKeySupportingAnyResource<InstantDt, IPrimitiveType<Date>> DELETED_AT = new ResourceMetadataKeySupportingAnyResource<InstantDt, IPrimitiveType<Date>>("DELETED_AT") {
		private static final long serialVersionUID = 1L;

		@Override
		public InstantDt get(IResource theResource) {
			return getInstantFromMetadataOrNullIfNone(theResource.getResourceMetadata(), DELETED_AT);
		}

		@SuppressWarnings("unchecked")
		@Override
		public IPrimitiveType<Date> get(IAnyResource theResource) {
			return (IPrimitiveType<Date>) theResource.getUserData(DELETED_AT.name());
		}

		@Override
		public void put(IResource theResource, InstantDt theObject) {
			theResource.getResourceMetadata().put(DELETED_AT, theObject);
		}

		@Override
		public void put(IAnyResource theResource, IPrimitiveType<Date> theObject) {
			theResource.setUserData(DELETED_AT.name(), theObject);
		}
	};

	/**
	 * If present and populated with a {@link BundleEntrySearchModeEnum}, contains the "bundle entry search mode", which is the value of the status field in the Bundle entry containing this resource.
	 * The value for this key corresponds to field <code>Bundle.entry.search.mode</code>. This value can be set to provide a status value of "include" for included resources being returned by a
	 * server, or to "match" to indicate that the resource was returned because it matched the given search criteria.
	 * <p>
	 * Note that status is only used in FHIR DSTU2 and later.
	 * </p>
	 * <p>
	 * Values for this key are of type <b>{@link BundleEntrySearchModeEnum}</b>
	 * </p>
	 */
	public static final ResourceMetadataKeySupportingAnyResource<BundleEntrySearchModeEnum, String> ENTRY_SEARCH_MODE = new ResourceMetadataKeySupportingAnyResource<BundleEntrySearchModeEnum, String>("ENTRY_SEARCH_MODE") {
		private static final long serialVersionUID = 1L;

		@Override
		public BundleEntrySearchModeEnum get(IResource theResource) {
			return getEnumFromMetadataOrNullIfNone(theResource.getResourceMetadata(), ENTRY_SEARCH_MODE, BundleEntrySearchModeEnum.class, BundleEntrySearchModeEnum.VALUESET_BINDER);
		}

		@Override
		public String get(IAnyResource theResource) {
			return (String) theResource.getUserData(ENTRY_SEARCH_MODE.name());
		}

		@Override
		public void put(IResource theResource, BundleEntrySearchModeEnum theObject) {
			theResource.getResourceMetadata().put(ENTRY_SEARCH_MODE, theObject);
		}

		@Override
		public void put(IAnyResource theResource, String theObject) {
			theResource.setUserData(ENTRY_SEARCH_MODE.name(), theObject);
		}
	};
	/**
	 * If present and populated with a {@link BundleEntryTransactionMethodEnum}, contains the "bundle entry transaction operation", which is the value of the status field in the Bundle entry
	 * containing this resource. The value for this key corresponds to field <code>Bundle.entry.transaction.operation</code>. This value can be set in resources being transmitted to a server to
	 * provide a status value of "create" or "update" to indicate behaviour the server should observe. It may also be set to similar values (or to "noop") in resources being returned by a server as a
	 * result of a transaction to indicate to the client what operation was actually performed.
	 * <p>
	 * Note that status is only used in FHIR DSTU2 and later.
	 * </p>
	 * <p>
	 * Values for this key are of type <b>{@link BundleEntryTransactionMethodEnum}</b>
	 * </p>
	 */
	public static final ResourceMetadataKeySupportingAnyResource<BundleEntryTransactionMethodEnum, String> ENTRY_TRANSACTION_METHOD = new ResourceMetadataKeySupportingAnyResource<BundleEntryTransactionMethodEnum, String>(
		"ENTRY_TRANSACTION_OPERATION") {
		private static final long serialVersionUID = 1L;

		@Override
		public BundleEntryTransactionMethodEnum get(IResource theResource) {
			return getEnumFromMetadataOrNullIfNone(theResource.getResourceMetadata(), ENTRY_TRANSACTION_METHOD, BundleEntryTransactionMethodEnum.class,
				BundleEntryTransactionMethodEnum.VALUESET_BINDER);
		}

		@Override
		public String get(IAnyResource theResource) {
			return (String) theResource.getUserData(ENTRY_TRANSACTION_METHOD.name());
		}

		@Override
		public void put(IResource theResource, BundleEntryTransactionMethodEnum theObject) {
			theResource.getResourceMetadata().put(ENTRY_TRANSACTION_METHOD, theObject);
		}

		@Override
		public void put(IAnyResource theResource, String theObject) {
			theResource.setUserData(ENTRY_TRANSACTION_METHOD.name(), theObject);
		}

	};

	/**
	 * The value for this key represents a {@link List} of profile IDs that this resource claims to conform to.
	 * <p>
	 * <p>
	 * Values for this key are of type <b>List&lt;IdDt&gt;</b>. Note that the returned list is <i>unmodifiable</i>, so you need to create a new list and call <code>put</code> to change its value.
	 * </p>
	 */
	public static final ResourceMetadataKeyEnum<List<IdDt>> PROFILES = new ResourceMetadataKeyEnum<List<IdDt>>("PROFILES") {
		private static final long serialVersionUID = 1L;

		@Override
		public List<IdDt> get(IResource theResource) {
			return getIdListFromMetadataOrNullIfNone(theResource.getResourceMetadata(), PROFILES);
		}

		@Override
		public void put(IResource theResource, List<IdDt> theObject) {
			theResource.getResourceMetadata().put(PROFILES, theObject);
		}
	};
	/**
	 * The value for this key is the bundle entry <b>Published</b> time. This is defined by FHIR as "Time resource copied into the feed", which is generally best left to the current time.
	 * <p>
	 * Values for this key are of type <b>{@link InstantDt}</b>
	 * </p>
	 * <p>
	 * <b>Server Note</b>: In servers, it is generally advisable to leave this value <code>null</code>, in which case the server will substitute the current time automatically.
	 * </p>
	 *
	 * @see InstantDt
	 */
	public static final ResourceMetadataKeyEnum<InstantDt> PUBLISHED = new ResourceMetadataKeyEnum<InstantDt>("PUBLISHED") {
		private static final long serialVersionUID = 1L;

		@Override
		public InstantDt get(IResource theResource) {
			return getInstantFromMetadataOrNullIfNone(theResource.getResourceMetadata(), PUBLISHED);
		}

		@Override
		public void put(IResource theResource, InstantDt theObject) {
			theResource.getResourceMetadata().put(PUBLISHED, theObject);
		}
	};
	public static final ResourceMetadataKeyEnum<List<BaseCodingDt>> SECURITY_LABELS = new ResourceMetadataKeyEnum<List<BaseCodingDt>>("SECURITY_LABELS") {
		private static final long serialVersionUID = 1L;

		@Override
		public List<BaseCodingDt> get(IResource resource) {
			Object obj = resource.getResourceMetadata().get(SECURITY_LABELS);
			if (obj == null) {
				return null;
			}
			//noinspection unchecked
			return (List<BaseCodingDt>) obj;
		}

		@Override
		public void put(IResource iResource, List<BaseCodingDt> labels) {
			iResource.getResourceMetadata().put(SECURITY_LABELS, labels);
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
		private static final long serialVersionUID = 1L;

		@Override
		public TagList get(IResource theResource) {
			Object retValObj = theResource.getResourceMetadata().get(TAG_LIST);
			if (retValObj == null) {
				return null;
			} else {
				return (TagList) retValObj;
			}
		}

		@Override
		public void put(IResource theResource, TagList theObject) {
			theResource.getResourceMetadata().put(TAG_LIST, theObject);
		}
	};
	/**
	 * The value for this key is the bundle entry <b>Updated</b> time. This is defined by FHIR as "Last Updated for resource". This value is also used for populating the "Last-Modified" header in the
	 * case of methods that return a single resource (read, vread, etc.)
	 * <p>
	 * Values for this key are of type <b>{@link InstantDt}</b>
	 * </p>
	 *
	 * @see InstantDt
	 */
	public static final ResourceMetadataKeyEnum<InstantDt> UPDATED = new ResourceMetadataKeyEnum<InstantDt>("UPDATED") {
		private static final long serialVersionUID = 1L;

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
	 * Values for this key are of type <b>{@link String}</b>
	 * </p>
	 *
	 * @deprecated The {@link IResource#getId()} resource ID will now be populated with the version ID via the {@link IdDt#getVersionIdPart()} method
	 */
	@Deprecated
	public static final ResourceMetadataKeyEnum<String> VERSION = new ResourceMetadataKeyEnum<String>("VERSION") {
		private static final long serialVersionUID = 1L;

		@Override
		public String get(IResource theResource) {
			return getStringFromMetadataOrNullIfNone(theResource.getResourceMetadata(), VERSION);
		}

		@Override
		public void put(IResource theResource, String theObject) {
			theResource.getResourceMetadata().put(VERSION, theObject);
		}
	};
	/**
	 * The value for this key is the version ID of the resource object.
	 * <p>
	 * Values for this key are of type <b>{@link IdDt}</b>
	 * </p>
	 *
	 * @deprecated The {@link IResource#getId()} resource ID will now be populated with the version ID via the {@link IdDt#getVersionIdPart()} method
	 */
	@Deprecated
	public static final ResourceMetadataKeyEnum<IdDt> VERSION_ID = new ResourceMetadataKeyEnum<IdDt>("VERSION_ID") {
		private static final long serialVersionUID = 1L;

		@Override
		public IdDt get(IResource theResource) {
			return getIdFromMetadataOrNullIfNone(theResource.getResourceMetadata());
		}

		@Override
		public void put(IResource theResource, IdDt theObject) {
			theResource.getResourceMetadata().put(VERSION_ID, theObject);
		}
	};
	private static final long serialVersionUID = 1L;
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

	public String name() {
		return myValue;
	}

	public abstract void put(IResource theResource, T theObject);

	public static abstract class ResourceMetadataKeySupportingAnyResource<T, T2> extends ResourceMetadataKeyEnum<T> {

		private static final long serialVersionUID = 1L;

		public ResourceMetadataKeySupportingAnyResource(String theValue) {
			super(theValue);
		}

		public abstract T2 get(IAnyResource theResource);

		public abstract void put(IAnyResource theResource, T2 theObject);

	}

	public static final class ExtensionResourceMetadataKey extends ResourceMetadataKeyEnum<ExtensionDt> {
		public ExtensionResourceMetadataKey(String theUrl) {
			super(theUrl);
		}

		@Override
		public ExtensionDt get(IResource theResource) {
			Object retValObj = theResource.getResourceMetadata().get(this);
			if (retValObj == null) {
				return null;
			} else if (retValObj instanceof ExtensionDt) {
				return (ExtensionDt) retValObj;
			}
			throw new InternalErrorException(Msg.code(1890) + "Found an object of type '" + retValObj.getClass().getCanonicalName()
				+ "' in resource metadata for key " + this.name() + " - Expected "
				+ ExtensionDt.class.getCanonicalName());
		}

		@Override
		public void put(IResource theResource, ExtensionDt theObject) {
			theResource.getResourceMetadata().put(this, theObject);
		}
	}


	@SuppressWarnings("unchecked")
	private static <T extends Enum<?>> T getEnumFromMetadataOrNullIfNone(Map<ResourceMetadataKeyEnum<?>, Object> theResourceMetadata, ResourceMetadataKeyEnum<T> theKey, Class<T> theEnumType,
																								IValueSetEnumBinder<T> theBinder) {
		Object retValObj = theResourceMetadata.get(theKey);
		if (retValObj == null) {
			return null;
		} else if (theEnumType.equals(retValObj.getClass())) {
			return (T) retValObj;
		} else if (retValObj instanceof String) {
			return theBinder.fromCodeString((String) retValObj);
		}
		throw new InternalErrorException(Msg.code(1891) + "Found an object of type '" + retValObj.getClass().getCanonicalName() + "' in resource metadata for key " + theKey.name() + " - Expected "
			+ InstantDt.class.getCanonicalName());
	}

	private static IdDt getIdFromMetadataOrNullIfNone(Map<ResourceMetadataKeyEnum<?>, Object> theResourceMetadata) {
		return toId(ResourceMetadataKeyEnum.VERSION_ID, theResourceMetadata.get(ResourceMetadataKeyEnum.VERSION_ID));
	}

	private static List<IdDt> getIdListFromMetadataOrNullIfNone(Map<ResourceMetadataKeyEnum<?>, Object> theResourceMetadata, ResourceMetadataKeyEnum<?> theKey) {
		Object retValObj = theResourceMetadata.get(theKey);
		if (retValObj == null) {
			return null;
		} else if (retValObj instanceof List) {
			List<?> retValList = (List<?>) retValObj;
			for (Object next : retValList) {
				if (!(next instanceof IdDt)) {
					List<IdDt> retVal = new ArrayList<IdDt>();
					for (Object nextVal : retValList) {
						retVal.add(toId(theKey, nextVal));
					}
					return Collections.unmodifiableList(retVal);
				}
			}
			@SuppressWarnings("unchecked")
			List<IdDt> retVal = (List<IdDt>) retValList;
			return Collections.unmodifiableList(retVal);
		} else {
			return Collections.singletonList(toId(theKey, retValObj));
		}
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
			}
			return (InstantDt) retValObj;
		}
		throw new InternalErrorException(Msg.code(1892) + "Found an object of type '" + retValObj.getClass().getCanonicalName() + "' in resource metadata for key " + theKey.name() + " - Expected "
			+ InstantDt.class.getCanonicalName());
	}

	private static String getStringFromMetadataOrNullIfNone(Map<ResourceMetadataKeyEnum<?>, Object> theResourceMetadata, ResourceMetadataKeyEnum<String> theKey) {
		Object retValObj = theResourceMetadata.get(theKey);
		if (retValObj == null) {
			return null;
		} else if (retValObj instanceof String) {
			if (StringUtils.isBlank(((String) retValObj))) {
				return null;
			}
			return (String) retValObj;
		}
		throw new InternalErrorException(Msg.code(1893) + "Found an object of type '" + retValObj.getClass().getCanonicalName() + "' in resource metadata for key " + theKey.name() + " - Expected "
			+ String.class.getCanonicalName());
	}

	private static IdDt toId(ResourceMetadataKeyEnum<?> theKey, Object retValObj) {
		if (retValObj == null) {
			return null;
		} else if (retValObj instanceof String) {
			if (isNotBlank((String) retValObj)) {
				return new IdDt((String) retValObj);
			}
			return null;
		} else if (retValObj instanceof IdDt) {
			if (((IdDt) retValObj).isEmpty()) {
				return null;
			}
			return (IdDt) retValObj;
		} else if (retValObj instanceof Number) {
			return new IdDt(retValObj.toString());
		}
		throw new InternalErrorException(Msg.code(1894) + "Found an object of type '" + retValObj.getClass().getCanonicalName() + "' in resource metadata for key " + theKey.name() + " - Expected "
			+ IdDt.class.getCanonicalName());
	}


}
