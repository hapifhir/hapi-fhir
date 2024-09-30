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
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.PatchTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

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

public class RehomeProvider {

	public static final int BATCH_REFERENCE_COUNT_THRESHOLD = 100;

	@Autowired
	private FhirContext myFhirContext;

	@Autowired
	private DaoRegistry myDaoRegistry;

	int myBatchReferenceCountThreshold = BATCH_REFERENCE_COUNT_THRESHOLD;

	@Description(
			value =
					"This operation repoints referenced resources to a new target resource instance of the same previously pointed type.",
			shortDefinition = "Repoints referencing resources to another resources instance")
	@Operation(name = ProviderConstants.OPERATION_REHOME, global = true)
	public IBaseParameters rehome(
			@IdParam IIdType theCurrentTargetIdParam,
			@IdParam IIdType theNewTargetIdParam,
			RequestDetails theRequest) {

		validate(theCurrentTargetIdParam, theNewTargetIdParam, theRequest);

		List<? extends IBaseResource> referencingResources =
				findReferencingResourceIds(theCurrentTargetIdParam, theRequest);

		// operation is performed online or batched depending on the number of references to patch
		return referencingResources.size() > myBatchReferenceCountThreshold
				? rehomeInTransaction(referencingResources, theCurrentTargetIdParam, theNewTargetIdParam, theRequest)
				: rehomingBatch(referencingResources, theCurrentTargetIdParam, theNewTargetIdParam, theRequest);
	}

	private IBaseParameters rehomeInTransaction(
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
				throw new InternalErrorException("No DAO registered for resource type: " + resourceType);
			}

			// patch each resource of resourceType
			patchResourceTypeResources(mapEntry, resDao, resultParams, theRequest);
		}

		return resultParams;
	}

	private void patchResourceTypeResources(Map.Entry<String, Map<IIdType, Parameters>> mapEntry,
											IFhirResourceDao<?> resDao,
											Parameters resultParams,
											RequestDetails theRequest) {

		for (Map.Entry<IIdType, Parameters> idParamMapEntry :
				mapEntry.getValue().entrySet()) {
			IIdType resourceId = idParamMapEntry.getKey();
			Parameters parameters = idParamMapEntry.getValue();

			// fixme jm: make sure it executes in hapiTxService (with retry)
			MethodOutcome result =
					resDao.patch(resourceId, null, PatchTypeEnum.FHIR_PATCH_JSON, null, parameters, theRequest);

			resultParams.addParameter().setResource((Resource) result.getOperationOutcome());
		}
	}

	private IBaseParameters rehomingBatch(
			List<? extends IBaseResource> theReferencingResources,
			IIdType theCurrentTargetId,
			IIdType theNewTargetId,
			RequestDetails theRequest) {
		//		fixme jm: implement
		throw new IllegalStateException("Not yet implemented");
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
	private Parameters.ParametersParameterComponent createReplaceReferencePatchOperation(String thePath, Type theValue) {
		Parameters.ParametersParameterComponent operation =
				new Parameters.ParametersParameterComponent();
		operation.setName(PARAMETER_OPERATION);
		operation.addPart().setName(PARAMETER_TYPE).setValue(new CodeType(OPERATION_REPLACE));

		operation.addPart().setName(PARAMETER_PATH).setValue(new StringType(thePath));

		operation.addPart().setName(PARAMETER_VALUE).setValue(theValue);

		return operation;
	}

	private List<? extends IBaseResource> findReferencingResourceIds(IIdType theCurrentTargetIdParam,
																	 RequestDetails theRequest) {
		IFhirResourceDao<?> dao = getDao(theRequest.getResourceName());
		if (dao == null) {
			// fixme jm: msg code
			throw new InternalErrorException(Msg.code(0) +
				"Couldn't obtain DAO for resource type" + theRequest.getResourceName());
		}

		SearchParameterMap parameterMap = new SearchParameterMap();
		parameterMap.add(PARAM_ID, new StringParam(theCurrentTargetIdParam.getValue()));
		parameterMap.addRevInclude(new Include("*"));
		return dao.search(parameterMap, theRequest).getAllResources();
	}


	private IFhirResourceDao<?> getDao(String theResourceName) {
		return myDaoRegistry.getResourceDao(theResourceName);
	}

	private void validate(IIdType theCurrentTargetIdParam, IIdType theNewTargetIdParam, RequestDetails theRequest) {
		if (theCurrentTargetIdParam == null) {
			throw new InvalidParameterException("Parameter 'theCurrentTargetIdParam' is null");
		}

		if (theNewTargetIdParam == null) {
			throw new InvalidParameterException("Parameter 'theNewTargetIdParam' is null");
		}

		if (isBlank(theRequest.getResourceName())) {
			throw new InvalidParameterException("RequestDetails.resourceName must must be provided");
		}
	}

}
