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

// Generated on Thu, Sep 13, 2018 09:04-0400 for FHIR v3.5.0

import org.hl7.fhir.r4.model.DateType;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.IntegerType;
import org.hl7.fhir.r4.model.OidType;
import org.hl7.fhir.r4.model.CanonicalType;
import org.hl7.fhir.r4.model.UriType;
import org.hl7.fhir.r4.model.UuidType;
import org.hl7.fhir.r4.model.UrlType;
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

  protected void composeCanonical(Complex parent, String parentType, String name, CanonicalType value, int index) {
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

  protected void composeUrl(Complex parent, String parentType, String name, UrlType value, int index) {
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
    if (element.hasSourceElement())
      composeUri(t, "Meta", "source", element.getSourceElement(), -1);
    for (int i = 0; i < element.getProfile().size(); i++)
      composeCanonical(t, "Meta", "profile", element.getProfile().get(i), i);
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
      composeUrl(t, "Attachment", "url", element.getUrlElement(), -1);
    if (element.hasSizeElement())
      composeUnsignedInt(t, "Attachment", "size", element.getSizeElement(), -1);
    if (element.hasHashElement())
      composeBase64Binary(t, "Attachment", "hash", element.getHashElement(), -1);
    if (element.hasTitleElement())
      composeString(t, "Attachment", "title", element.getTitleElement(), -1);
    if (element.hasCreationElement())
      composeDateTime(t, "Attachment", "creation", element.getCreationElement(), -1);
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
      composeCanonical(t, "DataRequirement", "profile", element.getProfile().get(i), i);
    if (element.hasSubject())
      composeType(t, "DataRequirement", "subject", element.getSubject(), -1);
    for (int i = 0; i < element.getMustSupport().size(); i++)
      composeString(t, "DataRequirement", "mustSupport", element.getMustSupport().get(i), i);
    for (int i = 0; i < element.getCodeFilter().size(); i++)
      composeDataRequirementDataRequirementCodeFilterComponent(t, "DataRequirement", "codeFilter", element.getCodeFilter().get(i), i);
    for (int i = 0; i < element.getDateFilter().size(); i++)
      composeDataRequirementDataRequirementDateFilterComponent(t, "DataRequirement", "dateFilter", element.getDateFilter().get(i), i);
    if (element.hasLimitElement())
      composePositiveInt(t, "DataRequirement", "limit", element.getLimitElement(), -1);
    for (int i = 0; i < element.getSort().size(); i++)
      composeDataRequirementDataRequirementSortComponent(t, "DataRequirement", "sort", element.getSort().get(i), i);
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
    if (element.hasSearchParamElement())
      composeString(t, "DataRequirement", "searchParam", element.getSearchParamElement(), -1);
    if (element.hasValueSetElement())
      composeCanonical(t, "DataRequirement", "valueSet", element.getValueSetElement(), -1);
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
    if (element.hasSearchParamElement())
      composeString(t, "DataRequirement", "searchParam", element.getSearchParamElement(), -1);
    if (element.hasValue())
      composeType(t, "DataRequirement", "value", element.getValue(), -1);
  }

  protected void composeDataRequirementDataRequirementSortComponent(Complex parent, String parentType, String name, DataRequirement.DataRequirementSortComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeElement(t, "sort", name, element, index);
    if (element.hasPathElement())
      composeString(t, "DataRequirement", "path", element.getPathElement(), -1);
    if (element.hasDirectionElement())
      composeEnum(t, "DataRequirement", "direction", element.getDirectionElement(), -1);
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
    for (int i = 0; i < element.getDoseAndRate().size(); i++)
      composeDosageDosageDoseAndRateComponent(t, "Dosage", "doseAndRate", element.getDoseAndRate().get(i), i);
    if (element.hasMaxDosePerPeriod())
      composeRatio(t, "Dosage", "maxDosePerPeriod", element.getMaxDosePerPeriod(), -1);
    if (element.hasMaxDosePerAdministration())
      composeQuantity(t, "Dosage", "maxDosePerAdministration", element.getMaxDosePerAdministration(), -1);
    if (element.hasMaxDosePerLifetime())
      composeQuantity(t, "Dosage", "maxDosePerLifetime", element.getMaxDosePerLifetime(), -1);
  }

  protected void composeDosageDosageDoseAndRateComponent(Complex parent, String parentType, String name, Dosage.DosageDoseAndRateComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeElement(t, "doseAndRate", name, element, index);
    if (element.hasType())
      composeCodeableConcept(t, "Dosage", "type", element.getType(), -1);
    if (element.hasDose())
      composeType(t, "Dosage", "dose", element.getDose(), -1);
    if (element.hasRate())
      composeType(t, "Dosage", "rate", element.getRate(), -1);
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
    if (element.hasValueElement())
      composeDecimal(t, "Money", "value", element.getValueElement(), -1);
    if (element.hasCurrencyElement())
      composeCode(t, "Money", "currency", element.getCurrencyElement(), -1);
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
    if (element.hasTypeElement())
      composeUri(t, "Reference", "type", element.getTypeElement(), -1);
    if (element.hasIdentifier())
      composeIdentifier(t, "Reference", "identifier", element.getIdentifier(), -1);
    if (element.hasDisplayElement())
      composeString(t, "Reference", "display", element.getDisplayElement(), -1);
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
      composeString(t, "TriggerDefinition", "name", element.getNameElement(), -1);
    if (element.hasTiming())
      composeType(t, "TriggerDefinition", "timing", element.getTiming(), -1);
    if (element.hasData())
      composeDataRequirement(t, "TriggerDefinition", "data", element.getData(), -1);
    if (element.hasCondition())
      composeExpression(t, "TriggerDefinition", "condition", element.getCondition(), -1);
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
      composeUrl(t, "RelatedArtifact", "url", element.getUrlElement(), -1);
    if (element.hasDocument())
      composeAttachment(t, "RelatedArtifact", "document", element.getDocument(), -1);
    if (element.hasResourceElement())
      composeCanonical(t, "RelatedArtifact", "resource", element.getResourceElement(), -1);
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
      composeMarkdown(t, "Annotation", "text", element.getTextElement(), -1);
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

  protected void composeExpression(Complex parent, String parentType, String name, Expression element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeElement(t, "Expression", name, element, index);
    if (element.hasDescriptionElement())
      composeString(t, "Expression", "description", element.getDescriptionElement(), -1);
    if (element.hasNameElement())
      composeId(t, "Expression", "name", element.getNameElement(), -1);
    if (element.hasLanguageElement())
      composeEnum(t, "Expression", "language", element.getLanguageElement(), -1);
    if (element.hasExpressionElement())
      composeString(t, "Expression", "expression", element.getExpressionElement(), -1);
    if (element.hasReferenceElement())
      composeUri(t, "Expression", "reference", element.getReferenceElement(), -1);
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
      composeReference(t, "Signature", "who", element.getWho(), -1);
    if (element.hasOnBehalfOf())
      composeReference(t, "Signature", "onBehalfOf", element.getOnBehalfOf(), -1);
    if (element.hasTargetFormatElement())
      composeCode(t, "Signature", "targetFormat", element.getTargetFormatElement(), -1);
    if (element.hasSigFormatElement())
      composeCode(t, "Signature", "sigFormat", element.getSigFormatElement(), -1);
    if (element.hasDataElement())
      composeBase64Binary(t, "Signature", "data", element.getDataElement(), -1);
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
      composePositiveInt(t, "Timing", "count", element.getCountElement(), -1);
    if (element.hasCountMaxElement())
      composePositiveInt(t, "Timing", "countMax", element.getCountMaxElement(), -1);
    if (element.hasDurationElement())
      composeDecimal(t, "Timing", "duration", element.getDurationElement(), -1);
    if (element.hasDurationMaxElement())
      composeDecimal(t, "Timing", "durationMax", element.getDurationMaxElement(), -1);
    if (element.hasDurationUnitElement())
      composeEnum(t, "Timing", "durationUnit", element.getDurationUnitElement(), -1);
    if (element.hasFrequencyElement())
      composePositiveInt(t, "Timing", "frequency", element.getFrequencyElement(), -1);
    if (element.hasFrequencyMaxElement())
      composePositiveInt(t, "Timing", "frequencyMax", element.getFrequencyMaxElement(), -1);
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
    if (element.hasProfileElement())
      composeCanonical(t, "ParameterDefinition", "profile", element.getProfileElement(), -1);
  }

  protected void composeMarketingStatus(Complex parent, String parentType, String name, MarketingStatus element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeElement(t, "MarketingStatus", name, element, index);
    if (element.hasCountry())
      composeCodeableConcept(t, "MarketingStatus", "country", element.getCountry(), -1);
    if (element.hasJurisdiction())
      composeCodeableConcept(t, "MarketingStatus", "jurisdiction", element.getJurisdiction(), -1);
    if (element.hasStatus())
      composeCodeableConcept(t, "MarketingStatus", "status", element.getStatus(), -1);
    if (element.hasDateRange())
      composePeriod(t, "MarketingStatus", "dateRange", element.getDateRange(), -1);
    if (element.hasRestoreDateElement())
      composeDateTime(t, "MarketingStatus", "restoreDate", element.getRestoreDateElement(), -1);
  }

  protected void composeSubstanceAmount(Complex parent, String parentType, String name, SubstanceAmount element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeElement(t, "SubstanceAmount", name, element, index);
    if (element.hasAmount())
      composeType(t, "SubstanceAmount", "amount", element.getAmount(), -1);
    if (element.hasAmountType())
      composeCodeableConcept(t, "SubstanceAmount", "amountType", element.getAmountType(), -1);
    if (element.hasAmountTextElement())
      composeString(t, "SubstanceAmount", "amountText", element.getAmountTextElement(), -1);
    if (element.hasReferenceRange())
      composeSubstanceAmountSubstanceAmountReferenceRangeComponent(t, "SubstanceAmount", "referenceRange", element.getReferenceRange(), -1);
  }

  protected void composeSubstanceAmountSubstanceAmountReferenceRangeComponent(Complex parent, String parentType, String name, SubstanceAmount.SubstanceAmountReferenceRangeComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeElement(t, "referenceRange", name, element, index);
    if (element.hasLowLimit())
      composeQuantity(t, "SubstanceAmount", "lowLimit", element.getLowLimit(), -1);
    if (element.hasHighLimit())
      composeQuantity(t, "SubstanceAmount", "highLimit", element.getHighLimit(), -1);
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
    if (element.hasSliceIsConstrainingElement())
      composeBoolean(t, "ElementDefinition", "sliceIsConstraining", element.getSliceIsConstrainingElement(), -1);
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
    if (element.hasIsModifierReasonElement())
      composeString(t, "ElementDefinition", "isModifierReason", element.getIsModifierReasonElement(), -1);
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
    for (int i = 0; i < element.getProfile().size(); i++)
      composeCanonical(t, "ElementDefinition", "profile", element.getProfile().get(i), i);
    for (int i = 0; i < element.getTargetProfile().size(); i++)
      composeCanonical(t, "ElementDefinition", "targetProfile", element.getTargetProfile().get(i), i);
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
      composeCanonical(t, "ElementDefinition", "source", element.getSourceElement(), -1);
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
    if (element.hasValueSetElement())
      composeCanonical(t, "ElementDefinition", "valueSet", element.getValueSetElement(), -1);
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

  protected void composeSubstanceMoiety(Complex parent, String parentType, String name, SubstanceMoiety element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeElement(t, "SubstanceMoiety", name, element, index);
    if (element.hasRole())
      composeCodeableConcept(t, "SubstanceMoiety", "role", element.getRole(), -1);
    if (element.hasIdentifier())
      composeIdentifier(t, "SubstanceMoiety", "identifier", element.getIdentifier(), -1);
    if (element.hasNameElement())
      composeString(t, "SubstanceMoiety", "name", element.getNameElement(), -1);
    if (element.hasStereochemistry())
      composeCodeableConcept(t, "SubstanceMoiety", "stereochemistry", element.getStereochemistry(), -1);
    if (element.hasOpticalActivity())
      composeCodeableConcept(t, "SubstanceMoiety", "opticalActivity", element.getOpticalActivity(), -1);
    if (element.hasMolecularFormulaElement())
      composeString(t, "SubstanceMoiety", "molecularFormula", element.getMolecularFormulaElement(), -1);
    if (element.hasAmount())
      composeSubstanceAmount(t, "SubstanceMoiety", "amount", element.getAmount(), -1);
  }

  protected void composeProductShelfLife(Complex parent, String parentType, String name, ProductShelfLife element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeElement(t, "ProductShelfLife", name, element, index);
    if (element.hasIdentifier())
      composeIdentifier(t, "ProductShelfLife", "identifier", element.getIdentifier(), -1);
    if (element.hasType())
      composeCodeableConcept(t, "ProductShelfLife", "type", element.getType(), -1);
    if (element.hasPeriod())
      composeQuantity(t, "ProductShelfLife", "period", element.getPeriod(), -1);
    for (int i = 0; i < element.getSpecialPrecautionsForStorage().size(); i++)
      composeCodeableConcept(t, "ProductShelfLife", "specialPrecautionsForStorage", element.getSpecialPrecautionsForStorage().get(i), i);
  }

  protected void composeProdCharacteristic(Complex parent, String parentType, String name, ProdCharacteristic element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeElement(t, "ProdCharacteristic", name, element, index);
    if (element.hasHeight())
      composeQuantity(t, "ProdCharacteristic", "height", element.getHeight(), -1);
    if (element.hasWidth())
      composeQuantity(t, "ProdCharacteristic", "width", element.getWidth(), -1);
    if (element.hasDepth())
      composeQuantity(t, "ProdCharacteristic", "depth", element.getDepth(), -1);
    if (element.hasWeight())
      composeQuantity(t, "ProdCharacteristic", "weight", element.getWeight(), -1);
    if (element.hasNominalVolume())
      composeQuantity(t, "ProdCharacteristic", "nominalVolume", element.getNominalVolume(), -1);
    if (element.hasExternalDiameter())
      composeQuantity(t, "ProdCharacteristic", "externalDiameter", element.getExternalDiameter(), -1);
    if (element.hasShapeElement())
      composeString(t, "ProdCharacteristic", "shape", element.getShapeElement(), -1);
    for (int i = 0; i < element.getColor().size(); i++)
      composeString(t, "ProdCharacteristic", "color", element.getColor().get(i), i);
    for (int i = 0; i < element.getImprint().size(); i++)
      composeString(t, "ProdCharacteristic", "imprint", element.getImprint().get(i), i);
    for (int i = 0; i < element.getImage().size(); i++)
      composeAttachment(t, "ProdCharacteristic", "image", element.getImage().get(i), i);
    if (element.hasScoring())
      composeCodeableConcept(t, "ProdCharacteristic", "scoring", element.getScoring(), -1);
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
    for (int i = 0; i < element.getSubject().size(); i++)
      composeReference(t, "Account", "subject", element.getSubject().get(i), i);
    if (element.hasServicePeriod())
      composePeriod(t, "Account", "servicePeriod", element.getServicePeriod(), -1);
    for (int i = 0; i < element.getCoverage().size(); i++)
      composeAccountCoverageComponent(t, "Account", "coverage", element.getCoverage().get(i), i);
    if (element.hasOwner())
      composeReference(t, "Account", "owner", element.getOwner(), -1);
    if (element.hasDescriptionElement())
      composeString(t, "Account", "description", element.getDescriptionElement(), -1);
    for (int i = 0; i < element.getGuarantor().size(); i++)
      composeAccountGuarantorComponent(t, "Account", "guarantor", element.getGuarantor().get(i), i);
    if (element.hasPartOf())
      composeReference(t, "Account", "partOf", element.getPartOf(), -1);
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
    if (element.hasSubtitleElement())
      composeString(t, "ActivityDefinition", "subtitle", element.getSubtitleElement(), -1);
    if (element.hasStatusElement())
      composeEnum(t, "ActivityDefinition", "status", element.getStatusElement(), -1);
    if (element.hasExperimentalElement())
      composeBoolean(t, "ActivityDefinition", "experimental", element.getExperimentalElement(), -1);
    if (element.hasSubject())
      composeType(t, "ActivityDefinition", "subject", element.getSubject(), -1);
    if (element.hasDateElement())
      composeDateTime(t, "ActivityDefinition", "date", element.getDateElement(), -1);
    if (element.hasPublisherElement())
      composeString(t, "ActivityDefinition", "publisher", element.getPublisherElement(), -1);
    for (int i = 0; i < element.getContact().size(); i++)
      composeContactDetail(t, "ActivityDefinition", "contact", element.getContact().get(i), i);
    if (element.hasDescriptionElement())
      composeMarkdown(t, "ActivityDefinition", "description", element.getDescriptionElement(), -1);
    for (int i = 0; i < element.getUseContext().size(); i++)
      composeUsageContext(t, "ActivityDefinition", "useContext", element.getUseContext().get(i), i);
    for (int i = 0; i < element.getJurisdiction().size(); i++)
      composeCodeableConcept(t, "ActivityDefinition", "jurisdiction", element.getJurisdiction().get(i), i);
    if (element.hasPurposeElement())
      composeMarkdown(t, "ActivityDefinition", "purpose", element.getPurposeElement(), -1);
    if (element.hasUsageElement())
      composeString(t, "ActivityDefinition", "usage", element.getUsageElement(), -1);
    if (element.hasCopyrightElement())
      composeMarkdown(t, "ActivityDefinition", "copyright", element.getCopyrightElement(), -1);
    if (element.hasApprovalDateElement())
      composeDate(t, "ActivityDefinition", "approvalDate", element.getApprovalDateElement(), -1);
    if (element.hasLastReviewDateElement())
      composeDate(t, "ActivityDefinition", "lastReviewDate", element.getLastReviewDateElement(), -1);
    if (element.hasEffectivePeriod())
      composePeriod(t, "ActivityDefinition", "effectivePeriod", element.getEffectivePeriod(), -1);
    for (int i = 0; i < element.getTopic().size(); i++)
      composeCodeableConcept(t, "ActivityDefinition", "topic", element.getTopic().get(i), i);
    for (int i = 0; i < element.getAuthor().size(); i++)
      composeContactDetail(t, "ActivityDefinition", "author", element.getAuthor().get(i), i);
    for (int i = 0; i < element.getEditor().size(); i++)
      composeContactDetail(t, "ActivityDefinition", "editor", element.getEditor().get(i), i);
    for (int i = 0; i < element.getReviewer().size(); i++)
      composeContactDetail(t, "ActivityDefinition", "reviewer", element.getReviewer().get(i), i);
    for (int i = 0; i < element.getEndorser().size(); i++)
      composeContactDetail(t, "ActivityDefinition", "endorser", element.getEndorser().get(i), i);
    for (int i = 0; i < element.getRelatedArtifact().size(); i++)
      composeRelatedArtifact(t, "ActivityDefinition", "relatedArtifact", element.getRelatedArtifact().get(i), i);
    for (int i = 0; i < element.getLibrary().size(); i++)
      composeCanonical(t, "ActivityDefinition", "library", element.getLibrary().get(i), i);
    if (element.hasKindElement())
      composeEnum(t, "ActivityDefinition", "kind", element.getKindElement(), -1);
    if (element.hasProfileElement())
      composeCanonical(t, "ActivityDefinition", "profile", element.getProfileElement(), -1);
    if (element.hasCode())
      composeCodeableConcept(t, "ActivityDefinition", "code", element.getCode(), -1);
    if (element.hasIntentElement())
      composeEnum(t, "ActivityDefinition", "intent", element.getIntentElement(), -1);
    if (element.hasPriorityElement())
      composeEnum(t, "ActivityDefinition", "priority", element.getPriorityElement(), -1);
    if (element.hasDoNotPerformElement())
      composeBoolean(t, "ActivityDefinition", "doNotPerform", element.getDoNotPerformElement(), -1);
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
    for (int i = 0; i < element.getSpecimenRequirement().size(); i++)
      composeReference(t, "ActivityDefinition", "specimenRequirement", element.getSpecimenRequirement().get(i), i);
    for (int i = 0; i < element.getObservationRequirement().size(); i++)
      composeReference(t, "ActivityDefinition", "observationRequirement", element.getObservationRequirement().get(i), i);
    for (int i = 0; i < element.getObservationResultRequirement().size(); i++)
      composeReference(t, "ActivityDefinition", "observationResultRequirement", element.getObservationResultRequirement().get(i), i);
    if (element.hasTransformElement())
      composeCanonical(t, "ActivityDefinition", "transform", element.getTransformElement(), -1);
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
    if (element.hasPathElement())
      composeString(t, "ActivityDefinition", "path", element.getPathElement(), -1);
    if (element.hasExpression())
      composeExpression(t, "ActivityDefinition", "expression", element.getExpression(), -1);
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
    for (int i = 0; i < element.getCategory().size(); i++)
      composeCodeableConcept(t, "AdverseEvent", "category", element.getCategory().get(i), i);
    if (element.hasEvent())
      composeCodeableConcept(t, "AdverseEvent", "event", element.getEvent(), -1);
    if (element.hasSubject())
      composeReference(t, "AdverseEvent", "subject", element.getSubject(), -1);
    if (element.hasContext())
      composeReference(t, "AdverseEvent", "context", element.getContext(), -1);
    if (element.hasDateElement())
      composeDateTime(t, "AdverseEvent", "date", element.getDateElement(), -1);
    if (element.hasDetectedElement())
      composeDateTime(t, "AdverseEvent", "detected", element.getDetectedElement(), -1);
    if (element.hasRecordedDateElement())
      composeDateTime(t, "AdverseEvent", "recordedDate", element.getRecordedDateElement(), -1);
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
    for (int i = 0; i < element.getContributor().size(); i++)
      composeReference(t, "AdverseEvent", "contributor", element.getContributor().get(i), i);
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
    if (element.hasEncounter())
      composeReference(t, "AllergyIntolerance", "encounter", element.getEncounter(), -1);
    if (element.hasOnset())
      composeType(t, "AllergyIntolerance", "onset", element.getOnset(), -1);
    if (element.hasRecordedDateElement())
      composeDateTime(t, "AllergyIntolerance", "recordedDate", element.getRecordedDateElement(), -1);
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
    for (int i = 0; i < element.getServiceCategory().size(); i++)
      composeCodeableConcept(t, "Appointment", "serviceCategory", element.getServiceCategory().get(i), i);
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
    if (element.hasPatientInstructionElement())
      composeString(t, "Appointment", "patientInstruction", element.getPatientInstructionElement(), -1);
    for (int i = 0; i < element.getBasedOn().size(); i++)
      composeReference(t, "Appointment", "basedOn", element.getBasedOn().get(i), i);
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
    if (element.hasPeriod())
      composePeriod(t, "Appointment", "period", element.getPeriod(), -1);
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
    if (element.hasWho())
      composeReference(t, "AuditEvent", "who", element.getWho(), -1);
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
    if (element.hasObserver())
      composeReference(t, "AuditEvent", "observer", element.getObserver(), -1);
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
    if (element.hasWhat())
      composeReference(t, "AuditEvent", "what", element.getWhat(), -1);
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
    if (element.hasDataElement())
      composeBase64Binary(t, "Binary", "data", element.getDataElement(), -1);
  }

  protected void composeBiologicallyDerivedProduct(Complex parent, String parentType, String name, BiologicallyDerivedProduct element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "BiologicallyDerivedProduct", name, element, index);
    for (int i = 0; i < element.getIdentifier().size(); i++)
      composeIdentifier(t, "BiologicallyDerivedProduct", "identifier", element.getIdentifier().get(i), i);
    if (element.hasProductCategoryElement())
      composeEnum(t, "BiologicallyDerivedProduct", "productCategory", element.getProductCategoryElement(), -1);
    if (element.hasProductCode())
      composeCodeableConcept(t, "BiologicallyDerivedProduct", "productCode", element.getProductCode(), -1);
    if (element.hasStatusElement())
      composeEnum(t, "BiologicallyDerivedProduct", "status", element.getStatusElement(), -1);
    for (int i = 0; i < element.getRequest().size(); i++)
      composeReference(t, "BiologicallyDerivedProduct", "request", element.getRequest().get(i), i);
    if (element.hasQuantityElement())
      composeInteger(t, "BiologicallyDerivedProduct", "quantity", element.getQuantityElement(), -1);
    if (element.hasParent())
      composeReference(t, "BiologicallyDerivedProduct", "parent", element.getParent(), -1);
    if (element.hasCollection())
      composeBiologicallyDerivedProductBiologicallyDerivedProductCollectionComponent(t, "BiologicallyDerivedProduct", "collection", element.getCollection(), -1);
    for (int i = 0; i < element.getProcessing().size(); i++)
      composeBiologicallyDerivedProductBiologicallyDerivedProductProcessingComponent(t, "BiologicallyDerivedProduct", "processing", element.getProcessing().get(i), i);
    if (element.hasManipulation())
      composeBiologicallyDerivedProductBiologicallyDerivedProductManipulationComponent(t, "BiologicallyDerivedProduct", "manipulation", element.getManipulation(), -1);
    for (int i = 0; i < element.getStorage().size(); i++)
      composeBiologicallyDerivedProductBiologicallyDerivedProductStorageComponent(t, "BiologicallyDerivedProduct", "storage", element.getStorage().get(i), i);
  }

  protected void composeBiologicallyDerivedProductBiologicallyDerivedProductCollectionComponent(Complex parent, String parentType, String name, BiologicallyDerivedProduct.BiologicallyDerivedProductCollectionComponent element, int index) {
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
      composeReference(t, "BiologicallyDerivedProduct", "collector", element.getCollector(), -1);
    if (element.hasSource())
      composeReference(t, "BiologicallyDerivedProduct", "source", element.getSource(), -1);
    if (element.hasCollected())
      composeType(t, "BiologicallyDerivedProduct", "collected", element.getCollected(), -1);
  }

  protected void composeBiologicallyDerivedProductBiologicallyDerivedProductProcessingComponent(Complex parent, String parentType, String name, BiologicallyDerivedProduct.BiologicallyDerivedProductProcessingComponent element, int index) {
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
      composeString(t, "BiologicallyDerivedProduct", "description", element.getDescriptionElement(), -1);
    if (element.hasProcedure())
      composeCodeableConcept(t, "BiologicallyDerivedProduct", "procedure", element.getProcedure(), -1);
    if (element.hasAdditive())
      composeReference(t, "BiologicallyDerivedProduct", "additive", element.getAdditive(), -1);
    if (element.hasTime())
      composeType(t, "BiologicallyDerivedProduct", "time", element.getTime(), -1);
  }

  protected void composeBiologicallyDerivedProductBiologicallyDerivedProductManipulationComponent(Complex parent, String parentType, String name, BiologicallyDerivedProduct.BiologicallyDerivedProductManipulationComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "manipulation", name, element, index);
    if (element.hasDescriptionElement())
      composeString(t, "BiologicallyDerivedProduct", "description", element.getDescriptionElement(), -1);
    if (element.hasTime())
      composeType(t, "BiologicallyDerivedProduct", "time", element.getTime(), -1);
  }

  protected void composeBiologicallyDerivedProductBiologicallyDerivedProductStorageComponent(Complex parent, String parentType, String name, BiologicallyDerivedProduct.BiologicallyDerivedProductStorageComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "storage", name, element, index);
    if (element.hasDescriptionElement())
      composeString(t, "BiologicallyDerivedProduct", "description", element.getDescriptionElement(), -1);
    if (element.hasTemperatureElement())
      composeDecimal(t, "BiologicallyDerivedProduct", "temperature", element.getTemperatureElement(), -1);
    if (element.hasScaleElement())
      composeEnum(t, "BiologicallyDerivedProduct", "scale", element.getScaleElement(), -1);
    if (element.hasDuration())
      composePeriod(t, "BiologicallyDerivedProduct", "duration", element.getDuration(), -1);
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
    if (element.hasTimestampElement())
      composeInstant(t, "Bundle", "timestamp", element.getTimestampElement(), -1);
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
      composeCanonical(t, "CapabilityStatement", "instantiates", element.getInstantiates().get(i), i);
    for (int i = 0; i < element.getImports().size(); i++)
      composeCanonical(t, "CapabilityStatement", "imports", element.getImports().get(i), i);
    if (element.hasSoftware())
      composeCapabilityStatementCapabilityStatementSoftwareComponent(t, "CapabilityStatement", "software", element.getSoftware(), -1);
    if (element.hasImplementation())
      composeCapabilityStatementCapabilityStatementImplementationComponent(t, "CapabilityStatement", "implementation", element.getImplementation(), -1);
    if (element.hasFhirVersionElement())
      composeId(t, "CapabilityStatement", "fhirVersion", element.getFhirVersionElement(), -1);
    for (int i = 0; i < element.getFormat().size(); i++)
      composeCode(t, "CapabilityStatement", "format", element.getFormat().get(i), i);
    for (int i = 0; i < element.getPatchFormat().size(); i++)
      composeCode(t, "CapabilityStatement", "patchFormat", element.getPatchFormat().get(i), i);
    for (int i = 0; i < element.getImplementationGuide().size(); i++)
      composeCanonical(t, "CapabilityStatement", "implementationGuide", element.getImplementationGuide().get(i), i);
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
      composeUrl(t, "CapabilityStatement", "url", element.getUrlElement(), -1);
    if (element.hasCustodian())
      composeReference(t, "CapabilityStatement", "custodian", element.getCustodian(), -1);
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
      composeMarkdown(t, "CapabilityStatement", "documentation", element.getDocumentationElement(), -1);
    if (element.hasSecurity())
      composeCapabilityStatementCapabilityStatementRestSecurityComponent(t, "CapabilityStatement", "security", element.getSecurity(), -1);
    for (int i = 0; i < element.getResource().size(); i++)
      composeCapabilityStatementCapabilityStatementRestResourceComponent(t, "CapabilityStatement", "resource", element.getResource().get(i), i);
    for (int i = 0; i < element.getInteraction().size(); i++)
      composeCapabilityStatementSystemInteractionComponent(t, "CapabilityStatement", "interaction", element.getInteraction().get(i), i);
    for (int i = 0; i < element.getSearchParam().size(); i++)
      composeCapabilityStatementCapabilityStatementRestResourceSearchParamComponent(t, "CapabilityStatement", "searchParam", element.getSearchParam().get(i), i);
    for (int i = 0; i < element.getOperation().size(); i++)
      composeCapabilityStatementCapabilityStatementRestResourceOperationComponent(t, "CapabilityStatement", "operation", element.getOperation().get(i), i);
    for (int i = 0; i < element.getCompartment().size(); i++)
      composeCanonical(t, "CapabilityStatement", "compartment", element.getCompartment().get(i), i);
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
      composeMarkdown(t, "CapabilityStatement", "description", element.getDescriptionElement(), -1);
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
    if (element.hasProfileElement())
      composeCanonical(t, "CapabilityStatement", "profile", element.getProfileElement(), -1);
    for (int i = 0; i < element.getSupportedProfile().size(); i++)
      composeCanonical(t, "CapabilityStatement", "supportedProfile", element.getSupportedProfile().get(i), i);
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
    for (int i = 0; i < element.getOperation().size(); i++)
      composeCapabilityStatementCapabilityStatementRestResourceOperationComponent(t, "CapabilityStatement", "operation", element.getOperation().get(i), i);
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
      composeMarkdown(t, "CapabilityStatement", "documentation", element.getDocumentationElement(), -1);
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
      composeCanonical(t, "CapabilityStatement", "definition", element.getDefinitionElement(), -1);
    if (element.hasTypeElement())
      composeEnum(t, "CapabilityStatement", "type", element.getTypeElement(), -1);
    if (element.hasDocumentationElement())
      composeMarkdown(t, "CapabilityStatement", "documentation", element.getDocumentationElement(), -1);
  }

  protected void composeCapabilityStatementCapabilityStatementRestResourceOperationComponent(Complex parent, String parentType, String name, CapabilityStatement.CapabilityStatementRestResourceOperationComponent element, int index) {
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
    if (element.hasDefinitionElement())
      composeCanonical(t, "CapabilityStatement", "definition", element.getDefinitionElement(), -1);
    if (element.hasDocumentationElement())
      composeMarkdown(t, "CapabilityStatement", "documentation", element.getDocumentationElement(), -1);
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
      composeMarkdown(t, "CapabilityStatement", "documentation", element.getDocumentationElement(), -1);
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
      composeMarkdown(t, "CapabilityStatement", "documentation", element.getDocumentationElement(), -1);
    for (int i = 0; i < element.getSupportedMessage().size(); i++)
      composeCapabilityStatementCapabilityStatementMessagingSupportedMessageComponent(t, "CapabilityStatement", "supportedMessage", element.getSupportedMessage().get(i), i);
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
      composeUrl(t, "CapabilityStatement", "address", element.getAddressElement(), -1);
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
    if (element.hasDefinitionElement())
      composeCanonical(t, "CapabilityStatement", "definition", element.getDefinitionElement(), -1);
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
      composeMarkdown(t, "CapabilityStatement", "documentation", element.getDocumentationElement(), -1);
    if (element.hasProfileElement())
      composeCanonical(t, "CapabilityStatement", "profile", element.getProfileElement(), -1);
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
    for (int i = 0; i < element.getInstantiatesCanonical().size(); i++)
      composeCanonical(t, "CarePlan", "instantiatesCanonical", element.getInstantiatesCanonical().get(i), i);
    for (int i = 0; i < element.getInstantiatesUri().size(); i++)
      composeUri(t, "CarePlan", "instantiatesUri", element.getInstantiatesUri().get(i), i);
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
    if (element.hasCreatedElement())
      composeDateTime(t, "CarePlan", "created", element.getCreatedElement(), -1);
    if (element.hasAuthor())
      composeReference(t, "CarePlan", "author", element.getAuthor(), -1);
    for (int i = 0; i < element.getContributor().size(); i++)
      composeReference(t, "CarePlan", "contributor", element.getContributor().get(i), i);
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
    for (int i = 0; i < element.getInstantiatesCanonical().size(); i++)
      composeCanonical(t, "CarePlan", "instantiatesCanonical", element.getInstantiatesCanonical().get(i), i);
    for (int i = 0; i < element.getInstantiatesUri().size(); i++)
      composeUri(t, "CarePlan", "instantiatesUri", element.getInstantiatesUri().get(i), i);
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
    if (element.hasDoNotPerformElement())
      composeBoolean(t, "CarePlan", "doNotPerform", element.getDoNotPerformElement(), -1);
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
    for (int i = 0; i < element.getRole().size(); i++)
      composeCodeableConcept(t, "CareTeam", "role", element.getRole().get(i), i);
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
    for (int i = 0; i < element.getIdentifier().size(); i++)
      composeIdentifier(t, "CatalogEntry", "identifier", element.getIdentifier().get(i), i);
    if (element.hasType())
      composeCodeableConcept(t, "CatalogEntry", "type", element.getType(), -1);
    if (element.hasOrderableElement())
      composeBoolean(t, "CatalogEntry", "orderable", element.getOrderableElement(), -1);
    if (element.hasReferencedItem())
      composeReference(t, "CatalogEntry", "referencedItem", element.getReferencedItem(), -1);
    for (int i = 0; i < element.getAdditionalIdentifier().size(); i++)
      composeIdentifier(t, "CatalogEntry", "additionalIdentifier", element.getAdditionalIdentifier().get(i), i);
    for (int i = 0; i < element.getClassification().size(); i++)
      composeCodeableConcept(t, "CatalogEntry", "classification", element.getClassification().get(i), i);
    if (element.hasStatusElement())
      composeEnum(t, "CatalogEntry", "status", element.getStatusElement(), -1);
    if (element.hasValidityPeriod())
      composePeriod(t, "CatalogEntry", "validityPeriod", element.getValidityPeriod(), -1);
    if (element.hasValidToElement())
      composeDateTime(t, "CatalogEntry", "validTo", element.getValidToElement(), -1);
    if (element.hasLastUpdatedElement())
      composeDateTime(t, "CatalogEntry", "lastUpdated", element.getLastUpdatedElement(), -1);
    for (int i = 0; i < element.getAdditionalCharacteristic().size(); i++)
      composeCodeableConcept(t, "CatalogEntry", "additionalCharacteristic", element.getAdditionalCharacteristic().get(i), i);
    for (int i = 0; i < element.getAdditionalClassification().size(); i++)
      composeCodeableConcept(t, "CatalogEntry", "additionalClassification", element.getAdditionalClassification().get(i), i);
    for (int i = 0; i < element.getRelatedEntry().size(); i++)
      composeCatalogEntryCatalogEntryRelatedEntryComponent(t, "CatalogEntry", "relatedEntry", element.getRelatedEntry().get(i), i);
  }

  protected void composeCatalogEntryCatalogEntryRelatedEntryComponent(Complex parent, String parentType, String name, CatalogEntry.CatalogEntryRelatedEntryComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "relatedEntry", name, element, index);
    if (element.hasRelationtypeElement())
      composeEnum(t, "CatalogEntry", "relationtype", element.getRelationtypeElement(), -1);
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
    for (int i = 0; i < element.getIdentifier().size(); i++)
      composeIdentifier(t, "ChargeItem", "identifier", element.getIdentifier().get(i), i);
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
    for (int i = 0; i < element.getPerformer().size(); i++)
      composeChargeItemChargeItemPerformerComponent(t, "ChargeItem", "performer", element.getPerformer().get(i), i);
    if (element.hasPerformingOrganization())
      composeReference(t, "ChargeItem", "performingOrganization", element.getPerformingOrganization(), -1);
    if (element.hasRequestingOrganization())
      composeReference(t, "ChargeItem", "requestingOrganization", element.getRequestingOrganization(), -1);
    if (element.hasCostCenter())
      composeReference(t, "ChargeItem", "costCenter", element.getCostCenter(), -1);
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
    if (element.hasProduct())
      composeType(t, "ChargeItem", "product", element.getProduct(), -1);
    for (int i = 0; i < element.getAccount().size(); i++)
      composeReference(t, "ChargeItem", "account", element.getAccount().get(i), i);
    for (int i = 0; i < element.getNote().size(); i++)
      composeAnnotation(t, "ChargeItem", "note", element.getNote().get(i), i);
    for (int i = 0; i < element.getSupportingInformation().size(); i++)
      composeReference(t, "ChargeItem", "supportingInformation", element.getSupportingInformation().get(i), i);
  }

  protected void composeChargeItemChargeItemPerformerComponent(Complex parent, String parentType, String name, ChargeItem.ChargeItemPerformerComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "performer", name, element, index);
    if (element.hasFunction())
      composeCodeableConcept(t, "ChargeItem", "function", element.getFunction(), -1);
    if (element.hasActor())
      composeReference(t, "ChargeItem", "actor", element.getActor(), -1);
  }

  protected void composeChargeItemDefinition(Complex parent, String parentType, String name, ChargeItemDefinition element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "ChargeItemDefinition", name, element, index);
    if (element.hasUrlElement())
      composeUri(t, "ChargeItemDefinition", "url", element.getUrlElement(), -1);
    for (int i = 0; i < element.getIdentifier().size(); i++)
      composeIdentifier(t, "ChargeItemDefinition", "identifier", element.getIdentifier().get(i), i);
    if (element.hasVersionElement())
      composeString(t, "ChargeItemDefinition", "version", element.getVersionElement(), -1);
    if (element.hasTitleElement())
      composeString(t, "ChargeItemDefinition", "title", element.getTitleElement(), -1);
    for (int i = 0; i < element.getDerivedFromUri().size(); i++)
      composeUri(t, "ChargeItemDefinition", "derivedFromUri", element.getDerivedFromUri().get(i), i);
    for (int i = 0; i < element.getPartOf().size(); i++)
      composeCanonical(t, "ChargeItemDefinition", "partOf", element.getPartOf().get(i), i);
    for (int i = 0; i < element.getReplaces().size(); i++)
      composeCanonical(t, "ChargeItemDefinition", "replaces", element.getReplaces().get(i), i);
    if (element.hasStatusElement())
      composeEnum(t, "ChargeItemDefinition", "status", element.getStatusElement(), -1);
    if (element.hasExperimentalElement())
      composeBoolean(t, "ChargeItemDefinition", "experimental", element.getExperimentalElement(), -1);
    if (element.hasDateElement())
      composeDateTime(t, "ChargeItemDefinition", "date", element.getDateElement(), -1);
    if (element.hasPublisherElement())
      composeString(t, "ChargeItemDefinition", "publisher", element.getPublisherElement(), -1);
    for (int i = 0; i < element.getContact().size(); i++)
      composeContactDetail(t, "ChargeItemDefinition", "contact", element.getContact().get(i), i);
    if (element.hasDescriptionElement())
      composeMarkdown(t, "ChargeItemDefinition", "description", element.getDescriptionElement(), -1);
    for (int i = 0; i < element.getUseContext().size(); i++)
      composeUsageContext(t, "ChargeItemDefinition", "useContext", element.getUseContext().get(i), i);
    for (int i = 0; i < element.getJurisdiction().size(); i++)
      composeCodeableConcept(t, "ChargeItemDefinition", "jurisdiction", element.getJurisdiction().get(i), i);
    if (element.hasCopyrightElement())
      composeMarkdown(t, "ChargeItemDefinition", "copyright", element.getCopyrightElement(), -1);
    if (element.hasApprovalDateElement())
      composeDate(t, "ChargeItemDefinition", "approvalDate", element.getApprovalDateElement(), -1);
    if (element.hasLastReviewDateElement())
      composeDate(t, "ChargeItemDefinition", "lastReviewDate", element.getLastReviewDateElement(), -1);
    if (element.hasEffectivePeriod())
      composePeriod(t, "ChargeItemDefinition", "effectivePeriod", element.getEffectivePeriod(), -1);
    if (element.hasCode())
      composeCodeableConcept(t, "ChargeItemDefinition", "code", element.getCode(), -1);
    for (int i = 0; i < element.getInstance().size(); i++)
      composeReference(t, "ChargeItemDefinition", "instance", element.getInstance().get(i), i);
    for (int i = 0; i < element.getApplicability().size(); i++)
      composeChargeItemDefinitionChargeItemDefinitionApplicabilityComponent(t, "ChargeItemDefinition", "applicability", element.getApplicability().get(i), i);
    for (int i = 0; i < element.getPropertyGroup().size(); i++)
      composeChargeItemDefinitionChargeItemDefinitionPropertyGroupComponent(t, "ChargeItemDefinition", "propertyGroup", element.getPropertyGroup().get(i), i);
  }

  protected void composeChargeItemDefinitionChargeItemDefinitionApplicabilityComponent(Complex parent, String parentType, String name, ChargeItemDefinition.ChargeItemDefinitionApplicabilityComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "applicability", name, element, index);
    if (element.hasDescriptionElement())
      composeString(t, "ChargeItemDefinition", "description", element.getDescriptionElement(), -1);
    if (element.hasLanguageElement())
      composeString(t, "ChargeItemDefinition", "language", element.getLanguageElement(), -1);
    if (element.hasExpressionElement())
      composeString(t, "ChargeItemDefinition", "expression", element.getExpressionElement(), -1);
  }

  protected void composeChargeItemDefinitionChargeItemDefinitionPropertyGroupComponent(Complex parent, String parentType, String name, ChargeItemDefinition.ChargeItemDefinitionPropertyGroupComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "propertyGroup", name, element, index);
    for (int i = 0; i < element.getApplicability().size(); i++)
      composeChargeItemDefinitionChargeItemDefinitionApplicabilityComponent(t, "ChargeItemDefinition", "applicability", element.getApplicability().get(i), i);
    for (int i = 0; i < element.getPriceComponent().size(); i++)
      composeChargeItemDefinitionChargeItemDefinitionPropertyGroupPriceComponentComponent(t, "ChargeItemDefinition", "priceComponent", element.getPriceComponent().get(i), i);
  }

  protected void composeChargeItemDefinitionChargeItemDefinitionPropertyGroupPriceComponentComponent(Complex parent, String parentType, String name, ChargeItemDefinition.ChargeItemDefinitionPropertyGroupPriceComponentComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "priceComponent", name, element, index);
    if (element.hasTypeElement())
      composeEnum(t, "ChargeItemDefinition", "type", element.getTypeElement(), -1);
    if (element.hasCode())
      composeCodeableConcept(t, "ChargeItemDefinition", "code", element.getCode(), -1);
    if (element.hasFactorElement())
      composeDecimal(t, "ChargeItemDefinition", "factor", element.getFactorElement(), -1);
    if (element.hasAmount())
      composeMoney(t, "ChargeItemDefinition", "amount", element.getAmount(), -1);
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
    if (element.hasSubType())
      composeCodeableConcept(t, "Claim", "subType", element.getSubType(), -1);
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
    if (element.hasOnAdmission())
      composeCodeableConcept(t, "Claim", "onAdmission", element.getOnAdmission(), -1);
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
    for (int i = 0; i < element.getCareTeamSequence().size(); i++)
      composePositiveInt(t, "Claim", "careTeamSequence", element.getCareTeamSequence().get(i), i);
    for (int i = 0; i < element.getDiagnosisSequence().size(); i++)
      composePositiveInt(t, "Claim", "diagnosisSequence", element.getDiagnosisSequence().get(i), i);
    for (int i = 0; i < element.getProcedureSequence().size(); i++)
      composePositiveInt(t, "Claim", "procedureSequence", element.getProcedureSequence().get(i), i);
    for (int i = 0; i < element.getInformationSequence().size(); i++)
      composePositiveInt(t, "Claim", "informationSequence", element.getInformationSequence().get(i), i);
    if (element.hasRevenue())
      composeCodeableConcept(t, "Claim", "revenue", element.getRevenue(), -1);
    if (element.hasCategory())
      composeCodeableConcept(t, "Claim", "category", element.getCategory(), -1);
    if (element.hasBillcode())
      composeCodeableConcept(t, "Claim", "billcode", element.getBillcode(), -1);
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
    if (element.hasBillcode())
      composeCodeableConcept(t, "Claim", "billcode", element.getBillcode(), -1);
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
    if (element.hasBillcode())
      composeCodeableConcept(t, "Claim", "billcode", element.getBillcode(), -1);
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
    if (element.hasType())
      composeCodeableConcept(t, "ClaimResponse", "type", element.getType(), -1);
    if (element.hasSubType())
      composeCodeableConcept(t, "ClaimResponse", "subType", element.getSubType(), -1);
    if (element.hasUseElement())
      composeEnum(t, "ClaimResponse", "use", element.getUseElement(), -1);
    if (element.hasPatient())
      composeReference(t, "ClaimResponse", "patient", element.getPatient(), -1);
    if (element.hasCreatedElement())
      composeDateTime(t, "ClaimResponse", "created", element.getCreatedElement(), -1);
    if (element.hasInsurer())
      composeReference(t, "ClaimResponse", "insurer", element.getInsurer(), -1);
    if (element.hasRequestProvider())
      composeReference(t, "ClaimResponse", "requestProvider", element.getRequestProvider(), -1);
    if (element.hasRequest())
      composeReference(t, "ClaimResponse", "request", element.getRequest(), -1);
    if (element.hasOutcomeElement())
      composeEnum(t, "ClaimResponse", "outcome", element.getOutcomeElement(), -1);
    if (element.hasDispositionElement())
      composeString(t, "ClaimResponse", "disposition", element.getDispositionElement(), -1);
    if (element.hasPreAuthRefElement())
      composeString(t, "ClaimResponse", "preAuthRef", element.getPreAuthRefElement(), -1);
    if (element.hasPayeeType())
      composeCodeableConcept(t, "ClaimResponse", "payeeType", element.getPayeeType(), -1);
    for (int i = 0; i < element.getItem().size(); i++)
      composeClaimResponseItemComponent(t, "ClaimResponse", "item", element.getItem().get(i), i);
    for (int i = 0; i < element.getAddItem().size(); i++)
      composeClaimResponseAddedItemComponent(t, "ClaimResponse", "addItem", element.getAddItem().get(i), i);
    for (int i = 0; i < element.getError().size(); i++)
      composeClaimResponseErrorComponent(t, "ClaimResponse", "error", element.getError().get(i), i);
    for (int i = 0; i < element.getTotal().size(); i++)
      composeClaimResponseTotalComponent(t, "ClaimResponse", "total", element.getTotal().get(i), i);
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
    if (element.hasItemSequenceElement())
      composePositiveInt(t, "ClaimResponse", "itemSequence", element.getItemSequenceElement(), -1);
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
    if (element.hasDetailSequenceElement())
      composePositiveInt(t, "ClaimResponse", "detailSequence", element.getDetailSequenceElement(), -1);
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
    if (element.hasSubDetailSequenceElement())
      composePositiveInt(t, "ClaimResponse", "subDetailSequence", element.getSubDetailSequenceElement(), -1);
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
    for (int i = 0; i < element.getItemSequence().size(); i++)
      composePositiveInt(t, "ClaimResponse", "itemSequence", element.getItemSequence().get(i), i);
    for (int i = 0; i < element.getDetailSequence().size(); i++)
      composePositiveInt(t, "ClaimResponse", "detailSequence", element.getDetailSequence().get(i), i);
    for (int i = 0; i < element.getSubdetailSequence().size(); i++)
      composePositiveInt(t, "ClaimResponse", "subdetailSequence", element.getSubdetailSequence().get(i), i);
    for (int i = 0; i < element.getProvider().size(); i++)
      composeReference(t, "ClaimResponse", "provider", element.getProvider().get(i), i);
    if (element.hasBillcode())
      composeCodeableConcept(t, "ClaimResponse", "billcode", element.getBillcode(), -1);
    for (int i = 0; i < element.getModifier().size(); i++)
      composeCodeableConcept(t, "ClaimResponse", "modifier", element.getModifier().get(i), i);
    for (int i = 0; i < element.getProgramCode().size(); i++)
      composeCodeableConcept(t, "ClaimResponse", "programCode", element.getProgramCode().get(i), i);
    if (element.hasServiced())
      composeType(t, "ClaimResponse", "serviced", element.getServiced(), -1);
    if (element.hasLocation())
      composeType(t, "ClaimResponse", "location", element.getLocation(), -1);
    if (element.hasQuantity())
      composeQuantity(t, "ClaimResponse", "quantity", element.getQuantity(), -1);
    if (element.hasUnitPrice())
      composeMoney(t, "ClaimResponse", "unitPrice", element.getUnitPrice(), -1);
    if (element.hasFactorElement())
      composeDecimal(t, "ClaimResponse", "factor", element.getFactorElement(), -1);
    if (element.hasNet())
      composeMoney(t, "ClaimResponse", "net", element.getNet(), -1);
    if (element.hasBodySite())
      composeCodeableConcept(t, "ClaimResponse", "bodySite", element.getBodySite(), -1);
    for (int i = 0; i < element.getSubSite().size(); i++)
      composeCodeableConcept(t, "ClaimResponse", "subSite", element.getSubSite().get(i), i);
    for (int i = 0; i < element.getNoteNumber().size(); i++)
      composePositiveInt(t, "ClaimResponse", "noteNumber", element.getNoteNumber().get(i), i);
    for (int i = 0; i < element.getAdjudication().size(); i++)
      composeClaimResponseAdjudicationComponent(t, "ClaimResponse", "adjudication", element.getAdjudication().get(i), i);
    for (int i = 0; i < element.getDetail().size(); i++)
      composeClaimResponseAddedItemDetailComponent(t, "ClaimResponse", "detail", element.getDetail().get(i), i);
  }

  protected void composeClaimResponseAddedItemDetailComponent(Complex parent, String parentType, String name, ClaimResponse.AddedItemDetailComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "detail", name, element, index);
    if (element.hasBillcode())
      composeCodeableConcept(t, "ClaimResponse", "billcode", element.getBillcode(), -1);
    for (int i = 0; i < element.getModifier().size(); i++)
      composeCodeableConcept(t, "ClaimResponse", "modifier", element.getModifier().get(i), i);
    if (element.hasQuantity())
      composeQuantity(t, "ClaimResponse", "quantity", element.getQuantity(), -1);
    if (element.hasUnitPrice())
      composeMoney(t, "ClaimResponse", "unitPrice", element.getUnitPrice(), -1);
    if (element.hasFactorElement())
      composeDecimal(t, "ClaimResponse", "factor", element.getFactorElement(), -1);
    if (element.hasNet())
      composeMoney(t, "ClaimResponse", "net", element.getNet(), -1);
    for (int i = 0; i < element.getNoteNumber().size(); i++)
      composePositiveInt(t, "ClaimResponse", "noteNumber", element.getNoteNumber().get(i), i);
    for (int i = 0; i < element.getAdjudication().size(); i++)
      composeClaimResponseAdjudicationComponent(t, "ClaimResponse", "adjudication", element.getAdjudication().get(i), i);
    for (int i = 0; i < element.getSubDetail().size(); i++)
      composeClaimResponseAddedItemSubDetailComponent(t, "ClaimResponse", "subDetail", element.getSubDetail().get(i), i);
  }

  protected void composeClaimResponseAddedItemSubDetailComponent(Complex parent, String parentType, String name, ClaimResponse.AddedItemSubDetailComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "subDetail", name, element, index);
    if (element.hasBillcode())
      composeCodeableConcept(t, "ClaimResponse", "billcode", element.getBillcode(), -1);
    for (int i = 0; i < element.getModifier().size(); i++)
      composeCodeableConcept(t, "ClaimResponse", "modifier", element.getModifier().get(i), i);
    if (element.hasQuantity())
      composeQuantity(t, "ClaimResponse", "quantity", element.getQuantity(), -1);
    if (element.hasUnitPrice())
      composeMoney(t, "ClaimResponse", "unitPrice", element.getUnitPrice(), -1);
    if (element.hasFactorElement())
      composeDecimal(t, "ClaimResponse", "factor", element.getFactorElement(), -1);
    if (element.hasNet())
      composeMoney(t, "ClaimResponse", "net", element.getNet(), -1);
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
    if (element.hasItemSequenceElement())
      composePositiveInt(t, "ClaimResponse", "itemSequence", element.getItemSequenceElement(), -1);
    if (element.hasDetailSequenceElement())
      composePositiveInt(t, "ClaimResponse", "detailSequence", element.getDetailSequenceElement(), -1);
    if (element.hasSubDetailSequenceElement())
      composePositiveInt(t, "ClaimResponse", "subDetailSequence", element.getSubDetailSequenceElement(), -1);
    if (element.hasCode())
      composeCodeableConcept(t, "ClaimResponse", "code", element.getCode(), -1);
  }

  protected void composeClaimResponseTotalComponent(Complex parent, String parentType, String name, ClaimResponse.TotalComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "total", name, element, index);
    if (element.hasCategory())
      composeCodeableConcept(t, "ClaimResponse", "category", element.getCategory(), -1);
    if (element.hasAmount())
      composeMoney(t, "ClaimResponse", "amount", element.getAmount(), -1);
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
    if (element.hasStatusReason())
      composeCodeableConcept(t, "ClinicalImpression", "statusReason", element.getStatusReason(), -1);
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
    for (int i = 0; i < element.getSupportingInfo().size(); i++)
      composeReference(t, "ClinicalImpression", "supportingInfo", element.getSupportingInfo().get(i), i);
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
    if (element.hasItemCodeableConcept())
      composeCodeableConcept(t, "ClinicalImpression", "itemCodeableConcept", element.getItemCodeableConcept(), -1);
    if (element.hasItemReference())
      composeReference(t, "ClinicalImpression", "itemReference", element.getItemReference(), -1);
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
    for (int i = 0; i < element.getIdentifier().size(); i++)
      composeIdentifier(t, "CodeSystem", "identifier", element.getIdentifier().get(i), i);
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
      composeCanonical(t, "CodeSystem", "valueSet", element.getValueSetElement(), -1);
    if (element.hasHierarchyMeaningElement())
      composeEnum(t, "CodeSystem", "hierarchyMeaning", element.getHierarchyMeaningElement(), -1);
    if (element.hasCompositionalElement())
      composeBoolean(t, "CodeSystem", "compositional", element.getCompositionalElement(), -1);
    if (element.hasVersionNeededElement())
      composeBoolean(t, "CodeSystem", "versionNeeded", element.getVersionNeededElement(), -1);
    if (element.hasContentElement())
      composeEnum(t, "CodeSystem", "content", element.getContentElement(), -1);
    if (element.hasSupplementsElement())
      composeCanonical(t, "CodeSystem", "supplements", element.getSupplementsElement(), -1);
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
    for (int i = 0; i < element.getInstantiatesCanonical().size(); i++)
      composeCanonical(t, "Communication", "instantiatesCanonical", element.getInstantiatesCanonical().get(i), i);
    for (int i = 0; i < element.getInstantiatesUri().size(); i++)
      composeUri(t, "Communication", "instantiatesUri", element.getInstantiatesUri().get(i), i);
    for (int i = 0; i < element.getBasedOn().size(); i++)
      composeReference(t, "Communication", "basedOn", element.getBasedOn().get(i), i);
    for (int i = 0; i < element.getPartOf().size(); i++)
      composeReference(t, "Communication", "partOf", element.getPartOf().get(i), i);
    for (int i = 0; i < element.getInResponseTo().size(); i++)
      composeReference(t, "Communication", "inResponseTo", element.getInResponseTo().get(i), i);
    if (element.hasStatusElement())
      composeEnum(t, "Communication", "status", element.getStatusElement(), -1);
    if (element.hasStatusReason())
      composeCodeableConcept(t, "Communication", "statusReason", element.getStatusReason(), -1);
    for (int i = 0; i < element.getCategory().size(); i++)
      composeCodeableConcept(t, "Communication", "category", element.getCategory().get(i), i);
    if (element.hasPriorityElement())
      composeEnum(t, "Communication", "priority", element.getPriorityElement(), -1);
    for (int i = 0; i < element.getMedium().size(); i++)
      composeCodeableConcept(t, "Communication", "medium", element.getMedium().get(i), i);
    if (element.hasSubject())
      composeReference(t, "Communication", "subject", element.getSubject(), -1);
    if (element.hasTopic())
      composeCodeableConcept(t, "Communication", "topic", element.getTopic(), -1);
    for (int i = 0; i < element.getAbout().size(); i++)
      composeReference(t, "Communication", "about", element.getAbout().get(i), i);
    if (element.hasContext())
      composeReference(t, "Communication", "context", element.getContext(), -1);
    if (element.hasSentElement())
      composeDateTime(t, "Communication", "sent", element.getSentElement(), -1);
    if (element.hasReceivedElement())
      composeDateTime(t, "Communication", "received", element.getReceivedElement(), -1);
    for (int i = 0; i < element.getRecipient().size(); i++)
      composeReference(t, "Communication", "recipient", element.getRecipient().get(i), i);
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
    if (element.hasStatusReason())
      composeCodeableConcept(t, "CommunicationRequest", "statusReason", element.getStatusReason(), -1);
    for (int i = 0; i < element.getCategory().size(); i++)
      composeCodeableConcept(t, "CommunicationRequest", "category", element.getCategory().get(i), i);
    if (element.hasPriorityElement())
      composeEnum(t, "CommunicationRequest", "priority", element.getPriorityElement(), -1);
    if (element.hasDoNotPerformElement())
      composeBoolean(t, "CommunicationRequest", "doNotPerform", element.getDoNotPerformElement(), -1);
    for (int i = 0; i < element.getMedium().size(); i++)
      composeCodeableConcept(t, "CommunicationRequest", "medium", element.getMedium().get(i), i);
    if (element.hasSubject())
      composeReference(t, "CommunicationRequest", "subject", element.getSubject(), -1);
    for (int i = 0; i < element.getAbout().size(); i++)
      composeReference(t, "CommunicationRequest", "about", element.getAbout().get(i), i);
    if (element.hasContext())
      composeReference(t, "CommunicationRequest", "context", element.getContext(), -1);
    for (int i = 0; i < element.getPayload().size(); i++)
      composeCommunicationRequestCommunicationRequestPayloadComponent(t, "CommunicationRequest", "payload", element.getPayload().get(i), i);
    if (element.hasOccurrence())
      composeType(t, "CommunicationRequest", "occurrence", element.getOccurrence(), -1);
    if (element.hasAuthoredOnElement())
      composeDateTime(t, "CommunicationRequest", "authoredOn", element.getAuthoredOnElement(), -1);
    if (element.hasRequester())
      composeReference(t, "CommunicationRequest", "requester", element.getRequester(), -1);
    for (int i = 0; i < element.getRecipient().size(); i++)
      composeReference(t, "CommunicationRequest", "recipient", element.getRecipient().get(i), i);
    if (element.hasSender())
      composeReference(t, "CommunicationRequest", "sender", element.getSender(), -1);
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
    if (element.hasVersionElement())
      composeString(t, "CompartmentDefinition", "version", element.getVersionElement(), -1);
    if (element.hasNameElement())
      composeString(t, "CompartmentDefinition", "name", element.getNameElement(), -1);
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
    for (int i = 0; i < element.getUseContext().size(); i++)
      composeUsageContext(t, "CompartmentDefinition", "useContext", element.getUseContext().get(i), i);
    if (element.hasPurposeElement())
      composeMarkdown(t, "CompartmentDefinition", "purpose", element.getPurposeElement(), -1);
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
    for (int i = 0; i < element.getCategory().size(); i++)
      composeCodeableConcept(t, "Composition", "category", element.getCategory().get(i), i);
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
    if (element.hasModeElement())
      composeEnum(t, "Composition", "mode", element.getModeElement(), -1);
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
    for (int i = 0; i < element.getAuthor().size(); i++)
      composeReference(t, "Composition", "author", element.getAuthor().get(i), i);
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
      composeCanonical(t, "ConceptMap", "system", element.getSystemElement(), -1);
    if (element.hasValueElement())
      composeString(t, "ConceptMap", "value", element.getValueElement(), -1);
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
      composeCanonical(t, "ConceptMap", "url", element.getUrlElement(), -1);
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
    if (element.hasClinicalStatus())
      composeCodeableConcept(t, "Condition", "clinicalStatus", element.getClinicalStatus(), -1);
    if (element.hasVerificationStatus())
      composeCodeableConcept(t, "Condition", "verificationStatus", element.getVerificationStatus(), -1);
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
    if (element.hasRecordedDateElement())
      composeDateTime(t, "Condition", "recordedDate", element.getRecordedDateElement(), -1);
    if (element.hasRecorder())
      composeReference(t, "Condition", "recorder", element.getRecorder(), -1);
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
    for (int i = 0; i < element.getIdentifier().size(); i++)
      composeIdentifier(t, "Consent", "identifier", element.getIdentifier().get(i), i);
    if (element.hasStatusElement())
      composeEnum(t, "Consent", "status", element.getStatusElement(), -1);
    if (element.hasScope())
      composeCodeableConcept(t, "Consent", "scope", element.getScope(), -1);
    for (int i = 0; i < element.getCategory().size(); i++)
      composeCodeableConcept(t, "Consent", "category", element.getCategory().get(i), i);
    if (element.hasPatient())
      composeReference(t, "Consent", "patient", element.getPatient(), -1);
    if (element.hasDateTimeElement())
      composeDateTime(t, "Consent", "dateTime", element.getDateTimeElement(), -1);
    for (int i = 0; i < element.getPerformer().size(); i++)
      composeReference(t, "Consent", "performer", element.getPerformer().get(i), i);
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
    if (element.hasProvision())
      composeConsentprovisionComponent(t, "Consent", "provision", element.getProvision(), -1);
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
      composeCodeableConcept(t, "Consent", "code", element.getCode().get(i), i);
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
    for (int i = 0; i < element.getIdentifier().size(); i++)
      composeIdentifier(t, "Contract", "identifier", element.getIdentifier().get(i), i);
    if (element.hasUrlElement())
      composeUri(t, "Contract", "url", element.getUrlElement(), -1);
    if (element.hasVersionElement())
      composeString(t, "Contract", "version", element.getVersionElement(), -1);
    if (element.hasStatusElement())
      composeEnum(t, "Contract", "status", element.getStatusElement(), -1);
    if (element.hasLegalState())
      composeCodeableConcept(t, "Contract", "legalState", element.getLegalState(), -1);
    if (element.hasInstantiatesCanonical())
      composeReference(t, "Contract", "instantiatesCanonical", element.getInstantiatesCanonical(), -1);
    if (element.hasInstantiatesUriElement())
      composeUri(t, "Contract", "instantiatesUri", element.getInstantiatesUriElement(), -1);
    if (element.hasContentDerivative())
      composeCodeableConcept(t, "Contract", "contentDerivative", element.getContentDerivative(), -1);
    if (element.hasIssuedElement())
      composeDateTime(t, "Contract", "issued", element.getIssuedElement(), -1);
    if (element.hasApplies())
      composePeriod(t, "Contract", "applies", element.getApplies(), -1);
    if (element.hasExpirationType())
      composeCodeableConcept(t, "Contract", "expirationType", element.getExpirationType(), -1);
    for (int i = 0; i < element.getSubject().size(); i++)
      composeReference(t, "Contract", "subject", element.getSubject().get(i), i);
    for (int i = 0; i < element.getAuthority().size(); i++)
      composeReference(t, "Contract", "authority", element.getAuthority().get(i), i);
    for (int i = 0; i < element.getDomain().size(); i++)
      composeReference(t, "Contract", "domain", element.getDomain().get(i), i);
    for (int i = 0; i < element.getSite().size(); i++)
      composeReference(t, "Contract", "site", element.getSite().get(i), i);
    if (element.hasNameElement())
      composeString(t, "Contract", "name", element.getNameElement(), -1);
    if (element.hasTitleElement())
      composeString(t, "Contract", "title", element.getTitleElement(), -1);
    if (element.hasSubtitleElement())
      composeString(t, "Contract", "subtitle", element.getSubtitleElement(), -1);
    for (int i = 0; i < element.getAlias().size(); i++)
      composeString(t, "Contract", "alias", element.getAlias().get(i), i);
    if (element.hasAuthor())
      composeReference(t, "Contract", "author", element.getAuthor(), -1);
    if (element.hasScope())
      composeCodeableConcept(t, "Contract", "scope", element.getScope(), -1);
    if (element.hasTopic())
      composeType(t, "Contract", "topic", element.getTopic(), -1);
    if (element.hasType())
      composeCodeableConcept(t, "Contract", "type", element.getType(), -1);
    for (int i = 0; i < element.getSubType().size(); i++)
      composeCodeableConcept(t, "Contract", "subType", element.getSubType().get(i), i);
    if (element.hasContentDefinition())
      composeContractContentDefinitionComponent(t, "Contract", "contentDefinition", element.getContentDefinition(), -1);
    for (int i = 0; i < element.getTerm().size(); i++)
      composeContractTermComponent(t, "Contract", "term", element.getTerm().get(i), i);
    for (int i = 0; i < element.getSupportingInfo().size(); i++)
      composeReference(t, "Contract", "supportingInfo", element.getSupportingInfo().get(i), i);
    for (int i = 0; i < element.getRelevantHistory().size(); i++)
      composeReference(t, "Contract", "relevantHistory", element.getRelevantHistory().get(i), i);
    for (int i = 0; i < element.getSigner().size(); i++)
      composeContractSignatoryComponent(t, "Contract", "signer", element.getSigner().get(i), i);
    for (int i = 0; i < element.getFriendly().size(); i++)
      composeContractFriendlyLanguageComponent(t, "Contract", "friendly", element.getFriendly().get(i), i);
    for (int i = 0; i < element.getLegal().size(); i++)
      composeContractLegalLanguageComponent(t, "Contract", "legal", element.getLegal().get(i), i);
    for (int i = 0; i < element.getRule().size(); i++)
      composeContractComputableLanguageComponent(t, "Contract", "rule", element.getRule().get(i), i);
    if (element.hasLegallyBinding())
      composeType(t, "Contract", "legallyBinding", element.getLegallyBinding(), -1);
  }

  protected void composeContractContentDefinitionComponent(Complex parent, String parentType, String name, Contract.ContentDefinitionComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "contentDefinition", name, element, index);
    if (element.hasType())
      composeCodeableConcept(t, "Contract", "type", element.getType(), -1);
    if (element.hasSubType())
      composeCodeableConcept(t, "Contract", "subType", element.getSubType(), -1);
    if (element.hasPublisher())
      composeReference(t, "Contract", "publisher", element.getPublisher(), -1);
    if (element.hasPublicationDateElement())
      composeDateTime(t, "Contract", "publicationDate", element.getPublicationDateElement(), -1);
    if (element.hasPublicationStatusElement())
      composeEnum(t, "Contract", "publicationStatus", element.getPublicationStatusElement(), -1);
    if (element.hasCopyrightElement())
      composeMarkdown(t, "Contract", "copyright", element.getCopyrightElement(), -1);
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
    if (element.hasTopic())
      composeType(t, "Contract", "topic", element.getTopic(), -1);
    if (element.hasType())
      composeCodeableConcept(t, "Contract", "type", element.getType(), -1);
    if (element.hasSubType())
      composeCodeableConcept(t, "Contract", "subType", element.getSubType(), -1);
    if (element.hasTextElement())
      composeString(t, "Contract", "text", element.getTextElement(), -1);
    for (int i = 0; i < element.getSecurityLabel().size(); i++)
      composeContractSecurityLabelComponent(t, "Contract", "securityLabel", element.getSecurityLabel().get(i), i);
    if (element.hasOffer())
      composeContractContractOfferComponent(t, "Contract", "offer", element.getOffer(), -1);
    for (int i = 0; i < element.getAsset().size(); i++)
      composeContractContractAssetComponent(t, "Contract", "asset", element.getAsset().get(i), i);
    for (int i = 0; i < element.getAction().size(); i++)
      composeContractActionComponent(t, "Contract", "action", element.getAction().get(i), i);
    for (int i = 0; i < element.getGroup().size(); i++)
      composeContractTermComponent(t, "Contract", "group", element.getGroup().get(i), i);
  }

  protected void composeContractSecurityLabelComponent(Complex parent, String parentType, String name, Contract.SecurityLabelComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "securityLabel", name, element, index);
    for (int i = 0; i < element.getNumber().size(); i++)
      composeUnsignedInt(t, "Contract", "number", element.getNumber().get(i), i);
    if (element.hasClassification())
      composeCoding(t, "Contract", "classification", element.getClassification(), -1);
    for (int i = 0; i < element.getCategory().size(); i++)
      composeCoding(t, "Contract", "category", element.getCategory().get(i), i);
    for (int i = 0; i < element.getControl().size(); i++)
      composeCoding(t, "Contract", "control", element.getControl().get(i), i);
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
    for (int i = 0; i < element.getIdentifier().size(); i++)
      composeIdentifier(t, "Contract", "identifier", element.getIdentifier().get(i), i);
    for (int i = 0; i < element.getParty().size(); i++)
      composeContractContractPartyComponent(t, "Contract", "party", element.getParty().get(i), i);
    if (element.hasTopic())
      composeReference(t, "Contract", "topic", element.getTopic(), -1);
    if (element.hasType())
      composeCodeableConcept(t, "Contract", "type", element.getType(), -1);
    if (element.hasDecision())
      composeCodeableConcept(t, "Contract", "decision", element.getDecision(), -1);
    for (int i = 0; i < element.getDecisionMode().size(); i++)
      composeCodeableConcept(t, "Contract", "decisionMode", element.getDecisionMode().get(i), i);
    for (int i = 0; i < element.getAnswer().size(); i++)
      composeContractAnswerComponent(t, "Contract", "answer", element.getAnswer().get(i), i);
    if (element.hasTextElement())
      composeString(t, "Contract", "text", element.getTextElement(), -1);
    for (int i = 0; i < element.getLinkId().size(); i++)
      composeString(t, "Contract", "linkId", element.getLinkId().get(i), i);
    for (int i = 0; i < element.getSecurityLabelNumber().size(); i++)
      composeUnsignedInt(t, "Contract", "securityLabelNumber", element.getSecurityLabelNumber().get(i), i);
  }

  protected void composeContractContractPartyComponent(Complex parent, String parentType, String name, Contract.ContractPartyComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "party", name, element, index);
    for (int i = 0; i < element.getReference().size(); i++)
      composeReference(t, "Contract", "reference", element.getReference().get(i), i);
    if (element.hasRole())
      composeCodeableConcept(t, "Contract", "role", element.getRole(), -1);
  }

  protected void composeContractAnswerComponent(Complex parent, String parentType, String name, Contract.AnswerComponent element, int index) {
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
      composeType(t, "Contract", "value", element.getValue(), -1);
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
    if (element.hasScope())
      composeCodeableConcept(t, "Contract", "scope", element.getScope(), -1);
    for (int i = 0; i < element.getType().size(); i++)
      composeCodeableConcept(t, "Contract", "type", element.getType().get(i), i);
    for (int i = 0; i < element.getTypeReference().size(); i++)
      composeReference(t, "Contract", "typeReference", element.getTypeReference().get(i), i);
    for (int i = 0; i < element.getSubtype().size(); i++)
      composeCodeableConcept(t, "Contract", "subtype", element.getSubtype().get(i), i);
    if (element.hasRelationship())
      composeCoding(t, "Contract", "relationship", element.getRelationship(), -1);
    for (int i = 0; i < element.getContext().size(); i++)
      composeContractAssetContextComponent(t, "Contract", "context", element.getContext().get(i), i);
    if (element.hasConditionElement())
      composeString(t, "Contract", "condition", element.getConditionElement(), -1);
    for (int i = 0; i < element.getPeriodType().size(); i++)
      composeCodeableConcept(t, "Contract", "periodType", element.getPeriodType().get(i), i);
    for (int i = 0; i < element.getPeriod().size(); i++)
      composePeriod(t, "Contract", "period", element.getPeriod().get(i), i);
    for (int i = 0; i < element.getUsePeriod().size(); i++)
      composePeriod(t, "Contract", "usePeriod", element.getUsePeriod().get(i), i);
    if (element.hasTextElement())
      composeString(t, "Contract", "text", element.getTextElement(), -1);
    for (int i = 0; i < element.getLinkId().size(); i++)
      composeString(t, "Contract", "linkId", element.getLinkId().get(i), i);
    for (int i = 0; i < element.getAnswer().size(); i++)
      composeContractAnswerComponent(t, "Contract", "answer", element.getAnswer().get(i), i);
    for (int i = 0; i < element.getSecurityLabelNumber().size(); i++)
      composeUnsignedInt(t, "Contract", "securityLabelNumber", element.getSecurityLabelNumber().get(i), i);
    for (int i = 0; i < element.getValuedItem().size(); i++)
      composeContractValuedItemComponent(t, "Contract", "valuedItem", element.getValuedItem().get(i), i);
  }

  protected void composeContractAssetContextComponent(Complex parent, String parentType, String name, Contract.AssetContextComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "context", name, element, index);
    if (element.hasReference())
      composeReference(t, "Contract", "reference", element.getReference(), -1);
    for (int i = 0; i < element.getCode().size(); i++)
      composeCodeableConcept(t, "Contract", "code", element.getCode().get(i), i);
    if (element.hasTextElement())
      composeString(t, "Contract", "text", element.getTextElement(), -1);
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
    if (element.hasPaymentElement())
      composeString(t, "Contract", "payment", element.getPaymentElement(), -1);
    if (element.hasPaymentDateElement())
      composeDateTime(t, "Contract", "paymentDate", element.getPaymentDateElement(), -1);
    if (element.hasResponsible())
      composeReference(t, "Contract", "responsible", element.getResponsible(), -1);
    if (element.hasRecipient())
      composeReference(t, "Contract", "recipient", element.getRecipient(), -1);
    for (int i = 0; i < element.getLinkId().size(); i++)
      composeString(t, "Contract", "linkId", element.getLinkId().get(i), i);
    for (int i = 0; i < element.getSecurityLabelNumber().size(); i++)
      composeUnsignedInt(t, "Contract", "securityLabelNumber", element.getSecurityLabelNumber().get(i), i);
  }

  protected void composeContractActionComponent(Complex parent, String parentType, String name, Contract.ActionComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "action", name, element, index);
    if (element.hasDoNotPerformElement())
      composeBoolean(t, "Contract", "doNotPerform", element.getDoNotPerformElement(), -1);
    if (element.hasType())
      composeCodeableConcept(t, "Contract", "type", element.getType(), -1);
    for (int i = 0; i < element.getSubject().size(); i++)
      composeContractActionSubjectComponent(t, "Contract", "subject", element.getSubject().get(i), i);
    if (element.hasIntent())
      composeCodeableConcept(t, "Contract", "intent", element.getIntent(), -1);
    for (int i = 0; i < element.getLinkId().size(); i++)
      composeString(t, "Contract", "linkId", element.getLinkId().get(i), i);
    if (element.hasStatus())
      composeCodeableConcept(t, "Contract", "status", element.getStatus(), -1);
    if (element.hasContext())
      composeReference(t, "Contract", "context", element.getContext(), -1);
    for (int i = 0; i < element.getContextLinkId().size(); i++)
      composeString(t, "Contract", "contextLinkId", element.getContextLinkId().get(i), i);
    if (element.hasOccurrence())
      composeType(t, "Contract", "occurrence", element.getOccurrence(), -1);
    for (int i = 0; i < element.getRequester().size(); i++)
      composeReference(t, "Contract", "requester", element.getRequester().get(i), i);
    for (int i = 0; i < element.getRequesterLinkId().size(); i++)
      composeString(t, "Contract", "requesterLinkId", element.getRequesterLinkId().get(i), i);
    for (int i = 0; i < element.getPerformerType().size(); i++)
      composeCodeableConcept(t, "Contract", "performerType", element.getPerformerType().get(i), i);
    if (element.hasPerformerRole())
      composeCodeableConcept(t, "Contract", "performerRole", element.getPerformerRole(), -1);
    if (element.hasPerformer())
      composeReference(t, "Contract", "performer", element.getPerformer(), -1);
    for (int i = 0; i < element.getPerformerLinkId().size(); i++)
      composeString(t, "Contract", "performerLinkId", element.getPerformerLinkId().get(i), i);
    for (int i = 0; i < element.getReasonCode().size(); i++)
      composeCodeableConcept(t, "Contract", "reasonCode", element.getReasonCode().get(i), i);
    for (int i = 0; i < element.getReasonReference().size(); i++)
      composeReference(t, "Contract", "reasonReference", element.getReasonReference().get(i), i);
    for (int i = 0; i < element.getReason().size(); i++)
      composeString(t, "Contract", "reason", element.getReason().get(i), i);
    for (int i = 0; i < element.getReasonLinkId().size(); i++)
      composeString(t, "Contract", "reasonLinkId", element.getReasonLinkId().get(i), i);
    for (int i = 0; i < element.getNote().size(); i++)
      composeAnnotation(t, "Contract", "note", element.getNote().get(i), i);
    for (int i = 0; i < element.getSecurityLabelNumber().size(); i++)
      composeUnsignedInt(t, "Contract", "securityLabelNumber", element.getSecurityLabelNumber().get(i), i);
  }

  protected void composeContractActionSubjectComponent(Complex parent, String parentType, String name, Contract.ActionSubjectComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "subject", name, element, index);
    for (int i = 0; i < element.getReference().size(); i++)
      composeReference(t, "Contract", "reference", element.getReference().get(i), i);
    if (element.hasRole())
      composeCodeableConcept(t, "Contract", "role", element.getRole(), -1);
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
    if (element.hasDependentElement())
      composeString(t, "Coverage", "dependent", element.getDependentElement(), -1);
    if (element.hasRelationship())
      composeCodeableConcept(t, "Coverage", "relationship", element.getRelationship(), -1);
    if (element.hasPeriod())
      composePeriod(t, "Coverage", "period", element.getPeriod(), -1);
    for (int i = 0; i < element.getPayor().size(); i++)
      composeReference(t, "Coverage", "payor", element.getPayor().get(i), i);
    for (int i = 0; i < element.getClass_().size(); i++)
      composeCoverageClassComponent(t, "Coverage", "class", element.getClass_().get(i), i);
    if (element.hasOrderElement())
      composePositiveInt(t, "Coverage", "order", element.getOrderElement(), -1);
    if (element.hasNetworkElement())
      composeString(t, "Coverage", "network", element.getNetworkElement(), -1);
    for (int i = 0; i < element.getCopay().size(); i++)
      composeCoverageCoPayComponent(t, "Coverage", "copay", element.getCopay().get(i), i);
    for (int i = 0; i < element.getContract().size(); i++)
      composeReference(t, "Coverage", "contract", element.getContract().get(i), i);
  }

  protected void composeCoverageClassComponent(Complex parent, String parentType, String name, Coverage.ClassComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "class", name, element, index);
    if (element.hasType())
      composeCoding(t, "Coverage", "type", element.getType(), -1);
    if (element.hasValueElement())
      composeString(t, "Coverage", "value", element.getValueElement(), -1);
    if (element.hasNameElement())
      composeString(t, "Coverage", "name", element.getNameElement(), -1);
  }

  protected void composeCoverageCoPayComponent(Complex parent, String parentType, String name, Coverage.CoPayComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "copay", name, element, index);
    if (element.hasType())
      composeCoding(t, "Coverage", "type", element.getType(), -1);
    if (element.hasValue())
      composeQuantity(t, "Coverage", "value", element.getValue(), -1);
  }

  protected void composeCoverageEligibilityRequest(Complex parent, String parentType, String name, CoverageEligibilityRequest element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "CoverageEligibilityRequest", name, element, index);
    for (int i = 0; i < element.getIdentifier().size(); i++)
      composeIdentifier(t, "CoverageEligibilityRequest", "identifier", element.getIdentifier().get(i), i);
    if (element.hasStatusElement())
      composeEnum(t, "CoverageEligibilityRequest", "status", element.getStatusElement(), -1);
    if (element.hasPriority())
      composeCodeableConcept(t, "CoverageEligibilityRequest", "priority", element.getPriority(), -1);
    for (int i = 0; i < element.getPurpose().size(); i++)
      composeEnum(t, "CoverageEligibilityRequest", "purpose", element.getPurpose().get(i), i);
    if (element.hasPatient())
      composeReference(t, "CoverageEligibilityRequest", "patient", element.getPatient(), -1);
    if (element.hasServiced())
      composeType(t, "CoverageEligibilityRequest", "serviced", element.getServiced(), -1);
    if (element.hasCreatedElement())
      composeDateTime(t, "CoverageEligibilityRequest", "created", element.getCreatedElement(), -1);
    if (element.hasEnterer())
      composeReference(t, "CoverageEligibilityRequest", "enterer", element.getEnterer(), -1);
    if (element.hasProvider())
      composeReference(t, "CoverageEligibilityRequest", "provider", element.getProvider(), -1);
    if (element.hasInsurer())
      composeReference(t, "CoverageEligibilityRequest", "insurer", element.getInsurer(), -1);
    if (element.hasFacility())
      composeReference(t, "CoverageEligibilityRequest", "facility", element.getFacility(), -1);
    for (int i = 0; i < element.getSupportingInformation().size(); i++)
      composeCoverageEligibilityRequestInformationComponent(t, "CoverageEligibilityRequest", "supportingInformation", element.getSupportingInformation().get(i), i);
    for (int i = 0; i < element.getInsurance().size(); i++)
      composeCoverageEligibilityRequestInsuranceComponent(t, "CoverageEligibilityRequest", "insurance", element.getInsurance().get(i), i);
    for (int i = 0; i < element.getItem().size(); i++)
      composeCoverageEligibilityRequestDetailsComponent(t, "CoverageEligibilityRequest", "item", element.getItem().get(i), i);
  }

  protected void composeCoverageEligibilityRequestInformationComponent(Complex parent, String parentType, String name, CoverageEligibilityRequest.InformationComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "supportingInformation", name, element, index);
    if (element.hasSequenceElement())
      composePositiveInt(t, "CoverageEligibilityRequest", "sequence", element.getSequenceElement(), -1);
    if (element.hasInformation())
      composeReference(t, "CoverageEligibilityRequest", "information", element.getInformation(), -1);
    if (element.hasAppliesToAllElement())
      composeBoolean(t, "CoverageEligibilityRequest", "appliesToAll", element.getAppliesToAllElement(), -1);
  }

  protected void composeCoverageEligibilityRequestInsuranceComponent(Complex parent, String parentType, String name, CoverageEligibilityRequest.InsuranceComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "insurance", name, element, index);
    if (element.hasFocalElement())
      composeBoolean(t, "CoverageEligibilityRequest", "focal", element.getFocalElement(), -1);
    if (element.hasCoverage())
      composeReference(t, "CoverageEligibilityRequest", "coverage", element.getCoverage(), -1);
    if (element.hasBusinessArrangementElement())
      composeString(t, "CoverageEligibilityRequest", "businessArrangement", element.getBusinessArrangementElement(), -1);
  }

  protected void composeCoverageEligibilityRequestDetailsComponent(Complex parent, String parentType, String name, CoverageEligibilityRequest.DetailsComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "item", name, element, index);
    for (int i = 0; i < element.getSupportingInformationSequence().size(); i++)
      composePositiveInt(t, "CoverageEligibilityRequest", "supportingInformationSequence", element.getSupportingInformationSequence().get(i), i);
    if (element.hasCategory())
      composeCodeableConcept(t, "CoverageEligibilityRequest", "category", element.getCategory(), -1);
    if (element.hasBillcode())
      composeCodeableConcept(t, "CoverageEligibilityRequest", "billcode", element.getBillcode(), -1);
    for (int i = 0; i < element.getModifier().size(); i++)
      composeCodeableConcept(t, "CoverageEligibilityRequest", "modifier", element.getModifier().get(i), i);
    if (element.hasProvider())
      composeReference(t, "CoverageEligibilityRequest", "provider", element.getProvider(), -1);
    if (element.hasQuantity())
      composeQuantity(t, "CoverageEligibilityRequest", "quantity", element.getQuantity(), -1);
    if (element.hasUnitPrice())
      composeMoney(t, "CoverageEligibilityRequest", "unitPrice", element.getUnitPrice(), -1);
    if (element.hasFacility())
      composeReference(t, "CoverageEligibilityRequest", "facility", element.getFacility(), -1);
    for (int i = 0; i < element.getDiagnosis().size(); i++)
      composeCoverageEligibilityRequestDiagnosisComponent(t, "CoverageEligibilityRequest", "diagnosis", element.getDiagnosis().get(i), i);
    for (int i = 0; i < element.getDetail().size(); i++)
      composeReference(t, "CoverageEligibilityRequest", "detail", element.getDetail().get(i), i);
  }

  protected void composeCoverageEligibilityRequestDiagnosisComponent(Complex parent, String parentType, String name, CoverageEligibilityRequest.DiagnosisComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "diagnosis", name, element, index);
    if (element.hasDiagnosis())
      composeType(t, "CoverageEligibilityRequest", "diagnosis", element.getDiagnosis(), -1);
  }

  protected void composeCoverageEligibilityResponse(Complex parent, String parentType, String name, CoverageEligibilityResponse element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "CoverageEligibilityResponse", name, element, index);
    for (int i = 0; i < element.getIdentifier().size(); i++)
      composeIdentifier(t, "CoverageEligibilityResponse", "identifier", element.getIdentifier().get(i), i);
    if (element.hasStatusElement())
      composeEnum(t, "CoverageEligibilityResponse", "status", element.getStatusElement(), -1);
    for (int i = 0; i < element.getPurpose().size(); i++)
      composeEnum(t, "CoverageEligibilityResponse", "purpose", element.getPurpose().get(i), i);
    if (element.hasPatient())
      composeReference(t, "CoverageEligibilityResponse", "patient", element.getPatient(), -1);
    if (element.hasServiced())
      composeType(t, "CoverageEligibilityResponse", "serviced", element.getServiced(), -1);
    if (element.hasCreatedElement())
      composeDateTime(t, "CoverageEligibilityResponse", "created", element.getCreatedElement(), -1);
    if (element.hasRequestProvider())
      composeReference(t, "CoverageEligibilityResponse", "requestProvider", element.getRequestProvider(), -1);
    if (element.hasRequest())
      composeReference(t, "CoverageEligibilityResponse", "request", element.getRequest(), -1);
    if (element.hasOutcomeElement())
      composeEnum(t, "CoverageEligibilityResponse", "outcome", element.getOutcomeElement(), -1);
    if (element.hasDispositionElement())
      composeString(t, "CoverageEligibilityResponse", "disposition", element.getDispositionElement(), -1);
    if (element.hasInsurer())
      composeReference(t, "CoverageEligibilityResponse", "insurer", element.getInsurer(), -1);
    for (int i = 0; i < element.getInsurance().size(); i++)
      composeCoverageEligibilityResponseInsuranceComponent(t, "CoverageEligibilityResponse", "insurance", element.getInsurance().get(i), i);
    if (element.hasPreAuthRefElement())
      composeString(t, "CoverageEligibilityResponse", "preAuthRef", element.getPreAuthRefElement(), -1);
    if (element.hasForm())
      composeCodeableConcept(t, "CoverageEligibilityResponse", "form", element.getForm(), -1);
    for (int i = 0; i < element.getError().size(); i++)
      composeCoverageEligibilityResponseErrorsComponent(t, "CoverageEligibilityResponse", "error", element.getError().get(i), i);
  }

  protected void composeCoverageEligibilityResponseInsuranceComponent(Complex parent, String parentType, String name, CoverageEligibilityResponse.InsuranceComponent element, int index) {
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
      composeReference(t, "CoverageEligibilityResponse", "coverage", element.getCoverage(), -1);
    if (element.hasContract())
      composeReference(t, "CoverageEligibilityResponse", "contract", element.getContract(), -1);
    if (element.hasInforceElement())
      composeBoolean(t, "CoverageEligibilityResponse", "inforce", element.getInforceElement(), -1);
    for (int i = 0; i < element.getItem().size(); i++)
      composeCoverageEligibilityResponseItemsComponent(t, "CoverageEligibilityResponse", "item", element.getItem().get(i), i);
  }

  protected void composeCoverageEligibilityResponseItemsComponent(Complex parent, String parentType, String name, CoverageEligibilityResponse.ItemsComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "item", name, element, index);
    if (element.hasCategory())
      composeCodeableConcept(t, "CoverageEligibilityResponse", "category", element.getCategory(), -1);
    if (element.hasBillcode())
      composeCodeableConcept(t, "CoverageEligibilityResponse", "billcode", element.getBillcode(), -1);
    for (int i = 0; i < element.getModifier().size(); i++)
      composeCodeableConcept(t, "CoverageEligibilityResponse", "modifier", element.getModifier().get(i), i);
    if (element.hasProvider())
      composeReference(t, "CoverageEligibilityResponse", "provider", element.getProvider(), -1);
    if (element.hasExcludedElement())
      composeBoolean(t, "CoverageEligibilityResponse", "excluded", element.getExcludedElement(), -1);
    if (element.hasNameElement())
      composeString(t, "CoverageEligibilityResponse", "name", element.getNameElement(), -1);
    if (element.hasDescriptionElement())
      composeString(t, "CoverageEligibilityResponse", "description", element.getDescriptionElement(), -1);
    if (element.hasNetwork())
      composeCodeableConcept(t, "CoverageEligibilityResponse", "network", element.getNetwork(), -1);
    if (element.hasUnit())
      composeCodeableConcept(t, "CoverageEligibilityResponse", "unit", element.getUnit(), -1);
    if (element.hasTerm())
      composeCodeableConcept(t, "CoverageEligibilityResponse", "term", element.getTerm(), -1);
    for (int i = 0; i < element.getBenefit().size(); i++)
      composeCoverageEligibilityResponseBenefitComponent(t, "CoverageEligibilityResponse", "benefit", element.getBenefit().get(i), i);
    if (element.hasAuthorizationRequiredElement())
      composeBoolean(t, "CoverageEligibilityResponse", "authorizationRequired", element.getAuthorizationRequiredElement(), -1);
    for (int i = 0; i < element.getAuthorizationSupporting().size(); i++)
      composeCodeableConcept(t, "CoverageEligibilityResponse", "authorizationSupporting", element.getAuthorizationSupporting().get(i), i);
    if (element.hasAuthorizationUrlElement())
      composeUri(t, "CoverageEligibilityResponse", "authorizationUrl", element.getAuthorizationUrlElement(), -1);
  }

  protected void composeCoverageEligibilityResponseBenefitComponent(Complex parent, String parentType, String name, CoverageEligibilityResponse.BenefitComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "benefit", name, element, index);
    if (element.hasType())
      composeCodeableConcept(t, "CoverageEligibilityResponse", "type", element.getType(), -1);
    if (element.hasAllowed())
      composeType(t, "CoverageEligibilityResponse", "allowed", element.getAllowed(), -1);
    if (element.hasUsed())
      composeType(t, "CoverageEligibilityResponse", "used", element.getUsed(), -1);
  }

  protected void composeCoverageEligibilityResponseErrorsComponent(Complex parent, String parentType, String name, CoverageEligibilityResponse.ErrorsComponent element, int index) {
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
      composeCodeableConcept(t, "CoverageEligibilityResponse", "code", element.getCode(), -1);
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
    for (int i = 0; i < element.getIdentifier().size(); i++)
      composeIdentifier(t, "DetectedIssue", "identifier", element.getIdentifier().get(i), i);
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
    if (element.hasDefinition())
      composeReference(t, "Device", "definition", element.getDefinition(), -1);
    for (int i = 0; i < element.getUdiCarrier().size(); i++)
      composeDeviceDeviceUdiCarrierComponent(t, "Device", "udiCarrier", element.getUdiCarrier().get(i), i);
    if (element.hasStatusElement())
      composeEnum(t, "Device", "status", element.getStatusElement(), -1);
    for (int i = 0; i < element.getStatusReason().size(); i++)
      composeCodeableConcept(t, "Device", "statusReason", element.getStatusReason().get(i), i);
    if (element.hasDistinctIdentificationCodeElement())
      composeString(t, "Device", "distinctIdentificationCode", element.getDistinctIdentificationCodeElement(), -1);
    if (element.hasManufacturerElement())
      composeString(t, "Device", "manufacturer", element.getManufacturerElement(), -1);
    if (element.hasManufactureDateElement())
      composeDateTime(t, "Device", "manufactureDate", element.getManufactureDateElement(), -1);
    if (element.hasExpirationDateElement())
      composeDateTime(t, "Device", "expirationDate", element.getExpirationDateElement(), -1);
    if (element.hasLotNumberElement())
      composeString(t, "Device", "lotNumber", element.getLotNumberElement(), -1);
    if (element.hasSerialNumberElement())
      composeString(t, "Device", "serialNumber", element.getSerialNumberElement(), -1);
    for (int i = 0; i < element.getDeviceName().size(); i++)
      composeDeviceDeviceDeviceNameComponent(t, "Device", "deviceName", element.getDeviceName().get(i), i);
    if (element.hasModelNumberElement())
      composeString(t, "Device", "modelNumber", element.getModelNumberElement(), -1);
    if (element.hasPartNumberElement())
      composeString(t, "Device", "partNumber", element.getPartNumberElement(), -1);
    if (element.hasType())
      composeCodeableConcept(t, "Device", "type", element.getType(), -1);
    for (int i = 0; i < element.getSpecialization().size(); i++)
      composeDeviceDeviceSpecializationComponent(t, "Device", "specialization", element.getSpecialization().get(i), i);
    for (int i = 0; i < element.getVersion().size(); i++)
      composeDeviceDeviceVersionComponent(t, "Device", "version", element.getVersion().get(i), i);
    for (int i = 0; i < element.getProperty().size(); i++)
      composeDeviceDevicePropertyComponent(t, "Device", "property", element.getProperty().get(i), i);
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
    if (element.hasParent())
      composeReference(t, "Device", "parent", element.getParent(), -1);
  }

  protected void composeDeviceDeviceUdiCarrierComponent(Complex parent, String parentType, String name, Device.DeviceUdiCarrierComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "udiCarrier", name, element, index);
    if (element.hasDeviceIdentifierElement())
      composeString(t, "Device", "deviceIdentifier", element.getDeviceIdentifierElement(), -1);
    if (element.hasIssuerElement())
      composeUri(t, "Device", "issuer", element.getIssuerElement(), -1);
    if (element.hasJurisdictionElement())
      composeUri(t, "Device", "jurisdiction", element.getJurisdictionElement(), -1);
    if (element.hasCarrierAIDCElement())
      composeBase64Binary(t, "Device", "carrierAIDC", element.getCarrierAIDCElement(), -1);
    if (element.hasCarrierHRFElement())
      composeString(t, "Device", "carrierHRF", element.getCarrierHRFElement(), -1);
    if (element.hasEntryTypeElement())
      composeEnum(t, "Device", "entryType", element.getEntryTypeElement(), -1);
  }

  protected void composeDeviceDeviceDeviceNameComponent(Complex parent, String parentType, String name, Device.DeviceDeviceNameComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "deviceName", name, element, index);
    if (element.hasNameElement())
      composeString(t, "Device", "name", element.getNameElement(), -1);
    if (element.hasTypeElement())
      composeEnum(t, "Device", "type", element.getTypeElement(), -1);
  }

  protected void composeDeviceDeviceSpecializationComponent(Complex parent, String parentType, String name, Device.DeviceSpecializationComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "specialization", name, element, index);
    if (element.hasSystemType())
      composeCodeableConcept(t, "Device", "systemType", element.getSystemType(), -1);
    if (element.hasVersionElement())
      composeString(t, "Device", "version", element.getVersionElement(), -1);
  }

  protected void composeDeviceDeviceVersionComponent(Complex parent, String parentType, String name, Device.DeviceVersionComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "version", name, element, index);
    if (element.hasType())
      composeCodeableConcept(t, "Device", "type", element.getType(), -1);
    if (element.hasComponent())
      composeIdentifier(t, "Device", "component", element.getComponent(), -1);
    if (element.hasValueElement())
      composeString(t, "Device", "value", element.getValueElement(), -1);
  }

  protected void composeDeviceDevicePropertyComponent(Complex parent, String parentType, String name, Device.DevicePropertyComponent element, int index) {
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
      composeCodeableConcept(t, "Device", "type", element.getType(), -1);
    for (int i = 0; i < element.getValueQuanity().size(); i++)
      composeQuantity(t, "Device", "valueQuanity", element.getValueQuanity().get(i), i);
    for (int i = 0; i < element.getValueCode().size(); i++)
      composeCodeableConcept(t, "Device", "valueCode", element.getValueCode().get(i), i);
  }

  protected void composeDeviceDefinition(Complex parent, String parentType, String name, DeviceDefinition element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "DeviceDefinition", name, element, index);
    for (int i = 0; i < element.getIdentifier().size(); i++)
      composeIdentifier(t, "DeviceDefinition", "identifier", element.getIdentifier().get(i), i);
    for (int i = 0; i < element.getUdiDeviceIdentifier().size(); i++)
      composeDeviceDefinitionDeviceDefinitionUdiDeviceIdentifierComponent(t, "DeviceDefinition", "udiDeviceIdentifier", element.getUdiDeviceIdentifier().get(i), i);
    if (element.hasManufacturer())
      composeType(t, "DeviceDefinition", "manufacturer", element.getManufacturer(), -1);
    for (int i = 0; i < element.getDeviceName().size(); i++)
      composeDeviceDefinitionDeviceDefinitionDeviceNameComponent(t, "DeviceDefinition", "deviceName", element.getDeviceName().get(i), i);
    if (element.hasModelNumberElement())
      composeString(t, "DeviceDefinition", "modelNumber", element.getModelNumberElement(), -1);
    if (element.hasType())
      composeCodeableConcept(t, "DeviceDefinition", "type", element.getType(), -1);
    for (int i = 0; i < element.getSpecialization().size(); i++)
      composeDeviceDefinitionDeviceDefinitionSpecializationComponent(t, "DeviceDefinition", "specialization", element.getSpecialization().get(i), i);
    for (int i = 0; i < element.getVersion().size(); i++)
      composeString(t, "DeviceDefinition", "version", element.getVersion().get(i), i);
    for (int i = 0; i < element.getSafety().size(); i++)
      composeCodeableConcept(t, "DeviceDefinition", "safety", element.getSafety().get(i), i);
    for (int i = 0; i < element.getShelfLifeStorage().size(); i++)
      composeProductShelfLife(t, "DeviceDefinition", "shelfLifeStorage", element.getShelfLifeStorage().get(i), i);
    if (element.hasPhysicalCharacteristics())
      composeProdCharacteristic(t, "DeviceDefinition", "physicalCharacteristics", element.getPhysicalCharacteristics(), -1);
    for (int i = 0; i < element.getLanguageCode().size(); i++)
      composeCodeableConcept(t, "DeviceDefinition", "languageCode", element.getLanguageCode().get(i), i);
    for (int i = 0; i < element.getCapability().size(); i++)
      composeDeviceDefinitionDeviceDefinitionCapabilityComponent(t, "DeviceDefinition", "capability", element.getCapability().get(i), i);
    for (int i = 0; i < element.getProperty().size(); i++)
      composeDeviceDefinitionDeviceDefinitionPropertyComponent(t, "DeviceDefinition", "property", element.getProperty().get(i), i);
    if (element.hasOwner())
      composeReference(t, "DeviceDefinition", "owner", element.getOwner(), -1);
    for (int i = 0; i < element.getContact().size(); i++)
      composeContactPoint(t, "DeviceDefinition", "contact", element.getContact().get(i), i);
    if (element.hasUrlElement())
      composeUri(t, "DeviceDefinition", "url", element.getUrlElement(), -1);
    if (element.hasOnlineInformationElement())
      composeUri(t, "DeviceDefinition", "onlineInformation", element.getOnlineInformationElement(), -1);
    for (int i = 0; i < element.getNote().size(); i++)
      composeAnnotation(t, "DeviceDefinition", "note", element.getNote().get(i), i);
    if (element.hasQuantity())
      composeQuantity(t, "DeviceDefinition", "quantity", element.getQuantity(), -1);
    if (element.hasParentDevice())
      composeReference(t, "DeviceDefinition", "parentDevice", element.getParentDevice(), -1);
    for (int i = 0; i < element.getMaterial().size(); i++)
      composeDeviceDefinitionDeviceDefinitionMaterialComponent(t, "DeviceDefinition", "material", element.getMaterial().get(i), i);
  }

  protected void composeDeviceDefinitionDeviceDefinitionUdiDeviceIdentifierComponent(Complex parent, String parentType, String name, DeviceDefinition.DeviceDefinitionUdiDeviceIdentifierComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "udiDeviceIdentifier", name, element, index);
    if (element.hasDeviceIdentifierElement())
      composeString(t, "DeviceDefinition", "deviceIdentifier", element.getDeviceIdentifierElement(), -1);
    if (element.hasIssuerElement())
      composeUri(t, "DeviceDefinition", "issuer", element.getIssuerElement(), -1);
    if (element.hasJurisdictionElement())
      composeUri(t, "DeviceDefinition", "jurisdiction", element.getJurisdictionElement(), -1);
  }

  protected void composeDeviceDefinitionDeviceDefinitionDeviceNameComponent(Complex parent, String parentType, String name, DeviceDefinition.DeviceDefinitionDeviceNameComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "deviceName", name, element, index);
    if (element.hasNameElement())
      composeString(t, "DeviceDefinition", "name", element.getNameElement(), -1);
    if (element.hasTypeElement())
      composeEnum(t, "DeviceDefinition", "type", element.getTypeElement(), -1);
  }

  protected void composeDeviceDefinitionDeviceDefinitionSpecializationComponent(Complex parent, String parentType, String name, DeviceDefinition.DeviceDefinitionSpecializationComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "specialization", name, element, index);
    if (element.hasSystemTypeElement())
      composeString(t, "DeviceDefinition", "systemType", element.getSystemTypeElement(), -1);
    if (element.hasVersionElement())
      composeString(t, "DeviceDefinition", "version", element.getVersionElement(), -1);
  }

  protected void composeDeviceDefinitionDeviceDefinitionCapabilityComponent(Complex parent, String parentType, String name, DeviceDefinition.DeviceDefinitionCapabilityComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "capability", name, element, index);
    if (element.hasType())
      composeCodeableConcept(t, "DeviceDefinition", "type", element.getType(), -1);
    for (int i = 0; i < element.getDescription().size(); i++)
      composeCodeableConcept(t, "DeviceDefinition", "description", element.getDescription().get(i), i);
  }

  protected void composeDeviceDefinitionDeviceDefinitionPropertyComponent(Complex parent, String parentType, String name, DeviceDefinition.DeviceDefinitionPropertyComponent element, int index) {
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
      composeCodeableConcept(t, "DeviceDefinition", "type", element.getType(), -1);
    for (int i = 0; i < element.getValueQuanity().size(); i++)
      composeQuantity(t, "DeviceDefinition", "valueQuanity", element.getValueQuanity().get(i), i);
    for (int i = 0; i < element.getValueCode().size(); i++)
      composeCodeableConcept(t, "DeviceDefinition", "valueCode", element.getValueCode().get(i), i);
  }

  protected void composeDeviceDefinitionDeviceDefinitionMaterialComponent(Complex parent, String parentType, String name, DeviceDefinition.DeviceDefinitionMaterialComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "material", name, element, index);
    if (element.hasSubstance())
      composeCodeableConcept(t, "DeviceDefinition", "substance", element.getSubstance(), -1);
    if (element.hasAlternateElement())
      composeBoolean(t, "DeviceDefinition", "alternate", element.getAlternateElement(), -1);
    if (element.hasAllergenicIndicatorElement())
      composeBoolean(t, "DeviceDefinition", "allergenicIndicator", element.getAllergenicIndicatorElement(), -1);
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
    for (int i = 0; i < element.getInstantiatesCanonical().size(); i++)
      composeCanonical(t, "DeviceRequest", "instantiatesCanonical", element.getInstantiatesCanonical().get(i), i);
    for (int i = 0; i < element.getInstantiatesUri().size(); i++)
      composeUri(t, "DeviceRequest", "instantiatesUri", element.getInstantiatesUri().get(i), i);
    for (int i = 0; i < element.getBasedOn().size(); i++)
      composeReference(t, "DeviceRequest", "basedOn", element.getBasedOn().get(i), i);
    for (int i = 0; i < element.getPriorRequest().size(); i++)
      composeReference(t, "DeviceRequest", "priorRequest", element.getPriorRequest().get(i), i);
    if (element.hasGroupIdentifier())
      composeIdentifier(t, "DeviceRequest", "groupIdentifier", element.getGroupIdentifier(), -1);
    if (element.hasStatusElement())
      composeEnum(t, "DeviceRequest", "status", element.getStatusElement(), -1);
    if (element.hasIntentElement())
      composeEnum(t, "DeviceRequest", "intent", element.getIntentElement(), -1);
    if (element.hasPriorityElement())
      composeEnum(t, "DeviceRequest", "priority", element.getPriorityElement(), -1);
    if (element.hasCode())
      composeType(t, "DeviceRequest", "code", element.getCode(), -1);
    for (int i = 0; i < element.getParameter().size(); i++)
      composeDeviceRequestDeviceRequestParameterComponent(t, "DeviceRequest", "parameter", element.getParameter().get(i), i);
    if (element.hasSubject())
      composeReference(t, "DeviceRequest", "subject", element.getSubject(), -1);
    if (element.hasContext())
      composeReference(t, "DeviceRequest", "context", element.getContext(), -1);
    if (element.hasOccurrence())
      composeType(t, "DeviceRequest", "occurrence", element.getOccurrence(), -1);
    if (element.hasAuthoredOnElement())
      composeDateTime(t, "DeviceRequest", "authoredOn", element.getAuthoredOnElement(), -1);
    if (element.hasRequester())
      composeReference(t, "DeviceRequest", "requester", element.getRequester(), -1);
    if (element.hasPerformerType())
      composeCodeableConcept(t, "DeviceRequest", "performerType", element.getPerformerType(), -1);
    if (element.hasPerformer())
      composeReference(t, "DeviceRequest", "performer", element.getPerformer(), -1);
    for (int i = 0; i < element.getReasonCode().size(); i++)
      composeCodeableConcept(t, "DeviceRequest", "reasonCode", element.getReasonCode().get(i), i);
    for (int i = 0; i < element.getReasonReference().size(); i++)
      composeReference(t, "DeviceRequest", "reasonReference", element.getReasonReference().get(i), i);
    for (int i = 0; i < element.getInsurance().size(); i++)
      composeReference(t, "DeviceRequest", "insurance", element.getInsurance().get(i), i);
    for (int i = 0; i < element.getSupportingInfo().size(); i++)
      composeReference(t, "DeviceRequest", "supportingInfo", element.getSupportingInfo().get(i), i);
    for (int i = 0; i < element.getNote().size(); i++)
      composeAnnotation(t, "DeviceRequest", "note", element.getNote().get(i), i);
    for (int i = 0; i < element.getRelevantHistory().size(); i++)
      composeReference(t, "DeviceRequest", "relevantHistory", element.getRelevantHistory().get(i), i);
  }

  protected void composeDeviceRequestDeviceRequestParameterComponent(Complex parent, String parentType, String name, DeviceRequest.DeviceRequestParameterComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "parameter", name, element, index);
    if (element.hasCode())
      composeCodeableConcept(t, "DeviceRequest", "code", element.getCode(), -1);
    if (element.hasValue())
      composeType(t, "DeviceRequest", "value", element.getValue(), -1);
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
    for (int i = 0; i < element.getBasedOn().size(); i++)
      composeReference(t, "DeviceUseStatement", "basedOn", element.getBasedOn().get(i), i);
    if (element.hasStatusElement())
      composeEnum(t, "DeviceUseStatement", "status", element.getStatusElement(), -1);
    if (element.hasSubject())
      composeReference(t, "DeviceUseStatement", "subject", element.getSubject(), -1);
    for (int i = 0; i < element.getDerivedFrom().size(); i++)
      composeReference(t, "DeviceUseStatement", "derivedFrom", element.getDerivedFrom().get(i), i);
    if (element.hasTiming())
      composeType(t, "DeviceUseStatement", "timing", element.getTiming(), -1);
    if (element.hasRecordedOnElement())
      composeDateTime(t, "DeviceUseStatement", "recordedOn", element.getRecordedOnElement(), -1);
    if (element.hasSource())
      composeReference(t, "DeviceUseStatement", "source", element.getSource(), -1);
    if (element.hasDevice())
      composeReference(t, "DeviceUseStatement", "device", element.getDevice(), -1);
    for (int i = 0; i < element.getReasonCode().size(); i++)
      composeCodeableConcept(t, "DeviceUseStatement", "reasonCode", element.getReasonCode().get(i), i);
    for (int i = 0; i < element.getReasonReference().size(); i++)
      composeReference(t, "DeviceUseStatement", "reasonReference", element.getReasonReference().get(i), i);
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
    for (int i = 0; i < element.getMedia().size(); i++)
      composeDiagnosticReportDiagnosticReportMediaComponent(t, "DiagnosticReport", "media", element.getMedia().get(i), i);
    if (element.hasConclusionElement())
      composeString(t, "DiagnosticReport", "conclusion", element.getConclusionElement(), -1);
    for (int i = 0; i < element.getConclusionCode().size(); i++)
      composeCodeableConcept(t, "DiagnosticReport", "conclusionCode", element.getConclusionCode().get(i), i);
    for (int i = 0; i < element.getPresentedForm().size(); i++)
      composeAttachment(t, "DiagnosticReport", "presentedForm", element.getPresentedForm().get(i), i);
  }

  protected void composeDiagnosticReportDiagnosticReportMediaComponent(Complex parent, String parentType, String name, DiagnosticReport.DiagnosticReportMediaComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "media", name, element, index);
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
    for (int i = 0; i < element.getCategory().size(); i++)
      composeCodeableConcept(t, "DocumentReference", "category", element.getCategory().get(i), i);
    if (element.hasSubject())
      composeReference(t, "DocumentReference", "subject", element.getSubject(), -1);
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
    for (int i = 0; i < element.getEncounter().size(); i++)
      composeReference(t, "DocumentReference", "encounter", element.getEncounter().get(i), i);
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
      composeReference(t, "DocumentReference", "related", element.getRelated().get(i), i);
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
    if (element.hasServiceType())
      composeCodeableConcept(t, "Encounter", "serviceType", element.getServiceType(), -1);
    if (element.hasPriority())
      composeCodeableConcept(t, "Encounter", "priority", element.getPriority(), -1);
    if (element.hasSubject())
      composeReference(t, "Encounter", "subject", element.getSubject(), -1);
    for (int i = 0; i < element.getEpisodeOfCare().size(); i++)
      composeReference(t, "Encounter", "episodeOfCare", element.getEpisodeOfCare().get(i), i);
    for (int i = 0; i < element.getBasedOn().size(); i++)
      composeReference(t, "Encounter", "basedOn", element.getBasedOn().get(i), i);
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
      composeUrl(t, "Endpoint", "address", element.getAddressElement(), -1);
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
    if (element.hasSubtitleElement())
      composeString(t, "EventDefinition", "subtitle", element.getSubtitleElement(), -1);
    if (element.hasStatusElement())
      composeEnum(t, "EventDefinition", "status", element.getStatusElement(), -1);
    if (element.hasExperimentalElement())
      composeBoolean(t, "EventDefinition", "experimental", element.getExperimentalElement(), -1);
    if (element.hasSubject())
      composeType(t, "EventDefinition", "subject", element.getSubject(), -1);
    if (element.hasDateElement())
      composeDateTime(t, "EventDefinition", "date", element.getDateElement(), -1);
    if (element.hasPublisherElement())
      composeString(t, "EventDefinition", "publisher", element.getPublisherElement(), -1);
    for (int i = 0; i < element.getContact().size(); i++)
      composeContactDetail(t, "EventDefinition", "contact", element.getContact().get(i), i);
    if (element.hasDescriptionElement())
      composeMarkdown(t, "EventDefinition", "description", element.getDescriptionElement(), -1);
    for (int i = 0; i < element.getUseContext().size(); i++)
      composeUsageContext(t, "EventDefinition", "useContext", element.getUseContext().get(i), i);
    for (int i = 0; i < element.getJurisdiction().size(); i++)
      composeCodeableConcept(t, "EventDefinition", "jurisdiction", element.getJurisdiction().get(i), i);
    if (element.hasPurposeElement())
      composeMarkdown(t, "EventDefinition", "purpose", element.getPurposeElement(), -1);
    if (element.hasUsageElement())
      composeString(t, "EventDefinition", "usage", element.getUsageElement(), -1);
    if (element.hasCopyrightElement())
      composeMarkdown(t, "EventDefinition", "copyright", element.getCopyrightElement(), -1);
    if (element.hasApprovalDateElement())
      composeDate(t, "EventDefinition", "approvalDate", element.getApprovalDateElement(), -1);
    if (element.hasLastReviewDateElement())
      composeDate(t, "EventDefinition", "lastReviewDate", element.getLastReviewDateElement(), -1);
    if (element.hasEffectivePeriod())
      composePeriod(t, "EventDefinition", "effectivePeriod", element.getEffectivePeriod(), -1);
    for (int i = 0; i < element.getTopic().size(); i++)
      composeCodeableConcept(t, "EventDefinition", "topic", element.getTopic().get(i), i);
    for (int i = 0; i < element.getAuthor().size(); i++)
      composeContactDetail(t, "EventDefinition", "author", element.getAuthor().get(i), i);
    for (int i = 0; i < element.getEditor().size(); i++)
      composeContactDetail(t, "EventDefinition", "editor", element.getEditor().get(i), i);
    for (int i = 0; i < element.getReviewer().size(); i++)
      composeContactDetail(t, "EventDefinition", "reviewer", element.getReviewer().get(i), i);
    for (int i = 0; i < element.getEndorser().size(); i++)
      composeContactDetail(t, "EventDefinition", "endorser", element.getEndorser().get(i), i);
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
    if (element.hasPurposeElement())
      composeMarkdown(t, "ExampleScenario", "purpose", element.getPurposeElement(), -1);
    for (int i = 0; i < element.getActor().size(); i++)
      composeExampleScenarioExampleScenarioActorComponent(t, "ExampleScenario", "actor", element.getActor().get(i), i);
    for (int i = 0; i < element.getInstance().size(); i++)
      composeExampleScenarioExampleScenarioInstanceComponent(t, "ExampleScenario", "instance", element.getInstance().get(i), i);
    for (int i = 0; i < element.getProcess().size(); i++)
      composeExampleScenarioExampleScenarioProcessComponent(t, "ExampleScenario", "process", element.getProcess().get(i), i);
    for (int i = 0; i < element.getWorkflow().size(); i++)
      composeCanonical(t, "ExampleScenario", "workflow", element.getWorkflow().get(i), i);
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
    if (element.hasSubType())
      composeCodeableConcept(t, "ExplanationOfBenefit", "subType", element.getSubType(), -1);
    if (element.hasUseElement())
      composeEnum(t, "ExplanationOfBenefit", "use", element.getUseElement(), -1);
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
    for (int i = 0; i < element.getInsurance().size(); i++)
      composeExplanationOfBenefitInsuranceComponent(t, "ExplanationOfBenefit", "insurance", element.getInsurance().get(i), i);
    if (element.hasAccident())
      composeExplanationOfBenefitAccidentComponent(t, "ExplanationOfBenefit", "accident", element.getAccident(), -1);
    for (int i = 0; i < element.getItem().size(); i++)
      composeExplanationOfBenefitItemComponent(t, "ExplanationOfBenefit", "item", element.getItem().get(i), i);
    for (int i = 0; i < element.getAddItem().size(); i++)
      composeExplanationOfBenefitAddedItemComponent(t, "ExplanationOfBenefit", "addItem", element.getAddItem().get(i), i);
    for (int i = 0; i < element.getTotal().size(); i++)
      composeExplanationOfBenefitTotalComponent(t, "ExplanationOfBenefit", "total", element.getTotal().get(i), i);
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
    if (element.hasOnAdmission())
      composeCodeableConcept(t, "ExplanationOfBenefit", "onAdmission", element.getOnAdmission(), -1);
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
    if (element.hasFocalElement())
      composeBoolean(t, "ExplanationOfBenefit", "focal", element.getFocalElement(), -1);
    if (element.hasCoverage())
      composeReference(t, "ExplanationOfBenefit", "coverage", element.getCoverage(), -1);
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
    for (int i = 0; i < element.getCareTeamSequence().size(); i++)
      composePositiveInt(t, "ExplanationOfBenefit", "careTeamSequence", element.getCareTeamSequence().get(i), i);
    for (int i = 0; i < element.getDiagnosisSequence().size(); i++)
      composePositiveInt(t, "ExplanationOfBenefit", "diagnosisSequence", element.getDiagnosisSequence().get(i), i);
    for (int i = 0; i < element.getProcedureSequence().size(); i++)
      composePositiveInt(t, "ExplanationOfBenefit", "procedureSequence", element.getProcedureSequence().get(i), i);
    for (int i = 0; i < element.getInformationSequence().size(); i++)
      composePositiveInt(t, "ExplanationOfBenefit", "informationSequence", element.getInformationSequence().get(i), i);
    if (element.hasRevenue())
      composeCodeableConcept(t, "ExplanationOfBenefit", "revenue", element.getRevenue(), -1);
    if (element.hasCategory())
      composeCodeableConcept(t, "ExplanationOfBenefit", "category", element.getCategory(), -1);
    if (element.hasBillcode())
      composeCodeableConcept(t, "ExplanationOfBenefit", "billcode", element.getBillcode(), -1);
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
    if (element.hasBillcode())
      composeCodeableConcept(t, "ExplanationOfBenefit", "billcode", element.getBillcode(), -1);
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
    if (element.hasBillcode())
      composeCodeableConcept(t, "ExplanationOfBenefit", "billcode", element.getBillcode(), -1);
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
    for (int i = 0; i < element.getItemSequence().size(); i++)
      composePositiveInt(t, "ExplanationOfBenefit", "itemSequence", element.getItemSequence().get(i), i);
    for (int i = 0; i < element.getDetailSequence().size(); i++)
      composePositiveInt(t, "ExplanationOfBenefit", "detailSequence", element.getDetailSequence().get(i), i);
    for (int i = 0; i < element.getSubDetailSequence().size(); i++)
      composePositiveInt(t, "ExplanationOfBenefit", "subDetailSequence", element.getSubDetailSequence().get(i), i);
    for (int i = 0; i < element.getProvider().size(); i++)
      composeReference(t, "ExplanationOfBenefit", "provider", element.getProvider().get(i), i);
    if (element.hasBillcode())
      composeCodeableConcept(t, "ExplanationOfBenefit", "billcode", element.getBillcode(), -1);
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
    if (element.hasBodySite())
      composeCodeableConcept(t, "ExplanationOfBenefit", "bodySite", element.getBodySite(), -1);
    for (int i = 0; i < element.getSubSite().size(); i++)
      composeCodeableConcept(t, "ExplanationOfBenefit", "subSite", element.getSubSite().get(i), i);
    for (int i = 0; i < element.getNoteNumber().size(); i++)
      composePositiveInt(t, "ExplanationOfBenefit", "noteNumber", element.getNoteNumber().get(i), i);
    for (int i = 0; i < element.getAdjudication().size(); i++)
      composeExplanationOfBenefitAdjudicationComponent(t, "ExplanationOfBenefit", "adjudication", element.getAdjudication().get(i), i);
    for (int i = 0; i < element.getDetail().size(); i++)
      composeExplanationOfBenefitAddedItemDetailComponent(t, "ExplanationOfBenefit", "detail", element.getDetail().get(i), i);
  }

  protected void composeExplanationOfBenefitAddedItemDetailComponent(Complex parent, String parentType, String name, ExplanationOfBenefit.AddedItemDetailComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "detail", name, element, index);
    if (element.hasBillcode())
      composeCodeableConcept(t, "ExplanationOfBenefit", "billcode", element.getBillcode(), -1);
    for (int i = 0; i < element.getModifier().size(); i++)
      composeCodeableConcept(t, "ExplanationOfBenefit", "modifier", element.getModifier().get(i), i);
    if (element.hasQuantity())
      composeQuantity(t, "ExplanationOfBenefit", "quantity", element.getQuantity(), -1);
    if (element.hasUnitPrice())
      composeMoney(t, "ExplanationOfBenefit", "unitPrice", element.getUnitPrice(), -1);
    if (element.hasFactorElement())
      composeDecimal(t, "ExplanationOfBenefit", "factor", element.getFactorElement(), -1);
    if (element.hasNet())
      composeMoney(t, "ExplanationOfBenefit", "net", element.getNet(), -1);
    for (int i = 0; i < element.getNoteNumber().size(); i++)
      composePositiveInt(t, "ExplanationOfBenefit", "noteNumber", element.getNoteNumber().get(i), i);
    for (int i = 0; i < element.getAdjudication().size(); i++)
      composeExplanationOfBenefitAdjudicationComponent(t, "ExplanationOfBenefit", "adjudication", element.getAdjudication().get(i), i);
    for (int i = 0; i < element.getSubDetail().size(); i++)
      composeExplanationOfBenefitAddedItemDetailSubDetailComponent(t, "ExplanationOfBenefit", "subDetail", element.getSubDetail().get(i), i);
  }

  protected void composeExplanationOfBenefitAddedItemDetailSubDetailComponent(Complex parent, String parentType, String name, ExplanationOfBenefit.AddedItemDetailSubDetailComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "subDetail", name, element, index);
    if (element.hasBillcode())
      composeCodeableConcept(t, "ExplanationOfBenefit", "billcode", element.getBillcode(), -1);
    for (int i = 0; i < element.getModifier().size(); i++)
      composeCodeableConcept(t, "ExplanationOfBenefit", "modifier", element.getModifier().get(i), i);
    if (element.hasQuantity())
      composeQuantity(t, "ExplanationOfBenefit", "quantity", element.getQuantity(), -1);
    if (element.hasUnitPrice())
      composeMoney(t, "ExplanationOfBenefit", "unitPrice", element.getUnitPrice(), -1);
    if (element.hasFactorElement())
      composeDecimal(t, "ExplanationOfBenefit", "factor", element.getFactorElement(), -1);
    if (element.hasNet())
      composeMoney(t, "ExplanationOfBenefit", "net", element.getNet(), -1);
    for (int i = 0; i < element.getNoteNumber().size(); i++)
      composePositiveInt(t, "ExplanationOfBenefit", "noteNumber", element.getNoteNumber().get(i), i);
    for (int i = 0; i < element.getAdjudication().size(); i++)
      composeExplanationOfBenefitAdjudicationComponent(t, "ExplanationOfBenefit", "adjudication", element.getAdjudication().get(i), i);
  }

  protected void composeExplanationOfBenefitTotalComponent(Complex parent, String parentType, String name, ExplanationOfBenefit.TotalComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "total", name, element, index);
    if (element.hasCategory())
      composeCodeableConcept(t, "ExplanationOfBenefit", "category", element.getCategory(), -1);
    if (element.hasAmount())
      composeMoney(t, "ExplanationOfBenefit", "amount", element.getAmount(), -1);
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
    for (int i = 0; i < element.getInstantiatesCanonical().size(); i++)
      composeCanonical(t, "FamilyMemberHistory", "instantiatesCanonical", element.getInstantiatesCanonical().get(i), i);
    for (int i = 0; i < element.getInstantiatesUri().size(); i++)
      composeUri(t, "FamilyMemberHistory", "instantiatesUri", element.getInstantiatesUri().get(i), i);
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
    if (element.hasGender())
      composeCodeableConcept(t, "FamilyMemberHistory", "gender", element.getGender(), -1);
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
      composeCanonical(t, "GraphDefinition", "profile", element.getProfileElement(), -1);
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
      composeCanonical(t, "GraphDefinition", "profile", element.getProfileElement(), -1);
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
    if (element.hasRequestIdentifier())
      composeIdentifier(t, "GuidanceResponse", "requestIdentifier", element.getRequestIdentifier(), -1);
    for (int i = 0; i < element.getIdentifier().size(); i++)
      composeIdentifier(t, "GuidanceResponse", "identifier", element.getIdentifier().get(i), i);
    if (element.hasModule())
      composeType(t, "GuidanceResponse", "module", element.getModule(), -1);
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
    for (int i = 0; i < element.getReasonCode().size(); i++)
      composeCodeableConcept(t, "GuidanceResponse", "reasonCode", element.getReasonCode().get(i), i);
    for (int i = 0; i < element.getReasonReference().size(); i++)
      composeReference(t, "GuidanceResponse", "reasonReference", element.getReasonReference().get(i), i);
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
    for (int i = 0; i < element.getCategory().size(); i++)
      composeCodeableConcept(t, "HealthcareService", "category", element.getCategory().get(i), i);
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
    for (int i = 0; i < element.getIdentifier().size(); i++)
      composeIdentifier(t, "ImagingStudy", "identifier", element.getIdentifier().get(i), i);
    if (element.hasStatusElement())
      composeEnum(t, "ImagingStudy", "status", element.getStatusElement(), -1);
    for (int i = 0; i < element.getModality().size(); i++)
      composeCoding(t, "ImagingStudy", "modality", element.getModality().get(i), i);
    if (element.hasSubject())
      composeReference(t, "ImagingStudy", "subject", element.getSubject(), -1);
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
    if (element.hasProcedureReference())
      composeReference(t, "ImagingStudy", "procedureReference", element.getProcedureReference(), -1);
    for (int i = 0; i < element.getProcedureCode().size(); i++)
      composeCodeableConcept(t, "ImagingStudy", "procedureCode", element.getProcedureCode().get(i), i);
    if (element.hasLocation())
      composeReference(t, "ImagingStudy", "location", element.getLocation(), -1);
    for (int i = 0; i < element.getReasonCode().size(); i++)
      composeCodeableConcept(t, "ImagingStudy", "reasonCode", element.getReasonCode().get(i), i);
    for (int i = 0; i < element.getReasonReference().size(); i++)
      composeReference(t, "ImagingStudy", "reasonReference", element.getReasonReference().get(i), i);
    for (int i = 0; i < element.getNote().size(); i++)
      composeAnnotation(t, "ImagingStudy", "note", element.getNote().get(i), i);
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
    if (element.hasIdentifier())
      composeIdentifier(t, "ImagingStudy", "identifier", element.getIdentifier(), -1);
    if (element.hasNumberElement())
      composeUnsignedInt(t, "ImagingStudy", "number", element.getNumberElement(), -1);
    if (element.hasModality())
      composeCoding(t, "ImagingStudy", "modality", element.getModality(), -1);
    if (element.hasDescriptionElement())
      composeString(t, "ImagingStudy", "description", element.getDescriptionElement(), -1);
    if (element.hasNumberOfInstancesElement())
      composeUnsignedInt(t, "ImagingStudy", "numberOfInstances", element.getNumberOfInstancesElement(), -1);
    for (int i = 0; i < element.getEndpoint().size(); i++)
      composeReference(t, "ImagingStudy", "endpoint", element.getEndpoint().get(i), i);
    if (element.hasBodySite())
      composeCoding(t, "ImagingStudy", "bodySite", element.getBodySite(), -1);
    if (element.hasLaterality())
      composeCoding(t, "ImagingStudy", "laterality", element.getLaterality(), -1);
    for (int i = 0; i < element.getSpecimen().size(); i++)
      composeReference(t, "ImagingStudy", "specimen", element.getSpecimen().get(i), i);
    if (element.hasStartedElement())
      composeDateTime(t, "ImagingStudy", "started", element.getStartedElement(), -1);
    for (int i = 0; i < element.getPerformer().size(); i++)
      composeImagingStudyImagingStudySeriesPerformerComponent(t, "ImagingStudy", "performer", element.getPerformer().get(i), i);
    for (int i = 0; i < element.getInstance().size(); i++)
      composeImagingStudyImagingStudySeriesInstanceComponent(t, "ImagingStudy", "instance", element.getInstance().get(i), i);
  }

  protected void composeImagingStudyImagingStudySeriesPerformerComponent(Complex parent, String parentType, String name, ImagingStudy.ImagingStudySeriesPerformerComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "performer", name, element, index);
    if (element.hasFunction())
      composeCodeableConcept(t, "ImagingStudy", "function", element.getFunction(), -1);
    if (element.hasActor())
      composeReference(t, "ImagingStudy", "actor", element.getActor(), -1);
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
    if (element.hasIdentifier())
      composeIdentifier(t, "ImagingStudy", "identifier", element.getIdentifier(), -1);
    if (element.hasNumberElement())
      composeUnsignedInt(t, "ImagingStudy", "number", element.getNumberElement(), -1);
    if (element.hasSopClass())
      composeCoding(t, "ImagingStudy", "sopClass", element.getSopClass(), -1);
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
    if (element.hasStatusReason())
      composeCodeableConcept(t, "Immunization", "statusReason", element.getStatusReason(), -1);
    if (element.hasVaccineCode())
      composeCodeableConcept(t, "Immunization", "vaccineCode", element.getVaccineCode(), -1);
    if (element.hasPatient())
      composeReference(t, "Immunization", "patient", element.getPatient(), -1);
    if (element.hasEncounter())
      composeReference(t, "Immunization", "encounter", element.getEncounter(), -1);
    if (element.hasOccurrence())
      composeType(t, "Immunization", "occurrence", element.getOccurrence(), -1);
    if (element.hasRecordedElement())
      composeDateTime(t, "Immunization", "recorded", element.getRecordedElement(), -1);
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
    for (int i = 0; i < element.getPerformer().size(); i++)
      composeImmunizationImmunizationPerformerComponent(t, "Immunization", "performer", element.getPerformer().get(i), i);
    for (int i = 0; i < element.getNote().size(); i++)
      composeAnnotation(t, "Immunization", "note", element.getNote().get(i), i);
    for (int i = 0; i < element.getReasonCode().size(); i++)
      composeCodeableConcept(t, "Immunization", "reasonCode", element.getReasonCode().get(i), i);
    for (int i = 0; i < element.getReasonReference().size(); i++)
      composeReference(t, "Immunization", "reasonReference", element.getReasonReference().get(i), i);
    if (element.hasIsSubpotentElement())
      composeBoolean(t, "Immunization", "isSubpotent", element.getIsSubpotentElement(), -1);
    for (int i = 0; i < element.getSubpotentReason().size(); i++)
      composeCodeableConcept(t, "Immunization", "subpotentReason", element.getSubpotentReason().get(i), i);
    for (int i = 0; i < element.getEducation().size(); i++)
      composeImmunizationImmunizationEducationComponent(t, "Immunization", "education", element.getEducation().get(i), i);
    for (int i = 0; i < element.getProgramEligibility().size(); i++)
      composeCodeableConcept(t, "Immunization", "programEligibility", element.getProgramEligibility().get(i), i);
    if (element.hasFundingSource())
      composeCodeableConcept(t, "Immunization", "fundingSource", element.getFundingSource(), -1);
    for (int i = 0; i < element.getProtocolApplied().size(); i++)
      composeImmunizationImmunizationProtocolAppliedComponent(t, "Immunization", "protocolApplied", element.getProtocolApplied().get(i), i);
  }

  protected void composeImmunizationImmunizationPerformerComponent(Complex parent, String parentType, String name, Immunization.ImmunizationPerformerComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "performer", name, element, index);
    if (element.hasFunction())
      composeCodeableConcept(t, "Immunization", "function", element.getFunction(), -1);
    if (element.hasActor())
      composeReference(t, "Immunization", "actor", element.getActor(), -1);
  }

  protected void composeImmunizationImmunizationEducationComponent(Complex parent, String parentType, String name, Immunization.ImmunizationEducationComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "education", name, element, index);
    if (element.hasDocumentTypeElement())
      composeString(t, "Immunization", "documentType", element.getDocumentTypeElement(), -1);
    if (element.hasReferenceElement())
      composeUri(t, "Immunization", "reference", element.getReferenceElement(), -1);
    if (element.hasPublicationDateElement())
      composeDateTime(t, "Immunization", "publicationDate", element.getPublicationDateElement(), -1);
    if (element.hasPresentationDateElement())
      composeDateTime(t, "Immunization", "presentationDate", element.getPresentationDateElement(), -1);
  }

  protected void composeImmunizationImmunizationProtocolAppliedComponent(Complex parent, String parentType, String name, Immunization.ImmunizationProtocolAppliedComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "protocolApplied", name, element, index);
    if (element.hasSeriesElement())
      composeString(t, "Immunization", "series", element.getSeriesElement(), -1);
    if (element.hasAuthority())
      composeReference(t, "Immunization", "authority", element.getAuthority(), -1);
    if (element.hasTargetDisease())
      composeCodeableConcept(t, "Immunization", "targetDisease", element.getTargetDisease(), -1);
    if (element.hasDoseNumber())
      composeType(t, "Immunization", "doseNumber", element.getDoseNumber(), -1);
  }

  protected void composeImmunizationEvaluation(Complex parent, String parentType, String name, ImmunizationEvaluation element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "ImmunizationEvaluation", name, element, index);
    for (int i = 0; i < element.getIdentifier().size(); i++)
      composeIdentifier(t, "ImmunizationEvaluation", "identifier", element.getIdentifier().get(i), i);
    if (element.hasStatusElement())
      composeEnum(t, "ImmunizationEvaluation", "status", element.getStatusElement(), -1);
    if (element.hasPatient())
      composeReference(t, "ImmunizationEvaluation", "patient", element.getPatient(), -1);
    if (element.hasDateElement())
      composeDateTime(t, "ImmunizationEvaluation", "date", element.getDateElement(), -1);
    if (element.hasAuthority())
      composeReference(t, "ImmunizationEvaluation", "authority", element.getAuthority(), -1);
    if (element.hasTargetDisease())
      composeCodeableConcept(t, "ImmunizationEvaluation", "targetDisease", element.getTargetDisease(), -1);
    if (element.hasImmunizationEvent())
      composeReference(t, "ImmunizationEvaluation", "immunizationEvent", element.getImmunizationEvent(), -1);
    if (element.hasDoseStatus())
      composeCodeableConcept(t, "ImmunizationEvaluation", "doseStatus", element.getDoseStatus(), -1);
    for (int i = 0; i < element.getDoseStatusReason().size(); i++)
      composeCodeableConcept(t, "ImmunizationEvaluation", "doseStatusReason", element.getDoseStatusReason().get(i), i);
    if (element.hasDescriptionElement())
      composeString(t, "ImmunizationEvaluation", "description", element.getDescriptionElement(), -1);
    if (element.hasSeriesElement())
      composeString(t, "ImmunizationEvaluation", "series", element.getSeriesElement(), -1);
    if (element.hasDoseNumber())
      composeType(t, "ImmunizationEvaluation", "doseNumber", element.getDoseNumber(), -1);
    if (element.hasSeriesDoses())
      composeType(t, "ImmunizationEvaluation", "seriesDoses", element.getSeriesDoses(), -1);
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
    if (element.hasDateElement())
      composeDateTime(t, "ImmunizationRecommendation", "date", element.getDateElement(), -1);
    if (element.hasAuthority())
      composeReference(t, "ImmunizationRecommendation", "authority", element.getAuthority(), -1);
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
    for (int i = 0; i < element.getVaccineCode().size(); i++)
      composeCodeableConcept(t, "ImmunizationRecommendation", "vaccineCode", element.getVaccineCode().get(i), i);
    if (element.hasTargetDisease())
      composeCodeableConcept(t, "ImmunizationRecommendation", "targetDisease", element.getTargetDisease(), -1);
    for (int i = 0; i < element.getContraindicatedVaccineCode().size(); i++)
      composeCodeableConcept(t, "ImmunizationRecommendation", "contraindicatedVaccineCode", element.getContraindicatedVaccineCode().get(i), i);
    if (element.hasForecastStatus())
      composeCodeableConcept(t, "ImmunizationRecommendation", "forecastStatus", element.getForecastStatus(), -1);
    for (int i = 0; i < element.getForecastReason().size(); i++)
      composeCodeableConcept(t, "ImmunizationRecommendation", "forecastReason", element.getForecastReason().get(i), i);
    for (int i = 0; i < element.getDateCriterion().size(); i++)
      composeImmunizationRecommendationImmunizationRecommendationRecommendationDateCriterionComponent(t, "ImmunizationRecommendation", "dateCriterion", element.getDateCriterion().get(i), i);
    if (element.hasDescriptionElement())
      composeString(t, "ImmunizationRecommendation", "description", element.getDescriptionElement(), -1);
    if (element.hasSeriesElement())
      composeString(t, "ImmunizationRecommendation", "series", element.getSeriesElement(), -1);
    if (element.hasDoseNumber())
      composeType(t, "ImmunizationRecommendation", "doseNumber", element.getDoseNumber(), -1);
    if (element.hasSeriesDoses())
      composeType(t, "ImmunizationRecommendation", "seriesDoses", element.getSeriesDoses(), -1);
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
    if (element.hasTitleElement())
      composeString(t, "ImplementationGuide", "title", element.getTitleElement(), -1);
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
    if (element.hasPackageIdElement())
      composeId(t, "ImplementationGuide", "packageId", element.getPackageIdElement(), -1);
    if (element.hasLicenseElement())
      composeEnum(t, "ImplementationGuide", "license", element.getLicenseElement(), -1);
    if (element.hasFhirVersionElement())
      composeId(t, "ImplementationGuide", "fhirVersion", element.getFhirVersionElement(), -1);
    for (int i = 0; i < element.getDependsOn().size(); i++)
      composeImplementationGuideImplementationGuideDependsOnComponent(t, "ImplementationGuide", "dependsOn", element.getDependsOn().get(i), i);
    for (int i = 0; i < element.getGlobal().size(); i++)
      composeImplementationGuideImplementationGuideGlobalComponent(t, "ImplementationGuide", "global", element.getGlobal().get(i), i);
    if (element.hasDefinition())
      composeImplementationGuideImplementationGuideDefinitionComponent(t, "ImplementationGuide", "definition", element.getDefinition(), -1);
    if (element.hasManifest())
      composeImplementationGuideImplementationGuideManifestComponent(t, "ImplementationGuide", "manifest", element.getManifest(), -1);
  }

  protected void composeImplementationGuideImplementationGuideDependsOnComponent(Complex parent, String parentType, String name, ImplementationGuide.ImplementationGuideDependsOnComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "dependsOn", name, element, index);
    if (element.hasUriElement())
      composeCanonical(t, "ImplementationGuide", "uri", element.getUriElement(), -1);
    if (element.hasPackageIdElement())
      composeId(t, "ImplementationGuide", "packageId", element.getPackageIdElement(), -1);
    if (element.hasVersionElement())
      composeString(t, "ImplementationGuide", "version", element.getVersionElement(), -1);
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
    if (element.hasProfileElement())
      composeCanonical(t, "ImplementationGuide", "profile", element.getProfileElement(), -1);
  }

  protected void composeImplementationGuideImplementationGuideDefinitionComponent(Complex parent, String parentType, String name, ImplementationGuide.ImplementationGuideDefinitionComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "definition", name, element, index);
    for (int i = 0; i < element.getPackage().size(); i++)
      composeImplementationGuideImplementationGuideDefinitionPackageComponent(t, "ImplementationGuide", "package", element.getPackage().get(i), i);
    for (int i = 0; i < element.getResource().size(); i++)
      composeImplementationGuideImplementationGuideDefinitionResourceComponent(t, "ImplementationGuide", "resource", element.getResource().get(i), i);
    if (element.hasPage())
      composeImplementationGuideImplementationGuideDefinitionPageComponent(t, "ImplementationGuide", "page", element.getPage(), -1);
    for (int i = 0; i < element.getParameter().size(); i++)
      composeImplementationGuideImplementationGuideDefinitionParameterComponent(t, "ImplementationGuide", "parameter", element.getParameter().get(i), i);
    for (int i = 0; i < element.getTemplate().size(); i++)
      composeImplementationGuideImplementationGuideDefinitionTemplateComponent(t, "ImplementationGuide", "template", element.getTemplate().get(i), i);
  }

  protected void composeImplementationGuideImplementationGuideDefinitionPackageComponent(Complex parent, String parentType, String name, ImplementationGuide.ImplementationGuideDefinitionPackageComponent element, int index) {
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
  }

  protected void composeImplementationGuideImplementationGuideDefinitionResourceComponent(Complex parent, String parentType, String name, ImplementationGuide.ImplementationGuideDefinitionResourceComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "resource", name, element, index);
    if (element.hasReference())
      composeReference(t, "ImplementationGuide", "reference", element.getReference(), -1);
    if (element.hasNameElement())
      composeString(t, "ImplementationGuide", "name", element.getNameElement(), -1);
    if (element.hasDescriptionElement())
      composeString(t, "ImplementationGuide", "description", element.getDescriptionElement(), -1);
    if (element.hasExample())
      composeType(t, "ImplementationGuide", "example", element.getExample(), -1);
    if (element.hasPackageElement())
      composeId(t, "ImplementationGuide", "package", element.getPackageElement(), -1);
  }

  protected void composeImplementationGuideImplementationGuideDefinitionPageComponent(Complex parent, String parentType, String name, ImplementationGuide.ImplementationGuideDefinitionPageComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "page", name, element, index);
    if (element.hasName())
      composeType(t, "ImplementationGuide", "name", element.getName(), -1);
    if (element.hasTitleElement())
      composeString(t, "ImplementationGuide", "title", element.getTitleElement(), -1);
    if (element.hasGenerationElement())
      composeEnum(t, "ImplementationGuide", "generation", element.getGenerationElement(), -1);
    for (int i = 0; i < element.getPage().size(); i++)
      composeImplementationGuideImplementationGuideDefinitionPageComponent(t, "ImplementationGuide", "page", element.getPage().get(i), i);
  }

  protected void composeImplementationGuideImplementationGuideDefinitionParameterComponent(Complex parent, String parentType, String name, ImplementationGuide.ImplementationGuideDefinitionParameterComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "parameter", name, element, index);
    if (element.hasCodeElement())
      composeEnum(t, "ImplementationGuide", "code", element.getCodeElement(), -1);
    if (element.hasValueElement())
      composeString(t, "ImplementationGuide", "value", element.getValueElement(), -1);
  }

  protected void composeImplementationGuideImplementationGuideDefinitionTemplateComponent(Complex parent, String parentType, String name, ImplementationGuide.ImplementationGuideDefinitionTemplateComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "template", name, element, index);
    if (element.hasCodeElement())
      composeCode(t, "ImplementationGuide", "code", element.getCodeElement(), -1);
    if (element.hasSourceElement())
      composeString(t, "ImplementationGuide", "source", element.getSourceElement(), -1);
    if (element.hasScopeElement())
      composeString(t, "ImplementationGuide", "scope", element.getScopeElement(), -1);
  }

  protected void composeImplementationGuideImplementationGuideManifestComponent(Complex parent, String parentType, String name, ImplementationGuide.ImplementationGuideManifestComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "manifest", name, element, index);
    if (element.hasRenderingElement())
      composeUrl(t, "ImplementationGuide", "rendering", element.getRenderingElement(), -1);
    for (int i = 0; i < element.getResource().size(); i++)
      composeImplementationGuideManifestResourceComponent(t, "ImplementationGuide", "resource", element.getResource().get(i), i);
    for (int i = 0; i < element.getPage().size(); i++)
      composeImplementationGuideManifestPageComponent(t, "ImplementationGuide", "page", element.getPage().get(i), i);
    for (int i = 0; i < element.getImage().size(); i++)
      composeString(t, "ImplementationGuide", "image", element.getImage().get(i), i);
    for (int i = 0; i < element.getOther().size(); i++)
      composeString(t, "ImplementationGuide", "other", element.getOther().get(i), i);
  }

  protected void composeImplementationGuideManifestResourceComponent(Complex parent, String parentType, String name, ImplementationGuide.ManifestResourceComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "resource", name, element, index);
    if (element.hasReference())
      composeReference(t, "ImplementationGuide", "reference", element.getReference(), -1);
    if (element.hasExample())
      composeType(t, "ImplementationGuide", "example", element.getExample(), -1);
    if (element.hasRelativePathElement())
      composeUrl(t, "ImplementationGuide", "relativePath", element.getRelativePathElement(), -1);
  }

  protected void composeImplementationGuideManifestPageComponent(Complex parent, String parentType, String name, ImplementationGuide.ManifestPageComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "page", name, element, index);
    if (element.hasNameElement())
      composeString(t, "ImplementationGuide", "name", element.getNameElement(), -1);
    if (element.hasTitleElement())
      composeString(t, "ImplementationGuide", "title", element.getTitleElement(), -1);
    for (int i = 0; i < element.getAnchor().size(); i++)
      composeString(t, "ImplementationGuide", "anchor", element.getAnchor().get(i), i);
  }

  protected void composeInsurancePlan(Complex parent, String parentType, String name, InsurancePlan element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "InsurancePlan", name, element, index);
    for (int i = 0; i < element.getIdentifier().size(); i++)
      composeIdentifier(t, "InsurancePlan", "identifier", element.getIdentifier().get(i), i);
    if (element.hasStatusElement())
      composeEnum(t, "InsurancePlan", "status", element.getStatusElement(), -1);
    for (int i = 0; i < element.getType().size(); i++)
      composeCodeableConcept(t, "InsurancePlan", "type", element.getType().get(i), i);
    if (element.hasNameElement())
      composeString(t, "InsurancePlan", "name", element.getNameElement(), -1);
    for (int i = 0; i < element.getAlias().size(); i++)
      composeString(t, "InsurancePlan", "alias", element.getAlias().get(i), i);
    if (element.hasPeriod())
      composePeriod(t, "InsurancePlan", "period", element.getPeriod(), -1);
    if (element.hasOwnedBy())
      composeReference(t, "InsurancePlan", "ownedBy", element.getOwnedBy(), -1);
    if (element.hasAdministeredBy())
      composeReference(t, "InsurancePlan", "administeredBy", element.getAdministeredBy(), -1);
    for (int i = 0; i < element.getCoverageArea().size(); i++)
      composeReference(t, "InsurancePlan", "coverageArea", element.getCoverageArea().get(i), i);
    for (int i = 0; i < element.getContact().size(); i++)
      composeInsurancePlanInsurancePlanContactComponent(t, "InsurancePlan", "contact", element.getContact().get(i), i);
    for (int i = 0; i < element.getEndpoint().size(); i++)
      composeReference(t, "InsurancePlan", "endpoint", element.getEndpoint().get(i), i);
    for (int i = 0; i < element.getNetwork().size(); i++)
      composeReference(t, "InsurancePlan", "network", element.getNetwork().get(i), i);
    for (int i = 0; i < element.getCoverage().size(); i++)
      composeInsurancePlanInsurancePlanCoverageComponent(t, "InsurancePlan", "coverage", element.getCoverage().get(i), i);
    for (int i = 0; i < element.getPlan().size(); i++)
      composeInsurancePlanInsurancePlanPlanComponent(t, "InsurancePlan", "plan", element.getPlan().get(i), i);
  }

  protected void composeInsurancePlanInsurancePlanContactComponent(Complex parent, String parentType, String name, InsurancePlan.InsurancePlanContactComponent element, int index) {
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
      composeCodeableConcept(t, "InsurancePlan", "purpose", element.getPurpose(), -1);
    if (element.hasName())
      composeHumanName(t, "InsurancePlan", "name", element.getName(), -1);
    for (int i = 0; i < element.getTelecom().size(); i++)
      composeContactPoint(t, "InsurancePlan", "telecom", element.getTelecom().get(i), i);
    if (element.hasAddress())
      composeAddress(t, "InsurancePlan", "address", element.getAddress(), -1);
  }

  protected void composeInsurancePlanInsurancePlanCoverageComponent(Complex parent, String parentType, String name, InsurancePlan.InsurancePlanCoverageComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "coverage", name, element, index);
    if (element.hasType())
      composeCodeableConcept(t, "InsurancePlan", "type", element.getType(), -1);
    for (int i = 0; i < element.getNetwork().size(); i++)
      composeReference(t, "InsurancePlan", "network", element.getNetwork().get(i), i);
    for (int i = 0; i < element.getBenefit().size(); i++)
      composeInsurancePlanCoverageBenefitComponent(t, "InsurancePlan", "benefit", element.getBenefit().get(i), i);
  }

  protected void composeInsurancePlanCoverageBenefitComponent(Complex parent, String parentType, String name, InsurancePlan.CoverageBenefitComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "benefit", name, element, index);
    if (element.hasType())
      composeCodeableConcept(t, "InsurancePlan", "type", element.getType(), -1);
    if (element.hasRequirementElement())
      composeString(t, "InsurancePlan", "requirement", element.getRequirementElement(), -1);
    for (int i = 0; i < element.getLimit().size(); i++)
      composeInsurancePlanCoverageBenefitLimitComponent(t, "InsurancePlan", "limit", element.getLimit().get(i), i);
  }

  protected void composeInsurancePlanCoverageBenefitLimitComponent(Complex parent, String parentType, String name, InsurancePlan.CoverageBenefitLimitComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "limit", name, element, index);
    if (element.hasValue())
      composeQuantity(t, "InsurancePlan", "value", element.getValue(), -1);
    if (element.hasCode())
      composeCodeableConcept(t, "InsurancePlan", "code", element.getCode(), -1);
  }

  protected void composeInsurancePlanInsurancePlanPlanComponent(Complex parent, String parentType, String name, InsurancePlan.InsurancePlanPlanComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "plan", name, element, index);
    for (int i = 0; i < element.getIdentifier().size(); i++)
      composeIdentifier(t, "InsurancePlan", "identifier", element.getIdentifier().get(i), i);
    if (element.hasType())
      composeCodeableConcept(t, "InsurancePlan", "type", element.getType(), -1);
    for (int i = 0; i < element.getCoverageArea().size(); i++)
      composeReference(t, "InsurancePlan", "coverageArea", element.getCoverageArea().get(i), i);
    for (int i = 0; i < element.getNetwork().size(); i++)
      composeReference(t, "InsurancePlan", "network", element.getNetwork().get(i), i);
    for (int i = 0; i < element.getGeneralCost().size(); i++)
      composeInsurancePlanInsurancePlanPlanGeneralCostComponent(t, "InsurancePlan", "generalCost", element.getGeneralCost().get(i), i);
    for (int i = 0; i < element.getSpecificCost().size(); i++)
      composeInsurancePlanInsurancePlanPlanSpecificCostComponent(t, "InsurancePlan", "specificCost", element.getSpecificCost().get(i), i);
  }

  protected void composeInsurancePlanInsurancePlanPlanGeneralCostComponent(Complex parent, String parentType, String name, InsurancePlan.InsurancePlanPlanGeneralCostComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "generalCost", name, element, index);
    if (element.hasType())
      composeCodeableConcept(t, "InsurancePlan", "type", element.getType(), -1);
    if (element.hasGroupSizeElement())
      composePositiveInt(t, "InsurancePlan", "groupSize", element.getGroupSizeElement(), -1);
    if (element.hasCost())
      composeMoney(t, "InsurancePlan", "cost", element.getCost(), -1);
    if (element.hasCommentElement())
      composeString(t, "InsurancePlan", "comment", element.getCommentElement(), -1);
  }

  protected void composeInsurancePlanInsurancePlanPlanSpecificCostComponent(Complex parent, String parentType, String name, InsurancePlan.InsurancePlanPlanSpecificCostComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "specificCost", name, element, index);
    if (element.hasCategory())
      composeCodeableConcept(t, "InsurancePlan", "category", element.getCategory(), -1);
    for (int i = 0; i < element.getBenefit().size(); i++)
      composeInsurancePlanPlanBenefitComponent(t, "InsurancePlan", "benefit", element.getBenefit().get(i), i);
  }

  protected void composeInsurancePlanPlanBenefitComponent(Complex parent, String parentType, String name, InsurancePlan.PlanBenefitComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "benefit", name, element, index);
    if (element.hasType())
      composeCodeableConcept(t, "InsurancePlan", "type", element.getType(), -1);
    for (int i = 0; i < element.getCost().size(); i++)
      composeInsurancePlanPlanBenefitCostComponent(t, "InsurancePlan", "cost", element.getCost().get(i), i);
  }

  protected void composeInsurancePlanPlanBenefitCostComponent(Complex parent, String parentType, String name, InsurancePlan.PlanBenefitCostComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "cost", name, element, index);
    if (element.hasType())
      composeCodeableConcept(t, "InsurancePlan", "type", element.getType(), -1);
    if (element.hasApplicability())
      composeCodeableConcept(t, "InsurancePlan", "applicability", element.getApplicability(), -1);
    for (int i = 0; i < element.getQualifiers().size(); i++)
      composeCodeableConcept(t, "InsurancePlan", "qualifiers", element.getQualifiers().get(i), i);
    if (element.hasValue())
      composeQuantity(t, "InsurancePlan", "value", element.getValue(), -1);
  }

  protected void composeInvoice(Complex parent, String parentType, String name, Invoice element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "Invoice", name, element, index);
    for (int i = 0; i < element.getIdentifier().size(); i++)
      composeIdentifier(t, "Invoice", "identifier", element.getIdentifier().get(i), i);
    if (element.hasStatusElement())
      composeEnum(t, "Invoice", "status", element.getStatusElement(), -1);
    if (element.hasCancelledReasonElement())
      composeString(t, "Invoice", "cancelledReason", element.getCancelledReasonElement(), -1);
    if (element.hasType())
      composeCodeableConcept(t, "Invoice", "type", element.getType(), -1);
    if (element.hasSubject())
      composeReference(t, "Invoice", "subject", element.getSubject(), -1);
    if (element.hasRecipient())
      composeReference(t, "Invoice", "recipient", element.getRecipient(), -1);
    if (element.hasDateElement())
      composeDateTime(t, "Invoice", "date", element.getDateElement(), -1);
    for (int i = 0; i < element.getParticipant().size(); i++)
      composeInvoiceInvoiceParticipantComponent(t, "Invoice", "participant", element.getParticipant().get(i), i);
    if (element.hasIssuer())
      composeReference(t, "Invoice", "issuer", element.getIssuer(), -1);
    if (element.hasAccount())
      composeReference(t, "Invoice", "account", element.getAccount(), -1);
    for (int i = 0; i < element.getLineItem().size(); i++)
      composeInvoiceInvoiceLineItemComponent(t, "Invoice", "lineItem", element.getLineItem().get(i), i);
    for (int i = 0; i < element.getTotalPriceComponent().size(); i++)
      composeInvoiceInvoiceLineItemPriceComponentComponent(t, "Invoice", "totalPriceComponent", element.getTotalPriceComponent().get(i), i);
    if (element.hasTotalNet())
      composeMoney(t, "Invoice", "totalNet", element.getTotalNet(), -1);
    if (element.hasTotalGross())
      composeMoney(t, "Invoice", "totalGross", element.getTotalGross(), -1);
    if (element.hasPaymentTermsElement())
      composeMarkdown(t, "Invoice", "paymentTerms", element.getPaymentTermsElement(), -1);
    for (int i = 0; i < element.getNote().size(); i++)
      composeAnnotation(t, "Invoice", "note", element.getNote().get(i), i);
  }

  protected void composeInvoiceInvoiceParticipantComponent(Complex parent, String parentType, String name, Invoice.InvoiceParticipantComponent element, int index) {
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
      composeCodeableConcept(t, "Invoice", "role", element.getRole(), -1);
    if (element.hasActor())
      composeReference(t, "Invoice", "actor", element.getActor(), -1);
  }

  protected void composeInvoiceInvoiceLineItemComponent(Complex parent, String parentType, String name, Invoice.InvoiceLineItemComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "lineItem", name, element, index);
    if (element.hasSequenceElement())
      composePositiveInt(t, "Invoice", "sequence", element.getSequenceElement(), -1);
    if (element.hasChargeItem())
      composeType(t, "Invoice", "chargeItem", element.getChargeItem(), -1);
    for (int i = 0; i < element.getPriceComponent().size(); i++)
      composeInvoiceInvoiceLineItemPriceComponentComponent(t, "Invoice", "priceComponent", element.getPriceComponent().get(i), i);
  }

  protected void composeInvoiceInvoiceLineItemPriceComponentComponent(Complex parent, String parentType, String name, Invoice.InvoiceLineItemPriceComponentComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "priceComponent", name, element, index);
    if (element.hasTypeElement())
      composeEnum(t, "Invoice", "type", element.getTypeElement(), -1);
    if (element.hasCode())
      composeCodeableConcept(t, "Invoice", "code", element.getCode(), -1);
    if (element.hasFactorElement())
      composeDecimal(t, "Invoice", "factor", element.getFactorElement(), -1);
    if (element.hasAmount())
      composeMoney(t, "Invoice", "amount", element.getAmount(), -1);
  }

  protected void composeItemInstance(Complex parent, String parentType, String name, ItemInstance element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "ItemInstance", name, element, index);
    if (element.hasCountElement())
      composeInteger(t, "ItemInstance", "count", element.getCountElement(), -1);
    if (element.hasLocation())
      composeReference(t, "ItemInstance", "location", element.getLocation(), -1);
    if (element.hasSubject())
      composeReference(t, "ItemInstance", "subject", element.getSubject(), -1);
    if (element.hasManufactureDateElement())
      composeDateTime(t, "ItemInstance", "manufactureDate", element.getManufactureDateElement(), -1);
    if (element.hasExpiryDateElement())
      composeDateTime(t, "ItemInstance", "expiryDate", element.getExpiryDateElement(), -1);
    if (element.hasCurrentSWVersionElement())
      composeString(t, "ItemInstance", "currentSWVersion", element.getCurrentSWVersionElement(), -1);
    if (element.hasLotNumberElement())
      composeString(t, "ItemInstance", "lotNumber", element.getLotNumberElement(), -1);
    if (element.hasSerialNumberElement())
      composeString(t, "ItemInstance", "serialNumber", element.getSerialNumberElement(), -1);
    if (element.hasCarrierAIDCElement())
      composeString(t, "ItemInstance", "carrierAIDC", element.getCarrierAIDCElement(), -1);
    if (element.hasCarrierHRFElement())
      composeString(t, "ItemInstance", "carrierHRF", element.getCarrierHRFElement(), -1);
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
    if (element.hasSubtitleElement())
      composeString(t, "Library", "subtitle", element.getSubtitleElement(), -1);
    if (element.hasStatusElement())
      composeEnum(t, "Library", "status", element.getStatusElement(), -1);
    if (element.hasExperimentalElement())
      composeBoolean(t, "Library", "experimental", element.getExperimentalElement(), -1);
    if (element.hasType())
      composeCodeableConcept(t, "Library", "type", element.getType(), -1);
    if (element.hasSubject())
      composeType(t, "Library", "subject", element.getSubject(), -1);
    if (element.hasDateElement())
      composeDateTime(t, "Library", "date", element.getDateElement(), -1);
    if (element.hasPublisherElement())
      composeString(t, "Library", "publisher", element.getPublisherElement(), -1);
    for (int i = 0; i < element.getContact().size(); i++)
      composeContactDetail(t, "Library", "contact", element.getContact().get(i), i);
    if (element.hasDescriptionElement())
      composeMarkdown(t, "Library", "description", element.getDescriptionElement(), -1);
    for (int i = 0; i < element.getUseContext().size(); i++)
      composeUsageContext(t, "Library", "useContext", element.getUseContext().get(i), i);
    for (int i = 0; i < element.getJurisdiction().size(); i++)
      composeCodeableConcept(t, "Library", "jurisdiction", element.getJurisdiction().get(i), i);
    if (element.hasPurposeElement())
      composeMarkdown(t, "Library", "purpose", element.getPurposeElement(), -1);
    if (element.hasUsageElement())
      composeString(t, "Library", "usage", element.getUsageElement(), -1);
    if (element.hasCopyrightElement())
      composeMarkdown(t, "Library", "copyright", element.getCopyrightElement(), -1);
    if (element.hasApprovalDateElement())
      composeDate(t, "Library", "approvalDate", element.getApprovalDateElement(), -1);
    if (element.hasLastReviewDateElement())
      composeDate(t, "Library", "lastReviewDate", element.getLastReviewDateElement(), -1);
    if (element.hasEffectivePeriod())
      composePeriod(t, "Library", "effectivePeriod", element.getEffectivePeriod(), -1);
    for (int i = 0; i < element.getTopic().size(); i++)
      composeCodeableConcept(t, "Library", "topic", element.getTopic().get(i), i);
    for (int i = 0; i < element.getAuthor().size(); i++)
      composeContactDetail(t, "Library", "author", element.getAuthor().get(i), i);
    for (int i = 0; i < element.getEditor().size(); i++)
      composeContactDetail(t, "Library", "editor", element.getEditor().get(i), i);
    for (int i = 0; i < element.getReviewer().size(); i++)
      composeContactDetail(t, "Library", "reviewer", element.getReviewer().get(i), i);
    for (int i = 0; i < element.getEndorser().size(); i++)
      composeContactDetail(t, "Library", "endorser", element.getEndorser().get(i), i);
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
    for (int i = 0; i < element.getType().size(); i++)
      composeCodeableConcept(t, "Location", "type", element.getType().get(i), i);
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
    for (int i = 0; i < element.getHoursOfOperation().size(); i++)
      composeLocationLocationHoursOfOperationComponent(t, "Location", "hoursOfOperation", element.getHoursOfOperation().get(i), i);
    if (element.hasAvailabilityExceptionsElement())
      composeString(t, "Location", "availabilityExceptions", element.getAvailabilityExceptionsElement(), -1);
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

  protected void composeLocationLocationHoursOfOperationComponent(Complex parent, String parentType, String name, Location.LocationHoursOfOperationComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "hoursOfOperation", name, element, index);
    for (int i = 0; i < element.getDaysOfWeek().size(); i++)
      composeEnum(t, "Location", "daysOfWeek", element.getDaysOfWeek().get(i), i);
    if (element.hasAllDayElement())
      composeBoolean(t, "Location", "allDay", element.getAllDayElement(), -1);
    if (element.hasOpeningTimeElement())
      composeTime(t, "Location", "openingTime", element.getOpeningTimeElement(), -1);
    if (element.hasClosingTimeElement())
      composeTime(t, "Location", "closingTime", element.getClosingTimeElement(), -1);
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
    if (element.hasSubtitleElement())
      composeString(t, "Measure", "subtitle", element.getSubtitleElement(), -1);
    if (element.hasStatusElement())
      composeEnum(t, "Measure", "status", element.getStatusElement(), -1);
    if (element.hasExperimentalElement())
      composeBoolean(t, "Measure", "experimental", element.getExperimentalElement(), -1);
    if (element.hasSubject())
      composeType(t, "Measure", "subject", element.getSubject(), -1);
    if (element.hasDateElement())
      composeDateTime(t, "Measure", "date", element.getDateElement(), -1);
    if (element.hasPublisherElement())
      composeString(t, "Measure", "publisher", element.getPublisherElement(), -1);
    for (int i = 0; i < element.getContact().size(); i++)
      composeContactDetail(t, "Measure", "contact", element.getContact().get(i), i);
    if (element.hasDescriptionElement())
      composeMarkdown(t, "Measure", "description", element.getDescriptionElement(), -1);
    for (int i = 0; i < element.getUseContext().size(); i++)
      composeUsageContext(t, "Measure", "useContext", element.getUseContext().get(i), i);
    for (int i = 0; i < element.getJurisdiction().size(); i++)
      composeCodeableConcept(t, "Measure", "jurisdiction", element.getJurisdiction().get(i), i);
    if (element.hasPurposeElement())
      composeMarkdown(t, "Measure", "purpose", element.getPurposeElement(), -1);
    if (element.hasUsageElement())
      composeString(t, "Measure", "usage", element.getUsageElement(), -1);
    if (element.hasCopyrightElement())
      composeMarkdown(t, "Measure", "copyright", element.getCopyrightElement(), -1);
    if (element.hasApprovalDateElement())
      composeDate(t, "Measure", "approvalDate", element.getApprovalDateElement(), -1);
    if (element.hasLastReviewDateElement())
      composeDate(t, "Measure", "lastReviewDate", element.getLastReviewDateElement(), -1);
    if (element.hasEffectivePeriod())
      composePeriod(t, "Measure", "effectivePeriod", element.getEffectivePeriod(), -1);
    for (int i = 0; i < element.getTopic().size(); i++)
      composeCodeableConcept(t, "Measure", "topic", element.getTopic().get(i), i);
    for (int i = 0; i < element.getAuthor().size(); i++)
      composeContactDetail(t, "Measure", "author", element.getAuthor().get(i), i);
    for (int i = 0; i < element.getEditor().size(); i++)
      composeContactDetail(t, "Measure", "editor", element.getEditor().get(i), i);
    for (int i = 0; i < element.getReviewer().size(); i++)
      composeContactDetail(t, "Measure", "reviewer", element.getReviewer().get(i), i);
    for (int i = 0; i < element.getEndorser().size(); i++)
      composeContactDetail(t, "Measure", "endorser", element.getEndorser().get(i), i);
    for (int i = 0; i < element.getRelatedArtifact().size(); i++)
      composeRelatedArtifact(t, "Measure", "relatedArtifact", element.getRelatedArtifact().get(i), i);
    for (int i = 0; i < element.getLibrary().size(); i++)
      composeCanonical(t, "Measure", "library", element.getLibrary().get(i), i);
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
      composeEnum(t, "Measure", "improvementNotation", element.getImprovementNotationElement(), -1);
    for (int i = 0; i < element.getDefinition().size(); i++)
      composeMarkdown(t, "Measure", "definition", element.getDefinition().get(i), i);
    if (element.hasGuidanceElement())
      composeMarkdown(t, "Measure", "guidance", element.getGuidanceElement(), -1);
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
    if (element.hasCode())
      composeCodeableConcept(t, "Measure", "code", element.getCode(), -1);
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
    if (element.hasCode())
      composeCodeableConcept(t, "Measure", "code", element.getCode(), -1);
    if (element.hasDescriptionElement())
      composeString(t, "Measure", "description", element.getDescriptionElement(), -1);
    if (element.hasCriteria())
      composeExpression(t, "Measure", "criteria", element.getCriteria(), -1);
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
    if (element.hasCode())
      composeCodeableConcept(t, "Measure", "code", element.getCode(), -1);
    if (element.hasDescriptionElement())
      composeString(t, "Measure", "description", element.getDescriptionElement(), -1);
    if (element.hasCriteria())
      composeExpression(t, "Measure", "criteria", element.getCriteria(), -1);
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
    if (element.hasCode())
      composeCodeableConcept(t, "Measure", "code", element.getCode(), -1);
    for (int i = 0; i < element.getUsage().size(); i++)
      composeCodeableConcept(t, "Measure", "usage", element.getUsage().get(i), i);
    if (element.hasDescriptionElement())
      composeString(t, "Measure", "description", element.getDescriptionElement(), -1);
    if (element.hasCriteria())
      composeExpression(t, "Measure", "criteria", element.getCriteria(), -1);
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
    for (int i = 0; i < element.getIdentifier().size(); i++)
      composeIdentifier(t, "MeasureReport", "identifier", element.getIdentifier().get(i), i);
    if (element.hasStatusElement())
      composeEnum(t, "MeasureReport", "status", element.getStatusElement(), -1);
    if (element.hasTypeElement())
      composeEnum(t, "MeasureReport", "type", element.getTypeElement(), -1);
    if (element.hasMeasureElement())
      composeCanonical(t, "MeasureReport", "measure", element.getMeasureElement(), -1);
    if (element.hasSubject())
      composeReference(t, "MeasureReport", "subject", element.getSubject(), -1);
    if (element.hasDateElement())
      composeDateTime(t, "MeasureReport", "date", element.getDateElement(), -1);
    if (element.hasReporter())
      composeReference(t, "MeasureReport", "reporter", element.getReporter(), -1);
    if (element.hasPeriod())
      composePeriod(t, "MeasureReport", "period", element.getPeriod(), -1);
    if (element.hasImprovementNotationElement())
      composeEnum(t, "MeasureReport", "improvementNotation", element.getImprovementNotationElement(), -1);
    for (int i = 0; i < element.getGroup().size(); i++)
      composeMeasureReportMeasureReportGroupComponent(t, "MeasureReport", "group", element.getGroup().get(i), i);
    for (int i = 0; i < element.getEvaluatedResource().size(); i++)
      composeReference(t, "MeasureReport", "evaluatedResource", element.getEvaluatedResource().get(i), i);
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
    if (element.hasCode())
      composeCodeableConcept(t, "MeasureReport", "code", element.getCode(), -1);
    for (int i = 0; i < element.getPopulation().size(); i++)
      composeMeasureReportMeasureReportGroupPopulationComponent(t, "MeasureReport", "population", element.getPopulation().get(i), i);
    if (element.hasMeasureScore())
      composeQuantity(t, "MeasureReport", "measureScore", element.getMeasureScore(), -1);
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
    if (element.hasCode())
      composeCodeableConcept(t, "MeasureReport", "code", element.getCode(), -1);
    if (element.hasCountElement())
      composeInteger(t, "MeasureReport", "count", element.getCountElement(), -1);
    if (element.hasSubjectResults())
      composeReference(t, "MeasureReport", "subjectResults", element.getSubjectResults(), -1);
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
    if (element.hasCode())
      composeCodeableConcept(t, "MeasureReport", "code", element.getCode(), -1);
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
    if (element.hasValue())
      composeCodeableConcept(t, "MeasureReport", "value", element.getValue(), -1);
    for (int i = 0; i < element.getPopulation().size(); i++)
      composeMeasureReportStratifierGroupPopulationComponent(t, "MeasureReport", "population", element.getPopulation().get(i), i);
    if (element.hasMeasureScore())
      composeQuantity(t, "MeasureReport", "measureScore", element.getMeasureScore(), -1);
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
    if (element.hasCode())
      composeCodeableConcept(t, "MeasureReport", "code", element.getCode(), -1);
    if (element.hasCountElement())
      composeInteger(t, "MeasureReport", "count", element.getCountElement(), -1);
    if (element.hasSubjectResults())
      composeReference(t, "MeasureReport", "subjectResults", element.getSubjectResults(), -1);
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
    for (int i = 0; i < element.getPartOf().size(); i++)
      composeReference(t, "Media", "partOf", element.getPartOf().get(i), i);
    if (element.hasStatusElement())
      composeEnum(t, "Media", "status", element.getStatusElement(), -1);
    if (element.hasType())
      composeCodeableConcept(t, "Media", "type", element.getType(), -1);
    if (element.hasModality())
      composeCodeableConcept(t, "Media", "modality", element.getModality(), -1);
    if (element.hasView())
      composeCodeableConcept(t, "Media", "view", element.getView(), -1);
    if (element.hasSubject())
      composeReference(t, "Media", "subject", element.getSubject(), -1);
    if (element.hasContext())
      composeReference(t, "Media", "context", element.getContext(), -1);
    if (element.hasCreated())
      composeType(t, "Media", "created", element.getCreated(), -1);
    if (element.hasIssuedElement())
      composeInstant(t, "Media", "issued", element.getIssuedElement(), -1);
    if (element.hasOperator())
      composeReference(t, "Media", "operator", element.getOperator(), -1);
    for (int i = 0; i < element.getReasonCode().size(); i++)
      composeCodeableConcept(t, "Media", "reasonCode", element.getReasonCode().get(i), i);
    if (element.hasBodySite())
      composeCodeableConcept(t, "Media", "bodySite", element.getBodySite(), -1);
    if (element.hasDeviceNameElement())
      composeString(t, "Media", "deviceName", element.getDeviceNameElement(), -1);
    if (element.hasDevice())
      composeReference(t, "Media", "device", element.getDevice(), -1);
    if (element.hasHeightElement())
      composePositiveInt(t, "Media", "height", element.getHeightElement(), -1);
    if (element.hasWidthElement())
      composePositiveInt(t, "Media", "width", element.getWidthElement(), -1);
    if (element.hasFramesElement())
      composePositiveInt(t, "Media", "frames", element.getFramesElement(), -1);
    if (element.hasDurationElement())
      composeDecimal(t, "Media", "duration", element.getDurationElement(), -1);
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
    if (element.hasManufacturer())
      composeReference(t, "Medication", "manufacturer", element.getManufacturer(), -1);
    if (element.hasForm())
      composeCodeableConcept(t, "Medication", "form", element.getForm(), -1);
    if (element.hasAmount())
      composeQuantity(t, "Medication", "amount", element.getAmount(), -1);
    for (int i = 0; i < element.getIngredient().size(); i++)
      composeMedicationMedicationIngredientComponent(t, "Medication", "ingredient", element.getIngredient().get(i), i);
    if (element.hasBatch())
      composeMedicationMedicationBatchComponent(t, "Medication", "batch", element.getBatch(), -1);
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

  protected void composeMedicationMedicationBatchComponent(Complex parent, String parentType, String name, Medication.MedicationBatchComponent element, int index) {
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
    if (element.hasSerialNumberElement())
      composeString(t, "Medication", "serialNumber", element.getSerialNumberElement(), -1);
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
    for (int i = 0; i < element.getInstantiates().size(); i++)
      composeUri(t, "MedicationAdministration", "instantiates", element.getInstantiates().get(i), i);
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
    for (int i = 0; i < element.getStatusReason().size(); i++)
      composeCodeableConcept(t, "MedicationAdministration", "statusReason", element.getStatusReason().get(i), i);
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
    if (element.hasFunction())
      composeCodeableConcept(t, "MedicationAdministration", "function", element.getFunction(), -1);
    if (element.hasActor())
      composeReference(t, "MedicationAdministration", "actor", element.getActor(), -1);
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
    if (element.hasLocation())
      composeReference(t, "MedicationDispense", "location", element.getLocation(), -1);
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
    if (element.hasStatusReason())
      composeType(t, "MedicationDispense", "statusReason", element.getStatusReason(), -1);
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
    if (element.hasFunction())
      composeCodeableConcept(t, "MedicationDispense", "function", element.getFunction(), -1);
    if (element.hasActor())
      composeReference(t, "MedicationDispense", "actor", element.getActor(), -1);
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

  protected void composeMedicationKnowledge(Complex parent, String parentType, String name, MedicationKnowledge element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "MedicationKnowledge", name, element, index);
    if (element.hasCode())
      composeCodeableConcept(t, "MedicationKnowledge", "code", element.getCode(), -1);
    if (element.hasStatusElement())
      composeEnum(t, "MedicationKnowledge", "status", element.getStatusElement(), -1);
    if (element.hasManufacturer())
      composeReference(t, "MedicationKnowledge", "manufacturer", element.getManufacturer(), -1);
    if (element.hasDoseForm())
      composeCodeableConcept(t, "MedicationKnowledge", "doseForm", element.getDoseForm(), -1);
    if (element.hasAmount())
      composeQuantity(t, "MedicationKnowledge", "amount", element.getAmount(), -1);
    for (int i = 0; i < element.getSynonym().size(); i++)
      composeString(t, "MedicationKnowledge", "synonym", element.getSynonym().get(i), i);
    for (int i = 0; i < element.getRelatedMedicationKnowledge().size(); i++)
      composeMedicationKnowledgeMedicationKnowledgeRelatedMedicationKnowledgeComponent(t, "MedicationKnowledge", "relatedMedicationKnowledge", element.getRelatedMedicationKnowledge().get(i), i);
    for (int i = 0; i < element.getAssociatedMedication().size(); i++)
      composeReference(t, "MedicationKnowledge", "associatedMedication", element.getAssociatedMedication().get(i), i);
    for (int i = 0; i < element.getProductType().size(); i++)
      composeCodeableConcept(t, "MedicationKnowledge", "productType", element.getProductType().get(i), i);
    for (int i = 0; i < element.getMonograph().size(); i++)
      composeMedicationKnowledgeMedicationKnowledgeMonographComponent(t, "MedicationKnowledge", "monograph", element.getMonograph().get(i), i);
    for (int i = 0; i < element.getIngredient().size(); i++)
      composeMedicationKnowledgeMedicationKnowledgeIngredientComponent(t, "MedicationKnowledge", "ingredient", element.getIngredient().get(i), i);
    if (element.hasPreparationInstructionElement())
      composeMarkdown(t, "MedicationKnowledge", "preparationInstruction", element.getPreparationInstructionElement(), -1);
    for (int i = 0; i < element.getIntendedRoute().size(); i++)
      composeCodeableConcept(t, "MedicationKnowledge", "intendedRoute", element.getIntendedRoute().get(i), i);
    for (int i = 0; i < element.getCost().size(); i++)
      composeMedicationKnowledgeMedicationKnowledgeCostComponent(t, "MedicationKnowledge", "cost", element.getCost().get(i), i);
    for (int i = 0; i < element.getMonitoringProgram().size(); i++)
      composeMedicationKnowledgeMedicationKnowledgeMonitoringProgramComponent(t, "MedicationKnowledge", "monitoringProgram", element.getMonitoringProgram().get(i), i);
    for (int i = 0; i < element.getAdministrationGuidelines().size(); i++)
      composeMedicationKnowledgeMedicationKnowledgeAdministrationGuidelinesComponent(t, "MedicationKnowledge", "administrationGuidelines", element.getAdministrationGuidelines().get(i), i);
    for (int i = 0; i < element.getMedicineClassification().size(); i++)
      composeMedicationKnowledgeMedicationKnowledgeMedicineClassificationComponent(t, "MedicationKnowledge", "medicineClassification", element.getMedicineClassification().get(i), i);
    if (element.hasPackaging())
      composeMedicationKnowledgeMedicationKnowledgePackagingComponent(t, "MedicationKnowledge", "packaging", element.getPackaging(), -1);
    for (int i = 0; i < element.getDrugCharacteristic().size(); i++)
      composeMedicationKnowledgeMedicationKnowledgeDrugCharacteristicComponent(t, "MedicationKnowledge", "drugCharacteristic", element.getDrugCharacteristic().get(i), i);
    for (int i = 0; i < element.getContraindication().size(); i++)
      composeReference(t, "MedicationKnowledge", "contraindication", element.getContraindication().get(i), i);
    for (int i = 0; i < element.getRegulatory().size(); i++)
      composeMedicationKnowledgeMedicationKnowledgeRegulatoryComponent(t, "MedicationKnowledge", "regulatory", element.getRegulatory().get(i), i);
    for (int i = 0; i < element.getKinetics().size(); i++)
      composeMedicationKnowledgeMedicationKnowledgeKineticsComponent(t, "MedicationKnowledge", "kinetics", element.getKinetics().get(i), i);
  }

  protected void composeMedicationKnowledgeMedicationKnowledgeRelatedMedicationKnowledgeComponent(Complex parent, String parentType, String name, MedicationKnowledge.MedicationKnowledgeRelatedMedicationKnowledgeComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "relatedMedicationKnowledge", name, element, index);
    if (element.hasType())
      composeCodeableConcept(t, "MedicationKnowledge", "type", element.getType(), -1);
    for (int i = 0; i < element.getReference().size(); i++)
      composeReference(t, "MedicationKnowledge", "reference", element.getReference().get(i), i);
  }

  protected void composeMedicationKnowledgeMedicationKnowledgeMonographComponent(Complex parent, String parentType, String name, MedicationKnowledge.MedicationKnowledgeMonographComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "monograph", name, element, index);
    if (element.hasType())
      composeCodeableConcept(t, "MedicationKnowledge", "type", element.getType(), -1);
    if (element.hasSource())
      composeReference(t, "MedicationKnowledge", "source", element.getSource(), -1);
  }

  protected void composeMedicationKnowledgeMedicationKnowledgeIngredientComponent(Complex parent, String parentType, String name, MedicationKnowledge.MedicationKnowledgeIngredientComponent element, int index) {
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
      composeType(t, "MedicationKnowledge", "item", element.getItem(), -1);
    if (element.hasIsActiveElement())
      composeBoolean(t, "MedicationKnowledge", "isActive", element.getIsActiveElement(), -1);
    if (element.hasStrength())
      composeRatio(t, "MedicationKnowledge", "strength", element.getStrength(), -1);
  }

  protected void composeMedicationKnowledgeMedicationKnowledgeCostComponent(Complex parent, String parentType, String name, MedicationKnowledge.MedicationKnowledgeCostComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "cost", name, element, index);
    if (element.hasType())
      composeCodeableConcept(t, "MedicationKnowledge", "type", element.getType(), -1);
    if (element.hasSourceElement())
      composeString(t, "MedicationKnowledge", "source", element.getSourceElement(), -1);
    if (element.hasCost())
      composeMoney(t, "MedicationKnowledge", "cost", element.getCost(), -1);
  }

  protected void composeMedicationKnowledgeMedicationKnowledgeMonitoringProgramComponent(Complex parent, String parentType, String name, MedicationKnowledge.MedicationKnowledgeMonitoringProgramComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "monitoringProgram", name, element, index);
    if (element.hasType())
      composeCodeableConcept(t, "MedicationKnowledge", "type", element.getType(), -1);
    if (element.hasNameElement())
      composeString(t, "MedicationKnowledge", "name", element.getNameElement(), -1);
  }

  protected void composeMedicationKnowledgeMedicationKnowledgeAdministrationGuidelinesComponent(Complex parent, String parentType, String name, MedicationKnowledge.MedicationKnowledgeAdministrationGuidelinesComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "administrationGuidelines", name, element, index);
    for (int i = 0; i < element.getDosage().size(); i++)
      composeMedicationKnowledgeMedicationKnowledgeAdministrationGuidelinesDosageComponent(t, "MedicationKnowledge", "dosage", element.getDosage().get(i), i);
    if (element.hasIndication())
      composeType(t, "MedicationKnowledge", "indication", element.getIndication(), -1);
    for (int i = 0; i < element.getPatientCharacteristics().size(); i++)
      composeMedicationKnowledgeMedicationKnowledgeAdministrationGuidelinesPatientCharacteristicsComponent(t, "MedicationKnowledge", "patientCharacteristics", element.getPatientCharacteristics().get(i), i);
  }

  protected void composeMedicationKnowledgeMedicationKnowledgeAdministrationGuidelinesDosageComponent(Complex parent, String parentType, String name, MedicationKnowledge.MedicationKnowledgeAdministrationGuidelinesDosageComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "dosage", name, element, index);
    if (element.hasType())
      composeCodeableConcept(t, "MedicationKnowledge", "type", element.getType(), -1);
    for (int i = 0; i < element.getDosage().size(); i++)
      composeDosage(t, "MedicationKnowledge", "dosage", element.getDosage().get(i), i);
  }

  protected void composeMedicationKnowledgeMedicationKnowledgeAdministrationGuidelinesPatientCharacteristicsComponent(Complex parent, String parentType, String name, MedicationKnowledge.MedicationKnowledgeAdministrationGuidelinesPatientCharacteristicsComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "patientCharacteristics", name, element, index);
    if (element.hasCharacteristic())
      composeType(t, "MedicationKnowledge", "characteristic", element.getCharacteristic(), -1);
    for (int i = 0; i < element.getValue().size(); i++)
      composeString(t, "MedicationKnowledge", "value", element.getValue().get(i), i);
  }

  protected void composeMedicationKnowledgeMedicationKnowledgeMedicineClassificationComponent(Complex parent, String parentType, String name, MedicationKnowledge.MedicationKnowledgeMedicineClassificationComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "medicineClassification", name, element, index);
    if (element.hasType())
      composeCodeableConcept(t, "MedicationKnowledge", "type", element.getType(), -1);
    for (int i = 0; i < element.getClassification().size(); i++)
      composeCodeableConcept(t, "MedicationKnowledge", "classification", element.getClassification().get(i), i);
  }

  protected void composeMedicationKnowledgeMedicationKnowledgePackagingComponent(Complex parent, String parentType, String name, MedicationKnowledge.MedicationKnowledgePackagingComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "packaging", name, element, index);
    if (element.hasType())
      composeCodeableConcept(t, "MedicationKnowledge", "type", element.getType(), -1);
    if (element.hasQuantity())
      composeQuantity(t, "MedicationKnowledge", "quantity", element.getQuantity(), -1);
  }

  protected void composeMedicationKnowledgeMedicationKnowledgeDrugCharacteristicComponent(Complex parent, String parentType, String name, MedicationKnowledge.MedicationKnowledgeDrugCharacteristicComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "drugCharacteristic", name, element, index);
    if (element.hasType())
      composeCodeableConcept(t, "MedicationKnowledge", "type", element.getType(), -1);
    if (element.hasValue())
      composeType(t, "MedicationKnowledge", "value", element.getValue(), -1);
  }

  protected void composeMedicationKnowledgeMedicationKnowledgeRegulatoryComponent(Complex parent, String parentType, String name, MedicationKnowledge.MedicationKnowledgeRegulatoryComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "regulatory", name, element, index);
    if (element.hasRegulatoryAuthority())
      composeReference(t, "MedicationKnowledge", "regulatoryAuthority", element.getRegulatoryAuthority(), -1);
    for (int i = 0; i < element.getSubstitution().size(); i++)
      composeMedicationKnowledgeMedicationKnowledgeRegulatorySubstitutionComponent(t, "MedicationKnowledge", "substitution", element.getSubstitution().get(i), i);
    for (int i = 0; i < element.getSchedule().size(); i++)
      composeMedicationKnowledgeMedicationKnowledgeRegulatoryScheduleComponent(t, "MedicationKnowledge", "schedule", element.getSchedule().get(i), i);
    if (element.hasMaxDispense())
      composeMedicationKnowledgeMedicationKnowledgeRegulatoryMaxDispenseComponent(t, "MedicationKnowledge", "maxDispense", element.getMaxDispense(), -1);
  }

  protected void composeMedicationKnowledgeMedicationKnowledgeRegulatorySubstitutionComponent(Complex parent, String parentType, String name, MedicationKnowledge.MedicationKnowledgeRegulatorySubstitutionComponent element, int index) {
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
      composeCodeableConcept(t, "MedicationKnowledge", "type", element.getType(), -1);
    if (element.hasAllowedElement())
      composeBoolean(t, "MedicationKnowledge", "allowed", element.getAllowedElement(), -1);
  }

  protected void composeMedicationKnowledgeMedicationKnowledgeRegulatoryScheduleComponent(Complex parent, String parentType, String name, MedicationKnowledge.MedicationKnowledgeRegulatoryScheduleComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "schedule", name, element, index);
    if (element.hasSchedule())
      composeCodeableConcept(t, "MedicationKnowledge", "schedule", element.getSchedule(), -1);
  }

  protected void composeMedicationKnowledgeMedicationKnowledgeRegulatoryMaxDispenseComponent(Complex parent, String parentType, String name, MedicationKnowledge.MedicationKnowledgeRegulatoryMaxDispenseComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "maxDispense", name, element, index);
    if (element.hasQuantity())
      composeQuantity(t, "MedicationKnowledge", "quantity", element.getQuantity(), -1);
    if (element.hasPeriod())
      composeDuration(t, "MedicationKnowledge", "period", element.getPeriod(), -1);
  }

  protected void composeMedicationKnowledgeMedicationKnowledgeKineticsComponent(Complex parent, String parentType, String name, MedicationKnowledge.MedicationKnowledgeKineticsComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "kinetics", name, element, index);
    for (int i = 0; i < element.getAreaUnderCurve().size(); i++)
      composeQuantity(t, "MedicationKnowledge", "areaUnderCurve", element.getAreaUnderCurve().get(i), i);
    for (int i = 0; i < element.getLethalDose50().size(); i++)
      composeQuantity(t, "MedicationKnowledge", "lethalDose50", element.getLethalDose50().get(i), i);
    if (element.hasHalfLifePeriod())
      composeDuration(t, "MedicationKnowledge", "halfLifePeriod", element.getHalfLifePeriod(), -1);
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
    if (element.hasStatusElement())
      composeEnum(t, "MedicationRequest", "status", element.getStatusElement(), -1);
    if (element.hasIntentElement())
      composeEnum(t, "MedicationRequest", "intent", element.getIntentElement(), -1);
    for (int i = 0; i < element.getCategory().size(); i++)
      composeCodeableConcept(t, "MedicationRequest", "category", element.getCategory().get(i), i);
    if (element.hasPriorityElement())
      composeEnum(t, "MedicationRequest", "priority", element.getPriorityElement(), -1);
    if (element.hasDoNotPerformElement())
      composeBoolean(t, "MedicationRequest", "doNotPerform", element.getDoNotPerformElement(), -1);
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
      composeReference(t, "MedicationRequest", "requester", element.getRequester(), -1);
    if (element.hasPerformer())
      composeReference(t, "MedicationRequest", "performer", element.getPerformer(), -1);
    if (element.hasPerformerType())
      composeCodeableConcept(t, "MedicationRequest", "performerType", element.getPerformerType(), -1);
    if (element.hasRecorder())
      composeReference(t, "MedicationRequest", "recorder", element.getRecorder(), -1);
    for (int i = 0; i < element.getReasonCode().size(); i++)
      composeCodeableConcept(t, "MedicationRequest", "reasonCode", element.getReasonCode().get(i), i);
    for (int i = 0; i < element.getReasonReference().size(); i++)
      composeReference(t, "MedicationRequest", "reasonReference", element.getReasonReference().get(i), i);
    for (int i = 0; i < element.getInstantiates().size(); i++)
      composeUri(t, "MedicationRequest", "instantiates", element.getInstantiates().get(i), i);
    for (int i = 0; i < element.getBasedOn().size(); i++)
      composeReference(t, "MedicationRequest", "basedOn", element.getBasedOn().get(i), i);
    if (element.hasGroupIdentifier())
      composeIdentifier(t, "MedicationRequest", "groupIdentifier", element.getGroupIdentifier(), -1);
    if (element.hasStatusReason())
      composeCodeableConcept(t, "MedicationRequest", "statusReason", element.getStatusReason(), -1);
    if (element.hasCourseOfTherapyType())
      composeCodeableConcept(t, "MedicationRequest", "courseOfTherapyType", element.getCourseOfTherapyType(), -1);
    for (int i = 0; i < element.getInsurance().size(); i++)
      composeReference(t, "MedicationRequest", "insurance", element.getInsurance().get(i), i);
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
    if (element.hasInitialFill())
      composeMedicationRequestMedicationRequestDispenseRequestInitialFillComponent(t, "MedicationRequest", "initialFill", element.getInitialFill(), -1);
    if (element.hasDispenseInterval())
      composeDuration(t, "MedicationRequest", "dispenseInterval", element.getDispenseInterval(), -1);
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

  protected void composeMedicationRequestMedicationRequestDispenseRequestInitialFillComponent(Complex parent, String parentType, String name, MedicationRequest.MedicationRequestDispenseRequestInitialFillComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "initialFill", name, element, index);
    if (element.hasQuantity())
      composeQuantity(t, "MedicationRequest", "quantity", element.getQuantity(), -1);
    if (element.hasDuration())
      composeDuration(t, "MedicationRequest", "duration", element.getDuration(), -1);
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
    if (element.hasStatusElement())
      composeEnum(t, "MedicationStatement", "status", element.getStatusElement(), -1);
    for (int i = 0; i < element.getStatusReason().size(); i++)
      composeCodeableConcept(t, "MedicationStatement", "statusReason", element.getStatusReason().get(i), i);
    if (element.hasCategory())
      composeCodeableConcept(t, "MedicationStatement", "category", element.getCategory(), -1);
    if (element.hasMedication())
      composeType(t, "MedicationStatement", "medication", element.getMedication(), -1);
    if (element.hasSubject())
      composeReference(t, "MedicationStatement", "subject", element.getSubject(), -1);
    if (element.hasContext())
      composeReference(t, "MedicationStatement", "context", element.getContext(), -1);
    if (element.hasEffective())
      composeType(t, "MedicationStatement", "effective", element.getEffective(), -1);
    if (element.hasDateAssertedElement())
      composeDateTime(t, "MedicationStatement", "dateAsserted", element.getDateAssertedElement(), -1);
    if (element.hasInformationSource())
      composeReference(t, "MedicationStatement", "informationSource", element.getInformationSource(), -1);
    for (int i = 0; i < element.getDerivedFrom().size(); i++)
      composeReference(t, "MedicationStatement", "derivedFrom", element.getDerivedFrom().get(i), i);
    for (int i = 0; i < element.getReasonCode().size(); i++)
      composeCodeableConcept(t, "MedicationStatement", "reasonCode", element.getReasonCode().get(i), i);
    for (int i = 0; i < element.getReasonReference().size(); i++)
      composeReference(t, "MedicationStatement", "reasonReference", element.getReasonReference().get(i), i);
    for (int i = 0; i < element.getNote().size(); i++)
      composeAnnotation(t, "MedicationStatement", "note", element.getNote().get(i), i);
    for (int i = 0; i < element.getDosage().size(); i++)
      composeDosage(t, "MedicationStatement", "dosage", element.getDosage().get(i), i);
  }

  protected void composeMedicinalProduct(Complex parent, String parentType, String name, MedicinalProduct element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "MedicinalProduct", name, element, index);
    for (int i = 0; i < element.getIdentifier().size(); i++)
      composeIdentifier(t, "MedicinalProduct", "identifier", element.getIdentifier().get(i), i);
    if (element.hasType())
      composeCodeableConcept(t, "MedicinalProduct", "type", element.getType(), -1);
    if (element.hasDomain())
      composeCoding(t, "MedicinalProduct", "domain", element.getDomain(), -1);
    if (element.hasCombinedPharmaceuticalDoseForm())
      composeCodeableConcept(t, "MedicinalProduct", "combinedPharmaceuticalDoseForm", element.getCombinedPharmaceuticalDoseForm(), -1);
    if (element.hasAdditionalMonitoringIndicator())
      composeCodeableConcept(t, "MedicinalProduct", "additionalMonitoringIndicator", element.getAdditionalMonitoringIndicator(), -1);
    for (int i = 0; i < element.getSpecialMeasures().size(); i++)
      composeString(t, "MedicinalProduct", "specialMeasures", element.getSpecialMeasures().get(i), i);
    if (element.hasPaediatricUseIndicator())
      composeCodeableConcept(t, "MedicinalProduct", "paediatricUseIndicator", element.getPaediatricUseIndicator(), -1);
    for (int i = 0; i < element.getProductClassification().size(); i++)
      composeCodeableConcept(t, "MedicinalProduct", "productClassification", element.getProductClassification().get(i), i);
    for (int i = 0; i < element.getMarketingStatus().size(); i++)
      composeMarketingStatus(t, "MedicinalProduct", "marketingStatus", element.getMarketingStatus().get(i), i);
    if (element.hasMarketingAuthorization())
      composeReference(t, "MedicinalProduct", "marketingAuthorization", element.getMarketingAuthorization(), -1);
    for (int i = 0; i < element.getPackagedMedicinalProduct().size(); i++)
      composeReference(t, "MedicinalProduct", "packagedMedicinalProduct", element.getPackagedMedicinalProduct().get(i), i);
    for (int i = 0; i < element.getPharmaceuticalProduct().size(); i++)
      composeReference(t, "MedicinalProduct", "pharmaceuticalProduct", element.getPharmaceuticalProduct().get(i), i);
    for (int i = 0; i < element.getContraindication().size(); i++)
      composeReference(t, "MedicinalProduct", "contraindication", element.getContraindication().get(i), i);
    for (int i = 0; i < element.getInteraction().size(); i++)
      composeReference(t, "MedicinalProduct", "interaction", element.getInteraction().get(i), i);
    for (int i = 0; i < element.getTherapeuticIndication().size(); i++)
      composeReference(t, "MedicinalProduct", "therapeuticIndication", element.getTherapeuticIndication().get(i), i);
    for (int i = 0; i < element.getUndesirableEffect().size(); i++)
      composeReference(t, "MedicinalProduct", "undesirableEffect", element.getUndesirableEffect().get(i), i);
    for (int i = 0; i < element.getAttachedDocument().size(); i++)
      composeReference(t, "MedicinalProduct", "attachedDocument", element.getAttachedDocument().get(i), i);
    for (int i = 0; i < element.getMasterFile().size(); i++)
      composeReference(t, "MedicinalProduct", "masterFile", element.getMasterFile().get(i), i);
    for (int i = 0; i < element.getContact().size(); i++)
      composeReference(t, "MedicinalProduct", "contact", element.getContact().get(i), i);
    for (int i = 0; i < element.getClinicalTrial().size(); i++)
      composeReference(t, "MedicinalProduct", "clinicalTrial", element.getClinicalTrial().get(i), i);
    for (int i = 0; i < element.getName().size(); i++)
      composeMedicinalProductMedicinalProductNameComponent(t, "MedicinalProduct", "name", element.getName().get(i), i);
    for (int i = 0; i < element.getCrossReference().size(); i++)
      composeIdentifier(t, "MedicinalProduct", "crossReference", element.getCrossReference().get(i), i);
    for (int i = 0; i < element.getManufacturingBusinessOperation().size(); i++)
      composeMedicinalProductMedicinalProductManufacturingBusinessOperationComponent(t, "MedicinalProduct", "manufacturingBusinessOperation", element.getManufacturingBusinessOperation().get(i), i);
    for (int i = 0; i < element.getSpecialDesignation().size(); i++)
      composeMedicinalProductMedicinalProductSpecialDesignationComponent(t, "MedicinalProduct", "specialDesignation", element.getSpecialDesignation().get(i), i);
  }

  protected void composeMedicinalProductMedicinalProductNameComponent(Complex parent, String parentType, String name, MedicinalProduct.MedicinalProductNameComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "name", name, element, index);
    if (element.hasProductNameElement())
      composeString(t, "MedicinalProduct", "productName", element.getProductNameElement(), -1);
    for (int i = 0; i < element.getNamePart().size(); i++)
      composeMedicinalProductMedicinalProductNameNamePartComponent(t, "MedicinalProduct", "namePart", element.getNamePart().get(i), i);
    for (int i = 0; i < element.getCountryLanguage().size(); i++)
      composeMedicinalProductMedicinalProductNameCountryLanguageComponent(t, "MedicinalProduct", "countryLanguage", element.getCountryLanguage().get(i), i);
  }

  protected void composeMedicinalProductMedicinalProductNameNamePartComponent(Complex parent, String parentType, String name, MedicinalProduct.MedicinalProductNameNamePartComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "namePart", name, element, index);
    if (element.hasPartElement())
      composeString(t, "MedicinalProduct", "part", element.getPartElement(), -1);
    if (element.hasType())
      composeCoding(t, "MedicinalProduct", "type", element.getType(), -1);
  }

  protected void composeMedicinalProductMedicinalProductNameCountryLanguageComponent(Complex parent, String parentType, String name, MedicinalProduct.MedicinalProductNameCountryLanguageComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "countryLanguage", name, element, index);
    if (element.hasCountry())
      composeCodeableConcept(t, "MedicinalProduct", "country", element.getCountry(), -1);
    if (element.hasJurisdiction())
      composeCodeableConcept(t, "MedicinalProduct", "jurisdiction", element.getJurisdiction(), -1);
    if (element.hasLanguage())
      composeCodeableConcept(t, "MedicinalProduct", "language", element.getLanguage(), -1);
  }

  protected void composeMedicinalProductMedicinalProductManufacturingBusinessOperationComponent(Complex parent, String parentType, String name, MedicinalProduct.MedicinalProductManufacturingBusinessOperationComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "manufacturingBusinessOperation", name, element, index);
    if (element.hasOperationType())
      composeCodeableConcept(t, "MedicinalProduct", "operationType", element.getOperationType(), -1);
    if (element.hasAuthorisationReferenceNumber())
      composeIdentifier(t, "MedicinalProduct", "authorisationReferenceNumber", element.getAuthorisationReferenceNumber(), -1);
    if (element.hasEffectiveDateElement())
      composeDateTime(t, "MedicinalProduct", "effectiveDate", element.getEffectiveDateElement(), -1);
    if (element.hasConfidentialityIndicator())
      composeCodeableConcept(t, "MedicinalProduct", "confidentialityIndicator", element.getConfidentialityIndicator(), -1);
    for (int i = 0; i < element.getManufacturer().size(); i++)
      composeReference(t, "MedicinalProduct", "manufacturer", element.getManufacturer().get(i), i);
    if (element.hasRegulator())
      composeReference(t, "MedicinalProduct", "regulator", element.getRegulator(), -1);
  }

  protected void composeMedicinalProductMedicinalProductSpecialDesignationComponent(Complex parent, String parentType, String name, MedicinalProduct.MedicinalProductSpecialDesignationComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "specialDesignation", name, element, index);
    for (int i = 0; i < element.getIdentifier().size(); i++)
      composeIdentifier(t, "MedicinalProduct", "identifier", element.getIdentifier().get(i), i);
    if (element.hasIntendedUse())
      composeCodeableConcept(t, "MedicinalProduct", "intendedUse", element.getIntendedUse(), -1);
    if (element.hasIndication())
      composeCodeableConcept(t, "MedicinalProduct", "indication", element.getIndication(), -1);
    if (element.hasStatus())
      composeCodeableConcept(t, "MedicinalProduct", "status", element.getStatus(), -1);
    if (element.hasDateElement())
      composeDateTime(t, "MedicinalProduct", "date", element.getDateElement(), -1);
    if (element.hasSpecies())
      composeCodeableConcept(t, "MedicinalProduct", "species", element.getSpecies(), -1);
  }

  protected void composeMedicinalProductAuthorization(Complex parent, String parentType, String name, MedicinalProductAuthorization element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "MedicinalProductAuthorization", name, element, index);
    for (int i = 0; i < element.getIdentifier().size(); i++)
      composeIdentifier(t, "MedicinalProductAuthorization", "identifier", element.getIdentifier().get(i), i);
    if (element.hasSubject())
      composeReference(t, "MedicinalProductAuthorization", "subject", element.getSubject(), -1);
    for (int i = 0; i < element.getCountry().size(); i++)
      composeCodeableConcept(t, "MedicinalProductAuthorization", "country", element.getCountry().get(i), i);
    for (int i = 0; i < element.getJurisdiction().size(); i++)
      composeCodeableConcept(t, "MedicinalProductAuthorization", "jurisdiction", element.getJurisdiction().get(i), i);
    if (element.hasLegalStatusOfSupply())
      composeCodeableConcept(t, "MedicinalProductAuthorization", "legalStatusOfSupply", element.getLegalStatusOfSupply(), -1);
    if (element.hasStatus())
      composeCodeableConcept(t, "MedicinalProductAuthorization", "status", element.getStatus(), -1);
    if (element.hasStatusDateElement())
      composeDateTime(t, "MedicinalProductAuthorization", "statusDate", element.getStatusDateElement(), -1);
    if (element.hasRestoreDateElement())
      composeDateTime(t, "MedicinalProductAuthorization", "restoreDate", element.getRestoreDateElement(), -1);
    if (element.hasValidityPeriod())
      composePeriod(t, "MedicinalProductAuthorization", "validityPeriod", element.getValidityPeriod(), -1);
    if (element.hasDataExclusivityPeriod())
      composePeriod(t, "MedicinalProductAuthorization", "dataExclusivityPeriod", element.getDataExclusivityPeriod(), -1);
    if (element.hasDateOfFirstAuthorizationElement())
      composeDateTime(t, "MedicinalProductAuthorization", "dateOfFirstAuthorization", element.getDateOfFirstAuthorizationElement(), -1);
    if (element.hasInternationalBirthDateElement())
      composeDateTime(t, "MedicinalProductAuthorization", "internationalBirthDate", element.getInternationalBirthDateElement(), -1);
    if (element.hasLegalBasis())
      composeCodeableConcept(t, "MedicinalProductAuthorization", "legalBasis", element.getLegalBasis(), -1);
    for (int i = 0; i < element.getJurisdictionalAuthorization().size(); i++)
      composeMedicinalProductAuthorizationMedicinalProductAuthorizationJurisdictionalAuthorizationComponent(t, "MedicinalProductAuthorization", "jurisdictionalAuthorization", element.getJurisdictionalAuthorization().get(i), i);
    if (element.hasHolder())
      composeReference(t, "MedicinalProductAuthorization", "holder", element.getHolder(), -1);
    if (element.hasRegulator())
      composeReference(t, "MedicinalProductAuthorization", "regulator", element.getRegulator(), -1);
    if (element.hasProcedure())
      composeMedicinalProductAuthorizationMedicinalProductAuthorizationProcedureComponent(t, "MedicinalProductAuthorization", "procedure", element.getProcedure(), -1);
  }

  protected void composeMedicinalProductAuthorizationMedicinalProductAuthorizationJurisdictionalAuthorizationComponent(Complex parent, String parentType, String name, MedicinalProductAuthorization.MedicinalProductAuthorizationJurisdictionalAuthorizationComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "jurisdictionalAuthorization", name, element, index);
    for (int i = 0; i < element.getIdentifier().size(); i++)
      composeIdentifier(t, "MedicinalProductAuthorization", "identifier", element.getIdentifier().get(i), i);
    if (element.hasCountry())
      composeCodeableConcept(t, "MedicinalProductAuthorization", "country", element.getCountry(), -1);
    for (int i = 0; i < element.getJurisdiction().size(); i++)
      composeCodeableConcept(t, "MedicinalProductAuthorization", "jurisdiction", element.getJurisdiction().get(i), i);
    if (element.hasLegalStatusOfSupply())
      composeCodeableConcept(t, "MedicinalProductAuthorization", "legalStatusOfSupply", element.getLegalStatusOfSupply(), -1);
    if (element.hasValidityPeriod())
      composePeriod(t, "MedicinalProductAuthorization", "validityPeriod", element.getValidityPeriod(), -1);
  }

  protected void composeMedicinalProductAuthorizationMedicinalProductAuthorizationProcedureComponent(Complex parent, String parentType, String name, MedicinalProductAuthorization.MedicinalProductAuthorizationProcedureComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "procedure", name, element, index);
    if (element.hasIdentifier())
      composeIdentifier(t, "MedicinalProductAuthorization", "identifier", element.getIdentifier(), -1);
    if (element.hasType())
      composeCodeableConcept(t, "MedicinalProductAuthorization", "type", element.getType(), -1);
    if (element.hasDate())
      composeType(t, "MedicinalProductAuthorization", "date", element.getDate(), -1);
    for (int i = 0; i < element.getApplication().size(); i++)
      composeMedicinalProductAuthorizationMedicinalProductAuthorizationProcedureComponent(t, "MedicinalProductAuthorization", "application", element.getApplication().get(i), i);
  }

  protected void composeMedicinalProductClinicals(Complex parent, String parentType, String name, MedicinalProductClinicals element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "MedicinalProductClinicals", name, element, index);
    for (int i = 0; i < element.getUndesirableEffects().size(); i++)
      composeMedicinalProductClinicalsMedicinalProductClinicalsUndesirableEffectsComponent(t, "MedicinalProductClinicals", "undesirableEffects", element.getUndesirableEffects().get(i), i);
    for (int i = 0; i < element.getTherapeuticIndication().size(); i++)
      composeMedicinalProductClinicalsMedicinalProductClinicalsTherapeuticIndicationComponent(t, "MedicinalProductClinicals", "therapeuticIndication", element.getTherapeuticIndication().get(i), i);
    for (int i = 0; i < element.getContraindication().size(); i++)
      composeMedicinalProductClinicalsMedicinalProductClinicalsContraindicationComponent(t, "MedicinalProductClinicals", "contraindication", element.getContraindication().get(i), i);
    for (int i = 0; i < element.getInteractions().size(); i++)
      composeMedicinalProductClinicalsMedicinalProductClinicalsInteractionsComponent(t, "MedicinalProductClinicals", "interactions", element.getInteractions().get(i), i);
  }

  protected void composeMedicinalProductClinicalsMedicinalProductClinicalsUndesirableEffectsComponent(Complex parent, String parentType, String name, MedicinalProductClinicals.MedicinalProductClinicalsUndesirableEffectsComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "undesirableEffects", name, element, index);
    if (element.hasSymptomConditionEffect())
      composeCodeableConcept(t, "MedicinalProductClinicals", "symptomConditionEffect", element.getSymptomConditionEffect(), -1);
    if (element.hasClassification())
      composeCodeableConcept(t, "MedicinalProductClinicals", "classification", element.getClassification(), -1);
    if (element.hasFrequencyOfOccurrence())
      composeCodeableConcept(t, "MedicinalProductClinicals", "frequencyOfOccurrence", element.getFrequencyOfOccurrence(), -1);
    for (int i = 0; i < element.getPopulation().size(); i++)
      composeMedicinalProductClinicalsMedicinalProductClinicalsUndesirableEffectsPopulationComponent(t, "MedicinalProductClinicals", "population", element.getPopulation().get(i), i);
  }

  protected void composeMedicinalProductClinicalsMedicinalProductClinicalsUndesirableEffectsPopulationComponent(Complex parent, String parentType, String name, MedicinalProductClinicals.MedicinalProductClinicalsUndesirableEffectsPopulationComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "population", name, element, index);
    if (element.hasAge())
      composeType(t, "MedicinalProductClinicals", "age", element.getAge(), -1);
    if (element.hasGender())
      composeCodeableConcept(t, "MedicinalProductClinicals", "gender", element.getGender(), -1);
    if (element.hasRace())
      composeCodeableConcept(t, "MedicinalProductClinicals", "race", element.getRace(), -1);
    if (element.hasPhysiologicalCondition())
      composeCodeableConcept(t, "MedicinalProductClinicals", "physiologicalCondition", element.getPhysiologicalCondition(), -1);
  }

  protected void composeMedicinalProductClinicalsMedicinalProductClinicalsTherapeuticIndicationComponent(Complex parent, String parentType, String name, MedicinalProductClinicals.MedicinalProductClinicalsTherapeuticIndicationComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "therapeuticIndication", name, element, index);
    if (element.hasDiseaseSymptomProcedure())
      composeCodeableConcept(t, "MedicinalProductClinicals", "diseaseSymptomProcedure", element.getDiseaseSymptomProcedure(), -1);
    if (element.hasDiseaseStatus())
      composeCodeableConcept(t, "MedicinalProductClinicals", "diseaseStatus", element.getDiseaseStatus(), -1);
    for (int i = 0; i < element.getComorbidity().size(); i++)
      composeCodeableConcept(t, "MedicinalProductClinicals", "comorbidity", element.getComorbidity().get(i), i);
    if (element.hasIntendedEffect())
      composeCodeableConcept(t, "MedicinalProductClinicals", "intendedEffect", element.getIntendedEffect(), -1);
    if (element.hasDuration())
      composeQuantity(t, "MedicinalProductClinicals", "duration", element.getDuration(), -1);
    for (int i = 0; i < element.getUndesirableEffects().size(); i++)
      composeMedicinalProductClinicalsMedicinalProductClinicalsUndesirableEffectsComponent(t, "MedicinalProductClinicals", "undesirableEffects", element.getUndesirableEffects().get(i), i);
    for (int i = 0; i < element.getOtherTherapy().size(); i++)
      composeMedicinalProductClinicalsMedicinalProductClinicalsTherapeuticIndicationOtherTherapyComponent(t, "MedicinalProductClinicals", "otherTherapy", element.getOtherTherapy().get(i), i);
    for (int i = 0; i < element.getPopulation().size(); i++)
      composeMedicinalProductClinicalsMedicinalProductClinicalsUndesirableEffectsPopulationComponent(t, "MedicinalProductClinicals", "population", element.getPopulation().get(i), i);
  }

  protected void composeMedicinalProductClinicalsMedicinalProductClinicalsTherapeuticIndicationOtherTherapyComponent(Complex parent, String parentType, String name, MedicinalProductClinicals.MedicinalProductClinicalsTherapeuticIndicationOtherTherapyComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "otherTherapy", name, element, index);
    if (element.hasTherapyRelationshipType())
      composeCodeableConcept(t, "MedicinalProductClinicals", "therapyRelationshipType", element.getTherapyRelationshipType(), -1);
    if (element.hasMedication())
      composeType(t, "MedicinalProductClinicals", "medication", element.getMedication(), -1);
  }

  protected void composeMedicinalProductClinicalsMedicinalProductClinicalsContraindicationComponent(Complex parent, String parentType, String name, MedicinalProductClinicals.MedicinalProductClinicalsContraindicationComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "contraindication", name, element, index);
    if (element.hasDisease())
      composeCodeableConcept(t, "MedicinalProductClinicals", "disease", element.getDisease(), -1);
    if (element.hasDiseaseStatus())
      composeCodeableConcept(t, "MedicinalProductClinicals", "diseaseStatus", element.getDiseaseStatus(), -1);
    for (int i = 0; i < element.getComorbidity().size(); i++)
      composeCodeableConcept(t, "MedicinalProductClinicals", "comorbidity", element.getComorbidity().get(i), i);
    for (int i = 0; i < element.getTherapeuticIndication().size(); i++)
      composeMedicinalProductClinicalsMedicinalProductClinicalsTherapeuticIndicationComponent(t, "MedicinalProductClinicals", "therapeuticIndication", element.getTherapeuticIndication().get(i), i);
    for (int i = 0; i < element.getOtherTherapy().size(); i++)
      composeMedicinalProductClinicalsMedicinalProductClinicalsTherapeuticIndicationOtherTherapyComponent(t, "MedicinalProductClinicals", "otherTherapy", element.getOtherTherapy().get(i), i);
    for (int i = 0; i < element.getPopulation().size(); i++)
      composeMedicinalProductClinicalsMedicinalProductClinicalsUndesirableEffectsPopulationComponent(t, "MedicinalProductClinicals", "population", element.getPopulation().get(i), i);
  }

  protected void composeMedicinalProductClinicalsMedicinalProductClinicalsInteractionsComponent(Complex parent, String parentType, String name, MedicinalProductClinicals.MedicinalProductClinicalsInteractionsComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "interactions", name, element, index);
    if (element.hasInteractionElement())
      composeString(t, "MedicinalProductClinicals", "interaction", element.getInteractionElement(), -1);
    for (int i = 0; i < element.getInteractant().size(); i++)
      composeCodeableConcept(t, "MedicinalProductClinicals", "interactant", element.getInteractant().get(i), i);
    if (element.hasType())
      composeCodeableConcept(t, "MedicinalProductClinicals", "type", element.getType(), -1);
    if (element.hasEffect())
      composeCodeableConcept(t, "MedicinalProductClinicals", "effect", element.getEffect(), -1);
    if (element.hasIncidence())
      composeCodeableConcept(t, "MedicinalProductClinicals", "incidence", element.getIncidence(), -1);
    if (element.hasManagement())
      composeCodeableConcept(t, "MedicinalProductClinicals", "management", element.getManagement(), -1);
  }

  protected void composeMedicinalProductContraindication(Complex parent, String parentType, String name, MedicinalProductContraindication element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "MedicinalProductContraindication", name, element, index);
    for (int i = 0; i < element.getSubject().size(); i++)
      composeReference(t, "MedicinalProductContraindication", "subject", element.getSubject().get(i), i);
    if (element.hasDisease())
      composeCodeableConcept(t, "MedicinalProductContraindication", "disease", element.getDisease(), -1);
    if (element.hasDiseaseStatus())
      composeCodeableConcept(t, "MedicinalProductContraindication", "diseaseStatus", element.getDiseaseStatus(), -1);
    for (int i = 0; i < element.getComorbidity().size(); i++)
      composeCodeableConcept(t, "MedicinalProductContraindication", "comorbidity", element.getComorbidity().get(i), i);
    for (int i = 0; i < element.getTherapeuticIndication().size(); i++)
      composeReference(t, "MedicinalProductContraindication", "therapeuticIndication", element.getTherapeuticIndication().get(i), i);
    for (int i = 0; i < element.getOtherTherapy().size(); i++)
      composeMedicinalProductContraindicationMedicinalProductContraindicationOtherTherapyComponent(t, "MedicinalProductContraindication", "otherTherapy", element.getOtherTherapy().get(i), i);
    for (int i = 0; i < element.getPopulation().size(); i++)
      composeMedicinalProductContraindicationMedicinalProductContraindicationPopulationComponent(t, "MedicinalProductContraindication", "population", element.getPopulation().get(i), i);
  }

  protected void composeMedicinalProductContraindicationMedicinalProductContraindicationOtherTherapyComponent(Complex parent, String parentType, String name, MedicinalProductContraindication.MedicinalProductContraindicationOtherTherapyComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "otherTherapy", name, element, index);
    if (element.hasTherapyRelationshipType())
      composeCodeableConcept(t, "MedicinalProductContraindication", "therapyRelationshipType", element.getTherapyRelationshipType(), -1);
    if (element.hasMedication())
      composeType(t, "MedicinalProductContraindication", "medication", element.getMedication(), -1);
  }

  protected void composeMedicinalProductContraindicationMedicinalProductContraindicationPopulationComponent(Complex parent, String parentType, String name, MedicinalProductContraindication.MedicinalProductContraindicationPopulationComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "population", name, element, index);
    if (element.hasAge())
      composeType(t, "MedicinalProductContraindication", "age", element.getAge(), -1);
    if (element.hasGender())
      composeCodeableConcept(t, "MedicinalProductContraindication", "gender", element.getGender(), -1);
    if (element.hasRace())
      composeCodeableConcept(t, "MedicinalProductContraindication", "race", element.getRace(), -1);
    if (element.hasPhysiologicalCondition())
      composeCodeableConcept(t, "MedicinalProductContraindication", "physiologicalCondition", element.getPhysiologicalCondition(), -1);
  }

  protected void composeMedicinalProductDeviceSpec(Complex parent, String parentType, String name, MedicinalProductDeviceSpec element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "MedicinalProductDeviceSpec", name, element, index);
    if (element.hasIdentifier())
      composeIdentifier(t, "MedicinalProductDeviceSpec", "identifier", element.getIdentifier(), -1);
    if (element.hasType())
      composeCodeableConcept(t, "MedicinalProductDeviceSpec", "type", element.getType(), -1);
    if (element.hasTradeNameElement())
      composeString(t, "MedicinalProductDeviceSpec", "tradeName", element.getTradeNameElement(), -1);
    if (element.hasQuantity())
      composeQuantity(t, "MedicinalProductDeviceSpec", "quantity", element.getQuantity(), -1);
    if (element.hasListingNumberElement())
      composeString(t, "MedicinalProductDeviceSpec", "listingNumber", element.getListingNumberElement(), -1);
    if (element.hasModelNumberElement())
      composeString(t, "MedicinalProductDeviceSpec", "modelNumber", element.getModelNumberElement(), -1);
    if (element.hasSterilityIndicator())
      composeCodeableConcept(t, "MedicinalProductDeviceSpec", "sterilityIndicator", element.getSterilityIndicator(), -1);
    if (element.hasSterilisationRequirement())
      composeCodeableConcept(t, "MedicinalProductDeviceSpec", "sterilisationRequirement", element.getSterilisationRequirement(), -1);
    if (element.hasUsage())
      composeCodeableConcept(t, "MedicinalProductDeviceSpec", "usage", element.getUsage(), -1);
    for (int i = 0; i < element.getNomenclature().size(); i++)
      composeCodeableConcept(t, "MedicinalProductDeviceSpec", "nomenclature", element.getNomenclature().get(i), i);
    for (int i = 0; i < element.getShelfLifeStorage().size(); i++)
      composeProductShelfLife(t, "MedicinalProductDeviceSpec", "shelfLifeStorage", element.getShelfLifeStorage().get(i), i);
    if (element.hasPhysicalCharacteristics())
      composeProdCharacteristic(t, "MedicinalProductDeviceSpec", "physicalCharacteristics", element.getPhysicalCharacteristics(), -1);
    for (int i = 0; i < element.getOtherCharacteristics().size(); i++)
      composeCodeableConcept(t, "MedicinalProductDeviceSpec", "otherCharacteristics", element.getOtherCharacteristics().get(i), i);
    for (int i = 0; i < element.getBatchIdentifier().size(); i++)
      composeIdentifier(t, "MedicinalProductDeviceSpec", "batchIdentifier", element.getBatchIdentifier().get(i), i);
    for (int i = 0; i < element.getManufacturer().size(); i++)
      composeReference(t, "MedicinalProductDeviceSpec", "manufacturer", element.getManufacturer().get(i), i);
    for (int i = 0; i < element.getMaterial().size(); i++)
      composeMedicinalProductDeviceSpecMedicinalProductDeviceSpecMaterialComponent(t, "MedicinalProductDeviceSpec", "material", element.getMaterial().get(i), i);
  }

  protected void composeMedicinalProductDeviceSpecMedicinalProductDeviceSpecMaterialComponent(Complex parent, String parentType, String name, MedicinalProductDeviceSpec.MedicinalProductDeviceSpecMaterialComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "material", name, element, index);
    if (element.hasSubstance())
      composeCodeableConcept(t, "MedicinalProductDeviceSpec", "substance", element.getSubstance(), -1);
    if (element.hasAlternateElement())
      composeBoolean(t, "MedicinalProductDeviceSpec", "alternate", element.getAlternateElement(), -1);
    if (element.hasAllergenicIndicatorElement())
      composeBoolean(t, "MedicinalProductDeviceSpec", "allergenicIndicator", element.getAllergenicIndicatorElement(), -1);
  }

  protected void composeMedicinalProductIndication(Complex parent, String parentType, String name, MedicinalProductIndication element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "MedicinalProductIndication", name, element, index);
    for (int i = 0; i < element.getSubject().size(); i++)
      composeReference(t, "MedicinalProductIndication", "subject", element.getSubject().get(i), i);
    if (element.hasDiseaseSymptomProcedure())
      composeCodeableConcept(t, "MedicinalProductIndication", "diseaseSymptomProcedure", element.getDiseaseSymptomProcedure(), -1);
    if (element.hasDiseaseStatus())
      composeCodeableConcept(t, "MedicinalProductIndication", "diseaseStatus", element.getDiseaseStatus(), -1);
    for (int i = 0; i < element.getComorbidity().size(); i++)
      composeCodeableConcept(t, "MedicinalProductIndication", "comorbidity", element.getComorbidity().get(i), i);
    if (element.hasIntendedEffect())
      composeCodeableConcept(t, "MedicinalProductIndication", "intendedEffect", element.getIntendedEffect(), -1);
    if (element.hasDuration())
      composeQuantity(t, "MedicinalProductIndication", "duration", element.getDuration(), -1);
    for (int i = 0; i < element.getOtherTherapy().size(); i++)
      composeMedicinalProductIndicationMedicinalProductIndicationOtherTherapyComponent(t, "MedicinalProductIndication", "otherTherapy", element.getOtherTherapy().get(i), i);
    for (int i = 0; i < element.getUndesirableEffect().size(); i++)
      composeReference(t, "MedicinalProductIndication", "undesirableEffect", element.getUndesirableEffect().get(i), i);
    for (int i = 0; i < element.getPopulation().size(); i++)
      composeMedicinalProductIndicationMedicinalProductIndicationPopulationComponent(t, "MedicinalProductIndication", "population", element.getPopulation().get(i), i);
  }

  protected void composeMedicinalProductIndicationMedicinalProductIndicationOtherTherapyComponent(Complex parent, String parentType, String name, MedicinalProductIndication.MedicinalProductIndicationOtherTherapyComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "otherTherapy", name, element, index);
    if (element.hasTherapyRelationshipType())
      composeCodeableConcept(t, "MedicinalProductIndication", "therapyRelationshipType", element.getTherapyRelationshipType(), -1);
    if (element.hasMedication())
      composeType(t, "MedicinalProductIndication", "medication", element.getMedication(), -1);
  }

  protected void composeMedicinalProductIndicationMedicinalProductIndicationPopulationComponent(Complex parent, String parentType, String name, MedicinalProductIndication.MedicinalProductIndicationPopulationComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "population", name, element, index);
    if (element.hasAge())
      composeType(t, "MedicinalProductIndication", "age", element.getAge(), -1);
    if (element.hasGender())
      composeCodeableConcept(t, "MedicinalProductIndication", "gender", element.getGender(), -1);
    if (element.hasRace())
      composeCodeableConcept(t, "MedicinalProductIndication", "race", element.getRace(), -1);
    if (element.hasPhysiologicalCondition())
      composeCodeableConcept(t, "MedicinalProductIndication", "physiologicalCondition", element.getPhysiologicalCondition(), -1);
  }

  protected void composeMedicinalProductIngredient(Complex parent, String parentType, String name, MedicinalProductIngredient element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "MedicinalProductIngredient", name, element, index);
    if (element.hasIdentifier())
      composeIdentifier(t, "MedicinalProductIngredient", "identifier", element.getIdentifier(), -1);
    if (element.hasRole())
      composeCodeableConcept(t, "MedicinalProductIngredient", "role", element.getRole(), -1);
    if (element.hasAllergenicIndicatorElement())
      composeBoolean(t, "MedicinalProductIngredient", "allergenicIndicator", element.getAllergenicIndicatorElement(), -1);
    for (int i = 0; i < element.getManufacturer().size(); i++)
      composeReference(t, "MedicinalProductIngredient", "manufacturer", element.getManufacturer().get(i), i);
    for (int i = 0; i < element.getSpecifiedSubstance().size(); i++)
      composeMedicinalProductIngredientMedicinalProductIngredientSpecifiedSubstanceComponent(t, "MedicinalProductIngredient", "specifiedSubstance", element.getSpecifiedSubstance().get(i), i);
    if (element.hasSubstance())
      composeMedicinalProductIngredientMedicinalProductIngredientSubstanceComponent(t, "MedicinalProductIngredient", "substance", element.getSubstance(), -1);
  }

  protected void composeMedicinalProductIngredientMedicinalProductIngredientSpecifiedSubstanceComponent(Complex parent, String parentType, String name, MedicinalProductIngredient.MedicinalProductIngredientSpecifiedSubstanceComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "specifiedSubstance", name, element, index);
    if (element.hasCode())
      composeCodeableConcept(t, "MedicinalProductIngredient", "code", element.getCode(), -1);
    if (element.hasGroup())
      composeCodeableConcept(t, "MedicinalProductIngredient", "group", element.getGroup(), -1);
    if (element.hasConfidentiality())
      composeCodeableConcept(t, "MedicinalProductIngredient", "confidentiality", element.getConfidentiality(), -1);
    for (int i = 0; i < element.getStrength().size(); i++)
      composeMedicinalProductIngredientMedicinalProductIngredientSpecifiedSubstanceStrengthComponent(t, "MedicinalProductIngredient", "strength", element.getStrength().get(i), i);
  }

  protected void composeMedicinalProductIngredientMedicinalProductIngredientSpecifiedSubstanceStrengthComponent(Complex parent, String parentType, String name, MedicinalProductIngredient.MedicinalProductIngredientSpecifiedSubstanceStrengthComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "strength", name, element, index);
    if (element.hasPresentation())
      composeRatio(t, "MedicinalProductIngredient", "presentation", element.getPresentation(), -1);
    if (element.hasPresentationLowLimit())
      composeRatio(t, "MedicinalProductIngredient", "presentationLowLimit", element.getPresentationLowLimit(), -1);
    if (element.hasConcentration())
      composeRatio(t, "MedicinalProductIngredient", "concentration", element.getConcentration(), -1);
    if (element.hasConcentrationLowLimit())
      composeRatio(t, "MedicinalProductIngredient", "concentrationLowLimit", element.getConcentrationLowLimit(), -1);
    if (element.hasMeasurementPointElement())
      composeString(t, "MedicinalProductIngredient", "measurementPoint", element.getMeasurementPointElement(), -1);
    for (int i = 0; i < element.getCountry().size(); i++)
      composeCodeableConcept(t, "MedicinalProductIngredient", "country", element.getCountry().get(i), i);
    for (int i = 0; i < element.getReferenceStrength().size(); i++)
      composeMedicinalProductIngredientMedicinalProductIngredientSpecifiedSubstanceStrengthReferenceStrengthComponent(t, "MedicinalProductIngredient", "referenceStrength", element.getReferenceStrength().get(i), i);
  }

  protected void composeMedicinalProductIngredientMedicinalProductIngredientSpecifiedSubstanceStrengthReferenceStrengthComponent(Complex parent, String parentType, String name, MedicinalProductIngredient.MedicinalProductIngredientSpecifiedSubstanceStrengthReferenceStrengthComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "referenceStrength", name, element, index);
    if (element.hasSubstance())
      composeCodeableConcept(t, "MedicinalProductIngredient", "substance", element.getSubstance(), -1);
    if (element.hasStrength())
      composeRatio(t, "MedicinalProductIngredient", "strength", element.getStrength(), -1);
    if (element.hasMeasurementPointElement())
      composeString(t, "MedicinalProductIngredient", "measurementPoint", element.getMeasurementPointElement(), -1);
    for (int i = 0; i < element.getCountry().size(); i++)
      composeCodeableConcept(t, "MedicinalProductIngredient", "country", element.getCountry().get(i), i);
  }

  protected void composeMedicinalProductIngredientMedicinalProductIngredientSubstanceComponent(Complex parent, String parentType, String name, MedicinalProductIngredient.MedicinalProductIngredientSubstanceComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "substance", name, element, index);
    if (element.hasCode())
      composeCodeableConcept(t, "MedicinalProductIngredient", "code", element.getCode(), -1);
    for (int i = 0; i < element.getStrength().size(); i++)
      composeMedicinalProductIngredientMedicinalProductIngredientSpecifiedSubstanceStrengthComponent(t, "MedicinalProductIngredient", "strength", element.getStrength().get(i), i);
  }

  protected void composeMedicinalProductInteraction(Complex parent, String parentType, String name, MedicinalProductInteraction element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "MedicinalProductInteraction", name, element, index);
    for (int i = 0; i < element.getSubject().size(); i++)
      composeReference(t, "MedicinalProductInteraction", "subject", element.getSubject().get(i), i);
    if (element.hasInteractionElement())
      composeString(t, "MedicinalProductInteraction", "interaction", element.getInteractionElement(), -1);
    for (int i = 0; i < element.getInteractant().size(); i++)
      composeCodeableConcept(t, "MedicinalProductInteraction", "interactant", element.getInteractant().get(i), i);
    if (element.hasType())
      composeCodeableConcept(t, "MedicinalProductInteraction", "type", element.getType(), -1);
    if (element.hasEffect())
      composeCodeableConcept(t, "MedicinalProductInteraction", "effect", element.getEffect(), -1);
    if (element.hasIncidence())
      composeCodeableConcept(t, "MedicinalProductInteraction", "incidence", element.getIncidence(), -1);
    if (element.hasManagement())
      composeCodeableConcept(t, "MedicinalProductInteraction", "management", element.getManagement(), -1);
  }

  protected void composeMedicinalProductManufactured(Complex parent, String parentType, String name, MedicinalProductManufactured element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "MedicinalProductManufactured", name, element, index);
    if (element.hasManufacturedDoseForm())
      composeCodeableConcept(t, "MedicinalProductManufactured", "manufacturedDoseForm", element.getManufacturedDoseForm(), -1);
    if (element.hasUnitOfPresentation())
      composeCodeableConcept(t, "MedicinalProductManufactured", "unitOfPresentation", element.getUnitOfPresentation(), -1);
    if (element.hasQuantity())
      composeQuantity(t, "MedicinalProductManufactured", "quantity", element.getQuantity(), -1);
    for (int i = 0; i < element.getManufacturer().size(); i++)
      composeReference(t, "MedicinalProductManufactured", "manufacturer", element.getManufacturer().get(i), i);
    for (int i = 0; i < element.getIngredient().size(); i++)
      composeReference(t, "MedicinalProductManufactured", "ingredient", element.getIngredient().get(i), i);
    if (element.hasPhysicalCharacteristics())
      composeProdCharacteristic(t, "MedicinalProductManufactured", "physicalCharacteristics", element.getPhysicalCharacteristics(), -1);
    for (int i = 0; i < element.getOtherCharacteristics().size(); i++)
      composeCodeableConcept(t, "MedicinalProductManufactured", "otherCharacteristics", element.getOtherCharacteristics().get(i), i);
  }

  protected void composeMedicinalProductPackaged(Complex parent, String parentType, String name, MedicinalProductPackaged element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "MedicinalProductPackaged", name, element, index);
    if (element.hasIdentifier())
      composeIdentifier(t, "MedicinalProductPackaged", "identifier", element.getIdentifier(), -1);
    if (element.hasDescriptionElement())
      composeString(t, "MedicinalProductPackaged", "description", element.getDescriptionElement(), -1);
    for (int i = 0; i < element.getMarketingStatus().size(); i++)
      composeMarketingStatus(t, "MedicinalProductPackaged", "marketingStatus", element.getMarketingStatus().get(i), i);
    if (element.hasMarketingAuthorization())
      composeReference(t, "MedicinalProductPackaged", "marketingAuthorization", element.getMarketingAuthorization(), -1);
    for (int i = 0; i < element.getManufacturer().size(); i++)
      composeReference(t, "MedicinalProductPackaged", "manufacturer", element.getManufacturer().get(i), i);
    for (int i = 0; i < element.getBatchIdentifier().size(); i++)
      composeMedicinalProductPackagedMedicinalProductPackagedBatchIdentifierComponent(t, "MedicinalProductPackaged", "batchIdentifier", element.getBatchIdentifier().get(i), i);
    for (int i = 0; i < element.getPackageItem().size(); i++)
      composeMedicinalProductPackagedMedicinalProductPackagedPackageItemComponent(t, "MedicinalProductPackaged", "packageItem", element.getPackageItem().get(i), i);
  }

  protected void composeMedicinalProductPackagedMedicinalProductPackagedBatchIdentifierComponent(Complex parent, String parentType, String name, MedicinalProductPackaged.MedicinalProductPackagedBatchIdentifierComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "batchIdentifier", name, element, index);
    if (element.hasOuterPackaging())
      composeIdentifier(t, "MedicinalProductPackaged", "outerPackaging", element.getOuterPackaging(), -1);
    if (element.hasImmediatePackaging())
      composeIdentifier(t, "MedicinalProductPackaged", "immediatePackaging", element.getImmediatePackaging(), -1);
  }

  protected void composeMedicinalProductPackagedMedicinalProductPackagedPackageItemComponent(Complex parent, String parentType, String name, MedicinalProductPackaged.MedicinalProductPackagedPackageItemComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "packageItem", name, element, index);
    for (int i = 0; i < element.getIdentifier().size(); i++)
      composeIdentifier(t, "MedicinalProductPackaged", "identifier", element.getIdentifier().get(i), i);
    if (element.hasType())
      composeCodeableConcept(t, "MedicinalProductPackaged", "type", element.getType(), -1);
    if (element.hasQuantity())
      composeQuantity(t, "MedicinalProductPackaged", "quantity", element.getQuantity(), -1);
    for (int i = 0; i < element.getMaterial().size(); i++)
      composeCodeableConcept(t, "MedicinalProductPackaged", "material", element.getMaterial().get(i), i);
    for (int i = 0; i < element.getAlternateMaterial().size(); i++)
      composeCodeableConcept(t, "MedicinalProductPackaged", "alternateMaterial", element.getAlternateMaterial().get(i), i);
    for (int i = 0; i < element.getDevice().size(); i++)
      composeReference(t, "MedicinalProductPackaged", "device", element.getDevice().get(i), i);
    for (int i = 0; i < element.getManufacturedItem().size(); i++)
      composeReference(t, "MedicinalProductPackaged", "manufacturedItem", element.getManufacturedItem().get(i), i);
    for (int i = 0; i < element.getPackageItem().size(); i++)
      composeMedicinalProductPackagedMedicinalProductPackagedPackageItemComponent(t, "MedicinalProductPackaged", "packageItem", element.getPackageItem().get(i), i);
    if (element.hasPhysicalCharacteristics())
      composeProdCharacteristic(t, "MedicinalProductPackaged", "physicalCharacteristics", element.getPhysicalCharacteristics(), -1);
    for (int i = 0; i < element.getOtherCharacteristics().size(); i++)
      composeCodeableConcept(t, "MedicinalProductPackaged", "otherCharacteristics", element.getOtherCharacteristics().get(i), i);
    for (int i = 0; i < element.getShelfLifeStorage().size(); i++)
      composeProductShelfLife(t, "MedicinalProductPackaged", "shelfLifeStorage", element.getShelfLifeStorage().get(i), i);
    for (int i = 0; i < element.getManufacturer().size(); i++)
      composeReference(t, "MedicinalProductPackaged", "manufacturer", element.getManufacturer().get(i), i);
  }

  protected void composeMedicinalProductPharmaceutical(Complex parent, String parentType, String name, MedicinalProductPharmaceutical element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "MedicinalProductPharmaceutical", name, element, index);
    for (int i = 0; i < element.getIdentifier().size(); i++)
      composeIdentifier(t, "MedicinalProductPharmaceutical", "identifier", element.getIdentifier().get(i), i);
    if (element.hasAdministrableDoseForm())
      composeCodeableConcept(t, "MedicinalProductPharmaceutical", "administrableDoseForm", element.getAdministrableDoseForm(), -1);
    if (element.hasUnitOfPresentation())
      composeCodeableConcept(t, "MedicinalProductPharmaceutical", "unitOfPresentation", element.getUnitOfPresentation(), -1);
    for (int i = 0; i < element.getIngredient().size(); i++)
      composeReference(t, "MedicinalProductPharmaceutical", "ingredient", element.getIngredient().get(i), i);
    for (int i = 0; i < element.getDevice().size(); i++)
      composeReference(t, "MedicinalProductPharmaceutical", "device", element.getDevice().get(i), i);
    for (int i = 0; i < element.getCharacteristics().size(); i++)
      composeMedicinalProductPharmaceuticalMedicinalProductPharmaceuticalCharacteristicsComponent(t, "MedicinalProductPharmaceutical", "characteristics", element.getCharacteristics().get(i), i);
    for (int i = 0; i < element.getRouteOfAdministration().size(); i++)
      composeMedicinalProductPharmaceuticalMedicinalProductPharmaceuticalRouteOfAdministrationComponent(t, "MedicinalProductPharmaceutical", "routeOfAdministration", element.getRouteOfAdministration().get(i), i);
  }

  protected void composeMedicinalProductPharmaceuticalMedicinalProductPharmaceuticalCharacteristicsComponent(Complex parent, String parentType, String name, MedicinalProductPharmaceutical.MedicinalProductPharmaceuticalCharacteristicsComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "characteristics", name, element, index);
    if (element.hasCode())
      composeCodeableConcept(t, "MedicinalProductPharmaceutical", "code", element.getCode(), -1);
    if (element.hasStatus())
      composeCodeableConcept(t, "MedicinalProductPharmaceutical", "status", element.getStatus(), -1);
  }

  protected void composeMedicinalProductPharmaceuticalMedicinalProductPharmaceuticalRouteOfAdministrationComponent(Complex parent, String parentType, String name, MedicinalProductPharmaceutical.MedicinalProductPharmaceuticalRouteOfAdministrationComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "routeOfAdministration", name, element, index);
    if (element.hasCode())
      composeCodeableConcept(t, "MedicinalProductPharmaceutical", "code", element.getCode(), -1);
    if (element.hasFirstDose())
      composeQuantity(t, "MedicinalProductPharmaceutical", "firstDose", element.getFirstDose(), -1);
    if (element.hasMaxSingleDose())
      composeQuantity(t, "MedicinalProductPharmaceutical", "maxSingleDose", element.getMaxSingleDose(), -1);
    if (element.hasMaxDosePerDay())
      composeQuantity(t, "MedicinalProductPharmaceutical", "maxDosePerDay", element.getMaxDosePerDay(), -1);
    if (element.hasMaxDosePerTreatmentPeriod())
      composeRatio(t, "MedicinalProductPharmaceutical", "maxDosePerTreatmentPeriod", element.getMaxDosePerTreatmentPeriod(), -1);
    if (element.hasMaxTreatmentPeriod())
      composeDuration(t, "MedicinalProductPharmaceutical", "maxTreatmentPeriod", element.getMaxTreatmentPeriod(), -1);
    for (int i = 0; i < element.getTargetSpecies().size(); i++)
      composeMedicinalProductPharmaceuticalMedicinalProductPharmaceuticalRouteOfAdministrationTargetSpeciesComponent(t, "MedicinalProductPharmaceutical", "targetSpecies", element.getTargetSpecies().get(i), i);
  }

  protected void composeMedicinalProductPharmaceuticalMedicinalProductPharmaceuticalRouteOfAdministrationTargetSpeciesComponent(Complex parent, String parentType, String name, MedicinalProductPharmaceutical.MedicinalProductPharmaceuticalRouteOfAdministrationTargetSpeciesComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "targetSpecies", name, element, index);
    if (element.hasCode())
      composeCodeableConcept(t, "MedicinalProductPharmaceutical", "code", element.getCode(), -1);
    for (int i = 0; i < element.getWithdrawalPeriod().size(); i++)
      composeMedicinalProductPharmaceuticalMedicinalProductPharmaceuticalRouteOfAdministrationTargetSpeciesWithdrawalPeriodComponent(t, "MedicinalProductPharmaceutical", "withdrawalPeriod", element.getWithdrawalPeriod().get(i), i);
  }

  protected void composeMedicinalProductPharmaceuticalMedicinalProductPharmaceuticalRouteOfAdministrationTargetSpeciesWithdrawalPeriodComponent(Complex parent, String parentType, String name, MedicinalProductPharmaceutical.MedicinalProductPharmaceuticalRouteOfAdministrationTargetSpeciesWithdrawalPeriodComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "withdrawalPeriod", name, element, index);
    if (element.hasTissue())
      composeCodeableConcept(t, "MedicinalProductPharmaceutical", "tissue", element.getTissue(), -1);
    if (element.hasValue())
      composeQuantity(t, "MedicinalProductPharmaceutical", "value", element.getValue(), -1);
    if (element.hasSupportingInformationElement())
      composeString(t, "MedicinalProductPharmaceutical", "supportingInformation", element.getSupportingInformationElement(), -1);
  }

  protected void composeMedicinalProductUndesirableEffect(Complex parent, String parentType, String name, MedicinalProductUndesirableEffect element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "MedicinalProductUndesirableEffect", name, element, index);
    for (int i = 0; i < element.getSubject().size(); i++)
      composeReference(t, "MedicinalProductUndesirableEffect", "subject", element.getSubject().get(i), i);
    if (element.hasSymptomConditionEffect())
      composeCodeableConcept(t, "MedicinalProductUndesirableEffect", "symptomConditionEffect", element.getSymptomConditionEffect(), -1);
    if (element.hasClassification())
      composeCodeableConcept(t, "MedicinalProductUndesirableEffect", "classification", element.getClassification(), -1);
    if (element.hasFrequencyOfOccurrence())
      composeCodeableConcept(t, "MedicinalProductUndesirableEffect", "frequencyOfOccurrence", element.getFrequencyOfOccurrence(), -1);
    for (int i = 0; i < element.getPopulation().size(); i++)
      composeMedicinalProductUndesirableEffectMedicinalProductUndesirableEffectPopulationComponent(t, "MedicinalProductUndesirableEffect", "population", element.getPopulation().get(i), i);
  }

  protected void composeMedicinalProductUndesirableEffectMedicinalProductUndesirableEffectPopulationComponent(Complex parent, String parentType, String name, MedicinalProductUndesirableEffect.MedicinalProductUndesirableEffectPopulationComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "population", name, element, index);
    if (element.hasAge())
      composeType(t, "MedicinalProductUndesirableEffect", "age", element.getAge(), -1);
    if (element.hasGender())
      composeCodeableConcept(t, "MedicinalProductUndesirableEffect", "gender", element.getGender(), -1);
    if (element.hasRace())
      composeCodeableConcept(t, "MedicinalProductUndesirableEffect", "race", element.getRace(), -1);
    if (element.hasPhysiologicalCondition())
      composeCodeableConcept(t, "MedicinalProductUndesirableEffect", "physiologicalCondition", element.getPhysiologicalCondition(), -1);
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
    for (int i = 0; i < element.getIdentifier().size(); i++)
      composeIdentifier(t, "MessageDefinition", "identifier", element.getIdentifier().get(i), i);
    if (element.hasVersionElement())
      composeString(t, "MessageDefinition", "version", element.getVersionElement(), -1);
    if (element.hasNameElement())
      composeString(t, "MessageDefinition", "name", element.getNameElement(), -1);
    if (element.hasTitleElement())
      composeString(t, "MessageDefinition", "title", element.getTitleElement(), -1);
    for (int i = 0; i < element.getReplaces().size(); i++)
      composeCanonical(t, "MessageDefinition", "replaces", element.getReplaces().get(i), i);
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
    if (element.hasBaseElement())
      composeCanonical(t, "MessageDefinition", "base", element.getBaseElement(), -1);
    for (int i = 0; i < element.getParent().size(); i++)
      composeCanonical(t, "MessageDefinition", "parent", element.getParent().get(i), i);
    if (element.hasEvent())
      composeType(t, "MessageDefinition", "event", element.getEvent(), -1);
    if (element.hasCategoryElement())
      composeEnum(t, "MessageDefinition", "category", element.getCategoryElement(), -1);
    for (int i = 0; i < element.getFocus().size(); i++)
      composeMessageDefinitionMessageDefinitionFocusComponent(t, "MessageDefinition", "focus", element.getFocus().get(i), i);
    if (element.hasResponseRequiredElement())
      composeEnum(t, "MessageDefinition", "responseRequired", element.getResponseRequiredElement(), -1);
    for (int i = 0; i < element.getAllowedResponse().size(); i++)
      composeMessageDefinitionMessageDefinitionAllowedResponseComponent(t, "MessageDefinition", "allowedResponse", element.getAllowedResponse().get(i), i);
    for (int i = 0; i < element.getGraph().size(); i++)
      composeCanonical(t, "MessageDefinition", "graph", element.getGraph().get(i), i);
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
    if (element.hasProfileElement())
      composeCanonical(t, "MessageDefinition", "profile", element.getProfileElement(), -1);
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
    if (element.hasMessageElement())
      composeCanonical(t, "MessageDefinition", "message", element.getMessageElement(), -1);
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
      composeType(t, "MessageHeader", "event", element.getEvent(), -1);
    for (int i = 0; i < element.getDestination().size(); i++)
      composeMessageHeaderMessageDestinationComponent(t, "MessageHeader", "destination", element.getDestination().get(i), i);
    if (element.hasSender())
      composeReference(t, "MessageHeader", "sender", element.getSender(), -1);
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
      composeCanonical(t, "MessageHeader", "definition", element.getDefinitionElement(), -1);
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
      composeUrl(t, "MessageHeader", "endpoint", element.getEndpointElement(), -1);
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
      composeUrl(t, "MessageHeader", "endpoint", element.getEndpointElement(), -1);
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
    for (int i = 0; i < element.getInstantiatesCanonical().size(); i++)
      composeCanonical(t, "NutritionOrder", "instantiatesCanonical", element.getInstantiatesCanonical().get(i), i);
    for (int i = 0; i < element.getInstantiatesUri().size(); i++)
      composeUri(t, "NutritionOrder", "instantiatesUri", element.getInstantiatesUri().get(i), i);
    for (int i = 0; i < element.getInstantiates().size(); i++)
      composeUri(t, "NutritionOrder", "instantiates", element.getInstantiates().get(i), i);
    if (element.hasStatusElement())
      composeEnum(t, "NutritionOrder", "status", element.getStatusElement(), -1);
    if (element.hasIntentElement())
      composeEnum(t, "NutritionOrder", "intent", element.getIntentElement(), -1);
    if (element.hasPatient())
      composeReference(t, "NutritionOrder", "patient", element.getPatient(), -1);
    if (element.hasContext())
      composeReference(t, "NutritionOrder", "context", element.getContext(), -1);
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
    for (int i = 0; i < element.getNote().size(); i++)
      composeAnnotation(t, "NutritionOrder", "note", element.getNote().get(i), i);
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
    for (int i = 0; i < element.getPartOf().size(); i++)
      composeReference(t, "Observation", "partOf", element.getPartOf().get(i), i);
    if (element.hasStatusElement())
      composeEnum(t, "Observation", "status", element.getStatusElement(), -1);
    for (int i = 0; i < element.getCategory().size(); i++)
      composeCodeableConcept(t, "Observation", "category", element.getCategory().get(i), i);
    if (element.hasCode())
      composeCodeableConcept(t, "Observation", "code", element.getCode(), -1);
    if (element.hasSubject())
      composeReference(t, "Observation", "subject", element.getSubject(), -1);
    for (int i = 0; i < element.getFocus().size(); i++)
      composeReference(t, "Observation", "focus", element.getFocus().get(i), i);
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
    for (int i = 0; i < element.getInterpretation().size(); i++)
      composeCodeableConcept(t, "Observation", "interpretation", element.getInterpretation().get(i), i);
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
    for (int i = 0; i < element.getHasMember().size(); i++)
      composeReference(t, "Observation", "hasMember", element.getHasMember().get(i), i);
    for (int i = 0; i < element.getDerivedFrom().size(); i++)
      composeReference(t, "Observation", "derivedFrom", element.getDerivedFrom().get(i), i);
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
    for (int i = 0; i < element.getInterpretation().size(); i++)
      composeCodeableConcept(t, "Observation", "interpretation", element.getInterpretation().get(i), i);
    for (int i = 0; i < element.getReferenceRange().size(); i++)
      composeObservationObservationReferenceRangeComponent(t, "Observation", "referenceRange", element.getReferenceRange().get(i), i);
  }

  protected void composeObservationDefinition(Complex parent, String parentType, String name, ObservationDefinition element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "ObservationDefinition", name, element, index);
    if (element.hasCategory())
      composeCodeableConcept(t, "ObservationDefinition", "category", element.getCategory(), -1);
    if (element.hasCode())
      composeCodeableConcept(t, "ObservationDefinition", "code", element.getCode(), -1);
    for (int i = 0; i < element.getIdentifier().size(); i++)
      composeIdentifier(t, "ObservationDefinition", "identifier", element.getIdentifier().get(i), i);
    for (int i = 0; i < element.getPermittedDataType().size(); i++)
      composeCoding(t, "ObservationDefinition", "permittedDataType", element.getPermittedDataType().get(i), i);
    if (element.hasMultipleResultsAllowedElement())
      composeBoolean(t, "ObservationDefinition", "multipleResultsAllowed", element.getMultipleResultsAllowedElement(), -1);
    if (element.hasMethod())
      composeCodeableConcept(t, "ObservationDefinition", "method", element.getMethod(), -1);
    if (element.hasPreferredReportNameElement())
      composeString(t, "ObservationDefinition", "preferredReportName", element.getPreferredReportNameElement(), -1);
    if (element.hasQuantitativeDetails())
      composeObservationDefinitionObservationDefinitionQuantitativeDetailsComponent(t, "ObservationDefinition", "quantitativeDetails", element.getQuantitativeDetails(), -1);
    for (int i = 0; i < element.getQualifiedInterval().size(); i++)
      composeObservationDefinitionObservationDefinitionQualifiedIntervalComponent(t, "ObservationDefinition", "qualifiedInterval", element.getQualifiedInterval().get(i), i);
    if (element.hasValidCodedValueSetElement())
      composeUri(t, "ObservationDefinition", "validCodedValueSet", element.getValidCodedValueSetElement(), -1);
    if (element.hasNormalCodedValueSetElement())
      composeUri(t, "ObservationDefinition", "normalCodedValueSet", element.getNormalCodedValueSetElement(), -1);
    if (element.hasAbnormalCodedValueSetElement())
      composeUri(t, "ObservationDefinition", "abnormalCodedValueSet", element.getAbnormalCodedValueSetElement(), -1);
    if (element.hasCriticalCodedValueSetElement())
      composeUri(t, "ObservationDefinition", "criticalCodedValueSet", element.getCriticalCodedValueSetElement(), -1);
  }

  protected void composeObservationDefinitionObservationDefinitionQuantitativeDetailsComponent(Complex parent, String parentType, String name, ObservationDefinition.ObservationDefinitionQuantitativeDetailsComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "quantitativeDetails", name, element, index);
    if (element.hasCustomaryUnit())
      composeCoding(t, "ObservationDefinition", "customaryUnit", element.getCustomaryUnit(), -1);
    if (element.hasUnit())
      composeCoding(t, "ObservationDefinition", "unit", element.getUnit(), -1);
    if (element.hasConversionFactorElement())
      composeDecimal(t, "ObservationDefinition", "conversionFactor", element.getConversionFactorElement(), -1);
    if (element.hasDecimalPrecisionElement())
      composeInteger(t, "ObservationDefinition", "decimalPrecision", element.getDecimalPrecisionElement(), -1);
  }

  protected void composeObservationDefinitionObservationDefinitionQualifiedIntervalComponent(Complex parent, String parentType, String name, ObservationDefinition.ObservationDefinitionQualifiedIntervalComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "qualifiedInterval", name, element, index);
    if (element.hasCategory())
      composeCodeableConcept(t, "ObservationDefinition", "category", element.getCategory(), -1);
    if (element.hasRange())
      composeRange(t, "ObservationDefinition", "range", element.getRange(), -1);
    if (element.hasType())
      composeCodeableConcept(t, "ObservationDefinition", "type", element.getType(), -1);
    for (int i = 0; i < element.getAppliesTo().size(); i++)
      composeCodeableConcept(t, "ObservationDefinition", "appliesTo", element.getAppliesTo().get(i), i);
    if (element.hasAge())
      composeRange(t, "ObservationDefinition", "age", element.getAge(), -1);
    if (element.hasGestationalAge())
      composeRange(t, "ObservationDefinition", "gestationalAge", element.getGestationalAge(), -1);
    if (element.hasConditionElement())
      composeString(t, "ObservationDefinition", "condition", element.getConditionElement(), -1);
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
    if (element.hasTitleElement())
      composeString(t, "OperationDefinition", "title", element.getTitleElement(), -1);
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
    if (element.hasAffectsStateElement())
      composeBoolean(t, "OperationDefinition", "affectsState", element.getAffectsStateElement(), -1);
    if (element.hasCodeElement())
      composeCode(t, "OperationDefinition", "code", element.getCodeElement(), -1);
    if (element.hasCommentElement())
      composeMarkdown(t, "OperationDefinition", "comment", element.getCommentElement(), -1);
    if (element.hasBaseElement())
      composeCanonical(t, "OperationDefinition", "base", element.getBaseElement(), -1);
    for (int i = 0; i < element.getResource().size(); i++)
      composeCode(t, "OperationDefinition", "resource", element.getResource().get(i), i);
    if (element.hasSystemElement())
      composeBoolean(t, "OperationDefinition", "system", element.getSystemElement(), -1);
    if (element.hasTypeElement())
      composeBoolean(t, "OperationDefinition", "type", element.getTypeElement(), -1);
    if (element.hasInstanceElement())
      composeBoolean(t, "OperationDefinition", "instance", element.getInstanceElement(), -1);
    if (element.hasInputProfileElement())
      composeCanonical(t, "OperationDefinition", "inputProfile", element.getInputProfileElement(), -1);
    if (element.hasOutputProfileElement())
      composeCanonical(t, "OperationDefinition", "outputProfile", element.getOutputProfileElement(), -1);
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
    for (int i = 0; i < element.getTargetProfile().size(); i++)
      composeCanonical(t, "OperationDefinition", "targetProfile", element.getTargetProfile().get(i), i);
    if (element.hasSearchTypeElement())
      composeEnum(t, "OperationDefinition", "searchType", element.getSearchTypeElement(), -1);
    if (element.hasBinding())
      composeOperationDefinitionOperationDefinitionParameterBindingComponent(t, "OperationDefinition", "binding", element.getBinding(), -1);
    for (int i = 0; i < element.getReferencedFrom().size(); i++)
      composeOperationDefinitionOperationDefinitionParameterReferencedFromComponent(t, "OperationDefinition", "referencedFrom", element.getReferencedFrom().get(i), i);
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
    if (element.hasValueSetElement())
      composeCanonical(t, "OperationDefinition", "valueSet", element.getValueSetElement(), -1);
  }

  protected void composeOperationDefinitionOperationDefinitionParameterReferencedFromComponent(Complex parent, String parentType, String name, OperationDefinition.OperationDefinitionParameterReferencedFromComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "referencedFrom", name, element, index);
    if (element.hasSourceElement())
      composeString(t, "OperationDefinition", "source", element.getSourceElement(), -1);
    if (element.hasSourceIdElement())
      composeString(t, "OperationDefinition", "sourceId", element.getSourceIdElement(), -1);
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

  protected void composeOrganizationAffiliation(Complex parent, String parentType, String name, OrganizationAffiliation element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "OrganizationAffiliation", name, element, index);
    for (int i = 0; i < element.getIdentifier().size(); i++)
      composeIdentifier(t, "OrganizationAffiliation", "identifier", element.getIdentifier().get(i), i);
    if (element.hasActiveElement())
      composeBoolean(t, "OrganizationAffiliation", "active", element.getActiveElement(), -1);
    if (element.hasPeriod())
      composePeriod(t, "OrganizationAffiliation", "period", element.getPeriod(), -1);
    if (element.hasOrganization())
      composeReference(t, "OrganizationAffiliation", "organization", element.getOrganization(), -1);
    if (element.hasParticipatingOrganization())
      composeReference(t, "OrganizationAffiliation", "participatingOrganization", element.getParticipatingOrganization(), -1);
    for (int i = 0; i < element.getNetwork().size(); i++)
      composeReference(t, "OrganizationAffiliation", "network", element.getNetwork().get(i), i);
    for (int i = 0; i < element.getCode().size(); i++)
      composeCodeableConcept(t, "OrganizationAffiliation", "code", element.getCode().get(i), i);
    for (int i = 0; i < element.getSpecialty().size(); i++)
      composeCodeableConcept(t, "OrganizationAffiliation", "specialty", element.getSpecialty().get(i), i);
    for (int i = 0; i < element.getLocation().size(); i++)
      composeReference(t, "OrganizationAffiliation", "location", element.getLocation().get(i), i);
    for (int i = 0; i < element.getHealthcareService().size(); i++)
      composeReference(t, "OrganizationAffiliation", "healthcareService", element.getHealthcareService().get(i), i);
    for (int i = 0; i < element.getTelecom().size(); i++)
      composeContactPoint(t, "OrganizationAffiliation", "telecom", element.getTelecom().get(i), i);
    for (int i = 0; i < element.getEndpoint().size(); i++)
      composeReference(t, "OrganizationAffiliation", "endpoint", element.getEndpoint().get(i), i);
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
    if (element.hasSubtitleElement())
      composeString(t, "PlanDefinition", "subtitle", element.getSubtitleElement(), -1);
    if (element.hasType())
      composeCodeableConcept(t, "PlanDefinition", "type", element.getType(), -1);
    if (element.hasStatusElement())
      composeEnum(t, "PlanDefinition", "status", element.getStatusElement(), -1);
    if (element.hasExperimentalElement())
      composeBoolean(t, "PlanDefinition", "experimental", element.getExperimentalElement(), -1);
    if (element.hasSubject())
      composeType(t, "PlanDefinition", "subject", element.getSubject(), -1);
    if (element.hasDateElement())
      composeDateTime(t, "PlanDefinition", "date", element.getDateElement(), -1);
    if (element.hasPublisherElement())
      composeString(t, "PlanDefinition", "publisher", element.getPublisherElement(), -1);
    for (int i = 0; i < element.getContact().size(); i++)
      composeContactDetail(t, "PlanDefinition", "contact", element.getContact().get(i), i);
    if (element.hasDescriptionElement())
      composeMarkdown(t, "PlanDefinition", "description", element.getDescriptionElement(), -1);
    for (int i = 0; i < element.getUseContext().size(); i++)
      composeUsageContext(t, "PlanDefinition", "useContext", element.getUseContext().get(i), i);
    for (int i = 0; i < element.getJurisdiction().size(); i++)
      composeCodeableConcept(t, "PlanDefinition", "jurisdiction", element.getJurisdiction().get(i), i);
    if (element.hasPurposeElement())
      composeMarkdown(t, "PlanDefinition", "purpose", element.getPurposeElement(), -1);
    if (element.hasUsageElement())
      composeString(t, "PlanDefinition", "usage", element.getUsageElement(), -1);
    if (element.hasCopyrightElement())
      composeMarkdown(t, "PlanDefinition", "copyright", element.getCopyrightElement(), -1);
    if (element.hasApprovalDateElement())
      composeDate(t, "PlanDefinition", "approvalDate", element.getApprovalDateElement(), -1);
    if (element.hasLastReviewDateElement())
      composeDate(t, "PlanDefinition", "lastReviewDate", element.getLastReviewDateElement(), -1);
    if (element.hasEffectivePeriod())
      composePeriod(t, "PlanDefinition", "effectivePeriod", element.getEffectivePeriod(), -1);
    for (int i = 0; i < element.getTopic().size(); i++)
      composeCodeableConcept(t, "PlanDefinition", "topic", element.getTopic().get(i), i);
    for (int i = 0; i < element.getAuthor().size(); i++)
      composeContactDetail(t, "PlanDefinition", "author", element.getAuthor().get(i), i);
    for (int i = 0; i < element.getEditor().size(); i++)
      composeContactDetail(t, "PlanDefinition", "editor", element.getEditor().get(i), i);
    for (int i = 0; i < element.getReviewer().size(); i++)
      composeContactDetail(t, "PlanDefinition", "reviewer", element.getReviewer().get(i), i);
    for (int i = 0; i < element.getEndorser().size(); i++)
      composeContactDetail(t, "PlanDefinition", "endorser", element.getEndorser().get(i), i);
    for (int i = 0; i < element.getRelatedArtifact().size(); i++)
      composeRelatedArtifact(t, "PlanDefinition", "relatedArtifact", element.getRelatedArtifact().get(i), i);
    for (int i = 0; i < element.getLibrary().size(); i++)
      composeCanonical(t, "PlanDefinition", "library", element.getLibrary().get(i), i);
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
    if (element.hasPriorityElement())
      composeEnum(t, "PlanDefinition", "priority", element.getPriorityElement(), -1);
    for (int i = 0; i < element.getCode().size(); i++)
      composeCodeableConcept(t, "PlanDefinition", "code", element.getCode().get(i), i);
    for (int i = 0; i < element.getReason().size(); i++)
      composeCodeableConcept(t, "PlanDefinition", "reason", element.getReason().get(i), i);
    for (int i = 0; i < element.getDocumentation().size(); i++)
      composeRelatedArtifact(t, "PlanDefinition", "documentation", element.getDocumentation().get(i), i);
    for (int i = 0; i < element.getGoalId().size(); i++)
      composeId(t, "PlanDefinition", "goalId", element.getGoalId().get(i), i);
    for (int i = 0; i < element.getTrigger().size(); i++)
      composeTriggerDefinition(t, "PlanDefinition", "trigger", element.getTrigger().get(i), i);
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
      composeCodeableConcept(t, "PlanDefinition", "type", element.getType(), -1);
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
    if (element.hasDefinitionElement())
      composeCanonical(t, "PlanDefinition", "definition", element.getDefinitionElement(), -1);
    if (element.hasTransformElement())
      composeCanonical(t, "PlanDefinition", "transform", element.getTransformElement(), -1);
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
    if (element.hasExpression())
      composeExpression(t, "PlanDefinition", "expression", element.getExpression(), -1);
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
    if (element.hasPathElement())
      composeString(t, "PlanDefinition", "path", element.getPathElement(), -1);
    if (element.hasExpression())
      composeExpression(t, "PlanDefinition", "expression", element.getExpression(), -1);
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
    for (int i = 0; i < element.getInstantiatesCanonical().size(); i++)
      composeCanonical(t, "Procedure", "instantiatesCanonical", element.getInstantiatesCanonical().get(i), i);
    for (int i = 0; i < element.getInstantiatesUri().size(); i++)
      composeUri(t, "Procedure", "instantiatesUri", element.getInstantiatesUri().get(i), i);
    for (int i = 0; i < element.getBasedOn().size(); i++)
      composeReference(t, "Procedure", "basedOn", element.getBasedOn().get(i), i);
    for (int i = 0; i < element.getPartOf().size(); i++)
      composeReference(t, "Procedure", "partOf", element.getPartOf().get(i), i);
    if (element.hasStatusElement())
      composeEnum(t, "Procedure", "status", element.getStatusElement(), -1);
    if (element.hasStatusReason())
      composeCodeableConcept(t, "Procedure", "statusReason", element.getStatusReason(), -1);
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
    if (element.hasRecorder())
      composeReference(t, "Procedure", "recorder", element.getRecorder(), -1);
    if (element.hasAsserter())
      composeReference(t, "Procedure", "asserter", element.getAsserter(), -1);
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
    if (element.hasFunction())
      composeCodeableConcept(t, "Procedure", "function", element.getFunction(), -1);
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
    if (element.hasOccurred())
      composeType(t, "Provenance", "occurred", element.getOccurred(), -1);
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
      composeReference(t, "Provenance", "who", element.getWho(), -1);
    if (element.hasOnBehalfOf())
      composeReference(t, "Provenance", "onBehalfOf", element.getOnBehalfOf(), -1);
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
      composeReference(t, "Provenance", "what", element.getWhat(), -1);
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
    for (int i = 0; i < element.getDerivedFrom().size(); i++)
      composeCanonical(t, "Questionnaire", "derivedFrom", element.getDerivedFrom().get(i), i);
    if (element.hasStatusElement())
      composeEnum(t, "Questionnaire", "status", element.getStatusElement(), -1);
    if (element.hasExperimentalElement())
      composeBoolean(t, "Questionnaire", "experimental", element.getExperimentalElement(), -1);
    for (int i = 0; i < element.getSubjectType().size(); i++)
      composeCode(t, "Questionnaire", "subjectType", element.getSubjectType().get(i), i);
    if (element.hasDateElement())
      composeDateTime(t, "Questionnaire", "date", element.getDateElement(), -1);
    if (element.hasPublisherElement())
      composeString(t, "Questionnaire", "publisher", element.getPublisherElement(), -1);
    for (int i = 0; i < element.getContact().size(); i++)
      composeContactDetail(t, "Questionnaire", "contact", element.getContact().get(i), i);
    if (element.hasDescriptionElement())
      composeMarkdown(t, "Questionnaire", "description", element.getDescriptionElement(), -1);
    for (int i = 0; i < element.getUseContext().size(); i++)
      composeUsageContext(t, "Questionnaire", "useContext", element.getUseContext().get(i), i);
    for (int i = 0; i < element.getJurisdiction().size(); i++)
      composeCodeableConcept(t, "Questionnaire", "jurisdiction", element.getJurisdiction().get(i), i);
    if (element.hasPurposeElement())
      composeMarkdown(t, "Questionnaire", "purpose", element.getPurposeElement(), -1);
    if (element.hasCopyrightElement())
      composeMarkdown(t, "Questionnaire", "copyright", element.getCopyrightElement(), -1);
    if (element.hasApprovalDateElement())
      composeDate(t, "Questionnaire", "approvalDate", element.getApprovalDateElement(), -1);
    if (element.hasLastReviewDateElement())
      composeDate(t, "Questionnaire", "lastReviewDate", element.getLastReviewDateElement(), -1);
    if (element.hasEffectivePeriod())
      composePeriod(t, "Questionnaire", "effectivePeriod", element.getEffectivePeriod(), -1);
    for (int i = 0; i < element.getCode().size(); i++)
      composeCoding(t, "Questionnaire", "code", element.getCode().get(i), i);
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
    if (element.hasEnableBehaviorElement())
      composeEnum(t, "Questionnaire", "enableBehavior", element.getEnableBehaviorElement(), -1);
    if (element.hasRequiredElement())
      composeBoolean(t, "Questionnaire", "required", element.getRequiredElement(), -1);
    if (element.hasRepeatsElement())
      composeBoolean(t, "Questionnaire", "repeats", element.getRepeatsElement(), -1);
    if (element.hasReadOnlyElement())
      composeBoolean(t, "Questionnaire", "readOnly", element.getReadOnlyElement(), -1);
    if (element.hasMaxLengthElement())
      composeInteger(t, "Questionnaire", "maxLength", element.getMaxLengthElement(), -1);
    if (element.hasAnswerValueSetElement())
      composeCanonical(t, "Questionnaire", "answerValueSet", element.getAnswerValueSetElement(), -1);
    for (int i = 0; i < element.getAnswerOption().size(); i++)
      composeQuestionnaireQuestionnaireItemAnswerOptionComponent(t, "Questionnaire", "answerOption", element.getAnswerOption().get(i), i);
    for (int i = 0; i < element.getInitial().size(); i++)
      composeQuestionnaireQuestionnaireItemInitialComponent(t, "Questionnaire", "initial", element.getInitial().get(i), i);
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
    if (element.hasOperatorElement())
      composeEnum(t, "Questionnaire", "operator", element.getOperatorElement(), -1);
    if (element.hasAnswer())
      composeType(t, "Questionnaire", "answer", element.getAnswer(), -1);
  }

  protected void composeQuestionnaireQuestionnaireItemAnswerOptionComponent(Complex parent, String parentType, String name, Questionnaire.QuestionnaireItemAnswerOptionComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "answerOption", name, element, index);
    if (element.hasValue())
      composeType(t, "Questionnaire", "value", element.getValue(), -1);
    if (element.hasInitialSelectedElement())
      composeBoolean(t, "Questionnaire", "initialSelected", element.getInitialSelectedElement(), -1);
  }

  protected void composeQuestionnaireQuestionnaireItemInitialComponent(Complex parent, String parentType, String name, Questionnaire.QuestionnaireItemInitialComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "initial", name, element, index);
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
    for (int i = 0; i < element.getPartOf().size(); i++)
      composeReference(t, "QuestionnaireResponse", "partOf", element.getPartOf().get(i), i);
    if (element.hasQuestionnaireElement())
      composeCanonical(t, "QuestionnaireResponse", "questionnaire", element.getQuestionnaireElement(), -1);
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
    for (int i = 0; i < element.getRelationship().size(); i++)
      composeCodeableConcept(t, "RelatedPerson", "relationship", element.getRelationship().get(i), i);
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
    for (int i = 0; i < element.getInstantiatesCanonical().size(); i++)
      composeCanonical(t, "RequestGroup", "instantiatesCanonical", element.getInstantiatesCanonical().get(i), i);
    for (int i = 0; i < element.getInstantiatesUri().size(); i++)
      composeUri(t, "RequestGroup", "instantiatesUri", element.getInstantiatesUri().get(i), i);
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
    if (element.hasPriorityElement())
      composeEnum(t, "RequestGroup", "priority", element.getPriorityElement(), -1);
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
      composeCodeableConcept(t, "RequestGroup", "type", element.getType(), -1);
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
    if (element.hasPrimaryPurposeType())
      composeCodeableConcept(t, "ResearchStudy", "primaryPurposeType", element.getPrimaryPurposeType(), -1);
    if (element.hasPhase())
      composeCodeableConcept(t, "ResearchStudy", "phase", element.getPhase(), -1);
    for (int i = 0; i < element.getCategory().size(); i++)
      composeCodeableConcept(t, "ResearchStudy", "category", element.getCategory().get(i), i);
    for (int i = 0; i < element.getFocus().size(); i++)
      composeCodeableConcept(t, "ResearchStudy", "focus", element.getFocus().get(i), i);
    for (int i = 0; i < element.getCondition().size(); i++)
      composeCodeableConcept(t, "ResearchStudy", "condition", element.getCondition().get(i), i);
    for (int i = 0; i < element.getContact().size(); i++)
      composeContactDetail(t, "ResearchStudy", "contact", element.getContact().get(i), i);
    for (int i = 0; i < element.getRelatedArtifact().size(); i++)
      composeRelatedArtifact(t, "ResearchStudy", "relatedArtifact", element.getRelatedArtifact().get(i), i);
    for (int i = 0; i < element.getKeyword().size(); i++)
      composeCodeableConcept(t, "ResearchStudy", "keyword", element.getKeyword().get(i), i);
    for (int i = 0; i < element.getLocation().size(); i++)
      composeCodeableConcept(t, "ResearchStudy", "location", element.getLocation().get(i), i);
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
    for (int i = 0; i < element.getObjective().size(); i++)
      composeResearchStudyResearchStudyObjectiveComponent(t, "ResearchStudy", "objective", element.getObjective().get(i), i);
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
    if (element.hasType())
      composeCodeableConcept(t, "ResearchStudy", "type", element.getType(), -1);
    if (element.hasDescriptionElement())
      composeString(t, "ResearchStudy", "description", element.getDescriptionElement(), -1);
  }

  protected void composeResearchStudyResearchStudyObjectiveComponent(Complex parent, String parentType, String name, ResearchStudy.ResearchStudyObjectiveComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "objective", name, element, index);
    if (element.hasNameElement())
      composeString(t, "ResearchStudy", "name", element.getNameElement(), -1);
    if (element.hasType())
      composeCodeableConcept(t, "ResearchStudy", "type", element.getType(), -1);
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
    for (int i = 0; i < element.getIdentifier().size(); i++)
      composeIdentifier(t, "ResearchSubject", "identifier", element.getIdentifier().get(i), i);
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
    for (int i = 0; i < element.getIdentifier().size(); i++)
      composeIdentifier(t, "RiskAssessment", "identifier", element.getIdentifier().get(i), i);
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
    for (int i = 0; i < element.getReasonCode().size(); i++)
      composeCodeableConcept(t, "RiskAssessment", "reasonCode", element.getReasonCode().get(i), i);
    for (int i = 0; i < element.getReasonReference().size(); i++)
      composeReference(t, "RiskAssessment", "reasonReference", element.getReasonReference().get(i), i);
    for (int i = 0; i < element.getBasis().size(); i++)
      composeReference(t, "RiskAssessment", "basis", element.getBasis().get(i), i);
    for (int i = 0; i < element.getPrediction().size(); i++)
      composeRiskAssessmentRiskAssessmentPredictionComponent(t, "RiskAssessment", "prediction", element.getPrediction().get(i), i);
    if (element.hasMitigationElement())
      composeString(t, "RiskAssessment", "mitigation", element.getMitigationElement(), -1);
    for (int i = 0; i < element.getNote().size(); i++)
      composeAnnotation(t, "RiskAssessment", "note", element.getNote().get(i), i);
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
    for (int i = 0; i < element.getServiceCategory().size(); i++)
      composeCodeableConcept(t, "Schedule", "serviceCategory", element.getServiceCategory().get(i), i);
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
    if (element.hasDerivedFromElement())
      composeCanonical(t, "SearchParameter", "derivedFrom", element.getDerivedFromElement(), -1);
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
    if (element.hasDescriptionElement())
      composeMarkdown(t, "SearchParameter", "description", element.getDescriptionElement(), -1);
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
    if (element.hasExpressionElement())
      composeString(t, "SearchParameter", "expression", element.getExpressionElement(), -1);
    if (element.hasXpathElement())
      composeString(t, "SearchParameter", "xpath", element.getXpathElement(), -1);
    if (element.hasXpathUsageElement())
      composeEnum(t, "SearchParameter", "xpathUsage", element.getXpathUsageElement(), -1);
    for (int i = 0; i < element.getTarget().size(); i++)
      composeCode(t, "SearchParameter", "target", element.getTarget().get(i), i);
    if (element.hasMultipleOrElement())
      composeBoolean(t, "SearchParameter", "multipleOr", element.getMultipleOrElement(), -1);
    if (element.hasMultipleAndElement())
      composeBoolean(t, "SearchParameter", "multipleAnd", element.getMultipleAndElement(), -1);
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
    if (element.hasDefinitionElement())
      composeCanonical(t, "SearchParameter", "definition", element.getDefinitionElement(), -1);
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
    if (element.hasOrientationElement())
      composeEnum(t, "Sequence", "orientation", element.getOrientationElement(), -1);
    if (element.hasReferenceSeqId())
      composeCodeableConcept(t, "Sequence", "referenceSeqId", element.getReferenceSeqId(), -1);
    if (element.hasReferenceSeqPointer())
      composeReference(t, "Sequence", "referenceSeqPointer", element.getReferenceSeqPointer(), -1);
    if (element.hasReferenceSeqStringElement())
      composeString(t, "Sequence", "referenceSeqString", element.getReferenceSeqStringElement(), -1);
    if (element.hasStrandElement())
      composeEnum(t, "Sequence", "strand", element.getStrandElement(), -1);
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

  protected void composeServiceRequest(Complex parent, String parentType, String name, ServiceRequest element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "ServiceRequest", name, element, index);
    for (int i = 0; i < element.getIdentifier().size(); i++)
      composeIdentifier(t, "ServiceRequest", "identifier", element.getIdentifier().get(i), i);
    for (int i = 0; i < element.getInstantiatesCanonical().size(); i++)
      composeCanonical(t, "ServiceRequest", "instantiatesCanonical", element.getInstantiatesCanonical().get(i), i);
    for (int i = 0; i < element.getInstantiatesUri().size(); i++)
      composeUri(t, "ServiceRequest", "instantiatesUri", element.getInstantiatesUri().get(i), i);
    for (int i = 0; i < element.getBasedOn().size(); i++)
      composeReference(t, "ServiceRequest", "basedOn", element.getBasedOn().get(i), i);
    for (int i = 0; i < element.getReplaces().size(); i++)
      composeReference(t, "ServiceRequest", "replaces", element.getReplaces().get(i), i);
    if (element.hasRequisition())
      composeIdentifier(t, "ServiceRequest", "requisition", element.getRequisition(), -1);
    if (element.hasStatusElement())
      composeEnum(t, "ServiceRequest", "status", element.getStatusElement(), -1);
    if (element.hasIntentElement())
      composeEnum(t, "ServiceRequest", "intent", element.getIntentElement(), -1);
    for (int i = 0; i < element.getCategory().size(); i++)
      composeCodeableConcept(t, "ServiceRequest", "category", element.getCategory().get(i), i);
    if (element.hasPriorityElement())
      composeEnum(t, "ServiceRequest", "priority", element.getPriorityElement(), -1);
    if (element.hasDoNotPerformElement())
      composeBoolean(t, "ServiceRequest", "doNotPerform", element.getDoNotPerformElement(), -1);
    if (element.hasCode())
      composeCodeableConcept(t, "ServiceRequest", "code", element.getCode(), -1);
    for (int i = 0; i < element.getOrderDetail().size(); i++)
      composeCodeableConcept(t, "ServiceRequest", "orderDetail", element.getOrderDetail().get(i), i);
    if (element.hasQuantity())
      composeType(t, "ServiceRequest", "quantity", element.getQuantity(), -1);
    if (element.hasSubject())
      composeReference(t, "ServiceRequest", "subject", element.getSubject(), -1);
    if (element.hasContext())
      composeReference(t, "ServiceRequest", "context", element.getContext(), -1);
    if (element.hasOccurrence())
      composeType(t, "ServiceRequest", "occurrence", element.getOccurrence(), -1);
    if (element.hasAsNeeded())
      composeType(t, "ServiceRequest", "asNeeded", element.getAsNeeded(), -1);
    if (element.hasAuthoredOnElement())
      composeDateTime(t, "ServiceRequest", "authoredOn", element.getAuthoredOnElement(), -1);
    if (element.hasRequester())
      composeReference(t, "ServiceRequest", "requester", element.getRequester(), -1);
    if (element.hasPerformerType())
      composeCodeableConcept(t, "ServiceRequest", "performerType", element.getPerformerType(), -1);
    for (int i = 0; i < element.getPerformer().size(); i++)
      composeReference(t, "ServiceRequest", "performer", element.getPerformer().get(i), i);
    for (int i = 0; i < element.getLocationCode().size(); i++)
      composeCodeableConcept(t, "ServiceRequest", "locationCode", element.getLocationCode().get(i), i);
    for (int i = 0; i < element.getLocationReference().size(); i++)
      composeReference(t, "ServiceRequest", "locationReference", element.getLocationReference().get(i), i);
    for (int i = 0; i < element.getReasonCode().size(); i++)
      composeCodeableConcept(t, "ServiceRequest", "reasonCode", element.getReasonCode().get(i), i);
    for (int i = 0; i < element.getReasonReference().size(); i++)
      composeReference(t, "ServiceRequest", "reasonReference", element.getReasonReference().get(i), i);
    for (int i = 0; i < element.getInsurance().size(); i++)
      composeReference(t, "ServiceRequest", "insurance", element.getInsurance().get(i), i);
    for (int i = 0; i < element.getSupportingInfo().size(); i++)
      composeReference(t, "ServiceRequest", "supportingInfo", element.getSupportingInfo().get(i), i);
    for (int i = 0; i < element.getSpecimen().size(); i++)
      composeReference(t, "ServiceRequest", "specimen", element.getSpecimen().get(i), i);
    for (int i = 0; i < element.getBodySite().size(); i++)
      composeCodeableConcept(t, "ServiceRequest", "bodySite", element.getBodySite().get(i), i);
    for (int i = 0; i < element.getNote().size(); i++)
      composeAnnotation(t, "ServiceRequest", "note", element.getNote().get(i), i);
    if (element.hasPatientInstructionElement())
      composeString(t, "ServiceRequest", "patientInstruction", element.getPatientInstructionElement(), -1);
    for (int i = 0; i < element.getRelevantHistory().size(); i++)
      composeReference(t, "ServiceRequest", "relevantHistory", element.getRelevantHistory().get(i), i);
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
    for (int i = 0; i < element.getServiceCategory().size(); i++)
      composeCodeableConcept(t, "Slot", "serviceCategory", element.getServiceCategory().get(i), i);
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
    for (int i = 0; i < element.getCondition().size(); i++)
      composeCodeableConcept(t, "Specimen", "condition", element.getCondition().get(i), i);
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
    if (element.hasDuration())
      composeDuration(t, "Specimen", "duration", element.getDuration(), -1);
    if (element.hasQuantity())
      composeQuantity(t, "Specimen", "quantity", element.getQuantity(), -1);
    if (element.hasMethod())
      composeCodeableConcept(t, "Specimen", "method", element.getMethod(), -1);
    if (element.hasBodySite())
      composeCodeableConcept(t, "Specimen", "bodySite", element.getBodySite(), -1);
    if (element.hasFastingStatus())
      composeType(t, "Specimen", "fastingStatus", element.getFastingStatus(), -1);
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
    for (int i = 0; i < element.getTypeTested().size(); i++)
      composeSpecimenDefinitionSpecimenDefinitionTypeTestedComponent(t, "SpecimenDefinition", "typeTested", element.getTypeTested().get(i), i);
  }

  protected void composeSpecimenDefinitionSpecimenDefinitionTypeTestedComponent(Complex parent, String parentType, String name, SpecimenDefinition.SpecimenDefinitionTypeTestedComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "typeTested", name, element, index);
    if (element.hasIsDerivedElement())
      composeBoolean(t, "SpecimenDefinition", "isDerived", element.getIsDerivedElement(), -1);
    if (element.hasType())
      composeCodeableConcept(t, "SpecimenDefinition", "type", element.getType(), -1);
    if (element.hasPreferenceElement())
      composeEnum(t, "SpecimenDefinition", "preference", element.getPreferenceElement(), -1);
    if (element.hasContainer())
      composeSpecimenDefinitionSpecimenDefinitionTypeTestedContainerComponent(t, "SpecimenDefinition", "container", element.getContainer(), -1);
    if (element.hasRequirementElement())
      composeString(t, "SpecimenDefinition", "requirement", element.getRequirementElement(), -1);
    if (element.hasRetentionTime())
      composeDuration(t, "SpecimenDefinition", "retentionTime", element.getRetentionTime(), -1);
    for (int i = 0; i < element.getRejectionCriterion().size(); i++)
      composeCodeableConcept(t, "SpecimenDefinition", "rejectionCriterion", element.getRejectionCriterion().get(i), i);
    for (int i = 0; i < element.getHandling().size(); i++)
      composeSpecimenDefinitionSpecimenDefinitionTypeTestedHandlingComponent(t, "SpecimenDefinition", "handling", element.getHandling().get(i), i);
  }

  protected void composeSpecimenDefinitionSpecimenDefinitionTypeTestedContainerComponent(Complex parent, String parentType, String name, SpecimenDefinition.SpecimenDefinitionTypeTestedContainerComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "container", name, element, index);
    if (element.hasMaterial())
      composeCodeableConcept(t, "SpecimenDefinition", "material", element.getMaterial(), -1);
    if (element.hasType())
      composeCodeableConcept(t, "SpecimenDefinition", "type", element.getType(), -1);
    if (element.hasCap())
      composeCodeableConcept(t, "SpecimenDefinition", "cap", element.getCap(), -1);
    if (element.hasDescriptionElement())
      composeString(t, "SpecimenDefinition", "description", element.getDescriptionElement(), -1);
    if (element.hasCapacity())
      composeQuantity(t, "SpecimenDefinition", "capacity", element.getCapacity(), -1);
    if (element.hasMinimumVolume())
      composeQuantity(t, "SpecimenDefinition", "minimumVolume", element.getMinimumVolume(), -1);
    for (int i = 0; i < element.getAdditive().size(); i++)
      composeSpecimenDefinitionSpecimenDefinitionTypeTestedContainerAdditiveComponent(t, "SpecimenDefinition", "additive", element.getAdditive().get(i), i);
    if (element.hasPreparationElement())
      composeString(t, "SpecimenDefinition", "preparation", element.getPreparationElement(), -1);
  }

  protected void composeSpecimenDefinitionSpecimenDefinitionTypeTestedContainerAdditiveComponent(Complex parent, String parentType, String name, SpecimenDefinition.SpecimenDefinitionTypeTestedContainerAdditiveComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "additive", name, element, index);
    if (element.hasAdditive())
      composeType(t, "SpecimenDefinition", "additive", element.getAdditive(), -1);
  }

  protected void composeSpecimenDefinitionSpecimenDefinitionTypeTestedHandlingComponent(Complex parent, String parentType, String name, SpecimenDefinition.SpecimenDefinitionTypeTestedHandlingComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "handling", name, element, index);
    if (element.hasTemperatureQualifier())
      composeCodeableConcept(t, "SpecimenDefinition", "temperatureQualifier", element.getTemperatureQualifier(), -1);
    if (element.hasTemperatureRange())
      composeRange(t, "SpecimenDefinition", "temperatureRange", element.getTemperatureRange(), -1);
    if (element.hasMaxDuration())
      composeDuration(t, "SpecimenDefinition", "maxDuration", element.getMaxDuration(), -1);
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
    for (int i = 0; i < element.getContext().size(); i++)
      composeStructureDefinitionStructureDefinitionContextComponent(t, "StructureDefinition", "context", element.getContext().get(i), i);
    for (int i = 0; i < element.getContextInvariant().size(); i++)
      composeString(t, "StructureDefinition", "contextInvariant", element.getContextInvariant().get(i), i);
    if (element.hasTypeElement())
      composeUri(t, "StructureDefinition", "type", element.getTypeElement(), -1);
    if (element.hasBaseDefinitionElement())
      composeCanonical(t, "StructureDefinition", "baseDefinition", element.getBaseDefinitionElement(), -1);
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

  protected void composeStructureDefinitionStructureDefinitionContextComponent(Complex parent, String parentType, String name, StructureDefinition.StructureDefinitionContextComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "context", name, element, index);
    if (element.hasTypeElement())
      composeEnum(t, "StructureDefinition", "type", element.getTypeElement(), -1);
    if (element.hasExpressionElement())
      composeString(t, "StructureDefinition", "expression", element.getExpressionElement(), -1);
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
      composeCanonical(t, "StructureMap", "import", element.getImport().get(i), i);
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
      composeCanonical(t, "StructureMap", "url", element.getUrlElement(), -1);
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
    if (element.hasLogMessageElement())
      composeString(t, "StructureMap", "logMessage", element.getLogMessageElement(), -1);
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
      composeUrl(t, "Subscription", "endpoint", element.getEndpointElement(), -1);
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

  protected void composeSubstancePolymer(Complex parent, String parentType, String name, SubstancePolymer element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "SubstancePolymer", name, element, index);
    if (element.hasClass_())
      composeCodeableConcept(t, "SubstancePolymer", "class", element.getClass_(), -1);
    if (element.hasGeometry())
      composeCodeableConcept(t, "SubstancePolymer", "geometry", element.getGeometry(), -1);
    for (int i = 0; i < element.getCopolymerConnectivity().size(); i++)
      composeCodeableConcept(t, "SubstancePolymer", "copolymerConnectivity", element.getCopolymerConnectivity().get(i), i);
    for (int i = 0; i < element.getModification().size(); i++)
      composeString(t, "SubstancePolymer", "modification", element.getModification().get(i), i);
    for (int i = 0; i < element.getMonomerSet().size(); i++)
      composeSubstancePolymerSubstancePolymerMonomerSetComponent(t, "SubstancePolymer", "monomerSet", element.getMonomerSet().get(i), i);
    for (int i = 0; i < element.getRepeat().size(); i++)
      composeSubstancePolymerSubstancePolymerRepeatComponent(t, "SubstancePolymer", "repeat", element.getRepeat().get(i), i);
  }

  protected void composeSubstancePolymerSubstancePolymerMonomerSetComponent(Complex parent, String parentType, String name, SubstancePolymer.SubstancePolymerMonomerSetComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "monomerSet", name, element, index);
    if (element.hasRatioType())
      composeCodeableConcept(t, "SubstancePolymer", "ratioType", element.getRatioType(), -1);
    for (int i = 0; i < element.getStartingMaterial().size(); i++)
      composeSubstancePolymerSubstancePolymerMonomerSetStartingMaterialComponent(t, "SubstancePolymer", "startingMaterial", element.getStartingMaterial().get(i), i);
  }

  protected void composeSubstancePolymerSubstancePolymerMonomerSetStartingMaterialComponent(Complex parent, String parentType, String name, SubstancePolymer.SubstancePolymerMonomerSetStartingMaterialComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "startingMaterial", name, element, index);
    if (element.hasMaterial())
      composeCodeableConcept(t, "SubstancePolymer", "material", element.getMaterial(), -1);
    if (element.hasType())
      composeCodeableConcept(t, "SubstancePolymer", "type", element.getType(), -1);
    if (element.hasIsDefiningElement())
      composeBoolean(t, "SubstancePolymer", "isDefining", element.getIsDefiningElement(), -1);
    if (element.hasAmount())
      composeSubstanceAmount(t, "SubstancePolymer", "amount", element.getAmount(), -1);
  }

  protected void composeSubstancePolymerSubstancePolymerRepeatComponent(Complex parent, String parentType, String name, SubstancePolymer.SubstancePolymerRepeatComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "repeat", name, element, index);
    if (element.hasNumberOfUnitsElement())
      composeInteger(t, "SubstancePolymer", "numberOfUnits", element.getNumberOfUnitsElement(), -1);
    if (element.hasAverageMolecularFormulaElement())
      composeString(t, "SubstancePolymer", "averageMolecularFormula", element.getAverageMolecularFormulaElement(), -1);
    if (element.hasRepeatUnitAmountType())
      composeCodeableConcept(t, "SubstancePolymer", "repeatUnitAmountType", element.getRepeatUnitAmountType(), -1);
    for (int i = 0; i < element.getRepeatUnit().size(); i++)
      composeSubstancePolymerSubstancePolymerRepeatRepeatUnitComponent(t, "SubstancePolymer", "repeatUnit", element.getRepeatUnit().get(i), i);
  }

  protected void composeSubstancePolymerSubstancePolymerRepeatRepeatUnitComponent(Complex parent, String parentType, String name, SubstancePolymer.SubstancePolymerRepeatRepeatUnitComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "repeatUnit", name, element, index);
    if (element.hasOrientationOfPolymerisation())
      composeCodeableConcept(t, "SubstancePolymer", "orientationOfPolymerisation", element.getOrientationOfPolymerisation(), -1);
    if (element.hasRepeatUnitElement())
      composeString(t, "SubstancePolymer", "repeatUnit", element.getRepeatUnitElement(), -1);
    if (element.hasAmount())
      composeSubstanceAmount(t, "SubstancePolymer", "amount", element.getAmount(), -1);
    for (int i = 0; i < element.getDegreeOfPolymerisation().size(); i++)
      composeSubstancePolymerSubstancePolymerRepeatRepeatUnitDegreeOfPolymerisationComponent(t, "SubstancePolymer", "degreeOfPolymerisation", element.getDegreeOfPolymerisation().get(i), i);
    for (int i = 0; i < element.getStructuralRepresentation().size(); i++)
      composeSubstancePolymerSubstancePolymerRepeatRepeatUnitStructuralRepresentationComponent(t, "SubstancePolymer", "structuralRepresentation", element.getStructuralRepresentation().get(i), i);
  }

  protected void composeSubstancePolymerSubstancePolymerRepeatRepeatUnitDegreeOfPolymerisationComponent(Complex parent, String parentType, String name, SubstancePolymer.SubstancePolymerRepeatRepeatUnitDegreeOfPolymerisationComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "degreeOfPolymerisation", name, element, index);
    if (element.hasDegree())
      composeCodeableConcept(t, "SubstancePolymer", "degree", element.getDegree(), -1);
    if (element.hasAmount())
      composeSubstanceAmount(t, "SubstancePolymer", "amount", element.getAmount(), -1);
  }

  protected void composeSubstancePolymerSubstancePolymerRepeatRepeatUnitStructuralRepresentationComponent(Complex parent, String parentType, String name, SubstancePolymer.SubstancePolymerRepeatRepeatUnitStructuralRepresentationComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "structuralRepresentation", name, element, index);
    if (element.hasType())
      composeCodeableConcept(t, "SubstancePolymer", "type", element.getType(), -1);
    if (element.hasRepresentationElement())
      composeString(t, "SubstancePolymer", "representation", element.getRepresentationElement(), -1);
    if (element.hasAttachment())
      composeAttachment(t, "SubstancePolymer", "attachment", element.getAttachment(), -1);
  }

  protected void composeSubstanceReferenceInformation(Complex parent, String parentType, String name, SubstanceReferenceInformation element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "SubstanceReferenceInformation", name, element, index);
    if (element.hasCommentElement())
      composeString(t, "SubstanceReferenceInformation", "comment", element.getCommentElement(), -1);
    for (int i = 0; i < element.getGene().size(); i++)
      composeSubstanceReferenceInformationSubstanceReferenceInformationGeneComponent(t, "SubstanceReferenceInformation", "gene", element.getGene().get(i), i);
    for (int i = 0; i < element.getGene().size(); i++)
      composeSubstanceReferenceInformationSubstanceReferenceInformationGeneElementComponent(t, "SubstanceReferenceInformation", "geneElement", null, i);
    for (int i = 0; i < element.getClassification().size(); i++)
      composeSubstanceReferenceInformationSubstanceReferenceInformationClassificationComponent(t, "SubstanceReferenceInformation", "classification", element.getClassification().get(i), i);
    for (int i = 0; i < element.getRelationship().size(); i++)
      composeSubstanceReferenceInformationSubstanceReferenceInformationRelationshipComponent(t, "SubstanceReferenceInformation", "relationship", element.getRelationship().get(i), i);
    for (int i = 0; i < element.getTarget().size(); i++)
      composeSubstanceReferenceInformationSubstanceReferenceInformationTargetComponent(t, "SubstanceReferenceInformation", "target", element.getTarget().get(i), i);
  }

  protected void composeSubstanceReferenceInformationSubstanceReferenceInformationGeneComponent(Complex parent, String parentType, String name, SubstanceReferenceInformation.SubstanceReferenceInformationGeneComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "gene", name, element, index);
    if (element.hasGeneSequenceOrigin())
      composeCodeableConcept(t, "SubstanceReferenceInformation", "geneSequenceOrigin", element.getGeneSequenceOrigin(), -1);
    if (element.hasGene())
      composeCodeableConcept(t, "SubstanceReferenceInformation", "gene", element.getGene(), -1);
    for (int i = 0; i < element.getSource().size(); i++)
      composeReference(t, "SubstanceReferenceInformation", "source", element.getSource().get(i), i);
  }

  protected void composeSubstanceReferenceInformationSubstanceReferenceInformationGeneElementComponent(Complex parent, String parentType, String name, SubstanceReferenceInformation.SubstanceReferenceInformationGeneElementComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "geneElement", name, element, index);
    if (element.hasType())
      composeCodeableConcept(t, "SubstanceReferenceInformation", "type", element.getType(), -1);
    if (element.hasElement())
      composeIdentifier(t, "SubstanceReferenceInformation", "element", element.getElement(), -1);
    for (int i = 0; i < element.getSource().size(); i++)
      composeReference(t, "SubstanceReferenceInformation", "source", element.getSource().get(i), i);
  }

  protected void composeSubstanceReferenceInformationSubstanceReferenceInformationClassificationComponent(Complex parent, String parentType, String name, SubstanceReferenceInformation.SubstanceReferenceInformationClassificationComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "classification", name, element, index);
    if (element.hasDomain())
      composeCodeableConcept(t, "SubstanceReferenceInformation", "domain", element.getDomain(), -1);
    if (element.hasClassification())
      composeCodeableConcept(t, "SubstanceReferenceInformation", "classification", element.getClassification(), -1);
    for (int i = 0; i < element.getSubtype().size(); i++)
      composeCodeableConcept(t, "SubstanceReferenceInformation", "subtype", element.getSubtype().get(i), i);
    for (int i = 0; i < element.getSource().size(); i++)
      composeReference(t, "SubstanceReferenceInformation", "source", element.getSource().get(i), i);
  }

  protected void composeSubstanceReferenceInformationSubstanceReferenceInformationRelationshipComponent(Complex parent, String parentType, String name, SubstanceReferenceInformation.SubstanceReferenceInformationRelationshipComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "relationship", name, element, index);
    if (element.hasSubstance())
      composeType(t, "SubstanceReferenceInformation", "substance", element.getSubstance(), -1);
    if (element.hasRelationship())
      composeCodeableConcept(t, "SubstanceReferenceInformation", "relationship", element.getRelationship(), -1);
    if (element.hasInteraction())
      composeCodeableConcept(t, "SubstanceReferenceInformation", "interaction", element.getInteraction(), -1);
    if (element.hasIsDefiningElement())
      composeBoolean(t, "SubstanceReferenceInformation", "isDefining", element.getIsDefiningElement(), -1);
    if (element.hasAmount())
      composeType(t, "SubstanceReferenceInformation", "amount", element.getAmount(), -1);
    if (element.hasAmountType())
      composeCodeableConcept(t, "SubstanceReferenceInformation", "amountType", element.getAmountType(), -1);
    if (element.hasAmountTextElement())
      composeString(t, "SubstanceReferenceInformation", "amountText", element.getAmountTextElement(), -1);
    for (int i = 0; i < element.getSource().size(); i++)
      composeReference(t, "SubstanceReferenceInformation", "source", element.getSource().get(i), i);
  }

  protected void composeSubstanceReferenceInformationSubstanceReferenceInformationTargetComponent(Complex parent, String parentType, String name, SubstanceReferenceInformation.SubstanceReferenceInformationTargetComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "target", name, element, index);
    if (element.hasTarget())
      composeIdentifier(t, "SubstanceReferenceInformation", "target", element.getTarget(), -1);
    if (element.hasType())
      composeCodeableConcept(t, "SubstanceReferenceInformation", "type", element.getType(), -1);
    if (element.hasInteraction())
      composeCodeableConcept(t, "SubstanceReferenceInformation", "interaction", element.getInteraction(), -1);
    if (element.hasOrganism())
      composeCodeableConcept(t, "SubstanceReferenceInformation", "organism", element.getOrganism(), -1);
    if (element.hasOrganismType())
      composeCodeableConcept(t, "SubstanceReferenceInformation", "organismType", element.getOrganismType(), -1);
    for (int i = 0; i < element.getSource().size(); i++)
      composeReference(t, "SubstanceReferenceInformation", "source", element.getSource().get(i), i);
    if (element.hasAmount())
      composeType(t, "SubstanceReferenceInformation", "amount", element.getAmount(), -1);
    if (element.hasAmountType())
      composeCodeableConcept(t, "SubstanceReferenceInformation", "amountType", element.getAmountType(), -1);
  }

  protected void composeSubstanceSpecification(Complex parent, String parentType, String name, SubstanceSpecification element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "SubstanceSpecification", name, element, index);
    if (element.hasCommentElement())
      composeString(t, "SubstanceSpecification", "comment", element.getCommentElement(), -1);
    if (element.hasStoichiometricElement())
      composeBoolean(t, "SubstanceSpecification", "stoichiometric", element.getStoichiometricElement(), -1);
    if (element.hasIdentifier())
      composeIdentifier(t, "SubstanceSpecification", "identifier", element.getIdentifier(), -1);
    if (element.hasType())
      composeCodeableConcept(t, "SubstanceSpecification", "type", element.getType(), -1);
    for (int i = 0; i < element.getReferenceSource().size(); i++)
      composeString(t, "SubstanceSpecification", "referenceSource", element.getReferenceSource().get(i), i);
    for (int i = 0; i < element.getMoiety().size(); i++)
      composeSubstanceSpecificationSubstanceSpecificationMoietyComponent(t, "SubstanceSpecification", "moiety", element.getMoiety().get(i), i);
    for (int i = 0; i < element.getProperty().size(); i++)
      composeSubstanceSpecificationSubstanceSpecificationPropertyComponent(t, "SubstanceSpecification", "property", element.getProperty().get(i), i);
    if (element.hasReferenceInformation())
      composeReference(t, "SubstanceSpecification", "referenceInformation", element.getReferenceInformation(), -1);
    if (element.hasStructure())
      composeSubstanceSpecificationSubstanceSpecificationStructureComponent(t, "SubstanceSpecification", "structure", element.getStructure(), -1);
    for (int i = 0; i < element.getSubstanceCode().size(); i++)
      composeSubstanceSpecificationSubstanceSpecificationSubstanceCodeComponent(t, "SubstanceSpecification", "substanceCode", element.getSubstanceCode().get(i), i);
    for (int i = 0; i < element.getSubstanceName().size(); i++)
      composeSubstanceSpecificationSubstanceSpecificationSubstanceNameComponent(t, "SubstanceSpecification", "substanceName", element.getSubstanceName().get(i), i);
    for (int i = 0; i < element.getMolecularWeight().size(); i++)
      composeSubstanceSpecificationSubstanceSpecificationStructureIsotopeMolecularWeightComponent(t, "SubstanceSpecification", "molecularWeight", element.getMolecularWeight().get(i), i);
    if (element.hasPolymer())
      composeReference(t, "SubstanceSpecification", "polymer", element.getPolymer(), -1);
  }

  protected void composeSubstanceSpecificationSubstanceSpecificationMoietyComponent(Complex parent, String parentType, String name, SubstanceSpecification.SubstanceSpecificationMoietyComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "moiety", name, element, index);
    if (element.hasRole())
      composeCodeableConcept(t, "SubstanceSpecification", "role", element.getRole(), -1);
    if (element.hasIdentifier())
      composeIdentifier(t, "SubstanceSpecification", "identifier", element.getIdentifier(), -1);
    if (element.hasNameElement())
      composeString(t, "SubstanceSpecification", "name", element.getNameElement(), -1);
    if (element.hasStereochemistry())
      composeCodeableConcept(t, "SubstanceSpecification", "stereochemistry", element.getStereochemistry(), -1);
    if (element.hasOpticalActivity())
      composeCodeableConcept(t, "SubstanceSpecification", "opticalActivity", element.getOpticalActivity(), -1);
    if (element.hasMolecularFormulaElement())
      composeString(t, "SubstanceSpecification", "molecularFormula", element.getMolecularFormulaElement(), -1);
    if (element.hasAmountElement())
      composeString(t, "SubstanceSpecification", "amount", element.getAmountElement(), -1);
  }

  protected void composeSubstanceSpecificationSubstanceSpecificationPropertyComponent(Complex parent, String parentType, String name, SubstanceSpecification.SubstanceSpecificationPropertyComponent element, int index) {
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
      composeCodeableConcept(t, "SubstanceSpecification", "type", element.getType(), -1);
    if (element.hasName())
      composeCodeableConcept(t, "SubstanceSpecification", "name", element.getName(), -1);
    if (element.hasParametersElement())
      composeString(t, "SubstanceSpecification", "parameters", element.getParametersElement(), -1);
    if (element.hasSubstanceId())
      composeIdentifier(t, "SubstanceSpecification", "substanceId", element.getSubstanceId(), -1);
    if (element.hasSubstanceNameElement())
      composeString(t, "SubstanceSpecification", "substanceName", element.getSubstanceNameElement(), -1);
    if (element.hasAmountElement())
      composeString(t, "SubstanceSpecification", "amount", element.getAmountElement(), -1);
  }

  protected void composeSubstanceSpecificationSubstanceSpecificationStructureComponent(Complex parent, String parentType, String name, SubstanceSpecification.SubstanceSpecificationStructureComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "structure", name, element, index);
    if (element.hasStereochemistry())
      composeCodeableConcept(t, "SubstanceSpecification", "stereochemistry", element.getStereochemistry(), -1);
    if (element.hasOpticalActivity())
      composeCodeableConcept(t, "SubstanceSpecification", "opticalActivity", element.getOpticalActivity(), -1);
    if (element.hasMolecularFormulaElement())
      composeString(t, "SubstanceSpecification", "molecularFormula", element.getMolecularFormulaElement(), -1);
    if (element.hasMolecularFormulaByMoietyElement())
      composeString(t, "SubstanceSpecification", "molecularFormulaByMoiety", element.getMolecularFormulaByMoietyElement(), -1);
    for (int i = 0; i < element.getIsotope().size(); i++)
      composeSubstanceSpecificationSubstanceSpecificationStructureIsotopeComponent(t, "SubstanceSpecification", "isotope", element.getIsotope().get(i), i);
    if (element.hasMolecularWeight())
      composeSubstanceSpecificationSubstanceSpecificationStructureIsotopeMolecularWeightComponent(t, "SubstanceSpecification", "molecularWeight", element.getMolecularWeight(), -1);
    for (int i = 0; i < element.getReferenceSource().size(); i++)
      composeReference(t, "SubstanceSpecification", "referenceSource", element.getReferenceSource().get(i), i);
    for (int i = 0; i < element.getStructuralRepresentation().size(); i++)
      composeSubstanceSpecificationSubstanceSpecificationStructureStructuralRepresentationComponent(t, "SubstanceSpecification", "structuralRepresentation", element.getStructuralRepresentation().get(i), i);
  }

  protected void composeSubstanceSpecificationSubstanceSpecificationStructureIsotopeComponent(Complex parent, String parentType, String name, SubstanceSpecification.SubstanceSpecificationStructureIsotopeComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "isotope", name, element, index);
    if (element.hasNuclideId())
      composeIdentifier(t, "SubstanceSpecification", "nuclideId", element.getNuclideId(), -1);
    if (element.hasNuclideName())
      composeCodeableConcept(t, "SubstanceSpecification", "nuclideName", element.getNuclideName(), -1);
    if (element.hasSubstitutionType())
      composeCodeableConcept(t, "SubstanceSpecification", "substitutionType", element.getSubstitutionType(), -1);
    if (element.hasNuclideHalfLife())
      composeQuantity(t, "SubstanceSpecification", "nuclideHalfLife", element.getNuclideHalfLife(), -1);
    if (element.hasAmountElement())
      composeString(t, "SubstanceSpecification", "amount", element.getAmountElement(), -1);
    if (element.hasMolecularWeight())
      composeSubstanceSpecificationSubstanceSpecificationStructureIsotopeMolecularWeightComponent(t, "SubstanceSpecification", "molecularWeight", element.getMolecularWeight(), -1);
  }

  protected void composeSubstanceSpecificationSubstanceSpecificationStructureIsotopeMolecularWeightComponent(Complex parent, String parentType, String name, SubstanceSpecification.SubstanceSpecificationStructureIsotopeMolecularWeightComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "molecularWeight", name, element, index);
    if (element.hasMethod())
      composeCodeableConcept(t, "SubstanceSpecification", "method", element.getMethod(), -1);
    if (element.hasType())
      composeCodeableConcept(t, "SubstanceSpecification", "type", element.getType(), -1);
    if (element.hasAmountElement())
      composeString(t, "SubstanceSpecification", "amount", element.getAmountElement(), -1);
  }

  protected void composeSubstanceSpecificationSubstanceSpecificationStructureStructuralRepresentationComponent(Complex parent, String parentType, String name, SubstanceSpecification.SubstanceSpecificationStructureStructuralRepresentationComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "structuralRepresentation", name, element, index);
    if (element.hasType())
      composeCodeableConcept(t, "SubstanceSpecification", "type", element.getType(), -1);
    if (element.hasRepresentationElement())
      composeString(t, "SubstanceSpecification", "representation", element.getRepresentationElement(), -1);
    if (element.hasAttachment())
      composeAttachment(t, "SubstanceSpecification", "attachment", element.getAttachment(), -1);
  }

  protected void composeSubstanceSpecificationSubstanceSpecificationSubstanceCodeComponent(Complex parent, String parentType, String name, SubstanceSpecification.SubstanceSpecificationSubstanceCodeComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "substanceCode", name, element, index);
    if (element.hasCode())
      composeCodeableConcept(t, "SubstanceSpecification", "code", element.getCode(), -1);
    if (element.hasStatus())
      composeCodeableConcept(t, "SubstanceSpecification", "status", element.getStatus(), -1);
    if (element.hasStatusDateElement())
      composeDateTime(t, "SubstanceSpecification", "statusDate", element.getStatusDateElement(), -1);
    if (element.hasCommentElement())
      composeString(t, "SubstanceSpecification", "comment", element.getCommentElement(), -1);
    for (int i = 0; i < element.getReferenceSource().size(); i++)
      composeString(t, "SubstanceSpecification", "referenceSource", element.getReferenceSource().get(i), i);
  }

  protected void composeSubstanceSpecificationSubstanceSpecificationSubstanceNameComponent(Complex parent, String parentType, String name, SubstanceSpecification.SubstanceSpecificationSubstanceNameComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "substanceName", name, element, index);
    if (element.hasNameElement())
      composeString(t, "SubstanceSpecification", "name", element.getNameElement(), -1);
    if (element.hasType())
      composeCodeableConcept(t, "SubstanceSpecification", "type", element.getType(), -1);
    for (int i = 0; i < element.getLanguage().size(); i++)
      composeCodeableConcept(t, "SubstanceSpecification", "language", element.getLanguage().get(i), i);
    for (int i = 0; i < element.getDomain().size(); i++)
      composeCodeableConcept(t, "SubstanceSpecification", "domain", element.getDomain().get(i), i);
    for (int i = 0; i < element.getJurisdiction().size(); i++)
      composeCodeableConcept(t, "SubstanceSpecification", "jurisdiction", element.getJurisdiction().get(i), i);
    for (int i = 0; i < element.getOfficialName().size(); i++)
      composeSubstanceSpecificationSubstanceSpecificationSubstanceNameOfficialNameComponent(t, "SubstanceSpecification", "officialName", element.getOfficialName().get(i), i);
    for (int i = 0; i < element.getReferenceSource().size(); i++)
      composeString(t, "SubstanceSpecification", "referenceSource", element.getReferenceSource().get(i), i);
  }

  protected void composeSubstanceSpecificationSubstanceSpecificationSubstanceNameOfficialNameComponent(Complex parent, String parentType, String name, SubstanceSpecification.SubstanceSpecificationSubstanceNameOfficialNameComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "officialName", name, element, index);
    if (element.hasAuthority())
      composeCodeableConcept(t, "SubstanceSpecification", "authority", element.getAuthority(), -1);
    if (element.hasStatus())
      composeCodeableConcept(t, "SubstanceSpecification", "status", element.getStatus(), -1);
    if (element.hasDateElement())
      composeDateTime(t, "SubstanceSpecification", "date", element.getDateElement(), -1);
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
    for (int i = 0; i < element.getIdentifier().size(); i++)
      composeIdentifier(t, "SupplyDelivery", "identifier", element.getIdentifier().get(i), i);
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
    if (element.hasItem())
      composeType(t, "SupplyRequest", "item", element.getItem(), -1);
    if (element.hasQuantity())
      composeQuantity(t, "SupplyRequest", "quantity", element.getQuantity(), -1);
    for (int i = 0; i < element.getParameter().size(); i++)
      composeSupplyRequestSupplyRequestParameterComponent(t, "SupplyRequest", "parameter", element.getParameter().get(i), i);
    if (element.hasOccurrence())
      composeType(t, "SupplyRequest", "occurrence", element.getOccurrence(), -1);
    if (element.hasAuthoredOnElement())
      composeDateTime(t, "SupplyRequest", "authoredOn", element.getAuthoredOnElement(), -1);
    if (element.hasRequester())
      composeReference(t, "SupplyRequest", "requester", element.getRequester(), -1);
    for (int i = 0; i < element.getSupplier().size(); i++)
      composeReference(t, "SupplyRequest", "supplier", element.getSupplier().get(i), i);
    for (int i = 0; i < element.getReasonCode().size(); i++)
      composeCodeableConcept(t, "SupplyRequest", "reasonCode", element.getReasonCode().get(i), i);
    for (int i = 0; i < element.getReasonReference().size(); i++)
      composeReference(t, "SupplyRequest", "reasonReference", element.getReasonReference().get(i), i);
    if (element.hasDeliverFrom())
      composeReference(t, "SupplyRequest", "deliverFrom", element.getDeliverFrom(), -1);
    if (element.hasDeliverTo())
      composeReference(t, "SupplyRequest", "deliverTo", element.getDeliverTo(), -1);
  }

  protected void composeSupplyRequestSupplyRequestParameterComponent(Complex parent, String parentType, String name, SupplyRequest.SupplyRequestParameterComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "parameter", name, element, index);
    if (element.hasCode())
      composeCodeableConcept(t, "SupplyRequest", "code", element.getCode(), -1);
    if (element.hasValue())
      composeType(t, "SupplyRequest", "value", element.getValue(), -1);
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
    if (element.hasInstantiatesCanonicalElement())
      composeCanonical(t, "Task", "instantiatesCanonical", element.getInstantiatesCanonicalElement(), -1);
    if (element.hasInstantiatesUriElement())
      composeUri(t, "Task", "instantiatesUri", element.getInstantiatesUriElement(), -1);
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
      composeReference(t, "Task", "requester", element.getRequester(), -1);
    for (int i = 0; i < element.getPerformerType().size(); i++)
      composeCodeableConcept(t, "Task", "performerType", element.getPerformerType().get(i), i);
    if (element.hasOwner())
      composeReference(t, "Task", "owner", element.getOwner(), -1);
    if (element.hasLocation())
      composeReference(t, "Task", "location", element.getLocation(), -1);
    if (element.hasReasonCode())
      composeCodeableConcept(t, "Task", "reasonCode", element.getReasonCode(), -1);
    if (element.hasReasonReference())
      composeReference(t, "Task", "reasonReference", element.getReasonReference(), -1);
    for (int i = 0; i < element.getInsurance().size(); i++)
      composeReference(t, "Task", "insurance", element.getInsurance().get(i), i);
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

  protected void composeTerminologyCapabilities(Complex parent, String parentType, String name, TerminologyCapabilities element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "TerminologyCapabilities", name, element, index);
    if (element.hasUrlElement())
      composeUri(t, "TerminologyCapabilities", "url", element.getUrlElement(), -1);
    if (element.hasVersionElement())
      composeString(t, "TerminologyCapabilities", "version", element.getVersionElement(), -1);
    if (element.hasNameElement())
      composeString(t, "TerminologyCapabilities", "name", element.getNameElement(), -1);
    if (element.hasTitleElement())
      composeString(t, "TerminologyCapabilities", "title", element.getTitleElement(), -1);
    if (element.hasStatusElement())
      composeEnum(t, "TerminologyCapabilities", "status", element.getStatusElement(), -1);
    if (element.hasExperimentalElement())
      composeBoolean(t, "TerminologyCapabilities", "experimental", element.getExperimentalElement(), -1);
    if (element.hasDateElement())
      composeDateTime(t, "TerminologyCapabilities", "date", element.getDateElement(), -1);
    if (element.hasPublisherElement())
      composeString(t, "TerminologyCapabilities", "publisher", element.getPublisherElement(), -1);
    for (int i = 0; i < element.getContact().size(); i++)
      composeContactDetail(t, "TerminologyCapabilities", "contact", element.getContact().get(i), i);
    if (element.hasDescriptionElement())
      composeMarkdown(t, "TerminologyCapabilities", "description", element.getDescriptionElement(), -1);
    for (int i = 0; i < element.getUseContext().size(); i++)
      composeUsageContext(t, "TerminologyCapabilities", "useContext", element.getUseContext().get(i), i);
    for (int i = 0; i < element.getJurisdiction().size(); i++)
      composeCodeableConcept(t, "TerminologyCapabilities", "jurisdiction", element.getJurisdiction().get(i), i);
    if (element.hasPurposeElement())
      composeMarkdown(t, "TerminologyCapabilities", "purpose", element.getPurposeElement(), -1);
    if (element.hasCopyrightElement())
      composeMarkdown(t, "TerminologyCapabilities", "copyright", element.getCopyrightElement(), -1);
    if (element.hasKindElement())
      composeEnum(t, "TerminologyCapabilities", "kind", element.getKindElement(), -1);
    if (element.hasSoftware())
      composeTerminologyCapabilitiesTerminologyCapabilitiesSoftwareComponent(t, "TerminologyCapabilities", "software", element.getSoftware(), -1);
    if (element.hasImplementation())
      composeTerminologyCapabilitiesTerminologyCapabilitiesImplementationComponent(t, "TerminologyCapabilities", "implementation", element.getImplementation(), -1);
    if (element.hasLockedDateElement())
      composeBoolean(t, "TerminologyCapabilities", "lockedDate", element.getLockedDateElement(), -1);
    for (int i = 0; i < element.getCodeSystem().size(); i++)
      composeTerminologyCapabilitiesTerminologyCapabilitiesCodeSystemComponent(t, "TerminologyCapabilities", "codeSystem", element.getCodeSystem().get(i), i);
    if (element.hasExpansion())
      composeTerminologyCapabilitiesTerminologyCapabilitiesExpansionComponent(t, "TerminologyCapabilities", "expansion", element.getExpansion(), -1);
    if (element.hasCodeSearchElement())
      composeEnum(t, "TerminologyCapabilities", "codeSearch", element.getCodeSearchElement(), -1);
    if (element.hasValidateCode())
      composeTerminologyCapabilitiesTerminologyCapabilitiesValidateCodeComponent(t, "TerminologyCapabilities", "validateCode", element.getValidateCode(), -1);
    if (element.hasTranslation())
      composeTerminologyCapabilitiesTerminologyCapabilitiesTranslationComponent(t, "TerminologyCapabilities", "translation", element.getTranslation(), -1);
    if (element.hasClosure())
      composeTerminologyCapabilitiesTerminologyCapabilitiesClosureComponent(t, "TerminologyCapabilities", "closure", element.getClosure(), -1);
  }

  protected void composeTerminologyCapabilitiesTerminologyCapabilitiesSoftwareComponent(Complex parent, String parentType, String name, TerminologyCapabilities.TerminologyCapabilitiesSoftwareComponent element, int index) {
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
      composeString(t, "TerminologyCapabilities", "name", element.getNameElement(), -1);
    if (element.hasVersionElement())
      composeString(t, "TerminologyCapabilities", "version", element.getVersionElement(), -1);
  }

  protected void composeTerminologyCapabilitiesTerminologyCapabilitiesImplementationComponent(Complex parent, String parentType, String name, TerminologyCapabilities.TerminologyCapabilitiesImplementationComponent element, int index) {
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
      composeString(t, "TerminologyCapabilities", "description", element.getDescriptionElement(), -1);
    if (element.hasUrlElement())
      composeUrl(t, "TerminologyCapabilities", "url", element.getUrlElement(), -1);
  }

  protected void composeTerminologyCapabilitiesTerminologyCapabilitiesCodeSystemComponent(Complex parent, String parentType, String name, TerminologyCapabilities.TerminologyCapabilitiesCodeSystemComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "codeSystem", name, element, index);
    if (element.hasUriElement())
      composeCanonical(t, "TerminologyCapabilities", "uri", element.getUriElement(), -1);
    for (int i = 0; i < element.getVersion().size(); i++)
      composeTerminologyCapabilitiesTerminologyCapabilitiesCodeSystemVersionComponent(t, "TerminologyCapabilities", "version", element.getVersion().get(i), i);
    if (element.hasSubsumptionElement())
      composeBoolean(t, "TerminologyCapabilities", "subsumption", element.getSubsumptionElement(), -1);
  }

  protected void composeTerminologyCapabilitiesTerminologyCapabilitiesCodeSystemVersionComponent(Complex parent, String parentType, String name, TerminologyCapabilities.TerminologyCapabilitiesCodeSystemVersionComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "version", name, element, index);
    if (element.hasCodeElement())
      composeString(t, "TerminologyCapabilities", "code", element.getCodeElement(), -1);
    if (element.hasIsDefaultElement())
      composeBoolean(t, "TerminologyCapabilities", "isDefault", element.getIsDefaultElement(), -1);
    if (element.hasCompositionalElement())
      composeBoolean(t, "TerminologyCapabilities", "compositional", element.getCompositionalElement(), -1);
    for (int i = 0; i < element.getLanguage().size(); i++)
      composeCode(t, "TerminologyCapabilities", "language", element.getLanguage().get(i), i);
    for (int i = 0; i < element.getFilter().size(); i++)
      composeTerminologyCapabilitiesTerminologyCapabilitiesCodeSystemVersionFilterComponent(t, "TerminologyCapabilities", "filter", element.getFilter().get(i), i);
    for (int i = 0; i < element.getProperty().size(); i++)
      composeCode(t, "TerminologyCapabilities", "property", element.getProperty().get(i), i);
  }

  protected void composeTerminologyCapabilitiesTerminologyCapabilitiesCodeSystemVersionFilterComponent(Complex parent, String parentType, String name, TerminologyCapabilities.TerminologyCapabilitiesCodeSystemVersionFilterComponent element, int index) {
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
      composeCode(t, "TerminologyCapabilities", "code", element.getCodeElement(), -1);
    for (int i = 0; i < element.getOp().size(); i++)
      composeCode(t, "TerminologyCapabilities", "op", element.getOp().get(i), i);
  }

  protected void composeTerminologyCapabilitiesTerminologyCapabilitiesExpansionComponent(Complex parent, String parentType, String name, TerminologyCapabilities.TerminologyCapabilitiesExpansionComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "expansion", name, element, index);
    if (element.hasHierarchicalElement())
      composeBoolean(t, "TerminologyCapabilities", "hierarchical", element.getHierarchicalElement(), -1);
    if (element.hasPagingElement())
      composeBoolean(t, "TerminologyCapabilities", "paging", element.getPagingElement(), -1);
    if (element.hasIncompleteElement())
      composeBoolean(t, "TerminologyCapabilities", "incomplete", element.getIncompleteElement(), -1);
    for (int i = 0; i < element.getParameter().size(); i++)
      composeTerminologyCapabilitiesTerminologyCapabilitiesExpansionParameterComponent(t, "TerminologyCapabilities", "parameter", element.getParameter().get(i), i);
    if (element.hasTextFilterElement())
      composeMarkdown(t, "TerminologyCapabilities", "textFilter", element.getTextFilterElement(), -1);
  }

  protected void composeTerminologyCapabilitiesTerminologyCapabilitiesExpansionParameterComponent(Complex parent, String parentType, String name, TerminologyCapabilities.TerminologyCapabilitiesExpansionParameterComponent element, int index) {
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
      composeCode(t, "TerminologyCapabilities", "name", element.getNameElement(), -1);
    if (element.hasDocumentationElement())
      composeString(t, "TerminologyCapabilities", "documentation", element.getDocumentationElement(), -1);
  }

  protected void composeTerminologyCapabilitiesTerminologyCapabilitiesValidateCodeComponent(Complex parent, String parentType, String name, TerminologyCapabilities.TerminologyCapabilitiesValidateCodeComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "validateCode", name, element, index);
    if (element.hasTranslationsElement())
      composeBoolean(t, "TerminologyCapabilities", "translations", element.getTranslationsElement(), -1);
  }

  protected void composeTerminologyCapabilitiesTerminologyCapabilitiesTranslationComponent(Complex parent, String parentType, String name, TerminologyCapabilities.TerminologyCapabilitiesTranslationComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "translation", name, element, index);
    if (element.hasNeedsMapElement())
      composeBoolean(t, "TerminologyCapabilities", "needsMap", element.getNeedsMapElement(), -1);
  }

  protected void composeTerminologyCapabilitiesTerminologyCapabilitiesClosureComponent(Complex parent, String parentType, String name, TerminologyCapabilities.TerminologyCapabilitiesClosureComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "closure", name, element, index);
    if (element.hasTranslationElement())
      composeBoolean(t, "TerminologyCapabilities", "translation", element.getTranslationElement(), -1);
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
    if (element.hasCapabilitiesElement())
      composeCanonical(t, "TestScript", "capabilities", element.getCapabilitiesElement(), -1);
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
      composeCode(t, "TestScript", "accept", element.getAcceptElement(), -1);
    if (element.hasContentTypeElement())
      composeCode(t, "TestScript", "contentType", element.getContentTypeElement(), -1);
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
      composeCode(t, "TestScript", "contentType", element.getContentTypeElement(), -1);
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

  protected void composeUserSession(Complex parent, String parentType, String name, UserSession element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "UserSession", name, element, index);
    if (element.hasIdentifier())
      composeIdentifier(t, "UserSession", "identifier", element.getIdentifier(), -1);
    if (element.hasUser())
      composeReference(t, "UserSession", "user", element.getUser(), -1);
    if (element.hasStatus())
      composeUserSessionUserSessionStatusComponent(t, "UserSession", "status", element.getStatus(), -1);
    if (element.hasWorkstation())
      composeIdentifier(t, "UserSession", "workstation", element.getWorkstation(), -1);
    for (int i = 0; i < element.getFocus().size(); i++)
      composeReference(t, "UserSession", "focus", element.getFocus().get(i), i);
    if (element.hasCreatedElement())
      composeInstant(t, "UserSession", "created", element.getCreatedElement(), -1);
    if (element.hasExpiresElement())
      composeInstant(t, "UserSession", "expires", element.getExpiresElement(), -1);
    for (int i = 0; i < element.getContext().size(); i++)
      composeUserSessionUserSessionContextComponent(t, "UserSession", "context", element.getContext().get(i), i);
  }

  protected void composeUserSessionUserSessionStatusComponent(Complex parent, String parentType, String name, UserSession.UserSessionStatusComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "status", name, element, index);
    if (element.hasCodeElement())
      composeEnum(t, "UserSession", "code", element.getCodeElement(), -1);
    if (element.hasSourceElement())
      composeEnum(t, "UserSession", "source", element.getSourceElement(), -1);
  }

  protected void composeUserSessionUserSessionContextComponent(Complex parent, String parentType, String name, UserSession.UserSessionContextComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "context", name, element, index);
    if (element.hasTypeElement())
      composeString(t, "UserSession", "type", element.getTypeElement(), -1);
    if (element.hasValue())
      composeType(t, "UserSession", "value", element.getValue(), -1);
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
      composeCanonical(t, "ValueSet", "valueSet", element.getValueSet().get(i), i);
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
      composeString(t, "ValueSet", "value", element.getValueElement(), -1);
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

  protected void composeVerificationResult(Complex parent, String parentType, String name, VerificationResult element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeDomainResource(t, "VerificationResult", name, element, index);
    for (int i = 0; i < element.getTarget().size(); i++)
      composeReference(t, "VerificationResult", "target", element.getTarget().get(i), i);
    for (int i = 0; i < element.getTargetLocation().size(); i++)
      composeString(t, "VerificationResult", "targetLocation", element.getTargetLocation().get(i), i);
    if (element.hasNeed())
      composeCodeableConcept(t, "VerificationResult", "need", element.getNeed(), -1);
    if (element.hasStatusElement())
      composeEnum(t, "VerificationResult", "status", element.getStatusElement(), -1);
    if (element.hasStatusDateElement())
      composeDateTime(t, "VerificationResult", "statusDate", element.getStatusDateElement(), -1);
    if (element.hasValidationType())
      composeCodeableConcept(t, "VerificationResult", "validationType", element.getValidationType(), -1);
    for (int i = 0; i < element.getValidationProcess().size(); i++)
      composeCodeableConcept(t, "VerificationResult", "validationProcess", element.getValidationProcess().get(i), i);
    if (element.hasFrequency())
      composeTiming(t, "VerificationResult", "frequency", element.getFrequency(), -1);
    if (element.hasLastPerformedElement())
      composeDateTime(t, "VerificationResult", "lastPerformed", element.getLastPerformedElement(), -1);
    if (element.hasNextScheduledElement())
      composeDate(t, "VerificationResult", "nextScheduled", element.getNextScheduledElement(), -1);
    if (element.hasFailureAction())
      composeCodeableConcept(t, "VerificationResult", "failureAction", element.getFailureAction(), -1);
    for (int i = 0; i < element.getPrimarySource().size(); i++)
      composeVerificationResultVerificationResultPrimarySourceComponent(t, "VerificationResult", "primarySource", element.getPrimarySource().get(i), i);
    if (element.hasAttestation())
      composeVerificationResultVerificationResultAttestationComponent(t, "VerificationResult", "attestation", element.getAttestation(), -1);
    for (int i = 0; i < element.getValidator().size(); i++)
      composeVerificationResultVerificationResultValidatorComponent(t, "VerificationResult", "validator", element.getValidator().get(i), i);
  }

  protected void composeVerificationResultVerificationResultPrimarySourceComponent(Complex parent, String parentType, String name, VerificationResult.VerificationResultPrimarySourceComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "primarySource", name, element, index);
    if (element.hasOrganization())
      composeReference(t, "VerificationResult", "organization", element.getOrganization(), -1);
    for (int i = 0; i < element.getType().size(); i++)
      composeCodeableConcept(t, "VerificationResult", "type", element.getType().get(i), i);
    for (int i = 0; i < element.getValidationProcess().size(); i++)
      composeCodeableConcept(t, "VerificationResult", "validationProcess", element.getValidationProcess().get(i), i);
    if (element.hasValidationStatus())
      composeCodeableConcept(t, "VerificationResult", "validationStatus", element.getValidationStatus(), -1);
    if (element.hasValidationDateElement())
      composeDateTime(t, "VerificationResult", "validationDate", element.getValidationDateElement(), -1);
    if (element.hasCanPushUpdates())
      composeCodeableConcept(t, "VerificationResult", "canPushUpdates", element.getCanPushUpdates(), -1);
    for (int i = 0; i < element.getPushTypeAvailable().size(); i++)
      composeCodeableConcept(t, "VerificationResult", "pushTypeAvailable", element.getPushTypeAvailable().get(i), i);
  }

  protected void composeVerificationResultVerificationResultAttestationComponent(Complex parent, String parentType, String name, VerificationResult.VerificationResultAttestationComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "attestation", name, element, index);
    if (element.hasSource())
      composeReference(t, "VerificationResult", "source", element.getSource(), -1);
    if (element.hasOrganization())
      composeReference(t, "VerificationResult", "organization", element.getOrganization(), -1);
    if (element.hasMethod())
      composeCodeableConcept(t, "VerificationResult", "method", element.getMethod(), -1);
    if (element.hasDateElement())
      composeDate(t, "VerificationResult", "date", element.getDateElement(), -1);
    if (element.hasSourceIdentityCertificateElement())
      composeString(t, "VerificationResult", "sourceIdentityCertificate", element.getSourceIdentityCertificateElement(), -1);
    if (element.hasProxyIdentityCertificateElement())
      composeString(t, "VerificationResult", "proxyIdentityCertificate", element.getProxyIdentityCertificateElement(), -1);
    if (element.hasSignedProxyRight())
      composeType(t, "VerificationResult", "signedProxyRight", element.getSignedProxyRight(), -1);
    if (element.hasSignedSourceAttestation())
      composeType(t, "VerificationResult", "signedSourceAttestation", element.getSignedSourceAttestation(), -1);
  }

  protected void composeVerificationResultVerificationResultValidatorComponent(Complex parent, String parentType, String name, VerificationResult.VerificationResultValidatorComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "validator", name, element, index);
    if (element.hasOrganization())
      composeReference(t, "VerificationResult", "organization", element.getOrganization(), -1);
    if (element.hasIdentityCertificateElement())
      composeString(t, "VerificationResult", "identityCertificate", element.getIdentityCertificateElement(), -1);
    if (element.hasSignedValidatorAttestation())
      composeType(t, "VerificationResult", "signedValidatorAttestation", element.getSignedValidatorAttestation(), -1);
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
    for (int i = 0; i < element.getPrism().size(); i++)
      composeVisionPrescriptionPrismComponent(t, "VisionPrescription", "prism", element.getPrism().get(i), i);
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

  protected void composeVisionPrescriptionPrismComponent(Complex parent, String parentType, String name, VisionPrescription.PrismComponent element, int index) {
    if (element == null) 
      return;
    Complex t;
    if (Utilities.noString(parentType))
      t = parent;
    else {
      t = parent.predicate("fhir:"+parentType+'.'+name);
    }
    composeBackboneElement(t, "prism", name, element, index);
    if (element.hasAmountElement())
      composeDecimal(t, "VisionPrescription", "amount", element.getAmountElement(), -1);
    if (element.hasBaseElement())
      composeEnum(t, "VisionPrescription", "base", element.getBaseElement(), -1);
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
    else if (resource instanceof BiologicallyDerivedProduct)
      composeBiologicallyDerivedProduct(parent, null, "BiologicallyDerivedProduct", (BiologicallyDerivedProduct)resource, -1);
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
    else if (resource instanceof ChargeItemDefinition)
      composeChargeItemDefinition(parent, null, "ChargeItemDefinition", (ChargeItemDefinition)resource, -1);
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
    else if (resource instanceof CoverageEligibilityRequest)
      composeCoverageEligibilityRequest(parent, null, "CoverageEligibilityRequest", (CoverageEligibilityRequest)resource, -1);
    else if (resource instanceof CoverageEligibilityResponse)
      composeCoverageEligibilityResponse(parent, null, "CoverageEligibilityResponse", (CoverageEligibilityResponse)resource, -1);
    else if (resource instanceof DetectedIssue)
      composeDetectedIssue(parent, null, "DetectedIssue", (DetectedIssue)resource, -1);
    else if (resource instanceof Device)
      composeDevice(parent, null, "Device", (Device)resource, -1);
    else if (resource instanceof DeviceDefinition)
      composeDeviceDefinition(parent, null, "DeviceDefinition", (DeviceDefinition)resource, -1);
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
    else if (resource instanceof ImagingStudy)
      composeImagingStudy(parent, null, "ImagingStudy", (ImagingStudy)resource, -1);
    else if (resource instanceof Immunization)
      composeImmunization(parent, null, "Immunization", (Immunization)resource, -1);
    else if (resource instanceof ImmunizationEvaluation)
      composeImmunizationEvaluation(parent, null, "ImmunizationEvaluation", (ImmunizationEvaluation)resource, -1);
    else if (resource instanceof ImmunizationRecommendation)
      composeImmunizationRecommendation(parent, null, "ImmunizationRecommendation", (ImmunizationRecommendation)resource, -1);
    else if (resource instanceof ImplementationGuide)
      composeImplementationGuide(parent, null, "ImplementationGuide", (ImplementationGuide)resource, -1);
    else if (resource instanceof InsurancePlan)
      composeInsurancePlan(parent, null, "InsurancePlan", (InsurancePlan)resource, -1);
    else if (resource instanceof Invoice)
      composeInvoice(parent, null, "Invoice", (Invoice)resource, -1);
    else if (resource instanceof ItemInstance)
      composeItemInstance(parent, null, "ItemInstance", (ItemInstance)resource, -1);
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
    else if (resource instanceof MedicationKnowledge)
      composeMedicationKnowledge(parent, null, "MedicationKnowledge", (MedicationKnowledge)resource, -1);
    else if (resource instanceof MedicationRequest)
      composeMedicationRequest(parent, null, "MedicationRequest", (MedicationRequest)resource, -1);
    else if (resource instanceof MedicationStatement)
      composeMedicationStatement(parent, null, "MedicationStatement", (MedicationStatement)resource, -1);
    else if (resource instanceof MedicinalProduct)
      composeMedicinalProduct(parent, null, "MedicinalProduct", (MedicinalProduct)resource, -1);
    else if (resource instanceof MedicinalProductAuthorization)
      composeMedicinalProductAuthorization(parent, null, "MedicinalProductAuthorization", (MedicinalProductAuthorization)resource, -1);
    else if (resource instanceof MedicinalProductClinicals)
      composeMedicinalProductClinicals(parent, null, "MedicinalProductClinicals", (MedicinalProductClinicals)resource, -1);
    else if (resource instanceof MedicinalProductContraindication)
      composeMedicinalProductContraindication(parent, null, "MedicinalProductContraindication", (MedicinalProductContraindication)resource, -1);
    else if (resource instanceof MedicinalProductDeviceSpec)
      composeMedicinalProductDeviceSpec(parent, null, "MedicinalProductDeviceSpec", (MedicinalProductDeviceSpec)resource, -1);
    else if (resource instanceof MedicinalProductIndication)
      composeMedicinalProductIndication(parent, null, "MedicinalProductIndication", (MedicinalProductIndication)resource, -1);
    else if (resource instanceof MedicinalProductIngredient)
      composeMedicinalProductIngredient(parent, null, "MedicinalProductIngredient", (MedicinalProductIngredient)resource, -1);
    else if (resource instanceof MedicinalProductInteraction)
      composeMedicinalProductInteraction(parent, null, "MedicinalProductInteraction", (MedicinalProductInteraction)resource, -1);
    else if (resource instanceof MedicinalProductManufactured)
      composeMedicinalProductManufactured(parent, null, "MedicinalProductManufactured", (MedicinalProductManufactured)resource, -1);
    else if (resource instanceof MedicinalProductPackaged)
      composeMedicinalProductPackaged(parent, null, "MedicinalProductPackaged", (MedicinalProductPackaged)resource, -1);
    else if (resource instanceof MedicinalProductPharmaceutical)
      composeMedicinalProductPharmaceutical(parent, null, "MedicinalProductPharmaceutical", (MedicinalProductPharmaceutical)resource, -1);
    else if (resource instanceof MedicinalProductUndesirableEffect)
      composeMedicinalProductUndesirableEffect(parent, null, "MedicinalProductUndesirableEffect", (MedicinalProductUndesirableEffect)resource, -1);
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
    else if (resource instanceof ObservationDefinition)
      composeObservationDefinition(parent, null, "ObservationDefinition", (ObservationDefinition)resource, -1);
    else if (resource instanceof OperationDefinition)
      composeOperationDefinition(parent, null, "OperationDefinition", (OperationDefinition)resource, -1);
    else if (resource instanceof OperationOutcome)
      composeOperationOutcome(parent, null, "OperationOutcome", (OperationOutcome)resource, -1);
    else if (resource instanceof Organization)
      composeOrganization(parent, null, "Organization", (Organization)resource, -1);
    else if (resource instanceof OrganizationAffiliation)
      composeOrganizationAffiliation(parent, null, "OrganizationAffiliation", (OrganizationAffiliation)resource, -1);
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
    else if (resource instanceof ServiceRequest)
      composeServiceRequest(parent, null, "ServiceRequest", (ServiceRequest)resource, -1);
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
    else if (resource instanceof SubstancePolymer)
      composeSubstancePolymer(parent, null, "SubstancePolymer", (SubstancePolymer)resource, -1);
    else if (resource instanceof SubstanceReferenceInformation)
      composeSubstanceReferenceInformation(parent, null, "SubstanceReferenceInformation", (SubstanceReferenceInformation)resource, -1);
    else if (resource instanceof SubstanceSpecification)
      composeSubstanceSpecification(parent, null, "SubstanceSpecification", (SubstanceSpecification)resource, -1);
    else if (resource instanceof SupplyDelivery)
      composeSupplyDelivery(parent, null, "SupplyDelivery", (SupplyDelivery)resource, -1);
    else if (resource instanceof SupplyRequest)
      composeSupplyRequest(parent, null, "SupplyRequest", (SupplyRequest)resource, -1);
    else if (resource instanceof Task)
      composeTask(parent, null, "Task", (Task)resource, -1);
    else if (resource instanceof TerminologyCapabilities)
      composeTerminologyCapabilities(parent, null, "TerminologyCapabilities", (TerminologyCapabilities)resource, -1);
    else if (resource instanceof TestReport)
      composeTestReport(parent, null, "TestReport", (TestReport)resource, -1);
    else if (resource instanceof TestScript)
      composeTestScript(parent, null, "TestScript", (TestScript)resource, -1);
    else if (resource instanceof UserSession)
      composeUserSession(parent, null, "UserSession", (UserSession)resource, -1);
    else if (resource instanceof ValueSet)
      composeValueSet(parent, null, "ValueSet", (ValueSet)resource, -1);
    else if (resource instanceof VerificationResult)
      composeVerificationResult(parent, null, "VerificationResult", (VerificationResult)resource, -1);
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
    else if (value instanceof CanonicalType)
      composeCanonical(parent, parentType, name, (CanonicalType)value, index);
    else if (value instanceof UriType)
      composeUri(parent, parentType, name, (UriType)value, index);
    else if (value instanceof UuidType)
      composeUuid(parent, parentType, name, (UuidType)value, index);
    else if (value instanceof UrlType)
      composeUrl(parent, parentType, name, (UrlType)value, index);
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
    else if (value instanceof Meta)
      composeMeta(parent, parentType, name, (Meta)value, index);
    else if (value instanceof Address)
      composeAddress(parent, parentType, name, (Address)value, index);
    else if (value instanceof Contributor)
      composeContributor(parent, parentType, name, (Contributor)value, index);
    else if (value instanceof Attachment)
      composeAttachment(parent, parentType, name, (Attachment)value, index);
    else if (value instanceof Count)
      composeCount(parent, parentType, name, (Count)value, index);
    else if (value instanceof DataRequirement)
      composeDataRequirement(parent, parentType, name, (DataRequirement)value, index);
    else if (value instanceof Dosage)
      composeDosage(parent, parentType, name, (Dosage)value, index);
    else if (value instanceof Money)
      composeMoney(parent, parentType, name, (Money)value, index);
    else if (value instanceof HumanName)
      composeHumanName(parent, parentType, name, (HumanName)value, index);
    else if (value instanceof ContactPoint)
      composeContactPoint(parent, parentType, name, (ContactPoint)value, index);
    else if (value instanceof Identifier)
      composeIdentifier(parent, parentType, name, (Identifier)value, index);
    else if (value instanceof Coding)
      composeCoding(parent, parentType, name, (Coding)value, index);
    else if (value instanceof SampledData)
      composeSampledData(parent, parentType, name, (SampledData)value, index);
    else if (value instanceof Ratio)
      composeRatio(parent, parentType, name, (Ratio)value, index);
    else if (value instanceof Distance)
      composeDistance(parent, parentType, name, (Distance)value, index);
    else if (value instanceof Age)
      composeAge(parent, parentType, name, (Age)value, index);
    else if (value instanceof Reference)
      composeReference(parent, parentType, name, (Reference)value, index);
    else if (value instanceof TriggerDefinition)
      composeTriggerDefinition(parent, parentType, name, (TriggerDefinition)value, index);
    else if (value instanceof Quantity)
      composeQuantity(parent, parentType, name, (Quantity)value, index);
    else if (value instanceof Period)
      composePeriod(parent, parentType, name, (Period)value, index);
    else if (value instanceof Duration)
      composeDuration(parent, parentType, name, (Duration)value, index);
    else if (value instanceof Range)
      composeRange(parent, parentType, name, (Range)value, index);
    else if (value instanceof RelatedArtifact)
      composeRelatedArtifact(parent, parentType, name, (RelatedArtifact)value, index);
    else if (value instanceof Annotation)
      composeAnnotation(parent, parentType, name, (Annotation)value, index);
    else if (value instanceof ContactDetail)
      composeContactDetail(parent, parentType, name, (ContactDetail)value, index);
    else if (value instanceof UsageContext)
      composeUsageContext(parent, parentType, name, (UsageContext)value, index);
    else if (value instanceof Expression)
      composeExpression(parent, parentType, name, (Expression)value, index);
    else if (value instanceof Signature)
      composeSignature(parent, parentType, name, (Signature)value, index);
    else if (value instanceof Timing)
      composeTiming(parent, parentType, name, (Timing)value, index);
    else if (value instanceof CodeableConcept)
      composeCodeableConcept(parent, parentType, name, (CodeableConcept)value, index);
    else if (value instanceof ParameterDefinition)
      composeParameterDefinition(parent, parentType, name, (ParameterDefinition)value, index);
    else
      throw new Error("Unhandled type");
  }

}

