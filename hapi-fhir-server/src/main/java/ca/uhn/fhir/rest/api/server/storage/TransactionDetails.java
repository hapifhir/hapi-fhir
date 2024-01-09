/*-
 * #%L
 * HAPI FHIR - Server Framework
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
package ca.uhn.fhir.rest.api.server.storage;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.rest.api.InterceptorInvocationTimingEnum;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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

	public static final IResourcePersistentId NOT_FOUND = IResourcePersistentId.NOT_FOUND;

	private final Date myTransactionDate;
	private List<Runnable> myRollbackUndoActions = Collections.emptyList();
	private Map<String, IResourcePersistentId> myResolvedResourceIds = Collections.emptyMap();
	private Map<String, IResourcePersistentId> myResolvedMatchUrls = Collections.emptyMap();
	private Map<String, Supplier<IBaseResource>> myResolvedResources = Collections.emptyMap();
	private Set<IResourcePersistentId> myDeletedResourceIds = Collections.emptySet();
	private Map<String, Object> myUserData;
	private ListMultimap<Pointcut, HookParams> myDeferredInterceptorBroadcasts;
	private EnumSet<Pointcut> myDeferredInterceptorBroadcastPointcuts;
	private boolean myFhirTransaction;

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
	 * Get the actions that should be executed if the transaction is rolled back
	 *
	 * @since 5.5.0
	 */
	public List<Runnable> getRollbackUndoActions() {
		return Collections.unmodifiableList(myRollbackUndoActions);
	}

	/**
	 * Add an action that should be executed if the transaction is rolled back
	 *
	 * @since 5.5.0
	 */
	public void addRollbackUndoAction(@Nonnull Runnable theRunnable) {
		assert theRunnable != null;
		if (myRollbackUndoActions.isEmpty()) {
			myRollbackUndoActions = new ArrayList<>();
		}
		myRollbackUndoActions.add(theRunnable);
	}

	/**
	 * Clears any previously added rollback actions
	 *
	 * @since 5.5.0
	 */
	public void clearRollbackUndoActions() {
		if (!myRollbackUndoActions.isEmpty()) {
			myRollbackUndoActions.clear();
		}
	}

	/**
	 * @since 6.8.0
	 */
	public void addDeletedResourceId(@Nonnull IResourcePersistentId theResourceId) {
		Validate.notNull(theResourceId, "theResourceId must not be null");
		if (myDeletedResourceIds.isEmpty()) {
			myDeletedResourceIds = new HashSet<>();
		}
		myDeletedResourceIds.add(theResourceId);
	}

	/**
	 * @since 6.8.0
	 */
	public void addDeletedResourceIds(Collection<? extends IResourcePersistentId> theResourceIds) {
		for (IResourcePersistentId<?> next : theResourceIds) {
			addDeletedResourceId(next);
		}
	}

	/**
	 * @since 6.8.0
	 */
	@Nonnull
	public Set<IResourcePersistentId> getDeletedResourceIds() {
		return Collections.unmodifiableSet(myDeletedResourceIds);
	}

	/**
	 * A <b>Resolved Resource ID</b> is a mapping between a resource ID (e.g. "<code>Patient/ABC</code>" or
	 * "<code>Observation/123</code>") and a storage ID for that resource. Resources should only be placed within
	 * the TransactionDetails if they are known to exist and be valid targets for other resources to link to.
	 */
	@Nullable
	public IResourcePersistentId getResolvedResourceId(IIdType theId) {
		String idValue = theId.toUnqualifiedVersionless().getValue();
		return myResolvedResourceIds.get(idValue);
	}

	/**
	 * A <b>Resolved Resource ID</b> is a mapping between a resource ID (e.g. "<code>Patient/ABC</code>" or
	 * "<code>Observation/123</code>") and the actual persisted/resolved resource with this ID.
	 */
	@Nullable
	public IBaseResource getResolvedResource(IIdType theId) {
		String idValue = theId.toUnqualifiedVersionless().getValue();
		IBaseResource retVal = null;
		Supplier<IBaseResource> supplier = myResolvedResources.get(idValue);
		if (supplier != null) {
			retVal = supplier.get();
		}
		return retVal;
	}

	/**
	 * Was the given resource ID resolved previously in this transaction as not existing
	 */
	public boolean isResolvedResourceIdEmpty(IIdType theId) {
		if (myResolvedResourceIds != null) {
			if (myResolvedResourceIds.containsKey(theId.toVersionless().getValue())) {
				return myResolvedResourceIds.get(theId.toVersionless().getValue()) == null;
			}
		}
		return false;
	}

	/**
	 * Was the given resource ID resolved previously in this transaction
	 */
	public boolean hasResolvedResourceId(IIdType theId) {
		if (myResolvedResourceIds != null) {
			return myResolvedResourceIds.containsKey(theId.toVersionless().getValue());
		}
		return false;
	}

	/**
	 * A <b>Resolved Resource ID</b> is a mapping between a resource ID (e.g. "<code>Patient/ABC</code>" or
	 * "<code>Observation/123</code>") and a storage ID for that resource. Resources should only be placed within
	 * the TransactionDetails if they are known to exist and be valid targets for other resources to link to.
	 */
	public void addResolvedResourceId(IIdType theResourceId, @Nullable IResourcePersistentId thePersistentId) {
		assert theResourceId != null;

		if (myResolvedResourceIds.isEmpty()) {
			myResolvedResourceIds = new HashMap<>();
		}
		myResolvedResourceIds.put(theResourceId.toVersionless().getValue(), thePersistentId);
	}

	/**
	 * A <b>Resolved Resource ID</b> is a mapping between a resource ID (e.g. "<code>Patient/ABC</code>" or
	 * "<code>Observation/123</code>") and the actual persisted/resolved resource.
	 * This version takes a {@link Supplier} which will only be fetched if the
	 * resource is actually needed. This is good in cases where the resource is
	 * lazy loaded.
	 */
	public void addResolvedResource(IIdType theResourceId, @Nonnull Supplier<IBaseResource> theResource) {
		assert theResourceId != null;

		if (myResolvedResources.isEmpty()) {
			myResolvedResources = new HashMap<>();
		}
		myResolvedResources.put(theResourceId.toVersionless().getValue(), theResource);
	}

	/**
	 * A <b>Resolved Resource ID</b> is a mapping between a resource ID (e.g. "<code>Patient/ABC</code>" or
	 * "<code>Observation/123</code>") and the actual persisted/resolved resource.
	 */
	public void addResolvedResource(IIdType theResourceId, @Nonnull IBaseResource theResource) {
		addResolvedResource(theResourceId, () -> theResource);
	}

	public Map<String, IResourcePersistentId> getResolvedMatchUrls() {
		return myResolvedMatchUrls;
	}

	/**
	 * A <b>Resolved Conditional URL</b> is a mapping between a conditional URL (e.g. "<code>Patient?identifier=foo|bar</code>" or
	 * "<code>Observation/123</code>") and a storage ID for that resource. Resources should only be placed within
	 * the TransactionDetails if they are known to exist and be valid targets for other resources to link to.
	 */
	public void addResolvedMatchUrl(
			FhirContext theFhirContext, String theConditionalUrl, @Nonnull IResourcePersistentId thePersistentId) {
		Validate.notBlank(theConditionalUrl);
		Validate.notNull(thePersistentId);

		if (myResolvedMatchUrls.isEmpty()) {
			myResolvedMatchUrls = new HashMap<>();
		} else if (matchUrlWithDiffIdExists(theConditionalUrl, thePersistentId)) {
			String msg = theFhirContext
					.getLocalizer()
					.getMessage(TransactionDetails.class, "invalidMatchUrlMultipleMatches", theConditionalUrl);
			throw new PreconditionFailedException(Msg.code(2207) + msg);
		}
		myResolvedMatchUrls.put(theConditionalUrl, thePersistentId);
	}

	/**
	 * @since 6.8.0
	 * @see #addResolvedMatchUrl(FhirContext, String, IResourcePersistentId)
	 */
	public void removeResolvedMatchUrl(String theMatchUrl) {
		myResolvedMatchUrls.remove(theMatchUrl);
	}

	private boolean matchUrlWithDiffIdExists(String theConditionalUrl, @Nonnull IResourcePersistentId thePersistentId) {
		if (myResolvedMatchUrls.containsKey(theConditionalUrl)
				&& myResolvedMatchUrls.get(theConditionalUrl) != NOT_FOUND) {
			return !myResolvedMatchUrls.get(theConditionalUrl).getId().equals(thePersistentId.getId());
		}
		return false;
	}

	/**
	 * This is the wall-clock time that a given transaction started.
	 */
	public Date getTransactionDate() {
		return myTransactionDate;
	}

	/**
	 * Remove an item previously stored in user data
	 *
	 * @see #getUserData(String)
	 */
	public void clearUserData(String theKey) {
		if (myUserData != null) {
			myUserData.remove(theKey);
		}
	}

	/**
	 * Sets an arbitrary object that will last the lifetime of the current transaction
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
	 * Gets an arbitrary object that will last the lifetime of the current transaction
	 *
	 * @see #putUserData(String, Object)
	 */
	@SuppressWarnings("unchecked")
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
	@SuppressWarnings("unchecked")
	public <T> T getOrCreateUserData(String theKey, Supplier<T> theSupplier) {
		T retVal = getUserData(theKey);
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

	public InterceptorInvocationTimingEnum getInvocationTiming(Pointcut thePointcut) {
		if (myDeferredInterceptorBroadcasts == null) {
			return InterceptorInvocationTimingEnum.ACTIVE;
		}
		List<HookParams> hookParams = myDeferredInterceptorBroadcasts.get(thePointcut);
		return hookParams == null ? InterceptorInvocationTimingEnum.ACTIVE : InterceptorInvocationTimingEnum.DEFERRED;
	}

	public void deferredBroadcastProcessingFinished() {}

	public void clearResolvedItems() {
		myResolvedResourceIds.clear();
		myResolvedMatchUrls.clear();
	}

	public boolean hasResolvedResourceIds() {
		return !myResolvedResourceIds.isEmpty();
	}

	public boolean isFhirTransaction() {
		return myFhirTransaction;
	}

	public void setFhirTransaction(boolean theFhirTransaction) {
		myFhirTransaction = theFhirTransaction;
	}
}
