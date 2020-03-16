package org.hl7.fhir.common.hapi.validation.support;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.ConceptValidationOptions;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.util.ParametersUtil;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IBaseResource;

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
	public CodeValidationResult validateCode(IValidationSupport theRootValidationSupport, ConceptValidationOptions theOptions, String theCodeSystem, String theCode, String theDisplay, String theValueSetUrl) {
		return invokeRemoteValidateCode(theCodeSystem, theCode, theDisplay, theValueSetUrl, null);
	}

	private IGenericClient provideClient() {
		IGenericClient retVal = myCtx.newRestfulGenericClient(myBaseUrl);
		for (Object next : myClientInterceptors) {
			retVal.registerInterceptor(next);
		}
		return retVal;
	}

	@Override
	public CodeValidationResult validateCodeInValueSet(IValidationSupport theRootValidationSupport, ConceptValidationOptions theOptions, String theCodeSystem, String theCode, String theDisplay, @Nonnull IBaseResource theValueSet) {
		return invokeRemoteValidateCode(theCodeSystem, theCode, theDisplay, null, theValueSet);
	}

	protected CodeValidationResult invokeRemoteValidateCode(String theCodeSystem, String theCode, String theDisplay, String theValueSetUrl, IBaseResource theValueSet) {
		if (isBlank(theCode)) {
			return null;
		}

		IGenericClient client = provideClient();

		IBaseParameters input = ParametersUtil.newInstance(getFhirContext());

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

		IBaseParameters output = client
			.operation()
			.onType("ValueSet")
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
