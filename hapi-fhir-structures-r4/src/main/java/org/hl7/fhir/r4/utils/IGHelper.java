package org.hl7.fhir.r4.utils;

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
