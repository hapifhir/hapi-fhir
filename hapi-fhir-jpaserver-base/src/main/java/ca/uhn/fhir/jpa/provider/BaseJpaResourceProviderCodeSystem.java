/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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

import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoCodeSystem;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.jpa.util.ValidationInvocationHelper;
import ca.uhn.fhir.jpa.validation.JpaValidationSupportChain;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.hapi.converters.canonical.VersionCanonicalizer;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import org.hl7.fhir.common.hapi.validation.support.CommonCodeSystemsTerminologyService;
import org.hl7.fhir.instance.model.api.IBaseCoding;
import org.hl7.fhir.instance.model.api.IBaseDatatype;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@SuppressWarnings("DefaultAnnotationParam")
public abstract class BaseJpaResourceProviderCodeSystem<T extends IBaseResource> extends BaseJpaResourceProvider<T> {

	@Autowired
	private JpaValidationSupportChain myValidationSupportChain;

	@Autowired
	private VersionCanonicalizer myVersionCanonicalizer;

	@Autowired
	private ValidationInvocationHelper myValidationInvocationHelper;

	/**
	 * $lookup operation
	 */
	@SuppressWarnings("unchecked")
	@Operation(
			name = JpaConstants.OPERATION_LOOKUP,
			idempotent = true,
			returnParameters = {
				@OperationParam(name = "name", typeName = "string", min = 1),
				@OperationParam(name = "version", typeName = "string", min = 0),
				@OperationParam(name = "display", typeName = "string", min = 1),
				@OperationParam(name = "abstract", typeName = "boolean", min = 1),
				@OperationParam(name = "property", min = 0, max = OperationParam.MAX_UNLIMITED, typeName = "code")
			})
	public IBaseParameters lookup(
			HttpServletRequest theServletRequest,
			@OperationParam(name = "code", min = 0, max = 1, typeName = "code") IPrimitiveType<String> theCode,
			@OperationParam(name = "system", min = 0, max = 1, typeName = "uri") IPrimitiveType<String> theSystem,
			@OperationParam(name = "coding", min = 0, max = 1, typeName = "Coding") IBaseCoding theCoding,
			@OperationParam(name = "version", min = 0, max = 1, typeName = "string") IPrimitiveType<String> theVersion,
			@OperationParam(name = "displayLanguage", min = 0, max = 1, typeName = "code")
					IPrimitiveType<String> theDisplayLanguage,
			@OperationParam(name = "property", min = 0, max = OperationParam.MAX_UNLIMITED, typeName = "code")
					List<IPrimitiveType<String>> thePropertyNames,
			RequestDetails theRequestDetails) {

		startRequest(theServletRequest);
		try {
			IFhirResourceDaoCodeSystem dao = (IFhirResourceDaoCodeSystem) getDao();
			IValidationSupport.LookupCodeResult result;
			applyVersionToSystem(theSystem, theVersion);
			result = dao.lookupCode(
					theCode, theSystem, theCoding, theDisplayLanguage, thePropertyNames, theRequestDetails);
			result.throwNotFoundIfAppropriate();
			return result.toParameters(theRequestDetails.getFhirContext(), thePropertyNames);
		} finally {
			endRequest(theServletRequest);
		}
	}

	/**
	 * $subsumes operation
	 */
	@Operation(
			name = JpaConstants.OPERATION_SUBSUMES,
			idempotent = true,
			returnParameters = {
				@OperationParam(name = "outcome", typeName = "code", min = 1),
			})
	public IBaseParameters subsumes(
			HttpServletRequest theServletRequest,
			@OperationParam(name = "codeA", min = 0, max = 1, typeName = "code") IPrimitiveType<String> theCodeA,
			@OperationParam(name = "codeB", min = 0, max = 1, typeName = "code") IPrimitiveType<String> theCodeB,
			@OperationParam(name = "system", min = 0, max = 1, typeName = "uri") IPrimitiveType<String> theSystem,
			@OperationParam(name = "codingA", min = 0, max = 1, typeName = "Coding") IBaseCoding theCodingA,
			@OperationParam(name = "codingB", min = 0, max = 1, typeName = "Coding") IBaseCoding theCodingB,
			@OperationParam(name = "version", min = 0, max = 1, typeName = "string") IPrimitiveType<String> theVersion,
			RequestDetails theRequestDetails) {

		startRequest(theServletRequest);
		try {
			IFhirResourceDaoCodeSystem dao = (IFhirResourceDaoCodeSystem) getDao();
			IFhirResourceDaoCodeSystem.SubsumesResult result;
			applyVersionToSystem(theSystem, theVersion);
			result = dao.subsumes(theCodeA, theCodeB, theSystem, theCodingA, theCodingB, theRequestDetails);
			return result.toParameters(theRequestDetails.getFhirContext());
		} finally {
			endRequest(theServletRequest);
		}
	}

	static void applyVersionToSystem(IPrimitiveType<String> theSystem, IPrimitiveType<String> theVersion) {
		if (theVersion != null && isNotBlank(theVersion.getValueAsString()) && theSystem != null) {
			theSystem.setValue(theSystem.getValueAsString() + "|" + theVersion.getValueAsString());
		}
	}

	/**
	 * $validate-code operation
	 */
	@Operation(
			name = JpaConstants.OPERATION_VALIDATE_CODE,
			idempotent = true,
			returnParameters = {
				@OperationParam(name = "result", typeName = "boolean", min = 1),
				@OperationParam(name = "message", typeName = "string"),
				@OperationParam(name = "display", typeName = "string")
			})
	public IBaseParameters validateCode(
			HttpServletRequest theServletRequest,
			@IdParam(optional = true) IIdType theId,
			@OperationParam(name = "url", min = 0, max = 1, typeName = "uri") IPrimitiveType<String> theUrl,
			@OperationParam(name = "version", min = 0, max = 1, typeName = "string") IPrimitiveType<String> theVersion,
			@OperationParam(name = "code", min = 0, max = 1, typeName = "code") IPrimitiveType<String> theCode,
			@OperationParam(name = "display", min = 0, max = 1, typeName = "string") IPrimitiveType<String> theDisplay,
			@OperationParam(name = "coding", min = 0, max = 1, typeName = "Coding") IBaseCoding theCoding,
			@OperationParam(name = "codeableConcept", min = 0, max = 1, typeName = "CodeableConcept")
					IBaseDatatype theCodeableConcept,
			RequestDetails theRequestDetails) {

		startRequest(theServletRequest);
		try {
			// Determine the CodeSystem URL to validate against
			String codeSystemUrl = resolveCodeSystemUrl(theId, theUrl, theRequestDetails);

			// Delegate to the validation helper
			return myValidationInvocationHelper.invokeValidateCodeForCodeSystem(
					theCode, theVersion, theDisplay, theCoding, theCodeableConcept, codeSystemUrl);
		} finally {
			endRequest(theServletRequest);
		}
	}

	private String resolveCodeSystemUrl(
			IIdType theId, IPrimitiveType<String> theUrl, RequestDetails theRequestDetails) {
		// If an ID was provided, look up the CodeSystem URL from the resource
		if (theId != null && theId.hasIdPart()) {
			IFhirResourceDaoCodeSystem<T> dao = (IFhirResourceDaoCodeSystem<T>) getDao();
			IBaseResource codeSystem = dao.read(theId, theRequestDetails);
			return CommonCodeSystemsTerminologyService.getCodeSystemUrl(getContext(), codeSystem);
		}
		return getStringValue(theUrl);
	}

	private static @Nullable String getStringValue(IPrimitiveType<String> thePrimitive) {
		return (thePrimitive != null && thePrimitive.hasValue()) ? thePrimitive.getValueAsString() : null;
	}
}
