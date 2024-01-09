/*-
 * #%L
 * HAPI FHIR Storage api
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
package ca.uhn.fhir.storage.interceptor.balp;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.util.BundleBuilder;
import ca.uhn.fhir.util.ThreadPoolUtil;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.annotation.PreDestroy;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * This implementation of the {@link IBalpAuditEventSink} transmits audit events to
 * a FHIR endpoint for creation, using a standard fhir <i>create</i> event. The target
 * server FHIR version does not need to match the FHIR version of the AuditEvent source,
 * events will be converted automatically prior to sending.
 * <p>
 * This sink transmits events asynchronously using an in-memory queue. This means that
 * in the event of a server shutdown or unavailability of the target server <b>data could be lost</b>.
 * </p>
 */
public class AsyncMemoryQueueBackedFhirClientBalpSink extends FhirClientBalpSink implements IBalpAuditEventSink {

	public static final IBaseResource[] EMPTY_RESOURCE_ARRAY = new IBaseResource[0];
	private static final AtomicLong ourNextThreadId = new AtomicLong(0);
	private static final Logger ourLog = LoggerFactory.getLogger(AsyncMemoryQueueBackedFhirClientBalpSink.class);
	private final List<IBaseResource> myQueue = new ArrayList<>(100);
	private final ThreadPoolTaskExecutor myThreadPool;
	private final Runnable myTransmitterTask = new TransmitterTask();

	/**
	 * Sets the FhirContext to use when initiating outgoing connections
	 *
	 * @param theFhirContext   The FhirContext instance. This context must be
	 *                         for the FHIR Version supported by the target/sink
	 *                         server (as opposed to the FHIR Version supported
	 *                         by the audit source).
	 * @param theTargetBaseUrl The FHIR server base URL for the target/sink server to
	 *                         receive audit events.
	 */
	public AsyncMemoryQueueBackedFhirClientBalpSink(
			@Nonnull FhirContext theFhirContext, @Nonnull String theTargetBaseUrl) {
		this(theFhirContext, theTargetBaseUrl, null);
	}

	/**
	 * Sets the FhirContext to use when initiating outgoing connections
	 *
	 * @param theFhirContext        The FhirContext instance. This context must be
	 *                              for the FHIR Version supported by the target/sink
	 *                              server (as opposed to the FHIR Version supported
	 *                              by the audit source).
	 * @param theTargetBaseUrl      The FHIR server base URL for the target/sink server to
	 *                              receive audit events.
	 * @param theClientInterceptors An optional list of interceptors to register against
	 *                              the client. May be {@literal null}.
	 */
	public AsyncMemoryQueueBackedFhirClientBalpSink(
			@Nonnull FhirContext theFhirContext,
			@Nonnull String theTargetBaseUrl,
			@Nullable List<Object> theClientInterceptors) {
		this(createClient(theFhirContext, theTargetBaseUrl, theClientInterceptors));
	}

	/**
	 * Constructor
	 *
	 * @param theClient The FHIR client to use as a sink.
	 */
	public AsyncMemoryQueueBackedFhirClientBalpSink(IGenericClient theClient) {
		super(theClient);
		myThreadPool = ThreadPoolUtil.newThreadPool(
				1, 1, "BalpClientSink-" + ourNextThreadId.getAndIncrement() + "-", Integer.MAX_VALUE);
	}

	@Override
	protected void recordAuditEvent(IBaseResource theAuditEvent) {
		synchronized (myQueue) {
			myQueue.add(theAuditEvent);
		}
		myThreadPool.submit(myTransmitterTask);
	}

	@PreDestroy
	public void stop() {
		myThreadPool.shutdown();
	}

	private class TransmitterTask implements Runnable {

		@Override
		public void run() {
			IBaseResource[] queue;
			synchronized (myQueue) {
				if (myQueue.isEmpty()) {
					queue = EMPTY_RESOURCE_ARRAY;
				} else {
					queue = myQueue.toArray(EMPTY_RESOURCE_ARRAY);
					myQueue.clear();
				}
			}

			if (queue.length == 0) {
				return;
			}

			BundleBuilder bundleBuilder = new BundleBuilder(myClient.getFhirContext());
			for (IBaseResource next : queue) {
				bundleBuilder.addTransactionCreateEntry(next);
			}

			IBaseBundle transactionBundle = bundleBuilder.getBundle();
			try {
				myClient.transaction().withBundle(transactionBundle).execute();
				return;
			} catch (BaseServerResponseException e) {
				ourLog.error(
						"Failed to transmit AuditEvent items to target. Will re-attempt {} failed events once. Error: {}",
						queue.length,
						e.toString());
			}

			// Retry once then give up
			for (IBaseResource next : queue) {
				try {
					myClient.create().resource(next).execute();
				} catch (BaseServerResponseException e) {
					ourLog.error("Second failure uploading AuditEvent. Error: {}", e.toString());
				}
			}
		}
	}
}
