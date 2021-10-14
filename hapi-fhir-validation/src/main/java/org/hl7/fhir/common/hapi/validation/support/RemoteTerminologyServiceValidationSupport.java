package org.hl7.fhir.common.hapi.validation.support;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.support.ConceptValidationOptions;
import ca.uhn.fhir.context.support.DefaultProfileValidationSupport;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.ValidationSupportContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.util.BundleUtil;
import ca.uhn.fhir.util.ParametersUtil;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.ValueSet;
import org.thymeleaf.util.StringUtils;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * This class is an implementation of {@link IValidationSupport} that fetches validation codes
 * from a remote FHIR based terminology server. It will invoke the FHIR
 * <a href="http://hl7.org/fhir/valueset-operation-validate-code.html">ValueSet/$validate-code</a>
 * operation in order to validate codes.
 */
public class RemoteTerminologyServiceValidationSupport extends BaseValidationSupport implements IValidationSupport {

	private String myBaseUrl;
	private List<Object> myClientInterceptors = new ArrayList<>();

	/**
	 * Constructor
	 *
	 * @param theFhirContext The FhirContext object to use
	 */
	public RemoteTerminologyServiceValidationSupport(FhirContext theFhirContext) {
		super(theFhirContext);
	}

	@Override
	public CodeValidationResult validateCode(ValidationSupportContext theValidationSupportContext, ConceptValidationOptions theOptions, String theCodeSystem, String theCode, String theDisplay, String theValueSetUrl) {
		return invokeRemoteValidateCode(theCodeSystem, theCode, theDisplay, theValueSetUrl, null);
	}

	@Override
	public CodeValidationResult validateCodeInValueSet(ValidationSupportContext theValidationSupportContext, ConceptValidationOptions theOptions, String theCodeSystem, String theCode, String theDisplay, @Nonnull IBaseResource theValueSet) {

		IBaseResource valueSet = theValueSet;

		// some external validators require the system when the code is passed
		// so let's try to get it from the VS if is is not present
		String codeSystem = theCodeSystem;
		if (isNotBlank(theCode) && isBlank(codeSystem)) {
			ValueSet vs = (ValueSet) theValueSet;
			if ( vs.getCompose() != null && vs.getCompose().getInclude() != null && vs.getCompose().getInclude().size() > 0) {
				codeSystem = vs.getCompose().getInclude().iterator().next().getSystem();
			}
		}

	 	// Remote terminology services shouldn't be used to validate codes with an implied system
		if (isBlank(codeSystem)) { return null; }

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
		IGenericClient client = provideClient();
		Class<? extends IBaseBundle> bundleType = myCtx.getResourceDefinition("Bundle").getImplementingClass(IBaseBundle.class);
		IBaseBundle results = client
			.search()
			.forResource("CodeSystem")
			.where(CodeSystem.URL.matches().value(theSystem))
			.returnBundle(bundleType)
			.execute();
		List<IBaseResource> resultsList = BundleUtil.toListOfResources(myCtx, results);
		if (resultsList.size() > 0) {
			return resultsList.get(0);
		}

		return null;
	}

	@Override
	public LookupCodeResult lookupCode(ValidationSupportContext theValidationSupportContext, String theSystem, String theCode, String theDisplayLanguage) {
		Validate.notBlank(theCode, "theCode must be provided");

		IGenericClient client = provideClient();
		FhirVersionEnum fhirVersion = client.getFhirContext().getVersion().getVersion();

		switch (fhirVersion) {
			case DSTU3:
				org.hl7.fhir.dstu3.model.Parameters paramsDSTU3 = new org.hl7.fhir.dstu3.model.Parameters();
				paramsDSTU3.addParameter().setName("code").setValue(new org.hl7.fhir.dstu3.model.StringType(theCode));
				if (!StringUtils.isEmpty(theSystem)) {
					paramsDSTU3.addParameter().setName("system").setValue(new org.hl7.fhir.dstu3.model.UriType(theSystem));
				}
				if (!StringUtils.isEmpty(theDisplayLanguage)) {
					paramsDSTU3.addParameter().setName("language").setValue(new org.hl7.fhir.dstu3.model.StringType(theDisplayLanguage));
				}
				org.hl7.fhir.dstu3.model.Parameters outcomeDSTU3 = client
					.operation()
					.onType(org.hl7.fhir.dstu3.model.CodeSystem.class)
					.named("$lookup")
					.withParameters(paramsDSTU3)
					.useHttpGet()
					.execute();
				if (outcomeDSTU3 != null && !outcomeDSTU3.isEmpty()) {
					return generateLookupCodeResultDSTU3(theCode, theSystem, outcomeDSTU3);
				}
				break;
			case R4:
				org.hl7.fhir.r4.model.Parameters paramsR4 = new org.hl7.fhir.r4.model.Parameters();
				paramsR4.addParameter().setName("code").setValue(new org.hl7.fhir.r4.model.StringType(theCode));
				if (!StringUtils.isEmpty(theSystem)) {
					paramsR4.addParameter().setName("system").setValue(new org.hl7.fhir.r4.model.UriType(theSystem));
				}
				if (!StringUtils.isEmpty(theDisplayLanguage)) {
					paramsR4.addParameter().setName("language").setValue(new org.hl7.fhir.r4.model.StringType(theDisplayLanguage));
				}
				org.hl7.fhir.r4.model.Parameters outcomeR4 = client
					.operation()
					.onType(CodeSystem.class)
					.named("$lookup")
					.withParameters(paramsR4)
					.useHttpGet()
					.execute();
				if (outcomeR4 != null && !outcomeR4.isEmpty()) {
					return generateLookupCodeResultR4(theCode, theSystem, outcomeR4);
				}
				break;
			default:
				throw new UnsupportedOperationException("Unsupported FHIR version '" + fhirVersion.getFhirVersionString() +
					"'. Only DSTU3 and R4 are supported.");
		}

		return null;
	}

	private LookupCodeResult generateLookupCodeResultDSTU3(String theCode, String theSystem, org.hl7.fhir.dstu3.model.Parameters outcomeDSTU3) {
		// NOTE: I wanted to put all of this logic into the IValidationSupport Class, but it would've required adding
		//       several new dependencies on version-specific libraries and that is explicitly forbidden (see comment in POM).
		LookupCodeResult result = new LookupCodeResult();
		result.setSearchedForCode(theCode);
		result.setSearchedForSystem(theSystem);
		result.setFound(true);
		for (org.hl7.fhir.dstu3.model.Parameters.ParametersParameterComponent parameterComponent : outcomeDSTU3.getParameter()) {
			switch (parameterComponent.getName()) {
				case "property":
					org.hl7.fhir.dstu3.model.Property part = parameterComponent.getChildByName("part");
					// The assumption here is that we may only have 2 elements in this part, and if so, these 2 will be saved
					if (part != null && part.hasValues() && part.getValues().size() >= 2) {
						String key = ((org.hl7.fhir.dstu3.model.Parameters.ParametersParameterComponent) part.getValues().get(0)).getValue().toString();
						String value = ((org.hl7.fhir.dstu3.model.Parameters.ParametersParameterComponent) part.getValues().get(1)).getValue().toString();
						if (!StringUtils.isEmpty(key) && !StringUtils.isEmpty(value)) {
							result.getProperties().add(new StringConceptProperty(key, value));
						}
					}
					break;
				case "designation":
					ConceptDesignation conceptDesignation = new ConceptDesignation();
					for (org.hl7.fhir.dstu3.model.Parameters.ParametersParameterComponent designationComponent : parameterComponent.getPart()) {
						switch(designationComponent.getName()) {
							case "language":
								conceptDesignation.setLanguage(designationComponent.getValue().toString());
								break;
							case "use":
								org.hl7.fhir.dstu3.model.Coding coding = (org.hl7.fhir.dstu3.model.Coding)designationComponent.getValue();
								if (coding != null) {
									conceptDesignation.setUseSystem(coding.getSystem());
									conceptDesignation.setUseCode(coding.getCode());
									conceptDesignation.setUseDisplay(coding.getDisplay());
								}
								break;
							case "value":
								conceptDesignation.setValue(((designationComponent.getValue() == null)?null:designationComponent.getValue().toString()));
								break;
						}
					}
					result.getDesignations().add(conceptDesignation);
					break;
				case "name":
					result.setCodeSystemDisplayName(((parameterComponent.getValue() == null)?null:parameterComponent.getValue().toString()));
					break;
				case "version":
					result.setCodeSystemVersion(((parameterComponent.getValue() == null)?null:parameterComponent.getValue().toString()));
					break;
				case "display":
					result.setCodeDisplay(((parameterComponent.getValue() == null)?null:parameterComponent.getValue().toString()));
					break;
				case "abstract":
					result.setCodeIsAbstract(((parameterComponent.getValue() == null)?false:Boolean.parseBoolean(parameterComponent.getValue().toString())));
					break;
			}
		}
		return result;
	}

	private LookupCodeResult generateLookupCodeResultR4(String theCode, String theSystem, org.hl7.fhir.r4.model.Parameters outcomeR4) {
		// NOTE: I wanted to put all of this logic into the IValidationSupport Class, but it would've required adding
		//       several new dependencies on version-specific libraries and that is explicitly forbidden (see comment in POM).
		LookupCodeResult result = new LookupCodeResult();
		result.setSearchedForCode(theCode);
		result.setSearchedForSystem(theSystem);
		result.setFound(true);
		for (org.hl7.fhir.r4.model.Parameters.ParametersParameterComponent parameterComponent : outcomeR4.getParameter()) {
			switch (parameterComponent.getName()) {
				case "property":
					org.hl7.fhir.r4.model.Property part = parameterComponent.getChildByName("part");
					// The assumption here is that we may only have 2 elements in this part, and if so, these 2 will be saved
					if (part != null && part.hasValues() && part.getValues().size() >= 2) {
						String key = ((org.hl7.fhir.r4.model.Parameters.ParametersParameterComponent) part.getValues().get(0)).getValue().toString();
						String value = ((org.hl7.fhir.r4.model.Parameters.ParametersParameterComponent) part.getValues().get(1)).getValue().toString();
						if (!StringUtils.isEmpty(key) && !StringUtils.isEmpty(value)) {
							result.getProperties().add(new StringConceptProperty(key, value));
						}
					}
					break;
				case "designation":
					ConceptDesignation conceptDesignation = new ConceptDesignation();
					for (org.hl7.fhir.r4.model.Parameters.ParametersParameterComponent designationComponent : parameterComponent.getPart()) {
						switch(designationComponent.getName()) {
							case "language":
								conceptDesignation.setLanguage(designationComponent.getValue().toString());
								break;
							case "use":
								org.hl7.fhir.r4.model.Coding coding = (org.hl7.fhir.r4.model.Coding)designationComponent.getValue();
								if (coding != null) {
									conceptDesignation.setUseSystem(coding.getSystem());
									conceptDesignation.setUseCode(coding.getCode());
									conceptDesignation.setUseDisplay(coding.getDisplay());
								}
								break;
							case "value":
								conceptDesignation.setValue(((designationComponent.getValue() == null)?null:designationComponent.getValue().toString()));
								break;
						}
					}
					result.getDesignations().add(conceptDesignation);
					break;
				case "name":
					result.setCodeSystemDisplayName(((parameterComponent.getValue() == null)?null:parameterComponent.getValue().toString()));
					break;
				case "version":
					result.setCodeSystemVersion(((parameterComponent.getValue() == null)?null:parameterComponent.getValue().toString()));
					break;
				case "display":
					result.setCodeDisplay(((parameterComponent.getValue() == null)?null:parameterComponent.getValue().toString()));
					break;
				case "abstract":
					result.setCodeIsAbstract(((parameterComponent.getValue() == null)?false:Boolean.parseBoolean(parameterComponent.getValue().toString())));
					break;
			}
		}
		return result;
	}

	@Override
	public IBaseResource fetchValueSet(String theValueSetUrl) {
		IGenericClient client = provideClient();
		Class<? extends IBaseBundle> bundleType = myCtx.getResourceDefinition("Bundle").getImplementingClass(IBaseBundle.class);
		IBaseBundle results = client
			.search()
			.forResource("ValueSet")
			.where(CodeSystem.URL.matches().value(theValueSetUrl))
			.returnBundle(bundleType)
			.execute();
		List<IBaseResource> resultsList = BundleUtil.toListOfResources(myCtx, results);
		if (resultsList.size() > 0) {
			return resultsList.get(0);
		}

		return null;
	}

	@Override
	public boolean isCodeSystemSupported(ValidationSupportContext theValidationSupportContext, String theSystem) {
		return fetchCodeSystem(theSystem) != null;
	}

	@Override
	public boolean isValueSetSupported(ValidationSupportContext theValidationSupportContext, String theValueSetUrl) {
		return fetchValueSet(theValueSetUrl) != null;
	}

	private IGenericClient provideClient() {
		IGenericClient retVal = myCtx.newRestfulGenericClient(myBaseUrl);
		for (Object next : myClientInterceptors) {
			retVal.registerInterceptor(next);
		}
		return retVal;
	}

	protected CodeValidationResult invokeRemoteValidateCode(String theCodeSystem, String theCode, String theDisplay, String theValueSetUrl, IBaseResource theValueSet) {
		if (isBlank(theCode)) {
			return null;
		}

		IGenericClient client = provideClient();

		IBaseParameters input = ParametersUtil.newInstance(getFhirContext());

		String resourceType = "ValueSet";
		if (theValueSet == null && theValueSetUrl == null) {
			resourceType = "CodeSystem";

			ParametersUtil.addParameterToParametersUri(getFhirContext(), input, "url", theCodeSystem);
			ParametersUtil.addParameterToParametersString(getFhirContext(), input, "code", theCode);
			if (isNotBlank(theDisplay)) {
				ParametersUtil.addParameterToParametersString(getFhirContext(), input, "display", theDisplay);
			}

		} else {

			if (isNotBlank(theValueSetUrl)) {
				ParametersUtil.addParameterToParametersUri(getFhirContext(), input, "url", theValueSetUrl);
			}
			ParametersUtil.addParameterToParametersString(getFhirContext(), input, "code", theCode);
			if (isNotBlank(theCodeSystem)) {
				ParametersUtil.addParameterToParametersUri(getFhirContext(), input, "system", theCodeSystem);
			}
			if (isNotBlank(theDisplay)) {
				ParametersUtil.addParameterToParametersString(getFhirContext(), input, "display", theDisplay);
			}
			if (theValueSet != null) {
				ParametersUtil.addParameterToParameters(getFhirContext(), input, "valueSet", theValueSet);
			}

		}


		IBaseParameters output = client
			.operation()
			.onType(resourceType)
			.named("validate-code")
			.withParameters(input)
			.execute();

		List<String> resultValues = ParametersUtil.getNamedParameterValuesAsString(getFhirContext(), output, "result");
		if (resultValues.size() < 1 || isBlank(resultValues.get(0))) {
			return null;
		}
		Validate.isTrue(resultValues.size() == 1, "Response contained %d 'result' values", resultValues.size());

		boolean success = "true".equalsIgnoreCase(resultValues.get(0));

		CodeValidationResult retVal = new CodeValidationResult();
		if (success) {

			retVal.setCode(theCode);
			List<String> displayValues = ParametersUtil.getNamedParameterValuesAsString(getFhirContext(), output, "display");
			if (displayValues.size() > 0) {
				retVal.setDisplay(displayValues.get(0));
			}

		} else {

			retVal.setSeverity(IssueSeverity.ERROR);
			List<String> messageValues = ParametersUtil.getNamedParameterValuesAsString(getFhirContext(), output, "message");
			if (messageValues.size() > 0) {
				retVal.setMessage(messageValues.get(0));
			}

		}
		return retVal;
	}

	/**
	 * Sets the FHIR Terminology Server base URL
	 *
	 * @param theBaseUrl The base URL, e.g. "https://hapi.fhir.org/baseR4"
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
