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

public class V3NullFlavorEnumFactory implements EnumFactory<V3NullFlavor> {

  public V3NullFlavor fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("NI".equals(codeString))
      return V3NullFlavor.NI;
    if ("INV".equals(codeString))
      return V3NullFlavor.INV;
    if ("DER".equals(codeString))
      return V3NullFlavor.DER;
    if ("OTH".equals(codeString))
      return V3NullFlavor.OTH;
    if ("NINF".equals(codeString))
      return V3NullFlavor.NINF;
    if ("PINF".equals(codeString))
      return V3NullFlavor.PINF;
    if ("UNC".equals(codeString))
      return V3NullFlavor.UNC;
    if ("MSK".equals(codeString))
      return V3NullFlavor.MSK;
    if ("NA".equals(codeString))
      return V3NullFlavor.NA;
    if ("UNK".equals(codeString))
      return V3NullFlavor.UNK;
    if ("ASKU".equals(codeString))
      return V3NullFlavor.ASKU;
    if ("NAV".equals(codeString))
      return V3NullFlavor.NAV;
    if ("NASK".equals(codeString))
      return V3NullFlavor.NASK;
    if ("QS".equals(codeString))
      return V3NullFlavor.QS;
    if ("TRC".equals(codeString))
      return V3NullFlavor.TRC;
    throw new IllegalArgumentException("Unknown V3NullFlavor code '"+codeString+"'");
  }

  public String toCode(V3NullFlavor code) {
    if (code == V3NullFlavor.NI)
      return "NI";
    if (code == V3NullFlavor.INV)
      return "INV";
    if (code == V3NullFlavor.DER)
      return "DER";
    if (code == V3NullFlavor.OTH)
      return "OTH";
    if (code == V3NullFlavor.NINF)
      return "NINF";
    if (code == V3NullFlavor.PINF)
      return "PINF";
    if (code == V3NullFlavor.UNC)
      return "UNC";
    if (code == V3NullFlavor.MSK)
      return "MSK";
    if (code == V3NullFlavor.NA)
      return "NA";
    if (code == V3NullFlavor.UNK)
      return "UNK";
    if (code == V3NullFlavor.ASKU)
      return "ASKU";
    if (code == V3NullFlavor.NAV)
      return "NAV";
    if (code == V3NullFlavor.NASK)
      return "NASK";
    if (code == V3NullFlavor.QS)
      return "QS";
    if (code == V3NullFlavor.TRC)
      return "TRC";
    return "?";
  }


}

