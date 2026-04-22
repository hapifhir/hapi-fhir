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

import ca.uhn.fhir.context.support.ConceptValidationOptions;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.IValidationSupport.CodeValidationResult;
import ca.uhn.fhir.context.support.ValidationSupportContext;
import ca.uhn.fhir.context.support.ValueSetExpansionOptions;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoValueSet;
import ca.uhn.fhir.jpa.config.JpaConfig;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.jpa.term.api.ITermReadSvc;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import ca.uhn.fhir.util.ParametersUtil;
import com.google.common.annotations.VisibleForTesting;
import jakarta.servlet.http.HttpServletRequest;
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

import java.util.Optional;
import java.util.function.Supplier;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class ValueSetOperationProvider extends BaseJpaProvider {

	private static final Logger ourLog = LoggerFactory.getLogger(ValueSetOperationProvider.class);

	@Autowired
	protected IValidationSupport myValidationSupport;

	@Autowired
	private DaoRegistry myDaoRegistry;

	@Autowired
	private ITermReadSvc myTermReadSvc;

	@Autowired
	@Qualifier(JpaConfig.JPA_VALIDATION_SUPPORT_CHAIN)
	private ValidationSupportChain myValidationSupportChain;

	@VisibleForTesting
	public void setDaoRegistryForUnitTest(DaoRegistry theDaoRegistry) {
		myDaoRegistry = theDaoRegistry;
	}

	public void setValidationSupport(IValidationSupport theValidationSupport) {
		myValidationSupport = theValidationSupport;
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

	@SuppressWarnings("unchecked")
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

		CodeValidationResult result;
		startRequest(theServletRequest);
		try {
			// If a Remote Terminology Server has been configured, use it
			if (myValidationSupportChain != null && myValidationSupportChain.isRemoteTerminologyServiceConfigured()) {
				String theSystemString =
						(theSystem != null && theSystem.hasValue()) ? theSystem.getValueAsString() : null;
				String theCodeString = (theCode != null && theCode.hasValue()) ? theCode.getValueAsString() : null;
				String theDisplayString =
						(theDisplay != null && theDisplay.hasValue()) ? theDisplay.getValueAsString() : null;
				String theValueSetUrlString = (theValueSetUrl != null && theValueSetUrl.hasValue())
						? theValueSetUrl.getValueAsString()
						: null;
				if (theCoding != null) {
					if (isNotBlank(theCoding.getSystem())) {
						if (theSystemString != null && !theSystemString.equalsIgnoreCase(theCoding.getSystem())) {
							throw new InvalidRequestException(Msg.code(2352) + "Coding.system '" + theCoding.getSystem()
									+ "' does not equal param system '" + theSystemString
									+ "'. Unable to validate-code.");
						}
						theSystemString = theCoding.getSystem();
						theCodeString = theCoding.getCode();
						theDisplayString = theCoding.getDisplay();
					}
				}

				result = validateCodeWithTerminologyService(
								theSystemString, theCodeString, theDisplayString, theValueSetUrlString)
						.orElseGet(supplyUnableToValidateResult(theSystemString, theCodeString, theValueSetUrlString));
			} else {
				// Otherwise, use the local DAO layer to validate the code
				IFhirResourceDaoValueSet<IBaseResource> dao = getDao();
				IPrimitiveType<String> valueSetIdentifier;
				if (theValueSetUrl != null && theValueSetVersion != null) {
					valueSetIdentifier = (IPrimitiveType<String>)
							getContext().getElementDefinition("uri").newInstance();
					valueSetIdentifier.setValue(theValueSetUrl.getValue() + "|" + theValueSetVersion);
				} else {
					valueSetIdentifier = theValueSetUrl;
				}
				IPrimitiveType<String> codeSystemIdentifier;
				if (theSystem != null && theSystemVersion != null) {
					codeSystemIdentifier = (IPrimitiveType<String>)
							getContext().getElementDefinition("uri").newInstance();
					codeSystemIdentifier.setValue(theSystem.getValue() + "|" + theSystemVersion);
				} else {
					codeSystemIdentifier = theSystem;
				}
				result = dao.validateCode(
						valueSetIdentifier,
						theId,
						theCode,
						codeSystemIdentifier,
						theDisplay,
						theCoding,
						theCodeableConcept,
						theRequestDetails);
			}
			return result.toParameters(getContext());
		} finally {
			endRequest(theServletRequest);
		}
	}

	private Optional<CodeValidationResult> validateCodeWithTerminologyService(
			String theSystem, String theCode, String theDisplay, String theValueSetUrl) {
		return Optional.ofNullable(myValidationSupportChain.validateCode(
				new ValidationSupportContext(myValidationSupportChain),
				new ConceptValidationOptions(),
				theSystem,
				theCode,
				theDisplay,
				theValueSetUrl));
	}

	private Supplier<CodeValidationResult> supplyUnableToValidateResult(
			String theSystem, String theCode, String theValueSetUrl) {
		return () -> new CodeValidationResult()
				.setMessage("Validator is unable to provide validation for " + theCode + "#" + theSystem
						+ " - Unknown or unusable ValueSet[" + theValueSetUrl + "]");
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
