/*
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

import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoPatient;
import ca.uhn.fhir.jpa.api.dao.PatientEverythingParameters;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.primitive.IdDt;
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
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import java.util.Arrays;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public abstract class BaseJpaResourceProviderPatient<T extends IBaseResource> extends BaseJpaResourceProvider<T> {

	/**
	 * Patient/123/$everything
	 */
	@Operation(
			name = JpaConstants.OPERATION_EVERYTHING,
			canonicalUrl = "http://hl7.org/fhir/OperationDefinition/Patient-everything",
			idempotent = true,
			bundleType = BundleTypeEnum.SEARCHSET)
	public IBundleProvider patientInstanceEverything(
			jakarta.servlet.http.HttpServletRequest theServletRequest,
			@IdParam IIdType theId,
			@Description(
							shortDefinition =
									"Results from this method are returned across multiple pages. This parameter controls the size of those pages.")
					@OperationParam(name = Constants.PARAM_COUNT, typeName = "unsignedInt")
					IPrimitiveType<Integer> theCount,
			@Description(
							shortDefinition =
									"Results from this method are returned across multiple pages. This parameter controls the offset when fetching a page.")
					@OperationParam(name = Constants.PARAM_OFFSET, typeName = "unsignedInt")
					IPrimitiveType<Integer> theOffset,
			@Description(
							shortDefinition =
									"Only return resources which were last updated as specified by the given range")
					@OperationParam(name = Constants.PARAM_LASTUPDATED, min = 0, max = 1)
					DateRangeParam theLastUpdated,
			@Description(
							shortDefinition =
									"Filter the resources to return only resources matching the given _content filter (note that this filter is applied only to results which link to the given patient, not to the patient itself or to supporting resources linked to by the matched resources)")
					@OperationParam(
							name = Constants.PARAM_CONTENT,
							min = 0,
							max = OperationParam.MAX_UNLIMITED,
							typeName = "string")
					List<IPrimitiveType<String>> theContent,
			@Description(
							shortDefinition =
									"Filter the resources to return only resources matching the given _text filter (note that this filter is applied only to results which link to the given patient, not to the patient itself or to supporting resources linked to by the matched resources)")
					@OperationParam(
							name = Constants.PARAM_TEXT,
							min = 0,
							max = OperationParam.MAX_UNLIMITED,
							typeName = "string")
					List<IPrimitiveType<String>> theNarrative,
			@Description(
							shortDefinition =
									"Filter the resources to return only resources matching the given _filter filter (note that this filter is applied only to results which link to the given patient, not to the patient itself or to supporting resources linked to by the matched resources)")
					@OperationParam(
							name = Constants.PARAM_FILTER,
							min = 0,
							max = OperationParam.MAX_UNLIMITED,
							typeName = "string")
					List<IPrimitiveType<String>> theFilter,
			@Description(
							shortDefinition =
									"Filter the resources to return only resources matching the given _type filter (note that this filter is applied only to results which link to the given patient, not to the patient itself or to supporting resources linked to by the matched resources)")
					@OperationParam(
							name = Constants.PARAM_TYPE,
							min = 0,
							max = OperationParam.MAX_UNLIMITED,
							typeName = "string")
					List<IPrimitiveType<String>> theTypes,
			@Description(
							shortDefinition =
									"Filter the resources to return only resources matching the given _type filter (note that this filter is applied only to results which link to the given patient, not to the patient itself or to supporting resources linked to by the matched resources)")
					@OperationParam(name = Constants.PARAM_MDM, min = 0, max = 1, typeName = "boolean")
					IPrimitiveType<Boolean> theMdmExpand,
			@Sort SortSpec theSortSpec,
			RequestDetails theRequestDetails) {

		startRequest(theServletRequest);
		try {
			PatientEverythingParameters everythingParams = new PatientEverythingParameters();
			everythingParams.setCount(theCount);
			everythingParams.setOffset(theOffset);
			everythingParams.setLastUpdated(theLastUpdated);
			everythingParams.setSort(theSortSpec);
			everythingParams.setContent(toStringAndList(theContent));
			everythingParams.setNarrative(toStringAndList(theNarrative));
			everythingParams.setFilter(toStringAndList(theFilter));
			everythingParams.setTypes(toStringAndList(theTypes));
			everythingParams.setMdmExpand(resolveNullValue(theMdmExpand));

			return ((IFhirResourceDaoPatient<?>) getDao())
					.patientInstanceEverything(theServletRequest, theRequestDetails, everythingParams, theId);
		} finally {
			endRequest(theServletRequest);
		}
	}

	/**
	 * /Patient/$everything
	 */
	@Operation(
			name = JpaConstants.OPERATION_EVERYTHING,
			canonicalUrl = "http://hl7.org/fhir/OperationDefinition/Patient-everything",
			idempotent = true,
			bundleType = BundleTypeEnum.SEARCHSET)
	public IBundleProvider patientTypeEverything(
			jakarta.servlet.http.HttpServletRequest theServletRequest,
			@Description(
							shortDefinition =
									"Results from this method are returned across multiple pages. This parameter controls the size of those pages.")
					@OperationParam(name = Constants.PARAM_COUNT, typeName = "unsignedInt")
					IPrimitiveType<Integer> theCount,
			@Description(
							shortDefinition =
									"Results from this method are returned across multiple pages. This parameter controls the offset when fetching a page.")
					@OperationParam(name = Constants.PARAM_OFFSET, typeName = "unsignedInt")
					IPrimitiveType<Integer> theOffset,
			@Description(
							shortDefinition =
									"Only return resources which were last updated as specified by the given range")
					@OperationParam(name = Constants.PARAM_LASTUPDATED, min = 0, max = 1)
					DateRangeParam theLastUpdated,
			@Description(
							shortDefinition =
									"Filter the resources to return only resources matching the given _content filter (note that this filter is applied only to results which link to the given patient, not to the patient itself or to supporting resources linked to by the matched resources)")
					@OperationParam(
							name = Constants.PARAM_CONTENT,
							min = 0,
							max = OperationParam.MAX_UNLIMITED,
							typeName = "string")
					List<IPrimitiveType<String>> theContent,
			@Description(
							shortDefinition =
									"Filter the resources to return only resources matching the given _text filter (note that this filter is applied only to results which link to the given patient, not to the patient itself or to supporting resources linked to by the matched resources)")
					@OperationParam(
							name = Constants.PARAM_TEXT,
							min = 0,
							max = OperationParam.MAX_UNLIMITED,
							typeName = "string")
					List<IPrimitiveType<String>> theNarrative,
			@Description(
							shortDefinition =
									"Filter the resources to return only resources matching the given _filter filter (note that this filter is applied only to results which link to the given patient, not to the patient itself or to supporting resources linked to by the matched resources)")
					@OperationParam(
							name = Constants.PARAM_FILTER,
							min = 0,
							max = OperationParam.MAX_UNLIMITED,
							typeName = "string")
					List<IPrimitiveType<String>> theFilter,
			@Description(
							shortDefinition =
									"Filter the resources to return only resources matching the given _type filter (note that this filter is applied only to results which link to the given patient, not to the patient itself or to supporting resources linked to by the matched resources)")
					@OperationParam(
							name = Constants.PARAM_TYPE,
							min = 0,
							max = OperationParam.MAX_UNLIMITED,
							typeName = "string")
					List<IPrimitiveType<String>> theTypes,
			@Description(shortDefinition = "Filter the resources to return based on the patient ids provided.")
					@OperationParam(
							name = Constants.PARAM_ID,
							min = 0,
							max = OperationParam.MAX_UNLIMITED,
							typeName = "id")
					List<IIdType> theId,
			@Description(
							shortDefinition =
									"Filter the resources to return only resources matching the given _type filter (note that this filter is applied only to results which link to the given patient, not to the patient itself or to supporting resources linked to by the matched resources)")
					@OperationParam(name = Constants.PARAM_MDM, min = 0, max = 1, typeName = "boolean")
					IPrimitiveType<Boolean> theMdmExpand,
			@Sort SortSpec theSortSpec,
			RequestDetails theRequestDetails) {

		startRequest(theServletRequest);
		try {
			PatientEverythingParameters everythingParams = new PatientEverythingParameters();
			everythingParams.setCount(theCount);
			everythingParams.setOffset(theOffset);
			everythingParams.setLastUpdated(theLastUpdated);
			everythingParams.setSort(theSortSpec);
			everythingParams.setContent(toStringAndList(theContent));
			everythingParams.setNarrative(toStringAndList(theNarrative));
			everythingParams.setFilter(toStringAndList(theFilter));
			everythingParams.setTypes(toStringAndList(theTypes));
			everythingParams.setMdmExpand(resolveNullValue(theMdmExpand));

			return ((IFhirResourceDaoPatient<?>) getDao())
					.patientTypeEverything(
							theServletRequest,
							theRequestDetails,
							everythingParams,
							toFlattenedPatientIdTokenParamList(theId));
		} finally {
			endRequest(theServletRequest);
		}
	}

	/**
	 * Given a list of string types, return only the ID portions of any parameters passed in.
	 */
	private TokenOrListParam toFlattenedPatientIdTokenParamList(List<IIdType> theId) {
		TokenOrListParam retVal = new TokenOrListParam();
		if (theId != null) {
			for (IIdType next : theId) {
				if (isNotBlank(next.getValue())) {
					String[] split = next.getValueAsString().split(",");
					Arrays.stream(split).map(IdDt::new).forEach(id -> {
						retVal.addOr(new TokenParam(id.getIdPart()));
					});
				}
			}
		}

		return retVal.getValuesAsQueryTokens().isEmpty() ? null : retVal;
	}

	private StringAndListParam toStringAndList(List<IPrimitiveType<String>> theNarrative) {
		StringAndListParam retVal = new StringAndListParam();
		if (theNarrative != null) {
			for (IPrimitiveType<String> next : theNarrative) {
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

	private boolean resolveNullValue(IPrimitiveType<Boolean> theMdmExpand) {
		return theMdmExpand == null ? Boolean.FALSE : theMdmExpand.getValue();
	}
}
