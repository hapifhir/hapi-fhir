package ca.uhn.fhir.batch2.jobs.replacereferences;

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IJobStepWorker;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.jobs.chunk.FhirIdJson;
import ca.uhn.fhir.batch2.jobs.chunk.FhirIdListWorkChunkJson;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.dao.IFhirSystemDao;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.util.BundleBuilder;
import ca.uhn.fhir.util.ResourceReferenceInfo;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.Meta;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.Type;

import java.util.UUID;

import static ca.uhn.fhir.jpa.patch.FhirPatch.OPERATION_REPLACE;
import static ca.uhn.fhir.jpa.patch.FhirPatch.PARAMETER_OPERATION;
import static ca.uhn.fhir.jpa.patch.FhirPatch.PARAMETER_PATH;
import static ca.uhn.fhir.jpa.patch.FhirPatch.PARAMETER_TYPE;
import static ca.uhn.fhir.jpa.patch.FhirPatch.PARAMETER_VALUE;

public class ReplaceReferenceUpdateStep
		implements IJobStepWorker<
				ReplaceReferencesJobParameters, FhirIdListWorkChunkJson, ReplaceReferencePatchOutcomeJson> {

	private FhirContext myFhirContext;
	private DaoRegistry myDaoRegistry;

	public ReplaceReferenceUpdateStep(FhirContext theFhirContext, DaoRegistry theDaoRegistry) {
		myFhirContext = theFhirContext;
		myDaoRegistry = theDaoRegistry;
	}

	@Nonnull
	@Override
	public RunOutcome run(
			@Nonnull
					StepExecutionDetails<ReplaceReferencesJobParameters, FhirIdListWorkChunkJson>
							theStepExecutionDetails,
			@Nonnull IJobDataSink<ReplaceReferencePatchOutcomeJson> theDataSink)
			throws JobExecutionFailedException {

		ReplaceReferencesJobParameters params = theStepExecutionDetails.getParameters();
		FhirIdListWorkChunkJson theFhirIds = theStepExecutionDetails.getData();

		SystemRequestDetails requestDetails = SystemRequestDetails.forRequestPartitionId(params.getPartitionId());
		Bundle patchBundle = buildPatchBundle(params, theFhirIds, requestDetails);
		IFhirSystemDao<Bundle, Meta> systemDao = myDaoRegistry.getSystemDao();
		Bundle result = systemDao.transaction(requestDetails, patchBundle);
		// TODO KHS shouldn't transaction response bundles have ids?
		result.setId(UUID.randomUUID().toString());

		ReplaceReferencePatchOutcomeJson data = new ReplaceReferencePatchOutcomeJson(myFhirContext, result);
		theDataSink.accept(data);

		return new RunOutcome(result.getEntry().size());
	}

	private Bundle buildPatchBundle(
			ReplaceReferencesJobParameters theParams,
			FhirIdListWorkChunkJson theFhirIds,
			RequestDetails theRequestDetails) {
		BundleBuilder bundleBuilder = new BundleBuilder(myFhirContext);

		theFhirIds.getFhirIds().stream().map(FhirIdJson::asIdDt).forEach(referencingResourceId -> {
			IFhirResourceDao<?> dao = myDaoRegistry.getResourceDao(referencingResourceId.getResourceType());
			IBaseResource resource = dao.read(referencingResourceId, theRequestDetails);
			Parameters patchParams = buildPatchParams(theParams, resource);
			IIdType resourceId = resource.getIdElement();
			bundleBuilder.addTransactionFhirPatchEntry(resourceId, patchParams);
		});
		return bundleBuilder.getBundleTyped();
	}

	private @Nonnull Parameters buildPatchParams(
			ReplaceReferencesJobParameters theParams, IBaseResource referencingResource) {
		Parameters params = new Parameters();

		myFhirContext.newTerser().getAllResourceReferences(referencingResource).stream()
				.filter(refInfo -> matches(
						refInfo,
						theParams.getSourceId().asIdDt())) // We only care about references to our source resource
				.map(refInfo -> createReplaceReferencePatchOperation(
						referencingResource.fhirType() + "." + refInfo.getName(),
						new Reference(theParams.getTargetId().toString())))
				.forEach(params::addParameter); // Add each operation to parameters
		return params;
	}

	private static boolean matches(ResourceReferenceInfo refInfo, IIdType theSourceId) {
		return refInfo.getResourceReference()
				.getReferenceElement()
				.toUnqualifiedVersionless()
				.getValueAsString()
				.equals(theSourceId.getValueAsString());
	}

	@Nonnull
	private Parameters.ParametersParameterComponent createReplaceReferencePatchOperation(
			String thePath, Type theValue) {

		Parameters.ParametersParameterComponent operation = new Parameters.ParametersParameterComponent();
		operation.setName(PARAMETER_OPERATION);
		operation.addPart().setName(PARAMETER_TYPE).setValue(new CodeType(OPERATION_REPLACE));
		operation.addPart().setName(PARAMETER_PATH).setValue(new StringType(thePath));
		operation.addPart().setName(PARAMETER_VALUE).setValue(theValue);
		return operation;
	}
}
