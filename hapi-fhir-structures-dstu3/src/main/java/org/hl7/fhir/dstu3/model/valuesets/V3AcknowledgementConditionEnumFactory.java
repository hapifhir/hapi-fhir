package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class V3AcknowledgementConditionEnumFactory implements EnumFactory<V3AcknowledgementCondition> {

  public V3AcknowledgementCondition fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("AL".equals(codeString))
      return V3AcknowledgementCondition.AL;
    if ("ER".equals(codeString))
      return V3AcknowledgementCondition.ER;
    if ("NE".equals(codeString))
      return V3AcknowledgementCondition.NE;
    if ("SU".equals(codeString))
      return V3AcknowledgementCondition.SU;
    throw new IllegalArgumentException("Unknown V3AcknowledgementCondition code '"+codeString+"'");
  }

  public String toCode(V3AcknowledgementCondition code) {
    if (code == V3AcknowledgementCondition.AL)
      return "AL";
    if (code == V3AcknowledgementCondition.ER)
      return "ER";
    if (code == V3AcknowledgementCondition.NE)
      return "NE";
    if (code == V3AcknowledgementCondition.SU)
      return "SU";
    return "?";
  }

    public String toSystem(V3AcknowledgementCondition code) {
      return code.getSystem();
      }

}

