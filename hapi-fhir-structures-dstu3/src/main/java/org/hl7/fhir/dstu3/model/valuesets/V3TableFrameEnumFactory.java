package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class V3TableFrameEnumFactory implements EnumFactory<V3TableFrame> {

  public V3TableFrame fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("above".equals(codeString))
      return V3TableFrame.ABOVE;
    if ("below".equals(codeString))
      return V3TableFrame.BELOW;
    if ("border".equals(codeString))
      return V3TableFrame.BORDER;
    if ("box".equals(codeString))
      return V3TableFrame.BOX;
    if ("hsides".equals(codeString))
      return V3TableFrame.HSIDES;
    if ("lhs".equals(codeString))
      return V3TableFrame.LHS;
    if ("rhs".equals(codeString))
      return V3TableFrame.RHS;
    if ("void".equals(codeString))
      return V3TableFrame.VOID;
    if ("vsides".equals(codeString))
      return V3TableFrame.VSIDES;
    throw new IllegalArgumentException("Unknown V3TableFrame code '"+codeString+"'");
  }

  public String toCode(V3TableFrame code) {
    if (code == V3TableFrame.ABOVE)
      return "above";
    if (code == V3TableFrame.BELOW)
      return "below";
    if (code == V3TableFrame.BORDER)
      return "border";
    if (code == V3TableFrame.BOX)
      return "box";
    if (code == V3TableFrame.HSIDES)
      return "hsides";
    if (code == V3TableFrame.LHS)
      return "lhs";
    if (code == V3TableFrame.RHS)
      return "rhs";
    if (code == V3TableFrame.VOID)
      return "void";
    if (code == V3TableFrame.VSIDES)
      return "vsides";
    return "?";
  }

    public String toSystem(V3TableFrame code) {
      return code.getSystem();
      }

}

