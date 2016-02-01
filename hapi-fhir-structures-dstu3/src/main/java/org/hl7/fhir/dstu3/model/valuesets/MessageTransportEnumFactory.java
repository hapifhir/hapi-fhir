package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class MessageTransportEnumFactory implements EnumFactory<MessageTransport> {

  public MessageTransport fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("http".equals(codeString))
      return MessageTransport.HTTP;
    if ("ftp".equals(codeString))
      return MessageTransport.FTP;
    if ("mllp".equals(codeString))
      return MessageTransport.MLLP;
    throw new IllegalArgumentException("Unknown MessageTransport code '"+codeString+"'");
  }

  public String toCode(MessageTransport code) {
    if (code == MessageTransport.HTTP)
      return "http";
    if (code == MessageTransport.FTP)
      return "ftp";
    if (code == MessageTransport.MLLP)
      return "mllp";
    return "?";
  }

    public String toSystem(MessageTransport code) {
      return code.getSystem();
      }

}

