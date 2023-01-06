package ca.uhn.fhir.okhttp.client;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.client.api.IHttpResponse;
import ca.uhn.fhir.rest.client.impl.BaseHttpResponse;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.util.StopWatch;
import okhttp3.MediaType;
import okhttp3.Response;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;
import java.util.Map;

/**
 * Wraps an OkHttp {@link Response}
 *
 * @author Matthew Clarke | matthew.clarke@orionhealth.com | Orion Health
 */
public class OkHttpRestfulResponse extends BaseHttpResponse implements IHttpResponse {

	private boolean myEntityBuffered = false;
	private byte[] myEntityBytes;
	private Response myResponse;

	public OkHttpRestfulResponse(Response theResponse, StopWatch theResponseStopWatch) {
		super(theResponseStopWatch);
		this.myResponse = theResponse;
	}

	@Override
	public void bufferEntity() throws IOException {
		if (myEntityBuffered) {
			return;
		}
		try (InputStream responseEntity = readEntity()) {
			if (responseEntity != null) {
				myEntityBuffered = true;
				try {
					myEntityBytes = IOUtils.toByteArray(responseEntity);
				} catch (IllegalStateException e) {
					throw new InternalErrorException(Msg.code(465) + e);
				}
			}
		}
	}

	@Override
	public void close() {
		myResponse.close();
	}

	@Override
	public Reader createReader() throws IOException {
		if (!myEntityBuffered && myResponse.body() == null) {
			return new StringReader("");
		} else {
			return new InputStreamReader(readEntity());
		}
	}

	@Override
	public Map<String, List<String>> getAllHeaders() {
		return myResponse.headers().toMultimap();
	}

	@Override
	public List<String> getHeaders(String theName) {
		return myResponse.headers(theName);
	}

	@Override
	public String getMimeType() {
		String contentType = myResponse.header(Constants.HEADER_CONTENT_TYPE);
		MediaType mediaType = null;
		if (contentType == null) {
			if (myResponse.body() != null) {
				mediaType = myResponse.body().contentType();
			}
		} else {
			mediaType = MediaType.parse(contentType);
		}

		if (mediaType == null) {
			return null;
		}

		return typeAndSubtypeOnly(mediaType).toString();
	}

	@Override
	public Object getResponse() {
		return myResponse;
	}

	@Override
	public int getStatus() {
		return myResponse.code();
	}

	@Override
	public String getStatusInfo() {
		return myResponse.message();
	}

	@Override
	public InputStream readEntity() throws IOException {
		if (this.myEntityBuffered) {
			return new ByteArrayInputStream(myEntityBytes);
		} else if (myResponse.body() != null) {
			return myResponse.body().byteStream();
		} else {
			return null;
		}
	}

	private MediaType typeAndSubtypeOnly(MediaType input) {
		return MediaType.parse(input.type() + "/" + input.subtype());
	}

}
