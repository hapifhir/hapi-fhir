package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class ListItemFlagEnumFactory implements EnumFactory<ListItemFlag> {

  public ListItemFlag fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("01".equals(codeString))
      return ListItemFlag._01;
    if ("02".equals(codeString))
      return ListItemFlag._02;
    if ("03".equals(codeString))
      return ListItemFlag._03;
    if ("04".equals(codeString))
      return ListItemFlag._04;
    if ("05".equals(codeString))
      return ListItemFlag._05;
    if ("06".equals(codeString))
      return ListItemFlag._06;
    throw new IllegalArgumentException("Unknown ListItemFlag code '"+codeString+"'");
  }

  public String toCode(ListItemFlag code) {
    if (code == ListItemFlag._01)
      return "01";
    if (code == ListItemFlag._02)
      return "02";
    if (code == ListItemFlag._03)
      return "03";
    if (code == ListItemFlag._04)
      return "04";
    if (code == ListItemFlag._05)
      return "05";
    if (code == ListItemFlag._06)
      return "06";
    return "?";
  }

    public String toSystem(ListItemFlag code) {
      return code.getSystem();
      }

}

