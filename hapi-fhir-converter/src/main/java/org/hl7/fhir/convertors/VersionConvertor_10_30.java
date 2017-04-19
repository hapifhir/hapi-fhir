package org.hl7.fhir.convertors;

/*-
 * #%L
 * HAPI FHIR - Converter
 * %%
 * Copyright (C) 2014 - 2017 University Health Network
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


import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.hl7.fhir.instance.model.CodeableConcept;
import org.hl7.fhir.instance.model.Reference;
import org.hl7.fhir.instance.utils.ToolingExtensions;
import org.hl7.fhir.dstu3.conformance.ProfileUtilities;
import org.hl7.fhir.dstu3.model.Annotation;
import org.hl7.fhir.dstu3.model.CapabilityStatement.SystemRestfulInteraction;
import org.hl7.fhir.dstu3.model.CodeSystem;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.CodeSystem.CodeSystemContentMode;
import org.hl7.fhir.dstu3.model.CodeSystem.ConceptDefinitionComponent;
import org.hl7.fhir.dstu3.model.CodeSystem.ConceptDefinitionDesignationComponent;
import org.hl7.fhir.dstu3.model.CommunicationRequest.CommunicationPriority;
import org.hl7.fhir.dstu3.model.ConceptMap;
import org.hl7.fhir.dstu3.model.ContactDetail;
import org.hl7.fhir.dstu3.model.ConceptMap.ConceptMapGroupComponent;
import org.hl7.fhir.dstu3.model.ConceptMap.SourceElementComponent;
import org.hl7.fhir.dstu3.model.DocumentReference.ReferredDocumentStatus;
import org.hl7.fhir.dstu3.model.Dosage;
import org.hl7.fhir.dstu3.model.ElementDefinition;
import org.hl7.fhir.dstu3.model.ElementDefinition.ElementDefinitionSlicingDiscriminatorComponent;
import org.hl7.fhir.dstu3.model.Enumeration;
import org.hl7.fhir.dstu3.model.Immunization.ImmunizationPractitionerComponent;
import org.hl7.fhir.dstu3.model.ReferralRequest.ReferralPriority;
import org.hl7.fhir.dstu3.model.StructureDefinition.StructureDefinitionKind;
import org.hl7.fhir.dstu3.model.StructureDefinition.TypeDerivationRule;
import org.hl7.fhir.dstu3.model.Timing.EventTiming;
import org.hl7.fhir.dstu3.model.UriType;
import org.hl7.fhir.dstu3.terminologies.CodeSystemUtilities;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.utilities.Utilities;

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

// Generated on Thu, Apr 7, 2016 02:14+1000 for FHIR v1.4.0


public class VersionConvertor_10_30 {

  public VersionConvertorAdvisor advisor;

  public VersionConvertor_10_30(VersionConvertorAdvisor advisor) {
    super();
    this.advisor = advisor;
  }

  public void copyElement(org.hl7.fhir.instance.model.Element src, org.hl7.fhir.dstu3.model.Element tgt) throws FHIRException {
    tgt.setId(src.getId());
    for (org.hl7.fhir.instance.model.Extension  e : src.getExtension()) {
      tgt.addExtension(convertExtension(e));
    }
  }

  public void copyElement(org.hl7.fhir.dstu3.model.Element src, org.hl7.fhir.instance.model.Element tgt) throws FHIRException {
    tgt.setId(src.getId());
    for (org.hl7.fhir.dstu3.model.Extension  e : src.getExtension()) {
      tgt.addExtension(convertExtension(e));
    }
  }

  public void copyElement(org.hl7.fhir.dstu3.model.DomainResource src, org.hl7.fhir.instance.model.Element tgt) throws FHIRException {
    tgt.setId(src.getId());
    for (org.hl7.fhir.dstu3.model.Extension  e : src.getExtension()) {
      tgt.addExtension(convertExtension(e));
    }
  }

  public void copyBackboneElement(org.hl7.fhir.instance.model.BackboneElement src, org.hl7.fhir.dstu3.model.BackboneElement tgt) throws FHIRException {
    copyElement(src, tgt);
    for (org.hl7.fhir.instance.model.Extension  e : src.getModifierExtension()) {
      tgt.addModifierExtension(convertExtension(e));
    }
  }

  public void copyBackboneElement(org.hl7.fhir.dstu3.model.BackboneElement src, org.hl7.fhir.instance.model.BackboneElement tgt) throws FHIRException {
    copyElement(src, tgt);
    for (org.hl7.fhir.dstu3.model.Extension  e : src.getModifierExtension()) {
      tgt.addModifierExtension(convertExtension(e));
    }
  }

  public org.hl7.fhir.dstu3.model.Base64BinaryType convertBase64Binary(org.hl7.fhir.instance.model.Base64BinaryType src) throws FHIRException {
    org.hl7.fhir.dstu3.model.Base64BinaryType tgt = new org.hl7.fhir.dstu3.model.Base64BinaryType(src.getValue());
    copyElement(src, tgt);
    return tgt;
  }

  public org.hl7.fhir.instance.model.Base64BinaryType convertBase64Binary(org.hl7.fhir.dstu3.model.Base64BinaryType src) throws FHIRException {
    org.hl7.fhir.instance.model.Base64BinaryType tgt = new org.hl7.fhir.instance.model.Base64BinaryType(src.getValue());
    copyElement(src, tgt);
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.BooleanType convertBoolean(org.hl7.fhir.instance.model.BooleanType src) throws FHIRException {
    org.hl7.fhir.dstu3.model.BooleanType tgt = new org.hl7.fhir.dstu3.model.BooleanType(src.getValue());
    copyElement(src, tgt);
    return tgt;
  }

  public org.hl7.fhir.instance.model.BooleanType convertBoolean(org.hl7.fhir.dstu3.model.BooleanType src) throws FHIRException {
    org.hl7.fhir.instance.model.BooleanType tgt = new org.hl7.fhir.instance.model.BooleanType(src.getValue());
    copyElement(src, tgt);
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.CodeType convertCode(org.hl7.fhir.instance.model.CodeType src) throws FHIRException {
    org.hl7.fhir.dstu3.model.CodeType tgt = new org.hl7.fhir.dstu3.model.CodeType(src.getValue());
    copyElement(src, tgt);
    return tgt;
  }

  public org.hl7.fhir.instance.model.CodeType convertCode(org.hl7.fhir.dstu3.model.CodeType src) throws FHIRException {
    org.hl7.fhir.instance.model.CodeType tgt = new org.hl7.fhir.instance.model.CodeType(src.getValue());
    copyElement(src, tgt);
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.UriType convertCodeToUri(org.hl7.fhir.instance.model.CodeType src) throws FHIRException {
    org.hl7.fhir.dstu3.model.UriType tgt = new org.hl7.fhir.dstu3.model.UriType(src.getValue());
    copyElement(src, tgt);
    return tgt;
  }

  public org.hl7.fhir.instance.model.CodeType convertUriToCode(org.hl7.fhir.dstu3.model.UriType src) throws FHIRException {
    org.hl7.fhir.instance.model.CodeType tgt = new org.hl7.fhir.instance.model.CodeType(src.getValue());
    copyElement(src, tgt);
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.DateType convertDate(org.hl7.fhir.instance.model.DateType src) throws FHIRException {
    org.hl7.fhir.dstu3.model.DateType tgt = new org.hl7.fhir.dstu3.model.DateType(src.getValue());
    copyElement(src, tgt);
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.DateType convertDate(org.hl7.fhir.instance.model.DateTimeType src) throws FHIRException {
    org.hl7.fhir.dstu3.model.DateType tgt = new org.hl7.fhir.dstu3.model.DateType(src.getValue());
    copyElement(src, tgt);
    return tgt;
  }

  public org.hl7.fhir.instance.model.DateType convertDate(org.hl7.fhir.dstu3.model.DateType src) throws FHIRException {
    org.hl7.fhir.instance.model.DateType tgt = new org.hl7.fhir.instance.model.DateType(src.getValue());
    copyElement(src, tgt);
    return tgt;
  }

  public org.hl7.fhir.instance.model.DateType convertDate(org.hl7.fhir.dstu3.model.DateTimeType src) throws FHIRException {
    org.hl7.fhir.instance.model.DateType tgt = new org.hl7.fhir.instance.model.DateType(src.getValue());
    copyElement(src, tgt);
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.DateTimeType convertDateTime(org.hl7.fhir.instance.model.DateTimeType src) throws FHIRException {
    org.hl7.fhir.dstu3.model.DateTimeType tgt = new org.hl7.fhir.dstu3.model.DateTimeType(src.getValue());
    copyElement(src, tgt);
    return tgt;
  }

  public org.hl7.fhir.instance.model.DateTimeType convertDateTime(org.hl7.fhir.dstu3.model.DateTimeType src) throws FHIRException {
    org.hl7.fhir.instance.model.DateTimeType tgt = new org.hl7.fhir.instance.model.DateTimeType(src.getValue());
    copyElement(src, tgt);
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.DecimalType convertDecimal(org.hl7.fhir.instance.model.DecimalType src) throws FHIRException {
    org.hl7.fhir.dstu3.model.DecimalType tgt = new org.hl7.fhir.dstu3.model.DecimalType(src.getValue());
    copyElement(src, tgt);
    return tgt;
  }

  public org.hl7.fhir.instance.model.DecimalType convertDecimal(org.hl7.fhir.dstu3.model.DecimalType src) throws FHIRException {
    org.hl7.fhir.instance.model.DecimalType tgt = new org.hl7.fhir.instance.model.DecimalType(src.getValue());
    copyElement(src, tgt);
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.IdType convertId(org.hl7.fhir.instance.model.IdType src) throws FHIRException {
    org.hl7.fhir.dstu3.model.IdType tgt = new org.hl7.fhir.dstu3.model.IdType(src.getValue());
    copyElement(src, tgt);
    return tgt;
  }

  public org.hl7.fhir.instance.model.IdType convertId(org.hl7.fhir.dstu3.model.IdType src) throws FHIRException {
    org.hl7.fhir.instance.model.IdType tgt = new org.hl7.fhir.instance.model.IdType(src.getValue());
    copyElement(src, tgt);
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.InstantType convertInstant(org.hl7.fhir.instance.model.InstantType src) throws FHIRException {
    org.hl7.fhir.dstu3.model.InstantType tgt = new org.hl7.fhir.dstu3.model.InstantType(src.getValue());
    copyElement(src, tgt);
    return tgt;
  }

  public org.hl7.fhir.instance.model.InstantType convertInstant(org.hl7.fhir.dstu3.model.InstantType src) throws FHIRException {
    org.hl7.fhir.instance.model.InstantType tgt = new org.hl7.fhir.instance.model.InstantType(src.getValue());
    copyElement(src, tgt);
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.IntegerType convertInteger(org.hl7.fhir.instance.model.IntegerType src) throws FHIRException {
    org.hl7.fhir.dstu3.model.IntegerType tgt = new org.hl7.fhir.dstu3.model.IntegerType(src.getValue());
    copyElement(src, tgt);
    return tgt;
  }

  public org.hl7.fhir.instance.model.IntegerType convertInteger(org.hl7.fhir.dstu3.model.IntegerType src) throws FHIRException {
    org.hl7.fhir.instance.model.IntegerType tgt = new org.hl7.fhir.instance.model.IntegerType(src.getValue());
    copyElement(src, tgt);
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.MarkdownType convertMarkdown(org.hl7.fhir.instance.model.MarkdownType src) throws FHIRException {
    org.hl7.fhir.dstu3.model.MarkdownType tgt = new org.hl7.fhir.dstu3.model.MarkdownType(src.getValue());
    copyElement(src, tgt);
    return tgt;
  }

  public org.hl7.fhir.instance.model.MarkdownType convertMarkdown(org.hl7.fhir.dstu3.model.MarkdownType src) throws FHIRException {
    org.hl7.fhir.instance.model.MarkdownType tgt = new org.hl7.fhir.instance.model.MarkdownType(src.getValue());
    copyElement(src, tgt);
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.OidType convertOid(org.hl7.fhir.instance.model.OidType src) throws FHIRException {
    org.hl7.fhir.dstu3.model.OidType tgt = new org.hl7.fhir.dstu3.model.OidType(src.getValue());
    copyElement(src, tgt);
    return tgt;
  }

  public org.hl7.fhir.instance.model.OidType convertOid(org.hl7.fhir.dstu3.model.OidType src) throws FHIRException {
    org.hl7.fhir.instance.model.OidType tgt = new org.hl7.fhir.instance.model.OidType(src.getValue());
    copyElement(src, tgt);
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.PositiveIntType convertPositiveInt(org.hl7.fhir.instance.model.PositiveIntType src) throws FHIRException {
    org.hl7.fhir.dstu3.model.PositiveIntType tgt = new org.hl7.fhir.dstu3.model.PositiveIntType(src.getValue());
    copyElement(src, tgt);
    return tgt;
  }

  public org.hl7.fhir.instance.model.PositiveIntType convertPositiveInt(org.hl7.fhir.dstu3.model.PositiveIntType src) throws FHIRException {
    org.hl7.fhir.instance.model.PositiveIntType tgt = new org.hl7.fhir.instance.model.PositiveIntType(src.getValue());
    copyElement(src, tgt);
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.StringType convertString(org.hl7.fhir.instance.model.StringType src) throws FHIRException {
    org.hl7.fhir.dstu3.model.StringType tgt = new org.hl7.fhir.dstu3.model.StringType(src.getValue());
    copyElement(src, tgt);
    return tgt;
  }

  public org.hl7.fhir.instance.model.StringType convertString(org.hl7.fhir.dstu3.model.StringType src) throws FHIRException {
    org.hl7.fhir.instance.model.StringType tgt = new org.hl7.fhir.instance.model.StringType(src.getValue());
    copyElement(src, tgt);
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.TimeType convertTime(org.hl7.fhir.instance.model.TimeType src) throws FHIRException {
    org.hl7.fhir.dstu3.model.TimeType tgt = new org.hl7.fhir.dstu3.model.TimeType(src.getValue());
    copyElement(src, tgt);
    return tgt;
  }

  public org.hl7.fhir.instance.model.TimeType convertTime(org.hl7.fhir.dstu3.model.TimeType src) throws FHIRException {
    org.hl7.fhir.instance.model.TimeType tgt = new org.hl7.fhir.instance.model.TimeType(src.getValue());
    copyElement(src, tgt);
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.UnsignedIntType convertUnsignedInt(org.hl7.fhir.instance.model.UnsignedIntType src) throws FHIRException {
    org.hl7.fhir.dstu3.model.UnsignedIntType tgt = new org.hl7.fhir.dstu3.model.UnsignedIntType(src.getValue());
    copyElement(src, tgt);
    return tgt;
  }

  public org.hl7.fhir.instance.model.UnsignedIntType convertUnsignedInt(org.hl7.fhir.dstu3.model.UnsignedIntType src) throws FHIRException {
    org.hl7.fhir.instance.model.UnsignedIntType tgt = new org.hl7.fhir.instance.model.UnsignedIntType(src.getValue());
    copyElement(src, tgt);
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.UriType convertUri(org.hl7.fhir.instance.model.UriType src) throws FHIRException {
    org.hl7.fhir.dstu3.model.UriType tgt = new org.hl7.fhir.dstu3.model.UriType(src.getValue());
    copyElement(src, tgt);
    return tgt;
  }

  public org.hl7.fhir.instance.model.UriType convertUri(org.hl7.fhir.dstu3.model.UriType src) throws FHIRException {
    org.hl7.fhir.instance.model.UriType tgt = new org.hl7.fhir.instance.model.UriType(src.getValue());
    copyElement(src, tgt);
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.UuidType convertUuid(org.hl7.fhir.instance.model.UuidType src) throws FHIRException {
    org.hl7.fhir.dstu3.model.UuidType tgt = new org.hl7.fhir.dstu3.model.UuidType(src.getValue());
    copyElement(src, tgt);
    return tgt;
  }

  public org.hl7.fhir.instance.model.UuidType convertUuid(org.hl7.fhir.dstu3.model.UuidType src) throws FHIRException {
    org.hl7.fhir.instance.model.UuidType tgt = new org.hl7.fhir.instance.model.UuidType(src.getValue());
    copyElement(src, tgt);
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.Extension convertExtension(org.hl7.fhir.instance.model.Extension src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.Extension tgt = new org.hl7.fhir.dstu3.model.Extension();
    copyElement(src, tgt);
    tgt.setUrl(src.getUrl());
    tgt.setValue(convertType(src.getValue()));
    return tgt;
  }

  public org.hl7.fhir.instance.model.Extension convertExtension(org.hl7.fhir.dstu3.model.Extension src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.Extension tgt = new org.hl7.fhir.instance.model.Extension();
    copyElement(src, tgt);
    tgt.setUrl(src.getUrl());
    tgt.setValue(convertType(src.getValue()));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.Narrative convertNarrative(org.hl7.fhir.instance.model.Narrative src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.Narrative tgt = new org.hl7.fhir.dstu3.model.Narrative();
    copyElement(src, tgt);
    tgt.setStatus(convertNarrativeStatus(src.getStatus()));
    tgt.setDiv(src.getDiv());
    return tgt;
  }

  public org.hl7.fhir.instance.model.Narrative convertNarrative(org.hl7.fhir.dstu3.model.Narrative src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.Narrative tgt = new org.hl7.fhir.instance.model.Narrative();
    copyElement(src, tgt);
    tgt.setStatus(convertNarrativeStatus(src.getStatus()));
    tgt.setDiv(src.getDiv());
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.Narrative.NarrativeStatus convertNarrativeStatus(org.hl7.fhir.instance.model.Narrative.NarrativeStatus src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case GENERATED: return org.hl7.fhir.dstu3.model.Narrative.NarrativeStatus.GENERATED;
    case EXTENSIONS: return org.hl7.fhir.dstu3.model.Narrative.NarrativeStatus.EXTENSIONS;
    case ADDITIONAL: return org.hl7.fhir.dstu3.model.Narrative.NarrativeStatus.ADDITIONAL;
    case EMPTY: return org.hl7.fhir.dstu3.model.Narrative.NarrativeStatus.EMPTY;
    default: return org.hl7.fhir.dstu3.model.Narrative.NarrativeStatus.NULL;
    }
  }

  public org.hl7.fhir.instance.model.Narrative.NarrativeStatus convertNarrativeStatus(org.hl7.fhir.dstu3.model.Narrative.NarrativeStatus src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case GENERATED: return org.hl7.fhir.instance.model.Narrative.NarrativeStatus.GENERATED;
    case EXTENSIONS: return org.hl7.fhir.instance.model.Narrative.NarrativeStatus.EXTENSIONS;
    case ADDITIONAL: return org.hl7.fhir.instance.model.Narrative.NarrativeStatus.ADDITIONAL;
    case EMPTY: return org.hl7.fhir.instance.model.Narrative.NarrativeStatus.EMPTY;
    default: return org.hl7.fhir.instance.model.Narrative.NarrativeStatus.NULL;
    }
  }

  public org.hl7.fhir.dstu3.model.Annotation convertAnnotation(org.hl7.fhir.instance.model.Annotation src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.Annotation tgt = new org.hl7.fhir.dstu3.model.Annotation();
    copyElement(src, tgt);
    tgt.setAuthor(convertType(src.getAuthor()));
    tgt.setTime(src.getTime());
    tgt.setText(src.getText());
    return tgt;
  }

  public org.hl7.fhir.instance.model.Annotation convertAnnotation(org.hl7.fhir.dstu3.model.Annotation src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.Annotation tgt = new org.hl7.fhir.instance.model.Annotation();
    copyElement(src, tgt);
    tgt.setAuthor(convertType(src.getAuthor()));
    tgt.setTime(src.getTime());
    tgt.setText(src.getText());
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.Attachment convertAttachment(org.hl7.fhir.instance.model.Attachment src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.Attachment tgt = new org.hl7.fhir.dstu3.model.Attachment();
    copyElement(src, tgt);
    tgt.setContentType(src.getContentType());
    tgt.setLanguage(src.getLanguage());
    tgt.setData(src.getData());
    tgt.setUrl(src.getUrl());
    tgt.setSize(src.getSize());
    tgt.setHash(src.getHash());
    tgt.setTitle(src.getTitle());
    tgt.setCreation(src.getCreation());
    return tgt;
  }

  public org.hl7.fhir.instance.model.Attachment convertAttachment(org.hl7.fhir.dstu3.model.Attachment src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.Attachment tgt = new org.hl7.fhir.instance.model.Attachment();
    copyElement(src, tgt);
    tgt.setContentType(src.getContentType());
    tgt.setLanguage(src.getLanguage());
    tgt.setData(src.getData());
    tgt.setUrl(src.getUrl());
    tgt.setSize(src.getSize());
    tgt.setHash(src.getHash());
    tgt.setTitle(src.getTitle());
    tgt.setCreation(src.getCreation());
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.CodeableConcept convertCodeableConcept(org.hl7.fhir.instance.model.CodeableConcept src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.CodeableConcept tgt = new org.hl7.fhir.dstu3.model.CodeableConcept();
    copyElement(src, tgt);
    for (org.hl7.fhir.instance.model.Coding t : src.getCoding())
      tgt.addCoding(convertCoding(t));
    tgt.setText(src.getText());
    return tgt;
  }

  public org.hl7.fhir.instance.model.CodeableConcept convertCodeableConcept(org.hl7.fhir.dstu3.model.CodeableConcept src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.CodeableConcept tgt = new org.hl7.fhir.instance.model.CodeableConcept();
    copyElement(src, tgt);
    for (org.hl7.fhir.dstu3.model.Coding t : src.getCoding())
      tgt.addCoding(convertCoding(t));
    tgt.setText(src.getText());
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.Coding convertCoding(org.hl7.fhir.instance.model.Coding src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.Coding tgt = new org.hl7.fhir.dstu3.model.Coding();
    copyElement(src, tgt);
    tgt.setSystem(src.getSystem());
    tgt.setVersion(src.getVersion());
    tgt.setCode(src.getCode());
    tgt.setDisplay(src.getDisplay());
    tgt.setUserSelected(src.getUserSelected());
    return tgt;
  }

  public org.hl7.fhir.instance.model.Coding convertCoding(org.hl7.fhir.dstu3.model.Coding src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.Coding tgt = new org.hl7.fhir.instance.model.Coding();
    copyElement(src, tgt);
    tgt.setSystem(src.getSystem());
    tgt.setVersion(src.getVersion());
    tgt.setCode(src.getCode());
    tgt.setDisplay(src.getDisplay());
    tgt.setUserSelected(src.getUserSelected());
    return tgt;
  }



  public org.hl7.fhir.dstu3.model.Identifier convertIdentifier(org.hl7.fhir.instance.model.Identifier src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.Identifier tgt = new org.hl7.fhir.dstu3.model.Identifier();
    copyElement(src, tgt);
    tgt.setUse(convertIdentifierUse(src.getUse()));
    tgt.setType(convertCodeableConcept(src.getType()));
    tgt.setSystem(src.getSystem());
    tgt.setValue(src.getValue());
    tgt.setPeriod(convertPeriod(src.getPeriod()));
    tgt.setAssigner(convertReference(src.getAssigner()));
    return tgt;
  }

  public org.hl7.fhir.instance.model.Identifier convertIdentifier(org.hl7.fhir.dstu3.model.Identifier src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.Identifier tgt = new org.hl7.fhir.instance.model.Identifier();
    copyElement(src, tgt);
    if (src.hasUse())
      tgt.setUse(convertIdentifierUse(src.getUse()));
    if (src.hasType())
      tgt.setType(convertCodeableConcept(src.getType()));
    if (src.hasSystem())
      tgt.setSystem(src.getSystem());
    if (src.hasValue())
      tgt.setValue(src.getValue());
    if (src.hasPeriod())
      tgt.setPeriod(convertPeriod(src.getPeriod()));
    if (src.hasAssigner())
      tgt.setAssigner(convertReference(src.getAssigner()));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.Identifier.IdentifierUse convertIdentifierUse(org.hl7.fhir.instance.model.Identifier.IdentifierUse src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case USUAL: return org.hl7.fhir.dstu3.model.Identifier.IdentifierUse.USUAL;
    case OFFICIAL: return org.hl7.fhir.dstu3.model.Identifier.IdentifierUse.OFFICIAL;
    case TEMP: return org.hl7.fhir.dstu3.model.Identifier.IdentifierUse.TEMP;
    case SECONDARY: return org.hl7.fhir.dstu3.model.Identifier.IdentifierUse.SECONDARY;
    default: return org.hl7.fhir.dstu3.model.Identifier.IdentifierUse.NULL;
    }
  }

  public org.hl7.fhir.instance.model.Identifier.IdentifierUse convertIdentifierUse(org.hl7.fhir.dstu3.model.Identifier.IdentifierUse src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case USUAL: return org.hl7.fhir.instance.model.Identifier.IdentifierUse.USUAL;
    case OFFICIAL: return org.hl7.fhir.instance.model.Identifier.IdentifierUse.OFFICIAL;
    case TEMP: return org.hl7.fhir.instance.model.Identifier.IdentifierUse.TEMP;
    case SECONDARY: return org.hl7.fhir.instance.model.Identifier.IdentifierUse.SECONDARY;
    default: return org.hl7.fhir.instance.model.Identifier.IdentifierUse.NULL;
    }
  }

  public org.hl7.fhir.dstu3.model.Period convertPeriod(org.hl7.fhir.instance.model.Period src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.Period tgt = new org.hl7.fhir.dstu3.model.Period();
    copyElement(src, tgt);
    tgt.setStart(src.getStart());
    tgt.setEnd(src.getEnd());
    return tgt;
  }

  public org.hl7.fhir.instance.model.Period convertPeriod(org.hl7.fhir.dstu3.model.Period src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.Period tgt = new org.hl7.fhir.instance.model.Period();
    copyElement(src, tgt);
    tgt.setStart(src.getStart());
    tgt.setEnd(src.getEnd());
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.Quantity convertQuantity(org.hl7.fhir.instance.model.Quantity src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.Quantity tgt = new org.hl7.fhir.dstu3.model.Quantity();
    copyElement(src, tgt);
    tgt.setValue(src.getValue());
    tgt.setComparator(convertQuantityComparator(src.getComparator()));
    tgt.setUnit(src.getUnit());
    tgt.setSystem(src.getSystem());
    tgt.setCode(src.getCode());
    return tgt;
  }

  public org.hl7.fhir.instance.model.Quantity convertQuantity(org.hl7.fhir.dstu3.model.Quantity src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.Quantity tgt = new org.hl7.fhir.instance.model.Quantity();
    copyElement(src, tgt);
    tgt.setValue(src.getValue());
    tgt.setComparator(convertQuantityComparator(src.getComparator()));
    tgt.setUnit(src.getUnit());
    tgt.setSystem(src.getSystem());
    tgt.setCode(src.getCode());
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.Quantity.QuantityComparator convertQuantityComparator(org.hl7.fhir.instance.model.Quantity.QuantityComparator src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case LESS_THAN: return org.hl7.fhir.dstu3.model.Quantity.QuantityComparator.LESS_THAN;
    case LESS_OR_EQUAL: return org.hl7.fhir.dstu3.model.Quantity.QuantityComparator.LESS_OR_EQUAL;
    case GREATER_OR_EQUAL: return org.hl7.fhir.dstu3.model.Quantity.QuantityComparator.GREATER_OR_EQUAL;
    case GREATER_THAN: return org.hl7.fhir.dstu3.model.Quantity.QuantityComparator.GREATER_THAN;
    default: return org.hl7.fhir.dstu3.model.Quantity.QuantityComparator.NULL;
    }
  }

  public org.hl7.fhir.instance.model.Quantity.QuantityComparator convertQuantityComparator(org.hl7.fhir.dstu3.model.Quantity.QuantityComparator src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case LESS_THAN: return org.hl7.fhir.instance.model.Quantity.QuantityComparator.LESS_THAN;
    case LESS_OR_EQUAL: return org.hl7.fhir.instance.model.Quantity.QuantityComparator.LESS_OR_EQUAL;
    case GREATER_OR_EQUAL: return org.hl7.fhir.instance.model.Quantity.QuantityComparator.GREATER_OR_EQUAL;
    case GREATER_THAN: return org.hl7.fhir.instance.model.Quantity.QuantityComparator.GREATER_THAN;
    default: return org.hl7.fhir.instance.model.Quantity.QuantityComparator.NULL;
    }
  }

  public org.hl7.fhir.dstu3.model.Range convertRange(org.hl7.fhir.instance.model.Range src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.Range tgt = new org.hl7.fhir.dstu3.model.Range();
    copyElement(src, tgt);
    tgt.setLow(convertSimpleQuantity(src.getLow()));
    tgt.setHigh(convertSimpleQuantity(src.getHigh()));
    return tgt;
  }

  public org.hl7.fhir.instance.model.Range convertRange(org.hl7.fhir.dstu3.model.Range src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.Range tgt = new org.hl7.fhir.instance.model.Range();
    copyElement(src, tgt);
    tgt.setLow(convertSimpleQuantity(src.getLow()));
    tgt.setHigh(convertSimpleQuantity(src.getHigh()));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.Ratio convertRatio(org.hl7.fhir.instance.model.Ratio src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.Ratio tgt = new org.hl7.fhir.dstu3.model.Ratio();
    copyElement(src, tgt);
    tgt.setNumerator(convertQuantity(src.getNumerator()));
    tgt.setDenominator(convertQuantity(src.getDenominator()));
    return tgt;
  }

  public org.hl7.fhir.instance.model.Ratio convertRatio(org.hl7.fhir.dstu3.model.Ratio src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.Ratio tgt = new org.hl7.fhir.instance.model.Ratio();
    copyElement(src, tgt);
    tgt.setNumerator(convertQuantity(src.getNumerator()));
    tgt.setDenominator(convertQuantity(src.getDenominator()));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.Reference convertReference(org.hl7.fhir.instance.model.Reference src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.Reference tgt = new org.hl7.fhir.dstu3.model.Reference();
    copyElement(src, tgt);
    tgt.setReference(src.getReference());
    tgt.setDisplay(src.getDisplay());
    return tgt;
  }

  public org.hl7.fhir.instance.model.Reference convertReference(org.hl7.fhir.dstu3.model.Reference src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.Reference tgt = new org.hl7.fhir.instance.model.Reference();
    copyElement(src, tgt);
    tgt.setReference(src.getReference());
    tgt.setDisplay(src.getDisplay());
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.SampledData convertSampledData(org.hl7.fhir.instance.model.SampledData src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.SampledData tgt = new org.hl7.fhir.dstu3.model.SampledData();
    copyElement(src, tgt);
    tgt.setOrigin(convertSimpleQuantity(src.getOrigin()));
    tgt.setPeriod(src.getPeriod());
    tgt.setFactor(src.getFactor());
    tgt.setLowerLimit(src.getLowerLimit());
    tgt.setUpperLimit(src.getUpperLimit());
    tgt.setDimensions(src.getDimensions());
    tgt.setData(src.getData());
    return tgt;
  }

  public org.hl7.fhir.instance.model.SampledData convertSampledData(org.hl7.fhir.dstu3.model.SampledData src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.SampledData tgt = new org.hl7.fhir.instance.model.SampledData();
    copyElement(src, tgt);
    tgt.setOrigin(convertSimpleQuantity(src.getOrigin()));
    tgt.setPeriod(src.getPeriod());
    tgt.setFactor(src.getFactor());
    tgt.setLowerLimit(src.getLowerLimit());
    tgt.setUpperLimit(src.getUpperLimit());
    tgt.setDimensions(src.getDimensions());
    tgt.setData(src.getData());
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.Signature convertSignature(org.hl7.fhir.instance.model.Signature src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.Signature tgt = new org.hl7.fhir.dstu3.model.Signature();
    copyElement(src, tgt);
    for (org.hl7.fhir.instance.model.Coding t : src.getType())
      tgt.addType(convertCoding(t));
    tgt.setWhen(src.getWhen());
    tgt.setWho(convertType(src.getWho()));
    tgt.setContentType(src.getContentType());
    tgt.setBlob(src.getBlob());
    return tgt;
  }

  public org.hl7.fhir.instance.model.Signature convertSignature(org.hl7.fhir.dstu3.model.Signature src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.Signature tgt = new org.hl7.fhir.instance.model.Signature();
    copyElement(src, tgt);
    for (org.hl7.fhir.dstu3.model.Coding t : src.getType())
      tgt.addType(convertCoding(t));
    tgt.setWhen(src.getWhen());
    tgt.setWho(convertType(src.getWho()));
    tgt.setContentType(src.getContentType());
    tgt.setBlob(src.getBlob());
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.Address convertAddress(org.hl7.fhir.instance.model.Address src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.Address tgt = new org.hl7.fhir.dstu3.model.Address();
    copyElement(src, tgt);
    tgt.setUse(convertAddressUse(src.getUse()));
    tgt.setType(convertAddressType(src.getType()));
    tgt.setText(src.getText());
    for (org.hl7.fhir.instance.model.StringType t : src.getLine())
      tgt.addLine(t.getValue());
    tgt.setCity(src.getCity());
    tgt.setDistrict(src.getDistrict());
    tgt.setState(src.getState());
    tgt.setPostalCode(src.getPostalCode());
    tgt.setCountry(src.getCountry());
    tgt.setPeriod(convertPeriod(src.getPeriod()));
    return tgt;
  }

  public org.hl7.fhir.instance.model.Address convertAddress(org.hl7.fhir.dstu3.model.Address src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.Address tgt = new org.hl7.fhir.instance.model.Address();
    copyElement(src, tgt);
    tgt.setUse(convertAddressUse(src.getUse()));
    tgt.setType(convertAddressType(src.getType()));
    tgt.setText(src.getText());
    for (org.hl7.fhir.dstu3.model.StringType t : src.getLine())
      tgt.addLine(t.getValue());
    tgt.setCity(src.getCity());
    tgt.setDistrict(src.getDistrict());
    tgt.setState(src.getState());
    tgt.setPostalCode(src.getPostalCode());
    tgt.setCountry(src.getCountry());
    tgt.setPeriod(convertPeriod(src.getPeriod()));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.Address.AddressUse convertAddressUse(org.hl7.fhir.instance.model.Address.AddressUse src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case HOME: return org.hl7.fhir.dstu3.model.Address.AddressUse.HOME;
    case WORK: return org.hl7.fhir.dstu3.model.Address.AddressUse.WORK;
    case TEMP: return org.hl7.fhir.dstu3.model.Address.AddressUse.TEMP;
    case OLD: return org.hl7.fhir.dstu3.model.Address.AddressUse.OLD;
    default: return org.hl7.fhir.dstu3.model.Address.AddressUse.NULL;
    }
  }

  public org.hl7.fhir.instance.model.Address.AddressUse convertAddressUse(org.hl7.fhir.dstu3.model.Address.AddressUse src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case HOME: return org.hl7.fhir.instance.model.Address.AddressUse.HOME;
    case WORK: return org.hl7.fhir.instance.model.Address.AddressUse.WORK;
    case TEMP: return org.hl7.fhir.instance.model.Address.AddressUse.TEMP;
    case OLD: return org.hl7.fhir.instance.model.Address.AddressUse.OLD;
    default: return org.hl7.fhir.instance.model.Address.AddressUse.NULL;
    }
  }

  public org.hl7.fhir.dstu3.model.Address.AddressType convertAddressType(org.hl7.fhir.instance.model.Address.AddressType src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case POSTAL: return org.hl7.fhir.dstu3.model.Address.AddressType.POSTAL;
    case PHYSICAL: return org.hl7.fhir.dstu3.model.Address.AddressType.PHYSICAL;
    case BOTH: return org.hl7.fhir.dstu3.model.Address.AddressType.BOTH;
    default: return org.hl7.fhir.dstu3.model.Address.AddressType.NULL;
    }
  }

  public org.hl7.fhir.instance.model.Address.AddressType convertAddressType(org.hl7.fhir.dstu3.model.Address.AddressType src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case POSTAL: return org.hl7.fhir.instance.model.Address.AddressType.POSTAL;
    case PHYSICAL: return org.hl7.fhir.instance.model.Address.AddressType.PHYSICAL;
    case BOTH: return org.hl7.fhir.instance.model.Address.AddressType.BOTH;
    default: return org.hl7.fhir.instance.model.Address.AddressType.NULL;
    }
  }

  public org.hl7.fhir.dstu3.model.ContactPoint convertContactPoint(org.hl7.fhir.instance.model.ContactPoint src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.ContactPoint tgt = new org.hl7.fhir.dstu3.model.ContactPoint();
    copyElement(src, tgt);
    tgt.setSystem(convertContactPointSystem(src.getSystem()));
    tgt.setValue(src.getValue());
    tgt.setUse(convertContactPointUse(src.getUse()));
    if (src.hasRank())
      tgt.setRank(src.getRank());
    tgt.setPeriod(convertPeriod(src.getPeriod()));
    return tgt;
  }

  public org.hl7.fhir.instance.model.ContactPoint convertContactPoint(org.hl7.fhir.dstu3.model.ContactPoint src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.ContactPoint tgt = new org.hl7.fhir.instance.model.ContactPoint();
    copyElement(src, tgt);
    if (src.hasSystem())
      tgt.setSystem(convertContactPointSystem(src.getSystem()));
    tgt.setValue(src.getValue());
    tgt.setUse(convertContactPointUse(src.getUse()));
    tgt.setRank(src.getRank());
    if (src.hasPeriod())
      tgt.setPeriod(convertPeriod(src.getPeriod()));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.ContactPoint.ContactPointSystem convertContactPointSystem(org.hl7.fhir.instance.model.ContactPoint.ContactPointSystem src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case PHONE: return org.hl7.fhir.dstu3.model.ContactPoint.ContactPointSystem.PHONE;
    case FAX: return org.hl7.fhir.dstu3.model.ContactPoint.ContactPointSystem.FAX;
    case EMAIL: return org.hl7.fhir.dstu3.model.ContactPoint.ContactPointSystem.EMAIL;
    case PAGER: return org.hl7.fhir.dstu3.model.ContactPoint.ContactPointSystem.PAGER;
    case OTHER: return org.hl7.fhir.dstu3.model.ContactPoint.ContactPointSystem.OTHER;
    default: return org.hl7.fhir.dstu3.model.ContactPoint.ContactPointSystem.NULL;
    }
  }

  public org.hl7.fhir.instance.model.ContactPoint.ContactPointSystem convertContactPointSystem(org.hl7.fhir.dstu3.model.ContactPoint.ContactPointSystem src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case PHONE: return org.hl7.fhir.instance.model.ContactPoint.ContactPointSystem.PHONE;
    case FAX: return org.hl7.fhir.instance.model.ContactPoint.ContactPointSystem.FAX;
    case EMAIL: return org.hl7.fhir.instance.model.ContactPoint.ContactPointSystem.EMAIL;
    case PAGER: return org.hl7.fhir.instance.model.ContactPoint.ContactPointSystem.PAGER;
    case OTHER: return org.hl7.fhir.instance.model.ContactPoint.ContactPointSystem.OTHER;
    case URL: return org.hl7.fhir.instance.model.ContactPoint.ContactPointSystem.OTHER;
    default: return org.hl7.fhir.instance.model.ContactPoint.ContactPointSystem.NULL;
    }
  }

  public org.hl7.fhir.dstu3.model.ContactPoint.ContactPointUse convertContactPointUse(org.hl7.fhir.instance.model.ContactPoint.ContactPointUse src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case HOME: return org.hl7.fhir.dstu3.model.ContactPoint.ContactPointUse.HOME;
    case WORK: return org.hl7.fhir.dstu3.model.ContactPoint.ContactPointUse.WORK;
    case TEMP: return org.hl7.fhir.dstu3.model.ContactPoint.ContactPointUse.TEMP;
    case OLD: return org.hl7.fhir.dstu3.model.ContactPoint.ContactPointUse.OLD;
    case MOBILE: return org.hl7.fhir.dstu3.model.ContactPoint.ContactPointUse.MOBILE;
    default: return org.hl7.fhir.dstu3.model.ContactPoint.ContactPointUse.NULL;
    }
  }

  public org.hl7.fhir.instance.model.ContactPoint.ContactPointUse convertContactPointUse(org.hl7.fhir.dstu3.model.ContactPoint.ContactPointUse src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case HOME: return org.hl7.fhir.instance.model.ContactPoint.ContactPointUse.HOME;
    case WORK: return org.hl7.fhir.instance.model.ContactPoint.ContactPointUse.WORK;
    case TEMP: return org.hl7.fhir.instance.model.ContactPoint.ContactPointUse.TEMP;
    case OLD: return org.hl7.fhir.instance.model.ContactPoint.ContactPointUse.OLD;
    case MOBILE: return org.hl7.fhir.instance.model.ContactPoint.ContactPointUse.MOBILE;
    default: return org.hl7.fhir.instance.model.ContactPoint.ContactPointUse.NULL;
    }
  }

  public org.hl7.fhir.dstu3.model.ElementDefinition convertElementDefinition(org.hl7.fhir.instance.model.ElementDefinition src, List<String> slicePaths) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.ElementDefinition tgt = new org.hl7.fhir.dstu3.model.ElementDefinition();
    copyElement(src, tgt);
    tgt.setPath(src.getPath());
    for (org.hl7.fhir.instance.model.Enumeration<org.hl7.fhir.instance.model.ElementDefinition.PropertyRepresentation> t : src.getRepresentation())
      tgt.addRepresentation(convertPropertyRepresentation(t.getValue()));
    if (src.hasName()) {
      if (slicePaths.contains(src.getPath()))
        tgt.setSliceName(src.getName());
      tgt.setId(src.getName());
    }
    if (src.hasLabel())
      tgt.setLabel(src.getLabel());
    for (org.hl7.fhir.instance.model.Coding t : src.getCode())
      tgt.addCode(convertCoding(t));
    if (src.hasSlicing())
      tgt.setSlicing(convertElementDefinitionSlicingComponent(src.getSlicing()));
    if (src.hasShort())
      tgt.setShort(src.getShort());
    if (src.hasDefinition())
      tgt.setDefinition(src.getDefinition());
    if (src.hasComments())
      tgt.setComment(src.getComments());
    if (src.hasRequirements())
      tgt.setRequirements(src.getRequirements());
    for (org.hl7.fhir.instance.model.StringType t : src.getAlias())
      tgt.addAlias(t.getValue());
    if (src.hasMin())
      tgt.setMin(src.getMin());
    if (src.hasMax())
      tgt.setMax(src.getMax());
    if (src.hasBase())
      tgt.setBase(convertElementDefinitionBaseComponent(src.getBase()));
    if (src.hasNameReference())
      tgt.setContentReference("#"+src.getNameReference());
    for (org.hl7.fhir.instance.model.ElementDefinition.TypeRefComponent t : src.getType())
      tgt.addType(convertElementDefinitionTypeComponent(t));
    if (src.hasDefaultValue())
      tgt.setDefaultValue(convertType(src.getDefaultValue()));
    if (src.hasMeaningWhenMissing())
      tgt.setMeaningWhenMissing(src.getMeaningWhenMissing());
    if (src.hasFixed())
      tgt.setFixed(convertType(src.getFixed()));
    if (src.hasPattern())
      tgt.setPattern(convertType(src.getPattern()));
    if (src.hasExample())
      tgt.addExample().setLabel("General").setValue(convertType(src.getExample()));
    if (src.hasMinValue())
      tgt.setMinValue(convertType(src.getMinValue()));
    if (src.hasMaxValue())
      tgt.setMaxValue(convertType(src.getMaxValue()));
    if (src.hasMaxLength())
      tgt.setMaxLength(src.getMaxLength());
    for (org.hl7.fhir.instance.model.IdType t : src.getCondition())
      tgt.addCondition(t.getValue());
    for (org.hl7.fhir.instance.model.ElementDefinition.ElementDefinitionConstraintComponent t : src.getConstraint())
      tgt.addConstraint(convertElementDefinitionConstraintComponent(t));
    if (src.hasMustSupport())
      tgt.setMustSupport(src.getMustSupport());
    if (src.hasIsModifier())
      tgt.setIsModifier(src.getIsModifier());
    if (src.hasIsSummary())
      tgt.setIsSummary(src.getIsSummary());
    if (src.hasBinding())
      tgt.setBinding(convertElementDefinitionBindingComponent(src.getBinding()));
    for (org.hl7.fhir.instance.model.ElementDefinition.ElementDefinitionMappingComponent t : src.getMapping())
      tgt.addMapping(convertElementDefinitionMappingComponent(t));
    return tgt;
  }

  public org.hl7.fhir.instance.model.ElementDefinition convertElementDefinition(org.hl7.fhir.dstu3.model.ElementDefinition src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.ElementDefinition tgt = new org.hl7.fhir.instance.model.ElementDefinition();
    copyElement(src, tgt);
    tgt.setPath(src.getPath());
    for (org.hl7.fhir.dstu3.model.Enumeration<org.hl7.fhir.dstu3.model.ElementDefinition.PropertyRepresentation> t : src.getRepresentation())
      tgt.addRepresentation(convertPropertyRepresentation(t.getValue()));
    if (src.hasSliceName())
      tgt.setName(src.getSliceName());
    else
      tgt.setName(src.getId());
    tgt.setLabel(src.getLabel());
    for (org.hl7.fhir.dstu3.model.Coding t : src.getCode())
      tgt.addCode(convertCoding(t));
    if (src.hasSlicing())
      tgt.setSlicing(convertElementDefinitionSlicingComponent(src.getSlicing()));
    tgt.setShort(src.getShort());
    tgt.setDefinition(src.getDefinition());
    tgt.setComments(src.getComment());
    tgt.setRequirements(src.getRequirements());
    for (org.hl7.fhir.dstu3.model.StringType t : src.getAlias())
      tgt.addAlias(t.getValue());
    tgt.setMin(src.getMin());
    tgt.setMax(src.getMax());
    if (src.hasBase())
      tgt.setBase(convertElementDefinitionBaseComponent(src.getBase()));
    if (src.hasContentReference())
      tgt.setNameReference(src.getContentReference().substring(1));
    for (org.hl7.fhir.dstu3.model.ElementDefinition.TypeRefComponent t : src.getType())
      tgt.addType(convertElementDefinitionTypeComponent(t));
    tgt.setDefaultValue(convertType(src.getDefaultValue()));
    tgt.setMeaningWhenMissing(src.getMeaningWhenMissing());
    tgt.setFixed(convertType(src.getFixed()));
    tgt.setPattern(convertType(src.getPattern()));
    if (src.hasExample())
      tgt.setExample(convertType(src.getExampleFirstRep().getValue()));
    tgt.setMinValue(convertType(src.getMinValue()));
    tgt.setMaxValue(convertType(src.getMaxValue()));
    tgt.setMaxLength(src.getMaxLength());
    for (org.hl7.fhir.dstu3.model.IdType t : src.getCondition())
      tgt.addCondition(t.getValue());
    for (org.hl7.fhir.dstu3.model.ElementDefinition.ElementDefinitionConstraintComponent t : src.getConstraint())
      tgt.addConstraint(convertElementDefinitionConstraintComponent(t));
    tgt.setMustSupport(src.getMustSupport());
    tgt.setIsModifier(src.getIsModifier());
    tgt.setIsSummary(src.getIsSummary());
    if (src.hasBinding())
      tgt.setBinding(convertElementDefinitionBindingComponent(src.getBinding()));
    for (org.hl7.fhir.dstu3.model.ElementDefinition.ElementDefinitionMappingComponent t : src.getMapping())
      tgt.addMapping(convertElementDefinitionMappingComponent(t));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.ElementDefinition.PropertyRepresentation convertPropertyRepresentation(org.hl7.fhir.instance.model.ElementDefinition.PropertyRepresentation src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case XMLATTR: return org.hl7.fhir.dstu3.model.ElementDefinition.PropertyRepresentation.XMLATTR;
    default: return org.hl7.fhir.dstu3.model.ElementDefinition.PropertyRepresentation.NULL;
    }
  }

  public org.hl7.fhir.instance.model.ElementDefinition.PropertyRepresentation convertPropertyRepresentation(org.hl7.fhir.dstu3.model.ElementDefinition.PropertyRepresentation src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case XMLATTR: return org.hl7.fhir.instance.model.ElementDefinition.PropertyRepresentation.XMLATTR;
    default: return org.hl7.fhir.instance.model.ElementDefinition.PropertyRepresentation.NULL;
    }
  }

  public org.hl7.fhir.dstu3.model.ElementDefinition.ElementDefinitionSlicingComponent convertElementDefinitionSlicingComponent(org.hl7.fhir.instance.model.ElementDefinition.ElementDefinitionSlicingComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.ElementDefinition.ElementDefinitionSlicingComponent tgt = new org.hl7.fhir.dstu3.model.ElementDefinition.ElementDefinitionSlicingComponent();
    copyElement(src, tgt);
    for (org.hl7.fhir.instance.model.StringType t : src.getDiscriminator())
      tgt.addDiscriminator(ProfileUtilities.interpretR2Discriminator(t.getValue()));
    tgt.setDescription(src.getDescription());
    tgt.setOrdered(src.getOrdered());
    tgt.setRules(convertSlicingRules(src.getRules()));
    return tgt;
  }

  public org.hl7.fhir.instance.model.ElementDefinition.ElementDefinitionSlicingComponent convertElementDefinitionSlicingComponent(org.hl7.fhir.dstu3.model.ElementDefinition.ElementDefinitionSlicingComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.ElementDefinition.ElementDefinitionSlicingComponent tgt = new org.hl7.fhir.instance.model.ElementDefinition.ElementDefinitionSlicingComponent();
    copyElement(src, tgt);
    for (ElementDefinitionSlicingDiscriminatorComponent t : src.getDiscriminator())
      tgt.addDiscriminator(ProfileUtilities.buildR2Discriminator(t));
    tgt.setDescription(src.getDescription());
    tgt.setOrdered(src.getOrdered());
    tgt.setRules(convertSlicingRules(src.getRules()));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.ElementDefinition.SlicingRules convertSlicingRules(org.hl7.fhir.instance.model.ElementDefinition.SlicingRules src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case CLOSED: return org.hl7.fhir.dstu3.model.ElementDefinition.SlicingRules.CLOSED;
    case OPEN: return org.hl7.fhir.dstu3.model.ElementDefinition.SlicingRules.OPEN;
    case OPENATEND: return org.hl7.fhir.dstu3.model.ElementDefinition.SlicingRules.OPENATEND;
    default: return org.hl7.fhir.dstu3.model.ElementDefinition.SlicingRules.NULL;
    }
  }

  public org.hl7.fhir.instance.model.ElementDefinition.SlicingRules convertSlicingRules(org.hl7.fhir.dstu3.model.ElementDefinition.SlicingRules src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case CLOSED: return org.hl7.fhir.instance.model.ElementDefinition.SlicingRules.CLOSED;
    case OPEN: return org.hl7.fhir.instance.model.ElementDefinition.SlicingRules.OPEN;
    case OPENATEND: return org.hl7.fhir.instance.model.ElementDefinition.SlicingRules.OPENATEND;
    default: return org.hl7.fhir.instance.model.ElementDefinition.SlicingRules.NULL;
    }
  }

  public org.hl7.fhir.dstu3.model.ElementDefinition.ElementDefinitionBaseComponent convertElementDefinitionBaseComponent(org.hl7.fhir.instance.model.ElementDefinition.ElementDefinitionBaseComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.ElementDefinition.ElementDefinitionBaseComponent tgt = new org.hl7.fhir.dstu3.model.ElementDefinition.ElementDefinitionBaseComponent();
    copyElement(src, tgt);
    tgt.setPath(src.getPath());
    tgt.setMin(src.getMin());
    tgt.setMax(src.getMax());
    return tgt;
  }

  public org.hl7.fhir.instance.model.ElementDefinition.ElementDefinitionBaseComponent convertElementDefinitionBaseComponent(org.hl7.fhir.dstu3.model.ElementDefinition.ElementDefinitionBaseComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.ElementDefinition.ElementDefinitionBaseComponent tgt = new org.hl7.fhir.instance.model.ElementDefinition.ElementDefinitionBaseComponent();
    copyElement(src, tgt);
    tgt.setPath(src.getPath());
    tgt.setMin(src.getMin());
    tgt.setMax(src.getMax());
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.ElementDefinition.TypeRefComponent convertElementDefinitionTypeComponent(org.hl7.fhir.instance.model.ElementDefinition.TypeRefComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.ElementDefinition.TypeRefComponent tgt = new org.hl7.fhir.dstu3.model.ElementDefinition.TypeRefComponent();
    copyElement(src, tgt);
    tgt.setCodeElement(convertCodeToUri(src.getCodeElement()));
    for (org.hl7.fhir.instance.model.UriType t : src.getProfile())
      if (src.hasCode() && "Reference".equals(src.getCode()))
        tgt.setTargetProfile(t.getValueAsString());
      else
        tgt.setProfile(t.getValue());
    for (org.hl7.fhir.instance.model.Enumeration<org.hl7.fhir.instance.model.ElementDefinition.AggregationMode> t : src.getAggregation())
      tgt.addAggregation(convertAggregationMode(t.getValue()));
    return tgt;
  }

  public org.hl7.fhir.instance.model.ElementDefinition.TypeRefComponent convertElementDefinitionTypeComponent(org.hl7.fhir.dstu3.model.ElementDefinition.TypeRefComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.ElementDefinition.TypeRefComponent tgt = new org.hl7.fhir.instance.model.ElementDefinition.TypeRefComponent();
    copyElement(src, tgt);
    tgt.setCodeElement(convertUriToCode(src.getCodeElement()));
    if (src.hasCode() && "Reference".equals(src.getCode())) {
      if (src.hasTargetProfile())
        tgt.addProfile(src.getTargetProfile());
    } else if (src.hasProfile())
      tgt.addProfile(src.getProfile());
    for (org.hl7.fhir.dstu3.model.Enumeration<org.hl7.fhir.dstu3.model.ElementDefinition.AggregationMode> t : src.getAggregation())
      tgt.addAggregation(convertAggregationMode(t.getValue()));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.ElementDefinition.AggregationMode convertAggregationMode(org.hl7.fhir.instance.model.ElementDefinition.AggregationMode src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case CONTAINED: return org.hl7.fhir.dstu3.model.ElementDefinition.AggregationMode.CONTAINED;
    case REFERENCED: return org.hl7.fhir.dstu3.model.ElementDefinition.AggregationMode.REFERENCED;
    case BUNDLED: return org.hl7.fhir.dstu3.model.ElementDefinition.AggregationMode.BUNDLED;
    default: return org.hl7.fhir.dstu3.model.ElementDefinition.AggregationMode.NULL;
    }
  }

  public org.hl7.fhir.instance.model.ElementDefinition.AggregationMode convertAggregationMode(org.hl7.fhir.dstu3.model.ElementDefinition.AggregationMode src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case CONTAINED: return org.hl7.fhir.instance.model.ElementDefinition.AggregationMode.CONTAINED;
    case REFERENCED: return org.hl7.fhir.instance.model.ElementDefinition.AggregationMode.REFERENCED;
    case BUNDLED: return org.hl7.fhir.instance.model.ElementDefinition.AggregationMode.BUNDLED;
    default: return org.hl7.fhir.instance.model.ElementDefinition.AggregationMode.NULL;
    }
  }

  public org.hl7.fhir.dstu3.model.ElementDefinition.ElementDefinitionConstraintComponent convertElementDefinitionConstraintComponent(org.hl7.fhir.instance.model.ElementDefinition.ElementDefinitionConstraintComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.ElementDefinition.ElementDefinitionConstraintComponent tgt = new org.hl7.fhir.dstu3.model.ElementDefinition.ElementDefinitionConstraintComponent();
    copyElement(src, tgt);
    tgt.setKey(src.getKey());
    tgt.setRequirements(src.getRequirements());
    tgt.setSeverity(convertConstraintSeverity(src.getSeverity()));
    tgt.setHuman(src.getHuman());
    tgt.setExpression(ToolingExtensions.readStringExtension(src, ToolingExtensions.EXT_EXPRESSION));
    tgt.setXpath(src.getXpath());
    return tgt;
  }

  public org.hl7.fhir.instance.model.ElementDefinition.ElementDefinitionConstraintComponent convertElementDefinitionConstraintComponent(org.hl7.fhir.dstu3.model.ElementDefinition.ElementDefinitionConstraintComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.ElementDefinition.ElementDefinitionConstraintComponent tgt = new org.hl7.fhir.instance.model.ElementDefinition.ElementDefinitionConstraintComponent();
    copyElement(src, tgt);
    tgt.setKey(src.getKey());
    tgt.setRequirements(src.getRequirements());
    tgt.setSeverity(convertConstraintSeverity(src.getSeverity()));
    tgt.setHuman(src.getHuman());
    if (src.hasExpression())
      ToolingExtensions.addStringExtension(tgt, ToolingExtensions.EXT_EXPRESSION, src.getExpression());
    tgt.setXpath(src.getXpath());
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.ElementDefinition.ConstraintSeverity convertConstraintSeverity(org.hl7.fhir.instance.model.ElementDefinition.ConstraintSeverity src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case ERROR: return org.hl7.fhir.dstu3.model.ElementDefinition.ConstraintSeverity.ERROR;
    case WARNING: return org.hl7.fhir.dstu3.model.ElementDefinition.ConstraintSeverity.WARNING;
    default: return org.hl7.fhir.dstu3.model.ElementDefinition.ConstraintSeverity.NULL;
    }
  }

  public org.hl7.fhir.instance.model.ElementDefinition.ConstraintSeverity convertConstraintSeverity(org.hl7.fhir.dstu3.model.ElementDefinition.ConstraintSeverity src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case ERROR: return org.hl7.fhir.instance.model.ElementDefinition.ConstraintSeverity.ERROR;
    case WARNING: return org.hl7.fhir.instance.model.ElementDefinition.ConstraintSeverity.WARNING;
    default: return org.hl7.fhir.instance.model.ElementDefinition.ConstraintSeverity.NULL;
    }
  }

  public org.hl7.fhir.dstu3.model.ElementDefinition.ElementDefinitionBindingComponent convertElementDefinitionBindingComponent(org.hl7.fhir.instance.model.ElementDefinition.ElementDefinitionBindingComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.ElementDefinition.ElementDefinitionBindingComponent tgt = new org.hl7.fhir.dstu3.model.ElementDefinition.ElementDefinitionBindingComponent();
    copyElement(src, tgt);
    tgt.setStrength(convertBindingStrength(src.getStrength()));
    tgt.setDescription(src.getDescription());
    tgt.setValueSet(convertType(src.getValueSet()));
    return tgt;
  }

  public org.hl7.fhir.instance.model.ElementDefinition.ElementDefinitionBindingComponent convertElementDefinitionBindingComponent(org.hl7.fhir.dstu3.model.ElementDefinition.ElementDefinitionBindingComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.ElementDefinition.ElementDefinitionBindingComponent tgt = new org.hl7.fhir.instance.model.ElementDefinition.ElementDefinitionBindingComponent();
    copyElement(src, tgt);
    tgt.setStrength(convertBindingStrength(src.getStrength()));
    tgt.setDescription(src.getDescription());
    tgt.setValueSet(convertType(src.getValueSet()));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.Enumerations.BindingStrength convertBindingStrength(org.hl7.fhir.instance.model.Enumerations.BindingStrength src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case REQUIRED: return org.hl7.fhir.dstu3.model.Enumerations.BindingStrength.REQUIRED;
    case EXTENSIBLE: return org.hl7.fhir.dstu3.model.Enumerations.BindingStrength.EXTENSIBLE;
    case PREFERRED: return org.hl7.fhir.dstu3.model.Enumerations.BindingStrength.PREFERRED;
    case EXAMPLE: return org.hl7.fhir.dstu3.model.Enumerations.BindingStrength.EXAMPLE;
    default: return org.hl7.fhir.dstu3.model.Enumerations.BindingStrength.NULL;
    }
  }

  public org.hl7.fhir.instance.model.Enumerations.BindingStrength convertBindingStrength(org.hl7.fhir.dstu3.model.Enumerations.BindingStrength src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case REQUIRED: return org.hl7.fhir.instance.model.Enumerations.BindingStrength.REQUIRED;
    case EXTENSIBLE: return org.hl7.fhir.instance.model.Enumerations.BindingStrength.EXTENSIBLE;
    case PREFERRED: return org.hl7.fhir.instance.model.Enumerations.BindingStrength.PREFERRED;
    case EXAMPLE: return org.hl7.fhir.instance.model.Enumerations.BindingStrength.EXAMPLE;
    default: return org.hl7.fhir.instance.model.Enumerations.BindingStrength.NULL;
    }
  }

  public org.hl7.fhir.dstu3.model.ElementDefinition.ElementDefinitionMappingComponent convertElementDefinitionMappingComponent(org.hl7.fhir.instance.model.ElementDefinition.ElementDefinitionMappingComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.ElementDefinition.ElementDefinitionMappingComponent tgt = new org.hl7.fhir.dstu3.model.ElementDefinition.ElementDefinitionMappingComponent();
    copyElement(src, tgt);
    tgt.setIdentity(src.getIdentity());
    tgt.setLanguage(src.getLanguage());
    tgt.setMap(src.getMap());
    return tgt;
  }

  public org.hl7.fhir.instance.model.ElementDefinition.ElementDefinitionMappingComponent convertElementDefinitionMappingComponent(org.hl7.fhir.dstu3.model.ElementDefinition.ElementDefinitionMappingComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.ElementDefinition.ElementDefinitionMappingComponent tgt = new org.hl7.fhir.instance.model.ElementDefinition.ElementDefinitionMappingComponent();
    copyElement(src, tgt);
    tgt.setIdentity(src.getIdentity());
    tgt.setLanguage(src.getLanguage());
    tgt.setMap(src.getMap());
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.HumanName convertHumanName(org.hl7.fhir.instance.model.HumanName src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.HumanName tgt = new org.hl7.fhir.dstu3.model.HumanName();
    copyElement(src, tgt);
    tgt.setUse(convertNameUse(src.getUse()));
    tgt.setText(src.getText());
    for (org.hl7.fhir.instance.model.StringType t : src.getFamily())
      tgt.setFamily(t.getValue());
    for (org.hl7.fhir.instance.model.StringType t : src.getGiven())
      tgt.addGiven(t.getValue());
    for (org.hl7.fhir.instance.model.StringType t : src.getPrefix())
      tgt.addPrefix(t.getValue());
    for (org.hl7.fhir.instance.model.StringType t : src.getSuffix())
      tgt.addSuffix(t.getValue());
    tgt.setPeriod(convertPeriod(src.getPeriod()));
    return tgt;
  }

  public org.hl7.fhir.instance.model.HumanName convertHumanName(org.hl7.fhir.dstu3.model.HumanName src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.HumanName tgt = new org.hl7.fhir.instance.model.HumanName();
    copyElement(src, tgt);
    tgt.setUse(convertNameUse(src.getUse()));
    tgt.setText(src.getText());
    if (src.hasFamily())
      tgt.addFamily(src.getFamily());
    for (org.hl7.fhir.dstu3.model.StringType t : src.getGiven())
      tgt.addGiven(t.getValue());
    for (org.hl7.fhir.dstu3.model.StringType t : src.getPrefix())
      tgt.addPrefix(t.getValue());
    for (org.hl7.fhir.dstu3.model.StringType t : src.getSuffix())
      tgt.addSuffix(t.getValue());
    tgt.setPeriod(convertPeriod(src.getPeriod()));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.HumanName.NameUse convertNameUse(org.hl7.fhir.instance.model.HumanName.NameUse src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case USUAL: return org.hl7.fhir.dstu3.model.HumanName.NameUse.USUAL;
    case OFFICIAL: return org.hl7.fhir.dstu3.model.HumanName.NameUse.OFFICIAL;
    case TEMP: return org.hl7.fhir.dstu3.model.HumanName.NameUse.TEMP;
    case NICKNAME: return org.hl7.fhir.dstu3.model.HumanName.NameUse.NICKNAME;
    case ANONYMOUS: return org.hl7.fhir.dstu3.model.HumanName.NameUse.ANONYMOUS;
    case OLD: return org.hl7.fhir.dstu3.model.HumanName.NameUse.OLD;
    case MAIDEN: return org.hl7.fhir.dstu3.model.HumanName.NameUse.MAIDEN;
    default: return org.hl7.fhir.dstu3.model.HumanName.NameUse.NULL;
    }
  }

  public org.hl7.fhir.instance.model.HumanName.NameUse convertNameUse(org.hl7.fhir.dstu3.model.HumanName.NameUse src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case USUAL: return org.hl7.fhir.instance.model.HumanName.NameUse.USUAL;
    case OFFICIAL: return org.hl7.fhir.instance.model.HumanName.NameUse.OFFICIAL;
    case TEMP: return org.hl7.fhir.instance.model.HumanName.NameUse.TEMP;
    case NICKNAME: return org.hl7.fhir.instance.model.HumanName.NameUse.NICKNAME;
    case ANONYMOUS: return org.hl7.fhir.instance.model.HumanName.NameUse.ANONYMOUS;
    case OLD: return org.hl7.fhir.instance.model.HumanName.NameUse.OLD;
    case MAIDEN: return org.hl7.fhir.instance.model.HumanName.NameUse.MAIDEN;
    default: return org.hl7.fhir.instance.model.HumanName.NameUse.NULL;
    }
  }

  public org.hl7.fhir.dstu3.model.Meta convertMeta(org.hl7.fhir.instance.model.Meta src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.Meta tgt = new org.hl7.fhir.dstu3.model.Meta();
    copyElement(src, tgt);
    tgt.setVersionId(src.getVersionId());
    tgt.setLastUpdated(src.getLastUpdated());
    for (org.hl7.fhir.instance.model.UriType t : src.getProfile())
      tgt.addProfile(t.getValue());
    for (org.hl7.fhir.instance.model.Coding t : src.getSecurity())
      tgt.addSecurity(convertCoding(t));
    for (org.hl7.fhir.instance.model.Coding t : src.getTag())
      tgt.addTag(convertCoding(t));
    return tgt;
  }

  public org.hl7.fhir.instance.model.Meta convertMeta(org.hl7.fhir.dstu3.model.Meta src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.Meta tgt = new org.hl7.fhir.instance.model.Meta();
    copyElement(src, tgt);
    tgt.setVersionId(src.getVersionId());
    tgt.setLastUpdated(src.getLastUpdated());
    for (org.hl7.fhir.dstu3.model.UriType t : src.getProfile())
      tgt.addProfile(t.getValue());
    for (org.hl7.fhir.dstu3.model.Coding t : src.getSecurity())
      tgt.addSecurity(convertCoding(t));
    for (org.hl7.fhir.dstu3.model.Coding t : src.getTag())
      tgt.addTag(convertCoding(t));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.Timing convertTiming(org.hl7.fhir.instance.model.Timing src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.Timing tgt = new org.hl7.fhir.dstu3.model.Timing();
    copyElement(src, tgt);
    for (org.hl7.fhir.instance.model.DateTimeType t : src.getEvent())
      tgt.addEvent(t.getValue());
    tgt.setRepeat(convertTimingRepeatComponent(src.getRepeat()));
    tgt.setCode(convertCodeableConcept(src.getCode()));
    return tgt;
  }

  public org.hl7.fhir.instance.model.Timing convertTiming(org.hl7.fhir.dstu3.model.Timing src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.Timing tgt = new org.hl7.fhir.instance.model.Timing();
    copyElement(src, tgt);
    for (org.hl7.fhir.dstu3.model.DateTimeType t : src.getEvent())
      tgt.addEvent(t.getValue());
    tgt.setRepeat(convertTimingRepeatComponent(src.getRepeat()));
    tgt.setCode(convertCodeableConcept(src.getCode()));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.Timing.TimingRepeatComponent convertTimingRepeatComponent(org.hl7.fhir.instance.model.Timing.TimingRepeatComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.Timing.TimingRepeatComponent tgt = new org.hl7.fhir.dstu3.model.Timing.TimingRepeatComponent();
    copyElement(src, tgt);
    tgt.setBounds(convertType(src.getBounds()));
    tgt.setCount(src.getCount());
    tgt.setDuration(src.getDuration());
    tgt.setDurationMax(src.getDurationMax());
    tgt.setDurationUnit(convertUnitsOfTime(src.getDurationUnits()));
    tgt.setFrequency(src.getFrequency());
    tgt.setFrequencyMax(src.getFrequencyMax());
    tgt.setPeriod(src.getPeriod());
    tgt.setPeriodMax(src.getPeriodMax());
    tgt.setPeriodUnit(convertUnitsOfTime(src.getPeriodUnits()));
    tgt.addWhen(convertEventTiming(src.getWhen()));
    return tgt;
  }

  public org.hl7.fhir.instance.model.Timing.TimingRepeatComponent convertTimingRepeatComponent(org.hl7.fhir.dstu3.model.Timing.TimingRepeatComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.Timing.TimingRepeatComponent tgt = new org.hl7.fhir.instance.model.Timing.TimingRepeatComponent();
    copyElement(src, tgt);
    tgt.setBounds(convertType(src.getBounds()));
    tgt.setCount(src.getCount());
    tgt.setDuration(src.getDuration());
    tgt.setDurationMax(src.getDurationMax());
    tgt.setDurationUnits(convertUnitsOfTime(src.getDurationUnit()));
    tgt.setFrequency(src.getFrequency());
    tgt.setFrequencyMax(src.getFrequencyMax());
    tgt.setPeriod(src.getPeriod());
    tgt.setPeriodMax(src.getPeriodMax());
    tgt.setPeriodUnits(convertUnitsOfTime(src.getPeriodUnit()));
    for (Enumeration<EventTiming> t : src.getWhen())
      tgt.setWhen(convertEventTiming(t.getValue()));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.Timing.UnitsOfTime convertUnitsOfTime(org.hl7.fhir.instance.model.Timing.UnitsOfTime src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case S: return org.hl7.fhir.dstu3.model.Timing.UnitsOfTime.S;
    case MIN: return org.hl7.fhir.dstu3.model.Timing.UnitsOfTime.MIN;
    case H: return org.hl7.fhir.dstu3.model.Timing.UnitsOfTime.H;
    case D: return org.hl7.fhir.dstu3.model.Timing.UnitsOfTime.D;
    case WK: return org.hl7.fhir.dstu3.model.Timing.UnitsOfTime.WK;
    case MO: return org.hl7.fhir.dstu3.model.Timing.UnitsOfTime.MO;
    case A: return org.hl7.fhir.dstu3.model.Timing.UnitsOfTime.A;
    default: return org.hl7.fhir.dstu3.model.Timing.UnitsOfTime.NULL;
    }
  }

  public org.hl7.fhir.instance.model.Timing.UnitsOfTime convertUnitsOfTime(org.hl7.fhir.dstu3.model.Timing.UnitsOfTime src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case S: return org.hl7.fhir.instance.model.Timing.UnitsOfTime.S;
    case MIN: return org.hl7.fhir.instance.model.Timing.UnitsOfTime.MIN;
    case H: return org.hl7.fhir.instance.model.Timing.UnitsOfTime.H;
    case D: return org.hl7.fhir.instance.model.Timing.UnitsOfTime.D;
    case WK: return org.hl7.fhir.instance.model.Timing.UnitsOfTime.WK;
    case MO: return org.hl7.fhir.instance.model.Timing.UnitsOfTime.MO;
    case A: return org.hl7.fhir.instance.model.Timing.UnitsOfTime.A;
    default: return org.hl7.fhir.instance.model.Timing.UnitsOfTime.NULL;
    }
  }

  public org.hl7.fhir.dstu3.model.Timing.EventTiming convertEventTiming(org.hl7.fhir.instance.model.Timing.EventTiming src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case HS: return org.hl7.fhir.dstu3.model.Timing.EventTiming.HS;
    case WAKE: return org.hl7.fhir.dstu3.model.Timing.EventTiming.WAKE;
    case C: return org.hl7.fhir.dstu3.model.Timing.EventTiming.C;
    case CM: return org.hl7.fhir.dstu3.model.Timing.EventTiming.CM;
    case CD: return org.hl7.fhir.dstu3.model.Timing.EventTiming.CD;
    case CV: return org.hl7.fhir.dstu3.model.Timing.EventTiming.CV;
    case AC: return org.hl7.fhir.dstu3.model.Timing.EventTiming.AC;
    case ACM: return org.hl7.fhir.dstu3.model.Timing.EventTiming.ACM;
    case ACD: return org.hl7.fhir.dstu3.model.Timing.EventTiming.ACD;
    case ACV: return org.hl7.fhir.dstu3.model.Timing.EventTiming.ACV;
    case PC: return org.hl7.fhir.dstu3.model.Timing.EventTiming.PC;
    case PCM: return org.hl7.fhir.dstu3.model.Timing.EventTiming.PCM;
    case PCD: return org.hl7.fhir.dstu3.model.Timing.EventTiming.PCD;
    case PCV: return org.hl7.fhir.dstu3.model.Timing.EventTiming.PCV;
    default: return org.hl7.fhir.dstu3.model.Timing.EventTiming.NULL;
    }
  }

  public org.hl7.fhir.instance.model.Timing.EventTiming convertEventTiming(org.hl7.fhir.dstu3.model.Timing.EventTiming src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case HS: return org.hl7.fhir.instance.model.Timing.EventTiming.HS;
    case WAKE: return org.hl7.fhir.instance.model.Timing.EventTiming.WAKE;
    case C: return org.hl7.fhir.instance.model.Timing.EventTiming.C;
    case CM: return org.hl7.fhir.instance.model.Timing.EventTiming.CM;
    case CD: return org.hl7.fhir.instance.model.Timing.EventTiming.CD;
    case CV: return org.hl7.fhir.instance.model.Timing.EventTiming.CV;
    case AC: return org.hl7.fhir.instance.model.Timing.EventTiming.AC;
    case ACM: return org.hl7.fhir.instance.model.Timing.EventTiming.ACM;
    case ACD: return org.hl7.fhir.instance.model.Timing.EventTiming.ACD;
    case ACV: return org.hl7.fhir.instance.model.Timing.EventTiming.ACV;
    case PC: return org.hl7.fhir.instance.model.Timing.EventTiming.PC;
    case PCM: return org.hl7.fhir.instance.model.Timing.EventTiming.PCM;
    case PCD: return org.hl7.fhir.instance.model.Timing.EventTiming.PCD;
    case PCV: return org.hl7.fhir.instance.model.Timing.EventTiming.PCV;
    default: return org.hl7.fhir.instance.model.Timing.EventTiming.NULL;
    }
  }

  public org.hl7.fhir.dstu3.model.Age convertAge(org.hl7.fhir.instance.model.Age src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.Age tgt = new org.hl7.fhir.dstu3.model.Age();
    copyElement(src, tgt);
    tgt.setValue(src.getValue());
    tgt.setComparator(convertQuantityComparator(src.getComparator()));
    tgt.setUnit(src.getUnit());
    tgt.setSystem(src.getSystem());
    tgt.setCode(src.getCode());
    return tgt;
  }

  public org.hl7.fhir.instance.model.Age convertAge(org.hl7.fhir.dstu3.model.Age src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.Age tgt = new org.hl7.fhir.instance.model.Age();
    copyElement(src, tgt);
    tgt.setValue(src.getValue());
    tgt.setComparator(convertQuantityComparator(src.getComparator()));
    tgt.setUnit(src.getUnit());
    tgt.setSystem(src.getSystem());
    tgt.setCode(src.getCode());
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.Count convertCount(org.hl7.fhir.instance.model.Count src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.Count tgt = new org.hl7.fhir.dstu3.model.Count();
    copyElement(src, tgt);
    tgt.setValue(src.getValue());
    tgt.setComparator(convertQuantityComparator(src.getComparator()));
    tgt.setUnit(src.getUnit());
    tgt.setSystem(src.getSystem());
    tgt.setCode(src.getCode());
    return tgt;
  }

  public org.hl7.fhir.instance.model.Count convertCount(org.hl7.fhir.dstu3.model.Count src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.Count tgt = new org.hl7.fhir.instance.model.Count();
    copyElement(src, tgt);
    tgt.setValue(src.getValue());
    tgt.setComparator(convertQuantityComparator(src.getComparator()));
    tgt.setUnit(src.getUnit());
    tgt.setSystem(src.getSystem());
    tgt.setCode(src.getCode());
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.Distance convertDistance(org.hl7.fhir.instance.model.Distance src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.Distance tgt = new org.hl7.fhir.dstu3.model.Distance();
    copyElement(src, tgt);
    tgt.setValue(src.getValue());
    tgt.setComparator(convertQuantityComparator(src.getComparator()));
    tgt.setUnit(src.getUnit());
    tgt.setSystem(src.getSystem());
    tgt.setCode(src.getCode());
    return tgt;
  }

  public org.hl7.fhir.instance.model.Distance convertDistance(org.hl7.fhir.dstu3.model.Distance src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.Distance tgt = new org.hl7.fhir.instance.model.Distance();
    copyElement(src, tgt);
    tgt.setValue(src.getValue());
    tgt.setComparator(convertQuantityComparator(src.getComparator()));
    tgt.setUnit(src.getUnit());
    tgt.setSystem(src.getSystem());
    tgt.setCode(src.getCode());
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.Duration convertDuration(org.hl7.fhir.instance.model.Duration src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.Duration tgt = new org.hl7.fhir.dstu3.model.Duration();
    copyElement(src, tgt);
    tgt.setValue(src.getValue());
    tgt.setComparator(convertQuantityComparator(src.getComparator()));
    tgt.setUnit(src.getUnit());
    tgt.setSystem(src.getSystem());
    tgt.setCode(src.getCode());
    return tgt;
  }

  public org.hl7.fhir.instance.model.Duration convertDuration(org.hl7.fhir.dstu3.model.Duration src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.Duration tgt = new org.hl7.fhir.instance.model.Duration();
    copyElement(src, tgt);
    tgt.setValue(src.getValue());
    tgt.setComparator(convertQuantityComparator(src.getComparator()));
    tgt.setUnit(src.getUnit());
    tgt.setSystem(src.getSystem());
    tgt.setCode(src.getCode());
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.Money convertMoney(org.hl7.fhir.instance.model.Money src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.Money tgt = new org.hl7.fhir.dstu3.model.Money();
    copyElement(src, tgt);
    tgt.setValue(src.getValue());
    tgt.setComparator(convertQuantityComparator(src.getComparator()));
    tgt.setUnit(src.getUnit());
    tgt.setSystem(src.getSystem());
    tgt.setCode(src.getCode());
    return tgt;
  }

  public org.hl7.fhir.instance.model.Money convertMoney(org.hl7.fhir.dstu3.model.Money src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.Money tgt = new org.hl7.fhir.instance.model.Money();
    copyElement(src, tgt);
    tgt.setValue(src.getValue());
    tgt.setComparator(convertQuantityComparator(src.getComparator()));
    tgt.setUnit(src.getUnit());
    tgt.setSystem(src.getSystem());
    tgt.setCode(src.getCode());
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.SimpleQuantity convertSimpleQuantity(org.hl7.fhir.instance.model.SimpleQuantity src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.SimpleQuantity tgt = new org.hl7.fhir.dstu3.model.SimpleQuantity();
    copyElement(src, tgt);
    tgt.setValue(src.getValue());
    tgt.setComparator(convertQuantityComparator(src.getComparator()));
    tgt.setUnit(src.getUnit());
    tgt.setSystem(src.getSystem());
    tgt.setCode(src.getCode());
    return tgt;
  }

  public org.hl7.fhir.instance.model.SimpleQuantity convertSimpleQuantity(org.hl7.fhir.dstu3.model.SimpleQuantity src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.SimpleQuantity tgt = new org.hl7.fhir.instance.model.SimpleQuantity();
    copyElement(src, tgt);
    tgt.setValue(src.getValue());
    tgt.setComparator(convertQuantityComparator(src.getComparator()));
    tgt.setUnit(src.getUnit());
    tgt.setSystem(src.getSystem());
    tgt.setCode(src.getCode());
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.Type convertType(org.hl7.fhir.instance.model.Type src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    if (src instanceof org.hl7.fhir.instance.model.Base64BinaryType)
      return convertBase64Binary((org.hl7.fhir.instance.model.Base64BinaryType) src);
    if (src instanceof org.hl7.fhir.instance.model.BooleanType)
      return convertBoolean((org.hl7.fhir.instance.model.BooleanType) src);
    if (src instanceof org.hl7.fhir.instance.model.CodeType)
      return convertCode((org.hl7.fhir.instance.model.CodeType) src);
    if (src instanceof org.hl7.fhir.instance.model.DateType)
      return convertDate((org.hl7.fhir.instance.model.DateType) src);
    if (src instanceof org.hl7.fhir.instance.model.DateTimeType)
      return convertDateTime((org.hl7.fhir.instance.model.DateTimeType) src);
    if (src instanceof org.hl7.fhir.instance.model.DecimalType)
      return convertDecimal((org.hl7.fhir.instance.model.DecimalType) src);
    if (src instanceof org.hl7.fhir.instance.model.IdType)
      return convertId((org.hl7.fhir.instance.model.IdType) src);
    if (src instanceof org.hl7.fhir.instance.model.InstantType)
      return convertInstant((org.hl7.fhir.instance.model.InstantType) src);
    if (src instanceof org.hl7.fhir.instance.model.IntegerType)
      return convertInteger((org.hl7.fhir.instance.model.IntegerType) src);
    if (src instanceof org.hl7.fhir.instance.model.MarkdownType)
      return convertMarkdown((org.hl7.fhir.instance.model.MarkdownType) src);
    if (src instanceof org.hl7.fhir.instance.model.OidType)
      return convertOid((org.hl7.fhir.instance.model.OidType) src);
    if (src instanceof org.hl7.fhir.instance.model.PositiveIntType)
      return convertPositiveInt((org.hl7.fhir.instance.model.PositiveIntType) src);
    if (src instanceof org.hl7.fhir.instance.model.StringType)
      return convertString((org.hl7.fhir.instance.model.StringType) src);
    if (src instanceof org.hl7.fhir.instance.model.TimeType)
      return convertTime((org.hl7.fhir.instance.model.TimeType) src);
    if (src instanceof org.hl7.fhir.instance.model.UnsignedIntType)
      return convertUnsignedInt((org.hl7.fhir.instance.model.UnsignedIntType) src);
    if (src instanceof org.hl7.fhir.instance.model.UriType)
      return convertUri((org.hl7.fhir.instance.model.UriType) src);
    if (src instanceof org.hl7.fhir.instance.model.UuidType)
      return convertUuid((org.hl7.fhir.instance.model.UuidType) src);
    if (src instanceof org.hl7.fhir.instance.model.Extension)
      return convertExtension((org.hl7.fhir.instance.model.Extension) src);
    if (src instanceof org.hl7.fhir.instance.model.Narrative)
      return convertNarrative((org.hl7.fhir.instance.model.Narrative) src);
    if (src instanceof org.hl7.fhir.instance.model.Annotation)
      return convertAnnotation((org.hl7.fhir.instance.model.Annotation) src);
    if (src instanceof org.hl7.fhir.instance.model.Attachment)
      return convertAttachment((org.hl7.fhir.instance.model.Attachment) src);
    if (src instanceof org.hl7.fhir.instance.model.CodeableConcept)
      return convertCodeableConcept((org.hl7.fhir.instance.model.CodeableConcept) src);
    if (src instanceof org.hl7.fhir.instance.model.Coding)
      return convertCoding((org.hl7.fhir.instance.model.Coding) src);
    if (src instanceof org.hl7.fhir.instance.model.Identifier)
      return convertIdentifier((org.hl7.fhir.instance.model.Identifier) src);
    if (src instanceof org.hl7.fhir.instance.model.Period)
      return convertPeriod((org.hl7.fhir.instance.model.Period) src);
    if (src instanceof org.hl7.fhir.instance.model.Quantity)
      return convertQuantity((org.hl7.fhir.instance.model.Quantity) src);
    if (src instanceof org.hl7.fhir.instance.model.Range)
      return convertRange((org.hl7.fhir.instance.model.Range) src);
    if (src instanceof org.hl7.fhir.instance.model.Ratio)
      return convertRatio((org.hl7.fhir.instance.model.Ratio) src);
    if (src instanceof org.hl7.fhir.instance.model.Reference)
      return convertReference((org.hl7.fhir.instance.model.Reference) src);
    if (src instanceof org.hl7.fhir.instance.model.SampledData)
      return convertSampledData((org.hl7.fhir.instance.model.SampledData) src);
    if (src instanceof org.hl7.fhir.instance.model.Signature)
      return convertSignature((org.hl7.fhir.instance.model.Signature) src);
    if (src instanceof org.hl7.fhir.instance.model.Address)
      return convertAddress((org.hl7.fhir.instance.model.Address) src);
    if (src instanceof org.hl7.fhir.instance.model.ContactPoint)
      return convertContactPoint((org.hl7.fhir.instance.model.ContactPoint) src);
    if (src instanceof org.hl7.fhir.instance.model.ElementDefinition)
      return convertElementDefinition((org.hl7.fhir.instance.model.ElementDefinition) src, new ArrayList<String>());
    if (src instanceof org.hl7.fhir.instance.model.HumanName)
      return convertHumanName((org.hl7.fhir.instance.model.HumanName) src);
    if (src instanceof org.hl7.fhir.instance.model.Meta)
      return convertMeta((org.hl7.fhir.instance.model.Meta) src);
    if (src instanceof org.hl7.fhir.instance.model.Timing)
      return convertTiming((org.hl7.fhir.instance.model.Timing) src);
    if (src instanceof org.hl7.fhir.instance.model.Age)
      return convertAge((org.hl7.fhir.instance.model.Age) src);
    if (src instanceof org.hl7.fhir.instance.model.Count)
      return convertCount((org.hl7.fhir.instance.model.Count) src);
    if (src instanceof org.hl7.fhir.instance.model.Distance)
      return convertDistance((org.hl7.fhir.instance.model.Distance) src);
    if (src instanceof org.hl7.fhir.instance.model.Duration)
      return convertDuration((org.hl7.fhir.instance.model.Duration) src);
    if (src instanceof org.hl7.fhir.instance.model.Money)
      return convertMoney((org.hl7.fhir.instance.model.Money) src);
    if (src instanceof org.hl7.fhir.instance.model.SimpleQuantity)
      return convertSimpleQuantity((org.hl7.fhir.instance.model.SimpleQuantity) src);
    throw new Error("Unknown type "+src.getClass());
  }

  public org.hl7.fhir.instance.model.Type convertType(org.hl7.fhir.dstu3.model.Type src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    if (src instanceof org.hl7.fhir.dstu3.model.Base64BinaryType)
      return convertBase64Binary((org.hl7.fhir.dstu3.model.Base64BinaryType) src);
    if (src instanceof org.hl7.fhir.dstu3.model.BooleanType)
      return convertBoolean((org.hl7.fhir.dstu3.model.BooleanType) src);
    if (src instanceof org.hl7.fhir.dstu3.model.CodeType)
      return convertCode((org.hl7.fhir.dstu3.model.CodeType) src);
    if (src instanceof org.hl7.fhir.dstu3.model.DateType)
      return convertDate((org.hl7.fhir.dstu3.model.DateType) src);
    if (src instanceof org.hl7.fhir.dstu3.model.DateTimeType)
      return convertDateTime((org.hl7.fhir.dstu3.model.DateTimeType) src);
    if (src instanceof org.hl7.fhir.dstu3.model.DecimalType)
      return convertDecimal((org.hl7.fhir.dstu3.model.DecimalType) src);
    if (src instanceof org.hl7.fhir.dstu3.model.IdType)
      return convertId((org.hl7.fhir.dstu3.model.IdType) src);
    if (src instanceof org.hl7.fhir.dstu3.model.InstantType)
      return convertInstant((org.hl7.fhir.dstu3.model.InstantType) src);
    if (src instanceof org.hl7.fhir.dstu3.model.IntegerType)
      return convertInteger((org.hl7.fhir.dstu3.model.IntegerType) src);
    if (src instanceof org.hl7.fhir.dstu3.model.MarkdownType)
      return convertMarkdown((org.hl7.fhir.dstu3.model.MarkdownType) src);
    if (src instanceof org.hl7.fhir.dstu3.model.OidType)
      return convertOid((org.hl7.fhir.dstu3.model.OidType) src);
    if (src instanceof org.hl7.fhir.dstu3.model.PositiveIntType)
      return convertPositiveInt((org.hl7.fhir.dstu3.model.PositiveIntType) src);
    if (src instanceof org.hl7.fhir.dstu3.model.StringType)
      return convertString((org.hl7.fhir.dstu3.model.StringType) src);
    if (src instanceof org.hl7.fhir.dstu3.model.TimeType)
      return convertTime((org.hl7.fhir.dstu3.model.TimeType) src);
    if (src instanceof org.hl7.fhir.dstu3.model.UnsignedIntType)
      return convertUnsignedInt((org.hl7.fhir.dstu3.model.UnsignedIntType) src);
    if (src instanceof org.hl7.fhir.dstu3.model.UriType)
      return convertUri((org.hl7.fhir.dstu3.model.UriType) src);
    if (src instanceof org.hl7.fhir.dstu3.model.UuidType)
      return convertUuid((org.hl7.fhir.dstu3.model.UuidType) src);
    if (src instanceof org.hl7.fhir.dstu3.model.Extension)
      return convertExtension((org.hl7.fhir.dstu3.model.Extension) src);
    if (src instanceof org.hl7.fhir.dstu3.model.Narrative)
      return convertNarrative((org.hl7.fhir.dstu3.model.Narrative) src);
    if (src instanceof org.hl7.fhir.dstu3.model.Annotation)
      return convertAnnotation((org.hl7.fhir.dstu3.model.Annotation) src);
    if (src instanceof org.hl7.fhir.dstu3.model.Attachment)
      return convertAttachment((org.hl7.fhir.dstu3.model.Attachment) src);
    if (src instanceof org.hl7.fhir.dstu3.model.CodeableConcept)
      return convertCodeableConcept((org.hl7.fhir.dstu3.model.CodeableConcept) src);
    if (src instanceof org.hl7.fhir.dstu3.model.Coding)
      return convertCoding((org.hl7.fhir.dstu3.model.Coding) src);
    if (src instanceof org.hl7.fhir.dstu3.model.Identifier)
      return convertIdentifier((org.hl7.fhir.dstu3.model.Identifier) src);
    if (src instanceof org.hl7.fhir.dstu3.model.Period)
      return convertPeriod((org.hl7.fhir.dstu3.model.Period) src);
    if (src instanceof org.hl7.fhir.dstu3.model.Quantity)
      return convertQuantity((org.hl7.fhir.dstu3.model.Quantity) src);
    if (src instanceof org.hl7.fhir.dstu3.model.Range)
      return convertRange((org.hl7.fhir.dstu3.model.Range) src);
    if (src instanceof org.hl7.fhir.dstu3.model.Ratio)
      return convertRatio((org.hl7.fhir.dstu3.model.Ratio) src);
    if (src instanceof org.hl7.fhir.dstu3.model.Reference)
      return convertReference((org.hl7.fhir.dstu3.model.Reference) src);
    if (src instanceof org.hl7.fhir.dstu3.model.SampledData)
      return convertSampledData((org.hl7.fhir.dstu3.model.SampledData) src);
    if (src instanceof org.hl7.fhir.dstu3.model.Signature)
      return convertSignature((org.hl7.fhir.dstu3.model.Signature) src);
    if (src instanceof org.hl7.fhir.dstu3.model.Address)
      return convertAddress((org.hl7.fhir.dstu3.model.Address) src);
    if (src instanceof org.hl7.fhir.dstu3.model.ContactPoint)
      return convertContactPoint((org.hl7.fhir.dstu3.model.ContactPoint) src);
    if (src instanceof org.hl7.fhir.dstu3.model.ElementDefinition)
      return convertElementDefinition((org.hl7.fhir.dstu3.model.ElementDefinition) src);
    if (src instanceof org.hl7.fhir.dstu3.model.HumanName)
      return convertHumanName((org.hl7.fhir.dstu3.model.HumanName) src);
    if (src instanceof org.hl7.fhir.dstu3.model.Meta)
      return convertMeta((org.hl7.fhir.dstu3.model.Meta) src);
    if (src instanceof org.hl7.fhir.dstu3.model.Timing)
      return convertTiming((org.hl7.fhir.dstu3.model.Timing) src);
    if (src instanceof org.hl7.fhir.dstu3.model.Age)
      return convertAge((org.hl7.fhir.dstu3.model.Age) src);
    if (src instanceof org.hl7.fhir.dstu3.model.Count)
      return convertCount((org.hl7.fhir.dstu3.model.Count) src);
    if (src instanceof org.hl7.fhir.dstu3.model.Distance)
      return convertDistance((org.hl7.fhir.dstu3.model.Distance) src);
    if (src instanceof org.hl7.fhir.dstu3.model.Duration)
      return convertDuration((org.hl7.fhir.dstu3.model.Duration) src);
    if (src instanceof org.hl7.fhir.dstu3.model.Money)
      return convertMoney((org.hl7.fhir.dstu3.model.Money) src);
    if (src instanceof org.hl7.fhir.dstu3.model.SimpleQuantity)
      return convertSimpleQuantity((org.hl7.fhir.dstu3.model.SimpleQuantity) src);
    throw new Error("Unknown type "+src.fhirType());
  }

  public void copyDomainResource(org.hl7.fhir.instance.model.DomainResource src, org.hl7.fhir.dstu3.model.DomainResource tgt) throws FHIRException {
    copyResource(src, tgt);
    tgt.setText(convertNarrative(src.getText()));
    for (org.hl7.fhir.instance.model.Resource t : src.getContained())
      tgt.addContained(convertResource(t));
    for (org.hl7.fhir.instance.model.Extension t : src.getExtension())
      tgt.addExtension(convertExtension(t));
    for (org.hl7.fhir.instance.model.Extension t : src.getModifierExtension())
      tgt.addModifierExtension(convertExtension(t));
  }
  public void copyDomainResource(org.hl7.fhir.dstu3.model.DomainResource src, org.hl7.fhir.instance.model.DomainResource tgt) throws FHIRException {
    copyResource(src, tgt);
    tgt.setText(convertNarrative(src.getText()));
    for (org.hl7.fhir.dstu3.model.Resource t : src.getContained())
      tgt.addContained(convertResource(t));
    for (org.hl7.fhir.dstu3.model.Extension t : src.getExtension())
      tgt.addExtension(convertExtension(t));
    for (org.hl7.fhir.dstu3.model.Extension t : src.getModifierExtension())
      tgt.addModifierExtension(convertExtension(t));
  }

  public org.hl7.fhir.dstu3.model.Parameters convertParameters(org.hl7.fhir.instance.model.Parameters src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.Parameters tgt = new org.hl7.fhir.dstu3.model.Parameters();
    copyResource(src, tgt);
    for (org.hl7.fhir.instance.model.Parameters.ParametersParameterComponent t : src.getParameter())
      tgt.addParameter(convertParametersParameterComponent(t));
    return tgt;
  }

  public org.hl7.fhir.instance.model.Parameters convertParameters(org.hl7.fhir.dstu3.model.Parameters src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.Parameters tgt = new org.hl7.fhir.instance.model.Parameters();
    copyResource(src, tgt);
    for (org.hl7.fhir.dstu3.model.Parameters.ParametersParameterComponent t : src.getParameter())
      tgt.addParameter(convertParametersParameterComponent(t));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.Parameters.ParametersParameterComponent convertParametersParameterComponent(org.hl7.fhir.instance.model.Parameters.ParametersParameterComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.Parameters.ParametersParameterComponent tgt = new org.hl7.fhir.dstu3.model.Parameters.ParametersParameterComponent();
    copyElement(src, tgt);
    tgt.setName(src.getName());
    tgt.setValue(convertType(src.getValue()));
    tgt.setResource(convertResource(src.getResource()));
    for (org.hl7.fhir.instance.model.Parameters.ParametersParameterComponent t : src.getPart())
      tgt.addPart(convertParametersParameterComponent(t));
    return tgt;
  }

  public org.hl7.fhir.instance.model.Parameters.ParametersParameterComponent convertParametersParameterComponent(org.hl7.fhir.dstu3.model.Parameters.ParametersParameterComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.Parameters.ParametersParameterComponent tgt = new org.hl7.fhir.instance.model.Parameters.ParametersParameterComponent();
    copyElement(src, tgt);
    tgt.setName(src.getName());
    tgt.setValue(convertType(src.getValue()));
    tgt.setResource(convertResource(src.getResource()));
    for (org.hl7.fhir.dstu3.model.Parameters.ParametersParameterComponent t : src.getPart())
      tgt.addPart(convertParametersParameterComponent(t));
    return tgt;
  }

  public void copyResource(org.hl7.fhir.instance.model.Resource src, org.hl7.fhir.dstu3.model.Resource tgt) throws FHIRException {
    tgt.setId(src.getId());
    tgt.setMeta(convertMeta(src.getMeta()));
    tgt.setImplicitRules(src.getImplicitRules());
    tgt.setLanguage(src.getLanguage());
  }
  public void copyResource(org.hl7.fhir.dstu3.model.Resource src, org.hl7.fhir.instance.model.Resource tgt) throws FHIRException {
    tgt.setId(src.getId());
    if (src.hasMeta())
      tgt.setMeta(convertMeta(src.getMeta()));
    if (src.hasImplicitRules())
      tgt.setImplicitRules(src.getImplicitRules());
    if (src.hasLanguage())
      tgt.setLanguage(src.getLanguage());
  }

  public org.hl7.fhir.dstu3.model.Enumerations.AdministrativeGender convertAdministrativeGender(org.hl7.fhir.instance.model.Enumerations.AdministrativeGender src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case MALE: return org.hl7.fhir.dstu3.model.Enumerations.AdministrativeGender.MALE;
    case FEMALE: return org.hl7.fhir.dstu3.model.Enumerations.AdministrativeGender.FEMALE;
    case OTHER: return org.hl7.fhir.dstu3.model.Enumerations.AdministrativeGender.OTHER;
    case UNKNOWN: return org.hl7.fhir.dstu3.model.Enumerations.AdministrativeGender.UNKNOWN;
    default: return org.hl7.fhir.dstu3.model.Enumerations.AdministrativeGender.NULL;
    }
  }

  public org.hl7.fhir.instance.model.Enumerations.AdministrativeGender convertAdministrativeGender(org.hl7.fhir.dstu3.model.Enumerations.AdministrativeGender src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case MALE: return org.hl7.fhir.instance.model.Enumerations.AdministrativeGender.MALE;
    case FEMALE: return org.hl7.fhir.instance.model.Enumerations.AdministrativeGender.FEMALE;
    case OTHER: return org.hl7.fhir.instance.model.Enumerations.AdministrativeGender.OTHER;
    case UNKNOWN: return org.hl7.fhir.instance.model.Enumerations.AdministrativeGender.UNKNOWN;
    default: return org.hl7.fhir.instance.model.Enumerations.AdministrativeGender.NULL;
    }
  }

  public org.hl7.fhir.dstu3.model.Enumerations.SearchParamType convertSearchParamType(org.hl7.fhir.instance.model.Enumerations.SearchParamType src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case NUMBER: return org.hl7.fhir.dstu3.model.Enumerations.SearchParamType.NUMBER;
    case DATE: return org.hl7.fhir.dstu3.model.Enumerations.SearchParamType.DATE;
    case STRING: return org.hl7.fhir.dstu3.model.Enumerations.SearchParamType.STRING;
    case TOKEN: return org.hl7.fhir.dstu3.model.Enumerations.SearchParamType.TOKEN;
    case REFERENCE: return org.hl7.fhir.dstu3.model.Enumerations.SearchParamType.REFERENCE;
    case COMPOSITE: return org.hl7.fhir.dstu3.model.Enumerations.SearchParamType.COMPOSITE;
    case QUANTITY: return org.hl7.fhir.dstu3.model.Enumerations.SearchParamType.QUANTITY;
    case URI: return org.hl7.fhir.dstu3.model.Enumerations.SearchParamType.URI;
    default: return org.hl7.fhir.dstu3.model.Enumerations.SearchParamType.NULL;
    }
  }

  public org.hl7.fhir.instance.model.Enumerations.SearchParamType convertSearchParamType(org.hl7.fhir.dstu3.model.Enumerations.SearchParamType src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case NUMBER: return org.hl7.fhir.instance.model.Enumerations.SearchParamType.NUMBER;
    case DATE: return org.hl7.fhir.instance.model.Enumerations.SearchParamType.DATE;
    case STRING: return org.hl7.fhir.instance.model.Enumerations.SearchParamType.STRING;
    case TOKEN: return org.hl7.fhir.instance.model.Enumerations.SearchParamType.TOKEN;
    case REFERENCE: return org.hl7.fhir.instance.model.Enumerations.SearchParamType.REFERENCE;
    case COMPOSITE: return org.hl7.fhir.instance.model.Enumerations.SearchParamType.COMPOSITE;
    case QUANTITY: return org.hl7.fhir.instance.model.Enumerations.SearchParamType.QUANTITY;
    case URI: return org.hl7.fhir.instance.model.Enumerations.SearchParamType.URI;
    default: return org.hl7.fhir.instance.model.Enumerations.SearchParamType.NULL;
    }
  }

  public org.hl7.fhir.dstu3.model.Account convertAccount(org.hl7.fhir.instance.model.Account src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.Account tgt = new org.hl7.fhir.dstu3.model.Account();
    copyDomainResource(src, tgt);
    for (org.hl7.fhir.instance.model.Identifier t : src.getIdentifier())
      tgt.addIdentifier(convertIdentifier(t));
    if (src.hasName())
      tgt.setName(src.getName());
    if (src.hasType())
      tgt.setType(convertCodeableConcept(src.getType()));
    tgt.setStatus(convertAccountStatus(src.getStatus()));
    tgt.setActive(convertPeriod(src.getActivePeriod()));
    tgt.setBalance(convertMoney(src.getBalance()));
//    tgt.setCoveragePeriod(convertPeriod(src.getCoveragePeriod()));
    tgt.setSubject(convertReference(src.getSubject()));
    tgt.setOwner(convertReference(src.getOwner()));
    tgt.setDescription(src.getDescription());
    return tgt;
  }

  public org.hl7.fhir.instance.model.Account convertAccount(org.hl7.fhir.dstu3.model.Account src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.Account tgt = new org.hl7.fhir.instance.model.Account();
    copyDomainResource(src, tgt);
    for (org.hl7.fhir.dstu3.model.Identifier t : src.getIdentifier())
      tgt.addIdentifier(convertIdentifier(t));
    tgt.setName(src.getName());
    tgt.setType(convertCodeableConcept(src.getType()));
    tgt.setStatus(convertAccountStatus(src.getStatus()));
    tgt.setActivePeriod(convertPeriod(src.getActive()));
    tgt.setBalance(convertMoney(src.getBalance()));
//    tgt.setCoveragePeriod(convertPeriod(src.getCoveragePeriod()));
    tgt.setSubject(convertReference(src.getSubject()));
    tgt.setOwner(convertReference(src.getOwner()));
    tgt.setDescription(src.getDescription());
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.Account.AccountStatus convertAccountStatus(org.hl7.fhir.instance.model.Account.AccountStatus src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case ACTIVE: return org.hl7.fhir.dstu3.model.Account.AccountStatus.ACTIVE;
    case INACTIVE: return org.hl7.fhir.dstu3.model.Account.AccountStatus.INACTIVE;
    default: return org.hl7.fhir.dstu3.model.Account.AccountStatus.NULL;
    }
  }

  public org.hl7.fhir.instance.model.Account.AccountStatus convertAccountStatus(org.hl7.fhir.dstu3.model.Account.AccountStatus src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case ACTIVE: return org.hl7.fhir.instance.model.Account.AccountStatus.ACTIVE;
    case INACTIVE: return org.hl7.fhir.instance.model.Account.AccountStatus.INACTIVE;
    default: return org.hl7.fhir.instance.model.Account.AccountStatus.NULL;
    }
  }


  public org.hl7.fhir.dstu3.model.Appointment convertAppointment(org.hl7.fhir.instance.model.Appointment src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.Appointment tgt = new org.hl7.fhir.dstu3.model.Appointment();
    copyDomainResource(src, tgt);
    for (org.hl7.fhir.instance.model.Identifier t : src.getIdentifier())
      tgt.addIdentifier(convertIdentifier(t));
    tgt.setStatus(convertAppointmentStatus(src.getStatus()));
    if (src.hasType())
      tgt.addServiceType(convertCodeableConcept(src.getType()));
//    tgt.setReason(convertCodeableConcept(src.getReason()));
    tgt.setPriority(src.getPriority());
    tgt.setDescription(src.getDescription());
    tgt.setStart(src.getStart());
    tgt.setEnd(src.getEnd());
    tgt.setMinutesDuration(src.getMinutesDuration());
    for (org.hl7.fhir.instance.model.Reference t : src.getSlot())
      tgt.addSlot(convertReference(t));
    tgt.setComment(src.getComment());
    for (org.hl7.fhir.instance.model.Appointment.AppointmentParticipantComponent t : src.getParticipant())
      tgt.addParticipant(convertAppointmentParticipantComponent(t));
    return tgt;
  }

  public org.hl7.fhir.instance.model.Appointment convertAppointment(org.hl7.fhir.dstu3.model.Appointment src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.Appointment tgt = new org.hl7.fhir.instance.model.Appointment();
    copyDomainResource(src, tgt);
    for (org.hl7.fhir.dstu3.model.Identifier t : src.getIdentifier())
      tgt.addIdentifier(convertIdentifier(t));
    tgt.setStatus(convertAppointmentStatus(src.getStatus()));
    for (org.hl7.fhir.dstu3.model.CodeableConcept t : src.getServiceType())
      tgt.setType(convertCodeableConcept(t));
//    tgt.setReason(convertCodeableConcept(src.getReason()));
    tgt.setPriority(src.getPriority());
    tgt.setDescription(src.getDescription());
    tgt.setStart(src.getStart());
    tgt.setEnd(src.getEnd());
    tgt.setMinutesDuration(src.getMinutesDuration());
    for (org.hl7.fhir.dstu3.model.Reference t : src.getSlot())
      tgt.addSlot(convertReference(t));
    tgt.setComment(src.getComment());
    for (org.hl7.fhir.dstu3.model.Appointment.AppointmentParticipantComponent t : src.getParticipant())
      tgt.addParticipant(convertAppointmentParticipantComponent(t));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.Appointment.AppointmentStatus convertAppointmentStatus(org.hl7.fhir.instance.model.Appointment.AppointmentStatus src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case PROPOSED: return org.hl7.fhir.dstu3.model.Appointment.AppointmentStatus.PROPOSED;
    case PENDING: return org.hl7.fhir.dstu3.model.Appointment.AppointmentStatus.PENDING;
    case BOOKED: return org.hl7.fhir.dstu3.model.Appointment.AppointmentStatus.BOOKED;
    case ARRIVED: return org.hl7.fhir.dstu3.model.Appointment.AppointmentStatus.ARRIVED;
    case FULFILLED: return org.hl7.fhir.dstu3.model.Appointment.AppointmentStatus.FULFILLED;
    case CANCELLED: return org.hl7.fhir.dstu3.model.Appointment.AppointmentStatus.CANCELLED;
    case NOSHOW: return org.hl7.fhir.dstu3.model.Appointment.AppointmentStatus.NOSHOW;
    default: return org.hl7.fhir.dstu3.model.Appointment.AppointmentStatus.NULL;
    }
  }

  public org.hl7.fhir.instance.model.Appointment.AppointmentStatus convertAppointmentStatus(org.hl7.fhir.dstu3.model.Appointment.AppointmentStatus src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case PROPOSED: return org.hl7.fhir.instance.model.Appointment.AppointmentStatus.PROPOSED;
    case PENDING: return org.hl7.fhir.instance.model.Appointment.AppointmentStatus.PENDING;
    case BOOKED: return org.hl7.fhir.instance.model.Appointment.AppointmentStatus.BOOKED;
    case ARRIVED: return org.hl7.fhir.instance.model.Appointment.AppointmentStatus.ARRIVED;
    case FULFILLED: return org.hl7.fhir.instance.model.Appointment.AppointmentStatus.FULFILLED;
    case CANCELLED: return org.hl7.fhir.instance.model.Appointment.AppointmentStatus.CANCELLED;
    case NOSHOW: return org.hl7.fhir.instance.model.Appointment.AppointmentStatus.NOSHOW;
    default: return org.hl7.fhir.instance.model.Appointment.AppointmentStatus.NULL;
    }
  }

  public org.hl7.fhir.dstu3.model.Appointment.AppointmentParticipantComponent convertAppointmentParticipantComponent(org.hl7.fhir.instance.model.Appointment.AppointmentParticipantComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.Appointment.AppointmentParticipantComponent tgt = new org.hl7.fhir.dstu3.model.Appointment.AppointmentParticipantComponent();
    copyElement(src, tgt);
    for (org.hl7.fhir.instance.model.CodeableConcept t : src.getType())
      tgt.addType(convertCodeableConcept(t));
    tgt.setActor(convertReference(src.getActor()));
    tgt.setRequired(convertParticipantRequired(src.getRequired()));
    tgt.setStatus(convertParticipationStatus(src.getStatus()));
    return tgt;
  }

  public org.hl7.fhir.instance.model.Appointment.AppointmentParticipantComponent convertAppointmentParticipantComponent(org.hl7.fhir.dstu3.model.Appointment.AppointmentParticipantComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.Appointment.AppointmentParticipantComponent tgt = new org.hl7.fhir.instance.model.Appointment.AppointmentParticipantComponent();
    copyElement(src, tgt);
    for (org.hl7.fhir.dstu3.model.CodeableConcept t : src.getType())
      tgt.addType(convertCodeableConcept(t));
    tgt.setActor(convertReference(src.getActor()));
    tgt.setRequired(convertParticipantRequired(src.getRequired()));
    tgt.setStatus(convertParticipationStatus(src.getStatus()));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.Appointment.ParticipantRequired convertParticipantRequired(org.hl7.fhir.instance.model.Appointment.ParticipantRequired src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case REQUIRED: return org.hl7.fhir.dstu3.model.Appointment.ParticipantRequired.REQUIRED;
    case OPTIONAL: return org.hl7.fhir.dstu3.model.Appointment.ParticipantRequired.OPTIONAL;
    case INFORMATIONONLY: return org.hl7.fhir.dstu3.model.Appointment.ParticipantRequired.INFORMATIONONLY;
    default: return org.hl7.fhir.dstu3.model.Appointment.ParticipantRequired.NULL;
    }
  }

  public org.hl7.fhir.instance.model.Appointment.ParticipantRequired convertParticipantRequired(org.hl7.fhir.dstu3.model.Appointment.ParticipantRequired src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case REQUIRED: return org.hl7.fhir.instance.model.Appointment.ParticipantRequired.REQUIRED;
    case OPTIONAL: return org.hl7.fhir.instance.model.Appointment.ParticipantRequired.OPTIONAL;
    case INFORMATIONONLY: return org.hl7.fhir.instance.model.Appointment.ParticipantRequired.INFORMATIONONLY;
    default: return org.hl7.fhir.instance.model.Appointment.ParticipantRequired.NULL;
    }
  }

  public org.hl7.fhir.dstu3.model.Appointment.ParticipationStatus convertParticipationStatus(org.hl7.fhir.instance.model.Appointment.ParticipationStatus src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case ACCEPTED: return org.hl7.fhir.dstu3.model.Appointment.ParticipationStatus.ACCEPTED;
    case DECLINED: return org.hl7.fhir.dstu3.model.Appointment.ParticipationStatus.DECLINED;
    case TENTATIVE: return org.hl7.fhir.dstu3.model.Appointment.ParticipationStatus.TENTATIVE;
    case NEEDSACTION: return org.hl7.fhir.dstu3.model.Appointment.ParticipationStatus.NEEDSACTION;
    default: return org.hl7.fhir.dstu3.model.Appointment.ParticipationStatus.NULL;
    }
  }

  public org.hl7.fhir.instance.model.Appointment.ParticipationStatus convertParticipationStatus(org.hl7.fhir.dstu3.model.Appointment.ParticipationStatus src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case ACCEPTED: return org.hl7.fhir.instance.model.Appointment.ParticipationStatus.ACCEPTED;
    case DECLINED: return org.hl7.fhir.instance.model.Appointment.ParticipationStatus.DECLINED;
    case TENTATIVE: return org.hl7.fhir.instance.model.Appointment.ParticipationStatus.TENTATIVE;
    case NEEDSACTION: return org.hl7.fhir.instance.model.Appointment.ParticipationStatus.NEEDSACTION;
    default: return org.hl7.fhir.instance.model.Appointment.ParticipationStatus.NULL;
    }
  }

  public org.hl7.fhir.dstu3.model.AppointmentResponse convertAppointmentResponse(org.hl7.fhir.instance.model.AppointmentResponse src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.AppointmentResponse tgt = new org.hl7.fhir.dstu3.model.AppointmentResponse();
    copyDomainResource(src, tgt);
    for (org.hl7.fhir.instance.model.Identifier t : src.getIdentifier())
      tgt.addIdentifier(convertIdentifier(t));
    tgt.setAppointment(convertReference(src.getAppointment()));
    tgt.setStart(src.getStart());
    tgt.setEnd(src.getEnd());
    for (org.hl7.fhir.instance.model.CodeableConcept t : src.getParticipantType())
      tgt.addParticipantType(convertCodeableConcept(t));
    tgt.setActor(convertReference(src.getActor()));
    tgt.setParticipantStatus(convertParticipantStatus(src.getParticipantStatus()));
    tgt.setComment(src.getComment());
    return tgt;
  }

  private org.hl7.fhir.dstu3.model.AppointmentResponse.ParticipantStatus convertParticipantStatus(org.hl7.fhir.instance.model.AppointmentResponse.ParticipantStatus src) {
    if (src == null)
      return null;
    switch (src) {
    case ACCEPTED: return org.hl7.fhir.dstu3.model.AppointmentResponse.ParticipantStatus.ACCEPTED;
    case DECLINED: return org.hl7.fhir.dstu3.model.AppointmentResponse.ParticipantStatus.DECLINED;
    case TENTATIVE:  return org.hl7.fhir.dstu3.model.AppointmentResponse.ParticipantStatus.TENTATIVE;
    case INPROCESS:  return org.hl7.fhir.dstu3.model.AppointmentResponse.ParticipantStatus.ACCEPTED;
    case COMPLETED:  return org.hl7.fhir.dstu3.model.AppointmentResponse.ParticipantStatus.ACCEPTED;
    case NEEDSACTION:  return org.hl7.fhir.dstu3.model.AppointmentResponse.ParticipantStatus.NEEDSACTION;
    default:  return org.hl7.fhir.dstu3.model.AppointmentResponse.ParticipantStatus.NULL;
    }
  }

  private org.hl7.fhir.instance.model.AppointmentResponse.ParticipantStatus convertParticipantStatus(org.hl7.fhir.dstu3.model.AppointmentResponse.ParticipantStatus src) {
    if (src == null)
      return null;
    switch (src) {
    case ACCEPTED: return org.hl7.fhir.instance.model.AppointmentResponse.ParticipantStatus.ACCEPTED;
    case DECLINED: return org.hl7.fhir.instance.model.AppointmentResponse.ParticipantStatus.DECLINED;
    case TENTATIVE:  return org.hl7.fhir.instance.model.AppointmentResponse.ParticipantStatus.TENTATIVE;
    case NEEDSACTION:  return org.hl7.fhir.instance.model.AppointmentResponse.ParticipantStatus.NEEDSACTION;
    default:  return org.hl7.fhir.instance.model.AppointmentResponse.ParticipantStatus.NULL;
    }
  }

  public org.hl7.fhir.instance.model.AppointmentResponse convertAppointmentResponse(org.hl7.fhir.dstu3.model.AppointmentResponse src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.AppointmentResponse tgt = new org.hl7.fhir.instance.model.AppointmentResponse();
    copyDomainResource(src, tgt);
    for (org.hl7.fhir.dstu3.model.Identifier t : src.getIdentifier())
      tgt.addIdentifier(convertIdentifier(t));
    tgt.setAppointment(convertReference(src.getAppointment()));
    tgt.setStart(src.getStart());
    tgt.setEnd(src.getEnd());
    for (org.hl7.fhir.dstu3.model.CodeableConcept t : src.getParticipantType())
      tgt.addParticipantType(convertCodeableConcept(t));
    tgt.setActor(convertReference(src.getActor()));
    tgt.setParticipantStatus(convertParticipantStatus(src.getParticipantStatus()));
    tgt.setComment(src.getComment());
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.AuditEvent convertAuditEvent(org.hl7.fhir.instance.model.AuditEvent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.AuditEvent tgt = new org.hl7.fhir.dstu3.model.AuditEvent();
    copyDomainResource(src, tgt);
    if (src.hasEvent()) {
      tgt.setType(convertCoding(src.getEvent().getType()));
      for (org.hl7.fhir.instance.model.Coding t : src.getEvent().getSubtype())
        tgt.addSubtype(convertCoding(t));
      tgt.setAction(convertAuditEventAction(src.getEvent().getAction()));
      tgt.setRecorded(src.getEvent().getDateTime());
      tgt.setOutcome(convertAuditEventOutcome(src.getEvent().getOutcome()));
      tgt.setOutcomeDesc(src.getEvent().getOutcomeDesc());
      for (org.hl7.fhir.instance.model.Coding t : src.getEvent().getPurposeOfEvent())
        tgt.addPurposeOfEvent().addCoding(convertCoding(t));
    }
    for (org.hl7.fhir.instance.model.AuditEvent.AuditEventParticipantComponent t : src.getParticipant())
      tgt.addAgent(convertAuditEventAgentComponent(t));
    tgt.setSource(convertAuditEventSourceComponent(src.getSource()));
    for (org.hl7.fhir.instance.model.AuditEvent.AuditEventObjectComponent t : src.getObject())
      tgt.addEntity(convertAuditEventEntityComponent(t));
    return tgt;
  }

  public org.hl7.fhir.instance.model.AuditEvent convertAuditEvent(org.hl7.fhir.dstu3.model.AuditEvent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.AuditEvent tgt = new org.hl7.fhir.instance.model.AuditEvent();
    copyDomainResource(src, tgt);
    tgt.getEvent().setType(convertCoding(src.getType()));
    for (org.hl7.fhir.dstu3.model.Coding t : src.getSubtype())
      tgt.getEvent().addSubtype(convertCoding(t));
    tgt.getEvent().setAction(convertAuditEventAction(src.getAction()));
    tgt.getEvent().setDateTime(src.getRecorded());
    tgt.getEvent().setOutcome(convertAuditEventOutcome(src.getOutcome()));
    tgt.getEvent().setOutcomeDesc(src.getOutcomeDesc());
    for (org.hl7.fhir.dstu3.model.CodeableConcept t : src.getPurposeOfEvent())
      for (org.hl7.fhir.dstu3.model.Coding cc : t.getCoding())
        tgt.getEvent().addPurposeOfEvent(convertCoding(cc));
    for (org.hl7.fhir.dstu3.model.AuditEvent.AuditEventAgentComponent t : src.getAgent())
      tgt.addParticipant(convertAuditEventAgentComponent(t));
    tgt.setSource(convertAuditEventSourceComponent(src.getSource()));
    for (org.hl7.fhir.dstu3.model.AuditEvent.AuditEventEntityComponent t : src.getEntity())
      tgt.addObject(convertAuditEventEntityComponent(t));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.AuditEvent.AuditEventAction convertAuditEventAction(org.hl7.fhir.instance.model.AuditEvent.AuditEventAction src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case C: return org.hl7.fhir.dstu3.model.AuditEvent.AuditEventAction.C;
    case R: return org.hl7.fhir.dstu3.model.AuditEvent.AuditEventAction.R;
    case U: return org.hl7.fhir.dstu3.model.AuditEvent.AuditEventAction.U;
    case D: return org.hl7.fhir.dstu3.model.AuditEvent.AuditEventAction.D;
    case E: return org.hl7.fhir.dstu3.model.AuditEvent.AuditEventAction.E;
    default: return org.hl7.fhir.dstu3.model.AuditEvent.AuditEventAction.NULL;
    }
  }

  public org.hl7.fhir.instance.model.AuditEvent.AuditEventAction convertAuditEventAction(org.hl7.fhir.dstu3.model.AuditEvent.AuditEventAction src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case C: return org.hl7.fhir.instance.model.AuditEvent.AuditEventAction.C;
    case R: return org.hl7.fhir.instance.model.AuditEvent.AuditEventAction.R;
    case U: return org.hl7.fhir.instance.model.AuditEvent.AuditEventAction.U;
    case D: return org.hl7.fhir.instance.model.AuditEvent.AuditEventAction.D;
    case E: return org.hl7.fhir.instance.model.AuditEvent.AuditEventAction.E;
    default: return org.hl7.fhir.instance.model.AuditEvent.AuditEventAction.NULL;
    }
  }

  public org.hl7.fhir.dstu3.model.AuditEvent.AuditEventOutcome convertAuditEventOutcome(org.hl7.fhir.instance.model.AuditEvent.AuditEventOutcome src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case _0: return org.hl7.fhir.dstu3.model.AuditEvent.AuditEventOutcome._0;
    case _4: return org.hl7.fhir.dstu3.model.AuditEvent.AuditEventOutcome._4;
    case _8: return org.hl7.fhir.dstu3.model.AuditEvent.AuditEventOutcome._8;
    case _12: return org.hl7.fhir.dstu3.model.AuditEvent.AuditEventOutcome._12;
    default: return org.hl7.fhir.dstu3.model.AuditEvent.AuditEventOutcome.NULL;
    }
  }

  public org.hl7.fhir.instance.model.AuditEvent.AuditEventOutcome convertAuditEventOutcome(org.hl7.fhir.dstu3.model.AuditEvent.AuditEventOutcome src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case _0: return org.hl7.fhir.instance.model.AuditEvent.AuditEventOutcome._0;
    case _4: return org.hl7.fhir.instance.model.AuditEvent.AuditEventOutcome._4;
    case _8: return org.hl7.fhir.instance.model.AuditEvent.AuditEventOutcome._8;
    case _12: return org.hl7.fhir.instance.model.AuditEvent.AuditEventOutcome._12;
    default: return org.hl7.fhir.instance.model.AuditEvent.AuditEventOutcome.NULL;
    }
  }

  public org.hl7.fhir.dstu3.model.AuditEvent.AuditEventAgentComponent convertAuditEventAgentComponent(org.hl7.fhir.instance.model.AuditEvent.AuditEventParticipantComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.AuditEvent.AuditEventAgentComponent tgt = new org.hl7.fhir.dstu3.model.AuditEvent.AuditEventAgentComponent();
    copyElement(src, tgt);
    for (org.hl7.fhir.instance.model.CodeableConcept t : src.getRole())
      tgt.addRole(convertCodeableConcept(t));
    tgt.setReference(convertReference(src.getReference()));
    tgt.setUserId(convertIdentifier(src.getUserId()));
    tgt.setAltId(src.getAltId());
    tgt.setName(src.getName());
    tgt.setRequestor(src.getRequestor());
    tgt.setLocation(convertReference(src.getLocation()));
    for (org.hl7.fhir.instance.model.UriType t : src.getPolicy())
      tgt.addPolicy(t.getValue());
    tgt.setMedia(convertCoding(src.getMedia()));
    tgt.setNetwork(convertAuditEventAgentNetworkComponent(src.getNetwork()));
    for (org.hl7.fhir.instance.model.Coding t : src.getPurposeOfUse())
      tgt.addPurposeOfUse().addCoding(convertCoding(t));
    return tgt;
  }

  public org.hl7.fhir.instance.model.AuditEvent.AuditEventParticipantComponent convertAuditEventAgentComponent(org.hl7.fhir.dstu3.model.AuditEvent.AuditEventAgentComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.AuditEvent.AuditEventParticipantComponent tgt = new org.hl7.fhir.instance.model.AuditEvent.AuditEventParticipantComponent();
    copyElement(src, tgt);
    for (org.hl7.fhir.dstu3.model.CodeableConcept t : src.getRole())
      tgt.addRole(convertCodeableConcept(t));
    tgt.setReference(convertReference(src.getReference()));
    tgt.setUserId(convertIdentifier(src.getUserId()));
    tgt.setAltId(src.getAltId());
    tgt.setName(src.getName());
    tgt.setRequestor(src.getRequestor());
    tgt.setLocation(convertReference(src.getLocation()));
    for (org.hl7.fhir.dstu3.model.UriType t : src.getPolicy())
      tgt.addPolicy(t.getValue());
    tgt.setMedia(convertCoding(src.getMedia()));
    tgt.setNetwork(convertAuditEventAgentNetworkComponent(src.getNetwork()));
    for (org.hl7.fhir.dstu3.model.CodeableConcept t : src.getPurposeOfUse())
      for (org.hl7.fhir.dstu3.model.Coding cc : t.getCoding())
      tgt.addPurposeOfUse(convertCoding(cc));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.AuditEvent.AuditEventAgentNetworkComponent convertAuditEventAgentNetworkComponent(org.hl7.fhir.instance.model.AuditEvent.AuditEventParticipantNetworkComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.AuditEvent.AuditEventAgentNetworkComponent tgt = new org.hl7.fhir.dstu3.model.AuditEvent.AuditEventAgentNetworkComponent();
    copyElement(src, tgt);
    tgt.setAddress(src.getAddress());
    tgt.setType(convertAuditEventParticipantNetworkType(src.getType()));
    return tgt;
  }

  public org.hl7.fhir.instance.model.AuditEvent.AuditEventParticipantNetworkComponent convertAuditEventAgentNetworkComponent(org.hl7.fhir.dstu3.model.AuditEvent.AuditEventAgentNetworkComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.AuditEvent.AuditEventParticipantNetworkComponent tgt = new org.hl7.fhir.instance.model.AuditEvent.AuditEventParticipantNetworkComponent();
    copyElement(src, tgt);
    tgt.setAddress(src.getAddress());
    tgt.setType(convertAuditEventParticipantNetworkType(src.getType()));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.AuditEvent.AuditEventAgentNetworkType convertAuditEventParticipantNetworkType(org.hl7.fhir.instance.model.AuditEvent.AuditEventParticipantNetworkType src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case _1: return org.hl7.fhir.dstu3.model.AuditEvent.AuditEventAgentNetworkType._1;
    case _2: return org.hl7.fhir.dstu3.model.AuditEvent.AuditEventAgentNetworkType._2;
    case _3: return org.hl7.fhir.dstu3.model.AuditEvent.AuditEventAgentNetworkType._3;
    case _4: return org.hl7.fhir.dstu3.model.AuditEvent.AuditEventAgentNetworkType._4;
    case _5: return org.hl7.fhir.dstu3.model.AuditEvent.AuditEventAgentNetworkType._5;
    default: return org.hl7.fhir.dstu3.model.AuditEvent.AuditEventAgentNetworkType.NULL;
    }
  }

  public org.hl7.fhir.instance.model.AuditEvent.AuditEventParticipantNetworkType convertAuditEventParticipantNetworkType(org.hl7.fhir.dstu3.model.AuditEvent.AuditEventAgentNetworkType src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case _1: return org.hl7.fhir.instance.model.AuditEvent.AuditEventParticipantNetworkType._1;
    case _2: return org.hl7.fhir.instance.model.AuditEvent.AuditEventParticipantNetworkType._2;
    case _3: return org.hl7.fhir.instance.model.AuditEvent.AuditEventParticipantNetworkType._3;
    case _4: return org.hl7.fhir.instance.model.AuditEvent.AuditEventParticipantNetworkType._4;
    case _5: return org.hl7.fhir.instance.model.AuditEvent.AuditEventParticipantNetworkType._5;
    default: return org.hl7.fhir.instance.model.AuditEvent.AuditEventParticipantNetworkType.NULL;
    }
  }

  public org.hl7.fhir.dstu3.model.AuditEvent.AuditEventSourceComponent convertAuditEventSourceComponent(org.hl7.fhir.instance.model.AuditEvent.AuditEventSourceComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.AuditEvent.AuditEventSourceComponent tgt = new org.hl7.fhir.dstu3.model.AuditEvent.AuditEventSourceComponent();
    copyElement(src, tgt);
    tgt.setSite(src.getSite());
    tgt.setIdentifier(convertIdentifier(src.getIdentifier()));
    for (org.hl7.fhir.instance.model.Coding t : src.getType())
      tgt.addType(convertCoding(t));
    return tgt;
  }

  public org.hl7.fhir.instance.model.AuditEvent.AuditEventSourceComponent convertAuditEventSourceComponent(org.hl7.fhir.dstu3.model.AuditEvent.AuditEventSourceComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.AuditEvent.AuditEventSourceComponent tgt = new org.hl7.fhir.instance.model.AuditEvent.AuditEventSourceComponent();
    copyElement(src, tgt);
    tgt.setSite(src.getSite());
    tgt.setIdentifier(convertIdentifier(src.getIdentifier()));
    for (org.hl7.fhir.dstu3.model.Coding t : src.getType())
      tgt.addType(convertCoding(t));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.AuditEvent.AuditEventEntityComponent convertAuditEventEntityComponent(org.hl7.fhir.instance.model.AuditEvent.AuditEventObjectComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.AuditEvent.AuditEventEntityComponent tgt = new org.hl7.fhir.dstu3.model.AuditEvent.AuditEventEntityComponent();
    copyElement(src, tgt);
    tgt.setIdentifier(convertIdentifier(src.getIdentifier()));
    tgt.setReference(convertReference(src.getReference()));
    tgt.setType(convertCoding(src.getType()));
    tgt.setRole(convertCoding(src.getRole()));
    tgt.setLifecycle(convertCoding(src.getLifecycle()));
    for (org.hl7.fhir.instance.model.Coding t : src.getSecurityLabel())
      tgt.addSecurityLabel(convertCoding(t));
    tgt.setName(src.getName());
    tgt.setDescription(src.getDescription());
    tgt.setQuery(src.getQuery());
    for (org.hl7.fhir.instance.model.AuditEvent.AuditEventObjectDetailComponent t : src.getDetail())
      tgt.addDetail(convertAuditEventEntityDetailComponent(t));
    return tgt;
  }

  public org.hl7.fhir.instance.model.AuditEvent.AuditEventObjectComponent convertAuditEventEntityComponent(org.hl7.fhir.dstu3.model.AuditEvent.AuditEventEntityComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.AuditEvent.AuditEventObjectComponent tgt = new org.hl7.fhir.instance.model.AuditEvent.AuditEventObjectComponent();
    copyElement(src, tgt);
    tgt.setIdentifier(convertIdentifier(src.getIdentifier()));
    tgt.setReference(convertReference(src.getReference()));
    tgt.setType(convertCoding(src.getType()));
    tgt.setRole(convertCoding(src.getRole()));
    tgt.setLifecycle(convertCoding(src.getLifecycle()));
    for (org.hl7.fhir.dstu3.model.Coding t : src.getSecurityLabel())
      tgt.addSecurityLabel(convertCoding(t));
    tgt.setName(src.getName());
    tgt.setDescription(src.getDescription());
    tgt.setQuery(src.getQuery());
    for (org.hl7.fhir.dstu3.model.AuditEvent.AuditEventEntityDetailComponent t : src.getDetail())
      tgt.addDetail(convertAuditEventEntityDetailComponent(t));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.AuditEvent.AuditEventEntityDetailComponent convertAuditEventEntityDetailComponent(org.hl7.fhir.instance.model.AuditEvent.AuditEventObjectDetailComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.AuditEvent.AuditEventEntityDetailComponent tgt = new org.hl7.fhir.dstu3.model.AuditEvent.AuditEventEntityDetailComponent();
    copyElement(src, tgt);
    tgt.setType(src.getType());
    tgt.setValue(src.getValue());
    return tgt;
  }

  public org.hl7.fhir.instance.model.AuditEvent.AuditEventObjectDetailComponent convertAuditEventEntityDetailComponent(org.hl7.fhir.dstu3.model.AuditEvent.AuditEventEntityDetailComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.AuditEvent.AuditEventObjectDetailComponent tgt = new org.hl7.fhir.instance.model.AuditEvent.AuditEventObjectDetailComponent();
    copyElement(src, tgt);
    tgt.setType(src.getType());
    tgt.setValue(src.getValue());
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.Basic convertBasic(org.hl7.fhir.instance.model.Basic src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.Basic tgt = new org.hl7.fhir.dstu3.model.Basic();
    copyDomainResource(src, tgt);
    for (org.hl7.fhir.instance.model.Identifier t : src.getIdentifier())
      tgt.addIdentifier(convertIdentifier(t));
    tgt.setCode(convertCodeableConcept(src.getCode()));
    tgt.setSubject(convertReference(src.getSubject()));
    tgt.setCreated(src.getCreated());
    tgt.setAuthor(convertReference(src.getAuthor()));
    return tgt;
  }

  public org.hl7.fhir.instance.model.Basic convertBasic(org.hl7.fhir.dstu3.model.Basic src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.Basic tgt = new org.hl7.fhir.instance.model.Basic();
    copyDomainResource(src, tgt);
    for (org.hl7.fhir.dstu3.model.Identifier t : src.getIdentifier())
      tgt.addIdentifier(convertIdentifier(t));
    tgt.setCode(convertCodeableConcept(src.getCode()));
    tgt.setSubject(convertReference(src.getSubject()));
    tgt.setCreated(src.getCreated());
    tgt.setAuthor(convertReference(src.getAuthor()));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.Binary convertBinary(org.hl7.fhir.instance.model.Binary src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.Binary tgt = new org.hl7.fhir.dstu3.model.Binary();
    copyResource(src, tgt);
    tgt.setContentType(src.getContentType());
    tgt.setContent(src.getContent());
    return tgt;
  }

  public org.hl7.fhir.instance.model.Binary convertBinary(org.hl7.fhir.dstu3.model.Binary src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.Binary tgt = new org.hl7.fhir.instance.model.Binary();
    copyResource(src, tgt);
    tgt.setContentType(src.getContentType());
    tgt.setContent(src.getContent());
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.Bundle convertBundle(org.hl7.fhir.instance.model.Bundle src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.Bundle tgt = new org.hl7.fhir.dstu3.model.Bundle();
    copyResource(src, tgt);
    tgt.setType(convertBundleType(src.getType()));
    if (src.hasTotal())
      tgt.setTotal(src.getTotal());
    for (org.hl7.fhir.instance.model.Bundle.BundleLinkComponent t : src.getLink())
      tgt.addLink(convertBundleLinkComponent(t));
    for (org.hl7.fhir.instance.model.Bundle.BundleEntryComponent t : src.getEntry())
      tgt.addEntry(convertBundleEntryComponent(t));
    tgt.setSignature(convertSignature(src.getSignature()));
    return tgt;
  }

  public org.hl7.fhir.instance.model.Bundle convertBundle(org.hl7.fhir.dstu3.model.Bundle src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.Bundle tgt = new org.hl7.fhir.instance.model.Bundle();
    copyResource(src, tgt);
    tgt.setType(convertBundleType(src.getType()));
    if (src.hasTotal())
      tgt.setTotal(src.getTotal());
    for (org.hl7.fhir.dstu3.model.Bundle.BundleLinkComponent t : src.getLink())
      tgt.addLink(convertBundleLinkComponent(t));
    for (org.hl7.fhir.dstu3.model.Bundle.BundleEntryComponent t : src.getEntry())
      tgt.addEntry(convertBundleEntryComponent(t));
    if (src.hasSignature())
      tgt.setSignature(convertSignature(src.getSignature()));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.Bundle.BundleType convertBundleType(org.hl7.fhir.instance.model.Bundle.BundleType src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case DOCUMENT: return org.hl7.fhir.dstu3.model.Bundle.BundleType.DOCUMENT;
    case MESSAGE: return org.hl7.fhir.dstu3.model.Bundle.BundleType.MESSAGE;
    case TRANSACTION: return org.hl7.fhir.dstu3.model.Bundle.BundleType.TRANSACTION;
    case TRANSACTIONRESPONSE: return org.hl7.fhir.dstu3.model.Bundle.BundleType.TRANSACTIONRESPONSE;
    case BATCH: return org.hl7.fhir.dstu3.model.Bundle.BundleType.BATCH;
    case BATCHRESPONSE: return org.hl7.fhir.dstu3.model.Bundle.BundleType.BATCHRESPONSE;
    case HISTORY: return org.hl7.fhir.dstu3.model.Bundle.BundleType.HISTORY;
    case SEARCHSET: return org.hl7.fhir.dstu3.model.Bundle.BundleType.SEARCHSET;
    case COLLECTION: return org.hl7.fhir.dstu3.model.Bundle.BundleType.COLLECTION;
    default: return org.hl7.fhir.dstu3.model.Bundle.BundleType.NULL;
    }
  }

  public org.hl7.fhir.instance.model.Bundle.BundleType convertBundleType(org.hl7.fhir.dstu3.model.Bundle.BundleType src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case DOCUMENT: return org.hl7.fhir.instance.model.Bundle.BundleType.DOCUMENT;
    case MESSAGE: return org.hl7.fhir.instance.model.Bundle.BundleType.MESSAGE;
    case TRANSACTION: return org.hl7.fhir.instance.model.Bundle.BundleType.TRANSACTION;
    case TRANSACTIONRESPONSE: return org.hl7.fhir.instance.model.Bundle.BundleType.TRANSACTIONRESPONSE;
    case BATCH: return org.hl7.fhir.instance.model.Bundle.BundleType.BATCH;
    case BATCHRESPONSE: return org.hl7.fhir.instance.model.Bundle.BundleType.BATCHRESPONSE;
    case HISTORY: return org.hl7.fhir.instance.model.Bundle.BundleType.HISTORY;
    case SEARCHSET: return org.hl7.fhir.instance.model.Bundle.BundleType.SEARCHSET;
    case COLLECTION: return org.hl7.fhir.instance.model.Bundle.BundleType.COLLECTION;
    default: return org.hl7.fhir.instance.model.Bundle.BundleType.NULL;
    }
  }

  public org.hl7.fhir.dstu3.model.Bundle.BundleLinkComponent convertBundleLinkComponent(org.hl7.fhir.instance.model.Bundle.BundleLinkComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.Bundle.BundleLinkComponent tgt = new org.hl7.fhir.dstu3.model.Bundle.BundleLinkComponent();
    copyElement(src, tgt);
    tgt.setRelation(src.getRelation());
    tgt.setUrl(src.getUrl());
    return tgt;
  }

  public org.hl7.fhir.instance.model.Bundle.BundleLinkComponent convertBundleLinkComponent(org.hl7.fhir.dstu3.model.Bundle.BundleLinkComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.Bundle.BundleLinkComponent tgt = new org.hl7.fhir.instance.model.Bundle.BundleLinkComponent();
    copyElement(src, tgt);
    tgt.setRelation(src.getRelation());
    tgt.setUrl(src.getUrl());
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.Bundle.BundleEntryComponent convertBundleEntryComponent(org.hl7.fhir.instance.model.Bundle.BundleEntryComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.Bundle.BundleEntryComponent tgt = new org.hl7.fhir.dstu3.model.Bundle.BundleEntryComponent();
    copyElement(src, tgt);
    for (org.hl7.fhir.instance.model.Bundle.BundleLinkComponent t : src.getLink())
      tgt.addLink(convertBundleLinkComponent(t));
    tgt.setFullUrl(src.getFullUrl());
    tgt.setResource(convertResource(src.getResource()));
    tgt.setSearch(convertBundleEntrySearchComponent(src.getSearch()));
    tgt.setRequest(convertBundleEntryRequestComponent(src.getRequest()));
    tgt.setResponse(convertBundleEntryResponseComponent(src.getResponse()));
    return tgt;
  }

  public org.hl7.fhir.instance.model.Bundle.BundleEntryComponent convertBundleEntryComponent(org.hl7.fhir.dstu3.model.Bundle.BundleEntryComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    if (advisor.ignoreEntry(src))
      return null;

    org.hl7.fhir.instance.model.Bundle.BundleEntryComponent tgt = new org.hl7.fhir.instance.model.Bundle.BundleEntryComponent();
    copyElement(src, tgt);
    for (org.hl7.fhir.dstu3.model.Bundle.BundleLinkComponent t : src.getLink())
      tgt.addLink(convertBundleLinkComponent(t));
    tgt.setFullUrl(src.getFullUrl());
    org.hl7.fhir.instance.model.Resource res = advisor.convert(src.getResource());
    if (res == null)
      res = convertResource(src.getResource());
    tgt.setResource(res);
    if (src.hasSearch())
      tgt.setSearch(convertBundleEntrySearchComponent(src.getSearch()));
    if (src.hasRequest())
      tgt.setRequest(convertBundleEntryRequestComponent(src.getRequest()));
    if (src.hasResponse())
      tgt.setResponse(convertBundleEntryResponseComponent(src.getResponse()));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.Bundle.BundleEntrySearchComponent convertBundleEntrySearchComponent(org.hl7.fhir.instance.model.Bundle.BundleEntrySearchComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.Bundle.BundleEntrySearchComponent tgt = new org.hl7.fhir.dstu3.model.Bundle.BundleEntrySearchComponent();
    copyElement(src, tgt);
    tgt.setMode(convertSearchEntryMode(src.getMode()));
    tgt.setScore(src.getScore());
    return tgt;
  }

  public org.hl7.fhir.instance.model.Bundle.BundleEntrySearchComponent convertBundleEntrySearchComponent(org.hl7.fhir.dstu3.model.Bundle.BundleEntrySearchComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.Bundle.BundleEntrySearchComponent tgt = new org.hl7.fhir.instance.model.Bundle.BundleEntrySearchComponent();
    copyElement(src, tgt);
    tgt.setMode(convertSearchEntryMode(src.getMode()));
    tgt.setScore(src.getScore());
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.Bundle.SearchEntryMode convertSearchEntryMode(org.hl7.fhir.instance.model.Bundle.SearchEntryMode src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case MATCH: return org.hl7.fhir.dstu3.model.Bundle.SearchEntryMode.MATCH;
    case INCLUDE: return org.hl7.fhir.dstu3.model.Bundle.SearchEntryMode.INCLUDE;
    case OUTCOME: return org.hl7.fhir.dstu3.model.Bundle.SearchEntryMode.OUTCOME;
    default: return org.hl7.fhir.dstu3.model.Bundle.SearchEntryMode.NULL;
    }
  }

  public org.hl7.fhir.instance.model.Bundle.SearchEntryMode convertSearchEntryMode(org.hl7.fhir.dstu3.model.Bundle.SearchEntryMode src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case MATCH: return org.hl7.fhir.instance.model.Bundle.SearchEntryMode.MATCH;
    case INCLUDE: return org.hl7.fhir.instance.model.Bundle.SearchEntryMode.INCLUDE;
    case OUTCOME: return org.hl7.fhir.instance.model.Bundle.SearchEntryMode.OUTCOME;
    default: return org.hl7.fhir.instance.model.Bundle.SearchEntryMode.NULL;
    }
  }

  public org.hl7.fhir.dstu3.model.Bundle.BundleEntryRequestComponent convertBundleEntryRequestComponent(org.hl7.fhir.instance.model.Bundle.BundleEntryRequestComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.Bundle.BundleEntryRequestComponent tgt = new org.hl7.fhir.dstu3.model.Bundle.BundleEntryRequestComponent();
    copyElement(src, tgt);
    tgt.setMethod(convertHTTPVerb(src.getMethod()));
    tgt.setUrl(src.getUrl());
    tgt.setIfNoneMatch(src.getIfNoneMatch());
    tgt.setIfModifiedSince(src.getIfModifiedSince());
    tgt.setIfMatch(src.getIfMatch());
    tgt.setIfNoneExist(src.getIfNoneExist());
    return tgt;
  }

  public org.hl7.fhir.instance.model.Bundle.BundleEntryRequestComponent convertBundleEntryRequestComponent(org.hl7.fhir.dstu3.model.Bundle.BundleEntryRequestComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.Bundle.BundleEntryRequestComponent tgt = new org.hl7.fhir.instance.model.Bundle.BundleEntryRequestComponent();
    copyElement(src, tgt);
    tgt.setMethod(convertHTTPVerb(src.getMethod()));
    tgt.setUrl(src.getUrl());
    tgt.setIfNoneMatch(src.getIfNoneMatch());
    tgt.setIfModifiedSince(src.getIfModifiedSince());
    tgt.setIfMatch(src.getIfMatch());
    tgt.setIfNoneExist(src.getIfNoneExist());
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.Bundle.HTTPVerb convertHTTPVerb(org.hl7.fhir.instance.model.Bundle.HTTPVerb src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case GET: return org.hl7.fhir.dstu3.model.Bundle.HTTPVerb.GET;
    case POST: return org.hl7.fhir.dstu3.model.Bundle.HTTPVerb.POST;
    case PUT: return org.hl7.fhir.dstu3.model.Bundle.HTTPVerb.PUT;
    case DELETE: return org.hl7.fhir.dstu3.model.Bundle.HTTPVerb.DELETE;
    default: return org.hl7.fhir.dstu3.model.Bundle.HTTPVerb.NULL;
    }
  }

  public org.hl7.fhir.instance.model.Bundle.HTTPVerb convertHTTPVerb(org.hl7.fhir.dstu3.model.Bundle.HTTPVerb src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case GET: return org.hl7.fhir.instance.model.Bundle.HTTPVerb.GET;
    case POST: return org.hl7.fhir.instance.model.Bundle.HTTPVerb.POST;
    case PUT: return org.hl7.fhir.instance.model.Bundle.HTTPVerb.PUT;
    case DELETE: return org.hl7.fhir.instance.model.Bundle.HTTPVerb.DELETE;
    default: return org.hl7.fhir.instance.model.Bundle.HTTPVerb.NULL;
    }
  }

  public org.hl7.fhir.dstu3.model.Bundle.BundleEntryResponseComponent convertBundleEntryResponseComponent(org.hl7.fhir.instance.model.Bundle.BundleEntryResponseComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.Bundle.BundleEntryResponseComponent tgt = new org.hl7.fhir.dstu3.model.Bundle.BundleEntryResponseComponent();
    copyElement(src, tgt);
    tgt.setStatus(src.getStatus());
    tgt.setLocation(src.getLocation());
    tgt.setEtag(src.getEtag());
    tgt.setLastModified(src.getLastModified());
    return tgt;
  }

  public org.hl7.fhir.instance.model.Bundle.BundleEntryResponseComponent convertBundleEntryResponseComponent(org.hl7.fhir.dstu3.model.Bundle.BundleEntryResponseComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.Bundle.BundleEntryResponseComponent tgt = new org.hl7.fhir.instance.model.Bundle.BundleEntryResponseComponent();
    copyElement(src, tgt);
    tgt.setStatus(src.getStatus());
    tgt.setLocation(src.getLocation());
    tgt.setEtag(src.getEtag());
    tgt.setLastModified(src.getLastModified());
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.CarePlan convertCarePlan(org.hl7.fhir.instance.model.CarePlan src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.CarePlan tgt = new org.hl7.fhir.dstu3.model.CarePlan();
    copyDomainResource(src, tgt);
    for (org.hl7.fhir.instance.model.Identifier t : src.getIdentifier())
      tgt.addIdentifier(convertIdentifier(t));
    tgt.setSubject(convertReference(src.getSubject()));
    tgt.setStatus(convertCarePlanStatus(src.getStatus()));
    tgt.setContext(convertReference(src.getContext()));
    tgt.setPeriod(convertPeriod(src.getPeriod()));
    for (org.hl7.fhir.instance.model.Reference t : src.getAuthor())
      tgt.addAuthor(convertReference(t));
//    tgt.setModified(src.getModified());
    for (org.hl7.fhir.instance.model.CodeableConcept t : src.getCategory())
      tgt.addCategory(convertCodeableConcept(t));
    tgt.setDescription(src.getDescription());
    for (org.hl7.fhir.instance.model.Reference t : src.getAddresses())
      tgt.addAddresses(convertReference(t));
//    for (org.hl7.fhir.instance.model.Reference t : src.getSupport())
//      tgt.addSupport(convertReference(t));
//    for (org.hl7.fhir.instance.model.CarePlan.CarePlanRelatedPlanComponent t : src.getRelatedPlan())
//      tgt.addRelatedPlan(convertCarePlanRelatedPlanComponent(t));
//    for (org.hl7.fhir.instance.model.CarePlan.CarePlanParticipantComponent t : src.getParticipant())
//      tgt.addParticipant(convertCarePlanParticipantComponent(t));
    for (org.hl7.fhir.instance.model.Reference t : src.getGoal())
      tgt.addGoal(convertReference(t));
    for (org.hl7.fhir.instance.model.CarePlan.CarePlanActivityComponent t : src.getActivity())
      tgt.addActivity(convertCarePlanActivityComponent(t));
//    tgt.setNote(convertAnnotation(src.getNote()));
    return tgt;
  }

  public org.hl7.fhir.instance.model.CarePlan convertCarePlan(org.hl7.fhir.dstu3.model.CarePlan src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.CarePlan tgt = new org.hl7.fhir.instance.model.CarePlan();
    copyDomainResource(src, tgt);
    for (org.hl7.fhir.dstu3.model.Identifier t : src.getIdentifier())
      tgt.addIdentifier(convertIdentifier(t));
    tgt.setSubject(convertReference(src.getSubject()));
    tgt.setStatus(convertCarePlanStatus(src.getStatus()));
    tgt.setContext(convertReference(src.getContext()));
    tgt.setPeriod(convertPeriod(src.getPeriod()));
    for (org.hl7.fhir.dstu3.model.Reference t : src.getAuthor())
      tgt.addAuthor(convertReference(t));
//    tgt.setModified(src.getModified());
    for (org.hl7.fhir.dstu3.model.CodeableConcept t : src.getCategory())
      tgt.addCategory(convertCodeableConcept(t));
    tgt.setDescription(src.getDescription());
    for (org.hl7.fhir.dstu3.model.Reference t : src.getAddresses())
      tgt.addAddresses(convertReference(t));
//    for (org.hl7.fhir.dstu3.model.Reference t : src.getSupport())
//      tgt.addSupport(convertReference(t));
//    for (org.hl7.fhir.dstu3.model.CarePlan.CarePlanRelatedPlanComponent t : src.getRelatedPlan())
//      tgt.addRelatedPlan(convertCarePlanRelatedPlanComponent(t));
//    for (org.hl7.fhir.dstu3.model.CarePlan.CarePlanParticipantComponent t : src.getParticipant())
//      tgt.addParticipant(convertCarePlanParticipantComponent(t));
    for (org.hl7.fhir.dstu3.model.Reference t : src.getGoal())
      tgt.addGoal(convertReference(t));
    for (org.hl7.fhir.dstu3.model.CarePlan.CarePlanActivityComponent t : src.getActivity())
      tgt.addActivity(convertCarePlanActivityComponent(t));
//    tgt.setNote(convertAnnotation(src.getNote()));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.CarePlan.CarePlanStatus convertCarePlanStatus(org.hl7.fhir.instance.model.CarePlan.CarePlanStatus src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case PROPOSED: return org.hl7.fhir.dstu3.model.CarePlan.CarePlanStatus.DRAFT;
    case DRAFT: return org.hl7.fhir.dstu3.model.CarePlan.CarePlanStatus.DRAFT;
    case ACTIVE: return org.hl7.fhir.dstu3.model.CarePlan.CarePlanStatus.ACTIVE;
    case COMPLETED: return org.hl7.fhir.dstu3.model.CarePlan.CarePlanStatus.COMPLETED;
    case CANCELLED: return org.hl7.fhir.dstu3.model.CarePlan.CarePlanStatus.CANCELLED;
    default: return org.hl7.fhir.dstu3.model.CarePlan.CarePlanStatus.NULL;
    }
  }

  public org.hl7.fhir.instance.model.CarePlan.CarePlanStatus convertCarePlanStatus(org.hl7.fhir.dstu3.model.CarePlan.CarePlanStatus src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
//    case PROPOSED: return org.hl7.fhir.instance.model.CarePlan.CarePlanStatus.PROPOSED;
    case DRAFT: return org.hl7.fhir.instance.model.CarePlan.CarePlanStatus.DRAFT;
    case ACTIVE: return org.hl7.fhir.instance.model.CarePlan.CarePlanStatus.ACTIVE;
    case COMPLETED: return org.hl7.fhir.instance.model.CarePlan.CarePlanStatus.COMPLETED;
    case CANCELLED: return org.hl7.fhir.instance.model.CarePlan.CarePlanStatus.CANCELLED;
    default: return org.hl7.fhir.instance.model.CarePlan.CarePlanStatus.NULL;
    }
  }

//  public org.hl7.fhir.dstu3.model.CarePlan.CarePlanRelatedPlanComponent convertCarePlanRelatedPlanComponent(org.hl7.fhir.instance.model.CarePlan.CarePlanRelatedPlanComponent src) throws FHIRException {
//    if (src == null || src.isEmpty())
//      return null;
//    org.hl7.fhir.dstu3.model.CarePlan.CarePlanRelatedPlanComponent tgt = new org.hl7.fhir.dstu3.model.CarePlan.CarePlanRelatedPlanComponent();
//    copyElement(src, tgt);
//    tgt.setCode(convertCarePlanRelationship(src.getCode()));
//    tgt.setPlan(convertReference(src.getPlan()));
//    return tgt;
//  }

//  public org.hl7.fhir.instance.model.CarePlan.CarePlanRelatedPlanComponent convertCarePlanRelatedPlanComponent(org.hl7.fhir.dstu3.model.CarePlan.CarePlanRelatedPlanComponent src) throws FHIRException {
//    if (src == null || src.isEmpty())
//      return null;
//    org.hl7.fhir.instance.model.CarePlan.CarePlanRelatedPlanComponent tgt = new org.hl7.fhir.instance.model.CarePlan.CarePlanRelatedPlanComponent();
//    copyElement(src, tgt);
//    tgt.setCode(convertCarePlanRelationship(src.getCode()));
//    tgt.setPlan(convertReference(src.getPlan()));
//    return tgt;
//  }

//  public org.hl7.fhir.dstu3.model.CarePlan.CarePlanRelationship convertCarePlanRelationship(org.hl7.fhir.instance.model.CarePlan.CarePlanRelationship src) throws FHIRException {
//    if (src == null)
//      return null;
//    switch (src) {
//    case INCLUDES: return org.hl7.fhir.dstu3.model.CarePlan.CarePlanRelationship.INCLUDES;
//    case REPLACES: return org.hl7.fhir.dstu3.model.CarePlan.CarePlanRelationship.REPLACES;
//    case FULFILLS: return org.hl7.fhir.dstu3.model.CarePlan.CarePlanRelationship.FULFILLS;
//    default: return org.hl7.fhir.dstu3.model.CarePlan.CarePlanRelationship.NULL;
//    }
//  }

//  public org.hl7.fhir.instance.model.CarePlan.CarePlanRelationship convertCarePlanRelationship(org.hl7.fhir.dstu3.model.CarePlan.CarePlanRelationship src) throws FHIRException {
//    if (src == null)
//      return null;
//    switch (src) {
//    case INCLUDES: return org.hl7.fhir.instance.model.CarePlan.CarePlanRelationship.INCLUDES;
//    case REPLACES: return org.hl7.fhir.instance.model.CarePlan.CarePlanRelationship.REPLACES;
//    case FULFILLS: return org.hl7.fhir.instance.model.CarePlan.CarePlanRelationship.FULFILLS;
//    default: return org.hl7.fhir.instance.model.CarePlan.CarePlanRelationship.NULL;
//    }
//  }

//  public org.hl7.fhir.dstu3.model.CarePlan.CarePlanParticipantComponent convertCarePlanParticipantComponent(org.hl7.fhir.instance.model.CarePlan.CarePlanParticipantComponent src) throws FHIRException {
//    if (src == null || src.isEmpty())
//      return null;
//    org.hl7.fhir.dstu3.model.CarePlan.CarePlanParticipantComponent tgt = new org.hl7.fhir.dstu3.model.CarePlan.CarePlanParticipantComponent();
//    copyElement(src, tgt);
//    tgt.setRole(convertCodeableConcept(src.getRole()));
//    tgt.setMember(convertReference(src.getMember()));
//    return tgt;
//  }
//
//  public org.hl7.fhir.instance.model.CarePlan.CarePlanParticipantComponent convertCarePlanParticipantComponent(org.hl7.fhir.dstu3.model.CarePlan.CarePlanParticipantComponent src) throws FHIRException {
//    if (src == null || src.isEmpty())
//      return null;
//    org.hl7.fhir.instance.model.CarePlan.CarePlanParticipantComponent tgt = new org.hl7.fhir.instance.model.CarePlan.CarePlanParticipantComponent();
//    copyElement(src, tgt);
//    tgt.setRole(convertCodeableConcept(src.getRole()));
//    tgt.setMember(convertReference(src.getMember()));
//    return tgt;
//  }
//
  public org.hl7.fhir.dstu3.model.CarePlan.CarePlanActivityComponent convertCarePlanActivityComponent(org.hl7.fhir.instance.model.CarePlan.CarePlanActivityComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.CarePlan.CarePlanActivityComponent tgt = new org.hl7.fhir.dstu3.model.CarePlan.CarePlanActivityComponent();
    copyElement(src, tgt);
//    for (org.hl7.fhir.instance.model.Reference t : src.getActionResulting())
//      tgt.addActionResulting(convertReference(t));
    for (org.hl7.fhir.instance.model.Annotation t : src.getProgress())
      tgt.addProgress(convertAnnotation(t));
    tgt.setReference(convertReference(src.getReference()));
    tgt.setDetail(convertCarePlanActivityDetailComponent(src.getDetail()));
    return tgt;
  }

  public org.hl7.fhir.instance.model.CarePlan.CarePlanActivityComponent convertCarePlanActivityComponent(org.hl7.fhir.dstu3.model.CarePlan.CarePlanActivityComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.CarePlan.CarePlanActivityComponent tgt = new org.hl7.fhir.instance.model.CarePlan.CarePlanActivityComponent();
    copyElement(src, tgt);
//    for (org.hl7.fhir.dstu3.model.Reference t : src.getActionResulting())
//      tgt.addActionResulting(convertReference(t));
    for (org.hl7.fhir.dstu3.model.Annotation t : src.getProgress())
      tgt.addProgress(convertAnnotation(t));
    tgt.setReference(convertReference(src.getReference()));
    tgt.setDetail(convertCarePlanActivityDetailComponent(src.getDetail()));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.CarePlan.CarePlanActivityDetailComponent convertCarePlanActivityDetailComponent(org.hl7.fhir.instance.model.CarePlan.CarePlanActivityDetailComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.CarePlan.CarePlanActivityDetailComponent tgt = new org.hl7.fhir.dstu3.model.CarePlan.CarePlanActivityDetailComponent();
    copyElement(src, tgt);
    tgt.setCategory(convertCodeableConcept(src.getCategory()));
    tgt.setCode(convertCodeableConcept(src.getCode()));
    for (org.hl7.fhir.instance.model.CodeableConcept t : src.getReasonCode())
      tgt.addReasonCode(convertCodeableConcept(t));
    for (org.hl7.fhir.instance.model.Reference t : src.getReasonReference())
      tgt.addReasonReference(convertReference(t));
    for (org.hl7.fhir.instance.model.Reference t : src.getGoal())
      tgt.addGoal(convertReference(t));
    tgt.setStatus(convertCarePlanActivityStatus(src.getStatus()));
//    tgt.setStatusReason(convertCodeableConcept(src.getStatusReason()));
    tgt.setProhibited(src.getProhibited());
    tgt.setScheduled(convertType(src.getScheduled()));
    tgt.setLocation(convertReference(src.getLocation()));
    for (org.hl7.fhir.instance.model.Reference t : src.getPerformer())
      tgt.addPerformer(convertReference(t));
    tgt.setProduct(convertType(src.getProduct()));
    tgt.setDailyAmount(convertSimpleQuantity(src.getDailyAmount()));
    tgt.setQuantity(convertSimpleQuantity(src.getQuantity()));
    tgt.setDescription(src.getDescription());
    return tgt;
  }

  public org.hl7.fhir.instance.model.CarePlan.CarePlanActivityDetailComponent convertCarePlanActivityDetailComponent(org.hl7.fhir.dstu3.model.CarePlan.CarePlanActivityDetailComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.CarePlan.CarePlanActivityDetailComponent tgt = new org.hl7.fhir.instance.model.CarePlan.CarePlanActivityDetailComponent();
    copyElement(src, tgt);
    tgt.setCategory(convertCodeableConcept(src.getCategory()));
    tgt.setCode(convertCodeableConcept(src.getCode()));
    for (org.hl7.fhir.dstu3.model.CodeableConcept t : src.getReasonCode())
      tgt.addReasonCode(convertCodeableConcept(t));
    for (org.hl7.fhir.dstu3.model.Reference t : src.getReasonReference())
      tgt.addReasonReference(convertReference(t));
    for (org.hl7.fhir.dstu3.model.Reference t : src.getGoal())
      tgt.addGoal(convertReference(t));
    tgt.setStatus(convertCarePlanActivityStatus(src.getStatus()));
//    tgt.setStatusReason(convertCodeableConcept(src.getStatusReason()));
    tgt.setProhibited(src.getProhibited());
    tgt.setScheduled(convertType(src.getScheduled()));
    tgt.setLocation(convertReference(src.getLocation()));
    for (org.hl7.fhir.dstu3.model.Reference t : src.getPerformer())
      tgt.addPerformer(convertReference(t));
    tgt.setProduct(convertType(src.getProduct()));
    tgt.setDailyAmount(convertSimpleQuantity(src.getDailyAmount()));
    tgt.setQuantity(convertSimpleQuantity(src.getQuantity()));
    tgt.setDescription(src.getDescription());
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.CarePlan.CarePlanActivityStatus convertCarePlanActivityStatus(org.hl7.fhir.instance.model.CarePlan.CarePlanActivityStatus src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case NOTSTARTED: return org.hl7.fhir.dstu3.model.CarePlan.CarePlanActivityStatus.NOTSTARTED;
    case SCHEDULED: return org.hl7.fhir.dstu3.model.CarePlan.CarePlanActivityStatus.SCHEDULED;
    case INPROGRESS: return org.hl7.fhir.dstu3.model.CarePlan.CarePlanActivityStatus.INPROGRESS;
    case ONHOLD: return org.hl7.fhir.dstu3.model.CarePlan.CarePlanActivityStatus.ONHOLD;
    case COMPLETED: return org.hl7.fhir.dstu3.model.CarePlan.CarePlanActivityStatus.COMPLETED;
    case CANCELLED: return org.hl7.fhir.dstu3.model.CarePlan.CarePlanActivityStatus.CANCELLED;
    default: return org.hl7.fhir.dstu3.model.CarePlan.CarePlanActivityStatus.NULL;
    }
  }

  public org.hl7.fhir.instance.model.CarePlan.CarePlanActivityStatus convertCarePlanActivityStatus(org.hl7.fhir.dstu3.model.CarePlan.CarePlanActivityStatus src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case NOTSTARTED: return org.hl7.fhir.instance.model.CarePlan.CarePlanActivityStatus.NOTSTARTED;
    case SCHEDULED: return org.hl7.fhir.instance.model.CarePlan.CarePlanActivityStatus.SCHEDULED;
    case INPROGRESS: return org.hl7.fhir.instance.model.CarePlan.CarePlanActivityStatus.INPROGRESS;
    case ONHOLD: return org.hl7.fhir.instance.model.CarePlan.CarePlanActivityStatus.ONHOLD;
    case COMPLETED: return org.hl7.fhir.instance.model.CarePlan.CarePlanActivityStatus.COMPLETED;
    case CANCELLED: return org.hl7.fhir.instance.model.CarePlan.CarePlanActivityStatus.CANCELLED;
    default: return org.hl7.fhir.instance.model.CarePlan.CarePlanActivityStatus.NULL;
    }
  }

  public org.hl7.fhir.dstu3.model.ClinicalImpression convertClinicalImpression(org.hl7.fhir.instance.model.ClinicalImpression src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.ClinicalImpression tgt = new org.hl7.fhir.dstu3.model.ClinicalImpression();
    copyDomainResource(src, tgt);
    tgt.setSubject(convertReference(src.getPatient()));
    tgt.setAssessor(convertReference(src.getAssessor()));
    tgt.setStatus(convertClinicalImpressionStatus(src.getStatus()));
    if (src.hasDate())
      tgt.setDate(src.getDate());
    tgt.setDescription(src.getDescription());
    tgt.setPrevious(convertReference(src.getPrevious()));
    for (org.hl7.fhir.instance.model.Reference t : src.getProblem())
      tgt.addProblem(convertReference(t));
//    for (org.hl7.fhir.instance.model.ClinicalImpression.ClinicalImpressionInvestigationsComponent t : src.getInvestigations())
//      tgt.addInvestigations(convertClinicalImpressionInvestigationsComponent(t));
    tgt.addProtocol(src.getProtocol());
    tgt.setSummary(src.getSummary());
    for (org.hl7.fhir.instance.model.ClinicalImpression.ClinicalImpressionFindingComponent t : src.getFinding())
      tgt.addFinding(convertClinicalImpressionFindingComponent(t));
    if (src.hasPrognosis())
      tgt.addPrognosisCodeableConcept ().setText(src.getPrognosis());
//    for (org.hl7.fhir.instance.model.Reference t : src.getPlan())
//      tgt.addPlan(convertReference(t));
    for (org.hl7.fhir.instance.model.Reference t : src.getAction())
      tgt.addAction(convertReference(t));
    return tgt;
  }

  public org.hl7.fhir.instance.model.ClinicalImpression convertClinicalImpression(org.hl7.fhir.dstu3.model.ClinicalImpression src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.ClinicalImpression tgt = new org.hl7.fhir.instance.model.ClinicalImpression();
    copyDomainResource(src, tgt);
    tgt.setPatient(convertReference(src.getSubject()));
    tgt.setAssessor(convertReference(src.getAssessor()));
    tgt.setStatus(convertClinicalImpressionStatus(src.getStatus()));
    if (src.hasDate())
      tgt.setDate(src.getDate());
    tgt.setDescription(src.getDescription());
    tgt.setPrevious(convertReference(src.getPrevious()));
    for (org.hl7.fhir.dstu3.model.Reference t : src.getProblem())
      tgt.addProblem(convertReference(t));
//    for (org.hl7.fhir.dstu3.model.ClinicalImpression.ClinicalImpressionInvestigationsComponent t : src.getInvestigations())
//      tgt.addInvestigations(convertClinicalImpressionInvestigationsComponent(t));
    for (UriType t : src.getProtocol())
      tgt.setProtocol(t.asStringValue());
    tgt.setSummary(src.getSummary());
    for (org.hl7.fhir.dstu3.model.ClinicalImpression.ClinicalImpressionFindingComponent t : src.getFinding())
      tgt.addFinding(convertClinicalImpressionFindingComponent(t));
    tgt.setPrognosis(src.getPrognosisCodeableConceptFirstRep().getText());
//    for (org.hl7.fhir.dstu3.model.Reference t : src.getPlan())
//      tgt.addPlan(convertReference(t));
    for (org.hl7.fhir.dstu3.model.Reference t : src.getAction())
      tgt.addAction(convertReference(t));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.ClinicalImpression.ClinicalImpressionStatus convertClinicalImpressionStatus(org.hl7.fhir.instance.model.ClinicalImpression.ClinicalImpressionStatus src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case INPROGRESS: return org.hl7.fhir.dstu3.model.ClinicalImpression.ClinicalImpressionStatus.DRAFT;
    case COMPLETED: return org.hl7.fhir.dstu3.model.ClinicalImpression.ClinicalImpressionStatus.COMPLETED;
    case ENTEREDINERROR: return org.hl7.fhir.dstu3.model.ClinicalImpression.ClinicalImpressionStatus.ENTEREDINERROR;
    default: return org.hl7.fhir.dstu3.model.ClinicalImpression.ClinicalImpressionStatus.NULL;
    }
  }

  public org.hl7.fhir.instance.model.ClinicalImpression.ClinicalImpressionStatus convertClinicalImpressionStatus(org.hl7.fhir.dstu3.model.ClinicalImpression.ClinicalImpressionStatus src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case DRAFT: return org.hl7.fhir.instance.model.ClinicalImpression.ClinicalImpressionStatus.INPROGRESS;
    case COMPLETED: return org.hl7.fhir.instance.model.ClinicalImpression.ClinicalImpressionStatus.COMPLETED;
    case ENTEREDINERROR: return org.hl7.fhir.instance.model.ClinicalImpression.ClinicalImpressionStatus.ENTEREDINERROR;
    default: return org.hl7.fhir.instance.model.ClinicalImpression.ClinicalImpressionStatus.NULL;
    }
  }

//  public org.hl7.fhir.dstu3.model.ClinicalImpression.ClinicalImpressionInvestigationsComponent convertClinicalImpressionInvestigationsComponent(org.hl7.fhir.instance.model.ClinicalImpression.ClinicalImpressionInvestigationsComponent src) throws FHIRException {
//    if (src == null || src.isEmpty())
//      return null;
//    org.hl7.fhir.dstu3.model.ClinicalImpression.ClinicalImpressionInvestigationsComponent tgt = new org.hl7.fhir.dstu3.model.ClinicalImpression.ClinicalImpressionInvestigationsComponent();
//    copyElement(src, tgt);
//    tgt.setCode(convertCodeableConcept(src.getCode()));
//    for (org.hl7.fhir.instance.model.Reference t : src.getItem())
//      tgt.addItem(convertReference(t));
//    return tgt;
//  }
//
//  public org.hl7.fhir.instance.model.ClinicalImpression.ClinicalImpressionInvestigationsComponent convertClinicalImpressionInvestigationsComponent(org.hl7.fhir.dstu3.model.ClinicalImpression.ClinicalImpressionInvestigationsComponent src) throws FHIRException {
//    if (src == null || src.isEmpty())
//      return null;
//    org.hl7.fhir.instance.model.ClinicalImpression.ClinicalImpressionInvestigationsComponent tgt = new org.hl7.fhir.instance.model.ClinicalImpression.ClinicalImpressionInvestigationsComponent();
//    copyElement(src, tgt);
//    tgt.setCode(convertCodeableConcept(src.getCode()));
//    for (org.hl7.fhir.dstu3.model.Reference t : src.getItem())
//      tgt.addItem(convertReference(t));
//    return tgt;
//  }

  public org.hl7.fhir.dstu3.model.ClinicalImpression.ClinicalImpressionFindingComponent convertClinicalImpressionFindingComponent(org.hl7.fhir.instance.model.ClinicalImpression.ClinicalImpressionFindingComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.ClinicalImpression.ClinicalImpressionFindingComponent tgt = new org.hl7.fhir.dstu3.model.ClinicalImpression.ClinicalImpressionFindingComponent();
    copyElement(src, tgt);
    tgt.setItem(convertCodeableConcept(src.getItem()));
//    tgt.setCause(src.getCause());
    return tgt;
  }

  public org.hl7.fhir.instance.model.ClinicalImpression.ClinicalImpressionFindingComponent convertClinicalImpressionFindingComponent(org.hl7.fhir.dstu3.model.ClinicalImpression.ClinicalImpressionFindingComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.ClinicalImpression.ClinicalImpressionFindingComponent tgt = new org.hl7.fhir.instance.model.ClinicalImpression.ClinicalImpressionFindingComponent();
    copyElement(src, tgt);
    if (src.hasItemCodeableConcept())
      try {
        tgt.setItem(convertCodeableConcept(src.getItemCodeableConcept()));
      } catch (org.hl7.fhir.exceptions.FHIRException e) {
      }
//    tgt.setCause(src.getCause());
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.Communication convertCommunication(org.hl7.fhir.instance.model.Communication src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.Communication tgt = new org.hl7.fhir.dstu3.model.Communication();
    copyDomainResource(src, tgt);
    for (org.hl7.fhir.instance.model.Identifier t : src.getIdentifier())
      tgt.addIdentifier(convertIdentifier(t));
    tgt.addCategory(convertCodeableConcept(src.getCategory()));
    tgt.setSender(convertReference(src.getSender()));
    for (org.hl7.fhir.instance.model.Reference t : src.getRecipient())
      tgt.addRecipient(convertReference(t));
    for (org.hl7.fhir.instance.model.Communication.CommunicationPayloadComponent t : src.getPayload())
      tgt.addPayload(convertCommunicationPayloadComponent(t));
    for (org.hl7.fhir.instance.model.CodeableConcept t : src.getMedium())
      tgt.addMedium(convertCodeableConcept(t));
    tgt.setStatus(convertCommunicationStatus(src.getStatus()));
    tgt.setContext(convertReference(src.getEncounter()));
    tgt.setSent(src.getSent());
    tgt.setReceived(src.getReceived());
    for (org.hl7.fhir.instance.model.CodeableConcept t : src.getReason())
      tgt.addReasonCode(convertCodeableConcept(t));
    tgt.setSubject(convertReference(src.getSubject()));
    return tgt;
  }

  public org.hl7.fhir.instance.model.Communication convertCommunication(org.hl7.fhir.dstu3.model.Communication src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.Communication tgt = new org.hl7.fhir.instance.model.Communication();
    copyDomainResource(src, tgt);
    for (org.hl7.fhir.dstu3.model.Identifier t : src.getIdentifier())
      tgt.addIdentifier(convertIdentifier(t));
    tgt.setCategory(convertCodeableConcept(src.getCategoryFirstRep()));
    tgt.setSender(convertReference(src.getSender()));
    for (org.hl7.fhir.dstu3.model.Reference t : src.getRecipient())
      tgt.addRecipient(convertReference(t));
    for (org.hl7.fhir.dstu3.model.Communication.CommunicationPayloadComponent t : src.getPayload())
      tgt.addPayload(convertCommunicationPayloadComponent(t));
    for (org.hl7.fhir.dstu3.model.CodeableConcept t : src.getMedium())
      tgt.addMedium(convertCodeableConcept(t));
    tgt.setStatus(convertCommunicationStatus(src.getStatus()));
    tgt.setEncounter(convertReference(src.getContext()));
    tgt.setSent(src.getSent());
    tgt.setReceived(src.getReceived());
    for (org.hl7.fhir.dstu3.model.CodeableConcept t : src.getReasonCode())
      tgt.addReason(convertCodeableConcept(t));
    tgt.setSubject(convertReference(src.getSubject()));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.Communication.CommunicationStatus convertCommunicationStatus(org.hl7.fhir.instance.model.Communication.CommunicationStatus src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case INPROGRESS: return org.hl7.fhir.dstu3.model.Communication.CommunicationStatus.INPROGRESS;
    case COMPLETED: return org.hl7.fhir.dstu3.model.Communication.CommunicationStatus.COMPLETED;
    case SUSPENDED: return org.hl7.fhir.dstu3.model.Communication.CommunicationStatus.SUSPENDED;
    case REJECTED: return org.hl7.fhir.dstu3.model.Communication.CommunicationStatus.ENTEREDINERROR;
    case FAILED: return org.hl7.fhir.dstu3.model.Communication.CommunicationStatus.ABORTED;
    default: return org.hl7.fhir.dstu3.model.Communication.CommunicationStatus.NULL;
    }
  }

  public org.hl7.fhir.instance.model.Communication.CommunicationStatus convertCommunicationStatus(org.hl7.fhir.dstu3.model.Communication.CommunicationStatus src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case INPROGRESS: return org.hl7.fhir.instance.model.Communication.CommunicationStatus.INPROGRESS;
    case COMPLETED: return org.hl7.fhir.instance.model.Communication.CommunicationStatus.COMPLETED;
    case SUSPENDED: return org.hl7.fhir.instance.model.Communication.CommunicationStatus.SUSPENDED;
    case ENTEREDINERROR: return org.hl7.fhir.instance.model.Communication.CommunicationStatus.REJECTED;
    case ABORTED: return org.hl7.fhir.instance.model.Communication.CommunicationStatus.FAILED;
    default: return org.hl7.fhir.instance.model.Communication.CommunicationStatus.NULL;
    }
  }

  public org.hl7.fhir.dstu3.model.Communication.CommunicationPayloadComponent convertCommunicationPayloadComponent(org.hl7.fhir.instance.model.Communication.CommunicationPayloadComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.Communication.CommunicationPayloadComponent tgt = new org.hl7.fhir.dstu3.model.Communication.CommunicationPayloadComponent();
    copyElement(src, tgt);
    tgt.setContent(convertType(src.getContent()));
    return tgt;
  }

  public org.hl7.fhir.instance.model.Communication.CommunicationPayloadComponent convertCommunicationPayloadComponent(org.hl7.fhir.dstu3.model.Communication.CommunicationPayloadComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.Communication.CommunicationPayloadComponent tgt = new org.hl7.fhir.instance.model.Communication.CommunicationPayloadComponent();
    copyElement(src, tgt);
    tgt.setContent(convertType(src.getContent()));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.CommunicationRequest convertCommunicationRequest(org.hl7.fhir.instance.model.CommunicationRequest src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.CommunicationRequest tgt = new org.hl7.fhir.dstu3.model.CommunicationRequest();
    copyDomainResource(src, tgt);
    for (org.hl7.fhir.instance.model.Identifier t : src.getIdentifier())
      tgt.addIdentifier(convertIdentifier(t));
    tgt.addCategory(convertCodeableConcept(src.getCategory()));
    tgt.setSender(convertReference(src.getSender()));
    for (org.hl7.fhir.instance.model.Reference t : src.getRecipient())
      tgt.addRecipient(convertReference(t));
    for (org.hl7.fhir.instance.model.CommunicationRequest.CommunicationRequestPayloadComponent t : src.getPayload())
      tgt.addPayload(convertCommunicationRequestPayloadComponent(t));
    for (org.hl7.fhir.instance.model.CodeableConcept t : src.getMedium())
      tgt.addMedium(convertCodeableConcept(t));
    tgt.getRequester().setAgent(convertReference(src.getRequester()));
    tgt.setStatus(convertCommunicationRequestStatus(src.getStatus()));
    tgt.setContext(convertReference(src.getEncounter()));
    tgt.setOccurrence(convertType(src.getScheduled()));
    for (org.hl7.fhir.instance.model.CodeableConcept t : src.getReason())
      tgt.addReasonCode(convertCodeableConcept(t));
    tgt.setAuthoredOn(src.getRequestedOn());
    tgt.setSubject(convertReference(src.getSubject()));
    tgt.setPriority(convertPriorityCode(src.getPriority()));
    return tgt;
  }

  private org.hl7.fhir.dstu3.model.CommunicationRequest.CommunicationPriority convertPriorityCode(org.hl7.fhir.instance.model.CodeableConcept priority) {
    for (org.hl7.fhir.instance.model.Coding c : priority.getCoding()) {
      if ("http://hl7.org/fhir/diagnostic-order-priority".equals(c.getSystem()) &&  "routine".equals(c.getCode()))
          return org.hl7.fhir.dstu3.model.CommunicationRequest.CommunicationPriority.ROUTINE;
      if ("http://hl7.org/fhir/diagnostic-order-priority".equals(c.getSystem()) &&  "urgent".equals(c.getCode()))
        return org.hl7.fhir.dstu3.model.CommunicationRequest.CommunicationPriority.URGENT;
      if ("http://hl7.org/fhir/diagnostic-order-priority".equals(c.getSystem()) &&  "stat".equals(c.getCode()))
        return org.hl7.fhir.dstu3.model.CommunicationRequest.CommunicationPriority.STAT;
      if ("http://hl7.org/fhir/diagnostic-order-priority".equals(c.getSystem()) &&  "asap".equals(c.getCode()))
        return org.hl7.fhir.dstu3.model.CommunicationRequest.CommunicationPriority.ASAP;
    }
    return null;
  }

  public org.hl7.fhir.instance.model.CommunicationRequest convertCommunicationRequest(org.hl7.fhir.dstu3.model.CommunicationRequest src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.CommunicationRequest tgt = new org.hl7.fhir.instance.model.CommunicationRequest();
    copyDomainResource(src, tgt);
    for (org.hl7.fhir.dstu3.model.Identifier t : src.getIdentifier())
      tgt.addIdentifier(convertIdentifier(t));
    tgt.setCategory(convertCodeableConcept(src.getCategoryFirstRep()));
    tgt.setSender(convertReference(src.getSender()));
    for (org.hl7.fhir.dstu3.model.Reference t : src.getRecipient())
      tgt.addRecipient(convertReference(t));
    for (org.hl7.fhir.dstu3.model.CommunicationRequest.CommunicationRequestPayloadComponent t : src.getPayload())
      tgt.addPayload(convertCommunicationRequestPayloadComponent(t));
    for (org.hl7.fhir.dstu3.model.CodeableConcept t : src.getMedium())
      tgt.addMedium(convertCodeableConcept(t));
    tgt.setRequester(convertReference(src.getRequester().getAgent()));
    tgt.setStatus(convertCommunicationRequestStatus(src.getStatus()));
    tgt.setEncounter(convertReference(src.getContext()));
    tgt.setScheduled(convertType(src.getOccurrence()));
    for (org.hl7.fhir.dstu3.model.CodeableConcept t : src.getReasonCode())
      tgt.addReason(convertCodeableConcept(t));
    tgt.setRequestedOn(src.getAuthoredOn());
    tgt.setSubject(convertReference(src.getSubject()));
    tgt.setPriority(convertPriorityCode(src.getPriority()));
    return tgt;
  }

  private org.hl7.fhir.instance.model.CodeableConcept convertPriorityCode(org.hl7.fhir.dstu3.model.CommunicationRequest.CommunicationPriority priority) {
    org.hl7.fhir.instance.model.CodeableConcept cc = new org.hl7.fhir.instance.model.CodeableConcept();
    switch (priority) {
    case ROUTINE: cc.addCoding().setSystem("http://hl7.org/fhir/diagnostic-order-priority").setCode("routine"); break;
    case URGENT: cc.addCoding().setSystem("http://hl7.org/fhir/diagnostic-order-priority").setCode("urgent"); break;
    case STAT: cc.addCoding().setSystem("http://hl7.org/fhir/diagnostic-order-priority").setCode("stat"); break;
    case ASAP: cc.addCoding().setSystem("http://hl7.org/fhir/diagnostic-order-priority").setCode("asap"); break;
    default: return null;
    }
    return cc;
  }

  public org.hl7.fhir.dstu3.model.CommunicationRequest.CommunicationRequestStatus convertCommunicationRequestStatus(org.hl7.fhir.instance.model.CommunicationRequest.CommunicationRequestStatus src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case PROPOSED: return org.hl7.fhir.dstu3.model.CommunicationRequest.CommunicationRequestStatus.DRAFT;
    case PLANNED: return org.hl7.fhir.dstu3.model.CommunicationRequest.CommunicationRequestStatus.ACTIVE;
    case REQUESTED: return org.hl7.fhir.dstu3.model.CommunicationRequest.CommunicationRequestStatus.ACTIVE;
    case RECEIVED: return org.hl7.fhir.dstu3.model.CommunicationRequest.CommunicationRequestStatus.ACTIVE;
    case ACCEPTED: return org.hl7.fhir.dstu3.model.CommunicationRequest.CommunicationRequestStatus.ACTIVE;
    case INPROGRESS: return org.hl7.fhir.dstu3.model.CommunicationRequest.CommunicationRequestStatus.ACTIVE;
    case COMPLETED: return org.hl7.fhir.dstu3.model.CommunicationRequest.CommunicationRequestStatus.COMPLETED;
    case SUSPENDED: return org.hl7.fhir.dstu3.model.CommunicationRequest.CommunicationRequestStatus.SUSPENDED;
    case REJECTED: return org.hl7.fhir.dstu3.model.CommunicationRequest.CommunicationRequestStatus.ENTEREDINERROR;
//    case FAILED: return org.hl7.fhir.dstu3.model.CommunicationRequest.CommunicationRequestStatus.FAILED;
    default: return org.hl7.fhir.dstu3.model.CommunicationRequest.CommunicationRequestStatus.NULL;
    }
  }

  public org.hl7.fhir.instance.model.CommunicationRequest.CommunicationRequestStatus convertCommunicationRequestStatus(org.hl7.fhir.dstu3.model.CommunicationRequest.CommunicationRequestStatus src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case DRAFT: return org.hl7.fhir.instance.model.CommunicationRequest.CommunicationRequestStatus.PROPOSED;
//    case PLANNED: return org.hl7.fhir.instance.model.CommunicationRequest.CommunicationRequestStatus.PLANNED;
//    case REQUESTED: return org.hl7.fhir.instance.model.CommunicationRequest.CommunicationRequestStatus.REQUESTED;
//    case RECEIVED: return org.hl7.fhir.instance.model.CommunicationRequest.CommunicationRequestStatus.RECEIVED;
//    case ACCEPTED: return org.hl7.fhir.instance.model.CommunicationRequest.CommunicationRequestStatus.ACCEPTED;
    case ACTIVE: return org.hl7.fhir.instance.model.CommunicationRequest.CommunicationRequestStatus.INPROGRESS;
    case COMPLETED: return org.hl7.fhir.instance.model.CommunicationRequest.CommunicationRequestStatus.COMPLETED;
    case SUSPENDED: return org.hl7.fhir.instance.model.CommunicationRequest.CommunicationRequestStatus.SUSPENDED;
    case ENTEREDINERROR: return org.hl7.fhir.instance.model.CommunicationRequest.CommunicationRequestStatus.REJECTED;
//    case FAILED: return org.hl7.fhir.instance.model.CommunicationRequest.CommunicationRequestStatus.FAILED;
    default: return org.hl7.fhir.instance.model.CommunicationRequest.CommunicationRequestStatus.NULL;
    }
  }

  public org.hl7.fhir.dstu3.model.CommunicationRequest.CommunicationRequestPayloadComponent convertCommunicationRequestPayloadComponent(org.hl7.fhir.instance.model.CommunicationRequest.CommunicationRequestPayloadComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.CommunicationRequest.CommunicationRequestPayloadComponent tgt = new org.hl7.fhir.dstu3.model.CommunicationRequest.CommunicationRequestPayloadComponent();
    copyElement(src, tgt);
    tgt.setContent(convertType(src.getContent()));
    return tgt;
  }

  public org.hl7.fhir.instance.model.CommunicationRequest.CommunicationRequestPayloadComponent convertCommunicationRequestPayloadComponent(org.hl7.fhir.dstu3.model.CommunicationRequest.CommunicationRequestPayloadComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.CommunicationRequest.CommunicationRequestPayloadComponent tgt = new org.hl7.fhir.instance.model.CommunicationRequest.CommunicationRequestPayloadComponent();
    copyElement(src, tgt);
    tgt.setContent(convertType(src.getContent()));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.Composition convertComposition(org.hl7.fhir.instance.model.Composition src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.Composition tgt = new org.hl7.fhir.dstu3.model.Composition();
    copyDomainResource(src, tgt);
    tgt.setIdentifier(convertIdentifier(src.getIdentifier()));
    if (src.hasDate())
      tgt.setDate(src.getDate());
    tgt.setType(convertCodeableConcept(src.getType()));
    tgt.setClass_(convertCodeableConcept(src.getClass_()));
    tgt.setTitle(src.getTitle());
    tgt.setStatus(convertCompositionStatus(src.getStatus()));
    try {
      tgt.setConfidentiality(org.hl7.fhir.dstu3.model.Composition.DocumentConfidentiality.fromCode(src.getConfidentiality()));
    } catch (org.hl7.fhir.exceptions.FHIRException e) {
      throw new FHIRException(e);
    }
    tgt.setSubject(convertReference(src.getSubject()));
    for (org.hl7.fhir.instance.model.Reference t : src.getAuthor())
      tgt.addAuthor(convertReference(t));
    for (org.hl7.fhir.instance.model.Composition.CompositionAttesterComponent t : src.getAttester())
      tgt.addAttester(convertCompositionAttesterComponent(t));
    tgt.setCustodian(convertReference(src.getCustodian()));
    for (org.hl7.fhir.instance.model.Composition.CompositionEventComponent t : src.getEvent())
      tgt.addEvent(convertCompositionEventComponent(t));
    tgt.setEncounter(convertReference(src.getEncounter()));
    for (org.hl7.fhir.instance.model.Composition.SectionComponent t : src.getSection())
      tgt.addSection(convertSectionComponent(t));
    return tgt;
  }

  public org.hl7.fhir.instance.model.Composition convertComposition(org.hl7.fhir.dstu3.model.Composition src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.Composition tgt = new org.hl7.fhir.instance.model.Composition();
    copyDomainResource(src, tgt);
    tgt.setIdentifier(convertIdentifier(src.getIdentifier()));
    if (src.hasDate())
      tgt.setDate(src.getDate());
    tgt.setType(convertCodeableConcept(src.getType()));
    tgt.setClass_(convertCodeableConcept(src.getClass_()));
    tgt.setTitle(src.getTitle());
    tgt.setStatus(convertCompositionStatus(src.getStatus()));
    tgt.setConfidentiality(src.getConfidentiality().toCode());
    tgt.setSubject(convertReference(src.getSubject()));
    for (org.hl7.fhir.dstu3.model.Reference t : src.getAuthor())
      tgt.addAuthor(convertReference(t));
    for (org.hl7.fhir.dstu3.model.Composition.CompositionAttesterComponent t : src.getAttester())
      tgt.addAttester(convertCompositionAttesterComponent(t));
    tgt.setCustodian(convertReference(src.getCustodian()));
    for (org.hl7.fhir.dstu3.model.Composition.CompositionEventComponent t : src.getEvent())
      tgt.addEvent(convertCompositionEventComponent(t));
    tgt.setEncounter(convertReference(src.getEncounter()));
    for (org.hl7.fhir.dstu3.model.Composition.SectionComponent t : src.getSection())
      tgt.addSection(convertSectionComponent(t));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.Composition.CompositionStatus convertCompositionStatus(org.hl7.fhir.instance.model.Composition.CompositionStatus src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case PRELIMINARY: return org.hl7.fhir.dstu3.model.Composition.CompositionStatus.PRELIMINARY;
    case FINAL: return org.hl7.fhir.dstu3.model.Composition.CompositionStatus.FINAL;
    case AMENDED: return org.hl7.fhir.dstu3.model.Composition.CompositionStatus.AMENDED;
    case ENTEREDINERROR: return org.hl7.fhir.dstu3.model.Composition.CompositionStatus.ENTEREDINERROR;
    default: return org.hl7.fhir.dstu3.model.Composition.CompositionStatus.NULL;
    }
  }

  public org.hl7.fhir.instance.model.Composition.CompositionStatus convertCompositionStatus(org.hl7.fhir.dstu3.model.Composition.CompositionStatus src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case PRELIMINARY: return org.hl7.fhir.instance.model.Composition.CompositionStatus.PRELIMINARY;
    case FINAL: return org.hl7.fhir.instance.model.Composition.CompositionStatus.FINAL;
    case AMENDED: return org.hl7.fhir.instance.model.Composition.CompositionStatus.AMENDED;
    case ENTEREDINERROR: return org.hl7.fhir.instance.model.Composition.CompositionStatus.ENTEREDINERROR;
    default: return org.hl7.fhir.instance.model.Composition.CompositionStatus.NULL;
    }
  }

  public org.hl7.fhir.dstu3.model.Composition.CompositionAttesterComponent convertCompositionAttesterComponent(org.hl7.fhir.instance.model.Composition.CompositionAttesterComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.Composition.CompositionAttesterComponent tgt = new org.hl7.fhir.dstu3.model.Composition.CompositionAttesterComponent();
    copyElement(src, tgt);
    for (org.hl7.fhir.instance.model.Enumeration<org.hl7.fhir.instance.model.Composition.CompositionAttestationMode> t : src.getMode())
      tgt.addMode(convertCompositionAttestationMode(t.getValue()));
    tgt.setTime(src.getTime());
    tgt.setParty(convertReference(src.getParty()));
    return tgt;
  }

  public org.hl7.fhir.instance.model.Composition.CompositionAttesterComponent convertCompositionAttesterComponent(org.hl7.fhir.dstu3.model.Composition.CompositionAttesterComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.Composition.CompositionAttesterComponent tgt = new org.hl7.fhir.instance.model.Composition.CompositionAttesterComponent();
    copyElement(src, tgt);
    for (org.hl7.fhir.dstu3.model.Enumeration<org.hl7.fhir.dstu3.model.Composition.CompositionAttestationMode> t : src.getMode())
      tgt.addMode(convertCompositionAttestationMode(t.getValue()));
    tgt.setTime(src.getTime());
    tgt.setParty(convertReference(src.getParty()));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.Composition.CompositionAttestationMode convertCompositionAttestationMode(org.hl7.fhir.instance.model.Composition.CompositionAttestationMode src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case PERSONAL: return org.hl7.fhir.dstu3.model.Composition.CompositionAttestationMode.PERSONAL;
    case PROFESSIONAL: return org.hl7.fhir.dstu3.model.Composition.CompositionAttestationMode.PROFESSIONAL;
    case LEGAL: return org.hl7.fhir.dstu3.model.Composition.CompositionAttestationMode.LEGAL;
    case OFFICIAL: return org.hl7.fhir.dstu3.model.Composition.CompositionAttestationMode.OFFICIAL;
    default: return org.hl7.fhir.dstu3.model.Composition.CompositionAttestationMode.NULL;
    }
  }

  public org.hl7.fhir.instance.model.Composition.CompositionAttestationMode convertCompositionAttestationMode(org.hl7.fhir.dstu3.model.Composition.CompositionAttestationMode src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case PERSONAL: return org.hl7.fhir.instance.model.Composition.CompositionAttestationMode.PERSONAL;
    case PROFESSIONAL: return org.hl7.fhir.instance.model.Composition.CompositionAttestationMode.PROFESSIONAL;
    case LEGAL: return org.hl7.fhir.instance.model.Composition.CompositionAttestationMode.LEGAL;
    case OFFICIAL: return org.hl7.fhir.instance.model.Composition.CompositionAttestationMode.OFFICIAL;
    default: return org.hl7.fhir.instance.model.Composition.CompositionAttestationMode.NULL;
    }
  }

  public org.hl7.fhir.dstu3.model.Composition.CompositionEventComponent convertCompositionEventComponent(org.hl7.fhir.instance.model.Composition.CompositionEventComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.Composition.CompositionEventComponent tgt = new org.hl7.fhir.dstu3.model.Composition.CompositionEventComponent();
    copyElement(src, tgt);
    for (org.hl7.fhir.instance.model.CodeableConcept t : src.getCode())
      tgt.addCode(convertCodeableConcept(t));
    tgt.setPeriod(convertPeriod(src.getPeriod()));
    for (org.hl7.fhir.instance.model.Reference t : src.getDetail())
      tgt.addDetail(convertReference(t));
    return tgt;
  }

  public org.hl7.fhir.instance.model.Composition.CompositionEventComponent convertCompositionEventComponent(org.hl7.fhir.dstu3.model.Composition.CompositionEventComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.Composition.CompositionEventComponent tgt = new org.hl7.fhir.instance.model.Composition.CompositionEventComponent();
    copyElement(src, tgt);
    for (org.hl7.fhir.dstu3.model.CodeableConcept t : src.getCode())
      tgt.addCode(convertCodeableConcept(t));
    tgt.setPeriod(convertPeriod(src.getPeriod()));
    for (org.hl7.fhir.dstu3.model.Reference t : src.getDetail())
      tgt.addDetail(convertReference(t));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.Composition.SectionComponent convertSectionComponent(org.hl7.fhir.instance.model.Composition.SectionComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.Composition.SectionComponent tgt = new org.hl7.fhir.dstu3.model.Composition.SectionComponent();
    copyElement(src, tgt);
    tgt.setTitle(src.getTitle());
    tgt.setCode(convertCodeableConcept(src.getCode()));
    tgt.setText(convertNarrative(src.getText()));
    try {
      tgt.setMode(org.hl7.fhir.dstu3.model.Composition.SectionMode.fromCode(src.getMode()));
    } catch (org.hl7.fhir.exceptions.FHIRException e) {
      throw new FHIRException(e);
    }
    tgt.setOrderedBy(convertCodeableConcept(src.getOrderedBy()));
    for (org.hl7.fhir.instance.model.Reference t : src.getEntry())
      tgt.addEntry(convertReference(t));
    tgt.setEmptyReason(convertCodeableConcept(src.getEmptyReason()));
    for (org.hl7.fhir.instance.model.Composition.SectionComponent t : src.getSection())
      tgt.addSection(convertSectionComponent(t));
    return tgt;
  }

  public org.hl7.fhir.instance.model.Composition.SectionComponent convertSectionComponent(org.hl7.fhir.dstu3.model.Composition.SectionComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.Composition.SectionComponent tgt = new org.hl7.fhir.instance.model.Composition.SectionComponent();
    copyElement(src, tgt);
    tgt.setTitle(src.getTitle());
    tgt.setCode(convertCodeableConcept(src.getCode()));
    tgt.setText(convertNarrative(src.getText()));
    tgt.setMode(src.getMode().toCode());
    tgt.setOrderedBy(convertCodeableConcept(src.getOrderedBy()));
    for (org.hl7.fhir.dstu3.model.Reference t : src.getEntry())
      tgt.addEntry(convertReference(t));
    tgt.setEmptyReason(convertCodeableConcept(src.getEmptyReason()));
    for (org.hl7.fhir.dstu3.model.Composition.SectionComponent t : src.getSection())
      tgt.addSection(convertSectionComponent(t));
    return tgt;
  }

  private class SourceElementComponentWrapper {
    public SourceElementComponentWrapper(SourceElementComponent comp, String source, String target) {
      super();
      this.source = source;
      this.target = target;
      this.comp = comp;
    }
    private String source;
    private String target;
    private org.hl7.fhir.dstu3.model.ConceptMap.SourceElementComponent comp;

  }

  public org.hl7.fhir.dstu3.model.ConceptMap convertConceptMap(org.hl7.fhir.instance.model.ConceptMap src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.ConceptMap tgt = new org.hl7.fhir.dstu3.model.ConceptMap();
    copyDomainResource(src, tgt);
    tgt.setUrl(src.getUrl());
    tgt.setIdentifier(convertIdentifier(src.getIdentifier()));
    tgt.setVersion(src.getVersion());
    tgt.setName(src.getName());
    tgt.setStatus(convertConformanceResourceStatus(src.getStatus()));
    if (src.hasExperimental())
      tgt.setExperimental(src.getExperimental());
    tgt.setPublisher(src.getPublisher());
    for (org.hl7.fhir.instance.model.ConceptMap.ConceptMapContactComponent t : src.getContact())
      tgt.addContact(convertConceptMapContactComponent(t));
    if (src.hasDate())
      tgt.setDate(src.getDate());
    tgt.setDescription(src.getDescription());
    for (org.hl7.fhir.instance.model.CodeableConcept t : src.getUseContext())
      if (isJurisdiction(t))
        tgt.addJurisdiction(convertCodeableConcept(t));
      else
        tgt.addUseContext(convertCodeableConceptToUsageContext(t));
    tgt.setPurpose(src.getRequirements());
    tgt.setCopyright(src.getCopyright());
    tgt.setSource(convertType(src.getSource()));
    tgt.setTarget(convertType(src.getTarget()));
    for (org.hl7.fhir.instance.model.ConceptMap.SourceElementComponent t : src.getElement()) {
      List<SourceElementComponentWrapper> ws = convertSourceElementComponent(t);
      for (SourceElementComponentWrapper w : ws)
      getGroup(tgt, w.source, w.target).addElement(w.comp);
    }
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.UsageContext convertCodeableConceptToUsageContext(org.hl7.fhir.instance.model.CodeableConcept t) throws FHIRException {
    org.hl7.fhir.dstu3.model.UsageContext result = new org.hl7.fhir.dstu3.model.UsageContext();
    // todo: set type..
    result.setValue(convertCodeableConcept(t));
    return result;
  }


  private ConceptMapGroupComponent getGroup(ConceptMap map, String srcs, String tgts) {
    for (ConceptMapGroupComponent grp : map.getGroup()) {
      if (grp.getSource().equals(srcs) && grp.getTarget().equals(tgts))
        return grp;
    }
    ConceptMapGroupComponent grp = map.addGroup();
    grp.setSource(srcs);
    grp.setTarget(tgts);
    return grp;
  }


  public org.hl7.fhir.instance.model.ConceptMap convertConceptMap(org.hl7.fhir.dstu3.model.ConceptMap src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.ConceptMap tgt = new org.hl7.fhir.instance.model.ConceptMap();
    copyDomainResource(src, tgt);
    tgt.setUrl(src.getUrl());
    tgt.setIdentifier(convertIdentifier(src.getIdentifier()));
    tgt.setVersion(src.getVersion());
    tgt.setName(src.getName());
    tgt.setStatus(convertConformanceResourceStatus(src.getStatus()));
    if (src.hasExperimental())
      tgt.setExperimental(src.getExperimental());
    tgt.setPublisher(src.getPublisher());
    for (org.hl7.fhir.dstu3.model.ContactDetail t : src.getContact())
      tgt.addContact(convertConceptMapContactComponent(t));
    if (src.hasDate())
      tgt.setDate(src.getDate());
    tgt.setDescription(src.getDescription());
    for (org.hl7.fhir.dstu3.model.UsageContext t : src.getUseContext())
      if (t.hasValueCodeableConcept())
        tgt.addUseContext(convertCodeableConcept(t.getValueCodeableConcept()));
    for (org.hl7.fhir.dstu3.model.CodeableConcept t : src.getJurisdiction())
        tgt.addUseContext(convertCodeableConcept(t));
    tgt.setRequirements(src.getPurpose());
    tgt.setCopyright(src.getCopyright());
    tgt.setSource(convertType(src.getSource()));
    tgt.setTarget(convertType(src.getTarget()));
    for (org.hl7.fhir.dstu3.model.ConceptMap.ConceptMapGroupComponent g : src.getGroup())
      for (org.hl7.fhir.dstu3.model.ConceptMap.SourceElementComponent t : g.getElement())
        tgt.addElement(convertSourceElementComponent(t, g));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.Enumerations.PublicationStatus convertConformanceResourceStatus(org.hl7.fhir.instance.model.Enumerations.ConformanceResourceStatus src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case DRAFT: return org.hl7.fhir.dstu3.model.Enumerations.PublicationStatus.DRAFT;
    case ACTIVE: return org.hl7.fhir.dstu3.model.Enumerations.PublicationStatus.ACTIVE;
    case RETIRED: return org.hl7.fhir.dstu3.model.Enumerations.PublicationStatus.RETIRED;
    default: return org.hl7.fhir.dstu3.model.Enumerations.PublicationStatus.NULL;
    }
  }

  public org.hl7.fhir.instance.model.Enumerations.ConformanceResourceStatus convertConformanceResourceStatus(org.hl7.fhir.dstu3.model.Enumerations.PublicationStatus src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case DRAFT: return org.hl7.fhir.instance.model.Enumerations.ConformanceResourceStatus.DRAFT;
    case ACTIVE: return org.hl7.fhir.instance.model.Enumerations.ConformanceResourceStatus.ACTIVE;
    case RETIRED: return org.hl7.fhir.instance.model.Enumerations.ConformanceResourceStatus.RETIRED;
    default: return org.hl7.fhir.instance.model.Enumerations.ConformanceResourceStatus.NULL;
    }
  }

  public org.hl7.fhir.dstu3.model.ContactDetail convertConceptMapContactComponent(org.hl7.fhir.instance.model.ConceptMap.ConceptMapContactComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.ContactDetail tgt = new org.hl7.fhir.dstu3.model.ContactDetail();
    copyElement(src, tgt);
    tgt.setName(src.getName());
    for (org.hl7.fhir.instance.model.ContactPoint t : src.getTelecom())
      tgt.addTelecom(convertContactPoint(t));
    return tgt;
  }

  public org.hl7.fhir.instance.model.ConceptMap.ConceptMapContactComponent convertConceptMapContactComponent(org.hl7.fhir.dstu3.model.ContactDetail src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.ConceptMap.ConceptMapContactComponent tgt = new org.hl7.fhir.instance.model.ConceptMap.ConceptMapContactComponent();
    copyElement(src, tgt);
    tgt.setName(src.getName());
    for (org.hl7.fhir.dstu3.model.ContactPoint t : src.getTelecom())
      tgt.addTelecom(convertContactPoint(t));
    return tgt;
  }

  public List<SourceElementComponentWrapper> convertSourceElementComponent(org.hl7.fhir.instance.model.ConceptMap.SourceElementComponent src) throws FHIRException {
    List<SourceElementComponentWrapper> res = new ArrayList<SourceElementComponentWrapper>();
    if (src == null || src.isEmpty())
      return res;
    for (org.hl7.fhir.instance.model.ConceptMap.TargetElementComponent t : src.getTarget()) {
      org.hl7.fhir.dstu3.model.ConceptMap.SourceElementComponent tgt = new org.hl7.fhir.dstu3.model.ConceptMap.SourceElementComponent();
      copyElement(src, tgt);
      tgt.setCode(src.getCode());
      tgt.addTarget(convertTargetElementComponent(t));
      res.add(new SourceElementComponentWrapper(tgt, src.getCodeSystem(), t.getCodeSystem()));
    }
    return res;
  }

  public org.hl7.fhir.instance.model.ConceptMap.SourceElementComponent convertSourceElementComponent(org.hl7.fhir.dstu3.model.ConceptMap.SourceElementComponent src, org.hl7.fhir.dstu3.model.ConceptMap.ConceptMapGroupComponent g) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.ConceptMap.SourceElementComponent tgt = new org.hl7.fhir.instance.model.ConceptMap.SourceElementComponent();
    copyElement(src, tgt);
    tgt.setCodeSystem(g.getSource());
    tgt.setCode(src.getCode());
    for (org.hl7.fhir.dstu3.model.ConceptMap.TargetElementComponent t : src.getTarget())
      tgt.addTarget(convertTargetElementComponent(t, g));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.ConceptMap.TargetElementComponent convertTargetElementComponent(org.hl7.fhir.instance.model.ConceptMap.TargetElementComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.ConceptMap.TargetElementComponent tgt = new org.hl7.fhir.dstu3.model.ConceptMap.TargetElementComponent();
    copyElement(src, tgt);
    tgt.setCode(src.getCode());
    tgt.setEquivalence(convertConceptMapEquivalence(src.getEquivalence()));
    tgt.setComment(src.getComments());
    for (org.hl7.fhir.instance.model.ConceptMap.OtherElementComponent t : src.getDependsOn())
      tgt.addDependsOn(convertOtherElementComponent(t));
    for (org.hl7.fhir.instance.model.ConceptMap.OtherElementComponent t : src.getProduct())
      tgt.addProduct(convertOtherElementComponent(t));
    return tgt;
  }

  public org.hl7.fhir.instance.model.ConceptMap.TargetElementComponent convertTargetElementComponent(org.hl7.fhir.dstu3.model.ConceptMap.TargetElementComponent src, org.hl7.fhir.dstu3.model.ConceptMap.ConceptMapGroupComponent g) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.ConceptMap.TargetElementComponent tgt = new org.hl7.fhir.instance.model.ConceptMap.TargetElementComponent();
    copyElement(src, tgt);
    tgt.setCodeSystem(g.getTarget());
    tgt.setCode(src.getCode());
    tgt.setEquivalence(convertConceptMapEquivalence(src.getEquivalence()));
    tgt.setComments(src.getComment());
    for (org.hl7.fhir.dstu3.model.ConceptMap.OtherElementComponent t : src.getDependsOn())
      tgt.addDependsOn(convertOtherElementComponent(t));
    for (org.hl7.fhir.dstu3.model.ConceptMap.OtherElementComponent t : src.getProduct())
      tgt.addProduct(convertOtherElementComponent(t));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.Enumerations.ConceptMapEquivalence convertConceptMapEquivalence(org.hl7.fhir.instance.model.Enumerations.ConceptMapEquivalence src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case EQUIVALENT: return org.hl7.fhir.dstu3.model.Enumerations.ConceptMapEquivalence.EQUIVALENT;
    case EQUAL: return org.hl7.fhir.dstu3.model.Enumerations.ConceptMapEquivalence.EQUAL;
    case WIDER: return org.hl7.fhir.dstu3.model.Enumerations.ConceptMapEquivalence.WIDER;
    case SUBSUMES: return org.hl7.fhir.dstu3.model.Enumerations.ConceptMapEquivalence.SUBSUMES;
    case NARROWER: return org.hl7.fhir.dstu3.model.Enumerations.ConceptMapEquivalence.NARROWER;
    case SPECIALIZES: return org.hl7.fhir.dstu3.model.Enumerations.ConceptMapEquivalence.SPECIALIZES;
    case INEXACT: return org.hl7.fhir.dstu3.model.Enumerations.ConceptMapEquivalence.INEXACT;
    case UNMATCHED: return org.hl7.fhir.dstu3.model.Enumerations.ConceptMapEquivalence.UNMATCHED;
    case DISJOINT: return org.hl7.fhir.dstu3.model.Enumerations.ConceptMapEquivalence.DISJOINT;
    default: return org.hl7.fhir.dstu3.model.Enumerations.ConceptMapEquivalence.NULL;
    }
  }

  public org.hl7.fhir.instance.model.Enumerations.ConceptMapEquivalence convertConceptMapEquivalence(org.hl7.fhir.dstu3.model.Enumerations.ConceptMapEquivalence src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case EQUIVALENT: return org.hl7.fhir.instance.model.Enumerations.ConceptMapEquivalence.EQUIVALENT;
    case EQUAL: return org.hl7.fhir.instance.model.Enumerations.ConceptMapEquivalence.EQUAL;
    case WIDER: return org.hl7.fhir.instance.model.Enumerations.ConceptMapEquivalence.WIDER;
    case SUBSUMES: return org.hl7.fhir.instance.model.Enumerations.ConceptMapEquivalence.SUBSUMES;
    case NARROWER: return org.hl7.fhir.instance.model.Enumerations.ConceptMapEquivalence.NARROWER;
    case SPECIALIZES: return org.hl7.fhir.instance.model.Enumerations.ConceptMapEquivalence.SPECIALIZES;
    case INEXACT: return org.hl7.fhir.instance.model.Enumerations.ConceptMapEquivalence.INEXACT;
    case UNMATCHED: return org.hl7.fhir.instance.model.Enumerations.ConceptMapEquivalence.UNMATCHED;
    case DISJOINT: return org.hl7.fhir.instance.model.Enumerations.ConceptMapEquivalence.DISJOINT;
    default: return org.hl7.fhir.instance.model.Enumerations.ConceptMapEquivalence.NULL;
    }
  }

  public org.hl7.fhir.dstu3.model.ConceptMap.OtherElementComponent convertOtherElementComponent(org.hl7.fhir.instance.model.ConceptMap.OtherElementComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.ConceptMap.OtherElementComponent tgt = new org.hl7.fhir.dstu3.model.ConceptMap.OtherElementComponent();
    copyElement(src, tgt);
    tgt.setProperty(src.getElement());
    tgt.setSystem(src.getCodeSystem());
    tgt.setCode(src.getCode());
    return tgt;
  }

  public org.hl7.fhir.instance.model.ConceptMap.OtherElementComponent convertOtherElementComponent(org.hl7.fhir.dstu3.model.ConceptMap.OtherElementComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.ConceptMap.OtherElementComponent tgt = new org.hl7.fhir.instance.model.ConceptMap.OtherElementComponent();
    copyElement(src, tgt);
    tgt.setElement(src.getProperty());
    tgt.setCodeSystem(src.getSystem());
    tgt.setCode(src.getCode());
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.Condition convertCondition(org.hl7.fhir.instance.model.Condition src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.Condition tgt = new org.hl7.fhir.dstu3.model.Condition();
    copyDomainResource(src, tgt);
    for (org.hl7.fhir.instance.model.Identifier t : src.getIdentifier())
      tgt.addIdentifier(convertIdentifier(t));
    tgt.setSubject(convertReference(src.getPatient()));
    tgt.setContext(convertReference(src.getEncounter()));
    tgt.setAsserter(convertReference(src.getAsserter()));
    if (src.hasDateRecorded())
      tgt.setAssertedDate(src.getDateRecorded());
    tgt.setCode(convertCodeableConcept(src.getCode()));
    tgt.addCategory(convertCodeableConcept(src.getCategory()));
    try {
      tgt.setClinicalStatus(org.hl7.fhir.dstu3.model.Condition.ConditionClinicalStatus.fromCode(src.getClinicalStatus()));
    } catch (org.hl7.fhir.exceptions.FHIRException e) {
      throw new FHIRException(e);
    }
    tgt.setVerificationStatus(convertConditionVerificationStatus(src.getVerificationStatus()));
    tgt.setSeverity(convertCodeableConcept(src.getSeverity()));
    tgt.setOnset(convertType(src.getOnset()));
    tgt.setAbatement(convertType(src.getAbatement()));
    tgt.setStage(convertConditionStageComponent(src.getStage()));
    for (org.hl7.fhir.instance.model.Condition.ConditionEvidenceComponent t : src.getEvidence())
      tgt.addEvidence(convertConditionEvidenceComponent(t));
    for (org.hl7.fhir.instance.model.CodeableConcept t : src.getBodySite())
      tgt.addBodySite(convertCodeableConcept(t));
//    tgt.setNotes(src.getNotes());
    return tgt;
  }

  public org.hl7.fhir.instance.model.Condition convertCondition(org.hl7.fhir.dstu3.model.Condition src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.Condition tgt = new org.hl7.fhir.instance.model.Condition();
    copyDomainResource(src, tgt);
    for (org.hl7.fhir.dstu3.model.Identifier t : src.getIdentifier())
      tgt.addIdentifier(convertIdentifier(t));
    tgt.setPatient(convertReference(src.getSubject()));
    tgt.setEncounter(convertReference(src.getContext()));
    tgt.setAsserter(convertReference(src.getAsserter()));
    if (src.hasAssertedDate())
      tgt.setDateRecorded(src.getAssertedDate());
    tgt.setCode(convertCodeableConcept(src.getCode()));
    for (org.hl7.fhir.dstu3.model.CodeableConcept t : src.getCategory())
      tgt.setCategory(convertCodeableConcept(t));
    tgt.setClinicalStatus(src.getClinicalStatus().toCode());
    tgt.setVerificationStatus(convertConditionVerificationStatus(src.getVerificationStatus()));
    tgt.setSeverity(convertCodeableConcept(src.getSeverity()));
    tgt.setOnset(convertType(src.getOnset()));
    tgt.setAbatement(convertType(src.getAbatement()));
    tgt.setStage(convertConditionStageComponent(src.getStage()));
    for (org.hl7.fhir.dstu3.model.Condition.ConditionEvidenceComponent t : src.getEvidence())
      tgt.addEvidence(convertConditionEvidenceComponent(t));
    for (org.hl7.fhir.dstu3.model.CodeableConcept t : src.getBodySite())
      tgt.addBodySite(convertCodeableConcept(t));
//    tgt.setNotes(src.getNotes());
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.Condition.ConditionVerificationStatus convertConditionVerificationStatus(org.hl7.fhir.instance.model.Condition.ConditionVerificationStatus src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case PROVISIONAL: return org.hl7.fhir.dstu3.model.Condition.ConditionVerificationStatus.PROVISIONAL;
    case DIFFERENTIAL: return org.hl7.fhir.dstu3.model.Condition.ConditionVerificationStatus.DIFFERENTIAL;
    case CONFIRMED: return org.hl7.fhir.dstu3.model.Condition.ConditionVerificationStatus.CONFIRMED;
    case REFUTED: return org.hl7.fhir.dstu3.model.Condition.ConditionVerificationStatus.REFUTED;
    case ENTEREDINERROR: return org.hl7.fhir.dstu3.model.Condition.ConditionVerificationStatus.ENTEREDINERROR;
    case UNKNOWN: return org.hl7.fhir.dstu3.model.Condition.ConditionVerificationStatus.UNKNOWN;
    default: return org.hl7.fhir.dstu3.model.Condition.ConditionVerificationStatus.NULL;
    }
  }

  public org.hl7.fhir.instance.model.Condition.ConditionVerificationStatus convertConditionVerificationStatus(org.hl7.fhir.dstu3.model.Condition.ConditionVerificationStatus src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case PROVISIONAL: return org.hl7.fhir.instance.model.Condition.ConditionVerificationStatus.PROVISIONAL;
    case DIFFERENTIAL: return org.hl7.fhir.instance.model.Condition.ConditionVerificationStatus.DIFFERENTIAL;
    case CONFIRMED: return org.hl7.fhir.instance.model.Condition.ConditionVerificationStatus.CONFIRMED;
    case REFUTED: return org.hl7.fhir.instance.model.Condition.ConditionVerificationStatus.REFUTED;
    case ENTEREDINERROR: return org.hl7.fhir.instance.model.Condition.ConditionVerificationStatus.ENTEREDINERROR;
    case UNKNOWN: return org.hl7.fhir.instance.model.Condition.ConditionVerificationStatus.UNKNOWN;
    default: return org.hl7.fhir.instance.model.Condition.ConditionVerificationStatus.NULL;
    }
  }

  public org.hl7.fhir.dstu3.model.Condition.ConditionStageComponent convertConditionStageComponent(org.hl7.fhir.instance.model.Condition.ConditionStageComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.Condition.ConditionStageComponent tgt = new org.hl7.fhir.dstu3.model.Condition.ConditionStageComponent();
    copyElement(src, tgt);
    tgt.setSummary(convertCodeableConcept(src.getSummary()));
    for (org.hl7.fhir.instance.model.Reference t : src.getAssessment())
      tgt.addAssessment(convertReference(t));
    return tgt;
  }

  public org.hl7.fhir.instance.model.Condition.ConditionStageComponent convertConditionStageComponent(org.hl7.fhir.dstu3.model.Condition.ConditionStageComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.Condition.ConditionStageComponent tgt = new org.hl7.fhir.instance.model.Condition.ConditionStageComponent();
    copyElement(src, tgt);
    tgt.setSummary(convertCodeableConcept(src.getSummary()));
    for (org.hl7.fhir.dstu3.model.Reference t : src.getAssessment())
      tgt.addAssessment(convertReference(t));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.Condition.ConditionEvidenceComponent convertConditionEvidenceComponent(org.hl7.fhir.instance.model.Condition.ConditionEvidenceComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.Condition.ConditionEvidenceComponent tgt = new org.hl7.fhir.dstu3.model.Condition.ConditionEvidenceComponent();
    copyElement(src, tgt);
    tgt.addCode(convertCodeableConcept(src.getCode()));
    for (org.hl7.fhir.instance.model.Reference t : src.getDetail())
      tgt.addDetail(convertReference(t));
    return tgt;
  }

  public org.hl7.fhir.instance.model.Condition.ConditionEvidenceComponent convertConditionEvidenceComponent(org.hl7.fhir.dstu3.model.Condition.ConditionEvidenceComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.Condition.ConditionEvidenceComponent tgt = new org.hl7.fhir.instance.model.Condition.ConditionEvidenceComponent();
    copyElement(src, tgt);
    for (org.hl7.fhir.dstu3.model.CodeableConcept cc : src.getCode())
      tgt.setCode(convertCodeableConcept(cc));
    for (org.hl7.fhir.dstu3.model.Reference t : src.getDetail())
      tgt.addDetail(convertReference(t));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.CapabilityStatement convertConformance(org.hl7.fhir.instance.model.Conformance src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.CapabilityStatement tgt = new org.hl7.fhir.dstu3.model.CapabilityStatement();
    copyDomainResource(src, tgt);
    tgt.setUrl(src.getUrl());
    tgt.setVersion(src.getVersion());
    tgt.setName(src.getName());
    tgt.setStatus(convertConformanceResourceStatus(src.getStatus()));
    if (src.hasExperimental())
      tgt.setExperimental(src.getExperimental());
    if (src.hasDate())
      tgt.setDate(src.getDate());
    tgt.setPublisher(src.getPublisher());
    for (org.hl7.fhir.instance.model.Conformance.ConformanceContactComponent t : src.getContact())
      tgt.addContact(convertConformanceContactComponent(t));
    tgt.setDescription(src.getDescription());
    tgt.setPurpose(src.getRequirements());
    tgt.setCopyright(src.getCopyright());
    tgt.setKind(convertConformanceStatementKind(src.getKind()));
    tgt.setSoftware(convertConformanceSoftwareComponent(src.getSoftware()));
    tgt.setImplementation(convertConformanceImplementationComponent(src.getImplementation()));
    tgt.setFhirVersion(src.getFhirVersion());
    tgt.setAcceptUnknown(convertUnknownContentCode(src.getAcceptUnknown()));
    for (org.hl7.fhir.instance.model.CodeType t : src.getFormat())
      tgt.addFormat(t.getValue());
    for (org.hl7.fhir.instance.model.Reference t : src.getProfile())
      tgt.addProfile(convertReference(t));
    for (org.hl7.fhir.instance.model.Conformance.ConformanceRestComponent t : src.getRest())
      tgt.addRest(convertConformanceRestComponent(t));
    for (org.hl7.fhir.instance.model.Conformance.ConformanceMessagingComponent t : src.getMessaging())
      tgt.addMessaging(convertConformanceMessagingComponent(t));
    for (org.hl7.fhir.instance.model.Conformance.ConformanceDocumentComponent t : src.getDocument())
      tgt.addDocument(convertConformanceDocumentComponent(t));
    return tgt;
  }

  public org.hl7.fhir.instance.model.Conformance convertConformance(org.hl7.fhir.dstu3.model.CapabilityStatement src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.Conformance tgt = new org.hl7.fhir.instance.model.Conformance();
    copyDomainResource(src, tgt);
    tgt.setUrl(src.getUrl());
    tgt.setVersion(src.getVersion());
    tgt.setName(src.getName());
    tgt.setStatus(convertConformanceResourceStatus(src.getStatus()));
    if (src.hasExperimental())
      tgt.setExperimental(src.getExperimental());
    if (src.hasDate())
      tgt.setDate(src.getDate());
    tgt.setPublisher(src.getPublisher());
    for (org.hl7.fhir.dstu3.model.ContactDetail t : src.getContact())
      tgt.addContact(convertConformanceContactComponent(t));
    tgt.setDescription(src.getDescription());
    tgt.setRequirements(src.getPurpose());
    tgt.setCopyright(src.getCopyright());
    tgt.setKind(convertConformanceStatementKind(src.getKind()));
    tgt.setSoftware(convertConformanceSoftwareComponent(src.getSoftware()));
    if (src.hasImplementation())
      tgt.setImplementation(convertConformanceImplementationComponent(src.getImplementation()));
    tgt.setFhirVersion(src.getFhirVersion());
    tgt.setAcceptUnknown(convertUnknownContentCode(src.getAcceptUnknown()));
    for (org.hl7.fhir.dstu3.model.CodeType t : src.getFormat())
      tgt.addFormat(t.getValue());
    for (org.hl7.fhir.dstu3.model.Reference t : src.getProfile())
      tgt.addProfile(convertReference(t));
    for (org.hl7.fhir.dstu3.model.CapabilityStatement.CapabilityStatementRestComponent t : src.getRest())
      tgt.addRest(convertConformanceRestComponent(t));
    for (org.hl7.fhir.dstu3.model.CapabilityStatement.CapabilityStatementMessagingComponent t : src.getMessaging())
      tgt.addMessaging(convertConformanceMessagingComponent(t));
    for (org.hl7.fhir.dstu3.model.CapabilityStatement.CapabilityStatementDocumentComponent t : src.getDocument())
      tgt.addDocument(convertConformanceDocumentComponent(t));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.CapabilityStatement.CapabilityStatementKind convertConformanceStatementKind(org.hl7.fhir.instance.model.Conformance.ConformanceStatementKind src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case INSTANCE: return org.hl7.fhir.dstu3.model.CapabilityStatement.CapabilityStatementKind.INSTANCE;
    case CAPABILITY: return org.hl7.fhir.dstu3.model.CapabilityStatement.CapabilityStatementKind.CAPABILITY;
    case REQUIREMENTS: return org.hl7.fhir.dstu3.model.CapabilityStatement.CapabilityStatementKind.REQUIREMENTS;
    default: return org.hl7.fhir.dstu3.model.CapabilityStatement.CapabilityStatementKind.NULL;
    }
  }

  public org.hl7.fhir.instance.model.Conformance.ConformanceStatementKind convertConformanceStatementKind(org.hl7.fhir.dstu3.model.CapabilityStatement.CapabilityStatementKind src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case INSTANCE: return org.hl7.fhir.instance.model.Conformance.ConformanceStatementKind.INSTANCE;
    case CAPABILITY: return org.hl7.fhir.instance.model.Conformance.ConformanceStatementKind.CAPABILITY;
    case REQUIREMENTS: return org.hl7.fhir.instance.model.Conformance.ConformanceStatementKind.REQUIREMENTS;
    default: return org.hl7.fhir.instance.model.Conformance.ConformanceStatementKind.NULL;
    }
  }

  public org.hl7.fhir.dstu3.model.CapabilityStatement.UnknownContentCode convertUnknownContentCode(org.hl7.fhir.instance.model.Conformance.UnknownContentCode src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case NO: return org.hl7.fhir.dstu3.model.CapabilityStatement.UnknownContentCode.NO;
    case EXTENSIONS: return org.hl7.fhir.dstu3.model.CapabilityStatement.UnknownContentCode.EXTENSIONS;
    case ELEMENTS: return org.hl7.fhir.dstu3.model.CapabilityStatement.UnknownContentCode.ELEMENTS;
    case BOTH: return org.hl7.fhir.dstu3.model.CapabilityStatement.UnknownContentCode.BOTH;
    default: return org.hl7.fhir.dstu3.model.CapabilityStatement.UnknownContentCode.NULL;
    }
  }

  public org.hl7.fhir.instance.model.Conformance.UnknownContentCode convertUnknownContentCode(org.hl7.fhir.dstu3.model.CapabilityStatement.UnknownContentCode src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case NO: return org.hl7.fhir.instance.model.Conformance.UnknownContentCode.NO;
    case EXTENSIONS: return org.hl7.fhir.instance.model.Conformance.UnknownContentCode.EXTENSIONS;
    case ELEMENTS: return org.hl7.fhir.instance.model.Conformance.UnknownContentCode.ELEMENTS;
    case BOTH: return org.hl7.fhir.instance.model.Conformance.UnknownContentCode.BOTH;
    default: return org.hl7.fhir.instance.model.Conformance.UnknownContentCode.NULL;
    }
  }

  public org.hl7.fhir.dstu3.model.ContactDetail convertConformanceContactComponent(org.hl7.fhir.instance.model.Conformance.ConformanceContactComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.ContactDetail tgt = new org.hl7.fhir.dstu3.model.ContactDetail();
    copyElement(src, tgt);
    tgt.setName(src.getName());
    for (org.hl7.fhir.instance.model.ContactPoint t : src.getTelecom())
      tgt.addTelecom(convertContactPoint(t));
    return tgt;
  }

  public org.hl7.fhir.instance.model.Conformance.ConformanceContactComponent convertConformanceContactComponent(org.hl7.fhir.dstu3.model.ContactDetail src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.Conformance.ConformanceContactComponent tgt = new org.hl7.fhir.instance.model.Conformance.ConformanceContactComponent();
    copyElement(src, tgt);
    tgt.setName(src.getName());
    for (org.hl7.fhir.dstu3.model.ContactPoint t : src.getTelecom())
      tgt.addTelecom(convertContactPoint(t));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.CapabilityStatement.CapabilityStatementSoftwareComponent convertConformanceSoftwareComponent(org.hl7.fhir.instance.model.Conformance.ConformanceSoftwareComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.CapabilityStatement.CapabilityStatementSoftwareComponent tgt = new org.hl7.fhir.dstu3.model.CapabilityStatement.CapabilityStatementSoftwareComponent();
    copyElement(src, tgt);
    tgt.setName(src.getName());
    tgt.setVersion(src.getVersion());
    tgt.setReleaseDate(src.getReleaseDate());
    return tgt;
  }

  public org.hl7.fhir.instance.model.Conformance.ConformanceSoftwareComponent convertConformanceSoftwareComponent(org.hl7.fhir.dstu3.model.CapabilityStatement.CapabilityStatementSoftwareComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.Conformance.ConformanceSoftwareComponent tgt = new org.hl7.fhir.instance.model.Conformance.ConformanceSoftwareComponent();
    copyElement(src, tgt);
    tgt.setName(src.getName());
    tgt.setVersion(src.getVersion());
    tgt.setReleaseDate(src.getReleaseDate());
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.CapabilityStatement.CapabilityStatementImplementationComponent convertConformanceImplementationComponent(org.hl7.fhir.instance.model.Conformance.ConformanceImplementationComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.CapabilityStatement.CapabilityStatementImplementationComponent tgt = new org.hl7.fhir.dstu3.model.CapabilityStatement.CapabilityStatementImplementationComponent();
    copyElement(src, tgt);
    tgt.setDescription(src.getDescription());
    tgt.setUrl(src.getUrl());
    return tgt;
  }

  public org.hl7.fhir.instance.model.Conformance.ConformanceImplementationComponent convertConformanceImplementationComponent(org.hl7.fhir.dstu3.model.CapabilityStatement.CapabilityStatementImplementationComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.Conformance.ConformanceImplementationComponent tgt = new org.hl7.fhir.instance.model.Conformance.ConformanceImplementationComponent();
    copyElement(src, tgt);
    tgt.setDescription(src.getDescription());
    tgt.setUrl(src.getUrl());
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.CapabilityStatement.CapabilityStatementRestComponent convertConformanceRestComponent(org.hl7.fhir.instance.model.Conformance.ConformanceRestComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.CapabilityStatement.CapabilityStatementRestComponent tgt = new org.hl7.fhir.dstu3.model.CapabilityStatement.CapabilityStatementRestComponent();
    copyElement(src, tgt);
    tgt.setMode(convertRestfulConformanceMode(src.getMode()));
    tgt.setDocumentation(src.getDocumentation());
    tgt.setSecurity(convertConformanceRestSecurityComponent(src.getSecurity()));
    for (org.hl7.fhir.instance.model.Conformance.ConformanceRestResourceComponent t : src.getResource())
      tgt.addResource(convertConformanceRestResourceComponent(t));
    for (org.hl7.fhir.instance.model.Conformance.SystemInteractionComponent t : src.getInteraction())
      tgt.addInteraction(convertSystemInteractionComponent(t));
    if (src.getTransactionMode() == org.hl7.fhir.instance.model.Conformance.TransactionMode.BATCH || src.getTransactionMode() == org.hl7.fhir.instance.model.Conformance.TransactionMode.BOTH)
      tgt.addInteraction().setCode(SystemRestfulInteraction.BATCH);
    for (org.hl7.fhir.instance.model.Conformance.ConformanceRestResourceSearchParamComponent t : src.getSearchParam())
      tgt.addSearchParam(convertConformanceRestResourceSearchParamComponent(t));
    for (org.hl7.fhir.instance.model.Conformance.ConformanceRestOperationComponent t : src.getOperation())
      tgt.addOperation(convertConformanceRestOperationComponent(t));
    for (org.hl7.fhir.instance.model.UriType t : src.getCompartment())
      tgt.addCompartment(t.getValue());
    return tgt;
  }

  public org.hl7.fhir.instance.model.Conformance.ConformanceRestComponent convertConformanceRestComponent(org.hl7.fhir.dstu3.model.CapabilityStatement.CapabilityStatementRestComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.Conformance.ConformanceRestComponent tgt = new org.hl7.fhir.instance.model.Conformance.ConformanceRestComponent();
    copyElement(src, tgt);
    tgt.setMode(convertRestfulConformanceMode(src.getMode()));
    tgt.setDocumentation(src.getDocumentation());
    tgt.setSecurity(convertConformanceRestSecurityComponent(src.getSecurity()));
    for (org.hl7.fhir.dstu3.model.CapabilityStatement.CapabilityStatementRestResourceComponent t : src.getResource())
      tgt.addResource(convertConformanceRestResourceComponent(t));
    boolean batch = false;
    boolean transaction = false;
    for (org.hl7.fhir.dstu3.model.CapabilityStatement.SystemInteractionComponent t : src.getInteraction()) {
      if (t.getCode().equals(SystemRestfulInteraction.BATCH))
        batch = true;
      else
        tgt.addInteraction(convertSystemInteractionComponent(t));
      if (t.getCode().equals(SystemRestfulInteraction.TRANSACTION))
        transaction = true;
    }
    if (batch)
      tgt.setTransactionMode(transaction ? org.hl7.fhir.instance.model.Conformance.TransactionMode.BOTH : org.hl7.fhir.instance.model.Conformance.TransactionMode.BATCH);
    else
      tgt.setTransactionMode(transaction ? org.hl7.fhir.instance.model.Conformance.TransactionMode.TRANSACTION : org.hl7.fhir.instance.model.Conformance.TransactionMode.NOTSUPPORTED);
    for (org.hl7.fhir.dstu3.model.CapabilityStatement.CapabilityStatementRestResourceSearchParamComponent t : src.getSearchParam())
      tgt.addSearchParam(convertConformanceRestResourceSearchParamComponent(t));
    for (org.hl7.fhir.dstu3.model.CapabilityStatement.CapabilityStatementRestOperationComponent t : src.getOperation())
      tgt.addOperation(convertConformanceRestOperationComponent(t));
    for (org.hl7.fhir.dstu3.model.UriType t : src.getCompartment())
      tgt.addCompartment(t.getValue());
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.CapabilityStatement.RestfulCapabilityMode convertRestfulConformanceMode(org.hl7.fhir.instance.model.Conformance.RestfulConformanceMode src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case CLIENT: return org.hl7.fhir.dstu3.model.CapabilityStatement.RestfulCapabilityMode.CLIENT;
    case SERVER: return org.hl7.fhir.dstu3.model.CapabilityStatement.RestfulCapabilityMode.SERVER;
    default: return org.hl7.fhir.dstu3.model.CapabilityStatement.RestfulCapabilityMode.NULL;
    }
  }

  public org.hl7.fhir.instance.model.Conformance.RestfulConformanceMode convertRestfulConformanceMode(org.hl7.fhir.dstu3.model.CapabilityStatement.RestfulCapabilityMode src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case CLIENT: return org.hl7.fhir.instance.model.Conformance.RestfulConformanceMode.CLIENT;
    case SERVER: return org.hl7.fhir.instance.model.Conformance.RestfulConformanceMode.SERVER;
    default: return org.hl7.fhir.instance.model.Conformance.RestfulConformanceMode.NULL;
    }
  }


  public org.hl7.fhir.dstu3.model.CapabilityStatement.CapabilityStatementRestSecurityComponent convertConformanceRestSecurityComponent(org.hl7.fhir.instance.model.Conformance.ConformanceRestSecurityComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.CapabilityStatement.CapabilityStatementRestSecurityComponent tgt = new org.hl7.fhir.dstu3.model.CapabilityStatement.CapabilityStatementRestSecurityComponent();
    copyElement(src, tgt);
    tgt.setCors(src.getCors());
    for (org.hl7.fhir.instance.model.CodeableConcept t : src.getService())
      tgt.addService(convertCodeableConcept(t));
    tgt.setDescription(src.getDescription());
    for (org.hl7.fhir.instance.model.Conformance.ConformanceRestSecurityCertificateComponent t : src.getCertificate())
      tgt.addCertificate(convertConformanceRestSecurityCertificateComponent(t));
    return tgt;
  }

  public org.hl7.fhir.instance.model.Conformance.ConformanceRestSecurityComponent convertConformanceRestSecurityComponent(org.hl7.fhir.dstu3.model.CapabilityStatement.CapabilityStatementRestSecurityComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.Conformance.ConformanceRestSecurityComponent tgt = new org.hl7.fhir.instance.model.Conformance.ConformanceRestSecurityComponent();
    copyElement(src, tgt);
    tgt.setCors(src.getCors());
    for (org.hl7.fhir.dstu3.model.CodeableConcept t : src.getService())
      tgt.addService(convertCodeableConcept(t));
    tgt.setDescription(src.getDescription());
    for (org.hl7.fhir.dstu3.model.CapabilityStatement.CapabilityStatementRestSecurityCertificateComponent t : src.getCertificate())
      tgt.addCertificate(convertConformanceRestSecurityCertificateComponent(t));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.CapabilityStatement.CapabilityStatementRestSecurityCertificateComponent convertConformanceRestSecurityCertificateComponent(org.hl7.fhir.instance.model.Conformance.ConformanceRestSecurityCertificateComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.CapabilityStatement.CapabilityStatementRestSecurityCertificateComponent tgt = new org.hl7.fhir.dstu3.model.CapabilityStatement.CapabilityStatementRestSecurityCertificateComponent();
    copyElement(src, tgt);
    tgt.setType(src.getType());
    tgt.setBlob(src.getBlob());
    return tgt;
  }

  public org.hl7.fhir.instance.model.Conformance.ConformanceRestSecurityCertificateComponent convertConformanceRestSecurityCertificateComponent(org.hl7.fhir.dstu3.model.CapabilityStatement.CapabilityStatementRestSecurityCertificateComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.Conformance.ConformanceRestSecurityCertificateComponent tgt = new org.hl7.fhir.instance.model.Conformance.ConformanceRestSecurityCertificateComponent();
    copyElement(src, tgt);
    tgt.setType(src.getType());
    tgt.setBlob(src.getBlob());
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.CapabilityStatement.CapabilityStatementRestResourceComponent convertConformanceRestResourceComponent(org.hl7.fhir.instance.model.Conformance.ConformanceRestResourceComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.CapabilityStatement.CapabilityStatementRestResourceComponent tgt = new org.hl7.fhir.dstu3.model.CapabilityStatement.CapabilityStatementRestResourceComponent();
    copyElement(src, tgt);
    tgt.setType(src.getType());
    tgt.setProfile(convertReference(src.getProfile()));
    for (org.hl7.fhir.instance.model.Conformance.ResourceInteractionComponent t : src.getInteraction())
      tgt.addInteraction(convertResourceInteractionComponent(t));
    tgt.setVersioning(convertResourceVersionPolicy(src.getVersioning()));
    tgt.setReadHistory(src.getReadHistory());
    tgt.setUpdateCreate(src.getUpdateCreate());
    tgt.setConditionalCreate(src.getConditionalCreate());
    tgt.setConditionalUpdate(src.getConditionalUpdate());
    tgt.setConditionalDelete(convertConditionalDeleteStatus(src.getConditionalDelete()));
    for (org.hl7.fhir.instance.model.StringType t : src.getSearchInclude())
      tgt.addSearchInclude(t.getValue());
    for (org.hl7.fhir.instance.model.StringType t : src.getSearchRevInclude())
      tgt.addSearchRevInclude(t.getValue());
    for (org.hl7.fhir.instance.model.Conformance.ConformanceRestResourceSearchParamComponent t : src.getSearchParam())
      tgt.addSearchParam(convertConformanceRestResourceSearchParamComponent(t));
    return tgt;
  }

  public org.hl7.fhir.instance.model.Conformance.ConformanceRestResourceComponent convertConformanceRestResourceComponent(org.hl7.fhir.dstu3.model.CapabilityStatement.CapabilityStatementRestResourceComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.Conformance.ConformanceRestResourceComponent tgt = new org.hl7.fhir.instance.model.Conformance.ConformanceRestResourceComponent();
    copyElement(src, tgt);
    tgt.setType(src.getType());
    if (src.hasProfile())
      tgt.setProfile(convertReference(src.getProfile()));
    for (org.hl7.fhir.dstu3.model.CapabilityStatement.ResourceInteractionComponent t : src.getInteraction())
      tgt.addInteraction(convertResourceInteractionComponent(t));
    tgt.setVersioning(convertResourceVersionPolicy(src.getVersioning()));
    tgt.setReadHistory(src.getReadHistory());
    tgt.setUpdateCreate(src.getUpdateCreate());
    tgt.setConditionalCreate(src.getConditionalCreate());
    tgt.setConditionalUpdate(src.getConditionalUpdate());
    tgt.setConditionalDelete(convertConditionalDeleteStatus(src.getConditionalDelete()));
    for (org.hl7.fhir.dstu3.model.StringType t : src.getSearchInclude())
      tgt.addSearchInclude(t.getValue());
    for (org.hl7.fhir.dstu3.model.StringType t : src.getSearchRevInclude())
      tgt.addSearchRevInclude(t.getValue());
    for (org.hl7.fhir.dstu3.model.CapabilityStatement.CapabilityStatementRestResourceSearchParamComponent t : src.getSearchParam())
      tgt.addSearchParam(convertConformanceRestResourceSearchParamComponent(t));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.CapabilityStatement.ResourceVersionPolicy convertResourceVersionPolicy(org.hl7.fhir.instance.model.Conformance.ResourceVersionPolicy src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case NOVERSION: return org.hl7.fhir.dstu3.model.CapabilityStatement.ResourceVersionPolicy.NOVERSION;
    case VERSIONED: return org.hl7.fhir.dstu3.model.CapabilityStatement.ResourceVersionPolicy.VERSIONED;
    case VERSIONEDUPDATE: return org.hl7.fhir.dstu3.model.CapabilityStatement.ResourceVersionPolicy.VERSIONEDUPDATE;
    default: return org.hl7.fhir.dstu3.model.CapabilityStatement.ResourceVersionPolicy.NULL;
    }
  }

  public org.hl7.fhir.instance.model.Conformance.ResourceVersionPolicy convertResourceVersionPolicy(org.hl7.fhir.dstu3.model.CapabilityStatement.ResourceVersionPolicy src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case NOVERSION: return org.hl7.fhir.instance.model.Conformance.ResourceVersionPolicy.NOVERSION;
    case VERSIONED: return org.hl7.fhir.instance.model.Conformance.ResourceVersionPolicy.VERSIONED;
    case VERSIONEDUPDATE: return org.hl7.fhir.instance.model.Conformance.ResourceVersionPolicy.VERSIONEDUPDATE;
    default: return org.hl7.fhir.instance.model.Conformance.ResourceVersionPolicy.NULL;
    }
  }

  public org.hl7.fhir.dstu3.model.CapabilityStatement.ConditionalDeleteStatus convertConditionalDeleteStatus(org.hl7.fhir.instance.model.Conformance.ConditionalDeleteStatus src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case NOTSUPPORTED: return org.hl7.fhir.dstu3.model.CapabilityStatement.ConditionalDeleteStatus.NOTSUPPORTED;
    case SINGLE: return org.hl7.fhir.dstu3.model.CapabilityStatement.ConditionalDeleteStatus.SINGLE;
    case MULTIPLE: return org.hl7.fhir.dstu3.model.CapabilityStatement.ConditionalDeleteStatus.MULTIPLE;
    default: return org.hl7.fhir.dstu3.model.CapabilityStatement.ConditionalDeleteStatus.NULL;
    }
  }

  public org.hl7.fhir.instance.model.Conformance.ConditionalDeleteStatus convertConditionalDeleteStatus(org.hl7.fhir.dstu3.model.CapabilityStatement.ConditionalDeleteStatus src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case NOTSUPPORTED: return org.hl7.fhir.instance.model.Conformance.ConditionalDeleteStatus.NOTSUPPORTED;
    case SINGLE: return org.hl7.fhir.instance.model.Conformance.ConditionalDeleteStatus.SINGLE;
    case MULTIPLE: return org.hl7.fhir.instance.model.Conformance.ConditionalDeleteStatus.MULTIPLE;
    default: return org.hl7.fhir.instance.model.Conformance.ConditionalDeleteStatus.NULL;
    }
  }

  public org.hl7.fhir.dstu3.model.CapabilityStatement.ResourceInteractionComponent convertResourceInteractionComponent(org.hl7.fhir.instance.model.Conformance.ResourceInteractionComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.CapabilityStatement.ResourceInteractionComponent tgt = new org.hl7.fhir.dstu3.model.CapabilityStatement.ResourceInteractionComponent();
    copyElement(src, tgt);
    tgt.setCode(convertTypeRestfulInteraction(src.getCode()));
    tgt.setDocumentation(src.getDocumentation());
    return tgt;
  }

  public org.hl7.fhir.instance.model.Conformance.ResourceInteractionComponent convertResourceInteractionComponent(org.hl7.fhir.dstu3.model.CapabilityStatement.ResourceInteractionComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.Conformance.ResourceInteractionComponent tgt = new org.hl7.fhir.instance.model.Conformance.ResourceInteractionComponent();
    copyElement(src, tgt);
    tgt.setCode(convertTypeRestfulInteraction(src.getCode()));
    tgt.setDocumentation(src.getDocumentation());
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.CapabilityStatement.TypeRestfulInteraction convertTypeRestfulInteraction(org.hl7.fhir.instance.model.Conformance.TypeRestfulInteraction src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case READ: return org.hl7.fhir.dstu3.model.CapabilityStatement.TypeRestfulInteraction.READ;
    case VREAD: return org.hl7.fhir.dstu3.model.CapabilityStatement.TypeRestfulInteraction.VREAD;
    case UPDATE: return org.hl7.fhir.dstu3.model.CapabilityStatement.TypeRestfulInteraction.UPDATE;
    case DELETE: return org.hl7.fhir.dstu3.model.CapabilityStatement.TypeRestfulInteraction.DELETE;
    case HISTORYINSTANCE: return org.hl7.fhir.dstu3.model.CapabilityStatement.TypeRestfulInteraction.HISTORYINSTANCE;
    case HISTORYTYPE: return org.hl7.fhir.dstu3.model.CapabilityStatement.TypeRestfulInteraction.HISTORYTYPE;
    case CREATE: return org.hl7.fhir.dstu3.model.CapabilityStatement.TypeRestfulInteraction.CREATE;
    case SEARCHTYPE: return org.hl7.fhir.dstu3.model.CapabilityStatement.TypeRestfulInteraction.SEARCHTYPE;
    default: return org.hl7.fhir.dstu3.model.CapabilityStatement.TypeRestfulInteraction.NULL;
    }
  }

  public org.hl7.fhir.instance.model.Conformance.TypeRestfulInteraction convertTypeRestfulInteraction(org.hl7.fhir.dstu3.model.CapabilityStatement.TypeRestfulInteraction src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case READ: return org.hl7.fhir.instance.model.Conformance.TypeRestfulInteraction.READ;
    case VREAD: return org.hl7.fhir.instance.model.Conformance.TypeRestfulInteraction.VREAD;
    case UPDATE: return org.hl7.fhir.instance.model.Conformance.TypeRestfulInteraction.UPDATE;
    case DELETE: return org.hl7.fhir.instance.model.Conformance.TypeRestfulInteraction.DELETE;
    case HISTORYINSTANCE: return org.hl7.fhir.instance.model.Conformance.TypeRestfulInteraction.HISTORYINSTANCE;
    case HISTORYTYPE: return org.hl7.fhir.instance.model.Conformance.TypeRestfulInteraction.HISTORYTYPE;
    case CREATE: return org.hl7.fhir.instance.model.Conformance.TypeRestfulInteraction.CREATE;
    case SEARCHTYPE: return org.hl7.fhir.instance.model.Conformance.TypeRestfulInteraction.SEARCHTYPE;
    default: return org.hl7.fhir.instance.model.Conformance.TypeRestfulInteraction.NULL;
    }
  }

  public org.hl7.fhir.dstu3.model.CapabilityStatement.CapabilityStatementRestResourceSearchParamComponent convertConformanceRestResourceSearchParamComponent(org.hl7.fhir.instance.model.Conformance.ConformanceRestResourceSearchParamComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.CapabilityStatement.CapabilityStatementRestResourceSearchParamComponent tgt = new org.hl7.fhir.dstu3.model.CapabilityStatement.CapabilityStatementRestResourceSearchParamComponent();
    copyElement(src, tgt);
    tgt.setName(src.getName());
    tgt.setDefinition(src.getDefinition());
    tgt.setType(convertSearchParamType(src.getType()));
    tgt.setDocumentation(src.getDocumentation());
    return tgt;
  }

  public org.hl7.fhir.instance.model.Conformance.ConformanceRestResourceSearchParamComponent convertConformanceRestResourceSearchParamComponent(org.hl7.fhir.dstu3.model.CapabilityStatement.CapabilityStatementRestResourceSearchParamComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.Conformance.ConformanceRestResourceSearchParamComponent tgt = new org.hl7.fhir.instance.model.Conformance.ConformanceRestResourceSearchParamComponent();
    copyElement(src, tgt);
    tgt.setName(src.getName());
    tgt.setDefinition(src.getDefinition());
    tgt.setType(convertSearchParamType(src.getType()));
    tgt.setDocumentation(src.getDocumentation());
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.CapabilityStatement.SystemInteractionComponent convertSystemInteractionComponent(org.hl7.fhir.instance.model.Conformance.SystemInteractionComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.CapabilityStatement.SystemInteractionComponent tgt = new org.hl7.fhir.dstu3.model.CapabilityStatement.SystemInteractionComponent();
    copyElement(src, tgt);
    tgt.setCode(convertSystemRestfulInteraction(src.getCode()));
    tgt.setDocumentation(src.getDocumentation());
    return tgt;
  }

  public org.hl7.fhir.instance.model.Conformance.SystemInteractionComponent convertSystemInteractionComponent(org.hl7.fhir.dstu3.model.CapabilityStatement.SystemInteractionComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.Conformance.SystemInteractionComponent tgt = new org.hl7.fhir.instance.model.Conformance.SystemInteractionComponent();
    copyElement(src, tgt);
    tgt.setCode(convertSystemRestfulInteraction(src.getCode()));
    tgt.setDocumentation(src.getDocumentation());
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.CapabilityStatement.SystemRestfulInteraction convertSystemRestfulInteraction(org.hl7.fhir.instance.model.Conformance.SystemRestfulInteraction src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case TRANSACTION: return org.hl7.fhir.dstu3.model.CapabilityStatement.SystemRestfulInteraction.TRANSACTION;
    case SEARCHSYSTEM: return org.hl7.fhir.dstu3.model.CapabilityStatement.SystemRestfulInteraction.SEARCHSYSTEM;
    case HISTORYSYSTEM: return org.hl7.fhir.dstu3.model.CapabilityStatement.SystemRestfulInteraction.HISTORYSYSTEM;
    default: return org.hl7.fhir.dstu3.model.CapabilityStatement.SystemRestfulInteraction.NULL;
    }
  }

  public org.hl7.fhir.instance.model.Conformance.SystemRestfulInteraction convertSystemRestfulInteraction(org.hl7.fhir.dstu3.model.CapabilityStatement.SystemRestfulInteraction src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case TRANSACTION: return org.hl7.fhir.instance.model.Conformance.SystemRestfulInteraction.TRANSACTION;
    case SEARCHSYSTEM: return org.hl7.fhir.instance.model.Conformance.SystemRestfulInteraction.SEARCHSYSTEM;
    case HISTORYSYSTEM: return org.hl7.fhir.instance.model.Conformance.SystemRestfulInteraction.HISTORYSYSTEM;
    default: return org.hl7.fhir.instance.model.Conformance.SystemRestfulInteraction.NULL;
    }
  }

  public org.hl7.fhir.dstu3.model.CapabilityStatement.CapabilityStatementRestOperationComponent convertConformanceRestOperationComponent(org.hl7.fhir.instance.model.Conformance.ConformanceRestOperationComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.CapabilityStatement.CapabilityStatementRestOperationComponent tgt = new org.hl7.fhir.dstu3.model.CapabilityStatement.CapabilityStatementRestOperationComponent();
    copyElement(src, tgt);
    tgt.setName(src.getName());
    tgt.setDefinition(convertReference(src.getDefinition()));
    return tgt;
  }

  public org.hl7.fhir.instance.model.Conformance.ConformanceRestOperationComponent convertConformanceRestOperationComponent(org.hl7.fhir.dstu3.model.CapabilityStatement.CapabilityStatementRestOperationComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.Conformance.ConformanceRestOperationComponent tgt = new org.hl7.fhir.instance.model.Conformance.ConformanceRestOperationComponent();
    copyElement(src, tgt);
    tgt.setName(src.getName());
    tgt.setDefinition(convertReference(src.getDefinition()));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.CapabilityStatement.CapabilityStatementMessagingComponent convertConformanceMessagingComponent(org.hl7.fhir.instance.model.Conformance.ConformanceMessagingComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.CapabilityStatement.CapabilityStatementMessagingComponent tgt = new org.hl7.fhir.dstu3.model.CapabilityStatement.CapabilityStatementMessagingComponent();
    copyElement(src, tgt);
    for (org.hl7.fhir.instance.model.Conformance.ConformanceMessagingEndpointComponent t : src.getEndpoint())
      tgt.addEndpoint(convertConformanceMessagingEndpointComponent(t));
    tgt.setReliableCache(src.getReliableCache());
    tgt.setDocumentation(src.getDocumentation());
    for (org.hl7.fhir.instance.model.Conformance.ConformanceMessagingEventComponent t : src.getEvent())
      tgt.addEvent(convertConformanceMessagingEventComponent(t));
    return tgt;
  }

  public org.hl7.fhir.instance.model.Conformance.ConformanceMessagingComponent convertConformanceMessagingComponent(org.hl7.fhir.dstu3.model.CapabilityStatement.CapabilityStatementMessagingComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.Conformance.ConformanceMessagingComponent tgt = new org.hl7.fhir.instance.model.Conformance.ConformanceMessagingComponent();
    copyElement(src, tgt);
    for (org.hl7.fhir.dstu3.model.CapabilityStatement.CapabilityStatementMessagingEndpointComponent t : src.getEndpoint())
      tgt.addEndpoint(convertConformanceMessagingEndpointComponent(t));
    tgt.setReliableCache(src.getReliableCache());
    tgt.setDocumentation(src.getDocumentation());
    for (org.hl7.fhir.dstu3.model.CapabilityStatement.CapabilityStatementMessagingEventComponent t : src.getEvent())
      tgt.addEvent(convertConformanceMessagingEventComponent(t));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.CapabilityStatement.CapabilityStatementMessagingEndpointComponent convertConformanceMessagingEndpointComponent(org.hl7.fhir.instance.model.Conformance.ConformanceMessagingEndpointComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.CapabilityStatement.CapabilityStatementMessagingEndpointComponent tgt = new org.hl7.fhir.dstu3.model.CapabilityStatement.CapabilityStatementMessagingEndpointComponent();
    copyElement(src, tgt);
    tgt.setProtocol(convertCoding(src.getProtocol()));
    tgt.setAddress(src.getAddress());
    return tgt;
  }

  public org.hl7.fhir.instance.model.Conformance.ConformanceMessagingEndpointComponent convertConformanceMessagingEndpointComponent(org.hl7.fhir.dstu3.model.CapabilityStatement.CapabilityStatementMessagingEndpointComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.Conformance.ConformanceMessagingEndpointComponent tgt = new org.hl7.fhir.instance.model.Conformance.ConformanceMessagingEndpointComponent();
    copyElement(src, tgt);
    tgt.setProtocol(convertCoding(src.getProtocol()));
    tgt.setAddress(src.getAddress());
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.CapabilityStatement.CapabilityStatementMessagingEventComponent convertConformanceMessagingEventComponent(org.hl7.fhir.instance.model.Conformance.ConformanceMessagingEventComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.CapabilityStatement.CapabilityStatementMessagingEventComponent tgt = new org.hl7.fhir.dstu3.model.CapabilityStatement.CapabilityStatementMessagingEventComponent();
    copyElement(src, tgt);
    tgt.setCode(convertCoding(src.getCode()));
    tgt.setCategory(convertMessageSignificanceCategory(src.getCategory()));
    tgt.setMode(convertConformanceEventMode(src.getMode()));
    tgt.setFocus(src.getFocus());
    tgt.setRequest(convertReference(src.getRequest()));
    tgt.setResponse(convertReference(src.getResponse()));
    tgt.setDocumentation(src.getDocumentation());
    return tgt;
  }

  public org.hl7.fhir.instance.model.Conformance.ConformanceMessagingEventComponent convertConformanceMessagingEventComponent(org.hl7.fhir.dstu3.model.CapabilityStatement.CapabilityStatementMessagingEventComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.Conformance.ConformanceMessagingEventComponent tgt = new org.hl7.fhir.instance.model.Conformance.ConformanceMessagingEventComponent();
    copyElement(src, tgt);
    tgt.setCode(convertCoding(src.getCode()));
    tgt.setCategory(convertMessageSignificanceCategory(src.getCategory()));
    tgt.setMode(convertConformanceEventMode(src.getMode()));
    tgt.setFocus(src.getFocus());
    tgt.setRequest(convertReference(src.getRequest()));
    tgt.setResponse(convertReference(src.getResponse()));
    tgt.setDocumentation(src.getDocumentation());
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.CapabilityStatement.MessageSignificanceCategory convertMessageSignificanceCategory(org.hl7.fhir.instance.model.Conformance.MessageSignificanceCategory src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case CONSEQUENCE: return org.hl7.fhir.dstu3.model.CapabilityStatement.MessageSignificanceCategory.CONSEQUENCE;
    case CURRENCY: return org.hl7.fhir.dstu3.model.CapabilityStatement.MessageSignificanceCategory.CURRENCY;
    case NOTIFICATION: return org.hl7.fhir.dstu3.model.CapabilityStatement.MessageSignificanceCategory.NOTIFICATION;
    default: return org.hl7.fhir.dstu3.model.CapabilityStatement.MessageSignificanceCategory.NULL;
    }
  }

  public org.hl7.fhir.instance.model.Conformance.MessageSignificanceCategory convertMessageSignificanceCategory(org.hl7.fhir.dstu3.model.CapabilityStatement.MessageSignificanceCategory src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case CONSEQUENCE: return org.hl7.fhir.instance.model.Conformance.MessageSignificanceCategory.CONSEQUENCE;
    case CURRENCY: return org.hl7.fhir.instance.model.Conformance.MessageSignificanceCategory.CURRENCY;
    case NOTIFICATION: return org.hl7.fhir.instance.model.Conformance.MessageSignificanceCategory.NOTIFICATION;
    default: return org.hl7.fhir.instance.model.Conformance.MessageSignificanceCategory.NULL;
    }
  }

  public org.hl7.fhir.dstu3.model.CapabilityStatement.EventCapabilityMode convertConformanceEventMode(org.hl7.fhir.instance.model.Conformance.ConformanceEventMode src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case SENDER: return org.hl7.fhir.dstu3.model.CapabilityStatement.EventCapabilityMode.SENDER;
    case RECEIVER: return org.hl7.fhir.dstu3.model.CapabilityStatement.EventCapabilityMode.RECEIVER;
    default: return org.hl7.fhir.dstu3.model.CapabilityStatement.EventCapabilityMode.NULL;
    }
  }

  public org.hl7.fhir.instance.model.Conformance.ConformanceEventMode convertConformanceEventMode(org.hl7.fhir.dstu3.model.CapabilityStatement.EventCapabilityMode src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case SENDER: return org.hl7.fhir.instance.model.Conformance.ConformanceEventMode.SENDER;
    case RECEIVER: return org.hl7.fhir.instance.model.Conformance.ConformanceEventMode.RECEIVER;
    default: return org.hl7.fhir.instance.model.Conformance.ConformanceEventMode.NULL;
    }
  }

  public org.hl7.fhir.dstu3.model.CapabilityStatement.CapabilityStatementDocumentComponent convertConformanceDocumentComponent(org.hl7.fhir.instance.model.Conformance.ConformanceDocumentComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.CapabilityStatement.CapabilityStatementDocumentComponent tgt = new org.hl7.fhir.dstu3.model.CapabilityStatement.CapabilityStatementDocumentComponent();
    copyElement(src, tgt);
    tgt.setMode(convertDocumentMode(src.getMode()));
    tgt.setDocumentation(src.getDocumentation());
    tgt.setProfile(convertReference(src.getProfile()));
    return tgt;
  }

  public org.hl7.fhir.instance.model.Conformance.ConformanceDocumentComponent convertConformanceDocumentComponent(org.hl7.fhir.dstu3.model.CapabilityStatement.CapabilityStatementDocumentComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.Conformance.ConformanceDocumentComponent tgt = new org.hl7.fhir.instance.model.Conformance.ConformanceDocumentComponent();
    copyElement(src, tgt);
    tgt.setMode(convertDocumentMode(src.getMode()));
    tgt.setDocumentation(src.getDocumentation());
    tgt.setProfile(convertReference(src.getProfile()));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.CapabilityStatement.DocumentMode convertDocumentMode(org.hl7.fhir.instance.model.Conformance.DocumentMode src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case PRODUCER: return org.hl7.fhir.dstu3.model.CapabilityStatement.DocumentMode.PRODUCER;
    case CONSUMER: return org.hl7.fhir.dstu3.model.CapabilityStatement.DocumentMode.CONSUMER;
    default: return org.hl7.fhir.dstu3.model.CapabilityStatement.DocumentMode.NULL;
    }
  }

  public org.hl7.fhir.instance.model.Conformance.DocumentMode convertDocumentMode(org.hl7.fhir.dstu3.model.CapabilityStatement.DocumentMode src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case PRODUCER: return org.hl7.fhir.instance.model.Conformance.DocumentMode.PRODUCER;
    case CONSUMER: return org.hl7.fhir.instance.model.Conformance.DocumentMode.CONSUMER;
    default: return org.hl7.fhir.instance.model.Conformance.DocumentMode.NULL;
    }
  }

  public org.hl7.fhir.dstu3.model.Contract convertContract(org.hl7.fhir.instance.model.Contract src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.Contract tgt = new org.hl7.fhir.dstu3.model.Contract();
    copyDomainResource(src, tgt);
    tgt.setIdentifier(convertIdentifier(src.getIdentifier()));
    tgt.setIssued(src.getIssued());
    tgt.setApplies(convertPeriod(src.getApplies()));
    for (org.hl7.fhir.instance.model.Reference t : src.getSubject())
      tgt.addSubject(convertReference(t));
    for (org.hl7.fhir.instance.model.Reference t : src.getAuthority())
      tgt.addAuthority(convertReference(t));
    for (org.hl7.fhir.instance.model.Reference t : src.getDomain())
      tgt.addDomain(convertReference(t));
    tgt.setType(convertCodeableConcept(src.getType()));
    for (org.hl7.fhir.instance.model.CodeableConcept t : src.getSubType())
      tgt.addSubType(convertCodeableConcept(t));
    for (org.hl7.fhir.instance.model.CodeableConcept t : src.getAction())
      tgt.addAction(convertCodeableConcept(t));
    for (org.hl7.fhir.instance.model.CodeableConcept t : src.getActionReason())
      tgt.addActionReason(convertCodeableConcept(t));
    for (org.hl7.fhir.instance.model.Contract.ActorComponent t : src.getActor())
      tgt.addAgent(convertAgentComponent(t));
    for (org.hl7.fhir.instance.model.Contract.SignatoryComponent t : src.getSigner())
      tgt.addSigner(convertSignatoryComponent(t));
    for (org.hl7.fhir.instance.model.Contract.ValuedItemComponent t : src.getValuedItem())
      tgt.addValuedItem(convertValuedItemComponent(t));
    for (org.hl7.fhir.instance.model.Contract.TermComponent t : src.getTerm())
      tgt.addTerm(convertTermComponent(t));
    tgt.setBinding(convertType(src.getBinding()));
    for (org.hl7.fhir.instance.model.Contract.FriendlyLanguageComponent t : src.getFriendly())
      tgt.addFriendly(convertFriendlyLanguageComponent(t));
    for (org.hl7.fhir.instance.model.Contract.LegalLanguageComponent t : src.getLegal())
      tgt.addLegal(convertLegalLanguageComponent(t));
    for (org.hl7.fhir.instance.model.Contract.ComputableLanguageComponent t : src.getRule())
      tgt.addRule(convertComputableLanguageComponent(t));
    return tgt;
  }

  public org.hl7.fhir.instance.model.Contract convertContract(org.hl7.fhir.dstu3.model.Contract src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.Contract tgt = new org.hl7.fhir.instance.model.Contract();
    copyDomainResource(src, tgt);
    tgt.setIdentifier(convertIdentifier(src.getIdentifier()));
    tgt.setIssued(src.getIssued());
    tgt.setApplies(convertPeriod(src.getApplies()));
    for (org.hl7.fhir.dstu3.model.Reference t : src.getSubject())
      tgt.addSubject(convertReference(t));
    for (org.hl7.fhir.dstu3.model.Reference t : src.getAuthority())
      tgt.addAuthority(convertReference(t));
    for (org.hl7.fhir.dstu3.model.Reference t : src.getDomain())
      tgt.addDomain(convertReference(t));
    tgt.setType(convertCodeableConcept(src.getType()));
    for (org.hl7.fhir.dstu3.model.CodeableConcept t : src.getSubType())
      tgt.addSubType(convertCodeableConcept(t));
    for (org.hl7.fhir.dstu3.model.CodeableConcept t : src.getAction())
      tgt.addAction(convertCodeableConcept(t));
    for (org.hl7.fhir.dstu3.model.CodeableConcept t : src.getActionReason())
      tgt.addActionReason(convertCodeableConcept(t));
    for (org.hl7.fhir.dstu3.model.Contract.AgentComponent t : src.getAgent())
      tgt.addActor(convertAgentComponent(t));
    for (org.hl7.fhir.dstu3.model.Contract.SignatoryComponent t : src.getSigner())
      tgt.addSigner(convertSignatoryComponent(t));
    for (org.hl7.fhir.dstu3.model.Contract.ValuedItemComponent t : src.getValuedItem())
      tgt.addValuedItem(convertValuedItemComponent(t));
    for (org.hl7.fhir.dstu3.model.Contract.TermComponent t : src.getTerm())
      tgt.addTerm(convertTermComponent(t));
    tgt.setBinding(convertType(src.getBinding()));
    for (org.hl7.fhir.dstu3.model.Contract.FriendlyLanguageComponent t : src.getFriendly())
      tgt.addFriendly(convertFriendlyLanguageComponent(t));
    for (org.hl7.fhir.dstu3.model.Contract.LegalLanguageComponent t : src.getLegal())
      tgt.addLegal(convertLegalLanguageComponent(t));
    for (org.hl7.fhir.dstu3.model.Contract.ComputableLanguageComponent t : src.getRule())
      tgt.addRule(convertComputableLanguageComponent(t));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.Contract.AgentComponent convertAgentComponent(org.hl7.fhir.instance.model.Contract.ActorComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.Contract.AgentComponent tgt = new org.hl7.fhir.dstu3.model.Contract.AgentComponent();
    copyElement(src, tgt);
    tgt.setActor(convertReference(src.getEntity()));
    for (org.hl7.fhir.instance.model.CodeableConcept t : src.getRole())
      tgt.addRole(convertCodeableConcept(t));
    return tgt;
  }

  public org.hl7.fhir.instance.model.Contract.ActorComponent convertAgentComponent(org.hl7.fhir.dstu3.model.Contract.AgentComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.Contract.ActorComponent tgt = new org.hl7.fhir.instance.model.Contract.ActorComponent();
    copyElement(src, tgt);
    tgt.setEntity(convertReference(src.getActor()));
    for (org.hl7.fhir.dstu3.model.CodeableConcept t : src.getRole())
      tgt.addRole(convertCodeableConcept(t));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.Contract.SignatoryComponent convertSignatoryComponent(org.hl7.fhir.instance.model.Contract.SignatoryComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.Contract.SignatoryComponent tgt = new org.hl7.fhir.dstu3.model.Contract.SignatoryComponent();
    copyElement(src, tgt);
    tgt.setType(convertCoding(src.getType()));
    tgt.setParty(convertReference(src.getParty()));
    if (src.hasSignature())
      tgt.addSignature(new org.hl7.fhir.dstu3.model.Signature().setBlob(src.getSignature().getBytes()));
    return tgt;
  }

  public org.hl7.fhir.instance.model.Contract.SignatoryComponent convertSignatoryComponent(org.hl7.fhir.dstu3.model.Contract.SignatoryComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.Contract.SignatoryComponent tgt = new org.hl7.fhir.instance.model.Contract.SignatoryComponent();
    copyElement(src, tgt);
    tgt.setType(convertCoding(src.getType()));
    tgt.setParty(convertReference(src.getParty()));
    for (org.hl7.fhir.dstu3.model.Signature t : src.getSignature())
      tgt.setSignature(Base64.encodeBase64String(t.getBlob()));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.Contract.ValuedItemComponent convertValuedItemComponent(org.hl7.fhir.instance.model.Contract.ValuedItemComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.Contract.ValuedItemComponent tgt = new org.hl7.fhir.dstu3.model.Contract.ValuedItemComponent();
    copyElement(src, tgt);
    tgt.setEntity(convertType(src.getEntity()));
    tgt.setIdentifier(convertIdentifier(src.getIdentifier()));
    tgt.setEffectiveTime(src.getEffectiveTime());
    tgt.setQuantity(convertSimpleQuantity(src.getQuantity()));
    tgt.setUnitPrice(convertMoney(src.getUnitPrice()));
    tgt.setFactor(src.getFactor());
    tgt.setPoints(src.getPoints());
    tgt.setNet(convertMoney(src.getNet()));
    return tgt;
  }

  public org.hl7.fhir.instance.model.Contract.ValuedItemComponent convertValuedItemComponent(org.hl7.fhir.dstu3.model.Contract.ValuedItemComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.Contract.ValuedItemComponent tgt = new org.hl7.fhir.instance.model.Contract.ValuedItemComponent();
    copyElement(src, tgt);
    tgt.setEntity(convertType(src.getEntity()));
    tgt.setIdentifier(convertIdentifier(src.getIdentifier()));
    tgt.setEffectiveTime(src.getEffectiveTime());
    tgt.setQuantity(convertSimpleQuantity(src.getQuantity()));
    tgt.setUnitPrice(convertMoney(src.getUnitPrice()));
    tgt.setFactor(src.getFactor());
    tgt.setPoints(src.getPoints());
    tgt.setNet(convertMoney(src.getNet()));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.Contract.TermComponent convertTermComponent(org.hl7.fhir.instance.model.Contract.TermComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.Contract.TermComponent tgt = new org.hl7.fhir.dstu3.model.Contract.TermComponent();
    copyElement(src, tgt);
    tgt.setIdentifier(convertIdentifier(src.getIdentifier()));
    tgt.setIssued(src.getIssued());
    tgt.setApplies(convertPeriod(src.getApplies()));
    tgt.setType(convertCodeableConcept(src.getType()));
    tgt.setSubType(convertCodeableConcept(src.getSubType()));
    for (org.hl7.fhir.instance.model.CodeableConcept t : src.getAction())
      tgt.addAction(convertCodeableConcept(t));
    for (org.hl7.fhir.instance.model.CodeableConcept t : src.getActionReason())
      tgt.addActionReason(convertCodeableConcept(t));
    for (org.hl7.fhir.instance.model.Contract.TermActorComponent t : src.getActor())
      tgt.addAgent(convertTermAgentComponent(t));
    tgt.setText(src.getText());
    for (org.hl7.fhir.instance.model.Contract.TermValuedItemComponent t : src.getValuedItem())
      tgt.addValuedItem(convertTermValuedItemComponent(t));
    for (org.hl7.fhir.instance.model.Contract.TermComponent t : src.getGroup())
      tgt.addGroup(convertTermComponent(t));
    return tgt;
  }

  public org.hl7.fhir.instance.model.Contract.TermComponent convertTermComponent(org.hl7.fhir.dstu3.model.Contract.TermComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.Contract.TermComponent tgt = new org.hl7.fhir.instance.model.Contract.TermComponent();
    copyElement(src, tgt);
    tgt.setIdentifier(convertIdentifier(src.getIdentifier()));
    tgt.setIssued(src.getIssued());
    tgt.setApplies(convertPeriod(src.getApplies()));
    tgt.setType(convertCodeableConcept(src.getType()));
    tgt.setSubType(convertCodeableConcept(src.getSubType()));
    for (org.hl7.fhir.dstu3.model.CodeableConcept t : src.getAction())
      tgt.addAction(convertCodeableConcept(t));
    for (org.hl7.fhir.dstu3.model.CodeableConcept t : src.getActionReason())
      tgt.addActionReason(convertCodeableConcept(t));
    for (org.hl7.fhir.dstu3.model.Contract.TermAgentComponent t : src.getAgent())
      tgt.addActor(convertTermAgentComponent(t));
    tgt.setText(src.getText());
    for (org.hl7.fhir.dstu3.model.Contract.TermValuedItemComponent t : src.getValuedItem())
      tgt.addValuedItem(convertTermValuedItemComponent(t));
    for (org.hl7.fhir.dstu3.model.Contract.TermComponent t : src.getGroup())
      tgt.addGroup(convertTermComponent(t));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.Contract.TermAgentComponent convertTermAgentComponent(org.hl7.fhir.instance.model.Contract.TermActorComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.Contract.TermAgentComponent tgt = new org.hl7.fhir.dstu3.model.Contract.TermAgentComponent();
    copyElement(src, tgt);
    tgt.setActor(convertReference(src.getEntity()));
    for (org.hl7.fhir.instance.model.CodeableConcept t : src.getRole())
      tgt.addRole(convertCodeableConcept(t));
    return tgt;
  }

  public org.hl7.fhir.instance.model.Contract.TermActorComponent convertTermAgentComponent(org.hl7.fhir.dstu3.model.Contract.TermAgentComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.Contract.TermActorComponent tgt = new org.hl7.fhir.instance.model.Contract.TermActorComponent();
    copyElement(src, tgt);
    tgt.setEntity(convertReference(src.getActor()));
    for (org.hl7.fhir.dstu3.model.CodeableConcept t : src.getRole())
      tgt.addRole(convertCodeableConcept(t));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.Contract.TermValuedItemComponent convertTermValuedItemComponent(org.hl7.fhir.instance.model.Contract.TermValuedItemComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.Contract.TermValuedItemComponent tgt = new org.hl7.fhir.dstu3.model.Contract.TermValuedItemComponent();
    copyElement(src, tgt);
    tgt.setEntity(convertType(src.getEntity()));
    tgt.setIdentifier(convertIdentifier(src.getIdentifier()));
    tgt.setEffectiveTime(src.getEffectiveTime());
    tgt.setQuantity(convertSimpleQuantity(src.getQuantity()));
    tgt.setUnitPrice(convertMoney(src.getUnitPrice()));
    tgt.setFactor(src.getFactor());
    tgt.setPoints(src.getPoints());
    tgt.setNet(convertMoney(src.getNet()));
    return tgt;
  }

  public org.hl7.fhir.instance.model.Contract.TermValuedItemComponent convertTermValuedItemComponent(org.hl7.fhir.dstu3.model.Contract.TermValuedItemComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.Contract.TermValuedItemComponent tgt = new org.hl7.fhir.instance.model.Contract.TermValuedItemComponent();
    copyElement(src, tgt);
    tgt.setEntity(convertType(src.getEntity()));
    tgt.setIdentifier(convertIdentifier(src.getIdentifier()));
    tgt.setEffectiveTime(src.getEffectiveTime());
    tgt.setQuantity(convertSimpleQuantity(src.getQuantity()));
    tgt.setUnitPrice(convertMoney(src.getUnitPrice()));
    tgt.setFactor(src.getFactor());
    tgt.setPoints(src.getPoints());
    tgt.setNet(convertMoney(src.getNet()));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.Contract.FriendlyLanguageComponent convertFriendlyLanguageComponent(org.hl7.fhir.instance.model.Contract.FriendlyLanguageComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.Contract.FriendlyLanguageComponent tgt = new org.hl7.fhir.dstu3.model.Contract.FriendlyLanguageComponent();
    copyElement(src, tgt);
    tgt.setContent(convertType(src.getContent()));
    return tgt;
  }

  public org.hl7.fhir.instance.model.Contract.FriendlyLanguageComponent convertFriendlyLanguageComponent(org.hl7.fhir.dstu3.model.Contract.FriendlyLanguageComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.Contract.FriendlyLanguageComponent tgt = new org.hl7.fhir.instance.model.Contract.FriendlyLanguageComponent();
    copyElement(src, tgt);
    tgt.setContent(convertType(src.getContent()));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.Contract.LegalLanguageComponent convertLegalLanguageComponent(org.hl7.fhir.instance.model.Contract.LegalLanguageComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.Contract.LegalLanguageComponent tgt = new org.hl7.fhir.dstu3.model.Contract.LegalLanguageComponent();
    copyElement(src, tgt);
    tgt.setContent(convertType(src.getContent()));
    return tgt;
  }

  public org.hl7.fhir.instance.model.Contract.LegalLanguageComponent convertLegalLanguageComponent(org.hl7.fhir.dstu3.model.Contract.LegalLanguageComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.Contract.LegalLanguageComponent tgt = new org.hl7.fhir.instance.model.Contract.LegalLanguageComponent();
    copyElement(src, tgt);
    tgt.setContent(convertType(src.getContent()));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.Contract.ComputableLanguageComponent convertComputableLanguageComponent(org.hl7.fhir.instance.model.Contract.ComputableLanguageComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.Contract.ComputableLanguageComponent tgt = new org.hl7.fhir.dstu3.model.Contract.ComputableLanguageComponent();
    copyElement(src, tgt);
    tgt.setContent(convertType(src.getContent()));
    return tgt;
  }

  public org.hl7.fhir.instance.model.Contract.ComputableLanguageComponent convertComputableLanguageComponent(org.hl7.fhir.dstu3.model.Contract.ComputableLanguageComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.Contract.ComputableLanguageComponent tgt = new org.hl7.fhir.instance.model.Contract.ComputableLanguageComponent();
    copyElement(src, tgt);
    tgt.setContent(convertType(src.getContent()));
    return tgt;
  }


  public org.hl7.fhir.dstu3.model.DataElement convertDataElement(org.hl7.fhir.instance.model.DataElement src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.DataElement tgt = new org.hl7.fhir.dstu3.model.DataElement();
    copyDomainResource(src, tgt);
    tgt.setUrl(src.getUrl());
    for (org.hl7.fhir.instance.model.Identifier t : src.getIdentifier())
      tgt.addIdentifier(convertIdentifier(t));
    tgt.setVersion(src.getVersion());
    tgt.setStatus(convertConformanceResourceStatus(src.getStatus()));
    if (src.hasExperimental())
      tgt.setExperimental(src.getExperimental());
    tgt.setPublisher(src.getPublisher());
    if (src.hasDate())
      tgt.setDate(src.getDate());
    tgt.setName(src.getName());
    for (org.hl7.fhir.instance.model.DataElement.DataElementContactComponent t : src.getContact())
      tgt.addContact(convertDataElementContactComponent(t));
    for (org.hl7.fhir.instance.model.CodeableConcept t : src.getUseContext())
      if (isJurisdiction(t))
        tgt.addJurisdiction(convertCodeableConcept(t));
      else
        tgt.addUseContext(convertCodeableConceptToUsageContext(t));
    tgt.setCopyright(src.getCopyright());
    tgt.setStringency(convertDataElementStringency(src.getStringency()));
    for (org.hl7.fhir.instance.model.DataElement.DataElementMappingComponent t : src.getMapping())
      tgt.addMapping(convertDataElementMappingComponent(t));
    List<String> slicePaths = new ArrayList<String>();
    for (org.hl7.fhir.instance.model.ElementDefinition t : src.getElement()) {
      if (t.hasSlicing())
        slicePaths.add(t.getPath());
      tgt.addElement(convertElementDefinition(t, slicePaths));
    }
    return tgt;
  }

  public org.hl7.fhir.instance.model.DataElement convertDataElement(org.hl7.fhir.dstu3.model.DataElement src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.DataElement tgt = new org.hl7.fhir.instance.model.DataElement();
    copyDomainResource(src, tgt);
    tgt.setUrl(src.getUrl());
    for (org.hl7.fhir.dstu3.model.Identifier t : src.getIdentifier())
      tgt.addIdentifier(convertIdentifier(t));
    tgt.setVersion(src.getVersion());
    tgt.setStatus(convertConformanceResourceStatus(src.getStatus()));
    if (src.hasExperimental())
      tgt.setExperimental(src.getExperimental());
    tgt.setPublisher(src.getPublisher());
    if (src.hasDate())
      tgt.setDate(src.getDate());
    tgt.setName(src.getName());
    for (org.hl7.fhir.dstu3.model.ContactDetail t : src.getContact())
      tgt.addContact(convertDataElementContactComponent(t));
    for (org.hl7.fhir.dstu3.model.UsageContext t : src.getUseContext())
      if (t.hasValueCodeableConcept())
        tgt.addUseContext(convertCodeableConcept(t.getValueCodeableConcept()));
    for (org.hl7.fhir.dstu3.model.CodeableConcept t : src.getJurisdiction())
        tgt.addUseContext(convertCodeableConcept(t));
    tgt.setCopyright(src.getCopyright());
    tgt.setStringency(convertDataElementStringency(src.getStringency()));
    for (org.hl7.fhir.dstu3.model.DataElement.DataElementMappingComponent t : src.getMapping())
      tgt.addMapping(convertDataElementMappingComponent(t));
    for (org.hl7.fhir.dstu3.model.ElementDefinition t : src.getElement())
      tgt.addElement(convertElementDefinition(t));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.DataElement.DataElementStringency convertDataElementStringency(org.hl7.fhir.instance.model.DataElement.DataElementStringency src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case COMPARABLE: return org.hl7.fhir.dstu3.model.DataElement.DataElementStringency.COMPARABLE;
    case FULLYSPECIFIED: return org.hl7.fhir.dstu3.model.DataElement.DataElementStringency.FULLYSPECIFIED;
    case EQUIVALENT: return org.hl7.fhir.dstu3.model.DataElement.DataElementStringency.EQUIVALENT;
    case CONVERTABLE: return org.hl7.fhir.dstu3.model.DataElement.DataElementStringency.CONVERTABLE;
    case SCALEABLE: return org.hl7.fhir.dstu3.model.DataElement.DataElementStringency.SCALEABLE;
    case FLEXIBLE: return org.hl7.fhir.dstu3.model.DataElement.DataElementStringency.FLEXIBLE;
    default: return org.hl7.fhir.dstu3.model.DataElement.DataElementStringency.NULL;
    }
  }

  public org.hl7.fhir.instance.model.DataElement.DataElementStringency convertDataElementStringency(org.hl7.fhir.dstu3.model.DataElement.DataElementStringency src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case COMPARABLE: return org.hl7.fhir.instance.model.DataElement.DataElementStringency.COMPARABLE;
    case FULLYSPECIFIED: return org.hl7.fhir.instance.model.DataElement.DataElementStringency.FULLYSPECIFIED;
    case EQUIVALENT: return org.hl7.fhir.instance.model.DataElement.DataElementStringency.EQUIVALENT;
    case CONVERTABLE: return org.hl7.fhir.instance.model.DataElement.DataElementStringency.CONVERTABLE;
    case SCALEABLE: return org.hl7.fhir.instance.model.DataElement.DataElementStringency.SCALEABLE;
    case FLEXIBLE: return org.hl7.fhir.instance.model.DataElement.DataElementStringency.FLEXIBLE;
    default: return org.hl7.fhir.instance.model.DataElement.DataElementStringency.NULL;
    }
  }

  public org.hl7.fhir.dstu3.model.ContactDetail convertDataElementContactComponent(org.hl7.fhir.instance.model.DataElement.DataElementContactComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.ContactDetail tgt = new org.hl7.fhir.dstu3.model.ContactDetail();
    copyElement(src, tgt);
    tgt.setName(src.getName());
    for (org.hl7.fhir.instance.model.ContactPoint t : src.getTelecom())
      tgt.addTelecom(convertContactPoint(t));
    return tgt;
  }

  public org.hl7.fhir.instance.model.DataElement.DataElementContactComponent convertDataElementContactComponent(org.hl7.fhir.dstu3.model.ContactDetail src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.DataElement.DataElementContactComponent tgt = new org.hl7.fhir.instance.model.DataElement.DataElementContactComponent();
    copyElement(src, tgt);
    tgt.setName(src.getName());
    for (org.hl7.fhir.dstu3.model.ContactPoint t : src.getTelecom())
      tgt.addTelecom(convertContactPoint(t));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.DataElement.DataElementMappingComponent convertDataElementMappingComponent(org.hl7.fhir.instance.model.DataElement.DataElementMappingComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.DataElement.DataElementMappingComponent tgt = new org.hl7.fhir.dstu3.model.DataElement.DataElementMappingComponent();
    copyElement(src, tgt);
    tgt.setIdentity(src.getIdentity());
    tgt.setUri(src.getUri());
    tgt.setName(src.getName());
    tgt.setComment(src.getComments());
    return tgt;
  }

  public org.hl7.fhir.instance.model.DataElement.DataElementMappingComponent convertDataElementMappingComponent(org.hl7.fhir.dstu3.model.DataElement.DataElementMappingComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.DataElement.DataElementMappingComponent tgt = new org.hl7.fhir.instance.model.DataElement.DataElementMappingComponent();
    copyElement(src, tgt);
    tgt.setIdentity(src.getIdentity());
    tgt.setUri(src.getUri());
    tgt.setName(src.getName());
    tgt.setComments(src.getComment());
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.DetectedIssue convertDetectedIssue(org.hl7.fhir.instance.model.DetectedIssue src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.DetectedIssue tgt = new org.hl7.fhir.dstu3.model.DetectedIssue();
    copyDomainResource(src, tgt);
    tgt.setPatient(convertReference(src.getPatient()));
    tgt.setCategory(convertCodeableConcept(src.getCategory()));
    tgt.setSeverity(convertDetectedIssueSeverity(src.getSeverity()));
    for (org.hl7.fhir.instance.model.Reference t : src.getImplicated())
      tgt.addImplicated(convertReference(t));
    tgt.setDetail(src.getDetail());
    if (src.hasDate())
      tgt.setDate(src.getDate());
    tgt.setAuthor(convertReference(src.getAuthor()));
    tgt.setIdentifier(convertIdentifier(src.getIdentifier()));
    tgt.setReference(src.getReference());
    for (org.hl7.fhir.instance.model.DetectedIssue.DetectedIssueMitigationComponent t : src.getMitigation())
      tgt.addMitigation(convertDetectedIssueMitigationComponent(t));
    return tgt;
  }

  public org.hl7.fhir.instance.model.DetectedIssue convertDetectedIssue(org.hl7.fhir.dstu3.model.DetectedIssue src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.DetectedIssue tgt = new org.hl7.fhir.instance.model.DetectedIssue();
    copyDomainResource(src, tgt);
    tgt.setPatient(convertReference(src.getPatient()));
    tgt.setCategory(convertCodeableConcept(src.getCategory()));
    tgt.setSeverity(convertDetectedIssueSeverity(src.getSeverity()));
    for (org.hl7.fhir.dstu3.model.Reference t : src.getImplicated())
      tgt.addImplicated(convertReference(t));
    tgt.setDetail(src.getDetail());
    if (src.hasDate())
      tgt.setDate(src.getDate());
    tgt.setAuthor(convertReference(src.getAuthor()));
    tgt.setIdentifier(convertIdentifier(src.getIdentifier()));
    tgt.setReference(src.getReference());
    for (org.hl7.fhir.dstu3.model.DetectedIssue.DetectedIssueMitigationComponent t : src.getMitigation())
      tgt.addMitigation(convertDetectedIssueMitigationComponent(t));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.DetectedIssue.DetectedIssueSeverity convertDetectedIssueSeverity(org.hl7.fhir.instance.model.DetectedIssue.DetectedIssueSeverity src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case HIGH: return org.hl7.fhir.dstu3.model.DetectedIssue.DetectedIssueSeverity.HIGH;
    case MODERATE: return org.hl7.fhir.dstu3.model.DetectedIssue.DetectedIssueSeverity.MODERATE;
    case LOW: return org.hl7.fhir.dstu3.model.DetectedIssue.DetectedIssueSeverity.LOW;
    default: return org.hl7.fhir.dstu3.model.DetectedIssue.DetectedIssueSeverity.NULL;
    }
  }

  public org.hl7.fhir.instance.model.DetectedIssue.DetectedIssueSeverity convertDetectedIssueSeverity(org.hl7.fhir.dstu3.model.DetectedIssue.DetectedIssueSeverity src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case HIGH: return org.hl7.fhir.instance.model.DetectedIssue.DetectedIssueSeverity.HIGH;
    case MODERATE: return org.hl7.fhir.instance.model.DetectedIssue.DetectedIssueSeverity.MODERATE;
    case LOW: return org.hl7.fhir.instance.model.DetectedIssue.DetectedIssueSeverity.LOW;
    default: return org.hl7.fhir.instance.model.DetectedIssue.DetectedIssueSeverity.NULL;
    }
  }

  public org.hl7.fhir.dstu3.model.DetectedIssue.DetectedIssueMitigationComponent convertDetectedIssueMitigationComponent(org.hl7.fhir.instance.model.DetectedIssue.DetectedIssueMitigationComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.DetectedIssue.DetectedIssueMitigationComponent tgt = new org.hl7.fhir.dstu3.model.DetectedIssue.DetectedIssueMitigationComponent();
    copyElement(src, tgt);
    tgt.setAction(convertCodeableConcept(src.getAction()));
    if (src.hasDate())
      tgt.setDate(src.getDate());
    tgt.setAuthor(convertReference(src.getAuthor()));
    return tgt;
  }

  public org.hl7.fhir.instance.model.DetectedIssue.DetectedIssueMitigationComponent convertDetectedIssueMitigationComponent(org.hl7.fhir.dstu3.model.DetectedIssue.DetectedIssueMitigationComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.DetectedIssue.DetectedIssueMitigationComponent tgt = new org.hl7.fhir.instance.model.DetectedIssue.DetectedIssueMitigationComponent();
    copyElement(src, tgt);
    tgt.setAction(convertCodeableConcept(src.getAction()));
    if (src.hasDate())
      tgt.setDate(src.getDate());
    tgt.setAuthor(convertReference(src.getAuthor()));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.Device convertDevice(org.hl7.fhir.instance.model.Device src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.Device tgt = new org.hl7.fhir.dstu3.model.Device();
    copyDomainResource(src, tgt);
    for (org.hl7.fhir.instance.model.Identifier t : src.getIdentifier())
      tgt.addIdentifier(convertIdentifier(t));
    tgt.setUdi((new org.hl7.fhir.dstu3.model.Device.DeviceUdiComponent()).setDeviceIdentifier(src.getUdi()));
    tgt.setStatus(convertDeviceStatus(src.getStatus()));
    tgt.setType(convertCodeableConcept(src.getType()));
    tgt.setLotNumber(src.getLotNumber());
    tgt.setManufacturer(src.getManufacturer());
    tgt.setManufactureDate(src.getManufactureDate());
    tgt.setExpirationDate(src.getExpiry());
    tgt.setModel(src.getModel());
    tgt.setVersion(src.getVersion());
    tgt.setPatient(convertReference(src.getPatient()));
    tgt.setOwner(convertReference(src.getOwner()));
    for (org.hl7.fhir.instance.model.ContactPoint t : src.getContact())
      tgt.addContact(convertContactPoint(t));
    tgt.setLocation(convertReference(src.getLocation()));
    tgt.setUrl(src.getUrl());
    for (org.hl7.fhir.instance.model.Annotation t : src.getNote())
      tgt.addNote(convertAnnotation(t));
    return tgt;
  }

  public org.hl7.fhir.instance.model.Device convertDevice(org.hl7.fhir.dstu3.model.Device src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.Device tgt = new org.hl7.fhir.instance.model.Device();
    copyDomainResource(src, tgt);
    for (org.hl7.fhir.dstu3.model.Identifier t : src.getIdentifier())
      tgt.addIdentifier(convertIdentifier(t));
    if (src.hasUdi())
      tgt.setUdi(src.getUdi().getDeviceIdentifier());
    tgt.setStatus(convertDeviceStatus(src.getStatus()));
    tgt.setType(convertCodeableConcept(src.getType()));
    tgt.setLotNumber(src.getLotNumber());
    tgt.setManufacturer(src.getManufacturer());
    tgt.setManufactureDate(src.getManufactureDate());
    tgt.setExpiry(src.getExpirationDate());
    tgt.setModel(src.getModel());
    tgt.setVersion(src.getVersion());
    tgt.setPatient(convertReference(src.getPatient()));
    tgt.setOwner(convertReference(src.getOwner()));
    for (org.hl7.fhir.dstu3.model.ContactPoint t : src.getContact())
      tgt.addContact(convertContactPoint(t));
    tgt.setLocation(convertReference(src.getLocation()));
    tgt.setUrl(src.getUrl());
    for (org.hl7.fhir.dstu3.model.Annotation t : src.getNote())
      tgt.addNote(convertAnnotation(t));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.Device.FHIRDeviceStatus convertDeviceStatus(org.hl7.fhir.instance.model.Device.DeviceStatus src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case AVAILABLE: return org.hl7.fhir.dstu3.model.Device.FHIRDeviceStatus.ACTIVE;
    case NOTAVAILABLE: return org.hl7.fhir.dstu3.model.Device.FHIRDeviceStatus.INACTIVE;
    case ENTEREDINERROR: return org.hl7.fhir.dstu3.model.Device.FHIRDeviceStatus.ENTEREDINERROR;
    default: return org.hl7.fhir.dstu3.model.Device.FHIRDeviceStatus.NULL;
    }
  }

  public org.hl7.fhir.instance.model.Device.DeviceStatus convertDeviceStatus(org.hl7.fhir.dstu3.model.Device.FHIRDeviceStatus src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case ACTIVE: return org.hl7.fhir.instance.model.Device.DeviceStatus.AVAILABLE;
    case INACTIVE: return org.hl7.fhir.instance.model.Device.DeviceStatus.NOTAVAILABLE;
    case ENTEREDINERROR: return org.hl7.fhir.instance.model.Device.DeviceStatus.ENTEREDINERROR;
    default: return org.hl7.fhir.instance.model.Device.DeviceStatus.NULL;
    }
  }

  public org.hl7.fhir.dstu3.model.DeviceComponent convertDeviceComponent(org.hl7.fhir.instance.model.DeviceComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.DeviceComponent tgt = new org.hl7.fhir.dstu3.model.DeviceComponent();
    copyDomainResource(src, tgt);
    tgt.setType(convertCodeableConcept(src.getType()));
    tgt.setIdentifier(convertIdentifier(src.getIdentifier()));
    tgt.setLastSystemChange(src.getLastSystemChange());
    tgt.setSource(convertReference(src.getSource()));
    tgt.setParent(convertReference(src.getParent()));
    for (org.hl7.fhir.instance.model.CodeableConcept t : src.getOperationalStatus())
      tgt.addOperationalStatus(convertCodeableConcept(t));
    tgt.setParameterGroup(convertCodeableConcept(src.getParameterGroup()));
    tgt.setMeasurementPrinciple(convertMeasmntPrinciple(src.getMeasurementPrinciple()));
    for (org.hl7.fhir.instance.model.DeviceComponent.DeviceComponentProductionSpecificationComponent t : src.getProductionSpecification())
      tgt.addProductionSpecification(convertDeviceComponentProductionSpecificationComponent(t));
    tgt.setLanguageCode(convertCodeableConcept(src.getLanguageCode()));
    return tgt;
  }

  public org.hl7.fhir.instance.model.DeviceComponent convertDeviceComponent(org.hl7.fhir.dstu3.model.DeviceComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.DeviceComponent tgt = new org.hl7.fhir.instance.model.DeviceComponent();
    copyDomainResource(src, tgt);
    tgt.setType(convertCodeableConcept(src.getType()));
    tgt.setIdentifier(convertIdentifier(src.getIdentifier()));
    tgt.setLastSystemChange(src.getLastSystemChange());
    tgt.setSource(convertReference(src.getSource()));
    tgt.setParent(convertReference(src.getParent()));
    for (org.hl7.fhir.dstu3.model.CodeableConcept t : src.getOperationalStatus())
      tgt.addOperationalStatus(convertCodeableConcept(t));
    tgt.setParameterGroup(convertCodeableConcept(src.getParameterGroup()));
    tgt.setMeasurementPrinciple(convertMeasmntPrinciple(src.getMeasurementPrinciple()));
    for (org.hl7.fhir.dstu3.model.DeviceComponent.DeviceComponentProductionSpecificationComponent t : src.getProductionSpecification())
      tgt.addProductionSpecification(convertDeviceComponentProductionSpecificationComponent(t));
    tgt.setLanguageCode(convertCodeableConcept(src.getLanguageCode()));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.DeviceComponent.MeasmntPrinciple convertMeasmntPrinciple(org.hl7.fhir.instance.model.DeviceComponent.MeasmntPrinciple src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case OTHER: return org.hl7.fhir.dstu3.model.DeviceComponent.MeasmntPrinciple.OTHER; 
    case CHEMICAL: return org.hl7.fhir.dstu3.model.DeviceComponent.MeasmntPrinciple.CHEMICAL;
    case ELECTRICAL: return org.hl7.fhir.dstu3.model.DeviceComponent.MeasmntPrinciple.ELECTRICAL;
    case IMPEDANCE: return org.hl7.fhir.dstu3.model.DeviceComponent.MeasmntPrinciple.IMPEDANCE;
    case NUCLEAR: return org.hl7.fhir.dstu3.model.DeviceComponent.MeasmntPrinciple.NUCLEAR;
    case OPTICAL: return org.hl7.fhir.dstu3.model.DeviceComponent.MeasmntPrinciple.OPTICAL;
    case THERMAL: return org.hl7.fhir.dstu3.model.DeviceComponent.MeasmntPrinciple.THERMAL;
    case BIOLOGICAL: return org.hl7.fhir.dstu3.model.DeviceComponent.MeasmntPrinciple.BIOLOGICAL;
    case MECHANICAL: return org.hl7.fhir.dstu3.model.DeviceComponent.MeasmntPrinciple.MECHANICAL;
    case ACOUSTICAL: return org.hl7.fhir.dstu3.model.DeviceComponent.MeasmntPrinciple.ACOUSTICAL;
    case MANUAL: return org.hl7.fhir.dstu3.model.DeviceComponent.MeasmntPrinciple.MANUAL;
    default: return org.hl7.fhir.dstu3.model.DeviceComponent.MeasmntPrinciple.NULL;
    }
  }

  public org.hl7.fhir.instance.model.DeviceComponent.MeasmntPrinciple convertMeasmntPrinciple(org.hl7.fhir.dstu3.model.DeviceComponent.MeasmntPrinciple src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case OTHER: return org.hl7.fhir.instance.model.DeviceComponent.MeasmntPrinciple.OTHER;
    case CHEMICAL: return org.hl7.fhir.instance.model.DeviceComponent.MeasmntPrinciple.CHEMICAL;
    case ELECTRICAL: return org.hl7.fhir.instance.model.DeviceComponent.MeasmntPrinciple.ELECTRICAL;
    case IMPEDANCE: return org.hl7.fhir.instance.model.DeviceComponent.MeasmntPrinciple.IMPEDANCE;
    case NUCLEAR: return org.hl7.fhir.instance.model.DeviceComponent.MeasmntPrinciple.NUCLEAR;
    case OPTICAL: return org.hl7.fhir.instance.model.DeviceComponent.MeasmntPrinciple.OPTICAL;
    case THERMAL: return org.hl7.fhir.instance.model.DeviceComponent.MeasmntPrinciple.THERMAL;
    case BIOLOGICAL: return org.hl7.fhir.instance.model.DeviceComponent.MeasmntPrinciple.BIOLOGICAL;
    case MECHANICAL: return org.hl7.fhir.instance.model.DeviceComponent.MeasmntPrinciple.MECHANICAL;
    case ACOUSTICAL: return org.hl7.fhir.instance.model.DeviceComponent.MeasmntPrinciple.ACOUSTICAL;
    case MANUAL: return org.hl7.fhir.instance.model.DeviceComponent.MeasmntPrinciple.MANUAL;
    default: return org.hl7.fhir.instance.model.DeviceComponent.MeasmntPrinciple.NULL;
    }
  }

  public org.hl7.fhir.dstu3.model.DeviceComponent.DeviceComponentProductionSpecificationComponent convertDeviceComponentProductionSpecificationComponent(org.hl7.fhir.instance.model.DeviceComponent.DeviceComponentProductionSpecificationComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.DeviceComponent.DeviceComponentProductionSpecificationComponent tgt = new org.hl7.fhir.dstu3.model.DeviceComponent.DeviceComponentProductionSpecificationComponent();
    copyElement(src, tgt);
    tgt.setSpecType(convertCodeableConcept(src.getSpecType()));
    tgt.setComponentId(convertIdentifier(src.getComponentId()));
    tgt.setProductionSpec(src.getProductionSpec());
    return tgt;
  }

  public org.hl7.fhir.instance.model.DeviceComponent.DeviceComponentProductionSpecificationComponent convertDeviceComponentProductionSpecificationComponent(org.hl7.fhir.dstu3.model.DeviceComponent.DeviceComponentProductionSpecificationComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.DeviceComponent.DeviceComponentProductionSpecificationComponent tgt = new org.hl7.fhir.instance.model.DeviceComponent.DeviceComponentProductionSpecificationComponent();
    copyElement(src, tgt);
    tgt.setSpecType(convertCodeableConcept(src.getSpecType()));
    tgt.setComponentId(convertIdentifier(src.getComponentId()));
    tgt.setProductionSpec(src.getProductionSpec());
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.DeviceMetric convertDeviceMetric(org.hl7.fhir.instance.model.DeviceMetric src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.DeviceMetric tgt = new org.hl7.fhir.dstu3.model.DeviceMetric();
    copyDomainResource(src, tgt);
    tgt.setType(convertCodeableConcept(src.getType()));
    tgt.setIdentifier(convertIdentifier(src.getIdentifier()));
    tgt.setUnit(convertCodeableConcept(src.getUnit()));
    tgt.setSource(convertReference(src.getSource()));
    tgt.setParent(convertReference(src.getParent()));
    tgt.setOperationalStatus(convertDeviceMetricOperationalStatus(src.getOperationalStatus()));
    tgt.setColor(convertDeviceMetricColor(src.getColor()));
    tgt.setCategory(convertDeviceMetricCategory(src.getCategory()));
    tgt.setMeasurementPeriod(convertTiming(src.getMeasurementPeriod()));
    for (org.hl7.fhir.instance.model.DeviceMetric.DeviceMetricCalibrationComponent t : src.getCalibration())
      tgt.addCalibration(convertDeviceMetricCalibrationComponent(t));
    return tgt;
  }

  public org.hl7.fhir.instance.model.DeviceMetric convertDeviceMetric(org.hl7.fhir.dstu3.model.DeviceMetric src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.DeviceMetric tgt = new org.hl7.fhir.instance.model.DeviceMetric();
    copyDomainResource(src, tgt);
    tgt.setType(convertCodeableConcept(src.getType()));
    tgt.setIdentifier(convertIdentifier(src.getIdentifier()));
    tgt.setUnit(convertCodeableConcept(src.getUnit()));
    tgt.setSource(convertReference(src.getSource()));
    tgt.setParent(convertReference(src.getParent()));
    tgt.setOperationalStatus(convertDeviceMetricOperationalStatus(src.getOperationalStatus()));
    tgt.setColor(convertDeviceMetricColor(src.getColor()));
    tgt.setCategory(convertDeviceMetricCategory(src.getCategory()));
    tgt.setMeasurementPeriod(convertTiming(src.getMeasurementPeriod()));
    for (org.hl7.fhir.dstu3.model.DeviceMetric.DeviceMetricCalibrationComponent t : src.getCalibration())
      tgt.addCalibration(convertDeviceMetricCalibrationComponent(t));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.DeviceMetric.DeviceMetricOperationalStatus convertDeviceMetricOperationalStatus(org.hl7.fhir.instance.model.DeviceMetric.DeviceMetricOperationalStatus src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case ON: return org.hl7.fhir.dstu3.model.DeviceMetric.DeviceMetricOperationalStatus.ON;
    case OFF: return org.hl7.fhir.dstu3.model.DeviceMetric.DeviceMetricOperationalStatus.OFF;
    case STANDBY: return org.hl7.fhir.dstu3.model.DeviceMetric.DeviceMetricOperationalStatus.STANDBY;
    default: return org.hl7.fhir.dstu3.model.DeviceMetric.DeviceMetricOperationalStatus.NULL;
    }
  }

  public org.hl7.fhir.instance.model.DeviceMetric.DeviceMetricOperationalStatus convertDeviceMetricOperationalStatus(org.hl7.fhir.dstu3.model.DeviceMetric.DeviceMetricOperationalStatus src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case ON: return org.hl7.fhir.instance.model.DeviceMetric.DeviceMetricOperationalStatus.ON;
    case OFF: return org.hl7.fhir.instance.model.DeviceMetric.DeviceMetricOperationalStatus.OFF;
    case STANDBY: return org.hl7.fhir.instance.model.DeviceMetric.DeviceMetricOperationalStatus.STANDBY;
    default: return org.hl7.fhir.instance.model.DeviceMetric.DeviceMetricOperationalStatus.NULL;
    }
  }

  public org.hl7.fhir.dstu3.model.DeviceMetric.DeviceMetricColor convertDeviceMetricColor(org.hl7.fhir.instance.model.DeviceMetric.DeviceMetricColor src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case BLACK: return org.hl7.fhir.dstu3.model.DeviceMetric.DeviceMetricColor.BLACK;
    case RED: return org.hl7.fhir.dstu3.model.DeviceMetric.DeviceMetricColor.RED;
    case GREEN: return org.hl7.fhir.dstu3.model.DeviceMetric.DeviceMetricColor.GREEN;
    case YELLOW: return org.hl7.fhir.dstu3.model.DeviceMetric.DeviceMetricColor.YELLOW;
    case BLUE: return org.hl7.fhir.dstu3.model.DeviceMetric.DeviceMetricColor.BLUE;
    case MAGENTA: return org.hl7.fhir.dstu3.model.DeviceMetric.DeviceMetricColor.MAGENTA;
    case CYAN: return org.hl7.fhir.dstu3.model.DeviceMetric.DeviceMetricColor.CYAN;
    case WHITE: return org.hl7.fhir.dstu3.model.DeviceMetric.DeviceMetricColor.WHITE;
    default: return org.hl7.fhir.dstu3.model.DeviceMetric.DeviceMetricColor.NULL;
    }
  }

  public org.hl7.fhir.instance.model.DeviceMetric.DeviceMetricColor convertDeviceMetricColor(org.hl7.fhir.dstu3.model.DeviceMetric.DeviceMetricColor src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case BLACK: return org.hl7.fhir.instance.model.DeviceMetric.DeviceMetricColor.BLACK;
    case RED: return org.hl7.fhir.instance.model.DeviceMetric.DeviceMetricColor.RED;
    case GREEN: return org.hl7.fhir.instance.model.DeviceMetric.DeviceMetricColor.GREEN;
    case YELLOW: return org.hl7.fhir.instance.model.DeviceMetric.DeviceMetricColor.YELLOW;
    case BLUE: return org.hl7.fhir.instance.model.DeviceMetric.DeviceMetricColor.BLUE;
    case MAGENTA: return org.hl7.fhir.instance.model.DeviceMetric.DeviceMetricColor.MAGENTA;
    case CYAN: return org.hl7.fhir.instance.model.DeviceMetric.DeviceMetricColor.CYAN;
    case WHITE: return org.hl7.fhir.instance.model.DeviceMetric.DeviceMetricColor.WHITE;
    default: return org.hl7.fhir.instance.model.DeviceMetric.DeviceMetricColor.NULL;
    }
  }

  public org.hl7.fhir.dstu3.model.DeviceMetric.DeviceMetricCategory convertDeviceMetricCategory(org.hl7.fhir.instance.model.DeviceMetric.DeviceMetricCategory src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case MEASUREMENT: return org.hl7.fhir.dstu3.model.DeviceMetric.DeviceMetricCategory.MEASUREMENT;
    case SETTING: return org.hl7.fhir.dstu3.model.DeviceMetric.DeviceMetricCategory.SETTING;
    case CALCULATION: return org.hl7.fhir.dstu3.model.DeviceMetric.DeviceMetricCategory.CALCULATION;
    case UNSPECIFIED: return org.hl7.fhir.dstu3.model.DeviceMetric.DeviceMetricCategory.UNSPECIFIED;
    default: return org.hl7.fhir.dstu3.model.DeviceMetric.DeviceMetricCategory.NULL;
    }
  }

  public org.hl7.fhir.instance.model.DeviceMetric.DeviceMetricCategory convertDeviceMetricCategory(org.hl7.fhir.dstu3.model.DeviceMetric.DeviceMetricCategory src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case MEASUREMENT: return org.hl7.fhir.instance.model.DeviceMetric.DeviceMetricCategory.MEASUREMENT;
    case SETTING: return org.hl7.fhir.instance.model.DeviceMetric.DeviceMetricCategory.SETTING;
    case CALCULATION: return org.hl7.fhir.instance.model.DeviceMetric.DeviceMetricCategory.CALCULATION;
    case UNSPECIFIED: return org.hl7.fhir.instance.model.DeviceMetric.DeviceMetricCategory.UNSPECIFIED;
    default: return org.hl7.fhir.instance.model.DeviceMetric.DeviceMetricCategory.NULL;
    }
  }

  public org.hl7.fhir.dstu3.model.DeviceMetric.DeviceMetricCalibrationComponent convertDeviceMetricCalibrationComponent(org.hl7.fhir.instance.model.DeviceMetric.DeviceMetricCalibrationComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.DeviceMetric.DeviceMetricCalibrationComponent tgt = new org.hl7.fhir.dstu3.model.DeviceMetric.DeviceMetricCalibrationComponent();
    copyElement(src, tgt);
    tgt.setType(convertDeviceMetricCalibrationType(src.getType()));
    tgt.setState(convertDeviceMetricCalibrationState(src.getState()));
    tgt.setTime(src.getTime());
    return tgt;
  }

  public org.hl7.fhir.instance.model.DeviceMetric.DeviceMetricCalibrationComponent convertDeviceMetricCalibrationComponent(org.hl7.fhir.dstu3.model.DeviceMetric.DeviceMetricCalibrationComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.DeviceMetric.DeviceMetricCalibrationComponent tgt = new org.hl7.fhir.instance.model.DeviceMetric.DeviceMetricCalibrationComponent();
    copyElement(src, tgt);
    tgt.setType(convertDeviceMetricCalibrationType(src.getType()));
    tgt.setState(convertDeviceMetricCalibrationState(src.getState()));
    tgt.setTime(src.getTime());
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.DeviceMetric.DeviceMetricCalibrationType convertDeviceMetricCalibrationType(org.hl7.fhir.instance.model.DeviceMetric.DeviceMetricCalibrationType src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case UNSPECIFIED: return org.hl7.fhir.dstu3.model.DeviceMetric.DeviceMetricCalibrationType.UNSPECIFIED;
    case OFFSET: return org.hl7.fhir.dstu3.model.DeviceMetric.DeviceMetricCalibrationType.OFFSET;
    case GAIN: return org.hl7.fhir.dstu3.model.DeviceMetric.DeviceMetricCalibrationType.GAIN;
    case TWOPOINT: return org.hl7.fhir.dstu3.model.DeviceMetric.DeviceMetricCalibrationType.TWOPOINT;
    default: return org.hl7.fhir.dstu3.model.DeviceMetric.DeviceMetricCalibrationType.NULL;
    }
  }

  public org.hl7.fhir.instance.model.DeviceMetric.DeviceMetricCalibrationType convertDeviceMetricCalibrationType(org.hl7.fhir.dstu3.model.DeviceMetric.DeviceMetricCalibrationType src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case UNSPECIFIED: return org.hl7.fhir.instance.model.DeviceMetric.DeviceMetricCalibrationType.UNSPECIFIED;
    case OFFSET: return org.hl7.fhir.instance.model.DeviceMetric.DeviceMetricCalibrationType.OFFSET;
    case GAIN: return org.hl7.fhir.instance.model.DeviceMetric.DeviceMetricCalibrationType.GAIN;
    case TWOPOINT: return org.hl7.fhir.instance.model.DeviceMetric.DeviceMetricCalibrationType.TWOPOINT;
    default: return org.hl7.fhir.instance.model.DeviceMetric.DeviceMetricCalibrationType.NULL;
    }
  }

  public org.hl7.fhir.dstu3.model.DeviceMetric.DeviceMetricCalibrationState convertDeviceMetricCalibrationState(org.hl7.fhir.instance.model.DeviceMetric.DeviceMetricCalibrationState src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case NOTCALIBRATED: return org.hl7.fhir.dstu3.model.DeviceMetric.DeviceMetricCalibrationState.NOTCALIBRATED;
    case CALIBRATIONREQUIRED: return org.hl7.fhir.dstu3.model.DeviceMetric.DeviceMetricCalibrationState.CALIBRATIONREQUIRED;
    case CALIBRATED: return org.hl7.fhir.dstu3.model.DeviceMetric.DeviceMetricCalibrationState.CALIBRATED;
    case UNSPECIFIED: return org.hl7.fhir.dstu3.model.DeviceMetric.DeviceMetricCalibrationState.UNSPECIFIED;
    default: return org.hl7.fhir.dstu3.model.DeviceMetric.DeviceMetricCalibrationState.NULL;
    }
  }

  public org.hl7.fhir.instance.model.DeviceMetric.DeviceMetricCalibrationState convertDeviceMetricCalibrationState(org.hl7.fhir.dstu3.model.DeviceMetric.DeviceMetricCalibrationState src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case NOTCALIBRATED: return org.hl7.fhir.instance.model.DeviceMetric.DeviceMetricCalibrationState.NOTCALIBRATED;
    case CALIBRATIONREQUIRED: return org.hl7.fhir.instance.model.DeviceMetric.DeviceMetricCalibrationState.CALIBRATIONREQUIRED;
    case CALIBRATED: return org.hl7.fhir.instance.model.DeviceMetric.DeviceMetricCalibrationState.CALIBRATED;
    case UNSPECIFIED: return org.hl7.fhir.instance.model.DeviceMetric.DeviceMetricCalibrationState.UNSPECIFIED;
    default: return org.hl7.fhir.instance.model.DeviceMetric.DeviceMetricCalibrationState.NULL;
    }
  }

  public org.hl7.fhir.dstu3.model.DeviceUseStatement convertDeviceUseStatement(org.hl7.fhir.instance.model.DeviceUseStatement src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.DeviceUseStatement tgt = new org.hl7.fhir.dstu3.model.DeviceUseStatement();
    copyDomainResource(src, tgt);
    if (src.hasBodySiteCodeableConcept())
      tgt.setBodySite(convertCodeableConcept(src.getBodySiteCodeableConcept()));
    tgt.setWhenUsed(convertPeriod(src.getWhenUsed()));
    tgt.setDevice(convertReference(src.getDevice()));
    for (org.hl7.fhir.instance.model.Identifier t : src.getIdentifier())
      tgt.addIdentifier(convertIdentifier(t));
    for (org.hl7.fhir.instance.model.CodeableConcept t : src.getIndication())
      tgt.addIndication(convertCodeableConcept(t));
    for (org.hl7.fhir.instance.model.StringType t : src.getNotes())
      tgt.addNote().setText(t.getValue());
    tgt.setRecordedOn(src.getRecordedOn());
    tgt.setSubject(convertReference(src.getSubject()));
    tgt.setTiming(convertType(src.getTiming()));
    return tgt;
  }

  public org.hl7.fhir.instance.model.DeviceUseStatement convertDeviceUseStatement(org.hl7.fhir.dstu3.model.DeviceUseStatement src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.DeviceUseStatement tgt = new org.hl7.fhir.instance.model.DeviceUseStatement();
    copyDomainResource(src, tgt);
    tgt.setBodySite(convertType(src.getBodySite()));
    tgt.setWhenUsed(convertPeriod(src.getWhenUsed()));
    tgt.setDevice(convertReference(src.getDevice()));
    for (org.hl7.fhir.dstu3.model.Identifier t : src.getIdentifier())
      tgt.addIdentifier(convertIdentifier(t));
    for (org.hl7.fhir.dstu3.model.CodeableConcept t : src.getIndication())
      tgt.addIndication(convertCodeableConcept(t));
    for (Annotation t : src.getNote())
      tgt.addNotes(t.getText());
    tgt.setRecordedOn(src.getRecordedOn());
    tgt.setSubject(convertReference(src.getSubject()));
    tgt.setTiming(convertType(src.getTiming()));
    return tgt;
  }

//  public org.hl7.fhir.dstu3.model.DiagnosticRequest.DiagnosticRequestStatus convertDiagnosticOrderStatus(org.hl7.fhir.instance.model.DiagnosticOrder.DiagnosticOrderStatus src) throws FHIRException {
//    if (src ==/* null || src.isEmpty()*/)
//      return null;
//    switch (src) {
//    case PROPOSED: return org.hl7.fhir.dstu3.model.DiagnosticRequest.DiagnosticRequestStatus.PROPOSED;
//    case DRAFT: return org.hl7.fhir.dstu3.model.DiagnosticRequest.DiagnosticRequestStatus.DRAFT;
//    case PLANNED: return org.hl7.fhir.dstu3.model.DiagnosticRequest.DiagnosticRequestStatus.PLANNED;
//    case REQUESTED: return org.hl7.fhir.dstu3.model.DiagnosticRequest.DiagnosticRequestStatus.REQUESTED;
//    case RECEIVED: return org.hl7.fhir.dstu3.model.DiagnosticRequest.DiagnosticRequestStatus.RECEIVED;
//    case ACCEPTED: return org.hl7.fhir.dstu3.model.DiagnosticRequest.DiagnosticRequestStatus.ACCEPTED;
//    case INPROGRESS: return org.hl7.fhir.dstu3.model.DiagnosticRequest.DiagnosticRequestStatus.INPROGRESS;
//    case REVIEW: return org.hl7.fhir.dstu3.model.DiagnosticRequest.DiagnosticRequestStatus.REVIEW;
//    case COMPLETED: return org.hl7.fhir.dstu3.model.DiagnosticRequest.DiagnosticRequestStatus.COMPLETED;
//    case CANCELLED: return org.hl7.fhir.dstu3.model.DiagnosticRequest.DiagnosticRequestStatus.CANCELLED;
//    case SUSPENDED: return org.hl7.fhir.dstu3.model.DiagnosticRequest.DiagnosticRequestStatus.SUSPENDED;
//    case REJECTED: return org.hl7.fhir.dstu3.model.DiagnosticRequest.DiagnosticRequestStatus.REJECTED;
//    case FAILED: return org.hl7.fhir.dstu3.model.DiagnosticRequest.DiagnosticRequestStatus.FAILED;
//    default: return org.hl7.fhir.dstu3.model.DiagnosticRequest.DiagnosticRequestStatus.NULL;
//    }
//  }
//
//  public org.hl7.fhir.instance.model.DiagnosticOrder.DiagnosticOrderStatus convertDiagnosticOrderStatus(org.hl7.fhir.dstu3.model.DiagnosticRequest.DiagnosticRequestStatus src) throws FHIRException {
//    if (src ==/* null || src.isEmpty()*/)
//      return null;
//    switch (src) {
//    case PROPOSED: return org.hl7.fhir.instance.model.DiagnosticOrder.DiagnosticOrderStatus.PROPOSED;
//    case DRAFT: return org.hl7.fhir.instance.model.DiagnosticOrder.DiagnosticOrderStatus.DRAFT;
//    case PLANNED: return org.hl7.fhir.instance.model.DiagnosticOrder.DiagnosticOrderStatus.PLANNED;
//    case REQUESTED: return org.hl7.fhir.instance.model.DiagnosticOrder.DiagnosticOrderStatus.REQUESTED;
//    case RECEIVED: return org.hl7.fhir.instance.model.DiagnosticOrder.DiagnosticOrderStatus.RECEIVED;
//    case ACCEPTED: return org.hl7.fhir.instance.model.DiagnosticOrder.DiagnosticOrderStatus.ACCEPTED;
//    case INPROGRESS: return org.hl7.fhir.instance.model.DiagnosticOrder.DiagnosticOrderStatus.INPROGRESS;
//    case REVIEW: return org.hl7.fhir.instance.model.DiagnosticOrder.DiagnosticOrderStatus.REVIEW;
//    case COMPLETED: return org.hl7.fhir.instance.model.DiagnosticOrder.DiagnosticOrderStatus.COMPLETED;
//    case CANCELLED: return org.hl7.fhir.instance.model.DiagnosticOrder.DiagnosticOrderStatus.CANCELLED;
//    case SUSPENDED: return org.hl7.fhir.instance.model.DiagnosticOrder.DiagnosticOrderStatus.SUSPENDED;
//    case REJECTED: return org.hl7.fhir.instance.model.DiagnosticOrder.DiagnosticOrderStatus.REJECTED;
//    case FAILED: return org.hl7.fhir.instance.model.DiagnosticOrder.DiagnosticOrderStatus.FAILED;
//    default: return org.hl7.fhir.instance.model.DiagnosticOrder.DiagnosticOrderStatus.NULL;
//    }
//  }


  public org.hl7.fhir.dstu3.model.DiagnosticReport convertDiagnosticReport(org.hl7.fhir.instance.model.DiagnosticReport src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.DiagnosticReport tgt = new org.hl7.fhir.dstu3.model.DiagnosticReport();
    copyDomainResource(src, tgt);
    for (org.hl7.fhir.instance.model.Identifier t : src.getIdentifier())
      tgt.addIdentifier(convertIdentifier(t));
    tgt.setStatus(convertDiagnosticReportStatus(src.getStatus()));
    tgt.setCategory(convertCodeableConcept(src.getCategory()));
    tgt.setCode(convertCodeableConcept(src.getCode()));
    tgt.setSubject(convertReference(src.getSubject()));
    tgt.setContext(convertReference(src.getEncounter()));
    tgt.setEffective(convertType(src.getEffective()));
    tgt.setIssued(src.getIssued());
//    tgt.setPerformer(convertReference(src.getPerformer()));
//    for (org.hl7.fhir.instance.model.Reference t : src.getRequest())
//      tgt.addRequest(convertReference(t));
    for (org.hl7.fhir.instance.model.Reference t : src.getSpecimen())
      tgt.addSpecimen(convertReference(t));
    for (org.hl7.fhir.instance.model.Reference t : src.getResult())
      tgt.addResult(convertReference(t));
    for (org.hl7.fhir.instance.model.Reference t : src.getImagingStudy())
      tgt.addImagingStudy(convertReference(t));
    for (org.hl7.fhir.instance.model.DiagnosticReport.DiagnosticReportImageComponent t : src.getImage())
      tgt.addImage(convertDiagnosticReportImageComponent(t));
    tgt.setConclusion(src.getConclusion());
    for (org.hl7.fhir.instance.model.CodeableConcept t : src.getCodedDiagnosis())
      tgt.addCodedDiagnosis(convertCodeableConcept(t));
    for (org.hl7.fhir.instance.model.Attachment t : src.getPresentedForm())
      tgt.addPresentedForm(convertAttachment(t));
    return tgt;
  }

  public org.hl7.fhir.instance.model.DiagnosticReport convertDiagnosticReport(org.hl7.fhir.dstu3.model.DiagnosticReport src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.DiagnosticReport tgt = new org.hl7.fhir.instance.model.DiagnosticReport();
    copyDomainResource(src, tgt);
    for (org.hl7.fhir.dstu3.model.Identifier t : src.getIdentifier())
      tgt.addIdentifier(convertIdentifier(t));
    tgt.setStatus(convertDiagnosticReportStatus(src.getStatus()));
    tgt.setCategory(convertCodeableConcept(src.getCategory()));
    tgt.setCode(convertCodeableConcept(src.getCode()));
    tgt.setSubject(convertReference(src.getSubject()));
    tgt.setEncounter(convertReference(src.getContext()));
    tgt.setEffective(convertType(src.getEffective()));
    tgt.setIssued(src.getIssued());
//    tgt.setPerformer(convertReference(src.getPerformer()));
//    for (org.hl7.fhir.dstu3.model.Reference t : src.getRequest())
//      tgt.addRequest(convertReference(t));
    for (org.hl7.fhir.dstu3.model.Reference t : src.getSpecimen())
      tgt.addSpecimen(convertReference(t));
    for (org.hl7.fhir.dstu3.model.Reference t : src.getResult())
      tgt.addResult(convertReference(t));
    for (org.hl7.fhir.dstu3.model.Reference t : src.getImagingStudy())
      tgt.addImagingStudy(convertReference(t));
    for (org.hl7.fhir.dstu3.model.DiagnosticReport.DiagnosticReportImageComponent t : src.getImage())
      tgt.addImage(convertDiagnosticReportImageComponent(t));
    tgt.setConclusion(src.getConclusion());
    for (org.hl7.fhir.dstu3.model.CodeableConcept t : src.getCodedDiagnosis())
      tgt.addCodedDiagnosis(convertCodeableConcept(t));
    for (org.hl7.fhir.dstu3.model.Attachment t : src.getPresentedForm())
      tgt.addPresentedForm(convertAttachment(t));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.DiagnosticReport.DiagnosticReportStatus convertDiagnosticReportStatus(org.hl7.fhir.instance.model.DiagnosticReport.DiagnosticReportStatus src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case REGISTERED: return org.hl7.fhir.dstu3.model.DiagnosticReport.DiagnosticReportStatus.REGISTERED;
    case PARTIAL: return org.hl7.fhir.dstu3.model.DiagnosticReport.DiagnosticReportStatus.PARTIAL;
    case FINAL: return org.hl7.fhir.dstu3.model.DiagnosticReport.DiagnosticReportStatus.FINAL;
    case CORRECTED: return org.hl7.fhir.dstu3.model.DiagnosticReport.DiagnosticReportStatus.CORRECTED;
    case APPENDED: return org.hl7.fhir.dstu3.model.DiagnosticReport.DiagnosticReportStatus.APPENDED;
    case CANCELLED: return org.hl7.fhir.dstu3.model.DiagnosticReport.DiagnosticReportStatus.CANCELLED;
    case ENTEREDINERROR: return org.hl7.fhir.dstu3.model.DiagnosticReport.DiagnosticReportStatus.ENTEREDINERROR;
    default: return org.hl7.fhir.dstu3.model.DiagnosticReport.DiagnosticReportStatus.NULL;
    }
  }

  public org.hl7.fhir.instance.model.DiagnosticReport.DiagnosticReportStatus convertDiagnosticReportStatus(org.hl7.fhir.dstu3.model.DiagnosticReport.DiagnosticReportStatus src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case REGISTERED: return org.hl7.fhir.instance.model.DiagnosticReport.DiagnosticReportStatus.REGISTERED;
    case PARTIAL: return org.hl7.fhir.instance.model.DiagnosticReport.DiagnosticReportStatus.PARTIAL;
    case FINAL: return org.hl7.fhir.instance.model.DiagnosticReport.DiagnosticReportStatus.FINAL;
    case CORRECTED: return org.hl7.fhir.instance.model.DiagnosticReport.DiagnosticReportStatus.CORRECTED;
    case APPENDED: return org.hl7.fhir.instance.model.DiagnosticReport.DiagnosticReportStatus.APPENDED;
    case CANCELLED: return org.hl7.fhir.instance.model.DiagnosticReport.DiagnosticReportStatus.CANCELLED;
    case ENTEREDINERROR: return org.hl7.fhir.instance.model.DiagnosticReport.DiagnosticReportStatus.ENTEREDINERROR;
    default: return org.hl7.fhir.instance.model.DiagnosticReport.DiagnosticReportStatus.NULL;
    }
  }

  public org.hl7.fhir.dstu3.model.DiagnosticReport.DiagnosticReportImageComponent convertDiagnosticReportImageComponent(org.hl7.fhir.instance.model.DiagnosticReport.DiagnosticReportImageComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.DiagnosticReport.DiagnosticReportImageComponent tgt = new org.hl7.fhir.dstu3.model.DiagnosticReport.DiagnosticReportImageComponent();
    copyElement(src, tgt);
    tgt.setComment(src.getComment());
    tgt.setLink(convertReference(src.getLink()));
    return tgt;
  }

  public org.hl7.fhir.instance.model.DiagnosticReport.DiagnosticReportImageComponent convertDiagnosticReportImageComponent(org.hl7.fhir.dstu3.model.DiagnosticReport.DiagnosticReportImageComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.DiagnosticReport.DiagnosticReportImageComponent tgt = new org.hl7.fhir.instance.model.DiagnosticReport.DiagnosticReportImageComponent();
    copyElement(src, tgt);
    tgt.setComment(src.getComment());
    tgt.setLink(convertReference(src.getLink()));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.DocumentManifest convertDocumentManifest(org.hl7.fhir.instance.model.DocumentManifest src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.DocumentManifest tgt = new org.hl7.fhir.dstu3.model.DocumentManifest();
    copyDomainResource(src, tgt);
    tgt.setMasterIdentifier(convertIdentifier(src.getMasterIdentifier()));
    for (org.hl7.fhir.instance.model.Identifier t : src.getIdentifier())
      tgt.addIdentifier(convertIdentifier(t));
    tgt.setSubject(convertReference(src.getSubject()));
    for (org.hl7.fhir.instance.model.Reference t : src.getRecipient())
      tgt.addRecipient(convertReference(t));
    tgt.setType(convertCodeableConcept(src.getType()));
    for (org.hl7.fhir.instance.model.Reference t : src.getAuthor())
      tgt.addAuthor(convertReference(t));
    tgt.setCreated(src.getCreated());
    tgt.setSource(src.getSource());
    tgt.setStatus(convertDocumentReferenceStatus(src.getStatus()));
    tgt.setDescription(src.getDescription());
    for (org.hl7.fhir.instance.model.DocumentManifest.DocumentManifestContentComponent t : src.getContent())
      tgt.addContent(convertDocumentManifestContentComponent(t));
    for (org.hl7.fhir.instance.model.DocumentManifest.DocumentManifestRelatedComponent t : src.getRelated())
      tgt.addRelated(convertDocumentManifestRelatedComponent(t));
    return tgt;
  }

  public org.hl7.fhir.instance.model.DocumentManifest convertDocumentManifest(org.hl7.fhir.dstu3.model.DocumentManifest src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.DocumentManifest tgt = new org.hl7.fhir.instance.model.DocumentManifest();
    copyDomainResource(src, tgt);
    tgt.setMasterIdentifier(convertIdentifier(src.getMasterIdentifier()));
    for (org.hl7.fhir.dstu3.model.Identifier t : src.getIdentifier())
      tgt.addIdentifier(convertIdentifier(t));
    tgt.setSubject(convertReference(src.getSubject()));
    for (org.hl7.fhir.dstu3.model.Reference t : src.getRecipient())
      tgt.addRecipient(convertReference(t));
    tgt.setType(convertCodeableConcept(src.getType()));
    for (org.hl7.fhir.dstu3.model.Reference t : src.getAuthor())
      tgt.addAuthor(convertReference(t));
    tgt.setCreated(src.getCreated());
    tgt.setSource(src.getSource());
    tgt.setStatus(convertDocumentReferenceStatus(src.getStatus()));
    tgt.setDescription(src.getDescription());
    for (org.hl7.fhir.dstu3.model.DocumentManifest.DocumentManifestContentComponent t : src.getContent())
      tgt.addContent(convertDocumentManifestContentComponent(t));
    for (org.hl7.fhir.dstu3.model.DocumentManifest.DocumentManifestRelatedComponent t : src.getRelated())
      tgt.addRelated(convertDocumentManifestRelatedComponent(t));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.Enumerations.DocumentReferenceStatus convertDocumentReferenceStatus(org.hl7.fhir.instance.model.Enumerations.DocumentReferenceStatus src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case CURRENT: return org.hl7.fhir.dstu3.model.Enumerations.DocumentReferenceStatus.CURRENT;
    case SUPERSEDED: return org.hl7.fhir.dstu3.model.Enumerations.DocumentReferenceStatus.SUPERSEDED;
    case ENTEREDINERROR: return org.hl7.fhir.dstu3.model.Enumerations.DocumentReferenceStatus.ENTEREDINERROR;
    default: return org.hl7.fhir.dstu3.model.Enumerations.DocumentReferenceStatus.NULL;
    }
  }

  public org.hl7.fhir.instance.model.Enumerations.DocumentReferenceStatus convertDocumentReferenceStatus(org.hl7.fhir.dstu3.model.Enumerations.DocumentReferenceStatus src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case CURRENT: return org.hl7.fhir.instance.model.Enumerations.DocumentReferenceStatus.CURRENT;
    case SUPERSEDED: return org.hl7.fhir.instance.model.Enumerations.DocumentReferenceStatus.SUPERSEDED;
    case ENTEREDINERROR: return org.hl7.fhir.instance.model.Enumerations.DocumentReferenceStatus.ENTEREDINERROR;
    default: return org.hl7.fhir.instance.model.Enumerations.DocumentReferenceStatus.NULL;
    }
  }

  public org.hl7.fhir.dstu3.model.DocumentManifest.DocumentManifestContentComponent convertDocumentManifestContentComponent(org.hl7.fhir.instance.model.DocumentManifest.DocumentManifestContentComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.DocumentManifest.DocumentManifestContentComponent tgt = new org.hl7.fhir.dstu3.model.DocumentManifest.DocumentManifestContentComponent();
    copyElement(src, tgt);
    tgt.setP(convertType(src.getP()));
    return tgt;
  }

  public org.hl7.fhir.instance.model.DocumentManifest.DocumentManifestContentComponent convertDocumentManifestContentComponent(org.hl7.fhir.dstu3.model.DocumentManifest.DocumentManifestContentComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.DocumentManifest.DocumentManifestContentComponent tgt = new org.hl7.fhir.instance.model.DocumentManifest.DocumentManifestContentComponent();
    copyElement(src, tgt);
    tgt.setP(convertType(src.getP()));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.DocumentManifest.DocumentManifestRelatedComponent convertDocumentManifestRelatedComponent(org.hl7.fhir.instance.model.DocumentManifest.DocumentManifestRelatedComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.DocumentManifest.DocumentManifestRelatedComponent tgt = new org.hl7.fhir.dstu3.model.DocumentManifest.DocumentManifestRelatedComponent();
    copyElement(src, tgt);
    tgt.setIdentifier(convertIdentifier(src.getIdentifier()));
    tgt.setRef(convertReference(src.getRef()));
    return tgt;
  }

  public org.hl7.fhir.instance.model.DocumentManifest.DocumentManifestRelatedComponent convertDocumentManifestRelatedComponent(org.hl7.fhir.dstu3.model.DocumentManifest.DocumentManifestRelatedComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.DocumentManifest.DocumentManifestRelatedComponent tgt = new org.hl7.fhir.instance.model.DocumentManifest.DocumentManifestRelatedComponent();
    copyElement(src, tgt);
    tgt.setIdentifier(convertIdentifier(src.getIdentifier()));
    tgt.setRef(convertReference(src.getRef()));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.DocumentReference convertDocumentReference(org.hl7.fhir.instance.model.DocumentReference src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.DocumentReference tgt = new org.hl7.fhir.dstu3.model.DocumentReference();
    copyDomainResource(src, tgt);
    tgt.setMasterIdentifier(convertIdentifier(src.getMasterIdentifier()));
    for (org.hl7.fhir.instance.model.Identifier t : src.getIdentifier())
      tgt.addIdentifier(convertIdentifier(t));
    tgt.setSubject(convertReference(src.getSubject()));
    tgt.setType(convertCodeableConcept(src.getType()));
    tgt.setClass_(convertCodeableConcept(src.getClass_()));
    for (org.hl7.fhir.instance.model.Reference t : src.getAuthor())
      tgt.addAuthor(convertReference(t));
    tgt.setCustodian(convertReference(src.getCustodian()));
    tgt.setAuthenticator(convertReference(src.getAuthenticator()));
    tgt.setCreated(src.getCreated());
    tgt.setIndexed(src.getIndexed());
    tgt.setStatus(convertDocumentReferenceStatus(src.getStatus()));
    tgt.setDocStatus(convertDocStatus(src.getDocStatus()));
    for (org.hl7.fhir.instance.model.DocumentReference.DocumentReferenceRelatesToComponent t : src.getRelatesTo())
      tgt.addRelatesTo(convertDocumentReferenceRelatesToComponent(t));
    tgt.setDescription(src.getDescription());
    for (org.hl7.fhir.instance.model.CodeableConcept t : src.getSecurityLabel())
      tgt.addSecurityLabel(convertCodeableConcept(t));
    for (org.hl7.fhir.instance.model.DocumentReference.DocumentReferenceContentComponent t : src.getContent())
      tgt.addContent(convertDocumentReferenceContentComponent(t));
    tgt.setContext(convertDocumentReferenceContextComponent(src.getContext()));
    return tgt;
  }

  private ReferredDocumentStatus convertDocStatus(CodeableConcept cc) {
    if (hasConcept(cc, "http://hl7.org/fhir/composition-status", "preliminary"))
      return ReferredDocumentStatus.PRELIMINARY;
    if (hasConcept(cc, "http://hl7.org/fhir/composition-status", "final"))
      return ReferredDocumentStatus.FINAL;
    if (hasConcept(cc, "http://hl7.org/fhir/composition-status", "amended"))
      return ReferredDocumentStatus.AMENDED;
    if (hasConcept(cc, "http://hl7.org/fhir/composition-status", "entered-in-error"))
      return ReferredDocumentStatus.ENTEREDINERROR;

    return null;
  }

  private CodeableConcept convertDocStatus(ReferredDocumentStatus docStatus) {
    CodeableConcept cc = new CodeableConcept ();
    switch (docStatus) {
    case AMENDED: cc.addCoding(). setSystem("http://hl7.org/fhir/composition-status").setCode("amended"); break;
    case ENTEREDINERROR: cc.addCoding(). setSystem("http://hl7.org/fhir/composition-status").setCode("entered-in-error"); break;
    case FINAL: cc.addCoding(). setSystem("http://hl7.org/fhir/composition-status").setCode("final"); break;
    case PRELIMINARY: cc.addCoding(). setSystem("http://hl7.org/fhir/composition-status").setCode("preliminary"); break;
    default: return null;
    }
    return cc;
  }

  public org.hl7.fhir.instance.model.DocumentReference convertDocumentReference(org.hl7.fhir.dstu3.model.DocumentReference src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.DocumentReference tgt = new org.hl7.fhir.instance.model.DocumentReference();
    copyDomainResource(src, tgt);
    tgt.setMasterIdentifier(convertIdentifier(src.getMasterIdentifier()));
    for (org.hl7.fhir.dstu3.model.Identifier t : src.getIdentifier())
      tgt.addIdentifier(convertIdentifier(t));
    tgt.setSubject(convertReference(src.getSubject()));
    tgt.setType(convertCodeableConcept(src.getType()));
    tgt.setClass_(convertCodeableConcept(src.getClass_()));
    for (org.hl7.fhir.dstu3.model.Reference t : src.getAuthor())
      tgt.addAuthor(convertReference(t));
    tgt.setCustodian(convertReference(src.getCustodian()));
    tgt.setAuthenticator(convertReference(src.getAuthenticator()));
    tgt.setCreated(src.getCreated());
    tgt.setIndexed(src.getIndexed());
    tgt.setStatus(convertDocumentReferenceStatus(src.getStatus()));
    tgt.setDocStatus(convertDocStatus(src.getDocStatus()));
    for (org.hl7.fhir.dstu3.model.DocumentReference.DocumentReferenceRelatesToComponent t : src.getRelatesTo())
      tgt.addRelatesTo(convertDocumentReferenceRelatesToComponent(t));
    tgt.setDescription(src.getDescription());
    for (org.hl7.fhir.dstu3.model.CodeableConcept t : src.getSecurityLabel())
      tgt.addSecurityLabel(convertCodeableConcept(t));
    for (org.hl7.fhir.dstu3.model.DocumentReference.DocumentReferenceContentComponent t : src.getContent())
      tgt.addContent(convertDocumentReferenceContentComponent(t));
    tgt.setContext(convertDocumentReferenceContextComponent(src.getContext()));
    return tgt;
  }


  public org.hl7.fhir.dstu3.model.DocumentReference.DocumentReferenceRelatesToComponent convertDocumentReferenceRelatesToComponent(org.hl7.fhir.instance.model.DocumentReference.DocumentReferenceRelatesToComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.DocumentReference.DocumentReferenceRelatesToComponent tgt = new org.hl7.fhir.dstu3.model.DocumentReference.DocumentReferenceRelatesToComponent();
    copyElement(src, tgt);
    tgt.setCode(convertDocumentRelationshipType(src.getCode()));
    tgt.setTarget(convertReference(src.getTarget()));
    return tgt;
  }

  public org.hl7.fhir.instance.model.DocumentReference.DocumentReferenceRelatesToComponent convertDocumentReferenceRelatesToComponent(org.hl7.fhir.dstu3.model.DocumentReference.DocumentReferenceRelatesToComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.DocumentReference.DocumentReferenceRelatesToComponent tgt = new org.hl7.fhir.instance.model.DocumentReference.DocumentReferenceRelatesToComponent();
    copyElement(src, tgt);
    tgt.setCode(convertDocumentRelationshipType(src.getCode()));
    tgt.setTarget(convertReference(src.getTarget()));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.DocumentReference.DocumentRelationshipType convertDocumentRelationshipType(org.hl7.fhir.instance.model.DocumentReference.DocumentRelationshipType src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case REPLACES: return org.hl7.fhir.dstu3.model.DocumentReference.DocumentRelationshipType.REPLACES;
    case TRANSFORMS: return org.hl7.fhir.dstu3.model.DocumentReference.DocumentRelationshipType.TRANSFORMS;
    case SIGNS: return org.hl7.fhir.dstu3.model.DocumentReference.DocumentRelationshipType.SIGNS;
    case APPENDS: return org.hl7.fhir.dstu3.model.DocumentReference.DocumentRelationshipType.APPENDS;
    default: return org.hl7.fhir.dstu3.model.DocumentReference.DocumentRelationshipType.NULL;
    }
  }

  public org.hl7.fhir.instance.model.DocumentReference.DocumentRelationshipType convertDocumentRelationshipType(org.hl7.fhir.dstu3.model.DocumentReference.DocumentRelationshipType src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case REPLACES: return org.hl7.fhir.instance.model.DocumentReference.DocumentRelationshipType.REPLACES;
    case TRANSFORMS: return org.hl7.fhir.instance.model.DocumentReference.DocumentRelationshipType.TRANSFORMS;
    case SIGNS: return org.hl7.fhir.instance.model.DocumentReference.DocumentRelationshipType.SIGNS;
    case APPENDS: return org.hl7.fhir.instance.model.DocumentReference.DocumentRelationshipType.APPENDS;
    default: return org.hl7.fhir.instance.model.DocumentReference.DocumentRelationshipType.NULL;
    }
  }

  public org.hl7.fhir.dstu3.model.DocumentReference.DocumentReferenceContentComponent convertDocumentReferenceContentComponent(org.hl7.fhir.instance.model.DocumentReference.DocumentReferenceContentComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.DocumentReference.DocumentReferenceContentComponent tgt = new org.hl7.fhir.dstu3.model.DocumentReference.DocumentReferenceContentComponent();
    copyElement(src, tgt);
    tgt.setAttachment(convertAttachment(src.getAttachment()));
    for (org.hl7.fhir.instance.model.Coding t : src.getFormat())
      tgt.setFormat(convertCoding(t));
    return tgt;
  }

  public org.hl7.fhir.instance.model.DocumentReference.DocumentReferenceContentComponent convertDocumentReferenceContentComponent(org.hl7.fhir.dstu3.model.DocumentReference.DocumentReferenceContentComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.DocumentReference.DocumentReferenceContentComponent tgt = new org.hl7.fhir.instance.model.DocumentReference.DocumentReferenceContentComponent();
    copyElement(src, tgt);
    tgt.setAttachment(convertAttachment(src.getAttachment()));
    tgt.addFormat(convertCoding(src.getFormat()));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.DocumentReference.DocumentReferenceContextComponent convertDocumentReferenceContextComponent(org.hl7.fhir.instance.model.DocumentReference.DocumentReferenceContextComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.DocumentReference.DocumentReferenceContextComponent tgt = new org.hl7.fhir.dstu3.model.DocumentReference.DocumentReferenceContextComponent();
    copyElement(src, tgt);
    tgt.setEncounter(convertReference(src.getEncounter()));
    for (org.hl7.fhir.instance.model.CodeableConcept t : src.getEvent())
      tgt.addEvent(convertCodeableConcept(t));
    tgt.setPeriod(convertPeriod(src.getPeriod()));
    tgt.setFacilityType(convertCodeableConcept(src.getFacilityType()));
    tgt.setPracticeSetting(convertCodeableConcept(src.getPracticeSetting()));
    tgt.setSourcePatientInfo(convertReference(src.getSourcePatientInfo()));
    for (org.hl7.fhir.instance.model.DocumentReference.DocumentReferenceContextRelatedComponent t : src.getRelated())
      tgt.addRelated(convertDocumentReferenceContextRelatedComponent(t));
    return tgt;
  }

  public org.hl7.fhir.instance.model.DocumentReference.DocumentReferenceContextComponent convertDocumentReferenceContextComponent(org.hl7.fhir.dstu3.model.DocumentReference.DocumentReferenceContextComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.DocumentReference.DocumentReferenceContextComponent tgt = new org.hl7.fhir.instance.model.DocumentReference.DocumentReferenceContextComponent();
    copyElement(src, tgt);
    tgt.setEncounter(convertReference(src.getEncounter()));
    for (org.hl7.fhir.dstu3.model.CodeableConcept t : src.getEvent())
      tgt.addEvent(convertCodeableConcept(t));
    tgt.setPeriod(convertPeriod(src.getPeriod()));
    tgt.setFacilityType(convertCodeableConcept(src.getFacilityType()));
    tgt.setPracticeSetting(convertCodeableConcept(src.getPracticeSetting()));
    tgt.setSourcePatientInfo(convertReference(src.getSourcePatientInfo()));
    for (org.hl7.fhir.dstu3.model.DocumentReference.DocumentReferenceContextRelatedComponent t : src.getRelated())
      tgt.addRelated(convertDocumentReferenceContextRelatedComponent(t));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.DocumentReference.DocumentReferenceContextRelatedComponent convertDocumentReferenceContextRelatedComponent(org.hl7.fhir.instance.model.DocumentReference.DocumentReferenceContextRelatedComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.DocumentReference.DocumentReferenceContextRelatedComponent tgt = new org.hl7.fhir.dstu3.model.DocumentReference.DocumentReferenceContextRelatedComponent();
    copyElement(src, tgt);
    tgt.setIdentifier(convertIdentifier(src.getIdentifier()));
    tgt.setRef(convertReference(src.getRef()));
    return tgt;
  }

  public org.hl7.fhir.instance.model.DocumentReference.DocumentReferenceContextRelatedComponent convertDocumentReferenceContextRelatedComponent(org.hl7.fhir.dstu3.model.DocumentReference.DocumentReferenceContextRelatedComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.DocumentReference.DocumentReferenceContextRelatedComponent tgt = new org.hl7.fhir.instance.model.DocumentReference.DocumentReferenceContextRelatedComponent();
    copyElement(src, tgt);
    tgt.setIdentifier(convertIdentifier(src.getIdentifier()));
    tgt.setRef(convertReference(src.getRef()));
    return tgt;
  }


  public org.hl7.fhir.dstu3.model.Encounter convertEncounter(org.hl7.fhir.instance.model.Encounter src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.Encounter tgt = new org.hl7.fhir.dstu3.model.Encounter();
    copyDomainResource(src, tgt);
    for (org.hl7.fhir.instance.model.Identifier t : src.getIdentifier())
      tgt.addIdentifier(convertIdentifier(t));
    tgt.setStatus(convertEncounterState(src.getStatus()));
//    for (org.hl7.fhir.instance.model.Encounter.EncounterStatusHistoryComponent t : src.getStatusHistory())
//      tgt.addStatusHistory(convertEncounterStatusHistoryComponent(t));
    tgt.setClass_(convertEncounterClass(src.getClass_()));
    for (org.hl7.fhir.instance.model.CodeableConcept t : src.getType())
      tgt.addType(convertCodeableConcept(t));
    tgt.setPriority(convertCodeableConcept(src.getPriority()));
    tgt.setSubject(convertReference(src.getPatient()));
    for (org.hl7.fhir.instance.model.Reference t : src.getEpisodeOfCare())
      tgt.addEpisodeOfCare(convertReference(t));
    for (org.hl7.fhir.instance.model.Reference t : src.getIncomingReferral())
      tgt.addIncomingReferral(convertReference(t));
    for (org.hl7.fhir.instance.model.Encounter.EncounterParticipantComponent t : src.getParticipant())
      tgt.addParticipant(convertEncounterParticipantComponent(t));
    tgt.setAppointment(convertReference(src.getAppointment()));
    tgt.setPeriod(convertPeriod(src.getPeriod()));
    tgt.setLength(convertDuration(src.getLength()));
    for (org.hl7.fhir.instance.model.CodeableConcept t : src.getReason())
      tgt.addReason(convertCodeableConcept(t));
    tgt.setHospitalization(convertEncounterHospitalizationComponent(src.getHospitalization()));
    for (org.hl7.fhir.instance.model.Encounter.EncounterLocationComponent t : src.getLocation())
      tgt.addLocation(convertEncounterLocationComponent(t));
    tgt.setServiceProvider(convertReference(src.getServiceProvider()));
    tgt.setPartOf(convertReference(src.getPartOf()));
    return tgt;
  }

  public org.hl7.fhir.instance.model.Encounter convertEncounter(org.hl7.fhir.dstu3.model.Encounter src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.Encounter tgt = new org.hl7.fhir.instance.model.Encounter();
    copyDomainResource(src, tgt);
    for (org.hl7.fhir.dstu3.model.Identifier t : src.getIdentifier())
      tgt.addIdentifier(convertIdentifier(t));
    tgt.setStatus(convertEncounterState(src.getStatus()));
//    for (org.hl7.fhir.dstu3.model.Encounter.EncounterStatusHistoryComponent t : src.getStatusHistory())
//      tgt.addStatusHistory(convertEncounterStatusHistoryComponent(t));
    tgt.setClass_(convertEncounterClass(src.getClass_()));
    for (org.hl7.fhir.dstu3.model.CodeableConcept t : src.getType())
      tgt.addType(convertCodeableConcept(t));
    tgt.setPriority(convertCodeableConcept(src.getPriority()));
    tgt.setPatient(convertReference(src.getSubject()));
    for (org.hl7.fhir.dstu3.model.Reference t : src.getEpisodeOfCare())
      tgt.addEpisodeOfCare(convertReference(t));
    for (org.hl7.fhir.dstu3.model.Reference t : src.getIncomingReferral())
      tgt.addIncomingReferral(convertReference(t));
    for (org.hl7.fhir.dstu3.model.Encounter.EncounterParticipantComponent t : src.getParticipant())
      tgt.addParticipant(convertEncounterParticipantComponent(t));
    tgt.setAppointment(convertReference(src.getAppointment()));
    tgt.setPeriod(convertPeriod(src.getPeriod()));
    tgt.setLength(convertDuration(src.getLength()));
    for (org.hl7.fhir.dstu3.model.CodeableConcept t : src.getReason())
      tgt.addReason(convertCodeableConcept(t));
    tgt.setHospitalization(convertEncounterHospitalizationComponent(src.getHospitalization()));
    for (org.hl7.fhir.dstu3.model.Encounter.EncounterLocationComponent t : src.getLocation())
      tgt.addLocation(convertEncounterLocationComponent(t));
    tgt.setServiceProvider(convertReference(src.getServiceProvider()));
    tgt.setPartOf(convertReference(src.getPartOf()));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.Encounter.EncounterStatus convertEncounterState(org.hl7.fhir.instance.model.Encounter.EncounterState src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case PLANNED: return org.hl7.fhir.dstu3.model.Encounter.EncounterStatus.PLANNED;
    case ARRIVED: return org.hl7.fhir.dstu3.model.Encounter.EncounterStatus.ARRIVED;
    case INPROGRESS: return org.hl7.fhir.dstu3.model.Encounter.EncounterStatus.INPROGRESS;
    case ONLEAVE: return org.hl7.fhir.dstu3.model.Encounter.EncounterStatus.ONLEAVE;
    case FINISHED: return org.hl7.fhir.dstu3.model.Encounter.EncounterStatus.FINISHED;
    case CANCELLED: return org.hl7.fhir.dstu3.model.Encounter.EncounterStatus.CANCELLED;
    default: return org.hl7.fhir.dstu3.model.Encounter.EncounterStatus.NULL;
    }
  }

  public org.hl7.fhir.instance.model.Encounter.EncounterState convertEncounterState(org.hl7.fhir.dstu3.model.Encounter.EncounterStatus src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case PLANNED: return org.hl7.fhir.instance.model.Encounter.EncounterState.PLANNED;
    case ARRIVED: return org.hl7.fhir.instance.model.Encounter.EncounterState.ARRIVED;
    case INPROGRESS: return org.hl7.fhir.instance.model.Encounter.EncounterState.INPROGRESS;
    case ONLEAVE: return org.hl7.fhir.instance.model.Encounter.EncounterState.ONLEAVE;
    case FINISHED: return org.hl7.fhir.instance.model.Encounter.EncounterState.FINISHED;
    case CANCELLED: return org.hl7.fhir.instance.model.Encounter.EncounterState.CANCELLED;
    default: return org.hl7.fhir.instance.model.Encounter.EncounterState.NULL;
    }
  }

  public org.hl7.fhir.dstu3.model.Coding convertEncounterClass(org.hl7.fhir.instance.model.Encounter.EncounterClass src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case INPATIENT: return new org.hl7.fhir.dstu3.model.Coding().setSystem("http://hl7.org/fhir/v3/ActCode").setCode("IMP");
    case OUTPATIENT: return new org.hl7.fhir.dstu3.model.Coding().setSystem("http://hl7.org/fhir/v3/ActCode").setCode("AMB");
    case AMBULATORY: return new org.hl7.fhir.dstu3.model.Coding().setSystem("http://hl7.org/fhir/v3/ActCode").setCode("AMB");
    case EMERGENCY: return new org.hl7.fhir.dstu3.model.Coding().setSystem("http://hl7.org/fhir/v3/ActCode").setCode("EMER");
    case HOME: return new org.hl7.fhir.dstu3.model.Coding().setSystem("http://hl7.org/fhir/v3/ActCode").setCode("HH");
    case FIELD: return new org.hl7.fhir.dstu3.model.Coding().setSystem("http://hl7.org/fhir/v3/ActCode").setCode("FLD");
    case DAYTIME: return new org.hl7.fhir.dstu3.model.Coding().setSystem("http://hl7.org/fhir/v3/ActCode").setCode("SS");
    case VIRTUAL: return new org.hl7.fhir.dstu3.model.Coding().setSystem("http://hl7.org/fhir/v3/ActCode").setCode("VR");
    default: return null;
    }
  }

  public org.hl7.fhir.instance.model.Encounter.EncounterClass convertEncounterClass(org.hl7.fhir.dstu3.model.Coding src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    if (src.getSystem().equals("http://hl7.org/fhir/v3/ActCode")) {
    if (src.getCode().equals("IMP")) return org.hl7.fhir.instance.model.Encounter.EncounterClass.INPATIENT;
    if (src.getCode().equals("AMB")) return org.hl7.fhir.instance.model.Encounter.EncounterClass.AMBULATORY;
    if (src.getCode().equals("EMER")) return org.hl7.fhir.instance.model.Encounter.EncounterClass.EMERGENCY;
    if (src.getCode().equals("HH")) return org.hl7.fhir.instance.model.Encounter.EncounterClass.HOME;
    if (src.getCode().equals("FLD")) return org.hl7.fhir.instance.model.Encounter.EncounterClass.FIELD;
    if (src.getCode().equals("")) return org.hl7.fhir.instance.model.Encounter.EncounterClass.DAYTIME;
    if (src.getCode().equals("VR")) return org.hl7.fhir.instance.model.Encounter.EncounterClass.VIRTUAL;
    }
    return org.hl7.fhir.instance.model.Encounter.EncounterClass.NULL;
  }

//  public org.hl7.fhir.dstu3.model.Encounter.EncounterStatusHistoryComponent convertEncounterStatusHistoryComponent(org.hl7.fhir.instance.model.Encounter.EncounterStatusHistoryComponent src) throws FHIRException {
//    if (src == null || src.isEmpty())
//      return null;
//    org.hl7.fhir.dstu3.model.Encounter.EncounterStatusHistoryComponent tgt = new org.hl7.fhir.dstu3.model.Encounter.EncounterStatusHistoryComponent();
//    copyElement(src, tgt);
//    tgt.setStatus(convertEncounterState(src.getStatus()));
//    tgt.setPeriod(convertPeriod(src.getPeriod()));
//    return tgt;
//  }

//  public org.hl7.fhir.instance.model.Encounter.EncounterStatusHistoryComponent convertEncounterStatusHistoryComponent(org.hl7.fhir.dstu3.model.Encounter.EncounterStatusHistoryComponent src) throws FHIRException {
//    if (src == null || src.isEmpty())
//      return null;
//    org.hl7.fhir.instance.model.Encounter.EncounterStatusHistoryComponent tgt = new org.hl7.fhir.instance.model.Encounter.EncounterStatusHistoryComponent();
//    copyElement(src, tgt);
//    tgt.setStatus(convertEncounterState(src.getStatus()));
//    tgt.setPeriod(convertPeriod(src.getPeriod()));
//    return tgt;
//  }

  public org.hl7.fhir.dstu3.model.Encounter.EncounterParticipantComponent convertEncounterParticipantComponent(org.hl7.fhir.instance.model.Encounter.EncounterParticipantComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.Encounter.EncounterParticipantComponent tgt = new org.hl7.fhir.dstu3.model.Encounter.EncounterParticipantComponent();
    copyElement(src, tgt);
    for (org.hl7.fhir.instance.model.CodeableConcept t : src.getType())
      tgt.addType(convertCodeableConcept(t));
    tgt.setPeriod(convertPeriod(src.getPeriod()));
    tgt.setIndividual(convertReference(src.getIndividual()));
    return tgt;
  }

  public org.hl7.fhir.instance.model.Encounter.EncounterParticipantComponent convertEncounterParticipantComponent(org.hl7.fhir.dstu3.model.Encounter.EncounterParticipantComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.Encounter.EncounterParticipantComponent tgt = new org.hl7.fhir.instance.model.Encounter.EncounterParticipantComponent();
    copyElement(src, tgt);
    for (org.hl7.fhir.dstu3.model.CodeableConcept t : src.getType())
      tgt.addType(convertCodeableConcept(t));
    tgt.setPeriod(convertPeriod(src.getPeriod()));
    tgt.setIndividual(convertReference(src.getIndividual()));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.Encounter.EncounterHospitalizationComponent convertEncounterHospitalizationComponent(org.hl7.fhir.instance.model.Encounter.EncounterHospitalizationComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.Encounter.EncounterHospitalizationComponent tgt = new org.hl7.fhir.dstu3.model.Encounter.EncounterHospitalizationComponent();
    copyElement(src, tgt);
    tgt.setPreAdmissionIdentifier(convertIdentifier(src.getPreAdmissionIdentifier()));
    tgt.setOrigin(convertReference(src.getOrigin()));
    tgt.setAdmitSource(convertCodeableConcept(src.getAdmitSource()));
    tgt.setReAdmission(convertCodeableConcept(src.getReAdmission()));
    for (org.hl7.fhir.instance.model.CodeableConcept t : src.getDietPreference())
      tgt.addDietPreference(convertCodeableConcept(t));
    for (org.hl7.fhir.instance.model.CodeableConcept t : src.getSpecialCourtesy())
      tgt.addSpecialCourtesy(convertCodeableConcept(t));
    for (org.hl7.fhir.instance.model.CodeableConcept t : src.getSpecialArrangement())
      tgt.addSpecialArrangement(convertCodeableConcept(t));
    tgt.setDestination(convertReference(src.getDestination()));
    tgt.setDischargeDisposition(convertCodeableConcept(src.getDischargeDisposition()));
    return tgt;
  }

  public org.hl7.fhir.instance.model.Encounter.EncounterHospitalizationComponent convertEncounterHospitalizationComponent(org.hl7.fhir.dstu3.model.Encounter.EncounterHospitalizationComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.Encounter.EncounterHospitalizationComponent tgt = new org.hl7.fhir.instance.model.Encounter.EncounterHospitalizationComponent();
    copyElement(src, tgt);
    tgt.setPreAdmissionIdentifier(convertIdentifier(src.getPreAdmissionIdentifier()));
    tgt.setOrigin(convertReference(src.getOrigin()));
    tgt.setAdmitSource(convertCodeableConcept(src.getAdmitSource()));
    tgt.setReAdmission(convertCodeableConcept(src.getReAdmission()));
    for (org.hl7.fhir.dstu3.model.CodeableConcept t : src.getDietPreference())
      tgt.addDietPreference(convertCodeableConcept(t));
    for (org.hl7.fhir.dstu3.model.CodeableConcept t : src.getSpecialCourtesy())
      tgt.addSpecialCourtesy(convertCodeableConcept(t));
    for (org.hl7.fhir.dstu3.model.CodeableConcept t : src.getSpecialArrangement())
      tgt.addSpecialArrangement(convertCodeableConcept(t));
    tgt.setDestination(convertReference(src.getDestination()));
    tgt.setDischargeDisposition(convertCodeableConcept(src.getDischargeDisposition()));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.Encounter.EncounterLocationComponent convertEncounterLocationComponent(org.hl7.fhir.instance.model.Encounter.EncounterLocationComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.Encounter.EncounterLocationComponent tgt = new org.hl7.fhir.dstu3.model.Encounter.EncounterLocationComponent();
    copyElement(src, tgt);
    tgt.setLocation(convertReference(src.getLocation()));
    tgt.setStatus(convertEncounterLocationStatus(src.getStatus()));
    tgt.setPeriod(convertPeriod(src.getPeriod()));
    return tgt;
  }

  public org.hl7.fhir.instance.model.Encounter.EncounterLocationComponent convertEncounterLocationComponent(org.hl7.fhir.dstu3.model.Encounter.EncounterLocationComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.Encounter.EncounterLocationComponent tgt = new org.hl7.fhir.instance.model.Encounter.EncounterLocationComponent();
    copyElement(src, tgt);
    tgt.setLocation(convertReference(src.getLocation()));
    tgt.setStatus(convertEncounterLocationStatus(src.getStatus()));
    tgt.setPeriod(convertPeriod(src.getPeriod()));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.Encounter.EncounterLocationStatus convertEncounterLocationStatus(org.hl7.fhir.instance.model.Encounter.EncounterLocationStatus src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case PLANNED: return org.hl7.fhir.dstu3.model.Encounter.EncounterLocationStatus.PLANNED;
    case ACTIVE: return org.hl7.fhir.dstu3.model.Encounter.EncounterLocationStatus.ACTIVE;
    case RESERVED: return org.hl7.fhir.dstu3.model.Encounter.EncounterLocationStatus.RESERVED;
    case COMPLETED: return org.hl7.fhir.dstu3.model.Encounter.EncounterLocationStatus.COMPLETED;
    default: return org.hl7.fhir.dstu3.model.Encounter.EncounterLocationStatus.NULL;
    }
  }

  public org.hl7.fhir.instance.model.Encounter.EncounterLocationStatus convertEncounterLocationStatus(org.hl7.fhir.dstu3.model.Encounter.EncounterLocationStatus src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case PLANNED: return org.hl7.fhir.instance.model.Encounter.EncounterLocationStatus.PLANNED;
    case ACTIVE: return org.hl7.fhir.instance.model.Encounter.EncounterLocationStatus.ACTIVE;
    case RESERVED: return org.hl7.fhir.instance.model.Encounter.EncounterLocationStatus.RESERVED;
    case COMPLETED: return org.hl7.fhir.instance.model.Encounter.EncounterLocationStatus.COMPLETED;
    default: return org.hl7.fhir.instance.model.Encounter.EncounterLocationStatus.NULL;
    }
  }

  public org.hl7.fhir.dstu3.model.EnrollmentRequest convertEnrollmentRequest(org.hl7.fhir.instance.model.EnrollmentRequest src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.EnrollmentRequest tgt = new org.hl7.fhir.dstu3.model.EnrollmentRequest();
    copyDomainResource(src, tgt);
    for (org.hl7.fhir.instance.model.Identifier t : src.getIdentifier())
      tgt.addIdentifier(convertIdentifier(t));
    tgt.setCreated(src.getCreated());
//    tgt.setTarget(convertReference(src.getTarget()));
    tgt.setProvider(convertReference(src.getProvider()));
    tgt.setOrganization(convertReference(src.getOrganization()));
    tgt.setSubject(convertReference(src.getSubject()));
    tgt.setCoverage(convertReference(src.getCoverage()));
//    tgt.setRelationship(convertCoding(src.getRelationship()));
    return tgt;
  }

  public org.hl7.fhir.instance.model.EnrollmentRequest convertEnrollmentRequest(org.hl7.fhir.dstu3.model.EnrollmentRequest src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.EnrollmentRequest tgt = new org.hl7.fhir.instance.model.EnrollmentRequest();
    copyDomainResource(src, tgt);
    for (org.hl7.fhir.dstu3.model.Identifier t : src.getIdentifier())
      tgt.addIdentifier(convertIdentifier(t));
    tgt.setCreated(src.getCreated());
//    tgt.setTarget(convertReference(src.getTarget()));
//    tgt.setProvider(convertReference(src.getProvider()));
//    tgt.setOrganization(convertReference(src.getOrganization()));
//    tgt.setSubject(convertReference(src.getSubject()));
    tgt.setCoverage(convertReference(src.getCoverage()));
//    tgt.setRelationship(convertCoding(src.getRelationship()));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.EnrollmentResponse convertEnrollmentResponse(org.hl7.fhir.instance.model.EnrollmentResponse src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.EnrollmentResponse tgt = new org.hl7.fhir.dstu3.model.EnrollmentResponse();
    copyDomainResource(src, tgt);
    for (org.hl7.fhir.instance.model.Identifier t : src.getIdentifier())
      tgt.addIdentifier(convertIdentifier(t));
    tgt.setRequest(convertReference(src.getRequest()));
//    tgt.setOutcome(convertRemittanceOutcome(src.getOutcome()));
    tgt.setDisposition(src.getDisposition());
    tgt.setCreated(src.getCreated());
    tgt.setOrganization(convertReference(src.getOrganization()));
    tgt.setRequestProvider(convertReference(src.getRequestProvider()));
    tgt.setRequestOrganization(convertReference(src.getRequestOrganization()));
    return tgt;
  }

  public org.hl7.fhir.instance.model.EnrollmentResponse convertEnrollmentResponse(org.hl7.fhir.dstu3.model.EnrollmentResponse src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.EnrollmentResponse tgt = new org.hl7.fhir.instance.model.EnrollmentResponse();
    copyDomainResource(src, tgt);
    for (org.hl7.fhir.dstu3.model.Identifier t : src.getIdentifier())
      tgt.addIdentifier(convertIdentifier(t));
//      tgt.setRequest(convertReference(src.getRequestReference()));
//    tgt.setOutcome(convertRemittanceOutcome(src.getOutcome()));
    tgt.setDisposition(src.getDisposition());
    tgt.setCreated(src.getCreated());
//      tgt.setOrganization(convertReference(src.getOrganizationReference()));
//      tgt.setRequestProvider(convertReference(src.getRequestProviderReference()));
//    tgt.setRequestOrganization(convertReference(src.getRequestOrganizationReference()));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.EpisodeOfCare convertEpisodeOfCare(org.hl7.fhir.instance.model.EpisodeOfCare src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.EpisodeOfCare tgt = new org.hl7.fhir.dstu3.model.EpisodeOfCare();
    copyDomainResource(src, tgt);
    for (org.hl7.fhir.instance.model.Identifier t : src.getIdentifier())
      tgt.addIdentifier(convertIdentifier(t));
    tgt.setStatus(convertEpisodeOfCareStatus(src.getStatus()));
    for (org.hl7.fhir.instance.model.EpisodeOfCare.EpisodeOfCareStatusHistoryComponent t : src.getStatusHistory())
      tgt.addStatusHistory(convertEpisodeOfCareStatusHistoryComponent(t));
    for (org.hl7.fhir.instance.model.CodeableConcept t : src.getType())
      tgt.addType(convertCodeableConcept(t));
    tgt.setPatient(convertReference(src.getPatient()));
    tgt.setManagingOrganization(convertReference(src.getManagingOrganization()));
    tgt.setPeriod(convertPeriod(src.getPeriod()));
    for (org.hl7.fhir.instance.model.Reference t : src.getReferralRequest())
      tgt.addReferralRequest(convertReference(t));
    tgt.setCareManager(convertReference(src.getCareManager()));
    return tgt;
  }

  public org.hl7.fhir.instance.model.EpisodeOfCare convertEpisodeOfCare(org.hl7.fhir.dstu3.model.EpisodeOfCare src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.EpisodeOfCare tgt = new org.hl7.fhir.instance.model.EpisodeOfCare();
    copyDomainResource(src, tgt);
    for (org.hl7.fhir.dstu3.model.Identifier t : src.getIdentifier())
      tgt.addIdentifier(convertIdentifier(t));
    tgt.setStatus(convertEpisodeOfCareStatus(src.getStatus()));
    for (org.hl7.fhir.dstu3.model.EpisodeOfCare.EpisodeOfCareStatusHistoryComponent t : src.getStatusHistory())
      tgt.addStatusHistory(convertEpisodeOfCareStatusHistoryComponent(t));
    for (org.hl7.fhir.dstu3.model.CodeableConcept t : src.getType())
      tgt.addType(convertCodeableConcept(t));
    tgt.setPatient(convertReference(src.getPatient()));
    tgt.setManagingOrganization(convertReference(src.getManagingOrganization()));
    tgt.setPeriod(convertPeriod(src.getPeriod()));
    for (org.hl7.fhir.dstu3.model.Reference t : src.getReferralRequest())
      tgt.addReferralRequest(convertReference(t));
    tgt.setCareManager(convertReference(src.getCareManager()));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.EpisodeOfCare.EpisodeOfCareStatus convertEpisodeOfCareStatus(org.hl7.fhir.instance.model.EpisodeOfCare.EpisodeOfCareStatus src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case PLANNED: return org.hl7.fhir.dstu3.model.EpisodeOfCare.EpisodeOfCareStatus.PLANNED;
    case WAITLIST: return org.hl7.fhir.dstu3.model.EpisodeOfCare.EpisodeOfCareStatus.WAITLIST;
    case ACTIVE: return org.hl7.fhir.dstu3.model.EpisodeOfCare.EpisodeOfCareStatus.ACTIVE;
    case ONHOLD: return org.hl7.fhir.dstu3.model.EpisodeOfCare.EpisodeOfCareStatus.ONHOLD;
    case FINISHED: return org.hl7.fhir.dstu3.model.EpisodeOfCare.EpisodeOfCareStatus.FINISHED;
    case CANCELLED: return org.hl7.fhir.dstu3.model.EpisodeOfCare.EpisodeOfCareStatus.CANCELLED;
    default: return org.hl7.fhir.dstu3.model.EpisodeOfCare.EpisodeOfCareStatus.NULL;
    }
  }

  public org.hl7.fhir.instance.model.EpisodeOfCare.EpisodeOfCareStatus convertEpisodeOfCareStatus(org.hl7.fhir.dstu3.model.EpisodeOfCare.EpisodeOfCareStatus src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case PLANNED: return org.hl7.fhir.instance.model.EpisodeOfCare.EpisodeOfCareStatus.PLANNED;
    case WAITLIST: return org.hl7.fhir.instance.model.EpisodeOfCare.EpisodeOfCareStatus.WAITLIST;
    case ACTIVE: return org.hl7.fhir.instance.model.EpisodeOfCare.EpisodeOfCareStatus.ACTIVE;
    case ONHOLD: return org.hl7.fhir.instance.model.EpisodeOfCare.EpisodeOfCareStatus.ONHOLD;
    case FINISHED: return org.hl7.fhir.instance.model.EpisodeOfCare.EpisodeOfCareStatus.FINISHED;
    case CANCELLED: return org.hl7.fhir.instance.model.EpisodeOfCare.EpisodeOfCareStatus.CANCELLED;
    default: return org.hl7.fhir.instance.model.EpisodeOfCare.EpisodeOfCareStatus.NULL;
    }
  }

  public org.hl7.fhir.dstu3.model.EpisodeOfCare.EpisodeOfCareStatusHistoryComponent convertEpisodeOfCareStatusHistoryComponent(org.hl7.fhir.instance.model.EpisodeOfCare.EpisodeOfCareStatusHistoryComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.EpisodeOfCare.EpisodeOfCareStatusHistoryComponent tgt = new org.hl7.fhir.dstu3.model.EpisodeOfCare.EpisodeOfCareStatusHistoryComponent();
    copyElement(src, tgt);
    tgt.setStatus(convertEpisodeOfCareStatus(src.getStatus()));
    tgt.setPeriod(convertPeriod(src.getPeriod()));
    return tgt;
  }

  public org.hl7.fhir.instance.model.EpisodeOfCare.EpisodeOfCareStatusHistoryComponent convertEpisodeOfCareStatusHistoryComponent(org.hl7.fhir.dstu3.model.EpisodeOfCare.EpisodeOfCareStatusHistoryComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.EpisodeOfCare.EpisodeOfCareStatusHistoryComponent tgt = new org.hl7.fhir.instance.model.EpisodeOfCare.EpisodeOfCareStatusHistoryComponent();
    copyElement(src, tgt);
    tgt.setStatus(convertEpisodeOfCareStatus(src.getStatus()));
    tgt.setPeriod(convertPeriod(src.getPeriod()));
    return tgt;
  }


  public org.hl7.fhir.dstu3.model.FamilyMemberHistory convertFamilyMemberHistory(org.hl7.fhir.instance.model.FamilyMemberHistory src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.FamilyMemberHistory tgt = new org.hl7.fhir.dstu3.model.FamilyMemberHistory();
    copyDomainResource(src, tgt);
    for (org.hl7.fhir.instance.model.Identifier t : src.getIdentifier())
      tgt.addIdentifier(convertIdentifier(t));
    tgt.setPatient(convertReference(src.getPatient()));
    if (src.hasDate())
      tgt.setDate(src.getDate());
    tgt.setStatus(convertFamilyHistoryStatus(src.getStatus()));
    tgt.setName(src.getName());
    tgt.setRelationship(convertCodeableConcept(src.getRelationship()));
    tgt.setGender(convertAdministrativeGender(src.getGender()));
    tgt.setBorn(convertType(src.getBorn()));
    tgt.setAge(convertType(src.getAge()));
    tgt.setDeceased(convertType(src.getDeceased()));
//    tgt.setNote(convertAnnotation(src.getNote()));
    for (org.hl7.fhir.instance.model.FamilyMemberHistory.FamilyMemberHistoryConditionComponent t : src.getCondition())
      tgt.addCondition(convertFamilyMemberHistoryConditionComponent(t));
    return tgt;
  }

  public org.hl7.fhir.instance.model.FamilyMemberHistory convertFamilyMemberHistory(org.hl7.fhir.dstu3.model.FamilyMemberHistory src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.FamilyMemberHistory tgt = new org.hl7.fhir.instance.model.FamilyMemberHistory();
    copyDomainResource(src, tgt);
    for (org.hl7.fhir.dstu3.model.Identifier t : src.getIdentifier())
      tgt.addIdentifier(convertIdentifier(t));
    tgt.setPatient(convertReference(src.getPatient()));
    if (src.hasDate())
      tgt.setDate(src.getDate());
    tgt.setStatus(convertFamilyHistoryStatus(src.getStatus()));
    tgt.setName(src.getName());
    tgt.setRelationship(convertCodeableConcept(src.getRelationship()));
    tgt.setGender(convertAdministrativeGender(src.getGender()));
    tgt.setBorn(convertType(src.getBorn()));
    tgt.setAge(convertType(src.getAge()));
    tgt.setDeceased(convertType(src.getDeceased()));
//    tgt.setNote(convertAnnotation(src.getNote()));
    for (org.hl7.fhir.dstu3.model.FamilyMemberHistory.FamilyMemberHistoryConditionComponent t : src.getCondition())
      tgt.addCondition(convertFamilyMemberHistoryConditionComponent(t));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.FamilyMemberHistory.FamilyHistoryStatus convertFamilyHistoryStatus(org.hl7.fhir.instance.model.FamilyMemberHistory.FamilyHistoryStatus src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case PARTIAL: return org.hl7.fhir.dstu3.model.FamilyMemberHistory.FamilyHistoryStatus.PARTIAL;
    case COMPLETED: return org.hl7.fhir.dstu3.model.FamilyMemberHistory.FamilyHistoryStatus.COMPLETED;
    case ENTEREDINERROR: return org.hl7.fhir.dstu3.model.FamilyMemberHistory.FamilyHistoryStatus.ENTEREDINERROR;
    case HEALTHUNKNOWN: return org.hl7.fhir.dstu3.model.FamilyMemberHistory.FamilyHistoryStatus.HEALTHUNKNOWN;
    default: return org.hl7.fhir.dstu3.model.FamilyMemberHistory.FamilyHistoryStatus.NULL;
    }
  }

  public org.hl7.fhir.instance.model.FamilyMemberHistory.FamilyHistoryStatus convertFamilyHistoryStatus(org.hl7.fhir.dstu3.model.FamilyMemberHistory.FamilyHistoryStatus src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case PARTIAL: return org.hl7.fhir.instance.model.FamilyMemberHistory.FamilyHistoryStatus.PARTIAL;
    case COMPLETED: return org.hl7.fhir.instance.model.FamilyMemberHistory.FamilyHistoryStatus.COMPLETED;
    case ENTEREDINERROR: return org.hl7.fhir.instance.model.FamilyMemberHistory.FamilyHistoryStatus.ENTEREDINERROR;
    case HEALTHUNKNOWN: return org.hl7.fhir.instance.model.FamilyMemberHistory.FamilyHistoryStatus.HEALTHUNKNOWN;
    default: return org.hl7.fhir.instance.model.FamilyMemberHistory.FamilyHistoryStatus.NULL;
    }
  }

  public org.hl7.fhir.dstu3.model.FamilyMemberHistory.FamilyMemberHistoryConditionComponent convertFamilyMemberHistoryConditionComponent(org.hl7.fhir.instance.model.FamilyMemberHistory.FamilyMemberHistoryConditionComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.FamilyMemberHistory.FamilyMemberHistoryConditionComponent tgt = new org.hl7.fhir.dstu3.model.FamilyMemberHistory.FamilyMemberHistoryConditionComponent();
    copyElement(src, tgt);
    tgt.setCode(convertCodeableConcept(src.getCode()));
    tgt.setOutcome(convertCodeableConcept(src.getOutcome()));
    tgt.setOnset(convertType(src.getOnset()));
//    tgt.setNote(convertAnnotation(src.getNote()));
    return tgt;
  }

  public org.hl7.fhir.instance.model.FamilyMemberHistory.FamilyMemberHistoryConditionComponent convertFamilyMemberHistoryConditionComponent(org.hl7.fhir.dstu3.model.FamilyMemberHistory.FamilyMemberHistoryConditionComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.FamilyMemberHistory.FamilyMemberHistoryConditionComponent tgt = new org.hl7.fhir.instance.model.FamilyMemberHistory.FamilyMemberHistoryConditionComponent();
    copyElement(src, tgt);
    tgt.setCode(convertCodeableConcept(src.getCode()));
    tgt.setOutcome(convertCodeableConcept(src.getOutcome()));
    tgt.setOnset(convertType(src.getOnset()));
//    tgt.setNote(convertAnnotation(src.getNote()));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.Flag convertFlag(org.hl7.fhir.instance.model.Flag src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.Flag tgt = new org.hl7.fhir.dstu3.model.Flag();
    copyDomainResource(src, tgt);
    for (org.hl7.fhir.instance.model.Identifier t : src.getIdentifier())
      tgt.addIdentifier(convertIdentifier(t));
    tgt.setCategory(convertCodeableConcept(src.getCategory()));
    tgt.setStatus(convertFlagStatus(src.getStatus()));
    tgt.setPeriod(convertPeriod(src.getPeriod()));
    tgt.setSubject(convertReference(src.getSubject()));
    tgt.setEncounter(convertReference(src.getEncounter()));
    tgt.setAuthor(convertReference(src.getAuthor()));
    tgt.setCode(convertCodeableConcept(src.getCode()));
    return tgt;
  }

  public org.hl7.fhir.instance.model.Flag convertFlag(org.hl7.fhir.dstu3.model.Flag src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.Flag tgt = new org.hl7.fhir.instance.model.Flag();
    copyDomainResource(src, tgt);
    for (org.hl7.fhir.dstu3.model.Identifier t : src.getIdentifier())
      tgt.addIdentifier(convertIdentifier(t));
    tgt.setCategory(convertCodeableConcept(src.getCategory()));
    tgt.setStatus(convertFlagStatus(src.getStatus()));
    tgt.setPeriod(convertPeriod(src.getPeriod()));
    tgt.setSubject(convertReference(src.getSubject()));
    tgt.setEncounter(convertReference(src.getEncounter()));
    tgt.setAuthor(convertReference(src.getAuthor()));
    tgt.setCode(convertCodeableConcept(src.getCode()));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.Flag.FlagStatus convertFlagStatus(org.hl7.fhir.instance.model.Flag.FlagStatus src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case ACTIVE: return org.hl7.fhir.dstu3.model.Flag.FlagStatus.ACTIVE;
    case INACTIVE: return org.hl7.fhir.dstu3.model.Flag.FlagStatus.INACTIVE;
    case ENTEREDINERROR: return org.hl7.fhir.dstu3.model.Flag.FlagStatus.ENTEREDINERROR;
    default: return org.hl7.fhir.dstu3.model.Flag.FlagStatus.NULL;
    }
  }

  public org.hl7.fhir.instance.model.Flag.FlagStatus convertFlagStatus(org.hl7.fhir.dstu3.model.Flag.FlagStatus src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case ACTIVE: return org.hl7.fhir.instance.model.Flag.FlagStatus.ACTIVE;
    case INACTIVE: return org.hl7.fhir.instance.model.Flag.FlagStatus.INACTIVE;
    case ENTEREDINERROR: return org.hl7.fhir.instance.model.Flag.FlagStatus.ENTEREDINERROR;
    default: return org.hl7.fhir.instance.model.Flag.FlagStatus.NULL;
    }
  }

  public org.hl7.fhir.dstu3.model.Group convertGroup(org.hl7.fhir.instance.model.Group src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.Group tgt = new org.hl7.fhir.dstu3.model.Group();
    copyDomainResource(src, tgt);
    for (org.hl7.fhir.instance.model.Identifier t : src.getIdentifier())
      tgt.addIdentifier(convertIdentifier(t));
    tgt.setType(convertGroupType(src.getType()));
    tgt.setActual(src.getActual());
    tgt.setCode(convertCodeableConcept(src.getCode()));
    tgt.setName(src.getName());
    tgt.setQuantity(src.getQuantity());
    for (org.hl7.fhir.instance.model.Group.GroupCharacteristicComponent t : src.getCharacteristic())
      tgt.addCharacteristic(convertGroupCharacteristicComponent(t));
    for (org.hl7.fhir.instance.model.Group.GroupMemberComponent t : src.getMember())
      tgt.addMember(convertGroupMemberComponent(t));
    return tgt;
  }

  public org.hl7.fhir.instance.model.Group convertGroup(org.hl7.fhir.dstu3.model.Group src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.Group tgt = new org.hl7.fhir.instance.model.Group();
    copyDomainResource(src, tgt);
    for (org.hl7.fhir.dstu3.model.Identifier t : src.getIdentifier())
      tgt.addIdentifier(convertIdentifier(t));
    tgt.setType(convertGroupType(src.getType()));
    tgt.setActual(src.getActual());
    tgt.setCode(convertCodeableConcept(src.getCode()));
    tgt.setName(src.getName());
    tgt.setQuantity(src.getQuantity());
    for (org.hl7.fhir.dstu3.model.Group.GroupCharacteristicComponent t : src.getCharacteristic())
      tgt.addCharacteristic(convertGroupCharacteristicComponent(t));
    for (org.hl7.fhir.dstu3.model.Group.GroupMemberComponent t : src.getMember())
      tgt.addMember(convertGroupMemberComponent(t));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.Group.GroupType convertGroupType(org.hl7.fhir.instance.model.Group.GroupType src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case PERSON: return org.hl7.fhir.dstu3.model.Group.GroupType.PERSON;
    case ANIMAL: return org.hl7.fhir.dstu3.model.Group.GroupType.ANIMAL;
    case PRACTITIONER: return org.hl7.fhir.dstu3.model.Group.GroupType.PRACTITIONER;
    case DEVICE: return org.hl7.fhir.dstu3.model.Group.GroupType.DEVICE;
    case MEDICATION: return org.hl7.fhir.dstu3.model.Group.GroupType.MEDICATION;
    case SUBSTANCE: return org.hl7.fhir.dstu3.model.Group.GroupType.SUBSTANCE;
    default: return org.hl7.fhir.dstu3.model.Group.GroupType.NULL;
    }
  }

  public org.hl7.fhir.instance.model.Group.GroupType convertGroupType(org.hl7.fhir.dstu3.model.Group.GroupType src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case PERSON: return org.hl7.fhir.instance.model.Group.GroupType.PERSON;
    case ANIMAL: return org.hl7.fhir.instance.model.Group.GroupType.ANIMAL;
    case PRACTITIONER: return org.hl7.fhir.instance.model.Group.GroupType.PRACTITIONER;
    case DEVICE: return org.hl7.fhir.instance.model.Group.GroupType.DEVICE;
    case MEDICATION: return org.hl7.fhir.instance.model.Group.GroupType.MEDICATION;
    case SUBSTANCE: return org.hl7.fhir.instance.model.Group.GroupType.SUBSTANCE;
    default: return org.hl7.fhir.instance.model.Group.GroupType.NULL;
    }
  }

  public org.hl7.fhir.dstu3.model.Group.GroupCharacteristicComponent convertGroupCharacteristicComponent(org.hl7.fhir.instance.model.Group.GroupCharacteristicComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.Group.GroupCharacteristicComponent tgt = new org.hl7.fhir.dstu3.model.Group.GroupCharacteristicComponent();
    copyElement(src, tgt);
    tgt.setCode(convertCodeableConcept(src.getCode()));
    tgt.setValue(convertType(src.getValue()));
    tgt.setExclude(src.getExclude());
    tgt.setPeriod(convertPeriod(src.getPeriod()));
    return tgt;
  }

  public org.hl7.fhir.instance.model.Group.GroupCharacteristicComponent convertGroupCharacteristicComponent(org.hl7.fhir.dstu3.model.Group.GroupCharacteristicComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.Group.GroupCharacteristicComponent tgt = new org.hl7.fhir.instance.model.Group.GroupCharacteristicComponent();
    copyElement(src, tgt);
    tgt.setCode(convertCodeableConcept(src.getCode()));
    tgt.setValue(convertType(src.getValue()));
    tgt.setExclude(src.getExclude());
    tgt.setPeriod(convertPeriod(src.getPeriod()));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.Group.GroupMemberComponent convertGroupMemberComponent(org.hl7.fhir.instance.model.Group.GroupMemberComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.Group.GroupMemberComponent tgt = new org.hl7.fhir.dstu3.model.Group.GroupMemberComponent();
    copyElement(src, tgt);
    tgt.setEntity(convertReference(src.getEntity()));
    tgt.setPeriod(convertPeriod(src.getPeriod()));
    tgt.setInactive(src.getInactive());
    return tgt;
  }

  public org.hl7.fhir.instance.model.Group.GroupMemberComponent convertGroupMemberComponent(org.hl7.fhir.dstu3.model.Group.GroupMemberComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.Group.GroupMemberComponent tgt = new org.hl7.fhir.instance.model.Group.GroupMemberComponent();
    copyElement(src, tgt);
    tgt.setEntity(convertReference(src.getEntity()));
    tgt.setPeriod(convertPeriod(src.getPeriod()));
    tgt.setInactive(src.getInactive());
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.HealthcareService convertHealthcareService(org.hl7.fhir.instance.model.HealthcareService src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.HealthcareService tgt = new org.hl7.fhir.dstu3.model.HealthcareService();
    copyDomainResource(src, tgt);
    for (org.hl7.fhir.instance.model.Identifier t : src.getIdentifier())
      tgt.addIdentifier(convertIdentifier(t));
    tgt.setProvidedBy(convertReference(src.getProvidedBy()));
//    tgt.setServiceCategory(convertCodeableConcept(src.getServiceCategory()));
    for (org.hl7.fhir.instance.model.HealthcareService.ServiceTypeComponent t : src.getServiceType()) {
//      if (t.hasType())
//        tgt.addServiceType(convertCodeableConcept(t.getType()));
      for (org.hl7.fhir.instance.model.CodeableConcept tj : t.getSpecialty())
        tgt.addSpecialty(convertCodeableConcept(tj));
    }
    tgt.addLocation(convertReference(src.getLocation()));
//    tgt.setServiceName(src.getServiceName());
    tgt.setComment(src.getComment());
    tgt.setExtraDetails(src.getExtraDetails());
    tgt.setPhoto(convertAttachment(src.getPhoto()));
    for (org.hl7.fhir.instance.model.ContactPoint t : src.getTelecom())
      tgt.addTelecom(convertContactPoint(t));
    for (org.hl7.fhir.instance.model.Reference t : src.getCoverageArea())
      tgt.addCoverageArea(convertReference(t));
    for (org.hl7.fhir.instance.model.CodeableConcept t : src.getServiceProvisionCode())
      tgt.addServiceProvisionCode(convertCodeableConcept(t));
    tgt.setEligibility(convertCodeableConcept(src.getEligibility()));
    tgt.setEligibilityNote(src.getEligibilityNote());
    for (org.hl7.fhir.instance.model.StringType t : src.getProgramName())
      tgt.addProgramName(t.getValue());
    for (org.hl7.fhir.instance.model.CodeableConcept t : src.getCharacteristic())
      tgt.addCharacteristic(convertCodeableConcept(t));
    for (org.hl7.fhir.instance.model.CodeableConcept t : src.getReferralMethod())
      tgt.addReferralMethod(convertCodeableConcept(t));
//    tgt.setPublicKey(src.getPublicKey());
    tgt.setAppointmentRequired(src.getAppointmentRequired());
    for (org.hl7.fhir.instance.model.HealthcareService.HealthcareServiceAvailableTimeComponent t : src.getAvailableTime())
      tgt.addAvailableTime(convertHealthcareServiceAvailableTimeComponent(t));
    for (org.hl7.fhir.instance.model.HealthcareService.HealthcareServiceNotAvailableComponent t : src.getNotAvailable())
      tgt.addNotAvailable(convertHealthcareServiceNotAvailableComponent(t));
    tgt.setAvailabilityExceptions(src.getAvailabilityExceptions());
    return tgt;
  }

  public org.hl7.fhir.instance.model.HealthcareService convertHealthcareService(org.hl7.fhir.dstu3.model.HealthcareService src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.HealthcareService tgt = new org.hl7.fhir.instance.model.HealthcareService();
    copyDomainResource(src, tgt);
    for (org.hl7.fhir.dstu3.model.Identifier t : src.getIdentifier())
      tgt.addIdentifier(convertIdentifier(t));
    tgt.setProvidedBy(convertReference(src.getProvidedBy()));
//    tgt.setServiceCategory(convertCodeableConcept(src.getServiceCategory()));
//    for (org.hl7.fhir.dstu3.model.CodeableConcept t : src.getServiceType())
//      tgt.addServiceType().setType(convertCodeableConcept(t));
    for (org.hl7.fhir.dstu3.model.CodeableConcept t : src.getSpecialty()) {
      if (!tgt.hasServiceType())
        tgt.addServiceType();
      tgt.getServiceType().get(0).addSpecialty(convertCodeableConcept(t));
    }
    for (org.hl7.fhir.dstu3.model.Reference t : src.getLocation())
      tgt.setLocation(convertReference(t));
//    tgt.setServiceName(src.getServiceName());
    tgt.setComment(src.getComment());
    tgt.setExtraDetails(src.getExtraDetails());
    tgt.setPhoto(convertAttachment(src.getPhoto()));
    for (org.hl7.fhir.dstu3.model.ContactPoint t : src.getTelecom())
      tgt.addTelecom(convertContactPoint(t));
    for (org.hl7.fhir.dstu3.model.Reference t : src.getCoverageArea())
      tgt.addCoverageArea(convertReference(t));
    for (org.hl7.fhir.dstu3.model.CodeableConcept t : src.getServiceProvisionCode())
      tgt.addServiceProvisionCode(convertCodeableConcept(t));
    tgt.setEligibility(convertCodeableConcept(src.getEligibility()));
    tgt.setEligibilityNote(src.getEligibilityNote());
    for (org.hl7.fhir.dstu3.model.StringType t : src.getProgramName())
      tgt.addProgramName(t.getValue());
    for (org.hl7.fhir.dstu3.model.CodeableConcept t : src.getCharacteristic())
      tgt.addCharacteristic(convertCodeableConcept(t));
    for (org.hl7.fhir.dstu3.model.CodeableConcept t : src.getReferralMethod())
      tgt.addReferralMethod(convertCodeableConcept(t));
//    tgt.setPublicKey(src.getPublicKey());
    tgt.setAppointmentRequired(src.getAppointmentRequired());
    for (org.hl7.fhir.dstu3.model.HealthcareService.HealthcareServiceAvailableTimeComponent t : src.getAvailableTime())
      tgt.addAvailableTime(convertHealthcareServiceAvailableTimeComponent(t));
    for (org.hl7.fhir.dstu3.model.HealthcareService.HealthcareServiceNotAvailableComponent t : src.getNotAvailable())
      tgt.addNotAvailable(convertHealthcareServiceNotAvailableComponent(t));
    tgt.setAvailabilityExceptions(src.getAvailabilityExceptions());
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.HealthcareService.HealthcareServiceAvailableTimeComponent convertHealthcareServiceAvailableTimeComponent(org.hl7.fhir.instance.model.HealthcareService.HealthcareServiceAvailableTimeComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.HealthcareService.HealthcareServiceAvailableTimeComponent tgt = new org.hl7.fhir.dstu3.model.HealthcareService.HealthcareServiceAvailableTimeComponent();
    copyElement(src, tgt);
    for (org.hl7.fhir.instance.model.Enumeration<org.hl7.fhir.instance.model.HealthcareService.DaysOfWeek> t : src.getDaysOfWeek())
      tgt.addDaysOfWeek(convertDaysOfWeek(t.getValue()));
    tgt.setAllDay(src.getAllDay());
    tgt.setAvailableStartTime(src.getAvailableStartTime());
    tgt.setAvailableEndTime(src.getAvailableEndTime());
    return tgt;
  }

  public org.hl7.fhir.instance.model.HealthcareService.HealthcareServiceAvailableTimeComponent convertHealthcareServiceAvailableTimeComponent(org.hl7.fhir.dstu3.model.HealthcareService.HealthcareServiceAvailableTimeComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.HealthcareService.HealthcareServiceAvailableTimeComponent tgt = new org.hl7.fhir.instance.model.HealthcareService.HealthcareServiceAvailableTimeComponent();
    copyElement(src, tgt);
    for (org.hl7.fhir.dstu3.model.Enumeration<org.hl7.fhir.dstu3.model.HealthcareService.DaysOfWeek> t : src.getDaysOfWeek())
      tgt.addDaysOfWeek(convertDaysOfWeek(t.getValue()));
    tgt.setAllDay(src.getAllDay());
    tgt.setAvailableStartTime(src.getAvailableStartTime());
    tgt.setAvailableEndTime(src.getAvailableEndTime());
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.HealthcareService.DaysOfWeek convertDaysOfWeek(org.hl7.fhir.instance.model.HealthcareService.DaysOfWeek src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case MON: return org.hl7.fhir.dstu3.model.HealthcareService.DaysOfWeek.MON;
    case TUE: return org.hl7.fhir.dstu3.model.HealthcareService.DaysOfWeek.TUE;
    case WED: return org.hl7.fhir.dstu3.model.HealthcareService.DaysOfWeek.WED;
    case THU: return org.hl7.fhir.dstu3.model.HealthcareService.DaysOfWeek.THU;
    case FRI: return org.hl7.fhir.dstu3.model.HealthcareService.DaysOfWeek.FRI;
    case SAT: return org.hl7.fhir.dstu3.model.HealthcareService.DaysOfWeek.SAT;
    case SUN: return org.hl7.fhir.dstu3.model.HealthcareService.DaysOfWeek.SUN;
    default: return org.hl7.fhir.dstu3.model.HealthcareService.DaysOfWeek.NULL;
    }
  }

  public org.hl7.fhir.instance.model.HealthcareService.DaysOfWeek convertDaysOfWeek(org.hl7.fhir.dstu3.model.HealthcareService.DaysOfWeek src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case MON: return org.hl7.fhir.instance.model.HealthcareService.DaysOfWeek.MON;
    case TUE: return org.hl7.fhir.instance.model.HealthcareService.DaysOfWeek.TUE;
    case WED: return org.hl7.fhir.instance.model.HealthcareService.DaysOfWeek.WED;
    case THU: return org.hl7.fhir.instance.model.HealthcareService.DaysOfWeek.THU;
    case FRI: return org.hl7.fhir.instance.model.HealthcareService.DaysOfWeek.FRI;
    case SAT: return org.hl7.fhir.instance.model.HealthcareService.DaysOfWeek.SAT;
    case SUN: return org.hl7.fhir.instance.model.HealthcareService.DaysOfWeek.SUN;
    default: return org.hl7.fhir.instance.model.HealthcareService.DaysOfWeek.NULL;
    }
  }

  public org.hl7.fhir.dstu3.model.HealthcareService.HealthcareServiceNotAvailableComponent convertHealthcareServiceNotAvailableComponent(org.hl7.fhir.instance.model.HealthcareService.HealthcareServiceNotAvailableComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.HealthcareService.HealthcareServiceNotAvailableComponent tgt = new org.hl7.fhir.dstu3.model.HealthcareService.HealthcareServiceNotAvailableComponent();
    copyElement(src, tgt);
    tgt.setDescription(src.getDescription());
    tgt.setDuring(convertPeriod(src.getDuring()));
    return tgt;
  }

  public org.hl7.fhir.instance.model.HealthcareService.HealthcareServiceNotAvailableComponent convertHealthcareServiceNotAvailableComponent(org.hl7.fhir.dstu3.model.HealthcareService.HealthcareServiceNotAvailableComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.HealthcareService.HealthcareServiceNotAvailableComponent tgt = new org.hl7.fhir.instance.model.HealthcareService.HealthcareServiceNotAvailableComponent();
    copyElement(src, tgt);
    tgt.setDescription(src.getDescription());
    tgt.setDuring(convertPeriod(src.getDuring()));
    return tgt;
  }

//  public org.hl7.fhir.dstu3.model.ImagingObjectSelection convertImagingObjectSelection(org.hl7.fhir.instance.model.ImagingObjectSelection src) throws FHIRException {
//    if (src == null || src.isEmpty())
//      return null;
//    org.hl7.fhir.dstu3.model.ImagingObjectSelection tgt = new org.hl7.fhir.dstu3.model.ImagingObjectSelection();
//    copyDomainResource(src, tgt);
//    tgt.setUid(src.getUid());
//    tgt.setPatient(convertReference(src.getPatient()));
//    tgt.setAuthoringTime(src.getAuthoringTime());
//    tgt.setAuthor(convertReference(src.getAuthor()));
//    tgt.setTitle(convertCodeableConcept(src.getTitle()));
//    tgt.setDescription(src.getDescription());
//    for (org.hl7.fhir.instance.model.ImagingObjectSelection.StudyComponent t : src.getStudy())
//      tgt.addStudy(convertStudyComponent(t));
//    return tgt;
//  }
//
//  public org.hl7.fhir.instance.model.ImagingObjectSelection convertImagingObjectSelection(org.hl7.fhir.dstu3.model.ImagingObjectSelection src) throws FHIRException {
//    if (src == null || src.isEmpty())
//      return null;
//    org.hl7.fhir.instance.model.ImagingObjectSelection tgt = new org.hl7.fhir.instance.model.ImagingObjectSelection();
//    copyDomainResource(src, tgt);
//    tgt.setUid(src.getUid());
//    tgt.setPatient(convertReference(src.getPatient()));
//    tgt.setAuthoringTime(src.getAuthoringTime());
//    tgt.setAuthor(convertReference(src.getAuthor()));
//    tgt.setTitle(convertCodeableConcept(src.getTitle()));
//    tgt.setDescription(src.getDescription());
//    for (org.hl7.fhir.dstu3.model.ImagingObjectSelection.StudyComponent t : src.getStudy())
//      tgt.addStudy(convertStudyComponent(t));
//    return tgt;
//  }
//
//  public org.hl7.fhir.dstu3.model.ImagingObjectSelection.StudyComponent convertStudyComponent(org.hl7.fhir.instance.model.ImagingObjectSelection.StudyComponent src) throws FHIRException {
//    if (src == null || src.isEmpty())
//      return null;
//    org.hl7.fhir.dstu3.model.ImagingObjectSelection.StudyComponent tgt = new org.hl7.fhir.dstu3.model.ImagingObjectSelection.StudyComponent();
//    copyElement(src, tgt);
//    tgt.setUid(src.getUid());
//    tgt.setUrl(src.getUrl());
//    tgt.setImagingStudy(convertReference(src.getImagingStudy()));
//    for (org.hl7.fhir.instance.model.ImagingObjectSelection.SeriesComponent t : src.getSeries())
//      tgt.addSeries(convertSeriesComponent(t));
//    return tgt;
//  }
//
//  public org.hl7.fhir.instance.model.ImagingObjectSelection.StudyComponent convertStudyComponent(org.hl7.fhir.dstu3.model.ImagingObjectSelection.StudyComponent src) throws FHIRException {
//    if (src == null || src.isEmpty())
//      return null;
//    org.hl7.fhir.instance.model.ImagingObjectSelection.StudyComponent tgt = new org.hl7.fhir.instance.model.ImagingObjectSelection.StudyComponent();
//    copyElement(src, tgt);
//    tgt.setUid(src.getUid());
//    tgt.setUrl(src.getUrl());
//    tgt.setImagingStudy(convertReference(src.getImagingStudy()));
//    for (org.hl7.fhir.dstu3.model.ImagingObjectSelection.SeriesComponent t : src.getSeries())
//      tgt.addSeries(convertSeriesComponent(t));
//    return tgt;
//  }
//
//  public org.hl7.fhir.dstu3.model.ImagingObjectSelection.SeriesComponent convertSeriesComponent(org.hl7.fhir.instance.model.ImagingObjectSelection.SeriesComponent src) throws FHIRException {
//    if (src == null || src.isEmpty())
//      return null;
//    org.hl7.fhir.dstu3.model.ImagingObjectSelection.SeriesComponent tgt = new org.hl7.fhir.dstu3.model.ImagingObjectSelection.SeriesComponent();
//    copyElement(src, tgt);
//    tgt.setUid(src.getUid());
//    tgt.setUrl(src.getUrl());
//    for (org.hl7.fhir.instance.model.ImagingObjectSelection.InstanceComponent t : src.getInstance())
//      tgt.addInstance(convertInstanceComponent(t));
//    return tgt;
//  }
//
//  public org.hl7.fhir.instance.model.ImagingObjectSelection.SeriesComponent convertSeriesComponent(org.hl7.fhir.dstu3.model.ImagingObjectSelection.SeriesComponent src) throws FHIRException {
//    if (src == null || src.isEmpty())
//      return null;
//    org.hl7.fhir.instance.model.ImagingObjectSelection.SeriesComponent tgt = new org.hl7.fhir.instance.model.ImagingObjectSelection.SeriesComponent();
//    copyElement(src, tgt);
//    tgt.setUid(src.getUid());
//    tgt.setUrl(src.getUrl());
//    for (org.hl7.fhir.dstu3.model.ImagingObjectSelection.InstanceComponent t : src.getInstance())
//      tgt.addInstance(convertInstanceComponent(t));
//    return tgt;
//  }
//
//  public org.hl7.fhir.dstu3.model.ImagingObjectSelection.InstanceComponent convertInstanceComponent(org.hl7.fhir.instance.model.ImagingObjectSelection.InstanceComponent src) throws FHIRException {
//    if (src == null || src.isEmpty())
//      return null;
//    org.hl7.fhir.dstu3.model.ImagingObjectSelection.InstanceComponent tgt = new org.hl7.fhir.dstu3.model.ImagingObjectSelection.InstanceComponent();
//    copyElement(src, tgt);
//    tgt.setSopClass(src.getSopClass());
//    tgt.setUid(src.getUid());
//    tgt.setUrl(src.getUrl());
//    for (org.hl7.fhir.instance.model.ImagingObjectSelection.FramesComponent t : src.getFrames())
//      tgt.addFrame(convertFramesComponent(t));
//    return tgt;
//  }
//
//  public org.hl7.fhir.instance.model.ImagingObjectSelection.InstanceComponent convertInstanceComponent(org.hl7.fhir.dstu3.model.ImagingObjectSelection.InstanceComponent src) throws FHIRException {
//    if (src == null || src.isEmpty())
//      return null;
//    org.hl7.fhir.instance.model.ImagingObjectSelection.InstanceComponent tgt = new org.hl7.fhir.instance.model.ImagingObjectSelection.InstanceComponent();
//    copyElement(src, tgt);
//    tgt.setSopClass(src.getSopClass());
//    tgt.setUid(src.getUid());
//    tgt.setUrl(src.getUrl());
//    for (org.hl7.fhir.dstu3.model.ImagingObjectSelection.FramesComponent t : src.getFrame())
//      tgt.addFrames(convertFramesComponent(t));
//    return tgt;
//  }
//
//  public org.hl7.fhir.dstu3.model.ImagingObjectSelection.FramesComponent convertFramesComponent(org.hl7.fhir.instance.model.ImagingObjectSelection.FramesComponent src) throws FHIRException {
//    if (src == null || src.isEmpty())
//      return null;
//    org.hl7.fhir.dstu3.model.ImagingObjectSelection.FramesComponent tgt = new org.hl7.fhir.dstu3.model.ImagingObjectSelection.FramesComponent();
//    copyElement(src, tgt);
//    for (org.hl7.fhir.instance.model.UnsignedIntType t : src.getFrameNumbers())
//      tgt.addNumber(t.getValue());
//    tgt.setUrl(src.getUrl());
//    return tgt;
//  }
//
//  public org.hl7.fhir.instance.model.ImagingObjectSelection.FramesComponent convertFramesComponent(org.hl7.fhir.dstu3.model.ImagingObjectSelection.FramesComponent src) throws FHIRException {
//    if (src == null || src.isEmpty())
//      return null;
//    org.hl7.fhir.instance.model.ImagingObjectSelection.FramesComponent tgt = new org.hl7.fhir.instance.model.ImagingObjectSelection.FramesComponent();
//    copyElement(src, tgt);
//    for (org.hl7.fhir.dstu3.model.UnsignedIntType t : src.getNumber())
//      tgt.addFrameNumbers(t.getValue());
//    tgt.setUrl(src.getUrl());
//    return tgt;
//  }
//
  public org.hl7.fhir.dstu3.model.ImagingStudy convertImagingStudy(org.hl7.fhir.instance.model.ImagingStudy src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.ImagingStudy tgt = new org.hl7.fhir.dstu3.model.ImagingStudy();
    copyDomainResource(src, tgt);
    tgt.setUid(src.getUid());
    tgt.setAccession(convertIdentifier(src.getAccession()));
    for (org.hl7.fhir.instance.model.Identifier t : src.getIdentifier())
      tgt.addIdentifier(convertIdentifier(t));
    tgt.setAvailability(convertInstanceAvailability(src.getAvailability()));
    for (org.hl7.fhir.instance.model.Coding t : src.getModalityList())
      tgt.addModalityList(convertCoding(t));
    tgt.setPatient(convertReference(src.getPatient()));
    tgt.setStarted(src.getStarted());
    tgt.setReferrer(convertReference(src.getReferrer()));
    tgt.addInterpreter(convertReference(src.getInterpreter()));
    tgt.setNumberOfSeries(src.getNumberOfSeries());
    tgt.setNumberOfInstances(src.getNumberOfInstances());
    for (org.hl7.fhir.instance.model.Reference t : src.getProcedure())
      tgt.addProcedureReference(convertReference(t));
    tgt.setDescription(src.getDescription());
    for (org.hl7.fhir.instance.model.ImagingStudy.ImagingStudySeriesComponent t : src.getSeries())
      tgt.addSeries(convertImagingStudySeriesComponent(t));
    return tgt;
  }

  public org.hl7.fhir.instance.model.ImagingStudy convertImagingStudy(org.hl7.fhir.dstu3.model.ImagingStudy src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.ImagingStudy tgt = new org.hl7.fhir.instance.model.ImagingStudy();
    copyDomainResource(src, tgt);
    tgt.setUid(src.getUid());
    tgt.setAccession(convertIdentifier(src.getAccession()));
    for (org.hl7.fhir.dstu3.model.Identifier t : src.getIdentifier())
      tgt.addIdentifier(convertIdentifier(t));
    tgt.setAvailability(convertInstanceAvailability(src.getAvailability()));
    for (org.hl7.fhir.dstu3.model.Coding t : src.getModalityList())
      tgt.addModalityList(convertCoding(t));
    tgt.setPatient(convertReference(src.getPatient()));
    tgt.setStarted(src.getStarted());
    tgt.setReferrer(convertReference(src.getReferrer()));
    for (org.hl7.fhir.dstu3.model.Reference t : src.getInterpreter())
      tgt.setInterpreter(convertReference(t));
    tgt.setNumberOfSeries(src.getNumberOfSeries());
    tgt.setNumberOfInstances(src.getNumberOfInstances());
    for (org.hl7.fhir.dstu3.model.Reference t : src.getProcedureReference())
      tgt.addProcedure(convertReference(t));
    tgt.setDescription(src.getDescription());
    for (org.hl7.fhir.dstu3.model.ImagingStudy.ImagingStudySeriesComponent t : src.getSeries())
      tgt.addSeries(convertImagingStudySeriesComponent(t));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.ImagingStudy.InstanceAvailability convertInstanceAvailability(org.hl7.fhir.instance.model.ImagingStudy.InstanceAvailability src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case ONLINE: return org.hl7.fhir.dstu3.model.ImagingStudy.InstanceAvailability.ONLINE;
    case OFFLINE: return org.hl7.fhir.dstu3.model.ImagingStudy.InstanceAvailability.OFFLINE;
    case NEARLINE: return org.hl7.fhir.dstu3.model.ImagingStudy.InstanceAvailability.NEARLINE;
    case UNAVAILABLE: return org.hl7.fhir.dstu3.model.ImagingStudy.InstanceAvailability.UNAVAILABLE;
    default: return org.hl7.fhir.dstu3.model.ImagingStudy.InstanceAvailability.NULL;
    }
  }

  public org.hl7.fhir.instance.model.ImagingStudy.InstanceAvailability convertInstanceAvailability(org.hl7.fhir.dstu3.model.ImagingStudy.InstanceAvailability src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case ONLINE: return org.hl7.fhir.instance.model.ImagingStudy.InstanceAvailability.ONLINE;
    case OFFLINE: return org.hl7.fhir.instance.model.ImagingStudy.InstanceAvailability.OFFLINE;
    case NEARLINE: return org.hl7.fhir.instance.model.ImagingStudy.InstanceAvailability.NEARLINE;
    case UNAVAILABLE: return org.hl7.fhir.instance.model.ImagingStudy.InstanceAvailability.UNAVAILABLE;
    default: return org.hl7.fhir.instance.model.ImagingStudy.InstanceAvailability.NULL;
    }
  }

  public org.hl7.fhir.dstu3.model.ImagingStudy.ImagingStudySeriesComponent convertImagingStudySeriesComponent(org.hl7.fhir.instance.model.ImagingStudy.ImagingStudySeriesComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.ImagingStudy.ImagingStudySeriesComponent tgt = new org.hl7.fhir.dstu3.model.ImagingStudy.ImagingStudySeriesComponent();
    copyElement(src, tgt);
    tgt.setUid(src.getUid());
    tgt.setNumber(src.getNumber());
    tgt.setModality(convertCoding(src.getModality()));
    tgt.setDescription(src.getDescription());
    tgt.setNumberOfInstances(src.getNumberOfInstances());
    tgt.setAvailability(convertInstanceAvailability(src.getAvailability()));
    tgt.setBodySite(convertCoding(src.getBodySite()));
    tgt.setLaterality(convertCoding(src.getLaterality()));
    tgt.setStarted(src.getStarted());
    for (org.hl7.fhir.instance.model.ImagingStudy.ImagingStudySeriesInstanceComponent t : src.getInstance())
      tgt.addInstance(convertImagingStudySeriesInstanceComponent(t));
    return tgt;
  }

  public org.hl7.fhir.instance.model.ImagingStudy.ImagingStudySeriesComponent convertImagingStudySeriesComponent(org.hl7.fhir.dstu3.model.ImagingStudy.ImagingStudySeriesComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.ImagingStudy.ImagingStudySeriesComponent tgt = new org.hl7.fhir.instance.model.ImagingStudy.ImagingStudySeriesComponent();
    copyElement(src, tgt);
    tgt.setUid(src.getUid());
    tgt.setNumber(src.getNumber());
    tgt.setModality(convertCoding(src.getModality()));
    tgt.setDescription(src.getDescription());
    tgt.setNumberOfInstances(src.getNumberOfInstances());
    tgt.setAvailability(convertInstanceAvailability(src.getAvailability()));
    tgt.setBodySite(convertCoding(src.getBodySite()));
    tgt.setLaterality(convertCoding(src.getLaterality()));
    tgt.setStarted(src.getStarted());
    for (org.hl7.fhir.dstu3.model.ImagingStudy.ImagingStudySeriesInstanceComponent t : src.getInstance())
      tgt.addInstance(convertImagingStudySeriesInstanceComponent(t));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.ImagingStudy.ImagingStudySeriesInstanceComponent convertImagingStudySeriesInstanceComponent(org.hl7.fhir.instance.model.ImagingStudy.ImagingStudySeriesInstanceComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.ImagingStudy.ImagingStudySeriesInstanceComponent tgt = new org.hl7.fhir.dstu3.model.ImagingStudy.ImagingStudySeriesInstanceComponent();
    copyElement(src, tgt);
    tgt.setUid(src.getUid());
    tgt.setNumber(src.getNumber());
    tgt.setSopClass(src.getSopClass());
    tgt.setTitle(src.getTitle());
    return tgt;
  }

  public org.hl7.fhir.instance.model.ImagingStudy.ImagingStudySeriesInstanceComponent convertImagingStudySeriesInstanceComponent(org.hl7.fhir.dstu3.model.ImagingStudy.ImagingStudySeriesInstanceComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.ImagingStudy.ImagingStudySeriesInstanceComponent tgt = new org.hl7.fhir.instance.model.ImagingStudy.ImagingStudySeriesInstanceComponent();
    copyElement(src, tgt);
    tgt.setUid(src.getUid());
    tgt.setNumber(src.getNumber());
    tgt.setSopClass(src.getSopClass());
    tgt.setTitle(src.getTitle());
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.Immunization convertImmunization(org.hl7.fhir.instance.model.Immunization src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.Immunization tgt = new org.hl7.fhir.dstu3.model.Immunization();
    copyDomainResource(src, tgt);
    for (org.hl7.fhir.instance.model.Identifier t : src.getIdentifier())
      tgt.addIdentifier(convertIdentifier(t));
    try {
      tgt.setStatus(org.hl7.fhir.dstu3.model.Immunization.ImmunizationStatus.fromCode(src.getStatus()));
    } catch (org.hl7.fhir.exceptions.FHIRException e) {
      throw new FHIRException(e);
    }
    if (src.hasDate())
      tgt.setDate(src.getDate());
    tgt.setVaccineCode(convertCodeableConcept(src.getVaccineCode()));
    tgt.setPatient(convertReference(src.getPatient()));
    tgt.setNotGiven(src.getWasNotGiven());
    tgt.setPrimarySource(!src.getReported());
    if (src.hasPerformer())
      tgt.addPractitioner().setActor(convertReference(src.getPerformer())).setRole(new org.hl7.fhir.dstu3.model.CodeableConcept().addCoding(new Coding().setSystem("http://hl7.org/fhir/v2/0443").setCode("AP")));
    if (src.hasRequester())
      tgt.addPractitioner().setActor(convertReference(src.getRequester())).setRole(new org.hl7.fhir.dstu3.model.CodeableConcept().addCoding(new Coding().setSystem("http://hl7.org/fhir/v2/0443").setCode("OP")));
    tgt.setEncounter(convertReference(src.getEncounter()));
    tgt.setManufacturer(convertReference(src.getManufacturer()));
    tgt.setLocation(convertReference(src.getLocation()));
    tgt.setLotNumber(src.getLotNumber());
    tgt.setExpirationDate(src.getExpirationDate());
    tgt.setSite(convertCodeableConcept(src.getSite()));
    tgt.setRoute(convertCodeableConcept(src.getRoute()));
    tgt.setDoseQuantity(convertSimpleQuantity(src.getDoseQuantity()));
    for (org.hl7.fhir.instance.model.Annotation t : src.getNote())
      tgt.addNote(convertAnnotation(t));
    tgt.setExplanation(convertImmunizationExplanationComponent(src.getExplanation()));
    for (org.hl7.fhir.instance.model.Immunization.ImmunizationReactionComponent t : src.getReaction())
      tgt.addReaction(convertImmunizationReactionComponent(t));
    for (org.hl7.fhir.instance.model.Immunization.ImmunizationVaccinationProtocolComponent t : src.getVaccinationProtocol())
      tgt.addVaccinationProtocol(convertImmunizationVaccinationProtocolComponent(t));
    return tgt;
  }

  public org.hl7.fhir.instance.model.Immunization convertImmunization(org.hl7.fhir.dstu3.model.Immunization src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.Immunization tgt = new org.hl7.fhir.instance.model.Immunization();
    copyDomainResource(src, tgt);
    for (org.hl7.fhir.dstu3.model.Identifier t : src.getIdentifier())
      tgt.addIdentifier(convertIdentifier(t));
    tgt.setStatus(src.getStatus().toCode());
    if (src.hasDate())
      tgt.setDate(src.getDate());
    tgt.setVaccineCode(convertCodeableConcept(src.getVaccineCode()));
    tgt.setPatient(convertReference(src.getPatient()));
    tgt.setWasNotGiven(src.getNotGiven());
    tgt.setReported(!src.getPrimarySource());
    tgt.setPerformer(convertReference(getPerformer(src.getPractitioner())));
    tgt.setRequester(convertReference(getRequester(src.getPractitioner())));
    tgt.setEncounter(convertReference(src.getEncounter()));
    tgt.setManufacturer(convertReference(src.getManufacturer()));
    tgt.setLocation(convertReference(src.getLocation()));
    tgt.setLotNumber(src.getLotNumber());
    tgt.setExpirationDate(src.getExpirationDate());
    tgt.setSite(convertCodeableConcept(src.getSite()));
    tgt.setRoute(convertCodeableConcept(src.getRoute()));
    tgt.setDoseQuantity(convertSimpleQuantity(src.getDoseQuantity()));
    for (org.hl7.fhir.dstu3.model.Annotation t : src.getNote())
      tgt.addNote(convertAnnotation(t));
    tgt.setExplanation(convertImmunizationExplanationComponent(src.getExplanation()));
    for (org.hl7.fhir.dstu3.model.Immunization.ImmunizationReactionComponent t : src.getReaction())
      tgt.addReaction(convertImmunizationReactionComponent(t));
    for (org.hl7.fhir.dstu3.model.Immunization.ImmunizationVaccinationProtocolComponent t : src.getVaccinationProtocol())
      tgt.addVaccinationProtocol(convertImmunizationVaccinationProtocolComponent(t));
    return tgt;
  }

  private org.hl7.fhir.dstu3.model.Reference getPerformer(List<ImmunizationPractitionerComponent> practitioner) {
    for (ImmunizationPractitionerComponent p : practitioner) {
      if (hasConcept(p.getRole(), "http://hl7.org/fhir/v2/0443", "AP"))
        return p.getActor();
    }
    return null;
  }

  private org.hl7.fhir.dstu3.model.Reference getRequester(List<ImmunizationPractitionerComponent> practitioner) {
    for (ImmunizationPractitionerComponent p : practitioner) {
      if (hasConcept(p.getRole(), "http://hl7.org/fhir/v2/0443", "OP"))
        return p.getActor();
    }
    return null;
  }

  private boolean hasConcept(org.hl7.fhir.dstu3.model.CodeableConcept cc, String system, String code) {
    for (org.hl7.fhir.dstu3.model.Coding c : cc.getCoding()) {
      if (system.equals(c.getSystem()) && code.equals(c.getCode()))
        return true;
    }
    return false;
  }

  private boolean hasConcept(org.hl7.fhir.instance.model.CodeableConcept cc, String system, String code) {
    for (org.hl7.fhir.instance.model.Coding c : cc.getCoding()) {
      if (system.equals(c.getSystem()) && code.equals(c.getCode()))
        return true;
    }
    return false;
  }

  public org.hl7.fhir.dstu3.model.Immunization.ImmunizationExplanationComponent convertImmunizationExplanationComponent(org.hl7.fhir.instance.model.Immunization.ImmunizationExplanationComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.Immunization.ImmunizationExplanationComponent tgt = new org.hl7.fhir.dstu3.model.Immunization.ImmunizationExplanationComponent();
    copyElement(src, tgt);
    for (org.hl7.fhir.instance.model.CodeableConcept t : src.getReason())
      tgt.addReason(convertCodeableConcept(t));
    for (org.hl7.fhir.instance.model.CodeableConcept t : src.getReasonNotGiven())
      tgt.addReasonNotGiven(convertCodeableConcept(t));
    return tgt;
  }

  public org.hl7.fhir.instance.model.Immunization.ImmunizationExplanationComponent convertImmunizationExplanationComponent(org.hl7.fhir.dstu3.model.Immunization.ImmunizationExplanationComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.Immunization.ImmunizationExplanationComponent tgt = new org.hl7.fhir.instance.model.Immunization.ImmunizationExplanationComponent();
    copyElement(src, tgt);
    for (org.hl7.fhir.dstu3.model.CodeableConcept t : src.getReason())
      tgt.addReason(convertCodeableConcept(t));
    for (org.hl7.fhir.dstu3.model.CodeableConcept t : src.getReasonNotGiven())
      tgt.addReasonNotGiven(convertCodeableConcept(t));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.Immunization.ImmunizationReactionComponent convertImmunizationReactionComponent(org.hl7.fhir.instance.model.Immunization.ImmunizationReactionComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.Immunization.ImmunizationReactionComponent tgt = new org.hl7.fhir.dstu3.model.Immunization.ImmunizationReactionComponent();
    copyElement(src, tgt);
    if (src.hasDate())
      tgt.setDate(src.getDate());
    tgt.setDetail(convertReference(src.getDetail()));
    tgt.setReported(src.getReported());
    return tgt;
  }

  public org.hl7.fhir.instance.model.Immunization.ImmunizationReactionComponent convertImmunizationReactionComponent(org.hl7.fhir.dstu3.model.Immunization.ImmunizationReactionComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.Immunization.ImmunizationReactionComponent tgt = new org.hl7.fhir.instance.model.Immunization.ImmunizationReactionComponent();
    copyElement(src, tgt);
    if (src.hasDate())
      tgt.setDate(src.getDate());
    tgt.setDetail(convertReference(src.getDetail()));
    tgt.setReported(src.getReported());
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.Immunization.ImmunizationVaccinationProtocolComponent convertImmunizationVaccinationProtocolComponent(org.hl7.fhir.instance.model.Immunization.ImmunizationVaccinationProtocolComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.Immunization.ImmunizationVaccinationProtocolComponent tgt = new org.hl7.fhir.dstu3.model.Immunization.ImmunizationVaccinationProtocolComponent();
    copyElement(src, tgt);
    tgt.setDoseSequence(src.getDoseSequence());
    tgt.setDescription(src.getDescription());
    tgt.setAuthority(convertReference(src.getAuthority()));
    tgt.setSeries(src.getSeries());
    tgt.setSeriesDoses(src.getSeriesDoses());
    for (org.hl7.fhir.instance.model.CodeableConcept t : src.getTargetDisease())
      tgt.addTargetDisease(convertCodeableConcept(t));
    tgt.setDoseStatus(convertCodeableConcept(src.getDoseStatus()));
    tgt.setDoseStatusReason(convertCodeableConcept(src.getDoseStatusReason()));
    return tgt;
  }

  public org.hl7.fhir.instance.model.Immunization.ImmunizationVaccinationProtocolComponent convertImmunizationVaccinationProtocolComponent(org.hl7.fhir.dstu3.model.Immunization.ImmunizationVaccinationProtocolComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.Immunization.ImmunizationVaccinationProtocolComponent tgt = new org.hl7.fhir.instance.model.Immunization.ImmunizationVaccinationProtocolComponent();
    copyElement(src, tgt);
    tgt.setDoseSequence(src.getDoseSequence());
    tgt.setDescription(src.getDescription());
    tgt.setAuthority(convertReference(src.getAuthority()));
    tgt.setSeries(src.getSeries());
    tgt.setSeriesDoses(src.getSeriesDoses());
    for (org.hl7.fhir.dstu3.model.CodeableConcept t : src.getTargetDisease())
      tgt.addTargetDisease(convertCodeableConcept(t));
    tgt.setDoseStatus(convertCodeableConcept(src.getDoseStatus()));
    tgt.setDoseStatusReason(convertCodeableConcept(src.getDoseStatusReason()));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.ImmunizationRecommendation convertImmunizationRecommendation(org.hl7.fhir.instance.model.ImmunizationRecommendation src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.ImmunizationRecommendation tgt = new org.hl7.fhir.dstu3.model.ImmunizationRecommendation();
    copyDomainResource(src, tgt);
    for (org.hl7.fhir.instance.model.Identifier t : src.getIdentifier())
      tgt.addIdentifier(convertIdentifier(t));
    tgt.setPatient(convertReference(src.getPatient()));
    for (org.hl7.fhir.instance.model.ImmunizationRecommendation.ImmunizationRecommendationRecommendationComponent t : src.getRecommendation())
      tgt.addRecommendation(convertImmunizationRecommendationRecommendationComponent(t));
    return tgt;
  }

  public org.hl7.fhir.instance.model.ImmunizationRecommendation convertImmunizationRecommendation(org.hl7.fhir.dstu3.model.ImmunizationRecommendation src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.ImmunizationRecommendation tgt = new org.hl7.fhir.instance.model.ImmunizationRecommendation();
    copyDomainResource(src, tgt);
    for (org.hl7.fhir.dstu3.model.Identifier t : src.getIdentifier())
      tgt.addIdentifier(convertIdentifier(t));
    tgt.setPatient(convertReference(src.getPatient()));
    for (org.hl7.fhir.dstu3.model.ImmunizationRecommendation.ImmunizationRecommendationRecommendationComponent t : src.getRecommendation())
      tgt.addRecommendation(convertImmunizationRecommendationRecommendationComponent(t));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.ImmunizationRecommendation.ImmunizationRecommendationRecommendationComponent convertImmunizationRecommendationRecommendationComponent(org.hl7.fhir.instance.model.ImmunizationRecommendation.ImmunizationRecommendationRecommendationComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.ImmunizationRecommendation.ImmunizationRecommendationRecommendationComponent tgt = new org.hl7.fhir.dstu3.model.ImmunizationRecommendation.ImmunizationRecommendationRecommendationComponent();
    copyElement(src, tgt);
    if (src.hasDate())
      tgt.setDate(src.getDate());
    tgt.setVaccineCode(convertCodeableConcept(src.getVaccineCode()));
    tgt.setDoseNumber(src.getDoseNumber());
    tgt.setForecastStatus(convertCodeableConcept(src.getForecastStatus()));
    for (org.hl7.fhir.instance.model.ImmunizationRecommendation.ImmunizationRecommendationRecommendationDateCriterionComponent t : src.getDateCriterion())
      tgt.addDateCriterion(convertImmunizationRecommendationRecommendationDateCriterionComponent(t));
    tgt.setProtocol(convertImmunizationRecommendationRecommendationProtocolComponent(src.getProtocol()));
    for (org.hl7.fhir.instance.model.Reference t : src.getSupportingImmunization())
      tgt.addSupportingImmunization(convertReference(t));
    for (org.hl7.fhir.instance.model.Reference t : src.getSupportingPatientInformation())
      tgt.addSupportingPatientInformation(convertReference(t));
    return tgt;
  }

  public org.hl7.fhir.instance.model.ImmunizationRecommendation.ImmunizationRecommendationRecommendationComponent convertImmunizationRecommendationRecommendationComponent(org.hl7.fhir.dstu3.model.ImmunizationRecommendation.ImmunizationRecommendationRecommendationComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.ImmunizationRecommendation.ImmunizationRecommendationRecommendationComponent tgt = new org.hl7.fhir.instance.model.ImmunizationRecommendation.ImmunizationRecommendationRecommendationComponent();
    copyElement(src, tgt);
    if (src.hasDate())
      tgt.setDate(src.getDate());
    tgt.setVaccineCode(convertCodeableConcept(src.getVaccineCode()));
    tgt.setDoseNumber(src.getDoseNumber());
    tgt.setForecastStatus(convertCodeableConcept(src.getForecastStatus()));
    for (org.hl7.fhir.dstu3.model.ImmunizationRecommendation.ImmunizationRecommendationRecommendationDateCriterionComponent t : src.getDateCriterion())
      tgt.addDateCriterion(convertImmunizationRecommendationRecommendationDateCriterionComponent(t));
    tgt.setProtocol(convertImmunizationRecommendationRecommendationProtocolComponent(src.getProtocol()));
    for (org.hl7.fhir.dstu3.model.Reference t : src.getSupportingImmunization())
      tgt.addSupportingImmunization(convertReference(t));
    for (org.hl7.fhir.dstu3.model.Reference t : src.getSupportingPatientInformation())
      tgt.addSupportingPatientInformation(convertReference(t));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.ImmunizationRecommendation.ImmunizationRecommendationRecommendationDateCriterionComponent convertImmunizationRecommendationRecommendationDateCriterionComponent(org.hl7.fhir.instance.model.ImmunizationRecommendation.ImmunizationRecommendationRecommendationDateCriterionComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.ImmunizationRecommendation.ImmunizationRecommendationRecommendationDateCriterionComponent tgt = new org.hl7.fhir.dstu3.model.ImmunizationRecommendation.ImmunizationRecommendationRecommendationDateCriterionComponent();
    copyElement(src, tgt);
    tgt.setCode(convertCodeableConcept(src.getCode()));
    tgt.setValue(src.getValue());
    return tgt;
  }

  public org.hl7.fhir.instance.model.ImmunizationRecommendation.ImmunizationRecommendationRecommendationDateCriterionComponent convertImmunizationRecommendationRecommendationDateCriterionComponent(org.hl7.fhir.dstu3.model.ImmunizationRecommendation.ImmunizationRecommendationRecommendationDateCriterionComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.ImmunizationRecommendation.ImmunizationRecommendationRecommendationDateCriterionComponent tgt = new org.hl7.fhir.instance.model.ImmunizationRecommendation.ImmunizationRecommendationRecommendationDateCriterionComponent();
    copyElement(src, tgt);
    tgt.setCode(convertCodeableConcept(src.getCode()));
    tgt.setValue(src.getValue());
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.ImmunizationRecommendation.ImmunizationRecommendationRecommendationProtocolComponent convertImmunizationRecommendationRecommendationProtocolComponent(org.hl7.fhir.instance.model.ImmunizationRecommendation.ImmunizationRecommendationRecommendationProtocolComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.ImmunizationRecommendation.ImmunizationRecommendationRecommendationProtocolComponent tgt = new org.hl7.fhir.dstu3.model.ImmunizationRecommendation.ImmunizationRecommendationRecommendationProtocolComponent();
    copyElement(src, tgt);
    tgt.setDoseSequence(src.getDoseSequence());
    tgt.setDescription(src.getDescription());
    tgt.setAuthority(convertReference(src.getAuthority()));
    tgt.setSeries(src.getSeries());
    return tgt;
  }

  public org.hl7.fhir.instance.model.ImmunizationRecommendation.ImmunizationRecommendationRecommendationProtocolComponent convertImmunizationRecommendationRecommendationProtocolComponent(org.hl7.fhir.dstu3.model.ImmunizationRecommendation.ImmunizationRecommendationRecommendationProtocolComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.ImmunizationRecommendation.ImmunizationRecommendationRecommendationProtocolComponent tgt = new org.hl7.fhir.instance.model.ImmunizationRecommendation.ImmunizationRecommendationRecommendationProtocolComponent();
    copyElement(src, tgt);
    tgt.setDoseSequence(src.getDoseSequence());
    tgt.setDescription(src.getDescription());
    tgt.setAuthority(convertReference(src.getAuthority()));
    tgt.setSeries(src.getSeries());
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.ImplementationGuide convertImplementationGuide(org.hl7.fhir.instance.model.ImplementationGuide src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.ImplementationGuide tgt = new org.hl7.fhir.dstu3.model.ImplementationGuide();
    copyDomainResource(src, tgt);
    tgt.setUrl(src.getUrl());
    tgt.setVersion(src.getVersion());
    tgt.setName(src.getName());
    tgt.setStatus(convertConformanceResourceStatus(src.getStatus()));
    if (src.hasExperimental())
      tgt.setExperimental(src.getExperimental());
    tgt.setPublisher(src.getPublisher());
    for (org.hl7.fhir.instance.model.ImplementationGuide.ImplementationGuideContactComponent t : src.getContact())
      tgt.addContact(convertImplementationGuideContactComponent(t));
    if (src.hasDate())
      tgt.setDate(src.getDate());
    tgt.setDescription(src.getDescription());
    for (org.hl7.fhir.instance.model.CodeableConcept t : src.getUseContext())
      if (isJurisdiction(t))
        tgt.addJurisdiction(convertCodeableConcept(t));
      else
        tgt.addUseContext(convertCodeableConceptToUsageContext(t));
    tgt.setCopyright(src.getCopyright());
    tgt.setFhirVersion(src.getFhirVersion());
    for (org.hl7.fhir.instance.model.ImplementationGuide.ImplementationGuideDependencyComponent t : src.getDependency())
      tgt.addDependency(convertImplementationGuideDependencyComponent(t));
    for (org.hl7.fhir.instance.model.ImplementationGuide.ImplementationGuidePackageComponent t : src.getPackage())
      tgt.addPackage(convertImplementationGuidePackageComponent(t));
    for (org.hl7.fhir.instance.model.ImplementationGuide.ImplementationGuideGlobalComponent t : src.getGlobal())
      tgt.addGlobal(convertImplementationGuideGlobalComponent(t));
    for (org.hl7.fhir.instance.model.UriType t : src.getBinary())
      tgt.addBinary(t.getValue());
    tgt.setPage(convertImplementationGuidePageComponent(src.getPage()));
    return tgt;
  }

  public org.hl7.fhir.instance.model.ImplementationGuide convertImplementationGuide(org.hl7.fhir.dstu3.model.ImplementationGuide src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.ImplementationGuide tgt = new org.hl7.fhir.instance.model.ImplementationGuide();
    copyDomainResource(src, tgt);
    tgt.setUrl(src.getUrl());
    tgt.setVersion(src.getVersion());
    tgt.setName(src.getName());
    tgt.setStatus(convertConformanceResourceStatus(src.getStatus()));
    if (src.hasExperimental())
      tgt.setExperimental(src.getExperimental());
    tgt.setPublisher(src.getPublisher());
    for (org.hl7.fhir.dstu3.model.ContactDetail t : src.getContact())
      tgt.addContact(convertImplementationGuideContactComponent(t));
    if (src.hasDate())
      tgt.setDate(src.getDate());
    tgt.setDescription(src.getDescription());
    for (org.hl7.fhir.dstu3.model.UsageContext t : src.getUseContext())
      if (t.hasValueCodeableConcept())
        tgt.addUseContext(convertCodeableConcept(t.getValueCodeableConcept()));
    for (org.hl7.fhir.dstu3.model.CodeableConcept t : src.getJurisdiction())
        tgt.addUseContext(convertCodeableConcept(t));
    tgt.setCopyright(src.getCopyright());
    tgt.setFhirVersion(src.getFhirVersion());
    for (org.hl7.fhir.dstu3.model.ImplementationGuide.ImplementationGuideDependencyComponent t : src.getDependency())
      tgt.addDependency(convertImplementationGuideDependencyComponent(t));
    for (org.hl7.fhir.dstu3.model.ImplementationGuide.ImplementationGuidePackageComponent t : src.getPackage())
      tgt.addPackage(convertImplementationGuidePackageComponent(t));
    for (org.hl7.fhir.dstu3.model.ImplementationGuide.ImplementationGuideGlobalComponent t : src.getGlobal())
      tgt.addGlobal(convertImplementationGuideGlobalComponent(t));
    for (org.hl7.fhir.dstu3.model.UriType t : src.getBinary())
      tgt.addBinary(t.getValue());
    tgt.setPage(convertImplementationGuidePageComponent(src.getPage()));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.ContactDetail convertImplementationGuideContactComponent(org.hl7.fhir.instance.model.ImplementationGuide.ImplementationGuideContactComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.ContactDetail tgt = new org.hl7.fhir.dstu3.model.ContactDetail();
    copyElement(src, tgt);
    tgt.setName(src.getName());
    for (org.hl7.fhir.instance.model.ContactPoint t : src.getTelecom())
      tgt.addTelecom(convertContactPoint(t));
    return tgt;
  }

  public org.hl7.fhir.instance.model.ImplementationGuide.ImplementationGuideContactComponent convertImplementationGuideContactComponent(org.hl7.fhir.dstu3.model.ContactDetail src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.ImplementationGuide.ImplementationGuideContactComponent tgt = new org.hl7.fhir.instance.model.ImplementationGuide.ImplementationGuideContactComponent();
    copyElement(src, tgt);
    tgt.setName(src.getName());
    for (org.hl7.fhir.dstu3.model.ContactPoint t : src.getTelecom())
      tgt.addTelecom(convertContactPoint(t));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.ImplementationGuide.ImplementationGuideDependencyComponent convertImplementationGuideDependencyComponent(org.hl7.fhir.instance.model.ImplementationGuide.ImplementationGuideDependencyComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.ImplementationGuide.ImplementationGuideDependencyComponent tgt = new org.hl7.fhir.dstu3.model.ImplementationGuide.ImplementationGuideDependencyComponent();
    copyElement(src, tgt);
    tgt.setType(convertGuideDependencyType(src.getType()));
    tgt.setUri(src.getUri());
    return tgt;
  }

  public org.hl7.fhir.instance.model.ImplementationGuide.ImplementationGuideDependencyComponent convertImplementationGuideDependencyComponent(org.hl7.fhir.dstu3.model.ImplementationGuide.ImplementationGuideDependencyComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.ImplementationGuide.ImplementationGuideDependencyComponent tgt = new org.hl7.fhir.instance.model.ImplementationGuide.ImplementationGuideDependencyComponent();
    copyElement(src, tgt);
    tgt.setType(convertGuideDependencyType(src.getType()));
    tgt.setUri(src.getUri());
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.ImplementationGuide.GuideDependencyType convertGuideDependencyType(org.hl7.fhir.instance.model.ImplementationGuide.GuideDependencyType src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case REFERENCE: return org.hl7.fhir.dstu3.model.ImplementationGuide.GuideDependencyType.REFERENCE;
    case INCLUSION: return org.hl7.fhir.dstu3.model.ImplementationGuide.GuideDependencyType.INCLUSION;
    default: return org.hl7.fhir.dstu3.model.ImplementationGuide.GuideDependencyType.NULL;
    }
  }

  public org.hl7.fhir.instance.model.ImplementationGuide.GuideDependencyType convertGuideDependencyType(org.hl7.fhir.dstu3.model.ImplementationGuide.GuideDependencyType src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case REFERENCE: return org.hl7.fhir.instance.model.ImplementationGuide.GuideDependencyType.REFERENCE;
    case INCLUSION: return org.hl7.fhir.instance.model.ImplementationGuide.GuideDependencyType.INCLUSION;
    default: return org.hl7.fhir.instance.model.ImplementationGuide.GuideDependencyType.NULL;
    }
  }

  public org.hl7.fhir.dstu3.model.ImplementationGuide.ImplementationGuidePackageComponent convertImplementationGuidePackageComponent(org.hl7.fhir.instance.model.ImplementationGuide.ImplementationGuidePackageComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.ImplementationGuide.ImplementationGuidePackageComponent tgt = new org.hl7.fhir.dstu3.model.ImplementationGuide.ImplementationGuidePackageComponent();
    copyElement(src, tgt);
    tgt.setName(src.getName());
    tgt.setDescription(src.getDescription());
    for (org.hl7.fhir.instance.model.ImplementationGuide.ImplementationGuidePackageResourceComponent t : src.getResource())
      tgt.addResource(convertImplementationGuidePackageResourceComponent(t));
    return tgt;
  }

  public org.hl7.fhir.instance.model.ImplementationGuide.ImplementationGuidePackageComponent convertImplementationGuidePackageComponent(org.hl7.fhir.dstu3.model.ImplementationGuide.ImplementationGuidePackageComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.ImplementationGuide.ImplementationGuidePackageComponent tgt = new org.hl7.fhir.instance.model.ImplementationGuide.ImplementationGuidePackageComponent();
    copyElement(src, tgt);
    tgt.setName(src.getName());
    tgt.setDescription(src.getDescription());
    for (org.hl7.fhir.dstu3.model.ImplementationGuide.ImplementationGuidePackageResourceComponent t : src.getResource())
      tgt.addResource(convertImplementationGuidePackageResourceComponent(t));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.ImplementationGuide.ImplementationGuidePackageResourceComponent convertImplementationGuidePackageResourceComponent(org.hl7.fhir.instance.model.ImplementationGuide.ImplementationGuidePackageResourceComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.ImplementationGuide.ImplementationGuidePackageResourceComponent tgt = new org.hl7.fhir.dstu3.model.ImplementationGuide.ImplementationGuidePackageResourceComponent();
    copyElement(src, tgt);
    tgt.setExample(src.getPurpose() == org.hl7.fhir.instance.model.ImplementationGuide.GuideResourcePurpose.EXAMPLE);
    tgt.setName(src.getName());
    tgt.setDescription(src.getDescription());
    tgt.setAcronym(src.getAcronym());
    tgt.setSource(convertType(src.getSource()));
    tgt.setExampleFor(convertReference(src.getExampleFor()));
    return tgt;
  }

  public org.hl7.fhir.instance.model.ImplementationGuide.ImplementationGuidePackageResourceComponent convertImplementationGuidePackageResourceComponent(org.hl7.fhir.dstu3.model.ImplementationGuide.ImplementationGuidePackageResourceComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.ImplementationGuide.ImplementationGuidePackageResourceComponent tgt = new org.hl7.fhir.instance.model.ImplementationGuide.ImplementationGuidePackageResourceComponent();
    copyElement(src, tgt);
    if (src.getExample())
      tgt.setPurpose(org.hl7.fhir.instance.model.ImplementationGuide.GuideResourcePurpose.EXAMPLE);
    else
      tgt.setPurpose(org.hl7.fhir.instance.model.ImplementationGuide.GuideResourcePurpose.PROFILE);
    tgt.setName(src.getName());
    tgt.setDescription(src.getDescription());
    tgt.setAcronym(src.getAcronym());
    tgt.setSource(convertType(src.getSource()));
    tgt.setExampleFor(convertReference(src.getExampleFor()));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.ImplementationGuide.ImplementationGuideGlobalComponent convertImplementationGuideGlobalComponent(org.hl7.fhir.instance.model.ImplementationGuide.ImplementationGuideGlobalComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.ImplementationGuide.ImplementationGuideGlobalComponent tgt = new org.hl7.fhir.dstu3.model.ImplementationGuide.ImplementationGuideGlobalComponent();
    copyElement(src, tgt);
    tgt.setType(src.getType());
    tgt.setProfile(convertReference(src.getProfile()));
    return tgt;
  }

  public org.hl7.fhir.instance.model.ImplementationGuide.ImplementationGuideGlobalComponent convertImplementationGuideGlobalComponent(org.hl7.fhir.dstu3.model.ImplementationGuide.ImplementationGuideGlobalComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.ImplementationGuide.ImplementationGuideGlobalComponent tgt = new org.hl7.fhir.instance.model.ImplementationGuide.ImplementationGuideGlobalComponent();
    copyElement(src, tgt);
    tgt.setType(src.getType());
    tgt.setProfile(convertReference(src.getProfile()));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.ImplementationGuide.ImplementationGuidePageComponent convertImplementationGuidePageComponent(org.hl7.fhir.instance.model.ImplementationGuide.ImplementationGuidePageComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.ImplementationGuide.ImplementationGuidePageComponent tgt = new org.hl7.fhir.dstu3.model.ImplementationGuide.ImplementationGuidePageComponent();
    copyElement(src, tgt);
    tgt.setSource(src.getSource());
    tgt.setTitle(src.getName());
    tgt.setKind(convertGuidePageKind(src.getKind()));
    for (org.hl7.fhir.instance.model.CodeType t : src.getType())
      tgt.addType(t.getValue());
    for (org.hl7.fhir.instance.model.StringType t : src.getPackage())
      tgt.addPackage(t.getValue());
    tgt.setFormat(src.getFormat());
    for (org.hl7.fhir.instance.model.ImplementationGuide.ImplementationGuidePageComponent t : src.getPage())
      tgt.addPage(convertImplementationGuidePageComponent(t));
    return tgt;
  }

  public org.hl7.fhir.instance.model.ImplementationGuide.ImplementationGuidePageComponent convertImplementationGuidePageComponent(org.hl7.fhir.dstu3.model.ImplementationGuide.ImplementationGuidePageComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.ImplementationGuide.ImplementationGuidePageComponent tgt = new org.hl7.fhir.instance.model.ImplementationGuide.ImplementationGuidePageComponent();
    copyElement(src, tgt);
    tgt.setSource(src.getSource());
    tgt.setName(src.getTitle());
    tgt.setKind(convertGuidePageKind(src.getKind()));
    for (org.hl7.fhir.dstu3.model.CodeType t : src.getType())
      tgt.addType(t.getValue());
    for (org.hl7.fhir.dstu3.model.StringType t : src.getPackage())
      tgt.addPackage(t.getValue());
    tgt.setFormat(src.getFormat());
    for (org.hl7.fhir.dstu3.model.ImplementationGuide.ImplementationGuidePageComponent t : src.getPage())
      tgt.addPage(convertImplementationGuidePageComponent(t));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.ImplementationGuide.GuidePageKind convertGuidePageKind(org.hl7.fhir.instance.model.ImplementationGuide.GuidePageKind src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case PAGE: return org.hl7.fhir.dstu3.model.ImplementationGuide.GuidePageKind.PAGE;
    case EXAMPLE: return org.hl7.fhir.dstu3.model.ImplementationGuide.GuidePageKind.EXAMPLE;
    case LIST: return org.hl7.fhir.dstu3.model.ImplementationGuide.GuidePageKind.LIST;
    case INCLUDE: return org.hl7.fhir.dstu3.model.ImplementationGuide.GuidePageKind.INCLUDE;
    case DIRECTORY: return org.hl7.fhir.dstu3.model.ImplementationGuide.GuidePageKind.DIRECTORY;
    case DICTIONARY: return org.hl7.fhir.dstu3.model.ImplementationGuide.GuidePageKind.DICTIONARY;
    case TOC: return org.hl7.fhir.dstu3.model.ImplementationGuide.GuidePageKind.TOC;
    case RESOURCE: return org.hl7.fhir.dstu3.model.ImplementationGuide.GuidePageKind.RESOURCE;
    default: return org.hl7.fhir.dstu3.model.ImplementationGuide.GuidePageKind.NULL;
    }
  }

  public org.hl7.fhir.instance.model.ImplementationGuide.GuidePageKind convertGuidePageKind(org.hl7.fhir.dstu3.model.ImplementationGuide.GuidePageKind src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case PAGE: return org.hl7.fhir.instance.model.ImplementationGuide.GuidePageKind.PAGE;
    case EXAMPLE: return org.hl7.fhir.instance.model.ImplementationGuide.GuidePageKind.EXAMPLE;
    case LIST: return org.hl7.fhir.instance.model.ImplementationGuide.GuidePageKind.LIST;
    case INCLUDE: return org.hl7.fhir.instance.model.ImplementationGuide.GuidePageKind.INCLUDE;
    case DIRECTORY: return org.hl7.fhir.instance.model.ImplementationGuide.GuidePageKind.DIRECTORY;
    case DICTIONARY: return org.hl7.fhir.instance.model.ImplementationGuide.GuidePageKind.DICTIONARY;
    case TOC: return org.hl7.fhir.instance.model.ImplementationGuide.GuidePageKind.TOC;
    case RESOURCE: return org.hl7.fhir.instance.model.ImplementationGuide.GuidePageKind.RESOURCE;
    default: return org.hl7.fhir.instance.model.ImplementationGuide.GuidePageKind.NULL;
    }
  }


  public org.hl7.fhir.dstu3.model.Location convertLocation(org.hl7.fhir.instance.model.Location src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.Location tgt = new org.hl7.fhir.dstu3.model.Location();
    copyDomainResource(src, tgt);
    for (org.hl7.fhir.instance.model.Identifier t : src.getIdentifier())
      tgt.addIdentifier(convertIdentifier(t));
    tgt.setStatus(convertLocationStatus(src.getStatus()));
    tgt.setName(src.getName());
    tgt.setDescription(src.getDescription());
    tgt.setMode(convertLocationMode(src.getMode()));
    tgt.setType(convertCodeableConcept(src.getType()));
    for (org.hl7.fhir.instance.model.ContactPoint t : src.getTelecom())
      tgt.addTelecom(convertContactPoint(t));
    tgt.setAddress(convertAddress(src.getAddress()));
    tgt.setPhysicalType(convertCodeableConcept(src.getPhysicalType()));
    tgt.setPosition(convertLocationPositionComponent(src.getPosition()));
    tgt.setManagingOrganization(convertReference(src.getManagingOrganization()));
    tgt.setPartOf(convertReference(src.getPartOf()));
    return tgt;
  }

  public org.hl7.fhir.instance.model.Location convertLocation(org.hl7.fhir.dstu3.model.Location src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.Location tgt = new org.hl7.fhir.instance.model.Location();
    copyDomainResource(src, tgt);
    for (org.hl7.fhir.dstu3.model.Identifier t : src.getIdentifier())
      tgt.addIdentifier(convertIdentifier(t));
    tgt.setStatus(convertLocationStatus(src.getStatus()));
    tgt.setName(src.getName());
    tgt.setDescription(src.getDescription());
    tgt.setMode(convertLocationMode(src.getMode()));
    tgt.setType(convertCodeableConcept(src.getType()));
    for (org.hl7.fhir.dstu3.model.ContactPoint t : src.getTelecom())
      tgt.addTelecom(convertContactPoint(t));
    tgt.setAddress(convertAddress(src.getAddress()));
    tgt.setPhysicalType(convertCodeableConcept(src.getPhysicalType()));
    tgt.setPosition(convertLocationPositionComponent(src.getPosition()));
    tgt.setManagingOrganization(convertReference(src.getManagingOrganization()));
    tgt.setPartOf(convertReference(src.getPartOf()));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.Location.LocationStatus convertLocationStatus(org.hl7.fhir.instance.model.Location.LocationStatus src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case ACTIVE: return org.hl7.fhir.dstu3.model.Location.LocationStatus.ACTIVE;
    case SUSPENDED: return org.hl7.fhir.dstu3.model.Location.LocationStatus.SUSPENDED;
    case INACTIVE: return org.hl7.fhir.dstu3.model.Location.LocationStatus.INACTIVE;
    default: return org.hl7.fhir.dstu3.model.Location.LocationStatus.NULL;
    }
  }

  public org.hl7.fhir.instance.model.Location.LocationStatus convertLocationStatus(org.hl7.fhir.dstu3.model.Location.LocationStatus src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case ACTIVE: return org.hl7.fhir.instance.model.Location.LocationStatus.ACTIVE;
    case SUSPENDED: return org.hl7.fhir.instance.model.Location.LocationStatus.SUSPENDED;
    case INACTIVE: return org.hl7.fhir.instance.model.Location.LocationStatus.INACTIVE;
    default: return org.hl7.fhir.instance.model.Location.LocationStatus.NULL;
    }
  }

  public org.hl7.fhir.dstu3.model.Location.LocationMode convertLocationMode(org.hl7.fhir.instance.model.Location.LocationMode src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case INSTANCE: return org.hl7.fhir.dstu3.model.Location.LocationMode.INSTANCE;
    case KIND: return org.hl7.fhir.dstu3.model.Location.LocationMode.KIND;
    default: return org.hl7.fhir.dstu3.model.Location.LocationMode.NULL;
    }
  }

  public org.hl7.fhir.instance.model.Location.LocationMode convertLocationMode(org.hl7.fhir.dstu3.model.Location.LocationMode src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case INSTANCE: return org.hl7.fhir.instance.model.Location.LocationMode.INSTANCE;
    case KIND: return org.hl7.fhir.instance.model.Location.LocationMode.KIND;
    default: return org.hl7.fhir.instance.model.Location.LocationMode.NULL;
    }
  }

  public org.hl7.fhir.dstu3.model.Location.LocationPositionComponent convertLocationPositionComponent(org.hl7.fhir.instance.model.Location.LocationPositionComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.Location.LocationPositionComponent tgt = new org.hl7.fhir.dstu3.model.Location.LocationPositionComponent();
    copyElement(src, tgt);
    tgt.setLongitude(src.getLongitude());
    tgt.setLatitude(src.getLatitude());
    tgt.setAltitude(src.getAltitude());
    return tgt;
  }

  public org.hl7.fhir.instance.model.Location.LocationPositionComponent convertLocationPositionComponent(org.hl7.fhir.dstu3.model.Location.LocationPositionComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.Location.LocationPositionComponent tgt = new org.hl7.fhir.instance.model.Location.LocationPositionComponent();
    copyElement(src, tgt);
    tgt.setLongitude(src.getLongitude());
    tgt.setLatitude(src.getLatitude());
    tgt.setAltitude(src.getAltitude());
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.Media convertMedia(org.hl7.fhir.instance.model.Media src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.Media tgt = new org.hl7.fhir.dstu3.model.Media();
    copyDomainResource(src, tgt);
    for (org.hl7.fhir.instance.model.Identifier t : src.getIdentifier())
      tgt.addIdentifier(convertIdentifier(t));
    tgt.setType(convertDigitalMediaType(src.getType()));
    tgt.setSubtype(convertCodeableConcept(src.getSubtype()));
    tgt.setView(convertCodeableConcept(src.getView()));
    tgt.setSubject(convertReference(src.getSubject()));
    tgt.setOperator(convertReference(src.getOperator()));
    tgt.getDevice().setDisplay(src.getDeviceName());
    tgt.setHeight(src.getHeight());
    tgt.setWidth(src.getWidth());
    tgt.setFrames(src.getFrames());
    tgt.setDuration(src.getDuration());
    tgt.setContent(convertAttachment(src.getContent()));
    return tgt;
  }

  public org.hl7.fhir.instance.model.Media convertMedia(org.hl7.fhir.dstu3.model.Media src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.Media tgt = new org.hl7.fhir.instance.model.Media();
    copyDomainResource(src, tgt);
    for (org.hl7.fhir.dstu3.model.Identifier t : src.getIdentifier())
      tgt.addIdentifier(convertIdentifier(t));
    tgt.setType(convertDigitalMediaType(src.getType()));
    tgt.setSubtype(convertCodeableConcept(src.getSubtype()));
    tgt.setView(convertCodeableConcept(src.getView()));
    tgt.setSubject(convertReference(src.getSubject()));
    tgt.setOperator(convertReference(src.getOperator()));
    tgt.setDeviceName(src.getDevice().getDisplay());
    tgt.setHeight(src.getHeight());
    tgt.setWidth(src.getWidth());
    tgt.setFrames(src.getFrames());
    tgt.setDuration(src.getDuration());
    tgt.setContent(convertAttachment(src.getContent()));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.Media.DigitalMediaType convertDigitalMediaType(org.hl7.fhir.instance.model.Media.DigitalMediaType src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case PHOTO: return org.hl7.fhir.dstu3.model.Media.DigitalMediaType.PHOTO;
    case VIDEO: return org.hl7.fhir.dstu3.model.Media.DigitalMediaType.VIDEO;
    case AUDIO: return org.hl7.fhir.dstu3.model.Media.DigitalMediaType.AUDIO;
    default: return org.hl7.fhir.dstu3.model.Media.DigitalMediaType.NULL;
    }
  }

  public org.hl7.fhir.instance.model.Media.DigitalMediaType convertDigitalMediaType(org.hl7.fhir.dstu3.model.Media.DigitalMediaType src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case PHOTO: return org.hl7.fhir.instance.model.Media.DigitalMediaType.PHOTO;
    case VIDEO: return org.hl7.fhir.instance.model.Media.DigitalMediaType.VIDEO;
    case AUDIO: return org.hl7.fhir.instance.model.Media.DigitalMediaType.AUDIO;
    default: return org.hl7.fhir.instance.model.Media.DigitalMediaType.NULL;
    }
  }

  public org.hl7.fhir.dstu3.model.Medication convertMedication(org.hl7.fhir.instance.model.Medication src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.Medication tgt = new org.hl7.fhir.dstu3.model.Medication();
    copyDomainResource(src, tgt);
    tgt.setCode(convertCodeableConcept(src.getCode()));
    tgt.setIsBrand(src.getIsBrand());
    tgt.setManufacturer(convertReference(src.getManufacturer()));
//    tgt.setProduct(convertMedicationProductComponent(src.getProduct()));
    tgt.setPackage(convertMedicationPackageComponent(src.getPackage()));
    return tgt;
  }

  public org.hl7.fhir.instance.model.Medication convertMedication(org.hl7.fhir.dstu3.model.Medication src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.Medication tgt = new org.hl7.fhir.instance.model.Medication();
    copyDomainResource(src, tgt);
    tgt.setCode(convertCodeableConcept(src.getCode()));
    tgt.setIsBrand(src.getIsBrand());
    tgt.setManufacturer(convertReference(src.getManufacturer()));
//    tgt.setProduct(convertMedicationProductComponent(src.getProduct()));
    tgt.setPackage(convertMedicationPackageComponent(src.getPackage()));
    return tgt;
  }

//  public org.hl7.fhir.dstu3.model.Medication.MedicationProductComponent convertMedicationProductComponent(org.hl7.fhir.instance.model.Medication.MedicationProductComponent src) throws FHIRException {
//    if (src == null || src.isEmpty())
//      return null;
//    org.hl7.fhir.dstu3.model.Medication.MedicationProductComponent tgt = new org.hl7.fhir.dstu3.model.Medication.MedicationProductComponent();
//    copyElement(src, tgt);
//    tgt.setForm(convertCodeableConcept(src.getForm()));
//    for (org.hl7.fhir.instance.model.Medication.MedicationProductIngredientComponent t : src.getIngredient())
//      tgt.addIngredient(convertMedicationProductIngredientComponent(t));
//    for (org.hl7.fhir.instance.model.Medication.MedicationProductBatchComponent t : src.getBatch())
//      tgt.addBatch(convertMedicationProductBatchComponent(t));
//    return tgt;
//  }

//  public org.hl7.fhir.instance.model.Medication.MedicationProductComponent convertMedicationProductComponent(org.hl7.fhir.dstu3.model.Medication.MedicationProductComponent src) throws FHIRException {
//    if (src == null || src.isEmpty())
//      return null;
//    org.hl7.fhir.instance.model.Medication.MedicationProductComponent tgt = new org.hl7.fhir.instance.model.Medication.MedicationProductComponent();
//    copyElement(src, tgt);
//    tgt.setForm(convertCodeableConcept(src.getForm()));
//    for (org.hl7.fhir.dstu3.model.Medication.MedicationProductIngredientComponent t : src.getIngredient())
//      tgt.addIngredient(convertMedicationProductIngredientComponent(t));
//    for (org.hl7.fhir.dstu3.model.Medication.MedicationProductBatchComponent t : src.getBatch())
//      tgt.addBatch(convertMedicationProductBatchComponent(t));
//    return tgt;
//  }

//  public org.hl7.fhir.dstu3.model.Medication.MedicationProductIngredientComponent convertMedicationProductIngredientComponent(org.hl7.fhir.instance.model.Medication.MedicationProductIngredientComponent src) throws FHIRException {
//    if (src == null || src.isEmpty())
//      return null;
//    org.hl7.fhir.dstu3.model.Medication.MedicationProductIngredientComponent tgt = new org.hl7.fhir.dstu3.model.Medication.MedicationProductIngredientComponent();
//    copyElement(src, tgt);
//    tgt.setItem(convertType(src.getItem()));
//    tgt.setAmount(convertRatio(src.getAmount()));
//    return tgt;
//  }

//  public org.hl7.fhir.instance.model.Medication.MedicationProductIngredientComponent convertMedicationProductIngredientComponent(org.hl7.fhir.dstu3.model.Medication.MedicationProductIngredientComponent src) throws FHIRException {
//    if (src == null || src.isEmpty())
//      return null;
//    org.hl7.fhir.instance.model.Medication.MedicationProductIngredientComponent tgt = new org.hl7.fhir.instance.model.Medication.MedicationProductIngredientComponent();
//    copyElement(src, tgt);
//    if (src.hasItemReference())
//      tgt.setItem((org.hl7.fhir.instance.model.Reference) convertType(src.getItem()));
//    tgt.setAmount(convertRatio(src.getAmount()));
//    return tgt;
//  }

//  public org.hl7.fhir.dstu3.model.Medication.MedicationProductBatchComponent convertMedicationProductBatchComponent(org.hl7.fhir.instance.model.Medication.MedicationProductBatchComponent src) throws FHIRException {
//    if (src == null || src.isEmpty())
//      return null;
//    org.hl7.fhir.dstu3.model.Medication.MedicationProductBatchComponent tgt = new org.hl7.fhir.dstu3.model.Medication.MedicationProductBatchComponent();
//    copyElement(src, tgt);
//    tgt.setLotNumber(src.getLotNumber());
//    tgt.setExpirationDate(src.getExpirationDate());
//    return tgt;
//  }

//  public org.hl7.fhir.instance.model.Medication.MedicationProductBatchComponent convertMedicationProductBatchComponent(org.hl7.fhir.dstu3.model.Medication.MedicationProductBatchComponent src) throws FHIRException {
//    if (src == null || src.isEmpty())
//      return null;
//    org.hl7.fhir.instance.model.Medication.MedicationProductBatchComponent tgt = new org.hl7.fhir.instance.model.Medication.MedicationProductBatchComponent();
//    copyElement(src, tgt);
//    tgt.setLotNumber(src.getLotNumber());
//    tgt.setExpirationDate(src.getExpirationDate());
//    return tgt;
//  }

  public org.hl7.fhir.dstu3.model.Medication.MedicationPackageComponent convertMedicationPackageComponent(org.hl7.fhir.instance.model.Medication.MedicationPackageComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.Medication.MedicationPackageComponent tgt = new org.hl7.fhir.dstu3.model.Medication.MedicationPackageComponent();
    copyElement(src, tgt);
    tgt.setContainer(convertCodeableConcept(src.getContainer()));
    for (org.hl7.fhir.instance.model.Medication.MedicationPackageContentComponent t : src.getContent())
      tgt.addContent(convertMedicationPackageContentComponent(t));
    return tgt;
  }

  public org.hl7.fhir.instance.model.Medication.MedicationPackageComponent convertMedicationPackageComponent(org.hl7.fhir.dstu3.model.Medication.MedicationPackageComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.Medication.MedicationPackageComponent tgt = new org.hl7.fhir.instance.model.Medication.MedicationPackageComponent();
    copyElement(src, tgt);
    tgt.setContainer(convertCodeableConcept(src.getContainer()));
    for (org.hl7.fhir.dstu3.model.Medication.MedicationPackageContentComponent t : src.getContent())
      tgt.addContent(convertMedicationPackageContentComponent(t));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.Medication.MedicationPackageContentComponent convertMedicationPackageContentComponent(org.hl7.fhir.instance.model.Medication.MedicationPackageContentComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.Medication.MedicationPackageContentComponent tgt = new org.hl7.fhir.dstu3.model.Medication.MedicationPackageContentComponent();
    copyElement(src, tgt);
    tgt.setItem(convertType(src.getItem()));
    tgt.setAmount(convertSimpleQuantity(src.getAmount()));
    return tgt;
  }

  public org.hl7.fhir.instance.model.Medication.MedicationPackageContentComponent convertMedicationPackageContentComponent(org.hl7.fhir.dstu3.model.Medication.MedicationPackageContentComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.Medication.MedicationPackageContentComponent tgt = new org.hl7.fhir.instance.model.Medication.MedicationPackageContentComponent();
    copyElement(src, tgt);
    if (src.hasItemReference())
      tgt.setItem((org.hl7.fhir.instance.model.Reference) convertType(src.getItem()));
    tgt.setAmount(convertSimpleQuantity(src.getAmount()));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.MedicationDispense convertMedicationDispense(org.hl7.fhir.instance.model.MedicationDispense src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.MedicationDispense tgt = new org.hl7.fhir.dstu3.model.MedicationDispense();
    copyDomainResource(src, tgt);
    tgt.addIdentifier(convertIdentifier(src.getIdentifier()));
    tgt.setStatus(convertMedicationDispenseStatus(src.getStatus()));
    tgt.setMedication(convertType(src.getMedication()));
    tgt.setSubject(convertReference(src.getPatient()));
//    tgt.setDispenser(convertReference(src.getDispenser()));
    for (org.hl7.fhir.instance.model.Reference t : src.getAuthorizingPrescription())
      tgt.addAuthorizingPrescription(convertReference(t));
    tgt.setType(convertCodeableConcept(src.getType()));
    tgt.setQuantity(convertSimpleQuantity(src.getQuantity()));
    tgt.setDaysSupply(convertSimpleQuantity(src.getDaysSupply()));
    tgt.setWhenPrepared(src.getWhenPrepared());
    tgt.setWhenHandedOver(src.getWhenHandedOver());
    tgt.setDestination(convertReference(src.getDestination()));
    for (org.hl7.fhir.instance.model.Reference t : src.getReceiver())
      tgt.addReceiver(convertReference(t));
    if (src.hasNote())
      tgt.addNote().setText(src.getNote());
    for (org.hl7.fhir.instance.model.MedicationDispense.MedicationDispenseDosageInstructionComponent t : src.getDosageInstruction())
      tgt.addDosageInstruction(convertMedicationDispenseDosageInstructionComponent(t));
    tgt.setSubstitution(convertMedicationDispenseSubstitutionComponent(src.getSubstitution()));
    return tgt;
  }

  public org.hl7.fhir.instance.model.MedicationDispense convertMedicationDispense(org.hl7.fhir.dstu3.model.MedicationDispense src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.MedicationDispense tgt = new org.hl7.fhir.instance.model.MedicationDispense();
    copyDomainResource(src, tgt);
    tgt.setIdentifier(convertIdentifier(src.getIdentifierFirstRep()));
    tgt.setStatus(convertMedicationDispenseStatus(src.getStatus()));
    tgt.setMedication(convertType(src.getMedication()));
    tgt.setPatient(convertReference(src.getSubject()));
//    tgt.setDispenser(convertReference(src.getDispenser()));
    for (org.hl7.fhir.dstu3.model.Reference t : src.getAuthorizingPrescription())
      tgt.addAuthorizingPrescription(convertReference(t));
    tgt.setType(convertCodeableConcept(src.getType()));
    tgt.setQuantity(convertSimpleQuantity(src.getQuantity()));
    tgt.setDaysSupply(convertSimpleQuantity(src.getDaysSupply()));
    tgt.setWhenPrepared(src.getWhenPrepared());
    tgt.setWhenHandedOver(src.getWhenHandedOver());
    tgt.setDestination(convertReference(src.getDestination()));
    for (org.hl7.fhir.dstu3.model.Reference t : src.getReceiver())
      tgt.addReceiver(convertReference(t));
    for (org.hl7.fhir.dstu3.model.Annotation t : src.getNote())
      tgt.setNote(t.getText());
    for (org.hl7.fhir.dstu3.model.Dosage t : src.getDosageInstruction())
      tgt.addDosageInstruction(convertMedicationDispenseDosageInstructionComponent(t));
    tgt.setSubstitution(convertMedicationDispenseSubstitutionComponent(src.getSubstitution()));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.MedicationDispense.MedicationDispenseStatus convertMedicationDispenseStatus(org.hl7.fhir.instance.model.MedicationDispense.MedicationDispenseStatus src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case INPROGRESS: return org.hl7.fhir.dstu3.model.MedicationDispense.MedicationDispenseStatus.INPROGRESS;
    case ONHOLD: return org.hl7.fhir.dstu3.model.MedicationDispense.MedicationDispenseStatus.ONHOLD;
    case COMPLETED: return org.hl7.fhir.dstu3.model.MedicationDispense.MedicationDispenseStatus.COMPLETED;
    case ENTEREDINERROR: return org.hl7.fhir.dstu3.model.MedicationDispense.MedicationDispenseStatus.ENTEREDINERROR;
    case STOPPED: return org.hl7.fhir.dstu3.model.MedicationDispense.MedicationDispenseStatus.STOPPED;
    default: return org.hl7.fhir.dstu3.model.MedicationDispense.MedicationDispenseStatus.NULL;
    }
  }

  public org.hl7.fhir.instance.model.MedicationDispense.MedicationDispenseStatus convertMedicationDispenseStatus(org.hl7.fhir.dstu3.model.MedicationDispense.MedicationDispenseStatus src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case INPROGRESS: return org.hl7.fhir.instance.model.MedicationDispense.MedicationDispenseStatus.INPROGRESS;
    case ONHOLD: return org.hl7.fhir.instance.model.MedicationDispense.MedicationDispenseStatus.ONHOLD;
    case COMPLETED: return org.hl7.fhir.instance.model.MedicationDispense.MedicationDispenseStatus.COMPLETED;
    case ENTEREDINERROR: return org.hl7.fhir.instance.model.MedicationDispense.MedicationDispenseStatus.ENTEREDINERROR;
    case STOPPED: return org.hl7.fhir.instance.model.MedicationDispense.MedicationDispenseStatus.STOPPED;
    default: return org.hl7.fhir.instance.model.MedicationDispense.MedicationDispenseStatus.NULL;
    }
  }

  public org.hl7.fhir.dstu3.model.Dosage convertMedicationDispenseDosageInstructionComponent(org.hl7.fhir.instance.model.MedicationDispense.MedicationDispenseDosageInstructionComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.Dosage tgt = new org.hl7.fhir.dstu3.model.Dosage();
    copyElement(src, tgt);
    tgt.setText(src.getText());
//    tgt.setAdditionalInstructions(convertCodeableConcept(src.getAdditionalInstructions()));
    tgt.setTiming(convertTiming(src.getTiming()));
    tgt.setAsNeeded(convertType(src.getAsNeeded()));
    if (src.hasSiteCodeableConcept())
      tgt.setSite(convertCodeableConcept(src.getSiteCodeableConcept()));
    tgt.setRoute(convertCodeableConcept(src.getRoute()));
    tgt.setMethod(convertCodeableConcept(src.getMethod()));
    tgt.setDose(convertType(src.getDose()));
    tgt.setRate(convertType(src.getRate()));
    tgt.setMaxDosePerPeriod(convertRatio(src.getMaxDosePerPeriod()));
    return tgt;
  }

  public org.hl7.fhir.instance.model.MedicationDispense.MedicationDispenseDosageInstructionComponent convertMedicationDispenseDosageInstructionComponent(Dosage src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.MedicationDispense.MedicationDispenseDosageInstructionComponent tgt = new org.hl7.fhir.instance.model.MedicationDispense.MedicationDispenseDosageInstructionComponent();
    copyElement(src, tgt);
    tgt.setText(src.getText());
//    tgt.setAdditionalInstructions(convertCodeableConcept(src.getAdditionalInstructions()));
    tgt.setTiming(convertTiming(src.getTiming()));
    tgt.setAsNeeded(convertType(src.getAsNeeded()));
    tgt.setSite(convertType(src.getSite()));
    tgt.setRoute(convertCodeableConcept(src.getRoute()));
    tgt.setMethod(convertCodeableConcept(src.getMethod()));
    tgt.setDose(convertType(src.getDose()));
    tgt.setRate(convertType(src.getRate()));
    tgt.setMaxDosePerPeriod(convertRatio(src.getMaxDosePerPeriod()));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.MedicationDispense.MedicationDispenseSubstitutionComponent convertMedicationDispenseSubstitutionComponent(org.hl7.fhir.instance.model.MedicationDispense.MedicationDispenseSubstitutionComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.MedicationDispense.MedicationDispenseSubstitutionComponent tgt = new org.hl7.fhir.dstu3.model.MedicationDispense.MedicationDispenseSubstitutionComponent();
    copyElement(src, tgt);
    tgt.setType(convertCodeableConcept(src.getType()));
    for (org.hl7.fhir.instance.model.CodeableConcept t : src.getReason())
      tgt.addReason(convertCodeableConcept(t));
    for (org.hl7.fhir.instance.model.Reference t : src.getResponsibleParty())
      tgt.addResponsibleParty(convertReference(t));
    return tgt;
  }

  public org.hl7.fhir.instance.model.MedicationDispense.MedicationDispenseSubstitutionComponent convertMedicationDispenseSubstitutionComponent(org.hl7.fhir.dstu3.model.MedicationDispense.MedicationDispenseSubstitutionComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.MedicationDispense.MedicationDispenseSubstitutionComponent tgt = new org.hl7.fhir.instance.model.MedicationDispense.MedicationDispenseSubstitutionComponent();
    copyElement(src, tgt);
    tgt.setType(convertCodeableConcept(src.getType()));
    for (org.hl7.fhir.dstu3.model.CodeableConcept t : src.getReason())
      tgt.addReason(convertCodeableConcept(t));
    for (org.hl7.fhir.dstu3.model.Reference t : src.getResponsibleParty())
      tgt.addResponsibleParty(convertReference(t));
    return tgt;
  }

//  public org.hl7.fhir.dstu3.model.MedicationOrder convertMedicationOrder(org.hl7.fhir.instance.model.MedicationOrder src) throws FHIRException {
//    if (src == null || src.isEmpty())
//      return null;
//    org.hl7.fhir.dstu3.model.MedicationOrder tgt = new org.hl7.fhir.dstu3.model.MedicationOrder();
//    copyDomainResource(src, tgt);
//    for (org.hl7.fhir.instance.model.Identifier t : src.getIdentifier())
//      tgt.addIdentifier(convertIdentifier(t));
//    tgt.setStatus(convertMedicationOrderStatus(src.getStatus()));
//    tgt.setMedication(convertType(src.getMedication()));
//    tgt.setPatient(convertReference(src.getPatient()));
//    tgt.setEncounter(convertReference(src.getEncounter()));
//    if (src.hasDateWritten())
//      tgt.setDateWritten(src.getDateWritten());
//    tgt.setPrescriber(convertReference(src.getPrescriber()));
//    if (src.hasReasonCodeableConcept())
//      tgt.addReasonCode(convertCodeableConcept(src.getReasonCodeableConcept()));
//    if (src.hasReasonReference())
//      tgt.addReasonReference(convertReference(src.getReasonReference()));
////    tgt.setDateEnded(src.getDateEnded());
////    tgt.setReasonEnded(convertCodeableConcept(src.getReasonEnded()));
//    if (src.hasNote())
//      tgt.addNote().setText(src.getNote());
//    for (org.hl7.fhir.instance.model.MedicationOrder.MedicationOrderDosageInstructionComponent t : src.getDosageInstruction())
//      tgt.addDosageInstruction(convertMedicationOrderDosageInstructionComponent(t));
//    tgt.setDispenseRequest(convertMedicationOrderDispenseRequestComponent(src.getDispenseRequest()));
//    tgt.setSubstitution(convertMedicationOrderSubstitutionComponent(src.getSubstitution()));
//    tgt.setPriorPrescription(convertReference(src.getPriorPrescription()));
//    return tgt;
//  }
//
//  public org.hl7.fhir.instance.model.MedicationOrder convertMedicationOrder(org.hl7.fhir.dstu3.model.MedicationOrder src) throws FHIRException {
//    if (src == null || src.isEmpty())
//      return null;
//    org.hl7.fhir.instance.model.MedicationOrder tgt = new org.hl7.fhir.instance.model.MedicationOrder();
//    copyDomainResource(src, tgt);
//    for (org.hl7.fhir.dstu3.model.Identifier t : src.getIdentifier())
//      tgt.addIdentifier(convertIdentifier(t));
//    tgt.setStatus(convertMedicationOrderStatus(src.getStatus()));
//    tgt.setMedication(convertType(src.getMedication()));
//    tgt.setPatient(convertReference(src.getPatient()));
//    tgt.setEncounter(convertReference(src.getEncounter()));
//    if (src.hasDateWritten())
//      tgt.setDateWritten(src.getDateWritten());
//    tgt.setPrescriber(convertReference(src.getPrescriber()));
//    for (org.hl7.fhir.dstu3.model.CodeableConcept t : src.getReasonCode())
//      tgt.setReason(convertCodeableConcept(t));
//    for (org.hl7.fhir.dstu3.model.Reference t : src.getReasonReference())
//      tgt.setReason(convertReference(t));
////    tgt.setDateEnded(src.getDateEnded());
////    tgt.setReasonEnded(convertCodeableConcept(src.getReasonEnded()));
//    for (org.hl7.fhir.dstu3.model.Annotation t : src.getNote())
//      tgt.setNote(t.getText());
//    for (org.hl7.fhir.dstu3.model.DosageInstruction t : src.getDosageInstruction())
//      tgt.addDosageInstruction(convertMedicationOrderDosageInstructionComponent(t));
//    tgt.setDispenseRequest(convertMedicationOrderDispenseRequestComponent(src.getDispenseRequest()));
//    tgt.setSubstitution(convertMedicationOrderSubstitutionComponent(src.getSubstitution()));
//    tgt.setPriorPrescription(convertReference(src.getPriorPrescription()));
//    return tgt;
//  }
//
//  public org.hl7.fhir.dstu3.model.MedicationOrder.MedicationOrderStatus convertMedicationOrderStatus(org.hl7.fhir.instance.model.MedicationOrder.MedicationOrderStatus src) throws FHIRException {
//    if (src == null)
//      return null;
//    switch (src) {
//    case ACTIVE: return org.hl7.fhir.dstu3.model.MedicationOrder.MedicationOrderStatus.ACTIVE;
//    case ONHOLD: return org.hl7.fhir.dstu3.model.MedicationOrder.MedicationOrderStatus.ONHOLD;
//    case COMPLETED: return org.hl7.fhir.dstu3.model.MedicationOrder.MedicationOrderStatus.COMPLETED;
//    case ENTEREDINERROR: return org.hl7.fhir.dstu3.model.MedicationOrder.MedicationOrderStatus.ENTEREDINERROR;
//    case STOPPED: return org.hl7.fhir.dstu3.model.MedicationOrder.MedicationOrderStatus.STOPPED;
//    case DRAFT: return org.hl7.fhir.dstu3.model.MedicationOrder.MedicationOrderStatus.DRAFT;
//    default: return org.hl7.fhir.dstu3.model.MedicationOrder.MedicationOrderStatus.NULL;
//    }
//  }
//
//  public org.hl7.fhir.instance.model.MedicationOrder.MedicationOrderStatus convertMedicationOrderStatus(org.hl7.fhir.dstu3.model.MedicationOrder.MedicationOrderStatus src) throws FHIRException {
//    if (src == null)
//      return null;
//    switch (src) {
//    case ACTIVE: return org.hl7.fhir.instance.model.MedicationOrder.MedicationOrderStatus.ACTIVE;
//    case ONHOLD: return org.hl7.fhir.instance.model.MedicationOrder.MedicationOrderStatus.ONHOLD;
//    case COMPLETED: return org.hl7.fhir.instance.model.MedicationOrder.MedicationOrderStatus.COMPLETED;
//    case ENTEREDINERROR: return org.hl7.fhir.instance.model.MedicationOrder.MedicationOrderStatus.ENTEREDINERROR;
//    case STOPPED: return org.hl7.fhir.instance.model.MedicationOrder.MedicationOrderStatus.STOPPED;
//    case DRAFT: return org.hl7.fhir.instance.model.MedicationOrder.MedicationOrderStatus.DRAFT;
//    default: return org.hl7.fhir.instance.model.MedicationOrder.MedicationOrderStatus.NULL;
//    }
//  }

  public org.hl7.fhir.dstu3.model.Dosage convertMedicationOrderDosageInstructionComponent(org.hl7.fhir.instance.model.MedicationOrder.MedicationOrderDosageInstructionComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.Dosage tgt = new org.hl7.fhir.dstu3.model.Dosage();
    copyElement(src, tgt);
    tgt.setText(src.getText());
//    tgt.setAdditionalInstructions(convertCodeableConcept(src.getAdditionalInstructions()));
    tgt.setTiming(convertTiming(src.getTiming()));
    tgt.setAsNeeded(convertType(src.getAsNeeded()));
    if (src.hasSiteCodeableConcept())
      tgt.setSite(convertCodeableConcept(src.getSiteCodeableConcept()));
    tgt.setRoute(convertCodeableConcept(src.getRoute()));
    tgt.setMethod(convertCodeableConcept(src.getMethod()));
    tgt.setDose(convertType(src.getDose()));
    tgt.setRate(convertType(src.getRate()));
    tgt.setMaxDosePerPeriod(convertRatio(src.getMaxDosePerPeriod()));
    return tgt;
  }

  public org.hl7.fhir.instance.model.MedicationOrder.MedicationOrderDosageInstructionComponent convertMedicationOrderDosageInstructionComponent(org.hl7.fhir.dstu3.model.Dosage src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.MedicationOrder.MedicationOrderDosageInstructionComponent tgt = new org.hl7.fhir.instance.model.MedicationOrder.MedicationOrderDosageInstructionComponent();
    copyElement(src, tgt);
    tgt.setText(src.getText());
//    tgt.setAdditionalInstructions(convertCodeableConcept(src.getAdditionalInstructions()));
    tgt.setTiming(convertTiming(src.getTiming()));
    tgt.setAsNeeded(convertType(src.getAsNeeded()));
    tgt.setSite(convertType(src.getSite()));
    tgt.setRoute(convertCodeableConcept(src.getRoute()));
    tgt.setMethod(convertCodeableConcept(src.getMethod()));
    tgt.setDose(convertType(src.getDose()));
    tgt.setRate(convertType(src.getRate()));
    tgt.setMaxDosePerPeriod(convertRatio(src.getMaxDosePerPeriod()));
    return tgt;
  }

//  public org.hl7.fhir.dstu3.model.MedicationOrder.MedicationOrderDispenseRequestComponent convertMedicationOrderDispenseRequestComponent(org.hl7.fhir.instance.model.MedicationOrder.MedicationOrderDispenseRequestComponent src) throws FHIRException {
//    if (src == null || src.isEmpty())
//      return null;
//    org.hl7.fhir.dstu3.model.MedicationOrder.MedicationOrderDispenseRequestComponent tgt = new org.hl7.fhir.dstu3.model.MedicationOrder.MedicationOrderDispenseRequestComponent();
//    copyElement(src, tgt);
////    tgt.setMedication(convertType(src.getMedication()));
//    tgt.setValidityPeriod(convertPeriod(src.getValidityPeriod()));
//    tgt.setNumberOfRepeatsAllowed(src.getNumberOfRepeatsAllowed());
//    tgt.setQuantity(convertSimpleQuantity(src.getQuantity()));
//    tgt.setExpectedSupplyDuration(convertDuration(src.getExpectedSupplyDuration()));
//    return tgt;
//  }
//
//  public org.hl7.fhir.instance.model.MedicationOrder.MedicationOrderDispenseRequestComponent convertMedicationOrderDispenseRequestComponent(org.hl7.fhir.dstu3.model.MedicationOrder.MedicationOrderDispenseRequestComponent src) throws FHIRException {
//    if (src == null || src.isEmpty())
//      return null;
//    org.hl7.fhir.instance.model.MedicationOrder.MedicationOrderDispenseRequestComponent tgt = new org.hl7.fhir.instance.model.MedicationOrder.MedicationOrderDispenseRequestComponent();
//    copyElement(src, tgt);
////    tgt.setMedication(convertType(src.getMedication()));
//    tgt.setValidityPeriod(convertPeriod(src.getValidityPeriod()));
//    tgt.setNumberOfRepeatsAllowed(src.getNumberOfRepeatsAllowed());
//    tgt.setQuantity(convertSimpleQuantity(src.getQuantity()));
//    tgt.setExpectedSupplyDuration(convertDuration(src.getExpectedSupplyDuration()));
//    return tgt;
//  }
//
//  public org.hl7.fhir.dstu3.model.MedicationOrder.MedicationOrderSubstitutionComponent convertMedicationOrderSubstitutionComponent(org.hl7.fhir.instance.model.MedicationOrder.MedicationOrderSubstitutionComponent src) throws FHIRException {
//    if (src == null || src.isEmpty())
//      return null;
//    org.hl7.fhir.dstu3.model.MedicationOrder.MedicationOrderSubstitutionComponent tgt = new org.hl7.fhir.dstu3.model.MedicationOrder.MedicationOrderSubstitutionComponent();
//    copyElement(src, tgt);
////    tgt.setType(convertCodeableConcept(src.getType()));
//    tgt.setReason(convertCodeableConcept(src.getReason()));
//    return tgt;
//  }
//
//  public org.hl7.fhir.instance.model.MedicationOrder.MedicationOrderSubstitutionComponent convertMedicationOrderSubstitutionComponent(org.hl7.fhir.dstu3.model.MedicationOrder.MedicationOrderSubstitutionComponent src) throws FHIRException {
//    if (src == null || src.isEmpty())
//      return null;
//    org.hl7.fhir.instance.model.MedicationOrder.MedicationOrderSubstitutionComponent tgt = new org.hl7.fhir.instance.model.MedicationOrder.MedicationOrderSubstitutionComponent();
//    copyElement(src, tgt);
////    tgt.setType(convertCodeableConcept(src.getType()));
//    tgt.setReason(convertCodeableConcept(src.getReason()));
//    return tgt;
//  }

  public org.hl7.fhir.dstu3.model.MedicationStatement convertMedicationStatement(org.hl7.fhir.instance.model.MedicationStatement src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.MedicationStatement tgt = new org.hl7.fhir.dstu3.model.MedicationStatement();
    copyDomainResource(src, tgt);
    for (org.hl7.fhir.instance.model.Identifier t : src.getIdentifier())
      tgt.addIdentifier(convertIdentifier(t));
    tgt.setStatus(convertMedicationStatementStatus(src.getStatus()));
    tgt.setMedication(convertType(src.getMedication()));
    tgt.setSubject(convertReference(src.getPatient()));
    tgt.setEffective(convertType(src.getEffective()));
    tgt.setInformationSource(convertReference(src.getInformationSource()));
    for (org.hl7.fhir.instance.model.Reference t : src.getSupportingInformation())
      tgt.addDerivedFrom(convertReference(t));
    if (src.hasDateAsserted())
      tgt.setDateAsserted(src.getDateAsserted());
//    tgt.getNotTakenElement().setValueAsString(src.getWasNotTaken() ? "Y" : "N");
    for (org.hl7.fhir.instance.model.CodeableConcept t : src.getReasonNotTaken())
      tgt.addReasonNotTaken(convertCodeableConcept(t));
//    tgt.setReasonForUse(convertType(src.getReasonForUse()));
    if (src.hasNote())
      tgt.addNote().setText(src.getNote());
    for (org.hl7.fhir.instance.model.MedicationStatement.MedicationStatementDosageComponent t : src.getDosage())
      tgt.addDosage(convertMedicationStatementDosageComponent(t));
    return tgt;
  }

  public org.hl7.fhir.instance.model.MedicationStatement convertMedicationStatement(org.hl7.fhir.dstu3.model.MedicationStatement src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.MedicationStatement tgt = new org.hl7.fhir.instance.model.MedicationStatement();
    copyDomainResource(src, tgt);
    for (org.hl7.fhir.dstu3.model.Identifier t : src.getIdentifier())
      tgt.addIdentifier(convertIdentifier(t));
    tgt.setStatus(convertMedicationStatementStatus(src.getStatus()));
    tgt.setMedication(convertType(src.getMedication()));
    tgt.setPatient(convertReference(src.getSubject()));
    tgt.setEffective(convertType(src.getEffective()));
    tgt.setInformationSource(convertReference(src.getInformationSource()));
    for (org.hl7.fhir.dstu3.model.Reference t : src.getDerivedFrom())
      tgt.addSupportingInformation(convertReference(t));
    if (src.hasDateAsserted())
      tgt.setDateAsserted(src.getDateAsserted());
//    tgt.setWasNotTaken("Y".equals(src.getNotTakenElement().getValueAsString()));
    for (org.hl7.fhir.dstu3.model.CodeableConcept t : src.getReasonNotTaken())
      tgt.addReasonNotTaken(convertCodeableConcept(t));
//    tgt.setReasonForUse(convertType(src.getReasonForUse()));
    for (org.hl7.fhir.dstu3.model.Annotation t : src.getNote())
      tgt.setNote(t.getText());
    for (org.hl7.fhir.dstu3.model.Dosage t : src.getDosage())
      tgt.addDosage(convertMedicationStatementDosageComponent(t));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.MedicationStatement.MedicationStatementStatus convertMedicationStatementStatus(org.hl7.fhir.instance.model.MedicationStatement.MedicationStatementStatus src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case ACTIVE: return org.hl7.fhir.dstu3.model.MedicationStatement.MedicationStatementStatus.ACTIVE;
    case COMPLETED: return org.hl7.fhir.dstu3.model.MedicationStatement.MedicationStatementStatus.COMPLETED;
    case ENTEREDINERROR: return org.hl7.fhir.dstu3.model.MedicationStatement.MedicationStatementStatus.ENTEREDINERROR;
    case INTENDED: return org.hl7.fhir.dstu3.model.MedicationStatement.MedicationStatementStatus.INTENDED;
    default: return org.hl7.fhir.dstu3.model.MedicationStatement.MedicationStatementStatus.NULL;
    }
  }

  public org.hl7.fhir.instance.model.MedicationStatement.MedicationStatementStatus convertMedicationStatementStatus(org.hl7.fhir.dstu3.model.MedicationStatement.MedicationStatementStatus src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case ACTIVE: return org.hl7.fhir.instance.model.MedicationStatement.MedicationStatementStatus.ACTIVE;
    case COMPLETED: return org.hl7.fhir.instance.model.MedicationStatement.MedicationStatementStatus.COMPLETED;
    case ENTEREDINERROR: return org.hl7.fhir.instance.model.MedicationStatement.MedicationStatementStatus.ENTEREDINERROR;
    case INTENDED: return org.hl7.fhir.instance.model.MedicationStatement.MedicationStatementStatus.INTENDED;
    default: return org.hl7.fhir.instance.model.MedicationStatement.MedicationStatementStatus.NULL;
    }
  }

  public org.hl7.fhir.dstu3.model.Dosage convertMedicationStatementDosageComponent(org.hl7.fhir.instance.model.MedicationStatement.MedicationStatementDosageComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.Dosage tgt = new org.hl7.fhir.dstu3.model.Dosage();
    copyElement(src, tgt);
    tgt.setText(src.getText());
    tgt.setTiming(convertTiming(src.getTiming()));
    tgt.setAsNeeded(convertType(src.getAsNeeded()));
    if (src.hasSiteCodeableConcept())
      tgt.setSite(convertCodeableConcept(src.getSiteCodeableConcept()));
    tgt.setRoute(convertCodeableConcept(src.getRoute()));
    tgt.setMethod(convertCodeableConcept(src.getMethod()));
//    tgt.setQuantity(convertType(src.getQuantity()));
    tgt.setRate(convertType(src.getRate()));
    tgt.setMaxDosePerPeriod(convertRatio(src.getMaxDosePerPeriod()));
    return tgt;
  }

  public org.hl7.fhir.instance.model.MedicationStatement.MedicationStatementDosageComponent convertMedicationStatementDosageComponent(org.hl7.fhir.dstu3.model.Dosage src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.MedicationStatement.MedicationStatementDosageComponent tgt = new org.hl7.fhir.instance.model.MedicationStatement.MedicationStatementDosageComponent();
    copyElement(src, tgt);
    tgt.setText(src.getText());
    tgt.setTiming(convertTiming(src.getTiming()));
    tgt.setAsNeeded(convertType(src.getAsNeeded()));
    tgt.setSite(convertType(src.getSite()));
    tgt.setRoute(convertCodeableConcept(src.getRoute()));
    tgt.setMethod(convertCodeableConcept(src.getMethod()));
//    tgt.setQuantity(convertType(src.getQuantity()));
    tgt.setRate(convertType(src.getRate()));
    tgt.setMaxDosePerPeriod(convertRatio(src.getMaxDosePerPeriod()));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.MessageHeader convertMessageHeader(org.hl7.fhir.instance.model.MessageHeader src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.MessageHeader tgt = new org.hl7.fhir.dstu3.model.MessageHeader();
    copyDomainResource(src, tgt);
    tgt.setTimestamp(src.getTimestamp());
    tgt.setEvent(convertCoding(src.getEvent()));
    tgt.setResponse(convertMessageHeaderResponseComponent(src.getResponse()));
    tgt.setSource(convertMessageSourceComponent(src.getSource()));
    for (org.hl7.fhir.instance.model.MessageHeader.MessageDestinationComponent t : src.getDestination())
      tgt.addDestination(convertMessageDestinationComponent(t));
    tgt.setEnterer(convertReference(src.getEnterer()));
    tgt.setAuthor(convertReference(src.getAuthor()));
    tgt.setReceiver(convertReference(src.getReceiver()));
    tgt.setResponsible(convertReference(src.getResponsible()));
    tgt.setReason(convertCodeableConcept(src.getReason()));
    for (org.hl7.fhir.instance.model.Reference t : src.getData())
      tgt.addFocus(convertReference(t));
    return tgt;
  }

  public org.hl7.fhir.instance.model.MessageHeader convertMessageHeader(org.hl7.fhir.dstu3.model.MessageHeader src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.MessageHeader tgt = new org.hl7.fhir.instance.model.MessageHeader();
    copyDomainResource(src, tgt);
    tgt.setTimestamp(src.getTimestamp());
    tgt.setEvent(convertCoding(src.getEvent()));
    tgt.setResponse(convertMessageHeaderResponseComponent(src.getResponse()));
    tgt.setSource(convertMessageSourceComponent(src.getSource()));
    for (org.hl7.fhir.dstu3.model.MessageHeader.MessageDestinationComponent t : src.getDestination())
      tgt.addDestination(convertMessageDestinationComponent(t));
    tgt.setEnterer(convertReference(src.getEnterer()));
    tgt.setAuthor(convertReference(src.getAuthor()));
    tgt.setReceiver(convertReference(src.getReceiver()));
    tgt.setResponsible(convertReference(src.getResponsible()));
    tgt.setReason(convertCodeableConcept(src.getReason()));
    for (org.hl7.fhir.dstu3.model.Reference t : src.getFocus())
      tgt.addData(convertReference(t));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.MessageHeader.MessageHeaderResponseComponent convertMessageHeaderResponseComponent(org.hl7.fhir.instance.model.MessageHeader.MessageHeaderResponseComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.MessageHeader.MessageHeaderResponseComponent tgt = new org.hl7.fhir.dstu3.model.MessageHeader.MessageHeaderResponseComponent();
    copyElement(src, tgt);
    tgt.setIdentifier(src.getIdentifier());
    tgt.setCode(convertResponseType(src.getCode()));
    tgt.setDetails(convertReference(src.getDetails()));
    return tgt;
  }

  public org.hl7.fhir.instance.model.MessageHeader.MessageHeaderResponseComponent convertMessageHeaderResponseComponent(org.hl7.fhir.dstu3.model.MessageHeader.MessageHeaderResponseComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.MessageHeader.MessageHeaderResponseComponent tgt = new org.hl7.fhir.instance.model.MessageHeader.MessageHeaderResponseComponent();
    copyElement(src, tgt);
    tgt.setIdentifier(src.getIdentifier());
    tgt.setCode(convertResponseType(src.getCode()));
    tgt.setDetails(convertReference(src.getDetails()));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.MessageHeader.ResponseType convertResponseType(org.hl7.fhir.instance.model.MessageHeader.ResponseType src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case OK: return org.hl7.fhir.dstu3.model.MessageHeader.ResponseType.OK;
    case TRANSIENTERROR: return org.hl7.fhir.dstu3.model.MessageHeader.ResponseType.TRANSIENTERROR;
    case FATALERROR: return org.hl7.fhir.dstu3.model.MessageHeader.ResponseType.FATALERROR;
    default: return org.hl7.fhir.dstu3.model.MessageHeader.ResponseType.NULL;
    }
  }

  public org.hl7.fhir.instance.model.MessageHeader.ResponseType convertResponseType(org.hl7.fhir.dstu3.model.MessageHeader.ResponseType src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case OK: return org.hl7.fhir.instance.model.MessageHeader.ResponseType.OK;
    case TRANSIENTERROR: return org.hl7.fhir.instance.model.MessageHeader.ResponseType.TRANSIENTERROR;
    case FATALERROR: return org.hl7.fhir.instance.model.MessageHeader.ResponseType.FATALERROR;
    default: return org.hl7.fhir.instance.model.MessageHeader.ResponseType.NULL;
    }
  }

  public org.hl7.fhir.dstu3.model.MessageHeader.MessageSourceComponent convertMessageSourceComponent(org.hl7.fhir.instance.model.MessageHeader.MessageSourceComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.MessageHeader.MessageSourceComponent tgt = new org.hl7.fhir.dstu3.model.MessageHeader.MessageSourceComponent();
    copyElement(src, tgt);
    tgt.setName(src.getName());
    tgt.setSoftware(src.getSoftware());
    tgt.setVersion(src.getVersion());
    tgt.setContact(convertContactPoint(src.getContact()));
    tgt.setEndpoint(src.getEndpoint());
    return tgt;
  }

  public org.hl7.fhir.instance.model.MessageHeader.MessageSourceComponent convertMessageSourceComponent(org.hl7.fhir.dstu3.model.MessageHeader.MessageSourceComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.MessageHeader.MessageSourceComponent tgt = new org.hl7.fhir.instance.model.MessageHeader.MessageSourceComponent();
    copyElement(src, tgt);
    tgt.setName(src.getName());
    tgt.setSoftware(src.getSoftware());
    tgt.setVersion(src.getVersion());
    tgt.setContact(convertContactPoint(src.getContact()));
    tgt.setEndpoint(src.getEndpoint());
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.MessageHeader.MessageDestinationComponent convertMessageDestinationComponent(org.hl7.fhir.instance.model.MessageHeader.MessageDestinationComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.MessageHeader.MessageDestinationComponent tgt = new org.hl7.fhir.dstu3.model.MessageHeader.MessageDestinationComponent();
    copyElement(src, tgt);
    tgt.setName(src.getName());
    tgt.setTarget(convertReference(src.getTarget()));
    tgt.setEndpoint(src.getEndpoint());
    return tgt;
  }

  public org.hl7.fhir.instance.model.MessageHeader.MessageDestinationComponent convertMessageDestinationComponent(org.hl7.fhir.dstu3.model.MessageHeader.MessageDestinationComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.MessageHeader.MessageDestinationComponent tgt = new org.hl7.fhir.instance.model.MessageHeader.MessageDestinationComponent();
    copyElement(src, tgt);
    tgt.setName(src.getName());
    tgt.setTarget(convertReference(src.getTarget()));
    tgt.setEndpoint(src.getEndpoint());
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.NamingSystem convertNamingSystem(org.hl7.fhir.instance.model.NamingSystem src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.NamingSystem tgt = new org.hl7.fhir.dstu3.model.NamingSystem();
    copyDomainResource(src, tgt);
    tgt.setName(src.getName());
    tgt.setStatus(convertConformanceResourceStatus(src.getStatus()));
    tgt.setKind(convertNamingSystemType(src.getKind()));
    if (src.hasDate())
      tgt.setDate(src.getDate());
    tgt.setPublisher(src.getPublisher());
    for (org.hl7.fhir.instance.model.NamingSystem.NamingSystemContactComponent t : src.getContact())
      tgt.addContact(convertNamingSystemContactComponent(t));
    tgt.setResponsible(src.getResponsible());
    tgt.setType(convertCodeableConcept(src.getType()));
    tgt.setDescription(src.getDescription());
    for (org.hl7.fhir.instance.model.CodeableConcept t : src.getUseContext())
      if (isJurisdiction(t))
        tgt.addJurisdiction(convertCodeableConcept(t));
      else
        tgt.addUseContext(convertCodeableConceptToUsageContext(t));
    tgt.setUsage(src.getUsage());
    for (org.hl7.fhir.instance.model.NamingSystem.NamingSystemUniqueIdComponent t : src.getUniqueId())
      tgt.addUniqueId(convertNamingSystemUniqueIdComponent(t));
    tgt.setReplacedBy(convertReference(src.getReplacedBy()));
    return tgt;
  }

  public org.hl7.fhir.instance.model.NamingSystem convertNamingSystem(org.hl7.fhir.dstu3.model.NamingSystem src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.NamingSystem tgt = new org.hl7.fhir.instance.model.NamingSystem();
    copyDomainResource(src, tgt);
    tgt.setName(src.getName());
    tgt.setStatus(convertConformanceResourceStatus(src.getStatus()));
    tgt.setKind(convertNamingSystemType(src.getKind()));
    if (src.hasDate())
      tgt.setDate(src.getDate());
    tgt.setPublisher(src.getPublisher());
    for (org.hl7.fhir.dstu3.model.ContactDetail t : src.getContact())
      tgt.addContact(convertNamingSystemContactComponent(t));
    tgt.setResponsible(src.getResponsible());
    tgt.setType(convertCodeableConcept(src.getType()));
    tgt.setDescription(src.getDescription());
    for (org.hl7.fhir.dstu3.model.UsageContext t : src.getUseContext())
      if (t.hasValueCodeableConcept())
        tgt.addUseContext(convertCodeableConcept(t.getValueCodeableConcept()));
    for (org.hl7.fhir.dstu3.model.CodeableConcept t : src.getJurisdiction())
        tgt.addUseContext(convertCodeableConcept(t));
    tgt.setUsage(src.getUsage());
    for (org.hl7.fhir.dstu3.model.NamingSystem.NamingSystemUniqueIdComponent t : src.getUniqueId())
      tgt.addUniqueId(convertNamingSystemUniqueIdComponent(t));
    tgt.setReplacedBy(convertReference(src.getReplacedBy()));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.NamingSystem.NamingSystemType convertNamingSystemType(org.hl7.fhir.instance.model.NamingSystem.NamingSystemType src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case CODESYSTEM: return org.hl7.fhir.dstu3.model.NamingSystem.NamingSystemType.CODESYSTEM;
    case IDENTIFIER: return org.hl7.fhir.dstu3.model.NamingSystem.NamingSystemType.IDENTIFIER;
    case ROOT: return org.hl7.fhir.dstu3.model.NamingSystem.NamingSystemType.ROOT;
    default: return org.hl7.fhir.dstu3.model.NamingSystem.NamingSystemType.NULL;
    }
  }

  public org.hl7.fhir.instance.model.NamingSystem.NamingSystemType convertNamingSystemType(org.hl7.fhir.dstu3.model.NamingSystem.NamingSystemType src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case CODESYSTEM: return org.hl7.fhir.instance.model.NamingSystem.NamingSystemType.CODESYSTEM;
    case IDENTIFIER: return org.hl7.fhir.instance.model.NamingSystem.NamingSystemType.IDENTIFIER;
    case ROOT: return org.hl7.fhir.instance.model.NamingSystem.NamingSystemType.ROOT;
    default: return org.hl7.fhir.instance.model.NamingSystem.NamingSystemType.NULL;
    }
  }

  public org.hl7.fhir.dstu3.model.ContactDetail convertNamingSystemContactComponent(org.hl7.fhir.instance.model.NamingSystem.NamingSystemContactComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.ContactDetail tgt = new org.hl7.fhir.dstu3.model.ContactDetail();
    copyElement(src, tgt);
    tgt.setName(src.getName());
    for (org.hl7.fhir.instance.model.ContactPoint t : src.getTelecom())
      tgt.addTelecom(convertContactPoint(t));
    return tgt;
  }

  public org.hl7.fhir.instance.model.NamingSystem.NamingSystemContactComponent convertNamingSystemContactComponent(org.hl7.fhir.dstu3.model.ContactDetail src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.NamingSystem.NamingSystemContactComponent tgt = new org.hl7.fhir.instance.model.NamingSystem.NamingSystemContactComponent();
    copyElement(src, tgt);
    tgt.setName(src.getName());
    for (org.hl7.fhir.dstu3.model.ContactPoint t : src.getTelecom())
      tgt.addTelecom(convertContactPoint(t));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.NamingSystem.NamingSystemUniqueIdComponent convertNamingSystemUniqueIdComponent(org.hl7.fhir.instance.model.NamingSystem.NamingSystemUniqueIdComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.NamingSystem.NamingSystemUniqueIdComponent tgt = new org.hl7.fhir.dstu3.model.NamingSystem.NamingSystemUniqueIdComponent();
    copyElement(src, tgt);
    tgt.setType(convertNamingSystemIdentifierType(src.getType()));
    tgt.setValue(src.getValue());
    tgt.setPreferred(src.getPreferred());
    tgt.setPeriod(convertPeriod(src.getPeriod()));
    return tgt;
  }

  public org.hl7.fhir.instance.model.NamingSystem.NamingSystemUniqueIdComponent convertNamingSystemUniqueIdComponent(org.hl7.fhir.dstu3.model.NamingSystem.NamingSystemUniqueIdComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.NamingSystem.NamingSystemUniqueIdComponent tgt = new org.hl7.fhir.instance.model.NamingSystem.NamingSystemUniqueIdComponent();
    copyElement(src, tgt);
    tgt.setType(convertNamingSystemIdentifierType(src.getType()));
    tgt.setValue(src.getValue());
    tgt.setPreferred(src.getPreferred());
    tgt.setPeriod(convertPeriod(src.getPeriod()));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.NamingSystem.NamingSystemIdentifierType convertNamingSystemIdentifierType(org.hl7.fhir.instance.model.NamingSystem.NamingSystemIdentifierType src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case OID: return org.hl7.fhir.dstu3.model.NamingSystem.NamingSystemIdentifierType.OID;
    case UUID: return org.hl7.fhir.dstu3.model.NamingSystem.NamingSystemIdentifierType.UUID;
    case URI: return org.hl7.fhir.dstu3.model.NamingSystem.NamingSystemIdentifierType.URI;
    case OTHER: return org.hl7.fhir.dstu3.model.NamingSystem.NamingSystemIdentifierType.OTHER;
    default: return org.hl7.fhir.dstu3.model.NamingSystem.NamingSystemIdentifierType.NULL;
    }
  }

  public org.hl7.fhir.instance.model.NamingSystem.NamingSystemIdentifierType convertNamingSystemIdentifierType(org.hl7.fhir.dstu3.model.NamingSystem.NamingSystemIdentifierType src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case OID: return org.hl7.fhir.instance.model.NamingSystem.NamingSystemIdentifierType.OID;
    case UUID: return org.hl7.fhir.instance.model.NamingSystem.NamingSystemIdentifierType.UUID;
    case URI: return org.hl7.fhir.instance.model.NamingSystem.NamingSystemIdentifierType.URI;
    case OTHER: return org.hl7.fhir.instance.model.NamingSystem.NamingSystemIdentifierType.OTHER;
    default: return org.hl7.fhir.instance.model.NamingSystem.NamingSystemIdentifierType.NULL;
    }
  }

  public org.hl7.fhir.dstu3.model.Observation convertObservation(org.hl7.fhir.instance.model.Observation src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.Observation tgt = new org.hl7.fhir.dstu3.model.Observation();
    copyDomainResource(src, tgt);
    for (org.hl7.fhir.instance.model.Identifier t : src.getIdentifier())
      tgt.addIdentifier(convertIdentifier(t));
    tgt.setStatus(convertObservationStatus(src.getStatus()));
    tgt.addCategory(convertCodeableConcept(src.getCategory()));
    tgt.setCode(convertCodeableConcept(src.getCode()));
    tgt.setSubject(convertReference(src.getSubject()));
    tgt.setContext(convertReference(src.getEncounter()));
    tgt.setEffective(convertType(src.getEffective()));
    tgt.setIssued(src.getIssued());
    for (org.hl7.fhir.instance.model.Reference t : src.getPerformer())
      tgt.addPerformer(convertReference(t));
    tgt.setValue(convertType(src.getValue()));
    tgt.setDataAbsentReason(convertCodeableConcept(src.getDataAbsentReason()));
    tgt.setInterpretation(convertCodeableConcept(src.getInterpretation()));
    tgt.setComment(src.getComments());
    tgt.setBodySite(convertCodeableConcept(src.getBodySite()));
    tgt.setMethod(convertCodeableConcept(src.getMethod()));
    tgt.setSpecimen(convertReference(src.getSpecimen()));
    tgt.setDevice(convertReference(src.getDevice()));
    for (org.hl7.fhir.instance.model.Observation.ObservationReferenceRangeComponent t : src.getReferenceRange())
      tgt.addReferenceRange(convertObservationReferenceRangeComponent(t));
    for (org.hl7.fhir.instance.model.Observation.ObservationRelatedComponent t : src.getRelated())
      tgt.addRelated(convertObservationRelatedComponent(t));
    for (org.hl7.fhir.instance.model.Observation.ObservationComponentComponent t : src.getComponent())
      tgt.addComponent(convertObservationComponentComponent(t));
    return tgt;
  }

  public org.hl7.fhir.instance.model.Observation convertObservation(org.hl7.fhir.dstu3.model.Observation src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.Observation tgt = new org.hl7.fhir.instance.model.Observation();
    copyDomainResource(src, tgt);
    for (org.hl7.fhir.dstu3.model.Identifier t : src.getIdentifier())
      tgt.addIdentifier(convertIdentifier(t));
    tgt.setStatus(convertObservationStatus(src.getStatus()));
    for (org.hl7.fhir.dstu3.model.CodeableConcept c : src.getCategory())
      tgt.setCategory(convertCodeableConcept(c));
    tgt.setCode(convertCodeableConcept(src.getCode()));
    tgt.setSubject(convertReference(src.getSubject()));
    tgt.setEncounter(convertReference(src.getContext()));
    tgt.setEffective(convertType(src.getEffective()));
    tgt.setIssued(src.getIssued());
    for (org.hl7.fhir.dstu3.model.Reference t : src.getPerformer())
      tgt.addPerformer(convertReference(t));
    tgt.setValue(convertType(src.getValue()));
    tgt.setDataAbsentReason(convertCodeableConcept(src.getDataAbsentReason()));
    tgt.setInterpretation(convertCodeableConcept(src.getInterpretation()));
    tgt.setComments(src.getComment());
    tgt.setBodySite(convertCodeableConcept(src.getBodySite()));
    tgt.setMethod(convertCodeableConcept(src.getMethod()));
    tgt.setSpecimen(convertReference(src.getSpecimen()));
    tgt.setDevice(convertReference(src.getDevice()));
    for (org.hl7.fhir.dstu3.model.Observation.ObservationReferenceRangeComponent t : src.getReferenceRange())
      tgt.addReferenceRange(convertObservationReferenceRangeComponent(t));
    for (org.hl7.fhir.dstu3.model.Observation.ObservationRelatedComponent t : src.getRelated())
      tgt.addRelated(convertObservationRelatedComponent(t));
    for (org.hl7.fhir.dstu3.model.Observation.ObservationComponentComponent t : src.getComponent())
      tgt.addComponent(convertObservationComponentComponent(t));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.Observation.ObservationStatus convertObservationStatus(org.hl7.fhir.instance.model.Observation.ObservationStatus src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case REGISTERED: return org.hl7.fhir.dstu3.model.Observation.ObservationStatus.REGISTERED;
    case PRELIMINARY: return org.hl7.fhir.dstu3.model.Observation.ObservationStatus.PRELIMINARY;
    case FINAL: return org.hl7.fhir.dstu3.model.Observation.ObservationStatus.FINAL;
    case AMENDED: return org.hl7.fhir.dstu3.model.Observation.ObservationStatus.AMENDED;
    case CANCELLED: return org.hl7.fhir.dstu3.model.Observation.ObservationStatus.CANCELLED;
    case ENTEREDINERROR: return org.hl7.fhir.dstu3.model.Observation.ObservationStatus.ENTEREDINERROR;
    case UNKNOWN: return org.hl7.fhir.dstu3.model.Observation.ObservationStatus.UNKNOWN;
    default: return org.hl7.fhir.dstu3.model.Observation.ObservationStatus.NULL;
    }
  }

  public org.hl7.fhir.instance.model.Observation.ObservationStatus convertObservationStatus(org.hl7.fhir.dstu3.model.Observation.ObservationStatus src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case REGISTERED: return org.hl7.fhir.instance.model.Observation.ObservationStatus.REGISTERED;
    case PRELIMINARY: return org.hl7.fhir.instance.model.Observation.ObservationStatus.PRELIMINARY;
    case FINAL: return org.hl7.fhir.instance.model.Observation.ObservationStatus.FINAL;
    case AMENDED: return org.hl7.fhir.instance.model.Observation.ObservationStatus.AMENDED;
    case CANCELLED: return org.hl7.fhir.instance.model.Observation.ObservationStatus.CANCELLED;
    case ENTEREDINERROR: return org.hl7.fhir.instance.model.Observation.ObservationStatus.ENTEREDINERROR;
    case UNKNOWN: return org.hl7.fhir.instance.model.Observation.ObservationStatus.UNKNOWN;
    default: return org.hl7.fhir.instance.model.Observation.ObservationStatus.NULL;
    }
  }

  public org.hl7.fhir.dstu3.model.Observation.ObservationReferenceRangeComponent convertObservationReferenceRangeComponent(org.hl7.fhir.instance.model.Observation.ObservationReferenceRangeComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.Observation.ObservationReferenceRangeComponent tgt = new org.hl7.fhir.dstu3.model.Observation.ObservationReferenceRangeComponent();
    copyElement(src, tgt);
    tgt.setLow(convertSimpleQuantity(src.getLow()));
    tgt.setHigh(convertSimpleQuantity(src.getHigh()));
    tgt.setType(convertCodeableConcept(src.getMeaning()));
    tgt.setAge(convertRange(src.getAge()));
    tgt.setText(src.getText());
    return tgt;
  }

  public org.hl7.fhir.instance.model.Observation.ObservationReferenceRangeComponent convertObservationReferenceRangeComponent(org.hl7.fhir.dstu3.model.Observation.ObservationReferenceRangeComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.Observation.ObservationReferenceRangeComponent tgt = new org.hl7.fhir.instance.model.Observation.ObservationReferenceRangeComponent();
    copyElement(src, tgt);
    tgt.setLow(convertSimpleQuantity(src.getLow()));
    tgt.setHigh(convertSimpleQuantity(src.getHigh()));
//    for (org.hl7.fhir.dstu3.model.CodeableConcept c : src.getMeaning())
      tgt.setMeaning(convertCodeableConcept(src.getType()));
    tgt.setAge(convertRange(src.getAge()));
    tgt.setText(src.getText());
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.Observation.ObservationRelatedComponent convertObservationRelatedComponent(org.hl7.fhir.instance.model.Observation.ObservationRelatedComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.Observation.ObservationRelatedComponent tgt = new org.hl7.fhir.dstu3.model.Observation.ObservationRelatedComponent();
    copyElement(src, tgt);
    tgt.setType(convertObservationRelationshipType(src.getType()));
    tgt.setTarget(convertReference(src.getTarget()));
    return tgt;
  }

  public org.hl7.fhir.instance.model.Observation.ObservationRelatedComponent convertObservationRelatedComponent(org.hl7.fhir.dstu3.model.Observation.ObservationRelatedComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.Observation.ObservationRelatedComponent tgt = new org.hl7.fhir.instance.model.Observation.ObservationRelatedComponent();
    copyElement(src, tgt);
    tgt.setType(convertObservationRelationshipType(src.getType()));
    tgt.setTarget(convertReference(src.getTarget()));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.Observation.ObservationRelationshipType convertObservationRelationshipType(org.hl7.fhir.instance.model.Observation.ObservationRelationshipType src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case HASMEMBER: return org.hl7.fhir.dstu3.model.Observation.ObservationRelationshipType.HASMEMBER;
    case DERIVEDFROM: return org.hl7.fhir.dstu3.model.Observation.ObservationRelationshipType.DERIVEDFROM;
    case SEQUELTO: return org.hl7.fhir.dstu3.model.Observation.ObservationRelationshipType.SEQUELTO;
    case REPLACES: return org.hl7.fhir.dstu3.model.Observation.ObservationRelationshipType.REPLACES;
    case QUALIFIEDBY: return org.hl7.fhir.dstu3.model.Observation.ObservationRelationshipType.QUALIFIEDBY;
    case INTERFEREDBY: return org.hl7.fhir.dstu3.model.Observation.ObservationRelationshipType.INTERFEREDBY;
    default: return org.hl7.fhir.dstu3.model.Observation.ObservationRelationshipType.NULL;
    }
  }

  public org.hl7.fhir.instance.model.Observation.ObservationRelationshipType convertObservationRelationshipType(org.hl7.fhir.dstu3.model.Observation.ObservationRelationshipType src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case HASMEMBER: return org.hl7.fhir.instance.model.Observation.ObservationRelationshipType.HASMEMBER;
    case DERIVEDFROM: return org.hl7.fhir.instance.model.Observation.ObservationRelationshipType.DERIVEDFROM;
    case SEQUELTO: return org.hl7.fhir.instance.model.Observation.ObservationRelationshipType.SEQUELTO;
    case REPLACES: return org.hl7.fhir.instance.model.Observation.ObservationRelationshipType.REPLACES;
    case QUALIFIEDBY: return org.hl7.fhir.instance.model.Observation.ObservationRelationshipType.QUALIFIEDBY;
    case INTERFEREDBY: return org.hl7.fhir.instance.model.Observation.ObservationRelationshipType.INTERFEREDBY;
    default: return org.hl7.fhir.instance.model.Observation.ObservationRelationshipType.NULL;
    }
  }

  public org.hl7.fhir.dstu3.model.Observation.ObservationComponentComponent convertObservationComponentComponent(org.hl7.fhir.instance.model.Observation.ObservationComponentComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.Observation.ObservationComponentComponent tgt = new org.hl7.fhir.dstu3.model.Observation.ObservationComponentComponent();
    copyElement(src, tgt);
    tgt.setCode(convertCodeableConcept(src.getCode()));
    tgt.setValue(convertType(src.getValue()));
    tgt.setDataAbsentReason(convertCodeableConcept(src.getDataAbsentReason()));
    for (org.hl7.fhir.instance.model.Observation.ObservationReferenceRangeComponent t : src.getReferenceRange())
      tgt.addReferenceRange(convertObservationReferenceRangeComponent(t));
    return tgt;
  }

  public org.hl7.fhir.instance.model.Observation.ObservationComponentComponent convertObservationComponentComponent(org.hl7.fhir.dstu3.model.Observation.ObservationComponentComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.Observation.ObservationComponentComponent tgt = new org.hl7.fhir.instance.model.Observation.ObservationComponentComponent();
    copyElement(src, tgt);
    tgt.setCode(convertCodeableConcept(src.getCode()));
    tgt.setValue(convertType(src.getValue()));
    tgt.setDataAbsentReason(convertCodeableConcept(src.getDataAbsentReason()));
    for (org.hl7.fhir.dstu3.model.Observation.ObservationReferenceRangeComponent t : src.getReferenceRange())
      tgt.addReferenceRange(convertObservationReferenceRangeComponent(t));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.OperationDefinition convertOperationDefinition(org.hl7.fhir.instance.model.OperationDefinition src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.OperationDefinition tgt = new org.hl7.fhir.dstu3.model.OperationDefinition();
    copyDomainResource(src, tgt);
    tgt.setUrl(src.getUrl());
    tgt.setVersion(src.getVersion());
    tgt.setName(src.getName());
    tgt.setStatus(convertConformanceResourceStatus(src.getStatus()));
    tgt.setKind(convertOperationKind(src.getKind()));
    if (src.hasExperimental())
      tgt.setExperimental(src.getExperimental());
    if (src.hasDate())
      tgt.setDate(src.getDate());
    tgt.setPublisher(src.getPublisher());
    for (org.hl7.fhir.instance.model.OperationDefinition.OperationDefinitionContactComponent t : src.getContact())
      tgt.addContact(convertOperationDefinitionContactComponent(t));
    tgt.setDescription(src.getDescription());
    tgt.setPurpose(src.getRequirements());
    if (src.hasIdempotent())
      tgt.setIdempotent(src.getIdempotent());
    tgt.setCode(src.getCode());
    tgt.setComment(src.getNotes());
    tgt.setBase(convertReference(src.getBase()));
    tgt.setSystem(src.getSystem());
    for (org.hl7.fhir.instance.model.CodeType t : src.getType())
      tgt.addResource(t.getValue());
    tgt.setType(tgt.hasResource());
    tgt.setInstance(src.getInstance());
    for (org.hl7.fhir.instance.model.OperationDefinition.OperationDefinitionParameterComponent t : src.getParameter())
      tgt.addParameter(convertOperationDefinitionParameterComponent(t));
    return tgt;
  }

  public org.hl7.fhir.instance.model.OperationDefinition convertOperationDefinition(org.hl7.fhir.dstu3.model.OperationDefinition src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.OperationDefinition tgt = new org.hl7.fhir.instance.model.OperationDefinition();
    copyDomainResource(src, tgt);
    tgt.setUrl(src.getUrl());
    tgt.setVersion(src.getVersion());
    tgt.setName(src.getName());
    tgt.setStatus(convertConformanceResourceStatus(src.getStatus()));
    tgt.setKind(convertOperationKind(src.getKind()));
    if (src.hasExperimental())
      tgt.setExperimental(src.getExperimental());
    if (src.hasDate())
      tgt.setDate(src.getDate());
    tgt.setPublisher(src.getPublisher());
    for (org.hl7.fhir.dstu3.model.ContactDetail t : src.getContact())
      tgt.addContact(convertOperationDefinitionContactComponent(t));
    tgt.setDescription(src.getDescription());
    tgt.setRequirements(src.getPurpose());
    tgt.setIdempotent(src.getIdempotent());
    tgt.setCode(src.getCode());
    tgt.setNotes(src.getComment());
    if (src.hasBase())
      tgt.setBase(convertReference(src.getBase()));
    tgt.setSystem(src.getSystem());
    if (src.getType())
      for (org.hl7.fhir.dstu3.model.CodeType t : src.getResource())
        tgt.addType(t.getValue());
    tgt.setInstance(src.getInstance());
    for (org.hl7.fhir.dstu3.model.OperationDefinition.OperationDefinitionParameterComponent t : src.getParameter())
      tgt.addParameter(convertOperationDefinitionParameterComponent(t));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.OperationDefinition.OperationKind convertOperationKind(org.hl7.fhir.instance.model.OperationDefinition.OperationKind src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case OPERATION: return org.hl7.fhir.dstu3.model.OperationDefinition.OperationKind.OPERATION;
    case QUERY: return org.hl7.fhir.dstu3.model.OperationDefinition.OperationKind.QUERY;
    default: return org.hl7.fhir.dstu3.model.OperationDefinition.OperationKind.NULL;
    }
  }

  public org.hl7.fhir.instance.model.OperationDefinition.OperationKind convertOperationKind(org.hl7.fhir.dstu3.model.OperationDefinition.OperationKind src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case OPERATION: return org.hl7.fhir.instance.model.OperationDefinition.OperationKind.OPERATION;
    case QUERY: return org.hl7.fhir.instance.model.OperationDefinition.OperationKind.QUERY;
    default: return org.hl7.fhir.instance.model.OperationDefinition.OperationKind.NULL;
    }
  }

  public org.hl7.fhir.dstu3.model.ContactDetail convertOperationDefinitionContactComponent(org.hl7.fhir.instance.model.OperationDefinition.OperationDefinitionContactComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.ContactDetail tgt = new org.hl7.fhir.dstu3.model.ContactDetail();
    copyElement(src, tgt);
    tgt.setName(src.getName());
    for (org.hl7.fhir.instance.model.ContactPoint t : src.getTelecom())
      tgt.addTelecom(convertContactPoint(t));
    return tgt;
  }

  public org.hl7.fhir.instance.model.OperationDefinition.OperationDefinitionContactComponent convertOperationDefinitionContactComponent(org.hl7.fhir.dstu3.model.ContactDetail src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.OperationDefinition.OperationDefinitionContactComponent tgt = new org.hl7.fhir.instance.model.OperationDefinition.OperationDefinitionContactComponent();
    copyElement(src, tgt);
    tgt.setName(src.getName());
    for (org.hl7.fhir.dstu3.model.ContactPoint t : src.getTelecom())
      tgt.addTelecom(convertContactPoint(t));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.OperationDefinition.OperationDefinitionParameterComponent convertOperationDefinitionParameterComponent(org.hl7.fhir.instance.model.OperationDefinition.OperationDefinitionParameterComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.OperationDefinition.OperationDefinitionParameterComponent tgt = new org.hl7.fhir.dstu3.model.OperationDefinition.OperationDefinitionParameterComponent();
    copyElement(src, tgt);
    tgt.setName(src.getName());
    tgt.setUse(convertOperationParameterUse(src.getUse()));
    tgt.setMin(src.getMin());
    tgt.setMax(src.getMax());
    tgt.setDocumentation(src.getDocumentation());
    tgt.setType(src.getType());
    tgt.setProfile(convertReference(src.getProfile()));
    tgt.setBinding(convertOperationDefinitionParameterBindingComponent(src.getBinding()));
    for (org.hl7.fhir.instance.model.OperationDefinition.OperationDefinitionParameterComponent t : src.getPart())
      tgt.addPart(convertOperationDefinitionParameterComponent(t));
    return tgt;
  }

  public org.hl7.fhir.instance.model.OperationDefinition.OperationDefinitionParameterComponent convertOperationDefinitionParameterComponent(org.hl7.fhir.dstu3.model.OperationDefinition.OperationDefinitionParameterComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.OperationDefinition.OperationDefinitionParameterComponent tgt = new org.hl7.fhir.instance.model.OperationDefinition.OperationDefinitionParameterComponent();
    copyElement(src, tgt);
    tgt.setName(src.getName());
    tgt.setUse(convertOperationParameterUse(src.getUse()));
    tgt.setMin(src.getMin());
    tgt.setMax(src.getMax());
    tgt.setDocumentation(src.getDocumentation());
    if (src.hasSearchType()) {
      tgt.setType(src.getSearchType().toCode());
      tgt.setType("string");
    } else
      tgt.setType(src.getType());
    if (src.hasProfile())
      tgt.setProfile(convertReference(src.getProfile()));
    if (src.hasBinding())
      tgt.setBinding(convertOperationDefinitionParameterBindingComponent(src.getBinding()));
    for (org.hl7.fhir.dstu3.model.OperationDefinition.OperationDefinitionParameterComponent t : src.getPart())
      tgt.addPart(convertOperationDefinitionParameterComponent(t));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.OperationDefinition.OperationParameterUse convertOperationParameterUse(org.hl7.fhir.instance.model.OperationDefinition.OperationParameterUse src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case IN: return org.hl7.fhir.dstu3.model.OperationDefinition.OperationParameterUse.IN;
    case OUT: return org.hl7.fhir.dstu3.model.OperationDefinition.OperationParameterUse.OUT;
    default: return org.hl7.fhir.dstu3.model.OperationDefinition.OperationParameterUse.NULL;
    }
  }

  public org.hl7.fhir.instance.model.OperationDefinition.OperationParameterUse convertOperationParameterUse(org.hl7.fhir.dstu3.model.OperationDefinition.OperationParameterUse src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case IN: return org.hl7.fhir.instance.model.OperationDefinition.OperationParameterUse.IN;
    case OUT: return org.hl7.fhir.instance.model.OperationDefinition.OperationParameterUse.OUT;
    default: return org.hl7.fhir.instance.model.OperationDefinition.OperationParameterUse.NULL;
    }
  }

  public org.hl7.fhir.dstu3.model.OperationDefinition.OperationDefinitionParameterBindingComponent convertOperationDefinitionParameterBindingComponent(org.hl7.fhir.instance.model.OperationDefinition.OperationDefinitionParameterBindingComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.OperationDefinition.OperationDefinitionParameterBindingComponent tgt = new org.hl7.fhir.dstu3.model.OperationDefinition.OperationDefinitionParameterBindingComponent();
    copyElement(src, tgt);
    tgt.setStrength(convertBindingStrength(src.getStrength()));
    tgt.setValueSet(convertType(src.getValueSet()));
    return tgt;
  }

  public org.hl7.fhir.instance.model.OperationDefinition.OperationDefinitionParameterBindingComponent convertOperationDefinitionParameterBindingComponent(org.hl7.fhir.dstu3.model.OperationDefinition.OperationDefinitionParameterBindingComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.OperationDefinition.OperationDefinitionParameterBindingComponent tgt = new org.hl7.fhir.instance.model.OperationDefinition.OperationDefinitionParameterBindingComponent();
    copyElement(src, tgt);
    tgt.setStrength(convertBindingStrength(src.getStrength()));
    tgt.setValueSet(convertType(src.getValueSet()));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.OperationOutcome convertOperationOutcome(org.hl7.fhir.instance.model.OperationOutcome src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.OperationOutcome tgt = new org.hl7.fhir.dstu3.model.OperationOutcome();
    copyDomainResource(src, tgt);
    for (org.hl7.fhir.instance.model.OperationOutcome.OperationOutcomeIssueComponent t : src.getIssue())
      tgt.addIssue(convertOperationOutcomeIssueComponent(t));
    return tgt;
  }

  public org.hl7.fhir.instance.model.OperationOutcome convertOperationOutcome(org.hl7.fhir.dstu3.model.OperationOutcome src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.OperationOutcome tgt = new org.hl7.fhir.instance.model.OperationOutcome();
    copyDomainResource(src, tgt);
    for (org.hl7.fhir.dstu3.model.OperationOutcome.OperationOutcomeIssueComponent t : src.getIssue())
      tgt.addIssue(convertOperationOutcomeIssueComponent(t));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.OperationOutcome.OperationOutcomeIssueComponent convertOperationOutcomeIssueComponent(org.hl7.fhir.instance.model.OperationOutcome.OperationOutcomeIssueComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.OperationOutcome.OperationOutcomeIssueComponent tgt = new org.hl7.fhir.dstu3.model.OperationOutcome.OperationOutcomeIssueComponent();
    copyElement(src, tgt);
    tgt.setSeverity(convertIssueSeverity(src.getSeverity()));
    tgt.setCode(convertIssueType(src.getCode()));
    tgt.setDetails(convertCodeableConcept(src.getDetails()));
    tgt.setDiagnostics(src.getDiagnostics());
    for (org.hl7.fhir.instance.model.StringType t : src.getLocation())
      tgt.addLocation(t.getValue());
    return tgt;
  }

  public org.hl7.fhir.instance.model.OperationOutcome.OperationOutcomeIssueComponent convertOperationOutcomeIssueComponent(org.hl7.fhir.dstu3.model.OperationOutcome.OperationOutcomeIssueComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.OperationOutcome.OperationOutcomeIssueComponent tgt = new org.hl7.fhir.instance.model.OperationOutcome.OperationOutcomeIssueComponent();
    copyElement(src, tgt);
    tgt.setSeverity(convertIssueSeverity(src.getSeverity()));
    tgt.setCode(convertIssueType(src.getCode()));
    tgt.setDetails(convertCodeableConcept(src.getDetails()));
    tgt.setDiagnostics(src.getDiagnostics());
    for (org.hl7.fhir.dstu3.model.StringType t : src.getLocation())
      tgt.addLocation(t.getValue());
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.OperationOutcome.IssueSeverity convertIssueSeverity(org.hl7.fhir.instance.model.OperationOutcome.IssueSeverity src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case FATAL: return org.hl7.fhir.dstu3.model.OperationOutcome.IssueSeverity.FATAL;
    case ERROR: return org.hl7.fhir.dstu3.model.OperationOutcome.IssueSeverity.ERROR;
    case WARNING: return org.hl7.fhir.dstu3.model.OperationOutcome.IssueSeverity.WARNING;
    case INFORMATION: return org.hl7.fhir.dstu3.model.OperationOutcome.IssueSeverity.INFORMATION;
    default: return org.hl7.fhir.dstu3.model.OperationOutcome.IssueSeverity.NULL;
    }
  }

  public org.hl7.fhir.instance.model.OperationOutcome.IssueSeverity convertIssueSeverity(org.hl7.fhir.dstu3.model.OperationOutcome.IssueSeverity src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case FATAL: return org.hl7.fhir.instance.model.OperationOutcome.IssueSeverity.FATAL;
    case ERROR: return org.hl7.fhir.instance.model.OperationOutcome.IssueSeverity.ERROR;
    case WARNING: return org.hl7.fhir.instance.model.OperationOutcome.IssueSeverity.WARNING;
    case INFORMATION: return org.hl7.fhir.instance.model.OperationOutcome.IssueSeverity.INFORMATION;
    default: return org.hl7.fhir.instance.model.OperationOutcome.IssueSeverity.NULL;
    }
  }

  public org.hl7.fhir.dstu3.model.OperationOutcome.IssueType convertIssueType(org.hl7.fhir.instance.model.OperationOutcome.IssueType src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case INVALID: return org.hl7.fhir.dstu3.model.OperationOutcome.IssueType.INVALID;
    case STRUCTURE: return org.hl7.fhir.dstu3.model.OperationOutcome.IssueType.STRUCTURE;
    case REQUIRED: return org.hl7.fhir.dstu3.model.OperationOutcome.IssueType.REQUIRED;
    case VALUE: return org.hl7.fhir.dstu3.model.OperationOutcome.IssueType.VALUE;
    case INVARIANT: return org.hl7.fhir.dstu3.model.OperationOutcome.IssueType.INVARIANT;
    case SECURITY: return org.hl7.fhir.dstu3.model.OperationOutcome.IssueType.SECURITY;
    case LOGIN: return org.hl7.fhir.dstu3.model.OperationOutcome.IssueType.LOGIN;
    case UNKNOWN: return org.hl7.fhir.dstu3.model.OperationOutcome.IssueType.UNKNOWN;
    case EXPIRED: return org.hl7.fhir.dstu3.model.OperationOutcome.IssueType.EXPIRED;
    case FORBIDDEN: return org.hl7.fhir.dstu3.model.OperationOutcome.IssueType.FORBIDDEN;
    case SUPPRESSED: return org.hl7.fhir.dstu3.model.OperationOutcome.IssueType.SUPPRESSED;
    case PROCESSING: return org.hl7.fhir.dstu3.model.OperationOutcome.IssueType.PROCESSING;
    case NOTSUPPORTED: return org.hl7.fhir.dstu3.model.OperationOutcome.IssueType.NOTSUPPORTED;
    case DUPLICATE: return org.hl7.fhir.dstu3.model.OperationOutcome.IssueType.DUPLICATE;
    case NOTFOUND: return org.hl7.fhir.dstu3.model.OperationOutcome.IssueType.NOTFOUND;
    case TOOLONG: return org.hl7.fhir.dstu3.model.OperationOutcome.IssueType.TOOLONG;
    case CODEINVALID: return org.hl7.fhir.dstu3.model.OperationOutcome.IssueType.CODEINVALID;
    case EXTENSION: return org.hl7.fhir.dstu3.model.OperationOutcome.IssueType.EXTENSION;
    case TOOCOSTLY: return org.hl7.fhir.dstu3.model.OperationOutcome.IssueType.TOOCOSTLY;
    case BUSINESSRULE: return org.hl7.fhir.dstu3.model.OperationOutcome.IssueType.BUSINESSRULE;
    case CONFLICT: return org.hl7.fhir.dstu3.model.OperationOutcome.IssueType.CONFLICT;
    case INCOMPLETE: return org.hl7.fhir.dstu3.model.OperationOutcome.IssueType.INCOMPLETE;
    case TRANSIENT: return org.hl7.fhir.dstu3.model.OperationOutcome.IssueType.TRANSIENT;
    case LOCKERROR: return org.hl7.fhir.dstu3.model.OperationOutcome.IssueType.LOCKERROR;
    case NOSTORE: return org.hl7.fhir.dstu3.model.OperationOutcome.IssueType.NOSTORE;
    case EXCEPTION: return org.hl7.fhir.dstu3.model.OperationOutcome.IssueType.EXCEPTION;
    case TIMEOUT: return org.hl7.fhir.dstu3.model.OperationOutcome.IssueType.TIMEOUT;
    case THROTTLED: return org.hl7.fhir.dstu3.model.OperationOutcome.IssueType.THROTTLED;
    case INFORMATIONAL: return org.hl7.fhir.dstu3.model.OperationOutcome.IssueType.INFORMATIONAL;
    default: return org.hl7.fhir.dstu3.model.OperationOutcome.IssueType.NULL;
    }
  }

  public org.hl7.fhir.instance.model.OperationOutcome.IssueType convertIssueType(org.hl7.fhir.dstu3.model.OperationOutcome.IssueType src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case INVALID: return org.hl7.fhir.instance.model.OperationOutcome.IssueType.INVALID;
    case STRUCTURE: return org.hl7.fhir.instance.model.OperationOutcome.IssueType.STRUCTURE;
    case REQUIRED: return org.hl7.fhir.instance.model.OperationOutcome.IssueType.REQUIRED;
    case VALUE: return org.hl7.fhir.instance.model.OperationOutcome.IssueType.VALUE;
    case INVARIANT: return org.hl7.fhir.instance.model.OperationOutcome.IssueType.INVARIANT;
    case SECURITY: return org.hl7.fhir.instance.model.OperationOutcome.IssueType.SECURITY;
    case LOGIN: return org.hl7.fhir.instance.model.OperationOutcome.IssueType.LOGIN;
    case UNKNOWN: return org.hl7.fhir.instance.model.OperationOutcome.IssueType.UNKNOWN;
    case EXPIRED: return org.hl7.fhir.instance.model.OperationOutcome.IssueType.EXPIRED;
    case FORBIDDEN: return org.hl7.fhir.instance.model.OperationOutcome.IssueType.FORBIDDEN;
    case SUPPRESSED: return org.hl7.fhir.instance.model.OperationOutcome.IssueType.SUPPRESSED;
    case PROCESSING: return org.hl7.fhir.instance.model.OperationOutcome.IssueType.PROCESSING;
    case NOTSUPPORTED: return org.hl7.fhir.instance.model.OperationOutcome.IssueType.NOTSUPPORTED;
    case DUPLICATE: return org.hl7.fhir.instance.model.OperationOutcome.IssueType.DUPLICATE;
    case NOTFOUND: return org.hl7.fhir.instance.model.OperationOutcome.IssueType.NOTFOUND;
    case TOOLONG: return org.hl7.fhir.instance.model.OperationOutcome.IssueType.TOOLONG;
    case CODEINVALID: return org.hl7.fhir.instance.model.OperationOutcome.IssueType.CODEINVALID;
    case EXTENSION: return org.hl7.fhir.instance.model.OperationOutcome.IssueType.EXTENSION;
    case TOOCOSTLY: return org.hl7.fhir.instance.model.OperationOutcome.IssueType.TOOCOSTLY;
    case BUSINESSRULE: return org.hl7.fhir.instance.model.OperationOutcome.IssueType.BUSINESSRULE;
    case CONFLICT: return org.hl7.fhir.instance.model.OperationOutcome.IssueType.CONFLICT;
    case INCOMPLETE: return org.hl7.fhir.instance.model.OperationOutcome.IssueType.INCOMPLETE;
    case TRANSIENT: return org.hl7.fhir.instance.model.OperationOutcome.IssueType.TRANSIENT;
    case LOCKERROR: return org.hl7.fhir.instance.model.OperationOutcome.IssueType.LOCKERROR;
    case NOSTORE: return org.hl7.fhir.instance.model.OperationOutcome.IssueType.NOSTORE;
    case EXCEPTION: return org.hl7.fhir.instance.model.OperationOutcome.IssueType.EXCEPTION;
    case TIMEOUT: return org.hl7.fhir.instance.model.OperationOutcome.IssueType.TIMEOUT;
    case THROTTLED: return org.hl7.fhir.instance.model.OperationOutcome.IssueType.THROTTLED;
    case INFORMATIONAL: return org.hl7.fhir.instance.model.OperationOutcome.IssueType.INFORMATIONAL;
    default: return org.hl7.fhir.instance.model.OperationOutcome.IssueType.NULL;
    }
  }


  public org.hl7.fhir.dstu3.model.Organization convertOrganization(org.hl7.fhir.instance.model.Organization src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.Organization tgt = new org.hl7.fhir.dstu3.model.Organization();
    copyDomainResource(src, tgt);
    for (org.hl7.fhir.instance.model.Identifier t : src.getIdentifier())
      tgt.addIdentifier(convertIdentifier(t));
    tgt.setActive(src.getActive());
    tgt.addType(convertCodeableConcept(src.getType()));
    tgt.setName(src.getName());
    for (org.hl7.fhir.instance.model.ContactPoint t : src.getTelecom())
      tgt.addTelecom(convertContactPoint(t));
    for (org.hl7.fhir.instance.model.Address t : src.getAddress())
      tgt.addAddress(convertAddress(t));
    tgt.setPartOf(convertReference(src.getPartOf()));
    for (org.hl7.fhir.instance.model.Organization.OrganizationContactComponent t : src.getContact())
      tgt.addContact(convertOrganizationContactComponent(t));
    return tgt;
  }

  public org.hl7.fhir.instance.model.Organization convertOrganization(org.hl7.fhir.dstu3.model.Organization src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.Organization tgt = new org.hl7.fhir.instance.model.Organization();
    copyDomainResource(src, tgt);
    for (org.hl7.fhir.dstu3.model.Identifier t : src.getIdentifier())
      tgt.addIdentifier(convertIdentifier(t));
    tgt.setActive(src.getActive());
    tgt.setType(convertCodeableConcept(src.getTypeFirstRep()));
    tgt.setName(src.getName());
    for (org.hl7.fhir.dstu3.model.ContactPoint t : src.getTelecom())
      tgt.addTelecom(convertContactPoint(t));
    for (org.hl7.fhir.dstu3.model.Address t : src.getAddress())
      tgt.addAddress(convertAddress(t));
    tgt.setPartOf(convertReference(src.getPartOf()));
    for (org.hl7.fhir.dstu3.model.Organization.OrganizationContactComponent t : src.getContact())
      tgt.addContact(convertOrganizationContactComponent(t));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.Organization.OrganizationContactComponent convertOrganizationContactComponent(org.hl7.fhir.instance.model.Organization.OrganizationContactComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.Organization.OrganizationContactComponent tgt = new org.hl7.fhir.dstu3.model.Organization.OrganizationContactComponent();
    copyElement(src, tgt);
    tgt.setPurpose(convertCodeableConcept(src.getPurpose()));
    tgt.setName(convertHumanName(src.getName()));
    for (org.hl7.fhir.instance.model.ContactPoint t : src.getTelecom())
      tgt.addTelecom(convertContactPoint(t));
    tgt.setAddress(convertAddress(src.getAddress()));
    return tgt;
  }

  public org.hl7.fhir.instance.model.Organization.OrganizationContactComponent convertOrganizationContactComponent(org.hl7.fhir.dstu3.model.Organization.OrganizationContactComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.Organization.OrganizationContactComponent tgt = new org.hl7.fhir.instance.model.Organization.OrganizationContactComponent();
    copyElement(src, tgt);
    tgt.setPurpose(convertCodeableConcept(src.getPurpose()));
    tgt.setName(convertHumanName(src.getName()));
    for (org.hl7.fhir.dstu3.model.ContactPoint t : src.getTelecom())
      tgt.addTelecom(convertContactPoint(t));
    tgt.setAddress(convertAddress(src.getAddress()));
    return tgt;
  }


  public org.hl7.fhir.dstu3.model.Patient convertPatient(org.hl7.fhir.instance.model.Patient src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.Patient tgt = new org.hl7.fhir.dstu3.model.Patient();
    copyDomainResource(src, tgt);
    for (org.hl7.fhir.instance.model.Identifier t : src.getIdentifier())
      tgt.addIdentifier(convertIdentifier(t));
    tgt.setActive(src.getActive());
    for (org.hl7.fhir.instance.model.HumanName t : src.getName())
      tgt.addName(convertHumanName(t));
    for (org.hl7.fhir.instance.model.ContactPoint t : src.getTelecom())
      tgt.addTelecom(convertContactPoint(t));
    tgt.setGender(convertAdministrativeGender(src.getGender()));
    tgt.setBirthDate(src.getBirthDate());
    tgt.setDeceased(convertType(src.getDeceased()));
    for (org.hl7.fhir.instance.model.Address t : src.getAddress())
      tgt.addAddress(convertAddress(t));
    tgt.setMaritalStatus(convertCodeableConcept(src.getMaritalStatus()));
    tgt.setMultipleBirth(convertType(src.getMultipleBirth()));
    for (org.hl7.fhir.instance.model.Attachment t : src.getPhoto())
      tgt.addPhoto(convertAttachment(t));
    for (org.hl7.fhir.instance.model.Patient.ContactComponent t : src.getContact())
      tgt.addContact(convertContactComponent(t));
    tgt.setAnimal(convertAnimalComponent(src.getAnimal()));
    for (org.hl7.fhir.instance.model.Patient.PatientCommunicationComponent t : src.getCommunication())
      tgt.addCommunication(convertPatientCommunicationComponent(t));
    for (org.hl7.fhir.instance.model.Reference t : src.getCareProvider())
      tgt.addGeneralPractitioner(convertReference(t));
    tgt.setManagingOrganization(convertReference(src.getManagingOrganization()));
    for (org.hl7.fhir.instance.model.Patient.PatientLinkComponent t : src.getLink())
      tgt.addLink(convertPatientLinkComponent(t));
    return tgt;
  }

  public org.hl7.fhir.instance.model.Patient convertPatient(org.hl7.fhir.dstu3.model.Patient src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.Patient tgt = new org.hl7.fhir.instance.model.Patient();
    copyDomainResource(src, tgt);
    for (org.hl7.fhir.dstu3.model.Identifier t : src.getIdentifier())
      tgt.addIdentifier(convertIdentifier(t));
    tgt.setActive(src.getActive());
    for (org.hl7.fhir.dstu3.model.HumanName t : src.getName())
      tgt.addName(convertHumanName(t));
    for (org.hl7.fhir.dstu3.model.ContactPoint t : src.getTelecom())
      tgt.addTelecom(convertContactPoint(t));
    tgt.setGender(convertAdministrativeGender(src.getGender()));
    tgt.setBirthDate(src.getBirthDate());
    tgt.setDeceased(convertType(src.getDeceased()));
    for (org.hl7.fhir.dstu3.model.Address t : src.getAddress())
      tgt.addAddress(convertAddress(t));
    tgt.setMaritalStatus(convertCodeableConcept(src.getMaritalStatus()));
    tgt.setMultipleBirth(convertType(src.getMultipleBirth()));
    for (org.hl7.fhir.dstu3.model.Attachment t : src.getPhoto())
      tgt.addPhoto(convertAttachment(t));
    for (org.hl7.fhir.dstu3.model.Patient.ContactComponent t : src.getContact())
      tgt.addContact(convertContactComponent(t));
    tgt.setAnimal(convertAnimalComponent(src.getAnimal()));
    for (org.hl7.fhir.dstu3.model.Patient.PatientCommunicationComponent t : src.getCommunication())
      tgt.addCommunication(convertPatientCommunicationComponent(t));
    for (org.hl7.fhir.dstu3.model.Reference t : src.getGeneralPractitioner())
      tgt.addCareProvider(convertReference(t));
    tgt.setManagingOrganization(convertReference(src.getManagingOrganization()));
    for (org.hl7.fhir.dstu3.model.Patient.PatientLinkComponent t : src.getLink())
      tgt.addLink(convertPatientLinkComponent(t));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.Patient.ContactComponent convertContactComponent(org.hl7.fhir.instance.model.Patient.ContactComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.Patient.ContactComponent tgt = new org.hl7.fhir.dstu3.model.Patient.ContactComponent();
    copyElement(src, tgt);
    for (org.hl7.fhir.instance.model.CodeableConcept t : src.getRelationship())
      tgt.addRelationship(convertCodeableConcept(t));
    tgt.setName(convertHumanName(src.getName()));
    for (org.hl7.fhir.instance.model.ContactPoint t : src.getTelecom())
      tgt.addTelecom(convertContactPoint(t));
    tgt.setAddress(convertAddress(src.getAddress()));
    tgt.setGender(convertAdministrativeGender(src.getGender()));
    tgt.setOrganization(convertReference(src.getOrganization()));
    tgt.setPeriod(convertPeriod(src.getPeriod()));
    return tgt;
  }

  public org.hl7.fhir.instance.model.Patient.ContactComponent convertContactComponent(org.hl7.fhir.dstu3.model.Patient.ContactComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.Patient.ContactComponent tgt = new org.hl7.fhir.instance.model.Patient.ContactComponent();
    copyElement(src, tgt);
    for (org.hl7.fhir.dstu3.model.CodeableConcept t : src.getRelationship())
      tgt.addRelationship(convertCodeableConcept(t));
    tgt.setName(convertHumanName(src.getName()));
    for (org.hl7.fhir.dstu3.model.ContactPoint t : src.getTelecom())
      tgt.addTelecom(convertContactPoint(t));
    tgt.setAddress(convertAddress(src.getAddress()));
    tgt.setGender(convertAdministrativeGender(src.getGender()));
    tgt.setOrganization(convertReference(src.getOrganization()));
    tgt.setPeriod(convertPeriod(src.getPeriod()));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.Patient.AnimalComponent convertAnimalComponent(org.hl7.fhir.instance.model.Patient.AnimalComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.Patient.AnimalComponent tgt = new org.hl7.fhir.dstu3.model.Patient.AnimalComponent();
    copyElement(src, tgt);
    tgt.setSpecies(convertCodeableConcept(src.getSpecies()));
    tgt.setBreed(convertCodeableConcept(src.getBreed()));
    tgt.setGenderStatus(convertCodeableConcept(src.getGenderStatus()));
    return tgt;
  }

  public org.hl7.fhir.instance.model.Patient.AnimalComponent convertAnimalComponent(org.hl7.fhir.dstu3.model.Patient.AnimalComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.Patient.AnimalComponent tgt = new org.hl7.fhir.instance.model.Patient.AnimalComponent();
    copyElement(src, tgt);
    tgt.setSpecies(convertCodeableConcept(src.getSpecies()));
    tgt.setBreed(convertCodeableConcept(src.getBreed()));
    tgt.setGenderStatus(convertCodeableConcept(src.getGenderStatus()));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.Patient.PatientCommunicationComponent convertPatientCommunicationComponent(org.hl7.fhir.instance.model.Patient.PatientCommunicationComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.Patient.PatientCommunicationComponent tgt = new org.hl7.fhir.dstu3.model.Patient.PatientCommunicationComponent();
    copyElement(src, tgt);
    tgt.setLanguage(convertCodeableConcept(src.getLanguage()));
    tgt.setPreferred(src.getPreferred());
    return tgt;
  }

  public org.hl7.fhir.instance.model.Patient.PatientCommunicationComponent convertPatientCommunicationComponent(org.hl7.fhir.dstu3.model.Patient.PatientCommunicationComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.Patient.PatientCommunicationComponent tgt = new org.hl7.fhir.instance.model.Patient.PatientCommunicationComponent();
    copyElement(src, tgt);
    tgt.setLanguage(convertCodeableConcept(src.getLanguage()));
    tgt.setPreferred(src.getPreferred());
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.Patient.PatientLinkComponent convertPatientLinkComponent(org.hl7.fhir.instance.model.Patient.PatientLinkComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.Patient.PatientLinkComponent tgt = new org.hl7.fhir.dstu3.model.Patient.PatientLinkComponent();
    copyElement(src, tgt);
    tgt.setOther(convertReference(src.getOther()));
    tgt.setType(convertLinkType(src.getType()));
    return tgt;
  }

  public org.hl7.fhir.instance.model.Patient.PatientLinkComponent convertPatientLinkComponent(org.hl7.fhir.dstu3.model.Patient.PatientLinkComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.Patient.PatientLinkComponent tgt = new org.hl7.fhir.instance.model.Patient.PatientLinkComponent();
    copyElement(src, tgt);
    tgt.setOther(convertReference(src.getOther()));
    tgt.setType(convertLinkType(src.getType()));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.Patient.LinkType convertLinkType(org.hl7.fhir.instance.model.Patient.LinkType src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case REPLACE: return org.hl7.fhir.dstu3.model.Patient.LinkType.REPLACEDBY;
    case REFER: return org.hl7.fhir.dstu3.model.Patient.LinkType.REFER;
    case SEEALSO: return org.hl7.fhir.dstu3.model.Patient.LinkType.SEEALSO;
    default: return org.hl7.fhir.dstu3.model.Patient.LinkType.NULL;
    }
  }

  public org.hl7.fhir.instance.model.Patient.LinkType convertLinkType(org.hl7.fhir.dstu3.model.Patient.LinkType src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case REPLACEDBY: return org.hl7.fhir.instance.model.Patient.LinkType.REPLACE;
    case REPLACES: return org.hl7.fhir.instance.model.Patient.LinkType.REPLACE;
    case REFER: return org.hl7.fhir.instance.model.Patient.LinkType.REFER;
    case SEEALSO: return org.hl7.fhir.instance.model.Patient.LinkType.SEEALSO;
    default: return org.hl7.fhir.instance.model.Patient.LinkType.NULL;
    }
  }


  public org.hl7.fhir.dstu3.model.Person convertPerson(org.hl7.fhir.instance.model.Person src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.Person tgt = new org.hl7.fhir.dstu3.model.Person();
    copyDomainResource(src, tgt);
    for (org.hl7.fhir.instance.model.Identifier t : src.getIdentifier())
      tgt.addIdentifier(convertIdentifier(t));
    for (org.hl7.fhir.instance.model.HumanName t : src.getName())
      tgt.addName(convertHumanName(t));
    for (org.hl7.fhir.instance.model.ContactPoint t : src.getTelecom())
      tgt.addTelecom(convertContactPoint(t));
    tgt.setGender(convertAdministrativeGender(src.getGender()));
    tgt.setBirthDate(src.getBirthDate());
    for (org.hl7.fhir.instance.model.Address t : src.getAddress())
      tgt.addAddress(convertAddress(t));
    tgt.setPhoto(convertAttachment(src.getPhoto()));
    tgt.setManagingOrganization(convertReference(src.getManagingOrganization()));
    tgt.setActive(src.getActive());
    for (org.hl7.fhir.instance.model.Person.PersonLinkComponent t : src.getLink())
      tgt.addLink(convertPersonLinkComponent(t));
    return tgt;
  }

  public org.hl7.fhir.instance.model.Person convertPerson(org.hl7.fhir.dstu3.model.Person src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.Person tgt = new org.hl7.fhir.instance.model.Person();
    copyDomainResource(src, tgt);
    for (org.hl7.fhir.dstu3.model.Identifier t : src.getIdentifier())
      tgt.addIdentifier(convertIdentifier(t));
    for (org.hl7.fhir.dstu3.model.HumanName t : src.getName())
      tgt.addName(convertHumanName(t));
    for (org.hl7.fhir.dstu3.model.ContactPoint t : src.getTelecom())
      tgt.addTelecom(convertContactPoint(t));
    tgt.setGender(convertAdministrativeGender(src.getGender()));
    tgt.setBirthDate(src.getBirthDate());
    for (org.hl7.fhir.dstu3.model.Address t : src.getAddress())
      tgt.addAddress(convertAddress(t));
    tgt.setPhoto(convertAttachment(src.getPhoto()));
    tgt.setManagingOrganization(convertReference(src.getManagingOrganization()));
    tgt.setActive(src.getActive());
    for (org.hl7.fhir.dstu3.model.Person.PersonLinkComponent t : src.getLink())
      tgt.addLink(convertPersonLinkComponent(t));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.Person.PersonLinkComponent convertPersonLinkComponent(org.hl7.fhir.instance.model.Person.PersonLinkComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.Person.PersonLinkComponent tgt = new org.hl7.fhir.dstu3.model.Person.PersonLinkComponent();
    copyElement(src, tgt);
    tgt.setTarget(convertReference(src.getTarget()));
    tgt.setAssurance(convertIdentityAssuranceLevel(src.getAssurance()));
    return tgt;
  }

  public org.hl7.fhir.instance.model.Person.PersonLinkComponent convertPersonLinkComponent(org.hl7.fhir.dstu3.model.Person.PersonLinkComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.Person.PersonLinkComponent tgt = new org.hl7.fhir.instance.model.Person.PersonLinkComponent();
    copyElement(src, tgt);
    tgt.setTarget(convertReference(src.getTarget()));
    tgt.setAssurance(convertIdentityAssuranceLevel(src.getAssurance()));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.Person.IdentityAssuranceLevel convertIdentityAssuranceLevel(org.hl7.fhir.instance.model.Person.IdentityAssuranceLevel src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case LEVEL1: return org.hl7.fhir.dstu3.model.Person.IdentityAssuranceLevel.LEVEL1;
    case LEVEL2: return org.hl7.fhir.dstu3.model.Person.IdentityAssuranceLevel.LEVEL2;
    case LEVEL3: return org.hl7.fhir.dstu3.model.Person.IdentityAssuranceLevel.LEVEL3;
    case LEVEL4: return org.hl7.fhir.dstu3.model.Person.IdentityAssuranceLevel.LEVEL4;
    default: return org.hl7.fhir.dstu3.model.Person.IdentityAssuranceLevel.NULL;
    }
  }

  public org.hl7.fhir.instance.model.Person.IdentityAssuranceLevel convertIdentityAssuranceLevel(org.hl7.fhir.dstu3.model.Person.IdentityAssuranceLevel src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case LEVEL1: return org.hl7.fhir.instance.model.Person.IdentityAssuranceLevel.LEVEL1;
    case LEVEL2: return org.hl7.fhir.instance.model.Person.IdentityAssuranceLevel.LEVEL2;
    case LEVEL3: return org.hl7.fhir.instance.model.Person.IdentityAssuranceLevel.LEVEL3;
    case LEVEL4: return org.hl7.fhir.instance.model.Person.IdentityAssuranceLevel.LEVEL4;
    default: return org.hl7.fhir.instance.model.Person.IdentityAssuranceLevel.NULL;
    }
  }

  public org.hl7.fhir.dstu3.model.Practitioner convertPractitioner(org.hl7.fhir.instance.model.Practitioner src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.Practitioner tgt = new org.hl7.fhir.dstu3.model.Practitioner();
    copyDomainResource(src, tgt);
    for (org.hl7.fhir.instance.model.Identifier t : src.getIdentifier())
      tgt.addIdentifier(convertIdentifier(t));
    tgt.setActive(src.getActive());
    if (src.hasName())
      tgt.addName(convertHumanName(src.getName()));
    for (org.hl7.fhir.instance.model.ContactPoint t : src.getTelecom())
      tgt.addTelecom(convertContactPoint(t));
    for (org.hl7.fhir.instance.model.Address t : src.getAddress())
      tgt.addAddress(convertAddress(t));
    tgt.setGender(convertAdministrativeGender(src.getGender()));
    tgt.setBirthDate(src.getBirthDate());
    for (org.hl7.fhir.instance.model.Attachment t : src.getPhoto())
      tgt.addPhoto(convertAttachment(t));
//    for (org.hl7.fhir.instance.model.Practitioner.PractitionerPractitionerRoleComponent t : src.getPractitionerRole())
//      tgt.addRole(convertPractitionerPractitionerRoleComponent(t));
    for (org.hl7.fhir.instance.model.Practitioner.PractitionerQualificationComponent t : src.getQualification())
      tgt.addQualification(convertPractitionerQualificationComponent(t));
    for (org.hl7.fhir.instance.model.CodeableConcept t : src.getCommunication())
      tgt.addCommunication(convertCodeableConcept(t));
    return tgt;
  }

  public org.hl7.fhir.instance.model.Practitioner convertPractitioner(org.hl7.fhir.dstu3.model.Practitioner src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.Practitioner tgt = new org.hl7.fhir.instance.model.Practitioner();
    copyDomainResource(src, tgt);
    for (org.hl7.fhir.dstu3.model.Identifier t : src.getIdentifier())
      tgt.addIdentifier(convertIdentifier(t));
    tgt.setActive(src.getActive());
    for (org.hl7.fhir.dstu3.model.HumanName t : src.getName())
      tgt.setName(convertHumanName(t));
    for (org.hl7.fhir.dstu3.model.ContactPoint t : src.getTelecom())
      tgt.addTelecom(convertContactPoint(t));
    for (org.hl7.fhir.dstu3.model.Address t : src.getAddress())
      tgt.addAddress(convertAddress(t));
    tgt.setGender(convertAdministrativeGender(src.getGender()));
    tgt.setBirthDate(src.getBirthDate());
    for (org.hl7.fhir.dstu3.model.Attachment t : src.getPhoto())
      tgt.addPhoto(convertAttachment(t));
//    for (org.hl7.fhir.dstu3.model.Practitioner.PractitionerRoleComponent t : src.getRole())
//      tgt.addPractitionerRole(convertPractitionerPractitionerRoleComponent(t));
    for (org.hl7.fhir.dstu3.model.Practitioner.PractitionerQualificationComponent t : src.getQualification())
      tgt.addQualification(convertPractitionerQualificationComponent(t));
    for (org.hl7.fhir.dstu3.model.CodeableConcept t : src.getCommunication())
      tgt.addCommunication(convertCodeableConcept(t));
    return tgt;
  }

//  public org.hl7.fhir.dstu3.model.Practitioner.PractitionerRoleComponent convertPractitionerPractitionerRoleComponent(org.hl7.fhir.instance.model.Practitioner.PractitionerPractitionerRoleComponent src) throws FHIRException {
//    if (src == null || src.isEmpty())
//      return null;
//    org.hl7.fhir.dstu3.model.Practitioner.PractitionerRoleComponent tgt = new org.hl7.fhir.dstu3.model.Practitioner.PractitionerRoleComponent();
//    copyElement(src, tgt);
//    tgt.setOrganization(convertReference(src.getManagingOrganization()));
//    tgt.setCode(convertCodeableConcept(src.getRole()));
//    for (org.hl7.fhir.instance.model.CodeableConcept t : src.getSpecialty())
//      tgt.addSpecialty(convertCodeableConcept(t));
//    tgt.setPeriod(convertPeriod(src.getPeriod()));
//    for (org.hl7.fhir.instance.model.Reference t : src.getLocation())
//      tgt.addLocation(convertReference(t));
//    for (org.hl7.fhir.instance.model.Reference t : src.getHealthcareService())
//      tgt.addHealthcareService(convertReference(t));
//    return tgt;
//  }

//  public org.hl7.fhir.instance.model.Practitioner.PractitionerPractitionerRoleComponent convertPractitionerPractitionerRoleComponent(org.hl7.fhir.dstu3.model.Practitioner.PractitionerRoleComponent src) throws FHIRException {
//    if (src == null || src.isEmpty())
//      return null;
//    org.hl7.fhir.instance.model.Practitioner.PractitionerPractitionerRoleComponent tgt = new org.hl7.fhir.instance.model.Practitioner.PractitionerPractitionerRoleComponent();
//    copyElement(src, tgt);
//    tgt.setManagingOrganization(convertReference(src.getOrganization()));
//    tgt.setRole(convertCodeableConcept(src.getCode()));
//    for (org.hl7.fhir.dstu3.model.CodeableConcept t : src.getSpecialty())
//      tgt.addSpecialty(convertCodeableConcept(t));
//    tgt.setPeriod(convertPeriod(src.getPeriod()));
//    for (org.hl7.fhir.dstu3.model.Reference t : src.getLocation())
//      tgt.addLocation(convertReference(t));
//    for (org.hl7.fhir.dstu3.model.Reference t : src.getHealthcareService())
//      tgt.addHealthcareService(convertReference(t));
//    return tgt;
//  }

  public org.hl7.fhir.dstu3.model.Practitioner.PractitionerQualificationComponent convertPractitionerQualificationComponent(org.hl7.fhir.instance.model.Practitioner.PractitionerQualificationComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.Practitioner.PractitionerQualificationComponent tgt = new org.hl7.fhir.dstu3.model.Practitioner.PractitionerQualificationComponent();
    copyElement(src, tgt);
    for (org.hl7.fhir.instance.model.Identifier t : src.getIdentifier())
      tgt.addIdentifier(convertIdentifier(t));
    tgt.setCode(convertCodeableConcept(src.getCode()));
    tgt.setPeriod(convertPeriod(src.getPeriod()));
    tgt.setIssuer(convertReference(src.getIssuer()));
    return tgt;
  }

  public org.hl7.fhir.instance.model.Practitioner.PractitionerQualificationComponent convertPractitionerQualificationComponent(org.hl7.fhir.dstu3.model.Practitioner.PractitionerQualificationComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.Practitioner.PractitionerQualificationComponent tgt = new org.hl7.fhir.instance.model.Practitioner.PractitionerQualificationComponent();
    copyElement(src, tgt);
    for (org.hl7.fhir.dstu3.model.Identifier t : src.getIdentifier())
      tgt.addIdentifier(convertIdentifier(t));
    tgt.setCode(convertCodeableConcept(src.getCode()));
    tgt.setPeriod(convertPeriod(src.getPeriod()));
    tgt.setIssuer(convertReference(src.getIssuer()));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.Procedure convertProcedure(org.hl7.fhir.instance.model.Procedure src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.Procedure tgt = new org.hl7.fhir.dstu3.model.Procedure();
    copyDomainResource(src, tgt);
    for (org.hl7.fhir.instance.model.Identifier t : src.getIdentifier())
      tgt.addIdentifier(convertIdentifier(t));
    tgt.setSubject(convertReference(src.getSubject()));
    tgt.setStatus(convertProcedureStatus(src.getStatus()));
    tgt.setCategory(convertCodeableConcept(src.getCategory()));
    tgt.setCode(convertCodeableConcept(src.getCode()));
    tgt.setNotDone(src.getNotPerformed());
//    for (org.hl7.fhir.instance.model.CodeableConcept t : src.getReasonNotPerformed())
    if (src.hasReasonNotPerformed())
      tgt.setNotDoneReason(convertCodeableConcept(src.getReasonNotPerformed().get(0)));
    for (org.hl7.fhir.instance.model.CodeableConcept t : src.getBodySite())
      tgt.addBodySite(convertCodeableConcept(t));
    if (src.hasReasonCodeableConcept())
      tgt.addReasonCode(convertCodeableConcept(src.getReasonCodeableConcept()));
    for (org.hl7.fhir.instance.model.Procedure.ProcedurePerformerComponent t : src.getPerformer())
      tgt.addPerformer(convertProcedurePerformerComponent(t));
    tgt.setPerformed(convertType(src.getPerformed()));
    tgt.setContext(convertReference(src.getEncounter()));
    tgt.setLocation(convertReference(src.getLocation()));
    tgt.setOutcome(convertCodeableConcept(src.getOutcome()));
    for (org.hl7.fhir.instance.model.Reference t : src.getReport())
      tgt.addReport(convertReference(t));
    for (org.hl7.fhir.instance.model.CodeableConcept t : src.getComplication())
      tgt.addComplication(convertCodeableConcept(t));
    for (org.hl7.fhir.instance.model.CodeableConcept t : src.getFollowUp())
      tgt.addFollowUp(convertCodeableConcept(t));
    tgt.addBasedOn(convertReference(src.getRequest()));
//    for (org.hl7.fhir.instance.model.Annotation t : src.getNotes())
//      tgt.addNotes(convertAnnotation(t));
    for (org.hl7.fhir.instance.model.Procedure.ProcedureFocalDeviceComponent t : src.getFocalDevice())
      tgt.addFocalDevice(convertProcedureFocalDeviceComponent(t));
    return tgt;
  }

  public org.hl7.fhir.instance.model.Procedure convertProcedure(org.hl7.fhir.dstu3.model.Procedure src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.Procedure tgt = new org.hl7.fhir.instance.model.Procedure();
    copyDomainResource(src, tgt);
    for (org.hl7.fhir.dstu3.model.Identifier t : src.getIdentifier())
      tgt.addIdentifier(convertIdentifier(t));
    tgt.setSubject(convertReference(src.getSubject()));
    tgt.setStatus(convertProcedureStatus(src.getStatus()));
    tgt.setCategory(convertCodeableConcept(src.getCategory()));
    tgt.setCode(convertCodeableConcept(src.getCode()));
    tgt.setNotPerformed(src.getNotDone());
//    for (org.hl7.fhir.dstu3.model.CodeableConcept t : src.getReasonNotPerformed())
      tgt.addReasonNotPerformed(convertCodeableConcept(src.getNotDoneReason()));
    for (org.hl7.fhir.dstu3.model.CodeableConcept t : src.getBodySite())
      tgt.addBodySite(convertCodeableConcept(t));
    tgt.setReason(convertType(src.getReasonCodeFirstRep()));
    for (org.hl7.fhir.dstu3.model.Procedure.ProcedurePerformerComponent t : src.getPerformer())
      tgt.addPerformer(convertProcedurePerformerComponent(t));
    tgt.setPerformed(convertType(src.getPerformed()));
    tgt.setEncounter(convertReference(src.getContext()));
    tgt.setLocation(convertReference(src.getLocation()));
    tgt.setOutcome(convertCodeableConcept(src.getOutcome()));
    for (org.hl7.fhir.dstu3.model.Reference t : src.getReport())
      tgt.addReport(convertReference(t));
    for (org.hl7.fhir.dstu3.model.CodeableConcept t : src.getComplication())
      tgt.addComplication(convertCodeableConcept(t));
    for (org.hl7.fhir.dstu3.model.CodeableConcept t : src.getFollowUp())
      tgt.addFollowUp(convertCodeableConcept(t));
    tgt.setRequest(convertReference(src.getBasedOnFirstRep()));
//    for (org.hl7.fhir.dstu3.model.Annotation t : src.getNotes())
//      tgt.addNotes(convertAnnotation(t));
    for (org.hl7.fhir.dstu3.model.Procedure.ProcedureFocalDeviceComponent t : src.getFocalDevice())
      tgt.addFocalDevice(convertProcedureFocalDeviceComponent(t));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.Procedure.ProcedureStatus convertProcedureStatus(org.hl7.fhir.instance.model.Procedure.ProcedureStatus src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case INPROGRESS: return org.hl7.fhir.dstu3.model.Procedure.ProcedureStatus.INPROGRESS;
    case ABORTED: return org.hl7.fhir.dstu3.model.Procedure.ProcedureStatus.ABORTED;
    case COMPLETED: return org.hl7.fhir.dstu3.model.Procedure.ProcedureStatus.COMPLETED;
    case ENTEREDINERROR: return org.hl7.fhir.dstu3.model.Procedure.ProcedureStatus.ENTEREDINERROR;
    default: return org.hl7.fhir.dstu3.model.Procedure.ProcedureStatus.NULL;
    }
  }

  public org.hl7.fhir.instance.model.Procedure.ProcedureStatus convertProcedureStatus(org.hl7.fhir.dstu3.model.Procedure.ProcedureStatus src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case INPROGRESS: return org.hl7.fhir.instance.model.Procedure.ProcedureStatus.INPROGRESS;
    case ABORTED: return org.hl7.fhir.instance.model.Procedure.ProcedureStatus.ABORTED;
    case COMPLETED: return org.hl7.fhir.instance.model.Procedure.ProcedureStatus.COMPLETED;
    case ENTEREDINERROR: return org.hl7.fhir.instance.model.Procedure.ProcedureStatus.ENTEREDINERROR;
    default: return org.hl7.fhir.instance.model.Procedure.ProcedureStatus.NULL;
    }
  }

  public org.hl7.fhir.dstu3.model.Procedure.ProcedurePerformerComponent convertProcedurePerformerComponent(org.hl7.fhir.instance.model.Procedure.ProcedurePerformerComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.Procedure.ProcedurePerformerComponent tgt = new org.hl7.fhir.dstu3.model.Procedure.ProcedurePerformerComponent();
    copyElement(src, tgt);
    tgt.setActor(convertReference(src.getActor()));
    tgt.setRole(convertCodeableConcept(src.getRole()));
    return tgt;
  }

  public org.hl7.fhir.instance.model.Procedure.ProcedurePerformerComponent convertProcedurePerformerComponent(org.hl7.fhir.dstu3.model.Procedure.ProcedurePerformerComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.Procedure.ProcedurePerformerComponent tgt = new org.hl7.fhir.instance.model.Procedure.ProcedurePerformerComponent();
    copyElement(src, tgt);
    tgt.setActor(convertReference(src.getActor()));
    tgt.setRole(convertCodeableConcept(src.getRole()));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.Procedure.ProcedureFocalDeviceComponent convertProcedureFocalDeviceComponent(org.hl7.fhir.instance.model.Procedure.ProcedureFocalDeviceComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.Procedure.ProcedureFocalDeviceComponent tgt = new org.hl7.fhir.dstu3.model.Procedure.ProcedureFocalDeviceComponent();
    copyElement(src, tgt);
    tgt.setAction(convertCodeableConcept(src.getAction()));
    tgt.setManipulated(convertReference(src.getManipulated()));
    return tgt;
  }

  public org.hl7.fhir.instance.model.Procedure.ProcedureFocalDeviceComponent convertProcedureFocalDeviceComponent(org.hl7.fhir.dstu3.model.Procedure.ProcedureFocalDeviceComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.Procedure.ProcedureFocalDeviceComponent tgt = new org.hl7.fhir.instance.model.Procedure.ProcedureFocalDeviceComponent();
    copyElement(src, tgt);
    tgt.setAction(convertCodeableConcept(src.getAction()));
    tgt.setManipulated(convertReference(src.getManipulated()));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.ProcedureRequest convertProcedureRequest(org.hl7.fhir.instance.model.ProcedureRequest src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.ProcedureRequest tgt = new org.hl7.fhir.dstu3.model.ProcedureRequest();
    copyDomainResource(src, tgt);
    for (org.hl7.fhir.instance.model.Identifier t : src.getIdentifier())
      tgt.addIdentifier(convertIdentifier(t));
    tgt.setSubject(convertReference(src.getSubject()));
    tgt.setCode(convertCodeableConcept(src.getCode()));
    for (org.hl7.fhir.instance.model.CodeableConcept t : src.getBodySite())
      tgt.addBodySite(convertCodeableConcept(t));
    if (src.hasReasonCodeableConcept())
      tgt.addReasonCode(convertCodeableConcept(src.getReasonCodeableConcept()));
    tgt.setOccurrence(convertType(src.getScheduled()));
    tgt.setContext(convertReference(src.getEncounter()));
    tgt.setPerformer(convertReference(src.getPerformer()));
    tgt.setStatus(convertProcedureRequestStatus(src.getStatus()));
//    for (org.hl7.fhir.instance.model.Annotation t : src.getNotes())
//      tgt.addNotes(convertAnnotation(t));
    tgt.setAsNeeded(convertType(src.getAsNeeded()));
    tgt.setAuthoredOn(src.getOrderedOn());
//    tgt.setOrderer(convertReference(src.getOrderer()));
    tgt.setPriority(convertProcedureRequestPriority(src.getPriority()));
    return tgt;
  }

  public org.hl7.fhir.instance.model.ProcedureRequest convertProcedureRequest(org.hl7.fhir.dstu3.model.ProcedureRequest src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.ProcedureRequest tgt = new org.hl7.fhir.instance.model.ProcedureRequest();
    copyDomainResource(src, tgt);
    for (org.hl7.fhir.dstu3.model.Identifier t : src.getIdentifier())
      tgt.addIdentifier(convertIdentifier(t));
    tgt.setSubject(convertReference(src.getSubject()));
    tgt.setCode(convertCodeableConcept(src.getCode()));
    for (org.hl7.fhir.dstu3.model.CodeableConcept t : src.getBodySite())
      tgt.addBodySite(convertCodeableConcept(t));
    tgt.setReason(convertType(src.getReasonCodeFirstRep()));
    tgt.setScheduled(convertType(src.getOccurrence()));
    tgt.setEncounter(convertReference(src.getContext()));
    tgt.setPerformer(convertReference(src.getPerformer()));
    tgt.setStatus(convertProcedureRequestStatus(src.getStatus()));
//    for (org.hl7.fhir.dstu3.model.Annotation t : src.getNotes())
//      tgt.addNotes(convertAnnotation(t));
    tgt.setAsNeeded(convertType(src.getAsNeeded()));
    tgt.setOrderedOn(src.getAuthoredOn());
//    tgt.setOrderer(convertReference(src.getOrderer()));
    tgt.setPriority(convertProcedureRequestPriority(src.getPriority()));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.ProcedureRequest.ProcedureRequestStatus convertProcedureRequestStatus(org.hl7.fhir.instance.model.ProcedureRequest.ProcedureRequestStatus src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case PROPOSED: return org.hl7.fhir.dstu3.model.ProcedureRequest.ProcedureRequestStatus.DRAFT;
    case DRAFT: return org.hl7.fhir.dstu3.model.ProcedureRequest.ProcedureRequestStatus.DRAFT;
    case REQUESTED: return org.hl7.fhir.dstu3.model.ProcedureRequest.ProcedureRequestStatus.ACTIVE;
    case RECEIVED: return org.hl7.fhir.dstu3.model.ProcedureRequest.ProcedureRequestStatus.ACTIVE;
    case ACCEPTED: return org.hl7.fhir.dstu3.model.ProcedureRequest.ProcedureRequestStatus.ACTIVE;
    case INPROGRESS: return org.hl7.fhir.dstu3.model.ProcedureRequest.ProcedureRequestStatus.ACTIVE;
    case COMPLETED: return org.hl7.fhir.dstu3.model.ProcedureRequest.ProcedureRequestStatus.COMPLETED;
    case SUSPENDED: return org.hl7.fhir.dstu3.model.ProcedureRequest.ProcedureRequestStatus.SUSPENDED;
//    case REJECTED: return org.hl7.fhir.dstu3.model.ProcedureRequest.ProcedureRequestStatus.REJECTED;
    case ABORTED: return org.hl7.fhir.dstu3.model.ProcedureRequest.ProcedureRequestStatus.ENTEREDINERROR;
    default: return org.hl7.fhir.dstu3.model.ProcedureRequest.ProcedureRequestStatus.NULL;
    }
  }

  public org.hl7.fhir.instance.model.ProcedureRequest.ProcedureRequestStatus convertProcedureRequestStatus(org.hl7.fhir.dstu3.model.ProcedureRequest.ProcedureRequestStatus src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
//    case PROPOSED: return org.hl7.fhir.instance.model.ProcedureRequest.ProcedureRequestStatus.PROPOSED;
    case DRAFT: return org.hl7.fhir.instance.model.ProcedureRequest.ProcedureRequestStatus.DRAFT;
//    case REQUESTED: return org.hl7.fhir.instance.model.ProcedureRequest.ProcedureRequestStatus.REQUESTED;
//    case RECEIVED: return org.hl7.fhir.instance.model.ProcedureRequest.ProcedureRequestStatus.RECEIVED;
//    case ACCEPTED: return org.hl7.fhir.instance.model.ProcedureRequest.ProcedureRequestStatus.ACCEPTED;
    case ACTIVE: return org.hl7.fhir.instance.model.ProcedureRequest.ProcedureRequestStatus.INPROGRESS;
    case COMPLETED: return org.hl7.fhir.instance.model.ProcedureRequest.ProcedureRequestStatus.COMPLETED;
    case SUSPENDED: return org.hl7.fhir.instance.model.ProcedureRequest.ProcedureRequestStatus.SUSPENDED;
//    case REJECTED: return org.hl7.fhir.instance.model.ProcedureRequest.ProcedureRequestStatus.REJECTED;
    case ENTEREDINERROR: return org.hl7.fhir.instance.model.ProcedureRequest.ProcedureRequestStatus.ABORTED;
    default: return org.hl7.fhir.instance.model.ProcedureRequest.ProcedureRequestStatus.NULL;
    }
  }

  public org.hl7.fhir.dstu3.model.ProcedureRequest.ProcedureRequestPriority convertProcedureRequestPriority(org.hl7.fhir.instance.model.ProcedureRequest.ProcedureRequestPriority src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case ROUTINE: return org.hl7.fhir.dstu3.model.ProcedureRequest.ProcedureRequestPriority.ROUTINE;
    case URGENT: return org.hl7.fhir.dstu3.model.ProcedureRequest.ProcedureRequestPriority.URGENT;
    case STAT: return org.hl7.fhir.dstu3.model.ProcedureRequest.ProcedureRequestPriority.STAT;
    case ASAP: return org.hl7.fhir.dstu3.model.ProcedureRequest.ProcedureRequestPriority.ASAP;
    default: return org.hl7.fhir.dstu3.model.ProcedureRequest.ProcedureRequestPriority.NULL;
    }
  }

  public org.hl7.fhir.instance.model.ProcedureRequest.ProcedureRequestPriority convertProcedureRequestPriority(org.hl7.fhir.dstu3.model.ProcedureRequest.ProcedureRequestPriority src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case ROUTINE: return org.hl7.fhir.instance.model.ProcedureRequest.ProcedureRequestPriority.ROUTINE;
    case URGENT: return org.hl7.fhir.instance.model.ProcedureRequest.ProcedureRequestPriority.URGENT;
    case STAT: return org.hl7.fhir.instance.model.ProcedureRequest.ProcedureRequestPriority.STAT;
    case ASAP: return org.hl7.fhir.instance.model.ProcedureRequest.ProcedureRequestPriority.ASAP;
    default: return org.hl7.fhir.instance.model.ProcedureRequest.ProcedureRequestPriority.NULL;
    }
  }


  public org.hl7.fhir.dstu3.model.ProcessRequest.ActionList convertActionList(org.hl7.fhir.instance.model.ProcessRequest.ActionList src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case CANCEL: return org.hl7.fhir.dstu3.model.ProcessRequest.ActionList.CANCEL;
    case POLL: return org.hl7.fhir.dstu3.model.ProcessRequest.ActionList.POLL;
    case REPROCESS: return org.hl7.fhir.dstu3.model.ProcessRequest.ActionList.REPROCESS;
    case STATUS: return org.hl7.fhir.dstu3.model.ProcessRequest.ActionList.STATUS;
    default: return org.hl7.fhir.dstu3.model.ProcessRequest.ActionList.NULL;
    }
  }

  public org.hl7.fhir.instance.model.ProcessRequest.ActionList convertActionList(org.hl7.fhir.dstu3.model.ProcessRequest.ActionList src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case CANCEL: return org.hl7.fhir.instance.model.ProcessRequest.ActionList.CANCEL;
    case POLL: return org.hl7.fhir.instance.model.ProcessRequest.ActionList.POLL;
    case REPROCESS: return org.hl7.fhir.instance.model.ProcessRequest.ActionList.REPROCESS;
    case STATUS: return org.hl7.fhir.instance.model.ProcessRequest.ActionList.STATUS;
    default: return org.hl7.fhir.instance.model.ProcessRequest.ActionList.NULL;
    }
  }

  public org.hl7.fhir.dstu3.model.ProcessRequest.ItemsComponent convertItemsComponent(org.hl7.fhir.instance.model.ProcessRequest.ItemsComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.ProcessRequest.ItemsComponent tgt = new org.hl7.fhir.dstu3.model.ProcessRequest.ItemsComponent();
    copyElement(src, tgt);
    tgt.setSequenceLinkId(src.getSequenceLinkId());
    return tgt;
  }

  public org.hl7.fhir.instance.model.ProcessRequest.ItemsComponent convertItemsComponent(org.hl7.fhir.dstu3.model.ProcessRequest.ItemsComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.ProcessRequest.ItemsComponent tgt = new org.hl7.fhir.instance.model.ProcessRequest.ItemsComponent();
    copyElement(src, tgt);
    tgt.setSequenceLinkId(src.getSequenceLinkId());
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.Provenance convertProvenance(org.hl7.fhir.instance.model.Provenance src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.Provenance tgt = new org.hl7.fhir.dstu3.model.Provenance();
    copyDomainResource(src, tgt);
    for (org.hl7.fhir.instance.model.Reference t : src.getTarget())
      tgt.addTarget(convertReference(t));
    tgt.setPeriod(convertPeriod(src.getPeriod()));
    tgt.setRecorded(src.getRecorded());
    for (org.hl7.fhir.instance.model.CodeableConcept t : src.getReason())
      for (org.hl7.fhir.instance.model.Coding tc : t.getCoding())
        tgt.addReason(convertCoding(tc));
    for (org.hl7.fhir.instance.model.Coding t : src.getActivity().getCoding())
      tgt.setActivity(convertCoding(t));
    tgt.setLocation(convertReference(src.getLocation()));
    for (org.hl7.fhir.instance.model.UriType t : src.getPolicy())
      tgt.addPolicy(t.getValue());
    for (org.hl7.fhir.instance.model.Provenance.ProvenanceAgentComponent t : src.getAgent())
      tgt.addAgent(convertProvenanceAgentComponent(t));
    for (org.hl7.fhir.instance.model.Provenance.ProvenanceEntityComponent t : src.getEntity())
      tgt.addEntity(convertProvenanceEntityComponent(t));
    for (org.hl7.fhir.instance.model.Signature t : src.getSignature())
      tgt.addSignature(convertSignature(t));
    return tgt;
  }

  public org.hl7.fhir.instance.model.Provenance convertProvenance(org.hl7.fhir.dstu3.model.Provenance src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.Provenance tgt = new org.hl7.fhir.instance.model.Provenance();
    copyDomainResource(src, tgt);
    for (org.hl7.fhir.dstu3.model.Reference t : src.getTarget())
      tgt.addTarget(convertReference(t));
    tgt.setPeriod(convertPeriod(src.getPeriod()));
    tgt.setRecorded(src.getRecorded());
    for (org.hl7.fhir.dstu3.model.Coding t : src.getReason())
      tgt.addReason().addCoding(convertCoding(t));
    tgt.setActivity(new org.hl7.fhir.instance.model.CodeableConcept().addCoding(convertCoding(src.getActivity())));
    tgt.setLocation(convertReference(src.getLocation()));
    for (org.hl7.fhir.dstu3.model.UriType t : src.getPolicy())
      tgt.addPolicy(t.getValue());
    for (org.hl7.fhir.dstu3.model.Provenance.ProvenanceAgentComponent t : src.getAgent())
      tgt.addAgent(convertProvenanceAgentComponent(t));
    for (org.hl7.fhir.dstu3.model.Provenance.ProvenanceEntityComponent t : src.getEntity())
      tgt.addEntity(convertProvenanceEntityComponent(t));
    for (org.hl7.fhir.dstu3.model.Signature t : src.getSignature())
      tgt.addSignature(convertSignature(t));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.Provenance.ProvenanceAgentComponent convertProvenanceAgentComponent(org.hl7.fhir.instance.model.Provenance.ProvenanceAgentComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.Provenance.ProvenanceAgentComponent tgt = new org.hl7.fhir.dstu3.model.Provenance.ProvenanceAgentComponent();
    copyElement(src, tgt);
//    tgt.setRole(convertCoding(src.getRole()));
    tgt.setWho(convertReference(src.getActor()));
    return tgt;
  }

  public org.hl7.fhir.instance.model.Provenance.ProvenanceAgentComponent convertProvenanceAgentComponent(org.hl7.fhir.dstu3.model.Provenance.ProvenanceAgentComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.Provenance.ProvenanceAgentComponent tgt = new org.hl7.fhir.instance.model.Provenance.ProvenanceAgentComponent();
    copyElement(src, tgt);
//    tgt.setRole(convertCoding(src.getRole()));
    if (src.hasWhoReference())
    tgt.setActor(convertReference(src.getWhoReference()));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.Provenance.ProvenanceEntityComponent convertProvenanceEntityComponent(org.hl7.fhir.instance.model.Provenance.ProvenanceEntityComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.Provenance.ProvenanceEntityComponent tgt = new org.hl7.fhir.dstu3.model.Provenance.ProvenanceEntityComponent();
    copyElement(src, tgt);
    tgt.setRole(convertProvenanceEntityRole(src.getRole()));
    if (src.hasReference())
    tgt.setWhat(new org.hl7.fhir.dstu3.model.Reference().setReference(src.getReference()));
    tgt.addAgent(convertProvenanceAgentComponent(src.getAgent()));
    return tgt;
  }

  public org.hl7.fhir.instance.model.Provenance.ProvenanceEntityComponent convertProvenanceEntityComponent(org.hl7.fhir.dstu3.model.Provenance.ProvenanceEntityComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.Provenance.ProvenanceEntityComponent tgt = new org.hl7.fhir.instance.model.Provenance.ProvenanceEntityComponent();
    copyElement(src, tgt);
    tgt.setRole(convertProvenanceEntityRole(src.getRole()));
    if (src.hasWhatReference() && src.getWhatReference().hasReference())
      tgt.setReference(src.getWhatReference().getReference());
    for (org.hl7.fhir.dstu3.model.Provenance.ProvenanceAgentComponent t : src.getAgent())
      tgt.setAgent(convertProvenanceAgentComponent(t));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.Provenance.ProvenanceEntityRole convertProvenanceEntityRole(org.hl7.fhir.instance.model.Provenance.ProvenanceEntityRole src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case DERIVATION: return org.hl7.fhir.dstu3.model.Provenance.ProvenanceEntityRole.DERIVATION;
    case REVISION: return org.hl7.fhir.dstu3.model.Provenance.ProvenanceEntityRole.REVISION;
    case QUOTATION: return org.hl7.fhir.dstu3.model.Provenance.ProvenanceEntityRole.QUOTATION;
    case SOURCE: return org.hl7.fhir.dstu3.model.Provenance.ProvenanceEntityRole.SOURCE;
    default: return org.hl7.fhir.dstu3.model.Provenance.ProvenanceEntityRole.NULL;
    }
  }

  public org.hl7.fhir.instance.model.Provenance.ProvenanceEntityRole convertProvenanceEntityRole(org.hl7.fhir.dstu3.model.Provenance.ProvenanceEntityRole src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case DERIVATION: return org.hl7.fhir.instance.model.Provenance.ProvenanceEntityRole.DERIVATION;
    case REVISION: return org.hl7.fhir.instance.model.Provenance.ProvenanceEntityRole.REVISION;
    case QUOTATION: return org.hl7.fhir.instance.model.Provenance.ProvenanceEntityRole.QUOTATION;
    case SOURCE: return org.hl7.fhir.instance.model.Provenance.ProvenanceEntityRole.SOURCE;
    default: return org.hl7.fhir.instance.model.Provenance.ProvenanceEntityRole.NULL;
    }
  }

  public org.hl7.fhir.dstu3.model.Questionnaire convertQuestionnaire(org.hl7.fhir.instance.model.Questionnaire src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.Questionnaire tgt = new org.hl7.fhir.dstu3.model.Questionnaire();
    copyDomainResource(src, tgt);
    for (org.hl7.fhir.instance.model.Identifier t : src.getIdentifier())
      tgt.addIdentifier(convertIdentifier(t));
    tgt.setVersion(src.getVersion());
    tgt.setStatus(convertQuestionnaireStatus(src.getStatus()));
    if (src.hasDate())
      tgt.setDate(src.getDate());
    tgt.setPublisher(src.getPublisher());
    for (org.hl7.fhir.instance.model.ContactPoint t : src.getTelecom())
      tgt.addContact(convertQuestionnaireContactComponent(t));
    org.hl7.fhir.instance.model.Questionnaire.GroupComponent root = src.getGroup();
    tgt.setTitle(root.getTitle());
    for (org.hl7.fhir.instance.model.Coding t : root.getConcept())
      tgt.addCode(convertCoding(t));
    for (org.hl7.fhir.instance.model.CodeType t : src.getSubjectType())
      tgt.addSubjectType(t.getValue());
    tgt.addItem(convertQuestionnaireGroupComponent(root));
    return tgt;
  }

  public org.hl7.fhir.instance.model.Questionnaire convertQuestionnaire(org.hl7.fhir.dstu3.model.Questionnaire src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.Questionnaire tgt = new org.hl7.fhir.instance.model.Questionnaire();
    copyDomainResource(src, tgt);
    for (org.hl7.fhir.dstu3.model.Identifier t : src.getIdentifier())
      tgt.addIdentifier(convertIdentifier(t));
    tgt.setVersion(src.getVersion());
    tgt.setStatus(convertQuestionnaireStatus(src.getStatus()));
    if (src.hasDate())
      tgt.setDate(src.getDate());
    tgt.setPublisher(src.getPublisher());
    for (ContactDetail t : src.getContact())
      for (org.hl7.fhir.dstu3.model.ContactPoint t1 : t.getTelecom())
        tgt.addTelecom(convertContactPoint(t1));
    org.hl7.fhir.instance.model.Questionnaire.GroupComponent root = tgt.getGroup();
    root.setTitle(src.getTitle());
    for (org.hl7.fhir.dstu3.model.Coding t : src.getCode()) {
      root.addConcept(convertCoding(t));
    }
    for (org.hl7.fhir.dstu3.model.CodeType t : src.getSubjectType())
      tgt.addSubjectType(t.getValue());
    for (org.hl7.fhir.dstu3.model.Questionnaire.QuestionnaireItemComponent t : src.getItem())
      if (t.getType() == org.hl7.fhir.dstu3.model.Questionnaire.QuestionnaireItemType.GROUP)
        root.addGroup(convertQuestionnaireGroupComponent(t));
      else
        root.addQuestion(convertQuestionnaireQuestionComponent(t));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.ContactDetail convertQuestionnaireContactComponent(org.hl7.fhir.instance.model.ContactPoint src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.ContactDetail tgt = new org.hl7.fhir.dstu3.model.ContactDetail();
    copyElement(src, tgt);
    tgt.addTelecom(convertContactPoint(src));
    return tgt;
  }
  
  private static org.hl7.fhir.dstu3.model.Enumerations.PublicationStatus convertQuestionnaireStatus(org.hl7.fhir.instance.model.Questionnaire.QuestionnaireStatus src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case DRAFT: return org.hl7.fhir.dstu3.model.Enumerations.PublicationStatus.DRAFT;
    case PUBLISHED: return org.hl7.fhir.dstu3.model.Enumerations.PublicationStatus.ACTIVE;
    case RETIRED: return org.hl7.fhir.dstu3.model.Enumerations.PublicationStatus.RETIRED;
    default: return org.hl7.fhir.dstu3.model.Enumerations.PublicationStatus.NULL;
    }
  }

  private static org.hl7.fhir.instance.model.Questionnaire.QuestionnaireStatus convertQuestionnaireStatus(org.hl7.fhir.dstu3.model.Enumerations.PublicationStatus src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case DRAFT: return org.hl7.fhir.instance.model.Questionnaire.QuestionnaireStatus.DRAFT;
    case ACTIVE: return org.hl7.fhir.instance.model.Questionnaire.QuestionnaireStatus.PUBLISHED;
    case RETIRED: return org.hl7.fhir.instance.model.Questionnaire.QuestionnaireStatus.RETIRED;
    default: return org.hl7.fhir.instance.model.Questionnaire.QuestionnaireStatus.NULL;
    }
  }
  
  public org.hl7.fhir.dstu3.model.Questionnaire.QuestionnaireItemComponent convertQuestionnaireQuestionComponent(org.hl7.fhir.instance.model.Questionnaire.QuestionComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.Questionnaire.QuestionnaireItemComponent tgt = new org.hl7.fhir.dstu3.model.Questionnaire.QuestionnaireItemComponent();
    copyElement(src, tgt);
    tgt.setLinkId(src.getLinkId());
    for (org.hl7.fhir.instance.model.Coding t : src.getConcept())
      tgt.addCode(convertCoding(t));
    tgt.setText(src.getText());
    tgt.setType(convertQuestionnaireQuestionType(src.getType()));
    tgt.setRequired(src.getRequired());
    tgt.setRepeats(src.getRepeats());
    tgt.setOptions(convertReference(src.getOptions()));
    for (org.hl7.fhir.instance.model.Coding t : src.getOption())
      tgt.addOption().setValue(convertCoding(t));
    for (org.hl7.fhir.instance.model.Questionnaire.GroupComponent t : src.getGroup())
      tgt.addItem(convertQuestionnaireGroupComponent(t));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.Questionnaire.QuestionnaireItemComponent convertQuestionnaireGroupComponent(org.hl7.fhir.instance.model.Questionnaire.GroupComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.Questionnaire.QuestionnaireItemComponent tgt = new org.hl7.fhir.dstu3.model.Questionnaire.QuestionnaireItemComponent();
    copyElement(src, tgt);
    tgt.setLinkId(src.getLinkId());
    for (org.hl7.fhir.instance.model.Coding t : src.getConcept())
      tgt.addCode(convertCoding(t));
    tgt.setText(src.getText());
    tgt.setType(org.hl7.fhir.dstu3.model.Questionnaire.QuestionnaireItemType.GROUP);
    tgt.setRequired(src.getRequired());
    tgt.setRepeats(src.getRepeats());
    for (org.hl7.fhir.instance.model.Questionnaire.GroupComponent t : src.getGroup())
      tgt.addItem(convertQuestionnaireGroupComponent(t));
    for (org.hl7.fhir.instance.model.Questionnaire.QuestionComponent t : src.getQuestion())
      tgt.addItem(convertQuestionnaireQuestionComponent(t));
    return tgt;
  }

  public org.hl7.fhir.instance.model.Questionnaire.GroupComponent convertQuestionnaireGroupComponent(org.hl7.fhir.dstu3.model.Questionnaire.QuestionnaireItemComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.Questionnaire.GroupComponent tgt = new org.hl7.fhir.instance.model.Questionnaire.GroupComponent();
    copyElement(src, tgt);
    tgt.setLinkId(src.getLinkId());
    for (org.hl7.fhir.dstu3.model.Coding t : src.getCode())
      tgt.addConcept(convertCoding(t));
    tgt.setText(src.getText());
    tgt.setRequired(src.getRequired());
    tgt.setRepeats(src.getRepeats());
    for (org.hl7.fhir.dstu3.model.Questionnaire.QuestionnaireItemComponent t : src.getItem())
      if (t.getType() == org.hl7.fhir.dstu3.model.Questionnaire.QuestionnaireItemType.GROUP)
        tgt.addGroup(convertQuestionnaireGroupComponent(t));
      else
        tgt.addQuestion(convertQuestionnaireQuestionComponent(t));
    return tgt;
  }

  public org.hl7.fhir.instance.model.Questionnaire.QuestionComponent convertQuestionnaireQuestionComponent(org.hl7.fhir.dstu3.model.Questionnaire.QuestionnaireItemComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.Questionnaire.QuestionComponent tgt = new org.hl7.fhir.instance.model.Questionnaire.QuestionComponent();
    copyElement(src, tgt);
    tgt.setLinkId(src.getLinkId());
    for (org.hl7.fhir.dstu3.model.Coding t : src.getCode())
      tgt.addConcept(convertCoding(t));
    tgt.setText(src.getText());
    tgt.setType(convertQuestionnaireItemType(src.getType()));
    tgt.setRequired(src.getRequired());
    tgt.setRepeats(src.getRepeats());
    tgt.setOptions(convertReference(src.getOptions()));
    for (org.hl7.fhir.dstu3.model.Questionnaire.QuestionnaireItemOptionComponent t : src.getOption())
      if (t.hasValueCoding())
        try {
          tgt.addOption(convertCoding(t.getValueCoding()));
        } catch (org.hl7.fhir.exceptions.FHIRException e) {
          throw new FHIRException(e);
        }
    for (org.hl7.fhir.dstu3.model.Questionnaire.QuestionnaireItemComponent t : src.getItem())
      tgt.addGroup(convertQuestionnaireGroupComponent(t));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.Questionnaire.QuestionnaireItemType convertQuestionnaireQuestionType(org.hl7.fhir.instance.model.Questionnaire.AnswerFormat src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case BOOLEAN: return org.hl7.fhir.dstu3.model.Questionnaire.QuestionnaireItemType.BOOLEAN;
    case DECIMAL: return org.hl7.fhir.dstu3.model.Questionnaire.QuestionnaireItemType.DECIMAL;
    case INTEGER: return org.hl7.fhir.dstu3.model.Questionnaire.QuestionnaireItemType.INTEGER;
    case DATE: return org.hl7.fhir.dstu3.model.Questionnaire.QuestionnaireItemType.DATE;
    case DATETIME: return org.hl7.fhir.dstu3.model.Questionnaire.QuestionnaireItemType.DATETIME;
    case INSTANT: return org.hl7.fhir.dstu3.model.Questionnaire.QuestionnaireItemType.DATETIME;
    case TIME: return org.hl7.fhir.dstu3.model.Questionnaire.QuestionnaireItemType.TIME;
    case STRING: return org.hl7.fhir.dstu3.model.Questionnaire.QuestionnaireItemType.STRING;
    case TEXT: return org.hl7.fhir.dstu3.model.Questionnaire.QuestionnaireItemType.TEXT;
    case URL: return org.hl7.fhir.dstu3.model.Questionnaire.QuestionnaireItemType.URL;
    case CHOICE: return org.hl7.fhir.dstu3.model.Questionnaire.QuestionnaireItemType.CHOICE;
    case OPENCHOICE: return org.hl7.fhir.dstu3.model.Questionnaire.QuestionnaireItemType.OPENCHOICE;
    case ATTACHMENT: return org.hl7.fhir.dstu3.model.Questionnaire.QuestionnaireItemType.ATTACHMENT;
    case REFERENCE: return org.hl7.fhir.dstu3.model.Questionnaire.QuestionnaireItemType.REFERENCE;
    case QUANTITY: return org.hl7.fhir.dstu3.model.Questionnaire.QuestionnaireItemType.QUANTITY;
    default: return org.hl7.fhir.dstu3.model.Questionnaire.QuestionnaireItemType.NULL;
    }
  }

  public org.hl7.fhir.instance.model.Questionnaire.AnswerFormat convertQuestionnaireItemType(org.hl7.fhir.dstu3.model.Questionnaire.QuestionnaireItemType src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case BOOLEAN: return org.hl7.fhir.instance.model.Questionnaire.AnswerFormat.BOOLEAN;
    case DECIMAL: return org.hl7.fhir.instance.model.Questionnaire.AnswerFormat.DECIMAL;
    case INTEGER: return org.hl7.fhir.instance.model.Questionnaire.AnswerFormat.INTEGER;
    case DATE: return org.hl7.fhir.instance.model.Questionnaire.AnswerFormat.DATE;
    case DATETIME: return org.hl7.fhir.instance.model.Questionnaire.AnswerFormat.DATETIME;
    case TIME: return org.hl7.fhir.instance.model.Questionnaire.AnswerFormat.TIME;
    case STRING: return org.hl7.fhir.instance.model.Questionnaire.AnswerFormat.STRING;
    case TEXT: return org.hl7.fhir.instance.model.Questionnaire.AnswerFormat.TEXT;
    case URL: return org.hl7.fhir.instance.model.Questionnaire.AnswerFormat.URL;
    case CHOICE: return org.hl7.fhir.instance.model.Questionnaire.AnswerFormat.CHOICE;
    case OPENCHOICE: return org.hl7.fhir.instance.model.Questionnaire.AnswerFormat.OPENCHOICE;
    case ATTACHMENT: return org.hl7.fhir.instance.model.Questionnaire.AnswerFormat.ATTACHMENT;
    case REFERENCE: return org.hl7.fhir.instance.model.Questionnaire.AnswerFormat.REFERENCE;
    case QUANTITY: return org.hl7.fhir.instance.model.Questionnaire.AnswerFormat.QUANTITY;
    default: return org.hl7.fhir.instance.model.Questionnaire.AnswerFormat.NULL;
    }
  }

  public org.hl7.fhir.dstu3.model.QuestionnaireResponse convertQuestionnaireResponse(org.hl7.fhir.instance.model.QuestionnaireResponse src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.QuestionnaireResponse tgt = new org.hl7.fhir.dstu3.model.QuestionnaireResponse();
    copyDomainResource(src, tgt);
    tgt.setIdentifier(convertIdentifier(src.getIdentifier()));
    tgt.setQuestionnaire(convertReference(src.getQuestionnaire()));
    tgt.setStatus(convertQuestionnaireResponseStatus(src.getStatus()));
    tgt.setSubject(convertReference(src.getSubject()));
    tgt.setAuthor(convertReference(src.getAuthor()));
    tgt.setAuthored(src.getAuthored());
    tgt.setSource(convertReference(src.getSource()));
    tgt.setContext(convertReference(src.getEncounter()));
    if (src.hasGroup())
      tgt.addItem(convertQuestionnaireResponseGroupComponent(src.getGroup()));
    return tgt;
  }

  public org.hl7.fhir.instance.model.QuestionnaireResponse convertQuestionnaireResponse(org.hl7.fhir.dstu3.model.QuestionnaireResponse src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.QuestionnaireResponse tgt = new org.hl7.fhir.instance.model.QuestionnaireResponse();
    copyDomainResource(src, tgt);
    tgt.setIdentifier(convertIdentifier(src.getIdentifier()));
    tgt.setQuestionnaire(convertReference(src.getQuestionnaire()));
    tgt.setStatus(convertQuestionnaireResponseStatus(src.getStatus()));
    tgt.setSubject(convertReference(src.getSubject()));
    tgt.setAuthor(convertReference(src.getAuthor()));
    tgt.setAuthored(src.getAuthored());
    tgt.setSource(convertReference(src.getSource()));
    tgt.setEncounter(convertReference(src.getContext()));

    if (src.getItem().size() != 1)
      throw new FHIRException("multiple root items not supported"); // though we could define a placeholder group?

    tgt.setGroup(convertQuestionnaireItemToGroup(src.getItem().get(0)));
    return tgt;
  }


  public org.hl7.fhir.dstu3.model.QuestionnaireResponse.QuestionnaireResponseStatus convertQuestionnaireResponseStatus(org.hl7.fhir.instance.model.QuestionnaireResponse.QuestionnaireResponseStatus src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case INPROGRESS: return org.hl7.fhir.dstu3.model.QuestionnaireResponse.QuestionnaireResponseStatus.INPROGRESS;
    case COMPLETED: return org.hl7.fhir.dstu3.model.QuestionnaireResponse.QuestionnaireResponseStatus.COMPLETED;
    case AMENDED: return org.hl7.fhir.dstu3.model.QuestionnaireResponse.QuestionnaireResponseStatus.AMENDED;
    default: return org.hl7.fhir.dstu3.model.QuestionnaireResponse.QuestionnaireResponseStatus.NULL;
    }
  }

  public org.hl7.fhir.instance.model.QuestionnaireResponse.QuestionnaireResponseStatus convertQuestionnaireResponseStatus(org.hl7.fhir.dstu3.model.QuestionnaireResponse.QuestionnaireResponseStatus src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case INPROGRESS: return org.hl7.fhir.instance.model.QuestionnaireResponse.QuestionnaireResponseStatus.INPROGRESS;
    case COMPLETED: return org.hl7.fhir.instance.model.QuestionnaireResponse.QuestionnaireResponseStatus.COMPLETED;
    case AMENDED: return org.hl7.fhir.instance.model.QuestionnaireResponse.QuestionnaireResponseStatus.AMENDED;
    default: return org.hl7.fhir.instance.model.QuestionnaireResponse.QuestionnaireResponseStatus.NULL;
    }
  }

  public org.hl7.fhir.dstu3.model.QuestionnaireResponse.QuestionnaireResponseItemComponent convertQuestionnaireResponseGroupComponent(org.hl7.fhir.instance.model.QuestionnaireResponse.GroupComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.QuestionnaireResponse.QuestionnaireResponseItemComponent tgt = new org.hl7.fhir.dstu3.model.QuestionnaireResponse.QuestionnaireResponseItemComponent();
    copyElement(src, tgt);
    tgt.setLinkId(src.getLinkId());
    tgt.setText(src.getText());
    tgt.setSubject(convertReference(src.getSubject()));
    for (org.hl7.fhir.instance.model.QuestionnaireResponse.GroupComponent t : src.getGroup())
      tgt.addItem(convertQuestionnaireResponseGroupComponent(t));
    for (org.hl7.fhir.instance.model.QuestionnaireResponse.QuestionComponent t : src.getQuestion())
      tgt.addItem(convertQuestionnaireResponseQuestionComponent(t));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.QuestionnaireResponse.QuestionnaireResponseItemComponent convertQuestionnaireResponseQuestionComponent(org.hl7.fhir.instance.model.QuestionnaireResponse.QuestionComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.QuestionnaireResponse.QuestionnaireResponseItemComponent tgt = new org.hl7.fhir.dstu3.model.QuestionnaireResponse.QuestionnaireResponseItemComponent();
    copyElement(src, tgt);
    tgt.setLinkId(src.getLinkId());
    tgt.setText(src.getText());
    for (org.hl7.fhir.instance.model.QuestionnaireResponse.QuestionAnswerComponent t : src.getAnswer())
      tgt.addAnswer(convertQuestionnaireResponseItemAnswerComponent(t));
    return tgt;
  }

  public org.hl7.fhir.instance.model.QuestionnaireResponse.GroupComponent convertQuestionnaireItemToGroup(org.hl7.fhir.dstu3.model.QuestionnaireResponse.QuestionnaireResponseItemComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.QuestionnaireResponse.GroupComponent tgt = new org.hl7.fhir.instance.model.QuestionnaireResponse.GroupComponent();
    copyElement(src, tgt);
    tgt.setLinkId(src.getLinkId());
    tgt.setText(src.getText());
    tgt.setSubject(convertReference(src.getSubject()));
    for (org.hl7.fhir.dstu3.model.QuestionnaireResponse.QuestionnaireResponseItemComponent t : src.getItem())
      if (t.hasAnswer())
        tgt.addQuestion(convertQuestionnaireItemToQuestion(t));
      else
        tgt.addGroup(convertQuestionnaireItemToGroup(t));
    return tgt;
  }

  public org.hl7.fhir.instance.model.QuestionnaireResponse.QuestionComponent convertQuestionnaireItemToQuestion(org.hl7.fhir.dstu3.model.QuestionnaireResponse.QuestionnaireResponseItemComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.QuestionnaireResponse.QuestionComponent tgt = new org.hl7.fhir.instance.model.QuestionnaireResponse.QuestionComponent();
    copyElement(src, tgt);
    tgt.setLinkId(src.getLinkId());
    tgt.setText(src.getText());
    for (org.hl7.fhir.dstu3.model.QuestionnaireResponse.QuestionnaireResponseItemAnswerComponent t : src.getAnswer())
      tgt.addAnswer(convertQuestionnaireResponseItemAnswerComponent(t));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.QuestionnaireResponse.QuestionnaireResponseItemAnswerComponent convertQuestionnaireResponseItemAnswerComponent(org.hl7.fhir.instance.model.QuestionnaireResponse.QuestionAnswerComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.QuestionnaireResponse.QuestionnaireResponseItemAnswerComponent tgt = new org.hl7.fhir.dstu3.model.QuestionnaireResponse.QuestionnaireResponseItemAnswerComponent();
    copyElement(src, tgt);
    tgt.setValue(convertType(src.getValue()));
    for (org.hl7.fhir.instance.model.QuestionnaireResponse.GroupComponent t : src.getGroup())
      tgt.addItem(convertQuestionnaireResponseGroupComponent(t));
    return tgt;
  }

  public org.hl7.fhir.instance.model.QuestionnaireResponse.QuestionAnswerComponent convertQuestionnaireResponseItemAnswerComponent(org.hl7.fhir.dstu3.model.QuestionnaireResponse.QuestionnaireResponseItemAnswerComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.QuestionnaireResponse.QuestionAnswerComponent tgt = new org.hl7.fhir.instance.model.QuestionnaireResponse.QuestionAnswerComponent();
    copyElement(src, tgt);
    tgt.setValue(convertType(src.getValue()));
    for (org.hl7.fhir.dstu3.model.QuestionnaireResponse.QuestionnaireResponseItemComponent t : src.getItem())
      tgt.addGroup(convertQuestionnaireItemToGroup(t));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.ReferralRequest convertReferralRequest(org.hl7.fhir.instance.model.ReferralRequest src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.ReferralRequest tgt = new org.hl7.fhir.dstu3.model.ReferralRequest();
    copyDomainResource(src, tgt);
    for (org.hl7.fhir.instance.model.Identifier t : src.getIdentifier())
      tgt.addIdentifier(convertIdentifier(t));
    tgt.setStatus(convertReferralStatus(src.getStatus()));
    tgt.setType(convertCodeableConcept(src.getType()));
    tgt.setPriority(convertReferralPriorityCode(src.getPriority()));
    tgt.setSubject(convertReference(src.getPatient()));
    tgt.setOccurrence(convertPeriod(src.getFulfillmentTime()));
    tgt.getRequester().setAgent(convertReference(src.getRequester()));
    tgt.setSpecialty(convertCodeableConcept(src.getSpecialty()));
    for (org.hl7.fhir.instance.model.Reference t : src.getRecipient())
      tgt.addRecipient(convertReference(t));
    tgt.addReasonCode(convertCodeableConcept(src.getReason()));
    tgt.setDescription(src.getDescription());
    for (org.hl7.fhir.instance.model.CodeableConcept t : src.getServiceRequested())
      tgt.addServiceRequested(convertCodeableConcept(t));
    for (org.hl7.fhir.instance.model.Reference t : src.getSupportingInformation())
      tgt.addSupportingInfo(convertReference(t));
    return tgt;
  }

  private ReferralPriority convertReferralPriorityCode(CodeableConcept priority) {
    for (org.hl7.fhir.instance.model.Coding c : priority.getCoding()) {
      if ("http://hl7.org/fhir/diagnostic-order-priority".equals(c.getSystem()) &&  "routine".equals(c.getCode()))
        return org.hl7.fhir.dstu3.model.ReferralRequest.ReferralPriority.ROUTINE;
      if ("http://hl7.org/fhir/diagnostic-order-priority".equals(c.getSystem()) &&  "urgent".equals(c.getCode()))
        return org.hl7.fhir.dstu3.model.ReferralRequest.ReferralPriority.URGENT;
      if ("http://hl7.org/fhir/diagnostic-order-priority".equals(c.getSystem()) &&  "stat".equals(c.getCode()))
        return org.hl7.fhir.dstu3.model.ReferralRequest.ReferralPriority.STAT;
      if ("http://hl7.org/fhir/diagnostic-order-priority".equals(c.getSystem()) &&  "asap".equals(c.getCode()))
        return org.hl7.fhir.dstu3.model.ReferralRequest.ReferralPriority.ASAP;
    }
    return null;
  }

  public org.hl7.fhir.instance.model.ReferralRequest convertReferralRequest(org.hl7.fhir.dstu3.model.ReferralRequest src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.ReferralRequest tgt = new org.hl7.fhir.instance.model.ReferralRequest();
    copyDomainResource(src, tgt);
    for (org.hl7.fhir.dstu3.model.Identifier t : src.getIdentifier())
      tgt.addIdentifier(convertIdentifier(t));
    tgt.setStatus(convertReferralStatus(src.getStatus()));
    tgt.setType(convertCodeableConcept(src.getType()));
    tgt.setPriority(convertReferralPriorityCode(src.getPriority()));
    tgt.setPatient(convertReference(src.getSubject()));
    tgt.setFulfillmentTime(convertPeriod(src.getOccurrencePeriod()));
    tgt.setRequester(convertReference(src.getRequester().getAgent()));
    tgt.setSpecialty(convertCodeableConcept(src.getSpecialty()));
    for (org.hl7.fhir.dstu3.model.Reference t : src.getRecipient())
      tgt.addRecipient(convertReference(t));
    for (org.hl7.fhir.dstu3.model.CodeableConcept cc : src.getReasonCode())
      tgt.setReason(convertCodeableConcept(cc));
    tgt.setDescription(src.getDescription());
    for (org.hl7.fhir.dstu3.model.CodeableConcept t : src.getServiceRequested())
      tgt.addServiceRequested(convertCodeableConcept(t));
    for (org.hl7.fhir.dstu3.model.Reference t : src.getSupportingInfo())
      tgt.addSupportingInformation(convertReference(t));
    return tgt;
  }

  private org.hl7.fhir.instance.model.CodeableConcept convertReferralPriorityCode(org.hl7.fhir.dstu3.model.ReferralRequest.ReferralPriority priority) {
    org.hl7.fhir.instance.model.CodeableConcept cc = new org.hl7.fhir.instance.model.CodeableConcept();
    switch (priority) {
    case ROUTINE: cc.addCoding().setSystem("http://hl7.org/fhir/diagnostic-order-priority").setCode("routine"); break;
    case URGENT: cc.addCoding().setSystem("http://hl7.org/fhir/diagnostic-order-priority").setCode("urgent"); break;
    case STAT: cc.addCoding().setSystem("http://hl7.org/fhir/diagnostic-order-priority").setCode("stat"); break;
    case ASAP: cc.addCoding().setSystem("http://hl7.org/fhir/diagnostic-order-priority").setCode("asap"); break;
    default: return null;
    }
    return cc;
  }


  public org.hl7.fhir.dstu3.model.ReferralRequest.ReferralRequestStatus convertReferralStatus(org.hl7.fhir.instance.model.ReferralRequest.ReferralStatus src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case DRAFT: return org.hl7.fhir.dstu3.model.ReferralRequest.ReferralRequestStatus.DRAFT;
    case REQUESTED: return org.hl7.fhir.dstu3.model.ReferralRequest.ReferralRequestStatus.DRAFT;
    case ACTIVE: return org.hl7.fhir.dstu3.model.ReferralRequest.ReferralRequestStatus.ACTIVE;
    case CANCELLED: return org.hl7.fhir.dstu3.model.ReferralRequest.ReferralRequestStatus.CANCELLED;
    case ACCEPTED: return org.hl7.fhir.dstu3.model.ReferralRequest.ReferralRequestStatus.ACTIVE;
    case REJECTED: return org.hl7.fhir.dstu3.model.ReferralRequest.ReferralRequestStatus.ENTEREDINERROR;
    case COMPLETED: return org.hl7.fhir.dstu3.model.ReferralRequest.ReferralRequestStatus.COMPLETED;
    default: return org.hl7.fhir.dstu3.model.ReferralRequest.ReferralRequestStatus.NULL;
    }
  }

  public org.hl7.fhir.instance.model.ReferralRequest.ReferralStatus convertReferralStatus(org.hl7.fhir.dstu3.model.ReferralRequest.ReferralRequestStatus src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case DRAFT: return org.hl7.fhir.instance.model.ReferralRequest.ReferralStatus.DRAFT;
    case ACTIVE: return org.hl7.fhir.instance.model.ReferralRequest.ReferralStatus.ACTIVE;
    case CANCELLED: return org.hl7.fhir.instance.model.ReferralRequest.ReferralStatus.CANCELLED;
    case COMPLETED: return org.hl7.fhir.instance.model.ReferralRequest.ReferralStatus.COMPLETED;
    case ENTEREDINERROR: return org.hl7.fhir.instance.model.ReferralRequest.ReferralStatus.REJECTED;
    default: return org.hl7.fhir.instance.model.ReferralRequest.ReferralStatus.NULL;
    }
  }

  public org.hl7.fhir.dstu3.model.RelatedPerson convertRelatedPerson(org.hl7.fhir.instance.model.RelatedPerson src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.RelatedPerson tgt = new org.hl7.fhir.dstu3.model.RelatedPerson();
    copyDomainResource(src, tgt);
    for (org.hl7.fhir.instance.model.Identifier t : src.getIdentifier())
      tgt.addIdentifier(convertIdentifier(t));
    tgt.setPatient(convertReference(src.getPatient()));
    tgt.setRelationship(convertCodeableConcept(src.getRelationship()));
    tgt.addName(convertHumanName(src.getName()));
    for (org.hl7.fhir.instance.model.ContactPoint t : src.getTelecom())
      tgt.addTelecom(convertContactPoint(t));
    tgt.setGender(convertAdministrativeGender(src.getGender()));
    tgt.setBirthDate(src.getBirthDate());
    for (org.hl7.fhir.instance.model.Address t : src.getAddress())
      tgt.addAddress(convertAddress(t));
    for (org.hl7.fhir.instance.model.Attachment t : src.getPhoto())
      tgt.addPhoto(convertAttachment(t));
    tgt.setPeriod(convertPeriod(src.getPeriod()));
    return tgt;
  }

  public org.hl7.fhir.instance.model.RelatedPerson convertRelatedPerson(org.hl7.fhir.dstu3.model.RelatedPerson src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.RelatedPerson tgt = new org.hl7.fhir.instance.model.RelatedPerson();
    copyDomainResource(src, tgt);
    for (org.hl7.fhir.dstu3.model.Identifier t : src.getIdentifier())
      tgt.addIdentifier(convertIdentifier(t));
    tgt.setPatient(convertReference(src.getPatient()));
    tgt.setRelationship(convertCodeableConcept(src.getRelationship()));
    if (!src.getName().isEmpty())
    tgt.setName(convertHumanName(src.getName().get(0)));
    for (org.hl7.fhir.dstu3.model.ContactPoint t : src.getTelecom())
      tgt.addTelecom(convertContactPoint(t));
    tgt.setGender(convertAdministrativeGender(src.getGender()));
    tgt.setBirthDate(src.getBirthDate());
    for (org.hl7.fhir.dstu3.model.Address t : src.getAddress())
      tgt.addAddress(convertAddress(t));
    for (org.hl7.fhir.dstu3.model.Attachment t : src.getPhoto())
      tgt.addPhoto(convertAttachment(t));
    tgt.setPeriod(convertPeriod(src.getPeriod()));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.RiskAssessment convertRiskAssessment(org.hl7.fhir.instance.model.RiskAssessment src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.RiskAssessment tgt = new org.hl7.fhir.dstu3.model.RiskAssessment();
    copyDomainResource(src, tgt);
    tgt.setSubject(convertReference(src.getSubject()));
//    tgt.setDate(src.getDate());
    tgt.setCondition(convertReference(src.getCondition()));
    tgt.setContext(convertReference(src.getEncounter()));
    tgt.setPerformer(convertReference(src.getPerformer()));
    tgt.setIdentifier(convertIdentifier(src.getIdentifier()));
    tgt.setMethod(convertCodeableConcept(src.getMethod()));
    for (org.hl7.fhir.instance.model.Reference t : src.getBasis())
      tgt.addBasis(convertReference(t));
    for (org.hl7.fhir.instance.model.RiskAssessment.RiskAssessmentPredictionComponent t : src.getPrediction())
      tgt.addPrediction(convertRiskAssessmentPredictionComponent(t));
    tgt.setMitigation(src.getMitigation());
    return tgt;
  }

  public org.hl7.fhir.instance.model.RiskAssessment convertRiskAssessment(org.hl7.fhir.dstu3.model.RiskAssessment src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.RiskAssessment tgt = new org.hl7.fhir.instance.model.RiskAssessment();
    copyDomainResource(src, tgt);
    tgt.setSubject(convertReference(src.getSubject()));
//    tgt.setDateElement(src.getOccurrenceDateTimeType());
    tgt.setCondition(convertReference(src.getCondition()));
    tgt.setEncounter(convertReference(src.getContext()));
    tgt.setPerformer(convertReference(src.getPerformer()));
    tgt.setIdentifier(convertIdentifier(src.getIdentifier()));
    tgt.setMethod(convertCodeableConcept(src.getMethod()));
    for (org.hl7.fhir.dstu3.model.Reference t : src.getBasis())
      tgt.addBasis(convertReference(t));
    for (org.hl7.fhir.dstu3.model.RiskAssessment.RiskAssessmentPredictionComponent t : src.getPrediction())
      tgt.addPrediction(convertRiskAssessmentPredictionComponent(t));
    tgt.setMitigation(src.getMitigation());
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.RiskAssessment.RiskAssessmentPredictionComponent convertRiskAssessmentPredictionComponent(org.hl7.fhir.instance.model.RiskAssessment.RiskAssessmentPredictionComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.RiskAssessment.RiskAssessmentPredictionComponent tgt = new org.hl7.fhir.dstu3.model.RiskAssessment.RiskAssessmentPredictionComponent();
    copyElement(src, tgt);
    tgt.setOutcome(convertCodeableConcept(src.getOutcome()));
    tgt.setProbability(convertType(src.getProbability()));
    tgt.setRelativeRisk(src.getRelativeRisk());
    tgt.setWhen(convertType(src.getWhen()));
    tgt.setRationale(src.getRationale());
    return tgt;
  }

  public org.hl7.fhir.instance.model.RiskAssessment.RiskAssessmentPredictionComponent convertRiskAssessmentPredictionComponent(org.hl7.fhir.dstu3.model.RiskAssessment.RiskAssessmentPredictionComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.RiskAssessment.RiskAssessmentPredictionComponent tgt = new org.hl7.fhir.instance.model.RiskAssessment.RiskAssessmentPredictionComponent();
    copyElement(src, tgt);
    tgt.setOutcome(convertCodeableConcept(src.getOutcome()));
    tgt.setProbability(convertType(src.getProbability()));
    tgt.setRelativeRisk(src.getRelativeRisk());
    tgt.setWhen(convertType(src.getWhen()));
    tgt.setRationale(src.getRationale());
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.Schedule convertSchedule(org.hl7.fhir.instance.model.Schedule src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.Schedule tgt = new org.hl7.fhir.dstu3.model.Schedule();
    copyDomainResource(src, tgt);
    for (org.hl7.fhir.instance.model.Identifier t : src.getIdentifier())
      tgt.addIdentifier(convertIdentifier(t));
    for (org.hl7.fhir.instance.model.CodeableConcept t : src.getType())
      tgt.addServiceType(convertCodeableConcept(t));
    tgt.addActor(convertReference(src.getActor()));
    tgt.setPlanningHorizon(convertPeriod(src.getPlanningHorizon()));
    tgt.setComment(src.getComment());
    return tgt;
  }

  public org.hl7.fhir.instance.model.Schedule convertSchedule(org.hl7.fhir.dstu3.model.Schedule src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.Schedule tgt = new org.hl7.fhir.instance.model.Schedule();
    copyDomainResource(src, tgt);
    for (org.hl7.fhir.dstu3.model.Identifier t : src.getIdentifier())
      tgt.addIdentifier(convertIdentifier(t));
    for (org.hl7.fhir.dstu3.model.CodeableConcept t : src.getServiceType())
      tgt.addType(convertCodeableConcept(t));
    tgt.setActor(convertReference(src.getActorFirstRep()));
    tgt.setPlanningHorizon(convertPeriod(src.getPlanningHorizon()));
    tgt.setComment(src.getComment());
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.SearchParameter convertSearchParameter(org.hl7.fhir.instance.model.SearchParameter src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.SearchParameter tgt = new org.hl7.fhir.dstu3.model.SearchParameter();
    copyDomainResource(src, tgt);
    tgt.setUrl(src.getUrl());
    tgt.setName(src.getName());
    tgt.setStatus(convertConformanceResourceStatus(src.getStatus()));
    if (src.hasExperimental())
      tgt.setExperimental(src.getExperimental());
    if (src.hasDate())
      tgt.setDate(src.getDate());
    tgt.setPublisher(src.getPublisher());
    for (org.hl7.fhir.instance.model.SearchParameter.SearchParameterContactComponent t : src.getContact())
      tgt.addContact(convertSearchParameterContactComponent(t));
    tgt.setPurpose(src.getRequirements());
    tgt.setCode(src.getCode());
    tgt.addBase(src.getBase());
    tgt.setType(convertSearchParamType(src.getType()));
    tgt.setDescription(src.getDescription());
    tgt.setExpression(ToolingExtensions.readStringExtension(src, ToolingExtensions.EXT_EXPRESSION));
    tgt.setXpath(src.getXpath());
    tgt.setXpathUsage(convertXPathUsageType(src.getXpathUsage()));
    for (org.hl7.fhir.instance.model.CodeType t : src.getTarget())
      tgt.addTarget(t.getValue());
    return tgt;
  }

  public org.hl7.fhir.instance.model.SearchParameter convertSearchParameter(org.hl7.fhir.dstu3.model.SearchParameter src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.SearchParameter tgt = new org.hl7.fhir.instance.model.SearchParameter();
    copyDomainResource(src, tgt);
    tgt.setUrl(src.getUrl());
    tgt.setName(src.getName());
    tgt.setStatus(convertConformanceResourceStatus(src.getStatus()));
    if (src.hasExperimental())
      tgt.setExperimental(src.getExperimental());
    if (src.hasDate())
      tgt.setDate(src.getDate());
    tgt.setPublisher(src.getPublisher());
    for (org.hl7.fhir.dstu3.model.ContactDetail t : src.getContact())
      tgt.addContact(convertSearchParameterContactComponent(t));
    tgt.setRequirements(src.getPurpose());
    tgt.setCode(src.getCode());
    for (org.hl7.fhir.dstu3.model.CodeType t : src.getBase())
      tgt.setBase(t.asStringValue());
    tgt.setType(convertSearchParamType(src.getType()));
    tgt.setDescription(src.getDescription());
    org.hl7.fhir.instance.utils.ToolingExtensions.setStringExtension(tgt, ToolingExtensions.EXT_EXPRESSION, src.getExpression());
    tgt.setXpath(src.getXpath());
    tgt.setXpathUsage(convertXPathUsageType(src.getXpathUsage()));
    for (org.hl7.fhir.dstu3.model.CodeType t : src.getTarget())
      tgt.addTarget(t.getValue());
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.SearchParameter.XPathUsageType convertXPathUsageType(org.hl7.fhir.instance.model.SearchParameter.XPathUsageType src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case NORMAL: return org.hl7.fhir.dstu3.model.SearchParameter.XPathUsageType.NORMAL;
    case PHONETIC: return org.hl7.fhir.dstu3.model.SearchParameter.XPathUsageType.PHONETIC;
    case NEARBY: return org.hl7.fhir.dstu3.model.SearchParameter.XPathUsageType.NEARBY;
    case DISTANCE: return org.hl7.fhir.dstu3.model.SearchParameter.XPathUsageType.DISTANCE;
    case OTHER: return org.hl7.fhir.dstu3.model.SearchParameter.XPathUsageType.OTHER;
    default: return org.hl7.fhir.dstu3.model.SearchParameter.XPathUsageType.NULL;
    }
  }

  public org.hl7.fhir.instance.model.SearchParameter.XPathUsageType convertXPathUsageType(org.hl7.fhir.dstu3.model.SearchParameter.XPathUsageType src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case NORMAL: return org.hl7.fhir.instance.model.SearchParameter.XPathUsageType.NORMAL;
    case PHONETIC: return org.hl7.fhir.instance.model.SearchParameter.XPathUsageType.PHONETIC;
    case NEARBY: return org.hl7.fhir.instance.model.SearchParameter.XPathUsageType.NEARBY;
    case DISTANCE: return org.hl7.fhir.instance.model.SearchParameter.XPathUsageType.DISTANCE;
    case OTHER: return org.hl7.fhir.instance.model.SearchParameter.XPathUsageType.OTHER;
    default: return org.hl7.fhir.instance.model.SearchParameter.XPathUsageType.NULL;
    }
  }

  public org.hl7.fhir.dstu3.model.ContactDetail convertSearchParameterContactComponent(org.hl7.fhir.instance.model.SearchParameter.SearchParameterContactComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.ContactDetail tgt = new org.hl7.fhir.dstu3.model.ContactDetail();
    copyElement(src, tgt);
    tgt.setName(src.getName());
    for (org.hl7.fhir.instance.model.ContactPoint t : src.getTelecom())
      tgt.addTelecom(convertContactPoint(t));
    return tgt;
  }

  public org.hl7.fhir.instance.model.SearchParameter.SearchParameterContactComponent convertSearchParameterContactComponent(org.hl7.fhir.dstu3.model.ContactDetail src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.SearchParameter.SearchParameterContactComponent tgt = new org.hl7.fhir.instance.model.SearchParameter.SearchParameterContactComponent();
    copyElement(src, tgt);
    tgt.setName(src.getName());
    for (org.hl7.fhir.dstu3.model.ContactPoint t : src.getTelecom())
      tgt.addTelecom(convertContactPoint(t));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.Slot convertSlot(org.hl7.fhir.instance.model.Slot src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.Slot tgt = new org.hl7.fhir.dstu3.model.Slot();
    copyDomainResource(src, tgt);
    for (org.hl7.fhir.instance.model.Identifier t : src.getIdentifier())
      tgt.addIdentifier(convertIdentifier(t));
    if (src.hasType())
      tgt.addServiceType(convertCodeableConcept(src.getType()));
    tgt.setSchedule(convertReference(src.getSchedule()));
    tgt.setStart(src.getStart());
    tgt.setEnd(src.getEnd());
    tgt.setOverbooked(src.getOverbooked());
    tgt.setComment(src.getComment());
    return tgt;
  }

  public org.hl7.fhir.instance.model.Slot convertSlot(org.hl7.fhir.dstu3.model.Slot src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.Slot tgt = new org.hl7.fhir.instance.model.Slot();
    copyDomainResource(src, tgt);
    for (org.hl7.fhir.dstu3.model.Identifier t : src.getIdentifier())
      tgt.addIdentifier(convertIdentifier(t));
    for (org.hl7.fhir.dstu3.model.CodeableConcept t : src.getServiceType())
      tgt.setType(convertCodeableConcept(t));
    tgt.setSchedule(convertReference(src.getSchedule()));
    tgt.setStart(src.getStart());
    tgt.setEnd(src.getEnd());
    tgt.setOverbooked(src.getOverbooked());
    tgt.setComment(src.getComment());
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.Slot.SlotStatus convertSlotStatus(org.hl7.fhir.instance.model.Slot.SlotStatus src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case BUSY: return org.hl7.fhir.dstu3.model.Slot.SlotStatus.BUSY;
    case FREE: return org.hl7.fhir.dstu3.model.Slot.SlotStatus.FREE;
    case BUSYUNAVAILABLE: return org.hl7.fhir.dstu3.model.Slot.SlotStatus.BUSYUNAVAILABLE;
    case BUSYTENTATIVE: return org.hl7.fhir.dstu3.model.Slot.SlotStatus.BUSYTENTATIVE;
    default: return org.hl7.fhir.dstu3.model.Slot.SlotStatus.NULL;
    }
  }

  public org.hl7.fhir.instance.model.Slot.SlotStatus convertSlotStatus(org.hl7.fhir.dstu3.model.Slot.SlotStatus src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case BUSY: return org.hl7.fhir.instance.model.Slot.SlotStatus.BUSY;
    case FREE: return org.hl7.fhir.instance.model.Slot.SlotStatus.FREE;
    case BUSYUNAVAILABLE: return org.hl7.fhir.instance.model.Slot.SlotStatus.BUSYUNAVAILABLE;
    case BUSYTENTATIVE: return org.hl7.fhir.instance.model.Slot.SlotStatus.BUSYTENTATIVE;
    default: return org.hl7.fhir.instance.model.Slot.SlotStatus.NULL;
    }
  }

  public org.hl7.fhir.dstu3.model.StructureDefinition convertStructureDefinition(org.hl7.fhir.instance.model.StructureDefinition src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.StructureDefinition tgt = new org.hl7.fhir.dstu3.model.StructureDefinition();
    copyDomainResource(src, tgt);
    tgt.setUrl(src.getUrl());
    for (org.hl7.fhir.instance.model.Identifier t : src.getIdentifier())
      tgt.addIdentifier(convertIdentifier(t));
    tgt.setVersion(src.getVersion());
    tgt.setName(src.getName());
    tgt.setTitle(src.getDisplay());
    tgt.setStatus(convertConformanceResourceStatus(src.getStatus()));
    if (src.hasExperimental())
      tgt.setExperimental(src.getExperimental());
    tgt.setPublisher(src.getPublisher());
    for (org.hl7.fhir.instance.model.StructureDefinition.StructureDefinitionContactComponent t : src.getContact())
      tgt.addContact(convertStructureDefinitionContactComponent(t));
    if (src.hasDate())
      tgt.setDate(src.getDate());
    tgt.setDescription(src.getDescription());
    for (org.hl7.fhir.instance.model.CodeableConcept t : src.getUseContext())
      if (isJurisdiction(t))
        tgt.addJurisdiction(convertCodeableConcept(t));
      else
        tgt.addUseContext(convertCodeableConceptToUsageContext(t));
    tgt.setPurpose(src.getRequirements());
    tgt.setCopyright(src.getCopyright());
    for (org.hl7.fhir.instance.model.Coding t : src.getCode())
      tgt.addKeyword(convertCoding(t));
    tgt.setFhirVersion(src.getFhirVersion());
    for (org.hl7.fhir.instance.model.StructureDefinition.StructureDefinitionMappingComponent t : src.getMapping())
      tgt.addMapping(convertStructureDefinitionMappingComponent(t));
    tgt.setKind(convertStructureDefinitionKind(src.getKind(), tgt.getId()));
    tgt.setAbstract(src.getAbstract());
    tgt.setContextType(convertExtensionContext(src.getContextType()));
    for (org.hl7.fhir.instance.model.StringType t : src.getContext())
      tgt.addContext(t.getValue());
    if (src.hasConstrainedType())
      tgt.setType(src.getConstrainedType());
    else if (src.getSnapshot().hasElement())
      tgt.setType(src.getSnapshot().getElement().get(0).getPath());
    else if (src.getDifferential().hasElement() && !src.getDifferential().getElement().get(0).getPath().contains("."))
      tgt.setType(src.getDifferential().getElement().get(0).getPath());
    else
      tgt.setType(src.getDifferential().getElement().get(0).getPath().substring(0, src.getDifferential().getElement().get(0).getPath().indexOf(".")));
    tgt.setBaseDefinition(src.getBase());
    tgt.setDerivation(src.hasConstrainedType() ?  org.hl7.fhir.dstu3.model.StructureDefinition.TypeDerivationRule.CONSTRAINT : org.hl7.fhir.dstu3.model.StructureDefinition.TypeDerivationRule.SPECIALIZATION);
    tgt.setSnapshot(convertStructureDefinitionSnapshotComponent(src.getSnapshot()));
    tgt.setDifferential(convertStructureDefinitionDifferentialComponent(src.getDifferential()));
    if (tgt.hasSnapshot())
      tgt.getSnapshot().getElementFirstRep().getType().clear();
    if (tgt.hasDifferential())
      tgt.getDifferential().getElementFirstRep().getType().clear();
    if (tgt.getKind() == StructureDefinitionKind.PRIMITIVETYPE && !tgt.getType().equals(tgt.getId())) {
      tgt.setDerivation(TypeDerivationRule.SPECIALIZATION);
      tgt.setBaseDefinition("http://hl7.org/fhir/StructureDefinition/"+tgt.getType());
//      for (ElementDefinition ed : tgt.getSnapshot().getElement()) {
//        ed.setPath(ed.getPath().replace(tgt.getType()+".", tgt.getId()+"."));
//      }
//      for (ElementDefinition ed : tgt.getDifferential().getElement()) {
//        ed.setPath(ed.getPath().replace(tgt.getType()+".", tgt.getId()+"."));
//      }
      tgt.setType(tgt.getId());
    }
    return tgt;
  }

  public org.hl7.fhir.instance.model.StructureDefinition convertStructureDefinition(org.hl7.fhir.dstu3.model.StructureDefinition src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.StructureDefinition tgt = new org.hl7.fhir.instance.model.StructureDefinition();
    copyDomainResource(src, tgt);
    tgt.setUrl(src.getUrl());
    for (org.hl7.fhir.dstu3.model.Identifier t : src.getIdentifier())
      tgt.addIdentifier(convertIdentifier(t));
    tgt.setVersion(src.getVersion());
    tgt.setName(src.getName());
    tgt.setDisplay(src.getTitle());
    tgt.setStatus(convertConformanceResourceStatus(src.getStatus()));
    if (src.hasExperimental())
      tgt.setExperimental(src.getExperimental());
    tgt.setPublisher(src.getPublisher());
    for (org.hl7.fhir.dstu3.model.ContactDetail t : src.getContact())
      tgt.addContact(convertStructureDefinitionContactComponent(t));
    if (src.hasDate())
      tgt.setDate(src.getDate());
    tgt.setDescription(src.getDescription());
    for (org.hl7.fhir.dstu3.model.UsageContext t : src.getUseContext())
      if (t.hasValueCodeableConcept())
        tgt.addUseContext(convertCodeableConcept(t.getValueCodeableConcept()));
    for (org.hl7.fhir.dstu3.model.CodeableConcept t : src.getJurisdiction())
        tgt.addUseContext(convertCodeableConcept(t));
    tgt.setRequirements(src.getPurpose());
    tgt.setCopyright(src.getCopyright());
    for (org.hl7.fhir.dstu3.model.Coding t : src.getKeyword())
      tgt.addCode(convertCoding(t));
    tgt.setFhirVersion(src.getFhirVersion());
    for (org.hl7.fhir.dstu3.model.StructureDefinition.StructureDefinitionMappingComponent t : src.getMapping())
      tgt.addMapping(convertStructureDefinitionMappingComponent(t));
    tgt.setKind(convertStructureDefinitionKind(src.getKind()));
    tgt.setAbstract(src.getAbstract());
    tgt.setContextType(convertExtensionContext(src.getContextType()));
    for (org.hl7.fhir.dstu3.model.StringType t : src.getContext())
      tgt.addContext(t.getValue());
    tgt.setConstrainedType(src.getType());
    tgt.setBase(src.getBaseDefinition());
    tgt.setSnapshot(convertStructureDefinitionSnapshotComponent(src.getSnapshot()));
    tgt.setDifferential(convertStructureDefinitionDifferentialComponent(src.getDifferential()));
    if (tgt.hasBase()) {
      if (tgt.hasDifferential())
        tgt.getDifferential().getElement().get(0).addType().setCode(tail(tgt.getBase()));
      if (tgt.hasSnapshot())
        tgt.getSnapshot().getElement().get(0).addType().setCode(tail(tgt.getBase()));
    }
    return tgt;
  }

  private String tail(String base) {
    return base.substring(base.lastIndexOf("/")+1);
  }

  public org.hl7.fhir.dstu3.model.StructureDefinition.StructureDefinitionKind convertStructureDefinitionKind(org.hl7.fhir.instance.model.StructureDefinition.StructureDefinitionKind src, String dtName) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case DATATYPE:
      if (Utilities.existsInList(dtName, "boolean", "integer", "decimal", "base64Binary", "instant", "string", "uri", "date", "dateTime", "time", "code", "oid", "uuid", "id", "unsignedInt", "positiveInt", "markdown", "xhtml"))
        return org.hl7.fhir.dstu3.model.StructureDefinition.StructureDefinitionKind.PRIMITIVETYPE;
      else
        return org.hl7.fhir.dstu3.model.StructureDefinition.StructureDefinitionKind.COMPLEXTYPE;
    case RESOURCE: return org.hl7.fhir.dstu3.model.StructureDefinition.StructureDefinitionKind.RESOURCE;
    case LOGICAL: return org.hl7.fhir.dstu3.model.StructureDefinition.StructureDefinitionKind.LOGICAL;
    default: return org.hl7.fhir.dstu3.model.StructureDefinition.StructureDefinitionKind.NULL;
    }
  }

  public org.hl7.fhir.instance.model.StructureDefinition.StructureDefinitionKind convertStructureDefinitionKind(org.hl7.fhir.dstu3.model.StructureDefinition.StructureDefinitionKind src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case PRIMITIVETYPE: return org.hl7.fhir.instance.model.StructureDefinition.StructureDefinitionKind.DATATYPE;
    case COMPLEXTYPE: return org.hl7.fhir.instance.model.StructureDefinition.StructureDefinitionKind.DATATYPE;
    case RESOURCE: return org.hl7.fhir.instance.model.StructureDefinition.StructureDefinitionKind.RESOURCE;
    case LOGICAL: return org.hl7.fhir.instance.model.StructureDefinition.StructureDefinitionKind.LOGICAL;
    default: return org.hl7.fhir.instance.model.StructureDefinition.StructureDefinitionKind.NULL;
    }
  }

  public org.hl7.fhir.dstu3.model.StructureDefinition.ExtensionContext convertExtensionContext(org.hl7.fhir.instance.model.StructureDefinition.ExtensionContext src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case RESOURCE: return org.hl7.fhir.dstu3.model.StructureDefinition.ExtensionContext.RESOURCE;
    case DATATYPE: return org.hl7.fhir.dstu3.model.StructureDefinition.ExtensionContext.DATATYPE;
    case EXTENSION: return org.hl7.fhir.dstu3.model.StructureDefinition.ExtensionContext.EXTENSION;
    default: return org.hl7.fhir.dstu3.model.StructureDefinition.ExtensionContext.NULL;
    }
  }

  public org.hl7.fhir.instance.model.StructureDefinition.ExtensionContext convertExtensionContext(org.hl7.fhir.dstu3.model.StructureDefinition.ExtensionContext src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case RESOURCE: return org.hl7.fhir.instance.model.StructureDefinition.ExtensionContext.RESOURCE;
    case DATATYPE: return org.hl7.fhir.instance.model.StructureDefinition.ExtensionContext.DATATYPE;
    case EXTENSION: return org.hl7.fhir.instance.model.StructureDefinition.ExtensionContext.EXTENSION;
    default: return org.hl7.fhir.instance.model.StructureDefinition.ExtensionContext.NULL;
    }
  }

  public org.hl7.fhir.dstu3.model.ContactDetail convertStructureDefinitionContactComponent(org.hl7.fhir.instance.model.StructureDefinition.StructureDefinitionContactComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.ContactDetail tgt = new org.hl7.fhir.dstu3.model.ContactDetail();
    copyElement(src, tgt);
    tgt.setName(src.getName());
    for (org.hl7.fhir.instance.model.ContactPoint t : src.getTelecom())
      tgt.addTelecom(convertContactPoint(t));
    return tgt;
  }

  public org.hl7.fhir.instance.model.StructureDefinition.StructureDefinitionContactComponent convertStructureDefinitionContactComponent(org.hl7.fhir.dstu3.model.ContactDetail src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.StructureDefinition.StructureDefinitionContactComponent tgt = new org.hl7.fhir.instance.model.StructureDefinition.StructureDefinitionContactComponent();
    copyElement(src, tgt);
    tgt.setName(src.getName());
    for (org.hl7.fhir.dstu3.model.ContactPoint t : src.getTelecom())
      tgt.addTelecom(convertContactPoint(t));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.StructureDefinition.StructureDefinitionMappingComponent convertStructureDefinitionMappingComponent(org.hl7.fhir.instance.model.StructureDefinition.StructureDefinitionMappingComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.StructureDefinition.StructureDefinitionMappingComponent tgt = new org.hl7.fhir.dstu3.model.StructureDefinition.StructureDefinitionMappingComponent();
    copyElement(src, tgt);
    tgt.setIdentity(src.getIdentity());
    tgt.setUri(src.getUri());
    tgt.setName(src.getName());
    tgt.setComment(src.getComments());
    return tgt;
  }

  public org.hl7.fhir.instance.model.StructureDefinition.StructureDefinitionMappingComponent convertStructureDefinitionMappingComponent(org.hl7.fhir.dstu3.model.StructureDefinition.StructureDefinitionMappingComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.StructureDefinition.StructureDefinitionMappingComponent tgt = new org.hl7.fhir.instance.model.StructureDefinition.StructureDefinitionMappingComponent();
    copyElement(src, tgt);
    tgt.setIdentity(src.getIdentity());
    tgt.setUri(src.getUri());
    tgt.setName(src.getName());
    tgt.setComments(src.getComment());
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.StructureDefinition.StructureDefinitionSnapshotComponent convertStructureDefinitionSnapshotComponent(org.hl7.fhir.instance.model.StructureDefinition.StructureDefinitionSnapshotComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.StructureDefinition.StructureDefinitionSnapshotComponent tgt = new org.hl7.fhir.dstu3.model.StructureDefinition.StructureDefinitionSnapshotComponent();
    copyElement(src, tgt);
    List<String> slicePaths = new ArrayList<String>();
    for (org.hl7.fhir.instance.model.ElementDefinition t : src.getElement()) {
      if (t.hasSlicing())
        slicePaths.add(t.getPath());
      tgt.addElement(convertElementDefinition(t, slicePaths));
    }
    return tgt;
  }

  public org.hl7.fhir.instance.model.StructureDefinition.StructureDefinitionSnapshotComponent convertStructureDefinitionSnapshotComponent(org.hl7.fhir.dstu3.model.StructureDefinition.StructureDefinitionSnapshotComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.StructureDefinition.StructureDefinitionSnapshotComponent tgt = new org.hl7.fhir.instance.model.StructureDefinition.StructureDefinitionSnapshotComponent();
    copyElement(src, tgt);
    for (org.hl7.fhir.dstu3.model.ElementDefinition t : src.getElement())
      tgt.addElement(convertElementDefinition(t));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.StructureDefinition.StructureDefinitionDifferentialComponent convertStructureDefinitionDifferentialComponent(org.hl7.fhir.instance.model.StructureDefinition.StructureDefinitionDifferentialComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.StructureDefinition.StructureDefinitionDifferentialComponent tgt = new org.hl7.fhir.dstu3.model.StructureDefinition.StructureDefinitionDifferentialComponent();
    copyElement(src, tgt);
    List<String> slicePaths = new ArrayList<String>();
    for (org.hl7.fhir.instance.model.ElementDefinition t : src.getElement()) {
      if (t.hasSlicing())
        slicePaths.add(t.getPath());
      tgt.addElement(convertElementDefinition(t, slicePaths));
    }
    return tgt;
  }

  public org.hl7.fhir.instance.model.StructureDefinition.StructureDefinitionDifferentialComponent convertStructureDefinitionDifferentialComponent(org.hl7.fhir.dstu3.model.StructureDefinition.StructureDefinitionDifferentialComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.StructureDefinition.StructureDefinitionDifferentialComponent tgt = new org.hl7.fhir.instance.model.StructureDefinition.StructureDefinitionDifferentialComponent();
    copyElement(src, tgt);
    for (org.hl7.fhir.dstu3.model.ElementDefinition t : src.getElement())
      tgt.addElement(convertElementDefinition(t));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.Subscription convertSubscription(org.hl7.fhir.instance.model.Subscription src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.Subscription tgt = new org.hl7.fhir.dstu3.model.Subscription();
    copyDomainResource(src, tgt);
    tgt.setCriteria(src.getCriteria());
    for (org.hl7.fhir.instance.model.ContactPoint t : src.getContact())
      tgt.addContact(convertContactPoint(t));
    tgt.setReason(src.getReason());
    tgt.setStatus(convertSubscriptionStatus(src.getStatus()));
    tgt.setError(src.getError());
    tgt.setChannel(convertSubscriptionChannelComponent(src.getChannel()));
    tgt.setEnd(src.getEnd());
    for (org.hl7.fhir.instance.model.Coding t : src.getTag())
      tgt.addTag(convertCoding(t));
    return tgt;
  }

  public org.hl7.fhir.instance.model.Subscription convertSubscription(org.hl7.fhir.dstu3.model.Subscription src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.Subscription tgt = new org.hl7.fhir.instance.model.Subscription();
    copyDomainResource(src, tgt);
    tgt.setCriteria(src.getCriteria());
    for (org.hl7.fhir.dstu3.model.ContactPoint t : src.getContact())
      tgt.addContact(convertContactPoint(t));
    tgt.setReason(src.getReason());
    tgt.setStatus(convertSubscriptionStatus(src.getStatus()));
    tgt.setError(src.getError());
    tgt.setChannel(convertSubscriptionChannelComponent(src.getChannel()));
    tgt.setEnd(src.getEnd());
    for (org.hl7.fhir.dstu3.model.Coding t : src.getTag())
      tgt.addTag(convertCoding(t));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.Subscription.SubscriptionStatus convertSubscriptionStatus(org.hl7.fhir.instance.model.Subscription.SubscriptionStatus src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case REQUESTED: return org.hl7.fhir.dstu3.model.Subscription.SubscriptionStatus.REQUESTED;
    case ACTIVE: return org.hl7.fhir.dstu3.model.Subscription.SubscriptionStatus.ACTIVE;
    case ERROR: return org.hl7.fhir.dstu3.model.Subscription.SubscriptionStatus.ERROR;
    case OFF: return org.hl7.fhir.dstu3.model.Subscription.SubscriptionStatus.OFF;
    default: return org.hl7.fhir.dstu3.model.Subscription.SubscriptionStatus.NULL;
    }
  }

  public org.hl7.fhir.instance.model.Subscription.SubscriptionStatus convertSubscriptionStatus(org.hl7.fhir.dstu3.model.Subscription.SubscriptionStatus src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case REQUESTED: return org.hl7.fhir.instance.model.Subscription.SubscriptionStatus.REQUESTED;
    case ACTIVE: return org.hl7.fhir.instance.model.Subscription.SubscriptionStatus.ACTIVE;
    case ERROR: return org.hl7.fhir.instance.model.Subscription.SubscriptionStatus.ERROR;
    case OFF: return org.hl7.fhir.instance.model.Subscription.SubscriptionStatus.OFF;
    default: return org.hl7.fhir.instance.model.Subscription.SubscriptionStatus.NULL;
    }
  }

  public org.hl7.fhir.dstu3.model.Subscription.SubscriptionChannelComponent convertSubscriptionChannelComponent(org.hl7.fhir.instance.model.Subscription.SubscriptionChannelComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.Subscription.SubscriptionChannelComponent tgt = new org.hl7.fhir.dstu3.model.Subscription.SubscriptionChannelComponent();
    copyElement(src, tgt);
    tgt.setType(convertSubscriptionChannelType(src.getType()));
    tgt.setEndpoint(src.getEndpoint());
    tgt.setPayload(src.getPayload());
    tgt.addHeader(src.getHeader());
    return tgt;
  }

  public org.hl7.fhir.instance.model.Subscription.SubscriptionChannelComponent convertSubscriptionChannelComponent(org.hl7.fhir.dstu3.model.Subscription.SubscriptionChannelComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.Subscription.SubscriptionChannelComponent tgt = new org.hl7.fhir.instance.model.Subscription.SubscriptionChannelComponent();
    copyElement(src, tgt);
    tgt.setType(convertSubscriptionChannelType(src.getType()));
    tgt.setEndpoint(src.getEndpoint());
    tgt.setPayload(src.getPayload());
    if (src.hasHeader())
      tgt.setHeaderElement(convertString(src.getHeader().get(0)));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.Subscription.SubscriptionChannelType convertSubscriptionChannelType(org.hl7.fhir.instance.model.Subscription.SubscriptionChannelType src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case RESTHOOK: return org.hl7.fhir.dstu3.model.Subscription.SubscriptionChannelType.RESTHOOK;
    case WEBSOCKET: return org.hl7.fhir.dstu3.model.Subscription.SubscriptionChannelType.WEBSOCKET;
    case EMAIL: return org.hl7.fhir.dstu3.model.Subscription.SubscriptionChannelType.EMAIL;
    case SMS: return org.hl7.fhir.dstu3.model.Subscription.SubscriptionChannelType.SMS;
    case MESSAGE: return org.hl7.fhir.dstu3.model.Subscription.SubscriptionChannelType.MESSAGE;
    default: return org.hl7.fhir.dstu3.model.Subscription.SubscriptionChannelType.NULL;
    }
  }

  public org.hl7.fhir.instance.model.Subscription.SubscriptionChannelType convertSubscriptionChannelType(org.hl7.fhir.dstu3.model.Subscription.SubscriptionChannelType src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case RESTHOOK: return org.hl7.fhir.instance.model.Subscription.SubscriptionChannelType.RESTHOOK;
    case WEBSOCKET: return org.hl7.fhir.instance.model.Subscription.SubscriptionChannelType.WEBSOCKET;
    case EMAIL: return org.hl7.fhir.instance.model.Subscription.SubscriptionChannelType.EMAIL;
    case SMS: return org.hl7.fhir.instance.model.Subscription.SubscriptionChannelType.SMS;
    case MESSAGE: return org.hl7.fhir.instance.model.Subscription.SubscriptionChannelType.MESSAGE;
    default: return org.hl7.fhir.instance.model.Subscription.SubscriptionChannelType.NULL;
    }
  }

  public org.hl7.fhir.dstu3.model.Substance convertSubstance(org.hl7.fhir.instance.model.Substance src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.Substance tgt = new org.hl7.fhir.dstu3.model.Substance();
    copyDomainResource(src, tgt);
    for (org.hl7.fhir.instance.model.Identifier t : src.getIdentifier())
      tgt.addIdentifier(convertIdentifier(t));
    for (org.hl7.fhir.instance.model.CodeableConcept t : src.getCategory())
      tgt.addCategory(convertCodeableConcept(t));
    tgt.setCode(convertCodeableConcept(src.getCode()));
    tgt.setDescription(src.getDescription());
    for (org.hl7.fhir.instance.model.Substance.SubstanceInstanceComponent t : src.getInstance())
      tgt.addInstance(convertSubstanceInstanceComponent(t));
    for (org.hl7.fhir.instance.model.Substance.SubstanceIngredientComponent t : src.getIngredient())
      tgt.addIngredient(convertSubstanceIngredientComponent(t));
    return tgt;
  }

  public org.hl7.fhir.instance.model.Substance convertSubstance(org.hl7.fhir.dstu3.model.Substance src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.Substance tgt = new org.hl7.fhir.instance.model.Substance();
    copyDomainResource(src, tgt);
    for (org.hl7.fhir.dstu3.model.Identifier t : src.getIdentifier())
      tgt.addIdentifier(convertIdentifier(t));
    for (org.hl7.fhir.dstu3.model.CodeableConcept t : src.getCategory())
      tgt.addCategory(convertCodeableConcept(t));
    tgt.setCode(convertCodeableConcept(src.getCode()));
    tgt.setDescription(src.getDescription());
    for (org.hl7.fhir.dstu3.model.Substance.SubstanceInstanceComponent t : src.getInstance())
      tgt.addInstance(convertSubstanceInstanceComponent(t));
    for (org.hl7.fhir.dstu3.model.Substance.SubstanceIngredientComponent t : src.getIngredient())
      tgt.addIngredient(convertSubstanceIngredientComponent(t));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.Substance.SubstanceInstanceComponent convertSubstanceInstanceComponent(org.hl7.fhir.instance.model.Substance.SubstanceInstanceComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.Substance.SubstanceInstanceComponent tgt = new org.hl7.fhir.dstu3.model.Substance.SubstanceInstanceComponent();
    copyElement(src, tgt);
    tgt.setIdentifier(convertIdentifier(src.getIdentifier()));
    tgt.setExpiry(src.getExpiry());
    tgt.setQuantity(convertSimpleQuantity(src.getQuantity()));
    return tgt;
  }

  public org.hl7.fhir.instance.model.Substance.SubstanceInstanceComponent convertSubstanceInstanceComponent(org.hl7.fhir.dstu3.model.Substance.SubstanceInstanceComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.Substance.SubstanceInstanceComponent tgt = new org.hl7.fhir.instance.model.Substance.SubstanceInstanceComponent();
    copyElement(src, tgt);
    tgt.setIdentifier(convertIdentifier(src.getIdentifier()));
    tgt.setExpiry(src.getExpiry());
    tgt.setQuantity(convertSimpleQuantity(src.getQuantity()));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.Substance.SubstanceIngredientComponent convertSubstanceIngredientComponent(org.hl7.fhir.instance.model.Substance.SubstanceIngredientComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.Substance.SubstanceIngredientComponent tgt = new org.hl7.fhir.dstu3.model.Substance.SubstanceIngredientComponent();
    copyElement(src, tgt);
    tgt.setQuantity(convertRatio(src.getQuantity()));
    tgt.setSubstance(convertReference(src.getSubstance()));
    return tgt;
  }

  public org.hl7.fhir.instance.model.Substance.SubstanceIngredientComponent convertSubstanceIngredientComponent(org.hl7.fhir.dstu3.model.Substance.SubstanceIngredientComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.Substance.SubstanceIngredientComponent tgt = new org.hl7.fhir.instance.model.Substance.SubstanceIngredientComponent();
    copyElement(src, tgt);
    tgt.setQuantity(convertRatio(src.getQuantity()));
//    tgt.setSubstance(convertReference(src.getSubstance()));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.SupplyDelivery convertSupplyDelivery(org.hl7.fhir.instance.model.SupplyDelivery src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.SupplyDelivery tgt = new org.hl7.fhir.dstu3.model.SupplyDelivery();
    copyDomainResource(src, tgt);
    tgt.setIdentifier(convertIdentifier(src.getIdentifier()));
    tgt.setStatus(convertSupplyDeliveryStatus(src.getStatus()));
    tgt.setPatient(convertReference(src.getPatient()));
    tgt.setType(convertCodeableConcept(src.getType()));
//    tgt.setQuantity(convertSimpleQuantity(src.getQuantity()));
//    tgt.setSuppliedItem(convertReference(src.getSuppliedItem()));
    tgt.setSupplier(convertReference(src.getSupplier()));
//    tgt.setWhenPrepared(convertPeriod(src.getWhenPrepared()));
//    tgt.setTime(src.getTime());
    tgt.setDestination(convertReference(src.getDestination()));
    for (org.hl7.fhir.instance.model.Reference t : src.getReceiver())
      tgt.addReceiver(convertReference(t));
    return tgt;
  }

  public org.hl7.fhir.instance.model.SupplyDelivery convertSupplyDelivery(org.hl7.fhir.dstu3.model.SupplyDelivery src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.SupplyDelivery tgt = new org.hl7.fhir.instance.model.SupplyDelivery();
    copyDomainResource(src, tgt);
    tgt.setIdentifier(convertIdentifier(src.getIdentifier()));
    tgt.setStatus(convertSupplyDeliveryStatus(src.getStatus()));
    tgt.setPatient(convertReference(src.getPatient()));
    tgt.setType(convertCodeableConcept(src.getType()));
//    tgt.setQuantity(convertSimpleQuantity(src.getQuantity()));
//    tgt.setSuppliedItem(convertReference(src.getSuppliedItem()));
    tgt.setSupplier(convertReference(src.getSupplier()));
//    tgt.setWhenPrepared(convertPeriod(src.getWhenPrepared()));
//    tgt.setTime(src.getTime());
    tgt.setDestination(convertReference(src.getDestination()));
    for (org.hl7.fhir.dstu3.model.Reference t : src.getReceiver())
      tgt.addReceiver(convertReference(t));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.SupplyDelivery.SupplyDeliveryStatus convertSupplyDeliveryStatus(org.hl7.fhir.instance.model.SupplyDelivery.SupplyDeliveryStatus src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case INPROGRESS: return org.hl7.fhir.dstu3.model.SupplyDelivery.SupplyDeliveryStatus.INPROGRESS;
    case COMPLETED: return org.hl7.fhir.dstu3.model.SupplyDelivery.SupplyDeliveryStatus.COMPLETED;
    case ABANDONED: return org.hl7.fhir.dstu3.model.SupplyDelivery.SupplyDeliveryStatus.ABANDONED;
    default: return org.hl7.fhir.dstu3.model.SupplyDelivery.SupplyDeliveryStatus.NULL;
    }
  }

  public org.hl7.fhir.instance.model.SupplyDelivery.SupplyDeliveryStatus convertSupplyDeliveryStatus(org.hl7.fhir.dstu3.model.SupplyDelivery.SupplyDeliveryStatus src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case INPROGRESS: return org.hl7.fhir.instance.model.SupplyDelivery.SupplyDeliveryStatus.INPROGRESS;
    case COMPLETED: return org.hl7.fhir.instance.model.SupplyDelivery.SupplyDeliveryStatus.COMPLETED;
    case ABANDONED: return org.hl7.fhir.instance.model.SupplyDelivery.SupplyDeliveryStatus.ABANDONED;
    default: return org.hl7.fhir.instance.model.SupplyDelivery.SupplyDeliveryStatus.NULL;
    }
  }

  public org.hl7.fhir.dstu3.model.SupplyRequest convertSupplyRequest(org.hl7.fhir.instance.model.SupplyRequest src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.SupplyRequest tgt = new org.hl7.fhir.dstu3.model.SupplyRequest();
    copyDomainResource(src, tgt);
//    tgt.setPatient(convertReference(src.getPatient()));
//    tgt.setSource(convertReference(src.getSource()));
//    if (src.hasDate())
//      tgt.setDate(src.getDate());
//    tgt.setIdentifier(convertIdentifier(src.getIdentifier()));
//    tgt.setStatus(convertSupplyRequestStatus(src.getStatus()));
//    tgt.setKind(convertCodeableConcept(src.getKind()));
//    tgt.getOrderedItem().setItem(convertReference(src.getOrderedItem()));
//    for (org.hl7.fhir.instance.model.Reference t : src.getSupplier())
//      tgt.addSupplier(convertReference(t));
//    tgt.setReason(convertType(src.getReason()));
//    tgt.setWhen(convertSupplyRequestWhenComponent(src.getWhen()));
    return tgt;
  }

  public org.hl7.fhir.instance.model.SupplyRequest convertSupplyRequest(org.hl7.fhir.dstu3.model.SupplyRequest src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.SupplyRequest tgt = new org.hl7.fhir.instance.model.SupplyRequest();
//    copyDomainResource(src, tgt);
//    tgt.setPatient(convertReference(src.getPatient()));
//    tgt.setSource(convertReference(src.getSource()));
//    if (src.hasDate())
//      tgt.setDate(src.getDate());
//    tgt.setIdentifier(convertIdentifier(src.getIdentifier()));
//    tgt.setStatus(convertSupplyRequestStatus(src.getStatus()));
//    tgt.setKind(convertCodeableConcept(src.getKind()));
//    tgt.setOrderedItem(convertReference(src.getOrderedItem().getItemReference()));
//    for (org.hl7.fhir.dstu3.model.Reference t : src.getSupplier())
//      tgt.addSupplier(convertReference(t));
//    tgt.setReason(convertType(src.getReason()));
//    tgt.setWhen(convertSupplyRequestWhenComponent(src.getWhen()));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.SupplyRequest.SupplyRequestStatus convertSupplyRequestStatus(org.hl7.fhir.instance.model.SupplyRequest.SupplyRequestStatus src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case REQUESTED: return org.hl7.fhir.dstu3.model.SupplyRequest.SupplyRequestStatus.ACTIVE;
    case COMPLETED: return org.hl7.fhir.dstu3.model.SupplyRequest.SupplyRequestStatus.COMPLETED;
    case FAILED: return org.hl7.fhir.dstu3.model.SupplyRequest.SupplyRequestStatus.CANCELLED;
    case CANCELLED: return org.hl7.fhir.dstu3.model.SupplyRequest.SupplyRequestStatus.CANCELLED;
    default: return org.hl7.fhir.dstu3.model.SupplyRequest.SupplyRequestStatus.NULL;
    }
  }

  public org.hl7.fhir.instance.model.SupplyRequest.SupplyRequestStatus convertSupplyRequestStatus(org.hl7.fhir.dstu3.model.SupplyRequest.SupplyRequestStatus src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case ACTIVE: return org.hl7.fhir.instance.model.SupplyRequest.SupplyRequestStatus.REQUESTED;
    case COMPLETED: return org.hl7.fhir.instance.model.SupplyRequest.SupplyRequestStatus.COMPLETED;
    case CANCELLED: return org.hl7.fhir.instance.model.SupplyRequest.SupplyRequestStatus.CANCELLED;
    default: return org.hl7.fhir.instance.model.SupplyRequest.SupplyRequestStatus.NULL;
    }
  }

  public org.hl7.fhir.dstu3.model.TestScript convertTestScript(org.hl7.fhir.instance.model.TestScript src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.TestScript tgt = new org.hl7.fhir.dstu3.model.TestScript();
    copyDomainResource(src, tgt);
    tgt.setUrl(src.getUrl());
    tgt.setVersion(src.getVersion());
    tgt.setName(src.getName());
    tgt.setStatus(convertConformanceResourceStatus(src.getStatus()));
    tgt.setIdentifier(convertIdentifier(src.getIdentifier()));
    if (src.hasExperimental())
      tgt.setExperimental(src.getExperimental());
    tgt.setPublisher(src.getPublisher());
    for (org.hl7.fhir.instance.model.TestScript.TestScriptContactComponent t : src.getContact())
      tgt.addContact(convertTestScriptContactComponent(t));
    if (src.hasDate())
      tgt.setDate(src.getDate());
    tgt.setDescription(src.getDescription());
    for (org.hl7.fhir.instance.model.CodeableConcept t : src.getUseContext())
      if (isJurisdiction(t))
        tgt.addJurisdiction(convertCodeableConcept(t));
      else
        tgt.addUseContext(convertCodeableConceptToUsageContext(t));
    tgt.setPurpose(src.getRequirements());
    tgt.setCopyright(src.getCopyright());
    tgt.setMetadata(convertTestScriptMetadataComponent(src.getMetadata()));
    for (org.hl7.fhir.instance.model.TestScript.TestScriptFixtureComponent t : src.getFixture())
      tgt.addFixture(convertTestScriptFixtureComponent(t));
    for (org.hl7.fhir.instance.model.Reference t : src.getProfile())
      tgt.addProfile(convertReference(t));
    for (org.hl7.fhir.instance.model.TestScript.TestScriptVariableComponent t : src.getVariable())
      tgt.addVariable(convertTestScriptVariableComponent(t));
    tgt.setSetup(convertTestScriptSetupComponent(src.getSetup()));
    for (org.hl7.fhir.instance.model.TestScript.TestScriptTestComponent t : src.getTest())
      tgt.addTest(convertTestScriptTestComponent(t));
    tgt.setTeardown(convertTestScriptTeardownComponent(src.getTeardown()));
    return tgt;
  }

  public org.hl7.fhir.instance.model.TestScript convertTestScript(org.hl7.fhir.dstu3.model.TestScript src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.TestScript tgt = new org.hl7.fhir.instance.model.TestScript();
    copyDomainResource(src, tgt);
    tgt.setUrl(src.getUrl());
    tgt.setVersion(src.getVersion());
    tgt.setName(src.getName());
    tgt.setStatus(convertConformanceResourceStatus(src.getStatus()));
    tgt.setIdentifier(convertIdentifier(src.getIdentifier()));
    if (src.hasExperimental())
      tgt.setExperimental(src.getExperimental());
    tgt.setPublisher(src.getPublisher());
    for (org.hl7.fhir.dstu3.model.ContactDetail t : src.getContact())
      tgt.addContact(convertTestScriptContactComponent(t));
    if (src.hasDate())
      tgt.setDate(src.getDate());
    tgt.setDescription(src.getDescription());
    for (org.hl7.fhir.dstu3.model.UsageContext t : src.getUseContext())
      if (t.hasValueCodeableConcept())
        tgt.addUseContext(convertCodeableConcept(t.getValueCodeableConcept()));
    for (org.hl7.fhir.dstu3.model.CodeableConcept t : src.getJurisdiction())
        tgt.addUseContext(convertCodeableConcept(t));
    tgt.setRequirements(src.getPurpose());
    tgt.setCopyright(src.getCopyright());
    tgt.setMetadata(convertTestScriptMetadataComponent(src.getMetadata()));
    for (org.hl7.fhir.dstu3.model.TestScript.TestScriptFixtureComponent t : src.getFixture())
      tgt.addFixture(convertTestScriptFixtureComponent(t));
    for (org.hl7.fhir.dstu3.model.Reference t : src.getProfile())
      tgt.addProfile(convertReference(t));
    for (org.hl7.fhir.dstu3.model.TestScript.TestScriptVariableComponent t : src.getVariable())
      tgt.addVariable(convertTestScriptVariableComponent(t));
    tgt.setSetup(convertTestScriptSetupComponent(src.getSetup()));
    for (org.hl7.fhir.dstu3.model.TestScript.TestScriptTestComponent t : src.getTest())
      tgt.addTest(convertTestScriptTestComponent(t));
    tgt.setTeardown(convertTestScriptTeardownComponent(src.getTeardown()));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.ContactDetail convertTestScriptContactComponent(org.hl7.fhir.instance.model.TestScript.TestScriptContactComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.ContactDetail tgt = new org.hl7.fhir.dstu3.model.ContactDetail();
    copyElement(src, tgt);
    tgt.setName(src.getName());
    for (org.hl7.fhir.instance.model.ContactPoint t : src.getTelecom())
      tgt.addTelecom(convertContactPoint(t));
    return tgt;
  }

  public org.hl7.fhir.instance.model.TestScript.TestScriptContactComponent convertTestScriptContactComponent(org.hl7.fhir.dstu3.model.ContactDetail src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.TestScript.TestScriptContactComponent tgt = new org.hl7.fhir.instance.model.TestScript.TestScriptContactComponent();
    copyElement(src, tgt);
    tgt.setName(src.getName());
    for (org.hl7.fhir.dstu3.model.ContactPoint t : src.getTelecom())
      tgt.addTelecom(convertContactPoint(t));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.TestScript.TestScriptMetadataComponent convertTestScriptMetadataComponent(org.hl7.fhir.instance.model.TestScript.TestScriptMetadataComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.TestScript.TestScriptMetadataComponent tgt = new org.hl7.fhir.dstu3.model.TestScript.TestScriptMetadataComponent();
    copyElement(src, tgt);
    for (org.hl7.fhir.instance.model.TestScript.TestScriptMetadataLinkComponent t : src.getLink())
      tgt.addLink(convertTestScriptMetadataLinkComponent(t));
    for (org.hl7.fhir.instance.model.TestScript.TestScriptMetadataCapabilityComponent t : src.getCapability())
      tgt.addCapability(convertTestScriptMetadataCapabilityComponent(t));
    return tgt;
  }

  public org.hl7.fhir.instance.model.TestScript.TestScriptMetadataComponent convertTestScriptMetadataComponent(org.hl7.fhir.dstu3.model.TestScript.TestScriptMetadataComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.TestScript.TestScriptMetadataComponent tgt = new org.hl7.fhir.instance.model.TestScript.TestScriptMetadataComponent();
    copyElement(src, tgt);
    for (org.hl7.fhir.dstu3.model.TestScript.TestScriptMetadataLinkComponent t : src.getLink())
      tgt.addLink(convertTestScriptMetadataLinkComponent(t));
    for (org.hl7.fhir.dstu3.model.TestScript.TestScriptMetadataCapabilityComponent t : src.getCapability())
      tgt.addCapability(convertTestScriptMetadataCapabilityComponent(t));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.TestScript.TestScriptMetadataLinkComponent convertTestScriptMetadataLinkComponent(org.hl7.fhir.instance.model.TestScript.TestScriptMetadataLinkComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.TestScript.TestScriptMetadataLinkComponent tgt = new org.hl7.fhir.dstu3.model.TestScript.TestScriptMetadataLinkComponent();
    copyElement(src, tgt);
    tgt.setUrl(src.getUrl());
    tgt.setDescription(src.getDescription());
    return tgt;
  }

  public org.hl7.fhir.instance.model.TestScript.TestScriptMetadataLinkComponent convertTestScriptMetadataLinkComponent(org.hl7.fhir.dstu3.model.TestScript.TestScriptMetadataLinkComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.TestScript.TestScriptMetadataLinkComponent tgt = new org.hl7.fhir.instance.model.TestScript.TestScriptMetadataLinkComponent();
    copyElement(src, tgt);
    tgt.setUrl(src.getUrl());
    tgt.setDescription(src.getDescription());
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.TestScript.TestScriptMetadataCapabilityComponent convertTestScriptMetadataCapabilityComponent(org.hl7.fhir.instance.model.TestScript.TestScriptMetadataCapabilityComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.TestScript.TestScriptMetadataCapabilityComponent tgt = new org.hl7.fhir.dstu3.model.TestScript.TestScriptMetadataCapabilityComponent();
    copyElement(src, tgt);
    tgt.setRequired(src.getRequired());
    tgt.setValidated(src.getValidated());
    tgt.setDescription(src.getDescription());
    tgt.setDestination(src.getDestination());
    for (org.hl7.fhir.instance.model.UriType t : src.getLink())
      tgt.addLink(t.getValue());
    tgt.setCapabilities(convertReference(src.getConformance()));
    return tgt;
  }

  public org.hl7.fhir.instance.model.TestScript.TestScriptMetadataCapabilityComponent convertTestScriptMetadataCapabilityComponent(org.hl7.fhir.dstu3.model.TestScript.TestScriptMetadataCapabilityComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.TestScript.TestScriptMetadataCapabilityComponent tgt = new org.hl7.fhir.instance.model.TestScript.TestScriptMetadataCapabilityComponent();
    copyElement(src, tgt);
    tgt.setRequired(src.getRequired());
    tgt.setValidated(src.getValidated());
    tgt.setDescription(src.getDescription());
    tgt.setDestination(src.getDestination());
    for (org.hl7.fhir.dstu3.model.UriType t : src.getLink())
      tgt.addLink(t.getValue());
    tgt.setConformance(convertReference(src.getCapabilities()));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.TestScript.TestScriptFixtureComponent convertTestScriptFixtureComponent(org.hl7.fhir.instance.model.TestScript.TestScriptFixtureComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.TestScript.TestScriptFixtureComponent tgt = new org.hl7.fhir.dstu3.model.TestScript.TestScriptFixtureComponent();
    copyElement(src, tgt);
    tgt.setAutocreate(src.getAutocreate());
    tgt.setAutodelete(src.getAutodelete());
    tgt.setResource(convertReference(src.getResource()));
    return tgt;
  }

  public org.hl7.fhir.instance.model.TestScript.TestScriptFixtureComponent convertTestScriptFixtureComponent(org.hl7.fhir.dstu3.model.TestScript.TestScriptFixtureComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.TestScript.TestScriptFixtureComponent tgt = new org.hl7.fhir.instance.model.TestScript.TestScriptFixtureComponent();
    copyElement(src, tgt);
    tgt.setAutocreate(src.getAutocreate());
    tgt.setAutodelete(src.getAutodelete());
    tgt.setResource(convertReference(src.getResource()));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.TestScript.TestScriptVariableComponent convertTestScriptVariableComponent(org.hl7.fhir.instance.model.TestScript.TestScriptVariableComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.TestScript.TestScriptVariableComponent tgt = new org.hl7.fhir.dstu3.model.TestScript.TestScriptVariableComponent();
    copyElement(src, tgt);
    tgt.setName(src.getName());
    tgt.setHeaderField(src.getHeaderField());
    tgt.setPath(src.getPath());
    tgt.setSourceId(src.getSourceId());
    return tgt;
  }

  public org.hl7.fhir.instance.model.TestScript.TestScriptVariableComponent convertTestScriptVariableComponent(org.hl7.fhir.dstu3.model.TestScript.TestScriptVariableComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.TestScript.TestScriptVariableComponent tgt = new org.hl7.fhir.instance.model.TestScript.TestScriptVariableComponent();
    copyElement(src, tgt);
    tgt.setName(src.getName());
    tgt.setHeaderField(src.getHeaderField());
    tgt.setPath(src.getPath());
    tgt.setSourceId(src.getSourceId());
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.TestScript.TestScriptSetupComponent convertTestScriptSetupComponent(org.hl7.fhir.instance.model.TestScript.TestScriptSetupComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.TestScript.TestScriptSetupComponent tgt = new org.hl7.fhir.dstu3.model.TestScript.TestScriptSetupComponent();
    copyElement(src, tgt);
    for (org.hl7.fhir.instance.model.TestScript.TestScriptSetupActionComponent t : src.getAction())
      tgt.addAction(convertSetupActionComponent(t));
    return tgt;
  }

  public org.hl7.fhir.instance.model.TestScript.TestScriptSetupComponent convertTestScriptSetupComponent(org.hl7.fhir.dstu3.model.TestScript.TestScriptSetupComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.TestScript.TestScriptSetupComponent tgt = new org.hl7.fhir.instance.model.TestScript.TestScriptSetupComponent();
    copyElement(src, tgt);
    for (org.hl7.fhir.dstu3.model.TestScript.SetupActionComponent t : src.getAction())
      tgt.addAction(convertSetupActionComponent(t));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.TestScript.SetupActionComponent convertSetupActionComponent(org.hl7.fhir.instance.model.TestScript.TestScriptSetupActionComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.TestScript.SetupActionComponent tgt = new org.hl7.fhir.dstu3.model.TestScript.SetupActionComponent();
    copyElement(src, tgt);
    tgt.setOperation(convertSetupActionOperationComponent(src.getOperation()));
    tgt.setAssert(convertSetupActionAssertComponent(src.getAssert()));
    return tgt;
  }

  public org.hl7.fhir.instance.model.TestScript.TestScriptSetupActionComponent convertSetupActionComponent(org.hl7.fhir.dstu3.model.TestScript.SetupActionComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.TestScript.TestScriptSetupActionComponent tgt = new org.hl7.fhir.instance.model.TestScript.TestScriptSetupActionComponent();
    copyElement(src, tgt);
    tgt.setOperation(convertSetupActionOperationComponent(src.getOperation()));
    tgt.setAssert(convertSetupActionAssertComponent(src.getAssert()));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.TestScript.SetupActionOperationComponent convertSetupActionOperationComponent(org.hl7.fhir.instance.model.TestScript.TestScriptSetupActionOperationComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.TestScript.SetupActionOperationComponent tgt = new org.hl7.fhir.dstu3.model.TestScript.SetupActionOperationComponent();
    copyElement(src, tgt);
    tgt.setType(convertCoding(src.getType()));
    tgt.setResource(src.getResource());
    tgt.setLabel(src.getLabel());
    tgt.setDescription(src.getDescription());
    tgt.setAccept(convertContentType(src.getAccept()));
    tgt.setContentType(convertContentType(src.getContentType()));
    tgt.setDestination(src.getDestination());
    tgt.setEncodeRequestUrl(src.getEncodeRequestUrl());
    tgt.setParams(src.getParams());
    for (org.hl7.fhir.instance.model.TestScript.TestScriptSetupActionOperationRequestHeaderComponent t : src.getRequestHeader())
      tgt.addRequestHeader(convertSetupActionOperationRequestHeaderComponent(t));
    tgt.setResponseId(src.getResponseId());
    tgt.setSourceId(src.getSourceId());
    tgt.setTargetId(src.getTargetId());
    tgt.setUrl(src.getUrl());
    return tgt;
  }

  public org.hl7.fhir.instance.model.TestScript.TestScriptSetupActionOperationComponent convertSetupActionOperationComponent(org.hl7.fhir.dstu3.model.TestScript.SetupActionOperationComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.TestScript.TestScriptSetupActionOperationComponent tgt = new org.hl7.fhir.instance.model.TestScript.TestScriptSetupActionOperationComponent();
    copyElement(src, tgt);
    tgt.setType(convertCoding(src.getType()));
    tgt.setResource(src.getResource());
    tgt.setLabel(src.getLabel());
    tgt.setDescription(src.getDescription());
    tgt.setAccept(convertContentType(src.getAccept()));
    tgt.setContentType(convertContentType(src.getContentType()));
    tgt.setDestination(src.getDestination());
    tgt.setEncodeRequestUrl(src.getEncodeRequestUrl());
    tgt.setParams(src.getParams());
    for (org.hl7.fhir.dstu3.model.TestScript.SetupActionOperationRequestHeaderComponent t : src.getRequestHeader())
      tgt.addRequestHeader(convertSetupActionOperationRequestHeaderComponent(t));
    tgt.setResponseId(src.getResponseId());
    tgt.setSourceId(src.getSourceId());
    tgt.setTargetId(src.getTargetId());
    tgt.setUrl(src.getUrl());
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.TestScript.ContentType convertContentType(org.hl7.fhir.instance.model.TestScript.ContentType src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case XML: return org.hl7.fhir.dstu3.model.TestScript.ContentType.XML;
    case JSON: return org.hl7.fhir.dstu3.model.TestScript.ContentType.JSON;
    default: return org.hl7.fhir.dstu3.model.TestScript.ContentType.NULL;
    }
  }

  public org.hl7.fhir.instance.model.TestScript.ContentType convertContentType(org.hl7.fhir.dstu3.model.TestScript.ContentType src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case XML: return org.hl7.fhir.instance.model.TestScript.ContentType.XML;
    case JSON: return org.hl7.fhir.instance.model.TestScript.ContentType.JSON;
    default: return org.hl7.fhir.instance.model.TestScript.ContentType.NULL;
    }
  }

  public org.hl7.fhir.dstu3.model.TestScript.SetupActionOperationRequestHeaderComponent convertSetupActionOperationRequestHeaderComponent(org.hl7.fhir.instance.model.TestScript.TestScriptSetupActionOperationRequestHeaderComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.TestScript.SetupActionOperationRequestHeaderComponent tgt = new org.hl7.fhir.dstu3.model.TestScript.SetupActionOperationRequestHeaderComponent();
    copyElement(src, tgt);
    tgt.setField(src.getField());
    tgt.setValue(src.getValue());
    return tgt;
  }

  public org.hl7.fhir.instance.model.TestScript.TestScriptSetupActionOperationRequestHeaderComponent convertSetupActionOperationRequestHeaderComponent(org.hl7.fhir.dstu3.model.TestScript.SetupActionOperationRequestHeaderComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.TestScript.TestScriptSetupActionOperationRequestHeaderComponent tgt = new org.hl7.fhir.instance.model.TestScript.TestScriptSetupActionOperationRequestHeaderComponent();
    copyElement(src, tgt);
    tgt.setField(src.getField());
    tgt.setValue(src.getValue());
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.TestScript.SetupActionAssertComponent convertSetupActionAssertComponent(org.hl7.fhir.instance.model.TestScript.TestScriptSetupActionAssertComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.TestScript.SetupActionAssertComponent tgt = new org.hl7.fhir.dstu3.model.TestScript.SetupActionAssertComponent();
    copyElement(src, tgt);
    tgt.setLabel(src.getLabel());
    tgt.setDescription(src.getDescription());
    tgt.setDirection(convertAssertionDirectionType(src.getDirection()));
    tgt.setCompareToSourceId(src.getCompareToSourceId());
    tgt.setCompareToSourcePath(src.getCompareToSourcePath());
    tgt.setContentType(convertContentType(src.getContentType()));
    tgt.setHeaderField(src.getHeaderField());
    tgt.setMinimumId(src.getMinimumId());
    tgt.setNavigationLinks(src.getNavigationLinks());
    tgt.setOperator(convertAssertionOperatorType(src.getOperator()));
    tgt.setPath(src.getPath());
    tgt.setResource(src.getResource());
    tgt.setResponse(convertAssertionResponseTypes(src.getResponse()));
    tgt.setResponseCode(src.getResponseCode());
    tgt.setSourceId(src.getSourceId());
    tgt.setValidateProfileId(src.getValidateProfileId());
    tgt.setValue(src.getValue());
    tgt.setWarningOnly(src.getWarningOnly());
    return tgt;
  }

  public org.hl7.fhir.instance.model.TestScript.TestScriptSetupActionAssertComponent convertSetupActionAssertComponent(org.hl7.fhir.dstu3.model.TestScript.SetupActionAssertComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.TestScript.TestScriptSetupActionAssertComponent tgt = new org.hl7.fhir.instance.model.TestScript.TestScriptSetupActionAssertComponent();
    copyElement(src, tgt);
    tgt.setLabel(src.getLabel());
    tgt.setDescription(src.getDescription());
    tgt.setDirection(convertAssertionDirectionType(src.getDirection()));
    tgt.setCompareToSourceId(src.getCompareToSourceId());
    tgt.setCompareToSourcePath(src.getCompareToSourcePath());
    tgt.setContentType(convertContentType(src.getContentType()));
    tgt.setHeaderField(src.getHeaderField());
    tgt.setMinimumId(src.getMinimumId());
    tgt.setNavigationLinks(src.getNavigationLinks());
    tgt.setOperator(convertAssertionOperatorType(src.getOperator()));
    tgt.setPath(src.getPath());
    tgt.setResource(src.getResource());
    tgt.setResponse(convertAssertionResponseTypes(src.getResponse()));
    tgt.setResponseCode(src.getResponseCode());
    tgt.setSourceId(src.getSourceId());
    tgt.setValidateProfileId(src.getValidateProfileId());
    tgt.setValue(src.getValue());
    tgt.setWarningOnly(src.getWarningOnly());
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.TestScript.AssertionDirectionType convertAssertionDirectionType(org.hl7.fhir.instance.model.TestScript.AssertionDirectionType src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case RESPONSE: return org.hl7.fhir.dstu3.model.TestScript.AssertionDirectionType.RESPONSE;
    case REQUEST: return org.hl7.fhir.dstu3.model.TestScript.AssertionDirectionType.REQUEST;
    default: return org.hl7.fhir.dstu3.model.TestScript.AssertionDirectionType.NULL;
    }
  }

  public org.hl7.fhir.instance.model.TestScript.AssertionDirectionType convertAssertionDirectionType(org.hl7.fhir.dstu3.model.TestScript.AssertionDirectionType src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case RESPONSE: return org.hl7.fhir.instance.model.TestScript.AssertionDirectionType.RESPONSE;
    case REQUEST: return org.hl7.fhir.instance.model.TestScript.AssertionDirectionType.REQUEST;
    default: return org.hl7.fhir.instance.model.TestScript.AssertionDirectionType.NULL;
    }
  }

  public org.hl7.fhir.dstu3.model.TestScript.AssertionOperatorType convertAssertionOperatorType(org.hl7.fhir.instance.model.TestScript.AssertionOperatorType src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case EQUALS: return org.hl7.fhir.dstu3.model.TestScript.AssertionOperatorType.EQUALS;
    case NOTEQUALS: return org.hl7.fhir.dstu3.model.TestScript.AssertionOperatorType.NOTEQUALS;
    case IN: return org.hl7.fhir.dstu3.model.TestScript.AssertionOperatorType.IN;
    case NOTIN: return org.hl7.fhir.dstu3.model.TestScript.AssertionOperatorType.NOTIN;
    case GREATERTHAN: return org.hl7.fhir.dstu3.model.TestScript.AssertionOperatorType.GREATERTHAN;
    case LESSTHAN: return org.hl7.fhir.dstu3.model.TestScript.AssertionOperatorType.LESSTHAN;
    case EMPTY: return org.hl7.fhir.dstu3.model.TestScript.AssertionOperatorType.EMPTY;
    case NOTEMPTY: return org.hl7.fhir.dstu3.model.TestScript.AssertionOperatorType.NOTEMPTY;
    case CONTAINS: return org.hl7.fhir.dstu3.model.TestScript.AssertionOperatorType.CONTAINS;
    case NOTCONTAINS: return org.hl7.fhir.dstu3.model.TestScript.AssertionOperatorType.NOTCONTAINS;
    default: return org.hl7.fhir.dstu3.model.TestScript.AssertionOperatorType.NULL;
    }
  }

  public org.hl7.fhir.instance.model.TestScript.AssertionOperatorType convertAssertionOperatorType(org.hl7.fhir.dstu3.model.TestScript.AssertionOperatorType src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case EQUALS: return org.hl7.fhir.instance.model.TestScript.AssertionOperatorType.EQUALS;
    case NOTEQUALS: return org.hl7.fhir.instance.model.TestScript.AssertionOperatorType.NOTEQUALS;
    case IN: return org.hl7.fhir.instance.model.TestScript.AssertionOperatorType.IN;
    case NOTIN: return org.hl7.fhir.instance.model.TestScript.AssertionOperatorType.NOTIN;
    case GREATERTHAN: return org.hl7.fhir.instance.model.TestScript.AssertionOperatorType.GREATERTHAN;
    case LESSTHAN: return org.hl7.fhir.instance.model.TestScript.AssertionOperatorType.LESSTHAN;
    case EMPTY: return org.hl7.fhir.instance.model.TestScript.AssertionOperatorType.EMPTY;
    case NOTEMPTY: return org.hl7.fhir.instance.model.TestScript.AssertionOperatorType.NOTEMPTY;
    case CONTAINS: return org.hl7.fhir.instance.model.TestScript.AssertionOperatorType.CONTAINS;
    case NOTCONTAINS: return org.hl7.fhir.instance.model.TestScript.AssertionOperatorType.NOTCONTAINS;
    default: return org.hl7.fhir.instance.model.TestScript.AssertionOperatorType.NULL;
    }
  }

  public org.hl7.fhir.dstu3.model.TestScript.AssertionResponseTypes convertAssertionResponseTypes(org.hl7.fhir.instance.model.TestScript.AssertionResponseTypes src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case OKAY: return org.hl7.fhir.dstu3.model.TestScript.AssertionResponseTypes.OKAY;
    case CREATED: return org.hl7.fhir.dstu3.model.TestScript.AssertionResponseTypes.CREATED;
    case NOCONTENT: return org.hl7.fhir.dstu3.model.TestScript.AssertionResponseTypes.NOCONTENT;
    case NOTMODIFIED: return org.hl7.fhir.dstu3.model.TestScript.AssertionResponseTypes.NOTMODIFIED;
    case BAD: return org.hl7.fhir.dstu3.model.TestScript.AssertionResponseTypes.BAD;
    case FORBIDDEN: return org.hl7.fhir.dstu3.model.TestScript.AssertionResponseTypes.FORBIDDEN;
    case NOTFOUND: return org.hl7.fhir.dstu3.model.TestScript.AssertionResponseTypes.NOTFOUND;
    case METHODNOTALLOWED: return org.hl7.fhir.dstu3.model.TestScript.AssertionResponseTypes.METHODNOTALLOWED;
    case CONFLICT: return org.hl7.fhir.dstu3.model.TestScript.AssertionResponseTypes.CONFLICT;
    case GONE: return org.hl7.fhir.dstu3.model.TestScript.AssertionResponseTypes.GONE;
    case PRECONDITIONFAILED: return org.hl7.fhir.dstu3.model.TestScript.AssertionResponseTypes.PRECONDITIONFAILED;
    case UNPROCESSABLE: return org.hl7.fhir.dstu3.model.TestScript.AssertionResponseTypes.UNPROCESSABLE;
    default: return org.hl7.fhir.dstu3.model.TestScript.AssertionResponseTypes.NULL;
    }
  }

  public org.hl7.fhir.instance.model.TestScript.AssertionResponseTypes convertAssertionResponseTypes(org.hl7.fhir.dstu3.model.TestScript.AssertionResponseTypes src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case OKAY: return org.hl7.fhir.instance.model.TestScript.AssertionResponseTypes.OKAY;
    case CREATED: return org.hl7.fhir.instance.model.TestScript.AssertionResponseTypes.CREATED;
    case NOCONTENT: return org.hl7.fhir.instance.model.TestScript.AssertionResponseTypes.NOCONTENT;
    case NOTMODIFIED: return org.hl7.fhir.instance.model.TestScript.AssertionResponseTypes.NOTMODIFIED;
    case BAD: return org.hl7.fhir.instance.model.TestScript.AssertionResponseTypes.BAD;
    case FORBIDDEN: return org.hl7.fhir.instance.model.TestScript.AssertionResponseTypes.FORBIDDEN;
    case NOTFOUND: return org.hl7.fhir.instance.model.TestScript.AssertionResponseTypes.NOTFOUND;
    case METHODNOTALLOWED: return org.hl7.fhir.instance.model.TestScript.AssertionResponseTypes.METHODNOTALLOWED;
    case CONFLICT: return org.hl7.fhir.instance.model.TestScript.AssertionResponseTypes.CONFLICT;
    case GONE: return org.hl7.fhir.instance.model.TestScript.AssertionResponseTypes.GONE;
    case PRECONDITIONFAILED: return org.hl7.fhir.instance.model.TestScript.AssertionResponseTypes.PRECONDITIONFAILED;
    case UNPROCESSABLE: return org.hl7.fhir.instance.model.TestScript.AssertionResponseTypes.UNPROCESSABLE;
    default: return org.hl7.fhir.instance.model.TestScript.AssertionResponseTypes.NULL;
    }
  }


  public org.hl7.fhir.dstu3.model.TestScript.TestScriptTestComponent convertTestScriptTestComponent(org.hl7.fhir.instance.model.TestScript.TestScriptTestComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.TestScript.TestScriptTestComponent tgt = new org.hl7.fhir.dstu3.model.TestScript.TestScriptTestComponent();
    copyElement(src, tgt);
    tgt.setName(src.getName());
    tgt.setDescription(src.getDescription());
    for (org.hl7.fhir.instance.model.TestScript.TestScriptTestActionComponent t : src.getAction())
      tgt.addAction(convertTestActionComponent(t));
    return tgt;
  }

  public org.hl7.fhir.instance.model.TestScript.TestScriptTestComponent convertTestScriptTestComponent(org.hl7.fhir.dstu3.model.TestScript.TestScriptTestComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.TestScript.TestScriptTestComponent tgt = new org.hl7.fhir.instance.model.TestScript.TestScriptTestComponent();
    copyElement(src, tgt);
    tgt.setName(src.getName());
    tgt.setDescription(src.getDescription());
    for (org.hl7.fhir.dstu3.model.TestScript.TestActionComponent t : src.getAction())
      tgt.addAction(convertTestActionComponent(t));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.TestScript.TestActionComponent convertTestActionComponent(org.hl7.fhir.instance.model.TestScript.TestScriptTestActionComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.TestScript.TestActionComponent tgt = new org.hl7.fhir.dstu3.model.TestScript.TestActionComponent();
    copyElement(src, tgt);
    tgt.setOperation(convertSetupActionOperationComponent(src.getOperation()));
    tgt.setAssert(convertSetupActionAssertComponent(src.getAssert()));
    return tgt;
  }

  public org.hl7.fhir.instance.model.TestScript.TestScriptTestActionComponent convertTestActionComponent(org.hl7.fhir.dstu3.model.TestScript.TestActionComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.TestScript.TestScriptTestActionComponent tgt = new org.hl7.fhir.instance.model.TestScript.TestScriptTestActionComponent();
    copyElement(src, tgt);
    tgt.setOperation(convertSetupActionOperationComponent(src.getOperation()));
    tgt.setAssert(convertSetupActionAssertComponent(src.getAssert()));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.TestScript.TestScriptTeardownComponent convertTestScriptTeardownComponent(org.hl7.fhir.instance.model.TestScript.TestScriptTeardownComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.TestScript.TestScriptTeardownComponent tgt = new org.hl7.fhir.dstu3.model.TestScript.TestScriptTeardownComponent();
    copyElement(src, tgt);
    for (org.hl7.fhir.instance.model.TestScript.TestScriptTeardownActionComponent t : src.getAction())
      tgt.addAction(convertTeardownActionComponent(t));
    return tgt;
  }

  public org.hl7.fhir.instance.model.TestScript.TestScriptTeardownComponent convertTestScriptTeardownComponent(org.hl7.fhir.dstu3.model.TestScript.TestScriptTeardownComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.TestScript.TestScriptTeardownComponent tgt = new org.hl7.fhir.instance.model.TestScript.TestScriptTeardownComponent();
    copyElement(src, tgt);
    for (org.hl7.fhir.dstu3.model.TestScript.TeardownActionComponent t : src.getAction())
      tgt.addAction(convertTeardownActionComponent(t));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.TestScript.TeardownActionComponent convertTeardownActionComponent(org.hl7.fhir.instance.model.TestScript.TestScriptTeardownActionComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.TestScript.TeardownActionComponent tgt = new org.hl7.fhir.dstu3.model.TestScript.TeardownActionComponent();
    copyElement(src, tgt);
    tgt.setOperation(convertSetupActionOperationComponent(src.getOperation()));
    return tgt;
  }

  public org.hl7.fhir.instance.model.TestScript.TestScriptTeardownActionComponent convertTeardownActionComponent(org.hl7.fhir.dstu3.model.TestScript.TeardownActionComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.TestScript.TestScriptTeardownActionComponent tgt = new org.hl7.fhir.instance.model.TestScript.TestScriptTeardownActionComponent();
    copyElement(src, tgt);
    tgt.setOperation(convertSetupActionOperationComponent(src.getOperation()));
    return tgt;
  }

  public org.hl7.fhir.instance.model.ValueSet.ValueSetCodeSystemComponent convertCodeSystem(org.hl7.fhir.dstu3.model.CodeSystem src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.ValueSet.ValueSetCodeSystemComponent tgt = new org.hl7.fhir.instance.model.ValueSet.ValueSetCodeSystemComponent();
    copyElement(src, tgt);
    tgt.setSystem(src.getUrl());
    tgt.setVersion(src.getVersion());
    tgt.setCaseSensitive(src.getCaseSensitive());

    for (ConceptDefinitionComponent cc : src.getConcept())
      tgt.addConcept(convertCodeSystemConcept(src, cc));
    return tgt;
  }

public org.hl7.fhir.instance.model.ValueSet.ConceptDefinitionComponent convertCodeSystemConcept(CodeSystem cs, ConceptDefinitionComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.ValueSet.ConceptDefinitionComponent tgt = new org.hl7.fhir.instance.model.ValueSet.ConceptDefinitionComponent();
    copyElement(src, tgt);
    tgt.setAbstract(CodeSystemUtilities.isNotSelectable(cs, src));
    tgt.setCode(src.getCode());
    tgt.setDefinition(src.getDefinition());
    tgt.setDisplay(src.getDisplay());

    for (ConceptDefinitionComponent cc : src.getConcept())
      tgt.addConcept(convertCodeSystemConcept(cs, cc));
    for (ConceptDefinitionDesignationComponent cc : src.getDesignation())
      tgt.addDesignation(convertCodeSystemDesignation(cc));
    return tgt;
  }

public org.hl7.fhir.instance.model.ValueSet.ConceptDefinitionDesignationComponent convertCodeSystemDesignation(ConceptDefinitionDesignationComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.ValueSet.ConceptDefinitionDesignationComponent tgt = new org.hl7.fhir.instance.model.ValueSet.ConceptDefinitionDesignationComponent();
    copyElement(src, tgt);
    tgt.setUse(convertCoding(src.getUse()));
    tgt.setLanguage(src.getLanguage());
    tgt.setValue(src.getValue());

    return tgt;
  }

  public org.hl7.fhir.dstu3.model.ValueSet convertValueSet(org.hl7.fhir.instance.model.ValueSet src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.ValueSet tgt = new org.hl7.fhir.dstu3.model.ValueSet();
    copyDomainResource(src, tgt);
    tgt.setUrl(src.getUrl());
    tgt.addIdentifier(convertIdentifier(src.getIdentifier()));
    tgt.setVersion(src.getVersion());
    tgt.setName(src.getName());
    tgt.setStatus(convertConformanceResourceStatus(src.getStatus()));
    if (src.hasExperimental())
      tgt.setExperimental(src.getExperimental());
    tgt.setPublisher(src.getPublisher());
    for (org.hl7.fhir.instance.model.ValueSet.ValueSetContactComponent t : src.getContact())
      tgt.addContact(convertValueSetContactComponent(t));
    if (src.hasDate())
      tgt.setDate(src.getDate());
    tgt.setDescription(src.getDescription());
    for (org.hl7.fhir.instance.model.CodeableConcept t : src.getUseContext())
      if (isJurisdiction(t))
        tgt.addJurisdiction(convertCodeableConcept(t));
      else
        tgt.addUseContext(convertCodeableConceptToUsageContext(t));
    tgt.setImmutable(src.getImmutable());
    tgt.setPurpose(src.getRequirements());
    tgt.setCopyright(src.getCopyright());
    tgt.setExtensible(src.getExtensible());
    if (src.hasCompose()) {
      tgt.setCompose(convertValueSetComposeComponent(src.getCompose()));
      tgt.getCompose().setLockedDate(src.getLockedDate());
    }
    if (src.hasCodeSystem() && advisor != null) {
      org.hl7.fhir.dstu3.model.CodeSystem tgtcs = new org.hl7.fhir.dstu3.model.CodeSystem();
      copyDomainResource(src, tgtcs);
      tgtcs.setUrl(src.getCodeSystem().getSystem());
      tgtcs.setIdentifier(convertIdentifier(src.getIdentifier()));
      tgtcs.setVersion(src.getCodeSystem().getVersion());
      tgtcs.setName(src.getName()+" Code System");
      tgtcs.setStatus(convertConformanceResourceStatus(src.getStatus()));
      if (src.hasExperimental())
        tgtcs.setExperimental(src.getExperimental());
      tgtcs.setPublisher(src.getPublisher());
      for (org.hl7.fhir.instance.model.ValueSet.ValueSetContactComponent t : src.getContact())
        tgtcs.addContact(convertValueSetContactComponent(t));
      if (src.hasDate())
        tgtcs.setDate(src.getDate());
      tgtcs.setDescription(src.getDescription());
      for (org.hl7.fhir.instance.model.CodeableConcept t : src.getUseContext())
        if (isJurisdiction(t))
          tgtcs.addJurisdiction(convertCodeableConcept(t));
        else
          tgtcs.addUseContext(convertCodeableConceptToUsageContext(t));
      tgtcs.setPurpose(src.getRequirements());
      tgtcs.setCopyright(src.getCopyright());
      tgtcs.setContent(CodeSystemContentMode.COMPLETE);
      tgtcs.setCaseSensitive(src.getCodeSystem().getCaseSensitive());
      for (org.hl7.fhir.instance.model.ValueSet.ConceptDefinitionComponent cs : src.getCodeSystem().getConcept())
        processConcept(tgtcs.getConcept(), cs, tgtcs);
      advisor.handleCodeSystem(tgtcs, tgt);
      tgt.setUserData("r2-cs", tgtcs);
      tgt.getCompose().addInclude().setSystem(tgtcs.getUrl());
    }
    tgt.setExpansion(convertValueSetExpansionComponent(src.getExpansion()));
    return tgt;
  }

  private void processConcept(List<ConceptDefinitionComponent> concepts, org.hl7.fhir.instance.model.ValueSet.ConceptDefinitionComponent cs, CodeSystem tgtcs) throws FHIRException {
    org.hl7.fhir.dstu3.model.CodeSystem.ConceptDefinitionComponent ct = new org.hl7.fhir.dstu3.model.CodeSystem.ConceptDefinitionComponent();
    concepts.add(ct);
    ct.setCode(cs.getCode());
    ct.setDisplay(cs.getDisplay());
    ct.setDefinition(cs.getDefinition());
    if (cs.getAbstract())
      CodeSystemUtilities.setNotSelectable(tgtcs, ct);
    for (org.hl7.fhir.instance.model.ValueSet.ConceptDefinitionDesignationComponent csd : cs.getDesignation()) {
      org.hl7.fhir.dstu3.model.CodeSystem.ConceptDefinitionDesignationComponent cst = new org.hl7.fhir.dstu3.model.CodeSystem.ConceptDefinitionDesignationComponent();
      cst.setLanguage(csd.getLanguage());
      cst.setUse(convertCoding(csd.getUse()));
      cst.setValue(csd.getValue());
    }

    for (org.hl7.fhir.instance.model.ValueSet.ConceptDefinitionComponent csc : cs.getConcept())
      processConcept(ct.getConcept(), csc, tgtcs);
  }

  private void processConcept(List<org.hl7.fhir.instance.model.ValueSet.ConceptDefinitionComponent> concepts, ConceptDefinitionComponent cs, CodeSystem srcCS) throws FHIRException {
    org.hl7.fhir.instance.model.ValueSet.ConceptDefinitionComponent ct = new org.hl7.fhir.instance.model.ValueSet.ConceptDefinitionComponent();
    concepts.add(ct);
    ct.setCode(cs.getCode());
    ct.setDisplay(cs.getDisplay());
    ct.setDefinition(cs.getDefinition());
    if (CodeSystemUtilities.isNotSelectable(srcCS, cs))
      ct.setAbstract(true);
    for (org.hl7.fhir.dstu3.model.CodeSystem.ConceptDefinitionDesignationComponent csd : cs.getDesignation()) {
      org.hl7.fhir.instance.model.ValueSet.ConceptDefinitionDesignationComponent cst = new org.hl7.fhir.instance.model.ValueSet.ConceptDefinitionDesignationComponent();
      cst.setLanguage(csd.getLanguage());
      cst.setUse(convertCoding(csd.getUse()));
      cst.setValue(csd.getValue());
    }

    for (ConceptDefinitionComponent csc : cs.getConcept())
      processConcept(ct.getConcept(), csc, srcCS);
  }

  public org.hl7.fhir.instance.model.ValueSet convertValueSet(org.hl7.fhir.dstu3.model.ValueSet src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.ValueSet tgt = new org.hl7.fhir.instance.model.ValueSet();
    copyDomainResource(src, tgt);
    tgt.setUrl(src.getUrl());
    for (org.hl7.fhir.dstu3.model.Identifier i : src.getIdentifier())
      tgt.setIdentifier(convertIdentifier(i));
    tgt.setVersion(src.getVersion());
    tgt.setName(src.getName());
    tgt.setStatus(convertConformanceResourceStatus(src.getStatus()));
    if (src.hasExperimental())
      tgt.setExperimental(src.getExperimental());
    tgt.setPublisher(src.getPublisher());
    for (org.hl7.fhir.dstu3.model.ContactDetail t : src.getContact())
      tgt.addContact(convertValueSetContactComponent(t));
    if (src.hasDate())
      tgt.setDate(src.getDate());
    tgt.setLockedDate(src.getCompose().getLockedDate());
    tgt.setDescription(src.getDescription());
    for (org.hl7.fhir.dstu3.model.UsageContext t : src.getUseContext())
      if (t.hasValueCodeableConcept())
        tgt.addUseContext(convertCodeableConcept(t.getValueCodeableConcept()));
    for (org.hl7.fhir.dstu3.model.CodeableConcept t : src.getJurisdiction())
        tgt.addUseContext(convertCodeableConcept(t));
    tgt.setImmutable(src.getImmutable());
    tgt.setRequirements(src.getPurpose());
    tgt.setCopyright(src.getCopyright());
    tgt.setExtensible(src.getExtensible());
    org.hl7.fhir.dstu3.model.CodeSystem srcCS = (CodeSystem) src.getUserData("r2-cs");
    if (srcCS == null)
      srcCS = advisor.getCodeSystem(src);
    if (srcCS != null) {
      tgt.getCodeSystem().setSystem(srcCS.getUrl());
      tgt.getCodeSystem().setVersion(srcCS.getVersion());
      tgt.getCodeSystem().setCaseSensitive(srcCS.getCaseSensitive());
      for (org.hl7.fhir.dstu3.model.CodeSystem.ConceptDefinitionComponent cs : srcCS.getConcept())
        processConcept(tgt.getCodeSystem().getConcept(), cs, srcCS);

    }
    tgt.setCompose(convertValueSetComposeComponent(src.getCompose(), srcCS == null ? null : srcCS.getUrl()));
    tgt.setExpansion(convertValueSetExpansionComponent(src.getExpansion()));
    return tgt;
  }

  private static boolean isJurisdiction(CodeableConcept t) {
    return t.hasCoding() && ("http://unstats.un.org/unsd/methods/m49/m49.htm".equals(t.getCoding().get(0).getSystem()) || "urn:iso:std:iso:3166".equals(t.getCoding().get(0).getSystem())
        || "https://www.usps.com/".equals(t.getCoding().get(0).getSystem()));
  }


  public org.hl7.fhir.dstu3.model.ContactDetail convertValueSetContactComponent(org.hl7.fhir.instance.model.ValueSet.ValueSetContactComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.ContactDetail tgt = new org.hl7.fhir.dstu3.model.ContactDetail();
    copyElement(src, tgt);
    tgt.setName(src.getName());
    for (org.hl7.fhir.instance.model.ContactPoint t : src.getTelecom())
      tgt.addTelecom(convertContactPoint(t));
    return tgt;
  }

  public org.hl7.fhir.instance.model.ValueSet.ValueSetContactComponent convertValueSetContactComponent(org.hl7.fhir.dstu3.model.ContactDetail src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.ValueSet.ValueSetContactComponent tgt = new org.hl7.fhir.instance.model.ValueSet.ValueSetContactComponent();
    copyElement(src, tgt);
    tgt.setName(src.getName());
    for (org.hl7.fhir.dstu3.model.ContactPoint t : src.getTelecom())
      tgt.addTelecom(convertContactPoint(t));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.ValueSet.ValueSetComposeComponent convertValueSetComposeComponent(org.hl7.fhir.instance.model.ValueSet.ValueSetComposeComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.ValueSet.ValueSetComposeComponent tgt = new org.hl7.fhir.dstu3.model.ValueSet.ValueSetComposeComponent();
    copyElement(src, tgt);
    for (org.hl7.fhir.instance.model.UriType t : src.getImport())
      tgt.addInclude().addValueSet(t.getValue());
    for (org.hl7.fhir.instance.model.ValueSet.ConceptSetComponent t : src.getInclude())
      tgt.addInclude(convertConceptSetComponent(t));
    for (org.hl7.fhir.instance.model.ValueSet.ConceptSetComponent t : src.getExclude())
      tgt.addExclude(convertConceptSetComponent(t));
    return tgt;
  }

  public org.hl7.fhir.instance.model.ValueSet.ValueSetComposeComponent convertValueSetComposeComponent(org.hl7.fhir.dstu3.model.ValueSet.ValueSetComposeComponent src, String noSystem) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.ValueSet.ValueSetComposeComponent tgt = new org.hl7.fhir.instance.model.ValueSet.ValueSetComposeComponent();
    copyElement(src, tgt);
    for (org.hl7.fhir.dstu3.model.ValueSet.ConceptSetComponent t : src.getInclude()) {
      for (org.hl7.fhir.dstu3.model.UriType ti : t.getValueSet())
        tgt.addImport(ti.getValue());
      if (noSystem == null || !t.getSystem().equals(noSystem))
        tgt.addInclude(convertConceptSetComponent(t));
    }
    for (org.hl7.fhir.dstu3.model.ValueSet.ConceptSetComponent t : src.getExclude())
      tgt.addExclude(convertConceptSetComponent(t));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.ValueSet.ConceptSetComponent convertConceptSetComponent(org.hl7.fhir.instance.model.ValueSet.ConceptSetComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.ValueSet.ConceptSetComponent tgt = new org.hl7.fhir.dstu3.model.ValueSet.ConceptSetComponent();
    copyElement(src, tgt);
    tgt.setSystem(src.getSystem());
    tgt.setVersion(src.getVersion());
    for (org.hl7.fhir.instance.model.ValueSet.ConceptReferenceComponent t : src.getConcept())
      tgt.addConcept(convertConceptReferenceComponent(t));
    for (org.hl7.fhir.instance.model.ValueSet.ConceptSetFilterComponent t : src.getFilter())
      tgt.addFilter(convertConceptSetFilterComponent(t));
    return tgt;
  }

  public org.hl7.fhir.instance.model.ValueSet.ConceptSetComponent convertConceptSetComponent(org.hl7.fhir.dstu3.model.ValueSet.ConceptSetComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.ValueSet.ConceptSetComponent tgt = new org.hl7.fhir.instance.model.ValueSet.ConceptSetComponent();
    copyElement(src, tgt);
    tgt.setSystem(src.getSystem());
    tgt.setVersion(src.getVersion());
    for (org.hl7.fhir.dstu3.model.ValueSet.ConceptReferenceComponent t : src.getConcept())
      tgt.addConcept(convertConceptReferenceComponent(t));
    for (org.hl7.fhir.dstu3.model.ValueSet.ConceptSetFilterComponent t : src.getFilter())
      tgt.addFilter(convertConceptSetFilterComponent(t));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.ValueSet.ConceptReferenceComponent convertConceptReferenceComponent(org.hl7.fhir.instance.model.ValueSet.ConceptReferenceComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.ValueSet.ConceptReferenceComponent tgt = new org.hl7.fhir.dstu3.model.ValueSet.ConceptReferenceComponent();
    copyElement(src, tgt);
    tgt.setCode(src.getCode());
    tgt.setDisplay(src.getDisplay());
    for (org.hl7.fhir.instance.model.ValueSet.ConceptDefinitionDesignationComponent t : src.getDesignation())
      tgt.addDesignation(convertConceptReferenceDesignationComponent(t));
    return tgt;
  }

  public org.hl7.fhir.instance.model.ValueSet.ConceptReferenceComponent convertConceptReferenceComponent(org.hl7.fhir.dstu3.model.ValueSet.ConceptReferenceComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.ValueSet.ConceptReferenceComponent tgt = new org.hl7.fhir.instance.model.ValueSet.ConceptReferenceComponent();
    copyElement(src, tgt);
    tgt.setCode(src.getCode());
    tgt.setDisplay(src.getDisplay());
    for (org.hl7.fhir.dstu3.model.ValueSet.ConceptReferenceDesignationComponent t : src.getDesignation())
      tgt.addDesignation(convertConceptReferenceDesignationComponent(t));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.ValueSet.ConceptReferenceDesignationComponent convertConceptReferenceDesignationComponent(org.hl7.fhir.instance.model.ValueSet.ConceptDefinitionDesignationComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.ValueSet.ConceptReferenceDesignationComponent tgt = new org.hl7.fhir.dstu3.model.ValueSet.ConceptReferenceDesignationComponent();
    copyElement(src, tgt);
    tgt.setLanguage(src.getLanguage());
    tgt.setUse(convertCoding(src.getUse()));
    tgt.setValue(src.getValue());
    return tgt;
  }

  public org.hl7.fhir.instance.model.ValueSet.ConceptDefinitionDesignationComponent convertConceptReferenceDesignationComponent(org.hl7.fhir.dstu3.model.ValueSet.ConceptReferenceDesignationComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.ValueSet.ConceptDefinitionDesignationComponent tgt = new org.hl7.fhir.instance.model.ValueSet.ConceptDefinitionDesignationComponent();
    copyElement(src, tgt);
    tgt.setLanguage(src.getLanguage());
    tgt.setUse(convertCoding(src.getUse()));
    tgt.setValue(src.getValue());
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.ValueSet.ConceptSetFilterComponent convertConceptSetFilterComponent(org.hl7.fhir.instance.model.ValueSet.ConceptSetFilterComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.ValueSet.ConceptSetFilterComponent tgt = new org.hl7.fhir.dstu3.model.ValueSet.ConceptSetFilterComponent();
    copyElement(src, tgt);
    tgt.setProperty(src.getProperty());
    tgt.setOp(convertFilterOperator(src.getOp()));
    tgt.setValue(src.getValue());
    return tgt;
  }

  public org.hl7.fhir.instance.model.ValueSet.ConceptSetFilterComponent convertConceptSetFilterComponent(org.hl7.fhir.dstu3.model.ValueSet.ConceptSetFilterComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.ValueSet.ConceptSetFilterComponent tgt = new org.hl7.fhir.instance.model.ValueSet.ConceptSetFilterComponent();
    copyElement(src, tgt);
    tgt.setProperty(src.getProperty());
    tgt.setOp(convertFilterOperator(src.getOp()));
    tgt.setValue(src.getValue());
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.ValueSet.FilterOperator convertFilterOperator(org.hl7.fhir.instance.model.ValueSet.FilterOperator src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case EQUAL: return org.hl7.fhir.dstu3.model.ValueSet.FilterOperator.EQUAL;
    case ISA: return org.hl7.fhir.dstu3.model.ValueSet.FilterOperator.ISA;
    case ISNOTA: return org.hl7.fhir.dstu3.model.ValueSet.FilterOperator.ISNOTA;
    case REGEX: return org.hl7.fhir.dstu3.model.ValueSet.FilterOperator.REGEX;
    case IN: return org.hl7.fhir.dstu3.model.ValueSet.FilterOperator.IN;
    case NOTIN: return org.hl7.fhir.dstu3.model.ValueSet.FilterOperator.NOTIN;
    default: return org.hl7.fhir.dstu3.model.ValueSet.FilterOperator.NULL;
    }
  }

  public org.hl7.fhir.instance.model.ValueSet.FilterOperator convertFilterOperator(org.hl7.fhir.dstu3.model.ValueSet.FilterOperator src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case EQUAL: return org.hl7.fhir.instance.model.ValueSet.FilterOperator.EQUAL;
    case ISA: return org.hl7.fhir.instance.model.ValueSet.FilterOperator.ISA;
    case ISNOTA: return org.hl7.fhir.instance.model.ValueSet.FilterOperator.ISNOTA;
    case REGEX: return org.hl7.fhir.instance.model.ValueSet.FilterOperator.REGEX;
    case IN: return org.hl7.fhir.instance.model.ValueSet.FilterOperator.IN;
    case NOTIN: return org.hl7.fhir.instance.model.ValueSet.FilterOperator.NOTIN;
    default: return org.hl7.fhir.instance.model.ValueSet.FilterOperator.NULL;
    }
  }

  public org.hl7.fhir.dstu3.model.ValueSet.ValueSetExpansionComponent convertValueSetExpansionComponent(org.hl7.fhir.instance.model.ValueSet.ValueSetExpansionComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.ValueSet.ValueSetExpansionComponent tgt = new org.hl7.fhir.dstu3.model.ValueSet.ValueSetExpansionComponent();
    copyElement(src, tgt);
    tgt.setIdentifier(src.getIdentifier());
    tgt.setTimestamp(src.getTimestamp());
    tgt.setTotal(src.getTotal());
    tgt.setOffset(src.getOffset());
    for (org.hl7.fhir.instance.model.ValueSet.ValueSetExpansionParameterComponent t : src.getParameter())
      tgt.addParameter(convertValueSetExpansionParameterComponent(t));
    for (org.hl7.fhir.instance.model.ValueSet.ValueSetExpansionContainsComponent t : src.getContains())
      tgt.addContains(convertValueSetExpansionContainsComponent(t));
    return tgt;
  }

  public org.hl7.fhir.instance.model.ValueSet.ValueSetExpansionComponent convertValueSetExpansionComponent(org.hl7.fhir.dstu3.model.ValueSet.ValueSetExpansionComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.ValueSet.ValueSetExpansionComponent tgt = new org.hl7.fhir.instance.model.ValueSet.ValueSetExpansionComponent();
    copyElement(src, tgt);
    tgt.setIdentifier(src.getIdentifier());
    tgt.setTimestamp(src.getTimestamp());
    tgt.setTotal(src.getTotal());
    tgt.setOffset(src.getOffset());
    for (org.hl7.fhir.dstu3.model.ValueSet.ValueSetExpansionParameterComponent t : src.getParameter())
      tgt.addParameter(convertValueSetExpansionParameterComponent(t));
    for (org.hl7.fhir.dstu3.model.ValueSet.ValueSetExpansionContainsComponent t : src.getContains())
      tgt.addContains(convertValueSetExpansionContainsComponent(t));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.ValueSet.ValueSetExpansionParameterComponent convertValueSetExpansionParameterComponent(org.hl7.fhir.instance.model.ValueSet.ValueSetExpansionParameterComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.ValueSet.ValueSetExpansionParameterComponent tgt = new org.hl7.fhir.dstu3.model.ValueSet.ValueSetExpansionParameterComponent();
    copyElement(src, tgt);
    tgt.setName(src.getName());
    tgt.setValue(convertType(src.getValue()));
    return tgt;
  }

  public org.hl7.fhir.instance.model.ValueSet.ValueSetExpansionParameterComponent convertValueSetExpansionParameterComponent(org.hl7.fhir.dstu3.model.ValueSet.ValueSetExpansionParameterComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.ValueSet.ValueSetExpansionParameterComponent tgt = new org.hl7.fhir.instance.model.ValueSet.ValueSetExpansionParameterComponent();
    copyElement(src, tgt);
    tgt.setName(src.getName());
    tgt.setValue(convertType(src.getValue()));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.ValueSet.ValueSetExpansionContainsComponent convertValueSetExpansionContainsComponent(org.hl7.fhir.instance.model.ValueSet.ValueSetExpansionContainsComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.ValueSet.ValueSetExpansionContainsComponent tgt = new org.hl7.fhir.dstu3.model.ValueSet.ValueSetExpansionContainsComponent();
    copyElement(src, tgt);
    tgt.setSystem(src.getSystem());
    tgt.setAbstract(src.getAbstract());
    tgt.setVersion(src.getVersion());
    tgt.setCode(src.getCode());
    tgt.setDisplay(src.getDisplay());
    for (org.hl7.fhir.instance.model.ValueSet.ValueSetExpansionContainsComponent t : src.getContains())
      tgt.addContains(convertValueSetExpansionContainsComponent(t));
    return tgt;
  }

  public org.hl7.fhir.instance.model.ValueSet.ValueSetExpansionContainsComponent convertValueSetExpansionContainsComponent(org.hl7.fhir.dstu3.model.ValueSet.ValueSetExpansionContainsComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.ValueSet.ValueSetExpansionContainsComponent tgt = new org.hl7.fhir.instance.model.ValueSet.ValueSetExpansionContainsComponent();
    copyElement(src, tgt);
    tgt.setSystem(src.getSystem());
    tgt.setAbstract(src.getAbstract());
    tgt.setVersion(src.getVersion());
    tgt.setCode(src.getCode());
    tgt.setDisplay(src.getDisplay());
    for (org.hl7.fhir.dstu3.model.ValueSet.ValueSetExpansionContainsComponent t : src.getContains())
      tgt.addContains(convertValueSetExpansionContainsComponent(t));
    return tgt;
  }

  public org.hl7.fhir.dstu3.model.ListResource convertList(org.hl7.fhir.instance.model.List_ src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.ListResource tgt = new org.hl7.fhir.dstu3.model.ListResource();
    copyDomainResource(src, tgt);
    for (org.hl7.fhir.instance.model.Identifier t : src.getIdentifier())
      tgt.addIdentifier(convertIdentifier(t));
    tgt.setTitle(src.getTitle());
    tgt.setCode(convertCodeableConcept(src.getCode()));
    tgt.setSubject(convertReference(src.getSubject()));
    tgt.setSource(convertReference(src.getSource()));
    tgt.setEncounter(convertReference(src.getEncounter()));
    tgt.setStatus(convertListStatus(src.getStatus()));
    if (src.hasDate())
      tgt.setDate(src.getDate());
    tgt.setOrderedBy(convertCodeableConcept(src.getOrderedBy()));
    tgt.setMode(convertListMode(src.getMode()));
    if (src.hasNote())
      tgt.addNote(new org.hl7.fhir.dstu3.model.Annotation().setText(src.getNote()));
    for (org.hl7.fhir.instance.model.List_.ListEntryComponent t : src.getEntry())
      tgt.addEntry(convertListEntry(t));
    return tgt;
  }


  public org.hl7.fhir.dstu3.model.ListResource.ListStatus convertListStatus(org.hl7.fhir.instance.model.List_.ListStatus src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case CURRENT: return org.hl7.fhir.dstu3.model.ListResource.ListStatus.CURRENT;
    case RETIRED: return org.hl7.fhir.dstu3.model.ListResource.ListStatus.RETIRED;
    case ENTEREDINERROR:  return org.hl7.fhir.dstu3.model.ListResource.ListStatus.ENTEREDINERROR;
    default: return org.hl7.fhir.dstu3.model.ListResource.ListStatus.NULL;
    }
  }

  public org.hl7.fhir.dstu3.model.ListResource.ListMode convertListMode(org.hl7.fhir.instance.model.List_.ListMode src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case WORKING: return org.hl7.fhir.dstu3.model.ListResource.ListMode.WORKING;
    case SNAPSHOT: return org.hl7.fhir.dstu3.model.ListResource.ListMode.SNAPSHOT;
    case CHANGES:  return org.hl7.fhir.dstu3.model.ListResource.ListMode.CHANGES;
    default: return org.hl7.fhir.dstu3.model.ListResource.ListMode.NULL;
    }
  }

  public org.hl7.fhir.dstu3.model.ListResource.ListEntryComponent convertListEntry(org.hl7.fhir.instance.model.List_.ListEntryComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu3.model.ListResource.ListEntryComponent tgt = new org.hl7.fhir.dstu3.model.ListResource.ListEntryComponent();
    copyBackboneElement(src, tgt);
    tgt.setFlag(convertCodeableConcept(src.getFlag()));
    tgt.setDeleted(src.getDeleted());
    if (src.hasDate())
      tgt.setDate(src.getDate());
    tgt.setItem(convertReference(src.getItem()));
    return tgt;
  }

  public org.hl7.fhir.instance.model.List_ convertList(org.hl7.fhir.dstu3.model.ListResource src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.List_ tgt = new org.hl7.fhir.instance.model.List_();
    copyDomainResource(src, tgt);
    for (org.hl7.fhir.dstu3.model.Identifier t : src.getIdentifier())
      tgt.addIdentifier(convertIdentifier(t));
    tgt.setTitle(src.getTitle());
    tgt.setCode(convertCodeableConcept(src.getCode()));
    tgt.setSubject(convertReference(src.getSubject()));
    tgt.setSource(convertReference(src.getSource()));
    tgt.setEncounter(convertReference(src.getEncounter()));
    tgt.setStatus(convertListStatus(src.getStatus()));
    if (src.hasDate())
      tgt.setDate(src.getDate());
    tgt.setOrderedBy(convertCodeableConcept(src.getOrderedBy()));
    tgt.setMode(convertListMode(src.getMode()));
    for (org.hl7.fhir.dstu3.model.Annotation t : src.getNote())
      tgt.setNote(t.getText());
    for (org.hl7.fhir.dstu3.model.ListResource.ListEntryComponent t : src.getEntry())
      tgt.addEntry(convertListEntry(t));
    return tgt;
  }


  public org.hl7.fhir.instance.model.List_.ListStatus convertListStatus(org.hl7.fhir.dstu3.model.ListResource.ListStatus src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case CURRENT: return org.hl7.fhir.instance.model.List_.ListStatus.CURRENT;
    case RETIRED: return org.hl7.fhir.instance.model.List_.ListStatus.RETIRED;
    case ENTEREDINERROR:  return org.hl7.fhir.instance.model.List_.ListStatus.ENTEREDINERROR;
    default: return org.hl7.fhir.instance.model.List_.ListStatus.NULL;
    }
  }

  public org.hl7.fhir.instance.model.List_.ListMode convertListMode(org.hl7.fhir.dstu3.model.ListResource.ListMode src) throws FHIRException {
    if (src == null)
      return null;
    switch (src) {
    case WORKING: return org.hl7.fhir.instance.model.List_.ListMode.WORKING;
    case SNAPSHOT: return org.hl7.fhir.instance.model.List_.ListMode.SNAPSHOT;
    case CHANGES:  return org.hl7.fhir.instance.model.List_.ListMode.CHANGES;
    default: return org.hl7.fhir.instance.model.List_.ListMode.NULL;
    }
  }

  public org.hl7.fhir.instance.model.List_.ListEntryComponent convertListEntry(org.hl7.fhir.dstu3.model.ListResource.ListEntryComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.instance.model.List_.ListEntryComponent tgt = new org.hl7.fhir.instance.model.List_.ListEntryComponent();
    copyBackboneElement(src, tgt);
    tgt.setFlag(convertCodeableConcept(src.getFlag()));
    tgt.setDeleted(src.getDeleted());
    if (src.hasDate())
      tgt.setDate(src.getDate());
    tgt.setItem(convertReference(src.getItem()));
    return tgt;
  }



  public org.hl7.fhir.dstu3.model.Resource convertResource(org.hl7.fhir.instance.model.Resource src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    if (src instanceof org.hl7.fhir.instance.model.Parameters)
      return convertParameters((org.hl7.fhir.instance.model.Parameters) src);
    if (src instanceof org.hl7.fhir.instance.model.Account)
      return convertAccount((org.hl7.fhir.instance.model.Account) src);
    if (src instanceof org.hl7.fhir.instance.model.Appointment)
      return convertAppointment((org.hl7.fhir.instance.model.Appointment) src);
    if (src instanceof org.hl7.fhir.instance.model.AppointmentResponse)
      return convertAppointmentResponse((org.hl7.fhir.instance.model.AppointmentResponse) src);
    if (src instanceof org.hl7.fhir.instance.model.AuditEvent)
      return convertAuditEvent((org.hl7.fhir.instance.model.AuditEvent) src);
    if (src instanceof org.hl7.fhir.instance.model.Basic)
      return convertBasic((org.hl7.fhir.instance.model.Basic) src);
    if (src instanceof org.hl7.fhir.instance.model.Binary)
      return convertBinary((org.hl7.fhir.instance.model.Binary) src);
    if (src instanceof org.hl7.fhir.instance.model.Bundle)
      return convertBundle((org.hl7.fhir.instance.model.Bundle) src);
    if (src instanceof org.hl7.fhir.instance.model.CarePlan)
      return convertCarePlan((org.hl7.fhir.instance.model.CarePlan) src);
    if (src instanceof org.hl7.fhir.instance.model.ClinicalImpression)
      return convertClinicalImpression((org.hl7.fhir.instance.model.ClinicalImpression) src);
    if (src instanceof org.hl7.fhir.instance.model.Communication)
      return convertCommunication((org.hl7.fhir.instance.model.Communication) src);
    if (src instanceof org.hl7.fhir.instance.model.CommunicationRequest)
      return convertCommunicationRequest((org.hl7.fhir.instance.model.CommunicationRequest) src);
    if (src instanceof org.hl7.fhir.instance.model.Composition)
      return convertComposition((org.hl7.fhir.instance.model.Composition) src);
    if (src instanceof org.hl7.fhir.instance.model.ConceptMap)
      return convertConceptMap((org.hl7.fhir.instance.model.ConceptMap) src);
    if (src instanceof org.hl7.fhir.instance.model.Condition)
      return convertCondition((org.hl7.fhir.instance.model.Condition) src);
    if (src instanceof org.hl7.fhir.instance.model.Conformance)
      return convertConformance((org.hl7.fhir.instance.model.Conformance) src);
    if (src instanceof org.hl7.fhir.instance.model.Contract)
      return convertContract((org.hl7.fhir.instance.model.Contract) src);
    if (src instanceof org.hl7.fhir.instance.model.DataElement)
      return convertDataElement((org.hl7.fhir.instance.model.DataElement) src);
    if (src instanceof org.hl7.fhir.instance.model.DetectedIssue)
      return convertDetectedIssue((org.hl7.fhir.instance.model.DetectedIssue) src);
    if (src instanceof org.hl7.fhir.instance.model.Device)
      return convertDevice((org.hl7.fhir.instance.model.Device) src);
    if (src instanceof org.hl7.fhir.instance.model.DeviceComponent)
      return convertDeviceComponent((org.hl7.fhir.instance.model.DeviceComponent) src);
    if (src instanceof org.hl7.fhir.instance.model.DeviceMetric)
      return convertDeviceMetric((org.hl7.fhir.instance.model.DeviceMetric) src);
    if (src instanceof org.hl7.fhir.instance.model.DeviceUseStatement)
      return convertDeviceUseStatement((org.hl7.fhir.instance.model.DeviceUseStatement) src);
    if (src instanceof org.hl7.fhir.instance.model.DiagnosticReport)
      return convertDiagnosticReport((org.hl7.fhir.instance.model.DiagnosticReport) src);
    if (src instanceof org.hl7.fhir.instance.model.DocumentManifest)
      return convertDocumentManifest((org.hl7.fhir.instance.model.DocumentManifest) src);
    if (src instanceof org.hl7.fhir.instance.model.DocumentReference)
      return convertDocumentReference((org.hl7.fhir.instance.model.DocumentReference) src);
    if (src instanceof org.hl7.fhir.instance.model.Encounter)
      return convertEncounter((org.hl7.fhir.instance.model.Encounter) src);
    if (src instanceof org.hl7.fhir.instance.model.EnrollmentRequest)
      return convertEnrollmentRequest((org.hl7.fhir.instance.model.EnrollmentRequest) src);
    if (src instanceof org.hl7.fhir.instance.model.EnrollmentResponse)
      return convertEnrollmentResponse((org.hl7.fhir.instance.model.EnrollmentResponse) src);
    if (src instanceof org.hl7.fhir.instance.model.EpisodeOfCare)
      return convertEpisodeOfCare((org.hl7.fhir.instance.model.EpisodeOfCare) src);
    if (src instanceof org.hl7.fhir.instance.model.FamilyMemberHistory)
      return convertFamilyMemberHistory((org.hl7.fhir.instance.model.FamilyMemberHistory) src);
    if (src instanceof org.hl7.fhir.instance.model.Flag)
      return convertFlag((org.hl7.fhir.instance.model.Flag) src);
    if (src instanceof org.hl7.fhir.instance.model.Group)
      return convertGroup((org.hl7.fhir.instance.model.Group) src);
    if (src instanceof org.hl7.fhir.instance.model.HealthcareService)
      return convertHealthcareService((org.hl7.fhir.instance.model.HealthcareService) src);
    if (src instanceof org.hl7.fhir.instance.model.ImagingStudy)
      return convertImagingStudy((org.hl7.fhir.instance.model.ImagingStudy) src);
    if (src instanceof org.hl7.fhir.instance.model.Immunization)
      return convertImmunization((org.hl7.fhir.instance.model.Immunization) src);
    if (src instanceof org.hl7.fhir.instance.model.ImmunizationRecommendation)
      return convertImmunizationRecommendation((org.hl7.fhir.instance.model.ImmunizationRecommendation) src);
    if (src instanceof org.hl7.fhir.instance.model.ImplementationGuide)
      return convertImplementationGuide((org.hl7.fhir.instance.model.ImplementationGuide) src);
    if (src instanceof org.hl7.fhir.instance.model.List_)
      return convertList((org.hl7.fhir.instance.model.List_) src);
    if (src instanceof org.hl7.fhir.instance.model.Location)
      return convertLocation((org.hl7.fhir.instance.model.Location) src);
    if (src instanceof org.hl7.fhir.instance.model.Media)
      return convertMedia((org.hl7.fhir.instance.model.Media) src);
    if (src instanceof org.hl7.fhir.instance.model.Medication)
      return convertMedication((org.hl7.fhir.instance.model.Medication) src);
    if (src instanceof org.hl7.fhir.instance.model.MedicationDispense)
      return convertMedicationDispense((org.hl7.fhir.instance.model.MedicationDispense) src);
//    if (src instanceof org.hl7.fhir.instance.model.MedicationOrder)
//      return convertMedicationOrder((org.hl7.fhir.instance.model.MedicationOrder) src);
    if (src instanceof org.hl7.fhir.instance.model.MedicationStatement)
      return convertMedicationStatement((org.hl7.fhir.instance.model.MedicationStatement) src);
    if (src instanceof org.hl7.fhir.instance.model.MessageHeader)
      return convertMessageHeader((org.hl7.fhir.instance.model.MessageHeader) src);
    if (src instanceof org.hl7.fhir.instance.model.NamingSystem)
      return convertNamingSystem((org.hl7.fhir.instance.model.NamingSystem) src);
    if (src instanceof org.hl7.fhir.instance.model.Observation)
      return convertObservation((org.hl7.fhir.instance.model.Observation) src);
    if (src instanceof org.hl7.fhir.instance.model.OperationDefinition)
      return convertOperationDefinition((org.hl7.fhir.instance.model.OperationDefinition) src);
    if (src instanceof org.hl7.fhir.instance.model.OperationOutcome)
      return convertOperationOutcome((org.hl7.fhir.instance.model.OperationOutcome) src);
    if (src instanceof org.hl7.fhir.instance.model.Organization)
      return convertOrganization((org.hl7.fhir.instance.model.Organization) src);
    if (src instanceof org.hl7.fhir.instance.model.Patient)
      return convertPatient((org.hl7.fhir.instance.model.Patient) src);
    if (src instanceof org.hl7.fhir.instance.model.Person)
      return convertPerson((org.hl7.fhir.instance.model.Person) src);
    if (src instanceof org.hl7.fhir.instance.model.Practitioner)
      return convertPractitioner((org.hl7.fhir.instance.model.Practitioner) src);
    if (src instanceof org.hl7.fhir.instance.model.Procedure)
      return convertProcedure((org.hl7.fhir.instance.model.Procedure) src);
    if (src instanceof org.hl7.fhir.instance.model.ProcedureRequest)
      return convertProcedureRequest((org.hl7.fhir.instance.model.ProcedureRequest) src);
    if (src instanceof org.hl7.fhir.instance.model.Provenance)
      return convertProvenance((org.hl7.fhir.instance.model.Provenance) src);
    if (src instanceof org.hl7.fhir.instance.model.Questionnaire)
      return convertQuestionnaire((org.hl7.fhir.instance.model.Questionnaire) src);
    if (src instanceof org.hl7.fhir.instance.model.QuestionnaireResponse)
      return convertQuestionnaireResponse((org.hl7.fhir.instance.model.QuestionnaireResponse) src);
    if (src instanceof org.hl7.fhir.instance.model.ReferralRequest)
      return convertReferralRequest((org.hl7.fhir.instance.model.ReferralRequest) src);
    if (src instanceof org.hl7.fhir.instance.model.RelatedPerson)
      return convertRelatedPerson((org.hl7.fhir.instance.model.RelatedPerson) src);
    if (src instanceof org.hl7.fhir.instance.model.RiskAssessment)
      return convertRiskAssessment((org.hl7.fhir.instance.model.RiskAssessment) src);
    if (src instanceof org.hl7.fhir.instance.model.Schedule)
      return convertSchedule((org.hl7.fhir.instance.model.Schedule) src);
    if (src instanceof org.hl7.fhir.instance.model.SearchParameter)
      return convertSearchParameter((org.hl7.fhir.instance.model.SearchParameter) src);
    if (src instanceof org.hl7.fhir.instance.model.Slot)
      return convertSlot((org.hl7.fhir.instance.model.Slot) src);
    if (src instanceof org.hl7.fhir.instance.model.StructureDefinition)
      return convertStructureDefinition((org.hl7.fhir.instance.model.StructureDefinition) src);
    if (src instanceof org.hl7.fhir.instance.model.Subscription)
      return convertSubscription((org.hl7.fhir.instance.model.Subscription) src);
    if (src instanceof org.hl7.fhir.instance.model.Substance)
      return convertSubstance((org.hl7.fhir.instance.model.Substance) src);
    if (src instanceof org.hl7.fhir.instance.model.SupplyDelivery)
      return convertSupplyDelivery((org.hl7.fhir.instance.model.SupplyDelivery) src);
    if (src instanceof org.hl7.fhir.instance.model.SupplyRequest)
      return convertSupplyRequest((org.hl7.fhir.instance.model.SupplyRequest) src);
    if (src instanceof org.hl7.fhir.instance.model.TestScript)
      return convertTestScript((org.hl7.fhir.instance.model.TestScript) src);
    if (src instanceof org.hl7.fhir.instance.model.ValueSet)
      return convertValueSet((org.hl7.fhir.instance.model.ValueSet) src);
    throw new Error("Unknown resource "+src.getClass());
  }

  public org.hl7.fhir.instance.model.Resource convertResource(org.hl7.fhir.dstu3.model.Resource src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    if (src instanceof org.hl7.fhir.dstu3.model.Parameters)
      return convertParameters((org.hl7.fhir.dstu3.model.Parameters) src);
    if (src instanceof org.hl7.fhir.dstu3.model.Appointment)
      return convertAppointment((org.hl7.fhir.dstu3.model.Appointment) src);
    if (src instanceof org.hl7.fhir.dstu3.model.AppointmentResponse)
      return convertAppointmentResponse((org.hl7.fhir.dstu3.model.AppointmentResponse) src);
    if (src instanceof org.hl7.fhir.dstu3.model.AuditEvent)
      return convertAuditEvent((org.hl7.fhir.dstu3.model.AuditEvent) src);
    if (src instanceof org.hl7.fhir.dstu3.model.Basic)
      return convertBasic((org.hl7.fhir.dstu3.model.Basic) src);
    if (src instanceof org.hl7.fhir.dstu3.model.Binary)
      return convertBinary((org.hl7.fhir.dstu3.model.Binary) src);
    if (src instanceof org.hl7.fhir.dstu3.model.Bundle)
      return convertBundle((org.hl7.fhir.dstu3.model.Bundle) src);
    if (src instanceof org.hl7.fhir.dstu3.model.CarePlan)
      return convertCarePlan((org.hl7.fhir.dstu3.model.CarePlan) src);
    if (src instanceof org.hl7.fhir.dstu3.model.ClinicalImpression)
      return convertClinicalImpression((org.hl7.fhir.dstu3.model.ClinicalImpression) src);
    if (src instanceof org.hl7.fhir.dstu3.model.Communication)
      return convertCommunication((org.hl7.fhir.dstu3.model.Communication) src);
    if (src instanceof org.hl7.fhir.dstu3.model.CommunicationRequest)
      return convertCommunicationRequest((org.hl7.fhir.dstu3.model.CommunicationRequest) src);
    if (src instanceof org.hl7.fhir.dstu3.model.Composition)
      return convertComposition((org.hl7.fhir.dstu3.model.Composition) src);
    if (src instanceof org.hl7.fhir.dstu3.model.ConceptMap)
      return convertConceptMap((org.hl7.fhir.dstu3.model.ConceptMap) src);
    if (src instanceof org.hl7.fhir.dstu3.model.Condition)
      return convertCondition((org.hl7.fhir.dstu3.model.Condition) src);
    if (src instanceof org.hl7.fhir.dstu3.model.CapabilityStatement)
      return convertConformance((org.hl7.fhir.dstu3.model.CapabilityStatement) src);
    if (src instanceof org.hl7.fhir.dstu3.model.Contract)
      return convertContract((org.hl7.fhir.dstu3.model.Contract) src);
    if (src instanceof org.hl7.fhir.dstu3.model.DataElement)
      return convertDataElement((org.hl7.fhir.dstu3.model.DataElement) src);
    if (src instanceof org.hl7.fhir.dstu3.model.DetectedIssue)
      return convertDetectedIssue((org.hl7.fhir.dstu3.model.DetectedIssue) src);
    if (src instanceof org.hl7.fhir.dstu3.model.Device)
      return convertDevice((org.hl7.fhir.dstu3.model.Device) src);
    if (src instanceof org.hl7.fhir.dstu3.model.DeviceComponent)
      return convertDeviceComponent((org.hl7.fhir.dstu3.model.DeviceComponent) src);
    if (src instanceof org.hl7.fhir.dstu3.model.DeviceMetric)
      return convertDeviceMetric((org.hl7.fhir.dstu3.model.DeviceMetric) src);
    if (src instanceof org.hl7.fhir.dstu3.model.DeviceUseStatement)
      return convertDeviceUseStatement((org.hl7.fhir.dstu3.model.DeviceUseStatement) src);
    if (src instanceof org.hl7.fhir.dstu3.model.DiagnosticReport)
      return convertDiagnosticReport((org.hl7.fhir.dstu3.model.DiagnosticReport) src);
    if (src instanceof org.hl7.fhir.dstu3.model.DocumentManifest)
      return convertDocumentManifest((org.hl7.fhir.dstu3.model.DocumentManifest) src);
    if (src instanceof org.hl7.fhir.dstu3.model.DocumentReference)
      return convertDocumentReference((org.hl7.fhir.dstu3.model.DocumentReference) src);
    if (src instanceof org.hl7.fhir.dstu3.model.Encounter)
      return convertEncounter((org.hl7.fhir.dstu3.model.Encounter) src);
    if (src instanceof org.hl7.fhir.dstu3.model.EnrollmentRequest)
      return convertEnrollmentRequest((org.hl7.fhir.dstu3.model.EnrollmentRequest) src);
    if (src instanceof org.hl7.fhir.dstu3.model.EnrollmentResponse)
      return convertEnrollmentResponse((org.hl7.fhir.dstu3.model.EnrollmentResponse) src);
    if (src instanceof org.hl7.fhir.dstu3.model.EpisodeOfCare)
      return convertEpisodeOfCare((org.hl7.fhir.dstu3.model.EpisodeOfCare) src);
    if (src instanceof org.hl7.fhir.dstu3.model.FamilyMemberHistory)
      return convertFamilyMemberHistory((org.hl7.fhir.dstu3.model.FamilyMemberHistory) src);
    if (src instanceof org.hl7.fhir.dstu3.model.Flag)
      return convertFlag((org.hl7.fhir.dstu3.model.Flag) src);
    if (src instanceof org.hl7.fhir.dstu3.model.Group)
      return convertGroup((org.hl7.fhir.dstu3.model.Group) src);
    if (src instanceof org.hl7.fhir.dstu3.model.HealthcareService)
      return convertHealthcareService((org.hl7.fhir.dstu3.model.HealthcareService) src);
    if (src instanceof org.hl7.fhir.dstu3.model.ImagingStudy)
      return convertImagingStudy((org.hl7.fhir.dstu3.model.ImagingStudy) src);
    if (src instanceof org.hl7.fhir.dstu3.model.Immunization)
      return convertImmunization((org.hl7.fhir.dstu3.model.Immunization) src);
    if (src instanceof org.hl7.fhir.dstu3.model.ImmunizationRecommendation)
      return convertImmunizationRecommendation((org.hl7.fhir.dstu3.model.ImmunizationRecommendation) src);
    if (src instanceof org.hl7.fhir.dstu3.model.ImplementationGuide)
      return convertImplementationGuide((org.hl7.fhir.dstu3.model.ImplementationGuide) src);
    if (src instanceof org.hl7.fhir.dstu3.model.ListResource)
      return convertList((org.hl7.fhir.dstu3.model.ListResource) src);
    if (src instanceof org.hl7.fhir.dstu3.model.Location)
      return convertLocation((org.hl7.fhir.dstu3.model.Location) src);
    if (src instanceof org.hl7.fhir.dstu3.model.Media)
      return convertMedia((org.hl7.fhir.dstu3.model.Media) src);
    if (src instanceof org.hl7.fhir.dstu3.model.Medication)
      return convertMedication((org.hl7.fhir.dstu3.model.Medication) src);
    if (src instanceof org.hl7.fhir.dstu3.model.MedicationDispense)
      return convertMedicationDispense((org.hl7.fhir.dstu3.model.MedicationDispense) src);
//    if (src instanceof org.hl7.fhir.dstu3.model.MedicationOrder)
//      return convertMedicationOrder((org.hl7.fhir.dstu3.model.MedicationOrder) src);
    if (src instanceof org.hl7.fhir.dstu3.model.MedicationStatement)
      return convertMedicationStatement((org.hl7.fhir.dstu3.model.MedicationStatement) src);
    if (src instanceof org.hl7.fhir.dstu3.model.MessageHeader)
      return convertMessageHeader((org.hl7.fhir.dstu3.model.MessageHeader) src);
    if (src instanceof org.hl7.fhir.dstu3.model.NamingSystem)
      return convertNamingSystem((org.hl7.fhir.dstu3.model.NamingSystem) src);
    if (src instanceof org.hl7.fhir.dstu3.model.Observation)
      return convertObservation((org.hl7.fhir.dstu3.model.Observation) src);
    if (src instanceof org.hl7.fhir.dstu3.model.OperationDefinition)
      return convertOperationDefinition((org.hl7.fhir.dstu3.model.OperationDefinition) src);
    if (src instanceof org.hl7.fhir.dstu3.model.OperationOutcome)
      return convertOperationOutcome((org.hl7.fhir.dstu3.model.OperationOutcome) src);
    if (src instanceof org.hl7.fhir.dstu3.model.Organization)
      return convertOrganization((org.hl7.fhir.dstu3.model.Organization) src);
    if (src instanceof org.hl7.fhir.dstu3.model.Patient)
      return convertPatient((org.hl7.fhir.dstu3.model.Patient) src);
    if (src instanceof org.hl7.fhir.dstu3.model.Person)
      return convertPerson((org.hl7.fhir.dstu3.model.Person) src);
    if (src instanceof org.hl7.fhir.dstu3.model.Practitioner)
      return convertPractitioner((org.hl7.fhir.dstu3.model.Practitioner) src);
    if (src instanceof org.hl7.fhir.dstu3.model.Procedure)
      return convertProcedure((org.hl7.fhir.dstu3.model.Procedure) src);
    if (src instanceof org.hl7.fhir.dstu3.model.ProcedureRequest)
      return convertProcedureRequest((org.hl7.fhir.dstu3.model.ProcedureRequest) src);
    if (src instanceof org.hl7.fhir.dstu3.model.Provenance)
      return convertProvenance((org.hl7.fhir.dstu3.model.Provenance) src);
    if (src instanceof org.hl7.fhir.dstu3.model.Questionnaire)
      return convertQuestionnaire((org.hl7.fhir.dstu3.model.Questionnaire) src);
    if (src instanceof org.hl7.fhir.dstu3.model.QuestionnaireResponse)
      return convertQuestionnaireResponse((org.hl7.fhir.dstu3.model.QuestionnaireResponse) src);
    if (src instanceof org.hl7.fhir.dstu3.model.ReferralRequest)
      return convertReferralRequest((org.hl7.fhir.dstu3.model.ReferralRequest) src);
    if (src instanceof org.hl7.fhir.dstu3.model.RelatedPerson)
      return convertRelatedPerson((org.hl7.fhir.dstu3.model.RelatedPerson) src);
    if (src instanceof org.hl7.fhir.dstu3.model.RiskAssessment)
      return convertRiskAssessment((org.hl7.fhir.dstu3.model.RiskAssessment) src);
    if (src instanceof org.hl7.fhir.dstu3.model.Schedule)
      return convertSchedule((org.hl7.fhir.dstu3.model.Schedule) src);
    if (src instanceof org.hl7.fhir.dstu3.model.SearchParameter)
      return convertSearchParameter((org.hl7.fhir.dstu3.model.SearchParameter) src);
    if (src instanceof org.hl7.fhir.dstu3.model.Slot)
      return convertSlot((org.hl7.fhir.dstu3.model.Slot) src);
    if (src instanceof org.hl7.fhir.dstu3.model.StructureDefinition)
      return convertStructureDefinition((org.hl7.fhir.dstu3.model.StructureDefinition) src);
    if (src instanceof org.hl7.fhir.dstu3.model.Subscription)
      return convertSubscription((org.hl7.fhir.dstu3.model.Subscription) src);
    if (src instanceof org.hl7.fhir.dstu3.model.Substance)
      return convertSubstance((org.hl7.fhir.dstu3.model.Substance) src);
    if (src instanceof org.hl7.fhir.dstu3.model.SupplyDelivery)
      return convertSupplyDelivery((org.hl7.fhir.dstu3.model.SupplyDelivery) src);
    if (src instanceof org.hl7.fhir.dstu3.model.SupplyRequest)
      return convertSupplyRequest((org.hl7.fhir.dstu3.model.SupplyRequest) src);
    if (src instanceof org.hl7.fhir.dstu3.model.TestScript)
      return convertTestScript((org.hl7.fhir.dstu3.model.TestScript) src);
    if (src instanceof org.hl7.fhir.dstu3.model.ValueSet)
      return convertValueSet((org.hl7.fhir.dstu3.model.ValueSet) src);
    throw new Error("Unknown resource "+src.fhirType());
  }

}
