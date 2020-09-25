package ca.uhn.fhir.empi.provider;

/*-
 * #%L
 * HAPI FHIR - Enterprise Master Patient Index
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.empi.api.EmpiLinkJson;
import ca.uhn.fhir.empi.api.IEmpiControllerSvc;
import ca.uhn.fhir.empi.api.IEmpiExpungeSvc;
import ca.uhn.fhir.empi.api.IEmpiMatchFinderSvc;
import ca.uhn.fhir.empi.api.IEmpiSubmitSvc;
import ca.uhn.fhir.empi.model.EmpiTransactionContext;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.util.ParametersUtil;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.DecimalType;
import org.hl7.fhir.dstu3.model.InstantType;
import org.hl7.fhir.dstu3.model.Parameters;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.dstu3.model.Person;
import org.hl7.fhir.dstu3.model.Practitioner;
import org.hl7.fhir.dstu3.model.Resource;
import org.hl7.fhir.dstu3.model.StringType;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.Collection;
import java.util.UUID;
import java.util.stream.Stream;

public class EmpiProviderDstu3 extends BaseEmpiProvider {
	private final IEmpiControllerSvc myEmpiControllerSvc;
	private final IEmpiMatchFinderSvc myEmpiMatchFinderSvc;
	private final IEmpiExpungeSvc myEmpiResetSvc;
	private final IEmpiSubmitSvc myEmpiBatchSvc;

	/**
	 * Constructor
	 *
	 * Note that this is not a spring bean. Any necessary injections should
	 * happen in the constructor
	 */
	public EmpiProviderDstu3(FhirContext theFhirContext, IEmpiControllerSvc theEmpiControllerSvc, IEmpiMatchFinderSvc theEmpiMatchFinderSvc, IEmpiExpungeSvc theEmpiResetSvc, IEmpiSubmitSvc theEmpiBatchSvc) {
		super(theFhirContext);
		myEmpiControllerSvc = theEmpiControllerSvc;
		myEmpiMatchFinderSvc = theEmpiMatchFinderSvc;
		myEmpiResetSvc = theEmpiResetSvc;
		myEmpiBatchSvc = theEmpiBatchSvc;
	}

	@Operation(name = ProviderConstants.EMPI_MATCH, type = Patient.class)
	public Bundle match(@OperationParam(name=ProviderConstants.EMPI_MATCH_RESOURCE, min = 1, max = 1) Patient thePatient) {
		if (thePatient == null) {
			throw new InvalidRequestException("resource may not be null");
		}
		Collection<IAnyResource> matches = myEmpiMatchFinderSvc.findMatches("Patient", thePatient);

		Bundle retVal = new Bundle();
		retVal.setType(Bundle.BundleType.SEARCHSET);
		retVal.setId(UUID.randomUUID().toString());
		retVal.getMeta().setLastUpdatedElement(InstantType.now());

		for (IAnyResource next : matches) {
			retVal.addEntry().setResource((Resource) next);
		}

		return retVal;
	}

	@Operation(name = ProviderConstants.EMPI_MERGE_PERSONS, type = Person.class)
	public Person mergePerson(@OperationParam(name=ProviderConstants.EMPI_MERGE_PERSONS_FROM_PERSON_ID, min = 1, max = 1) StringType theFromPersonId,
									  @OperationParam(name=ProviderConstants.EMPI_MERGE_PERSONS_TO_PERSON_ID, min = 1, max = 1) StringType theToPersonId,
									  RequestDetails theRequestDetails) {
		validateMergeParameters(theFromPersonId, theToPersonId);

		return (Person) myEmpiControllerSvc.mergePersons(theFromPersonId.getValue(), theToPersonId.getValue(), createEmpiContext(theRequestDetails, EmpiTransactionContext.OperationType.MERGE_PERSONS));
	}

	@Operation(name = ProviderConstants.EMPI_UPDATE_LINK, type = Person.class)
	public Person updateLink(@OperationParam(name=ProviderConstants.EMPI_UPDATE_LINK_PERSON_ID, min = 1, max = 1) StringType thePersonId,
																  @OperationParam(name=ProviderConstants.EMPI_UPDATE_LINK_TARGET_ID, min = 1, max = 1) StringType theTargetId,
																  @OperationParam(name=ProviderConstants.EMPI_UPDATE_LINK_MATCH_RESULT, min = 1, max = 1) StringType theMatchResult,
																  ServletRequestDetails theRequestDetails) {

		validateUpdateLinkParameters(thePersonId, theTargetId, theMatchResult);

		return (Person) myEmpiControllerSvc.updateLink(thePersonId.getValue(), theTargetId.getValue(), theMatchResult.getValue(), createEmpiContext(theRequestDetails, EmpiTransactionContext.OperationType.UPDATE_LINK));
	}

	@Operation(name = ProviderConstants.EMPI_QUERY_LINKS)
	public Parameters queryLinks(@OperationParam(name=ProviderConstants.EMPI_QUERY_LINKS_PERSON_ID, min = 0, max = 1) StringType thePersonId,
										  @OperationParam(name=ProviderConstants.EMPI_QUERY_LINKS_TARGET_ID, min = 0, max = 1) StringType theTargetId,
										  @OperationParam(name=ProviderConstants.EMPI_QUERY_LINKS_MATCH_RESULT, min = 0, max = 1) StringType theMatchResult,
										  @OperationParam(name=ProviderConstants.EMPI_QUERY_LINKS_MATCH_RESULT, min = 0, max = 1) StringType theLinkSource,
										  ServletRequestDetails theRequestDetails) {

		Stream<EmpiLinkJson> empiLinkJson = myEmpiControllerSvc.queryLinks(extractStringOrNull(thePersonId), extractStringOrNull(theTargetId), extractStringOrNull(theMatchResult), extractStringOrNull(theLinkSource), createEmpiContext(theRequestDetails, EmpiTransactionContext.OperationType.QUERY_LINKS));
		return (Parameters) parametersFromEmpiLinks(empiLinkJson, true);
	}

	@Operation(name = ProviderConstants.EMPI_DUPLICATE_PERSONS)
	public Parameters getDuplicatePersons(ServletRequestDetails theRequestDetails) {
		Stream<EmpiLinkJson> possibleDuplicates = myEmpiControllerSvc.getDuplicatePersons(createEmpiContext(theRequestDetails, EmpiTransactionContext.OperationType.QUERY_LINKS));
		return (Parameters) parametersFromEmpiLinks(possibleDuplicates, false);
	}

	@Operation(name = ProviderConstants.EMPI_NOT_DUPLICATE)
	// TODO KHS can this return void?
	public Parameters notDuplicate(@OperationParam(name=ProviderConstants.EMPI_QUERY_LINKS_PERSON_ID, min = 1, max = 1) StringType thePersonId,
																		  @OperationParam(name=ProviderConstants.EMPI_QUERY_LINKS_TARGET_ID, min = 1, max = 1) StringType theTargetId,
																		  ServletRequestDetails theRequestDetails) {

		validateNotDuplicateParameters(thePersonId, theTargetId);
		myEmpiControllerSvc.notDuplicatePerson(thePersonId.getValue(), theTargetId.getValue(), createEmpiContext(theRequestDetails, EmpiTransactionContext.OperationType.NOT_DUPLICATE));

		Parameters retval = (Parameters) ParametersUtil.newInstance(myFhirContext);
		ParametersUtil.addParameterToParametersBoolean(myFhirContext, retval, "success", true);
		return retval;
	}

	@Operation(name = ProviderConstants.OPERATION_EMPI_SUBMIT, idempotent = false, returnParameters = {
		@OperationParam(name = ProviderConstants.OPERATION_EMPI_BATCH_RUN_OUT_PARAM_SUBMIT_COUNT, type= DecimalType.class)
	})
	public Parameters empiBatchOnAllTargets(
		@OperationParam(name= ProviderConstants.EMPI_BATCH_RUN_CRITERIA,min = 0 , max = 1) StringType theCriteria,
		ServletRequestDetails theRequestDetails) {
		String criteria = convertCriteriaToString(theCriteria);
		long submittedCount  = myEmpiBatchSvc.runEmpiOnAllTargetTypes(criteria);
		return buildEmpiOutParametersWithCount(submittedCount);
	}

	private String convertCriteriaToString(StringType theCriteria) {
		return theCriteria == null ? null : theCriteria.getValueAsString();
	}

	@Operation(name = ProviderConstants.EMPI_CLEAR, returnParameters = {
		@OperationParam(name = ProviderConstants.OPERATION_EMPI_BATCH_RUN_OUT_PARAM_SUBMIT_COUNT, type= DecimalType.class)
	})
	public Parameters clearEmpiLinks(@OperationParam(name=ProviderConstants.EMPI_CLEAR_TARGET_TYPE, min = 0, max = 1) StringType theTargetType) {
		long resetCount;
		if (theTargetType == null || StringUtils.isBlank(theTargetType.getValue())) {
			resetCount = myEmpiResetSvc.removeAllEmpiLinks();
		} else {
			resetCount = myEmpiResetSvc.expungeAllEmpiLinksOfTargetType(theTargetType.getValueNotNull());
		}
		Parameters parameters = new Parameters();
		parameters.addParameter().setName(ProviderConstants.OPERATION_EMPI_CLEAR_OUT_PARAM_DELETED_COUNT)
			.setValue(new DecimalType(resetCount));
		return parameters;
	}

	@Operation(name = ProviderConstants.OPERATION_EMPI_SUBMIT, idempotent = false, type = Patient.class, returnParameters = {
		@OperationParam(name = ProviderConstants.OPERATION_EMPI_BATCH_RUN_OUT_PARAM_SUBMIT_COUNT, type = DecimalType.class)
	})
	public Parameters empiBatchPatientInstance(
		@IdParam IIdType theIdParam,
		RequestDetails theRequest) {
		long submittedCount = myEmpiBatchSvc.runEmpiOnTarget(theIdParam);
		return buildEmpiOutParametersWithCount(submittedCount);
	}

	@Operation(name = ProviderConstants.OPERATION_EMPI_SUBMIT, idempotent = false, type = Patient.class, returnParameters = {
		@OperationParam(name = ProviderConstants.OPERATION_EMPI_BATCH_RUN_OUT_PARAM_SUBMIT_COUNT, type = DecimalType.class)
	})
	public Parameters empiBatchPatientType(
		@OperationParam(name = ProviderConstants.EMPI_BATCH_RUN_CRITERIA) StringType theCriteria,
		RequestDetails theRequest) {
		String criteria = convertCriteriaToString(theCriteria);
		long submittedCount = myEmpiBatchSvc.runEmpiOnPatientType(criteria);
		return buildEmpiOutParametersWithCount(submittedCount);
	}

	@Operation(name = ProviderConstants.OPERATION_EMPI_SUBMIT, idempotent = false, type = Practitioner.class, returnParameters = {
		@OperationParam(name = ProviderConstants.OPERATION_EMPI_BATCH_RUN_OUT_PARAM_SUBMIT_COUNT, type = DecimalType.class)
	})
	public Parameters empiBatchPractitionerInstance(
		@IdParam IIdType theIdParam,
		RequestDetails theRequest) {
		long submittedCount = myEmpiBatchSvc.runEmpiOnTarget(theIdParam);
		return buildEmpiOutParametersWithCount(submittedCount);
	}

	@Operation(name = ProviderConstants.OPERATION_EMPI_SUBMIT, idempotent = false, type = Practitioner.class, returnParameters = {
		@OperationParam(name = ProviderConstants.OPERATION_EMPI_BATCH_RUN_OUT_PARAM_SUBMIT_COUNT, type = DecimalType.class)
	})
	public Parameters empiBatchPractitionerType(
		@OperationParam(name = ProviderConstants.EMPI_BATCH_RUN_CRITERIA) StringType theCriteria,
		RequestDetails theRequest) {
		String criteria = convertCriteriaToString(theCriteria);
		long submittedCount = myEmpiBatchSvc.runEmpiOnPractitionerType(criteria);
		return buildEmpiOutParametersWithCount(submittedCount);
	}

	/**
	 * Helper function to build the out-parameters for all batch EMPI operations.
	 */
	private Parameters buildEmpiOutParametersWithCount(long theCount) {
		Parameters parameters = new Parameters();
		parameters.addParameter()
			.setName(ProviderConstants.OPERATION_EMPI_BATCH_RUN_OUT_PARAM_SUBMIT_COUNT)
			.setValue(new DecimalType(theCount));
		return parameters;
	}
}
