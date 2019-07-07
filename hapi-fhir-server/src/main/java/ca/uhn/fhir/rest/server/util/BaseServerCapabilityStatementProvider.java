package ca.uhn.fhir.rest.server.util;

import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.IRestfulServerDefaults;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.RestfulServerConfiguration;
import org.apache.commons.lang3.Validate;

public class BaseServerCapabilityStatementProvider {

  private RestfulServerConfiguration myConfiguration;

  protected BaseServerCapabilityStatementProvider() {
    super();
  }

  protected BaseServerCapabilityStatementProvider(RestfulServerConfiguration theServerConfiguration) {
    myConfiguration = theServerConfiguration;
  }


  protected RestfulServerConfiguration getServerConfiguration(RequestDetails theRequestDetails) {
    RestfulServerConfiguration retVal;
    if (theRequestDetails != null && theRequestDetails.getServer() instanceof RestfulServer) {
      retVal = ((RestfulServer) theRequestDetails.getServer()).createConfiguration();
      Validate.isTrue(myConfiguration == null);
    } else {
      retVal = myConfiguration;
      Validate.notNull(retVal);
    }
    return retVal;
  }

}
