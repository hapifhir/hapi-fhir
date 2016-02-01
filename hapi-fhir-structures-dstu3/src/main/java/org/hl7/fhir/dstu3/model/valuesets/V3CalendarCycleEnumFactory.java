package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class V3CalendarCycleEnumFactory implements EnumFactory<V3CalendarCycle> {

  public V3CalendarCycle fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("_CalendarCycleOneLetter".equals(codeString))
      return V3CalendarCycle._CALENDARCYCLEONELETTER;
    if ("CW".equals(codeString))
      return V3CalendarCycle.CW;
    if ("CY".equals(codeString))
      return V3CalendarCycle.CY;
    if ("D".equals(codeString))
      return V3CalendarCycle.D;
    if ("DW".equals(codeString))
      return V3CalendarCycle.DW;
    if ("H".equals(codeString))
      return V3CalendarCycle.H;
    if ("M".equals(codeString))
      return V3CalendarCycle.M;
    if ("N".equals(codeString))
      return V3CalendarCycle.N;
    if ("S".equals(codeString))
      return V3CalendarCycle.S;
    if ("_CalendarCycleTwoLetter".equals(codeString))
      return V3CalendarCycle._CALENDARCYCLETWOLETTER;
    if ("CD".equals(codeString))
      return V3CalendarCycle.CD;
    if ("CH".equals(codeString))
      return V3CalendarCycle.CH;
    if ("CM".equals(codeString))
      return V3CalendarCycle.CM;
    if ("CN".equals(codeString))
      return V3CalendarCycle.CN;
    if ("CS".equals(codeString))
      return V3CalendarCycle.CS;
    if ("DY".equals(codeString))
      return V3CalendarCycle.DY;
    if ("WY".equals(codeString))
      return V3CalendarCycle.WY;
    if ("WM".equals(codeString))
      return V3CalendarCycle.WM;
    throw new IllegalArgumentException("Unknown V3CalendarCycle code '"+codeString+"'");
  }

  public String toCode(V3CalendarCycle code) {
    if (code == V3CalendarCycle._CALENDARCYCLEONELETTER)
      return "_CalendarCycleOneLetter";
    if (code == V3CalendarCycle.CW)
      return "CW";
    if (code == V3CalendarCycle.CY)
      return "CY";
    if (code == V3CalendarCycle.D)
      return "D";
    if (code == V3CalendarCycle.DW)
      return "DW";
    if (code == V3CalendarCycle.H)
      return "H";
    if (code == V3CalendarCycle.M)
      return "M";
    if (code == V3CalendarCycle.N)
      return "N";
    if (code == V3CalendarCycle.S)
      return "S";
    if (code == V3CalendarCycle._CALENDARCYCLETWOLETTER)
      return "_CalendarCycleTwoLetter";
    if (code == V3CalendarCycle.CD)
      return "CD";
    if (code == V3CalendarCycle.CH)
      return "CH";
    if (code == V3CalendarCycle.CM)
      return "CM";
    if (code == V3CalendarCycle.CN)
      return "CN";
    if (code == V3CalendarCycle.CS)
      return "CS";
    if (code == V3CalendarCycle.DY)
      return "DY";
    if (code == V3CalendarCycle.WY)
      return "WY";
    if (code == V3CalendarCycle.WM)
      return "WM";
    return "?";
  }

    public String toSystem(V3CalendarCycle code) {
      return code.getSystem();
      }

}

