
package ca.uhn.fhir.jpa.subscription.dstu3;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ca.uhn.fhir.jpa.subscription.BaseSubscriptionWebsocketInterceptor;
import ca.uhn.fhir.jpa.subscription.CanonicalSubscription;
import org.hl7.fhir.dstu3.model.Subscription;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/*-
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

import ca.uhn.fhir.jpa.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.dao.IFhirResourceDaoSubscription;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.AuthenticationException;
import ca.uhn.fhir.rest.server.interceptor.ServerOperationInterceptorAdapter;

public class WebSocketSubscriptionDstu3Interceptor extends BaseSubscriptionWebsocketInterceptor {

	@Autowired
	@Qualifier("mySubscriptionDaoDstu3")
	private IFhirResourceDao<org.hl7.fhir.dstu3.model.Subscription> mySubscriptionDao;

	@Override
	public org.hl7.fhir.r4.model.Subscription.SubscriptionChannelType getChannelType() {
		return org.hl7.fhir.r4.model.Subscription.SubscriptionChannelType.WEBSOCKET;
	}

	@Override
	protected CanonicalSubscription canonicalize(IBaseResource theSubscription) {
		return RestHookSubscriptionDstu3Interceptor.doCanonicalize(theSubscription);
	}


	@Override
	protected IFhirResourceDao<?> getSubscriptionDao() {
		return mySubscriptionDao;
	}
}
