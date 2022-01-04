package ca.uhn.fhir.rest.client.interceptor;

/*-
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

import ca.uhn.fhir.rest.client.api.IClientInterceptor;
import ca.uhn.fhir.rest.client.api.IHttpRequest;
import ca.uhn.fhir.rest.client.api.IHttpResponse;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;

import java.io.IOException;

/**
 * This is a client interceptor that captures the current request and response
 * in a ThreadLocal variable, meaning that it can work in multithreaded
 * environments without mixing up requests.
 * <p>
 * Use this with caution, since <b>this interceptor does not automatically clean up</b>
 * the ThreadLocal after setting it. You must make sure to call
 * {@link #clearThreadLocals()} after a given request has been completed,
 * or you will end up leaving stale request/response objects associated
 * with threads that no longer need them.
 * </p>
 *
 * @see CapturingInterceptor for an equivalent interceptor that does not use a ThreadLocal
 * @since 3.5.0
 */
public class ThreadLocalCapturingInterceptor implements IClientInterceptor {

	private final ThreadLocal<IHttpRequest> myRequestThreadLocal = new ThreadLocal<>();
	private final ThreadLocal<IHttpResponse> myResponseThreadLocal = new ThreadLocal<>();
	private boolean myBufferResponse;

	/**
	 * This method should be called at the end of any request process, in
	 * order to clear the last request and response from the current thread.
	 */
	public void clearThreadLocals() {
		myRequestThreadLocal.remove();
		myResponseThreadLocal.remove();
	}

	public IHttpRequest getRequestForCurrentThread() {
		return myRequestThreadLocal.get();
	}

	public IHttpResponse getResponseForCurrentThread() {
		return myResponseThreadLocal.get();
	}

	@Override
	public void interceptRequest(IHttpRequest theRequest) {
		myRequestThreadLocal.set(theRequest);
	}

	@Override
	public void interceptResponse(IHttpResponse theResponse) {
		if (isBufferResponse()) {
			CapturingInterceptor.bufferResponse(theResponse);
		}
		myResponseThreadLocal.set(theResponse);
	}

	/**
	 * Should we buffer (capture) the response body? This defaults to
	 * <code>false</code>. Set to <code>true</code> if you are planning on
	 * examining response bodies after the response processing is complete.
	 */
	public boolean isBufferResponse() {
		return myBufferResponse;
	}

	/**
	 * Should we buffer (capture) the response body? This defaults to
	 * <code>false</code>. Set to <code>true</code> if you are planning on
	 * examining response bodies after the response processing is complete.
	 */
	public ThreadLocalCapturingInterceptor setBufferResponse(boolean theBufferResponse) {
		myBufferResponse = theBufferResponse;
		return this;
	}
}
