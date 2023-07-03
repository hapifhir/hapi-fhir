/*-
 * #%L
 * HAPI FHIR JPA Server - Master Data Management
 * %%
 * Copyright (C) 2014 - 2023 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.mdm.svc;

import ca.uhn.fhir.batch2.api.IJobCoordinator;
import ca.uhn.fhir.batch2.model.JobInstanceStartRequest;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.model.ReadPartitionIdRequestDetails;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.batch.models.Batch2JobStartResponse;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.mdm.api.IGoldenResourceMergerSvc;
import ca.uhn.fhir.mdm.api.IMdmControllerSvc;
import ca.uhn.fhir.mdm.api.IMdmLinkCreateSvc;
import ca.uhn.fhir.mdm.api.IMdmLinkQuerySvc;
import ca.uhn.fhir.mdm.api.IMdmLinkUpdaterSvc;
import ca.uhn.fhir.mdm.api.MdmHistorySearchParameters;
import ca.uhn.fhir.mdm.api.MdmLinkJson;
import ca.uhn.fhir.mdm.api.MdmLinkWithRevisionJson;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.mdm.api.MdmQuerySearchParameters;
import ca.uhn.fhir.mdm.api.paging.MdmPageRequest;
import ca.uhn.fhir.mdm.batch2.clear.MdmClearAppCtx;
import ca.uhn.fhir.mdm.batch2.clear.MdmClearJobParameters;
import ca.uhn.fhir.mdm.batch2.submit.MdmSubmitAppCtx;
import ca.uhn.fhir.mdm.batch2.submit.MdmSubmitJobParameters;
import ca.uhn.fhir.mdm.model.MdmTransactionContext;
import ca.uhn.fhir.mdm.provider.MdmControllerHelper;
import ca.uhn.fhir.mdm.provider.MdmControllerUtil;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.util.ParametersUtil;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This class acts as a layer between MdmProviders and MDM services to support a REST API that's not a FHIR Operation API.
 */
@Service
public class MdmControllerSvcImpl implements IMdmControllerSvc {

	@Autowired
	FhirContext myFhirContext;
	@Autowired
	MdmControllerHelper myMdmControllerHelper;
	@Autowired
	IGoldenResourceMergerSvc myGoldenResourceMergerSvc;
	@Autowired
	IMdmLinkQuerySvc myMdmLinkQuerySvc;
	@Autowired
	IMdmLinkUpdaterSvc myIMdmLinkUpdaterSvc;
	@Autowired
	IMdmLinkCreateSvc myIMdmLinkCreateSvc;
	@Autowired
	IRequestPartitionHelperSvc myRequestPartitionHelperSvc;
	@Autowired
	IJobCoordinator myJobCoordinator;

	public MdmControllerSvcImpl() {
	}

	@Override
	public IAnyResource mergeGoldenResources(String theFromGoldenResourceId, String theToGoldenResourceId, IAnyResource theManuallyMergedGoldenResource, MdmTransactionContext theMdmTransactionContext) {
		IAnyResource fromGoldenResource = myMdmControllerHelper.getLatestGoldenResourceFromIdOrThrowException(ProviderConstants.MDM_MERGE_GR_FROM_GOLDEN_RESOURCE_ID, theFromGoldenResourceId);
		IAnyResource toGoldenResource = myMdmControllerHelper.getLatestGoldenResourceFromIdOrThrowException(ProviderConstants.MDM_MERGE_GR_TO_GOLDEN_RESOURCE_ID, theToGoldenResourceId);
		myMdmControllerHelper.validateMergeResources(fromGoldenResource, toGoldenResource);
		myMdmControllerHelper.validateSameVersion(fromGoldenResource, theFromGoldenResourceId);
		myMdmControllerHelper.validateSameVersion(toGoldenResource, theToGoldenResourceId);

		return myGoldenResourceMergerSvc.mergeGoldenResources(fromGoldenResource, theManuallyMergedGoldenResource, toGoldenResource, theMdmTransactionContext);
	}

	@Override
	@Deprecated
	public Page<MdmLinkJson> queryLinks(@Nullable String theGoldenResourceId, @Nullable String theSourceResourceId, @Nullable String theMatchResult, @Nullable String theLinkSource, MdmTransactionContext theMdmTransactionContext, MdmPageRequest thePageRequest) {
		MdmQuerySearchParameters mdmQuerySearchParameters = new MdmQuerySearchParameters(thePageRequest)
			.setGoldenResourceId(theGoldenResourceId)
			.setSourceId(theSourceResourceId)
			.setMatchResult(theMatchResult)
			.setLinkSource(theLinkSource);
		return queryLinksFromPartitionList(mdmQuerySearchParameters, theMdmTransactionContext);
	}

	@Override
	@Deprecated
	public Page<MdmLinkJson> queryLinks(@Nullable String theGoldenResourceId, @Nullable String theSourceResourceId,
													@Nullable String theMatchResult, @Nullable String theLinkSource, MdmTransactionContext theMdmTransactionContext,
													MdmPageRequest thePageRequest, @Nullable RequestDetails theRequestDetails) {
		MdmQuerySearchParameters mdmQuerySearchParameters = new MdmQuerySearchParameters(thePageRequest)
			.setGoldenResourceId(theGoldenResourceId)
			.setSourceId(theSourceResourceId)
			.setMatchResult(theMatchResult)
			.setLinkSource(theLinkSource);
		return queryLinks(mdmQuerySearchParameters, theMdmTransactionContext, theRequestDetails);
	}

	@Override
	public Page<MdmLinkJson> queryLinks(MdmQuerySearchParameters theMdmQuerySearchParameters, MdmTransactionContext theMdmTransactionContext, RequestDetails theRequestDetails) {
		RequestPartitionId theReadPartitionId = myRequestPartitionHelperSvc.determineReadPartitionForRequest(theRequestDetails, null);
		Page<MdmLinkJson> resultPage;
		if (theReadPartitionId.hasPartitionIds()) {
			theMdmQuerySearchParameters.setPartitionIds(theReadPartitionId.getPartitionIds());
		}
		resultPage = queryLinksFromPartitionList(theMdmQuerySearchParameters, theMdmTransactionContext);
		validateMdmQueryPermissions(theReadPartitionId, resultPage.getContent(), theRequestDetails);

		return resultPage;
	}

	@Override
	public List<MdmLinkWithRevisionJson> queryLinkHistory(MdmHistorySearchParameters theMdmHistorySearchParameters, RequestDetails theRequestDetails) {
		return myMdmLinkQuerySvc.queryLinkHistory(theMdmHistorySearchParameters);
	}

	@Override
	@Deprecated
	public Page<MdmLinkJson> queryLinksFromPartitionList(@Nullable String theGoldenResourceId, @Nullable String theSourceResourceId,
																		  @Nullable String theMatchResult, @Nullable String theLinkSource,
																		  MdmTransactionContext theMdmTransactionContext, MdmPageRequest thePageRequest,
																		  @Nullable List<Integer> thePartitionIds) {
		MdmQuerySearchParameters mdmQuerySearchParameters = new MdmQuerySearchParameters(thePageRequest)
			.setGoldenResourceId(theGoldenResourceId)
			.setSourceId(theSourceResourceId)
			.setMatchResult(theMatchResult)
			.setLinkSource(theLinkSource)
			.setPartitionIds(thePartitionIds);
		return queryLinksFromPartitionList(mdmQuerySearchParameters, theMdmTransactionContext);
	}

	@Override
	public Page<MdmLinkJson> queryLinksFromPartitionList(MdmQuerySearchParameters theMdmQuerySearchParameters, MdmTransactionContext theMdmTransactionContext) {
		return myMdmLinkQuerySvc.queryLinks(theMdmQuerySearchParameters, theMdmTransactionContext);
	}

	@Override
	public Page<MdmLinkJson> getDuplicateGoldenResources(MdmTransactionContext theMdmTransactionContext, MdmPageRequest thePageRequest) {
		return myMdmLinkQuerySvc.getDuplicateGoldenResources(theMdmTransactionContext, thePageRequest, null, null);
	}

	@Override
	public Page<MdmLinkJson> getDuplicateGoldenResources(MdmTransactionContext theMdmTransactionContext, MdmPageRequest thePageRequest, RequestDetails theRequestDetails, String theRequestResourceType) {
		Page<MdmLinkJson> resultPage;
		RequestPartitionId readPartitionId = myRequestPartitionHelperSvc.determineReadPartitionForRequest(theRequestDetails, null);

		if (readPartitionId.isAllPartitions()) {
			resultPage = myMdmLinkQuerySvc.getDuplicateGoldenResources(theMdmTransactionContext, thePageRequest, null, theRequestResourceType);
		} else {
			resultPage = myMdmLinkQuerySvc.getDuplicateGoldenResources(theMdmTransactionContext, thePageRequest, readPartitionId.getPartitionIds(), theRequestResourceType);
		}

		validateMdmQueryPermissions(readPartitionId, resultPage.getContent(), theRequestDetails);

		return resultPage;
	}

	@Override
	public IAnyResource updateLink(String theGoldenResourceId, String theSourceResourceId, String theMatchResult, MdmTransactionContext theMdmTransactionContext) {
		MdmMatchResultEnum matchResult = MdmControllerUtil.extractMatchResultOrNull(theMatchResult);
		IAnyResource goldenResource = myMdmControllerHelper.getLatestGoldenResourceFromIdOrThrowException(ProviderConstants.MDM_UPDATE_LINK_GOLDEN_RESOURCE_ID, theGoldenResourceId);
		IAnyResource source = myMdmControllerHelper.getLatestSourceFromIdOrThrowException(ProviderConstants.MDM_UPDATE_LINK_RESOURCE_ID, theSourceResourceId);
		myMdmControllerHelper.validateSameVersion(goldenResource, theGoldenResourceId);
		myMdmControllerHelper.validateSameVersion(source, theSourceResourceId);

		return myIMdmLinkUpdaterSvc.updateLink(goldenResource, source, matchResult, theMdmTransactionContext);
	}

	@Override
	public IAnyResource createLink(String theGoldenResourceId, String theSourceResourceId, @Nullable String theMatchResult, MdmTransactionContext theMdmTransactionContext) {
		MdmMatchResultEnum matchResult = MdmControllerUtil.extractMatchResultOrNull(theMatchResult);
		IAnyResource goldenResource = myMdmControllerHelper.getLatestGoldenResourceFromIdOrThrowException(ProviderConstants.MDM_CREATE_LINK_GOLDEN_RESOURCE_ID, theGoldenResourceId);
		IAnyResource source = myMdmControllerHelper.getLatestSourceFromIdOrThrowException(ProviderConstants.MDM_CREATE_LINK_RESOURCE_ID, theSourceResourceId);
		myMdmControllerHelper.validateSameVersion(goldenResource, theGoldenResourceId);
		myMdmControllerHelper.validateSameVersion(source, theSourceResourceId);

		return myIMdmLinkCreateSvc.createLink(goldenResource, source, matchResult, theMdmTransactionContext);
	}

	@Override
	public IBaseParameters submitMdmClearJob(@Nonnull List<String> theResourceNames, IPrimitiveType<BigDecimal> theBatchSize, ServletRequestDetails theRequestDetails) {
		MdmClearJobParameters params = new MdmClearJobParameters();
		params.setResourceNames(theResourceNames);
		if (theBatchSize != null && theBatchSize.getValue() != null && theBatchSize.getValue().longValue() > 0) {
			params.setBatchSize(theBatchSize.getValue().intValue());
		}

		ReadPartitionIdRequestDetails details = ReadPartitionIdRequestDetails.forOperation(null, null, ProviderConstants.OPERATION_MDM_CLEAR);
		RequestPartitionId requestPartition = myRequestPartitionHelperSvc.determineReadPartitionForRequest(theRequestDetails, details);
		params.setRequestPartitionId(requestPartition);

		JobInstanceStartRequest request = new JobInstanceStartRequest();
		request.setJobDefinitionId(MdmClearAppCtx.JOB_MDM_CLEAR);
		request.setParameters(params);
		Batch2JobStartResponse response = myJobCoordinator.startInstance(theRequestDetails, request);
		String id = response.getInstanceId();

		IBaseParameters retVal = ParametersUtil.newInstance(myFhirContext);
		ParametersUtil.addParameterToParametersString(myFhirContext, retVal, ProviderConstants.OPERATION_BATCH_RESPONSE_JOB_ID, id);
		return retVal;
	}


	@Override
	public IBaseParameters submitMdmSubmitJob(List<String> theUrls, IPrimitiveType<BigDecimal> theBatchSize, ServletRequestDetails theRequestDetails) {
		MdmSubmitJobParameters params = new MdmSubmitJobParameters();

		if (theBatchSize != null && theBatchSize.getValue() != null && theBatchSize.getValue().longValue() > 0) {
			params.setBatchSize(theBatchSize.getValue().intValue());
		}
		params.setRequestPartitionId(RequestPartitionId.allPartitions());

		theUrls.forEach(params::addUrl);

		JobInstanceStartRequest request = new JobInstanceStartRequest();
		request.setParameters(params);
		request.setJobDefinitionId(MdmSubmitAppCtx.MDM_SUBMIT_JOB);

		Batch2JobStartResponse batch2JobStartResponse = myJobCoordinator.startInstance(theRequestDetails, request);
		String id = batch2JobStartResponse.getInstanceId();

		IBaseParameters retVal = ParametersUtil.newInstance(myFhirContext);
		ParametersUtil.addParameterToParametersString(myFhirContext, retVal, ProviderConstants.OPERATION_BATCH_RESPONSE_JOB_ID, id);
		return retVal;
	}

	@Override
	public void notDuplicateGoldenResource(String theGoldenResourceId, String theTargetGoldenResourceId, MdmTransactionContext theMdmTransactionContext) {
		IAnyResource goldenResource = myMdmControllerHelper.getLatestGoldenResourceFromIdOrThrowException(ProviderConstants.MDM_UPDATE_LINK_GOLDEN_RESOURCE_ID, theGoldenResourceId);
		IAnyResource target = myMdmControllerHelper.getLatestGoldenResourceFromIdOrThrowException(ProviderConstants.MDM_UPDATE_LINK_RESOURCE_ID, theTargetGoldenResourceId);

		myIMdmLinkUpdaterSvc.notDuplicateGoldenResource(goldenResource, target, theMdmTransactionContext);
	}

	private void validateMdmQueryPermissions(RequestPartitionId theRequestPartitionId, List<MdmLinkJson> theMdmLinkJsonList, RequestDetails theRequestDetails) {
		Set<String> seenResourceTypes = new HashSet<>();
		for (MdmLinkJson mdmLinkJson : theMdmLinkJsonList) {
			IdDt idDt = new IdDt(mdmLinkJson.getSourceId());

			if (!seenResourceTypes.contains(idDt.getResourceType())) {
				myRequestPartitionHelperSvc.validateHasPartitionPermissions(theRequestDetails, idDt.getResourceType(), theRequestPartitionId);
				seenResourceTypes.add(idDt.getResourceType());
			}
		}
	}
}
