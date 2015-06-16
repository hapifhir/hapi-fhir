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
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.TypedQuery;

import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.jpa.entity.TagDefinition;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.base.composite.BaseResourceReferenceDt;
import ca.uhn.fhir.model.dstu2.composite.MetaDt;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Bundle.Entry;
import ca.uhn.fhir.model.dstu2.resource.Bundle.EntryTransactionResponse;
import ca.uhn.fhir.model.dstu2.resource.OperationOutcome;
import ca.uhn.fhir.model.dstu2.valueset.BundleTypeEnum;
import ca.uhn.fhir.model.dstu2.valueset.HTTPVerbEnum;
import ca.uhn.fhir.model.dstu2.valueset.IssueSeverityEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.IBundleProvider;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.util.FhirTerser;

public class FhirSystemDaoDstu2 extends BaseFhirSystemDao<Bundle> {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirSystemDaoDstu2.class);

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
						String msg = getContext().getLocalizer().getMessage(BaseFhirSystemDao.class, "transactionInvalidUrl", theAction, theUrl);
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
			String msg = getContext().getLocalizer().getMessage(BaseFhirSystemDao.class, "transactionInvalidUrl", theAction, theUrl);
			throw new InvalidRequestException(msg);
		}
		IFhirResourceDao<? extends IResource> dao = null;
		if (resType != null) {
			dao = getDao(resType.getImplementingClass());
		}
		if (dao == null) {
			String msg = getContext().getLocalizer().getMessage(BaseFhirSystemDao.class, "transactionInvalidUrl", theAction, theUrl);
			throw new InvalidRequestException(msg);
		}
		retVal.setDao(dao);

		if (retVal.getResourceId() == null && retVal.getParams() == null) {
			String msg = getContext().getLocalizer().getMessage(BaseFhirSystemDao.class, "transactionInvalidUrl", theAction, theUrl);
			throw new InvalidRequestException(msg);
		}

		return retVal;
	}

	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public Bundle transaction(Bundle theResources) {
		ourLog.info("Beginning transaction with {} resources", theResources.getEntry().size());
		long start = System.currentTimeMillis();

		Set<IdDt> allIds = new LinkedHashSet<IdDt>();
		Map<IdDt, IdDt> idSubstitutions = new HashMap<IdDt, IdDt>();
		Map<IdDt, DaoMethodOutcome> idToPersistedOutcome = new HashMap<IdDt, DaoMethodOutcome>();

		Bundle response = new Bundle();
		OperationOutcome oo = new OperationOutcome();
		response.addEntry().setResource(oo);

		for (int i = 0; i < theResources.getEntry().size(); i++) {
			Entry nextEntry = theResources.getEntry().get(i);
			IResource res = nextEntry.getResource();
			IdDt nextResourceId = null;
			if (res != null) {

				nextResourceId = res.getId();
				if (nextResourceId.hasIdPart() && !nextResourceId.hasResourceType() && !nextResourceId.isLocal()) {
					nextResourceId = new IdDt(toResourceName(res.getClass()), nextResourceId.getIdPart());
					res.setId(nextResourceId);
				}

				/*
				 * Ensure that the bundle doesn't have any duplicates, since this causes all kinds of weirdness
				 */
				if (nextResourceId.isLocal()) {
					if (!allIds.add(nextResourceId)) {
						throw new InvalidRequestException(getContext().getLocalizer().getMessage(BaseFhirSystemDao.class, "transactionContainsMultipleWithDuplicateId", nextResourceId));
					}
				} else if (nextResourceId.hasResourceType() && nextResourceId.hasIdPart()) {
					IdDt nextId = nextResourceId.toUnqualifiedVersionless();
					if (!allIds.add(nextId)) {
						throw new InvalidRequestException(getContext().getLocalizer().getMessage(BaseFhirSystemDao.class, "transactionContainsMultipleWithDuplicateId", nextId));
					}
				}

			}

			HTTPVerbEnum verb = nextEntry.getTransaction().getMethodElement().getValueAsEnum();
			if (verb == null) {
				throw new InvalidRequestException(getContext().getLocalizer().getMessage(BaseFhirSystemDao.class, "transactionEntryHasInvalidVerb", nextEntry.getTransaction().getMethod()));
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
				outcome = resourceDao.create(res, nextEntry.getTransaction().getIfNoneExist(), false);
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

				newEntry.getTransactionResponse().setStatus(Integer.toString(Constants.STATUS_HTTP_204_NO_CONTENT));
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

				if (parts.getResourceId() != null && parts.getParams() == null) {
					IResource found;
					if (parts.getVersionId() != null) {
						found = resourceDao.read(new IdDt(parts.getResourceType(), parts.getResourceId(), parts.getVersionId()));
					} else {
						found = resourceDao.read(new IdDt(parts.getResourceType(), parts.getResourceId()));
					}
					EntryTransactionResponse resp = response.addEntry().setResource(found).getTransactionResponse();
					resp.setLocation(found.getId().toUnqualified().getValue());
					resp.setEtag(found.getId().getVersionIdPart());
				} else if (parts.getParams() != null) {
					RuntimeResourceDefinition def = getContext().getResourceDefinition(parts.getDao().getResourceType());
					SearchParameterMap params = translateMatchUrl(url, def);
					IBundleProvider bundle = parts.getDao().search(params);

					Bundle searchBundle = new Bundle();
					searchBundle.setTotal(bundle.size());

					int configuredMax = 100; // this should probably be configurable or something
					if (bundle.size() > configuredMax) {
						oo.addIssue().setSeverity(IssueSeverityEnum.WARNING)
								.setDetails("Search nested within transaction found more than " + configuredMax + " matches, but paging is not supported in nested transactions");
					}
					List<IBaseResource> resourcesToAdd = bundle.getResources(0, Math.min(bundle.size(), configuredMax));
					for (IBaseResource next : resourcesToAdd) {
						searchBundle.addEntry().setResource((IResource) next);
					}

					response.addEntry().setResource(searchBundle);
				}
			}
			}

		}

		FhirTerser terser = getContext().newTerser();

		for (DaoMethodOutcome nextOutcome : idToPersistedOutcome.values()) {
			IResource nextResource = nextOutcome.getResource();
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

		long delay = System.currentTimeMillis() - start;
		ourLog.info("Transaction completed in {}ms", new Object[] { delay });

		oo.addIssue().setSeverity(IssueSeverityEnum.INFORMATION).setDetails("Transaction completed in " + delay + "ms");

		for (IdDt next : allIds) {
			IdDt replacement = idSubstitutions.get(next);
			if (replacement == null) {
				continue;
			}
			if (replacement.equals(next)) {
				continue;
			}
			oo.addIssue().setSeverity(IssueSeverityEnum.INFORMATION).setDetails("Placeholder resource ID \"" + next + "\" was replaced with permanent ID \"" + replacement + "\"");
		}

		notifyWriteCompleted();

		response.setType(BundleTypeEnum.TRANSACTION_RESPONSE);
		return response;
	}

	@Override
	public MetaDt metaGetOperation() {

		String sql = "SELECT d FROM TagDefinition d WHERE d.myId IN (SELECT DISTINCT t.myTagId FROM ResourceTag t)";
		TypedQuery<TagDefinition> q = myEntityManager.createQuery(sql, TagDefinition.class);
		List<TagDefinition> tagDefinitions = q.getResultList();

		MetaDt retVal = super.toMetaDt(tagDefinitions);

		return retVal;
	}

	private String extractTransactionUrlOrThrowException(Entry nextEntry, HTTPVerbEnum verb) {
		String url = nextEntry.getTransaction().getUrl();
		if (isBlank(url)) {
			throw new InvalidRequestException(getContext().getLocalizer().getMessage(BaseFhirSystemDao.class, "transactionMissingUrl", verb.name()));
		}
		return url;
	}

	private static void handleTransactionCreateOrUpdateOutcome(Map<IdDt, IdDt> idSubstitutions, Map<IdDt, DaoMethodOutcome> idToPersistedOutcome, IdDt nextResourceId, DaoMethodOutcome outcome,
			Entry newEntry, String theResourceType) {
		IdDt newId = outcome.getId().toUnqualifiedVersionless();
		IdDt resourceId = nextResourceId.isLocal() ? nextResourceId : nextResourceId.toUnqualifiedVersionless();
		if (newId.equals(resourceId) == false) {
			idSubstitutions.put(resourceId, newId);
			if (resourceId.isLocal()) {
				/*
				 * The correct way for substitution IDs to be is to be with no resource type, but we'll accept the qualified kind too just to be lenient.
				 */
				idSubstitutions.put(new IdDt(theResourceType + '/' + resourceId.getValue()), newId);
			}
		}
		idToPersistedOutcome.put(newId, outcome);
		if (outcome.getCreated().booleanValue()) {
			newEntry.getTransactionResponse().setStatus(Long.toString(Constants.STATUS_HTTP_201_CREATED));
		} else {
			newEntry.getTransactionResponse().setStatus(Long.toString(Constants.STATUS_HTTP_200_OK));
		}
		newEntry.getTransactionResponse().setLocation(outcome.getId().toUnqualified().getValue());
		newEntry.getTransactionResponse().setEtag(outcome.getId().getVersionIdPart());
	}

	private static class UrlParts {
		private IFhirResourceDao<? extends IResource> myDao;
		private String myParams;
		private String myResourceId;
		private String myResourceType;
		private String myVersionId;

		public IFhirResourceDao<? extends IResource> getDao() {
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

		public void setDao(IFhirResourceDao<? extends IResource> theDao) {
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
