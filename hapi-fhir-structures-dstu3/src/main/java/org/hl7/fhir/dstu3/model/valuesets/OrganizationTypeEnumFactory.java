package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class OrganizationTypeEnumFactory implements EnumFactory<OrganizationType> {

  public OrganizationType fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("prov".equals(codeString))
      return OrganizationType.PROV;
    if ("dept".equals(codeString))
      return OrganizationType.DEPT;
    if ("team".equals(codeString))
      return OrganizationType.TEAM;
    if ("govt".equals(codeString))
      return OrganizationType.GOVT;
    if ("ins".equals(codeString))
      return OrganizationType.INS;
    if ("edu".equals(codeString))
      return OrganizationType.EDU;
    if ("reli".equals(codeString))
      return OrganizationType.RELI;
    if ("crs".equals(codeString))
      return OrganizationType.CRS;
    if ("cg".equals(codeString))
      return OrganizationType.CG;
    if ("bus".equals(codeString))
      return OrganizationType.BUS;
    if ("other".equals(codeString))
      return OrganizationType.OTHER;
    throw new IllegalArgumentException("Unknown OrganizationType code '"+codeString+"'");
  }

  public String toCode(OrganizationType code) {
    if (code == OrganizationType.PROV)
      return "prov";
    if (code == OrganizationType.DEPT)
      return "dept";
    if (code == OrganizationType.TEAM)
      return "team";
    if (code == OrganizationType.GOVT)
      return "govt";
    if (code == OrganizationType.INS)
      return "ins";
    if (code == OrganizationType.EDU)
      return "edu";
    if (code == OrganizationType.RELI)
      return "reli";
    if (code == OrganizationType.CRS)
      return "crs";
    if (code == OrganizationType.CG)
      return "cg";
    if (code == OrganizationType.BUS)
      return "bus";
    if (code == OrganizationType.OTHER)
      return "other";
    return "?";
  }

    public String toSystem(OrganizationType code) {
      return code.getSystem();
      }

}

