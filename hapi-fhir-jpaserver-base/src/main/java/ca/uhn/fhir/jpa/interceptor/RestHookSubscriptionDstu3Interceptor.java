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
import ca.uhn.fhir.jpa.dao.BaseHapiFhirDao;
import ca.uhn.fhir.jpa.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.dao.SearchParameterMap;
import ca.uhn.fhir.jpa.dao.dstu3.FhirResourceDaoSubscriptionDstu3;
import ca.uhn.fhir.jpa.entity.ResourceTable;
import ca.uhn.fhir.jpa.util.SpringObjectCaster;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.EncodingEnum;
import ca.uhn.fhir.rest.server.IBundleProvider;
import ca.uhn.fhir.rest.server.interceptor.InterceptorAdapter;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.hl7.fhir.dstu3.model.Subscription;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.apache.http.protocol.HTTP.USER_AGENT;

public class RestHookSubscriptionDstu3Interceptor extends InterceptorAdapter implements IJpaServerInterceptor {

    @Autowired
    @Qualifier("mySubscriptionDaoDstu3")
    private IFhirResourceDao<Subscription> mySubscriptionDao;

    private FhirResourceDaoSubscriptionDstu3 myResourceSubscriptionDao;

    private static final Logger logger = LoggerFactory.getLogger(RestHookSubscriptionDstu3Interceptor.class);
    private final List<Subscription> restHookSubscriptions = new ArrayList<Subscription>();

    @PostConstruct
    public void postConstruct(){
        try {
            myResourceSubscriptionDao = SpringObjectCaster.getTargetObject(mySubscriptionDao, FhirResourceDaoSubscriptionDstu3.class);
        }catch (Exception e){
            throw new RuntimeException("Unable to get DAO from PROXY");
        }
    }

    /**
     * Read the existing subscriptions from the database
     */
    public void initSubscriptions(){
        SearchParameterMap map = new SearchParameterMap();
        map.add(Subscription.SP_TYPE, new TokenParam(null, Subscription.SubscriptionChannelType.RESTHOOK.toCode()));
        map.add(Subscription.SP_STATUS, new TokenParam(null, Subscription.SubscriptionStatus.ACTIVE.toCode()));

        IBundleProvider subscriptionBundleList = mySubscriptionDao.search(map);
        List<IBaseResource> resourceList = subscriptionBundleList.getResources(0, subscriptionBundleList.size());

        for(IBaseResource resource : resourceList){
            restHookSubscriptions.add((Subscription) resource);
        }
    }

    /**
     * Handles incoming resources.  If the resource is a rest-hook subscription, it adds
     * it to the rest-hook subscription list.  Otherwise it checks to see if the resource
     * matches any rest-hook subscriptions.
     *
     * @param theDetails       The request details
     * @param theResourceTable The actual created entity
     */
    @Override
    public void resourceCreated(ActionRequestDetails theDetails, ResourceTable theResourceTable) {
       String resourceType = theDetails.getResourceType();
       IIdType idType = theDetails.getId();
        logger.info("resource created type: " + resourceType);
        if(resourceType.equals(Subscription.class.getSimpleName())){
            Subscription subscription = (Subscription)theDetails.getResource();
            if (subscription.getChannel() != null && subscription.getChannel().getType() == Subscription.SubscriptionChannelType.RESTHOOK && subscription.getStatus() == Subscription.SubscriptionStatus.REQUESTED) {
                subscription.setStatus(Subscription.SubscriptionStatus.ACTIVE);
                mySubscriptionDao.update(subscription);
                restHookSubscriptions.add(subscription);
            }
        } else {
            checkSubscriptions(idType, resourceType);
        }
    }

    /**
     * Check subscriptions
     * @param idType
     * @param resourceType
     */
    private void checkSubscriptions(IIdType idType, String resourceType) {
        //handle any object besides subscription, such as a new observation
        for (Subscription subscription : restHookSubscriptions) {
            //see if the criteria matches the created object
            logger.info("subscription for " + resourceType + " with criteria " + subscription.getCriteria());
            if (resourceType != null && subscription.getCriteria() != null && !subscription.getCriteria().startsWith(resourceType)) {
                logger.info("Skipping subscription search for " + resourceType + " because it does not match the criteria " + subscription.getCriteria());
                continue;
            }

            RuntimeResourceDefinition resourceDef = myResourceSubscriptionDao.validateCriteriaAndReturnResourceDefinition(subscription);
            SearchParameterMap criteriaUrl = BaseHapiFhirDao.translateMatchUrl(myResourceSubscriptionDao.getContext(), subscription.getCriteria(), resourceDef);

            IFhirResourceDao<? extends IBaseResource> dao = myResourceSubscriptionDao.getDao(resourceDef.getImplementingClass());
            IBundleProvider results = dao.search(criteriaUrl);
            if (results.size() == 0) {
                continue;
            }

            for (IBaseResource nextBase : results.getResources(0, results.size())) {
                IAnyResource next = (IAnyResource) nextBase;

                if (idType.getIdPart().equals(next.getIdElement().getIdPart())) {
                    logger.info("found match: Sending rest-hook notification for resource: {}", next.getIdElement());

                    EncodingEnum encoding = EncodingEnum.JSON;
                    String criteria = subscription.getCriteria();
                    String params = criteria.substring(criteria.indexOf('?') + 1);
                    List<NameValuePair> paramValues = URLEncodedUtils.parse(params, Constants.CHARSET_UTF8, '&');
                    for (NameValuePair nameValuePair : paramValues) {
                        if (Constants.PARAM_FORMAT.equals(nameValuePair.getName())) {
                            encoding = EncodingEnum.forContentType(nameValuePair.getValue());
                        }
                    }

                    String encoded = encoding.newParser(mySubscriptionDao.getContext()).encodeResourceToString(next);
                    StringEntity entity;
                    if (encoded.equalsIgnoreCase(EncodingEnum.JSON.name())) {
                        entity = new StringEntity(encoded, ContentType.APPLICATION_JSON);
                    } else {
                        entity = new StringEntity(encoded, ContentType.APPLICATION_XML);
                    }

                    String url = subscription.getChannel().getEndpoint();
                    HttpClient client = HttpClientBuilder.create().build();
                    HttpPost request = new HttpPost(url);
                    request.setEntity(entity);
                    request.addHeader("User-Agent", USER_AGENT);
                    try {
                        HttpResponse response = client.execute(request);
                    } catch (IOException e) {
                        logger.error("Error sending rest post call from subscription " + subscription.getId() + " with endpoint " + url);
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * Checks for updates to subscriptions or if an update to a resource matches
     * a rest-hook subscription
     * @param theDetails The request details
     * @param theResourceTable The actual updated entity
     */
    @Override
    public void resourceUpdated(ActionRequestDetails theDetails, ResourceTable theResourceTable) {
        String resourceType = theDetails.getResourceType();
        IIdType idType = theDetails.getId();

        logger.info("resource updated type: " + resourceType);

        if(theDetails.getResourceType().equals(Subscription.class.getSimpleName())){
            Subscription subscription = (Subscription)theDetails.getResource();
            if (subscription.getChannel() != null && subscription.getChannel().getType() == Subscription.SubscriptionChannelType.RESTHOOK) {
                removeLocalSubscription(subscription.getIdElement().getIdPart());

                if(subscription.getStatus() == Subscription.SubscriptionStatus.ACTIVE){
                    restHookSubscriptions.add(subscription);
                    logger.info("Subscription was updated. Id: " + subscription.getId());
                }
            }
        } else {
            checkSubscriptions(idType, resourceType);
        }
    }

    /**
     * Checks to remove a rest-hook subscription
     * @param theDetails The request details
     * @param theResourceTable The actual updated entity
     */
    @Override
    public void resourceDeleted(ActionRequestDetails theDetails, ResourceTable theResourceTable) {
        logger.info("resource removed type: " + theDetails.getResourceType());

        if(theDetails.getResourceType().equals(Subscription.class.getSimpleName())) {
            String id = theDetails.getId().getIdPart();
            removeLocalSubscription(id);
        }
    }

    /**
     * Remove subscription from cache
     * @param subscriptionId
     */
    private void removeLocalSubscription(String subscriptionId){
        Subscription localSubscription = getLocalSubscription(subscriptionId);
        if(localSubscription != null) {
            restHookSubscriptions.remove(localSubscription);
            logger.info("Subscription removed: " + subscriptionId);
        }else{
            logger.info("Subscription not found in local list. Subscription id: " + subscriptionId);
        }
    }

    /**
     * Get subscription from cache
     * @param id
     * @return
     */
    private Subscription getLocalSubscription(String id){
        if(id != null && !id.trim().isEmpty()) {
            int size = restHookSubscriptions.size();
            if (size > 0) {
                for (Subscription restHookSubscription : restHookSubscriptions) {
                    if (id.equals(restHookSubscription.getIdElement().getIdPart())) {
                        return restHookSubscription;
                    }
                }
            }
        }

        return null;
    }
}