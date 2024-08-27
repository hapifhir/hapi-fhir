package ca.uhn.fhir.rest.client.apache;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.FifoMemoryPagingProvider;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.interceptor.VerboseLoggingInterceptor;
import ca.uhn.fhir.test.utilities.HttpClientExtension;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.TestUtil;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ApacheClientIntegrationTest {

	private static final FhirContext ourCtx = FhirContext.forDstu2Cached();
	private static String ourLastMethod;
	private static StringParam ourLastName;

	@RegisterExtension
	public static final RestfulServerExtension ourServer  = new RestfulServerExtension(ourCtx)
		.setDefaultResponseEncoding(EncodingEnum.XML)
		.registerProvider(new DummyPatientResourceProvider())
		.registerInterceptor(new VerboseLoggingInterceptor())
		.withPagingProvider(new FifoMemoryPagingProvider(100))
		.setDefaultPrettyPrint(false);

	@RegisterExtension
	public static final HttpClientExtension ourClient = new HttpClientExtension();

	@BeforeEach
	public void before() {
		ourLastMethod = null;
		ourLastName = null;
	}


	@Test
	public void testSearchWithParam() {
		Bundle response = ourServer.getFhirClient().search().forResource(Patient.class).where(Patient.NAME.matches().value("FOO")).returnBundle(Bundle.class).execute();
		assertEquals("search", ourLastMethod);
		assertEquals("FOO", ourLastName.getValue());
		assertThat(response.getEntry()).hasSize(1);
		assertEquals("123", response.getEntry().get(0).getResource().getIdElement().getIdPart());
	}
	
	@AfterAll
	public static void afterClassClearContext() throws Exception {
		TestUtil.randomizeLocaleAndTimezone();
	}

	public static class DummyPatientResourceProvider implements IResourceProvider {
		
		//@formatter:off
		@Search()
		public List<IBaseResource> search(@OptionalParam(name=Patient.SP_NAME) StringParam theName) {
			ourLastMethod = "search";
			ourLastName = theName;
			
			List<IBaseResource> retVal = new ArrayList<>();
			Patient patient = new Patient();
			patient.setId("123");
			patient.addName().addGiven("GIVEN");
			retVal.add(patient);
			return retVal;
		}
		//@formatter:on

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return Patient.class;
		}

	}

}
