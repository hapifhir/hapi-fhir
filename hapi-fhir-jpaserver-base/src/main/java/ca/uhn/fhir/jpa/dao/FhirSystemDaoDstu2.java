package ca.uhn.fhir.jpa.dao;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2015 University Health Network
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

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.TypedQuery;

import org.apache.http.NameValuePair;
import org.hl7.fhir.instance.model.api.IBaseResource;
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
import ca.uhn.fhir.jpa.entity.TagDefinition;
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
import ca.uhn.fhir.util.FhirTerser;
import ca.uhn.fhir.util.UrlUtil;
import ca.uhn.fhir.util.UrlUtil.UrlParts;

public class FhirSystemDaoDstu2 extends BaseHapiFhirSystemDao<Bundle> {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirSystemDaoDstu2.class);

	@Autowired
	private PlatformTransactionManager myTxManager;

	private String extractTransactionUrlOrThrowException(Entry nextEntry, HTTPVerbEnum verb) {
		String url = nextEntry.getRequest().getUrl();
		if (isBlank(url)) {
			throw new InvalidRequestException(getContext().getLocalizer().getMessage(BaseHapiFhirSystemDao.class, "transactionMissingUrl", verb.name()));
		}
		return url;
	}

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

					Bundle subResponseBundle = transaction(theRequestDetails, subRequestBundle, "Batch sub-request");
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

	@Override
	public MetaDt metaGetOperation() {
		// Notify interceptors
		ActionRequestDetails requestDetails = new ActionRequestDetails(null, null);
		notifyInterceptors(RestOperationTypeEnum.META, requestDetails);

		String sql = "SELECT d FROM TagDefinition d WHERE d.myId IN (SELECT DISTINCT t.myTagId FROM ResourceTag t)";
		TypedQuery<TagDefinition> q = myEntityManager.createQuery(sql, TagDefinition.class);
		List<TagDefinition> tagDefinitions = q.getResultList();

		MetaDt retVal = super.toMetaDt(tagDefinitions);

		return retVal;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public Bundle transaction(RequestDetails theRequestDetails, Bundle theRequest) {
		ActionRequestDetails requestDetails = new ActionRequestDetails(null, "Bundle", theRequest);
		notifyInterceptors(RestOperationTypeEnum.TRANSACTION, requestDetails);

		String actionName = "Transaction";
		return transaction(theRequestDetails, theRequest, actionName);
	}

	@SuppressWarnings("unchecked")
	private Bundle transaction(RequestDetails theRequestDetails, Bundle theRequest, String theActionName) {
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

		Bundle response = new Bundle();

		// TODO: process verbs in the correct order

		for (int i = 0; i < theRequest.getEntry().size(); i++) {

			if (i % 100 == 0) {
				ourLog.info("Processed {} entries out of {}", i, theRequest.getEntry().size());
			}

			Entry nextEntry = theRequest.getEntry().get(i);
			IResource res = nextEntry.getResource();
			IdDt nextResourceId = null;
			if (res != null) {

				nextResourceId = res.getId();

				if (nextResourceId.hasIdPart() == false) {
					if (isNotBlank(nextEntry.getFullUrl())) {
						nextResourceId = new IdDt(nextEntry.getFullUrl());
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

			HTTPVerbEnum verb = nextEntry.getRequest().getMethodElement().getValueAsEnum();
			if (verb == null) {
				throw new InvalidRequestException(getContext().getLocalizer().getMessage(BaseHapiFhirSystemDao.class, "transactionEntryHasInvalidVerb", nextEntry.getRequest().getMethod()));
			}

			String resourceType = res != null ? getContext().getResourceDefinition(res).getName() : null;

			switch (verb) {
			case POST: {
				// CREATE
				@SuppressWarnings("rawtypes")
				IFhirResourceDao resourceDao = getDaoOrThrowException(res.getClass());
				res.setId((String) null);
				DaoMethodOutcome outcome;
				Entry newEntry = response.addEntry();
				outcome = resourceDao.create(res, nextEntry.getRequest().getIfNoneExist(), false);
				handleTransactionCreateOrUpdateOutcome(idSubstitutions, idToPersistedOutcome, nextResourceId, outcome, newEntry, resourceType, res);
				break;
			}
			case DELETE: {
				// DELETE
				Entry newEntry = response.addEntry();
				String url = extractTransactionUrlOrThrowException(nextEntry, verb);
				UrlParts parts = UrlUtil.parseUrl(url);
				ca.uhn.fhir.jpa.dao.IFhirResourceDao<? extends IBaseResource> dao = toDao(parts, verb.getCode(), url);
				if (parts.getResourceId() != null) {
					dao.delete(new IdDt(parts.getResourceType(), parts.getResourceId()));
				} else {
					dao.deleteByUrl(parts.getResourceType() + '?' + parts.getParams(), true);
				}

				newEntry.getResponse().setStatus(toStatusString(Constants.STATUS_HTTP_204_NO_CONTENT));
				break;
			}
			case PUT: {
				// UPDATE
				@SuppressWarnings("rawtypes")
				IFhirResourceDao resourceDao = getDaoOrThrowException(res.getClass());

				DaoMethodOutcome outcome;
				Entry newEntry = response.addEntry();

				String url = extractTransactionUrlOrThrowException(nextEntry, verb);

				UrlParts parts = UrlUtil.parseUrl(url);
				if (isNotBlank(parts.getResourceId())) {
					res.setId(new IdDt(parts.getResourceType(), parts.getResourceId()));
					outcome = resourceDao.update(res, null, false);
				} else {
					res.setId((String) null);
					outcome = resourceDao.update(res, parts.getResourceType() + '?' + parts.getParams(), false);
				}

				handleTransactionCreateOrUpdateOutcome(idSubstitutions, idToPersistedOutcome, nextResourceId, outcome, newEntry, resourceType, res);
				break;
			}
			case GET: {
				// SEARCH/READ/VREAD
				RequestDetails requestDetails = new RequestDetails();
				requestDetails.setServletRequest(theRequestDetails.getServletRequest());
				requestDetails.setRequestType(RequestTypeEnum.GET);
				requestDetails.setServer(theRequestDetails.getServer());
				
				String url = extractTransactionUrlOrThrowException(nextEntry, verb);
				
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
				
				if (isNotBlank(nextEntry.getRequest().getIfMatch())) {
					requestDetails.addHeader(Constants.HEADER_IF_MATCH, nextEntry.getRequest().getIfMatch());
				}
				if (isNotBlank(nextEntry.getRequest().getIfNoneExist())) {
					requestDetails.addHeader(Constants.HEADER_IF_NONE_EXIST, nextEntry.getRequest().getIfNoneExist());
				}
				if (isNotBlank(nextEntry.getRequest().getIfNoneMatch())) {
					requestDetails.addHeader(Constants.HEADER_IF_NONE_MATCH, nextEntry.getRequest().getIfNoneMatch());
				}
				
				if (method instanceof BaseResourceReturningMethodBinding) {
					try {
						ResourceOrDstu1Bundle responseData = ((BaseResourceReturningMethodBinding) method).invokeServer(theRequestDetails.getServer(), requestDetails, new byte[0]);
						Entry newEntry = response.addEntry();
						IBaseResource resource = responseData.getResource();
						if (paramValues.containsKey(Constants.PARAM_SUMMARY) || paramValues.containsKey(Constants.PARAM_CONTENT)) {
							resource = filterNestedBundle(requestDetails, resource);
						}
						newEntry.setResource((IResource) resource);
						newEntry.getResponse().setStatus(toStatusString(Constants.STATUS_HTTP_200_OK));
					} catch (NotModifiedException e) {
						Entry newEntry = response.addEntry();
						newEntry.getResponse().setStatus(toStatusString(Constants.STATUS_HTTP_304_NOT_MODIFIED));
					}
				} else {
					throw new IllegalArgumentException("Unable to handle GET " + url);
				}
			}
			}
		}

		FhirTerser terser = getContext().newTerser();

		/*
		 * Perform ID substitutions and then index each resource we have saved
		 */

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
			updateEntity(nextResource, nextOutcome.getEntity(), false, deletedTimestampOrNull, true, false, updateTime);
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

		long delay = System.currentTimeMillis() - start;
		ourLog.info(theActionName + " completed in {}ms", new Object[] { delay });

		response.setType(BundleTypeEnum.TRANSACTION_RESPONSE);
		return response;
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

	private IFhirResourceDao<?> getDaoOrThrowException(Class<? extends IResource> theClass) {
		IFhirResourceDao<? extends IResource> retVal = getDao(theClass);
		if (retVal == null) {
			throw new InvalidRequestException("Unable to process request, this server does not know how to handle resources of type " + getContext().getResourceDefinition(theClass).getName());
		}
		return retVal;
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

}
