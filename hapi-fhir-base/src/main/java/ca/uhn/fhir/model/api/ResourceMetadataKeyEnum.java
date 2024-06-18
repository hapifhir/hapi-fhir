/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.model.api;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.model.base.composite.BaseCodingDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.model.valueset.BundleEntrySearchModeEnum;
import ca.uhn.fhir.model.valueset.BundleEntryTransactionMethodEnum;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

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
	public static final ResourceMetadataKeyEnum<IPrimitiveType<Date>> DELETED_AT =
			new ResourceMetadataKeyEnum<>("DELETED_AT", IPrimitiveType.class) {};
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
	public static final ResourceMetadataKeyEnum<BundleEntrySearchModeEnum> ENTRY_SEARCH_MODE =
			new ResourceMetadataKeyEnum<>("ENTRY_SEARCH_MODE", BundleEntrySearchModeEnum.class) {};
	/**
	 * If present and populated with a decimal value, contains the "bundle entry search score", which is the value of the status field in the Bundle entry containing this resource.
	 * The value for this key corresponds to field <code>Bundle.entry.search.score</code>. This value represents the search ranking score, where 1.0 is relevant and 0.0 is irrelevant.
	 * <p>
	 * Note that status is only used in FHIR DSTU2 and later.
	 * </p>
	 */
	public static final ResourceMetadataKeyEnum<BigDecimal> ENTRY_SEARCH_SCORE =
			new ResourceMetadataKeyEnum<>("ENTRY_SEARCH_SCORE", BigDecimal.class) {};
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
	public static final ResourceMetadataKeyEnum<BundleEntryTransactionMethodEnum> ENTRY_TRANSACTION_METHOD =
			new ResourceMetadataKeyEnum<>("ENTRY_TRANSACTION_OPERATION", BundleEntryTransactionMethodEnum.class) {};
	/**
	 * The value for this key represents a {@link List} of profile IDs that this resource claims to conform to.
	 * <p>
	 * <p>
	 * Values for this key are of type <b>List&lt;IdDt&gt;</b>. Note that the returned list is <i>unmodifiable</i>, so you need to create a new list and call <code>put</code> to change its value.
	 * </p>
	 */
	public static final ResourceMetadataKeyEnum<List<IdDt>> PROFILES =
			new ResourceMetadataKeyEnum<>("PROFILES", List.class) {};
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
	public static final ResourceMetadataKeyEnum<InstantDt> PUBLISHED =
			new ResourceMetadataKeyEnum<>("PUBLISHED", InstantDt.class) {};

	public static final ResourceMetadataKeyEnum<List<BaseCodingDt>> SECURITY_LABELS =
			new ResourceMetadataKeyEnum<>("SECURITY_LABELS", List.class) {};
	/**
	 * The value for this key is the list of tags associated with this resource
	 * <p>
	 * Values for this key are of type <b>{@link TagList}</b>
	 * </p>
	 *
	 * @see TagList
	 */
	public static final ResourceMetadataKeyEnum<TagList> TAG_LIST =
			new ResourceMetadataKeyEnum<>("TAG_LIST", TagList.class) {};
	/**
	 * The value for this key is the bundle entry <b>Updated</b> time. This is defined by FHIR as "Last Updated for resource". This value is also used for populating the "Last-Modified" header in the
	 * case of methods that return a single resource (read, vread, etc.)
	 * <p>
	 * Values for this key are of type <b>{@link InstantDt}</b>
	 * </p>
	 *
	 * @see InstantDt
	 */
	public static final ResourceMetadataKeyEnum<InstantDt> UPDATED =
			new ResourceMetadataKeyEnum<>("UPDATED", InstantDt.class) {};
	/**
	 * The value for this key is the version ID of the resource object.
	 * <p>
	 * Values for this key are of type <b>{@link String}</b>
	 * </p>
	 *
	 * @deprecated The {@link IResource#getId()} resource ID will now be populated with the version ID via the {@link IdDt#getVersionIdPart()} method
	 */
	@Deprecated
	public static final ResourceMetadataKeyEnum<String> VERSION =
			new ResourceMetadataKeyEnum<>("VERSION", String.class) {};
	/**
	 * The value for this key is the version ID of the resource object.
	 * <p>
	 * Values for this key are of type <b>{@link IdDt}</b>
	 * </p>
	 *
	 * @deprecated The {@link IResource#getId()} resource ID will now be populated with the version ID via the {@link IdDt#getVersionIdPart()} method
	 */
	@Deprecated
	public static final ResourceMetadataKeyEnum<IdDt> VERSION_ID =
			new ResourceMetadataKeyEnum<>("VERSION_ID", IdDt.class) {};

	private static final long serialVersionUID = 1L;
	private final String myValue;
	private final Class<?> myType;

	public ResourceMetadataKeyEnum(String theValue, Class<?> theType) {
		myValue = theValue;
		myType = theType;
	}

	// TODO: JA - Replace all of the various other get/put methods in subclasses with just using the two that are here
	public T get(IBaseResource theResource) {
		Object retVal;
		if (theResource instanceof IAnyResource) {
			retVal = theResource.getUserData(name());
		} else {
			retVal = ((IResource) theResource).getResourceMetadata().get(this);
		}

		if (retVal != null && !myType.isAssignableFrom(retVal.getClass())) {
			throw new InternalErrorException(Msg.code(1890) + "Found an object of type '"
					+ retVal.getClass().getCanonicalName()
					+ "' in resource metadata for key " + this.name() + " - Expected "
					+ myType.getCanonicalName());
		}

		//noinspection unchecked
		return (T) retVal;
	}

	public void put(IBaseResource theResource, T theValue) {
		if (theValue != null && !myType.isAssignableFrom(theValue.getClass())) {
			throw new InternalErrorException(Msg.code(1891) + "Can not put object of type '"
					+ theValue.getClass().getCanonicalName()
					+ "' in resource metadata for key " + this.name() + " - Expected "
					+ myType.getCanonicalName());
		}

		if (theResource instanceof IAnyResource) {
			theResource.setUserData(name(), theValue);
		} else {
			((IResource) theResource).getResourceMetadata().put(this, theValue);
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		ResourceMetadataKeyEnum<?> other = (ResourceMetadataKeyEnum<?>) obj;
		if (myValue == null) {
			return other.myValue == null;
		} else return myValue.equals(other.myValue);
	}

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

	public static final class ExtensionResourceMetadataKey extends ResourceMetadataKeyEnum<ExtensionDt> {
		public ExtensionResourceMetadataKey(String theUrl) {
			super(theUrl, ExtensionDt.class);
		}
	}
}
