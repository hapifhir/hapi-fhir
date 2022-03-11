package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoPatient;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.model.valueset.BundleTypeEnum;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.annotation.Sort;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.StringAndListParam;
import ca.uhn.fhir.rest.param.StringOrListParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenOrListParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import org.hl7.fhir.r4.model.Coverage;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.UnsignedIntType;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

public class BaseJpaResourceProviderPatientR4 extends JpaResourceProviderR4<Patient> {

	@Autowired
	private MemberMatcherR4Helper myMemberMatcherR4Helper;


	/**
	 * Patient/123/$everything
	 */
	@Operation(name = JpaConstants.OPERATION_EVERYTHING, idempotent = true, bundleType = BundleTypeEnum.SEARCHSET)
	public IBundleProvider patientInstanceEverything(

		javax.servlet.http.HttpServletRequest theServletRequest,

		@IdParam
			IdType theId,

		@Description(formalDefinition = "Results from this method are returned across multiple pages. This parameter controls the size of those pages.")
		@OperationParam(name = Constants.PARAM_COUNT)
			UnsignedIntType theCount,

		@Description(formalDefinition="Results from this method are returned across multiple pages. This parameter controls the offset when fetching a page.")
		@OperationParam(name = Constants.PARAM_OFFSET)
			UnsignedIntType theOffset,

		@Description(shortDefinition = "Only return resources which were last updated as specified by the given range")
		@OperationParam(name = Constants.PARAM_LASTUPDATED, min = 0, max = 1)
			DateRangeParam theLastUpdated,

		@Description(shortDefinition = "Filter the resources to return only resources matching the given _content filter (note that this filter is applied only to results which link to the given patient, not to the patient itself or to supporting resources linked to by the matched resources)")
		@OperationParam(name = Constants.PARAM_CONTENT, min = 0, max = OperationParam.MAX_UNLIMITED)
			List<StringType> theContent,

		@Description(shortDefinition = "Filter the resources to return only resources matching the given _text filter (note that this filter is applied only to results which link to the given patient, not to the patient itself or to supporting resources linked to by the matched resources)")
		@OperationParam(name = Constants.PARAM_TEXT, min = 0, max = OperationParam.MAX_UNLIMITED)
			List<StringType> theNarrative,

		@Description(shortDefinition = "Filter the resources to return only resources matching the given _filter filter (note that this filter is applied only to results which link to the given patient, not to the patient itself or to supporting resources linked to by the matched resources)")
		@OperationParam(name = Constants.PARAM_FILTER, min = 0, max = OperationParam.MAX_UNLIMITED)
			List<StringType> theFilter,

		@Sort
			SortSpec theSortSpec,

		RequestDetails theRequestDetails
	) {

		startRequest(theServletRequest);
		try {
			return ((IFhirResourceDaoPatient<Patient>) getDao()).patientInstanceEverything(theServletRequest, theId, theCount, theOffset, theLastUpdated, theSortSpec, toStringAndList(theContent), toStringAndList(theNarrative), toStringAndList(theFilter), theRequestDetails);
		} finally {
			endRequest(theServletRequest);
		}
	}

	/**
	 * /Patient/$everything
	 */
	@Operation(name = JpaConstants.OPERATION_EVERYTHING, idempotent = true, bundleType = BundleTypeEnum.SEARCHSET)
	public IBundleProvider patientTypeEverything(

		javax.servlet.http.HttpServletRequest theServletRequest,

		@Description(formalDefinition = "Results from this method are returned across multiple pages. This parameter controls the size of those pages.")
		@OperationParam(name = Constants.PARAM_COUNT)
			UnsignedIntType theCount,

		@Description(formalDefinition="Results from this method are returned across multiple pages. This parameter controls the offset when fetching a page.")
		@OperationParam(name = Constants.PARAM_OFFSET)
			UnsignedIntType theOffset,

		@Description(shortDefinition = "Only return resources which were last updated as specified by the given range")
		@OperationParam(name = Constants.PARAM_LASTUPDATED, min = 0, max = 1)
			DateRangeParam theLastUpdated,

		@Description(shortDefinition = "Filter the resources to return only resources matching the given _content filter (note that this filter is applied only to results which link to the given patient, not to the patient itself or to supporting resources linked to by the matched resources)")
		@OperationParam(name = Constants.PARAM_CONTENT, min = 0, max = OperationParam.MAX_UNLIMITED)
			List<StringType> theContent,

		@Description(shortDefinition = "Filter the resources to return only resources matching the given _text filter (note that this filter is applied only to results which link to the given patient, not to the patient itself or to supporting resources linked to by the matched resources)")
		@OperationParam(name = Constants.PARAM_TEXT, min = 0, max = OperationParam.MAX_UNLIMITED)
			List<StringType> theNarrative,

		@Description(shortDefinition = "Filter the resources to return only resources matching the given _filter filter (note that this filter is applied only to results which link to the given patient, not to the patient itself or to supporting resources linked to by the matched resources)")
		@OperationParam(name = Constants.PARAM_FILTER, min = 0, max = OperationParam.MAX_UNLIMITED)
			List<StringType> theFilter,

		@Description(shortDefinition = "Filter the resources to return based on the patient ids provided.")
		@OperationParam(name = Constants.PARAM_ID, min = 0, max = OperationParam.MAX_UNLIMITED)
			List<IdType> theId,

		@Sort
			SortSpec theSortSpec,

		RequestDetails theRequestDetails
	) {

		startRequest(theServletRequest);
		try {
			return ((IFhirResourceDaoPatient<Patient>) getDao()).patientTypeEverything(theServletRequest, theCount, theOffset, theLastUpdated, theSortSpec, toStringAndList(theContent), toStringAndList(theNarrative), toStringAndList(theFilter), theRequestDetails, toFlattenedPatientIdTokenParamList(theId));
		} finally {
			endRequest(theServletRequest);
		}

	}


	/**
	 * /Patient/$member-match operation
	 * Basic implementation matching by coverage id or by coverage identifier. Not matching by
	 * Beneficiary (Patient) demographics in this version
	 */
	@Operation(name = ProviderConstants.OPERATION_MEMBER_MATCH, idempotent = false, returnParameters = {
		@OperationParam(name = "MemberIdentifier", typeName = "string")
	})
	public Parameters patientMemberMatch(
		javax.servlet.http.HttpServletRequest theServletRequest,

		@Description(shortDefinition = "The target of the operation. Will be returned with Identifier for matched coverage added.")
		@OperationParam(name = Constants.PARAM_MEMBER_PATIENT, min = 1, max = 1)
		Patient theMemberPatient,

		@Description(shortDefinition = "Old coverage information as extracted from beneficiary's card.")
		@OperationParam(name = Constants.PARAM_OLD_COVERAGE, min = 1, max = 1)
		Coverage oldCoverage,

		@Description(shortDefinition = "New Coverage information. Provided as a reference. Optionally returned unmodified.")
		@OperationParam(name = Constants.PARAM_NEW_COVERAGE, min = 1, max = 1)
		Coverage newCoverage,

		RequestDetails theRequestDetails
	) {
		return doMemberMatchOperation(theServletRequest, theMemberPatient, oldCoverage, newCoverage, theRequestDetails);
	}


	private Parameters doMemberMatchOperation(HttpServletRequest theServletRequest, Patient theMemberPatient,
				Coverage theCoverageToMatch, Coverage theCoverageToLink, RequestDetails theRequestDetails) {

		validateParams(theMemberPatient, theCoverageToMatch, theCoverageToLink);

		Optional<Coverage> coverageOpt = myMemberMatcherR4Helper.findMatchingCoverage(theCoverageToMatch);
		if ( ! coverageOpt.isPresent()) {
			String i18nMessage = getContext().getLocalizer().getMessage(
				"operation.member.match.error.coverage.not.found");
			throw new UnprocessableEntityException(Msg.code(1155) + i18nMessage);
		}
		Coverage coverage = coverageOpt.get();

		Optional<Patient> patientOpt = myMemberMatcherR4Helper.getBeneficiaryPatient(coverage);
		if (! patientOpt.isPresent()) {
			String i18nMessage = getContext().getLocalizer().getMessage(
				"operation.member.match.error.beneficiary.not.found");
			throw new UnprocessableEntityException(Msg.code(1156) + i18nMessage);
		}
		Patient patient = patientOpt.get();

		if (patient.getIdentifier().isEmpty()) {
			String i18nMessage = getContext().getLocalizer().getMessage(
				"operation.member.match.error.beneficiary.without.identifier");
			throw new UnprocessableEntityException(Msg.code(1157) + i18nMessage);
		}

		myMemberMatcherR4Helper.addMemberIdentifierToMemberPatient(theMemberPatient, patient.getIdentifierFirstRep());

		return myMemberMatcherR4Helper.buildSuccessReturnParameters(theMemberPatient, theCoverageToLink);
	}


	private void validateParams(Patient theMemberPatient, Coverage theOldCoverage, Coverage theNewCoverage) {
		validateParam(theMemberPatient, Constants.PARAM_MEMBER_PATIENT);
		validateParam(theOldCoverage, Constants.PARAM_OLD_COVERAGE);
		validateParam(theNewCoverage, Constants.PARAM_NEW_COVERAGE);
	}


	private void validateParam(Object theParam, String theParamName) {
		if (theParam == null) {
			String i18nMessage = getContext().getLocalizer().getMessage(
				"operation.member.match.error.missing.parameter", theParamName);
			throw new UnprocessableEntityException(Msg.code(1158) + i18nMessage);
		}
	}


	/**
	 * Given a list of string types, return only the ID portions of any parameters passed in.
	 */
	private TokenOrListParam toFlattenedPatientIdTokenParamList(List<IdType> theId) {
		TokenOrListParam retVal = new TokenOrListParam();
		if (theId != null) {
			for (IdType next: theId) {
				if (isNotBlank(next.getValue())) {
					String[] split = next.getValueAsString().split(",");
					Arrays.stream(split).map(IdType::new).forEach(id -> {
						retVal.addOr(new TokenParam(id.getIdPart()));
					});
				}
			}
		}
		return retVal.getValuesAsQueryTokens().isEmpty() ? null: retVal;
	}

	private StringAndListParam toStringAndList(List<StringType> theNarrative) {
		StringAndListParam retVal = new StringAndListParam();
		if (theNarrative != null) {
			for (StringType next : theNarrative) {
				if (isNotBlank(next.getValue())) {
					retVal.addAnd(new StringOrListParam().addOr(new StringParam(next.getValue())));
				}
			}
		}
		if (retVal.getValuesAsQueryTokens().isEmpty()) {
			return null;
		}
		return retVal;
	}

}
