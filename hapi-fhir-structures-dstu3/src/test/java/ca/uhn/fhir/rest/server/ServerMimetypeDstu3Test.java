package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.MyPatientWithExtensions;
import ca.uhn.fhir.test.utilities.HttpTestResponse;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.TestUtil;
import org.hl7.fhir.dstu3.model.CapabilityStatement;
import org.hl7.fhir.dstu3.model.CodeType;
import org.hl7.fhir.dstu3.model.DateType;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.OperationOutcome;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ServerMimetypeDstu3Test {

	private static final FhirContext ourCtx = FhirContext.forDstu3Cached();
	@RegisterExtension
	private RestfulServerExtension ourServer  = new RestfulServerExtension(ourCtx)
		 .registerProvider(new PatientProvider())
		 .withPagingProvider(new FifoMemoryPagingProvider(100))
		 .setDefaultResponseEncoding(EncodingEnum.XML)
		 .setDefaultPrettyPrint(false);

	@Test
	public void testConformanceMetadataUsesNewMimetypes() {
		CapabilityStatement conf = ourServer.fhirRequest("/metadata")
				.get()
				.getBodyAsResource(CapabilityStatement.class);
		List<String> strings = toStrings(conf.getFormat());
		assertThat(strings).contains(Constants.CT_FHIR_XML_NEW, Constants.CT_FHIR_JSON_NEW, Constants.FORMAT_XML, Constants.FORMAT_JSON);
	}
	
	
	
	private List<String> toStrings(List<CodeType> theFormat) {
		ArrayList<String> retVal = new ArrayList<String>();
		for (CodeType next : theFormat) {
			retVal.add(next.asStringValue());
		}
		return retVal;
	}



	@Test
	public void testCreateWithXmlLegacyNoAcceptHeader() {
		Patient p = new Patient();
		p.addName().setFamily("FAMILY");
		String enc = ourCtx.newXmlParser().encodeResourceToString(p);
		String expectedResponseContent = "<Patient xmlns=\"http://hl7.org/fhir\"><id value=\"1\"/><meta><versionId value=\"1\"/></meta><name><family value=\"FAMILY\"/></name></Patient>";
		
		ourServer.fhirRequest("/Patient")
				.post(enc, Constants.CT_FHIR_XML + "; charset=utf-8")
				.assertStatus(201)
				.assertContentTypeEquals(Constants.CT_FHIR_XML)
				.assertBodyEquals(expectedResponseContent);
	}

	@Test
	public void testHttpTraceNotEnabled() {
		HttpTestResponse status = ourServer.fhirRequest("/Patient").method("TRACE");
		status.assertStatus(400);
	}

	@Test
	public void testHttpTrackNotEnabled() {
		HttpTestResponse status = ourServer.fhirRequest("/Patient").method("TRACK");
		status.assertStatus(400);
	}

	@Test
	public void testCreateWithXmlNewNoAcceptHeaderReturnsOperationOutcome() {
		Patient p = new Patient();
		p.addName().setFamily("FAMILY");
		String enc = ourCtx.newXmlParser().encodeResourceToString(p);
		String expectedResponseContent = "<OperationOutcome xmlns=\"http://hl7.org/fhir\"><issue><diagnostics value=\"FAMILY\"/></issue></OperationOutcome>";
		
		ourServer.fhirRequest("/Patient")
				.withHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RETURN + "=" + Constants.HEADER_PREFER_RETURN_OPERATION_OUTCOME)
				.post(enc, Constants.CT_FHIR_XML_NEW + "; charset=utf-8")
				.assertStatus(201)
				.assertContentTypeEquals(Constants.CT_FHIR_XML_NEW)
				.assertBodyEquals(expectedResponseContent);
	}

	@Test
	public void testCreateWithXmlNewWithAcceptHeader() {
		Patient p = new Patient();
		p.addName().setFamily("FAMILY");
		String enc = ourCtx.newXmlParser().encodeResourceToString(p);
		String expectedResponseContent = "<Patient xmlns=\"http://hl7.org/fhir\"><id value=\"1\"/><meta><versionId value=\"1\"/></meta><name><family value=\"FAMILY\"/></name></Patient>";
		
		ourServer.fhirRequest("/Patient")
				.withHeader(Constants.HEADER_ACCEPT, Constants.CT_FHIR_XML_NEW)
				.post(enc, Constants.CT_FHIR_XML + "; charset=utf-8")
				.assertStatus(201)
				.assertContentTypeEquals(Constants.CT_FHIR_XML_NEW)
				.assertBodyEquals(expectedResponseContent);
	}

	@Test
	public void testCreateWithJsonLegacyNoAcceptHeader() {
		Patient p = new Patient();
		p.addName().setFamily("FAMILY");
		String enc = ourCtx.newJsonParser().encodeResourceToString(p);
		String expectedResponseContent = "{\"resourceType\":\"Patient\",\"id\":\"1\",\"meta\":{\"versionId\":\"1\"},\"name\":[{\"family\":\"FAMILY\"}]}";
		
		ourServer.fhirRequest("/Patient")
				.post(enc, Constants.CT_FHIR_JSON + "; charset=utf-8")
				.assertStatus(201)
				.assertContentTypeEquals(Constants.CT_FHIR_JSON)
				.assertBodyEquals(expectedResponseContent);
	}

	@Test
	public void testCreateWithJsonNewNoAcceptHeader() {
		Patient p = new Patient();
		p.addName().setFamily("FAMILY");
		String enc = ourCtx.newJsonParser().encodeResourceToString(p);
		String expectedResponseContent = "{\"resourceType\":\"Patient\",\"id\":\"1\",\"meta\":{\"versionId\":\"1\"},\"name\":[{\"family\":\"FAMILY\"}]}";
		
		ourServer.fhirRequest("/Patient")
				.post(enc, Constants.CT_FHIR_JSON_NEW + "; charset=utf-8")
				.assertStatus(201)
				.assertContentTypeEquals(Constants.CT_FHIR_JSON_NEW)
				.assertBodyEquals(expectedResponseContent);
	}

	@Test
	public void testCreateWithJsonNewWithAcceptHeaderReturnsOperationOutcome() {
		Patient p = new Patient();
		p.addName().setFamily("FAMILY");
		String enc = ourCtx.newJsonParser().encodeResourceToString(p);
		String expectedResponseContent = "{\"resourceType\":\"OperationOutcome\",\"issue\":[{\"diagnostics\":\"FAMILY\"}]}";
		
		ourServer.fhirRequest("/Patient")
				.withHeader(Constants.HEADER_ACCEPT, Constants.CT_FHIR_JSON_NEW)
				.withHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RETURN + "=" + Constants.HEADER_PREFER_RETURN_OPERATION_OUTCOME)
				.post(enc, Constants.CT_FHIR_JSON + "; charset=utf-8")
				.assertStatus(201)
				.assertContentTypeEquals(Constants.CT_FHIR_JSON_NEW)
				.assertBodyEquals(expectedResponseContent);
	}
	
	@Test
	public void testSearchWithFormatXmlSimple() {

		ourServer.fhirRequest("/Patient?_format=xml")
				.get()
				.assertStatus(200)
				.assertBodyContains("<Patient xmlns=\"http://hl7.org/fhir\">")
				.assertBodyDoesNotContain("http://hl7.org/fhir/")
				.assertContentTypeEquals(Constants.CT_FHIR_XML_NEW);
	}

	@Test
	public void testSearchWithFormatXmlLegacy() {

		ourServer.fhirRequest("/Patient?_format=" + Constants.CT_FHIR_XML)
				.get()
				.assertStatus(200)
				.assertBodyContains("<Patient xmlns=\"http://hl7.org/fhir\">")
				.assertBodyDoesNotContain("http://hl7.org/fhir/")
				.assertContentTypeEquals(Constants.CT_FHIR_XML);
	}

	@Test
	public void testSearchWithFormatXmlNew() {

		ourServer.fhirRequest("/Patient?_format=" + Constants.CT_FHIR_XML_NEW)
				.get()
				.assertStatus(200)
				.assertBodyContains("<Patient xmlns=\"http://hl7.org/fhir\">")
				.assertBodyDoesNotContain("http://hl7.org/fhir/")
				.assertContentTypeEquals(Constants.CT_FHIR_XML_NEW);
	}


	
	@Test
	public void testSearchWithFormatJsonSimple() {

		ourServer.fhirRequest("/Patient?_format=json")
				.get()
				.assertStatus(200)
				.assertBodyContains("\"resourceType\"")
				.assertContentTypeEquals(Constants.CT_FHIR_JSON_NEW);
	}

	@Test
	public void testSearchWithFormatJsonLegacy() {

		ourServer.fhirRequest("/Patient?_format=" + Constants.CT_FHIR_JSON)
				.get()
				.assertStatus(200)
				.assertBodyContains("\"resourceType\"")
				.assertContentTypeEquals(Constants.CT_FHIR_JSON);
	}

	@Test
	public void testSearchWithFormatJsonNew() {

		ourServer.fhirRequest("/Patient?_format=" + Constants.CT_FHIR_JSON_NEW)
				.get()
				.assertStatus(200)
				.assertBodyContains("\"resourceType\"")
				.assertContentTypeEquals(Constants.CT_FHIR_JSON_NEW);
	}

	@AfterAll
	public static void afterClassClearContext() throws Exception {
		TestUtil.randomizeLocaleAndTimezone();
	}
	

	public static class PatientProvider implements IResourceProvider {

		@Create()
		public MethodOutcome create(@ResourceParam Patient theIdParam) {
			OperationOutcome oo = new OperationOutcome();
			oo.addIssue().setDiagnostics(theIdParam.getNameFirstRep().getFamily());
			theIdParam.setId("1");
			theIdParam.getMeta().setVersionId("1");
			return new MethodOutcome(new IdType("Patient", "1"), true).setOperationOutcome(oo).setResource(theIdParam);
		}

		@Override
		public Class<Patient> getResourceType() {
			return Patient.class;
		}

		@Read()
		public MyPatientWithExtensions read(@IdParam IdType theIdParam) {
			MyPatientWithExtensions p0 = new MyPatientWithExtensions();
			p0.setId(theIdParam);
			p0.setDateExt(new DateType("2011-01-01"));
			return p0;
		}

		@Search
		public List<IBaseResource> search() {
			ArrayList<IBaseResource> retVal = new ArrayList<IBaseResource>();

			MyPatientWithExtensions p0 = new MyPatientWithExtensions();
			p0.setId(new IdType("Patient/0"));
			p0.setDateExt(new DateType("2011-01-01"));
			retVal.add(p0);

			Patient p1 = new Patient();
			p1.setId(new IdType("Patient/1"));
			p1.addName().setFamily("The Family");
			retVal.add(p1);

			return retVal;
		}

	}

}
