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

import static org.apache.commons.lang3.StringUtils.isBlank;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.apache.commons.lang3.time.DateUtils;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.jpa.dao.data.ISubscriptionFlaggedResourceDataDao;
import ca.uhn.fhir.jpa.dao.data.ISubscriptionTableDao;
import ca.uhn.fhir.jpa.entity.ResourceTable;
import ca.uhn.fhir.jpa.entity.SubscriptionFlaggedResource;
import ca.uhn.fhir.jpa.entity.SubscriptionTable;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.dstu.valueset.QuantityCompararatorEnum;
import ca.uhn.fhir.model.dstu2.resource.Subscription;
import ca.uhn.fhir.model.dstu2.valueset.SubscriptionStatusEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.rest.api.SortOrderEnum;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.method.RequestDetails;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.IBundleProvider;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;

public class FhirResourceDaoSubscriptionDstu2 extends FhirResourceDaoDstu2<Subscription>implements IFhirResourceDaoSubscription<Subscription> {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirResourceDaoSubscriptionDstu2.class);

	@Autowired
	private ISubscriptionFlaggedResourceDataDao mySubscriptionFlaggedResourceDataDao;

	@Autowired
	private ISubscriptionTableDao mySubscriptionTableDao;

	@Autowired
	private PlatformTransactionManager myTxManager;

	private void createSubscriptionTable(ResourceTable theEntity, Subscription theSubscription) {
		SubscriptionTable subscriptionEntity = new SubscriptionTable();
		subscriptionEntity.setCreated(new Date());
		subscriptionEntity.setSubscriptionResource(theEntity);
		subscriptionEntity.setNextCheck(theEntity.getPublished().getValue());
		subscriptionEntity.setMostRecentMatch(theEntity.getPublished().getValue());
		subscriptionEntity.setStatus(theSubscription.getStatusElement().getValue());
		myEntityManager.persist(subscriptionEntity);
	}

	@Override
	public Long getSubscriptionTablePidForSubscriptionResource(IIdType theId) {
		ResourceTable entity = readEntityLatestVersion(theId);
		SubscriptionTable table = mySubscriptionTableDao.findOneByResourcePid(entity.getId());
		if (table == null) {
			return null;
		}
		return table.getId();
	}
	
	@Override
	public synchronized List<IBaseResource> getUndeliveredResourcesAndPurge(Long theSubscriptionPid) {
		List<IBaseResource> retVal = new ArrayList<IBaseResource>();
		Page<SubscriptionFlaggedResource> flaggedResources = mySubscriptionFlaggedResourceDataDao.findAllBySubscriptionId(theSubscriptionPid, new PageRequest(0, 100));
		for (SubscriptionFlaggedResource nextFlaggedResource : flaggedResources) {
			retVal.add(toResource(nextFlaggedResource.getResource(), false));
		}

		mySubscriptionFlaggedResourceDataDao.delete(flaggedResources);
		mySubscriptionFlaggedResourceDataDao.flush();

		mySubscriptionTableDao.updateLastClientPoll(new Date());

		return retVal;
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public synchronized int pollForNewUndeliveredResources() {
		if (getConfig().isSubscriptionEnabled() == false) {
			return 0;
		}
		ourLog.trace("Beginning pollForNewUndeliveredResources()");

		// SubscriptionCandidateResource

		Collection<Long> subscriptions = mySubscriptionTableDao.findSubscriptionsWhichNeedToBeChecked(SubscriptionStatusEnum.ACTIVE.getCode(), new Date());

		TransactionTemplate txTemplate = new TransactionTemplate(myTxManager);
		txTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		
		int retVal = 0;
		for (final Long nextSubscriptionTablePid : subscriptions) {
			retVal += txTemplate.execute(new TransactionCallback<Integer>() {
				@Override
				public Integer doInTransaction(TransactionStatus theStatus) {
					SubscriptionTable nextSubscriptionTable = mySubscriptionTableDao.findOne(nextSubscriptionTablePid);
					return pollForNewUndeliveredResources(nextSubscriptionTable);
				}
			});
		}
		
		return retVal;
	}

	private int pollForNewUndeliveredResources(SubscriptionTable theSubscriptionTable) {
		Subscription subscription = toResource(Subscription.class, theSubscriptionTable.getSubscriptionResource(), false);
		RuntimeResourceDefinition resourceDef = validateCriteriaAndReturnResourceDefinition(subscription);
		SearchParameterMap criteriaUrl = translateMatchUrl(subscription.getCriteria(), resourceDef);

		criteriaUrl = new SearchParameterMap();
		long start = theSubscriptionTable.getMostRecentMatch().getTime();
		long end = System.currentTimeMillis() - getConfig().getSubscriptionPollDelay();
		if (end <= start) {
			ourLog.trace("Skipping search for subscription");
			return 0;
		}

		ourLog.debug("Subscription {} search from {} to {}", new Object[] { subscription.getId().getIdPart(), new InstantDt(new Date(start)), new InstantDt(new Date(end)) });

		DateRangeParam range = new DateRangeParam();
		range.setLowerBound(new DateParam(QuantityCompararatorEnum.GREATERTHAN, start));
		range.setUpperBound(new DateParam(QuantityCompararatorEnum.LESSTHAN, end));
		criteriaUrl.setLastUpdated(range);
		criteriaUrl.setSort(new SortSpec(Constants.PARAM_LASTUPDATED, SortOrderEnum.ASC));
		IFhirResourceDao<? extends IBaseResource> dao = getDao(resourceDef.getImplementingClass());
		IBundleProvider results = dao.search(criteriaUrl);
		if (results.size() == 0) {
			return 0;
		}

		ourLog.info("Found {} new results for Subscription {}", results.size(), subscription.getId().getIdPart());

		List<SubscriptionFlaggedResource> flags = new ArrayList<SubscriptionFlaggedResource>();
		Date mostRecentMatch = null;
		for (IBaseResource next : results.getResources(0, results.size())) {

			Date updated = ResourceMetadataKeyEnum.UPDATED.get((IResource) next).getValue();
			if (mostRecentMatch == null) {
				mostRecentMatch = updated;
			} else {
				long mostRecentMatchTime = mostRecentMatch.getTime();
				long updatedTime = updated.getTime();
				if (mostRecentMatchTime < updatedTime) {
					mostRecentMatch = updated;
				}
			}

			SubscriptionFlaggedResource nextFlag = new SubscriptionFlaggedResource();
			Long pid = IDao.RESOURCE_PID.get((IResource) next);
			
			ourLog.info("New resource for subscription: {}", pid);
			
			nextFlag.setResource(myEntityManager.find(ResourceTable.class, pid));
			nextFlag.setSubscription(theSubscriptionTable);
			nextFlag.setVersion(next.getIdElement().getVersionIdPartAsLong());
			flags.add(nextFlag);
		}

		mySubscriptionFlaggedResourceDataDao.save(flags);

		ourLog.debug("Updating most recent match for subcription {} to {}", subscription.getId().getIdPart(), new InstantDt(mostRecentMatch));
		
		theSubscriptionTable.setMostRecentMatch(mostRecentMatch);
		mySubscriptionTableDao.save(theSubscriptionTable);
		
		return results.size();
	}

	@Scheduled(fixedDelay = 10 * DateUtils.MILLIS_PER_SECOND)
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	@Override
	public synchronized void pollForNewUndeliveredResourcesScheduler() {
		if (getConfig().isSchedulingDisabled()) {
			return;
		}
		pollForNewUndeliveredResources();
	}

	@Override
	protected void postPersist(ResourceTable theEntity, Subscription theSubscription) {
		super.postPersist(theEntity, theSubscription);

		createSubscriptionTable(theEntity, theSubscription);
	}

	@Scheduled(fixedDelay = DateUtils.MILLIS_PER_MINUTE)
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	@Override
	public void purgeInactiveSubscriptions() {
		if (getConfig().isSchedulingDisabled()) {
			return;
		}

		Long purgeInactiveAfterMillis = getConfig().getSubscriptionPurgeInactiveAfterMillis();
		if (getConfig().isSubscriptionEnabled() == false || purgeInactiveAfterMillis == null) {
			return;
		}

		Date cutoff = new Date(System.currentTimeMillis() - purgeInactiveAfterMillis);
		Collection<SubscriptionTable> toPurge = mySubscriptionTableDao.findInactiveBeforeCutoff(cutoff);
		for (SubscriptionTable subscriptionTable : toPurge) {

			final IdDt subscriptionId = subscriptionTable.getSubscriptionResource().getIdDt();
			ourLog.info("Deleting inactive subscription {} - Created {}, last client poll {}",
					new Object[] { subscriptionId.toUnqualified(), subscriptionTable.getCreated(), subscriptionTable.getLastClientPoll() });
			TransactionTemplate txTemplate = new TransactionTemplate(myTxManager);
			txTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
			txTemplate.execute(new TransactionCallback<Void>() {
				@Override
				public Void doInTransaction(TransactionStatus theStatus) {
					delete(subscriptionId, new ServletRequestDetails());
					return null;
				}
			});
		}
	}

	@Override
	protected ResourceTable updateEntity(IBaseResource theResource, ResourceTable theEntity, boolean theUpdateHistory, Date theDeletedTimestampOrNull, boolean thePerformIndexing, boolean theUpdateVersion,
			Date theUpdateTime, RequestDetails theRequestDetails) {
		ResourceTable retVal = super.updateEntity(theResource, theEntity, theUpdateHistory, theDeletedTimestampOrNull, thePerformIndexing, theUpdateVersion, theUpdateTime, theRequestDetails);

		Subscription resource = (Subscription) theResource;
		Long resourceId = theEntity.getId();
		if (theDeletedTimestampOrNull != null) {
			Long subscriptionId = getSubscriptionTablePidForSubscriptionResource(theEntity.getIdDt());
			if (subscriptionId != null) {
				mySubscriptionFlaggedResourceDataDao.deleteAllForSubscription(subscriptionId);
				mySubscriptionTableDao.deleteAllForSubscription(subscriptionId);
			}
		} else {
			Query q = myEntityManager.createNamedQuery("Q_HFJ_SUBSCRIPTION_SET_STATUS");
			q.setParameter("res_id", resourceId);
			q.setParameter("status", resource.getStatusElement().getValue());
			if (q.executeUpdate() > 0) {
				ourLog.info("Updated subscription status for subscription {} to {}", resourceId, resource.getStatusElement().getValueAsEnum());
			} else {
				createSubscriptionTable(retVal, resource);
			}
		}
		return retVal;
	}

	private RuntimeResourceDefinition validateCriteriaAndReturnResourceDefinition(Subscription theResource) {
		String query = theResource.getCriteria();
		if (isBlank(query)) {
			throw new UnprocessableEntityException("Subscription.criteria must be populated");
		}

		int sep = query.indexOf('?');
		if (sep <= 1) {
			throw new UnprocessableEntityException("Subscription.criteria must be in the form \"{Resource Type}?[params]\"");
		}

		String resType = query.substring(0, sep);
		if (resType.contains("/")) {
			throw new UnprocessableEntityException("Subscription.criteria must be in the form \"{Resource Type}?[params]\"");
		}

		RuntimeResourceDefinition resDef;
		try {
			resDef = getContext().getResourceDefinition(resType);
		} catch (DataFormatException e) {
			throw new UnprocessableEntityException("Subscription.criteria contains invalid/unsupported resource type: " + resType);
		}
		return resDef;
	}

	@Override
	protected void validateResourceForStorage(Subscription theResource, ResourceTable theEntityToSave, RequestDetails theRequestDetails) {
		super.validateResourceForStorage(theResource, theEntityToSave, theRequestDetails);

		RuntimeResourceDefinition resDef = validateCriteriaAndReturnResourceDefinition(theResource);

		IFhirResourceDao<? extends IBaseResource> dao = getDao(resDef.getImplementingClass());
		if (dao == null) {
			throw new UnprocessableEntityException("Subscription.criteria contains invalid/unsupported resource type: " + resDef);
		}

		if (theResource.getChannel().getType() == null) {
			throw new UnprocessableEntityException("Subscription.channel.type must be populated on this server");
		}

		SubscriptionStatusEnum status = theResource.getStatusElement().getValueAsEnum();
		if (status == null) {
			throw new UnprocessableEntityException("Subscription.status must be populated on this server");
		}

	}

}
