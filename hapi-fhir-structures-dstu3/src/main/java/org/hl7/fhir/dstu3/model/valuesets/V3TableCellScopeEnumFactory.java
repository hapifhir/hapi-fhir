package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class V3TableCellScopeEnumFactory implements EnumFactory<V3TableCellScope> {

  public V3TableCellScope fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("col".equals(codeString))
      return V3TableCellScope.COL;
    if ("colgroup".equals(codeString))
      return V3TableCellScope.COLGROUP;
    if ("row".equals(codeString))
      return V3TableCellScope.ROW;
    if ("rowgroup".equals(codeString))
      return V3TableCellScope.ROWGROUP;
    throw new IllegalArgumentException("Unknown V3TableCellScope code '"+codeString+"'");
  }

  public String toCode(V3TableCellScope code) {
    if (code == V3TableCellScope.COL)
      return "col";
    if (code == V3TableCellScope.COLGROUP)
      return "colgroup";
    if (code == V3TableCellScope.ROW)
      return "row";
    if (code == V3TableCellScope.ROWGROUP)
      return "rowgroup";
    return "?";
  }

    public String toSystem(V3TableCellScope code) {
      return code.getSystem();
      }

}

