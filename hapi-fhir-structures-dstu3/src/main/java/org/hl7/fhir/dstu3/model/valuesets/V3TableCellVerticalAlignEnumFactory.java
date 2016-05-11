package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class V3TableCellVerticalAlignEnumFactory implements EnumFactory<V3TableCellVerticalAlign> {

  public V3TableCellVerticalAlign fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("baseline".equals(codeString))
      return V3TableCellVerticalAlign.BASELINE;
    if ("bottom".equals(codeString))
      return V3TableCellVerticalAlign.BOTTOM;
    if ("middle".equals(codeString))
      return V3TableCellVerticalAlign.MIDDLE;
    if ("top".equals(codeString))
      return V3TableCellVerticalAlign.TOP;
    throw new IllegalArgumentException("Unknown V3TableCellVerticalAlign code '"+codeString+"'");
  }

  public String toCode(V3TableCellVerticalAlign code) {
    if (code == V3TableCellVerticalAlign.BASELINE)
      return "baseline";
    if (code == V3TableCellVerticalAlign.BOTTOM)
      return "bottom";
    if (code == V3TableCellVerticalAlign.MIDDLE)
      return "middle";
    if (code == V3TableCellVerticalAlign.TOP)
      return "top";
    return "?";
  }

    public String toSystem(V3TableCellVerticalAlign code) {
      return code.getSystem();
      }

}

