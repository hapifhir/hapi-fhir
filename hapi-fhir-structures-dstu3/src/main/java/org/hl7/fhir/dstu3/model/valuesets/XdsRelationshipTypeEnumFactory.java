package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class XdsRelationshipTypeEnumFactory implements EnumFactory<XdsRelationshipType> {

  public XdsRelationshipType fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("APND".equals(codeString))
      return XdsRelationshipType.APND;
    if ("RPLC".equals(codeString))
      return XdsRelationshipType.RPLC;
    if ("XFRM".equals(codeString))
      return XdsRelationshipType.XFRM;
    if ("XFRM_RPLC".equals(codeString))
      return XdsRelationshipType.XFRMRPLC;
    if ("signs".equals(codeString))
      return XdsRelationshipType.SIGNS;
    throw new IllegalArgumentException("Unknown XdsRelationshipType code '"+codeString+"'");
  }

  public String toCode(XdsRelationshipType code) {
    if (code == XdsRelationshipType.APND)
      return "APND";
    if (code == XdsRelationshipType.RPLC)
      return "RPLC";
    if (code == XdsRelationshipType.XFRM)
      return "XFRM";
    if (code == XdsRelationshipType.XFRMRPLC)
      return "XFRM_RPLC";
    if (code == XdsRelationshipType.SIGNS)
      return "signs";
    return "?";
  }

    public String toSystem(XdsRelationshipType code) {
      return code.getSystem();
      }

}

