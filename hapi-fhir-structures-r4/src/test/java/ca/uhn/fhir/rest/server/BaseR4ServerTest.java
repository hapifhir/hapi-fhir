package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.api.BundleInclusionRule;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.api.ServerValidationModeEnum;
import ca.uhn.fhir.test.utilities.HttpClientExtension;
import ca.uhn.fhir.test.utilities.JettyUtil;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.ee10.servlet.ServletHandler;
import org.eclipse.jetty.ee10.servlet.ServletHolder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.extension.RegisterExtension;

public class BaseR4ServerTest {
	private static final FhirContext ourCtx = FhirContext.forR4Cached();

	@RegisterExtension
	public RestfulServerExtension ourServer = new RestfulServerExtension(ourCtx)
		 .registerProvider(new PlaceholderProvider())
		 .withPagingProvider(new FifoMemoryPagingProvider(100))
		 .setDefaultResponseEncoding(EncodingEnum.XML)
		 .withServer(s->s.setBundleInclusionRule(BundleInclusionRule.BASED_ON_RESOURCE_PRESENCE));

	@RegisterExtension
	public HttpClientExtension ourClient = new HttpClientExtension();

	protected void startServer(Object theProvider) throws Exception {
		ourServer.getRestfulServer().registerProvider(theProvider);
	}

	private static class PlaceholderProvider {

		@Operation(name = "placeholderOperation")
		public void placeholderOperation() {
			// nothing
		}

	}
}
