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
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.dao.IFhirSystemDao;
import ca.uhn.fhir.jpa.dao.data.IResourceLinkDao;
import ca.uhn.fhir.jpa.dao.index.IdHelperService;
import ca.uhn.fhir.jpa.dao.tx.HapiTransactionService;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.util.BundleBuilder;
import ca.uhn.fhir.util.ResourceReferenceInfo;
import ca.uhn.fhir.util.StopLimitAccumulator;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.Meta;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.Type;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.stream.Stream;

import static ca.uhn.fhir.jpa.patch.FhirPatch.OPERATION_REPLACE;
import static ca.uhn.fhir.jpa.patch.FhirPatch.PARAMETER_OPERATION;
import static ca.uhn.fhir.jpa.patch.FhirPatch.PARAMETER_PATH;
import static ca.uhn.fhir.jpa.patch.FhirPatch.PARAMETER_TYPE;
import static ca.uhn.fhir.jpa.patch.FhirPatch.PARAMETER_VALUE;
import static ca.uhn.fhir.rest.server.provider.ProviderConstants.OPERATION_REPLACE_REFERENCES_OUTPUT_PARAM_OUTCOME;

public class ReplaceReferencesSvcImpl implements IReplaceReferencesSvc {
	private static final Logger ourLog = LoggerFactory.getLogger(ReplaceReferencesSvcImpl.class);
	private final FhirContext myFhirContext;
	private final DaoRegistry myDaoRegistry;
	private final HapiTransactionService myHapiTransactionService;
	private final IdHelperService myIdHelperService;
	private final IResourceLinkDao myResourceLinkDao;

	public ReplaceReferencesSvcImpl(
		FhirContext theFhirContext,
		DaoRegistry theDaoRegistry,
		HapiTransactionService theHapiTransactionService,
		IdHelperService theIdHelperService,
		IResourceLinkDao theResourceLinkDao) {
		myFhirContext = theFhirContext;
		myDaoRegistry = theDaoRegistry;
		myHapiTransactionService = theHapiTransactionService;
		myIdHelperService = theIdHelperService;
		myResourceLinkDao = theResourceLinkDao;
	}

	@Override
	public IBaseParameters replaceReferences(
		ReplaceReferenceRequest theReplaceReferenceRequest, RequestDetails theRequestDetails) {
		theReplaceReferenceRequest.validateOrThrowInvalidParameterException();

		if (theRequestDetails.isPreferAsync()) {
			return replaceReferencesPreferAsync(theReplaceReferenceRequest, theRequestDetails);
		} else {
			return replaceReferencesPreferSync(theReplaceReferenceRequest, theRequestDetails);
		}
	}

	@Override
	public Integer countResourcesReferencingResource(IIdType theResourceId, RequestDetails theRequestDetails) {
		return myHapiTransactionService.withRequest(theRequestDetails).execute(() -> {
			// FIXME KHS get partition from request
			JpaPid sourcePid =
				myIdHelperService.getPidOrThrowException(RequestPartitionId.allPartitions(), theResourceId);
			return myResourceLinkDao.countResourcesTargetingPid(sourcePid.getId());
		});
	}

	private IBaseParameters replaceReferencesPreferAsync(
		ReplaceReferenceRequest theReplaceReferenceRequest, RequestDetails theRequestDetails) {
		// FIXME KHS
		return null;
	}

	/**
	 * Try to perform the operation synchronously. However if there is more than a page of results, fall back to asynchronous operation
	 */
	@Nonnull
	private IBaseParameters replaceReferencesPreferSync(
		ReplaceReferenceRequest theReplaceReferenceRequest, RequestDetails theRequestDetails) {

		// TODO KHS get partition from request
		StopLimitAccumulator<JpaPid> accumulator = myHapiTransactionService
			.withRequest(theRequestDetails)
			.execute(() -> getAllPidsWithLimit(theReplaceReferenceRequest));

		if (accumulator.isTruncated()) {
			ourLog.warn("Too many results. Switching to asynchronous reference replacement.");
			return replaceReferencesPreferAsync(theReplaceReferenceRequest, theRequestDetails);
		}

		Bundle patchBundle = buildPatchBundle(theReplaceReferenceRequest, theRequestDetails, accumulator);

		IFhirSystemDao<Bundle, Meta> systemDao = myDaoRegistry.getSystemDao();
		Bundle result = systemDao.transaction(theRequestDetails, patchBundle);

		Parameters retval = new Parameters();
		retval.addParameter().setName(OPERATION_REPLACE_REFERENCES_OUTPUT_PARAM_OUTCOME).setResource(result);
		return retval;
	}

	private @NotNull StopLimitAccumulator<JpaPid> getAllPidsWithLimit(ReplaceReferenceRequest theReplaceReferenceRequest) {
		JpaPid sourcePid = myIdHelperService.getPidOrThrowException(
			RequestPartitionId.allPartitions(), theReplaceReferenceRequest.sourceId);

		Stream<JpaPid> pidStream = myResourceLinkDao
			.streamSourcePidsForTargetPid(sourcePid.getId())
			.map(JpaPid::fromId);
		StopLimitAccumulator<JpaPid> accumulator =
			StopLimitAccumulator.fromStreamAndLimit(pidStream, theReplaceReferenceRequest.batchSize);
		return accumulator;
	}

	private Bundle buildPatchBundle(ReplaceReferenceRequest theReplaceReferenceRequest, RequestDetails theRequestDetails, StopLimitAccumulator<JpaPid> accumulator) {
		BundleBuilder bundleBuilder = new BundleBuilder(myFhirContext);

		accumulator.getItemList().stream()
			.map(myIdHelperService::translatePidIdToForcedIdWithCache)
			.filter(Optional::isPresent)
			.map(Optional::get)
			.map(IdDt::new)
			.forEach(referencingResourceId -> {
				IFhirResourceDao<?> dao = getDao(referencingResourceId.getResourceType());
				IBaseResource resource = dao.read(referencingResourceId, theRequestDetails);
				Parameters patchParams = buildPatchParams(theReplaceReferenceRequest, resource);
				IIdType resourceId = resource.getIdElement();
				bundleBuilder.addTransactionFhirPatchEntry(resourceId, patchParams);
			});
		Bundle patchBundle = bundleBuilder.getBundleTyped();
		return patchBundle;
	}

	private @NotNull Parameters buildPatchParams(ReplaceReferenceRequest theReplaceReferenceRequest, IBaseResource referencingResource) {
		Parameters params = new Parameters();

		myFhirContext.newTerser().getAllResourceReferences(referencingResource).stream()
			.filter(refInfo -> matches(
				refInfo,
				theReplaceReferenceRequest
					.sourceId)) // We only care about references to our source resource
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

	private IFhirResourceDao<?> getDao(String theResourceName) {
		return myDaoRegistry.getResourceDao(theResourceName);
	}
}
