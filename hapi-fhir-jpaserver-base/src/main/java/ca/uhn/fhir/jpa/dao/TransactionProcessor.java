package ca.uhn.fhir.jpa.dao;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2019 University Health Network
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
import ca.uhn.fhir.jpa.config.HapiFhirHibernateJpaDialect;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.provider.ServletSubRequestDetails;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.jpa.util.DeleteConflict;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.PreferReturnEnum;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.ParameterUtil;
import ca.uhn.fhir.rest.server.RestfulServerUtils;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.NotModifiedException;
import ca.uhn.fhir.rest.server.interceptor.IServerInterceptor;
import ca.uhn.fhir.rest.server.method.BaseMethodBinding;
import ca.uhn.fhir.rest.server.method.BaseResourceReturningMethodBinding;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.util.*;
import com.google.common.collect.ArrayListMultimap;
import org.apache.commons.lang3.Validate;
import org.apache.http.NameValuePair;
import org.hibernate.Session;
import org.hibernate.internal.SessionImpl;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.PersistenceException;
import java.util.*;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.*;

public class TransactionProcessor<BUNDLE extends IBaseBundle, BUNDLEENTRY> {

	public static final String URN_PREFIX = "urn:";
	private static final Logger ourLog = LoggerFactory.getLogger(TransactionProcessor.class);
	private BaseHapiFhirDao myDao;
	@Autowired
	private PlatformTransactionManager myTxManager;
	@PersistenceContext(type = PersistenceContextType.TRANSACTION)
	private EntityManager myEntityManager;
	@Autowired
	private FhirContext myContext;
	@Autowired
	private ITransactionProcessorVersionAdapter<BUNDLE, BUNDLEENTRY> myVersionAdapter;
	@Autowired
	private MatchUrlService myMatchUrlService;
	@Autowired
	private DaoRegistry myDaoRegistry;
	@Autowired(required = false)
	private HapiFhirHibernateJpaDialect myHapiFhirHibernateJpaDialect;

	public BUNDLE transaction(RequestDetails theRequestDetails, BUNDLE theRequest) {
		if (theRequestDetails != null) {
			IServerInterceptor.ActionRequestDetails requestDetails = new IServerInterceptor.ActionRequestDetails(theRequestDetails, theRequest, "Bundle", null);
			myDao.notifyInterceptors(RestOperationTypeEnum.TRANSACTION, requestDetails);
		}

		String actionName = "Transaction";
		BUNDLE response = processTransactionAsSubRequest((ServletRequestDetails) theRequestDetails, theRequest, actionName);

		List<BUNDLEENTRY> entries = myVersionAdapter.getEntries(response);
		for (int i = 0; i < entries.size(); i++) {
			if (ElementUtil.isEmpty(entries.get(i))) {
				entries.remove(i);
				i--;
			}
		}

		return response;
	}

	public BUNDLE collection(final RequestDetails theRequestDetails, BUNDLE theRequest) {
		String transactionType = myVersionAdapter.getBundleType(theRequest);

		if (!org.hl7.fhir.r4.model.Bundle.BundleType.COLLECTION.toCode().equals(transactionType)) {
			throw new InvalidRequestException("Can not process collection Bundle of type: " + transactionType);
		}

		ourLog.info("Beginning storing collection with {} resources", myVersionAdapter.getEntries(theRequest).size());
		long start = System.currentTimeMillis();

		TransactionTemplate txTemplate = new TransactionTemplate(myTxManager);
		txTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);

		BUNDLE resp = myVersionAdapter.createBundle(org.hl7.fhir.r4.model.Bundle.BundleType.BATCHRESPONSE.toCode());

		List<IBaseResource> resources = new ArrayList<>();
		for (final BUNDLEENTRY nextRequestEntry : myVersionAdapter.getEntries(theRequest)) {
			IBaseResource resource = myVersionAdapter.getResource(nextRequestEntry);
			resources.add(resource);
		}

		BUNDLE transactionBundle = myVersionAdapter.createBundle("transaction");
		for (IBaseResource next : resources) {
			BUNDLEENTRY entry = myVersionAdapter.addEntry(transactionBundle);
			myVersionAdapter.setResource(entry, next);
			myVersionAdapter.setRequestVerb(entry, "PUT");
			myVersionAdapter.setRequestUrl(entry, next.getIdElement().toUnqualifiedVersionless().getValue());
		}

		transaction(theRequestDetails, transactionBundle);

		return resp;
	}

	private void populateEntryWithOperationOutcome(BaseServerResponseException caughtEx, BUNDLEENTRY nextEntry) {
		myVersionAdapter.populateEntryWithOperationOutcome(caughtEx, nextEntry);
	}

	private void handleTransactionCreateOrUpdateOutcome(Map<IIdType, IIdType> idSubstitutions, Map<IIdType, DaoMethodOutcome> idToPersistedOutcome, IIdType nextResourceId, DaoMethodOutcome outcome,
																		 BUNDLEENTRY newEntry, String theResourceType, IBaseResource theRes, ServletRequestDetails theRequestDetails) {
		IIdType newId = outcome.getId().toUnqualifiedVersionless();
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
		if (outcome.getCreated().booleanValue()) {
			myVersionAdapter.setResponseStatus(newEntry, toStatusString(Constants.STATUS_HTTP_201_CREATED));
		} else {
			myVersionAdapter.setResponseStatus(newEntry, toStatusString(Constants.STATUS_HTTP_200_OK));
		}
		Date lastModifier = getLastModified(theRes);
		myVersionAdapter.setResponseLastModified(newEntry, lastModifier);

		if (theRequestDetails != null) {
			if (outcome.getResource() != null) {
				String prefer = theRequestDetails.getHeader(Constants.HEADER_PREFER);
				PreferReturnEnum preferReturn = RestfulServerUtils.parsePreferHeader(prefer);
				if (preferReturn != null) {
					if (preferReturn == PreferReturnEnum.REPRESENTATION) {
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
					matchUrl = matchUrl.replace(UrlUtil.escapeUrlParam(nextTemporaryIdPart), nextReplacementIdPart);
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

	private BUNDLE processTransactionAsSubRequest(ServletRequestDetails theRequestDetails, BUNDLE theRequest, String theActionName) {
		BaseHapiFhirDao.markRequestAsProcessingSubRequest(theRequestDetails);
		try {
			return processTransaction(theRequestDetails, theRequest, theActionName);
		} finally {
			BaseHapiFhirDao.clearRequestAsProcessingSubRequest(theRequestDetails);
		}
	}

	private BUNDLE batch(final RequestDetails theRequestDetails, BUNDLE theRequest) {
		ourLog.info("Beginning batch with {} resources", myVersionAdapter.getEntries(theRequest).size());
		long start = System.currentTimeMillis();

		TransactionTemplate txTemplate = new TransactionTemplate(myTxManager);
		txTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);

		BUNDLE resp = myVersionAdapter.createBundle(org.hl7.fhir.r4.model.Bundle.BundleType.BATCHRESPONSE.toCode());

		/*
		 * For batch, we handle each entry as a mini-transaction in its own database transaction so that if one fails, it doesn't prevent others
		 */

		for (final BUNDLEENTRY nextRequestEntry : myVersionAdapter.getEntries(theRequest)) {

			BaseServerResponseExceptionHolder caughtEx = new BaseServerResponseExceptionHolder();

			TransactionCallback<BUNDLE> callback = theStatus -> {
				BUNDLE subRequestBundle = myVersionAdapter.createBundle(org.hl7.fhir.r4.model.Bundle.BundleType.TRANSACTION.toCode());
				myVersionAdapter.addEntry(subRequestBundle, nextRequestEntry);

				return processTransactionAsSubRequest((ServletRequestDetails) theRequestDetails, subRequestBundle, "Batch sub-request");
			};

			try {
				// FIXME: this doesn't need to be a callback
				BUNDLE nextResponseBundle = callback.doInTransaction(null);

				BUNDLEENTRY subResponseEntry = myVersionAdapter.getEntries(nextResponseBundle).get(0);
				myVersionAdapter.addEntry(resp, subResponseEntry);

				/*
				 * If the individual entry didn't have a resource in its response, bring the sub-transaction's OperationOutcome across so the client can see it
				 */
				if (myVersionAdapter.getResource(subResponseEntry) == null) {
					BUNDLEENTRY nextResponseBundleFirstEntry = myVersionAdapter.getEntries(nextResponseBundle).get(0);
					myVersionAdapter.setResource(subResponseEntry, myVersionAdapter.getResource(nextResponseBundleFirstEntry));
				}

			} catch (BaseServerResponseException e) {
				caughtEx.setException(e);
			} catch (Throwable t) {
				ourLog.error("Failure during BATCH sub transaction processing", t);
				caughtEx.setException(new InternalErrorException(t));
			}

			if (caughtEx.getException() != null) {
				BUNDLEENTRY nextEntry = myVersionAdapter.addEntry(resp);

				populateEntryWithOperationOutcome(caughtEx.getException(), nextEntry);

				myVersionAdapter.setResponseStatus(nextEntry, toStatusString(caughtEx.getException().getStatusCode()));
			}

		}

		long delay = System.currentTimeMillis() - start;
		ourLog.info("Batch completed in {}ms", new Object[]{delay});

		return resp;
	}

	private BUNDLE processTransaction(final ServletRequestDetails theRequestDetails, final BUNDLE theRequest, final String theActionName) {
		validateDependencies();

		String transactionType = myVersionAdapter.getBundleType(theRequest);

		if (org.hl7.fhir.r4.model.Bundle.BundleType.BATCH.toCode().equals(transactionType)) {
			return batch(theRequestDetails, theRequest);
		}

		if (transactionType == null) {
			String message = "Transaction Bundle did not specify valid Bundle.type, assuming " + Bundle.BundleType.TRANSACTION.toCode();
			ourLog.warn(message);
			transactionType = org.hl7.fhir.r4.model.Bundle.BundleType.TRANSACTION.toCode();
		}
		if (!org.hl7.fhir.r4.model.Bundle.BundleType.TRANSACTION.toCode().equals(transactionType)) {
			throw new InvalidRequestException("Unable to process transaction where incoming Bundle.type = " + transactionType);
		}

		ourLog.debug("Beginning {} with {} resources", theActionName, myVersionAdapter.getEntries(theRequest).size());

		final Date updateTime = new Date();
		final StopWatch transactionStopWatch = new StopWatch();

		final Set<IIdType> allIds = new LinkedHashSet<>();
		final Map<IIdType, IIdType> idSubstitutions = new HashMap<>();
		final Map<IIdType, DaoMethodOutcome> idToPersistedOutcome = new HashMap<>();
		List<BUNDLEENTRY> requestEntries = myVersionAdapter.getEntries(theRequest);

		// Do all entries have a verb?
		for (int i = 0; i < myVersionAdapter.getEntries(theRequest).size(); i++) {
			BUNDLEENTRY nextReqEntry = requestEntries.get(i);
			String verb = myVersionAdapter.getEntryRequestVerb(nextReqEntry);
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
		final BUNDLE response = myVersionAdapter.createBundle(org.hl7.fhir.r4.model.Bundle.BundleType.TRANSACTIONRESPONSE.toCode());
		List<BUNDLEENTRY> getEntries = new ArrayList<>();
		final IdentityHashMap<BUNDLEENTRY, Integer> originalRequestOrder = new IdentityHashMap<>();
		for (int i = 0; i < requestEntries.size(); i++) {
			originalRequestOrder.put(requestEntries.get(i), i);
			myVersionAdapter.addEntry(response);
			if (myVersionAdapter.getEntryRequestVerb(requestEntries.get(i)).equals("GET")) {
				getEntries.add(requestEntries.get(i));
			}
		}

		/*
		 * See FhirSystemDaoDstu3Test#testTransactionWithPlaceholderIdInMatchUrl
		 * Basically if the resource has a match URL that references a placeholder,
		 * we try to handle the resource with the placeholder first.
		 */
		Set<String> placeholderIds = new HashSet<>();
		final List<BUNDLEENTRY> entries = requestEntries;
		for (BUNDLEENTRY nextEntry : entries) {
			String fullUrl = myVersionAdapter.getFullUrl(nextEntry);
			if (isNotBlank(fullUrl) && fullUrl.startsWith(URN_PREFIX)) {
				placeholderIds.add(fullUrl);
			}
		}
		Collections.sort(entries, new TransactionSorter(placeholderIds));

		/*
		 * All of the write operations in the transaction (PUT, POST, etc.. basically anything
		 * except GET) are performed in their own database transaction before we do the reads.
		 * We do this because the reads (specifically the searches) often spawn their own
		 * secondary database transaction and if we allow that within the primary
		 * database transaction we can end up with deadlocks if the server is under
		 * heavy load with lots of concurrent transactions using all available
		 * database connections.
		 */
		TransactionTemplate txManager = new TransactionTemplate(myTxManager);
		Map<BUNDLEENTRY, ResourceTable> entriesToProcess = txManager.execute(status -> {
			Map<BUNDLEENTRY, ResourceTable> retVal = doTransactionWriteOperations(theRequestDetails, theActionName, updateTime, allIds, idSubstitutions, idToPersistedOutcome, response, originalRequestOrder, entries, transactionStopWatch);

			transactionStopWatch.startTask("Commit writes to database");
			return retVal;
		});
		transactionStopWatch.endCurrentTask();

		for (Map.Entry<BUNDLEENTRY, ResourceTable> nextEntry : entriesToProcess.entrySet()) {
			String responseLocation = nextEntry.getValue().getIdDt().toUnqualified().getValue();
			String responseEtag = nextEntry.getValue().getIdDt().getVersionIdPart();
			myVersionAdapter.setResponseLocation(nextEntry.getKey(), responseLocation);
			myVersionAdapter.setResponseETag(nextEntry.getKey(), responseEtag);
		}

		/*
		 * Loop through the request and process any entries of type GET
		 */
		if (getEntries.size() > 0) {
			transactionStopWatch.startTask("Process " + getEntries.size() + " GET entries");
		}
		for (BUNDLEENTRY nextReqEntry : getEntries) {
			Integer originalOrder = originalRequestOrder.get(nextReqEntry);
			BUNDLEENTRY nextRespEntry = myVersionAdapter.getEntries(response).get(originalOrder);

			ServletSubRequestDetails requestDetails = new ServletSubRequestDetails(theRequestDetails);
			requestDetails.setServletRequest(theRequestDetails.getServletRequest());
			requestDetails.setRequestType(RequestTypeEnum.GET);
			requestDetails.setServer(theRequestDetails.getServer());

			String url = extractTransactionUrlOrThrowException(nextReqEntry, "GET");

			int qIndex = url.indexOf('?');
			ArrayListMultimap<String, String> paramValues = ArrayListMultimap.create();
			requestDetails.setParameters(new HashMap<>());
			if (qIndex != -1) {
				String params = url.substring(qIndex);
				List<NameValuePair> parameters = myMatchUrlService.translateMatchUrl(params);
				for (NameValuePair next : parameters) {
					paramValues.put(next.getName(), next.getValue());
				}
				for (Map.Entry<String, Collection<String>> nextParamEntry : paramValues.asMap().entrySet()) {
					String[] nextValue = nextParamEntry.getValue().toArray(new String[nextParamEntry.getValue().size()]);
					requestDetails.addParameter(nextParamEntry.getKey(), nextValue);
				}
				url = url.substring(0, qIndex);
			}

			requestDetails.setRequestPath(url);
			requestDetails.setFhirServerBase(theRequestDetails.getFhirServerBase());

			theRequestDetails.getServer().populateRequestDetailsFromRequestPath(requestDetails, url);
			BaseMethodBinding<?> method = theRequestDetails.getServer().determineResourceMethod(requestDetails, url);
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
				IBaseResource resource = ((BaseResourceReturningMethodBinding) method).doInvokeServer(theRequestDetails.getServer(), requestDetails);
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

		ourLog.debug("Transaction timing:\n{}", transactionStopWatch.formatTaskDurations());

		return response;
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

	private void validateDependencies() {
		Validate.notNull(myEntityManager);
		Validate.notNull(myContext);
		Validate.notNull(myDao);
		Validate.notNull(myTxManager);
	}

	private IIdType newIdType(String theValue) {
		return myContext.getVersion().newIdType().setValue(theValue);
	}


	private Map<BUNDLEENTRY, ResourceTable> doTransactionWriteOperations(final ServletRequestDetails theRequestDetails, String theActionName, Date theUpdateTime, Set<IIdType> theAllIds,
																								Map<IIdType, IIdType> theIdSubstitutions, Map<IIdType, DaoMethodOutcome> theIdToPersistedOutcome, BUNDLE theResponse, IdentityHashMap<BUNDLEENTRY, Integer> theOriginalRequestOrder, List<BUNDLEENTRY> theEntries, StopWatch theTransactionStopWatch) {

		if (theRequestDetails != null) {
			theRequestDetails.startDeferredOperationCallback();
		}
		try {

			Set<String> deletedResources = new HashSet<>();
			List<DeleteConflict> deleteConflicts = new ArrayList<>();
			Map<BUNDLEENTRY, ResourceTable> entriesToProcess = new IdentityHashMap<>();
			Set<ResourceTable> nonUpdatedEntities = new HashSet<>();
			Set<ResourceTable> updatedEntities = new HashSet<>();
			Map<String, Class<? extends IBaseResource>> conditionalRequestUrls = new HashMap<>();

			/*
			 * Look for duplicate conditional creates and consolidate them
			 */
			final HashMap<String, String> keyToUuid = new HashMap<>();
			final IdentityHashMap<IBaseResource, String> identityToUuid = new IdentityHashMap<>();
			for (int index = 0, originalIndex = 0; index < theEntries.size(); index++, originalIndex++) {
				BUNDLEENTRY nextReqEntry = theEntries.get(index);
				IBaseResource resource = myVersionAdapter.getResource(nextReqEntry);
				if (resource != null) {
					String verb = myVersionAdapter.getEntryRequestVerb(nextReqEntry);
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
							identityToUuid.put(resource, entryUrl);
						} else {
							ourLog.info("Discarding transaction bundle entry {} as it contained a duplicate conditional {}", originalIndex, verb);
							theEntries.remove(index);
							index--;
							String existingUuid = keyToUuid.get(key);
							for (BUNDLEENTRY nextEntry : theEntries) {
								IBaseResource nextResource = myVersionAdapter.getResource(nextEntry);
								for (ResourceReferenceInfo nextReference : myContext.newTerser().getAllResourceReferences(nextResource)) {
									// We're interested in any references directly to the placeholder ID, but also
									// references that have a resource target that has the placeholder ID.
									String nextReferenceId = nextReference.getResourceReference().getReferenceElement().getValue();
									if (isBlank(nextReferenceId) && nextReference.getResourceReference().getResource() != null) {
										nextReferenceId = nextReference.getResourceReference().getResource().getIdElement().getValue();
									}
									if (entryUrl.equals(nextReferenceId)) {
										nextReference.getResourceReference().setReference(existingUuid);
										nextReference.getResourceReference().setResource(null);
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
					ourLog.info("Processed {} non-GET entries out of {} in transaction", i, theEntries.size());
				}

				BUNDLEENTRY nextReqEntry = theEntries.get(i);
				IBaseResource res = myVersionAdapter.getResource(nextReqEntry);
				IIdType nextResourceId = null;
				if (res != null) {

					nextResourceId = res.getIdElement();

					if (!nextResourceId.hasIdPart()) {
						if (isNotBlank(myVersionAdapter.getFullUrl(nextReqEntry))) {
							nextResourceId = newIdType(myVersionAdapter.getFullUrl(nextReqEntry));
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

				String verb = myVersionAdapter.getEntryRequestVerb(nextReqEntry);
				String resourceType = res != null ? myContext.getResourceDefinition(res).getName() : null;
				Integer order = theOriginalRequestOrder.get(nextReqEntry);
				BUNDLEENTRY nextRespEntry = myVersionAdapter.getEntries(theResponse).get(order);

				theTransactionStopWatch.startTask("Bundle.entry[" + i + "]: " + verb + " " + defaultString(resourceType));

				switch (verb) {
					case "POST": {
						// CREATE
						@SuppressWarnings("rawtypes")
						IFhirResourceDao resourceDao = getDaoOrThrowException(res.getClass());
						res.setId((String) null);
						DaoMethodOutcome outcome;
						String matchUrl = myVersionAdapter.getEntryRequestIfNoneExist(nextReqEntry);
						matchUrl = performIdSubstitutionsInMatchUrl(theIdSubstitutions, matchUrl);
						outcome = resourceDao.create(res, matchUrl, false, theUpdateTime, theRequestDetails);
						if (nextResourceId != null) {
							handleTransactionCreateOrUpdateOutcome(theIdSubstitutions, theIdToPersistedOutcome, nextResourceId, outcome, nextRespEntry, resourceType, res, theRequestDetails);
						}
						entriesToProcess.put(nextRespEntry, outcome.getEntity());
						if (outcome.getCreated() == false) {
							nonUpdatedEntities.add(outcome.getEntity());
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
						ca.uhn.fhir.jpa.dao.IFhirResourceDao<? extends IBaseResource> dao = toDao(parts, verb, url);
						int status = Constants.STATUS_HTTP_204_NO_CONTENT;
						if (parts.getResourceId() != null) {
							IIdType deleteId = newIdType(parts.getResourceType(), parts.getResourceId());
							if (!deletedResources.contains(deleteId.getValueAsString())) {
								DaoMethodOutcome outcome = dao.delete(deleteId, deleteConflicts, theRequestDetails);
								if (outcome.getEntity() != null) {
									deletedResources.add(deleteId.getValueAsString());
									entriesToProcess.put(nextRespEntry, outcome.getEntity());
								}
							}
						} else {
							String matchUrl = parts.getResourceType() + '?' + parts.getParams();
							matchUrl = performIdSubstitutionsInMatchUrl(theIdSubstitutions, matchUrl);
							DeleteMethodOutcome deleteOutcome = dao.deleteByUrl(matchUrl, deleteConflicts, theRequestDetails);
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
							outcome = resourceDao.update(res, null, false, false, theRequestDetails);
						} else {
							res.setId((String) null);
							String matchUrl;
							if (isNotBlank(parts.getParams())) {
								matchUrl = parts.getResourceType() + '?' + parts.getParams();
							} else {
								matchUrl = parts.getResourceType();
							}
							matchUrl = performIdSubstitutionsInMatchUrl(theIdSubstitutions, matchUrl);
							outcome = resourceDao.update(res, matchUrl, false, false, theRequestDetails);
							if (Boolean.TRUE.equals(outcome.getCreated())) {
								conditionalRequestUrls.put(matchUrl, res.getClass());
							}
						}

						if (outcome.getCreated() == Boolean.FALSE) {
							updatedEntities.add(outcome.getEntity());
						}

						handleTransactionCreateOrUpdateOutcome(theIdSubstitutions, theIdToPersistedOutcome, nextResourceId, outcome, nextRespEntry, resourceType, res, theRequestDetails);
						entriesToProcess.put(nextRespEntry, outcome.getEntity());
						break;
					}
					case "GET":
					default:
						break;

				}

				theTransactionStopWatch.endCurrentTask();
			}


			/*
			 * Make sure that there are no conflicts from deletions. E.g. we can't delete something
			 * if something else has a reference to it.. Unless the thing that has a reference to it
			 * was also deleted as a part of this transaction, which is why we check this now at the
			 * end.
			 */

			deleteConflicts.removeIf(next ->
				deletedResources.contains(next.getTargetId().toUnqualifiedVersionless().getValue()));
			myDao.validateDeleteConflictsEmptyOrThrowException(deleteConflicts);

			/*
			 * Perform ID substitutions and then index each resource we have saved
			 */

			FhirTerser terser = myContext.newTerser();
			theTransactionStopWatch.startTask("Index " + theIdToPersistedOutcome.size() + " resources");
			int i = 0;
			for (DaoMethodOutcome nextOutcome : theIdToPersistedOutcome.values()) {

				if (i++ % 250 == 0) {
					ourLog.info("Have indexed {} entities out of {} in transaction", i, theIdToPersistedOutcome.values().size());
				}

				IBaseResource nextResource = nextOutcome.getResource();
				if (nextResource == null) {
					continue;
				}

				// References
				List<ResourceReferenceInfo> allRefs = terser.getAllResourceReferences(nextResource);
				for (ResourceReferenceInfo nextRef : allRefs) {
					IIdType nextId = nextRef.getResourceReference().getReferenceElement();
					if (!nextId.hasIdPart()) {
						continue;
					}
					if (theIdSubstitutions.containsKey(nextId)) {
						IIdType newId = theIdSubstitutions.get(nextId);
						ourLog.debug(" * Replacing resource ref {} with {}", nextId, newId);
						nextRef.getResourceReference().setReference(newId.getValue());
					} else if (nextId.getValue().startsWith("urn:")) {
						throw new InvalidRequestException("Unable to satisfy placeholder ID " + nextId.getValue() + " found in element named '" + nextRef.getName() + "' within resource of type: " + nextResource.getIdElement().getResourceType());
					} else {
						ourLog.debug(" * Reference [{}] does not exist in bundle", nextId);
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
						nextRef.setValueAsString(newId.getValue());
					} else {
						ourLog.debug(" * Reference [{}] does not exist in bundle", nextUriString);
					}
				}

				IPrimitiveType<Date> deletedInstantOrNull = ResourceMetadataKeyEnum.DELETED_AT.get((IAnyResource) nextResource);
				Date deletedTimestampOrNull = deletedInstantOrNull != null ? deletedInstantOrNull.getValue() : null;

				if (updatedEntities.contains(nextOutcome.getEntity())) {
					myDao.updateInternal(theRequestDetails, nextResource, true, false, nextOutcome.getEntity(), nextResource.getIdElement(), nextOutcome.getPreviousResource());
				} else if (!nonUpdatedEntities.contains(nextOutcome.getEntity())) {
					myDao.updateEntity(theRequestDetails, nextResource, nextOutcome.getEntity(), deletedTimestampOrNull, true, false, theUpdateTime, false, true);
				}
			}

			theTransactionStopWatch.endCurrentTask();
			theTransactionStopWatch.startTask("Flush writes to database");

			try {
				flushJpaSession();
			} catch (PersistenceException e) {
				if (myHapiFhirHibernateJpaDialect != null) {
					List<String> types = theIdToPersistedOutcome.keySet().stream().filter(t -> t != null).map(t -> t.getResourceType()).collect(Collectors.toList());
					String message = "Error flushing transaction with resource types: " + types;
					throw myHapiFhirHibernateJpaDialect.translate(e, message);
				}
				throw e;
			}

			theTransactionStopWatch.endCurrentTask();
			if (conditionalRequestUrls.size() > 0) {
				theTransactionStopWatch.startTask("Check for conflicts in conditional resources");
			}

			/*
			 * Double check we didn't allow any duplicates we shouldn't have
			 */
			for (Map.Entry<String, Class<? extends IBaseResource>> nextEntry : conditionalRequestUrls.entrySet()) {
				String matchUrl = nextEntry.getKey();
				Class<? extends IBaseResource> resType = nextEntry.getValue();
				if (isNotBlank(matchUrl)) {
					IFhirResourceDao<?> resourceDao = myDao.getDao(resType);
					Set<Long> val = resourceDao.processMatchUrl(matchUrl);
					if (val.size() > 1) {
						throw new InvalidRequestException(
							"Unable to process " + theActionName + " - Request would cause multiple resources to match URL: \"" + matchUrl + "\". Does transaction request contain duplicates?");
					}
				}
			}

			theTransactionStopWatch.endCurrentTask();

			for (IIdType next : theAllIds) {
				IIdType replacement = theIdSubstitutions.get(next);
				if (replacement == null) {
					continue;
				}
				if (replacement.equals(next)) {
					continue;
				}
				ourLog.debug("Placeholder resource ID \"{}\" was replaced with permanent ID \"{}\"", next, replacement);
			}
			return entriesToProcess;

		} finally {
			if (theRequestDetails != null) {
				theRequestDetails.stopDeferredRequestOperationCallbackAndRunDeferredItems();
			}
		}
	}

	private IIdType newIdType(String theResourceType, String theResourceId, String theVersion) {
		org.hl7.fhir.r4.model.IdType id = new org.hl7.fhir.r4.model.IdType(theResourceType, theResourceId, theVersion);
		return myContext.getVersion().newIdType().setValue(id.getValue());
	}

	private IIdType newIdType(String theToResourceName, String theIdPart) {
		return newIdType(theToResourceName, theIdPart, null);
	}

	private IFhirResourceDao getDaoOrThrowException(Class<? extends IBaseResource> theClass) {
		return myDaoRegistry.getResourceDao(theClass);
	}

	protected void flushJpaSession() {
		SessionImpl session = (SessionImpl) myEntityManager.unwrap(Session.class);
		int insertionCount = session.getActionQueue().numberOfInsertions();
		int updateCount = session.getActionQueue().numberOfUpdates();

		StopWatch sw = new StopWatch();
		myEntityManager.flush();
		ourLog.debug("Session flush took {}ms for {} inserts and {} updates", sw.getMillis(), insertionCount, updateCount);
	}

	protected String toResourceName(Class<? extends IBaseResource> theResourceType) {
		return myContext.getResourceDefinition(theResourceType).getName();
	}

	public void setContext(FhirContext theContext) {
		myContext = theContext;
	}

	private String extractTransactionUrlOrThrowException(BUNDLEENTRY nextEntry, String verb) {
		String url = myVersionAdapter.getEntryRequestUrl(nextEntry);
		if (isBlank(url)) {
			throw new InvalidRequestException(myContext.getLocalizer().getMessage(BaseHapiFhirSystemDao.class, "transactionMissingUrl", verb));
		}
		return url;
	}

	private ca.uhn.fhir.jpa.dao.IFhirResourceDao<? extends IBaseResource> toDao(UrlUtil.UrlParts theParts, String theVerb, String theUrl) {
		RuntimeResourceDefinition resType;
		try {
			resType = myContext.getResourceDefinition(theParts.getResourceType());
		} catch (DataFormatException e) {
			String msg = myContext.getLocalizer().getMessage(BaseHapiFhirSystemDao.class, "transactionInvalidUrl", theVerb, theUrl);
			throw new InvalidRequestException(msg);
		}
		IFhirResourceDao<? extends IBaseResource> dao = null;
		if (resType != null) {
			dao = myDao.getDao(resType.getImplementingClass());
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

	public interface ITransactionProcessorVersionAdapter<BUNDLE, BUNDLEENTRY> {

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

		String getEntryRequestVerb(BUNDLEENTRY theEntry);

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
	 * Process any GET interactions
	 */
	//@formatter:off
	public class TransactionSorter implements Comparator<BUNDLEENTRY> {

		private Set<String> myPlaceholderIds;

		public TransactionSorter(Set<String> thePlaceholderIds) {
			myPlaceholderIds = thePlaceholderIds;
		}

		@Override
		public int compare(BUNDLEENTRY theO1, BUNDLEENTRY theO2) {
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

		private String toMatchUrl(BUNDLEENTRY theEntry) {
			String verb = myVersionAdapter.getEntryRequestVerb(theEntry);
			if (verb.equals("POST")) {
				return myVersionAdapter.getEntryIfNoneExist(theEntry);
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

		private int toOrder(BUNDLEENTRY theO1) {
			int o1 = 0;
			if (myVersionAdapter.getEntryRequestVerb(theO1) != null) {
				switch (myVersionAdapter.getEntryRequestVerb(theO1)) {
					case "DELETE":
						o1 = 1;
						break;
					case "POST":
						o1 = 2;
						break;
					case "PUT":
						o1 = 3;
						break;
					case "GET":
						o1 = 4;
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
		return Integer.toString(theStatusCode) + " " + defaultString(Constants.HTTP_STATUS_NAMES.get(theStatusCode));
	}

}
