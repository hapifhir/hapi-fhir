/*-
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

import ca.uhn.fhir.context.support.IValidationSupport.CodeValidationResult;
import ca.uhn.fhir.context.support.ValueSetExpansionOptions;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoValueSet;
import ca.uhn.fhir.jpa.config.JpaConfig;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.jpa.term.api.ITermReadSvc;
import ca.uhn.fhir.jpa.util.ValidationInvocationHelper;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import ca.uhn.fhir.util.ParametersUtil;
import ca.uhn.hapi.converters.canonical.VersionCanonicalizer;
import com.google.common.annotations.VisibleForTesting;
import jakarta.servlet.http.HttpServletRequest;
import org.hl7.fhir.common.hapi.validation.support.CommonCodeSystemsTerminologyService;
import org.hl7.fhir.common.hapi.validation.support.ValidationSupportChain;
import org.hl7.fhir.instance.model.api.IBaseCoding;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.ICompositeType;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@SuppressWarnings("DefaultAnnotationParam")
public class ValueSetOperationProvider extends BaseJpaProvider {

	private static final Logger ourLog = LoggerFactory.getLogger(ValueSetOperationProvider.class);

	@Autowired
	private DaoRegistry myDaoRegistry;

	@Autowired
	private ITermReadSvc myTermReadSvc;

	@Autowired
	private VersionCanonicalizer myVersionCanonicalizer;

	@Autowired
	@Qualifier(JpaConfig.JPA_VALIDATION_SUPPORT_CHAIN)
	private ValidationSupportChain myValidationSupportChain;

	@Autowired
	private ValidationInvocationHelper myValidationInvocationHelper;

	@VisibleForTesting
	public void setDaoRegistryForUnitTest(DaoRegistry theDaoRegistry) {
		myDaoRegistry = theDaoRegistry;
	}

	@VisibleForTesting
	public void setVersionCanonicalizerForUnitTest(VersionCanonicalizer theVersionCanonicalizer) {
		myVersionCanonicalizer = theVersionCanonicalizer;
	}

	@VisibleForTesting
	public void setTermReadSvcForUnitTest(ITermReadSvc theTermReadSvc) {
		myTermReadSvc = theTermReadSvc;
	}

	@VisibleForTesting
	public void setValidationSupportChainForUnitTest(ValidationSupportChain theValidationSupportChain) {
		myValidationSupportChain = theValidationSupportChain;
	}

	@Operation(name = JpaConstants.OPERATION_EXPAND, idempotent = true, typeName = "ValueSet")
	public IBaseResource expand(
			HttpServletRequest theServletRequest,
			@IdParam(optional = true) IIdType theId,
			@OperationParam(name = "valueSet", min = 0, max = 1) IBaseResource theValueSet,
			@OperationParam(name = "url", min = 0, max = 1, typeName = "uri") IPrimitiveType<String> theUrl,
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

		startRequest(theServletRequest);
		try {

			return getDao().expand(
							theId,
							theValueSet,
							theUrl,
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

	@SuppressWarnings("unchecked")
	protected IFhirResourceDaoValueSet<IBaseResource> getDao() {
		return (IFhirResourceDaoValueSet<IBaseResource>) myDaoRegistry.getResourceDao("ValueSet");
	}

	@Operation(
			name = JpaConstants.OPERATION_VALIDATE_CODE,
			idempotent = true,
			typeName = "ValueSet",
			returnParameters = {
				@OperationParam(name = CodeValidationResult.RESULT, typeName = "boolean", min = 1),
				@OperationParam(name = CodeValidationResult.MESSAGE, typeName = "string"),
				@OperationParam(name = CodeValidationResult.DISPLAY, typeName = "string"),
				@OperationParam(name = CodeValidationResult.SOURCE_DETAILS, typeName = "string")
			})
	public IBaseParameters validateCode(
			HttpServletRequest theServletRequest,
			@IdParam(optional = true) IIdType theId,
			@OperationParam(name = "url", min = 0, max = 1, typeName = "uri") IPrimitiveType<String> theValueSetUrl,
			@OperationParam(name = "valueSetVersion", min = 0, max = 1, typeName = "string")
					IPrimitiveType<String> theValueSetVersion,
			@OperationParam(name = "code", min = 0, max = 1, typeName = "code") IPrimitiveType<String> theCode,
			@OperationParam(name = "system", min = 0, max = 1, typeName = "uri") IPrimitiveType<String> theSystem,
			@OperationParam(name = "systemVersion", min = 0, max = 1, typeName = "string")
					IPrimitiveType<String> theSystemVersion,
			@OperationParam(name = CodeValidationResult.DISPLAY, min = 0, max = 1, typeName = "string")
					IPrimitiveType<String> theDisplay,
			@OperationParam(name = "coding", min = 0, max = 1, typeName = "Coding") IBaseCoding theCoding,
			@OperationParam(name = "codeableConcept", min = 0, max = 1, typeName = "CodeableConcept")
					ICompositeType theCodeableConcept,
			RequestDetails theRequestDetails) {

		startRequest(theServletRequest);
		try {
			// Determine the ValueSet URL to validate against
			String valueSetUrlString = resolveValueSetUrl(theId, theValueSetUrl, theValueSetVersion, theRequestDetails);

			// Validate that a ValueSet identifier was provided
			if (valueSetUrlString == null) {
				throw new InvalidRequestException(
						Msg.code(901)
								+ "Either ValueSet ID or ValueSet identifier or system and code must be provided. Unable to validate.");
			}

			// Delegate to the validation helper
			return myValidationInvocationHelper.invokeValidateCodeForValueSet(
					theCode, theSystem, theSystemVersion, theDisplay, theCoding, theCodeableConcept, valueSetUrlString);
		} finally {
			endRequest(theServletRequest);
		}
	}

	private String resolveValueSetUrl(
			IIdType theId,
			IPrimitiveType<String> theValueSetUrl,
			IPrimitiveType<String> theValueSetVersion,
			RequestDetails theRequestDetails) {
		// If an ID was provided, look up the ValueSet URL from the resource
		if (theId != null && theId.hasIdPart()) {
			IFhirResourceDaoValueSet<IBaseResource> dao = getDao();
			IBaseResource valueSet = dao.read(theId, theRequestDetails);

			// Extract URL
			String url = extractValueSetUrl(valueSet);

			// Extract and append version from the resource if present
			String version = CommonCodeSystemsTerminologyService.getValueSetVersion(getContext(), valueSet);
			if (version != null) {
				return url + "|" + version;
			}
			return url;
		}

		// Build URL with version if provided via parameter
		String valueSetUrlString =
				(theValueSetUrl != null && theValueSetUrl.hasValue()) ? theValueSetUrl.getValueAsString() : null;
		if (valueSetUrlString != null && theValueSetVersion != null && theValueSetVersion.hasValue()) {
			valueSetUrlString = valueSetUrlString + "|" + theValueSetVersion.getValueAsString();
		}
		return valueSetUrlString;
	}

	private String extractValueSetUrl(IBaseResource theValueSet) {
		return getContext().newTerser().getSinglePrimitiveValueOrNull(theValueSet, "url");
	}

	@Operation(
			name = ProviderConstants.OPERATION_INVALIDATE_EXPANSION,
			idempotent = false,
			typeName = "ValueSet",
			returnParameters = {
				@OperationParam(name = CodeValidationResult.MESSAGE, typeName = "string", min = 1, max = 1)
			})
	public IBaseParameters invalidateValueSetExpansion(
			@IdParam IIdType theValueSetId, RequestDetails theRequestDetails, HttpServletRequest theServletRequest) {
		startRequest(theServletRequest);
		try {

			String outcome = myTermReadSvc.invalidatePreCalculatedExpansion(theValueSetId, theRequestDetails);

			IBaseParameters retVal = ParametersUtil.newInstance(getContext());
			ParametersUtil.addParameterToParametersString(getContext(), retVal, CodeValidationResult.MESSAGE, outcome);
			return retVal;

		} finally {
			endRequest(theServletRequest);
		}
	}

	public static ValueSetExpansionOptions createValueSetExpansionOptions(
			JpaStorageSettings theStorageSettings,
			IPrimitiveType<Integer> theOffset,
			IPrimitiveType<Integer> theCount,
			IPrimitiveType<Boolean> theIncludeHierarchy,
			IPrimitiveType<String> theFilter,
			IPrimitiveType<String> theDisplayLanguage) {
		int offset = theStorageSettings.getPreExpandValueSetsDefaultOffset();
		if (theOffset != null && theOffset.hasValue()) {
			if (theOffset.getValue() >= 0) {
				offset = theOffset.getValue();
			} else {
				throw new InvalidRequestException(
						Msg.code(1135) + "offset parameter for $expand operation must be >= 0 when specified. offset: "
								+ theOffset.getValue());
			}
		}

		int count = theStorageSettings.getPreExpandValueSetsDefaultCount();
		if (theCount != null && theCount.hasValue()) {
			if (theCount.getValue() >= 0) {
				count = theCount.getValue();
			} else {
				throw new InvalidRequestException(
						Msg.code(1136) + "count parameter for $expand operation must be >= 0 when specified. count: "
								+ theCount.getValue());
			}
		}
		int countMax = theStorageSettings.getPreExpandValueSetsMaxCount();
		if (count > countMax) {
			ourLog.warn(
					"count parameter for $expand operation of {} exceeds maximum value of {}; using maximum value.",
					count,
					countMax);
			count = countMax;
		}

		ValueSetExpansionOptions options = ValueSetExpansionOptions.forOffsetAndCount(offset, count);

		if (theIncludeHierarchy != null && Boolean.TRUE.equals(theIncludeHierarchy.getValue())) {
			options.setIncludeHierarchy(true);
		}

		if (theFilter != null) {
			options.setFilter(theFilter.getValue());
		}

		if (theDisplayLanguage != null) {
			options.setTheDisplayLanguage(theDisplayLanguage.getValue());
		}

		return options;
	}
}
