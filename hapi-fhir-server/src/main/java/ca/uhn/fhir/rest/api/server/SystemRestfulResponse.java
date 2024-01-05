/*-
 * #%L
 * HAPI FHIR - Server Framework
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
package ca.uhn.fhir.rest.api.server;

import ca.uhn.fhir.rest.server.BaseRestfulResponse;
import ca.uhn.fhir.util.IoUtil;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.apache.commons.lang3.Validate;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.Writer;

/**
 * A default RestfulResponse that returns the body as an IBaseResource and ignores everything else.
 */
public class SystemRestfulResponse extends BaseRestfulResponse<SystemRequestDetails> {
	private Writer myWriter;
	private ByteArrayOutputStream myOutputStream;

	public SystemRestfulResponse(SystemRequestDetails theSystemRequestDetails) {
		super(theSystemRequestDetails);
	}

	@Nonnull
	@Override
	public Writer getResponseWriter(int theStatusCode, String theContentType, String theCharset, boolean theRespondGzip)
			throws IOException {
		Validate.isTrue(myWriter == null, "getResponseWriter() called multiple times");
		Validate.isTrue(myOutputStream == null, "getResponseWriter() called after getResponseOutputStream()");

		myWriter = new StringWriter();
		return myWriter;
	}

	@Nonnull
	@Override
	public OutputStream getResponseOutputStream(
			int theStatusCode, String theContentType, @Nullable Integer theContentLength) throws IOException {
		Validate.isTrue(myWriter == null, "getResponseOutputStream() called multiple times");
		Validate.isTrue(myOutputStream == null, "getResponseOutputStream() called after getResponseWriter()");

		myOutputStream = new ByteArrayOutputStream();
		return myOutputStream;
	}

	@Override
	public Object commitResponse(@Nonnull Closeable theWriterOrOutputStream) throws IOException {
		IoUtil.closeQuietly(theWriterOrOutputStream);

		return getRequestDetails().getServer().getFhirContext().newJsonParser().parseResource(myWriter.toString());
	}
}
