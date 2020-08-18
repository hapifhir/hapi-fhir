package org.hl7.fhir.common.hapi.validation.support;

import ca.uhn.fhir.context.FhirContext;
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
import org.hl7.fhir.r4.model.CodeSystem;

import javax.annotation.Nonnull;
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

		if (theOptions != null) {
			if (theOptions.isInferSystem()) {
				return null;
			}
		}

		IBaseResource valueSet = theValueSet;
		String valueSetUrl = DefaultProfileValidationSupport.getConformanceResourceUrl(myCtx, valueSet);
		if (isNotBlank(valueSetUrl)) {
			valueSet = null;
		} else {
			valueSetUrl = null;
		}
		return invokeRemoteValidateCode(theCodeSystem, theCode, theDisplay, valueSetUrl, valueSet);
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
