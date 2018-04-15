package ca.uhn.fhir.jpa.dao.r4;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2018 University Health Network
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.jpa.dao.BaseHapiFhirSystemDao;
import ca.uhn.fhir.jpa.dao.DaoMethodOutcome;
import ca.uhn.fhir.jpa.dao.DeleteMethodOutcome;
import ca.uhn.fhir.jpa.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.entity.ResourceTable;
import ca.uhn.fhir.jpa.entity.TagDefinition;
import ca.uhn.fhir.jpa.provider.ServletSubRequestDetails;
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
import ca.uhn.fhir.rest.server.interceptor.IServerInterceptor.ActionRequestDetails;
import ca.uhn.fhir.rest.server.method.BaseMethodBinding;
import ca.uhn.fhir.rest.server.method.BaseResourceReturningMethodBinding;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.util.FhirTerser;
import ca.uhn.fhir.util.UrlUtil;
import ca.uhn.fhir.util.UrlUtil.UrlParts;
import com.google.common.collect.ArrayListMultimap;
import javolution.io.Struct;
import org.apache.commons.lang3.Validate;
import org.apache.http.NameValuePair;
import org.hl7.fhir.instance.model.api.*;
import org.hl7.fhir.r4.model.*;
import org.hl7.fhir.r4.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.r4.model.Bundle.BundleEntryResponseComponent;
import org.hl7.fhir.r4.model.Bundle.BundleType;
import org.hl7.fhir.r4.model.Bundle.HTTPVerb;
import org.hl7.fhir.r4.model.OperationOutcome.IssueSeverity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import javax.persistence.TypedQuery;
import java.util.*;
import java.util.Map.Entry;

import static org.apache.commons.lang3.StringUtils.*;

public class FhirSystemDaoR4 extends BaseHapiFhirSystemDao<Bundle, Meta> {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirSystemDaoR4.class);

	@Autowired
	private PlatformTransactionManager myTxManager;

	private Bundle batch(final RequestDetails theRequestDetails, Bundle theRequest) {
		ourLog.info("Beginning batch with {} resources", theRequest.getEntry().size());
		long start = System.currentTimeMillis();

		TransactionTemplate txTemplate = new TransactionTemplate(myTxManager);
		txTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);

		Bundle resp = new Bundle();
		resp.setType(BundleType.BATCHRESPONSE);

		/*
		 * For batch, we handle each entry as a mini-transaction in its own database transaction so that if one fails, it doesn't prevent others
		 */

		for (final BundleEntryComponent nextRequestEntry : theRequest.getEntry()) {

			BaseServerResponseExceptionHolder caughtEx = new BaseServerResponseExceptionHolder();

			TransactionCallback<Bundle> callback = new TransactionCallback<Bundle>() {
				@Override
				public Bundle doInTransaction(TransactionStatus theStatus) {
					Bundle subRequestBundle = new Bundle();
					subRequestBundle.setType(BundleType.TRANSACTION);
					subRequestBundle.addEntry(nextRequestEntry);

					Bundle subResponseBundle = transaction((ServletRequestDetails) theRequestDetails, subRequestBundle, "Batch sub-request");
					return subResponseBundle;
				}
			};

			try {
				Bundle nextResponseBundle = callback.doInTransaction(null);

				BundleEntryComponent subResponseEntry = nextResponseBundle.getEntry().get(0);
				resp.addEntry(subResponseEntry);

				/*
				 * If the individual entry didn't have a resource in its response, bring the sub-transaction's OperationOutcome across so the client can see it
				 */
				if (subResponseEntry.getResource() == null) {
					subResponseEntry.setResource(nextResponseBundle.getEntry().get(0).getResource());
				}

			} catch (BaseServerResponseException e) {
				caughtEx.setException(e);
			} catch (Throwable t) {
				ourLog.error("Failure during BATCH sub transaction processing", t);
				caughtEx.setException(new InternalErrorException(t));
			}

			if (caughtEx.getException() != null) {
				BundleEntryComponent nextEntry = resp.addEntry();

				populateEntryWithOperationOutcome(caughtEx.getException(), nextEntry);

				BundleEntryResponseComponent nextEntryResp = nextEntry.getResponse();
				nextEntryResp.setStatus(toStatusString(caughtEx.getException().getStatusCode()));
			}

		}

		long delay = System.currentTimeMillis() - start;
		ourLog.info("Batch completed in {}ms", new Object[] {delay});

		return resp;
	}

	private Bundle doTransaction(final ServletRequestDetails theRequestDetails, final Bundle theRequest, final String theActionName) {
		BundleType transactionType = theRequest.getTypeElement().getValue();
		if (transactionType == BundleType.BATCH) {
			return batch(theRequestDetails, theRequest);
		}

		if (transactionType == null) {
			String message = "Transaction Bundle did not specify valid Bundle.type, assuming " + BundleType.TRANSACTION.toCode();
			ourLog.warn(message);
			transactionType = BundleType.TRANSACTION;
		}
		if (transactionType != BundleType.TRANSACTION) {
			throw new InvalidRequestException("Unable to process transaction where incoming Bundle.type = " + transactionType.toCode());
		}

		ourLog.debug("Beginning {} with {} resources", theActionName, theRequest.getEntry().size());

		long start = System.currentTimeMillis();
		final Date updateTime = new Date();

		final Set<IdType> allIds = new LinkedHashSet<>();
		final Map<IdType, IdType> idSubstitutions = new HashMap<>();
		final Map<IdType, DaoMethodOutcome> idToPersistedOutcome = new HashMap<>();

		// Do all entries have a verb?
		for (int i = 0; i < theRequest.getEntry().size(); i++) {
			BundleEntryComponent nextReqEntry = theRequest.getEntry().get(i);
			HTTPVerb verb = nextReqEntry.getRequest().getMethodElement().getValue();
			if (verb == null) {
				throw new InvalidRequestException(getContext().getLocalizer().getMessage(BaseHapiFhirSystemDao.class, "transactionEntryHasInvalidVerb", nextReqEntry.getRequest().getMethod(), i));
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
		final Bundle response = new Bundle();
		List<BundleEntryComponent> getEntries = new ArrayList<>();
		final IdentityHashMap<BundleEntryComponent, Integer> originalRequestOrder = new IdentityHashMap<>();
		for (int i = 0; i < theRequest.getEntry().size(); i++) {
			originalRequestOrder.put(theRequest.getEntry().get(i), i);
			response.addEntry();
			if (theRequest.getEntry().get(i).getRequest().getMethodElement().getValue() == HTTPVerb.GET) {
				getEntries.add(theRequest.getEntry().get(i));
			}
		}

		/*
		 * See FhirSystemDaoR4Test#testTransactionWithPlaceholderIdInMatchUrl
		 * Basically if the resource has a match URL that references a placeholder,
		 * we try to handle the resource with the placeholder first.
		 */
		Set<String> placeholderIds = new HashSet<String>();
		final List<BundleEntryComponent> entries = theRequest.getEntry();
		for (BundleEntryComponent nextEntry : entries) {
			if (isNotBlank(nextEntry.getFullUrl()) && nextEntry.getFullUrl().startsWith(IdType.URN_PREFIX)) {
				placeholderIds.add(nextEntry.getFullUrl());
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
		Map<BundleEntryComponent, ResourceTable> entriesToProcess = txManager.execute(new TransactionCallback<Map<BundleEntryComponent, ResourceTable>>() {
			@Override
			public Map<BundleEntryComponent, ResourceTable> doInTransaction(TransactionStatus status) {
				return doTransactionWriteOperations(theRequestDetails, theRequest, theActionName, updateTime, allIds, idSubstitutions, idToPersistedOutcome, response, originalRequestOrder, entries);
			}
		});
		for (Entry<BundleEntryComponent, ResourceTable> nextEntry : entriesToProcess.entrySet()) {
			String responseLocation = nextEntry.getValue().getIdDt().toUnqualified().getValue();
			String responseEtag = nextEntry.getValue().getIdDt().getVersionIdPart();
			nextEntry.getKey().getResponse().setLocation(responseLocation);
			nextEntry.getKey().getResponse().setEtag(responseEtag);
		}

		/*
		 * Loop through the request and process any entries of type GET
		 */
		for (int i = 0; i < getEntries.size(); i++) {
			BundleEntryComponent nextReqEntry = getEntries.get(i);
			Integer originalOrder = originalRequestOrder.get(nextReqEntry);
			BundleEntryComponent nextRespEntry = response.getEntry().get(originalOrder);

			ServletSubRequestDetails requestDetails = new ServletSubRequestDetails();
			requestDetails.setServletRequest(theRequestDetails.getServletRequest());
			requestDetails.setRequestType(RequestTypeEnum.GET);
			requestDetails.setServer(theRequestDetails.getServer());

			String url = extractTransactionUrlOrThrowException(nextReqEntry, HTTPVerb.GET);

			int qIndex = url.indexOf('?');
			ArrayListMultimap<String, String> paramValues = ArrayListMultimap.create();
			requestDetails.setParameters(new HashMap<String, String[]>());
			if (qIndex != -1) {
				String params = url.substring(qIndex);
				List<NameValuePair> parameters = translateMatchUrl(params);
				for (NameValuePair next : parameters) {
					paramValues.put(next.getName(), next.getValue());
				}
				for (java.util.Map.Entry<String, Collection<String>> nextParamEntry : paramValues.asMap().entrySet()) {
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

			if (isNotBlank(nextReqEntry.getRequest().getIfMatch())) {
				requestDetails.addHeader(Constants.HEADER_IF_MATCH, nextReqEntry.getRequest().getIfMatch());
			}
			if (isNotBlank(nextReqEntry.getRequest().getIfNoneExist())) {
				requestDetails.addHeader(Constants.HEADER_IF_NONE_EXIST, nextReqEntry.getRequest().getIfNoneExist());
			}
			if (isNotBlank(nextReqEntry.getRequest().getIfNoneMatch())) {
				requestDetails.addHeader(Constants.HEADER_IF_NONE_MATCH, nextReqEntry.getRequest().getIfNoneMatch());
			}

			Validate.isTrue(method instanceof BaseResourceReturningMethodBinding, "Unable to handle GET {0}", url);
			try {
				IBaseResource resource = ((BaseResourceReturningMethodBinding) method).doInvokeServer(theRequestDetails.getServer(), requestDetails);
				if (paramValues.containsKey(Constants.PARAM_SUMMARY) || paramValues.containsKey(Constants.PARAM_CONTENT)) {
					resource = filterNestedBundle(requestDetails, resource);
				}
				nextRespEntry.setResource((Resource) resource);
				nextRespEntry.getResponse().setStatus(toStatusString(Constants.STATUS_HTTP_200_OK));
			} catch (NotModifiedException e) {
				nextRespEntry.getResponse().setStatus(toStatusString(Constants.STATUS_HTTP_304_NOT_MODIFIED));
			} catch (BaseServerResponseException e) {
				ourLog.info("Failure processing transaction GET {}: {}", url, e.toString());
				nextRespEntry.getResponse().setStatus(toStatusString(e.getStatusCode()));
				populateEntryWithOperationOutcome(e, nextRespEntry);
			}

		}

		long delay = System.currentTimeMillis() - start;
		ourLog.info(theActionName + " completed in {}ms", new Object[] {delay});

		response.setType(BundleType.TRANSACTIONRESPONSE);
		return response;
	}

	@SuppressWarnings("unchecked")
	private Map<BundleEntryComponent, ResourceTable> doTransactionWriteOperations(ServletRequestDetails theRequestDetails, Bundle theRequest, String theActionName, Date theUpdateTime, Set<IdType> theAllIds,
																											Map<IdType, IdType> theIdSubstitutions, Map<IdType, DaoMethodOutcome> theIdToPersistedOutcome, Bundle theResponse, IdentityHashMap<BundleEntryComponent, Integer> theOriginalRequestOrder, List<BundleEntryComponent> theEntries) {
		Set<String> deletedResources = new HashSet<>();
		List<DeleteConflict> deleteConflicts = new ArrayList<>();
		Map<BundleEntryComponent, ResourceTable> entriesToProcess = new IdentityHashMap<>();
		Set<ResourceTable> nonUpdatedEntities = new HashSet<>();
		Set<ResourceTable> updatedEntities = new HashSet<>();
		Map<String, Class<? extends IBaseResource>> conditionalRequestUrls = new HashMap<>();

		/*
		 * Loop through the request and process any entries of type
		 * PUT, POST or DELETE
		 */
		for (int i = 0; i < theEntries.size(); i++) {

			if (i % 100 == 0) {
				ourLog.debug("Processed {} non-GET entries out of {}", i, theEntries.size());
			}

			BundleEntryComponent nextReqEntry = theEntries.get(i);
			Resource res = nextReqEntry.getResource();
			IdType nextResourceId = null;
			if (res != null) {

				nextResourceId = res.getIdElement();

				if (!nextResourceId.hasIdPart()) {
					if (isNotBlank(nextReqEntry.getFullUrl())) {
						nextResourceId = new IdType(nextReqEntry.getFullUrl());
					}
				}

				if (nextResourceId.hasIdPart() && nextResourceId.getIdPart().matches("[a-zA-Z]+\\:.*") && !isPlaceholder(nextResourceId)) {
					throw new InvalidRequestException("Invalid placeholder ID found: " + nextResourceId.getIdPart() + " - Must be of the form 'urn:uuid:[uuid]' or 'urn:oid:[oid]'");
				}

				if (nextResourceId.hasIdPart() && !nextResourceId.hasResourceType() && !isPlaceholder(nextResourceId)) {
					nextResourceId = new IdType(toResourceName(res.getClass()), nextResourceId.getIdPart());
					res.setId(nextResourceId);
				}

				/*
				 * Ensure that the bundle doesn't have any duplicates, since this causes all kinds of weirdness
				 */
				if (isPlaceholder(nextResourceId)) {
					if (!theAllIds.add(nextResourceId)) {
						throw new InvalidRequestException(getContext().getLocalizer().getMessage(BaseHapiFhirSystemDao.class, "transactionContainsMultipleWithDuplicateId", nextResourceId));
					}
				} else if (nextResourceId.hasResourceType() && nextResourceId.hasIdPart()) {
					IdType nextId = nextResourceId.toUnqualifiedVersionless();
					if (!theAllIds.add(nextId)) {
						throw new InvalidRequestException(getContext().getLocalizer().getMessage(BaseHapiFhirSystemDao.class, "transactionContainsMultipleWithDuplicateId", nextId));
					}
				}

			}

			HTTPVerb verb = nextReqEntry.getRequest().getMethodElement().getValue();

			String resourceType = res != null ? getContext().getResourceDefinition(res).getName() : null;
			BundleEntryComponent nextRespEntry = theResponse.getEntry().get(theOriginalRequestOrder.get(nextReqEntry));

			switch (verb) {
				case POST: {
					// CREATE
					@SuppressWarnings("rawtypes")
					IFhirResourceDao resourceDao = getDaoOrThrowException(res.getClass());
					res.setId((String) null);
					DaoMethodOutcome outcome;
					String matchUrl = nextReqEntry.getRequest().getIfNoneExist();
					matchUrl = performIdSubstitutionsInMatchUrl(theIdSubstitutions, matchUrl);
					outcome = resourceDao.create(res, matchUrl, false, theRequestDetails);
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
				case DELETE: {
					// DELETE
					String url = extractTransactionUrlOrThrowException(nextReqEntry, verb);
					UrlParts parts = UrlUtil.parseUrl(url);
					ca.uhn.fhir.jpa.dao.IFhirResourceDao<? extends IBaseResource> dao = toDao(parts, verb.toCode(), url);
					int status = Constants.STATUS_HTTP_204_NO_CONTENT;
					if (parts.getResourceId() != null) {
						IdType deleteId = new IdType(parts.getResourceType(), parts.getResourceId());
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

						nextRespEntry.getResponse().setOutcome((Resource) deleteOutcome.getOperationOutcome());
					}

					nextRespEntry.getResponse().setStatus(toStatusString(status));

					break;
				}
				case PUT: {
					// UPDATE
					@SuppressWarnings("rawtypes")
					IFhirResourceDao resourceDao = getDaoOrThrowException(res.getClass());

					String url = extractTransactionUrlOrThrowException(nextReqEntry, verb);

					DaoMethodOutcome outcome;
					UrlParts parts = UrlUtil.parseUrl(url);
					if (isNotBlank(parts.getResourceId())) {
						String version = null;
						if (isNotBlank(nextReqEntry.getRequest().getIfMatch())) {
							version = ParameterUtil.parseETagValue(nextReqEntry.getRequest().getIfMatch());
						}
						res.setId(new IdType(parts.getResourceType(), parts.getResourceId(), version));
						outcome = resourceDao.update(res, null, false, theRequestDetails);
					} else {
						res.setId((String) null);
						String matchUrl;
						if (isNotBlank(parts.getParams())) {
							matchUrl = parts.getResourceType() + '?' + parts.getParams();
						} else {
							matchUrl = parts.getResourceType();
						}
						matchUrl = performIdSubstitutionsInMatchUrl(theIdSubstitutions, matchUrl);
						outcome = resourceDao.update(res, matchUrl, false, theRequestDetails);
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
				case GET:
				case NULL:
				case HEAD:
				case PATCH:
					break;

			}
		}

		/*
		 * Make sure that there are no conflicts from deletions. E.g. we can't delete something
		 * if something else has a reference to it.. Unless the thing that has a reference to it
		 * was also deleted as a part of this transaction, which is why we check this now at the
		 * end.
		 */

		deleteConflicts.removeIf(next ->
			deletedResources.contains(next.getTargetId().toUnqualifiedVersionless().getValue()));
		validateDeleteConflictsEmptyOrThrowException(deleteConflicts);

		/*
		 * Perform ID substitutions and then index each resource we have saved
		 */

		FhirTerser terser = getContext().newTerser();
		for (DaoMethodOutcome nextOutcome : theIdToPersistedOutcome.values()) {
			IBaseResource nextResource = nextOutcome.getResource();
			if (nextResource == null) {
				continue;
			}

			// References
			List<IBaseReference> allRefs = terser.getAllPopulatedChildElementsOfType(nextResource, IBaseReference.class);
			for (IBaseReference nextRef : allRefs) {
				IIdType nextId = nextRef.getReferenceElement();
				if (!nextId.hasIdPart()) {
					continue;
				}
				if (theIdSubstitutions.containsKey(nextId)) {
					IdType newId = theIdSubstitutions.get(nextId);
					ourLog.debug(" * Replacing resource ref {} with {}", nextId, newId);
					nextRef.setReference(newId.getValue());
				} else if (nextId.getValue().startsWith("urn:")) {
					throw new InvalidRequestException("Unable to satisfy placeholder ID: " + nextId.getValue());
				} else {
					ourLog.debug(" * Reference [{}] does not exist in bundle", nextId);
				}
			}

			// URIs
			List<UriType> allUris = terser.getAllPopulatedChildElementsOfType(nextResource, UriType.class);
			for (UriType nextRef : allUris) {
				if (nextRef instanceof IIdType) {
					continue; // No substitution on the resource ID itself!
				}
				IdType nextUriString = new IdType(nextRef.getValueAsString());
				if (theIdSubstitutions.containsKey(nextUriString)) {
					IdType newId = theIdSubstitutions.get(nextUriString);
					ourLog.debug(" * Replacing resource ref {} with {}", nextUriString, newId);
					nextRef.setValue(newId.getValue());
				} else {
					ourLog.debug(" * Reference [{}] does not exist in bundle", nextUriString);
				}
			}

			IPrimitiveType<Date> deletedInstantOrNull = ResourceMetadataKeyEnum.DELETED_AT.get((IAnyResource) nextResource);
			Date deletedTimestampOrNull = deletedInstantOrNull != null ? deletedInstantOrNull.getValue() : null;

			if (updatedEntities.contains(nextOutcome.getEntity())) {
				updateInternal(nextResource, true, false, theRequestDetails, nextOutcome.getEntity(), nextResource.getIdElement(), nextOutcome.getPreviousResource());
			} else if (!nonUpdatedEntities.contains(nextOutcome.getEntity())) {
				updateEntity(nextResource, nextOutcome.getEntity(), deletedTimestampOrNull, true, false, theUpdateTime, false, true);
			}
		}

		flushJpaSession();

		/*
		 * Double check we didn't allow any duplicates we shouldn't have
		 */
		for (Entry<String, Class<? extends IBaseResource>> nextEntry : conditionalRequestUrls.entrySet()) {
			String matchUrl = nextEntry.getKey();
			Class<? extends IBaseResource> resType = nextEntry.getValue();
			if (isNotBlank(matchUrl)) {
				IFhirResourceDao<?> resourceDao = getDao(resType);
				Set<Long> val = resourceDao.processMatchUrl(matchUrl);
				if (val.size() > 1) {
					throw new InvalidRequestException(
						"Unable to process " + theActionName + " - Request would cause multiple resources to match URL: \"" + matchUrl + "\". Does transaction request contain duplicates?");
				}
			}
		}

		for (IdType next : theAllIds) {
			IdType replacement = theIdSubstitutions.get(next);
			if (replacement == null) {
				continue;
			}
			if (replacement.equals(next)) {
				continue;
			}
			ourLog.debug("Placeholder resource ID \"{}\" was replaced with permanent ID \"{}\"", next, replacement);
		}
		return entriesToProcess;
	}

	private String extractTransactionUrlOrThrowException(BundleEntryComponent nextEntry, HTTPVerb verb) {
		String url = nextEntry.getRequest().getUrl();
		if (isBlank(url)) {
			throw new InvalidRequestException(getContext().getLocalizer().getMessage(BaseHapiFhirSystemDao.class, "transactionMissingUrl", verb.name()));
		}
		return url;
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
		IParser p = getContext().newJsonParser();
		RestfulServerUtils.configureResponseParser(theRequestDetails, p);
		return p.parseResource(theResource.getClass(), p.encodeResourceToString(theResource));
	}

	private IFhirResourceDao<?> getDaoOrThrowException(Class<? extends IBaseResource> theClass) {
		IFhirResourceDao<? extends IBaseResource> retVal = getDao(theClass);
		if (retVal == null) {
			throw new InvalidRequestException("Unable to process request, this server does not know how to handle resources of type " + getContext().getResourceDefinition(theClass).getName());
		}
		return retVal;
	}

	@Override
	public Meta metaGetOperation(RequestDetails theRequestDetails) {
		// Notify interceptors
		ActionRequestDetails requestDetails = new ActionRequestDetails(theRequestDetails);
		notifyInterceptors(RestOperationTypeEnum.META, requestDetails);

		String sql = "SELECT d FROM TagDefinition d WHERE d.myId IN (SELECT DISTINCT t.myTagId FROM ResourceTag t)";
		TypedQuery<TagDefinition> q = myEntityManager.createQuery(sql, TagDefinition.class);
		List<TagDefinition> tagDefinitions = q.getResultList();

		return toMeta(tagDefinitions);
	}

	private String performIdSubstitutionsInMatchUrl(Map<IdType, IdType> theIdSubstitutions, String theMatchUrl) {
		String matchUrl = theMatchUrl;
		if (isNotBlank(matchUrl)) {
			for (Entry<IdType, IdType> nextSubstitutionEntry : theIdSubstitutions.entrySet()) {
				IdType nextTemporaryId = nextSubstitutionEntry.getKey();
				IdType nextReplacementId = nextSubstitutionEntry.getValue();
				String nextTemporaryIdPart = nextTemporaryId.getIdPart();
				String nextReplacementIdPart = nextReplacementId.getValueAsString();
				if (nextTemporaryId.isUrn() && nextTemporaryIdPart.length() > IdType.URN_PREFIX.length()) {
					matchUrl = matchUrl.replace(nextTemporaryIdPart, nextReplacementIdPart);
					matchUrl = matchUrl.replace(UrlUtil.escapeUrlParam(nextTemporaryIdPart), nextReplacementIdPart);
				}
			}
		}
		return matchUrl;
	}

	private void populateEntryWithOperationOutcome(BaseServerResponseException caughtEx, BundleEntryComponent nextEntry) {
		OperationOutcome oo = new OperationOutcome();
		oo.addIssue().setSeverity(IssueSeverity.ERROR).setDiagnostics(caughtEx.getMessage());
		nextEntry.getResponse().setOutcome(oo);
	}

	private ca.uhn.fhir.jpa.dao.IFhirResourceDao<? extends IBaseResource> toDao(UrlParts theParts, String theVerb, String theUrl) {
		RuntimeResourceDefinition resType;
		try {
			resType = getContext().getResourceDefinition(theParts.getResourceType());
		} catch (DataFormatException e) {
			String msg = getContext().getLocalizer().getMessage(BaseHapiFhirSystemDao.class, "transactionInvalidUrl", theVerb, theUrl);
			throw new InvalidRequestException(msg);
		}
		IFhirResourceDao<? extends IBaseResource> dao = null;
		if (resType != null) {
			dao = getDao(resType.getImplementingClass());
		}
		if (dao == null) {
			String msg = getContext().getLocalizer().getMessage(BaseHapiFhirSystemDao.class, "transactionInvalidUrl", theVerb, theUrl);
			throw new InvalidRequestException(msg);
		}

		// if (theParts.getResourceId() == null && theParts.getParams() == null) {
		// String msg = getContext().getLocalizer().getMessage(BaseHapiFhirSystemDao.class, "transactionInvalidUrl", theVerb, theUrl);
		// throw new InvalidRequestException(msg);
		// }

		return dao;
	}

	protected Meta toMeta(Collection<TagDefinition> tagDefinitions) {
		Meta retVal = new Meta();
		for (TagDefinition next : tagDefinitions) {
			switch (next.getTagType()) {
				case PROFILE:
					retVal.addProfile(next.getCode());
					break;
				case SECURITY_LABEL:
					retVal.addSecurity().setSystem(next.getSystem()).setCode(next.getCode()).setDisplay(next.getDisplay());
					break;
				case TAG:
					retVal.addTag().setSystem(next.getSystem()).setCode(next.getCode()).setDisplay(next.getDisplay());
					break;
			}
		}
		return retVal;
	}

	@Transactional(propagation = Propagation.NEVER)
	@Override
	public Bundle transaction(RequestDetails theRequestDetails, Bundle theRequest) {
		if (theRequestDetails != null) {
			ActionRequestDetails requestDetails = new ActionRequestDetails(theRequestDetails, theRequest, "Bundle", null);
			notifyInterceptors(RestOperationTypeEnum.TRANSACTION, requestDetails);
		}

		String actionName = "Transaction";
		return transaction((ServletRequestDetails) theRequestDetails, theRequest, actionName);
	}

	private Bundle transaction(ServletRequestDetails theRequestDetails, Bundle theRequest, String theActionName) {
		super.markRequestAsProcessingSubRequest(theRequestDetails);
		try {
			return doTransaction(theRequestDetails, theRequest, theActionName);
		} finally {
			super.clearRequestAsProcessingSubRequest(theRequestDetails);
		}
	}

	private static void handleTransactionCreateOrUpdateOutcome(Map<IdType, IdType> idSubstitutions, Map<IdType, DaoMethodOutcome> idToPersistedOutcome, IdType nextResourceId, DaoMethodOutcome outcome,
																				  BundleEntryComponent newEntry, String theResourceType, IBaseResource theRes, ServletRequestDetails theRequestDetails) {
		IdType newId = (IdType) outcome.getId().toUnqualifiedVersionless();
		IdType resourceId = isPlaceholder(nextResourceId) ? nextResourceId : nextResourceId.toUnqualifiedVersionless();
		if (newId.equals(resourceId) == false) {
			idSubstitutions.put(resourceId, newId);
			if (isPlaceholder(resourceId)) {
				/*
				 * The correct way for substitution IDs to be is to be with no resource type, but we'll accept the qualified kind too just to be lenient.
				 */
				idSubstitutions.put(new IdType(theResourceType + '/' + resourceId.getValue()), newId);
			}
		}
		idToPersistedOutcome.put(newId, outcome);
		if (outcome.getCreated().booleanValue()) {
			newEntry.getResponse().setStatus(toStatusString(Constants.STATUS_HTTP_201_CREATED));
		} else {
			newEntry.getResponse().setStatus(toStatusString(Constants.STATUS_HTTP_200_OK));
		}
		newEntry.getResponse().setLastModified(((Resource) theRes).getMeta().getLastUpdated());

		if (theRequestDetails != null) {
			if (outcome.getResource() != null) {
				String prefer = theRequestDetails.getHeader(Constants.HEADER_PREFER);
				PreferReturnEnum preferReturn = RestfulServerUtils.parsePreferHeader(prefer);
				if (preferReturn != null) {
					if (preferReturn == PreferReturnEnum.REPRESENTATION) {
						newEntry.setResource((Resource) outcome.getResource());
					}
				}
			}
		}

	}

	private static boolean isPlaceholder(IdType theId) {
		if (theId != null && theId.getValue() != null) {
			if (theId.getValue().startsWith("urn:oid:") || theId.getValue().startsWith("urn:uuid:")) {
				return true;
			}
		}
		return false;
	}

	private static String toStatusString(int theStatusCode) {
		return Integer.toString(theStatusCode) + " " + defaultString(Constants.HTTP_STATUS_NAMES.get(theStatusCode));
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
	public class TransactionSorter implements Comparator<BundleEntryComponent> {

		private Set<String> myPlaceholderIds;

		public TransactionSorter(Set<String> thePlaceholderIds) {
			myPlaceholderIds = thePlaceholderIds;
		}

		@Override
		public int compare(BundleEntryComponent theO1, BundleEntryComponent theO2) {
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

		private String toMatchUrl(BundleEntryComponent theEntry) {
			HTTPVerb verb = theEntry.getRequest().getMethod();
			if (verb == HTTPVerb.POST) {
				return theEntry.getRequest().getIfNoneExist();
			}
			if (verb == HTTPVerb.PUT || verb == HTTPVerb.DELETE) {
				String url = extractTransactionUrlOrThrowException(theEntry, verb);
				UrlParts parts = UrlUtil.parseUrl(url);
				if (isBlank(parts.getResourceId())) {
					return parts.getResourceType() + '?' + parts.getParams();
				}
			}
			return null;
		}

		private int toOrder(BundleEntryComponent theO1) {
			int o1 = 0;
			if (theO1.getRequest().getMethodElement().getValue() != null) {
				switch (theO1.getRequest().getMethodElement().getValue()) {
					case DELETE:
						o1 = 1;
						break;
					case POST:
						o1 = 2;
						break;
					case PUT:
						o1 = 3;
						break;
					case PATCH:
						o1 = 4;
						break;
					case HEAD:
						o1 = 5;
						break;
					case GET:
						o1 = 6;
						break;
					case NULL:
						o1 = 0;
						break;
				}
			}
			return o1;
		}

	}

	//@formatter:off

	private static class BaseServerResponseExceptionHolder {
		private BaseServerResponseException myException;

		public BaseServerResponseException getException() {
			return myException;
		}

		public void setException(BaseServerResponseException myException) {
			this.myException = myException;
		}
	}

}
