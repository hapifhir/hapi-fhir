package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.test.utilities.HttpClientExtension;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.TestUtil;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
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

  @RegisterExtension
  public static HttpClientExtension ourClient = new HttpClientExtension();

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

    HttpPost httpPost = new HttpPost(ourServer.getBaseUrl() + "/Patient/$OP_TYPE");
    httpPost.setEntity(new StringEntity(inParamsStr, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));
    HttpResponse status = ourClient.execute(httpPost);

		assertThat(status.getStatusLine().getStatusCode()).isEqualTo(200);
    String response = IOUtils.toString(status.getEntity().getContent());
    IOUtils.closeQuietly(status.getEntity().getContent());

		assertThat(ourLastParam1.getValue()).isEqualTo("PARAM1val");
		assertThat(ourLastParam2.getActive()).isEqualTo(true);
		assertThat(ourLastMethod).isEqualTo("$OP_TYPE");

    Parameters resp = ourCtx.newXmlParser().parseResource(Parameters.class, response);
		assertThat(resp.getParameter().get(0).getName()).isEqualTo("RET1");
  }

  @Test
  public void testOperationWithGetUsingParams() throws Exception {
    HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient/$OP_TYPE?PARAM1=PARAM1val");
    HttpResponse status = ourClient.execute(httpGet);

		assertThat(status.getStatusLine().getStatusCode()).isEqualTo(200);
    String response = IOUtils.toString(status.getEntity().getContent());
    IOUtils.closeQuietly(status.getEntity().getContent());

		assertThat(ourLastParam1.getValue()).isEqualTo("PARAM1val");
		assertThat(ourLastParam2).isNull();
		assertThat(ourLastMethod).isEqualTo("$OP_TYPE");

    Parameters resp = ourCtx.newXmlParser().parseResource(Parameters.class, response);
		assertThat(resp.getParameter().get(0).getName()).isEqualTo("RET1");
  }

  @Test
  public void testOperationWithGetUsingParamsFailsWithNonPrimitive() throws Exception {
    HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient/$OP_TYPE?PARAM1=PARAM1val&PARAM2=foo");
    HttpResponse status = ourClient.execute(httpGet);

		assertThat(status.getStatusLine().getStatusCode()).isEqualTo(405);
    String response = IOUtils.toString(status.getEntity().getContent());
    IOUtils.closeQuietly(status.getEntity().getContent());

		assertThat(status.getFirstHeader(Constants.HEADER_ALLOW).getValue()).isEqualTo("POST");
		assertThat(response).contains("Can not invoke operation $OP_TYPE using HTTP GET because parameter PARAM2 is not a primitive datatype");
  }

  @Test
  public void testOperationOnTypeReturnBundle() throws Exception {
    Parameters p = new Parameters();
    p.addParameter().setName("PARAM1").setValue(new StringType("PARAM1val"));
    p.addParameter().setName("PARAM2").setResource(new Patient().setActive(true));
    String inParamsStr = ourCtx.newXmlParser().encodeResourceToString(p);

    HttpPost httpPost = new HttpPost(ourServer.getBaseUrl() + "/Patient/$OP_TYPE_RET_BUNDLE");
    httpPost.setEntity(new StringEntity(inParamsStr, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));
    HttpResponse status = ourClient.execute(httpPost);

		assertThat(status.getStatusLine().getStatusCode()).isEqualTo(200);
    String response = IOUtils.toString(status.getEntity().getContent());
    IOUtils.closeQuietly(status.getEntity().getContent());

		assertThat(ourLastParam1.getValue()).isEqualTo("PARAM1val");
		assertThat(ourLastParam2.getActive()).isEqualTo(true);
		assertThat(ourLastMethod).isEqualTo("$OP_TYPE_RET_BUNDLE");

    Bundle resp = ourCtx.newXmlParser().parseResource(Bundle.class, response);
		assertThat(resp.getEntry().get(0).getResponse().getStatus()).isEqualTo("100");
  }

  @Test
  public void testOperationOnServer() throws Exception {
    Parameters p = new Parameters();
    p.addParameter().setName("PARAM1").setValue(new StringType("PARAM1val"));
    p.addParameter().setName("PARAM2").setResource(new Patient().setActive(true));
    String inParamsStr = ourCtx.newXmlParser().encodeResourceToString(p);

    HttpPost httpPost = new HttpPost(ourServer.getBaseUrl() + "/$OP_SERVER");
    httpPost.setEntity(new StringEntity(inParamsStr, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));
    HttpResponse status = ourClient.execute(httpPost);

		assertThat(status.getStatusLine().getStatusCode()).isEqualTo(200);
    String response = IOUtils.toString(status.getEntity().getContent());
    IOUtils.closeQuietly(status.getEntity().getContent());

		assertThat(ourLastParam1.getValue()).isEqualTo("PARAM1val");
		assertThat(ourLastParam2.getActive()).isEqualTo(true);
		assertThat(ourLastMethod).isEqualTo("$OP_SERVER");

    Parameters resp = ourCtx.newXmlParser().parseResource(Parameters.class, response);
		assertThat(resp.getParameter().get(0).getName()).isEqualTo("RET1");
  }

  @Test
  public void testOperationWithBundleProviderResponse() throws Exception {
    HttpGet httpPost = new HttpGet(ourServer.getBaseUrl() + "/$OP_INSTANCE_BUNDLE_PROVIDER?_pretty=true");
    HttpResponse status = ourClient.execute(httpPost);

		assertThat(status.getStatusLine().getStatusCode()).isEqualTo(200);
    String response = IOUtils.toString(status.getEntity().getContent());
    IOUtils.closeQuietly(status.getEntity().getContent());
    ourLog.info(response);

    Bundle resp = ourCtx.newXmlParser().parseResource(Bundle.class, response);
		assertThat(resp).isNotNull();
  }

  @Test
  public void testOperationWithListParam() throws Exception {
    Parameters p = new Parameters();
    p.addParameter().setName("PARAM2").setResource(new Patient().setActive(true));
    p.addParameter().setName("PARAM3").setValue(new StringType("PARAM3val1"));
    p.addParameter().setName("PARAM3").setValue(new StringType("PARAM3val2"));
    String inParamsStr = ourCtx.newXmlParser().encodeResourceToString(p);

    HttpPost httpPost = new HttpPost(ourServer.getBaseUrl() + "/$OP_SERVER_LIST_PARAM");
    httpPost.setEntity(new StringEntity(inParamsStr, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));
    HttpResponse status = ourClient.execute(httpPost);

		assertThat(status.getStatusLine().getStatusCode()).isEqualTo(200);
    String response = IOUtils.toString(status.getEntity().getContent());
    IOUtils.closeQuietly(status.getEntity().getContent());

		assertThat(ourLastMethod).isEqualTo("$OP_SERVER_LIST_PARAM");
		assertThat(ourLastParam2.getActive()).isEqualTo(true);
		assertThat(ourLastParam1).isEqualTo(null);
		assertThat(ourLastParam3).hasSize(2);
		assertThat(ourLastParam3.get(0).getValue()).isEqualTo("PARAM3val1");
		assertThat(ourLastParam3.get(1).getValue()).isEqualTo("PARAM3val2");

    Parameters resp = ourCtx.newXmlParser().parseResource(Parameters.class, response);
		assertThat(resp.getParameter().get(0).getName()).isEqualTo("RET1");
  }

  @Test
  public void testOperationOnInstance() throws Exception {
    Parameters p = new Parameters();
    p.addParameter().setName("PARAM1").setValue(new StringType("PARAM1val"));
    p.addParameter().setName("PARAM2").setResource(new Patient().setActive(true));
    String inParamsStr = ourCtx.newXmlParser().encodeResourceToString(p);

    HttpPost httpPost = new HttpPost(ourServer.getBaseUrl() + "/Patient/123/$OP_INSTANCE");
    httpPost.setEntity(new StringEntity(inParamsStr, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));
    HttpResponse status = ourClient.execute(httpPost);

		assertThat(status.getStatusLine().getStatusCode()).isEqualTo(200);
    String response = IOUtils.toString(status.getEntity().getContent());
    IOUtils.closeQuietly(status.getEntity().getContent());

		assertThat(ourLastParam1.getValue()).isEqualTo("PARAM1val");
		assertThat(ourLastParam2.getActive()).isEqualTo(true);
		assertThat(ourLastId.getIdPart()).isEqualTo("123");
		assertThat(ourLastMethod).isEqualTo("$OP_INSTANCE");

    Parameters resp = ourCtx.newXmlParser().parseResource(Parameters.class, response);
		assertThat(resp.getParameter().get(0).getName()).isEqualTo("RET1");
  }

  @Test
  public void testOperationCantUseGetIfItIsntIdempotent() throws Exception {
    HttpGet httpPost = new HttpGet(ourServer.getBaseUrl() + "/Patient/123/$OP_INSTANCE");
    HttpResponse status = ourClient.execute(httpPost);

		assertThat(status.getStatusLine().getStatusCode()).isEqualTo(Constants.STATUS_HTTP_405_METHOD_NOT_ALLOWED);
    String response = IOUtils.toString(status.getEntity().getContent());
    IOUtils.closeQuietly(status.getEntity().getContent());

		assertThat(status.getFirstHeader(Constants.HEADER_ALLOW).getValue()).isEqualTo("POST");
		assertThat(response).contains("HTTP Method GET is not allowed");
  }

  @Test
  public void testOperationWrongParamType() throws Exception {
    Parameters p = new Parameters();
    p.addParameter().setName("PARAM1").setValue(new IntegerType("123"));
    p.addParameter().setName("PARAM2").setResource(new Patient().setActive(true));
    String inParamsStr = ourCtx.newXmlParser().encodeResourceToString(p);

    HttpPost httpPost = new HttpPost(ourServer.getBaseUrl() + "/Patient/$OP_TYPE");
    httpPost.setEntity(new StringEntity(inParamsStr, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));
    HttpResponse status = ourClient.execute(httpPost);

		assertThat(status.getStatusLine().getStatusCode()).isEqualTo(400);
    String response = IOUtils.toString(status.getEntity().getContent());
    IOUtils.closeQuietly(status.getEntity().getContent());

    ourLog.info(status.getStatusLine().toString());
    ourLog.info(response);

		assertThat(response).contains("Request has parameter PARAM1 of type IntegerType but method expects type StringType");
  }

  @Test
  public void testReadWithOperations() throws Exception {
    HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient/123");
    HttpResponse status = ourClient.execute(httpGet);

		assertThat(status.getStatusLine().getStatusCode()).isEqualTo(200);
    IOUtils.closeQuietly(status.getEntity().getContent());

		assertThat(ourLastMethod).isEqualTo("read");
  }

  @Test
  public void testInstanceEverythingPost() throws Exception {
    String inParamsStr = ourCtx.newXmlParser().encodeResourceToString(new Parameters());

    // Try with a POST
    HttpPost httpPost = new HttpPost(ourServer.getBaseUrl() + "/Patient/123/$everything");
    httpPost.setEntity(new StringEntity(inParamsStr, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));
    HttpResponse status = ourClient.execute(httpPost);

		assertThat(status.getStatusLine().getStatusCode()).isEqualTo(200);
    String response = IOUtils.toString(status.getEntity().getContent());
    IOUtils.closeQuietly(status.getEntity().getContent());

		assertThat(ourLastMethod).isEqualTo("instance $everything");
		assertThat(response).startsWith("<Bundle");
		assertThat(ourLastId.toUnqualifiedVersionless().getValue()).isEqualTo("Patient/123");

  }

  @Test
  public void testInstanceEverythingHapiClient() throws Exception {
    Parameters p = ourCtx.newRestfulGenericClient(ourServer.getBaseUrl()).operation().onInstance(new IdType("Patient/123")).named("$everything").withParameters(new Parameters()).execute();
    Bundle b = (Bundle) p.getParameter().get(0).getResource();
		assertThat(b).isNotNull();

		assertThat(ourLastMethod).isEqualTo("instance $everything");
		assertThat(ourLastId.toUnqualifiedVersionless().getValue()).isEqualTo("Patient/123");

  }

  @Test
  public void testInstanceEverythingGet() throws Exception {

    // Try with a GET
    HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient/123/$everything");
    CloseableHttpResponse status = ourClient.execute(httpGet);

		assertThat(status.getStatusLine().getStatusCode()).isEqualTo(200);
    String response = IOUtils.toString(status.getEntity().getContent());
    IOUtils.closeQuietly(status.getEntity().getContent());

		assertThat(ourLastMethod).isEqualTo("instance $everything");
		assertThat(response).startsWith("<Bundle");
		assertThat(ourLastId.toUnqualifiedVersionless().getValue()).isEqualTo("Patient/123");

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
