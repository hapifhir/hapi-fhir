package org.hl7.fhir.r4.utils;

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


import org.hl7.fhir.r4.model.ImplementationGuide.GuideParameterCode;
import org.hl7.fhir.r4.model.ImplementationGuide.ImplementationGuideDefinitionComponent;
import org.hl7.fhir.r4.model.ImplementationGuide.ImplementationGuideDefinitionParameterComponent;

public class IGHelper {

  public static String readStringParameter(ImplementationGuideDefinitionComponent ig, GuideParameterCode name) {
    for (ImplementationGuideDefinitionParameterComponent p : ig.getParameter()) {
      if (name == p.getCode()) {
        return p.getValue();
      }
    }
    return null;
  }

  public static boolean getBooleanParameter(ImplementationGuideDefinitionComponent ig, GuideParameterCode name, boolean defaultValue) {
    String v = readStringParameter(ig, name);
    return v == null ? false : Boolean.parseBoolean(v);
  }

  public static void setParameter(ImplementationGuideDefinitionComponent ig, GuideParameterCode name, String value) {
    for (ImplementationGuideDefinitionParameterComponent p : ig.getParameter()) {
      if (name == p.getCode()) {
        p.setValue(value);
        return;
      }
    }
    ImplementationGuideDefinitionParameterComponent p = ig.addParameter();
    p.setCode(name);
    p.setValue(value);
  }
  
  public static void setParameter(ImplementationGuideDefinitionComponent ig, GuideParameterCode name, boolean value) {
    setParameter(ig, name, Boolean.toString(value));
  }

}
