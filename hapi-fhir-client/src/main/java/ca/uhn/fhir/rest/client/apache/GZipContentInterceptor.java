package ca.uhn.fhir.rest.client.apache;

/*
 * #%L
 * HAPI FHIR - Client Framework
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

import org.apache.http.Header;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ByteArrayEntity;

import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.client.api.*;

/**
 * Client interceptor which GZip compresses outgoing (POST/PUT) contents being uploaded
 * from the client to the server. This can improve performance by reducing network 
 * load time.
 */
public class GZipContentInterceptor implements IClientInterceptor {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(GZipContentInterceptor.class);
	
	@Override
	public void interceptRequest(IHttpRequest theRequestInterface) {
		HttpRequestBase theRequest = ((ApacheHttpRequest) theRequestInterface).getApacheRequest();
		if (theRequest instanceof HttpEntityEnclosingRequest) {
			Header[] encodingHeaders = theRequest.getHeaders(Constants.HEADER_CONTENT_ENCODING);
			if (encodingHeaders == null || encodingHeaders.length == 0) {
				HttpEntityEnclosingRequest req = (HttpEntityEnclosingRequest)theRequest;
				
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				GZIPOutputStream gos;
				try {
					gos = new GZIPOutputStream(bos);
					req.getEntity().writeTo(gos);
					gos.finish();
				} catch (IOException e) {
					ourLog.warn("Failed to GZip outgoing content", e);
					return;
				}
				
				byte[] byteArray = bos.toByteArray();
				ByteArrayEntity newEntity = new ByteArrayEntity(byteArray);
				req.setEntity(newEntity);
				req.addHeader(Constants.HEADER_CONTENT_ENCODING, "gzip");
			}
		}
		
	}

	@Override
	public void interceptResponse(IHttpResponse theResponse) throws IOException {
		// nothing
	}

}
