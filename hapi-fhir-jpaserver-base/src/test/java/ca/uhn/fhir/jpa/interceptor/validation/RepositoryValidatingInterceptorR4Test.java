package ca.uhn.fhir.jpa.interceptor.validation;

import ca.uhn.fhir.jpa.dao.r4.BaseJpaR4Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

public class RepositoryValidatingInterceptorR4Test extends BaseJpaR4Test {

	private RepositoryValidatingInterceptor myValInterceptor;

	@BeforeEach
	public void before() {
		myValInterceptor = new RepositoryValidatingInterceptor();
	}

	@AfterEach
	public void after() {
		myInterceptorRegistry.unregisterInterceptorsIf(t->t instanceof RepositoryValidatingInterceptor);
	}

	@Test
	public void testRequireAtLeastOneProfileOf() {

		List<IRepositoryValidatingRule> rules = RepositoryValidatingRuleBuilder
			.newInstance(myFhirCtx)
			.forResourcesOfType("Patient")
			.requireAtLeastOneProfileOf("http://foo/Profile1", "http://foo/Profile1")
			.build();
		myValInterceptor.setRules(rules);

	}

}
