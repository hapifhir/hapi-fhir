package ca.uhn.fhir.jpa.provider;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.PatchTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.util.ResourceReferenceInfo;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.Resource;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.Type;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static ca.uhn.fhir.jpa.patch.FhirPatch.OPERATION_REPLACE;
import static ca.uhn.fhir.jpa.patch.FhirPatch.PARAMETER_OPERATION;
import static ca.uhn.fhir.jpa.patch.FhirPatch.PARAMETER_PATH;
import static ca.uhn.fhir.jpa.patch.FhirPatch.PARAMETER_TYPE;
import static ca.uhn.fhir.jpa.patch.FhirPatch.PARAMETER_VALUE;
import static ca.uhn.fhir.rest.api.Constants.PARAM_ID;
import static software.amazon.awssdk.utils.StringUtils.isBlank;

public class ReplaceReferencesSvcImpl implements IReplaceReferencesSvc {

	private final FhirContext myFhirContext;
	private final DaoRegistry myDaoRegistry;

	public ReplaceReferencesSvcImpl(FhirContext theFhirContext, DaoRegistry theDaoRegistry) {
		myFhirContext = theFhirContext;
		myDaoRegistry = theDaoRegistry;
	}

	@Override
	public IBaseParameters replaceReferences(
		IIdType theCurrentTargetId, String theNewTargetId, RequestDetails theRequest) {

		validateParameters(theCurrentTargetId, theNewTargetId, theRequest);
		IIdType newTargetId = new IdDt(theNewTargetId);

		List<? extends IBaseResource> referencingResources = findReferencingResourceIds(theCurrentTargetId, theRequest);

		return replaceReferencesInTransaction(referencingResources, theCurrentTargetId, newTargetId, theRequest);
	}

	private IBaseParameters replaceReferencesInTransaction(
			List<? extends IBaseResource> theReferencingResources,
			IIdType theCurrentTargetId,
			IIdType theNewTargetId,
			RequestDetails theRequest) {

		Parameters resultParams = new Parameters();
		// map resourceType -> map resourceId -> patch Parameters
		Map<String, Map<IIdType, Parameters>> parametersMap =
				buildPatchParameterMap(theReferencingResources, theCurrentTargetId, theNewTargetId);

		for (Map.Entry<String, Map<IIdType, Parameters>> mapEntry : parametersMap.entrySet()) {
			String resourceType = mapEntry.getKey();
			IFhirResourceDao<?> resDao = myDaoRegistry.getResourceDao(resourceType);
			if (resDao == null) {
				throw new InternalErrorException(
						Msg.code(2587) + "No DAO registered for resource type: " + resourceType);
			}

			// patch each resource of resourceType
			patchResourceTypeResources(mapEntry, resDao, resultParams, theRequest);
		}

		return resultParams;
	}

	private void patchResourceTypeResources(
			Map.Entry<String, Map<IIdType, Parameters>> mapEntry,
			IFhirResourceDao<?> resDao,
			Parameters resultParams,
			RequestDetails theRequest) {

		for (Map.Entry<IIdType, Parameters> idParamMapEntry :
				mapEntry.getValue().entrySet()) {
			IIdType resourceId = idParamMapEntry.getKey();
			Parameters parameters = idParamMapEntry.getValue();

			MethodOutcome result =
					resDao.patch(resourceId, null, PatchTypeEnum.FHIR_PATCH_JSON, null, parameters, theRequest);

			resultParams.addParameter().setResource((Resource) result.getOperationOutcome());
		}
	}

	private Map<String, Map<IIdType, Parameters>> buildPatchParameterMap(
			List<? extends IBaseResource> theReferencingResources,
			IIdType theCurrentReferencedResourceId,
			IIdType theNewReferencedResourceId) {
		Map<String, Map<IIdType, Parameters>> paramsMap = new HashMap<>();

		for (IBaseResource referencingResource : theReferencingResources) {
			// resource can have more than one reference to the same target resource
			for (ResourceReferenceInfo refInfo :
					myFhirContext.newTerser().getAllResourceReferences(referencingResource)) {
				// is this a reference to the target resource?
				if (refInfo.getResourceReference()
						.getReferenceElement()
						.equals(theCurrentReferencedResourceId.toUnqualifiedVersionless())) {

					Parameters.ParametersParameterComponent paramComponent = createReplaceReferencePatchOperation(
							referencingResource.fhirType() + "." + refInfo.getName(),
							new Reference(theNewReferencedResourceId
									.toUnqualifiedVersionless()
									.getValueAsString()));

					paramsMap
							// preserve order, in case it could matter
							.computeIfAbsent(referencingResource.fhirType(), k -> new LinkedHashMap<>())
							.computeIfAbsent(referencingResource.getIdElement(), k -> new Parameters())
							.addParameter(paramComponent);
				}
			}
		}
		return paramsMap;
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

	private List<? extends IBaseResource> findReferencingResourceIds(
			IIdType theCurrentTargetIdParam, RequestDetails theRequest) {

		IFhirResourceDao<?> dao = getDao(theRequest.getResourceName());
		if (dao == null) {
			throw new InternalErrorException(
					Msg.code(2582) + "Couldn't obtain DAO for resource type" + theRequest.getResourceName());
		}

		SearchParameterMap parameterMap = new SearchParameterMap();
		parameterMap.add(PARAM_ID, new StringParam(theCurrentTargetIdParam.getValue()));
		parameterMap.addRevInclude(new Include("*"));
		return dao.search(parameterMap, theRequest).getAllResources();
	}

	private IFhirResourceDao<?> getDao(String theResourceName) {
		return myDaoRegistry.getResourceDao(theResourceName);
	}

	private void validateParameters(IIdType theCurrentTargetIdParam, String theNewTargetIdParam, RequestDetails theRequest) {
		if (theCurrentTargetIdParam == null) {
			throw new InvalidParameterException(Msg.code(2583) + "Parameter 'theCurrentTargetIdParam' is null");
		}

		if (isBlank(theNewTargetIdParam)) {
			throw new InvalidParameterException(Msg.code(2584) + "Parameter 'theNewTargetIdParam' is blank");
		}

		IIdType targetId = new IdDt(theNewTargetIdParam);
		if (isBlank(targetId.getResourceType())) {
			throw new InvalidParameterException(
					Msg.code(2585) + "New reference id parameter must be a resource type qualified id");
		}
		if (!targetId.getResourceType().equals(theCurrentTargetIdParam.getResourceType())) {
			throw new InvalidParameterException(
					Msg.code(2586) + "Current and new references must be for the same resource type");
		}

		if (isBlank(theRequest.getResourceName())) {
			throw new InvalidParameterException(Msg.code(2587) + "RequestDetails.resourceName must must be provided");
		}
	}
}
