package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class V3QueryParameterValueEnumFactory implements EnumFactory<V3QueryParameterValue> {

  public V3QueryParameterValue fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("_DispenseQueryFilterCode".equals(codeString))
      return V3QueryParameterValue._DISPENSEQUERYFILTERCODE;
    if ("ALLDISP".equals(codeString))
      return V3QueryParameterValue.ALLDISP;
    if ("LASTDISP".equals(codeString))
      return V3QueryParameterValue.LASTDISP;
    if ("NODISP".equals(codeString))
      return V3QueryParameterValue.NODISP;
    if ("_OrderFilterCode".equals(codeString))
      return V3QueryParameterValue._ORDERFILTERCODE;
    if ("AO".equals(codeString))
      return V3QueryParameterValue.AO;
    if ("ONR".equals(codeString))
      return V3QueryParameterValue.ONR;
    if ("OWR".equals(codeString))
      return V3QueryParameterValue.OWR;
    if ("_PrescriptionDispenseFilterCode".equals(codeString))
      return V3QueryParameterValue._PRESCRIPTIONDISPENSEFILTERCODE;
    if ("C".equals(codeString))
      return V3QueryParameterValue.C;
    if ("N".equals(codeString))
      return V3QueryParameterValue.N;
    if ("R".equals(codeString))
      return V3QueryParameterValue.R;
    if ("_QueryParameterValue".equals(codeString))
      return V3QueryParameterValue._QUERYPARAMETERVALUE;
    if ("ISSFA".equals(codeString))
      return V3QueryParameterValue.ISSFA;
    if ("ISSFI".equals(codeString))
      return V3QueryParameterValue.ISSFI;
    if ("ISSFU".equals(codeString))
      return V3QueryParameterValue.ISSFU;
    throw new IllegalArgumentException("Unknown V3QueryParameterValue code '"+codeString+"'");
  }

  public String toCode(V3QueryParameterValue code) {
    if (code == V3QueryParameterValue._DISPENSEQUERYFILTERCODE)
      return "_DispenseQueryFilterCode";
    if (code == V3QueryParameterValue.ALLDISP)
      return "ALLDISP";
    if (code == V3QueryParameterValue.LASTDISP)
      return "LASTDISP";
    if (code == V3QueryParameterValue.NODISP)
      return "NODISP";
    if (code == V3QueryParameterValue._ORDERFILTERCODE)
      return "_OrderFilterCode";
    if (code == V3QueryParameterValue.AO)
      return "AO";
    if (code == V3QueryParameterValue.ONR)
      return "ONR";
    if (code == V3QueryParameterValue.OWR)
      return "OWR";
    if (code == V3QueryParameterValue._PRESCRIPTIONDISPENSEFILTERCODE)
      return "_PrescriptionDispenseFilterCode";
    if (code == V3QueryParameterValue.C)
      return "C";
    if (code == V3QueryParameterValue.N)
      return "N";
    if (code == V3QueryParameterValue.R)
      return "R";
    if (code == V3QueryParameterValue._QUERYPARAMETERVALUE)
      return "_QueryParameterValue";
    if (code == V3QueryParameterValue.ISSFA)
      return "ISSFA";
    if (code == V3QueryParameterValue.ISSFI)
      return "ISSFI";
    if (code == V3QueryParameterValue.ISSFU)
      return "ISSFU";
    return "?";
  }

    public String toSystem(V3QueryParameterValue code) {
      return code.getSystem();
      }

}

