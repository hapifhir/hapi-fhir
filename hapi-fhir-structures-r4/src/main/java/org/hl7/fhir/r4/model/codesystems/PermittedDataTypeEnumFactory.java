package org.hl7.fhir.r4.model.codesystems;

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

// Generated on Thu, Dec 27, 2018 10:06-0500 for FHIR v4.0.0


import org.hl7.fhir.r4.model.EnumFactory;

public class PermittedDataTypeEnumFactory implements EnumFactory<PermittedDataType> {

  public PermittedDataType fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("Quantity".equals(codeString))
      return PermittedDataType.QUANTITY;
    if ("CodeableConcept".equals(codeString))
      return PermittedDataType.CODEABLECONCEPT;
    if ("string".equals(codeString))
      return PermittedDataType.STRING;
    if ("boolean".equals(codeString))
      return PermittedDataType.BOOLEAN;
    if ("integer".equals(codeString))
      return PermittedDataType.INTEGER;
    if ("Range".equals(codeString))
      return PermittedDataType.RANGE;
    if ("Ratio".equals(codeString))
      return PermittedDataType.RATIO;
    if ("SampledData".equals(codeString))
      return PermittedDataType.SAMPLEDDATA;
    if ("time".equals(codeString))
      return PermittedDataType.TIME;
    if ("dateTime".equals(codeString))
      return PermittedDataType.DATETIME;
    if ("Period".equals(codeString))
      return PermittedDataType.PERIOD;
    throw new IllegalArgumentException("Unknown PermittedDataType code '"+codeString+"'");
  }

  public String toCode(PermittedDataType code) {
    if (code == PermittedDataType.QUANTITY)
      return "Quantity";
    if (code == PermittedDataType.CODEABLECONCEPT)
      return "CodeableConcept";
    if (code == PermittedDataType.STRING)
      return "string";
    if (code == PermittedDataType.BOOLEAN)
      return "boolean";
    if (code == PermittedDataType.INTEGER)
      return "integer";
    if (code == PermittedDataType.RANGE)
      return "Range";
    if (code == PermittedDataType.RATIO)
      return "Ratio";
    if (code == PermittedDataType.SAMPLEDDATA)
      return "SampledData";
    if (code == PermittedDataType.TIME)
      return "time";
    if (code == PermittedDataType.DATETIME)
      return "dateTime";
    if (code == PermittedDataType.PERIOD)
      return "Period";
    return "?";
  }

    public String toSystem(PermittedDataType code) {
      return code.getSystem();
      }

}

