/*
 * #%L
 * HAPI FHIR - Client Framework using Apache HttpClient 5
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
package ca.uhn.fhir.rest.client.apache;

import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.client.api.IClientInterceptor;
import ca.uhn.fhir.rest.client.api.IHttpRequest;
import ca.uhn.fhir.rest.client.api.IHttpResponse;
import org.apache.hc.client5.http.classic.methods.HttpUriRequest;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.io.entity.ByteArrayEntity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

/**
 * Client interceptor which GZip compresses outgoing (POST/PUT) contents being uploaded
 * from the client to the server. This can improve performance by reducing network
 * load time.
 */
public class ApacheHttp5GZipContentInterceptor implements IClientInterceptor {
	private static final org.slf4j.Logger ourLog =
			org.slf4j.LoggerFactory.getLogger(ApacheHttp5GZipContentInterceptor.class);

	@Override
	public void interceptRequest(IHttpRequest theRequestInterface) {
		HttpUriRequest theRequest = ((ApacheHttp5Request) theRequestInterface).getApacheRequest();
		if (theRequest != null) {
			Header[] encodingHeaders = theRequest.getHeaders(Constants.HEADER_CONTENT_ENCODING);
			if (encodingHeaders == null || encodingHeaders.length == 0) {

				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				GZIPOutputStream gos;
				try {
					gos = new GZIPOutputStream(bos);
					theRequest.getEntity().writeTo(gos);
					gos.finish();
				} catch (IOException e) {
					ourLog.warn("Failed to GZip outgoing content", e);
					return;
				}

				byte[] byteArray = bos.toByteArray();
				ByteArrayEntity newEntity = new ByteArrayEntity(byteArray, ContentType.APPLICATION_OCTET_STREAM);
				theRequest.setEntity(newEntity);
				theRequest.addHeader(Constants.HEADER_CONTENT_ENCODING, "gzip");
			}
		}
	}

	@Override
	public void interceptResponse(IHttpResponse theResponse) throws IOException {
		// nothing
	}
}
