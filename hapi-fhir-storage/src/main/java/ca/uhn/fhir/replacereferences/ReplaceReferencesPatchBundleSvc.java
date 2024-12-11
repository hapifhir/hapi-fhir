package ca.uhn.fhir.replacereferences;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.dao.IFhirSystemDao;
import ca.uhn.fhir.model.primitive.IdDt;
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

import java.util.List;
import java.util.UUID;

import static ca.uhn.fhir.jpa.patch.FhirPatch.OPERATION_REPLACE;
import static ca.uhn.fhir.jpa.patch.FhirPatch.PARAMETER_OPERATION;
import static ca.uhn.fhir.jpa.patch.FhirPatch.PARAMETER_PATH;
import static ca.uhn.fhir.jpa.patch.FhirPatch.PARAMETER_TYPE;
import static ca.uhn.fhir.jpa.patch.FhirPatch.PARAMETER_VALUE;

public class ReplaceReferencesPatchBundleSvc {

	private final FhirContext myFhirContext;
	private final DaoRegistry myDaoRegistry;

	public ReplaceReferencesPatchBundleSvc(DaoRegistry theDaoRegistry) {
		myDaoRegistry = theDaoRegistry;
		myFhirContext = theDaoRegistry.getFhirContext();
	}

	public Bundle patchReferencingResources(ReplaceReferenceRequest theReplaceReferenceRequest, List<IdDt> theResourceIds, RequestDetails theRequestDetails) {
		Bundle patchBundle = buildPatchBundle(theReplaceReferenceRequest, theResourceIds, theRequestDetails);
		IFhirSystemDao<Bundle, Meta> systemDao = myDaoRegistry.getSystemDao();
		Bundle result = systemDao.transaction(theRequestDetails, patchBundle);
		// TODO KHS shouldn't transaction response bundles already have ids?
		result.setId(UUID.randomUUID().toString());
		return result;
	}

	private Bundle buildPatchBundle(
		ReplaceReferenceRequest theReplaceReferenceRequest,
		List<IdDt> theResourceIds,
		RequestDetails theRequestDetails) {
		BundleBuilder bundleBuilder = new BundleBuilder(myFhirContext);

		theResourceIds.forEach(referencingResourceId -> {
			IFhirResourceDao<?> dao = myDaoRegistry.getResourceDao(referencingResourceId.getResourceType());
			IBaseResource resource = dao.read(referencingResourceId, theRequestDetails);
			Parameters patchParams = buildPatchParams(theReplaceReferenceRequest, resource);
			IIdType resourceId = resource.getIdElement();
			bundleBuilder.addTransactionFhirPatchEntry(resourceId, patchParams);
		});
		return bundleBuilder.getBundleTyped();
	}

	private @Nonnull Parameters buildPatchParams(
		ReplaceReferenceRequest theReplaceReferenceRequest, IBaseResource referencingResource) {
		Parameters params = new Parameters();

		myFhirContext.newTerser().getAllResourceReferences(referencingResource).stream()
			.filter(refInfo -> matches(
				refInfo,
				theReplaceReferenceRequest.sourceId)) // We only care about references to our source resource
			.map(refInfo -> createReplaceReferencePatchOperation(
				referencingResource.fhirType() + "." + refInfo.getName(),
				new Reference(theReplaceReferenceRequest.targetId.getValueAsString())))
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
