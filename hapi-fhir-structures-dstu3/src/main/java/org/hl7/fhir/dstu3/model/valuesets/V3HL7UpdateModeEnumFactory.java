package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class V3HL7UpdateModeEnumFactory implements EnumFactory<V3HL7UpdateMode> {

  public V3HL7UpdateMode fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("A".equals(codeString))
      return V3HL7UpdateMode.A;
    if ("AR".equals(codeString))
      return V3HL7UpdateMode.AR;
    if ("D".equals(codeString))
      return V3HL7UpdateMode.D;
    if ("K".equals(codeString))
      return V3HL7UpdateMode.K;
    if ("N".equals(codeString))
      return V3HL7UpdateMode.N;
    if ("R".equals(codeString))
      return V3HL7UpdateMode.R;
    if ("REF".equals(codeString))
      return V3HL7UpdateMode.REF;
    if ("U".equals(codeString))
      return V3HL7UpdateMode.U;
    if ("_SetUpdateMode".equals(codeString))
      return V3HL7UpdateMode._SETUPDATEMODE;
    if ("ESA".equals(codeString))
      return V3HL7UpdateMode.ESA;
    if ("ESAC".equals(codeString))
      return V3HL7UpdateMode.ESAC;
    if ("ESC".equals(codeString))
      return V3HL7UpdateMode.ESC;
    if ("ESD".equals(codeString))
      return V3HL7UpdateMode.ESD;
    if ("AU".equals(codeString))
      return V3HL7UpdateMode.AU;
    if ("I".equals(codeString))
      return V3HL7UpdateMode.I;
    if ("V".equals(codeString))
      return V3HL7UpdateMode.V;
    throw new IllegalArgumentException("Unknown V3HL7UpdateMode code '"+codeString+"'");
  }

  public String toCode(V3HL7UpdateMode code) {
    if (code == V3HL7UpdateMode.A)
      return "A";
    if (code == V3HL7UpdateMode.AR)
      return "AR";
    if (code == V3HL7UpdateMode.D)
      return "D";
    if (code == V3HL7UpdateMode.K)
      return "K";
    if (code == V3HL7UpdateMode.N)
      return "N";
    if (code == V3HL7UpdateMode.R)
      return "R";
    if (code == V3HL7UpdateMode.REF)
      return "REF";
    if (code == V3HL7UpdateMode.U)
      return "U";
    if (code == V3HL7UpdateMode._SETUPDATEMODE)
      return "_SetUpdateMode";
    if (code == V3HL7UpdateMode.ESA)
      return "ESA";
    if (code == V3HL7UpdateMode.ESAC)
      return "ESAC";
    if (code == V3HL7UpdateMode.ESC)
      return "ESC";
    if (code == V3HL7UpdateMode.ESD)
      return "ESD";
    if (code == V3HL7UpdateMode.AU)
      return "AU";
    if (code == V3HL7UpdateMode.I)
      return "I";
    if (code == V3HL7UpdateMode.V)
      return "V";
    return "?";
  }

    public String toSystem(V3HL7UpdateMode code) {
      return code.getSystem();
      }

}

