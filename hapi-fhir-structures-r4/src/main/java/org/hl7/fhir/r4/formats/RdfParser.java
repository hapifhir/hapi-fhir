package org.hl7.fhir.r4.formats;

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

// Generated on Sat, Sep 23, 2017 17:56-0400 for FHIR v3.1.0

import org.hl7.fhir.r4.model.DateType;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.IntegerType;
import org.hl7.fhir.r4.model.OidType;
import org.hl7.fhir.r4.model.UriType;
import org.hl7.fhir.r4.model.UuidType;
import org.hl7.fhir.r4.model.InstantType;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.Base64BinaryType;
import org.hl7.fhir.r4.model.UnsignedIntType;
import org.hl7.fhir.r4.model.MarkdownType;
import org.hl7.fhir.r4.model.TimeType;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.PositiveIntType;
import org.hl7.fhir.r4.model.DecimalType;
import org.hl7.fhir.r4.model.*;
import org.xmlpull.v1.*;
import org.hl7.fhir.utilities.Utilities;
import org.hl7.fhir.exceptions.FHIRFormatError;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.r4.utils.formats.Turtle.Complex;
import java.io.IOException;

public class RdfParser extends RdfParserBase {

  public RdfParser() {
    super();
  }

  public RdfParser(boolean allowUnknownContent) {
    super();
    setAllowUnknownContent(allowUnknownContent);
  }


  protected void composeElement(Complex t, String parentType, String name, Element element, int index) {
    if (element == null) 
      return;
    if (index > -1)
      t.predicate("fhir:index", Integer.toString(index));
    if (element.hasIdElement())
      composeString(t, "Element", "id", element.getIdElement(), -1);
    for (int i = 0; i < element.getExtension().size(); i++)
      composeExtension(t, "Element", "extension", element.getExtension().get(i), i);
  }

  protected void composeBackboneElement(Complex t, String tType, String name, BackboneElement element, int index) {
    composeElement(t, tType, name, element, index);
    for (int i = 0; i < element.getModifierExtension().size(); i++)
      composeExtension(t, "Element", "modifierExtension", element.getModifierExtension().get(i), i);
  }

  private void composeEnum(Complex parent, String parentType, String name, Enumeration<? extends Enum> value, int index) {
    if (value == null)
      return;
    Complex t = parent.predicate("fhir:"+parentType+"."+name);
    t.predicate("fhir:value", ttlLiteral(value.asStringValue()));
    composeElement(t, parentType, name, value, index);
    decorateCode(t, value);
  }


  protected void composeDate(Complex parent, String parentType, String name, DateType value, int index) {
    if (value == null)
      return;
    Complex t = parent.predicate("fhir:"+parentType+"."+name);
    t.predicate("fhir:value", ttlLiteral(value.asStringValue()));
    composeElement(t, parentType, name, value, index);
  }

  protected void composeDateTime(Complex parent, String parentType, String name, DateTimeType value, int index) {
    if (value == null)
      return;
    Complex t = parent.predicate("fhir:"+parentType+"."+name);
    t.predicate("fhir:value", ttlLiteral(value.asStringValue()));
    composeElement(t, parentType, name, value, index);
  }

  protected void composeCode(Complex parent, String parentType, String name, CodeType value, int index) {
    if (value == null)
      return;
    Complex t = parent.predicate("fhir:"+parentType+"."+name);
    t.predicate("fhir:value", ttlLiteral(value.asStringValue()));
    composeElement(t, parentType, name, value, index);
    decorateCode(t, value);
  }

  protected void composeString(Complex parent, String parentType, String name, StringType value, int index) {
    if (value == null)
      return;
    Complex t = parent.predicate("fhir:"+parentType+"."+name);
    t.predicate("fhir:value", ttlLiteral(value.asStringValue()));
    composeElement(t, parentType, name, value, index);
  }

  protected void composeInteger(Complex parent, String parentType, String name, IntegerType value, int index) {
    if (value == null)
      return;
    Complex t = parent.predicate("fhir:"+parentType+"."+name);
    t.predicate("fhir:value", ttlLiteral(value.asStringValue()));
    composeElement(t, parentType, name, value, index);
  }

  protected void composeOid(Complex parent, String parentType, String name, OidType value, int index) {
    if (value == null)
      return;
    Complex t = parent.predicate("fhir:"+parentType+"."+name);
    t.predicate("fhir:value", ttlLiteral(value.asStringValue()));
    composeElement(t, parentType, name, value, index);
  }

  protected void composeUri(Complex parent, String parentType, String name, UriType value, int index) {
    if (value == null)
      return;
    Complex t = parent.predicate("fhir:"+parentType+"."+name);
    t.predicate("fhir:value", ttlLiteral(value.asStringValue()));
    composeElement(t, parentType, name, value, index);
  }

  protected void composeUuid(Complex parent, String parentType, String name, UuidType value, int index) {
    if (value == null)
      return;
    Complex t = parent.predicate("fhir:"+parentType+"."+name);
    t.predicate("fhir:value", ttlLiteral(value.asStringValue()));
    composeElement(t, parentType, name, value, index);
  }

  protected void composeInstant(Complex parent, String parentType, String name, InstantType value, int index) {
    if (value == null)
      return;
    Complex t = parent.predicate("fhir:"+parentType+"."+name);
    t.predicate("fhir:value", ttlLiteral(value.asStringValue()));
    composeElement(t, parentType, name, value, index);
  }

  protected void composeBoolean(Complex parent, String parentType, String name, BooleanType value, int index) {
    if (value == null)
      return;
    Complex t = parent.predicate("fhir:"+parentType+"."+name);
    t.predicate("fhir:value", ttlLiteral(value.asStringValue()));
    composeElement(t, parentType, name, value, index);
  }

  protected void composeBase64Binary(Complex parent, String parentType, String name, Base64BinaryType value, int index) {
    if (value == null)
      return;
    Complex t = parent.predicate("fhir:"+parentType+"."+name);
    t.predicate("fhir:value", ttlLiteral(value.asStringValue()));
    composeElement(t, parentType, name, value, index);
  }

  protected void composeUnsignedInt(Complex parent, String parentType, String name, UnsignedIntType value, int index) {
    if (value == null)
      return;
    Complex t = parent.predicate("fhir:"+parentType+"."+name);
    t.predicate("fhir:value", ttlLiteral(value.asStringValue()));
    composeElement(t, parentType, name, value, index);
  }

  protected void composeMarkdown(Complex parent, String parentType, String name, MarkdownType value, int index) {
    if (value == null)
      return;
    Complex t = parent.predicate("fhir:"+parentType+"."+name);
    t.predicate("fhir:value", ttlLiteral(value.asStringValue()));
    composeElement(t, parentType, name, value, index);
  }

  protected void composeTime(Complex parent, String parentType, String name, TimeType value, int index) {
    if (value == null)
      return;
    Complex t = parent.predicate("fhir:"+parentType+"."+name);
    t.predicate("fhir:value", ttlLiteral(value.asStringValue()));
    composeElement(t, parentType, name, value, index);
  }

  protected void composeId(Complex parent, String parentType, String name, IdType value, int index) {
    if (value == null)
      return;
    Complex t = parent.predicate("fhir:"+parentType+"."+name);
    t.predicate("fhir:value", ttlLiteral(value.asStringValue()));
    composeElement(t, parentType, name, value, index);
  }

  protected void composePositiveInt(Complex parent, String parentType, String name, PositiveIntType value, int index) {
    if (value == null)
      return;
    Complex t = parent.predicate("fhir:"+parentType+"."+name);
    t.predicate("fhir:value", ttlLiteral(value.asStringValue()));
    composeElement(t, parentType, name, value, index);
  }

  protected void composeDecimal(Complex parent, String parentType, String name, DecimalType value, int index) {
    if (value == null)
      return;
    Complex t = parent.predicate("fhir:"+parentType+"."+name);
    t.predicate("fhir:value", ttlLiteral(value.asStringValue()));
    composeElement(t, parentType, name, value, index);
  }

  protected void composeExtension(Complex parent, String parentType, String name, Extension element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeElement(t, "Extension", name, element, index);
    if (element.hasUrlElement())
      composeUri(t, "Extension", "url", element.getUrlElement(), -1);
    if (element.hasValue())
      composeType(t, "Extension", "value", element.getValue(), -1);
  }

  protected void composeNarrative(Complex parent, String parentType, String name, Narrative element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeElement(t, "Narrative", name, element, index);
    if (element.hasStatusElement())
      composeEnum(t, "Narrative", "status", element.getStatusElement(), -1);
    if (element.hasDiv())
      composeXhtml(t, "Narrative", "div", element.getDiv(), -1);
  }

  protected void composeReference(Complex parent, String parentType, String name, Reference element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeElement(t, "Reference", name, element, index);
    if (element.hasReferenceElement())
      composeString(t, "Reference", "reference", element.getReferenceElement_(), -1);
    if (element.hasIdentifier())
      composeIdentifier(t, "Reference", "identifier", element.getIdentifier(), -1);
    if (element.hasDisplayElement())
      composeString(t, "Reference", "display", element.getDisplayElement(), -1);
  }

  protected void composeQuantity(Complex parent, String parentType, String name, Quantity element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeElement(t, "Quantity", name, element, index);
    if (element.hasValueElement())
      composeDecimal(t, "Quantity", "value", element.getValueElement(), -1);
    if (element.hasComparatorElement())
      composeEnum(t, "Quantity", "comparator", element.getComparatorElement(), -1);
    if (element.hasUnitElement())
      composeString(t, "Quantity", "unit", element.getUnitElement(), -1);
    if (element.hasSystemElement())
      composeUri(t, "Quantity", "system", element.getSystemElement(), -1);
    if (element.hasCodeElement())
      composeCode(t, "Quantity", "code", element.getCodeElement(), -1);
  }

  protected void composePeriod(Complex parent, String parentType, String name, Period element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeElement(t, "Period", name, element, index);
    if (element.hasStartElement())
      composeDateTime(t, "Period", "start", element.getStartElement(), -1);
    if (element.hasEndElement())
      composeDateTime(t, "Period", "end", element.getEndElement(), -1);
  }

  protected void composeAttachment(Complex parent, String parentType, String name, Attachment element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeElement(t, "Attachment", name, element, index);
    if (element.hasContentTypeElement())
      composeCode(t, "Attachment", "contentType", element.getContentTypeElement(), -1);
    if (element.hasLanguageElement())
      composeCode(t, "Attachment", "language", element.getLanguageElement(), -1);
    if (element.hasDataElement())
      composeBase64Binary(t, "Attachment", "data", element.getDataElement(), -1);
    if (element.hasUrlElement())
      composeUri(t, "Attachment", "url", element.getUrlElement(), -1);
    if (element.hasSizeElement())
      composeUnsignedInt(t, "Attachment", "size", element.getSizeElement(), -1);
    if (element.hasHashElement())
      composeBase64Binary(t, "Attachment", "hash", element.getHashElement(), -1);
    if (element.hasTitleElement())
      composeString(t, "Attachment", "title", element.getTitleElement(), -1);
    if (element.hasCreationElement())
      composeDateTime(t, "Attachment", "creation", element.getCreationElement(), -1);
  }

  protected void composeDuration(Complex parent, String parentType, String name, Duration element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeElement(t, "Duration", name, element, index);
  }

  protected void composeCount(Complex parent, String parentType, String name, Count element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeElement(t, "Count", name, element, index);
  }

  protected void composeRange(Complex parent, String parentType, String name, Range element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeElement(t, "Range", name, element, index);
    if (element.hasLow())
      composeQuantity(t, "Range", "low", element.getLow(), -1);
    if (element.hasHigh())
      composeQuantity(t, "Range", "high", element.getHigh(), -1);
  }

  protected void composeAnnotation(Complex parent, String parentType, String name, Annotation element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeElement(t, "Annotation", name, element, index);
    if (element.hasAuthor())
      composeType(t, "Annotation", "author", element.getAuthor(), -1);
    if (element.hasTimeElement())
      composeDateTime(t, "Annotation", "time", element.getTimeElement(), -1);
    if (element.hasTextElement())
      composeString(t, "Annotation", "text", element.getTextElement(), -1);
  }

  protected void composeMoney(Complex parent, String parentType, String name, Money element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeElement(t, "Money", name, element, index);
  }

  protected void composeIdentifier(Complex parent, String parentType, String name, Identifier element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeElement(t, "Identifier", name, element, index);
    if (element.hasUseElement())
      composeEnum(t, "Identifier", "use", element.getUseElement(), -1);
    if (element.hasType())
      composeCodeableConcept(t, "Identifier", "type", element.getType(), -1);
    if (element.hasSystemElement())
      composeUri(t, "Identifier", "system", element.getSystemElement(), -1);
    if (element.hasValueElement())
      composeString(t, "Identifier", "value", element.getValueElement(), -1);
    if (element.hasPeriod())
      composePeriod(t, "Identifier", "period", element.getPeriod(), -1);
    if (element.hasAssigner())
      composeReference(t, "Identifier", "assigner", element.getAssigner(), -1);
  }

  protected void composeCoding(Complex parent, String parentType, String name, Coding element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeElement(t, "Coding", name, element, index);
    decorateCoding(t, element);
    if (element.hasSystemElement())
      composeUri(t, "Coding", "system", element.getSystemElement(), -1);
    if (element.hasVersionElement())
      composeString(t, "Coding", "version", element.getVersionElement(), -1);
    if (element.hasCodeElement())
      composeCode(t, "Coding", "code", element.getCodeElement(), -1);
    if (element.hasDisplayElement())
      composeString(t, "Coding", "display", element.getDisplayElement(), -1);
    if (element.hasUserSelectedElement())
      composeBoolean(t, "Coding", "userSelected", element.getUserSelectedElement(), -1);
  }

  protected void composeSignature(Complex parent, String parentType, String name, Signature element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeElement(t, "Signature", name, element, index);
    for (int i = 0; i < element.getType().size(); i++)
      composeCoding(t, "Signature", "type", element.getType().get(i), i);
    if (element.hasWhenElement())
      composeInstant(t, "Signature", "when", element.getWhenElement(), -1);
    if (element.hasWho())
      composeType(t, "Signature", "who", element.getWho(), -1);
    if (element.hasOnBehalfOf())
      composeType(t, "Signature", "onBehalfOf", element.getOnBehalfOf(), -1);
    if (element.hasContentTypeElement())
      composeCode(t, "Signature", "contentType", element.getContentTypeElement(), -1);
    if (element.hasBlobElement())
      composeBase64Binary(t, "Signature", "blob", element.getBlobElement(), -1);
  }

  protected void composeSampledData(Complex parent, String parentType, String name, SampledData element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeElement(t, "SampledData", name, element, index);
    if (element.hasOrigin())
      composeQuantity(t, "SampledData", "origin", element.getOrigin(), -1);
    if (element.hasPeriodElement())
      composeDecimal(t, "SampledData", "period", element.getPeriodElement(), -1);
    if (element.hasFactorElement())
      composeDecimal(t, "SampledData", "factor", element.getFactorElement(), -1);
    if (element.hasLowerLimitElement())
      composeDecimal(t, "SampledData", "lowerLimit", element.getLowerLimitElement(), -1);
    if (element.hasUpperLimitElement())
      composeDecimal(t, "SampledData", "upperLimit", element.getUpperLimitElement(), -1);
    if (element.hasDimensionsElement())
      composePositiveInt(t, "SampledData", "dimensions", element.getDimensionsElement(), -1);
    if (element.hasDataElement())
      composeString(t, "SampledData", "data", element.getDataElement(), -1);
  }

  protected void composeRatio(Complex parent, String parentType, String name, Ratio element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeElement(t, "Ratio", name, element, index);
    if (element.hasNumerator())
      composeQuantity(t, "Ratio", "numerator", element.getNumerator(), -1);
    if (element.hasDenominator())
      composeQuantity(t, "Ratio", "denominator", element.getDenominator(), -1);
  }

  protected void composeDistance(Complex parent, String parentType, String name, Distance element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeElement(t, "Distance", name, element, index);
  }

  protected void composeAge(Complex parent, String parentType, String name, Age element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeElement(t, "Age", name, element, index);
  }

  protected void composeCodeableConcept(Complex parent, String parentType, String name, CodeableConcept element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeElement(t, "CodeableConcept", name, element, index);
    decorateCodeableConcept(t, element);
    for (int i = 0; i < element.getCoding().size(); i++)
      composeCoding(t, "CodeableConcept", "coding", element.getCoding().get(i), i);
    if (element.hasTextElement())
      composeString(t, "CodeableConcept", "text", element.getTextElement(), -1);
  }

  protected void composeMeta(Complex parent, String parentType, String name, Meta element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeElement(t, "Meta", name, element, index);
    if (element.hasVersionIdElement())
      composeId(t, "Meta", "versionId", element.getVersionIdElement(), -1);
    if (element.hasLastUpdatedElement())
      composeInstant(t, "Meta", "lastUpdated", element.getLastUpdatedElement(), -1);
    for (int i = 0; i < element.getProfile().size(); i++)
      composeUri(t, "Meta", "profile", element.getProfile().get(i), i);
    for (int i = 0; i < element.getSecurity().size(); i++)
      composeCoding(t, "Meta", "security", element.getSecurity().get(i), i);
    for (int i = 0; i < element.getTag().size(); i++)
      composeCoding(t, "Meta", "tag", element.getTag().get(i), i);
  }

  protected void composeAddress(Complex parent, String parentType, String name, Address element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeElement(t, "Address", name, element, index);
    if (element.hasUseElement())
      composeEnum(t, "Address", "use", element.getUseElement(), -1);
    if (element.hasTypeElement())
      composeEnum(t, "Address", "type", element.getTypeElement(), -1);
    if (element.hasTextElement())
      composeString(t, "Address", "text", element.getTextElement(), -1);
    for (int i = 0; i < element.getLine().size(); i++)
      composeString(t, "Address", "line", element.getLine().get(i), i);
    if (element.hasCityElement())
      composeString(t, "Address", "city", element.getCityElement(), -1);
    if (element.hasDistrictElement())
      composeString(t, "Address", "district", element.getDistrictElement(), -1);
    if (element.hasStateElement())
      composeString(t, "Address", "state", element.getStateElement(), -1);
    if (element.hasPostalCodeElement())
      composeString(t, "Address", "postalCode", element.getPostalCodeElement(), -1);
    if (element.hasCountryElement())
      composeString(t, "Address", "country", element.getCountryElement(), -1);
    if (element.hasPeriod())
      composePeriod(t, "Address", "period", element.getPeriod(), -1);
  }

  protected void composeTriggerDefinition(Complex parent, String parentType, String name, TriggerDefinition element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeElement(t, "TriggerDefinition", name, element, index);
    if (element.hasTypeElement())
      composeEnum(t, "TriggerDefinition", "type", element.getTypeElement(), -1);
    if (element.hasNameElement())
      composeUri(t, "TriggerDefinition", "name", element.getNameElement(), -1);
    if (element.hasTiming())
      composeType(t, "TriggerDefinition", "timing", element.getTiming(), -1);
    if (element.hasData())
      composeDataRequirement(t, "TriggerDefinition", "data", element.getData(), -1);
    if (element.hasCondition())
      composeTriggerDefinitionTriggerDefinitionConditionComponent(t, "TriggerDefinition", "condition", element.getCondition(), -1);
  }

  protected void composeTriggerDefinitionTriggerDefinitionConditionComponent(Complex parent, String parentType, String name, TriggerDefinition.TriggerDefinitionConditionComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeElement(t, "condition", name, element, index);
    if (element.hasDescriptionElement())
      composeString(t, "TriggerDefinition", "description", element.getDescriptionElement(), -1);
    if (element.hasLanguageElement())
      composeEnum(t, "TriggerDefinition", "language", element.getLanguageElement(), -1);
    if (element.hasExpressionElement())
      composeString(t, "TriggerDefinition", "expression", element.getExpressionElement(), -1);
  }

  protected void composeContributor(Complex parent, String parentType, String name, Contributor element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeElement(t, "Contributor", name, element, index);
    if (element.hasTypeElement())
      composeEnum(t, "Contributor", "type", element.getTypeElement(), -1);
    if (element.hasNameElement())
      composeString(t, "Contributor", "name", element.getNameElement(), -1);
    for (int i = 0; i < element.getContact().size(); i++)
      composeContactDetail(t, "Contributor", "contact", element.getContact().get(i), i);
  }

  protected void composeDataRequirement(Complex parent, String parentType, String name, DataRequirement element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeElement(t, "DataRequirement", name, element, index);
    if (element.hasTypeElement())
      composeCode(t, "DataRequirement", "type", element.getTypeElement(), -1);
    for (int i = 0; i < element.getProfile().size(); i++)
      composeUri(t, "DataRequirement", "profile", element.getProfile().get(i), i);
    for (int i = 0; i < element.getMustSupport().size(); i++)
      composeString(t, "DataRequirement", "mustSupport", element.getMustSupport().get(i), i);
    for (int i = 0; i < element.getCodeFilter().size(); i++)
      composeDataRequirementDataRequirementCodeFilterComponent(t, "DataRequirement", "codeFilter", element.getCodeFilter().get(i), i);
    for (int i = 0; i < element.getDateFilter().size(); i++)
      composeDataRequirementDataRequirementDateFilterComponent(t, "DataRequirement", "dateFilter", element.getDateFilter().get(i), i);
  }

  protected void composeDataRequirementDataRequirementCodeFilterComponent(Complex parent, String parentType, String name, DataRequirement.DataRequirementCodeFilterComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeElement(t, "codeFilter", name, element, index);
    if (element.hasPathElement())
      composeString(t, "DataRequirement", "path", element.getPathElement(), -1);
    if (element.hasValueSet())
      composeType(t, "DataRequirement", "valueSet", element.getValueSet(), -1);
    for (int i = 0; i < element.getCode().size(); i++)
      composeCoding(t, "DataRequirement", "code", element.getCode().get(i), i);
  }

  protected void composeDataRequirementDataRequirementDateFilterComponent(Complex parent, String parentType, String name, DataRequirement.DataRequirementDateFilterComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeElement(t, "dateFilter", name, element, index);
    if (element.hasPathElement())
      composeString(t, "DataRequirement", "path", element.getPathElement(), -1);
    if (element.hasValue())
      composeType(t, "DataRequirement", "value", element.getValue(), -1);
  }

  protected void composeDosage(Complex parent, String parentType, String name, Dosage element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeElement(t, "Dosage", name, element, index);
    if (element.hasSequenceElement())
      composeInteger(t, "Dosage", "sequence", element.getSequenceElement(), -1);
    if (element.hasTextElement())
      composeString(t, "Dosage", "text", element.getTextElement(), -1);
    for (int i = 0; i < element.getAdditionalInstruction().size(); i++)
      composeCodeableConcept(t, "Dosage", "additionalInstruction", element.getAdditionalInstruction().get(i), i);
    if (element.hasPatientInstructionElement())
      composeString(t, "Dosage", "patientInstruction", element.getPatientInstructionElement(), -1);
    if (element.hasTiming())
      composeTiming(t, "Dosage", "timing", element.getTiming(), -1);
    if (element.hasAsNeeded())
      composeType(t, "Dosage", "asNeeded", element.getAsNeeded(), -1);
    if (element.hasSite())
      composeCodeableConcept(t, "Dosage", "site", element.getSite(), -1);
    if (element.hasRoute())
      composeCodeableConcept(t, "Dosage", "route", element.getRoute(), -1);
    if (element.hasMethod())
      composeCodeableConcept(t, "Dosage", "method", element.getMethod(), -1);
    if (element.hasDose())
      composeType(t, "Dosage", "dose", element.getDose(), -1);
    if (element.hasMaxDosePerPeriod())
      composeRatio(t, "Dosage", "maxDosePerPeriod", element.getMaxDosePerPeriod(), -1);
    if (element.hasMaxDosePerAdministration())
      composeQuantity(t, "Dosage", "maxDosePerAdministration", element.getMaxDosePerAdministration(), -1);
    if (element.hasMaxDosePerLifetime())
      composeQuantity(t, "Dosage", "maxDosePerLifetime", element.getMaxDosePerLifetime(), -1);
    if (element.hasRate())
      composeType(t, "Dosage", "rate", element.getRate(), -1);
  }

  protected void composeRelatedArtifact(Complex parent, String parentType, String name, RelatedArtifact element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeElement(t, "RelatedArtifact", name, element, index);
    if (element.hasTypeElement())
      composeEnum(t, "RelatedArtifact", "type", element.getTypeElement(), -1);
    if (element.hasDisplayElement())
      composeString(t, "RelatedArtifact", "display", element.getDisplayElement(), -1);
    if (element.hasCitationElement())
      composeString(t, "RelatedArtifact", "citation", element.getCitationElement(), -1);
    if (element.hasUrlElement())
      composeUri(t, "RelatedArtifact", "url", element.getUrlElement(), -1);
    if (element.hasDocument())
      composeAttachment(t, "RelatedArtifact", "document", element.getDocument(), -1);
    if (element.hasResource())
      composeReference(t, "RelatedArtifact", "resource", element.getResource(), -1);
  }

  protected void composeContactDetail(Complex parent, String parentType, String name, ContactDetail element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeElement(t, "ContactDetail", name, element, index);
    if (element.hasNameElement())
      composeString(t, "ContactDetail", "name", element.getNameElement(), -1);
    for (int i = 0; i < element.getTelecom().size(); i++)
      composeContactPoint(t, "ContactDetail", "telecom", element.getTelecom().get(i), i);
  }

  protected void composeHumanName(Complex parent, String parentType, String name, HumanName element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeElement(t, "HumanName", name, element, index);
    if (element.hasUseElement())
      composeEnum(t, "HumanName", "use", element.getUseElement(), -1);
    if (element.hasTextElement())
      composeString(t, "HumanName", "text", element.getTextElement(), -1);
    if (element.hasFamilyElement())
      composeString(t, "HumanName", "family", element.getFamilyElement(), -1);
    for (int i = 0; i < element.getGiven().size(); i++)
      composeString(t, "HumanName", "given", element.getGiven().get(i), i);
    for (int i = 0; i < element.getPrefix().size(); i++)
      composeString(t, "HumanName", "prefix", element.getPrefix().get(i), i);
    for (int i = 0; i < element.getSuffix().size(); i++)
      composeString(t, "HumanName", "suffix", element.getSuffix().get(i), i);
    if (element.hasPeriod())
      composePeriod(t, "HumanName", "period", element.getPeriod(), -1);
  }

  protected void composeContactPoint(Complex parent, String parentType, String name, ContactPoint element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeElement(t, "ContactPoint", name, element, index);
    if (element.hasSystemElement())
      composeEnum(t, "ContactPoint", "system", element.getSystemElement(), -1);
    if (element.hasValueElement())
      composeString(t, "ContactPoint", "value", element.getValueElement(), -1);
    if (element.hasUseElement())
      composeEnum(t, "ContactPoint", "use", element.getUseElement(), -1);
    if (element.hasRankElement())
      composePositiveInt(t, "ContactPoint", "rank", element.getRankElement(), -1);
    if (element.hasPeriod())
      composePeriod(t, "ContactPoint", "period", element.getPeriod(), -1);
  }

  protected void composeUsageContext(Complex parent, String parentType, String name, UsageContext element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeElement(t, "UsageContext", name, element, index);
    if (element.hasCode())
      composeCoding(t, "UsageContext", "code", element.getCode(), -1);
    if (element.hasValue())
      composeType(t, "UsageContext", "value", element.getValue(), -1);
  }

  protected void composeTiming(Complex parent, String parentType, String name, Timing element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeElement(t, "Timing", name, element, index);
    for (int i = 0; i < element.getEvent().size(); i++)
      composeDateTime(t, "Timing", "event", element.getEvent().get(i), i);
    if (element.hasRepeat())
      composeTimingTimingRepeatComponent(t, "Timing", "repeat", element.getRepeat(), -1);
    if (element.hasCode())
      composeCodeableConcept(t, "Timing", "code", element.getCode(), -1);
  }

  protected void composeTimingTimingRepeatComponent(Complex parent, String parentType, String name, Timing.TimingRepeatComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeElement(t, "repeat", name, element, index);
    if (element.hasBounds())
      composeType(t, "Timing", "bounds", element.getBounds(), -1);
    if (element.hasCountElement())
      composeInteger(t, "Timing", "count", element.getCountElement(), -1);
    if (element.hasCountMaxElement())
      composeInteger(t, "Timing", "countMax", element.getCountMaxElement(), -1);
    if (element.hasDurationElement())
      composeDecimal(t, "Timing", "duration", element.getDurationElement(), -1);
    if (element.hasDurationMaxElement())
      composeDecimal(t, "Timing", "durationMax", element.getDurationMaxElement(), -1);
    if (element.hasDurationUnitElement())
      composeEnum(t, "Timing", "durationUnit", element.getDurationUnitElement(), -1);
    if (element.hasFrequencyElement())
      composeInteger(t, "Timing", "frequency", element.getFrequencyElement(), -1);
    if (element.hasFrequencyMaxElement())
      composeInteger(t, "Timing", "frequencyMax", element.getFrequencyMaxElement(), -1);
    if (element.hasPeriodElement())
      composeDecimal(t, "Timing", "period", element.getPeriodElement(), -1);
    if (element.hasPeriodMaxElement())
      composeDecimal(t, "Timing", "periodMax", element.getPeriodMaxElement(), -1);
    if (element.hasPeriodUnitElement())
      composeEnum(t, "Timing", "periodUnit", element.getPeriodUnitElement(), -1);
    for (int i = 0; i < element.getDayOfWeek().size(); i++)
      composeEnum(t, "Timing", "dayOfWeek", element.getDayOfWeek().get(i), i);
    for (int i = 0; i < element.getTimeOfDay().size(); i++)
      composeTime(t, "Timing", "timeOfDay", element.getTimeOfDay().get(i), i);
    for (int i = 0; i < element.getWhen().size(); i++)
      composeEnum(t, "Timing", "when", element.getWhen().get(i), i);
    if (element.hasOffsetElement())
      composeUnsignedInt(t, "Timing", "offset", element.getOffsetElement(), -1);
  }

  protected void composeElementDefinition(Complex parent, String parentType, String name, ElementDefinition element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeElement(t, "ElementDefinition", name, element, index);
    if (element.hasPathElement())
      composeString(t, "ElementDefinition", "path", element.getPathElement(), -1);
    for (int i = 0; i < element.getRepresentation().size(); i++)
      composeEnum(t, "ElementDefinition", "representation", element.getRepresentation().get(i), i);
    if (element.hasSliceNameElement())
      composeString(t, "ElementDefinition", "sliceName", element.getSliceNameElement(), -1);
    if (element.hasLabelElement())
      composeString(t, "ElementDefinition", "label", element.getLabelElement(), -1);
    for (int i = 0; i < element.getCode().size(); i++)
      composeCoding(t, "ElementDefinition", "code", element.getCode().get(i), i);
    if (element.hasSlicing())
      composeElementDefinitionElementDefinitionSlicingComponent(t, "ElementDefinition", "slicing", element.getSlicing(), -1);
    if (element.hasShortElement())
      composeString(t, "ElementDefinition", "short", element.getShortElement(), -1);
    if (element.hasDefinitionElement())
      composeMarkdown(t, "ElementDefinition", "definition", element.getDefinitionElement(), -1);
    if (element.hasCommentElement())
      composeMarkdown(t, "ElementDefinition", "comment", element.getCommentElement(), -1);
    if (element.hasRequirementsElement())
      composeMarkdown(t, "ElementDefinition", "requirements", element.getRequirementsElement(), -1);
    for (int i = 0; i < element.getAlias().size(); i++)
      composeString(t, "ElementDefinition", "alias", element.getAlias().get(i), i);
    if (element.hasMinElement())
      composeUnsignedInt(t, "ElementDefinition", "min", element.getMinElement(), -1);
    if (element.hasMaxElement())
      composeString(t, "ElementDefinition", "max", element.getMaxElement(), -1);
    if (element.hasBase())
      composeElementDefinitionElementDefinitionBaseComponent(t, "ElementDefinition", "base", element.getBase(), -1);
    if (element.hasContentReferenceElement())
      composeUri(t, "ElementDefinition", "contentReference", element.getContentReferenceElement(), -1);
    for (int i = 0; i < element.getType().size(); i++)
      composeElementDefinitionTypeRefComponent(t, "ElementDefinition", "type", element.getType().get(i), i);
    if (element.hasDefaultValue())
      composeType(t, "ElementDefinition", "defaultValue", element.getDefaultValue(), -1);
    if (element.hasMeaningWhenMissingElement())
      composeMarkdown(t, "ElementDefinition", "meaningWhenMissing", element.getMeaningWhenMissingElement(), -1);
    if (element.hasOrderMeaningElement())
      composeString(t, "ElementDefinition", "orderMeaning", element.getOrderMeaningElement(), -1);
    if (element.hasFixed())
      composeType(t, "ElementDefinition", "fixed", element.getFixed(), -1);
    if (element.hasPattern())
      composeType(t, "ElementDefinition", "pattern", element.getPattern(), -1);
    for (int i = 0; i < element.getExample().size(); i++)
      composeElementDefinitionElementDefinitionExampleComponent(t, "ElementDefinition", "example", element.getExample().get(i), i);
    if (element.hasMinValue())
      composeType(t, "ElementDefinition", "minValue", element.getMinValue(), -1);
    if (element.hasMaxValue())
      composeType(t, "ElementDefinition", "maxValue", element.getMaxValue(), -1);
    if (element.hasMaxLengthElement())
      composeInteger(t, "ElementDefinition", "maxLength", element.getMaxLengthElement(), -1);
    for (int i = 0; i < element.getCondition().size(); i++)
      composeId(t, "ElementDefinition", "condition", element.getCondition().get(i), i);
    for (int i = 0; i < element.getConstraint().size(); i++)
      composeElementDefinitionElementDefinitionConstraintComponent(t, "ElementDefinition", "constraint", element.getConstraint().get(i), i);
    if (element.hasMustSupportElement())
      composeBoolean(t, "ElementDefinition", "mustSupport", element.getMustSupportElement(), -1);
    if (element.hasIsModifierElement())
      composeBoolean(t, "ElementDefinition", "isModifier", element.getIsModifierElement(), -1);
    if (element.hasIsSummaryElement())
      composeBoolean(t, "ElementDefinition", "isSummary", element.getIsSummaryElement(), -1);
    if (element.hasBinding())
      composeElementDefinitionElementDefinitionBindingComponent(t, "ElementDefinition", "binding", element.getBinding(), -1);
    for (int i = 0; i < element.getMapping().size(); i++)
      composeElementDefinitionElementDefinitionMappingComponent(t, "ElementDefinition", "mapping", element.getMapping().get(i), i);
  }

  protected void composeElementDefinitionElementDefinitionSlicingComponent(Complex parent, String parentType, String name, ElementDefinition.ElementDefinitionSlicingComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeElement(t, "slicing", name, element, index);
    for (int i = 0; i < element.getDiscriminator().size(); i++)
      composeElementDefinitionElementDefinitionSlicingDiscriminatorComponent(t, "ElementDefinition", "discriminator", element.getDiscriminator().get(i), i);
    if (element.hasDescriptionElement())
      composeString(t, "ElementDefinition", "description", element.getDescriptionElement(), -1);
    if (element.hasOrderedElement())
      composeBoolean(t, "ElementDefinition", "ordered", element.getOrderedElement(), -1);
    if (element.hasRulesElement())
      composeEnum(t, "ElementDefinition", "rules", element.getRulesElement(), -1);
  }

  protected void composeElementDefinitionElementDefinitionSlicingDiscriminatorComponent(Complex parent, String parentType, String name, ElementDefinition.ElementDefinitionSlicingDiscriminatorComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeElement(t, "discriminator", name, element, index);
    if (element.hasTypeElement())
      composeEnum(t, "ElementDefinition", "type", element.getTypeElement(), -1);
    if (element.hasPathElement())
      composeString(t, "ElementDefinition", "path", element.getPathElement(), -1);
  }

  protected void composeElementDefinitionElementDefinitionBaseComponent(Complex parent, String parentType, String name, ElementDefinition.ElementDefinitionBaseComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeElement(t, "base", name, element, index);
    if (element.hasPathElement())
      composeString(t, "ElementDefinition", "path", element.getPathElement(), -1);
    if (element.hasMinElement())
      composeUnsignedInt(t, "ElementDefinition", "min", element.getMinElement(), -1);
    if (element.hasMaxElement())
      composeString(t, "ElementDefinition", "max", element.getMaxElement(), -1);
  }

  protected void composeElementDefinitionTypeRefComponent(Complex parent, String parentType, String name, ElementDefinition.TypeRefComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeElement(t, "type", name, element, index);
    if (element.hasCodeElement())
      composeUri(t, "ElementDefinition", "code", element.getCodeElement(), -1);
    if (element.hasProfileElement())
      composeUri(t, "ElementDefinition", "profile", element.getProfileElement(), -1);
    if (element.hasTargetProfileElement())
      composeUri(t, "ElementDefinition", "targetProfile", element.getTargetProfileElement(), -1);
    for (int i = 0; i < element.getAggregation().size(); i++)
      composeEnum(t, "ElementDefinition", "aggregation", element.getAggregation().get(i), i);
    if (element.hasVersioningElement())
      composeEnum(t, "ElementDefinition", "versioning", element.getVersioningElement(), -1);
  }

  protected void composeElementDefinitionElementDefinitionExampleComponent(Complex parent, String parentType, String name, ElementDefinition.ElementDefinitionExampleComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeElement(t, "example", name, element, index);
    if (element.hasLabelElement())
      composeString(t, "ElementDefinition", "label", element.getLabelElement(), -1);
    if (element.hasValue())
      composeType(t, "ElementDefinition", "value", element.getValue(), -1);
  }

  protected void composeElementDefinitionElementDefinitionConstraintComponent(Complex parent, String parentType, String name, ElementDefinition.ElementDefinitionConstraintComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeElement(t, "constraint", name, element, index);
    if (element.hasKeyElement())
      composeId(t, "ElementDefinition", "key", element.getKeyElement(), -1);
    if (element.hasRequirementsElement())
      composeString(t, "ElementDefinition", "requirements", element.getRequirementsElement(), -1);
    if (element.hasSeverityElement())
      composeEnum(t, "ElementDefinition", "severity", element.getSeverityElement(), -1);
    if (element.hasHumanElement())
      composeString(t, "ElementDefinition", "human", element.getHumanElement(), -1);
    if (element.hasExpressionElement())
      composeString(t, "ElementDefinition", "expression", element.getExpressionElement(), -1);
    if (element.hasXpathElement())
      composeString(t, "ElementDefinition", "xpath", element.getXpathElement(), -1);
    if (element.hasSourceElement())
      composeUri(t, "ElementDefinition", "source", element.getSourceElement(), -1);
  }

  protected void composeElementDefinitionElementDefinitionBindingComponent(Complex parent, String parentType, String name, ElementDefinition.ElementDefinitionBindingComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeElement(t, "binding", name, element, index);
    if (element.hasStrengthElement())
      composeEnum(t, "ElementDefinition", "strength", element.getStrengthElement(), -1);
    if (element.hasDescriptionElement())
      composeString(t, "ElementDefinition", "description", element.getDescriptionElement(), -1);
    if (element.hasValueSet())
      composeType(t, "ElementDefinition", "valueSet", element.getValueSet(), -1);
  }

  protected void composeElementDefinitionElementDefinitionMappingComponent(Complex parent, String parentType, String name, ElementDefinition.ElementDefinitionMappingComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeElement(t, "mapping", name, element, index);
    if (element.hasIdentityElement())
      composeId(t, "ElementDefinition", "identity", element.getIdentityElement(), -1);
    if (element.hasLanguageElement())
      composeCode(t, "ElementDefinition", "language", element.getLanguageElement(), -1);
    if (element.hasMapElement())
      composeString(t, "ElementDefinition", "map", element.getMapElement(), -1);
    if (element.hasCommentElement())
      composeString(t, "ElementDefinition", "comment", element.getCommentElement(), -1);
  }

  protected void composeParameterDefinition(Complex parent, String parentType, String name, ParameterDefinition element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeElement(t, "ParameterDefinition", name, element, index);
    if (element.hasNameElement())
      composeCode(t, "ParameterDefinition", "name", element.getNameElement(), -1);
    if (element.hasUseElement())
      composeEnum(t, "ParameterDefinition", "use", element.getUseElement(), -1);
    if (element.hasMinElement())
      composeInteger(t, "ParameterDefinition", "min", element.getMinElement(), -1);
    if (element.hasMaxElement())
      composeString(t, "ParameterDefinition", "max", element.getMaxElement(), -1);
    if (element.hasDocumentationElement())
      composeString(t, "ParameterDefinition", "documentation", element.getDocumentationElement(), -1);
    if (element.hasTypeElement())
      composeCode(t, "ParameterDefinition", "type", element.getTypeElement(), -1);
    if (element.hasProfile())
      composeReference(t, "ParameterDefinition", "profile", element.getProfile(), -1);
  }

  protected void composeDomainResource(Complex t, String parentType, String name, DomainResource element, int index) {
    composeResource(t, parentType, name, element, index);
    if (element.hasText())
      composeNarrative(t, "DomainResource", "text", element.getText(), -1);
    for (int i = 0; i < element.getContained().size(); i++)
      composeResource(t, "DomainResource", "contained", element.getContained().get(i), i);
    for (int i = 0; i < element.getExtension().size(); i++)
      composeExtension(t, "DomainResource", "extension", element.getExtension().get(i), i);
    for (int i = 0; i < element.getModifierExtension().size(); i++)
      composeExtension(t, "DomainResource", "modifierExtension", element.getModifierExtension().get(i), i);
  }

  protected void composeParameters(Complex parent, String parentType, String name, Parameters element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeResource(t, "Parameters", name, element, index);
    for (int i = 0; i < element.getParameter().size(); i++)
      composeParametersParametersParameterComponent(t, "Parameters", "parameter", element.getParameter().get(i), i);
  }

  protected void composeParametersParametersParameterComponent(Complex parent, String parentType, String name, Parameters.ParametersParameterComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "parameter", name, element, index);
    if (element.hasNameElement())
      composeString(t, "Parameters", "name", element.getNameElement(), -1);
    if (element.hasValue())
      composeType(t, "Parameters", "value", element.getValue(), -1);
    if (element.hasResource())
      composeResource(t, "Parameters", "resource", element.getResource(), -1);
    for (int i = 0; i < element.getPart().size(); i++)
      composeParametersParametersParameterComponent(t, "Parameters", "part", element.getPart().get(i), i);
  }

  protected void composeResource(Complex t, String parentType, String name, Resource element, int index) {
    if (element.hasIdElement())
      composeId(t, "Resource", "id", element.getIdElement(), -1);
    if (element.hasMeta())
      composeMeta(t, "Resource", "meta", element.getMeta(), -1);
    if (element.hasImplicitRulesElement())
      composeUri(t, "Resource", "implicitRules", element.getImplicitRulesElement(), -1);
    if (element.hasLanguageElement())
      composeCode(t, "Resource", "language", element.getLanguageElement(), -1);
  }

  protected void composeAccount(Complex parent, String parentType, String name, Account element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "Account", name, element, index);
    for (int i = 0; i < element.getIdentifier().size(); i++)
      composeIdentifier(t, "Account", "identifier", element.getIdentifier().get(i), i);
    if (element.hasStatusElement())
      composeEnum(t, "Account", "status", element.getStatusElement(), -1);
    if (element.hasType())
      composeCodeableConcept(t, "Account", "type", element.getType(), -1);
    if (element.hasNameElement())
      composeString(t, "Account", "name", element.getNameElement(), -1);
    if (element.hasSubject())
      composeReference(t, "Account", "subject", element.getSubject(), -1);
    if (element.hasPeriod())
      composePeriod(t, "Account", "period", element.getPeriod(), -1);
    if (element.hasActive())
      composePeriod(t, "Account", "active", element.getActive(), -1);
    if (element.hasBalance())
      composeMoney(t, "Account", "balance", element.getBalance(), -1);
    for (int i = 0; i < element.getCoverage().size(); i++)
      composeAccountCoverageComponent(t, "Account", "coverage", element.getCoverage().get(i), i);
    if (element.hasOwner())
      composeReference(t, "Account", "owner", element.getOwner(), -1);
    if (element.hasDescriptionElement())
      composeString(t, "Account", "description", element.getDescriptionElement(), -1);
    for (int i = 0; i < element.getGuarantor().size(); i++)
      composeAccountGuarantorComponent(t, "Account", "guarantor", element.getGuarantor().get(i), i);
  }

  protected void composeAccountCoverageComponent(Complex parent, String parentType, String name, Account.CoverageComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "coverage", name, element, index);
    if (element.hasCoverage())
      composeReference(t, "Account", "coverage", element.getCoverage(), -1);
    if (element.hasPriorityElement())
      composePositiveInt(t, "Account", "priority", element.getPriorityElement(), -1);
  }

  protected void composeAccountGuarantorComponent(Complex parent, String parentType, String name, Account.GuarantorComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "guarantor", name, element, index);
    if (element.hasParty())
      composeReference(t, "Account", "party", element.getParty(), -1);
    if (element.hasOnHoldElement())
      composeBoolean(t, "Account", "onHold", element.getOnHoldElement(), -1);
    if (element.hasPeriod())
      composePeriod(t, "Account", "period", element.getPeriod(), -1);
  }

  protected void composeActivityDefinition(Complex parent, String parentType, String name, ActivityDefinition element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "ActivityDefinition", name, element, index);
    if (element.hasUrlElement())
      composeUri(t, "ActivityDefinition", "url", element.getUrlElement(), -1);
    for (int i = 0; i < element.getIdentifier().size(); i++)
      composeIdentifier(t, "ActivityDefinition", "identifier", element.getIdentifier().get(i), i);
    if (element.hasVersionElement())
      composeString(t, "ActivityDefinition", "version", element.getVersionElement(), -1);
    if (element.hasNameElement())
      composeString(t, "ActivityDefinition", "name", element.getNameElement(), -1);
    if (element.hasTitleElement())
      composeString(t, "ActivityDefinition", "title", element.getTitleElement(), -1);
    if (element.hasStatusElement())
      composeEnum(t, "ActivityDefinition", "status", element.getStatusElement(), -1);
    if (element.hasExperimentalElement())
      composeBoolean(t, "ActivityDefinition", "experimental", element.getExperimentalElement(), -1);
    if (element.hasDateElement())
      composeDateTime(t, "ActivityDefinition", "date", element.getDateElement(), -1);
    if (element.hasPublisherElement())
      composeString(t, "ActivityDefinition", "publisher", element.getPublisherElement(), -1);
    if (element.hasDescriptionElement())
      composeMarkdown(t, "ActivityDefinition", "description", element.getDescriptionElement(), -1);
    if (element.hasPurposeElement())
      composeMarkdown(t, "ActivityDefinition", "purpose", element.getPurposeElement(), -1);
    if (element.hasUsageElement())
      composeString(t, "ActivityDefinition", "usage", element.getUsageElement(), -1);
    if (element.hasApprovalDateElement())
      composeDate(t, "ActivityDefinition", "approvalDate", element.getApprovalDateElement(), -1);
    if (element.hasLastReviewDateElement())
      composeDate(t, "ActivityDefinition", "lastReviewDate", element.getLastReviewDateElement(), -1);
    if (element.hasEffectivePeriod())
      composePeriod(t, "ActivityDefinition", "effectivePeriod", element.getEffectivePeriod(), -1);
    for (int i = 0; i < element.getUseContext().size(); i++)
      composeUsageContext(t, "ActivityDefinition", "useContext", element.getUseContext().get(i), i);
    for (int i = 0; i < element.getJurisdiction().size(); i++)
      composeCodeableConcept(t, "ActivityDefinition", "jurisdiction", element.getJurisdiction().get(i), i);
    for (int i = 0; i < element.getTopic().size(); i++)
      composeCodeableConcept(t, "ActivityDefinition", "topic", element.getTopic().get(i), i);
    for (int i = 0; i < element.getContributor().size(); i++)
      composeContributor(t, "ActivityDefinition", "contributor", element.getContributor().get(i), i);
    for (int i = 0; i < element.getContact().size(); i++)
      composeContactDetail(t, "ActivityDefinition", "contact", element.getContact().get(i), i);
    if (element.hasCopyrightElement())
      composeMarkdown(t, "ActivityDefinition", "copyright", element.getCopyrightElement(), -1);
    for (int i = 0; i < element.getRelatedArtifact().size(); i++)
      composeRelatedArtifact(t, "ActivityDefinition", "relatedArtifact", element.getRelatedArtifact().get(i), i);
    for (int i = 0; i < element.getLibrary().size(); i++)
      composeReference(t, "ActivityDefinition", "library", element.getLibrary().get(i), i);
    if (element.hasKindElement())
      composeEnum(t, "ActivityDefinition", "kind", element.getKindElement(), -1);
    if (element.hasCode())
      composeCodeableConcept(t, "ActivityDefinition", "code", element.getCode(), -1);
    if (element.hasTiming())
      composeType(t, "ActivityDefinition", "timing", element.getTiming(), -1);
    if (element.hasLocation())
      composeReference(t, "ActivityDefinition", "location", element.getLocation(), -1);
    for (int i = 0; i < element.getParticipant().size(); i++)
      composeActivityDefinitionActivityDefinitionParticipantComponent(t, "ActivityDefinition", "participant", element.getParticipant().get(i), i);
    if (element.hasProduct())
      composeType(t, "ActivityDefinition", "product", element.getProduct(), -1);
    if (element.hasQuantity())
      composeQuantity(t, "ActivityDefinition", "quantity", element.getQuantity(), -1);
    for (int i = 0; i < element.getDosage().size(); i++)
      composeDosage(t, "ActivityDefinition", "dosage", element.getDosage().get(i), i);
    for (int i = 0; i < element.getBodySite().size(); i++)
      composeCodeableConcept(t, "ActivityDefinition", "bodySite", element.getBodySite().get(i), i);
    if (element.hasTransform())
      composeReference(t, "ActivityDefinition", "transform", element.getTransform(), -1);
    for (int i = 0; i < element.getDynamicValue().size(); i++)
      composeActivityDefinitionActivityDefinitionDynamicValueComponent(t, "ActivityDefinition", "dynamicValue", element.getDynamicValue().get(i), i);
  }

  protected void composeActivityDefinitionActivityDefinitionParticipantComponent(Complex parent, String parentType, String name, ActivityDefinition.ActivityDefinitionParticipantComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "participant", name, element, index);
    if (element.hasTypeElement())
      composeEnum(t, "ActivityDefinition", "type", element.getTypeElement(), -1);
    if (element.hasRole())
      composeCodeableConcept(t, "ActivityDefinition", "role", element.getRole(), -1);
  }

  protected void composeActivityDefinitionActivityDefinitionDynamicValueComponent(Complex parent, String parentType, String name, ActivityDefinition.ActivityDefinitionDynamicValueComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "dynamicValue", name, element, index);
    if (element.hasDescriptionElement())
      composeString(t, "ActivityDefinition", "description", element.getDescriptionElement(), -1);
    if (element.hasPathElement())
      composeString(t, "ActivityDefinition", "path", element.getPathElement(), -1);
    if (element.hasLanguageElement())
      composeString(t, "ActivityDefinition", "language", element.getLanguageElement(), -1);
    if (element.hasExpressionElement())
      composeString(t, "ActivityDefinition", "expression", element.getExpressionElement(), -1);
  }

  protected void composeAdverseEvent(Complex parent, String parentType, String name, AdverseEvent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "AdverseEvent", name, element, index);
    if (element.hasIdentifier())
      composeIdentifier(t, "AdverseEvent", "identifier", element.getIdentifier(), -1);
    if (element.hasActualityElement())
      composeEnum(t, "AdverseEvent", "actuality", element.getActualityElement(), -1);
    if (element.hasCategoryElement())
      composeEnum(t, "AdverseEvent", "category", element.getCategoryElement(), -1);
    if (element.hasEvent())
      composeCodeableConcept(t, "AdverseEvent", "event", element.getEvent(), -1);
    if (element.hasSubject())
      composeReference(t, "AdverseEvent", "subject", element.getSubject(), -1);
    if (element.hasDateElement())
      composeDateTime(t, "AdverseEvent", "date", element.getDateElement(), -1);
    for (int i = 0; i < element.getResultingCondition().size(); i++)
      composeReference(t, "AdverseEvent", "resultingCondition", element.getResultingCondition().get(i), i);
    if (element.hasLocation())
      composeReference(t, "AdverseEvent", "location", element.getLocation(), -1);
    if (element.hasSeriousness())
      composeCodeableConcept(t, "AdverseEvent", "seriousness", element.getSeriousness(), -1);
    if (element.hasSeverity())
      composeCodeableConcept(t, "AdverseEvent", "severity", element.getSeverity(), -1);
    if (element.hasOutcome())
      composeCodeableConcept(t, "AdverseEvent", "outcome", element.getOutcome(), -1);
    if (element.hasRecorder())
      composeReference(t, "AdverseEvent", "recorder", element.getRecorder(), -1);
    if (element.hasEventParticipant())
      composeReference(t, "AdverseEvent", "eventParticipant", element.getEventParticipant(), -1);
    if (element.hasDescriptionElement())
      composeString(t, "AdverseEvent", "description", element.getDescriptionElement(), -1);
    for (int i = 0; i < element.getSuspectEntity().size(); i++)
      composeAdverseEventAdverseEventSuspectEntityComponent(t, "AdverseEvent", "suspectEntity", element.getSuspectEntity().get(i), i);
    for (int i = 0; i < element.getSubjectMedicalHistory().size(); i++)
      composeReference(t, "AdverseEvent", "subjectMedicalHistory", element.getSubjectMedicalHistory().get(i), i);
    for (int i = 0; i < element.getReferenceDocument().size(); i++)
      composeReference(t, "AdverseEvent", "referenceDocument", element.getReferenceDocument().get(i), i);
    for (int i = 0; i < element.getStudy().size(); i++)
      composeReference(t, "AdverseEvent", "study", element.getStudy().get(i), i);
  }

  protected void composeAdverseEventAdverseEventSuspectEntityComponent(Complex parent, String parentType, String name, AdverseEvent.AdverseEventSuspectEntityComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "suspectEntity", name, element, index);
    if (element.hasInstance())
      composeReference(t, "AdverseEvent", "instance", element.getInstance(), -1);
    for (int i = 0; i < element.getCausality().size(); i++)
      composeAdverseEventAdverseEventSuspectEntityCausalityComponent(t, "AdverseEvent", "causality", element.getCausality().get(i), i);
  }

  protected void composeAdverseEventAdverseEventSuspectEntityCausalityComponent(Complex parent, String parentType, String name, AdverseEvent.AdverseEventSuspectEntityCausalityComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "causality", name, element, index);
    if (element.hasAssessment())
      composeCodeableConcept(t, "AdverseEvent", "assessment", element.getAssessment(), -1);
    if (element.hasProductRelatednessElement())
      composeString(t, "AdverseEvent", "productRelatedness", element.getProductRelatednessElement(), -1);
    if (element.hasAuthor())
      composeReference(t, "AdverseEvent", "author", element.getAuthor(), -1);
    if (element.hasMethod())
      composeCodeableConcept(t, "AdverseEvent", "method", element.getMethod(), -1);
  }

  protected void composeAllergyIntolerance(Complex parent, String parentType, String name, AllergyIntolerance element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "AllergyIntolerance", name, element, index);
    for (int i = 0; i < element.getIdentifier().size(); i++)
      composeIdentifier(t, "AllergyIntolerance", "identifier", element.getIdentifier().get(i), i);
    if (element.hasClinicalStatusElement())
      composeEnum(t, "AllergyIntolerance", "clinicalStatus", element.getClinicalStatusElement(), -1);
    if (element.hasVerificationStatusElement())
      composeEnum(t, "AllergyIntolerance", "verificationStatus", element.getVerificationStatusElement(), -1);
    if (element.hasTypeElement())
      composeEnum(t, "AllergyIntolerance", "type", element.getTypeElement(), -1);
    for (int i = 0; i < element.getCategory().size(); i++)
      composeEnum(t, "AllergyIntolerance", "category", element.getCategory().get(i), i);
    if (element.hasCriticalityElement())
      composeEnum(t, "AllergyIntolerance", "criticality", element.getCriticalityElement(), -1);
    if (element.hasCode())
      composeCodeableConcept(t, "AllergyIntolerance", "code", element.getCode(), -1);
    if (element.hasPatient())
      composeReference(t, "AllergyIntolerance", "patient", element.getPatient(), -1);
    if (element.hasOnset())
      composeType(t, "AllergyIntolerance", "onset", element.getOnset(), -1);
    if (element.hasAssertedDateElement())
      composeDateTime(t, "AllergyIntolerance", "assertedDate", element.getAssertedDateElement(), -1);
    if (element.hasRecorder())
      composeReference(t, "AllergyIntolerance", "recorder", element.getRecorder(), -1);
    if (element.hasAsserter())
      composeReference(t, "AllergyIntolerance", "asserter", element.getAsserter(), -1);
    if (element.hasLastOccurrenceElement())
      composeDateTime(t, "AllergyIntolerance", "lastOccurrence", element.getLastOccurrenceElement(), -1);
    for (int i = 0; i < element.getNote().size(); i++)
      composeAnnotation(t, "AllergyIntolerance", "note", element.getNote().get(i), i);
    for (int i = 0; i < element.getReaction().size(); i++)
      composeAllergyIntoleranceAllergyIntoleranceReactionComponent(t, "AllergyIntolerance", "reaction", element.getReaction().get(i), i);
  }

  protected void composeAllergyIntoleranceAllergyIntoleranceReactionComponent(Complex parent, String parentType, String name, AllergyIntolerance.AllergyIntoleranceReactionComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "reaction", name, element, index);
    if (element.hasSubstance())
      composeCodeableConcept(t, "AllergyIntolerance", "substance", element.getSubstance(), -1);
    for (int i = 0; i < element.getManifestation().size(); i++)
      composeCodeableConcept(t, "AllergyIntolerance", "manifestation", element.getManifestation().get(i), i);
    if (element.hasDescriptionElement())
      composeString(t, "AllergyIntolerance", "description", element.getDescriptionElement(), -1);
    if (element.hasOnsetElement())
      composeDateTime(t, "AllergyIntolerance", "onset", element.getOnsetElement(), -1);
    if (element.hasSeverityElement())
      composeEnum(t, "AllergyIntolerance", "severity", element.getSeverityElement(), -1);
    if (element.hasExposureRoute())
      composeCodeableConcept(t, "AllergyIntolerance", "exposureRoute", element.getExposureRoute(), -1);
    for (int i = 0; i < element.getNote().size(); i++)
      composeAnnotation(t, "AllergyIntolerance", "note", element.getNote().get(i), i);
  }

  protected void composeAppointment(Complex parent, String parentType, String name, Appointment element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "Appointment", name, element, index);
    for (int i = 0; i < element.getIdentifier().size(); i++)
      composeIdentifier(t, "Appointment", "identifier", element.getIdentifier().get(i), i);
    if (element.hasStatusElement())
      composeEnum(t, "Appointment", "status", element.getStatusElement(), -1);
    if (element.hasServiceCategory())
      composeCodeableConcept(t, "Appointment", "serviceCategory", element.getServiceCategory(), -1);
    for (int i = 0; i < element.getServiceType().size(); i++)
      composeCodeableConcept(t, "Appointment", "serviceType", element.getServiceType().get(i), i);
    for (int i = 0; i < element.getSpecialty().size(); i++)
      composeCodeableConcept(t, "Appointment", "specialty", element.getSpecialty().get(i), i);
    if (element.hasAppointmentType())
      composeCodeableConcept(t, "Appointment", "appointmentType", element.getAppointmentType(), -1);
    for (int i = 0; i < element.getReason().size(); i++)
      composeCodeableConcept(t, "Appointment", "reason", element.getReason().get(i), i);
    for (int i = 0; i < element.getIndication().size(); i++)
      composeReference(t, "Appointment", "indication", element.getIndication().get(i), i);
    if (element.hasPriorityElement())
      composeUnsignedInt(t, "Appointment", "priority", element.getPriorityElement(), -1);
    if (element.hasDescriptionElement())
      composeString(t, "Appointment", "description", element.getDescriptionElement(), -1);
    for (int i = 0; i < element.getSupportingInformation().size(); i++)
      composeReference(t, "Appointment", "supportingInformation", element.getSupportingInformation().get(i), i);
    if (element.hasStartElement())
      composeInstant(t, "Appointment", "start", element.getStartElement(), -1);
    if (element.hasEndElement())
      composeInstant(t, "Appointment", "end", element.getEndElement(), -1);
    if (element.hasMinutesDurationElement())
      composePositiveInt(t, "Appointment", "minutesDuration", element.getMinutesDurationElement(), -1);
    for (int i = 0; i < element.getSlot().size(); i++)
      composeReference(t, "Appointment", "slot", element.getSlot().get(i), i);
    if (element.hasCreatedElement())
      composeDateTime(t, "Appointment", "created", element.getCreatedElement(), -1);
    if (element.hasCommentElement())
      composeString(t, "Appointment", "comment", element.getCommentElement(), -1);
    for (int i = 0; i < element.getIncomingReferral().size(); i++)
      composeReference(t, "Appointment", "incomingReferral", element.getIncomingReferral().get(i), i);
    for (int i = 0; i < element.getParticipant().size(); i++)
      composeAppointmentAppointmentParticipantComponent(t, "Appointment", "participant", element.getParticipant().get(i), i);
    for (int i = 0; i < element.getRequestedPeriod().size(); i++)
      composePeriod(t, "Appointment", "requestedPeriod", element.getRequestedPeriod().get(i), i);
  }

  protected void composeAppointmentAppointmentParticipantComponent(Complex parent, String parentType, String name, Appointment.AppointmentParticipantComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "participant", name, element, index);
    for (int i = 0; i < element.getType().size(); i++)
      composeCodeableConcept(t, "Appointment", "type", element.getType().get(i), i);
    if (element.hasActor())
      composeReference(t, "Appointment", "actor", element.getActor(), -1);
    if (element.hasRequiredElement())
      composeEnum(t, "Appointment", "required", element.getRequiredElement(), -1);
    if (element.hasStatusElement())
      composeEnum(t, "Appointment", "status", element.getStatusElement(), -1);
  }

  protected void composeAppointmentResponse(Complex parent, String parentType, String name, AppointmentResponse element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "AppointmentResponse", name, element, index);
    for (int i = 0; i < element.getIdentifier().size(); i++)
      composeIdentifier(t, "AppointmentResponse", "identifier", element.getIdentifier().get(i), i);
    if (element.hasAppointment())
      composeReference(t, "AppointmentResponse", "appointment", element.getAppointment(), -1);
    if (element.hasStartElement())
      composeInstant(t, "AppointmentResponse", "start", element.getStartElement(), -1);
    if (element.hasEndElement())
      composeInstant(t, "AppointmentResponse", "end", element.getEndElement(), -1);
    for (int i = 0; i < element.getParticipantType().size(); i++)
      composeCodeableConcept(t, "AppointmentResponse", "participantType", element.getParticipantType().get(i), i);
    if (element.hasActor())
      composeReference(t, "AppointmentResponse", "actor", element.getActor(), -1);
    if (element.hasParticipantStatusElement())
      composeEnum(t, "AppointmentResponse", "participantStatus", element.getParticipantStatusElement(), -1);
    if (element.hasCommentElement())
      composeString(t, "AppointmentResponse", "comment", element.getCommentElement(), -1);
  }

  protected void composeAuditEvent(Complex parent, String parentType, String name, AuditEvent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "AuditEvent", name, element, index);
    if (element.hasType())
      composeCoding(t, "AuditEvent", "type", element.getType(), -1);
    for (int i = 0; i < element.getSubtype().size(); i++)
      composeCoding(t, "AuditEvent", "subtype", element.getSubtype().get(i), i);
    if (element.hasActionElement())
      composeEnum(t, "AuditEvent", "action", element.getActionElement(), -1);
    if (element.hasPeriod())
      composePeriod(t, "AuditEvent", "period", element.getPeriod(), -1);
    if (element.hasRecordedElement())
      composeInstant(t, "AuditEvent", "recorded", element.getRecordedElement(), -1);
    if (element.hasOutcomeElement())
      composeEnum(t, "AuditEvent", "outcome", element.getOutcomeElement(), -1);
    if (element.hasOutcomeDescElement())
      composeString(t, "AuditEvent", "outcomeDesc", element.getOutcomeDescElement(), -1);
    for (int i = 0; i < element.getPurposeOfEvent().size(); i++)
      composeCodeableConcept(t, "AuditEvent", "purposeOfEvent", element.getPurposeOfEvent().get(i), i);
    for (int i = 0; i < element.getAgent().size(); i++)
      composeAuditEventAuditEventAgentComponent(t, "AuditEvent", "agent", element.getAgent().get(i), i);
    if (element.hasSource())
      composeAuditEventAuditEventSourceComponent(t, "AuditEvent", "source", element.getSource(), -1);
    for (int i = 0; i < element.getEntity().size(); i++)
      composeAuditEventAuditEventEntityComponent(t, "AuditEvent", "entity", element.getEntity().get(i), i);
  }

  protected void composeAuditEventAuditEventAgentComponent(Complex parent, String parentType, String name, AuditEvent.AuditEventAgentComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "agent", name, element, index);
    if (element.hasType())
      composeCodeableConcept(t, "AuditEvent", "type", element.getType(), -1);
    for (int i = 0; i < element.getRole().size(); i++)
      composeCodeableConcept(t, "AuditEvent", "role", element.getRole().get(i), i);
    if (element.hasReference())
      composeReference(t, "AuditEvent", "reference", element.getReference(), -1);
    if (element.hasUserId())
      composeIdentifier(t, "AuditEvent", "userId", element.getUserId(), -1);
    if (element.hasAltIdElement())
      composeString(t, "AuditEvent", "altId", element.getAltIdElement(), -1);
    if (element.hasNameElement())
      composeString(t, "AuditEvent", "name", element.getNameElement(), -1);
    if (element.hasRequestorElement())
      composeBoolean(t, "AuditEvent", "requestor", element.getRequestorElement(), -1);
    if (element.hasLocation())
      composeReference(t, "AuditEvent", "location", element.getLocation(), -1);
    for (int i = 0; i < element.getPolicy().size(); i++)
      composeUri(t, "AuditEvent", "policy", element.getPolicy().get(i), i);
    if (element.hasMedia())
      composeCoding(t, "AuditEvent", "media", element.getMedia(), -1);
    if (element.hasNetwork())
      composeAuditEventAuditEventAgentNetworkComponent(t, "AuditEvent", "network", element.getNetwork(), -1);
    for (int i = 0; i < element.getPurposeOfUse().size(); i++)
      composeCodeableConcept(t, "AuditEvent", "purposeOfUse", element.getPurposeOfUse().get(i), i);
  }

  protected void composeAuditEventAuditEventAgentNetworkComponent(Complex parent, String parentType, String name, AuditEvent.AuditEventAgentNetworkComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "network", name, element, index);
    if (element.hasAddressElement())
      composeString(t, "AuditEvent", "address", element.getAddressElement(), -1);
    if (element.hasTypeElement())
      composeEnum(t, "AuditEvent", "type", element.getTypeElement(), -1);
  }

  protected void composeAuditEventAuditEventSourceComponent(Complex parent, String parentType, String name, AuditEvent.AuditEventSourceComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "source", name, element, index);
    if (element.hasSiteElement())
      composeString(t, "AuditEvent", "site", element.getSiteElement(), -1);
    if (element.hasIdentifier())
      composeIdentifier(t, "AuditEvent", "identifier", element.getIdentifier(), -1);
    for (int i = 0; i < element.getType().size(); i++)
      composeCoding(t, "AuditEvent", "type", element.getType().get(i), i);
  }

  protected void composeAuditEventAuditEventEntityComponent(Complex parent, String parentType, String name, AuditEvent.AuditEventEntityComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "entity", name, element, index);
    if (element.hasIdentifier())
      composeIdentifier(t, "AuditEvent", "identifier", element.getIdentifier(), -1);
    if (element.hasReference())
      composeReference(t, "AuditEvent", "reference", element.getReference(), -1);
    if (element.hasType())
      composeCoding(t, "AuditEvent", "type", element.getType(), -1);
    if (element.hasRole())
      composeCoding(t, "AuditEvent", "role", element.getRole(), -1);
    if (element.hasLifecycle())
      composeCoding(t, "AuditEvent", "lifecycle", element.getLifecycle(), -1);
    for (int i = 0; i < element.getSecurityLabel().size(); i++)
      composeCoding(t, "AuditEvent", "securityLabel", element.getSecurityLabel().get(i), i);
    if (element.hasNameElement())
      composeString(t, "AuditEvent", "name", element.getNameElement(), -1);
    if (element.hasDescriptionElement())
      composeString(t, "AuditEvent", "description", element.getDescriptionElement(), -1);
    if (element.hasQueryElement())
      composeBase64Binary(t, "AuditEvent", "query", element.getQueryElement(), -1);
    for (int i = 0; i < element.getDetail().size(); i++)
      composeAuditEventAuditEventEntityDetailComponent(t, "AuditEvent", "detail", element.getDetail().get(i), i);
  }

  protected void composeAuditEventAuditEventEntityDetailComponent(Complex parent, String parentType, String name, AuditEvent.AuditEventEntityDetailComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "detail", name, element, index);
    if (element.hasTypeElement())
      composeString(t, "AuditEvent", "type", element.getTypeElement(), -1);
    if (element.hasValue())
      composeType(t, "AuditEvent", "value", element.getValue(), -1);
  }

  protected void composeBasic(Complex parent, String parentType, String name, Basic element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "Basic", name, element, index);
    for (int i = 0; i < element.getIdentifier().size(); i++)
      composeIdentifier(t, "Basic", "identifier", element.getIdentifier().get(i), i);
    if (element.hasCode())
      composeCodeableConcept(t, "Basic", "code", element.getCode(), -1);
    if (element.hasSubject())
      composeReference(t, "Basic", "subject", element.getSubject(), -1);
    if (element.hasCreatedElement())
      composeDate(t, "Basic", "created", element.getCreatedElement(), -1);
    if (element.hasAuthor())
      composeReference(t, "Basic", "author", element.getAuthor(), -1);
  }

  protected void composeBinary(Complex parent, String parentType, String name, Binary element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeResource(t, "Binary", name, element, index);
    if (element.hasContentTypeElement())
      composeCode(t, "Binary", "contentType", element.getContentTypeElement(), -1);
    if (element.hasSecurityContext())
      composeReference(t, "Binary", "securityContext", element.getSecurityContext(), -1);
    if (element.hasContentElement())
      composeBase64Binary(t, "Binary", "content", element.getContentElement(), -1);
  }

  protected void composeBodyStructure(Complex parent, String parentType, String name, BodyStructure element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "BodyStructure", name, element, index);
    for (int i = 0; i < element.getIdentifier().size(); i++)
      composeIdentifier(t, "BodyStructure", "identifier", element.getIdentifier().get(i), i);
    if (element.hasActiveElement())
      composeBoolean(t, "BodyStructure", "active", element.getActiveElement(), -1);
    if (element.hasMorphology())
      composeCodeableConcept(t, "BodyStructure", "morphology", element.getMorphology(), -1);
    if (element.hasLocation())
      composeCodeableConcept(t, "BodyStructure", "location", element.getLocation(), -1);
    for (int i = 0; i < element.getLocationQualifier().size(); i++)
      composeCodeableConcept(t, "BodyStructure", "locationQualifier", element.getLocationQualifier().get(i), i);
    if (element.hasDescriptionElement())
      composeString(t, "BodyStructure", "description", element.getDescriptionElement(), -1);
    for (int i = 0; i < element.getImage().size(); i++)
      composeAttachment(t, "BodyStructure", "image", element.getImage().get(i), i);
    if (element.hasPatient())
      composeReference(t, "BodyStructure", "patient", element.getPatient(), -1);
  }

  protected void composeBundle(Complex parent, String parentType, String name, Bundle element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeResource(t, "Bundle", name, element, index);
    if (element.hasIdentifier())
      composeIdentifier(t, "Bundle", "identifier", element.getIdentifier(), -1);
    if (element.hasTypeElement())
      composeEnum(t, "Bundle", "type", element.getTypeElement(), -1);
    if (element.hasTotalElement())
      composeUnsignedInt(t, "Bundle", "total", element.getTotalElement(), -1);
    for (int i = 0; i < element.getLink().size(); i++)
      composeBundleBundleLinkComponent(t, "Bundle", "link", element.getLink().get(i), i);
    for (int i = 0; i < element.getEntry().size(); i++)
      composeBundleBundleEntryComponent(t, "Bundle", "entry", element.getEntry().get(i), i);
    if (element.hasSignature())
      composeSignature(t, "Bundle", "signature", element.getSignature(), -1);
  }

  protected void composeBundleBundleLinkComponent(Complex parent, String parentType, String name, Bundle.BundleLinkComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "link", name, element, index);
    if (element.hasRelationElement())
      composeString(t, "Bundle", "relation", element.getRelationElement(), -1);
    if (element.hasUrlElement())
      composeUri(t, "Bundle", "url", element.getUrlElement(), -1);
  }

  protected void composeBundleBundleEntryComponent(Complex parent, String parentType, String name, Bundle.BundleEntryComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "entry", name, element, index);
    for (int i = 0; i < element.getLink().size(); i++)
      composeBundleBundleLinkComponent(t, "Bundle", "link", element.getLink().get(i), i);
    if (element.hasFullUrlElement())
      composeUri(t, "Bundle", "fullUrl", element.getFullUrlElement(), -1);
    if (element.hasResource())
      composeResource(t, "Bundle", "resource", element.getResource(), -1);
    if (element.hasSearch())
      composeBundleBundleEntrySearchComponent(t, "Bundle", "search", element.getSearch(), -1);
    if (element.hasRequest())
      composeBundleBundleEntryRequestComponent(t, "Bundle", "request", element.getRequest(), -1);
    if (element.hasResponse())
      composeBundleBundleEntryResponseComponent(t, "Bundle", "response", element.getResponse(), -1);
  }

  protected void composeBundleBundleEntrySearchComponent(Complex parent, String parentType, String name, Bundle.BundleEntrySearchComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "search", name, element, index);
    if (element.hasModeElement())
      composeEnum(t, "Bundle", "mode", element.getModeElement(), -1);
    if (element.hasScoreElement())
      composeDecimal(t, "Bundle", "score", element.getScoreElement(), -1);
  }

  protected void composeBundleBundleEntryRequestComponent(Complex parent, String parentType, String name, Bundle.BundleEntryRequestComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "request", name, element, index);
    if (element.hasMethodElement())
      composeEnum(t, "Bundle", "method", element.getMethodElement(), -1);
    if (element.hasUrlElement())
      composeUri(t, "Bundle", "url", element.getUrlElement(), -1);
    if (element.hasIfNoneMatchElement())
      composeString(t, "Bundle", "ifNoneMatch", element.getIfNoneMatchElement(), -1);
    if (element.hasIfModifiedSinceElement())
      composeInstant(t, "Bundle", "ifModifiedSince", element.getIfModifiedSinceElement(), -1);
    if (element.hasIfMatchElement())
      composeString(t, "Bundle", "ifMatch", element.getIfMatchElement(), -1);
    if (element.hasIfNoneExistElement())
      composeString(t, "Bundle", "ifNoneExist", element.getIfNoneExistElement(), -1);
  }

  protected void composeBundleBundleEntryResponseComponent(Complex parent, String parentType, String name, Bundle.BundleEntryResponseComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "response", name, element, index);
    if (element.hasStatusElement())
      composeString(t, "Bundle", "status", element.getStatusElement(), -1);
    if (element.hasLocationElement())
      composeUri(t, "Bundle", "location", element.getLocationElement(), -1);
    if (element.hasEtagElement())
      composeString(t, "Bundle", "etag", element.getEtagElement(), -1);
    if (element.hasLastModifiedElement())
      composeInstant(t, "Bundle", "lastModified", element.getLastModifiedElement(), -1);
    if (element.hasOutcome())
      composeResource(t, "Bundle", "outcome", element.getOutcome(), -1);
  }

  protected void composeCapabilityStatement(Complex parent, String parentType, String name, CapabilityStatement element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "CapabilityStatement", name, element, index);
    if (element.hasUrlElement())
      composeUri(t, "CapabilityStatement", "url", element.getUrlElement(), -1);
    if (element.hasVersionElement())
      composeString(t, "CapabilityStatement", "version", element.getVersionElement(), -1);
    if (element.hasNameElement())
      composeString(t, "CapabilityStatement", "name", element.getNameElement(), -1);
    if (element.hasTitleElement())
      composeString(t, "CapabilityStatement", "title", element.getTitleElement(), -1);
    if (element.hasStatusElement())
      composeEnum(t, "CapabilityStatement", "status", element.getStatusElement(), -1);
    if (element.hasExperimentalElement())
      composeBoolean(t, "CapabilityStatement", "experimental", element.getExperimentalElement(), -1);
    if (element.hasDateElement())
      composeDateTime(t, "CapabilityStatement", "date", element.getDateElement(), -1);
    if (element.hasPublisherElement())
      composeString(t, "CapabilityStatement", "publisher", element.getPublisherElement(), -1);
    for (int i = 0; i < element.getContact().size(); i++)
      composeContactDetail(t, "CapabilityStatement", "contact", element.getContact().get(i), i);
    if (element.hasDescriptionElement())
      composeMarkdown(t, "CapabilityStatement", "description", element.getDescriptionElement(), -1);
    for (int i = 0; i < element.getUseContext().size(); i++)
      composeUsageContext(t, "CapabilityStatement", "useContext", element.getUseContext().get(i), i);
    for (int i = 0; i < element.getJurisdiction().size(); i++)
      composeCodeableConcept(t, "CapabilityStatement", "jurisdiction", element.getJurisdiction().get(i), i);
    if (element.hasPurposeElement())
      composeMarkdown(t, "CapabilityStatement", "purpose", element.getPurposeElement(), -1);
    if (element.hasCopyrightElement())
      composeMarkdown(t, "CapabilityStatement", "copyright", element.getCopyrightElement(), -1);
    if (element.hasKindElement())
      composeEnum(t, "CapabilityStatement", "kind", element.getKindElement(), -1);
    for (int i = 0; i < element.getInstantiates().size(); i++)
      composeUri(t, "CapabilityStatement", "instantiates", element.getInstantiates().get(i), i);
    if (element.hasSoftware())
      composeCapabilityStatementCapabilityStatementSoftwareComponent(t, "CapabilityStatement", "software", element.getSoftware(), -1);
    if (element.hasImplementation())
      composeCapabilityStatementCapabilityStatementImplementationComponent(t, "CapabilityStatement", "implementation", element.getImplementation(), -1);
    if (element.hasFhirVersionElement())
      composeId(t, "CapabilityStatement", "fhirVersion", element.getFhirVersionElement(), -1);
    if (element.hasAcceptUnknownElement())
      composeEnum(t, "CapabilityStatement", "acceptUnknown", element.getAcceptUnknownElement(), -1);
    for (int i = 0; i < element.getFormat().size(); i++)
      composeCode(t, "CapabilityStatement", "format", element.getFormat().get(i), i);
    for (int i = 0; i < element.getPatchFormat().size(); i++)
      composeCode(t, "CapabilityStatement", "patchFormat", element.getPatchFormat().get(i), i);
    for (int i = 0; i < element.getImplementationGuide().size(); i++)
      composeUri(t, "CapabilityStatement", "implementationGuide", element.getImplementationGuide().get(i), i);
    for (int i = 0; i < element.getProfile().size(); i++)
      composeReference(t, "CapabilityStatement", "profile", element.getProfile().get(i), i);
    for (int i = 0; i < element.getRest().size(); i++)
      composeCapabilityStatementCapabilityStatementRestComponent(t, "CapabilityStatement", "rest", element.getRest().get(i), i);
    for (int i = 0; i < element.getMessaging().size(); i++)
      composeCapabilityStatementCapabilityStatementMessagingComponent(t, "CapabilityStatement", "messaging", element.getMessaging().get(i), i);
    for (int i = 0; i < element.getDocument().size(); i++)
      composeCapabilityStatementCapabilityStatementDocumentComponent(t, "CapabilityStatement", "document", element.getDocument().get(i), i);
  }

  protected void composeCapabilityStatementCapabilityStatementSoftwareComponent(Complex parent, String parentType, String name, CapabilityStatement.CapabilityStatementSoftwareComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "software", name, element, index);
    if (element.hasNameElement())
      composeString(t, "CapabilityStatement", "name", element.getNameElement(), -1);
    if (element.hasVersionElement())
      composeString(t, "CapabilityStatement", "version", element.getVersionElement(), -1);
    if (element.hasReleaseDateElement())
      composeDateTime(t, "CapabilityStatement", "releaseDate", element.getReleaseDateElement(), -1);
  }

  protected void composeCapabilityStatementCapabilityStatementImplementationComponent(Complex parent, String parentType, String name, CapabilityStatement.CapabilityStatementImplementationComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "implementation", name, element, index);
    if (element.hasDescriptionElement())
      composeString(t, "CapabilityStatement", "description", element.getDescriptionElement(), -1);
    if (element.hasUrlElement())
      composeUri(t, "CapabilityStatement", "url", element.getUrlElement(), -1);
  }

  protected void composeCapabilityStatementCapabilityStatementRestComponent(Complex parent, String parentType, String name, CapabilityStatement.CapabilityStatementRestComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "rest", name, element, index);
    if (element.hasModeElement())
      composeEnum(t, "CapabilityStatement", "mode", element.getModeElement(), -1);
    if (element.hasDocumentationElement())
      composeString(t, "CapabilityStatement", "documentation", element.getDocumentationElement(), -1);
    if (element.hasSecurity())
      composeCapabilityStatementCapabilityStatementRestSecurityComponent(t, "CapabilityStatement", "security", element.getSecurity(), -1);
    for (int i = 0; i < element.getResource().size(); i++)
      composeCapabilityStatementCapabilityStatementRestResourceComponent(t, "CapabilityStatement", "resource", element.getResource().get(i), i);
    for (int i = 0; i < element.getInteraction().size(); i++)
      composeCapabilityStatementSystemInteractionComponent(t, "CapabilityStatement", "interaction", element.getInteraction().get(i), i);
    for (int i = 0; i < element.getSearchParam().size(); i++)
      composeCapabilityStatementCapabilityStatementRestResourceSearchParamComponent(t, "CapabilityStatement", "searchParam", element.getSearchParam().get(i), i);
    for (int i = 0; i < element.getOperation().size(); i++)
      composeCapabilityStatementCapabilityStatementRestOperationComponent(t, "CapabilityStatement", "operation", element.getOperation().get(i), i);
    for (int i = 0; i < element.getCompartment().size(); i++)
      composeUri(t, "CapabilityStatement", "compartment", element.getCompartment().get(i), i);
  }

  protected void composeCapabilityStatementCapabilityStatementRestSecurityComponent(Complex parent, String parentType, String name, CapabilityStatement.CapabilityStatementRestSecurityComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "security", name, element, index);
    if (element.hasCorsElement())
      composeBoolean(t, "CapabilityStatement", "cors", element.getCorsElement(), -1);
    for (int i = 0; i < element.getService().size(); i++)
      composeCodeableConcept(t, "CapabilityStatement", "service", element.getService().get(i), i);
    if (element.hasDescriptionElement())
      composeString(t, "CapabilityStatement", "description", element.getDescriptionElement(), -1);
    for (int i = 0; i < element.getCertificate().size(); i++)
      composeCapabilityStatementCapabilityStatementRestSecurityCertificateComponent(t, "CapabilityStatement", "certificate", element.getCertificate().get(i), i);
  }

  protected void composeCapabilityStatementCapabilityStatementRestSecurityCertificateComponent(Complex parent, String parentType, String name, CapabilityStatement.CapabilityStatementRestSecurityCertificateComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "certificate", name, element, index);
    if (element.hasTypeElement())
      composeCode(t, "CapabilityStatement", "type", element.getTypeElement(), -1);
    if (element.hasBlobElement())
      composeBase64Binary(t, "CapabilityStatement", "blob", element.getBlobElement(), -1);
  }

  protected void composeCapabilityStatementCapabilityStatementRestResourceComponent(Complex parent, String parentType, String name, CapabilityStatement.CapabilityStatementRestResourceComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "resource", name, element, index);
    if (element.hasTypeElement())
      composeCode(t, "CapabilityStatement", "type", element.getTypeElement(), -1);
    if (element.hasProfile())
      composeReference(t, "CapabilityStatement", "profile", element.getProfile(), -1);
    if (element.hasDocumentationElement())
      composeMarkdown(t, "CapabilityStatement", "documentation", element.getDocumentationElement(), -1);
    for (int i = 0; i < element.getInteraction().size(); i++)
      composeCapabilityStatementResourceInteractionComponent(t, "CapabilityStatement", "interaction", element.getInteraction().get(i), i);
    if (element.hasVersioningElement())
      composeEnum(t, "CapabilityStatement", "versioning", element.getVersioningElement(), -1);
    if (element.hasReadHistoryElement())
      composeBoolean(t, "CapabilityStatement", "readHistory", element.getReadHistoryElement(), -1);
    if (element.hasUpdateCreateElement())
      composeBoolean(t, "CapabilityStatement", "updateCreate", element.getUpdateCreateElement(), -1);
    if (element.hasConditionalCreateElement())
      composeBoolean(t, "CapabilityStatement", "conditionalCreate", element.getConditionalCreateElement(), -1);
    if (element.hasConditionalReadElement())
      composeEnum(t, "CapabilityStatement", "conditionalRead", element.getConditionalReadElement(), -1);
    if (element.hasConditionalUpdateElement())
      composeBoolean(t, "CapabilityStatement", "conditionalUpdate", element.getConditionalUpdateElement(), -1);
    if (element.hasConditionalDeleteElement())
      composeEnum(t, "CapabilityStatement", "conditionalDelete", element.getConditionalDeleteElement(), -1);
    for (int i = 0; i < element.getReferencePolicy().size(); i++)
      composeEnum(t, "CapabilityStatement", "referencePolicy", element.getReferencePolicy().get(i), i);
    for (int i = 0; i < element.getSearchInclude().size(); i++)
      composeString(t, "CapabilityStatement", "searchInclude", element.getSearchInclude().get(i), i);
    for (int i = 0; i < element.getSearchRevInclude().size(); i++)
      composeString(t, "CapabilityStatement", "searchRevInclude", element.getSearchRevInclude().get(i), i);
    for (int i = 0; i < element.getSearchParam().size(); i++)
      composeCapabilityStatementCapabilityStatementRestResourceSearchParamComponent(t, "CapabilityStatement", "searchParam", element.getSearchParam().get(i), i);
  }

  protected void composeCapabilityStatementResourceInteractionComponent(Complex parent, String parentType, String name, CapabilityStatement.ResourceInteractionComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "interaction", name, element, index);
    if (element.hasCodeElement())
      composeEnum(t, "CapabilityStatement", "code", element.getCodeElement(), -1);
    if (element.hasDocumentationElement())
      composeString(t, "CapabilityStatement", "documentation", element.getDocumentationElement(), -1);
  }

  protected void composeCapabilityStatementCapabilityStatementRestResourceSearchParamComponent(Complex parent, String parentType, String name, CapabilityStatement.CapabilityStatementRestResourceSearchParamComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "searchParam", name, element, index);
    if (element.hasNameElement())
      composeString(t, "CapabilityStatement", "name", element.getNameElement(), -1);
    if (element.hasDefinitionElement())
      composeUri(t, "CapabilityStatement", "definition", element.getDefinitionElement(), -1);
    if (element.hasTypeElement())
      composeEnum(t, "CapabilityStatement", "type", element.getTypeElement(), -1);
    if (element.hasDocumentationElement())
      composeString(t, "CapabilityStatement", "documentation", element.getDocumentationElement(), -1);
  }

  protected void composeCapabilityStatementSystemInteractionComponent(Complex parent, String parentType, String name, CapabilityStatement.SystemInteractionComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "interaction", name, element, index);
    if (element.hasCodeElement())
      composeEnum(t, "CapabilityStatement", "code", element.getCodeElement(), -1);
    if (element.hasDocumentationElement())
      composeString(t, "CapabilityStatement", "documentation", element.getDocumentationElement(), -1);
  }

  protected void composeCapabilityStatementCapabilityStatementRestOperationComponent(Complex parent, String parentType, String name, CapabilityStatement.CapabilityStatementRestOperationComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "operation", name, element, index);
    if (element.hasNameElement())
      composeString(t, "CapabilityStatement", "name", element.getNameElement(), -1);
    if (element.hasDefinition())
      composeReference(t, "CapabilityStatement", "definition", element.getDefinition(), -1);
  }

  protected void composeCapabilityStatementCapabilityStatementMessagingComponent(Complex parent, String parentType, String name, CapabilityStatement.CapabilityStatementMessagingComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "messaging", name, element, index);
    for (int i = 0; i < element.getEndpoint().size(); i++)
      composeCapabilityStatementCapabilityStatementMessagingEndpointComponent(t, "CapabilityStatement", "endpoint", element.getEndpoint().get(i), i);
    if (element.hasReliableCacheElement())
      composeUnsignedInt(t, "CapabilityStatement", "reliableCache", element.getReliableCacheElement(), -1);
    if (element.hasDocumentationElement())
      composeString(t, "CapabilityStatement", "documentation", element.getDocumentationElement(), -1);
    for (int i = 0; i < element.getSupportedMessage().size(); i++)
      composeCapabilityStatementCapabilityStatementMessagingSupportedMessageComponent(t, "CapabilityStatement", "supportedMessage", element.getSupportedMessage().get(i), i);
    for (int i = 0; i < element.getEvent().size(); i++)
      composeCapabilityStatementCapabilityStatementMessagingEventComponent(t, "CapabilityStatement", "event", element.getEvent().get(i), i);
  }

  protected void composeCapabilityStatementCapabilityStatementMessagingEndpointComponent(Complex parent, String parentType, String name, CapabilityStatement.CapabilityStatementMessagingEndpointComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "endpoint", name, element, index);
    if (element.hasProtocol())
      composeCoding(t, "CapabilityStatement", "protocol", element.getProtocol(), -1);
    if (element.hasAddressElement())
      composeUri(t, "CapabilityStatement", "address", element.getAddressElement(), -1);
  }

  protected void composeCapabilityStatementCapabilityStatementMessagingSupportedMessageComponent(Complex parent, String parentType, String name, CapabilityStatement.CapabilityStatementMessagingSupportedMessageComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "supportedMessage", name, element, index);
    if (element.hasModeElement())
      composeEnum(t, "CapabilityStatement", "mode", element.getModeElement(), -1);
    if (element.hasDefinition())
      composeReference(t, "CapabilityStatement", "definition", element.getDefinition(), -1);
  }

  protected void composeCapabilityStatementCapabilityStatementMessagingEventComponent(Complex parent, String parentType, String name, CapabilityStatement.CapabilityStatementMessagingEventComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "event", name, element, index);
    if (element.hasCode())
      composeCoding(t, "CapabilityStatement", "code", element.getCode(), -1);
    if (element.hasCategoryElement())
      composeEnum(t, "CapabilityStatement", "category", element.getCategoryElement(), -1);
    if (element.hasModeElement())
      composeEnum(t, "CapabilityStatement", "mode", element.getModeElement(), -1);
    if (element.hasFocusElement())
      composeCode(t, "CapabilityStatement", "focus", element.getFocusElement(), -1);
    if (element.hasRequest())
      composeReference(t, "CapabilityStatement", "request", element.getRequest(), -1);
    if (element.hasResponse())
      composeReference(t, "CapabilityStatement", "response", element.getResponse(), -1);
    if (element.hasDocumentationElement())
      composeString(t, "CapabilityStatement", "documentation", element.getDocumentationElement(), -1);
  }

  protected void composeCapabilityStatementCapabilityStatementDocumentComponent(Complex parent, String parentType, String name, CapabilityStatement.CapabilityStatementDocumentComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "document", name, element, index);
    if (element.hasModeElement())
      composeEnum(t, "CapabilityStatement", "mode", element.getModeElement(), -1);
    if (element.hasDocumentationElement())
      composeString(t, "CapabilityStatement", "documentation", element.getDocumentationElement(), -1);
    if (element.hasProfile())
      composeReference(t, "CapabilityStatement", "profile", element.getProfile(), -1);
  }

  protected void composeCarePlan(Complex parent, String parentType, String name, CarePlan element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "CarePlan", name, element, index);
    for (int i = 0; i < element.getIdentifier().size(); i++)
      composeIdentifier(t, "CarePlan", "identifier", element.getIdentifier().get(i), i);
    for (int i = 0; i < element.getDefinition().size(); i++)
      composeReference(t, "CarePlan", "definition", element.getDefinition().get(i), i);
    for (int i = 0; i < element.getBasedOn().size(); i++)
      composeReference(t, "CarePlan", "basedOn", element.getBasedOn().get(i), i);
    for (int i = 0; i < element.getReplaces().size(); i++)
      composeReference(t, "CarePlan", "replaces", element.getReplaces().get(i), i);
    for (int i = 0; i < element.getPartOf().size(); i++)
      composeReference(t, "CarePlan", "partOf", element.getPartOf().get(i), i);
    if (element.hasStatusElement())
      composeEnum(t, "CarePlan", "status", element.getStatusElement(), -1);
    if (element.hasIntentElement())
      composeEnum(t, "CarePlan", "intent", element.getIntentElement(), -1);
    for (int i = 0; i < element.getCategory().size(); i++)
      composeCodeableConcept(t, "CarePlan", "category", element.getCategory().get(i), i);
    if (element.hasTitleElement())
      composeString(t, "CarePlan", "title", element.getTitleElement(), -1);
    if (element.hasDescriptionElement())
      composeString(t, "CarePlan", "description", element.getDescriptionElement(), -1);
    if (element.hasSubject())
      composeReference(t, "CarePlan", "subject", element.getSubject(), -1);
    if (element.hasContext())
      composeReference(t, "CarePlan", "context", element.getContext(), -1);
    if (element.hasPeriod())
      composePeriod(t, "CarePlan", "period", element.getPeriod(), -1);
    for (int i = 0; i < element.getAuthor().size(); i++)
      composeReference(t, "CarePlan", "author", element.getAuthor().get(i), i);
    for (int i = 0; i < element.getCareTeam().size(); i++)
      composeReference(t, "CarePlan", "careTeam", element.getCareTeam().get(i), i);
    for (int i = 0; i < element.getAddresses().size(); i++)
      composeReference(t, "CarePlan", "addresses", element.getAddresses().get(i), i);
    for (int i = 0; i < element.getSupportingInfo().size(); i++)
      composeReference(t, "CarePlan", "supportingInfo", element.getSupportingInfo().get(i), i);
    for (int i = 0; i < element.getGoal().size(); i++)
      composeReference(t, "CarePlan", "goal", element.getGoal().get(i), i);
    for (int i = 0; i < element.getActivity().size(); i++)
      composeCarePlanCarePlanActivityComponent(t, "CarePlan", "activity", element.getActivity().get(i), i);
    for (int i = 0; i < element.getNote().size(); i++)
      composeAnnotation(t, "CarePlan", "note", element.getNote().get(i), i);
  }

  protected void composeCarePlanCarePlanActivityComponent(Complex parent, String parentType, String name, CarePlan.CarePlanActivityComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "activity", name, element, index);
    for (int i = 0; i < element.getOutcomeCodeableConcept().size(); i++)
      composeCodeableConcept(t, "CarePlan", "outcomeCodeableConcept", element.getOutcomeCodeableConcept().get(i), i);
    for (int i = 0; i < element.getOutcomeReference().size(); i++)
      composeReference(t, "CarePlan", "outcomeReference", element.getOutcomeReference().get(i), i);
    for (int i = 0; i < element.getProgress().size(); i++)
      composeAnnotation(t, "CarePlan", "progress", element.getProgress().get(i), i);
    if (element.hasReference())
      composeReference(t, "CarePlan", "reference", element.getReference(), -1);
    if (element.hasDetail())
      composeCarePlanCarePlanActivityDetailComponent(t, "CarePlan", "detail", element.getDetail(), -1);
  }

  protected void composeCarePlanCarePlanActivityDetailComponent(Complex parent, String parentType, String name, CarePlan.CarePlanActivityDetailComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "detail", name, element, index);
    if (element.hasKindElement())
      composeEnum(t, "CarePlan", "kind", element.getKindElement(), -1);
    if (element.hasDefinition())
      composeReference(t, "CarePlan", "definition", element.getDefinition(), -1);
    if (element.hasCode())
      composeCodeableConcept(t, "CarePlan", "code", element.getCode(), -1);
    for (int i = 0; i < element.getReasonCode().size(); i++)
      composeCodeableConcept(t, "CarePlan", "reasonCode", element.getReasonCode().get(i), i);
    for (int i = 0; i < element.getReasonReference().size(); i++)
      composeReference(t, "CarePlan", "reasonReference", element.getReasonReference().get(i), i);
    for (int i = 0; i < element.getGoal().size(); i++)
      composeReference(t, "CarePlan", "goal", element.getGoal().get(i), i);
    if (element.hasStatusElement())
      composeEnum(t, "CarePlan", "status", element.getStatusElement(), -1);
    if (element.hasStatusReasonElement())
      composeString(t, "CarePlan", "statusReason", element.getStatusReasonElement(), -1);
    if (element.hasProhibitedElement())
      composeBoolean(t, "CarePlan", "prohibited", element.getProhibitedElement(), -1);
    if (element.hasScheduled())
      composeType(t, "CarePlan", "scheduled", element.getScheduled(), -1);
    if (element.hasLocation())
      composeReference(t, "CarePlan", "location", element.getLocation(), -1);
    for (int i = 0; i < element.getPerformer().size(); i++)
      composeReference(t, "CarePlan", "performer", element.getPerformer().get(i), i);
    if (element.hasProduct())
      composeType(t, "CarePlan", "product", element.getProduct(), -1);
    if (element.hasDailyAmount())
      composeQuantity(t, "CarePlan", "dailyAmount", element.getDailyAmount(), -1);
    if (element.hasQuantity())
      composeQuantity(t, "CarePlan", "quantity", element.getQuantity(), -1);
    if (element.hasDescriptionElement())
      composeString(t, "CarePlan", "description", element.getDescriptionElement(), -1);
  }

  protected void composeCareTeam(Complex parent, String parentType, String name, CareTeam element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "CareTeam", name, element, index);
    for (int i = 0; i < element.getIdentifier().size(); i++)
      composeIdentifier(t, "CareTeam", "identifier", element.getIdentifier().get(i), i);
    if (element.hasStatusElement())
      composeEnum(t, "CareTeam", "status", element.getStatusElement(), -1);
    for (int i = 0; i < element.getCategory().size(); i++)
      composeCodeableConcept(t, "CareTeam", "category", element.getCategory().get(i), i);
    if (element.hasNameElement())
      composeString(t, "CareTeam", "name", element.getNameElement(), -1);
    if (element.hasSubject())
      composeReference(t, "CareTeam", "subject", element.getSubject(), -1);
    if (element.hasContext())
      composeReference(t, "CareTeam", "context", element.getContext(), -1);
    if (element.hasPeriod())
      composePeriod(t, "CareTeam", "period", element.getPeriod(), -1);
    for (int i = 0; i < element.getParticipant().size(); i++)
      composeCareTeamCareTeamParticipantComponent(t, "CareTeam", "participant", element.getParticipant().get(i), i);
    for (int i = 0; i < element.getReasonCode().size(); i++)
      composeCodeableConcept(t, "CareTeam", "reasonCode", element.getReasonCode().get(i), i);
    for (int i = 0; i < element.getReasonReference().size(); i++)
      composeReference(t, "CareTeam", "reasonReference", element.getReasonReference().get(i), i);
    for (int i = 0; i < element.getManagingOrganization().size(); i++)
      composeReference(t, "CareTeam", "managingOrganization", element.getManagingOrganization().get(i), i);
    for (int i = 0; i < element.getTelecom().size(); i++)
      composeContactPoint(t, "CareTeam", "telecom", element.getTelecom().get(i), i);
    for (int i = 0; i < element.getNote().size(); i++)
      composeAnnotation(t, "CareTeam", "note", element.getNote().get(i), i);
  }

  protected void composeCareTeamCareTeamParticipantComponent(Complex parent, String parentType, String name, CareTeam.CareTeamParticipantComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "participant", name, element, index);
    if (element.hasRole())
      composeCodeableConcept(t, "CareTeam", "role", element.getRole(), -1);
    if (element.hasMember())
      composeReference(t, "CareTeam", "member", element.getMember(), -1);
    if (element.hasOnBehalfOf())
      composeReference(t, "CareTeam", "onBehalfOf", element.getOnBehalfOf(), -1);
    if (element.hasPeriod())
      composePeriod(t, "CareTeam", "period", element.getPeriod(), -1);
  }

  protected void composeCatalogEntry(Complex parent, String parentType, String name, CatalogEntry element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "CatalogEntry", name, element, index);
    if (element.hasType())
      composeCodeableConcept(t, "CatalogEntry", "type", element.getType(), -1);
    if (element.hasPurpose())
      composeCodeableConcept(t, "CatalogEntry", "purpose", element.getPurpose(), -1);
    if (element.hasReferencedItem())
      composeReference(t, "CatalogEntry", "referencedItem", element.getReferencedItem(), -1);
    if (element.hasIdentifier())
      composeIdentifier(t, "CatalogEntry", "identifier", element.getIdentifier(), -1);
    for (int i = 0; i < element.getAdditionalIdentifier().size(); i++)
      composeIdentifier(t, "CatalogEntry", "additionalIdentifier", element.getAdditionalIdentifier().get(i), i);
    for (int i = 0; i < element.getClassification().size(); i++)
      composeIdentifier(t, "CatalogEntry", "classification", element.getClassification().get(i), i);
    if (element.hasStatus())
      composeCodeableConcept(t, "CatalogEntry", "status", element.getStatus(), -1);
    if (element.hasValidityPeriod())
      composePeriod(t, "CatalogEntry", "validityPeriod", element.getValidityPeriod(), -1);
    if (element.hasLastUpdatedElement())
      composeDateTime(t, "CatalogEntry", "lastUpdated", element.getLastUpdatedElement(), -1);
    for (int i = 0; i < element.getAdditionalCharacteristic().size(); i++)
      composeCodeableConcept(t, "CatalogEntry", "additionalCharacteristic", element.getAdditionalCharacteristic().get(i), i);
    for (int i = 0; i < element.getAdditionalClassification().size(); i++)
      composeCodeableConcept(t, "CatalogEntry", "additionalClassification", element.getAdditionalClassification().get(i), i);
    for (int i = 0; i < element.getRelatedItem().size(); i++)
      composeCatalogEntryCatalogEntryRelatedItemComponent(t, "CatalogEntry", "relatedItem", element.getRelatedItem().get(i), i);
  }

  protected void composeCatalogEntryCatalogEntryRelatedItemComponent(Complex parent, String parentType, String name, CatalogEntry.CatalogEntryRelatedItemComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "relatedItem", name, element, index);
    if (element.hasRelationtype())
      composeCodeableConcept(t, "CatalogEntry", "relationtype", element.getRelationtype(), -1);
    if (element.hasType())
      composeCodeableConcept(t, "CatalogEntry", "type", element.getType(), -1);
    if (element.hasItem())
      composeReference(t, "CatalogEntry", "item", element.getItem(), -1);
  }

  protected void composeChargeItem(Complex parent, String parentType, String name, ChargeItem element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "ChargeItem", name, element, index);
    if (element.hasIdentifier())
      composeIdentifier(t, "ChargeItem", "identifier", element.getIdentifier(), -1);
    for (int i = 0; i < element.getDefinition().size(); i++)
      composeUri(t, "ChargeItem", "definition", element.getDefinition().get(i), i);
    if (element.hasStatusElement())
      composeEnum(t, "ChargeItem", "status", element.getStatusElement(), -1);
    for (int i = 0; i < element.getPartOf().size(); i++)
      composeReference(t, "ChargeItem", "partOf", element.getPartOf().get(i), i);
    if (element.hasCode())
      composeCodeableConcept(t, "ChargeItem", "code", element.getCode(), -1);
    if (element.hasSubject())
      composeReference(t, "ChargeItem", "subject", element.getSubject(), -1);
    if (element.hasContext())
      composeReference(t, "ChargeItem", "context", element.getContext(), -1);
    if (element.hasOccurrence())
      composeType(t, "ChargeItem", "occurrence", element.getOccurrence(), -1);
    for (int i = 0; i < element.getParticipant().size(); i++)
      composeChargeItemChargeItemParticipantComponent(t, "ChargeItem", "participant", element.getParticipant().get(i), i);
    if (element.hasPerformingOrganization())
      composeReference(t, "ChargeItem", "performingOrganization", element.getPerformingOrganization(), -1);
    if (element.hasRequestingOrganization())
      composeReference(t, "ChargeItem", "requestingOrganization", element.getRequestingOrganization(), -1);
    if (element.hasQuantity())
      composeQuantity(t, "ChargeItem", "quantity", element.getQuantity(), -1);
    for (int i = 0; i < element.getBodysite().size(); i++)
      composeCodeableConcept(t, "ChargeItem", "bodysite", element.getBodysite().get(i), i);
    if (element.hasFactorOverrideElement())
      composeDecimal(t, "ChargeItem", "factorOverride", element.getFactorOverrideElement(), -1);
    if (element.hasPriceOverride())
      composeMoney(t, "ChargeItem", "priceOverride", element.getPriceOverride(), -1);
    if (element.hasOverrideReasonElement())
      composeString(t, "ChargeItem", "overrideReason", element.getOverrideReasonElement(), -1);
    if (element.hasEnterer())
      composeReference(t, "ChargeItem", "enterer", element.getEnterer(), -1);
    if (element.hasEnteredDateElement())
      composeDateTime(t, "ChargeItem", "enteredDate", element.getEnteredDateElement(), -1);
    for (int i = 0; i < element.getReason().size(); i++)
      composeCodeableConcept(t, "ChargeItem", "reason", element.getReason().get(i), i);
    for (int i = 0; i < element.getService().size(); i++)
      composeReference(t, "ChargeItem", "service", element.getService().get(i), i);
    for (int i = 0; i < element.getAccount().size(); i++)
      composeReference(t, "ChargeItem", "account", element.getAccount().get(i), i);
    for (int i = 0; i < element.getNote().size(); i++)
      composeAnnotation(t, "ChargeItem", "note", element.getNote().get(i), i);
    for (int i = 0; i < element.getSupportingInformation().size(); i++)
      composeReference(t, "ChargeItem", "supportingInformation", element.getSupportingInformation().get(i), i);
  }

  protected void composeChargeItemChargeItemParticipantComponent(Complex parent, String parentType, String name, ChargeItem.ChargeItemParticipantComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "participant", name, element, index);
    if (element.hasRole())
      composeCodeableConcept(t, "ChargeItem", "role", element.getRole(), -1);
    if (element.hasActor())
      composeReference(t, "ChargeItem", "actor", element.getActor(), -1);
  }

  protected void composeClaim(Complex parent, String parentType, String name, Claim element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "Claim", name, element, index);
    for (int i = 0; i < element.getIdentifier().size(); i++)
      composeIdentifier(t, "Claim", "identifier", element.getIdentifier().get(i), i);
    if (element.hasStatusElement())
      composeEnum(t, "Claim", "status", element.getStatusElement(), -1);
    if (element.hasType())
      composeCodeableConcept(t, "Claim", "type", element.getType(), -1);
    for (int i = 0; i < element.getSubType().size(); i++)
      composeCodeableConcept(t, "Claim", "subType", element.getSubType().get(i), i);
    if (element.hasUseElement())
      composeEnum(t, "Claim", "use", element.getUseElement(), -1);
    if (element.hasPatient())
      composeReference(t, "Claim", "patient", element.getPatient(), -1);
    if (element.hasBillablePeriod())
      composePeriod(t, "Claim", "billablePeriod", element.getBillablePeriod(), -1);
    if (element.hasCreatedElement())
      composeDateTime(t, "Claim", "created", element.getCreatedElement(), -1);
    if (element.hasEnterer())
      composeReference(t, "Claim", "enterer", element.getEnterer(), -1);
    if (element.hasInsurer())
      composeReference(t, "Claim", "insurer", element.getInsurer(), -1);
    if (element.hasProvider())
      composeReference(t, "Claim", "provider", element.getProvider(), -1);
    if (element.hasOrganization())
      composeReference(t, "Claim", "organization", element.getOrganization(), -1);
    if (element.hasPriority())
      composeCodeableConcept(t, "Claim", "priority", element.getPriority(), -1);
    if (element.hasFundsReserve())
      composeCodeableConcept(t, "Claim", "fundsReserve", element.getFundsReserve(), -1);
    for (int i = 0; i < element.getRelated().size(); i++)
      composeClaimRelatedClaimComponent(t, "Claim", "related", element.getRelated().get(i), i);
    if (element.hasPrescription())
      composeReference(t, "Claim", "prescription", element.getPrescription(), -1);
    if (element.hasOriginalPrescription())
      composeReference(t, "Claim", "originalPrescription", element.getOriginalPrescription(), -1);
    if (element.hasPayee())
      composeClaimPayeeComponent(t, "Claim", "payee", element.getPayee(), -1);
    if (element.hasReferral())
      composeReference(t, "Claim", "referral", element.getReferral(), -1);
    if (element.hasFacility())
      composeReference(t, "Claim", "facility", element.getFacility(), -1);
    for (int i = 0; i < element.getCareTeam().size(); i++)
      composeClaimCareTeamComponent(t, "Claim", "careTeam", element.getCareTeam().get(i), i);
    for (int i = 0; i < element.getInformation().size(); i++)
      composeClaimSpecialConditionComponent(t, "Claim", "information", element.getInformation().get(i), i);
    for (int i = 0; i < element.getDiagnosis().size(); i++)
      composeClaimDiagnosisComponent(t, "Claim", "diagnosis", element.getDiagnosis().get(i), i);
    for (int i = 0; i < element.getProcedure().size(); i++)
      composeClaimProcedureComponent(t, "Claim", "procedure", element.getProcedure().get(i), i);
    for (int i = 0; i < element.getInsurance().size(); i++)
      composeClaimInsuranceComponent(t, "Claim", "insurance", element.getInsurance().get(i), i);
    if (element.hasAccident())
      composeClaimAccidentComponent(t, "Claim", "accident", element.getAccident(), -1);
    if (element.hasEmploymentImpacted())
      composePeriod(t, "Claim", "employmentImpacted", element.getEmploymentImpacted(), -1);
    if (element.hasHospitalization())
      composePeriod(t, "Claim", "hospitalization", element.getHospitalization(), -1);
    for (int i = 0; i < element.getItem().size(); i++)
      composeClaimItemComponent(t, "Claim", "item", element.getItem().get(i), i);
    if (element.hasTotal())
      composeMoney(t, "Claim", "total", element.getTotal(), -1);
  }

  protected void composeClaimRelatedClaimComponent(Complex parent, String parentType, String name, Claim.RelatedClaimComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "related", name, element, index);
    if (element.hasClaim())
      composeReference(t, "Claim", "claim", element.getClaim(), -1);
    if (element.hasRelationship())
      composeCodeableConcept(t, "Claim", "relationship", element.getRelationship(), -1);
    if (element.hasReference())
      composeIdentifier(t, "Claim", "reference", element.getReference(), -1);
  }

  protected void composeClaimPayeeComponent(Complex parent, String parentType, String name, Claim.PayeeComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "payee", name, element, index);
    if (element.hasType())
      composeCodeableConcept(t, "Claim", "type", element.getType(), -1);
    if (element.hasResource())
      composeCoding(t, "Claim", "resource", element.getResource(), -1);
    if (element.hasParty())
      composeReference(t, "Claim", "party", element.getParty(), -1);
  }

  protected void composeClaimCareTeamComponent(Complex parent, String parentType, String name, Claim.CareTeamComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "careTeam", name, element, index);
    if (element.hasSequenceElement())
      composePositiveInt(t, "Claim", "sequence", element.getSequenceElement(), -1);
    if (element.hasProvider())
      composeReference(t, "Claim", "provider", element.getProvider(), -1);
    if (element.hasResponsibleElement())
      composeBoolean(t, "Claim", "responsible", element.getResponsibleElement(), -1);
    if (element.hasRole())
      composeCodeableConcept(t, "Claim", "role", element.getRole(), -1);
    if (element.hasQualification())
      composeCodeableConcept(t, "Claim", "qualification", element.getQualification(), -1);
  }

  protected void composeClaimSpecialConditionComponent(Complex parent, String parentType, String name, Claim.SpecialConditionComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "information", name, element, index);
    if (element.hasSequenceElement())
      composePositiveInt(t, "Claim", "sequence", element.getSequenceElement(), -1);
    if (element.hasCategory())
      composeCodeableConcept(t, "Claim", "category", element.getCategory(), -1);
    if (element.hasCode())
      composeCodeableConcept(t, "Claim", "code", element.getCode(), -1);
    if (element.hasTiming())
      composeType(t, "Claim", "timing", element.getTiming(), -1);
    if (element.hasValue())
      composeType(t, "Claim", "value", element.getValue(), -1);
    if (element.hasReason())
      composeCodeableConcept(t, "Claim", "reason", element.getReason(), -1);
  }

  protected void composeClaimDiagnosisComponent(Complex parent, String parentType, String name, Claim.DiagnosisComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "diagnosis", name, element, index);
    if (element.hasSequenceElement())
      composePositiveInt(t, "Claim", "sequence", element.getSequenceElement(), -1);
    if (element.hasDiagnosis())
      composeType(t, "Claim", "diagnosis", element.getDiagnosis(), -1);
    for (int i = 0; i < element.getType().size(); i++)
      composeCodeableConcept(t, "Claim", "type", element.getType().get(i), i);
    if (element.hasPackageCode())
      composeCodeableConcept(t, "Claim", "packageCode", element.getPackageCode(), -1);
  }

  protected void composeClaimProcedureComponent(Complex parent, String parentType, String name, Claim.ProcedureComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "procedure", name, element, index);
    if (element.hasSequenceElement())
      composePositiveInt(t, "Claim", "sequence", element.getSequenceElement(), -1);
    if (element.hasDateElement())
      composeDateTime(t, "Claim", "date", element.getDateElement(), -1);
    if (element.hasProcedure())
      composeType(t, "Claim", "procedure", element.getProcedure(), -1);
  }

  protected void composeClaimInsuranceComponent(Complex parent, String parentType, String name, Claim.InsuranceComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "insurance", name, element, index);
    if (element.hasSequenceElement())
      composePositiveInt(t, "Claim", "sequence", element.getSequenceElement(), -1);
    if (element.hasFocalElement())
      composeBoolean(t, "Claim", "focal", element.getFocalElement(), -1);
    if (element.hasIdentifier())
      composeIdentifier(t, "Claim", "identifier", element.getIdentifier(), -1);
    if (element.hasCoverage())
      composeReference(t, "Claim", "coverage", element.getCoverage(), -1);
    if (element.hasBusinessArrangementElement())
      composeString(t, "Claim", "businessArrangement", element.getBusinessArrangementElement(), -1);
    for (int i = 0; i < element.getPreAuthRef().size(); i++)
      composeString(t, "Claim", "preAuthRef", element.getPreAuthRef().get(i), i);
    if (element.hasClaimResponse())
      composeReference(t, "Claim", "claimResponse", element.getClaimResponse(), -1);
  }

  protected void composeClaimAccidentComponent(Complex parent, String parentType, String name, Claim.AccidentComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "accident", name, element, index);
    if (element.hasDateElement())
      composeDate(t, "Claim", "date", element.getDateElement(), -1);
    if (element.hasType())
      composeCodeableConcept(t, "Claim", "type", element.getType(), -1);
    if (element.hasLocation())
      composeType(t, "Claim", "location", element.getLocation(), -1);
  }

  protected void composeClaimItemComponent(Complex parent, String parentType, String name, Claim.ItemComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "item", name, element, index);
    if (element.hasSequenceElement())
      composePositiveInt(t, "Claim", "sequence", element.getSequenceElement(), -1);
    for (int i = 0; i < element.getCareTeamLinkId().size(); i++)
      composePositiveInt(t, "Claim", "careTeamLinkId", element.getCareTeamLinkId().get(i), i);
    for (int i = 0; i < element.getDiagnosisLinkId().size(); i++)
      composePositiveInt(t, "Claim", "diagnosisLinkId", element.getDiagnosisLinkId().get(i), i);
    for (int i = 0; i < element.getProcedureLinkId().size(); i++)
      composePositiveInt(t, "Claim", "procedureLinkId", element.getProcedureLinkId().get(i), i);
    for (int i = 0; i < element.getInformationLinkId().size(); i++)
      composePositiveInt(t, "Claim", "informationLinkId", element.getInformationLinkId().get(i), i);
    if (element.hasRevenue())
      composeCodeableConcept(t, "Claim", "revenue", element.getRevenue(), -1);
    if (element.hasCategory())
      composeCodeableConcept(t, "Claim", "category", element.getCategory(), -1);
    if (element.hasService())
      composeCodeableConcept(t, "Claim", "service", element.getService(), -1);
    for (int i = 0; i < element.getModifier().size(); i++)
      composeCodeableConcept(t, "Claim", "modifier", element.getModifier().get(i), i);
    for (int i = 0; i < element.getProgramCode().size(); i++)
      composeCodeableConcept(t, "Claim", "programCode", element.getProgramCode().get(i), i);
    if (element.hasServiced())
      composeType(t, "Claim", "serviced", element.getServiced(), -1);
    if (element.hasLocation())
      composeType(t, "Claim", "location", element.getLocation(), -1);
    if (element.hasQuantity())
      composeQuantity(t, "Claim", "quantity", element.getQuantity(), -1);
    if (element.hasUnitPrice())
      composeMoney(t, "Claim", "unitPrice", element.getUnitPrice(), -1);
    if (element.hasFactorElement())
      composeDecimal(t, "Claim", "factor", element.getFactorElement(), -1);
    if (element.hasNet())
      composeMoney(t, "Claim", "net", element.getNet(), -1);
    for (int i = 0; i < element.getUdi().size(); i++)
      composeReference(t, "Claim", "udi", element.getUdi().get(i), i);
    if (element.hasBodySite())
      composeCodeableConcept(t, "Claim", "bodySite", element.getBodySite(), -1);
    for (int i = 0; i < element.getSubSite().size(); i++)
      composeCodeableConcept(t, "Claim", "subSite", element.getSubSite().get(i), i);
    for (int i = 0; i < element.getEncounter().size(); i++)
      composeReference(t, "Claim", "encounter", element.getEncounter().get(i), i);
    for (int i = 0; i < element.getDetail().size(); i++)
      composeClaimDetailComponent(t, "Claim", "detail", element.getDetail().get(i), i);
  }

  protected void composeClaimDetailComponent(Complex parent, String parentType, String name, Claim.DetailComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "detail", name, element, index);
    if (element.hasSequenceElement())
      composePositiveInt(t, "Claim", "sequence", element.getSequenceElement(), -1);
    if (element.hasRevenue())
      composeCodeableConcept(t, "Claim", "revenue", element.getRevenue(), -1);
    if (element.hasCategory())
      composeCodeableConcept(t, "Claim", "category", element.getCategory(), -1);
    if (element.hasService())
      composeCodeableConcept(t, "Claim", "service", element.getService(), -1);
    for (int i = 0; i < element.getModifier().size(); i++)
      composeCodeableConcept(t, "Claim", "modifier", element.getModifier().get(i), i);
    for (int i = 0; i < element.getProgramCode().size(); i++)
      composeCodeableConcept(t, "Claim", "programCode", element.getProgramCode().get(i), i);
    if (element.hasQuantity())
      composeQuantity(t, "Claim", "quantity", element.getQuantity(), -1);
    if (element.hasUnitPrice())
      composeMoney(t, "Claim", "unitPrice", element.getUnitPrice(), -1);
    if (element.hasFactorElement())
      composeDecimal(t, "Claim", "factor", element.getFactorElement(), -1);
    if (element.hasNet())
      composeMoney(t, "Claim", "net", element.getNet(), -1);
    for (int i = 0; i < element.getUdi().size(); i++)
      composeReference(t, "Claim", "udi", element.getUdi().get(i), i);
    for (int i = 0; i < element.getSubDetail().size(); i++)
      composeClaimSubDetailComponent(t, "Claim", "subDetail", element.getSubDetail().get(i), i);
  }

  protected void composeClaimSubDetailComponent(Complex parent, String parentType, String name, Claim.SubDetailComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "subDetail", name, element, index);
    if (element.hasSequenceElement())
      composePositiveInt(t, "Claim", "sequence", element.getSequenceElement(), -1);
    if (element.hasRevenue())
      composeCodeableConcept(t, "Claim", "revenue", element.getRevenue(), -1);
    if (element.hasCategory())
      composeCodeableConcept(t, "Claim", "category", element.getCategory(), -1);
    if (element.hasService())
      composeCodeableConcept(t, "Claim", "service", element.getService(), -1);
    for (int i = 0; i < element.getModifier().size(); i++)
      composeCodeableConcept(t, "Claim", "modifier", element.getModifier().get(i), i);
    for (int i = 0; i < element.getProgramCode().size(); i++)
      composeCodeableConcept(t, "Claim", "programCode", element.getProgramCode().get(i), i);
    if (element.hasQuantity())
      composeQuantity(t, "Claim", "quantity", element.getQuantity(), -1);
    if (element.hasUnitPrice())
      composeMoney(t, "Claim", "unitPrice", element.getUnitPrice(), -1);
    if (element.hasFactorElement())
      composeDecimal(t, "Claim", "factor", element.getFactorElement(), -1);
    if (element.hasNet())
      composeMoney(t, "Claim", "net", element.getNet(), -1);
    for (int i = 0; i < element.getUdi().size(); i++)
      composeReference(t, "Claim", "udi", element.getUdi().get(i), i);
  }

  protected void composeClaimResponse(Complex parent, String parentType, String name, ClaimResponse element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "ClaimResponse", name, element, index);
    for (int i = 0; i < element.getIdentifier().size(); i++)
      composeIdentifier(t, "ClaimResponse", "identifier", element.getIdentifier().get(i), i);
    if (element.hasStatusElement())
      composeEnum(t, "ClaimResponse", "status", element.getStatusElement(), -1);
    if (element.hasPatient())
      composeReference(t, "ClaimResponse", "patient", element.getPatient(), -1);
    if (element.hasCreatedElement())
      composeDateTime(t, "ClaimResponse", "created", element.getCreatedElement(), -1);
    if (element.hasInsurer())
      composeReference(t, "ClaimResponse", "insurer", element.getInsurer(), -1);
    if (element.hasRequestProvider())
      composeReference(t, "ClaimResponse", "requestProvider", element.getRequestProvider(), -1);
    if (element.hasRequestOrganization())
      composeReference(t, "ClaimResponse", "requestOrganization", element.getRequestOrganization(), -1);
    if (element.hasRequest())
      composeReference(t, "ClaimResponse", "request", element.getRequest(), -1);
    if (element.hasOutcomeElement())
      composeEnum(t, "ClaimResponse", "outcome", element.getOutcomeElement(), -1);
    if (element.hasDispositionElement())
      composeString(t, "ClaimResponse", "disposition", element.getDispositionElement(), -1);
    if (element.hasPayeeType())
      composeCodeableConcept(t, "ClaimResponse", "payeeType", element.getPayeeType(), -1);
    for (int i = 0; i < element.getItem().size(); i++)
      composeClaimResponseItemComponent(t, "ClaimResponse", "item", element.getItem().get(i), i);
    for (int i = 0; i < element.getAddItem().size(); i++)
      composeClaimResponseAddedItemComponent(t, "ClaimResponse", "addItem", element.getAddItem().get(i), i);
    for (int i = 0; i < element.getError().size(); i++)
      composeClaimResponseErrorComponent(t, "ClaimResponse", "error", element.getError().get(i), i);
    if (element.hasTotalCost())
      composeMoney(t, "ClaimResponse", "totalCost", element.getTotalCost(), -1);
    if (element.hasUnallocDeductable())
      composeMoney(t, "ClaimResponse", "unallocDeductable", element.getUnallocDeductable(), -1);
    if (element.hasTotalBenefit())
      composeMoney(t, "ClaimResponse", "totalBenefit", element.getTotalBenefit(), -1);
    if (element.hasPayment())
      composeClaimResponsePaymentComponent(t, "ClaimResponse", "payment", element.getPayment(), -1);
    if (element.hasReserved())
      composeCoding(t, "ClaimResponse", "reserved", element.getReserved(), -1);
    if (element.hasForm())
      composeCodeableConcept(t, "ClaimResponse", "form", element.getForm(), -1);
    for (int i = 0; i < element.getProcessNote().size(); i++)
      composeClaimResponseNoteComponent(t, "ClaimResponse", "processNote", element.getProcessNote().get(i), i);
    for (int i = 0; i < element.getCommunicationRequest().size(); i++)
      composeReference(t, "ClaimResponse", "communicationRequest", element.getCommunicationRequest().get(i), i);
    for (int i = 0; i < element.getInsurance().size(); i++)
      composeClaimResponseInsuranceComponent(t, "ClaimResponse", "insurance", element.getInsurance().get(i), i);
  }

  protected void composeClaimResponseItemComponent(Complex parent, String parentType, String name, ClaimResponse.ItemComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "item", name, element, index);
    if (element.hasSequenceLinkIdElement())
      composePositiveInt(t, "ClaimResponse", "sequenceLinkId", element.getSequenceLinkIdElement(), -1);
    for (int i = 0; i < element.getNoteNumber().size(); i++)
      composePositiveInt(t, "ClaimResponse", "noteNumber", element.getNoteNumber().get(i), i);
    for (int i = 0; i < element.getAdjudication().size(); i++)
      composeClaimResponseAdjudicationComponent(t, "ClaimResponse", "adjudication", element.getAdjudication().get(i), i);
    for (int i = 0; i < element.getDetail().size(); i++)
      composeClaimResponseItemDetailComponent(t, "ClaimResponse", "detail", element.getDetail().get(i), i);
  }

  protected void composeClaimResponseAdjudicationComponent(Complex parent, String parentType, String name, ClaimResponse.AdjudicationComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "adjudication", name, element, index);
    if (element.hasCategory())
      composeCodeableConcept(t, "ClaimResponse", "category", element.getCategory(), -1);
    if (element.hasReason())
      composeCodeableConcept(t, "ClaimResponse", "reason", element.getReason(), -1);
    if (element.hasAmount())
      composeMoney(t, "ClaimResponse", "amount", element.getAmount(), -1);
    if (element.hasValueElement())
      composeDecimal(t, "ClaimResponse", "value", element.getValueElement(), -1);
  }

  protected void composeClaimResponseItemDetailComponent(Complex parent, String parentType, String name, ClaimResponse.ItemDetailComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "detail", name, element, index);
    if (element.hasSequenceLinkIdElement())
      composePositiveInt(t, "ClaimResponse", "sequenceLinkId", element.getSequenceLinkIdElement(), -1);
    for (int i = 0; i < element.getNoteNumber().size(); i++)
      composePositiveInt(t, "ClaimResponse", "noteNumber", element.getNoteNumber().get(i), i);
    for (int i = 0; i < element.getAdjudication().size(); i++)
      composeClaimResponseAdjudicationComponent(t, "ClaimResponse", "adjudication", element.getAdjudication().get(i), i);
    for (int i = 0; i < element.getSubDetail().size(); i++)
      composeClaimResponseSubDetailComponent(t, "ClaimResponse", "subDetail", element.getSubDetail().get(i), i);
  }

  protected void composeClaimResponseSubDetailComponent(Complex parent, String parentType, String name, ClaimResponse.SubDetailComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "subDetail", name, element, index);
    if (element.hasSequenceLinkIdElement())
      composePositiveInt(t, "ClaimResponse", "sequenceLinkId", element.getSequenceLinkIdElement(), -1);
    for (int i = 0; i < element.getNoteNumber().size(); i++)
      composePositiveInt(t, "ClaimResponse", "noteNumber", element.getNoteNumber().get(i), i);
    for (int i = 0; i < element.getAdjudication().size(); i++)
      composeClaimResponseAdjudicationComponent(t, "ClaimResponse", "adjudication", element.getAdjudication().get(i), i);
  }

  protected void composeClaimResponseAddedItemComponent(Complex parent, String parentType, String name, ClaimResponse.AddedItemComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "addItem", name, element, index);
    for (int i = 0; i < element.getSequenceLinkId().size(); i++)
      composePositiveInt(t, "ClaimResponse", "sequenceLinkId", element.getSequenceLinkId().get(i), i);
    for (int i = 0; i < element.getDetailSequenceLinkId().size(); i++)
      composePositiveInt(t, "ClaimResponse", "detailSequenceLinkId", element.getDetailSequenceLinkId().get(i), i);
    for (int i = 0; i < element.getSubdetailSequenceLinkId().size(); i++)
      composePositiveInt(t, "ClaimResponse", "subdetailSequenceLinkId", element.getSubdetailSequenceLinkId().get(i), i);
    if (element.hasService())
      composeCodeableConcept(t, "ClaimResponse", "service", element.getService(), -1);
    for (int i = 0; i < element.getModifier().size(); i++)
      composeCodeableConcept(t, "ClaimResponse", "modifier", element.getModifier().get(i), i);
    if (element.hasFee())
      composeMoney(t, "ClaimResponse", "fee", element.getFee(), -1);
    for (int i = 0; i < element.getNoteNumber().size(); i++)
      composePositiveInt(t, "ClaimResponse", "noteNumber", element.getNoteNumber().get(i), i);
    for (int i = 0; i < element.getAdjudication().size(); i++)
      composeClaimResponseAdjudicationComponent(t, "ClaimResponse", "adjudication", element.getAdjudication().get(i), i);
  }

  protected void composeClaimResponseErrorComponent(Complex parent, String parentType, String name, ClaimResponse.ErrorComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "error", name, element, index);
    if (element.hasSequenceLinkIdElement())
      composePositiveInt(t, "ClaimResponse", "sequenceLinkId", element.getSequenceLinkIdElement(), -1);
    if (element.hasDetailSequenceLinkIdElement())
      composePositiveInt(t, "ClaimResponse", "detailSequenceLinkId", element.getDetailSequenceLinkIdElement(), -1);
    if (element.hasSubdetailSequenceLinkIdElement())
      composePositiveInt(t, "ClaimResponse", "subdetailSequenceLinkId", element.getSubdetailSequenceLinkIdElement(), -1);
    if (element.hasCode())
      composeCodeableConcept(t, "ClaimResponse", "code", element.getCode(), -1);
  }

  protected void composeClaimResponsePaymentComponent(Complex parent, String parentType, String name, ClaimResponse.PaymentComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "payment", name, element, index);
    if (element.hasType())
      composeCodeableConcept(t, "ClaimResponse", "type", element.getType(), -1);
    if (element.hasAdjustment())
      composeMoney(t, "ClaimResponse", "adjustment", element.getAdjustment(), -1);
    if (element.hasAdjustmentReason())
      composeCodeableConcept(t, "ClaimResponse", "adjustmentReason", element.getAdjustmentReason(), -1);
    if (element.hasDateElement())
      composeDate(t, "ClaimResponse", "date", element.getDateElement(), -1);
    if (element.hasAmount())
      composeMoney(t, "ClaimResponse", "amount", element.getAmount(), -1);
    if (element.hasIdentifier())
      composeIdentifier(t, "ClaimResponse", "identifier", element.getIdentifier(), -1);
  }

  protected void composeClaimResponseNoteComponent(Complex parent, String parentType, String name, ClaimResponse.NoteComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "processNote", name, element, index);
    if (element.hasNumberElement())
      composePositiveInt(t, "ClaimResponse", "number", element.getNumberElement(), -1);
    if (element.hasTypeElement())
      composeEnum(t, "ClaimResponse", "type", element.getTypeElement(), -1);
    if (element.hasTextElement())
      composeString(t, "ClaimResponse", "text", element.getTextElement(), -1);
    if (element.hasLanguage())
      composeCodeableConcept(t, "ClaimResponse", "language", element.getLanguage(), -1);
  }

  protected void composeClaimResponseInsuranceComponent(Complex parent, String parentType, String name, ClaimResponse.InsuranceComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "insurance", name, element, index);
    if (element.hasSequenceElement())
      composePositiveInt(t, "ClaimResponse", "sequence", element.getSequenceElement(), -1);
    if (element.hasFocalElement())
      composeBoolean(t, "ClaimResponse", "focal", element.getFocalElement(), -1);
    if (element.hasCoverage())
      composeReference(t, "ClaimResponse", "coverage", element.getCoverage(), -1);
    if (element.hasBusinessArrangementElement())
      composeString(t, "ClaimResponse", "businessArrangement", element.getBusinessArrangementElement(), -1);
    for (int i = 0; i < element.getPreAuthRef().size(); i++)
      composeString(t, "ClaimResponse", "preAuthRef", element.getPreAuthRef().get(i), i);
    if (element.hasClaimResponse())
      composeReference(t, "ClaimResponse", "claimResponse", element.getClaimResponse(), -1);
  }

  protected void composeClinicalImpression(Complex parent, String parentType, String name, ClinicalImpression element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "ClinicalImpression", name, element, index);
    for (int i = 0; i < element.getIdentifier().size(); i++)
      composeIdentifier(t, "ClinicalImpression", "identifier", element.getIdentifier().get(i), i);
    if (element.hasStatusElement())
      composeEnum(t, "ClinicalImpression", "status", element.getStatusElement(), -1);
    if (element.hasCode())
      composeCodeableConcept(t, "ClinicalImpression", "code", element.getCode(), -1);
    if (element.hasDescriptionElement())
      composeString(t, "ClinicalImpression", "description", element.getDescriptionElement(), -1);
    if (element.hasSubject())
      composeReference(t, "ClinicalImpression", "subject", element.getSubject(), -1);
    if (element.hasContext())
      composeReference(t, "ClinicalImpression", "context", element.getContext(), -1);
    if (element.hasEffective())
      composeType(t, "ClinicalImpression", "effective", element.getEffective(), -1);
    if (element.hasDateElement())
      composeDateTime(t, "ClinicalImpression", "date", element.getDateElement(), -1);
    if (element.hasAssessor())
      composeReference(t, "ClinicalImpression", "assessor", element.getAssessor(), -1);
    if (element.hasPrevious())
      composeReference(t, "ClinicalImpression", "previous", element.getPrevious(), -1);
    for (int i = 0; i < element.getProblem().size(); i++)
      composeReference(t, "ClinicalImpression", "problem", element.getProblem().get(i), i);
    for (int i = 0; i < element.getInvestigation().size(); i++)
      composeClinicalImpressionClinicalImpressionInvestigationComponent(t, "ClinicalImpression", "investigation", element.getInvestigation().get(i), i);
    for (int i = 0; i < element.getProtocol().size(); i++)
      composeUri(t, "ClinicalImpression", "protocol", element.getProtocol().get(i), i);
    if (element.hasSummaryElement())
      composeString(t, "ClinicalImpression", "summary", element.getSummaryElement(), -1);
    for (int i = 0; i < element.getFinding().size(); i++)
      composeClinicalImpressionClinicalImpressionFindingComponent(t, "ClinicalImpression", "finding", element.getFinding().get(i), i);
    for (int i = 0; i < element.getPrognosisCodeableConcept().size(); i++)
      composeCodeableConcept(t, "ClinicalImpression", "prognosisCodeableConcept", element.getPrognosisCodeableConcept().get(i), i);
    for (int i = 0; i < element.getPrognosisReference().size(); i++)
      composeReference(t, "ClinicalImpression", "prognosisReference", element.getPrognosisReference().get(i), i);
    for (int i = 0; i < element.getAction().size(); i++)
      composeReference(t, "ClinicalImpression", "action", element.getAction().get(i), i);
    for (int i = 0; i < element.getNote().size(); i++)
      composeAnnotation(t, "ClinicalImpression", "note", element.getNote().get(i), i);
  }

  protected void composeClinicalImpressionClinicalImpressionInvestigationComponent(Complex parent, String parentType, String name, ClinicalImpression.ClinicalImpressionInvestigationComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "investigation", name, element, index);
    if (element.hasCode())
      composeCodeableConcept(t, "ClinicalImpression", "code", element.getCode(), -1);
    for (int i = 0; i < element.getItem().size(); i++)
      composeReference(t, "ClinicalImpression", "item", element.getItem().get(i), i);
  }

  protected void composeClinicalImpressionClinicalImpressionFindingComponent(Complex parent, String parentType, String name, ClinicalImpression.ClinicalImpressionFindingComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "finding", name, element, index);
    if (element.hasItem())
      composeType(t, "ClinicalImpression", "item", element.getItem(), -1);
    if (element.hasBasisElement())
      composeString(t, "ClinicalImpression", "basis", element.getBasisElement(), -1);
  }

  protected void composeCodeSystem(Complex parent, String parentType, String name, CodeSystem element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "CodeSystem", name, element, index);
    if (element.hasUrlElement())
      composeUri(t, "CodeSystem", "url", element.getUrlElement(), -1);
    if (element.hasIdentifier())
      composeIdentifier(t, "CodeSystem", "identifier", element.getIdentifier(), -1);
    if (element.hasVersionElement())
      composeString(t, "CodeSystem", "version", element.getVersionElement(), -1);
    if (element.hasNameElement())
      composeString(t, "CodeSystem", "name", element.getNameElement(), -1);
    if (element.hasTitleElement())
      composeString(t, "CodeSystem", "title", element.getTitleElement(), -1);
    if (element.hasStatusElement())
      composeEnum(t, "CodeSystem", "status", element.getStatusElement(), -1);
    if (element.hasExperimentalElement())
      composeBoolean(t, "CodeSystem", "experimental", element.getExperimentalElement(), -1);
    if (element.hasDateElement())
      composeDateTime(t, "CodeSystem", "date", element.getDateElement(), -1);
    if (element.hasPublisherElement())
      composeString(t, "CodeSystem", "publisher", element.getPublisherElement(), -1);
    for (int i = 0; i < element.getContact().size(); i++)
      composeContactDetail(t, "CodeSystem", "contact", element.getContact().get(i), i);
    if (element.hasDescriptionElement())
      composeMarkdown(t, "CodeSystem", "description", element.getDescriptionElement(), -1);
    for (int i = 0; i < element.getUseContext().size(); i++)
      composeUsageContext(t, "CodeSystem", "useContext", element.getUseContext().get(i), i);
    for (int i = 0; i < element.getJurisdiction().size(); i++)
      composeCodeableConcept(t, "CodeSystem", "jurisdiction", element.getJurisdiction().get(i), i);
    if (element.hasPurposeElement())
      composeMarkdown(t, "CodeSystem", "purpose", element.getPurposeElement(), -1);
    if (element.hasCopyrightElement())
      composeMarkdown(t, "CodeSystem", "copyright", element.getCopyrightElement(), -1);
    if (element.hasCaseSensitiveElement())
      composeBoolean(t, "CodeSystem", "caseSensitive", element.getCaseSensitiveElement(), -1);
    if (element.hasValueSetElement())
      composeUri(t, "CodeSystem", "valueSet", element.getValueSetElement(), -1);
    if (element.hasHierarchyMeaningElement())
      composeEnum(t, "CodeSystem", "hierarchyMeaning", element.getHierarchyMeaningElement(), -1);
    if (element.hasCompositionalElement())
      composeBoolean(t, "CodeSystem", "compositional", element.getCompositionalElement(), -1);
    if (element.hasVersionNeededElement())
      composeBoolean(t, "CodeSystem", "versionNeeded", element.getVersionNeededElement(), -1);
    if (element.hasContentElement())
      composeEnum(t, "CodeSystem", "content", element.getContentElement(), -1);
    if (element.hasCountElement())
      composeUnsignedInt(t, "CodeSystem", "count", element.getCountElement(), -1);
    for (int i = 0; i < element.getFilter().size(); i++)
      composeCodeSystemCodeSystemFilterComponent(t, "CodeSystem", "filter", element.getFilter().get(i), i);
    for (int i = 0; i < element.getProperty().size(); i++)
      composeCodeSystemPropertyComponent(t, "CodeSystem", "property", element.getProperty().get(i), i);
    for (int i = 0; i < element.getConcept().size(); i++)
      composeCodeSystemConceptDefinitionComponent(t, "CodeSystem", "concept", element.getConcept().get(i), i);
  }

  protected void composeCodeSystemCodeSystemFilterComponent(Complex parent, String parentType, String name, CodeSystem.CodeSystemFilterComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "filter", name, element, index);
    if (element.hasCodeElement())
      composeCode(t, "CodeSystem", "code", element.getCodeElement(), -1);
    if (element.hasDescriptionElement())
      composeString(t, "CodeSystem", "description", element.getDescriptionElement(), -1);
    for (int i = 0; i < element.getOperator().size(); i++)
      composeEnum(t, "CodeSystem", "operator", element.getOperator().get(i), i);
    if (element.hasValueElement())
      composeString(t, "CodeSystem", "value", element.getValueElement(), -1);
  }

  protected void composeCodeSystemPropertyComponent(Complex parent, String parentType, String name, CodeSystem.PropertyComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "property", name, element, index);
    if (element.hasCodeElement())
      composeCode(t, "CodeSystem", "code", element.getCodeElement(), -1);
    if (element.hasUriElement())
      composeUri(t, "CodeSystem", "uri", element.getUriElement(), -1);
    if (element.hasDescriptionElement())
      composeString(t, "CodeSystem", "description", element.getDescriptionElement(), -1);
    if (element.hasTypeElement())
      composeEnum(t, "CodeSystem", "type", element.getTypeElement(), -1);
  }

  protected void composeCodeSystemConceptDefinitionComponent(Complex parent, String parentType, String name, CodeSystem.ConceptDefinitionComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "concept", name, element, index);
    if (element.hasCodeElement())
      composeCode(t, "CodeSystem", "code", element.getCodeElement(), -1);
    if (element.hasDisplayElement())
      composeString(t, "CodeSystem", "display", element.getDisplayElement(), -1);
    if (element.hasDefinitionElement())
      composeString(t, "CodeSystem", "definition", element.getDefinitionElement(), -1);
    for (int i = 0; i < element.getDesignation().size(); i++)
      composeCodeSystemConceptDefinitionDesignationComponent(t, "CodeSystem", "designation", element.getDesignation().get(i), i);
    for (int i = 0; i < element.getProperty().size(); i++)
      composeCodeSystemConceptPropertyComponent(t, "CodeSystem", "property", element.getProperty().get(i), i);
    for (int i = 0; i < element.getConcept().size(); i++)
      composeCodeSystemConceptDefinitionComponent(t, "CodeSystem", "concept", element.getConcept().get(i), i);
  }

  protected void composeCodeSystemConceptDefinitionDesignationComponent(Complex parent, String parentType, String name, CodeSystem.ConceptDefinitionDesignationComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "designation", name, element, index);
    if (element.hasLanguageElement())
      composeCode(t, "CodeSystem", "language", element.getLanguageElement(), -1);
    if (element.hasUse())
      composeCoding(t, "CodeSystem", "use", element.getUse(), -1);
    if (element.hasValueElement())
      composeString(t, "CodeSystem", "value", element.getValueElement(), -1);
  }

  protected void composeCodeSystemConceptPropertyComponent(Complex parent, String parentType, String name, CodeSystem.ConceptPropertyComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "property", name, element, index);
    if (element.hasCodeElement())
      composeCode(t, "CodeSystem", "code", element.getCodeElement(), -1);
    if (element.hasValue())
      composeType(t, "CodeSystem", "value", element.getValue(), -1);
  }

  protected void composeCommunication(Complex parent, String parentType, String name, Communication element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "Communication", name, element, index);
    for (int i = 0; i < element.getIdentifier().size(); i++)
      composeIdentifier(t, "Communication", "identifier", element.getIdentifier().get(i), i);
    for (int i = 0; i < element.getDefinition().size(); i++)
      composeReference(t, "Communication", "definition", element.getDefinition().get(i), i);
    for (int i = 0; i < element.getBasedOn().size(); i++)
      composeReference(t, "Communication", "basedOn", element.getBasedOn().get(i), i);
    for (int i = 0; i < element.getPartOf().size(); i++)
      composeReference(t, "Communication", "partOf", element.getPartOf().get(i), i);
    for (int i = 0; i < element.getInResponseTo().size(); i++)
      composeReference(t, "Communication", "inResponseTo", element.getInResponseTo().get(i), i);
    if (element.hasStatusElement())
      composeEnum(t, "Communication", "status", element.getStatusElement(), -1);
    if (element.hasNotDoneElement())
      composeBoolean(t, "Communication", "notDone", element.getNotDoneElement(), -1);
    if (element.hasNotDoneReason())
      composeCodeableConcept(t, "Communication", "notDoneReason", element.getNotDoneReason(), -1);
    for (int i = 0; i < element.getCategory().size(); i++)
      composeCodeableConcept(t, "Communication", "category", element.getCategory().get(i), i);
    if (element.hasPriorityElement())
      composeEnum(t, "Communication", "priority", element.getPriorityElement(), -1);
    for (int i = 0; i < element.getMedium().size(); i++)
      composeCodeableConcept(t, "Communication", "medium", element.getMedium().get(i), i);
    if (element.hasSubject())
      composeReference(t, "Communication", "subject", element.getSubject(), -1);
    for (int i = 0; i < element.getRecipient().size(); i++)
      composeReference(t, "Communication", "recipient", element.getRecipient().get(i), i);
    for (int i = 0; i < element.getTopic().size(); i++)
      composeReference(t, "Communication", "topic", element.getTopic().get(i), i);
    if (element.hasContext())
      composeReference(t, "Communication", "context", element.getContext(), -1);
    if (element.hasSentElement())
      composeDateTime(t, "Communication", "sent", element.getSentElement(), -1);
    if (element.hasReceivedElement())
      composeDateTime(t, "Communication", "received", element.getReceivedElement(), -1);
    if (element.hasSender())
      composeReference(t, "Communication", "sender", element.getSender(), -1);
    for (int i = 0; i < element.getReasonCode().size(); i++)
      composeCodeableConcept(t, "Communication", "reasonCode", element.getReasonCode().get(i), i);
    for (int i = 0; i < element.getReasonReference().size(); i++)
      composeReference(t, "Communication", "reasonReference", element.getReasonReference().get(i), i);
    for (int i = 0; i < element.getPayload().size(); i++)
      composeCommunicationCommunicationPayloadComponent(t, "Communication", "payload", element.getPayload().get(i), i);
    for (int i = 0; i < element.getNote().size(); i++)
      composeAnnotation(t, "Communication", "note", element.getNote().get(i), i);
  }

  protected void composeCommunicationCommunicationPayloadComponent(Complex parent, String parentType, String name, Communication.CommunicationPayloadComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "payload", name, element, index);
    if (element.hasContent())
      composeType(t, "Communication", "content", element.getContent(), -1);
  }

  protected void composeCommunicationRequest(Complex parent, String parentType, String name, CommunicationRequest element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "CommunicationRequest", name, element, index);
    for (int i = 0; i < element.getIdentifier().size(); i++)
      composeIdentifier(t, "CommunicationRequest", "identifier", element.getIdentifier().get(i), i);
    for (int i = 0; i < element.getBasedOn().size(); i++)
      composeReference(t, "CommunicationRequest", "basedOn", element.getBasedOn().get(i), i);
    for (int i = 0; i < element.getReplaces().size(); i++)
      composeReference(t, "CommunicationRequest", "replaces", element.getReplaces().get(i), i);
    if (element.hasGroupIdentifier())
      composeIdentifier(t, "CommunicationRequest", "groupIdentifier", element.getGroupIdentifier(), -1);
    if (element.hasStatusElement())
      composeEnum(t, "CommunicationRequest", "status", element.getStatusElement(), -1);
    for (int i = 0; i < element.getCategory().size(); i++)
      composeCodeableConcept(t, "CommunicationRequest", "category", element.getCategory().get(i), i);
    if (element.hasPriorityElement())
      composeEnum(t, "CommunicationRequest", "priority", element.getPriorityElement(), -1);
    for (int i = 0; i < element.getMedium().size(); i++)
      composeCodeableConcept(t, "CommunicationRequest", "medium", element.getMedium().get(i), i);
    if (element.hasSubject())
      composeReference(t, "CommunicationRequest", "subject", element.getSubject(), -1);
    for (int i = 0; i < element.getRecipient().size(); i++)
      composeReference(t, "CommunicationRequest", "recipient", element.getRecipient().get(i), i);
    for (int i = 0; i < element.getTopic().size(); i++)
      composeReference(t, "CommunicationRequest", "topic", element.getTopic().get(i), i);
    if (element.hasContext())
      composeReference(t, "CommunicationRequest", "context", element.getContext(), -1);
    for (int i = 0; i < element.getPayload().size(); i++)
      composeCommunicationRequestCommunicationRequestPayloadComponent(t, "CommunicationRequest", "payload", element.getPayload().get(i), i);
    if (element.hasOccurrence())
      composeType(t, "CommunicationRequest", "occurrence", element.getOccurrence(), -1);
    if (element.hasAuthoredOnElement())
      composeDateTime(t, "CommunicationRequest", "authoredOn", element.getAuthoredOnElement(), -1);
    if (element.hasSender())
      composeReference(t, "CommunicationRequest", "sender", element.getSender(), -1);
    if (element.hasRequester())
      composeCommunicationRequestCommunicationRequestRequesterComponent(t, "CommunicationRequest", "requester", element.getRequester(), -1);
    for (int i = 0; i < element.getReasonCode().size(); i++)
      composeCodeableConcept(t, "CommunicationRequest", "reasonCode", element.getReasonCode().get(i), i);
    for (int i = 0; i < element.getReasonReference().size(); i++)
      composeReference(t, "CommunicationRequest", "reasonReference", element.getReasonReference().get(i), i);
    for (int i = 0; i < element.getNote().size(); i++)
      composeAnnotation(t, "CommunicationRequest", "note", element.getNote().get(i), i);
  }

  protected void composeCommunicationRequestCommunicationRequestPayloadComponent(Complex parent, String parentType, String name, CommunicationRequest.CommunicationRequestPayloadComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "payload", name, element, index);
    if (element.hasContent())
      composeType(t, "CommunicationRequest", "content", element.getContent(), -1);
  }

  protected void composeCommunicationRequestCommunicationRequestRequesterComponent(Complex parent, String parentType, String name, CommunicationRequest.CommunicationRequestRequesterComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "requester", name, element, index);
    if (element.hasAgent())
      composeReference(t, "CommunicationRequest", "agent", element.getAgent(), -1);
    if (element.hasOnBehalfOf())
      composeReference(t, "CommunicationRequest", "onBehalfOf", element.getOnBehalfOf(), -1);
  }

  protected void composeCompartmentDefinition(Complex parent, String parentType, String name, CompartmentDefinition element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "CompartmentDefinition", name, element, index);
    if (element.hasUrlElement())
      composeUri(t, "CompartmentDefinition", "url", element.getUrlElement(), -1);
    if (element.hasNameElement())
      composeString(t, "CompartmentDefinition", "name", element.getNameElement(), -1);
    if (element.hasTitleElement())
      composeString(t, "CompartmentDefinition", "title", element.getTitleElement(), -1);
    if (element.hasStatusElement())
      composeEnum(t, "CompartmentDefinition", "status", element.getStatusElement(), -1);
    if (element.hasExperimentalElement())
      composeBoolean(t, "CompartmentDefinition", "experimental", element.getExperimentalElement(), -1);
    if (element.hasDateElement())
      composeDateTime(t, "CompartmentDefinition", "date", element.getDateElement(), -1);
    if (element.hasPublisherElement())
      composeString(t, "CompartmentDefinition", "publisher", element.getPublisherElement(), -1);
    for (int i = 0; i < element.getContact().size(); i++)
      composeContactDetail(t, "CompartmentDefinition", "contact", element.getContact().get(i), i);
    if (element.hasDescriptionElement())
      composeMarkdown(t, "CompartmentDefinition", "description", element.getDescriptionElement(), -1);
    if (element.hasPurposeElement())
      composeMarkdown(t, "CompartmentDefinition", "purpose", element.getPurposeElement(), -1);
    for (int i = 0; i < element.getUseContext().size(); i++)
      composeUsageContext(t, "CompartmentDefinition", "useContext", element.getUseContext().get(i), i);
    for (int i = 0; i < element.getJurisdiction().size(); i++)
      composeCodeableConcept(t, "CompartmentDefinition", "jurisdiction", element.getJurisdiction().get(i), i);
    if (element.hasCodeElement())
      composeEnum(t, "CompartmentDefinition", "code", element.getCodeElement(), -1);
    if (element.hasSearchElement())
      composeBoolean(t, "CompartmentDefinition", "search", element.getSearchElement(), -1);
    for (int i = 0; i < element.getResource().size(); i++)
      composeCompartmentDefinitionCompartmentDefinitionResourceComponent(t, "CompartmentDefinition", "resource", element.getResource().get(i), i);
  }

  protected void composeCompartmentDefinitionCompartmentDefinitionResourceComponent(Complex parent, String parentType, String name, CompartmentDefinition.CompartmentDefinitionResourceComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "resource", name, element, index);
    if (element.hasCodeElement())
      composeCode(t, "CompartmentDefinition", "code", element.getCodeElement(), -1);
    for (int i = 0; i < element.getParam().size(); i++)
      composeString(t, "CompartmentDefinition", "param", element.getParam().get(i), i);
    if (element.hasDocumentationElement())
      composeString(t, "CompartmentDefinition", "documentation", element.getDocumentationElement(), -1);
  }

  protected void composeComposition(Complex parent, String parentType, String name, Composition element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "Composition", name, element, index);
    if (element.hasIdentifier())
      composeIdentifier(t, "Composition", "identifier", element.getIdentifier(), -1);
    if (element.hasStatusElement())
      composeEnum(t, "Composition", "status", element.getStatusElement(), -1);
    if (element.hasType())
      composeCodeableConcept(t, "Composition", "type", element.getType(), -1);
    if (element.hasClass_())
      composeCodeableConcept(t, "Composition", "class", element.getClass_(), -1);
    if (element.hasSubject())
      composeReference(t, "Composition", "subject", element.getSubject(), -1);
    if (element.hasEncounter())
      composeReference(t, "Composition", "encounter", element.getEncounter(), -1);
    if (element.hasDateElement())
      composeDateTime(t, "Composition", "date", element.getDateElement(), -1);
    for (int i = 0; i < element.getAuthor().size(); i++)
      composeReference(t, "Composition", "author", element.getAuthor().get(i), i);
    if (element.hasTitleElement())
      composeString(t, "Composition", "title", element.getTitleElement(), -1);
    if (element.hasConfidentialityElement())
      composeEnum(t, "Composition", "confidentiality", element.getConfidentialityElement(), -1);
    for (int i = 0; i < element.getAttester().size(); i++)
      composeCompositionCompositionAttesterComponent(t, "Composition", "attester", element.getAttester().get(i), i);
    if (element.hasCustodian())
      composeReference(t, "Composition", "custodian", element.getCustodian(), -1);
    for (int i = 0; i < element.getRelatesTo().size(); i++)
      composeCompositionCompositionRelatesToComponent(t, "Composition", "relatesTo", element.getRelatesTo().get(i), i);
    for (int i = 0; i < element.getEvent().size(); i++)
      composeCompositionCompositionEventComponent(t, "Composition", "event", element.getEvent().get(i), i);
    for (int i = 0; i < element.getSection().size(); i++)
      composeCompositionSectionComponent(t, "Composition", "section", element.getSection().get(i), i);
  }

  protected void composeCompositionCompositionAttesterComponent(Complex parent, String parentType, String name, Composition.CompositionAttesterComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "attester", name, element, index);
    for (int i = 0; i < element.getMode().size(); i++)
      composeEnum(t, "Composition", "mode", element.getMode().get(i), i);
    if (element.hasTimeElement())
      composeDateTime(t, "Composition", "time", element.getTimeElement(), -1);
    if (element.hasParty())
      composeReference(t, "Composition", "party", element.getParty(), -1);
  }

  protected void composeCompositionCompositionRelatesToComponent(Complex parent, String parentType, String name, Composition.CompositionRelatesToComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "relatesTo", name, element, index);
    if (element.hasCodeElement())
      composeEnum(t, "Composition", "code", element.getCodeElement(), -1);
    if (element.hasTarget())
      composeType(t, "Composition", "target", element.getTarget(), -1);
  }

  protected void composeCompositionCompositionEventComponent(Complex parent, String parentType, String name, Composition.CompositionEventComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "event", name, element, index);
    for (int i = 0; i < element.getCode().size(); i++)
      composeCodeableConcept(t, "Composition", "code", element.getCode().get(i), i);
    if (element.hasPeriod())
      composePeriod(t, "Composition", "period", element.getPeriod(), -1);
    for (int i = 0; i < element.getDetail().size(); i++)
      composeReference(t, "Composition", "detail", element.getDetail().get(i), i);
  }

  protected void composeCompositionSectionComponent(Complex parent, String parentType, String name, Composition.SectionComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "section", name, element, index);
    if (element.hasTitleElement())
      composeString(t, "Composition", "title", element.getTitleElement(), -1);
    if (element.hasCode())
      composeCodeableConcept(t, "Composition", "code", element.getCode(), -1);
    if (element.hasText())
      composeNarrative(t, "Composition", "text", element.getText(), -1);
    if (element.hasModeElement())
      composeEnum(t, "Composition", "mode", element.getModeElement(), -1);
    if (element.hasOrderedBy())
      composeCodeableConcept(t, "Composition", "orderedBy", element.getOrderedBy(), -1);
    for (int i = 0; i < element.getEntry().size(); i++)
      composeReference(t, "Composition", "entry", element.getEntry().get(i), i);
    if (element.hasEmptyReason())
      composeCodeableConcept(t, "Composition", "emptyReason", element.getEmptyReason(), -1);
    for (int i = 0; i < element.getSection().size(); i++)
      composeCompositionSectionComponent(t, "Composition", "section", element.getSection().get(i), i);
  }

  protected void composeConceptMap(Complex parent, String parentType, String name, ConceptMap element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "ConceptMap", name, element, index);
    if (element.hasUrlElement())
      composeUri(t, "ConceptMap", "url", element.getUrlElement(), -1);
    if (element.hasIdentifier())
      composeIdentifier(t, "ConceptMap", "identifier", element.getIdentifier(), -1);
    if (element.hasVersionElement())
      composeString(t, "ConceptMap", "version", element.getVersionElement(), -1);
    if (element.hasNameElement())
      composeString(t, "ConceptMap", "name", element.getNameElement(), -1);
    if (element.hasTitleElement())
      composeString(t, "ConceptMap", "title", element.getTitleElement(), -1);
    if (element.hasStatusElement())
      composeEnum(t, "ConceptMap", "status", element.getStatusElement(), -1);
    if (element.hasExperimentalElement())
      composeBoolean(t, "ConceptMap", "experimental", element.getExperimentalElement(), -1);
    if (element.hasDateElement())
      composeDateTime(t, "ConceptMap", "date", element.getDateElement(), -1);
    if (element.hasPublisherElement())
      composeString(t, "ConceptMap", "publisher", element.getPublisherElement(), -1);
    for (int i = 0; i < element.getContact().size(); i++)
      composeContactDetail(t, "ConceptMap", "contact", element.getContact().get(i), i);
    if (element.hasDescriptionElement())
      composeMarkdown(t, "ConceptMap", "description", element.getDescriptionElement(), -1);
    for (int i = 0; i < element.getUseContext().size(); i++)
      composeUsageContext(t, "ConceptMap", "useContext", element.getUseContext().get(i), i);
    for (int i = 0; i < element.getJurisdiction().size(); i++)
      composeCodeableConcept(t, "ConceptMap", "jurisdiction", element.getJurisdiction().get(i), i);
    if (element.hasPurposeElement())
      composeMarkdown(t, "ConceptMap", "purpose", element.getPurposeElement(), -1);
    if (element.hasCopyrightElement())
      composeMarkdown(t, "ConceptMap", "copyright", element.getCopyrightElement(), -1);
    if (element.hasSource())
      composeType(t, "ConceptMap", "source", element.getSource(), -1);
    if (element.hasTarget())
      composeType(t, "ConceptMap", "target", element.getTarget(), -1);
    for (int i = 0; i < element.getGroup().size(); i++)
      composeConceptMapConceptMapGroupComponent(t, "ConceptMap", "group", element.getGroup().get(i), i);
  }

  protected void composeConceptMapConceptMapGroupComponent(Complex parent, String parentType, String name, ConceptMap.ConceptMapGroupComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "group", name, element, index);
    if (element.hasSourceElement())
      composeUri(t, "ConceptMap", "source", element.getSourceElement(), -1);
    if (element.hasSourceVersionElement())
      composeString(t, "ConceptMap", "sourceVersion", element.getSourceVersionElement(), -1);
    if (element.hasTargetElement())
      composeUri(t, "ConceptMap", "target", element.getTargetElement(), -1);
    if (element.hasTargetVersionElement())
      composeString(t, "ConceptMap", "targetVersion", element.getTargetVersionElement(), -1);
    for (int i = 0; i < element.getElement().size(); i++)
      composeConceptMapSourceElementComponent(t, "ConceptMap", "element", element.getElement().get(i), i);
    if (element.hasUnmapped())
      composeConceptMapConceptMapGroupUnmappedComponent(t, "ConceptMap", "unmapped", element.getUnmapped(), -1);
  }

  protected void composeConceptMapSourceElementComponent(Complex parent, String parentType, String name, ConceptMap.SourceElementComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "element", name, element, index);
    if (element.hasCodeElement())
      composeCode(t, "ConceptMap", "code", element.getCodeElement(), -1);
    if (element.hasDisplayElement())
      composeString(t, "ConceptMap", "display", element.getDisplayElement(), -1);
    for (int i = 0; i < element.getTarget().size(); i++)
      composeConceptMapTargetElementComponent(t, "ConceptMap", "target", element.getTarget().get(i), i);
  }

  protected void composeConceptMapTargetElementComponent(Complex parent, String parentType, String name, ConceptMap.TargetElementComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "target", name, element, index);
    if (element.hasCodeElement())
      composeCode(t, "ConceptMap", "code", element.getCodeElement(), -1);
    if (element.hasDisplayElement())
      composeString(t, "ConceptMap", "display", element.getDisplayElement(), -1);
    if (element.hasEquivalenceElement())
      composeEnum(t, "ConceptMap", "equivalence", element.getEquivalenceElement(), -1);
    if (element.hasCommentElement())
      composeString(t, "ConceptMap", "comment", element.getCommentElement(), -1);
    for (int i = 0; i < element.getDependsOn().size(); i++)
      composeConceptMapOtherElementComponent(t, "ConceptMap", "dependsOn", element.getDependsOn().get(i), i);
    for (int i = 0; i < element.getProduct().size(); i++)
      composeConceptMapOtherElementComponent(t, "ConceptMap", "product", element.getProduct().get(i), i);
  }

  protected void composeConceptMapOtherElementComponent(Complex parent, String parentType, String name, ConceptMap.OtherElementComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "dependsOn", name, element, index);
    if (element.hasPropertyElement())
      composeUri(t, "ConceptMap", "property", element.getPropertyElement(), -1);
    if (element.hasSystemElement())
      composeUri(t, "ConceptMap", "system", element.getSystemElement(), -1);
    if (element.hasCodeElement())
      composeString(t, "ConceptMap", "code", element.getCodeElement(), -1);
    if (element.hasDisplayElement())
      composeString(t, "ConceptMap", "display", element.getDisplayElement(), -1);
  }

  protected void composeConceptMapConceptMapGroupUnmappedComponent(Complex parent, String parentType, String name, ConceptMap.ConceptMapGroupUnmappedComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "unmapped", name, element, index);
    if (element.hasModeElement())
      composeEnum(t, "ConceptMap", "mode", element.getModeElement(), -1);
    if (element.hasCodeElement())
      composeCode(t, "ConceptMap", "code", element.getCodeElement(), -1);
    if (element.hasDisplayElement())
      composeString(t, "ConceptMap", "display", element.getDisplayElement(), -1);
    if (element.hasUrlElement())
      composeUri(t, "ConceptMap", "url", element.getUrlElement(), -1);
  }

  protected void composeCondition(Complex parent, String parentType, String name, Condition element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "Condition", name, element, index);
    for (int i = 0; i < element.getIdentifier().size(); i++)
      composeIdentifier(t, "Condition", "identifier", element.getIdentifier().get(i), i);
    if (element.hasClinicalStatusElement())
      composeEnum(t, "Condition", "clinicalStatus", element.getClinicalStatusElement(), -1);
    if (element.hasVerificationStatusElement())
      composeEnum(t, "Condition", "verificationStatus", element.getVerificationStatusElement(), -1);
    for (int i = 0; i < element.getCategory().size(); i++)
      composeCodeableConcept(t, "Condition", "category", element.getCategory().get(i), i);
    if (element.hasSeverity())
      composeCodeableConcept(t, "Condition", "severity", element.getSeverity(), -1);
    if (element.hasCode())
      composeCodeableConcept(t, "Condition", "code", element.getCode(), -1);
    for (int i = 0; i < element.getBodySite().size(); i++)
      composeCodeableConcept(t, "Condition", "bodySite", element.getBodySite().get(i), i);
    if (element.hasSubject())
      composeReference(t, "Condition", "subject", element.getSubject(), -1);
    if (element.hasContext())
      composeReference(t, "Condition", "context", element.getContext(), -1);
    if (element.hasOnset())
      composeType(t, "Condition", "onset", element.getOnset(), -1);
    if (element.hasAbatement())
      composeType(t, "Condition", "abatement", element.getAbatement(), -1);
    if (element.hasAssertedDateElement())
      composeDateTime(t, "Condition", "assertedDate", element.getAssertedDateElement(), -1);
    if (element.hasAsserter())
      composeReference(t, "Condition", "asserter", element.getAsserter(), -1);
    for (int i = 0; i < element.getStage().size(); i++)
      composeConditionConditionStageComponent(t, "Condition", "stage", element.getStage().get(i), i);
    for (int i = 0; i < element.getEvidence().size(); i++)
      composeConditionConditionEvidenceComponent(t, "Condition", "evidence", element.getEvidence().get(i), i);
    for (int i = 0; i < element.getNote().size(); i++)
      composeAnnotation(t, "Condition", "note", element.getNote().get(i), i);
  }

  protected void composeConditionConditionStageComponent(Complex parent, String parentType, String name, Condition.ConditionStageComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "stage", name, element, index);
    if (element.hasSummary())
      composeCodeableConcept(t, "Condition", "summary", element.getSummary(), -1);
    for (int i = 0; i < element.getAssessment().size(); i++)
      composeReference(t, "Condition", "assessment", element.getAssessment().get(i), i);
    if (element.hasType())
      composeCodeableConcept(t, "Condition", "type", element.getType(), -1);
  }

  protected void composeConditionConditionEvidenceComponent(Complex parent, String parentType, String name, Condition.ConditionEvidenceComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "evidence", name, element, index);
    for (int i = 0; i < element.getCode().size(); i++)
      composeCodeableConcept(t, "Condition", "code", element.getCode().get(i), i);
    for (int i = 0; i < element.getDetail().size(); i++)
      composeReference(t, "Condition", "detail", element.getDetail().get(i), i);
  }

  protected void composeConsent(Complex parent, String parentType, String name, Consent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "Consent", name, element, index);
    if (element.hasIdentifier())
      composeIdentifier(t, "Consent", "identifier", element.getIdentifier(), -1);
    if (element.hasStatusElement())
      composeEnum(t, "Consent", "status", element.getStatusElement(), -1);
    for (int i = 0; i < element.getCategory().size(); i++)
      composeCodeableConcept(t, "Consent", "category", element.getCategory().get(i), i);
    if (element.hasPatient())
      composeReference(t, "Consent", "patient", element.getPatient(), -1);
    if (element.hasDateTimeElement())
      composeDateTime(t, "Consent", "dateTime", element.getDateTimeElement(), -1);
    for (int i = 0; i < element.getConsentingParty().size(); i++)
      composeReference(t, "Consent", "consentingParty", element.getConsentingParty().get(i), i);
    for (int i = 0; i < element.getOrganization().size(); i++)
      composeReference(t, "Consent", "organization", element.getOrganization().get(i), i);
    if (element.hasSource())
      composeType(t, "Consent", "source", element.getSource(), -1);
    for (int i = 0; i < element.getPolicy().size(); i++)
      composeConsentConsentPolicyComponent(t, "Consent", "policy", element.getPolicy().get(i), i);
    if (element.hasPolicyRule())
      composeCodeableConcept(t, "Consent", "policyRule", element.getPolicyRule(), -1);
    for (int i = 0; i < element.getVerification().size(); i++)
      composeConsentConsentVerificationComponent(t, "Consent", "verification", element.getVerification().get(i), i);
    for (int i = 0; i < element.getProvision().size(); i++)
      composeConsentprovisionComponent(t, "Consent", "provision", element.getProvision().get(i), i);
  }

  protected void composeConsentConsentPolicyComponent(Complex parent, String parentType, String name, Consent.ConsentPolicyComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "policy", name, element, index);
    if (element.hasAuthorityElement())
      composeUri(t, "Consent", "authority", element.getAuthorityElement(), -1);
    if (element.hasUriElement())
      composeUri(t, "Consent", "uri", element.getUriElement(), -1);
  }

  protected void composeConsentConsentVerificationComponent(Complex parent, String parentType, String name, Consent.ConsentVerificationComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "verification", name, element, index);
    if (element.hasVerifiedElement())
      composeBoolean(t, "Consent", "verified", element.getVerifiedElement(), -1);
    if (element.hasVerifiedWith())
      composeReference(t, "Consent", "verifiedWith", element.getVerifiedWith(), -1);
    if (element.hasVerificationDateElement())
      composeDateTime(t, "Consent", "verificationDate", element.getVerificationDateElement(), -1);
  }

  protected void composeConsentprovisionComponent(Complex parent, String parentType, String name, Consent.provisionComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "provision", name, element, index);
    if (element.hasTypeElement())
      composeEnum(t, "Consent", "type", element.getTypeElement(), -1);
    if (element.hasPeriod())
      composePeriod(t, "Consent", "period", element.getPeriod(), -1);
    for (int i = 0; i < element.getActor().size(); i++)
      composeConsentprovisionActorComponent(t, "Consent", "actor", element.getActor().get(i), i);
    for (int i = 0; i < element.getAction().size(); i++)
      composeCodeableConcept(t, "Consent", "action", element.getAction().get(i), i);
    for (int i = 0; i < element.getSecurityLabel().size(); i++)
      composeCoding(t, "Consent", "securityLabel", element.getSecurityLabel().get(i), i);
    for (int i = 0; i < element.getPurpose().size(); i++)
      composeCoding(t, "Consent", "purpose", element.getPurpose().get(i), i);
    for (int i = 0; i < element.getClass_().size(); i++)
      composeCoding(t, "Consent", "class", element.getClass_().get(i), i);
    for (int i = 0; i < element.getCode().size(); i++)
      composeCoding(t, "Consent", "code", element.getCode().get(i), i);
    if (element.hasDataPeriod())
      composePeriod(t, "Consent", "dataPeriod", element.getDataPeriod(), -1);
    for (int i = 0; i < element.getData().size(); i++)
      composeConsentprovisionDataComponent(t, "Consent", "data", element.getData().get(i), i);
    for (int i = 0; i < element.getProvision().size(); i++)
      composeConsentprovisionComponent(t, "Consent", "provision", element.getProvision().get(i), i);
  }

  protected void composeConsentprovisionActorComponent(Complex parent, String parentType, String name, Consent.provisionActorComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "actor", name, element, index);
    if (element.hasRole())
      composeCodeableConcept(t, "Consent", "role", element.getRole(), -1);
    if (element.hasReference())
      composeReference(t, "Consent", "reference", element.getReference(), -1);
  }

  protected void composeConsentprovisionDataComponent(Complex parent, String parentType, String name, Consent.provisionDataComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "data", name, element, index);
    if (element.hasMeaningElement())
      composeEnum(t, "Consent", "meaning", element.getMeaningElement(), -1);
    if (element.hasReference())
      composeReference(t, "Consent", "reference", element.getReference(), -1);
  }

  protected void composeContract(Complex parent, String parentType, String name, Contract element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "Contract", name, element, index);
    if (element.hasIdentifier())
      composeIdentifier(t, "Contract", "identifier", element.getIdentifier(), -1);
    if (element.hasStatusElement())
      composeEnum(t, "Contract", "status", element.getStatusElement(), -1);
    if (element.hasContentDerivative())
      composeCodeableConcept(t, "Contract", "contentDerivative", element.getContentDerivative(), -1);
    if (element.hasIssuedElement())
      composeDateTime(t, "Contract", "issued", element.getIssuedElement(), -1);
    if (element.hasApplies())
      composePeriod(t, "Contract", "applies", element.getApplies(), -1);
    for (int i = 0; i < element.getSubject().size(); i++)
      composeReference(t, "Contract", "subject", element.getSubject().get(i), i);
    for (int i = 0; i < element.getAuthority().size(); i++)
      composeReference(t, "Contract", "authority", element.getAuthority().get(i), i);
    for (int i = 0; i < element.getDomain().size(); i++)
      composeReference(t, "Contract", "domain", element.getDomain().get(i), i);
    if (element.hasType())
      composeCodeableConcept(t, "Contract", "type", element.getType(), -1);
    for (int i = 0; i < element.getSubType().size(); i++)
      composeCodeableConcept(t, "Contract", "subType", element.getSubType().get(i), i);
    for (int i = 0; i < element.getTerm().size(); i++)
      composeContractTermComponent(t, "Contract", "term", element.getTerm().get(i), i);
    for (int i = 0; i < element.getSigner().size(); i++)
      composeContractSignatoryComponent(t, "Contract", "signer", element.getSigner().get(i), i);
    for (int i = 0; i < element.getFriendly().size(); i++)
      composeContractFriendlyLanguageComponent(t, "Contract", "friendly", element.getFriendly().get(i), i);
    for (int i = 0; i < element.getLegal().size(); i++)
      composeContractLegalLanguageComponent(t, "Contract", "legal", element.getLegal().get(i), i);
    if (element.hasRule())
      composeContractComputableLanguageComponent(t, "Contract", "rule", element.getRule(), -1);
    if (element.hasLegallyBinding())
      composeType(t, "Contract", "legallyBinding", element.getLegallyBinding(), -1);
  }

  protected void composeContractTermComponent(Complex parent, String parentType, String name, Contract.TermComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "term", name, element, index);
    if (element.hasIdentifier())
      composeIdentifier(t, "Contract", "identifier", element.getIdentifier(), -1);
    if (element.hasIssuedElement())
      composeDateTime(t, "Contract", "issued", element.getIssuedElement(), -1);
    if (element.hasApplies())
      composePeriod(t, "Contract", "applies", element.getApplies(), -1);
    if (element.hasType())
      composeCodeableConcept(t, "Contract", "type", element.getType(), -1);
    if (element.hasSubType())
      composeCodeableConcept(t, "Contract", "subType", element.getSubType(), -1);
    if (element.hasOffer())
      composeContractContractOfferComponent(t, "Contract", "offer", element.getOffer(), -1);
    for (int i = 0; i < element.getAsset().size(); i++)
      composeContractContractAssetComponent(t, "Contract", "asset", element.getAsset().get(i), i);
    for (int i = 0; i < element.getAgent().size(); i++)
      composeContractAgentComponent(t, "Contract", "agent", element.getAgent().get(i), i);
    for (int i = 0; i < element.getAction().size(); i++)
      composeCodeableConcept(t, "Contract", "action", element.getAction().get(i), i);
    for (int i = 0; i < element.getActionReason().size(); i++)
      composeCodeableConcept(t, "Contract", "actionReason", element.getActionReason().get(i), i);
    for (int i = 0; i < element.getGroup().size(); i++)
      composeContractTermComponent(t, "Contract", "group", element.getGroup().get(i), i);
  }

  protected void composeContractContractOfferComponent(Complex parent, String parentType, String name, Contract.ContractOfferComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "offer", name, element, index);
    if (element.hasTopic())
      composeReference(t, "Contract", "topic", element.getTopic(), -1);
    if (element.hasType())
      composeCodeableConcept(t, "Contract", "type", element.getType(), -1);
    if (element.hasDecision())
      composeCodeableConcept(t, "Contract", "decision", element.getDecision(), -1);
    if (element.hasTextElement())
      composeString(t, "Contract", "text", element.getTextElement(), -1);
    if (element.hasLinkIdElement())
      composeString(t, "Contract", "linkId", element.getLinkIdElement(), -1);
  }

  protected void composeContractContractAssetComponent(Complex parent, String parentType, String name, Contract.ContractAssetComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "asset", name, element, index);
    if (element.hasClass_())
      composeCoding(t, "Contract", "class", element.getClass_(), -1);
    if (element.hasCode())
      composeCoding(t, "Contract", "code", element.getCode(), -1);
    if (element.hasPeriod())
      composePeriod(t, "Contract", "period", element.getPeriod(), -1);
    if (element.hasDataPeriod())
      composePeriod(t, "Contract", "dataPeriod", element.getDataPeriod(), -1);
    for (int i = 0; i < element.getData().size(); i++)
      composeContractAssetDataComponent(t, "Contract", "data", element.getData().get(i), i);
    for (int i = 0; i < element.getValuedItem().size(); i++)
      composeContractValuedItemComponent(t, "Contract", "valuedItem", element.getValuedItem().get(i), i);
    for (int i = 0; i < element.getSecurityLabel().size(); i++)
      composeCoding(t, "Contract", "securityLabel", element.getSecurityLabel().get(i), i);
  }

  protected void composeContractAssetDataComponent(Complex parent, String parentType, String name, Contract.AssetDataComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "data", name, element, index);
    if (element.hasMeaningElement())
      composeEnum(t, "Contract", "meaning", element.getMeaningElement(), -1);
    if (element.hasReference())
      composeReference(t, "Contract", "reference", element.getReference(), -1);
  }

  protected void composeContractValuedItemComponent(Complex parent, String parentType, String name, Contract.ValuedItemComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "valuedItem", name, element, index);
    if (element.hasEntity())
      composeType(t, "Contract", "entity", element.getEntity(), -1);
    if (element.hasIdentifier())
      composeIdentifier(t, "Contract", "identifier", element.getIdentifier(), -1);
    if (element.hasEffectiveTimeElement())
      composeDateTime(t, "Contract", "effectiveTime", element.getEffectiveTimeElement(), -1);
    if (element.hasQuantity())
      composeQuantity(t, "Contract", "quantity", element.getQuantity(), -1);
    if (element.hasUnitPrice())
      composeMoney(t, "Contract", "unitPrice", element.getUnitPrice(), -1);
    if (element.hasFactorElement())
      composeDecimal(t, "Contract", "factor", element.getFactorElement(), -1);
    if (element.hasPointsElement())
      composeDecimal(t, "Contract", "points", element.getPointsElement(), -1);
    if (element.hasNet())
      composeMoney(t, "Contract", "net", element.getNet(), -1);
  }

  protected void composeContractAgentComponent(Complex parent, String parentType, String name, Contract.AgentComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "agent", name, element, index);
    if (element.hasActor())
      composeReference(t, "Contract", "actor", element.getActor(), -1);
    for (int i = 0; i < element.getRole().size(); i++)
      composeCodeableConcept(t, "Contract", "role", element.getRole().get(i), i);
  }

  protected void composeContractSignatoryComponent(Complex parent, String parentType, String name, Contract.SignatoryComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "signer", name, element, index);
    if (element.hasType())
      composeCoding(t, "Contract", "type", element.getType(), -1);
    if (element.hasParty())
      composeReference(t, "Contract", "party", element.getParty(), -1);
    for (int i = 0; i < element.getSignature().size(); i++)
      composeSignature(t, "Contract", "signature", element.getSignature().get(i), i);
  }

  protected void composeContractFriendlyLanguageComponent(Complex parent, String parentType, String name, Contract.FriendlyLanguageComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "friendly", name, element, index);
    if (element.hasContent())
      composeType(t, "Contract", "content", element.getContent(), -1);
  }

  protected void composeContractLegalLanguageComponent(Complex parent, String parentType, String name, Contract.LegalLanguageComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "legal", name, element, index);
    if (element.hasContent())
      composeType(t, "Contract", "content", element.getContent(), -1);
  }

  protected void composeContractComputableLanguageComponent(Complex parent, String parentType, String name, Contract.ComputableLanguageComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "rule", name, element, index);
    if (element.hasContent())
      composeType(t, "Contract", "content", element.getContent(), -1);
  }

  protected void composeCoverage(Complex parent, String parentType, String name, Coverage element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "Coverage", name, element, index);
    for (int i = 0; i < element.getIdentifier().size(); i++)
      composeIdentifier(t, "Coverage", "identifier", element.getIdentifier().get(i), i);
    if (element.hasStatusElement())
      composeEnum(t, "Coverage", "status", element.getStatusElement(), -1);
    if (element.hasType())
      composeCodeableConcept(t, "Coverage", "type", element.getType(), -1);
    if (element.hasPolicyHolder())
      composeReference(t, "Coverage", "policyHolder", element.getPolicyHolder(), -1);
    if (element.hasSubscriber())
      composeReference(t, "Coverage", "subscriber", element.getSubscriber(), -1);
    if (element.hasSubscriberIdElement())
      composeString(t, "Coverage", "subscriberId", element.getSubscriberIdElement(), -1);
    if (element.hasBeneficiary())
      composeReference(t, "Coverage", "beneficiary", element.getBeneficiary(), -1);
    if (element.hasRelationship())
      composeCodeableConcept(t, "Coverage", "relationship", element.getRelationship(), -1);
    if (element.hasPeriod())
      composePeriod(t, "Coverage", "period", element.getPeriod(), -1);
    for (int i = 0; i < element.getPayor().size(); i++)
      composeReference(t, "Coverage", "payor", element.getPayor().get(i), i);
    if (element.hasGrouping())
      composeCoverageGroupComponent(t, "Coverage", "grouping", element.getGrouping(), -1);
    if (element.hasDependentElement())
      composeString(t, "Coverage", "dependent", element.getDependentElement(), -1);
    if (element.hasSequenceElement())
      composeString(t, "Coverage", "sequence", element.getSequenceElement(), -1);
    if (element.hasOrderElement())
      composePositiveInt(t, "Coverage", "order", element.getOrderElement(), -1);
    if (element.hasNetworkElement())
      composeString(t, "Coverage", "network", element.getNetworkElement(), -1);
    for (int i = 0; i < element.getContract().size(); i++)
      composeReference(t, "Coverage", "contract", element.getContract().get(i), i);
  }

  protected void composeCoverageGroupComponent(Complex parent, String parentType, String name, Coverage.GroupComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "grouping", name, element, index);
    if (element.hasGroupElement())
      composeString(t, "Coverage", "group", element.getGroupElement(), -1);
    if (element.hasGroupDisplayElement())
      composeString(t, "Coverage", "groupDisplay", element.getGroupDisplayElement(), -1);
    if (element.hasSubGroupElement())
      composeString(t, "Coverage", "subGroup", element.getSubGroupElement(), -1);
    if (element.hasSubGroupDisplayElement())
      composeString(t, "Coverage", "subGroupDisplay", element.getSubGroupDisplayElement(), -1);
    if (element.hasPlanElement())
      composeString(t, "Coverage", "plan", element.getPlanElement(), -1);
    if (element.hasPlanDisplayElement())
      composeString(t, "Coverage", "planDisplay", element.getPlanDisplayElement(), -1);
    if (element.hasSubPlanElement())
      composeString(t, "Coverage", "subPlan", element.getSubPlanElement(), -1);
    if (element.hasSubPlanDisplayElement())
      composeString(t, "Coverage", "subPlanDisplay", element.getSubPlanDisplayElement(), -1);
    if (element.hasClass_Element())
      composeString(t, "Coverage", "class", element.getClass_Element(), -1);
    if (element.hasClassDisplayElement())
      composeString(t, "Coverage", "classDisplay", element.getClassDisplayElement(), -1);
    if (element.hasSubClassElement())
      composeString(t, "Coverage", "subClass", element.getSubClassElement(), -1);
    if (element.hasSubClassDisplayElement())
      composeString(t, "Coverage", "subClassDisplay", element.getSubClassDisplayElement(), -1);
  }

  protected void composeDetectedIssue(Complex parent, String parentType, String name, DetectedIssue element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "DetectedIssue", name, element, index);
    if (element.hasIdentifier())
      composeIdentifier(t, "DetectedIssue", "identifier", element.getIdentifier(), -1);
    if (element.hasStatusElement())
      composeEnum(t, "DetectedIssue", "status", element.getStatusElement(), -1);
    if (element.hasCategory())
      composeCodeableConcept(t, "DetectedIssue", "category", element.getCategory(), -1);
    if (element.hasSeverityElement())
      composeEnum(t, "DetectedIssue", "severity", element.getSeverityElement(), -1);
    if (element.hasPatient())
      composeReference(t, "DetectedIssue", "patient", element.getPatient(), -1);
    if (element.hasDateElement())
      composeDateTime(t, "DetectedIssue", "date", element.getDateElement(), -1);
    if (element.hasAuthor())
      composeReference(t, "DetectedIssue", "author", element.getAuthor(), -1);
    for (int i = 0; i < element.getImplicated().size(); i++)
      composeReference(t, "DetectedIssue", "implicated", element.getImplicated().get(i), i);
    if (element.hasDetailElement())
      composeString(t, "DetectedIssue", "detail", element.getDetailElement(), -1);
    if (element.hasReferenceElement())
      composeUri(t, "DetectedIssue", "reference", element.getReferenceElement(), -1);
    for (int i = 0; i < element.getMitigation().size(); i++)
      composeDetectedIssueDetectedIssueMitigationComponent(t, "DetectedIssue", "mitigation", element.getMitigation().get(i), i);
  }

  protected void composeDetectedIssueDetectedIssueMitigationComponent(Complex parent, String parentType, String name, DetectedIssue.DetectedIssueMitigationComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "mitigation", name, element, index);
    if (element.hasAction())
      composeCodeableConcept(t, "DetectedIssue", "action", element.getAction(), -1);
    if (element.hasDateElement())
      composeDateTime(t, "DetectedIssue", "date", element.getDateElement(), -1);
    if (element.hasAuthor())
      composeReference(t, "DetectedIssue", "author", element.getAuthor(), -1);
  }

  protected void composeDevice(Complex parent, String parentType, String name, Device element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "Device", name, element, index);
    for (int i = 0; i < element.getIdentifier().size(); i++)
      composeIdentifier(t, "Device", "identifier", element.getIdentifier().get(i), i);
    if (element.hasUdi())
      composeDeviceDeviceUdiComponent(t, "Device", "udi", element.getUdi(), -1);
    if (element.hasStatusElement())
      composeEnum(t, "Device", "status", element.getStatusElement(), -1);
    if (element.hasType())
      composeCodeableConcept(t, "Device", "type", element.getType(), -1);
    if (element.hasLotNumberElement())
      composeString(t, "Device", "lotNumber", element.getLotNumberElement(), -1);
    if (element.hasManufacturerElement())
      composeString(t, "Device", "manufacturer", element.getManufacturerElement(), -1);
    if (element.hasManufactureDateElement())
      composeDateTime(t, "Device", "manufactureDate", element.getManufactureDateElement(), -1);
    if (element.hasExpirationDateElement())
      composeDateTime(t, "Device", "expirationDate", element.getExpirationDateElement(), -1);
    if (element.hasModelElement())
      composeString(t, "Device", "model", element.getModelElement(), -1);
    if (element.hasVersionElement())
      composeString(t, "Device", "version", element.getVersionElement(), -1);
    if (element.hasPatient())
      composeReference(t, "Device", "patient", element.getPatient(), -1);
    if (element.hasOwner())
      composeReference(t, "Device", "owner", element.getOwner(), -1);
    for (int i = 0; i < element.getContact().size(); i++)
      composeContactPoint(t, "Device", "contact", element.getContact().get(i), i);
    if (element.hasLocation())
      composeReference(t, "Device", "location", element.getLocation(), -1);
    if (element.hasUrlElement())
      composeUri(t, "Device", "url", element.getUrlElement(), -1);
    for (int i = 0; i < element.getNote().size(); i++)
      composeAnnotation(t, "Device", "note", element.getNote().get(i), i);
    for (int i = 0; i < element.getSafety().size(); i++)
      composeCodeableConcept(t, "Device", "safety", element.getSafety().get(i), i);
  }

  protected void composeDeviceDeviceUdiComponent(Complex parent, String parentType, String name, Device.DeviceUdiComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "udi", name, element, index);
    if (element.hasDeviceIdentifierElement())
      composeString(t, "Device", "deviceIdentifier", element.getDeviceIdentifierElement(), -1);
    if (element.hasNameElement())
      composeString(t, "Device", "name", element.getNameElement(), -1);
    if (element.hasJurisdictionElement())
      composeUri(t, "Device", "jurisdiction", element.getJurisdictionElement(), -1);
    if (element.hasCarrierHRFElement())
      composeString(t, "Device", "carrierHRF", element.getCarrierHRFElement(), -1);
    if (element.hasCarrierAIDCElement())
      composeBase64Binary(t, "Device", "carrierAIDC", element.getCarrierAIDCElement(), -1);
    if (element.hasIssuerElement())
      composeUri(t, "Device", "issuer", element.getIssuerElement(), -1);
    if (element.hasEntryTypeElement())
      composeEnum(t, "Device", "entryType", element.getEntryTypeElement(), -1);
  }

  protected void composeDeviceComponent(Complex parent, String parentType, String name, DeviceComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "DeviceComponent", name, element, index);
    for (int i = 0; i < element.getIdentifier().size(); i++)
      composeIdentifier(t, "DeviceComponent", "identifier", element.getIdentifier().get(i), i);
    if (element.hasType())
      composeCodeableConcept(t, "DeviceComponent", "type", element.getType(), -1);
    if (element.hasLastSystemChangeElement())
      composeInstant(t, "DeviceComponent", "lastSystemChange", element.getLastSystemChangeElement(), -1);
    if (element.hasSource())
      composeReference(t, "DeviceComponent", "source", element.getSource(), -1);
    if (element.hasParent())
      composeReference(t, "DeviceComponent", "parent", element.getParent(), -1);
    for (int i = 0; i < element.getOperationalStatus().size(); i++)
      composeCodeableConcept(t, "DeviceComponent", "operationalStatus", element.getOperationalStatus().get(i), i);
    if (element.hasParameterGroup())
      composeCodeableConcept(t, "DeviceComponent", "parameterGroup", element.getParameterGroup(), -1);
    if (element.hasMeasurementPrincipleElement())
      composeEnum(t, "DeviceComponent", "measurementPrinciple", element.getMeasurementPrincipleElement(), -1);
    for (int i = 0; i < element.getProductionSpecification().size(); i++)
      composeDeviceComponentDeviceComponentProductionSpecificationComponent(t, "DeviceComponent", "productionSpecification", element.getProductionSpecification().get(i), i);
    if (element.hasLanguageCode())
      composeCodeableConcept(t, "DeviceComponent", "languageCode", element.getLanguageCode(), -1);
    for (int i = 0; i < element.getProperty().size(); i++)
      composeDeviceComponentDeviceComponentPropertyComponent(t, "DeviceComponent", "property", element.getProperty().get(i), i);
  }

  protected void composeDeviceComponentDeviceComponentProductionSpecificationComponent(Complex parent, String parentType, String name, DeviceComponent.DeviceComponentProductionSpecificationComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "productionSpecification", name, element, index);
    if (element.hasSpecType())
      composeCodeableConcept(t, "DeviceComponent", "specType", element.getSpecType(), -1);
    if (element.hasComponentId())
      composeIdentifier(t, "DeviceComponent", "componentId", element.getComponentId(), -1);
    if (element.hasProductionSpecElement())
      composeString(t, "DeviceComponent", "productionSpec", element.getProductionSpecElement(), -1);
  }

  protected void composeDeviceComponentDeviceComponentPropertyComponent(Complex parent, String parentType, String name, DeviceComponent.DeviceComponentPropertyComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "property", name, element, index);
    if (element.hasType())
      composeCodeableConcept(t, "DeviceComponent", "type", element.getType(), -1);
    for (int i = 0; i < element.getValueQuantity().size(); i++)
      composeQuantity(t, "DeviceComponent", "valueQuantity", element.getValueQuantity().get(i), i);
    for (int i = 0; i < element.getValueCode().size(); i++)
      composeCodeableConcept(t, "DeviceComponent", "valueCode", element.getValueCode().get(i), i);
  }

  protected void composeDeviceMetric(Complex parent, String parentType, String name, DeviceMetric element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "DeviceMetric", name, element, index);
    for (int i = 0; i < element.getIdentifier().size(); i++)
      composeIdentifier(t, "DeviceMetric", "identifier", element.getIdentifier().get(i), i);
    if (element.hasType())
      composeCodeableConcept(t, "DeviceMetric", "type", element.getType(), -1);
    if (element.hasUnit())
      composeCodeableConcept(t, "DeviceMetric", "unit", element.getUnit(), -1);
    if (element.hasSource())
      composeReference(t, "DeviceMetric", "source", element.getSource(), -1);
    if (element.hasParent())
      composeReference(t, "DeviceMetric", "parent", element.getParent(), -1);
    if (element.hasOperationalStatusElement())
      composeEnum(t, "DeviceMetric", "operationalStatus", element.getOperationalStatusElement(), -1);
    if (element.hasColorElement())
      composeEnum(t, "DeviceMetric", "color", element.getColorElement(), -1);
    if (element.hasCategoryElement())
      composeEnum(t, "DeviceMetric", "category", element.getCategoryElement(), -1);
    if (element.hasMeasurementPeriod())
      composeTiming(t, "DeviceMetric", "measurementPeriod", element.getMeasurementPeriod(), -1);
    for (int i = 0; i < element.getCalibration().size(); i++)
      composeDeviceMetricDeviceMetricCalibrationComponent(t, "DeviceMetric", "calibration", element.getCalibration().get(i), i);
  }

  protected void composeDeviceMetricDeviceMetricCalibrationComponent(Complex parent, String parentType, String name, DeviceMetric.DeviceMetricCalibrationComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "calibration", name, element, index);
    if (element.hasTypeElement())
      composeEnum(t, "DeviceMetric", "type", element.getTypeElement(), -1);
    if (element.hasStateElement())
      composeEnum(t, "DeviceMetric", "state", element.getStateElement(), -1);
    if (element.hasTimeElement())
      composeInstant(t, "DeviceMetric", "time", element.getTimeElement(), -1);
  }

  protected void composeDeviceRequest(Complex parent, String parentType, String name, DeviceRequest element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "DeviceRequest", name, element, index);
    for (int i = 0; i < element.getIdentifier().size(); i++)
      composeIdentifier(t, "DeviceRequest", "identifier", element.getIdentifier().get(i), i);
    for (int i = 0; i < element.getDefinition().size(); i++)
      composeReference(t, "DeviceRequest", "definition", element.getDefinition().get(i), i);
    for (int i = 0; i < element.getBasedOn().size(); i++)
      composeReference(t, "DeviceRequest", "basedOn", element.getBasedOn().get(i), i);
    for (int i = 0; i < element.getPriorRequest().size(); i++)
      composeReference(t, "DeviceRequest", "priorRequest", element.getPriorRequest().get(i), i);
    if (element.hasGroupIdentifier())
      composeIdentifier(t, "DeviceRequest", "groupIdentifier", element.getGroupIdentifier(), -1);
    if (element.hasStatusElement())
      composeEnum(t, "DeviceRequest", "status", element.getStatusElement(), -1);
    if (element.hasIntent())
      composeCodeableConcept(t, "DeviceRequest", "intent", element.getIntent(), -1);
    if (element.hasPriorityElement())
      composeEnum(t, "DeviceRequest", "priority", element.getPriorityElement(), -1);
    if (element.hasCode())
      composeType(t, "DeviceRequest", "code", element.getCode(), -1);
    if (element.hasSubject())
      composeReference(t, "DeviceRequest", "subject", element.getSubject(), -1);
    if (element.hasContext())
      composeReference(t, "DeviceRequest", "context", element.getContext(), -1);
    if (element.hasOccurrence())
      composeType(t, "DeviceRequest", "occurrence", element.getOccurrence(), -1);
    if (element.hasAuthoredOnElement())
      composeDateTime(t, "DeviceRequest", "authoredOn", element.getAuthoredOnElement(), -1);
    if (element.hasRequester())
      composeDeviceRequestDeviceRequestRequesterComponent(t, "DeviceRequest", "requester", element.getRequester(), -1);
    if (element.hasPerformerType())
      composeCodeableConcept(t, "DeviceRequest", "performerType", element.getPerformerType(), -1);
    if (element.hasPerformer())
      composeReference(t, "DeviceRequest", "performer", element.getPerformer(), -1);
    for (int i = 0; i < element.getReasonCode().size(); i++)
      composeCodeableConcept(t, "DeviceRequest", "reasonCode", element.getReasonCode().get(i), i);
    for (int i = 0; i < element.getReasonReference().size(); i++)
      composeReference(t, "DeviceRequest", "reasonReference", element.getReasonReference().get(i), i);
    for (int i = 0; i < element.getSupportingInfo().size(); i++)
      composeReference(t, "DeviceRequest", "supportingInfo", element.getSupportingInfo().get(i), i);
    for (int i = 0; i < element.getNote().size(); i++)
      composeAnnotation(t, "DeviceRequest", "note", element.getNote().get(i), i);
    for (int i = 0; i < element.getRelevantHistory().size(); i++)
      composeReference(t, "DeviceRequest", "relevantHistory", element.getRelevantHistory().get(i), i);
  }

  protected void composeDeviceRequestDeviceRequestRequesterComponent(Complex parent, String parentType, String name, DeviceRequest.DeviceRequestRequesterComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "requester", name, element, index);
    if (element.hasAgent())
      composeReference(t, "DeviceRequest", "agent", element.getAgent(), -1);
    if (element.hasOnBehalfOf())
      composeReference(t, "DeviceRequest", "onBehalfOf", element.getOnBehalfOf(), -1);
  }

  protected void composeDeviceUseStatement(Complex parent, String parentType, String name, DeviceUseStatement element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "DeviceUseStatement", name, element, index);
    for (int i = 0; i < element.getIdentifier().size(); i++)
      composeIdentifier(t, "DeviceUseStatement", "identifier", element.getIdentifier().get(i), i);
    if (element.hasStatusElement())
      composeEnum(t, "DeviceUseStatement", "status", element.getStatusElement(), -1);
    if (element.hasSubject())
      composeReference(t, "DeviceUseStatement", "subject", element.getSubject(), -1);
    if (element.hasWhenUsed())
      composePeriod(t, "DeviceUseStatement", "whenUsed", element.getWhenUsed(), -1);
    if (element.hasTiming())
      composeType(t, "DeviceUseStatement", "timing", element.getTiming(), -1);
    if (element.hasRecordedOnElement())
      composeDateTime(t, "DeviceUseStatement", "recordedOn", element.getRecordedOnElement(), -1);
    if (element.hasSource())
      composeReference(t, "DeviceUseStatement", "source", element.getSource(), -1);
    if (element.hasDevice())
      composeReference(t, "DeviceUseStatement", "device", element.getDevice(), -1);
    for (int i = 0; i < element.getIndication().size(); i++)
      composeCodeableConcept(t, "DeviceUseStatement", "indication", element.getIndication().get(i), i);
    if (element.hasBodySite())
      composeCodeableConcept(t, "DeviceUseStatement", "bodySite", element.getBodySite(), -1);
    for (int i = 0; i < element.getNote().size(); i++)
      composeAnnotation(t, "DeviceUseStatement", "note", element.getNote().get(i), i);
  }

  protected void composeDiagnosticReport(Complex parent, String parentType, String name, DiagnosticReport element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "DiagnosticReport", name, element, index);
    for (int i = 0; i < element.getIdentifier().size(); i++)
      composeIdentifier(t, "DiagnosticReport", "identifier", element.getIdentifier().get(i), i);
    for (int i = 0; i < element.getBasedOn().size(); i++)
      composeReference(t, "DiagnosticReport", "basedOn", element.getBasedOn().get(i), i);
    if (element.hasStatusElement())
      composeEnum(t, "DiagnosticReport", "status", element.getStatusElement(), -1);
    if (element.hasCategory())
      composeCodeableConcept(t, "DiagnosticReport", "category", element.getCategory(), -1);
    if (element.hasCode())
      composeCodeableConcept(t, "DiagnosticReport", "code", element.getCode(), -1);
    if (element.hasSubject())
      composeReference(t, "DiagnosticReport", "subject", element.getSubject(), -1);
    if (element.hasContext())
      composeReference(t, "DiagnosticReport", "context", element.getContext(), -1);
    if (element.hasEffective())
      composeType(t, "DiagnosticReport", "effective", element.getEffective(), -1);
    if (element.hasIssuedElement())
      composeInstant(t, "DiagnosticReport", "issued", element.getIssuedElement(), -1);
    for (int i = 0; i < element.getPerformer().size(); i++)
      composeReference(t, "DiagnosticReport", "performer", element.getPerformer().get(i), i);
    for (int i = 0; i < element.getResultsInterpreter().size(); i++)
      composeReference(t, "DiagnosticReport", "resultsInterpreter", element.getResultsInterpreter().get(i), i);
    for (int i = 0; i < element.getSpecimen().size(); i++)
      composeReference(t, "DiagnosticReport", "specimen", element.getSpecimen().get(i), i);
    for (int i = 0; i < element.getResult().size(); i++)
      composeReference(t, "DiagnosticReport", "result", element.getResult().get(i), i);
    for (int i = 0; i < element.getImagingStudy().size(); i++)
      composeReference(t, "DiagnosticReport", "imagingStudy", element.getImagingStudy().get(i), i);
    for (int i = 0; i < element.getImage().size(); i++)
      composeDiagnosticReportDiagnosticReportImageComponent(t, "DiagnosticReport", "image", element.getImage().get(i), i);
    if (element.hasConclusionElement())
      composeString(t, "DiagnosticReport", "conclusion", element.getConclusionElement(), -1);
    for (int i = 0; i < element.getCodedDiagnosis().size(); i++)
      composeCodeableConcept(t, "DiagnosticReport", "codedDiagnosis", element.getCodedDiagnosis().get(i), i);
    for (int i = 0; i < element.getPresentedForm().size(); i++)
      composeAttachment(t, "DiagnosticReport", "presentedForm", element.getPresentedForm().get(i), i);
  }

  protected void composeDiagnosticReportDiagnosticReportImageComponent(Complex parent, String parentType, String name, DiagnosticReport.DiagnosticReportImageComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "image", name, element, index);
    if (element.hasCommentElement())
      composeString(t, "DiagnosticReport", "comment", element.getCommentElement(), -1);
    if (element.hasLink())
      composeReference(t, "DiagnosticReport", "link", element.getLink(), -1);
  }

  protected void composeDocumentManifest(Complex parent, String parentType, String name, DocumentManifest element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "DocumentManifest", name, element, index);
    if (element.hasMasterIdentifier())
      composeIdentifier(t, "DocumentManifest", "masterIdentifier", element.getMasterIdentifier(), -1);
    for (int i = 0; i < element.getIdentifier().size(); i++)
      composeIdentifier(t, "DocumentManifest", "identifier", element.getIdentifier().get(i), i);
    if (element.hasStatusElement())
      composeEnum(t, "DocumentManifest", "status", element.getStatusElement(), -1);
    if (element.hasType())
      composeCodeableConcept(t, "DocumentManifest", "type", element.getType(), -1);
    if (element.hasSubject())
      composeReference(t, "DocumentManifest", "subject", element.getSubject(), -1);
    if (element.hasCreatedElement())
      composeDateTime(t, "DocumentManifest", "created", element.getCreatedElement(), -1);
    for (int i = 0; i < element.getAgent().size(); i++)
      composeDocumentManifestDocumentManifestAgentComponent(t, "DocumentManifest", "agent", element.getAgent().get(i), i);
    for (int i = 0; i < element.getRecipient().size(); i++)
      composeReference(t, "DocumentManifest", "recipient", element.getRecipient().get(i), i);
    if (element.hasSourceElement())
      composeUri(t, "DocumentManifest", "source", element.getSourceElement(), -1);
    if (element.hasDescriptionElement())
      composeString(t, "DocumentManifest", "description", element.getDescriptionElement(), -1);
    for (int i = 0; i < element.getContent().size(); i++)
      composeReference(t, "DocumentManifest", "content", element.getContent().get(i), i);
    for (int i = 0; i < element.getRelated().size(); i++)
      composeDocumentManifestDocumentManifestRelatedComponent(t, "DocumentManifest", "related", element.getRelated().get(i), i);
  }

  protected void composeDocumentManifestDocumentManifestAgentComponent(Complex parent, String parentType, String name, DocumentManifest.DocumentManifestAgentComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "agent", name, element, index);
    if (element.hasType())
      composeCodeableConcept(t, "DocumentManifest", "type", element.getType(), -1);
    if (element.hasWho())
      composeReference(t, "DocumentManifest", "who", element.getWho(), -1);
  }

  protected void composeDocumentManifestDocumentManifestRelatedComponent(Complex parent, String parentType, String name, DocumentManifest.DocumentManifestRelatedComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "related", name, element, index);
    if (element.hasIdentifier())
      composeIdentifier(t, "DocumentManifest", "identifier", element.getIdentifier(), -1);
    if (element.hasRef())
      composeReference(t, "DocumentManifest", "ref", element.getRef(), -1);
  }

  protected void composeDocumentReference(Complex parent, String parentType, String name, DocumentReference element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "DocumentReference", name, element, index);
    if (element.hasMasterIdentifier())
      composeIdentifier(t, "DocumentReference", "masterIdentifier", element.getMasterIdentifier(), -1);
    for (int i = 0; i < element.getIdentifier().size(); i++)
      composeIdentifier(t, "DocumentReference", "identifier", element.getIdentifier().get(i), i);
    if (element.hasStatusElement())
      composeEnum(t, "DocumentReference", "status", element.getStatusElement(), -1);
    if (element.hasDocStatusElement())
      composeEnum(t, "DocumentReference", "docStatus", element.getDocStatusElement(), -1);
    if (element.hasType())
      composeCodeableConcept(t, "DocumentReference", "type", element.getType(), -1);
    if (element.hasClass_())
      composeCodeableConcept(t, "DocumentReference", "class", element.getClass_(), -1);
    if (element.hasSubject())
      composeReference(t, "DocumentReference", "subject", element.getSubject(), -1);
    if (element.hasCreatedElement())
      composeDateTime(t, "DocumentReference", "created", element.getCreatedElement(), -1);
    if (element.hasDateElement())
      composeInstant(t, "DocumentReference", "date", element.getDateElement(), -1);
    for (int i = 0; i < element.getAgent().size(); i++)
      composeDocumentReferenceDocumentReferenceAgentComponent(t, "DocumentReference", "agent", element.getAgent().get(i), i);
    if (element.hasAuthenticator())
      composeReference(t, "DocumentReference", "authenticator", element.getAuthenticator(), -1);
    if (element.hasCustodian())
      composeReference(t, "DocumentReference", "custodian", element.getCustodian(), -1);
    for (int i = 0; i < element.getRelatesTo().size(); i++)
      composeDocumentReferenceDocumentReferenceRelatesToComponent(t, "DocumentReference", "relatesTo", element.getRelatesTo().get(i), i);
    if (element.hasDescriptionElement())
      composeString(t, "DocumentReference", "description", element.getDescriptionElement(), -1);
    for (int i = 0; i < element.getSecurityLabel().size(); i++)
      composeCodeableConcept(t, "DocumentReference", "securityLabel", element.getSecurityLabel().get(i), i);
    for (int i = 0; i < element.getContent().size(); i++)
      composeDocumentReferenceDocumentReferenceContentComponent(t, "DocumentReference", "content", element.getContent().get(i), i);
    if (element.hasContext())
      composeDocumentReferenceDocumentReferenceContextComponent(t, "DocumentReference", "context", element.getContext(), -1);
  }

  protected void composeDocumentReferenceDocumentReferenceAgentComponent(Complex parent, String parentType, String name, DocumentReference.DocumentReferenceAgentComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "agent", name, element, index);
    if (element.hasType())
      composeCodeableConcept(t, "DocumentReference", "type", element.getType(), -1);
    if (element.hasWho())
      composeReference(t, "DocumentReference", "who", element.getWho(), -1);
  }

  protected void composeDocumentReferenceDocumentReferenceRelatesToComponent(Complex parent, String parentType, String name, DocumentReference.DocumentReferenceRelatesToComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "relatesTo", name, element, index);
    if (element.hasCodeElement())
      composeEnum(t, "DocumentReference", "code", element.getCodeElement(), -1);
    if (element.hasTarget())
      composeReference(t, "DocumentReference", "target", element.getTarget(), -1);
  }

  protected void composeDocumentReferenceDocumentReferenceContentComponent(Complex parent, String parentType, String name, DocumentReference.DocumentReferenceContentComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "content", name, element, index);
    if (element.hasAttachment())
      composeAttachment(t, "DocumentReference", "attachment", element.getAttachment(), -1);
    if (element.hasFormat())
      composeCoding(t, "DocumentReference", "format", element.getFormat(), -1);
  }

  protected void composeDocumentReferenceDocumentReferenceContextComponent(Complex parent, String parentType, String name, DocumentReference.DocumentReferenceContextComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "context", name, element, index);
    if (element.hasEncounter())
      composeReference(t, "DocumentReference", "encounter", element.getEncounter(), -1);
    for (int i = 0; i < element.getEvent().size(); i++)
      composeCodeableConcept(t, "DocumentReference", "event", element.getEvent().get(i), i);
    if (element.hasPeriod())
      composePeriod(t, "DocumentReference", "period", element.getPeriod(), -1);
    if (element.hasFacilityType())
      composeCodeableConcept(t, "DocumentReference", "facilityType", element.getFacilityType(), -1);
    if (element.hasPracticeSetting())
      composeCodeableConcept(t, "DocumentReference", "practiceSetting", element.getPracticeSetting(), -1);
    if (element.hasSourcePatientInfo())
      composeReference(t, "DocumentReference", "sourcePatientInfo", element.getSourcePatientInfo(), -1);
    for (int i = 0; i < element.getRelated().size(); i++)
      composeDocumentReferenceDocumentReferenceContextRelatedComponent(t, "DocumentReference", "related", element.getRelated().get(i), i);
  }

  protected void composeDocumentReferenceDocumentReferenceContextRelatedComponent(Complex parent, String parentType, String name, DocumentReference.DocumentReferenceContextRelatedComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "related", name, element, index);
    if (element.hasIdentifier())
      composeIdentifier(t, "DocumentReference", "identifier", element.getIdentifier(), -1);
    if (element.hasRef())
      composeReference(t, "DocumentReference", "ref", element.getRef(), -1);
  }

  protected void composeEligibilityRequest(Complex parent, String parentType, String name, EligibilityRequest element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "EligibilityRequest", name, element, index);
    for (int i = 0; i < element.getIdentifier().size(); i++)
      composeIdentifier(t, "EligibilityRequest", "identifier", element.getIdentifier().get(i), i);
    if (element.hasStatusElement())
      composeEnum(t, "EligibilityRequest", "status", element.getStatusElement(), -1);
    if (element.hasPriority())
      composeCodeableConcept(t, "EligibilityRequest", "priority", element.getPriority(), -1);
    if (element.hasPatient())
      composeReference(t, "EligibilityRequest", "patient", element.getPatient(), -1);
    if (element.hasServiced())
      composeType(t, "EligibilityRequest", "serviced", element.getServiced(), -1);
    if (element.hasCreatedElement())
      composeDateTime(t, "EligibilityRequest", "created", element.getCreatedElement(), -1);
    if (element.hasEnterer())
      composeReference(t, "EligibilityRequest", "enterer", element.getEnterer(), -1);
    if (element.hasProvider())
      composeReference(t, "EligibilityRequest", "provider", element.getProvider(), -1);
    if (element.hasOrganization())
      composeReference(t, "EligibilityRequest", "organization", element.getOrganization(), -1);
    if (element.hasInsurer())
      composeReference(t, "EligibilityRequest", "insurer", element.getInsurer(), -1);
    if (element.hasFacility())
      composeReference(t, "EligibilityRequest", "facility", element.getFacility(), -1);
    if (element.hasCoverage())
      composeReference(t, "EligibilityRequest", "coverage", element.getCoverage(), -1);
    if (element.hasBusinessArrangementElement())
      composeString(t, "EligibilityRequest", "businessArrangement", element.getBusinessArrangementElement(), -1);
    if (element.hasBenefitCategory())
      composeCodeableConcept(t, "EligibilityRequest", "benefitCategory", element.getBenefitCategory(), -1);
    if (element.hasBenefitSubCategory())
      composeCodeableConcept(t, "EligibilityRequest", "benefitSubCategory", element.getBenefitSubCategory(), -1);
  }

  protected void composeEligibilityResponse(Complex parent, String parentType, String name, EligibilityResponse element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "EligibilityResponse", name, element, index);
    for (int i = 0; i < element.getIdentifier().size(); i++)
      composeIdentifier(t, "EligibilityResponse", "identifier", element.getIdentifier().get(i), i);
    if (element.hasStatusElement())
      composeEnum(t, "EligibilityResponse", "status", element.getStatusElement(), -1);
    if (element.hasCreatedElement())
      composeDateTime(t, "EligibilityResponse", "created", element.getCreatedElement(), -1);
    if (element.hasRequestProvider())
      composeReference(t, "EligibilityResponse", "requestProvider", element.getRequestProvider(), -1);
    if (element.hasRequestOrganization())
      composeReference(t, "EligibilityResponse", "requestOrganization", element.getRequestOrganization(), -1);
    if (element.hasRequest())
      composeReference(t, "EligibilityResponse", "request", element.getRequest(), -1);
    if (element.hasOutcomeElement())
      composeEnum(t, "EligibilityResponse", "outcome", element.getOutcomeElement(), -1);
    if (element.hasDispositionElement())
      composeString(t, "EligibilityResponse", "disposition", element.getDispositionElement(), -1);
    if (element.hasInsurer())
      composeReference(t, "EligibilityResponse", "insurer", element.getInsurer(), -1);
    if (element.hasInforceElement())
      composeBoolean(t, "EligibilityResponse", "inforce", element.getInforceElement(), -1);
    for (int i = 0; i < element.getInsurance().size(); i++)
      composeEligibilityResponseInsuranceComponent(t, "EligibilityResponse", "insurance", element.getInsurance().get(i), i);
    if (element.hasForm())
      composeCodeableConcept(t, "EligibilityResponse", "form", element.getForm(), -1);
    for (int i = 0; i < element.getError().size(); i++)
      composeEligibilityResponseErrorsComponent(t, "EligibilityResponse", "error", element.getError().get(i), i);
  }

  protected void composeEligibilityResponseInsuranceComponent(Complex parent, String parentType, String name, EligibilityResponse.InsuranceComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "insurance", name, element, index);
    if (element.hasCoverage())
      composeReference(t, "EligibilityResponse", "coverage", element.getCoverage(), -1);
    if (element.hasContract())
      composeReference(t, "EligibilityResponse", "contract", element.getContract(), -1);
    for (int i = 0; i < element.getBenefitBalance().size(); i++)
      composeEligibilityResponseBenefitsComponent(t, "EligibilityResponse", "benefitBalance", element.getBenefitBalance().get(i), i);
  }

  protected void composeEligibilityResponseBenefitsComponent(Complex parent, String parentType, String name, EligibilityResponse.BenefitsComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "benefitBalance", name, element, index);
    if (element.hasCategory())
      composeCodeableConcept(t, "EligibilityResponse", "category", element.getCategory(), -1);
    if (element.hasSubCategory())
      composeCodeableConcept(t, "EligibilityResponse", "subCategory", element.getSubCategory(), -1);
    if (element.hasExcludedElement())
      composeBoolean(t, "EligibilityResponse", "excluded", element.getExcludedElement(), -1);
    if (element.hasNameElement())
      composeString(t, "EligibilityResponse", "name", element.getNameElement(), -1);
    if (element.hasDescriptionElement())
      composeString(t, "EligibilityResponse", "description", element.getDescriptionElement(), -1);
    if (element.hasNetwork())
      composeCodeableConcept(t, "EligibilityResponse", "network", element.getNetwork(), -1);
    if (element.hasUnit())
      composeCodeableConcept(t, "EligibilityResponse", "unit", element.getUnit(), -1);
    if (element.hasTerm())
      composeCodeableConcept(t, "EligibilityResponse", "term", element.getTerm(), -1);
    for (int i = 0; i < element.getFinancial().size(); i++)
      composeEligibilityResponseBenefitComponent(t, "EligibilityResponse", "financial", element.getFinancial().get(i), i);
  }

  protected void composeEligibilityResponseBenefitComponent(Complex parent, String parentType, String name, EligibilityResponse.BenefitComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "financial", name, element, index);
    if (element.hasType())
      composeCodeableConcept(t, "EligibilityResponse", "type", element.getType(), -1);
    if (element.hasAllowed())
      composeType(t, "EligibilityResponse", "allowed", element.getAllowed(), -1);
    if (element.hasUsed())
      composeType(t, "EligibilityResponse", "used", element.getUsed(), -1);
  }

  protected void composeEligibilityResponseErrorsComponent(Complex parent, String parentType, String name, EligibilityResponse.ErrorsComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "error", name, element, index);
    if (element.hasCode())
      composeCodeableConcept(t, "EligibilityResponse", "code", element.getCode(), -1);
  }

  protected void composeEncounter(Complex parent, String parentType, String name, Encounter element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "Encounter", name, element, index);
    for (int i = 0; i < element.getIdentifier().size(); i++)
      composeIdentifier(t, "Encounter", "identifier", element.getIdentifier().get(i), i);
    if (element.hasStatusElement())
      composeEnum(t, "Encounter", "status", element.getStatusElement(), -1);
    for (int i = 0; i < element.getStatusHistory().size(); i++)
      composeEncounterStatusHistoryComponent(t, "Encounter", "statusHistory", element.getStatusHistory().get(i), i);
    if (element.hasClass_())
      composeCoding(t, "Encounter", "class", element.getClass_(), -1);
    for (int i = 0; i < element.getClassHistory().size(); i++)
      composeEncounterClassHistoryComponent(t, "Encounter", "classHistory", element.getClassHistory().get(i), i);
    for (int i = 0; i < element.getType().size(); i++)
      composeCodeableConcept(t, "Encounter", "type", element.getType().get(i), i);
    if (element.hasPriority())
      composeCodeableConcept(t, "Encounter", "priority", element.getPriority(), -1);
    if (element.hasSubject())
      composeReference(t, "Encounter", "subject", element.getSubject(), -1);
    for (int i = 0; i < element.getEpisodeOfCare().size(); i++)
      composeReference(t, "Encounter", "episodeOfCare", element.getEpisodeOfCare().get(i), i);
    for (int i = 0; i < element.getIncomingReferral().size(); i++)
      composeReference(t, "Encounter", "incomingReferral", element.getIncomingReferral().get(i), i);
    for (int i = 0; i < element.getParticipant().size(); i++)
      composeEncounterEncounterParticipantComponent(t, "Encounter", "participant", element.getParticipant().get(i), i);
    if (element.hasAppointment())
      composeReference(t, "Encounter", "appointment", element.getAppointment(), -1);
    if (element.hasPeriod())
      composePeriod(t, "Encounter", "period", element.getPeriod(), -1);
    if (element.hasLength())
      composeDuration(t, "Encounter", "length", element.getLength(), -1);
    for (int i = 0; i < element.getReason().size(); i++)
      composeCodeableConcept(t, "Encounter", "reason", element.getReason().get(i), i);
    for (int i = 0; i < element.getDiagnosis().size(); i++)
      composeEncounterDiagnosisComponent(t, "Encounter", "diagnosis", element.getDiagnosis().get(i), i);
    for (int i = 0; i < element.getAccount().size(); i++)
      composeReference(t, "Encounter", "account", element.getAccount().get(i), i);
    if (element.hasHospitalization())
      composeEncounterEncounterHospitalizationComponent(t, "Encounter", "hospitalization", element.getHospitalization(), -1);
    for (int i = 0; i < element.getLocation().size(); i++)
      composeEncounterEncounterLocationComponent(t, "Encounter", "location", element.getLocation().get(i), i);
    if (element.hasServiceProvider())
      composeReference(t, "Encounter", "serviceProvider", element.getServiceProvider(), -1);
    if (element.hasPartOf())
      composeReference(t, "Encounter", "partOf", element.getPartOf(), -1);
  }

  protected void composeEncounterStatusHistoryComponent(Complex parent, String parentType, String name, Encounter.StatusHistoryComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "statusHistory", name, element, index);
    if (element.hasStatusElement())
      composeEnum(t, "Encounter", "status", element.getStatusElement(), -1);
    if (element.hasPeriod())
      composePeriod(t, "Encounter", "period", element.getPeriod(), -1);
  }

  protected void composeEncounterClassHistoryComponent(Complex parent, String parentType, String name, Encounter.ClassHistoryComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "classHistory", name, element, index);
    if (element.hasClass_())
      composeCoding(t, "Encounter", "class", element.getClass_(), -1);
    if (element.hasPeriod())
      composePeriod(t, "Encounter", "period", element.getPeriod(), -1);
  }

  protected void composeEncounterEncounterParticipantComponent(Complex parent, String parentType, String name, Encounter.EncounterParticipantComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "participant", name, element, index);
    for (int i = 0; i < element.getType().size(); i++)
      composeCodeableConcept(t, "Encounter", "type", element.getType().get(i), i);
    if (element.hasPeriod())
      composePeriod(t, "Encounter", "period", element.getPeriod(), -1);
    if (element.hasIndividual())
      composeReference(t, "Encounter", "individual", element.getIndividual(), -1);
  }

  protected void composeEncounterDiagnosisComponent(Complex parent, String parentType, String name, Encounter.DiagnosisComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "diagnosis", name, element, index);
    if (element.hasCondition())
      composeReference(t, "Encounter", "condition", element.getCondition(), -1);
    if (element.hasRole())
      composeCodeableConcept(t, "Encounter", "role", element.getRole(), -1);
    if (element.hasRankElement())
      composePositiveInt(t, "Encounter", "rank", element.getRankElement(), -1);
  }

  protected void composeEncounterEncounterHospitalizationComponent(Complex parent, String parentType, String name, Encounter.EncounterHospitalizationComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "hospitalization", name, element, index);
    if (element.hasPreAdmissionIdentifier())
      composeIdentifier(t, "Encounter", "preAdmissionIdentifier", element.getPreAdmissionIdentifier(), -1);
    if (element.hasOrigin())
      composeReference(t, "Encounter", "origin", element.getOrigin(), -1);
    if (element.hasAdmitSource())
      composeCodeableConcept(t, "Encounter", "admitSource", element.getAdmitSource(), -1);
    if (element.hasReAdmission())
      composeCodeableConcept(t, "Encounter", "reAdmission", element.getReAdmission(), -1);
    for (int i = 0; i < element.getDietPreference().size(); i++)
      composeCodeableConcept(t, "Encounter", "dietPreference", element.getDietPreference().get(i), i);
    for (int i = 0; i < element.getSpecialCourtesy().size(); i++)
      composeCodeableConcept(t, "Encounter", "specialCourtesy", element.getSpecialCourtesy().get(i), i);
    for (int i = 0; i < element.getSpecialArrangement().size(); i++)
      composeCodeableConcept(t, "Encounter", "specialArrangement", element.getSpecialArrangement().get(i), i);
    if (element.hasDestination())
      composeReference(t, "Encounter", "destination", element.getDestination(), -1);
    if (element.hasDischargeDisposition())
      composeCodeableConcept(t, "Encounter", "dischargeDisposition", element.getDischargeDisposition(), -1);
  }

  protected void composeEncounterEncounterLocationComponent(Complex parent, String parentType, String name, Encounter.EncounterLocationComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "location", name, element, index);
    if (element.hasLocation())
      composeReference(t, "Encounter", "location", element.getLocation(), -1);
    if (element.hasStatusElement())
      composeEnum(t, "Encounter", "status", element.getStatusElement(), -1);
    if (element.hasPeriod())
      composePeriod(t, "Encounter", "period", element.getPeriod(), -1);
  }

  protected void composeEndpoint(Complex parent, String parentType, String name, Endpoint element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "Endpoint", name, element, index);
    for (int i = 0; i < element.getIdentifier().size(); i++)
      composeIdentifier(t, "Endpoint", "identifier", element.getIdentifier().get(i), i);
    if (element.hasStatusElement())
      composeEnum(t, "Endpoint", "status", element.getStatusElement(), -1);
    if (element.hasConnectionType())
      composeCoding(t, "Endpoint", "connectionType", element.getConnectionType(), -1);
    if (element.hasNameElement())
      composeString(t, "Endpoint", "name", element.getNameElement(), -1);
    if (element.hasManagingOrganization())
      composeReference(t, "Endpoint", "managingOrganization", element.getManagingOrganization(), -1);
    for (int i = 0; i < element.getContact().size(); i++)
      composeContactPoint(t, "Endpoint", "contact", element.getContact().get(i), i);
    if (element.hasPeriod())
      composePeriod(t, "Endpoint", "period", element.getPeriod(), -1);
    for (int i = 0; i < element.getPayloadType().size(); i++)
      composeCodeableConcept(t, "Endpoint", "payloadType", element.getPayloadType().get(i), i);
    for (int i = 0; i < element.getPayloadMimeType().size(); i++)
      composeCode(t, "Endpoint", "payloadMimeType", element.getPayloadMimeType().get(i), i);
    if (element.hasAddressElement())
      composeUri(t, "Endpoint", "address", element.getAddressElement(), -1);
    for (int i = 0; i < element.getHeader().size(); i++)
      composeString(t, "Endpoint", "header", element.getHeader().get(i), i);
  }

  protected void composeEnrollmentRequest(Complex parent, String parentType, String name, EnrollmentRequest element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "EnrollmentRequest", name, element, index);
    for (int i = 0; i < element.getIdentifier().size(); i++)
      composeIdentifier(t, "EnrollmentRequest", "identifier", element.getIdentifier().get(i), i);
    if (element.hasStatusElement())
      composeEnum(t, "EnrollmentRequest", "status", element.getStatusElement(), -1);
    if (element.hasCreatedElement())
      composeDateTime(t, "EnrollmentRequest", "created", element.getCreatedElement(), -1);
    if (element.hasInsurer())
      composeReference(t, "EnrollmentRequest", "insurer", element.getInsurer(), -1);
    if (element.hasProvider())
      composeReference(t, "EnrollmentRequest", "provider", element.getProvider(), -1);
    if (element.hasOrganization())
      composeReference(t, "EnrollmentRequest", "organization", element.getOrganization(), -1);
    if (element.hasCandidate())
      composeReference(t, "EnrollmentRequest", "candidate", element.getCandidate(), -1);
    if (element.hasCoverage())
      composeReference(t, "EnrollmentRequest", "coverage", element.getCoverage(), -1);
  }

  protected void composeEnrollmentResponse(Complex parent, String parentType, String name, EnrollmentResponse element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "EnrollmentResponse", name, element, index);
    for (int i = 0; i < element.getIdentifier().size(); i++)
      composeIdentifier(t, "EnrollmentResponse", "identifier", element.getIdentifier().get(i), i);
    if (element.hasStatusElement())
      composeEnum(t, "EnrollmentResponse", "status", element.getStatusElement(), -1);
    if (element.hasRequest())
      composeReference(t, "EnrollmentResponse", "request", element.getRequest(), -1);
    if (element.hasOutcomeElement())
      composeEnum(t, "EnrollmentResponse", "outcome", element.getOutcomeElement(), -1);
    if (element.hasDispositionElement())
      composeString(t, "EnrollmentResponse", "disposition", element.getDispositionElement(), -1);
    if (element.hasCreatedElement())
      composeDateTime(t, "EnrollmentResponse", "created", element.getCreatedElement(), -1);
    if (element.hasOrganization())
      composeReference(t, "EnrollmentResponse", "organization", element.getOrganization(), -1);
    if (element.hasRequestProvider())
      composeReference(t, "EnrollmentResponse", "requestProvider", element.getRequestProvider(), -1);
    if (element.hasRequestOrganization())
      composeReference(t, "EnrollmentResponse", "requestOrganization", element.getRequestOrganization(), -1);
  }

  protected void composeEpisodeOfCare(Complex parent, String parentType, String name, EpisodeOfCare element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "EpisodeOfCare", name, element, index);
    for (int i = 0; i < element.getIdentifier().size(); i++)
      composeIdentifier(t, "EpisodeOfCare", "identifier", element.getIdentifier().get(i), i);
    if (element.hasStatusElement())
      composeEnum(t, "EpisodeOfCare", "status", element.getStatusElement(), -1);
    for (int i = 0; i < element.getStatusHistory().size(); i++)
      composeEpisodeOfCareEpisodeOfCareStatusHistoryComponent(t, "EpisodeOfCare", "statusHistory", element.getStatusHistory().get(i), i);
    for (int i = 0; i < element.getType().size(); i++)
      composeCodeableConcept(t, "EpisodeOfCare", "type", element.getType().get(i), i);
    for (int i = 0; i < element.getDiagnosis().size(); i++)
      composeEpisodeOfCareDiagnosisComponent(t, "EpisodeOfCare", "diagnosis", element.getDiagnosis().get(i), i);
    if (element.hasPatient())
      composeReference(t, "EpisodeOfCare", "patient", element.getPatient(), -1);
    if (element.hasManagingOrganization())
      composeReference(t, "EpisodeOfCare", "managingOrganization", element.getManagingOrganization(), -1);
    if (element.hasPeriod())
      composePeriod(t, "EpisodeOfCare", "period", element.getPeriod(), -1);
    for (int i = 0; i < element.getReferralRequest().size(); i++)
      composeReference(t, "EpisodeOfCare", "referralRequest", element.getReferralRequest().get(i), i);
    if (element.hasCareManager())
      composeReference(t, "EpisodeOfCare", "careManager", element.getCareManager(), -1);
    for (int i = 0; i < element.getTeam().size(); i++)
      composeReference(t, "EpisodeOfCare", "team", element.getTeam().get(i), i);
    for (int i = 0; i < element.getAccount().size(); i++)
      composeReference(t, "EpisodeOfCare", "account", element.getAccount().get(i), i);
  }

  protected void composeEpisodeOfCareEpisodeOfCareStatusHistoryComponent(Complex parent, String parentType, String name, EpisodeOfCare.EpisodeOfCareStatusHistoryComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "statusHistory", name, element, index);
    if (element.hasStatusElement())
      composeEnum(t, "EpisodeOfCare", "status", element.getStatusElement(), -1);
    if (element.hasPeriod())
      composePeriod(t, "EpisodeOfCare", "period", element.getPeriod(), -1);
  }

  protected void composeEpisodeOfCareDiagnosisComponent(Complex parent, String parentType, String name, EpisodeOfCare.DiagnosisComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "diagnosis", name, element, index);
    if (element.hasCondition())
      composeReference(t, "EpisodeOfCare", "condition", element.getCondition(), -1);
    if (element.hasRole())
      composeCodeableConcept(t, "EpisodeOfCare", "role", element.getRole(), -1);
    if (element.hasRankElement())
      composePositiveInt(t, "EpisodeOfCare", "rank", element.getRankElement(), -1);
  }

  protected void composeEventDefinition(Complex parent, String parentType, String name, EventDefinition element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "EventDefinition", name, element, index);
    if (element.hasUrlElement())
      composeUri(t, "EventDefinition", "url", element.getUrlElement(), -1);
    for (int i = 0; i < element.getIdentifier().size(); i++)
      composeIdentifier(t, "EventDefinition", "identifier", element.getIdentifier().get(i), i);
    if (element.hasVersionElement())
      composeString(t, "EventDefinition", "version", element.getVersionElement(), -1);
    if (element.hasNameElement())
      composeString(t, "EventDefinition", "name", element.getNameElement(), -1);
    if (element.hasTitleElement())
      composeString(t, "EventDefinition", "title", element.getTitleElement(), -1);
    if (element.hasStatusElement())
      composeEnum(t, "EventDefinition", "status", element.getStatusElement(), -1);
    if (element.hasExperimentalElement())
      composeBoolean(t, "EventDefinition", "experimental", element.getExperimentalElement(), -1);
    if (element.hasDateElement())
      composeDateTime(t, "EventDefinition", "date", element.getDateElement(), -1);
    if (element.hasPublisherElement())
      composeString(t, "EventDefinition", "publisher", element.getPublisherElement(), -1);
    if (element.hasDescriptionElement())
      composeMarkdown(t, "EventDefinition", "description", element.getDescriptionElement(), -1);
    if (element.hasPurposeElement())
      composeMarkdown(t, "EventDefinition", "purpose", element.getPurposeElement(), -1);
    if (element.hasUsageElement())
      composeString(t, "EventDefinition", "usage", element.getUsageElement(), -1);
    if (element.hasApprovalDateElement())
      composeDate(t, "EventDefinition", "approvalDate", element.getApprovalDateElement(), -1);
    if (element.hasLastReviewDateElement())
      composeDate(t, "EventDefinition", "lastReviewDate", element.getLastReviewDateElement(), -1);
    if (element.hasEffectivePeriod())
      composePeriod(t, "EventDefinition", "effectivePeriod", element.getEffectivePeriod(), -1);
    for (int i = 0; i < element.getUseContext().size(); i++)
      composeUsageContext(t, "EventDefinition", "useContext", element.getUseContext().get(i), i);
    for (int i = 0; i < element.getJurisdiction().size(); i++)
      composeCodeableConcept(t, "EventDefinition", "jurisdiction", element.getJurisdiction().get(i), i);
    for (int i = 0; i < element.getTopic().size(); i++)
      composeCodeableConcept(t, "EventDefinition", "topic", element.getTopic().get(i), i);
    for (int i = 0; i < element.getContributor().size(); i++)
      composeContributor(t, "EventDefinition", "contributor", element.getContributor().get(i), i);
    for (int i = 0; i < element.getContact().size(); i++)
      composeContactDetail(t, "EventDefinition", "contact", element.getContact().get(i), i);
    if (element.hasCopyrightElement())
      composeMarkdown(t, "EventDefinition", "copyright", element.getCopyrightElement(), -1);
    for (int i = 0; i < element.getRelatedArtifact().size(); i++)
      composeRelatedArtifact(t, "EventDefinition", "relatedArtifact", element.getRelatedArtifact().get(i), i);
    if (element.hasTrigger())
      composeTriggerDefinition(t, "EventDefinition", "trigger", element.getTrigger(), -1);
  }

  protected void composeExampleScenario(Complex parent, String parentType, String name, ExampleScenario element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "ExampleScenario", name, element, index);
    if (element.hasUrlElement())
      composeUri(t, "ExampleScenario", "url", element.getUrlElement(), -1);
    for (int i = 0; i < element.getIdentifier().size(); i++)
      composeIdentifier(t, "ExampleScenario", "identifier", element.getIdentifier().get(i), i);
    if (element.hasVersionElement())
      composeString(t, "ExampleScenario", "version", element.getVersionElement(), -1);
    if (element.hasNameElement())
      composeString(t, "ExampleScenario", "name", element.getNameElement(), -1);
    if (element.hasTitleElement())
      composeString(t, "ExampleScenario", "title", element.getTitleElement(), -1);
    if (element.hasStatusElement())
      composeEnum(t, "ExampleScenario", "status", element.getStatusElement(), -1);
    if (element.hasExperimentalElement())
      composeBoolean(t, "ExampleScenario", "experimental", element.getExperimentalElement(), -1);
    if (element.hasDateElement())
      composeDateTime(t, "ExampleScenario", "date", element.getDateElement(), -1);
    if (element.hasPublisherElement())
      composeString(t, "ExampleScenario", "publisher", element.getPublisherElement(), -1);
    for (int i = 0; i < element.getContact().size(); i++)
      composeContactDetail(t, "ExampleScenario", "contact", element.getContact().get(i), i);
    for (int i = 0; i < element.getUseContext().size(); i++)
      composeUsageContext(t, "ExampleScenario", "useContext", element.getUseContext().get(i), i);
    for (int i = 0; i < element.getJurisdiction().size(); i++)
      composeCodeableConcept(t, "ExampleScenario", "jurisdiction", element.getJurisdiction().get(i), i);
    if (element.hasCopyrightElement())
      composeMarkdown(t, "ExampleScenario", "copyright", element.getCopyrightElement(), -1);
    if (element.hasDescriptionElement())
      composeMarkdown(t, "ExampleScenario", "description", element.getDescriptionElement(), -1);
    if (element.hasPurposeElement())
      composeMarkdown(t, "ExampleScenario", "purpose", element.getPurposeElement(), -1);
    for (int i = 0; i < element.getActor().size(); i++)
      composeExampleScenarioExampleScenarioActorComponent(t, "ExampleScenario", "actor", element.getActor().get(i), i);
    for (int i = 0; i < element.getInstance().size(); i++)
      composeExampleScenarioExampleScenarioInstanceComponent(t, "ExampleScenario", "instance", element.getInstance().get(i), i);
    if (element.hasProcess())
      composeExampleScenarioExampleScenarioProcessComponent(t, "ExampleScenario", "process", element.getProcess(), -1);
    for (int i = 0; i < element.getWorkflow().size(); i++)
      composeReference(t, "ExampleScenario", "workflow", element.getWorkflow().get(i), i);
  }

  protected void composeExampleScenarioExampleScenarioActorComponent(Complex parent, String parentType, String name, ExampleScenario.ExampleScenarioActorComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "actor", name, element, index);
    if (element.hasActorIdElement())
      composeString(t, "ExampleScenario", "actorId", element.getActorIdElement(), -1);
    if (element.hasTypeElement())
      composeEnum(t, "ExampleScenario", "type", element.getTypeElement(), -1);
    if (element.hasNameElement())
      composeString(t, "ExampleScenario", "name", element.getNameElement(), -1);
    if (element.hasDescriptionElement())
      composeMarkdown(t, "ExampleScenario", "description", element.getDescriptionElement(), -1);
  }

  protected void composeExampleScenarioExampleScenarioInstanceComponent(Complex parent, String parentType, String name, ExampleScenario.ExampleScenarioInstanceComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "instance", name, element, index);
    if (element.hasResourceIdElement())
      composeString(t, "ExampleScenario", "resourceId", element.getResourceIdElement(), -1);
    if (element.hasResourceTypeElement())
      composeEnum(t, "ExampleScenario", "resourceType", element.getResourceTypeElement(), -1);
    if (element.hasNameElement())
      composeString(t, "ExampleScenario", "name", element.getNameElement(), -1);
    if (element.hasDescriptionElement())
      composeMarkdown(t, "ExampleScenario", "description", element.getDescriptionElement(), -1);
    for (int i = 0; i < element.getVersion().size(); i++)
      composeExampleScenarioExampleScenarioInstanceVersionComponent(t, "ExampleScenario", "version", element.getVersion().get(i), i);
    for (int i = 0; i < element.getContainedInstance().size(); i++)
      composeExampleScenarioExampleScenarioInstanceContainedInstanceComponent(t, "ExampleScenario", "containedInstance", element.getContainedInstance().get(i), i);
  }

  protected void composeExampleScenarioExampleScenarioInstanceVersionComponent(Complex parent, String parentType, String name, ExampleScenario.ExampleScenarioInstanceVersionComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "version", name, element, index);
    if (element.hasVersionIdElement())
      composeString(t, "ExampleScenario", "versionId", element.getVersionIdElement(), -1);
    if (element.hasDescriptionElement())
      composeMarkdown(t, "ExampleScenario", "description", element.getDescriptionElement(), -1);
  }

  protected void composeExampleScenarioExampleScenarioInstanceContainedInstanceComponent(Complex parent, String parentType, String name, ExampleScenario.ExampleScenarioInstanceContainedInstanceComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "containedInstance", name, element, index);
    if (element.hasResourceIdElement())
      composeString(t, "ExampleScenario", "resourceId", element.getResourceIdElement(), -1);
    if (element.hasVersionIdElement())
      composeString(t, "ExampleScenario", "versionId", element.getVersionIdElement(), -1);
  }

  protected void composeExampleScenarioExampleScenarioProcessComponent(Complex parent, String parentType, String name, ExampleScenario.ExampleScenarioProcessComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "process", name, element, index);
    if (element.hasTitleElement())
      composeString(t, "ExampleScenario", "title", element.getTitleElement(), -1);
    if (element.hasDescriptionElement())
      composeMarkdown(t, "ExampleScenario", "description", element.getDescriptionElement(), -1);
    if (element.hasPreConditionsElement())
      composeMarkdown(t, "ExampleScenario", "preConditions", element.getPreConditionsElement(), -1);
    if (element.hasPostConditionsElement())
      composeMarkdown(t, "ExampleScenario", "postConditions", element.getPostConditionsElement(), -1);
    for (int i = 0; i < element.getStep().size(); i++)
      composeExampleScenarioExampleScenarioProcessStepComponent(t, "ExampleScenario", "step", element.getStep().get(i), i);
  }

  protected void composeExampleScenarioExampleScenarioProcessStepComponent(Complex parent, String parentType, String name, ExampleScenario.ExampleScenarioProcessStepComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "step", name, element, index);
    for (int i = 0; i < element.getProcess().size(); i++)
      composeExampleScenarioExampleScenarioProcessComponent(t, "ExampleScenario", "process", element.getProcess().get(i), i);
    if (element.hasPauseElement())
      composeBoolean(t, "ExampleScenario", "pause", element.getPauseElement(), -1);
    if (element.hasOperation())
      composeExampleScenarioExampleScenarioProcessStepOperationComponent(t, "ExampleScenario", "operation", element.getOperation(), -1);
    if (element.hasAlternative())
      composeExampleScenarioExampleScenarioProcessStepAlternativeComponent(t, "ExampleScenario", "alternative", element.getAlternative(), -1);
  }

  protected void composeExampleScenarioExampleScenarioProcessStepOperationComponent(Complex parent, String parentType, String name, ExampleScenario.ExampleScenarioProcessStepOperationComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "operation", name, element, index);
    if (element.hasNumberElement())
      composeString(t, "ExampleScenario", "number", element.getNumberElement(), -1);
    if (element.hasTypeElement())
      composeString(t, "ExampleScenario", "type", element.getTypeElement(), -1);
    if (element.hasNameElement())
      composeString(t, "ExampleScenario", "name", element.getNameElement(), -1);
    if (element.hasInitiatorElement())
      composeString(t, "ExampleScenario", "initiator", element.getInitiatorElement(), -1);
    if (element.hasReceiverElement())
      composeString(t, "ExampleScenario", "receiver", element.getReceiverElement(), -1);
    if (element.hasDescriptionElement())
      composeMarkdown(t, "ExampleScenario", "description", element.getDescriptionElement(), -1);
    if (element.hasInitiatorActiveElement())
      composeBoolean(t, "ExampleScenario", "initiatorActive", element.getInitiatorActiveElement(), -1);
    if (element.hasReceiverActiveElement())
      composeBoolean(t, "ExampleScenario", "receiverActive", element.getReceiverActiveElement(), -1);
    if (element.hasRequest())
      composeExampleScenarioExampleScenarioInstanceContainedInstanceComponent(t, "ExampleScenario", "request", element.getRequest(), -1);
    if (element.hasResponse())
      composeExampleScenarioExampleScenarioInstanceContainedInstanceComponent(t, "ExampleScenario", "response", element.getResponse(), -1);
  }

  protected void composeExampleScenarioExampleScenarioProcessStepAlternativeComponent(Complex parent, String parentType, String name, ExampleScenario.ExampleScenarioProcessStepAlternativeComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "alternative", name, element, index);
    if (element.hasNameElement())
      composeString(t, "ExampleScenario", "name", element.getNameElement(), -1);
    for (int i = 0; i < element.getOption().size(); i++)
      composeExampleScenarioExampleScenarioProcessStepAlternativeOptionComponent(t, "ExampleScenario", "option", element.getOption().get(i), i);
  }

  protected void composeExampleScenarioExampleScenarioProcessStepAlternativeOptionComponent(Complex parent, String parentType, String name, ExampleScenario.ExampleScenarioProcessStepAlternativeOptionComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "option", name, element, index);
    if (element.hasDescriptionElement())
      composeMarkdown(t, "ExampleScenario", "description", element.getDescriptionElement(), -1);
    for (int i = 0; i < element.getStep().size(); i++)
      composeExampleScenarioExampleScenarioProcessStepComponent(t, "ExampleScenario", "step", element.getStep().get(i), i);
    for (int i = 0; i < element.getPause().size(); i++)
      composeBoolean(t, "ExampleScenario", "pause", element.getPause().get(i), i);
  }

  protected void composeExpansionProfile(Complex parent, String parentType, String name, ExpansionProfile element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "ExpansionProfile", name, element, index);
    if (element.hasUrlElement())
      composeUri(t, "ExpansionProfile", "url", element.getUrlElement(), -1);
    if (element.hasIdentifier())
      composeIdentifier(t, "ExpansionProfile", "identifier", element.getIdentifier(), -1);
    if (element.hasVersionElement())
      composeString(t, "ExpansionProfile", "version", element.getVersionElement(), -1);
    if (element.hasNameElement())
      composeString(t, "ExpansionProfile", "name", element.getNameElement(), -1);
    if (element.hasStatusElement())
      composeEnum(t, "ExpansionProfile", "status", element.getStatusElement(), -1);
    if (element.hasExperimentalElement())
      composeBoolean(t, "ExpansionProfile", "experimental", element.getExperimentalElement(), -1);
    if (element.hasDateElement())
      composeDateTime(t, "ExpansionProfile", "date", element.getDateElement(), -1);
    if (element.hasPublisherElement())
      composeString(t, "ExpansionProfile", "publisher", element.getPublisherElement(), -1);
    for (int i = 0; i < element.getContact().size(); i++)
      composeContactDetail(t, "ExpansionProfile", "contact", element.getContact().get(i), i);
    if (element.hasDescriptionElement())
      composeMarkdown(t, "ExpansionProfile", "description", element.getDescriptionElement(), -1);
    for (int i = 0; i < element.getUseContext().size(); i++)
      composeUsageContext(t, "ExpansionProfile", "useContext", element.getUseContext().get(i), i);
    for (int i = 0; i < element.getJurisdiction().size(); i++)
      composeCodeableConcept(t, "ExpansionProfile", "jurisdiction", element.getJurisdiction().get(i), i);
    for (int i = 0; i < element.getFixedVersion().size(); i++)
      composeExpansionProfileExpansionProfileFixedVersionComponent(t, "ExpansionProfile", "fixedVersion", element.getFixedVersion().get(i), i);
    if (element.hasExcludedSystem())
      composeExpansionProfileExpansionProfileExcludedSystemComponent(t, "ExpansionProfile", "excludedSystem", element.getExcludedSystem(), -1);
    if (element.hasIncludeDesignationsElement())
      composeBoolean(t, "ExpansionProfile", "includeDesignations", element.getIncludeDesignationsElement(), -1);
    if (element.hasDesignation())
      composeExpansionProfileExpansionProfileDesignationComponent(t, "ExpansionProfile", "designation", element.getDesignation(), -1);
    if (element.hasIncludeDefinitionElement())
      composeBoolean(t, "ExpansionProfile", "includeDefinition", element.getIncludeDefinitionElement(), -1);
    if (element.hasActiveOnlyElement())
      composeBoolean(t, "ExpansionProfile", "activeOnly", element.getActiveOnlyElement(), -1);
    if (element.hasExcludeNestedElement())
      composeBoolean(t, "ExpansionProfile", "excludeNested", element.getExcludeNestedElement(), -1);
    if (element.hasExcludeNotForUIElement())
      composeBoolean(t, "ExpansionProfile", "excludeNotForUI", element.getExcludeNotForUIElement(), -1);
    if (element.hasExcludePostCoordinatedElement())
      composeBoolean(t, "ExpansionProfile", "excludePostCoordinated", element.getExcludePostCoordinatedElement(), -1);
    if (element.hasDisplayLanguageElement())
      composeCode(t, "ExpansionProfile", "displayLanguage", element.getDisplayLanguageElement(), -1);
    if (element.hasLimitedExpansionElement())
      composeBoolean(t, "ExpansionProfile", "limitedExpansion", element.getLimitedExpansionElement(), -1);
  }

  protected void composeExpansionProfileExpansionProfileFixedVersionComponent(Complex parent, String parentType, String name, ExpansionProfile.ExpansionProfileFixedVersionComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "fixedVersion", name, element, index);
    if (element.hasSystemElement())
      composeUri(t, "ExpansionProfile", "system", element.getSystemElement(), -1);
    if (element.hasVersionElement())
      composeString(t, "ExpansionProfile", "version", element.getVersionElement(), -1);
    if (element.hasModeElement())
      composeEnum(t, "ExpansionProfile", "mode", element.getModeElement(), -1);
  }

  protected void composeExpansionProfileExpansionProfileExcludedSystemComponent(Complex parent, String parentType, String name, ExpansionProfile.ExpansionProfileExcludedSystemComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "excludedSystem", name, element, index);
    if (element.hasSystemElement())
      composeUri(t, "ExpansionProfile", "system", element.getSystemElement(), -1);
    if (element.hasVersionElement())
      composeString(t, "ExpansionProfile", "version", element.getVersionElement(), -1);
  }

  protected void composeExpansionProfileExpansionProfileDesignationComponent(Complex parent, String parentType, String name, ExpansionProfile.ExpansionProfileDesignationComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "designation", name, element, index);
    if (element.hasInclude())
      composeExpansionProfileDesignationIncludeComponent(t, "ExpansionProfile", "include", element.getInclude(), -1);
    if (element.hasExclude())
      composeExpansionProfileDesignationExcludeComponent(t, "ExpansionProfile", "exclude", element.getExclude(), -1);
  }

  protected void composeExpansionProfileDesignationIncludeComponent(Complex parent, String parentType, String name, ExpansionProfile.DesignationIncludeComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "include", name, element, index);
    for (int i = 0; i < element.getDesignation().size(); i++)
      composeExpansionProfileDesignationIncludeDesignationComponent(t, "ExpansionProfile", "designation", element.getDesignation().get(i), i);
  }

  protected void composeExpansionProfileDesignationIncludeDesignationComponent(Complex parent, String parentType, String name, ExpansionProfile.DesignationIncludeDesignationComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "designation", name, element, index);
    if (element.hasLanguageElement())
      composeCode(t, "ExpansionProfile", "language", element.getLanguageElement(), -1);
    if (element.hasUse())
      composeCoding(t, "ExpansionProfile", "use", element.getUse(), -1);
  }

  protected void composeExpansionProfileDesignationExcludeComponent(Complex parent, String parentType, String name, ExpansionProfile.DesignationExcludeComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "exclude", name, element, index);
    for (int i = 0; i < element.getDesignation().size(); i++)
      composeExpansionProfileDesignationExcludeDesignationComponent(t, "ExpansionProfile", "designation", element.getDesignation().get(i), i);
  }

  protected void composeExpansionProfileDesignationExcludeDesignationComponent(Complex parent, String parentType, String name, ExpansionProfile.DesignationExcludeDesignationComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "designation", name, element, index);
    if (element.hasLanguageElement())
      composeCode(t, "ExpansionProfile", "language", element.getLanguageElement(), -1);
    if (element.hasUse())
      composeCoding(t, "ExpansionProfile", "use", element.getUse(), -1);
  }

  protected void composeExplanationOfBenefit(Complex parent, String parentType, String name, ExplanationOfBenefit element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "ExplanationOfBenefit", name, element, index);
    for (int i = 0; i < element.getIdentifier().size(); i++)
      composeIdentifier(t, "ExplanationOfBenefit", "identifier", element.getIdentifier().get(i), i);
    if (element.hasStatusElement())
      composeEnum(t, "ExplanationOfBenefit", "status", element.getStatusElement(), -1);
    if (element.hasType())
      composeCodeableConcept(t, "ExplanationOfBenefit", "type", element.getType(), -1);
    for (int i = 0; i < element.getSubType().size(); i++)
      composeCodeableConcept(t, "ExplanationOfBenefit", "subType", element.getSubType().get(i), i);
    if (element.hasPatient())
      composeReference(t, "ExplanationOfBenefit", "patient", element.getPatient(), -1);
    if (element.hasBillablePeriod())
      composePeriod(t, "ExplanationOfBenefit", "billablePeriod", element.getBillablePeriod(), -1);
    if (element.hasCreatedElement())
      composeDateTime(t, "ExplanationOfBenefit", "created", element.getCreatedElement(), -1);
    if (element.hasEnterer())
      composeReference(t, "ExplanationOfBenefit", "enterer", element.getEnterer(), -1);
    if (element.hasInsurer())
      composeReference(t, "ExplanationOfBenefit", "insurer", element.getInsurer(), -1);
    if (element.hasProvider())
      composeReference(t, "ExplanationOfBenefit", "provider", element.getProvider(), -1);
    if (element.hasOrganization())
      composeReference(t, "ExplanationOfBenefit", "organization", element.getOrganization(), -1);
    if (element.hasReferral())
      composeReference(t, "ExplanationOfBenefit", "referral", element.getReferral(), -1);
    if (element.hasFacility())
      composeReference(t, "ExplanationOfBenefit", "facility", element.getFacility(), -1);
    if (element.hasClaim())
      composeReference(t, "ExplanationOfBenefit", "claim", element.getClaim(), -1);
    if (element.hasClaimResponse())
      composeReference(t, "ExplanationOfBenefit", "claimResponse", element.getClaimResponse(), -1);
    if (element.hasOutcomeElement())
      composeEnum(t, "ExplanationOfBenefit", "outcome", element.getOutcomeElement(), -1);
    if (element.hasDispositionElement())
      composeString(t, "ExplanationOfBenefit", "disposition", element.getDispositionElement(), -1);
    for (int i = 0; i < element.getRelated().size(); i++)
      composeExplanationOfBenefitRelatedClaimComponent(t, "ExplanationOfBenefit", "related", element.getRelated().get(i), i);
    if (element.hasPrescription())
      composeReference(t, "ExplanationOfBenefit", "prescription", element.getPrescription(), -1);
    if (element.hasOriginalPrescription())
      composeReference(t, "ExplanationOfBenefit", "originalPrescription", element.getOriginalPrescription(), -1);
    if (element.hasPayee())
      composeExplanationOfBenefitPayeeComponent(t, "ExplanationOfBenefit", "payee", element.getPayee(), -1);
    for (int i = 0; i < element.getInformation().size(); i++)
      composeExplanationOfBenefitSupportingInformationComponent(t, "ExplanationOfBenefit", "information", element.getInformation().get(i), i);
    for (int i = 0; i < element.getCareTeam().size(); i++)
      composeExplanationOfBenefitCareTeamComponent(t, "ExplanationOfBenefit", "careTeam", element.getCareTeam().get(i), i);
    for (int i = 0; i < element.getDiagnosis().size(); i++)
      composeExplanationOfBenefitDiagnosisComponent(t, "ExplanationOfBenefit", "diagnosis", element.getDiagnosis().get(i), i);
    for (int i = 0; i < element.getProcedure().size(); i++)
      composeExplanationOfBenefitProcedureComponent(t, "ExplanationOfBenefit", "procedure", element.getProcedure().get(i), i);
    if (element.hasPrecedenceElement())
      composePositiveInt(t, "ExplanationOfBenefit", "precedence", element.getPrecedenceElement(), -1);
    if (element.hasInsurance())
      composeExplanationOfBenefitInsuranceComponent(t, "ExplanationOfBenefit", "insurance", element.getInsurance(), -1);
    if (element.hasAccident())
      composeExplanationOfBenefitAccidentComponent(t, "ExplanationOfBenefit", "accident", element.getAccident(), -1);
    if (element.hasEmploymentImpacted())
      composePeriod(t, "ExplanationOfBenefit", "employmentImpacted", element.getEmploymentImpacted(), -1);
    if (element.hasHospitalization())
      composePeriod(t, "ExplanationOfBenefit", "hospitalization", element.getHospitalization(), -1);
    for (int i = 0; i < element.getItem().size(); i++)
      composeExplanationOfBenefitItemComponent(t, "ExplanationOfBenefit", "item", element.getItem().get(i), i);
    for (int i = 0; i < element.getAddItem().size(); i++)
      composeExplanationOfBenefitAddedItemComponent(t, "ExplanationOfBenefit", "addItem", element.getAddItem().get(i), i);
    if (element.hasTotalCost())
      composeMoney(t, "ExplanationOfBenefit", "totalCost", element.getTotalCost(), -1);
    if (element.hasUnallocDeductable())
      composeMoney(t, "ExplanationOfBenefit", "unallocDeductable", element.getUnallocDeductable(), -1);
    if (element.hasTotalBenefit())
      composeMoney(t, "ExplanationOfBenefit", "totalBenefit", element.getTotalBenefit(), -1);
    if (element.hasPayment())
      composeExplanationOfBenefitPaymentComponent(t, "ExplanationOfBenefit", "payment", element.getPayment(), -1);
    if (element.hasForm())
      composeCodeableConcept(t, "ExplanationOfBenefit", "form", element.getForm(), -1);
    for (int i = 0; i < element.getProcessNote().size(); i++)
      composeExplanationOfBenefitNoteComponent(t, "ExplanationOfBenefit", "processNote", element.getProcessNote().get(i), i);
    for (int i = 0; i < element.getBenefitBalance().size(); i++)
      composeExplanationOfBenefitBenefitBalanceComponent(t, "ExplanationOfBenefit", "benefitBalance", element.getBenefitBalance().get(i), i);
  }

  protected void composeExplanationOfBenefitRelatedClaimComponent(Complex parent, String parentType, String name, ExplanationOfBenefit.RelatedClaimComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "related", name, element, index);
    if (element.hasClaim())
      composeReference(t, "ExplanationOfBenefit", "claim", element.getClaim(), -1);
    if (element.hasRelationship())
      composeCodeableConcept(t, "ExplanationOfBenefit", "relationship", element.getRelationship(), -1);
    if (element.hasReference())
      composeIdentifier(t, "ExplanationOfBenefit", "reference", element.getReference(), -1);
  }

  protected void composeExplanationOfBenefitPayeeComponent(Complex parent, String parentType, String name, ExplanationOfBenefit.PayeeComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "payee", name, element, index);
    if (element.hasType())
      composeCodeableConcept(t, "ExplanationOfBenefit", "type", element.getType(), -1);
    if (element.hasResource())
      composeCoding(t, "ExplanationOfBenefit", "resource", element.getResource(), -1);
    if (element.hasParty())
      composeReference(t, "ExplanationOfBenefit", "party", element.getParty(), -1);
  }

  protected void composeExplanationOfBenefitSupportingInformationComponent(Complex parent, String parentType, String name, ExplanationOfBenefit.SupportingInformationComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "information", name, element, index);
    if (element.hasSequenceElement())
      composePositiveInt(t, "ExplanationOfBenefit", "sequence", element.getSequenceElement(), -1);
    if (element.hasCategory())
      composeCodeableConcept(t, "ExplanationOfBenefit", "category", element.getCategory(), -1);
    if (element.hasCode())
      composeCodeableConcept(t, "ExplanationOfBenefit", "code", element.getCode(), -1);
    if (element.hasTiming())
      composeType(t, "ExplanationOfBenefit", "timing", element.getTiming(), -1);
    if (element.hasValue())
      composeType(t, "ExplanationOfBenefit", "value", element.getValue(), -1);
    if (element.hasReason())
      composeCoding(t, "ExplanationOfBenefit", "reason", element.getReason(), -1);
  }

  protected void composeExplanationOfBenefitCareTeamComponent(Complex parent, String parentType, String name, ExplanationOfBenefit.CareTeamComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "careTeam", name, element, index);
    if (element.hasSequenceElement())
      composePositiveInt(t, "ExplanationOfBenefit", "sequence", element.getSequenceElement(), -1);
    if (element.hasProvider())
      composeReference(t, "ExplanationOfBenefit", "provider", element.getProvider(), -1);
    if (element.hasResponsibleElement())
      composeBoolean(t, "ExplanationOfBenefit", "responsible", element.getResponsibleElement(), -1);
    if (element.hasRole())
      composeCodeableConcept(t, "ExplanationOfBenefit", "role", element.getRole(), -1);
    if (element.hasQualification())
      composeCodeableConcept(t, "ExplanationOfBenefit", "qualification", element.getQualification(), -1);
  }

  protected void composeExplanationOfBenefitDiagnosisComponent(Complex parent, String parentType, String name, ExplanationOfBenefit.DiagnosisComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "diagnosis", name, element, index);
    if (element.hasSequenceElement())
      composePositiveInt(t, "ExplanationOfBenefit", "sequence", element.getSequenceElement(), -1);
    if (element.hasDiagnosis())
      composeType(t, "ExplanationOfBenefit", "diagnosis", element.getDiagnosis(), -1);
    for (int i = 0; i < element.getType().size(); i++)
      composeCodeableConcept(t, "ExplanationOfBenefit", "type", element.getType().get(i), i);
    if (element.hasPackageCode())
      composeCodeableConcept(t, "ExplanationOfBenefit", "packageCode", element.getPackageCode(), -1);
  }

  protected void composeExplanationOfBenefitProcedureComponent(Complex parent, String parentType, String name, ExplanationOfBenefit.ProcedureComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "procedure", name, element, index);
    if (element.hasSequenceElement())
      composePositiveInt(t, "ExplanationOfBenefit", "sequence", element.getSequenceElement(), -1);
    if (element.hasDateElement())
      composeDateTime(t, "ExplanationOfBenefit", "date", element.getDateElement(), -1);
    if (element.hasProcedure())
      composeType(t, "ExplanationOfBenefit", "procedure", element.getProcedure(), -1);
  }

  protected void composeExplanationOfBenefitInsuranceComponent(Complex parent, String parentType, String name, ExplanationOfBenefit.InsuranceComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "insurance", name, element, index);
    if (element.hasCoverage())
      composeReference(t, "ExplanationOfBenefit", "coverage", element.getCoverage(), -1);
    for (int i = 0; i < element.getPreAuthRef().size(); i++)
      composeString(t, "ExplanationOfBenefit", "preAuthRef", element.getPreAuthRef().get(i), i);
  }

  protected void composeExplanationOfBenefitAccidentComponent(Complex parent, String parentType, String name, ExplanationOfBenefit.AccidentComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "accident", name, element, index);
    if (element.hasDateElement())
      composeDate(t, "ExplanationOfBenefit", "date", element.getDateElement(), -1);
    if (element.hasType())
      composeCodeableConcept(t, "ExplanationOfBenefit", "type", element.getType(), -1);
    if (element.hasLocation())
      composeType(t, "ExplanationOfBenefit", "location", element.getLocation(), -1);
  }

  protected void composeExplanationOfBenefitItemComponent(Complex parent, String parentType, String name, ExplanationOfBenefit.ItemComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "item", name, element, index);
    if (element.hasSequenceElement())
      composePositiveInt(t, "ExplanationOfBenefit", "sequence", element.getSequenceElement(), -1);
    for (int i = 0; i < element.getCareTeamLinkId().size(); i++)
      composePositiveInt(t, "ExplanationOfBenefit", "careTeamLinkId", element.getCareTeamLinkId().get(i), i);
    for (int i = 0; i < element.getDiagnosisLinkId().size(); i++)
      composePositiveInt(t, "ExplanationOfBenefit", "diagnosisLinkId", element.getDiagnosisLinkId().get(i), i);
    for (int i = 0; i < element.getProcedureLinkId().size(); i++)
      composePositiveInt(t, "ExplanationOfBenefit", "procedureLinkId", element.getProcedureLinkId().get(i), i);
    for (int i = 0; i < element.getInformationLinkId().size(); i++)
      composePositiveInt(t, "ExplanationOfBenefit", "informationLinkId", element.getInformationLinkId().get(i), i);
    if (element.hasRevenue())
      composeCodeableConcept(t, "ExplanationOfBenefit", "revenue", element.getRevenue(), -1);
    if (element.hasCategory())
      composeCodeableConcept(t, "ExplanationOfBenefit", "category", element.getCategory(), -1);
    if (element.hasService())
      composeCodeableConcept(t, "ExplanationOfBenefit", "service", element.getService(), -1);
    for (int i = 0; i < element.getModifier().size(); i++)
      composeCodeableConcept(t, "ExplanationOfBenefit", "modifier", element.getModifier().get(i), i);
    for (int i = 0; i < element.getProgramCode().size(); i++)
      composeCodeableConcept(t, "ExplanationOfBenefit", "programCode", element.getProgramCode().get(i), i);
    if (element.hasServiced())
      composeType(t, "ExplanationOfBenefit", "serviced", element.getServiced(), -1);
    if (element.hasLocation())
      composeType(t, "ExplanationOfBenefit", "location", element.getLocation(), -1);
    if (element.hasQuantity())
      composeQuantity(t, "ExplanationOfBenefit", "quantity", element.getQuantity(), -1);
    if (element.hasUnitPrice())
      composeMoney(t, "ExplanationOfBenefit", "unitPrice", element.getUnitPrice(), -1);
    if (element.hasFactorElement())
      composeDecimal(t, "ExplanationOfBenefit", "factor", element.getFactorElement(), -1);
    if (element.hasNet())
      composeMoney(t, "ExplanationOfBenefit", "net", element.getNet(), -1);
    for (int i = 0; i < element.getUdi().size(); i++)
      composeReference(t, "ExplanationOfBenefit", "udi", element.getUdi().get(i), i);
    if (element.hasBodySite())
      composeCodeableConcept(t, "ExplanationOfBenefit", "bodySite", element.getBodySite(), -1);
    for (int i = 0; i < element.getSubSite().size(); i++)
      composeCodeableConcept(t, "ExplanationOfBenefit", "subSite", element.getSubSite().get(i), i);
    for (int i = 0; i < element.getEncounter().size(); i++)
      composeReference(t, "ExplanationOfBenefit", "encounter", element.getEncounter().get(i), i);
    for (int i = 0; i < element.getNoteNumber().size(); i++)
      composePositiveInt(t, "ExplanationOfBenefit", "noteNumber", element.getNoteNumber().get(i), i);
    for (int i = 0; i < element.getAdjudication().size(); i++)
      composeExplanationOfBenefitAdjudicationComponent(t, "ExplanationOfBenefit", "adjudication", element.getAdjudication().get(i), i);
    for (int i = 0; i < element.getDetail().size(); i++)
      composeExplanationOfBenefitDetailComponent(t, "ExplanationOfBenefit", "detail", element.getDetail().get(i), i);
  }

  protected void composeExplanationOfBenefitAdjudicationComponent(Complex parent, String parentType, String name, ExplanationOfBenefit.AdjudicationComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "adjudication", name, element, index);
    if (element.hasCategory())
      composeCodeableConcept(t, "ExplanationOfBenefit", "category", element.getCategory(), -1);
    if (element.hasReason())
      composeCodeableConcept(t, "ExplanationOfBenefit", "reason", element.getReason(), -1);
    if (element.hasAmount())
      composeMoney(t, "ExplanationOfBenefit", "amount", element.getAmount(), -1);
    if (element.hasValueElement())
      composeDecimal(t, "ExplanationOfBenefit", "value", element.getValueElement(), -1);
  }

  protected void composeExplanationOfBenefitDetailComponent(Complex parent, String parentType, String name, ExplanationOfBenefit.DetailComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "detail", name, element, index);
    if (element.hasSequenceElement())
      composePositiveInt(t, "ExplanationOfBenefit", "sequence", element.getSequenceElement(), -1);
    if (element.hasRevenue())
      composeCodeableConcept(t, "ExplanationOfBenefit", "revenue", element.getRevenue(), -1);
    if (element.hasCategory())
      composeCodeableConcept(t, "ExplanationOfBenefit", "category", element.getCategory(), -1);
    if (element.hasService())
      composeCodeableConcept(t, "ExplanationOfBenefit", "service", element.getService(), -1);
    for (int i = 0; i < element.getModifier().size(); i++)
      composeCodeableConcept(t, "ExplanationOfBenefit", "modifier", element.getModifier().get(i), i);
    for (int i = 0; i < element.getProgramCode().size(); i++)
      composeCodeableConcept(t, "ExplanationOfBenefit", "programCode", element.getProgramCode().get(i), i);
    if (element.hasQuantity())
      composeQuantity(t, "ExplanationOfBenefit", "quantity", element.getQuantity(), -1);
    if (element.hasUnitPrice())
      composeMoney(t, "ExplanationOfBenefit", "unitPrice", element.getUnitPrice(), -1);
    if (element.hasFactorElement())
      composeDecimal(t, "ExplanationOfBenefit", "factor", element.getFactorElement(), -1);
    if (element.hasNet())
      composeMoney(t, "ExplanationOfBenefit", "net", element.getNet(), -1);
    for (int i = 0; i < element.getUdi().size(); i++)
      composeReference(t, "ExplanationOfBenefit", "udi", element.getUdi().get(i), i);
    for (int i = 0; i < element.getNoteNumber().size(); i++)
      composePositiveInt(t, "ExplanationOfBenefit", "noteNumber", element.getNoteNumber().get(i), i);
    for (int i = 0; i < element.getAdjudication().size(); i++)
      composeExplanationOfBenefitAdjudicationComponent(t, "ExplanationOfBenefit", "adjudication", element.getAdjudication().get(i), i);
    for (int i = 0; i < element.getSubDetail().size(); i++)
      composeExplanationOfBenefitSubDetailComponent(t, "ExplanationOfBenefit", "subDetail", element.getSubDetail().get(i), i);
  }

  protected void composeExplanationOfBenefitSubDetailComponent(Complex parent, String parentType, String name, ExplanationOfBenefit.SubDetailComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "subDetail", name, element, index);
    if (element.hasSequenceElement())
      composePositiveInt(t, "ExplanationOfBenefit", "sequence", element.getSequenceElement(), -1);
    if (element.hasRevenue())
      composeCodeableConcept(t, "ExplanationOfBenefit", "revenue", element.getRevenue(), -1);
    if (element.hasCategory())
      composeCodeableConcept(t, "ExplanationOfBenefit", "category", element.getCategory(), -1);
    if (element.hasService())
      composeCodeableConcept(t, "ExplanationOfBenefit", "service", element.getService(), -1);
    for (int i = 0; i < element.getModifier().size(); i++)
      composeCodeableConcept(t, "ExplanationOfBenefit", "modifier", element.getModifier().get(i), i);
    for (int i = 0; i < element.getProgramCode().size(); i++)
      composeCodeableConcept(t, "ExplanationOfBenefit", "programCode", element.getProgramCode().get(i), i);
    if (element.hasQuantity())
      composeQuantity(t, "ExplanationOfBenefit", "quantity", element.getQuantity(), -1);
    if (element.hasUnitPrice())
      composeMoney(t, "ExplanationOfBenefit", "unitPrice", element.getUnitPrice(), -1);
    if (element.hasFactorElement())
      composeDecimal(t, "ExplanationOfBenefit", "factor", element.getFactorElement(), -1);
    if (element.hasNet())
      composeMoney(t, "ExplanationOfBenefit", "net", element.getNet(), -1);
    for (int i = 0; i < element.getUdi().size(); i++)
      composeReference(t, "ExplanationOfBenefit", "udi", element.getUdi().get(i), i);
    for (int i = 0; i < element.getNoteNumber().size(); i++)
      composePositiveInt(t, "ExplanationOfBenefit", "noteNumber", element.getNoteNumber().get(i), i);
    for (int i = 0; i < element.getAdjudication().size(); i++)
      composeExplanationOfBenefitAdjudicationComponent(t, "ExplanationOfBenefit", "adjudication", element.getAdjudication().get(i), i);
  }

  protected void composeExplanationOfBenefitAddedItemComponent(Complex parent, String parentType, String name, ExplanationOfBenefit.AddedItemComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "addItem", name, element, index);
    for (int i = 0; i < element.getSequenceLinkId().size(); i++)
      composePositiveInt(t, "ExplanationOfBenefit", "sequenceLinkId", element.getSequenceLinkId().get(i), i);
    for (int i = 0; i < element.getDetailSequenceLinkId().size(); i++)
      composePositiveInt(t, "ExplanationOfBenefit", "detailSequenceLinkId", element.getDetailSequenceLinkId().get(i), i);
    for (int i = 0; i < element.getSubdetailSequenceLinkId().size(); i++)
      composePositiveInt(t, "ExplanationOfBenefit", "subdetailSequenceLinkId", element.getSubdetailSequenceLinkId().get(i), i);
    if (element.hasService())
      composeCodeableConcept(t, "ExplanationOfBenefit", "service", element.getService(), -1);
    for (int i = 0; i < element.getModifier().size(); i++)
      composeCodeableConcept(t, "ExplanationOfBenefit", "modifier", element.getModifier().get(i), i);
    if (element.hasFee())
      composeMoney(t, "ExplanationOfBenefit", "fee", element.getFee(), -1);
    for (int i = 0; i < element.getNoteNumber().size(); i++)
      composePositiveInt(t, "ExplanationOfBenefit", "noteNumber", element.getNoteNumber().get(i), i);
    for (int i = 0; i < element.getAdjudication().size(); i++)
      composeExplanationOfBenefitAdjudicationComponent(t, "ExplanationOfBenefit", "adjudication", element.getAdjudication().get(i), i);
  }

  protected void composeExplanationOfBenefitPaymentComponent(Complex parent, String parentType, String name, ExplanationOfBenefit.PaymentComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "payment", name, element, index);
    if (element.hasType())
      composeCodeableConcept(t, "ExplanationOfBenefit", "type", element.getType(), -1);
    if (element.hasAdjustment())
      composeMoney(t, "ExplanationOfBenefit", "adjustment", element.getAdjustment(), -1);
    if (element.hasAdjustmentReason())
      composeCodeableConcept(t, "ExplanationOfBenefit", "adjustmentReason", element.getAdjustmentReason(), -1);
    if (element.hasDateElement())
      composeDate(t, "ExplanationOfBenefit", "date", element.getDateElement(), -1);
    if (element.hasAmount())
      composeMoney(t, "ExplanationOfBenefit", "amount", element.getAmount(), -1);
    if (element.hasIdentifier())
      composeIdentifier(t, "ExplanationOfBenefit", "identifier", element.getIdentifier(), -1);
  }

  protected void composeExplanationOfBenefitNoteComponent(Complex parent, String parentType, String name, ExplanationOfBenefit.NoteComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "processNote", name, element, index);
    if (element.hasNumberElement())
      composePositiveInt(t, "ExplanationOfBenefit", "number", element.getNumberElement(), -1);
    if (element.hasTypeElement())
      composeEnum(t, "ExplanationOfBenefit", "type", element.getTypeElement(), -1);
    if (element.hasTextElement())
      composeString(t, "ExplanationOfBenefit", "text", element.getTextElement(), -1);
    if (element.hasLanguage())
      composeCodeableConcept(t, "ExplanationOfBenefit", "language", element.getLanguage(), -1);
  }

  protected void composeExplanationOfBenefitBenefitBalanceComponent(Complex parent, String parentType, String name, ExplanationOfBenefit.BenefitBalanceComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "benefitBalance", name, element, index);
    if (element.hasCategory())
      composeCodeableConcept(t, "ExplanationOfBenefit", "category", element.getCategory(), -1);
    if (element.hasSubCategory())
      composeCodeableConcept(t, "ExplanationOfBenefit", "subCategory", element.getSubCategory(), -1);
    if (element.hasExcludedElement())
      composeBoolean(t, "ExplanationOfBenefit", "excluded", element.getExcludedElement(), -1);
    if (element.hasNameElement())
      composeString(t, "ExplanationOfBenefit", "name", element.getNameElement(), -1);
    if (element.hasDescriptionElement())
      composeString(t, "ExplanationOfBenefit", "description", element.getDescriptionElement(), -1);
    if (element.hasNetwork())
      composeCodeableConcept(t, "ExplanationOfBenefit", "network", element.getNetwork(), -1);
    if (element.hasUnit())
      composeCodeableConcept(t, "ExplanationOfBenefit", "unit", element.getUnit(), -1);
    if (element.hasTerm())
      composeCodeableConcept(t, "ExplanationOfBenefit", "term", element.getTerm(), -1);
    for (int i = 0; i < element.getFinancial().size(); i++)
      composeExplanationOfBenefitBenefitComponent(t, "ExplanationOfBenefit", "financial", element.getFinancial().get(i), i);
  }

  protected void composeExplanationOfBenefitBenefitComponent(Complex parent, String parentType, String name, ExplanationOfBenefit.BenefitComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "financial", name, element, index);
    if (element.hasType())
      composeCodeableConcept(t, "ExplanationOfBenefit", "type", element.getType(), -1);
    if (element.hasAllowed())
      composeType(t, "ExplanationOfBenefit", "allowed", element.getAllowed(), -1);
    if (element.hasUsed())
      composeType(t, "ExplanationOfBenefit", "used", element.getUsed(), -1);
  }

  protected void composeFamilyMemberHistory(Complex parent, String parentType, String name, FamilyMemberHistory element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "FamilyMemberHistory", name, element, index);
    for (int i = 0; i < element.getIdentifier().size(); i++)
      composeIdentifier(t, "FamilyMemberHistory", "identifier", element.getIdentifier().get(i), i);
    for (int i = 0; i < element.getDefinition().size(); i++)
      composeReference(t, "FamilyMemberHistory", "definition", element.getDefinition().get(i), i);
    if (element.hasStatusElement())
      composeEnum(t, "FamilyMemberHistory", "status", element.getStatusElement(), -1);
    if (element.hasDataAbsentReason())
      composeCodeableConcept(t, "FamilyMemberHistory", "dataAbsentReason", element.getDataAbsentReason(), -1);
    if (element.hasPatient())
      composeReference(t, "FamilyMemberHistory", "patient", element.getPatient(), -1);
    if (element.hasDateElement())
      composeDateTime(t, "FamilyMemberHistory", "date", element.getDateElement(), -1);
    if (element.hasNameElement())
      composeString(t, "FamilyMemberHistory", "name", element.getNameElement(), -1);
    if (element.hasRelationship())
      composeCodeableConcept(t, "FamilyMemberHistory", "relationship", element.getRelationship(), -1);
    if (element.hasGenderElement())
      composeEnum(t, "FamilyMemberHistory", "gender", element.getGenderElement(), -1);
    if (element.hasBorn())
      composeType(t, "FamilyMemberHistory", "born", element.getBorn(), -1);
    if (element.hasAge())
      composeType(t, "FamilyMemberHistory", "age", element.getAge(), -1);
    if (element.hasEstimatedAgeElement())
      composeBoolean(t, "FamilyMemberHistory", "estimatedAge", element.getEstimatedAgeElement(), -1);
    if (element.hasDeceased())
      composeType(t, "FamilyMemberHistory", "deceased", element.getDeceased(), -1);
    for (int i = 0; i < element.getReasonCode().size(); i++)
      composeCodeableConcept(t, "FamilyMemberHistory", "reasonCode", element.getReasonCode().get(i), i);
    for (int i = 0; i < element.getReasonReference().size(); i++)
      composeReference(t, "FamilyMemberHistory", "reasonReference", element.getReasonReference().get(i), i);
    for (int i = 0; i < element.getNote().size(); i++)
      composeAnnotation(t, "FamilyMemberHistory", "note", element.getNote().get(i), i);
    for (int i = 0; i < element.getCondition().size(); i++)
      composeFamilyMemberHistoryFamilyMemberHistoryConditionComponent(t, "FamilyMemberHistory", "condition", element.getCondition().get(i), i);
  }

  protected void composeFamilyMemberHistoryFamilyMemberHistoryConditionComponent(Complex parent, String parentType, String name, FamilyMemberHistory.FamilyMemberHistoryConditionComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "condition", name, element, index);
    if (element.hasCode())
      composeCodeableConcept(t, "FamilyMemberHistory", "code", element.getCode(), -1);
    if (element.hasOutcome())
      composeCodeableConcept(t, "FamilyMemberHistory", "outcome", element.getOutcome(), -1);
    if (element.hasOnset())
      composeType(t, "FamilyMemberHistory", "onset", element.getOnset(), -1);
    for (int i = 0; i < element.getNote().size(); i++)
      composeAnnotation(t, "FamilyMemberHistory", "note", element.getNote().get(i), i);
  }

  protected void composeFlag(Complex parent, String parentType, String name, Flag element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "Flag", name, element, index);
    for (int i = 0; i < element.getIdentifier().size(); i++)
      composeIdentifier(t, "Flag", "identifier", element.getIdentifier().get(i), i);
    if (element.hasStatusElement())
      composeEnum(t, "Flag", "status", element.getStatusElement(), -1);
    for (int i = 0; i < element.getCategory().size(); i++)
      composeCodeableConcept(t, "Flag", "category", element.getCategory().get(i), i);
    if (element.hasCode())
      composeCodeableConcept(t, "Flag", "code", element.getCode(), -1);
    if (element.hasSubject())
      composeReference(t, "Flag", "subject", element.getSubject(), -1);
    if (element.hasPeriod())
      composePeriod(t, "Flag", "period", element.getPeriod(), -1);
    if (element.hasEncounter())
      composeReference(t, "Flag", "encounter", element.getEncounter(), -1);
    if (element.hasAuthor())
      composeReference(t, "Flag", "author", element.getAuthor(), -1);
  }

  protected void composeGoal(Complex parent, String parentType, String name, Goal element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "Goal", name, element, index);
    for (int i = 0; i < element.getIdentifier().size(); i++)
      composeIdentifier(t, "Goal", "identifier", element.getIdentifier().get(i), i);
    if (element.hasStatusElement())
      composeEnum(t, "Goal", "status", element.getStatusElement(), -1);
    for (int i = 0; i < element.getCategory().size(); i++)
      composeCodeableConcept(t, "Goal", "category", element.getCategory().get(i), i);
    if (element.hasPriority())
      composeCodeableConcept(t, "Goal", "priority", element.getPriority(), -1);
    if (element.hasDescription())
      composeCodeableConcept(t, "Goal", "description", element.getDescription(), -1);
    if (element.hasSubject())
      composeReference(t, "Goal", "subject", element.getSubject(), -1);
    if (element.hasStart())
      composeType(t, "Goal", "start", element.getStart(), -1);
    if (element.hasTarget())
      composeGoalGoalTargetComponent(t, "Goal", "target", element.getTarget(), -1);
    if (element.hasStatusDateElement())
      composeDate(t, "Goal", "statusDate", element.getStatusDateElement(), -1);
    if (element.hasStatusReasonElement())
      composeString(t, "Goal", "statusReason", element.getStatusReasonElement(), -1);
    if (element.hasExpressedBy())
      composeReference(t, "Goal", "expressedBy", element.getExpressedBy(), -1);
    for (int i = 0; i < element.getAddresses().size(); i++)
      composeReference(t, "Goal", "addresses", element.getAddresses().get(i), i);
    for (int i = 0; i < element.getNote().size(); i++)
      composeAnnotation(t, "Goal", "note", element.getNote().get(i), i);
    for (int i = 0; i < element.getOutcomeCode().size(); i++)
      composeCodeableConcept(t, "Goal", "outcomeCode", element.getOutcomeCode().get(i), i);
    for (int i = 0; i < element.getOutcomeReference().size(); i++)
      composeReference(t, "Goal", "outcomeReference", element.getOutcomeReference().get(i), i);
  }

  protected void composeGoalGoalTargetComponent(Complex parent, String parentType, String name, Goal.GoalTargetComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "target", name, element, index);
    if (element.hasMeasure())
      composeCodeableConcept(t, "Goal", "measure", element.getMeasure(), -1);
    if (element.hasDetail())
      composeType(t, "Goal", "detail", element.getDetail(), -1);
    if (element.hasDue())
      composeType(t, "Goal", "due", element.getDue(), -1);
  }

  protected void composeGraphDefinition(Complex parent, String parentType, String name, GraphDefinition element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "GraphDefinition", name, element, index);
    if (element.hasUrlElement())
      composeUri(t, "GraphDefinition", "url", element.getUrlElement(), -1);
    if (element.hasVersionElement())
      composeString(t, "GraphDefinition", "version", element.getVersionElement(), -1);
    if (element.hasNameElement())
      composeString(t, "GraphDefinition", "name", element.getNameElement(), -1);
    if (element.hasStatusElement())
      composeEnum(t, "GraphDefinition", "status", element.getStatusElement(), -1);
    if (element.hasExperimentalElement())
      composeBoolean(t, "GraphDefinition", "experimental", element.getExperimentalElement(), -1);
    if (element.hasDateElement())
      composeDateTime(t, "GraphDefinition", "date", element.getDateElement(), -1);
    if (element.hasPublisherElement())
      composeString(t, "GraphDefinition", "publisher", element.getPublisherElement(), -1);
    for (int i = 0; i < element.getContact().size(); i++)
      composeContactDetail(t, "GraphDefinition", "contact", element.getContact().get(i), i);
    if (element.hasDescriptionElement())
      composeMarkdown(t, "GraphDefinition", "description", element.getDescriptionElement(), -1);
    for (int i = 0; i < element.getUseContext().size(); i++)
      composeUsageContext(t, "GraphDefinition", "useContext", element.getUseContext().get(i), i);
    for (int i = 0; i < element.getJurisdiction().size(); i++)
      composeCodeableConcept(t, "GraphDefinition", "jurisdiction", element.getJurisdiction().get(i), i);
    if (element.hasPurposeElement())
      composeMarkdown(t, "GraphDefinition", "purpose", element.getPurposeElement(), -1);
    if (element.hasStartElement())
      composeCode(t, "GraphDefinition", "start", element.getStartElement(), -1);
    if (element.hasProfileElement())
      composeUri(t, "GraphDefinition", "profile", element.getProfileElement(), -1);
    for (int i = 0; i < element.getLink().size(); i++)
      composeGraphDefinitionGraphDefinitionLinkComponent(t, "GraphDefinition", "link", element.getLink().get(i), i);
  }

  protected void composeGraphDefinitionGraphDefinitionLinkComponent(Complex parent, String parentType, String name, GraphDefinition.GraphDefinitionLinkComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "link", name, element, index);
    if (element.hasPathElement())
      composeString(t, "GraphDefinition", "path", element.getPathElement(), -1);
    if (element.hasSliceNameElement())
      composeString(t, "GraphDefinition", "sliceName", element.getSliceNameElement(), -1);
    if (element.hasMinElement())
      composeInteger(t, "GraphDefinition", "min", element.getMinElement(), -1);
    if (element.hasMaxElement())
      composeString(t, "GraphDefinition", "max", element.getMaxElement(), -1);
    if (element.hasDescriptionElement())
      composeString(t, "GraphDefinition", "description", element.getDescriptionElement(), -1);
    for (int i = 0; i < element.getTarget().size(); i++)
      composeGraphDefinitionGraphDefinitionLinkTargetComponent(t, "GraphDefinition", "target", element.getTarget().get(i), i);
  }

  protected void composeGraphDefinitionGraphDefinitionLinkTargetComponent(Complex parent, String parentType, String name, GraphDefinition.GraphDefinitionLinkTargetComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "target", name, element, index);
    if (element.hasTypeElement())
      composeCode(t, "GraphDefinition", "type", element.getTypeElement(), -1);
    if (element.hasParamsElement())
      composeString(t, "GraphDefinition", "params", element.getParamsElement(), -1);
    if (element.hasProfileElement())
      composeUri(t, "GraphDefinition", "profile", element.getProfileElement(), -1);
    for (int i = 0; i < element.getCompartment().size(); i++)
      composeGraphDefinitionGraphDefinitionLinkTargetCompartmentComponent(t, "GraphDefinition", "compartment", element.getCompartment().get(i), i);
    for (int i = 0; i < element.getLink().size(); i++)
      composeGraphDefinitionGraphDefinitionLinkComponent(t, "GraphDefinition", "link", element.getLink().get(i), i);
  }

  protected void composeGraphDefinitionGraphDefinitionLinkTargetCompartmentComponent(Complex parent, String parentType, String name, GraphDefinition.GraphDefinitionLinkTargetCompartmentComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "compartment", name, element, index);
    if (element.hasUseElement())
      composeEnum(t, "GraphDefinition", "use", element.getUseElement(), -1);
    if (element.hasCodeElement())
      composeEnum(t, "GraphDefinition", "code", element.getCodeElement(), -1);
    if (element.hasRuleElement())
      composeEnum(t, "GraphDefinition", "rule", element.getRuleElement(), -1);
    if (element.hasExpressionElement())
      composeString(t, "GraphDefinition", "expression", element.getExpressionElement(), -1);
    if (element.hasDescriptionElement())
      composeString(t, "GraphDefinition", "description", element.getDescriptionElement(), -1);
  }

  protected void composeGroup(Complex parent, String parentType, String name, Group element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "Group", name, element, index);
    for (int i = 0; i < element.getIdentifier().size(); i++)
      composeIdentifier(t, "Group", "identifier", element.getIdentifier().get(i), i);
    if (element.hasActiveElement())
      composeBoolean(t, "Group", "active", element.getActiveElement(), -1);
    if (element.hasTypeElement())
      composeEnum(t, "Group", "type", element.getTypeElement(), -1);
    if (element.hasActualElement())
      composeBoolean(t, "Group", "actual", element.getActualElement(), -1);
    if (element.hasCode())
      composeCodeableConcept(t, "Group", "code", element.getCode(), -1);
    if (element.hasNameElement())
      composeString(t, "Group", "name", element.getNameElement(), -1);
    if (element.hasQuantityElement())
      composeUnsignedInt(t, "Group", "quantity", element.getQuantityElement(), -1);
    for (int i = 0; i < element.getCharacteristic().size(); i++)
      composeGroupGroupCharacteristicComponent(t, "Group", "characteristic", element.getCharacteristic().get(i), i);
    for (int i = 0; i < element.getMember().size(); i++)
      composeGroupGroupMemberComponent(t, "Group", "member", element.getMember().get(i), i);
  }

  protected void composeGroupGroupCharacteristicComponent(Complex parent, String parentType, String name, Group.GroupCharacteristicComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "characteristic", name, element, index);
    if (element.hasCode())
      composeCodeableConcept(t, "Group", "code", element.getCode(), -1);
    if (element.hasValue())
      composeType(t, "Group", "value", element.getValue(), -1);
    if (element.hasExcludeElement())
      composeBoolean(t, "Group", "exclude", element.getExcludeElement(), -1);
    if (element.hasPeriod())
      composePeriod(t, "Group", "period", element.getPeriod(), -1);
  }

  protected void composeGroupGroupMemberComponent(Complex parent, String parentType, String name, Group.GroupMemberComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "member", name, element, index);
    if (element.hasEntity())
      composeReference(t, "Group", "entity", element.getEntity(), -1);
    if (element.hasPeriod())
      composePeriod(t, "Group", "period", element.getPeriod(), -1);
    if (element.hasInactiveElement())
      composeBoolean(t, "Group", "inactive", element.getInactiveElement(), -1);
  }

  protected void composeGuidanceResponse(Complex parent, String parentType, String name, GuidanceResponse element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "GuidanceResponse", name, element, index);
    if (element.hasRequestIdElement())
      composeId(t, "GuidanceResponse", "requestId", element.getRequestIdElement(), -1);
    if (element.hasIdentifier())
      composeIdentifier(t, "GuidanceResponse", "identifier", element.getIdentifier(), -1);
    if (element.hasModule())
      composeReference(t, "GuidanceResponse", "module", element.getModule(), -1);
    if (element.hasStatusElement())
      composeEnum(t, "GuidanceResponse", "status", element.getStatusElement(), -1);
    if (element.hasSubject())
      composeReference(t, "GuidanceResponse", "subject", element.getSubject(), -1);
    if (element.hasContext())
      composeReference(t, "GuidanceResponse", "context", element.getContext(), -1);
    if (element.hasOccurrenceDateTimeElement())
      composeDateTime(t, "GuidanceResponse", "occurrenceDateTime", element.getOccurrenceDateTimeElement(), -1);
    if (element.hasPerformer())
      composeReference(t, "GuidanceResponse", "performer", element.getPerformer(), -1);
    if (element.hasReason())
      composeType(t, "GuidanceResponse", "reason", element.getReason(), -1);
    for (int i = 0; i < element.getNote().size(); i++)
      composeAnnotation(t, "GuidanceResponse", "note", element.getNote().get(i), i);
    for (int i = 0; i < element.getEvaluationMessage().size(); i++)
      composeReference(t, "GuidanceResponse", "evaluationMessage", element.getEvaluationMessage().get(i), i);
    if (element.hasOutputParameters())
      composeReference(t, "GuidanceResponse", "outputParameters", element.getOutputParameters(), -1);
    if (element.hasResult())
      composeReference(t, "GuidanceResponse", "result", element.getResult(), -1);
    for (int i = 0; i < element.getDataRequirement().size(); i++)
      composeDataRequirement(t, "GuidanceResponse", "dataRequirement", element.getDataRequirement().get(i), i);
  }

  protected void composeHealthcareService(Complex parent, String parentType, String name, HealthcareService element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "HealthcareService", name, element, index);
    for (int i = 0; i < element.getIdentifier().size(); i++)
      composeIdentifier(t, "HealthcareService", "identifier", element.getIdentifier().get(i), i);
    if (element.hasActiveElement())
      composeBoolean(t, "HealthcareService", "active", element.getActiveElement(), -1);
    if (element.hasProvidedBy())
      composeReference(t, "HealthcareService", "providedBy", element.getProvidedBy(), -1);
    if (element.hasCategory())
      composeCodeableConcept(t, "HealthcareService", "category", element.getCategory(), -1);
    for (int i = 0; i < element.getType().size(); i++)
      composeCodeableConcept(t, "HealthcareService", "type", element.getType().get(i), i);
    for (int i = 0; i < element.getSpecialty().size(); i++)
      composeCodeableConcept(t, "HealthcareService", "specialty", element.getSpecialty().get(i), i);
    for (int i = 0; i < element.getLocation().size(); i++)
      composeReference(t, "HealthcareService", "location", element.getLocation().get(i), i);
    if (element.hasNameElement())
      composeString(t, "HealthcareService", "name", element.getNameElement(), -1);
    if (element.hasCommentElement())
      composeString(t, "HealthcareService", "comment", element.getCommentElement(), -1);
    if (element.hasExtraDetailsElement())
      composeString(t, "HealthcareService", "extraDetails", element.getExtraDetailsElement(), -1);
    if (element.hasPhoto())
      composeAttachment(t, "HealthcareService", "photo", element.getPhoto(), -1);
    for (int i = 0; i < element.getTelecom().size(); i++)
      composeContactPoint(t, "HealthcareService", "telecom", element.getTelecom().get(i), i);
    for (int i = 0; i < element.getCoverageArea().size(); i++)
      composeReference(t, "HealthcareService", "coverageArea", element.getCoverageArea().get(i), i);
    for (int i = 0; i < element.getServiceProvisionCode().size(); i++)
      composeCodeableConcept(t, "HealthcareService", "serviceProvisionCode", element.getServiceProvisionCode().get(i), i);
    if (element.hasEligibility())
      composeCodeableConcept(t, "HealthcareService", "eligibility", element.getEligibility(), -1);
    if (element.hasEligibilityNoteElement())
      composeString(t, "HealthcareService", "eligibilityNote", element.getEligibilityNoteElement(), -1);
    for (int i = 0; i < element.getProgramName().size(); i++)
      composeString(t, "HealthcareService", "programName", element.getProgramName().get(i), i);
    for (int i = 0; i < element.getCharacteristic().size(); i++)
      composeCodeableConcept(t, "HealthcareService", "characteristic", element.getCharacteristic().get(i), i);
    for (int i = 0; i < element.getReferralMethod().size(); i++)
      composeCodeableConcept(t, "HealthcareService", "referralMethod", element.getReferralMethod().get(i), i);
    if (element.hasAppointmentRequiredElement())
      composeBoolean(t, "HealthcareService", "appointmentRequired", element.getAppointmentRequiredElement(), -1);
    for (int i = 0; i < element.getAvailableTime().size(); i++)
      composeHealthcareServiceHealthcareServiceAvailableTimeComponent(t, "HealthcareService", "availableTime", element.getAvailableTime().get(i), i);
    for (int i = 0; i < element.getNotAvailable().size(); i++)
      composeHealthcareServiceHealthcareServiceNotAvailableComponent(t, "HealthcareService", "notAvailable", element.getNotAvailable().get(i), i);
    if (element.hasAvailabilityExceptionsElement())
      composeString(t, "HealthcareService", "availabilityExceptions", element.getAvailabilityExceptionsElement(), -1);
    for (int i = 0; i < element.getEndpoint().size(); i++)
      composeReference(t, "HealthcareService", "endpoint", element.getEndpoint().get(i), i);
  }

  protected void composeHealthcareServiceHealthcareServiceAvailableTimeComponent(Complex parent, String parentType, String name, HealthcareService.HealthcareServiceAvailableTimeComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "availableTime", name, element, index);
    for (int i = 0; i < element.getDaysOfWeek().size(); i++)
      composeEnum(t, "HealthcareService", "daysOfWeek", element.getDaysOfWeek().get(i), i);
    if (element.hasAllDayElement())
      composeBoolean(t, "HealthcareService", "allDay", element.getAllDayElement(), -1);
    if (element.hasAvailableStartTimeElement())
      composeTime(t, "HealthcareService", "availableStartTime", element.getAvailableStartTimeElement(), -1);
    if (element.hasAvailableEndTimeElement())
      composeTime(t, "HealthcareService", "availableEndTime", element.getAvailableEndTimeElement(), -1);
  }

  protected void composeHealthcareServiceHealthcareServiceNotAvailableComponent(Complex parent, String parentType, String name, HealthcareService.HealthcareServiceNotAvailableComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "notAvailable", name, element, index);
    if (element.hasDescriptionElement())
      composeString(t, "HealthcareService", "description", element.getDescriptionElement(), -1);
    if (element.hasDuring())
      composePeriod(t, "HealthcareService", "during", element.getDuring(), -1);
  }

  protected void composeImagingManifest(Complex parent, String parentType, String name, ImagingManifest element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "ImagingManifest", name, element, index);
    if (element.hasIdentifier())
      composeIdentifier(t, "ImagingManifest", "identifier", element.getIdentifier(), -1);
    if (element.hasPatient())
      composeReference(t, "ImagingManifest", "patient", element.getPatient(), -1);
    if (element.hasAuthoringTimeElement())
      composeDateTime(t, "ImagingManifest", "authoringTime", element.getAuthoringTimeElement(), -1);
    if (element.hasAuthor())
      composeReference(t, "ImagingManifest", "author", element.getAuthor(), -1);
    if (element.hasDescriptionElement())
      composeString(t, "ImagingManifest", "description", element.getDescriptionElement(), -1);
    for (int i = 0; i < element.getStudy().size(); i++)
      composeImagingManifestStudyComponent(t, "ImagingManifest", "study", element.getStudy().get(i), i);
  }

  protected void composeImagingManifestStudyComponent(Complex parent, String parentType, String name, ImagingManifest.StudyComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "study", name, element, index);
    if (element.hasUidElement())
      composeOid(t, "ImagingManifest", "uid", element.getUidElement(), -1);
    if (element.hasImagingStudy())
      composeReference(t, "ImagingManifest", "imagingStudy", element.getImagingStudy(), -1);
    for (int i = 0; i < element.getEndpoint().size(); i++)
      composeReference(t, "ImagingManifest", "endpoint", element.getEndpoint().get(i), i);
    for (int i = 0; i < element.getSeries().size(); i++)
      composeImagingManifestSeriesComponent(t, "ImagingManifest", "series", element.getSeries().get(i), i);
  }

  protected void composeImagingManifestSeriesComponent(Complex parent, String parentType, String name, ImagingManifest.SeriesComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "series", name, element, index);
    if (element.hasUidElement())
      composeOid(t, "ImagingManifest", "uid", element.getUidElement(), -1);
    for (int i = 0; i < element.getEndpoint().size(); i++)
      composeReference(t, "ImagingManifest", "endpoint", element.getEndpoint().get(i), i);
    for (int i = 0; i < element.getInstance().size(); i++)
      composeImagingManifestInstanceComponent(t, "ImagingManifest", "instance", element.getInstance().get(i), i);
  }

  protected void composeImagingManifestInstanceComponent(Complex parent, String parentType, String name, ImagingManifest.InstanceComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "instance", name, element, index);
    if (element.hasSopClassElement())
      composeOid(t, "ImagingManifest", "sopClass", element.getSopClassElement(), -1);
    if (element.hasUidElement())
      composeOid(t, "ImagingManifest", "uid", element.getUidElement(), -1);
    for (int i = 0; i < element.getFrameNumber().size(); i++)
      composeUnsignedInt(t, "ImagingManifest", "frameNumber", element.getFrameNumber().get(i), i);
  }

  protected void composeImagingStudy(Complex parent, String parentType, String name, ImagingStudy element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "ImagingStudy", name, element, index);
    if (element.hasUidElement())
      composeOid(t, "ImagingStudy", "uid", element.getUidElement(), -1);
    if (element.hasAccession())
      composeIdentifier(t, "ImagingStudy", "accession", element.getAccession(), -1);
    for (int i = 0; i < element.getIdentifier().size(); i++)
      composeIdentifier(t, "ImagingStudy", "identifier", element.getIdentifier().get(i), i);
    if (element.hasAvailabilityElement())
      composeEnum(t, "ImagingStudy", "availability", element.getAvailabilityElement(), -1);
    for (int i = 0; i < element.getModalityList().size(); i++)
      composeCoding(t, "ImagingStudy", "modalityList", element.getModalityList().get(i), i);
    if (element.hasPatient())
      composeReference(t, "ImagingStudy", "patient", element.getPatient(), -1);
    if (element.hasContext())
      composeReference(t, "ImagingStudy", "context", element.getContext(), -1);
    if (element.hasStartedElement())
      composeDateTime(t, "ImagingStudy", "started", element.getStartedElement(), -1);
    for (int i = 0; i < element.getBasedOn().size(); i++)
      composeReference(t, "ImagingStudy", "basedOn", element.getBasedOn().get(i), i);
    if (element.hasReferrer())
      composeReference(t, "ImagingStudy", "referrer", element.getReferrer(), -1);
    for (int i = 0; i < element.getInterpreter().size(); i++)
      composeReference(t, "ImagingStudy", "interpreter", element.getInterpreter().get(i), i);
    for (int i = 0; i < element.getEndpoint().size(); i++)
      composeReference(t, "ImagingStudy", "endpoint", element.getEndpoint().get(i), i);
    if (element.hasNumberOfSeriesElement())
      composeUnsignedInt(t, "ImagingStudy", "numberOfSeries", element.getNumberOfSeriesElement(), -1);
    if (element.hasNumberOfInstancesElement())
      composeUnsignedInt(t, "ImagingStudy", "numberOfInstances", element.getNumberOfInstancesElement(), -1);
    for (int i = 0; i < element.getProcedureReference().size(); i++)
      composeReference(t, "ImagingStudy", "procedureReference", element.getProcedureReference().get(i), i);
    for (int i = 0; i < element.getProcedureCode().size(); i++)
      composeCodeableConcept(t, "ImagingStudy", "procedureCode", element.getProcedureCode().get(i), i);
    if (element.hasReason())
      composeCodeableConcept(t, "ImagingStudy", "reason", element.getReason(), -1);
    if (element.hasDescriptionElement())
      composeString(t, "ImagingStudy", "description", element.getDescriptionElement(), -1);
    for (int i = 0; i < element.getSeries().size(); i++)
      composeImagingStudyImagingStudySeriesComponent(t, "ImagingStudy", "series", element.getSeries().get(i), i);
  }

  protected void composeImagingStudyImagingStudySeriesComponent(Complex parent, String parentType, String name, ImagingStudy.ImagingStudySeriesComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "series", name, element, index);
    if (element.hasUidElement())
      composeOid(t, "ImagingStudy", "uid", element.getUidElement(), -1);
    if (element.hasNumberElement())
      composeUnsignedInt(t, "ImagingStudy", "number", element.getNumberElement(), -1);
    if (element.hasModality())
      composeCoding(t, "ImagingStudy", "modality", element.getModality(), -1);
    if (element.hasDescriptionElement())
      composeString(t, "ImagingStudy", "description", element.getDescriptionElement(), -1);
    if (element.hasNumberOfInstancesElement())
      composeUnsignedInt(t, "ImagingStudy", "numberOfInstances", element.getNumberOfInstancesElement(), -1);
    if (element.hasAvailabilityElement())
      composeEnum(t, "ImagingStudy", "availability", element.getAvailabilityElement(), -1);
    for (int i = 0; i < element.getEndpoint().size(); i++)
      composeReference(t, "ImagingStudy", "endpoint", element.getEndpoint().get(i), i);
    if (element.hasBodySite())
      composeCoding(t, "ImagingStudy", "bodySite", element.getBodySite(), -1);
    if (element.hasLaterality())
      composeCoding(t, "ImagingStudy", "laterality", element.getLaterality(), -1);
    if (element.hasStartedElement())
      composeDateTime(t, "ImagingStudy", "started", element.getStartedElement(), -1);
    for (int i = 0; i < element.getPerformer().size(); i++)
      composeReference(t, "ImagingStudy", "performer", element.getPerformer().get(i), i);
    for (int i = 0; i < element.getInstance().size(); i++)
      composeImagingStudyImagingStudySeriesInstanceComponent(t, "ImagingStudy", "instance", element.getInstance().get(i), i);
  }

  protected void composeImagingStudyImagingStudySeriesInstanceComponent(Complex parent, String parentType, String name, ImagingStudy.ImagingStudySeriesInstanceComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "instance", name, element, index);
    if (element.hasUidElement())
      composeOid(t, "ImagingStudy", "uid", element.getUidElement(), -1);
    if (element.hasNumberElement())
      composeUnsignedInt(t, "ImagingStudy", "number", element.getNumberElement(), -1);
    if (element.hasSopClassElement())
      composeOid(t, "ImagingStudy", "sopClass", element.getSopClassElement(), -1);
    if (element.hasTitleElement())
      composeString(t, "ImagingStudy", "title", element.getTitleElement(), -1);
  }

  protected void composeImmunization(Complex parent, String parentType, String name, Immunization element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "Immunization", name, element, index);
    for (int i = 0; i < element.getIdentifier().size(); i++)
      composeIdentifier(t, "Immunization", "identifier", element.getIdentifier().get(i), i);
    if (element.hasStatusElement())
      composeEnum(t, "Immunization", "status", element.getStatusElement(), -1);
    if (element.hasNotGivenElement())
      composeBoolean(t, "Immunization", "notGiven", element.getNotGivenElement(), -1);
    if (element.hasVaccineCode())
      composeCodeableConcept(t, "Immunization", "vaccineCode", element.getVaccineCode(), -1);
    if (element.hasPatient())
      composeReference(t, "Immunization", "patient", element.getPatient(), -1);
    if (element.hasEncounter())
      composeReference(t, "Immunization", "encounter", element.getEncounter(), -1);
    if (element.hasDateElement())
      composeDateTime(t, "Immunization", "date", element.getDateElement(), -1);
    if (element.hasPrimarySourceElement())
      composeBoolean(t, "Immunization", "primarySource", element.getPrimarySourceElement(), -1);
    if (element.hasReportOrigin())
      composeCodeableConcept(t, "Immunization", "reportOrigin", element.getReportOrigin(), -1);
    if (element.hasLocation())
      composeReference(t, "Immunization", "location", element.getLocation(), -1);
    if (element.hasManufacturer())
      composeReference(t, "Immunization", "manufacturer", element.getManufacturer(), -1);
    if (element.hasLotNumberElement())
      composeString(t, "Immunization", "lotNumber", element.getLotNumberElement(), -1);
    if (element.hasExpirationDateElement())
      composeDate(t, "Immunization", "expirationDate", element.getExpirationDateElement(), -1);
    if (element.hasSite())
      composeCodeableConcept(t, "Immunization", "site", element.getSite(), -1);
    if (element.hasRoute())
      composeCodeableConcept(t, "Immunization", "route", element.getRoute(), -1);
    if (element.hasDoseQuantity())
      composeQuantity(t, "Immunization", "doseQuantity", element.getDoseQuantity(), -1);
    for (int i = 0; i < element.getPractitioner().size(); i++)
      composeImmunizationImmunizationPractitionerComponent(t, "Immunization", "practitioner", element.getPractitioner().get(i), i);
    for (int i = 0; i < element.getNote().size(); i++)
      composeAnnotation(t, "Immunization", "note", element.getNote().get(i), i);
    if (element.hasExplanation())
      composeImmunizationImmunizationExplanationComponent(t, "Immunization", "explanation", element.getExplanation(), -1);
    for (int i = 0; i < element.getReaction().size(); i++)
      composeImmunizationImmunizationReactionComponent(t, "Immunization", "reaction", element.getReaction().get(i), i);
    for (int i = 0; i < element.getVaccinationProtocol().size(); i++)
      composeImmunizationImmunizationVaccinationProtocolComponent(t, "Immunization", "vaccinationProtocol", element.getVaccinationProtocol().get(i), i);
  }

  protected void composeImmunizationImmunizationPractitionerComponent(Complex parent, String parentType, String name, Immunization.ImmunizationPractitionerComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "practitioner", name, element, index);
    if (element.hasRole())
      composeCodeableConcept(t, "Immunization", "role", element.getRole(), -1);
    if (element.hasActor())
      composeReference(t, "Immunization", "actor", element.getActor(), -1);
  }

  protected void composeImmunizationImmunizationExplanationComponent(Complex parent, String parentType, String name, Immunization.ImmunizationExplanationComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "explanation", name, element, index);
    for (int i = 0; i < element.getReason().size(); i++)
      composeCodeableConcept(t, "Immunization", "reason", element.getReason().get(i), i);
    for (int i = 0; i < element.getReasonNotGiven().size(); i++)
      composeCodeableConcept(t, "Immunization", "reasonNotGiven", element.getReasonNotGiven().get(i), i);
  }

  protected void composeImmunizationImmunizationReactionComponent(Complex parent, String parentType, String name, Immunization.ImmunizationReactionComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "reaction", name, element, index);
    if (element.hasDateElement())
      composeDateTime(t, "Immunization", "date", element.getDateElement(), -1);
    if (element.hasDetail())
      composeReference(t, "Immunization", "detail", element.getDetail(), -1);
    if (element.hasReportedElement())
      composeBoolean(t, "Immunization", "reported", element.getReportedElement(), -1);
  }

  protected void composeImmunizationImmunizationVaccinationProtocolComponent(Complex parent, String parentType, String name, Immunization.ImmunizationVaccinationProtocolComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "vaccinationProtocol", name, element, index);
    if (element.hasDoseSequenceElement())
      composePositiveInt(t, "Immunization", "doseSequence", element.getDoseSequenceElement(), -1);
    if (element.hasDescriptionElement())
      composeString(t, "Immunization", "description", element.getDescriptionElement(), -1);
    if (element.hasAuthority())
      composeReference(t, "Immunization", "authority", element.getAuthority(), -1);
    if (element.hasSeriesElement())
      composeString(t, "Immunization", "series", element.getSeriesElement(), -1);
    if (element.hasSeriesDosesElement())
      composePositiveInt(t, "Immunization", "seriesDoses", element.getSeriesDosesElement(), -1);
    for (int i = 0; i < element.getTargetDisease().size(); i++)
      composeCodeableConcept(t, "Immunization", "targetDisease", element.getTargetDisease().get(i), i);
    if (element.hasDoseStatus())
      composeCodeableConcept(t, "Immunization", "doseStatus", element.getDoseStatus(), -1);
    if (element.hasDoseStatusReason())
      composeCodeableConcept(t, "Immunization", "doseStatusReason", element.getDoseStatusReason(), -1);
  }

  protected void composeImmunizationRecommendation(Complex parent, String parentType, String name, ImmunizationRecommendation element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "ImmunizationRecommendation", name, element, index);
    for (int i = 0; i < element.getIdentifier().size(); i++)
      composeIdentifier(t, "ImmunizationRecommendation", "identifier", element.getIdentifier().get(i), i);
    if (element.hasPatient())
      composeReference(t, "ImmunizationRecommendation", "patient", element.getPatient(), -1);
    for (int i = 0; i < element.getRecommendation().size(); i++)
      composeImmunizationRecommendationImmunizationRecommendationRecommendationComponent(t, "ImmunizationRecommendation", "recommendation", element.getRecommendation().get(i), i);
  }

  protected void composeImmunizationRecommendationImmunizationRecommendationRecommendationComponent(Complex parent, String parentType, String name, ImmunizationRecommendation.ImmunizationRecommendationRecommendationComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "recommendation", name, element, index);
    if (element.hasDateElement())
      composeDateTime(t, "ImmunizationRecommendation", "date", element.getDateElement(), -1);
    if (element.hasVaccineCode())
      composeCodeableConcept(t, "ImmunizationRecommendation", "vaccineCode", element.getVaccineCode(), -1);
    if (element.hasTargetDisease())
      composeCodeableConcept(t, "ImmunizationRecommendation", "targetDisease", element.getTargetDisease(), -1);
    if (element.hasDoseNumberElement())
      composePositiveInt(t, "ImmunizationRecommendation", "doseNumber", element.getDoseNumberElement(), -1);
    if (element.hasForecastStatus())
      composeCodeableConcept(t, "ImmunizationRecommendation", "forecastStatus", element.getForecastStatus(), -1);
    for (int i = 0; i < element.getDateCriterion().size(); i++)
      composeImmunizationRecommendationImmunizationRecommendationRecommendationDateCriterionComponent(t, "ImmunizationRecommendation", "dateCriterion", element.getDateCriterion().get(i), i);
    if (element.hasProtocol())
      composeImmunizationRecommendationImmunizationRecommendationRecommendationProtocolComponent(t, "ImmunizationRecommendation", "protocol", element.getProtocol(), -1);
    for (int i = 0; i < element.getSupportingImmunization().size(); i++)
      composeReference(t, "ImmunizationRecommendation", "supportingImmunization", element.getSupportingImmunization().get(i), i);
    for (int i = 0; i < element.getSupportingPatientInformation().size(); i++)
      composeReference(t, "ImmunizationRecommendation", "supportingPatientInformation", element.getSupportingPatientInformation().get(i), i);
  }

  protected void composeImmunizationRecommendationImmunizationRecommendationRecommendationDateCriterionComponent(Complex parent, String parentType, String name, ImmunizationRecommendation.ImmunizationRecommendationRecommendationDateCriterionComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "dateCriterion", name, element, index);
    if (element.hasCode())
      composeCodeableConcept(t, "ImmunizationRecommendation", "code", element.getCode(), -1);
    if (element.hasValueElement())
      composeDateTime(t, "ImmunizationRecommendation", "value", element.getValueElement(), -1);
  }

  protected void composeImmunizationRecommendationImmunizationRecommendationRecommendationProtocolComponent(Complex parent, String parentType, String name, ImmunizationRecommendation.ImmunizationRecommendationRecommendationProtocolComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "protocol", name, element, index);
    if (element.hasDoseSequenceElement())
      composePositiveInt(t, "ImmunizationRecommendation", "doseSequence", element.getDoseSequenceElement(), -1);
    if (element.hasDescriptionElement())
      composeString(t, "ImmunizationRecommendation", "description", element.getDescriptionElement(), -1);
    if (element.hasAuthority())
      composeReference(t, "ImmunizationRecommendation", "authority", element.getAuthority(), -1);
    if (element.hasSeriesElement())
      composeString(t, "ImmunizationRecommendation", "series", element.getSeriesElement(), -1);
  }

  protected void composeImplementationGuide(Complex parent, String parentType, String name, ImplementationGuide element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "ImplementationGuide", name, element, index);
    if (element.hasUrlElement())
      composeUri(t, "ImplementationGuide", "url", element.getUrlElement(), -1);
    if (element.hasVersionElement())
      composeString(t, "ImplementationGuide", "version", element.getVersionElement(), -1);
    if (element.hasNameElement())
      composeString(t, "ImplementationGuide", "name", element.getNameElement(), -1);
    if (element.hasStatusElement())
      composeEnum(t, "ImplementationGuide", "status", element.getStatusElement(), -1);
    if (element.hasExperimentalElement())
      composeBoolean(t, "ImplementationGuide", "experimental", element.getExperimentalElement(), -1);
    if (element.hasDateElement())
      composeDateTime(t, "ImplementationGuide", "date", element.getDateElement(), -1);
    if (element.hasPublisherElement())
      composeString(t, "ImplementationGuide", "publisher", element.getPublisherElement(), -1);
    for (int i = 0; i < element.getContact().size(); i++)
      composeContactDetail(t, "ImplementationGuide", "contact", element.getContact().get(i), i);
    if (element.hasDescriptionElement())
      composeMarkdown(t, "ImplementationGuide", "description", element.getDescriptionElement(), -1);
    for (int i = 0; i < element.getUseContext().size(); i++)
      composeUsageContext(t, "ImplementationGuide", "useContext", element.getUseContext().get(i), i);
    for (int i = 0; i < element.getJurisdiction().size(); i++)
      composeCodeableConcept(t, "ImplementationGuide", "jurisdiction", element.getJurisdiction().get(i), i);
    if (element.hasCopyrightElement())
      composeMarkdown(t, "ImplementationGuide", "copyright", element.getCopyrightElement(), -1);
    if (element.hasFhirVersionElement())
      composeId(t, "ImplementationGuide", "fhirVersion", element.getFhirVersionElement(), -1);
    for (int i = 0; i < element.getDependency().size(); i++)
      composeImplementationGuideImplementationGuideDependencyComponent(t, "ImplementationGuide", "dependency", element.getDependency().get(i), i);
    for (int i = 0; i < element.getPackage().size(); i++)
      composeImplementationGuideImplementationGuidePackageComponent(t, "ImplementationGuide", "package", element.getPackage().get(i), i);
    for (int i = 0; i < element.getGlobal().size(); i++)
      composeImplementationGuideImplementationGuideGlobalComponent(t, "ImplementationGuide", "global", element.getGlobal().get(i), i);
    for (int i = 0; i < element.getBinary().size(); i++)
      composeUri(t, "ImplementationGuide", "binary", element.getBinary().get(i), i);
    if (element.hasPage())
      composeImplementationGuideImplementationGuidePageComponent(t, "ImplementationGuide", "page", element.getPage(), -1);
  }

  protected void composeImplementationGuideImplementationGuideDependencyComponent(Complex parent, String parentType, String name, ImplementationGuide.ImplementationGuideDependencyComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "dependency", name, element, index);
    if (element.hasTypeElement())
      composeEnum(t, "ImplementationGuide", "type", element.getTypeElement(), -1);
    if (element.hasUriElement())
      composeUri(t, "ImplementationGuide", "uri", element.getUriElement(), -1);
  }

  protected void composeImplementationGuideImplementationGuidePackageComponent(Complex parent, String parentType, String name, ImplementationGuide.ImplementationGuidePackageComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "package", name, element, index);
    if (element.hasNameElement())
      composeString(t, "ImplementationGuide", "name", element.getNameElement(), -1);
    if (element.hasDescriptionElement())
      composeString(t, "ImplementationGuide", "description", element.getDescriptionElement(), -1);
    for (int i = 0; i < element.getResource().size(); i++)
      composeImplementationGuideImplementationGuidePackageResourceComponent(t, "ImplementationGuide", "resource", element.getResource().get(i), i);
  }

  protected void composeImplementationGuideImplementationGuidePackageResourceComponent(Complex parent, String parentType, String name, ImplementationGuide.ImplementationGuidePackageResourceComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "resource", name, element, index);
    if (element.hasExampleElement())
      composeBoolean(t, "ImplementationGuide", "example", element.getExampleElement(), -1);
    if (element.hasNameElement())
      composeString(t, "ImplementationGuide", "name", element.getNameElement(), -1);
    if (element.hasDescriptionElement())
      composeString(t, "ImplementationGuide", "description", element.getDescriptionElement(), -1);
    if (element.hasAcronymElement())
      composeString(t, "ImplementationGuide", "acronym", element.getAcronymElement(), -1);
    if (element.hasSource())
      composeType(t, "ImplementationGuide", "source", element.getSource(), -1);
    if (element.hasExampleFor())
      composeReference(t, "ImplementationGuide", "exampleFor", element.getExampleFor(), -1);
  }

  protected void composeImplementationGuideImplementationGuideGlobalComponent(Complex parent, String parentType, String name, ImplementationGuide.ImplementationGuideGlobalComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "global", name, element, index);
    if (element.hasTypeElement())
      composeCode(t, "ImplementationGuide", "type", element.getTypeElement(), -1);
    if (element.hasProfile())
      composeReference(t, "ImplementationGuide", "profile", element.getProfile(), -1);
  }

  protected void composeImplementationGuideImplementationGuidePageComponent(Complex parent, String parentType, String name, ImplementationGuide.ImplementationGuidePageComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "page", name, element, index);
    if (element.hasSourceElement())
      composeUri(t, "ImplementationGuide", "source", element.getSourceElement(), -1);
    if (element.hasTitleElement())
      composeString(t, "ImplementationGuide", "title", element.getTitleElement(), -1);
    if (element.hasKindElement())
      composeEnum(t, "ImplementationGuide", "kind", element.getKindElement(), -1);
    for (int i = 0; i < element.getType().size(); i++)
      composeCode(t, "ImplementationGuide", "type", element.getType().get(i), i);
    for (int i = 0; i < element.getPackage().size(); i++)
      composeString(t, "ImplementationGuide", "package", element.getPackage().get(i), i);
    if (element.hasFormatElement())
      composeCode(t, "ImplementationGuide", "format", element.getFormatElement(), -1);
    for (int i = 0; i < element.getPage().size(); i++)
      composeImplementationGuideImplementationGuidePageComponent(t, "ImplementationGuide", "page", element.getPage().get(i), i);
  }

  protected void composeLibrary(Complex parent, String parentType, String name, Library element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "Library", name, element, index);
    if (element.hasUrlElement())
      composeUri(t, "Library", "url", element.getUrlElement(), -1);
    for (int i = 0; i < element.getIdentifier().size(); i++)
      composeIdentifier(t, "Library", "identifier", element.getIdentifier().get(i), i);
    if (element.hasVersionElement())
      composeString(t, "Library", "version", element.getVersionElement(), -1);
    if (element.hasNameElement())
      composeString(t, "Library", "name", element.getNameElement(), -1);
    if (element.hasTitleElement())
      composeString(t, "Library", "title", element.getTitleElement(), -1);
    if (element.hasStatusElement())
      composeEnum(t, "Library", "status", element.getStatusElement(), -1);
    if (element.hasExperimentalElement())
      composeBoolean(t, "Library", "experimental", element.getExperimentalElement(), -1);
    if (element.hasType())
      composeCodeableConcept(t, "Library", "type", element.getType(), -1);
    if (element.hasDateElement())
      composeDateTime(t, "Library", "date", element.getDateElement(), -1);
    if (element.hasPublisherElement())
      composeString(t, "Library", "publisher", element.getPublisherElement(), -1);
    if (element.hasDescriptionElement())
      composeMarkdown(t, "Library", "description", element.getDescriptionElement(), -1);
    if (element.hasPurposeElement())
      composeMarkdown(t, "Library", "purpose", element.getPurposeElement(), -1);
    if (element.hasUsageElement())
      composeString(t, "Library", "usage", element.getUsageElement(), -1);
    if (element.hasApprovalDateElement())
      composeDate(t, "Library", "approvalDate", element.getApprovalDateElement(), -1);
    if (element.hasLastReviewDateElement())
      composeDate(t, "Library", "lastReviewDate", element.getLastReviewDateElement(), -1);
    if (element.hasEffectivePeriod())
      composePeriod(t, "Library", "effectivePeriod", element.getEffectivePeriod(), -1);
    for (int i = 0; i < element.getUseContext().size(); i++)
      composeUsageContext(t, "Library", "useContext", element.getUseContext().get(i), i);
    for (int i = 0; i < element.getJurisdiction().size(); i++)
      composeCodeableConcept(t, "Library", "jurisdiction", element.getJurisdiction().get(i), i);
    for (int i = 0; i < element.getTopic().size(); i++)
      composeCodeableConcept(t, "Library", "topic", element.getTopic().get(i), i);
    for (int i = 0; i < element.getContributor().size(); i++)
      composeContributor(t, "Library", "contributor", element.getContributor().get(i), i);
    for (int i = 0; i < element.getContact().size(); i++)
      composeContactDetail(t, "Library", "contact", element.getContact().get(i), i);
    if (element.hasCopyrightElement())
      composeMarkdown(t, "Library", "copyright", element.getCopyrightElement(), -1);
    for (int i = 0; i < element.getRelatedArtifact().size(); i++)
      composeRelatedArtifact(t, "Library", "relatedArtifact", element.getRelatedArtifact().get(i), i);
    for (int i = 0; i < element.getParameter().size(); i++)
      composeParameterDefinition(t, "Library", "parameter", element.getParameter().get(i), i);
    for (int i = 0; i < element.getDataRequirement().size(); i++)
      composeDataRequirement(t, "Library", "dataRequirement", element.getDataRequirement().get(i), i);
    for (int i = 0; i < element.getContent().size(); i++)
      composeAttachment(t, "Library", "content", element.getContent().get(i), i);
  }

  protected void composeLinkage(Complex parent, String parentType, String name, Linkage element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "Linkage", name, element, index);
    if (element.hasActiveElement())
      composeBoolean(t, "Linkage", "active", element.getActiveElement(), -1);
    if (element.hasAuthor())
      composeReference(t, "Linkage", "author", element.getAuthor(), -1);
    for (int i = 0; i < element.getItem().size(); i++)
      composeLinkageLinkageItemComponent(t, "Linkage", "item", element.getItem().get(i), i);
  }

  protected void composeLinkageLinkageItemComponent(Complex parent, String parentType, String name, Linkage.LinkageItemComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "item", name, element, index);
    if (element.hasTypeElement())
      composeEnum(t, "Linkage", "type", element.getTypeElement(), -1);
    if (element.hasResource())
      composeReference(t, "Linkage", "resource", element.getResource(), -1);
  }

  protected void composeListResource(Complex parent, String parentType, String name, ListResource element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "List", name, element, index);
    for (int i = 0; i < element.getIdentifier().size(); i++)
      composeIdentifier(t, "List", "identifier", element.getIdentifier().get(i), i);
    if (element.hasStatusElement())
      composeEnum(t, "List", "status", element.getStatusElement(), -1);
    if (element.hasModeElement())
      composeEnum(t, "List", "mode", element.getModeElement(), -1);
    if (element.hasTitleElement())
      composeString(t, "List", "title", element.getTitleElement(), -1);
    if (element.hasCode())
      composeCodeableConcept(t, "List", "code", element.getCode(), -1);
    if (element.hasSubject())
      composeReference(t, "List", "subject", element.getSubject(), -1);
    if (element.hasEncounter())
      composeReference(t, "List", "encounter", element.getEncounter(), -1);
    if (element.hasDateElement())
      composeDateTime(t, "List", "date", element.getDateElement(), -1);
    if (element.hasSource())
      composeReference(t, "List", "source", element.getSource(), -1);
    if (element.hasOrderedBy())
      composeCodeableConcept(t, "List", "orderedBy", element.getOrderedBy(), -1);
    for (int i = 0; i < element.getNote().size(); i++)
      composeAnnotation(t, "List", "note", element.getNote().get(i), i);
    for (int i = 0; i < element.getEntry().size(); i++)
      composeListResourceListEntryComponent(t, "List", "entry", element.getEntry().get(i), i);
    if (element.hasEmptyReason())
      composeCodeableConcept(t, "List", "emptyReason", element.getEmptyReason(), -1);
  }

  protected void composeListResourceListEntryComponent(Complex parent, String parentType, String name, ListResource.ListEntryComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "entry", name, element, index);
    if (element.hasFlag())
      composeCodeableConcept(t, "List", "flag", element.getFlag(), -1);
    if (element.hasDeletedElement())
      composeBoolean(t, "List", "deleted", element.getDeletedElement(), -1);
    if (element.hasDateElement())
      composeDateTime(t, "List", "date", element.getDateElement(), -1);
    if (element.hasItem())
      composeReference(t, "List", "item", element.getItem(), -1);
  }

  protected void composeLocation(Complex parent, String parentType, String name, Location element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "Location", name, element, index);
    for (int i = 0; i < element.getIdentifier().size(); i++)
      composeIdentifier(t, "Location", "identifier", element.getIdentifier().get(i), i);
    if (element.hasStatusElement())
      composeEnum(t, "Location", "status", element.getStatusElement(), -1);
    if (element.hasOperationalStatus())
      composeCoding(t, "Location", "operationalStatus", element.getOperationalStatus(), -1);
    if (element.hasNameElement())
      composeString(t, "Location", "name", element.getNameElement(), -1);
    for (int i = 0; i < element.getAlias().size(); i++)
      composeString(t, "Location", "alias", element.getAlias().get(i), i);
    if (element.hasDescriptionElement())
      composeString(t, "Location", "description", element.getDescriptionElement(), -1);
    if (element.hasModeElement())
      composeEnum(t, "Location", "mode", element.getModeElement(), -1);
    if (element.hasType())
      composeCodeableConcept(t, "Location", "type", element.getType(), -1);
    for (int i = 0; i < element.getTelecom().size(); i++)
      composeContactPoint(t, "Location", "telecom", element.getTelecom().get(i), i);
    if (element.hasAddress())
      composeAddress(t, "Location", "address", element.getAddress(), -1);
    if (element.hasPhysicalType())
      composeCodeableConcept(t, "Location", "physicalType", element.getPhysicalType(), -1);
    if (element.hasPosition())
      composeLocationLocationPositionComponent(t, "Location", "position", element.getPosition(), -1);
    if (element.hasManagingOrganization())
      composeReference(t, "Location", "managingOrganization", element.getManagingOrganization(), -1);
    if (element.hasPartOf())
      composeReference(t, "Location", "partOf", element.getPartOf(), -1);
    for (int i = 0; i < element.getEndpoint().size(); i++)
      composeReference(t, "Location", "endpoint", element.getEndpoint().get(i), i);
  }

  protected void composeLocationLocationPositionComponent(Complex parent, String parentType, String name, Location.LocationPositionComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "position", name, element, index);
    if (element.hasLongitudeElement())
      composeDecimal(t, "Location", "longitude", element.getLongitudeElement(), -1);
    if (element.hasLatitudeElement())
      composeDecimal(t, "Location", "latitude", element.getLatitudeElement(), -1);
    if (element.hasAltitudeElement())
      composeDecimal(t, "Location", "altitude", element.getAltitudeElement(), -1);
  }

  protected void composeMeasure(Complex parent, String parentType, String name, Measure element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "Measure", name, element, index);
    if (element.hasUrlElement())
      composeUri(t, "Measure", "url", element.getUrlElement(), -1);
    for (int i = 0; i < element.getIdentifier().size(); i++)
      composeIdentifier(t, "Measure", "identifier", element.getIdentifier().get(i), i);
    if (element.hasVersionElement())
      composeString(t, "Measure", "version", element.getVersionElement(), -1);
    if (element.hasNameElement())
      composeString(t, "Measure", "name", element.getNameElement(), -1);
    if (element.hasTitleElement())
      composeString(t, "Measure", "title", element.getTitleElement(), -1);
    if (element.hasStatusElement())
      composeEnum(t, "Measure", "status", element.getStatusElement(), -1);
    if (element.hasExperimentalElement())
      composeBoolean(t, "Measure", "experimental", element.getExperimentalElement(), -1);
    if (element.hasDateElement())
      composeDateTime(t, "Measure", "date", element.getDateElement(), -1);
    if (element.hasPublisherElement())
      composeString(t, "Measure", "publisher", element.getPublisherElement(), -1);
    if (element.hasDescriptionElement())
      composeMarkdown(t, "Measure", "description", element.getDescriptionElement(), -1);
    if (element.hasPurposeElement())
      composeMarkdown(t, "Measure", "purpose", element.getPurposeElement(), -1);
    if (element.hasUsageElement())
      composeString(t, "Measure", "usage", element.getUsageElement(), -1);
    if (element.hasApprovalDateElement())
      composeDate(t, "Measure", "approvalDate", element.getApprovalDateElement(), -1);
    if (element.hasLastReviewDateElement())
      composeDate(t, "Measure", "lastReviewDate", element.getLastReviewDateElement(), -1);
    if (element.hasEffectivePeriod())
      composePeriod(t, "Measure", "effectivePeriod", element.getEffectivePeriod(), -1);
    for (int i = 0; i < element.getUseContext().size(); i++)
      composeUsageContext(t, "Measure", "useContext", element.getUseContext().get(i), i);
    for (int i = 0; i < element.getJurisdiction().size(); i++)
      composeCodeableConcept(t, "Measure", "jurisdiction", element.getJurisdiction().get(i), i);
    for (int i = 0; i < element.getTopic().size(); i++)
      composeCodeableConcept(t, "Measure", "topic", element.getTopic().get(i), i);
    for (int i = 0; i < element.getContributor().size(); i++)
      composeContributor(t, "Measure", "contributor", element.getContributor().get(i), i);
    for (int i = 0; i < element.getContact().size(); i++)
      composeContactDetail(t, "Measure", "contact", element.getContact().get(i), i);
    if (element.hasCopyrightElement())
      composeMarkdown(t, "Measure", "copyright", element.getCopyrightElement(), -1);
    for (int i = 0; i < element.getRelatedArtifact().size(); i++)
      composeRelatedArtifact(t, "Measure", "relatedArtifact", element.getRelatedArtifact().get(i), i);
    for (int i = 0; i < element.getLibrary().size(); i++)
      composeReference(t, "Measure", "library", element.getLibrary().get(i), i);
    if (element.hasDisclaimerElement())
      composeMarkdown(t, "Measure", "disclaimer", element.getDisclaimerElement(), -1);
    if (element.hasScoring())
      composeCodeableConcept(t, "Measure", "scoring", element.getScoring(), -1);
    if (element.hasCompositeScoring())
      composeCodeableConcept(t, "Measure", "compositeScoring", element.getCompositeScoring(), -1);
    for (int i = 0; i < element.getType().size(); i++)
      composeCodeableConcept(t, "Measure", "type", element.getType().get(i), i);
    if (element.hasRiskAdjustmentElement())
      composeString(t, "Measure", "riskAdjustment", element.getRiskAdjustmentElement(), -1);
    if (element.hasRateAggregationElement())
      composeString(t, "Measure", "rateAggregation", element.getRateAggregationElement(), -1);
    if (element.hasRationaleElement())
      composeMarkdown(t, "Measure", "rationale", element.getRationaleElement(), -1);
    if (element.hasClinicalRecommendationStatementElement())
      composeMarkdown(t, "Measure", "clinicalRecommendationStatement", element.getClinicalRecommendationStatementElement(), -1);
    if (element.hasImprovementNotationElement())
      composeString(t, "Measure", "improvementNotation", element.getImprovementNotationElement(), -1);
    for (int i = 0; i < element.getDefinition().size(); i++)
      composeMarkdown(t, "Measure", "definition", element.getDefinition().get(i), i);
    if (element.hasGuidanceElement())
      composeMarkdown(t, "Measure", "guidance", element.getGuidanceElement(), -1);
    if (element.hasSetElement())
      composeString(t, "Measure", "set", element.getSetElement(), -1);
    for (int i = 0; i < element.getGroup().size(); i++)
      composeMeasureMeasureGroupComponent(t, "Measure", "group", element.getGroup().get(i), i);
    for (int i = 0; i < element.getSupplementalData().size(); i++)
      composeMeasureMeasureSupplementalDataComponent(t, "Measure", "supplementalData", element.getSupplementalData().get(i), i);
  }

  protected void composeMeasureMeasureGroupComponent(Complex parent, String parentType, String name, Measure.MeasureGroupComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "group", name, element, index);
    if (element.hasIdentifier())
      composeIdentifier(t, "Measure", "identifier", element.getIdentifier(), -1);
    if (element.hasNameElement())
      composeString(t, "Measure", "name", element.getNameElement(), -1);
    if (element.hasDescriptionElement())
      composeString(t, "Measure", "description", element.getDescriptionElement(), -1);
    for (int i = 0; i < element.getPopulation().size(); i++)
      composeMeasureMeasureGroupPopulationComponent(t, "Measure", "population", element.getPopulation().get(i), i);
    for (int i = 0; i < element.getStratifier().size(); i++)
      composeMeasureMeasureGroupStratifierComponent(t, "Measure", "stratifier", element.getStratifier().get(i), i);
  }

  protected void composeMeasureMeasureGroupPopulationComponent(Complex parent, String parentType, String name, Measure.MeasureGroupPopulationComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "population", name, element, index);
    if (element.hasIdentifier())
      composeIdentifier(t, "Measure", "identifier", element.getIdentifier(), -1);
    if (element.hasCode())
      composeCodeableConcept(t, "Measure", "code", element.getCode(), -1);
    if (element.hasNameElement())
      composeString(t, "Measure", "name", element.getNameElement(), -1);
    if (element.hasDescriptionElement())
      composeString(t, "Measure", "description", element.getDescriptionElement(), -1);
    if (element.hasCriteriaElement())
      composeString(t, "Measure", "criteria", element.getCriteriaElement(), -1);
  }

  protected void composeMeasureMeasureGroupStratifierComponent(Complex parent, String parentType, String name, Measure.MeasureGroupStratifierComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "stratifier", name, element, index);
    if (element.hasIdentifier())
      composeIdentifier(t, "Measure", "identifier", element.getIdentifier(), -1);
    if (element.hasCriteriaElement())
      composeString(t, "Measure", "criteria", element.getCriteriaElement(), -1);
    if (element.hasPathElement())
      composeString(t, "Measure", "path", element.getPathElement(), -1);
  }

  protected void composeMeasureMeasureSupplementalDataComponent(Complex parent, String parentType, String name, Measure.MeasureSupplementalDataComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "supplementalData", name, element, index);
    if (element.hasIdentifier())
      composeIdentifier(t, "Measure", "identifier", element.getIdentifier(), -1);
    for (int i = 0; i < element.getUsage().size(); i++)
      composeCodeableConcept(t, "Measure", "usage", element.getUsage().get(i), i);
    if (element.hasCriteriaElement())
      composeString(t, "Measure", "criteria", element.getCriteriaElement(), -1);
    if (element.hasPathElement())
      composeString(t, "Measure", "path", element.getPathElement(), -1);
  }

  protected void composeMeasureReport(Complex parent, String parentType, String name, MeasureReport element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "MeasureReport", name, element, index);
    if (element.hasIdentifier())
      composeIdentifier(t, "MeasureReport", "identifier", element.getIdentifier(), -1);
    if (element.hasStatusElement())
      composeEnum(t, "MeasureReport", "status", element.getStatusElement(), -1);
    if (element.hasTypeElement())
      composeEnum(t, "MeasureReport", "type", element.getTypeElement(), -1);
    if (element.hasMeasure())
      composeReference(t, "MeasureReport", "measure", element.getMeasure(), -1);
    if (element.hasPatient())
      composeReference(t, "MeasureReport", "patient", element.getPatient(), -1);
    if (element.hasDateElement())
      composeDateTime(t, "MeasureReport", "date", element.getDateElement(), -1);
    if (element.hasReportingOrganization())
      composeReference(t, "MeasureReport", "reportingOrganization", element.getReportingOrganization(), -1);
    if (element.hasPeriod())
      composePeriod(t, "MeasureReport", "period", element.getPeriod(), -1);
    for (int i = 0; i < element.getGroup().size(); i++)
      composeMeasureReportMeasureReportGroupComponent(t, "MeasureReport", "group", element.getGroup().get(i), i);
    if (element.hasEvaluatedResources())
      composeReference(t, "MeasureReport", "evaluatedResources", element.getEvaluatedResources(), -1);
  }

  protected void composeMeasureReportMeasureReportGroupComponent(Complex parent, String parentType, String name, MeasureReport.MeasureReportGroupComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "group", name, element, index);
    if (element.hasIdentifier())
      composeIdentifier(t, "MeasureReport", "identifier", element.getIdentifier(), -1);
    for (int i = 0; i < element.getPopulation().size(); i++)
      composeMeasureReportMeasureReportGroupPopulationComponent(t, "MeasureReport", "population", element.getPopulation().get(i), i);
    if (element.hasMeasureScoreElement())
      composeDecimal(t, "MeasureReport", "measureScore", element.getMeasureScoreElement(), -1);
    for (int i = 0; i < element.getStratifier().size(); i++)
      composeMeasureReportMeasureReportGroupStratifierComponent(t, "MeasureReport", "stratifier", element.getStratifier().get(i), i);
  }

  protected void composeMeasureReportMeasureReportGroupPopulationComponent(Complex parent, String parentType, String name, MeasureReport.MeasureReportGroupPopulationComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "population", name, element, index);
    if (element.hasIdentifier())
      composeIdentifier(t, "MeasureReport", "identifier", element.getIdentifier(), -1);
    if (element.hasCode())
      composeCodeableConcept(t, "MeasureReport", "code", element.getCode(), -1);
    if (element.hasCountElement())
      composeInteger(t, "MeasureReport", "count", element.getCountElement(), -1);
    if (element.hasPatients())
      composeReference(t, "MeasureReport", "patients", element.getPatients(), -1);
  }

  protected void composeMeasureReportMeasureReportGroupStratifierComponent(Complex parent, String parentType, String name, MeasureReport.MeasureReportGroupStratifierComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "stratifier", name, element, index);
    if (element.hasIdentifier())
      composeIdentifier(t, "MeasureReport", "identifier", element.getIdentifier(), -1);
    for (int i = 0; i < element.getStratum().size(); i++)
      composeMeasureReportStratifierGroupComponent(t, "MeasureReport", "stratum", element.getStratum().get(i), i);
  }

  protected void composeMeasureReportStratifierGroupComponent(Complex parent, String parentType, String name, MeasureReport.StratifierGroupComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "stratum", name, element, index);
    if (element.hasValueElement())
      composeString(t, "MeasureReport", "value", element.getValueElement(), -1);
    for (int i = 0; i < element.getPopulation().size(); i++)
      composeMeasureReportStratifierGroupPopulationComponent(t, "MeasureReport", "population", element.getPopulation().get(i), i);
    if (element.hasMeasureScoreElement())
      composeDecimal(t, "MeasureReport", "measureScore", element.getMeasureScoreElement(), -1);
  }

  protected void composeMeasureReportStratifierGroupPopulationComponent(Complex parent, String parentType, String name, MeasureReport.StratifierGroupPopulationComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "population", name, element, index);
    if (element.hasIdentifier())
      composeIdentifier(t, "MeasureReport", "identifier", element.getIdentifier(), -1);
    if (element.hasCode())
      composeCodeableConcept(t, "MeasureReport", "code", element.getCode(), -1);
    if (element.hasCountElement())
      composeInteger(t, "MeasureReport", "count", element.getCountElement(), -1);
    if (element.hasPatients())
      composeReference(t, "MeasureReport", "patients", element.getPatients(), -1);
  }

  protected void composeMedia(Complex parent, String parentType, String name, Media element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "Media", name, element, index);
    for (int i = 0; i < element.getIdentifier().size(); i++)
      composeIdentifier(t, "Media", "identifier", element.getIdentifier().get(i), i);
    for (int i = 0; i < element.getBasedOn().size(); i++)
      composeReference(t, "Media", "basedOn", element.getBasedOn().get(i), i);
    if (element.hasTypeElement())
      composeEnum(t, "Media", "type", element.getTypeElement(), -1);
    if (element.hasSubtype())
      composeCodeableConcept(t, "Media", "subtype", element.getSubtype(), -1);
    if (element.hasView())
      composeCodeableConcept(t, "Media", "view", element.getView(), -1);
    if (element.hasSubject())
      composeReference(t, "Media", "subject", element.getSubject(), -1);
    if (element.hasContext())
      composeReference(t, "Media", "context", element.getContext(), -1);
    if (element.hasOccurrence())
      composeType(t, "Media", "occurrence", element.getOccurrence(), -1);
    if (element.hasOperator())
      composeReference(t, "Media", "operator", element.getOperator(), -1);
    for (int i = 0; i < element.getReasonCode().size(); i++)
      composeCodeableConcept(t, "Media", "reasonCode", element.getReasonCode().get(i), i);
    if (element.hasBodySite())
      composeCodeableConcept(t, "Media", "bodySite", element.getBodySite(), -1);
    if (element.hasDevice())
      composeReference(t, "Media", "device", element.getDevice(), -1);
    if (element.hasHeightElement())
      composePositiveInt(t, "Media", "height", element.getHeightElement(), -1);
    if (element.hasWidthElement())
      composePositiveInt(t, "Media", "width", element.getWidthElement(), -1);
    if (element.hasFramesElement())
      composePositiveInt(t, "Media", "frames", element.getFramesElement(), -1);
    if (element.hasDurationElement())
      composeUnsignedInt(t, "Media", "duration", element.getDurationElement(), -1);
    if (element.hasContent())
      composeAttachment(t, "Media", "content", element.getContent(), -1);
    for (int i = 0; i < element.getNote().size(); i++)
      composeAnnotation(t, "Media", "note", element.getNote().get(i), i);
  }

  protected void composeMedication(Complex parent, String parentType, String name, Medication element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "Medication", name, element, index);
    if (element.hasCode())
      composeCodeableConcept(t, "Medication", "code", element.getCode(), -1);
    if (element.hasStatusElement())
      composeEnum(t, "Medication", "status", element.getStatusElement(), -1);
    if (element.hasIsBrandElement())
      composeBoolean(t, "Medication", "isBrand", element.getIsBrandElement(), -1);
    if (element.hasIsOverTheCounterElement())
      composeBoolean(t, "Medication", "isOverTheCounter", element.getIsOverTheCounterElement(), -1);
    if (element.hasManufacturer())
      composeReference(t, "Medication", "manufacturer", element.getManufacturer(), -1);
    if (element.hasForm())
      composeCodeableConcept(t, "Medication", "form", element.getForm(), -1);
    for (int i = 0; i < element.getIngredient().size(); i++)
      composeMedicationMedicationIngredientComponent(t, "Medication", "ingredient", element.getIngredient().get(i), i);
    if (element.hasPackage())
      composeMedicationMedicationPackageComponent(t, "Medication", "package", element.getPackage(), -1);
    for (int i = 0; i < element.getImage().size(); i++)
      composeAttachment(t, "Medication", "image", element.getImage().get(i), i);
  }

  protected void composeMedicationMedicationIngredientComponent(Complex parent, String parentType, String name, Medication.MedicationIngredientComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "ingredient", name, element, index);
    if (element.hasItem())
      composeType(t, "Medication", "item", element.getItem(), -1);
    if (element.hasIsActiveElement())
      composeBoolean(t, "Medication", "isActive", element.getIsActiveElement(), -1);
    if (element.hasAmount())
      composeRatio(t, "Medication", "amount", element.getAmount(), -1);
  }

  protected void composeMedicationMedicationPackageComponent(Complex parent, String parentType, String name, Medication.MedicationPackageComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "package", name, element, index);
    if (element.hasContainer())
      composeCodeableConcept(t, "Medication", "container", element.getContainer(), -1);
    for (int i = 0; i < element.getContent().size(); i++)
      composeMedicationMedicationPackageContentComponent(t, "Medication", "content", element.getContent().get(i), i);
    for (int i = 0; i < element.getBatch().size(); i++)
      composeMedicationMedicationPackageBatchComponent(t, "Medication", "batch", element.getBatch().get(i), i);
  }

  protected void composeMedicationMedicationPackageContentComponent(Complex parent, String parentType, String name, Medication.MedicationPackageContentComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "content", name, element, index);
    if (element.hasItem())
      composeType(t, "Medication", "item", element.getItem(), -1);
    if (element.hasAmount())
      composeQuantity(t, "Medication", "amount", element.getAmount(), -1);
  }

  protected void composeMedicationMedicationPackageBatchComponent(Complex parent, String parentType, String name, Medication.MedicationPackageBatchComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "batch", name, element, index);
    if (element.hasLotNumberElement())
      composeString(t, "Medication", "lotNumber", element.getLotNumberElement(), -1);
    if (element.hasExpirationDateElement())
      composeDateTime(t, "Medication", "expirationDate", element.getExpirationDateElement(), -1);
  }

  protected void composeMedicationAdministration(Complex parent, String parentType, String name, MedicationAdministration element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "MedicationAdministration", name, element, index);
    for (int i = 0; i < element.getIdentifier().size(); i++)
      composeIdentifier(t, "MedicationAdministration", "identifier", element.getIdentifier().get(i), i);
    for (int i = 0; i < element.getDefinition().size(); i++)
      composeReference(t, "MedicationAdministration", "definition", element.getDefinition().get(i), i);
    for (int i = 0; i < element.getPartOf().size(); i++)
      composeReference(t, "MedicationAdministration", "partOf", element.getPartOf().get(i), i);
    if (element.hasStatusElement())
      composeEnum(t, "MedicationAdministration", "status", element.getStatusElement(), -1);
    if (element.hasCategory())
      composeCodeableConcept(t, "MedicationAdministration", "category", element.getCategory(), -1);
    if (element.hasMedication())
      composeType(t, "MedicationAdministration", "medication", element.getMedication(), -1);
    if (element.hasSubject())
      composeReference(t, "MedicationAdministration", "subject", element.getSubject(), -1);
    if (element.hasContext())
      composeReference(t, "MedicationAdministration", "context", element.getContext(), -1);
    for (int i = 0; i < element.getSupportingInformation().size(); i++)
      composeReference(t, "MedicationAdministration", "supportingInformation", element.getSupportingInformation().get(i), i);
    if (element.hasEffective())
      composeType(t, "MedicationAdministration", "effective", element.getEffective(), -1);
    for (int i = 0; i < element.getPerformer().size(); i++)
      composeMedicationAdministrationMedicationAdministrationPerformerComponent(t, "MedicationAdministration", "performer", element.getPerformer().get(i), i);
    if (element.hasNotGivenElement())
      composeBoolean(t, "MedicationAdministration", "notGiven", element.getNotGivenElement(), -1);
    for (int i = 0; i < element.getReasonNotGiven().size(); i++)
      composeCodeableConcept(t, "MedicationAdministration", "reasonNotGiven", element.getReasonNotGiven().get(i), i);
    for (int i = 0; i < element.getReasonCode().size(); i++)
      composeCodeableConcept(t, "MedicationAdministration", "reasonCode", element.getReasonCode().get(i), i);
    for (int i = 0; i < element.getReasonReference().size(); i++)
      composeReference(t, "MedicationAdministration", "reasonReference", element.getReasonReference().get(i), i);
    if (element.hasRequest())
      composeReference(t, "MedicationAdministration", "request", element.getRequest(), -1);
    for (int i = 0; i < element.getDevice().size(); i++)
      composeReference(t, "MedicationAdministration", "device", element.getDevice().get(i), i);
    for (int i = 0; i < element.getNote().size(); i++)
      composeAnnotation(t, "MedicationAdministration", "note", element.getNote().get(i), i);
    if (element.hasDosage())
      composeMedicationAdministrationMedicationAdministrationDosageComponent(t, "MedicationAdministration", "dosage", element.getDosage(), -1);
    for (int i = 0; i < element.getEventHistory().size(); i++)
      composeReference(t, "MedicationAdministration", "eventHistory", element.getEventHistory().get(i), i);
  }

  protected void composeMedicationAdministrationMedicationAdministrationPerformerComponent(Complex parent, String parentType, String name, MedicationAdministration.MedicationAdministrationPerformerComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "performer", name, element, index);
    if (element.hasActor())
      composeReference(t, "MedicationAdministration", "actor", element.getActor(), -1);
    if (element.hasOnBehalfOf())
      composeReference(t, "MedicationAdministration", "onBehalfOf", element.getOnBehalfOf(), -1);
  }

  protected void composeMedicationAdministrationMedicationAdministrationDosageComponent(Complex parent, String parentType, String name, MedicationAdministration.MedicationAdministrationDosageComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "dosage", name, element, index);
    if (element.hasTextElement())
      composeString(t, "MedicationAdministration", "text", element.getTextElement(), -1);
    if (element.hasSite())
      composeCodeableConcept(t, "MedicationAdministration", "site", element.getSite(), -1);
    if (element.hasRoute())
      composeCodeableConcept(t, "MedicationAdministration", "route", element.getRoute(), -1);
    if (element.hasMethod())
      composeCodeableConcept(t, "MedicationAdministration", "method", element.getMethod(), -1);
    if (element.hasDose())
      composeQuantity(t, "MedicationAdministration", "dose", element.getDose(), -1);
    if (element.hasRate())
      composeType(t, "MedicationAdministration", "rate", element.getRate(), -1);
  }

  protected void composeMedicationDispense(Complex parent, String parentType, String name, MedicationDispense element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "MedicationDispense", name, element, index);
    for (int i = 0; i < element.getIdentifier().size(); i++)
      composeIdentifier(t, "MedicationDispense", "identifier", element.getIdentifier().get(i), i);
    for (int i = 0; i < element.getPartOf().size(); i++)
      composeReference(t, "MedicationDispense", "partOf", element.getPartOf().get(i), i);
    if (element.hasStatusElement())
      composeEnum(t, "MedicationDispense", "status", element.getStatusElement(), -1);
    if (element.hasCategory())
      composeCodeableConcept(t, "MedicationDispense", "category", element.getCategory(), -1);
    if (element.hasMedication())
      composeType(t, "MedicationDispense", "medication", element.getMedication(), -1);
    if (element.hasSubject())
      composeReference(t, "MedicationDispense", "subject", element.getSubject(), -1);
    if (element.hasContext())
      composeReference(t, "MedicationDispense", "context", element.getContext(), -1);
    for (int i = 0; i < element.getSupportingInformation().size(); i++)
      composeReference(t, "MedicationDispense", "supportingInformation", element.getSupportingInformation().get(i), i);
    for (int i = 0; i < element.getPerformer().size(); i++)
      composeMedicationDispenseMedicationDispensePerformerComponent(t, "MedicationDispense", "performer", element.getPerformer().get(i), i);
    for (int i = 0; i < element.getAuthorizingPrescription().size(); i++)
      composeReference(t, "MedicationDispense", "authorizingPrescription", element.getAuthorizingPrescription().get(i), i);
    if (element.hasType())
      composeCodeableConcept(t, "MedicationDispense", "type", element.getType(), -1);
    if (element.hasQuantity())
      composeQuantity(t, "MedicationDispense", "quantity", element.getQuantity(), -1);
    if (element.hasDaysSupply())
      composeQuantity(t, "MedicationDispense", "daysSupply", element.getDaysSupply(), -1);
    if (element.hasWhenPreparedElement())
      composeDateTime(t, "MedicationDispense", "whenPrepared", element.getWhenPreparedElement(), -1);
    if (element.hasWhenHandedOverElement())
      composeDateTime(t, "MedicationDispense", "whenHandedOver", element.getWhenHandedOverElement(), -1);
    if (element.hasDestination())
      composeReference(t, "MedicationDispense", "destination", element.getDestination(), -1);
    for (int i = 0; i < element.getReceiver().size(); i++)
      composeReference(t, "MedicationDispense", "receiver", element.getReceiver().get(i), i);
    for (int i = 0; i < element.getNote().size(); i++)
      composeAnnotation(t, "MedicationDispense", "note", element.getNote().get(i), i);
    for (int i = 0; i < element.getDosageInstruction().size(); i++)
      composeDosage(t, "MedicationDispense", "dosageInstruction", element.getDosageInstruction().get(i), i);
    if (element.hasSubstitution())
      composeMedicationDispenseMedicationDispenseSubstitutionComponent(t, "MedicationDispense", "substitution", element.getSubstitution(), -1);
    for (int i = 0; i < element.getDetectedIssue().size(); i++)
      composeReference(t, "MedicationDispense", "detectedIssue", element.getDetectedIssue().get(i), i);
    if (element.hasNotDoneElement())
      composeBoolean(t, "MedicationDispense", "notDone", element.getNotDoneElement(), -1);
    if (element.hasNotDoneReason())
      composeType(t, "MedicationDispense", "notDoneReason", element.getNotDoneReason(), -1);
    for (int i = 0; i < element.getEventHistory().size(); i++)
      composeReference(t, "MedicationDispense", "eventHistory", element.getEventHistory().get(i), i);
  }

  protected void composeMedicationDispenseMedicationDispensePerformerComponent(Complex parent, String parentType, String name, MedicationDispense.MedicationDispensePerformerComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "performer", name, element, index);
    if (element.hasActor())
      composeReference(t, "MedicationDispense", "actor", element.getActor(), -1);
    if (element.hasOnBehalfOf())
      composeReference(t, "MedicationDispense", "onBehalfOf", element.getOnBehalfOf(), -1);
  }

  protected void composeMedicationDispenseMedicationDispenseSubstitutionComponent(Complex parent, String parentType, String name, MedicationDispense.MedicationDispenseSubstitutionComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "substitution", name, element, index);
    if (element.hasWasSubstitutedElement())
      composeBoolean(t, "MedicationDispense", "wasSubstituted", element.getWasSubstitutedElement(), -1);
    if (element.hasType())
      composeCodeableConcept(t, "MedicationDispense", "type", element.getType(), -1);
    for (int i = 0; i < element.getReason().size(); i++)
      composeCodeableConcept(t, "MedicationDispense", "reason", element.getReason().get(i), i);
    for (int i = 0; i < element.getResponsibleParty().size(); i++)
      composeReference(t, "MedicationDispense", "responsibleParty", element.getResponsibleParty().get(i), i);
  }

  protected void composeMedicationRequest(Complex parent, String parentType, String name, MedicationRequest element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "MedicationRequest", name, element, index);
    for (int i = 0; i < element.getIdentifier().size(); i++)
      composeIdentifier(t, "MedicationRequest", "identifier", element.getIdentifier().get(i), i);
    for (int i = 0; i < element.getDefinition().size(); i++)
      composeReference(t, "MedicationRequest", "definition", element.getDefinition().get(i), i);
    for (int i = 0; i < element.getBasedOn().size(); i++)
      composeReference(t, "MedicationRequest", "basedOn", element.getBasedOn().get(i), i);
    if (element.hasGroupIdentifier())
      composeIdentifier(t, "MedicationRequest", "groupIdentifier", element.getGroupIdentifier(), -1);
    if (element.hasStatusElement())
      composeEnum(t, "MedicationRequest", "status", element.getStatusElement(), -1);
    if (element.hasIntentElement())
      composeEnum(t, "MedicationRequest", "intent", element.getIntentElement(), -1);
    for (int i = 0; i < element.getCategory().size(); i++)
      composeCodeableConcept(t, "MedicationRequest", "category", element.getCategory().get(i), i);
    if (element.hasPriorityElement())
      composeEnum(t, "MedicationRequest", "priority", element.getPriorityElement(), -1);
    if (element.hasMedication())
      composeType(t, "MedicationRequest", "medication", element.getMedication(), -1);
    if (element.hasSubject())
      composeReference(t, "MedicationRequest", "subject", element.getSubject(), -1);
    if (element.hasContext())
      composeReference(t, "MedicationRequest", "context", element.getContext(), -1);
    for (int i = 0; i < element.getSupportingInformation().size(); i++)
      composeReference(t, "MedicationRequest", "supportingInformation", element.getSupportingInformation().get(i), i);
    if (element.hasAuthoredOnElement())
      composeDateTime(t, "MedicationRequest", "authoredOn", element.getAuthoredOnElement(), -1);
    if (element.hasRequester())
      composeMedicationRequestMedicationRequestRequesterComponent(t, "MedicationRequest", "requester", element.getRequester(), -1);
    if (element.hasRecorder())
      composeReference(t, "MedicationRequest", "recorder", element.getRecorder(), -1);
    if (element.hasPerformer())
      composeReference(t, "MedicationRequest", "performer", element.getPerformer(), -1);
    for (int i = 0; i < element.getReasonCode().size(); i++)
      composeCodeableConcept(t, "MedicationRequest", "reasonCode", element.getReasonCode().get(i), i);
    for (int i = 0; i < element.getReasonReference().size(); i++)
      composeReference(t, "MedicationRequest", "reasonReference", element.getReasonReference().get(i), i);
    for (int i = 0; i < element.getNote().size(); i++)
      composeAnnotation(t, "MedicationRequest", "note", element.getNote().get(i), i);
    for (int i = 0; i < element.getDosageInstruction().size(); i++)
      composeDosage(t, "MedicationRequest", "dosageInstruction", element.getDosageInstruction().get(i), i);
    if (element.hasDispenseRequest())
      composeMedicationRequestMedicationRequestDispenseRequestComponent(t, "MedicationRequest", "dispenseRequest", element.getDispenseRequest(), -1);
    if (element.hasSubstitution())
      composeMedicationRequestMedicationRequestSubstitutionComponent(t, "MedicationRequest", "substitution", element.getSubstitution(), -1);
    if (element.hasPriorPrescription())
      composeReference(t, "MedicationRequest", "priorPrescription", element.getPriorPrescription(), -1);
    for (int i = 0; i < element.getDetectedIssue().size(); i++)
      composeReference(t, "MedicationRequest", "detectedIssue", element.getDetectedIssue().get(i), i);
    for (int i = 0; i < element.getEventHistory().size(); i++)
      composeReference(t, "MedicationRequest", "eventHistory", element.getEventHistory().get(i), i);
  }

  protected void composeMedicationRequestMedicationRequestRequesterComponent(Complex parent, String parentType, String name, MedicationRequest.MedicationRequestRequesterComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "requester", name, element, index);
    if (element.hasAgent())
      composeReference(t, "MedicationRequest", "agent", element.getAgent(), -1);
    if (element.hasOnBehalfOf())
      composeReference(t, "MedicationRequest", "onBehalfOf", element.getOnBehalfOf(), -1);
  }

  protected void composeMedicationRequestMedicationRequestDispenseRequestComponent(Complex parent, String parentType, String name, MedicationRequest.MedicationRequestDispenseRequestComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "dispenseRequest", name, element, index);
    if (element.hasValidityPeriod())
      composePeriod(t, "MedicationRequest", "validityPeriod", element.getValidityPeriod(), -1);
    if (element.hasNumberOfRepeatsAllowedElement())
      composeUnsignedInt(t, "MedicationRequest", "numberOfRepeatsAllowed", element.getNumberOfRepeatsAllowedElement(), -1);
    if (element.hasQuantity())
      composeQuantity(t, "MedicationRequest", "quantity", element.getQuantity(), -1);
    if (element.hasExpectedSupplyDuration())
      composeDuration(t, "MedicationRequest", "expectedSupplyDuration", element.getExpectedSupplyDuration(), -1);
    if (element.hasPerformer())
      composeReference(t, "MedicationRequest", "performer", element.getPerformer(), -1);
  }

  protected void composeMedicationRequestMedicationRequestSubstitutionComponent(Complex parent, String parentType, String name, MedicationRequest.MedicationRequestSubstitutionComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "substitution", name, element, index);
    if (element.hasAllowedElement())
      composeBoolean(t, "MedicationRequest", "allowed", element.getAllowedElement(), -1);
    if (element.hasReason())
      composeCodeableConcept(t, "MedicationRequest", "reason", element.getReason(), -1);
  }

  protected void composeMedicationStatement(Complex parent, String parentType, String name, MedicationStatement element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "MedicationStatement", name, element, index);
    for (int i = 0; i < element.getIdentifier().size(); i++)
      composeIdentifier(t, "MedicationStatement", "identifier", element.getIdentifier().get(i), i);
    for (int i = 0; i < element.getBasedOn().size(); i++)
      composeReference(t, "MedicationStatement", "basedOn", element.getBasedOn().get(i), i);
    for (int i = 0; i < element.getPartOf().size(); i++)
      composeReference(t, "MedicationStatement", "partOf", element.getPartOf().get(i), i);
    if (element.hasContext())
      composeReference(t, "MedicationStatement", "context", element.getContext(), -1);
    if (element.hasStatusElement())
      composeEnum(t, "MedicationStatement", "status", element.getStatusElement(), -1);
    if (element.hasCategory())
      composeCodeableConcept(t, "MedicationStatement", "category", element.getCategory(), -1);
    if (element.hasMedication())
      composeType(t, "MedicationStatement", "medication", element.getMedication(), -1);
    if (element.hasEffective())
      composeType(t, "MedicationStatement", "effective", element.getEffective(), -1);
    if (element.hasDateAssertedElement())
      composeDateTime(t, "MedicationStatement", "dateAsserted", element.getDateAssertedElement(), -1);
    if (element.hasInformationSource())
      composeReference(t, "MedicationStatement", "informationSource", element.getInformationSource(), -1);
    if (element.hasSubject())
      composeReference(t, "MedicationStatement", "subject", element.getSubject(), -1);
    for (int i = 0; i < element.getDerivedFrom().size(); i++)
      composeReference(t, "MedicationStatement", "derivedFrom", element.getDerivedFrom().get(i), i);
    if (element.hasTakenElement())
      composeEnum(t, "MedicationStatement", "taken", element.getTakenElement(), -1);
    for (int i = 0; i < element.getReasonNotTaken().size(); i++)
      composeCodeableConcept(t, "MedicationStatement", "reasonNotTaken", element.getReasonNotTaken().get(i), i);
    for (int i = 0; i < element.getReasonCode().size(); i++)
      composeCodeableConcept(t, "MedicationStatement", "reasonCode", element.getReasonCode().get(i), i);
    for (int i = 0; i < element.getReasonReference().size(); i++)
      composeReference(t, "MedicationStatement", "reasonReference", element.getReasonReference().get(i), i);
    for (int i = 0; i < element.getNote().size(); i++)
      composeAnnotation(t, "MedicationStatement", "note", element.getNote().get(i), i);
    for (int i = 0; i < element.getDosage().size(); i++)
      composeDosage(t, "MedicationStatement", "dosage", element.getDosage().get(i), i);
  }

  protected void composeMessageDefinition(Complex parent, String parentType, String name, MessageDefinition element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "MessageDefinition", name, element, index);
    if (element.hasUrlElement())
      composeUri(t, "MessageDefinition", "url", element.getUrlElement(), -1);
    if (element.hasIdentifier())
      composeIdentifier(t, "MessageDefinition", "identifier", element.getIdentifier(), -1);
    if (element.hasVersionElement())
      composeString(t, "MessageDefinition", "version", element.getVersionElement(), -1);
    if (element.hasNameElement())
      composeString(t, "MessageDefinition", "name", element.getNameElement(), -1);
    if (element.hasTitleElement())
      composeString(t, "MessageDefinition", "title", element.getTitleElement(), -1);
    if (element.hasStatusElement())
      composeEnum(t, "MessageDefinition", "status", element.getStatusElement(), -1);
    if (element.hasExperimentalElement())
      composeBoolean(t, "MessageDefinition", "experimental", element.getExperimentalElement(), -1);
    if (element.hasDateElement())
      composeDateTime(t, "MessageDefinition", "date", element.getDateElement(), -1);
    if (element.hasPublisherElement())
      composeString(t, "MessageDefinition", "publisher", element.getPublisherElement(), -1);
    for (int i = 0; i < element.getContact().size(); i++)
      composeContactDetail(t, "MessageDefinition", "contact", element.getContact().get(i), i);
    if (element.hasDescriptionElement())
      composeMarkdown(t, "MessageDefinition", "description", element.getDescriptionElement(), -1);
    for (int i = 0; i < element.getUseContext().size(); i++)
      composeUsageContext(t, "MessageDefinition", "useContext", element.getUseContext().get(i), i);
    for (int i = 0; i < element.getJurisdiction().size(); i++)
      composeCodeableConcept(t, "MessageDefinition", "jurisdiction", element.getJurisdiction().get(i), i);
    if (element.hasPurposeElement())
      composeMarkdown(t, "MessageDefinition", "purpose", element.getPurposeElement(), -1);
    if (element.hasCopyrightElement())
      composeMarkdown(t, "MessageDefinition", "copyright", element.getCopyrightElement(), -1);
    if (element.hasBase())
      composeReference(t, "MessageDefinition", "base", element.getBase(), -1);
    for (int i = 0; i < element.getParent().size(); i++)
      composeReference(t, "MessageDefinition", "parent", element.getParent().get(i), i);
    for (int i = 0; i < element.getReplaces().size(); i++)
      composeReference(t, "MessageDefinition", "replaces", element.getReplaces().get(i), i);
    if (element.hasEventElement())
      composeUri(t, "MessageDefinition", "event", element.getEventElement(), -1);
    if (element.hasCategoryElement())
      composeEnum(t, "MessageDefinition", "category", element.getCategoryElement(), -1);
    for (int i = 0; i < element.getFocus().size(); i++)
      composeMessageDefinitionMessageDefinitionFocusComponent(t, "MessageDefinition", "focus", element.getFocus().get(i), i);
    if (element.hasResponseRequiredElement())
      composeBoolean(t, "MessageDefinition", "responseRequired", element.getResponseRequiredElement(), -1);
    for (int i = 0; i < element.getAllowedResponse().size(); i++)
      composeMessageDefinitionMessageDefinitionAllowedResponseComponent(t, "MessageDefinition", "allowedResponse", element.getAllowedResponse().get(i), i);
  }

  protected void composeMessageDefinitionMessageDefinitionFocusComponent(Complex parent, String parentType, String name, MessageDefinition.MessageDefinitionFocusComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "focus", name, element, index);
    if (element.hasCodeElement())
      composeCode(t, "MessageDefinition", "code", element.getCodeElement(), -1);
    if (element.hasProfile())
      composeReference(t, "MessageDefinition", "profile", element.getProfile(), -1);
    if (element.hasMinElement())
      composeUnsignedInt(t, "MessageDefinition", "min", element.getMinElement(), -1);
    if (element.hasMaxElement())
      composeString(t, "MessageDefinition", "max", element.getMaxElement(), -1);
  }

  protected void composeMessageDefinitionMessageDefinitionAllowedResponseComponent(Complex parent, String parentType, String name, MessageDefinition.MessageDefinitionAllowedResponseComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "allowedResponse", name, element, index);
    if (element.hasMessage())
      composeReference(t, "MessageDefinition", "message", element.getMessage(), -1);
    if (element.hasSituationElement())
      composeMarkdown(t, "MessageDefinition", "situation", element.getSituationElement(), -1);
  }

  protected void composeMessageHeader(Complex parent, String parentType, String name, MessageHeader element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "MessageHeader", name, element, index);
    if (element.hasEvent())
      composeCoding(t, "MessageHeader", "event", element.getEvent(), -1);
    for (int i = 0; i < element.getDestination().size(); i++)
      composeMessageHeaderMessageDestinationComponent(t, "MessageHeader", "destination", element.getDestination().get(i), i);
    if (element.hasSender())
      composeReference(t, "MessageHeader", "sender", element.getSender(), -1);
    if (element.hasTimestampElement())
      composeInstant(t, "MessageHeader", "timestamp", element.getTimestampElement(), -1);
    if (element.hasEnterer())
      composeReference(t, "MessageHeader", "enterer", element.getEnterer(), -1);
    if (element.hasAuthor())
      composeReference(t, "MessageHeader", "author", element.getAuthor(), -1);
    if (element.hasSource())
      composeMessageHeaderMessageSourceComponent(t, "MessageHeader", "source", element.getSource(), -1);
    if (element.hasResponsible())
      composeReference(t, "MessageHeader", "responsible", element.getResponsible(), -1);
    if (element.hasReason())
      composeCodeableConcept(t, "MessageHeader", "reason", element.getReason(), -1);
    if (element.hasResponse())
      composeMessageHeaderMessageHeaderResponseComponent(t, "MessageHeader", "response", element.getResponse(), -1);
    for (int i = 0; i < element.getFocus().size(); i++)
      composeReference(t, "MessageHeader", "focus", element.getFocus().get(i), i);
    if (element.hasDefinitionElement())
      composeUri(t, "MessageHeader", "definition", element.getDefinitionElement(), -1);
  }

  protected void composeMessageHeaderMessageDestinationComponent(Complex parent, String parentType, String name, MessageHeader.MessageDestinationComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "destination", name, element, index);
    if (element.hasNameElement())
      composeString(t, "MessageHeader", "name", element.getNameElement(), -1);
    if (element.hasTarget())
      composeReference(t, "MessageHeader", "target", element.getTarget(), -1);
    if (element.hasEndpointElement())
      composeUri(t, "MessageHeader", "endpoint", element.getEndpointElement(), -1);
    if (element.hasReceiver())
      composeReference(t, "MessageHeader", "receiver", element.getReceiver(), -1);
  }

  protected void composeMessageHeaderMessageSourceComponent(Complex parent, String parentType, String name, MessageHeader.MessageSourceComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "source", name, element, index);
    if (element.hasNameElement())
      composeString(t, "MessageHeader", "name", element.getNameElement(), -1);
    if (element.hasSoftwareElement())
      composeString(t, "MessageHeader", "software", element.getSoftwareElement(), -1);
    if (element.hasVersionElement())
      composeString(t, "MessageHeader", "version", element.getVersionElement(), -1);
    if (element.hasContact())
      composeContactPoint(t, "MessageHeader", "contact", element.getContact(), -1);
    if (element.hasEndpointElement())
      composeUri(t, "MessageHeader", "endpoint", element.getEndpointElement(), -1);
  }

  protected void composeMessageHeaderMessageHeaderResponseComponent(Complex parent, String parentType, String name, MessageHeader.MessageHeaderResponseComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "response", name, element, index);
    if (element.hasIdentifierElement())
      composeId(t, "MessageHeader", "identifier", element.getIdentifierElement(), -1);
    if (element.hasCodeElement())
      composeEnum(t, "MessageHeader", "code", element.getCodeElement(), -1);
    if (element.hasDetails())
      composeReference(t, "MessageHeader", "details", element.getDetails(), -1);
  }

  protected void composeNamingSystem(Complex parent, String parentType, String name, NamingSystem element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "NamingSystem", name, element, index);
    if (element.hasNameElement())
      composeString(t, "NamingSystem", "name", element.getNameElement(), -1);
    if (element.hasStatusElement())
      composeEnum(t, "NamingSystem", "status", element.getStatusElement(), -1);
    if (element.hasKindElement())
      composeEnum(t, "NamingSystem", "kind", element.getKindElement(), -1);
    if (element.hasDateElement())
      composeDateTime(t, "NamingSystem", "date", element.getDateElement(), -1);
    if (element.hasPublisherElement())
      composeString(t, "NamingSystem", "publisher", element.getPublisherElement(), -1);
    for (int i = 0; i < element.getContact().size(); i++)
      composeContactDetail(t, "NamingSystem", "contact", element.getContact().get(i), i);
    if (element.hasResponsibleElement())
      composeString(t, "NamingSystem", "responsible", element.getResponsibleElement(), -1);
    if (element.hasType())
      composeCodeableConcept(t, "NamingSystem", "type", element.getType(), -1);
    if (element.hasDescriptionElement())
      composeMarkdown(t, "NamingSystem", "description", element.getDescriptionElement(), -1);
    for (int i = 0; i < element.getUseContext().size(); i++)
      composeUsageContext(t, "NamingSystem", "useContext", element.getUseContext().get(i), i);
    for (int i = 0; i < element.getJurisdiction().size(); i++)
      composeCodeableConcept(t, "NamingSystem", "jurisdiction", element.getJurisdiction().get(i), i);
    if (element.hasUsageElement())
      composeString(t, "NamingSystem", "usage", element.getUsageElement(), -1);
    for (int i = 0; i < element.getUniqueId().size(); i++)
      composeNamingSystemNamingSystemUniqueIdComponent(t, "NamingSystem", "uniqueId", element.getUniqueId().get(i), i);
    if (element.hasReplacedBy())
      composeReference(t, "NamingSystem", "replacedBy", element.getReplacedBy(), -1);
  }

  protected void composeNamingSystemNamingSystemUniqueIdComponent(Complex parent, String parentType, String name, NamingSystem.NamingSystemUniqueIdComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "uniqueId", name, element, index);
    if (element.hasTypeElement())
      composeEnum(t, "NamingSystem", "type", element.getTypeElement(), -1);
    if (element.hasValueElement())
      composeString(t, "NamingSystem", "value", element.getValueElement(), -1);
    if (element.hasPreferredElement())
      composeBoolean(t, "NamingSystem", "preferred", element.getPreferredElement(), -1);
    if (element.hasCommentElement())
      composeString(t, "NamingSystem", "comment", element.getCommentElement(), -1);
    if (element.hasPeriod())
      composePeriod(t, "NamingSystem", "period", element.getPeriod(), -1);
  }

  protected void composeNutritionOrder(Complex parent, String parentType, String name, NutritionOrder element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "NutritionOrder", name, element, index);
    for (int i = 0; i < element.getIdentifier().size(); i++)
      composeIdentifier(t, "NutritionOrder", "identifier", element.getIdentifier().get(i), i);
    if (element.hasStatusElement())
      composeEnum(t, "NutritionOrder", "status", element.getStatusElement(), -1);
    if (element.hasPatient())
      composeReference(t, "NutritionOrder", "patient", element.getPatient(), -1);
    if (element.hasEncounter())
      composeReference(t, "NutritionOrder", "encounter", element.getEncounter(), -1);
    if (element.hasDateTimeElement())
      composeDateTime(t, "NutritionOrder", "dateTime", element.getDateTimeElement(), -1);
    if (element.hasOrderer())
      composeReference(t, "NutritionOrder", "orderer", element.getOrderer(), -1);
    for (int i = 0; i < element.getAllergyIntolerance().size(); i++)
      composeReference(t, "NutritionOrder", "allergyIntolerance", element.getAllergyIntolerance().get(i), i);
    for (int i = 0; i < element.getFoodPreferenceModifier().size(); i++)
      composeCodeableConcept(t, "NutritionOrder", "foodPreferenceModifier", element.getFoodPreferenceModifier().get(i), i);
    for (int i = 0; i < element.getExcludeFoodModifier().size(); i++)
      composeCodeableConcept(t, "NutritionOrder", "excludeFoodModifier", element.getExcludeFoodModifier().get(i), i);
    if (element.hasOralDiet())
      composeNutritionOrderNutritionOrderOralDietComponent(t, "NutritionOrder", "oralDiet", element.getOralDiet(), -1);
    for (int i = 0; i < element.getSupplement().size(); i++)
      composeNutritionOrderNutritionOrderSupplementComponent(t, "NutritionOrder", "supplement", element.getSupplement().get(i), i);
    if (element.hasEnteralFormula())
      composeNutritionOrderNutritionOrderEnteralFormulaComponent(t, "NutritionOrder", "enteralFormula", element.getEnteralFormula(), -1);
  }

  protected void composeNutritionOrderNutritionOrderOralDietComponent(Complex parent, String parentType, String name, NutritionOrder.NutritionOrderOralDietComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "oralDiet", name, element, index);
    for (int i = 0; i < element.getType().size(); i++)
      composeCodeableConcept(t, "NutritionOrder", "type", element.getType().get(i), i);
    for (int i = 0; i < element.getSchedule().size(); i++)
      composeTiming(t, "NutritionOrder", "schedule", element.getSchedule().get(i), i);
    for (int i = 0; i < element.getNutrient().size(); i++)
      composeNutritionOrderNutritionOrderOralDietNutrientComponent(t, "NutritionOrder", "nutrient", element.getNutrient().get(i), i);
    for (int i = 0; i < element.getTexture().size(); i++)
      composeNutritionOrderNutritionOrderOralDietTextureComponent(t, "NutritionOrder", "texture", element.getTexture().get(i), i);
    for (int i = 0; i < element.getFluidConsistencyType().size(); i++)
      composeCodeableConcept(t, "NutritionOrder", "fluidConsistencyType", element.getFluidConsistencyType().get(i), i);
    if (element.hasInstructionElement())
      composeString(t, "NutritionOrder", "instruction", element.getInstructionElement(), -1);
  }

  protected void composeNutritionOrderNutritionOrderOralDietNutrientComponent(Complex parent, String parentType, String name, NutritionOrder.NutritionOrderOralDietNutrientComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "nutrient", name, element, index);
    if (element.hasModifier())
      composeCodeableConcept(t, "NutritionOrder", "modifier", element.getModifier(), -1);
    if (element.hasAmount())
      composeQuantity(t, "NutritionOrder", "amount", element.getAmount(), -1);
  }

  protected void composeNutritionOrderNutritionOrderOralDietTextureComponent(Complex parent, String parentType, String name, NutritionOrder.NutritionOrderOralDietTextureComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "texture", name, element, index);
    if (element.hasModifier())
      composeCodeableConcept(t, "NutritionOrder", "modifier", element.getModifier(), -1);
    if (element.hasFoodType())
      composeCodeableConcept(t, "NutritionOrder", "foodType", element.getFoodType(), -1);
  }

  protected void composeNutritionOrderNutritionOrderSupplementComponent(Complex parent, String parentType, String name, NutritionOrder.NutritionOrderSupplementComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "supplement", name, element, index);
    if (element.hasType())
      composeCodeableConcept(t, "NutritionOrder", "type", element.getType(), -1);
    if (element.hasProductNameElement())
      composeString(t, "NutritionOrder", "productName", element.getProductNameElement(), -1);
    for (int i = 0; i < element.getSchedule().size(); i++)
      composeTiming(t, "NutritionOrder", "schedule", element.getSchedule().get(i), i);
    if (element.hasQuantity())
      composeQuantity(t, "NutritionOrder", "quantity", element.getQuantity(), -1);
    if (element.hasInstructionElement())
      composeString(t, "NutritionOrder", "instruction", element.getInstructionElement(), -1);
  }

  protected void composeNutritionOrderNutritionOrderEnteralFormulaComponent(Complex parent, String parentType, String name, NutritionOrder.NutritionOrderEnteralFormulaComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "enteralFormula", name, element, index);
    if (element.hasBaseFormulaType())
      composeCodeableConcept(t, "NutritionOrder", "baseFormulaType", element.getBaseFormulaType(), -1);
    if (element.hasBaseFormulaProductNameElement())
      composeString(t, "NutritionOrder", "baseFormulaProductName", element.getBaseFormulaProductNameElement(), -1);
    if (element.hasAdditiveType())
      composeCodeableConcept(t, "NutritionOrder", "additiveType", element.getAdditiveType(), -1);
    if (element.hasAdditiveProductNameElement())
      composeString(t, "NutritionOrder", "additiveProductName", element.getAdditiveProductNameElement(), -1);
    if (element.hasCaloricDensity())
      composeQuantity(t, "NutritionOrder", "caloricDensity", element.getCaloricDensity(), -1);
    if (element.hasRouteofAdministration())
      composeCodeableConcept(t, "NutritionOrder", "routeofAdministration", element.getRouteofAdministration(), -1);
    for (int i = 0; i < element.getAdministration().size(); i++)
      composeNutritionOrderNutritionOrderEnteralFormulaAdministrationComponent(t, "NutritionOrder", "administration", element.getAdministration().get(i), i);
    if (element.hasMaxVolumeToDeliver())
      composeQuantity(t, "NutritionOrder", "maxVolumeToDeliver", element.getMaxVolumeToDeliver(), -1);
    if (element.hasAdministrationInstructionElement())
      composeString(t, "NutritionOrder", "administrationInstruction", element.getAdministrationInstructionElement(), -1);
  }

  protected void composeNutritionOrderNutritionOrderEnteralFormulaAdministrationComponent(Complex parent, String parentType, String name, NutritionOrder.NutritionOrderEnteralFormulaAdministrationComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "administration", name, element, index);
    if (element.hasSchedule())
      composeTiming(t, "NutritionOrder", "schedule", element.getSchedule(), -1);
    if (element.hasQuantity())
      composeQuantity(t, "NutritionOrder", "quantity", element.getQuantity(), -1);
    if (element.hasRate())
      composeType(t, "NutritionOrder", "rate", element.getRate(), -1);
  }

  protected void composeObservation(Complex parent, String parentType, String name, Observation element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "Observation", name, element, index);
    for (int i = 0; i < element.getIdentifier().size(); i++)
      composeIdentifier(t, "Observation", "identifier", element.getIdentifier().get(i), i);
    for (int i = 0; i < element.getBasedOn().size(); i++)
      composeReference(t, "Observation", "basedOn", element.getBasedOn().get(i), i);
    if (element.hasStatusElement())
      composeEnum(t, "Observation", "status", element.getStatusElement(), -1);
    for (int i = 0; i < element.getCategory().size(); i++)
      composeCodeableConcept(t, "Observation", "category", element.getCategory().get(i), i);
    if (element.hasCode())
      composeCodeableConcept(t, "Observation", "code", element.getCode(), -1);
    if (element.hasSubject())
      composeReference(t, "Observation", "subject", element.getSubject(), -1);
    if (element.hasContext())
      composeReference(t, "Observation", "context", element.getContext(), -1);
    if (element.hasEffective())
      composeType(t, "Observation", "effective", element.getEffective(), -1);
    if (element.hasIssuedElement())
      composeInstant(t, "Observation", "issued", element.getIssuedElement(), -1);
    for (int i = 0; i < element.getPerformer().size(); i++)
      composeReference(t, "Observation", "performer", element.getPerformer().get(i), i);
    if (element.hasValue())
      composeType(t, "Observation", "value", element.getValue(), -1);
    if (element.hasDataAbsentReason())
      composeCodeableConcept(t, "Observation", "dataAbsentReason", element.getDataAbsentReason(), -1);
    if (element.hasInterpretation())
      composeCodeableConcept(t, "Observation", "interpretation", element.getInterpretation(), -1);
    if (element.hasCommentElement())
      composeString(t, "Observation", "comment", element.getCommentElement(), -1);
    if (element.hasBodySite())
      composeCodeableConcept(t, "Observation", "bodySite", element.getBodySite(), -1);
    if (element.hasMethod())
      composeCodeableConcept(t, "Observation", "method", element.getMethod(), -1);
    if (element.hasSpecimen())
      composeReference(t, "Observation", "specimen", element.getSpecimen(), -1);
    if (element.hasDevice())
      composeReference(t, "Observation", "device", element.getDevice(), -1);
    for (int i = 0; i < element.getReferenceRange().size(); i++)
      composeObservationObservationReferenceRangeComponent(t, "Observation", "referenceRange", element.getReferenceRange().get(i), i);
    for (int i = 0; i < element.getRelated().size(); i++)
      composeObservationObservationRelatedComponent(t, "Observation", "related", element.getRelated().get(i), i);
    for (int i = 0; i < element.getComponent().size(); i++)
      composeObservationObservationComponentComponent(t, "Observation", "component", element.getComponent().get(i), i);
  }

  protected void composeObservationObservationReferenceRangeComponent(Complex parent, String parentType, String name, Observation.ObservationReferenceRangeComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "referenceRange", name, element, index);
    if (element.hasLow())
      composeQuantity(t, "Observation", "low", element.getLow(), -1);
    if (element.hasHigh())
      composeQuantity(t, "Observation", "high", element.getHigh(), -1);
    if (element.hasType())
      composeCodeableConcept(t, "Observation", "type", element.getType(), -1);
    for (int i = 0; i < element.getAppliesTo().size(); i++)
      composeCodeableConcept(t, "Observation", "appliesTo", element.getAppliesTo().get(i), i);
    if (element.hasAge())
      composeRange(t, "Observation", "age", element.getAge(), -1);
    if (element.hasTextElement())
      composeString(t, "Observation", "text", element.getTextElement(), -1);
  }

  protected void composeObservationObservationRelatedComponent(Complex parent, String parentType, String name, Observation.ObservationRelatedComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "related", name, element, index);
    if (element.hasTypeElement())
      composeEnum(t, "Observation", "type", element.getTypeElement(), -1);
    if (element.hasTarget())
      composeReference(t, "Observation", "target", element.getTarget(), -1);
  }

  protected void composeObservationObservationComponentComponent(Complex parent, String parentType, String name, Observation.ObservationComponentComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "component", name, element, index);
    if (element.hasCode())
      composeCodeableConcept(t, "Observation", "code", element.getCode(), -1);
    if (element.hasValue())
      composeType(t, "Observation", "value", element.getValue(), -1);
    if (element.hasDataAbsentReason())
      composeCodeableConcept(t, "Observation", "dataAbsentReason", element.getDataAbsentReason(), -1);
    if (element.hasInterpretation())
      composeCodeableConcept(t, "Observation", "interpretation", element.getInterpretation(), -1);
    for (int i = 0; i < element.getReferenceRange().size(); i++)
      composeObservationObservationReferenceRangeComponent(t, "Observation", "referenceRange", element.getReferenceRange().get(i), i);
  }

  protected void composeOperationDefinition(Complex parent, String parentType, String name, OperationDefinition element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "OperationDefinition", name, element, index);
    if (element.hasUrlElement())
      composeUri(t, "OperationDefinition", "url", element.getUrlElement(), -1);
    if (element.hasVersionElement())
      composeString(t, "OperationDefinition", "version", element.getVersionElement(), -1);
    if (element.hasNameElement())
      composeString(t, "OperationDefinition", "name", element.getNameElement(), -1);
    if (element.hasStatusElement())
      composeEnum(t, "OperationDefinition", "status", element.getStatusElement(), -1);
    if (element.hasKindElement())
      composeEnum(t, "OperationDefinition", "kind", element.getKindElement(), -1);
    if (element.hasExperimentalElement())
      composeBoolean(t, "OperationDefinition", "experimental", element.getExperimentalElement(), -1);
    if (element.hasDateElement())
      composeDateTime(t, "OperationDefinition", "date", element.getDateElement(), -1);
    if (element.hasPublisherElement())
      composeString(t, "OperationDefinition", "publisher", element.getPublisherElement(), -1);
    for (int i = 0; i < element.getContact().size(); i++)
      composeContactDetail(t, "OperationDefinition", "contact", element.getContact().get(i), i);
    if (element.hasDescriptionElement())
      composeMarkdown(t, "OperationDefinition", "description", element.getDescriptionElement(), -1);
    for (int i = 0; i < element.getUseContext().size(); i++)
      composeUsageContext(t, "OperationDefinition", "useContext", element.getUseContext().get(i), i);
    for (int i = 0; i < element.getJurisdiction().size(); i++)
      composeCodeableConcept(t, "OperationDefinition", "jurisdiction", element.getJurisdiction().get(i), i);
    if (element.hasPurposeElement())
      composeMarkdown(t, "OperationDefinition", "purpose", element.getPurposeElement(), -1);
    if (element.hasIdempotentElement())
      composeBoolean(t, "OperationDefinition", "idempotent", element.getIdempotentElement(), -1);
    if (element.hasCodeElement())
      composeCode(t, "OperationDefinition", "code", element.getCodeElement(), -1);
    if (element.hasCommentElement())
      composeString(t, "OperationDefinition", "comment", element.getCommentElement(), -1);
    if (element.hasBase())
      composeReference(t, "OperationDefinition", "base", element.getBase(), -1);
    for (int i = 0; i < element.getResource().size(); i++)
      composeCode(t, "OperationDefinition", "resource", element.getResource().get(i), i);
    if (element.hasSystemElement())
      composeBoolean(t, "OperationDefinition", "system", element.getSystemElement(), -1);
    if (element.hasTypeElement())
      composeBoolean(t, "OperationDefinition", "type", element.getTypeElement(), -1);
    if (element.hasInstanceElement())
      composeBoolean(t, "OperationDefinition", "instance", element.getInstanceElement(), -1);
    for (int i = 0; i < element.getParameter().size(); i++)
      composeOperationDefinitionOperationDefinitionParameterComponent(t, "OperationDefinition", "parameter", element.getParameter().get(i), i);
    for (int i = 0; i < element.getOverload().size(); i++)
      composeOperationDefinitionOperationDefinitionOverloadComponent(t, "OperationDefinition", "overload", element.getOverload().get(i), i);
  }

  protected void composeOperationDefinitionOperationDefinitionParameterComponent(Complex parent, String parentType, String name, OperationDefinition.OperationDefinitionParameterComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "parameter", name, element, index);
    if (element.hasNameElement())
      composeCode(t, "OperationDefinition", "name", element.getNameElement(), -1);
    if (element.hasUseElement())
      composeEnum(t, "OperationDefinition", "use", element.getUseElement(), -1);
    if (element.hasMinElement())
      composeInteger(t, "OperationDefinition", "min", element.getMinElement(), -1);
    if (element.hasMaxElement())
      composeString(t, "OperationDefinition", "max", element.getMaxElement(), -1);
    if (element.hasDocumentationElement())
      composeString(t, "OperationDefinition", "documentation", element.getDocumentationElement(), -1);
    if (element.hasTypeElement())
      composeCode(t, "OperationDefinition", "type", element.getTypeElement(), -1);
    if (element.hasSearchTypeElement())
      composeEnum(t, "OperationDefinition", "searchType", element.getSearchTypeElement(), -1);
    if (element.hasProfile())
      composeReference(t, "OperationDefinition", "profile", element.getProfile(), -1);
    if (element.hasBinding())
      composeOperationDefinitionOperationDefinitionParameterBindingComponent(t, "OperationDefinition", "binding", element.getBinding(), -1);
    for (int i = 0; i < element.getPart().size(); i++)
      composeOperationDefinitionOperationDefinitionParameterComponent(t, "OperationDefinition", "part", element.getPart().get(i), i);
  }

  protected void composeOperationDefinitionOperationDefinitionParameterBindingComponent(Complex parent, String parentType, String name, OperationDefinition.OperationDefinitionParameterBindingComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "binding", name, element, index);
    if (element.hasStrengthElement())
      composeEnum(t, "OperationDefinition", "strength", element.getStrengthElement(), -1);
    if (element.hasValueSet())
      composeType(t, "OperationDefinition", "valueSet", element.getValueSet(), -1);
  }

  protected void composeOperationDefinitionOperationDefinitionOverloadComponent(Complex parent, String parentType, String name, OperationDefinition.OperationDefinitionOverloadComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "overload", name, element, index);
    for (int i = 0; i < element.getParameterName().size(); i++)
      composeString(t, "OperationDefinition", "parameterName", element.getParameterName().get(i), i);
    if (element.hasCommentElement())
      composeString(t, "OperationDefinition", "comment", element.getCommentElement(), -1);
  }

  protected void composeOperationOutcome(Complex parent, String parentType, String name, OperationOutcome element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "OperationOutcome", name, element, index);
    for (int i = 0; i < element.getIssue().size(); i++)
      composeOperationOutcomeOperationOutcomeIssueComponent(t, "OperationOutcome", "issue", element.getIssue().get(i), i);
  }

  protected void composeOperationOutcomeOperationOutcomeIssueComponent(Complex parent, String parentType, String name, OperationOutcome.OperationOutcomeIssueComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "issue", name, element, index);
    if (element.hasSeverityElement())
      composeEnum(t, "OperationOutcome", "severity", element.getSeverityElement(), -1);
    if (element.hasCodeElement())
      composeEnum(t, "OperationOutcome", "code", element.getCodeElement(), -1);
    if (element.hasDetails())
      composeCodeableConcept(t, "OperationOutcome", "details", element.getDetails(), -1);
    if (element.hasDiagnosticsElement())
      composeString(t, "OperationOutcome", "diagnostics", element.getDiagnosticsElement(), -1);
    for (int i = 0; i < element.getLocation().size(); i++)
      composeString(t, "OperationOutcome", "location", element.getLocation().get(i), i);
    for (int i = 0; i < element.getExpression().size(); i++)
      composeString(t, "OperationOutcome", "expression", element.getExpression().get(i), i);
  }

  protected void composeOrganization(Complex parent, String parentType, String name, Organization element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "Organization", name, element, index);
    for (int i = 0; i < element.getIdentifier().size(); i++)
      composeIdentifier(t, "Organization", "identifier", element.getIdentifier().get(i), i);
    if (element.hasActiveElement())
      composeBoolean(t, "Organization", "active", element.getActiveElement(), -1);
    for (int i = 0; i < element.getType().size(); i++)
      composeCodeableConcept(t, "Organization", "type", element.getType().get(i), i);
    if (element.hasNameElement())
      composeString(t, "Organization", "name", element.getNameElement(), -1);
    for (int i = 0; i < element.getAlias().size(); i++)
      composeString(t, "Organization", "alias", element.getAlias().get(i), i);
    for (int i = 0; i < element.getTelecom().size(); i++)
      composeContactPoint(t, "Organization", "telecom", element.getTelecom().get(i), i);
    for (int i = 0; i < element.getAddress().size(); i++)
      composeAddress(t, "Organization", "address", element.getAddress().get(i), i);
    if (element.hasPartOf())
      composeReference(t, "Organization", "partOf", element.getPartOf(), -1);
    for (int i = 0; i < element.getContact().size(); i++)
      composeOrganizationOrganizationContactComponent(t, "Organization", "contact", element.getContact().get(i), i);
    for (int i = 0; i < element.getEndpoint().size(); i++)
      composeReference(t, "Organization", "endpoint", element.getEndpoint().get(i), i);
  }

  protected void composeOrganizationOrganizationContactComponent(Complex parent, String parentType, String name, Organization.OrganizationContactComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "contact", name, element, index);
    if (element.hasPurpose())
      composeCodeableConcept(t, "Organization", "purpose", element.getPurpose(), -1);
    if (element.hasName())
      composeHumanName(t, "Organization", "name", element.getName(), -1);
    for (int i = 0; i < element.getTelecom().size(); i++)
      composeContactPoint(t, "Organization", "telecom", element.getTelecom().get(i), i);
    if (element.hasAddress())
      composeAddress(t, "Organization", "address", element.getAddress(), -1);
  }

  protected void composePatient(Complex parent, String parentType, String name, Patient element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "Patient", name, element, index);
    for (int i = 0; i < element.getIdentifier().size(); i++)
      composeIdentifier(t, "Patient", "identifier", element.getIdentifier().get(i), i);
    if (element.hasActiveElement())
      composeBoolean(t, "Patient", "active", element.getActiveElement(), -1);
    for (int i = 0; i < element.getName().size(); i++)
      composeHumanName(t, "Patient", "name", element.getName().get(i), i);
    for (int i = 0; i < element.getTelecom().size(); i++)
      composeContactPoint(t, "Patient", "telecom", element.getTelecom().get(i), i);
    if (element.hasGenderElement())
      composeEnum(t, "Patient", "gender", element.getGenderElement(), -1);
    if (element.hasBirthDateElement())
      composeDate(t, "Patient", "birthDate", element.getBirthDateElement(), -1);
    if (element.hasDeceased())
      composeType(t, "Patient", "deceased", element.getDeceased(), -1);
    for (int i = 0; i < element.getAddress().size(); i++)
      composeAddress(t, "Patient", "address", element.getAddress().get(i), i);
    if (element.hasMaritalStatus())
      composeCodeableConcept(t, "Patient", "maritalStatus", element.getMaritalStatus(), -1);
    if (element.hasMultipleBirth())
      composeType(t, "Patient", "multipleBirth", element.getMultipleBirth(), -1);
    for (int i = 0; i < element.getPhoto().size(); i++)
      composeAttachment(t, "Patient", "photo", element.getPhoto().get(i), i);
    for (int i = 0; i < element.getContact().size(); i++)
      composePatientContactComponent(t, "Patient", "contact", element.getContact().get(i), i);
    if (element.hasAnimal())
      composePatientAnimalComponent(t, "Patient", "animal", element.getAnimal(), -1);
    for (int i = 0; i < element.getCommunication().size(); i++)
      composePatientPatientCommunicationComponent(t, "Patient", "communication", element.getCommunication().get(i), i);
    for (int i = 0; i < element.getGeneralPractitioner().size(); i++)
      composeReference(t, "Patient", "generalPractitioner", element.getGeneralPractitioner().get(i), i);
    if (element.hasManagingOrganization())
      composeReference(t, "Patient", "managingOrganization", element.getManagingOrganization(), -1);
    for (int i = 0; i < element.getLink().size(); i++)
      composePatientPatientLinkComponent(t, "Patient", "link", element.getLink().get(i), i);
  }

  protected void composePatientContactComponent(Complex parent, String parentType, String name, Patient.ContactComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "contact", name, element, index);
    for (int i = 0; i < element.getRelationship().size(); i++)
      composeCodeableConcept(t, "Patient", "relationship", element.getRelationship().get(i), i);
    if (element.hasName())
      composeHumanName(t, "Patient", "name", element.getName(), -1);
    for (int i = 0; i < element.getTelecom().size(); i++)
      composeContactPoint(t, "Patient", "telecom", element.getTelecom().get(i), i);
    if (element.hasAddress())
      composeAddress(t, "Patient", "address", element.getAddress(), -1);
    if (element.hasGenderElement())
      composeEnum(t, "Patient", "gender", element.getGenderElement(), -1);
    if (element.hasOrganization())
      composeReference(t, "Patient", "organization", element.getOrganization(), -1);
    if (element.hasPeriod())
      composePeriod(t, "Patient", "period", element.getPeriod(), -1);
  }

  protected void composePatientAnimalComponent(Complex parent, String parentType, String name, Patient.AnimalComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "animal", name, element, index);
    if (element.hasSpecies())
      composeCodeableConcept(t, "Patient", "species", element.getSpecies(), -1);
    if (element.hasBreed())
      composeCodeableConcept(t, "Patient", "breed", element.getBreed(), -1);
    if (element.hasGenderStatus())
      composeCodeableConcept(t, "Patient", "genderStatus", element.getGenderStatus(), -1);
  }

  protected void composePatientPatientCommunicationComponent(Complex parent, String parentType, String name, Patient.PatientCommunicationComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "communication", name, element, index);
    if (element.hasLanguage())
      composeCodeableConcept(t, "Patient", "language", element.getLanguage(), -1);
    if (element.hasPreferredElement())
      composeBoolean(t, "Patient", "preferred", element.getPreferredElement(), -1);
  }

  protected void composePatientPatientLinkComponent(Complex parent, String parentType, String name, Patient.PatientLinkComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "link", name, element, index);
    if (element.hasOther())
      composeReference(t, "Patient", "other", element.getOther(), -1);
    if (element.hasTypeElement())
      composeEnum(t, "Patient", "type", element.getTypeElement(), -1);
  }

  protected void composePaymentNotice(Complex parent, String parentType, String name, PaymentNotice element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "PaymentNotice", name, element, index);
    for (int i = 0; i < element.getIdentifier().size(); i++)
      composeIdentifier(t, "PaymentNotice", "identifier", element.getIdentifier().get(i), i);
    if (element.hasStatusElement())
      composeEnum(t, "PaymentNotice", "status", element.getStatusElement(), -1);
    if (element.hasRequest())
      composeReference(t, "PaymentNotice", "request", element.getRequest(), -1);
    if (element.hasResponse())
      composeReference(t, "PaymentNotice", "response", element.getResponse(), -1);
    if (element.hasStatusDateElement())
      composeDate(t, "PaymentNotice", "statusDate", element.getStatusDateElement(), -1);
    if (element.hasCreatedElement())
      composeDateTime(t, "PaymentNotice", "created", element.getCreatedElement(), -1);
    if (element.hasTarget())
      composeReference(t, "PaymentNotice", "target", element.getTarget(), -1);
    if (element.hasProvider())
      composeReference(t, "PaymentNotice", "provider", element.getProvider(), -1);
    if (element.hasOrganization())
      composeReference(t, "PaymentNotice", "organization", element.getOrganization(), -1);
    if (element.hasPaymentStatus())
      composeCodeableConcept(t, "PaymentNotice", "paymentStatus", element.getPaymentStatus(), -1);
  }

  protected void composePaymentReconciliation(Complex parent, String parentType, String name, PaymentReconciliation element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "PaymentReconciliation", name, element, index);
    for (int i = 0; i < element.getIdentifier().size(); i++)
      composeIdentifier(t, "PaymentReconciliation", "identifier", element.getIdentifier().get(i), i);
    if (element.hasStatusElement())
      composeEnum(t, "PaymentReconciliation", "status", element.getStatusElement(), -1);
    if (element.hasPeriod())
      composePeriod(t, "PaymentReconciliation", "period", element.getPeriod(), -1);
    if (element.hasCreatedElement())
      composeDateTime(t, "PaymentReconciliation", "created", element.getCreatedElement(), -1);
    if (element.hasOrganization())
      composeReference(t, "PaymentReconciliation", "organization", element.getOrganization(), -1);
    if (element.hasRequest())
      composeReference(t, "PaymentReconciliation", "request", element.getRequest(), -1);
    if (element.hasOutcomeElement())
      composeEnum(t, "PaymentReconciliation", "outcome", element.getOutcomeElement(), -1);
    if (element.hasDispositionElement())
      composeString(t, "PaymentReconciliation", "disposition", element.getDispositionElement(), -1);
    if (element.hasRequestProvider())
      composeReference(t, "PaymentReconciliation", "requestProvider", element.getRequestProvider(), -1);
    if (element.hasRequestOrganization())
      composeReference(t, "PaymentReconciliation", "requestOrganization", element.getRequestOrganization(), -1);
    for (int i = 0; i < element.getDetail().size(); i++)
      composePaymentReconciliationDetailsComponent(t, "PaymentReconciliation", "detail", element.getDetail().get(i), i);
    if (element.hasForm())
      composeCodeableConcept(t, "PaymentReconciliation", "form", element.getForm(), -1);
    if (element.hasTotal())
      composeMoney(t, "PaymentReconciliation", "total", element.getTotal(), -1);
    for (int i = 0; i < element.getProcessNote().size(); i++)
      composePaymentReconciliationNotesComponent(t, "PaymentReconciliation", "processNote", element.getProcessNote().get(i), i);
  }

  protected void composePaymentReconciliationDetailsComponent(Complex parent, String parentType, String name, PaymentReconciliation.DetailsComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "detail", name, element, index);
    if (element.hasType())
      composeCodeableConcept(t, "PaymentReconciliation", "type", element.getType(), -1);
    if (element.hasRequest())
      composeReference(t, "PaymentReconciliation", "request", element.getRequest(), -1);
    if (element.hasResponse())
      composeReference(t, "PaymentReconciliation", "response", element.getResponse(), -1);
    if (element.hasSubmitter())
      composeReference(t, "PaymentReconciliation", "submitter", element.getSubmitter(), -1);
    if (element.hasPayee())
      composeReference(t, "PaymentReconciliation", "payee", element.getPayee(), -1);
    if (element.hasDateElement())
      composeDate(t, "PaymentReconciliation", "date", element.getDateElement(), -1);
    if (element.hasAmount())
      composeMoney(t, "PaymentReconciliation", "amount", element.getAmount(), -1);
  }

  protected void composePaymentReconciliationNotesComponent(Complex parent, String parentType, String name, PaymentReconciliation.NotesComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "processNote", name, element, index);
    if (element.hasTypeElement())
      composeEnum(t, "PaymentReconciliation", "type", element.getTypeElement(), -1);
    if (element.hasTextElement())
      composeString(t, "PaymentReconciliation", "text", element.getTextElement(), -1);
  }

  protected void composePerson(Complex parent, String parentType, String name, Person element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "Person", name, element, index);
    for (int i = 0; i < element.getIdentifier().size(); i++)
      composeIdentifier(t, "Person", "identifier", element.getIdentifier().get(i), i);
    for (int i = 0; i < element.getName().size(); i++)
      composeHumanName(t, "Person", "name", element.getName().get(i), i);
    for (int i = 0; i < element.getTelecom().size(); i++)
      composeContactPoint(t, "Person", "telecom", element.getTelecom().get(i), i);
    if (element.hasGenderElement())
      composeEnum(t, "Person", "gender", element.getGenderElement(), -1);
    if (element.hasBirthDateElement())
      composeDate(t, "Person", "birthDate", element.getBirthDateElement(), -1);
    for (int i = 0; i < element.getAddress().size(); i++)
      composeAddress(t, "Person", "address", element.getAddress().get(i), i);
    if (element.hasPhoto())
      composeAttachment(t, "Person", "photo", element.getPhoto(), -1);
    if (element.hasManagingOrganization())
      composeReference(t, "Person", "managingOrganization", element.getManagingOrganization(), -1);
    if (element.hasActiveElement())
      composeBoolean(t, "Person", "active", element.getActiveElement(), -1);
    for (int i = 0; i < element.getLink().size(); i++)
      composePersonPersonLinkComponent(t, "Person", "link", element.getLink().get(i), i);
  }

  protected void composePersonPersonLinkComponent(Complex parent, String parentType, String name, Person.PersonLinkComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "link", name, element, index);
    if (element.hasTarget())
      composeReference(t, "Person", "target", element.getTarget(), -1);
    if (element.hasAssuranceElement())
      composeEnum(t, "Person", "assurance", element.getAssuranceElement(), -1);
  }

  protected void composePlanDefinition(Complex parent, String parentType, String name, PlanDefinition element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "PlanDefinition", name, element, index);
    if (element.hasUrlElement())
      composeUri(t, "PlanDefinition", "url", element.getUrlElement(), -1);
    for (int i = 0; i < element.getIdentifier().size(); i++)
      composeIdentifier(t, "PlanDefinition", "identifier", element.getIdentifier().get(i), i);
    if (element.hasVersionElement())
      composeString(t, "PlanDefinition", "version", element.getVersionElement(), -1);
    if (element.hasNameElement())
      composeString(t, "PlanDefinition", "name", element.getNameElement(), -1);
    if (element.hasTitleElement())
      composeString(t, "PlanDefinition", "title", element.getTitleElement(), -1);
    if (element.hasType())
      composeCodeableConcept(t, "PlanDefinition", "type", element.getType(), -1);
    if (element.hasStatusElement())
      composeEnum(t, "PlanDefinition", "status", element.getStatusElement(), -1);
    if (element.hasExperimentalElement())
      composeBoolean(t, "PlanDefinition", "experimental", element.getExperimentalElement(), -1);
    if (element.hasDateElement())
      composeDateTime(t, "PlanDefinition", "date", element.getDateElement(), -1);
    if (element.hasPublisherElement())
      composeString(t, "PlanDefinition", "publisher", element.getPublisherElement(), -1);
    if (element.hasDescriptionElement())
      composeMarkdown(t, "PlanDefinition", "description", element.getDescriptionElement(), -1);
    if (element.hasPurposeElement())
      composeMarkdown(t, "PlanDefinition", "purpose", element.getPurposeElement(), -1);
    if (element.hasUsageElement())
      composeString(t, "PlanDefinition", "usage", element.getUsageElement(), -1);
    if (element.hasApprovalDateElement())
      composeDate(t, "PlanDefinition", "approvalDate", element.getApprovalDateElement(), -1);
    if (element.hasLastReviewDateElement())
      composeDate(t, "PlanDefinition", "lastReviewDate", element.getLastReviewDateElement(), -1);
    if (element.hasEffectivePeriod())
      composePeriod(t, "PlanDefinition", "effectivePeriod", element.getEffectivePeriod(), -1);
    for (int i = 0; i < element.getUseContext().size(); i++)
      composeUsageContext(t, "PlanDefinition", "useContext", element.getUseContext().get(i), i);
    for (int i = 0; i < element.getJurisdiction().size(); i++)
      composeCodeableConcept(t, "PlanDefinition", "jurisdiction", element.getJurisdiction().get(i), i);
    for (int i = 0; i < element.getTopic().size(); i++)
      composeCodeableConcept(t, "PlanDefinition", "topic", element.getTopic().get(i), i);
    for (int i = 0; i < element.getContributor().size(); i++)
      composeContributor(t, "PlanDefinition", "contributor", element.getContributor().get(i), i);
    for (int i = 0; i < element.getContact().size(); i++)
      composeContactDetail(t, "PlanDefinition", "contact", element.getContact().get(i), i);
    if (element.hasCopyrightElement())
      composeMarkdown(t, "PlanDefinition", "copyright", element.getCopyrightElement(), -1);
    for (int i = 0; i < element.getRelatedArtifact().size(); i++)
      composeRelatedArtifact(t, "PlanDefinition", "relatedArtifact", element.getRelatedArtifact().get(i), i);
    for (int i = 0; i < element.getLibrary().size(); i++)
      composeReference(t, "PlanDefinition", "library", element.getLibrary().get(i), i);
    for (int i = 0; i < element.getGoal().size(); i++)
      composePlanDefinitionPlanDefinitionGoalComponent(t, "PlanDefinition", "goal", element.getGoal().get(i), i);
    for (int i = 0; i < element.getAction().size(); i++)
      composePlanDefinitionPlanDefinitionActionComponent(t, "PlanDefinition", "action", element.getAction().get(i), i);
  }

  protected void composePlanDefinitionPlanDefinitionGoalComponent(Complex parent, String parentType, String name, PlanDefinition.PlanDefinitionGoalComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "goal", name, element, index);
    if (element.hasCategory())
      composeCodeableConcept(t, "PlanDefinition", "category", element.getCategory(), -1);
    if (element.hasDescription())
      composeCodeableConcept(t, "PlanDefinition", "description", element.getDescription(), -1);
    if (element.hasPriority())
      composeCodeableConcept(t, "PlanDefinition", "priority", element.getPriority(), -1);
    if (element.hasStart())
      composeCodeableConcept(t, "PlanDefinition", "start", element.getStart(), -1);
    for (int i = 0; i < element.getAddresses().size(); i++)
      composeCodeableConcept(t, "PlanDefinition", "addresses", element.getAddresses().get(i), i);
    for (int i = 0; i < element.getDocumentation().size(); i++)
      composeRelatedArtifact(t, "PlanDefinition", "documentation", element.getDocumentation().get(i), i);
    for (int i = 0; i < element.getTarget().size(); i++)
      composePlanDefinitionPlanDefinitionGoalTargetComponent(t, "PlanDefinition", "target", element.getTarget().get(i), i);
  }

  protected void composePlanDefinitionPlanDefinitionGoalTargetComponent(Complex parent, String parentType, String name, PlanDefinition.PlanDefinitionGoalTargetComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "target", name, element, index);
    if (element.hasMeasure())
      composeCodeableConcept(t, "PlanDefinition", "measure", element.getMeasure(), -1);
    if (element.hasDetail())
      composeType(t, "PlanDefinition", "detail", element.getDetail(), -1);
    if (element.hasDue())
      composeDuration(t, "PlanDefinition", "due", element.getDue(), -1);
  }

  protected void composePlanDefinitionPlanDefinitionActionComponent(Complex parent, String parentType, String name, PlanDefinition.PlanDefinitionActionComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "action", name, element, index);
    if (element.hasPrefixElement())
      composeString(t, "PlanDefinition", "prefix", element.getPrefixElement(), -1);
    if (element.hasTitleElement())
      composeString(t, "PlanDefinition", "title", element.getTitleElement(), -1);
    if (element.hasDescriptionElement())
      composeString(t, "PlanDefinition", "description", element.getDescriptionElement(), -1);
    if (element.hasTextEquivalentElement())
      composeString(t, "PlanDefinition", "textEquivalent", element.getTextEquivalentElement(), -1);
    for (int i = 0; i < element.getCode().size(); i++)
      composeCodeableConcept(t, "PlanDefinition", "code", element.getCode().get(i), i);
    for (int i = 0; i < element.getReason().size(); i++)
      composeCodeableConcept(t, "PlanDefinition", "reason", element.getReason().get(i), i);
    for (int i = 0; i < element.getDocumentation().size(); i++)
      composeRelatedArtifact(t, "PlanDefinition", "documentation", element.getDocumentation().get(i), i);
    for (int i = 0; i < element.getGoalId().size(); i++)
      composeId(t, "PlanDefinition", "goalId", element.getGoalId().get(i), i);
    for (int i = 0; i < element.getTriggerDefinition().size(); i++)
      composeTriggerDefinition(t, "PlanDefinition", "triggerDefinition", element.getTriggerDefinition().get(i), i);
    for (int i = 0; i < element.getCondition().size(); i++)
      composePlanDefinitionPlanDefinitionActionConditionComponent(t, "PlanDefinition", "condition", element.getCondition().get(i), i);
    for (int i = 0; i < element.getInput().size(); i++)
      composeDataRequirement(t, "PlanDefinition", "input", element.getInput().get(i), i);
    for (int i = 0; i < element.getOutput().size(); i++)
      composeDataRequirement(t, "PlanDefinition", "output", element.getOutput().get(i), i);
    for (int i = 0; i < element.getRelatedAction().size(); i++)
      composePlanDefinitionPlanDefinitionActionRelatedActionComponent(t, "PlanDefinition", "relatedAction", element.getRelatedAction().get(i), i);
    if (element.hasTiming())
      composeType(t, "PlanDefinition", "timing", element.getTiming(), -1);
    for (int i = 0; i < element.getParticipant().size(); i++)
      composePlanDefinitionPlanDefinitionActionParticipantComponent(t, "PlanDefinition", "participant", element.getParticipant().get(i), i);
    if (element.hasType())
      composeCoding(t, "PlanDefinition", "type", element.getType(), -1);
    if (element.hasGroupingBehaviorElement())
      composeEnum(t, "PlanDefinition", "groupingBehavior", element.getGroupingBehaviorElement(), -1);
    if (element.hasSelectionBehaviorElement())
      composeEnum(t, "PlanDefinition", "selectionBehavior", element.getSelectionBehaviorElement(), -1);
    if (element.hasRequiredBehaviorElement())
      composeEnum(t, "PlanDefinition", "requiredBehavior", element.getRequiredBehaviorElement(), -1);
    if (element.hasPrecheckBehaviorElement())
      composeEnum(t, "PlanDefinition", "precheckBehavior", element.getPrecheckBehaviorElement(), -1);
    if (element.hasCardinalityBehaviorElement())
      composeEnum(t, "PlanDefinition", "cardinalityBehavior", element.getCardinalityBehaviorElement(), -1);
    if (element.hasDefinition())
      composeReference(t, "PlanDefinition", "definition", element.getDefinition(), -1);
    if (element.hasTransform())
      composeReference(t, "PlanDefinition", "transform", element.getTransform(), -1);
    for (int i = 0; i < element.getDynamicValue().size(); i++)
      composePlanDefinitionPlanDefinitionActionDynamicValueComponent(t, "PlanDefinition", "dynamicValue", element.getDynamicValue().get(i), i);
    for (int i = 0; i < element.getAction().size(); i++)
      composePlanDefinitionPlanDefinitionActionComponent(t, "PlanDefinition", "action", element.getAction().get(i), i);
  }

  protected void composePlanDefinitionPlanDefinitionActionConditionComponent(Complex parent, String parentType, String name, PlanDefinition.PlanDefinitionActionConditionComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "condition", name, element, index);
    if (element.hasKindElement())
      composeEnum(t, "PlanDefinition", "kind", element.getKindElement(), -1);
    if (element.hasDescriptionElement())
      composeString(t, "PlanDefinition", "description", element.getDescriptionElement(), -1);
    if (element.hasLanguageElement())
      composeString(t, "PlanDefinition", "language", element.getLanguageElement(), -1);
    if (element.hasExpressionElement())
      composeString(t, "PlanDefinition", "expression", element.getExpressionElement(), -1);
  }

  protected void composePlanDefinitionPlanDefinitionActionRelatedActionComponent(Complex parent, String parentType, String name, PlanDefinition.PlanDefinitionActionRelatedActionComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "relatedAction", name, element, index);
    if (element.hasActionIdElement())
      composeId(t, "PlanDefinition", "actionId", element.getActionIdElement(), -1);
    if (element.hasRelationshipElement())
      composeEnum(t, "PlanDefinition", "relationship", element.getRelationshipElement(), -1);
    if (element.hasOffset())
      composeType(t, "PlanDefinition", "offset", element.getOffset(), -1);
  }

  protected void composePlanDefinitionPlanDefinitionActionParticipantComponent(Complex parent, String parentType, String name, PlanDefinition.PlanDefinitionActionParticipantComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "participant", name, element, index);
    if (element.hasTypeElement())
      composeEnum(t, "PlanDefinition", "type", element.getTypeElement(), -1);
    if (element.hasRole())
      composeCodeableConcept(t, "PlanDefinition", "role", element.getRole(), -1);
  }

  protected void composePlanDefinitionPlanDefinitionActionDynamicValueComponent(Complex parent, String parentType, String name, PlanDefinition.PlanDefinitionActionDynamicValueComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "dynamicValue", name, element, index);
    if (element.hasDescriptionElement())
      composeString(t, "PlanDefinition", "description", element.getDescriptionElement(), -1);
    if (element.hasPathElement())
      composeString(t, "PlanDefinition", "path", element.getPathElement(), -1);
    if (element.hasLanguageElement())
      composeString(t, "PlanDefinition", "language", element.getLanguageElement(), -1);
    if (element.hasExpressionElement())
      composeString(t, "PlanDefinition", "expression", element.getExpressionElement(), -1);
  }

  protected void composePractitioner(Complex parent, String parentType, String name, Practitioner element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "Practitioner", name, element, index);
    for (int i = 0; i < element.getIdentifier().size(); i++)
      composeIdentifier(t, "Practitioner", "identifier", element.getIdentifier().get(i), i);
    if (element.hasActiveElement())
      composeBoolean(t, "Practitioner", "active", element.getActiveElement(), -1);
    for (int i = 0; i < element.getName().size(); i++)
      composeHumanName(t, "Practitioner", "name", element.getName().get(i), i);
    for (int i = 0; i < element.getTelecom().size(); i++)
      composeContactPoint(t, "Practitioner", "telecom", element.getTelecom().get(i), i);
    for (int i = 0; i < element.getAddress().size(); i++)
      composeAddress(t, "Practitioner", "address", element.getAddress().get(i), i);
    if (element.hasGenderElement())
      composeEnum(t, "Practitioner", "gender", element.getGenderElement(), -1);
    if (element.hasBirthDateElement())
      composeDate(t, "Practitioner", "birthDate", element.getBirthDateElement(), -1);
    for (int i = 0; i < element.getPhoto().size(); i++)
      composeAttachment(t, "Practitioner", "photo", element.getPhoto().get(i), i);
    for (int i = 0; i < element.getQualification().size(); i++)
      composePractitionerPractitionerQualificationComponent(t, "Practitioner", "qualification", element.getQualification().get(i), i);
    for (int i = 0; i < element.getCommunication().size(); i++)
      composeCodeableConcept(t, "Practitioner", "communication", element.getCommunication().get(i), i);
  }

  protected void composePractitionerPractitionerQualificationComponent(Complex parent, String parentType, String name, Practitioner.PractitionerQualificationComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "qualification", name, element, index);
    for (int i = 0; i < element.getIdentifier().size(); i++)
      composeIdentifier(t, "Practitioner", "identifier", element.getIdentifier().get(i), i);
    if (element.hasCode())
      composeCodeableConcept(t, "Practitioner", "code", element.getCode(), -1);
    if (element.hasPeriod())
      composePeriod(t, "Practitioner", "period", element.getPeriod(), -1);
    if (element.hasIssuer())
      composeReference(t, "Practitioner", "issuer", element.getIssuer(), -1);
  }

  protected void composePractitionerRole(Complex parent, String parentType, String name, PractitionerRole element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "PractitionerRole", name, element, index);
    for (int i = 0; i < element.getIdentifier().size(); i++)
      composeIdentifier(t, "PractitionerRole", "identifier", element.getIdentifier().get(i), i);
    if (element.hasActiveElement())
      composeBoolean(t, "PractitionerRole", "active", element.getActiveElement(), -1);
    if (element.hasPeriod())
      composePeriod(t, "PractitionerRole", "period", element.getPeriod(), -1);
    if (element.hasPractitioner())
      composeReference(t, "PractitionerRole", "practitioner", element.getPractitioner(), -1);
    if (element.hasOrganization())
      composeReference(t, "PractitionerRole", "organization", element.getOrganization(), -1);
    for (int i = 0; i < element.getCode().size(); i++)
      composeCodeableConcept(t, "PractitionerRole", "code", element.getCode().get(i), i);
    for (int i = 0; i < element.getSpecialty().size(); i++)
      composeCodeableConcept(t, "PractitionerRole", "specialty", element.getSpecialty().get(i), i);
    for (int i = 0; i < element.getLocation().size(); i++)
      composeReference(t, "PractitionerRole", "location", element.getLocation().get(i), i);
    for (int i = 0; i < element.getHealthcareService().size(); i++)
      composeReference(t, "PractitionerRole", "healthcareService", element.getHealthcareService().get(i), i);
    for (int i = 0; i < element.getTelecom().size(); i++)
      composeContactPoint(t, "PractitionerRole", "telecom", element.getTelecom().get(i), i);
    for (int i = 0; i < element.getAvailableTime().size(); i++)
      composePractitionerRolePractitionerRoleAvailableTimeComponent(t, "PractitionerRole", "availableTime", element.getAvailableTime().get(i), i);
    for (int i = 0; i < element.getNotAvailable().size(); i++)
      composePractitionerRolePractitionerRoleNotAvailableComponent(t, "PractitionerRole", "notAvailable", element.getNotAvailable().get(i), i);
    if (element.hasAvailabilityExceptionsElement())
      composeString(t, "PractitionerRole", "availabilityExceptions", element.getAvailabilityExceptionsElement(), -1);
    for (int i = 0; i < element.getEndpoint().size(); i++)
      composeReference(t, "PractitionerRole", "endpoint", element.getEndpoint().get(i), i);
  }

  protected void composePractitionerRolePractitionerRoleAvailableTimeComponent(Complex parent, String parentType, String name, PractitionerRole.PractitionerRoleAvailableTimeComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "availableTime", name, element, index);
    for (int i = 0; i < element.getDaysOfWeek().size(); i++)
      composeEnum(t, "PractitionerRole", "daysOfWeek", element.getDaysOfWeek().get(i), i);
    if (element.hasAllDayElement())
      composeBoolean(t, "PractitionerRole", "allDay", element.getAllDayElement(), -1);
    if (element.hasAvailableStartTimeElement())
      composeTime(t, "PractitionerRole", "availableStartTime", element.getAvailableStartTimeElement(), -1);
    if (element.hasAvailableEndTimeElement())
      composeTime(t, "PractitionerRole", "availableEndTime", element.getAvailableEndTimeElement(), -1);
  }

  protected void composePractitionerRolePractitionerRoleNotAvailableComponent(Complex parent, String parentType, String name, PractitionerRole.PractitionerRoleNotAvailableComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "notAvailable", name, element, index);
    if (element.hasDescriptionElement())
      composeString(t, "PractitionerRole", "description", element.getDescriptionElement(), -1);
    if (element.hasDuring())
      composePeriod(t, "PractitionerRole", "during", element.getDuring(), -1);
  }

  protected void composeProcedure(Complex parent, String parentType, String name, Procedure element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "Procedure", name, element, index);
    for (int i = 0; i < element.getIdentifier().size(); i++)
      composeIdentifier(t, "Procedure", "identifier", element.getIdentifier().get(i), i);
    for (int i = 0; i < element.getDefinition().size(); i++)
      composeReference(t, "Procedure", "definition", element.getDefinition().get(i), i);
    for (int i = 0; i < element.getBasedOn().size(); i++)
      composeReference(t, "Procedure", "basedOn", element.getBasedOn().get(i), i);
    for (int i = 0; i < element.getPartOf().size(); i++)
      composeReference(t, "Procedure", "partOf", element.getPartOf().get(i), i);
    if (element.hasStatusElement())
      composeEnum(t, "Procedure", "status", element.getStatusElement(), -1);
    if (element.hasNotDoneElement())
      composeBoolean(t, "Procedure", "notDone", element.getNotDoneElement(), -1);
    if (element.hasNotDoneReason())
      composeCodeableConcept(t, "Procedure", "notDoneReason", element.getNotDoneReason(), -1);
    if (element.hasCategory())
      composeCodeableConcept(t, "Procedure", "category", element.getCategory(), -1);
    if (element.hasCode())
      composeCodeableConcept(t, "Procedure", "code", element.getCode(), -1);
    if (element.hasSubject())
      composeReference(t, "Procedure", "subject", element.getSubject(), -1);
    if (element.hasContext())
      composeReference(t, "Procedure", "context", element.getContext(), -1);
    if (element.hasPerformed())
      composeType(t, "Procedure", "performed", element.getPerformed(), -1);
    for (int i = 0; i < element.getPerformer().size(); i++)
      composeProcedureProcedurePerformerComponent(t, "Procedure", "performer", element.getPerformer().get(i), i);
    if (element.hasLocation())
      composeReference(t, "Procedure", "location", element.getLocation(), -1);
    for (int i = 0; i < element.getReasonCode().size(); i++)
      composeCodeableConcept(t, "Procedure", "reasonCode", element.getReasonCode().get(i), i);
    for (int i = 0; i < element.getReasonReference().size(); i++)
      composeReference(t, "Procedure", "reasonReference", element.getReasonReference().get(i), i);
    for (int i = 0; i < element.getBodySite().size(); i++)
      composeCodeableConcept(t, "Procedure", "bodySite", element.getBodySite().get(i), i);
    if (element.hasOutcome())
      composeCodeableConcept(t, "Procedure", "outcome", element.getOutcome(), -1);
    for (int i = 0; i < element.getReport().size(); i++)
      composeReference(t, "Procedure", "report", element.getReport().get(i), i);
    for (int i = 0; i < element.getComplication().size(); i++)
      composeCodeableConcept(t, "Procedure", "complication", element.getComplication().get(i), i);
    for (int i = 0; i < element.getComplicationDetail().size(); i++)
      composeReference(t, "Procedure", "complicationDetail", element.getComplicationDetail().get(i), i);
    for (int i = 0; i < element.getFollowUp().size(); i++)
      composeCodeableConcept(t, "Procedure", "followUp", element.getFollowUp().get(i), i);
    for (int i = 0; i < element.getNote().size(); i++)
      composeAnnotation(t, "Procedure", "note", element.getNote().get(i), i);
    for (int i = 0; i < element.getFocalDevice().size(); i++)
      composeProcedureProcedureFocalDeviceComponent(t, "Procedure", "focalDevice", element.getFocalDevice().get(i), i);
    for (int i = 0; i < element.getUsedReference().size(); i++)
      composeReference(t, "Procedure", "usedReference", element.getUsedReference().get(i), i);
    for (int i = 0; i < element.getUsedCode().size(); i++)
      composeCodeableConcept(t, "Procedure", "usedCode", element.getUsedCode().get(i), i);
  }

  protected void composeProcedureProcedurePerformerComponent(Complex parent, String parentType, String name, Procedure.ProcedurePerformerComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "performer", name, element, index);
    if (element.hasRole())
      composeCodeableConcept(t, "Procedure", "role", element.getRole(), -1);
    if (element.hasActor())
      composeReference(t, "Procedure", "actor", element.getActor(), -1);
    if (element.hasOnBehalfOf())
      composeReference(t, "Procedure", "onBehalfOf", element.getOnBehalfOf(), -1);
  }

  protected void composeProcedureProcedureFocalDeviceComponent(Complex parent, String parentType, String name, Procedure.ProcedureFocalDeviceComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "focalDevice", name, element, index);
    if (element.hasAction())
      composeCodeableConcept(t, "Procedure", "action", element.getAction(), -1);
    if (element.hasManipulated())
      composeReference(t, "Procedure", "manipulated", element.getManipulated(), -1);
  }

  protected void composeProcedureRequest(Complex parent, String parentType, String name, ProcedureRequest element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "ProcedureRequest", name, element, index);
    for (int i = 0; i < element.getIdentifier().size(); i++)
      composeIdentifier(t, "ProcedureRequest", "identifier", element.getIdentifier().get(i), i);
    for (int i = 0; i < element.getDefinition().size(); i++)
      composeReference(t, "ProcedureRequest", "definition", element.getDefinition().get(i), i);
    for (int i = 0; i < element.getBasedOn().size(); i++)
      composeReference(t, "ProcedureRequest", "basedOn", element.getBasedOn().get(i), i);
    for (int i = 0; i < element.getReplaces().size(); i++)
      composeReference(t, "ProcedureRequest", "replaces", element.getReplaces().get(i), i);
    if (element.hasRequisition())
      composeIdentifier(t, "ProcedureRequest", "requisition", element.getRequisition(), -1);
    if (element.hasStatusElement())
      composeEnum(t, "ProcedureRequest", "status", element.getStatusElement(), -1);
    if (element.hasIntentElement())
      composeEnum(t, "ProcedureRequest", "intent", element.getIntentElement(), -1);
    if (element.hasPriorityElement())
      composeEnum(t, "ProcedureRequest", "priority", element.getPriorityElement(), -1);
    if (element.hasDoNotPerformElement())
      composeBoolean(t, "ProcedureRequest", "doNotPerform", element.getDoNotPerformElement(), -1);
    for (int i = 0; i < element.getCategory().size(); i++)
      composeCodeableConcept(t, "ProcedureRequest", "category", element.getCategory().get(i), i);
    if (element.hasCode())
      composeCodeableConcept(t, "ProcedureRequest", "code", element.getCode(), -1);
    if (element.hasSubject())
      composeReference(t, "ProcedureRequest", "subject", element.getSubject(), -1);
    if (element.hasContext())
      composeReference(t, "ProcedureRequest", "context", element.getContext(), -1);
    if (element.hasOccurrence())
      composeType(t, "ProcedureRequest", "occurrence", element.getOccurrence(), -1);
    if (element.hasAsNeeded())
      composeType(t, "ProcedureRequest", "asNeeded", element.getAsNeeded(), -1);
    if (element.hasAuthoredOnElement())
      composeDateTime(t, "ProcedureRequest", "authoredOn", element.getAuthoredOnElement(), -1);
    if (element.hasRequester())
      composeProcedureRequestProcedureRequestRequesterComponent(t, "ProcedureRequest", "requester", element.getRequester(), -1);
    if (element.hasPerformerType())
      composeCodeableConcept(t, "ProcedureRequest", "performerType", element.getPerformerType(), -1);
    if (element.hasPerformer())
      composeReference(t, "ProcedureRequest", "performer", element.getPerformer(), -1);
    for (int i = 0; i < element.getReasonCode().size(); i++)
      composeCodeableConcept(t, "ProcedureRequest", "reasonCode", element.getReasonCode().get(i), i);
    for (int i = 0; i < element.getReasonReference().size(); i++)
      composeReference(t, "ProcedureRequest", "reasonReference", element.getReasonReference().get(i), i);
    for (int i = 0; i < element.getSupportingInfo().size(); i++)
      composeReference(t, "ProcedureRequest", "supportingInfo", element.getSupportingInfo().get(i), i);
    for (int i = 0; i < element.getSpecimen().size(); i++)
      composeReference(t, "ProcedureRequest", "specimen", element.getSpecimen().get(i), i);
    for (int i = 0; i < element.getBodySite().size(); i++)
      composeCodeableConcept(t, "ProcedureRequest", "bodySite", element.getBodySite().get(i), i);
    for (int i = 0; i < element.getNote().size(); i++)
      composeAnnotation(t, "ProcedureRequest", "note", element.getNote().get(i), i);
    if (element.hasDescriptionElement())
      composeString(t, "ProcedureRequest", "description", element.getDescriptionElement(), -1);
    for (int i = 0; i < element.getRelevantHistory().size(); i++)
      composeReference(t, "ProcedureRequest", "relevantHistory", element.getRelevantHistory().get(i), i);
  }

  protected void composeProcedureRequestProcedureRequestRequesterComponent(Complex parent, String parentType, String name, ProcedureRequest.ProcedureRequestRequesterComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "requester", name, element, index);
    if (element.hasAgent())
      composeReference(t, "ProcedureRequest", "agent", element.getAgent(), -1);
    if (element.hasOnBehalfOf())
      composeReference(t, "ProcedureRequest", "onBehalfOf", element.getOnBehalfOf(), -1);
  }

  protected void composeProcessRequest(Complex parent, String parentType, String name, ProcessRequest element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "ProcessRequest", name, element, index);
    for (int i = 0; i < element.getIdentifier().size(); i++)
      composeIdentifier(t, "ProcessRequest", "identifier", element.getIdentifier().get(i), i);
    if (element.hasStatusElement())
      composeEnum(t, "ProcessRequest", "status", element.getStatusElement(), -1);
    if (element.hasActionElement())
      composeEnum(t, "ProcessRequest", "action", element.getActionElement(), -1);
    if (element.hasTarget())
      composeReference(t, "ProcessRequest", "target", element.getTarget(), -1);
    if (element.hasCreatedElement())
      composeDateTime(t, "ProcessRequest", "created", element.getCreatedElement(), -1);
    if (element.hasProvider())
      composeReference(t, "ProcessRequest", "provider", element.getProvider(), -1);
    if (element.hasOrganization())
      composeReference(t, "ProcessRequest", "organization", element.getOrganization(), -1);
    if (element.hasRequest())
      composeReference(t, "ProcessRequest", "request", element.getRequest(), -1);
    if (element.hasResponse())
      composeReference(t, "ProcessRequest", "response", element.getResponse(), -1);
    if (element.hasNullifyElement())
      composeBoolean(t, "ProcessRequest", "nullify", element.getNullifyElement(), -1);
    if (element.hasReferenceElement())
      composeString(t, "ProcessRequest", "reference", element.getReferenceElement(), -1);
    for (int i = 0; i < element.getItem().size(); i++)
      composeProcessRequestItemsComponent(t, "ProcessRequest", "item", element.getItem().get(i), i);
    for (int i = 0; i < element.getInclude().size(); i++)
      composeString(t, "ProcessRequest", "include", element.getInclude().get(i), i);
    for (int i = 0; i < element.getExclude().size(); i++)
      composeString(t, "ProcessRequest", "exclude", element.getExclude().get(i), i);
    if (element.hasPeriod())
      composePeriod(t, "ProcessRequest", "period", element.getPeriod(), -1);
  }

  protected void composeProcessRequestItemsComponent(Complex parent, String parentType, String name, ProcessRequest.ItemsComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "item", name, element, index);
    if (element.hasSequenceLinkIdElement())
      composeInteger(t, "ProcessRequest", "sequenceLinkId", element.getSequenceLinkIdElement(), -1);
  }

  protected void composeProcessResponse(Complex parent, String parentType, String name, ProcessResponse element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "ProcessResponse", name, element, index);
    for (int i = 0; i < element.getIdentifier().size(); i++)
      composeIdentifier(t, "ProcessResponse", "identifier", element.getIdentifier().get(i), i);
    if (element.hasStatusElement())
      composeEnum(t, "ProcessResponse", "status", element.getStatusElement(), -1);
    if (element.hasCreatedElement())
      composeDateTime(t, "ProcessResponse", "created", element.getCreatedElement(), -1);
    if (element.hasOrganization())
      composeReference(t, "ProcessResponse", "organization", element.getOrganization(), -1);
    if (element.hasRequest())
      composeReference(t, "ProcessResponse", "request", element.getRequest(), -1);
    if (element.hasOutcomeElement())
      composeEnum(t, "ProcessResponse", "outcome", element.getOutcomeElement(), -1);
    if (element.hasDispositionElement())
      composeString(t, "ProcessResponse", "disposition", element.getDispositionElement(), -1);
    if (element.hasRequestProvider())
      composeReference(t, "ProcessResponse", "requestProvider", element.getRequestProvider(), -1);
    if (element.hasRequestOrganization())
      composeReference(t, "ProcessResponse", "requestOrganization", element.getRequestOrganization(), -1);
    if (element.hasForm())
      composeCodeableConcept(t, "ProcessResponse", "form", element.getForm(), -1);
    for (int i = 0; i < element.getProcessNote().size(); i++)
      composeProcessResponseProcessResponseProcessNoteComponent(t, "ProcessResponse", "processNote", element.getProcessNote().get(i), i);
    for (int i = 0; i < element.getError().size(); i++)
      composeCodeableConcept(t, "ProcessResponse", "error", element.getError().get(i), i);
    for (int i = 0; i < element.getCommunicationRequest().size(); i++)
      composeReference(t, "ProcessResponse", "communicationRequest", element.getCommunicationRequest().get(i), i);
  }

  protected void composeProcessResponseProcessResponseProcessNoteComponent(Complex parent, String parentType, String name, ProcessResponse.ProcessResponseProcessNoteComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "processNote", name, element, index);
    if (element.hasTypeElement())
      composeEnum(t, "ProcessResponse", "type", element.getTypeElement(), -1);
    if (element.hasTextElement())
      composeString(t, "ProcessResponse", "text", element.getTextElement(), -1);
  }

  protected void composeProvenance(Complex parent, String parentType, String name, Provenance element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "Provenance", name, element, index);
    for (int i = 0; i < element.getTarget().size(); i++)
      composeReference(t, "Provenance", "target", element.getTarget().get(i), i);
    if (element.hasOccured())
      composeType(t, "Provenance", "occured", element.getOccured(), -1);
    if (element.hasRecordedElement())
      composeInstant(t, "Provenance", "recorded", element.getRecordedElement(), -1);
    for (int i = 0; i < element.getPolicy().size(); i++)
      composeUri(t, "Provenance", "policy", element.getPolicy().get(i), i);
    if (element.hasLocation())
      composeReference(t, "Provenance", "location", element.getLocation(), -1);
    for (int i = 0; i < element.getReason().size(); i++)
      composeCodeableConcept(t, "Provenance", "reason", element.getReason().get(i), i);
    if (element.hasActivity())
      composeCodeableConcept(t, "Provenance", "activity", element.getActivity(), -1);
    for (int i = 0; i < element.getAgent().size(); i++)
      composeProvenanceProvenanceAgentComponent(t, "Provenance", "agent", element.getAgent().get(i), i);
    for (int i = 0; i < element.getEntity().size(); i++)
      composeProvenanceProvenanceEntityComponent(t, "Provenance", "entity", element.getEntity().get(i), i);
    for (int i = 0; i < element.getSignature().size(); i++)
      composeSignature(t, "Provenance", "signature", element.getSignature().get(i), i);
  }

  protected void composeProvenanceProvenanceAgentComponent(Complex parent, String parentType, String name, Provenance.ProvenanceAgentComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "agent", name, element, index);
    if (element.hasType())
      composeCodeableConcept(t, "Provenance", "type", element.getType(), -1);
    for (int i = 0; i < element.getRole().size(); i++)
      composeCodeableConcept(t, "Provenance", "role", element.getRole().get(i), i);
    if (element.hasWho())
      composeType(t, "Provenance", "who", element.getWho(), -1);
    if (element.hasOnBehalfOf())
      composeType(t, "Provenance", "onBehalfOf", element.getOnBehalfOf(), -1);
  }

  protected void composeProvenanceProvenanceEntityComponent(Complex parent, String parentType, String name, Provenance.ProvenanceEntityComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "entity", name, element, index);
    if (element.hasRoleElement())
      composeEnum(t, "Provenance", "role", element.getRoleElement(), -1);
    if (element.hasWhat())
      composeType(t, "Provenance", "what", element.getWhat(), -1);
    for (int i = 0; i < element.getAgent().size(); i++)
      composeProvenanceProvenanceAgentComponent(t, "Provenance", "agent", element.getAgent().get(i), i);
  }

  protected void composeQuestionnaire(Complex parent, String parentType, String name, Questionnaire element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "Questionnaire", name, element, index);
    if (element.hasUrlElement())
      composeUri(t, "Questionnaire", "url", element.getUrlElement(), -1);
    for (int i = 0; i < element.getIdentifier().size(); i++)
      composeIdentifier(t, "Questionnaire", "identifier", element.getIdentifier().get(i), i);
    if (element.hasVersionElement())
      composeString(t, "Questionnaire", "version", element.getVersionElement(), -1);
    if (element.hasNameElement())
      composeString(t, "Questionnaire", "name", element.getNameElement(), -1);
    if (element.hasTitleElement())
      composeString(t, "Questionnaire", "title", element.getTitleElement(), -1);
    if (element.hasStatusElement())
      composeEnum(t, "Questionnaire", "status", element.getStatusElement(), -1);
    if (element.hasExperimentalElement())
      composeBoolean(t, "Questionnaire", "experimental", element.getExperimentalElement(), -1);
    if (element.hasDateElement())
      composeDateTime(t, "Questionnaire", "date", element.getDateElement(), -1);
    if (element.hasPublisherElement())
      composeString(t, "Questionnaire", "publisher", element.getPublisherElement(), -1);
    if (element.hasDescriptionElement())
      composeMarkdown(t, "Questionnaire", "description", element.getDescriptionElement(), -1);
    if (element.hasPurposeElement())
      composeMarkdown(t, "Questionnaire", "purpose", element.getPurposeElement(), -1);
    if (element.hasApprovalDateElement())
      composeDate(t, "Questionnaire", "approvalDate", element.getApprovalDateElement(), -1);
    if (element.hasLastReviewDateElement())
      composeDate(t, "Questionnaire", "lastReviewDate", element.getLastReviewDateElement(), -1);
    if (element.hasEffectivePeriod())
      composePeriod(t, "Questionnaire", "effectivePeriod", element.getEffectivePeriod(), -1);
    for (int i = 0; i < element.getUseContext().size(); i++)
      composeUsageContext(t, "Questionnaire", "useContext", element.getUseContext().get(i), i);
    for (int i = 0; i < element.getJurisdiction().size(); i++)
      composeCodeableConcept(t, "Questionnaire", "jurisdiction", element.getJurisdiction().get(i), i);
    for (int i = 0; i < element.getContact().size(); i++)
      composeContactDetail(t, "Questionnaire", "contact", element.getContact().get(i), i);
    if (element.hasCopyrightElement())
      composeMarkdown(t, "Questionnaire", "copyright", element.getCopyrightElement(), -1);
    for (int i = 0; i < element.getCode().size(); i++)
      composeCoding(t, "Questionnaire", "code", element.getCode().get(i), i);
    for (int i = 0; i < element.getSubjectType().size(); i++)
      composeCode(t, "Questionnaire", "subjectType", element.getSubjectType().get(i), i);
    for (int i = 0; i < element.getItem().size(); i++)
      composeQuestionnaireQuestionnaireItemComponent(t, "Questionnaire", "item", element.getItem().get(i), i);
  }

  protected void composeQuestionnaireQuestionnaireItemComponent(Complex parent, String parentType, String name, Questionnaire.QuestionnaireItemComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "item", name, element, index);
    if (element.hasLinkIdElement())
      composeString(t, "Questionnaire", "linkId", element.getLinkIdElement(), -1);
    if (element.hasDefinitionElement())
      composeUri(t, "Questionnaire", "definition", element.getDefinitionElement(), -1);
    for (int i = 0; i < element.getCode().size(); i++)
      composeCoding(t, "Questionnaire", "code", element.getCode().get(i), i);
    if (element.hasPrefixElement())
      composeString(t, "Questionnaire", "prefix", element.getPrefixElement(), -1);
    if (element.hasTextElement())
      composeString(t, "Questionnaire", "text", element.getTextElement(), -1);
    if (element.hasTypeElement())
      composeEnum(t, "Questionnaire", "type", element.getTypeElement(), -1);
    for (int i = 0; i < element.getEnableWhen().size(); i++)
      composeQuestionnaireQuestionnaireItemEnableWhenComponent(t, "Questionnaire", "enableWhen", element.getEnableWhen().get(i), i);
    if (element.hasRequiredElement())
      composeBoolean(t, "Questionnaire", "required", element.getRequiredElement(), -1);
    if (element.hasRepeatsElement())
      composeBoolean(t, "Questionnaire", "repeats", element.getRepeatsElement(), -1);
    if (element.hasReadOnlyElement())
      composeBoolean(t, "Questionnaire", "readOnly", element.getReadOnlyElement(), -1);
    if (element.hasMaxLengthElement())
      composeInteger(t, "Questionnaire", "maxLength", element.getMaxLengthElement(), -1);
    if (element.hasOptions())
      composeReference(t, "Questionnaire", "options", element.getOptions(), -1);
    for (int i = 0; i < element.getOption().size(); i++)
      composeQuestionnaireQuestionnaireItemOptionComponent(t, "Questionnaire", "option", element.getOption().get(i), i);
    if (element.hasInitial())
      composeType(t, "Questionnaire", "initial", element.getInitial(), -1);
    for (int i = 0; i < element.getItem().size(); i++)
      composeQuestionnaireQuestionnaireItemComponent(t, "Questionnaire", "item", element.getItem().get(i), i);
  }

  protected void composeQuestionnaireQuestionnaireItemEnableWhenComponent(Complex parent, String parentType, String name, Questionnaire.QuestionnaireItemEnableWhenComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "enableWhen", name, element, index);
    if (element.hasQuestionElement())
      composeString(t, "Questionnaire", "question", element.getQuestionElement(), -1);
    if (element.hasHasAnswerElement())
      composeBoolean(t, "Questionnaire", "hasAnswer", element.getHasAnswerElement(), -1);
    if (element.hasAnswer())
      composeType(t, "Questionnaire", "answer", element.getAnswer(), -1);
  }

  protected void composeQuestionnaireQuestionnaireItemOptionComponent(Complex parent, String parentType, String name, Questionnaire.QuestionnaireItemOptionComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "option", name, element, index);
    if (element.hasValue())
      composeType(t, "Questionnaire", "value", element.getValue(), -1);
  }

  protected void composeQuestionnaireResponse(Complex parent, String parentType, String name, QuestionnaireResponse element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "QuestionnaireResponse", name, element, index);
    if (element.hasIdentifier())
      composeIdentifier(t, "QuestionnaireResponse", "identifier", element.getIdentifier(), -1);
    for (int i = 0; i < element.getBasedOn().size(); i++)
      composeReference(t, "QuestionnaireResponse", "basedOn", element.getBasedOn().get(i), i);
    for (int i = 0; i < element.getParent().size(); i++)
      composeReference(t, "QuestionnaireResponse", "parent", element.getParent().get(i), i);
    if (element.hasQuestionnaire())
      composeReference(t, "QuestionnaireResponse", "questionnaire", element.getQuestionnaire(), -1);
    if (element.hasStatusElement())
      composeEnum(t, "QuestionnaireResponse", "status", element.getStatusElement(), -1);
    if (element.hasSubject())
      composeReference(t, "QuestionnaireResponse", "subject", element.getSubject(), -1);
    if (element.hasContext())
      composeReference(t, "QuestionnaireResponse", "context", element.getContext(), -1);
    if (element.hasAuthoredElement())
      composeDateTime(t, "QuestionnaireResponse", "authored", element.getAuthoredElement(), -1);
    if (element.hasAuthor())
      composeReference(t, "QuestionnaireResponse", "author", element.getAuthor(), -1);
    if (element.hasSource())
      composeReference(t, "QuestionnaireResponse", "source", element.getSource(), -1);
    for (int i = 0; i < element.getItem().size(); i++)
      composeQuestionnaireResponseQuestionnaireResponseItemComponent(t, "QuestionnaireResponse", "item", element.getItem().get(i), i);
  }

  protected void composeQuestionnaireResponseQuestionnaireResponseItemComponent(Complex parent, String parentType, String name, QuestionnaireResponse.QuestionnaireResponseItemComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "item", name, element, index);
    if (element.hasLinkIdElement())
      composeString(t, "QuestionnaireResponse", "linkId", element.getLinkIdElement(), -1);
    if (element.hasDefinitionElement())
      composeUri(t, "QuestionnaireResponse", "definition", element.getDefinitionElement(), -1);
    if (element.hasTextElement())
      composeString(t, "QuestionnaireResponse", "text", element.getTextElement(), -1);
    if (element.hasSubject())
      composeReference(t, "QuestionnaireResponse", "subject", element.getSubject(), -1);
    for (int i = 0; i < element.getAnswer().size(); i++)
      composeQuestionnaireResponseQuestionnaireResponseItemAnswerComponent(t, "QuestionnaireResponse", "answer", element.getAnswer().get(i), i);
    for (int i = 0; i < element.getItem().size(); i++)
      composeQuestionnaireResponseQuestionnaireResponseItemComponent(t, "QuestionnaireResponse", "item", element.getItem().get(i), i);
  }

  protected void composeQuestionnaireResponseQuestionnaireResponseItemAnswerComponent(Complex parent, String parentType, String name, QuestionnaireResponse.QuestionnaireResponseItemAnswerComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "answer", name, element, index);
    if (element.hasValue())
      composeType(t, "QuestionnaireResponse", "value", element.getValue(), -1);
    for (int i = 0; i < element.getItem().size(); i++)
      composeQuestionnaireResponseQuestionnaireResponseItemComponent(t, "QuestionnaireResponse", "item", element.getItem().get(i), i);
  }

  protected void composeRelatedPerson(Complex parent, String parentType, String name, RelatedPerson element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "RelatedPerson", name, element, index);
    for (int i = 0; i < element.getIdentifier().size(); i++)
      composeIdentifier(t, "RelatedPerson", "identifier", element.getIdentifier().get(i), i);
    if (element.hasActiveElement())
      composeBoolean(t, "RelatedPerson", "active", element.getActiveElement(), -1);
    if (element.hasPatient())
      composeReference(t, "RelatedPerson", "patient", element.getPatient(), -1);
    if (element.hasRelationship())
      composeCodeableConcept(t, "RelatedPerson", "relationship", element.getRelationship(), -1);
    for (int i = 0; i < element.getName().size(); i++)
      composeHumanName(t, "RelatedPerson", "name", element.getName().get(i), i);
    for (int i = 0; i < element.getTelecom().size(); i++)
      composeContactPoint(t, "RelatedPerson", "telecom", element.getTelecom().get(i), i);
    if (element.hasGenderElement())
      composeEnum(t, "RelatedPerson", "gender", element.getGenderElement(), -1);
    if (element.hasBirthDateElement())
      composeDate(t, "RelatedPerson", "birthDate", element.getBirthDateElement(), -1);
    for (int i = 0; i < element.getAddress().size(); i++)
      composeAddress(t, "RelatedPerson", "address", element.getAddress().get(i), i);
    for (int i = 0; i < element.getPhoto().size(); i++)
      composeAttachment(t, "RelatedPerson", "photo", element.getPhoto().get(i), i);
    if (element.hasPeriod())
      composePeriod(t, "RelatedPerson", "period", element.getPeriod(), -1);
  }

  protected void composeRequestGroup(Complex parent, String parentType, String name, RequestGroup element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "RequestGroup", name, element, index);
    for (int i = 0; i < element.getIdentifier().size(); i++)
      composeIdentifier(t, "RequestGroup", "identifier", element.getIdentifier().get(i), i);
    for (int i = 0; i < element.getDefinition().size(); i++)
      composeReference(t, "RequestGroup", "definition", element.getDefinition().get(i), i);
    for (int i = 0; i < element.getBasedOn().size(); i++)
      composeReference(t, "RequestGroup", "basedOn", element.getBasedOn().get(i), i);
    for (int i = 0; i < element.getReplaces().size(); i++)
      composeReference(t, "RequestGroup", "replaces", element.getReplaces().get(i), i);
    if (element.hasGroupIdentifier())
      composeIdentifier(t, "RequestGroup", "groupIdentifier", element.getGroupIdentifier(), -1);
    if (element.hasStatusElement())
      composeEnum(t, "RequestGroup", "status", element.getStatusElement(), -1);
    if (element.hasIntentElement())
      composeEnum(t, "RequestGroup", "intent", element.getIntentElement(), -1);
    if (element.hasPriorityElement())
      composeEnum(t, "RequestGroup", "priority", element.getPriorityElement(), -1);
    if (element.hasCode())
      composeCodeableConcept(t, "RequestGroup", "code", element.getCode(), -1);
    if (element.hasSubject())
      composeReference(t, "RequestGroup", "subject", element.getSubject(), -1);
    if (element.hasContext())
      composeReference(t, "RequestGroup", "context", element.getContext(), -1);
    if (element.hasAuthoredOnElement())
      composeDateTime(t, "RequestGroup", "authoredOn", element.getAuthoredOnElement(), -1);
    if (element.hasAuthor())
      composeReference(t, "RequestGroup", "author", element.getAuthor(), -1);
    for (int i = 0; i < element.getReasonCode().size(); i++)
      composeCodeableConcept(t, "RequestGroup", "reasonCode", element.getReasonCode().get(i), i);
    for (int i = 0; i < element.getReasonReference().size(); i++)
      composeReference(t, "RequestGroup", "reasonReference", element.getReasonReference().get(i), i);
    for (int i = 0; i < element.getNote().size(); i++)
      composeAnnotation(t, "RequestGroup", "note", element.getNote().get(i), i);
    for (int i = 0; i < element.getAction().size(); i++)
      composeRequestGroupRequestGroupActionComponent(t, "RequestGroup", "action", element.getAction().get(i), i);
  }

  protected void composeRequestGroupRequestGroupActionComponent(Complex parent, String parentType, String name, RequestGroup.RequestGroupActionComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "action", name, element, index);
    if (element.hasPrefixElement())
      composeString(t, "RequestGroup", "prefix", element.getPrefixElement(), -1);
    if (element.hasTitleElement())
      composeString(t, "RequestGroup", "title", element.getTitleElement(), -1);
    if (element.hasDescriptionElement())
      composeString(t, "RequestGroup", "description", element.getDescriptionElement(), -1);
    if (element.hasTextEquivalentElement())
      composeString(t, "RequestGroup", "textEquivalent", element.getTextEquivalentElement(), -1);
    for (int i = 0; i < element.getCode().size(); i++)
      composeCodeableConcept(t, "RequestGroup", "code", element.getCode().get(i), i);
    for (int i = 0; i < element.getDocumentation().size(); i++)
      composeRelatedArtifact(t, "RequestGroup", "documentation", element.getDocumentation().get(i), i);
    for (int i = 0; i < element.getCondition().size(); i++)
      composeRequestGroupRequestGroupActionConditionComponent(t, "RequestGroup", "condition", element.getCondition().get(i), i);
    for (int i = 0; i < element.getRelatedAction().size(); i++)
      composeRequestGroupRequestGroupActionRelatedActionComponent(t, "RequestGroup", "relatedAction", element.getRelatedAction().get(i), i);
    if (element.hasTiming())
      composeType(t, "RequestGroup", "timing", element.getTiming(), -1);
    for (int i = 0; i < element.getParticipant().size(); i++)
      composeReference(t, "RequestGroup", "participant", element.getParticipant().get(i), i);
    if (element.hasType())
      composeCoding(t, "RequestGroup", "type", element.getType(), -1);
    if (element.hasGroupingBehaviorElement())
      composeEnum(t, "RequestGroup", "groupingBehavior", element.getGroupingBehaviorElement(), -1);
    if (element.hasSelectionBehaviorElement())
      composeEnum(t, "RequestGroup", "selectionBehavior", element.getSelectionBehaviorElement(), -1);
    if (element.hasRequiredBehaviorElement())
      composeEnum(t, "RequestGroup", "requiredBehavior", element.getRequiredBehaviorElement(), -1);
    if (element.hasPrecheckBehaviorElement())
      composeEnum(t, "RequestGroup", "precheckBehavior", element.getPrecheckBehaviorElement(), -1);
    if (element.hasCardinalityBehaviorElement())
      composeEnum(t, "RequestGroup", "cardinalityBehavior", element.getCardinalityBehaviorElement(), -1);
    if (element.hasResource())
      composeReference(t, "RequestGroup", "resource", element.getResource(), -1);
    for (int i = 0; i < element.getAction().size(); i++)
      composeRequestGroupRequestGroupActionComponent(t, "RequestGroup", "action", element.getAction().get(i), i);
  }

  protected void composeRequestGroupRequestGroupActionConditionComponent(Complex parent, String parentType, String name, RequestGroup.RequestGroupActionConditionComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "condition", name, element, index);
    if (element.hasKindElement())
      composeEnum(t, "RequestGroup", "kind", element.getKindElement(), -1);
    if (element.hasDescriptionElement())
      composeString(t, "RequestGroup", "description", element.getDescriptionElement(), -1);
    if (element.hasLanguageElement())
      composeString(t, "RequestGroup", "language", element.getLanguageElement(), -1);
    if (element.hasExpressionElement())
      composeString(t, "RequestGroup", "expression", element.getExpressionElement(), -1);
  }

  protected void composeRequestGroupRequestGroupActionRelatedActionComponent(Complex parent, String parentType, String name, RequestGroup.RequestGroupActionRelatedActionComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "relatedAction", name, element, index);
    if (element.hasActionIdElement())
      composeId(t, "RequestGroup", "actionId", element.getActionIdElement(), -1);
    if (element.hasRelationshipElement())
      composeEnum(t, "RequestGroup", "relationship", element.getRelationshipElement(), -1);
    if (element.hasOffset())
      composeType(t, "RequestGroup", "offset", element.getOffset(), -1);
  }

  protected void composeResearchStudy(Complex parent, String parentType, String name, ResearchStudy element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "ResearchStudy", name, element, index);
    for (int i = 0; i < element.getIdentifier().size(); i++)
      composeIdentifier(t, "ResearchStudy", "identifier", element.getIdentifier().get(i), i);
    if (element.hasTitleElement())
      composeString(t, "ResearchStudy", "title", element.getTitleElement(), -1);
    for (int i = 0; i < element.getProtocol().size(); i++)
      composeReference(t, "ResearchStudy", "protocol", element.getProtocol().get(i), i);
    for (int i = 0; i < element.getPartOf().size(); i++)
      composeReference(t, "ResearchStudy", "partOf", element.getPartOf().get(i), i);
    if (element.hasStatusElement())
      composeEnum(t, "ResearchStudy", "status", element.getStatusElement(), -1);
    for (int i = 0; i < element.getCategory().size(); i++)
      composeCodeableConcept(t, "ResearchStudy", "category", element.getCategory().get(i), i);
    for (int i = 0; i < element.getFocus().size(); i++)
      composeCodeableConcept(t, "ResearchStudy", "focus", element.getFocus().get(i), i);
    for (int i = 0; i < element.getContact().size(); i++)
      composeContactDetail(t, "ResearchStudy", "contact", element.getContact().get(i), i);
    for (int i = 0; i < element.getRelatedArtifact().size(); i++)
      composeRelatedArtifact(t, "ResearchStudy", "relatedArtifact", element.getRelatedArtifact().get(i), i);
    for (int i = 0; i < element.getKeyword().size(); i++)
      composeCodeableConcept(t, "ResearchStudy", "keyword", element.getKeyword().get(i), i);
    for (int i = 0; i < element.getJurisdiction().size(); i++)
      composeCodeableConcept(t, "ResearchStudy", "jurisdiction", element.getJurisdiction().get(i), i);
    if (element.hasDescriptionElement())
      composeMarkdown(t, "ResearchStudy", "description", element.getDescriptionElement(), -1);
    for (int i = 0; i < element.getEnrollment().size(); i++)
      composeReference(t, "ResearchStudy", "enrollment", element.getEnrollment().get(i), i);
    if (element.hasPeriod())
      composePeriod(t, "ResearchStudy", "period", element.getPeriod(), -1);
    if (element.hasSponsor())
      composeReference(t, "ResearchStudy", "sponsor", element.getSponsor(), -1);
    if (element.hasPrincipalInvestigator())
      composeReference(t, "ResearchStudy", "principalInvestigator", element.getPrincipalInvestigator(), -1);
    for (int i = 0; i < element.getSite().size(); i++)
      composeReference(t, "ResearchStudy", "site", element.getSite().get(i), i);
    if (element.hasReasonStopped())
      composeCodeableConcept(t, "ResearchStudy", "reasonStopped", element.getReasonStopped(), -1);
    for (int i = 0; i < element.getNote().size(); i++)
      composeAnnotation(t, "ResearchStudy", "note", element.getNote().get(i), i);
    for (int i = 0; i < element.getArm().size(); i++)
      composeResearchStudyResearchStudyArmComponent(t, "ResearchStudy", "arm", element.getArm().get(i), i);
  }

  protected void composeResearchStudyResearchStudyArmComponent(Complex parent, String parentType, String name, ResearchStudy.ResearchStudyArmComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "arm", name, element, index);
    if (element.hasNameElement())
      composeString(t, "ResearchStudy", "name", element.getNameElement(), -1);
    if (element.hasCode())
      composeCodeableConcept(t, "ResearchStudy", "code", element.getCode(), -1);
    if (element.hasDescriptionElement())
      composeString(t, "ResearchStudy", "description", element.getDescriptionElement(), -1);
  }

  protected void composeResearchSubject(Complex parent, String parentType, String name, ResearchSubject element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "ResearchSubject", name, element, index);
    if (element.hasIdentifier())
      composeIdentifier(t, "ResearchSubject", "identifier", element.getIdentifier(), -1);
    if (element.hasStatusElement())
      composeEnum(t, "ResearchSubject", "status", element.getStatusElement(), -1);
    if (element.hasPeriod())
      composePeriod(t, "ResearchSubject", "period", element.getPeriod(), -1);
    if (element.hasStudy())
      composeReference(t, "ResearchSubject", "study", element.getStudy(), -1);
    if (element.hasIndividual())
      composeReference(t, "ResearchSubject", "individual", element.getIndividual(), -1);
    if (element.hasAssignedArmElement())
      composeString(t, "ResearchSubject", "assignedArm", element.getAssignedArmElement(), -1);
    if (element.hasActualArmElement())
      composeString(t, "ResearchSubject", "actualArm", element.getActualArmElement(), -1);
    if (element.hasConsent())
      composeReference(t, "ResearchSubject", "consent", element.getConsent(), -1);
  }

  protected void composeRiskAssessment(Complex parent, String parentType, String name, RiskAssessment element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "RiskAssessment", name, element, index);
    if (element.hasIdentifier())
      composeIdentifier(t, "RiskAssessment", "identifier", element.getIdentifier(), -1);
    if (element.hasBasedOn())
      composeReference(t, "RiskAssessment", "basedOn", element.getBasedOn(), -1);
    if (element.hasParent())
      composeReference(t, "RiskAssessment", "parent", element.getParent(), -1);
    if (element.hasStatusElement())
      composeEnum(t, "RiskAssessment", "status", element.getStatusElement(), -1);
    if (element.hasMethod())
      composeCodeableConcept(t, "RiskAssessment", "method", element.getMethod(), -1);
    if (element.hasCode())
      composeCodeableConcept(t, "RiskAssessment", "code", element.getCode(), -1);
    if (element.hasSubject())
      composeReference(t, "RiskAssessment", "subject", element.getSubject(), -1);
    if (element.hasContext())
      composeReference(t, "RiskAssessment", "context", element.getContext(), -1);
    if (element.hasOccurrence())
      composeType(t, "RiskAssessment", "occurrence", element.getOccurrence(), -1);
    if (element.hasCondition())
      composeReference(t, "RiskAssessment", "condition", element.getCondition(), -1);
    if (element.hasPerformer())
      composeReference(t, "RiskAssessment", "performer", element.getPerformer(), -1);
    if (element.hasReason())
      composeType(t, "RiskAssessment", "reason", element.getReason(), -1);
    for (int i = 0; i < element.getBasis().size(); i++)
      composeReference(t, "RiskAssessment", "basis", element.getBasis().get(i), i);
    for (int i = 0; i < element.getPrediction().size(); i++)
      composeRiskAssessmentRiskAssessmentPredictionComponent(t, "RiskAssessment", "prediction", element.getPrediction().get(i), i);
    if (element.hasMitigationElement())
      composeString(t, "RiskAssessment", "mitigation", element.getMitigationElement(), -1);
    if (element.hasCommentElement())
      composeString(t, "RiskAssessment", "comment", element.getCommentElement(), -1);
  }

  protected void composeRiskAssessmentRiskAssessmentPredictionComponent(Complex parent, String parentType, String name, RiskAssessment.RiskAssessmentPredictionComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "prediction", name, element, index);
    if (element.hasOutcome())
      composeCodeableConcept(t, "RiskAssessment", "outcome", element.getOutcome(), -1);
    if (element.hasProbability())
      composeType(t, "RiskAssessment", "probability", element.getProbability(), -1);
    if (element.hasQualitativeRisk())
      composeCodeableConcept(t, "RiskAssessment", "qualitativeRisk", element.getQualitativeRisk(), -1);
    if (element.hasRelativeRiskElement())
      composeDecimal(t, "RiskAssessment", "relativeRisk", element.getRelativeRiskElement(), -1);
    if (element.hasWhen())
      composeType(t, "RiskAssessment", "when", element.getWhen(), -1);
    if (element.hasRationaleElement())
      composeString(t, "RiskAssessment", "rationale", element.getRationaleElement(), -1);
  }

  protected void composeSchedule(Complex parent, String parentType, String name, Schedule element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "Schedule", name, element, index);
    for (int i = 0; i < element.getIdentifier().size(); i++)
      composeIdentifier(t, "Schedule", "identifier", element.getIdentifier().get(i), i);
    if (element.hasActiveElement())
      composeBoolean(t, "Schedule", "active", element.getActiveElement(), -1);
    if (element.hasServiceCategory())
      composeCodeableConcept(t, "Schedule", "serviceCategory", element.getServiceCategory(), -1);
    for (int i = 0; i < element.getServiceType().size(); i++)
      composeCodeableConcept(t, "Schedule", "serviceType", element.getServiceType().get(i), i);
    for (int i = 0; i < element.getSpecialty().size(); i++)
      composeCodeableConcept(t, "Schedule", "specialty", element.getSpecialty().get(i), i);
    for (int i = 0; i < element.getActor().size(); i++)
      composeReference(t, "Schedule", "actor", element.getActor().get(i), i);
    if (element.hasPlanningHorizon())
      composePeriod(t, "Schedule", "planningHorizon", element.getPlanningHorizon(), -1);
    if (element.hasCommentElement())
      composeString(t, "Schedule", "comment", element.getCommentElement(), -1);
  }

  protected void composeSearchParameter(Complex parent, String parentType, String name, SearchParameter element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "SearchParameter", name, element, index);
    if (element.hasUrlElement())
      composeUri(t, "SearchParameter", "url", element.getUrlElement(), -1);
    if (element.hasVersionElement())
      composeString(t, "SearchParameter", "version", element.getVersionElement(), -1);
    if (element.hasNameElement())
      composeString(t, "SearchParameter", "name", element.getNameElement(), -1);
    if (element.hasStatusElement())
      composeEnum(t, "SearchParameter", "status", element.getStatusElement(), -1);
    if (element.hasExperimentalElement())
      composeBoolean(t, "SearchParameter", "experimental", element.getExperimentalElement(), -1);
    if (element.hasDateElement())
      composeDateTime(t, "SearchParameter", "date", element.getDateElement(), -1);
    if (element.hasPublisherElement())
      composeString(t, "SearchParameter", "publisher", element.getPublisherElement(), -1);
    for (int i = 0; i < element.getContact().size(); i++)
      composeContactDetail(t, "SearchParameter", "contact", element.getContact().get(i), i);
    for (int i = 0; i < element.getUseContext().size(); i++)
      composeUsageContext(t, "SearchParameter", "useContext", element.getUseContext().get(i), i);
    for (int i = 0; i < element.getJurisdiction().size(); i++)
      composeCodeableConcept(t, "SearchParameter", "jurisdiction", element.getJurisdiction().get(i), i);
    if (element.hasPurposeElement())
      composeMarkdown(t, "SearchParameter", "purpose", element.getPurposeElement(), -1);
    if (element.hasCodeElement())
      composeCode(t, "SearchParameter", "code", element.getCodeElement(), -1);
    for (int i = 0; i < element.getBase().size(); i++)
      composeCode(t, "SearchParameter", "base", element.getBase().get(i), i);
    if (element.hasTypeElement())
      composeEnum(t, "SearchParameter", "type", element.getTypeElement(), -1);
    if (element.hasDerivedFromElement())
      composeUri(t, "SearchParameter", "derivedFrom", element.getDerivedFromElement(), -1);
    if (element.hasDescriptionElement())
      composeMarkdown(t, "SearchParameter", "description", element.getDescriptionElement(), -1);
    if (element.hasExpressionElement())
      composeString(t, "SearchParameter", "expression", element.getExpressionElement(), -1);
    if (element.hasXpathElement())
      composeString(t, "SearchParameter", "xpath", element.getXpathElement(), -1);
    if (element.hasXpathUsageElement())
      composeEnum(t, "SearchParameter", "xpathUsage", element.getXpathUsageElement(), -1);
    for (int i = 0; i < element.getTarget().size(); i++)
      composeCode(t, "SearchParameter", "target", element.getTarget().get(i), i);
    for (int i = 0; i < element.getComparator().size(); i++)
      composeEnum(t, "SearchParameter", "comparator", element.getComparator().get(i), i);
    for (int i = 0; i < element.getModifier().size(); i++)
      composeEnum(t, "SearchParameter", "modifier", element.getModifier().get(i), i);
    for (int i = 0; i < element.getChain().size(); i++)
      composeString(t, "SearchParameter", "chain", element.getChain().get(i), i);
    for (int i = 0; i < element.getComponent().size(); i++)
      composeSearchParameterSearchParameterComponentComponent(t, "SearchParameter", "component", element.getComponent().get(i), i);
  }

  protected void composeSearchParameterSearchParameterComponentComponent(Complex parent, String parentType, String name, SearchParameter.SearchParameterComponentComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "component", name, element, index);
    if (element.hasDefinition())
      composeReference(t, "SearchParameter", "definition", element.getDefinition(), -1);
    if (element.hasExpressionElement())
      composeString(t, "SearchParameter", "expression", element.getExpressionElement(), -1);
  }

  protected void composeSequence(Complex parent, String parentType, String name, Sequence element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "Sequence", name, element, index);
    for (int i = 0; i < element.getIdentifier().size(); i++)
      composeIdentifier(t, "Sequence", "identifier", element.getIdentifier().get(i), i);
    if (element.hasTypeElement())
      composeEnum(t, "Sequence", "type", element.getTypeElement(), -1);
    if (element.hasCoordinateSystemElement())
      composeInteger(t, "Sequence", "coordinateSystem", element.getCoordinateSystemElement(), -1);
    if (element.hasPatient())
      composeReference(t, "Sequence", "patient", element.getPatient(), -1);
    if (element.hasSpecimen())
      composeReference(t, "Sequence", "specimen", element.getSpecimen(), -1);
    if (element.hasDevice())
      composeReference(t, "Sequence", "device", element.getDevice(), -1);
    if (element.hasPerformer())
      composeReference(t, "Sequence", "performer", element.getPerformer(), -1);
    if (element.hasQuantity())
      composeQuantity(t, "Sequence", "quantity", element.getQuantity(), -1);
    if (element.hasReferenceSeq())
      composeSequenceSequenceReferenceSeqComponent(t, "Sequence", "referenceSeq", element.getReferenceSeq(), -1);
    for (int i = 0; i < element.getVariant().size(); i++)
      composeSequenceSequenceVariantComponent(t, "Sequence", "variant", element.getVariant().get(i), i);
    if (element.hasObservedSeqElement())
      composeString(t, "Sequence", "observedSeq", element.getObservedSeqElement(), -1);
    for (int i = 0; i < element.getQuality().size(); i++)
      composeSequenceSequenceQualityComponent(t, "Sequence", "quality", element.getQuality().get(i), i);
    if (element.hasReadCoverageElement())
      composeInteger(t, "Sequence", "readCoverage", element.getReadCoverageElement(), -1);
    for (int i = 0; i < element.getRepository().size(); i++)
      composeSequenceSequenceRepositoryComponent(t, "Sequence", "repository", element.getRepository().get(i), i);
    for (int i = 0; i < element.getPointer().size(); i++)
      composeReference(t, "Sequence", "pointer", element.getPointer().get(i), i);
    for (int i = 0; i < element.getStructureVariant().size(); i++)
      composeSequenceSequenceStructureVariantComponent(t, "Sequence", "structureVariant", element.getStructureVariant().get(i), i);
  }

  protected void composeSequenceSequenceReferenceSeqComponent(Complex parent, String parentType, String name, Sequence.SequenceReferenceSeqComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "referenceSeq", name, element, index);
    if (element.hasChromosome())
      composeCodeableConcept(t, "Sequence", "chromosome", element.getChromosome(), -1);
    if (element.hasGenomeBuildElement())
      composeString(t, "Sequence", "genomeBuild", element.getGenomeBuildElement(), -1);
    if (element.hasReferenceSeqId())
      composeCodeableConcept(t, "Sequence", "referenceSeqId", element.getReferenceSeqId(), -1);
    if (element.hasReferenceSeqPointer())
      composeReference(t, "Sequence", "referenceSeqPointer", element.getReferenceSeqPointer(), -1);
    if (element.hasReferenceSeqStringElement())
      composeString(t, "Sequence", "referenceSeqString", element.getReferenceSeqStringElement(), -1);
    if (element.hasStrandElement())
      composeInteger(t, "Sequence", "strand", element.getStrandElement(), -1);
    if (element.hasWindowStartElement())
      composeInteger(t, "Sequence", "windowStart", element.getWindowStartElement(), -1);
    if (element.hasWindowEndElement())
      composeInteger(t, "Sequence", "windowEnd", element.getWindowEndElement(), -1);
  }

  protected void composeSequenceSequenceVariantComponent(Complex parent, String parentType, String name, Sequence.SequenceVariantComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "variant", name, element, index);
    if (element.hasStartElement())
      composeInteger(t, "Sequence", "start", element.getStartElement(), -1);
    if (element.hasEndElement())
      composeInteger(t, "Sequence", "end", element.getEndElement(), -1);
    if (element.hasObservedAlleleElement())
      composeString(t, "Sequence", "observedAllele", element.getObservedAlleleElement(), -1);
    if (element.hasReferenceAlleleElement())
      composeString(t, "Sequence", "referenceAllele", element.getReferenceAlleleElement(), -1);
    if (element.hasCigarElement())
      composeString(t, "Sequence", "cigar", element.getCigarElement(), -1);
    if (element.hasVariantPointer())
      composeReference(t, "Sequence", "variantPointer", element.getVariantPointer(), -1);
  }

  protected void composeSequenceSequenceQualityComponent(Complex parent, String parentType, String name, Sequence.SequenceQualityComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "quality", name, element, index);
    if (element.hasTypeElement())
      composeEnum(t, "Sequence", "type", element.getTypeElement(), -1);
    if (element.hasStandardSequence())
      composeCodeableConcept(t, "Sequence", "standardSequence", element.getStandardSequence(), -1);
    if (element.hasStartElement())
      composeInteger(t, "Sequence", "start", element.getStartElement(), -1);
    if (element.hasEndElement())
      composeInteger(t, "Sequence", "end", element.getEndElement(), -1);
    if (element.hasScore())
      composeQuantity(t, "Sequence", "score", element.getScore(), -1);
    if (element.hasMethod())
      composeCodeableConcept(t, "Sequence", "method", element.getMethod(), -1);
    if (element.hasTruthTPElement())
      composeDecimal(t, "Sequence", "truthTP", element.getTruthTPElement(), -1);
    if (element.hasQueryTPElement())
      composeDecimal(t, "Sequence", "queryTP", element.getQueryTPElement(), -1);
    if (element.hasTruthFNElement())
      composeDecimal(t, "Sequence", "truthFN", element.getTruthFNElement(), -1);
    if (element.hasQueryFPElement())
      composeDecimal(t, "Sequence", "queryFP", element.getQueryFPElement(), -1);
    if (element.hasGtFPElement())
      composeDecimal(t, "Sequence", "gtFP", element.getGtFPElement(), -1);
    if (element.hasPrecisionElement())
      composeDecimal(t, "Sequence", "precision", element.getPrecisionElement(), -1);
    if (element.hasRecallElement())
      composeDecimal(t, "Sequence", "recall", element.getRecallElement(), -1);
    if (element.hasFScoreElement())
      composeDecimal(t, "Sequence", "fScore", element.getFScoreElement(), -1);
    if (element.hasRoc())
      composeSequenceSequenceQualityRocComponent(t, "Sequence", "roc", element.getRoc(), -1);
  }

  protected void composeSequenceSequenceQualityRocComponent(Complex parent, String parentType, String name, Sequence.SequenceQualityRocComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "roc", name, element, index);
    for (int i = 0; i < element.getScore().size(); i++)
      composeInteger(t, "Sequence", "score", element.getScore().get(i), i);
    for (int i = 0; i < element.getNumTP().size(); i++)
      composeInteger(t, "Sequence", "numTP", element.getNumTP().get(i), i);
    for (int i = 0; i < element.getNumFP().size(); i++)
      composeInteger(t, "Sequence", "numFP", element.getNumFP().get(i), i);
    for (int i = 0; i < element.getNumFN().size(); i++)
      composeInteger(t, "Sequence", "numFN", element.getNumFN().get(i), i);
    for (int i = 0; i < element.getPrecision().size(); i++)
      composeDecimal(t, "Sequence", "precision", element.getPrecision().get(i), i);
    for (int i = 0; i < element.getSensitivity().size(); i++)
      composeDecimal(t, "Sequence", "sensitivity", element.getSensitivity().get(i), i);
    for (int i = 0; i < element.getFMeasure().size(); i++)
      composeDecimal(t, "Sequence", "fMeasure", element.getFMeasure().get(i), i);
  }

  protected void composeSequenceSequenceRepositoryComponent(Complex parent, String parentType, String name, Sequence.SequenceRepositoryComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "repository", name, element, index);
    if (element.hasTypeElement())
      composeEnum(t, "Sequence", "type", element.getTypeElement(), -1);
    if (element.hasUrlElement())
      composeUri(t, "Sequence", "url", element.getUrlElement(), -1);
    if (element.hasNameElement())
      composeString(t, "Sequence", "name", element.getNameElement(), -1);
    if (element.hasDatasetIdElement())
      composeString(t, "Sequence", "datasetId", element.getDatasetIdElement(), -1);
    if (element.hasVariantsetIdElement())
      composeString(t, "Sequence", "variantsetId", element.getVariantsetIdElement(), -1);
    if (element.hasReadsetIdElement())
      composeString(t, "Sequence", "readsetId", element.getReadsetIdElement(), -1);
  }

  protected void composeSequenceSequenceStructureVariantComponent(Complex parent, String parentType, String name, Sequence.SequenceStructureVariantComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "structureVariant", name, element, index);
    if (element.hasPrecisionElement())
      composeString(t, "Sequence", "precision", element.getPrecisionElement(), -1);
    if (element.hasReportedaCGHRatioElement())
      composeDecimal(t, "Sequence", "reportedaCGHRatio", element.getReportedaCGHRatioElement(), -1);
    if (element.hasLengthElement())
      composeInteger(t, "Sequence", "length", element.getLengthElement(), -1);
    if (element.hasOuter())
      composeSequenceSequenceStructureVariantOuterComponent(t, "Sequence", "outer", element.getOuter(), -1);
    if (element.hasInner())
      composeSequenceSequenceStructureVariantInnerComponent(t, "Sequence", "inner", element.getInner(), -1);
  }

  protected void composeSequenceSequenceStructureVariantOuterComponent(Complex parent, String parentType, String name, Sequence.SequenceStructureVariantOuterComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "outer", name, element, index);
    if (element.hasStartElement())
      composeInteger(t, "Sequence", "start", element.getStartElement(), -1);
    if (element.hasEndElement())
      composeInteger(t, "Sequence", "end", element.getEndElement(), -1);
  }

  protected void composeSequenceSequenceStructureVariantInnerComponent(Complex parent, String parentType, String name, Sequence.SequenceStructureVariantInnerComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "inner", name, element, index);
    if (element.hasStartElement())
      composeInteger(t, "Sequence", "start", element.getStartElement(), -1);
    if (element.hasEndElement())
      composeInteger(t, "Sequence", "end", element.getEndElement(), -1);
  }

  protected void composeServiceDefinition(Complex parent, String parentType, String name, ServiceDefinition element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "ServiceDefinition", name, element, index);
    if (element.hasUrlElement())
      composeUri(t, "ServiceDefinition", "url", element.getUrlElement(), -1);
    for (int i = 0; i < element.getIdentifier().size(); i++)
      composeIdentifier(t, "ServiceDefinition", "identifier", element.getIdentifier().get(i), i);
    if (element.hasVersionElement())
      composeString(t, "ServiceDefinition", "version", element.getVersionElement(), -1);
    if (element.hasNameElement())
      composeString(t, "ServiceDefinition", "name", element.getNameElement(), -1);
    if (element.hasTitleElement())
      composeString(t, "ServiceDefinition", "title", element.getTitleElement(), -1);
    if (element.hasStatusElement())
      composeEnum(t, "ServiceDefinition", "status", element.getStatusElement(), -1);
    if (element.hasExperimentalElement())
      composeBoolean(t, "ServiceDefinition", "experimental", element.getExperimentalElement(), -1);
    if (element.hasDateElement())
      composeDateTime(t, "ServiceDefinition", "date", element.getDateElement(), -1);
    if (element.hasPublisherElement())
      composeString(t, "ServiceDefinition", "publisher", element.getPublisherElement(), -1);
    if (element.hasDescriptionElement())
      composeMarkdown(t, "ServiceDefinition", "description", element.getDescriptionElement(), -1);
    if (element.hasPurposeElement())
      composeMarkdown(t, "ServiceDefinition", "purpose", element.getPurposeElement(), -1);
    if (element.hasUsageElement())
      composeString(t, "ServiceDefinition", "usage", element.getUsageElement(), -1);
    if (element.hasApprovalDateElement())
      composeDate(t, "ServiceDefinition", "approvalDate", element.getApprovalDateElement(), -1);
    if (element.hasLastReviewDateElement())
      composeDate(t, "ServiceDefinition", "lastReviewDate", element.getLastReviewDateElement(), -1);
    if (element.hasEffectivePeriod())
      composePeriod(t, "ServiceDefinition", "effectivePeriod", element.getEffectivePeriod(), -1);
    for (int i = 0; i < element.getUseContext().size(); i++)
      composeUsageContext(t, "ServiceDefinition", "useContext", element.getUseContext().get(i), i);
    for (int i = 0; i < element.getJurisdiction().size(); i++)
      composeCodeableConcept(t, "ServiceDefinition", "jurisdiction", element.getJurisdiction().get(i), i);
    for (int i = 0; i < element.getTopic().size(); i++)
      composeCodeableConcept(t, "ServiceDefinition", "topic", element.getTopic().get(i), i);
    for (int i = 0; i < element.getContributor().size(); i++)
      composeContributor(t, "ServiceDefinition", "contributor", element.getContributor().get(i), i);
    for (int i = 0; i < element.getContact().size(); i++)
      composeContactDetail(t, "ServiceDefinition", "contact", element.getContact().get(i), i);
    if (element.hasCopyrightElement())
      composeMarkdown(t, "ServiceDefinition", "copyright", element.getCopyrightElement(), -1);
    for (int i = 0; i < element.getRelatedArtifact().size(); i++)
      composeRelatedArtifact(t, "ServiceDefinition", "relatedArtifact", element.getRelatedArtifact().get(i), i);
    for (int i = 0; i < element.getTrigger().size(); i++)
      composeTriggerDefinition(t, "ServiceDefinition", "trigger", element.getTrigger().get(i), i);
    for (int i = 0; i < element.getDataRequirement().size(); i++)
      composeDataRequirement(t, "ServiceDefinition", "dataRequirement", element.getDataRequirement().get(i), i);
    if (element.hasOperationDefinition())
      composeReference(t, "ServiceDefinition", "operationDefinition", element.getOperationDefinition(), -1);
  }

  protected void composeSlot(Complex parent, String parentType, String name, Slot element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "Slot", name, element, index);
    for (int i = 0; i < element.getIdentifier().size(); i++)
      composeIdentifier(t, "Slot", "identifier", element.getIdentifier().get(i), i);
    if (element.hasServiceCategory())
      composeCodeableConcept(t, "Slot", "serviceCategory", element.getServiceCategory(), -1);
    for (int i = 0; i < element.getServiceType().size(); i++)
      composeCodeableConcept(t, "Slot", "serviceType", element.getServiceType().get(i), i);
    for (int i = 0; i < element.getSpecialty().size(); i++)
      composeCodeableConcept(t, "Slot", "specialty", element.getSpecialty().get(i), i);
    if (element.hasAppointmentType())
      composeCodeableConcept(t, "Slot", "appointmentType", element.getAppointmentType(), -1);
    if (element.hasSchedule())
      composeReference(t, "Slot", "schedule", element.getSchedule(), -1);
    if (element.hasStatusElement())
      composeEnum(t, "Slot", "status", element.getStatusElement(), -1);
    if (element.hasStartElement())
      composeInstant(t, "Slot", "start", element.getStartElement(), -1);
    if (element.hasEndElement())
      composeInstant(t, "Slot", "end", element.getEndElement(), -1);
    if (element.hasOverbookedElement())
      composeBoolean(t, "Slot", "overbooked", element.getOverbookedElement(), -1);
    if (element.hasCommentElement())
      composeString(t, "Slot", "comment", element.getCommentElement(), -1);
  }

  protected void composeSpecimen(Complex parent, String parentType, String name, Specimen element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "Specimen", name, element, index);
    for (int i = 0; i < element.getIdentifier().size(); i++)
      composeIdentifier(t, "Specimen", "identifier", element.getIdentifier().get(i), i);
    if (element.hasAccessionIdentifier())
      composeIdentifier(t, "Specimen", "accessionIdentifier", element.getAccessionIdentifier(), -1);
    if (element.hasStatusElement())
      composeEnum(t, "Specimen", "status", element.getStatusElement(), -1);
    if (element.hasType())
      composeCodeableConcept(t, "Specimen", "type", element.getType(), -1);
    if (element.hasSubject())
      composeReference(t, "Specimen", "subject", element.getSubject(), -1);
    if (element.hasReceivedTimeElement())
      composeDateTime(t, "Specimen", "receivedTime", element.getReceivedTimeElement(), -1);
    for (int i = 0; i < element.getParent().size(); i++)
      composeReference(t, "Specimen", "parent", element.getParent().get(i), i);
    for (int i = 0; i < element.getRequest().size(); i++)
      composeReference(t, "Specimen", "request", element.getRequest().get(i), i);
    if (element.hasCollection())
      composeSpecimenSpecimenCollectionComponent(t, "Specimen", "collection", element.getCollection(), -1);
    for (int i = 0; i < element.getProcessing().size(); i++)
      composeSpecimenSpecimenProcessingComponent(t, "Specimen", "processing", element.getProcessing().get(i), i);
    for (int i = 0; i < element.getContainer().size(); i++)
      composeSpecimenSpecimenContainerComponent(t, "Specimen", "container", element.getContainer().get(i), i);
    for (int i = 0; i < element.getNote().size(); i++)
      composeAnnotation(t, "Specimen", "note", element.getNote().get(i), i);
  }

  protected void composeSpecimenSpecimenCollectionComponent(Complex parent, String parentType, String name, Specimen.SpecimenCollectionComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "collection", name, element, index);
    if (element.hasCollector())
      composeReference(t, "Specimen", "collector", element.getCollector(), -1);
    if (element.hasCollected())
      composeType(t, "Specimen", "collected", element.getCollected(), -1);
    if (element.hasQuantity())
      composeQuantity(t, "Specimen", "quantity", element.getQuantity(), -1);
    if (element.hasMethod())
      composeCodeableConcept(t, "Specimen", "method", element.getMethod(), -1);
    if (element.hasBodySite())
      composeCodeableConcept(t, "Specimen", "bodySite", element.getBodySite(), -1);
  }

  protected void composeSpecimenSpecimenProcessingComponent(Complex parent, String parentType, String name, Specimen.SpecimenProcessingComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "processing", name, element, index);
    if (element.hasDescriptionElement())
      composeString(t, "Specimen", "description", element.getDescriptionElement(), -1);
    if (element.hasProcedure())
      composeCodeableConcept(t, "Specimen", "procedure", element.getProcedure(), -1);
    for (int i = 0; i < element.getAdditive().size(); i++)
      composeReference(t, "Specimen", "additive", element.getAdditive().get(i), i);
    if (element.hasTime())
      composeType(t, "Specimen", "time", element.getTime(), -1);
  }

  protected void composeSpecimenSpecimenContainerComponent(Complex parent, String parentType, String name, Specimen.SpecimenContainerComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "container", name, element, index);
    for (int i = 0; i < element.getIdentifier().size(); i++)
      composeIdentifier(t, "Specimen", "identifier", element.getIdentifier().get(i), i);
    if (element.hasDescriptionElement())
      composeString(t, "Specimen", "description", element.getDescriptionElement(), -1);
    if (element.hasType())
      composeCodeableConcept(t, "Specimen", "type", element.getType(), -1);
    if (element.hasCapacity())
      composeQuantity(t, "Specimen", "capacity", element.getCapacity(), -1);
    if (element.hasSpecimenQuantity())
      composeQuantity(t, "Specimen", "specimenQuantity", element.getSpecimenQuantity(), -1);
    if (element.hasAdditive())
      composeType(t, "Specimen", "additive", element.getAdditive(), -1);
  }

  protected void composeSpecimenDefinition(Complex parent, String parentType, String name, SpecimenDefinition element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "SpecimenDefinition", name, element, index);
    if (element.hasIdentifier())
      composeIdentifier(t, "SpecimenDefinition", "identifier", element.getIdentifier(), -1);
    if (element.hasTypeCollected())
      composeCodeableConcept(t, "SpecimenDefinition", "typeCollected", element.getTypeCollected(), -1);
    if (element.hasPatientPreparationElement())
      composeString(t, "SpecimenDefinition", "patientPreparation", element.getPatientPreparationElement(), -1);
    if (element.hasTimeAspectElement())
      composeString(t, "SpecimenDefinition", "timeAspect", element.getTimeAspectElement(), -1);
    for (int i = 0; i < element.getCollection().size(); i++)
      composeCodeableConcept(t, "SpecimenDefinition", "collection", element.getCollection().get(i), i);
    for (int i = 0; i < element.getSpecimenToLab().size(); i++)
      composeSpecimenDefinitionSpecimenDefinitionSpecimenToLabComponent(t, "SpecimenDefinition", "specimenToLab", element.getSpecimenToLab().get(i), i);
  }

  protected void composeSpecimenDefinitionSpecimenDefinitionSpecimenToLabComponent(Complex parent, String parentType, String name, SpecimenDefinition.SpecimenDefinitionSpecimenToLabComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "specimenToLab", name, element, index);
    if (element.hasIsDerivedElement())
      composeBoolean(t, "SpecimenDefinition", "isDerived", element.getIsDerivedElement(), -1);
    if (element.hasType())
      composeCodeableConcept(t, "SpecimenDefinition", "type", element.getType(), -1);
    if (element.hasPreferenceElement())
      composeEnum(t, "SpecimenDefinition", "preference", element.getPreferenceElement(), -1);
    if (element.hasContainerMaterial())
      composeCodeableConcept(t, "SpecimenDefinition", "containerMaterial", element.getContainerMaterial(), -1);
    if (element.hasContainerType())
      composeCodeableConcept(t, "SpecimenDefinition", "containerType", element.getContainerType(), -1);
    if (element.hasContainerCap())
      composeCodeableConcept(t, "SpecimenDefinition", "containerCap", element.getContainerCap(), -1);
    if (element.hasContainerDescriptionElement())
      composeString(t, "SpecimenDefinition", "containerDescription", element.getContainerDescriptionElement(), -1);
    if (element.hasContainerCapacity())
      composeQuantity(t, "SpecimenDefinition", "containerCapacity", element.getContainerCapacity(), -1);
    if (element.hasContainerMinimumVolume())
      composeQuantity(t, "SpecimenDefinition", "containerMinimumVolume", element.getContainerMinimumVolume(), -1);
    for (int i = 0; i < element.getContainerAdditive().size(); i++)
      composeSpecimenDefinitionSpecimenDefinitionSpecimenToLabContainerAdditiveComponent(t, "SpecimenDefinition", "containerAdditive", element.getContainerAdditive().get(i), i);
    if (element.hasContainerPreparationElement())
      composeString(t, "SpecimenDefinition", "containerPreparation", element.getContainerPreparationElement(), -1);
    if (element.hasRequirementElement())
      composeString(t, "SpecimenDefinition", "requirement", element.getRequirementElement(), -1);
    if (element.hasRetentionTime())
      composeDuration(t, "SpecimenDefinition", "retentionTime", element.getRetentionTime(), -1);
    for (int i = 0; i < element.getRejectionCriterion().size(); i++)
      composeCodeableConcept(t, "SpecimenDefinition", "rejectionCriterion", element.getRejectionCriterion().get(i), i);
    for (int i = 0; i < element.getHandling().size(); i++)
      composeSpecimenDefinitionSpecimenDefinitionSpecimenToLabHandlingComponent(t, "SpecimenDefinition", "handling", element.getHandling().get(i), i);
  }

  protected void composeSpecimenDefinitionSpecimenDefinitionSpecimenToLabContainerAdditiveComponent(Complex parent, String parentType, String name, SpecimenDefinition.SpecimenDefinitionSpecimenToLabContainerAdditiveComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "containerAdditive", name, element, index);
    if (element.hasAdditive())
      composeType(t, "SpecimenDefinition", "additive", element.getAdditive(), -1);
  }

  protected void composeSpecimenDefinitionSpecimenDefinitionSpecimenToLabHandlingComponent(Complex parent, String parentType, String name, SpecimenDefinition.SpecimenDefinitionSpecimenToLabHandlingComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "handling", name, element, index);
    if (element.hasConditionSet())
      composeCodeableConcept(t, "SpecimenDefinition", "conditionSet", element.getConditionSet(), -1);
    if (element.hasTempRange())
      composeRange(t, "SpecimenDefinition", "tempRange", element.getTempRange(), -1);
    if (element.hasMaxDuration())
      composeDuration(t, "SpecimenDefinition", "maxDuration", element.getMaxDuration(), -1);
    if (element.hasLightExposureElement())
      composeString(t, "SpecimenDefinition", "lightExposure", element.getLightExposureElement(), -1);
    if (element.hasInstructionElement())
      composeString(t, "SpecimenDefinition", "instruction", element.getInstructionElement(), -1);
  }

  protected void composeStructureDefinition(Complex parent, String parentType, String name, StructureDefinition element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "StructureDefinition", name, element, index);
    if (element.hasUrlElement())
      composeUri(t, "StructureDefinition", "url", element.getUrlElement(), -1);
    for (int i = 0; i < element.getIdentifier().size(); i++)
      composeIdentifier(t, "StructureDefinition", "identifier", element.getIdentifier().get(i), i);
    if (element.hasVersionElement())
      composeString(t, "StructureDefinition", "version", element.getVersionElement(), -1);
    if (element.hasNameElement())
      composeString(t, "StructureDefinition", "name", element.getNameElement(), -1);
    if (element.hasTitleElement())
      composeString(t, "StructureDefinition", "title", element.getTitleElement(), -1);
    if (element.hasStatusElement())
      composeEnum(t, "StructureDefinition", "status", element.getStatusElement(), -1);
    if (element.hasExperimentalElement())
      composeBoolean(t, "StructureDefinition", "experimental", element.getExperimentalElement(), -1);
    if (element.hasDateElement())
      composeDateTime(t, "StructureDefinition", "date", element.getDateElement(), -1);
    if (element.hasPublisherElement())
      composeString(t, "StructureDefinition", "publisher", element.getPublisherElement(), -1);
    for (int i = 0; i < element.getContact().size(); i++)
      composeContactDetail(t, "StructureDefinition", "contact", element.getContact().get(i), i);
    if (element.hasDescriptionElement())
      composeMarkdown(t, "StructureDefinition", "description", element.getDescriptionElement(), -1);
    for (int i = 0; i < element.getUseContext().size(); i++)
      composeUsageContext(t, "StructureDefinition", "useContext", element.getUseContext().get(i), i);
    for (int i = 0; i < element.getJurisdiction().size(); i++)
      composeCodeableConcept(t, "StructureDefinition", "jurisdiction", element.getJurisdiction().get(i), i);
    if (element.hasPurposeElement())
      composeMarkdown(t, "StructureDefinition", "purpose", element.getPurposeElement(), -1);
    if (element.hasCopyrightElement())
      composeMarkdown(t, "StructureDefinition", "copyright", element.getCopyrightElement(), -1);
    for (int i = 0; i < element.getKeyword().size(); i++)
      composeCoding(t, "StructureDefinition", "keyword", element.getKeyword().get(i), i);
    if (element.hasFhirVersionElement())
      composeId(t, "StructureDefinition", "fhirVersion", element.getFhirVersionElement(), -1);
    for (int i = 0; i < element.getMapping().size(); i++)
      composeStructureDefinitionStructureDefinitionMappingComponent(t, "StructureDefinition", "mapping", element.getMapping().get(i), i);
    if (element.hasKindElement())
      composeEnum(t, "StructureDefinition", "kind", element.getKindElement(), -1);
    if (element.hasAbstractElement())
      composeBoolean(t, "StructureDefinition", "abstract", element.getAbstractElement(), -1);
    if (element.hasContextTypeElement())
      composeEnum(t, "StructureDefinition", "contextType", element.getContextTypeElement(), -1);
    for (int i = 0; i < element.getContext().size(); i++)
      composeString(t, "StructureDefinition", "context", element.getContext().get(i), i);
    for (int i = 0; i < element.getContextInvariant().size(); i++)
      composeString(t, "StructureDefinition", "contextInvariant", element.getContextInvariant().get(i), i);
    if (element.hasTypeElement())
      composeCode(t, "StructureDefinition", "type", element.getTypeElement(), -1);
    if (element.hasBaseDefinitionElement())
      composeUri(t, "StructureDefinition", "baseDefinition", element.getBaseDefinitionElement(), -1);
    if (element.hasDerivationElement())
      composeEnum(t, "StructureDefinition", "derivation", element.getDerivationElement(), -1);
    if (element.hasSnapshot())
      composeStructureDefinitionStructureDefinitionSnapshotComponent(t, "StructureDefinition", "snapshot", element.getSnapshot(), -1);
    if (element.hasDifferential())
      composeStructureDefinitionStructureDefinitionDifferentialComponent(t, "StructureDefinition", "differential", element.getDifferential(), -1);
  }

  protected void composeStructureDefinitionStructureDefinitionMappingComponent(Complex parent, String parentType, String name, StructureDefinition.StructureDefinitionMappingComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "mapping", name, element, index);
    if (element.hasIdentityElement())
      composeId(t, "StructureDefinition", "identity", element.getIdentityElement(), -1);
    if (element.hasUriElement())
      composeUri(t, "StructureDefinition", "uri", element.getUriElement(), -1);
    if (element.hasNameElement())
      composeString(t, "StructureDefinition", "name", element.getNameElement(), -1);
    if (element.hasCommentElement())
      composeString(t, "StructureDefinition", "comment", element.getCommentElement(), -1);
  }

  protected void composeStructureDefinitionStructureDefinitionSnapshotComponent(Complex parent, String parentType, String name, StructureDefinition.StructureDefinitionSnapshotComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "snapshot", name, element, index);
    for (int i = 0; i < element.getElement().size(); i++)
      composeElementDefinition(t, "StructureDefinition", "element", element.getElement().get(i), i);
  }

  protected void composeStructureDefinitionStructureDefinitionDifferentialComponent(Complex parent, String parentType, String name, StructureDefinition.StructureDefinitionDifferentialComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "differential", name, element, index);
    for (int i = 0; i < element.getElement().size(); i++)
      composeElementDefinition(t, "StructureDefinition", "element", element.getElement().get(i), i);
  }

  protected void composeStructureMap(Complex parent, String parentType, String name, StructureMap element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "StructureMap", name, element, index);
    if (element.hasUrlElement())
      composeUri(t, "StructureMap", "url", element.getUrlElement(), -1);
    for (int i = 0; i < element.getIdentifier().size(); i++)
      composeIdentifier(t, "StructureMap", "identifier", element.getIdentifier().get(i), i);
    if (element.hasVersionElement())
      composeString(t, "StructureMap", "version", element.getVersionElement(), -1);
    if (element.hasNameElement())
      composeString(t, "StructureMap", "name", element.getNameElement(), -1);
    if (element.hasTitleElement())
      composeString(t, "StructureMap", "title", element.getTitleElement(), -1);
    if (element.hasStatusElement())
      composeEnum(t, "StructureMap", "status", element.getStatusElement(), -1);
    if (element.hasExperimentalElement())
      composeBoolean(t, "StructureMap", "experimental", element.getExperimentalElement(), -1);
    if (element.hasDateElement())
      composeDateTime(t, "StructureMap", "date", element.getDateElement(), -1);
    if (element.hasPublisherElement())
      composeString(t, "StructureMap", "publisher", element.getPublisherElement(), -1);
    for (int i = 0; i < element.getContact().size(); i++)
      composeContactDetail(t, "StructureMap", "contact", element.getContact().get(i), i);
    if (element.hasDescriptionElement())
      composeMarkdown(t, "StructureMap", "description", element.getDescriptionElement(), -1);
    for (int i = 0; i < element.getUseContext().size(); i++)
      composeUsageContext(t, "StructureMap", "useContext", element.getUseContext().get(i), i);
    for (int i = 0; i < element.getJurisdiction().size(); i++)
      composeCodeableConcept(t, "StructureMap", "jurisdiction", element.getJurisdiction().get(i), i);
    if (element.hasPurposeElement())
      composeMarkdown(t, "StructureMap", "purpose", element.getPurposeElement(), -1);
    if (element.hasCopyrightElement())
      composeMarkdown(t, "StructureMap", "copyright", element.getCopyrightElement(), -1);
    for (int i = 0; i < element.getStructure().size(); i++)
      composeStructureMapStructureMapStructureComponent(t, "StructureMap", "structure", element.getStructure().get(i), i);
    for (int i = 0; i < element.getImport().size(); i++)
      composeUri(t, "StructureMap", "import", element.getImport().get(i), i);
    for (int i = 0; i < element.getGroup().size(); i++)
      composeStructureMapStructureMapGroupComponent(t, "StructureMap", "group", element.getGroup().get(i), i);
  }

  protected void composeStructureMapStructureMapStructureComponent(Complex parent, String parentType, String name, StructureMap.StructureMapStructureComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "structure", name, element, index);
    if (element.hasUrlElement())
      composeUri(t, "StructureMap", "url", element.getUrlElement(), -1);
    if (element.hasModeElement())
      composeEnum(t, "StructureMap", "mode", element.getModeElement(), -1);
    if (element.hasAliasElement())
      composeString(t, "StructureMap", "alias", element.getAliasElement(), -1);
    if (element.hasDocumentationElement())
      composeString(t, "StructureMap", "documentation", element.getDocumentationElement(), -1);
  }

  protected void composeStructureMapStructureMapGroupComponent(Complex parent, String parentType, String name, StructureMap.StructureMapGroupComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "group", name, element, index);
    if (element.hasNameElement())
      composeId(t, "StructureMap", "name", element.getNameElement(), -1);
    if (element.hasExtendsElement())
      composeId(t, "StructureMap", "extends", element.getExtendsElement(), -1);
    if (element.hasTypeModeElement())
      composeEnum(t, "StructureMap", "typeMode", element.getTypeModeElement(), -1);
    if (element.hasDocumentationElement())
      composeString(t, "StructureMap", "documentation", element.getDocumentationElement(), -1);
    for (int i = 0; i < element.getInput().size(); i++)
      composeStructureMapStructureMapGroupInputComponent(t, "StructureMap", "input", element.getInput().get(i), i);
    for (int i = 0; i < element.getRule().size(); i++)
      composeStructureMapStructureMapGroupRuleComponent(t, "StructureMap", "rule", element.getRule().get(i), i);
  }

  protected void composeStructureMapStructureMapGroupInputComponent(Complex parent, String parentType, String name, StructureMap.StructureMapGroupInputComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "input", name, element, index);
    if (element.hasNameElement())
      composeId(t, "StructureMap", "name", element.getNameElement(), -1);
    if (element.hasTypeElement())
      composeString(t, "StructureMap", "type", element.getTypeElement(), -1);
    if (element.hasModeElement())
      composeEnum(t, "StructureMap", "mode", element.getModeElement(), -1);
    if (element.hasDocumentationElement())
      composeString(t, "StructureMap", "documentation", element.getDocumentationElement(), -1);
  }

  protected void composeStructureMapStructureMapGroupRuleComponent(Complex parent, String parentType, String name, StructureMap.StructureMapGroupRuleComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "rule", name, element, index);
    if (element.hasNameElement())
      composeId(t, "StructureMap", "name", element.getNameElement(), -1);
    for (int i = 0; i < element.getSource().size(); i++)
      composeStructureMapStructureMapGroupRuleSourceComponent(t, "StructureMap", "source", element.getSource().get(i), i);
    for (int i = 0; i < element.getTarget().size(); i++)
      composeStructureMapStructureMapGroupRuleTargetComponent(t, "StructureMap", "target", element.getTarget().get(i), i);
    for (int i = 0; i < element.getRule().size(); i++)
      composeStructureMapStructureMapGroupRuleComponent(t, "StructureMap", "rule", element.getRule().get(i), i);
    for (int i = 0; i < element.getDependent().size(); i++)
      composeStructureMapStructureMapGroupRuleDependentComponent(t, "StructureMap", "dependent", element.getDependent().get(i), i);
    if (element.hasDocumentationElement())
      composeString(t, "StructureMap", "documentation", element.getDocumentationElement(), -1);
  }

  protected void composeStructureMapStructureMapGroupRuleSourceComponent(Complex parent, String parentType, String name, StructureMap.StructureMapGroupRuleSourceComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "source", name, element, index);
    if (element.hasContextElement())
      composeId(t, "StructureMap", "context", element.getContextElement(), -1);
    if (element.hasMinElement())
      composeInteger(t, "StructureMap", "min", element.getMinElement(), -1);
    if (element.hasMaxElement())
      composeString(t, "StructureMap", "max", element.getMaxElement(), -1);
    if (element.hasTypeElement())
      composeString(t, "StructureMap", "type", element.getTypeElement(), -1);
    if (element.hasDefaultValue())
      composeType(t, "StructureMap", "defaultValue", element.getDefaultValue(), -1);
    if (element.hasElementElement())
      composeString(t, "StructureMap", "element", element.getElementElement(), -1);
    if (element.hasListModeElement())
      composeEnum(t, "StructureMap", "listMode", element.getListModeElement(), -1);
    if (element.hasVariableElement())
      composeId(t, "StructureMap", "variable", element.getVariableElement(), -1);
    if (element.hasConditionElement())
      composeString(t, "StructureMap", "condition", element.getConditionElement(), -1);
    if (element.hasCheckElement())
      composeString(t, "StructureMap", "check", element.getCheckElement(), -1);
  }

  protected void composeStructureMapStructureMapGroupRuleTargetComponent(Complex parent, String parentType, String name, StructureMap.StructureMapGroupRuleTargetComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "target", name, element, index);
    if (element.hasContextElement())
      composeId(t, "StructureMap", "context", element.getContextElement(), -1);
    if (element.hasContextTypeElement())
      composeEnum(t, "StructureMap", "contextType", element.getContextTypeElement(), -1);
    if (element.hasElementElement())
      composeString(t, "StructureMap", "element", element.getElementElement(), -1);
    if (element.hasVariableElement())
      composeId(t, "StructureMap", "variable", element.getVariableElement(), -1);
    for (int i = 0; i < element.getListMode().size(); i++)
      composeEnum(t, "StructureMap", "listMode", element.getListMode().get(i), i);
    if (element.hasListRuleIdElement())
      composeId(t, "StructureMap", "listRuleId", element.getListRuleIdElement(), -1);
    if (element.hasTransformElement())
      composeEnum(t, "StructureMap", "transform", element.getTransformElement(), -1);
    for (int i = 0; i < element.getParameter().size(); i++)
      composeStructureMapStructureMapGroupRuleTargetParameterComponent(t, "StructureMap", "parameter", element.getParameter().get(i), i);
  }

  protected void composeStructureMapStructureMapGroupRuleTargetParameterComponent(Complex parent, String parentType, String name, StructureMap.StructureMapGroupRuleTargetParameterComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "parameter", name, element, index);
    if (element.hasValue())
      composeType(t, "StructureMap", "value", element.getValue(), -1);
  }

  protected void composeStructureMapStructureMapGroupRuleDependentComponent(Complex parent, String parentType, String name, StructureMap.StructureMapGroupRuleDependentComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "dependent", name, element, index);
    if (element.hasNameElement())
      composeId(t, "StructureMap", "name", element.getNameElement(), -1);
    for (int i = 0; i < element.getVariable().size(); i++)
      composeString(t, "StructureMap", "variable", element.getVariable().get(i), i);
  }

  protected void composeSubscription(Complex parent, String parentType, String name, Subscription element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "Subscription", name, element, index);
    if (element.hasStatusElement())
      composeEnum(t, "Subscription", "status", element.getStatusElement(), -1);
    for (int i = 0; i < element.getContact().size(); i++)
      composeContactPoint(t, "Subscription", "contact", element.getContact().get(i), i);
    if (element.hasEndElement())
      composeInstant(t, "Subscription", "end", element.getEndElement(), -1);
    if (element.hasReasonElement())
      composeString(t, "Subscription", "reason", element.getReasonElement(), -1);
    if (element.hasCriteriaElement())
      composeString(t, "Subscription", "criteria", element.getCriteriaElement(), -1);
    if (element.hasErrorElement())
      composeString(t, "Subscription", "error", element.getErrorElement(), -1);
    if (element.hasChannel())
      composeSubscriptionSubscriptionChannelComponent(t, "Subscription", "channel", element.getChannel(), -1);
    for (int i = 0; i < element.getTag().size(); i++)
      composeCoding(t, "Subscription", "tag", element.getTag().get(i), i);
  }

  protected void composeSubscriptionSubscriptionChannelComponent(Complex parent, String parentType, String name, Subscription.SubscriptionChannelComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "channel", name, element, index);
    if (element.hasTypeElement())
      composeEnum(t, "Subscription", "type", element.getTypeElement(), -1);
    if (element.hasEndpointElement())
      composeUri(t, "Subscription", "endpoint", element.getEndpointElement(), -1);
    if (element.hasPayloadElement())
      composeString(t, "Subscription", "payload", element.getPayloadElement(), -1);
    for (int i = 0; i < element.getHeader().size(); i++)
      composeString(t, "Subscription", "header", element.getHeader().get(i), i);
  }

  protected void composeSubstance(Complex parent, String parentType, String name, Substance element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "Substance", name, element, index);
    for (int i = 0; i < element.getIdentifier().size(); i++)
      composeIdentifier(t, "Substance", "identifier", element.getIdentifier().get(i), i);
    if (element.hasStatusElement())
      composeEnum(t, "Substance", "status", element.getStatusElement(), -1);
    for (int i = 0; i < element.getCategory().size(); i++)
      composeCodeableConcept(t, "Substance", "category", element.getCategory().get(i), i);
    if (element.hasCode())
      composeCodeableConcept(t, "Substance", "code", element.getCode(), -1);
    if (element.hasDescriptionElement())
      composeString(t, "Substance", "description", element.getDescriptionElement(), -1);
    for (int i = 0; i < element.getInstance().size(); i++)
      composeSubstanceSubstanceInstanceComponent(t, "Substance", "instance", element.getInstance().get(i), i);
    for (int i = 0; i < element.getIngredient().size(); i++)
      composeSubstanceSubstanceIngredientComponent(t, "Substance", "ingredient", element.getIngredient().get(i), i);
  }

  protected void composeSubstanceSubstanceInstanceComponent(Complex parent, String parentType, String name, Substance.SubstanceInstanceComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "instance", name, element, index);
    if (element.hasIdentifier())
      composeIdentifier(t, "Substance", "identifier", element.getIdentifier(), -1);
    if (element.hasExpiryElement())
      composeDateTime(t, "Substance", "expiry", element.getExpiryElement(), -1);
    if (element.hasQuantity())
      composeQuantity(t, "Substance", "quantity", element.getQuantity(), -1);
  }

  protected void composeSubstanceSubstanceIngredientComponent(Complex parent, String parentType, String name, Substance.SubstanceIngredientComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "ingredient", name, element, index);
    if (element.hasQuantity())
      composeRatio(t, "Substance", "quantity", element.getQuantity(), -1);
    if (element.hasSubstance())
      composeType(t, "Substance", "substance", element.getSubstance(), -1);
  }

  protected void composeSupplyDelivery(Complex parent, String parentType, String name, SupplyDelivery element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "SupplyDelivery", name, element, index);
    if (element.hasIdentifier())
      composeIdentifier(t, "SupplyDelivery", "identifier", element.getIdentifier(), -1);
    for (int i = 0; i < element.getBasedOn().size(); i++)
      composeReference(t, "SupplyDelivery", "basedOn", element.getBasedOn().get(i), i);
    for (int i = 0; i < element.getPartOf().size(); i++)
      composeReference(t, "SupplyDelivery", "partOf", element.getPartOf().get(i), i);
    if (element.hasStatusElement())
      composeEnum(t, "SupplyDelivery", "status", element.getStatusElement(), -1);
    if (element.hasPatient())
      composeReference(t, "SupplyDelivery", "patient", element.getPatient(), -1);
    if (element.hasType())
      composeCodeableConcept(t, "SupplyDelivery", "type", element.getType(), -1);
    if (element.hasSuppliedItem())
      composeSupplyDeliverySupplyDeliverySuppliedItemComponent(t, "SupplyDelivery", "suppliedItem", element.getSuppliedItem(), -1);
    if (element.hasOccurrence())
      composeType(t, "SupplyDelivery", "occurrence", element.getOccurrence(), -1);
    if (element.hasSupplier())
      composeReference(t, "SupplyDelivery", "supplier", element.getSupplier(), -1);
    if (element.hasDestination())
      composeReference(t, "SupplyDelivery", "destination", element.getDestination(), -1);
    for (int i = 0; i < element.getReceiver().size(); i++)
      composeReference(t, "SupplyDelivery", "receiver", element.getReceiver().get(i), i);
  }

  protected void composeSupplyDeliverySupplyDeliverySuppliedItemComponent(Complex parent, String parentType, String name, SupplyDelivery.SupplyDeliverySuppliedItemComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "suppliedItem", name, element, index);
    if (element.hasQuantity())
      composeQuantity(t, "SupplyDelivery", "quantity", element.getQuantity(), -1);
    if (element.hasItem())
      composeType(t, "SupplyDelivery", "item", element.getItem(), -1);
  }

  protected void composeSupplyRequest(Complex parent, String parentType, String name, SupplyRequest element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "SupplyRequest", name, element, index);
    if (element.hasIdentifier())
      composeIdentifier(t, "SupplyRequest", "identifier", element.getIdentifier(), -1);
    if (element.hasStatusElement())
      composeEnum(t, "SupplyRequest", "status", element.getStatusElement(), -1);
    if (element.hasCategory())
      composeCodeableConcept(t, "SupplyRequest", "category", element.getCategory(), -1);
    if (element.hasPriorityElement())
      composeEnum(t, "SupplyRequest", "priority", element.getPriorityElement(), -1);
    if (element.hasOrderedItem())
      composeSupplyRequestSupplyRequestOrderedItemComponent(t, "SupplyRequest", "orderedItem", element.getOrderedItem(), -1);
    if (element.hasOccurrence())
      composeType(t, "SupplyRequest", "occurrence", element.getOccurrence(), -1);
    if (element.hasAuthoredOnElement())
      composeDateTime(t, "SupplyRequest", "authoredOn", element.getAuthoredOnElement(), -1);
    if (element.hasRequester())
      composeSupplyRequestSupplyRequestRequesterComponent(t, "SupplyRequest", "requester", element.getRequester(), -1);
    for (int i = 0; i < element.getSupplier().size(); i++)
      composeReference(t, "SupplyRequest", "supplier", element.getSupplier().get(i), i);
    if (element.hasReason())
      composeType(t, "SupplyRequest", "reason", element.getReason(), -1);
    if (element.hasDeliverFrom())
      composeReference(t, "SupplyRequest", "deliverFrom", element.getDeliverFrom(), -1);
    if (element.hasDeliverTo())
      composeReference(t, "SupplyRequest", "deliverTo", element.getDeliverTo(), -1);
  }

  protected void composeSupplyRequestSupplyRequestOrderedItemComponent(Complex parent, String parentType, String name, SupplyRequest.SupplyRequestOrderedItemComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "orderedItem", name, element, index);
    if (element.hasQuantity())
      composeQuantity(t, "SupplyRequest", "quantity", element.getQuantity(), -1);
    if (element.hasItem())
      composeType(t, "SupplyRequest", "item", element.getItem(), -1);
  }

  protected void composeSupplyRequestSupplyRequestRequesterComponent(Complex parent, String parentType, String name, SupplyRequest.SupplyRequestRequesterComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "requester", name, element, index);
    if (element.hasAgent())
      composeReference(t, "SupplyRequest", "agent", element.getAgent(), -1);
    if (element.hasOnBehalfOf())
      composeReference(t, "SupplyRequest", "onBehalfOf", element.getOnBehalfOf(), -1);
  }

  protected void composeTask(Complex parent, String parentType, String name, Task element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "Task", name, element, index);
    for (int i = 0; i < element.getIdentifier().size(); i++)
      composeIdentifier(t, "Task", "identifier", element.getIdentifier().get(i), i);
    if (element.hasDefinition())
      composeType(t, "Task", "definition", element.getDefinition(), -1);
    for (int i = 0; i < element.getBasedOn().size(); i++)
      composeReference(t, "Task", "basedOn", element.getBasedOn().get(i), i);
    if (element.hasGroupIdentifier())
      composeIdentifier(t, "Task", "groupIdentifier", element.getGroupIdentifier(), -1);
    for (int i = 0; i < element.getPartOf().size(); i++)
      composeReference(t, "Task", "partOf", element.getPartOf().get(i), i);
    if (element.hasStatusElement())
      composeEnum(t, "Task", "status", element.getStatusElement(), -1);
    if (element.hasStatusReason())
      composeCodeableConcept(t, "Task", "statusReason", element.getStatusReason(), -1);
    if (element.hasBusinessStatus())
      composeCodeableConcept(t, "Task", "businessStatus", element.getBusinessStatus(), -1);
    if (element.hasIntentElement())
      composeEnum(t, "Task", "intent", element.getIntentElement(), -1);
    if (element.hasPriorityElement())
      composeEnum(t, "Task", "priority", element.getPriorityElement(), -1);
    if (element.hasCode())
      composeCodeableConcept(t, "Task", "code", element.getCode(), -1);
    if (element.hasDescriptionElement())
      composeString(t, "Task", "description", element.getDescriptionElement(), -1);
    if (element.hasFocus())
      composeReference(t, "Task", "focus", element.getFocus(), -1);
    if (element.hasFor())
      composeReference(t, "Task", "for", element.getFor(), -1);
    if (element.hasContext())
      composeReference(t, "Task", "context", element.getContext(), -1);
    if (element.hasExecutionPeriod())
      composePeriod(t, "Task", "executionPeriod", element.getExecutionPeriod(), -1);
    if (element.hasAuthoredOnElement())
      composeDateTime(t, "Task", "authoredOn", element.getAuthoredOnElement(), -1);
    if (element.hasLastModifiedElement())
      composeDateTime(t, "Task", "lastModified", element.getLastModifiedElement(), -1);
    if (element.hasRequester())
      composeTaskTaskRequesterComponent(t, "Task", "requester", element.getRequester(), -1);
    for (int i = 0; i < element.getPerformerType().size(); i++)
      composeCodeableConcept(t, "Task", "performerType", element.getPerformerType().get(i), i);
    if (element.hasOwner())
      composeReference(t, "Task", "owner", element.getOwner(), -1);
    if (element.hasReasonCode())
      composeCodeableConcept(t, "Task", "reasonCode", element.getReasonCode(), -1);
    if (element.hasReasonReference())
      composeReference(t, "Task", "reasonReference", element.getReasonReference(), -1);
    for (int i = 0; i < element.getNote().size(); i++)
      composeAnnotation(t, "Task", "note", element.getNote().get(i), i);
    for (int i = 0; i < element.getRelevantHistory().size(); i++)
      composeReference(t, "Task", "relevantHistory", element.getRelevantHistory().get(i), i);
    if (element.hasRestriction())
      composeTaskTaskRestrictionComponent(t, "Task", "restriction", element.getRestriction(), -1);
    for (int i = 0; i < element.getInput().size(); i++)
      composeTaskParameterComponent(t, "Task", "input", element.getInput().get(i), i);
    for (int i = 0; i < element.getOutput().size(); i++)
      composeTaskTaskOutputComponent(t, "Task", "output", element.getOutput().get(i), i);
  }

  protected void composeTaskTaskRequesterComponent(Complex parent, String parentType, String name, Task.TaskRequesterComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "requester", name, element, index);
    if (element.hasAgent())
      composeReference(t, "Task", "agent", element.getAgent(), -1);
    if (element.hasOnBehalfOf())
      composeReference(t, "Task", "onBehalfOf", element.getOnBehalfOf(), -1);
  }

  protected void composeTaskTaskRestrictionComponent(Complex parent, String parentType, String name, Task.TaskRestrictionComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "restriction", name, element, index);
    if (element.hasRepetitionsElement())
      composePositiveInt(t, "Task", "repetitions", element.getRepetitionsElement(), -1);
    if (element.hasPeriod())
      composePeriod(t, "Task", "period", element.getPeriod(), -1);
    for (int i = 0; i < element.getRecipient().size(); i++)
      composeReference(t, "Task", "recipient", element.getRecipient().get(i), i);
  }

  protected void composeTaskParameterComponent(Complex parent, String parentType, String name, Task.ParameterComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "input", name, element, index);
    if (element.hasType())
      composeCodeableConcept(t, "Task", "type", element.getType(), -1);
    if (element.hasValue())
      composeType(t, "Task", "value", element.getValue(), -1);
  }

  protected void composeTaskTaskOutputComponent(Complex parent, String parentType, String name, Task.TaskOutputComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "output", name, element, index);
    if (element.hasType())
      composeCodeableConcept(t, "Task", "type", element.getType(), -1);
    if (element.hasValue())
      composeType(t, "Task", "value", element.getValue(), -1);
  }

  protected void composeTestReport(Complex parent, String parentType, String name, TestReport element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "TestReport", name, element, index);
    if (element.hasIdentifier())
      composeIdentifier(t, "TestReport", "identifier", element.getIdentifier(), -1);
    if (element.hasNameElement())
      composeString(t, "TestReport", "name", element.getNameElement(), -1);
    if (element.hasStatusElement())
      composeEnum(t, "TestReport", "status", element.getStatusElement(), -1);
    if (element.hasTestScript())
      composeReference(t, "TestReport", "testScript", element.getTestScript(), -1);
    if (element.hasResultElement())
      composeEnum(t, "TestReport", "result", element.getResultElement(), -1);
    if (element.hasScoreElement())
      composeDecimal(t, "TestReport", "score", element.getScoreElement(), -1);
    if (element.hasTesterElement())
      composeString(t, "TestReport", "tester", element.getTesterElement(), -1);
    if (element.hasIssuedElement())
      composeDateTime(t, "TestReport", "issued", element.getIssuedElement(), -1);
    for (int i = 0; i < element.getParticipant().size(); i++)
      composeTestReportTestReportParticipantComponent(t, "TestReport", "participant", element.getParticipant().get(i), i);
    if (element.hasSetup())
      composeTestReportTestReportSetupComponent(t, "TestReport", "setup", element.getSetup(), -1);
    for (int i = 0; i < element.getTest().size(); i++)
      composeTestReportTestReportTestComponent(t, "TestReport", "test", element.getTest().get(i), i);
    if (element.hasTeardown())
      composeTestReportTestReportTeardownComponent(t, "TestReport", "teardown", element.getTeardown(), -1);
  }

  protected void composeTestReportTestReportParticipantComponent(Complex parent, String parentType, String name, TestReport.TestReportParticipantComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "participant", name, element, index);
    if (element.hasTypeElement())
      composeEnum(t, "TestReport", "type", element.getTypeElement(), -1);
    if (element.hasUriElement())
      composeUri(t, "TestReport", "uri", element.getUriElement(), -1);
    if (element.hasDisplayElement())
      composeString(t, "TestReport", "display", element.getDisplayElement(), -1);
  }

  protected void composeTestReportTestReportSetupComponent(Complex parent, String parentType, String name, TestReport.TestReportSetupComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "setup", name, element, index);
    for (int i = 0; i < element.getAction().size(); i++)
      composeTestReportSetupActionComponent(t, "TestReport", "action", element.getAction().get(i), i);
  }

  protected void composeTestReportSetupActionComponent(Complex parent, String parentType, String name, TestReport.SetupActionComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "action", name, element, index);
    if (element.hasOperation())
      composeTestReportSetupActionOperationComponent(t, "TestReport", "operation", element.getOperation(), -1);
    if (element.hasAssert())
      composeTestReportSetupActionAssertComponent(t, "TestReport", "assert", element.getAssert(), -1);
  }

  protected void composeTestReportSetupActionOperationComponent(Complex parent, String parentType, String name, TestReport.SetupActionOperationComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "operation", name, element, index);
    if (element.hasResultElement())
      composeEnum(t, "TestReport", "result", element.getResultElement(), -1);
    if (element.hasMessageElement())
      composeMarkdown(t, "TestReport", "message", element.getMessageElement(), -1);
    if (element.hasDetailElement())
      composeUri(t, "TestReport", "detail", element.getDetailElement(), -1);
  }

  protected void composeTestReportSetupActionAssertComponent(Complex parent, String parentType, String name, TestReport.SetupActionAssertComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "assert", name, element, index);
    if (element.hasResultElement())
      composeEnum(t, "TestReport", "result", element.getResultElement(), -1);
    if (element.hasMessageElement())
      composeMarkdown(t, "TestReport", "message", element.getMessageElement(), -1);
    if (element.hasDetailElement())
      composeString(t, "TestReport", "detail", element.getDetailElement(), -1);
  }

  protected void composeTestReportTestReportTestComponent(Complex parent, String parentType, String name, TestReport.TestReportTestComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "test", name, element, index);
    if (element.hasNameElement())
      composeString(t, "TestReport", "name", element.getNameElement(), -1);
    if (element.hasDescriptionElement())
      composeString(t, "TestReport", "description", element.getDescriptionElement(), -1);
    for (int i = 0; i < element.getAction().size(); i++)
      composeTestReportTestActionComponent(t, "TestReport", "action", element.getAction().get(i), i);
  }

  protected void composeTestReportTestActionComponent(Complex parent, String parentType, String name, TestReport.TestActionComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "action", name, element, index);
    if (element.hasOperation())
      composeTestReportSetupActionOperationComponent(t, "TestReport", "operation", element.getOperation(), -1);
    if (element.hasAssert())
      composeTestReportSetupActionAssertComponent(t, "TestReport", "assert", element.getAssert(), -1);
  }

  protected void composeTestReportTestReportTeardownComponent(Complex parent, String parentType, String name, TestReport.TestReportTeardownComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "teardown", name, element, index);
    for (int i = 0; i < element.getAction().size(); i++)
      composeTestReportTeardownActionComponent(t, "TestReport", "action", element.getAction().get(i), i);
  }

  protected void composeTestReportTeardownActionComponent(Complex parent, String parentType, String name, TestReport.TeardownActionComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "action", name, element, index);
    if (element.hasOperation())
      composeTestReportSetupActionOperationComponent(t, "TestReport", "operation", element.getOperation(), -1);
  }

  protected void composeTestScript(Complex parent, String parentType, String name, TestScript element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "TestScript", name, element, index);
    if (element.hasUrlElement())
      composeUri(t, "TestScript", "url", element.getUrlElement(), -1);
    if (element.hasIdentifier())
      composeIdentifier(t, "TestScript", "identifier", element.getIdentifier(), -1);
    if (element.hasVersionElement())
      composeString(t, "TestScript", "version", element.getVersionElement(), -1);
    if (element.hasNameElement())
      composeString(t, "TestScript", "name", element.getNameElement(), -1);
    if (element.hasTitleElement())
      composeString(t, "TestScript", "title", element.getTitleElement(), -1);
    if (element.hasStatusElement())
      composeEnum(t, "TestScript", "status", element.getStatusElement(), -1);
    if (element.hasExperimentalElement())
      composeBoolean(t, "TestScript", "experimental", element.getExperimentalElement(), -1);
    if (element.hasDateElement())
      composeDateTime(t, "TestScript", "date", element.getDateElement(), -1);
    if (element.hasPublisherElement())
      composeString(t, "TestScript", "publisher", element.getPublisherElement(), -1);
    for (int i = 0; i < element.getContact().size(); i++)
      composeContactDetail(t, "TestScript", "contact", element.getContact().get(i), i);
    if (element.hasDescriptionElement())
      composeMarkdown(t, "TestScript", "description", element.getDescriptionElement(), -1);
    for (int i = 0; i < element.getUseContext().size(); i++)
      composeUsageContext(t, "TestScript", "useContext", element.getUseContext().get(i), i);
    for (int i = 0; i < element.getJurisdiction().size(); i++)
      composeCodeableConcept(t, "TestScript", "jurisdiction", element.getJurisdiction().get(i), i);
    if (element.hasPurposeElement())
      composeMarkdown(t, "TestScript", "purpose", element.getPurposeElement(), -1);
    if (element.hasCopyrightElement())
      composeMarkdown(t, "TestScript", "copyright", element.getCopyrightElement(), -1);
    for (int i = 0; i < element.getOrigin().size(); i++)
      composeTestScriptTestScriptOriginComponent(t, "TestScript", "origin", element.getOrigin().get(i), i);
    for (int i = 0; i < element.getDestination().size(); i++)
      composeTestScriptTestScriptDestinationComponent(t, "TestScript", "destination", element.getDestination().get(i), i);
    if (element.hasMetadata())
      composeTestScriptTestScriptMetadataComponent(t, "TestScript", "metadata", element.getMetadata(), -1);
    for (int i = 0; i < element.getFixture().size(); i++)
      composeTestScriptTestScriptFixtureComponent(t, "TestScript", "fixture", element.getFixture().get(i), i);
    for (int i = 0; i < element.getProfile().size(); i++)
      composeReference(t, "TestScript", "profile", element.getProfile().get(i), i);
    for (int i = 0; i < element.getVariable().size(); i++)
      composeTestScriptTestScriptVariableComponent(t, "TestScript", "variable", element.getVariable().get(i), i);
    for (int i = 0; i < element.getRule().size(); i++)
      composeTestScriptTestScriptRuleComponent(t, "TestScript", "rule", element.getRule().get(i), i);
    for (int i = 0; i < element.getRuleset().size(); i++)
      composeTestScriptTestScriptRulesetComponent(t, "TestScript", "ruleset", element.getRuleset().get(i), i);
    if (element.hasSetup())
      composeTestScriptTestScriptSetupComponent(t, "TestScript", "setup", element.getSetup(), -1);
    for (int i = 0; i < element.getTest().size(); i++)
      composeTestScriptTestScriptTestComponent(t, "TestScript", "test", element.getTest().get(i), i);
    if (element.hasTeardown())
      composeTestScriptTestScriptTeardownComponent(t, "TestScript", "teardown", element.getTeardown(), -1);
  }

  protected void composeTestScriptTestScriptOriginComponent(Complex parent, String parentType, String name, TestScript.TestScriptOriginComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "origin", name, element, index);
    if (element.hasIndexElement())
      composeInteger(t, "TestScript", "index", element.getIndexElement(), -1);
    if (element.hasProfile())
      composeCoding(t, "TestScript", "profile", element.getProfile(), -1);
  }

  protected void composeTestScriptTestScriptDestinationComponent(Complex parent, String parentType, String name, TestScript.TestScriptDestinationComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "destination", name, element, index);
    if (element.hasIndexElement())
      composeInteger(t, "TestScript", "index", element.getIndexElement(), -1);
    if (element.hasProfile())
      composeCoding(t, "TestScript", "profile", element.getProfile(), -1);
  }

  protected void composeTestScriptTestScriptMetadataComponent(Complex parent, String parentType, String name, TestScript.TestScriptMetadataComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "metadata", name, element, index);
    for (int i = 0; i < element.getLink().size(); i++)
      composeTestScriptTestScriptMetadataLinkComponent(t, "TestScript", "link", element.getLink().get(i), i);
    for (int i = 0; i < element.getCapability().size(); i++)
      composeTestScriptTestScriptMetadataCapabilityComponent(t, "TestScript", "capability", element.getCapability().get(i), i);
  }

  protected void composeTestScriptTestScriptMetadataLinkComponent(Complex parent, String parentType, String name, TestScript.TestScriptMetadataLinkComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "link", name, element, index);
    if (element.hasUrlElement())
      composeUri(t, "TestScript", "url", element.getUrlElement(), -1);
    if (element.hasDescriptionElement())
      composeString(t, "TestScript", "description", element.getDescriptionElement(), -1);
  }

  protected void composeTestScriptTestScriptMetadataCapabilityComponent(Complex parent, String parentType, String name, TestScript.TestScriptMetadataCapabilityComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "capability", name, element, index);
    if (element.hasRequiredElement())
      composeBoolean(t, "TestScript", "required", element.getRequiredElement(), -1);
    if (element.hasValidatedElement())
      composeBoolean(t, "TestScript", "validated", element.getValidatedElement(), -1);
    if (element.hasDescriptionElement())
      composeString(t, "TestScript", "description", element.getDescriptionElement(), -1);
    for (int i = 0; i < element.getOrigin().size(); i++)
      composeInteger(t, "TestScript", "origin", element.getOrigin().get(i), i);
    if (element.hasDestinationElement())
      composeInteger(t, "TestScript", "destination", element.getDestinationElement(), -1);
    for (int i = 0; i < element.getLink().size(); i++)
      composeUri(t, "TestScript", "link", element.getLink().get(i), i);
    if (element.hasCapabilities())
      composeReference(t, "TestScript", "capabilities", element.getCapabilities(), -1);
  }

  protected void composeTestScriptTestScriptFixtureComponent(Complex parent, String parentType, String name, TestScript.TestScriptFixtureComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "fixture", name, element, index);
    if (element.hasAutocreateElement())
      composeBoolean(t, "TestScript", "autocreate", element.getAutocreateElement(), -1);
    if (element.hasAutodeleteElement())
      composeBoolean(t, "TestScript", "autodelete", element.getAutodeleteElement(), -1);
    if (element.hasResource())
      composeReference(t, "TestScript", "resource", element.getResource(), -1);
  }

  protected void composeTestScriptTestScriptVariableComponent(Complex parent, String parentType, String name, TestScript.TestScriptVariableComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "variable", name, element, index);
    if (element.hasNameElement())
      composeString(t, "TestScript", "name", element.getNameElement(), -1);
    if (element.hasDefaultValueElement())
      composeString(t, "TestScript", "defaultValue", element.getDefaultValueElement(), -1);
    if (element.hasDescriptionElement())
      composeString(t, "TestScript", "description", element.getDescriptionElement(), -1);
    if (element.hasExpressionElement())
      composeString(t, "TestScript", "expression", element.getExpressionElement(), -1);
    if (element.hasHeaderFieldElement())
      composeString(t, "TestScript", "headerField", element.getHeaderFieldElement(), -1);
    if (element.hasHintElement())
      composeString(t, "TestScript", "hint", element.getHintElement(), -1);
    if (element.hasPathElement())
      composeString(t, "TestScript", "path", element.getPathElement(), -1);
    if (element.hasSourceIdElement())
      composeId(t, "TestScript", "sourceId", element.getSourceIdElement(), -1);
  }

  protected void composeTestScriptTestScriptRuleComponent(Complex parent, String parentType, String name, TestScript.TestScriptRuleComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "rule", name, element, index);
    if (element.hasResource())
      composeReference(t, "TestScript", "resource", element.getResource(), -1);
    for (int i = 0; i < element.getParam().size(); i++)
      composeTestScriptRuleParamComponent(t, "TestScript", "param", element.getParam().get(i), i);
  }

  protected void composeTestScriptRuleParamComponent(Complex parent, String parentType, String name, TestScript.RuleParamComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "param", name, element, index);
    if (element.hasNameElement())
      composeString(t, "TestScript", "name", element.getNameElement(), -1);
    if (element.hasValueElement())
      composeString(t, "TestScript", "value", element.getValueElement(), -1);
  }

  protected void composeTestScriptTestScriptRulesetComponent(Complex parent, String parentType, String name, TestScript.TestScriptRulesetComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "ruleset", name, element, index);
    if (element.hasResource())
      composeReference(t, "TestScript", "resource", element.getResource(), -1);
    for (int i = 0; i < element.getRule().size(); i++)
      composeTestScriptRulesetRuleComponent(t, "TestScript", "rule", element.getRule().get(i), i);
  }

  protected void composeTestScriptRulesetRuleComponent(Complex parent, String parentType, String name, TestScript.RulesetRuleComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "rule", name, element, index);
    if (element.hasRuleIdElement())
      composeId(t, "TestScript", "ruleId", element.getRuleIdElement(), -1);
    for (int i = 0; i < element.getParam().size(); i++)
      composeTestScriptRulesetRuleParamComponent(t, "TestScript", "param", element.getParam().get(i), i);
  }

  protected void composeTestScriptRulesetRuleParamComponent(Complex parent, String parentType, String name, TestScript.RulesetRuleParamComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "param", name, element, index);
    if (element.hasNameElement())
      composeString(t, "TestScript", "name", element.getNameElement(), -1);
    if (element.hasValueElement())
      composeString(t, "TestScript", "value", element.getValueElement(), -1);
  }

  protected void composeTestScriptTestScriptSetupComponent(Complex parent, String parentType, String name, TestScript.TestScriptSetupComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "setup", name, element, index);
    for (int i = 0; i < element.getAction().size(); i++)
      composeTestScriptSetupActionComponent(t, "TestScript", "action", element.getAction().get(i), i);
  }

  protected void composeTestScriptSetupActionComponent(Complex parent, String parentType, String name, TestScript.SetupActionComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "action", name, element, index);
    if (element.hasOperation())
      composeTestScriptSetupActionOperationComponent(t, "TestScript", "operation", element.getOperation(), -1);
    if (element.hasAssert())
      composeTestScriptSetupActionAssertComponent(t, "TestScript", "assert", element.getAssert(), -1);
  }

  protected void composeTestScriptSetupActionOperationComponent(Complex parent, String parentType, String name, TestScript.SetupActionOperationComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "operation", name, element, index);
    if (element.hasType())
      composeCoding(t, "TestScript", "type", element.getType(), -1);
    if (element.hasResourceElement())
      composeCode(t, "TestScript", "resource", element.getResourceElement(), -1);
    if (element.hasLabelElement())
      composeString(t, "TestScript", "label", element.getLabelElement(), -1);
    if (element.hasDescriptionElement())
      composeString(t, "TestScript", "description", element.getDescriptionElement(), -1);
    if (element.hasAcceptElement())
      composeEnum(t, "TestScript", "accept", element.getAcceptElement(), -1);
    if (element.hasContentTypeElement())
      composeEnum(t, "TestScript", "contentType", element.getContentTypeElement(), -1);
    if (element.hasDestinationElement())
      composeInteger(t, "TestScript", "destination", element.getDestinationElement(), -1);
    if (element.hasEncodeRequestUrlElement())
      composeBoolean(t, "TestScript", "encodeRequestUrl", element.getEncodeRequestUrlElement(), -1);
    if (element.hasOriginElement())
      composeInteger(t, "TestScript", "origin", element.getOriginElement(), -1);
    if (element.hasParamsElement())
      composeString(t, "TestScript", "params", element.getParamsElement(), -1);
    for (int i = 0; i < element.getRequestHeader().size(); i++)
      composeTestScriptSetupActionOperationRequestHeaderComponent(t, "TestScript", "requestHeader", element.getRequestHeader().get(i), i);
    if (element.hasRequestIdElement())
      composeId(t, "TestScript", "requestId", element.getRequestIdElement(), -1);
    if (element.hasResponseIdElement())
      composeId(t, "TestScript", "responseId", element.getResponseIdElement(), -1);
    if (element.hasSourceIdElement())
      composeId(t, "TestScript", "sourceId", element.getSourceIdElement(), -1);
    if (element.hasTargetIdElement())
      composeId(t, "TestScript", "targetId", element.getTargetIdElement(), -1);
    if (element.hasUrlElement())
      composeString(t, "TestScript", "url", element.getUrlElement(), -1);
  }

  protected void composeTestScriptSetupActionOperationRequestHeaderComponent(Complex parent, String parentType, String name, TestScript.SetupActionOperationRequestHeaderComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "requestHeader", name, element, index);
    if (element.hasFieldElement())
      composeString(t, "TestScript", "field", element.getFieldElement(), -1);
    if (element.hasValueElement())
      composeString(t, "TestScript", "value", element.getValueElement(), -1);
  }

  protected void composeTestScriptSetupActionAssertComponent(Complex parent, String parentType, String name, TestScript.SetupActionAssertComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "assert", name, element, index);
    if (element.hasLabelElement())
      composeString(t, "TestScript", "label", element.getLabelElement(), -1);
    if (element.hasDescriptionElement())
      composeString(t, "TestScript", "description", element.getDescriptionElement(), -1);
    if (element.hasDirectionElement())
      composeEnum(t, "TestScript", "direction", element.getDirectionElement(), -1);
    if (element.hasCompareToSourceIdElement())
      composeString(t, "TestScript", "compareToSourceId", element.getCompareToSourceIdElement(), -1);
    if (element.hasCompareToSourceExpressionElement())
      composeString(t, "TestScript", "compareToSourceExpression", element.getCompareToSourceExpressionElement(), -1);
    if (element.hasCompareToSourcePathElement())
      composeString(t, "TestScript", "compareToSourcePath", element.getCompareToSourcePathElement(), -1);
    if (element.hasContentTypeElement())
      composeEnum(t, "TestScript", "contentType", element.getContentTypeElement(), -1);
    if (element.hasExpressionElement())
      composeString(t, "TestScript", "expression", element.getExpressionElement(), -1);
    if (element.hasHeaderFieldElement())
      composeString(t, "TestScript", "headerField", element.getHeaderFieldElement(), -1);
    if (element.hasMinimumIdElement())
      composeString(t, "TestScript", "minimumId", element.getMinimumIdElement(), -1);
    if (element.hasNavigationLinksElement())
      composeBoolean(t, "TestScript", "navigationLinks", element.getNavigationLinksElement(), -1);
    if (element.hasOperatorElement())
      composeEnum(t, "TestScript", "operator", element.getOperatorElement(), -1);
    if (element.hasPathElement())
      composeString(t, "TestScript", "path", element.getPathElement(), -1);
    if (element.hasRequestMethodElement())
      composeEnum(t, "TestScript", "requestMethod", element.getRequestMethodElement(), -1);
    if (element.hasRequestURLElement())
      composeString(t, "TestScript", "requestURL", element.getRequestURLElement(), -1);
    if (element.hasResourceElement())
      composeCode(t, "TestScript", "resource", element.getResourceElement(), -1);
    if (element.hasResponseElement())
      composeEnum(t, "TestScript", "response", element.getResponseElement(), -1);
    if (element.hasResponseCodeElement())
      composeString(t, "TestScript", "responseCode", element.getResponseCodeElement(), -1);
    if (element.hasRule())
      composeTestScriptActionAssertRuleComponent(t, "TestScript", "rule", element.getRule(), -1);
    if (element.hasRuleset())
      composeTestScriptActionAssertRulesetComponent(t, "TestScript", "ruleset", element.getRuleset(), -1);
    if (element.hasSourceIdElement())
      composeId(t, "TestScript", "sourceId", element.getSourceIdElement(), -1);
    if (element.hasValidateProfileIdElement())
      composeId(t, "TestScript", "validateProfileId", element.getValidateProfileIdElement(), -1);
    if (element.hasValueElement())
      composeString(t, "TestScript", "value", element.getValueElement(), -1);
    if (element.hasWarningOnlyElement())
      composeBoolean(t, "TestScript", "warningOnly", element.getWarningOnlyElement(), -1);
  }

  protected void composeTestScriptActionAssertRuleComponent(Complex parent, String parentType, String name, TestScript.ActionAssertRuleComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "rule", name, element, index);
    if (element.hasRuleIdElement())
      composeId(t, "TestScript", "ruleId", element.getRuleIdElement(), -1);
    for (int i = 0; i < element.getParam().size(); i++)
      composeTestScriptActionAssertRuleParamComponent(t, "TestScript", "param", element.getParam().get(i), i);
  }

  protected void composeTestScriptActionAssertRuleParamComponent(Complex parent, String parentType, String name, TestScript.ActionAssertRuleParamComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "param", name, element, index);
    if (element.hasNameElement())
      composeString(t, "TestScript", "name", element.getNameElement(), -1);
    if (element.hasValueElement())
      composeString(t, "TestScript", "value", element.getValueElement(), -1);
  }

  protected void composeTestScriptActionAssertRulesetComponent(Complex parent, String parentType, String name, TestScript.ActionAssertRulesetComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "ruleset", name, element, index);
    if (element.hasRulesetIdElement())
      composeId(t, "TestScript", "rulesetId", element.getRulesetIdElement(), -1);
    for (int i = 0; i < element.getRule().size(); i++)
      composeTestScriptActionAssertRulesetRuleComponent(t, "TestScript", "rule", element.getRule().get(i), i);
  }

  protected void composeTestScriptActionAssertRulesetRuleComponent(Complex parent, String parentType, String name, TestScript.ActionAssertRulesetRuleComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "rule", name, element, index);
    if (element.hasRuleIdElement())
      composeId(t, "TestScript", "ruleId", element.getRuleIdElement(), -1);
    for (int i = 0; i < element.getParam().size(); i++)
      composeTestScriptActionAssertRulesetRuleParamComponent(t, "TestScript", "param", element.getParam().get(i), i);
  }

  protected void composeTestScriptActionAssertRulesetRuleParamComponent(Complex parent, String parentType, String name, TestScript.ActionAssertRulesetRuleParamComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "param", name, element, index);
    if (element.hasNameElement())
      composeString(t, "TestScript", "name", element.getNameElement(), -1);
    if (element.hasValueElement())
      composeString(t, "TestScript", "value", element.getValueElement(), -1);
  }

  protected void composeTestScriptTestScriptTestComponent(Complex parent, String parentType, String name, TestScript.TestScriptTestComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "test", name, element, index);
    if (element.hasNameElement())
      composeString(t, "TestScript", "name", element.getNameElement(), -1);
    if (element.hasDescriptionElement())
      composeString(t, "TestScript", "description", element.getDescriptionElement(), -1);
    for (int i = 0; i < element.getAction().size(); i++)
      composeTestScriptTestActionComponent(t, "TestScript", "action", element.getAction().get(i), i);
  }

  protected void composeTestScriptTestActionComponent(Complex parent, String parentType, String name, TestScript.TestActionComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "action", name, element, index);
    if (element.hasOperation())
      composeTestScriptSetupActionOperationComponent(t, "TestScript", "operation", element.getOperation(), -1);
    if (element.hasAssert())
      composeTestScriptSetupActionAssertComponent(t, "TestScript", "assert", element.getAssert(), -1);
  }

  protected void composeTestScriptTestScriptTeardownComponent(Complex parent, String parentType, String name, TestScript.TestScriptTeardownComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "teardown", name, element, index);
    for (int i = 0; i < element.getAction().size(); i++)
      composeTestScriptTeardownActionComponent(t, "TestScript", "action", element.getAction().get(i), i);
  }

  protected void composeTestScriptTeardownActionComponent(Complex parent, String parentType, String name, TestScript.TeardownActionComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "action", name, element, index);
    if (element.hasOperation())
      composeTestScriptSetupActionOperationComponent(t, "TestScript", "operation", element.getOperation(), -1);
  }

  protected void composeValueSet(Complex parent, String parentType, String name, ValueSet element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "ValueSet", name, element, index);
    if (element.hasUrlElement())
      composeUri(t, "ValueSet", "url", element.getUrlElement(), -1);
    for (int i = 0; i < element.getIdentifier().size(); i++)
      composeIdentifier(t, "ValueSet", "identifier", element.getIdentifier().get(i), i);
    if (element.hasVersionElement())
      composeString(t, "ValueSet", "version", element.getVersionElement(), -1);
    if (element.hasNameElement())
      composeString(t, "ValueSet", "name", element.getNameElement(), -1);
    if (element.hasTitleElement())
      composeString(t, "ValueSet", "title", element.getTitleElement(), -1);
    if (element.hasStatusElement())
      composeEnum(t, "ValueSet", "status", element.getStatusElement(), -1);
    if (element.hasExperimentalElement())
      composeBoolean(t, "ValueSet", "experimental", element.getExperimentalElement(), -1);
    if (element.hasDateElement())
      composeDateTime(t, "ValueSet", "date", element.getDateElement(), -1);
    if (element.hasPublisherElement())
      composeString(t, "ValueSet", "publisher", element.getPublisherElement(), -1);
    for (int i = 0; i < element.getContact().size(); i++)
      composeContactDetail(t, "ValueSet", "contact", element.getContact().get(i), i);
    if (element.hasDescriptionElement())
      composeMarkdown(t, "ValueSet", "description", element.getDescriptionElement(), -1);
    for (int i = 0; i < element.getUseContext().size(); i++)
      composeUsageContext(t, "ValueSet", "useContext", element.getUseContext().get(i), i);
    for (int i = 0; i < element.getJurisdiction().size(); i++)
      composeCodeableConcept(t, "ValueSet", "jurisdiction", element.getJurisdiction().get(i), i);
    if (element.hasImmutableElement())
      composeBoolean(t, "ValueSet", "immutable", element.getImmutableElement(), -1);
    if (element.hasPurposeElement())
      composeMarkdown(t, "ValueSet", "purpose", element.getPurposeElement(), -1);
    if (element.hasCopyrightElement())
      composeMarkdown(t, "ValueSet", "copyright", element.getCopyrightElement(), -1);
    if (element.hasExtensibleElement())
      composeBoolean(t, "ValueSet", "extensible", element.getExtensibleElement(), -1);
    if (element.hasCompose())
      composeValueSetValueSetComposeComponent(t, "ValueSet", "compose", element.getCompose(), -1);
    if (element.hasExpansion())
      composeValueSetValueSetExpansionComponent(t, "ValueSet", "expansion", element.getExpansion(), -1);
  }

  protected void composeValueSetValueSetComposeComponent(Complex parent, String parentType, String name, ValueSet.ValueSetComposeComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "compose", name, element, index);
    if (element.hasLockedDateElement())
      composeDate(t, "ValueSet", "lockedDate", element.getLockedDateElement(), -1);
    if (element.hasInactiveElement())
      composeBoolean(t, "ValueSet", "inactive", element.getInactiveElement(), -1);
    for (int i = 0; i < element.getInclude().size(); i++)
      composeValueSetConceptSetComponent(t, "ValueSet", "include", element.getInclude().get(i), i);
    for (int i = 0; i < element.getExclude().size(); i++)
      composeValueSetConceptSetComponent(t, "ValueSet", "exclude", element.getExclude().get(i), i);
  }

  protected void composeValueSetConceptSetComponent(Complex parent, String parentType, String name, ValueSet.ConceptSetComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "include", name, element, index);
    if (element.hasSystemElement())
      composeUri(t, "ValueSet", "system", element.getSystemElement(), -1);
    if (element.hasVersionElement())
      composeString(t, "ValueSet", "version", element.getVersionElement(), -1);
    for (int i = 0; i < element.getConcept().size(); i++)
      composeValueSetConceptReferenceComponent(t, "ValueSet", "concept", element.getConcept().get(i), i);
    for (int i = 0; i < element.getFilter().size(); i++)
      composeValueSetConceptSetFilterComponent(t, "ValueSet", "filter", element.getFilter().get(i), i);
    for (int i = 0; i < element.getValueSet().size(); i++)
      composeUri(t, "ValueSet", "valueSet", element.getValueSet().get(i), i);
  }

  protected void composeValueSetConceptReferenceComponent(Complex parent, String parentType, String name, ValueSet.ConceptReferenceComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "concept", name, element, index);
    if (element.hasCodeElement())
      composeCode(t, "ValueSet", "code", element.getCodeElement(), -1);
    if (element.hasDisplayElement())
      composeString(t, "ValueSet", "display", element.getDisplayElement(), -1);
    for (int i = 0; i < element.getDesignation().size(); i++)
      composeValueSetConceptReferenceDesignationComponent(t, "ValueSet", "designation", element.getDesignation().get(i), i);
  }

  protected void composeValueSetConceptReferenceDesignationComponent(Complex parent, String parentType, String name, ValueSet.ConceptReferenceDesignationComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "designation", name, element, index);
    if (element.hasLanguageElement())
      composeCode(t, "ValueSet", "language", element.getLanguageElement(), -1);
    if (element.hasUse())
      composeCoding(t, "ValueSet", "use", element.getUse(), -1);
    if (element.hasValueElement())
      composeString(t, "ValueSet", "value", element.getValueElement(), -1);
  }

  protected void composeValueSetConceptSetFilterComponent(Complex parent, String parentType, String name, ValueSet.ConceptSetFilterComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "filter", name, element, index);
    if (element.hasPropertyElement())
      composeCode(t, "ValueSet", "property", element.getPropertyElement(), -1);
    if (element.hasOpElement())
      composeEnum(t, "ValueSet", "op", element.getOpElement(), -1);
    if (element.hasValueElement())
      composeCode(t, "ValueSet", "value", element.getValueElement(), -1);
  }

  protected void composeValueSetValueSetExpansionComponent(Complex parent, String parentType, String name, ValueSet.ValueSetExpansionComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "expansion", name, element, index);
    if (element.hasIdentifierElement())
      composeUri(t, "ValueSet", "identifier", element.getIdentifierElement(), -1);
    if (element.hasTimestampElement())
      composeDateTime(t, "ValueSet", "timestamp", element.getTimestampElement(), -1);
    if (element.hasTotalElement())
      composeInteger(t, "ValueSet", "total", element.getTotalElement(), -1);
    if (element.hasOffsetElement())
      composeInteger(t, "ValueSet", "offset", element.getOffsetElement(), -1);
    for (int i = 0; i < element.getParameter().size(); i++)
      composeValueSetValueSetExpansionParameterComponent(t, "ValueSet", "parameter", element.getParameter().get(i), i);
    for (int i = 0; i < element.getContains().size(); i++)
      composeValueSetValueSetExpansionContainsComponent(t, "ValueSet", "contains", element.getContains().get(i), i);
  }

  protected void composeValueSetValueSetExpansionParameterComponent(Complex parent, String parentType, String name, ValueSet.ValueSetExpansionParameterComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "parameter", name, element, index);
    if (element.hasNameElement())
      composeString(t, "ValueSet", "name", element.getNameElement(), -1);
    if (element.hasValue())
      composeType(t, "ValueSet", "value", element.getValue(), -1);
  }

  protected void composeValueSetValueSetExpansionContainsComponent(Complex parent, String parentType, String name, ValueSet.ValueSetExpansionContainsComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "contains", name, element, index);
    if (element.hasSystemElement())
      composeUri(t, "ValueSet", "system", element.getSystemElement(), -1);
    if (element.hasAbstractElement())
      composeBoolean(t, "ValueSet", "abstract", element.getAbstractElement(), -1);
    if (element.hasInactiveElement())
      composeBoolean(t, "ValueSet", "inactive", element.getInactiveElement(), -1);
    if (element.hasVersionElement())
      composeString(t, "ValueSet", "version", element.getVersionElement(), -1);
    if (element.hasCodeElement())
      composeCode(t, "ValueSet", "code", element.getCodeElement(), -1);
    if (element.hasDisplayElement())
      composeString(t, "ValueSet", "display", element.getDisplayElement(), -1);
    for (int i = 0; i < element.getDesignation().size(); i++)
      composeValueSetConceptReferenceDesignationComponent(t, "ValueSet", "designation", element.getDesignation().get(i), i);
    for (int i = 0; i < element.getContains().size(); i++)
      composeValueSetValueSetExpansionContainsComponent(t, "ValueSet", "contains", element.getContains().get(i), i);
  }

  protected void composeVisionPrescription(Complex parent, String parentType, String name, VisionPrescription element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "VisionPrescription", name, element, index);
    for (int i = 0; i < element.getIdentifier().size(); i++)
      composeIdentifier(t, "VisionPrescription", "identifier", element.getIdentifier().get(i), i);
    if (element.hasStatusElement())
      composeEnum(t, "VisionPrescription", "status", element.getStatusElement(), -1);
    if (element.hasPatient())
      composeReference(t, "VisionPrescription", "patient", element.getPatient(), -1);
    if (element.hasEncounter())
      composeReference(t, "VisionPrescription", "encounter", element.getEncounter(), -1);
    if (element.hasDateWrittenElement())
      composeDateTime(t, "VisionPrescription", "dateWritten", element.getDateWrittenElement(), -1);
    if (element.hasPrescriber())
      composeReference(t, "VisionPrescription", "prescriber", element.getPrescriber(), -1);
    if (element.hasReason())
      composeType(t, "VisionPrescription", "reason", element.getReason(), -1);
    for (int i = 0; i < element.getDispense().size(); i++)
      composeVisionPrescriptionVisionPrescriptionDispenseComponent(t, "VisionPrescription", "dispense", element.getDispense().get(i), i);
  }

  protected void composeVisionPrescriptionVisionPrescriptionDispenseComponent(Complex parent, String parentType, String name, VisionPrescription.VisionPrescriptionDispenseComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "dispense", name, element, index);
    if (element.hasProduct())
      composeCodeableConcept(t, "VisionPrescription", "product", element.getProduct(), -1);
    if (element.hasEyeElement())
      composeEnum(t, "VisionPrescription", "eye", element.getEyeElement(), -1);
    if (element.hasSphereElement())
      composeDecimal(t, "VisionPrescription", "sphere", element.getSphereElement(), -1);
    if (element.hasCylinderElement())
      composeDecimal(t, "VisionPrescription", "cylinder", element.getCylinderElement(), -1);
    if (element.hasAxisElement())
      composeInteger(t, "VisionPrescription", "axis", element.getAxisElement(), -1);
    if (element.hasPrismElement())
      composeDecimal(t, "VisionPrescription", "prism", element.getPrismElement(), -1);
    if (element.hasBaseElement())
      composeEnum(t, "VisionPrescription", "base", element.getBaseElement(), -1);
    if (element.hasAddElement())
      composeDecimal(t, "VisionPrescription", "add", element.getAddElement(), -1);
    if (element.hasPowerElement())
      composeDecimal(t, "VisionPrescription", "power", element.getPowerElement(), -1);
    if (element.hasBackCurveElement())
      composeDecimal(t, "VisionPrescription", "backCurve", element.getBackCurveElement(), -1);
    if (element.hasDiameterElement())
      composeDecimal(t, "VisionPrescription", "diameter", element.getDiameterElement(), -1);
    if (element.hasDuration())
      composeQuantity(t, "VisionPrescription", "duration", element.getDuration(), -1);
    if (element.hasColorElement())
      composeString(t, "VisionPrescription", "color", element.getColorElement(), -1);
    if (element.hasBrandElement())
      composeString(t, "VisionPrescription", "brand", element.getBrandElement(), -1);
    for (int i = 0; i < element.getNote().size(); i++)
      composeAnnotation(t, "VisionPrescription", "note", element.getNote().get(i), i);
  }

  @Override
  protected void composeResource(Complex parent, Resource resource) {
    if (resource instanceof Parameters)
      composeParameters(parent, null, "Parameters", (Parameters)resource, -1);
    else if (resource instanceof Account)
      composeAccount(parent, null, "Account", (Account)resource, -1);
    else if (resource instanceof ActivityDefinition)
      composeActivityDefinition(parent, null, "ActivityDefinition", (ActivityDefinition)resource, -1);
    else if (resource instanceof AdverseEvent)
      composeAdverseEvent(parent, null, "AdverseEvent", (AdverseEvent)resource, -1);
    else if (resource instanceof AllergyIntolerance)
      composeAllergyIntolerance(parent, null, "AllergyIntolerance", (AllergyIntolerance)resource, -1);
    else if (resource instanceof Appointment)
      composeAppointment(parent, null, "Appointment", (Appointment)resource, -1);
    else if (resource instanceof AppointmentResponse)
      composeAppointmentResponse(parent, null, "AppointmentResponse", (AppointmentResponse)resource, -1);
    else if (resource instanceof AuditEvent)
      composeAuditEvent(parent, null, "AuditEvent", (AuditEvent)resource, -1);
    else if (resource instanceof Basic)
      composeBasic(parent, null, "Basic", (Basic)resource, -1);
    else if (resource instanceof Binary)
      composeBinary(parent, null, "Binary", (Binary)resource, -1);
    else if (resource instanceof BodyStructure)
      composeBodyStructure(parent, null, "BodyStructure", (BodyStructure)resource, -1);
    else if (resource instanceof Bundle)
      composeBundle(parent, null, "Bundle", (Bundle)resource, -1);
    else if (resource instanceof CapabilityStatement)
      composeCapabilityStatement(parent, null, "CapabilityStatement", (CapabilityStatement)resource, -1);
    else if (resource instanceof CarePlan)
      composeCarePlan(parent, null, "CarePlan", (CarePlan)resource, -1);
    else if (resource instanceof CareTeam)
      composeCareTeam(parent, null, "CareTeam", (CareTeam)resource, -1);
    else if (resource instanceof CatalogEntry)
      composeCatalogEntry(parent, null, "CatalogEntry", (CatalogEntry)resource, -1);
    else if (resource instanceof ChargeItem)
      composeChargeItem(parent, null, "ChargeItem", (ChargeItem)resource, -1);
    else if (resource instanceof Claim)
      composeClaim(parent, null, "Claim", (Claim)resource, -1);
    else if (resource instanceof ClaimResponse)
      composeClaimResponse(parent, null, "ClaimResponse", (ClaimResponse)resource, -1);
    else if (resource instanceof ClinicalImpression)
      composeClinicalImpression(parent, null, "ClinicalImpression", (ClinicalImpression)resource, -1);
    else if (resource instanceof CodeSystem)
      composeCodeSystem(parent, null, "CodeSystem", (CodeSystem)resource, -1);
    else if (resource instanceof Communication)
      composeCommunication(parent, null, "Communication", (Communication)resource, -1);
    else if (resource instanceof CommunicationRequest)
      composeCommunicationRequest(parent, null, "CommunicationRequest", (CommunicationRequest)resource, -1);
    else if (resource instanceof CompartmentDefinition)
      composeCompartmentDefinition(parent, null, "CompartmentDefinition", (CompartmentDefinition)resource, -1);
    else if (resource instanceof Composition)
      composeComposition(parent, null, "Composition", (Composition)resource, -1);
    else if (resource instanceof ConceptMap)
      composeConceptMap(parent, null, "ConceptMap", (ConceptMap)resource, -1);
    else if (resource instanceof Condition)
      composeCondition(parent, null, "Condition", (Condition)resource, -1);
    else if (resource instanceof Consent)
      composeConsent(parent, null, "Consent", (Consent)resource, -1);
    else if (resource instanceof Contract)
      composeContract(parent, null, "Contract", (Contract)resource, -1);
    else if (resource instanceof Coverage)
      composeCoverage(parent, null, "Coverage", (Coverage)resource, -1);
    else if (resource instanceof DetectedIssue)
      composeDetectedIssue(parent, null, "DetectedIssue", (DetectedIssue)resource, -1);
    else if (resource instanceof Device)
      composeDevice(parent, null, "Device", (Device)resource, -1);
    else if (resource instanceof DeviceComponent)
      composeDeviceComponent(parent, null, "DeviceComponent", (DeviceComponent)resource, -1);
    else if (resource instanceof DeviceMetric)
      composeDeviceMetric(parent, null, "DeviceMetric", (DeviceMetric)resource, -1);
    else if (resource instanceof DeviceRequest)
      composeDeviceRequest(parent, null, "DeviceRequest", (DeviceRequest)resource, -1);
    else if (resource instanceof DeviceUseStatement)
      composeDeviceUseStatement(parent, null, "DeviceUseStatement", (DeviceUseStatement)resource, -1);
    else if (resource instanceof DiagnosticReport)
      composeDiagnosticReport(parent, null, "DiagnosticReport", (DiagnosticReport)resource, -1);
    else if (resource instanceof DocumentManifest)
      composeDocumentManifest(parent, null, "DocumentManifest", (DocumentManifest)resource, -1);
    else if (resource instanceof DocumentReference)
      composeDocumentReference(parent, null, "DocumentReference", (DocumentReference)resource, -1);
    else if (resource instanceof EligibilityRequest)
      composeEligibilityRequest(parent, null, "EligibilityRequest", (EligibilityRequest)resource, -1);
    else if (resource instanceof EligibilityResponse)
      composeEligibilityResponse(parent, null, "EligibilityResponse", (EligibilityResponse)resource, -1);
    else if (resource instanceof Encounter)
      composeEncounter(parent, null, "Encounter", (Encounter)resource, -1);
    else if (resource instanceof Endpoint)
      composeEndpoint(parent, null, "Endpoint", (Endpoint)resource, -1);
    else if (resource instanceof EnrollmentRequest)
      composeEnrollmentRequest(parent, null, "EnrollmentRequest", (EnrollmentRequest)resource, -1);
    else if (resource instanceof EnrollmentResponse)
      composeEnrollmentResponse(parent, null, "EnrollmentResponse", (EnrollmentResponse)resource, -1);
    else if (resource instanceof EpisodeOfCare)
      composeEpisodeOfCare(parent, null, "EpisodeOfCare", (EpisodeOfCare)resource, -1);
    else if (resource instanceof EventDefinition)
      composeEventDefinition(parent, null, "EventDefinition", (EventDefinition)resource, -1);
    else if (resource instanceof ExampleScenario)
      composeExampleScenario(parent, null, "ExampleScenario", (ExampleScenario)resource, -1);
    else if (resource instanceof ExpansionProfile)
      composeExpansionProfile(parent, null, "ExpansionProfile", (ExpansionProfile)resource, -1);
    else if (resource instanceof ExplanationOfBenefit)
      composeExplanationOfBenefit(parent, null, "ExplanationOfBenefit", (ExplanationOfBenefit)resource, -1);
    else if (resource instanceof FamilyMemberHistory)
      composeFamilyMemberHistory(parent, null, "FamilyMemberHistory", (FamilyMemberHistory)resource, -1);
    else if (resource instanceof Flag)
      composeFlag(parent, null, "Flag", (Flag)resource, -1);
    else if (resource instanceof Goal)
      composeGoal(parent, null, "Goal", (Goal)resource, -1);
    else if (resource instanceof GraphDefinition)
      composeGraphDefinition(parent, null, "GraphDefinition", (GraphDefinition)resource, -1);
    else if (resource instanceof Group)
      composeGroup(parent, null, "Group", (Group)resource, -1);
    else if (resource instanceof GuidanceResponse)
      composeGuidanceResponse(parent, null, "GuidanceResponse", (GuidanceResponse)resource, -1);
    else if (resource instanceof HealthcareService)
      composeHealthcareService(parent, null, "HealthcareService", (HealthcareService)resource, -1);
    else if (resource instanceof ImagingManifest)
      composeImagingManifest(parent, null, "ImagingManifest", (ImagingManifest)resource, -1);
    else if (resource instanceof ImagingStudy)
      composeImagingStudy(parent, null, "ImagingStudy", (ImagingStudy)resource, -1);
    else if (resource instanceof Immunization)
      composeImmunization(parent, null, "Immunization", (Immunization)resource, -1);
    else if (resource instanceof ImmunizationRecommendation)
      composeImmunizationRecommendation(parent, null, "ImmunizationRecommendation", (ImmunizationRecommendation)resource, -1);
    else if (resource instanceof ImplementationGuide)
      composeImplementationGuide(parent, null, "ImplementationGuide", (ImplementationGuide)resource, -1);
    else if (resource instanceof Library)
      composeLibrary(parent, null, "Library", (Library)resource, -1);
    else if (resource instanceof Linkage)
      composeLinkage(parent, null, "Linkage", (Linkage)resource, -1);
    else if (resource instanceof ListResource)
      composeListResource(parent, null, "ListResource", (ListResource)resource, -1);
    else if (resource instanceof Location)
      composeLocation(parent, null, "Location", (Location)resource, -1);
    else if (resource instanceof Measure)
      composeMeasure(parent, null, "Measure", (Measure)resource, -1);
    else if (resource instanceof MeasureReport)
      composeMeasureReport(parent, null, "MeasureReport", (MeasureReport)resource, -1);
    else if (resource instanceof Media)
      composeMedia(parent, null, "Media", (Media)resource, -1);
    else if (resource instanceof Medication)
      composeMedication(parent, null, "Medication", (Medication)resource, -1);
    else if (resource instanceof MedicationAdministration)
      composeMedicationAdministration(parent, null, "MedicationAdministration", (MedicationAdministration)resource, -1);
    else if (resource instanceof MedicationDispense)
      composeMedicationDispense(parent, null, "MedicationDispense", (MedicationDispense)resource, -1);
    else if (resource instanceof MedicationRequest)
      composeMedicationRequest(parent, null, "MedicationRequest", (MedicationRequest)resource, -1);
    else if (resource instanceof MedicationStatement)
      composeMedicationStatement(parent, null, "MedicationStatement", (MedicationStatement)resource, -1);
    else if (resource instanceof MessageDefinition)
      composeMessageDefinition(parent, null, "MessageDefinition", (MessageDefinition)resource, -1);
    else if (resource instanceof MessageHeader)
      composeMessageHeader(parent, null, "MessageHeader", (MessageHeader)resource, -1);
    else if (resource instanceof NamingSystem)
      composeNamingSystem(parent, null, "NamingSystem", (NamingSystem)resource, -1);
    else if (resource instanceof NutritionOrder)
      composeNutritionOrder(parent, null, "NutritionOrder", (NutritionOrder)resource, -1);
    else if (resource instanceof Observation)
      composeObservation(parent, null, "Observation", (Observation)resource, -1);
    else if (resource instanceof OperationDefinition)
      composeOperationDefinition(parent, null, "OperationDefinition", (OperationDefinition)resource, -1);
    else if (resource instanceof OperationOutcome)
      composeOperationOutcome(parent, null, "OperationOutcome", (OperationOutcome)resource, -1);
    else if (resource instanceof Organization)
      composeOrganization(parent, null, "Organization", (Organization)resource, -1);
    else if (resource instanceof Patient)
      composePatient(parent, null, "Patient", (Patient)resource, -1);
    else if (resource instanceof PaymentNotice)
      composePaymentNotice(parent, null, "PaymentNotice", (PaymentNotice)resource, -1);
    else if (resource instanceof PaymentReconciliation)
      composePaymentReconciliation(parent, null, "PaymentReconciliation", (PaymentReconciliation)resource, -1);
    else if (resource instanceof Person)
      composePerson(parent, null, "Person", (Person)resource, -1);
    else if (resource instanceof PlanDefinition)
      composePlanDefinition(parent, null, "PlanDefinition", (PlanDefinition)resource, -1);
    else if (resource instanceof Practitioner)
      composePractitioner(parent, null, "Practitioner", (Practitioner)resource, -1);
    else if (resource instanceof PractitionerRole)
      composePractitionerRole(parent, null, "PractitionerRole", (PractitionerRole)resource, -1);
    else if (resource instanceof Procedure)
      composeProcedure(parent, null, "Procedure", (Procedure)resource, -1);
    else if (resource instanceof ProcedureRequest)
      composeProcedureRequest(parent, null, "ProcedureRequest", (ProcedureRequest)resource, -1);
    else if (resource instanceof ProcessRequest)
      composeProcessRequest(parent, null, "ProcessRequest", (ProcessRequest)resource, -1);
    else if (resource instanceof ProcessResponse)
      composeProcessResponse(parent, null, "ProcessResponse", (ProcessResponse)resource, -1);
    else if (resource instanceof Provenance)
      composeProvenance(parent, null, "Provenance", (Provenance)resource, -1);
    else if (resource instanceof Questionnaire)
      composeQuestionnaire(parent, null, "Questionnaire", (Questionnaire)resource, -1);
    else if (resource instanceof QuestionnaireResponse)
      composeQuestionnaireResponse(parent, null, "QuestionnaireResponse", (QuestionnaireResponse)resource, -1);
    else if (resource instanceof RelatedPerson)
      composeRelatedPerson(parent, null, "RelatedPerson", (RelatedPerson)resource, -1);
    else if (resource instanceof RequestGroup)
      composeRequestGroup(parent, null, "RequestGroup", (RequestGroup)resource, -1);
    else if (resource instanceof ResearchStudy)
      composeResearchStudy(parent, null, "ResearchStudy", (ResearchStudy)resource, -1);
    else if (resource instanceof ResearchSubject)
      composeResearchSubject(parent, null, "ResearchSubject", (ResearchSubject)resource, -1);
    else if (resource instanceof RiskAssessment)
      composeRiskAssessment(parent, null, "RiskAssessment", (RiskAssessment)resource, -1);
    else if (resource instanceof Schedule)
      composeSchedule(parent, null, "Schedule", (Schedule)resource, -1);
    else if (resource instanceof SearchParameter)
      composeSearchParameter(parent, null, "SearchParameter", (SearchParameter)resource, -1);
    else if (resource instanceof Sequence)
      composeSequence(parent, null, "Sequence", (Sequence)resource, -1);
    else if (resource instanceof ServiceDefinition)
      composeServiceDefinition(parent, null, "ServiceDefinition", (ServiceDefinition)resource, -1);
    else if (resource instanceof Slot)
      composeSlot(parent, null, "Slot", (Slot)resource, -1);
    else if (resource instanceof Specimen)
      composeSpecimen(parent, null, "Specimen", (Specimen)resource, -1);
    else if (resource instanceof SpecimenDefinition)
      composeSpecimenDefinition(parent, null, "SpecimenDefinition", (SpecimenDefinition)resource, -1);
    else if (resource instanceof StructureDefinition)
      composeStructureDefinition(parent, null, "StructureDefinition", (StructureDefinition)resource, -1);
    else if (resource instanceof StructureMap)
      composeStructureMap(parent, null, "StructureMap", (StructureMap)resource, -1);
    else if (resource instanceof Subscription)
      composeSubscription(parent, null, "Subscription", (Subscription)resource, -1);
    else if (resource instanceof Substance)
      composeSubstance(parent, null, "Substance", (Substance)resource, -1);
    else if (resource instanceof SupplyDelivery)
      composeSupplyDelivery(parent, null, "SupplyDelivery", (SupplyDelivery)resource, -1);
    else if (resource instanceof SupplyRequest)
      composeSupplyRequest(parent, null, "SupplyRequest", (SupplyRequest)resource, -1);
    else if (resource instanceof Task)
      composeTask(parent, null, "Task", (Task)resource, -1);
    else if (resource instanceof TestReport)
      composeTestReport(parent, null, "TestReport", (TestReport)resource, -1);
    else if (resource instanceof TestScript)
      composeTestScript(parent, null, "TestScript", (TestScript)resource, -1);
    else if (resource instanceof ValueSet)
      composeValueSet(parent, null, "ValueSet", (ValueSet)resource, -1);
    else if (resource instanceof VisionPrescription)
      composeVisionPrescription(parent, null, "VisionPrescription", (VisionPrescription)resource, -1);
    else
      throw new Error("Unhandled resource type "+resource.getClass().getName());
  }

  protected void composeType(Complex parent, String parentType, String name, Type value, int index) {
    if (value == null)
      return;
    else if (value instanceof DateType)
      composeDate(parent, parentType, name, (DateType)value, index);
    else if (value instanceof DateTimeType)
      composeDateTime(parent, parentType, name, (DateTimeType)value, index);
    else if (value instanceof CodeType)
      composeCode(parent, parentType, name, (CodeType)value, index);
    else if (value instanceof StringType)
      composeString(parent, parentType, name, (StringType)value, index);
    else if (value instanceof IntegerType)
      composeInteger(parent, parentType, name, (IntegerType)value, index);
    else if (value instanceof OidType)
      composeOid(parent, parentType, name, (OidType)value, index);
    else if (value instanceof UriType)
      composeUri(parent, parentType, name, (UriType)value, index);
    else if (value instanceof UuidType)
      composeUuid(parent, parentType, name, (UuidType)value, index);
    else if (value instanceof InstantType)
      composeInstant(parent, parentType, name, (InstantType)value, index);
    else if (value instanceof BooleanType)
      composeBoolean(parent, parentType, name, (BooleanType)value, index);
    else if (value instanceof Base64BinaryType)
      composeBase64Binary(parent, parentType, name, (Base64BinaryType)value, index);
    else if (value instanceof UnsignedIntType)
      composeUnsignedInt(parent, parentType, name, (UnsignedIntType)value, index);
    else if (value instanceof MarkdownType)
      composeMarkdown(parent, parentType, name, (MarkdownType)value, index);
    else if (value instanceof TimeType)
      composeTime(parent, parentType, name, (TimeType)value, index);
    else if (value instanceof IdType)
      composeId(parent, parentType, name, (IdType)value, index);
    else if (value instanceof PositiveIntType)
      composePositiveInt(parent, parentType, name, (PositiveIntType)value, index);
    else if (value instanceof DecimalType)
      composeDecimal(parent, parentType, name, (DecimalType)value, index);
    else if (value instanceof Extension)
      composeExtension(parent, parentType, name, (Extension)value, index);
    else if (value instanceof Narrative)
      composeNarrative(parent, parentType, name, (Narrative)value, index);
    else if (value instanceof Reference)
      composeReference(parent, parentType, name, (Reference)value, index);
    else if (value instanceof Quantity)
      composeQuantity(parent, parentType, name, (Quantity)value, index);
    else if (value instanceof Period)
      composePeriod(parent, parentType, name, (Period)value, index);
    else if (value instanceof Attachment)
      composeAttachment(parent, parentType, name, (Attachment)value, index);
    else if (value instanceof Duration)
      composeDuration(parent, parentType, name, (Duration)value, index);
    else if (value instanceof Count)
      composeCount(parent, parentType, name, (Count)value, index);
    else if (value instanceof Range)
      composeRange(parent, parentType, name, (Range)value, index);
    else if (value instanceof Annotation)
      composeAnnotation(parent, parentType, name, (Annotation)value, index);
    else if (value instanceof Money)
      composeMoney(parent, parentType, name, (Money)value, index);
    else if (value instanceof Identifier)
      composeIdentifier(parent, parentType, name, (Identifier)value, index);
    else if (value instanceof Coding)
      composeCoding(parent, parentType, name, (Coding)value, index);
    else if (value instanceof Signature)
      composeSignature(parent, parentType, name, (Signature)value, index);
    else if (value instanceof SampledData)
      composeSampledData(parent, parentType, name, (SampledData)value, index);
    else if (value instanceof Ratio)
      composeRatio(parent, parentType, name, (Ratio)value, index);
    else if (value instanceof Distance)
      composeDistance(parent, parentType, name, (Distance)value, index);
    else if (value instanceof Age)
      composeAge(parent, parentType, name, (Age)value, index);
    else if (value instanceof CodeableConcept)
      composeCodeableConcept(parent, parentType, name, (CodeableConcept)value, index);
    else if (value instanceof Meta)
      composeMeta(parent, parentType, name, (Meta)value, index);
    else if (value instanceof Address)
      composeAddress(parent, parentType, name, (Address)value, index);
    else if (value instanceof TriggerDefinition)
      composeTriggerDefinition(parent, parentType, name, (TriggerDefinition)value, index);
    else if (value instanceof Contributor)
      composeContributor(parent, parentType, name, (Contributor)value, index);
    else if (value instanceof DataRequirement)
      composeDataRequirement(parent, parentType, name, (DataRequirement)value, index);
    else if (value instanceof Dosage)
      composeDosage(parent, parentType, name, (Dosage)value, index);
    else if (value instanceof RelatedArtifact)
      composeRelatedArtifact(parent, parentType, name, (RelatedArtifact)value, index);
    else if (value instanceof ContactDetail)
      composeContactDetail(parent, parentType, name, (ContactDetail)value, index);
    else if (value instanceof HumanName)
      composeHumanName(parent, parentType, name, (HumanName)value, index);
    else if (value instanceof ContactPoint)
      composeContactPoint(parent, parentType, name, (ContactPoint)value, index);
    else if (value instanceof UsageContext)
      composeUsageContext(parent, parentType, name, (UsageContext)value, index);
    else if (value instanceof Timing)
      composeTiming(parent, parentType, name, (Timing)value, index);
    else if (value instanceof ElementDefinition)
      composeElementDefinition(parent, parentType, name, (ElementDefinition)value, index);
    else if (value instanceof ParameterDefinition)
      composeParameterDefinition(parent, parentType, name, (ParameterDefinition)value, index);
    else
      throw new Error("Unhandled type");
  }

}

