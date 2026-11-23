/*-
 * #%L
 * HAPI FHIR Test Utilities
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
package ca.uhn.fhir.test.utilities.client;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletRequestWrapper;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import org.mockito.Mockito;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

/**
 * This is a Servle {@link Filter} implementation that counts the number of bytes read
 * from the HTTP request entity.
 */
public class RequestCountingFilter implements Filter {

	private final AtomicLong myRequestReadByteCount = new AtomicLong();

	public void resetRequestByteCount() {
		myRequestReadByteCount.set(0);
	}

	public long getRequestReadByteCount() {
		return myRequestReadByteCount.get();
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		MyServletRequestWrapper requestWrapper = new MyServletRequestWrapper((HttpServletRequest) request);
		try {
			chain.doFilter(requestWrapper, response);
		} finally {
			myRequestReadByteCount.addAndGet(requestWrapper.getByteCount());
		}
	}

	private class MyServletRequestWrapper extends HttpServletRequestWrapper {

		private ByteCountingServletInputStream myByteCountingServletInputStream;

		public MyServletRequestWrapper(HttpServletRequest request) {
			super(request);
		}

		@Override
		public synchronized ServletInputStream getInputStream() throws IOException {
			if (myByteCountingServletInputStream == null) {
				myByteCountingServletInputStream = new ByteCountingServletInputStream(super.getInputStream());
			}
			return myByteCountingServletInputStream;
		}

		@Override
		public BufferedReader getReader() {
			throw new UnsupportedOperationException();
		}

		public synchronized long getByteCount() {
			if (myByteCountingServletInputStream == null) {
				return 0;
			}
			return myByteCountingServletInputStream.getByteCount();
		}

	}


	public class ByteCountingServletInputStream extends ServletInputStream {
		private final ServletInputStream innerStream;
		private long byteCount = 0;

		public ByteCountingServletInputStream(ServletInputStream innerStream) {
			this.innerStream = innerStream;
		}

		@Override
		public int read() throws IOException {
			int b = innerStream.read();
			if (b != -1) {
				byteCount++;
			}
			return b;
		}

		@Override
		public int read(byte[] b, int off, int len) throws IOException {
			int read = innerStream.read(b, off, len);
			if (read > 0) {
				byteCount += read;
			}
			return read;
		}

		public long getByteCount() {
			return byteCount;
		}

		// Delegate required abstract methods from ServletInputStream
		@Override
		public boolean isFinished() {
			return innerStream.isFinished();
		}

		@Override
		public boolean isReady() {
			return innerStream.isReady();
		}

		@Override
		public void setReadListener(ReadListener readListener) {
			innerStream.setReadListener(readListener);
		}
	}
}
