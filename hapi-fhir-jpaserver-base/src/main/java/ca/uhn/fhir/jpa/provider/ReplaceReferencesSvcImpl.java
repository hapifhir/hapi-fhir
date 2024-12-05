/*-
 * #%L
 * HAPI FHIR JPA Server
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
import static ca.uhn.fhir.rest.server.provider.ProviderConstants.PARAM_SOURCE_REFERENCE_ID;
import static ca.uhn.fhir.rest.server.provider.ProviderConstants.PARAM_TARGET_REFERENCE_ID;
import static software.amazon.awssdk.utils.StringUtils.isBlank;

public class ReplaceReferencesSvcImpl implements IReplaceReferencesSvc {

	private final FhirContext myFhirContext;
	private final DaoRegistry myDaoRegistry;

	public ReplaceReferencesSvcImpl(FhirContext theFhirContext, DaoRegistry theDaoRegistry) {
		myFhirContext = theFhirContext;
		myDaoRegistry = theDaoRegistry;
	}

	@Override
	public IBaseParameters replaceReferences(String theSourceRefId, String theTargetRefId, RequestDetails theRequest) {

		validateParameters(theSourceRefId, theTargetRefId);
		IIdType sourceRefId = new IdDt(theSourceRefId);
		IIdType targetRefId = new IdDt(theTargetRefId);

		//		todo jm: this could be problematic depending on referenceing object set size, however we are adding
		//			batch job option to handle that case as part of this feature
		List<? extends IBaseResource> referencingResources = findReferencingResourceIds(sourceRefId, theRequest);

		return replaceReferencesInTransaction(referencingResources, sourceRefId, targetRefId, theRequest);
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
						Msg.code(2588) + "No DAO registered for resource type: " + resourceType);
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

				addReferenceToMapIfForSource(
						theCurrentReferencedResourceId,
						theNewReferencedResourceId,
						referencingResource,
						refInfo,
						paramsMap);
			}
		}
		return paramsMap;
	}

	private void addReferenceToMapIfForSource(
			IIdType theCurrentReferencedResourceId,
			IIdType theNewReferencedResourceId,
			IBaseResource referencingResource,
			ResourceReferenceInfo refInfo,
			Map<String, Map<IIdType, Parameters>> paramsMap) {
		if (!refInfo.getResourceReference()
				.getReferenceElement()
				.toUnqualifiedVersionless()
				.getValueAsString()
				.equals(theCurrentReferencedResourceId
						.toUnqualifiedVersionless()
						.getValueAsString())) {

			// not a reference to the resource being replaced
			return;
		}

		Parameters.ParametersParameterComponent paramComponent = createReplaceReferencePatchOperation(
				referencingResource.fhirType() + "." + refInfo.getName(),
				new Reference(
						theNewReferencedResourceId.toUnqualifiedVersionless().getValueAsString()));

		paramsMap
				// preserve order, in case it could matter
				.computeIfAbsent(referencingResource.fhirType(), k -> new LinkedHashMap<>())
				.computeIfAbsent(referencingResource.getIdElement(), k -> new Parameters())
				.addParameter(paramComponent);
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
			IIdType theSourceRefIdParam, RequestDetails theRequest) {
		IFhirResourceDao<?> dao = getDao(theSourceRefIdParam.getResourceType());
		if (dao == null) {
			throw new InternalErrorException(
					Msg.code(2582) + "Couldn't obtain DAO for resource type" + theSourceRefIdParam.getResourceType());
		}

		SearchParameterMap parameterMap = new SearchParameterMap();
		parameterMap.add(PARAM_ID, new StringParam(theSourceRefIdParam.getValue()));
		parameterMap.addRevInclude(new Include("*"));
		return dao.search(parameterMap, theRequest).getAllResources();
	}

	private IFhirResourceDao<?> getDao(String theResourceName) {
		return myDaoRegistry.getResourceDao(theResourceName);
	}

	private void validateParameters(String theSourceRefIdParam, String theTargetRefIdParam) {
		if (isBlank(theSourceRefIdParam)) {
			throw new InvalidParameterException(
					Msg.code(2583) + "Parameter '" + PARAM_SOURCE_REFERENCE_ID + "' is blank");
		}

		if (isBlank(theTargetRefIdParam)) {
			throw new InvalidParameterException(
					Msg.code(2584) + "Parameter '" + PARAM_TARGET_REFERENCE_ID + "' is blank");
		}

		IIdType sourceId = new IdDt(theSourceRefIdParam);
		if (isBlank(sourceId.getResourceType())) {
			throw new InvalidParameterException(
					Msg.code(2585) + "'" + PARAM_SOURCE_REFERENCE_ID + "' must be a resource type qualified id");
		}

		IIdType targetId = new IdDt(theTargetRefIdParam);
		if (isBlank(targetId.getResourceType())) {
			throw new InvalidParameterException(
					Msg.code(2586) + "'" + PARAM_TARGET_REFERENCE_ID + "' must be a resource type qualified id");
		}

		if (!targetId.getResourceType().equals(sourceId.getResourceType())) {
			throw new InvalidParameterException(
					Msg.code(2587) + "Source and target id parameters must be for the same resource type");
		}
	}
}
