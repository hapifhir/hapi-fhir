package ca.uhn.fhir.jpa.dao;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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

import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.api.model.DeleteConflictList;
import ca.uhn.fhir.jpa.api.model.DeleteMethodOutcome;
import ca.uhn.fhir.jpa.delete.DeleteConflictService;
import ca.uhn.fhir.jpa.model.cross.IBasePersistedResource;
import ca.uhn.fhir.jpa.model.cross.ResourcePersistentId;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.model.entity.TagDefinition;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.base.composite.BaseResourceReferenceDt;
import ca.uhn.fhir.model.dstu2.composite.MetaDt;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Bundle.Entry;
import ca.uhn.fhir.model.dstu2.resource.Bundle.EntryResponse;
import ca.uhn.fhir.model.dstu2.resource.OperationOutcome;
import ca.uhn.fhir.model.dstu2.valueset.BundleTypeEnum;
import ca.uhn.fhir.model.dstu2.valueset.HTTPVerbEnum;
import ca.uhn.fhir.model.dstu2.valueset.IssueSeverityEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.model.primitive.UriDt;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.RestfulServerUtils;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.NotModifiedException;
import ca.uhn.fhir.rest.server.interceptor.IServerInterceptor.ActionRequestDetails;
import ca.uhn.fhir.rest.server.method.BaseMethodBinding;
import ca.uhn.fhir.rest.server.method.BaseResourceReturningMethodBinding;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.rest.server.servlet.ServletSubRequestDetails;
import ca.uhn.fhir.util.FhirTerser;
import ca.uhn.fhir.util.UrlUtil;
import ca.uhn.fhir.util.UrlUtil.UrlParts;
import com.google.common.collect.ArrayListMultimap;
import org.apache.http.NameValuePair;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
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

import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class FhirSystemDaoDstu2 extends BaseHapiFhirSystemDao<Bundle, MetaDt> {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirSystemDaoDstu2.class);

	@Autowired
	private PlatformTransactionManager myTxManager;
	@Autowired
	private MatchUrlService myMatchUrlService;
	@Autowired
	private DaoRegistry myDaoRegistry;
	@Autowired
	private MatchResourceUrlService myMatchResourceUrlService;

	private Bundle batch(final RequestDetails theRequestDetails, Bundle theRequest) {
		ourLog.info("Beginning batch with {} resources", theRequest.getEntry().size());
		long start = System.currentTimeMillis();

		TransactionTemplate txTemplate = new TransactionTemplate(myTxManager);
		txTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);

		Bundle resp = new Bundle();
		resp.setType(BundleTypeEnum.BATCH_RESPONSE);

		/*
		 * For batch, we handle each entry as a mini-transaction in its own database transaction so that if one fails, it doesn't prevent others
		 */

		for (final Entry nextRequestEntry : theRequest.getEntry()) {

			TransactionCallback<Bundle> callback = new TransactionCallback<Bundle>() {
				@Override
				public Bundle doInTransaction(TransactionStatus theStatus) {
					Bundle subRequestBundle = new Bundle();
					subRequestBundle.setType(BundleTypeEnum.TRANSACTION);
					subRequestBundle.addEntry(nextRequestEntry);
					return transaction((ServletRequestDetails) theRequestDetails, subRequestBundle, "Batch sub-request");
				}
			};

			BaseServerResponseException caughtEx;
			try {
				Bundle nextResponseBundle;
				if (nextRequestEntry.getRequest().getMethodElement().getValueAsEnum() == HTTPVerbEnum.GET) {
					// Don't process GETs in a transaction because they'll
					// create their own
					nextResponseBundle = callback.doInTransaction(null);
				} else {
					nextResponseBundle = txTemplate.execute(callback);
				}
				caughtEx = null;

				Entry subResponseEntry = nextResponseBundle.getEntry().get(0);
				resp.addEntry(subResponseEntry);
				/*
				 * If the individual entry didn't have a resource in its response, bring the sub-transaction's OperationOutcome across so the client can see it
				 */
				if (subResponseEntry.getResource() == null) {
					subResponseEntry.setResource(nextResponseBundle.getEntry().get(0).getResource());
				}

			} catch (BaseServerResponseException e) {
				caughtEx = e;
			} catch (Throwable t) {
				ourLog.error("Failure during BATCH sub transaction processing", t);
				caughtEx = new InternalErrorException(t);
			}

			if (caughtEx != null) {
				Entry nextEntry = resp.addEntry();

				OperationOutcome oo = new OperationOutcome();
				oo.addIssue().setSeverity(IssueSeverityEnum.ERROR).setDiagnostics(caughtEx.getMessage());
				nextEntry.setResource(oo);

				EntryResponse nextEntryResp = nextEntry.getResponse();
				nextEntryResp.setStatus(toStatusString(caughtEx.getStatusCode()));
			}

		}

		long delay = System.currentTimeMillis() - start;
		ourLog.info("Batch completed in {}ms", new Object[] {delay});

		return resp;
	}

	@SuppressWarnings("unchecked")
	private Bundle doTransaction(ServletRequestDetails theRequestDetails, Bundle theRequest, String theActionName) {
		BundleTypeEnum transactionType = theRequest.getTypeElement().getValueAsEnum();
		if (transactionType == BundleTypeEnum.BATCH) {
			return batch(theRequestDetails, theRequest);
		}

		return doTransaction(theRequestDetails, theRequest, theActionName, transactionType);
	}

	private Bundle doTransaction(ServletRequestDetails theRequestDetails, Bundle theRequest, String theActionName, BundleTypeEnum theTransactionType) {
		if (theTransactionType == null) {
			String message = "Transaction Bundle did not specify valid Bundle.type, assuming " + BundleTypeEnum.TRANSACTION.getCode();
			ourLog.warn(message);
			theTransactionType = BundleTypeEnum.TRANSACTION;
		}
		if (theTransactionType != BundleTypeEnum.TRANSACTION) {
			throw new InvalidRequestException("Unable to process transaction where incoming Bundle.type = " + theTransactionType.getCode());
		}

		ourLog.info("Beginning {} with {} resources", theActionName, theRequest.getEntry().size());

		long start = System.currentTimeMillis();
		Date updateTime = new Date();

		Set<IdDt> allIds = new LinkedHashSet<IdDt>();
		Map<IdDt, IdDt> idSubstitutions = new HashMap<IdDt, IdDt>();
		Map<IdDt, DaoMethodOutcome> idToPersistedOutcome = new HashMap<IdDt, DaoMethodOutcome>();

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
		Bundle response = new Bundle();
		List<Entry> getEntries = new ArrayList<Entry>();
		IdentityHashMap<Entry, Integer> originalRequestOrder = new IdentityHashMap<Entry, Integer>();
		for (int i = 0; i < theRequest.getEntry().size(); i++) {
			originalRequestOrder.put(theRequest.getEntry().get(i), i);
			response.addEntry();
			if (theRequest.getEntry().get(i).getRequest().getMethodElement().getValueAsEnum() == HTTPVerbEnum.GET) {
				getEntries.add(theRequest.getEntry().get(i));
			}
		}
		Collections.sort(theRequest.getEntry(), new TransactionSorter());

		List<IIdType> deletedResources = new ArrayList<>();
		DeleteConflictList deleteConflicts = new DeleteConflictList();
		Map<Entry, IBasePersistedResource> entriesToProcess = new IdentityHashMap<>();
		Set<IBasePersistedResource> nonUpdatedEntities = new HashSet<>();
		Set<IBasePersistedResource> updatedEntities = new HashSet<>();

		/*
		 * Handle: GET/PUT/POST
		 */
		TransactionTemplate txTemplate = new TransactionTemplate(myTxManager);
		txTemplate.execute(t->{
			handleTransactionWriteOperations(theRequestDetails, theRequest, theActionName, updateTime, allIds, idSubstitutions, idToPersistedOutcome, response, originalRequestOrder, deletedResources, deleteConflicts, entriesToProcess, nonUpdatedEntities, updatedEntities);
			return null;
		});

		/*
		 * Loop through the request and process any entries of type GET
		 */
		for (int i = 0; i < getEntries.size(); i++) {
			Entry nextReqEntry = getEntries.get(i);
			Integer originalOrder = originalRequestOrder.get(nextReqEntry);
			Entry nextRespEntry = response.getEntry().get(originalOrder);

			ServletSubRequestDetails requestDetails = new ServletSubRequestDetails(theRequestDetails);
			requestDetails.setServletRequest(theRequestDetails.getServletRequest());
			requestDetails.setRequestType(RequestTypeEnum.GET);
			requestDetails.setServer(theRequestDetails.getServer());

			String url = extractTransactionUrlOrThrowException(nextReqEntry, HTTPVerbEnum.GET);

			int qIndex = url.indexOf('?');
			ArrayListMultimap<String, String> paramValues = ArrayListMultimap.create();
			requestDetails.setParameters(new HashMap<String, String[]>());
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

			if (isNotBlank(nextReqEntry.getRequest().getIfMatch())) {
				requestDetails.addHeader(Constants.HEADER_IF_MATCH, nextReqEntry.getRequest().getIfMatch());
			}
			if (isNotBlank(nextReqEntry.getRequest().getIfNoneExist())) {
				requestDetails.addHeader(Constants.HEADER_IF_NONE_EXIST, nextReqEntry.getRequest().getIfNoneExist());
			}
			if (isNotBlank(nextReqEntry.getRequest().getIfNoneMatch())) {
				requestDetails.addHeader(Constants.HEADER_IF_NONE_MATCH, nextReqEntry.getRequest().getIfNoneMatch());
			}

			if (method instanceof BaseResourceReturningMethodBinding) {
				try {
					IBaseResource resource = ((BaseResourceReturningMethodBinding) method).doInvokeServer(theRequestDetails.getServer(), requestDetails);
					if (paramValues.containsKey(Constants.PARAM_SUMMARY) || paramValues.containsKey(Constants.PARAM_CONTENT)) {
						resource = filterNestedBundle(requestDetails, resource);
					}
					nextRespEntry.setResource((IResource) resource);
					nextRespEntry.getResponse().setStatus(toStatusString(Constants.STATUS_HTTP_200_OK));
				} catch (NotModifiedException e) {
					nextRespEntry.getResponse().setStatus(toStatusString(Constants.STATUS_HTTP_304_NOT_MODIFIED));
				}
			} else {
				throw new IllegalArgumentException("Unable to handle GET " + url);
			}

		}

		for (Map.Entry<Entry, IBasePersistedResource> nextEntry : entriesToProcess.entrySet()) {
			nextEntry.getKey().getResponse().setLocation(nextEntry.getValue().getIdDt().toUnqualified().getValue());
			nextEntry.getKey().getResponse().setEtag(nextEntry.getValue().getIdDt().getVersionIdPart());
		}

		long delay = System.currentTimeMillis() - start;
		int numEntries = theRequest.getEntry().size();
		long delayPer = delay / numEntries;
		ourLog.info("{} completed in {}ms ({} entries at {}ms per entry)", theActionName, delay, numEntries, delayPer);

		response.setType(BundleTypeEnum.TRANSACTION_RESPONSE);
		return response;
	}

	private void handleTransactionWriteOperations(ServletRequestDetails theRequestDetails, Bundle theRequest, String theActionName, Date theUpdateTime, Set<IdDt> theAllIds, Map<IdDt, IdDt> theIdSubstitutions, Map<IdDt, DaoMethodOutcome> theIdToPersistedOutcome, Bundle theResponse, IdentityHashMap<Entry, Integer> theOriginalRequestOrder, List<IIdType> theDeletedResources, DeleteConflictList theDeleteConflicts, Map<Entry, IBasePersistedResource> theEntriesToProcess, Set<IBasePersistedResource> theNonUpdatedEntities, Set<IBasePersistedResource> theUpdatedEntities) {
		/*
		 * Loop through the request and process any entries of type
		 * PUT, POST or DELETE
		 */
		for (int i = 0; i < theRequest.getEntry().size(); i++) {

			if (i % 100 == 0) {
				ourLog.debug("Processed {} non-GET entries out of {}", i, theRequest.getEntry().size());
			}

			Entry nextReqEntry = theRequest.getEntry().get(i);
			IResource res = nextReqEntry.getResource();
			IdDt nextResourceId = null;
			if (res != null) {

				nextResourceId = res.getId();

				if (!nextResourceId.hasIdPart()) {
					if (isNotBlank(nextReqEntry.getFullUrl())) {
						nextResourceId = new IdDt(nextReqEntry.getFullUrl());
					}
				}

				if (nextResourceId.hasIdPart() && nextResourceId.getIdPart().matches("[a-zA-Z]+:.*") && !isPlaceholder(nextResourceId)) {
					throw new InvalidRequestException("Invalid placeholder ID found: " + nextResourceId.getIdPart() + " - Must be of the form 'urn:uuid:[uuid]' or 'urn:oid:[oid]'");
				}

				if (nextResourceId.hasIdPart() && !nextResourceId.hasResourceType() && !isPlaceholder(nextResourceId)) {
					nextResourceId = new IdDt(toResourceName(res.getClass()), nextResourceId.getIdPart());
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
					IdDt nextId = nextResourceId.toUnqualifiedVersionless();
					if (!theAllIds.add(nextId)) {
						throw new InvalidRequestException(getContext().getLocalizer().getMessage(BaseHapiFhirSystemDao.class, "transactionContainsMultipleWithDuplicateId", nextId));
					}
				}

			}

			HTTPVerbEnum verb = nextReqEntry.getRequest().getMethodElement().getValueAsEnum();
			if (verb == null) {
				throw new InvalidRequestException(getContext().getLocalizer().getMessage(BaseHapiFhirSystemDao.class, "transactionEntryHasInvalidVerb", nextReqEntry.getRequest().getMethod()));
			}

			String resourceType = res != null ? getContext().getResourceDefinition(res).getName() : null;
			Entry nextRespEntry = theResponse.getEntry().get(theOriginalRequestOrder.get(nextReqEntry));

			switch (verb) {
				case POST: {
					// CREATE
					@SuppressWarnings("rawtypes")
					IFhirResourceDao resourceDao = myDaoRegistry.getResourceDao(res.getClass());
					res.setId((String) null);
					DaoMethodOutcome outcome;
					outcome = resourceDao.create(res, nextReqEntry.getRequest().getIfNoneExist(), false, theUpdateTime, theRequestDetails);
					handleTransactionCreateOrUpdateOutcome(theIdSubstitutions, theIdToPersistedOutcome, nextResourceId, outcome, nextRespEntry, resourceType, res);
					theEntriesToProcess.put(nextRespEntry, outcome.getEntity());
					if (outcome.getCreated() == false) {
						theNonUpdatedEntities.add(outcome.getEntity());
					}
					break;
				}
				case DELETE: {
					// DELETE
					String url = extractTransactionUrlOrThrowException(nextReqEntry, verb);
					UrlParts parts = UrlUtil.parseUrl(url);
					IFhirResourceDao<? extends IBaseResource> dao = toDao(parts, verb.getCode(), url);
					int status = Constants.STATUS_HTTP_204_NO_CONTENT;
					if (parts.getResourceId() != null) {
						DaoMethodOutcome outcome = dao.delete(new IdDt(parts.getResourceType(), parts.getResourceId()), theDeleteConflicts, theRequestDetails);
						if (outcome.getEntity() != null) {
							theDeletedResources.add(outcome.getId().toUnqualifiedVersionless());
							theEntriesToProcess.put(nextRespEntry, outcome.getEntity());
						}
					} else {
						DeleteMethodOutcome deleteOutcome = dao.deleteByUrl(parts.getResourceType() + '?' + parts.getParams(), theDeleteConflicts, theRequestDetails);
						List<ResourceTable> allDeleted = deleteOutcome.getDeletedEntities();
						for (ResourceTable deleted : allDeleted) {
							theDeletedResources.add(deleted.getIdDt().toUnqualifiedVersionless());
						}
						if (allDeleted.isEmpty()) {
							status = Constants.STATUS_HTTP_404_NOT_FOUND;
						}
					}

					nextRespEntry.getResponse().setStatus(toStatusString(status));
					break;
				}
				case PUT: {
					// UPDATE
					@SuppressWarnings("rawtypes")
					IFhirResourceDao resourceDao = myDaoRegistry.getResourceDao(res.getClass());

					DaoMethodOutcome outcome;

					String url = extractTransactionUrlOrThrowException(nextReqEntry, verb);

					UrlParts parts = UrlUtil.parseUrl(url);
					if (isNotBlank(parts.getResourceId())) {
						res.setId(new IdDt(parts.getResourceType(), parts.getResourceId()));
						outcome = resourceDao.update(res, null, false, theRequestDetails);
					} else {
						res.setId((String) null);
						outcome = resourceDao.update(res, parts.getResourceType() + '?' + parts.getParams(), false, theRequestDetails);
					}

					if (outcome.getCreated() == Boolean.FALSE) {
						theUpdatedEntities.add(outcome.getEntity());
					}

					handleTransactionCreateOrUpdateOutcome(theIdSubstitutions, theIdToPersistedOutcome, nextResourceId, outcome, nextRespEntry, resourceType, res);
					theEntriesToProcess.put(nextRespEntry, outcome.getEntity());
					break;
				}
				case GET:
					break;
			}
		}

		/*
		 * Make sure that there are no conflicts from deletions. E.g. we can't delete something
		 * if something else has a reference to it.. Unless the thing that has a reference to it
		 * was also deleted as a part of this transaction, which is why we check this now at the
		 * end.
		 */

		theDeleteConflicts.removeIf(next -> theDeletedResources.contains(next.getTargetId().toVersionless()));
		DeleteConflictService.validateDeleteConflictsEmptyOrThrowException(getContext(), theDeleteConflicts);

		/*
		 * Perform ID substitutions and then index each resource we have saved
		 */

		FhirTerser terser = getContext().newTerser();
		for (DaoMethodOutcome nextOutcome : theIdToPersistedOutcome.values()) {
			IResource nextResource = (IResource) nextOutcome.getResource();
			if (nextResource == null) {
				continue;
			}

			// References
			List<BaseResourceReferenceDt> allRefs = terser.getAllPopulatedChildElementsOfType(nextResource, BaseResourceReferenceDt.class);
			for (BaseResourceReferenceDt nextRef : allRefs) {
				IdDt nextId = nextRef.getReference();
				if (!nextId.hasIdPart()) {
					continue;
				}
				if (theIdSubstitutions.containsKey(nextId)) {
					IdDt newId = theIdSubstitutions.get(nextId);
					ourLog.debug(" * Replacing resource ref {} with {}", nextId, newId);
					nextRef.setReference(newId);
				} else {
					ourLog.debug(" * Reference [{}] does not exist in bundle", nextId);
				}
			}

			// URIs
			List<UriDt> allUris = terser.getAllPopulatedChildElementsOfType(nextResource, UriDt.class);
			for (UriDt nextRef : allUris) {
				if (nextRef instanceof IIdType) {
					continue; // No substitution on the resource ID itself!
				}
				IdDt nextUriString = new IdDt(nextRef.getValueAsString());
				if (theIdSubstitutions.containsKey(nextUriString)) {
					IdDt newId = theIdSubstitutions.get(nextUriString);
					ourLog.debug(" * Replacing resource ref {} with {}", nextUriString, newId);
					nextRef.setValue(newId.getValue());
				} else {
					ourLog.debug(" * Reference [{}] does not exist in bundle", nextUriString);
				}
			}


			InstantDt deletedInstantOrNull = ResourceMetadataKeyEnum.DELETED_AT.get(nextResource);
			Date deletedTimestampOrNull = deletedInstantOrNull != null ? deletedInstantOrNull.getValue() : null;
			if (theUpdatedEntities.contains(nextOutcome.getEntity())) {
				updateInternal(theRequestDetails, nextResource, true, false, nextOutcome.getEntity(), nextResource.getIdElement(), nextOutcome.getPreviousResource());
			} else if (!theNonUpdatedEntities.contains(nextOutcome.getEntity())) {
				updateEntity(theRequestDetails, nextResource, nextOutcome.getEntity(), deletedTimestampOrNull, true, false, theUpdateTime, false, true);
			}
		}

		myEntityManager.flush();

		/*
		 * Double check we didn't allow any duplicates we shouldn't have
		 */
		for (Entry nextEntry : theRequest.getEntry()) {
			if (nextEntry.getRequest().getMethodElement().getValueAsEnum() == HTTPVerbEnum.POST) {
				String matchUrl = nextEntry.getRequest().getIfNoneExist();
				if (isNotBlank(matchUrl)) {
					Class<? extends IBaseResource> resType = nextEntry.getResource().getClass();
					Set<ResourcePersistentId> val = myMatchResourceUrlService.processMatchUrl(matchUrl, resType, theRequestDetails);
					if (val.size() > 1) {
						throw new InvalidRequestException(
							"Unable to process " + theActionName + " - Request would cause multiple resources to match URL: \"" + matchUrl + "\". Does transaction request contain duplicates?");
					}
				}
			}
		}

		for (IdDt next : theAllIds) {
			IdDt replacement = theIdSubstitutions.get(next);
			if (replacement == null) {
				continue;
			}
			if (replacement.equals(next)) {
				continue;
			}
			ourLog.debug("Placeholder resource ID \"{}\" was replaced with permanent ID \"{}\"", next, replacement);
		}
	}

	private String extractTransactionUrlOrThrowException(Entry nextEntry, HTTPVerbEnum verb) {
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

	@Override
	public MetaDt metaGetOperation(RequestDetails theRequestDetails) {
		// Notify interceptors
		ActionRequestDetails requestDetails = new ActionRequestDetails(theRequestDetails);
		notifyInterceptors(RestOperationTypeEnum.META, requestDetails);

		String sql = "SELECT d FROM TagDefinition d WHERE d.myId IN (SELECT DISTINCT t.myTagId FROM ResourceTag t)";
		TypedQuery<TagDefinition> q = myEntityManager.createQuery(sql, TagDefinition.class);
		List<TagDefinition> tagDefinitions = q.getResultList();

		MetaDt retVal = toMetaDt(tagDefinitions);

		return retVal;
	}

	private IFhirResourceDao<? extends IBaseResource> toDao(UrlParts theParts, String theVerb, String theUrl) {
		RuntimeResourceDefinition resType;
		try {
			resType = getContext().getResourceDefinition(theParts.getResourceType());
		} catch (DataFormatException e) {
			String msg = getContext().getLocalizer().getMessage(BaseHapiFhirSystemDao.class, "transactionInvalidUrl", theVerb, theUrl);
			throw new InvalidRequestException(msg);
		}
		IFhirResourceDao<? extends IBaseResource> dao = null;
		if (resType != null) {
			dao = this.myDaoRegistry.getResourceDaoOrNull(resType.getImplementingClass());
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

	protected MetaDt toMetaDt(Collection<TagDefinition> tagDefinitions) {
		MetaDt retVal = new MetaDt();
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
		markRequestAsProcessingSubRequest(theRequestDetails);
		try {
			return doTransaction(theRequestDetails, theRequest, theActionName);
		} finally {
			clearRequestAsProcessingSubRequest(theRequestDetails);
		}
	}

	private static void handleTransactionCreateOrUpdateOutcome(Map<IdDt, IdDt> idSubstitutions, Map<IdDt, DaoMethodOutcome> idToPersistedOutcome, IdDt nextResourceId, DaoMethodOutcome outcome,
																				  Entry newEntry, String theResourceType, IResource theRes) {
		IdDt newId = (IdDt) outcome.getId().toUnqualifiedVersionless();
		IdDt resourceId = isPlaceholder(nextResourceId) ? nextResourceId : nextResourceId.toUnqualifiedVersionless();
		if (newId.equals(resourceId) == false) {
			idSubstitutions.put(resourceId, newId);
			if (isPlaceholder(resourceId)) {
				/*
				 * The correct way for substitution IDs to be is to be with no resource type, but we'll accept the qualified kind too just to be lenient.
				 */
				idSubstitutions.put(new IdDt(theResourceType + '/' + resourceId.getValue()), newId);
			}
		}
		idToPersistedOutcome.put(newId, outcome);
		if (outcome.getCreated().booleanValue()) {
			newEntry.getResponse().setStatus(toStatusString(Constants.STATUS_HTTP_201_CREATED));
		} else {
			newEntry.getResponse().setStatus(toStatusString(Constants.STATUS_HTTP_200_OK));
		}
		newEntry.getResponse().setLastModified(ResourceMetadataKeyEnum.UPDATED.get(theRes));
	}

	private static boolean isPlaceholder(IdDt theId) {
		if (theId.getValue() != null) {
			return theId.getValue().startsWith("urn:oid:") || theId.getValue().startsWith("urn:uuid:");
		}
		return false;
	}

	private static String toStatusString(int theStatusCode) {
		return theStatusCode + " " + defaultString(Constants.HTTP_STATUS_NAMES.get(theStatusCode));
	}

	@Override
	public IBaseBundle processMessage(RequestDetails theRequestDetails, IBaseBundle theMessage) {
		return FhirResourceDaoMessageHeaderDstu2.throwProcessMessageNotImplemented();
	}


	/**
	 * Transaction Order, per the spec:
	 * <p>
	 * Process any DELETE interactions
	 * Process any POST interactions
	 * Process any PUT interactions
	 * Process any GET interactions
	 */
	public class TransactionSorter implements Comparator<Entry> {

		@Override
		public int compare(Entry theO1, Entry theO2) {
			int o1 = toOrder(theO1);
			int o2 = toOrder(theO2);

			return o1 - o2;
		}

		private int toOrder(Entry theO1) {
			int o1 = 0;
			if (theO1.getRequest().getMethodElement().getValueAsEnum() != null) {
				switch (theO1.getRequest().getMethodElement().getValueAsEnum()) {
					case DELETE:
						o1 = 1;
						break;
					case POST:
						o1 = 2;
						break;
					case PUT:
						o1 = 3;
						break;
					case GET:
						o1 = 4;
						break;
				}
			}
			return o1;
		}

	}

}
