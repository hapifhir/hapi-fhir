package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.test.utilities.HttpTestResponse;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.TestUtil;
import org.hl7.fhir.dstu2.model.Bundle;
import org.hl7.fhir.dstu2.model.IdType;
import org.hl7.fhir.dstu2.model.IntegerType;
import org.hl7.fhir.dstu2.model.Parameters;
import org.hl7.fhir.dstu2.model.Patient;
import org.hl7.fhir.dstu2.model.StringType;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class OperationServerHl7OrgTest {
  private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(OperationServerHl7OrgTest.class);
  private static final FhirContext ourCtx = FhirContext.forDstu2Hl7OrgCached();
  private static StringType ourLastParam1;
  private static Patient ourLastParam2;
  private static IdType ourLastId;
  private static String ourLastMethod;
  private static List<StringType> ourLastParam3;

  @RegisterExtension
  public static RestfulServerExtension ourServer = new RestfulServerExtension(ourCtx)
      .registerProvider(new PatientProvider())
      .registerProvider(new PlainProvider())
      .withPagingProvider(new FifoMemoryPagingProvider(10).setDefaultPageSize(2))
      .setDefaultResponseEncoding(EncodingEnum.XML)
      .setDefaultPrettyPrint(false);

  @BeforeEach
  public void before() {
    ourLastParam1 = null;
    ourLastParam2 = null;
    ourLastParam3 = null;
    ourLastId = null;
    ourLastMethod = "";
  }

  @Test
  public void testOperationOnType() throws Exception {
    Parameters p = new Parameters();
    p.addParameter().setName("PARAM1").setValue(new StringType("PARAM1val"));
    p.addParameter().setName("PARAM2").setResource(new Patient().setActive(true));
    String inParamsStr = ourCtx.newXmlParser().encodeResourceToString(p);

    String response = ourServer.fhirRequest("/Patient/$OP_TYPE").post(inParamsStr, Constants.CT_FHIR_XML).assertStatus(200).getBody();

		assertEquals("PARAM1val", ourLastParam1.getValue());
		assertEquals(true, ourLastParam2.getActive());
		assertEquals("$OP_TYPE", ourLastMethod);

    Parameters resp = ourCtx.newXmlParser().parseResource(Parameters.class, response);
		assertEquals("RET1", resp.getParameter().get(0).getName());
  }

  @Test
  public void testOperationWithGetUsingParams() throws Exception {
    String response = ourServer.fhirRequest("/Patient/$OP_TYPE?PARAM1=PARAM1val").get().assertStatus(200).getBody();

		assertEquals("PARAM1val", ourLastParam1.getValue());
		assertNull(ourLastParam2);
		assertEquals("$OP_TYPE", ourLastMethod);

    Parameters resp = ourCtx.newXmlParser().parseResource(Parameters.class, response);
		assertEquals("RET1", resp.getParameter().get(0).getName());
  }

  @Test
  public void testOperationWithGetUsingParamsFailsWithNonPrimitive() throws Exception {
    HttpTestResponse status = ourServer.fhirRequest("/Patient/$OP_TYPE?PARAM1=PARAM1val&PARAM2=foo").get().assertStatus(405);
    String response = status.getBody();

		assertEquals("POST", status.getHeader(Constants.HEADER_ALLOW));
		assertThat(response).contains("Can not invoke operation $OP_TYPE using HTTP GET because parameter PARAM2 is not a primitive datatype");
  }

  @Test
  public void testOperationOnTypeReturnBundle() throws Exception {
    Parameters p = new Parameters();
    p.addParameter().setName("PARAM1").setValue(new StringType("PARAM1val"));
    p.addParameter().setName("PARAM2").setResource(new Patient().setActive(true));
    String inParamsStr = ourCtx.newXmlParser().encodeResourceToString(p);

    String response = ourServer.fhirRequest("/Patient/$OP_TYPE_RET_BUNDLE").post(inParamsStr, Constants.CT_FHIR_XML).assertStatus(200).getBody();

		assertEquals("PARAM1val", ourLastParam1.getValue());
		assertEquals(true, ourLastParam2.getActive());
		assertEquals("$OP_TYPE_RET_BUNDLE", ourLastMethod);

    Bundle resp = ourCtx.newXmlParser().parseResource(Bundle.class, response);
		assertEquals("100", resp.getEntry().get(0).getResponse().getStatus());
  }

  @Test
  public void testOperationOnServer() throws Exception {
    Parameters p = new Parameters();
    p.addParameter().setName("PARAM1").setValue(new StringType("PARAM1val"));
    p.addParameter().setName("PARAM2").setResource(new Patient().setActive(true));
    String inParamsStr = ourCtx.newXmlParser().encodeResourceToString(p);

    String response = ourServer.fhirRequest("/$OP_SERVER").post(inParamsStr, Constants.CT_FHIR_XML).assertStatus(200).getBody();

		assertEquals("PARAM1val", ourLastParam1.getValue());
		assertEquals(true, ourLastParam2.getActive());
		assertEquals("$OP_SERVER", ourLastMethod);

    Parameters resp = ourCtx.newXmlParser().parseResource(Parameters.class, response);
		assertEquals("RET1", resp.getParameter().get(0).getName());
  }

  @Test
  public void testOperationWithBundleProviderResponse() throws Exception {
    String response = ourServer.fhirRequest("/$OP_INSTANCE_BUNDLE_PROVIDER?_pretty=true").get().assertStatus(200).getBody();
    ourLog.info(response);

    Bundle resp = ourCtx.newXmlParser().parseResource(Bundle.class, response);
		assertNotNull(resp);
  }

  @Test
  public void testOperationWithListParam() throws Exception {
    Parameters p = new Parameters();
    p.addParameter().setName("PARAM2").setResource(new Patient().setActive(true));
    p.addParameter().setName("PARAM3").setValue(new StringType("PARAM3val1"));
    p.addParameter().setName("PARAM3").setValue(new StringType("PARAM3val2"));
    String inParamsStr = ourCtx.newXmlParser().encodeResourceToString(p);

    String response = ourServer.fhirRequest("/$OP_SERVER_LIST_PARAM").post(inParamsStr, Constants.CT_FHIR_XML).assertStatus(200).getBody();

		assertEquals("$OP_SERVER_LIST_PARAM", ourLastMethod);
		assertEquals(true, ourLastParam2.getActive());
		assertNull(ourLastParam1);
		assertThat(ourLastParam3).hasSize(2);
		assertEquals("PARAM3val1", ourLastParam3.get(0).getValue());
		assertEquals("PARAM3val2", ourLastParam3.get(1).getValue());

    Parameters resp = ourCtx.newXmlParser().parseResource(Parameters.class, response);
		assertEquals("RET1", resp.getParameter().get(0).getName());
  }

  @Test
  public void testOperationOnInstance() throws Exception {
    Parameters p = new Parameters();
    p.addParameter().setName("PARAM1").setValue(new StringType("PARAM1val"));
    p.addParameter().setName("PARAM2").setResource(new Patient().setActive(true));
    String inParamsStr = ourCtx.newXmlParser().encodeResourceToString(p);

    String response = ourServer.fhirRequest("/Patient/123/$OP_INSTANCE").post(inParamsStr, Constants.CT_FHIR_XML).assertStatus(200).getBody();

		assertEquals("PARAM1val", ourLastParam1.getValue());
		assertEquals(true, ourLastParam2.getActive());
		assertEquals("123", ourLastId.getIdPart());
		assertEquals("$OP_INSTANCE", ourLastMethod);

    Parameters resp = ourCtx.newXmlParser().parseResource(Parameters.class, response);
		assertEquals("RET1", resp.getParameter().get(0).getName());
  }

  @Test
  public void testOperationCantUseGetIfItIsntIdempotent() throws Exception {
    HttpTestResponse status = ourServer.fhirRequest("/Patient/123/$OP_INSTANCE").get().assertStatus(Constants.STATUS_HTTP_405_METHOD_NOT_ALLOWED);
    String response = status.getBody();

		assertEquals("POST", status.getHeader(Constants.HEADER_ALLOW));
		assertThat(response).contains("HTTP Method GET is not allowed");
  }

  @Test
  public void testOperationWrongParamType() throws Exception {
    Parameters p = new Parameters();
    p.addParameter().setName("PARAM1").setValue(new IntegerType("123"));
    p.addParameter().setName("PARAM2").setResource(new Patient().setActive(true));
    String inParamsStr = ourCtx.newXmlParser().encodeResourceToString(p);

    HttpTestResponse status = ourServer.fhirRequest("/Patient/$OP_TYPE").post(inParamsStr, Constants.CT_FHIR_XML).assertStatus(400);
    String response = status.getBody();

    ourLog.info(status.toString());
    ourLog.info(response);

		assertThat(response).contains("Request has parameter PARAM1 of type IntegerType but method expects type StringType");
  }

  @Test
  public void testReadWithOperations() throws Exception {
    ourServer.fhirRequest("/Patient/123").get().assertStatus(200);

		assertEquals("read", ourLastMethod);
  }

  @Test
  public void testInstanceEverythingPost() throws Exception {
    String inParamsStr = ourCtx.newXmlParser().encodeResourceToString(new Parameters());

    // Try with a POST
    String response = ourServer.fhirRequest("/Patient/123/$everything").post(inParamsStr, Constants.CT_FHIR_XML).assertStatus(200).getBody();

		assertEquals("instance $everything", ourLastMethod);
		assertThat(response).startsWith("<Bundle");
		assertEquals("Patient/123", ourLastId.toUnqualifiedVersionless().getValue());

  }

  @Test
  public void testInstanceEverythingHapiClient() throws Exception {
    Parameters p = ourCtx.newRestfulGenericClient(ourServer.getBaseUrl()).operation().onInstance(new IdType("Patient/123")).named("$everything").withParameters(new Parameters()).execute();
    Bundle b = (Bundle) p.getParameter().get(0).getResource();
		assertNotNull(b);

		assertEquals("instance $everything", ourLastMethod);
		assertEquals("Patient/123", ourLastId.toUnqualifiedVersionless().getValue());

  }

  @Test
  public void testInstanceEverythingGet() throws Exception {

    // Try with a GET
    String response = ourServer.fhirRequest("/Patient/123/$everything").get().assertStatus(200).getBody();

		assertEquals("instance $everything", ourLastMethod);
		assertThat(response).startsWith("<Bundle");
		assertEquals("Patient/123", ourLastId.toUnqualifiedVersionless().getValue());

  }
  public static class PlainProvider {

    //@formatter:off
    @Operation(name = "$OP_INSTANCE_BUNDLE_PROVIDER", idempotent = true)
    public IBundleProvider opInstanceReturnsBundleProvider() {
      ourLastMethod = "$OP_INSTANCE_BUNDLE_PROVIDER";

      List<IBaseResource> resources = new ArrayList<IBaseResource>();
      for (int i = 0; i < 100; i++) {
        Patient p = new Patient();
        p.setId("Patient/" + i);
        p.addName().addFamily("Patient " + i);
        resources.add(p);
      }

      return new SimpleBundleProvider(resources);
    }

    //@formatter:off
    @Operation(name = "$OP_SERVER")
    public Parameters opServer(
      @OperationParam(name = "PARAM1") StringType theParam1,
      @OperationParam(name = "PARAM2") Patient theParam2
    ) {
      //@formatter:on

      ourLastMethod = "$OP_SERVER";
      ourLastParam1 = theParam1;
      ourLastParam2 = theParam2;

      Parameters retVal = new Parameters();
      retVal.addParameter().setName("RET1").setValue(new StringType("RETVAL1"));
      return retVal;
    }

    //@formatter:off
    @Operation(name = "$OP_SERVER_LIST_PARAM")
    public Parameters opServerListParam(
      @OperationParam(name = "PARAM2") Patient theParam2,
      @OperationParam(name = "PARAM3") List<StringType> theParam3
    ) {
      //@formatter:on

      ourLastMethod = "$OP_SERVER_LIST_PARAM";
      ourLastParam2 = theParam2;
      ourLastParam3 = theParam3;

      Parameters retVal = new Parameters();
      retVal.addParameter().setName("RET1").setValue(new StringType("RETVAL1"));
      return retVal;
    }

  }

  public static class PatientProvider implements IResourceProvider {

    @Override
    public Class<Patient> getResourceType() {
      return Patient.class;
    }

    /**
     * Just to make sure this method doesn't "steal" calls
     */
    @Read
    public Patient read(@IdParam IdType theId) {
      ourLastMethod = "read";
      Patient retVal = new Patient();
      retVal.setId(theId);
      return retVal;
    }

    @Operation(name = "$everything", idempotent = true)
    public Bundle patientEverything(@IdParam IdType thePatientId) {
      ourLastMethod = "instance $everything";
      ourLastId = thePatientId;
      return new Bundle();
    }

    //@formatter:off
    @Operation(name = "$OP_TYPE", idempotent = true)
    public Parameters opType(
      @OperationParam(name = "PARAM1") StringType theParam1,
      @OperationParam(name = "PARAM2") Patient theParam2
    ) {
      //@formatter:on

      ourLastMethod = "$OP_TYPE";
      ourLastParam1 = theParam1;
      ourLastParam2 = theParam2;

      Parameters retVal = new Parameters();
      retVal.addParameter().setName("RET1").setValue(new StringType("RETVAL1"));
      return retVal;
    }

    //@formatter:off
    @Operation(name = "$OP_TYPE_ONLY_STRING", idempotent = true)
    public Parameters opTypeOnlyString(
      @OperationParam(name = "PARAM1") StringType theParam1
    ) {
      //@formatter:on

      ourLastMethod = "$OP_TYPE";
      ourLastParam1 = theParam1;

      Parameters retVal = new Parameters();
      retVal.addParameter().setName("RET1").setValue(new StringType("RETVAL1"));
      return retVal;
    }

    //@formatter:off
    @Operation(name = "$OP_TYPE_RET_BUNDLE")
    public Bundle opTypeRetBundle(
      @OperationParam(name = "PARAM1") StringType theParam1,
      @OperationParam(name = "PARAM2") Patient theParam2
    ) {
      //@formatter:on

      ourLastMethod = "$OP_TYPE_RET_BUNDLE";
      ourLastParam1 = theParam1;
      ourLastParam2 = theParam2;

      Bundle retVal = new Bundle();
      retVal.addEntry().getResponse().setStatus("100");
      return retVal;
    }

    //@formatter:off
    @Operation(name = "$OP_INSTANCE")
    public Parameters opInstance(
      @IdParam IdType theId,
      @OperationParam(name = "PARAM1") StringType theParam1,
      @OperationParam(name = "PARAM2") Patient theParam2
    ) {
      //@formatter:on

      ourLastMethod = "$OP_INSTANCE";
      ourLastId = theId;
      ourLastParam1 = theParam1;
      ourLastParam2 = theParam2;

      Parameters retVal = new Parameters();
      retVal.addParameter().setName("RET1").setValue(new StringType("RETVAL1"));
      return retVal;
    }

  }

  @AfterAll
  public static void afterClass() throws Exception {
    TestUtil.randomizeLocaleAndTimezone();
  }


}
