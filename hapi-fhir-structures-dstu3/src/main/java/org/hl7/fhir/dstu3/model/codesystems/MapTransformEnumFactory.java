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

public class MapTransformEnumFactory implements EnumFactory<MapTransform> {

  public MapTransform fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("create".equals(codeString))
      return MapTransform.CREATE;
    if ("copy".equals(codeString))
      return MapTransform.COPY;
    if ("truncate".equals(codeString))
      return MapTransform.TRUNCATE;
    if ("escape".equals(codeString))
      return MapTransform.ESCAPE;
    if ("cast".equals(codeString))
      return MapTransform.CAST;
    if ("append".equals(codeString))
      return MapTransform.APPEND;
    if ("translate".equals(codeString))
      return MapTransform.TRANSLATE;
    if ("reference".equals(codeString))
      return MapTransform.REFERENCE;
    if ("dateOp".equals(codeString))
      return MapTransform.DATEOP;
    if ("uuid".equals(codeString))
      return MapTransform.UUID;
    if ("pointer".equals(codeString))
      return MapTransform.POINTER;
    if ("evaluate".equals(codeString))
      return MapTransform.EVALUATE;
    if ("cc".equals(codeString))
      return MapTransform.CC;
    if ("c".equals(codeString))
      return MapTransform.C;
    if ("qty".equals(codeString))
      return MapTransform.QTY;
    if ("id".equals(codeString))
      return MapTransform.ID;
    if ("cp".equals(codeString))
      return MapTransform.CP;
    throw new IllegalArgumentException("Unknown MapTransform code '"+codeString+"'");
  }

  public String toCode(MapTransform code) {
    if (code == MapTransform.CREATE)
      return "create";
    if (code == MapTransform.COPY)
      return "copy";
    if (code == MapTransform.TRUNCATE)
      return "truncate";
    if (code == MapTransform.ESCAPE)
      return "escape";
    if (code == MapTransform.CAST)
      return "cast";
    if (code == MapTransform.APPEND)
      return "append";
    if (code == MapTransform.TRANSLATE)
      return "translate";
    if (code == MapTransform.REFERENCE)
      return "reference";
    if (code == MapTransform.DATEOP)
      return "dateOp";
    if (code == MapTransform.UUID)
      return "uuid";
    if (code == MapTransform.POINTER)
      return "pointer";
    if (code == MapTransform.EVALUATE)
      return "evaluate";
    if (code == MapTransform.CC)
      return "cc";
    if (code == MapTransform.C)
      return "c";
    if (code == MapTransform.QTY)
      return "qty";
    if (code == MapTransform.ID)
      return "id";
    if (code == MapTransform.CP)
      return "cp";
    return "?";
  }

    public String toSystem(MapTransform code) {
      return code.getSystem();
      }

}

