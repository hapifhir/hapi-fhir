/*-
 * #%L
 * HAPI FHIR Storage api
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.interceptor.model.ReadPartitionIdRequestDetails;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.interceptor.model.TransactionWriteOperationsDetails;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.dao.IJpaDao;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.api.model.DeleteConflict;
import ca.uhn.fhir.jpa.api.model.DeleteConflictList;
import ca.uhn.fhir.jpa.api.model.DeleteMethodOutcome;
import ca.uhn.fhir.jpa.api.model.LazyDaoMethodOutcome;
import ca.uhn.fhir.jpa.cache.IResourceVersionSvc;
import ca.uhn.fhir.jpa.cache.ResourcePersistentIdMap;
import ca.uhn.fhir.jpa.dao.tx.HapiTransactionService;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.jpa.delete.DeleteConflictUtil;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.cross.IBasePersistedResource;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.model.entity.StorageSettings;
import ca.uhn.fhir.jpa.model.search.StorageProcessingMessage;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.jpa.searchparam.extractor.ResourceIndexedSearchParams;
import ca.uhn.fhir.jpa.searchparam.matcher.InMemoryMatchResult;
import ca.uhn.fhir.jpa.searchparam.matcher.InMemoryResourceMatcher;
import ca.uhn.fhir.jpa.searchparam.matcher.SearchParamMatcher;
import ca.uhn.fhir.jpa.util.TransactionSemanticsHeader;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.valueset.BundleEntryTransactionMethodEnum;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.PatchTypeEnum;
import ca.uhn.fhir.rest.api.PreferReturnEnum;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.api.server.storage.DeferredInterceptorBroadcasts;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import ca.uhn.fhir.rest.api.server.storage.TransactionDetails;
import ca.uhn.fhir.rest.param.ParameterUtil;
import ca.uhn.fhir.rest.server.RestfulServerUtils;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.MethodNotAllowedException;
import ca.uhn.fhir.rest.server.exceptions.NotModifiedException;
import ca.uhn.fhir.rest.server.exceptions.PayloadTooLargeException;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import ca.uhn.fhir.rest.server.method.BaseMethodBinding;
import ca.uhn.fhir.rest.server.method.BaseResourceReturningMethodBinding;
import ca.uhn.fhir.rest.server.method.UpdateMethodBinding;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.rest.server.servlet.ServletSubRequestDetails;
import ca.uhn.fhir.rest.server.util.CompositeInterceptorBroadcaster;
import ca.uhn.fhir.rest.server.util.ServletRequestUtil;
import ca.uhn.fhir.util.AsyncUtil;
import ca.uhn.fhir.util.BundleUtil;
import ca.uhn.fhir.util.ElementUtil;
import ca.uhn.fhir.util.FhirTerser;
import ca.uhn.fhir.util.ResourceReferenceInfo;
import ca.uhn.fhir.util.StopWatch;
import ca.uhn.fhir.util.UrlUtil;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.ThreadUtils;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseBinary;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseExtension;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.IdType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static ca.uhn.fhir.util.HapiExtensions.EXTENSION_TRANSACTION_ENTRY_PARTITION_IDS;
import static ca.uhn.fhir.util.StringUtil.toUtf8String;
import static java.util.Objects.isNull;
import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;
import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public abstract class BaseTransactionProcessor {

	public static final String URN_PREFIX = "urn:";
	public static final String URN_PREFIX_ESCAPED = UrlUtil.escapeUrlParam(URN_PREFIX);
	public static final Pattern UNQUALIFIED_MATCH_URL_START = Pattern.compile("^[a-zA-Z0-9_-]+=");
	public static final Pattern INVALID_PLACEHOLDER_PATTERN = Pattern.compile("[a-zA-Z]+:.*");

	private static final Logger ourLog = LoggerFactory.getLogger(BaseTransactionProcessor.class);
	private static final String POST = "POST";
	private static final String PUT = "PUT";
	private static final String DELETE = "DELETE";
	private static final String PATCH = "PATCH";

	@Autowired
	private IRequestPartitionHelperSvc myRequestPartitionHelperService;

	@Autowired
	private PlatformTransactionManager myTxManager;

	@Autowired
	private FhirContext myContext;

	@Autowired
	@SuppressWarnings("rawtypes")
	private ITransactionProcessorVersionAdapter myVersionAdapter;

	@Autowired
	private DaoRegistry myDaoRegistry;

	@Autowired
	private IInterceptorBroadcaster myInterceptorBroadcaster;

	@Autowired
	private IHapiTransactionService myHapiTransactionService;

	@Autowired
	private StorageSettings myStorageSettings;

	@Autowired
	PartitionSettings myPartitionSettings;

	@Autowired
	private InMemoryResourceMatcher myInMemoryResourceMatcher;

	@Autowired
	private SearchParamMatcher mySearchParamMatcher;

	@Autowired
	private ThreadPoolFactory myThreadPoolFactory;

	private TaskExecutor myExecutor;

	@Autowired
	private IResourceVersionSvc myResourceVersionSvc;

	@VisibleForTesting
	public void setStorageSettings(StorageSettings theStorageSettings) {
		myStorageSettings = theStorageSettings;
	}

	public ITransactionProcessorVersionAdapter getVersionAdapter() {
		return myVersionAdapter;
	}

	@VisibleForTesting
	public void setVersionAdapter(ITransactionProcessorVersionAdapter theVersionAdapter) {
		myVersionAdapter = theVersionAdapter;
	}

	private TaskExecutor getTaskExecutor() {
		if (myExecutor == null) {
			myExecutor = myThreadPoolFactory.newThreadPool(
					myStorageSettings.getBundleBatchPoolSize(),
					myStorageSettings.getBundleBatchMaxPoolSize(),
					"bundle-batch-");
		}
		return myExecutor;
	}

	public <BUNDLE extends IBaseBundle> BUNDLE transaction(
			RequestDetails theRequestDetails, BUNDLE theRequest, boolean theNestedMode) {
		String actionName = "Transaction";
		IBaseBundle response = processTransactionAsSubRequest(theRequestDetails, theRequest, actionName, theNestedMode);

		List<IBase> entries = myVersionAdapter.getEntries(response);
		for (int i = 0; i < entries.size(); i++) {
			if (ElementUtil.isEmpty(entries.get(i))) {
				entries.remove(i);
				i--;
			}
		}

		return (BUNDLE) response;
	}

	public IBaseBundle collection(final RequestDetails theRequestDetails, IBaseBundle theRequest) {
		String transactionType = myVersionAdapter.getBundleType(theRequest);

		if (!org.hl7.fhir.r4.model.Bundle.BundleType.COLLECTION.toCode().equals(transactionType)) {
			throw new InvalidRequestException(
					Msg.code(526) + "Can not process collection Bundle of type: " + transactionType);
		}

		ourLog.info(
				"Beginning storing collection with {} resources",
				myVersionAdapter.getEntries(theRequest).size());

		TransactionTemplate txTemplate = new TransactionTemplate(myTxManager);
		txTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);

		IBaseBundle resp =
				myVersionAdapter.createBundle(org.hl7.fhir.r4.model.Bundle.BundleType.BATCHRESPONSE.toCode());

		List<IBaseResource> resources = new ArrayList<>();
		for (final Object nextRequestEntry : myVersionAdapter.getEntries(theRequest)) {
			IBaseResource resource = myVersionAdapter.getResource((IBase) nextRequestEntry);
			resources.add(resource);
		}

		IBaseBundle transactionBundle = myVersionAdapter.createBundle("transaction");
		for (IBaseResource next : resources) {
			IBase entry = myVersionAdapter.addEntry(transactionBundle);
			myVersionAdapter.setResource(entry, next);
			myVersionAdapter.setRequestVerb(entry, "PUT");
			myVersionAdapter.setRequestUrl(
					entry, next.getIdElement().toUnqualifiedVersionless().getValue());
		}

		transaction(theRequestDetails, transactionBundle, false);

		return resp;
	}

	@SuppressWarnings("unchecked")
	private void populateEntryWithOperationOutcome(BaseServerResponseException caughtEx, IBase nextEntry) {
		myVersionAdapter.populateEntryWithOperationOutcome(caughtEx, nextEntry);
	}

	@SuppressWarnings("unchecked")
	private void handleTransactionCreateOrUpdateOutcome(
			IdSubstitutionMap theIdSubstitutions,
			Map<IIdType, DaoMethodOutcome> theIdToPersistedOutcome,
			IIdType theNextResourceId,
			DaoMethodOutcome theOutcome,
			IBase theNewEntry,
			String theResourceType,
			IBaseResource theRes,
			RequestDetails theRequestDetails) {
		IIdType newId = theOutcome.getId().toUnqualified();
		IIdType resourceId =
				isPlaceholder(theNextResourceId) ? theNextResourceId : theNextResourceId.toUnqualifiedVersionless();
		if (!newId.equals(resourceId)) {
			if (!theNextResourceId.isEmpty()) {
				theIdSubstitutions.put(resourceId, newId);
			}
			if (isPlaceholder(resourceId)) {
				/*
				 * The correct way for substitution IDs to be is to be with no resource type, but we'll accept the qualified kind too just to be lenient.
				 */
				IIdType id = myContext.getVersion().newIdType();
				id.setValue(theResourceType + '/' + resourceId.getValue());
				theIdSubstitutions.put(id, newId);
			}
		}

		populateIdToPersistedOutcomeMap(theIdToPersistedOutcome, newId, theOutcome);

		if (shouldSwapBinaryToActualResource(theRes, theResourceType, theNextResourceId)) {
			theRes = theIdToPersistedOutcome.get(newId).getResource();
		}

		if (theOutcome.getCreated()) {
			myVersionAdapter.setResponseStatus(theNewEntry, toStatusString(Constants.STATUS_HTTP_201_CREATED));
		} else {
			myVersionAdapter.setResponseStatus(theNewEntry, toStatusString(Constants.STATUS_HTTP_200_OK));
		}

		Date lastModified = getLastModified(theRes);
		myVersionAdapter.setResponseLastModified(theNewEntry, lastModified);

		if (theOutcome.getOperationOutcome() != null) {
			myVersionAdapter.setResponseOutcome(theNewEntry, theOutcome.getOperationOutcome());
		}

		if (theRequestDetails != null) {
			String prefer = theRequestDetails.getHeader(Constants.HEADER_PREFER);
			PreferReturnEnum preferReturn =
					RestfulServerUtils.parsePreferHeader(null, prefer).getReturn();
			if (preferReturn != null) {
				if (preferReturn == PreferReturnEnum.REPRESENTATION) {
					if (theOutcome.getResource() != null) {
						theOutcome.fireResourceViewCallbacks();
						myVersionAdapter.setResource(theNewEntry, theOutcome.getResource());
					}
				}
			}
		}
	}

	/**
	 * Method which populates entry in idToPersistedOutcome.
	 * Will store whatever outcome is sent, unless the key already exists, then we only replace an instance if we find that the instance
	 * we are replacing with is non-lazy. This allows us to evaluate later more easily, as we _know_ we need access to these.
	 */
	private void populateIdToPersistedOutcomeMap(
			Map<IIdType, DaoMethodOutcome> idToPersistedOutcome, IIdType newId, DaoMethodOutcome outcome) {
		// Prefer real method outcomes over lazy ones.
		if (idToPersistedOutcome.containsKey(newId)) {
			if (!(outcome instanceof LazyDaoMethodOutcome)) {
				idToPersistedOutcome.put(newId, outcome);
			}
		} else {
			idToPersistedOutcome.put(newId, outcome);
		}
	}

	private Date getLastModified(IBaseResource theRes) {
		return theRes.getMeta().getLastUpdated();
	}

	private IBaseBundle processTransactionAsSubRequest(
			RequestDetails theRequestDetails, IBaseBundle theRequest, String theActionName, boolean theNestedMode) {
		BaseStorageDao.markRequestAsProcessingSubRequest(theRequestDetails);
		try {

			// Interceptor call: STORAGE_TRANSACTION_PROCESSING
			IInterceptorBroadcaster compositeBroadcaster = CompositeInterceptorBroadcaster.newCompositeBroadcaster(
					myInterceptorBroadcaster, theRequestDetails);
			if (compositeBroadcaster.hasHooks(Pointcut.STORAGE_TRANSACTION_PROCESSING)) {
				HookParams params = new HookParams()
						.add(RequestDetails.class, theRequestDetails)
						.addIfMatchesType(ServletRequestDetails.class, theRequest)
						.add(IBaseBundle.class, theRequest);
				compositeBroadcaster.callHooks(Pointcut.STORAGE_TRANSACTION_PROCESSING, params);
			}

			return processTransaction(theRequestDetails, theRequest, theActionName, theNestedMode);
		} finally {
			BaseStorageDao.clearRequestAsProcessingSubRequest(theRequestDetails);
		}
	}

	@VisibleForTesting
	public void setTxManager(PlatformTransactionManager theTxManager) {
		myTxManager = theTxManager;
	}

	private IBaseBundle batch(final RequestDetails theRequestDetails, IBaseBundle theRequest, boolean theNestedMode) {
		TransactionSemanticsHeader transactionSemantics = TransactionSemanticsHeader.DEFAULT;
		if (theRequestDetails != null) {
			String transactionSemanticsString = theRequestDetails.getHeader("X-Transaction-Semantics");
			if (transactionSemanticsString != null) {
				transactionSemantics = TransactionSemanticsHeader.parse(transactionSemanticsString);
			}
		}

		int totalAttempts = defaultIfNull(transactionSemantics.getRetryCount(), 0) + 1;
		boolean switchedToBatch = false;

		int minRetryDelay = defaultIfNull(transactionSemantics.getMinRetryDelay(), 0);
		int maxRetryDelay = defaultIfNull(transactionSemantics.getMaxRetryDelay(), 0);

		// Don't let the user request a crazy delay, this could be used
		// as a DOS attack
		maxRetryDelay = Math.max(maxRetryDelay, minRetryDelay);
		maxRetryDelay = Math.min(maxRetryDelay, 10000);
		totalAttempts = Math.min(totalAttempts, 5);

		IBaseBundle response;
		for (int i = 1; ; i++) {
			try {
				if (i < totalAttempts && transactionSemantics.isTryBatchAsTransactionFirst()) {
					BundleUtil.setBundleType(myContext, theRequest, "transaction");
					response = processTransaction(theRequestDetails, theRequest, "Transaction", theNestedMode);
				} else {
					BundleUtil.setBundleType(myContext, theRequest, "batch");
					response = processBatch(theRequestDetails, theRequest, theNestedMode);
				}
				break;
			} catch (BaseServerResponseException e) {
				if (i >= totalAttempts || theNestedMode) {
					throw e;
				}

				long delay = RandomUtils.insecure().randomLong(minRetryDelay, maxRetryDelay);
				String sleepMessage = "";
				if (delay > 0) {
					sleepMessage = "Sleeping for " + delay + " ms. ";
				}

				String switchedToBatchMessage = "";
				if (switchedToBatch) {
					switchedToBatchMessage = " (as batch)";
				}

				String switchingToBatchMessage = "";
				if (i + 1 == totalAttempts && transactionSemantics.isTryBatchAsTransactionFirst()) {
					switchingToBatchMessage = "Performing final retry using batch semantics. ";
					switchedToBatch = true;
					BundleUtil.setBundleType(myContext, theRequest, "batch");
				}

				ourLog.warn(
						"Transaction processing attempt{} {}/{} failed. {}{}",
						switchedToBatchMessage,
						i,
						totalAttempts,
						sleepMessage,
						switchingToBatchMessage);

				if (delay > 0) {
					ThreadUtils.sleepQuietly(Duration.ofMillis(delay));
				}
			}
		}

		return response;
	}

	private IBaseBundle processBatch(RequestDetails theRequestDetails, IBaseBundle theRequest, boolean theNestedMode) {
		ourLog.info(
				"Beginning batch with {} resources",
				myVersionAdapter.getEntries(theRequest).size());

		long start = System.currentTimeMillis();

		IBaseBundle response =
				myVersionAdapter.createBundle(org.hl7.fhir.r4.model.Bundle.BundleType.BATCHRESPONSE.toCode());
		Map<Integer, Object> responseMap = new ConcurrentHashMap<>();

		List<IBase> requestEntries = myVersionAdapter.getEntries(theRequest);
		int requestEntriesSize = requestEntries.size();

		// Now, run all non-gets sequentially, and all gets are submitted to the executor to run (potentially) in
		// parallel
		// The result is kept in the map to save the original position
		List<RetriableBundleTask> getCalls = new ArrayList<>();
		List<RetriableBundleTask> nonGetCalls = new ArrayList<>();

		CountDownLatch completionLatch = new CountDownLatch(requestEntriesSize);
		for (int i = 0; i < requestEntriesSize; i++) {
			IBase nextRequestEntry = requestEntries.get(i);
			RetriableBundleTask retriableBundleTask = new RetriableBundleTask(
					completionLatch, theRequestDetails, responseMap, i, nextRequestEntry, theNestedMode);
			if (myVersionAdapter
					.getEntryRequestVerb(myContext, nextRequestEntry)
					.equalsIgnoreCase("GET")) {
				getCalls.add(retriableBundleTask);
			} else {
				nonGetCalls.add(retriableBundleTask);
			}
		}
		// Execute all non-gets on calling thread.
		nonGetCalls.forEach(RetriableBundleTask::run);
		// Execute all gets (potentially in a pool)
		if (myStorageSettings.getBundleBatchPoolSize() == 1) {
			getCalls.forEach(RetriableBundleTask::run);
		} else {
			getCalls.forEach(getCall -> getTaskExecutor().execute(getCall));
		}

		// waiting for all async tasks to be completed
		AsyncUtil.awaitLatchAndIgnoreInterrupt(completionLatch, 300L, TimeUnit.SECONDS);

		// Now, create the bundle response in original order
		Object nextResponseEntry;
		for (int i = 0; i < requestEntriesSize; i++) {

			nextResponseEntry = responseMap.get(i);
			if (nextResponseEntry instanceof ServerResponseExceptionHolder) {
				ServerResponseExceptionHolder caughtEx = (ServerResponseExceptionHolder) nextResponseEntry;
				if (caughtEx.getException() != null) {
					IBase nextEntry = myVersionAdapter.addEntry(response);
					populateEntryWithOperationOutcome(caughtEx.getException(), nextEntry);
					myVersionAdapter.setResponseStatus(
							nextEntry, toStatusString(caughtEx.getException().getStatusCode()));
				}
			} else {
				myVersionAdapter.addEntry(response, (IBase) nextResponseEntry);
			}
		}

		long delay = System.currentTimeMillis() - start;
		ourLog.info("Batch completed in {}ms", delay);

		return response;
	}

	@VisibleForTesting
	public void setHapiTransactionService(HapiTransactionService theHapiTransactionService) {
		myHapiTransactionService = theHapiTransactionService;
	}

	private IBaseBundle processTransaction(
			final RequestDetails theRequestDetails,
			final IBaseBundle theRequest,
			final String theActionName,
			boolean theNestedMode) {
		validateDependencies();

		String transactionType = myVersionAdapter.getBundleType(theRequest);

		if (org.hl7.fhir.r4.model.Bundle.BundleType.BATCH.toCode().equals(transactionType)) {
			return batch(theRequestDetails, theRequest, theNestedMode);
		}

		if (transactionType == null) {
			String message = "Transaction Bundle did not specify valid Bundle.type, assuming "
					+ Bundle.BundleType.TRANSACTION.toCode();
			ourLog.warn(message);
			transactionType = org.hl7.fhir.r4.model.Bundle.BundleType.TRANSACTION.toCode();
		}
		if (!org.hl7.fhir.r4.model.Bundle.BundleType.TRANSACTION.toCode().equals(transactionType)) {
			throw new InvalidRequestException(
					Msg.code(527) + "Unable to process transaction where incoming Bundle.type = " + transactionType);
		}

		List<IBase> requestEntries = myVersionAdapter.getEntries(theRequest);
		int numberOfEntries = requestEntries.size();

		if (myStorageSettings.getMaximumTransactionBundleSize() != null
				&& numberOfEntries > myStorageSettings.getMaximumTransactionBundleSize()) {
			throw new PayloadTooLargeException(
					Msg.code(528) + "Transaction Bundle Too large.  Transaction bundle contains " + numberOfEntries
							+ " which exceedes the maximum permitted transaction bundle size of "
							+ myStorageSettings.getMaximumTransactionBundleSize());
		}

		ourLog.debug("Beginning {} with {} resources", theActionName, numberOfEntries);

		final TransactionDetails transactionDetails = new TransactionDetails();
		transactionDetails.setFhirTransaction(true);
		final StopWatch transactionStopWatch = new StopWatch();

		// Do all entries have a verb?
		for (int i = 0; i < numberOfEntries; i++) {
			IBase nextReqEntry = requestEntries.get(i);
			String verb = myVersionAdapter.getEntryRequestVerb(myContext, nextReqEntry);
			if (verb == null || !isValidVerb(verb)) {
				throw new InvalidRequestException(Msg.code(529)
						+ myContext
								.getLocalizer()
								.getMessage(BaseStorageDao.class, "transactionEntryHasInvalidVerb", verb, i));
			}
		}

		/*
		 * We want to execute the transaction request bundle elements in the order
		 * specified by the FHIR specification (see TransactionSorter) so we save the
		 * original order in the request, then sort it.
		 *
		 * Entries with a type of GET are removed from the bundle so that they
		 * can be processed at the very end. We do this because the incoming resources
		 * are saved in a two-phase way in order to deal with interdependencies, and
		 * we want the GET processing to use the final indexing state
		 */
		final IBaseBundle response =
				myVersionAdapter.createBundle(org.hl7.fhir.r4.model.Bundle.BundleType.TRANSACTIONRESPONSE.toCode());
		List<IBase> getEntries = new ArrayList<>();
		final IdentityHashMap<IBase, Integer> originalRequestOrder = new IdentityHashMap<>();
		for (int i = 0; i < requestEntries.size(); i++) {
			IBase requestEntry = requestEntries.get(i);
			originalRequestOrder.put(requestEntry, i);
			myVersionAdapter.addEntry(response);
			if (myVersionAdapter.getEntryRequestVerb(myContext, requestEntry).equals("GET")) {
				getEntries.add(requestEntry);
			}
		}

		/*
		 * See FhirSystemDaoDstu3Test#testTransactionWithPlaceholderIdInMatchUrl
		 * Basically if the resource has a match URL that references a placeholder,
		 * we try to handle the resource with the placeholder first.
		 */
		Set<String> placeholderIds = new HashSet<>();
		for (IBase nextEntry : requestEntries) {
			String fullUrl = myVersionAdapter.getFullUrl(nextEntry);
			if (isNotBlank(fullUrl) && fullUrl.startsWith(URN_PREFIX)) {
				placeholderIds.add(fullUrl);
			}
		}
		requestEntries.sort(new TransactionSorter(placeholderIds));

		// perform all writes
		prepareThenExecuteTransactionWriteOperations(
				theRequestDetails,
				theActionName,
				transactionDetails,
				transactionStopWatch,
				response,
				originalRequestOrder,
				requestEntries);

		// perform all gets
		// (we do these last so that the gets happen on the final state of the DB;
		// see above note)
		doTransactionReadOperations(
				theRequestDetails, response, getEntries, originalRequestOrder, transactionStopWatch, theNestedMode);

		// Interceptor broadcast: JPA_PERFTRACE_INFO
		IInterceptorBroadcaster compositeBroadcaster =
				CompositeInterceptorBroadcaster.newCompositeBroadcaster(myInterceptorBroadcaster, theRequestDetails);
		if (compositeBroadcaster.hasHooks(Pointcut.JPA_PERFTRACE_INFO)) {
			String taskDurations = transactionStopWatch.formatTaskDurations();
			StorageProcessingMessage message = new StorageProcessingMessage();
			message.setMessage("Transaction timing:\n" + taskDurations);
			HookParams params = new HookParams()
					.add(RequestDetails.class, theRequestDetails)
					.addIfMatchesType(ServletRequestDetails.class, theRequestDetails)
					.add(StorageProcessingMessage.class, message);
			compositeBroadcaster.callHooks(Pointcut.JPA_PERFTRACE_INFO, params);
		}

		return response;
	}

	@SuppressWarnings("unchecked")
	private void doTransactionReadOperations(
			final RequestDetails theRequestDetails,
			IBaseBundle theResponse,
			List<IBase> theGetEntries,
			IdentityHashMap<IBase, Integer> theOriginalRequestOrder,
			StopWatch theTransactionStopWatch,
			boolean theNestedMode) {
		if (theGetEntries.size() > 0) {
			theTransactionStopWatch.startTask("Process " + theGetEntries.size() + " GET entries");

			/*
			 * Loop through the request and process any entries of type GET
			 */
			for (IBase nextReqEntry : theGetEntries) {
				if (theNestedMode) {
					throw new InvalidRequestException(
							Msg.code(530) + "Can not invoke read operation on nested transaction");
				}

				if (!(theRequestDetails instanceof ServletRequestDetails)) {
					throw new MethodNotAllowedException(
							Msg.code(531) + "Can not call transaction GET methods from this context");
				}

				ServletRequestDetails srd = (ServletRequestDetails) theRequestDetails;
				Integer originalOrder = theOriginalRequestOrder.get(nextReqEntry);
				IBase nextRespEntry =
						(IBase) myVersionAdapter.getEntries(theResponse).get(originalOrder);

				ArrayListMultimap<String, String> paramValues = ArrayListMultimap.create();
				ServletSubRequestDetails requestDetailsForEntry =
						createRequestDetailsForReadEntry(srd, nextReqEntry, paramValues);

				String url = requestDetailsForEntry.getRequestPath();

				BaseMethodBinding method = srd.getServer().determineResourceMethod(requestDetailsForEntry, url);
				if (method == null) {
					throw new IllegalArgumentException(Msg.code(532) + "Unable to handle GET " + url);
				}

				Validate.isTrue(method instanceof BaseResourceReturningMethodBinding, "Unable to handle GET {}", url);
				try {
					BaseResourceReturningMethodBinding methodBinding = (BaseResourceReturningMethodBinding) method;
					requestDetailsForEntry.setRestOperationType(methodBinding.getRestOperationType());

					IBaseResource resource = methodBinding.doInvokeServer(srd.getServer(), requestDetailsForEntry);
					if (paramValues.containsKey(Constants.PARAM_SUMMARY)
							|| paramValues.containsKey(Constants.PARAM_CONTENT)) {
						resource = filterNestedBundle(requestDetailsForEntry, resource);
					}
					myVersionAdapter.setResource(nextRespEntry, resource);
					myVersionAdapter.setResponseStatus(nextRespEntry, toStatusString(Constants.STATUS_HTTP_200_OK));
				} catch (NotModifiedException e) {
					myVersionAdapter.setResponseStatus(
							nextRespEntry, toStatusString(Constants.STATUS_HTTP_304_NOT_MODIFIED));
				} catch (BaseServerResponseException e) {
					ourLog.info("Failure processing transaction GET {}: {}", url, e.toString());
					myVersionAdapter.setResponseStatus(nextRespEntry, toStatusString(e.getStatusCode()));
					populateEntryWithOperationOutcome(e, nextRespEntry);
				}
			}
			theTransactionStopWatch.endCurrentTask();
		}
	}

	/**
	 * Checks if the given request entry has the extension specified to override the partition ids to use when processing that entry.
	 * If the extension is present, this method will set the partition ids header in the given request details.
	 */
	private void setRequestPartitionHeaderIfEntryHasTheExtension(IBase theReqEntry, RequestDetails theRequestDetails) {
		Optional<IBaseExtension<?, ?>> partitionIdsExtensionOptional =
				myVersionAdapter.getEntryRequestExtensionByUrl(theReqEntry, EXTENSION_TRANSACTION_ENTRY_PARTITION_IDS);
		if (partitionIdsExtensionOptional.isPresent()
				&& partitionIdsExtensionOptional.get().getValue() instanceof IPrimitiveType<?>) {
			IPrimitiveType<?> valueAsPrimitiveType =
					(IPrimitiveType<?>) partitionIdsExtensionOptional.get().getValue();
			String value = valueAsPrimitiveType.getValueAsString();
			theRequestDetails.setHeaders(Constants.HEADER_X_REQUEST_PARTITION_IDS, List.of(value));
		}
	}

	/**
	 * Creates a new RequestDetails object based on the given RequestDetails. The new RequestDetails is to be used
	 * when processing a write entry.
	 * If the entry.request has the extension to override the partition ids, this method
	 * sets the partition ids header in the newly created request details with the values from extension.
	 * This allows using different partitions for different entries in the same transaction.
	 *
	 * @return the newly created request details
	 */
	private RequestDetails createRequestDetailsForWriteEntry(
			RequestDetails theRequestDetails, IBase theEntry, String theUrl, String theVerb) {

		if (theRequestDetails == null) {
			ourLog.warn(
					"The RequestDetails passed in to the transaction is null. Cannot create a new RequestDetails for transaction entry.");
			return null;
		}

		RequestDetails newRequestDetails;
		if (theRequestDetails instanceof ServletRequestDetails) {
			newRequestDetails = ServletRequestUtil.getServletSubRequestDetails(
					(ServletRequestDetails) theRequestDetails, theUrl, theVerb, ArrayListMultimap.create());
		} else if (theRequestDetails instanceof SystemRequestDetails) {
			newRequestDetails = new SystemRequestDetails((SystemRequestDetails) theRequestDetails);
		} else {
			// RequestDetails is not a ServletRequestDetails or SystemRequestDetails, and I don't know how to properly
			// clone such a RequestDetails. Use the original RequestDetails without making any entry specific
			// modifications
			ourLog.warn(
					"Cannot create a new RequestDetails for transaction entry out of the existing RequestDetails which is of type '{}'"
							+ "Using the original RequestDetails for transaction entries without any entry specific modifications.",
					theRequestDetails.getClass().getSimpleName());
			return theRequestDetails;
		}
		setRequestPartitionHeaderIfEntryHasTheExtension(theEntry, newRequestDetails);
		return newRequestDetails;
	}

	/**
	 * Creates a new RequestDetails based on the given one. The returned RequestDetails is to be used when processing
	 * a GET entry of transaction.
	 * It sets the headers in the newly request details according to the information from entry.request if needed.
	 * Currently, GET entries only support ServletRequestDetails so it handles only that type.
	 */
	private ServletSubRequestDetails createRequestDetailsForReadEntry(
			ServletRequestDetails theRequestDetails, IBase theEntry, ArrayListMultimap<String, String> theParamValues) {

		final String verb = "GET";
		String transactionUrl = extractTransactionUrlOrThrowException(theEntry, verb);

		ServletSubRequestDetails subRequestDetails =
				ServletRequestUtil.getServletSubRequestDetails(theRequestDetails, transactionUrl, verb, theParamValues);

		if (isNotBlank(myVersionAdapter.getEntryRequestIfMatch(theEntry))) {
			subRequestDetails.addHeader(Constants.HEADER_IF_MATCH, myVersionAdapter.getEntryRequestIfMatch(theEntry));
		}
		if (isNotBlank(myVersionAdapter.getEntryRequestIfNoneExist(theEntry))) {
			subRequestDetails.addHeader(
					Constants.HEADER_IF_NONE_EXIST, myVersionAdapter.getEntryRequestIfNoneExist(theEntry));
		}
		if (isNotBlank(myVersionAdapter.getEntryRequestIfNoneMatch(theEntry))) {
			subRequestDetails.addHeader(
					Constants.HEADER_IF_NONE_MATCH, myVersionAdapter.getEntryRequestIfNoneMatch(theEntry));
		}

		setRequestPartitionHeaderIfEntryHasTheExtension(theEntry, subRequestDetails);

		return subRequestDetails;
	}

	/**
	 * All of the write operations in the transaction (PUT, POST, etc.. basically anything
	 * except GET) are performed in their own database transaction before we do the reads.
	 * We do this because the reads (specifically the searches) often spawn their own
	 * secondary database transaction and if we allow that within the primary
	 * database transaction we can end up with deadlocks if the server is under
	 * heavy load with lots of concurrent transactions using all available
	 * database connections.
	 */
	@SuppressWarnings("unchecked")
	private void prepareThenExecuteTransactionWriteOperations(
			RequestDetails theRequestDetails,
			String theActionName,
			TransactionDetails theTransactionDetails,
			StopWatch theTransactionStopWatch,
			IBaseBundle theResponse,
			IdentityHashMap<IBase, Integer> theOriginalRequestOrder,
			List<IBase> theEntries) {

		TransactionWriteOperationsDetails writeOperationsDetails = null;
		if (haveWriteOperationsHooks(theRequestDetails)) {
			writeOperationsDetails = buildWriteOperationsDetails(theEntries);
			callWriteOperationsHook(
					Pointcut.STORAGE_TRANSACTION_WRITE_OPERATIONS_PRE,
					theRequestDetails,
					theTransactionDetails,
					writeOperationsDetails);
		}

		TransactionCallback<EntriesToProcessMap> txCallback = status -> {
			final Set<IIdType> allIds = new LinkedHashSet<>();
			final IdSubstitutionMap idSubstitutions = new IdSubstitutionMap();
			final Map<IIdType, DaoMethodOutcome> idToPersistedOutcome = new LinkedHashMap<>();

			EntriesToProcessMap retVal = doTransactionWriteOperations(
					theRequestDetails,
					theActionName,
					theTransactionDetails,
					allIds,
					idSubstitutions,
					idToPersistedOutcome,
					theResponse,
					theOriginalRequestOrder,
					theEntries,
					theTransactionStopWatch);

			theTransactionStopWatch.startTask("Commit writes to database");
			return retVal;
		};
		EntriesToProcessMap entriesToProcess;

		RequestPartitionId requestPartitionId =
				determineRequestPartitionIdForWriteEntries(theRequestDetails, theEntries);

		try {
			entriesToProcess = myHapiTransactionService
					.withRequest(theRequestDetails)
					.withRequestPartitionId(requestPartitionId)
					.withTransactionDetails(theTransactionDetails)
					.execute(txCallback);
		} finally {
			if (haveWriteOperationsHooks(theRequestDetails)) {
				callWriteOperationsHook(
						Pointcut.STORAGE_TRANSACTION_WRITE_OPERATIONS_POST,
						theRequestDetails,
						theTransactionDetails,
						writeOperationsDetails);
			}
		}

		theTransactionStopWatch.endCurrentTask();

		for (Map.Entry<IBase, IIdType> nextEntry : entriesToProcess.entrySet()) {
			String responseLocation = nextEntry.getValue().toUnqualified().getValue();
			String responseEtag = nextEntry.getValue().getVersionIdPart();
			myVersionAdapter.setResponseLocation(nextEntry.getKey(), responseLocation);
			myVersionAdapter.setResponseETag(nextEntry.getKey(), responseEtag);
		}
	}

	/**
	 * This method looks at the FHIR actions being performed in a List of bundle entries,
	 * and determines the associated request partitions.
	 */
	@Nullable
	protected RequestPartitionId determineRequestPartitionIdForWriteEntries(
			RequestDetails theRequestDetails, List<IBase> theEntries) {
		if (!myPartitionSettings.isPartitioningEnabled()) {
			return RequestPartitionId.allPartitions();
		}

		return theEntries.stream()
				.map(e -> getEntryRequestPartitionId(theRequestDetails, e))
				.reduce(null, (accumulator, nextPartition) -> {
					if (accumulator == null) {
						return nextPartition;
					} else if (nextPartition == null) {
						return accumulator;
					} else if (myHapiTransactionService.isCompatiblePartition(accumulator, nextPartition)) {
						return accumulator.mergeIds(nextPartition);
					} else {
						String msg = myContext
								.getLocalizer()
								.getMessage(
										BaseTransactionProcessor.class, "multiplePartitionAccesses", theEntries.size());
						throw new InvalidRequestException(Msg.code(2541) + msg);
					}
				});
	}

	@Nullable
	private RequestPartitionId getEntryRequestPartitionId(RequestDetails theRequestDetails, IBase nextEntry) {

		RequestPartitionId nextWriteEntryRequestPartitionId = null;
		String verb = myVersionAdapter.getEntryRequestVerb(myContext, nextEntry);
		String url = extractTransactionUrlOrThrowException(nextEntry, verb);
		RequestDetails requestDetailsForEntry =
				createRequestDetailsForWriteEntry(theRequestDetails, nextEntry, url, verb);
		if (isNotBlank(verb)) {
			BundleEntryTransactionMethodEnum verbEnum = BundleEntryTransactionMethodEnum.valueOf(verb);
			switch (verbEnum) {
				case GET:
					nextWriteEntryRequestPartitionId = null;
					break;
				case DELETE: {
					String requestUrl = myVersionAdapter.getEntryRequestUrl(nextEntry);
					if (isNotBlank(requestUrl)) {
						IdType id = new IdType(requestUrl);
						String resourceType = id.getResourceType();
						ReadPartitionIdRequestDetails details =
								ReadPartitionIdRequestDetails.forDelete(resourceType, id);
						nextWriteEntryRequestPartitionId =
								myRequestPartitionHelperService.determineReadPartitionForRequest(
										requestDetailsForEntry, details);
					}
					break;
				}
				case PATCH: {
					String requestUrl = myVersionAdapter.getEntryRequestUrl(nextEntry);
					if (isNotBlank(requestUrl)) {
						IdType id = new IdType(requestUrl);
						String resourceType = id.getResourceType();
						ReadPartitionIdRequestDetails details =
								ReadPartitionIdRequestDetails.forPatch(resourceType, id);
						nextWriteEntryRequestPartitionId =
								myRequestPartitionHelperService.determineReadPartitionForRequest(
										requestDetailsForEntry, details);
					}
					break;
				}
				case POST: {
					IBaseResource resource = myVersionAdapter.getResource(nextEntry);
					String resourceType = myContext.getResourceType(resource);
					nextWriteEntryRequestPartitionId =
							myRequestPartitionHelperService.determineReadPartitionForRequestForSearchType(
									requestDetailsForEntry, resourceType);
					break;
				}
				case PUT: {
					IBaseResource resource = myVersionAdapter.getResource(nextEntry);
					if (resource != null) {
						String resourceType = myContext.getResourceType(resource);
						nextWriteEntryRequestPartitionId =
								myRequestPartitionHelperService.determineCreatePartitionForRequest(
										requestDetailsForEntry, resource, resourceType);
					}
					break;
				}
			}
		}
		return nextWriteEntryRequestPartitionId;
	}

	private boolean haveWriteOperationsHooks(RequestDetails theRequestDetails) {
		IInterceptorBroadcaster compositeBroadcaster =
				CompositeInterceptorBroadcaster.newCompositeBroadcaster(myInterceptorBroadcaster, theRequestDetails);
		return compositeBroadcaster.hasHooks(Pointcut.STORAGE_TRANSACTION_WRITE_OPERATIONS_PRE)
				|| compositeBroadcaster.hasHooks(Pointcut.STORAGE_TRANSACTION_WRITE_OPERATIONS_POST);
	}

	private void callWriteOperationsHook(
			Pointcut thePointcut,
			RequestDetails theRequestDetails,
			TransactionDetails theTransactionDetails,
			TransactionWriteOperationsDetails theWriteOperationsDetails) {
		IInterceptorBroadcaster compositeBroadcaster =
				CompositeInterceptorBroadcaster.newCompositeBroadcaster(myInterceptorBroadcaster, theRequestDetails);
		if (compositeBroadcaster.hasHooks(thePointcut)) {
			HookParams params = new HookParams()
					.add(TransactionDetails.class, theTransactionDetails)
					.add(TransactionWriteOperationsDetails.class, theWriteOperationsDetails);
			compositeBroadcaster.callHooks(thePointcut, params);
		}
	}

	@SuppressWarnings("unchecked")
	private TransactionWriteOperationsDetails buildWriteOperationsDetails(List<IBase> theEntries) {
		TransactionWriteOperationsDetails writeOperationsDetails;
		List<String> updateRequestUrls = new ArrayList<>();
		List<String> conditionalCreateRequestUrls = new ArrayList<>();
		// Extract
		for (IBase nextEntry : theEntries) {
			String method = myVersionAdapter.getEntryRequestVerb(myContext, nextEntry);
			if ("PUT".equals(method)) {
				String requestUrl = myVersionAdapter.getEntryRequestUrl(nextEntry);
				if (isNotBlank(requestUrl)) {
					updateRequestUrls.add(requestUrl);
				}
			} else if ("POST".equals(method)) {
				String requestUrl = myVersionAdapter.getEntryRequestIfNoneExist(nextEntry);
				if (isNotBlank(requestUrl) && requestUrl.contains("?")) {
					conditionalCreateRequestUrls.add(requestUrl);
				}
			}
		}

		writeOperationsDetails = new TransactionWriteOperationsDetails();
		writeOperationsDetails.setUpdateRequestUrls(updateRequestUrls);
		writeOperationsDetails.setConditionalCreateRequestUrls(conditionalCreateRequestUrls);
		return writeOperationsDetails;
	}

	private boolean isValidVerb(String theVerb) {
		try {
			return org.hl7.fhir.r4.model.Bundle.HTTPVerb.fromCode(theVerb) != null;
		} catch (FHIRException theE) {
			return false;
		}
	}

	/**
	 * This method is called for nested bundles (e.g. if we received a transaction with an entry that
	 * was a GET search, this method is called on the bundle for the search result, that will be placed in the
	 * outer bundle). This method applies the _summary and _content parameters to the output of
	 * that bundle.
	 * <p>
	 * TODO: This isn't the most efficient way of doing this.. hopefully we can come up with something better in the future.
	 */
	private IBaseResource filterNestedBundle(RequestDetails theRequestDetails, IBaseResource theResource) {
		IParser p = myContext.newJsonParser();
		RestfulServerUtils.configureResponseParser(theRequestDetails, p);
		return p.parseResource(theResource.getClass(), p.encodeResourceToString(theResource));
	}

	protected void validateDependencies() {
		Validate.notNull(myContext);
		Validate.notNull(myTxManager);
	}

	private IIdType newIdType(String theValue) {
		return myContext.getVersion().newIdType().setValue(theValue);
	}

	/**
	 * Searches for duplicate conditional creates and consolidates them.
	 */
	@SuppressWarnings("unchecked")
	private void consolidateDuplicateConditionals(
			RequestDetails theRequestDetails, String theActionName, List<IBase> theEntries) {
		final Set<String> keysWithNoFullUrl = new HashSet<>();
		final HashMap<String, String> keyToUuid = new HashMap<>();

		for (int index = 0, originalIndex = 0; index < theEntries.size(); index++, originalIndex++) {
			IBase nextReqEntry = theEntries.get(index);
			IBaseResource resource = myVersionAdapter.getResource(nextReqEntry);
			if (resource != null) {
				String verb = myVersionAdapter.getEntryRequestVerb(myContext, nextReqEntry);
				String entryFullUrl = myVersionAdapter.getFullUrl(nextReqEntry);
				String requestUrl = myVersionAdapter.getEntryRequestUrl(nextReqEntry);
				String ifNoneExist = myVersionAdapter.getEntryRequestIfNoneExist(nextReqEntry);

				// Conditional UPDATE
				boolean consolidateEntryCandidate = false;
				String conditionalUrl;
				switch (verb) {
					case "PUT":
						conditionalUrl = requestUrl;
						if (isNotBlank(requestUrl)) {
							int questionMarkIndex = requestUrl.indexOf('?');
							if (questionMarkIndex >= 0 && requestUrl.length() > (questionMarkIndex + 1)) {
								consolidateEntryCandidate = true;
							}
						}
						break;

						// Conditional CREATE
					case "POST":
						conditionalUrl = ifNoneExist;
						if (isNotBlank(ifNoneExist)) {
							if (isBlank(entryFullUrl) || !entryFullUrl.equals(requestUrl)) {
								consolidateEntryCandidate = true;
							}
						}
						break;

					default:
						continue;
				}

				if (isNotBlank(conditionalUrl) && !conditionalUrl.contains("?")) {
					conditionalUrl = myContext.getResourceType(resource) + "?" + conditionalUrl;
				}

				String key = verb + "|" + conditionalUrl;
				if (consolidateEntryCandidate) {
					if (isBlank(entryFullUrl)) {
						if (isNotBlank(conditionalUrl)) {
							if (!keysWithNoFullUrl.add(key)) {
								throw new InvalidRequestException(Msg.code(2008) + "Unable to process " + theActionName
										+ " - Request contains multiple anonymous entries (Bundle.entry.fullUrl not populated) with conditional URL: \""
										+ UrlUtil.sanitizeUrlPart(conditionalUrl)
										+ "\". Does transaction request contain duplicates?");
							}
						}
					} else {
						if (!keyToUuid.containsKey(key)) {
							keyToUuid.put(key, entryFullUrl);
						} else {
							String msg = "Discarding transaction bundle entry " + originalIndex
									+ " as it contained a duplicate conditional " + verb;
							ourLog.info(msg);
							// Interceptor broadcast: JPA_PERFTRACE_INFO
							IInterceptorBroadcaster compositeBroadcaster =
									CompositeInterceptorBroadcaster.newCompositeBroadcaster(
											myInterceptorBroadcaster, theRequestDetails);
							if (compositeBroadcaster.hasHooks(Pointcut.JPA_PERFTRACE_WARNING)) {
								StorageProcessingMessage message = new StorageProcessingMessage().setMessage(msg);
								HookParams params = new HookParams()
										.add(RequestDetails.class, theRequestDetails)
										.addIfMatchesType(ServletRequestDetails.class, theRequestDetails)
										.add(StorageProcessingMessage.class, message);
								compositeBroadcaster.callHooks(Pointcut.JPA_PERFTRACE_INFO, params);
							}

							theEntries.remove(index);
							index--;
							String existingUuid = keyToUuid.get(key);
							replaceReferencesInEntriesWithConsolidatedUUID(theEntries, entryFullUrl, existingUuid);
						}
					}
				}
			}
		}
	}

	/**
	 * Iterates over all entries, and if it finds any which have references which match the fullUrl of the entry that was consolidated out
	 * replace them with our new consolidated UUID
	 */
	private void replaceReferencesInEntriesWithConsolidatedUUID(
			List<IBase> theEntries, String theEntryFullUrl, String existingUuid) {
		for (IBase nextEntry : theEntries) {
			@SuppressWarnings("unchecked")
			IBaseResource nextResource = myVersionAdapter.getResource(nextEntry);
			if (nextResource != null) {
				for (IBaseReference nextReference :
						myContext.newTerser().getAllPopulatedChildElementsOfType(nextResource, IBaseReference.class)) {
					// We're interested in any references directly to the placeholder ID, but also
					// references that have a resource target that has the placeholder ID.
					String nextReferenceId = nextReference.getReferenceElement().getValue();
					if (isBlank(nextReferenceId) && nextReference.getResource() != null) {
						nextReferenceId =
								nextReference.getResource().getIdElement().getValue();
					}
					if (theEntryFullUrl.equals(nextReferenceId)) {
						nextReference.setReference(existingUuid);
						nextReference.setResource(null);
					}
				}
			}
		}
	}

	/**
	 * Retrieves the next resource id (IIdType) from the base resource and next request entry.
	 *
	 * @param theBaseResource - base resource
	 * @param theNextReqEntry - next request entry
	 * @param theAllIds       - set of all IIdType values
	 * @param theVerb
	 * @return
	 */
	private IIdType getNextResourceIdFromBaseResource(
			IBaseResource theBaseResource, IBase theNextReqEntry, Set<IIdType> theAllIds, String theVerb) {
		IIdType nextResourceId = null;
		if (theBaseResource != null) {
			nextResourceId = theBaseResource.getIdElement();

			@SuppressWarnings("unchecked")
			String fullUrl = myVersionAdapter.getFullUrl(theNextReqEntry);
			if (isNotBlank(fullUrl)) {
				IIdType fullUrlIdType = newIdType(fullUrl);
				if (isPlaceholder(fullUrlIdType)) {
					nextResourceId = fullUrlIdType;
				} else if (!nextResourceId.hasIdPart()) {
					nextResourceId = fullUrlIdType;
				}
			}

			if (nextResourceId.hasIdPart() && !isPlaceholder(nextResourceId)) {
				int colonIndex = nextResourceId.getIdPart().indexOf(':');
				if (colonIndex != -1) {
					if (INVALID_PLACEHOLDER_PATTERN
							.matcher(nextResourceId.getIdPart())
							.matches()) {
						throw new InvalidRequestException(
								Msg.code(533) + "Invalid placeholder ID found: " + nextResourceId.getIdPart()
										+ " - Must be of the form 'urn:uuid:[uuid]' or 'urn:oid:[oid]'");
					}
				}
			}

			if (nextResourceId.hasIdPart() && !nextResourceId.hasResourceType() && !isPlaceholder(nextResourceId)) {
				nextResourceId = newIdType(toResourceName(theBaseResource.getClass()), nextResourceId.getIdPart());
				theBaseResource.setId(nextResourceId);
			}

			/*
			 * Ensure that the bundle doesn't have any duplicates, since this causes all kinds of weirdness
			 */
			if (isPlaceholder(nextResourceId)) {
				if (!theAllIds.add(nextResourceId)) {
					throw new InvalidRequestException(Msg.code(534)
							+ myContext
									.getLocalizer()
									.getMessage(
											BaseStorageDao.class,
											"transactionContainsMultipleWithDuplicateId",
											nextResourceId));
				}
			} else if (nextResourceId.hasResourceType() && nextResourceId.hasIdPart() && !"POST".equals(theVerb)) {
				IIdType nextId = nextResourceId.toUnqualifiedVersionless();
				if (!theAllIds.add(nextId)) {
					throw new InvalidRequestException(Msg.code(535)
							+ myContext
									.getLocalizer()
									.getMessage(
											BaseStorageDao.class,
											"transactionContainsMultipleWithDuplicateId",
											nextId));
				}
			}
		}

		return nextResourceId;
	}

	/**
	 * After pre-hooks have been called
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	protected EntriesToProcessMap doTransactionWriteOperations(
			final RequestDetails theRequest,
			String theActionName,
			TransactionDetails theTransactionDetails,
			Set<IIdType> theAllIds,
			IdSubstitutionMap theIdSubstitutions,
			Map<IIdType, DaoMethodOutcome> theIdToPersistedOutcome,
			IBaseBundle theResponse,
			IdentityHashMap<IBase, Integer> theOriginalRequestOrder,
			List<IBase> theEntries,
			StopWatch theTransactionStopWatch) {

		// During a transaction, we don't execute hooks, instead, we execute them all post-transaction.
		theTransactionDetails.beginAcceptingDeferredInterceptorBroadcasts(
				Pointcut.STORAGE_PRECOMMIT_RESOURCE_CREATED,
				Pointcut.STORAGE_PRECOMMIT_RESOURCE_UPDATED,
				Pointcut.STORAGE_PRECOMMIT_RESOURCE_DELETED);
		try {
			Set<String> deletedResources = new HashSet<>();
			DeleteConflictList deleteConflicts = new DeleteConflictList();
			EntriesToProcessMap entriesToProcess = new EntriesToProcessMap();
			Set<IIdType> nonUpdatedEntities = new HashSet<>();
			Set<IBasePersistedResource> updatedEntities = new HashSet<>();
			Map<String, IIdType> conditionalUrlToIdMap = new HashMap<>();
			List<IBaseResource> updatedResources = new ArrayList<>();
			Map<String, Class<? extends IBaseResource>> conditionalRequestUrls = new HashMap<>();

			/*
			 * Look for duplicate conditional creates and consolidate them
			 */
			consolidateDuplicateConditionals(theRequest, theActionName, theEntries);

			/*
			 * Loop through the request and process any entries of type
			 * PUT, POST or DELETE
			 */
			String previousVerb = null;
			for (int i = 0; i < theEntries.size(); i++) {
				if (i % 250 == 0) {
					ourLog.debug("Processed {} non-GET entries out of {} in transaction", i, theEntries.size());
				}

				IBase nextReqEntry = theEntries.get(i);

				String verb = myVersionAdapter.getEntryRequestVerb(myContext, nextReqEntry);

				if (previousVerb != null && !previousVerb.equals(verb)) {
					handleVerbChangeInTransactionWriteOperations();
				}
				previousVerb = verb;
				if ("GET".equals(verb)) {
					continue;
				}

				String url = extractAndVerifyTransactionUrlForEntry(nextReqEntry, verb);
				RequestDetails requestDetailsForEntry =
						createRequestDetailsForWriteEntry(theRequest, nextReqEntry, url, verb);

				IBaseResource res = myVersionAdapter.getResource(nextReqEntry);
				IIdType nextResourceId = getNextResourceIdFromBaseResource(res, nextReqEntry, theAllIds, verb);

				String resourceType = res != null ? myContext.getResourceType(res) : null;
				Integer order = theOriginalRequestOrder.get(nextReqEntry);
				IBase nextRespEntry =
						(IBase) myVersionAdapter.getEntries(theResponse).get(order);

				theTransactionStopWatch.startTask(
						"Bundle.entry[" + i + "]: " + verb + " " + defaultString(resourceType));

				if (res != null) {
					String previousResourceId = res.getIdElement().getValue();
					theTransactionDetails.addRollbackUndoAction(() -> res.setId(previousResourceId));
				}

				switch (verb) {
					case "POST": {
						// CREATE
						validateResourcePresent(res, order, verb);

						IFhirResourceDao resourceDao = getDaoOrThrowException(res.getClass());
						res.setId((String) null);

						DaoMethodOutcome outcome;
						String matchUrl = myVersionAdapter.getEntryRequestIfNoneExist(nextReqEntry);
						matchUrl = performIdSubstitutionsInMatchUrl(theIdSubstitutions, matchUrl);
						// create individual resource
						outcome =
								resourceDao.create(res, matchUrl, false, requestDetailsForEntry, theTransactionDetails);
						setConditionalUrlToBeValidatedLater(conditionalUrlToIdMap, matchUrl, outcome.getId());
						res.setId(outcome.getId());

						if (nextResourceId != null) {
							handleTransactionCreateOrUpdateOutcome(
									theIdSubstitutions,
									theIdToPersistedOutcome,
									nextResourceId,
									outcome,
									nextRespEntry,
									resourceType,
									res,
									requestDetailsForEntry);
						}
						entriesToProcess.put(nextRespEntry, outcome.getId(), nextRespEntry);
						theTransactionDetails.addResolvedResource(outcome.getId(), outcome::getResource);
						if (outcome.getCreated() == false) {
							nonUpdatedEntities.add(outcome.getId());
						} else {
							if (isNotBlank(matchUrl)) {
								conditionalRequestUrls.put(matchUrl, res.getClass());
							}
						}

						break;
					}
					case "DELETE": {
						// DELETE
						UrlUtil.UrlParts parts = UrlUtil.parseUrl(url);
						IFhirResourceDao<? extends IBaseResource> dao = toDao(parts, verb, url);
						int status = Constants.STATUS_HTTP_204_NO_CONTENT;
						if (parts.getResourceId() != null) {
							IIdType deleteId = newIdType(parts.getResourceType(), parts.getResourceId());
							if (!deletedResources.contains(deleteId.getValueAsString())) {
								DaoMethodOutcome outcome = dao.delete(
										deleteId, deleteConflicts, requestDetailsForEntry, theTransactionDetails);
								if (outcome.getEntity() != null) {
									deletedResources.add(deleteId.getValueAsString());
									entriesToProcess.put(nextRespEntry, outcome.getId(), nextRespEntry);
								}
								myVersionAdapter.setResponseOutcome(nextRespEntry, outcome.getOperationOutcome());
							}
						} else {
							String matchUrl = parts.getResourceType() + '?' + parts.getParams();
							matchUrl = performIdSubstitutionsInMatchUrl(theIdSubstitutions, matchUrl);
							DeleteMethodOutcome deleteOutcome = dao.deleteByUrl(
									matchUrl, deleteConflicts, requestDetailsForEntry, theTransactionDetails);
							setConditionalUrlToBeValidatedLater(conditionalUrlToIdMap, matchUrl, deleteOutcome.getId());
							List<? extends IBasePersistedResource> allDeleted = deleteOutcome.getDeletedEntities();
							for (IBasePersistedResource deleted : allDeleted) {
								deletedResources.add(deleted.getIdDt()
										.toUnqualifiedVersionless()
										.getValueAsString());
							}
							if (allDeleted.isEmpty()) {
								status = Constants.STATUS_HTTP_204_NO_CONTENT;
							}

							myVersionAdapter.setResponseOutcome(nextRespEntry, deleteOutcome.getOperationOutcome());
						}

						myVersionAdapter.setResponseStatus(nextRespEntry, toStatusString(status));

						break;
					}
					case "PUT": {
						// UPDATE
						validateResourcePresent(res, order, verb);
						@SuppressWarnings("rawtypes")
						IFhirResourceDao resourceDao = getDaoOrThrowException(res.getClass());

						DaoMethodOutcome outcome;
						UrlUtil.UrlParts parts = UrlUtil.parseUrl(url);
						if (isNotBlank(parts.getResourceId())) {
							String version = null;
							if (isNotBlank(myVersionAdapter.getEntryRequestIfMatch(nextReqEntry))) {
								version = ParameterUtil.parseETagValue(
										myVersionAdapter.getEntryRequestIfMatch(nextReqEntry));
							}
							res.setId(newIdType(parts.getResourceType(), parts.getResourceId(), version));
							outcome = resourceDao.update(
									res, null, false, false, requestDetailsForEntry, theTransactionDetails);
						} else {
							if (!shouldConditionalUpdateMatchId(theTransactionDetails, res.getIdElement())) {
								res.setId((String) null);
							}
							String matchUrl;
							if (isNotBlank(parts.getParams())) {
								matchUrl = parts.getResourceType() + '?' + parts.getParams();
							} else {
								matchUrl = parts.getResourceType();
							}
							matchUrl = performIdSubstitutionsInMatchUrl(theIdSubstitutions, matchUrl);
							outcome = resourceDao.update(
									res, matchUrl, false, false, requestDetailsForEntry, theTransactionDetails);
							setConditionalUrlToBeValidatedLater(conditionalUrlToIdMap, matchUrl, outcome.getId());
							if (Boolean.TRUE.equals(outcome.getCreated())) {
								conditionalRequestUrls.put(matchUrl, res.getClass());
							}
						}

						if (outcome.getCreated() == Boolean.FALSE
								|| (outcome.getCreated() == Boolean.TRUE
										&& outcome.getId().getVersionIdPartAsLong() > 1)) {
							updatedEntities.add(outcome.getEntity());
							if (outcome.getResource() != null) {
								updatedResources.add(outcome.getResource());
							}
						}

						theTransactionDetails.addResolvedResource(outcome.getId(), outcome::getResource);
						handleTransactionCreateOrUpdateOutcome(
								theIdSubstitutions,
								theIdToPersistedOutcome,
								nextResourceId,
								outcome,
								nextRespEntry,
								resourceType,
								res,
								requestDetailsForEntry);
						entriesToProcess.put(nextRespEntry, outcome.getId(), nextRespEntry);
						break;
					}
					case "PATCH": {
						// PATCH
						validateResourcePresent(res, order, verb);

						UrlUtil.UrlParts parts = UrlUtil.parseUrl(url);

						String matchUrl = toMatchUrl(nextReqEntry);
						matchUrl = performIdSubstitutionsInMatchUrl(theIdSubstitutions, matchUrl);
						String patchBody = null;
						String contentType;
						IBaseParameters patchBodyParameters = null;
						PatchTypeEnum patchType = null;

						if (res instanceof IBaseBinary) {
							IBaseBinary binary = (IBaseBinary) res;
							if (binary.getContent() != null && binary.getContent().length > 0) {
								patchBody = toUtf8String(binary.getContent());
							}
							contentType = binary.getContentType();
							patchType =
									PatchTypeEnum.forContentTypeOrThrowInvalidRequestException(myContext, contentType);
							if (patchType == PatchTypeEnum.FHIR_PATCH_JSON
									|| patchType == PatchTypeEnum.FHIR_PATCH_XML) {
								String msg = myContext
										.getLocalizer()
										.getMessage(
												BaseTransactionProcessor.class, "fhirPatchShouldNotUseBinaryResource");
								throw new InvalidRequestException(Msg.code(536) + msg);
							}
						} else if (res instanceof IBaseParameters) {
							patchBodyParameters = (IBaseParameters) res;
							patchType = PatchTypeEnum.FHIR_PATCH_JSON;
						}

						if (patchBodyParameters == null) {
							if (isBlank(patchBody)) {
								String msg = myContext
										.getLocalizer()
										.getMessage(BaseTransactionProcessor.class, "missingPatchBody");
								throw new InvalidRequestException(Msg.code(537) + msg);
							}
						}

						IFhirResourceDao<? extends IBaseResource> dao = toDao(parts, verb, url);
						IIdType patchId =
								myContext.getVersion().newIdType(parts.getResourceType(), parts.getResourceId());

						String conditionalUrl;
						if (isNull(patchId.getIdPart())) {
							conditionalUrl = url;
						} else {
							conditionalUrl = matchUrl;
							String ifMatch = myVersionAdapter.getEntryRequestIfMatch(nextReqEntry);
							if (isNotBlank(ifMatch)) {
								patchId = UpdateMethodBinding.applyETagAsVersion(ifMatch, patchId);
							}
						}

						DaoMethodOutcome outcome = dao.patchInTransaction(
								patchId,
								conditionalUrl,
								false,
								patchType,
								patchBody,
								patchBodyParameters,
								requestDetailsForEntry,
								theTransactionDetails);
						setConditionalUrlToBeValidatedLater(conditionalUrlToIdMap, matchUrl, outcome.getId());
						updatedEntities.add(outcome.getEntity());
						if (outcome.getResource() != null) {
							updatedResources.add(outcome.getResource());
						}
						if (nextResourceId != null) {
							handleTransactionCreateOrUpdateOutcome(
									theIdSubstitutions,
									theIdToPersistedOutcome,
									nextResourceId,
									outcome,
									nextRespEntry,
									resourceType,
									res,
									requestDetailsForEntry);
						}
						entriesToProcess.put(nextRespEntry, outcome.getId(), nextRespEntry);

						break;
					}
					case "GET":
						break;
					default:
						throw new InvalidRequestException(
								Msg.code(538) + "Unable to handle verb in transaction: " + verb);
				}

				theTransactionStopWatch.endCurrentTask();
			}

			postTransactionProcess(theTransactionDetails);

			/*
			 * Make sure that there are no conflicts from deletions. E.g. we can't delete something
			 * if something else has a reference to it.. Unless the thing that has a reference to it
			 * was also deleted as a part of this transaction, which is why we check this now at the
			 * end.
			 */
			checkForDeleteConflicts(deleteConflicts, deletedResources, updatedResources);

			theIdToPersistedOutcome.entrySet().forEach(idAndOutcome -> {
				theTransactionDetails.addResolvedResourceId(
						idAndOutcome.getKey(), idAndOutcome.getValue().getPersistentId());
			});

			/*
			 * Perform ID substitutions and then index each resource we have saved
			 */

			resolveReferencesThenSaveAndIndexResources(
					theRequest,
					theTransactionDetails,
					theIdSubstitutions,
					theIdToPersistedOutcome,
					theTransactionStopWatch,
					entriesToProcess,
					nonUpdatedEntities,
					updatedEntities);

			theTransactionStopWatch.endCurrentTask();

			// flush writes to db
			theTransactionStopWatch.startTask("Flush writes to database");

			// flush the changes
			flushSession(theIdToPersistedOutcome);

			theTransactionStopWatch.endCurrentTask();

			/*
			 * Double check we didn't allow any duplicates we shouldn't have
			 */
			if (conditionalRequestUrls.size() > 0) {
				theTransactionStopWatch.startTask("Check for conflicts in conditional resources");
			}
			if (!myStorageSettings.isMassIngestionMode()) {
				validateNoDuplicates(
						theRequest,
						theTransactionDetails,
						theActionName,
						conditionalRequestUrls,
						theIdToPersistedOutcome.values());
			}

			theTransactionStopWatch.endCurrentTask();
			if (conditionalUrlToIdMap.size() > 0) {
				theTransactionStopWatch.startTask(
						"Check that all conditionally created/updated entities actually match their conditionals.");
			}

			if (!myStorageSettings.isMassIngestionMode()) {
				validateAllInsertsMatchTheirConditionalUrls(theIdToPersistedOutcome, conditionalUrlToIdMap, theRequest);
			}
			theTransactionStopWatch.endCurrentTask();

			for (IIdType next : theAllIds) {
				IIdType replacement = theIdSubstitutions.getForSource(next);
				if (replacement != null && !replacement.equals(next)) {
					ourLog.debug(
							"Placeholder resource ID \"{}\" was replaced with permanent ID \"{}\"", next, replacement);
				}
			}

			IInterceptorBroadcaster compositeBroadcaster =
					CompositeInterceptorBroadcaster.newCompositeBroadcaster(myInterceptorBroadcaster, theRequest);
			ListMultimap<Pointcut, HookParams> deferredBroadcastEvents =
					theTransactionDetails.endAcceptingDeferredInterceptorBroadcasts();
			for (Map.Entry<Pointcut, HookParams> nextEntry : deferredBroadcastEvents.entries()) {
				Pointcut nextPointcut = nextEntry.getKey();
				HookParams nextParams = nextEntry.getValue();
				compositeBroadcaster.callHooks(nextPointcut, nextParams);
			}

			DeferredInterceptorBroadcasts deferredInterceptorBroadcasts =
					new DeferredInterceptorBroadcasts(deferredBroadcastEvents);
			HookParams params = new HookParams()
					.add(RequestDetails.class, theRequest)
					.addIfMatchesType(ServletRequestDetails.class, theRequest)
					.add(DeferredInterceptorBroadcasts.class, deferredInterceptorBroadcasts)
					.add(TransactionDetails.class, theTransactionDetails)
					.add(IBaseBundle.class, theResponse);
			compositeBroadcaster.callHooks(Pointcut.STORAGE_TRANSACTION_PROCESSED, params);

			theTransactionDetails.deferredBroadcastProcessingFinished();

			// finishedCallingDeferredInterceptorBroadcasts

			return entriesToProcess;

		} finally {
			if (theTransactionDetails.isAcceptingDeferredInterceptorBroadcasts()) {
				theTransactionDetails.endAcceptingDeferredInterceptorBroadcasts();
			}
		}
	}

	/**
	 * Subclasses may override this in order to invoke specific operations when
	 * we're finished handling all the write entries in the transaction bundle
	 * with a given verb.
	 */
	protected void handleVerbChangeInTransactionWriteOperations() {
		// nothing
	}

	/**
	 * Implement to handle post transaction processing
	 */
	protected void postTransactionProcess(TransactionDetails theTransactionDetails) {
		// nothing
	}

	/**
	 * Check for if a resource id should be matched in a conditional update
	 * If the FHIR version is older than R4, it follows the old specifications and does not match
	 * If the resource id has been resolved, then it is an existing resource and does not need to be matched
	 * If the resource id is local or a placeholder, the id is temporary and should not be matched
	 */
	private boolean shouldConditionalUpdateMatchId(TransactionDetails theTransactionDetails, IIdType theId) {
		if (myContext.getVersion().getVersion().isOlderThan(FhirVersionEnum.R4)) {
			return false;
		}
		if (theTransactionDetails.hasResolvedResourceId(theId)
				&& !theTransactionDetails.isResolvedResourceIdEmpty(theId)) {
			return false;
		}
		if (theId != null && theId.getValue() != null) {
			return !(theId.getValue().startsWith("urn:") || theId.getValue().startsWith("#"));
		}
		return true;
	}

	private boolean shouldSwapBinaryToActualResource(
			IBaseResource theResource, String theResourceType, IIdType theNextResourceId) {
		if ("Binary".equalsIgnoreCase(theResourceType)
				&& theNextResourceId.getResourceType() != null
				&& !theNextResourceId.getResourceType().equalsIgnoreCase("Binary")) {
			return true;
		} else {
			return false;
		}
	}

	private void setConditionalUrlToBeValidatedLater(
			Map<String, IIdType> theConditionalUrlToIdMap, String theMatchUrl, IIdType theId) {
		if (!isBlank(theMatchUrl)) {
			theConditionalUrlToIdMap.put(theMatchUrl, theId);
		}
	}

	/**
	 * After transaction processing and resolution of indexes and references, we want to validate that the resources that were stored _actually_
	 * match the conditional URLs that they were brought in on.
	 */
	private void validateAllInsertsMatchTheirConditionalUrls(
			Map<IIdType, DaoMethodOutcome> theIdToPersistedOutcome,
			Map<String, IIdType> conditionalUrlToIdMap,
			RequestDetails theRequest) {
		conditionalUrlToIdMap.entrySet().stream()
				.filter(entry -> entry.getKey() != null)
				.forEach(entry -> {
					String matchUrl = entry.getKey();
					IIdType value = entry.getValue();
					DaoMethodOutcome daoMethodOutcome = theIdToPersistedOutcome.get(value);
					if (daoMethodOutcome != null
							&& !daoMethodOutcome.isNop()
							&& daoMethodOutcome.getResource() != null) {
						InMemoryMatchResult match =
								mySearchParamMatcher.match(matchUrl, daoMethodOutcome.getResource(), theRequest);
						if (ourLog.isDebugEnabled()) {
							ourLog.debug(
									"Checking conditional URL [{}] against resource with ID [{}]: Supported?:[{}], Matched?:[{}]",
									matchUrl,
									value,
									match.supported(),
									match.matched());
						}
						if (match.supported()) {
							if (!match.matched()) {
								throw new PreconditionFailedException(Msg.code(539) + "Invalid conditional URL \""
										+ matchUrl + "\". The given resource is not matched by this URL.");
							}
						}
					}
				});
	}

	/**
	 * Checks for any delete conflicts.
	 *
	 * @param theDeleteConflicts  - set of delete conflicts
	 * @param theDeletedResources - set of deleted resources
	 * @param theUpdatedResources - list of updated resources
	 */
	private void checkForDeleteConflicts(
			DeleteConflictList theDeleteConflicts,
			Set<String> theDeletedResources,
			List<IBaseResource> theUpdatedResources) {
		for (Iterator<DeleteConflict> iter = theDeleteConflicts.iterator(); iter.hasNext(); ) {
			DeleteConflict nextDeleteConflict = iter.next();

			/*
			 * If we have a conflict, it means we can't delete Resource/A because
			 * Resource/B has a reference to it. We'll ignore that conflict though
			 * if it turns out we're also deleting Resource/B in this transaction.
			 */
			if (theDeletedResources.contains(
					nextDeleteConflict.getSourceId().toUnqualifiedVersionless().getValue())) {
				iter.remove();
				continue;
			}

			/*
			 * And then, this is kind of a last ditch check. It's also ok to delete
			 * Resource/A if Resource/B isn't being deleted, but it is being UPDATED
			 * in this transaction, and the updated version of it has no references
			 * to Resource/A any more.
			 */
			String sourceId =
					nextDeleteConflict.getSourceId().toUnqualifiedVersionless().getValue();
			String targetId =
					nextDeleteConflict.getTargetId().toUnqualifiedVersionless().getValue();
			Optional<IBaseResource> updatedSource = theUpdatedResources.stream()
					.filter(t -> sourceId.equals(
							t.getIdElement().toUnqualifiedVersionless().getValue()))
					.findFirst();
			if (updatedSource.isPresent()) {
				List<ResourceReferenceInfo> referencesInSource =
						myContext.newTerser().getAllResourceReferences(updatedSource.get());
				boolean sourceStillReferencesTarget = referencesInSource.stream()
						.anyMatch(t -> targetId.equals(t.getResourceReference()
								.getReferenceElement()
								.toUnqualifiedVersionless()
								.getValue()));
				if (!sourceStillReferencesTarget) {
					iter.remove();
				}
			}
		}
		DeleteConflictUtil.validateDeleteConflictsEmptyOrThrowException(myContext, theDeleteConflicts);
	}

	/**
	 * This method replaces any placeholder references in the
	 * source transaction Bundle with their actual targets, then stores the resource contents and indexes
	 * in the database. This is trickier than you'd think because of a couple of possibilities during the
	 * save:
	 * * There may be resources that have not changed (e.g. an update/PUT with a resource body identical
	 * to what is already in the database)
	 * * There may be resources with auto-versioned references, meaning we're replacing certain references
	 * in the resource with a versioned references, referencing the current version at the time of the
	 * transaction processing
	 * * There may by auto-versioned references pointing to these unchanged targets
	 * <p>
	 * If we're not doing any auto-versioned references, we'll just iterate through all resources in the
	 * transaction and save them one at a time.
	 * <p>
	 * However, if we have any auto-versioned references we do this in 2 passes: First the resources from the
	 * transaction that don't have any auto-versioned references are stored. We do them first since there's
	 * a chance they may be a NOP and we'll need to account for their version number not actually changing.
	 * Then we do a second pass for any resources that have auto-versioned references. These happen in a separate
	 * pass because it's too complex to try and insert the auto-versioned references and still
	 * account for NOPs, so we block NOPs in that pass.
	 */
	private void resolveReferencesThenSaveAndIndexResources(
			RequestDetails theRequest,
			TransactionDetails theTransactionDetails,
			IdSubstitutionMap theIdSubstitutions,
			Map<IIdType, DaoMethodOutcome> theIdToPersistedOutcome,
			StopWatch theTransactionStopWatch,
			EntriesToProcessMap theEntriesToProcess,
			Set<IIdType> theNonUpdatedEntities,
			Set<IBasePersistedResource> theUpdatedEntities) {
		FhirTerser terser = myContext.newTerser();
		theTransactionStopWatch.startTask("Index " + theIdToPersistedOutcome.size() + " resources");
		IdentityHashMap<DaoMethodOutcome, Set<IBaseReference>> deferredIndexesForAutoVersioning = null;
		int i = 0;
		for (DaoMethodOutcome nextOutcome : theIdToPersistedOutcome.values()) {

			if (i++ % 250 == 0) {
				ourLog.debug(
						"Have indexed {} entities out of {} in transaction",
						i,
						theIdToPersistedOutcome.values().size());
			}

			if (nextOutcome.isNop()) {
				continue;
			}

			IBaseResource nextResource = nextOutcome.getResource();
			if (nextResource == null) {
				continue;
			}

			Set<IBaseReference> referencesToAutoVersion =
					BaseStorageDao.extractReferencesToAutoVersion(myContext, myStorageSettings, nextResource);
			Set<IBaseReference> referencesToKeepClientSuppliedVersion =
					BaseStorageDao.extractReferencesToAvoidReplacement(myContext, nextResource);

			if (referencesToAutoVersion.isEmpty()) {
				// no references to autoversion - we can do the resolve and save now
				resolveReferencesThenSaveAndIndexResource(
						theRequest,
						theTransactionDetails,
						theIdSubstitutions,
						theIdToPersistedOutcome,
						theEntriesToProcess,
						theNonUpdatedEntities,
						theUpdatedEntities,
						terser,
						nextOutcome,
						nextResource,
						referencesToAutoVersion, // this is empty
						referencesToKeepClientSuppliedVersion);
			} else {
				// we have autoversioned things to defer until later
				if (deferredIndexesForAutoVersioning == null) {
					deferredIndexesForAutoVersioning = new IdentityHashMap<>();
				}
				deferredIndexesForAutoVersioning.put(nextOutcome, referencesToAutoVersion);
			}
		}

		// If we have any resources we'll be auto-versioning, index these next
		if (deferredIndexesForAutoVersioning != null) {
			for (Map.Entry<DaoMethodOutcome, Set<IBaseReference>> nextEntry :
					deferredIndexesForAutoVersioning.entrySet()) {
				DaoMethodOutcome nextOutcome = nextEntry.getKey();
				Set<IBaseReference> referencesToAutoVersion = nextEntry.getValue();
				IBaseResource nextResource = nextOutcome.getResource();
				Set<IBaseReference> referencesToKeepClientSuppliedVersion =
						BaseStorageDao.extractReferencesToAvoidReplacement(myContext, nextResource);

				resolveReferencesThenSaveAndIndexResource(
						theRequest,
						theTransactionDetails,
						theIdSubstitutions,
						theIdToPersistedOutcome,
						theEntriesToProcess,
						theNonUpdatedEntities,
						theUpdatedEntities,
						terser,
						nextOutcome,
						nextResource,
						referencesToAutoVersion,
						referencesToKeepClientSuppliedVersion);
			}
		}
	}

	private void resolveReferencesThenSaveAndIndexResource(
			RequestDetails theRequest,
			TransactionDetails theTransactionDetails,
			IdSubstitutionMap theIdSubstitutions,
			Map<IIdType, DaoMethodOutcome> theIdToPersistedOutcome,
			EntriesToProcessMap theEntriesToProcess,
			Set<IIdType> theNonUpdatedEntities,
			Set<IBasePersistedResource> theUpdatedEntities,
			FhirTerser theTerser,
			DaoMethodOutcome theDaoMethodOutcome,
			IBaseResource theResource,
			Set<IBaseReference> theReferencesToAutoVersion,
			Set<IBaseReference> theReferencesToKeepClientSuppliedVersion) {
		// References
		List<ResourceReferenceInfo> allRefs = theTerser.getAllResourceReferences(theResource);
		for (ResourceReferenceInfo nextRef : allRefs) {
			IBaseReference resourceReference = nextRef.getResourceReference();
			IIdType nextId = resourceReference.getReferenceElement();
			IIdType newId = null;
			if (!nextId.hasIdPart()) {
				if (resourceReference.getResource() != null) {
					IIdType targetId = resourceReference.getResource().getIdElement();
					if (targetId.getValue() == null) {
						// This means it's a contained resource
						continue;
					} else if (theIdSubstitutions.containsTarget(targetId)) {
						newId = targetId;
					} else {
						throw new InternalErrorException(Msg.code(540)
								+ "References by resource with no reference ID are not supported in DAO layer");
					}
				} else {
					continue;
				}
			}
			if (newId != null || theIdSubstitutions.containsSource(nextId)) {
				if (shouldReplaceResourceReference(
						theReferencesToAutoVersion, theReferencesToKeepClientSuppliedVersion, resourceReference)) {
					if (newId == null) {
						newId = theIdSubstitutions.getForSource(nextId);
					}
					if (newId != null) {
						ourLog.debug(" * Replacing resource ref {} with {}", nextId, newId);
						if (theReferencesToAutoVersion.contains(resourceReference)) {
							replaceResourceReference(newId, resourceReference, theTransactionDetails);
						} else {
							replaceResourceReference(newId.toVersionless(), resourceReference, theTransactionDetails);
						}
					}
				}
			} else if (nextId.getValue().startsWith("urn:")) {
				throw new InvalidRequestException(
						Msg.code(541) + "Unable to satisfy placeholder ID " + nextId.getValue()
								+ " found in element named '" + nextRef.getName() + "' within resource of type: "
								+ theResource.getIdElement().getResourceType());
			} else {
				// get a map of
				// existing ids -> PID (for resources that exist in the DB)
				// should this be allPartitions?
				ResourcePersistentIdMap resourceVersionMap = myResourceVersionSvc.getLatestVersionIdsForResourceIds(
						RequestPartitionId.allPartitions(),
						theReferencesToAutoVersion.stream()
								.map(IBaseReference::getReferenceElement)
								.collect(Collectors.toList()));

				for (IBaseReference baseRef : theReferencesToAutoVersion) {
					IIdType id = baseRef.getReferenceElement();
					if (!resourceVersionMap.containsKey(id)
							&& myStorageSettings.isAutoCreatePlaceholderReferenceTargets()) {
						// not in the db, but autocreateplaceholders is true
						// so the version we'll set is "1" (since it will be
						// created later)
						String newRef = id.withVersion("1").getValue();
						id.setValue(newRef);
					} else {
						// we will add the looked up info to the transaction
						// for later
						if (resourceVersionMap.containsKey(id)) {
							theTransactionDetails.addResolvedResourceId(
									id, resourceVersionMap.getResourcePersistentId(id));
						}
					}
				}

				if (theReferencesToAutoVersion.contains(resourceReference)) {
					DaoMethodOutcome outcome = theIdToPersistedOutcome.get(nextId);

					if (outcome != null && !outcome.isNop() && !Boolean.TRUE.equals(outcome.getCreated())) {
						replaceResourceReference(nextId, resourceReference, theTransactionDetails);
					}

					// if referenced resource is not in transaction but exists in the DB, resolving its version
					IResourcePersistentId persistedReferenceId = resourceVersionMap.getResourcePersistentId(nextId);
					if (outcome == null && persistedReferenceId != null && persistedReferenceId.getVersion() != null) {
						IIdType newReferenceId = nextId.withVersion(
								persistedReferenceId.getVersion().toString());
						replaceResourceReference(newReferenceId, resourceReference, theTransactionDetails);
					}
				}
			}
		}

		// URIs
		Class<? extends IPrimitiveType<?>> uriType = (Class<? extends IPrimitiveType<?>>)
				myContext.getElementDefinition("uri").getImplementingClass();
		List<? extends IPrimitiveType<?>> allUris = theTerser.getAllPopulatedChildElementsOfType(theResource, uriType);
		for (IPrimitiveType<?> nextRef : allUris) {
			if (nextRef instanceof IIdType) {
				continue; // No substitution on the resource ID itself!
			}
			String nextUriString = nextRef.getValueAsString();
			if (isNotBlank(nextUriString)) {
				if (theIdSubstitutions.containsSource(nextUriString)) {
					IIdType newId = theIdSubstitutions.getForSource(nextUriString);
					ourLog.debug(" * Replacing resource ref {} with {}", nextUriString, newId);

					String existingValue = nextRef.getValueAsString();
					theTransactionDetails.addRollbackUndoAction(() -> nextRef.setValueAsString(existingValue));

					nextRef.setValueAsString(newId.toVersionless().getValue());
				} else {
					ourLog.debug(" * Reference [{}] does not exist in bundle", nextUriString);
				}
			}
		}

		IPrimitiveType<Date> deletedInstantOrNull = ResourceMetadataKeyEnum.DELETED_AT.get(theResource);
		Date deletedTimestampOrNull = deletedInstantOrNull != null ? deletedInstantOrNull.getValue() : null;

		IFhirResourceDao<? extends IBaseResource> dao = myDaoRegistry.getResourceDao(theResource.getClass());
		IJpaDao jpaDao = (IJpaDao) dao;

		IBasePersistedResource updateOutcome = null;
		if (theUpdatedEntities.contains(theDaoMethodOutcome.getEntity())) {
			boolean forceUpdateVersion = !theReferencesToAutoVersion.isEmpty();
			String matchUrl = theDaoMethodOutcome.getMatchUrl();
			RestOperationTypeEnum operationType = theDaoMethodOutcome.getOperationType();
			DaoMethodOutcome daoMethodOutcome = jpaDao.updateInternal(
					theRequest,
					theResource,
					matchUrl,
					true,
					forceUpdateVersion,
					theDaoMethodOutcome.getEntity(),
					theResource.getIdElement(),
					theDaoMethodOutcome.getPreviousResource(),
					operationType,
					theTransactionDetails);
			updateOutcome = daoMethodOutcome.getEntity();
			theDaoMethodOutcome = daoMethodOutcome;
		} else if (!theNonUpdatedEntities.contains(theDaoMethodOutcome.getId())) {
			updateOutcome = jpaDao.updateEntity(
					theRequest,
					theResource,
					theDaoMethodOutcome.getEntity(),
					deletedTimestampOrNull,
					true,
					false,
					theTransactionDetails,
					false,
					true);
		}

		// Make sure we reflect the actual final version for the resource.
		if (updateOutcome != null) {
			IIdType newId = updateOutcome.getIdDt();

			IIdType entryId = theEntriesToProcess.getIdWithVersionlessComparison(newId);
			if (entryId != null && !StringUtils.equals(entryId.getValue(), newId.getValue())) {
				entryId.setValue(newId.getValue());
			}

			theDaoMethodOutcome.setId(newId);

			theIdSubstitutions.updateTargets(newId);

			// This will only be null if we're not intending to return an OO
			IBaseOperationOutcome operationOutcome = theDaoMethodOutcome.getOperationOutcome();
			if (operationOutcome != null) {

				List<IIdType> autoCreatedPlaceholders =
						theTransactionDetails.getAutoCreatedPlaceholderResourcesAndClear();
				for (IIdType autoCreatedPlaceholder : autoCreatedPlaceholders) {
					BaseStorageDao.addIssueToOperationOutcomeForAutoCreatedPlaceholder(
							myContext, autoCreatedPlaceholder, operationOutcome);
				}

				IBase responseEntry = theEntriesToProcess.getResponseBundleEntryWithVersionlessComparison(newId);
				myVersionAdapter.setResponseOutcome(responseEntry, operationOutcome);
			}
		}
	}

	/**
	 * We should replace the references when
	 * 1. It is not a reference we should keep the client-supplied version for as configured by `DontStripVersionsFromReferences` or
	 * 2. It is a reference that has been identified for auto versioning or
	 * 3. Is a placeholder reference
	 *
	 * @param theReferencesToAutoVersion               list of references identified for auto versioning
	 * @param theReferencesToKeepClientSuppliedVersion list of references that we should not strip the version for
	 * @param theResourceReference                     the resource reference
	 * @return true if we should replace the resource reference, false if we should keep the client provided reference
	 */
	private boolean shouldReplaceResourceReference(
			Set<IBaseReference> theReferencesToAutoVersion,
			Set<IBaseReference> theReferencesToKeepClientSuppliedVersion,
			IBaseReference theResourceReference) {
		return (!theReferencesToKeepClientSuppliedVersion.contains(theResourceReference)
						&& myContext.getParserOptions().isStripVersionsFromReferences())
				|| theReferencesToAutoVersion.contains(theResourceReference)
				|| isPlaceholder(theResourceReference.getReferenceElement());
	}

	private void replaceResourceReference(
			IIdType theReferenceId, IBaseReference theResourceReference, TransactionDetails theTransactionDetails) {
		addRollbackReferenceRestore(theTransactionDetails, theResourceReference);
		theResourceReference.setReference(theReferenceId.getValue());
		theResourceReference.setResource(null);
	}

	private void addRollbackReferenceRestore(
			TransactionDetails theTransactionDetails, IBaseReference resourceReference) {
		String existingValue = resourceReference.getReferenceElement().getValue();
		theTransactionDetails.addRollbackUndoAction(() -> resourceReference.setReference(existingValue));
	}

	private void validateNoDuplicates(
			RequestDetails theRequest,
			TransactionDetails theTransactionDetails,
			String theActionName,
			Map<String, Class<? extends IBaseResource>> conditionalRequestUrls,
			Collection<DaoMethodOutcome> thePersistedOutcomes) {

		Map<ResourceTable, ResourceIndexedSearchParams> existingSearchParams =
				theTransactionDetails.getOrCreateUserData(
						HapiTransactionService.XACT_USERDATA_KEY_EXISTING_SEARCH_PARAMS, Collections::emptyMap);

		IdentityHashMap<IBaseResource, ResourceIndexedSearchParams> resourceToIndexedParams =
				new IdentityHashMap<>(thePersistedOutcomes.size());
		thePersistedOutcomes.stream()
				.filter(t -> !t.isNop())
				// N.B. GGG: This validation never occurs for mongo, as nothing is a ResourceTable.
				.filter(t -> t.getEntity() instanceof ResourceTable)
				.filter(t -> t.getEntity().getDeleted() == null)
				.filter(t -> t.getResource() != null)
				.forEach(t -> {
					ResourceTable entity = (ResourceTable) t.getEntity();
					ResourceIndexedSearchParams params = existingSearchParams.get(entity);
					if (params == null) {
						params = ResourceIndexedSearchParams.withLists(entity);
					}
					resourceToIndexedParams.put(t.getResource(), params);
				});

		for (Map.Entry<String, Class<? extends IBaseResource>> nextEntry : conditionalRequestUrls.entrySet()) {
			String matchUrl = nextEntry.getKey();
			if (isNotBlank(matchUrl)) {
				if (matchUrl.startsWith("?")
						|| (!matchUrl.contains("?")
								&& UNQUALIFIED_MATCH_URL_START.matcher(matchUrl).find())) {
					StringBuilder b = new StringBuilder();
					b.append(myContext.getResourceType(nextEntry.getValue()));
					if (!matchUrl.startsWith("?")) {
						b.append("?");
					}
					b.append(matchUrl);
					matchUrl = b.toString();
				}

				if (!myInMemoryResourceMatcher.canBeEvaluatedInMemory(matchUrl).supported()) {
					continue;
				}

				int counter = 0;
				for (Map.Entry<IBaseResource, ResourceIndexedSearchParams> entries :
						resourceToIndexedParams.entrySet()) {
					ResourceIndexedSearchParams indexedParams = entries.getValue();
					IBaseResource resource = entries.getKey();

					String resourceType = myContext.getResourceType(resource);
					if (!matchUrl.startsWith(resourceType + "?")) {
						continue;
					}

					if (myInMemoryResourceMatcher
							.match(matchUrl, resource, indexedParams, theRequest)
							.matched()) {
						counter++;
						if (counter > 1) {
							throw new InvalidRequestException(Msg.code(542) + "Unable to process " + theActionName
									+ " - Request would cause multiple resources to match URL: \"" + matchUrl
									+ "\". Does transaction request contain duplicates?");
						}
					}
				}
			}
		}
	}

	protected abstract void flushSession(Map<IIdType, DaoMethodOutcome> theIdToPersistedOutcome);

	private void validateResourcePresent(IBaseResource theResource, Integer theOrder, String theVerb) {
		if (theResource == null) {
			String msg = myContext
					.getLocalizer()
					.getMessage(BaseTransactionProcessor.class, "missingMandatoryResource", theVerb, theOrder);
			throw new InvalidRequestException(Msg.code(543) + msg);
		}
	}

	private IIdType newIdType(String theResourceType, String theResourceId, String theVersion) {
		IdType id = new IdType(theResourceType, theResourceId, theVersion);
		return myContext.getVersion().newIdType().setValue(id.getValue());
	}

	private IIdType newIdType(String theToResourceName, String theIdPart) {
		return newIdType(theToResourceName, theIdPart, null);
	}

	@VisibleForTesting
	public void setDaoRegistry(DaoRegistry theDaoRegistry) {
		myDaoRegistry = theDaoRegistry;
	}

	private IFhirResourceDao getDaoOrThrowException(Class<? extends IBaseResource> theClass) {
		IFhirResourceDao<? extends IBaseResource> dao = myDaoRegistry.getResourceDaoOrNull(theClass);
		if (dao == null) {
			Set<String> types = new TreeSet<>(myDaoRegistry.getRegisteredDaoTypes());
			String type = myContext.getResourceType(theClass);
			String msg = myContext
					.getLocalizer()
					.getMessage(BaseTransactionProcessor.class, "unsupportedResourceType", type, types.toString());
			throw new InvalidRequestException(Msg.code(544) + msg);
		}
		return dao;
	}

	private String toResourceName(Class<? extends IBaseResource> theResourceType) {
		return myContext.getResourceType(theResourceType);
	}

	public void setContext(FhirContext theContext) {
		myContext = theContext;
	}

	/**
	 * Extracts the transaction url from the entry and verifies it's:
	 * <li>not null or blank (unless it is a POST), and</li>
	 * <li>is a relative url matching the resourceType it is about</li>
	 * <p>
	 * For POST requests, the url is allowed to be blank to preserve the existing behavior.
	 * <p>
	 * Returns the transaction url (or throws an InvalidRequestException if url is missing or not valid)
	 */
	private String extractAndVerifyTransactionUrlForEntry(IBase theEntry, String theVerb) {
		String url = extractTransactionUrlOrThrowException(theEntry, theVerb);
		if (url.isEmpty() || isValidResourceTypeUrl(url)) {
			// for POST requests, the url is allowed to be blank, which is checked in
			// extractTransactionUrlOrThrowException and an empty string is returned in that case.
			// That is why empty url is allowed here.
			return url;
		}

		ourLog.debug("Invalid url. Should begin with a resource type: {}", url);
		String msg = myContext.getLocalizer().getMessage(BaseStorageDao.class, "transactionInvalidUrl", theVerb, url);
		throw new InvalidRequestException(Msg.code(2006) + msg);
	}

	/**
	 * Returns true if the provided url is a valid entry request.url.
	 * <p>
	 * This means:
	 * a) not an absolute url (does not start with http/https)
	 * b) starts with either a ResourceType or /ResourceType
	 */
	private boolean isValidResourceTypeUrl(@Nonnull String theUrl) {
		if (UrlUtil.isAbsolute(theUrl)) {
			return false;
		} else {
			int queryStringIndex = theUrl.indexOf("?");
			String url;
			if (queryStringIndex > 0) {
				url = theUrl.substring(0, theUrl.indexOf("?"));
			} else {
				url = theUrl;
			}
			String[] parts;
			if (url.startsWith("/")) {
				parts = url.substring(1).split("/");
			} else {
				parts = url.split("/");
			}
			Set<String> allResourceTypes = myContext.getResourceTypes();

			return allResourceTypes.contains(parts[0]);
		}
	}

	/**
	 * Extracts the transaction url from the entry and verifies that it is not null/blank, unless it is a POST,
	 * and returns it. For POST requests allows null or blank values to keep the existing behaviour and returns
	 * an empty string in that case.
	 */
	private String extractTransactionUrlOrThrowException(IBase nextEntry, String verb) {
		String url = myVersionAdapter.getEntryRequestUrl(nextEntry);
		if ("POST".equals(verb) && isBlank(url)) {
			// allow blanks for POST to keep the existing behaviour
			// returning empty string instead of null to not get NPE later
			return "";
		}

		if (isBlank(url)) {
			throw new InvalidRequestException(Msg.code(545)
					+ myContext.getLocalizer().getMessage(BaseStorageDao.class, "transactionMissingUrl", verb));
		}
		return url;
	}

	private IFhirResourceDao<? extends IBaseResource> toDao(UrlUtil.UrlParts theParts, String theVerb, String theUrl) {
		RuntimeResourceDefinition resType;
		try {
			resType = myContext.getResourceDefinition(theParts.getResourceType());
		} catch (DataFormatException e) {
			String msg =
					myContext.getLocalizer().getMessage(BaseStorageDao.class, "transactionInvalidUrl", theVerb, theUrl);
			throw new InvalidRequestException(Msg.code(546) + msg);
		}
		IFhirResourceDao<? extends IBaseResource> dao = null;
		if (resType != null) {
			dao = myDaoRegistry.getResourceDao(resType.getImplementingClass());
		}
		if (dao == null) {
			String msg =
					myContext.getLocalizer().getMessage(BaseStorageDao.class, "transactionInvalidUrl", theVerb, theUrl);
			throw new InvalidRequestException(Msg.code(547) + msg);
		}

		return dao;
	}

	private String toMatchUrl(IBase theEntry) {
		String verb = myVersionAdapter.getEntryRequestVerb(myContext, theEntry);
		switch (defaultString(verb)) {
			case POST:
				return myVersionAdapter.getEntryIfNoneExist(theEntry);
			case PUT:
			case DELETE:
			case PATCH:
				String url = extractTransactionUrlOrThrowException(theEntry, verb);
				UrlUtil.UrlParts parts = UrlUtil.parseUrl(url);
				if (isBlank(parts.getResourceId())) {
					return parts.getResourceType() + '?' + parts.getParams();
				}
				return null;
			default:
				return null;
		}
	}

	@VisibleForTesting
	public void setPartitionSettingsForUnitTest(PartitionSettings thePartitionSettings) {
		myPartitionSettings = thePartitionSettings;
	}

	/**
	 * Transaction Order, per the spec:
	 * <p>
	 * Process any DELETE interactions
	 * Process any POST interactions
	 * Process any PUT interactions
	 * Process any PATCH interactions
	 * Process any GET interactions
	 */
	// @formatter:off
	public class TransactionSorter implements Comparator<IBase> {

		private final Set<String> myPlaceholderIds;

		public TransactionSorter(Set<String> thePlaceholderIds) {
			myPlaceholderIds = thePlaceholderIds;
		}

		@Override
		public int compare(IBase theO1, IBase theO2) {
			int o1 = toOrder(theO1);
			int o2 = toOrder(theO2);

			if (o1 == o2) {
				String matchUrl1 = toMatchUrl(theO1);
				String matchUrl2 = toMatchUrl(theO2);
				if (isBlank(matchUrl1) && isBlank(matchUrl2)) {
					return 0;
				}
				if (isBlank(matchUrl1)) {
					return -1;
				}
				if (isBlank(matchUrl2)) {
					return 1;
				}

				boolean match1containsSubstitutions = false;
				boolean match2containsSubstitutions = false;
				for (String nextPlaceholder : myPlaceholderIds) {
					if (matchUrl1.contains(nextPlaceholder)) {
						match1containsSubstitutions = true;
					}
					if (matchUrl2.contains(nextPlaceholder)) {
						match2containsSubstitutions = true;
					}
				}

				if (match1containsSubstitutions && match2containsSubstitutions) {
					return 0;
				}
				if (!match1containsSubstitutions && !match2containsSubstitutions) {
					return 0;
				}
				if (match1containsSubstitutions) {
					return 1;
				} else {
					return -1;
				}
			}

			return o1 - o2;
		}

		private int toOrder(IBase theO1) {
			int o1 = 0;
			if (myVersionAdapter.getEntryRequestVerb(myContext, theO1) != null) {
				switch (myVersionAdapter.getEntryRequestVerb(myContext, theO1)) {
					case "DELETE":
						o1 = 1;
						break;
					case "POST":
						o1 = 2;
						break;
					case "PUT":
						o1 = 3;
						break;
					case "PATCH":
						o1 = 4;
						break;
					case "GET":
						o1 = 5;
						break;
					default:
						o1 = 0;
						break;
				}
			}
			return o1;
		}
	}

	public class RetriableBundleTask implements Runnable {

		private final CountDownLatch myCompletedLatch;
		private final RequestDetails myRequestDetails;
		private final IBase myNextReqEntry;
		private final Map<Integer, Object> myResponseMap;
		private final int myResponseOrder;
		private final boolean myNestedMode;
		private BaseServerResponseException myLastSeenException;

		protected RetriableBundleTask(
				CountDownLatch theCompletedLatch,
				RequestDetails theRequestDetails,
				Map<Integer, Object> theResponseMap,
				int theResponseOrder,
				IBase theNextReqEntry,
				boolean theNestedMode) {
			this.myCompletedLatch = theCompletedLatch;
			this.myRequestDetails = theRequestDetails;
			this.myNextReqEntry = theNextReqEntry;
			this.myResponseMap = theResponseMap;
			this.myResponseOrder = theResponseOrder;
			this.myNestedMode = theNestedMode;
			this.myLastSeenException = null;
		}

		private void processBatchEntry() {
			IBaseBundle subRequestBundle =
					myVersionAdapter.createBundle(org.hl7.fhir.r4.model.Bundle.BundleType.TRANSACTION.toCode());
			myVersionAdapter.addEntry(subRequestBundle, myNextReqEntry);

			IBaseBundle nextResponseBundle = processTransactionAsSubRequest(
					myRequestDetails, subRequestBundle, "Batch sub-request", myNestedMode);

			IBase subResponseEntry =
					(IBase) myVersionAdapter.getEntries(nextResponseBundle).get(0);
			myResponseMap.put(myResponseOrder, subResponseEntry);

			/*
			 * If the individual entry didn't have a resource in its response, bring the sub-transaction's OperationOutcome across so the client can see it
			 */
			if (myVersionAdapter.getResource(subResponseEntry) == null) {
				IBase nextResponseBundleFirstEntry =
						(IBase) myVersionAdapter.getEntries(nextResponseBundle).get(0);
				myResponseMap.put(myResponseOrder, nextResponseBundleFirstEntry);
			}
		}

		private boolean processBatchEntryWithRetry() {
			int maxAttempts = 3;
			for (int attempt = 1; ; attempt++) {
				try {
					processBatchEntry();
					return true;
				} catch (BaseServerResponseException e) {
					// If we catch a known and structured exception from HAPI, just fail.
					myLastSeenException = e;
					return false;
				} catch (Throwable t) {
					myLastSeenException = new InternalErrorException(t);
					// If we have caught a non-tag-storage failure we are unfamiliar with, or we have exceeded max
					// attempts, exit.
					if (!DaoFailureUtil.isTagStorageFailure(t) || attempt >= maxAttempts) {
						ourLog.error("Failure during BATCH sub transaction processing", t);
						return false;
					}
				}
			}
		}

		@Override
		public void run() {
			boolean success = processBatchEntryWithRetry();
			if (!success) {
				populateResponseMapWithLastSeenException();
			}

			// checking for the parallelism
			ourLog.debug("processing batch for {} is completed", myVersionAdapter.getEntryRequestUrl(myNextReqEntry));
			myCompletedLatch.countDown();
		}

		private void populateResponseMapWithLastSeenException() {
			ServerResponseExceptionHolder caughtEx = new ServerResponseExceptionHolder();
			caughtEx.setException(myLastSeenException);
			myResponseMap.put(myResponseOrder, caughtEx);
		}
	}

	private static class ServerResponseExceptionHolder {
		private BaseServerResponseException myException;

		public BaseServerResponseException getException() {
			return myException;
		}

		public void setException(BaseServerResponseException myException) {
			this.myException = myException;
		}
	}

	public static boolean isPlaceholder(IIdType theId) {
		if (theId != null && theId.getValue() != null) {
			return theId.getValue().startsWith("urn:oid:") || theId.getValue().startsWith("urn:uuid:");
		}
		return false;
	}

	private static String toStatusString(int theStatusCode) {
		return theStatusCode + " " + defaultString(Constants.HTTP_STATUS_NAMES.get(theStatusCode));
	}

	/**
	 * Given a match URL containing
	 *
	 * @param theIdSubstitutions
	 * @param theMatchUrl
	 * @return
	 */
	public static String performIdSubstitutionsInMatchUrl(IdSubstitutionMap theIdSubstitutions, String theMatchUrl) {
		String matchUrl = theMatchUrl;
		if (isNotBlank(matchUrl) && !theIdSubstitutions.isEmpty()) {
			int startIdx = 0;
			while (startIdx != -1) {

				int endIdx = matchUrl.indexOf('&', startIdx + 1);
				if (endIdx == -1) {
					endIdx = matchUrl.length();
				}

				int equalsIdx = matchUrl.indexOf('=', startIdx + 1);

				int searchFrom;
				if (equalsIdx == -1) {
					searchFrom = matchUrl.length();
				} else if (equalsIdx >= endIdx) {
					// First equals we found is from a subsequent parameter
					searchFrom = matchUrl.length();
				} else {
					String paramValue = matchUrl.substring(equalsIdx + 1, endIdx);
					boolean isUrn = isUrn(paramValue);
					boolean isUrnEscaped = !isUrn && isUrnEscaped(paramValue);
					if (isUrn || isUrnEscaped) {
						if (isUrnEscaped) {
							paramValue = UrlUtil.unescape(paramValue);
						}
						IIdType replacement = theIdSubstitutions.getForSource(paramValue);
						if (replacement != null) {
							String replacementValue;
							if (replacement.hasVersionIdPart()) {
								replacementValue = replacement.toVersionless().getValue();
							} else {
								replacementValue = replacement.getValue();
							}
							matchUrl = matchUrl.substring(0, equalsIdx + 1)
									+ replacementValue
									+ matchUrl.substring(endIdx);
							searchFrom = equalsIdx + 1 + replacementValue.length();
						} else {
							searchFrom = endIdx;
						}
					} else {
						searchFrom = endIdx;
					}
				}

				if (searchFrom >= matchUrl.length()) {
					break;
				}

				startIdx = matchUrl.indexOf('&', searchFrom);
			}
		}
		return matchUrl;
	}

	private static boolean isUrn(@Nonnull String theId) {
		return theId.startsWith(URN_PREFIX);
	}

	private static boolean isUrnEscaped(@Nonnull String theId) {
		return theId.startsWith(URN_PREFIX_ESCAPED);
	}
}
