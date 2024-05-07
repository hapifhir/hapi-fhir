package ca.uhn.fhir.rest.server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.rest.annotation.IncludeParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.test.utilities.HttpClientExtension;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.TestUtil;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.hl7.fhir.dstu3.model.Organization;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class SearchWithIncludesDstu3Test {

	private static final FhirContext ourCtx = FhirContext.forDstu3Cached();
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(SearchWithIncludesDstu3Test.class);

	@RegisterExtension
	private RestfulServerExtension ourServer  = new RestfulServerExtension(ourCtx)
		 .setDefaultResponseEncoding(EncodingEnum.XML)
		 .registerProvider(new DummyPatientResourceProvider())
		 .withPagingProvider(new FifoMemoryPagingProvider(100))
		 .setDefaultPrettyPrint(false);

	@RegisterExtension
	private HttpClientExtension ourClient = new HttpClientExtension();

	@Test
	public void testSearchIncludesReferences() throws Exception {
		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient?_pretty=true&_include=Patient:organization&_include=Organization:" + Organization.SP_PARTOF);
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
		IOUtils.closeQuietly(status.getEntity().getContent());
		ourLog.info(responseContent);
		assertEquals(200, status.getStatusLine().getStatusCode());

		// Response should include both the patient, and the organization that was referred to
		// by a linked resource
		
		assertThat(responseContent).contains("<name value=\"child\"/>");
		assertThat(responseContent).contains("<name value=\"parent\"/>");
		assertThat(responseContent).contains("<name value=\"grandparent\"/>");
		assertThat(responseContent).contains("<mode value=\"include\"/>");
		
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

		//@formatter:off
		@Search()
		public List<Patient> search(@IncludeParam() Set<Include> theIncludes) {
			ourLog.info("Includes: {}", theIncludes);
			
			// Grandparent has ID, other orgs don't
			Organization gp = new Organization();
			gp.setId("Organization/GP");
			gp.setName("grandparent");
			
			Organization parent = new Organization();
			parent.setName("parent");
			parent.getPartOf().setResource(gp);
			
			Organization child = new Organization();
			child.setName("child");
			child.getPartOf().setResource(parent);
			
			Patient patient = new Patient();
			patient.setId("Patient/FOO");
			patient.getManagingOrganization().setResource(child);

			ArrayList<Patient> retVal = new ArrayList<Patient>();
			retVal.add(patient);
			return retVal;
		}
		//@formatter:on


	}

}
