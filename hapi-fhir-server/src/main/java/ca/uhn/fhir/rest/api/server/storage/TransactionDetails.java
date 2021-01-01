package ca.uhn.fhir.rest.api.server.storage;

/*-
 * #%L
 * HAPI FHIR - Server Framework
 * %%
 * Copyright (C) 2014 - 2021 Smile CDR, Inc.
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

import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.Pointcut;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.Collections;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * This object contains runtime information that is gathered and relevant to a single <i>database transaction</i>.
 * This doesn't mean a FHIR transaction necessarily, but rather any operation that happens within a single DB transaction
 * (i.e. a FHIR create, read, transaction, etc.).
 * <p>
 * The intent with this class is to hold things we want to pass from operation to operation within a transaction in
 * order to avoid looking things up multiple times, etc.
 * </p>
 *
 * @since 5.0.0
 */
public class TransactionDetails {

	private final Date myTransactionDate;
	private Map<IIdType, ResourcePersistentId> myResolvedResourceIds = Collections.emptyMap();
	private Map<String, Object> myUserData;
	private ListMultimap<Pointcut, HookParams> myDeferredInterceptorBroadcasts;
	private EnumSet<Pointcut> myDeferredInterceptorBroadcastPointcuts;

	/**
	 * Constructor
	 */
	public TransactionDetails() {
		this(new Date());
	}

	/**
	 * Constructor
	 */
	public TransactionDetails(Date theTransactionDate) {
		myTransactionDate = theTransactionDate;
	}

	/**
	 * A <b>Resolved Resource ID</b> is a mapping between a resource ID (e.g. "<code>Patient/ABC</code>" or
	 * "<code>Observation/123</code>") and a storage ID for that resource. Resources should only be placed within
	 * the TransactionDetails if they are known to exist and be valid targets for other resources to link to.
	 */
	public Map<IIdType, ResourcePersistentId> getResolvedResourceIds() {
		return myResolvedResourceIds;
	}

	/**
	 * A <b>Resolved Resource ID</b> is a mapping between a resource ID (e.g. "<code>Patient/ABC</code>" or
	 * "<code>Observation/123</code>") and a storage ID for that resource. Resources should only be placed within
	 * the TransactionDetails if they are known to exist and be valid targets for other resources to link to.
	 */
	public void addResolvedResourceId(IIdType theResourceId, ResourcePersistentId thePersistentId) {
		assert theResourceId != null;
		assert thePersistentId != null;

		if (myResolvedResourceIds.isEmpty()) {
			myResolvedResourceIds = new HashMap<>();
		}
		myResolvedResourceIds.put(theResourceId, thePersistentId);
	}

	/**
	 * This is the wall-clock time that a given transaction started.
	 */
	public Date getTransactionDate() {
		return myTransactionDate;
	}

	/**
	 * Sets an arbitraty object that will last the lifetime of the current transaction
	 *
	 * @see #getUserData(String)
	 */
	public void putUserData(String theKey, Object theValue) {
		if (myUserData == null) {
			myUserData = new HashMap<>();
		}
		myUserData.put(theKey, theValue);
	}

	/**
	 * Gets an arbitraty object that will last the lifetime of the current transaction
	 *
	 * @see #putUserData(String, Object)
	 */
	public <T> T getUserData(String theKey) {
		if (myUserData != null) {
			return (T) myUserData.get(theKey);
		}
		return null;
	}

	/**
	 * Fetches the existing value in the user data map, or uses {@literal theSupplier} to create a new object and
	 * puts that in the map, and returns it
	 */
	public <T> T getOrCreateUserData(String theKey, Supplier<T> theSupplier) {
		T retVal = (T) getUserData(theKey);
		if (retVal == null) {
			retVal = theSupplier.get();
			putUserData(theKey, retVal);
		}
		return retVal;
	}

	/**
	 * This can be used by processors for FHIR transactions to defer interceptor broadcasts on sub-requests if needed
	 *
	 * @since 5.2.0
	 */
	public void beginAcceptingDeferredInterceptorBroadcasts(Pointcut... thePointcuts) {
		Validate.isTrue(!isAcceptingDeferredInterceptorBroadcasts());
		myDeferredInterceptorBroadcasts = ArrayListMultimap.create();
		myDeferredInterceptorBroadcastPointcuts = EnumSet.of(thePointcuts[0], thePointcuts);
	}

	/**
	 * This can be used by processors for FHIR transactions to defer interceptor broadcasts on sub-requests if needed
	 *
	 * @since 5.2.0
	 */
	public boolean isAcceptingDeferredInterceptorBroadcasts() {
		return myDeferredInterceptorBroadcasts != null;
	}

	/**
	 * This can be used by processors for FHIR transactions to defer interceptor broadcasts on sub-requests if needed
	 *
	 * @since 5.2.0
	 */
	public boolean isAcceptingDeferredInterceptorBroadcasts(Pointcut thePointcut) {
		return myDeferredInterceptorBroadcasts != null && myDeferredInterceptorBroadcastPointcuts.contains(thePointcut);
	}

	/**
	 * This can be used by processors for FHIR transactions to defer interceptor broadcasts on sub-requests if needed
	 *
	 * @since 5.2.0
	 */
	public ListMultimap<Pointcut, HookParams> endAcceptingDeferredInterceptorBroadcasts() {
		Validate.isTrue(isAcceptingDeferredInterceptorBroadcasts());
		ListMultimap<Pointcut, HookParams> retVal = myDeferredInterceptorBroadcasts;
		myDeferredInterceptorBroadcasts = null;
		myDeferredInterceptorBroadcastPointcuts = null;
		return retVal;
	}

	/**
	 * This can be used by processors for FHIR transactions to defer interceptor broadcasts on sub-requests if needed
	 *
	 * @since 5.2.0
	 */
	public void addDeferredInterceptorBroadcast(Pointcut thePointcut, HookParams theHookParams) {
		Validate.isTrue(isAcceptingDeferredInterceptorBroadcasts(thePointcut));
		myDeferredInterceptorBroadcasts.put(thePointcut, theHookParams);
	}
}

