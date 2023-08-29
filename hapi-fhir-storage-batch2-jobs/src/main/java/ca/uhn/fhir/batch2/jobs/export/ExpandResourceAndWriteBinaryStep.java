/*-
 * #%L
 * hapi-fhir-storage-batch2-jobs
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
package ca.uhn.fhir.batch2.jobs.export;

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IJobStepWorker;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.jobs.export.models.BulkExportBinaryFileId;
import ca.uhn.fhir.batch2.jobs.export.models.ExpandedResourcesList;
import ca.uhn.fhir.batch2.jobs.export.models.ResourceIdList;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.interceptor.executor.InterceptorService;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.api.model.PersistentIdToForcedIdMap;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.bulk.export.api.IBulkExportProcessor;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.jpa.model.entity.StorageSettings;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.matcher.InMemoryMatchResult;
import ca.uhn.fhir.jpa.searchparam.matcher.InMemoryResourceMatcher;
import ca.uhn.fhir.jpa.util.RandomTextUtils;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.api.server.bulk.BulkExportJobParameters;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import ca.uhn.fhir.rest.param.TokenOrListParam;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.interceptor.ResponseTerminologyTranslationSvc;
import ca.uhn.fhir.util.BinaryUtil;
import ca.uhn.fhir.util.FhirTerser;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IBaseBinary;
import org.hl7.fhir.instance.model.api.IBaseExtension;
import org.hl7.fhir.instance.model.api.IBaseHasExtensions;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;

import static ca.uhn.fhir.rest.api.Constants.PARAM_ID;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.slf4j.LoggerFactory.getLogger;

public class ExpandResourceAndWriteBinaryStep
		implements IJobStepWorker<BulkExportJobParameters, ResourceIdList, BulkExportBinaryFileId> {
	private static final Logger ourLog = getLogger(ExpandResourceAndWriteBinaryStep.class);

	@Autowired
	private FhirContext myFhirContext;

	@Autowired
	private DaoRegistry myDaoRegistry;

	@Autowired
	private InMemoryResourceMatcher myInMemoryResourceMatcher;

	@Autowired
	private IBulkExportProcessor<?> myBulkExportProcessor;

	@Autowired
	private StorageSettings myStorageSettings;

	@Autowired
	private ApplicationContext myApplicationContext;

	@Autowired
	private InterceptorService myInterceptorService;

	@Autowired
	private IIdHelperService myIdHelperService;

	@Autowired
	private IHapiTransactionService myTransactionService;

	private volatile ResponseTerminologyTranslationSvc myResponseTerminologyTranslationSvc;

	@Nonnull
	@Override
	public RunOutcome run(
			@Nonnull StepExecutionDetails<BulkExportJobParameters, ResourceIdList> theStepExecutionDetails,
			@Nonnull IJobDataSink<BulkExportBinaryFileId> theDataSink)
			throws JobExecutionFailedException {

		List<ExpandedResourcesList> expandedResourcesList = expandResourcesFromList(theStepExecutionDetails);
		int numResourcesProcessed = 0;
		ourLog.info("Write binary step of Job Export");

		// write to binary each resource type separately, without chunking, we need to do this in a loop now
		for (ExpandedResourcesList expandedResources : expandedResourcesList) {

			numResourcesProcessed += expandedResources.getStringifiedResources().size();

			ourLog.info("Writing {} resources to binary file", numResourcesProcessed);

			@SuppressWarnings("unchecked")
			IFhirResourceDao<IBaseBinary> binaryDao = myDaoRegistry.getResourceDao("Binary");

			IBaseBinary binary = BinaryUtil.newBinary(myFhirContext);

			addMetadataExtensionsToBinary(theStepExecutionDetails, expandedResources, binary);

			// TODO
			// should be dependent on the output format in parameters but for now, only NDJSON is supported
			binary.setContentType(Constants.CT_FHIR_NDJSON);

			int processedRecordsCount = 0;
			try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
				try (OutputStreamWriter streamWriter = getStreamWriter(outputStream)) {
					for (String stringified : expandedResources.getStringifiedResources()) {
						streamWriter.append(stringified);
						streamWriter.append("\n");
						processedRecordsCount++;
					}
					streamWriter.flush();
					outputStream.flush();
				}
				binary.setContent(outputStream.toByteArray());
			} catch (IOException ex) {
				String errorMsg = String.format(
						"Failure to process resource of type %s : %s",
						expandedResources.getResourceType(), ex.getMessage());
				ourLog.error(errorMsg);

				throw new JobExecutionFailedException(Msg.code(2431) + errorMsg);
			}

			SystemRequestDetails srd = new SystemRequestDetails();
			BulkExportJobParameters jobParameters = theStepExecutionDetails.getParameters();
			RequestPartitionId partitionId = jobParameters.getPartitionId();
			if (partitionId == null) {
				srd.setRequestPartitionId(RequestPartitionId.defaultPartition());
			} else {
				srd.setRequestPartitionId(partitionId);
			}

			// Pick a unique ID and retry until we get one that isn't already used. This is just to
			// avoid any possibility of people guessing the IDs of these Binaries and fishing for them.
			while (true) {
				// Use a random ID to make it harder to guess IDs - 32 characters of a-zA-Z0-9
				// has 190 bts of entropy according to https://www.omnicalculator.com/other/password-entropy
				String proposedId = RandomTextUtils.newSecureRandomAlphaNumericString(32);
				binary.setId(proposedId);

				// Make sure we don't accidentally reuse an ID. This should be impossible given the
				// amount of entropy in the IDs but might as well be sure.
				try {
					IBaseBinary output = binaryDao.read(binary.getIdElement(), new SystemRequestDetails(), true);
					if (output != null) {
						continue;
					}
				} catch (ResourceNotFoundException e) {
					// good
				}

				break;
			}

			if (myFhirContext.getVersion().getVersion().isNewerThan(FhirVersionEnum.DSTU2)) {
				if (isNotBlank(jobParameters.getBinarySecurityContextIdentifierSystem())
						|| isNotBlank(jobParameters.getBinarySecurityContextIdentifierValue())) {
					FhirTerser terser = myFhirContext.newTerser();
					terser.setElement(
							binary,
							"securityContext.identifier.system",
							jobParameters.getBinarySecurityContextIdentifierSystem());
					terser.setElement(
							binary,
							"securityContext.identifier.value",
							jobParameters.getBinarySecurityContextIdentifierValue());
				}
			}

			DaoMethodOutcome outcome = binaryDao.update(binary, srd);
			IIdType id = outcome.getId();

			BulkExportBinaryFileId bulkExportBinaryFileId = new BulkExportBinaryFileId();
			bulkExportBinaryFileId.setBinaryId(id.getValueAsString());
			bulkExportBinaryFileId.setResourceType(expandedResources.getResourceType());
			theDataSink.accept(bulkExportBinaryFileId);

			ourLog.info(
					"Binary writing complete for {} resources of type {}.",
					processedRecordsCount,
					expandedResources.getResourceType());
		}
		return new RunOutcome(numResourcesProcessed);
	}

	private List<ExpandedResourcesList> expandResourcesFromList(
			StepExecutionDetails<BulkExportJobParameters, ResourceIdList> theStepExecutionDetails) {
		List<ExpandedResourcesList> expandedResourcesList = new ArrayList<>();
		String instanceId = theStepExecutionDetails.getInstance().getInstanceId();
		String chunkId = theStepExecutionDetails.getChunkId();
		ResourceIdList idList = theStepExecutionDetails.getData();
		BulkExportJobParameters parameters = theStepExecutionDetails.getParameters();

		ourLog.info(
				"Bulk export instance[{}] chunk[{}] - About to expand {} resource IDs into their full resource bodies.",
				instanceId,
				chunkId,
				idList.getIds().size());

		// search the resources
		List<IBaseResource> allResources = fetchAllResources(idList, parameters.getPartitionId());

		// Apply post-fetch filtering
		String resourceType = idList.getResourceType();
		List<String> postFetchFilterUrls = parameters.getPostFetchFilterUrls().stream()
				.filter(t -> t.substring(0, t.indexOf('?')).equals(resourceType))
				.collect(Collectors.toList());

		if (!postFetchFilterUrls.isEmpty()) {
			applyPostFetchFiltering(allResources, postFetchFilterUrls, instanceId, chunkId);
		}

		// if necessary, expand resources
		if (parameters.isExpandMdm()) {
			myBulkExportProcessor.expandMdmResources(allResources);
		}

		// Normalize terminology
		if (myStorageSettings.isNormalizeTerminologyForBulkExportJobs()) {
			ResponseTerminologyTranslationSvc terminologyTranslationSvc = myResponseTerminologyTranslationSvc;
			if (terminologyTranslationSvc == null) {
				terminologyTranslationSvc = myApplicationContext.getBean(ResponseTerminologyTranslationSvc.class);
				myResponseTerminologyTranslationSvc = terminologyTranslationSvc;
			}
			terminologyTranslationSvc.processResourcesForTerminologyTranslation(allResources);
		}

		// Interceptor call
		if (myInterceptorService.hasHooks(Pointcut.STORAGE_BULK_EXPORT_RESOURCE_INCLUSION)) {
			for (Iterator<IBaseResource> iter = allResources.iterator(); iter.hasNext(); ) {
				HookParams params = new HookParams()
						.add(BulkExportJobParameters.class, theStepExecutionDetails.getParameters())
						.add(IBaseResource.class, iter.next());
				boolean outcome =
						myInterceptorService.callHooks(Pointcut.STORAGE_BULK_EXPORT_RESOURCE_INCLUSION, params);
				if (!outcome) {
					iter.remove();
				}
			}
		}

		// encode them - Key is resource type, Value is a collection of serialized resources of that type
		ListMultimap<String, String> resources = encodeToString(allResources, parameters);

		for (String nextResourceType : resources.keySet()) {

			ExpandedResourcesList output = new ExpandedResourcesList();
			output.setStringifiedResources(resources.get(nextResourceType));
			output.setResourceType(nextResourceType);
			expandedResourcesList.add(output);

			ourLog.info(
					"Expanding of {} resources of type {} completed",
					idList.getIds().size(),
					idList.getResourceType());
		}
		return expandedResourcesList;
	}

	private void applyPostFetchFiltering(
			List<IBaseResource> theResources,
			List<String> thePostFetchFilterUrls,
			String theInstanceId,
			String theChunkId) {
		int numRemoved = 0;
		for (Iterator<IBaseResource> iter = theResources.iterator(); iter.hasNext(); ) {
			boolean matched = applyPostFetchFilteringForSingleResource(thePostFetchFilterUrls, iter);

			if (!matched) {
				iter.remove();
				numRemoved++;
			}
		}

		if (numRemoved > 0) {
			ourLog.info(
					"Bulk export instance[{}] chunk[{}] - {} resources were filtered out because of post-fetch filter URLs",
					theInstanceId,
					theChunkId,
					numRemoved);
		}
	}

	private boolean applyPostFetchFilteringForSingleResource(
			List<String> thePostFetchFilterUrls, Iterator<IBaseResource> iter) {
		IBaseResource nextResource = iter.next();
		String nextResourceType = myFhirContext.getResourceType(nextResource);

		for (String nextPostFetchFilterUrl : thePostFetchFilterUrls) {
			if (nextPostFetchFilterUrl.contains("?")) {
				String resourceType = nextPostFetchFilterUrl.substring(0, nextPostFetchFilterUrl.indexOf('?'));
				if (nextResourceType.equals(resourceType)) {
					InMemoryMatchResult matchResult = myInMemoryResourceMatcher.match(
							nextPostFetchFilterUrl, nextResource, null, new SystemRequestDetails());
					if (matchResult.matched()) {
						return true;
					}
				}
			}
		}
		return false;
	}

	private List<IBaseResource> fetchAllResources(ResourceIdList theIds, RequestPartitionId theRequestPartitionId) {
		ArrayListMultimap<String, String> typeToIds = ArrayListMultimap.create();
		theIds.getIds().forEach(t -> typeToIds.put(t.getResourceType(), t.getId()));

		List<IBaseResource> resources = new ArrayList<>(theIds.getIds().size());

		for (String resourceType : typeToIds.keySet()) {

			IFhirResourceDao<?> dao = myDaoRegistry.getResourceDao(resourceType);
			List<String> allIds = typeToIds.get(resourceType);
			while (!allIds.isEmpty()) {

				// Load in batches in order to avoid having too many PIDs go into a
				// single SQ statement at once
				int batchSize = Math.min(500, allIds.size());

				Set<IResourcePersistentId> nextBatchOfPids = allIds.subList(0, batchSize).stream()
						.map(t -> myIdHelperService.newPidFromStringIdAndResourceName(t, resourceType))
						.collect(Collectors.toSet());
				allIds = allIds.subList(batchSize, allIds.size());

				PersistentIdToForcedIdMap nextBatchOfResourceIds = myTransactionService
						.withRequest(null)
						.execute(() -> myIdHelperService.translatePidsToForcedIds(nextBatchOfPids));

				TokenOrListParam idListParam = new TokenOrListParam();
				for (IResourcePersistentId nextPid : nextBatchOfPids) {
					Optional<String> resourceId = nextBatchOfResourceIds.get(nextPid);
					idListParam.add(resourceId.orElse(nextPid.getId().toString()));
				}

				SearchParameterMap spMap = SearchParameterMap.newSynchronous().add(PARAM_ID, idListParam);
				IBundleProvider outcome =
						dao.search(spMap, new SystemRequestDetails().setRequestPartitionId(theRequestPartitionId));
				resources.addAll(outcome.getAllResources());
			}
		}

		return resources;
	}

	private ListMultimap<String, String> encodeToString(
			List<IBaseResource> theResources, BulkExportJobParameters theParameters) {
		IParser parser = getParser(theParameters);

		ListMultimap<String, String> retVal = ArrayListMultimap.create();
		for (IBaseResource resource : theResources) {
			String type = myFhirContext.getResourceType(resource);
			String jsonResource = parser.encodeResourceToString(resource);
			retVal.put(type, jsonResource);
		}
		return retVal;
	}

	private IParser getParser(BulkExportJobParameters theParameters) {
		// The parser depends on the output format
		// but for now, only ndjson is supported
		// see WriteBinaryStep as well
		return myFhirContext.newJsonParser().setPrettyPrint(false);
	}

	/**
	 * Adds 3 extensions to the `binary.meta` element.
	 *
	 * 1. the _exportId provided at request time
	 * 2. the job_id of the job instance.
	 * 3. the resource type of the resources contained in the binary
	 */
	private void addMetadataExtensionsToBinary(
			@Nonnull StepExecutionDetails<BulkExportJobParameters, ResourceIdList> theStepExecutionDetails,
			ExpandedResourcesList expandedResources,
			IBaseBinary binary) {
		// Note that this applies only to hl7.org structures, so these extensions will not be added
		// to DSTU2 structures
		if (binary.getMeta() instanceof IBaseHasExtensions) {
			IBaseHasExtensions meta = (IBaseHasExtensions) binary.getMeta();

			// export identifier, potentially null.
			String exportIdentifier = theStepExecutionDetails.getParameters().getExportIdentifier();
			if (!StringUtils.isBlank(exportIdentifier)) {
				IBaseExtension<?, ?> exportIdentifierExtension = meta.addExtension();
				exportIdentifierExtension.setUrl(JpaConstants.BULK_META_EXTENSION_EXPORT_IDENTIFIER);
				exportIdentifierExtension.setValue(myFhirContext.newPrimitiveString(exportIdentifier));
			}

			// job id
			IBaseExtension<?, ?> jobExtension = meta.addExtension();
			jobExtension.setUrl(JpaConstants.BULK_META_EXTENSION_JOB_ID);
			jobExtension.setValue(myFhirContext.newPrimitiveString(
					theStepExecutionDetails.getInstance().getInstanceId()));

			// resource type
			IBaseExtension<?, ?> typeExtension = meta.addExtension();
			typeExtension.setUrl(JpaConstants.BULK_META_EXTENSION_RESOURCE_TYPE);
			typeExtension.setValue(myFhirContext.newPrimitiveString(expandedResources.getResourceType()));
		} else {
			ourLog.warn(
					"Could not attach metadata extensions to binary resource, as this binary metadata does not support extensions");
		}
	}

	/**
	 * Returns an output stream writer
	 * (exposed for testing)
	 */
	protected OutputStreamWriter getStreamWriter(ByteArrayOutputStream theOutputStream) {
		return new OutputStreamWriter(theOutputStream, Constants.CHARSET_UTF8);
	}
}
