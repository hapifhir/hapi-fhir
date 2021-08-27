package ca.uhn.fhir.jpa.dao;

/*-
 * #%L
 * HAPI FHIR JPA Server
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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.interceptor.model.TransactionWriteOperationsDetails;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.dao.IJpaDao;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.api.model.DeleteConflict;
import ca.uhn.fhir.jpa.api.model.DeleteConflictList;
import ca.uhn.fhir.jpa.api.model.DeleteMethodOutcome;
import ca.uhn.fhir.jpa.dao.tx.HapiTransactionService;
import ca.uhn.fhir.jpa.delete.DeleteConflictService;
import ca.uhn.fhir.jpa.model.cross.IBasePersistedResource;
import ca.uhn.fhir.jpa.model.entity.ModelConfig;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.model.search.StorageProcessingMessage;
import ca.uhn.fhir.jpa.searchparam.extractor.ResourceIndexedSearchParams;
import ca.uhn.fhir.jpa.searchparam.matcher.InMemoryResourceMatcher;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.PatchTypeEnum;
import ca.uhn.fhir.rest.api.PreferReturnEnum;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.DeferredInterceptorBroadcasts;
import ca.uhn.fhir.rest.api.server.storage.TransactionDetails;
import ca.uhn.fhir.rest.param.ParameterUtil;
import ca.uhn.fhir.rest.server.RestfulServerUtils;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.MethodNotAllowedException;
import ca.uhn.fhir.rest.server.exceptions.NotModifiedException;
import ca.uhn.fhir.rest.server.exceptions.PayloadTooLargeException;
import ca.uhn.fhir.rest.server.interceptor.IServerInterceptor;
import ca.uhn.fhir.rest.server.method.BaseMethodBinding;
import ca.uhn.fhir.rest.server.method.BaseResourceReturningMethodBinding;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.rest.server.servlet.ServletSubRequestDetails;
import ca.uhn.fhir.rest.server.util.CompositeInterceptorBroadcaster;
import ca.uhn.fhir.rest.server.util.ServletRequestUtil;
import ca.uhn.fhir.util.ElementUtil;
import ca.uhn.fhir.util.FhirTerser;
import ca.uhn.fhir.util.ResourceReferenceInfo;
import ca.uhn.fhir.util.StopWatch;
import ca.uhn.fhir.util.AsyncUtil;
import ca.uhn.fhir.util.UrlUtil;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseBinary;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import static ca.uhn.fhir.util.StringUtil.toUtf8String;
import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public abstract class BaseTransactionProcessor {

	public static final String URN_PREFIX = "urn:";
	public static final Pattern UNQUALIFIED_MATCH_URL_START = Pattern.compile("^[a-zA-Z0-9_]+=");
	private static final Logger ourLog = LoggerFactory.getLogger(TransactionProcessor.class);
	private BaseHapiFhirDao myDao;
	@Autowired
	private PlatformTransactionManager myTxManager;
	@Autowired
	private FhirContext myContext;
	@Autowired
	private ITransactionProcessorVersionAdapter myVersionAdapter;
	@Autowired
	private DaoRegistry myDaoRegistry;
	@Autowired
	private IInterceptorBroadcaster myInterceptorBroadcaster;
	@Autowired
	private MatchResourceUrlService myMatchResourceUrlService;
	@Autowired
	private HapiTransactionService myHapiTransactionService;
	@Autowired
	private DaoConfig myDaoConfig;
	@Autowired
	private ModelConfig myModelConfig;
	@Autowired
	private InMemoryResourceMatcher myInMemoryResourceMatcher;

	private ThreadPoolTaskExecutor myExecutor ;

	@VisibleForTesting
	public void setDaoConfig(DaoConfig theDaoConfig) {
		myDaoConfig = theDaoConfig;
	}

	public ITransactionProcessorVersionAdapter getVersionAdapter() {
		return myVersionAdapter;
	}

	@VisibleForTesting
	public void setVersionAdapter(ITransactionProcessorVersionAdapter theVersionAdapter) {
		myVersionAdapter = theVersionAdapter;
	}

	@PostConstruct
	public void start() {
		ourLog.trace("Starting transaction processor");
		myExecutor = new ThreadPoolTaskExecutor();
		myExecutor.setThreadNamePrefix("bundle_batch_");
		// For single thread set the value to 1
		//myExecutor.setCorePoolSize(1);
		//myExecutor.setMaxPoolSize(1);
		myExecutor.setCorePoolSize(myDaoConfig.getBundleBatchPoolSize());
		myExecutor.setMaxPoolSize(myDaoConfig.getBundleBatchMaxPoolSize());
		myExecutor.setQueueCapacity(DaoConfig.DEFAULT_BUNDLE_BATCH_QUEUE_CAPACITY);

		myExecutor.initialize();
	}

	public <BUNDLE extends IBaseBundle> BUNDLE transaction(RequestDetails theRequestDetails, BUNDLE theRequest, boolean theNestedMode) {
		if (theRequestDetails != null && theRequestDetails.getServer() != null && myDao != null) {
			IServerInterceptor.ActionRequestDetails requestDetails = new IServerInterceptor.ActionRequestDetails(theRequestDetails, theRequest, "Bundle", null);
			myDao.notifyInterceptors(RestOperationTypeEnum.TRANSACTION, requestDetails);
		}

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
			throw new InvalidRequestException("Can not process collection Bundle of type: " + transactionType);
		}

		ourLog.info("Beginning storing collection with {} resources", myVersionAdapter.getEntries(theRequest).size());

		TransactionTemplate txTemplate = new TransactionTemplate(myTxManager);
		txTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);

		IBaseBundle resp = myVersionAdapter.createBundle(org.hl7.fhir.r4.model.Bundle.BundleType.BATCHRESPONSE.toCode());

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
			myVersionAdapter.setRequestUrl(entry, next.getIdElement().toUnqualifiedVersionless().getValue());
		}

		transaction(theRequestDetails, transactionBundle, false);

		return resp;
	}

	private void populateEntryWithOperationOutcome(BaseServerResponseException caughtEx, IBase nextEntry) {
		myVersionAdapter.populateEntryWithOperationOutcome(caughtEx, nextEntry);
	}

	private void handleTransactionCreateOrUpdateOutcome(Map<IIdType, IIdType> idSubstitutions, Map<IIdType, DaoMethodOutcome> idToPersistedOutcome, IIdType nextResourceId, DaoMethodOutcome outcome,
																		 IBase newEntry, String theResourceType, IBaseResource theRes, RequestDetails theRequestDetails) {
		IIdType newId = outcome.getId().toUnqualified();
		IIdType resourceId = isPlaceholder(nextResourceId) ? nextResourceId : nextResourceId.toUnqualifiedVersionless();
		if (newId.equals(resourceId) == false) {
			idSubstitutions.put(resourceId, newId);
			if (isPlaceholder(resourceId)) {
				/*
				 * The correct way for substitution IDs to be is to be with no resource type, but we'll accept the qualified kind too just to be lenient.
				 */
				IIdType id = myContext.getVersion().newIdType();
				id.setValue(theResourceType + '/' + resourceId.getValue());
				idSubstitutions.put(id, newId);
			}
		}
		idToPersistedOutcome.put(newId, outcome);
		if (outcome.getCreated()) {
			myVersionAdapter.setResponseStatus(newEntry, toStatusString(Constants.STATUS_HTTP_201_CREATED));
		} else {
			myVersionAdapter.setResponseStatus(newEntry, toStatusString(Constants.STATUS_HTTP_200_OK));
		}
		Date lastModifier = getLastModified(theRes);
		myVersionAdapter.setResponseLastModified(newEntry, lastModifier);

		if (theRequestDetails != null) {
			String prefer = theRequestDetails.getHeader(Constants.HEADER_PREFER);
			PreferReturnEnum preferReturn = RestfulServerUtils.parsePreferHeader(null, prefer).getReturn();
			if (preferReturn != null) {
				if (preferReturn == PreferReturnEnum.REPRESENTATION) {
					if (outcome.getResource() != null) {
						outcome.fireResourceViewCallbacks();
						myVersionAdapter.setResource(newEntry, outcome.getResource());
					}
				}
			}
		}

	}

	private Date getLastModified(IBaseResource theRes) {
		return theRes.getMeta().getLastUpdated();
	}

	private String performIdSubstitutionsInMatchUrl(Map<IIdType, IIdType> theIdSubstitutions, String theMatchUrl) {
		String matchUrl = theMatchUrl;
		if (isNotBlank(matchUrl)) {
			for (Map.Entry<IIdType, IIdType> nextSubstitutionEntry : theIdSubstitutions.entrySet()) {
				IIdType nextTemporaryId = nextSubstitutionEntry.getKey();
				IIdType nextReplacementId = nextSubstitutionEntry.getValue();
				String nextTemporaryIdPart = nextTemporaryId.getIdPart();
				String nextReplacementIdPart = nextReplacementId.getValueAsString();
				if (isUrn(nextTemporaryId) && nextTemporaryIdPart.length() > URN_PREFIX.length()) {
					matchUrl = matchUrl.replace(nextTemporaryIdPart, nextReplacementIdPart);
					String escapedUrlParam = UrlUtil.escapeUrlParam(nextTemporaryIdPart);
					if (isNotBlank(escapedUrlParam)) {
						matchUrl = matchUrl.replace(escapedUrlParam, nextReplacementIdPart);
					}
				}
			}
		}
		return matchUrl;
	}

	private boolean isUrn(IIdType theId) {
		return defaultString(theId.getValue()).startsWith(URN_PREFIX);
	}

	public void setDao(BaseHapiFhirDao theDao) {
		myDao = theDao;
	}

	private IBaseBundle processTransactionAsSubRequest(RequestDetails theRequestDetails, IBaseBundle theRequest, String theActionName, boolean theNestedMode) {
		BaseHapiFhirDao.markRequestAsProcessingSubRequest(theRequestDetails);
		try {
			return processTransaction(theRequestDetails, theRequest, theActionName, theNestedMode);
		} finally {
			BaseHapiFhirDao.clearRequestAsProcessingSubRequest(theRequestDetails);
		}
	}

	@VisibleForTesting
	public void setTxManager(PlatformTransactionManager theTxManager) {
		myTxManager = theTxManager;
	}

	private IBaseBundle batch(final RequestDetails theRequestDetails, IBaseBundle theRequest, boolean theNestedMode) {
		ourLog.info("Beginning batch with {} resources", myVersionAdapter.getEntries(theRequest).size());

		long start = System.currentTimeMillis();

		TransactionTemplate txTemplate = new TransactionTemplate(myTxManager);
		txTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);

		IBaseBundle response = myVersionAdapter.createBundle(org.hl7.fhir.r4.model.Bundle.BundleType.BATCHRESPONSE.toCode());
		Map<Integer, Object> responseMap = new ConcurrentHashMap<>();
				
		List<IBase> requestEntries = myVersionAdapter.getEntries(theRequest);
		int requestEntriesSize = requestEntries.size();

		// And execute for each entry in parallel as a mini-transaction in its 
		// own database transaction so that if one fails, it doesn't prevent others.
		// The result is keep in the map to save the original position

		CountDownLatch completionLatch = new CountDownLatch(requestEntriesSize);
		IBase nextRequestEntry = null;
		for (int i=0; i<requestEntriesSize; i++ ) {
			nextRequestEntry = requestEntries.get(i);
			BundleTask bundleTask = new BundleTask(completionLatch, theRequestDetails, responseMap, i, nextRequestEntry, theNestedMode);
			//Don't spin up a new thread for nothing if batch size is <=1
			if (myDaoConfig.getBundleBatchPoolSize() <= 1) {
				bundleTask.call();
			} else {
				myExecutor.submit(bundleTask);
			}
		}

		// waiting for all tasks to be completed
		AsyncUtil.awaitLatchAndIgnoreInterrupt(completionLatch, 300L, TimeUnit.SECONDS);
		
		// Now, create the bundle response in original order
		Object nextResponseEntry;
		for (int i=0; i<requestEntriesSize; i++ )  {
			
			nextResponseEntry = responseMap.get(i);
			if (nextResponseEntry instanceof BaseServerResponseExceptionHolder) {
				BaseServerResponseExceptionHolder caughtEx = (BaseServerResponseExceptionHolder)nextResponseEntry;
				if (caughtEx.getException() != null) {
					IBase nextEntry = myVersionAdapter.addEntry(response);
					populateEntryWithOperationOutcome(caughtEx.getException(), nextEntry);
					myVersionAdapter.setResponseStatus(nextEntry, toStatusString(caughtEx.getException().getStatusCode()));
				} 
			} else {
				myVersionAdapter.addEntry(response, (IBase)nextResponseEntry);
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

	private IBaseBundle processTransaction(final RequestDetails theRequestDetails, final IBaseBundle theRequest, final String theActionName, boolean theNestedMode) {
		validateDependencies();

		String transactionType = myVersionAdapter.getBundleType(theRequest);

		if (org.hl7.fhir.r4.model.Bundle.BundleType.BATCH.toCode().equals(transactionType)) {
			return batch(theRequestDetails, theRequest, theNestedMode);
		}

		if (transactionType == null) {
			String message = "Transaction Bundle did not specify valid Bundle.type, assuming " + Bundle.BundleType.TRANSACTION.toCode();
			ourLog.warn(message);
			transactionType = org.hl7.fhir.r4.model.Bundle.BundleType.TRANSACTION.toCode();
		}
		if (!org.hl7.fhir.r4.model.Bundle.BundleType.TRANSACTION.toCode().equals(transactionType)) {
			throw new InvalidRequestException("Unable to process transaction where incoming Bundle.type = " + transactionType);
		}

		int numberOfEntries = myVersionAdapter.getEntries(theRequest).size();

		if (myDaoConfig.getMaximumTransactionBundleSize() != null && numberOfEntries > myDaoConfig.getMaximumTransactionBundleSize()) {
			throw new PayloadTooLargeException("Transaction Bundle Too large.  Transaction bundle contains " +
				numberOfEntries +
				" which exceedes the maximum permitted transaction bundle size of " + myDaoConfig.getMaximumTransactionBundleSize());
		}

		ourLog.debug("Beginning {} with {} resources", theActionName, numberOfEntries);

		final TransactionDetails transactionDetails = new TransactionDetails();
		final StopWatch transactionStopWatch = new StopWatch();

		List<IBase> requestEntries = myVersionAdapter.getEntries(theRequest);

		// Do all entries have a verb?
		for (int i = 0; i < numberOfEntries; i++) {
			IBase nextReqEntry = requestEntries.get(i);
			String verb = myVersionAdapter.getEntryRequestVerb(myContext, nextReqEntry);
			if (verb == null || !isValidVerb(verb)) {
				throw new InvalidRequestException(myContext.getLocalizer().getMessage(BaseHapiFhirSystemDao.class, "transactionEntryHasInvalidVerb", verb, i));
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
		final IBaseBundle response = myVersionAdapter.createBundle(org.hl7.fhir.r4.model.Bundle.BundleType.TRANSACTIONRESPONSE.toCode());
		List<IBase> getEntries = new ArrayList<>();
		final IdentityHashMap<IBase, Integer> originalRequestOrder = new IdentityHashMap<>();
		for (int i = 0; i < requestEntries.size(); i++) {
			originalRequestOrder.put(requestEntries.get(i), i);
			myVersionAdapter.addEntry(response);
			if (myVersionAdapter.getEntryRequestVerb(myContext, requestEntries.get(i)).equals("GET")) {
				getEntries.add(requestEntries.get(i));
			}
		}

		/*
		 * See FhirSystemDaoDstu3Test#testTransactionWithPlaceholderIdInMatchUrl
		 * Basically if the resource has a match URL that references a placeholder,
		 * we try to handle the resource with the placeholder first.
		 */
		Set<String> placeholderIds = new HashSet<>();
		final List<IBase> entries = requestEntries;
		for (IBase nextEntry : entries) {
			String fullUrl = myVersionAdapter.getFullUrl(nextEntry);
			if (isNotBlank(fullUrl) && fullUrl.startsWith(URN_PREFIX)) {
				placeholderIds.add(fullUrl);
			}
		}
		entries.sort(new TransactionSorter(placeholderIds));

		doTransactionWriteOperations(theRequestDetails, theActionName, transactionDetails, transactionStopWatch, response, originalRequestOrder, entries);

		/*
		 * Loop through the request and process any entries of type GET
		 */
		if (getEntries.size() > 0) {
			transactionStopWatch.startTask("Process " + getEntries.size() + " GET entries");
		}
		for (IBase nextReqEntry : getEntries) {

			if (theNestedMode) {
				throw new InvalidRequestException("Can not invoke read operation on nested transaction");
			}

			if (!(theRequestDetails instanceof ServletRequestDetails)) {
				throw new MethodNotAllowedException("Can not call transaction GET methods from this context");
			}

			ServletRequestDetails srd = (ServletRequestDetails) theRequestDetails;
			Integer originalOrder = originalRequestOrder.get(nextReqEntry);
			IBase nextRespEntry = (IBase) myVersionAdapter.getEntries(response).get(originalOrder);

			ArrayListMultimap<String, String> paramValues = ArrayListMultimap.create();

			String transactionUrl = extractTransactionUrlOrThrowException(nextReqEntry, "GET");

			ServletSubRequestDetails requestDetails = ServletRequestUtil.getServletSubRequestDetails(srd, transactionUrl, paramValues);

			String url = requestDetails.getRequestPath();

			BaseMethodBinding<?> method = srd.getServer().determineResourceMethod(requestDetails, url);
			if (method == null) {
				throw new IllegalArgumentException("Unable to handle GET " + url);
			}

			if (isNotBlank(myVersionAdapter.getEntryRequestIfMatch(nextReqEntry))) {
				requestDetails.addHeader(Constants.HEADER_IF_MATCH, myVersionAdapter.getEntryRequestIfMatch(nextReqEntry));
			}
			if (isNotBlank(myVersionAdapter.getEntryRequestIfNoneExist(nextReqEntry))) {
				requestDetails.addHeader(Constants.HEADER_IF_NONE_EXIST, myVersionAdapter.getEntryRequestIfNoneExist(nextReqEntry));
			}
			if (isNotBlank(myVersionAdapter.getEntryRequestIfNoneMatch(nextReqEntry))) {
				requestDetails.addHeader(Constants.HEADER_IF_NONE_MATCH, myVersionAdapter.getEntryRequestIfNoneMatch(nextReqEntry));
			}

			Validate.isTrue(method instanceof BaseResourceReturningMethodBinding, "Unable to handle GET {}", url);
			try {

				BaseResourceReturningMethodBinding methodBinding = (BaseResourceReturningMethodBinding) method;
				requestDetails.setRestOperationType(methodBinding.getRestOperationType());

				IBaseResource resource = methodBinding.doInvokeServer(srd.getServer(), requestDetails);
				if (paramValues.containsKey(Constants.PARAM_SUMMARY) || paramValues.containsKey(Constants.PARAM_CONTENT)) {
					resource = filterNestedBundle(requestDetails, resource);
				}
				myVersionAdapter.setResource(nextRespEntry, resource);
				myVersionAdapter.setResponseStatus(nextRespEntry, toStatusString(Constants.STATUS_HTTP_200_OK));
			} catch (NotModifiedException e) {
				myVersionAdapter.setResponseStatus(nextRespEntry, toStatusString(Constants.STATUS_HTTP_304_NOT_MODIFIED));
			} catch (BaseServerResponseException e) {
				ourLog.info("Failure processing transaction GET {}: {}", url, e.toString());
				myVersionAdapter.setResponseStatus(nextRespEntry, toStatusString(e.getStatusCode()));
				populateEntryWithOperationOutcome(e, nextRespEntry);
			}

		}
		transactionStopWatch.endCurrentTask();

		// Interceptor broadcast: JPA_PERFTRACE_INFO
		if (CompositeInterceptorBroadcaster.hasHooks(Pointcut.JPA_PERFTRACE_INFO, myInterceptorBroadcaster, theRequestDetails)) {
			String taskDurations = transactionStopWatch.formatTaskDurations();
			StorageProcessingMessage message = new StorageProcessingMessage();
			message.setMessage("Transaction timing:\n" + taskDurations);
			HookParams params = new HookParams()
				.add(RequestDetails.class, theRequestDetails)
				.addIfMatchesType(ServletRequestDetails.class, theRequestDetails)
				.add(StorageProcessingMessage.class, message);
			CompositeInterceptorBroadcaster.doCallHooks(myInterceptorBroadcaster, theRequestDetails, Pointcut.JPA_PERFTRACE_INFO, params);
		}

		return response;
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
	private void doTransactionWriteOperations(RequestDetails theRequestDetails, String theActionName, TransactionDetails theTransactionDetails, StopWatch theTransactionStopWatch, IBaseBundle theResponse, IdentityHashMap<IBase, Integer> theOriginalRequestOrder, List<IBase> theEntries) {
		TransactionWriteOperationsDetails writeOperationsDetails = null;
		if (CompositeInterceptorBroadcaster.hasHooks(Pointcut.STORAGE_TRANSACTION_WRITE_OPERATIONS_PRE, myInterceptorBroadcaster, theRequestDetails) ||
			CompositeInterceptorBroadcaster.hasHooks(Pointcut.STORAGE_TRANSACTION_WRITE_OPERATIONS_POST, myInterceptorBroadcaster, theRequestDetails)) {

			List<String> updateRequestUrls = new ArrayList<>();
			List<String> conditionalCreateRequestUrls = new ArrayList<>();
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
			HookParams params = new HookParams()
				.add(TransactionDetails.class, theTransactionDetails)
				.add(TransactionWriteOperationsDetails.class, writeOperationsDetails);
			CompositeInterceptorBroadcaster.doCallHooks(myInterceptorBroadcaster, theRequestDetails, Pointcut.STORAGE_TRANSACTION_WRITE_OPERATIONS_PRE, params);

		}

		TransactionCallback<Map<IBase, IIdType>> txCallback = status -> {
			final Set<IIdType> allIds = new LinkedHashSet<>();
			final Map<IIdType, IIdType> idSubstitutions = new HashMap<>();
			final Map<IIdType, DaoMethodOutcome> idToPersistedOutcome = new HashMap<>();
			Map<IBase, IIdType> retVal = doTransactionWriteOperations(theRequestDetails, theActionName, theTransactionDetails, allIds, idSubstitutions, idToPersistedOutcome, theResponse, theOriginalRequestOrder, theEntries, theTransactionStopWatch);

			theTransactionStopWatch.startTask("Commit writes to database");
			return retVal;
		};
		Map<IBase, IIdType> entriesToProcess;

		try {
			entriesToProcess = myHapiTransactionService.execute(theRequestDetails, theTransactionDetails, txCallback);
		} finally {
			if (writeOperationsDetails != null) {
				HookParams params = new HookParams()
					.add(TransactionDetails.class, theTransactionDetails)
					.add(TransactionWriteOperationsDetails.class, writeOperationsDetails);
				CompositeInterceptorBroadcaster.doCallHooks(myInterceptorBroadcaster, theRequestDetails, Pointcut.STORAGE_TRANSACTION_WRITE_OPERATIONS_POST, params);
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

	@VisibleForTesting
	public void setModelConfig(ModelConfig theModelConfig) {
		myModelConfig = theModelConfig;
	}

	protected Map<IBase, IIdType> doTransactionWriteOperations(final RequestDetails theRequest, String theActionName, TransactionDetails theTransactionDetails, Set<IIdType> theAllIds,
																				  Map<IIdType, IIdType> theIdSubstitutions, Map<IIdType, DaoMethodOutcome> theIdToPersistedOutcome, IBaseBundle theResponse, IdentityHashMap<IBase, Integer> theOriginalRequestOrder, List<IBase> theEntries, StopWatch theTransactionStopWatch) {

		theTransactionDetails.beginAcceptingDeferredInterceptorBroadcasts(
			Pointcut.STORAGE_PRECOMMIT_RESOURCE_CREATED,
			Pointcut.STORAGE_PRECOMMIT_RESOURCE_UPDATED,
			Pointcut.STORAGE_PRECOMMIT_RESOURCE_DELETED
		);
		try {

			Set<String> deletedResources = new HashSet<>();
			DeleteConflictList deleteConflicts = new DeleteConflictList();
			Map<IBase, IIdType> entriesToProcess = new IdentityHashMap<>();
			Set<IIdType> nonUpdatedEntities = new HashSet<>();
			Set<IBasePersistedResource> updatedEntities = new HashSet<>();
			List<IBaseResource> updatedResources = new ArrayList<>();
			Map<String, Class<? extends IBaseResource>> conditionalRequestUrls = new HashMap<>();

			/*
			 * Look for duplicate conditional creates and consolidate them
			 */
			final HashMap<String, String> keyToUuid = new HashMap<>();
			for (int index = 0, originalIndex = 0; index < theEntries.size(); index++, originalIndex++) {
				IBase nextReqEntry = theEntries.get(index);
				IBaseResource resource = myVersionAdapter.getResource(nextReqEntry);
				if (resource != null) {
					String verb = myVersionAdapter.getEntryRequestVerb(myContext, nextReqEntry);
					String entryUrl = myVersionAdapter.getFullUrl(nextReqEntry);
					String requestUrl = myVersionAdapter.getEntryRequestUrl(nextReqEntry);
					String ifNoneExist = myVersionAdapter.getEntryRequestIfNoneExist(nextReqEntry);
					String key = verb + "|" + requestUrl + "|" + ifNoneExist;

					// Conditional UPDATE
					boolean consolidateEntry = false;
					if ("PUT".equals(verb)) {
						if (isNotBlank(entryUrl) && isNotBlank(requestUrl)) {
							int questionMarkIndex = requestUrl.indexOf('?');
							if (questionMarkIndex >= 0 && requestUrl.length() > (questionMarkIndex + 1)) {
								consolidateEntry = true;
							}
						}
					}

					// Conditional CREATE
					if ("POST".equals(verb)) {
						if (isNotBlank(entryUrl) && isNotBlank(requestUrl) && isNotBlank(ifNoneExist)) {
							if (!entryUrl.equals(requestUrl)) {
								consolidateEntry = true;
							}
						}
					}

					if (consolidateEntry) {
						if (!keyToUuid.containsKey(key)) {
							keyToUuid.put(key, entryUrl);
						} else {
							ourLog.info("Discarding transaction bundle entry {} as it contained a duplicate conditional {}", originalIndex, verb);
							theEntries.remove(index);
							index--;
							String existingUuid = keyToUuid.get(key);
							for (IBase nextEntry : theEntries) {
								IBaseResource nextResource = myVersionAdapter.getResource(nextEntry);
								for (IBaseReference nextReference : myContext.newTerser().getAllPopulatedChildElementsOfType(nextResource, IBaseReference.class)) {
									// We're interested in any references directly to the placeholder ID, but also
									// references that have a resource target that has the placeholder ID.
									String nextReferenceId = nextReference.getReferenceElement().getValue();
									if (isBlank(nextReferenceId) && nextReference.getResource() != null) {
										nextReferenceId = nextReference.getResource().getIdElement().getValue();
									}
									if (entryUrl.equals(nextReferenceId)) {
										nextReference.setReference(existingUuid);
										nextReference.setResource(null);
									}
								}
							}
						}
					}
				}
			}


			/*
			 * Loop through the request and process any entries of type
			 * PUT, POST or DELETE
			 */
			for (int i = 0; i < theEntries.size(); i++) {

				if (i % 250 == 0) {
					ourLog.debug("Processed {} non-GET entries out of {} in transaction", i, theEntries.size());
				}

				IBase nextReqEntry = theEntries.get(i);
				IBaseResource res = myVersionAdapter.getResource(nextReqEntry);
				IIdType nextResourceId = null;
				if (res != null) {

					nextResourceId = res.getIdElement();

					String fullUrl = myVersionAdapter.getFullUrl(nextReqEntry);
					if (isNotBlank(fullUrl)) {
						IIdType fullUrlIdType = newIdType(fullUrl);
						if (isPlaceholder(fullUrlIdType)) {
							nextResourceId = fullUrlIdType;
						} else if (!nextResourceId.hasIdPart()) {
							nextResourceId = fullUrlIdType;
						}
					}

					if (nextResourceId.hasIdPart() && nextResourceId.getIdPart().matches("[a-zA-Z]+:.*") && !isPlaceholder(nextResourceId)) {
						throw new InvalidRequestException("Invalid placeholder ID found: " + nextResourceId.getIdPart() + " - Must be of the form 'urn:uuid:[uuid]' or 'urn:oid:[oid]'");
					}

					if (nextResourceId.hasIdPart() && !nextResourceId.hasResourceType() && !isPlaceholder(nextResourceId)) {
						nextResourceId = newIdType(toResourceName(res.getClass()), nextResourceId.getIdPart());
						res.setId(nextResourceId);
					}

					/*
					 * Ensure that the bundle doesn't have any duplicates, since this causes all kinds of weirdness
					 */
					if (isPlaceholder(nextResourceId)) {
						if (!theAllIds.add(nextResourceId)) {
							throw new InvalidRequestException(myContext.getLocalizer().getMessage(BaseHapiFhirSystemDao.class, "transactionContainsMultipleWithDuplicateId", nextResourceId));
						}
					} else if (nextResourceId.hasResourceType() && nextResourceId.hasIdPart()) {
						IIdType nextId = nextResourceId.toUnqualifiedVersionless();
						if (!theAllIds.add(nextId)) {
							throw new InvalidRequestException(myContext.getLocalizer().getMessage(BaseHapiFhirSystemDao.class, "transactionContainsMultipleWithDuplicateId", nextId));
						}
					}

				}

				String verb = myVersionAdapter.getEntryRequestVerb(myContext, nextReqEntry);
				String resourceType = res != null ? myContext.getResourceType(res) : null;
				Integer order = theOriginalRequestOrder.get(nextReqEntry);
				IBase nextRespEntry = (IBase) myVersionAdapter.getEntries(theResponse).get(order);

				theTransactionStopWatch.startTask("Bundle.entry[" + i + "]: " + verb + " " + defaultString(resourceType));

				switch (verb) {
					case "POST": {
						// CREATE
						validateResourcePresent(res, order, verb);
						@SuppressWarnings("rawtypes")
						IFhirResourceDao resourceDao = getDaoOrThrowException(res.getClass());
						res.setId((String) null);
						DaoMethodOutcome outcome;
						String matchUrl = myVersionAdapter.getEntryRequestIfNoneExist(nextReqEntry);
						matchUrl = performIdSubstitutionsInMatchUrl(theIdSubstitutions, matchUrl);
						outcome = resourceDao.create(res, matchUrl, false, theTransactionDetails, theRequest);
					 	res.setId(outcome.getId());
						if (nextResourceId != null) {
							handleTransactionCreateOrUpdateOutcome(theIdSubstitutions, theIdToPersistedOutcome, nextResourceId, outcome, nextRespEntry, resourceType, res, theRequest);
						}
						entriesToProcess.put(nextRespEntry, outcome.getId());
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
						String url = extractTransactionUrlOrThrowException(nextReqEntry, verb);
						UrlUtil.UrlParts parts = UrlUtil.parseUrl(url);
						IFhirResourceDao<? extends IBaseResource> dao = toDao(parts, verb, url);
						int status = Constants.STATUS_HTTP_204_NO_CONTENT;
						if (parts.getResourceId() != null) {
							IIdType deleteId = newIdType(parts.getResourceType(), parts.getResourceId());
							if (!deletedResources.contains(deleteId.getValueAsString())) {
								DaoMethodOutcome outcome = dao.delete(deleteId, deleteConflicts, theRequest, theTransactionDetails);
								if (outcome.getEntity() != null) {
									deletedResources.add(deleteId.getValueAsString());
									entriesToProcess.put(nextRespEntry, outcome.getId());
								}
							}
						} else {
							String matchUrl = parts.getResourceType() + '?' + parts.getParams();
							matchUrl = performIdSubstitutionsInMatchUrl(theIdSubstitutions, matchUrl);
							DeleteMethodOutcome deleteOutcome = dao.deleteByUrl(matchUrl, deleteConflicts, theRequest);
							List<ResourceTable> allDeleted = deleteOutcome.getDeletedEntities();
							for (ResourceTable deleted : allDeleted) {
								deletedResources.add(deleted.getIdDt().toUnqualifiedVersionless().getValueAsString());
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

						String url = extractTransactionUrlOrThrowException(nextReqEntry, verb);

						DaoMethodOutcome outcome;
						UrlUtil.UrlParts parts = UrlUtil.parseUrl(url);
						if (isNotBlank(parts.getResourceId())) {
							String version = null;
							if (isNotBlank(myVersionAdapter.getEntryRequestIfMatch(nextReqEntry))) {
								version = ParameterUtil.parseETagValue(myVersionAdapter.getEntryRequestIfMatch(nextReqEntry));
							}
							res.setId(newIdType(parts.getResourceType(), parts.getResourceId(), version));
							outcome = resourceDao.update(res, null, false, false, theRequest, theTransactionDetails);
						} else {
							res.setId((String) null);
							String matchUrl;
							if (isNotBlank(parts.getParams())) {
								matchUrl = parts.getResourceType() + '?' + parts.getParams();
							} else {
								matchUrl = parts.getResourceType();
							}
							matchUrl = performIdSubstitutionsInMatchUrl(theIdSubstitutions, matchUrl);
							outcome = resourceDao.update(res, matchUrl, false, false, theRequest, theTransactionDetails);
							if (Boolean.TRUE.equals(outcome.getCreated())) {
								conditionalRequestUrls.put(matchUrl, res.getClass());
							}
						}

						if (outcome.getCreated() == Boolean.FALSE
							|| (outcome.getCreated() == Boolean.TRUE && outcome.getId().getVersionIdPartAsLong() > 1)) {
							updatedEntities.add(outcome.getEntity());
							if (outcome.getResource() != null) {
								updatedResources.add(outcome.getResource());
							}
						}

						handleTransactionCreateOrUpdateOutcome(theIdSubstitutions, theIdToPersistedOutcome, nextResourceId, outcome, nextRespEntry, resourceType, res, theRequest);
						entriesToProcess.put(nextRespEntry, outcome.getId());
						break;
					}
					case "PATCH": {
						// PATCH
						validateResourcePresent(res, order, verb);

						String url = extractTransactionUrlOrThrowException(nextReqEntry, verb);
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
							patchType = PatchTypeEnum.forContentTypeOrThrowInvalidRequestException(myContext, contentType);
							if (patchType == PatchTypeEnum.FHIR_PATCH_JSON || patchType == PatchTypeEnum.FHIR_PATCH_XML) {
								String msg = myContext.getLocalizer().getMessage(TransactionProcessor.class, "fhirPatchShouldNotUseBinaryResource");
								throw new InvalidRequestException(msg);
							}
						} else if (res instanceof IBaseParameters) {
							patchBodyParameters = (IBaseParameters) res;
							patchType = PatchTypeEnum.FHIR_PATCH_JSON;
						}

						if (patchBodyParameters == null) {
							if (isBlank(patchBody)) {
								String msg = myContext.getLocalizer().getMessage(TransactionProcessor.class, "missingPatchBody");
								throw new InvalidRequestException(msg);
							}
						}

						IFhirResourceDao<? extends IBaseResource> dao = toDao(parts, verb, url);
						IIdType patchId = myContext.getVersion().newIdType().setValue(parts.getResourceId());
						DaoMethodOutcome outcome = dao.patch(patchId, matchUrl, patchType, patchBody, patchBodyParameters, theRequest);
						updatedEntities.add(outcome.getEntity());
						if (outcome.getResource() != null) {
							updatedResources.add(outcome.getResource());
						}

						break;
					}
					case "GET":
						break;
					default:
						throw new InvalidRequestException("Unable to handle verb in transaction: " + verb);

				}

				theTransactionStopWatch.endCurrentTask();
			}

			/*
			 * Make sure that there are no conflicts from deletions. E.g. we can't delete something
			 * if something else has a reference to it.. Unless the thing that has a reference to it
			 * was also deleted as a part of this transaction, which is why we check this now at the
			 * end.
			 */
			for (Iterator<DeleteConflict> iter = deleteConflicts.iterator(); iter.hasNext(); ) {
				DeleteConflict nextDeleteConflict = iter.next();

				/*
				 * If we have a conflict, it means we can't delete Resource/A because
				 * Resource/B has a reference to it. We'll ignore that conflict though
				 * if it turns out we're also deleting Resource/B in this transaction.
				 */
				if (deletedResources.contains(nextDeleteConflict.getSourceId().toUnqualifiedVersionless().getValue())) {
					iter.remove();
					continue;
				}

				/*
				 * And then, this is kind of a last ditch check. It's also ok to delete
				 * Resource/A if Resource/B isn't being deleted, but it is being UPDATED
				 * in this transaction, and the updated version of it has no references
				 * to Resource/A any more.
				 */
				String sourceId = nextDeleteConflict.getSourceId().toUnqualifiedVersionless().getValue();
				String targetId = nextDeleteConflict.getTargetId().toUnqualifiedVersionless().getValue();
				Optional<IBaseResource> updatedSource = updatedResources
					.stream()
					.filter(t -> sourceId.equals(t.getIdElement().toUnqualifiedVersionless().getValue()))
					.findFirst();
				if (updatedSource.isPresent()) {
					List<ResourceReferenceInfo> referencesInSource = myContext.newTerser().getAllResourceReferences(updatedSource.get());
					boolean sourceStillReferencesTarget = referencesInSource
						.stream()
						.anyMatch(t -> targetId.equals(t.getResourceReference().getReferenceElement().toUnqualifiedVersionless().getValue()));
					if (!sourceStillReferencesTarget) {
						iter.remove();
					}
				}
			}
			DeleteConflictService.validateDeleteConflictsEmptyOrThrowException(myContext, deleteConflicts);

			theIdToPersistedOutcome.entrySet().forEach(t -> theTransactionDetails.addResolvedResourceId(t.getKey(), t.getValue().getPersistentId()));

			/*
			 * Perform ID substitutions and then index each resource we have saved
			 */

			resolveReferencesThenSaveAndIndexResources(theRequest, theTransactionDetails, theIdSubstitutions, theIdToPersistedOutcome, theTransactionStopWatch, entriesToProcess, nonUpdatedEntities, updatedEntities);

			theTransactionStopWatch.endCurrentTask();
			theTransactionStopWatch.startTask("Flush writes to database");

			flushSession(theIdToPersistedOutcome);

			theTransactionStopWatch.endCurrentTask();

			/*
			 * Double check we didn't allow any duplicates we shouldn't have
			 */
			if (conditionalRequestUrls.size() > 0) {
				theTransactionStopWatch.startTask("Check for conflicts in conditional resources");
			}
			if (!myDaoConfig.isMassIngestionMode()) {
				validateNoDuplicates(theRequest, theActionName, conditionalRequestUrls, theIdToPersistedOutcome.values());
			}
			theTransactionStopWatch.endCurrentTask();

			for (IIdType next : theAllIds) {
				IIdType replacement = theIdSubstitutions.get(next);
				if (replacement != null && !replacement.equals(next)) {
					ourLog.debug("Placeholder resource ID \"{}\" was replaced with permanent ID \"{}\"", next, replacement);
				}
			}

			ListMultimap<Pointcut, HookParams> deferredBroadcastEvents = theTransactionDetails.endAcceptingDeferredInterceptorBroadcasts();
			for (Map.Entry<Pointcut, HookParams> nextEntry : deferredBroadcastEvents.entries()) {
				Pointcut nextPointcut = nextEntry.getKey();
				HookParams nextParams = nextEntry.getValue();
				CompositeInterceptorBroadcaster.doCallHooks(myInterceptorBroadcaster, theRequest, nextPointcut, nextParams);
			}

			DeferredInterceptorBroadcasts deferredInterceptorBroadcasts = new DeferredInterceptorBroadcasts(deferredBroadcastEvents);
			HookParams params = new HookParams()
				.add(RequestDetails.class, theRequest)
				.addIfMatchesType(ServletRequestDetails.class, theRequest)
				.add(DeferredInterceptorBroadcasts.class, deferredInterceptorBroadcasts)
				.add(TransactionDetails.class, theTransactionDetails)
				.add(IBaseBundle.class, theResponse);
			CompositeInterceptorBroadcaster.doCallHooks(myInterceptorBroadcaster, theRequest, Pointcut.STORAGE_TRANSACTION_PROCESSED, params);

			theTransactionDetails.deferredBroadcastProcessingFinished();

			//finishedCallingDeferredInterceptorBroadcasts

			return entriesToProcess;

		} finally {
			if (theTransactionDetails.isAcceptingDeferredInterceptorBroadcasts()) {
				theTransactionDetails.endAcceptingDeferredInterceptorBroadcasts();
			}
		}
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
	private void resolveReferencesThenSaveAndIndexResources(RequestDetails theRequest, TransactionDetails theTransactionDetails, Map<IIdType, IIdType> theIdSubstitutions, Map<IIdType, DaoMethodOutcome> theIdToPersistedOutcome, StopWatch theTransactionStopWatch, Map<IBase, IIdType> entriesToProcess, Set<IIdType> nonUpdatedEntities, Set<IBasePersistedResource> updatedEntities) {
		FhirTerser terser = myContext.newTerser();
		theTransactionStopWatch.startTask("Index " + theIdToPersistedOutcome.size() + " resources");
		IdentityHashMap<DaoMethodOutcome, Set<IBaseReference>> deferredIndexesForAutoVersioning = null;
		int i = 0;
		for (DaoMethodOutcome nextOutcome : theIdToPersistedOutcome.values()) {

			if (i++ % 250 == 0) {
				ourLog.debug("Have indexed {} entities out of {} in transaction", i, theIdToPersistedOutcome.values().size());
			}

			if (nextOutcome.isNop()) {
				continue;
			}

			IBaseResource nextResource = nextOutcome.getResource();
			if (nextResource == null) {
				continue;
			}

			Set<IBaseReference> referencesToAutoVersion = BaseStorageDao.extractReferencesToAutoVersion(myContext, myModelConfig, nextResource);
			if (referencesToAutoVersion.isEmpty()) {
				resolveReferencesThenSaveAndIndexResource(theRequest, theTransactionDetails, theIdSubstitutions, theIdToPersistedOutcome, entriesToProcess, nonUpdatedEntities, updatedEntities, terser, nextOutcome, nextResource, referencesToAutoVersion);
			} else {
				if (deferredIndexesForAutoVersioning == null) {
					deferredIndexesForAutoVersioning = new IdentityHashMap<>();
				}
				deferredIndexesForAutoVersioning.put(nextOutcome, referencesToAutoVersion);
			}
		}

		// If we have any resources we'll be auto-versioning, index these next
		if (deferredIndexesForAutoVersioning != null) {
			for (Map.Entry<DaoMethodOutcome, Set<IBaseReference>> nextEntry : deferredIndexesForAutoVersioning.entrySet()) {
				DaoMethodOutcome nextOutcome = nextEntry.getKey();
				Set<IBaseReference> referencesToAutoVersion = nextEntry.getValue();
				IBaseResource nextResource = nextOutcome.getResource();
				resolveReferencesThenSaveAndIndexResource(theRequest, theTransactionDetails, theIdSubstitutions, theIdToPersistedOutcome, entriesToProcess, nonUpdatedEntities, updatedEntities, terser, nextOutcome, nextResource, referencesToAutoVersion);
			}
		}
	}

	private void resolveReferencesThenSaveAndIndexResource(RequestDetails theRequest, TransactionDetails theTransactionDetails, Map<IIdType, IIdType> theIdSubstitutions, Map<IIdType, DaoMethodOutcome> theIdToPersistedOutcome, Map<IBase, IIdType> entriesToProcess, Set<IIdType> nonUpdatedEntities, Set<IBasePersistedResource> updatedEntities, FhirTerser terser, DaoMethodOutcome nextOutcome, IBaseResource nextResource, Set<IBaseReference> theReferencesToAutoVersion) {
		// References
		List<ResourceReferenceInfo> allRefs = terser.getAllResourceReferences(nextResource);
		for (ResourceReferenceInfo nextRef : allRefs) {
			IBaseReference resourceReference = nextRef.getResourceReference();
			IIdType nextId = resourceReference.getReferenceElement();
			IIdType newId = null;
			if (!nextId.hasIdPart()) {
				if (resourceReference.getResource() != null) {
					IIdType targetId = resourceReference.getResource().getIdElement();
					if (targetId.getValue() == null || targetId.getValue().startsWith("#")) {
						// This means it's a contained resource
						continue;
					} else if (theIdSubstitutions.containsValue(targetId)) {
						newId = targetId;
					} else {
						throw new InternalErrorException("References by resource with no reference ID are not supported in DAO layer");
					}
				} else {
					continue;
				}
			}
			if (newId != null || theIdSubstitutions.containsKey(nextId)) {
				if (newId == null) {
					newId = theIdSubstitutions.get(nextId);
				}
				if (newId != null) {
					ourLog.debug(" * Replacing resource ref {} with {}", nextId, newId);

					addRollbackReferenceRestore(theTransactionDetails, resourceReference);
					if (theReferencesToAutoVersion.contains(resourceReference)) {
						resourceReference.setReference(newId.getValue());
						resourceReference.setResource(null);
					} else {
						resourceReference.setReference(newId.toVersionless().getValue());
						resourceReference.setResource(null);
					}
				}
			} else if (nextId.getValue().startsWith("urn:")) {
				throw new InvalidRequestException("Unable to satisfy placeholder ID " + nextId.getValue() + " found in element named '" + nextRef.getName() + "' within resource of type: " + nextResource.getIdElement().getResourceType());
			} else {
				if (theReferencesToAutoVersion.contains(resourceReference)) {
					DaoMethodOutcome outcome = theIdToPersistedOutcome.get(nextId);
					if (!outcome.isNop() && !Boolean.TRUE.equals(outcome.getCreated())) {
						addRollbackReferenceRestore(theTransactionDetails, resourceReference);
						resourceReference.setReference(nextId.getValue());
						resourceReference.setResource(null);
					}
				}
			}
		}

		// URIs
		Class<? extends IPrimitiveType<?>> uriType = (Class<? extends IPrimitiveType<?>>) myContext.getElementDefinition("uri").getImplementingClass();
		List<? extends IPrimitiveType<?>> allUris = terser.getAllPopulatedChildElementsOfType(nextResource, uriType);
		for (IPrimitiveType<?> nextRef : allUris) {
			if (nextRef instanceof IIdType) {
				continue; // No substitution on the resource ID itself!
			}
			IIdType nextUriString = newIdType(nextRef.getValueAsString());
			if (theIdSubstitutions.containsKey(nextUriString)) {
				IIdType newId = theIdSubstitutions.get(nextUriString);
				ourLog.debug(" * Replacing resource ref {} with {}", nextUriString, newId);

				String existingValue = nextRef.getValueAsString();
				theTransactionDetails.addRollbackUndoAction(() -> nextRef.setValueAsString(existingValue));

				nextRef.setValueAsString(newId.toVersionless().getValue());
			} else {
				ourLog.debug(" * Reference [{}] does not exist in bundle", nextUriString);
			}
		}

		IPrimitiveType<Date> deletedInstantOrNull;
		if (nextResource instanceof IAnyResource) {
			deletedInstantOrNull = ResourceMetadataKeyEnum.DELETED_AT.get((IAnyResource) nextResource);
		} else {
			deletedInstantOrNull = ResourceMetadataKeyEnum.DELETED_AT.get((IResource) nextResource);
		}
		Date deletedTimestampOrNull = deletedInstantOrNull != null ? deletedInstantOrNull.getValue() : null;

		IFhirResourceDao<? extends IBaseResource> dao = myDaoRegistry.getResourceDao(nextResource.getClass());
		IJpaDao jpaDao = (IJpaDao) dao;

		IBasePersistedResource updateOutcome = null;
		if (updatedEntities.contains(nextOutcome.getEntity())) {
			boolean forceUpdateVersion = !theReferencesToAutoVersion.isEmpty();

			updateOutcome = jpaDao.updateInternal(theRequest, nextResource, true, forceUpdateVersion, nextOutcome.getEntity(), nextResource.getIdElement(), nextOutcome.getPreviousResource(), theTransactionDetails);
		} else if (!nonUpdatedEntities.contains(nextOutcome.getId())) {
			updateOutcome = jpaDao.updateEntity(theRequest, nextResource, nextOutcome.getEntity(), deletedTimestampOrNull, true, false, theTransactionDetails, false, true);
		}

		// Make sure we reflect the actual final version for the resource.
		if (updateOutcome != null) {
			IIdType newId = updateOutcome.getIdDt();
			for (IIdType nextEntry : entriesToProcess.values()) {
				if (nextEntry.getResourceType().equals(newId.getResourceType())) {
					if (nextEntry.getIdPart().equals(newId.getIdPart())) {
						if (!nextEntry.hasVersionIdPart() || !nextEntry.getVersionIdPart().equals(newId.getVersionIdPart())) {
							nextEntry.setParts(nextEntry.getBaseUrl(), nextEntry.getResourceType(), nextEntry.getIdPart(), newId.getVersionIdPart());
						}
					}
				}
			}

			nextOutcome.setId(newId);

			for (IIdType next : theIdSubstitutions.values()) {
				if (next.getResourceType().equals(newId.getResourceType())) {
					if (next.getIdPart().equals(newId.getIdPart())) {
						if (!next.getValue().equals(newId.getValue())) {
							next.setValue(newId.getValue());
						}
					}
				}
			}

		}
	}

	private void addRollbackReferenceRestore(TransactionDetails theTransactionDetails, IBaseReference resourceReference) {
		String existingValue = resourceReference.getReferenceElement().getValue();
		theTransactionDetails.addRollbackUndoAction(() -> resourceReference.setReference(existingValue));
	}

	private void validateNoDuplicates(RequestDetails theRequest, String theActionName, Map<String, Class<? extends IBaseResource>> conditionalRequestUrls, Collection<DaoMethodOutcome> thePersistedOutcomes) {

		IdentityHashMap<IBaseResource, ResourceIndexedSearchParams> resourceToIndexedParams = new IdentityHashMap<>(thePersistedOutcomes.size());
		thePersistedOutcomes
			.stream()
			.filter(t -> !t.isNop())
			.filter(t -> t.getEntity() instanceof ResourceTable)
			.filter(t -> t.getEntity().getDeleted() == null)
			.filter(t -> t.getResource() != null)
			.forEach(t -> resourceToIndexedParams.put(t.getResource(), new ResourceIndexedSearchParams((ResourceTable) t.getEntity())));

		for (Map.Entry<String, Class<? extends IBaseResource>> nextEntry : conditionalRequestUrls.entrySet()) {
			String matchUrl = nextEntry.getKey();
			if (isNotBlank(matchUrl)) {
				if (matchUrl.startsWith("?") || (!matchUrl.contains("?") && UNQUALIFIED_MATCH_URL_START.matcher(matchUrl).find())) {
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
				for (Map.Entry<IBaseResource, ResourceIndexedSearchParams> entries : resourceToIndexedParams.entrySet()) {
					ResourceIndexedSearchParams indexedParams = entries.getValue();
					IBaseResource resource = entries.getKey();

					String resourceType = myContext.getResourceType(resource);
					if (!matchUrl.startsWith(resourceType + "?")) {
						continue;
					}

					if (myInMemoryResourceMatcher.match(matchUrl, resource, indexedParams).matched()) {
						counter++;
						if (counter > 1) {
							throw new InvalidRequestException(
								"Unable to process " + theActionName + " - Request would cause multiple resources to match URL: \"" + matchUrl + "\". Does transaction request contain duplicates?");
						}
					}
				}
			}
		}
	}

	protected abstract void flushSession(Map<IIdType, DaoMethodOutcome> theIdToPersistedOutcome);

	private void validateResourcePresent(IBaseResource theResource, Integer theOrder, String theVerb) {
		if (theResource == null) {
			String msg = myContext.getLocalizer().getMessage(TransactionProcessor.class, "missingMandatoryResource", theVerb, theOrder);
			throw new InvalidRequestException(msg);
		}
	}

	private IIdType newIdType(String theResourceType, String theResourceId, String theVersion) {
		org.hl7.fhir.r4.model.IdType id = new org.hl7.fhir.r4.model.IdType(theResourceType, theResourceId, theVersion);
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
			String msg = myContext.getLocalizer().getMessage(BaseTransactionProcessor.class, "unsupportedResourceType", type, types.toString());
			throw new InvalidRequestException(msg);
		}
		return dao;
	}

	private String toResourceName(Class<? extends IBaseResource> theResourceType) {
		return myContext.getResourceType(theResourceType);
	}

	public void setContext(FhirContext theContext) {
		myContext = theContext;
	}

	private String extractTransactionUrlOrThrowException(IBase nextEntry, String verb) {
		String url = myVersionAdapter.getEntryRequestUrl(nextEntry);
		if (isBlank(url)) {
			throw new InvalidRequestException(myContext.getLocalizer().getMessage(BaseHapiFhirSystemDao.class, "transactionMissingUrl", verb));
		}
		return url;
	}

	private IFhirResourceDao<? extends IBaseResource> toDao(UrlUtil.UrlParts theParts, String theVerb, String theUrl) {
		RuntimeResourceDefinition resType;
		try {
			resType = myContext.getResourceDefinition(theParts.getResourceType());
		} catch (DataFormatException e) {
			String msg = myContext.getLocalizer().getMessage(BaseHapiFhirSystemDao.class, "transactionInvalidUrl", theVerb, theUrl);
			throw new InvalidRequestException(msg);
		}
		IFhirResourceDao<? extends IBaseResource> dao = null;
		if (resType != null) {
			dao = myDaoRegistry.getResourceDao(resType.getImplementingClass());
		}
		if (dao == null) {
			String msg = myContext.getLocalizer().getMessage(BaseHapiFhirSystemDao.class, "transactionInvalidUrl", theVerb, theUrl);
			throw new InvalidRequestException(msg);
		}

		// if (theParts.getResourceId() == null && theParts.getParams() == null) {
		// String msg = getContext().getLocalizer().getMessage(BaseHapiFhirSystemDao.class, "transactionInvalidUrl", theVerb, theUrl);
		// throw new InvalidRequestException(msg);
		// }

		return dao;
	}

	private String toMatchUrl(IBase theEntry) {
		String verb = myVersionAdapter.getEntryRequestVerb(myContext, theEntry);
		if (verb.equals("POST")) {
			return myVersionAdapter.getEntryIfNoneExist(theEntry);
		}
		if (verb.equals("PATCH")) {
			return myVersionAdapter.getEntryRequestIfMatch(theEntry);
		}
		if (verb.equals("PUT") || verb.equals("DELETE")) {
			String url = extractTransactionUrlOrThrowException(theEntry, verb);
			UrlUtil.UrlParts parts = UrlUtil.parseUrl(url);
			if (isBlank(parts.getResourceId())) {
				return parts.getResourceType() + '?' + parts.getParams();
			}
		}
		return null;
	}

	public interface ITransactionProcessorVersionAdapter<BUNDLE extends IBaseBundle, BUNDLEENTRY extends IBase> {

		void setResponseStatus(BUNDLEENTRY theBundleEntry, String theStatus);

		void setResponseLastModified(BUNDLEENTRY theBundleEntry, Date theLastModified);

		void setResource(BUNDLEENTRY theBundleEntry, IBaseResource theResource);

		IBaseResource getResource(BUNDLEENTRY theBundleEntry);

		String getBundleType(BUNDLE theRequest);

		void populateEntryWithOperationOutcome(BaseServerResponseException theCaughtEx, BUNDLEENTRY theEntry);

		BUNDLE createBundle(String theBundleType);

		List<BUNDLEENTRY> getEntries(BUNDLE theRequest);

		void addEntry(BUNDLE theBundle, BUNDLEENTRY theEntry);

		BUNDLEENTRY addEntry(BUNDLE theBundle);

		String getEntryRequestVerb(FhirContext theContext, BUNDLEENTRY theEntry);

		String getFullUrl(BUNDLEENTRY theEntry);

		String getEntryIfNoneExist(BUNDLEENTRY theEntry);

		String getEntryRequestUrl(BUNDLEENTRY theEntry);

		void setResponseLocation(BUNDLEENTRY theEntry, String theResponseLocation);

		void setResponseETag(BUNDLEENTRY theEntry, String theEtag);

		String getEntryRequestIfMatch(BUNDLEENTRY theEntry);

		String getEntryRequestIfNoneExist(BUNDLEENTRY theEntry);

		String getEntryRequestIfNoneMatch(BUNDLEENTRY theEntry);

		void setResponseOutcome(BUNDLEENTRY theEntry, IBaseOperationOutcome theOperationOutcome);

		void setRequestVerb(BUNDLEENTRY theEntry, String theVerb);

		void setRequestUrl(BUNDLEENTRY theEntry, String theUrl);
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
	//@formatter:off
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

	private static class BaseServerResponseExceptionHolder {
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

	public class BundleTask implements Callable<Void> {

		private CountDownLatch myCompletedLatch;
		private RequestDetails myRequestDetails;
		private IBase myNextReqEntry;
		private Map<Integer, Object> myResponseMap;
		private int myResponseOrder;
		private boolean myNestedMode;
		
		protected BundleTask(CountDownLatch theCompletedLatch, RequestDetails theRequestDetails, Map<Integer, Object> theResponseMap, int theResponseOrder, IBase theNextReqEntry, boolean theNestedMode) {
			this.myCompletedLatch = theCompletedLatch;
			this.myRequestDetails = theRequestDetails;
			this.myNextReqEntry = theNextReqEntry;
			this.myResponseMap = theResponseMap;		
			this.myResponseOrder = theResponseOrder;					
			this.myNestedMode = theNestedMode;
		}
		
		@Override
		public Void call() {
			
			BaseServerResponseExceptionHolder caughtEx = new BaseServerResponseExceptionHolder();

			try {
				IBaseBundle subRequestBundle = myVersionAdapter.createBundle(org.hl7.fhir.r4.model.Bundle.BundleType.TRANSACTION.toCode());
				myVersionAdapter.addEntry(subRequestBundle, (IBase) myNextReqEntry);

				IBaseBundle nextResponseBundle = processTransactionAsSubRequest(myRequestDetails, subRequestBundle, "Batch sub-request", myNestedMode);

				IBase  subResponseEntry = (IBase) myVersionAdapter.getEntries(nextResponseBundle).get(0);
				myResponseMap.put(myResponseOrder, subResponseEntry);
								
				/*
				 * If the individual entry didn't have a resource in its response, bring the sub-transaction's OperationOutcome across so the client can see it
				 */
				if (myVersionAdapter.getResource(subResponseEntry) == null) {
					IBase nextResponseBundleFirstEntry = (IBase) myVersionAdapter.getEntries(nextResponseBundle).get(0);
					myResponseMap.put(myResponseOrder, nextResponseBundleFirstEntry);
				}

			} catch (BaseServerResponseException e) {
				caughtEx.setException(e);
			} catch (Throwable t) {
				ourLog.error("Failure during BATCH sub transaction processing", t);
				caughtEx.setException(new InternalErrorException(t));
			}

			if (caughtEx.getException() != null) {
				// add exception to the response map
				myResponseMap.put(myResponseOrder, caughtEx);
			}
			
			// checking for the parallelism
			ourLog.debug("processing bacth for {} is completed", myVersionAdapter.getEntryRequestUrl((IBase)myNextReqEntry));
			myCompletedLatch.countDown();
			return null;
		}
	}
}
