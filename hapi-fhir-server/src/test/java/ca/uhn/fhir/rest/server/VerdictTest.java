package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.interceptor.auth.AuthorizationFlagsEnum;
import ca.uhn.fhir.rest.server.interceptor.auth.AuthorizationInterceptor.Verdict;
import ca.uhn.fhir.rest.server.interceptor.auth.IAuthRule;
import ca.uhn.fhir.rest.server.interceptor.auth.IRuleApplier;
import ca.uhn.fhir.rest.server.interceptor.auth.PolicyEnum;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.jupiter.api.Test;

import java.util.Set;

/**
 * Tests for {@link Verdict}
 *
 * @author Jafer Khan Shamshad
 */
public class VerdictTest {

	/**
	 * Implementers should be able to instantiate {@link Verdict} outside the package where it has been defined.
	 */
	@Test
	public void testInstantiationFromAnotherPackage() {
		Verdict verdict = new Verdict(PolicyEnum.ALLOW, new CustomRule());
	}

	/**
	 * Existing implementations of {@link IAuthRule} are inaccessible from this package.
	 * This test class is a sample implementation of {@link IAuthRule}.
	 */
	public static class CustomRule implements IAuthRule {

		@Override
		public Verdict applyRule(RestOperationTypeEnum theOperation, RequestDetails theRequestDetails, IBaseResource theInputResource, IIdType theInputResourceId, IBaseResource theOutputResource, IRuleApplier theRuleApplier, Set<AuthorizationFlagsEnum> theFlags, Pointcut thePointcut) {
			return new Verdict(PolicyEnum.ALLOW, this);
		}

		@Override
		public String getName() {
			return "Custom rule";
		}
	}
}
