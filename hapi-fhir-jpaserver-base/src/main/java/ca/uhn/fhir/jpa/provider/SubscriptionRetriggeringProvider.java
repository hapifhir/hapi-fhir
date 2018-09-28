package ca.uhn.fhir.jpa.provider;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.dao.IFhirSystemDao;
import ca.uhn.fhir.jpa.subscription.BaseSubscriptionInterceptor;
import ca.uhn.fhir.jpa.subscription.ResourceModifiedMessage;
import ca.uhn.fhir.jpa.util.JpaConstants;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.param.UriParam;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.util.OperationOutcomeUtil;
import ca.uhn.fhir.util.ValidateUtil;
import org.hl7.fhir.dstu3.model.UriType;
import org.hl7.fhir.instance.model.IdType;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class SubscriptionRetriggeringProvider implements IResourceProvider {

	public static final String RESOURCE_ID = "resourceId";
	@Autowired
	private FhirContext myFhirContext;
	@Autowired
	private IFhirSystemDao<?,?> mySystemDao;
	@Autowired(required = false)
	private List<BaseSubscriptionInterceptor<?>> mySubscriptionInterceptorList;

	@Operation(name= JpaConstants.OPERATION_RETRIGGER_SUBSCRIPTION)
	public IBaseOperationOutcome reTriggerSubscription(
		@IdParam IIdType theSubscriptionId,
		@OperationParam(name= RESOURCE_ID) UriParam theResourceId) {

		ValidateUtil.isTrueOrThrowInvalidRequest(theResourceId != null, RESOURCE_ID + " parameter not provided");
		IdType resourceId = new IdType(theResourceId.getValue());
		ValidateUtil.isTrueOrThrowInvalidRequest(resourceId.hasResourceType(), RESOURCE_ID + " parameter must have resource type");
		ValidateUtil.isTrueOrThrowInvalidRequest(resourceId.hasIdPart(), RESOURCE_ID + " parameter must have resource ID part");

		Class<? extends IBaseResource> resourceType = myFhirContext.getResourceDefinition(resourceId.getResourceType()).getImplementingClass();
		IFhirResourceDao<? extends IBaseResource> dao = mySystemDao.getDao(resourceType);
		IBaseResource resourceToRetrigger = dao.read(resourceId);

		ResourceModifiedMessage msg = new ResourceModifiedMessage();
		msg.setId(resourceToRetrigger.getIdElement());
		msg.setOperationType(ResourceModifiedMessage.OperationTypeEnum.UPDATE);
		msg.setSubscriptionId(theSubscriptionId.toUnqualifiedVersionless().getValue());
		msg.setNewPayload(myFhirContext, resourceToRetrigger);

		for (BaseSubscriptionInterceptor<?> next :mySubscriptionInterceptorList) {
			next.submitResourceModified(msg);
		}

		IBaseOperationOutcome retVal = OperationOutcomeUtil.newInstance(myFhirContext);
		OperationOutcomeUtil.addIssue(myFhirContext, retVal, "information", "Triggered resource " + theResourceId.getValue() + " for subscription", null, null);
		return retVal;
	}

	@Override
	public Class<? extends IBaseResource> getResourceType() {
		return myFhirContext.getResourceDefinition("Subscription").getImplementingClass();
	}
}
