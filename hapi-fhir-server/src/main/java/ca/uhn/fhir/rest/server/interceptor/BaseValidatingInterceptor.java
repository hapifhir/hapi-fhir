package ca.uhn.fhir.rest.server.interceptor;

/*
 * #%L
 * HAPI FHIR - Server Framework
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.util.OperationOutcomeUtil;
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.IValidatorModule;
import ca.uhn.fhir.validation.ResultSeverityEnum;
import ca.uhn.fhir.validation.SingleValidationMessage;
import ca.uhn.fhir.validation.ValidationResult;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.text.StrLookup;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * This interceptor intercepts each incoming request and if it contains a FHIR resource, validates that resource. The
 * interceptor may be configured to run any validator modules, and will then add headers to the response or fail the
 * request with an {@link UnprocessableEntityException HTTP 422 Unprocessable Entity}.
 */
@Interceptor
public abstract class BaseValidatingInterceptor<T> extends ValidationResultEnrichingInterceptor {

	/**
	 * Default value:<br/>
	 * <code>
	 * ${row}:${col} ${severity} ${message} (${location})
	 * </code>
	 */
	public static final String DEFAULT_RESPONSE_HEADER_VALUE = "${row}:${col} ${severity} ${message} (${location})";

	private static final Logger ourLog = LoggerFactory.getLogger(BaseValidatingInterceptor.class);

	private Integer myAddResponseIssueHeaderOnSeverity = null;
	private Integer myAddResponseOutcomeHeaderOnSeverity = null;
	private Integer myFailOnSeverity = ResultSeverityEnum.ERROR.ordinal();
	private boolean myIgnoreValidatorExceptions;
	private int myMaximumHeaderLength = 200;
	private String myResponseIssueHeaderName = provideDefaultResponseHeaderName();
	private String myResponseIssueHeaderValue = DEFAULT_RESPONSE_HEADER_VALUE;
	private String myResponseIssueHeaderValueNoIssues = null;
	private String myResponseOutcomeHeaderName = provideDefaultResponseHeaderName();

	private List<IValidatorModule> myValidatorModules;
	private FhirValidator myValidator;

	private void addResponseIssueHeader(RequestDetails theRequestDetails, SingleValidationMessage theNext) {
		// Perform any string substitutions from the message format
		StrLookup<?> lookup = new MyLookup(theNext);
		StrSubstitutor subs = new StrSubstitutor(lookup, "${", "}", '\\');

		// Log the header
		String headerValue = subs.replace(myResponseIssueHeaderValue);
		ourLog.trace("Adding header to response: {}", headerValue);

		theRequestDetails.getResponse().addHeader(myResponseIssueHeaderName, headerValue);
	}

	/**
	 * Specify a validator module to use.
	 *
	 * @see #setValidator(FhirValidator)
	 */
	public BaseValidatingInterceptor<T> addValidatorModule(IValidatorModule theModule) {
		Validate.notNull(theModule, "theModule must not be null");
		Validate.isTrue(myValidator == null, "Can not specify both a validator and validator modules. Only one needs to be supplied.");
		if (getValidatorModules() == null) {
			setValidatorModules(new ArrayList<>());
		}
		getValidatorModules().add(theModule);
		return this;
	}

	/**
	 * Provides the validator to use. This can be used as an alternative to {@link #addValidatorModule(IValidatorModule)}
	 *
	 * @see #addValidatorModule(IValidatorModule)
	 * @see #setValidatorModules(List)
	 */
	public void setValidator(FhirValidator theValidator) {
		Validate.isTrue(theValidator == null || getValidatorModules() == null || getValidatorModules().isEmpty(), "Can not specify both a validator and validator modules. Only one needs to be supplied.");
		myValidator = theValidator;
	}


	abstract ValidationResult doValidate(FhirValidator theValidator, T theRequest);

	/**
	 * Fail the request by throwing an {@link UnprocessableEntityException} as a result of a validation failure.
	 * Subclasses may change this behaviour by providing alternate behaviour.
	 */
	protected void fail(RequestDetails theRequestDetails, ValidationResult theValidationResult) {
		throw new UnprocessableEntityException(Msg.code(330) + theValidationResult.getMessages().get(0).getMessage(), theValidationResult.toOperationOutcome());
	}

	/**
	 * If the validation produces a result with at least the given severity, a header with the name
	 * specified by {@link #setResponseOutcomeHeaderName(String)} will be added containing a JSON encoded
	 * OperationOutcome resource containing the validation results.
	 */
	public ResultSeverityEnum getAddResponseOutcomeHeaderOnSeverity() {
		return myAddResponseOutcomeHeaderOnSeverity != null ? ResultSeverityEnum.values()[myAddResponseOutcomeHeaderOnSeverity] : null;
	}

	/**
	 * If the validation produces a result with at least the given severity, a header with the name
	 * specified by {@link #setResponseOutcomeHeaderName(String)} will be added containing a JSON encoded
	 * OperationOutcome resource containing the validation results.
	 */
	public void setAddResponseOutcomeHeaderOnSeverity(ResultSeverityEnum theAddResponseOutcomeHeaderOnSeverity) {
		myAddResponseOutcomeHeaderOnSeverity = theAddResponseOutcomeHeaderOnSeverity != null ? theAddResponseOutcomeHeaderOnSeverity.ordinal() : null;
	}

	/**
	 * The maximum length for an individual header. If an individual header would be written exceeding this length,
	 * the header value will be truncated.
	 */
	public int getMaximumHeaderLength() {
		return myMaximumHeaderLength;
	}

	/**
	 * The maximum length for an individual header. If an individual header would be written exceeding this length,
	 * the header value will be truncated. Value must be greater than 100.
	 */
	public void setMaximumHeaderLength(int theMaximumHeaderLength) {
		Validate.isTrue(theMaximumHeaderLength >= 100, "theMaximumHeadeerLength must be >= 100");
		myMaximumHeaderLength = theMaximumHeaderLength;
	}

	/**
	 * The name of the header specified by {@link #setAddResponseOutcomeHeaderOnSeverity(ResultSeverityEnum)}
	 */
	public String getResponseOutcomeHeaderName() {
		return myResponseOutcomeHeaderName;
	}

	/**
	 * The name of the header specified by {@link #setAddResponseOutcomeHeaderOnSeverity(ResultSeverityEnum)}
	 */
	public void setResponseOutcomeHeaderName(String theResponseOutcomeHeaderName) {
		Validate.notEmpty(theResponseOutcomeHeaderName, "theResponseOutcomeHeaderName can not be empty or null");
		myResponseOutcomeHeaderName = theResponseOutcomeHeaderName;
	}

	public List<IValidatorModule> getValidatorModules() {
		return myValidatorModules;
	}

	public void setValidatorModules(List<IValidatorModule> theValidatorModules) {
		Validate.isTrue(myValidator == null || theValidatorModules == null || theValidatorModules.isEmpty(), "Can not specify both a validator and validator modules. Only one needs to be supplied.");
		myValidatorModules = theValidatorModules;
	}

	/**
	 * If set to <code>true</code> (default is <code>false</code>) this interceptor
	 * will exit immediately and allow processing to continue if the validator throws
	 * any exceptions.
	 * <p>
	 * This setting is mostly useful in testing situations
	 * </p>
	 */
	public boolean isIgnoreValidatorExceptions() {
		return myIgnoreValidatorExceptions;
	}

	/**
	 * If set to <code>true</code> (default is <code>false</code>) this interceptor
	 * will exit immediately and allow processing to continue if the validator throws
	 * any exceptions.
	 * <p>
	 * This setting is mostly useful in testing situations
	 * </p>
	 */
	public void setIgnoreValidatorExceptions(boolean theIgnoreValidatorExceptions) {
		myIgnoreValidatorExceptions = theIgnoreValidatorExceptions;
	}

	abstract String provideDefaultResponseHeaderName();

	/**
	 * Sets the minimum severity at which an issue detected by the validator will result in a header being added to the
	 * response. Default is {@link ResultSeverityEnum#INFORMATION}. Set to <code>null</code> to disable this behaviour.
	 *
	 * @see #setResponseHeaderName(String)
	 * @see #setResponseHeaderValue(String)
	 */
	public void setAddResponseHeaderOnSeverity(ResultSeverityEnum theSeverity) {
		myAddResponseIssueHeaderOnSeverity = theSeverity != null ? theSeverity.ordinal() : null;
	}

	/**
	 * Sets the minimum severity at which an issue detected by the validator will fail/reject the request. Default is
	 * {@link ResultSeverityEnum#ERROR}. Set to <code>null</code> to disable this behaviour.
	 */
	public void setFailOnSeverity(ResultSeverityEnum theSeverity) {
		myFailOnSeverity = theSeverity != null ? theSeverity.ordinal() : null;
	}

	/**
	 * Sets the name of the response header to add validation failures to
	 *
	 * @see #setAddResponseHeaderOnSeverity(ResultSeverityEnum)
	 */
	protected void setResponseHeaderName(String theResponseHeaderName) {
		Validate.notBlank(theResponseHeaderName, "theResponseHeaderName must not be blank or null");
		myResponseIssueHeaderName = theResponseHeaderName;
	}

	/**
	 * Sets the value to add to the response header with the name specified by {@link #setResponseHeaderName(String)}
	 * when validation produces a message of severity equal to or greater than
	 * {@link #setAddResponseHeaderOnSeverity(ResultSeverityEnum)}
	 * <p>
	 * This field allows the following substitutions:
	 * </p>
	 * <table>
	 * <tr>
	 * <td>Name</td>
	 * <td>Value</td>
	 * </tr>
	 * <tr>
	 * <td>${line}</td>
	 * <td>The line in the request</td>
	 * </tr>
	 * <tr>
	 * <td>${col}</td>
	 * <td>The column in the request</td>
	 * </tr>
	 * <tr>
	 * <td>${location}</td>
	 * <td>The location in the payload as a string (typically this will be a path)</td>
	 * </tr>
	 * <tr>
	 * <td>${severity}</td>
	 * <td>The severity of the issue</td>
	 * </tr>
	 * <tr>
	 * <td>${message}</td>
	 * <td>The validation message</td>
	 * </tr>
	 * </table>
	 *
	 * @see #DEFAULT_RESPONSE_HEADER_VALUE
	 * @see #setAddResponseHeaderOnSeverity(ResultSeverityEnum)
	 */
	public void setResponseHeaderValue(String theResponseHeaderValue) {
		Validate.notBlank(theResponseHeaderValue, "theResponseHeaderValue must not be blank or null");
		myResponseIssueHeaderValue = theResponseHeaderValue;
	}

	/**
	 * Sets the header value to add when no issues are found at or exceeding the
	 * threshold specified by {@link #setAddResponseHeaderOnSeverity(ResultSeverityEnum)}
	 */
	public void setResponseHeaderValueNoIssues(String theResponseHeaderValueNoIssues) {
		myResponseIssueHeaderValueNoIssues = theResponseHeaderValueNoIssues;
	}

	/**
	 * Hook for subclasses (e.g. add a tag (coding) to an incoming resource when a given severity appears in the
	 * ValidationResult).
	 */
	protected void postProcessResult(RequestDetails theRequestDetails, ValidationResult theValidationResult) {
	}

	/**
	 * Hook for subclasses on failure (e.g. add a response header to an incoming resource upon rejection).
	 */
	protected void postProcessResultOnFailure(RequestDetails theRequestDetails, ValidationResult theValidationResult) {
	}

	/**
	 * Note: May return null
	 */
	protected ValidationResult validate(T theRequest, RequestDetails theRequestDetails) {
		if (theRequest == null || theRequestDetails == null) {
			return null;
		}

		RestOperationTypeEnum opType = theRequestDetails.getRestOperationType();
		if (opType != null) {
			switch (opType) {
				case GRAPHQL_REQUEST:
					return null;
				default:
					break;
			}
		}

		FhirValidator validator;
		if (myValidator != null) {
			validator = myValidator;
		} else {
			validator = theRequestDetails.getServer().getFhirContext().newValidator();
			if (myValidatorModules != null) {
				for (IValidatorModule next : myValidatorModules) {
					validator.registerValidatorModule(next);
				}
			}
		}

		ValidationResult validationResult;
		try {
			validationResult = doValidate(validator, theRequest);
		} catch (Exception e) {
			if (myIgnoreValidatorExceptions) {
				ourLog.warn("Validator threw an exception during validation", e);
				return null;
			}
			if (e instanceof BaseServerResponseException) {
				throw (BaseServerResponseException) e;
			}
			throw new InternalErrorException(Msg.code(331) + e);
		}

		if (myAddResponseIssueHeaderOnSeverity != null) {
			boolean found = false;
			for (SingleValidationMessage next : validationResult.getMessages()) {
				if (next.getSeverity().ordinal() >= myAddResponseIssueHeaderOnSeverity) {
					addResponseIssueHeader(theRequestDetails, next);
					found = true;
				}
			}
			if (!found) {
				if (isNotBlank(myResponseIssueHeaderValueNoIssues)) {
					theRequestDetails.getResponse().addHeader(myResponseIssueHeaderName, myResponseIssueHeaderValueNoIssues);
				}
			}
		}

		if (myFailOnSeverity != null) {
			for (SingleValidationMessage next : validationResult.getMessages()) {
				if (next.getSeverity().ordinal() >= myFailOnSeverity) {
					postProcessResultOnFailure(theRequestDetails, validationResult);
					fail(theRequestDetails, validationResult);
					return validationResult;
				}
			}
		}

		if (myAddResponseOutcomeHeaderOnSeverity != null) {
			IBaseOperationOutcome outcome = null;
			for (SingleValidationMessage next : validationResult.getMessages()) {
				if (next.getSeverity().ordinal() >= myAddResponseOutcomeHeaderOnSeverity) {
					outcome = validationResult.toOperationOutcome();
					break;
				}
			}
			if (outcome == null && myAddResponseOutcomeHeaderOnSeverity != null && myAddResponseOutcomeHeaderOnSeverity == ResultSeverityEnum.INFORMATION.ordinal()) {
				FhirContext ctx = theRequestDetails.getServer().getFhirContext();
				outcome = OperationOutcomeUtil.newInstance(ctx);
				OperationOutcomeUtil.addIssue(ctx, outcome, "information", "No issues detected", "", "informational");
			}

			if (outcome != null) {
				IParser parser = theRequestDetails.getServer().getFhirContext().newJsonParser().setPrettyPrint(false);
				String encoded = parser.encodeResourceToString(outcome);
				if (encoded.length() > getMaximumHeaderLength()) {
					encoded = encoded.substring(0, getMaximumHeaderLength() - 3) + "...";
				}
				theRequestDetails.getResponse().addHeader(myResponseOutcomeHeaderName, encoded);
			}
		}

		postProcessResult(theRequestDetails, validationResult);

		return validationResult;
	}

	private static class MyLookup extends StrLookup<String> {

		private SingleValidationMessage myMessage;

		public MyLookup(SingleValidationMessage theMessage) {
			myMessage = theMessage;
		}

		@Override
		public String lookup(String theKey) {
			if ("line".equals(theKey)) {
				return toString(myMessage.getLocationLine());
			}
			if ("col".equals(theKey)) {
				return toString(myMessage.getLocationCol());
			}
			if ("message".equals(theKey)) {
				return toString(myMessage.getMessage());
			}
			if ("location".equals(theKey)) {
				return toString(myMessage.getLocationString());
			}
			if ("severity".equals(theKey)) {
				return myMessage.getSeverity() != null ? myMessage.getSeverity().name() : null;
			}

			return "";
		}

		private static String toString(Object theInt) {
			return theInt != null ? theInt.toString() : "";
		}

	}

}
