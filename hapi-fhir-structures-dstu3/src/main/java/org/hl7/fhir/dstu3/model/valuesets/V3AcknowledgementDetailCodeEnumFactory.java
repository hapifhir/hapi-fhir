package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class V3AcknowledgementDetailCodeEnumFactory implements EnumFactory<V3AcknowledgementDetailCode> {

  public V3AcknowledgementDetailCode fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("_AcknowledgementDetailNotSupportedCode".equals(codeString))
      return V3AcknowledgementDetailCode._ACKNOWLEDGEMENTDETAILNOTSUPPORTEDCODE;
    if ("NS200".equals(codeString))
      return V3AcknowledgementDetailCode.NS200;
    if ("NS202".equals(codeString))
      return V3AcknowledgementDetailCode.NS202;
    if ("NS203".equals(codeString))
      return V3AcknowledgementDetailCode.NS203;
    if ("NS250".equals(codeString))
      return V3AcknowledgementDetailCode.NS250;
    if ("NS260".equals(codeString))
      return V3AcknowledgementDetailCode.NS260;
    if ("NS261".equals(codeString))
      return V3AcknowledgementDetailCode.NS261;
    if ("INTERR".equals(codeString))
      return V3AcknowledgementDetailCode.INTERR;
    if ("NOSTORE".equals(codeString))
      return V3AcknowledgementDetailCode.NOSTORE;
    if ("RTEDEST".equals(codeString))
      return V3AcknowledgementDetailCode.RTEDEST;
    if ("RTUDEST".equals(codeString))
      return V3AcknowledgementDetailCode.RTUDEST;
    if ("RTWDEST".equals(codeString))
      return V3AcknowledgementDetailCode.RTWDEST;
    if ("SYN".equals(codeString))
      return V3AcknowledgementDetailCode.SYN;
    if ("SYN102".equals(codeString))
      return V3AcknowledgementDetailCode.SYN102;
    if ("SYN105".equals(codeString))
      return V3AcknowledgementDetailCode.SYN105;
    if ("SYN100".equals(codeString))
      return V3AcknowledgementDetailCode.SYN100;
    if ("SYN101".equals(codeString))
      return V3AcknowledgementDetailCode.SYN101;
    if ("SYN114".equals(codeString))
      return V3AcknowledgementDetailCode.SYN114;
    if ("SYN106".equals(codeString))
      return V3AcknowledgementDetailCode.SYN106;
    if ("SYN103".equals(codeString))
      return V3AcknowledgementDetailCode.SYN103;
    if ("SYN104".equals(codeString))
      return V3AcknowledgementDetailCode.SYN104;
    if ("SYN107".equals(codeString))
      return V3AcknowledgementDetailCode.SYN107;
    if ("SYN108".equals(codeString))
      return V3AcknowledgementDetailCode.SYN108;
    if ("SYN110".equals(codeString))
      return V3AcknowledgementDetailCode.SYN110;
    if ("SYN112".equals(codeString))
      return V3AcknowledgementDetailCode.SYN112;
    if ("SYN109".equals(codeString))
      return V3AcknowledgementDetailCode.SYN109;
    if ("SYN111".equals(codeString))
      return V3AcknowledgementDetailCode.SYN111;
    if ("SYN113".equals(codeString))
      return V3AcknowledgementDetailCode.SYN113;
    throw new IllegalArgumentException("Unknown V3AcknowledgementDetailCode code '"+codeString+"'");
  }

  public String toCode(V3AcknowledgementDetailCode code) {
    if (code == V3AcknowledgementDetailCode._ACKNOWLEDGEMENTDETAILNOTSUPPORTEDCODE)
      return "_AcknowledgementDetailNotSupportedCode";
    if (code == V3AcknowledgementDetailCode.NS200)
      return "NS200";
    if (code == V3AcknowledgementDetailCode.NS202)
      return "NS202";
    if (code == V3AcknowledgementDetailCode.NS203)
      return "NS203";
    if (code == V3AcknowledgementDetailCode.NS250)
      return "NS250";
    if (code == V3AcknowledgementDetailCode.NS260)
      return "NS260";
    if (code == V3AcknowledgementDetailCode.NS261)
      return "NS261";
    if (code == V3AcknowledgementDetailCode.INTERR)
      return "INTERR";
    if (code == V3AcknowledgementDetailCode.NOSTORE)
      return "NOSTORE";
    if (code == V3AcknowledgementDetailCode.RTEDEST)
      return "RTEDEST";
    if (code == V3AcknowledgementDetailCode.RTUDEST)
      return "RTUDEST";
    if (code == V3AcknowledgementDetailCode.RTWDEST)
      return "RTWDEST";
    if (code == V3AcknowledgementDetailCode.SYN)
      return "SYN";
    if (code == V3AcknowledgementDetailCode.SYN102)
      return "SYN102";
    if (code == V3AcknowledgementDetailCode.SYN105)
      return "SYN105";
    if (code == V3AcknowledgementDetailCode.SYN100)
      return "SYN100";
    if (code == V3AcknowledgementDetailCode.SYN101)
      return "SYN101";
    if (code == V3AcknowledgementDetailCode.SYN114)
      return "SYN114";
    if (code == V3AcknowledgementDetailCode.SYN106)
      return "SYN106";
    if (code == V3AcknowledgementDetailCode.SYN103)
      return "SYN103";
    if (code == V3AcknowledgementDetailCode.SYN104)
      return "SYN104";
    if (code == V3AcknowledgementDetailCode.SYN107)
      return "SYN107";
    if (code == V3AcknowledgementDetailCode.SYN108)
      return "SYN108";
    if (code == V3AcknowledgementDetailCode.SYN110)
      return "SYN110";
    if (code == V3AcknowledgementDetailCode.SYN112)
      return "SYN112";
    if (code == V3AcknowledgementDetailCode.SYN109)
      return "SYN109";
    if (code == V3AcknowledgementDetailCode.SYN111)
      return "SYN111";
    if (code == V3AcknowledgementDetailCode.SYN113)
      return "SYN113";
    return "?";
  }

    public String toSystem(V3AcknowledgementDetailCode code) {
      return code.getSystem();
      }

}

