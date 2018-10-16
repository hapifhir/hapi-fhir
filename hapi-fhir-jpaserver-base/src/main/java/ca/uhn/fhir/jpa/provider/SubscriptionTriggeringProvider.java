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
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.jpa.dao.BaseHapiFhirDao;
import ca.uhn.fhir.jpa.dao.DaoRegistry;
import ca.uhn.fhir.jpa.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.dao.SearchParameterMap;
import ca.uhn.fhir.jpa.search.ISearchCoordinatorSvc;
import ca.uhn.fhir.jpa.search.warm.CacheWarmingSvcImpl;
import ca.uhn.fhir.jpa.subscription.BaseSubscriptionInterceptor;
import ca.uhn.fhir.jpa.subscription.ResourceModifiedMessage;
import ca.uhn.fhir.jpa.util.JpaConstants;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.api.CacheControlDirective;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.UriParam;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import ca.uhn.fhir.util.ParametersUtil;
import ca.uhn.fhir.util.StopWatch;
import ca.uhn.fhir.util.ValidateUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.time.DateUtils;
import org.hl7.fhir.instance.model.IdType;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class SubscriptionTriggeringProvider implements IResourceProvider, ApplicationContextAware {

	public static final String RESOURCE_ID = "resourceId";
	public static final int DEFAULT_MAX_SUBMIT = 10000;
	public static final String SEARCH_URL = "searchUrl";
	private static final Logger ourLog = LoggerFactory.getLogger(SubscriptionTriggeringProvider.class);
	private final List<SubscriptionTriggeringJobDetails> myActiveJobs = new ArrayList<>();
	@Autowired
	private FhirContext myFhirContext;
	@Autowired
	private DaoRegistry myDaoRegistry;
	private List<BaseSubscriptionInterceptor<?>> mySubscriptionInterceptorList;
	private int myMaxSubmitPerPass = DEFAULT_MAX_SUBMIT;
	@Autowired
	private ISearchCoordinatorSvc mySearchCoordinatorSvc;
	private ApplicationContext myAppCtx;

	/**
	 * Sets the maximum number of resources that will be submitted in a single pass
	 */
	public void setMaxSubmitPerPass(Integer theMaxSubmitPerPass) {
		Integer maxSubmitPerPass = theMaxSubmitPerPass;
		if (maxSubmitPerPass == null) {
			maxSubmitPerPass = DEFAULT_MAX_SUBMIT;
		}
		Validate.isTrue(maxSubmitPerPass > 0, "theMaxSubmitPerPass must be > 0");
		myMaxSubmitPerPass = maxSubmitPerPass;
	}

	@SuppressWarnings("unchecked")
	@PostConstruct
	public void start() {
		mySubscriptionInterceptorList = ObjectUtils.defaultIfNull(mySubscriptionInterceptorList, Collections.emptyList());
		mySubscriptionInterceptorList = new ArrayList<>();
		Collection values1 = myAppCtx.getBeansOfType(BaseSubscriptionInterceptor.class).values();
		Collection<BaseSubscriptionInterceptor<?>> values = (Collection<BaseSubscriptionInterceptor<?>>) values1;
		mySubscriptionInterceptorList.addAll(values);
	}

	@Operation(name = JpaConstants.OPERATION_TRIGGER_SUBSCRIPTION)
	public IBaseParameters triggerSubscription(
		@OperationParam(name = RESOURCE_ID, min = 0, max = OperationParam.MAX_UNLIMITED) List<UriParam> theResourceIds,
		@OperationParam(name = SEARCH_URL, min = 0, max = OperationParam.MAX_UNLIMITED) List<StringParam> theSearchUrls
	) {
		return doTriggerSubscription(theResourceIds, theSearchUrls, null);
	}

	@Operation(name = JpaConstants.OPERATION_TRIGGER_SUBSCRIPTION)
	public IBaseParameters triggerSubscription(
		@IdParam IIdType theSubscriptionId,
		@OperationParam(name = RESOURCE_ID, min = 0, max = OperationParam.MAX_UNLIMITED) List<UriParam> theResourceIds,
		@OperationParam(name = SEARCH_URL, min = 0, max = OperationParam.MAX_UNLIMITED) List<StringParam> theSearchUrls
	) {

		// Throw a 404 if the subscription doesn't exist
		IFhirResourceDao<?> subscriptionDao = myDaoRegistry.getResourceDao("Subscription");
		IIdType subscriptionId = theSubscriptionId;
		if (subscriptionId.hasResourceType() == false) {
			subscriptionId = subscriptionId.withResourceType("Subscription");
		}
		subscriptionDao.read(subscriptionId);

		return doTriggerSubscription(theResourceIds, theSearchUrls, subscriptionId);

	}

	private IBaseParameters doTriggerSubscription(@OperationParam(name = RESOURCE_ID, min = 0, max = OperationParam.MAX_UNLIMITED) List<UriParam> theResourceIds, @OperationParam(name = SEARCH_URL, min = 0, max = OperationParam.MAX_UNLIMITED) List<StringParam> theSearchUrls, @IdParam IIdType theSubscriptionId) {
		if (mySubscriptionInterceptorList.isEmpty()) {
			throw new PreconditionFailedException("Subscription processing not active on this server");
		}

		List<UriParam> resourceIds = ObjectUtils.defaultIfNull(theResourceIds, Collections.emptyList());
		List<StringParam> searchUrls = ObjectUtils.defaultIfNull(theSearchUrls, Collections.emptyList());

		// Make sure we have at least one resource ID or search URL
		if (resourceIds.size() == 0 && searchUrls.size() == 0) {
			throw new InvalidRequestException("No resource IDs or search URLs specified for triggering");
		}

		// Resource URLs must be compete
		for (UriParam next : resourceIds) {
			IdType resourceId = new IdType(next.getValue());
			ValidateUtil.isTrueOrThrowInvalidRequest(resourceId.hasResourceType(), RESOURCE_ID + " parameter must have resource type");
			ValidateUtil.isTrueOrThrowInvalidRequest(resourceId.hasIdPart(), RESOURCE_ID + " parameter must have resource ID part");
		}

		// Search URLs must be valid
		for (StringParam next : searchUrls) {
			if (next.getValue().contains("?") == false) {
				throw new InvalidRequestException("Search URL is not valid (must be in the form \"[resource type]?[optional params]\")");
			}
		}

		SubscriptionTriggeringJobDetails jobDetails = new SubscriptionTriggeringJobDetails();
		jobDetails.setJobId(UUID.randomUUID().toString());
		jobDetails.setRemainingResourceIds(resourceIds.stream().map(UriParam::getValue).collect(Collectors.toList()));
		jobDetails.setRemainingSearchUrls(searchUrls.stream().map(StringParam::getValue).collect(Collectors.toList()));
		if (theSubscriptionId != null) {
			jobDetails.setSubscriptionId(theSubscriptionId.toUnqualifiedVersionless().getValue());
		}

		// Submit job for processing
		synchronized (myActiveJobs) {
			myActiveJobs.add(jobDetails);
		}

		// Create a parameters response
		IBaseParameters retVal = ParametersUtil.newInstance(myFhirContext);
		IPrimitiveType<?> value = (IPrimitiveType<?>) myFhirContext.getElementDefinition("string").newInstance();
		value.setValueAsString("Subscription triggering job submitted as JOB ID: " + jobDetails.myJobId);
		ParametersUtil.addParameterToParameters(myFhirContext, retVal, "information", value);
		return retVal;
	}

	@Override
	public Class<? extends IBaseResource> getResourceType() {
		return myFhirContext.getResourceDefinition("Subscription").getImplementingClass();
	}

	@Scheduled(fixedDelay = DateUtils.MILLIS_PER_SECOND)
	public void runDeliveryPass() {

		synchronized (myActiveJobs) {
			if (myActiveJobs.isEmpty()) {
				return;
			}

			SubscriptionTriggeringJobDetails activeJob = myActiveJobs.get(0);

			runJob(activeJob);

			// If the job is complete, remove it from the queue
			if (activeJob.getRemainingResourceIds().isEmpty()) {
				if (activeJob.getRemainingSearchUrls().isEmpty()) {
					if (isBlank(activeJob.myCurrentSearchUuid)) {
						myActiveJobs.remove(0);
						String remainingJobsMsg = "";
						if (myActiveJobs.size() > 0) {
							remainingJobsMsg = "(" + myActiveJobs.size() + " jobs remaining)";
						}
						ourLog.info("Subscription triggering job {} is complete{}", activeJob.getJobId(), remainingJobsMsg);
					}
				}
			}

		}

	}

	private void runJob(SubscriptionTriggeringJobDetails theJobDetails) {
		StopWatch sw = new StopWatch();

		// Submit individual resources
		int totalSubmitted = 0;
		while (theJobDetails.getRemainingResourceIds().size() > 0 && totalSubmitted < myMaxSubmitPerPass) {
			totalSubmitted++;
			String nextResourceId = theJobDetails.getRemainingResourceIds().remove(0);
			submitResource(theJobDetails.getSubscriptionId(), nextResourceId);
		}

		// If we don't have an active search started, and one needs to be.. start it
		if (isBlank(theJobDetails.getCurrentSearchUuid()) && theJobDetails.getRemainingSearchUrls().size() > 0 && totalSubmitted < myMaxSubmitPerPass) {
			String nextSearchUrl = theJobDetails.getRemainingSearchUrls().remove(0);
			RuntimeResourceDefinition resourceDef = CacheWarmingSvcImpl.parseUrlResourceType(myFhirContext, nextSearchUrl);
			String queryPart = nextSearchUrl.substring(nextSearchUrl.indexOf('?'));
			String resourceType = resourceDef.getName();

			IFhirResourceDao<?> callingDao = myDaoRegistry.getResourceDao(resourceType);
			SearchParameterMap params = BaseHapiFhirDao.translateMatchUrl(callingDao, myFhirContext, queryPart, resourceDef);

			ourLog.info("Triggering job[{}] is starting a search for {}", theJobDetails.getJobId(), nextSearchUrl);

			IBundleProvider search = mySearchCoordinatorSvc.registerSearch(callingDao, params, resourceType, new CacheControlDirective());
			theJobDetails.setCurrentSearchUuid(search.getUuid());
			theJobDetails.setCurrentSearchResourceType(resourceType);
			theJobDetails.setCurrentSearchCount(params.getCount());
		}

		// If we have an active search going, submit resources from it
		if (isNotBlank(theJobDetails.getCurrentSearchUuid()) && totalSubmitted < myMaxSubmitPerPass) {
			int fromIndex = 0;
			if (theJobDetails.getCurrentSearchLastUploadedIndex() != null) {
				fromIndex = theJobDetails.getCurrentSearchLastUploadedIndex() + 1;
			}

			IFhirResourceDao<?> resourceDao = myDaoRegistry.getResourceDao(theJobDetails.getCurrentSearchResourceType());

			int maxQuerySize = myMaxSubmitPerPass - totalSubmitted;
			int toIndex = fromIndex + maxQuerySize;
			if (theJobDetails.getCurrentSearchCount() != null) {
				toIndex = Math.min(toIndex, theJobDetails.getCurrentSearchCount());
			}
			ourLog.info("Triggering job[{}] submitting up to {} resources for search {}", theJobDetails.getJobId(), maxQuerySize, theJobDetails.getCurrentSearchUuid());
			List<Long> resourceIds = mySearchCoordinatorSvc.getResources(theJobDetails.getCurrentSearchUuid(), fromIndex, toIndex);
			for (Long next : resourceIds) {
				IBaseResource nextResource = resourceDao.readByPid(next);
				submitResource(theJobDetails.getSubscriptionId(), nextResource);
				totalSubmitted++;
				theJobDetails.setCurrentSearchLastUploadedIndex(toIndex - 1);
			}

			int expectedCount = toIndex - fromIndex;
			if (resourceIds.size() < expectedCount || (theJobDetails.getCurrentSearchCount() != null && toIndex >= theJobDetails.getCurrentSearchCount())) {
				ourLog.info("Triggering job[{}] search {} has completed", theJobDetails.getJobId(), theJobDetails.getCurrentSearchUuid());
				theJobDetails.setCurrentSearchResourceType(null);
				theJobDetails.setCurrentSearchUuid(null);
				theJobDetails.setCurrentSearchLastUploadedIndex(null);
				theJobDetails.setCurrentSearchCount(null);
			}
		}

		ourLog.info("Subscription trigger job[{}] triggered {} resources in {} ({} res / second)", theJobDetails.getJobId(), totalSubmitted, sw.getMillis(), sw.getThroughput(totalSubmitted, TimeUnit.SECONDS));
	}

	private void submitResource(String theSubscriptionId, String theResourceIdToTrigger) {
		org.hl7.fhir.r4.model.IdType resourceId = new org.hl7.fhir.r4.model.IdType(theResourceIdToTrigger);
		IFhirResourceDao<? extends IBaseResource> dao = myDaoRegistry.getResourceDao(resourceId.getResourceType());
		IBaseResource resourceToTrigger = dao.read(resourceId);

		submitResource(theSubscriptionId, resourceToTrigger);
	}

	private void submitResource(String theSubscriptionId, IBaseResource theResourceToTrigger) {

		ourLog.info("Submitting resource {} to subscription {}", theResourceToTrigger.getIdElement().toUnqualifiedVersionless().getValue(), theSubscriptionId);

		ResourceModifiedMessage msg = new ResourceModifiedMessage();
		msg.setId(theResourceToTrigger.getIdElement());
		msg.setOperationType(ResourceModifiedMessage.OperationTypeEnum.UPDATE);
		msg.setSubscriptionId(new IdType(theSubscriptionId).toUnqualifiedVersionless().getValue());
		msg.setNewPayload(myFhirContext, theResourceToTrigger);

		for (BaseSubscriptionInterceptor<?> next : mySubscriptionInterceptorList) {
			next.submitResourceModified(msg);
		}
	}

	public void cancelAll() {
		synchronized (myActiveJobs) {
			myActiveJobs.clear();
		}
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		myAppCtx = applicationContext;
	}

	private static class SubscriptionTriggeringJobDetails {

		private String myJobId;
		private String mySubscriptionId;
		private List<String> myRemainingResourceIds;
		private List<String> myRemainingSearchUrls;
		private String myCurrentSearchUuid;
		private Integer myCurrentSearchCount;
		private String myCurrentSearchResourceType;
		private Integer myCurrentSearchLastUploadedIndex;

		public Integer getCurrentSearchCount() {
			return myCurrentSearchCount;
		}

		public void setCurrentSearchCount(Integer theCurrentSearchCount) {
			myCurrentSearchCount = theCurrentSearchCount;
		}

		public String getCurrentSearchResourceType() {
			return myCurrentSearchResourceType;
		}

		public void setCurrentSearchResourceType(String theCurrentSearchResourceType) {
			myCurrentSearchResourceType = theCurrentSearchResourceType;
		}

		public String getJobId() {
			return myJobId;
		}

		public void setJobId(String theJobId) {
			myJobId = theJobId;
		}

		public String getSubscriptionId() {
			return mySubscriptionId;
		}

		public void setSubscriptionId(String theSubscriptionId) {
			mySubscriptionId = theSubscriptionId;
		}

		public List<String> getRemainingResourceIds() {
			return myRemainingResourceIds;
		}

		public void setRemainingResourceIds(List<String> theRemainingResourceIds) {
			myRemainingResourceIds = theRemainingResourceIds;
		}

		public List<String> getRemainingSearchUrls() {
			return myRemainingSearchUrls;
		}

		public void setRemainingSearchUrls(List<String> theRemainingSearchUrls) {
			myRemainingSearchUrls = theRemainingSearchUrls;
		}

		public String getCurrentSearchUuid() {
			return myCurrentSearchUuid;
		}

		public void setCurrentSearchUuid(String theCurrentSearchUuid) {
			myCurrentSearchUuid = theCurrentSearchUuid;
		}

		public Integer getCurrentSearchLastUploadedIndex() {
			return myCurrentSearchLastUploadedIndex;
		}

		public void setCurrentSearchLastUploadedIndex(Integer theCurrentSearchLastUploadedIndex) {
			myCurrentSearchLastUploadedIndex = theCurrentSearchLastUploadedIndex;
		}
	}

}
