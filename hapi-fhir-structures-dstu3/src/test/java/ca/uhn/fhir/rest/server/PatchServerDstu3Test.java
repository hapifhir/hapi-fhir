package ca.uhn.fhir.rest.server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.rest.annotation.ConditionalUrlParam;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Patch;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.PatchTypeEnum;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.TestUtil;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.OperationOutcome;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

public class PatchServerDstu3Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(PatchServerDstu3Test.class);
	private static final FhirContext ourCtx = FhirContext.forDstu3Cached();
	private static String ourLastMethod;
	private static PatchTypeEnum ourLastPatchType;
	private static String ourLastBody;
	private static IdType ourLastId;
	private static String ourLastConditional;

	@RegisterExtension
	private RestfulServerExtension ourServer  = new RestfulServerExtension(ourCtx)
		 .setDefaultResponseEncoding(EncodingEnum.XML)
		 .registerProvider(new DummyPatientResourceProvider())
		 .withPagingProvider(new FifoMemoryPagingProvider(100))
		 .setDefaultPrettyPrint(false);

	@BeforeEach
	public void before() {
		ourLastMethod = null;
		ourLastBody = null;
		ourLastId = null;
		ourLastConditional = null;
	}

	@Test
	public void testPatchValidJson() throws Exception {
		String requestContents = "[ { \"op\": \"add\", \"path\": \"/a/b/c\", \"value\": [ \"foo\", \"bar\" ] } ]";
		String responseContent = ourServer.fhirRequest("/Patient/123").withHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RETURN + "=" + Constants.HEADER_PREFER_RETURN_OPERATION_OUTCOME)
			.method("PATCH", requestContents.getBytes(StandardCharsets.UTF_8), Constants.CT_JSON_PATCH).assertStatus(200).getBody();
		ourLog.info(responseContent);
		assertEquals("<OperationOutcome xmlns=\"http://hl7.org/fhir\"><text><div xmlns=\"http://www.w3.org/1999/xhtml\">OK</div></text></OperationOutcome>", responseContent);

		assertEquals("patientPatch", ourLastMethod);
		assertEquals("Patient/123", ourLastId.getValue());
		assertEquals(requestContents, ourLastBody);
		assertEquals(PatchTypeEnum.JSON_PATCH, ourLastPatchType);
	}

	@Test
	public void testPatchUsingConditional() throws Exception {
		String requestContents = "[ { \"op\": \"add\", \"path\": \"/a/b/c\", \"value\": [ \"foo\", \"bar\" ] } ]";
		String responseContent = ourServer.fhirRequest("/Patient?_id=123").withHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RETURN + "=" + Constants.HEADER_PREFER_RETURN_OPERATION_OUTCOME)
			.method("PATCH", requestContents.getBytes(StandardCharsets.UTF_8), Constants.CT_JSON_PATCH).assertStatus(200).getBody();
		ourLog.info(responseContent);
		assertEquals("<OperationOutcome xmlns=\"http://hl7.org/fhir\"><text><div xmlns=\"http://www.w3.org/1999/xhtml\">OK</div></text></OperationOutcome>", responseContent);

		assertEquals("patientPatch", ourLastMethod);
		assertEquals("Patient?_id=123", ourLastConditional);
		assertNull(ourLastId);
		assertEquals(requestContents, ourLastBody);
		assertEquals(PatchTypeEnum.JSON_PATCH, ourLastPatchType);
	}

	@Test
	public void testPatchValidXml() throws Exception {
		String requestContents = "<root/>";
		String responseContent = ourServer.fhirRequest("/Patient/123").withHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RETURN + "=" + Constants.HEADER_PREFER_RETURN_OPERATION_OUTCOME)
			.method("PATCH", requestContents.getBytes(StandardCharsets.UTF_8), Constants.CT_XML_PATCH).assertStatus(200).getBody();
		ourLog.info(responseContent);
		assertEquals("<OperationOutcome xmlns=\"http://hl7.org/fhir\"><text><div xmlns=\"http://www.w3.org/1999/xhtml\">OK</div></text></OperationOutcome>", responseContent);

		assertEquals("patientPatch", ourLastMethod);
		assertEquals("Patient/123", ourLastId.getValue());
		assertEquals(requestContents, ourLastBody);
		assertEquals(PatchTypeEnum.XML_PATCH, ourLastPatchType);
	}

	@Test
	public void testPatchValidJsonWithCharset() throws Exception {
		String requestContents = "[ { \"op\": \"add\", \"path\": \"/a/b/c\", \"value\": [ \"foo\", \"bar\" ] } ]";
		String responseContent = ourServer.fhirRequest("/Patient/123").patch(requestContents, Constants.CT_JSON_PATCH + Constants.CHARSET_UTF8_CTSUFFIX).assertStatus(200).getBody();
		ourLog.info(responseContent);

		assertEquals("patientPatch", ourLastMethod);
		assertEquals("Patient/123", ourLastId.getValue());
		assertEquals(requestContents, ourLastBody);
	}

	@Test
	public void testPatchInvalidMimeType() throws Exception {
		String requestContents = "[ { \"op\": \"add\", \"path\": \"/a/b/c\", \"value\": [ \"foo\", \"bar\" ] } ]";
		String responseContent = ourServer.fhirRequest("/Patient/123").patch(requestContents, "text/plain; charset=UTF-8").assertStatus(400).getBody();
		ourLog.info(responseContent);
		assertEquals("<OperationOutcome xmlns=\"http://hl7.org/fhir\"><issue><severity value=\"error\"/><code value=\"processing\"/><diagnostics value=\"" + Msg.code(1965) + "Invalid Content-Type for PATCH operation: text/plain\"/></issue></OperationOutcome>", responseContent);

	}

	public static class DummyPatientResourceProvider implements IResourceProvider {

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return Patient.class;
		}

		@Patch
		public OperationOutcome patientPatch(
			@IdParam IdType theId,
			PatchTypeEnum thePatchType,
			@ResourceParam String theBody,
			@ConditionalUrlParam String theConditional
		) {
			ourLastMethod = "patientPatch";
			ourLastBody = theBody;
			ourLastId = theId;
			ourLastPatchType = thePatchType;
			ourLastConditional = theConditional;
			OperationOutcome retVal = new OperationOutcome();
			retVal.getText().setDivAsString("<div>OK</div>");
			return retVal;
		}

	}

	@AfterAll
	public static void afterClassClearContext() throws Exception {
		TestUtil.randomizeLocaleAndTimezone();
	}

}
