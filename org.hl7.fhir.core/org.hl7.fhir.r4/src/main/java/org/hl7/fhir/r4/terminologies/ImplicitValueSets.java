package org.hl7.fhir.r4.terminologies;

/*-
 * #%L
 * org.hl7.fhir.r4
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


import org.hl7.fhir.r4.model.Enumerations.PublicationStatus;
import org.hl7.fhir.r4.model.ValueSet;
import org.hl7.fhir.utilities.Utilities;

public class ImplicitValueSets {

  public static ValueSet build(String url) {
    if (Utilities.noString(url))
      return null;
    
    if (url.startsWith("http://snomed.info/sct"))
      return buildSnomedValueSet(url);
    if (url.startsWith("http://loinc.org/vs"))
      return buildLoincValueSet(url);
    if (url.equals("http://unitsofmeasure.org/vs"))
      return allUcumValueSet();
    return null;
  }

  private static ValueSet buildSnomedValueSet(String url) {
    return null;
  }

  private static ValueSet buildLoincValueSet(String url) {
    if (url.startsWith("http://loinc.org/vs/LL")) {
      ValueSet vs = new ValueSet();
      vs.setUrl(url);
      vs.setStatus(PublicationStatus.ACTIVE);
      vs.setName("LoincVS"+url.substring(21).replace("-",  ""));
      vs.setTitle("Loinc Implicit ValueSet for "+url.substring(21));
      // todo: populate the compose fro the terminology server
      return vs;   
    } else if (url.equals("http://loinc.org/vs")) {
      ValueSet vs = new ValueSet();
      vs.setUrl(url);
      vs.setStatus(PublicationStatus.ACTIVE);
      vs.setName("LoincVSAll");
      vs.setTitle("Loinc Implicit ValueSet : all codes");
      // todo: populate the compose for the terminology server
      return vs;   
    } else {
      return null;
    }
  }

  private static ValueSet allUcumValueSet() {
    ValueSet vs = new ValueSet();
    vs.setUrl("http://unitsofmeasure.org/vs");
    vs.setStatus(PublicationStatus.ACTIVE);
    vs.setName("AllUcumCodes");
    vs.setTitle("All Ucum Codes");
    vs.getCompose().addInclude().setSystem("http://unitsofmeasure.org");
    return vs;
  }

}
