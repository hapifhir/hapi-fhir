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
import ca.uhn.hapi.converters.canonical.VersionCanonicalizer;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.AuditEvent;

import java.util.List;

public class FhirClientBalpSink implements IBalpAuditEventSink {

	protected final IGenericClient myClient;
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
	public FhirClientBalpSink(
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
	public FhirClientBalpSink(IGenericClient theClient) {
		myClient = theClient;
		myVersionCanonicalizer = new VersionCanonicalizer(myClient.getFhirContext());
	}

	@Override
	public void recordAuditEvent(AuditEvent theAuditEvent) {
		IBaseResource auditEvent = myVersionCanonicalizer.auditEventFromCanonical(theAuditEvent);
		recordAuditEvent(auditEvent);
	}

	protected void recordAuditEvent(IBaseResource auditEvent) {
		transmitEventToClient(auditEvent);
	}

	protected void transmitEventToClient(IBaseResource auditEvent) {
		myClient.create().resource(auditEvent).execute();
	}

	static IGenericClient createClient(
			@Nonnull FhirContext theFhirContext,
			@Nonnull String theTargetBaseUrl,
			@Nullable List<Object> theClientInterceptors) {
		Validate.notNull(theFhirContext, "theFhirContext must not be null");
		Validate.notBlank(theTargetBaseUrl, "theTargetBaseUrl must not be null or blank");
		IGenericClient client = theFhirContext.newRestfulGenericClient(theTargetBaseUrl);
		if (theClientInterceptors != null) {
			theClientInterceptors.forEach(client::registerInterceptor);
		}
		return client;
	}
}
