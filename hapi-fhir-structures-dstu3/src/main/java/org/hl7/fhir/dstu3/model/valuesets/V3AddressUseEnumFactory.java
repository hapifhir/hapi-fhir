package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class V3AddressUseEnumFactory implements EnumFactory<V3AddressUse> {

  public V3AddressUse fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("_GeneralAddressUse".equals(codeString))
      return V3AddressUse._GENERALADDRESSUSE;
    if ("BAD".equals(codeString))
      return V3AddressUse.BAD;
    if ("CONF".equals(codeString))
      return V3AddressUse.CONF;
    if ("H".equals(codeString))
      return V3AddressUse.H;
    if ("HP".equals(codeString))
      return V3AddressUse.HP;
    if ("HV".equals(codeString))
      return V3AddressUse.HV;
    if ("OLD".equals(codeString))
      return V3AddressUse.OLD;
    if ("TMP".equals(codeString))
      return V3AddressUse.TMP;
    if ("WP".equals(codeString))
      return V3AddressUse.WP;
    if ("DIR".equals(codeString))
      return V3AddressUse.DIR;
    if ("PUB".equals(codeString))
      return V3AddressUse.PUB;
    if ("_PostalAddressUse".equals(codeString))
      return V3AddressUse._POSTALADDRESSUSE;
    if ("PHYS".equals(codeString))
      return V3AddressUse.PHYS;
    if ("PST".equals(codeString))
      return V3AddressUse.PST;
    if ("_TelecommunicationAddressUse".equals(codeString))
      return V3AddressUse._TELECOMMUNICATIONADDRESSUSE;
    if ("AS".equals(codeString))
      return V3AddressUse.AS;
    if ("EC".equals(codeString))
      return V3AddressUse.EC;
    if ("MC".equals(codeString))
      return V3AddressUse.MC;
    if ("PG".equals(codeString))
      return V3AddressUse.PG;
    throw new IllegalArgumentException("Unknown V3AddressUse code '"+codeString+"'");
  }

  public String toCode(V3AddressUse code) {
    if (code == V3AddressUse._GENERALADDRESSUSE)
      return "_GeneralAddressUse";
    if (code == V3AddressUse.BAD)
      return "BAD";
    if (code == V3AddressUse.CONF)
      return "CONF";
    if (code == V3AddressUse.H)
      return "H";
    if (code == V3AddressUse.HP)
      return "HP";
    if (code == V3AddressUse.HV)
      return "HV";
    if (code == V3AddressUse.OLD)
      return "OLD";
    if (code == V3AddressUse.TMP)
      return "TMP";
    if (code == V3AddressUse.WP)
      return "WP";
    if (code == V3AddressUse.DIR)
      return "DIR";
    if (code == V3AddressUse.PUB)
      return "PUB";
    if (code == V3AddressUse._POSTALADDRESSUSE)
      return "_PostalAddressUse";
    if (code == V3AddressUse.PHYS)
      return "PHYS";
    if (code == V3AddressUse.PST)
      return "PST";
    if (code == V3AddressUse._TELECOMMUNICATIONADDRESSUSE)
      return "_TelecommunicationAddressUse";
    if (code == V3AddressUse.AS)
      return "AS";
    if (code == V3AddressUse.EC)
      return "EC";
    if (code == V3AddressUse.MC)
      return "MC";
    if (code == V3AddressUse.PG)
      return "PG";
    return "?";
  }

    public String toSystem(V3AddressUse code) {
      return code.getSystem();
      }

}

