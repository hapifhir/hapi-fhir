package ca.uhn.hapi.converters.server;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.TestUtil;
import ca.uhn.fhir.util.UrlUtil;
import org.hl7.fhir.dstu3.model.HumanName;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.ArrayList;
import java.util.List;


public class VersionedApiConverterInterceptorR4Test {

	private static final FhirContext ourCtx = FhirContext.forDstu3Cached();

	@RegisterExtension
	public static final RestfulServerExtension ourServer = new RestfulServerExtension(ourCtx)
		.registerProvider(new DummyPatientResourceProvider())
		.registerInterceptor(new VersionedApiConverterInterceptor())
		.setDefaultResponseEncoding(EncodingEnum.JSON);

	@Test
	public void testSearchNormal() {
		ourServer.fhirRequest("/Patient").get().assertBodyContains("\"family\": \"FAMILY\"");
	}

	@Test
	public void testSearchConvertToR2() {
		ourServer.fhirRequest("/Patient")
			.withHeader(Constants.HEADER_ACCEPT, "application/fhir+json; fhirVersion=1.0")
			.get()
			.assertBodyContains("\"family\": [");
	}

	@Test
	public void testSearchConvertToR2ByFormatParam() {
		String path = "/Patient?_format=" + UrlUtil.escapeUrlParam("application/fhir+json; fhirVersion=1.0");
		ourServer.fhirRequest(path).get().assertBodyContains("\"family\": [");
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

		@SuppressWarnings("rawtypes")
		@Search()
		public List search() {
			ArrayList<Patient> retVal = new ArrayList<>();

			Patient patient = new Patient();
			patient.getIdElement().setValue("Patient/A");
			patient.addName(new HumanName().setFamily("FAMILY"));
			retVal.add(patient);

			return retVal;
		}

	}

}
