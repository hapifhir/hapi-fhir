package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.test.utilities.HttpClientExtension;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.TestUtil;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.hl7.fhir.dstu2.model.Bundle;
import org.hl7.fhir.dstu2.model.Patient;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SearchWithHl7OrgDstu2BundleTest {

  private static final FhirContext ourCtx = FhirContext.forDstu2Hl7OrgCached();
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(SearchWithHl7OrgDstu2BundleTest.class);

  @RegisterExtension
  public static RestfulServerExtension ourServer = new RestfulServerExtension(ourCtx)
      .registerProvider(new DummyPatientResourceProvider())
      .withPagingProvider(new FifoMemoryPagingProvider(100))
      .setDefaultResponseEncoding(EncodingEnum.JSON)
      .setDefaultPrettyPrint(false);

  @RegisterExtension
  public static HttpClientExtension ourClient = new HttpClientExtension();

  @Test
	public void testSearch() throws Exception {
		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient?_format=xml&_pretty=true");
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		assertEquals(200, status.getStatusLine().getStatusCode());

		responseContent = responseContent.replace("_pretty=true&amp;_format=xml", "_format=xml&amp;_pretty=true");
		
		ourLog.info(responseContent);

		//@formatter:off
		assertThat(responseContent).containsSubsequence("<Bundle xmlns=\"http://hl7.org/fhir\">",
				"<type value=\"searchset\"/>", 
				"<link>" ,
				"<relation value=\"self\"/>", 
				"<url value=\"" + ourServer.getBaseUrl() + "/Patient?_format=xml&amp;_pretty=true\"/>",
				"</link>" ,
				"<entry>" , 
				//"<fullUrl value=\ourServer.getBaseUrl() + "/Patient/123\"/>" , 
				"<resource>" , 
				"<Patient xmlns=\"http://hl7.org/fhir\">");
		// @formatter:off
	}


	@AfterAll
	public static void afterClass() throws Exception {
		TestUtil.randomizeLocaleAndTimezone();
	}

	/**
	 * Created by dsotnikov on 2/25/2014.
	 */
	public static class DummyPatientResourceProvider implements IResourceProvider {

		@Search
		public Bundle search() {
			Bundle retVal = new Bundle();
			
			Patient p1 = new Patient();
			p1.setId("Patient/123/_history/456");
			p1.addIdentifier().setValue("p1ReadValue");
			
			retVal.addEntry().setResource(p1);
			
			return retVal;
		}

		@Override
		public Class<Patient> getResourceType() {
			return Patient.class;
		}

	}

}
