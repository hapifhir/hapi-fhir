package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.valueset.BundleEntrySearchModeEnum;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.gclient.StringClientParam;
import ca.uhn.fhir.rest.param.TokenAndListParam;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.interceptor.SearchPreferHandlingInterceptor;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class SearchPreferHandlingInterceptorTest {

	private final FhirContext myCtx = FhirContext.forR4Cached();
	@RegisterExtension
	public RestfulServerExtension myRestfulServerExtension = new RestfulServerExtension(myCtx)
		.registerProvider(new DummyPatientResourceProvider())
		.withServer(t -> t.setDefaultResponseEncoding(EncodingEnum.JSON))
		.withServer(t -> t.setPagingProvider(new FifoMemoryPagingProvider(10)))
		.registerInterceptor(new SearchPreferHandlingInterceptor());
	private int myPort;
	private IGenericClient myClient;

	@BeforeEach
	public void before() {
		myClient = myRestfulServerExtension.getFhirClient();
		myPort = myRestfulServerExtension.getPort();
	}


	@Test
	public void testSearchWithInvalidParam_NoHeader() {
		try {
			myClient
				.search()
				.forResource(Patient.class)
				.where(new StringClientParam("foo").matches().value("bar"))
				.prettyPrint()
				.returnBundle(Bundle.class)
				.encodedJson()
				.execute();
			fail();
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage(), containsString("Unknown search parameter \"foo\" for resource type \"Patient\". Valid search parameters for this search are: [identifier]"));
		}

	}

	@Test
	public void testSearchWithUnknownResourceType() throws IOException {
		try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
			try (CloseableHttpResponse result = client.execute(new HttpGet("http://localhost:" + myPort + "/BadResource?foo=bar"))) {
				assertEquals(404, result.getStatusLine().getStatusCode());
				String response = IOUtils.toString(result.getEntity().getContent(), StandardCharsets.UTF_8);
				assertThat(response, containsString("Unknown resource type 'BadResource' - Server knows how to handle: [Patient, OperationDefinition]"));
			}
		}
	}

	@Test
	public void testSearchWithInvalidParam_StrictHeader() {
		try {
			myClient
				.search()
				.forResource(Patient.class)
				.where(new StringClientParam("foo").matches().value("bar"))
				.withAdditionalHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_HANDLING + "=" + Constants.HEADER_PREFER_HANDLING_STRICT)
				.prettyPrint()
				.returnBundle(Bundle.class)
				.encodedJson()
				.execute();
			fail();
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage(), containsString("Unknown search parameter \"foo\" for resource type \"Patient\". Valid search parameters for this search are: [identifier]"));
		}

	}

	@Test
	public void testSearchWithInvalidParam_UnrelatedPreferHeader() {
		try {
			myClient
				.search()
				.forResource(Patient.class)
				.where(new StringClientParam("foo").matches().value("bar"))
				.withAdditionalHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RETURN + "=" + Constants.HEADER_PREFER_RETURN_REPRESENTATION)
				.prettyPrint()
				.returnBundle(Bundle.class)
				.encodedJson()
				.execute();
			fail();
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage(), containsString("Unknown search parameter \"foo\" for resource type \"Patient\". Valid search parameters for this search are: [identifier]"));
		}

	}

	@Test
	public void testSearchWithInvalidParam_LenientHeader() {
		Bundle outcome = myClient
			.search()
			.forResource(Patient.class)
			.where(new StringClientParam("foo").matches().value("bar"))
			.and(Patient.IDENTIFIER.exactly().codes("BLAH"))
			.prettyPrint()
			.returnBundle(Bundle.class)
			.withAdditionalHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_HANDLING + "=" + Constants.HEADER_PREFER_HANDLING_LENIENT)
			.encodedJson()
			.execute();
		assertEquals(200, outcome.getTotal());
		assertEquals("http://localhost:" + myPort + "/Patient?_format=json&_pretty=true&identifier=BLAH", outcome.getLink(Constants.LINK_SELF).getUrl());
	}

	public static class DummyPatientResourceProvider implements IResourceProvider {

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return Patient.class;
		}

		@SuppressWarnings("rawtypes")
		@Search()
		public List search(
			@OptionalParam(name = Patient.SP_IDENTIFIER) TokenAndListParam theIdentifiers) {
			ArrayList<Patient> retVal = new ArrayList<>();

			for (int i = 0; i < 200; i++) {
				Patient patient = new Patient();
				patient.getIdElement().setValue("Patient/" + i + "/_history/222");
				ResourceMetadataKeyEnum.ENTRY_SEARCH_MODE.put(patient, BundleEntrySearchModeEnum.INCLUDE.getCode());
				patient.addName(new HumanName().setFamily("FAMILY"));
				patient.setActive(true);
				retVal.add(patient);
			}
			return retVal;
		}

	}


}
