package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class V3AddressPartTypeEnumFactory implements EnumFactory<V3AddressPartType> {

  public V3AddressPartType fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("ADL".equals(codeString))
      return V3AddressPartType.ADL;
    if ("AL".equals(codeString))
      return V3AddressPartType.AL;
    if ("DAL".equals(codeString))
      return V3AddressPartType.DAL;
    if ("SAL".equals(codeString))
      return V3AddressPartType.SAL;
    if ("BNN".equals(codeString))
      return V3AddressPartType.BNN;
    if ("BNR".equals(codeString))
      return V3AddressPartType.BNR;
    if ("BNS".equals(codeString))
      return V3AddressPartType.BNS;
    if ("CAR".equals(codeString))
      return V3AddressPartType.CAR;
    if ("CEN".equals(codeString))
      return V3AddressPartType.CEN;
    if ("CNT".equals(codeString))
      return V3AddressPartType.CNT;
    if ("CPA".equals(codeString))
      return V3AddressPartType.CPA;
    if ("CTY".equals(codeString))
      return V3AddressPartType.CTY;
    if ("DEL".equals(codeString))
      return V3AddressPartType.DEL;
    if ("DINST".equals(codeString))
      return V3AddressPartType.DINST;
    if ("DINSTA".equals(codeString))
      return V3AddressPartType.DINSTA;
    if ("DINSTQ".equals(codeString))
      return V3AddressPartType.DINSTQ;
    if ("DIR".equals(codeString))
      return V3AddressPartType.DIR;
    if ("DMOD".equals(codeString))
      return V3AddressPartType.DMOD;
    if ("DMODID".equals(codeString))
      return V3AddressPartType.DMODID;
    if ("DPID".equals(codeString))
      return V3AddressPartType.DPID;
    if ("INT".equals(codeString))
      return V3AddressPartType.INT;
    if ("POB".equals(codeString))
      return V3AddressPartType.POB;
    if ("PRE".equals(codeString))
      return V3AddressPartType.PRE;
    if ("STA".equals(codeString))
      return V3AddressPartType.STA;
    if ("STB".equals(codeString))
      return V3AddressPartType.STB;
    if ("STR".equals(codeString))
      return V3AddressPartType.STR;
    if ("STTYP".equals(codeString))
      return V3AddressPartType.STTYP;
    if ("UNID".equals(codeString))
      return V3AddressPartType.UNID;
    if ("UNIT".equals(codeString))
      return V3AddressPartType.UNIT;
    if ("ZIP".equals(codeString))
      return V3AddressPartType.ZIP;
    throw new IllegalArgumentException("Unknown V3AddressPartType code '"+codeString+"'");
  }

  public String toCode(V3AddressPartType code) {
    if (code == V3AddressPartType.ADL)
      return "ADL";
    if (code == V3AddressPartType.AL)
      return "AL";
    if (code == V3AddressPartType.DAL)
      return "DAL";
    if (code == V3AddressPartType.SAL)
      return "SAL";
    if (code == V3AddressPartType.BNN)
      return "BNN";
    if (code == V3AddressPartType.BNR)
      return "BNR";
    if (code == V3AddressPartType.BNS)
      return "BNS";
    if (code == V3AddressPartType.CAR)
      return "CAR";
    if (code == V3AddressPartType.CEN)
      return "CEN";
    if (code == V3AddressPartType.CNT)
      return "CNT";
    if (code == V3AddressPartType.CPA)
      return "CPA";
    if (code == V3AddressPartType.CTY)
      return "CTY";
    if (code == V3AddressPartType.DEL)
      return "DEL";
    if (code == V3AddressPartType.DINST)
      return "DINST";
    if (code == V3AddressPartType.DINSTA)
      return "DINSTA";
    if (code == V3AddressPartType.DINSTQ)
      return "DINSTQ";
    if (code == V3AddressPartType.DIR)
      return "DIR";
    if (code == V3AddressPartType.DMOD)
      return "DMOD";
    if (code == V3AddressPartType.DMODID)
      return "DMODID";
    if (code == V3AddressPartType.DPID)
      return "DPID";
    if (code == V3AddressPartType.INT)
      return "INT";
    if (code == V3AddressPartType.POB)
      return "POB";
    if (code == V3AddressPartType.PRE)
      return "PRE";
    if (code == V3AddressPartType.STA)
      return "STA";
    if (code == V3AddressPartType.STB)
      return "STB";
    if (code == V3AddressPartType.STR)
      return "STR";
    if (code == V3AddressPartType.STTYP)
      return "STTYP";
    if (code == V3AddressPartType.UNID)
      return "UNID";
    if (code == V3AddressPartType.UNIT)
      return "UNIT";
    if (code == V3AddressPartType.ZIP)
      return "ZIP";
    return "?";
  }

    public String toSystem(V3AddressPartType code) {
      return code.getSystem();
      }

}

