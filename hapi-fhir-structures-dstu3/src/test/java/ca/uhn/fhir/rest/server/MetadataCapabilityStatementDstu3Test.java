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

public class MetadataCapabilityStatementDstu3Test {

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
	public void testElements() {
		ourServer.fhirRequest("/metadata?_elements=fhirVersion&_pretty=true")
				.get()
				.assertStatus(200)
				.assertBodyContains("<CapabilityStatement")
				.assertBodyContains("<meta>", "SUBSETTED", "</meta>");
	}

	@Test
	public void testHttpMethods() {
		HttpTestResponse status = ourServer.fhirRequest("/metadata")
				.get()
				.assertStatus(200)
				.assertBodyContains("<CapabilityStatement");
		assertThat(status.getHeader(Constants.HEADER_POWERED_BY)).contains("HAPI FHIR " + VersionUtil.getVersion());
		assertThat(status.getHeader(Constants.HEADER_POWERED_BY)).contains("REST Server (FHIR Server; FHIR " + ourCtx.getVersion().getVersion().getFhirVersionString() + "/" + ourCtx.getVersion().getVersion().name() + ")");

		ourServer.fhirRequest("/metadata")
				.method("POST", new byte[0], null)
				.assertStatus(405)
				.assertBodyEquals("<OperationOutcome xmlns=\"http://hl7.org/fhir\"><issue><severity value=\"error\"/><code value=\"processing\"/><diagnostics value=\"" + Msg.code(388) + "/metadata request must use HTTP GET or HTTP HEAD\"/></issue></OperationOutcome>");

		/*
		 * There is no @read on the RP below, so this should fail. Otherwise it
		 * would be interpreted as a read on ID "metadata"
		 */
		ourServer.fhirRequest("/Patient/metadata").get().assertStatus(400);
	}

	@Test
	public void testResponseContainsBaseUrl() {
		CapabilityStatement cs = ourServer.fhirRequest("/metadata?_format=json")
				.get()
				.assertStatus(200)
				.assertContentTypeEquals(Constants.CT_FHIR_JSON_NEW)
				.getBodyAsResource(CapabilityStatement.class);

		assertThat(cs.getImplementation().getUrl()).isEqualTo(ourServer.getBaseUrl() + "/");
	}

	@Test
	public void testHeadElements() {

		HttpTestResponse status = ourServer.fhirRequest("/metadata?_elements=fhirVersion&_pretty=true").head();
		status.getAllHeaders();
		status.assertStatus(200);
	}

	@Test
	public void testResponseContainsBaseUrlFixed() {
		ourServer.setServerAddressStrategy(new HardcodedServerAddressStrategy("http://foo/bar"));

		CapabilityStatement cs = ourServer.fhirRequest("/metadata?_format=json")
				.get()
				.assertStatus(200)
				.assertContentTypeEquals(Constants.CT_FHIR_JSON_NEW)
				.getBodyAsResource(CapabilityStatement.class);

		assertThat(cs.getImplementation().getUrl()).isEqualTo("http://foo/bar");
	}

	@Test
	public void testSummary() {
		// With
		ourServer.fhirRequest("/metadata?_summary=true&_pretty=true")
				.get()
				.assertStatus(200)
				.assertBodyContains("<CapabilityStatement")
				.assertBodyContains("<meta>", "SUBSETTED", "</meta>")
				.assertBodyDoesNotContain("searchParam");

		// Without
		ourServer.fhirRequest("/metadata?_pretty=true")
				.get()
				.assertStatus(200)
				.assertBodyContains("<CapabilityStatement")
				.assertBodyDoesNotContain("<meta>", "SUBSETTED", "</meta>")
				.assertBodyContains("searchParam");
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
