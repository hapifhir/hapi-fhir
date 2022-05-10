package ca.uhn.fhir.rest.server.provider;

/*-
 * #%L
 * HAPI FHIR - Server Framework
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
import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementCompositeDefinition;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.rest.annotation.ConditionalUrlParam;
import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.annotation.Delete;
import ca.uhn.fhir.rest.annotation.History;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.annotation.Update;
import ca.uhn.fhir.rest.api.InterceptorInvocationTimingEnum;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.server.IPreResourceAccessDetails;
import ca.uhn.fhir.rest.api.server.IPreResourceShowDetails;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SimplePreResourceAccessDetails;
import ca.uhn.fhir.rest.api.server.SimplePreResourceShowDetails;
import ca.uhn.fhir.rest.api.server.storage.TransactionDetails;
import ca.uhn.fhir.rest.param.TokenAndListParam;
import ca.uhn.fhir.rest.param.TokenOrListParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.util.ValidateUtil;
import com.google.common.collect.Lists;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicLong;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * This class is a simple implementation of the resource provider
 * interface that uses a HashMap to store all resources in memory.
 * <p>
 * This class currently supports the following FHIR operations:
 * </p>
 * <ul>
 * <li>Create</li>
 * <li>Update existing resource</li>
 * <li>Update non-existing resource (e.g. create with client-supplied ID)</li>
 * <li>Delete</li>
 * <li>Search by resource type with no parameters</li>
 * </ul>
 *
 * @param <T> The resource type to support
 */
public class HashMapResourceProvider<T extends IBaseResource> implements IResourceProvider {
	private static final Logger ourLog = LoggerFactory.getLogger(HashMapResourceProvider.class);
	private final Class<T> myResourceType;
	private final FhirContext myFhirContext;
	private final String myResourceName;
	protected Map<String, TreeMap<Long, T>> myIdToVersionToResourceMap = new LinkedHashMap<>();
	protected Map<String, LinkedList<T>> myIdToHistory = new LinkedHashMap<>();
	protected LinkedList<T> myTypeHistory = new LinkedList<>();
	protected AtomicLong mySearchCount = new AtomicLong(0);
	private long myNextId;
	private AtomicLong myDeleteCount = new AtomicLong(0);
	private AtomicLong myUpdateCount = new AtomicLong(0);
	private AtomicLong myCreateCount = new AtomicLong(0);
	private AtomicLong myReadCount = new AtomicLong(0);

	/**
	 * Constructor
	 *
	 * @param theFhirContext  The FHIR context
	 * @param theResourceType The resource type to support
	 */
	public HashMapResourceProvider(FhirContext theFhirContext, Class<T> theResourceType) {
		myFhirContext = theFhirContext;
		myResourceType = theResourceType;
		myResourceName = myFhirContext.getResourceType(theResourceType);
		clear();
	}

	/**
	 * Clear all data held in this resource provider
	 */
	public synchronized void clear() {
		myNextId = 1;
		myIdToVersionToResourceMap.clear();
		myIdToHistory.clear();
		myTypeHistory.clear();
	}

	/**
	 * Clear the counts used by {@link #getCountRead()} and other count methods
	 */
	public  synchronized void clearCounts() {
		myReadCount.set(0L);
		myUpdateCount.set(0L);
		myCreateCount.set(0L);
		myDeleteCount.set(0L);
		mySearchCount.set(0L);
	}

	@Create
	public synchronized MethodOutcome create(@ResourceParam T theResource, RequestDetails theRequestDetails) {
		TransactionDetails transactionDetails = new TransactionDetails();

		createInternal(theResource, theRequestDetails, transactionDetails);

		myCreateCount.incrementAndGet();

		return new MethodOutcome()
			.setCreated(true)
			.setResource(theResource)
			.setId(theResource.getIdElement());
	}

	private void createInternal(@ResourceParam T theResource, RequestDetails theRequestDetails, TransactionDetails theTransactionDetails) {
		long idPart = myNextId++;
		String idPartAsString = Long.toString(idPart);
		Long versionIdPart = 1L;

		assert !myIdToVersionToResourceMap.containsKey(idPartAsString);

		IIdType id = store(theResource, idPartAsString, versionIdPart, theRequestDetails, theTransactionDetails);
		theResource.setId(id);
	}

	@Delete
	public  synchronized MethodOutcome delete(@IdParam IIdType theId, RequestDetails theRequestDetails) {
		TransactionDetails transactionDetails = new TransactionDetails();

		TreeMap<Long, T> versions = myIdToVersionToResourceMap.get(theId.getIdPart());
		if (versions == null || versions.isEmpty()) {
			throw new ResourceNotFoundException(Msg.code(1979) + theId);
		}


		long nextVersion = versions.lastEntry().getKey() + 1L;
		IIdType id = store(null, theId.getIdPart(), nextVersion, theRequestDetails, transactionDetails);

		myDeleteCount.incrementAndGet();

		return new MethodOutcome()
			.setId(id);
	}

	/**
	 * This method returns a simple operation count. This is mostly
	 * useful for testing purposes.
	 */
	public  synchronized long getCountCreate() {
		return myCreateCount.get();
	}

	/**
	 * This method returns a simple operation count. This is mostly
	 * useful for testing purposes.
	 */
	public synchronized  long getCountDelete() {
		return myDeleteCount.get();
	}

	/**
	 * This method returns a simple operation count. This is mostly
	 * useful for testing purposes.
	 */
	public  synchronized long getCountRead() {
		return myReadCount.get();
	}

	/**
	 * This method returns a simple operation count. This is mostly
	 * useful for testing purposes.
	 */
	public synchronized  long getCountSearch() {
		return mySearchCount.get();
	}

	/**
	 * This method returns a simple operation count. This is mostly
	 * useful for testing purposes.
	 */
	public  synchronized long getCountUpdate() {
		return myUpdateCount.get();
	}

	@Override
	public Class<T> getResourceType() {
		return myResourceType;
	}

	private TreeMap<Long, T> getVersionToResource(String theIdPart) {
		myIdToVersionToResourceMap.computeIfAbsent(theIdPart, t -> new TreeMap<>());
		return myIdToVersionToResourceMap.get(theIdPart);
	}

	@History
	public  synchronized List<IBaseResource> historyInstance(@IdParam IIdType theId, RequestDetails theRequestDetails) {
		LinkedList<T> retVal = myIdToHistory.get(theId.getIdPart());
		if (retVal == null) {
			throw new ResourceNotFoundException(Msg.code(1980) + theId);
		}

		return fireInterceptorsAndFilterAsNeeded(retVal, theRequestDetails);
	}

	@History
	public List<T> historyType() {
		return myTypeHistory;
	}

	@Read(version = true)
	public  synchronized T read(@IdParam IIdType theId, RequestDetails theRequestDetails) {
		TreeMap<Long, T> versions = myIdToVersionToResourceMap.get(theId.getIdPart());
		if (versions == null || versions.isEmpty()) {
			throw new ResourceNotFoundException(Msg.code(1981) + theId);
		}

		T retVal;
		if (theId.hasVersionIdPart()) {
			Long versionId = theId.getVersionIdPartAsLong();
			if (!versions.containsKey(versionId)) {
				throw new ResourceNotFoundException(Msg.code(1982) + theId);
			} else {
				T resource = versions.get(versionId);
				if (resource == null) {
					throw new ResourceGoneException(Msg.code(1983) + theId);
				}
				retVal = resource;
			}

		} else {
			retVal = versions.lastEntry().getValue();
		}

		myReadCount.incrementAndGet();

		retVal = fireInterceptorsAndFilterAsNeeded(retVal, theRequestDetails);
		if (retVal == null) {
			throw new ResourceNotFoundException(Msg.code(1984) + theId);
		}
		return retVal;
	}

	@Search
	public  synchronized List<IBaseResource> searchAll(RequestDetails theRequestDetails) {
		mySearchCount.incrementAndGet();
		List<T> retVal = getAllResources();
		return fireInterceptorsAndFilterAsNeeded(retVal, theRequestDetails);
	}

	@Nonnull
	protected  synchronized List<T> getAllResources() {
		List<T> retVal = new ArrayList<>();

		for (TreeMap<Long, T> next : myIdToVersionToResourceMap.values()) {
			if (next.isEmpty() == false) {
				T nextResource = next.lastEntry().getValue();
				if (nextResource != null) {
					retVal.add(nextResource);
				}
			}
		}

		return retVal;
	}

	@Search
	public  synchronized List<IBaseResource> searchById(
		@RequiredParam(name = "_id") TokenAndListParam theIds, RequestDetails theRequestDetails) {

		List<T> retVal = new ArrayList<>();

		for (TreeMap<Long, T> next : myIdToVersionToResourceMap.values()) {
			if (next.isEmpty() == false) {
				T nextResource = next.lastEntry().getValue();

				boolean matches = true;
				if (theIds != null && theIds.getValuesAsQueryTokens().size() > 0) {
					for (TokenOrListParam nextIdAnd : theIds.getValuesAsQueryTokens()) {
						matches = false;
						for (TokenParam nextOr : nextIdAnd.getValuesAsQueryTokens()) {
							if (nextOr.getValue().equals(nextResource.getIdElement().getIdPart())) {
								matches = true;
							}
						}
						if (!matches) {
							break;
						}
					}
				}

				if (!matches) {
					continue;
				}

				retVal.add(nextResource);
			}
		}

		mySearchCount.incrementAndGet();

		return fireInterceptorsAndFilterAsNeeded(retVal, theRequestDetails);
	}

	private IIdType store(@ResourceParam T theResource, String theIdPart, Long theVersionIdPart, RequestDetails theRequestDetails, TransactionDetails theTransactionDetails) {
		IIdType id = myFhirContext.getVersion().newIdType();
		String versionIdPart = Long.toString(theVersionIdPart);
		id.setParts(null, myResourceName, theIdPart, versionIdPart);
		if (theResource != null) {
			theResource.setId(id);
		}

		/*
		 * This is a bit of magic to make sure that the versionId attribute
		 * in the resource being stored accurately represents the version
		 * that was assigned by this provider
		 */
		if (theResource != null) {
			if (myFhirContext.getVersion().getVersion() == FhirVersionEnum.DSTU2) {
				ResourceMetadataKeyEnum.VERSION.put((IResource) theResource, versionIdPart);
			} else {
				BaseRuntimeChildDefinition metaChild = myFhirContext.getResourceDefinition(myResourceType).getChildByName("meta");
				List<IBase> metaValues = metaChild.getAccessor().getValues(theResource);
				if (metaValues.size() > 0) {
					IBase meta = metaValues.get(0);
					BaseRuntimeElementCompositeDefinition<?> metaDef = (BaseRuntimeElementCompositeDefinition<?>) myFhirContext.getElementDefinition(meta.getClass());
					BaseRuntimeChildDefinition versionIdDef = metaDef.getChildByName("versionId");
					List<IBase> versionIdValues = versionIdDef.getAccessor().getValues(meta);
					if (versionIdValues.size() > 0) {
						IPrimitiveType<?> versionId = (IPrimitiveType<?>) versionIdValues.get(0);
						versionId.setValueAsString(versionIdPart);
					}
				}
			}
		}

		ourLog.info("Storing resource with ID: {}", id.getValue());

		// Store to ID->version->resource map
		TreeMap<Long, T> versionToResource = getVersionToResource(theIdPart);
		versionToResource.put(theVersionIdPart, theResource);

		if (theRequestDetails != null) {
			IInterceptorBroadcaster interceptorBroadcaster = theRequestDetails.getInterceptorBroadcaster();

			if (theResource != null) {
				if (!myIdToHistory.containsKey(theIdPart)) {

					// Interceptor call: STORAGE_PRESTORAGE_RESOURCE_CREATED
					HookParams preStorageParams = new HookParams()
						.add(RequestDetails.class, theRequestDetails)
						.addIfMatchesType(ServletRequestDetails.class, theRequestDetails)
						.add(IBaseResource.class, theResource)
						.add(TransactionDetails.class, theTransactionDetails);
					interceptorBroadcaster.callHooks(Pointcut.STORAGE_PRESTORAGE_RESOURCE_CREATED, preStorageParams);

					// Interceptor call: STORAGE_PRECOMMIT_RESOURCE_CREATED
					HookParams preCommitParams = new HookParams()
						.add(RequestDetails.class, theRequestDetails)
						.addIfMatchesType(ServletRequestDetails.class, theRequestDetails)
						.add(IBaseResource.class, theResource)
						.add(TransactionDetails.class, theTransactionDetails)
						.add(InterceptorInvocationTimingEnum.class, theTransactionDetails.getInvocationTiming(Pointcut.STORAGE_PRECOMMIT_RESOURCE_CREATED));
					interceptorBroadcaster.callHooks(Pointcut.STORAGE_PRECOMMIT_RESOURCE_CREATED, preCommitParams);

				} else {

					// Interceptor call: STORAGE_PRESTORAGE_RESOURCE_UPDATED
					HookParams preStorageParams = new HookParams()
						.add(RequestDetails.class, theRequestDetails)
						.addIfMatchesType(ServletRequestDetails.class, theRequestDetails)
						.add(IBaseResource.class, myIdToHistory.get(theIdPart).getFirst())
						.add(IBaseResource.class, theResource)
						.add(TransactionDetails.class, theTransactionDetails);
					interceptorBroadcaster.callHooks(Pointcut.STORAGE_PRESTORAGE_RESOURCE_UPDATED, preStorageParams);

					// Interceptor call: STORAGE_PRECOMMIT_RESOURCE_UPDATED
					HookParams preCommitParams = new HookParams()
						.add(RequestDetails.class, theRequestDetails)
						.addIfMatchesType(ServletRequestDetails.class, theRequestDetails)
						.add(IBaseResource.class, myIdToHistory.get(theIdPart).getFirst())
						.add(IBaseResource.class, theResource)
						.add(TransactionDetails.class, theTransactionDetails)
						.add(InterceptorInvocationTimingEnum.class, theTransactionDetails.getInvocationTiming(Pointcut.STORAGE_PRECOMMIT_RESOURCE_CREATED));
					interceptorBroadcaster.callHooks(Pointcut.STORAGE_PRECOMMIT_RESOURCE_UPDATED, preCommitParams);

				}
			}
		}

		// Store to type history map
		myTypeHistory.addFirst(theResource);

		// Store to ID history map
		myIdToHistory.computeIfAbsent(theIdPart, t -> new LinkedList<>());
		myIdToHistory.get(theIdPart).addFirst(theResource);

		// Return the newly assigned ID including the version ID
		return id;
	}

	/**
	 * @param theConditional This is provided only so that subclasses can implement if they want
	 */
	@Update
	public  synchronized MethodOutcome update(
		@ResourceParam T theResource,
		@ConditionalUrlParam String theConditional,
		RequestDetails theRequestDetails) {
		TransactionDetails transactionDetails = new TransactionDetails();

		ValidateUtil.isTrueOrThrowInvalidRequest(isBlank(theConditional), "This server doesn't support conditional update");

		boolean created = updateInternal(theResource, theRequestDetails, transactionDetails);
		myUpdateCount.incrementAndGet();

		return new MethodOutcome()
			.setCreated(created)
			.setResource(theResource)
			.setId(theResource.getIdElement());
	}

	private boolean updateInternal(@ResourceParam T theResource, RequestDetails theRequestDetails, TransactionDetails theTransactionDetails) {
		String idPartAsString = theResource.getIdElement().getIdPart();
		TreeMap<Long, T> versionToResource = getVersionToResource(idPartAsString);

		Long versionIdPart;
		boolean created;
		if (versionToResource.isEmpty()) {
			versionIdPart = 1L;
			created = true;
		} else {
			versionIdPart = versionToResource.lastKey() + 1L;
			created = false;
		}

		IIdType id = store(theResource, idPartAsString, versionIdPart, theRequestDetails, theTransactionDetails);
		theResource.setId(id);
		return created;
	}

	public FhirContext getFhirContext() {
		return myFhirContext;
	}

	/**
	 * This is a utility method that can be used to store a resource without
	 * having to use the outside API. In this case, the storage happens without
	 * any interaction with interceptors, etc.
	 *
	 * @param theResource The resource to store. If the resource has an ID, that ID is updated.
	 * @return Return the ID assigned to the stored resource
	 */
	public  synchronized IIdType store(T theResource) {
		if (theResource.getIdElement().hasIdPart()) {
			updateInternal(theResource, null, new TransactionDetails());
		} else {
			createInternal(theResource, null, new TransactionDetails());
		}
		return theResource.getIdElement();
	}

	/**
	 * Returns an unmodifiable list containing the current version of all resources stored in this provider
	 *
	 * @since 4.1.0
	 */
	public  synchronized List<T> getStoredResources() {
		List<T> retVal = new ArrayList<>();
		for (TreeMap<Long, T> next : myIdToVersionToResourceMap.values()) {
			retVal.add(next.lastEntry().getValue());
		}
		return Collections.unmodifiableList(retVal);
	}

	private static <T extends IBaseResource> T fireInterceptorsAndFilterAsNeeded(T theResource, RequestDetails theRequestDetails) {
		List<IBaseResource> output = fireInterceptorsAndFilterAsNeeded(Lists.newArrayList(theResource), theRequestDetails);
		if (output.size() == 1) {
			return theResource;
		} else {
			return null;
		}
	}

	protected static <T extends IBaseResource> List<IBaseResource> fireInterceptorsAndFilterAsNeeded(List<T> theResources, RequestDetails theRequestDetails) {
		List<IBaseResource> resourcesToReturn = new ArrayList<>(theResources);

		if (theRequestDetails != null) {
			IInterceptorBroadcaster interceptorBroadcaster = theRequestDetails.getInterceptorBroadcaster();

			// Call the STORAGE_PREACCESS_RESOURCES pointcut (used for consent/auth interceptors)
			SimplePreResourceAccessDetails preResourceAccessDetails = new SimplePreResourceAccessDetails(resourcesToReturn);
			HookParams params = new HookParams()
				.add(RequestDetails.class, theRequestDetails)
				.addIfMatchesType(ServletRequestDetails.class, theRequestDetails)
				.add(IPreResourceAccessDetails.class, preResourceAccessDetails);
			interceptorBroadcaster.callHooks(Pointcut.STORAGE_PREACCESS_RESOURCES, params);
			preResourceAccessDetails.applyFilterToList();

			// Call the STORAGE_PREACCESS_RESOURCES pointcut (used for consent/auth interceptors)
			SimplePreResourceShowDetails preResourceShowDetails = new SimplePreResourceShowDetails(resourcesToReturn);
			HookParams preShowParams = new HookParams()
				.add(RequestDetails.class, theRequestDetails)
				.addIfMatchesType(ServletRequestDetails.class, theRequestDetails)
				.add(IPreResourceShowDetails.class, preResourceShowDetails);
			interceptorBroadcaster.callHooks(Pointcut.STORAGE_PRESHOW_RESOURCES, preShowParams);
			resourcesToReturn = preResourceShowDetails.toList();

		}

		return resourcesToReturn;
	}
}
