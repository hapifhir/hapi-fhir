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
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.dao.data.IResourceLinkDao;
import ca.uhn.fhir.jpa.dao.index.IdHelperService;
import ca.uhn.fhir.jpa.dao.tx.HapiTransactionService;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.PatchTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.util.ResourceReferenceInfo;
import ca.uhn.fhir.util.StopLimitAccumulator;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.stream.Stream;

import static ca.uhn.fhir.jpa.patch.FhirPatch.OPERATION_REPLACE;
import static ca.uhn.fhir.jpa.patch.FhirPatch.PARAMETER_OPERATION;
import static ca.uhn.fhir.jpa.patch.FhirPatch.PARAMETER_PATH;
import static ca.uhn.fhir.jpa.patch.FhirPatch.PARAMETER_TYPE;
import static ca.uhn.fhir.jpa.patch.FhirPatch.PARAMETER_VALUE;

public class ReplaceReferencesSvcImpl implements IReplaceReferencesSvc {
	private static final Logger ourLog = LoggerFactory.getLogger(ReplaceReferencesSvcImpl.class);
	private final FhirContext myFhirContext;
	private final DaoRegistry myDaoRegistry;
	private final HapiTransactionService myHapiTransactionService;
	private final IdHelperService myIdHelperService;
	private final IResourceLinkDao myResourceLinkDao;

	public ReplaceReferencesSvcImpl(FhirContext theFhirContext, DaoRegistry theDaoRegistry, HapiTransactionService theHapiTransactionService, IdHelperService theIdHelperService, IResourceLinkDao theResourceLinkDao) {
		myFhirContext = theFhirContext;
		myDaoRegistry = theDaoRegistry;
		myHapiTransactionService = theHapiTransactionService;
		myIdHelperService = theIdHelperService;
		myResourceLinkDao = theResourceLinkDao;
	}

	@Override
	public IBaseParameters replaceReferences(ReplaceReferenceRequest theReplaceReferenceRequest, RequestDetails theRequestDetails) {
		theReplaceReferenceRequest.validateOrThrowInvalidParameterException();

		if (theRequestDetails.isPreferAsync()) {
			return replaceReferencesPreferAsync(theReplaceReferenceRequest, theRequestDetails);
		} else {
			return replaceReferencesPreferSync(theReplaceReferenceRequest, theRequestDetails);
		}
	}

	private IBaseParameters replaceReferencesPreferAsync(ReplaceReferenceRequest theReplaceReferenceRequest, RequestDetails theRequestDetails) {
		// FIXME KHS
		return null;
	}

	/**
	 * Try to perform the operation synchronously. However if there is more than a page of results, fall back to asynchronous operation
	 */
	private @NotNull IBaseParameters replaceReferencesPreferSync(ReplaceReferenceRequest theReplaceReferenceRequest, RequestDetails theRequestDetails) {

		//		todo jm: this could be problematic depending on referenceing object set size, however we are adding
		//			batch job option to handle that case as part of this feature
		IFhirResourceDao<?> dao = getDao(theReplaceReferenceRequest.sourceId.getResourceType());
		if (dao == null) {
			throw new InternalErrorException(
				Msg.code(2582) + "Couldn't obtain DAO for resource type" + theReplaceReferenceRequest.sourceId.getResourceType());
		}

		return myHapiTransactionService.withRequest(theRequestDetails).execute(
			() -> performReplaceInTransaction(theReplaceReferenceRequest, theRequestDetails, dao));
	}

	private @Nullable IBaseParameters performReplaceInTransaction(ReplaceReferenceRequest theReplaceReferenceRequest, RequestDetails theRequestDetails, IFhirResourceDao<?> dao) {
		// FIXME KHS get partition from request
		JpaPid sourcePid = myIdHelperService.getPidOrThrowException(RequestPartitionId.allPartitions(), theReplaceReferenceRequest.sourceId);

		Stream<JpaPid> pidStream = myResourceLinkDao.streamSourcePidsForTargetPid(sourcePid.getId()).map(JpaPid::fromId);
		StopLimitAccumulator<JpaPid> accumulator = StopLimitAccumulator.fromStreamAndLimit(pidStream, theReplaceReferenceRequest.batchSize);

		if (accumulator.isTruncated()) {
			ourLog.info("Too many results. Switching to asynchronous reference replacement.");
			return replaceReferencesPreferAsync(theReplaceReferenceRequest, theRequestDetails);
		}

		Stream<IBaseResource> referencingResourceStream = accumulator.getItemList().stream().map(myIdHelperService::translatePidIdToForcedIdWithCache)
			.filter(Optional::isPresent)
			.map(Optional::get)
			.map(IdDt::new)
			.map(id -> getDao(id.getResourceType()).read(id, theRequestDetails));

		return replaceReferencesInTransaction(referencingResourceStream, theReplaceReferenceRequest, theRequestDetails);
	}

	private IBaseParameters replaceReferencesInTransaction(
		Stream<IBaseResource> theReferencingResourceStream,
		ReplaceReferenceRequest theReplaceReferenceRequest,
		RequestDetails theRequestDetails) {

		Parameters resultParams = new Parameters();

		theReferencingResourceStream.forEach(referencingResource -> {
			Parameters params = new Parameters();

			String fhirType = referencingResource.fhirType();
			myFhirContext.newTerser().getAllResourceReferences(referencingResource).stream()
				.filter(refInfo -> matches(refInfo, theReplaceReferenceRequest.sourceId)) // We only care about references to our source resource
				.map(refInfo -> createReplaceReferencePatchOperation(
					fhirType + "." + refInfo.getName(),
					new Reference(theReplaceReferenceRequest.targetId.getValueAsString())))
				.forEach(params::addParameter); // Add each operation to parameters

			IFhirResourceDao<?> resDao = myDaoRegistry.getResourceDao(fhirType);

			IIdType resourceId = referencingResource.getIdElement();

			MethodOutcome result =
				resDao.patch(resourceId, null, PatchTypeEnum.FHIR_PATCH_JSON, null, params, theRequestDetails);

			resultParams.addParameter().setResource((Resource) result.getOperationOutcome());
		});

		return resultParams;
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
