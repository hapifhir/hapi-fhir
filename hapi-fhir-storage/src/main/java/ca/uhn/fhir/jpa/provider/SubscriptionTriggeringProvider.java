package ca.uhn.fhir.jpa.provider;

/*-
 * #%L
 * HAPI FHIR Storage api
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.jpa.subscription.triggering.ISubscriptionTriggeringSvc;
import ca.uhn.fhir.model.dstu2.valueset.ResourceTypeEnum;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class SubscriptionTriggeringProvider implements IResourceProvider {
	@Autowired
	private FhirContext myFhirContext;
	@Autowired
	private ISubscriptionTriggeringSvc mySubscriptionTriggeringSvc;


	@Operation(name = JpaConstants.OPERATION_TRIGGER_SUBSCRIPTION)
	public IBaseParameters triggerSubscription(
		@OperationParam(name = ProviderConstants.SUBSCRIPTION_TRIGGERING_PARAM_RESOURCE_ID, min = 0, max = OperationParam.MAX_UNLIMITED, typeName = "uri") List<IPrimitiveType<String>> theResourceIds,
		@OperationParam(name = ProviderConstants.SUBSCRIPTION_TRIGGERING_PARAM_SEARCH_URL, min = 0, max = OperationParam.MAX_UNLIMITED, typeName = "string") List<IPrimitiveType<String>> theSearchUrls
	) {
		return mySubscriptionTriggeringSvc.triggerSubscription(theResourceIds, theSearchUrls, null);
	}

	@Operation(name = JpaConstants.OPERATION_TRIGGER_SUBSCRIPTION)
	public IBaseParameters triggerSubscription(
		@IdParam IIdType theSubscriptionId,
		@OperationParam(name = ProviderConstants.SUBSCRIPTION_TRIGGERING_PARAM_RESOURCE_ID, min = 0, max = OperationParam.MAX_UNLIMITED, typeName = "uri") List<IPrimitiveType<String>> theResourceIds,
		@OperationParam(name = ProviderConstants.SUBSCRIPTION_TRIGGERING_PARAM_SEARCH_URL, min = 0, max = OperationParam.MAX_UNLIMITED, typeName = "string") List<IPrimitiveType<String>> theSearchUrls
	) {
		return mySubscriptionTriggeringSvc.triggerSubscription(theResourceIds, theSearchUrls, theSubscriptionId);
	}


	@Override
	public Class<? extends IBaseResource> getResourceType() {
		return myFhirContext.getResourceDefinition(ResourceTypeEnum.SUBSCRIPTION.getCode()).getImplementingClass();
	}

}
