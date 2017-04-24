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

public class ItemTypeEnumFactory implements EnumFactory<ItemType> {

  public ItemType fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("group".equals(codeString))
      return ItemType.GROUP;
    if ("display".equals(codeString))
      return ItemType.DISPLAY;
    if ("question".equals(codeString))
      return ItemType.QUESTION;
    if ("boolean".equals(codeString))
      return ItemType.BOOLEAN;
    if ("decimal".equals(codeString))
      return ItemType.DECIMAL;
    if ("integer".equals(codeString))
      return ItemType.INTEGER;
    if ("date".equals(codeString))
      return ItemType.DATE;
    if ("dateTime".equals(codeString))
      return ItemType.DATETIME;
    if ("time".equals(codeString))
      return ItemType.TIME;
    if ("string".equals(codeString))
      return ItemType.STRING;
    if ("text".equals(codeString))
      return ItemType.TEXT;
    if ("url".equals(codeString))
      return ItemType.URL;
    if ("choice".equals(codeString))
      return ItemType.CHOICE;
    if ("open-choice".equals(codeString))
      return ItemType.OPENCHOICE;
    if ("attachment".equals(codeString))
      return ItemType.ATTACHMENT;
    if ("reference".equals(codeString))
      return ItemType.REFERENCE;
    if ("quantity".equals(codeString))
      return ItemType.QUANTITY;
    throw new IllegalArgumentException("Unknown ItemType code '"+codeString+"'");
  }

  public String toCode(ItemType code) {
    if (code == ItemType.GROUP)
      return "group";
    if (code == ItemType.DISPLAY)
      return "display";
    if (code == ItemType.QUESTION)
      return "question";
    if (code == ItemType.BOOLEAN)
      return "boolean";
    if (code == ItemType.DECIMAL)
      return "decimal";
    if (code == ItemType.INTEGER)
      return "integer";
    if (code == ItemType.DATE)
      return "date";
    if (code == ItemType.DATETIME)
      return "dateTime";
    if (code == ItemType.TIME)
      return "time";
    if (code == ItemType.STRING)
      return "string";
    if (code == ItemType.TEXT)
      return "text";
    if (code == ItemType.URL)
      return "url";
    if (code == ItemType.CHOICE)
      return "choice";
    if (code == ItemType.OPENCHOICE)
      return "open-choice";
    if (code == ItemType.ATTACHMENT)
      return "attachment";
    if (code == ItemType.REFERENCE)
      return "reference";
    if (code == ItemType.QUANTITY)
      return "quantity";
    return "?";
  }

    public String toSystem(ItemType code) {
      return code.getSystem();
      }

}

