package ca.uhn.fhir.rest.server.interceptor;

/*-
 * #%L
 * HAPI FHIR - Server Framework
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

import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.apache.commons.lang3.Validate;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * This interceptor captures and makes
 * available the number of characters written (pre-compression if Gzip compression is being used) to the HTTP response
 * stream for FHIR responses.
 * <p>
 * Response details are made available in the request {@link RequestDetails#getUserData() RequestDetails UserData map}
 * with {@link #RESPONSE_RESULT_KEY} as the key.
 * </p>
 *
 * @since 5.0.0
 */
public class ResponseSizeCapturingInterceptor {

	/**
	 * If the response was a character stream, a character count will be placed in the
	 * {@link RequestDetails#getUserData() RequestDetails UserData map} with this key, containing
	 * an {@link Result} value.
	 * <p>
	 * The value will be placed at the start of the {@link Pointcut#SERVER_PROCESSING_COMPLETED} pointcut, so it will not
	 * be available before that time.
	 * </p>
	 */
	public static final String RESPONSE_RESULT_KEY = ResponseSizeCapturingInterceptor.class.getName() + "_RESPONSE_RESULT_KEY";

	private static final String COUNTING_WRITER_KEY = ResponseSizeCapturingInterceptor.class.getName() + "_COUNTING_WRITER_KEY";
	private final List<Consumer<Result>> myConsumers = new ArrayList<>();

	@Hook(Pointcut.SERVER_OUTGOING_WRITER_CREATED)
	public Writer capture(RequestDetails theRequestDetails, Writer theWriter) {
		CountingWriter retVal = new CountingWriter(theWriter);
		theRequestDetails.getUserData().put(COUNTING_WRITER_KEY, retVal);
		return retVal;
	}


	@Hook(value = Pointcut.SERVER_PROCESSING_COMPLETED, order = InterceptorOrders.RESPONSE_SIZE_CAPTURING_INTERCEPTOR_COMPLETED)
	public void completed(RequestDetails theRequestDetails) {
		CountingWriter countingWriter = (CountingWriter) theRequestDetails.getUserData().get(COUNTING_WRITER_KEY);
		if (countingWriter != null) {
			int charCount = countingWriter.getCount();
			Result result = new Result(theRequestDetails, charCount);
			notifyConsumers(result);

			theRequestDetails.getUserData().put(RESPONSE_RESULT_KEY, result);
		}
	}

	/**
	 * Registers a new consumer. All consumers will be notified each time a request is complete.
	 *
	 * @param theConsumer The consumer
	 */
	public void registerConsumer(@Nonnull Consumer<Result> theConsumer) {
		Validate.notNull(theConsumer);
		myConsumers.add(theConsumer);
	}

	private void notifyConsumers(Result theResult) {
		myConsumers.forEach(t -> t.accept(theResult));
	}

	/**
	 * Contains the results of the capture
	 */
	public static class Result {
		private final int myWrittenChars;

		public RequestDetails getRequestDetails() {
			return myRequestDetails;
		}

		private final RequestDetails myRequestDetails;

		public Result(RequestDetails theRequestDetails, int theWrittenChars) {
			myRequestDetails = theRequestDetails;
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
