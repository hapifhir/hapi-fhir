package ca.uhn.fhir.batch2.jobs.export.svcs;

import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.jobs.export.models.ResourceIdList;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.interceptor.executor.InterceptorService;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.bulk.export.api.IBulkExportProcessor;
import ca.uhn.fhir.jpa.searchparam.matcher.InMemoryMatchResult;
import ca.uhn.fhir.jpa.searchparam.matcher.InMemoryResourceMatcher;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.api.server.bulk.BulkExportJobParameters;
import ca.uhn.fhir.rest.api.server.bulk.BulkExportResourceList;
import ca.uhn.fhir.rest.api.server.bulk.ConvertedFile;
import ca.uhn.fhir.rest.api.server.bulk.ConvertedFiles;
import ca.uhn.fhir.rest.server.interceptor.ResponseTerminologyTranslationSvc;
import org.apache.commons.lang3.ObjectUtils;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;

import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * This class takes a lists of resources read from the
 * repository and processes them.
 *
 * It keeps track of how many resources were processed
 * and delegates to the {@link IResourceConverter} to convert the
 * consumed resources to the required output before writing them
 * to a binary file using the BinaryCreator (passed in).
 *
 * We try to avoid exceeding the maximum file
 * size defined in
 * {@link JpaStorageSettings#getBulkExportFileMaximumSize()}
 * so we will do our best to emit multiple lists in favour of emitting
 * a list that exceeds that threshold.
 */
public class ExpandResourcesConsumer implements Consumer<List<IBaseResource>> {
	private static final Logger ourLog = getLogger(ExpandResourcesConsumer.class);

	private final StepExecutionDetails<BulkExportJobParameters, ResourceIdList> myStepExecutionDetails;

	private final FhirContext myFhirContext;

	private final IBulkExportProcessor<?> myBulkExportProcessor;

	private final InterceptorService myInterceptorService;

	private final JpaStorageSettings myStorageSettings;

	private final InMemoryResourceMatcher myInMemoryResourceMatcher;

	private final ResponseTerminologyTranslationSvc myResponseTerminologyTranslationSvc;

	private final BinaryCreator myBinaryCreator;

	private boolean myDoExpandMDM;

	private int myConsumedResources;

	public ExpandResourcesConsumer(
		FhirContext theContext,
		IBulkExportProcessor<?> theIBulkExportProcessor,
		InterceptorService theInterceptorService,
		JpaStorageSettings theSettings,
		InMemoryResourceMatcher theInMemoryMatcher,
		ResponseTerminologyTranslationSvc theResponseTerminologyTranslationSvc,
		BinaryCreator theBinaryCreator,
		StepExecutionDetails<BulkExportJobParameters, ResourceIdList> theStepExecutionDetails
	) {
		myFhirContext = theContext;
		myBulkExportProcessor = theIBulkExportProcessor;
		myInterceptorService = theInterceptorService;
		myStorageSettings = theSettings;
		myInMemoryResourceMatcher = theInMemoryMatcher;
		myResponseTerminologyTranslationSvc = theResponseTerminologyTranslationSvc;
		myBinaryCreator = theBinaryCreator;
		myStepExecutionDetails = theStepExecutionDetails;
	}

	/**
	 * Whether or not mdm expansion should be done on the resources
	 */
	public void setDoExpandMDM(boolean theDoExpandMDM) {
		myDoExpandMDM = theDoExpandMDM;
	}

	/**
	 * How many resources this consumer has processed.
	 * Note that this does *not* include any resources added
	 * by custom IResourceConverters (which may add additional resources
	 * to the output without informing the caller).
	 */
	public int getConsumedResourceCount() {
		return myConsumedResources;
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
		if (myDoExpandMDM) {
			myBulkExportProcessor.expandMdmResources(theResources);
		}

		// Normalize terminology
		if (myStorageSettings.isNormalizeTerminologyForBulkExportJobs()) {
			myResponseTerminologyTranslationSvc.processResourcesForTerminologyTranslation(theResources);
		}

		// Interceptor call - remove omitted resources
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

		// we are using the list of resources we have
		// because we cannot rely on a consumer of a pointcut to
		// properly tell us how many resources they are filling in
		myConsumedResources += theResources.size();

		BulkExportResourceList resourceList = new BulkExportResourceList();
		resourceList.setResources(theResources);

		ConvertedFiles files = null;
		if (myInterceptorService.hasHooks(Pointcut.STORAGE_BULK_EXPORT_RESOURCE_CONVERT)) {
			/*
			 * if there's a pointcut, we'll use this for the conversion first.
			 * This allows consumers to override even our NDJson Conversion
			 * (default).
			 */
			IResourceConverter converter = new PointcutProvidedConverter(myInterceptorService);
			files = converter.consume(resourceList, myStepExecutionDetails.getParameters());
		}

		if (files == null && isNdJson()) {
			/*
			 * no converted files created but format is NDJson.
			 * Either:
			 * * no pointcut exists (at all)
			 * * no pointcut exists for this particular mime type (ndjson)
			 *
			 * Fall back to our default
			 */
			files = new NDJsonConverter(myFhirContext, myStorageSettings)
				.consume(resourceList, myStepExecutionDetails.getParameters());
		}

		if (files == null) {
			// TODO error (we shouldn't get here)
			throw new RuntimeException(
				String.format("Output format %s not supported",
					myStepExecutionDetails.getParameters().getOutputFormat())
			);
		}

		// create the binaries
		// (note that the IResourceConverter could produce
		// potentially many files for 1 set of resources)
		for (ConvertedFile file : files.getFiles()) {
			myBinaryCreator.accept(files);
		}
	}

	private boolean isNdJson() {
		return ObjectUtils.firstNonNull(myStepExecutionDetails.getParameters()
				.getOutputFormat(), Constants.CT_APP_NDJSON)
			.equals(Constants.CT_APP_NDJSON);
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
}

