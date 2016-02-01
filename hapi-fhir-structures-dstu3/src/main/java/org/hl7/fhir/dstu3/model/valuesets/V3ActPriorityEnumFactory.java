package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class V3ActPriorityEnumFactory implements EnumFactory<V3ActPriority> {

  public V3ActPriority fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("A".equals(codeString))
      return V3ActPriority.A;
    if ("CR".equals(codeString))
      return V3ActPriority.CR;
    if ("CS".equals(codeString))
      return V3ActPriority.CS;
    if ("CSP".equals(codeString))
      return V3ActPriority.CSP;
    if ("CSR".equals(codeString))
      return V3ActPriority.CSR;
    if ("EL".equals(codeString))
      return V3ActPriority.EL;
    if ("EM".equals(codeString))
      return V3ActPriority.EM;
    if ("P".equals(codeString))
      return V3ActPriority.P;
    if ("PRN".equals(codeString))
      return V3ActPriority.PRN;
    if ("R".equals(codeString))
      return V3ActPriority.R;
    if ("RR".equals(codeString))
      return V3ActPriority.RR;
    if ("S".equals(codeString))
      return V3ActPriority.S;
    if ("T".equals(codeString))
      return V3ActPriority.T;
    if ("UD".equals(codeString))
      return V3ActPriority.UD;
    if ("UR".equals(codeString))
      return V3ActPriority.UR;
    throw new IllegalArgumentException("Unknown V3ActPriority code '"+codeString+"'");
  }

  public String toCode(V3ActPriority code) {
    if (code == V3ActPriority.A)
      return "A";
    if (code == V3ActPriority.CR)
      return "CR";
    if (code == V3ActPriority.CS)
      return "CS";
    if (code == V3ActPriority.CSP)
      return "CSP";
    if (code == V3ActPriority.CSR)
      return "CSR";
    if (code == V3ActPriority.EL)
      return "EL";
    if (code == V3ActPriority.EM)
      return "EM";
    if (code == V3ActPriority.P)
      return "P";
    if (code == V3ActPriority.PRN)
      return "PRN";
    if (code == V3ActPriority.R)
      return "R";
    if (code == V3ActPriority.RR)
      return "RR";
    if (code == V3ActPriority.S)
      return "S";
    if (code == V3ActPriority.T)
      return "T";
    if (code == V3ActPriority.UD)
      return "UD";
    if (code == V3ActPriority.UR)
      return "UR";
    return "?";
  }

    public String toSystem(V3ActPriority code) {
      return code.getSystem();
      }

}

