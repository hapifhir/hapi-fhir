package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class ServiceUsclsEnumFactory implements EnumFactory<ServiceUscls> {

  public ServiceUscls fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("1101".equals(codeString))
      return ServiceUscls._1101;
    if ("1102".equals(codeString))
      return ServiceUscls._1102;
    if ("1103".equals(codeString))
      return ServiceUscls._1103;
    if ("1201".equals(codeString))
      return ServiceUscls._1201;
    if ("1205".equals(codeString))
      return ServiceUscls._1205;
    if ("2101".equals(codeString))
      return ServiceUscls._2101;
    if ("2102".equals(codeString))
      return ServiceUscls._2102;
    if ("2141".equals(codeString))
      return ServiceUscls._2141;
    if ("2601".equals(codeString))
      return ServiceUscls._2601;
    if ("11101".equals(codeString))
      return ServiceUscls._11101;
    if ("11102".equals(codeString))
      return ServiceUscls._11102;
    if ("11103".equals(codeString))
      return ServiceUscls._11103;
    if ("11104".equals(codeString))
      return ServiceUscls._11104;
    if ("21211".equals(codeString))
      return ServiceUscls._21211;
    if ("21212".equals(codeString))
      return ServiceUscls._21212;
    if ("27211".equals(codeString))
      return ServiceUscls._27211;
    if ("99111".equals(codeString))
      return ServiceUscls._99111;
    if ("99333".equals(codeString))
      return ServiceUscls._99333;
    if ("99555".equals(codeString))
      return ServiceUscls._99555;
    throw new IllegalArgumentException("Unknown ServiceUscls code '"+codeString+"'");
  }

  public String toCode(ServiceUscls code) {
    if (code == ServiceUscls._1101)
      return "1101";
    if (code == ServiceUscls._1102)
      return "1102";
    if (code == ServiceUscls._1103)
      return "1103";
    if (code == ServiceUscls._1201)
      return "1201";
    if (code == ServiceUscls._1205)
      return "1205";
    if (code == ServiceUscls._2101)
      return "2101";
    if (code == ServiceUscls._2102)
      return "2102";
    if (code == ServiceUscls._2141)
      return "2141";
    if (code == ServiceUscls._2601)
      return "2601";
    if (code == ServiceUscls._11101)
      return "11101";
    if (code == ServiceUscls._11102)
      return "11102";
    if (code == ServiceUscls._11103)
      return "11103";
    if (code == ServiceUscls._11104)
      return "11104";
    if (code == ServiceUscls._21211)
      return "21211";
    if (code == ServiceUscls._21212)
      return "21212";
    if (code == ServiceUscls._27211)
      return "27211";
    if (code == ServiceUscls._99111)
      return "99111";
    if (code == ServiceUscls._99333)
      return "99333";
    if (code == ServiceUscls._99555)
      return "99555";
    return "?";
  }

    public String toSystem(ServiceUscls code) {
      return code.getSystem();
      }

}

