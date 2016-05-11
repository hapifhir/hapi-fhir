package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class V3TableCellHorizontalAlignEnumFactory implements EnumFactory<V3TableCellHorizontalAlign> {

  public V3TableCellHorizontalAlign fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("center".equals(codeString))
      return V3TableCellHorizontalAlign.CENTER;
    if ("char".equals(codeString))
      return V3TableCellHorizontalAlign.CHAR;
    if ("justify".equals(codeString))
      return V3TableCellHorizontalAlign.JUSTIFY;
    if ("left".equals(codeString))
      return V3TableCellHorizontalAlign.LEFT;
    if ("right".equals(codeString))
      return V3TableCellHorizontalAlign.RIGHT;
    throw new IllegalArgumentException("Unknown V3TableCellHorizontalAlign code '"+codeString+"'");
  }

  public String toCode(V3TableCellHorizontalAlign code) {
    if (code == V3TableCellHorizontalAlign.CENTER)
      return "center";
    if (code == V3TableCellHorizontalAlign.CHAR)
      return "char";
    if (code == V3TableCellHorizontalAlign.JUSTIFY)
      return "justify";
    if (code == V3TableCellHorizontalAlign.LEFT)
      return "left";
    if (code == V3TableCellHorizontalAlign.RIGHT)
      return "right";
    return "?";
  }

    public String toSystem(V3TableCellHorizontalAlign code) {
      return code.getSystem();
      }

}

