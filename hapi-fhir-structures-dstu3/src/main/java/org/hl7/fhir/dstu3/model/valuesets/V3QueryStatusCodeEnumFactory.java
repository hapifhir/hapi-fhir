package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class V3QueryStatusCodeEnumFactory implements EnumFactory<V3QueryStatusCode> {

  public V3QueryStatusCode fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("aborted".equals(codeString))
      return V3QueryStatusCode.ABORTED;
    if ("deliveredResponse".equals(codeString))
      return V3QueryStatusCode.DELIVEREDRESPONSE;
    if ("executing".equals(codeString))
      return V3QueryStatusCode.EXECUTING;
    if ("new".equals(codeString))
      return V3QueryStatusCode.NEW;
    if ("waitContinuedQueryResponse".equals(codeString))
      return V3QueryStatusCode.WAITCONTINUEDQUERYRESPONSE;
    throw new IllegalArgumentException("Unknown V3QueryStatusCode code '"+codeString+"'");
  }

  public String toCode(V3QueryStatusCode code) {
    if (code == V3QueryStatusCode.ABORTED)
      return "aborted";
    if (code == V3QueryStatusCode.DELIVEREDRESPONSE)
      return "deliveredResponse";
    if (code == V3QueryStatusCode.EXECUTING)
      return "executing";
    if (code == V3QueryStatusCode.NEW)
      return "new";
    if (code == V3QueryStatusCode.WAITCONTINUEDQUERYRESPONSE)
      return "waitContinuedQueryResponse";
    return "?";
  }

    public String toSystem(V3QueryStatusCode code) {
      return code.getSystem();
      }

}

