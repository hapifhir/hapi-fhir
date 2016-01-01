package ca.uhn.fhir.rest.server;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import javax.servlet.ServletException;

import org.hamcrest.core.StringContains;
import org.hl7.fhir.instance.model.Patient;
import org.hl7.fhir.instance.model.StringType;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;

public class ServerInvalidDefinitionHl7OrgDstu2Test {

  private static FhirContext ourCtx = FhirContext.forDstu2Hl7Org();

  private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory
      .getLogger(ServerInvalidDefinitionHl7OrgDstu2Test.class);

  @Test
  public void testOperationReturningOldBundleProvider() {
    RestfulServer srv = new RestfulServer(ourCtx);
    srv.setFhirContext(ourCtx);
    srv.setResourceProviders(new OperationReturningOldBundleProvider());

    try {
      srv.init();
      fail();
    } catch (ServletException e) {
      ourLog.info(e.getCause().toString());
      assertThat(e.getCause().toString(), StringContains.containsString("ConfigurationException"));
      assertThat(e.getCause().toString(), StringContains.containsString("Can not return a DSTU1 bundle"));
    }
  }

  public static class OperationReturningOldBundleProvider implements IResourceProvider {

    @Override
    public Class<? extends IBaseResource> getResourceType() {
      return Patient.class;
    }

    @Operation(name = "$OP_TYPE_RET_OLD_BUNDLE")
    public ca.uhn.fhir.model.api.Bundle opTypeRetOldBundle(@OperationParam(name = "PARAM1") StringType theParam1,
        @OperationParam(name = "PARAM2") Patient theParam2) {
      return null;
    }

  }

}
