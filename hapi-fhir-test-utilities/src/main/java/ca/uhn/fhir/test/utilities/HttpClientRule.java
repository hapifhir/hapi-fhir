package ca.uhn.fhir.test.utilities;

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

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public class HttpClientRule implements TestRule {
	private CloseableHttpClient myClient;

	@Override
	public Statement apply(Statement theBase, Description theDescription) {
		return new Statement() {
			@Override
			public void evaluate() throws Throwable {
				startClient();
				theBase.evaluate();
				stopClient();
			}
		};
	}


	private void stopClient() throws Exception {
		myClient.close();
	}

	private void startClient() {
		myClient = HttpClientBuilder
			.create()
			.build();
	}

	public CloseableHttpClient getClient() {
		return myClient;
	}
}
