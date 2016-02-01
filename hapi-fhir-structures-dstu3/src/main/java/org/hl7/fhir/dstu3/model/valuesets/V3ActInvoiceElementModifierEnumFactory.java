package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class V3ActInvoiceElementModifierEnumFactory implements EnumFactory<V3ActInvoiceElementModifier> {

  public V3ActInvoiceElementModifier fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("EFORM".equals(codeString))
      return V3ActInvoiceElementModifier.EFORM;
    if ("FAX".equals(codeString))
      return V3ActInvoiceElementModifier.FAX;
    if ("LINV".equals(codeString))
      return V3ActInvoiceElementModifier.LINV;
    if ("PAPER".equals(codeString))
      return V3ActInvoiceElementModifier.PAPER;
    throw new IllegalArgumentException("Unknown V3ActInvoiceElementModifier code '"+codeString+"'");
  }

  public String toCode(V3ActInvoiceElementModifier code) {
    if (code == V3ActInvoiceElementModifier.EFORM)
      return "EFORM";
    if (code == V3ActInvoiceElementModifier.FAX)
      return "FAX";
    if (code == V3ActInvoiceElementModifier.LINV)
      return "LINV";
    if (code == V3ActInvoiceElementModifier.PAPER)
      return "PAPER";
    return "?";
  }

    public String toSystem(V3ActInvoiceElementModifier code) {
      return code.getSystem();
      }

}

