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

public class V3HL7StandardVersionCodeEnumFactory implements EnumFactory<V3HL7StandardVersionCode> {

  public V3HL7StandardVersionCode fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("Ballot2008Jan".equals(codeString))
      return V3HL7StandardVersionCode.BALLOT2008JAN;
    if ("Ballot2008May".equals(codeString))
      return V3HL7StandardVersionCode.BALLOT2008MAY;
    if ("Ballot2008Sep".equals(codeString))
      return V3HL7StandardVersionCode.BALLOT2008SEP;
    if ("Ballot2009Jan".equals(codeString))
      return V3HL7StandardVersionCode.BALLOT2009JAN;
    if ("Ballot2009May".equals(codeString))
      return V3HL7StandardVersionCode.BALLOT2009MAY;
    if ("Ballot2009Sep".equals(codeString))
      return V3HL7StandardVersionCode.BALLOT2009SEP;
    if ("Ballot2010Jan".equals(codeString))
      return V3HL7StandardVersionCode.BALLOT2010JAN;
    if ("Ballot2010May".equals(codeString))
      return V3HL7StandardVersionCode.BALLOT2010MAY;
    if ("Ballot2010Sep".equals(codeString))
      return V3HL7StandardVersionCode.BALLOT2010SEP;
    if ("Ballot2011Jan".equals(codeString))
      return V3HL7StandardVersionCode.BALLOT2011JAN;
    if ("Ballot2011May".equals(codeString))
      return V3HL7StandardVersionCode.BALLOT2011MAY;
    if ("Ballot2011Sep".equals(codeString))
      return V3HL7StandardVersionCode.BALLOT2011SEP;
    if ("Ballot2012Jan".equals(codeString))
      return V3HL7StandardVersionCode.BALLOT2012JAN;
    if ("Ballot2012May".equals(codeString))
      return V3HL7StandardVersionCode.BALLOT2012MAY;
    if ("Ballot2012Sep".equals(codeString))
      return V3HL7StandardVersionCode.BALLOT2012SEP;
    if ("V3-2003-12".equals(codeString))
      return V3HL7StandardVersionCode.V3200312;
    if ("V3-2005N".equals(codeString))
      return V3HL7StandardVersionCode.V32005N;
    if ("V3-2006N".equals(codeString))
      return V3HL7StandardVersionCode.V32006N;
    if ("V3-2008N".equals(codeString))
      return V3HL7StandardVersionCode.V32008N;
    if ("V3-2009N".equals(codeString))
      return V3HL7StandardVersionCode.V32009N;
    if ("V3-2010N".equals(codeString))
      return V3HL7StandardVersionCode.V32010N;
    if ("V3-2011N".equals(codeString))
      return V3HL7StandardVersionCode.V32011N;
    if ("V3-2012N".equals(codeString))
      return V3HL7StandardVersionCode.V32012N;
    if ("V3PR1".equals(codeString))
      return V3HL7StandardVersionCode.V3PR1;
    if ("V3-2007N".equals(codeString))
      return V3HL7StandardVersionCode.V32007N;
    throw new IllegalArgumentException("Unknown V3HL7StandardVersionCode code '"+codeString+"'");
  }

  public String toCode(V3HL7StandardVersionCode code) {
    if (code == V3HL7StandardVersionCode.BALLOT2008JAN)
      return "Ballot2008Jan";
    if (code == V3HL7StandardVersionCode.BALLOT2008MAY)
      return "Ballot2008May";
    if (code == V3HL7StandardVersionCode.BALLOT2008SEP)
      return "Ballot2008Sep";
    if (code == V3HL7StandardVersionCode.BALLOT2009JAN)
      return "Ballot2009Jan";
    if (code == V3HL7StandardVersionCode.BALLOT2009MAY)
      return "Ballot2009May";
    if (code == V3HL7StandardVersionCode.BALLOT2009SEP)
      return "Ballot2009Sep";
    if (code == V3HL7StandardVersionCode.BALLOT2010JAN)
      return "Ballot2010Jan";
    if (code == V3HL7StandardVersionCode.BALLOT2010MAY)
      return "Ballot2010May";
    if (code == V3HL7StandardVersionCode.BALLOT2010SEP)
      return "Ballot2010Sep";
    if (code == V3HL7StandardVersionCode.BALLOT2011JAN)
      return "Ballot2011Jan";
    if (code == V3HL7StandardVersionCode.BALLOT2011MAY)
      return "Ballot2011May";
    if (code == V3HL7StandardVersionCode.BALLOT2011SEP)
      return "Ballot2011Sep";
    if (code == V3HL7StandardVersionCode.BALLOT2012JAN)
      return "Ballot2012Jan";
    if (code == V3HL7StandardVersionCode.BALLOT2012MAY)
      return "Ballot2012May";
    if (code == V3HL7StandardVersionCode.BALLOT2012SEP)
      return "Ballot2012Sep";
    if (code == V3HL7StandardVersionCode.V3200312)
      return "V3-2003-12";
    if (code == V3HL7StandardVersionCode.V32005N)
      return "V3-2005N";
    if (code == V3HL7StandardVersionCode.V32006N)
      return "V3-2006N";
    if (code == V3HL7StandardVersionCode.V32008N)
      return "V3-2008N";
    if (code == V3HL7StandardVersionCode.V32009N)
      return "V3-2009N";
    if (code == V3HL7StandardVersionCode.V32010N)
      return "V3-2010N";
    if (code == V3HL7StandardVersionCode.V32011N)
      return "V3-2011N";
    if (code == V3HL7StandardVersionCode.V32012N)
      return "V3-2012N";
    if (code == V3HL7StandardVersionCode.V3PR1)
      return "V3PR1";
    if (code == V3HL7StandardVersionCode.V32007N)
      return "V3-2007N";
    return "?";
  }

    public String toSystem(V3HL7StandardVersionCode code) {
      return code.getSystem();
      }

}

