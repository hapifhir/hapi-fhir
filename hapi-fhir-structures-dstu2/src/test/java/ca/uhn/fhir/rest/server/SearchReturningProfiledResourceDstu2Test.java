package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.api.AddProfileTagEnum;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.interceptor.ResponseHighlighterInterceptor;
import ca.uhn.fhir.test.utilities.HttpClientExtension;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.TestUtil;
import ca.uhn.fhir.validation.PatientProfileDstu2;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SearchReturningProfiledResourceDstu2Test {

	private static final FhirContext ourCtx = FhirContext.forDstu2Cached();
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(SearchReturningProfiledResourceDstu2Test.class);

	@RegisterExtension
	public static final RestfulServerExtension ourServer  = new RestfulServerExtension(ourCtx)
		.setDefaultResponseEncoding(EncodingEnum.XML)
		.registerProvider(new DummyPatientResourceProvider())
		.registerInterceptor(new ResponseHighlighterInterceptor())
		.withPagingProvider(new FifoMemoryPagingProvider(100))
		.setDefaultPrettyPrint(false);

	@RegisterExtension
	public static final HttpClientExtension ourClient = new HttpClientExtension();

	@BeforeEach
	public void before() {
		ourCtx.setAddProfileTagWhenEncoding(AddProfileTagEnum.ONLY_FOR_CUSTOM);
	}

	@AfterEach
	public void after() {
		ourCtx.setDefaultTypeForProfile("http://ahr.copa.inso.tuwien.ac.at/StructureDefinition/Patient", null);
	}
	
	
	@Test
	public void testClientTypedRequest() throws Exception {
		IGenericClient client = ourCtx.newRestfulGenericClient(ourServer.getBaseUrl() + "/");
		Bundle bundle = client.search().forResource(PatientProfileDstu2.class).returnBundle(Bundle.class).execute();

		assertEquals(PatientProfileDstu2.class, bundle.getEntry().get(0).getResource().getClass());
	}

	@Test
	public void testClientUntypedRequestWithHint() throws Exception {
		ourCtx.setDefaultTypeForProfile("http://ahr.copa.inso.tuwien.ac.at/StructureDefinition/Patient", PatientProfileDstu2.class);
		IGenericClient client = ourCtx.newRestfulGenericClient(ourServer.getBaseUrl() + "/");
		Bundle bundle = client.search().forResource(Patient.class).returnBundle(Bundle.class).execute();

		assertEquals(PatientProfileDstu2.class, bundle.getEntry().get(0).getResource().getClass());
	}

	@Test
	public void testClientUntypedRequestWithoutHint() throws Exception {
		IGenericClient client = ourCtx.newRestfulGenericClient(ourServer.getBaseUrl() + "/");
		Bundle bundle = client.search().forResource(Patient.class).returnBundle(Bundle.class).execute();

		assertEquals(Patient.class, bundle.getEntry().get(0).getResource().getClass());
	}

	@Test
	public void testProfilesGetAdded() throws Exception {
		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient?_pretty=true");
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		ourLog.info(responseContent);

		assertThat(responseContent).doesNotContain("html");
		assertThat(responseContent).contains("<profile value=\"http://foo\"/>");
		assertThat(responseContent).contains("<profile value=\"http://ahr.copa.inso.tuwien.ac.at/StructureDefinition/Patient\"/>");

	}

	@Test
	public void testProfilesGetAddedHtml() throws Exception {
		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient?_format=html");
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		ourLog.info(responseContent);

		assertThat(responseContent).contains("html");
		assertThat(responseContent).contains("http://foo");
		assertThat(responseContent).contains("http://ahr.copa.inso.tuwien.ac.at/StructureDefinition/Patient");

	}

	@AfterAll
	public static void afterClassClearContext() throws Exception {
		TestUtil.randomizeLocaleAndTimezone();
	}

	public static class DummyPatientResourceProvider implements IResourceProvider {

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return Patient.class;
		}

		/**
		 * Basic search, available for every resource. Allows search per id (use of read is better, though), fulltext
		 * content
		 * and lastUpdated timestamp.
		 * 
		 * @param id
		 * @param content
		 * @param lastUpdated
		 * @return
		 */
		//@formatter:off
		@Search
		public List<Patient> search(
				@Description(shortDefinition = "The resource id") 
				@OptionalParam(name = "_id") StringParam id, 
				@Description(shortDefinition = "Search the contents of the resource's data using a fulltext search") 
				@OptionalParam(name = Constants.PARAM_CONTENT) StringParam content,
				@Description(shortDefinition = "Search for resources considering the time of their last update") 
				@OptionalParam(name = Constants.PARAM_LASTUPDATED) DateRangeParam lastUpdated) {
			//@formatter:on

			List<Patient> result = new ArrayList<Patient>();

			PatientProfileDstu2 pp = new PatientProfileDstu2();

			ResourceMetadataKeyEnum.PROFILES.put(pp, Collections.singletonList(new IdDt("http://foo")));

			pp.setId("123");
			pp.getOwningOrganization().setReference("Organization/456");
			result.add(pp);

			ourLog.info("Search: Everything ok. Going to return results!");
			return result;
		}

	}

}
