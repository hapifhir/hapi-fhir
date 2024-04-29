package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.test.utilities.HttpClientExtension;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.TestUtil;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.hl7.fhir.dstu2016may.model.HumanName;
import org.hl7.fhir.dstu2016may.model.Patient;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SearchWithGenericListDstu2_1Test {

	private static final FhirContext ourCtx = FhirContext.forDstu2_1();
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(SearchWithGenericListDstu2_1Test.class);
	private static String ourLastMethod;

	@RegisterExtension
	public static final RestfulServerExtension ourServer = new RestfulServerExtension(ourCtx)
		.registerProvider(new DummyPatientResourceProvider())
		.setDefaultResponseEncoding(EncodingEnum.XML);

	@RegisterExtension
	public static final HttpClientExtension ourClient = new HttpClientExtension();

	@BeforeEach
	public void before() {
		ourLastMethod = null;
	}

	/**
	 * See #291
	 */
	@Test
	public void testSearch() throws Exception {
		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient?identifier=foo&_pretty=true");
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		ourLog.info(responseContent);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertEquals("searchByIdentifier", ourLastMethod);
		assertThat(responseContent, containsString("<family value=\"FAMILY\""));
		assertThat(responseContent, containsString("<fullUrl value=\"" + ourServer.getBaseUrl() + "/Patient/1\"/>"));
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
		@SuppressWarnings("rawtypes")
		@Search()
		public List searchByIdentifier(
				@RequiredParam(name=Patient.SP_IDENTIFIER) TokenParam theIdentifier) {
			ourLastMethod = "searchByIdentifier";
			ArrayList<Patient> retVal = new ArrayList<>();
			retVal.add((Patient) new Patient().addName(new HumanName().addFamily("FAMILY")).setId("1"));
			return retVal;
		}
		//@formatter:on


	}

}
