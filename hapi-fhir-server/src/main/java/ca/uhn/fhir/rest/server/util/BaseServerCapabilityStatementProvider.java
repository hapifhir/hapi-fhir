package ca.uhn.fhir.rest.server.util;

/*-
 * #%L
 * HAPI FHIR - Server Framework
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.IRestfulServerDefaults;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.RestfulServerConfiguration;
import org.apache.commons.lang3.Validate;

import javax.annotation.Nullable;

public class BaseServerCapabilityStatementProvider {

  private RestfulServerConfiguration myConfiguration;

  protected BaseServerCapabilityStatementProvider() {
    super();
  }

  protected BaseServerCapabilityStatementProvider(RestfulServerConfiguration theServerConfiguration) {
    myConfiguration = theServerConfiguration;
  }


  protected RestfulServerConfiguration getServerConfiguration(@Nullable RequestDetails theRequestDetails) {
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
