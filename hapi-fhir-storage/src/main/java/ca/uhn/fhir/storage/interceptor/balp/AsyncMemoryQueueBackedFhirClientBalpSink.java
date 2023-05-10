package ca.uhn.fhir.storage.interceptor.balp;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * This implementation of the {@link IBalpAuditEventSink} transmits audit events to
 * a FHIR endpoint for creation, using a standard fhir <i>create</i> event. The target
 * server FHIR version does not need to match the FHIR version of the AuditEvent source,
 * events will be converted automatically prior to sending.
 * <p>
 * This sink transmits events asynchronously using an in-memory queue. This means that
 * in the event of a server shutdown data could be lost.
 * </p>
 */
public class AsyncMemoryQueueBackedFhirClientBalpSink extends FhirClientBalpSink implements IBalpAuditEventSink {

	private static final Logger ourLog = LoggerFactory.getLogger(AsyncMemoryQueueBackedFhirClientBalpSink.class);
	private static final AtomicLong ourNextThreadId = new AtomicLong(0);
	private final BlockingQueue<IBaseResource> myQueue;
	private boolean myRunning;
	private TransmitterThread myThread;

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
	public AsyncMemoryQueueBackedFhirClientBalpSink(@Nonnull FhirContext theFhirContext, @Nonnull String theTargetBaseUrl) {
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
	public AsyncMemoryQueueBackedFhirClientBalpSink(@Nonnull FhirContext theFhirContext, @Nonnull String theTargetBaseUrl, @Nullable List<Object> theClientInterceptors) {
		this(createClient(theFhirContext, theTargetBaseUrl, theClientInterceptors));
	}


	/**
	 * Constructor
	 *
	 * @param theClient The FHIR client to use as a sink.
	 */
	public AsyncMemoryQueueBackedFhirClientBalpSink(IGenericClient theClient) {
		super(theClient);
		myQueue = new LinkedBlockingQueue<>(100);
	}

	@Override
	protected void recordAuditEvent(IBaseResource theAuditEvent) {
		try {
			myQueue.put(theAuditEvent);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			ourLog.info("Interrupted while recording element");
			throw new InternalErrorException(e);
		}
	}

	@PostConstruct
	public void start() {
		if (!myRunning) {
			myRunning = true;
			myThread = new TransmitterThread();
			myThread.start();
		}
	}

	@PreDestroy
	public void stop() {
		if (myRunning) {
			myRunning = false;
			myThread.interrupt();
		}
	}

	public boolean isRunning() {
		return myThread != null && myThread.isRunning();
	}

	private class TransmitterThread extends Thread {

		private boolean myThreadRunning;

		public TransmitterThread() {
			setName("BalpClientSink-" + ourNextThreadId.getAndIncrement());
		}

		@Override
		public void run() {
			ourLog.info("Starting BALP Client Sink Transmitter");
			myThreadRunning = true;
			while (myRunning) {
				IBaseResource next = null;
				try {
					next = myQueue.poll(10, TimeUnit.SECONDS);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					ourLog.info("Interrupted while accessing element");
				}

				// TODO: Currently we transmit events one by one, but a nice optimization
				// would be to batch them into FHIR transaction Bundles. If we do this, we
				// would get better performance, but we'd also want to have some retry
				// logic that submits events individually if a transaction fails.

				if (next != null) {
					try {
						transmitEventToClient(next);
					} catch (Exception e) {
						ourLog.warn("Failed to transmit AuditEvent to sink: {}", e.toString());
						myQueue.add(next);
					}
				}

			}
			ourLog.info("Stopping BALP Client Sink Transmitter");
			myThreadRunning = false;
		}

		public boolean isRunning() {
			return myThreadRunning;
		}
	}

}
