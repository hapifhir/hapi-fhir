package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class V3NullFlavorEnumFactory implements EnumFactory<V3NullFlavor> {

  public V3NullFlavor fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("NI".equals(codeString))
      return V3NullFlavor.NI;
    if ("INV".equals(codeString))
      return V3NullFlavor.INV;
    if ("DER".equals(codeString))
      return V3NullFlavor.DER;
    if ("OTH".equals(codeString))
      return V3NullFlavor.OTH;
    if ("NINF".equals(codeString))
      return V3NullFlavor.NINF;
    if ("PINF".equals(codeString))
      return V3NullFlavor.PINF;
    if ("UNC".equals(codeString))
      return V3NullFlavor.UNC;
    if ("MSK".equals(codeString))
      return V3NullFlavor.MSK;
    if ("NA".equals(codeString))
      return V3NullFlavor.NA;
    if ("UNK".equals(codeString))
      return V3NullFlavor.UNK;
    if ("ASKU".equals(codeString))
      return V3NullFlavor.ASKU;
    if ("NAV".equals(codeString))
      return V3NullFlavor.NAV;
    if ("NASK".equals(codeString))
      return V3NullFlavor.NASK;
    if ("NAVU".equals(codeString))
      return V3NullFlavor.NAVU;
    if ("QS".equals(codeString))
      return V3NullFlavor.QS;
    if ("TRC".equals(codeString))
      return V3NullFlavor.TRC;
    if ("NP".equals(codeString))
      return V3NullFlavor.NP;
    throw new IllegalArgumentException("Unknown V3NullFlavor code '"+codeString+"'");
  }

  public String toCode(V3NullFlavor code) {
    if (code == V3NullFlavor.NI)
      return "NI";
    if (code == V3NullFlavor.INV)
      return "INV";
    if (code == V3NullFlavor.DER)
      return "DER";
    if (code == V3NullFlavor.OTH)
      return "OTH";
    if (code == V3NullFlavor.NINF)
      return "NINF";
    if (code == V3NullFlavor.PINF)
      return "PINF";
    if (code == V3NullFlavor.UNC)
      return "UNC";
    if (code == V3NullFlavor.MSK)
      return "MSK";
    if (code == V3NullFlavor.NA)
      return "NA";
    if (code == V3NullFlavor.UNK)
      return "UNK";
    if (code == V3NullFlavor.ASKU)
      return "ASKU";
    if (code == V3NullFlavor.NAV)
      return "NAV";
    if (code == V3NullFlavor.NASK)
      return "NASK";
    if (code == V3NullFlavor.NAVU)
      return "NAVU";
    if (code == V3NullFlavor.QS)
      return "QS";
    if (code == V3NullFlavor.TRC)
      return "TRC";
    if (code == V3NullFlavor.NP)
      return "NP";
    return "?";
  }

    public String toSystem(V3NullFlavor code) {
      return code.getSystem();
      }

}

