package ca.uhn.fhir.jpa.auth;

import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.interceptor.auth.AuthorizationInterceptor;
import ca.uhn.fhir.rest.server.interceptor.auth.IAuthorizationSearchParamMatcher;
import ca.uhn.fhir.rest.server.interceptor.auth.IRuleApplier;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Empty implementation to base a stub.
 */
public class TestRuleApplier implements IRuleApplier {
	private static final Logger ourLog = LoggerFactory.getLogger(TestRuleApplier.class);

	@Nonnull
	@Override
	public Logger getTroubleshootingLog() {
		return ourLog;
	}

	@Override
	public AuthorizationInterceptor.Verdict applyRulesAndReturnDecision(RestOperationTypeEnum theOperation, RequestDetails theRequestDetails, IBaseResource theInputResource, IIdType theInputResourceId, IBaseResource theOutputResource, Pointcut thePointcut) {
		return null;
	}

	@Nullable
	@Override
	public IValidationSupport getValidationSupport() {
		return null;
	}

	@Nullable
	@Override
	public IAuthorizationSearchParamMatcher getSearchParamMatcher() {
		return IRuleApplier.super.getSearchParamMatcher();
	}
}
