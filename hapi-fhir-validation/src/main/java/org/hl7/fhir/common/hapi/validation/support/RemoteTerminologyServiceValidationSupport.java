package org.hl7.fhir.common.hapi.validation.support;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.ConceptValidationOptions;
import ca.uhn.fhir.context.support.DefaultProfileValidationSupport;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.ValidationSupportContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.util.BundleUtil;
import ca.uhn.fhir.util.JsonUtil;
import ca.uhn.fhir.util.ParametersUtil;
import org.apache.commons.lang3.Validate;
import org.checkerframework.framework.qual.InvisibleQualifier;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.ValueSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
			codeSystem = extractCodeSystemForCode((ValueSet) theValueSet, theCode);
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

	/**
	 * Try to obtain the codeSystem of the received code from the received ValueSet
	 */
	private String extractCodeSystemForCode(ValueSet theValueSet, String theCode) {
		if (theValueSet.getCompose() == null || theValueSet.getCompose().getInclude() == null
					|| theValueSet.getCompose().getInclude().isEmpty()) {
			return null;
		}

		if (theValueSet.getCompose().getInclude().size() == 1) {
			ValueSet.ConceptSetComponent include = theValueSet.getCompose().getInclude().iterator().next();
			return getVersionedCodeSystem(include);
		}

		// when component has more than one include, their codeSystem(s) could be different, so we need to make sure
		// that we are picking up the system for the include to which the code corresponds
		for (ValueSet.ConceptSetComponent include: theValueSet.getCompose().getInclude()) {
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
		try {
			ourLog.trace("CodeSystem couldn't be extracted for code: {} for ValueSet: {}",
				theCode, JsonUtil.serialize(theValueSet));
		} catch (IOException theE) {
			ourLog.error("IOException trying to serialize ValueSet to json: " + theE);
		}

		return null;
	}


	private String getVersionedCodeSystem(ValueSet.ConceptSetComponent theComponent) {
			String codeSystem = theComponent.getSystem();
			if ( ! codeSystem.contains("|") && theComponent.hasVersion()) {
				codeSystem += "|" + theComponent.getVersion();
			}
			return codeSystem;
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

		IBaseParameters input = buildValidateCodeInputParameters(theCodeSystem, theCode, theDisplay, theValueSetUrl, theValueSet);

		String resourceType = "ValueSet";
		if (theValueSet == null && theValueSetUrl == null) {
			resourceType = "CodeSystem";
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

	protected IBaseParameters buildValidateCodeInputParameters(String theCodeSystem, String theCode, String theDisplay, String theValueSetUrl, IBaseResource theValueSet) {
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
