package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class V3EntityDeterminerEnumFactory implements EnumFactory<V3EntityDeterminer> {

  public V3EntityDeterminer fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("INSTANCE".equals(codeString))
      return V3EntityDeterminer.INSTANCE;
    if ("GROUP".equals(codeString))
      return V3EntityDeterminer.GROUP;
    if ("KIND".equals(codeString))
      return V3EntityDeterminer.KIND;
    if ("GROUPKIND".equals(codeString))
      return V3EntityDeterminer.GROUPKIND;
    if ("QUANTIFIED_KIND".equals(codeString))
      return V3EntityDeterminer.QUANTIFIEDKIND;
    throw new IllegalArgumentException("Unknown V3EntityDeterminer code '"+codeString+"'");
  }

  public String toCode(V3EntityDeterminer code) {
    if (code == V3EntityDeterminer.INSTANCE)
      return "INSTANCE";
    if (code == V3EntityDeterminer.GROUP)
      return "GROUP";
    if (code == V3EntityDeterminer.KIND)
      return "KIND";
    if (code == V3EntityDeterminer.GROUPKIND)
      return "GROUPKIND";
    if (code == V3EntityDeterminer.QUANTIFIEDKIND)
      return "QUANTIFIED_KIND";
    return "?";
  }

    public String toSystem(V3EntityDeterminer code) {
      return code.getSystem();
      }

}

