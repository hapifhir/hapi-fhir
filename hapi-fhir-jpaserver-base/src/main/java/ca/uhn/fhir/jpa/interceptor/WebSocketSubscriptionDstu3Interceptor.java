/*
 *  Copyright 2016 Cognitive Medical Systems, Inc (http://www.cognitivemedicine.com).
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package ca.uhn.fhir.jpa.interceptor;

import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.jpa.dao.*;
import ca.uhn.fhir.jpa.dao.data.ISubscriptionFlaggedResourceDataDao;
import ca.uhn.fhir.jpa.dao.data.ISubscriptionTableDao;
import ca.uhn.fhir.jpa.dao.dstu3.FhirResourceDaoSubscriptionDstu3;
import ca.uhn.fhir.jpa.entity.ResourceTable;
import ca.uhn.fhir.jpa.entity.SubscriptionFlaggedResource;
import ca.uhn.fhir.jpa.entity.SubscriptionTable;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.dstu.valueset.QuantityCompararatorEnum;
import ca.uhn.fhir.model.dstu2.valueset.SubscriptionStatusEnum;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.api.SortOrderEnum;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.method.RequestDetails;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.IBundleProvider;
import ca.uhn.fhir.rest.server.exceptions.AuthenticationException;
import ca.uhn.fhir.rest.server.interceptor.InterceptorAdapter;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.dstu3.model.Subscription;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.DependsOn;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Created by Jeff on 11/10/2016.
 */
public class WebSocketSubscriptionDstu3Interceptor extends InterceptorAdapter implements IJpaServerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketSubscriptionDstu3Interceptor.class);

    //todo sometimes throws an exception on startup
    @Autowired
    @Qualifier("mySubscriptionDaoDstu3")
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