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
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.util.BundleUtil;
import ca.uhn.fhir.util.ParametersUtil;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseDatatype;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Base;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Parameters.ParametersParameterComponent;
import org.hl7.fhir.r4.model.Property;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.Type;
import org.hl7.fhir.r4.model.ValueSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * This class is an implementation of {@link IValidationSupport} that fetches validation codes
 * from a remote FHIR based terminology server. It will invoke the FHIR
 * <a href="http://hl7.org/fhir/valueset-operation-validate-code.html">ValueSet/$validate-code</a>
 * operation in order to validate codes.
 */
public class RemoteTerminologyServiceValidationSupport extends BaseValidationSupport implements IValidationSupport {
	private static final Logger ourLog = LoggerFactory.getLogger(RemoteTerminologyServiceValidationSupport.class);

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
			codeSystem = extractCodeSystemForCode((ValueSet) theValueSet, theCode);
		}

		// Remote terminology services shouldn't be used to validate codes with an implied system
		if (isBlank(codeSystem)) {
			return null;
		}

		String valueSetUrl = DefaultProfileValidationSupport.getConformanceResourceUrl(myCtx, valueSet);
		if (isNotBlank(valueSetUrl)) {
			valueSet = null;
		} else {
			valueSetUrl = null;
		}
		return invokeRemoteValidateCode(codeSystem, theCode, theDisplay, valueSetUrl, valueSet);
	}

	/**
	 * Try to obtain the codeSystem of the received code from the received ValueSet
	 */
	private String extractCodeSystemForCode(ValueSet theValueSet, String theCode) {
		if (theValueSet.getCompose() == null
				|| theValueSet.getCompose().getInclude() == null
				|| theValueSet.getCompose().getInclude().isEmpty()) {
			return null;
		}

		if (theValueSet.getCompose().getInclude().size() == 1) {
			ValueSet.ConceptSetComponent include =
					theValueSet.getCompose().getInclude().iterator().next();
			return getVersionedCodeSystem(include);
		}

		// when component has more than one include, their codeSystem(s) could be different, so we need to make sure
		// that we are picking up the system for the include filter to which the code corresponds
		for (ValueSet.ConceptSetComponent include : theValueSet.getCompose().getInclude()) {
			if (include.hasSystem()) {
				for (ValueSet.ConceptReferenceComponent concept : include.getConcept()) {
					if (concept.hasCodeElement() && concept.getCode().equals(theCode)) {
						return getVersionedCodeSystem(include);
					}
				}
			}
		}

		// at this point codeSystem couldn't be extracted for a multi-include ValueSet. Just on case it was
		// because the format was not well handled, let's allow to watch the VS by an easy logging change
		ourLog.trace("CodeSystem couldn't be extracted for code: {} for ValueSet: {}", theCode, theValueSet.getId());
		return null;
	}

	private String getVersionedCodeSystem(ValueSet.ConceptSetComponent theComponent) {
		String codeSystem = theComponent.getSystem();
		if (!codeSystem.contains("|") && theComponent.hasVersion()) {
			codeSystem += "|" + theComponent.getVersion();
		}
		return codeSystem;
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

		switch (fhirVersion) {
			case DSTU3:
			case R4:
				IBaseParameters params = ParametersUtil.newInstance(fhirContext);
				ParametersUtil.addParameterToParametersString(fhirContext, params, "code", code);
				if (!StringUtils.isEmpty(system)) {
					ParametersUtil.addParameterToParametersString(fhirContext, params, "system", system);
				}
				if (!StringUtils.isEmpty(displayLanguage)) {
					ParametersUtil.addParameterToParametersString(fhirContext, params, "language", displayLanguage);
				}
				for (String propertyName : theLookupCodeRequest.getPropertyNames()) {
					ParametersUtil.addParameterToParametersString(fhirContext, params, "property", propertyName);
				}
				Class<? extends IBaseResource> codeSystemClass =
						myCtx.getResourceDefinition("CodeSystem").getImplementingClass();
				IBaseParameters outcome = client.operation()
						.onType(codeSystemClass)
						.named("$lookup")
						.withParameters(params)
						.useHttpGet()
						.execute();
				if (outcome != null && !outcome.isEmpty()) {
					switch (fhirVersion) {
						case DSTU3:
							return generateLookupCodeResultDSTU3(
									code, system, (org.hl7.fhir.dstu3.model.Parameters) outcome);
						case R4:
							return generateLookupCodeResultR4(code, system, (Parameters) outcome);
					}
				}
				break;
			default:
				throw new UnsupportedOperationException(Msg.code(710) + "Unsupported FHIR version '"
						+ fhirVersion.getFhirVersionString() + "'. Only DSTU3 and R4 are supported.");
		}
		return null;
	}

	private LookupCodeResult generateLookupCodeResultDSTU3(
			String theCode, String theSystem, org.hl7.fhir.dstu3.model.Parameters outcomeDSTU3) {
		// NOTE: I wanted to put all of this logic into the IValidationSupport Class, but it would've required adding
		//       several new dependencies on version-specific libraries and that is explicitly forbidden (see comment in
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
					org.hl7.fhir.dstu3.model.Property part = parameterComponent.getChildByName("part");
					// The assumption here is that we may only have 2 elements in this part, and if so, these 2 will be
					// saved
					if (part == null || part.getValues().size() < 2) {
						continue;
					}
					BaseConceptProperty conceptProperty = createBaseConceptPropertyDstu3(part.getValues());
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
			}
		}
		return result;
	}

	private static BaseConceptProperty createBaseConceptPropertyDstu3(List<org.hl7.fhir.dstu3.model.Base> theValues) {
		org.hl7.fhir.dstu3.model.Parameters.ParametersParameterComponent part1 =
				(org.hl7.fhir.dstu3.model.Parameters.ParametersParameterComponent) theValues.get(0);
		String propertyName = ((org.hl7.fhir.dstu3.model.CodeType) part1.getValue()).getValue();

		BaseConceptProperty conceptProperty = null;
		org.hl7.fhir.dstu3.model.Parameters.ParametersParameterComponent part2 =
				(org.hl7.fhir.dstu3.model.Parameters.ParametersParameterComponent) theValues.get(1);

		org.hl7.fhir.dstu3.model.Type value = part2.getValue();
		if (value == null) {
			return conceptProperty;
		}
		String fhirType = value.fhirType();
		switch (fhirType) {
			case TYPE_STRING:
				org.hl7.fhir.dstu3.model.StringType stringType = (org.hl7.fhir.dstu3.model.StringType) part2.getValue();
				conceptProperty = new StringConceptProperty(propertyName, stringType.getValue());
				break;
			case TYPE_CODING:
				org.hl7.fhir.dstu3.model.Coding coding = (org.hl7.fhir.dstu3.model.Coding) part2.getValue();
				conceptProperty = new CodingConceptProperty(
						propertyName, coding.getSystem(), coding.getCode(), coding.getDisplay());
				break;
			default:
				conceptProperty = new StringConceptProperty(propertyName, value.toString());
				// throw new InternalErrorException(Msg.code(2450) + "Property type " + fhirType + " is not supported.");
		}
		return conceptProperty;
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
			default:
				conceptProperty = new StringConceptProperty(theName, theValue.toString());
				// throw new InternalErrorException(Msg.code(2451) + "Property type " + fhirType + " is not supportedis not supported.");
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
					Property part = parameterComponent.getChildByName("part");
					// The assumption here is that we may only have 2 elements in this part, and if so, these 2 will be
					// saved
					if (part == null || part.getValues().size() < 2) {
						continue;
					}
					BaseConceptProperty conceptProperty = createBaseConceptPropertyR4(part.getValues());
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
			}
		}
		return result;
	}

	private static BaseConceptProperty createBaseConceptPropertyR4(List<Base> values) {
		ParametersParameterComponent part1 = (ParametersParameterComponent) values.get(0);
		String propertyName = ((CodeType) part1.getValue()).getValue();

		ParametersParameterComponent part2 = (ParametersParameterComponent) values.get(1);

		Type value = part2.getValue();
		if (value == null) {
			return null;
		}
		BaseConceptProperty conceptProperty;
		String fhirType = value.fhirType();
		switch (fhirType) {
			case IValidationSupport.TYPE_STRING:
				StringType stringType = (StringType) part2.getValue();
				conceptProperty = new StringConceptProperty(propertyName, stringType.getValue());
				break;
			case IValidationSupport.TYPE_CODING:
				Coding coding = (Coding) part2.getValue();
				conceptProperty = new CodingConceptProperty(
						propertyName, coding.getSystem(), coding.getCode(), coding.getDisplay());
				break;
			default:
				conceptProperty = new StringConceptProperty(propertyName, value.toString());
				// throw new InternalErrorException(Msg.code(2452) + "Property type " + fhirType + " is not supported.");
		}
		return conceptProperty;
	}

	private static BaseConceptProperty createConceptPropertyR4(final String theName, final Type theValue) {
		BaseConceptProperty conceptProperty;

		String fhirType = theValue.fhirType();
		switch (fhirType) {
			case IValidationSupport.TYPE_STRING:
				StringType stringType = (StringType) theValue;
				conceptProperty = new StringConceptProperty(theName, stringType.getValue());
				break;
			case IValidationSupport.TYPE_CODING:
				Coding coding = (Coding) theValue;
				conceptProperty =
						new CodingConceptProperty(theName, coding.getSystem(), coding.getCode(), coding.getDisplay());
				break;
			default:
				conceptProperty = new StringConceptProperty(theName, theValue.toString());
				// throw new InternalErrorException(Msg.code(2453) + "Property type " + fhirType + " is not supported.");
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

	protected CodeValidationResult invokeRemoteValidateCode(
			String theCodeSystem, String theCode, String theDisplay, String theValueSetUrl, IBaseResource theValueSet) {
		if (isBlank(theCode)) {
			return null;
		}

		IGenericClient client = provideClient();

		IBaseParameters input =
				buildValidateCodeInputParameters(theCodeSystem, theCode, theDisplay, theValueSetUrl, theValueSet);

		String resourceType = "ValueSet";
		if (theValueSet == null && theValueSetUrl == null) {
			resourceType = "CodeSystem";
		}

		IBaseParameters output = client.operation()
				.onType(resourceType)
				.named("validate-code")
				.withParameters(input)
				.execute();

		List<String> resultValues = ParametersUtil.getNamedParameterValuesAsString(getFhirContext(), output, "result");
		if (resultValues.isEmpty() || isBlank(resultValues.get(0))) {
			return null;
		}
		Validate.isTrue(resultValues.size() == 1, "Response contained %d 'result' values", resultValues.size());

		boolean success = "true".equalsIgnoreCase(resultValues.get(0));

		CodeValidationResult retVal = new CodeValidationResult();
		if (success) {

			retVal.setCode(theCode);
			List<String> displayValues =
					ParametersUtil.getNamedParameterValuesAsString(getFhirContext(), output, "display");
			if (!displayValues.isEmpty()) {
				retVal.setDisplay(displayValues.get(0));
			}

		} else {

			retVal.setSeverity(IssueSeverity.ERROR);
			List<String> messageValues =
					ParametersUtil.getNamedParameterValuesAsString(getFhirContext(), output, "message");
			if (!messageValues.isEmpty()) {
				retVal.setMessage(messageValues.get(0));
			}
		}
		return retVal;
	}

	protected IBaseParameters buildValidateCodeInputParameters(
			String theCodeSystem, String theCode, String theDisplay, String theValueSetUrl, IBaseResource theValueSet) {
		IBaseParameters params = ParametersUtil.newInstance(getFhirContext());

		if (theValueSet == null && theValueSetUrl == null) {
			ParametersUtil.addParameterToParametersUri(getFhirContext(), params, "url", theCodeSystem);
			ParametersUtil.addParameterToParametersString(getFhirContext(), params, "code", theCode);
			if (isNotBlank(theDisplay)) {
				ParametersUtil.addParameterToParametersString(getFhirContext(), params, "display", theDisplay);
			}
			return params;
		}

		if (isNotBlank(theValueSetUrl)) {
			ParametersUtil.addParameterToParametersUri(getFhirContext(), params, "url", theValueSetUrl);
		}
		ParametersUtil.addParameterToParametersString(getFhirContext(), params, "code", theCode);
		if (isNotBlank(theCodeSystem)) {
			ParametersUtil.addParameterToParametersUri(getFhirContext(), params, "system", theCodeSystem);
		}
		if (isNotBlank(theDisplay)) {
			ParametersUtil.addParameterToParametersString(getFhirContext(), params, "display", theDisplay);
		}
		if (theValueSet != null) {
			ParametersUtil.addParameterToParameters(getFhirContext(), params, "valueSet", theValueSet);
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
