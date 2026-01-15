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
package ca.uhn.fhir.rest.server.interceptor.partition;

import ca.uhn.fhir.batch2.api.IJobInstance;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.interceptor.model.ReadPartitionIdRequestDetails;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import ca.uhn.fhir.rest.server.tenant.ITenantIdentificationStrategy;
import jakarta.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * This interceptor uses the request tenant ID (as supplied to the server using
 * {@link ca.uhn.fhir.rest.server.RestfulServer#setTenantIdentificationStrategy(ITenantIdentificationStrategy)})
 * to indicate the partition ID. With this interceptor registered, The server treats the tenant name
 * supplied by the {@link ITenantIdentificationStrategy tenant identification strategy} as a partition name.
 * <p>
 * Partition names (aka tenant IDs) must be registered in advance using the partition management operations.
 * </p>
 *
 * @since 5.0.0
 */
@Interceptor
public class RequestTenantPartitionInterceptor {
	private static final String INITIATING_TENANT_KEY =
			RequestTenantPartitionInterceptor.class.getName() + "_INITIATING_TENANT";

	@Autowired
	private PartitionSettings myPartitionSettings;

	public void setPartitionSettings(PartitionSettings thePartitionSettings) {
		myPartitionSettings = thePartitionSettings;
	}

	@Hook(Pointcut.STORAGE_PARTITION_IDENTIFY_READ)
	public RequestPartitionId partitionIdentifyRead(
			RequestDetails theRequestDetails, ReadPartitionIdRequestDetails theReadDetails) {
		/*
		 * If we're configured to allow cross-partition references, and we're performing a search with references,
		 * we'll switch the search to being performed across all partitions.
		 *
		 * A potential future enhancement could be to look at the actual references and figure out which tenants they
		 * refer to. We don't currently allow the tenant to actually be stored in the reference currently, so this
		 * might or might not be a good idea. More thought is needed.
		 */
		if (myPartitionSettings.isAllowUnqualifiedCrossPartitionReference()) {
			if (theRequestDetails.getRestOperationType() == RestOperationTypeEnum.SEARCH_TYPE
					&& theReadDetails.getSearchParams() != null) {
				boolean haveReferences = theReadDetails.getSearchParams().entrySet().stream()
						.flatMap(t -> t.getValue().stream())
						.flatMap(Collection::stream)
						.filter(t -> t instanceof ReferenceParam)
						.map(t -> (ReferenceParam) t)
						.anyMatch(t -> t.getChain() == null);
				if (haveReferences) {
					return RequestPartitionId.allPartitions();
				}
			}
		}

		return extractPartitionIdFromRequest(theRequestDetails);
	}

	/**
	 * This method is invoked at the kickoff of a Batch2 job - It takes the request tenant from the
	 * RequestDetails object in the operation being used to kick off the job, and stashes it so that
	 * it's available later on to figure out the partitions during batch2 job exection steps.
	 */
	@Hook(Pointcut.STORAGE_PRESTORAGE_BATCH_JOB_CREATE)
	public void createBatchJob(RequestDetails theRequestDetails, JobInstance theJobInstance) {
		theJobInstance.addUserData(INITIATING_TENANT_KEY, theRequestDetails.getTenantId());
	}

	/**
	 * During batch2 step execution, restore the tenant ID to any new RequestDetails objects
	 */
	@Hook(Pointcut.STORAGE_BATCH_TASK_NEW_REQUEST_DETAILS)
	public void createBatchJob(RequestDetails theRequestDetails, IJobInstance theJobInstance) {
		String tenantId = (String) theJobInstance.getUserData().get(INITIATING_TENANT_KEY);
		if (tenantId != null) {
			theRequestDetails.setTenantId(tenantId);
		}
	}

	@Hook(Pointcut.STORAGE_PARTITION_IDENTIFY_CREATE)
	public RequestPartitionId partitionIdentifyCreate(RequestDetails theRequestDetails) {
		return extractPartitionIdFromRequest(theRequestDetails);
	}

	@Nonnull
	protected RequestPartitionId extractPartitionIdFromRequest(RequestDetails theRequestDetails) {
		String tenantId = theRequestDetails.getTenantId();
		if (tenantId == null) {
			tenantId = (String) theRequestDetails.getUserData().get(INITIATING_TENANT_KEY);
		}

		// We will use the tenant ID that came from the request as the partition name
		if (isBlank(tenantId)) {
			// this branch is no-op happen when "partitioning.tenant_identification_strategy" is set to URL_BASED
			if (theRequestDetails instanceof SystemRequestDetails requestDetails) {
				if (requestDetails.getRequestPartitionId() != null) {
					return requestDetails.getRequestPartitionId();
				}
				return RequestPartitionId.defaultPartition(myPartitionSettings);
			}
			throw new InternalErrorException(Msg.code(343) + "No partition ID has been specified");
		}

		// for REQUEST_TENANT partition selection mode, allPartitions is supported when URL includes _ALL as the tenant
		// else if no tenant is provided in the URL, DEFAULT will be used as per UrlBaseTenantIdentificationStrategy
		if (tenantId.equals(ProviderConstants.ALL_PARTITIONS_TENANT_NAME)) {
			return RequestPartitionId.allPartitions();
		}
		return RequestPartitionId.fromPartitionName(tenantId);
	}
}
