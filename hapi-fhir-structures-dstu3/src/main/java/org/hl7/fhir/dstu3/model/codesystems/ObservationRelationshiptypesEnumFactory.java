package org.hl7.fhir.dstu3.model.codesystems;

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

// Generated on Mon, Apr 17, 2017 17:38-0400 for FHIR v3.0.1


import org.hl7.fhir.dstu3.model.EnumFactory;

public class ObservationRelationshiptypesEnumFactory implements EnumFactory<ObservationRelationshiptypes> {

  public ObservationRelationshiptypes fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("has-member".equals(codeString))
      return ObservationRelationshiptypes.HASMEMBER;
    if ("derived-from".equals(codeString))
      return ObservationRelationshiptypes.DERIVEDFROM;
    if ("sequel-to".equals(codeString))
      return ObservationRelationshiptypes.SEQUELTO;
    if ("replaces".equals(codeString))
      return ObservationRelationshiptypes.REPLACES;
    if ("qualified-by".equals(codeString))
      return ObservationRelationshiptypes.QUALIFIEDBY;
    if ("interfered-by".equals(codeString))
      return ObservationRelationshiptypes.INTERFEREDBY;
    throw new IllegalArgumentException("Unknown ObservationRelationshiptypes code '"+codeString+"'");
  }

  public String toCode(ObservationRelationshiptypes code) {
    if (code == ObservationRelationshiptypes.HASMEMBER)
      return "has-member";
    if (code == ObservationRelationshiptypes.DERIVEDFROM)
      return "derived-from";
    if (code == ObservationRelationshiptypes.SEQUELTO)
      return "sequel-to";
    if (code == ObservationRelationshiptypes.REPLACES)
      return "replaces";
    if (code == ObservationRelationshiptypes.QUALIFIEDBY)
      return "qualified-by";
    if (code == ObservationRelationshiptypes.INTERFEREDBY)
      return "interfered-by";
    return "?";
  }

    public String toSystem(ObservationRelationshiptypes code) {
      return code.getSystem();
      }

}

