package ca.uhn.fhir.batch2.jobs.export;

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IJobStepWorker;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.jobs.export.models.BulkExportExpandedResources;
import ca.uhn.fhir.batch2.jobs.export.models.BulkExportIdList;
import ca.uhn.fhir.batch2.jobs.export.models.BulkExportJobParameters;
import ca.uhn.fhir.batch2.jobs.models.Id;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.bulk.export.api.IBulkExportProcessor;
import ca.uhn.fhir.jpa.partition.SystemRequestDetails;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.TokenOrListParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.util.ExtensionUtil;
import ca.uhn.fhir.util.HapiExtensions;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IBaseExtension;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static ca.uhn.fhir.jpa.batch.config.BatchConstants.PATIENT_BULK_EXPORT_FORWARD_REFERENCE_RESOURCE_TYPES;
import static org.slf4j.LoggerFactory.getLogger;

public class ExpandResourcesStep implements IJobStepWorker<BulkExportJobParameters, BulkExportIdList, BulkExportExpandedResources> {
	private static final Logger ourLog = getLogger(ExpandResourcesStep.class);

	@Autowired
	private DaoRegistry myDaoRegistry;

	@Autowired
	private FhirContext myFhirContext;

	@Autowired
	private IBulkExportProcessor myBulkExportProcessor;

	@Nonnull
	@Override
	public RunOutcome run(@Nonnull StepExecutionDetails<BulkExportJobParameters, BulkExportIdList> theStepExecutionDetails,
								 @Nonnull IJobDataSink<BulkExportExpandedResources> theDataSink) throws JobExecutionFailedException {
		BulkExportIdList idList = theStepExecutionDetails.getData();
		BulkExportJobParameters jobParameters = theStepExecutionDetails.getParameters();

		ourLog.info(jobParameters.getJobId() + " step 2 started");

		// search the resources
		IBundleProvider bundle = fetchAllResources(idList);

		List<IBaseResource> allResources = bundle.getAllResources();

		// if necessary, expand resources
		if (jobParameters.isExpandMdm()) {
			myBulkExportProcessor.expandMdmResources(allResources);
		}

		// encode them
		List<String> resources = encodeToString(allResources, jobParameters);

		// set to datasink
		BulkExportExpandedResources output = new BulkExportExpandedResources();
		output.setJobId(idList.getJobId());
		output.setStringifiedResources(resources);
		output.setResourceType(idList.getResourceType());
		theDataSink.accept(output);

		ourLog.trace("Expanding of {} resources of type {} completed",
			idList.getIds().size(),
			idList.getResourceType());

		// and return
		return RunOutcome.SUCCESS;
	}

	private IBundleProvider fetchAllResources(BulkExportIdList theIds) {
		IFhirResourceDao<?> dao = myDaoRegistry.getResourceDao(theIds.getResourceType());

		SearchParameterMap map = new SearchParameterMap();
		TokenOrListParam ids = new TokenOrListParam();
		for (Id id : theIds.getIds()) {
			ids.addOr(new TokenParam(id.toPID().getAssociatedResourceId().getValue()));
		}
		map.add(Constants.PARAM_ID, ids);
		return dao.search(map, SystemRequestDetails.forAllPartitions());
	}

	private List<String> encodeToString(List<IBaseResource> theResources, BulkExportJobParameters theParameters) {
		IParser parser = getParser(theParameters);

		List<String> resources = new ArrayList<>();
		for (IBaseResource resource : theResources) {
			String jsonResource = parser.encodeResourceToString(resource);
			resources.add(jsonResource);
		}
		return resources;
	}

	private IParser getParser(BulkExportJobParameters theParameters) {
		// The parser depends on the
		// output format
		// (but for now, only ndjson is supported
		// see WriteBinaryStep as well
		return myFhirContext.newJsonParser().setPrettyPrint(false);
	}
}
