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
import ca.uhn.fhir.jpa.service.MatchUrlService;
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
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import ca.uhn.fhir.util.ParametersUtil;
import ca.uhn.fhir.util.StopWatch;
import ca.uhn.fhir.util.ValidateUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.lang3.tuple.Pair;
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
import java.util.concurrent.*;
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
	@Autowired
	private MatchUrlService myMatchUrlService;
	private ApplicationContext myAppCtx;
	private ExecutorService myExecutorService;

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


		LinkedBlockingQueue<Runnable> executorQueue = new LinkedBlockingQueue<>(1000);
		BasicThreadFactory threadFactory = new BasicThreadFactory.Builder()
			.namingPattern("SubscriptionTriggering-%d")
			.daemon(false)
			.priority(Thread.NORM_PRIORITY)
			.build();
		RejectedExecutionHandler rejectedExecutionHandler = new RejectedExecutionHandler() {
			@Override
			public void rejectedExecution(Runnable theRunnable, ThreadPoolExecutor theExecutor) {
				ourLog.info("Note: Subscription triggering queue is full ({} elements), waiting for a slot to become available!", executorQueue.size());
				StopWatch sw = new StopWatch();
				try {
					executorQueue.put(theRunnable);
				} catch (InterruptedException theE) {
					throw new RejectedExecutionException("Task " + theRunnable.toString() +
						" rejected from " + theE.toString());
				}
				ourLog.info("Slot become available after {}ms", sw.getMillis());
			}
		};
		myExecutorService = new ThreadPoolExecutor(
			0,
			10,
			0L,
			TimeUnit.MILLISECONDS,
			executorQueue,
			threadFactory,
			rejectedExecutionHandler);

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
		ourLog.info("Subscription triggering requested for {} resource and {} search - Gave job ID: {}", resourceIds.size(), searchUrls.size(), jobDetails.getJobId());

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

			String activeJobIds = myActiveJobs.stream().map(t->t.getJobId()).collect(Collectors.joining(", "));
			ourLog.info("Starting pass: currently have {} active job IDs: {}", myActiveJobs.size(), activeJobIds);

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
		ourLog.info("Starting pass of subscription triggering job {}", theJobDetails.getJobId());

		// Submit individual resources
		int totalSubmitted = 0;
		List<Pair<String, Future<Void>>> futures = new ArrayList<>();
		while (theJobDetails.getRemainingResourceIds().size() > 0 && totalSubmitted < myMaxSubmitPerPass) {
			totalSubmitted++;
			String nextResourceId = theJobDetails.getRemainingResourceIds().remove(0);
			Future<Void> future = submitResource(theJobDetails.getSubscriptionId(), nextResourceId);
			futures.add(Pair.of(nextResourceId, future));
		}

		// Make sure these all succeeded in submitting
		if (validateFuturesAndReturnTrueIfWeShouldAbort(futures)) {
			return;
		}

		// If we don't have an active search started, and one needs to be.. start it
		if (isBlank(theJobDetails.getCurrentSearchUuid()) && theJobDetails.getRemainingSearchUrls().size() > 0 && totalSubmitted < myMaxSubmitPerPass) {
			String nextSearchUrl = theJobDetails.getRemainingSearchUrls().remove(0);
			RuntimeResourceDefinition resourceDef = CacheWarmingSvcImpl.parseUrlResourceType(myFhirContext, nextSearchUrl);
			String queryPart = nextSearchUrl.substring(nextSearchUrl.indexOf('?'));
			String resourceType = resourceDef.getName();

			IFhirResourceDao<?> callingDao = myDaoRegistry.getResourceDao(resourceType);
			SearchParameterMap params = myMatchUrlService.translateMatchUrl(callingDao, myFhirContext, queryPart, resourceDef);

			ourLog.info("Triggering job[{}] is starting a search for {}", theJobDetails.getJobId(), nextSearchUrl);

			IBundleProvider search = mySearchCoordinatorSvc.registerSearch(callingDao, params, resourceType, new CacheControlDirective());
			theJobDetails.setCurrentSearchUuid(search.getUuid());
			theJobDetails.setCurrentSearchResourceType(resourceType);
			theJobDetails.setCurrentSearchCount(params.getCount());
			theJobDetails.setCurrentSearchLastUploadedIndex(-1);
		}

		// If we have an active search going, submit resources from it
		if (isNotBlank(theJobDetails.getCurrentSearchUuid()) && totalSubmitted < myMaxSubmitPerPass) {
			int fromIndex = theJobDetails.getCurrentSearchLastUploadedIndex() + 1;

			IFhirResourceDao<?> resourceDao = myDaoRegistry.getResourceDao(theJobDetails.getCurrentSearchResourceType());

			int maxQuerySize = myMaxSubmitPerPass - totalSubmitted;
			int toIndex = fromIndex + maxQuerySize;
			if (theJobDetails.getCurrentSearchCount() != null) {
				toIndex = Math.min(toIndex, theJobDetails.getCurrentSearchCount());
			}
			ourLog.info("Triggering job[{}] search {} requesting resources {} - {}", theJobDetails.getJobId(), theJobDetails.getCurrentSearchUuid(), fromIndex, toIndex);
			List<Long> resourceIds = mySearchCoordinatorSvc.getResources(theJobDetails.getCurrentSearchUuid(), fromIndex, toIndex);

			ourLog.info("Triggering job[{}] delivering {} resources", theJobDetails.getJobId(), resourceIds.size());
			int highestIndexSubmitted = theJobDetails.getCurrentSearchLastUploadedIndex();

			for (Long next : resourceIds) {
				IBaseResource nextResource = resourceDao.readByPid(next);
				Future<Void> future = submitResource(theJobDetails.getSubscriptionId(), nextResource);
				futures.add(Pair.of(nextResource.getIdElement().getIdPart(), future));
				totalSubmitted++;
				highestIndexSubmitted++;
			}

			if (validateFuturesAndReturnTrueIfWeShouldAbort(futures)) {
				return;
			}

			theJobDetails.setCurrentSearchLastUploadedIndex(highestIndexSubmitted);

			if (resourceIds.size() == 0 || (theJobDetails.getCurrentSearchCount() != null && toIndex >= theJobDetails.getCurrentSearchCount())) {
				ourLog.info("Triggering job[{}] search {} has completed ", theJobDetails.getJobId(), theJobDetails.getCurrentSearchUuid());
				theJobDetails.setCurrentSearchResourceType(null);
				theJobDetails.setCurrentSearchUuid(null);
				theJobDetails.setCurrentSearchLastUploadedIndex(-1);
				theJobDetails.setCurrentSearchCount(null);
			}
		}

		ourLog.info("Subscription trigger job[{}] triggered {} resources in {}ms ({} res / second)", theJobDetails.getJobId(), totalSubmitted, sw.getMillis(), sw.getThroughput(totalSubmitted, TimeUnit.SECONDS));
	}

	private boolean validateFuturesAndReturnTrueIfWeShouldAbort(List<Pair<String, Future<Void>>> theIdToFutures) {

		for (Pair<String, Future<Void>> next : theIdToFutures) {
			String nextDeliveredId = next.getKey();
			try {
				Future<Void> nextFuture = next.getValue();
				nextFuture.get();
				ourLog.info("Finished redelivering {}", nextDeliveredId);
			} catch (Exception e) {
				ourLog.error("Failure triggering resource " + nextDeliveredId, e);
				return true;
			}
		}

		// Clear the list since it will potentially get reused
		theIdToFutures.clear();
		return false;
	}

	private Future<Void> submitResource(String theSubscriptionId, String theResourceIdToTrigger) {
		org.hl7.fhir.r4.model.IdType resourceId = new org.hl7.fhir.r4.model.IdType(theResourceIdToTrigger);
		IFhirResourceDao<? extends IBaseResource> dao = myDaoRegistry.getResourceDao(resourceId.getResourceType());
		IBaseResource resourceToTrigger = dao.read(resourceId);

		return submitResource(theSubscriptionId, resourceToTrigger);
	}

	private Future<Void> submitResource(String theSubscriptionId, IBaseResource theResourceToTrigger) {

		ourLog.info("Submitting resource {} to subscription {}", theResourceToTrigger.getIdElement().toUnqualifiedVersionless().getValue(), theSubscriptionId);

		ResourceModifiedMessage msg = new ResourceModifiedMessage();
		msg.setId(theResourceToTrigger.getIdElement());
		msg.setOperationType(ResourceModifiedMessage.OperationTypeEnum.UPDATE);
		msg.setSubscriptionId(new IdType(theSubscriptionId).toUnqualifiedVersionless().getValue());
		msg.setNewPayload(myFhirContext, theResourceToTrigger);

		return myExecutorService.submit(()->{
			for (int i = 0; ; i++) {
				try {
					for (BaseSubscriptionInterceptor<?> next : mySubscriptionInterceptorList) {
						next.submitResourceModified(msg);
					}
					break;
				} catch (Exception e) {
					if (i >= 3) {
						throw new InternalErrorException(e);
					}

					ourLog.warn("Exception while retriggering subscriptions (going to sleep and retry): {}", e.toString());
					Thread.sleep(1000);
				}
			}

			return null;
		});

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
		private int myCurrentSearchLastUploadedIndex;

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

		public int getCurrentSearchLastUploadedIndex() {
			return myCurrentSearchLastUploadedIndex;
		}

		public void setCurrentSearchLastUploadedIndex(int theCurrentSearchLastUploadedIndex) {
			myCurrentSearchLastUploadedIndex = theCurrentSearchLastUploadedIndex;
		}
	}

}
