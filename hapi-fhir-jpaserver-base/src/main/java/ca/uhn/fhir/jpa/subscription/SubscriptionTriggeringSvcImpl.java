package ca.uhn.fhir.jpa.subscription;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2019 University Health Network
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
import ca.uhn.fhir.jpa.dao.DaoConfig;
import ca.uhn.fhir.jpa.dao.DaoRegistry;
import ca.uhn.fhir.jpa.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.provider.SubscriptionTriggeringProvider;
import ca.uhn.fhir.jpa.search.ISearchCoordinatorSvc;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.subscription.module.ResourceModifiedMessage;
import ca.uhn.fhir.model.dstu2.valueset.ResourceTypeEnum;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.api.CacheControlDirective;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.UriParam;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import ca.uhn.fhir.util.ParametersUtil;
import ca.uhn.fhir.util.StopWatch;
import ca.uhn.fhir.util.UrlUtil;
import ca.uhn.fhir.util.ValidateUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.hl7.fhir.dstu2.model.IdType;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import static ca.uhn.fhir.jpa.provider.SubscriptionTriggeringProvider.RESOURCE_ID;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Service
public class SubscriptionTriggeringSvcImpl implements ISubscriptionTriggeringSvc {
	private static final Logger ourLog = LoggerFactory.getLogger(SubscriptionTriggeringProvider.class);

	private static final int DEFAULT_MAX_SUBMIT = 10000;

	@Autowired
	private FhirContext myFhirContext;
	@Autowired
	private DaoRegistry myDaoRegistry;
	@Autowired
	private DaoConfig myDaoConfig;
	@Autowired
	private ISearchCoordinatorSvc mySearchCoordinatorSvc;
	@Autowired
	private MatchUrlService myMatchUrlService;
	@Autowired
	private IResourceModifiedConsumer myResourceModifiedConsumer;

	private final List<SubscriptionTriggeringJobDetails> myActiveJobs = new ArrayList<>();
	private int myMaxSubmitPerPass = DEFAULT_MAX_SUBMIT;
	private ExecutorService myExecutorService;

	@Override
	public IBaseParameters triggerSubscription(List<UriParam> theResourceIds, List<StringParam> theSearchUrls, @IdParam IIdType theSubscriptionId) {
		if (myDaoConfig.getSupportedSubscriptionTypes().isEmpty()) {
			throw new PreconditionFailedException("Subscription processing not active on this server");
		}

		// Throw a 404 if the subscription doesn't exist
		if (theSubscriptionId != null) {
			IFhirResourceDao<?> subscriptionDao = myDaoRegistry.getSubscriptionDao();
			IIdType subscriptionId = theSubscriptionId;
			if (!subscriptionId.hasResourceType()) {
				subscriptionId = subscriptionId.withResourceType(ResourceTypeEnum.SUBSCRIPTION.getCode());
			}
			subscriptionDao.read(subscriptionId);
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
			if (!next.getValue().contains("?")) {
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

	@Scheduled(fixedDelay = DateUtils.MILLIS_PER_SECOND)
	public void runDeliveryPass() {

		synchronized (myActiveJobs) {
			if (myActiveJobs.isEmpty()) {
				return;
			}

			String activeJobIds = myActiveJobs.stream().map(SubscriptionTriggeringJobDetails::getJobId).collect(Collectors.joining(", "));
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
			RuntimeResourceDefinition resourceDef = UrlUtil.parseUrlResourceType(myFhirContext, nextSearchUrl);
			String queryPart = nextSearchUrl.substring(nextSearchUrl.indexOf('?'));
			String resourceType = resourceDef.getName();

			IFhirResourceDao<?> callingDao = myDaoRegistry.getResourceDao(resourceType);
			SearchParameterMap params = myMatchUrlService.translateMatchUrl(queryPart, resourceDef);

			ourLog.info("Triggering job[{}] is starting a search for {}", theJobDetails.getJobId(), nextSearchUrl);

			IBundleProvider search = mySearchCoordinatorSvc.registerSearch(callingDao, params, resourceType, new CacheControlDirective(), null);
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
			List<Long> resourceIds = mySearchCoordinatorSvc.getResources(theJobDetails.getCurrentSearchUuid(), fromIndex, toIndex, null);

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
		IFhirResourceDao dao = myDaoRegistry.getResourceDao(resourceId.getResourceType());
		IBaseResource resourceToTrigger = dao.read(resourceId);

		return submitResource(theSubscriptionId, resourceToTrigger);
	}

	private Future<Void> submitResource(String theSubscriptionId, IBaseResource theResourceToTrigger) {

		ourLog.info("Submitting resource {} to subscription {}", theResourceToTrigger.getIdElement().toUnqualifiedVersionless().getValue(), theSubscriptionId);

		ResourceModifiedMessage msg = new ResourceModifiedMessage(myFhirContext, theResourceToTrigger, ResourceModifiedMessage.OperationTypeEnum.UPDATE);
		msg.setSubscriptionId(new IdType(theSubscriptionId).toUnqualifiedVersionless().getValue());

		return myExecutorService.submit(() -> {
			for (int i = 0; ; i++) {
				try {
						myResourceModifiedConsumer.submitResourceModified(msg);
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

	@PostConstruct
	public void start() {
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
					// Restore interrupted state...
					Thread.currentThread().interrupt();
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

	private static class SubscriptionTriggeringJobDetails {

		private String myJobId;
		private String mySubscriptionId;
		private List<String> myRemainingResourceIds;
		private List<String> myRemainingSearchUrls;
		private String myCurrentSearchUuid;
		private Integer myCurrentSearchCount;
		private String myCurrentSearchResourceType;
		private int myCurrentSearchLastUploadedIndex;

		Integer getCurrentSearchCount() {
			return myCurrentSearchCount;
		}

		void setCurrentSearchCount(Integer theCurrentSearchCount) {
			myCurrentSearchCount = theCurrentSearchCount;
		}

		String getCurrentSearchResourceType() {
			return myCurrentSearchResourceType;
		}

		void setCurrentSearchResourceType(String theCurrentSearchResourceType) {
			myCurrentSearchResourceType = theCurrentSearchResourceType;
		}

		String getJobId() {
			return myJobId;
		}

		void setJobId(String theJobId) {
			myJobId = theJobId;
		}

		String getSubscriptionId() {
			return mySubscriptionId;
		}

		void setSubscriptionId(String theSubscriptionId) {
			mySubscriptionId = theSubscriptionId;
		}

		List<String> getRemainingResourceIds() {
			return myRemainingResourceIds;
		}

		void setRemainingResourceIds(List<String> theRemainingResourceIds) {
			myRemainingResourceIds = theRemainingResourceIds;
		}

		List<String> getRemainingSearchUrls() {
			return myRemainingSearchUrls;
		}

		void setRemainingSearchUrls(List<String> theRemainingSearchUrls) {
			myRemainingSearchUrls = theRemainingSearchUrls;
		}

		String getCurrentSearchUuid() {
			return myCurrentSearchUuid;
		}

		void setCurrentSearchUuid(String theCurrentSearchUuid) {
			myCurrentSearchUuid = theCurrentSearchUuid;
		}

		int getCurrentSearchLastUploadedIndex() {
			return myCurrentSearchLastUploadedIndex;
		}

		void setCurrentSearchLastUploadedIndex(int theCurrentSearchLastUploadedIndex) {
			myCurrentSearchLastUploadedIndex = theCurrentSearchLastUploadedIndex;
		}
	}

}
