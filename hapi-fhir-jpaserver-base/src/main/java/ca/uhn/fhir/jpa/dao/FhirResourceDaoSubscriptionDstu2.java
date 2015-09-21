package ca.uhn.fhir.jpa.dao;

import static org.apache.commons.lang3.StringUtils.isBlank;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.time.DateUtils;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.jpa.entity.ResourceTable;
import ca.uhn.fhir.jpa.entity.SubscriptionTable;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.dstu2.resource.Subscription;
import ca.uhn.fhir.model.dstu2.valueset.SubscriptionStatusEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.util.UrlUtil;
import ca.uhn.fhir.util.UrlUtil.UrlParts;

public class FhirResourceDaoSubscriptionDstu2 extends FhirResourceDaoDstu2<Subscription>implements IFhirResourceDaoSubscription<Subscription> {

	private static final ResourceMetadataKeyEnum<Object> ALLOW_STATUS_CHANGE = new ResourceMetadataKeyEnum<Object>(FhirResourceDaoSubscriptionDstu2.class.getName() + "_ALLOW_STATUS_CHANGE") {
		private static final long serialVersionUID = 1;

		@Override
		public Object get(IResource theResource) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void put(IResource theResource, Object theObject) {
			throw new UnsupportedOperationException();
		}
	};

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirResourceDaoSubscriptionDstu2.class);

	private void createSubscriptionTable(ResourceTable theEntity, Subscription theSubscription) {
		SubscriptionTable subscriptionEntity = new SubscriptionTable();
		subscriptionEntity.setSubscriptionResource(theEntity);
		subscriptionEntity.setNextCheck(theEntity.getPublished().getValue());
		subscriptionEntity.setNextCheckSince(theEntity.getPublished().getValue());
		subscriptionEntity.setStatus(theSubscription.getStatusElement().getValueAsEnum());
		myEntityManager.persist(subscriptionEntity);
	}

	@Override
	public SubscriptionTable getSubscriptionByResourceId(long theSubscriptionResourceId) {
		TypedQuery<SubscriptionTable> q = myEntityManager.createNamedQuery("Q_HFJ_SUBSCRIPTION_GET_BY_RES", SubscriptionTable.class);
		q.setParameter("res_id", theSubscriptionResourceId);
		return q.getSingleResult();
	}

	

	@Scheduled(fixedDelay = 10 * DateUtils.MILLIS_PER_SECOND)
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	@Override
	public void pollForNewUndeliveredResources() {
		if (getConfig().isSubscriptionEnabled() == false) {
			return;
		}
		ourLog.trace("Beginning pollForNewUndeliveredResources()");

		TypedQuery<SubscriptionTable> q = myEntityManager.createNamedQuery("Q_HFJ_SUBSCRIPTION_NEXT_CHECK", SubscriptionTable.class);
		q.setParameter("next_check", new Date());
		q.setParameter("status", SubscriptionStatusEnum.ACTIVE);
		List<SubscriptionTable> subscriptions = q.getResultList();

		
	}

	@Override
	protected void postPersist(ResourceTable theEntity, Subscription theSubscription) {
		super.postPersist(theEntity, theSubscription);

		createSubscriptionTable(theEntity, theSubscription);
	}

	@Override
	public void setSubscriptionStatus(Long theResourceId, SubscriptionStatusEnum theStatus) {
		Validate.notNull(theResourceId);
		Validate.notNull(theStatus);

		ResourceTable existing = readEntityLatestVersion(new IdDt("Subscription", theResourceId));
		Subscription existingRes = toResource(Subscription.class, existing, false);

		existingRes.getResourceMetadata().put(ALLOW_STATUS_CHANGE, new Object());
		existingRes.setStatus(theStatus);

		update(existingRes);
	}

	@Override
	protected ResourceTable updateEntity(IResource theResource, ResourceTable theEntity, boolean theUpdateHistory, Date theDeletedTimestampOrNull, boolean thePerformIndexing, boolean theUpdateVersion, Date theUpdateTime) {
		ResourceTable retVal = super.updateEntity(theResource, theEntity, theUpdateHistory, theDeletedTimestampOrNull, thePerformIndexing, theUpdateVersion, theUpdateTime);

		Subscription resource = (Subscription) theResource;
		Long resourceId = theEntity.getId();
		if (theDeletedTimestampOrNull != null) {
			Query q = myEntityManager.createNamedQuery("Q_HFJ_SUBSCRIPTION_DELETE");
			q.setParameter("res_id", resourceId);
			q.executeUpdate();
		} else {
			Query q = myEntityManager.createNamedQuery("Q_HFJ_SUBSCRIPTION_SET_STATUS");
			q.setParameter("res_id", resourceId);
			q.setParameter("status", resource.getStatusElement().getValueAsEnum());
			if (q.executeUpdate() > 0) {
				ourLog.info("Updated subscription status for subscription {} to {}", resourceId, resource.getStatusElement().getValueAsEnum());
			} else {
				createSubscriptionTable(retVal, resource);
			}
		}
		return retVal;
	}

	@Override
	protected void validateResourceForStorage(Subscription theResource, ResourceTable theEntityToSave) {
		super.validateResourceForStorage(theResource, theEntityToSave);

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

		IFhirResourceDao<? extends IBaseResource> dao = getDao(resDef.getImplementingClass());
		if (dao == null) {
			throw new UnprocessableEntityException("Subscription.criteria contains invalid/unsupported resource type: " + resDef);
		}

//		SearchParameterMap parsedUrl = translateMatchUrl(query, resDef);
		
		if (theResource.getChannel().getType() == null) {
			throw new UnprocessableEntityException("Subscription.channel.type must be populated on this server");
		}

		SubscriptionStatusEnum status = theResource.getStatusElement().getValueAsEnum();
		Subscription existing = theEntityToSave.getEncoding() != null ? toResource(Subscription.class, theEntityToSave, false) : null;
		if (status == null) {
			// if (existing != null) {
			// status = existing.getStatusElement().getValueAsEnum();
			// theResource.setStatus(status);
			// } else {
			status = SubscriptionStatusEnum.REQUESTED;
			theResource.setStatus(status);
			// }
		} else {
			SubscriptionStatusEnum existingStatus = existing.getStatusElement().getValueAsEnum();
			if (existingStatus != status) {
				if (!theResource.getResourceMetadata().containsKey(ALLOW_STATUS_CHANGE)) {
					throw new UnprocessableEntityException("Subscription.status can not be changed from " + existingStatus + " to " + status);
				}
			}
		}

		if (theEntityToSave.getId() == null) {
			if (status != SubscriptionStatusEnum.REQUESTED) {
				throw new UnprocessableEntityException("Subscription.status must be " + SubscriptionStatusEnum.REQUESTED.getCode() + " on a newly created subscription");
			}
		}

	}

}
