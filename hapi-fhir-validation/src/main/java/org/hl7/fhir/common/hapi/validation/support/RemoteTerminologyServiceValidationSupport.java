package org.hl7.fhir.common.hapi.validation.support;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.support.ConceptValidationOptions;
import ca.uhn.fhir.context.support.DefaultProfileValidationSupport;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.LookupCodeRequest;
import ca.uhn.fhir.context.support.TranslateConceptResults;
import ca.uhn.fhir.context.support.ValidationSupportContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.rest.api.SummaryEnum;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.gclient.IQuery;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.util.BundleUtil;
import ca.uhn.fhir.util.Logs;
import ca.uhn.fhir.util.ParametersUtil;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseDatatype;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Base;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Parameters.ParametersParameterComponent;
import org.hl7.fhir.r4.model.Property;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.Type;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static ca.uhn.fhir.util.ParametersUtil.getNamedParameterResource;
import static ca.uhn.fhir.util.ParametersUtil.getNamedParameterValueAsString;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * This class is an implementation of {@link IValidationSupport} that fetches validation codes
 * from a remote FHIR based terminology server. It will invoke the FHIR
 * <a href="http://hl7.org/fhir/valueset-operation-validate-code.html">ValueSet/$validate-code</a>
 * operation in order to validate codes.
 */
public class RemoteTerminologyServiceValidationSupport extends BaseValidationSupport implements IValidationSupport {
	private static final Logger ourLog = Logs.getTerminologyTroubleshootingLog();

	public static final String ERROR_CODE_UNKNOWN_CODE_IN_CODE_SYSTEM = "unknownCodeInSystem";
	public static final String ERROR_CODE_UNKNOWN_CODE_IN_VALUE_SET = "unknownCodeInValueSet";

	private String myBaseUrl;
	private final List<Object> myClientInterceptors = new ArrayList<>();

	/**
	 * Constructor
	 *
	 * @param theFhirContext The FhirContext object to use
	 */
	public RemoteTerminologyServiceValidationSupport(FhirContext theFhirContext) {
		super(theFhirContext);
	}

	public RemoteTerminologyServiceValidationSupport(FhirContext theFhirContext, String theBaseUrl) {
		super(theFhirContext);
		myBaseUrl = theBaseUrl;
	}

	@Override
	public String getName() {
		return getFhirContext().getVersion().getVersion() + " Remote Terminology Service Validation Support";
	}

	@Override
	public CodeValidationResult validateCode(
			ValidationSupportContext theValidationSupportContext,
			ConceptValidationOptions theOptions,
			String theCodeSystem,
			String theCode,
			String theDisplay,
			String theValueSetUrl) {

		return invokeRemoteValidateCode(theCodeSystem, theCode, theDisplay, theValueSetUrl, null);
	}

	@Override
	public CodeValidationResult validateCodeInValueSet(
			ValidationSupportContext theValidationSupportContext,
			ConceptValidationOptions theOptions,
			String theCodeSystem,
			String theCode,
			String theDisplay,
			@Nonnull IBaseResource theValueSet) {

		IBaseResource valueSet = theValueSet;

		// some external validators require the system when the code is passed
		// so let's try to get it from the VS if is not present
		String codeSystem = theCodeSystem;
		if (isNotBlank(theCode) && isBlank(codeSystem)) {
			codeSystem = ValidationSupportUtils.extractCodeSystemForCode(theValueSet, theCode);
		}

		String valueSetUrl = DefaultProfileValidationSupport.getConformanceResourceUrl(myCtx, valueSet);
		if (isNotBlank(valueSetUrl)) {
			valueSet = null;
		} else {
			valueSetUrl = null;
		}
		return invokeRemoteValidateCode(codeSystem, theCode, theDisplay, valueSetUrl, valueSet);
	}

	@Override
	public IBaseResource fetchCodeSystem(String theSystem) {
		// callers of this want the whole resource.
		return fetchCodeSystem(theSystem, SummaryEnum.FALSE);
	}

	/**
	 * Fetch the code system, possibly a summary.
	 * @param theSystem the canonical url
	 * @param theSummaryParam to force a summary mode - or null to allow server default.
	 * @return the CodeSystem
	 */
	@Nullable
	private IBaseResource fetchCodeSystem(String theSystem, @Nullable SummaryEnum theSummaryParam) {
		IGenericClient client = provideClient();
		Class<? extends IBaseBundle> bundleType =
				myCtx.getResourceDefinition("Bundle").getImplementingClass(IBaseBundle.class);
		IQuery<IBaseBundle> codeSystemQuery = client.search()
				.forResource("CodeSystem")
				.where(CodeSystem.URL.matches().value(theSystem));

		if (theSummaryParam != null) {
			codeSystemQuery.summaryMode(theSummaryParam);
		}

		IBaseBundle results = codeSystemQuery.returnBundle(bundleType).execute();
		List<IBaseResource> resultsList = BundleUtil.toListOfResources(myCtx, results);
		if (!resultsList.isEmpty()) {
			return resultsList.get(0);
		}

		return null;
	}

	@Override
	public LookupCodeResult lookupCode(
			ValidationSupportContext theValidationSupportContext, @Nonnull LookupCodeRequest theLookupCodeRequest) {
		final String code = theLookupCodeRequest.getCode();
		final String system = theLookupCodeRequest.getSystem();
		final String displayLanguage = theLookupCodeRequest.getDisplayLanguage();
		Validate.notBlank(code, "theCode must be provided");

		IGenericClient client = provideClient();
		FhirContext fhirContext = client.getFhirContext();
		FhirVersionEnum fhirVersion = fhirContext.getVersion().getVersion();

		if (fhirVersion.isNewerThan(FhirVersionEnum.R4) || fhirVersion.isOlderThan(FhirVersionEnum.DSTU3)) {
			throw new UnsupportedOperationException(Msg.code(710) + "Unsupported FHIR version '"
					+ fhirVersion.getFhirVersionString() + "'. Only DSTU3 and R4 are supported.");
		}

		IBaseParameters params = ParametersUtil.newInstance(fhirContext);
		ParametersUtil.addParameterToParametersString(fhirContext, params, "code", code);
		if (!StringUtils.isEmpty(system)) {
			ParametersUtil.addParameterToParametersString(fhirContext, params, "system", system);
		}
		if (!StringUtils.isEmpty(displayLanguage)) {
			ParametersUtil.addParameterToParametersString(fhirContext, params, "language", displayLanguage);
		}
		for (String propertyName : theLookupCodeRequest.getPropertyNames()) {
			ParametersUtil.addParameterToParametersCode(fhirContext, params, "property", propertyName);
		}
		Class<? extends IBaseResource> codeSystemClass =
				myCtx.getResourceDefinition("CodeSystem").getImplementingClass();
		IBaseParameters outcome;
		try {
			outcome = client.operation()
					.onType(codeSystemClass)
					.named("$lookup")
					.withParameters(params)
					.useHttpGet()
					.execute();
		} catch (ResourceNotFoundException | InvalidRequestException e) {
			// this can potentially be moved to an interceptor and be reused in other areas
			// where we call a remote server or by the client as a custom interceptor
			// that interceptor would alter the status code of the response and the body into a different format
			// e.g. ClientResponseInterceptorModificationTemplate
			ourLog.error(e.getMessage(), e);
			LookupCodeResult result = LookupCodeResult.notFound(system, code);
			result.setErrorMessage(getErrorMessage(
					ERROR_CODE_UNKNOWN_CODE_IN_CODE_SYSTEM, system, code, getBaseUrl(), e.getMessage()));
			return result;
		}
		if (outcome != null && !outcome.isEmpty()) {
			if (fhirVersion == FhirVersionEnum.DSTU3) {
				return generateLookupCodeResultDstu3(code, system, (org.hl7.fhir.dstu3.model.Parameters) outcome);
			}
			if (fhirVersion == FhirVersionEnum.R4) {
				return generateLookupCodeResultR4(code, system, (Parameters) outcome);
			}
		}
		return LookupCodeResult.notFound(system, code);
	}

	protected String getErrorMessage(String errorCode, Object... theParams) {
		return getFhirContext().getLocalizer().getMessage(getClass(), errorCode, theParams);
	}

	private LookupCodeResult generateLookupCodeResultDstu3(
			String theCode, String theSystem, org.hl7.fhir.dstu3.model.Parameters outcomeDSTU3) {
		// NOTE: I wanted to put all of this logic into the IValidationSupport Class, but it would've required adding
		// several new dependencies on version-specific libraries and that is explicitly forbidden (see comment in
		// POM).
		LookupCodeResult result = new LookupCodeResult();
		result.setSearchedForCode(theCode);
		result.setSearchedForSystem(theSystem);
		result.setFound(true);
		for (org.hl7.fhir.dstu3.model.Parameters.ParametersParameterComponent parameterComponent :
				outcomeDSTU3.getParameter()) {
			String parameterTypeAsString = Objects.toString(parameterComponent.getValue(), null);
			switch (parameterComponent.getName()) {
				case "property":
					BaseConceptProperty conceptProperty = createConceptPropertyDstu3(parameterComponent);
					if (conceptProperty != null) {
						result.getProperties().add(conceptProperty);
					}
					break;
				case "designation":
					ConceptDesignation conceptDesignation = createConceptDesignationDstu3(parameterComponent);
					result.getDesignations().add(conceptDesignation);
					break;
				case "name":
					result.setCodeSystemDisplayName(parameterTypeAsString);
					break;
				case "version":
					result.setCodeSystemVersion(parameterTypeAsString);
					break;
				case "display":
					result.setCodeDisplay(parameterTypeAsString);
					break;
				case "abstract":
					result.setCodeIsAbstract(Boolean.parseBoolean(parameterTypeAsString));
					break;
				default:
			}
		}
		return result;
	}

	private static BaseConceptProperty createConceptPropertyDstu3(
			org.hl7.fhir.dstu3.model.Parameters.ParametersParameterComponent theParameterComponent) {
		org.hl7.fhir.dstu3.model.Property property = theParameterComponent.getChildByName("part");

		// The assumption here is that we may at east 2 elements in this part
		if (property == null || property.getValues().size() < 2) {
			return null;
		}

		List<org.hl7.fhir.dstu3.model.Base> values = property.getValues();
		org.hl7.fhir.dstu3.model.Parameters.ParametersParameterComponent firstPart =
				(org.hl7.fhir.dstu3.model.Parameters.ParametersParameterComponent) values.get(0);
		String propertyName = ((org.hl7.fhir.dstu3.model.CodeType) firstPart.getValue()).getValue();

		org.hl7.fhir.dstu3.model.Parameters.ParametersParameterComponent secondPart =
				(org.hl7.fhir.dstu3.model.Parameters.ParametersParameterComponent) values.get(1);
		org.hl7.fhir.dstu3.model.Type value = secondPart.getValue();

		if (value != null) {
			return createConceptPropertyDstu3(propertyName, value);
		}

		String groupName = secondPart.getName();
		if (!"subproperty".equals(groupName)) {
			return null;
		}

		// handle property group (a property containing sub-properties)
		GroupConceptProperty groupConceptProperty = new GroupConceptProperty(propertyName);

		// we already retrieved the property name (group name) as first element, next will be the sub-properties.
		// there is no dedicated value for a property group as it is an aggregate
		for (int i = 1; i < values.size(); i++) {
			org.hl7.fhir.dstu3.model.Parameters.ParametersParameterComponent nextPart =
					(org.hl7.fhir.dstu3.model.Parameters.ParametersParameterComponent) values.get(i);
			BaseConceptProperty subProperty = createConceptPropertyDstu3(nextPart);
			if (subProperty != null) {
				groupConceptProperty.addSubProperty(subProperty);
			}
		}
		return groupConceptProperty;
	}

	public static BaseConceptProperty createConceptProperty(final String theName, final IBaseDatatype theValue) {
		if (theValue instanceof Type) {
			return createConceptPropertyR4(theName, (Type) theValue);
		}
		if (theValue instanceof org.hl7.fhir.dstu3.model.Type) {
			return createConceptPropertyDstu3(theName, (org.hl7.fhir.dstu3.model.Type) theValue);
		}
		return null;
	}

	private static BaseConceptProperty createConceptPropertyDstu3(
			final String theName, final org.hl7.fhir.dstu3.model.Type theValue) {
		if (theValue == null) {
			return null;
		}
		BaseConceptProperty conceptProperty;
		String fhirType = theValue.fhirType();
		switch (fhirType) {
			case IValidationSupport.TYPE_STRING:
				org.hl7.fhir.dstu3.model.StringType stringType = (org.hl7.fhir.dstu3.model.StringType) theValue;
				conceptProperty = new StringConceptProperty(theName, stringType.getValue());
				break;
			case IValidationSupport.TYPE_CODING:
				org.hl7.fhir.dstu3.model.Coding coding = (org.hl7.fhir.dstu3.model.Coding) theValue;
				conceptProperty =
						new CodingConceptProperty(theName, coding.getSystem(), coding.getCode(), coding.getDisplay());
				break;
				// TODO: add other property types as per FHIR spec https://github.com/hapifhir/hapi-fhir/issues/5699
			default:
				// other types will not fail for Remote Terminology
				conceptProperty = new StringConceptProperty(theName, theValue.toString());
		}
		return conceptProperty;
	}

	private ConceptDesignation createConceptDesignationDstu3(
			org.hl7.fhir.dstu3.model.Parameters.ParametersParameterComponent theParameterComponent) {
		ConceptDesignation conceptDesignation = new ConceptDesignation();
		for (org.hl7.fhir.dstu3.model.Parameters.ParametersParameterComponent designationComponent :
				theParameterComponent.getPart()) {
			org.hl7.fhir.dstu3.model.Type designationComponentValue = designationComponent.getValue();
			if (designationComponentValue == null) {
				continue;
			}
			switch (designationComponent.getName()) {
				case "language":
					conceptDesignation.setLanguage(designationComponentValue.toString());
					break;
				case "use":
					org.hl7.fhir.dstu3.model.Coding coding =
							(org.hl7.fhir.dstu3.model.Coding) designationComponentValue;
					conceptDesignation.setUseSystem(coding.getSystem());
					conceptDesignation.setUseCode(coding.getCode());
					conceptDesignation.setUseDisplay(coding.getDisplay());
					break;
				case "value":
					conceptDesignation.setValue(designationComponent.getValue().toString());
					break;
				default:
			}
		}
		return conceptDesignation;
	}

	private LookupCodeResult generateLookupCodeResultR4(String theCode, String theSystem, Parameters outcomeR4) {
		// NOTE: I wanted to put all of this logic into the IValidationSupport Class, but it would've required adding
		//       several new dependencies on version-specific libraries and that is explicitly forbidden (see comment in
		// POM).
		LookupCodeResult result = new LookupCodeResult();
		result.setSearchedForCode(theCode);
		result.setSearchedForSystem(theSystem);
		result.setFound(true);
		for (ParametersParameterComponent parameterComponent : outcomeR4.getParameter()) {
			String parameterTypeAsString = Objects.toString(parameterComponent.getValue(), null);
			switch (parameterComponent.getName()) {
				case "property":
					BaseConceptProperty conceptProperty = createConceptPropertyR4(parameterComponent);
					if (conceptProperty != null) {
						result.getProperties().add(conceptProperty);
					}
					break;
				case "designation":
					ConceptDesignation conceptDesignation = createConceptDesignationR4(parameterComponent);
					result.getDesignations().add(conceptDesignation);
					break;
				case "name":
					result.setCodeSystemDisplayName(parameterTypeAsString);
					break;
				case "version":
					result.setCodeSystemVersion(parameterTypeAsString);
					break;
				case "display":
					result.setCodeDisplay(parameterTypeAsString);
					break;
				case "abstract":
					result.setCodeIsAbstract(Boolean.parseBoolean(parameterTypeAsString));
					break;
				default:
			}
		}
		return result;
	}

	private static BaseConceptProperty createConceptPropertyR4(ParametersParameterComponent thePropertyComponent) {
		Property property = thePropertyComponent.getChildByName("part");

		// The assumption here is that we may at east 2 elements in this part
		if (property == null || property.getValues().size() < 2) {
			return null;
		}

		List<Base> values = property.getValues();
		ParametersParameterComponent firstPart = (ParametersParameterComponent) values.get(0);
		String propertyName = ((CodeType) firstPart.getValue()).getValue();

		ParametersParameterComponent secondPart = (ParametersParameterComponent) values.get(1);
		Type value = secondPart.getValue();

		if (value != null) {
			return createConceptPropertyR4(propertyName, value);
		}

		String groupName = secondPart.getName();
		if (!"subproperty".equals(groupName)) {
			return null;
		}

		// handle property group (a property containing sub-properties)
		GroupConceptProperty groupConceptProperty = new GroupConceptProperty(propertyName);

		// we already retrieved the property name (group name) as first element, next will be the sub-properties.
		// there is no dedicated value for a property group as it is an aggregate
		for (int i = 1; i < values.size(); i++) {
			ParametersParameterComponent nextPart = (ParametersParameterComponent) values.get(i);
			BaseConceptProperty subProperty = createConceptPropertyR4(nextPart);
			if (subProperty != null) {
				groupConceptProperty.addSubProperty(subProperty);
			}
		}
		return groupConceptProperty;
	}

	private static BaseConceptProperty createConceptPropertyR4(final String theName, final Type theValue) {
		BaseConceptProperty conceptProperty;

		String fhirType = theValue.fhirType();
		switch (fhirType) {
			case IValidationSupport.TYPE_STRING:
				StringType stringType = (StringType) theValue;
				conceptProperty = new StringConceptProperty(theName, stringType.getValue());
				break;
			case IValidationSupport.TYPE_BOOLEAN:
				BooleanType booleanType = (BooleanType) theValue;
				conceptProperty = new BooleanConceptProperty(theName, booleanType.getValue());
				break;
			case IValidationSupport.TYPE_CODING:
				Coding coding = (Coding) theValue;
				conceptProperty =
						new CodingConceptProperty(theName, coding.getSystem(), coding.getCode(), coding.getDisplay());
				break;
				// TODO: add other property types as per FHIR spec https://github.com/hapifhir/hapi-fhir/issues/5699
			default:
				// other types will not fail for Remote Terminology
				conceptProperty = new StringConceptProperty(theName, theValue.toString());
		}
		return conceptProperty;
	}

	private ConceptDesignation createConceptDesignationR4(ParametersParameterComponent theParameterComponent) {
		ConceptDesignation conceptDesignation = new ConceptDesignation();
		for (ParametersParameterComponent designationComponent : theParameterComponent.getPart()) {
			Type designationComponentValue = designationComponent.getValue();
			if (designationComponentValue == null) {
				continue;
			}
			switch (designationComponent.getName()) {
				case "language":
					conceptDesignation.setLanguage(designationComponentValue.toString());
					break;
				case "use":
					Coding coding = (Coding) designationComponentValue;
					conceptDesignation.setUseSystem(coding.getSystem());
					conceptDesignation.setUseCode(coding.getCode());
					conceptDesignation.setUseDisplay(coding.getDisplay());
					break;
				case "value":
					conceptDesignation.setValue(designationComponentValue.toString());
					break;
				default:
			}
		}
		return conceptDesignation;
	}

	@Override
	public IBaseResource fetchValueSet(String theValueSetUrl) {
		// force the remote server to send the whole resource.
		SummaryEnum summaryParam = SummaryEnum.FALSE;
		return fetchValueSet(theValueSetUrl, summaryParam);
	}

	/**
	 * Search for a ValueSet by canonical url via IGenericClient.
	 *
	 * @param theValueSetUrl the canonical url of the ValueSet
	 * @param theSummaryParam force a summary mode - null allows server default
	 * @return the ValueSet or null if none match the url
	 */
	@Nullable
	private IBaseResource fetchValueSet(String theValueSetUrl, SummaryEnum theSummaryParam) {
		IGenericClient client = provideClient();
		Class<? extends IBaseBundle> bundleType =
				myCtx.getResourceDefinition("Bundle").getImplementingClass(IBaseBundle.class);

		IQuery<IBaseBundle> valueSetQuery = client.search()
				.forResource("ValueSet")
				.where(CodeSystem.URL.matches().value(theValueSetUrl));

		if (theSummaryParam != null) {
			valueSetQuery.summaryMode(theSummaryParam);
		}

		IBaseBundle results = valueSetQuery.returnBundle(bundleType).execute();

		List<IBaseResource> resultsList = BundleUtil.toListOfResources(myCtx, results);
		if (!resultsList.isEmpty()) {
			return resultsList.get(0);
		}

		return null;
	}

	@Override
	public boolean isCodeSystemSupported(ValidationSupportContext theValidationSupportContext, String theSystem) {
		// a summary is ok if we are just checking the presence.
		SummaryEnum summaryParam = null;

		return fetchCodeSystem(theSystem, summaryParam) != null;
	}

	@Override
	public boolean isValueSetSupported(ValidationSupportContext theValidationSupportContext, String theValueSetUrl) {
		// a summary is ok if we are just checking the presence.
		SummaryEnum summaryParam = null;

		return fetchValueSet(theValueSetUrl, summaryParam) != null;
	}

	@Override
	public TranslateConceptResults translateConcept(TranslateCodeRequest theRequest) {
		IGenericClient client = provideClient();
		FhirContext fhirContext = client.getFhirContext();

		IBaseParameters params = RemoteTerminologyUtil.buildTranslateInputParameters(fhirContext, theRequest);

		IBaseParameters outcome = client.operation()
				.onType("ConceptMap")
				.named("$translate")
				.withParameters(params)
				.execute();

		return RemoteTerminologyUtil.translateOutcomeToResults(fhirContext, outcome);
	}

	private IGenericClient provideClient() {
		IGenericClient retVal = myCtx.newRestfulGenericClient(myBaseUrl);
		for (Object next : myClientInterceptors) {
			retVal.registerInterceptor(next);
		}
		return retVal;
	}

	public String getBaseUrl() {
		return myBaseUrl;
	}

	protected CodeValidationResult invokeRemoteValidateCode(
			String theCodeSystem, String theCode, String theDisplay, String theValueSetUrl, IBaseResource theValueSet) {
		if (isBlank(theCode)) {
			return null;
		}

		IGenericClient client = provideClient();

		// this message builder can be removed once we introduce a parameter object like CodeValidationRequest
		ValidationErrorMessageBuilder errorMessageBuilder = theServerMessage -> {
			if (theValueSetUrl == null && theValueSet == null) {
				return getErrorMessage(
						ERROR_CODE_UNKNOWN_CODE_IN_CODE_SYSTEM, theCodeSystem, theCode, getBaseUrl(), theServerMessage);
			}
			return getErrorMessage(
					ERROR_CODE_UNKNOWN_CODE_IN_VALUE_SET,
					theCodeSystem,
					theCode,
					theValueSetUrl,
					getBaseUrl(),
					theServerMessage);
		};

		IBaseParameters input =
				buildValidateCodeInputParameters(theCodeSystem, theCode, theDisplay, theValueSetUrl, theValueSet);

		String resourceType = "ValueSet";
		if (theValueSet == null && theValueSetUrl == null) {
			resourceType = "CodeSystem";
		}

		try {
			IBaseParameters output = client.operation()
					.onType(resourceType)
					.named("validate-code")
					.withParameters(input)
					.execute();
			return createCodeValidationResult(output, errorMessageBuilder, theCode);
		} catch (ResourceNotFoundException | InvalidRequestException ex) {
			ourLog.error(ex.getMessage(), ex);
			String errorMessage = errorMessageBuilder.buildErrorMessage(ex.getMessage());
			CodeValidationIssueCode issueCode = ex instanceof ResourceNotFoundException
					? CodeValidationIssueCode.NOT_FOUND
					: CodeValidationIssueCode.CODE_INVALID;
			return createErrorCodeValidationResult(issueCode, errorMessage);
		}
	}

	private CodeValidationResult createErrorCodeValidationResult(
			CodeValidationIssueCode theIssueCode, String theMessage) {
		IssueSeverity severity = IssueSeverity.ERROR;
		return new CodeValidationResult()
				.setSeverity(severity)
				.setMessage(theMessage)
				.addIssue(new CodeValidationIssue(
						theMessage, severity, theIssueCode, CodeValidationIssueCoding.INVALID_CODE));
	}

	private CodeValidationResult createCodeValidationResult(
			IBaseParameters theOutput, ValidationErrorMessageBuilder theMessageBuilder, String theCode) {
		final FhirContext fhirContext = getFhirContext();
		Optional<String> resultValue = getNamedParameterValueAsString(fhirContext, theOutput, "result");

		if (!resultValue.isPresent()) {
			throw new IllegalArgumentException(
					Msg.code(2560) + "Parameter `result` is missing from the $validate-code response.");
		}

		boolean success = resultValue.get().equalsIgnoreCase("true");

		CodeValidationResult result = new CodeValidationResult();

		// TODO MM: avoid passing the code and only retrieve it from the response
		// that implies larger changes, like adding the result boolean to CodeValidationResult
		// since CodeValidationResult#isOk() relies on code being populated to determine the result/success
		if (success) {
			result.setCode(theCode);
		}

		Optional<String> systemValue = getNamedParameterValueAsString(fhirContext, theOutput, "system");
		systemValue.ifPresent(result::setCodeSystemName);
		Optional<String> versionValue = getNamedParameterValueAsString(fhirContext, theOutput, "version");
		versionValue.ifPresent(result::setCodeSystemVersion);
		Optional<String> displayValue = getNamedParameterValueAsString(fhirContext, theOutput, "display");
		displayValue.ifPresent(result::setDisplay);

		// in theory the message and the issues should not be populated when result=false
		if (success) {
			return result;
		}

		// for now assume severity ERROR, we may need to process the following for success cases as well
		result.setSeverity(IssueSeverity.ERROR);

		Optional<String> messageValue = getNamedParameterValueAsString(fhirContext, theOutput, "message");
		messageValue.ifPresent(value -> result.setMessage(theMessageBuilder.buildErrorMessage(value)));

		Optional<IBaseResource> issuesValue = getNamedParameterResource(fhirContext, theOutput, "issues");
		if (issuesValue.isPresent()) {
			// it seems to be safe to cast to IBaseOperationOutcome as any other type would not reach this point
			createCodeValidationIssues(
							(IBaseOperationOutcome) issuesValue.get(),
							fhirContext.getVersion().getVersion())
					.ifPresent(i -> i.forEach(result::addIssue));
		} else {
			// create a validation issue out of the message
			// this is a workaround to overcome an issue in the FHIR Validator library
			// where ValueSet bindings are only reading issues but not messages
			// @see https://github.com/hapifhir/org.hl7.fhir.core/issues/1766
			result.addIssue(createCodeValidationIssue(result.getMessage()));
		}
		return result;
	}

	/**
	 * Creates a list of {@link ca.uhn.fhir.context.support.IValidationSupport.CodeValidationIssue} from the issues
	 * returned by the $validate-code operation.
	 * Please note that this method should only be used for Remote Terminology for now as it only translates
	 * issues text/message and assumes all other fields.
	 * When issues will be supported across all validators in hapi-fhir, a proper generic conversion method should
	 * be available and this method will be deleted.
	 *
	 * @param theOperationOutcome the outcome of the $validate-code operation
	 * @param theFhirVersion the FHIR version
	 * @return the list of {@link ca.uhn.fhir.context.support.IValidationSupport.CodeValidationIssue}
	 */
	public static Optional<Collection<CodeValidationIssue>> createCodeValidationIssues(
			IBaseOperationOutcome theOperationOutcome, FhirVersionEnum theFhirVersion) {
		if (theFhirVersion == FhirVersionEnum.R4) {
			return Optional.of(createCodeValidationIssuesR4((OperationOutcome) theOperationOutcome));
		}
		if (theFhirVersion == FhirVersionEnum.DSTU3) {
			return Optional.of(
					createCodeValidationIssuesDstu3((org.hl7.fhir.dstu3.model.OperationOutcome) theOperationOutcome));
		}
		return Optional.empty();
	}

	private static Collection<CodeValidationIssue> createCodeValidationIssuesR4(OperationOutcome theOperationOutcome) {
		return theOperationOutcome.getIssue().stream()
				.map(issueComponent -> {
					String diagnostics = issueComponent.getDiagnostics();
					IssueSeverity issueSeverity =
							IssueSeverity.fromCode(issueComponent.getSeverity().toCode());
					String issueTypeCode = issueComponent.getCode().toCode();
					CodeableConcept details = issueComponent.getDetails();
					CodeValidationIssue issue = new CodeValidationIssue(diagnostics, issueSeverity, issueTypeCode);
					CodeValidationIssueDetails issueDetails = new CodeValidationIssueDetails(details.getText());
					details.getCoding().forEach(coding -> issueDetails.addCoding(coding.getSystem(), coding.getCode()));
					issue.setDetails(issueDetails);
					return issue;
				})
				.collect(Collectors.toList());
	}

	private static Collection<CodeValidationIssue> createCodeValidationIssuesDstu3(
			org.hl7.fhir.dstu3.model.OperationOutcome theOperationOutcome) {
		return theOperationOutcome.getIssue().stream()
				.map(issueComponent -> {
					String diagnostics = issueComponent.getDiagnostics();
					IssueSeverity issueSeverity =
							IssueSeverity.fromCode(issueComponent.getSeverity().toCode());
					String issueTypeCode = issueComponent.getCode().toCode();
					org.hl7.fhir.dstu3.model.CodeableConcept details = issueComponent.getDetails();
					CodeValidationIssue issue = new CodeValidationIssue(diagnostics, issueSeverity, issueTypeCode);
					CodeValidationIssueDetails issueDetails = new CodeValidationIssueDetails(details.getText());
					details.getCoding().forEach(coding -> issueDetails.addCoding(coding.getSystem(), coding.getCode()));
					issue.setDetails(issueDetails);
					return issue;
				})
				.collect(Collectors.toList());
	}

	private static CodeValidationIssue createCodeValidationIssue(String theMessage) {
		return new CodeValidationIssue(
				theMessage,
				IssueSeverity.ERROR,
				CodeValidationIssueCode.INVALID,
				CodeValidationIssueCoding.INVALID_CODE);
	}

	public interface ValidationErrorMessageBuilder {
		String buildErrorMessage(String theServerMessage);
	}

	protected IBaseParameters buildValidateCodeInputParameters(
			String theCodeSystem, String theCode, String theDisplay, String theValueSetUrl, IBaseResource theValueSet) {
		final FhirContext fhirContext = getFhirContext();
		IBaseParameters params = ParametersUtil.newInstance(fhirContext);

		if (theValueSet == null && theValueSetUrl == null) {
			ParametersUtil.addParameterToParametersUri(fhirContext, params, "url", theCodeSystem);
			ParametersUtil.addParameterToParametersString(fhirContext, params, "code", theCode);
			if (isNotBlank(theDisplay)) {
				ParametersUtil.addParameterToParametersString(fhirContext, params, "display", theDisplay);
			}
			return params;
		}

		if (isNotBlank(theValueSetUrl)) {
			ParametersUtil.addParameterToParametersUri(fhirContext, params, "url", theValueSetUrl);
		}
		ParametersUtil.addParameterToParametersString(fhirContext, params, "code", theCode);
		if (isNotBlank(theCodeSystem)) {
			ParametersUtil.addParameterToParametersUri(fhirContext, params, "system", theCodeSystem);
		}
		if (isNotBlank(theDisplay)) {
			ParametersUtil.addParameterToParametersString(fhirContext, params, "display", theDisplay);
		}
		if (theValueSet != null) {
			ParametersUtil.addParameterToParameters(fhirContext, params, "valueSet", theValueSet);
		}
		return params;
	}

	/**
	 * Sets the FHIR Terminology Server base URL
	 *
	 * @param theBaseUrl The base URL, e.g. "<a href="https://hapi.fhir.org/baseR4">...</a>"
	 */
	public void setBaseUrl(String theBaseUrl) {
		Validate.notBlank(theBaseUrl, "theBaseUrl must be provided");
		myBaseUrl = theBaseUrl;
	}

	/**
	 * Adds an interceptor that will be registered to all clients.
	 * <p>
	 * Note that this method is not thread-safe and should only be called prior to this module
	 * being used.
	 * </p>
	 *
	 * @param theClientInterceptor The interceptor (must not be null)
	 */
	public void addClientInterceptor(@Nonnull Object theClientInterceptor) {
		Validate.notNull(theClientInterceptor, "theClientInterceptor must not be null");
		myClientInterceptors.add(theClientInterceptor);
	}
}
