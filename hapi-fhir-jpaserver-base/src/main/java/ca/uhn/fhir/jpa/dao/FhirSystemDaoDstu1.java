package ca.uhn.fhir.jpa.dao;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2017 University Health Network
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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import ca.uhn.fhir.jpa.entity.BaseHasResource;
import ca.uhn.fhir.jpa.entity.ResourceTable;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.base.composite.BaseResourceReferenceDt;
import ca.uhn.fhir.model.dstu.resource.OperationOutcome;
import ca.uhn.fhir.model.dstu.valueset.IssueSeverityEnum;
import ca.uhn.fhir.model.dstu2.composite.MetaDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.model.valueset.BundleEntryTransactionMethodEnum;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.method.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.NotImplementedOperationException;
import ca.uhn.fhir.rest.server.interceptor.IServerInterceptor.ActionRequestDetails;
import ca.uhn.fhir.util.FhirTerser;

public class FhirSystemDaoDstu1 extends BaseHapiFhirSystemDao<List<IResource>, MetaDt> {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirSystemDaoDstu1.class);

	@Override
	public MetaDt metaGetOperation(RequestDetails theRequestDetails) {
		throw new NotImplementedOperationException("meta not supported in DSTU1");
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public List<IResource> transaction(RequestDetails theRequestDetails, List<IResource> theResources) {
		ourLog.info("Beginning transaction with {} resources", theResources.size());

		// Notify interceptors
		ActionRequestDetails requestDetails = new ActionRequestDetails(theRequestDetails);
		notifyInterceptors(RestOperationTypeEnum.TRANSACTION, requestDetails);

		long start = System.currentTimeMillis();

		Set<IdDt> allIds = new HashSet<IdDt>();

		for (int i = 0; i < theResources.size(); i++) {
			IResource res = theResources.get(i);
			if (res.getId().hasIdPart() && !res.getId().hasResourceType() && !isPlaceholder(res.getId())) {
				res.setId(new IdDt(toResourceName(res.getClass()), res.getId().getIdPart()));
			}

			/*
			 * Ensure that the bundle doesn't have any duplicates, since this causes all kinds of weirdness
			 */
			if (isPlaceholder(res.getId())) {
				if (!allIds.add(res.getId())) {
					throw new InvalidRequestException("Transaction bundle contains multiple resources with ID: " + res.getId());
				}
			} else if (res.getId().hasResourceType() && res.getId().hasIdPart()) {
				IdDt nextId = res.getId().toUnqualifiedVersionless();
				if (!allIds.add(nextId)) {
					throw new InvalidRequestException("Transaction bundle contains multiple resources with ID: " + nextId);
				}
			}
		}

		FhirTerser terser = getContext().newTerser();

		int creations = 0;
		int updates = 0;

		Map<IdDt, IdDt> idConversions = new HashMap<IdDt, IdDt>();

		List<ResourceTable> persistedResources = new ArrayList<ResourceTable>();

		List<IResource> retVal = new ArrayList<IResource>();
		OperationOutcome oo = new OperationOutcome();
		retVal.add(oo);

		Date updateTime = new Date();
		for (int resourceIdx = 0; resourceIdx < theResources.size(); resourceIdx++) {
			IResource nextResource = theResources.get(resourceIdx);

			IdDt nextId = nextResource.getId();
			if (nextId == null) {
				nextId = new IdDt();
			}

			String resourceName = toResourceName(nextResource);
			BundleEntryTransactionMethodEnum nextResouceOperationIn = ResourceMetadataKeyEnum.ENTRY_TRANSACTION_METHOD.get(nextResource);
			if (nextResouceOperationIn == null && hasValue(ResourceMetadataKeyEnum.DELETED_AT.get(nextResource))) {
				nextResouceOperationIn = BundleEntryTransactionMethodEnum.DELETE;
			}

			String matchUrl = ResourceMetadataKeyEnum.LINK_SEARCH.get(nextResource);
			Set<Long> candidateMatches = null;
			if (StringUtils.isNotBlank(matchUrl)) {
				candidateMatches = processMatchUrl(matchUrl, nextResource.getClass());
			}

			ResourceTable entity;
			if (nextResouceOperationIn == BundleEntryTransactionMethodEnum.POST) {
				entity = null;
			} else if (nextResouceOperationIn == BundleEntryTransactionMethodEnum.PUT || nextResouceOperationIn == BundleEntryTransactionMethodEnum.DELETE) {
				if (candidateMatches == null || candidateMatches.size() == 0) {
					if (nextId == null || StringUtils.isBlank(nextId.getIdPart())) {
						throw new InvalidRequestException(getContext().getLocalizer().getMessage(BaseHapiFhirSystemDao.class, "transactionOperationFailedNoId", nextResouceOperationIn.name()));
					}
					entity = tryToLoadEntity(nextId);
					if (entity == null) {
						if (nextResouceOperationIn == BundleEntryTransactionMethodEnum.PUT) {
							ourLog.debug("Attempting to UPDATE resource with unknown ID '{}', will CREATE instead", nextId);
						} else if (candidateMatches == null) {
							throw new InvalidRequestException(getContext().getLocalizer().getMessage(BaseHapiFhirSystemDao.class, "transactionOperationFailedUnknownId", nextResouceOperationIn.name(), nextId));
						} else {
							ourLog.debug("Resource with match URL [{}] already exists, will be NOOP", matchUrl);
							persistedResources.add(null);
							retVal.add(nextResource);
							continue;
						}
					}
				} else if (candidateMatches.size() == 1) {
					entity = loadFirstEntityFromCandidateMatches(candidateMatches);
				} else {
					throw new InvalidRequestException(getContext().getLocalizer().getMessage(BaseHapiFhirSystemDao.class, "transactionOperationWithMultipleMatchFailure", nextResouceOperationIn.name(), matchUrl, candidateMatches.size()));
				}
			} else if (nextId.isEmpty() || isPlaceholder(nextId)) {
				entity = null;
			} else {
				entity = tryToLoadEntity(nextId);
			}

			BundleEntryTransactionMethodEnum nextResouceOperationOut;
			if (entity == null) {
				nextResouceOperationOut = BundleEntryTransactionMethodEnum.POST;
//				entity = toEntity(nextResource);
				entity = new ResourceTable();
				populateResourceIntoEntity(nextResource, entity, false);
				entity.setResourceType(resourceName);
				entity.setUpdated(updateTime);
				entity.setPublished(updateTime);
				if (nextId.getIdPart() != null && nextId.getIdPart().startsWith("cid:")) {
					ourLog.debug("Resource in transaction has ID[{}], will replace with server assigned ID", nextId.getIdPart());
				} else if (nextResouceOperationIn == BundleEntryTransactionMethodEnum.POST) {
					if (nextId.isEmpty() == false) {
						ourLog.debug("Resource in transaction has ID[{}] but is marked for CREATE, will ignore ID", nextId.getIdPart());
					}
					if (candidateMatches != null) {
						if (candidateMatches.size() == 1) {
							ourLog.debug("Resource with match URL [{}] already exists, will be NOOP", matchUrl);
							BaseHasResource existingEntity = loadFirstEntityFromCandidateMatches(candidateMatches);
							IResource existing = (IResource) toResource(existingEntity, false);
							persistedResources.add(null);
							retVal.add(existing);
							continue;
						}
						if (candidateMatches.size() > 1) {
							throw new InvalidRequestException(getContext().getLocalizer().getMessage(BaseHapiFhirSystemDao.class, "transactionOperationWithMultipleMatchFailure", BundleEntryTransactionMethodEnum.POST.name(), matchUrl, candidateMatches.size()));
						}
					}
				} else {
					createForcedIdIfNeeded(entity, nextId);
				}
				myEntityManager.persist(entity);
				if (entity.getForcedId() != null) {
					myEntityManager.persist(entity.getForcedId());
				}
				creations++;
				ourLog.info("Resource Type[{}] with ID[{}] does not exist, creating it", resourceName, nextId);
			} else {
				nextResouceOperationOut = nextResouceOperationIn;
				if (nextResouceOperationOut == null) {
					nextResouceOperationOut = BundleEntryTransactionMethodEnum.PUT;
				}
				updates++;
				ourLog.info("Resource Type[{}] with ID[{}] exists, updating it", resourceName, nextId);
			}

			persistedResources.add(entity);
			retVal.add(nextResource);
			ResourceMetadataKeyEnum.ENTRY_TRANSACTION_METHOD.put(nextResource, nextResouceOperationOut);
		}

		ourLog.info("Flushing transaction to database");
		myEntityManager.flush();

		for (int i = 0; i < persistedResources.size(); i++) {
			ResourceTable entity = persistedResources.get(i);

			String resourceName = toResourceName(theResources.get(i));
			IdDt nextId = theResources.get(i).getId();

			IdDt newId;

			if (entity == null) {
				newId = retVal.get(i + 1).getId().toUnqualifiedVersionless();
			} else {
				newId = entity.getIdDt().toUnqualifiedVersionless();
			}

			if (nextId == null || nextId.isEmpty()) {
				ourLog.info("Transaction resource (with no preexisting ID) has been assigned new ID[{}]", nextId, newId);
			} else {
				if (nextId.toUnqualifiedVersionless().equals(newId)) {
					ourLog.info("Transaction resource ID[{}] is being updated", newId);
				} else {
					if (isPlaceholder(nextId)) {
						// nextId = new IdDt(resourceName, nextId.getIdPart());
						ourLog.info("Transaction resource ID[{}] has been assigned new ID[{}]", nextId, newId);
						idConversions.put(nextId, newId);
						idConversions.put(new IdDt(resourceName + "/" + nextId.getValue()), newId);
					}
				}
			}

		}

		for (IResource nextResource : theResources) {
			List<BaseResourceReferenceDt> allRefs = terser.getAllPopulatedChildElementsOfType(nextResource, BaseResourceReferenceDt.class);
			for (BaseResourceReferenceDt nextRef : allRefs) {
				IdDt nextId = nextRef.getReference();
				if (idConversions.containsKey(nextId)) {
					IdDt newId = idConversions.get(nextId);
					ourLog.info(" * Replacing resource ref {} with {}", nextId, newId);
					nextRef.setReference(newId);
				} else {
					ourLog.debug(" * Reference [{}] does not exist in bundle", nextId);
				}
			}
		}

		ourLog.info("Re-flushing updated resource references and extracting search criteria");

		for (int i = 0; i < theResources.size(); i++) {
			IResource resource = theResources.get(i);
			ResourceTable table = persistedResources.get(i);
			if (table == null) {
				continue;
			}

			InstantDt deletedInstantOrNull = ResourceMetadataKeyEnum.DELETED_AT.get(resource);
			Date deletedTimestampOrNull = deletedInstantOrNull != null ? deletedInstantOrNull.getValue() : null;
			if (deletedInstantOrNull == null && ResourceMetadataKeyEnum.ENTRY_TRANSACTION_METHOD.get(resource) == BundleEntryTransactionMethodEnum.DELETE) {
				deletedTimestampOrNull = updateTime;
				ResourceMetadataKeyEnum.DELETED_AT.put(resource, new InstantDt(deletedTimestampOrNull));
			}

			updateEntity(resource, table, deletedTimestampOrNull, updateTime);
		}

		long delay = System.currentTimeMillis() - start;
		ourLog.info("Transaction completed in {}ms with {} creations and {} updates", new Object[] { delay, creations, updates });

		oo.addIssue().setSeverity(IssueSeverityEnum.INFORMATION).setDetails("Transaction completed in " + delay + "ms with " + creations + " creations and " + updates + " updates");

		return retVal;
	}

	private static boolean isPlaceholder(IdDt theId) {
		if (theId.getIdPart() != null && theId.getIdPart().startsWith("cid:")) {
			return true;
		}
		return false;
	}

}
