package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class BasicResourceTypeEnumFactory implements EnumFactory<BasicResourceType> {

  public BasicResourceType fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("consent".equals(codeString))
      return BasicResourceType.CONSENT;
    if ("referral".equals(codeString))
      return BasicResourceType.REFERRAL;
    if ("advevent".equals(codeString))
      return BasicResourceType.ADVEVENT;
    if ("aptmtreq".equals(codeString))
      return BasicResourceType.APTMTREQ;
    if ("transfer".equals(codeString))
      return BasicResourceType.TRANSFER;
    if ("diet".equals(codeString))
      return BasicResourceType.DIET;
    if ("adminact".equals(codeString))
      return BasicResourceType.ADMINACT;
    if ("exposure".equals(codeString))
      return BasicResourceType.EXPOSURE;
    if ("investigation".equals(codeString))
      return BasicResourceType.INVESTIGATION;
    if ("account".equals(codeString))
      return BasicResourceType.ACCOUNT;
    if ("invoice".equals(codeString))
      return BasicResourceType.INVOICE;
    if ("adjudicat".equals(codeString))
      return BasicResourceType.ADJUDICAT;
    if ("predetreq".equals(codeString))
      return BasicResourceType.PREDETREQ;
    if ("predetermine".equals(codeString))
      return BasicResourceType.PREDETERMINE;
    if ("study".equals(codeString))
      return BasicResourceType.STUDY;
    if ("protocol".equals(codeString))
      return BasicResourceType.PROTOCOL;
    throw new IllegalArgumentException("Unknown BasicResourceType code '"+codeString+"'");
  }

  public String toCode(BasicResourceType code) {
    if (code == BasicResourceType.CONSENT)
      return "consent";
    if (code == BasicResourceType.REFERRAL)
      return "referral";
    if (code == BasicResourceType.ADVEVENT)
      return "advevent";
    if (code == BasicResourceType.APTMTREQ)
      return "aptmtreq";
    if (code == BasicResourceType.TRANSFER)
      return "transfer";
    if (code == BasicResourceType.DIET)
      return "diet";
    if (code == BasicResourceType.ADMINACT)
      return "adminact";
    if (code == BasicResourceType.EXPOSURE)
      return "exposure";
    if (code == BasicResourceType.INVESTIGATION)
      return "investigation";
    if (code == BasicResourceType.ACCOUNT)
      return "account";
    if (code == BasicResourceType.INVOICE)
      return "invoice";
    if (code == BasicResourceType.ADJUDICAT)
      return "adjudicat";
    if (code == BasicResourceType.PREDETREQ)
      return "predetreq";
    if (code == BasicResourceType.PREDETERMINE)
      return "predetermine";
    if (code == BasicResourceType.STUDY)
      return "study";
    if (code == BasicResourceType.PROTOCOL)
      return "protocol";
    return "?";
  }

    public String toSystem(BasicResourceType code) {
      return code.getSystem();
      }

}

