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

public class DataTypesEnumFactory implements EnumFactory<DataTypes> {

  public DataTypes fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("Address".equals(codeString))
      return DataTypes.ADDRESS;
    if ("Age".equals(codeString))
      return DataTypes.AGE;
    if ("Annotation".equals(codeString))
      return DataTypes.ANNOTATION;
    if ("Attachment".equals(codeString))
      return DataTypes.ATTACHMENT;
    if ("BackboneElement".equals(codeString))
      return DataTypes.BACKBONEELEMENT;
    if ("CodeableConcept".equals(codeString))
      return DataTypes.CODEABLECONCEPT;
    if ("Coding".equals(codeString))
      return DataTypes.CODING;
    if ("ContactDetail".equals(codeString))
      return DataTypes.CONTACTDETAIL;
    if ("ContactPoint".equals(codeString))
      return DataTypes.CONTACTPOINT;
    if ("Contributor".equals(codeString))
      return DataTypes.CONTRIBUTOR;
    if ("Count".equals(codeString))
      return DataTypes.COUNT;
    if ("DataRequirement".equals(codeString))
      return DataTypes.DATAREQUIREMENT;
    if ("Distance".equals(codeString))
      return DataTypes.DISTANCE;
    if ("Dosage".equals(codeString))
      return DataTypes.DOSAGE;
    if ("Duration".equals(codeString))
      return DataTypes.DURATION;
    if ("Element".equals(codeString))
      return DataTypes.ELEMENT;
    if ("ElementDefinition".equals(codeString))
      return DataTypes.ELEMENTDEFINITION;
    if ("Extension".equals(codeString))
      return DataTypes.EXTENSION;
    if ("HumanName".equals(codeString))
      return DataTypes.HUMANNAME;
    if ("Identifier".equals(codeString))
      return DataTypes.IDENTIFIER;
    if ("Meta".equals(codeString))
      return DataTypes.META;
    if ("Money".equals(codeString))
      return DataTypes.MONEY;
    if ("Narrative".equals(codeString))
      return DataTypes.NARRATIVE;
    if ("ParameterDefinition".equals(codeString))
      return DataTypes.PARAMETERDEFINITION;
    if ("Period".equals(codeString))
      return DataTypes.PERIOD;
    if ("Quantity".equals(codeString))
      return DataTypes.QUANTITY;
    if ("Range".equals(codeString))
      return DataTypes.RANGE;
    if ("Ratio".equals(codeString))
      return DataTypes.RATIO;
    if ("Reference".equals(codeString))
      return DataTypes.REFERENCE;
    if ("RelatedArtifact".equals(codeString))
      return DataTypes.RELATEDARTIFACT;
    if ("SampledData".equals(codeString))
      return DataTypes.SAMPLEDDATA;
    if ("Signature".equals(codeString))
      return DataTypes.SIGNATURE;
    if ("SimpleQuantity".equals(codeString))
      return DataTypes.SIMPLEQUANTITY;
    if ("Timing".equals(codeString))
      return DataTypes.TIMING;
    if ("TriggerDefinition".equals(codeString))
      return DataTypes.TRIGGERDEFINITION;
    if ("UsageContext".equals(codeString))
      return DataTypes.USAGECONTEXT;
    if ("base64Binary".equals(codeString))
      return DataTypes.BASE64BINARY;
    if ("boolean".equals(codeString))
      return DataTypes.BOOLEAN;
    if ("code".equals(codeString))
      return DataTypes.CODE;
    if ("date".equals(codeString))
      return DataTypes.DATE;
    if ("dateTime".equals(codeString))
      return DataTypes.DATETIME;
    if ("decimal".equals(codeString))
      return DataTypes.DECIMAL;
    if ("id".equals(codeString))
      return DataTypes.ID;
    if ("instant".equals(codeString))
      return DataTypes.INSTANT;
    if ("integer".equals(codeString))
      return DataTypes.INTEGER;
    if ("markdown".equals(codeString))
      return DataTypes.MARKDOWN;
    if ("oid".equals(codeString))
      return DataTypes.OID;
    if ("positiveInt".equals(codeString))
      return DataTypes.POSITIVEINT;
    if ("string".equals(codeString))
      return DataTypes.STRING;
    if ("time".equals(codeString))
      return DataTypes.TIME;
    if ("unsignedInt".equals(codeString))
      return DataTypes.UNSIGNEDINT;
    if ("uri".equals(codeString))
      return DataTypes.URI;
    if ("uuid".equals(codeString))
      return DataTypes.UUID;
    if ("xhtml".equals(codeString))
      return DataTypes.XHTML;
    throw new IllegalArgumentException("Unknown DataTypes code '"+codeString+"'");
  }

  public String toCode(DataTypes code) {
    if (code == DataTypes.ADDRESS)
      return "Address";
    if (code == DataTypes.AGE)
      return "Age";
    if (code == DataTypes.ANNOTATION)
      return "Annotation";
    if (code == DataTypes.ATTACHMENT)
      return "Attachment";
    if (code == DataTypes.BACKBONEELEMENT)
      return "BackboneElement";
    if (code == DataTypes.CODEABLECONCEPT)
      return "CodeableConcept";
    if (code == DataTypes.CODING)
      return "Coding";
    if (code == DataTypes.CONTACTDETAIL)
      return "ContactDetail";
    if (code == DataTypes.CONTACTPOINT)
      return "ContactPoint";
    if (code == DataTypes.CONTRIBUTOR)
      return "Contributor";
    if (code == DataTypes.COUNT)
      return "Count";
    if (code == DataTypes.DATAREQUIREMENT)
      return "DataRequirement";
    if (code == DataTypes.DISTANCE)
      return "Distance";
    if (code == DataTypes.DOSAGE)
      return "Dosage";
    if (code == DataTypes.DURATION)
      return "Duration";
    if (code == DataTypes.ELEMENT)
      return "Element";
    if (code == DataTypes.ELEMENTDEFINITION)
      return "ElementDefinition";
    if (code == DataTypes.EXTENSION)
      return "Extension";
    if (code == DataTypes.HUMANNAME)
      return "HumanName";
    if (code == DataTypes.IDENTIFIER)
      return "Identifier";
    if (code == DataTypes.META)
      return "Meta";
    if (code == DataTypes.MONEY)
      return "Money";
    if (code == DataTypes.NARRATIVE)
      return "Narrative";
    if (code == DataTypes.PARAMETERDEFINITION)
      return "ParameterDefinition";
    if (code == DataTypes.PERIOD)
      return "Period";
    if (code == DataTypes.QUANTITY)
      return "Quantity";
    if (code == DataTypes.RANGE)
      return "Range";
    if (code == DataTypes.RATIO)
      return "Ratio";
    if (code == DataTypes.REFERENCE)
      return "Reference";
    if (code == DataTypes.RELATEDARTIFACT)
      return "RelatedArtifact";
    if (code == DataTypes.SAMPLEDDATA)
      return "SampledData";
    if (code == DataTypes.SIGNATURE)
      return "Signature";
    if (code == DataTypes.SIMPLEQUANTITY)
      return "SimpleQuantity";
    if (code == DataTypes.TIMING)
      return "Timing";
    if (code == DataTypes.TRIGGERDEFINITION)
      return "TriggerDefinition";
    if (code == DataTypes.USAGECONTEXT)
      return "UsageContext";
    if (code == DataTypes.BASE64BINARY)
      return "base64Binary";
    if (code == DataTypes.BOOLEAN)
      return "boolean";
    if (code == DataTypes.CODE)
      return "code";
    if (code == DataTypes.DATE)
      return "date";
    if (code == DataTypes.DATETIME)
      return "dateTime";
    if (code == DataTypes.DECIMAL)
      return "decimal";
    if (code == DataTypes.ID)
      return "id";
    if (code == DataTypes.INSTANT)
      return "instant";
    if (code == DataTypes.INTEGER)
      return "integer";
    if (code == DataTypes.MARKDOWN)
      return "markdown";
    if (code == DataTypes.OID)
      return "oid";
    if (code == DataTypes.POSITIVEINT)
      return "positiveInt";
    if (code == DataTypes.STRING)
      return "string";
    if (code == DataTypes.TIME)
      return "time";
    if (code == DataTypes.UNSIGNEDINT)
      return "unsignedInt";
    if (code == DataTypes.URI)
      return "uri";
    if (code == DataTypes.UUID)
      return "uuid";
    if (code == DataTypes.XHTML)
      return "xhtml";
    return "?";
  }

    public String toSystem(DataTypes code) {
      return code.getSystem();
      }

}

