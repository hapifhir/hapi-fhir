package ca.uhn.fhir.rest.server.interceptor;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.server.FifoMemoryPagingProvider;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.test.utilities.HttpTestResponse;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.TestUtil;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.Patient;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class BanUnsupprtedHttpMethodsInterceptorDstu3Test {

	private static final FhirContext ourCtx = FhirContext.forDstu3Cached();
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(BanUnsupprtedHttpMethodsInterceptorDstu3Test.class);
	@RegisterExtension
	private RestfulServerExtension ourServer  = new RestfulServerExtension(ourCtx)
		 .setDefaultResponseEncoding(EncodingEnum.XML)
		 .registerProvider(new DummyPatientResourceProvider())
		 .registerInterceptor(new BanUnsupportedHttpMethodsInterceptor())
		 .withPagingProvider(new FifoMemoryPagingProvider(100))
		 .setDefaultPrettyPrint(false);

	@Test
	public void testHttpTraceNotEnabled() {
		HttpTestResponse status = ourServer.fhirRequest("/Patient").method("TRACE");
		status.assertStatus(405);
	}
	
	@Test	
	public void testHeadJsonWithInvalidPatient() {	
		HttpTestResponse status = ourServer.fhirRequest("/Patient/123").head();
		assertThat(status.getBodyBytes()).isEmpty();	
 		ourLog.info(status.toString());

		status.assertStatus(404);
		assertThat(status.getHeader(Constants.HEADER_POWERED_BY)).contains("HAPI");
	}
	
	@Test	
	public void testHeadJsonWithValidPatient() {	
		HttpTestResponse status = ourServer.fhirRequest("/Patient/1").head();
		assertThat(status.getBodyBytes()).isEmpty();	
 		ourLog.info(status.toString());

		status.assertStatus(200);
		assertThat(status.getHeader(Constants.HEADER_POWERED_BY)).contains("HAPI");
	}
	
	@Test
	public void testHttpTrackNotEnabled() {
		HttpTestResponse status = ourServer.fhirRequest("/Patient").method("TRACK");
		status.assertStatus(405);
	}

	@Test
	public void testHttpFooNotEnabled() {
		HttpTestResponse status = ourServer.fhirRequest("/Patient").method("FOO");
		status.assertStatus(501);
	}

	@Test
	public void testRead() {

		ourServer.fhirRequest("/Patient/1").get().assertStatus(200);
	}
	
	@AfterAll
	public static void afterClassClearContext() throws Exception {
		TestUtil.randomizeLocaleAndTimezone();
	}

	public static class DummyPatientResourceProvider implements IResourceProvider {

		private Patient createPatient1() {
			Patient patient = new Patient();
			patient.addName();
			patient.getName().get(0).setFamily("Test");
			patient.getName().get(0).addGiven("PatientOne");
			return patient;
		}

		public Map<String, Patient> getIdToPatient() {
			Map<String, Patient> idToPatient = new HashMap<String, Patient>();
			{
				Patient patient = createPatient1();
				idToPatient.put("1", patient);
			}
			return idToPatient;
		}

		/**
		 * Retrieve the resource by its identifier
		 * 
		 * @param theId
		 *           The resource identity
		 * @return The resource
		 */
		@Read()
		public Patient getResourceById(@IdParam IdType theId) {
			if (theId.getIdPart().equals("EX")) {
				throw new InvalidRequestException("FOO");
			}
			String key = theId.getIdPart();
			Patient retVal = getIdToPatient().get(key);
			return retVal;
		}

		@Override
		public Class<Patient> getResourceType() {
			return Patient.class;
		}

	}

}
