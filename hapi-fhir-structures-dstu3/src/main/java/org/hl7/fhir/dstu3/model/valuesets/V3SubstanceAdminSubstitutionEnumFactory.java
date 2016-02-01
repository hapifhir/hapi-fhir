package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class V3SubstanceAdminSubstitutionEnumFactory implements EnumFactory<V3SubstanceAdminSubstitution> {

  public V3SubstanceAdminSubstitution fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("_ActSubstanceAdminSubstitutionCode".equals(codeString))
      return V3SubstanceAdminSubstitution._ACTSUBSTANCEADMINSUBSTITUTIONCODE;
    if ("E".equals(codeString))
      return V3SubstanceAdminSubstitution.E;
    if ("EC".equals(codeString))
      return V3SubstanceAdminSubstitution.EC;
    if ("BC".equals(codeString))
      return V3SubstanceAdminSubstitution.BC;
    if ("G".equals(codeString))
      return V3SubstanceAdminSubstitution.G;
    if ("TE".equals(codeString))
      return V3SubstanceAdminSubstitution.TE;
    if ("TB".equals(codeString))
      return V3SubstanceAdminSubstitution.TB;
    if ("TG".equals(codeString))
      return V3SubstanceAdminSubstitution.TG;
    if ("F".equals(codeString))
      return V3SubstanceAdminSubstitution.F;
    if ("N".equals(codeString))
      return V3SubstanceAdminSubstitution.N;
    throw new IllegalArgumentException("Unknown V3SubstanceAdminSubstitution code '"+codeString+"'");
  }

  public String toCode(V3SubstanceAdminSubstitution code) {
    if (code == V3SubstanceAdminSubstitution._ACTSUBSTANCEADMINSUBSTITUTIONCODE)
      return "_ActSubstanceAdminSubstitutionCode";
    if (code == V3SubstanceAdminSubstitution.E)
      return "E";
    if (code == V3SubstanceAdminSubstitution.EC)
      return "EC";
    if (code == V3SubstanceAdminSubstitution.BC)
      return "BC";
    if (code == V3SubstanceAdminSubstitution.G)
      return "G";
    if (code == V3SubstanceAdminSubstitution.TE)
      return "TE";
    if (code == V3SubstanceAdminSubstitution.TB)
      return "TB";
    if (code == V3SubstanceAdminSubstitution.TG)
      return "TG";
    if (code == V3SubstanceAdminSubstitution.F)
      return "F";
    if (code == V3SubstanceAdminSubstitution.N)
      return "N";
    return "?";
  }

    public String toSystem(V3SubstanceAdminSubstitution code) {
      return code.getSystem();
      }

}

