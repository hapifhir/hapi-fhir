package ca.uhn.fhir.test.utilities.server;

/*-
 * #%L
 * HAPI FHIR Test Utilities
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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
