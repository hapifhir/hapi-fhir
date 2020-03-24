package ca.uhn.fhir.rest.server.interceptor;

import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.rest.api.server.RequestDetails;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * @since 5.0.0
 */
public class ResponseSizeCapturingInterceptor {

	private static final String COUNTING_WRITER_KEY = ResponseSizeCapturingInterceptor.class.getName() + "_COUNTING_WRITER_KEY";
	private List<Consumer<Result>> myConsumers = new ArrayList<>();

	@Hook(Pointcut.SERVER_OUTGOING_WRITER_CREATED)
	public Writer capture(RequestDetails theRequestDetails, Writer theWriter) {
		CountingWriter retVal = new CountingWriter(theWriter);
		theRequestDetails.getUserData().put(COUNTING_WRITER_KEY, retVal);
		return retVal;
	}


	@Hook(Pointcut.SERVER_PROCESSING_COMPLETED)
	public void completed(RequestDetails theRequestDetails) {
		CountingWriter countingWriter = (CountingWriter) theRequestDetails.getUserData().get(COUNTING_WRITER_KEY);
		if (countingWriter != null) {
			int charCount = countingWriter.getCount();
			notifyConsumers(new Result(charCount));
		}
	}

	private void notifyConsumers(Result theResult) {
		myConsumers.forEach(t -> t.accept(theResult));
	}
	
	public static class Result {
		private final int myWrittenChars;

		public Result(int theWrittenChars) {
			myWrittenChars = theWrittenChars;
		}

		public int getWrittenChars() {
			return myWrittenChars;
		}

	}


	private static class CountingWriter extends Writer {

		private final Writer myWrap;
		private int myCount;

		private CountingWriter(Writer theWrap) {
			myWrap = theWrap;
		}

		@Override
		public void write(char[] cbuf, int off, int len) throws IOException {
			myCount += len;
			myWrap.write(cbuf, off, len);
		}

		@Override
		public void flush() throws IOException {
			myWrap.flush();
		}

		@Override
		public void close() throws IOException {
			myWrap.close();
		}

		public int getCount() {
			return myCount;
		}
	}

}
