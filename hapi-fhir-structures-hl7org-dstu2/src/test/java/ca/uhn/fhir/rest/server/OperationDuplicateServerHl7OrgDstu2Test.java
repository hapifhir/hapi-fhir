package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.system.HapiSystemProperties;
import ca.uhn.fhir.test.utilities.HttpClientExtension;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.TestUtil;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.hl7.fhir.dstu2.model.Conformance;
import org.hl7.fhir.dstu2.model.OperationDefinition;
import org.hl7.fhir.dstu2.model.Organization;
import org.hl7.fhir.dstu2.model.Parameters;
import org.hl7.fhir.dstu2.model.Patient;
import org.hl7.fhir.dstu2.model.StringType;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class OperationDuplicateServerHl7OrgDstu2Test {
  private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(OperationDuplicateServerHl7OrgDstu2Test.class);
  private static final FhirContext ourCtx = FhirContext.forDstu2Hl7OrgCached();

  static {
    HapiSystemProperties.enableTestMode();
  }

  @RegisterExtension
  public static RestfulServerExtension ourServer = new RestfulServerExtension(ourCtx)
      .registerProvider(new PatientProvider())
      .registerProvider(new OrganizationProvider())
      .registerProvider(new PlainProvider())
      .withPagingProvider(new FifoMemoryPagingProvider(100))
      .setDefaultResponseEncoding(EncodingEnum.XML)
      .setDefaultPrettyPrint(false);

  @RegisterExtension
  public static HttpClientExtension ourClient = new HttpClientExtension();

  @Test
  public void testOperationsAreCollapsed() throws Exception {
    // Metadata
    {
      HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/metadata?_pretty=true");
      HttpResponse status = ourClient.execute(httpGet);

			assertEquals(200, status.getStatusLine().getStatusCode());
      String response = IOUtils.toString(status.getEntity().getContent());
      IOUtils.closeQuietly(status.getEntity().getContent());
      ourLog.info(response);

      Conformance resp = ourCtx.newXmlParser().parseResource(Conformance.class, response);

      ourLog.debug(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(resp));

			assertThat(resp.getRest().get(0).getOperation()).hasSize(1);
			assertEquals("$myoperation", resp.getRest().get(0).getOperation().get(0).getName());
			assertEquals("OperationDefinition/OrganizationPatient-ts-myoperation", resp.getRest().get(0).getOperation().get(0).getDefinition().getReference());
    }

    // OperationDefinition
    {
      HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/OperationDefinition/OrganizationPatient-ts-myoperation?_pretty=true");
      HttpResponse status = ourClient.execute(httpGet);

			assertEquals(200, status.getStatusLine().getStatusCode());
      String response = IOUtils.toString(status.getEntity().getContent());
      IOUtils.closeQuietly(status.getEntity().getContent());
      ourLog.info(response);

      OperationDefinition resp = ourCtx.newXmlParser().parseResource(OperationDefinition.class, response);
			assertEquals("$myoperation", resp.getCode());
			assertEquals(true, resp.getIdempotent());
			assertThat(resp.getType()).hasSize(2);
			assertThat(resp.getParameter()).hasSize(1);
    }
  }

  public static class BaseProvider {

    @Operation(name = "$myoperation", idempotent = true)
    public Parameters opInstanceReturnsBundleProvider(@OperationParam(name = "myparam") StringType theString) {
      return null;
    }

  }

  public static class OrganizationProvider extends BaseProvider implements IResourceProvider {

    @Override
    public Class<? extends IBaseResource> getResourceType() {
      return Organization.class;
    }

  }

  public static class PatientProvider extends BaseProvider implements IResourceProvider {

    @Override
    public Class<? extends IBaseResource> getResourceType() {
      return Patient.class;
    }

  }

  public static class PlainProvider {

    @Operation(name = "$myoperation", idempotent = true)
    public Parameters opInstanceReturnsBundleProvider(@OperationParam(name = "myparam") StringType theString) {
      return null;
    }

  }

  @AfterAll
  public static void afterClass() throws Exception {
    TestUtil.randomizeLocaleAndTimezone();
  }

}
