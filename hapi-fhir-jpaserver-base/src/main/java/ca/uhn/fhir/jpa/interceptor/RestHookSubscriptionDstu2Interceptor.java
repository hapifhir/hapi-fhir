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
import ca.uhn.fhir.jpa.dao.FhirResourceDaoSubscriptionDstu2;
import ca.uhn.fhir.jpa.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.dao.SearchParameterMap;
import ca.uhn.fhir.jpa.entity.ResourceTable;
import ca.uhn.fhir.jpa.thread.HttpRequestDstu2Job;
import ca.uhn.fhir.jpa.util.SpringObjectCaster;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Observation;
import ca.uhn.fhir.model.dstu2.resource.Subscription;
import ca.uhn.fhir.model.dstu2.valueset.SubscriptionChannelTypeEnum;
import ca.uhn.fhir.model.dstu2.valueset.SubscriptionStatusEnum;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.method.RequestDetails;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.EncodingEnum;
import ca.uhn.fhir.rest.server.IBundleProvider;
import ca.uhn.fhir.rest.server.exceptions.AuthenticationException;
import ca.uhn.fhir.rest.server.interceptor.InterceptorAdapter;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.apache.http.protocol.HTTP.USER_AGENT;

/**
 * Adds the capability for DSTU2 rest-hook subscriptions.  It is compatible with
 * DSTU2 websocket subscriptions, but not with DSTU3 subscriptions
 */
public class RestHookSubscriptionDstu2Interceptor extends InterceptorAdapter implements IJpaServerInterceptor {

    @Autowired
    @Qualifier("mySubscriptionDaoDstu2")
    private IFhirResourceDao<Subscription> mySubscriptionDao;

    private static volatile ExecutorService executor;
    private FhirResourceDaoSubscriptionDstu2 myResourceSubscriptionDao;

    private static final Logger logger = LoggerFactory.getLogger(RestHookSubscriptionDstu2Interceptor.class);
    private final List<Subscription> restHookSubscriptions = new ArrayList<Subscription>();
    private boolean notifyOnDelete = false;

    private final static int MAX_THREADS = 1;

    @PostConstruct
    public void postConstruct() {
        try {
            executor = Executors.newFixedThreadPool(MAX_THREADS);
            myResourceSubscriptionDao = SpringObjectCaster.getTargetObject(mySubscriptionDao, FhirResourceDaoSubscriptionDstu2.class);
        } catch (Exception e) {
            throw new RuntimeException("Unable to get DAO from PROXY");
        }
    }

    /**
     * Read the existing subscriptions from the database
     */
    public void initSubscriptions() {
        SearchParameterMap map = new SearchParameterMap();
        map.add(Subscription.SP_TYPE, new TokenParam(null, SubscriptionChannelTypeEnum.REST_HOOK.getCode()));
        map.add(Subscription.SP_STATUS, new TokenParam(null, SubscriptionStatusEnum.ACTIVE.getCode()));

        IBundleProvider subscriptionBundleList = mySubscriptionDao.search(map);
        List<IBaseResource> resourceList = subscriptionBundleList.getResources(0, subscriptionBundleList.size());

        for (IBaseResource resource : resourceList) {
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
        if (resourceType.equals(Subscription.class.getSimpleName())) {
            Subscription subscription = (Subscription) theDetails.getResource();
            if (subscription.getChannel() != null
                    && subscription.getChannel().getType().equals(SubscriptionChannelTypeEnum.REST_HOOK.getCode())
                    && subscription.getStatus().equals(SubscriptionStatusEnum.REQUESTED.getCode())) {
                subscription.setStatus(SubscriptionStatusEnum.ACTIVE);
                mySubscriptionDao.update(subscription);
                restHookSubscriptions.add(subscription);
                logger.info("Subscription was added. Id: " + subscription.getId());
            }
        } else {
            checkSubscriptions(idType, resourceType);
        }
    }

    /**
     * Checks for updates to subscriptions or if an update to a resource matches
     * a rest-hook subscription
     *
     * @param theDetails       The request details
     * @param theResourceTable The actual updated entity
     */
    @Override
    public void resourceUpdated(ActionRequestDetails theDetails, ResourceTable theResourceTable) {
        String resourceType = theDetails.getResourceType();
        IIdType idType = theDetails.getId();

        logger.info("resource updated type: " + resourceType);

        if (resourceType.equals(Subscription.class.getSimpleName())) {
            Subscription subscription = (Subscription) theDetails.getResource();
            if (subscription.getChannel() != null && subscription.getChannel().getType().equals(SubscriptionChannelTypeEnum.REST_HOOK.getCode())) {
                removeLocalSubscription(subscription.getId().getIdPart());

                if (subscription.getStatus().equals(SubscriptionStatusEnum.ACTIVE.getCode())) {
                    restHookSubscriptions.add(subscription);
                    logger.info("Subscription was updated. Id: " + subscription.getId());
                }
            }
        } else {
            checkSubscriptions(idType, resourceType);
        }
    }

    /**
     * Check subscriptions to see if there is a matching subscription when there is delete
     *
     * @param theRequestDetails A bean containing details about the request that is about to be processed, including details such as the
     *                          resource type and logical ID (if any) and other FHIR-specific aspects of the request which have been
     *                          pulled out of the {@link HttpServletRequest servlet request}.
     * @param theRequest        The incoming request
     * @param theResponse       The response. Note that interceptors may choose to provide a response (i.e. by calling
     *                          {@link HttpServletResponse#getWriter()}) but in that case it is important to return <code>false</code>
     *                          to indicate that the server itself should not also provide a response.
     * @return
     * @throws AuthenticationException
     */
    @Override
    public boolean incomingRequestPostProcessed(RequestDetails theRequestDetails, HttpServletRequest theRequest, HttpServletResponse theResponse) throws AuthenticationException {
        if (theRequestDetails.getRestOperationType().equals(RestOperationTypeEnum.DELETE)) {
            String resourceType = theRequestDetails.getResourceName();
            IIdType idType = theRequestDetails.getId();

            if (resourceType.equals(Subscription.class.getSimpleName())) {
                String id = idType.getIdPart();
                removeLocalSubscription(id);
            } else {
                if (notifyOnDelete) {
                    checkSubscriptions(idType, resourceType);
                }
            }
        }

        return super.incomingRequestPostProcessed(theRequestDetails, theRequest, theResponse);
    }

    /**
     * Check subscriptions and send notifications or payload
     *
     * @param idType
     * @param resourceType
     */
    private void checkSubscriptions(IIdType idType, String resourceType) {
        for (Subscription subscription : restHookSubscriptions) {
            //see if the criteria matches the created object
            logger.info("subscription for " + resourceType + " with criteria " + subscription.getCriteria());
            if (resourceType != null && subscription.getCriteria() != null && !subscription.getCriteria().startsWith(resourceType)) {
                logger.info("Skipping subscription search for " + resourceType + " because it does not match the criteria " + subscription.getCriteria());
                continue;
            }
            //run the subscriptions query and look for matches, add the id as part of the criteria to avoid getting matches of previous resources rather than the recent resource
            String criteria = subscription.getCriteria();
            criteria += "&_id=" + idType.getResourceType() + "/" + idType.getIdPart();
            IBundleProvider results = getBundleProvider(criteria);

            if (results.size() == 0) {
                continue;
            }
            Observation aa;
            //should just be one resource as it was filtered by the id
            for (IBaseResource nextBase : results.getResources(0, results.size())) {
                IResource next = (IResource) nextBase;
                logger.info("Found match: queueing rest-hook notification for resource: {}", next.getIdElement());
                HttpUriRequest request = createRequest(subscription, next);
                executor.submit(new HttpRequestDstu2Job(request, subscription));
            }
        }
    }

    /**
     * Creates an HTTP Post for a subscription
     *
     * @param subscription
     * @param resource
     * @return
     */
    private HttpUriRequest createRequest(Subscription subscription, IResource resource) {
        String url = subscription.getChannel().getEndpoint();
        HttpUriRequest request = null;
        String payload = subscription.getChannel().getPayload();
        //HTTP post
        if (payload == null || payload.trim().length() == 0) {
            //return an empty response as there is no payload
            logger.info("No payload found, returning an empty notification");
            request = new HttpPost(url);
        }
        //HTTP put
        else if (payload.equals("application/xml") || payload.equals("application/fhir+xml")) {
            logger.info("XML payload found");
            StringEntity entity = getStringEntity(EncodingEnum.XML, resource);
            HttpPut putRequest = new HttpPut();
            putRequest.setEntity(entity);

            request = putRequest;
        }
        //HTTP put
        else if (payload.equals("application/json") || payload.equals("application/fhir+json")) {
            logger.info("JSON payload found");
            StringEntity entity = getStringEntity(EncodingEnum.JSON, resource);
            HttpPut putRequest = new HttpPut();
            putRequest.setEntity(entity);

            request = putRequest;
        }
        //HTTP post
        else if (payload.startsWith("application/fhir+query/")) { //custom payload that is a FHIR query
            logger.info("Custom query payload found");
            String responseCriteria = subscription.getChannel().getPayload().substring(23);
            //get the encoding type from payload which is a FHIR query with &_format=
            EncodingEnum encoding = getEncoding(responseCriteria);
            IBundleProvider responseResults = getBundleProvider(responseCriteria);
            if (responseResults.size() != 0) {
                List<IBaseResource> resourcelist = responseResults.getResources(0, responseResults.size());
                Bundle bundle = createBundle(resourcelist);
                StringEntity bundleEntity = getStringEntity(encoding, bundle);
                HttpPost postRequest = new HttpPost();
                postRequest.setEntity(bundleEntity);

                request = postRequest;
            } else {
                Bundle bundle = new Bundle();
                bundle.setTotal(0);
                StringEntity bundleEntity = getStringEntity(encoding, bundle);
                HttpPost postRequest = new HttpPost();
                postRequest.setEntity(bundleEntity);

                request = postRequest;
            }

        } else {
            logger.warn("Unsupported payload " + payload + ". Returning an empty notification");
            request = new HttpPost(url);
        }

        //request.addHeader("User-Agent", USER_AGENT);
        return request;
    }

    /**
     * Get the encoding from the criteria or return JSON encoding if its not found
     *
     * @param criteria
     * @return
     */
    private EncodingEnum getEncoding(String criteria) {
        //check criteria
        String params = criteria.substring(criteria.indexOf('?') + 1);
        List<NameValuePair> paramValues = URLEncodedUtils.parse(params, Constants.CHARSET_UTF8, '&');
        for (NameValuePair nameValuePair : paramValues) {
            if (Constants.PARAM_FORMAT.equals(nameValuePair.getName())) {
                return EncodingEnum.forContentType(nameValuePair.getValue());
            }
        }
        return EncodingEnum.JSON;
    }

    /**
     * Search based on a query criteria
     *
     * @param criteria
     * @return
     */
    private IBundleProvider getBundleProvider(String criteria) {
        Subscription subscription = new Subscription();
        subscription.setCriteria(criteria);

        RuntimeResourceDefinition responseResourceDef = myResourceSubscriptionDao.validateCriteriaAndReturnResourceDefinition(subscription);
        SearchParameterMap responseCriteriaUrl = BaseHapiFhirDao.translateMatchUrl(myResourceSubscriptionDao.getContext(), criteria, responseResourceDef);

        IFhirResourceDao<? extends IBaseResource> responseDao = myResourceSubscriptionDao.getDao(responseResourceDef.getImplementingClass());
        IBundleProvider responseResults = responseDao.search(responseCriteriaUrl);
        return responseResults;
    }

    /**
     * Create a bundle to return to the client
     *
     * @param resourcelist
     * @return
     */
    private Bundle createBundle(List<IBaseResource> resourcelist) {
        Bundle bundle = new Bundle();
        for (IBaseResource resource : resourcelist) {
            Bundle.Entry entry = bundle.addEntry();
            entry.setResource((IResource) resource);
        }
        bundle.setTotal(resourcelist.size());

        return bundle;
    }

    /**
     * Convert a resource into a string entity
     *
     * @param encoding
     * @param anyResource
     * @return
     */
    private StringEntity getStringEntity(EncodingEnum encoding, IResource anyResource) {
        String encoded = encoding.newParser(mySubscriptionDao.getContext()).encodeResourceToString(anyResource);

        StringEntity entity;
        if (encoded.equalsIgnoreCase(EncodingEnum.JSON.name())) {
            entity = new StringEntity(encoded, ContentType.APPLICATION_JSON);
        } else {
            entity = new StringEntity(encoded, ContentType.APPLICATION_XML);
        }

        return entity;
    }

    @Override
    public void resourceDeleted(ActionRequestDetails theDetails, ResourceTable theResourceTable) {
    }

    /**
     * Remove subscription from cache
     *
     * @param subscriptionId
     */
    private void removeLocalSubscription(String subscriptionId) {
        Subscription localSubscription = getLocalSubscription(subscriptionId);
        if (localSubscription != null) {
            restHookSubscriptions.remove(localSubscription);
            logger.info("Subscription removed: " + subscriptionId);
        } else {
            logger.info("Subscription not found in local list. Subscription id: " + subscriptionId);
        }
    }

    /**
     * Get subscription from cache
     *
     * @param id
     * @return
     */
    private Subscription getLocalSubscription(String id) {
        if (id != null && !id.trim().isEmpty()) {
            int size = restHookSubscriptions.size();
            if (size > 0) {
                for (Subscription restHookSubscription : restHookSubscriptions) {
                    if (id.equals(restHookSubscription.getId().getIdPart())) {
                        return restHookSubscription;
                    }
                }
            }
        }

        return null;
    }

    public boolean isNotifyOnDelete() {
        return notifyOnDelete;
    }

    public void setNotifyOnDelete(boolean notifyOnDelete) {
        this.notifyOnDelete = notifyOnDelete;
    }
}

