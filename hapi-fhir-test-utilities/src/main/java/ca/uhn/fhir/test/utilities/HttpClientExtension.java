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
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class HttpClientExtension implements BeforeEachCallback, AfterEachCallback {
	private CloseableHttpClient myClient;

	public CloseableHttpClient getClient() {
		return myClient;
	}

	@Override
	public void afterEach(ExtensionContext theExtensionContext) throws Exception {
		myClient.close();
	}

	@Override
	public void beforeEach(ExtensionContext theExtensionContext) throws Exception {
		myClient = HttpClientBuilder
			.create()
			.build();
	}
}
