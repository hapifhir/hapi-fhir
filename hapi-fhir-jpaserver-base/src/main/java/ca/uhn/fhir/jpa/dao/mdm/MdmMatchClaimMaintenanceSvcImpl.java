/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.dao.mdm;

import ca.uhn.fhir.jpa.model.sched.HapiJob;
import ca.uhn.fhir.jpa.model.sched.IHasScheduledJobs;
import ca.uhn.fhir.jpa.model.sched.ISchedulerService;
import ca.uhn.fhir.jpa.model.sched.ScheduledJobDefinition;
import ca.uhn.fhir.mdm.api.IMdmSettings;
import org.apache.commons.lang3.time.DateUtils;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * Registers a clustered job that purges MDM match claims older than
 * {@link IMdmSettings#getMatchClaimRetentionMillis()}, as {@link ca.uhn.fhir.jpa.search.SearchUrlJobMaintenanceSvcImpl}
 * does for search URLs.
 */
// Created by claude-opus-5-5
public class MdmMatchClaimMaintenanceSvcImpl implements IHasScheduledJobs {

	private final MdmMatchClaimSvcJpaImpl myMdmMatchClaimSvc;
	private final ObjectProvider<IMdmSettings> myMdmSettings;

	/**
	 * @param theMdmSettings the MDM settings, which are absent when MDM isn't configured; the default
	 *                       retention then applies
	 */
	public MdmMatchClaimMaintenanceSvcImpl(
			MdmMatchClaimSvcJpaImpl theMdmMatchClaimSvc, ObjectProvider<IMdmSettings> theMdmSettings) {
		myMdmMatchClaimSvc = theMdmMatchClaimSvc;
		myMdmSettings = theMdmSettings;
	}

	public void removeStaleEntries() {
		myMdmMatchClaimSvc.deleteEntriesOlderThan(calculateCutoffDate());
	}

	@Override
	public void scheduleJobs(ISchedulerService theSchedulerService) {
		ScheduledJobDefinition jobDetail = new ScheduledJobDefinition();
		jobDetail.setId(MdmMatchClaimMaintenanceJob.class.getName());
		jobDetail.setJobClass(MdmMatchClaimMaintenanceJob.class);
		theSchedulerService.scheduleClusteredJob(DateUtils.MILLIS_PER_MINUTE, jobDetail);
	}

	Date calculateCutoffDate() {
		IMdmSettings settings = myMdmSettings.getIfAvailable();
		long retention = settings != null
				? settings.getMatchClaimRetentionMillis()
				: IMdmSettings.DEFAULT_MATCH_CLAIM_RETENTION_MILLIS;
		return new Date(System.currentTimeMillis() - retention);
	}

	public static class MdmMatchClaimMaintenanceJob implements HapiJob {

		@Autowired
		private MdmMatchClaimMaintenanceSvcImpl myMaintenanceSvc;

		@Override
		public void execute(JobExecutionContext theJobExecutionContext) {
			myMaintenanceSvc.removeStaleEntries();
		}
	}
}
