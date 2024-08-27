/*-
 * #%L
 * hapi-fhir-storage-batch2-jobs
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.api.model.PersistentIdToForcedIdMap;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.bulk.export.api.IBulkExportProcessor;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
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
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import jakarta.annotation.Nonnull;
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

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
	private JpaStorageSettings myStorageSettings;

	@Autowired
	private ApplicationContext myApplicationContext;

	@Autowired
	private InterceptorService myInterceptorService;

	@Autowired
	private IIdHelperService myIdHelperService;

	@Autowired
	private IHapiTransactionService myTransactionService;

	private volatile ResponseTerminologyTranslationSvc myResponseTerminologyTranslationSvc;

	/**
	 * Note on the design of this step:
	 * This step takes a list of resource PIDs as input, fetches those
	 * resources, applies a bunch of filtering/consent/MDM/etc. modifications
	 * on them, serializes the result as NDJSON files, and then persists those
	 * NDJSON files as Binary resources.
	 * <p>
	 * We want to avoid writing files which exceed the configured maximum
	 * file size, and we also want to avoid keeping too much in memory
	 * at any given time, so this class works a bit like a stream processor
	 * (although not using Java streams).
	 * <p>
	 * The {@link #fetchResourcesByIdAndConsumeThem(ResourceIdList, RequestPartitionId, Consumer)}
	 * method loads the resources by ID, {@link ExpandResourcesConsumer} handles
	 * the filtering and whatnot, then the {@link NdJsonResourceWriter}
	 * ultimately writes them.
	 */
	@Nonnull
	@Override
	public RunOutcome run(
			@Nonnull StepExecutionDetails<BulkExportJobParameters, ResourceIdList> theStepExecutionDetails,
			@Nonnull IJobDataSink<BulkExportBinaryFileId> theDataSink)
			throws JobExecutionFailedException {

		// Currently only NDJSON output format is supported, but we could add other
		// kinds of writers here for other formats if needed
		NdJsonResourceWriter resourceWriter = new NdJsonResourceWriter(theStepExecutionDetails, theDataSink);

		expandResourcesFromList(theStepExecutionDetails, resourceWriter);

		return new RunOutcome(resourceWriter.getNumResourcesProcessed());
	}

	private void expandResourcesFromList(
			StepExecutionDetails<BulkExportJobParameters, ResourceIdList> theStepExecutionDetails,
			Consumer<ExpandedResourcesList> theResourceWriter) {

		ResourceIdList idList = theStepExecutionDetails.getData();
		BulkExportJobParameters parameters = theStepExecutionDetails.getParameters();

		Consumer<List<IBaseResource>> resourceListConsumer =
				new ExpandResourcesConsumer(theStepExecutionDetails, theResourceWriter);

		// search the resources
		fetchResourcesByIdAndConsumeThem(idList, parameters.getPartitionId(), resourceListConsumer);
	}

	private void fetchResourcesByIdAndConsumeThem(
			ResourceIdList theIds,
			RequestPartitionId theRequestPartitionId,
			Consumer<List<IBaseResource>> theResourceListConsumer) {
		ArrayListMultimap<String, String> typeToIds = ArrayListMultimap.create();
		theIds.getIds().forEach(t -> typeToIds.put(t.getResourceType(), t.getId()));

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
				theResourceListConsumer.accept(outcome.getAllResources());
			}
		}
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

	@VisibleForTesting
	public void setIdHelperServiceForUnitTest(IIdHelperService theIdHelperService) {
		myIdHelperService = theIdHelperService;
	}

	/**
	 * This class takes a collection of lists of resources read from the
	 * repository, and processes them, then converts them into
	 * {@link ExpandedResourcesList} instances, each one of which corresponds
	 * to a single output file. We try to avoid exceeding the maximum file
	 * size defined in
	 * {@link JpaStorageSettings#getBulkExportFileMaximumSize()}
	 * so we will do our best to emit multiple lists in favour of emitting
	 * a list that exceeds that threshold.
	 */
	private class ExpandResourcesConsumer implements Consumer<List<IBaseResource>> {

		private final Consumer<ExpandedResourcesList> myResourceWriter;
		private final StepExecutionDetails<BulkExportJobParameters, ResourceIdList> myStepExecutionDetails;

		public ExpandResourcesConsumer(
				StepExecutionDetails<BulkExportJobParameters, ResourceIdList> theStepExecutionDetails,
				Consumer<ExpandedResourcesList> theResourceWriter) {
			myStepExecutionDetails = theStepExecutionDetails;
			myResourceWriter = theResourceWriter;
		}

		@Override
		public void accept(List<IBaseResource> theResources) throws JobExecutionFailedException {
			String instanceId = myStepExecutionDetails.getInstance().getInstanceId();
			String chunkId = myStepExecutionDetails.getChunkId();
			ResourceIdList idList = myStepExecutionDetails.getData();
			BulkExportJobParameters parameters = myStepExecutionDetails.getParameters();

			ourLog.info(
					"Bulk export instance[{}] chunk[{}] - About to expand {} resource IDs into their full resource bodies.",
					instanceId,
					chunkId,
					idList.getIds().size());

			// Apply post-fetch filtering
			String resourceType = idList.getResourceType();
			List<String> postFetchFilterUrls = parameters.getPostFetchFilterUrls().stream()
					.filter(t -> t.substring(0, t.indexOf('?')).equals(resourceType))
					.collect(Collectors.toList());

			if (!postFetchFilterUrls.isEmpty()) {
				applyPostFetchFiltering(theResources, postFetchFilterUrls, instanceId, chunkId);
			}

			// if necessary, expand resources
			if (parameters.isExpandMdm()) {
				myBulkExportProcessor.expandMdmResources(theResources);
			}

			// Normalize terminology
			if (myStorageSettings.isNormalizeTerminologyForBulkExportJobs()) {
				ResponseTerminologyTranslationSvc terminologyTranslationSvc = myResponseTerminologyTranslationSvc;
				if (terminologyTranslationSvc == null) {
					terminologyTranslationSvc = myApplicationContext.getBean(ResponseTerminologyTranslationSvc.class);
					myResponseTerminologyTranslationSvc = terminologyTranslationSvc;
				}
				terminologyTranslationSvc.processResourcesForTerminologyTranslation(theResources);
			}

			// Interceptor call
			if (myInterceptorService.hasHooks(Pointcut.STORAGE_BULK_EXPORT_RESOURCE_INCLUSION)) {
				for (Iterator<IBaseResource> iter = theResources.iterator(); iter.hasNext(); ) {
					HookParams params = new HookParams()
							.add(BulkExportJobParameters.class, myStepExecutionDetails.getParameters())
							.add(IBaseResource.class, iter.next());
					boolean outcome =
							myInterceptorService.callHooks(Pointcut.STORAGE_BULK_EXPORT_RESOURCE_INCLUSION, params);
					if (!outcome) {
						iter.remove();
					}
				}
			}

			// encode them - Key is resource type, Value is a collection of serialized resources of that type
			IParser parser = getParser(parameters);

			ListMultimap<String, String> resourceTypeToStringifiedResources = ArrayListMultimap.create();
			Map<String, Integer> resourceTypeToTotalSize = new HashMap<>();
			for (IBaseResource resource : theResources) {
				String type = myFhirContext.getResourceType(resource);
				int existingSize = resourceTypeToTotalSize.getOrDefault(type, 0);

				String jsonResource = parser.encodeResourceToString(resource);
				int newSize = existingSize + jsonResource.length();

				// If adding another stringified resource to the list for the given type
				// would exceed the configured maximum allowed, then let's send the current
				// list and flush it. Note that if a single resource exceeds the configurable
				// maximum then we have no choice but to send it
				long bulkExportFileMaximumSize = myStorageSettings.getBulkExportFileMaximumSize();
				if (newSize > bulkExportFileMaximumSize) {
					if (existingSize == 0) {
						// If no files are already in the collection, then this one file
						// is bigger than the maximum allowable. We'll allow it in that
						// case
						ourLog.warn(
								"Single resource size {} exceeds allowable maximum of {}, so will ignore maximum",
								newSize,
								bulkExportFileMaximumSize);
					} else {
						// Otherwise, flush the contents now before adding the next file
						List<String> stringifiedResources = resourceTypeToStringifiedResources.get(type);
						writeStringifiedResources(type, stringifiedResources);

						resourceTypeToStringifiedResources.removeAll(type);
						newSize = jsonResource.length();
					}
				}

				resourceTypeToStringifiedResources.put(type, jsonResource);
				resourceTypeToTotalSize.put(type, newSize);
			}

			for (String nextResourceType : resourceTypeToStringifiedResources.keySet()) {
				List<String> stringifiedResources = resourceTypeToStringifiedResources.get(nextResourceType);
				writeStringifiedResources(nextResourceType, stringifiedResources);
			}
		}

		private void writeStringifiedResources(String theResourceType, List<String> theStringifiedResources) {
			if (!theStringifiedResources.isEmpty()) {

				ExpandedResourcesList output = new ExpandedResourcesList();
				output.setStringifiedResources(theStringifiedResources);
				output.setResourceType(theResourceType);
				myResourceWriter.accept(output);

				ourLog.info(
						"Expanding of {} resources of type {} completed",
						theStringifiedResources.size(),
						theResourceType);
			}
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

		private IParser getParser(BulkExportJobParameters theParameters) {
			// The parser depends on the output format
			// but for now, only ndjson is supported
			// see WriteBinaryStep as well
			return myFhirContext.newJsonParser().setPrettyPrint(false);
		}
	}

	/**
	 * This class takes a collection of expanded resources, and expands it to
	 * an NDJSON file, which is written to a Binary resource.
	 */
	private class NdJsonResourceWriter implements Consumer<ExpandedResourcesList> {

		private final StepExecutionDetails<BulkExportJobParameters, ResourceIdList> myStepExecutionDetails;
		private final IJobDataSink<BulkExportBinaryFileId> myDataSink;
		private int myNumResourcesProcessed = 0;

		public NdJsonResourceWriter(
				StepExecutionDetails<BulkExportJobParameters, ResourceIdList> theStepExecutionDetails,
				IJobDataSink<BulkExportBinaryFileId> theDataSink) {
			this.myStepExecutionDetails = theStepExecutionDetails;
			this.myDataSink = theDataSink;
		}

		public int getNumResourcesProcessed() {
			return myNumResourcesProcessed;
		}

		@Override
		public void accept(ExpandedResourcesList theExpandedResourcesList) throws JobExecutionFailedException {
			int batchSize = theExpandedResourcesList.getStringifiedResources().size();
			ourLog.info("Writing {} resources to binary file", batchSize);

			myNumResourcesProcessed += batchSize;

			@SuppressWarnings("unchecked")
			IFhirResourceDao<IBaseBinary> binaryDao = myDaoRegistry.getResourceDao("Binary");

			IBaseBinary binary = BinaryUtil.newBinary(myFhirContext);

			addMetadataExtensionsToBinary(myStepExecutionDetails, theExpandedResourcesList, binary);

			binary.setContentType(Constants.CT_FHIR_NDJSON);

			int processedRecordsCount = 0;
			try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
				try (OutputStreamWriter streamWriter = getStreamWriter(outputStream)) {
					for (String stringified : theExpandedResourcesList.getStringifiedResources()) {
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
						theExpandedResourcesList.getResourceType(), ex.getMessage());
				ourLog.error(errorMsg);

				throw new JobExecutionFailedException(Msg.code(2431) + errorMsg);
			}

			SystemRequestDetails srd = new SystemRequestDetails();
			BulkExportJobParameters jobParameters = myStepExecutionDetails.getParameters();
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
			bulkExportBinaryFileId.setResourceType(theExpandedResourcesList.getResourceType());
			myDataSink.accept(bulkExportBinaryFileId);

			ourLog.info(
					"Binary writing complete for {} resources of type {}.",
					processedRecordsCount,
					theExpandedResourcesList.getResourceType());
		}
	}
}
