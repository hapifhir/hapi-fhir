package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.annotation.Validate;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.system.HapiSystemProperties;
import ca.uhn.fhir.test.utilities.HttpTestResponse;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.TestUtil;
import ca.uhn.fhir.util.VersionUtil;
import org.hl7.fhir.dstu3.hapi.rest.server.ServerCapabilityStatementProvider;
import org.hl7.fhir.dstu3.model.CapabilityStatement;
import org.hl7.fhir.dstu3.model.Patient;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MetadataCapabilityStatementDstu3Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(MetadataCapabilityStatementDstu3Test.class);
	private static final FhirContext ourCtx = FhirContext.forDstu3Cached();

	@RegisterExtension
	private RestfulServerExtension ourServer  = new RestfulServerExtension(ourCtx)
		 .setDefaultResponseEncoding(EncodingEnum.XML)
		 .registerProvider(new DummyPatientResourceProvider())
		 .withPagingProvider(new FifoMemoryPagingProvider(100))
		 .setDefaultPrettyPrint(false)
		 .withServer(s->s.setServerConformanceProvider(new ServerCapabilityStatementProvider(s).setCache(false)));

	static {
		HapiSystemProperties.enableTestMode();
	}

	@AfterEach
	public void after() {
		ourServer.setServerAddressStrategy(new IncomingRequestAddressStrategy());
	}

	@Test
	public void testElements() throws Exception {
		String output;

		output = ourServer.fhirRequest("/metadata?_elements=fhirVersion&_pretty=true").get().assertStatus(200).getBody();
		ourLog.info(output);
		assertThat(output).contains("<CapabilityStatement");
		assertThat(output).contains("<meta>", "SUBSETTED", "</meta>");
	}

	@Test
	public void testHttpMethods() throws Exception {
		String output;

		HttpTestResponse status = ourServer.fhirRequest("/metadata").get();
		output = status.getBody();
		status.assertStatus(200);
		assertThat(output).contains("<CapabilityStatement");
		assertThat(status.getHeader(Constants.HEADER_POWERED_BY)).contains("HAPI FHIR " + VersionUtil.getVersion());
		assertThat(status.getHeader(Constants.HEADER_POWERED_BY)).contains("REST Server (FHIR Server; FHIR " + ourCtx.getVersion().getVersion().getFhirVersionString() + "/" + ourCtx.getVersion().getVersion().name() + ")");

		status = ourServer.fhirRequest("/metadata").method("POST", new byte[0], null);
		output = status.getBody();
		status.assertStatus(405);
		assertEquals("<OperationOutcome xmlns=\"http://hl7.org/fhir\"><issue><severity value=\"error\"/><code value=\"processing\"/><diagnostics value=\"" + Msg.code(388) + "/metadata request must use HTTP GET or HTTP HEAD\"/></issue></OperationOutcome>", output);

		/*
		 * There is no @read on the RP below, so this should fail. Otherwise it
		 * would be interpreted as a read on ID "metadata"
		 */
		status = ourServer.fhirRequest("/Patient/metadata").get();
		output = status.getBody();
		status.assertStatus(400);
	}

	@Test
	public void testResponseContainsBaseUrl() throws Exception {
		String output;

		output = ourServer.fhirRequest("/metadata?_format=json").get().assertStatus(200).getBody();
		ourLog.info(output);
		CapabilityStatement cs = ourCtx.newJsonParser().parseResource(CapabilityStatement.class, output);

		assertEquals(ourServer.getBaseUrl() + "/", cs.getImplementation().getUrl());
	}

	@Test
	public void testHeadElements() throws Exception {

		HttpTestResponse status = ourServer.fhirRequest("/metadata?_elements=fhirVersion&_pretty=true").head();
		status.getAllHeaders();
		assertEquals(200, status.getStatusCode());
	}

	@Test
	public void testResponseContainsBaseUrlFixed() throws Exception {
		ourServer.setServerAddressStrategy(new HardcodedServerAddressStrategy("http://foo/bar"));

		String output;

		output = ourServer.fhirRequest("/metadata?_format=json").get().assertStatus(200).getBody();
		ourLog.info(output);
		CapabilityStatement cs = ourCtx.newJsonParser().parseResource(CapabilityStatement.class, output);

		assertEquals("http://foo/bar", cs.getImplementation().getUrl());
	}

	@Test
	public void testSummary() throws Exception {
		String output;

		// With
		output = ourServer.fhirRequest("/metadata?_summary=true&_pretty=true").get().assertStatus(200).getBody();
		ourLog.info(output);
		assertThat(output).contains("<CapabilityStatement");
		assertThat(output).contains("<meta>", "SUBSETTED", "</meta>");
		assertThat(output).doesNotContain("searchParam");

		// Without
		output = ourServer.fhirRequest("/metadata?_pretty=true").get().assertStatus(200).getBody();
		ourLog.info(output);
		assertThat(output).contains("<CapabilityStatement");
		assertThat(output).doesNotContain("<meta>", "SUBSETTED", "</meta>");
		assertThat(output).contains("searchParam");
	}

	@AfterAll
	public static void afterClassClearContext() throws Exception {
		TestUtil.randomizeLocaleAndTimezone();
	}

	@SuppressWarnings("unused")
	public static class DummyPatientResourceProvider implements IResourceProvider {

		@Override
		public Class<Patient> getResourceType() {
			return Patient.class;
		}

		@Search
		public List<Patient> search(@OptionalParam(name = "foo") StringParam theFoo) {
			throw new UnsupportedOperationException();
		}

		@Validate()
		public MethodOutcome validate(@ResourceParam Patient theResource) {
			return new MethodOutcome();
		}
	}

}
