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

import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.jpa.dao.JpaResourceDaoCodeSystem;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.util.FhirTerser;
import jakarta.servlet.http.HttpServletRequest;
import org.hl7.fhir.instance.model.api.IBaseCoding;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static ca.uhn.fhir.jpa.provider.BaseJpaResourceProviderCodeSystem.applyVersionToSystem;

public class ValueSetOperationProviderDstu2 extends ValueSetOperationProvider {

	@Autowired
	private IValidationSupport myValidationSupport;

	/**
	 * Alternate expand implementation since DSTU2 uses "identifier" instead of "url" as a parameter.
	 */
	@Operation(name = JpaConstants.OPERATION_EXPAND, idempotent = true, typeName = "ValueSet")
	public IBaseResource expand(
			HttpServletRequest theServletRequest,
			@IdParam(optional = true) IIdType theId,
			@OperationParam(name = "valueSet", min = 0, max = 1) IBaseResource theValueSet,
			@OperationParam(name = "url", min = 0, max = 1, typeName = "uri") IPrimitiveType<String> theUrl,
			@OperationParam(name = "identifier", min = 0, max = 1, typeName = "uri")
					IPrimitiveType<String> theIdentifier,
			@OperationParam(name = "valueSetVersion", min = 0, max = 1, typeName = "string")
					IPrimitiveType<String> theValueSetVersion,
			@OperationParam(name = "filter", min = 0, max = 1, typeName = "string") IPrimitiveType<String> theFilter,
			@OperationParam(name = "context", min = 0, max = 1, typeName = "string") IPrimitiveType<String> theContext,
			@OperationParam(name = "contextDirection", min = 0, max = 1, typeName = "string")
					IPrimitiveType<String> theContextDirection,
			@OperationParam(name = "offset", min = 0, max = 1, typeName = "integer") IPrimitiveType<Integer> theOffset,
			@OperationParam(name = "count", min = 0, max = 1, typeName = "integer") IPrimitiveType<Integer> theCount,
			@OperationParam(
							name = JpaConstants.OPERATION_EXPAND_PARAM_DISPLAY_LANGUAGE,
							min = 0,
							max = 1,
							typeName = "code")
					IPrimitiveType<String> theDisplayLanguage,
			@OperationParam(
							name = JpaConstants.OPERATION_EXPAND_PARAM_INCLUDE_HIERARCHY,
							min = 0,
							max = 1,
							typeName = "boolean")
					IPrimitiveType<Boolean> theIncludeHierarchy,
			RequestDetails theRequestDetails) {

		IPrimitiveType<String> url = theUrl;
		if (theIdentifier != null) {
			url = theIdentifier;
		}

		startRequest(theServletRequest);
		try {

			return getDao().expand(
							theId,
							theValueSet,
							url,
							theValueSetVersion,
							theFilter,
							theContext,
							theContextDirection,
							theOffset,
							theCount,
							theDisplayLanguage,
							theIncludeHierarchy,
							theRequestDetails);

		} finally {
			endRequest(theServletRequest);
		}
	}

	/**
	 * $lookup operation - This is on CodeSystem after DSTU2 but on ValueSet in DSTU2
	 */
	@SuppressWarnings("unchecked")
	@Operation(
			name = JpaConstants.OPERATION_LOOKUP,
			idempotent = true,
			typeName = "ValueSet",
			returnParameters = {
				@OperationParam(name = "name", typeName = "string", min = 1),
				@OperationParam(name = "version", typeName = "string", min = 0),
				@OperationParam(name = "display", typeName = "string", min = 1),
				@OperationParam(name = "abstract", typeName = "boolean", min = 1),
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
			IValidationSupport.LookupCodeResult result;
			applyVersionToSystem(theSystem, theVersion);

			FhirTerser terser = getContext().newTerser();
			result = JpaResourceDaoCodeSystem.doLookupCode(
					getContext(),
					terser,
					myValidationSupport,
					theCode,
					theSystem,
					theCoding,
					theDisplayLanguage,
					thePropertyNames);
			result.throwNotFoundIfAppropriate();
			return result.toParameters(theRequestDetails.getFhirContext(), thePropertyNames);
		} finally {
			endRequest(theServletRequest);
		}
	}
}
