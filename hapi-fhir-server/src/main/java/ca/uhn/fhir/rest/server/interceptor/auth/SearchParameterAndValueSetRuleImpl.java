package ca.uhn.fhir.rest.server.interceptor.auth;

/*-
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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.context.support.ConceptValidationOptions;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.ValidationSupportContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import ca.uhn.fhir.util.FhirTerser;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.ICompositeType;
import org.hl7.fhir.instance.model.api.IIdType;
import org.slf4j.Logger;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

class SearchParameterAndValueSetRuleImpl extends RuleImplOp {

	private String mySearchParameterName;
	private String myValueSetUrl;
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
	protected AuthorizationInterceptor.Verdict applyRuleLogic(RestOperationTypeEnum theOperation, RequestDetails theRequestDetails, IBaseResource theInputResource, IIdType theInputResourceId, IBaseResource theOutputResource, Set<AuthorizationFlagsEnum> theFlags, FhirContext theFhirContext, RuleTarget theRuleTarget, IRuleApplier theRuleApplier) {
		// Sanity check
		Validate.isTrue(theInputResource == null || theOutputResource == null);

		if (theInputResource != null) {
			return applyRuleLogic(theFhirContext, theRequestDetails, theInputResource, theOperation, theInputResource, theInputResourceId, theOutputResource, theRuleApplier);
		}
		if (theOutputResource != null) {
			return applyRuleLogic(theFhirContext, theRequestDetails, theOutputResource, theOperation, theInputResource, theInputResourceId, theOutputResource, theRuleApplier);
		}

		// No resource present
		if (theOperation == RestOperationTypeEnum.READ || theOperation == RestOperationTypeEnum.SEARCH_TYPE) {
			return new AuthorizationInterceptor.Verdict(PolicyEnum.ALLOW, this);
		}

		return null;
	}

	private AuthorizationInterceptor.Verdict applyRuleLogic(FhirContext theFhirContext, RequestDetails theRequestDetails, IBaseResource theResource, RestOperationTypeEnum theOperation, IBaseResource theInputResource, IIdType theInputResourceId, IBaseResource theOutputResource, IRuleApplier theRuleApplier) {
		IValidationSupport validationSupport = theRuleApplier.getValidationSupport();
		if (validationSupport == null) {
			validationSupport = theFhirContext.getValidationSupport();
		}

		String operationDescription = "Authorization Rule";
		Logger troubleshootingLog = theRuleApplier.getTroubleshootingLog();
		boolean wantCode = myWantCode;

		ISearchParamRegistry searchParamRegistry = null;
		CodeMatchCount codeMatchCount = countMatchingCodesInValueSetForSearchParameter(theResource, validationSupport, searchParamRegistry, wantCode, mySearchParameterName, myValueSetUrl, troubleshootingLog, operationDescription);

		if (codeMatchCount.isAtLeastOneUnableToValidate()) {
			troubleshootingLog
				.warn("ValueSet {} could not be validated by terminology service - Assuming DENY", myValueSetUrl);
			return new AuthorizationInterceptor.Verdict(PolicyEnum.DENY, this);
		}

		if (myWantCode && codeMatchCount.getMatchingCodeCount() > 0) {
			return newVerdict(theOperation, theRequestDetails, theInputResource, theInputResourceId, theOutputResource);
		} else if (!myWantCode) {
			boolean notFound = getMode() == PolicyEnum.ALLOW && codeMatchCount.getMatchingCodeCount() == 0;
			boolean othersFound = getMode() == PolicyEnum.DENY && codeMatchCount.getMatchingCodeCount() < codeMatchCount.getOverallCodeCount();
			if (notFound || othersFound) {
				AuthorizationInterceptor.Verdict verdict = newVerdict(theOperation, theRequestDetails, theInputResource, theInputResourceId, theOutputResource);
				if (notFound) {
					troubleshootingLog
						.debug("Code was not found in VS - Verdict: {}", verdict);
				} else {
					troubleshootingLog
						.debug("Code(s) found that are not in VS - Verdict: {}", verdict);
				}
				return verdict;
			}
		}

		return null;
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
	 * @param theOperationDescription   A description of the operation being peformed (for logging)
	 */
	@Nonnull
	static CodeMatchCount countMatchingCodesInValueSetForSearchParameter(IBaseResource theResource, IValidationSupport theValidationSupport, ISearchParamRegistry theSearchParamRegistry, boolean theReturnOnFirstMatch, String theSearchParameterName, String theValueSetUrl, Logger theTroubleshootingLog, String theOperationDescription) {
		theTroubleshootingLog.debug("Applying {} {}:{} for valueSet: {}", theOperationDescription, theSearchParameterName, theReturnOnFirstMatch ? "in" : "not-in", theValueSetUrl);

		FhirContext fhirContext = theValidationSupport.getFhirContext();
		FhirTerser terser = fhirContext.newTerser();
		ConceptValidationOptions conceptValidationOptions = new ConceptValidationOptions();
		ValidationSupportContext validationSupportContext = new ValidationSupportContext(theValidationSupport);

		RuntimeResourceDefinition resourceDefinition = fhirContext.getResourceDefinition(theResource);
		RuntimeSearchParam searchParameter = resourceDefinition.getSearchParam(theSearchParameterName);
		if (searchParameter == null) {
			throw new InternalErrorException(Msg.code(2025) + "Unknown SearchParameter for resource " + resourceDefinition.getName() + ": " + theSearchParameterName);
		}

		List<String> paths = searchParameter.getPathsSplitForResourceType(resourceDefinition.getName());

		CodeMatchCount codeMatchCount = new CodeMatchCount();
		for (String nextPath : paths) {
			List<ICompositeType> foundCodeableConcepts = fhirContext.newFhirPath().evaluate(theResource, nextPath, ICompositeType.class);
			for (ICompositeType nextCodeableConcept : foundCodeableConcepts) {
				for (IBase nextCoding : terser.getValues(nextCodeableConcept, "coding")) {
					String system = terser.getSinglePrimitiveValueOrNull(nextCoding, "system");
					String code = terser.getSinglePrimitiveValueOrNull(nextCoding, "code");
					if (isNotBlank(system) && isNotBlank(code)) {
						IValidationSupport.CodeValidationResult validateCodeResult = theValidationSupport.validateCode(validationSupportContext, conceptValidationOptions, system, code, null, theValueSetUrl);
						if (validateCodeResult != null) {
							if (validateCodeResult.isOk()) {
								codeMatchCount.addMatchingCode();
								theTroubleshootingLog.debug("Code {}#{} was found in ValueSet[{}] - {}", system, code, theValueSetUrl, validateCodeResult.getMessage());
								if (theReturnOnFirstMatch) {
									return codeMatchCount;
								}
							} else {
								codeMatchCount.addNonMatchingCode();
								theTroubleshootingLog.debug("Code {}#{} was not found in ValueSet[{}]: {}", system, code, theValueSetUrl, validateCodeResult.getMessage());
							}
						} else {
							theTroubleshootingLog.debug("Terminology service was unable to validate code {}#{} in ValueSet[{}] - No service was able to handle this request", system, code, theValueSetUrl);
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
