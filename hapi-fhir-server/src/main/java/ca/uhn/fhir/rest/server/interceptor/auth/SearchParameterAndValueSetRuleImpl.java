package ca.uhn.fhir.rest.server.interceptor.auth;

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
import ca.uhn.fhir.util.FhirTerser;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.ICompositeType;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.List;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

class SearchParameterAndValueSetRuleImpl extends RuleImplOp {

	private String mySearchParameterName;
	private String myValueSetUrl;

	/**
	 * Constructor
	 *
	 * @param theRuleName The rule name
	 */
	SearchParameterAndValueSetRuleImpl(String theRuleName) {
		super(theRuleName);
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

		FhirTerser terser = theFhirContext.newTerser();
		ConceptValidationOptions conceptValidationOptions = new ConceptValidationOptions();
		ValidationSupportContext validationSupportContext = new ValidationSupportContext(validationSupport);

		RuntimeResourceDefinition resourceDefinition = theFhirContext.getResourceDefinition(theResource);
		RuntimeSearchParam searchParameter = resourceDefinition.getSearchParam(mySearchParameterName);
		if (searchParameter == null) {
			throw new InternalErrorException(Msg.code(2025) + "Unknown SearchParameter for resource " + resourceDefinition.getName() + ": " + mySearchParameterName);
		}

		List<String> paths = searchParameter.getPathsSplitForResourceType(resourceDefinition.getName());

		for (String nextPath : paths) {
			List<ICompositeType> foundCodeableConcepts = theFhirContext.newFhirPath().evaluate(theResource, nextPath, ICompositeType.class);
			for (ICompositeType nextCodeableConcept : foundCodeableConcepts) {
				for (IBase nextCoding : terser.getValues(nextCodeableConcept, "coding")) {
					String system = terser.getSinglePrimitiveValueOrNull(nextCoding, "system");
					String code = terser.getSinglePrimitiveValueOrNull(nextCoding, "code");
					if (isNotBlank(system) && isNotBlank(code)) {
						IValidationSupport.CodeValidationResult validateCodeResult = validationSupport.validateCode(validationSupportContext, conceptValidationOptions, system, code, null, myValueSetUrl);
						if (validateCodeResult != null) {
							if (validateCodeResult.isOk()) {
								return newVerdict(theOperation, theRequestDetails, theInputResource, theInputResourceId, theOutputResource);
							}
						}
					}
				}
			}
		}

		return null;
	}
}
