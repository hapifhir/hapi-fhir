package ca.uhn.fhir.jpa.search.cache;

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

import ca.uhn.fhir.jpa.entity.Search;
import ca.uhn.fhir.jpa.model.sched.ISchedulerService;
import ca.uhn.fhir.jpa.model.sched.ScheduledJobDefinition;
import org.apache.commons.lang3.time.DateUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class BaseSearchCacheSvcImpl implements ISearchCacheSvc {

	@Autowired
	private PlatformTransactionManager myTxManager;
	@Autowired
	private ISchedulerService mySchedulerService;

	private ConcurrentHashMap<Long, Date> myUnsyncedLastUpdated = new ConcurrentHashMap<>();

	@Override
	public void updateSearchLastReturned(Search theSearch, Date theDate) {
		myUnsyncedLastUpdated.put(theSearch.getId(), theDate);
	}

	@PostConstruct
	public void registerScheduledJob() {
		ScheduledJobDefinition jobDetail = new ScheduledJobDefinition();
		jobDetail.setId(BaseSearchCacheSvcImpl.class.getName());
		jobDetail.setJobClass(BaseSearchCacheSvcImpl.SubmitJob.class);
		mySchedulerService.scheduleFixedDelay(10 * DateUtils.MILLIS_PER_SECOND, false, jobDetail);
	}

	@Override
	public void flushLastUpdated() {
		TransactionTemplate txTemplate = new TransactionTemplate(myTxManager);
		txTemplate.execute(t -> {
			for (Iterator<Map.Entry<Long, Date>> iter = myUnsyncedLastUpdated.entrySet().iterator(); iter.hasNext(); ) {
				Map.Entry<Long, Date> next = iter.next();
				flushLastUpdated(next.getKey(), next.getValue());
				iter.remove();
			}
			return null;
		});
	}

	protected abstract void flushLastUpdated(Long theSearchId, Date theLastUpdated);

	public static class SubmitJob implements Job {
		@Autowired
		private ISearchCacheSvc myTarget;

		@Override
		public void execute(JobExecutionContext theContext) {
			myTarget.flushLastUpdated();
		}
	}


}
