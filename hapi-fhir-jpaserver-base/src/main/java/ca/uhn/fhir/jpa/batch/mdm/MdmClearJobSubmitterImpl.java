package ca.uhn.fhir.jpa.batch.mdm;

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

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.batch.api.IBatchJobSubmitter;
import ca.uhn.fhir.jpa.batch.config.BatchConstants;
import ca.uhn.fhir.jpa.batch.job.PartitionedUrlValidator;
import ca.uhn.fhir.jpa.batch.job.model.RequestListJson;
import ca.uhn.fhir.jpa.batch.mdm.job.ReverseCronologicalBatchMdmLinkPidReader;
import ca.uhn.fhir.mdm.api.IMdmClearJobSubmitter;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.ForbiddenOperationException;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.rest.server.util.CompositeInterceptorBroadcaster;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.transaction.Transactional;
import java.util.List;

public class MdmClearJobSubmitterImpl implements IMdmClearJobSubmitter {
	@Autowired
	DaoConfig myDaoConfig;
	@Autowired
	PartitionedUrlValidator myPartitionedUrlValidator;
	@Autowired
	IInterceptorBroadcaster myInterceptorBroadcaster;
	@Autowired
	private IBatchJobSubmitter myBatchJobSubmitter;
	@Autowired
	@Qualifier(BatchConstants.MDM_CLEAR_JOB_NAME)
	private Job myMdmClearJob;

	@Override
	@Transactional(Transactional.TxType.NEVER)
	public JobExecution submitJob(Integer theBatchSize, List<String> theUrls, RequestDetails theRequest) throws JobParametersInvalidException {
		if (theBatchSize == null) {
			theBatchSize = myDaoConfig.getExpungeBatchSize();
		}
		if (!myDaoConfig.canDeleteExpunge()) {
			throw new ForbiddenOperationException(Msg.code(1278) + "Delete Expunge not allowed:  " + myDaoConfig.cannotDeleteExpungeReason());
		}

		RequestListJson requestListJson = myPartitionedUrlValidator.buildRequestListJson(theRequest, theUrls);

		for (String url : theUrls) {
			HookParams params = new HookParams()
				.add(RequestDetails.class, theRequest)
				.addIfMatchesType(ServletRequestDetails.class, theRequest)
				.add(String.class, url);
			CompositeInterceptorBroadcaster.doCallHooks(myInterceptorBroadcaster, theRequest, Pointcut.STORAGE_PRE_DELETE_EXPUNGE, params);
		}

		JobParameters jobParameters = ReverseCronologicalBatchMdmLinkPidReader.buildJobParameters(ProviderConstants.OPERATION_MDM_CLEAR, theBatchSize, requestListJson);
		return myBatchJobSubmitter.runJob(myMdmClearJob, jobParameters);
	}
}
