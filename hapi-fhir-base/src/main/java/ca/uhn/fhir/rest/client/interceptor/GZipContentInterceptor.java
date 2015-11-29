package ca.uhn.fhir.rest.client.interceptor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

import org.apache.http.Header;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ByteArrayEntity;

import ca.uhn.fhir.rest.client.IClientInterceptor;
import ca.uhn.fhir.rest.server.Constants;

/**
 * Client interceptor which GZip compresses outgoing (POST/PUT) contents being uploaded
 * from the client to the server. This can improve performance by reducing network 
 * load time.
 */
public class GZipContentInterceptor implements IClientInterceptor {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(GZipContentInterceptor.class);
	
	@Override
	public void interceptRequest(HttpRequestBase theRequest) {
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
	public void interceptResponse(HttpResponse theResponse) throws IOException {
		// nothing
	}

}
