package ca.uhn.fhir.jpa.provider;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2018 University Health Network
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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.dao.IFhirSystemDao;
import ca.uhn.fhir.jpa.subscription.BaseSubscriptionInterceptor;
import ca.uhn.fhir.jpa.subscription.ResourceModifiedMessage;
import ca.uhn.fhir.jpa.util.JpaConstants;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.param.UriParam;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.util.ParametersUtil;
import ca.uhn.fhir.util.ValidateUtil;
import org.hl7.fhir.instance.model.IdType;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
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
	public IBaseParameters reTriggerSubscription(
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

		IBaseParameters retVal = ParametersUtil.newInstance(myFhirContext);
		IPrimitiveType<?> value = (IPrimitiveType<?>) myFhirContext.getElementDefinition("string").newInstance();
		value.setValueAsString("Triggered resource " + theResourceId.getValue() + " for subscription");
		ParametersUtil.addParameterToParameters(myFhirContext, retVal, "information", value);
		return retVal;
	}

	@Override
	public Class<? extends IBaseResource> getResourceType() {
		return myFhirContext.getResourceDefinition("Subscription").getImplementingClass();
	}
}
