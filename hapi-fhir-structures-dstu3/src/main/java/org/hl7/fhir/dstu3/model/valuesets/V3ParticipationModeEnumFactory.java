package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class V3ParticipationModeEnumFactory implements EnumFactory<V3ParticipationMode> {

  public V3ParticipationMode fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("ELECTRONIC".equals(codeString))
      return V3ParticipationMode.ELECTRONIC;
    if ("PHYSICAL".equals(codeString))
      return V3ParticipationMode.PHYSICAL;
    if ("REMOTE".equals(codeString))
      return V3ParticipationMode.REMOTE;
    if ("VERBAL".equals(codeString))
      return V3ParticipationMode.VERBAL;
    if ("DICTATE".equals(codeString))
      return V3ParticipationMode.DICTATE;
    if ("FACE".equals(codeString))
      return V3ParticipationMode.FACE;
    if ("PHONE".equals(codeString))
      return V3ParticipationMode.PHONE;
    if ("VIDEOCONF".equals(codeString))
      return V3ParticipationMode.VIDEOCONF;
    if ("WRITTEN".equals(codeString))
      return V3ParticipationMode.WRITTEN;
    if ("FAXWRIT".equals(codeString))
      return V3ParticipationMode.FAXWRIT;
    if ("HANDWRIT".equals(codeString))
      return V3ParticipationMode.HANDWRIT;
    if ("MAILWRIT".equals(codeString))
      return V3ParticipationMode.MAILWRIT;
    if ("ONLINEWRIT".equals(codeString))
      return V3ParticipationMode.ONLINEWRIT;
    if ("EMAILWRIT".equals(codeString))
      return V3ParticipationMode.EMAILWRIT;
    if ("TYPEWRIT".equals(codeString))
      return V3ParticipationMode.TYPEWRIT;
    throw new IllegalArgumentException("Unknown V3ParticipationMode code '"+codeString+"'");
  }

  public String toCode(V3ParticipationMode code) {
    if (code == V3ParticipationMode.ELECTRONIC)
      return "ELECTRONIC";
    if (code == V3ParticipationMode.PHYSICAL)
      return "PHYSICAL";
    if (code == V3ParticipationMode.REMOTE)
      return "REMOTE";
    if (code == V3ParticipationMode.VERBAL)
      return "VERBAL";
    if (code == V3ParticipationMode.DICTATE)
      return "DICTATE";
    if (code == V3ParticipationMode.FACE)
      return "FACE";
    if (code == V3ParticipationMode.PHONE)
      return "PHONE";
    if (code == V3ParticipationMode.VIDEOCONF)
      return "VIDEOCONF";
    if (code == V3ParticipationMode.WRITTEN)
      return "WRITTEN";
    if (code == V3ParticipationMode.FAXWRIT)
      return "FAXWRIT";
    if (code == V3ParticipationMode.HANDWRIT)
      return "HANDWRIT";
    if (code == V3ParticipationMode.MAILWRIT)
      return "MAILWRIT";
    if (code == V3ParticipationMode.ONLINEWRIT)
      return "ONLINEWRIT";
    if (code == V3ParticipationMode.EMAILWRIT)
      return "EMAILWRIT";
    if (code == V3ParticipationMode.TYPEWRIT)
      return "TYPEWRIT";
    return "?";
  }

    public String toSystem(V3ParticipationMode code) {
      return code.getSystem();
      }

}

