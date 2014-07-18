package ca.uhn.fhir.rest.client.interceptor;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.HttpEntityWrapper;

import ca.uhn.fhir.rest.client.IClientInterceptor;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;

public class LoggingInterceptor implements IClientInterceptor {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(LoggingInterceptor.class);
	private boolean myLogRequestSummary = true;
	private boolean myLogResponseSummary = true;
	private boolean myLogResponseBody = false;
	private boolean myLogRequestBody = false;
	private boolean myLogResponseHeaders=false;
	private boolean myLogRequestHeaders=false;

	/**
	 * Constructor
	 */
	public LoggingInterceptor() {
		super();
	}

	/**
	 * Constructor
	 * 
	 * @param theVerbose If set to true, all logging is enabled
	 */
	public LoggingInterceptor(boolean theVerbose) {
		if (theVerbose) {
			setLogRequestBody(true);
			setLogRequestSummary(true);
			setLogResponseBody(true);
			setLogResponseSummary(true);
			setLogRequestHeaders(true);
			setLogResponseHeaders(true);
		}
	}

	@Override
	public void interceptRequest(HttpRequestBase theRequest) {
		if (myLogRequestSummary) {
			ourLog.info("Client request: {}", theRequest);
		}

		if (myLogRequestHeaders) {
			StringBuilder b = new StringBuilder();
			for (int i = 0;i <  theRequest.getAllHeaders().length;i++) {
				Header next = theRequest.getAllHeaders()[i];
				b.append(next.getName() + ": " + next.getValue());
				if(i+1 <  theRequest.getAllHeaders().length) {
					b.append('\n');
				}
			}
			ourLog.info("Client request headers:\n{}", b.toString());
		}
		
		if (myLogRequestBody) {
			if (theRequest instanceof HttpEntityEnclosingRequest) {
				HttpEntity entity = ((HttpEntityEnclosingRequest) theRequest).getEntity();
				if (entity.isRepeatable()) {
					try {
						String content = IOUtils.toString(entity.getContent());
						ourLog.info("Client request body:\n{}", content);
					} catch (IllegalStateException e) {
						ourLog.warn("Failed to replay request contents (during logging attempt, actual FHIR call did not fail)", e);
					} catch (IOException e) {
						ourLog.warn("Failed to replay request contents (during logging attempt, actual FHIR call did not fail)", e);
					}
				}
			}
		}
		
	}

	@Override
	public void interceptResponse(HttpResponse theResponse) throws IOException {
		if (myLogResponseSummary) {
			String message = "HTTP " + theResponse.getStatusLine().getStatusCode() + " " + theResponse.getStatusLine().getReasonPhrase();
			ourLog.info("Client response: {}", message);
		}

		if (myLogRequestHeaders) {
			StringBuilder b = new StringBuilder();
			for (int i = 0;i <  theResponse.getAllHeaders().length;i++) {
				Header next = theResponse.getAllHeaders()[i];
				b.append(next.getName() + ": " + next.getValue());
				if(i+1 <  theResponse.getAllHeaders().length) {
					b.append('\n');
				}
			}
			ourLog.info("Client response headers:\n{}", b.toString());
		}

		if (myLogResponseBody) {
			HttpEntity respEntity = theResponse.getEntity();
			final byte[] bytes;
			try {
				bytes = IOUtils.toByteArray(respEntity.getContent());
			} catch (IllegalStateException e) {
				throw new InternalErrorException(e);
			}

			ourLog.info("Client response body:\n{}", new String(bytes));

			theResponse.setEntity(new MyEntityWrapper(respEntity, bytes));
		}
	}

	private static class MyEntityWrapper extends HttpEntityWrapper {

		private byte[] myBytes;

		public MyEntityWrapper(HttpEntity theWrappedEntity, byte[] theBytes) {
			super(theWrappedEntity);
			myBytes = theBytes;
		}

		@Override
		public InputStream getContent() throws IOException {
			return new ByteArrayInputStream(myBytes);
		}

		@Override
		public void writeTo(OutputStream theOutstream) throws IOException {
			theOutstream.write(myBytes);
		}

	}

	/**
	 * Should a summary (one line) for each request be logged, containing the URL and other information
	 */
	public void setLogRequestSummary(boolean theValue) {
		myLogRequestSummary = theValue;
	}

	/**
	 * Should a summary (one line) for each request be logged, containing the URL and other information
	 */
	public void setLogResponseSummary(boolean theValue) {
		myLogResponseSummary = theValue;
	}

	/**
	 * Should headers for each request be logged, containing the URL and other information
	 */
	public void setLogRequestHeaders(boolean theValue) {
		myLogRequestHeaders = theValue;
	}

	/**
	 * Should headers for each request be logged, containing the URL and other information
	 */
	public void setLogResponseHeaders(boolean theValue) {
		myLogResponseHeaders = theValue;
	}

	/**
	 * Should a summary (one line) for each request be logged, containing the URL and other information
	 */
	public void setLogRequestBody(boolean theValue) {
		myLogRequestBody = theValue;
	}

	/**
	 * Should a summary (one line) for each request be logged, containing the URL and other information
	 */
	public void setLogResponseBody(boolean theValue) {
		myLogResponseBody = theValue;
	}

}
