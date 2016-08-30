package ca.uhn.fhir.jpa.dao.dstu3;

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
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.persistence.TypedQuery;

import org.apache.http.NameValuePair;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.dstu3.model.Bundle.BundleEntryResponseComponent;
import org.hl7.fhir.dstu3.model.Bundle.BundleType;
import org.hl7.fhir.dstu3.model.Bundle.HTTPVerb;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.Meta;
import org.hl7.fhir.dstu3.model.OperationOutcome;
import org.hl7.fhir.dstu3.model.OperationOutcome.IssueSeverity;
import org.hl7.fhir.dstu3.model.Resource;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
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
import ca.uhn.fhir.jpa.dao.BaseHapiFhirSystemDao;
import ca.uhn.fhir.jpa.dao.DaoMethodOutcome;
import ca.uhn.fhir.jpa.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.entity.ResourceTable;
import ca.uhn.fhir.jpa.entity.TagDefinition;
import ca.uhn.fhir.jpa.provider.ServletSubRequestDetails;
import ca.uhn.fhir.jpa.util.DeleteConflict;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
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

public class FhirSystemDaoDstu3 extends BaseHapiFhirSystemDao<Bundle, Meta> {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirSystemDaoDstu3.class);

	@Autowired
	private PlatformTransactionManager myTxManager;

	private Bundle batch(final RequestDetails theRequestDetails, Bundle theRequest) {
		ourLog.info("Beginning batch with {} resources", theRequest.getEntry().size());
		long start = System.currentTimeMillis();

		TransactionTemplate txTemplate = new TransactionTemplate(myTxManager);
		txTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);

		Bundle resp = new Bundle();
		resp.setType(BundleType.BATCHRESPONSE);
		OperationOutcome ooResp = new OperationOutcome();
		resp.addEntry().setResource(ooResp);

		/*
		 * For batch, we handle each entry as a mini-transaction in its own database transaction so that if one fails, it doesn't prevent others
		 */

		for (final BundleEntryComponent nextRequestEntry : theRequest.getEntry()) {

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

			BaseServerResponseException caughtEx;
			try {
				Bundle nextResponseBundle = txTemplate.execute(callback);
				caughtEx = null;

				BundleEntryComponent subResponseEntry = nextResponseBundle.getEntry().get(0);
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
				BundleEntryComponent nextEntry = resp.addEntry();

				OperationOutcome oo = new OperationOutcome();
				oo.addIssue().setSeverity(IssueSeverity.ERROR).setDiagnostics(caughtEx.getMessage());
				nextEntry.setResource(oo);

				BundleEntryResponseComponent nextEntryResp = nextEntry.getResponse();
				nextEntryResp.setStatus(toStatusString(caughtEx.getStatusCode()));
			}

		}

		long delay = System.currentTimeMillis() - start;
		ourLog.info("Batch completed in {}ms", new Object[] { delay });
		ooResp.addIssue().setSeverity(IssueSeverity.INFORMATION).setDiagnostics("Batch completed in " + delay + "ms");

		return resp;
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
	 * 
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

		Meta retVal = toMeta(tagDefinitions);

		return retVal;
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
		if (theRequestDetails != null) {
			ActionRequestDetails requestDetails = new ActionRequestDetails(theRequestDetails, theRequest, "Bundle", null);
			notifyInterceptors(RestOperationTypeEnum.TRANSACTION, requestDetails);
		}
		
		String actionName = "Transaction";
		return transaction((ServletRequestDetails) theRequestDetails, theRequest, actionName);
	}

	@SuppressWarnings("unchecked")
	private Bundle transaction(ServletRequestDetails theRequestDetails, Bundle theRequest, String theActionName) {
		BundleType transactionType = theRequest.getTypeElement().getValue();
		if (transactionType == BundleType.BATCH) {
			return batch(theRequestDetails, theRequest);
		}

		if (transactionType == null) {
			String message = "Transactiion Bundle did not specify valid Bundle.type, assuming " + BundleType.TRANSACTION.toCode();
			ourLog.warn(message);
			transactionType = BundleType.TRANSACTION;
		}
		if (transactionType != BundleType.TRANSACTION) {
			throw new InvalidRequestException("Unable to process transaction where incoming Bundle.type = " + transactionType.toCode());
		}

		ourLog.info("Beginning {} with {} resources", theActionName, theRequest.getEntry().size());

		long start = System.currentTimeMillis();
		Date updateTime = new Date();

		Set<IdType> allIds = new LinkedHashSet<IdType>();
		Map<IdType, IdType> idSubstitutions = new HashMap<IdType, IdType>();
		Map<IdType, DaoMethodOutcome> idToPersistedOutcome = new HashMap<IdType, DaoMethodOutcome>();

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
		Bundle response = new Bundle();
		List<BundleEntryComponent> getEntries = new ArrayList<BundleEntryComponent>();
		IdentityHashMap<BundleEntryComponent, Integer> originalRequestOrder = new IdentityHashMap<Bundle.BundleEntryComponent, Integer>();
		for (int i = 0; i < theRequest.getEntry().size(); i++) {
			originalRequestOrder.put(theRequest.getEntry().get(i), i);
			response.addEntry();
			if (theRequest.getEntry().get(i).getRequest().getMethodElement().getValue() == HTTPVerb.GET) {
				getEntries.add(theRequest.getEntry().get(i));
			}
		}
		Collections.sort(theRequest.getEntry(), new TransactionSorter());
		
		Set<String> deletedResources = new HashSet<String>();
		List<DeleteConflict> deleteConflicts = new ArrayList<DeleteConflict>();
		Map<BundleEntryComponent, ResourceTable> entriesToProcess = new IdentityHashMap<BundleEntryComponent, ResourceTable>();
		Set<ResourceTable> nonUpdatedEntities = new HashSet<ResourceTable>();
		
		/*
		 * Loop through the request and process any entries of type
		 * PUT, POST or DELETE
		 */
		for (int i = 0; i < theRequest.getEntry().size(); i++) {

			if (i % 100 == 0) {
				ourLog.info("Processed {} non-GET entries out of {}", i, theRequest.getEntry().size());
			}

			BundleEntryComponent nextReqEntry = theRequest.getEntry().get(i);
			Resource res = nextReqEntry.getResource();
			IdType nextResourceId = null;
			if (res != null) {

				nextResourceId = res.getIdElement();

				if (nextResourceId.hasIdPart() == false) {
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
					if (!allIds.add(nextResourceId)) {
						throw new InvalidRequestException(getContext().getLocalizer().getMessage(BaseHapiFhirSystemDao.class, "transactionContainsMultipleWithDuplicateId", nextResourceId));
					}
				} else if (nextResourceId.hasResourceType() && nextResourceId.hasIdPart()) {
					IdType nextId = nextResourceId.toUnqualifiedVersionless();
					if (!allIds.add(nextId)) {
						throw new InvalidRequestException(getContext().getLocalizer().getMessage(BaseHapiFhirSystemDao.class, "transactionContainsMultipleWithDuplicateId", nextId));
					}
				}

			}

			HTTPVerb verb = nextReqEntry.getRequest().getMethodElement().getValue();

			String resourceType = res != null ? getContext().getResourceDefinition(res).getName() : null;
			BundleEntryComponent nextRespEntry = response.getEntry().get(originalRequestOrder.get(nextReqEntry));

			switch (verb) {
			case POST: {
				// CREATE
				@SuppressWarnings("rawtypes")
				IFhirResourceDao resourceDao = getDaoOrThrowException(res.getClass());
				res.setId((String) null);
				DaoMethodOutcome outcome;
				outcome = resourceDao.create(res, nextReqEntry.getRequest().getIfNoneExist(), false, theRequestDetails);
				handleTransactionCreateOrUpdateOutcome(idSubstitutions, idToPersistedOutcome, nextResourceId, outcome, nextRespEntry, resourceType, res);
				entriesToProcess.put(nextRespEntry, outcome.getEntity());
				if (outcome.getCreated() == false) {
					nonUpdatedEntities.add(outcome.getEntity());
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
						ResourceTable deleted = dao.delete(deleteId, deleteConflicts, theRequestDetails);
						if (deleted != null) {
							deletedResources.add(deleteId.getValueAsString());
						}
					}
				} else {
					List<ResourceTable> allDeleted = dao.deleteByUrl(parts.getResourceType() + '?' + parts.getParams(), deleteConflicts, theRequestDetails);
					for (ResourceTable deleted : allDeleted) {
						deletedResources.add(deleted.getIdDt().toUnqualifiedVersionless().getValueAsString());
					}
					if (allDeleted.isEmpty()) {
						status = Constants.STATUS_HTTP_204_NO_CONTENT;
					}
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
					res.setId(new IdType(parts.getResourceType(), parts.getResourceId()));
					outcome = resourceDao.update(res, null, false, theRequestDetails);
				} else {
					res.setId((String) null);
					outcome = resourceDao.update(res, parts.getResourceType() + '?' + parts.getParams(), false, theRequestDetails);
				}

				handleTransactionCreateOrUpdateOutcome(idSubstitutions, idToPersistedOutcome, nextResourceId, outcome, nextRespEntry, resourceType, res);
				entriesToProcess.put(nextRespEntry, outcome.getEntity());
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
			if (deletedResources.contains(next.getTargetId().toUnqualifiedVersionless().getValue())) {
				iter.remove();
			}
		}
		validateDeleteConflictsEmptyOrThrowException(deleteConflicts);

		/*
		 * Perform ID substitutions and then index each resource we have saved
		 */

		FhirTerser terser = getContext().newTerser();
		for (DaoMethodOutcome nextOutcome : idToPersistedOutcome.values()) {
			IBaseResource nextResource = nextOutcome.getResource();
			if (nextResource == null) {
				continue;
			}

			List<IBaseReference> allRefs = terser.getAllPopulatedChildElementsOfType(nextResource, IBaseReference.class);
			for (IBaseReference nextRef : allRefs) {
				IIdType nextId = nextRef.getReferenceElement();
				if (!nextId.hasIdPart()) {
					continue;
				}
				if (idSubstitutions.containsKey(nextId)) {
					IdType newId = idSubstitutions.get(nextId);
					ourLog.info(" * Replacing resource ref {} with {}", nextId, newId);
					nextRef.setReference(newId.getValue());
				} else if (nextId.getValue().startsWith("urn:")) {
					throw new InvalidRequestException("Unable to satisfy placeholder ID: " + nextId.getValue());
				} else {
					ourLog.debug(" * Reference [{}] does not exist in bundle", nextId);
				}
			}

			IPrimitiveType<Date> deletedInstantOrNull = ResourceMetadataKeyEnum.DELETED_AT.get((IAnyResource) nextResource);
			Date deletedTimestampOrNull = deletedInstantOrNull != null ? deletedInstantOrNull.getValue() : null;
			boolean shouldUpdate = !nonUpdatedEntities.contains(nextOutcome.getEntity());
			updateEntity(nextResource, nextOutcome.getEntity(), deletedTimestampOrNull, shouldUpdate, shouldUpdate, updateTime);
		}

		myEntityManager.flush();

		/*
		 * Double check we didn't allow any duplicates we shouldn't have
		 */
		for (BundleEntryComponent nextEntry : theRequest.getEntry()) {
			if (nextEntry.getRequest().getMethodElement().getValue() == HTTPVerb.POST) {
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

		for (IdType next : allIds) {
			IdType replacement = idSubstitutions.get(next);
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
					ResourceOrDstu1Bundle responseData = ((BaseResourceReturningMethodBinding) method).doInvokeServer(theRequestDetails.getServer(), requestDetails);
					IBaseResource resource = responseData.getResource();
					if (paramValues.containsKey(Constants.PARAM_SUMMARY) || paramValues.containsKey(Constants.PARAM_CONTENT)) {
						resource = filterNestedBundle(requestDetails, resource);
					}
					nextRespEntry.setResource((Resource) resource);
					nextRespEntry.getResponse().setStatus(toStatusString(Constants.STATUS_HTTP_200_OK));
				} catch (NotModifiedException e) {
					nextRespEntry.getResponse().setStatus(toStatusString(Constants.STATUS_HTTP_304_NOT_MODIFIED));
				}
			} else {
				throw new IllegalArgumentException("Unable to handle GET " + url);
			}

		}
		
		for (Entry<BundleEntryComponent, ResourceTable> nextEntry : entriesToProcess.entrySet()) {
			nextEntry.getKey().getResponse().setLocation(nextEntry.getValue().getIdDt().toUnqualified().getValue());
			nextEntry.getKey().getResponse().setEtag(nextEntry.getValue().getIdDt().getVersionIdPart());
		}
		
		long delay = System.currentTimeMillis() - start;
		ourLog.info(theActionName + " completed in {}ms", new Object[] { delay });

		response.setType(BundleType.TRANSACTIONRESPONSE);
		return response;
	}

	private static void handleTransactionCreateOrUpdateOutcome(Map<IdType, IdType> idSubstitutions, Map<IdType, DaoMethodOutcome> idToPersistedOutcome, IdType nextResourceId, DaoMethodOutcome outcome,
			BundleEntryComponent newEntry, String theResourceType, IBaseResource theRes) {
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
		newEntry.getResponse().setLastModified(((Resource)theRes).getMeta().getLastUpdated());
	}

	private static boolean isPlaceholder(IdType theId) {
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
	public class TransactionSorter implements Comparator<BundleEntryComponent> {

		@Override
		public int compare(BundleEntryComponent theO1, BundleEntryComponent theO2) {			
			int o1 = toOrder(theO1);
			int o2 = toOrder(theO2);

			return o1 - o2;
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
			case GET:
				o1 = 4;
				break;
			case NULL:
				o1 = 0;
				break;
			}
			}
			return o1;
		}

	}

}
