package ca.uhn.fhir.jpa.term;

/*-
 * #%L
 * HAPI FHIR JPA Server
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

import ca.uhn.fhir.jpa.dao.data.ITermConceptDao;
import ca.uhn.fhir.jpa.dao.data.ITermConceptParentChildLinkDao;
import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.model.sched.HapiJob;
import ca.uhn.fhir.jpa.model.sched.ISchedulerService;
import ca.uhn.fhir.jpa.model.sched.ScheduledJobDefinition;
import ca.uhn.fhir.jpa.term.api.ITermCodeSystemStorageSvc;
import ca.uhn.fhir.jpa.term.api.ITermDeferredStorageSvc;
import ca.uhn.fhir.jpa.term.api.ITermReindexingSvc;
import ca.uhn.fhir.util.StopWatch;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ArrayListMultimap;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.time.DateUtils;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class TermReindexingSvcImpl implements ITermReindexingSvc {
	private static final Logger ourLog = LoggerFactory.getLogger(TermReindexingSvcImpl.class);
	private static boolean ourForceSaveDeferredAlwaysForUnitTest;
	@Autowired
	protected ITermConceptDao myConceptDao;
	private ArrayListMultimap<Long, Long> myChildToParentPidCache;
	@Autowired
	private PlatformTransactionManager myTransactionMgr;
	@Autowired
	private ITermConceptParentChildLinkDao myConceptParentChildLinkDao;
	@Autowired
	private ITermDeferredStorageSvc myDeferredStorageSvc;
	@Autowired
	private ISchedulerService mySchedulerService;
	@Autowired
	private TermConceptDaoSvc myTermConceptDaoSvc;

	@Override
	public void processReindexing() {
		if (myDeferredStorageSvc.isStorageQueueEmpty() == false && !ourForceSaveDeferredAlwaysForUnitTest) {
			return;
		}

		TransactionTemplate tt = new TransactionTemplate(myTransactionMgr);
		tt.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		tt.execute(new TransactionCallbackWithoutResult() {
			private void createParentsString(StringBuilder theParentsBuilder, Long theConceptPid) {
				Validate.notNull(theConceptPid, "theConceptPid must not be null");
				List<Long> parents = myChildToParentPidCache.get(theConceptPid);
				if (parents.contains(-1L)) {
					return;
				} else if (parents.isEmpty()) {
					Collection<Long> parentLinks = myConceptParentChildLinkDao.findAllWithChild(theConceptPid);
					if (parentLinks.isEmpty()) {
						myChildToParentPidCache.put(theConceptPid, -1L);
						ourLog.info("Found {} parent concepts of concept {} (cache has {})", 0, theConceptPid, myChildToParentPidCache.size());
						return;
					} else {
						for (Long next : parentLinks) {
							myChildToParentPidCache.put(theConceptPid, next);
						}
						int parentCount = myChildToParentPidCache.get(theConceptPid).size();
						ourLog.info("Found {} parent concepts of concept {} (cache has {})", parentCount, theConceptPid, myChildToParentPidCache.size());
					}
				}

				for (Long nextParent : parents) {
					if (theParentsBuilder.length() > 0) {
						theParentsBuilder.append(' ');
					}
					theParentsBuilder.append(nextParent);
					createParentsString(theParentsBuilder, nextParent);
				}

			}


			@Override
			protected void doInTransactionWithoutResult(TransactionStatus theArg0) {
				int maxResult = 1000;
				Page<TermConcept> concepts = myConceptDao.findResourcesRequiringReindexing(PageRequest.of(0, maxResult));
				if (!concepts.hasContent()) {
					if (myChildToParentPidCache != null) {
						ourLog.info("Clearing parent concept cache");
						myChildToParentPidCache = null;
					}
					return;
				}

				if (myChildToParentPidCache == null) {
					myChildToParentPidCache = ArrayListMultimap.create();
				}

				ourLog.info("Indexing {} / {} concepts", concepts.getContent().size(), concepts.getTotalElements());

				int count = 0;
				StopWatch stopwatch = new StopWatch();

				for (TermConcept nextConcept : concepts) {

					if (isBlank(nextConcept.getParentPidsAsString())) {
						StringBuilder parentsBuilder = new StringBuilder();
						createParentsString(parentsBuilder, nextConcept.getId());
						nextConcept.setParentPids(parentsBuilder.toString());
					}

					myTermConceptDaoSvc.saveConcept(nextConcept);
					count++;
				}

				ourLog.info("Indexed {} / {} concepts in {}ms - Avg {}ms / resource", count, concepts.getContent().size(), stopwatch.getMillis(), stopwatch.getMillisPerOperation(count));
			}
		});

	}

	@PostConstruct
	public void scheduleJob() {
		// TODO KHS what does this mean?
		// Register scheduled job to save deferred concepts
		// In the future it would be great to make this a cluster-aware task somehow
		ScheduledJobDefinition jobDefinition = new ScheduledJobDefinition();
		jobDefinition.setId(this.getClass().getName());
		jobDefinition.setJobClass(Job.class);
		mySchedulerService.scheduleLocalJob(DateUtils.MILLIS_PER_MINUTE, jobDefinition);
	}

	public static class Job implements HapiJob {
		@Autowired
		private ITermReindexingSvc myTermReindexingSvc;

		@Override
		public void execute(JobExecutionContext theContext) {
			myTermReindexingSvc.processReindexing();
		}
	}

	/**
	 * This method is present only for unit tests, do not call from client code
	 */
	@VisibleForTesting
	public static void setForceSaveDeferredAlwaysForUnitTest(boolean theForceSaveDeferredAlwaysForUnitTest) {
		ourForceSaveDeferredAlwaysForUnitTest = theForceSaveDeferredAlwaysForUnitTest;
	}


}
