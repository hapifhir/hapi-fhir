/*-
 * #%L
 * HAPI FHIR Test Utilities
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
package ca.uhn.fhir.test.utilities.validation;

import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import jakarta.servlet.http.HttpServletRequest;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.UriType;
import org.hl7.fhir.r4.model.ValueSet;

import java.util.List;

public interface IValidationProvidersR4 {

	@SuppressWarnings("unused")
	class MyCodeSystemProviderR4 extends IValidationProviders.MyValidationProvider<CodeSystem> {

		@Operation(name = "$validate-code", idempotent = true, returnParameters = {
				@OperationParam(name = "result", type = BooleanType.class, min = 1),
				@OperationParam(name = "message", type = StringType.class),
				@OperationParam(name = "display", type = StringType.class)
		})
		public IBaseParameters validateCode(
				HttpServletRequest theServletRequest,
				@IdParam(optional = true) IdType theId,
				@OperationParam(name = "url", min = 0, max = 1) UriType theCodeSystemUrl,
				@OperationParam(name = "code", min = 0, max = 1) CodeType theCode,
				@OperationParam(name = "display", min = 0, max = 1) StringType theDisplay
		) throws Exception {
			String url = theCodeSystemUrl != null ? theCodeSystemUrl.getValue() : null;
			String code = theCode != null ? theCode.getValue() : null;
			return getTerminologyResponse("$validate-code", url, code);
		}

		@Operation(name = "$lookup", idempotent = true, returnParameters= {
				@OperationParam(name = "name", type = StringType.class, min = 1),
				@OperationParam(name = "version", type = StringType.class),
				@OperationParam(name = "display", type = StringType.class, min = 1),
				@OperationParam(name = "abstract", type = BooleanType.class, min = 1),
				@OperationParam(name = "property", type = StringType.class, min = 0, max = OperationParam.MAX_UNLIMITED)
		})
		public IBaseParameters lookup(
				HttpServletRequest theServletRequest,
				@OperationParam(name = "code", max = 1) CodeType theCode,
				@OperationParam(name = "system",max = 1) UriType theSystem,
				@OperationParam(name = "coding", max = 1) Coding theCoding,
				@OperationParam(name = "version", max = 1) StringType ignoredTheVersion,
				@OperationParam(name = "displayLanguage", max = 1) CodeType theDisplayLanguage,
				@OperationParam(name = "property", max = OperationParam.MAX_UNLIMITED) List<CodeType> thePropertyNames,
				RequestDetails theRequestDetails
		) throws Exception {
			String url = theSystem != null ? theSystem.getValue() : null;
			String code = theCode != null ? theCode.getValue() : null;
			return getTerminologyResponse("$lookup", url, code);
		}
		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return CodeSystem.class;
		}

		@Override
		Class<Parameters> getParameterType() {
			return Parameters.class;
		}

		@Override
		public CodeSystem addTerminologyResource(String theUrl) {
			CodeSystem codeSystem = new CodeSystem();
			codeSystem.setId(theUrl.substring(0, theUrl.lastIndexOf("/")));
			codeSystem.setUrl(theUrl);
			addTerminologyResource(theUrl, codeSystem);
			return codeSystem;
		}
	}

	@SuppressWarnings("unused")
	class MyValueSetProviderR4 extends IValidationProviders.MyValidationProvider<ValueSet> {

		@Operation(name = "$validate-code", idempotent = true, returnParameters = {
				@OperationParam(name = "result", type = BooleanType.class, min = 1),
				@OperationParam(name = "message", type = StringType.class),
				@OperationParam(name = "display", type = StringType.class)
		})
		public IBaseParameters validateCode(
				HttpServletRequest theServletRequest,
				@IdParam(optional = true) IdType theId,
				@OperationParam(name = "url", min = 0, max = 1) UriType theValueSetUrl,
				@OperationParam(name = "code", min = 0, max = 1) CodeType theCode,
				@OperationParam(name = "system", min = 0, max = 1) UriType theSystem,
				@OperationParam(name = "display", min = 0, max = 1) StringType theDisplay,
				@OperationParam(name = "valueSet") ValueSet theValueSet
		) throws Exception {
			String url = theValueSetUrl != null ? theValueSetUrl.getValue() : null;
			String code = theCode != null ? theCode.getValue() : null;
			return getTerminologyResponse("$validate-code", url, code);
		}
		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return ValueSet.class;
		}
		@Override
		Class<Parameters> getParameterType() {
			return Parameters.class;
		}
		@Override
		public ValueSet addTerminologyResource(String theUrl) {
			ValueSet valueSet = new ValueSet();
			valueSet.setId(theUrl.substring(0, theUrl.lastIndexOf("/")));
			valueSet.setUrl(theUrl);
			addTerminologyResource(theUrl, valueSet);
			return valueSet;
		}
	}
}
