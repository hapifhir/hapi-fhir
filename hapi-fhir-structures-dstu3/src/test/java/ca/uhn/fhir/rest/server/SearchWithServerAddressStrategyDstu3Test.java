package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.TestUtil;
import org.hl7.fhir.dstu3.model.HumanName;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class SearchWithServerAddressStrategyDstu3Test {

	private static final FhirContext ourCtx = FhirContext.forDstu3Cached();
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(SearchWithServerAddressStrategyDstu3Test.class);

	@RegisterExtension
	private RestfulServerExtension ourServer  = new RestfulServerExtension(ourCtx)
		 .registerProvider(new DummyPatientResourceProvider())
		 .setDefaultResponseEncoding(EncodingEnum.XML)
		 .withPagingProvider(new FifoMemoryPagingProvider(100))
		 .setDefaultPrettyPrint(false);

	@Test
	public void testIncomingRequestAddressStrategy() throws Exception {
		ourServer.setServerAddressStrategy(new IncomingRequestAddressStrategy());
		
		String responseContent = ourServer.fhirRequest("/Patient").get().assertStatus(200).getBody();
		ourLog.info(responseContent);
		assertThat(responseContent).contains("<given value=\"FAMILY\"");
		assertThat(responseContent).contains("<fullUrl value=\"" + ourServer.getBaseUrl() + "/Patient/1\"/>");
	}

	@Test
	public void testApacheProxyAddressStrategy() throws Exception {
		
		ourServer.setServerAddressStrategy(ApacheProxyAddressStrategy.forHttp());
		String responseContent = ourServer.fhirRequest("/Patient").get().assertStatus(200).getBody();
		ourLog.info(responseContent);
		assertThat(responseContent).contains("<given value=\"FAMILY\"");
		assertThat(responseContent).contains("<fullUrl value=\"" + ourServer.getBaseUrl() + "/Patient/1\"/>");
		
		ourServer.setServerAddressStrategy(new ApacheProxyAddressStrategy(false));
		responseContent = ourServer.fhirRequest("/Patient")
			.withHeader(Constants.HEADER_X_FORWARDED_HOST, "foo.com")
			.get()
			.assertStatus(200)
			.getBody();
		ourLog.info(responseContent);
		assertThat(responseContent).contains("<given value=\"FAMILY\"");
		assertThat(responseContent).contains("<fullUrl value=\"http://foo.com/Patient/1\"/>");

		ourServer.setServerAddressStrategy(ApacheProxyAddressStrategy.forHttps());
		responseContent = ourServer.fhirRequest("/Patient")
			.withHeader(Constants.HEADER_X_FORWARDED_HOST, "foo.com")
			.get()
			.assertStatus(200)
			.getBody();
		ourLog.info(responseContent);
		assertThat(responseContent).contains("<given value=\"FAMILY\"");
		assertThat(responseContent).contains("<fullUrl value=\"https://foo.com/Patient/1\"/>");

		ourServer.setServerAddressStrategy(new ApacheProxyAddressStrategy(false));
		responseContent = ourServer.fhirRequest("/Patient")
			.withHeader(Constants.HEADER_X_FORWARDED_HOST, "foo.com")
			.withHeader(Constants.HEADER_X_FORWARDED_PROTO, "https")
			.get()
			.assertStatus(200)
			.getBody();
		ourLog.info(responseContent);
		assertThat(responseContent).contains("<given value=\"FAMILY\"");
		assertThat(responseContent).contains("<fullUrl value=\"https://foo.com/Patient/1\"/>");

	}

	@Test
	public void testHardcodedAddressStrategy() throws Exception {
		ourServer.setServerAddressStrategy(new HardcodedServerAddressStrategy("http://example.com/fhir/base"));
		
		String responseContent = ourServer.fhirRequest("/Patient").get().assertStatus(200).getBody();
		ourLog.info(responseContent);
		assertThat(responseContent).contains("<given value=\"FAMILY\"");
		assertThat(responseContent).contains("<fullUrl value=\"http://example.com/fhir/base/Patient/1\"/>");
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
		public List<Patient> searchByIdentifier() {
			ArrayList<Patient> retVal = new ArrayList<>();
			retVal.add((Patient) new Patient().addName(new HumanName().addGiven("FAMILY")).setId("1"));
			retVal.add((Patient) new Patient().addName(new HumanName().addGiven("FAMILY")).setId("2"));
			return retVal;
		}
		//@formatter:on


	}

}
