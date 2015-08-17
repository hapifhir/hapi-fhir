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
import static org.apache.commons.lang3.StringUtils.*;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.TypedQuery;

import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

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
import ca.uhn.fhir.model.dstu2.valueset.IssueTypeEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.method.MethodUtil;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.IBundleProvider;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.interceptor.IServerInterceptor.ActionRequestDetails;
import ca.uhn.fhir.util.FhirTerser;

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

	private Bundle batch(Bundle theRequest) {
		ourLog.info("Beginning batch with {} resources", theRequest.getEntry().size());
		long start = System.currentTimeMillis();

		TransactionTemplate txTemplate = new TransactionTemplate(myTxManager);
		txTemplate.setPropagationBehavior(TransactionTemplate.PROPAGATION_REQUIRES_NEW);

		Bundle resp = new Bundle();
		resp.setType(BundleTypeEnum.BATCH_RESPONSE);
		OperationOutcome ooResp = new OperationOutcome();
		resp.addEntry().setResource(ooResp);

		/*
		 * For batch, we handle each entry as a mini-transaction in its own
		 * database transaction so that if one fails, it doesn't prevent others
		 */
		
		for (final Entry nextRequestEntry : theRequest.getEntry()) {

			TransactionCallback<Bundle> callback = new TransactionCallback<Bundle>() {
				@Override
				public Bundle doInTransaction(TransactionStatus theStatus) {
					Bundle subRequestBundle = new Bundle();
					subRequestBundle.setType(BundleTypeEnum.TRANSACTION);
					subRequestBundle.addEntry(nextRequestEntry);

					Bundle subResponseBundle = transaction(subRequestBundle, "Batch sub-request");
					return subResponseBundle;
				}
			};

			BaseServerResponseException caughtEx;
			try {
				Bundle nextResponseBundle = txTemplate.execute(callback);
				caughtEx = null;

				Entry subResponseEntry = nextResponseBundle.getEntry().get(1);
				resp.addEntry(subResponseEntry);
				/*
				 * If the individual entry didn't have a resource in its response, bring the
				 * sub-transaction's OperationOutcome across so the client can see it
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

	private UrlParts parseUrl(String theAction, String theUrl) {
		UrlParts retVal = new UrlParts();

		//@formatter:off
		/* 
		 * We assume that the URL passed in is in one of the following forms:
		 * [Resource Type]?[Search Params]
		 * [Resource Type]/[Resource ID]
		 * [Resource Type]/[Resource ID]/_history/[Version ID]
		 */
		//@formatter:on
		int nextStart = 0;
		boolean nextIsHistory = false;

		for (int idx = 0; idx < theUrl.length(); idx++) {
			char nextChar = theUrl.charAt(idx);
			boolean atEnd = (idx + 1) == theUrl.length();
			if (nextChar == '?' || nextChar == '/' || atEnd) {
				int endIdx = atEnd ? idx + 1 : idx;
				String nextSubstring = theUrl.substring(nextStart, endIdx);
				if (retVal.getResourceType() == null) {
					retVal.setResourceType(nextSubstring);
				} else if (retVal.getResourceId() == null) {
					retVal.setResourceId(nextSubstring);
				} else if (nextIsHistory) {
					retVal.setVersionId(nextSubstring);
				} else {
					if (nextSubstring.equals(Constants.URL_TOKEN_HISTORY)) {
						nextIsHistory = true;
					} else {
						String msg = getContext().getLocalizer().getMessage(BaseHapiFhirSystemDao.class, "transactionInvalidUrl", theAction, theUrl);
						throw new InvalidRequestException(msg);
					}
				}
				if (nextChar == '?') {
					if (theUrl.length() > idx + 1) {
						retVal.setParams(theUrl.substring(idx + 1, theUrl.length()));
					}
					break;
				}
				nextStart = idx + 1;
			}
		}

		RuntimeResourceDefinition resType;
		try {
			resType = getContext().getResourceDefinition(retVal.getResourceType());
		} catch (DataFormatException e) {
			String msg = getContext().getLocalizer().getMessage(BaseHapiFhirSystemDao.class, "transactionInvalidUrl", theAction, theUrl);
			throw new InvalidRequestException(msg);
		}
		IFhirResourceDao<? extends IBaseResource> dao = null;
		if (resType != null) {
			dao = getDao(resType.getImplementingClass());
		}
		if (dao == null) {
			String msg = getContext().getLocalizer().getMessage(BaseHapiFhirSystemDao.class, "transactionInvalidUrl", theAction, theUrl);
			throw new InvalidRequestException(msg);
		}
		retVal.setDao(dao);

		if (retVal.getResourceId() == null && retVal.getParams() == null) {
			String msg = getContext().getLocalizer().getMessage(BaseHapiFhirSystemDao.class, "transactionInvalidUrl", theAction, theUrl);
			throw new InvalidRequestException(msg);
		}

		return retVal;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public Bundle transaction(Bundle theRequest) {
		ActionRequestDetails requestDetails = new ActionRequestDetails(null, "Bundle", theRequest);
		notifyInterceptors(RestOperationTypeEnum.TRANSACTION, requestDetails);

		String theActionName = "Transaction";
		return transaction(theRequest, theActionName);
	}

	@SuppressWarnings("unchecked")
	private Bundle transaction(Bundle theRequest, String theActionName) {
		BundleTypeEnum transactionType = theRequest.getTypeElement().getValueAsEnum();
		if (transactionType == BundleTypeEnum.BATCH) {
			return batch(theRequest);
		}

		OperationOutcome statusOperationOutcome = new OperationOutcome();
		if (transactionType == null) {
			String message = "Transactiion Bundle did not specify valid Bundle.type, assuming " + BundleTypeEnum.TRANSACTION.getCode();
			statusOperationOutcome.addIssue().setCode(IssueTypeEnum.INVALID_CONTENT).setSeverity(IssueSeverityEnum.WARNING).setDiagnostics(message);
			ourLog.warn(message);
			transactionType = BundleTypeEnum.TRANSACTION;
		}
		if (transactionType != BundleTypeEnum.TRANSACTION) {
			throw new InvalidRequestException("Unable to process transaction where incoming Bundle.type = " + transactionType.getCode());
		}

		ourLog.info("Beginning {} with {} resources", theActionName, theRequest.getEntry().size());

		long start = System.currentTimeMillis();

		Set<IdDt> allIds = new LinkedHashSet<IdDt>();
		Map<IdDt, IdDt> idSubstitutions = new HashMap<IdDt, IdDt>();
		Map<IdDt, DaoMethodOutcome> idToPersistedOutcome = new HashMap<IdDt, DaoMethodOutcome>();

		Bundle response = new Bundle();
		response.addEntry().setResource(statusOperationOutcome);

		// TODO: process verbs in the correct order

		for (int i = 0; i < theRequest.getEntry().size(); i++) {
			Entry nextEntry = theRequest.getEntry().get(i);
			IResource res = nextEntry.getResource();
			IdDt nextResourceId = null;
			if (res != null) {

				nextResourceId = res.getId();
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
				IFhirResourceDao resourceDao = getDao(res.getClass());
				res.setId((String) null);
				DaoMethodOutcome outcome;
				Entry newEntry = response.addEntry();
				outcome = resourceDao.create(res, nextEntry.getRequest().getIfNoneExist(), false);
				handleTransactionCreateOrUpdateOutcome(idSubstitutions, idToPersistedOutcome, nextResourceId, outcome, newEntry, resourceType);
				break;
			}
			case DELETE: {
				// DELETE
				Entry newEntry = response.addEntry();
				String url = extractTransactionUrlOrThrowException(nextEntry, verb);
				UrlParts parts = parseUrl(verb.getCode(), url);
				if (parts.getResourceId() != null) {
					parts.getDao().delete(new IdDt(parts.getResourceType(), parts.getResourceId()));
				} else {
					parts.getDao().deleteByUrl(parts.getResourceType() + '?' + parts.getParams());
				}

				newEntry.getResponse().setStatus(toStatusString(Constants.STATUS_HTTP_204_NO_CONTENT));
				break;
			}
			case PUT: {
				// UPDATE
				@SuppressWarnings("rawtypes")
				IFhirResourceDao resourceDao = getDao(res.getClass());

				DaoMethodOutcome outcome;
				Entry newEntry = response.addEntry();

				String url = extractTransactionUrlOrThrowException(nextEntry, verb);

				UrlParts parts = parseUrl(verb.getCode(), url);
				if (isNotBlank(parts.getResourceId())) {
					res.setId(new IdDt(parts.getResourceType(), parts.getResourceId()));
					outcome = resourceDao.update(res, null, false);
				} else {
					res.setId((String) null);
					outcome = resourceDao.update(res, parts.getResourceType() + '?' + parts.getParams(), false);
				}

				handleTransactionCreateOrUpdateOutcome(idSubstitutions, idToPersistedOutcome, nextResourceId, outcome, newEntry, resourceType);
				break;
			}
			case GET: {
				// SEARCH/READ/VREAD
				String url = extractTransactionUrlOrThrowException(nextEntry, verb);
				UrlParts parts = parseUrl(verb.getCode(), url);

				@SuppressWarnings("rawtypes")
				IFhirResourceDao resourceDao = parts.getDao();

				String ifNoneMatch = nextEntry.getRequest().getIfNoneMatch();
				if (isNotBlank(ifNoneMatch)) {
					ifNoneMatch = MethodUtil.parseETagValue(ifNoneMatch);
				}

				if (parts.getResourceId() != null && parts.getParams() == null) {
					IResource found;
					boolean notChanged = false;
					if (parts.getVersionId() != null) {
						if (isNotBlank(ifNoneMatch)) {
							throw new InvalidRequestException("Unable to perform vread on '" + url + "' with ifNoneMatch also set. Do not include a version in the URL to perform a conditional read.");
						}
						found = (IResource) resourceDao.read(new IdDt(parts.getResourceType(), parts.getResourceId(), parts.getVersionId()));
					} else {
						found = (IResource) resourceDao.read(new IdDt(parts.getResourceType(), parts.getResourceId()));
						if (isNotBlank(ifNoneMatch) && ifNoneMatch.equals(found.getId().getVersionIdPart())) {
							notChanged = true;
						}
					}
					Entry entry = response.addEntry();
					if (notChanged == false) {
						entry.setResource(found);
					}
					EntryResponse resp = entry.getResponse();
					resp.setLocation(found.getId().toUnqualified().getValue());
					resp.setEtag(found.getId().getVersionIdPart());
					if (!notChanged) {
						resp.setStatus(toStatusString(Constants.STATUS_HTTP_200_OK));
					} else {
						resp.setStatus(toStatusString(Constants.STATUS_HTTP_304_NOT_MODIFIED));
					}
				} else if (parts.getParams() != null) {
					RuntimeResourceDefinition def = getContext().getResourceDefinition(parts.getDao().getResourceType());
					SearchParameterMap params = translateMatchUrl(url, def);
					IBundleProvider bundle = parts.getDao().search(params);

					Bundle searchBundle = new Bundle();
					searchBundle.setTotal(bundle.size());

					int configuredMax = 100; // this should probably be configurable or something
					if (bundle.size() > configuredMax) {
						statusOperationOutcome.addIssue().setSeverity(IssueSeverityEnum.WARNING)
								.setDiagnostics("Search nested within transaction found more than " + configuredMax + " matches, but paging is not supported in nested transactions");
					}
					List<IBaseResource> resourcesToAdd = bundle.getResources(0, Math.min(bundle.size(), configuredMax));
					for (IBaseResource next : resourcesToAdd) {
						searchBundle.addEntry().setResource((IResource) next);
					}

					Entry newEntry = response.addEntry();
					newEntry.setResource(searchBundle);
					newEntry.getResponse().setStatus(toStatusString(Constants.STATUS_HTTP_200_OK));
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
			updateEntity(nextResource, nextOutcome.getEntity(), false, deletedTimestampOrNull, true, false);
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

		long delay = System.currentTimeMillis() - start;
		ourLog.info(theActionName + " completed in {}ms", new Object[] { delay });

		statusOperationOutcome.addIssue().setSeverity(IssueSeverityEnum.INFORMATION).setDiagnostics(theActionName + " completed in " + delay + "ms");

		for (IdDt next : allIds) {
			IdDt replacement = idSubstitutions.get(next);
			if (replacement == null) {
				continue;
			}
			if (replacement.equals(next)) {
				continue;
			}
			statusOperationOutcome.addIssue().setSeverity(IssueSeverityEnum.INFORMATION).setDiagnostics("Placeholder resource ID \"" + next + "\" was replaced with permanent ID \"" + replacement + "\"");
		}

		notifyWriteCompleted();

		response.setType(BundleTypeEnum.TRANSACTION_RESPONSE);
		return response;
	}

	private static void handleTransactionCreateOrUpdateOutcome(Map<IdDt, IdDt> idSubstitutions, Map<IdDt, DaoMethodOutcome> idToPersistedOutcome, IdDt nextResourceId, DaoMethodOutcome outcome,
			Entry newEntry, String theResourceType) {
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

	private static class UrlParts {
		private IFhirResourceDao<? extends IBaseResource> myDao;
		private String myParams;
		private String myResourceId;
		private String myResourceType;
		private String myVersionId;

		public IFhirResourceDao<? extends IBaseResource> getDao() {
			return myDao;
		}

		public String getParams() {
			return myParams;
		}

		public String getResourceId() {
			return myResourceId;
		}

		public String getResourceType() {
			return myResourceType;
		}

		public String getVersionId() {
			return myVersionId;
		}

		public void setDao(IFhirResourceDao<? extends IBaseResource> theDao) {
			myDao = theDao;
		}

		public void setParams(String theParams) {
			myParams = theParams;
		}

		public void setResourceId(String theResourceId) {
			myResourceId = theResourceId;
		}

		public void setResourceType(String theResourceType) {
			myResourceType = theResourceType;
		}

		public void setVersionId(String theVersionId) {
			myVersionId = theVersionId;
		}
	}

}
