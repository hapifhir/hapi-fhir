package ca.uhn.fhir.jpa.dao;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2016 University Health Network
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
import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.TypedQuery;

import org.apache.http.NameValuePair;
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

import com.google.common.collect.ArrayListMultimap;

import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.jpa.entity.ResourceTable;
import ca.uhn.fhir.jpa.entity.TagDefinition;
import ca.uhn.fhir.jpa.provider.ServletSubRequestDetails;
import ca.uhn.fhir.jpa.util.DeleteConflict;
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
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.method.BaseMethodBinding;
import ca.uhn.fhir.rest.method.BaseResourceReturningMethodBinding;
import ca.uhn.fhir.rest.method.BaseResourceReturningMethodBinding.ResourceOrDstu1Bundle;
import ca.uhn.fhir.rest.method.RequestDetails;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.RestfulServerUtils;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.NotModifiedException;
import ca.uhn.fhir.rest.server.interceptor.IServerInterceptor.ActionRequestDetails;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.util.FhirTerser;
import ca.uhn.fhir.util.UrlUtil;
import ca.uhn.fhir.util.UrlUtil.UrlParts;

public class FhirSystemDaoDstu2 extends BaseHapiFhirSystemDao<Bundle, MetaDt> {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirSystemDaoDstu2.class);

	@Autowired
	private PlatformTransactionManager myTxManager;

	private Bundle batch(final RequestDetails theRequestDetails, Bundle theRequest) {
		ourLog.info("Beginning batch with {} resources", theRequest.getEntry().size());
		long start = System.currentTimeMillis();

		TransactionTemplate txTemplate = new TransactionTemplate(myTxManager);
		txTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);

		Bundle resp = new Bundle();
		resp.setType(BundleTypeEnum.BATCH_RESPONSE);
		OperationOutcome ooResp = new OperationOutcome();
		resp.addEntry().setResource(ooResp);

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

					Bundle subResponseBundle = transaction((ServletRequestDetails) theRequestDetails, subRequestBundle, "Batch sub-request");
					return subResponseBundle;
				}
			};

			BaseServerResponseException caughtEx;
			try {
				Bundle nextResponseBundle = txTemplate.execute(callback);
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
		ourLog.info("Batch completed in {}ms", new Object[] { delay });
		ooResp.addIssue().setSeverity(IssueSeverityEnum.INFORMATION).setDiagnostics("Batch completed in " + delay + "ms");

		return resp;
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
	 * 
	 * TODO: This isn't the most efficient way of doing this.. hopefully we can come up with something better in the future.
	 */
	private IBaseResource filterNestedBundle(RequestDetails theRequestDetails, IBaseResource theResource) {
		IParser p = getContext().newJsonParser();
		RestfulServerUtils.configureResponseParser(theRequestDetails, p);
		return p.parseResource(theResource.getClass(), p.encodeResourceToString(theResource));
	}

	private IFhirResourceDao<?> getDaoOrThrowException(Class<? extends IResource> theClass) {
		IFhirResourceDao<? extends IResource> retVal = getDao(theClass);
		if (retVal == null) {
			throw new InvalidRequestException("Unable to process request, this server does not know how to handle resources of type " + getContext().getResourceDefinition(theClass).getName());
		}
		return retVal;
	}

	@Override
	public MetaDt metaGetOperation(RequestDetails theRequestDetails) {
		// Notify interceptors
		ActionRequestDetails requestDetails = new ActionRequestDetails(null, null, getContext(), theRequestDetails);
		notifyInterceptors(RestOperationTypeEnum.META, requestDetails);

		String sql = "SELECT d FROM TagDefinition d WHERE d.myId IN (SELECT DISTINCT t.myTagId FROM ResourceTag t)";
		TypedQuery<TagDefinition> q = myEntityManager.createQuery(sql, TagDefinition.class);
		List<TagDefinition> tagDefinitions = q.getResultList();

		MetaDt retVal = toMetaDt(tagDefinitions);

		return retVal;
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

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public Bundle transaction(RequestDetails theRequestDetails, Bundle theRequest) {
		ActionRequestDetails requestDetails = new ActionRequestDetails(null, "Bundle", theRequest, getContext(), theRequestDetails);
		notifyInterceptors(RestOperationTypeEnum.TRANSACTION, requestDetails);

		String actionName = "Transaction";
		return transaction((ServletRequestDetails) theRequestDetails, theRequest, actionName);
	}

	@SuppressWarnings("unchecked")
	private Bundle transaction(ServletRequestDetails theRequestDetails, Bundle theRequest, String theActionName) {
		BundleTypeEnum transactionType = theRequest.getTypeElement().getValueAsEnum();
		if (transactionType == BundleTypeEnum.BATCH) {
			return batch(theRequestDetails, theRequest);
		}

		if (transactionType == null) {
			String message = "Transactiion Bundle did not specify valid Bundle.type, assuming " + BundleTypeEnum.TRANSACTION.getCode();
			ourLog.warn(message);
			transactionType = BundleTypeEnum.TRANSACTION;
		}
		if (transactionType != BundleTypeEnum.TRANSACTION) {
			throw new InvalidRequestException("Unable to process transaction where incoming Bundle.type = " + transactionType.getCode());
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
		IdentityHashMap<Entry, Integer> originalRequestOrder = new IdentityHashMap<Bundle.Entry, Integer>();
		for (int i = 0; i < theRequest.getEntry().size(); i++) {
			originalRequestOrder.put(theRequest.getEntry().get(i), i);
			response.addEntry();
			if (theRequest.getEntry().get(i).getRequest().getMethodElement().getValueAsEnum() == HTTPVerbEnum.GET) {
				getEntries.add(theRequest.getEntry().get(i));
			}
		}
		Collections.sort(theRequest.getEntry(), new TransactionSorter());
		
		List<IIdType> deletedResources = new ArrayList<IIdType>();
		List<DeleteConflict> deleteConflicts = new ArrayList<DeleteConflict>();
		
		/*
		 * Loop through the request and process any entries of type
		 * PUT, POST or DELETE
		 */
		for (int i = 0; i < theRequest.getEntry().size(); i++) {

			if (i % 100 == 0) {
				ourLog.info("Processed {} non-GET entries out of {}", i, theRequest.getEntry().size());
			}

			Entry nextReqEntry = theRequest.getEntry().get(i);
			IResource res = nextReqEntry.getResource();
			IdDt nextResourceId = null;
			if (res != null) {

				nextResourceId = res.getId();

				if (nextResourceId.hasIdPart() == false) {
					if (isNotBlank(nextReqEntry.getFullUrl())) {
						nextResourceId = new IdDt(nextReqEntry.getFullUrl());
					}
				}

				if (nextResourceId.hasIdPart() && nextResourceId.getIdPart().matches("[a-zA-Z]+\\:.*") && !isPlaceholder(nextResourceId)) {
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
					if (!allIds.add(nextResourceId)) {
						throw new InvalidRequestException(getContext().getLocalizer().getMessage(BaseHapiFhirSystemDao.class, "transactionContainsMultipleWithDuplicateId", nextResourceId));
					}
				} else if (nextResourceId.hasResourceType() && nextResourceId.hasIdPart()) {
					IdDt nextId = nextResourceId.toUnqualifiedVersionless();
					if (!allIds.add(nextId)) {
						throw new InvalidRequestException(getContext().getLocalizer().getMessage(BaseHapiFhirSystemDao.class, "transactionContainsMultipleWithDuplicateId", nextId));
					}
				}

			}

			HTTPVerbEnum verb = nextReqEntry.getRequest().getMethodElement().getValueAsEnum();
			if (verb == null) {
				throw new InvalidRequestException(getContext().getLocalizer().getMessage(BaseHapiFhirSystemDao.class, "transactionEntryHasInvalidVerb", nextReqEntry.getRequest().getMethod()));
			}

			String resourceType = res != null ? getContext().getResourceDefinition(res).getName() : null;
			Entry nextRespEntry = response.getEntry().get(originalRequestOrder.get(nextReqEntry));

			switch (verb) {
			case POST: {
				// CREATE
				@SuppressWarnings("rawtypes")
				IFhirResourceDao resourceDao = getDaoOrThrowException(res.getClass());
				res.setId((String) null);
				DaoMethodOutcome outcome;
				outcome = resourceDao.create(res, nextReqEntry.getRequest().getIfNoneExist(), false, theRequestDetails);
				handleTransactionCreateOrUpdateOutcome(idSubstitutions, idToPersistedOutcome, nextResourceId, outcome, nextRespEntry, resourceType, res);
				break;
			}
			case DELETE: {
				// DELETE
				String url = extractTransactionUrlOrThrowException(nextReqEntry, verb);
				UrlParts parts = UrlUtil.parseUrl(url);
				ca.uhn.fhir.jpa.dao.IFhirResourceDao<? extends IBaseResource> dao = toDao(parts, verb.getCode(), url);
				int status = Constants.STATUS_HTTP_204_NO_CONTENT;
				if (parts.getResourceId() != null) {
					ResourceTable deleted = dao.delete(new IdDt(parts.getResourceType(), parts.getResourceId()), deleteConflicts, theRequestDetails);
					if (deleted != null) {
						deletedResources.add(deleted.getIdDt().toUnqualifiedVersionless());
					}
				} else {
					List<ResourceTable> allDeleted = dao.deleteByUrl(parts.getResourceType() + '?' + parts.getParams(), deleteConflicts, theRequestDetails);
					for (ResourceTable deleted : allDeleted) {
						deletedResources.add(deleted.getIdDt().toUnqualifiedVersionless());						
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
				IFhirResourceDao resourceDao = getDaoOrThrowException(res.getClass());

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

				handleTransactionCreateOrUpdateOutcome(idSubstitutions, idToPersistedOutcome, nextResourceId, outcome, nextRespEntry, resourceType, res);
				break;
			}
			}
		}

		/*
		 * Make sure that there are no conflicts from deletions. E.g. we can't delete something
		 * if something else has a reference to it.. Unless the thing that has a reference to it
		 * was also deleted as a part of this transaction, which is why we check this now at the 
		 * end.
		 */
		
		for (Iterator<DeleteConflict> iter = deleteConflicts.iterator(); iter.hasNext(); ) {
			DeleteConflict next = iter.next();
			if (deletedResources.contains(next.getTargetId().toVersionless())) {
				iter.remove();
			}
		}
		validateDeleteConflictsEmptyOrThrowException(deleteConflicts);

		/*
		 * Perform ID substitutions and then index each resource we have saved
		 */

		FhirTerser terser = getContext().newTerser();
		for (DaoMethodOutcome nextOutcome : idToPersistedOutcome.values()) {
			IResource nextResource = (IResource) nextOutcome.getResource();
			if (nextResource == null) {
				continue;
			}

			List<BaseResourceReferenceDt> allRefs = terser.getAllPopulatedChildElementsOfType(nextResource, BaseResourceReferenceDt.class);
			for (BaseResourceReferenceDt nextRef : allRefs) {
				IdDt nextId = nextRef.getReference();
				if (idSubstitutions.containsKey(nextId)) {
					IdDt newId = idSubstitutions.get(nextId);
					ourLog.info(" * Replacing resource ref {} with {}", nextId, newId);
					nextRef.setReference(newId);
				} else {
					ourLog.debug(" * Reference [{}] does not exist in bundle", nextId);
				}
			}

			InstantDt deletedInstantOrNull = ResourceMetadataKeyEnum.DELETED_AT.get(nextResource);
			Date deletedTimestampOrNull = deletedInstantOrNull != null ? deletedInstantOrNull.getValue() : null;
			updateEntity(nextResource, nextOutcome.getEntity(), false, deletedTimestampOrNull, true, false, updateTime, theRequestDetails);
		}

		myEntityManager.flush();

		/*
		 * Double check we didn't allow any duplicates we shouldn't have
		 */
		for (Entry nextEntry : theRequest.getEntry()) {
			if (nextEntry.getRequest().getMethodElement().getValueAsEnum() == HTTPVerbEnum.POST) {
				String matchUrl = nextEntry.getRequest().getIfNoneExist();
				if (isNotBlank(matchUrl)) {
					IFhirResourceDao<?> resourceDao = getDao(nextEntry.getResource().getClass());
					Set<Long> val = resourceDao.processMatchUrl(matchUrl);
					if (val.size() > 1) {
						throw new InvalidRequestException(
								"Unable to process " + theActionName + " - Request would cause multiple resources to match URL: \"" + matchUrl + "\". Does transaction request contain duplicates?");
					}
				}
			}
		}

		for (IdDt next : allIds) {
			IdDt replacement = idSubstitutions.get(next);
			if (replacement == null) {
				continue;
			}
			if (replacement.equals(next)) {
				continue;
			}
			ourLog.info("Placeholder resource ID \"{}\" was replaced with permanent ID \"{}\"", next, replacement);
		}

		/*
		 * Loop through the request and process any entries of type GET
		 */
		for (int i = 0; i < getEntries.size(); i++) {
			Entry nextReqEntry = getEntries.get(i);
			Integer originalOrder = originalRequestOrder.get(nextReqEntry);
			Entry nextRespEntry = response.getEntry().get(originalOrder);

			ServletSubRequestDetails requestDetails = new ServletSubRequestDetails();
			requestDetails.setServletRequest(theRequestDetails.getServletRequest());
			requestDetails.setRequestType(RequestTypeEnum.GET);
			requestDetails.setServer(theRequestDetails.getServer());
			
			String url = extractTransactionUrlOrThrowException(nextReqEntry, HTTPVerbEnum.GET);
			
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
					requestDetails.getParameters().put(nextParamEntry.getKey(), nextValue);
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
					ResourceOrDstu1Bundle responseData = ((BaseResourceReturningMethodBinding) method).invokeServer(theRequestDetails.getServer(), requestDetails, new byte[0]);
					IBaseResource resource = responseData.getResource();
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

		ourLog.info("Flushing context after {}", theActionName);
		myEntityManager.flush();
		
		long delay = System.currentTimeMillis() - start;
		int numEntries = theRequest.getEntry().size();
		long delayPer = delay / numEntries;
		ourLog.info("{} completed in {}ms ({} entries at {}ms per entry)", new Object[] { theActionName , delay, numEntries, delayPer });

		response.setType(BundleTypeEnum.TRANSACTION_RESPONSE);
		return response;
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
		newEntry.getResponse().setLocation(outcome.getId().toUnqualified().getValue());
		newEntry.getResponse().setEtag(outcome.getId().getVersionIdPart());
		newEntry.getResponse().setLastModified(ResourceMetadataKeyEnum.UPDATED.get(theRes));
	}

	private static boolean isPlaceholder(IdDt theId) {
		if ("urn:oid:".equals(theId.getBaseUrl()) || "urn:uuid:".equals(theId.getBaseUrl())) {
			return true;
		}
		return false;
	}

	private static String toStatusString(int theStatusCode) {
		return Integer.toString(theStatusCode) + " " + defaultString(Constants.HTTP_STATUS_NAMES.get(theStatusCode));
	}

	//@formatter:off
	/**
	 * Transaction Order, per the spec:
	 * 
	 * Process any DELETE interactions
	 * Process any POST interactions
	 * Process any PUT interactions
	 * Process any GET interactions
	 */
	//@formatter:off
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
