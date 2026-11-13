/*-
 * #%L
 * HAPI FHIR - Server Framework
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
package ca.uhn.fhir.rest.server.interceptor.auth;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.context.support.ConceptValidationOptions;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.ValidationSupportContext;
import ca.uhn.fhir.context.support.ValueSetCodeSystemFinder;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import ca.uhn.fhir.util.FhirTerser;
import jakarta.annotation.Nonnull;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.ICompositeType;
import org.hl7.fhir.instance.model.api.IIdType;
import org.slf4j.Logger;

import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

class SearchParameterAndValueSetRuleImpl extends RuleImplOp {

	private String mySearchParameterName;
	private String myValueSetUrl;
	/**
	 * Whether this is a {@code withCodeInValueSet} rule.
	 * false for {@code withCodeNotInValueSet}
	 */
	private boolean myWantCode;

	/**
	 * Constructor
	 *
	 * @param theRuleName The rule name
	 */
	SearchParameterAndValueSetRuleImpl(String theRuleName) {
		super(theRuleName);
	}

	void setWantCode(boolean theWantCode) {
		myWantCode = theWantCode;
	}

	public void setSearchParameterName(String theSearchParameterName) {
		mySearchParameterName = theSearchParameterName;
	}

	public void setValueSetUrl(String theValueSetUrl) {
		myValueSetUrl = theValueSetUrl;
	}

	@Override
	protected AuthorizationInterceptor.Verdict applyRuleLogic(
			RestOperationTypeEnum theOperation,
			RequestDetails theRequestDetails,
			IBaseResource theInputResource,
			IIdType theInputResourceId,
			IBaseResource theOutputResource,
			Set<AuthorizationFlagsEnum> theFlags,
			FhirContext theFhirContext,
			RuleTarget theRuleTarget,
			IRuleApplier theRuleApplier) {
		// Sanity check
		Validate.isTrue(theInputResource == null || theOutputResource == null);

		if (theInputResource != null) {
			return applyRuleLogic(
					theFhirContext,
					theRequestDetails,
					theInputResource,
					theOperation,
					theInputResource,
					theInputResourceId,
					theOutputResource,
					theRuleApplier);
		}
		if (theOutputResource != null) {
			return applyRuleLogic(
					theFhirContext,
					theRequestDetails,
					theOutputResource,
					theOperation,
					theInputResource,
					theInputResourceId,
					theOutputResource,
					theRuleApplier);
		}

		// No resource present
		if (theOperation == RestOperationTypeEnum.READ || theOperation == RestOperationTypeEnum.SEARCH_TYPE) {
			return new AuthorizationInterceptor.Verdict(PolicyEnum.ALLOW, this);
		}

		return null;
	}

	/**
	 * Always returns <code>false</code>: a value-set rule never applies to every resource of its type.
	 * <p>
	 * This rule only applies to resources whose code (in the configured search parameter) is in the value set,
	 * or not in it when built with <code>withCodeNotInValueSet</code>. That restriction is enforced in
	 * {@link #applyRuleLogic}, when each resource is checked, and isn't recorded in the fields the base
	 * implementation inspects: a rule such as
	 * <code>allow().read().resourcesOfType("Observation").withCodeInValueSet("code", url)</code> applies to a type,
	 * has no compartment and no testers, so without this override it would be treated as allowing every
	 * Observation to be read.
	 * </p>
	 * <p>
	 * That matters for <code>_has</code> search parameters. For example, with the rule above,
	 * <code>Patient?_has:Observation:subject:code=X</code> joins over every Observation, including those
	 * whose code is outside the value set, which the caller isn't allowed to read. Returning <code>false</code>
	 * keeps such a search from being allowed on the strength of this rule.
	 * </p>
	 *
	 * @see RuleImplOp#isUnrestrictedWithinType()
	 */
	@Override
	boolean isUnrestrictedWithinType() {
		return false;
	}

	/**
	 * A code whose membership could not be decided only forces DENY when the decided codes do not settle the rule.
	 * <pre>
	 * Rule           |  Condition                                       | 1 member + 1 undecided                  | 1 non-member + 1 undecided
	 * allow + in     |  any code in the VS = ALLOW     (OR'd)           | ALLOW (1 being in is enough)            | DENY (the undecided one could be in)
	 * deny + in      |  any code in the VS = DENY      (OR'd)           | DENY (1 being in is enough)             | DENY (the undecided one could be in)
	 * allow + not-in |  no codes must be in VS = ALLOW   (AND'd)        | NULL (1 is in, so rule doesn't hold)    | DENY (the undecided one could be in)
	 * deny + not-in  |  at least 1 code must not be in VS = DENY (OR'd) | DENY (the undecided one could be out)   | DENY (1 being out is enough)
	 * </pre>
	 */
	private AuthorizationInterceptor.Verdict applyRuleLogic(
			FhirContext theFhirContext,
			RequestDetails theRequestDetails,
			IBaseResource theResource,
			RestOperationTypeEnum theOperation,
			IBaseResource theInputResource,
			IIdType theInputResourceId,
			IBaseResource theOutputResource,
			IRuleApplier theRuleApplier) {
		IValidationSupport validationSupport = theRuleApplier.getValidationSupport();
		if (validationSupport == null) {
			validationSupport = theFhirContext.getValidationSupport();
		}

		String operationDescription = "Authorization Rule";
		Logger troubleshootingLog = theRuleApplier.getTroubleshootingLog();
		boolean wantCode = myWantCode;

		// do code match count
		ISearchParamRegistry searchParamRegistry = null;
		CodeMatchCount codeMatchCount = countMatchingCodesInValueSetForSearchParameter(
				theResource,
				validationSupport,
				searchParamRegistry,
				wantCode,
				mySearchParameterName,
				myValueSetUrl,
				troubleshootingLog,
				operationDescription);

		// the overall code count only includes codes whose membership was decided
		boolean anyIn = codeMatchCount.getMatchingCodeCount() > 0;
		boolean anyOut = codeMatchCount.getMatchingCodeCount() < codeMatchCount.getOverallCodeCount();
		boolean anyUndecided = codeMatchCount.isAtLeastOneUnableToValidate();
		Supplier<AuthorizationInterceptor.Verdict> ruleVerdict = () -> newVerdict(
				theOperation,
				theRequestDetails,
				theInputResource,
				theInputResourceId,
				theOutputResource,
				theRuleApplier);

		AuthorizationInterceptor.Verdict verdict =
				switch (getMode()) {
					case ALLOW -> {
						if (!myWantCode) {
							// allow + not-in (if NONE are in we ALLOW)
							if (anyIn) {
								// some code in VS
								// rule doesn't apply (null)
								yield null;
							} else {
								// no codes in VS
								// any undecided -> DENY
								// else -> ALLOW
								if (anyUndecided) {
									yield undecidedDeny(troubleshootingLog);
								}
								AuthorizationInterceptor.Verdict allow = ruleVerdict.get();
								troubleshootingLog.debug("Code was not found in VS - Verdict: {}", allow);
								yield allow;
							}
						} else {
							// allow + in (if ANY are in we ALLOW)
							if (anyIn) {
								// allow
								yield ruleVerdict.get();
							} else {
								// no codes in VS
								// any undecided -> DENY
								// else -> rule doesn't apply (null)
								yield anyUndecided ? undecidedDeny(troubleshootingLog) : null;
							}
						}
					}
					case DENY -> {
						if (!myWantCode) {
							// deny + not-in (if even ONE code is *not* in we DENY)
							if (anyOut) {
								// at least 1 code not in VS -> DENY
								AuthorizationInterceptor.Verdict deny = ruleVerdict.get();
								troubleshootingLog.debug("Code(s) found that are not in VS - Verdict: {}", deny);
								yield deny;
							} else {
								// no codes not in VS
								// if there's any undecided -> deny (it could be outside the VS)
								// otherwise, rule doesn't apply (null)
								yield anyUndecided ? undecidedDeny(troubleshootingLog) : null;
							}
						} else {
							// deny + in (if ANY code is in we DENY)
							if (anyIn) {
								// any code in VS -> DENY
								yield ruleVerdict.get();
							} else {
								// no codes in VS
								// if any undecided -> DENY (could be in)
								// otherwise -> rule doesn't apply (null)
								yield anyUndecided ? undecidedDeny(troubleshootingLog) : null;
							}
						}
					}
				};

		return verdict;
	}

	/**
	 * A code whose membership could not be decided might change the verdict, so the rule denies
	 */
	private AuthorizationInterceptor.Verdict undecidedDeny(Logger theTroubleshootingLog) {
		theTroubleshootingLog.warn(
				"ValueSet {} could not be validated by terminology service - Assuming DENY", myValueSetUrl);
		return new AuthorizationInterceptor.Verdict(PolicyEnum.DENY, this);
	}

	/**
	 * Scan a resource for all codes indexed by the given SearchParameter and validates the codes for membership in
	 * the given SearchParameter
	 *
	 * @param theResource            The resource to scan
	 * @param theValidationSupport      The validation support module
	 * @param theReturnOnFirstMatch  Should we return as soon as one match is found? (as an optimization)
	 * @param theSearchParameterName The search parameter name being searched for
	 * @param theValueSetUrl         The ValueSet URL to validate against
	 * @param theTroubleshootingLog     A log to use for writing status updates
	 * @param theOperationDescription   A description of the operation being performed (for logging)
	 */
	@Nonnull
	static CodeMatchCount countMatchingCodesInValueSetForSearchParameter(
			IBaseResource theResource,
			IValidationSupport theValidationSupport,
			ISearchParamRegistry theSearchParamRegistry,
			boolean theReturnOnFirstMatch,
			String theSearchParameterName,
			String theValueSetUrl,
			Logger theTroubleshootingLog,
			String theOperationDescription) {
		theTroubleshootingLog.debug(
				"Applying {} {}:{} for valueSet: {}",
				theOperationDescription,
				theSearchParameterName,
				theReturnOnFirstMatch ? "in" : "not-in",
				theValueSetUrl);

		FhirContext fhirContext = theValidationSupport.getFhirContext();
		FhirTerser terser = fhirContext.newTerser();
		ConceptValidationOptions conceptValidationOptions = new ConceptValidationOptions();
		ValidationSupportContext validationSupportContext = new ValidationSupportContext(theValidationSupport);
		ValueSetCodeSystemFinder unknownSystemFinder = new ValueSetCodeSystemFinder(validationSupportContext);

		RuntimeResourceDefinition resourceDefinition = fhirContext.getResourceDefinition(theResource);
		RuntimeSearchParam searchParameter = resourceDefinition.getSearchParam(theSearchParameterName);
		if (searchParameter == null) {
			throw new InternalErrorException(Msg.code(2025) + "Unknown SearchParameter for resource "
					+ resourceDefinition.getName() + ": " + theSearchParameterName);
		}

		List<String> paths = searchParameter.getPathsSplitForResourceType(resourceDefinition.getName());

		CodeMatchCount codeMatchCount = new CodeMatchCount();
		for (String nextPath : paths) {
			List<ICompositeType> foundCodeableConcepts =
					fhirContext.newFhirPath().evaluate(theResource, nextPath, ICompositeType.class);
			for (ICompositeType nextCodeableConcept : foundCodeableConcepts) {
				for (IBase nextCoding : terser.getValues(nextCodeableConcept, "coding")) {
					String system = terser.getSinglePrimitiveValueOrNull(nextCoding, "system");
					String code = terser.getSinglePrimitiveValueOrNull(nextCoding, "code");
					if (isNotBlank(system) && isNotBlank(code)) {
						IValidationSupport.CodeValidationResult validateCodeResult = theValidationSupport.validateCode(
								validationSupportContext, conceptValidationOptions, system, code, null, theValueSetUrl);
						List<String> unknownSystems = validateCodeResult != null
								? unknownSystemFinder.findUnknownSystems(validateCodeResult, theValueSetUrl, system)
								: List.of();

						if (!unknownSystems.isEmpty()) {
							theTroubleshootingLog.debug(
									"Code {}#{} could not be checked against ValueSet[{}] - CodeSystem not loaded: {}",
									system,
									code,
									theValueSetUrl,
									unknownSystems);
							codeMatchCount.addUnableToValidate();
						} else if (validateCodeResult != null) {
							if (validateCodeResult.isOk()) {
								codeMatchCount.addMatchingCode();
								theTroubleshootingLog.debug(
										"Code {}#{} was found in ValueSet[{}] - {}",
										system,
										code,
										theValueSetUrl,
										validateCodeResult.getMessage());
								if (theReturnOnFirstMatch) {
									return codeMatchCount;
								}
							} else if (!validateCodeResult.isCodeDeterminable()) {
								codeMatchCount.addUnableToValidate();
								theTroubleshootingLog.debug(
										"Code {}#{} was not able to be determined: {}",
										system,
										code,
										validateCodeResult.getMessage());
							} else {
								codeMatchCount.addNonMatchingCode();
								theTroubleshootingLog.debug(
										"Code {}#{} was not found in ValueSet[{}]: {}",
										system,
										code,
										theValueSetUrl,
										validateCodeResult.getMessage());
							}
						} else {
							theTroubleshootingLog.debug(
									"Terminology service was unable to validate code {}#{} in ValueSet[{}] - No service was able to handle this request",
									system,
									code,
									theValueSetUrl);
							codeMatchCount.addUnableToValidate();
						}
					}
				}
			}
		}
		return codeMatchCount;
	}

	static class CodeMatchCount {

		private int myMatchingCodeCount;
		private int myOverallCodeCount;
		private boolean myAtLeastOneUnableToValidate;

		public boolean isAtLeastOneUnableToValidate() {
			return myAtLeastOneUnableToValidate;
		}

		public void addUnableToValidate() {
			myAtLeastOneUnableToValidate = true;
		}

		public void addNonMatchingCode() {
			myOverallCodeCount++;
		}

		public void addMatchingCode() {
			myMatchingCodeCount++;
			myOverallCodeCount++;
		}

		public int getMatchingCodeCount() {
			return myMatchingCodeCount;
		}

		public int getOverallCodeCount() {
			return myOverallCodeCount;
		}
	}
}
