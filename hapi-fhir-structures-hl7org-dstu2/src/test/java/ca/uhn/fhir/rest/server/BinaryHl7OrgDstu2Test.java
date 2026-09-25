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
import ca.uhn.fhir.test.utilities.HttpTestResponse;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.TestUtil;
import org.hl7.fhir.dstu2.model.Binary;
import org.hl7.fhir.dstu2.model.IdType;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by dsotnikov on 2/25/2014.
 */
public class BinaryHl7OrgDstu2Test {

  private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(BinaryHl7OrgDstu2Test.class);
  private static final FhirContext ourCtx = FhirContext.forDstu2Hl7OrgCached();
  private static Binary ourLast;

  @RegisterExtension
  public static RestfulServerExtension ourServer = new RestfulServerExtension(ourCtx)
      .registerProvider(new ResourceProvider())
      .withPagingProvider(new FifoMemoryPagingProvider(100))
      .setDefaultResponseEncoding(EncodingEnum.XML)
      .setDefaultPrettyPrint(false);

  @BeforeEach
  public void before() {
    ourLast = null;
  }

  @Test
  public void testReadWithExplicitTypeXml() throws Exception {
    HttpTestResponse status = ourServer.fhirRequest("/Binary/foo?_format=xml").get().assertStatus(200);
    String responseContent = status.getBody();

    ourLog.info(responseContent);

		assertThat(status.getHeader("content-type")).startsWith(Constants.CT_FHIR_XML + ";");

    Binary bin = ourCtx.newXmlParser().parseResource(Binary.class, responseContent);
		assertEquals("foo", bin.getContentType());
		assertThat(bin.getContent()).containsExactly(new byte[]{1, 2, 3, 4});
  }

  @Test
  public void testReadWithExplicitTypeJson() throws Exception {
    HttpTestResponse status = ourServer.fhirRequest("/Binary/foo?_format=json").get().assertStatus(200);
    String responseContent = status.getBody();

    ourLog.info(responseContent);

		assertThat(status.getHeader("content-type")).startsWith(Constants.CT_FHIR_JSON + ";");

    Binary bin = ourCtx.newJsonParser().parseResource(Binary.class, responseContent);
		assertEquals("foo", bin.getContentType());
		assertThat(bin.getContent()).containsExactly(new byte[]{1, 2, 3, 4});
  }


  @Test
  public void testCreate() throws Exception {
    ourServer.fhirRequest("/Binary").post(new byte[]{1, 2, 3, 4}, "foo/bar; charset=UTF-8").assertStatus(201);

		assertEquals("foo/bar; charset=UTF-8", ourLast.getContentType());
		assertThat(ourLast.getContent()).containsExactly(new byte[]{1, 2, 3, 4});

  }

  @Test
  public void testRead() throws Exception {
    HttpTestResponse status = ourServer.fhirRequest("/Binary/foo").get().assertStatus(200);
    byte[] responseContent = status.getBodyBytes();
		assertEquals("foo", status.getHeader("content-type"));
		assertThat(responseContent).containsExactly(new byte[]{1, 2, 3, 4});

  }

  @Test
  public void testSearchJson() throws Exception {
    HttpTestResponse status = ourServer.fhirRequest("/Binary?_pretty=true&_format=json").get().assertStatus(200);
    String responseContent = status.getBody();
		assertEquals(Constants.CT_FHIR_JSON + ";charset=utf-8", status.getHeader("content-type").replace(" ", "").replace("UTF", "utf"));

    ourLog.info(responseContent);

		org.hl7.fhir.dstu2.model.Bundle bundle = ourCtx.newJsonParser().parseResource(org.hl7.fhir.dstu2.model.Bundle.class, responseContent);
    Binary bin = (Binary) bundle.getEntry().get(0).getResource();

		assertEquals("text/plain", bin.getContentType());
		assertThat(bin.getContent()).containsExactly(new byte[]{1, 2, 3, 4});
  }

  @Test
  public void testSearchXml() throws Exception {
    HttpTestResponse status = ourServer.fhirRequest("/Binary?_pretty=true").get().assertStatus(200);
    String responseContent = status.getBody();
		assertEquals(Constants.CT_FHIR_XML + ";charset=utf-8", status.getHeader("content-type").replace(" ", "").replace("UTF", "utf"));

    ourLog.info(responseContent);

		org.hl7.fhir.dstu2.model.Bundle bundle = ourCtx.newXmlParser().parseResource(org.hl7.fhir.dstu2.model.Bundle.class, responseContent);
    Binary bin = (Binary) bundle.getEntry().get(0).getResource();

		assertEquals("text/plain", bin.getContentType());
		assertThat(bin.getContent()).containsExactly(new byte[]{1, 2, 3, 4});
  }

  /**
   * Created by dsotnikov on 2/25/2014.
   */
  public static class ResourceProvider implements IResourceProvider {

    @Create
    public MethodOutcome create(@ResourceParam Binary theBinary) {
      ourLast = theBinary;
      return new MethodOutcome(new IdType("1"));
    }

    @Override
    public Class<? extends IBaseResource> getResourceType() {
      return Binary.class;
    }

    @Read
    public Binary read(@IdParam IdType theId) {
      Binary retVal = new Binary();
      retVal.setId("1");
      retVal.setContent(new byte[]{1, 2, 3, 4});
      retVal.setContentType(theId.getIdPart());
      return retVal;
    }

    @Search
    public List<Binary> search() {
      Binary retVal = new Binary();
      retVal.setId("1");
      retVal.setContent(new byte[]{1, 2, 3, 4});
      retVal.setContentType("text/plain");
      return Collections.singletonList(retVal);
    }

  }

  @AfterAll
  public static void afterClass() throws Exception {
    TestUtil.randomizeLocaleAndTimezone();
  }

}
