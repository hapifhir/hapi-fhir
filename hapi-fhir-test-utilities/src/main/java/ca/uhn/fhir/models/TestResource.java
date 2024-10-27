/*-
 * #%L
 * HAPI FHIR Test Utilities
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.models;

import org.springframework.core.io.AbstractResource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * This is a test object that can be used to wrap a string
 * that could be injected as a spring-resource at bootup.
 */
public class TestResource extends AbstractResource {

	public static Resource createFromString(String theTxt) {
		TestResource resource = new TestResource(new ByteArrayResource(theTxt.getBytes(StandardCharsets.UTF_8)));

		return resource;
	}

	private Resource myResource;

	private TestResource(Resource theResource) {
		myResource = theResource;
	}

	@Override
	public String getDescription() {
		return getClass().getName();
	}

	@Override
	public InputStream getInputStream() throws IOException {
		return myResource.getInputStream();
	}

	@Override
	public URL getURL() throws IOException {
		return new URL("http://example.com/test");
	}
}
