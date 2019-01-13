package org.hl7.fhir.dstu2016may.formats;

/*-
 * #%L
 * org.hl7.fhir.dstu2016may
 * %%
 * Copyright (C) 2014 - 2019 Health Level 7
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */


import org.hl7.fhir.dstu2016may.formats.RdfGenerator.Complex;

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

// Generated on Sun, May 8, 2016 03:05+1000 for FHIR v1.4.0
import org.hl7.fhir.dstu2016may.model.*;
import org.hl7.fhir.utilities.Utilities;

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
      composeId(t, "Element", "id", element.getIdElement(), -1);
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


  protected void composeMarkdown(Complex parent, String parentType, String name, MarkdownType value, int index) {
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

  protected void composeDateTime(Complex parent, String parentType, String name, DateTimeType value, int index) {
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

  protected void composeCode(Complex parent, String parentType, String name, CodeType value, int index) {
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

  protected void composeDecimal(Complex parent, String parentType, String name, DecimalType value, int index) {
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

  protected void composeId(Complex parent, String parentType, String name, IdType value, int index) {
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

  protected void composeTime(Complex parent, String parentType, String name, TimeType value, int index) {
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

  protected void composePositiveInt(Complex parent, String parentType, String name, PositiveIntType value, int index) {
    if (value == null)
      return;
    Complex t = parent.predicate("fhir:"+parentType+"."+name);
    t.predicate("fhir:value", ttlLiteral(value.asStringValue()));
    composeElement(t, parentType, name, value, index);
  }

  protected void composeString(Complex parent, String parentType, String name, StringType value, int index) {
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
    if (element.hasDisplayElement())
      composeString(t, "Reference", "display", element.getDisplayElement(), -1);
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
    if (element.hasContentTypeElement())
      composeCode(t, "Signature", "contentType", element.getContentTypeElement(), -1);
    if (element.hasBlobElement())
      composeBase64Binary(t, "Signature", "blob", element.getBlobElement(), -1);
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
    if (element.hasEventNameElement())
      composeString(t, "TriggerDefinition", "eventName", element.getEventNameElement(), -1);
    if (element.hasEventTiming())
      composeType(t, "TriggerDefinition", "eventTiming", element.getEventTiming(), -1);
    if (element.hasEventData())
      composeDataRequirement(t, "TriggerDefinition", "eventData", element.getEventData(), -1);
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
    if (element.hasNameElement())
      composeString(t, "ElementDefinition", "name", element.getNameElement(), -1);
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
    if (element.hasCommentsElement())
      composeMarkdown(t, "ElementDefinition", "comments", element.getCommentsElement(), -1);
    if (element.hasRequirementsElement())
      composeMarkdown(t, "ElementDefinition", "requirements", element.getRequirementsElement(), -1);
    for (int i = 0; i < element.getAlias().size(); i++)
      composeString(t, "ElementDefinition", "alias", element.getAlias().get(i), i);
    if (element.hasMinElement())
      composeInteger(t, "ElementDefinition", "min", element.getMinElement(), -1);
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
    if (element.hasFixed())
      composeType(t, "ElementDefinition", "fixed", element.getFixed(), -1);
    if (element.hasPattern())
      composeType(t, "ElementDefinition", "pattern", element.getPattern(), -1);
    if (element.hasExample())
      composeType(t, "ElementDefinition", "example", element.getExample(), -1);
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
      composeString(t, "ElementDefinition", "discriminator", element.getDiscriminator().get(i), i);
    if (element.hasDescriptionElement())
      composeString(t, "ElementDefinition", "description", element.getDescriptionElement(), -1);
    if (element.hasOrderedElement())
      composeBoolean(t, "ElementDefinition", "ordered", element.getOrderedElement(), -1);
    if (element.hasRulesElement())
      composeEnum(t, "ElementDefinition", "rules", element.getRulesElement(), -1);
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
      composeInteger(t, "ElementDefinition", "min", element.getMinElement(), -1);
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
      composeCode(t, "ElementDefinition", "code", element.getCodeElement(), -1);
    for (int i = 0; i < element.getProfile().size(); i++)
      composeUri(t, "ElementDefinition", "profile", element.getProfile().get(i), i);
    for (int i = 0; i < element.getAggregation().size(); i++)
      composeEnum(t, "ElementDefinition", "aggregation", element.getAggregation().get(i), i);
    if (element.hasVersioningElement())
      composeEnum(t, "ElementDefinition", "versioning", element.getVersioningElement(), -1);
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
    if (element.hasWhenElement())
      composeEnum(t, "Timing", "when", element.getWhenElement(), -1);
    if (element.hasOffsetElement())
      composeUnsignedInt(t, "Timing", "offset", element.getOffsetElement(), -1);
  }

  protected void composeModuleMetadata(Complex parent, String parentType, String name, ModuleMetadata element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeElement(t, "ModuleMetadata", name, element, index);
    if (element.hasUrlElement())
      composeUri(t, "ModuleMetadata", "url", element.getUrlElement(), -1);
    for (int i = 0; i < element.getIdentifier().size(); i++)
      composeIdentifier(t, "ModuleMetadata", "identifier", element.getIdentifier().get(i), i);
    if (element.hasVersionElement())
      composeString(t, "ModuleMetadata", "version", element.getVersionElement(), -1);
    if (element.hasNameElement())
      composeString(t, "ModuleMetadata", "name", element.getNameElement(), -1);
    if (element.hasTitleElement())
      composeString(t, "ModuleMetadata", "title", element.getTitleElement(), -1);
    if (element.hasTypeElement())
      composeEnum(t, "ModuleMetadata", "type", element.getTypeElement(), -1);
    if (element.hasStatusElement())
      composeEnum(t, "ModuleMetadata", "status", element.getStatusElement(), -1);
    if (element.hasExperimentalElement())
      composeBoolean(t, "ModuleMetadata", "experimental", element.getExperimentalElement(), -1);
    if (element.hasDescriptionElement())
      composeString(t, "ModuleMetadata", "description", element.getDescriptionElement(), -1);
    if (element.hasPurposeElement())
      composeString(t, "ModuleMetadata", "purpose", element.getPurposeElement(), -1);
    if (element.hasUsageElement())
      composeString(t, "ModuleMetadata", "usage", element.getUsageElement(), -1);
    if (element.hasPublicationDateElement())
      composeDate(t, "ModuleMetadata", "publicationDate", element.getPublicationDateElement(), -1);
    if (element.hasLastReviewDateElement())
      composeDate(t, "ModuleMetadata", "lastReviewDate", element.getLastReviewDateElement(), -1);
    if (element.hasEffectivePeriod())
      composePeriod(t, "ModuleMetadata", "effectivePeriod", element.getEffectivePeriod(), -1);
    for (int i = 0; i < element.getCoverage().size(); i++)
      composeModuleMetadataModuleMetadataCoverageComponent(t, "ModuleMetadata", "coverage", element.getCoverage().get(i), i);
    for (int i = 0; i < element.getTopic().size(); i++)
      composeCodeableConcept(t, "ModuleMetadata", "topic", element.getTopic().get(i), i);
    for (int i = 0; i < element.getContributor().size(); i++)
      composeModuleMetadataModuleMetadataContributorComponent(t, "ModuleMetadata", "contributor", element.getContributor().get(i), i);
    if (element.hasPublisherElement())
      composeString(t, "ModuleMetadata", "publisher", element.getPublisherElement(), -1);
    for (int i = 0; i < element.getContact().size(); i++)
      composeModuleMetadataModuleMetadataContactComponent(t, "ModuleMetadata", "contact", element.getContact().get(i), i);
    if (element.hasCopyrightElement())
      composeString(t, "ModuleMetadata", "copyright", element.getCopyrightElement(), -1);
    for (int i = 0; i < element.getRelatedResource().size(); i++)
      composeModuleMetadataModuleMetadataRelatedResourceComponent(t, "ModuleMetadata", "relatedResource", element.getRelatedResource().get(i), i);
  }

  protected void composeModuleMetadataModuleMetadataCoverageComponent(Complex parent, String parentType, String name, ModuleMetadata.ModuleMetadataCoverageComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeElement(t, "coverage", name, element, index);
    if (element.hasFocus())
      composeCoding(t, "ModuleMetadata", "focus", element.getFocus(), -1);
    if (element.hasValue())
      composeCodeableConcept(t, "ModuleMetadata", "value", element.getValue(), -1);
  }

  protected void composeModuleMetadataModuleMetadataContributorComponent(Complex parent, String parentType, String name, ModuleMetadata.ModuleMetadataContributorComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeElement(t, "contributor", name, element, index);
    if (element.hasTypeElement())
      composeEnum(t, "ModuleMetadata", "type", element.getTypeElement(), -1);
    if (element.hasNameElement())
      composeString(t, "ModuleMetadata", "name", element.getNameElement(), -1);
    for (int i = 0; i < element.getContact().size(); i++)
      composeModuleMetadataModuleMetadataContributorContactComponent(t, "ModuleMetadata", "contact", element.getContact().get(i), i);
  }

  protected void composeModuleMetadataModuleMetadataContributorContactComponent(Complex parent, String parentType, String name, ModuleMetadata.ModuleMetadataContributorContactComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeElement(t, "contact", name, element, index);
    if (element.hasNameElement())
      composeString(t, "ModuleMetadata", "name", element.getNameElement(), -1);
    for (int i = 0; i < element.getTelecom().size(); i++)
      composeContactPoint(t, "ModuleMetadata", "telecom", element.getTelecom().get(i), i);
  }

  protected void composeModuleMetadataModuleMetadataContactComponent(Complex parent, String parentType, String name, ModuleMetadata.ModuleMetadataContactComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeElement(t, "contact", name, element, index);
    if (element.hasNameElement())
      composeString(t, "ModuleMetadata", "name", element.getNameElement(), -1);
    for (int i = 0; i < element.getTelecom().size(); i++)
      composeContactPoint(t, "ModuleMetadata", "telecom", element.getTelecom().get(i), i);
  }

  protected void composeModuleMetadataModuleMetadataRelatedResourceComponent(Complex parent, String parentType, String name, ModuleMetadata.ModuleMetadataRelatedResourceComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeElement(t, "relatedResource", name, element, index);
    if (element.hasTypeElement())
      composeEnum(t, "ModuleMetadata", "type", element.getTypeElement(), -1);
    if (element.hasDocument())
      composeAttachment(t, "ModuleMetadata", "document", element.getDocument(), -1);
    if (element.hasResource())
      composeReference(t, "ModuleMetadata", "resource", element.getResource(), -1);
  }

  protected void composeActionDefinition(Complex parent, String parentType, String name, ActionDefinition element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeElement(t, "ActionDefinition", name, element, index);
    if (element.hasActionIdentifier())
      composeIdentifier(t, "ActionDefinition", "actionIdentifier", element.getActionIdentifier(), -1);
    if (element.hasLabelElement())
      composeString(t, "ActionDefinition", "label", element.getLabelElement(), -1);
    if (element.hasTitleElement())
      composeString(t, "ActionDefinition", "title", element.getTitleElement(), -1);
    if (element.hasDescriptionElement())
      composeString(t, "ActionDefinition", "description", element.getDescriptionElement(), -1);
    if (element.hasTextEquivalentElement())
      composeString(t, "ActionDefinition", "textEquivalent", element.getTextEquivalentElement(), -1);
    for (int i = 0; i < element.getConcept().size(); i++)
      composeCodeableConcept(t, "ActionDefinition", "concept", element.getConcept().get(i), i);
    for (int i = 0; i < element.getSupportingEvidence().size(); i++)
      composeAttachment(t, "ActionDefinition", "supportingEvidence", element.getSupportingEvidence().get(i), i);
    for (int i = 0; i < element.getDocumentation().size(); i++)
      composeAttachment(t, "ActionDefinition", "documentation", element.getDocumentation().get(i), i);
    if (element.hasRelatedAction())
      composeActionDefinitionActionDefinitionRelatedActionComponent(t, "ActionDefinition", "relatedAction", element.getRelatedAction(), -1);
    for (int i = 0; i < element.getParticipantType().size(); i++)
      composeEnum(t, "ActionDefinition", "participantType", element.getParticipantType().get(i), i);
    if (element.hasTypeElement())
      composeEnum(t, "ActionDefinition", "type", element.getTypeElement(), -1);
    for (int i = 0; i < element.getBehavior().size(); i++)
      composeActionDefinitionActionDefinitionBehaviorComponent(t, "ActionDefinition", "behavior", element.getBehavior().get(i), i);
    if (element.hasResource())
      composeReference(t, "ActionDefinition", "resource", element.getResource(), -1);
    for (int i = 0; i < element.getCustomization().size(); i++)
      composeActionDefinitionActionDefinitionCustomizationComponent(t, "ActionDefinition", "customization", element.getCustomization().get(i), i);
    for (int i = 0; i < element.getAction().size(); i++)
      composeActionDefinition(t, "ActionDefinition", "action", element.getAction().get(i), i);
  }

  protected void composeActionDefinitionActionDefinitionRelatedActionComponent(Complex parent, String parentType, String name, ActionDefinition.ActionDefinitionRelatedActionComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeElement(t, "relatedAction", name, element, index);
    if (element.hasActionIdentifier())
      composeIdentifier(t, "ActionDefinition", "actionIdentifier", element.getActionIdentifier(), -1);
    if (element.hasRelationshipElement())
      composeEnum(t, "ActionDefinition", "relationship", element.getRelationshipElement(), -1);
    if (element.hasOffset())
      composeType(t, "ActionDefinition", "offset", element.getOffset(), -1);
    if (element.hasAnchorElement())
      composeEnum(t, "ActionDefinition", "anchor", element.getAnchorElement(), -1);
  }

  protected void composeActionDefinitionActionDefinitionBehaviorComponent(Complex parent, String parentType, String name, ActionDefinition.ActionDefinitionBehaviorComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeElement(t, "behavior", name, element, index);
    if (element.hasType())
      composeCoding(t, "ActionDefinition", "type", element.getType(), -1);
    if (element.hasValue())
      composeCoding(t, "ActionDefinition", "value", element.getValue(), -1);
  }

  protected void composeActionDefinitionActionDefinitionCustomizationComponent(Complex parent, String parentType, String name, ActionDefinition.ActionDefinitionCustomizationComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeElement(t, "customization", name, element, index);
    if (element.hasPathElement())
      composeString(t, "ActionDefinition", "path", element.getPathElement(), -1);
    if (element.hasExpressionElement())
      composeString(t, "ActionDefinition", "expression", element.getExpressionElement(), -1);
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
    for (int i = 0; i < element.getFamily().size(); i++)
      composeString(t, "HumanName", "family", element.getFamily().get(i), i);
    for (int i = 0; i < element.getGiven().size(); i++)
      composeString(t, "HumanName", "given", element.getGiven().get(i), i);
    for (int i = 0; i < element.getPrefix().size(); i++)
      composeString(t, "HumanName", "prefix", element.getPrefix().get(i), i);
    for (int i = 0; i < element.getSuffix().size(); i++)
      composeString(t, "HumanName", "suffix", element.getSuffix().get(i), i);
    if (element.hasPeriod())
      composePeriod(t, "HumanName", "period", element.getPeriod(), -1);
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
    if (element.hasProfile())
      composeReference(t, "DataRequirement", "profile", element.getProfile(), -1);
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
    for (int i = 0; i < element.getValueCode().size(); i++)
      composeCode(t, "DataRequirement", "valueCode", element.getValueCode().get(i), i);
    for (int i = 0; i < element.getValueCoding().size(); i++)
      composeCoding(t, "DataRequirement", "valueCoding", element.getValueCoding().get(i), i);
    for (int i = 0; i < element.getValueCodeableConcept().size(); i++)
      composeCodeableConcept(t, "DataRequirement", "valueCodeableConcept", element.getValueCodeableConcept().get(i), i);
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
      composeCode(t, "ParameterDefinition", "use", element.getUseElement(), -1);
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
    if (element.hasNameElement())
      composeString(t, "Account", "name", element.getNameElement(), -1);
    if (element.hasType())
      composeCodeableConcept(t, "Account", "type", element.getType(), -1);
    if (element.hasStatusElement())
      composeEnum(t, "Account", "status", element.getStatusElement(), -1);
    if (element.hasActivePeriod())
      composePeriod(t, "Account", "activePeriod", element.getActivePeriod(), -1);
    if (element.hasCurrency())
      composeCoding(t, "Account", "currency", element.getCurrency(), -1);
    if (element.hasBalance())
      composeQuantity(t, "Account", "balance", element.getBalance(), -1);
    if (element.hasCoveragePeriod())
      composePeriod(t, "Account", "coveragePeriod", element.getCoveragePeriod(), -1);
    if (element.hasSubject())
      composeReference(t, "Account", "subject", element.getSubject(), -1);
    if (element.hasOwner())
      composeReference(t, "Account", "owner", element.getOwner(), -1);
    if (element.hasDescriptionElement())
      composeString(t, "Account", "description", element.getDescriptionElement(), -1);
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
    if (element.hasStatusElement())
      composeEnum(t, "AllergyIntolerance", "status", element.getStatusElement(), -1);
    if (element.hasTypeElement())
      composeEnum(t, "AllergyIntolerance", "type", element.getTypeElement(), -1);
    if (element.hasCategoryElement())
      composeEnum(t, "AllergyIntolerance", "category", element.getCategoryElement(), -1);
    if (element.hasCriticalityElement())
      composeEnum(t, "AllergyIntolerance", "criticality", element.getCriticalityElement(), -1);
    if (element.hasSubstance())
      composeCodeableConcept(t, "AllergyIntolerance", "substance", element.getSubstance(), -1);
    if (element.hasPatient())
      composeReference(t, "AllergyIntolerance", "patient", element.getPatient(), -1);
    if (element.hasRecordedDateElement())
      composeDateTime(t, "AllergyIntolerance", "recordedDate", element.getRecordedDateElement(), -1);
    if (element.hasRecorder())
      composeReference(t, "AllergyIntolerance", "recorder", element.getRecorder(), -1);
    if (element.hasReporter())
      composeReference(t, "AllergyIntolerance", "reporter", element.getReporter(), -1);
    if (element.hasOnsetElement())
      composeDateTime(t, "AllergyIntolerance", "onset", element.getOnsetElement(), -1);
    if (element.hasLastOccurenceElement())
      composeDateTime(t, "AllergyIntolerance", "lastOccurence", element.getLastOccurenceElement(), -1);
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
    if (element.hasCertaintyElement())
      composeEnum(t, "AllergyIntolerance", "certainty", element.getCertaintyElement(), -1);
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
    if (element.hasReason())
      composeCodeableConcept(t, "Appointment", "reason", element.getReason(), -1);
    if (element.hasPriorityElement())
      composeUnsignedInt(t, "Appointment", "priority", element.getPriorityElement(), -1);
    if (element.hasDescriptionElement())
      composeString(t, "Appointment", "description", element.getDescriptionElement(), -1);
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
    for (int i = 0; i < element.getParticipant().size(); i++)
      composeAppointmentAppointmentParticipantComponent(t, "Appointment", "participant", element.getParticipant().get(i), i);
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
      composeCode(t, "AppointmentResponse", "participantStatus", element.getParticipantStatusElement(), -1);
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
    if (element.hasRecordedElement())
      composeInstant(t, "AuditEvent", "recorded", element.getRecordedElement(), -1);
    if (element.hasOutcomeElement())
      composeEnum(t, "AuditEvent", "outcome", element.getOutcomeElement(), -1);
    if (element.hasOutcomeDescElement())
      composeString(t, "AuditEvent", "outcomeDesc", element.getOutcomeDescElement(), -1);
    for (int i = 0; i < element.getPurposeOfEvent().size(); i++)
      composeCoding(t, "AuditEvent", "purposeOfEvent", element.getPurposeOfEvent().get(i), i);
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
      composeCoding(t, "AuditEvent", "purposeOfUse", element.getPurposeOfUse().get(i), i);
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
    if (element.hasValueElement())
      composeBase64Binary(t, "AuditEvent", "value", element.getValueElement(), -1);
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
    if (element.hasContentElement())
      composeBase64Binary(t, "Binary", "content", element.getContentElement(), -1);
  }

  protected void composeBodySite(Complex parent, String parentType, String name, BodySite element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "BodySite", name, element, index);
    if (element.hasPatient())
      composeReference(t, "BodySite", "patient", element.getPatient(), -1);
    for (int i = 0; i < element.getIdentifier().size(); i++)
      composeIdentifier(t, "BodySite", "identifier", element.getIdentifier().get(i), i);
    if (element.hasCode())
      composeCodeableConcept(t, "BodySite", "code", element.getCode(), -1);
    for (int i = 0; i < element.getModifier().size(); i++)
      composeCodeableConcept(t, "BodySite", "modifier", element.getModifier().get(i), i);
    if (element.hasDescriptionElement())
      composeString(t, "BodySite", "description", element.getDescriptionElement(), -1);
    for (int i = 0; i < element.getImage().size(); i++)
      composeAttachment(t, "BodySite", "image", element.getImage().get(i), i);
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
    if (element.hasSubject())
      composeReference(t, "CarePlan", "subject", element.getSubject(), -1);
    if (element.hasStatusElement())
      composeEnum(t, "CarePlan", "status", element.getStatusElement(), -1);
    if (element.hasContext())
      composeReference(t, "CarePlan", "context", element.getContext(), -1);
    if (element.hasPeriod())
      composePeriod(t, "CarePlan", "period", element.getPeriod(), -1);
    for (int i = 0; i < element.getAuthor().size(); i++)
      composeReference(t, "CarePlan", "author", element.getAuthor().get(i), i);
    if (element.hasModifiedElement())
      composeDateTime(t, "CarePlan", "modified", element.getModifiedElement(), -1);
    for (int i = 0; i < element.getCategory().size(); i++)
      composeCodeableConcept(t, "CarePlan", "category", element.getCategory().get(i), i);
    if (element.hasDescriptionElement())
      composeString(t, "CarePlan", "description", element.getDescriptionElement(), -1);
    for (int i = 0; i < element.getAddresses().size(); i++)
      composeReference(t, "CarePlan", "addresses", element.getAddresses().get(i), i);
    for (int i = 0; i < element.getSupport().size(); i++)
      composeReference(t, "CarePlan", "support", element.getSupport().get(i), i);
    for (int i = 0; i < element.getRelatedPlan().size(); i++)
      composeCarePlanCarePlanRelatedPlanComponent(t, "CarePlan", "relatedPlan", element.getRelatedPlan().get(i), i);
    for (int i = 0; i < element.getParticipant().size(); i++)
      composeCarePlanCarePlanParticipantComponent(t, "CarePlan", "participant", element.getParticipant().get(i), i);
    for (int i = 0; i < element.getGoal().size(); i++)
      composeReference(t, "CarePlan", "goal", element.getGoal().get(i), i);
    for (int i = 0; i < element.getActivity().size(); i++)
      composeCarePlanCarePlanActivityComponent(t, "CarePlan", "activity", element.getActivity().get(i), i);
    if (element.hasNote())
      composeAnnotation(t, "CarePlan", "note", element.getNote(), -1);
  }

  protected void composeCarePlanCarePlanRelatedPlanComponent(Complex parent, String parentType, String name, CarePlan.CarePlanRelatedPlanComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "relatedPlan", name, element, index);
    if (element.hasCodeElement())
      composeEnum(t, "CarePlan", "code", element.getCodeElement(), -1);
    if (element.hasPlan())
      composeReference(t, "CarePlan", "plan", element.getPlan(), -1);
  }

  protected void composeCarePlanCarePlanParticipantComponent(Complex parent, String parentType, String name, CarePlan.CarePlanParticipantComponent element, int index) {
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
      composeCodeableConcept(t, "CarePlan", "role", element.getRole(), -1);
    if (element.hasMember())
      composeReference(t, "CarePlan", "member", element.getMember(), -1);
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
    for (int i = 0; i < element.getActionResulting().size(); i++)
      composeReference(t, "CarePlan", "actionResulting", element.getActionResulting().get(i), i);
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
    if (element.hasCategory())
      composeCodeableConcept(t, "CarePlan", "category", element.getCategory(), -1);
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
    if (element.hasStatusReason())
      composeCodeableConcept(t, "CarePlan", "statusReason", element.getStatusReason(), -1);
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
    if (element.hasStatus())
      composeCodeableConcept(t, "CareTeam", "status", element.getStatus(), -1);
    for (int i = 0; i < element.getType().size(); i++)
      composeCodeableConcept(t, "CareTeam", "type", element.getType().get(i), i);
    if (element.hasNameElement())
      composeString(t, "CareTeam", "name", element.getNameElement(), -1);
    if (element.hasSubject())
      composeReference(t, "CareTeam", "subject", element.getSubject(), -1);
    if (element.hasPeriod())
      composePeriod(t, "CareTeam", "period", element.getPeriod(), -1);
    for (int i = 0; i < element.getParticipant().size(); i++)
      composeCareTeamCareTeamParticipantComponent(t, "CareTeam", "participant", element.getParticipant().get(i), i);
    if (element.hasManagingOrganization())
      composeReference(t, "CareTeam", "managingOrganization", element.getManagingOrganization(), -1);
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
    if (element.hasPeriod())
      composePeriod(t, "CareTeam", "period", element.getPeriod(), -1);
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
    if (element.hasTypeElement())
      composeEnum(t, "Claim", "type", element.getTypeElement(), -1);
    for (int i = 0; i < element.getSubType().size(); i++)
      composeCoding(t, "Claim", "subType", element.getSubType().get(i), i);
    for (int i = 0; i < element.getIdentifier().size(); i++)
      composeIdentifier(t, "Claim", "identifier", element.getIdentifier().get(i), i);
    if (element.hasRuleset())
      composeCoding(t, "Claim", "ruleset", element.getRuleset(), -1);
    if (element.hasOriginalRuleset())
      composeCoding(t, "Claim", "originalRuleset", element.getOriginalRuleset(), -1);
    if (element.hasCreatedElement())
      composeDateTime(t, "Claim", "created", element.getCreatedElement(), -1);
    if (element.hasBillablePeriod())
      composePeriod(t, "Claim", "billablePeriod", element.getBillablePeriod(), -1);
    if (element.hasTarget())
      composeType(t, "Claim", "target", element.getTarget(), -1);
    if (element.hasProvider())
      composeType(t, "Claim", "provider", element.getProvider(), -1);
    if (element.hasOrganization())
      composeType(t, "Claim", "organization", element.getOrganization(), -1);
    if (element.hasUseElement())
      composeEnum(t, "Claim", "use", element.getUseElement(), -1);
    if (element.hasPriority())
      composeCoding(t, "Claim", "priority", element.getPriority(), -1);
    if (element.hasFundsReserve())
      composeCoding(t, "Claim", "fundsReserve", element.getFundsReserve(), -1);
    if (element.hasEnterer())
      composeType(t, "Claim", "enterer", element.getEnterer(), -1);
    if (element.hasFacility())
      composeType(t, "Claim", "facility", element.getFacility(), -1);
    for (int i = 0; i < element.getRelated().size(); i++)
      composeClaimRelatedClaimsComponent(t, "Claim", "related", element.getRelated().get(i), i);
    if (element.hasPrescription())
      composeType(t, "Claim", "prescription", element.getPrescription(), -1);
    if (element.hasOriginalPrescription())
      composeType(t, "Claim", "originalPrescription", element.getOriginalPrescription(), -1);
    if (element.hasPayee())
      composeClaimPayeeComponent(t, "Claim", "payee", element.getPayee(), -1);
    if (element.hasReferral())
      composeType(t, "Claim", "referral", element.getReferral(), -1);
    for (int i = 0; i < element.getOccurrenceCode().size(); i++)
      composeCoding(t, "Claim", "occurrenceCode", element.getOccurrenceCode().get(i), i);
    for (int i = 0; i < element.getOccurenceSpanCode().size(); i++)
      composeCoding(t, "Claim", "occurenceSpanCode", element.getOccurenceSpanCode().get(i), i);
    for (int i = 0; i < element.getValueCode().size(); i++)
      composeCoding(t, "Claim", "valueCode", element.getValueCode().get(i), i);
    for (int i = 0; i < element.getDiagnosis().size(); i++)
      composeClaimDiagnosisComponent(t, "Claim", "diagnosis", element.getDiagnosis().get(i), i);
    for (int i = 0; i < element.getProcedure().size(); i++)
      composeClaimProcedureComponent(t, "Claim", "procedure", element.getProcedure().get(i), i);
    for (int i = 0; i < element.getSpecialCondition().size(); i++)
      composeCoding(t, "Claim", "specialCondition", element.getSpecialCondition().get(i), i);
    if (element.hasPatient())
      composeType(t, "Claim", "patient", element.getPatient(), -1);
    for (int i = 0; i < element.getCoverage().size(); i++)
      composeClaimCoverageComponent(t, "Claim", "coverage", element.getCoverage().get(i), i);
    if (element.hasAccidentDateElement())
      composeDate(t, "Claim", "accidentDate", element.getAccidentDateElement(), -1);
    if (element.hasAccidentType())
      composeCoding(t, "Claim", "accidentType", element.getAccidentType(), -1);
    if (element.hasAccidentLocation())
      composeType(t, "Claim", "accidentLocation", element.getAccidentLocation(), -1);
    for (int i = 0; i < element.getInterventionException().size(); i++)
      composeCoding(t, "Claim", "interventionException", element.getInterventionException().get(i), i);
    for (int i = 0; i < element.getOnset().size(); i++)
      composeClaimOnsetComponent(t, "Claim", "onset", element.getOnset().get(i), i);
    if (element.hasEmploymentImpacted())
      composePeriod(t, "Claim", "employmentImpacted", element.getEmploymentImpacted(), -1);
    if (element.hasHospitalization())
      composePeriod(t, "Claim", "hospitalization", element.getHospitalization(), -1);
    for (int i = 0; i < element.getItem().size(); i++)
      composeClaimItemsComponent(t, "Claim", "item", element.getItem().get(i), i);
    if (element.hasTotal())
      composeQuantity(t, "Claim", "total", element.getTotal(), -1);
    for (int i = 0; i < element.getAdditionalMaterial().size(); i++)
      composeCoding(t, "Claim", "additionalMaterial", element.getAdditionalMaterial().get(i), i);
    for (int i = 0; i < element.getMissingTeeth().size(); i++)
      composeClaimMissingTeethComponent(t, "Claim", "missingTeeth", element.getMissingTeeth().get(i), i);
  }

  protected void composeClaimRelatedClaimsComponent(Complex parent, String parentType, String name, Claim.RelatedClaimsComponent element, int index) {
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
      composeType(t, "Claim", "claim", element.getClaim(), -1);
    if (element.hasRelationship())
      composeCoding(t, "Claim", "relationship", element.getRelationship(), -1);
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
      composeCoding(t, "Claim", "type", element.getType(), -1);
    if (element.hasParty())
      composeType(t, "Claim", "party", element.getParty(), -1);
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
      composeCoding(t, "Claim", "diagnosis", element.getDiagnosis(), -1);
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

  protected void composeClaimCoverageComponent(Complex parent, String parentType, String name, Claim.CoverageComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "coverage", name, element, index);
    if (element.hasSequenceElement())
      composePositiveInt(t, "Claim", "sequence", element.getSequenceElement(), -1);
    if (element.hasFocalElement())
      composeBoolean(t, "Claim", "focal", element.getFocalElement(), -1);
    if (element.hasCoverage())
      composeType(t, "Claim", "coverage", element.getCoverage(), -1);
    if (element.hasBusinessArrangementElement())
      composeString(t, "Claim", "businessArrangement", element.getBusinessArrangementElement(), -1);
    for (int i = 0; i < element.getPreAuthRef().size(); i++)
      composeString(t, "Claim", "preAuthRef", element.getPreAuthRef().get(i), i);
    if (element.hasClaimResponse())
      composeReference(t, "Claim", "claimResponse", element.getClaimResponse(), -1);
    if (element.hasOriginalRuleset())
      composeCoding(t, "Claim", "originalRuleset", element.getOriginalRuleset(), -1);
  }

  protected void composeClaimOnsetComponent(Complex parent, String parentType, String name, Claim.OnsetComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "onset", name, element, index);
    if (element.hasTime())
      composeType(t, "Claim", "time", element.getTime(), -1);
    if (element.hasType())
      composeCoding(t, "Claim", "type", element.getType(), -1);
  }

  protected void composeClaimItemsComponent(Complex parent, String parentType, String name, Claim.ItemsComponent element, int index) {
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
    if (element.hasType())
      composeCoding(t, "Claim", "type", element.getType(), -1);
    if (element.hasProvider())
      composeType(t, "Claim", "provider", element.getProvider(), -1);
    if (element.hasSupervisor())
      composeType(t, "Claim", "supervisor", element.getSupervisor(), -1);
    if (element.hasProviderQualification())
      composeCoding(t, "Claim", "providerQualification", element.getProviderQualification(), -1);
    for (int i = 0; i < element.getDiagnosisLinkId().size(); i++)
      composePositiveInt(t, "Claim", "diagnosisLinkId", element.getDiagnosisLinkId().get(i), i);
    if (element.hasService())
      composeCoding(t, "Claim", "service", element.getService(), -1);
    for (int i = 0; i < element.getServiceModifier().size(); i++)
      composeCoding(t, "Claim", "serviceModifier", element.getServiceModifier().get(i), i);
    for (int i = 0; i < element.getModifier().size(); i++)
      composeCoding(t, "Claim", "modifier", element.getModifier().get(i), i);
    for (int i = 0; i < element.getProgramCode().size(); i++)
      composeCoding(t, "Claim", "programCode", element.getProgramCode().get(i), i);
    if (element.hasServiced())
      composeType(t, "Claim", "serviced", element.getServiced(), -1);
    if (element.hasPlace())
      composeCoding(t, "Claim", "place", element.getPlace(), -1);
    if (element.hasQuantity())
      composeQuantity(t, "Claim", "quantity", element.getQuantity(), -1);
    if (element.hasUnitPrice())
      composeQuantity(t, "Claim", "unitPrice", element.getUnitPrice(), -1);
    if (element.hasFactorElement())
      composeDecimal(t, "Claim", "factor", element.getFactorElement(), -1);
    if (element.hasPointsElement())
      composeDecimal(t, "Claim", "points", element.getPointsElement(), -1);
    if (element.hasNet())
      composeQuantity(t, "Claim", "net", element.getNet(), -1);
    for (int i = 0; i < element.getUdi().size(); i++)
      composeReference(t, "Claim", "udi", element.getUdi().get(i), i);
    if (element.hasBodySite())
      composeCoding(t, "Claim", "bodySite", element.getBodySite(), -1);
    for (int i = 0; i < element.getSubSite().size(); i++)
      composeCoding(t, "Claim", "subSite", element.getSubSite().get(i), i);
    for (int i = 0; i < element.getDetail().size(); i++)
      composeClaimDetailComponent(t, "Claim", "detail", element.getDetail().get(i), i);
    if (element.hasProsthesis())
      composeClaimProsthesisComponent(t, "Claim", "prosthesis", element.getProsthesis(), -1);
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
    if (element.hasType())
      composeCoding(t, "Claim", "type", element.getType(), -1);
    if (element.hasService())
      composeCoding(t, "Claim", "service", element.getService(), -1);
    for (int i = 0; i < element.getProgramCode().size(); i++)
      composeCoding(t, "Claim", "programCode", element.getProgramCode().get(i), i);
    if (element.hasQuantity())
      composeQuantity(t, "Claim", "quantity", element.getQuantity(), -1);
    if (element.hasUnitPrice())
      composeQuantity(t, "Claim", "unitPrice", element.getUnitPrice(), -1);
    if (element.hasFactorElement())
      composeDecimal(t, "Claim", "factor", element.getFactorElement(), -1);
    if (element.hasPointsElement())
      composeDecimal(t, "Claim", "points", element.getPointsElement(), -1);
    if (element.hasNet())
      composeQuantity(t, "Claim", "net", element.getNet(), -1);
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
    if (element.hasType())
      composeCoding(t, "Claim", "type", element.getType(), -1);
    if (element.hasService())
      composeCoding(t, "Claim", "service", element.getService(), -1);
    for (int i = 0; i < element.getProgramCode().size(); i++)
      composeCoding(t, "Claim", "programCode", element.getProgramCode().get(i), i);
    if (element.hasQuantity())
      composeQuantity(t, "Claim", "quantity", element.getQuantity(), -1);
    if (element.hasUnitPrice())
      composeQuantity(t, "Claim", "unitPrice", element.getUnitPrice(), -1);
    if (element.hasFactorElement())
      composeDecimal(t, "Claim", "factor", element.getFactorElement(), -1);
    if (element.hasPointsElement())
      composeDecimal(t, "Claim", "points", element.getPointsElement(), -1);
    if (element.hasNet())
      composeQuantity(t, "Claim", "net", element.getNet(), -1);
    for (int i = 0; i < element.getUdi().size(); i++)
      composeReference(t, "Claim", "udi", element.getUdi().get(i), i);
  }

  protected void composeClaimProsthesisComponent(Complex parent, String parentType, String name, Claim.ProsthesisComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "prosthesis", name, element, index);
    if (element.hasInitialElement())
      composeBoolean(t, "Claim", "initial", element.getInitialElement(), -1);
    if (element.hasPriorDateElement())
      composeDate(t, "Claim", "priorDate", element.getPriorDateElement(), -1);
    if (element.hasPriorMaterial())
      composeCoding(t, "Claim", "priorMaterial", element.getPriorMaterial(), -1);
  }

  protected void composeClaimMissingTeethComponent(Complex parent, String parentType, String name, Claim.MissingTeethComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "missingTeeth", name, element, index);
    if (element.hasTooth())
      composeCoding(t, "Claim", "tooth", element.getTooth(), -1);
    if (element.hasReason())
      composeCoding(t, "Claim", "reason", element.getReason(), -1);
    if (element.hasExtractionDateElement())
      composeDate(t, "Claim", "extractionDate", element.getExtractionDateElement(), -1);
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
    if (element.hasRequest())
      composeType(t, "ClaimResponse", "request", element.getRequest(), -1);
    if (element.hasRuleset())
      composeCoding(t, "ClaimResponse", "ruleset", element.getRuleset(), -1);
    if (element.hasOriginalRuleset())
      composeCoding(t, "ClaimResponse", "originalRuleset", element.getOriginalRuleset(), -1);
    if (element.hasCreatedElement())
      composeDateTime(t, "ClaimResponse", "created", element.getCreatedElement(), -1);
    if (element.hasOrganization())
      composeType(t, "ClaimResponse", "organization", element.getOrganization(), -1);
    if (element.hasRequestProvider())
      composeType(t, "ClaimResponse", "requestProvider", element.getRequestProvider(), -1);
    if (element.hasRequestOrganization())
      composeType(t, "ClaimResponse", "requestOrganization", element.getRequestOrganization(), -1);
    if (element.hasOutcomeElement())
      composeEnum(t, "ClaimResponse", "outcome", element.getOutcomeElement(), -1);
    if (element.hasDispositionElement())
      composeString(t, "ClaimResponse", "disposition", element.getDispositionElement(), -1);
    if (element.hasPayeeType())
      composeCoding(t, "ClaimResponse", "payeeType", element.getPayeeType(), -1);
    for (int i = 0; i < element.getItem().size(); i++)
      composeClaimResponseItemsComponent(t, "ClaimResponse", "item", element.getItem().get(i), i);
    for (int i = 0; i < element.getAddItem().size(); i++)
      composeClaimResponseAddedItemComponent(t, "ClaimResponse", "addItem", element.getAddItem().get(i), i);
    for (int i = 0; i < element.getError().size(); i++)
      composeClaimResponseErrorsComponent(t, "ClaimResponse", "error", element.getError().get(i), i);
    if (element.hasTotalCost())
      composeQuantity(t, "ClaimResponse", "totalCost", element.getTotalCost(), -1);
    if (element.hasUnallocDeductable())
      composeQuantity(t, "ClaimResponse", "unallocDeductable", element.getUnallocDeductable(), -1);
    if (element.hasTotalBenefit())
      composeQuantity(t, "ClaimResponse", "totalBenefit", element.getTotalBenefit(), -1);
    if (element.hasPaymentAdjustment())
      composeQuantity(t, "ClaimResponse", "paymentAdjustment", element.getPaymentAdjustment(), -1);
    if (element.hasPaymentAdjustmentReason())
      composeCoding(t, "ClaimResponse", "paymentAdjustmentReason", element.getPaymentAdjustmentReason(), -1);
    if (element.hasPaymentDateElement())
      composeDate(t, "ClaimResponse", "paymentDate", element.getPaymentDateElement(), -1);
    if (element.hasPaymentAmount())
      composeQuantity(t, "ClaimResponse", "paymentAmount", element.getPaymentAmount(), -1);
    if (element.hasPaymentRef())
      composeIdentifier(t, "ClaimResponse", "paymentRef", element.getPaymentRef(), -1);
    if (element.hasReserved())
      composeCoding(t, "ClaimResponse", "reserved", element.getReserved(), -1);
    if (element.hasForm())
      composeCoding(t, "ClaimResponse", "form", element.getForm(), -1);
    for (int i = 0; i < element.getNote().size(); i++)
      composeClaimResponseNotesComponent(t, "ClaimResponse", "note", element.getNote().get(i), i);
    for (int i = 0; i < element.getCoverage().size(); i++)
      composeClaimResponseCoverageComponent(t, "ClaimResponse", "coverage", element.getCoverage().get(i), i);
  }

  protected void composeClaimResponseItemsComponent(Complex parent, String parentType, String name, ClaimResponse.ItemsComponent element, int index) {
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
      composeClaimResponseItemAdjudicationComponent(t, "ClaimResponse", "adjudication", element.getAdjudication().get(i), i);
    for (int i = 0; i < element.getDetail().size(); i++)
      composeClaimResponseItemDetailComponent(t, "ClaimResponse", "detail", element.getDetail().get(i), i);
  }

  protected void composeClaimResponseItemAdjudicationComponent(Complex parent, String parentType, String name, ClaimResponse.ItemAdjudicationComponent element, int index) {
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
      composeCoding(t, "ClaimResponse", "category", element.getCategory(), -1);
    if (element.hasReason())
      composeCoding(t, "ClaimResponse", "reason", element.getReason(), -1);
    if (element.hasAmount())
      composeQuantity(t, "ClaimResponse", "amount", element.getAmount(), -1);
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
    for (int i = 0; i < element.getAdjudication().size(); i++)
      composeClaimResponseDetailAdjudicationComponent(t, "ClaimResponse", "adjudication", element.getAdjudication().get(i), i);
    for (int i = 0; i < element.getSubDetail().size(); i++)
      composeClaimResponseSubDetailComponent(t, "ClaimResponse", "subDetail", element.getSubDetail().get(i), i);
  }

  protected void composeClaimResponseDetailAdjudicationComponent(Complex parent, String parentType, String name, ClaimResponse.DetailAdjudicationComponent element, int index) {
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
      composeCoding(t, "ClaimResponse", "category", element.getCategory(), -1);
    if (element.hasReason())
      composeCoding(t, "ClaimResponse", "reason", element.getReason(), -1);
    if (element.hasAmount())
      composeQuantity(t, "ClaimResponse", "amount", element.getAmount(), -1);
    if (element.hasValueElement())
      composeDecimal(t, "ClaimResponse", "value", element.getValueElement(), -1);
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
    for (int i = 0; i < element.getAdjudication().size(); i++)
      composeClaimResponseSubdetailAdjudicationComponent(t, "ClaimResponse", "adjudication", element.getAdjudication().get(i), i);
  }

  protected void composeClaimResponseSubdetailAdjudicationComponent(Complex parent, String parentType, String name, ClaimResponse.SubdetailAdjudicationComponent element, int index) {
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
      composeCoding(t, "ClaimResponse", "category", element.getCategory(), -1);
    if (element.hasReason())
      composeCoding(t, "ClaimResponse", "reason", element.getReason(), -1);
    if (element.hasAmount())
      composeQuantity(t, "ClaimResponse", "amount", element.getAmount(), -1);
    if (element.hasValueElement())
      composeDecimal(t, "ClaimResponse", "value", element.getValueElement(), -1);
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
    if (element.hasService())
      composeCoding(t, "ClaimResponse", "service", element.getService(), -1);
    if (element.hasFee())
      composeQuantity(t, "ClaimResponse", "fee", element.getFee(), -1);
    for (int i = 0; i < element.getNoteNumberLinkId().size(); i++)
      composePositiveInt(t, "ClaimResponse", "noteNumberLinkId", element.getNoteNumberLinkId().get(i), i);
    for (int i = 0; i < element.getAdjudication().size(); i++)
      composeClaimResponseAddedItemAdjudicationComponent(t, "ClaimResponse", "adjudication", element.getAdjudication().get(i), i);
    for (int i = 0; i < element.getDetail().size(); i++)
      composeClaimResponseAddedItemsDetailComponent(t, "ClaimResponse", "detail", element.getDetail().get(i), i);
  }

  protected void composeClaimResponseAddedItemAdjudicationComponent(Complex parent, String parentType, String name, ClaimResponse.AddedItemAdjudicationComponent element, int index) {
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
      composeCoding(t, "ClaimResponse", "category", element.getCategory(), -1);
    if (element.hasReason())
      composeCoding(t, "ClaimResponse", "reason", element.getReason(), -1);
    if (element.hasAmount())
      composeQuantity(t, "ClaimResponse", "amount", element.getAmount(), -1);
    if (element.hasValueElement())
      composeDecimal(t, "ClaimResponse", "value", element.getValueElement(), -1);
  }

  protected void composeClaimResponseAddedItemsDetailComponent(Complex parent, String parentType, String name, ClaimResponse.AddedItemsDetailComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "detail", name, element, index);
    if (element.hasService())
      composeCoding(t, "ClaimResponse", "service", element.getService(), -1);
    if (element.hasFee())
      composeQuantity(t, "ClaimResponse", "fee", element.getFee(), -1);
    for (int i = 0; i < element.getAdjudication().size(); i++)
      composeClaimResponseAddedItemDetailAdjudicationComponent(t, "ClaimResponse", "adjudication", element.getAdjudication().get(i), i);
  }

  protected void composeClaimResponseAddedItemDetailAdjudicationComponent(Complex parent, String parentType, String name, ClaimResponse.AddedItemDetailAdjudicationComponent element, int index) {
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
      composeCoding(t, "ClaimResponse", "category", element.getCategory(), -1);
    if (element.hasReason())
      composeCoding(t, "ClaimResponse", "reason", element.getReason(), -1);
    if (element.hasAmount())
      composeQuantity(t, "ClaimResponse", "amount", element.getAmount(), -1);
    if (element.hasValueElement())
      composeDecimal(t, "ClaimResponse", "value", element.getValueElement(), -1);
  }

  protected void composeClaimResponseErrorsComponent(Complex parent, String parentType, String name, ClaimResponse.ErrorsComponent element, int index) {
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
      composeCoding(t, "ClaimResponse", "code", element.getCode(), -1);
  }

  protected void composeClaimResponseNotesComponent(Complex parent, String parentType, String name, ClaimResponse.NotesComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "note", name, element, index);
    if (element.hasNumberElement())
      composePositiveInt(t, "ClaimResponse", "number", element.getNumberElement(), -1);
    if (element.hasType())
      composeCoding(t, "ClaimResponse", "type", element.getType(), -1);
    if (element.hasTextElement())
      composeString(t, "ClaimResponse", "text", element.getTextElement(), -1);
  }

  protected void composeClaimResponseCoverageComponent(Complex parent, String parentType, String name, ClaimResponse.CoverageComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "coverage", name, element, index);
    if (element.hasSequenceElement())
      composePositiveInt(t, "ClaimResponse", "sequence", element.getSequenceElement(), -1);
    if (element.hasFocalElement())
      composeBoolean(t, "ClaimResponse", "focal", element.getFocalElement(), -1);
    if (element.hasCoverage())
      composeType(t, "ClaimResponse", "coverage", element.getCoverage(), -1);
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
    if (element.hasPatient())
      composeReference(t, "ClinicalImpression", "patient", element.getPatient(), -1);
    if (element.hasAssessor())
      composeReference(t, "ClinicalImpression", "assessor", element.getAssessor(), -1);
    if (element.hasStatusElement())
      composeEnum(t, "ClinicalImpression", "status", element.getStatusElement(), -1);
    if (element.hasDateElement())
      composeDateTime(t, "ClinicalImpression", "date", element.getDateElement(), -1);
    if (element.hasDescriptionElement())
      composeString(t, "ClinicalImpression", "description", element.getDescriptionElement(), -1);
    if (element.hasPrevious())
      composeReference(t, "ClinicalImpression", "previous", element.getPrevious(), -1);
    for (int i = 0; i < element.getProblem().size(); i++)
      composeReference(t, "ClinicalImpression", "problem", element.getProblem().get(i), i);
    if (element.hasTrigger())
      composeType(t, "ClinicalImpression", "trigger", element.getTrigger(), -1);
    for (int i = 0; i < element.getInvestigations().size(); i++)
      composeClinicalImpressionClinicalImpressionInvestigationsComponent(t, "ClinicalImpression", "investigations", element.getInvestigations().get(i), i);
    if (element.hasProtocolElement())
      composeUri(t, "ClinicalImpression", "protocol", element.getProtocolElement(), -1);
    if (element.hasSummaryElement())
      composeString(t, "ClinicalImpression", "summary", element.getSummaryElement(), -1);
    for (int i = 0; i < element.getFinding().size(); i++)
      composeClinicalImpressionClinicalImpressionFindingComponent(t, "ClinicalImpression", "finding", element.getFinding().get(i), i);
    for (int i = 0; i < element.getResolved().size(); i++)
      composeCodeableConcept(t, "ClinicalImpression", "resolved", element.getResolved().get(i), i);
    for (int i = 0; i < element.getRuledOut().size(); i++)
      composeClinicalImpressionClinicalImpressionRuledOutComponent(t, "ClinicalImpression", "ruledOut", element.getRuledOut().get(i), i);
    if (element.hasPrognosisElement())
      composeString(t, "ClinicalImpression", "prognosis", element.getPrognosisElement(), -1);
    for (int i = 0; i < element.getPlan().size(); i++)
      composeReference(t, "ClinicalImpression", "plan", element.getPlan().get(i), i);
    for (int i = 0; i < element.getAction().size(); i++)
      composeReference(t, "ClinicalImpression", "action", element.getAction().get(i), i);
  }

  protected void composeClinicalImpressionClinicalImpressionInvestigationsComponent(Complex parent, String parentType, String name, ClinicalImpression.ClinicalImpressionInvestigationsComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "investigations", name, element, index);
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
      composeCodeableConcept(t, "ClinicalImpression", "item", element.getItem(), -1);
    if (element.hasCauseElement())
      composeString(t, "ClinicalImpression", "cause", element.getCauseElement(), -1);
  }

  protected void composeClinicalImpressionClinicalImpressionRuledOutComponent(Complex parent, String parentType, String name, ClinicalImpression.ClinicalImpressionRuledOutComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "ruledOut", name, element, index);
    if (element.hasItem())
      composeCodeableConcept(t, "ClinicalImpression", "item", element.getItem(), -1);
    if (element.hasReasonElement())
      composeString(t, "ClinicalImpression", "reason", element.getReasonElement(), -1);
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
    if (element.hasStatusElement())
      composeEnum(t, "CodeSystem", "status", element.getStatusElement(), -1);
    if (element.hasExperimentalElement())
      composeBoolean(t, "CodeSystem", "experimental", element.getExperimentalElement(), -1);
    if (element.hasPublisherElement())
      composeString(t, "CodeSystem", "publisher", element.getPublisherElement(), -1);
    for (int i = 0; i < element.getContact().size(); i++)
      composeCodeSystemCodeSystemContactComponent(t, "CodeSystem", "contact", element.getContact().get(i), i);
    if (element.hasDateElement())
      composeDateTime(t, "CodeSystem", "date", element.getDateElement(), -1);
    if (element.hasDescriptionElement())
      composeString(t, "CodeSystem", "description", element.getDescriptionElement(), -1);
    for (int i = 0; i < element.getUseContext().size(); i++)
      composeCodeableConcept(t, "CodeSystem", "useContext", element.getUseContext().get(i), i);
    if (element.hasRequirementsElement())
      composeString(t, "CodeSystem", "requirements", element.getRequirementsElement(), -1);
    if (element.hasCopyrightElement())
      composeString(t, "CodeSystem", "copyright", element.getCopyrightElement(), -1);
    if (element.hasCaseSensitiveElement())
      composeBoolean(t, "CodeSystem", "caseSensitive", element.getCaseSensitiveElement(), -1);
    if (element.hasValueSetElement())
      composeUri(t, "CodeSystem", "valueSet", element.getValueSetElement(), -1);
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
      composeCodeSystemCodeSystemPropertyComponent(t, "CodeSystem", "property", element.getProperty().get(i), i);
    for (int i = 0; i < element.getConcept().size(); i++)
      composeCodeSystemConceptDefinitionComponent(t, "CodeSystem", "concept", element.getConcept().get(i), i);
  }

  protected void composeCodeSystemCodeSystemContactComponent(Complex parent, String parentType, String name, CodeSystem.CodeSystemContactComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "contact", name, element, index);
    if (element.hasNameElement())
      composeString(t, "CodeSystem", "name", element.getNameElement(), -1);
    for (int i = 0; i < element.getTelecom().size(); i++)
      composeContactPoint(t, "CodeSystem", "telecom", element.getTelecom().get(i), i);
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
      composeCode(t, "CodeSystem", "operator", element.getOperator().get(i), i);
    if (element.hasValueElement())
      composeString(t, "CodeSystem", "value", element.getValueElement(), -1);
  }

  protected void composeCodeSystemCodeSystemPropertyComponent(Complex parent, String parentType, String name, CodeSystem.CodeSystemPropertyComponent element, int index) {
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
      composeCodeSystemConceptDefinitionPropertyComponent(t, "CodeSystem", "property", element.getProperty().get(i), i);
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

  protected void composeCodeSystemConceptDefinitionPropertyComponent(Complex parent, String parentType, String name, CodeSystem.ConceptDefinitionPropertyComponent element, int index) {
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
    if (element.hasCategory())
      composeCodeableConcept(t, "Communication", "category", element.getCategory(), -1);
    if (element.hasSender())
      composeReference(t, "Communication", "sender", element.getSender(), -1);
    for (int i = 0; i < element.getRecipient().size(); i++)
      composeReference(t, "Communication", "recipient", element.getRecipient().get(i), i);
    for (int i = 0; i < element.getPayload().size(); i++)
      composeCommunicationCommunicationPayloadComponent(t, "Communication", "payload", element.getPayload().get(i), i);
    for (int i = 0; i < element.getMedium().size(); i++)
      composeCodeableConcept(t, "Communication", "medium", element.getMedium().get(i), i);
    if (element.hasStatusElement())
      composeEnum(t, "Communication", "status", element.getStatusElement(), -1);
    if (element.hasEncounter())
      composeReference(t, "Communication", "encounter", element.getEncounter(), -1);
    if (element.hasSentElement())
      composeDateTime(t, "Communication", "sent", element.getSentElement(), -1);
    if (element.hasReceivedElement())
      composeDateTime(t, "Communication", "received", element.getReceivedElement(), -1);
    for (int i = 0; i < element.getReason().size(); i++)
      composeCodeableConcept(t, "Communication", "reason", element.getReason().get(i), i);
    if (element.hasSubject())
      composeReference(t, "Communication", "subject", element.getSubject(), -1);
    if (element.hasRequestDetail())
      composeReference(t, "Communication", "requestDetail", element.getRequestDetail(), -1);
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
    if (element.hasCategory())
      composeCodeableConcept(t, "CommunicationRequest", "category", element.getCategory(), -1);
    if (element.hasSender())
      composeReference(t, "CommunicationRequest", "sender", element.getSender(), -1);
    for (int i = 0; i < element.getRecipient().size(); i++)
      composeReference(t, "CommunicationRequest", "recipient", element.getRecipient().get(i), i);
    for (int i = 0; i < element.getPayload().size(); i++)
      composeCommunicationRequestCommunicationRequestPayloadComponent(t, "CommunicationRequest", "payload", element.getPayload().get(i), i);
    for (int i = 0; i < element.getMedium().size(); i++)
      composeCodeableConcept(t, "CommunicationRequest", "medium", element.getMedium().get(i), i);
    if (element.hasRequester())
      composeReference(t, "CommunicationRequest", "requester", element.getRequester(), -1);
    if (element.hasStatusElement())
      composeEnum(t, "CommunicationRequest", "status", element.getStatusElement(), -1);
    if (element.hasEncounter())
      composeReference(t, "CommunicationRequest", "encounter", element.getEncounter(), -1);
    if (element.hasScheduled())
      composeType(t, "CommunicationRequest", "scheduled", element.getScheduled(), -1);
    for (int i = 0; i < element.getReason().size(); i++)
      composeCodeableConcept(t, "CommunicationRequest", "reason", element.getReason().get(i), i);
    if (element.hasRequestedOnElement())
      composeDateTime(t, "CommunicationRequest", "requestedOn", element.getRequestedOnElement(), -1);
    if (element.hasSubject())
      composeReference(t, "CommunicationRequest", "subject", element.getSubject(), -1);
    if (element.hasPriority())
      composeCodeableConcept(t, "CommunicationRequest", "priority", element.getPriority(), -1);
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
    if (element.hasStatusElement())
      composeEnum(t, "CompartmentDefinition", "status", element.getStatusElement(), -1);
    if (element.hasExperimentalElement())
      composeBoolean(t, "CompartmentDefinition", "experimental", element.getExperimentalElement(), -1);
    if (element.hasPublisherElement())
      composeString(t, "CompartmentDefinition", "publisher", element.getPublisherElement(), -1);
    for (int i = 0; i < element.getContact().size(); i++)
      composeCompartmentDefinitionCompartmentDefinitionContactComponent(t, "CompartmentDefinition", "contact", element.getContact().get(i), i);
    if (element.hasDateElement())
      composeDateTime(t, "CompartmentDefinition", "date", element.getDateElement(), -1);
    if (element.hasDescriptionElement())
      composeString(t, "CompartmentDefinition", "description", element.getDescriptionElement(), -1);
    if (element.hasRequirementsElement())
      composeString(t, "CompartmentDefinition", "requirements", element.getRequirementsElement(), -1);
    if (element.hasCodeElement())
      composeEnum(t, "CompartmentDefinition", "code", element.getCodeElement(), -1);
    if (element.hasSearchElement())
      composeBoolean(t, "CompartmentDefinition", "search", element.getSearchElement(), -1);
    for (int i = 0; i < element.getResource().size(); i++)
      composeCompartmentDefinitionCompartmentDefinitionResourceComponent(t, "CompartmentDefinition", "resource", element.getResource().get(i), i);
  }

  protected void composeCompartmentDefinitionCompartmentDefinitionContactComponent(Complex parent, String parentType, String name, CompartmentDefinition.CompartmentDefinitionContactComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "contact", name, element, index);
    if (element.hasNameElement())
      composeString(t, "CompartmentDefinition", "name", element.getNameElement(), -1);
    for (int i = 0; i < element.getTelecom().size(); i++)
      composeContactPoint(t, "CompartmentDefinition", "telecom", element.getTelecom().get(i), i);
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
    if (element.hasDateElement())
      composeDateTime(t, "Composition", "date", element.getDateElement(), -1);
    if (element.hasType())
      composeCodeableConcept(t, "Composition", "type", element.getType(), -1);
    if (element.hasClass_())
      composeCodeableConcept(t, "Composition", "class", element.getClass_(), -1);
    if (element.hasTitleElement())
      composeString(t, "Composition", "title", element.getTitleElement(), -1);
    if (element.hasStatusElement())
      composeEnum(t, "Composition", "status", element.getStatusElement(), -1);
    if (element.hasConfidentialityElement())
      composeCode(t, "Composition", "confidentiality", element.getConfidentialityElement(), -1);
    if (element.hasSubject())
      composeReference(t, "Composition", "subject", element.getSubject(), -1);
    for (int i = 0; i < element.getAuthor().size(); i++)
      composeReference(t, "Composition", "author", element.getAuthor().get(i), i);
    for (int i = 0; i < element.getAttester().size(); i++)
      composeCompositionCompositionAttesterComponent(t, "Composition", "attester", element.getAttester().get(i), i);
    if (element.hasCustodian())
      composeReference(t, "Composition", "custodian", element.getCustodian(), -1);
    for (int i = 0; i < element.getEvent().size(); i++)
      composeCompositionCompositionEventComponent(t, "Composition", "event", element.getEvent().get(i), i);
    if (element.hasEncounter())
      composeReference(t, "Composition", "encounter", element.getEncounter(), -1);
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
      composeCode(t, "Composition", "mode", element.getModeElement(), -1);
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
    if (element.hasStatusElement())
      composeEnum(t, "ConceptMap", "status", element.getStatusElement(), -1);
    if (element.hasExperimentalElement())
      composeBoolean(t, "ConceptMap", "experimental", element.getExperimentalElement(), -1);
    if (element.hasPublisherElement())
      composeString(t, "ConceptMap", "publisher", element.getPublisherElement(), -1);
    for (int i = 0; i < element.getContact().size(); i++)
      composeConceptMapConceptMapContactComponent(t, "ConceptMap", "contact", element.getContact().get(i), i);
    if (element.hasDateElement())
      composeDateTime(t, "ConceptMap", "date", element.getDateElement(), -1);
    if (element.hasDescriptionElement())
      composeString(t, "ConceptMap", "description", element.getDescriptionElement(), -1);
    for (int i = 0; i < element.getUseContext().size(); i++)
      composeCodeableConcept(t, "ConceptMap", "useContext", element.getUseContext().get(i), i);
    if (element.hasRequirementsElement())
      composeString(t, "ConceptMap", "requirements", element.getRequirementsElement(), -1);
    if (element.hasCopyrightElement())
      composeString(t, "ConceptMap", "copyright", element.getCopyrightElement(), -1);
    if (element.hasSource())
      composeType(t, "ConceptMap", "source", element.getSource(), -1);
    if (element.hasTarget())
      composeType(t, "ConceptMap", "target", element.getTarget(), -1);
    for (int i = 0; i < element.getElement().size(); i++)
      composeConceptMapSourceElementComponent(t, "ConceptMap", "element", element.getElement().get(i), i);
  }

  protected void composeConceptMapConceptMapContactComponent(Complex parent, String parentType, String name, ConceptMap.ConceptMapContactComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "contact", name, element, index);
    if (element.hasNameElement())
      composeString(t, "ConceptMap", "name", element.getNameElement(), -1);
    for (int i = 0; i < element.getTelecom().size(); i++)
      composeContactPoint(t, "ConceptMap", "telecom", element.getTelecom().get(i), i);
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
    if (element.hasSystemElement())
      composeUri(t, "ConceptMap", "system", element.getSystemElement(), -1);
    if (element.hasVersionElement())
      composeString(t, "ConceptMap", "version", element.getVersionElement(), -1);
    if (element.hasCodeElement())
      composeCode(t, "ConceptMap", "code", element.getCodeElement(), -1);
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
    if (element.hasSystemElement())
      composeUri(t, "ConceptMap", "system", element.getSystemElement(), -1);
    if (element.hasVersionElement())
      composeString(t, "ConceptMap", "version", element.getVersionElement(), -1);
    if (element.hasCodeElement())
      composeCode(t, "ConceptMap", "code", element.getCodeElement(), -1);
    if (element.hasEquivalenceElement())
      composeEnum(t, "ConceptMap", "equivalence", element.getEquivalenceElement(), -1);
    if (element.hasCommentsElement())
      composeString(t, "ConceptMap", "comments", element.getCommentsElement(), -1);
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
    if (element.hasElementElement())
      composeUri(t, "ConceptMap", "element", element.getElementElement(), -1);
    if (element.hasSystemElement())
      composeUri(t, "ConceptMap", "system", element.getSystemElement(), -1);
    if (element.hasCodeElement())
      composeString(t, "ConceptMap", "code", element.getCodeElement(), -1);
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
    if (element.hasPatient())
      composeReference(t, "Condition", "patient", element.getPatient(), -1);
    if (element.hasEncounter())
      composeReference(t, "Condition", "encounter", element.getEncounter(), -1);
    if (element.hasAsserter())
      composeReference(t, "Condition", "asserter", element.getAsserter(), -1);
    if (element.hasDateRecordedElement())
      composeDate(t, "Condition", "dateRecorded", element.getDateRecordedElement(), -1);
    if (element.hasCode())
      composeCodeableConcept(t, "Condition", "code", element.getCode(), -1);
    if (element.hasCategory())
      composeCodeableConcept(t, "Condition", "category", element.getCategory(), -1);
    if (element.hasClinicalStatusElement())
      composeCode(t, "Condition", "clinicalStatus", element.getClinicalStatusElement(), -1);
    if (element.hasVerificationStatusElement())
      composeEnum(t, "Condition", "verificationStatus", element.getVerificationStatusElement(), -1);
    if (element.hasSeverity())
      composeCodeableConcept(t, "Condition", "severity", element.getSeverity(), -1);
    if (element.hasOnset())
      composeType(t, "Condition", "onset", element.getOnset(), -1);
    if (element.hasAbatement())
      composeType(t, "Condition", "abatement", element.getAbatement(), -1);
    if (element.hasStage())
      composeConditionConditionStageComponent(t, "Condition", "stage", element.getStage(), -1);
    for (int i = 0; i < element.getEvidence().size(); i++)
      composeConditionConditionEvidenceComponent(t, "Condition", "evidence", element.getEvidence().get(i), i);
    for (int i = 0; i < element.getBodySite().size(); i++)
      composeCodeableConcept(t, "Condition", "bodySite", element.getBodySite().get(i), i);
    if (element.hasNotesElement())
      composeString(t, "Condition", "notes", element.getNotesElement(), -1);
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
    if (element.hasCode())
      composeCodeableConcept(t, "Condition", "code", element.getCode(), -1);
    for (int i = 0; i < element.getDetail().size(); i++)
      composeReference(t, "Condition", "detail", element.getDetail().get(i), i);
  }

  protected void composeConformance(Complex parent, String parentType, String name, Conformance element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "Conformance", name, element, index);
    if (element.hasUrlElement())
      composeUri(t, "Conformance", "url", element.getUrlElement(), -1);
    if (element.hasVersionElement())
      composeString(t, "Conformance", "version", element.getVersionElement(), -1);
    if (element.hasNameElement())
      composeString(t, "Conformance", "name", element.getNameElement(), -1);
    if (element.hasStatusElement())
      composeEnum(t, "Conformance", "status", element.getStatusElement(), -1);
    if (element.hasExperimentalElement())
      composeBoolean(t, "Conformance", "experimental", element.getExperimentalElement(), -1);
    if (element.hasDateElement())
      composeDateTime(t, "Conformance", "date", element.getDateElement(), -1);
    if (element.hasPublisherElement())
      composeString(t, "Conformance", "publisher", element.getPublisherElement(), -1);
    for (int i = 0; i < element.getContact().size(); i++)
      composeConformanceConformanceContactComponent(t, "Conformance", "contact", element.getContact().get(i), i);
    if (element.hasDescriptionElement())
      composeString(t, "Conformance", "description", element.getDescriptionElement(), -1);
    for (int i = 0; i < element.getUseContext().size(); i++)
      composeCodeableConcept(t, "Conformance", "useContext", element.getUseContext().get(i), i);
    if (element.hasRequirementsElement())
      composeString(t, "Conformance", "requirements", element.getRequirementsElement(), -1);
    if (element.hasCopyrightElement())
      composeString(t, "Conformance", "copyright", element.getCopyrightElement(), -1);
    if (element.hasKindElement())
      composeEnum(t, "Conformance", "kind", element.getKindElement(), -1);
    if (element.hasSoftware())
      composeConformanceConformanceSoftwareComponent(t, "Conformance", "software", element.getSoftware(), -1);
    if (element.hasImplementation())
      composeConformanceConformanceImplementationComponent(t, "Conformance", "implementation", element.getImplementation(), -1);
    if (element.hasFhirVersionElement())
      composeId(t, "Conformance", "fhirVersion", element.getFhirVersionElement(), -1);
    if (element.hasAcceptUnknownElement())
      composeEnum(t, "Conformance", "acceptUnknown", element.getAcceptUnknownElement(), -1);
    for (int i = 0; i < element.getFormat().size(); i++)
      composeCode(t, "Conformance", "format", element.getFormat().get(i), i);
    for (int i = 0; i < element.getProfile().size(); i++)
      composeReference(t, "Conformance", "profile", element.getProfile().get(i), i);
    for (int i = 0; i < element.getRest().size(); i++)
      composeConformanceConformanceRestComponent(t, "Conformance", "rest", element.getRest().get(i), i);
    for (int i = 0; i < element.getMessaging().size(); i++)
      composeConformanceConformanceMessagingComponent(t, "Conformance", "messaging", element.getMessaging().get(i), i);
    for (int i = 0; i < element.getDocument().size(); i++)
      composeConformanceConformanceDocumentComponent(t, "Conformance", "document", element.getDocument().get(i), i);
  }

  protected void composeConformanceConformanceContactComponent(Complex parent, String parentType, String name, Conformance.ConformanceContactComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "contact", name, element, index);
    if (element.hasNameElement())
      composeString(t, "Conformance", "name", element.getNameElement(), -1);
    for (int i = 0; i < element.getTelecom().size(); i++)
      composeContactPoint(t, "Conformance", "telecom", element.getTelecom().get(i), i);
  }

  protected void composeConformanceConformanceSoftwareComponent(Complex parent, String parentType, String name, Conformance.ConformanceSoftwareComponent element, int index) {
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
      composeString(t, "Conformance", "name", element.getNameElement(), -1);
    if (element.hasVersionElement())
      composeString(t, "Conformance", "version", element.getVersionElement(), -1);
    if (element.hasReleaseDateElement())
      composeDateTime(t, "Conformance", "releaseDate", element.getReleaseDateElement(), -1);
  }

  protected void composeConformanceConformanceImplementationComponent(Complex parent, String parentType, String name, Conformance.ConformanceImplementationComponent element, int index) {
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
      composeString(t, "Conformance", "description", element.getDescriptionElement(), -1);
    if (element.hasUrlElement())
      composeUri(t, "Conformance", "url", element.getUrlElement(), -1);
  }

  protected void composeConformanceConformanceRestComponent(Complex parent, String parentType, String name, Conformance.ConformanceRestComponent element, int index) {
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
      composeEnum(t, "Conformance", "mode", element.getModeElement(), -1);
    if (element.hasDocumentationElement())
      composeString(t, "Conformance", "documentation", element.getDocumentationElement(), -1);
    if (element.hasSecurity())
      composeConformanceConformanceRestSecurityComponent(t, "Conformance", "security", element.getSecurity(), -1);
    for (int i = 0; i < element.getResource().size(); i++)
      composeConformanceConformanceRestResourceComponent(t, "Conformance", "resource", element.getResource().get(i), i);
    for (int i = 0; i < element.getInteraction().size(); i++)
      composeConformanceSystemInteractionComponent(t, "Conformance", "interaction", element.getInteraction().get(i), i);
    if (element.hasTransactionModeElement())
      composeEnum(t, "Conformance", "transactionMode", element.getTransactionModeElement(), -1);
    for (int i = 0; i < element.getSearchParam().size(); i++)
      composeConformanceConformanceRestResourceSearchParamComponent(t, "Conformance", "searchParam", element.getSearchParam().get(i), i);
    for (int i = 0; i < element.getOperation().size(); i++)
      composeConformanceConformanceRestOperationComponent(t, "Conformance", "operation", element.getOperation().get(i), i);
    for (int i = 0; i < element.getCompartment().size(); i++)
      composeUri(t, "Conformance", "compartment", element.getCompartment().get(i), i);
  }

  protected void composeConformanceConformanceRestSecurityComponent(Complex parent, String parentType, String name, Conformance.ConformanceRestSecurityComponent element, int index) {
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
      composeBoolean(t, "Conformance", "cors", element.getCorsElement(), -1);
    for (int i = 0; i < element.getService().size(); i++)
      composeCodeableConcept(t, "Conformance", "service", element.getService().get(i), i);
    if (element.hasDescriptionElement())
      composeString(t, "Conformance", "description", element.getDescriptionElement(), -1);
    for (int i = 0; i < element.getCertificate().size(); i++)
      composeConformanceConformanceRestSecurityCertificateComponent(t, "Conformance", "certificate", element.getCertificate().get(i), i);
  }

  protected void composeConformanceConformanceRestSecurityCertificateComponent(Complex parent, String parentType, String name, Conformance.ConformanceRestSecurityCertificateComponent element, int index) {
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
      composeCode(t, "Conformance", "type", element.getTypeElement(), -1);
    if (element.hasBlobElement())
      composeBase64Binary(t, "Conformance", "blob", element.getBlobElement(), -1);
  }

  protected void composeConformanceConformanceRestResourceComponent(Complex parent, String parentType, String name, Conformance.ConformanceRestResourceComponent element, int index) {
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
      composeCode(t, "Conformance", "type", element.getTypeElement(), -1);
    if (element.hasProfile())
      composeReference(t, "Conformance", "profile", element.getProfile(), -1);
    for (int i = 0; i < element.getInteraction().size(); i++)
      composeConformanceResourceInteractionComponent(t, "Conformance", "interaction", element.getInteraction().get(i), i);
    if (element.hasVersioningElement())
      composeEnum(t, "Conformance", "versioning", element.getVersioningElement(), -1);
    if (element.hasReadHistoryElement())
      composeBoolean(t, "Conformance", "readHistory", element.getReadHistoryElement(), -1);
    if (element.hasUpdateCreateElement())
      composeBoolean(t, "Conformance", "updateCreate", element.getUpdateCreateElement(), -1);
    if (element.hasConditionalCreateElement())
      composeBoolean(t, "Conformance", "conditionalCreate", element.getConditionalCreateElement(), -1);
    if (element.hasConditionalUpdateElement())
      composeBoolean(t, "Conformance", "conditionalUpdate", element.getConditionalUpdateElement(), -1);
    if (element.hasConditionalDeleteElement())
      composeEnum(t, "Conformance", "conditionalDelete", element.getConditionalDeleteElement(), -1);
    for (int i = 0; i < element.getSearchInclude().size(); i++)
      composeString(t, "Conformance", "searchInclude", element.getSearchInclude().get(i), i);
    for (int i = 0; i < element.getSearchRevInclude().size(); i++)
      composeString(t, "Conformance", "searchRevInclude", element.getSearchRevInclude().get(i), i);
    for (int i = 0; i < element.getSearchParam().size(); i++)
      composeConformanceConformanceRestResourceSearchParamComponent(t, "Conformance", "searchParam", element.getSearchParam().get(i), i);
  }

  protected void composeConformanceResourceInteractionComponent(Complex parent, String parentType, String name, Conformance.ResourceInteractionComponent element, int index) {
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
      composeEnum(t, "Conformance", "code", element.getCodeElement(), -1);
    if (element.hasDocumentationElement())
      composeString(t, "Conformance", "documentation", element.getDocumentationElement(), -1);
  }

  protected void composeConformanceConformanceRestResourceSearchParamComponent(Complex parent, String parentType, String name, Conformance.ConformanceRestResourceSearchParamComponent element, int index) {
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
      composeString(t, "Conformance", "name", element.getNameElement(), -1);
    if (element.hasDefinitionElement())
      composeUri(t, "Conformance", "definition", element.getDefinitionElement(), -1);
    if (element.hasTypeElement())
      composeEnum(t, "Conformance", "type", element.getTypeElement(), -1);
    if (element.hasDocumentationElement())
      composeString(t, "Conformance", "documentation", element.getDocumentationElement(), -1);
    for (int i = 0; i < element.getTarget().size(); i++)
      composeCode(t, "Conformance", "target", element.getTarget().get(i), i);
    for (int i = 0; i < element.getModifier().size(); i++)
      composeEnum(t, "Conformance", "modifier", element.getModifier().get(i), i);
    for (int i = 0; i < element.getChain().size(); i++)
      composeString(t, "Conformance", "chain", element.getChain().get(i), i);
  }

  protected void composeConformanceSystemInteractionComponent(Complex parent, String parentType, String name, Conformance.SystemInteractionComponent element, int index) {
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
      composeEnum(t, "Conformance", "code", element.getCodeElement(), -1);
    if (element.hasDocumentationElement())
      composeString(t, "Conformance", "documentation", element.getDocumentationElement(), -1);
  }

  protected void composeConformanceConformanceRestOperationComponent(Complex parent, String parentType, String name, Conformance.ConformanceRestOperationComponent element, int index) {
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
      composeString(t, "Conformance", "name", element.getNameElement(), -1);
    if (element.hasDefinition())
      composeReference(t, "Conformance", "definition", element.getDefinition(), -1);
  }

  protected void composeConformanceConformanceMessagingComponent(Complex parent, String parentType, String name, Conformance.ConformanceMessagingComponent element, int index) {
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
      composeConformanceConformanceMessagingEndpointComponent(t, "Conformance", "endpoint", element.getEndpoint().get(i), i);
    if (element.hasReliableCacheElement())
      composeUnsignedInt(t, "Conformance", "reliableCache", element.getReliableCacheElement(), -1);
    if (element.hasDocumentationElement())
      composeString(t, "Conformance", "documentation", element.getDocumentationElement(), -1);
    for (int i = 0; i < element.getEvent().size(); i++)
      composeConformanceConformanceMessagingEventComponent(t, "Conformance", "event", element.getEvent().get(i), i);
  }

  protected void composeConformanceConformanceMessagingEndpointComponent(Complex parent, String parentType, String name, Conformance.ConformanceMessagingEndpointComponent element, int index) {
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
      composeCoding(t, "Conformance", "protocol", element.getProtocol(), -1);
    if (element.hasAddressElement())
      composeUri(t, "Conformance", "address", element.getAddressElement(), -1);
  }

  protected void composeConformanceConformanceMessagingEventComponent(Complex parent, String parentType, String name, Conformance.ConformanceMessagingEventComponent element, int index) {
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
      composeCoding(t, "Conformance", "code", element.getCode(), -1);
    if (element.hasCategoryElement())
      composeEnum(t, "Conformance", "category", element.getCategoryElement(), -1);
    if (element.hasModeElement())
      composeEnum(t, "Conformance", "mode", element.getModeElement(), -1);
    if (element.hasFocusElement())
      composeCode(t, "Conformance", "focus", element.getFocusElement(), -1);
    if (element.hasRequest())
      composeReference(t, "Conformance", "request", element.getRequest(), -1);
    if (element.hasResponse())
      composeReference(t, "Conformance", "response", element.getResponse(), -1);
    if (element.hasDocumentationElement())
      composeString(t, "Conformance", "documentation", element.getDocumentationElement(), -1);
  }

  protected void composeConformanceConformanceDocumentComponent(Complex parent, String parentType, String name, Conformance.ConformanceDocumentComponent element, int index) {
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
      composeEnum(t, "Conformance", "mode", element.getModeElement(), -1);
    if (element.hasDocumentationElement())
      composeString(t, "Conformance", "documentation", element.getDocumentationElement(), -1);
    if (element.hasProfile())
      composeReference(t, "Conformance", "profile", element.getProfile(), -1);
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
    if (element.hasIssuedElement())
      composeDateTime(t, "Contract", "issued", element.getIssuedElement(), -1);
    if (element.hasApplies())
      composePeriod(t, "Contract", "applies", element.getApplies(), -1);
    for (int i = 0; i < element.getSubject().size(); i++)
      composeReference(t, "Contract", "subject", element.getSubject().get(i), i);
    for (int i = 0; i < element.getTopic().size(); i++)
      composeReference(t, "Contract", "topic", element.getTopic().get(i), i);
    for (int i = 0; i < element.getAuthority().size(); i++)
      composeReference(t, "Contract", "authority", element.getAuthority().get(i), i);
    for (int i = 0; i < element.getDomain().size(); i++)
      composeReference(t, "Contract", "domain", element.getDomain().get(i), i);
    if (element.hasType())
      composeCodeableConcept(t, "Contract", "type", element.getType(), -1);
    for (int i = 0; i < element.getSubType().size(); i++)
      composeCodeableConcept(t, "Contract", "subType", element.getSubType().get(i), i);
    for (int i = 0; i < element.getAction().size(); i++)
      composeCodeableConcept(t, "Contract", "action", element.getAction().get(i), i);
    for (int i = 0; i < element.getActionReason().size(); i++)
      composeCodeableConcept(t, "Contract", "actionReason", element.getActionReason().get(i), i);
    for (int i = 0; i < element.getAgent().size(); i++)
      composeContractAgentComponent(t, "Contract", "agent", element.getAgent().get(i), i);
    for (int i = 0; i < element.getSigner().size(); i++)
      composeContractSignatoryComponent(t, "Contract", "signer", element.getSigner().get(i), i);
    for (int i = 0; i < element.getValuedItem().size(); i++)
      composeContractValuedItemComponent(t, "Contract", "valuedItem", element.getValuedItem().get(i), i);
    for (int i = 0; i < element.getTerm().size(); i++)
      composeContractTermComponent(t, "Contract", "term", element.getTerm().get(i), i);
    if (element.hasBinding())
      composeType(t, "Contract", "binding", element.getBinding(), -1);
    for (int i = 0; i < element.getFriendly().size(); i++)
      composeContractFriendlyLanguageComponent(t, "Contract", "friendly", element.getFriendly().get(i), i);
    for (int i = 0; i < element.getLegal().size(); i++)
      composeContractLegalLanguageComponent(t, "Contract", "legal", element.getLegal().get(i), i);
    for (int i = 0; i < element.getRule().size(); i++)
      composeContractComputableLanguageComponent(t, "Contract", "rule", element.getRule().get(i), i);
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
      composeQuantity(t, "Contract", "unitPrice", element.getUnitPrice(), -1);
    if (element.hasFactorElement())
      composeDecimal(t, "Contract", "factor", element.getFactorElement(), -1);
    if (element.hasPointsElement())
      composeDecimal(t, "Contract", "points", element.getPointsElement(), -1);
    if (element.hasNet())
      composeQuantity(t, "Contract", "net", element.getNet(), -1);
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
    for (int i = 0; i < element.getTopic().size(); i++)
      composeReference(t, "Contract", "topic", element.getTopic().get(i), i);
    for (int i = 0; i < element.getAction().size(); i++)
      composeCodeableConcept(t, "Contract", "action", element.getAction().get(i), i);
    for (int i = 0; i < element.getActionReason().size(); i++)
      composeCodeableConcept(t, "Contract", "actionReason", element.getActionReason().get(i), i);
    for (int i = 0; i < element.getAgent().size(); i++)
      composeContractTermAgentComponent(t, "Contract", "agent", element.getAgent().get(i), i);
    if (element.hasTextElement())
      composeString(t, "Contract", "text", element.getTextElement(), -1);
    for (int i = 0; i < element.getValuedItem().size(); i++)
      composeContractTermValuedItemComponent(t, "Contract", "valuedItem", element.getValuedItem().get(i), i);
    for (int i = 0; i < element.getGroup().size(); i++)
      composeContractTermComponent(t, "Contract", "group", element.getGroup().get(i), i);
  }

  protected void composeContractTermAgentComponent(Complex parent, String parentType, String name, Contract.TermAgentComponent element, int index) {
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

  protected void composeContractTermValuedItemComponent(Complex parent, String parentType, String name, Contract.TermValuedItemComponent element, int index) {
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
      composeQuantity(t, "Contract", "unitPrice", element.getUnitPrice(), -1);
    if (element.hasFactorElement())
      composeDecimal(t, "Contract", "factor", element.getFactorElement(), -1);
    if (element.hasPointsElement())
      composeDecimal(t, "Contract", "points", element.getPointsElement(), -1);
    if (element.hasNet())
      composeQuantity(t, "Contract", "net", element.getNet(), -1);
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
    if (element.hasIssuer())
      composeType(t, "Coverage", "issuer", element.getIssuer(), -1);
    if (element.hasBinElement())
      composeString(t, "Coverage", "bin", element.getBinElement(), -1);
    if (element.hasPeriod())
      composePeriod(t, "Coverage", "period", element.getPeriod(), -1);
    if (element.hasType())
      composeCoding(t, "Coverage", "type", element.getType(), -1);
    if (element.hasPlanholder())
      composeType(t, "Coverage", "planholder", element.getPlanholder(), -1);
    if (element.hasBeneficiary())
      composeType(t, "Coverage", "beneficiary", element.getBeneficiary(), -1);
    if (element.hasRelationship())
      composeCoding(t, "Coverage", "relationship", element.getRelationship(), -1);
    for (int i = 0; i < element.getIdentifier().size(); i++)
      composeIdentifier(t, "Coverage", "identifier", element.getIdentifier().get(i), i);
    if (element.hasGroupElement())
      composeString(t, "Coverage", "group", element.getGroupElement(), -1);
    if (element.hasPlanElement())
      composeString(t, "Coverage", "plan", element.getPlanElement(), -1);
    if (element.hasSubPlanElement())
      composeString(t, "Coverage", "subPlan", element.getSubPlanElement(), -1);
    if (element.hasDependentElement())
      composePositiveInt(t, "Coverage", "dependent", element.getDependentElement(), -1);
    if (element.hasSequenceElement())
      composePositiveInt(t, "Coverage", "sequence", element.getSequenceElement(), -1);
    for (int i = 0; i < element.getException().size(); i++)
      composeCoding(t, "Coverage", "exception", element.getException().get(i), i);
    if (element.hasSchoolElement())
      composeString(t, "Coverage", "school", element.getSchoolElement(), -1);
    if (element.hasNetworkElement())
      composeString(t, "Coverage", "network", element.getNetworkElement(), -1);
    for (int i = 0; i < element.getContract().size(); i++)
      composeReference(t, "Coverage", "contract", element.getContract().get(i), i);
  }

  protected void composeDataElement(Complex parent, String parentType, String name, DataElement element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "DataElement", name, element, index);
    if (element.hasUrlElement())
      composeUri(t, "DataElement", "url", element.getUrlElement(), -1);
    for (int i = 0; i < element.getIdentifier().size(); i++)
      composeIdentifier(t, "DataElement", "identifier", element.getIdentifier().get(i), i);
    if (element.hasVersionElement())
      composeString(t, "DataElement", "version", element.getVersionElement(), -1);
    if (element.hasStatusElement())
      composeEnum(t, "DataElement", "status", element.getStatusElement(), -1);
    if (element.hasExperimentalElement())
      composeBoolean(t, "DataElement", "experimental", element.getExperimentalElement(), -1);
    if (element.hasPublisherElement())
      composeString(t, "DataElement", "publisher", element.getPublisherElement(), -1);
    if (element.hasDateElement())
      composeDateTime(t, "DataElement", "date", element.getDateElement(), -1);
    if (element.hasNameElement())
      composeString(t, "DataElement", "name", element.getNameElement(), -1);
    for (int i = 0; i < element.getContact().size(); i++)
      composeDataElementDataElementContactComponent(t, "DataElement", "contact", element.getContact().get(i), i);
    for (int i = 0; i < element.getUseContext().size(); i++)
      composeCodeableConcept(t, "DataElement", "useContext", element.getUseContext().get(i), i);
    if (element.hasCopyrightElement())
      composeString(t, "DataElement", "copyright", element.getCopyrightElement(), -1);
    if (element.hasStringencyElement())
      composeEnum(t, "DataElement", "stringency", element.getStringencyElement(), -1);
    for (int i = 0; i < element.getMapping().size(); i++)
      composeDataElementDataElementMappingComponent(t, "DataElement", "mapping", element.getMapping().get(i), i);
    for (int i = 0; i < element.getElement().size(); i++)
      composeElementDefinition(t, "DataElement", "element", element.getElement().get(i), i);
  }

  protected void composeDataElementDataElementContactComponent(Complex parent, String parentType, String name, DataElement.DataElementContactComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "contact", name, element, index);
    if (element.hasNameElement())
      composeString(t, "DataElement", "name", element.getNameElement(), -1);
    for (int i = 0; i < element.getTelecom().size(); i++)
      composeContactPoint(t, "DataElement", "telecom", element.getTelecom().get(i), i);
  }

  protected void composeDataElementDataElementMappingComponent(Complex parent, String parentType, String name, DataElement.DataElementMappingComponent element, int index) {
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
      composeId(t, "DataElement", "identity", element.getIdentityElement(), -1);
    if (element.hasUriElement())
      composeUri(t, "DataElement", "uri", element.getUriElement(), -1);
    if (element.hasNameElement())
      composeString(t, "DataElement", "name", element.getNameElement(), -1);
    if (element.hasCommentElement())
      composeString(t, "DataElement", "comment", element.getCommentElement(), -1);
  }

  protected void composeDecisionSupportRule(Complex parent, String parentType, String name, DecisionSupportRule element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "DecisionSupportRule", name, element, index);
    if (element.hasModuleMetadata())
      composeModuleMetadata(t, "DecisionSupportRule", "moduleMetadata", element.getModuleMetadata(), -1);
    for (int i = 0; i < element.getLibrary().size(); i++)
      composeReference(t, "DecisionSupportRule", "library", element.getLibrary().get(i), i);
    for (int i = 0; i < element.getTrigger().size(); i++)
      composeTriggerDefinition(t, "DecisionSupportRule", "trigger", element.getTrigger().get(i), i);
    if (element.hasConditionElement())
      composeString(t, "DecisionSupportRule", "condition", element.getConditionElement(), -1);
    for (int i = 0; i < element.getAction().size(); i++)
      composeActionDefinition(t, "DecisionSupportRule", "action", element.getAction().get(i), i);
  }

  protected void composeDecisionSupportServiceModule(Complex parent, String parentType, String name, DecisionSupportServiceModule element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "DecisionSupportServiceModule", name, element, index);
    if (element.hasModuleMetadata())
      composeModuleMetadata(t, "DecisionSupportServiceModule", "moduleMetadata", element.getModuleMetadata(), -1);
    for (int i = 0; i < element.getTrigger().size(); i++)
      composeTriggerDefinition(t, "DecisionSupportServiceModule", "trigger", element.getTrigger().get(i), i);
    for (int i = 0; i < element.getParameter().size(); i++)
      composeParameterDefinition(t, "DecisionSupportServiceModule", "parameter", element.getParameter().get(i), i);
    for (int i = 0; i < element.getDataRequirement().size(); i++)
      composeDataRequirement(t, "DecisionSupportServiceModule", "dataRequirement", element.getDataRequirement().get(i), i);
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
    if (element.hasPatient())
      composeReference(t, "DetectedIssue", "patient", element.getPatient(), -1);
    if (element.hasCategory())
      composeCodeableConcept(t, "DetectedIssue", "category", element.getCategory(), -1);
    if (element.hasSeverityElement())
      composeEnum(t, "DetectedIssue", "severity", element.getSeverityElement(), -1);
    for (int i = 0; i < element.getImplicated().size(); i++)
      composeReference(t, "DetectedIssue", "implicated", element.getImplicated().get(i), i);
    if (element.hasDetailElement())
      composeString(t, "DetectedIssue", "detail", element.getDetailElement(), -1);
    if (element.hasDateElement())
      composeDateTime(t, "DetectedIssue", "date", element.getDateElement(), -1);
    if (element.hasAuthor())
      composeReference(t, "DetectedIssue", "author", element.getAuthor(), -1);
    if (element.hasIdentifier())
      composeIdentifier(t, "DetectedIssue", "identifier", element.getIdentifier(), -1);
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
    if (element.hasUdiCarrier())
      composeIdentifier(t, "Device", "udiCarrier", element.getUdiCarrier(), -1);
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
    if (element.hasType())
      composeCodeableConcept(t, "DeviceComponent", "type", element.getType(), -1);
    if (element.hasIdentifier())
      composeIdentifier(t, "DeviceComponent", "identifier", element.getIdentifier(), -1);
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
    if (element.hasType())
      composeCodeableConcept(t, "DeviceMetric", "type", element.getType(), -1);
    if (element.hasIdentifier())
      composeIdentifier(t, "DeviceMetric", "identifier", element.getIdentifier(), -1);
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

  protected void composeDeviceUseRequest(Complex parent, String parentType, String name, DeviceUseRequest element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "DeviceUseRequest", name, element, index);
    if (element.hasBodySite())
      composeType(t, "DeviceUseRequest", "bodySite", element.getBodySite(), -1);
    if (element.hasStatusElement())
      composeEnum(t, "DeviceUseRequest", "status", element.getStatusElement(), -1);
    if (element.hasDevice())
      composeReference(t, "DeviceUseRequest", "device", element.getDevice(), -1);
    if (element.hasEncounter())
      composeReference(t, "DeviceUseRequest", "encounter", element.getEncounter(), -1);
    for (int i = 0; i < element.getIdentifier().size(); i++)
      composeIdentifier(t, "DeviceUseRequest", "identifier", element.getIdentifier().get(i), i);
    for (int i = 0; i < element.getIndication().size(); i++)
      composeCodeableConcept(t, "DeviceUseRequest", "indication", element.getIndication().get(i), i);
    for (int i = 0; i < element.getNotes().size(); i++)
      composeString(t, "DeviceUseRequest", "notes", element.getNotes().get(i), i);
    for (int i = 0; i < element.getPrnReason().size(); i++)
      composeCodeableConcept(t, "DeviceUseRequest", "prnReason", element.getPrnReason().get(i), i);
    if (element.hasOrderedOnElement())
      composeDateTime(t, "DeviceUseRequest", "orderedOn", element.getOrderedOnElement(), -1);
    if (element.hasRecordedOnElement())
      composeDateTime(t, "DeviceUseRequest", "recordedOn", element.getRecordedOnElement(), -1);
    if (element.hasSubject())
      composeReference(t, "DeviceUseRequest", "subject", element.getSubject(), -1);
    if (element.hasTiming())
      composeType(t, "DeviceUseRequest", "timing", element.getTiming(), -1);
    if (element.hasPriorityElement())
      composeEnum(t, "DeviceUseRequest", "priority", element.getPriorityElement(), -1);
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
    if (element.hasBodySite())
      composeType(t, "DeviceUseStatement", "bodySite", element.getBodySite(), -1);
    if (element.hasWhenUsed())
      composePeriod(t, "DeviceUseStatement", "whenUsed", element.getWhenUsed(), -1);
    if (element.hasDevice())
      composeReference(t, "DeviceUseStatement", "device", element.getDevice(), -1);
    for (int i = 0; i < element.getIdentifier().size(); i++)
      composeIdentifier(t, "DeviceUseStatement", "identifier", element.getIdentifier().get(i), i);
    for (int i = 0; i < element.getIndication().size(); i++)
      composeCodeableConcept(t, "DeviceUseStatement", "indication", element.getIndication().get(i), i);
    for (int i = 0; i < element.getNotes().size(); i++)
      composeString(t, "DeviceUseStatement", "notes", element.getNotes().get(i), i);
    if (element.hasRecordedOnElement())
      composeDateTime(t, "DeviceUseStatement", "recordedOn", element.getRecordedOnElement(), -1);
    if (element.hasSubject())
      composeReference(t, "DeviceUseStatement", "subject", element.getSubject(), -1);
    if (element.hasTiming())
      composeType(t, "DeviceUseStatement", "timing", element.getTiming(), -1);
  }

  protected void composeDiagnosticOrder(Complex parent, String parentType, String name, DiagnosticOrder element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "DiagnosticOrder", name, element, index);
    for (int i = 0; i < element.getIdentifier().size(); i++)
      composeIdentifier(t, "DiagnosticOrder", "identifier", element.getIdentifier().get(i), i);
    if (element.hasStatusElement())
      composeEnum(t, "DiagnosticOrder", "status", element.getStatusElement(), -1);
    if (element.hasPriorityElement())
      composeEnum(t, "DiagnosticOrder", "priority", element.getPriorityElement(), -1);
    if (element.hasSubject())
      composeReference(t, "DiagnosticOrder", "subject", element.getSubject(), -1);
    if (element.hasEncounter())
      composeReference(t, "DiagnosticOrder", "encounter", element.getEncounter(), -1);
    if (element.hasOrderer())
      composeReference(t, "DiagnosticOrder", "orderer", element.getOrderer(), -1);
    for (int i = 0; i < element.getReason().size(); i++)
      composeCodeableConcept(t, "DiagnosticOrder", "reason", element.getReason().get(i), i);
    for (int i = 0; i < element.getSupportingInformation().size(); i++)
      composeReference(t, "DiagnosticOrder", "supportingInformation", element.getSupportingInformation().get(i), i);
    for (int i = 0; i < element.getSpecimen().size(); i++)
      composeReference(t, "DiagnosticOrder", "specimen", element.getSpecimen().get(i), i);
    for (int i = 0; i < element.getEvent().size(); i++)
      composeDiagnosticOrderDiagnosticOrderEventComponent(t, "DiagnosticOrder", "event", element.getEvent().get(i), i);
    for (int i = 0; i < element.getItem().size(); i++)
      composeDiagnosticOrderDiagnosticOrderItemComponent(t, "DiagnosticOrder", "item", element.getItem().get(i), i);
    for (int i = 0; i < element.getNote().size(); i++)
      composeAnnotation(t, "DiagnosticOrder", "note", element.getNote().get(i), i);
  }

  protected void composeDiagnosticOrderDiagnosticOrderEventComponent(Complex parent, String parentType, String name, DiagnosticOrder.DiagnosticOrderEventComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "event", name, element, index);
    if (element.hasStatusElement())
      composeEnum(t, "DiagnosticOrder", "status", element.getStatusElement(), -1);
    if (element.hasDescription())
      composeCodeableConcept(t, "DiagnosticOrder", "description", element.getDescription(), -1);
    if (element.hasDateTimeElement())
      composeDateTime(t, "DiagnosticOrder", "dateTime", element.getDateTimeElement(), -1);
    if (element.hasActor())
      composeReference(t, "DiagnosticOrder", "actor", element.getActor(), -1);
  }

  protected void composeDiagnosticOrderDiagnosticOrderItemComponent(Complex parent, String parentType, String name, DiagnosticOrder.DiagnosticOrderItemComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "item", name, element, index);
    if (element.hasCode())
      composeCodeableConcept(t, "DiagnosticOrder", "code", element.getCode(), -1);
    for (int i = 0; i < element.getSpecimen().size(); i++)
      composeReference(t, "DiagnosticOrder", "specimen", element.getSpecimen().get(i), i);
    if (element.hasBodySite())
      composeCodeableConcept(t, "DiagnosticOrder", "bodySite", element.getBodySite(), -1);
    if (element.hasStatusElement())
      composeEnum(t, "DiagnosticOrder", "status", element.getStatusElement(), -1);
    for (int i = 0; i < element.getEvent().size(); i++)
      composeDiagnosticOrderDiagnosticOrderEventComponent(t, "DiagnosticOrder", "event", element.getEvent().get(i), i);
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
    if (element.hasStatusElement())
      composeEnum(t, "DiagnosticReport", "status", element.getStatusElement(), -1);
    if (element.hasCategory())
      composeCodeableConcept(t, "DiagnosticReport", "category", element.getCategory(), -1);
    if (element.hasCode())
      composeCodeableConcept(t, "DiagnosticReport", "code", element.getCode(), -1);
    if (element.hasSubject())
      composeReference(t, "DiagnosticReport", "subject", element.getSubject(), -1);
    if (element.hasEncounter())
      composeReference(t, "DiagnosticReport", "encounter", element.getEncounter(), -1);
    if (element.hasEffective())
      composeType(t, "DiagnosticReport", "effective", element.getEffective(), -1);
    if (element.hasIssuedElement())
      composeInstant(t, "DiagnosticReport", "issued", element.getIssuedElement(), -1);
    if (element.hasPerformer())
      composeReference(t, "DiagnosticReport", "performer", element.getPerformer(), -1);
    for (int i = 0; i < element.getRequest().size(); i++)
      composeReference(t, "DiagnosticReport", "request", element.getRequest().get(i), i);
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
    if (element.hasSubject())
      composeReference(t, "DocumentManifest", "subject", element.getSubject(), -1);
    for (int i = 0; i < element.getRecipient().size(); i++)
      composeReference(t, "DocumentManifest", "recipient", element.getRecipient().get(i), i);
    if (element.hasType())
      composeCodeableConcept(t, "DocumentManifest", "type", element.getType(), -1);
    for (int i = 0; i < element.getAuthor().size(); i++)
      composeReference(t, "DocumentManifest", "author", element.getAuthor().get(i), i);
    if (element.hasCreatedElement())
      composeDateTime(t, "DocumentManifest", "created", element.getCreatedElement(), -1);
    if (element.hasSourceElement())
      composeUri(t, "DocumentManifest", "source", element.getSourceElement(), -1);
    if (element.hasStatusElement())
      composeEnum(t, "DocumentManifest", "status", element.getStatusElement(), -1);
    if (element.hasDescriptionElement())
      composeString(t, "DocumentManifest", "description", element.getDescriptionElement(), -1);
    for (int i = 0; i < element.getContent().size(); i++)
      composeDocumentManifestDocumentManifestContentComponent(t, "DocumentManifest", "content", element.getContent().get(i), i);
    for (int i = 0; i < element.getRelated().size(); i++)
      composeDocumentManifestDocumentManifestRelatedComponent(t, "DocumentManifest", "related", element.getRelated().get(i), i);
  }

  protected void composeDocumentManifestDocumentManifestContentComponent(Complex parent, String parentType, String name, DocumentManifest.DocumentManifestContentComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "content", name, element, index);
    if (element.hasP())
      composeType(t, "DocumentManifest", "p", element.getP(), -1);
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
    if (element.hasSubject())
      composeReference(t, "DocumentReference", "subject", element.getSubject(), -1);
    if (element.hasType())
      composeCodeableConcept(t, "DocumentReference", "type", element.getType(), -1);
    if (element.hasClass_())
      composeCodeableConcept(t, "DocumentReference", "class", element.getClass_(), -1);
    for (int i = 0; i < element.getAuthor().size(); i++)
      composeReference(t, "DocumentReference", "author", element.getAuthor().get(i), i);
    if (element.hasCustodian())
      composeReference(t, "DocumentReference", "custodian", element.getCustodian(), -1);
    if (element.hasAuthenticator())
      composeReference(t, "DocumentReference", "authenticator", element.getAuthenticator(), -1);
    if (element.hasCreatedElement())
      composeDateTime(t, "DocumentReference", "created", element.getCreatedElement(), -1);
    if (element.hasIndexedElement())
      composeInstant(t, "DocumentReference", "indexed", element.getIndexedElement(), -1);
    if (element.hasStatusElement())
      composeEnum(t, "DocumentReference", "status", element.getStatusElement(), -1);
    if (element.hasDocStatus())
      composeCodeableConcept(t, "DocumentReference", "docStatus", element.getDocStatus(), -1);
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
    for (int i = 0; i < element.getFormat().size(); i++)
      composeCoding(t, "DocumentReference", "format", element.getFormat().get(i), i);
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
    if (element.hasRuleset())
      composeCoding(t, "EligibilityRequest", "ruleset", element.getRuleset(), -1);
    if (element.hasOriginalRuleset())
      composeCoding(t, "EligibilityRequest", "originalRuleset", element.getOriginalRuleset(), -1);
    if (element.hasCreatedElement())
      composeDateTime(t, "EligibilityRequest", "created", element.getCreatedElement(), -1);
    if (element.hasTarget())
      composeType(t, "EligibilityRequest", "target", element.getTarget(), -1);
    if (element.hasProvider())
      composeType(t, "EligibilityRequest", "provider", element.getProvider(), -1);
    if (element.hasOrganization())
      composeType(t, "EligibilityRequest", "organization", element.getOrganization(), -1);
    if (element.hasPriority())
      composeCoding(t, "EligibilityRequest", "priority", element.getPriority(), -1);
    if (element.hasEnterer())
      composeType(t, "EligibilityRequest", "enterer", element.getEnterer(), -1);
    if (element.hasFacility())
      composeType(t, "EligibilityRequest", "facility", element.getFacility(), -1);
    if (element.hasPatient())
      composeType(t, "EligibilityRequest", "patient", element.getPatient(), -1);
    if (element.hasCoverage())
      composeType(t, "EligibilityRequest", "coverage", element.getCoverage(), -1);
    if (element.hasBusinessArrangementElement())
      composeString(t, "EligibilityRequest", "businessArrangement", element.getBusinessArrangementElement(), -1);
    if (element.hasServiced())
      composeType(t, "EligibilityRequest", "serviced", element.getServiced(), -1);
    if (element.hasBenefitCategory())
      composeCoding(t, "EligibilityRequest", "benefitCategory", element.getBenefitCategory(), -1);
    if (element.hasBenefitSubCategory())
      composeCoding(t, "EligibilityRequest", "benefitSubCategory", element.getBenefitSubCategory(), -1);
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
    if (element.hasRequest())
      composeType(t, "EligibilityResponse", "request", element.getRequest(), -1);
    if (element.hasOutcomeElement())
      composeEnum(t, "EligibilityResponse", "outcome", element.getOutcomeElement(), -1);
    if (element.hasDispositionElement())
      composeString(t, "EligibilityResponse", "disposition", element.getDispositionElement(), -1);
    if (element.hasRuleset())
      composeCoding(t, "EligibilityResponse", "ruleset", element.getRuleset(), -1);
    if (element.hasOriginalRuleset())
      composeCoding(t, "EligibilityResponse", "originalRuleset", element.getOriginalRuleset(), -1);
    if (element.hasCreatedElement())
      composeDateTime(t, "EligibilityResponse", "created", element.getCreatedElement(), -1);
    if (element.hasOrganization())
      composeType(t, "EligibilityResponse", "organization", element.getOrganization(), -1);
    if (element.hasRequestProvider())
      composeType(t, "EligibilityResponse", "requestProvider", element.getRequestProvider(), -1);
    if (element.hasRequestOrganization())
      composeType(t, "EligibilityResponse", "requestOrganization", element.getRequestOrganization(), -1);
    if (element.hasInforceElement())
      composeBoolean(t, "EligibilityResponse", "inforce", element.getInforceElement(), -1);
    if (element.hasContract())
      composeReference(t, "EligibilityResponse", "contract", element.getContract(), -1);
    if (element.hasForm())
      composeCoding(t, "EligibilityResponse", "form", element.getForm(), -1);
    for (int i = 0; i < element.getBenefitBalance().size(); i++)
      composeEligibilityResponseBenefitsComponent(t, "EligibilityResponse", "benefitBalance", element.getBenefitBalance().get(i), i);
    for (int i = 0; i < element.getError().size(); i++)
      composeEligibilityResponseErrorsComponent(t, "EligibilityResponse", "error", element.getError().get(i), i);
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
      composeCoding(t, "EligibilityResponse", "category", element.getCategory(), -1);
    if (element.hasSubCategory())
      composeCoding(t, "EligibilityResponse", "subCategory", element.getSubCategory(), -1);
    if (element.hasNetwork())
      composeCoding(t, "EligibilityResponse", "network", element.getNetwork(), -1);
    if (element.hasUnit())
      composeCoding(t, "EligibilityResponse", "unit", element.getUnit(), -1);
    if (element.hasTerm())
      composeCoding(t, "EligibilityResponse", "term", element.getTerm(), -1);
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
      composeCoding(t, "EligibilityResponse", "type", element.getType(), -1);
    if (element.hasBenefit())
      composeType(t, "EligibilityResponse", "benefit", element.getBenefit(), -1);
    if (element.hasBenefitUsed())
      composeType(t, "EligibilityResponse", "benefitUsed", element.getBenefitUsed(), -1);
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
      composeCoding(t, "EligibilityResponse", "code", element.getCode(), -1);
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
      composeEncounterEncounterStatusHistoryComponent(t, "Encounter", "statusHistory", element.getStatusHistory().get(i), i);
    if (element.hasClass_Element())
      composeEnum(t, "Encounter", "class", element.getClass_Element(), -1);
    for (int i = 0; i < element.getType().size(); i++)
      composeCodeableConcept(t, "Encounter", "type", element.getType().get(i), i);
    if (element.hasPriority())
      composeCodeableConcept(t, "Encounter", "priority", element.getPriority(), -1);
    if (element.hasPatient())
      composeReference(t, "Encounter", "patient", element.getPatient(), -1);
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
      composeQuantity(t, "Encounter", "length", element.getLength(), -1);
    for (int i = 0; i < element.getReason().size(); i++)
      composeCodeableConcept(t, "Encounter", "reason", element.getReason().get(i), i);
    for (int i = 0; i < element.getIndication().size(); i++)
      composeReference(t, "Encounter", "indication", element.getIndication().get(i), i);
    if (element.hasHospitalization())
      composeEncounterEncounterHospitalizationComponent(t, "Encounter", "hospitalization", element.getHospitalization(), -1);
    for (int i = 0; i < element.getLocation().size(); i++)
      composeEncounterEncounterLocationComponent(t, "Encounter", "location", element.getLocation().get(i), i);
    if (element.hasServiceProvider())
      composeReference(t, "Encounter", "serviceProvider", element.getServiceProvider(), -1);
    if (element.hasPartOf())
      composeReference(t, "Encounter", "partOf", element.getPartOf(), -1);
  }

  protected void composeEncounterEncounterStatusHistoryComponent(Complex parent, String parentType, String name, Encounter.EncounterStatusHistoryComponent element, int index) {
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
    for (int i = 0; i < element.getAdmittingDiagnosis().size(); i++)
      composeReference(t, "Encounter", "admittingDiagnosis", element.getAdmittingDiagnosis().get(i), i);
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
    for (int i = 0; i < element.getDischargeDiagnosis().size(); i++)
      composeReference(t, "Encounter", "dischargeDiagnosis", element.getDischargeDiagnosis().get(i), i);
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
    if (element.hasRuleset())
      composeCoding(t, "EnrollmentRequest", "ruleset", element.getRuleset(), -1);
    if (element.hasOriginalRuleset())
      composeCoding(t, "EnrollmentRequest", "originalRuleset", element.getOriginalRuleset(), -1);
    if (element.hasCreatedElement())
      composeDateTime(t, "EnrollmentRequest", "created", element.getCreatedElement(), -1);
    if (element.hasTarget())
      composeReference(t, "EnrollmentRequest", "target", element.getTarget(), -1);
    if (element.hasProvider())
      composeReference(t, "EnrollmentRequest", "provider", element.getProvider(), -1);
    if (element.hasOrganization())
      composeReference(t, "EnrollmentRequest", "organization", element.getOrganization(), -1);
    if (element.hasSubject())
      composeReference(t, "EnrollmentRequest", "subject", element.getSubject(), -1);
    if (element.hasCoverage())
      composeReference(t, "EnrollmentRequest", "coverage", element.getCoverage(), -1);
    if (element.hasRelationship())
      composeCoding(t, "EnrollmentRequest", "relationship", element.getRelationship(), -1);
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
    if (element.hasRequest())
      composeReference(t, "EnrollmentResponse", "request", element.getRequest(), -1);
    if (element.hasOutcomeElement())
      composeEnum(t, "EnrollmentResponse", "outcome", element.getOutcomeElement(), -1);
    if (element.hasDispositionElement())
      composeString(t, "EnrollmentResponse", "disposition", element.getDispositionElement(), -1);
    if (element.hasRuleset())
      composeCoding(t, "EnrollmentResponse", "ruleset", element.getRuleset(), -1);
    if (element.hasOriginalRuleset())
      composeCoding(t, "EnrollmentResponse", "originalRuleset", element.getOriginalRuleset(), -1);
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
    for (int i = 0; i < element.getCondition().size(); i++)
      composeReference(t, "EpisodeOfCare", "condition", element.getCondition().get(i), i);
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
    if (element.hasPublisherElement())
      composeString(t, "ExpansionProfile", "publisher", element.getPublisherElement(), -1);
    for (int i = 0; i < element.getContact().size(); i++)
      composeExpansionProfileExpansionProfileContactComponent(t, "ExpansionProfile", "contact", element.getContact().get(i), i);
    if (element.hasDateElement())
      composeDateTime(t, "ExpansionProfile", "date", element.getDateElement(), -1);
    if (element.hasDescriptionElement())
      composeString(t, "ExpansionProfile", "description", element.getDescriptionElement(), -1);
    if (element.hasCodeSystem())
      composeExpansionProfileExpansionProfileCodeSystemComponent(t, "ExpansionProfile", "codeSystem", element.getCodeSystem(), -1);
    if (element.hasIncludeDesignationsElement())
      composeBoolean(t, "ExpansionProfile", "includeDesignations", element.getIncludeDesignationsElement(), -1);
    if (element.hasDesignation())
      composeExpansionProfileExpansionProfileDesignationComponent(t, "ExpansionProfile", "designation", element.getDesignation(), -1);
    if (element.hasIncludeDefinitionElement())
      composeBoolean(t, "ExpansionProfile", "includeDefinition", element.getIncludeDefinitionElement(), -1);
    if (element.hasIncludeInactiveElement())
      composeBoolean(t, "ExpansionProfile", "includeInactive", element.getIncludeInactiveElement(), -1);
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

  protected void composeExpansionProfileExpansionProfileContactComponent(Complex parent, String parentType, String name, ExpansionProfile.ExpansionProfileContactComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "contact", name, element, index);
    if (element.hasNameElement())
      composeString(t, "ExpansionProfile", "name", element.getNameElement(), -1);
    for (int i = 0; i < element.getTelecom().size(); i++)
      composeContactPoint(t, "ExpansionProfile", "telecom", element.getTelecom().get(i), i);
  }

  protected void composeExpansionProfileExpansionProfileCodeSystemComponent(Complex parent, String parentType, String name, ExpansionProfile.ExpansionProfileCodeSystemComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "codeSystem", name, element, index);
    if (element.hasInclude())
      composeExpansionProfileCodeSystemIncludeComponent(t, "ExpansionProfile", "include", element.getInclude(), -1);
    if (element.hasExclude())
      composeExpansionProfileCodeSystemExcludeComponent(t, "ExpansionProfile", "exclude", element.getExclude(), -1);
  }

  protected void composeExpansionProfileCodeSystemIncludeComponent(Complex parent, String parentType, String name, ExpansionProfile.CodeSystemIncludeComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "include", name, element, index);
    for (int i = 0; i < element.getCodeSystem().size(); i++)
      composeExpansionProfileCodeSystemIncludeCodeSystemComponent(t, "ExpansionProfile", "codeSystem", element.getCodeSystem().get(i), i);
  }

  protected void composeExpansionProfileCodeSystemIncludeCodeSystemComponent(Complex parent, String parentType, String name, ExpansionProfile.CodeSystemIncludeCodeSystemComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "codeSystem", name, element, index);
    if (element.hasSystemElement())
      composeUri(t, "ExpansionProfile", "system", element.getSystemElement(), -1);
    if (element.hasVersionElement())
      composeString(t, "ExpansionProfile", "version", element.getVersionElement(), -1);
  }

  protected void composeExpansionProfileCodeSystemExcludeComponent(Complex parent, String parentType, String name, ExpansionProfile.CodeSystemExcludeComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "exclude", name, element, index);
    for (int i = 0; i < element.getCodeSystem().size(); i++)
      composeExpansionProfileCodeSystemExcludeCodeSystemComponent(t, "ExpansionProfile", "codeSystem", element.getCodeSystem().get(i), i);
  }

  protected void composeExpansionProfileCodeSystemExcludeCodeSystemComponent(Complex parent, String parentType, String name, ExpansionProfile.CodeSystemExcludeCodeSystemComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "codeSystem", name, element, index);
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
    if (element.hasClaim())
      composeType(t, "ExplanationOfBenefit", "claim", element.getClaim(), -1);
    if (element.hasClaimResponse())
      composeType(t, "ExplanationOfBenefit", "claimResponse", element.getClaimResponse(), -1);
    for (int i = 0; i < element.getSubType().size(); i++)
      composeCoding(t, "ExplanationOfBenefit", "subType", element.getSubType().get(i), i);
    if (element.hasRuleset())
      composeCoding(t, "ExplanationOfBenefit", "ruleset", element.getRuleset(), -1);
    if (element.hasOriginalRuleset())
      composeCoding(t, "ExplanationOfBenefit", "originalRuleset", element.getOriginalRuleset(), -1);
    if (element.hasCreatedElement())
      composeDateTime(t, "ExplanationOfBenefit", "created", element.getCreatedElement(), -1);
    if (element.hasBillablePeriod())
      composePeriod(t, "ExplanationOfBenefit", "billablePeriod", element.getBillablePeriod(), -1);
    if (element.hasDispositionElement())
      composeString(t, "ExplanationOfBenefit", "disposition", element.getDispositionElement(), -1);
    if (element.hasProvider())
      composeType(t, "ExplanationOfBenefit", "provider", element.getProvider(), -1);
    if (element.hasOrganization())
      composeType(t, "ExplanationOfBenefit", "organization", element.getOrganization(), -1);
    if (element.hasFacility())
      composeType(t, "ExplanationOfBenefit", "facility", element.getFacility(), -1);
    for (int i = 0; i < element.getRelated().size(); i++)
      composeExplanationOfBenefitRelatedClaimsComponent(t, "ExplanationOfBenefit", "related", element.getRelated().get(i), i);
    if (element.hasPrescription())
      composeType(t, "ExplanationOfBenefit", "prescription", element.getPrescription(), -1);
    if (element.hasOriginalPrescription())
      composeType(t, "ExplanationOfBenefit", "originalPrescription", element.getOriginalPrescription(), -1);
    if (element.hasPayee())
      composeExplanationOfBenefitPayeeComponent(t, "ExplanationOfBenefit", "payee", element.getPayee(), -1);
    if (element.hasReferral())
      composeType(t, "ExplanationOfBenefit", "referral", element.getReferral(), -1);
    for (int i = 0; i < element.getOccurrenceCode().size(); i++)
      composeCoding(t, "ExplanationOfBenefit", "occurrenceCode", element.getOccurrenceCode().get(i), i);
    for (int i = 0; i < element.getOccurenceSpanCode().size(); i++)
      composeCoding(t, "ExplanationOfBenefit", "occurenceSpanCode", element.getOccurenceSpanCode().get(i), i);
    for (int i = 0; i < element.getValueCode().size(); i++)
      composeCoding(t, "ExplanationOfBenefit", "valueCode", element.getValueCode().get(i), i);
    for (int i = 0; i < element.getDiagnosis().size(); i++)
      composeExplanationOfBenefitDiagnosisComponent(t, "ExplanationOfBenefit", "diagnosis", element.getDiagnosis().get(i), i);
    for (int i = 0; i < element.getProcedure().size(); i++)
      composeExplanationOfBenefitProcedureComponent(t, "ExplanationOfBenefit", "procedure", element.getProcedure().get(i), i);
    for (int i = 0; i < element.getSpecialCondition().size(); i++)
      composeCoding(t, "ExplanationOfBenefit", "specialCondition", element.getSpecialCondition().get(i), i);
    if (element.hasPatient())
      composeType(t, "ExplanationOfBenefit", "patient", element.getPatient(), -1);
    if (element.hasPrecedenceElement())
      composePositiveInt(t, "ExplanationOfBenefit", "precedence", element.getPrecedenceElement(), -1);
    if (element.hasCoverage())
      composeExplanationOfBenefitCoverageComponent(t, "ExplanationOfBenefit", "coverage", element.getCoverage(), -1);
    if (element.hasAccidentDateElement())
      composeDate(t, "ExplanationOfBenefit", "accidentDate", element.getAccidentDateElement(), -1);
    if (element.hasAccidentType())
      composeCoding(t, "ExplanationOfBenefit", "accidentType", element.getAccidentType(), -1);
    if (element.hasAccidentLocation())
      composeType(t, "ExplanationOfBenefit", "accidentLocation", element.getAccidentLocation(), -1);
    for (int i = 0; i < element.getInterventionException().size(); i++)
      composeCoding(t, "ExplanationOfBenefit", "interventionException", element.getInterventionException().get(i), i);
    for (int i = 0; i < element.getOnset().size(); i++)
      composeExplanationOfBenefitOnsetComponent(t, "ExplanationOfBenefit", "onset", element.getOnset().get(i), i);
    if (element.hasEmploymentImpacted())
      composePeriod(t, "ExplanationOfBenefit", "employmentImpacted", element.getEmploymentImpacted(), -1);
    if (element.hasHospitalization())
      composePeriod(t, "ExplanationOfBenefit", "hospitalization", element.getHospitalization(), -1);
    for (int i = 0; i < element.getItem().size(); i++)
      composeExplanationOfBenefitItemsComponent(t, "ExplanationOfBenefit", "item", element.getItem().get(i), i);
    for (int i = 0; i < element.getAddItem().size(); i++)
      composeExplanationOfBenefitAddedItemComponent(t, "ExplanationOfBenefit", "addItem", element.getAddItem().get(i), i);
    for (int i = 0; i < element.getMissingTeeth().size(); i++)
      composeExplanationOfBenefitMissingTeethComponent(t, "ExplanationOfBenefit", "missingTeeth", element.getMissingTeeth().get(i), i);
    if (element.hasTotalCost())
      composeQuantity(t, "ExplanationOfBenefit", "totalCost", element.getTotalCost(), -1);
    if (element.hasUnallocDeductable())
      composeQuantity(t, "ExplanationOfBenefit", "unallocDeductable", element.getUnallocDeductable(), -1);
    if (element.hasTotalBenefit())
      composeQuantity(t, "ExplanationOfBenefit", "totalBenefit", element.getTotalBenefit(), -1);
    if (element.hasPaymentAdjustment())
      composeQuantity(t, "ExplanationOfBenefit", "paymentAdjustment", element.getPaymentAdjustment(), -1);
    if (element.hasPaymentAdjustmentReason())
      composeCoding(t, "ExplanationOfBenefit", "paymentAdjustmentReason", element.getPaymentAdjustmentReason(), -1);
    if (element.hasPaymentDateElement())
      composeDate(t, "ExplanationOfBenefit", "paymentDate", element.getPaymentDateElement(), -1);
    if (element.hasPaymentAmount())
      composeQuantity(t, "ExplanationOfBenefit", "paymentAmount", element.getPaymentAmount(), -1);
    if (element.hasPaymentRef())
      composeIdentifier(t, "ExplanationOfBenefit", "paymentRef", element.getPaymentRef(), -1);
    if (element.hasReserved())
      composeCoding(t, "ExplanationOfBenefit", "reserved", element.getReserved(), -1);
    if (element.hasForm())
      composeCoding(t, "ExplanationOfBenefit", "form", element.getForm(), -1);
    for (int i = 0; i < element.getNote().size(); i++)
      composeExplanationOfBenefitNotesComponent(t, "ExplanationOfBenefit", "note", element.getNote().get(i), i);
    for (int i = 0; i < element.getBenefitBalance().size(); i++)
      composeExplanationOfBenefitBenefitBalanceComponent(t, "ExplanationOfBenefit", "benefitBalance", element.getBenefitBalance().get(i), i);
  }

  protected void composeExplanationOfBenefitRelatedClaimsComponent(Complex parent, String parentType, String name, ExplanationOfBenefit.RelatedClaimsComponent element, int index) {
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
      composeType(t, "ExplanationOfBenefit", "claim", element.getClaim(), -1);
    if (element.hasRelationship())
      composeCoding(t, "ExplanationOfBenefit", "relationship", element.getRelationship(), -1);
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
      composeCoding(t, "ExplanationOfBenefit", "type", element.getType(), -1);
    if (element.hasParty())
      composeType(t, "ExplanationOfBenefit", "party", element.getParty(), -1);
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
      composeCoding(t, "ExplanationOfBenefit", "diagnosis", element.getDiagnosis(), -1);
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

  protected void composeExplanationOfBenefitCoverageComponent(Complex parent, String parentType, String name, ExplanationOfBenefit.CoverageComponent element, int index) {
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
      composeType(t, "ExplanationOfBenefit", "coverage", element.getCoverage(), -1);
    for (int i = 0; i < element.getPreAuthRef().size(); i++)
      composeString(t, "ExplanationOfBenefit", "preAuthRef", element.getPreAuthRef().get(i), i);
  }

  protected void composeExplanationOfBenefitOnsetComponent(Complex parent, String parentType, String name, ExplanationOfBenefit.OnsetComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "onset", name, element, index);
    if (element.hasTime())
      composeType(t, "ExplanationOfBenefit", "time", element.getTime(), -1);
    if (element.hasType())
      composeCoding(t, "ExplanationOfBenefit", "type", element.getType(), -1);
  }

  protected void composeExplanationOfBenefitItemsComponent(Complex parent, String parentType, String name, ExplanationOfBenefit.ItemsComponent element, int index) {
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
    if (element.hasType())
      composeCoding(t, "ExplanationOfBenefit", "type", element.getType(), -1);
    if (element.hasProvider())
      composeType(t, "ExplanationOfBenefit", "provider", element.getProvider(), -1);
    if (element.hasSupervisor())
      composeType(t, "ExplanationOfBenefit", "supervisor", element.getSupervisor(), -1);
    if (element.hasProviderQualification())
      composeCoding(t, "ExplanationOfBenefit", "providerQualification", element.getProviderQualification(), -1);
    for (int i = 0; i < element.getDiagnosisLinkId().size(); i++)
      composePositiveInt(t, "ExplanationOfBenefit", "diagnosisLinkId", element.getDiagnosisLinkId().get(i), i);
    if (element.hasService())
      composeCoding(t, "ExplanationOfBenefit", "service", element.getService(), -1);
    for (int i = 0; i < element.getServiceModifier().size(); i++)
      composeCoding(t, "ExplanationOfBenefit", "serviceModifier", element.getServiceModifier().get(i), i);
    for (int i = 0; i < element.getModifier().size(); i++)
      composeCoding(t, "ExplanationOfBenefit", "modifier", element.getModifier().get(i), i);
    for (int i = 0; i < element.getProgramCode().size(); i++)
      composeCoding(t, "ExplanationOfBenefit", "programCode", element.getProgramCode().get(i), i);
    if (element.hasServiced())
      composeType(t, "ExplanationOfBenefit", "serviced", element.getServiced(), -1);
    if (element.hasPlace())
      composeCoding(t, "ExplanationOfBenefit", "place", element.getPlace(), -1);
    if (element.hasQuantity())
      composeQuantity(t, "ExplanationOfBenefit", "quantity", element.getQuantity(), -1);
    if (element.hasUnitPrice())
      composeQuantity(t, "ExplanationOfBenefit", "unitPrice", element.getUnitPrice(), -1);
    if (element.hasFactorElement())
      composeDecimal(t, "ExplanationOfBenefit", "factor", element.getFactorElement(), -1);
    if (element.hasPointsElement())
      composeDecimal(t, "ExplanationOfBenefit", "points", element.getPointsElement(), -1);
    if (element.hasNet())
      composeQuantity(t, "ExplanationOfBenefit", "net", element.getNet(), -1);
    for (int i = 0; i < element.getUdi().size(); i++)
      composeReference(t, "ExplanationOfBenefit", "udi", element.getUdi().get(i), i);
    if (element.hasBodySite())
      composeCoding(t, "ExplanationOfBenefit", "bodySite", element.getBodySite(), -1);
    for (int i = 0; i < element.getSubSite().size(); i++)
      composeCoding(t, "ExplanationOfBenefit", "subSite", element.getSubSite().get(i), i);
    for (int i = 0; i < element.getNoteNumber().size(); i++)
      composePositiveInt(t, "ExplanationOfBenefit", "noteNumber", element.getNoteNumber().get(i), i);
    for (int i = 0; i < element.getAdjudication().size(); i++)
      composeExplanationOfBenefitItemAdjudicationComponent(t, "ExplanationOfBenefit", "adjudication", element.getAdjudication().get(i), i);
    for (int i = 0; i < element.getDetail().size(); i++)
      composeExplanationOfBenefitDetailComponent(t, "ExplanationOfBenefit", "detail", element.getDetail().get(i), i);
    if (element.hasProsthesis())
      composeExplanationOfBenefitProsthesisComponent(t, "ExplanationOfBenefit", "prosthesis", element.getProsthesis(), -1);
  }

  protected void composeExplanationOfBenefitItemAdjudicationComponent(Complex parent, String parentType, String name, ExplanationOfBenefit.ItemAdjudicationComponent element, int index) {
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
      composeCoding(t, "ExplanationOfBenefit", "category", element.getCategory(), -1);
    if (element.hasReason())
      composeCoding(t, "ExplanationOfBenefit", "reason", element.getReason(), -1);
    if (element.hasAmount())
      composeQuantity(t, "ExplanationOfBenefit", "amount", element.getAmount(), -1);
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
    if (element.hasType())
      composeCoding(t, "ExplanationOfBenefit", "type", element.getType(), -1);
    if (element.hasService())
      composeCoding(t, "ExplanationOfBenefit", "service", element.getService(), -1);
    for (int i = 0; i < element.getProgramCode().size(); i++)
      composeCoding(t, "ExplanationOfBenefit", "programCode", element.getProgramCode().get(i), i);
    if (element.hasQuantity())
      composeQuantity(t, "ExplanationOfBenefit", "quantity", element.getQuantity(), -1);
    if (element.hasUnitPrice())
      composeQuantity(t, "ExplanationOfBenefit", "unitPrice", element.getUnitPrice(), -1);
    if (element.hasFactorElement())
      composeDecimal(t, "ExplanationOfBenefit", "factor", element.getFactorElement(), -1);
    if (element.hasPointsElement())
      composeDecimal(t, "ExplanationOfBenefit", "points", element.getPointsElement(), -1);
    if (element.hasNet())
      composeQuantity(t, "ExplanationOfBenefit", "net", element.getNet(), -1);
    for (int i = 0; i < element.getUdi().size(); i++)
      composeReference(t, "ExplanationOfBenefit", "udi", element.getUdi().get(i), i);
    for (int i = 0; i < element.getAdjudication().size(); i++)
      composeExplanationOfBenefitDetailAdjudicationComponent(t, "ExplanationOfBenefit", "adjudication", element.getAdjudication().get(i), i);
    for (int i = 0; i < element.getSubDetail().size(); i++)
      composeExplanationOfBenefitSubDetailComponent(t, "ExplanationOfBenefit", "subDetail", element.getSubDetail().get(i), i);
  }

  protected void composeExplanationOfBenefitDetailAdjudicationComponent(Complex parent, String parentType, String name, ExplanationOfBenefit.DetailAdjudicationComponent element, int index) {
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
      composeCoding(t, "ExplanationOfBenefit", "category", element.getCategory(), -1);
    if (element.hasReason())
      composeCoding(t, "ExplanationOfBenefit", "reason", element.getReason(), -1);
    if (element.hasAmount())
      composeQuantity(t, "ExplanationOfBenefit", "amount", element.getAmount(), -1);
    if (element.hasValueElement())
      composeDecimal(t, "ExplanationOfBenefit", "value", element.getValueElement(), -1);
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
    if (element.hasType())
      composeCoding(t, "ExplanationOfBenefit", "type", element.getType(), -1);
    if (element.hasService())
      composeCoding(t, "ExplanationOfBenefit", "service", element.getService(), -1);
    for (int i = 0; i < element.getProgramCode().size(); i++)
      composeCoding(t, "ExplanationOfBenefit", "programCode", element.getProgramCode().get(i), i);
    if (element.hasQuantity())
      composeQuantity(t, "ExplanationOfBenefit", "quantity", element.getQuantity(), -1);
    if (element.hasUnitPrice())
      composeQuantity(t, "ExplanationOfBenefit", "unitPrice", element.getUnitPrice(), -1);
    if (element.hasFactorElement())
      composeDecimal(t, "ExplanationOfBenefit", "factor", element.getFactorElement(), -1);
    if (element.hasPointsElement())
      composeDecimal(t, "ExplanationOfBenefit", "points", element.getPointsElement(), -1);
    if (element.hasNet())
      composeQuantity(t, "ExplanationOfBenefit", "net", element.getNet(), -1);
    for (int i = 0; i < element.getUdi().size(); i++)
      composeReference(t, "ExplanationOfBenefit", "udi", element.getUdi().get(i), i);
    for (int i = 0; i < element.getAdjudication().size(); i++)
      composeExplanationOfBenefitSubDetailAdjudicationComponent(t, "ExplanationOfBenefit", "adjudication", element.getAdjudication().get(i), i);
  }

  protected void composeExplanationOfBenefitSubDetailAdjudicationComponent(Complex parent, String parentType, String name, ExplanationOfBenefit.SubDetailAdjudicationComponent element, int index) {
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
      composeCoding(t, "ExplanationOfBenefit", "category", element.getCategory(), -1);
    if (element.hasReason())
      composeCoding(t, "ExplanationOfBenefit", "reason", element.getReason(), -1);
    if (element.hasAmount())
      composeQuantity(t, "ExplanationOfBenefit", "amount", element.getAmount(), -1);
    if (element.hasValueElement())
      composeDecimal(t, "ExplanationOfBenefit", "value", element.getValueElement(), -1);
  }

  protected void composeExplanationOfBenefitProsthesisComponent(Complex parent, String parentType, String name, ExplanationOfBenefit.ProsthesisComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "prosthesis", name, element, index);
    if (element.hasInitialElement())
      composeBoolean(t, "ExplanationOfBenefit", "initial", element.getInitialElement(), -1);
    if (element.hasPriorDateElement())
      composeDate(t, "ExplanationOfBenefit", "priorDate", element.getPriorDateElement(), -1);
    if (element.hasPriorMaterial())
      composeCoding(t, "ExplanationOfBenefit", "priorMaterial", element.getPriorMaterial(), -1);
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
    if (element.hasService())
      composeCoding(t, "ExplanationOfBenefit", "service", element.getService(), -1);
    if (element.hasFee())
      composeQuantity(t, "ExplanationOfBenefit", "fee", element.getFee(), -1);
    for (int i = 0; i < element.getNoteNumberLinkId().size(); i++)
      composePositiveInt(t, "ExplanationOfBenefit", "noteNumberLinkId", element.getNoteNumberLinkId().get(i), i);
    for (int i = 0; i < element.getAdjudication().size(); i++)
      composeExplanationOfBenefitAddedItemAdjudicationComponent(t, "ExplanationOfBenefit", "adjudication", element.getAdjudication().get(i), i);
    for (int i = 0; i < element.getDetail().size(); i++)
      composeExplanationOfBenefitAddedItemsDetailComponent(t, "ExplanationOfBenefit", "detail", element.getDetail().get(i), i);
  }

  protected void composeExplanationOfBenefitAddedItemAdjudicationComponent(Complex parent, String parentType, String name, ExplanationOfBenefit.AddedItemAdjudicationComponent element, int index) {
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
      composeCoding(t, "ExplanationOfBenefit", "category", element.getCategory(), -1);
    if (element.hasReason())
      composeCoding(t, "ExplanationOfBenefit", "reason", element.getReason(), -1);
    if (element.hasAmount())
      composeQuantity(t, "ExplanationOfBenefit", "amount", element.getAmount(), -1);
    if (element.hasValueElement())
      composeDecimal(t, "ExplanationOfBenefit", "value", element.getValueElement(), -1);
  }

  protected void composeExplanationOfBenefitAddedItemsDetailComponent(Complex parent, String parentType, String name, ExplanationOfBenefit.AddedItemsDetailComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "detail", name, element, index);
    if (element.hasService())
      composeCoding(t, "ExplanationOfBenefit", "service", element.getService(), -1);
    if (element.hasFee())
      composeQuantity(t, "ExplanationOfBenefit", "fee", element.getFee(), -1);
    for (int i = 0; i < element.getAdjudication().size(); i++)
      composeExplanationOfBenefitAddedItemDetailAdjudicationComponent(t, "ExplanationOfBenefit", "adjudication", element.getAdjudication().get(i), i);
  }

  protected void composeExplanationOfBenefitAddedItemDetailAdjudicationComponent(Complex parent, String parentType, String name, ExplanationOfBenefit.AddedItemDetailAdjudicationComponent element, int index) {
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
      composeCoding(t, "ExplanationOfBenefit", "category", element.getCategory(), -1);
    if (element.hasReason())
      composeCoding(t, "ExplanationOfBenefit", "reason", element.getReason(), -1);
    if (element.hasAmount())
      composeQuantity(t, "ExplanationOfBenefit", "amount", element.getAmount(), -1);
    if (element.hasValueElement())
      composeDecimal(t, "ExplanationOfBenefit", "value", element.getValueElement(), -1);
  }

  protected void composeExplanationOfBenefitMissingTeethComponent(Complex parent, String parentType, String name, ExplanationOfBenefit.MissingTeethComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "missingTeeth", name, element, index);
    if (element.hasTooth())
      composeCoding(t, "ExplanationOfBenefit", "tooth", element.getTooth(), -1);
    if (element.hasReason())
      composeCoding(t, "ExplanationOfBenefit", "reason", element.getReason(), -1);
    if (element.hasExtractionDateElement())
      composeDate(t, "ExplanationOfBenefit", "extractionDate", element.getExtractionDateElement(), -1);
  }

  protected void composeExplanationOfBenefitNotesComponent(Complex parent, String parentType, String name, ExplanationOfBenefit.NotesComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "note", name, element, index);
    if (element.hasNumberElement())
      composePositiveInt(t, "ExplanationOfBenefit", "number", element.getNumberElement(), -1);
    if (element.hasType())
      composeCoding(t, "ExplanationOfBenefit", "type", element.getType(), -1);
    if (element.hasTextElement())
      composeString(t, "ExplanationOfBenefit", "text", element.getTextElement(), -1);
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
      composeCoding(t, "ExplanationOfBenefit", "category", element.getCategory(), -1);
    if (element.hasSubCategory())
      composeCoding(t, "ExplanationOfBenefit", "subCategory", element.getSubCategory(), -1);
    if (element.hasNetwork())
      composeCoding(t, "ExplanationOfBenefit", "network", element.getNetwork(), -1);
    if (element.hasUnit())
      composeCoding(t, "ExplanationOfBenefit", "unit", element.getUnit(), -1);
    if (element.hasTerm())
      composeCoding(t, "ExplanationOfBenefit", "term", element.getTerm(), -1);
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
      composeCoding(t, "ExplanationOfBenefit", "type", element.getType(), -1);
    if (element.hasBenefit())
      composeType(t, "ExplanationOfBenefit", "benefit", element.getBenefit(), -1);
    if (element.hasBenefitUsed())
      composeType(t, "ExplanationOfBenefit", "benefitUsed", element.getBenefitUsed(), -1);
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
    if (element.hasPatient())
      composeReference(t, "FamilyMemberHistory", "patient", element.getPatient(), -1);
    if (element.hasDateElement())
      composeDateTime(t, "FamilyMemberHistory", "date", element.getDateElement(), -1);
    if (element.hasStatusElement())
      composeEnum(t, "FamilyMemberHistory", "status", element.getStatusElement(), -1);
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
    if (element.hasDeceased())
      composeType(t, "FamilyMemberHistory", "deceased", element.getDeceased(), -1);
    if (element.hasNote())
      composeAnnotation(t, "FamilyMemberHistory", "note", element.getNote(), -1);
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
    if (element.hasNote())
      composeAnnotation(t, "FamilyMemberHistory", "note", element.getNote(), -1);
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
    if (element.hasCategory())
      composeCodeableConcept(t, "Flag", "category", element.getCategory(), -1);
    if (element.hasStatusElement())
      composeEnum(t, "Flag", "status", element.getStatusElement(), -1);
    if (element.hasPeriod())
      composePeriod(t, "Flag", "period", element.getPeriod(), -1);
    if (element.hasSubject())
      composeReference(t, "Flag", "subject", element.getSubject(), -1);
    if (element.hasEncounter())
      composeReference(t, "Flag", "encounter", element.getEncounter(), -1);
    if (element.hasAuthor())
      composeReference(t, "Flag", "author", element.getAuthor(), -1);
    if (element.hasCode())
      composeCodeableConcept(t, "Flag", "code", element.getCode(), -1);
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
    if (element.hasSubject())
      composeReference(t, "Goal", "subject", element.getSubject(), -1);
    if (element.hasStart())
      composeType(t, "Goal", "start", element.getStart(), -1);
    if (element.hasTarget())
      composeType(t, "Goal", "target", element.getTarget(), -1);
    for (int i = 0; i < element.getCategory().size(); i++)
      composeCodeableConcept(t, "Goal", "category", element.getCategory().get(i), i);
    if (element.hasDescriptionElement())
      composeString(t, "Goal", "description", element.getDescriptionElement(), -1);
    if (element.hasStatusElement())
      composeEnum(t, "Goal", "status", element.getStatusElement(), -1);
    if (element.hasStatusDateElement())
      composeDate(t, "Goal", "statusDate", element.getStatusDateElement(), -1);
    if (element.hasStatusReason())
      composeCodeableConcept(t, "Goal", "statusReason", element.getStatusReason(), -1);
    if (element.hasAuthor())
      composeReference(t, "Goal", "author", element.getAuthor(), -1);
    if (element.hasPriority())
      composeCodeableConcept(t, "Goal", "priority", element.getPriority(), -1);
    for (int i = 0; i < element.getAddresses().size(); i++)
      composeReference(t, "Goal", "addresses", element.getAddresses().get(i), i);
    for (int i = 0; i < element.getNote().size(); i++)
      composeAnnotation(t, "Goal", "note", element.getNote().get(i), i);
    for (int i = 0; i < element.getOutcome().size(); i++)
      composeGoalGoalOutcomeComponent(t, "Goal", "outcome", element.getOutcome().get(i), i);
  }

  protected void composeGoalGoalOutcomeComponent(Complex parent, String parentType, String name, Goal.GoalOutcomeComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "outcome", name, element, index);
    if (element.hasResult())
      composeType(t, "Goal", "result", element.getResult(), -1);
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
    if (element.hasTypeElement())
      composeEnum(t, "Group", "type", element.getTypeElement(), -1);
    if (element.hasActualElement())
      composeBoolean(t, "Group", "actual", element.getActualElement(), -1);
    if (element.hasActiveElement())
      composeBoolean(t, "Group", "active", element.getActiveElement(), -1);
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
      composeString(t, "GuidanceResponse", "requestId", element.getRequestIdElement(), -1);
    if (element.hasModule())
      composeReference(t, "GuidanceResponse", "module", element.getModule(), -1);
    if (element.hasStatusElement())
      composeEnum(t, "GuidanceResponse", "status", element.getStatusElement(), -1);
    for (int i = 0; i < element.getEvaluationMessage().size(); i++)
      composeReference(t, "GuidanceResponse", "evaluationMessage", element.getEvaluationMessage().get(i), i);
    if (element.hasOutputParameters())
      composeReference(t, "GuidanceResponse", "outputParameters", element.getOutputParameters(), -1);
    for (int i = 0; i < element.getAction().size(); i++)
      composeGuidanceResponseGuidanceResponseActionComponent(t, "GuidanceResponse", "action", element.getAction().get(i), i);
    for (int i = 0; i < element.getDataRequirement().size(); i++)
      composeDataRequirement(t, "GuidanceResponse", "dataRequirement", element.getDataRequirement().get(i), i);
  }

  protected void composeGuidanceResponseGuidanceResponseActionComponent(Complex parent, String parentType, String name, GuidanceResponse.GuidanceResponseActionComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "action", name, element, index);
    if (element.hasActionIdentifier())
      composeIdentifier(t, "GuidanceResponse", "actionIdentifier", element.getActionIdentifier(), -1);
    if (element.hasLabelElement())
      composeString(t, "GuidanceResponse", "label", element.getLabelElement(), -1);
    if (element.hasTitleElement())
      composeString(t, "GuidanceResponse", "title", element.getTitleElement(), -1);
    if (element.hasDescriptionElement())
      composeString(t, "GuidanceResponse", "description", element.getDescriptionElement(), -1);
    if (element.hasTextEquivalentElement())
      composeString(t, "GuidanceResponse", "textEquivalent", element.getTextEquivalentElement(), -1);
    for (int i = 0; i < element.getConcept().size(); i++)
      composeCodeableConcept(t, "GuidanceResponse", "concept", element.getConcept().get(i), i);
    for (int i = 0; i < element.getSupportingEvidence().size(); i++)
      composeAttachment(t, "GuidanceResponse", "supportingEvidence", element.getSupportingEvidence().get(i), i);
    if (element.hasRelatedAction())
      composeGuidanceResponseGuidanceResponseActionRelatedActionComponent(t, "GuidanceResponse", "relatedAction", element.getRelatedAction(), -1);
    for (int i = 0; i < element.getDocumentation().size(); i++)
      composeAttachment(t, "GuidanceResponse", "documentation", element.getDocumentation().get(i), i);
    for (int i = 0; i < element.getParticipant().size(); i++)
      composeReference(t, "GuidanceResponse", "participant", element.getParticipant().get(i), i);
    if (element.hasTypeElement())
      composeCode(t, "GuidanceResponse", "type", element.getTypeElement(), -1);
    for (int i = 0; i < element.getBehavior().size(); i++)
      composeGuidanceResponseGuidanceResponseActionBehaviorComponent(t, "GuidanceResponse", "behavior", element.getBehavior().get(i), i);
    if (element.hasResource())
      composeReference(t, "GuidanceResponse", "resource", element.getResource(), -1);
    for (int i = 0; i < element.getAction().size(); i++)
      composeGuidanceResponseGuidanceResponseActionComponent(t, "GuidanceResponse", "action", element.getAction().get(i), i);
  }

  protected void composeGuidanceResponseGuidanceResponseActionRelatedActionComponent(Complex parent, String parentType, String name, GuidanceResponse.GuidanceResponseActionRelatedActionComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "relatedAction", name, element, index);
    if (element.hasActionIdentifier())
      composeIdentifier(t, "GuidanceResponse", "actionIdentifier", element.getActionIdentifier(), -1);
    if (element.hasRelationshipElement())
      composeCode(t, "GuidanceResponse", "relationship", element.getRelationshipElement(), -1);
    if (element.hasOffset())
      composeType(t, "GuidanceResponse", "offset", element.getOffset(), -1);
    if (element.hasAnchorElement())
      composeCode(t, "GuidanceResponse", "anchor", element.getAnchorElement(), -1);
  }

  protected void composeGuidanceResponseGuidanceResponseActionBehaviorComponent(Complex parent, String parentType, String name, GuidanceResponse.GuidanceResponseActionBehaviorComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "behavior", name, element, index);
    if (element.hasType())
      composeCoding(t, "GuidanceResponse", "type", element.getType(), -1);
    if (element.hasValue())
      composeCoding(t, "GuidanceResponse", "value", element.getValue(), -1);
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
    if (element.hasProvidedBy())
      composeReference(t, "HealthcareService", "providedBy", element.getProvidedBy(), -1);
    if (element.hasServiceCategory())
      composeCodeableConcept(t, "HealthcareService", "serviceCategory", element.getServiceCategory(), -1);
    for (int i = 0; i < element.getServiceType().size(); i++)
      composeCodeableConcept(t, "HealthcareService", "serviceType", element.getServiceType().get(i), i);
    for (int i = 0; i < element.getSpecialty().size(); i++)
      composeCodeableConcept(t, "HealthcareService", "specialty", element.getSpecialty().get(i), i);
    for (int i = 0; i < element.getLocation().size(); i++)
      composeReference(t, "HealthcareService", "location", element.getLocation().get(i), i);
    if (element.hasServiceNameElement())
      composeString(t, "HealthcareService", "serviceName", element.getServiceNameElement(), -1);
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
    if (element.hasPublicKeyElement())
      composeString(t, "HealthcareService", "publicKey", element.getPublicKeyElement(), -1);
    if (element.hasAppointmentRequiredElement())
      composeBoolean(t, "HealthcareService", "appointmentRequired", element.getAppointmentRequiredElement(), -1);
    for (int i = 0; i < element.getAvailableTime().size(); i++)
      composeHealthcareServiceHealthcareServiceAvailableTimeComponent(t, "HealthcareService", "availableTime", element.getAvailableTime().get(i), i);
    for (int i = 0; i < element.getNotAvailable().size(); i++)
      composeHealthcareServiceHealthcareServiceNotAvailableComponent(t, "HealthcareService", "notAvailable", element.getNotAvailable().get(i), i);
    if (element.hasAvailabilityExceptionsElement())
      composeString(t, "HealthcareService", "availabilityExceptions", element.getAvailabilityExceptionsElement(), -1);
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

  protected void composeImagingExcerpt(Complex parent, String parentType, String name, ImagingExcerpt element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "ImagingExcerpt", name, element, index);
    if (element.hasUidElement())
      composeOid(t, "ImagingExcerpt", "uid", element.getUidElement(), -1);
    if (element.hasPatient())
      composeReference(t, "ImagingExcerpt", "patient", element.getPatient(), -1);
    if (element.hasAuthoringTimeElement())
      composeDateTime(t, "ImagingExcerpt", "authoringTime", element.getAuthoringTimeElement(), -1);
    if (element.hasAuthor())
      composeReference(t, "ImagingExcerpt", "author", element.getAuthor(), -1);
    if (element.hasTitle())
      composeCodeableConcept(t, "ImagingExcerpt", "title", element.getTitle(), -1);
    if (element.hasDescriptionElement())
      composeString(t, "ImagingExcerpt", "description", element.getDescriptionElement(), -1);
    for (int i = 0; i < element.getStudy().size(); i++)
      composeImagingExcerptStudyComponent(t, "ImagingExcerpt", "study", element.getStudy().get(i), i);
  }

  protected void composeImagingExcerptStudyComponent(Complex parent, String parentType, String name, ImagingExcerpt.StudyComponent element, int index) {
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
      composeOid(t, "ImagingExcerpt", "uid", element.getUidElement(), -1);
    if (element.hasImagingStudy())
      composeReference(t, "ImagingExcerpt", "imagingStudy", element.getImagingStudy(), -1);
    for (int i = 0; i < element.getDicom().size(); i++)
      composeImagingExcerptStudyDicomComponent(t, "ImagingExcerpt", "dicom", element.getDicom().get(i), i);
    for (int i = 0; i < element.getViewable().size(); i++)
      composeImagingExcerptStudyViewableComponent(t, "ImagingExcerpt", "viewable", element.getViewable().get(i), i);
    for (int i = 0; i < element.getSeries().size(); i++)
      composeImagingExcerptSeriesComponent(t, "ImagingExcerpt", "series", element.getSeries().get(i), i);
  }

  protected void composeImagingExcerptStudyDicomComponent(Complex parent, String parentType, String name, ImagingExcerpt.StudyDicomComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "dicom", name, element, index);
    if (element.hasTypeElement())
      composeEnum(t, "ImagingExcerpt", "type", element.getTypeElement(), -1);
    if (element.hasUrlElement())
      composeUri(t, "ImagingExcerpt", "url", element.getUrlElement(), -1);
  }

  protected void composeImagingExcerptStudyViewableComponent(Complex parent, String parentType, String name, ImagingExcerpt.StudyViewableComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "viewable", name, element, index);
    if (element.hasContentTypeElement())
      composeCode(t, "ImagingExcerpt", "contentType", element.getContentTypeElement(), -1);
    if (element.hasHeightElement())
      composePositiveInt(t, "ImagingExcerpt", "height", element.getHeightElement(), -1);
    if (element.hasWidthElement())
      composePositiveInt(t, "ImagingExcerpt", "width", element.getWidthElement(), -1);
    if (element.hasFramesElement())
      composePositiveInt(t, "ImagingExcerpt", "frames", element.getFramesElement(), -1);
    if (element.hasDurationElement())
      composeUnsignedInt(t, "ImagingExcerpt", "duration", element.getDurationElement(), -1);
    if (element.hasSizeElement())
      composeUnsignedInt(t, "ImagingExcerpt", "size", element.getSizeElement(), -1);
    if (element.hasTitleElement())
      composeString(t, "ImagingExcerpt", "title", element.getTitleElement(), -1);
    if (element.hasUrlElement())
      composeUri(t, "ImagingExcerpt", "url", element.getUrlElement(), -1);
  }

  protected void composeImagingExcerptSeriesComponent(Complex parent, String parentType, String name, ImagingExcerpt.SeriesComponent element, int index) {
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
      composeOid(t, "ImagingExcerpt", "uid", element.getUidElement(), -1);
    for (int i = 0; i < element.getDicom().size(); i++)
      composeImagingExcerptSeriesDicomComponent(t, "ImagingExcerpt", "dicom", element.getDicom().get(i), i);
    for (int i = 0; i < element.getInstance().size(); i++)
      composeImagingExcerptInstanceComponent(t, "ImagingExcerpt", "instance", element.getInstance().get(i), i);
  }

  protected void composeImagingExcerptSeriesDicomComponent(Complex parent, String parentType, String name, ImagingExcerpt.SeriesDicomComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "dicom", name, element, index);
    if (element.hasTypeElement())
      composeEnum(t, "ImagingExcerpt", "type", element.getTypeElement(), -1);
    if (element.hasUrlElement())
      composeUri(t, "ImagingExcerpt", "url", element.getUrlElement(), -1);
  }

  protected void composeImagingExcerptInstanceComponent(Complex parent, String parentType, String name, ImagingExcerpt.InstanceComponent element, int index) {
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
      composeOid(t, "ImagingExcerpt", "sopClass", element.getSopClassElement(), -1);
    if (element.hasUidElement())
      composeOid(t, "ImagingExcerpt", "uid", element.getUidElement(), -1);
    for (int i = 0; i < element.getDicom().size(); i++)
      composeImagingExcerptInstanceDicomComponent(t, "ImagingExcerpt", "dicom", element.getDicom().get(i), i);
    for (int i = 0; i < element.getFrameNumbers().size(); i++)
      composeUnsignedInt(t, "ImagingExcerpt", "frameNumbers", element.getFrameNumbers().get(i), i);
  }

  protected void composeImagingExcerptInstanceDicomComponent(Complex parent, String parentType, String name, ImagingExcerpt.InstanceDicomComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "dicom", name, element, index);
    if (element.hasTypeElement())
      composeEnum(t, "ImagingExcerpt", "type", element.getTypeElement(), -1);
    if (element.hasUrlElement())
      composeUri(t, "ImagingExcerpt", "url", element.getUrlElement(), -1);
  }

  protected void composeImagingObjectSelection(Complex parent, String parentType, String name, ImagingObjectSelection element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "ImagingObjectSelection", name, element, index);
    if (element.hasUidElement())
      composeOid(t, "ImagingObjectSelection", "uid", element.getUidElement(), -1);
    if (element.hasPatient())
      composeReference(t, "ImagingObjectSelection", "patient", element.getPatient(), -1);
    if (element.hasAuthoringTimeElement())
      composeDateTime(t, "ImagingObjectSelection", "authoringTime", element.getAuthoringTimeElement(), -1);
    if (element.hasAuthor())
      composeReference(t, "ImagingObjectSelection", "author", element.getAuthor(), -1);
    if (element.hasTitle())
      composeCodeableConcept(t, "ImagingObjectSelection", "title", element.getTitle(), -1);
    if (element.hasDescriptionElement())
      composeString(t, "ImagingObjectSelection", "description", element.getDescriptionElement(), -1);
    for (int i = 0; i < element.getStudy().size(); i++)
      composeImagingObjectSelectionStudyComponent(t, "ImagingObjectSelection", "study", element.getStudy().get(i), i);
  }

  protected void composeImagingObjectSelectionStudyComponent(Complex parent, String parentType, String name, ImagingObjectSelection.StudyComponent element, int index) {
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
      composeOid(t, "ImagingObjectSelection", "uid", element.getUidElement(), -1);
    if (element.hasUrlElement())
      composeUri(t, "ImagingObjectSelection", "url", element.getUrlElement(), -1);
    if (element.hasImagingStudy())
      composeReference(t, "ImagingObjectSelection", "imagingStudy", element.getImagingStudy(), -1);
    for (int i = 0; i < element.getSeries().size(); i++)
      composeImagingObjectSelectionSeriesComponent(t, "ImagingObjectSelection", "series", element.getSeries().get(i), i);
  }

  protected void composeImagingObjectSelectionSeriesComponent(Complex parent, String parentType, String name, ImagingObjectSelection.SeriesComponent element, int index) {
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
      composeOid(t, "ImagingObjectSelection", "uid", element.getUidElement(), -1);
    if (element.hasUrlElement())
      composeUri(t, "ImagingObjectSelection", "url", element.getUrlElement(), -1);
    for (int i = 0; i < element.getInstance().size(); i++)
      composeImagingObjectSelectionInstanceComponent(t, "ImagingObjectSelection", "instance", element.getInstance().get(i), i);
  }

  protected void composeImagingObjectSelectionInstanceComponent(Complex parent, String parentType, String name, ImagingObjectSelection.InstanceComponent element, int index) {
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
      composeOid(t, "ImagingObjectSelection", "sopClass", element.getSopClassElement(), -1);
    if (element.hasUidElement())
      composeOid(t, "ImagingObjectSelection", "uid", element.getUidElement(), -1);
    if (element.hasUrlElement())
      composeUri(t, "ImagingObjectSelection", "url", element.getUrlElement(), -1);
    for (int i = 0; i < element.getFrame().size(); i++)
      composeImagingObjectSelectionFramesComponent(t, "ImagingObjectSelection", "frame", element.getFrame().get(i), i);
  }

  protected void composeImagingObjectSelectionFramesComponent(Complex parent, String parentType, String name, ImagingObjectSelection.FramesComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "frame", name, element, index);
    for (int i = 0; i < element.getNumber().size(); i++)
      composeUnsignedInt(t, "ImagingObjectSelection", "number", element.getNumber().get(i), i);
    if (element.hasUrlElement())
      composeUri(t, "ImagingObjectSelection", "url", element.getUrlElement(), -1);
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
    if (element.hasStartedElement())
      composeDateTime(t, "ImagingStudy", "started", element.getStartedElement(), -1);
    for (int i = 0; i < element.getOrder().size(); i++)
      composeReference(t, "ImagingStudy", "order", element.getOrder().get(i), i);
    if (element.hasReferrer())
      composeReference(t, "ImagingStudy", "referrer", element.getReferrer(), -1);
    if (element.hasInterpreter())
      composeReference(t, "ImagingStudy", "interpreter", element.getInterpreter(), -1);
    if (element.hasUrlElement())
      composeUri(t, "ImagingStudy", "url", element.getUrlElement(), -1);
    if (element.hasNumberOfSeriesElement())
      composeUnsignedInt(t, "ImagingStudy", "numberOfSeries", element.getNumberOfSeriesElement(), -1);
    if (element.hasNumberOfInstancesElement())
      composeUnsignedInt(t, "ImagingStudy", "numberOfInstances", element.getNumberOfInstancesElement(), -1);
    for (int i = 0; i < element.getProcedure().size(); i++)
      composeReference(t, "ImagingStudy", "procedure", element.getProcedure().get(i), i);
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
    if (element.hasUrlElement())
      composeUri(t, "ImagingStudy", "url", element.getUrlElement(), -1);
    if (element.hasBodySite())
      composeCoding(t, "ImagingStudy", "bodySite", element.getBodySite(), -1);
    if (element.hasLaterality())
      composeCoding(t, "ImagingStudy", "laterality", element.getLaterality(), -1);
    if (element.hasStartedElement())
      composeDateTime(t, "ImagingStudy", "started", element.getStartedElement(), -1);
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
    if (element.hasTypeElement())
      composeString(t, "ImagingStudy", "type", element.getTypeElement(), -1);
    if (element.hasTitleElement())
      composeString(t, "ImagingStudy", "title", element.getTitleElement(), -1);
    for (int i = 0; i < element.getContent().size(); i++)
      composeAttachment(t, "ImagingStudy", "content", element.getContent().get(i), i);
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
      composeCode(t, "Immunization", "status", element.getStatusElement(), -1);
    if (element.hasDateElement())
      composeDateTime(t, "Immunization", "date", element.getDateElement(), -1);
    if (element.hasVaccineCode())
      composeCodeableConcept(t, "Immunization", "vaccineCode", element.getVaccineCode(), -1);
    if (element.hasPatient())
      composeReference(t, "Immunization", "patient", element.getPatient(), -1);
    if (element.hasWasNotGivenElement())
      composeBoolean(t, "Immunization", "wasNotGiven", element.getWasNotGivenElement(), -1);
    if (element.hasReportedElement())
      composeBoolean(t, "Immunization", "reported", element.getReportedElement(), -1);
    if (element.hasPerformer())
      composeReference(t, "Immunization", "performer", element.getPerformer(), -1);
    if (element.hasRequester())
      composeReference(t, "Immunization", "requester", element.getRequester(), -1);
    if (element.hasEncounter())
      composeReference(t, "Immunization", "encounter", element.getEncounter(), -1);
    if (element.hasManufacturer())
      composeReference(t, "Immunization", "manufacturer", element.getManufacturer(), -1);
    if (element.hasLocation())
      composeReference(t, "Immunization", "location", element.getLocation(), -1);
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
    for (int i = 0; i < element.getNote().size(); i++)
      composeAnnotation(t, "Immunization", "note", element.getNote().get(i), i);
    if (element.hasExplanation())
      composeImmunizationImmunizationExplanationComponent(t, "Immunization", "explanation", element.getExplanation(), -1);
    for (int i = 0; i < element.getReaction().size(); i++)
      composeImmunizationImmunizationReactionComponent(t, "Immunization", "reaction", element.getReaction().get(i), i);
    for (int i = 0; i < element.getVaccinationProtocol().size(); i++)
      composeImmunizationImmunizationVaccinationProtocolComponent(t, "Immunization", "vaccinationProtocol", element.getVaccinationProtocol().get(i), i);
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
      composeInteger(t, "ImmunizationRecommendation", "doseSequence", element.getDoseSequenceElement(), -1);
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
    if (element.hasPublisherElement())
      composeString(t, "ImplementationGuide", "publisher", element.getPublisherElement(), -1);
    for (int i = 0; i < element.getContact().size(); i++)
      composeImplementationGuideImplementationGuideContactComponent(t, "ImplementationGuide", "contact", element.getContact().get(i), i);
    if (element.hasDateElement())
      composeDateTime(t, "ImplementationGuide", "date", element.getDateElement(), -1);
    if (element.hasDescriptionElement())
      composeString(t, "ImplementationGuide", "description", element.getDescriptionElement(), -1);
    for (int i = 0; i < element.getUseContext().size(); i++)
      composeCodeableConcept(t, "ImplementationGuide", "useContext", element.getUseContext().get(i), i);
    if (element.hasCopyrightElement())
      composeString(t, "ImplementationGuide", "copyright", element.getCopyrightElement(), -1);
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

  protected void composeImplementationGuideImplementationGuideContactComponent(Complex parent, String parentType, String name, ImplementationGuide.ImplementationGuideContactComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "contact", name, element, index);
    if (element.hasNameElement())
      composeString(t, "ImplementationGuide", "name", element.getNameElement(), -1);
    for (int i = 0; i < element.getTelecom().size(); i++)
      composeContactPoint(t, "ImplementationGuide", "telecom", element.getTelecom().get(i), i);
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
    if (element.hasNameElement())
      composeString(t, "ImplementationGuide", "name", element.getNameElement(), -1);
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
    if (element.hasModuleMetadata())
      composeModuleMetadata(t, "Library", "moduleMetadata", element.getModuleMetadata(), -1);
    for (int i = 0; i < element.getModel().size(); i++)
      composeLibraryLibraryModelComponent(t, "Library", "model", element.getModel().get(i), i);
    for (int i = 0; i < element.getLibrary().size(); i++)
      composeLibraryLibraryLibraryComponent(t, "Library", "library", element.getLibrary().get(i), i);
    for (int i = 0; i < element.getCodeSystem().size(); i++)
      composeLibraryLibraryCodeSystemComponent(t, "Library", "codeSystem", element.getCodeSystem().get(i), i);
    for (int i = 0; i < element.getValueSet().size(); i++)
      composeLibraryLibraryValueSetComponent(t, "Library", "valueSet", element.getValueSet().get(i), i);
    for (int i = 0; i < element.getParameter().size(); i++)
      composeParameterDefinition(t, "Library", "parameter", element.getParameter().get(i), i);
    for (int i = 0; i < element.getDataRequirement().size(); i++)
      composeDataRequirement(t, "Library", "dataRequirement", element.getDataRequirement().get(i), i);
    if (element.hasDocument())
      composeAttachment(t, "Library", "document", element.getDocument(), -1);
  }

  protected void composeLibraryLibraryModelComponent(Complex parent, String parentType, String name, Library.LibraryModelComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "model", name, element, index);
    if (element.hasNameElement())
      composeString(t, "Library", "name", element.getNameElement(), -1);
    if (element.hasIdentifierElement())
      composeString(t, "Library", "identifier", element.getIdentifierElement(), -1);
    if (element.hasVersionElement())
      composeString(t, "Library", "version", element.getVersionElement(), -1);
  }

  protected void composeLibraryLibraryLibraryComponent(Complex parent, String parentType, String name, Library.LibraryLibraryComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "library", name, element, index);
    if (element.hasNameElement())
      composeString(t, "Library", "name", element.getNameElement(), -1);
    if (element.hasIdentifierElement())
      composeString(t, "Library", "identifier", element.getIdentifierElement(), -1);
    if (element.hasVersionElement())
      composeString(t, "Library", "version", element.getVersionElement(), -1);
    if (element.hasDocument())
      composeType(t, "Library", "document", element.getDocument(), -1);
  }

  protected void composeLibraryLibraryCodeSystemComponent(Complex parent, String parentType, String name, Library.LibraryCodeSystemComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "codeSystem", name, element, index);
    if (element.hasNameElement())
      composeString(t, "Library", "name", element.getNameElement(), -1);
    if (element.hasIdentifierElement())
      composeString(t, "Library", "identifier", element.getIdentifierElement(), -1);
    if (element.hasVersionElement())
      composeString(t, "Library", "version", element.getVersionElement(), -1);
  }

  protected void composeLibraryLibraryValueSetComponent(Complex parent, String parentType, String name, Library.LibraryValueSetComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "valueSet", name, element, index);
    if (element.hasNameElement())
      composeString(t, "Library", "name", element.getNameElement(), -1);
    if (element.hasIdentifierElement())
      composeString(t, "Library", "identifier", element.getIdentifierElement(), -1);
    if (element.hasVersionElement())
      composeString(t, "Library", "version", element.getVersionElement(), -1);
    for (int i = 0; i < element.getCodeSystem().size(); i++)
      composeString(t, "Library", "codeSystem", element.getCodeSystem().get(i), i);
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
    if (element.hasNameElement())
      composeString(t, "Location", "name", element.getNameElement(), -1);
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
    if (element.hasModuleMetadata())
      composeModuleMetadata(t, "Measure", "moduleMetadata", element.getModuleMetadata(), -1);
    for (int i = 0; i < element.getLibrary().size(); i++)
      composeReference(t, "Measure", "library", element.getLibrary().get(i), i);
    if (element.hasDisclaimerElement())
      composeMarkdown(t, "Measure", "disclaimer", element.getDisclaimerElement(), -1);
    if (element.hasScoringElement())
      composeEnum(t, "Measure", "scoring", element.getScoringElement(), -1);
    for (int i = 0; i < element.getType().size(); i++)
      composeEnum(t, "Measure", "type", element.getType().get(i), i);
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
    if (element.hasDefinitionElement())
      composeMarkdown(t, "Measure", "definition", element.getDefinitionElement(), -1);
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
    if (element.hasTypeElement())
      composeEnum(t, "Measure", "type", element.getTypeElement(), -1);
    if (element.hasIdentifier())
      composeIdentifier(t, "Measure", "identifier", element.getIdentifier(), -1);
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
      composeEnum(t, "Measure", "usage", element.getUsage().get(i), i);
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
    if (element.hasMeasure())
      composeReference(t, "MeasureReport", "measure", element.getMeasure(), -1);
    if (element.hasTypeElement())
      composeEnum(t, "MeasureReport", "type", element.getTypeElement(), -1);
    if (element.hasPatient())
      composeReference(t, "MeasureReport", "patient", element.getPatient(), -1);
    if (element.hasPeriod())
      composePeriod(t, "MeasureReport", "period", element.getPeriod(), -1);
    if (element.hasStatusElement())
      composeEnum(t, "MeasureReport", "status", element.getStatusElement(), -1);
    if (element.hasDateElement())
      composeDateTime(t, "MeasureReport", "date", element.getDateElement(), -1);
    if (element.hasReportingOrganization())
      composeReference(t, "MeasureReport", "reportingOrganization", element.getReportingOrganization(), -1);
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
    for (int i = 0; i < element.getSupplementalData().size(); i++)
      composeMeasureReportMeasureReportGroupSupplementalDataComponent(t, "MeasureReport", "supplementalData", element.getSupplementalData().get(i), i);
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
    if (element.hasTypeElement())
      composeCode(t, "MeasureReport", "type", element.getTypeElement(), -1);
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
    for (int i = 0; i < element.getGroup().size(); i++)
      composeMeasureReportMeasureReportGroupStratifierGroupComponent(t, "MeasureReport", "group", element.getGroup().get(i), i);
  }

  protected void composeMeasureReportMeasureReportGroupStratifierGroupComponent(Complex parent, String parentType, String name, MeasureReport.MeasureReportGroupStratifierGroupComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "group", name, element, index);
    if (element.hasValueElement())
      composeString(t, "MeasureReport", "value", element.getValueElement(), -1);
    for (int i = 0; i < element.getPopulation().size(); i++)
      composeMeasureReportMeasureReportGroupStratifierGroupPopulationComponent(t, "MeasureReport", "population", element.getPopulation().get(i), i);
    if (element.hasMeasureScoreElement())
      composeDecimal(t, "MeasureReport", "measureScore", element.getMeasureScoreElement(), -1);
  }

  protected void composeMeasureReportMeasureReportGroupStratifierGroupPopulationComponent(Complex parent, String parentType, String name, MeasureReport.MeasureReportGroupStratifierGroupPopulationComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "population", name, element, index);
    if (element.hasTypeElement())
      composeCode(t, "MeasureReport", "type", element.getTypeElement(), -1);
    if (element.hasCountElement())
      composeInteger(t, "MeasureReport", "count", element.getCountElement(), -1);
    if (element.hasPatients())
      composeReference(t, "MeasureReport", "patients", element.getPatients(), -1);
  }

  protected void composeMeasureReportMeasureReportGroupSupplementalDataComponent(Complex parent, String parentType, String name, MeasureReport.MeasureReportGroupSupplementalDataComponent element, int index) {
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
      composeIdentifier(t, "MeasureReport", "identifier", element.getIdentifier(), -1);
    for (int i = 0; i < element.getGroup().size(); i++)
      composeMeasureReportMeasureReportGroupSupplementalDataGroupComponent(t, "MeasureReport", "group", element.getGroup().get(i), i);
  }

  protected void composeMeasureReportMeasureReportGroupSupplementalDataGroupComponent(Complex parent, String parentType, String name, MeasureReport.MeasureReportGroupSupplementalDataGroupComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "group", name, element, index);
    if (element.hasValueElement())
      composeString(t, "MeasureReport", "value", element.getValueElement(), -1);
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
    if (element.hasTypeElement())
      composeEnum(t, "Media", "type", element.getTypeElement(), -1);
    if (element.hasSubtype())
      composeCodeableConcept(t, "Media", "subtype", element.getSubtype(), -1);
    if (element.hasView())
      composeCodeableConcept(t, "Media", "view", element.getView(), -1);
    if (element.hasSubject())
      composeReference(t, "Media", "subject", element.getSubject(), -1);
    if (element.hasOperator())
      composeReference(t, "Media", "operator", element.getOperator(), -1);
    if (element.hasDeviceNameElement())
      composeString(t, "Media", "deviceName", element.getDeviceNameElement(), -1);
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
    if (element.hasIsBrandElement())
      composeBoolean(t, "Medication", "isBrand", element.getIsBrandElement(), -1);
    if (element.hasManufacturer())
      composeReference(t, "Medication", "manufacturer", element.getManufacturer(), -1);
    if (element.hasProduct())
      composeMedicationMedicationProductComponent(t, "Medication", "product", element.getProduct(), -1);
    if (element.hasPackage())
      composeMedicationMedicationPackageComponent(t, "Medication", "package", element.getPackage(), -1);
  }

  protected void composeMedicationMedicationProductComponent(Complex parent, String parentType, String name, Medication.MedicationProductComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "product", name, element, index);
    if (element.hasForm())
      composeCodeableConcept(t, "Medication", "form", element.getForm(), -1);
    for (int i = 0; i < element.getIngredient().size(); i++)
      composeMedicationMedicationProductIngredientComponent(t, "Medication", "ingredient", element.getIngredient().get(i), i);
    for (int i = 0; i < element.getBatch().size(); i++)
      composeMedicationMedicationProductBatchComponent(t, "Medication", "batch", element.getBatch().get(i), i);
  }

  protected void composeMedicationMedicationProductIngredientComponent(Complex parent, String parentType, String name, Medication.MedicationProductIngredientComponent element, int index) {
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
    if (element.hasAmount())
      composeRatio(t, "Medication", "amount", element.getAmount(), -1);
  }

  protected void composeMedicationMedicationProductBatchComponent(Complex parent, String parentType, String name, Medication.MedicationProductBatchComponent element, int index) {
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
    if (element.hasStatusElement())
      composeEnum(t, "MedicationAdministration", "status", element.getStatusElement(), -1);
    if (element.hasMedication())
      composeType(t, "MedicationAdministration", "medication", element.getMedication(), -1);
    if (element.hasPatient())
      composeReference(t, "MedicationAdministration", "patient", element.getPatient(), -1);
    if (element.hasEncounter())
      composeReference(t, "MedicationAdministration", "encounter", element.getEncounter(), -1);
    if (element.hasEffectiveTime())
      composeType(t, "MedicationAdministration", "effectiveTime", element.getEffectiveTime(), -1);
    if (element.hasPractitioner())
      composeReference(t, "MedicationAdministration", "practitioner", element.getPractitioner(), -1);
    if (element.hasPrescription())
      composeReference(t, "MedicationAdministration", "prescription", element.getPrescription(), -1);
    if (element.hasWasNotGivenElement())
      composeBoolean(t, "MedicationAdministration", "wasNotGiven", element.getWasNotGivenElement(), -1);
    for (int i = 0; i < element.getReasonNotGiven().size(); i++)
      composeCodeableConcept(t, "MedicationAdministration", "reasonNotGiven", element.getReasonNotGiven().get(i), i);
    for (int i = 0; i < element.getReasonGiven().size(); i++)
      composeCodeableConcept(t, "MedicationAdministration", "reasonGiven", element.getReasonGiven().get(i), i);
    for (int i = 0; i < element.getDevice().size(); i++)
      composeReference(t, "MedicationAdministration", "device", element.getDevice().get(i), i);
    for (int i = 0; i < element.getNote().size(); i++)
      composeAnnotation(t, "MedicationAdministration", "note", element.getNote().get(i), i);
    if (element.hasDosage())
      composeMedicationAdministrationMedicationAdministrationDosageComponent(t, "MedicationAdministration", "dosage", element.getDosage(), -1);
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
      composeType(t, "MedicationAdministration", "site", element.getSite(), -1);
    if (element.hasRoute())
      composeCodeableConcept(t, "MedicationAdministration", "route", element.getRoute(), -1);
    if (element.hasMethod())
      composeCodeableConcept(t, "MedicationAdministration", "method", element.getMethod(), -1);
    if (element.hasQuantity())
      composeQuantity(t, "MedicationAdministration", "quantity", element.getQuantity(), -1);
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
    if (element.hasIdentifier())
      composeIdentifier(t, "MedicationDispense", "identifier", element.getIdentifier(), -1);
    if (element.hasStatusElement())
      composeEnum(t, "MedicationDispense", "status", element.getStatusElement(), -1);
    if (element.hasMedication())
      composeType(t, "MedicationDispense", "medication", element.getMedication(), -1);
    if (element.hasPatient())
      composeReference(t, "MedicationDispense", "patient", element.getPatient(), -1);
    if (element.hasDispenser())
      composeReference(t, "MedicationDispense", "dispenser", element.getDispenser(), -1);
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
      composeMedicationDispenseMedicationDispenseDosageInstructionComponent(t, "MedicationDispense", "dosageInstruction", element.getDosageInstruction().get(i), i);
    if (element.hasSubstitution())
      composeMedicationDispenseMedicationDispenseSubstitutionComponent(t, "MedicationDispense", "substitution", element.getSubstitution(), -1);
  }

  protected void composeMedicationDispenseMedicationDispenseDosageInstructionComponent(Complex parent, String parentType, String name, MedicationDispense.MedicationDispenseDosageInstructionComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "dosageInstruction", name, element, index);
    if (element.hasTextElement())
      composeString(t, "MedicationDispense", "text", element.getTextElement(), -1);
    if (element.hasAdditionalInstructions())
      composeCodeableConcept(t, "MedicationDispense", "additionalInstructions", element.getAdditionalInstructions(), -1);
    if (element.hasTiming())
      composeTiming(t, "MedicationDispense", "timing", element.getTiming(), -1);
    if (element.hasAsNeeded())
      composeType(t, "MedicationDispense", "asNeeded", element.getAsNeeded(), -1);
    if (element.hasSite())
      composeType(t, "MedicationDispense", "site", element.getSite(), -1);
    if (element.hasRoute())
      composeCodeableConcept(t, "MedicationDispense", "route", element.getRoute(), -1);
    if (element.hasMethod())
      composeCodeableConcept(t, "MedicationDispense", "method", element.getMethod(), -1);
    if (element.hasDose())
      composeType(t, "MedicationDispense", "dose", element.getDose(), -1);
    if (element.hasRate())
      composeType(t, "MedicationDispense", "rate", element.getRate(), -1);
    if (element.hasMaxDosePerPeriod())
      composeRatio(t, "MedicationDispense", "maxDosePerPeriod", element.getMaxDosePerPeriod(), -1);
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
    if (element.hasType())
      composeCodeableConcept(t, "MedicationDispense", "type", element.getType(), -1);
    for (int i = 0; i < element.getReason().size(); i++)
      composeCodeableConcept(t, "MedicationDispense", "reason", element.getReason().get(i), i);
    for (int i = 0; i < element.getResponsibleParty().size(); i++)
      composeReference(t, "MedicationDispense", "responsibleParty", element.getResponsibleParty().get(i), i);
  }

  protected void composeMedicationOrder(Complex parent, String parentType, String name, MedicationOrder element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "MedicationOrder", name, element, index);
    for (int i = 0; i < element.getIdentifier().size(); i++)
      composeIdentifier(t, "MedicationOrder", "identifier", element.getIdentifier().get(i), i);
    if (element.hasStatusElement())
      composeEnum(t, "MedicationOrder", "status", element.getStatusElement(), -1);
    if (element.hasMedication())
      composeType(t, "MedicationOrder", "medication", element.getMedication(), -1);
    if (element.hasPatient())
      composeReference(t, "MedicationOrder", "patient", element.getPatient(), -1);
    if (element.hasEncounter())
      composeReference(t, "MedicationOrder", "encounter", element.getEncounter(), -1);
    if (element.hasDateWrittenElement())
      composeDateTime(t, "MedicationOrder", "dateWritten", element.getDateWrittenElement(), -1);
    if (element.hasPrescriber())
      composeReference(t, "MedicationOrder", "prescriber", element.getPrescriber(), -1);
    for (int i = 0; i < element.getReasonCode().size(); i++)
      composeCodeableConcept(t, "MedicationOrder", "reasonCode", element.getReasonCode().get(i), i);
    for (int i = 0; i < element.getReasonReference().size(); i++)
      composeReference(t, "MedicationOrder", "reasonReference", element.getReasonReference().get(i), i);
    if (element.hasDateEndedElement())
      composeDateTime(t, "MedicationOrder", "dateEnded", element.getDateEndedElement(), -1);
    if (element.hasReasonEnded())
      composeCodeableConcept(t, "MedicationOrder", "reasonEnded", element.getReasonEnded(), -1);
    for (int i = 0; i < element.getNote().size(); i++)
      composeAnnotation(t, "MedicationOrder", "note", element.getNote().get(i), i);
    for (int i = 0; i < element.getDosageInstruction().size(); i++)
      composeMedicationOrderMedicationOrderDosageInstructionComponent(t, "MedicationOrder", "dosageInstruction", element.getDosageInstruction().get(i), i);
    if (element.hasDispenseRequest())
      composeMedicationOrderMedicationOrderDispenseRequestComponent(t, "MedicationOrder", "dispenseRequest", element.getDispenseRequest(), -1);
    if (element.hasSubstitution())
      composeMedicationOrderMedicationOrderSubstitutionComponent(t, "MedicationOrder", "substitution", element.getSubstitution(), -1);
    if (element.hasPriorPrescription())
      composeReference(t, "MedicationOrder", "priorPrescription", element.getPriorPrescription(), -1);
  }

  protected void composeMedicationOrderMedicationOrderDosageInstructionComponent(Complex parent, String parentType, String name, MedicationOrder.MedicationOrderDosageInstructionComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "dosageInstruction", name, element, index);
    if (element.hasTextElement())
      composeString(t, "MedicationOrder", "text", element.getTextElement(), -1);
    if (element.hasAdditionalInstructions())
      composeCodeableConcept(t, "MedicationOrder", "additionalInstructions", element.getAdditionalInstructions(), -1);
    if (element.hasTiming())
      composeTiming(t, "MedicationOrder", "timing", element.getTiming(), -1);
    if (element.hasAsNeeded())
      composeType(t, "MedicationOrder", "asNeeded", element.getAsNeeded(), -1);
    if (element.hasSite())
      composeType(t, "MedicationOrder", "site", element.getSite(), -1);
    if (element.hasRoute())
      composeCodeableConcept(t, "MedicationOrder", "route", element.getRoute(), -1);
    if (element.hasMethod())
      composeCodeableConcept(t, "MedicationOrder", "method", element.getMethod(), -1);
    if (element.hasDose())
      composeType(t, "MedicationOrder", "dose", element.getDose(), -1);
    if (element.hasRate())
      composeType(t, "MedicationOrder", "rate", element.getRate(), -1);
    if (element.hasMaxDosePerPeriod())
      composeRatio(t, "MedicationOrder", "maxDosePerPeriod", element.getMaxDosePerPeriod(), -1);
  }

  protected void composeMedicationOrderMedicationOrderDispenseRequestComponent(Complex parent, String parentType, String name, MedicationOrder.MedicationOrderDispenseRequestComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "dispenseRequest", name, element, index);
    if (element.hasMedication())
      composeType(t, "MedicationOrder", "medication", element.getMedication(), -1);
    if (element.hasValidityPeriod())
      composePeriod(t, "MedicationOrder", "validityPeriod", element.getValidityPeriod(), -1);
    if (element.hasNumberOfRepeatsAllowedElement())
      composePositiveInt(t, "MedicationOrder", "numberOfRepeatsAllowed", element.getNumberOfRepeatsAllowedElement(), -1);
    if (element.hasQuantity())
      composeQuantity(t, "MedicationOrder", "quantity", element.getQuantity(), -1);
    if (element.hasExpectedSupplyDuration())
      composeQuantity(t, "MedicationOrder", "expectedSupplyDuration", element.getExpectedSupplyDuration(), -1);
  }

  protected void composeMedicationOrderMedicationOrderSubstitutionComponent(Complex parent, String parentType, String name, MedicationOrder.MedicationOrderSubstitutionComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "substitution", name, element, index);
    if (element.hasType())
      composeCodeableConcept(t, "MedicationOrder", "type", element.getType(), -1);
    if (element.hasReason())
      composeCodeableConcept(t, "MedicationOrder", "reason", element.getReason(), -1);
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
    if (element.hasStatusElement())
      composeEnum(t, "MedicationStatement", "status", element.getStatusElement(), -1);
    if (element.hasMedication())
      composeType(t, "MedicationStatement", "medication", element.getMedication(), -1);
    if (element.hasPatient())
      composeReference(t, "MedicationStatement", "patient", element.getPatient(), -1);
    if (element.hasEffective())
      composeType(t, "MedicationStatement", "effective", element.getEffective(), -1);
    if (element.hasInformationSource())
      composeReference(t, "MedicationStatement", "informationSource", element.getInformationSource(), -1);
    for (int i = 0; i < element.getSupportingInformation().size(); i++)
      composeReference(t, "MedicationStatement", "supportingInformation", element.getSupportingInformation().get(i), i);
    if (element.hasDateAssertedElement())
      composeDateTime(t, "MedicationStatement", "dateAsserted", element.getDateAssertedElement(), -1);
    if (element.hasWasNotTakenElement())
      composeBoolean(t, "MedicationStatement", "wasNotTaken", element.getWasNotTakenElement(), -1);
    for (int i = 0; i < element.getReasonNotTaken().size(); i++)
      composeCodeableConcept(t, "MedicationStatement", "reasonNotTaken", element.getReasonNotTaken().get(i), i);
    if (element.hasReasonForUse())
      composeType(t, "MedicationStatement", "reasonForUse", element.getReasonForUse(), -1);
    for (int i = 0; i < element.getNote().size(); i++)
      composeAnnotation(t, "MedicationStatement", "note", element.getNote().get(i), i);
    for (int i = 0; i < element.getDosage().size(); i++)
      composeMedicationStatementMedicationStatementDosageComponent(t, "MedicationStatement", "dosage", element.getDosage().get(i), i);
  }

  protected void composeMedicationStatementMedicationStatementDosageComponent(Complex parent, String parentType, String name, MedicationStatement.MedicationStatementDosageComponent element, int index) {
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
      composeString(t, "MedicationStatement", "text", element.getTextElement(), -1);
    if (element.hasTiming())
      composeTiming(t, "MedicationStatement", "timing", element.getTiming(), -1);
    if (element.hasAsNeeded())
      composeType(t, "MedicationStatement", "asNeeded", element.getAsNeeded(), -1);
    if (element.hasSite())
      composeType(t, "MedicationStatement", "site", element.getSite(), -1);
    if (element.hasRoute())
      composeCodeableConcept(t, "MedicationStatement", "route", element.getRoute(), -1);
    if (element.hasMethod())
      composeCodeableConcept(t, "MedicationStatement", "method", element.getMethod(), -1);
    if (element.hasQuantity())
      composeType(t, "MedicationStatement", "quantity", element.getQuantity(), -1);
    if (element.hasRate())
      composeType(t, "MedicationStatement", "rate", element.getRate(), -1);
    if (element.hasMaxDosePerPeriod())
      composeRatio(t, "MedicationStatement", "maxDosePerPeriod", element.getMaxDosePerPeriod(), -1);
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
    if (element.hasTimestampElement())
      composeInstant(t, "MessageHeader", "timestamp", element.getTimestampElement(), -1);
    if (element.hasEvent())
      composeCoding(t, "MessageHeader", "event", element.getEvent(), -1);
    if (element.hasResponse())
      composeMessageHeaderMessageHeaderResponseComponent(t, "MessageHeader", "response", element.getResponse(), -1);
    if (element.hasSource())
      composeMessageHeaderMessageSourceComponent(t, "MessageHeader", "source", element.getSource(), -1);
    for (int i = 0; i < element.getDestination().size(); i++)
      composeMessageHeaderMessageDestinationComponent(t, "MessageHeader", "destination", element.getDestination().get(i), i);
    if (element.hasEnterer())
      composeReference(t, "MessageHeader", "enterer", element.getEnterer(), -1);
    if (element.hasAuthor())
      composeReference(t, "MessageHeader", "author", element.getAuthor(), -1);
    if (element.hasReceiver())
      composeReference(t, "MessageHeader", "receiver", element.getReceiver(), -1);
    if (element.hasResponsible())
      composeReference(t, "MessageHeader", "responsible", element.getResponsible(), -1);
    if (element.hasReason())
      composeCodeableConcept(t, "MessageHeader", "reason", element.getReason(), -1);
    for (int i = 0; i < element.getData().size(); i++)
      composeReference(t, "MessageHeader", "data", element.getData().get(i), i);
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
  }

  protected void composeModuleDefinition(Complex parent, String parentType, String name, ModuleDefinition element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "ModuleDefinition", name, element, index);
    for (int i = 0; i < element.getIdentifier().size(); i++)
      composeIdentifier(t, "ModuleDefinition", "identifier", element.getIdentifier().get(i), i);
    if (element.hasVersionElement())
      composeString(t, "ModuleDefinition", "version", element.getVersionElement(), -1);
    for (int i = 0; i < element.getModel().size(); i++)
      composeModuleDefinitionModuleDefinitionModelComponent(t, "ModuleDefinition", "model", element.getModel().get(i), i);
    for (int i = 0; i < element.getLibrary().size(); i++)
      composeModuleDefinitionModuleDefinitionLibraryComponent(t, "ModuleDefinition", "library", element.getLibrary().get(i), i);
    for (int i = 0; i < element.getCodeSystem().size(); i++)
      composeModuleDefinitionModuleDefinitionCodeSystemComponent(t, "ModuleDefinition", "codeSystem", element.getCodeSystem().get(i), i);
    for (int i = 0; i < element.getValueSet().size(); i++)
      composeModuleDefinitionModuleDefinitionValueSetComponent(t, "ModuleDefinition", "valueSet", element.getValueSet().get(i), i);
    for (int i = 0; i < element.getParameter().size(); i++)
      composeModuleDefinitionModuleDefinitionParameterComponent(t, "ModuleDefinition", "parameter", element.getParameter().get(i), i);
    for (int i = 0; i < element.getData().size(); i++)
      composeModuleDefinitionModuleDefinitionDataComponent(t, "ModuleDefinition", "data", element.getData().get(i), i);
  }

  protected void composeModuleDefinitionModuleDefinitionModelComponent(Complex parent, String parentType, String name, ModuleDefinition.ModuleDefinitionModelComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "model", name, element, index);
    if (element.hasNameElement())
      composeString(t, "ModuleDefinition", "name", element.getNameElement(), -1);
    if (element.hasIdentifierElement())
      composeString(t, "ModuleDefinition", "identifier", element.getIdentifierElement(), -1);
    if (element.hasVersionElement())
      composeString(t, "ModuleDefinition", "version", element.getVersionElement(), -1);
  }

  protected void composeModuleDefinitionModuleDefinitionLibraryComponent(Complex parent, String parentType, String name, ModuleDefinition.ModuleDefinitionLibraryComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "library", name, element, index);
    if (element.hasNameElement())
      composeString(t, "ModuleDefinition", "name", element.getNameElement(), -1);
    if (element.hasIdentifierElement())
      composeString(t, "ModuleDefinition", "identifier", element.getIdentifierElement(), -1);
    if (element.hasVersionElement())
      composeString(t, "ModuleDefinition", "version", element.getVersionElement(), -1);
    if (element.hasDocument())
      composeType(t, "ModuleDefinition", "document", element.getDocument(), -1);
  }

  protected void composeModuleDefinitionModuleDefinitionCodeSystemComponent(Complex parent, String parentType, String name, ModuleDefinition.ModuleDefinitionCodeSystemComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "codeSystem", name, element, index);
    if (element.hasNameElement())
      composeString(t, "ModuleDefinition", "name", element.getNameElement(), -1);
    if (element.hasIdentifierElement())
      composeString(t, "ModuleDefinition", "identifier", element.getIdentifierElement(), -1);
    if (element.hasVersionElement())
      composeString(t, "ModuleDefinition", "version", element.getVersionElement(), -1);
  }

  protected void composeModuleDefinitionModuleDefinitionValueSetComponent(Complex parent, String parentType, String name, ModuleDefinition.ModuleDefinitionValueSetComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "valueSet", name, element, index);
    if (element.hasNameElement())
      composeString(t, "ModuleDefinition", "name", element.getNameElement(), -1);
    if (element.hasIdentifierElement())
      composeString(t, "ModuleDefinition", "identifier", element.getIdentifierElement(), -1);
    if (element.hasVersionElement())
      composeString(t, "ModuleDefinition", "version", element.getVersionElement(), -1);
    for (int i = 0; i < element.getCodeSystem().size(); i++)
      composeString(t, "ModuleDefinition", "codeSystem", element.getCodeSystem().get(i), i);
  }

  protected void composeModuleDefinitionModuleDefinitionParameterComponent(Complex parent, String parentType, String name, ModuleDefinition.ModuleDefinitionParameterComponent element, int index) {
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
      composeCode(t, "ModuleDefinition", "name", element.getNameElement(), -1);
    if (element.hasUseElement())
      composeCode(t, "ModuleDefinition", "use", element.getUseElement(), -1);
    if (element.hasDocumentationElement())
      composeString(t, "ModuleDefinition", "documentation", element.getDocumentationElement(), -1);
    if (element.hasTypeElement())
      composeCode(t, "ModuleDefinition", "type", element.getTypeElement(), -1);
    if (element.hasProfile())
      composeReference(t, "ModuleDefinition", "profile", element.getProfile(), -1);
  }

  protected void composeModuleDefinitionModuleDefinitionDataComponent(Complex parent, String parentType, String name, ModuleDefinition.ModuleDefinitionDataComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "data", name, element, index);
    if (element.hasTypeElement())
      composeCode(t, "ModuleDefinition", "type", element.getTypeElement(), -1);
    if (element.hasProfile())
      composeReference(t, "ModuleDefinition", "profile", element.getProfile(), -1);
    for (int i = 0; i < element.getMustSupport().size(); i++)
      composeString(t, "ModuleDefinition", "mustSupport", element.getMustSupport().get(i), i);
    for (int i = 0; i < element.getCodeFilter().size(); i++)
      composeModuleDefinitionModuleDefinitionDataCodeFilterComponent(t, "ModuleDefinition", "codeFilter", element.getCodeFilter().get(i), i);
    for (int i = 0; i < element.getDateFilter().size(); i++)
      composeModuleDefinitionModuleDefinitionDataDateFilterComponent(t, "ModuleDefinition", "dateFilter", element.getDateFilter().get(i), i);
  }

  protected void composeModuleDefinitionModuleDefinitionDataCodeFilterComponent(Complex parent, String parentType, String name, ModuleDefinition.ModuleDefinitionDataCodeFilterComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "codeFilter", name, element, index);
    if (element.hasPathElement())
      composeString(t, "ModuleDefinition", "path", element.getPathElement(), -1);
    if (element.hasValueSet())
      composeType(t, "ModuleDefinition", "valueSet", element.getValueSet(), -1);
    for (int i = 0; i < element.getCodeableConcept().size(); i++)
      composeCodeableConcept(t, "ModuleDefinition", "codeableConcept", element.getCodeableConcept().get(i), i);
  }

  protected void composeModuleDefinitionModuleDefinitionDataDateFilterComponent(Complex parent, String parentType, String name, ModuleDefinition.ModuleDefinitionDataDateFilterComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "dateFilter", name, element, index);
    if (element.hasPathElement())
      composeString(t, "ModuleDefinition", "path", element.getPathElement(), -1);
    if (element.hasValue())
      composeType(t, "ModuleDefinition", "value", element.getValue(), -1);
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
      composeNamingSystemNamingSystemContactComponent(t, "NamingSystem", "contact", element.getContact().get(i), i);
    if (element.hasResponsibleElement())
      composeString(t, "NamingSystem", "responsible", element.getResponsibleElement(), -1);
    if (element.hasType())
      composeCodeableConcept(t, "NamingSystem", "type", element.getType(), -1);
    if (element.hasDescriptionElement())
      composeString(t, "NamingSystem", "description", element.getDescriptionElement(), -1);
    for (int i = 0; i < element.getUseContext().size(); i++)
      composeCodeableConcept(t, "NamingSystem", "useContext", element.getUseContext().get(i), i);
    if (element.hasUsageElement())
      composeString(t, "NamingSystem", "usage", element.getUsageElement(), -1);
    for (int i = 0; i < element.getUniqueId().size(); i++)
      composeNamingSystemNamingSystemUniqueIdComponent(t, "NamingSystem", "uniqueId", element.getUniqueId().get(i), i);
    if (element.hasReplacedBy())
      composeReference(t, "NamingSystem", "replacedBy", element.getReplacedBy(), -1);
  }

  protected void composeNamingSystemNamingSystemContactComponent(Complex parent, String parentType, String name, NamingSystem.NamingSystemContactComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "contact", name, element, index);
    if (element.hasNameElement())
      composeString(t, "NamingSystem", "name", element.getNameElement(), -1);
    for (int i = 0; i < element.getTelecom().size(); i++)
      composeContactPoint(t, "NamingSystem", "telecom", element.getTelecom().get(i), i);
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
    if (element.hasStatusElement())
      composeEnum(t, "Observation", "status", element.getStatusElement(), -1);
    if (element.hasCategory())
      composeCodeableConcept(t, "Observation", "category", element.getCategory(), -1);
    if (element.hasCode())
      composeCodeableConcept(t, "Observation", "code", element.getCode(), -1);
    if (element.hasSubject())
      composeReference(t, "Observation", "subject", element.getSubject(), -1);
    if (element.hasEncounter())
      composeReference(t, "Observation", "encounter", element.getEncounter(), -1);
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
    if (element.hasMeaning())
      composeCodeableConcept(t, "Observation", "meaning", element.getMeaning(), -1);
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
      composeOperationDefinitionOperationDefinitionContactComponent(t, "OperationDefinition", "contact", element.getContact().get(i), i);
    if (element.hasDescriptionElement())
      composeString(t, "OperationDefinition", "description", element.getDescriptionElement(), -1);
    for (int i = 0; i < element.getUseContext().size(); i++)
      composeCodeableConcept(t, "OperationDefinition", "useContext", element.getUseContext().get(i), i);
    if (element.hasRequirementsElement())
      composeString(t, "OperationDefinition", "requirements", element.getRequirementsElement(), -1);
    if (element.hasIdempotentElement())
      composeBoolean(t, "OperationDefinition", "idempotent", element.getIdempotentElement(), -1);
    if (element.hasCodeElement())
      composeCode(t, "OperationDefinition", "code", element.getCodeElement(), -1);
    if (element.hasCommentElement())
      composeString(t, "OperationDefinition", "comment", element.getCommentElement(), -1);
    if (element.hasBase())
      composeReference(t, "OperationDefinition", "base", element.getBase(), -1);
    if (element.hasSystemElement())
      composeBoolean(t, "OperationDefinition", "system", element.getSystemElement(), -1);
    for (int i = 0; i < element.getType().size(); i++)
      composeCode(t, "OperationDefinition", "type", element.getType().get(i), i);
    if (element.hasInstanceElement())
      composeBoolean(t, "OperationDefinition", "instance", element.getInstanceElement(), -1);
    for (int i = 0; i < element.getParameter().size(); i++)
      composeOperationDefinitionOperationDefinitionParameterComponent(t, "OperationDefinition", "parameter", element.getParameter().get(i), i);
  }

  protected void composeOperationDefinitionOperationDefinitionContactComponent(Complex parent, String parentType, String name, OperationDefinition.OperationDefinitionContactComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "contact", name, element, index);
    if (element.hasNameElement())
      composeString(t, "OperationDefinition", "name", element.getNameElement(), -1);
    for (int i = 0; i < element.getTelecom().size(); i++)
      composeContactPoint(t, "OperationDefinition", "telecom", element.getTelecom().get(i), i);
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

  protected void composeOrder(Complex parent, String parentType, String name, Order element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "Order", name, element, index);
    for (int i = 0; i < element.getIdentifier().size(); i++)
      composeIdentifier(t, "Order", "identifier", element.getIdentifier().get(i), i);
    if (element.hasDateElement())
      composeDateTime(t, "Order", "date", element.getDateElement(), -1);
    if (element.hasSubject())
      composeReference(t, "Order", "subject", element.getSubject(), -1);
    if (element.hasSource())
      composeReference(t, "Order", "source", element.getSource(), -1);
    if (element.hasTarget())
      composeReference(t, "Order", "target", element.getTarget(), -1);
    if (element.hasReason())
      composeType(t, "Order", "reason", element.getReason(), -1);
    if (element.hasWhen())
      composeOrderOrderWhenComponent(t, "Order", "when", element.getWhen(), -1);
    for (int i = 0; i < element.getDetail().size(); i++)
      composeReference(t, "Order", "detail", element.getDetail().get(i), i);
  }

  protected void composeOrderOrderWhenComponent(Complex parent, String parentType, String name, Order.OrderWhenComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "when", name, element, index);
    if (element.hasCode())
      composeCodeableConcept(t, "Order", "code", element.getCode(), -1);
    if (element.hasSchedule())
      composeTiming(t, "Order", "schedule", element.getSchedule(), -1);
  }

  protected void composeOrderResponse(Complex parent, String parentType, String name, OrderResponse element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "OrderResponse", name, element, index);
    for (int i = 0; i < element.getIdentifier().size(); i++)
      composeIdentifier(t, "OrderResponse", "identifier", element.getIdentifier().get(i), i);
    if (element.hasRequest())
      composeReference(t, "OrderResponse", "request", element.getRequest(), -1);
    if (element.hasDateElement())
      composeDateTime(t, "OrderResponse", "date", element.getDateElement(), -1);
    if (element.hasWho())
      composeReference(t, "OrderResponse", "who", element.getWho(), -1);
    if (element.hasOrderStatusElement())
      composeEnum(t, "OrderResponse", "orderStatus", element.getOrderStatusElement(), -1);
    if (element.hasDescriptionElement())
      composeString(t, "OrderResponse", "description", element.getDescriptionElement(), -1);
    for (int i = 0; i < element.getFulfillment().size(); i++)
      composeReference(t, "OrderResponse", "fulfillment", element.getFulfillment().get(i), i);
  }

  protected void composeOrderSet(Complex parent, String parentType, String name, OrderSet element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "OrderSet", name, element, index);
    if (element.hasModuleMetadata())
      composeModuleMetadata(t, "OrderSet", "moduleMetadata", element.getModuleMetadata(), -1);
    for (int i = 0; i < element.getLibrary().size(); i++)
      composeReference(t, "OrderSet", "library", element.getLibrary().get(i), i);
    for (int i = 0; i < element.getAction().size(); i++)
      composeActionDefinition(t, "OrderSet", "action", element.getAction().get(i), i);
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
    if (element.hasType())
      composeCodeableConcept(t, "Organization", "type", element.getType(), -1);
    if (element.hasNameElement())
      composeString(t, "Organization", "name", element.getNameElement(), -1);
    for (int i = 0; i < element.getTelecom().size(); i++)
      composeContactPoint(t, "Organization", "telecom", element.getTelecom().get(i), i);
    for (int i = 0; i < element.getAddress().size(); i++)
      composeAddress(t, "Organization", "address", element.getAddress().get(i), i);
    if (element.hasPartOf())
      composeReference(t, "Organization", "partOf", element.getPartOf(), -1);
    for (int i = 0; i < element.getContact().size(); i++)
      composeOrganizationOrganizationContactComponent(t, "Organization", "contact", element.getContact().get(i), i);
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
    for (int i = 0; i < element.getCareProvider().size(); i++)
      composeReference(t, "Patient", "careProvider", element.getCareProvider().get(i), i);
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
    if (element.hasRuleset())
      composeCoding(t, "PaymentNotice", "ruleset", element.getRuleset(), -1);
    if (element.hasOriginalRuleset())
      composeCoding(t, "PaymentNotice", "originalRuleset", element.getOriginalRuleset(), -1);
    if (element.hasCreatedElement())
      composeDateTime(t, "PaymentNotice", "created", element.getCreatedElement(), -1);
    if (element.hasTarget())
      composeType(t, "PaymentNotice", "target", element.getTarget(), -1);
    if (element.hasProvider())
      composeType(t, "PaymentNotice", "provider", element.getProvider(), -1);
    if (element.hasOrganization())
      composeType(t, "PaymentNotice", "organization", element.getOrganization(), -1);
    if (element.hasRequest())
      composeType(t, "PaymentNotice", "request", element.getRequest(), -1);
    if (element.hasResponse())
      composeType(t, "PaymentNotice", "response", element.getResponse(), -1);
    if (element.hasPaymentStatus())
      composeCoding(t, "PaymentNotice", "paymentStatus", element.getPaymentStatus(), -1);
    if (element.hasStatusDateElement())
      composeDate(t, "PaymentNotice", "statusDate", element.getStatusDateElement(), -1);
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
    if (element.hasRequest())
      composeType(t, "PaymentReconciliation", "request", element.getRequest(), -1);
    if (element.hasOutcomeElement())
      composeEnum(t, "PaymentReconciliation", "outcome", element.getOutcomeElement(), -1);
    if (element.hasDispositionElement())
      composeString(t, "PaymentReconciliation", "disposition", element.getDispositionElement(), -1);
    if (element.hasRuleset())
      composeCoding(t, "PaymentReconciliation", "ruleset", element.getRuleset(), -1);
    if (element.hasOriginalRuleset())
      composeCoding(t, "PaymentReconciliation", "originalRuleset", element.getOriginalRuleset(), -1);
    if (element.hasCreatedElement())
      composeDateTime(t, "PaymentReconciliation", "created", element.getCreatedElement(), -1);
    if (element.hasPeriod())
      composePeriod(t, "PaymentReconciliation", "period", element.getPeriod(), -1);
    if (element.hasOrganization())
      composeType(t, "PaymentReconciliation", "organization", element.getOrganization(), -1);
    if (element.hasRequestProvider())
      composeType(t, "PaymentReconciliation", "requestProvider", element.getRequestProvider(), -1);
    if (element.hasRequestOrganization())
      composeType(t, "PaymentReconciliation", "requestOrganization", element.getRequestOrganization(), -1);
    for (int i = 0; i < element.getDetail().size(); i++)
      composePaymentReconciliationDetailsComponent(t, "PaymentReconciliation", "detail", element.getDetail().get(i), i);
    if (element.hasForm())
      composeCoding(t, "PaymentReconciliation", "form", element.getForm(), -1);
    if (element.hasTotal())
      composeQuantity(t, "PaymentReconciliation", "total", element.getTotal(), -1);
    for (int i = 0; i < element.getNote().size(); i++)
      composePaymentReconciliationNotesComponent(t, "PaymentReconciliation", "note", element.getNote().get(i), i);
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
      composeCoding(t, "PaymentReconciliation", "type", element.getType(), -1);
    if (element.hasRequest())
      composeType(t, "PaymentReconciliation", "request", element.getRequest(), -1);
    if (element.hasResponce())
      composeType(t, "PaymentReconciliation", "responce", element.getResponce(), -1);
    if (element.hasSubmitter())
      composeType(t, "PaymentReconciliation", "submitter", element.getSubmitter(), -1);
    if (element.hasPayee())
      composeType(t, "PaymentReconciliation", "payee", element.getPayee(), -1);
    if (element.hasDateElement())
      composeDate(t, "PaymentReconciliation", "date", element.getDateElement(), -1);
    if (element.hasAmount())
      composeQuantity(t, "PaymentReconciliation", "amount", element.getAmount(), -1);
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
    composeBackboneElement(t, "note", name, element, index);
    if (element.hasType())
      composeCoding(t, "PaymentReconciliation", "type", element.getType(), -1);
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
    for (int i = 0; i < element.getPractitionerRole().size(); i++)
      composePractitionerPractitionerPractitionerRoleComponent(t, "Practitioner", "practitionerRole", element.getPractitionerRole().get(i), i);
    for (int i = 0; i < element.getQualification().size(); i++)
      composePractitionerPractitionerQualificationComponent(t, "Practitioner", "qualification", element.getQualification().get(i), i);
    for (int i = 0; i < element.getCommunication().size(); i++)
      composeCodeableConcept(t, "Practitioner", "communication", element.getCommunication().get(i), i);
  }

  protected void composePractitionerPractitionerPractitionerRoleComponent(Complex parent, String parentType, String name, Practitioner.PractitionerPractitionerRoleComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "practitionerRole", name, element, index);
    if (element.hasOrganization())
      composeReference(t, "Practitioner", "organization", element.getOrganization(), -1);
    if (element.hasRole())
      composeCodeableConcept(t, "Practitioner", "role", element.getRole(), -1);
    for (int i = 0; i < element.getSpecialty().size(); i++)
      composeCodeableConcept(t, "Practitioner", "specialty", element.getSpecialty().get(i), i);
    for (int i = 0; i < element.getIdentifier().size(); i++)
      composeIdentifier(t, "Practitioner", "identifier", element.getIdentifier().get(i), i);
    for (int i = 0; i < element.getTelecom().size(); i++)
      composeContactPoint(t, "Practitioner", "telecom", element.getTelecom().get(i), i);
    if (element.hasPeriod())
      composePeriod(t, "Practitioner", "period", element.getPeriod(), -1);
    for (int i = 0; i < element.getLocation().size(); i++)
      composeReference(t, "Practitioner", "location", element.getLocation().get(i), i);
    for (int i = 0; i < element.getHealthcareService().size(); i++)
      composeReference(t, "Practitioner", "healthcareService", element.getHealthcareService().get(i), i);
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
    if (element.hasPractitioner())
      composeReference(t, "PractitionerRole", "practitioner", element.getPractitioner(), -1);
    if (element.hasOrganization())
      composeReference(t, "PractitionerRole", "organization", element.getOrganization(), -1);
    for (int i = 0; i < element.getRole().size(); i++)
      composeCodeableConcept(t, "PractitionerRole", "role", element.getRole().get(i), i);
    for (int i = 0; i < element.getSpecialty().size(); i++)
      composeCodeableConcept(t, "PractitionerRole", "specialty", element.getSpecialty().get(i), i);
    for (int i = 0; i < element.getLocation().size(); i++)
      composeReference(t, "PractitionerRole", "location", element.getLocation().get(i), i);
    for (int i = 0; i < element.getHealthcareService().size(); i++)
      composeReference(t, "PractitionerRole", "healthcareService", element.getHealthcareService().get(i), i);
    for (int i = 0; i < element.getTelecom().size(); i++)
      composeContactPoint(t, "PractitionerRole", "telecom", element.getTelecom().get(i), i);
    if (element.hasPeriod())
      composePeriod(t, "PractitionerRole", "period", element.getPeriod(), -1);
    for (int i = 0; i < element.getAvailableTime().size(); i++)
      composePractitionerRolePractitionerRoleAvailableTimeComponent(t, "PractitionerRole", "availableTime", element.getAvailableTime().get(i), i);
    for (int i = 0; i < element.getNotAvailable().size(); i++)
      composePractitionerRolePractitionerRoleNotAvailableComponent(t, "PractitionerRole", "notAvailable", element.getNotAvailable().get(i), i);
    if (element.hasAvailabilityExceptionsElement())
      composeString(t, "PractitionerRole", "availabilityExceptions", element.getAvailabilityExceptionsElement(), -1);
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
      composeCode(t, "PractitionerRole", "daysOfWeek", element.getDaysOfWeek().get(i), i);
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
    if (element.hasSubject())
      composeReference(t, "Procedure", "subject", element.getSubject(), -1);
    if (element.hasStatusElement())
      composeEnum(t, "Procedure", "status", element.getStatusElement(), -1);
    if (element.hasCategory())
      composeCodeableConcept(t, "Procedure", "category", element.getCategory(), -1);
    if (element.hasCode())
      composeCodeableConcept(t, "Procedure", "code", element.getCode(), -1);
    if (element.hasNotPerformedElement())
      composeBoolean(t, "Procedure", "notPerformed", element.getNotPerformedElement(), -1);
    for (int i = 0; i < element.getReasonNotPerformed().size(); i++)
      composeCodeableConcept(t, "Procedure", "reasonNotPerformed", element.getReasonNotPerformed().get(i), i);
    for (int i = 0; i < element.getBodySite().size(); i++)
      composeCodeableConcept(t, "Procedure", "bodySite", element.getBodySite().get(i), i);
    if (element.hasReason())
      composeType(t, "Procedure", "reason", element.getReason(), -1);
    for (int i = 0; i < element.getPerformer().size(); i++)
      composeProcedureProcedurePerformerComponent(t, "Procedure", "performer", element.getPerformer().get(i), i);
    if (element.hasPerformed())
      composeType(t, "Procedure", "performed", element.getPerformed(), -1);
    if (element.hasEncounter())
      composeReference(t, "Procedure", "encounter", element.getEncounter(), -1);
    if (element.hasLocation())
      composeReference(t, "Procedure", "location", element.getLocation(), -1);
    if (element.hasOutcome())
      composeCodeableConcept(t, "Procedure", "outcome", element.getOutcome(), -1);
    for (int i = 0; i < element.getReport().size(); i++)
      composeReference(t, "Procedure", "report", element.getReport().get(i), i);
    for (int i = 0; i < element.getComplication().size(); i++)
      composeCodeableConcept(t, "Procedure", "complication", element.getComplication().get(i), i);
    for (int i = 0; i < element.getFollowUp().size(); i++)
      composeCodeableConcept(t, "Procedure", "followUp", element.getFollowUp().get(i), i);
    if (element.hasRequest())
      composeReference(t, "Procedure", "request", element.getRequest(), -1);
    for (int i = 0; i < element.getNotes().size(); i++)
      composeAnnotation(t, "Procedure", "notes", element.getNotes().get(i), i);
    for (int i = 0; i < element.getFocalDevice().size(); i++)
      composeProcedureProcedureFocalDeviceComponent(t, "Procedure", "focalDevice", element.getFocalDevice().get(i), i);
    for (int i = 0; i < element.getUsed().size(); i++)
      composeReference(t, "Procedure", "used", element.getUsed().get(i), i);
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
    if (element.hasActor())
      composeReference(t, "Procedure", "actor", element.getActor(), -1);
    if (element.hasRole())
      composeCodeableConcept(t, "Procedure", "role", element.getRole(), -1);
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
    if (element.hasSubject())
      composeReference(t, "ProcedureRequest", "subject", element.getSubject(), -1);
    if (element.hasCode())
      composeCodeableConcept(t, "ProcedureRequest", "code", element.getCode(), -1);
    for (int i = 0; i < element.getBodySite().size(); i++)
      composeCodeableConcept(t, "ProcedureRequest", "bodySite", element.getBodySite().get(i), i);
    if (element.hasReason())
      composeType(t, "ProcedureRequest", "reason", element.getReason(), -1);
    if (element.hasScheduled())
      composeType(t, "ProcedureRequest", "scheduled", element.getScheduled(), -1);
    if (element.hasEncounter())
      composeReference(t, "ProcedureRequest", "encounter", element.getEncounter(), -1);
    if (element.hasPerformer())
      composeReference(t, "ProcedureRequest", "performer", element.getPerformer(), -1);
    if (element.hasStatusElement())
      composeEnum(t, "ProcedureRequest", "status", element.getStatusElement(), -1);
    for (int i = 0; i < element.getNotes().size(); i++)
      composeAnnotation(t, "ProcedureRequest", "notes", element.getNotes().get(i), i);
    if (element.hasAsNeeded())
      composeType(t, "ProcedureRequest", "asNeeded", element.getAsNeeded(), -1);
    if (element.hasOrderedOnElement())
      composeDateTime(t, "ProcedureRequest", "orderedOn", element.getOrderedOnElement(), -1);
    if (element.hasOrderer())
      composeReference(t, "ProcedureRequest", "orderer", element.getOrderer(), -1);
    if (element.hasPriorityElement())
      composeEnum(t, "ProcedureRequest", "priority", element.getPriorityElement(), -1);
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
    if (element.hasActionElement())
      composeEnum(t, "ProcessRequest", "action", element.getActionElement(), -1);
    for (int i = 0; i < element.getIdentifier().size(); i++)
      composeIdentifier(t, "ProcessRequest", "identifier", element.getIdentifier().get(i), i);
    if (element.hasRuleset())
      composeCoding(t, "ProcessRequest", "ruleset", element.getRuleset(), -1);
    if (element.hasOriginalRuleset())
      composeCoding(t, "ProcessRequest", "originalRuleset", element.getOriginalRuleset(), -1);
    if (element.hasCreatedElement())
      composeDateTime(t, "ProcessRequest", "created", element.getCreatedElement(), -1);
    if (element.hasTarget())
      composeType(t, "ProcessRequest", "target", element.getTarget(), -1);
    if (element.hasProvider())
      composeType(t, "ProcessRequest", "provider", element.getProvider(), -1);
    if (element.hasOrganization())
      composeType(t, "ProcessRequest", "organization", element.getOrganization(), -1);
    if (element.hasRequest())
      composeType(t, "ProcessRequest", "request", element.getRequest(), -1);
    if (element.hasResponse())
      composeType(t, "ProcessRequest", "response", element.getResponse(), -1);
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
    if (element.hasRequest())
      composeType(t, "ProcessResponse", "request", element.getRequest(), -1);
    if (element.hasOutcome())
      composeCoding(t, "ProcessResponse", "outcome", element.getOutcome(), -1);
    if (element.hasDispositionElement())
      composeString(t, "ProcessResponse", "disposition", element.getDispositionElement(), -1);
    if (element.hasRuleset())
      composeCoding(t, "ProcessResponse", "ruleset", element.getRuleset(), -1);
    if (element.hasOriginalRuleset())
      composeCoding(t, "ProcessResponse", "originalRuleset", element.getOriginalRuleset(), -1);
    if (element.hasCreatedElement())
      composeDateTime(t, "ProcessResponse", "created", element.getCreatedElement(), -1);
    if (element.hasOrganization())
      composeType(t, "ProcessResponse", "organization", element.getOrganization(), -1);
    if (element.hasRequestProvider())
      composeType(t, "ProcessResponse", "requestProvider", element.getRequestProvider(), -1);
    if (element.hasRequestOrganization())
      composeType(t, "ProcessResponse", "requestOrganization", element.getRequestOrganization(), -1);
    if (element.hasForm())
      composeCoding(t, "ProcessResponse", "form", element.getForm(), -1);
    for (int i = 0; i < element.getNotes().size(); i++)
      composeProcessResponseProcessResponseNotesComponent(t, "ProcessResponse", "notes", element.getNotes().get(i), i);
    for (int i = 0; i < element.getError().size(); i++)
      composeCoding(t, "ProcessResponse", "error", element.getError().get(i), i);
  }

  protected void composeProcessResponseProcessResponseNotesComponent(Complex parent, String parentType, String name, ProcessResponse.ProcessResponseNotesComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "notes", name, element, index);
    if (element.hasType())
      composeCoding(t, "ProcessResponse", "type", element.getType(), -1);
    if (element.hasTextElement())
      composeString(t, "ProcessResponse", "text", element.getTextElement(), -1);
  }

  protected void composeProtocol(Complex parent, String parentType, String name, Protocol element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "Protocol", name, element, index);
    for (int i = 0; i < element.getIdentifier().size(); i++)
      composeIdentifier(t, "Protocol", "identifier", element.getIdentifier().get(i), i);
    if (element.hasTitleElement())
      composeString(t, "Protocol", "title", element.getTitleElement(), -1);
    if (element.hasStatusElement())
      composeEnum(t, "Protocol", "status", element.getStatusElement(), -1);
    if (element.hasTypeElement())
      composeEnum(t, "Protocol", "type", element.getTypeElement(), -1);
    if (element.hasSubject())
      composeReference(t, "Protocol", "subject", element.getSubject(), -1);
    if (element.hasGroup())
      composeReference(t, "Protocol", "group", element.getGroup(), -1);
    if (element.hasPurposeElement())
      composeString(t, "Protocol", "purpose", element.getPurposeElement(), -1);
    if (element.hasAuthor())
      composeReference(t, "Protocol", "author", element.getAuthor(), -1);
    for (int i = 0; i < element.getStep().size(); i++)
      composeProtocolProtocolStepComponent(t, "Protocol", "step", element.getStep().get(i), i);
  }

  protected void composeProtocolProtocolStepComponent(Complex parent, String parentType, String name, Protocol.ProtocolStepComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "step", name, element, index);
    if (element.hasNameElement())
      composeString(t, "Protocol", "name", element.getNameElement(), -1);
    if (element.hasDescriptionElement())
      composeString(t, "Protocol", "description", element.getDescriptionElement(), -1);
    if (element.hasDuration())
      composeQuantity(t, "Protocol", "duration", element.getDuration(), -1);
    if (element.hasPrecondition())
      composeProtocolProtocolStepPreconditionComponent(t, "Protocol", "precondition", element.getPrecondition(), -1);
    if (element.hasExit())
      composeProtocolProtocolStepPreconditionComponent(t, "Protocol", "exit", element.getExit(), -1);
    if (element.hasFirstActivityElement())
      composeUri(t, "Protocol", "firstActivity", element.getFirstActivityElement(), -1);
    for (int i = 0; i < element.getActivity().size(); i++)
      composeProtocolProtocolStepActivityComponent(t, "Protocol", "activity", element.getActivity().get(i), i);
    for (int i = 0; i < element.getNext().size(); i++)
      composeProtocolProtocolStepNextComponent(t, "Protocol", "next", element.getNext().get(i), i);
  }

  protected void composeProtocolProtocolStepPreconditionComponent(Complex parent, String parentType, String name, Protocol.ProtocolStepPreconditionComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "precondition", name, element, index);
    if (element.hasDescriptionElement())
      composeString(t, "Protocol", "description", element.getDescriptionElement(), -1);
    if (element.hasCondition())
      composeProtocolProtocolStepPreconditionConditionComponent(t, "Protocol", "condition", element.getCondition(), -1);
    for (int i = 0; i < element.getIntersection().size(); i++)
      composeProtocolProtocolStepPreconditionComponent(t, "Protocol", "intersection", element.getIntersection().get(i), i);
    for (int i = 0; i < element.getUnion().size(); i++)
      composeProtocolProtocolStepPreconditionComponent(t, "Protocol", "union", element.getUnion().get(i), i);
    for (int i = 0; i < element.getExclude().size(); i++)
      composeProtocolProtocolStepPreconditionComponent(t, "Protocol", "exclude", element.getExclude().get(i), i);
  }

  protected void composeProtocolProtocolStepPreconditionConditionComponent(Complex parent, String parentType, String name, Protocol.ProtocolStepPreconditionConditionComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "condition", name, element, index);
    if (element.hasType())
      composeCodeableConcept(t, "Protocol", "type", element.getType(), -1);
    if (element.hasValue())
      composeType(t, "Protocol", "value", element.getValue(), -1);
  }

  protected void composeProtocolProtocolStepActivityComponent(Complex parent, String parentType, String name, Protocol.ProtocolStepActivityComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "activity", name, element, index);
    for (int i = 0; i < element.getAlternative().size(); i++)
      composeUri(t, "Protocol", "alternative", element.getAlternative().get(i), i);
    for (int i = 0; i < element.getComponent().size(); i++)
      composeProtocolProtocolStepActivityComponentComponent(t, "Protocol", "component", element.getComponent().get(i), i);
    for (int i = 0; i < element.getFollowing().size(); i++)
      composeUri(t, "Protocol", "following", element.getFollowing().get(i), i);
    if (element.hasWait())
      composeQuantity(t, "Protocol", "wait", element.getWait(), -1);
    if (element.hasDetail())
      composeProtocolProtocolStepActivityDetailComponent(t, "Protocol", "detail", element.getDetail(), -1);
  }

  protected void composeProtocolProtocolStepActivityComponentComponent(Complex parent, String parentType, String name, Protocol.ProtocolStepActivityComponentComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "component", name, element, index);
    if (element.hasSequenceElement())
      composeInteger(t, "Protocol", "sequence", element.getSequenceElement(), -1);
    if (element.hasActivityElement())
      composeUri(t, "Protocol", "activity", element.getActivityElement(), -1);
  }

  protected void composeProtocolProtocolStepActivityDetailComponent(Complex parent, String parentType, String name, Protocol.ProtocolStepActivityDetailComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "detail", name, element, index);
    if (element.hasCategoryElement())
      composeEnum(t, "Protocol", "category", element.getCategoryElement(), -1);
    if (element.hasCode())
      composeCodeableConcept(t, "Protocol", "code", element.getCode(), -1);
    if (element.hasTiming())
      composeType(t, "Protocol", "timing", element.getTiming(), -1);
    if (element.hasLocation())
      composeReference(t, "Protocol", "location", element.getLocation(), -1);
    for (int i = 0; i < element.getPerformer().size(); i++)
      composeReference(t, "Protocol", "performer", element.getPerformer().get(i), i);
    if (element.hasProduct())
      composeReference(t, "Protocol", "product", element.getProduct(), -1);
    if (element.hasQuantity())
      composeQuantity(t, "Protocol", "quantity", element.getQuantity(), -1);
    if (element.hasDescriptionElement())
      composeString(t, "Protocol", "description", element.getDescriptionElement(), -1);
  }

  protected void composeProtocolProtocolStepNextComponent(Complex parent, String parentType, String name, Protocol.ProtocolStepNextComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "next", name, element, index);
    if (element.hasDescriptionElement())
      composeString(t, "Protocol", "description", element.getDescriptionElement(), -1);
    if (element.hasReferenceElement())
      composeUri(t, "Protocol", "reference", element.getReferenceElement(), -1);
    if (element.hasCondition())
      composeProtocolProtocolStepPreconditionComponent(t, "Protocol", "condition", element.getCondition(), -1);
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
    if (element.hasPeriod())
      composePeriod(t, "Provenance", "period", element.getPeriod(), -1);
    if (element.hasRecordedElement())
      composeInstant(t, "Provenance", "recorded", element.getRecordedElement(), -1);
    for (int i = 0; i < element.getReason().size(); i++)
      composeCoding(t, "Provenance", "reason", element.getReason().get(i), i);
    if (element.hasActivity())
      composeCoding(t, "Provenance", "activity", element.getActivity(), -1);
    if (element.hasLocation())
      composeReference(t, "Provenance", "location", element.getLocation(), -1);
    for (int i = 0; i < element.getPolicy().size(); i++)
      composeUri(t, "Provenance", "policy", element.getPolicy().get(i), i);
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
    if (element.hasRole())
      composeCoding(t, "Provenance", "role", element.getRole(), -1);
    if (element.hasActor())
      composeReference(t, "Provenance", "actor", element.getActor(), -1);
    if (element.hasUserId())
      composeIdentifier(t, "Provenance", "userId", element.getUserId(), -1);
    for (int i = 0; i < element.getRelatedAgent().size(); i++)
      composeProvenanceProvenanceAgentRelatedAgentComponent(t, "Provenance", "relatedAgent", element.getRelatedAgent().get(i), i);
  }

  protected void composeProvenanceProvenanceAgentRelatedAgentComponent(Complex parent, String parentType, String name, Provenance.ProvenanceAgentRelatedAgentComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "relatedAgent", name, element, index);
    if (element.hasType())
      composeCodeableConcept(t, "Provenance", "type", element.getType(), -1);
    if (element.hasTargetElement())
      composeUri(t, "Provenance", "target", element.getTargetElement(), -1);
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
    if (element.hasType())
      composeCoding(t, "Provenance", "type", element.getType(), -1);
    if (element.hasReferenceElement())
      composeUri(t, "Provenance", "reference", element.getReferenceElement(), -1);
    if (element.hasDisplayElement())
      composeString(t, "Provenance", "display", element.getDisplayElement(), -1);
    if (element.hasAgent())
      composeProvenanceProvenanceAgentComponent(t, "Provenance", "agent", element.getAgent(), -1);
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
    if (element.hasStatusElement())
      composeEnum(t, "Questionnaire", "status", element.getStatusElement(), -1);
    if (element.hasDateElement())
      composeDateTime(t, "Questionnaire", "date", element.getDateElement(), -1);
    if (element.hasPublisherElement())
      composeString(t, "Questionnaire", "publisher", element.getPublisherElement(), -1);
    for (int i = 0; i < element.getTelecom().size(); i++)
      composeContactPoint(t, "Questionnaire", "telecom", element.getTelecom().get(i), i);
    for (int i = 0; i < element.getUseContext().size(); i++)
      composeCodeableConcept(t, "Questionnaire", "useContext", element.getUseContext().get(i), i);
    if (element.hasTitleElement())
      composeString(t, "Questionnaire", "title", element.getTitleElement(), -1);
    for (int i = 0; i < element.getConcept().size(); i++)
      composeCoding(t, "Questionnaire", "concept", element.getConcept().get(i), i);
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
    for (int i = 0; i < element.getConcept().size(); i++)
      composeCoding(t, "Questionnaire", "concept", element.getConcept().get(i), i);
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
    if (element.hasAnsweredElement())
      composeBoolean(t, "Questionnaire", "answered", element.getAnsweredElement(), -1);
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
    if (element.hasQuestionnaire())
      composeReference(t, "QuestionnaireResponse", "questionnaire", element.getQuestionnaire(), -1);
    if (element.hasStatusElement())
      composeEnum(t, "QuestionnaireResponse", "status", element.getStatusElement(), -1);
    if (element.hasSubject())
      composeReference(t, "QuestionnaireResponse", "subject", element.getSubject(), -1);
    if (element.hasAuthor())
      composeReference(t, "QuestionnaireResponse", "author", element.getAuthor(), -1);
    if (element.hasAuthoredElement())
      composeDateTime(t, "QuestionnaireResponse", "authored", element.getAuthoredElement(), -1);
    if (element.hasSource())
      composeReference(t, "QuestionnaireResponse", "source", element.getSource(), -1);
    if (element.hasEncounter())
      composeReference(t, "QuestionnaireResponse", "encounter", element.getEncounter(), -1);
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

  protected void composeReferralRequest(Complex parent, String parentType, String name, ReferralRequest element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "ReferralRequest", name, element, index);
    for (int i = 0; i < element.getIdentifier().size(); i++)
      composeIdentifier(t, "ReferralRequest", "identifier", element.getIdentifier().get(i), i);
    for (int i = 0; i < element.getBasedOn().size(); i++)
      composeReference(t, "ReferralRequest", "basedOn", element.getBasedOn().get(i), i);
    if (element.hasParent())
      composeIdentifier(t, "ReferralRequest", "parent", element.getParent(), -1);
    if (element.hasStatusElement())
      composeEnum(t, "ReferralRequest", "status", element.getStatusElement(), -1);
    if (element.hasCategoryElement())
      composeEnum(t, "ReferralRequest", "category", element.getCategoryElement(), -1);
    if (element.hasType())
      composeCodeableConcept(t, "ReferralRequest", "type", element.getType(), -1);
    if (element.hasPriority())
      composeCodeableConcept(t, "ReferralRequest", "priority", element.getPriority(), -1);
    if (element.hasPatient())
      composeReference(t, "ReferralRequest", "patient", element.getPatient(), -1);
    if (element.hasContext())
      composeReference(t, "ReferralRequest", "context", element.getContext(), -1);
    if (element.hasFulfillmentTime())
      composePeriod(t, "ReferralRequest", "fulfillmentTime", element.getFulfillmentTime(), -1);
    if (element.hasAuthoredElement())
      composeDateTime(t, "ReferralRequest", "authored", element.getAuthoredElement(), -1);
    if (element.hasRequester())
      composeReference(t, "ReferralRequest", "requester", element.getRequester(), -1);
    if (element.hasSpecialty())
      composeCodeableConcept(t, "ReferralRequest", "specialty", element.getSpecialty(), -1);
    for (int i = 0; i < element.getRecipient().size(); i++)
      composeReference(t, "ReferralRequest", "recipient", element.getRecipient().get(i), i);
    if (element.hasReason())
      composeCodeableConcept(t, "ReferralRequest", "reason", element.getReason(), -1);
    if (element.hasDescriptionElement())
      composeString(t, "ReferralRequest", "description", element.getDescriptionElement(), -1);
    for (int i = 0; i < element.getServiceRequested().size(); i++)
      composeCodeableConcept(t, "ReferralRequest", "serviceRequested", element.getServiceRequested().get(i), i);
    for (int i = 0; i < element.getSupportingInformation().size(); i++)
      composeReference(t, "ReferralRequest", "supportingInformation", element.getSupportingInformation().get(i), i);
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
    if (element.hasPatient())
      composeReference(t, "RelatedPerson", "patient", element.getPatient(), -1);
    if (element.hasRelationship())
      composeCodeableConcept(t, "RelatedPerson", "relationship", element.getRelationship(), -1);
    if (element.hasName())
      composeHumanName(t, "RelatedPerson", "name", element.getName(), -1);
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
    if (element.hasSubject())
      composeReference(t, "RiskAssessment", "subject", element.getSubject(), -1);
    if (element.hasDateElement())
      composeDateTime(t, "RiskAssessment", "date", element.getDateElement(), -1);
    if (element.hasCondition())
      composeReference(t, "RiskAssessment", "condition", element.getCondition(), -1);
    if (element.hasEncounter())
      composeReference(t, "RiskAssessment", "encounter", element.getEncounter(), -1);
    if (element.hasPerformer())
      composeReference(t, "RiskAssessment", "performer", element.getPerformer(), -1);
    if (element.hasIdentifier())
      composeIdentifier(t, "RiskAssessment", "identifier", element.getIdentifier(), -1);
    if (element.hasMethod())
      composeCodeableConcept(t, "RiskAssessment", "method", element.getMethod(), -1);
    for (int i = 0; i < element.getBasis().size(); i++)
      composeReference(t, "RiskAssessment", "basis", element.getBasis().get(i), i);
    for (int i = 0; i < element.getPrediction().size(); i++)
      composeRiskAssessmentRiskAssessmentPredictionComponent(t, "RiskAssessment", "prediction", element.getPrediction().get(i), i);
    if (element.hasMitigationElement())
      composeString(t, "RiskAssessment", "mitigation", element.getMitigationElement(), -1);
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
    if (element.hasServiceCategory())
      composeCodeableConcept(t, "Schedule", "serviceCategory", element.getServiceCategory(), -1);
    for (int i = 0; i < element.getServiceType().size(); i++)
      composeCodeableConcept(t, "Schedule", "serviceType", element.getServiceType().get(i), i);
    for (int i = 0; i < element.getSpecialty().size(); i++)
      composeCodeableConcept(t, "Schedule", "specialty", element.getSpecialty().get(i), i);
    if (element.hasActor())
      composeReference(t, "Schedule", "actor", element.getActor(), -1);
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
      composeSearchParameterSearchParameterContactComponent(t, "SearchParameter", "contact", element.getContact().get(i), i);
    for (int i = 0; i < element.getUseContext().size(); i++)
      composeCodeableConcept(t, "SearchParameter", "useContext", element.getUseContext().get(i), i);
    if (element.hasRequirementsElement())
      composeString(t, "SearchParameter", "requirements", element.getRequirementsElement(), -1);
    if (element.hasCodeElement())
      composeCode(t, "SearchParameter", "code", element.getCodeElement(), -1);
    if (element.hasBaseElement())
      composeCode(t, "SearchParameter", "base", element.getBaseElement(), -1);
    if (element.hasTypeElement())
      composeEnum(t, "SearchParameter", "type", element.getTypeElement(), -1);
    if (element.hasDescriptionElement())
      composeString(t, "SearchParameter", "description", element.getDescriptionElement(), -1);
    if (element.hasExpressionElement())
      composeString(t, "SearchParameter", "expression", element.getExpressionElement(), -1);
    if (element.hasXpathElement())
      composeString(t, "SearchParameter", "xpath", element.getXpathElement(), -1);
    if (element.hasXpathUsageElement())
      composeEnum(t, "SearchParameter", "xpathUsage", element.getXpathUsageElement(), -1);
    for (int i = 0; i < element.getTarget().size(); i++)
      composeCode(t, "SearchParameter", "target", element.getTarget().get(i), i);
  }

  protected void composeSearchParameterSearchParameterContactComponent(Complex parent, String parentType, String name, SearchParameter.SearchParameterContactComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "contact", name, element, index);
    if (element.hasNameElement())
      composeString(t, "SearchParameter", "name", element.getNameElement(), -1);
    for (int i = 0; i < element.getTelecom().size(); i++)
      composeContactPoint(t, "SearchParameter", "telecom", element.getTelecom().get(i), i);
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
    if (element.hasTypeElement())
      composeEnum(t, "Sequence", "type", element.getTypeElement(), -1);
    if (element.hasPatient())
      composeReference(t, "Sequence", "patient", element.getPatient(), -1);
    if (element.hasSpecimen())
      composeReference(t, "Sequence", "specimen", element.getSpecimen(), -1);
    if (element.hasDevice())
      composeReference(t, "Sequence", "device", element.getDevice(), -1);
    if (element.hasQuantity())
      composeQuantity(t, "Sequence", "quantity", element.getQuantity(), -1);
    if (element.hasSpecies())
      composeCodeableConcept(t, "Sequence", "species", element.getSpecies(), -1);
    for (int i = 0; i < element.getReferenceSeq().size(); i++)
      composeSequenceSequenceReferenceSeqComponent(t, "Sequence", "referenceSeq", element.getReferenceSeq().get(i), i);
    if (element.hasVariation())
      composeSequenceSequenceVariationComponent(t, "Sequence", "variation", element.getVariation(), -1);
    for (int i = 0; i < element.getQuality().size(); i++)
      composeSequenceSequenceQualityComponent(t, "Sequence", "quality", element.getQuality().get(i), i);
    if (element.hasAllelicState())
      composeCodeableConcept(t, "Sequence", "allelicState", element.getAllelicState(), -1);
    if (element.hasAllelicFrequencyElement())
      composeDecimal(t, "Sequence", "allelicFrequency", element.getAllelicFrequencyElement(), -1);
    if (element.hasCopyNumberEvent())
      composeCodeableConcept(t, "Sequence", "copyNumberEvent", element.getCopyNumberEvent(), -1);
    if (element.hasReadCoverageElement())
      composeInteger(t, "Sequence", "readCoverage", element.getReadCoverageElement(), -1);
    for (int i = 0; i < element.getRepository().size(); i++)
      composeSequenceSequenceRepositoryComponent(t, "Sequence", "repository", element.getRepository().get(i), i);
    for (int i = 0; i < element.getPointer().size(); i++)
      composeReference(t, "Sequence", "pointer", element.getPointer().get(i), i);
    if (element.hasObservedSeqElement())
      composeString(t, "Sequence", "observedSeq", element.getObservedSeqElement(), -1);
    if (element.hasObservation())
      composeReference(t, "Sequence", "observation", element.getObservation(), -1);
    if (element.hasStructureVariation())
      composeSequenceSequenceStructureVariationComponent(t, "Sequence", "structureVariation", element.getStructureVariation(), -1);
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
    if (element.hasWindowStartElement())
      composeInteger(t, "Sequence", "windowStart", element.getWindowStartElement(), -1);
    if (element.hasWindowEndElement())
      composeInteger(t, "Sequence", "windowEnd", element.getWindowEndElement(), -1);
  }

  protected void composeSequenceSequenceVariationComponent(Complex parent, String parentType, String name, Sequence.SequenceVariationComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "variation", name, element, index);
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
    if (element.hasStartElement())
      composeInteger(t, "Sequence", "start", element.getStartElement(), -1);
    if (element.hasEndElement())
      composeInteger(t, "Sequence", "end", element.getEndElement(), -1);
    if (element.hasScore())
      composeQuantity(t, "Sequence", "score", element.getScore(), -1);
    if (element.hasMethodElement())
      composeString(t, "Sequence", "method", element.getMethodElement(), -1);
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
    if (element.hasUrlElement())
      composeUri(t, "Sequence", "url", element.getUrlElement(), -1);
    if (element.hasNameElement())
      composeString(t, "Sequence", "name", element.getNameElement(), -1);
    if (element.hasVariantIdElement())
      composeString(t, "Sequence", "variantId", element.getVariantIdElement(), -1);
    if (element.hasReadIdElement())
      composeString(t, "Sequence", "readId", element.getReadIdElement(), -1);
  }

  protected void composeSequenceSequenceStructureVariationComponent(Complex parent, String parentType, String name, Sequence.SequenceStructureVariationComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "structureVariation", name, element, index);
    if (element.hasPrecisionOfBoundariesElement())
      composeString(t, "Sequence", "precisionOfBoundaries", element.getPrecisionOfBoundariesElement(), -1);
    if (element.hasReportedaCGHRatioElement())
      composeDecimal(t, "Sequence", "reportedaCGHRatio", element.getReportedaCGHRatioElement(), -1);
    if (element.hasLengthElement())
      composeInteger(t, "Sequence", "length", element.getLengthElement(), -1);
    if (element.hasOuter())
      composeSequenceSequenceStructureVariationOuterComponent(t, "Sequence", "outer", element.getOuter(), -1);
    if (element.hasInner())
      composeSequenceSequenceStructureVariationInnerComponent(t, "Sequence", "inner", element.getInner(), -1);
  }

  protected void composeSequenceSequenceStructureVariationOuterComponent(Complex parent, String parentType, String name, Sequence.SequenceStructureVariationOuterComponent element, int index) {
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

  protected void composeSequenceSequenceStructureVariationInnerComponent(Complex parent, String parentType, String name, Sequence.SequenceStructureVariationInnerComponent element, int index) {
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
    if (element.hasCollection())
      composeSpecimenSpecimenCollectionComponent(t, "Specimen", "collection", element.getCollection(), -1);
    for (int i = 0; i < element.getTreatment().size(); i++)
      composeSpecimenSpecimenTreatmentComponent(t, "Specimen", "treatment", element.getTreatment().get(i), i);
    for (int i = 0; i < element.getContainer().size(); i++)
      composeSpecimenSpecimenContainerComponent(t, "Specimen", "container", element.getContainer().get(i), i);
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
    if (element.hasCommentElement())
      composeString(t, "Specimen", "comment", element.getCommentElement(), -1);
    if (element.hasCollected())
      composeType(t, "Specimen", "collected", element.getCollected(), -1);
    if (element.hasQuantity())
      composeQuantity(t, "Specimen", "quantity", element.getQuantity(), -1);
    if (element.hasMethod())
      composeCodeableConcept(t, "Specimen", "method", element.getMethod(), -1);
    if (element.hasBodySite())
      composeCodeableConcept(t, "Specimen", "bodySite", element.getBodySite(), -1);
  }

  protected void composeSpecimenSpecimenTreatmentComponent(Complex parent, String parentType, String name, Specimen.SpecimenTreatmentComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "treatment", name, element, index);
    if (element.hasDescriptionElement())
      composeString(t, "Specimen", "description", element.getDescriptionElement(), -1);
    if (element.hasProcedure())
      composeCodeableConcept(t, "Specimen", "procedure", element.getProcedure(), -1);
    for (int i = 0; i < element.getAdditive().size(); i++)
      composeReference(t, "Specimen", "additive", element.getAdditive().get(i), i);
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
    if (element.hasDisplayElement())
      composeString(t, "StructureDefinition", "display", element.getDisplayElement(), -1);
    if (element.hasStatusElement())
      composeEnum(t, "StructureDefinition", "status", element.getStatusElement(), -1);
    if (element.hasExperimentalElement())
      composeBoolean(t, "StructureDefinition", "experimental", element.getExperimentalElement(), -1);
    if (element.hasPublisherElement())
      composeString(t, "StructureDefinition", "publisher", element.getPublisherElement(), -1);
    for (int i = 0; i < element.getContact().size(); i++)
      composeStructureDefinitionStructureDefinitionContactComponent(t, "StructureDefinition", "contact", element.getContact().get(i), i);
    if (element.hasDateElement())
      composeDateTime(t, "StructureDefinition", "date", element.getDateElement(), -1);
    if (element.hasDescriptionElement())
      composeString(t, "StructureDefinition", "description", element.getDescriptionElement(), -1);
    for (int i = 0; i < element.getUseContext().size(); i++)
      composeCodeableConcept(t, "StructureDefinition", "useContext", element.getUseContext().get(i), i);
    if (element.hasRequirementsElement())
      composeString(t, "StructureDefinition", "requirements", element.getRequirementsElement(), -1);
    if (element.hasCopyrightElement())
      composeString(t, "StructureDefinition", "copyright", element.getCopyrightElement(), -1);
    for (int i = 0; i < element.getCode().size(); i++)
      composeCoding(t, "StructureDefinition", "code", element.getCode().get(i), i);
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
    if (element.hasBaseTypeElement())
      composeCode(t, "StructureDefinition", "baseType", element.getBaseTypeElement(), -1);
    if (element.hasBaseDefinitionElement())
      composeUri(t, "StructureDefinition", "baseDefinition", element.getBaseDefinitionElement(), -1);
    if (element.hasDerivationElement())
      composeEnum(t, "StructureDefinition", "derivation", element.getDerivationElement(), -1);
    if (element.hasSnapshot())
      composeStructureDefinitionStructureDefinitionSnapshotComponent(t, "StructureDefinition", "snapshot", element.getSnapshot(), -1);
    if (element.hasDifferential())
      composeStructureDefinitionStructureDefinitionDifferentialComponent(t, "StructureDefinition", "differential", element.getDifferential(), -1);
  }

  protected void composeStructureDefinitionStructureDefinitionContactComponent(Complex parent, String parentType, String name, StructureDefinition.StructureDefinitionContactComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "contact", name, element, index);
    if (element.hasNameElement())
      composeString(t, "StructureDefinition", "name", element.getNameElement(), -1);
    for (int i = 0; i < element.getTelecom().size(); i++)
      composeContactPoint(t, "StructureDefinition", "telecom", element.getTelecom().get(i), i);
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
    if (element.hasCommentsElement())
      composeString(t, "StructureDefinition", "comments", element.getCommentsElement(), -1);
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
    if (element.hasStatusElement())
      composeEnum(t, "StructureMap", "status", element.getStatusElement(), -1);
    if (element.hasExperimentalElement())
      composeBoolean(t, "StructureMap", "experimental", element.getExperimentalElement(), -1);
    if (element.hasPublisherElement())
      composeString(t, "StructureMap", "publisher", element.getPublisherElement(), -1);
    for (int i = 0; i < element.getContact().size(); i++)
      composeStructureMapStructureMapContactComponent(t, "StructureMap", "contact", element.getContact().get(i), i);
    if (element.hasDateElement())
      composeDateTime(t, "StructureMap", "date", element.getDateElement(), -1);
    if (element.hasDescriptionElement())
      composeString(t, "StructureMap", "description", element.getDescriptionElement(), -1);
    for (int i = 0; i < element.getUseContext().size(); i++)
      composeCodeableConcept(t, "StructureMap", "useContext", element.getUseContext().get(i), i);
    if (element.hasRequirementsElement())
      composeString(t, "StructureMap", "requirements", element.getRequirementsElement(), -1);
    if (element.hasCopyrightElement())
      composeString(t, "StructureMap", "copyright", element.getCopyrightElement(), -1);
    for (int i = 0; i < element.getStructure().size(); i++)
      composeStructureMapStructureMapStructureComponent(t, "StructureMap", "structure", element.getStructure().get(i), i);
    for (int i = 0; i < element.getImport().size(); i++)
      composeUri(t, "StructureMap", "import", element.getImport().get(i), i);
    for (int i = 0; i < element.getGroup().size(); i++)
      composeStructureMapStructureMapGroupComponent(t, "StructureMap", "group", element.getGroup().get(i), i);
  }

  protected void composeStructureMapStructureMapContactComponent(Complex parent, String parentType, String name, StructureMap.StructureMapContactComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "contact", name, element, index);
    if (element.hasNameElement())
      composeString(t, "StructureMap", "name", element.getNameElement(), -1);
    for (int i = 0; i < element.getTelecom().size(); i++)
      composeContactPoint(t, "StructureMap", "telecom", element.getTelecom().get(i), i);
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
    if (element.hasRequiredElement())
      composeBoolean(t, "StructureMap", "required", element.getRequiredElement(), -1);
    if (element.hasContextElement())
      composeId(t, "StructureMap", "context", element.getContextElement(), -1);
    if (element.hasContextTypeElement())
      composeEnum(t, "StructureMap", "contextType", element.getContextTypeElement(), -1);
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
    if (element.hasCriteriaElement())
      composeString(t, "Subscription", "criteria", element.getCriteriaElement(), -1);
    for (int i = 0; i < element.getContact().size(); i++)
      composeContactPoint(t, "Subscription", "contact", element.getContact().get(i), i);
    if (element.hasReasonElement())
      composeString(t, "Subscription", "reason", element.getReasonElement(), -1);
    if (element.hasStatusElement())
      composeEnum(t, "Subscription", "status", element.getStatusElement(), -1);
    if (element.hasErrorElement())
      composeString(t, "Subscription", "error", element.getErrorElement(), -1);
    if (element.hasChannel())
      composeSubscriptionSubscriptionChannelComponent(t, "Subscription", "channel", element.getChannel(), -1);
    if (element.hasEndElement())
      composeInstant(t, "Subscription", "end", element.getEndElement(), -1);
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
    if (element.hasHeaderElement())
      composeString(t, "Subscription", "header", element.getHeaderElement(), -1);
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
      composeReference(t, "Substance", "substance", element.getSubstance(), -1);
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
    if (element.hasStatusElement())
      composeEnum(t, "SupplyDelivery", "status", element.getStatusElement(), -1);
    if (element.hasPatient())
      composeReference(t, "SupplyDelivery", "patient", element.getPatient(), -1);
    if (element.hasType())
      composeCodeableConcept(t, "SupplyDelivery", "type", element.getType(), -1);
    if (element.hasQuantity())
      composeQuantity(t, "SupplyDelivery", "quantity", element.getQuantity(), -1);
    if (element.hasSuppliedItem())
      composeReference(t, "SupplyDelivery", "suppliedItem", element.getSuppliedItem(), -1);
    if (element.hasSupplier())
      composeReference(t, "SupplyDelivery", "supplier", element.getSupplier(), -1);
    if (element.hasWhenPrepared())
      composePeriod(t, "SupplyDelivery", "whenPrepared", element.getWhenPrepared(), -1);
    if (element.hasTimeElement())
      composeDateTime(t, "SupplyDelivery", "time", element.getTimeElement(), -1);
    if (element.hasDestination())
      composeReference(t, "SupplyDelivery", "destination", element.getDestination(), -1);
    for (int i = 0; i < element.getReceiver().size(); i++)
      composeReference(t, "SupplyDelivery", "receiver", element.getReceiver().get(i), i);
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
    if (element.hasPatient())
      composeReference(t, "SupplyRequest", "patient", element.getPatient(), -1);
    if (element.hasSource())
      composeReference(t, "SupplyRequest", "source", element.getSource(), -1);
    if (element.hasDateElement())
      composeDateTime(t, "SupplyRequest", "date", element.getDateElement(), -1);
    if (element.hasIdentifier())
      composeIdentifier(t, "SupplyRequest", "identifier", element.getIdentifier(), -1);
    if (element.hasStatusElement())
      composeEnum(t, "SupplyRequest", "status", element.getStatusElement(), -1);
    if (element.hasKind())
      composeCodeableConcept(t, "SupplyRequest", "kind", element.getKind(), -1);
    if (element.hasOrderedItem())
      composeReference(t, "SupplyRequest", "orderedItem", element.getOrderedItem(), -1);
    for (int i = 0; i < element.getSupplier().size(); i++)
      composeReference(t, "SupplyRequest", "supplier", element.getSupplier().get(i), i);
    if (element.hasReason())
      composeType(t, "SupplyRequest", "reason", element.getReason(), -1);
    if (element.hasWhen())
      composeSupplyRequestSupplyRequestWhenComponent(t, "SupplyRequest", "when", element.getWhen(), -1);
  }

  protected void composeSupplyRequestSupplyRequestWhenComponent(Complex parent, String parentType, String name, SupplyRequest.SupplyRequestWhenComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "when", name, element, index);
    if (element.hasCode())
      composeCodeableConcept(t, "SupplyRequest", "code", element.getCode(), -1);
    if (element.hasSchedule())
      composeTiming(t, "SupplyRequest", "schedule", element.getSchedule(), -1);
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
    if (element.hasIdentifier())
      composeIdentifier(t, "Task", "identifier", element.getIdentifier(), -1);
    if (element.hasType())
      composeCodeableConcept(t, "Task", "type", element.getType(), -1);
    if (element.hasDescriptionElement())
      composeString(t, "Task", "description", element.getDescriptionElement(), -1);
    for (int i = 0; i < element.getPerformerType().size(); i++)
      composeCoding(t, "Task", "performerType", element.getPerformerType().get(i), i);
    if (element.hasPriorityElement())
      composeEnum(t, "Task", "priority", element.getPriorityElement(), -1);
    if (element.hasStatusElement())
      composeEnum(t, "Task", "status", element.getStatusElement(), -1);
    if (element.hasFailureReason())
      composeCodeableConcept(t, "Task", "failureReason", element.getFailureReason(), -1);
    if (element.hasSubject())
      composeReference(t, "Task", "subject", element.getSubject(), -1);
    if (element.hasFor())
      composeReference(t, "Task", "for", element.getFor(), -1);
    if (element.hasDefinitionElement())
      composeUri(t, "Task", "definition", element.getDefinitionElement(), -1);
    if (element.hasCreatedElement())
      composeDateTime(t, "Task", "created", element.getCreatedElement(), -1);
    if (element.hasLastModifiedElement())
      composeDateTime(t, "Task", "lastModified", element.getLastModifiedElement(), -1);
    if (element.hasCreator())
      composeReference(t, "Task", "creator", element.getCreator(), -1);
    if (element.hasOwner())
      composeReference(t, "Task", "owner", element.getOwner(), -1);
    if (element.hasParent())
      composeReference(t, "Task", "parent", element.getParent(), -1);
    for (int i = 0; i < element.getInput().size(); i++)
      composeTaskParameterComponent(t, "Task", "input", element.getInput().get(i), i);
    for (int i = 0; i < element.getOutput().size(); i++)
      composeTaskTaskOutputComponent(t, "Task", "output", element.getOutput().get(i), i);
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
    if (element.hasNameElement())
      composeString(t, "Task", "name", element.getNameElement(), -1);
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
    if (element.hasNameElement())
      composeString(t, "Task", "name", element.getNameElement(), -1);
    if (element.hasValue())
      composeType(t, "Task", "value", element.getValue(), -1);
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
    if (element.hasVersionElement())
      composeString(t, "TestScript", "version", element.getVersionElement(), -1);
    if (element.hasNameElement())
      composeString(t, "TestScript", "name", element.getNameElement(), -1);
    if (element.hasStatusElement())
      composeEnum(t, "TestScript", "status", element.getStatusElement(), -1);
    if (element.hasIdentifier())
      composeIdentifier(t, "TestScript", "identifier", element.getIdentifier(), -1);
    if (element.hasExperimentalElement())
      composeBoolean(t, "TestScript", "experimental", element.getExperimentalElement(), -1);
    if (element.hasPublisherElement())
      composeString(t, "TestScript", "publisher", element.getPublisherElement(), -1);
    for (int i = 0; i < element.getContact().size(); i++)
      composeTestScriptTestScriptContactComponent(t, "TestScript", "contact", element.getContact().get(i), i);
    if (element.hasDateElement())
      composeDateTime(t, "TestScript", "date", element.getDateElement(), -1);
    if (element.hasDescriptionElement())
      composeString(t, "TestScript", "description", element.getDescriptionElement(), -1);
    for (int i = 0; i < element.getUseContext().size(); i++)
      composeCodeableConcept(t, "TestScript", "useContext", element.getUseContext().get(i), i);
    if (element.hasRequirementsElement())
      composeString(t, "TestScript", "requirements", element.getRequirementsElement(), -1);
    if (element.hasCopyrightElement())
      composeString(t, "TestScript", "copyright", element.getCopyrightElement(), -1);
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

  protected void composeTestScriptTestScriptContactComponent(Complex parent, String parentType, String name, TestScript.TestScriptContactComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "contact", name, element, index);
    if (element.hasNameElement())
      composeString(t, "TestScript", "name", element.getNameElement(), -1);
    for (int i = 0; i < element.getTelecom().size(); i++)
      composeContactPoint(t, "TestScript", "telecom", element.getTelecom().get(i), i);
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
    if (element.hasConformance())
      composeReference(t, "TestScript", "conformance", element.getConformance(), -1);
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
    if (element.hasHeaderFieldElement())
      composeString(t, "TestScript", "headerField", element.getHeaderFieldElement(), -1);
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
      composeTestScriptTestScriptRuleParamComponent(t, "TestScript", "param", element.getParam().get(i), i);
  }

  protected void composeTestScriptTestScriptRuleParamComponent(Complex parent, String parentType, String name, TestScript.TestScriptRuleParamComponent element, int index) {
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
      composeTestScriptTestScriptRulesetRuleComponent(t, "TestScript", "rule", element.getRule().get(i), i);
  }

  protected void composeTestScriptTestScriptRulesetRuleComponent(Complex parent, String parentType, String name, TestScript.TestScriptRulesetRuleComponent element, int index) {
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
      composeTestScriptTestScriptRulesetRuleParamComponent(t, "TestScript", "param", element.getParam().get(i), i);
  }

  protected void composeTestScriptTestScriptRulesetRuleParamComponent(Complex parent, String parentType, String name, TestScript.TestScriptRulesetRuleParamComponent element, int index) {
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
    if (element.hasMetadata())
      composeTestScriptTestScriptMetadataComponent(t, "TestScript", "metadata", element.getMetadata(), -1);
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
    if (element.hasCompareToSourcePathElement())
      composeString(t, "TestScript", "compareToSourcePath", element.getCompareToSourcePathElement(), -1);
    if (element.hasContentTypeElement())
      composeEnum(t, "TestScript", "contentType", element.getContentTypeElement(), -1);
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
    if (element.hasResourceElement())
      composeCode(t, "TestScript", "resource", element.getResourceElement(), -1);
    if (element.hasResponseElement())
      composeEnum(t, "TestScript", "response", element.getResponseElement(), -1);
    if (element.hasResponseCodeElement())
      composeString(t, "TestScript", "responseCode", element.getResponseCodeElement(), -1);
    if (element.hasRule())
      composeTestScriptSetupActionAssertRuleComponent(t, "TestScript", "rule", element.getRule(), -1);
    if (element.hasRuleset())
      composeTestScriptSetupActionAssertRulesetComponent(t, "TestScript", "ruleset", element.getRuleset(), -1);
    if (element.hasSourceIdElement())
      composeId(t, "TestScript", "sourceId", element.getSourceIdElement(), -1);
    if (element.hasValidateProfileIdElement())
      composeId(t, "TestScript", "validateProfileId", element.getValidateProfileIdElement(), -1);
    if (element.hasValueElement())
      composeString(t, "TestScript", "value", element.getValueElement(), -1);
    if (element.hasWarningOnlyElement())
      composeBoolean(t, "TestScript", "warningOnly", element.getWarningOnlyElement(), -1);
  }

  protected void composeTestScriptSetupActionAssertRuleComponent(Complex parent, String parentType, String name, TestScript.SetupActionAssertRuleComponent element, int index) {
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
      composeTestScriptSetupActionAssertRuleParamComponent(t, "TestScript", "param", element.getParam().get(i), i);
  }

  protected void composeTestScriptSetupActionAssertRuleParamComponent(Complex parent, String parentType, String name, TestScript.SetupActionAssertRuleParamComponent element, int index) {
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

  protected void composeTestScriptSetupActionAssertRulesetComponent(Complex parent, String parentType, String name, TestScript.SetupActionAssertRulesetComponent element, int index) {
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
      composeTestScriptSetupActionAssertRulesetRuleComponent(t, "TestScript", "rule", element.getRule().get(i), i);
  }

  protected void composeTestScriptSetupActionAssertRulesetRuleComponent(Complex parent, String parentType, String name, TestScript.SetupActionAssertRulesetRuleComponent element, int index) {
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
      composeTestScriptSetupActionAssertRulesetRuleParamComponent(t, "TestScript", "param", element.getParam().get(i), i);
  }

  protected void composeTestScriptSetupActionAssertRulesetRuleParamComponent(Complex parent, String parentType, String name, TestScript.SetupActionAssertRulesetRuleParamComponent element, int index) {
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
    if (element.hasMetadata())
      composeTestScriptTestScriptMetadataComponent(t, "TestScript", "metadata", element.getMetadata(), -1);
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
    if (element.hasIdentifier())
      composeIdentifier(t, "ValueSet", "identifier", element.getIdentifier(), -1);
    if (element.hasVersionElement())
      composeString(t, "ValueSet", "version", element.getVersionElement(), -1);
    if (element.hasNameElement())
      composeString(t, "ValueSet", "name", element.getNameElement(), -1);
    if (element.hasStatusElement())
      composeEnum(t, "ValueSet", "status", element.getStatusElement(), -1);
    if (element.hasExperimentalElement())
      composeBoolean(t, "ValueSet", "experimental", element.getExperimentalElement(), -1);
    if (element.hasPublisherElement())
      composeString(t, "ValueSet", "publisher", element.getPublisherElement(), -1);
    for (int i = 0; i < element.getContact().size(); i++)
      composeValueSetValueSetContactComponent(t, "ValueSet", "contact", element.getContact().get(i), i);
    if (element.hasDateElement())
      composeDateTime(t, "ValueSet", "date", element.getDateElement(), -1);
    if (element.hasLockedDateElement())
      composeDate(t, "ValueSet", "lockedDate", element.getLockedDateElement(), -1);
    if (element.hasDescriptionElement())
      composeString(t, "ValueSet", "description", element.getDescriptionElement(), -1);
    for (int i = 0; i < element.getUseContext().size(); i++)
      composeCodeableConcept(t, "ValueSet", "useContext", element.getUseContext().get(i), i);
    if (element.hasImmutableElement())
      composeBoolean(t, "ValueSet", "immutable", element.getImmutableElement(), -1);
    if (element.hasRequirementsElement())
      composeString(t, "ValueSet", "requirements", element.getRequirementsElement(), -1);
    if (element.hasCopyrightElement())
      composeString(t, "ValueSet", "copyright", element.getCopyrightElement(), -1);
    if (element.hasExtensibleElement())
      composeBoolean(t, "ValueSet", "extensible", element.getExtensibleElement(), -1);
    if (element.hasCompose())
      composeValueSetValueSetComposeComponent(t, "ValueSet", "compose", element.getCompose(), -1);
    if (element.hasExpansion())
      composeValueSetValueSetExpansionComponent(t, "ValueSet", "expansion", element.getExpansion(), -1);
  }

  protected void composeValueSetValueSetContactComponent(Complex parent, String parentType, String name, ValueSet.ValueSetContactComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "contact", name, element, index);
    if (element.hasNameElement())
      composeString(t, "ValueSet", "name", element.getNameElement(), -1);
    for (int i = 0; i < element.getTelecom().size(); i++)
      composeContactPoint(t, "ValueSet", "telecom", element.getTelecom().get(i), i);
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
    for (int i = 0; i < element.getImport().size(); i++)
      composeUri(t, "ValueSet", "import", element.getImport().get(i), i);
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
    if (element.hasVersionElement())
      composeString(t, "ValueSet", "version", element.getVersionElement(), -1);
    if (element.hasCodeElement())
      composeCode(t, "ValueSet", "code", element.getCodeElement(), -1);
    if (element.hasDisplayElement())
      composeString(t, "ValueSet", "display", element.getDisplayElement(), -1);
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
    if (element.hasDateWrittenElement())
      composeDateTime(t, "VisionPrescription", "dateWritten", element.getDateWrittenElement(), -1);
    if (element.hasPatient())
      composeReference(t, "VisionPrescription", "patient", element.getPatient(), -1);
    if (element.hasPrescriber())
      composeReference(t, "VisionPrescription", "prescriber", element.getPrescriber(), -1);
    if (element.hasEncounter())
      composeReference(t, "VisionPrescription", "encounter", element.getEncounter(), -1);
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
      composeCoding(t, "VisionPrescription", "product", element.getProduct(), -1);
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
    if (element.hasNotesElement())
      composeString(t, "VisionPrescription", "notes", element.getNotesElement(), -1);
  }

  @Override
  protected void composeResource(Complex parent, Resource resource) {
    if (resource instanceof Parameters)
      composeParameters(parent, null, "Parameters", (Parameters)resource, -1);
    else if (resource instanceof Account)
      composeAccount(parent, null, "Account", (Account)resource, -1);
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
    else if (resource instanceof BodySite)
      composeBodySite(parent, null, "BodySite", (BodySite)resource, -1);
    else if (resource instanceof Bundle)
      composeBundle(parent, null, "Bundle", (Bundle)resource, -1);
    else if (resource instanceof CarePlan)
      composeCarePlan(parent, null, "CarePlan", (CarePlan)resource, -1);
    else if (resource instanceof CareTeam)
      composeCareTeam(parent, null, "CareTeam", (CareTeam)resource, -1);
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
    else if (resource instanceof Conformance)
      composeConformance(parent, null, "Conformance", (Conformance)resource, -1);
    else if (resource instanceof Contract)
      composeContract(parent, null, "Contract", (Contract)resource, -1);
    else if (resource instanceof Coverage)
      composeCoverage(parent, null, "Coverage", (Coverage)resource, -1);
    else if (resource instanceof DataElement)
      composeDataElement(parent, null, "DataElement", (DataElement)resource, -1);
    else if (resource instanceof DecisionSupportRule)
      composeDecisionSupportRule(parent, null, "DecisionSupportRule", (DecisionSupportRule)resource, -1);
    else if (resource instanceof DecisionSupportServiceModule)
      composeDecisionSupportServiceModule(parent, null, "DecisionSupportServiceModule", (DecisionSupportServiceModule)resource, -1);
    else if (resource instanceof DetectedIssue)
      composeDetectedIssue(parent, null, "DetectedIssue", (DetectedIssue)resource, -1);
    else if (resource instanceof Device)
      composeDevice(parent, null, "Device", (Device)resource, -1);
    else if (resource instanceof DeviceComponent)
      composeDeviceComponent(parent, null, "DeviceComponent", (DeviceComponent)resource, -1);
    else if (resource instanceof DeviceMetric)
      composeDeviceMetric(parent, null, "DeviceMetric", (DeviceMetric)resource, -1);
    else if (resource instanceof DeviceUseRequest)
      composeDeviceUseRequest(parent, null, "DeviceUseRequest", (DeviceUseRequest)resource, -1);
    else if (resource instanceof DeviceUseStatement)
      composeDeviceUseStatement(parent, null, "DeviceUseStatement", (DeviceUseStatement)resource, -1);
    else if (resource instanceof DiagnosticOrder)
      composeDiagnosticOrder(parent, null, "DiagnosticOrder", (DiagnosticOrder)resource, -1);
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
    else if (resource instanceof EnrollmentRequest)
      composeEnrollmentRequest(parent, null, "EnrollmentRequest", (EnrollmentRequest)resource, -1);
    else if (resource instanceof EnrollmentResponse)
      composeEnrollmentResponse(parent, null, "EnrollmentResponse", (EnrollmentResponse)resource, -1);
    else if (resource instanceof EpisodeOfCare)
      composeEpisodeOfCare(parent, null, "EpisodeOfCare", (EpisodeOfCare)resource, -1);
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
    else if (resource instanceof Group)
      composeGroup(parent, null, "Group", (Group)resource, -1);
    else if (resource instanceof GuidanceResponse)
      composeGuidanceResponse(parent, null, "GuidanceResponse", (GuidanceResponse)resource, -1);
    else if (resource instanceof HealthcareService)
      composeHealthcareService(parent, null, "HealthcareService", (HealthcareService)resource, -1);
    else if (resource instanceof ImagingExcerpt)
      composeImagingExcerpt(parent, null, "ImagingExcerpt", (ImagingExcerpt)resource, -1);
    else if (resource instanceof ImagingObjectSelection)
      composeImagingObjectSelection(parent, null, "ImagingObjectSelection", (ImagingObjectSelection)resource, -1);
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
    else if (resource instanceof MedicationOrder)
      composeMedicationOrder(parent, null, "MedicationOrder", (MedicationOrder)resource, -1);
    else if (resource instanceof MedicationStatement)
      composeMedicationStatement(parent, null, "MedicationStatement", (MedicationStatement)resource, -1);
    else if (resource instanceof MessageHeader)
      composeMessageHeader(parent, null, "MessageHeader", (MessageHeader)resource, -1);
    else if (resource instanceof ModuleDefinition)
      composeModuleDefinition(parent, null, "ModuleDefinition", (ModuleDefinition)resource, -1);
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
    else if (resource instanceof Order)
      composeOrder(parent, null, "Order", (Order)resource, -1);
    else if (resource instanceof OrderResponse)
      composeOrderResponse(parent, null, "OrderResponse", (OrderResponse)resource, -1);
    else if (resource instanceof OrderSet)
      composeOrderSet(parent, null, "OrderSet", (OrderSet)resource, -1);
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
    else if (resource instanceof Protocol)
      composeProtocol(parent, null, "Protocol", (Protocol)resource, -1);
    else if (resource instanceof Provenance)
      composeProvenance(parent, null, "Provenance", (Provenance)resource, -1);
    else if (resource instanceof Questionnaire)
      composeQuestionnaire(parent, null, "Questionnaire", (Questionnaire)resource, -1);
    else if (resource instanceof QuestionnaireResponse)
      composeQuestionnaireResponse(parent, null, "QuestionnaireResponse", (QuestionnaireResponse)resource, -1);
    else if (resource instanceof ReferralRequest)
      composeReferralRequest(parent, null, "ReferralRequest", (ReferralRequest)resource, -1);
    else if (resource instanceof RelatedPerson)
      composeRelatedPerson(parent, null, "RelatedPerson", (RelatedPerson)resource, -1);
    else if (resource instanceof RiskAssessment)
      composeRiskAssessment(parent, null, "RiskAssessment", (RiskAssessment)resource, -1);
    else if (resource instanceof Schedule)
      composeSchedule(parent, null, "Schedule", (Schedule)resource, -1);
    else if (resource instanceof SearchParameter)
      composeSearchParameter(parent, null, "SearchParameter", (SearchParameter)resource, -1);
    else if (resource instanceof Sequence)
      composeSequence(parent, null, "Sequence", (Sequence)resource, -1);
    else if (resource instanceof Slot)
      composeSlot(parent, null, "Slot", (Slot)resource, -1);
    else if (resource instanceof Specimen)
      composeSpecimen(parent, null, "Specimen", (Specimen)resource, -1);
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
    else if (value instanceof MarkdownType)
      composeMarkdown(parent, parentType, name, (MarkdownType)value, index);
    else if (value instanceof IntegerType)
      composeInteger(parent, parentType, name, (IntegerType)value, index);
    else if (value instanceof DateTimeType)
      composeDateTime(parent, parentType, name, (DateTimeType)value, index);
    else if (value instanceof UnsignedIntType)
      composeUnsignedInt(parent, parentType, name, (UnsignedIntType)value, index);
    else if (value instanceof CodeType)
      composeCode(parent, parentType, name, (CodeType)value, index);
    else if (value instanceof DateType)
      composeDate(parent, parentType, name, (DateType)value, index);
    else if (value instanceof DecimalType)
      composeDecimal(parent, parentType, name, (DecimalType)value, index);
    else if (value instanceof UriType)
      composeUri(parent, parentType, name, (UriType)value, index);
    else if (value instanceof IdType)
      composeId(parent, parentType, name, (IdType)value, index);
    else if (value instanceof Base64BinaryType)
      composeBase64Binary(parent, parentType, name, (Base64BinaryType)value, index);
    else if (value instanceof TimeType)
      composeTime(parent, parentType, name, (TimeType)value, index);
    else if (value instanceof OidType)
      composeOid(parent, parentType, name, (OidType)value, index);
    else if (value instanceof PositiveIntType)
      composePositiveInt(parent, parentType, name, (PositiveIntType)value, index);
    else if (value instanceof StringType)
      composeString(parent, parentType, name, (StringType)value, index);
    else if (value instanceof BooleanType)
      composeBoolean(parent, parentType, name, (BooleanType)value, index);
    else if (value instanceof UuidType)
      composeUuid(parent, parentType, name, (UuidType)value, index);
    else if (value instanceof InstantType)
      composeInstant(parent, parentType, name, (InstantType)value, index);
    else if (value instanceof Extension)
      composeExtension(parent, parentType, name, (Extension)value, index);
    else if (value instanceof Narrative)
      composeNarrative(parent, parentType, name, (Narrative)value, index);
    else if (value instanceof Period)
      composePeriod(parent, parentType, name, (Period)value, index);
    else if (value instanceof Coding)
      composeCoding(parent, parentType, name, (Coding)value, index);
    else if (value instanceof Range)
      composeRange(parent, parentType, name, (Range)value, index);
    else if (value instanceof Quantity)
      composeQuantity(parent, parentType, name, (Quantity)value, index);
    else if (value instanceof Attachment)
      composeAttachment(parent, parentType, name, (Attachment)value, index);
    else if (value instanceof Ratio)
      composeRatio(parent, parentType, name, (Ratio)value, index);
    else if (value instanceof Annotation)
      composeAnnotation(parent, parentType, name, (Annotation)value, index);
    else if (value instanceof SampledData)
      composeSampledData(parent, parentType, name, (SampledData)value, index);
    else if (value instanceof Reference)
      composeReference(parent, parentType, name, (Reference)value, index);
    else if (value instanceof CodeableConcept)
      composeCodeableConcept(parent, parentType, name, (CodeableConcept)value, index);
    else if (value instanceof Identifier)
      composeIdentifier(parent, parentType, name, (Identifier)value, index);
    else if (value instanceof Signature)
      composeSignature(parent, parentType, name, (Signature)value, index);
    else if (value instanceof TriggerDefinition)
      composeTriggerDefinition(parent, parentType, name, (TriggerDefinition)value, index);
    else if (value instanceof ElementDefinition)
      composeElementDefinition(parent, parentType, name, (ElementDefinition)value, index);
    else if (value instanceof Timing)
      composeTiming(parent, parentType, name, (Timing)value, index);
    else if (value instanceof ModuleMetadata)
      composeModuleMetadata(parent, parentType, name, (ModuleMetadata)value, index);
    else if (value instanceof ActionDefinition)
      composeActionDefinition(parent, parentType, name, (ActionDefinition)value, index);
    else if (value instanceof Address)
      composeAddress(parent, parentType, name, (Address)value, index);
    else if (value instanceof HumanName)
      composeHumanName(parent, parentType, name, (HumanName)value, index);
    else if (value instanceof DataRequirement)
      composeDataRequirement(parent, parentType, name, (DataRequirement)value, index);
    else if (value instanceof Meta)
      composeMeta(parent, parentType, name, (Meta)value, index);
    else if (value instanceof ParameterDefinition)
      composeParameterDefinition(parent, parentType, name, (ParameterDefinition)value, index);
    else if (value instanceof ContactPoint)
      composeContactPoint(parent, parentType, name, (ContactPoint)value, index);
    else
      throw new Error("Unhandled type");
  }

}

