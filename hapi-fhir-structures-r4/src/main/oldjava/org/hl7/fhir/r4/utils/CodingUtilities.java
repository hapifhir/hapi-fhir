package org.hl7.fhir.r4.utils;

import java.util.List;

import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.TestScript.TestActionComponent;

public class CodingUtilities {

  public static boolean matches(Coding coding, String system, String code) {
    if (coding == null)
      return false;
    return code.equals(coding.getCode()) && system.equals(coding.getSystem());
  }

  public static String present(Coding coding) {
    if (coding == null)
      return "";
    return coding.getSystem()+"::"+coding.getCode();
  }

}
