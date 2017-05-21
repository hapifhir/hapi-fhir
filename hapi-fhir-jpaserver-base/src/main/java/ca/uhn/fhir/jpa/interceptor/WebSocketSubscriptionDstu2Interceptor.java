
package ca.uhn.fhir.jpa.interceptor;

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
import ca.uhn.fhir.jpa.entity.ResourceTable;
import ca.uhn.fhir.model.dstu2.resource.Subscription;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.method.RequestDetails;
import ca.uhn.fhir.rest.server.interceptor.InterceptorAdapter;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.annotation.PostConstruct;

public class WebSocketSubscriptionDstu2Interceptor extends InterceptorAdapter implements IJpaServerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketSubscriptionDstu2Interceptor.class);

    @Autowired
    @Qualifier("mySubscriptionDaoDstu2")
    private IFhirResourceDao<Subscription> reference;

    private IFhirResourceDaoSubscription<Subscription> casted;

    @PostConstruct
    public void postConstruct(){
        casted = (IFhirResourceDaoSubscription) reference;
    }

    @Override
    public void resourceCreated(ActionRequestDetails theDetails, ResourceTable theResourceTable) {
    }

    @Override
    public void resourceUpdated(ActionRequestDetails theDetails, ResourceTable theResourceTable) {
    }

    @Override
    public void resourceDeleted(ActionRequestDetails theDetails, ResourceTable theResourceTable) {
    }

    /**
     * Checks for websocket subscriptions
     * @param theRequestDetails
     *           A bean containing details about the request that is about to be processed, including details such as the
     *           resource type and logical ID (if any) and other FHIR-specific aspects of the request which have been
     *           pulled out of the {@link HttpServletRequest servlet request}.
     * @param theResponseObject
     *           The actual object which is being streamed to the client as a response
     * @return
     */
    @Override
    public boolean outgoingResponse(RequestDetails theRequestDetails, IBaseResource theResponseObject) {
        if (theRequestDetails.getResourceName() == null ||
                theRequestDetails.getResourceName().isEmpty() ||
                theRequestDetails.getResourceName().equals("Subscription")) {
            return super.outgoingResponse(theRequestDetails, theResponseObject);
        }

        if (theRequestDetails.getRequestType().equals(RequestTypeEnum.POST) || theRequestDetails.getRequestType().equals(RequestTypeEnum.PUT)) {
            logger.info("Found POST or PUT for a non-subscription resource");
            casted.pollForNewUndeliveredResources(theRequestDetails.getResourceName());
        }

        return super.outgoingResponse(theRequestDetails, theResponseObject);
    }
}
