package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class V3RoleLinkTypeEnumFactory implements EnumFactory<V3RoleLinkType> {

  public V3RoleLinkType fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("REL".equals(codeString))
      return V3RoleLinkType.REL;
    if ("BACKUP".equals(codeString))
      return V3RoleLinkType.BACKUP;
    if ("CONT".equals(codeString))
      return V3RoleLinkType.CONT;
    if ("DIRAUTH".equals(codeString))
      return V3RoleLinkType.DIRAUTH;
    if ("IDENT".equals(codeString))
      return V3RoleLinkType.IDENT;
    if ("INDAUTH".equals(codeString))
      return V3RoleLinkType.INDAUTH;
    if ("PART".equals(codeString))
      return V3RoleLinkType.PART;
    if ("REPL".equals(codeString))
      return V3RoleLinkType.REPL;
    throw new IllegalArgumentException("Unknown V3RoleLinkType code '"+codeString+"'");
  }

  public String toCode(V3RoleLinkType code) {
    if (code == V3RoleLinkType.REL)
      return "REL";
    if (code == V3RoleLinkType.BACKUP)
      return "BACKUP";
    if (code == V3RoleLinkType.CONT)
      return "CONT";
    if (code == V3RoleLinkType.DIRAUTH)
      return "DIRAUTH";
    if (code == V3RoleLinkType.IDENT)
      return "IDENT";
    if (code == V3RoleLinkType.INDAUTH)
      return "INDAUTH";
    if (code == V3RoleLinkType.PART)
      return "PART";
    if (code == V3RoleLinkType.REPL)
      return "REPL";
    return "?";
  }

    public String toSystem(V3RoleLinkType code) {
      return code.getSystem();
      }

}

