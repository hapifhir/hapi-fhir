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

public class InvoicePriceComponentTypeEnumFactory implements EnumFactory<InvoicePriceComponentType> {

  public InvoicePriceComponentType fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("base".equals(codeString))
      return InvoicePriceComponentType.BASE;
    if ("surcharge".equals(codeString))
      return InvoicePriceComponentType.SURCHARGE;
    if ("deduction".equals(codeString))
      return InvoicePriceComponentType.DEDUCTION;
    if ("discount".equals(codeString))
      return InvoicePriceComponentType.DISCOUNT;
    if ("tax".equals(codeString))
      return InvoicePriceComponentType.TAX;
    if ("informational".equals(codeString))
      return InvoicePriceComponentType.INFORMATIONAL;
    throw new IllegalArgumentException("Unknown InvoicePriceComponentType code '"+codeString+"'");
  }

  public String toCode(InvoicePriceComponentType code) {
    if (code == InvoicePriceComponentType.BASE)
      return "base";
    if (code == InvoicePriceComponentType.SURCHARGE)
      return "surcharge";
    if (code == InvoicePriceComponentType.DEDUCTION)
      return "deduction";
    if (code == InvoicePriceComponentType.DISCOUNT)
      return "discount";
    if (code == InvoicePriceComponentType.TAX)
      return "tax";
    if (code == InvoicePriceComponentType.INFORMATIONAL)
      return "informational";
    return "?";
  }

    public String toSystem(InvoicePriceComponentType code) {
      return code.getSystem();
      }

}

