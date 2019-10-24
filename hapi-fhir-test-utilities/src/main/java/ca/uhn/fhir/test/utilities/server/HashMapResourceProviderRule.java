package ca.uhn.fhir.test.utilities.server;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.server.provider.HashMapResourceProvider;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public class HashMapResourceProviderRule<T extends IBaseResource> extends HashMapResourceProvider<T> implements TestRule {

	private final RestfulServerRule myRestfulServerRule;

	/**
	 * Constructor
	 *
	 * @param theFhirContext  The FHIR context
	 * @param theResourceType The resource type to support
	 */
	public HashMapResourceProviderRule(RestfulServerRule theRestfulServerRule, Class<T> theResourceType) {
		super(theRestfulServerRule.getFhirContext(), theResourceType);

		myRestfulServerRule = theRestfulServerRule;
	}

	@Override
	public Statement apply(Statement base, Description description) {
		return new Statement() {
			@Override
			public void evaluate() throws Throwable {
				clear();
				myRestfulServerRule.getRestfulServer().registerProvider(HashMapResourceProviderRule.this);
				try {
					base.evaluate();
				} finally {
					myRestfulServerRule.getRestfulServer().unregisterProvider(HashMapResourceProviderRule.this);
				}
			}
		};
	}

}
