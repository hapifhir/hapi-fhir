package ca.uhn.fhir.jpa.dao;

import static org.apache.commons.lang3.StringUtils.isBlank;

import java.util.Date;
import java.util.List;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.time.DateUtils;
import org.hl7.fhir.instance.model.api.IBaseResource;
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
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;

public class FhirResourceDaoSubscriptionDstu2 extends FhirResourceDaoDstu2<Subscription>implements IFhirResourceDaoSubscription<Subscription> {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirResourceDaoSubscriptionDstu2.class);

	private void createSubscriptionTable(ResourceTable theEntity, Subscription theSubscription) {
		SubscriptionTable subscriptionEntity = new SubscriptionTable();
		subscriptionEntity.setSubscriptionResource(theEntity);
		subscriptionEntity.setNextCheck(theEntity.getPublished().getValue());
		subscriptionEntity.setNextCheckSince(theEntity.getPublished().getValue());
		subscriptionEntity.setStatus(theSubscription.getStatusElement().getValueAsEnum());
		myEntityManager.persist(subscriptionEntity);
	}

	@Scheduled(fixedDelay = 10 * DateUtils.MILLIS_PER_SECOND)
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	@Override
	public void pollForNewUndeliveredResources() {
		if (getConfig().isSubscriptionEnabled() == false) {
			return;
		}
		ourLog.trace("Beginning pollForNewUndeliveredResources()");

//		SubscriptionCandidateResource
		
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

		if (theResource.getChannel().getType() == null) {
			throw new UnprocessableEntityException("Subscription.channel.type must be populated on this server");
		}

		SubscriptionStatusEnum status = theResource.getStatusElement().getValueAsEnum();
		if (status == null) {
			throw new UnprocessableEntityException("Subscription.status must be populated on this server");
		}

	}

}
