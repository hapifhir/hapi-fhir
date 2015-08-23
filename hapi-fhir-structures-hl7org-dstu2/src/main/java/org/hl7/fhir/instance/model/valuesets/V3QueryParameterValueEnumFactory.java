package org.hl7.fhir.instance.model.valuesets;

/*
  Copyright (c) 2011+, HL7, Inc.
  All rights reserved.
  
  Redistribution and use in source and binary forms, with or without modification, 
  are permitted provided that the following conditions are met:
  
   * Redistributions of source code must retain the above copyright notice, this 
     list of conditions and the following disclaimer.
   * Redistributions in binary form must reproduce the above copyright notice, 
     this list of conditions and the following disclaimer in the documentation 
     and/or other materials provided with the distribution.
   * Neither the name of HL7 nor the names of its contributors may be used to 
     endorse or promote products derived from this software without specific 
     prior written permission.
  
  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND 
  ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
  WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. 
  IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, 
  INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT 
  NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR 
  PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, 
  WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
  ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
  POSSIBILITY OF SUCH DAMAGE.
  
*/

// Generated on Sat, Aug 22, 2015 23:00-0400 for FHIR v0.5.0


import org.hl7.fhir.instance.model.EnumFactory;

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


}

