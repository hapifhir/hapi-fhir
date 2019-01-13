package org.hl7.fhir.convertors;

/*-
 * #%L
 * org.hl7.fhir.convertors
 * %%
 * Copyright (C) 2014 - 2019 Health Level 7
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


import java.net.URISyntaxException;

import org.hl7.fhir.r4.model.FhirPublication;
import org.hl7.fhir.r4.terminologies.TerminologyClient;
import org.hl7.fhir.r4.terminologies.TerminologyClientR4;
import org.hl7.fhir.utilities.Utilities;

public class TerminologyClientFactory {

  public static TerminologyClient makeClient(String url, FhirPublication v) throws URISyntaxException {
    if (v == null)
      return new TerminologyClientR4(checkEndsWith("/r4", url));
    switch (v) {
    case DSTU2016May: return new TerminologyClientR3(checkEndsWith("/r3", url)); // r3 is the least worst match 
    case DSTU1: throw new Error("The version "+v.toString()+" is not currently supported");
    case DSTU2: return new TerminologyClientR2(checkEndsWith("/r2", url));
    case R4: return new TerminologyClientR4(checkEndsWith("/r4", url));
    case STU3: return new TerminologyClientR3(checkEndsWith("/r3", url));
    default: throw new Error("The version "+v.toString()+" is not currently supported");
    }

  }
  private static String checkEndsWith(String term, String url) {
    if (url.endsWith(term))
      return url;
    if (url.startsWith("http://tx.fhir.org"))
      return Utilities.pathURL(url, term);
    if (url.equals("http://local.fhir.org:960"))
      return Utilities.pathURL(url, term);
    return url;
  }

}
