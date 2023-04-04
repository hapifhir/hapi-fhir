package ca.uhn.fhir.jpa.interceptor.balp;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.hapi.converters.canonical.VersionCanonicalizer;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.AuditEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class FhirClientBalpSink implements IBalpAuditEventSink {

	private final IGenericClient myClient;
	private final VersionCanonicalizer myVersionCanonicalizer;

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
	public FhirClientBalpSink(@Nonnull FhirContext theFhirContext, @Nonnull String theTargetBaseUrl) {
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
	public FhirClientBalpSink(@Nonnull FhirContext theFhirContext, @Nonnull String theTargetBaseUrl, @Nullable List<Object> theClientInterceptors) {
		this(createClient(theFhirContext, theTargetBaseUrl, theClientInterceptors));
	}

	/**
	 * Constructor
	 *
	 * @param theClient The FHIR client to use as a sink.
	 */
	public FhirClientBalpSink(IGenericClient theClient) {
		myClient = theClient;
		myVersionCanonicalizer = new VersionCanonicalizer(myClient.getFhirContext());
	}

	@Override
	public void recordAuditEvent(AuditEvent theAuditEvent) {
		IBaseResource auditEvent = myVersionCanonicalizer.auditEventFromCanonical(theAuditEvent);
		myClient
			.create()
			.resource(auditEvent)
			.execute();
	}

	private static IGenericClient createClient(@Nonnull FhirContext theFhirContext, @Nonnull String theTargetBaseUrl, @Nullable List<Object> theClientInterceptors) {
		Validate.notNull(theFhirContext, "theFhirContext must not be null");
		Validate.notBlank(theTargetBaseUrl, "theTargetBaseUrl must not be null or blank");
		IGenericClient client = theFhirContext.newRestfulGenericClient(theTargetBaseUrl);
		if (theClientInterceptors != null) {
			theClientInterceptors.forEach(client::registerInterceptor);
		}
		return client;
	}
}
