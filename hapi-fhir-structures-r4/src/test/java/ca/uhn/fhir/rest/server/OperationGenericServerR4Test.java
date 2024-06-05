package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.test.utilities.HttpClientExtension;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.TestUtil;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.StringType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class OperationGenericServerR4Test {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(OperationGenericServerR4Test.class);
	private static final FhirContext ourCtx = FhirContext.forR4Cached();
	private static IdType ourLastId;
	private static String ourLastMethod;
	private static StringType ourLastParam1;
	private static Patient ourLastParam2;
	private static Parameters ourLastResourceParam;

	@RegisterExtension
	public RestfulServerExtension ourServer = new RestfulServerExtension(ourCtx)
		 .registerProvider(new PatientProvider())
		 .registerProvider(new PlainProvider())
		 .withPagingProvider(new FifoMemoryPagingProvider(10).setDefaultPageSize(2))
		 .setDefaultResponseEncoding(EncodingEnum.XML);

	@RegisterExtension
	private HttpClientExtension ourClient = new HttpClientExtension();

	@BeforeEach
	public void before() {
		ourLastParam1 = null;
		ourLastParam2 = null;
		ourLastId = null;
		ourLastMethod = "";
		ourLastResourceParam = null;
	}


	@Test
	public void testOperationOnInstance() throws Exception {
		Parameters p = new Parameters();
		p.addParameter().setName("PARAM1").setValue(new StringType("PARAM1val"));
		p.addParameter().setName("PARAM2").setResource(new Patient().setActive(true));
		String inParamsStr = ourCtx.newXmlParser().encodeResourceToString(p);

		HttpPost httpPost = new HttpPost(ourServer.getBaseUrl() + "/Patient/123/$OP_INSTANCE");
		httpPost.setEntity(new StringEntity(inParamsStr, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));
		CloseableHttpResponse status = ourClient.execute(httpPost);
		try {
			assertEquals(200, status.getStatusLine().getStatusCode());
			String response = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
			status.getEntity().getContent().close();

			assertEquals("PARAM1val", ourLastParam1.getValue());
			assertEquals(true, ourLastParam2.getActive());
			assertEquals("123", ourLastId.getIdPart());
			assertEquals("$OP_INSTANCE", ourLastMethod);
			assertEquals("PARAM1", ourLastResourceParam.getParameterFirstRep().getName());

			Parameters resp = ourCtx.newXmlParser().parseResource(Parameters.class, response);
			assertEquals("RET1", resp.getParameter().get(0).getName());
		} finally {
			status.getEntity().getContent().close();
		}

	}


	@Test
	public void testOperationOnServer() throws Exception {
		Parameters p = new Parameters();
		p.addParameter().setName("PARAM1").setValue(new StringType("PARAM1val"));
		p.addParameter().setName("PARAM2").setResource(new Patient().setActive(true));
		String inParamsStr = ourCtx.newXmlParser().encodeResourceToString(p);

		HttpPost httpPost = new HttpPost(ourServer.getBaseUrl() + "/$OP_SERVER");
		httpPost.setEntity(new StringEntity(inParamsStr, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));
		CloseableHttpResponse status = ourClient.execute(httpPost);
		try {
			assertEquals(200, status.getStatusLine().getStatusCode());
			String response = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);

			assertEquals("PARAM1", ourLastResourceParam.getParameterFirstRep().getName());
			assertEquals("PARAM1val", ourLastParam1.getValue());
			assertEquals(true, ourLastParam2.getActive());
			assertEquals("$OP_SERVER", ourLastMethod);

			Parameters resp = ourCtx.newXmlParser().parseResource(Parameters.class, response);
			assertEquals("RET1", resp.getParameter().get(0).getName());
		} finally {
			status.getEntity().getContent().close();
		}
	}


	@Test
	public void testOperationOnType() throws Exception {
		Parameters p = new Parameters();
		p.addParameter().setName("PARAM1").setValue(new StringType("PARAM1val"));
		p.addParameter().setName("PARAM2").setResource(new Patient().setActive(true));
		String inParamsStr = ourCtx.newXmlParser().encodeResourceToString(p);

		HttpPost httpPost = new HttpPost(ourServer.getBaseUrl() + "/Patient/$OP_TYPE");
		httpPost.setEntity(new StringEntity(inParamsStr, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));
		CloseableHttpResponse status = ourClient.execute(httpPost);
		try {
			String response = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(response);
			assertEquals(200, status.getStatusLine().getStatusCode());
			status.getEntity().getContent().close();

			assertEquals("PARAM1", ourLastResourceParam.getParameterFirstRep().getName());
			assertEquals("PARAM1val", ourLastParam1.getValue());
			assertEquals(true, ourLastParam2.getActive());
			assertEquals("$OP_TYPE", ourLastMethod);

			Parameters resp = ourCtx.newXmlParser().parseResource(Parameters.class, response);
			assertEquals("RET1", resp.getParameter().get(0).getName());
		} finally {
			status.getEntity().getContent().close();
		}
	}


	@Test
	public void testOperationWithGetUsingParams() throws Exception {
		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient/$OP_TYPE?PARAM1=PARAM1val");
		CloseableHttpResponse status = ourClient.execute(httpGet);
		try {
			String response = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(response);
			assertEquals(200, status.getStatusLine().getStatusCode());
			status.getEntity().getContent().close();

			assertNull(ourLastResourceParam);
			assertEquals("PARAM1val", ourLastParam1.getValue());

			assertNull(ourLastParam2);
			assertEquals("$OP_TYPE", ourLastMethod);

			Parameters resp = ourCtx.newXmlParser().parseResource(Parameters.class, response);
			assertEquals("RET1", resp.getParameter().get(0).getName());
		} finally {
			status.getEntity().getContent().close();
		}
	}


	@Test
	public void testSearchGetsClassifiedAppropriately() throws Exception {
		HttpGet httpPost = new HttpGet(ourServer.getBaseUrl() + "/Patient");
		CloseableHttpResponse status = ourClient.execute(httpPost);
		try {
			assertEquals(200, status.getStatusLine().getStatusCode());
			status.getEntity().getContent().close();
		} finally {
			status.getEntity().getContent().close();
		}

		assertEquals("Patient/search", ourLastMethod);
	}


	@SuppressWarnings("unused")
	public static class PatientProvider implements IResourceProvider {

		@Override
		public Class<Patient> getResourceType() {
			return Patient.class;
		}

		@Operation(name = Operation.NAME_MATCH_ALL)
		public Parameters opInstance(
			@ResourceParam() IBaseResource theResourceParam,
			@IdParam IdType theId,
			@OperationParam(name = "PARAM1") StringType theParam1,
			@OperationParam(name = "PARAM2") Patient theParam2
		) {

			ourLastMethod = "$OP_INSTANCE";
			ourLastId = theId;
			ourLastParam1 = theParam1;
			ourLastParam2 = theParam2;
			ourLastResourceParam = (Parameters) theResourceParam;

			Parameters retVal = new Parameters();
			retVal.addParameter().setName("RET1").setValue(new StringType("RETVAL1"));
			return retVal;
		}

		@SuppressWarnings("unused")
		@Operation(name = Operation.NAME_MATCH_ALL, idempotent = true)
		public Parameters opType(
			@ResourceParam() IBaseResource theResourceParam,
			@OperationParam(name = "PARAM1") StringType theParam1,
			@OperationParam(name = "PARAM2") Patient theParam2,
			@OperationParam(name = "PARAM3", min = 2, max = 5) List<StringType> theParam3,
			@OperationParam(name = "PARAM4", min = 1) List<StringType> theParam4
		) {

			ourLastMethod = "$OP_TYPE";
			ourLastParam1 = theParam1;
			ourLastParam2 = theParam2;
			ourLastResourceParam = (Parameters) theResourceParam;

			Parameters retVal = new Parameters();
			retVal.addParameter().setName("RET1").setValue(new StringType("RETVAL1"));
			return retVal;
		}

		@Search
		public List<IBaseResource> search() {
			ourLastMethod = "Patient/search";
			return new ArrayList<>();
		}


	}

	@SuppressWarnings("unused")
	public static class PlainProvider {

		@Operation(name = Operation.NAME_MATCH_ALL)
		public Parameters opServer(
			@ResourceParam() IBaseResource theResourceParam,
			@OperationParam(name = "PARAM1") StringType theParam1,
			@OperationParam(name = "PARAM2") Patient theParam2
		) {

			ourLastMethod = "$OP_SERVER";
			ourLastParam1 = theParam1;
			ourLastParam2 = theParam2;
			ourLastResourceParam = (Parameters) theResourceParam;

			Parameters retVal = new Parameters();
			retVal.addParameter().setName("RET1").setValue(new StringType("RETVAL1"));
			return retVal;
		}


		@Search
		public List<IBaseResource> search() {
			ourLastMethod = "/search";
			return new ArrayList<>();
		}


	}

	@AfterAll
	public static void afterClassClearContext() throws Exception {
		TestUtil.randomizeLocaleAndTimezone();
	}


}
