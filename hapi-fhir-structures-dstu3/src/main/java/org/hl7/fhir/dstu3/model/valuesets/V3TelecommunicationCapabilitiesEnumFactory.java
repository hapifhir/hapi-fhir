package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class V3TelecommunicationCapabilitiesEnumFactory implements EnumFactory<V3TelecommunicationCapabilities> {

  public V3TelecommunicationCapabilities fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("data".equals(codeString))
      return V3TelecommunicationCapabilities.DATA;
    if ("fax".equals(codeString))
      return V3TelecommunicationCapabilities.FAX;
    if ("sms".equals(codeString))
      return V3TelecommunicationCapabilities.SMS;
    if ("tty".equals(codeString))
      return V3TelecommunicationCapabilities.TTY;
    if ("voice".equals(codeString))
      return V3TelecommunicationCapabilities.VOICE;
    throw new IllegalArgumentException("Unknown V3TelecommunicationCapabilities code '"+codeString+"'");
  }

  public String toCode(V3TelecommunicationCapabilities code) {
    if (code == V3TelecommunicationCapabilities.DATA)
      return "data";
    if (code == V3TelecommunicationCapabilities.FAX)
      return "fax";
    if (code == V3TelecommunicationCapabilities.SMS)
      return "sms";
    if (code == V3TelecommunicationCapabilities.TTY)
      return "tty";
    if (code == V3TelecommunicationCapabilities.VOICE)
      return "voice";
    return "?";
  }

    public String toSystem(V3TelecommunicationCapabilities code) {
      return code.getSystem();
      }

}

