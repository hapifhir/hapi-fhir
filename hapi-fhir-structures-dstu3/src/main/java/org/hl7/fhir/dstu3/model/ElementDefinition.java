package org.hl7.fhir.dstu3.model;

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

import java.util.*;

import org.hl7.fhir.utilities.Utilities;
import org.hl7.fhir.dstu3.model.Enumerations.*;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.ChildOrder;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.DatatypeDef;
import ca.uhn.fhir.model.api.annotation.Block;
import org.hl7.fhir.instance.model.api.*;
import org.hl7.fhir.exceptions.FHIRException;
/**
 * Captures constraints on each element within the resource, profile, or extension.
 */
@DatatypeDef(name="ElementDefinition")
public class ElementDefinition extends Type implements ICompositeType {

    public enum PropertyRepresentation {
        /**
         * In XML, this property is represented as an attribute not an element.
         */
        XMLATTR, 
        /**
         * This element is represented using the XML text attribute (primitives only)
         */
        XMLTEXT, 
        /**
         * The type of this element is indicated using xsi:type
         */
        TYPEATTR, 
        /**
         * Use CDA narrative instead of XHTML
         */
        CDATEXT, 
        /**
         * The property is represented using XHTML
         */
        XHTML, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static PropertyRepresentation fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("xmlAttr".equals(codeString))
          return XMLATTR;
        if ("xmlText".equals(codeString))
          return XMLTEXT;
        if ("typeAttr".equals(codeString))
          return TYPEATTR;
        if ("cdaText".equals(codeString))
          return CDATEXT;
        if ("xhtml".equals(codeString))
          return XHTML;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown PropertyRepresentation code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case XMLATTR: return "xmlAttr";
            case XMLTEXT: return "xmlText";
            case TYPEATTR: return "typeAttr";
            case CDATEXT: return "cdaText";
            case XHTML: return "xhtml";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case XMLATTR: return "http://hl7.org/fhir/property-representation";
            case XMLTEXT: return "http://hl7.org/fhir/property-representation";
            case TYPEATTR: return "http://hl7.org/fhir/property-representation";
            case CDATEXT: return "http://hl7.org/fhir/property-representation";
            case XHTML: return "http://hl7.org/fhir/property-representation";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case XMLATTR: return "In XML, this property is represented as an attribute not an element.";
            case XMLTEXT: return "This element is represented using the XML text attribute (primitives only)";
            case TYPEATTR: return "The type of this element is indicated using xsi:type";
            case CDATEXT: return "Use CDA narrative instead of XHTML";
            case XHTML: return "The property is represented using XHTML";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case XMLATTR: return "XML Attribute";
            case XMLTEXT: return "XML Text";
            case TYPEATTR: return "Type Attribute";
            case CDATEXT: return "CDA Text Format";
            case XHTML: return "XHTML";
            default: return "?";
          }
        }
    }

  public static class PropertyRepresentationEnumFactory implements EnumFactory<PropertyRepresentation> {
    public PropertyRepresentation fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("xmlAttr".equals(codeString))
          return PropertyRepresentation.XMLATTR;
        if ("xmlText".equals(codeString))
          return PropertyRepresentation.XMLTEXT;
        if ("typeAttr".equals(codeString))
          return PropertyRepresentation.TYPEATTR;
        if ("cdaText".equals(codeString))
          return PropertyRepresentation.CDATEXT;
        if ("xhtml".equals(codeString))
          return PropertyRepresentation.XHTML;
        throw new IllegalArgumentException("Unknown PropertyRepresentation code '"+codeString+"'");
        }
        public Enumeration<PropertyRepresentation> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<PropertyRepresentation>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("xmlAttr".equals(codeString))
          return new Enumeration<PropertyRepresentation>(this, PropertyRepresentation.XMLATTR);
        if ("xmlText".equals(codeString))
          return new Enumeration<PropertyRepresentation>(this, PropertyRepresentation.XMLTEXT);
        if ("typeAttr".equals(codeString))
          return new Enumeration<PropertyRepresentation>(this, PropertyRepresentation.TYPEATTR);
        if ("cdaText".equals(codeString))
          return new Enumeration<PropertyRepresentation>(this, PropertyRepresentation.CDATEXT);
        if ("xhtml".equals(codeString))
          return new Enumeration<PropertyRepresentation>(this, PropertyRepresentation.XHTML);
        throw new FHIRException("Unknown PropertyRepresentation code '"+codeString+"'");
        }
    public String toCode(PropertyRepresentation code) {
      if (code == PropertyRepresentation.XMLATTR)
        return "xmlAttr";
      if (code == PropertyRepresentation.XMLTEXT)
        return "xmlText";
      if (code == PropertyRepresentation.TYPEATTR)
        return "typeAttr";
      if (code == PropertyRepresentation.CDATEXT)
        return "cdaText";
      if (code == PropertyRepresentation.XHTML)
        return "xhtml";
      return "?";
      }
    public String toSystem(PropertyRepresentation code) {
      return code.getSystem();
      }
    }

    public enum DiscriminatorType {
        /**
         * The slices have different values in the nominated element
         */
        VALUE, 
        /**
         * The slices are differentiated by the presence or absence of the nominated element
         */
        EXISTS, 
        /**
         * The slices have different values in the nominated element, as determined by testing them against the applicable ElementDefinition.pattern[x]
         */
        PATTERN, 
        /**
         * The slices are differentiated by type of the nominated element to a specifed profile
         */
        TYPE, 
        /**
         * The slices are differentiated by conformance of the nominated element to a specifed profile
         */
        PROFILE, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static DiscriminatorType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("value".equals(codeString))
          return VALUE;
        if ("exists".equals(codeString))
          return EXISTS;
        if ("pattern".equals(codeString))
          return PATTERN;
        if ("type".equals(codeString))
          return TYPE;
        if ("profile".equals(codeString))
          return PROFILE;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown DiscriminatorType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case VALUE: return "value";
            case EXISTS: return "exists";
            case PATTERN: return "pattern";
            case TYPE: return "type";
            case PROFILE: return "profile";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case VALUE: return "http://hl7.org/fhir/discriminator-type";
            case EXISTS: return "http://hl7.org/fhir/discriminator-type";
            case PATTERN: return "http://hl7.org/fhir/discriminator-type";
            case TYPE: return "http://hl7.org/fhir/discriminator-type";
            case PROFILE: return "http://hl7.org/fhir/discriminator-type";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case VALUE: return "The slices have different values in the nominated element";
            case EXISTS: return "The slices are differentiated by the presence or absence of the nominated element";
            case PATTERN: return "The slices have different values in the nominated element, as determined by testing them against the applicable ElementDefinition.pattern[x]";
            case TYPE: return "The slices are differentiated by type of the nominated element to a specifed profile";
            case PROFILE: return "The slices are differentiated by conformance of the nominated element to a specifed profile";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case VALUE: return "Value";
            case EXISTS: return "Exists";
            case PATTERN: return "Pattern";
            case TYPE: return "Type";
            case PROFILE: return "Profile";
            default: return "?";
          }
        }
    }

  public static class DiscriminatorTypeEnumFactory implements EnumFactory<DiscriminatorType> {
    public DiscriminatorType fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("value".equals(codeString))
          return DiscriminatorType.VALUE;
        if ("exists".equals(codeString))
          return DiscriminatorType.EXISTS;
        if ("pattern".equals(codeString))
          return DiscriminatorType.PATTERN;
        if ("type".equals(codeString))
          return DiscriminatorType.TYPE;
        if ("profile".equals(codeString))
          return DiscriminatorType.PROFILE;
        throw new IllegalArgumentException("Unknown DiscriminatorType code '"+codeString+"'");
        }
        public Enumeration<DiscriminatorType> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<DiscriminatorType>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("value".equals(codeString))
          return new Enumeration<DiscriminatorType>(this, DiscriminatorType.VALUE);
        if ("exists".equals(codeString))
          return new Enumeration<DiscriminatorType>(this, DiscriminatorType.EXISTS);
        if ("pattern".equals(codeString))
          return new Enumeration<DiscriminatorType>(this, DiscriminatorType.PATTERN);
        if ("type".equals(codeString))
          return new Enumeration<DiscriminatorType>(this, DiscriminatorType.TYPE);
        if ("profile".equals(codeString))
          return new Enumeration<DiscriminatorType>(this, DiscriminatorType.PROFILE);
        throw new FHIRException("Unknown DiscriminatorType code '"+codeString+"'");
        }
    public String toCode(DiscriminatorType code) {
      if (code == DiscriminatorType.VALUE)
        return "value";
      if (code == DiscriminatorType.EXISTS)
        return "exists";
      if (code == DiscriminatorType.PATTERN)
        return "pattern";
      if (code == DiscriminatorType.TYPE)
        return "type";
      if (code == DiscriminatorType.PROFILE)
        return "profile";
      return "?";
      }
    public String toSystem(DiscriminatorType code) {
      return code.getSystem();
      }
    }

    public enum SlicingRules {
        /**
         * No additional content is allowed other than that described by the slices in this profile.
         */
        CLOSED, 
        /**
         * Additional content is allowed anywhere in the list.
         */
        OPEN, 
        /**
         * Additional content is allowed, but only at the end of the list. Note that using this requires that the slices be ordered, which makes it hard to share uses. This should only be done where absolutely required.
         */
        OPENATEND, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static SlicingRules fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("closed".equals(codeString))
          return CLOSED;
        if ("open".equals(codeString))
          return OPEN;
        if ("openAtEnd".equals(codeString))
          return OPENATEND;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown SlicingRules code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case CLOSED: return "closed";
            case OPEN: return "open";
            case OPENATEND: return "openAtEnd";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case CLOSED: return "http://hl7.org/fhir/resource-slicing-rules";
            case OPEN: return "http://hl7.org/fhir/resource-slicing-rules";
            case OPENATEND: return "http://hl7.org/fhir/resource-slicing-rules";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case CLOSED: return "No additional content is allowed other than that described by the slices in this profile.";
            case OPEN: return "Additional content is allowed anywhere in the list.";
            case OPENATEND: return "Additional content is allowed, but only at the end of the list. Note that using this requires that the slices be ordered, which makes it hard to share uses. This should only be done where absolutely required.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case CLOSED: return "Closed";
            case OPEN: return "Open";
            case OPENATEND: return "Open at End";
            default: return "?";
          }
        }
    }

  public static class SlicingRulesEnumFactory implements EnumFactory<SlicingRules> {
    public SlicingRules fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("closed".equals(codeString))
          return SlicingRules.CLOSED;
        if ("open".equals(codeString))
          return SlicingRules.OPEN;
        if ("openAtEnd".equals(codeString))
          return SlicingRules.OPENATEND;
        throw new IllegalArgumentException("Unknown SlicingRules code '"+codeString+"'");
        }
        public Enumeration<SlicingRules> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<SlicingRules>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("closed".equals(codeString))
          return new Enumeration<SlicingRules>(this, SlicingRules.CLOSED);
        if ("open".equals(codeString))
          return new Enumeration<SlicingRules>(this, SlicingRules.OPEN);
        if ("openAtEnd".equals(codeString))
          return new Enumeration<SlicingRules>(this, SlicingRules.OPENATEND);
        throw new FHIRException("Unknown SlicingRules code '"+codeString+"'");
        }
    public String toCode(SlicingRules code) {
      if (code == SlicingRules.CLOSED)
        return "closed";
      if (code == SlicingRules.OPEN)
        return "open";
      if (code == SlicingRules.OPENATEND)
        return "openAtEnd";
      return "?";
      }
    public String toSystem(SlicingRules code) {
      return code.getSystem();
      }
    }

    public enum AggregationMode {
        /**
         * The reference is a local reference to a contained resource.
         */
        CONTAINED, 
        /**
         * The reference to a resource that has to be resolved externally to the resource that includes the reference.
         */
        REFERENCED, 
        /**
         * The resource the reference points to will be found in the same bundle as the resource that includes the reference.
         */
        BUNDLED, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static AggregationMode fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("contained".equals(codeString))
          return CONTAINED;
        if ("referenced".equals(codeString))
          return REFERENCED;
        if ("bundled".equals(codeString))
          return BUNDLED;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown AggregationMode code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case CONTAINED: return "contained";
            case REFERENCED: return "referenced";
            case BUNDLED: return "bundled";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case CONTAINED: return "http://hl7.org/fhir/resource-aggregation-mode";
            case REFERENCED: return "http://hl7.org/fhir/resource-aggregation-mode";
            case BUNDLED: return "http://hl7.org/fhir/resource-aggregation-mode";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case CONTAINED: return "The reference is a local reference to a contained resource.";
            case REFERENCED: return "The reference to a resource that has to be resolved externally to the resource that includes the reference.";
            case BUNDLED: return "The resource the reference points to will be found in the same bundle as the resource that includes the reference.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case CONTAINED: return "Contained";
            case REFERENCED: return "Referenced";
            case BUNDLED: return "Bundled";
            default: return "?";
          }
        }
    }

  public static class AggregationModeEnumFactory implements EnumFactory<AggregationMode> {
    public AggregationMode fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("contained".equals(codeString))
          return AggregationMode.CONTAINED;
        if ("referenced".equals(codeString))
          return AggregationMode.REFERENCED;
        if ("bundled".equals(codeString))
          return AggregationMode.BUNDLED;
        throw new IllegalArgumentException("Unknown AggregationMode code '"+codeString+"'");
        }
        public Enumeration<AggregationMode> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<AggregationMode>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("contained".equals(codeString))
          return new Enumeration<AggregationMode>(this, AggregationMode.CONTAINED);
        if ("referenced".equals(codeString))
          return new Enumeration<AggregationMode>(this, AggregationMode.REFERENCED);
        if ("bundled".equals(codeString))
          return new Enumeration<AggregationMode>(this, AggregationMode.BUNDLED);
        throw new FHIRException("Unknown AggregationMode code '"+codeString+"'");
        }
    public String toCode(AggregationMode code) {
      if (code == AggregationMode.CONTAINED)
        return "contained";
      if (code == AggregationMode.REFERENCED)
        return "referenced";
      if (code == AggregationMode.BUNDLED)
        return "bundled";
      return "?";
      }
    public String toSystem(AggregationMode code) {
      return code.getSystem();
      }
    }

    public enum ReferenceVersionRules {
        /**
         * The reference may be either version independent or version specific
         */
        EITHER, 
        /**
         * The reference must be version independent
         */
        INDEPENDENT, 
        /**
         * The reference must be version specific
         */
        SPECIFIC, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static ReferenceVersionRules fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("either".equals(codeString))
          return EITHER;
        if ("independent".equals(codeString))
          return INDEPENDENT;
        if ("specific".equals(codeString))
          return SPECIFIC;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown ReferenceVersionRules code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case EITHER: return "either";
            case INDEPENDENT: return "independent";
            case SPECIFIC: return "specific";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case EITHER: return "http://hl7.org/fhir/reference-version-rules";
            case INDEPENDENT: return "http://hl7.org/fhir/reference-version-rules";
            case SPECIFIC: return "http://hl7.org/fhir/reference-version-rules";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case EITHER: return "The reference may be either version independent or version specific";
            case INDEPENDENT: return "The reference must be version independent";
            case SPECIFIC: return "The reference must be version specific";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case EITHER: return "Either Specific or independent";
            case INDEPENDENT: return "Version independent";
            case SPECIFIC: return "Version Specific";
            default: return "?";
          }
        }
    }

  public static class ReferenceVersionRulesEnumFactory implements EnumFactory<ReferenceVersionRules> {
    public ReferenceVersionRules fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("either".equals(codeString))
          return ReferenceVersionRules.EITHER;
        if ("independent".equals(codeString))
          return ReferenceVersionRules.INDEPENDENT;
        if ("specific".equals(codeString))
          return ReferenceVersionRules.SPECIFIC;
        throw new IllegalArgumentException("Unknown ReferenceVersionRules code '"+codeString+"'");
        }
        public Enumeration<ReferenceVersionRules> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<ReferenceVersionRules>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("either".equals(codeString))
          return new Enumeration<ReferenceVersionRules>(this, ReferenceVersionRules.EITHER);
        if ("independent".equals(codeString))
          return new Enumeration<ReferenceVersionRules>(this, ReferenceVersionRules.INDEPENDENT);
        if ("specific".equals(codeString))
          return new Enumeration<ReferenceVersionRules>(this, ReferenceVersionRules.SPECIFIC);
        throw new FHIRException("Unknown ReferenceVersionRules code '"+codeString+"'");
        }
    public String toCode(ReferenceVersionRules code) {
      if (code == ReferenceVersionRules.EITHER)
        return "either";
      if (code == ReferenceVersionRules.INDEPENDENT)
        return "independent";
      if (code == ReferenceVersionRules.SPECIFIC)
        return "specific";
      return "?";
      }
    public String toSystem(ReferenceVersionRules code) {
      return code.getSystem();
      }
    }

    public enum ConstraintSeverity {
        /**
         * If the constraint is violated, the resource is not conformant.
         */
        ERROR, 
        /**
         * If the constraint is violated, the resource is conformant, but it is not necessarily following best practice.
         */
        WARNING, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static ConstraintSeverity fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("error".equals(codeString))
          return ERROR;
        if ("warning".equals(codeString))
          return WARNING;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown ConstraintSeverity code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case ERROR: return "error";
            case WARNING: return "warning";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case ERROR: return "http://hl7.org/fhir/constraint-severity";
            case WARNING: return "http://hl7.org/fhir/constraint-severity";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case ERROR: return "If the constraint is violated, the resource is not conformant.";
            case WARNING: return "If the constraint is violated, the resource is conformant, but it is not necessarily following best practice.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case ERROR: return "Error";
            case WARNING: return "Warning";
            default: return "?";
          }
        }
    }

  public static class ConstraintSeverityEnumFactory implements EnumFactory<ConstraintSeverity> {
    public ConstraintSeverity fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("error".equals(codeString))
          return ConstraintSeverity.ERROR;
        if ("warning".equals(codeString))
          return ConstraintSeverity.WARNING;
        throw new IllegalArgumentException("Unknown ConstraintSeverity code '"+codeString+"'");
        }
        public Enumeration<ConstraintSeverity> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<ConstraintSeverity>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("error".equals(codeString))
          return new Enumeration<ConstraintSeverity>(this, ConstraintSeverity.ERROR);
        if ("warning".equals(codeString))
          return new Enumeration<ConstraintSeverity>(this, ConstraintSeverity.WARNING);
        throw new FHIRException("Unknown ConstraintSeverity code '"+codeString+"'");
        }
    public String toCode(ConstraintSeverity code) {
      if (code == ConstraintSeverity.ERROR)
        return "error";
      if (code == ConstraintSeverity.WARNING)
        return "warning";
      return "?";
      }
    public String toSystem(ConstraintSeverity code) {
      return code.getSystem();
      }
    }

    @Block()
    public static class ElementDefinitionSlicingComponent extends Element implements IBaseDatatypeElement {
        /**
         * Designates which child elements are used to discriminate between the slices when processing an instance. If one or more discriminators are provided, the value of the child elements in the instance data SHALL completely distinguish which slice the element in the resource matches based on the allowed values for those elements in each of the slices.
         */
        @Child(name = "discriminator", type = {}, order=1, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Element values that are used to distinguish the slices", formalDefinition="Designates which child elements are used to discriminate between the slices when processing an instance. If one or more discriminators are provided, the value of the child elements in the instance data SHALL completely distinguish which slice the element in the resource matches based on the allowed values for those elements in each of the slices." )
        protected List<ElementDefinitionSlicingDiscriminatorComponent> discriminator;

        /**
         * A human-readable text description of how the slicing works. If there is no discriminator, this is required to be present to provide whatever information is possible about how the slices can be differentiated.
         */
        @Child(name = "description", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Text description of how slicing works (or not)", formalDefinition="A human-readable text description of how the slicing works. If there is no discriminator, this is required to be present to provide whatever information is possible about how the slices can be differentiated." )
        protected StringType description;

        /**
         * If the matching elements have to occur in the same order as defined in the profile.
         */
        @Child(name = "ordered", type = {BooleanType.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="If elements must be in same order as slices", formalDefinition="If the matching elements have to occur in the same order as defined in the profile." )
        protected BooleanType ordered;

        /**
         * Whether additional slices are allowed or not. When the slices are ordered, profile authors can also say that additional slices are only allowed at the end.
         */
        @Child(name = "rules", type = {CodeType.class}, order=4, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="closed | open | openAtEnd", formalDefinition="Whether additional slices are allowed or not. When the slices are ordered, profile authors can also say that additional slices are only allowed at the end." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/resource-slicing-rules")
        protected Enumeration<SlicingRules> rules;

        private static final long serialVersionUID = -311635839L;

    /**
     * Constructor
     */
      public ElementDefinitionSlicingComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ElementDefinitionSlicingComponent(Enumeration<SlicingRules> rules) {
        super();
        this.rules = rules;
      }

        /**
         * @return {@link #discriminator} (Designates which child elements are used to discriminate between the slices when processing an instance. If one or more discriminators are provided, the value of the child elements in the instance data SHALL completely distinguish which slice the element in the resource matches based on the allowed values for those elements in each of the slices.)
         */
        public List<ElementDefinitionSlicingDiscriminatorComponent> getDiscriminator() { 
          if (this.discriminator == null)
            this.discriminator = new ArrayList<ElementDefinitionSlicingDiscriminatorComponent>();
          return this.discriminator;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public ElementDefinitionSlicingComponent setDiscriminator(List<ElementDefinitionSlicingDiscriminatorComponent> theDiscriminator) { 
          this.discriminator = theDiscriminator;
          return this;
        }

        public boolean hasDiscriminator() { 
          if (this.discriminator == null)
            return false;
          for (ElementDefinitionSlicingDiscriminatorComponent item : this.discriminator)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public ElementDefinitionSlicingDiscriminatorComponent addDiscriminator() { //3
          ElementDefinitionSlicingDiscriminatorComponent t = new ElementDefinitionSlicingDiscriminatorComponent();
          if (this.discriminator == null)
            this.discriminator = new ArrayList<ElementDefinitionSlicingDiscriminatorComponent>();
          this.discriminator.add(t);
          return t;
        }

        public ElementDefinitionSlicingComponent addDiscriminator(ElementDefinitionSlicingDiscriminatorComponent t) { //3
          if (t == null)
            return this;
          if (this.discriminator == null)
            this.discriminator = new ArrayList<ElementDefinitionSlicingDiscriminatorComponent>();
          this.discriminator.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #discriminator}, creating it if it does not already exist
         */
        public ElementDefinitionSlicingDiscriminatorComponent getDiscriminatorFirstRep() { 
          if (getDiscriminator().isEmpty()) {
            addDiscriminator();
          }
          return getDiscriminator().get(0);
        }

        /**
         * @return {@link #description} (A human-readable text description of how the slicing works. If there is no discriminator, this is required to be present to provide whatever information is possible about how the slices can be differentiated.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public StringType getDescriptionElement() { 
          if (this.description == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ElementDefinitionSlicingComponent.description");
            else if (Configuration.doAutoCreate())
              this.description = new StringType(); // bb
          return this.description;
        }

        public boolean hasDescriptionElement() { 
          return this.description != null && !this.description.isEmpty();
        }

        public boolean hasDescription() { 
          return this.description != null && !this.description.isEmpty();
        }

        /**
         * @param value {@link #description} (A human-readable text description of how the slicing works. If there is no discriminator, this is required to be present to provide whatever information is possible about how the slices can be differentiated.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public ElementDefinitionSlicingComponent setDescriptionElement(StringType value) { 
          this.description = value;
          return this;
        }

        /**
         * @return A human-readable text description of how the slicing works. If there is no discriminator, this is required to be present to provide whatever information is possible about how the slices can be differentiated.
         */
        public String getDescription() { 
          return this.description == null ? null : this.description.getValue();
        }

        /**
         * @param value A human-readable text description of how the slicing works. If there is no discriminator, this is required to be present to provide whatever information is possible about how the slices can be differentiated.
         */
        public ElementDefinitionSlicingComponent setDescription(String value) { 
          if (Utilities.noString(value))
            this.description = null;
          else {
            if (this.description == null)
              this.description = new StringType();
            this.description.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #ordered} (If the matching elements have to occur in the same order as defined in the profile.). This is the underlying object with id, value and extensions. The accessor "getOrdered" gives direct access to the value
         */
        public BooleanType getOrderedElement() { 
          if (this.ordered == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ElementDefinitionSlicingComponent.ordered");
            else if (Configuration.doAutoCreate())
              this.ordered = new BooleanType(); // bb
          return this.ordered;
        }

        public boolean hasOrderedElement() { 
          return this.ordered != null && !this.ordered.isEmpty();
        }

        public boolean hasOrdered() { 
          return this.ordered != null && !this.ordered.isEmpty();
        }

        /**
         * @param value {@link #ordered} (If the matching elements have to occur in the same order as defined in the profile.). This is the underlying object with id, value and extensions. The accessor "getOrdered" gives direct access to the value
         */
        public ElementDefinitionSlicingComponent setOrderedElement(BooleanType value) { 
          this.ordered = value;
          return this;
        }

        /**
         * @return If the matching elements have to occur in the same order as defined in the profile.
         */
        public boolean getOrdered() { 
          return this.ordered == null || this.ordered.isEmpty() ? false : this.ordered.getValue();
        }

        /**
         * @param value If the matching elements have to occur in the same order as defined in the profile.
         */
        public ElementDefinitionSlicingComponent setOrdered(boolean value) { 
            if (this.ordered == null)
              this.ordered = new BooleanType();
            this.ordered.setValue(value);
          return this;
        }

        /**
         * @return {@link #rules} (Whether additional slices are allowed or not. When the slices are ordered, profile authors can also say that additional slices are only allowed at the end.). This is the underlying object with id, value and extensions. The accessor "getRules" gives direct access to the value
         */
        public Enumeration<SlicingRules> getRulesElement() { 
          if (this.rules == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ElementDefinitionSlicingComponent.rules");
            else if (Configuration.doAutoCreate())
              this.rules = new Enumeration<SlicingRules>(new SlicingRulesEnumFactory()); // bb
          return this.rules;
        }

        public boolean hasRulesElement() { 
          return this.rules != null && !this.rules.isEmpty();
        }

        public boolean hasRules() { 
          return this.rules != null && !this.rules.isEmpty();
        }

        /**
         * @param value {@link #rules} (Whether additional slices are allowed or not. When the slices are ordered, profile authors can also say that additional slices are only allowed at the end.). This is the underlying object with id, value and extensions. The accessor "getRules" gives direct access to the value
         */
        public ElementDefinitionSlicingComponent setRulesElement(Enumeration<SlicingRules> value) { 
          this.rules = value;
          return this;
        }

        /**
         * @return Whether additional slices are allowed or not. When the slices are ordered, profile authors can also say that additional slices are only allowed at the end.
         */
        public SlicingRules getRules() { 
          return this.rules == null ? null : this.rules.getValue();
        }

        /**
         * @param value Whether additional slices are allowed or not. When the slices are ordered, profile authors can also say that additional slices are only allowed at the end.
         */
        public ElementDefinitionSlicingComponent setRules(SlicingRules value) { 
            if (this.rules == null)
              this.rules = new Enumeration<SlicingRules>(new SlicingRulesEnumFactory());
            this.rules.setValue(value);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("discriminator", "", "Designates which child elements are used to discriminate between the slices when processing an instance. If one or more discriminators are provided, the value of the child elements in the instance data SHALL completely distinguish which slice the element in the resource matches based on the allowed values for those elements in each of the slices.", 0, java.lang.Integer.MAX_VALUE, discriminator));
          childrenList.add(new Property("description", "string", "A human-readable text description of how the slicing works. If there is no discriminator, this is required to be present to provide whatever information is possible about how the slices can be differentiated.", 0, java.lang.Integer.MAX_VALUE, description));
          childrenList.add(new Property("ordered", "boolean", "If the matching elements have to occur in the same order as defined in the profile.", 0, java.lang.Integer.MAX_VALUE, ordered));
          childrenList.add(new Property("rules", "code", "Whether additional slices are allowed or not. When the slices are ordered, profile authors can also say that additional slices are only allowed at the end.", 0, java.lang.Integer.MAX_VALUE, rules));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1888270692: /*discriminator*/ return this.discriminator == null ? new Base[0] : this.discriminator.toArray(new Base[this.discriminator.size()]); // ElementDefinitionSlicingDiscriminatorComponent
        case -1724546052: /*description*/ return this.description == null ? new Base[0] : new Base[] {this.description}; // StringType
        case -1207109523: /*ordered*/ return this.ordered == null ? new Base[0] : new Base[] {this.ordered}; // BooleanType
        case 108873975: /*rules*/ return this.rules == null ? new Base[0] : new Base[] {this.rules}; // Enumeration<SlicingRules>
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1888270692: // discriminator
          this.getDiscriminator().add((ElementDefinitionSlicingDiscriminatorComponent) value); // ElementDefinitionSlicingDiscriminatorComponent
          return value;
        case -1724546052: // description
          this.description = castToString(value); // StringType
          return value;
        case -1207109523: // ordered
          this.ordered = castToBoolean(value); // BooleanType
          return value;
        case 108873975: // rules
          value = new SlicingRulesEnumFactory().fromType(castToCode(value));
          this.rules = (Enumeration) value; // Enumeration<SlicingRules>
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("discriminator")) {
          this.getDiscriminator().add((ElementDefinitionSlicingDiscriminatorComponent) value);
        } else if (name.equals("description")) {
          this.description = castToString(value); // StringType
        } else if (name.equals("ordered")) {
          this.ordered = castToBoolean(value); // BooleanType
        } else if (name.equals("rules")) {
          value = new SlicingRulesEnumFactory().fromType(castToCode(value));
          this.rules = (Enumeration) value; // Enumeration<SlicingRules>
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1888270692:  return addDiscriminator(); 
        case -1724546052:  return getDescriptionElement();
        case -1207109523:  return getOrderedElement();
        case 108873975:  return getRulesElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1888270692: /*discriminator*/ return new String[] {};
        case -1724546052: /*description*/ return new String[] {"string"};
        case -1207109523: /*ordered*/ return new String[] {"boolean"};
        case 108873975: /*rules*/ return new String[] {"code"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("discriminator")) {
          return addDiscriminator();
        }
        else if (name.equals("description")) {
          throw new FHIRException("Cannot call addChild on a primitive type ElementDefinition.description");
        }
        else if (name.equals("ordered")) {
          throw new FHIRException("Cannot call addChild on a primitive type ElementDefinition.ordered");
        }
        else if (name.equals("rules")) {
          throw new FHIRException("Cannot call addChild on a primitive type ElementDefinition.rules");
        }
        else
          return super.addChild(name);
      }

      public ElementDefinitionSlicingComponent copy() {
        ElementDefinitionSlicingComponent dst = new ElementDefinitionSlicingComponent();
        copyValues(dst);
        if (discriminator != null) {
          dst.discriminator = new ArrayList<ElementDefinitionSlicingDiscriminatorComponent>();
          for (ElementDefinitionSlicingDiscriminatorComponent i : discriminator)
            dst.discriminator.add(i.copy());
        };
        dst.description = description == null ? null : description.copy();
        dst.ordered = ordered == null ? null : ordered.copy();
        dst.rules = rules == null ? null : rules.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ElementDefinitionSlicingComponent))
          return false;
        ElementDefinitionSlicingComponent o = (ElementDefinitionSlicingComponent) other;
        return compareDeep(discriminator, o.discriminator, true) && compareDeep(description, o.description, true)
           && compareDeep(ordered, o.ordered, true) && compareDeep(rules, o.rules, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ElementDefinitionSlicingComponent))
          return false;
        ElementDefinitionSlicingComponent o = (ElementDefinitionSlicingComponent) other;
        return compareValues(description, o.description, true) && compareValues(ordered, o.ordered, true) && compareValues(rules, o.rules, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(discriminator, description
          , ordered, rules);
      }

  public String fhirType() {
    return "ElementDefinition.slicing";

  }

  }

    @Block()
    public static class ElementDefinitionSlicingDiscriminatorComponent extends Element implements IBaseDatatypeElement {
        /**
         * How the element value is interpreted when discrimination is evaluated.
         */
        @Child(name = "type", type = {CodeType.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="value | exists | pattern | type | profile", formalDefinition="How the element value is interpreted when discrimination is evaluated." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/discriminator-type")
        protected Enumeration<DiscriminatorType> type;

        /**
         * A FHIRPath expression, using a restricted subset of FHIRPath, that is used to identify the element on which discrimination is based.
         */
        @Child(name = "path", type = {StringType.class}, order=2, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Path to element value", formalDefinition="A FHIRPath expression, using a restricted subset of FHIRPath, that is used to identify the element on which discrimination is based." )
        protected StringType path;

        private static final long serialVersionUID = 1151159293L;

    /**
     * Constructor
     */
      public ElementDefinitionSlicingDiscriminatorComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ElementDefinitionSlicingDiscriminatorComponent(Enumeration<DiscriminatorType> type, StringType path) {
        super();
        this.type = type;
        this.path = path;
      }

        /**
         * @return {@link #type} (How the element value is interpreted when discrimination is evaluated.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public Enumeration<DiscriminatorType> getTypeElement() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ElementDefinitionSlicingDiscriminatorComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new Enumeration<DiscriminatorType>(new DiscriminatorTypeEnumFactory()); // bb
          return this.type;
        }

        public boolean hasTypeElement() { 
          return this.type != null && !this.type.isEmpty();
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (How the element value is interpreted when discrimination is evaluated.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public ElementDefinitionSlicingDiscriminatorComponent setTypeElement(Enumeration<DiscriminatorType> value) { 
          this.type = value;
          return this;
        }

        /**
         * @return How the element value is interpreted when discrimination is evaluated.
         */
        public DiscriminatorType getType() { 
          return this.type == null ? null : this.type.getValue();
        }

        /**
         * @param value How the element value is interpreted when discrimination is evaluated.
         */
        public ElementDefinitionSlicingDiscriminatorComponent setType(DiscriminatorType value) { 
            if (this.type == null)
              this.type = new Enumeration<DiscriminatorType>(new DiscriminatorTypeEnumFactory());
            this.type.setValue(value);
          return this;
        }

        /**
         * @return {@link #path} (A FHIRPath expression, using a restricted subset of FHIRPath, that is used to identify the element on which discrimination is based.). This is the underlying object with id, value and extensions. The accessor "getPath" gives direct access to the value
         */
        public StringType getPathElement() { 
          if (this.path == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ElementDefinitionSlicingDiscriminatorComponent.path");
            else if (Configuration.doAutoCreate())
              this.path = new StringType(); // bb
          return this.path;
        }

        public boolean hasPathElement() { 
          return this.path != null && !this.path.isEmpty();
        }

        public boolean hasPath() { 
          return this.path != null && !this.path.isEmpty();
        }

        /**
         * @param value {@link #path} (A FHIRPath expression, using a restricted subset of FHIRPath, that is used to identify the element on which discrimination is based.). This is the underlying object with id, value and extensions. The accessor "getPath" gives direct access to the value
         */
        public ElementDefinitionSlicingDiscriminatorComponent setPathElement(StringType value) { 
          this.path = value;
          return this;
        }

        /**
         * @return A FHIRPath expression, using a restricted subset of FHIRPath, that is used to identify the element on which discrimination is based.
         */
        public String getPath() { 
          return this.path == null ? null : this.path.getValue();
        }

        /**
         * @param value A FHIRPath expression, using a restricted subset of FHIRPath, that is used to identify the element on which discrimination is based.
         */
        public ElementDefinitionSlicingDiscriminatorComponent setPath(String value) { 
            if (this.path == null)
              this.path = new StringType();
            this.path.setValue(value);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("type", "code", "How the element value is interpreted when discrimination is evaluated.", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("path", "string", "A FHIRPath expression, using a restricted subset of FHIRPath, that is used to identify the element on which discrimination is based.", 0, java.lang.Integer.MAX_VALUE, path));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // Enumeration<DiscriminatorType>
        case 3433509: /*path*/ return this.path == null ? new Base[0] : new Base[] {this.path}; // StringType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3575610: // type
          value = new DiscriminatorTypeEnumFactory().fromType(castToCode(value));
          this.type = (Enumeration) value; // Enumeration<DiscriminatorType>
          return value;
        case 3433509: // path
          this.path = castToString(value); // StringType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("type")) {
          value = new DiscriminatorTypeEnumFactory().fromType(castToCode(value));
          this.type = (Enumeration) value; // Enumeration<DiscriminatorType>
        } else if (name.equals("path")) {
          this.path = castToString(value); // StringType
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610:  return getTypeElement();
        case 3433509:  return getPathElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return new String[] {"code"};
        case 3433509: /*path*/ return new String[] {"string"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("type")) {
          throw new FHIRException("Cannot call addChild on a primitive type ElementDefinition.type");
        }
        else if (name.equals("path")) {
          throw new FHIRException("Cannot call addChild on a primitive type ElementDefinition.path");
        }
        else
          return super.addChild(name);
      }

      public ElementDefinitionSlicingDiscriminatorComponent copy() {
        ElementDefinitionSlicingDiscriminatorComponent dst = new ElementDefinitionSlicingDiscriminatorComponent();
        copyValues(dst);
        dst.type = type == null ? null : type.copy();
        dst.path = path == null ? null : path.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ElementDefinitionSlicingDiscriminatorComponent))
          return false;
        ElementDefinitionSlicingDiscriminatorComponent o = (ElementDefinitionSlicingDiscriminatorComponent) other;
        return compareDeep(type, o.type, true) && compareDeep(path, o.path, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ElementDefinitionSlicingDiscriminatorComponent))
          return false;
        ElementDefinitionSlicingDiscriminatorComponent o = (ElementDefinitionSlicingDiscriminatorComponent) other;
        return compareValues(type, o.type, true) && compareValues(path, o.path, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(type, path);
      }

  public String fhirType() {
    return "ElementDefinition.slicing.discriminator";

  }

  }

    @Block()
    public static class ElementDefinitionBaseComponent extends Element implements IBaseDatatypeElement {
        /**
         * The Path that identifies the base element - this matches the ElementDefinition.path for that element. Across FHIR, there is only one base definition of any element - that is, an element definition on a [[[StructureDefinition]]] without a StructureDefinition.base.
         */
        @Child(name = "path", type = {StringType.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Path that identifies the base element", formalDefinition="The Path that identifies the base element - this matches the ElementDefinition.path for that element. Across FHIR, there is only one base definition of any element - that is, an element definition on a [[[StructureDefinition]]] without a StructureDefinition.base." )
        protected StringType path;

        /**
         * Minimum cardinality of the base element identified by the path.
         */
        @Child(name = "min", type = {UnsignedIntType.class}, order=2, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Min cardinality of the base element", formalDefinition="Minimum cardinality of the base element identified by the path." )
        protected UnsignedIntType min;

        /**
         * Maximum cardinality of the base element identified by the path.
         */
        @Child(name = "max", type = {StringType.class}, order=3, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Max cardinality of the base element", formalDefinition="Maximum cardinality of the base element identified by the path." )
        protected StringType max;

        private static final long serialVersionUID = -1412704221L;

    /**
     * Constructor
     */
      public ElementDefinitionBaseComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ElementDefinitionBaseComponent(StringType path, UnsignedIntType min, StringType max) {
        super();
        this.path = path;
        this.min = min;
        this.max = max;
      }

        /**
         * @return {@link #path} (The Path that identifies the base element - this matches the ElementDefinition.path for that element. Across FHIR, there is only one base definition of any element - that is, an element definition on a [[[StructureDefinition]]] without a StructureDefinition.base.). This is the underlying object with id, value and extensions. The accessor "getPath" gives direct access to the value
         */
        public StringType getPathElement() { 
          if (this.path == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ElementDefinitionBaseComponent.path");
            else if (Configuration.doAutoCreate())
              this.path = new StringType(); // bb
          return this.path;
        }

        public boolean hasPathElement() { 
          return this.path != null && !this.path.isEmpty();
        }

        public boolean hasPath() { 
          return this.path != null && !this.path.isEmpty();
        }

        /**
         * @param value {@link #path} (The Path that identifies the base element - this matches the ElementDefinition.path for that element. Across FHIR, there is only one base definition of any element - that is, an element definition on a [[[StructureDefinition]]] without a StructureDefinition.base.). This is the underlying object with id, value and extensions. The accessor "getPath" gives direct access to the value
         */
        public ElementDefinitionBaseComponent setPathElement(StringType value) { 
          this.path = value;
          return this;
        }

        /**
         * @return The Path that identifies the base element - this matches the ElementDefinition.path for that element. Across FHIR, there is only one base definition of any element - that is, an element definition on a [[[StructureDefinition]]] without a StructureDefinition.base.
         */
        public String getPath() { 
          return this.path == null ? null : this.path.getValue();
        }

        /**
         * @param value The Path that identifies the base element - this matches the ElementDefinition.path for that element. Across FHIR, there is only one base definition of any element - that is, an element definition on a [[[StructureDefinition]]] without a StructureDefinition.base.
         */
        public ElementDefinitionBaseComponent setPath(String value) { 
            if (this.path == null)
              this.path = new StringType();
            this.path.setValue(value);
          return this;
        }

        /**
         * @return {@link #min} (Minimum cardinality of the base element identified by the path.). This is the underlying object with id, value and extensions. The accessor "getMin" gives direct access to the value
         */
        public UnsignedIntType getMinElement() { 
          if (this.min == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ElementDefinitionBaseComponent.min");
            else if (Configuration.doAutoCreate())
              this.min = new UnsignedIntType(); // bb
          return this.min;
        }

        public boolean hasMinElement() { 
          return this.min != null && !this.min.isEmpty();
        }

        public boolean hasMin() { 
          return this.min != null && !this.min.isEmpty();
        }

        /**
         * @param value {@link #min} (Minimum cardinality of the base element identified by the path.). This is the underlying object with id, value and extensions. The accessor "getMin" gives direct access to the value
         */
        public ElementDefinitionBaseComponent setMinElement(UnsignedIntType value) { 
          this.min = value;
          return this;
        }

        /**
         * @return Minimum cardinality of the base element identified by the path.
         */
        public int getMin() { 
          return this.min == null || this.min.isEmpty() ? 0 : this.min.getValue();
        }

        /**
         * @param value Minimum cardinality of the base element identified by the path.
         */
        public ElementDefinitionBaseComponent setMin(int value) { 
            if (this.min == null)
              this.min = new UnsignedIntType();
            this.min.setValue(value);
          return this;
        }

        /**
         * @return {@link #max} (Maximum cardinality of the base element identified by the path.). This is the underlying object with id, value and extensions. The accessor "getMax" gives direct access to the value
         */
        public StringType getMaxElement() { 
          if (this.max == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ElementDefinitionBaseComponent.max");
            else if (Configuration.doAutoCreate())
              this.max = new StringType(); // bb
          return this.max;
        }

        public boolean hasMaxElement() { 
          return this.max != null && !this.max.isEmpty();
        }

        public boolean hasMax() { 
          return this.max != null && !this.max.isEmpty();
        }

        /**
         * @param value {@link #max} (Maximum cardinality of the base element identified by the path.). This is the underlying object with id, value and extensions. The accessor "getMax" gives direct access to the value
         */
        public ElementDefinitionBaseComponent setMaxElement(StringType value) { 
          this.max = value;
          return this;
        }

        /**
         * @return Maximum cardinality of the base element identified by the path.
         */
        public String getMax() { 
          return this.max == null ? null : this.max.getValue();
        }

        /**
         * @param value Maximum cardinality of the base element identified by the path.
         */
        public ElementDefinitionBaseComponent setMax(String value) { 
            if (this.max == null)
              this.max = new StringType();
            this.max.setValue(value);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("path", "string", "The Path that identifies the base element - this matches the ElementDefinition.path for that element. Across FHIR, there is only one base definition of any element - that is, an element definition on a [[[StructureDefinition]]] without a StructureDefinition.base.", 0, java.lang.Integer.MAX_VALUE, path));
          childrenList.add(new Property("min", "unsignedInt", "Minimum cardinality of the base element identified by the path.", 0, java.lang.Integer.MAX_VALUE, min));
          childrenList.add(new Property("max", "string", "Maximum cardinality of the base element identified by the path.", 0, java.lang.Integer.MAX_VALUE, max));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3433509: /*path*/ return this.path == null ? new Base[0] : new Base[] {this.path}; // StringType
        case 108114: /*min*/ return this.min == null ? new Base[0] : new Base[] {this.min}; // UnsignedIntType
        case 107876: /*max*/ return this.max == null ? new Base[0] : new Base[] {this.max}; // StringType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3433509: // path
          this.path = castToString(value); // StringType
          return value;
        case 108114: // min
          this.min = castToUnsignedInt(value); // UnsignedIntType
          return value;
        case 107876: // max
          this.max = castToString(value); // StringType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("path")) {
          this.path = castToString(value); // StringType
        } else if (name.equals("min")) {
          this.min = castToUnsignedInt(value); // UnsignedIntType
        } else if (name.equals("max")) {
          this.max = castToString(value); // StringType
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3433509:  return getPathElement();
        case 108114:  return getMinElement();
        case 107876:  return getMaxElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3433509: /*path*/ return new String[] {"string"};
        case 108114: /*min*/ return new String[] {"unsignedInt"};
        case 107876: /*max*/ return new String[] {"string"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("path")) {
          throw new FHIRException("Cannot call addChild on a primitive type ElementDefinition.path");
        }
        else if (name.equals("min")) {
          throw new FHIRException("Cannot call addChild on a primitive type ElementDefinition.min");
        }
        else if (name.equals("max")) {
          throw new FHIRException("Cannot call addChild on a primitive type ElementDefinition.max");
        }
        else
          return super.addChild(name);
      }

      public ElementDefinitionBaseComponent copy() {
        ElementDefinitionBaseComponent dst = new ElementDefinitionBaseComponent();
        copyValues(dst);
        dst.path = path == null ? null : path.copy();
        dst.min = min == null ? null : min.copy();
        dst.max = max == null ? null : max.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ElementDefinitionBaseComponent))
          return false;
        ElementDefinitionBaseComponent o = (ElementDefinitionBaseComponent) other;
        return compareDeep(path, o.path, true) && compareDeep(min, o.min, true) && compareDeep(max, o.max, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ElementDefinitionBaseComponent))
          return false;
        ElementDefinitionBaseComponent o = (ElementDefinitionBaseComponent) other;
        return compareValues(path, o.path, true) && compareValues(min, o.min, true) && compareValues(max, o.max, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(path, min, max);
      }

  public String fhirType() {
    return "ElementDefinition.base";

  }

  }

    @Block()
    public static class TypeRefComponent extends Element implements IBaseDatatypeElement {
        /**
         * URL of Data type or Resource that is a(or the) type used for this element. References are URLs that are relative to http://hl7.org/fhir/StructureDefinition e.g. "string" is a reference to http://hl7.org/fhir/StructureDefinition/string. Absolute URLs are only allowed in logical models.
         */
        @Child(name = "code", type = {UriType.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Data type or Resource (reference to definition)", formalDefinition="URL of Data type or Resource that is a(or the) type used for this element. References are URLs that are relative to http://hl7.org/fhir/StructureDefinition e.g. \"string\" is a reference to http://hl7.org/fhir/StructureDefinition/string. Absolute URLs are only allowed in logical models." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/defined-types")
        protected UriType code;

        /**
         * Identifies a profile structure or implementation Guide that SHALL hold for the datatype this element refers to. Can be a local reference - to a contained StructureDefinition, or a reference to another StructureDefinition or Implementation Guide by a canonical URL. When an implementation guide is specified, the resource SHALL conform to at least one profile defined in the implementation guide.
         */
        @Child(name = "profile", type = {UriType.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Profile (StructureDefinition) to apply (or IG)", formalDefinition="Identifies a profile structure or implementation Guide that SHALL hold for the datatype this element refers to. Can be a local reference - to a contained StructureDefinition, or a reference to another StructureDefinition or Implementation Guide by a canonical URL. When an implementation guide is specified, the resource SHALL conform to at least one profile defined in the implementation guide." )
        protected UriType profile;

        /**
         * Identifies a profile structure or implementation Guide that SHALL hold for the target of the reference this element refers to. Can be a local reference - to a contained StructureDefinition, or a reference to another StructureDefinition or Implementation Guide by a canonical URL. When an implementation guide is specified, the resource SHALL conform to at least one profile defined in the implementation guide.
         */
        @Child(name = "targetProfile", type = {UriType.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Profile (StructureDefinition) to apply to reference target (or IG)", formalDefinition="Identifies a profile structure or implementation Guide that SHALL hold for the target of the reference this element refers to. Can be a local reference - to a contained StructureDefinition, or a reference to another StructureDefinition or Implementation Guide by a canonical URL. When an implementation guide is specified, the resource SHALL conform to at least one profile defined in the implementation guide." )
        protected UriType targetProfile;

        /**
         * If the type is a reference to another resource, how the resource is or can be aggregated - is it a contained resource, or a reference, and if the context is a bundle, is it included in the bundle.
         */
        @Child(name = "aggregation", type = {CodeType.class}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="contained | referenced | bundled - how aggregated", formalDefinition="If the type is a reference to another resource, how the resource is or can be aggregated - is it a contained resource, or a reference, and if the context is a bundle, is it included in the bundle." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/resource-aggregation-mode")
        protected List<Enumeration<AggregationMode>> aggregation;

        /**
         * Whether this reference needs to be version specific or version independent, or whether either can be used.
         */
        @Child(name = "versioning", type = {CodeType.class}, order=5, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="either | independent | specific", formalDefinition="Whether this reference needs to be version specific or version independent, or whether either can be used." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/reference-version-rules")
        protected Enumeration<ReferenceVersionRules> versioning;

        private static final long serialVersionUID = -560921355L;

    /**
     * Constructor
     */
      public TypeRefComponent() {
        super();
      }

    /**
     * Constructor
     */
      public TypeRefComponent(UriType code) {
        super();
        this.code = code;
      }

        /**
         * @return {@link #code} (URL of Data type or Resource that is a(or the) type used for this element. References are URLs that are relative to http://hl7.org/fhir/StructureDefinition e.g. "string" is a reference to http://hl7.org/fhir/StructureDefinition/string. Absolute URLs are only allowed in logical models.). This is the underlying object with id, value and extensions. The accessor "getCode" gives direct access to the value
         */
        public UriType getCodeElement() { 
          if (this.code == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TypeRefComponent.code");
            else if (Configuration.doAutoCreate())
              this.code = new UriType(); // bb
          return this.code;
        }

        public boolean hasCodeElement() { 
          return this.code != null && !this.code.isEmpty();
        }

        public boolean hasCode() { 
          return this.code != null && !this.code.isEmpty();
        }

        /**
         * @param value {@link #code} (URL of Data type or Resource that is a(or the) type used for this element. References are URLs that are relative to http://hl7.org/fhir/StructureDefinition e.g. "string" is a reference to http://hl7.org/fhir/StructureDefinition/string. Absolute URLs are only allowed in logical models.). This is the underlying object with id, value and extensions. The accessor "getCode" gives direct access to the value
         */
        public TypeRefComponent setCodeElement(UriType value) { 
          this.code = value;
          return this;
        }

        /**
         * @return URL of Data type or Resource that is a(or the) type used for this element. References are URLs that are relative to http://hl7.org/fhir/StructureDefinition e.g. "string" is a reference to http://hl7.org/fhir/StructureDefinition/string. Absolute URLs are only allowed in logical models.
         */
        public String getCode() { 
          return this.code == null ? null : this.code.getValue();
        }

        /**
         * @param value URL of Data type or Resource that is a(or the) type used for this element. References are URLs that are relative to http://hl7.org/fhir/StructureDefinition e.g. "string" is a reference to http://hl7.org/fhir/StructureDefinition/string. Absolute URLs are only allowed in logical models.
         */
        public TypeRefComponent setCode(String value) { 
            if (this.code == null)
              this.code = new UriType();
            this.code.setValue(value);
          return this;
        }

        /**
         * @return {@link #profile} (Identifies a profile structure or implementation Guide that SHALL hold for the datatype this element refers to. Can be a local reference - to a contained StructureDefinition, or a reference to another StructureDefinition or Implementation Guide by a canonical URL. When an implementation guide is specified, the resource SHALL conform to at least one profile defined in the implementation guide.). This is the underlying object with id, value and extensions. The accessor "getProfile" gives direct access to the value
         */
        public UriType getProfileElement() { 
          if (this.profile == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TypeRefComponent.profile");
            else if (Configuration.doAutoCreate())
              this.profile = new UriType(); // bb
          return this.profile;
        }

        public boolean hasProfileElement() { 
          return this.profile != null && !this.profile.isEmpty();
        }

        public boolean hasProfile() { 
          return this.profile != null && !this.profile.isEmpty();
        }

        /**
         * @param value {@link #profile} (Identifies a profile structure or implementation Guide that SHALL hold for the datatype this element refers to. Can be a local reference - to a contained StructureDefinition, or a reference to another StructureDefinition or Implementation Guide by a canonical URL. When an implementation guide is specified, the resource SHALL conform to at least one profile defined in the implementation guide.). This is the underlying object with id, value and extensions. The accessor "getProfile" gives direct access to the value
         */
        public TypeRefComponent setProfileElement(UriType value) { 
          this.profile = value;
          return this;
        }

        /**
         * @return Identifies a profile structure or implementation Guide that SHALL hold for the datatype this element refers to. Can be a local reference - to a contained StructureDefinition, or a reference to another StructureDefinition or Implementation Guide by a canonical URL. When an implementation guide is specified, the resource SHALL conform to at least one profile defined in the implementation guide.
         */
        public String getProfile() { 
          return this.profile == null ? null : this.profile.getValue();
        }

        /**
         * @param value Identifies a profile structure or implementation Guide that SHALL hold for the datatype this element refers to. Can be a local reference - to a contained StructureDefinition, or a reference to another StructureDefinition or Implementation Guide by a canonical URL. When an implementation guide is specified, the resource SHALL conform to at least one profile defined in the implementation guide.
         */
        public TypeRefComponent setProfile(String value) { 
          if (Utilities.noString(value))
            this.profile = null;
          else {
            if (this.profile == null)
              this.profile = new UriType();
            this.profile.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #targetProfile} (Identifies a profile structure or implementation Guide that SHALL hold for the target of the reference this element refers to. Can be a local reference - to a contained StructureDefinition, or a reference to another StructureDefinition or Implementation Guide by a canonical URL. When an implementation guide is specified, the resource SHALL conform to at least one profile defined in the implementation guide.). This is the underlying object with id, value and extensions. The accessor "getTargetProfile" gives direct access to the value
         */
        public UriType getTargetProfileElement() { 
          if (this.targetProfile == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TypeRefComponent.targetProfile");
            else if (Configuration.doAutoCreate())
              this.targetProfile = new UriType(); // bb
          return this.targetProfile;
        }

        public boolean hasTargetProfileElement() { 
          return this.targetProfile != null && !this.targetProfile.isEmpty();
        }

        public boolean hasTargetProfile() { 
          return this.targetProfile != null && !this.targetProfile.isEmpty();
        }

        /**
         * @param value {@link #targetProfile} (Identifies a profile structure or implementation Guide that SHALL hold for the target of the reference this element refers to. Can be a local reference - to a contained StructureDefinition, or a reference to another StructureDefinition or Implementation Guide by a canonical URL. When an implementation guide is specified, the resource SHALL conform to at least one profile defined in the implementation guide.). This is the underlying object with id, value and extensions. The accessor "getTargetProfile" gives direct access to the value
         */
        public TypeRefComponent setTargetProfileElement(UriType value) { 
          this.targetProfile = value;
          return this;
        }

        /**
         * @return Identifies a profile structure or implementation Guide that SHALL hold for the target of the reference this element refers to. Can be a local reference - to a contained StructureDefinition, or a reference to another StructureDefinition or Implementation Guide by a canonical URL. When an implementation guide is specified, the resource SHALL conform to at least one profile defined in the implementation guide.
         */
        public String getTargetProfile() { 
          return this.targetProfile == null ? null : this.targetProfile.getValue();
        }

        /**
         * @param value Identifies a profile structure or implementation Guide that SHALL hold for the target of the reference this element refers to. Can be a local reference - to a contained StructureDefinition, or a reference to another StructureDefinition or Implementation Guide by a canonical URL. When an implementation guide is specified, the resource SHALL conform to at least one profile defined in the implementation guide.
         */
        public TypeRefComponent setTargetProfile(String value) { 
          if (Utilities.noString(value))
            this.targetProfile = null;
          else {
            if (this.targetProfile == null)
              this.targetProfile = new UriType();
            this.targetProfile.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #aggregation} (If the type is a reference to another resource, how the resource is or can be aggregated - is it a contained resource, or a reference, and if the context is a bundle, is it included in the bundle.)
         */
        public List<Enumeration<AggregationMode>> getAggregation() { 
          if (this.aggregation == null)
            this.aggregation = new ArrayList<Enumeration<AggregationMode>>();
          return this.aggregation;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public TypeRefComponent setAggregation(List<Enumeration<AggregationMode>> theAggregation) { 
          this.aggregation = theAggregation;
          return this;
        }

        public boolean hasAggregation() { 
          if (this.aggregation == null)
            return false;
          for (Enumeration<AggregationMode> item : this.aggregation)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #aggregation} (If the type is a reference to another resource, how the resource is or can be aggregated - is it a contained resource, or a reference, and if the context is a bundle, is it included in the bundle.)
         */
        public Enumeration<AggregationMode> addAggregationElement() {//2 
          Enumeration<AggregationMode> t = new Enumeration<AggregationMode>(new AggregationModeEnumFactory());
          if (this.aggregation == null)
            this.aggregation = new ArrayList<Enumeration<AggregationMode>>();
          this.aggregation.add(t);
          return t;
        }

        /**
         * @param value {@link #aggregation} (If the type is a reference to another resource, how the resource is or can be aggregated - is it a contained resource, or a reference, and if the context is a bundle, is it included in the bundle.)
         */
        public TypeRefComponent addAggregation(AggregationMode value) { //1
          Enumeration<AggregationMode> t = new Enumeration<AggregationMode>(new AggregationModeEnumFactory());
          t.setValue(value);
          if (this.aggregation == null)
            this.aggregation = new ArrayList<Enumeration<AggregationMode>>();
          this.aggregation.add(t);
          return this;
        }

        /**
         * @param value {@link #aggregation} (If the type is a reference to another resource, how the resource is or can be aggregated - is it a contained resource, or a reference, and if the context is a bundle, is it included in the bundle.)
         */
        public boolean hasAggregation(AggregationMode value) { 
          if (this.aggregation == null)
            return false;
          for (Enumeration<AggregationMode> v : this.aggregation)
            if (v.getValue().equals(value)) // code
              return true;
          return false;
        }

        /**
         * @return {@link #versioning} (Whether this reference needs to be version specific or version independent, or whether either can be used.). This is the underlying object with id, value and extensions. The accessor "getVersioning" gives direct access to the value
         */
        public Enumeration<ReferenceVersionRules> getVersioningElement() { 
          if (this.versioning == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TypeRefComponent.versioning");
            else if (Configuration.doAutoCreate())
              this.versioning = new Enumeration<ReferenceVersionRules>(new ReferenceVersionRulesEnumFactory()); // bb
          return this.versioning;
        }

        public boolean hasVersioningElement() { 
          return this.versioning != null && !this.versioning.isEmpty();
        }

        public boolean hasVersioning() { 
          return this.versioning != null && !this.versioning.isEmpty();
        }

        /**
         * @param value {@link #versioning} (Whether this reference needs to be version specific or version independent, or whether either can be used.). This is the underlying object with id, value and extensions. The accessor "getVersioning" gives direct access to the value
         */
        public TypeRefComponent setVersioningElement(Enumeration<ReferenceVersionRules> value) { 
          this.versioning = value;
          return this;
        }

        /**
         * @return Whether this reference needs to be version specific or version independent, or whether either can be used.
         */
        public ReferenceVersionRules getVersioning() { 
          return this.versioning == null ? null : this.versioning.getValue();
        }

        /**
         * @param value Whether this reference needs to be version specific or version independent, or whether either can be used.
         */
        public TypeRefComponent setVersioning(ReferenceVersionRules value) { 
          if (value == null)
            this.versioning = null;
          else {
            if (this.versioning == null)
              this.versioning = new Enumeration<ReferenceVersionRules>(new ReferenceVersionRulesEnumFactory());
            this.versioning.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("code", "uri", "URL of Data type or Resource that is a(or the) type used for this element. References are URLs that are relative to http://hl7.org/fhir/StructureDefinition e.g. \"string\" is a reference to http://hl7.org/fhir/StructureDefinition/string. Absolute URLs are only allowed in logical models.", 0, java.lang.Integer.MAX_VALUE, code));
          childrenList.add(new Property("profile", "uri", "Identifies a profile structure or implementation Guide that SHALL hold for the datatype this element refers to. Can be a local reference - to a contained StructureDefinition, or a reference to another StructureDefinition or Implementation Guide by a canonical URL. When an implementation guide is specified, the resource SHALL conform to at least one profile defined in the implementation guide.", 0, java.lang.Integer.MAX_VALUE, profile));
          childrenList.add(new Property("targetProfile", "uri", "Identifies a profile structure or implementation Guide that SHALL hold for the target of the reference this element refers to. Can be a local reference - to a contained StructureDefinition, or a reference to another StructureDefinition or Implementation Guide by a canonical URL. When an implementation guide is specified, the resource SHALL conform to at least one profile defined in the implementation guide.", 0, java.lang.Integer.MAX_VALUE, targetProfile));
          childrenList.add(new Property("aggregation", "code", "If the type is a reference to another resource, how the resource is or can be aggregated - is it a contained resource, or a reference, and if the context is a bundle, is it included in the bundle.", 0, java.lang.Integer.MAX_VALUE, aggregation));
          childrenList.add(new Property("versioning", "code", "Whether this reference needs to be version specific or version independent, or whether either can be used.", 0, java.lang.Integer.MAX_VALUE, versioning));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3059181: /*code*/ return this.code == null ? new Base[0] : new Base[] {this.code}; // UriType
        case -309425751: /*profile*/ return this.profile == null ? new Base[0] : new Base[] {this.profile}; // UriType
        case 1994521304: /*targetProfile*/ return this.targetProfile == null ? new Base[0] : new Base[] {this.targetProfile}; // UriType
        case 841524962: /*aggregation*/ return this.aggregation == null ? new Base[0] : this.aggregation.toArray(new Base[this.aggregation.size()]); // Enumeration<AggregationMode>
        case -670487542: /*versioning*/ return this.versioning == null ? new Base[0] : new Base[] {this.versioning}; // Enumeration<ReferenceVersionRules>
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3059181: // code
          this.code = castToUri(value); // UriType
          return value;
        case -309425751: // profile
          this.profile = castToUri(value); // UriType
          return value;
        case 1994521304: // targetProfile
          this.targetProfile = castToUri(value); // UriType
          return value;
        case 841524962: // aggregation
          value = new AggregationModeEnumFactory().fromType(castToCode(value));
          this.getAggregation().add((Enumeration) value); // Enumeration<AggregationMode>
          return value;
        case -670487542: // versioning
          value = new ReferenceVersionRulesEnumFactory().fromType(castToCode(value));
          this.versioning = (Enumeration) value; // Enumeration<ReferenceVersionRules>
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("code")) {
          this.code = castToUri(value); // UriType
        } else if (name.equals("profile")) {
          this.profile = castToUri(value); // UriType
        } else if (name.equals("targetProfile")) {
          this.targetProfile = castToUri(value); // UriType
        } else if (name.equals("aggregation")) {
          value = new AggregationModeEnumFactory().fromType(castToCode(value));
          this.getAggregation().add((Enumeration) value);
        } else if (name.equals("versioning")) {
          value = new ReferenceVersionRulesEnumFactory().fromType(castToCode(value));
          this.versioning = (Enumeration) value; // Enumeration<ReferenceVersionRules>
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3059181:  return getCodeElement();
        case -309425751:  return getProfileElement();
        case 1994521304:  return getTargetProfileElement();
        case 841524962:  return addAggregationElement();
        case -670487542:  return getVersioningElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3059181: /*code*/ return new String[] {"uri"};
        case -309425751: /*profile*/ return new String[] {"uri"};
        case 1994521304: /*targetProfile*/ return new String[] {"uri"};
        case 841524962: /*aggregation*/ return new String[] {"code"};
        case -670487542: /*versioning*/ return new String[] {"code"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("code")) {
          throw new FHIRException("Cannot call addChild on a primitive type ElementDefinition.code");
        }
        else if (name.equals("profile")) {
          throw new FHIRException("Cannot call addChild on a primitive type ElementDefinition.profile");
        }
        else if (name.equals("targetProfile")) {
          throw new FHIRException("Cannot call addChild on a primitive type ElementDefinition.targetProfile");
        }
        else if (name.equals("aggregation")) {
          throw new FHIRException("Cannot call addChild on a primitive type ElementDefinition.aggregation");
        }
        else if (name.equals("versioning")) {
          throw new FHIRException("Cannot call addChild on a primitive type ElementDefinition.versioning");
        }
        else
          return super.addChild(name);
      }

      public TypeRefComponent copy() {
        TypeRefComponent dst = new TypeRefComponent();
        copyValues(dst);
        dst.code = code == null ? null : code.copy();
        dst.profile = profile == null ? null : profile.copy();
        dst.targetProfile = targetProfile == null ? null : targetProfile.copy();
        if (aggregation != null) {
          dst.aggregation = new ArrayList<Enumeration<AggregationMode>>();
          for (Enumeration<AggregationMode> i : aggregation)
            dst.aggregation.add(i.copy());
        };
        dst.versioning = versioning == null ? null : versioning.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof TypeRefComponent))
          return false;
        TypeRefComponent o = (TypeRefComponent) other;
        return compareDeep(code, o.code, true) && compareDeep(profile, o.profile, true) && compareDeep(targetProfile, o.targetProfile, true)
           && compareDeep(aggregation, o.aggregation, true) && compareDeep(versioning, o.versioning, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof TypeRefComponent))
          return false;
        TypeRefComponent o = (TypeRefComponent) other;
        return compareValues(code, o.code, true) && compareValues(profile, o.profile, true) && compareValues(targetProfile, o.targetProfile, true)
           && compareValues(aggregation, o.aggregation, true) && compareValues(versioning, o.versioning, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(code, profile, targetProfile
          , aggregation, versioning);
      }

  public String fhirType() {
    return "ElementDefinition.type";

  }

  }

    @Block()
    public static class ElementDefinitionExampleComponent extends Element implements IBaseDatatypeElement {
        /**
         * Describes the purpose of this example amoung the set of examples.
         */
        @Child(name = "label", type = {StringType.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Describes the purpose of this example", formalDefinition="Describes the purpose of this example amoung the set of examples." )
        protected StringType label;

        /**
         * The actual value for the element, which must be one of the types allowed for this element.
         */
        @Child(name = "value", type = {}, order=2, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Value of Example (one of allowed types)", formalDefinition="The actual value for the element, which must be one of the types allowed for this element." )
        protected org.hl7.fhir.dstu3.model.Type value;

        private static final long serialVersionUID = 1346348024L;

    /**
     * Constructor
     */
      public ElementDefinitionExampleComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ElementDefinitionExampleComponent(StringType label, org.hl7.fhir.dstu3.model.Type value) {
        super();
        this.label = label;
        this.value = value;
      }

        /**
         * @return {@link #label} (Describes the purpose of this example amoung the set of examples.). This is the underlying object with id, value and extensions. The accessor "getLabel" gives direct access to the value
         */
        public StringType getLabelElement() { 
          if (this.label == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ElementDefinitionExampleComponent.label");
            else if (Configuration.doAutoCreate())
              this.label = new StringType(); // bb
          return this.label;
        }

        public boolean hasLabelElement() { 
          return this.label != null && !this.label.isEmpty();
        }

        public boolean hasLabel() { 
          return this.label != null && !this.label.isEmpty();
        }

        /**
         * @param value {@link #label} (Describes the purpose of this example amoung the set of examples.). This is the underlying object with id, value and extensions. The accessor "getLabel" gives direct access to the value
         */
        public ElementDefinitionExampleComponent setLabelElement(StringType value) { 
          this.label = value;
          return this;
        }

        /**
         * @return Describes the purpose of this example amoung the set of examples.
         */
        public String getLabel() { 
          return this.label == null ? null : this.label.getValue();
        }

        /**
         * @param value Describes the purpose of this example amoung the set of examples.
         */
        public ElementDefinitionExampleComponent setLabel(String value) { 
            if (this.label == null)
              this.label = new StringType();
            this.label.setValue(value);
          return this;
        }

        /**
         * @return {@link #value} (The actual value for the element, which must be one of the types allowed for this element.)
         */
        public org.hl7.fhir.dstu3.model.Type getValue() { 
          return this.value;
        }

        public boolean hasValue() { 
          return this.value != null && !this.value.isEmpty();
        }

        /**
         * @param value {@link #value} (The actual value for the element, which must be one of the types allowed for this element.)
         */
        public ElementDefinitionExampleComponent setValue(org.hl7.fhir.dstu3.model.Type value) { 
          this.value = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("label", "string", "Describes the purpose of this example amoung the set of examples.", 0, java.lang.Integer.MAX_VALUE, label));
          childrenList.add(new Property("value[x]", "*", "The actual value for the element, which must be one of the types allowed for this element.", 0, java.lang.Integer.MAX_VALUE, value));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 102727412: /*label*/ return this.label == null ? new Base[0] : new Base[] {this.label}; // StringType
        case 111972721: /*value*/ return this.value == null ? new Base[0] : new Base[] {this.value}; // org.hl7.fhir.dstu3.model.Type
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 102727412: // label
          this.label = castToString(value); // StringType
          return value;
        case 111972721: // value
          this.value = castToType(value); // org.hl7.fhir.dstu3.model.Type
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("label")) {
          this.label = castToString(value); // StringType
        } else if (name.equals("value[x]")) {
          this.value = castToType(value); // org.hl7.fhir.dstu3.model.Type
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 102727412:  return getLabelElement();
        case -1410166417:  return getValue(); 
        case 111972721:  return getValue(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 102727412: /*label*/ return new String[] {"string"};
        case 111972721: /*value*/ return new String[] {"*"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("label")) {
          throw new FHIRException("Cannot call addChild on a primitive type ElementDefinition.label");
        }
        else if (name.equals("valueBoolean")) {
          this.value = new BooleanType();
          return this.value;
        }
        else if (name.equals("valueInteger")) {
          this.value = new IntegerType();
          return this.value;
        }
        else if (name.equals("valueDecimal")) {
          this.value = new DecimalType();
          return this.value;
        }
        else if (name.equals("valueBase64Binary")) {
          this.value = new Base64BinaryType();
          return this.value;
        }
        else if (name.equals("valueInstant")) {
          this.value = new InstantType();
          return this.value;
        }
        else if (name.equals("valueString")) {
          this.value = new StringType();
          return this.value;
        }
        else if (name.equals("valueUri")) {
          this.value = new UriType();
          return this.value;
        }
        else if (name.equals("valueDate")) {
          this.value = new DateType();
          return this.value;
        }
        else if (name.equals("valueDateTime")) {
          this.value = new DateTimeType();
          return this.value;
        }
        else if (name.equals("valueTime")) {
          this.value = new TimeType();
          return this.value;
        }
        else if (name.equals("valueCode")) {
          this.value = new CodeType();
          return this.value;
        }
        else if (name.equals("valueOid")) {
          this.value = new OidType();
          return this.value;
        }
        else if (name.equals("valueId")) {
          this.value = new IdType();
          return this.value;
        }
        else if (name.equals("valueUnsignedInt")) {
          this.value = new UnsignedIntType();
          return this.value;
        }
        else if (name.equals("valuePositiveInt")) {
          this.value = new PositiveIntType();
          return this.value;
        }
        else if (name.equals("valueMarkdown")) {
          this.value = new MarkdownType();
          return this.value;
        }
        else if (name.equals("valueAnnotation")) {
          this.value = new Annotation();
          return this.value;
        }
        else if (name.equals("valueAttachment")) {
          this.value = new Attachment();
          return this.value;
        }
        else if (name.equals("valueIdentifier")) {
          this.value = new Identifier();
          return this.value;
        }
        else if (name.equals("valueCodeableConcept")) {
          this.value = new CodeableConcept();
          return this.value;
        }
        else if (name.equals("valueCoding")) {
          this.value = new Coding();
          return this.value;
        }
        else if (name.equals("valueQuantity")) {
          this.value = new Quantity();
          return this.value;
        }
        else if (name.equals("valueRange")) {
          this.value = new Range();
          return this.value;
        }
        else if (name.equals("valuePeriod")) {
          this.value = new Period();
          return this.value;
        }
        else if (name.equals("valueRatio")) {
          this.value = new Ratio();
          return this.value;
        }
        else if (name.equals("valueSampledData")) {
          this.value = new SampledData();
          return this.value;
        }
        else if (name.equals("valueSignature")) {
          this.value = new Signature();
          return this.value;
        }
        else if (name.equals("valueHumanName")) {
          this.value = new HumanName();
          return this.value;
        }
        else if (name.equals("valueAddress")) {
          this.value = new Address();
          return this.value;
        }
        else if (name.equals("valueContactPoint")) {
          this.value = new ContactPoint();
          return this.value;
        }
        else if (name.equals("valueTiming")) {
          this.value = new Timing();
          return this.value;
        }
        else if (name.equals("valueReference")) {
          this.value = new Reference();
          return this.value;
        }
        else if (name.equals("valueMeta")) {
          this.value = new Meta();
          return this.value;
        }
        else
          return super.addChild(name);
      }

      public ElementDefinitionExampleComponent copy() {
        ElementDefinitionExampleComponent dst = new ElementDefinitionExampleComponent();
        copyValues(dst);
        dst.label = label == null ? null : label.copy();
        dst.value = value == null ? null : value.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ElementDefinitionExampleComponent))
          return false;
        ElementDefinitionExampleComponent o = (ElementDefinitionExampleComponent) other;
        return compareDeep(label, o.label, true) && compareDeep(value, o.value, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ElementDefinitionExampleComponent))
          return false;
        ElementDefinitionExampleComponent o = (ElementDefinitionExampleComponent) other;
        return compareValues(label, o.label, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(label, value);
      }

  public String fhirType() {
    return "ElementDefinition.example";

  }

  }

    @Block()
    public static class ElementDefinitionConstraintComponent extends Element implements IBaseDatatypeElement {
        /**
         * Allows identification of which elements have their cardinalities impacted by the constraint.  Will not be referenced for constraints that do not affect cardinality.
         */
        @Child(name = "key", type = {IdType.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Target of 'condition' reference above", formalDefinition="Allows identification of which elements have their cardinalities impacted by the constraint.  Will not be referenced for constraints that do not affect cardinality." )
        protected IdType key;

        /**
         * Description of why this constraint is necessary or appropriate.
         */
        @Child(name = "requirements", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Why this constraint is necessary or appropriate", formalDefinition="Description of why this constraint is necessary or appropriate." )
        protected StringType requirements;

        /**
         * Identifies the impact constraint violation has on the conformance of the instance.
         */
        @Child(name = "severity", type = {CodeType.class}, order=3, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="error | warning", formalDefinition="Identifies the impact constraint violation has on the conformance of the instance." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/constraint-severity")
        protected Enumeration<ConstraintSeverity> severity;

        /**
         * Text that can be used to describe the constraint in messages identifying that the constraint has been violated.
         */
        @Child(name = "human", type = {StringType.class}, order=4, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Human description of constraint", formalDefinition="Text that can be used to describe the constraint in messages identifying that the constraint has been violated." )
        protected StringType human;

        /**
         * A [FHIRPath](http://hl7.org/fluentpath) expression of constraint that can be executed to see if this constraint is met.
         */
        @Child(name = "expression", type = {StringType.class}, order=5, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="FHIRPath expression of constraint", formalDefinition="A [FHIRPath](http://hl7.org/fluentpath) expression of constraint that can be executed to see if this constraint is met." )
        protected StringType expression;

        /**
         * An XPath expression of constraint that can be executed to see if this constraint is met.
         */
        @Child(name = "xpath", type = {StringType.class}, order=6, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="XPath expression of constraint", formalDefinition="An XPath expression of constraint that can be executed to see if this constraint is met." )
        protected StringType xpath;

        /**
         * A reference to the original source of the constraint, for traceability purposes.
         */
        @Child(name = "source", type = {UriType.class}, order=7, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Reference to original source of constraint", formalDefinition="A reference to the original source of the constraint, for traceability purposes." )
        protected UriType source;

        private static final long serialVersionUID = 1860862205L;

    /**
     * Constructor
     */
      public ElementDefinitionConstraintComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ElementDefinitionConstraintComponent(IdType key, Enumeration<ConstraintSeverity> severity, StringType human, StringType expression) {
        super();
        this.key = key;
        this.severity = severity;
        this.human = human;
        this.expression = expression;
      }

        /**
         * @return {@link #key} (Allows identification of which elements have their cardinalities impacted by the constraint.  Will not be referenced for constraints that do not affect cardinality.). This is the underlying object with id, value and extensions. The accessor "getKey" gives direct access to the value
         */
        public IdType getKeyElement() { 
          if (this.key == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ElementDefinitionConstraintComponent.key");
            else if (Configuration.doAutoCreate())
              this.key = new IdType(); // bb
          return this.key;
        }

        public boolean hasKeyElement() { 
          return this.key != null && !this.key.isEmpty();
        }

        public boolean hasKey() { 
          return this.key != null && !this.key.isEmpty();
        }

        /**
         * @param value {@link #key} (Allows identification of which elements have their cardinalities impacted by the constraint.  Will not be referenced for constraints that do not affect cardinality.). This is the underlying object with id, value and extensions. The accessor "getKey" gives direct access to the value
         */
        public ElementDefinitionConstraintComponent setKeyElement(IdType value) { 
          this.key = value;
          return this;
        }

        /**
         * @return Allows identification of which elements have their cardinalities impacted by the constraint.  Will not be referenced for constraints that do not affect cardinality.
         */
        public String getKey() { 
          return this.key == null ? null : this.key.getValue();
        }

        /**
         * @param value Allows identification of which elements have their cardinalities impacted by the constraint.  Will not be referenced for constraints that do not affect cardinality.
         */
        public ElementDefinitionConstraintComponent setKey(String value) { 
            if (this.key == null)
              this.key = new IdType();
            this.key.setValue(value);
          return this;
        }

        /**
         * @return {@link #requirements} (Description of why this constraint is necessary or appropriate.). This is the underlying object with id, value and extensions. The accessor "getRequirements" gives direct access to the value
         */
        public StringType getRequirementsElement() { 
          if (this.requirements == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ElementDefinitionConstraintComponent.requirements");
            else if (Configuration.doAutoCreate())
              this.requirements = new StringType(); // bb
          return this.requirements;
        }

        public boolean hasRequirementsElement() { 
          return this.requirements != null && !this.requirements.isEmpty();
        }

        public boolean hasRequirements() { 
          return this.requirements != null && !this.requirements.isEmpty();
        }

        /**
         * @param value {@link #requirements} (Description of why this constraint is necessary or appropriate.). This is the underlying object with id, value and extensions. The accessor "getRequirements" gives direct access to the value
         */
        public ElementDefinitionConstraintComponent setRequirementsElement(StringType value) { 
          this.requirements = value;
          return this;
        }

        /**
         * @return Description of why this constraint is necessary or appropriate.
         */
        public String getRequirements() { 
          return this.requirements == null ? null : this.requirements.getValue();
        }

        /**
         * @param value Description of why this constraint is necessary or appropriate.
         */
        public ElementDefinitionConstraintComponent setRequirements(String value) { 
          if (Utilities.noString(value))
            this.requirements = null;
          else {
            if (this.requirements == null)
              this.requirements = new StringType();
            this.requirements.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #severity} (Identifies the impact constraint violation has on the conformance of the instance.). This is the underlying object with id, value and extensions. The accessor "getSeverity" gives direct access to the value
         */
        public Enumeration<ConstraintSeverity> getSeverityElement() { 
          if (this.severity == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ElementDefinitionConstraintComponent.severity");
            else if (Configuration.doAutoCreate())
              this.severity = new Enumeration<ConstraintSeverity>(new ConstraintSeverityEnumFactory()); // bb
          return this.severity;
        }

        public boolean hasSeverityElement() { 
          return this.severity != null && !this.severity.isEmpty();
        }

        public boolean hasSeverity() { 
          return this.severity != null && !this.severity.isEmpty();
        }

        /**
         * @param value {@link #severity} (Identifies the impact constraint violation has on the conformance of the instance.). This is the underlying object with id, value and extensions. The accessor "getSeverity" gives direct access to the value
         */
        public ElementDefinitionConstraintComponent setSeverityElement(Enumeration<ConstraintSeverity> value) { 
          this.severity = value;
          return this;
        }

        /**
         * @return Identifies the impact constraint violation has on the conformance of the instance.
         */
        public ConstraintSeverity getSeverity() { 
          return this.severity == null ? null : this.severity.getValue();
        }

        /**
         * @param value Identifies the impact constraint violation has on the conformance of the instance.
         */
        public ElementDefinitionConstraintComponent setSeverity(ConstraintSeverity value) { 
            if (this.severity == null)
              this.severity = new Enumeration<ConstraintSeverity>(new ConstraintSeverityEnumFactory());
            this.severity.setValue(value);
          return this;
        }

        /**
         * @return {@link #human} (Text that can be used to describe the constraint in messages identifying that the constraint has been violated.). This is the underlying object with id, value and extensions. The accessor "getHuman" gives direct access to the value
         */
        public StringType getHumanElement() { 
          if (this.human == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ElementDefinitionConstraintComponent.human");
            else if (Configuration.doAutoCreate())
              this.human = new StringType(); // bb
          return this.human;
        }

        public boolean hasHumanElement() { 
          return this.human != null && !this.human.isEmpty();
        }

        public boolean hasHuman() { 
          return this.human != null && !this.human.isEmpty();
        }

        /**
         * @param value {@link #human} (Text that can be used to describe the constraint in messages identifying that the constraint has been violated.). This is the underlying object with id, value and extensions. The accessor "getHuman" gives direct access to the value
         */
        public ElementDefinitionConstraintComponent setHumanElement(StringType value) { 
          this.human = value;
          return this;
        }

        /**
         * @return Text that can be used to describe the constraint in messages identifying that the constraint has been violated.
         */
        public String getHuman() { 
          return this.human == null ? null : this.human.getValue();
        }

        /**
         * @param value Text that can be used to describe the constraint in messages identifying that the constraint has been violated.
         */
        public ElementDefinitionConstraintComponent setHuman(String value) { 
            if (this.human == null)
              this.human = new StringType();
            this.human.setValue(value);
          return this;
        }

        /**
         * @return {@link #expression} (A [FHIRPath](http://hl7.org/fluentpath) expression of constraint that can be executed to see if this constraint is met.). This is the underlying object with id, value and extensions. The accessor "getExpression" gives direct access to the value
         */
        public StringType getExpressionElement() { 
          if (this.expression == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ElementDefinitionConstraintComponent.expression");
            else if (Configuration.doAutoCreate())
              this.expression = new StringType(); // bb
          return this.expression;
        }

        public boolean hasExpressionElement() { 
          return this.expression != null && !this.expression.isEmpty();
        }

        public boolean hasExpression() { 
          return this.expression != null && !this.expression.isEmpty();
        }

        /**
         * @param value {@link #expression} (A [FHIRPath](http://hl7.org/fluentpath) expression of constraint that can be executed to see if this constraint is met.). This is the underlying object with id, value and extensions. The accessor "getExpression" gives direct access to the value
         */
        public ElementDefinitionConstraintComponent setExpressionElement(StringType value) { 
          this.expression = value;
          return this;
        }

        /**
         * @return A [FHIRPath](http://hl7.org/fluentpath) expression of constraint that can be executed to see if this constraint is met.
         */
        public String getExpression() { 
          return this.expression == null ? null : this.expression.getValue();
        }

        /**
         * @param value A [FHIRPath](http://hl7.org/fluentpath) expression of constraint that can be executed to see if this constraint is met.
         */
        public ElementDefinitionConstraintComponent setExpression(String value) { 
            if (this.expression == null)
              this.expression = new StringType();
            this.expression.setValue(value);
          return this;
        }

        /**
         * @return {@link #xpath} (An XPath expression of constraint that can be executed to see if this constraint is met.). This is the underlying object with id, value and extensions. The accessor "getXpath" gives direct access to the value
         */
        public StringType getXpathElement() { 
          if (this.xpath == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ElementDefinitionConstraintComponent.xpath");
            else if (Configuration.doAutoCreate())
              this.xpath = new StringType(); // bb
          return this.xpath;
        }

        public boolean hasXpathElement() { 
          return this.xpath != null && !this.xpath.isEmpty();
        }

        public boolean hasXpath() { 
          return this.xpath != null && !this.xpath.isEmpty();
        }

        /**
         * @param value {@link #xpath} (An XPath expression of constraint that can be executed to see if this constraint is met.). This is the underlying object with id, value and extensions. The accessor "getXpath" gives direct access to the value
         */
        public ElementDefinitionConstraintComponent setXpathElement(StringType value) { 
          this.xpath = value;
          return this;
        }

        /**
         * @return An XPath expression of constraint that can be executed to see if this constraint is met.
         */
        public String getXpath() { 
          return this.xpath == null ? null : this.xpath.getValue();
        }

        /**
         * @param value An XPath expression of constraint that can be executed to see if this constraint is met.
         */
        public ElementDefinitionConstraintComponent setXpath(String value) { 
          if (Utilities.noString(value))
            this.xpath = null;
          else {
            if (this.xpath == null)
              this.xpath = new StringType();
            this.xpath.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #source} (A reference to the original source of the constraint, for traceability purposes.). This is the underlying object with id, value and extensions. The accessor "getSource" gives direct access to the value
         */
        public UriType getSourceElement() { 
          if (this.source == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ElementDefinitionConstraintComponent.source");
            else if (Configuration.doAutoCreate())
              this.source = new UriType(); // bb
          return this.source;
        }

        public boolean hasSourceElement() { 
          return this.source != null && !this.source.isEmpty();
        }

        public boolean hasSource() { 
          return this.source != null && !this.source.isEmpty();
        }

        /**
         * @param value {@link #source} (A reference to the original source of the constraint, for traceability purposes.). This is the underlying object with id, value and extensions. The accessor "getSource" gives direct access to the value
         */
        public ElementDefinitionConstraintComponent setSourceElement(UriType value) { 
          this.source = value;
          return this;
        }

        /**
         * @return A reference to the original source of the constraint, for traceability purposes.
         */
        public String getSource() { 
          return this.source == null ? null : this.source.getValue();
        }

        /**
         * @param value A reference to the original source of the constraint, for traceability purposes.
         */
        public ElementDefinitionConstraintComponent setSource(String value) { 
          if (Utilities.noString(value))
            this.source = null;
          else {
            if (this.source == null)
              this.source = new UriType();
            this.source.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("key", "id", "Allows identification of which elements have their cardinalities impacted by the constraint.  Will not be referenced for constraints that do not affect cardinality.", 0, java.lang.Integer.MAX_VALUE, key));
          childrenList.add(new Property("requirements", "string", "Description of why this constraint is necessary or appropriate.", 0, java.lang.Integer.MAX_VALUE, requirements));
          childrenList.add(new Property("severity", "code", "Identifies the impact constraint violation has on the conformance of the instance.", 0, java.lang.Integer.MAX_VALUE, severity));
          childrenList.add(new Property("human", "string", "Text that can be used to describe the constraint in messages identifying that the constraint has been violated.", 0, java.lang.Integer.MAX_VALUE, human));
          childrenList.add(new Property("expression", "string", "A [FHIRPath](http://hl7.org/fluentpath) expression of constraint that can be executed to see if this constraint is met.", 0, java.lang.Integer.MAX_VALUE, expression));
          childrenList.add(new Property("xpath", "string", "An XPath expression of constraint that can be executed to see if this constraint is met.", 0, java.lang.Integer.MAX_VALUE, xpath));
          childrenList.add(new Property("source", "uri", "A reference to the original source of the constraint, for traceability purposes.", 0, java.lang.Integer.MAX_VALUE, source));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 106079: /*key*/ return this.key == null ? new Base[0] : new Base[] {this.key}; // IdType
        case -1619874672: /*requirements*/ return this.requirements == null ? new Base[0] : new Base[] {this.requirements}; // StringType
        case 1478300413: /*severity*/ return this.severity == null ? new Base[0] : new Base[] {this.severity}; // Enumeration<ConstraintSeverity>
        case 99639597: /*human*/ return this.human == null ? new Base[0] : new Base[] {this.human}; // StringType
        case -1795452264: /*expression*/ return this.expression == null ? new Base[0] : new Base[] {this.expression}; // StringType
        case 114256029: /*xpath*/ return this.xpath == null ? new Base[0] : new Base[] {this.xpath}; // StringType
        case -896505829: /*source*/ return this.source == null ? new Base[0] : new Base[] {this.source}; // UriType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 106079: // key
          this.key = castToId(value); // IdType
          return value;
        case -1619874672: // requirements
          this.requirements = castToString(value); // StringType
          return value;
        case 1478300413: // severity
          value = new ConstraintSeverityEnumFactory().fromType(castToCode(value));
          this.severity = (Enumeration) value; // Enumeration<ConstraintSeverity>
          return value;
        case 99639597: // human
          this.human = castToString(value); // StringType
          return value;
        case -1795452264: // expression
          this.expression = castToString(value); // StringType
          return value;
        case 114256029: // xpath
          this.xpath = castToString(value); // StringType
          return value;
        case -896505829: // source
          this.source = castToUri(value); // UriType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("key")) {
          this.key = castToId(value); // IdType
        } else if (name.equals("requirements")) {
          this.requirements = castToString(value); // StringType
        } else if (name.equals("severity")) {
          value = new ConstraintSeverityEnumFactory().fromType(castToCode(value));
          this.severity = (Enumeration) value; // Enumeration<ConstraintSeverity>
        } else if (name.equals("human")) {
          this.human = castToString(value); // StringType
        } else if (name.equals("expression")) {
          this.expression = castToString(value); // StringType
        } else if (name.equals("xpath")) {
          this.xpath = castToString(value); // StringType
        } else if (name.equals("source")) {
          this.source = castToUri(value); // UriType
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 106079:  return getKeyElement();
        case -1619874672:  return getRequirementsElement();
        case 1478300413:  return getSeverityElement();
        case 99639597:  return getHumanElement();
        case -1795452264:  return getExpressionElement();
        case 114256029:  return getXpathElement();
        case -896505829:  return getSourceElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 106079: /*key*/ return new String[] {"id"};
        case -1619874672: /*requirements*/ return new String[] {"string"};
        case 1478300413: /*severity*/ return new String[] {"code"};
        case 99639597: /*human*/ return new String[] {"string"};
        case -1795452264: /*expression*/ return new String[] {"string"};
        case 114256029: /*xpath*/ return new String[] {"string"};
        case -896505829: /*source*/ return new String[] {"uri"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("key")) {
          throw new FHIRException("Cannot call addChild on a primitive type ElementDefinition.key");
        }
        else if (name.equals("requirements")) {
          throw new FHIRException("Cannot call addChild on a primitive type ElementDefinition.requirements");
        }
        else if (name.equals("severity")) {
          throw new FHIRException("Cannot call addChild on a primitive type ElementDefinition.severity");
        }
        else if (name.equals("human")) {
          throw new FHIRException("Cannot call addChild on a primitive type ElementDefinition.human");
        }
        else if (name.equals("expression")) {
          throw new FHIRException("Cannot call addChild on a primitive type ElementDefinition.expression");
        }
        else if (name.equals("xpath")) {
          throw new FHIRException("Cannot call addChild on a primitive type ElementDefinition.xpath");
        }
        else if (name.equals("source")) {
          throw new FHIRException("Cannot call addChild on a primitive type ElementDefinition.source");
        }
        else
          return super.addChild(name);
      }

      public ElementDefinitionConstraintComponent copy() {
        ElementDefinitionConstraintComponent dst = new ElementDefinitionConstraintComponent();
        copyValues(dst);
        dst.key = key == null ? null : key.copy();
        dst.requirements = requirements == null ? null : requirements.copy();
        dst.severity = severity == null ? null : severity.copy();
        dst.human = human == null ? null : human.copy();
        dst.expression = expression == null ? null : expression.copy();
        dst.xpath = xpath == null ? null : xpath.copy();
        dst.source = source == null ? null : source.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ElementDefinitionConstraintComponent))
          return false;
        ElementDefinitionConstraintComponent o = (ElementDefinitionConstraintComponent) other;
        return compareDeep(key, o.key, true) && compareDeep(requirements, o.requirements, true) && compareDeep(severity, o.severity, true)
           && compareDeep(human, o.human, true) && compareDeep(expression, o.expression, true) && compareDeep(xpath, o.xpath, true)
           && compareDeep(source, o.source, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ElementDefinitionConstraintComponent))
          return false;
        ElementDefinitionConstraintComponent o = (ElementDefinitionConstraintComponent) other;
        return compareValues(key, o.key, true) && compareValues(requirements, o.requirements, true) && compareValues(severity, o.severity, true)
           && compareValues(human, o.human, true) && compareValues(expression, o.expression, true) && compareValues(xpath, o.xpath, true)
           && compareValues(source, o.source, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(key, requirements, severity
          , human, expression, xpath, source);
      }

  public String fhirType() {
    return "ElementDefinition.constraint";

  }

  }

    @Block()
    public static class ElementDefinitionBindingComponent extends Element implements IBaseDatatypeElement {
        /**
         * Indicates the degree of conformance expectations associated with this binding - that is, the degree to which the provided value set must be adhered to in the instances.
         */
        @Child(name = "strength", type = {CodeType.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="required | extensible | preferred | example", formalDefinition="Indicates the degree of conformance expectations associated with this binding - that is, the degree to which the provided value set must be adhered to in the instances." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/binding-strength")
        protected Enumeration<BindingStrength> strength;

        /**
         * Describes the intended use of this particular set of codes.
         */
        @Child(name = "description", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Human explanation of the value set", formalDefinition="Describes the intended use of this particular set of codes." )
        protected StringType description;

        /**
         * Points to the value set or external definition (e.g. implicit value set) that identifies the set of codes to be used. If the binding refers to an explicit value set - the normal case - then use a Reference(ValueSet) preferably containing the canonical URL for the value set. If the reference is to an implicit value set - usually, an IETF RFC that defines a grammar, such as mime types - then use a uri.
         */
        @Child(name = "valueSet", type = {UriType.class, ValueSet.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Source of value set", formalDefinition="Points to the value set or external definition (e.g. implicit value set) that identifies the set of codes to be used. If the binding refers to an explicit value set - the normal case - then use a Reference(ValueSet) preferably containing the canonical URL for the value set. If the reference is to an implicit value set - usually, an IETF RFC that defines a grammar, such as mime types - then use a uri." )
        protected Type valueSet;

        private static final long serialVersionUID = 1355538460L;

    /**
     * Constructor
     */
      public ElementDefinitionBindingComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ElementDefinitionBindingComponent(Enumeration<BindingStrength> strength) {
        super();
        this.strength = strength;
      }

        /**
         * @return {@link #strength} (Indicates the degree of conformance expectations associated with this binding - that is, the degree to which the provided value set must be adhered to in the instances.). This is the underlying object with id, value and extensions. The accessor "getStrength" gives direct access to the value
         */
        public Enumeration<BindingStrength> getStrengthElement() { 
          if (this.strength == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ElementDefinitionBindingComponent.strength");
            else if (Configuration.doAutoCreate())
              this.strength = new Enumeration<BindingStrength>(new BindingStrengthEnumFactory()); // bb
          return this.strength;
        }

        public boolean hasStrengthElement() { 
          return this.strength != null && !this.strength.isEmpty();
        }

        public boolean hasStrength() { 
          return this.strength != null && !this.strength.isEmpty();
        }

        /**
         * @param value {@link #strength} (Indicates the degree of conformance expectations associated with this binding - that is, the degree to which the provided value set must be adhered to in the instances.). This is the underlying object with id, value and extensions. The accessor "getStrength" gives direct access to the value
         */
        public ElementDefinitionBindingComponent setStrengthElement(Enumeration<BindingStrength> value) { 
          this.strength = value;
          return this;
        }

        /**
         * @return Indicates the degree of conformance expectations associated with this binding - that is, the degree to which the provided value set must be adhered to in the instances.
         */
        public BindingStrength getStrength() { 
          return this.strength == null ? null : this.strength.getValue();
        }

        /**
         * @param value Indicates the degree of conformance expectations associated with this binding - that is, the degree to which the provided value set must be adhered to in the instances.
         */
        public ElementDefinitionBindingComponent setStrength(BindingStrength value) { 
            if (this.strength == null)
              this.strength = new Enumeration<BindingStrength>(new BindingStrengthEnumFactory());
            this.strength.setValue(value);
          return this;
        }

        /**
         * @return {@link #description} (Describes the intended use of this particular set of codes.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public StringType getDescriptionElement() { 
          if (this.description == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ElementDefinitionBindingComponent.description");
            else if (Configuration.doAutoCreate())
              this.description = new StringType(); // bb
          return this.description;
        }

        public boolean hasDescriptionElement() { 
          return this.description != null && !this.description.isEmpty();
        }

        public boolean hasDescription() { 
          return this.description != null && !this.description.isEmpty();
        }

        /**
         * @param value {@link #description} (Describes the intended use of this particular set of codes.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public ElementDefinitionBindingComponent setDescriptionElement(StringType value) { 
          this.description = value;
          return this;
        }

        /**
         * @return Describes the intended use of this particular set of codes.
         */
        public String getDescription() { 
          return this.description == null ? null : this.description.getValue();
        }

        /**
         * @param value Describes the intended use of this particular set of codes.
         */
        public ElementDefinitionBindingComponent setDescription(String value) { 
          if (Utilities.noString(value))
            this.description = null;
          else {
            if (this.description == null)
              this.description = new StringType();
            this.description.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #valueSet} (Points to the value set or external definition (e.g. implicit value set) that identifies the set of codes to be used. If the binding refers to an explicit value set - the normal case - then use a Reference(ValueSet) preferably containing the canonical URL for the value set. If the reference is to an implicit value set - usually, an IETF RFC that defines a grammar, such as mime types - then use a uri.)
         */
        public Type getValueSet() { 
          return this.valueSet;
        }

        /**
         * @return {@link #valueSet} (Points to the value set or external definition (e.g. implicit value set) that identifies the set of codes to be used. If the binding refers to an explicit value set - the normal case - then use a Reference(ValueSet) preferably containing the canonical URL for the value set. If the reference is to an implicit value set - usually, an IETF RFC that defines a grammar, such as mime types - then use a uri.)
         */
        public UriType getValueSetUriType() throws FHIRException { 
          if (!(this.valueSet instanceof UriType))
            throw new FHIRException("Type mismatch: the type UriType was expected, but "+this.valueSet.getClass().getName()+" was encountered");
          return (UriType) this.valueSet;
        }

        public boolean hasValueSetUriType() { 
          return this.valueSet instanceof UriType;
        }

        /**
         * @return {@link #valueSet} (Points to the value set or external definition (e.g. implicit value set) that identifies the set of codes to be used. If the binding refers to an explicit value set - the normal case - then use a Reference(ValueSet) preferably containing the canonical URL for the value set. If the reference is to an implicit value set - usually, an IETF RFC that defines a grammar, such as mime types - then use a uri.)
         */
        public Reference getValueSetReference() throws FHIRException { 
          if (!(this.valueSet instanceof Reference))
            throw new FHIRException("Type mismatch: the type Reference was expected, but "+this.valueSet.getClass().getName()+" was encountered");
          return (Reference) this.valueSet;
        }

        public boolean hasValueSetReference() { 
          return this.valueSet instanceof Reference;
        }

        public boolean hasValueSet() { 
          return this.valueSet != null && !this.valueSet.isEmpty();
        }

        /**
         * @param value {@link #valueSet} (Points to the value set or external definition (e.g. implicit value set) that identifies the set of codes to be used. If the binding refers to an explicit value set - the normal case - then use a Reference(ValueSet) preferably containing the canonical URL for the value set. If the reference is to an implicit value set - usually, an IETF RFC that defines a grammar, such as mime types - then use a uri.)
         */
        public ElementDefinitionBindingComponent setValueSet(Type value) { 
          this.valueSet = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("strength", "code", "Indicates the degree of conformance expectations associated with this binding - that is, the degree to which the provided value set must be adhered to in the instances.", 0, java.lang.Integer.MAX_VALUE, strength));
          childrenList.add(new Property("description", "string", "Describes the intended use of this particular set of codes.", 0, java.lang.Integer.MAX_VALUE, description));
          childrenList.add(new Property("valueSet[x]", "uri|Reference(ValueSet)", "Points to the value set or external definition (e.g. implicit value set) that identifies the set of codes to be used. If the binding refers to an explicit value set - the normal case - then use a Reference(ValueSet) preferably containing the canonical URL for the value set. If the reference is to an implicit value set - usually, an IETF RFC that defines a grammar, such as mime types - then use a uri.", 0, java.lang.Integer.MAX_VALUE, valueSet));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 1791316033: /*strength*/ return this.strength == null ? new Base[0] : new Base[] {this.strength}; // Enumeration<BindingStrength>
        case -1724546052: /*description*/ return this.description == null ? new Base[0] : new Base[] {this.description}; // StringType
        case -1410174671: /*valueSet*/ return this.valueSet == null ? new Base[0] : new Base[] {this.valueSet}; // Type
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 1791316033: // strength
          value = new BindingStrengthEnumFactory().fromType(castToCode(value));
          this.strength = (Enumeration) value; // Enumeration<BindingStrength>
          return value;
        case -1724546052: // description
          this.description = castToString(value); // StringType
          return value;
        case -1410174671: // valueSet
          this.valueSet = castToType(value); // Type
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("strength")) {
          value = new BindingStrengthEnumFactory().fromType(castToCode(value));
          this.strength = (Enumeration) value; // Enumeration<BindingStrength>
        } else if (name.equals("description")) {
          this.description = castToString(value); // StringType
        } else if (name.equals("valueSet[x]")) {
          this.valueSet = castToType(value); // Type
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 1791316033:  return getStrengthElement();
        case -1724546052:  return getDescriptionElement();
        case -1438410321:  return getValueSet(); 
        case -1410174671:  return getValueSet(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 1791316033: /*strength*/ return new String[] {"code"};
        case -1724546052: /*description*/ return new String[] {"string"};
        case -1410174671: /*valueSet*/ return new String[] {"uri", "Reference"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("strength")) {
          throw new FHIRException("Cannot call addChild on a primitive type ElementDefinition.strength");
        }
        else if (name.equals("description")) {
          throw new FHIRException("Cannot call addChild on a primitive type ElementDefinition.description");
        }
        else if (name.equals("valueSetUri")) {
          this.valueSet = new UriType();
          return this.valueSet;
        }
        else if (name.equals("valueSetReference")) {
          this.valueSet = new Reference();
          return this.valueSet;
        }
        else
          return super.addChild(name);
      }

      public ElementDefinitionBindingComponent copy() {
        ElementDefinitionBindingComponent dst = new ElementDefinitionBindingComponent();
        copyValues(dst);
        dst.strength = strength == null ? null : strength.copy();
        dst.description = description == null ? null : description.copy();
        dst.valueSet = valueSet == null ? null : valueSet.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ElementDefinitionBindingComponent))
          return false;
        ElementDefinitionBindingComponent o = (ElementDefinitionBindingComponent) other;
        return compareDeep(strength, o.strength, true) && compareDeep(description, o.description, true)
           && compareDeep(valueSet, o.valueSet, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ElementDefinitionBindingComponent))
          return false;
        ElementDefinitionBindingComponent o = (ElementDefinitionBindingComponent) other;
        return compareValues(strength, o.strength, true) && compareValues(description, o.description, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(strength, description, valueSet
          );
      }

  public String fhirType() {
    return "ElementDefinition.binding";

  }

  }

    @Block()
    public static class ElementDefinitionMappingComponent extends Element implements IBaseDatatypeElement {
        /**
         * An internal reference to the definition of a mapping.
         */
        @Child(name = "identity", type = {IdType.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Reference to mapping declaration", formalDefinition="An internal reference to the definition of a mapping." )
        protected IdType identity;

        /**
         * Identifies the computable language in which mapping.map is expressed.
         */
        @Child(name = "language", type = {CodeType.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Computable language of mapping", formalDefinition="Identifies the computable language in which mapping.map is expressed." )
        protected CodeType language;

        /**
         * Expresses what part of the target specification corresponds to this element.
         */
        @Child(name = "map", type = {StringType.class}, order=3, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Details of the mapping", formalDefinition="Expresses what part of the target specification corresponds to this element." )
        protected StringType map;

        /**
         * Comments that provide information about the mapping or its use.
         */
        @Child(name = "comment", type = {StringType.class}, order=4, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Comments about the mapping or its use", formalDefinition="Comments that provide information about the mapping or its use." )
        protected StringType comment;

        private static final long serialVersionUID = 1386816887L;

    /**
     * Constructor
     */
      public ElementDefinitionMappingComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ElementDefinitionMappingComponent(IdType identity, StringType map) {
        super();
        this.identity = identity;
        this.map = map;
      }

        /**
         * @return {@link #identity} (An internal reference to the definition of a mapping.). This is the underlying object with id, value and extensions. The accessor "getIdentity" gives direct access to the value
         */
        public IdType getIdentityElement() { 
          if (this.identity == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ElementDefinitionMappingComponent.identity");
            else if (Configuration.doAutoCreate())
              this.identity = new IdType(); // bb
          return this.identity;
        }

        public boolean hasIdentityElement() { 
          return this.identity != null && !this.identity.isEmpty();
        }

        public boolean hasIdentity() { 
          return this.identity != null && !this.identity.isEmpty();
        }

        /**
         * @param value {@link #identity} (An internal reference to the definition of a mapping.). This is the underlying object with id, value and extensions. The accessor "getIdentity" gives direct access to the value
         */
        public ElementDefinitionMappingComponent setIdentityElement(IdType value) { 
          this.identity = value;
          return this;
        }

        /**
         * @return An internal reference to the definition of a mapping.
         */
        public String getIdentity() { 
          return this.identity == null ? null : this.identity.getValue();
        }

        /**
         * @param value An internal reference to the definition of a mapping.
         */
        public ElementDefinitionMappingComponent setIdentity(String value) { 
            if (this.identity == null)
              this.identity = new IdType();
            this.identity.setValue(value);
          return this;
        }

        /**
         * @return {@link #language} (Identifies the computable language in which mapping.map is expressed.). This is the underlying object with id, value and extensions. The accessor "getLanguage" gives direct access to the value
         */
        public CodeType getLanguageElement() { 
          if (this.language == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ElementDefinitionMappingComponent.language");
            else if (Configuration.doAutoCreate())
              this.language = new CodeType(); // bb
          return this.language;
        }

        public boolean hasLanguageElement() { 
          return this.language != null && !this.language.isEmpty();
        }

        public boolean hasLanguage() { 
          return this.language != null && !this.language.isEmpty();
        }

        /**
         * @param value {@link #language} (Identifies the computable language in which mapping.map is expressed.). This is the underlying object with id, value and extensions. The accessor "getLanguage" gives direct access to the value
         */
        public ElementDefinitionMappingComponent setLanguageElement(CodeType value) { 
          this.language = value;
          return this;
        }

        /**
         * @return Identifies the computable language in which mapping.map is expressed.
         */
        public String getLanguage() { 
          return this.language == null ? null : this.language.getValue();
        }

        /**
         * @param value Identifies the computable language in which mapping.map is expressed.
         */
        public ElementDefinitionMappingComponent setLanguage(String value) { 
          if (Utilities.noString(value))
            this.language = null;
          else {
            if (this.language == null)
              this.language = new CodeType();
            this.language.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #map} (Expresses what part of the target specification corresponds to this element.). This is the underlying object with id, value and extensions. The accessor "getMap" gives direct access to the value
         */
        public StringType getMapElement() { 
          if (this.map == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ElementDefinitionMappingComponent.map");
            else if (Configuration.doAutoCreate())
              this.map = new StringType(); // bb
          return this.map;
        }

        public boolean hasMapElement() { 
          return this.map != null && !this.map.isEmpty();
        }

        public boolean hasMap() { 
          return this.map != null && !this.map.isEmpty();
        }

        /**
         * @param value {@link #map} (Expresses what part of the target specification corresponds to this element.). This is the underlying object with id, value and extensions. The accessor "getMap" gives direct access to the value
         */
        public ElementDefinitionMappingComponent setMapElement(StringType value) { 
          this.map = value;
          return this;
        }

        /**
         * @return Expresses what part of the target specification corresponds to this element.
         */
        public String getMap() { 
          return this.map == null ? null : this.map.getValue();
        }

        /**
         * @param value Expresses what part of the target specification corresponds to this element.
         */
        public ElementDefinitionMappingComponent setMap(String value) { 
            if (this.map == null)
              this.map = new StringType();
            this.map.setValue(value);
          return this;
        }

        /**
         * @return {@link #comment} (Comments that provide information about the mapping or its use.). This is the underlying object with id, value and extensions. The accessor "getComment" gives direct access to the value
         */
        public StringType getCommentElement() { 
          if (this.comment == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ElementDefinitionMappingComponent.comment");
            else if (Configuration.doAutoCreate())
              this.comment = new StringType(); // bb
          return this.comment;
        }

        public boolean hasCommentElement() { 
          return this.comment != null && !this.comment.isEmpty();
        }

        public boolean hasComment() { 
          return this.comment != null && !this.comment.isEmpty();
        }

        /**
         * @param value {@link #comment} (Comments that provide information about the mapping or its use.). This is the underlying object with id, value and extensions. The accessor "getComment" gives direct access to the value
         */
        public ElementDefinitionMappingComponent setCommentElement(StringType value) { 
          this.comment = value;
          return this;
        }

        /**
         * @return Comments that provide information about the mapping or its use.
         */
        public String getComment() { 
          return this.comment == null ? null : this.comment.getValue();
        }

        /**
         * @param value Comments that provide information about the mapping or its use.
         */
        public ElementDefinitionMappingComponent setComment(String value) { 
          if (Utilities.noString(value))
            this.comment = null;
          else {
            if (this.comment == null)
              this.comment = new StringType();
            this.comment.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("identity", "id", "An internal reference to the definition of a mapping.", 0, java.lang.Integer.MAX_VALUE, identity));
          childrenList.add(new Property("language", "code", "Identifies the computable language in which mapping.map is expressed.", 0, java.lang.Integer.MAX_VALUE, language));
          childrenList.add(new Property("map", "string", "Expresses what part of the target specification corresponds to this element.", 0, java.lang.Integer.MAX_VALUE, map));
          childrenList.add(new Property("comment", "string", "Comments that provide information about the mapping or its use.", 0, java.lang.Integer.MAX_VALUE, comment));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -135761730: /*identity*/ return this.identity == null ? new Base[0] : new Base[] {this.identity}; // IdType
        case -1613589672: /*language*/ return this.language == null ? new Base[0] : new Base[] {this.language}; // CodeType
        case 107868: /*map*/ return this.map == null ? new Base[0] : new Base[] {this.map}; // StringType
        case 950398559: /*comment*/ return this.comment == null ? new Base[0] : new Base[] {this.comment}; // StringType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -135761730: // identity
          this.identity = castToId(value); // IdType
          return value;
        case -1613589672: // language
          this.language = castToCode(value); // CodeType
          return value;
        case 107868: // map
          this.map = castToString(value); // StringType
          return value;
        case 950398559: // comment
          this.comment = castToString(value); // StringType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identity")) {
          this.identity = castToId(value); // IdType
        } else if (name.equals("language")) {
          this.language = castToCode(value); // CodeType
        } else if (name.equals("map")) {
          this.map = castToString(value); // StringType
        } else if (name.equals("comment")) {
          this.comment = castToString(value); // StringType
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -135761730:  return getIdentityElement();
        case -1613589672:  return getLanguageElement();
        case 107868:  return getMapElement();
        case 950398559:  return getCommentElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -135761730: /*identity*/ return new String[] {"id"};
        case -1613589672: /*language*/ return new String[] {"code"};
        case 107868: /*map*/ return new String[] {"string"};
        case 950398559: /*comment*/ return new String[] {"string"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identity")) {
          throw new FHIRException("Cannot call addChild on a primitive type ElementDefinition.identity");
        }
        else if (name.equals("language")) {
          throw new FHIRException("Cannot call addChild on a primitive type ElementDefinition.language");
        }
        else if (name.equals("map")) {
          throw new FHIRException("Cannot call addChild on a primitive type ElementDefinition.map");
        }
        else if (name.equals("comment")) {
          throw new FHIRException("Cannot call addChild on a primitive type ElementDefinition.comment");
        }
        else
          return super.addChild(name);
      }

      public ElementDefinitionMappingComponent copy() {
        ElementDefinitionMappingComponent dst = new ElementDefinitionMappingComponent();
        copyValues(dst);
        dst.identity = identity == null ? null : identity.copy();
        dst.language = language == null ? null : language.copy();
        dst.map = map == null ? null : map.copy();
        dst.comment = comment == null ? null : comment.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ElementDefinitionMappingComponent))
          return false;
        ElementDefinitionMappingComponent o = (ElementDefinitionMappingComponent) other;
        return compareDeep(identity, o.identity, true) && compareDeep(language, o.language, true) && compareDeep(map, o.map, true)
           && compareDeep(comment, o.comment, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ElementDefinitionMappingComponent))
          return false;
        ElementDefinitionMappingComponent o = (ElementDefinitionMappingComponent) other;
        return compareValues(identity, o.identity, true) && compareValues(language, o.language, true) && compareValues(map, o.map, true)
           && compareValues(comment, o.comment, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(identity, language, map
          , comment);
      }

  public String fhirType() {
    return "ElementDefinition.mapping";

  }

  }

    /**
     * The path identifies the element and is expressed as a "."-separated list of ancestor elements, beginning with the name of the resource or extension.
     */
    @Child(name = "path", type = {StringType.class}, order=0, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Path of the element in the hierarchy of elements", formalDefinition="The path identifies the element and is expressed as a \".\"-separated list of ancestor elements, beginning with the name of the resource or extension." )
    protected StringType path;

    /**
     * Codes that define how this element is represented in instances, when the deviation varies from the normal case.
     */
    @Child(name = "representation", type = {CodeType.class}, order=1, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="xmlAttr | xmlText | typeAttr | cdaText | xhtml", formalDefinition="Codes that define how this element is represented in instances, when the deviation varies from the normal case." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/property-representation")
    protected List<Enumeration<PropertyRepresentation>> representation;

    /**
     * The name of this element definition slice, when slicing is working. The name must be a token with no dots or spaces. This is a unique name referring to a specific set of constraints applied to this element, used to provide a name to different slices of the same element.
     */
    @Child(name = "sliceName", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Name for this particular element (in a set of slices)", formalDefinition="The name of this element definition slice, when slicing is working. The name must be a token with no dots or spaces. This is a unique name referring to a specific set of constraints applied to this element, used to provide a name to different slices of the same element." )
    protected StringType sliceName;

    /**
     * A single preferred label which is the text to display beside the element indicating its meaning or to use to prompt for the element in a user display or form.
     */
    @Child(name = "label", type = {StringType.class}, order=3, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Name for element to display with or prompt for element", formalDefinition="A single preferred label which is the text to display beside the element indicating its meaning or to use to prompt for the element in a user display or form." )
    protected StringType label;

    /**
     * A code that has the same meaning as the element in a particular terminology.
     */
    @Child(name = "code", type = {Coding.class}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Corresponding codes in terminologies", formalDefinition="A code that has the same meaning as the element in a particular terminology." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/observation-codes")
    protected List<Coding> code;

    /**
     * Indicates that the element is sliced into a set of alternative definitions (i.e. in a structure definition, there are multiple different constraints on a single element in the base resource). Slicing can be used in any resource that has cardinality ..* on the base resource, or any resource with a choice of types. The set of slices is any elements that come after this in the element sequence that have the same path, until a shorter path occurs (the shorter path terminates the set).
     */
    @Child(name = "slicing", type = {}, order=5, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="This element is sliced - slices follow", formalDefinition="Indicates that the element is sliced into a set of alternative definitions (i.e. in a structure definition, there are multiple different constraints on a single element in the base resource). Slicing can be used in any resource that has cardinality ..* on the base resource, or any resource with a choice of types. The set of slices is any elements that come after this in the element sequence that have the same path, until a shorter path occurs (the shorter path terminates the set)." )
    protected ElementDefinitionSlicingComponent slicing;

    /**
     * A concise description of what this element means (e.g. for use in autogenerated summaries).
     */
    @Child(name = "short", type = {StringType.class}, order=6, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Concise definition for space-constrained presentation", formalDefinition="A concise description of what this element means (e.g. for use in autogenerated summaries)." )
    protected StringType short_;

    /**
     * Provides a complete explanation of the meaning of the data element for human readability.  For the case of elements derived from existing elements (e.g. constraints), the definition SHALL be consistent with the base definition, but convey the meaning of the element in the particular context of use of the resource.
     */
    @Child(name = "definition", type = {MarkdownType.class}, order=7, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Full formal definition as narrative text", formalDefinition="Provides a complete explanation of the meaning of the data element for human readability.  For the case of elements derived from existing elements (e.g. constraints), the definition SHALL be consistent with the base definition, but convey the meaning of the element in the particular context of use of the resource." )
    protected MarkdownType definition;

    /**
     * Explanatory notes and implementation guidance about the data element, including notes about how to use the data properly, exceptions to proper use, etc.
     */
    @Child(name = "comment", type = {MarkdownType.class}, order=8, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Comments about the use of this element", formalDefinition="Explanatory notes and implementation guidance about the data element, including notes about how to use the data properly, exceptions to proper use, etc." )
    protected MarkdownType comment;

    /**
     * This element is for traceability of why the element was created and why the constraints exist as they do. This may be used to point to source materials or specifications that drove the structure of this element.
     */
    @Child(name = "requirements", type = {MarkdownType.class}, order=9, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Why this resource has been created", formalDefinition="This element is for traceability of why the element was created and why the constraints exist as they do. This may be used to point to source materials or specifications that drove the structure of this element." )
    protected MarkdownType requirements;

    /**
     * Identifies additional names by which this element might also be known.
     */
    @Child(name = "alias", type = {StringType.class}, order=10, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Other names", formalDefinition="Identifies additional names by which this element might also be known." )
    protected List<StringType> alias;

    /**
     * The minimum number of times this element SHALL appear in the instance.
     */
    @Child(name = "min", type = {UnsignedIntType.class}, order=11, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Minimum Cardinality", formalDefinition="The minimum number of times this element SHALL appear in the instance." )
    protected UnsignedIntType min;

    /**
     * The maximum number of times this element is permitted to appear in the instance.
     */
    @Child(name = "max", type = {StringType.class}, order=12, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Maximum Cardinality (a number or *)", formalDefinition="The maximum number of times this element is permitted to appear in the instance." )
    protected StringType max;

    /**
     * Information about the base definition of the element, provided to make it unnecessary for tools to trace the deviation of the element through the derived and related profiles. This information is provided when the element definition is not the original definition of an element - i.g. either in a constraint on another type, or for elements from a super type in a snap shot.
     */
    @Child(name = "base", type = {}, order=13, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Base definition information for tools", formalDefinition="Information about the base definition of the element, provided to make it unnecessary for tools to trace the deviation of the element through the derived and related profiles. This information is provided when the element definition is not the original definition of an element - i.g. either in a constraint on another type, or for elements from a super type in a snap shot." )
    protected ElementDefinitionBaseComponent base;

    /**
     * Identifies the identity of an element defined elsewhere in the profile whose content rules should be applied to the current element.
     */
    @Child(name = "contentReference", type = {UriType.class}, order=14, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Reference to definition of content for the element", formalDefinition="Identifies the identity of an element defined elsewhere in the profile whose content rules should be applied to the current element." )
    protected UriType contentReference;

    /**
     * The data type or resource that the value of this element is permitted to be.
     */
    @Child(name = "type", type = {}, order=15, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Data type and Profile for this element", formalDefinition="The data type or resource that the value of this element is permitted to be." )
    protected List<TypeRefComponent> type;

    /**
     * The value that should be used if there is no value stated in the instance (e.g. 'if not otherwise specified, the abstract is false').
     */
    @Child(name = "defaultValue", type = {}, order=16, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Specified value if missing from instance", formalDefinition="The value that should be used if there is no value stated in the instance (e.g. 'if not otherwise specified, the abstract is false')." )
    protected org.hl7.fhir.dstu3.model.Type defaultValue;

    /**
     * The Implicit meaning that is to be understood when this element is missing (e.g. 'when this element is missing, the period is ongoing'.
     */
    @Child(name = "meaningWhenMissing", type = {MarkdownType.class}, order=17, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Implicit meaning when this element is missing", formalDefinition="The Implicit meaning that is to be understood when this element is missing (e.g. 'when this element is missing, the period is ongoing'." )
    protected MarkdownType meaningWhenMissing;

    /**
     * If present, indicates that the order of the repeating element has meaning and describes what that meaning is.  If absent, it means that the order of the element has no meaning.
     */
    @Child(name = "orderMeaning", type = {StringType.class}, order=18, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="What the order of the elements means", formalDefinition="If present, indicates that the order of the repeating element has meaning and describes what that meaning is.  If absent, it means that the order of the element has no meaning." )
    protected StringType orderMeaning;

    /**
     * Specifies a value that SHALL be exactly the value  for this element in the instance. For purposes of comparison, non-significant whitespace is ignored, and all values must be an exact match (case and accent sensitive). Missing elements/attributes must also be missing.
     */
    @Child(name = "fixed", type = {}, order=19, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Value must be exactly this", formalDefinition="Specifies a value that SHALL be exactly the value  for this element in the instance. For purposes of comparison, non-significant whitespace is ignored, and all values must be an exact match (case and accent sensitive). Missing elements/attributes must also be missing." )
    protected org.hl7.fhir.dstu3.model.Type fixed;

    /**
     * Specifies a value that the value in the instance SHALL follow - that is, any value in the pattern must be found in the instance. Other additional values may be found too. This is effectively constraint by example.  The values of elements present in the pattern must match exactly (case-sensitive, accent-sensitive, etc.).
     */
    @Child(name = "pattern", type = {}, order=20, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Value must have at least these property values", formalDefinition="Specifies a value that the value in the instance SHALL follow - that is, any value in the pattern must be found in the instance. Other additional values may be found too. This is effectively constraint by example.  The values of elements present in the pattern must match exactly (case-sensitive, accent-sensitive, etc.)." )
    protected org.hl7.fhir.dstu3.model.Type pattern;

    /**
     * A sample value for this element demonstrating the type of information that would typically be found in the element.
     */
    @Child(name = "example", type = {}, order=21, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Example value (as defined for type)", formalDefinition="A sample value for this element demonstrating the type of information that would typically be found in the element." )
    protected List<ElementDefinitionExampleComponent> example;

    /**
     * The minimum allowed value for the element. The value is inclusive. This is allowed for the types date, dateTime, instant, time, decimal, integer, and Quantity.
     */
    @Child(name = "minValue", type = {DateType.class, DateTimeType.class, InstantType.class, TimeType.class, DecimalType.class, IntegerType.class, PositiveIntType.class, UnsignedIntType.class, Quantity.class}, order=22, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Minimum Allowed Value (for some types)", formalDefinition="The minimum allowed value for the element. The value is inclusive. This is allowed for the types date, dateTime, instant, time, decimal, integer, and Quantity." )
    protected Type minValue;

    /**
     * The maximum allowed value for the element. The value is inclusive. This is allowed for the types date, dateTime, instant, time, decimal, integer, and Quantity.
     */
    @Child(name = "maxValue", type = {DateType.class, DateTimeType.class, InstantType.class, TimeType.class, DecimalType.class, IntegerType.class, PositiveIntType.class, UnsignedIntType.class, Quantity.class}, order=23, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Maximum Allowed Value (for some types)", formalDefinition="The maximum allowed value for the element. The value is inclusive. This is allowed for the types date, dateTime, instant, time, decimal, integer, and Quantity." )
    protected Type maxValue;

    /**
     * Indicates the maximum length in characters that is permitted to be present in conformant instances and which is expected to be supported by conformant consumers that support the element.
     */
    @Child(name = "maxLength", type = {IntegerType.class}, order=24, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Max length for strings", formalDefinition="Indicates the maximum length in characters that is permitted to be present in conformant instances and which is expected to be supported by conformant consumers that support the element." )
    protected IntegerType maxLength;

    /**
     * A reference to an invariant that may make additional statements about the cardinality or value in the instance.
     */
    @Child(name = "condition", type = {IdType.class}, order=25, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Reference to invariant about presence", formalDefinition="A reference to an invariant that may make additional statements about the cardinality or value in the instance." )
    protected List<IdType> condition;

    /**
     * Formal constraints such as co-occurrence and other constraints that can be computationally evaluated within the context of the instance.
     */
    @Child(name = "constraint", type = {}, order=26, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Condition that must evaluate to true", formalDefinition="Formal constraints such as co-occurrence and other constraints that can be computationally evaluated within the context of the instance." )
    protected List<ElementDefinitionConstraintComponent> constraint;

    /**
     * If true, implementations that produce or consume resources SHALL provide "support" for the element in some meaningful way.  If false, the element may be ignored and not supported.
     */
    @Child(name = "mustSupport", type = {BooleanType.class}, order=27, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="If the element must supported", formalDefinition="If true, implementations that produce or consume resources SHALL provide \"support\" for the element in some meaningful way.  If false, the element may be ignored and not supported." )
    protected BooleanType mustSupport;

    /**
     * If true, the value of this element affects the interpretation of the element or resource that contains it, and the value of the element cannot be ignored. Typically, this is used for status, negation and qualification codes. The effect of this is that the element cannot be ignored by systems: they SHALL either recognize the element and process it, and/or a pre-determination has been made that it is not relevant to their particular system.
     */
    @Child(name = "isModifier", type = {BooleanType.class}, order=28, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="If this modifies the meaning of other elements", formalDefinition="If true, the value of this element affects the interpretation of the element or resource that contains it, and the value of the element cannot be ignored. Typically, this is used for status, negation and qualification codes. The effect of this is that the element cannot be ignored by systems: they SHALL either recognize the element and process it, and/or a pre-determination has been made that it is not relevant to their particular system." )
    protected BooleanType isModifier;

    /**
     * Whether the element should be included if a client requests a search with the parameter _summary=true.
     */
    @Child(name = "isSummary", type = {BooleanType.class}, order=29, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Include when _summary = true?", formalDefinition="Whether the element should be included if a client requests a search with the parameter _summary=true." )
    protected BooleanType isSummary;

    /**
     * Binds to a value set if this element is coded (code, Coding, CodeableConcept, Quantity), or the data types (string, uri).
     */
    @Child(name = "binding", type = {}, order=30, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="ValueSet details if this is coded", formalDefinition="Binds to a value set if this element is coded (code, Coding, CodeableConcept, Quantity), or the data types (string, uri)." )
    protected ElementDefinitionBindingComponent binding;

    /**
     * Identifies a concept from an external specification that roughly corresponds to this element.
     */
    @Child(name = "mapping", type = {}, order=31, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Map element to another set of definitions", formalDefinition="Identifies a concept from an external specification that roughly corresponds to this element." )
    protected List<ElementDefinitionMappingComponent> mapping;

    private static final long serialVersionUID = -278262340L;

  /**
   * Constructor
   */
    public ElementDefinition() {
      super();
    }

  /**
   * Constructor
   */
    public ElementDefinition(StringType path) {
      super();
      this.path = path;
    }

    /**
     * @return {@link #path} (The path identifies the element and is expressed as a "."-separated list of ancestor elements, beginning with the name of the resource or extension.). This is the underlying object with id, value and extensions. The accessor "getPath" gives direct access to the value
     */
    public StringType getPathElement() { 
      if (this.path == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ElementDefinition.path");
        else if (Configuration.doAutoCreate())
          this.path = new StringType(); // bb
      return this.path;
    }

    public boolean hasPathElement() { 
      return this.path != null && !this.path.isEmpty();
    }

    public boolean hasPath() { 
      return this.path != null && !this.path.isEmpty();
    }

    /**
     * @param value {@link #path} (The path identifies the element and is expressed as a "."-separated list of ancestor elements, beginning with the name of the resource or extension.). This is the underlying object with id, value and extensions. The accessor "getPath" gives direct access to the value
     */
    public ElementDefinition setPathElement(StringType value) { 
      this.path = value;
      return this;
    }

    /**
     * @return The path identifies the element and is expressed as a "."-separated list of ancestor elements, beginning with the name of the resource or extension.
     */
    public String getPath() { 
      return this.path == null ? null : this.path.getValue();
    }

    /**
     * @param value The path identifies the element and is expressed as a "."-separated list of ancestor elements, beginning with the name of the resource or extension.
     */
    public ElementDefinition setPath(String value) { 
        if (this.path == null)
          this.path = new StringType();
        this.path.setValue(value);
      return this;
    }

    /**
     * @return {@link #representation} (Codes that define how this element is represented in instances, when the deviation varies from the normal case.)
     */
    public List<Enumeration<PropertyRepresentation>> getRepresentation() { 
      if (this.representation == null)
        this.representation = new ArrayList<Enumeration<PropertyRepresentation>>();
      return this.representation;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ElementDefinition setRepresentation(List<Enumeration<PropertyRepresentation>> theRepresentation) { 
      this.representation = theRepresentation;
      return this;
    }

    public boolean hasRepresentation() { 
      if (this.representation == null)
        return false;
      for (Enumeration<PropertyRepresentation> item : this.representation)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #representation} (Codes that define how this element is represented in instances, when the deviation varies from the normal case.)
     */
    public Enumeration<PropertyRepresentation> addRepresentationElement() {//2 
      Enumeration<PropertyRepresentation> t = new Enumeration<PropertyRepresentation>(new PropertyRepresentationEnumFactory());
      if (this.representation == null)
        this.representation = new ArrayList<Enumeration<PropertyRepresentation>>();
      this.representation.add(t);
      return t;
    }

    /**
     * @param value {@link #representation} (Codes that define how this element is represented in instances, when the deviation varies from the normal case.)
     */
    public ElementDefinition addRepresentation(PropertyRepresentation value) { //1
      Enumeration<PropertyRepresentation> t = new Enumeration<PropertyRepresentation>(new PropertyRepresentationEnumFactory());
      t.setValue(value);
      if (this.representation == null)
        this.representation = new ArrayList<Enumeration<PropertyRepresentation>>();
      this.representation.add(t);
      return this;
    }

    /**
     * @param value {@link #representation} (Codes that define how this element is represented in instances, when the deviation varies from the normal case.)
     */
    public boolean hasRepresentation(PropertyRepresentation value) { 
      if (this.representation == null)
        return false;
      for (Enumeration<PropertyRepresentation> v : this.representation)
        if (v.getValue().equals(value)) // code
          return true;
      return false;
    }

    /**
     * @return {@link #sliceName} (The name of this element definition slice, when slicing is working. The name must be a token with no dots or spaces. This is a unique name referring to a specific set of constraints applied to this element, used to provide a name to different slices of the same element.). This is the underlying object with id, value and extensions. The accessor "getSliceName" gives direct access to the value
     */
    public StringType getSliceNameElement() { 
      if (this.sliceName == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ElementDefinition.sliceName");
        else if (Configuration.doAutoCreate())
          this.sliceName = new StringType(); // bb
      return this.sliceName;
    }

    public boolean hasSliceNameElement() { 
      return this.sliceName != null && !this.sliceName.isEmpty();
    }

    public boolean hasSliceName() { 
      return this.sliceName != null && !this.sliceName.isEmpty();
    }

    /**
     * @param value {@link #sliceName} (The name of this element definition slice, when slicing is working. The name must be a token with no dots or spaces. This is a unique name referring to a specific set of constraints applied to this element, used to provide a name to different slices of the same element.). This is the underlying object with id, value and extensions. The accessor "getSliceName" gives direct access to the value
     */
    public ElementDefinition setSliceNameElement(StringType value) { 
      this.sliceName = value;
      return this;
    }

    /**
     * @return The name of this element definition slice, when slicing is working. The name must be a token with no dots or spaces. This is a unique name referring to a specific set of constraints applied to this element, used to provide a name to different slices of the same element.
     */
    public String getSliceName() { 
      return this.sliceName == null ? null : this.sliceName.getValue();
    }

    /**
     * @param value The name of this element definition slice, when slicing is working. The name must be a token with no dots or spaces. This is a unique name referring to a specific set of constraints applied to this element, used to provide a name to different slices of the same element.
     */
    public ElementDefinition setSliceName(String value) { 
      if (Utilities.noString(value))
        this.sliceName = null;
      else {
        if (this.sliceName == null)
          this.sliceName = new StringType();
        this.sliceName.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #label} (A single preferred label which is the text to display beside the element indicating its meaning or to use to prompt for the element in a user display or form.). This is the underlying object with id, value and extensions. The accessor "getLabel" gives direct access to the value
     */
    public StringType getLabelElement() { 
      if (this.label == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ElementDefinition.label");
        else if (Configuration.doAutoCreate())
          this.label = new StringType(); // bb
      return this.label;
    }

    public boolean hasLabelElement() { 
      return this.label != null && !this.label.isEmpty();
    }

    public boolean hasLabel() { 
      return this.label != null && !this.label.isEmpty();
    }

    /**
     * @param value {@link #label} (A single preferred label which is the text to display beside the element indicating its meaning or to use to prompt for the element in a user display or form.). This is the underlying object with id, value and extensions. The accessor "getLabel" gives direct access to the value
     */
    public ElementDefinition setLabelElement(StringType value) { 
      this.label = value;
      return this;
    }

    /**
     * @return A single preferred label which is the text to display beside the element indicating its meaning or to use to prompt for the element in a user display or form.
     */
    public String getLabel() { 
      return this.label == null ? null : this.label.getValue();
    }

    /**
     * @param value A single preferred label which is the text to display beside the element indicating its meaning or to use to prompt for the element in a user display or form.
     */
    public ElementDefinition setLabel(String value) { 
      if (Utilities.noString(value))
        this.label = null;
      else {
        if (this.label == null)
          this.label = new StringType();
        this.label.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #code} (A code that has the same meaning as the element in a particular terminology.)
     */
    public List<Coding> getCode() { 
      if (this.code == null)
        this.code = new ArrayList<Coding>();
      return this.code;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ElementDefinition setCode(List<Coding> theCode) { 
      this.code = theCode;
      return this;
    }

    public boolean hasCode() { 
      if (this.code == null)
        return false;
      for (Coding item : this.code)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Coding addCode() { //3
      Coding t = new Coding();
      if (this.code == null)
        this.code = new ArrayList<Coding>();
      this.code.add(t);
      return t;
    }

    public ElementDefinition addCode(Coding t) { //3
      if (t == null)
        return this;
      if (this.code == null)
        this.code = new ArrayList<Coding>();
      this.code.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #code}, creating it if it does not already exist
     */
    public Coding getCodeFirstRep() { 
      if (getCode().isEmpty()) {
        addCode();
      }
      return getCode().get(0);
    }

    /**
     * @return {@link #slicing} (Indicates that the element is sliced into a set of alternative definitions (i.e. in a structure definition, there are multiple different constraints on a single element in the base resource). Slicing can be used in any resource that has cardinality ..* on the base resource, or any resource with a choice of types. The set of slices is any elements that come after this in the element sequence that have the same path, until a shorter path occurs (the shorter path terminates the set).)
     */
    public ElementDefinitionSlicingComponent getSlicing() { 
      if (this.slicing == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ElementDefinition.slicing");
        else if (Configuration.doAutoCreate())
          this.slicing = new ElementDefinitionSlicingComponent(); // cc
      return this.slicing;
    }

    public boolean hasSlicing() { 
      return this.slicing != null && !this.slicing.isEmpty();
    }

    /**
     * @param value {@link #slicing} (Indicates that the element is sliced into a set of alternative definitions (i.e. in a structure definition, there are multiple different constraints on a single element in the base resource). Slicing can be used in any resource that has cardinality ..* on the base resource, or any resource with a choice of types. The set of slices is any elements that come after this in the element sequence that have the same path, until a shorter path occurs (the shorter path terminates the set).)
     */
    public ElementDefinition setSlicing(ElementDefinitionSlicingComponent value) { 
      this.slicing = value;
      return this;
    }

    /**
     * @return {@link #short_} (A concise description of what this element means (e.g. for use in autogenerated summaries).). This is the underlying object with id, value and extensions. The accessor "getShort" gives direct access to the value
     */
    public StringType getShortElement() { 
      if (this.short_ == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ElementDefinition.short_");
        else if (Configuration.doAutoCreate())
          this.short_ = new StringType(); // bb
      return this.short_;
    }

    public boolean hasShortElement() { 
      return this.short_ != null && !this.short_.isEmpty();
    }

    public boolean hasShort() { 
      return this.short_ != null && !this.short_.isEmpty();
    }

    /**
     * @param value {@link #short_} (A concise description of what this element means (e.g. for use in autogenerated summaries).). This is the underlying object with id, value and extensions. The accessor "getShort" gives direct access to the value
     */
    public ElementDefinition setShortElement(StringType value) { 
      this.short_ = value;
      return this;
    }

    /**
     * @return A concise description of what this element means (e.g. for use in autogenerated summaries).
     */
    public String getShort() { 
      return this.short_ == null ? null : this.short_.getValue();
    }

    /**
     * @param value A concise description of what this element means (e.g. for use in autogenerated summaries).
     */
    public ElementDefinition setShort(String value) { 
      if (Utilities.noString(value))
        this.short_ = null;
      else {
        if (this.short_ == null)
          this.short_ = new StringType();
        this.short_.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #definition} (Provides a complete explanation of the meaning of the data element for human readability.  For the case of elements derived from existing elements (e.g. constraints), the definition SHALL be consistent with the base definition, but convey the meaning of the element in the particular context of use of the resource.). This is the underlying object with id, value and extensions. The accessor "getDefinition" gives direct access to the value
     */
    public MarkdownType getDefinitionElement() { 
      if (this.definition == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ElementDefinition.definition");
        else if (Configuration.doAutoCreate())
          this.definition = new MarkdownType(); // bb
      return this.definition;
    }

    public boolean hasDefinitionElement() { 
      return this.definition != null && !this.definition.isEmpty();
    }

    public boolean hasDefinition() { 
      return this.definition != null && !this.definition.isEmpty();
    }

    /**
     * @param value {@link #definition} (Provides a complete explanation of the meaning of the data element for human readability.  For the case of elements derived from existing elements (e.g. constraints), the definition SHALL be consistent with the base definition, but convey the meaning of the element in the particular context of use of the resource.). This is the underlying object with id, value and extensions. The accessor "getDefinition" gives direct access to the value
     */
    public ElementDefinition setDefinitionElement(MarkdownType value) { 
      this.definition = value;
      return this;
    }

    /**
     * @return Provides a complete explanation of the meaning of the data element for human readability.  For the case of elements derived from existing elements (e.g. constraints), the definition SHALL be consistent with the base definition, but convey the meaning of the element in the particular context of use of the resource.
     */
    public String getDefinition() { 
      return this.definition == null ? null : this.definition.getValue();
    }

    /**
     * @param value Provides a complete explanation of the meaning of the data element for human readability.  For the case of elements derived from existing elements (e.g. constraints), the definition SHALL be consistent with the base definition, but convey the meaning of the element in the particular context of use of the resource.
     */
    public ElementDefinition setDefinition(String value) { 
      if (value == null)
        this.definition = null;
      else {
        if (this.definition == null)
          this.definition = new MarkdownType();
        this.definition.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #comment} (Explanatory notes and implementation guidance about the data element, including notes about how to use the data properly, exceptions to proper use, etc.). This is the underlying object with id, value and extensions. The accessor "getComment" gives direct access to the value
     */
    public MarkdownType getCommentElement() { 
      if (this.comment == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ElementDefinition.comment");
        else if (Configuration.doAutoCreate())
          this.comment = new MarkdownType(); // bb
      return this.comment;
    }

    public boolean hasCommentElement() { 
      return this.comment != null && !this.comment.isEmpty();
    }

    public boolean hasComment() { 
      return this.comment != null && !this.comment.isEmpty();
    }

    /**
     * @param value {@link #comment} (Explanatory notes and implementation guidance about the data element, including notes about how to use the data properly, exceptions to proper use, etc.). This is the underlying object with id, value and extensions. The accessor "getComment" gives direct access to the value
     */
    public ElementDefinition setCommentElement(MarkdownType value) { 
      this.comment = value;
      return this;
    }

    /**
     * @return Explanatory notes and implementation guidance about the data element, including notes about how to use the data properly, exceptions to proper use, etc.
     */
    public String getComment() { 
      return this.comment == null ? null : this.comment.getValue();
    }

    /**
     * @param value Explanatory notes and implementation guidance about the data element, including notes about how to use the data properly, exceptions to proper use, etc.
     */
    public ElementDefinition setComment(String value) { 
      if (value == null)
        this.comment = null;
      else {
        if (this.comment == null)
          this.comment = new MarkdownType();
        this.comment.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #requirements} (This element is for traceability of why the element was created and why the constraints exist as they do. This may be used to point to source materials or specifications that drove the structure of this element.). This is the underlying object with id, value and extensions. The accessor "getRequirements" gives direct access to the value
     */
    public MarkdownType getRequirementsElement() { 
      if (this.requirements == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ElementDefinition.requirements");
        else if (Configuration.doAutoCreate())
          this.requirements = new MarkdownType(); // bb
      return this.requirements;
    }

    public boolean hasRequirementsElement() { 
      return this.requirements != null && !this.requirements.isEmpty();
    }

    public boolean hasRequirements() { 
      return this.requirements != null && !this.requirements.isEmpty();
    }

    /**
     * @param value {@link #requirements} (This element is for traceability of why the element was created and why the constraints exist as they do. This may be used to point to source materials or specifications that drove the structure of this element.). This is the underlying object with id, value and extensions. The accessor "getRequirements" gives direct access to the value
     */
    public ElementDefinition setRequirementsElement(MarkdownType value) { 
      this.requirements = value;
      return this;
    }

    /**
     * @return This element is for traceability of why the element was created and why the constraints exist as they do. This may be used to point to source materials or specifications that drove the structure of this element.
     */
    public String getRequirements() { 
      return this.requirements == null ? null : this.requirements.getValue();
    }

    /**
     * @param value This element is for traceability of why the element was created and why the constraints exist as they do. This may be used to point to source materials or specifications that drove the structure of this element.
     */
    public ElementDefinition setRequirements(String value) { 
      if (value == null)
        this.requirements = null;
      else {
        if (this.requirements == null)
          this.requirements = new MarkdownType();
        this.requirements.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #alias} (Identifies additional names by which this element might also be known.)
     */
    public List<StringType> getAlias() { 
      if (this.alias == null)
        this.alias = new ArrayList<StringType>();
      return this.alias;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ElementDefinition setAlias(List<StringType> theAlias) { 
      this.alias = theAlias;
      return this;
    }

    public boolean hasAlias() { 
      if (this.alias == null)
        return false;
      for (StringType item : this.alias)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #alias} (Identifies additional names by which this element might also be known.)
     */
    public StringType addAliasElement() {//2 
      StringType t = new StringType();
      if (this.alias == null)
        this.alias = new ArrayList<StringType>();
      this.alias.add(t);
      return t;
    }

    /**
     * @param value {@link #alias} (Identifies additional names by which this element might also be known.)
     */
    public ElementDefinition addAlias(String value) { //1
      StringType t = new StringType();
      t.setValue(value);
      if (this.alias == null)
        this.alias = new ArrayList<StringType>();
      this.alias.add(t);
      return this;
    }

    /**
     * @param value {@link #alias} (Identifies additional names by which this element might also be known.)
     */
    public boolean hasAlias(String value) { 
      if (this.alias == null)
        return false;
      for (StringType v : this.alias)
        if (v.equals(value)) // string
          return true;
      return false;
    }

    /**
     * @return {@link #min} (The minimum number of times this element SHALL appear in the instance.). This is the underlying object with id, value and extensions. The accessor "getMin" gives direct access to the value
     */
    public UnsignedIntType getMinElement() { 
      if (this.min == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ElementDefinition.min");
        else if (Configuration.doAutoCreate())
          this.min = new UnsignedIntType(); // bb
      return this.min;
    }

    public boolean hasMinElement() { 
      return this.min != null && !this.min.isEmpty();
    }

    public boolean hasMin() { 
      return this.min != null && !this.min.isEmpty();
    }

    /**
     * @param value {@link #min} (The minimum number of times this element SHALL appear in the instance.). This is the underlying object with id, value and extensions. The accessor "getMin" gives direct access to the value
     */
    public ElementDefinition setMinElement(UnsignedIntType value) { 
      this.min = value;
      return this;
    }

    /**
     * @return The minimum number of times this element SHALL appear in the instance.
     */
    public int getMin() { 
      return this.min == null || this.min.isEmpty() ? 0 : this.min.getValue();
    }

    /**
     * @param value The minimum number of times this element SHALL appear in the instance.
     */
    public ElementDefinition setMin(int value) { 
        if (this.min == null)
          this.min = new UnsignedIntType();
        this.min.setValue(value);
      return this;
    }

    /**
     * @return {@link #max} (The maximum number of times this element is permitted to appear in the instance.). This is the underlying object with id, value and extensions. The accessor "getMax" gives direct access to the value
     */
    public StringType getMaxElement() { 
      if (this.max == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ElementDefinition.max");
        else if (Configuration.doAutoCreate())
          this.max = new StringType(); // bb
      return this.max;
    }

    public boolean hasMaxElement() { 
      return this.max != null && !this.max.isEmpty();
    }

    public boolean hasMax() { 
      return this.max != null && !this.max.isEmpty();
    }

    /**
     * @param value {@link #max} (The maximum number of times this element is permitted to appear in the instance.). This is the underlying object with id, value and extensions. The accessor "getMax" gives direct access to the value
     */
    public ElementDefinition setMaxElement(StringType value) { 
      this.max = value;
      return this;
    }

    /**
     * @return The maximum number of times this element is permitted to appear in the instance.
     */
    public String getMax() { 
      return this.max == null ? null : this.max.getValue();
    }

    /**
     * @param value The maximum number of times this element is permitted to appear in the instance.
     */
    public ElementDefinition setMax(String value) { 
      if (Utilities.noString(value))
        this.max = null;
      else {
        if (this.max == null)
          this.max = new StringType();
        this.max.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #base} (Information about the base definition of the element, provided to make it unnecessary for tools to trace the deviation of the element through the derived and related profiles. This information is provided when the element definition is not the original definition of an element - i.g. either in a constraint on another type, or for elements from a super type in a snap shot.)
     */
    public ElementDefinitionBaseComponent getBase() { 
      if (this.base == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ElementDefinition.base");
        else if (Configuration.doAutoCreate())
          this.base = new ElementDefinitionBaseComponent(); // cc
      return this.base;
    }

    public boolean hasBase() { 
      return this.base != null && !this.base.isEmpty();
    }

    /**
     * @param value {@link #base} (Information about the base definition of the element, provided to make it unnecessary for tools to trace the deviation of the element through the derived and related profiles. This information is provided when the element definition is not the original definition of an element - i.g. either in a constraint on another type, or for elements from a super type in a snap shot.)
     */
    public ElementDefinition setBase(ElementDefinitionBaseComponent value) { 
      this.base = value;
      return this;
    }

    /**
     * @return {@link #contentReference} (Identifies the identity of an element defined elsewhere in the profile whose content rules should be applied to the current element.). This is the underlying object with id, value and extensions. The accessor "getContentReference" gives direct access to the value
     */
    public UriType getContentReferenceElement() { 
      if (this.contentReference == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ElementDefinition.contentReference");
        else if (Configuration.doAutoCreate())
          this.contentReference = new UriType(); // bb
      return this.contentReference;
    }

    public boolean hasContentReferenceElement() { 
      return this.contentReference != null && !this.contentReference.isEmpty();
    }

    public boolean hasContentReference() { 
      return this.contentReference != null && !this.contentReference.isEmpty();
    }

    /**
     * @param value {@link #contentReference} (Identifies the identity of an element defined elsewhere in the profile whose content rules should be applied to the current element.). This is the underlying object with id, value and extensions. The accessor "getContentReference" gives direct access to the value
     */
    public ElementDefinition setContentReferenceElement(UriType value) { 
      this.contentReference = value;
      return this;
    }

    /**
     * @return Identifies the identity of an element defined elsewhere in the profile whose content rules should be applied to the current element.
     */
    public String getContentReference() { 
      return this.contentReference == null ? null : this.contentReference.getValue();
    }

    /**
     * @param value Identifies the identity of an element defined elsewhere in the profile whose content rules should be applied to the current element.
     */
    public ElementDefinition setContentReference(String value) { 
      if (Utilities.noString(value))
        this.contentReference = null;
      else {
        if (this.contentReference == null)
          this.contentReference = new UriType();
        this.contentReference.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #type} (The data type or resource that the value of this element is permitted to be.)
     */
    public List<TypeRefComponent> getType() { 
      if (this.type == null)
        this.type = new ArrayList<TypeRefComponent>();
      return this.type;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ElementDefinition setType(List<TypeRefComponent> theType) { 
      this.type = theType;
      return this;
    }

    public boolean hasType() { 
      if (this.type == null)
        return false;
      for (TypeRefComponent item : this.type)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public TypeRefComponent addType() { //3
      TypeRefComponent t = new TypeRefComponent();
      if (this.type == null)
        this.type = new ArrayList<TypeRefComponent>();
      this.type.add(t);
      return t;
    }

    public ElementDefinition addType(TypeRefComponent t) { //3
      if (t == null)
        return this;
      if (this.type == null)
        this.type = new ArrayList<TypeRefComponent>();
      this.type.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #type}, creating it if it does not already exist
     */
    public TypeRefComponent getTypeFirstRep() { 
      if (getType().isEmpty()) {
        addType();
      }
      return getType().get(0);
    }

    /**
     * @return {@link #defaultValue} (The value that should be used if there is no value stated in the instance (e.g. 'if not otherwise specified, the abstract is false').)
     */
    public org.hl7.fhir.dstu3.model.Type getDefaultValue() { 
      return this.defaultValue;
    }

    public boolean hasDefaultValue() { 
      return this.defaultValue != null && !this.defaultValue.isEmpty();
    }

    /**
     * @param value {@link #defaultValue} (The value that should be used if there is no value stated in the instance (e.g. 'if not otherwise specified, the abstract is false').)
     */
    public ElementDefinition setDefaultValue(org.hl7.fhir.dstu3.model.Type value) { 
      this.defaultValue = value;
      return this;
    }

    /**
     * @return {@link #meaningWhenMissing} (The Implicit meaning that is to be understood when this element is missing (e.g. 'when this element is missing, the period is ongoing'.). This is the underlying object with id, value and extensions. The accessor "getMeaningWhenMissing" gives direct access to the value
     */
    public MarkdownType getMeaningWhenMissingElement() { 
      if (this.meaningWhenMissing == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ElementDefinition.meaningWhenMissing");
        else if (Configuration.doAutoCreate())
          this.meaningWhenMissing = new MarkdownType(); // bb
      return this.meaningWhenMissing;
    }

    public boolean hasMeaningWhenMissingElement() { 
      return this.meaningWhenMissing != null && !this.meaningWhenMissing.isEmpty();
    }

    public boolean hasMeaningWhenMissing() { 
      return this.meaningWhenMissing != null && !this.meaningWhenMissing.isEmpty();
    }

    /**
     * @param value {@link #meaningWhenMissing} (The Implicit meaning that is to be understood when this element is missing (e.g. 'when this element is missing, the period is ongoing'.). This is the underlying object with id, value and extensions. The accessor "getMeaningWhenMissing" gives direct access to the value
     */
    public ElementDefinition setMeaningWhenMissingElement(MarkdownType value) { 
      this.meaningWhenMissing = value;
      return this;
    }

    /**
     * @return The Implicit meaning that is to be understood when this element is missing (e.g. 'when this element is missing, the period is ongoing'.
     */
    public String getMeaningWhenMissing() { 
      return this.meaningWhenMissing == null ? null : this.meaningWhenMissing.getValue();
    }

    /**
     * @param value The Implicit meaning that is to be understood when this element is missing (e.g. 'when this element is missing, the period is ongoing'.
     */
    public ElementDefinition setMeaningWhenMissing(String value) { 
      if (value == null)
        this.meaningWhenMissing = null;
      else {
        if (this.meaningWhenMissing == null)
          this.meaningWhenMissing = new MarkdownType();
        this.meaningWhenMissing.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #orderMeaning} (If present, indicates that the order of the repeating element has meaning and describes what that meaning is.  If absent, it means that the order of the element has no meaning.). This is the underlying object with id, value and extensions. The accessor "getOrderMeaning" gives direct access to the value
     */
    public StringType getOrderMeaningElement() { 
      if (this.orderMeaning == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ElementDefinition.orderMeaning");
        else if (Configuration.doAutoCreate())
          this.orderMeaning = new StringType(); // bb
      return this.orderMeaning;
    }

    public boolean hasOrderMeaningElement() { 
      return this.orderMeaning != null && !this.orderMeaning.isEmpty();
    }

    public boolean hasOrderMeaning() { 
      return this.orderMeaning != null && !this.orderMeaning.isEmpty();
    }

    /**
     * @param value {@link #orderMeaning} (If present, indicates that the order of the repeating element has meaning and describes what that meaning is.  If absent, it means that the order of the element has no meaning.). This is the underlying object with id, value and extensions. The accessor "getOrderMeaning" gives direct access to the value
     */
    public ElementDefinition setOrderMeaningElement(StringType value) { 
      this.orderMeaning = value;
      return this;
    }

    /**
     * @return If present, indicates that the order of the repeating element has meaning and describes what that meaning is.  If absent, it means that the order of the element has no meaning.
     */
    public String getOrderMeaning() { 
      return this.orderMeaning == null ? null : this.orderMeaning.getValue();
    }

    /**
     * @param value If present, indicates that the order of the repeating element has meaning and describes what that meaning is.  If absent, it means that the order of the element has no meaning.
     */
    public ElementDefinition setOrderMeaning(String value) { 
      if (Utilities.noString(value))
        this.orderMeaning = null;
      else {
        if (this.orderMeaning == null)
          this.orderMeaning = new StringType();
        this.orderMeaning.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #fixed} (Specifies a value that SHALL be exactly the value  for this element in the instance. For purposes of comparison, non-significant whitespace is ignored, and all values must be an exact match (case and accent sensitive). Missing elements/attributes must also be missing.)
     */
    public org.hl7.fhir.dstu3.model.Type getFixed() { 
      return this.fixed;
    }

    public boolean hasFixed() { 
      return this.fixed != null && !this.fixed.isEmpty();
    }

    /**
     * @param value {@link #fixed} (Specifies a value that SHALL be exactly the value  for this element in the instance. For purposes of comparison, non-significant whitespace is ignored, and all values must be an exact match (case and accent sensitive). Missing elements/attributes must also be missing.)
     */
    public ElementDefinition setFixed(org.hl7.fhir.dstu3.model.Type value) { 
      this.fixed = value;
      return this;
    }

    /**
     * @return {@link #pattern} (Specifies a value that the value in the instance SHALL follow - that is, any value in the pattern must be found in the instance. Other additional values may be found too. This is effectively constraint by example.  The values of elements present in the pattern must match exactly (case-sensitive, accent-sensitive, etc.).)
     */
    public org.hl7.fhir.dstu3.model.Type getPattern() { 
      return this.pattern;
    }

    public boolean hasPattern() { 
      return this.pattern != null && !this.pattern.isEmpty();
    }

    /**
     * @param value {@link #pattern} (Specifies a value that the value in the instance SHALL follow - that is, any value in the pattern must be found in the instance. Other additional values may be found too. This is effectively constraint by example.  The values of elements present in the pattern must match exactly (case-sensitive, accent-sensitive, etc.).)
     */
    public ElementDefinition setPattern(org.hl7.fhir.dstu3.model.Type value) { 
      this.pattern = value;
      return this;
    }

    /**
     * @return {@link #example} (A sample value for this element demonstrating the type of information that would typically be found in the element.)
     */
    public List<ElementDefinitionExampleComponent> getExample() { 
      if (this.example == null)
        this.example = new ArrayList<ElementDefinitionExampleComponent>();
      return this.example;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ElementDefinition setExample(List<ElementDefinitionExampleComponent> theExample) { 
      this.example = theExample;
      return this;
    }

    public boolean hasExample() { 
      if (this.example == null)
        return false;
      for (ElementDefinitionExampleComponent item : this.example)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public ElementDefinitionExampleComponent addExample() { //3
      ElementDefinitionExampleComponent t = new ElementDefinitionExampleComponent();
      if (this.example == null)
        this.example = new ArrayList<ElementDefinitionExampleComponent>();
      this.example.add(t);
      return t;
    }

    public ElementDefinition addExample(ElementDefinitionExampleComponent t) { //3
      if (t == null)
        return this;
      if (this.example == null)
        this.example = new ArrayList<ElementDefinitionExampleComponent>();
      this.example.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #example}, creating it if it does not already exist
     */
    public ElementDefinitionExampleComponent getExampleFirstRep() { 
      if (getExample().isEmpty()) {
        addExample();
      }
      return getExample().get(0);
    }

    /**
     * @return {@link #minValue} (The minimum allowed value for the element. The value is inclusive. This is allowed for the types date, dateTime, instant, time, decimal, integer, and Quantity.)
     */
    public Type getMinValue() { 
      return this.minValue;
    }

    /**
     * @return {@link #minValue} (The minimum allowed value for the element. The value is inclusive. This is allowed for the types date, dateTime, instant, time, decimal, integer, and Quantity.)
     */
    public DateType getMinValueDateType() throws FHIRException { 
      if (!(this.minValue instanceof DateType))
        throw new FHIRException("Type mismatch: the type DateType was expected, but "+this.minValue.getClass().getName()+" was encountered");
      return (DateType) this.minValue;
    }

    public boolean hasMinValueDateType() { 
      return this.minValue instanceof DateType;
    }

    /**
     * @return {@link #minValue} (The minimum allowed value for the element. The value is inclusive. This is allowed for the types date, dateTime, instant, time, decimal, integer, and Quantity.)
     */
    public DateTimeType getMinValueDateTimeType() throws FHIRException { 
      if (!(this.minValue instanceof DateTimeType))
        throw new FHIRException("Type mismatch: the type DateTimeType was expected, but "+this.minValue.getClass().getName()+" was encountered");
      return (DateTimeType) this.minValue;
    }

    public boolean hasMinValueDateTimeType() { 
      return this.minValue instanceof DateTimeType;
    }

    /**
     * @return {@link #minValue} (The minimum allowed value for the element. The value is inclusive. This is allowed for the types date, dateTime, instant, time, decimal, integer, and Quantity.)
     */
    public InstantType getMinValueInstantType() throws FHIRException { 
      if (!(this.minValue instanceof InstantType))
        throw new FHIRException("Type mismatch: the type InstantType was expected, but "+this.minValue.getClass().getName()+" was encountered");
      return (InstantType) this.minValue;
    }

    public boolean hasMinValueInstantType() { 
      return this.minValue instanceof InstantType;
    }

    /**
     * @return {@link #minValue} (The minimum allowed value for the element. The value is inclusive. This is allowed for the types date, dateTime, instant, time, decimal, integer, and Quantity.)
     */
    public TimeType getMinValueTimeType() throws FHIRException { 
      if (!(this.minValue instanceof TimeType))
        throw new FHIRException("Type mismatch: the type TimeType was expected, but "+this.minValue.getClass().getName()+" was encountered");
      return (TimeType) this.minValue;
    }

    public boolean hasMinValueTimeType() { 
      return this.minValue instanceof TimeType;
    }

    /**
     * @return {@link #minValue} (The minimum allowed value for the element. The value is inclusive. This is allowed for the types date, dateTime, instant, time, decimal, integer, and Quantity.)
     */
    public DecimalType getMinValueDecimalType() throws FHIRException { 
      if (!(this.minValue instanceof DecimalType))
        throw new FHIRException("Type mismatch: the type DecimalType was expected, but "+this.minValue.getClass().getName()+" was encountered");
      return (DecimalType) this.minValue;
    }

    public boolean hasMinValueDecimalType() { 
      return this.minValue instanceof DecimalType;
    }

    /**
     * @return {@link #minValue} (The minimum allowed value for the element. The value is inclusive. This is allowed for the types date, dateTime, instant, time, decimal, integer, and Quantity.)
     */
    public IntegerType getMinValueIntegerType() throws FHIRException { 
      if (!(this.minValue instanceof IntegerType))
        throw new FHIRException("Type mismatch: the type IntegerType was expected, but "+this.minValue.getClass().getName()+" was encountered");
      return (IntegerType) this.minValue;
    }

    public boolean hasMinValueIntegerType() { 
      return this.minValue instanceof IntegerType;
    }

    /**
     * @return {@link #minValue} (The minimum allowed value for the element. The value is inclusive. This is allowed for the types date, dateTime, instant, time, decimal, integer, and Quantity.)
     */
    public PositiveIntType getMinValuePositiveIntType() throws FHIRException { 
      if (!(this.minValue instanceof PositiveIntType))
        throw new FHIRException("Type mismatch: the type PositiveIntType was expected, but "+this.minValue.getClass().getName()+" was encountered");
      return (PositiveIntType) this.minValue;
    }

    public boolean hasMinValuePositiveIntType() { 
      return this.minValue instanceof PositiveIntType;
    }

    /**
     * @return {@link #minValue} (The minimum allowed value for the element. The value is inclusive. This is allowed for the types date, dateTime, instant, time, decimal, integer, and Quantity.)
     */
    public UnsignedIntType getMinValueUnsignedIntType() throws FHIRException { 
      if (!(this.minValue instanceof UnsignedIntType))
        throw new FHIRException("Type mismatch: the type UnsignedIntType was expected, but "+this.minValue.getClass().getName()+" was encountered");
      return (UnsignedIntType) this.minValue;
    }

    public boolean hasMinValueUnsignedIntType() { 
      return this.minValue instanceof UnsignedIntType;
    }

    /**
     * @return {@link #minValue} (The minimum allowed value for the element. The value is inclusive. This is allowed for the types date, dateTime, instant, time, decimal, integer, and Quantity.)
     */
    public Quantity getMinValueQuantity() throws FHIRException { 
      if (!(this.minValue instanceof Quantity))
        throw new FHIRException("Type mismatch: the type Quantity was expected, but "+this.minValue.getClass().getName()+" was encountered");
      return (Quantity) this.minValue;
    }

    public boolean hasMinValueQuantity() { 
      return this.minValue instanceof Quantity;
    }

    public boolean hasMinValue() { 
      return this.minValue != null && !this.minValue.isEmpty();
    }

    /**
     * @param value {@link #minValue} (The minimum allowed value for the element. The value is inclusive. This is allowed for the types date, dateTime, instant, time, decimal, integer, and Quantity.)
     */
    public ElementDefinition setMinValue(Type value) { 
      this.minValue = value;
      return this;
    }

    /**
     * @return {@link #maxValue} (The maximum allowed value for the element. The value is inclusive. This is allowed for the types date, dateTime, instant, time, decimal, integer, and Quantity.)
     */
    public Type getMaxValue() { 
      return this.maxValue;
    }

    /**
     * @return {@link #maxValue} (The maximum allowed value for the element. The value is inclusive. This is allowed for the types date, dateTime, instant, time, decimal, integer, and Quantity.)
     */
    public DateType getMaxValueDateType() throws FHIRException { 
      if (!(this.maxValue instanceof DateType))
        throw new FHIRException("Type mismatch: the type DateType was expected, but "+this.maxValue.getClass().getName()+" was encountered");
      return (DateType) this.maxValue;
    }

    public boolean hasMaxValueDateType() { 
      return this.maxValue instanceof DateType;
    }

    /**
     * @return {@link #maxValue} (The maximum allowed value for the element. The value is inclusive. This is allowed for the types date, dateTime, instant, time, decimal, integer, and Quantity.)
     */
    public DateTimeType getMaxValueDateTimeType() throws FHIRException { 
      if (!(this.maxValue instanceof DateTimeType))
        throw new FHIRException("Type mismatch: the type DateTimeType was expected, but "+this.maxValue.getClass().getName()+" was encountered");
      return (DateTimeType) this.maxValue;
    }

    public boolean hasMaxValueDateTimeType() { 
      return this.maxValue instanceof DateTimeType;
    }

    /**
     * @return {@link #maxValue} (The maximum allowed value for the element. The value is inclusive. This is allowed for the types date, dateTime, instant, time, decimal, integer, and Quantity.)
     */
    public InstantType getMaxValueInstantType() throws FHIRException { 
      if (!(this.maxValue instanceof InstantType))
        throw new FHIRException("Type mismatch: the type InstantType was expected, but "+this.maxValue.getClass().getName()+" was encountered");
      return (InstantType) this.maxValue;
    }

    public boolean hasMaxValueInstantType() { 
      return this.maxValue instanceof InstantType;
    }

    /**
     * @return {@link #maxValue} (The maximum allowed value for the element. The value is inclusive. This is allowed for the types date, dateTime, instant, time, decimal, integer, and Quantity.)
     */
    public TimeType getMaxValueTimeType() throws FHIRException { 
      if (!(this.maxValue instanceof TimeType))
        throw new FHIRException("Type mismatch: the type TimeType was expected, but "+this.maxValue.getClass().getName()+" was encountered");
      return (TimeType) this.maxValue;
    }

    public boolean hasMaxValueTimeType() { 
      return this.maxValue instanceof TimeType;
    }

    /**
     * @return {@link #maxValue} (The maximum allowed value for the element. The value is inclusive. This is allowed for the types date, dateTime, instant, time, decimal, integer, and Quantity.)
     */
    public DecimalType getMaxValueDecimalType() throws FHIRException { 
      if (!(this.maxValue instanceof DecimalType))
        throw new FHIRException("Type mismatch: the type DecimalType was expected, but "+this.maxValue.getClass().getName()+" was encountered");
      return (DecimalType) this.maxValue;
    }

    public boolean hasMaxValueDecimalType() { 
      return this.maxValue instanceof DecimalType;
    }

    /**
     * @return {@link #maxValue} (The maximum allowed value for the element. The value is inclusive. This is allowed for the types date, dateTime, instant, time, decimal, integer, and Quantity.)
     */
    public IntegerType getMaxValueIntegerType() throws FHIRException { 
      if (!(this.maxValue instanceof IntegerType))
        throw new FHIRException("Type mismatch: the type IntegerType was expected, but "+this.maxValue.getClass().getName()+" was encountered");
      return (IntegerType) this.maxValue;
    }

    public boolean hasMaxValueIntegerType() { 
      return this.maxValue instanceof IntegerType;
    }

    /**
     * @return {@link #maxValue} (The maximum allowed value for the element. The value is inclusive. This is allowed for the types date, dateTime, instant, time, decimal, integer, and Quantity.)
     */
    public PositiveIntType getMaxValuePositiveIntType() throws FHIRException { 
      if (!(this.maxValue instanceof PositiveIntType))
        throw new FHIRException("Type mismatch: the type PositiveIntType was expected, but "+this.maxValue.getClass().getName()+" was encountered");
      return (PositiveIntType) this.maxValue;
    }

    public boolean hasMaxValuePositiveIntType() { 
      return this.maxValue instanceof PositiveIntType;
    }

    /**
     * @return {@link #maxValue} (The maximum allowed value for the element. The value is inclusive. This is allowed for the types date, dateTime, instant, time, decimal, integer, and Quantity.)
     */
    public UnsignedIntType getMaxValueUnsignedIntType() throws FHIRException { 
      if (!(this.maxValue instanceof UnsignedIntType))
        throw new FHIRException("Type mismatch: the type UnsignedIntType was expected, but "+this.maxValue.getClass().getName()+" was encountered");
      return (UnsignedIntType) this.maxValue;
    }

    public boolean hasMaxValueUnsignedIntType() { 
      return this.maxValue instanceof UnsignedIntType;
    }

    /**
     * @return {@link #maxValue} (The maximum allowed value for the element. The value is inclusive. This is allowed for the types date, dateTime, instant, time, decimal, integer, and Quantity.)
     */
    public Quantity getMaxValueQuantity() throws FHIRException { 
      if (!(this.maxValue instanceof Quantity))
        throw new FHIRException("Type mismatch: the type Quantity was expected, but "+this.maxValue.getClass().getName()+" was encountered");
      return (Quantity) this.maxValue;
    }

    public boolean hasMaxValueQuantity() { 
      return this.maxValue instanceof Quantity;
    }

    public boolean hasMaxValue() { 
      return this.maxValue != null && !this.maxValue.isEmpty();
    }

    /**
     * @param value {@link #maxValue} (The maximum allowed value for the element. The value is inclusive. This is allowed for the types date, dateTime, instant, time, decimal, integer, and Quantity.)
     */
    public ElementDefinition setMaxValue(Type value) { 
      this.maxValue = value;
      return this;
    }

    /**
     * @return {@link #maxLength} (Indicates the maximum length in characters that is permitted to be present in conformant instances and which is expected to be supported by conformant consumers that support the element.). This is the underlying object with id, value and extensions. The accessor "getMaxLength" gives direct access to the value
     */
    public IntegerType getMaxLengthElement() { 
      if (this.maxLength == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ElementDefinition.maxLength");
        else if (Configuration.doAutoCreate())
          this.maxLength = new IntegerType(); // bb
      return this.maxLength;
    }

    public boolean hasMaxLengthElement() { 
      return this.maxLength != null && !this.maxLength.isEmpty();
    }

    public boolean hasMaxLength() { 
      return this.maxLength != null && !this.maxLength.isEmpty();
    }

    /**
     * @param value {@link #maxLength} (Indicates the maximum length in characters that is permitted to be present in conformant instances and which is expected to be supported by conformant consumers that support the element.). This is the underlying object with id, value and extensions. The accessor "getMaxLength" gives direct access to the value
     */
    public ElementDefinition setMaxLengthElement(IntegerType value) { 
      this.maxLength = value;
      return this;
    }

    /**
     * @return Indicates the maximum length in characters that is permitted to be present in conformant instances and which is expected to be supported by conformant consumers that support the element.
     */
    public int getMaxLength() { 
      return this.maxLength == null || this.maxLength.isEmpty() ? 0 : this.maxLength.getValue();
    }

    /**
     * @param value Indicates the maximum length in characters that is permitted to be present in conformant instances and which is expected to be supported by conformant consumers that support the element.
     */
    public ElementDefinition setMaxLength(int value) { 
        if (this.maxLength == null)
          this.maxLength = new IntegerType();
        this.maxLength.setValue(value);
      return this;
    }

    /**
     * @return {@link #condition} (A reference to an invariant that may make additional statements about the cardinality or value in the instance.)
     */
    public List<IdType> getCondition() { 
      if (this.condition == null)
        this.condition = new ArrayList<IdType>();
      return this.condition;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ElementDefinition setCondition(List<IdType> theCondition) { 
      this.condition = theCondition;
      return this;
    }

    public boolean hasCondition() { 
      if (this.condition == null)
        return false;
      for (IdType item : this.condition)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #condition} (A reference to an invariant that may make additional statements about the cardinality or value in the instance.)
     */
    public IdType addConditionElement() {//2 
      IdType t = new IdType();
      if (this.condition == null)
        this.condition = new ArrayList<IdType>();
      this.condition.add(t);
      return t;
    }

    /**
     * @param value {@link #condition} (A reference to an invariant that may make additional statements about the cardinality or value in the instance.)
     */
    public ElementDefinition addCondition(String value) { //1
      IdType t = new IdType();
      t.setValue(value);
      if (this.condition == null)
        this.condition = new ArrayList<IdType>();
      this.condition.add(t);
      return this;
    }

    /**
     * @param value {@link #condition} (A reference to an invariant that may make additional statements about the cardinality or value in the instance.)
     */
    public boolean hasCondition(String value) { 
      if (this.condition == null)
        return false;
      for (IdType v : this.condition)
        if (v.equals(value)) // id
          return true;
      return false;
    }

    /**
     * @return {@link #constraint} (Formal constraints such as co-occurrence and other constraints that can be computationally evaluated within the context of the instance.)
     */
    public List<ElementDefinitionConstraintComponent> getConstraint() { 
      if (this.constraint == null)
        this.constraint = new ArrayList<ElementDefinitionConstraintComponent>();
      return this.constraint;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ElementDefinition setConstraint(List<ElementDefinitionConstraintComponent> theConstraint) { 
      this.constraint = theConstraint;
      return this;
    }

    public boolean hasConstraint() { 
      if (this.constraint == null)
        return false;
      for (ElementDefinitionConstraintComponent item : this.constraint)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public ElementDefinitionConstraintComponent addConstraint() { //3
      ElementDefinitionConstraintComponent t = new ElementDefinitionConstraintComponent();
      if (this.constraint == null)
        this.constraint = new ArrayList<ElementDefinitionConstraintComponent>();
      this.constraint.add(t);
      return t;
    }

    public ElementDefinition addConstraint(ElementDefinitionConstraintComponent t) { //3
      if (t == null)
        return this;
      if (this.constraint == null)
        this.constraint = new ArrayList<ElementDefinitionConstraintComponent>();
      this.constraint.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #constraint}, creating it if it does not already exist
     */
    public ElementDefinitionConstraintComponent getConstraintFirstRep() { 
      if (getConstraint().isEmpty()) {
        addConstraint();
      }
      return getConstraint().get(0);
    }

    /**
     * @return {@link #mustSupport} (If true, implementations that produce or consume resources SHALL provide "support" for the element in some meaningful way.  If false, the element may be ignored and not supported.). This is the underlying object with id, value and extensions. The accessor "getMustSupport" gives direct access to the value
     */
    public BooleanType getMustSupportElement() { 
      if (this.mustSupport == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ElementDefinition.mustSupport");
        else if (Configuration.doAutoCreate())
          this.mustSupport = new BooleanType(); // bb
      return this.mustSupport;
    }

    public boolean hasMustSupportElement() { 
      return this.mustSupport != null && !this.mustSupport.isEmpty();
    }

    public boolean hasMustSupport() { 
      return this.mustSupport != null && !this.mustSupport.isEmpty();
    }

    /**
     * @param value {@link #mustSupport} (If true, implementations that produce or consume resources SHALL provide "support" for the element in some meaningful way.  If false, the element may be ignored and not supported.). This is the underlying object with id, value and extensions. The accessor "getMustSupport" gives direct access to the value
     */
    public ElementDefinition setMustSupportElement(BooleanType value) { 
      this.mustSupport = value;
      return this;
    }

    /**
     * @return If true, implementations that produce or consume resources SHALL provide "support" for the element in some meaningful way.  If false, the element may be ignored and not supported.
     */
    public boolean getMustSupport() { 
      return this.mustSupport == null || this.mustSupport.isEmpty() ? false : this.mustSupport.getValue();
    }

    /**
     * @param value If true, implementations that produce or consume resources SHALL provide "support" for the element in some meaningful way.  If false, the element may be ignored and not supported.
     */
    public ElementDefinition setMustSupport(boolean value) { 
        if (this.mustSupport == null)
          this.mustSupport = new BooleanType();
        this.mustSupport.setValue(value);
      return this;
    }

    /**
     * @return {@link #isModifier} (If true, the value of this element affects the interpretation of the element or resource that contains it, and the value of the element cannot be ignored. Typically, this is used for status, negation and qualification codes. The effect of this is that the element cannot be ignored by systems: they SHALL either recognize the element and process it, and/or a pre-determination has been made that it is not relevant to their particular system.). This is the underlying object with id, value and extensions. The accessor "getIsModifier" gives direct access to the value
     */
    public BooleanType getIsModifierElement() { 
      if (this.isModifier == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ElementDefinition.isModifier");
        else if (Configuration.doAutoCreate())
          this.isModifier = new BooleanType(); // bb
      return this.isModifier;
    }

    public boolean hasIsModifierElement() { 
      return this.isModifier != null && !this.isModifier.isEmpty();
    }

    public boolean hasIsModifier() { 
      return this.isModifier != null && !this.isModifier.isEmpty();
    }

    /**
     * @param value {@link #isModifier} (If true, the value of this element affects the interpretation of the element or resource that contains it, and the value of the element cannot be ignored. Typically, this is used for status, negation and qualification codes. The effect of this is that the element cannot be ignored by systems: they SHALL either recognize the element and process it, and/or a pre-determination has been made that it is not relevant to their particular system.). This is the underlying object with id, value and extensions. The accessor "getIsModifier" gives direct access to the value
     */
    public ElementDefinition setIsModifierElement(BooleanType value) { 
      this.isModifier = value;
      return this;
    }

    /**
     * @return If true, the value of this element affects the interpretation of the element or resource that contains it, and the value of the element cannot be ignored. Typically, this is used for status, negation and qualification codes. The effect of this is that the element cannot be ignored by systems: they SHALL either recognize the element and process it, and/or a pre-determination has been made that it is not relevant to their particular system.
     */
    public boolean getIsModifier() { 
      return this.isModifier == null || this.isModifier.isEmpty() ? false : this.isModifier.getValue();
    }

    /**
     * @param value If true, the value of this element affects the interpretation of the element or resource that contains it, and the value of the element cannot be ignored. Typically, this is used for status, negation and qualification codes. The effect of this is that the element cannot be ignored by systems: they SHALL either recognize the element and process it, and/or a pre-determination has been made that it is not relevant to their particular system.
     */
    public ElementDefinition setIsModifier(boolean value) { 
        if (this.isModifier == null)
          this.isModifier = new BooleanType();
        this.isModifier.setValue(value);
      return this;
    }

    /**
     * @return {@link #isSummary} (Whether the element should be included if a client requests a search with the parameter _summary=true.). This is the underlying object with id, value and extensions. The accessor "getIsSummary" gives direct access to the value
     */
    public BooleanType getIsSummaryElement() { 
      if (this.isSummary == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ElementDefinition.isSummary");
        else if (Configuration.doAutoCreate())
          this.isSummary = new BooleanType(); // bb
      return this.isSummary;
    }

    public boolean hasIsSummaryElement() { 
      return this.isSummary != null && !this.isSummary.isEmpty();
    }

    public boolean hasIsSummary() { 
      return this.isSummary != null && !this.isSummary.isEmpty();
    }

    /**
     * @param value {@link #isSummary} (Whether the element should be included if a client requests a search with the parameter _summary=true.). This is the underlying object with id, value and extensions. The accessor "getIsSummary" gives direct access to the value
     */
    public ElementDefinition setIsSummaryElement(BooleanType value) { 
      this.isSummary = value;
      return this;
    }

    /**
     * @return Whether the element should be included if a client requests a search with the parameter _summary=true.
     */
    public boolean getIsSummary() { 
      return this.isSummary == null || this.isSummary.isEmpty() ? false : this.isSummary.getValue();
    }

    /**
     * @param value Whether the element should be included if a client requests a search with the parameter _summary=true.
     */
    public ElementDefinition setIsSummary(boolean value) { 
        if (this.isSummary == null)
          this.isSummary = new BooleanType();
        this.isSummary.setValue(value);
      return this;
    }

    /**
     * @return {@link #binding} (Binds to a value set if this element is coded (code, Coding, CodeableConcept, Quantity), or the data types (string, uri).)
     */
    public ElementDefinitionBindingComponent getBinding() { 
      if (this.binding == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ElementDefinition.binding");
        else if (Configuration.doAutoCreate())
          this.binding = new ElementDefinitionBindingComponent(); // cc
      return this.binding;
    }

    public boolean hasBinding() { 
      return this.binding != null && !this.binding.isEmpty();
    }

    /**
     * @param value {@link #binding} (Binds to a value set if this element is coded (code, Coding, CodeableConcept, Quantity), or the data types (string, uri).)
     */
    public ElementDefinition setBinding(ElementDefinitionBindingComponent value) { 
      this.binding = value;
      return this;
    }

    /**
     * @return {@link #mapping} (Identifies a concept from an external specification that roughly corresponds to this element.)
     */
    public List<ElementDefinitionMappingComponent> getMapping() { 
      if (this.mapping == null)
        this.mapping = new ArrayList<ElementDefinitionMappingComponent>();
      return this.mapping;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ElementDefinition setMapping(List<ElementDefinitionMappingComponent> theMapping) { 
      this.mapping = theMapping;
      return this;
    }

    public boolean hasMapping() { 
      if (this.mapping == null)
        return false;
      for (ElementDefinitionMappingComponent item : this.mapping)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public ElementDefinitionMappingComponent addMapping() { //3
      ElementDefinitionMappingComponent t = new ElementDefinitionMappingComponent();
      if (this.mapping == null)
        this.mapping = new ArrayList<ElementDefinitionMappingComponent>();
      this.mapping.add(t);
      return t;
    }

    public ElementDefinition addMapping(ElementDefinitionMappingComponent t) { //3
      if (t == null)
        return this;
      if (this.mapping == null)
        this.mapping = new ArrayList<ElementDefinitionMappingComponent>();
      this.mapping.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #mapping}, creating it if it does not already exist
     */
    public ElementDefinitionMappingComponent getMappingFirstRep() { 
      if (getMapping().isEmpty()) {
        addMapping();
      }
      return getMapping().get(0);
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("path", "string", "The path identifies the element and is expressed as a \".\"-separated list of ancestor elements, beginning with the name of the resource or extension.", 0, java.lang.Integer.MAX_VALUE, path));
        childrenList.add(new Property("representation", "code", "Codes that define how this element is represented in instances, when the deviation varies from the normal case.", 0, java.lang.Integer.MAX_VALUE, representation));
        childrenList.add(new Property("sliceName", "string", "The name of this element definition slice, when slicing is working. The name must be a token with no dots or spaces. This is a unique name referring to a specific set of constraints applied to this element, used to provide a name to different slices of the same element.", 0, java.lang.Integer.MAX_VALUE, sliceName));
        childrenList.add(new Property("label", "string", "A single preferred label which is the text to display beside the element indicating its meaning or to use to prompt for the element in a user display or form.", 0, java.lang.Integer.MAX_VALUE, label));
        childrenList.add(new Property("code", "Coding", "A code that has the same meaning as the element in a particular terminology.", 0, java.lang.Integer.MAX_VALUE, code));
        childrenList.add(new Property("slicing", "", "Indicates that the element is sliced into a set of alternative definitions (i.e. in a structure definition, there are multiple different constraints on a single element in the base resource). Slicing can be used in any resource that has cardinality ..* on the base resource, or any resource with a choice of types. The set of slices is any elements that come after this in the element sequence that have the same path, until a shorter path occurs (the shorter path terminates the set).", 0, java.lang.Integer.MAX_VALUE, slicing));
        childrenList.add(new Property("short", "string", "A concise description of what this element means (e.g. for use in autogenerated summaries).", 0, java.lang.Integer.MAX_VALUE, short_));
        childrenList.add(new Property("definition", "markdown", "Provides a complete explanation of the meaning of the data element for human readability.  For the case of elements derived from existing elements (e.g. constraints), the definition SHALL be consistent with the base definition, but convey the meaning of the element in the particular context of use of the resource.", 0, java.lang.Integer.MAX_VALUE, definition));
        childrenList.add(new Property("comment", "markdown", "Explanatory notes and implementation guidance about the data element, including notes about how to use the data properly, exceptions to proper use, etc.", 0, java.lang.Integer.MAX_VALUE, comment));
        childrenList.add(new Property("requirements", "markdown", "This element is for traceability of why the element was created and why the constraints exist as they do. This may be used to point to source materials or specifications that drove the structure of this element.", 0, java.lang.Integer.MAX_VALUE, requirements));
        childrenList.add(new Property("alias", "string", "Identifies additional names by which this element might also be known.", 0, java.lang.Integer.MAX_VALUE, alias));
        childrenList.add(new Property("min", "unsignedInt", "The minimum number of times this element SHALL appear in the instance.", 0, java.lang.Integer.MAX_VALUE, min));
        childrenList.add(new Property("max", "string", "The maximum number of times this element is permitted to appear in the instance.", 0, java.lang.Integer.MAX_VALUE, max));
        childrenList.add(new Property("base", "", "Information about the base definition of the element, provided to make it unnecessary for tools to trace the deviation of the element through the derived and related profiles. This information is provided when the element definition is not the original definition of an element - i.g. either in a constraint on another type, or for elements from a super type in a snap shot.", 0, java.lang.Integer.MAX_VALUE, base));
        childrenList.add(new Property("contentReference", "uri", "Identifies the identity of an element defined elsewhere in the profile whose content rules should be applied to the current element.", 0, java.lang.Integer.MAX_VALUE, contentReference));
        childrenList.add(new Property("type", "", "The data type or resource that the value of this element is permitted to be.", 0, java.lang.Integer.MAX_VALUE, type));
        childrenList.add(new Property("defaultValue[x]", "*", "The value that should be used if there is no value stated in the instance (e.g. 'if not otherwise specified, the abstract is false').", 0, java.lang.Integer.MAX_VALUE, defaultValue));
        childrenList.add(new Property("meaningWhenMissing", "markdown", "The Implicit meaning that is to be understood when this element is missing (e.g. 'when this element is missing, the period is ongoing'.", 0, java.lang.Integer.MAX_VALUE, meaningWhenMissing));
        childrenList.add(new Property("orderMeaning", "string", "If present, indicates that the order of the repeating element has meaning and describes what that meaning is.  If absent, it means that the order of the element has no meaning.", 0, java.lang.Integer.MAX_VALUE, orderMeaning));
        childrenList.add(new Property("fixed[x]", "*", "Specifies a value that SHALL be exactly the value  for this element in the instance. For purposes of comparison, non-significant whitespace is ignored, and all values must be an exact match (case and accent sensitive). Missing elements/attributes must also be missing.", 0, java.lang.Integer.MAX_VALUE, fixed));
        childrenList.add(new Property("pattern[x]", "*", "Specifies a value that the value in the instance SHALL follow - that is, any value in the pattern must be found in the instance. Other additional values may be found too. This is effectively constraint by example.  The values of elements present in the pattern must match exactly (case-sensitive, accent-sensitive, etc.).", 0, java.lang.Integer.MAX_VALUE, pattern));
        childrenList.add(new Property("example", "", "A sample value for this element demonstrating the type of information that would typically be found in the element.", 0, java.lang.Integer.MAX_VALUE, example));
        childrenList.add(new Property("minValue[x]", "date|dateTime|instant|time|decimal|integer|positiveInt|unsignedInt|Quantity", "The minimum allowed value for the element. The value is inclusive. This is allowed for the types date, dateTime, instant, time, decimal, integer, and Quantity.", 0, java.lang.Integer.MAX_VALUE, minValue));
        childrenList.add(new Property("maxValue[x]", "date|dateTime|instant|time|decimal|integer|positiveInt|unsignedInt|Quantity", "The maximum allowed value for the element. The value is inclusive. This is allowed for the types date, dateTime, instant, time, decimal, integer, and Quantity.", 0, java.lang.Integer.MAX_VALUE, maxValue));
        childrenList.add(new Property("maxLength", "integer", "Indicates the maximum length in characters that is permitted to be present in conformant instances and which is expected to be supported by conformant consumers that support the element.", 0, java.lang.Integer.MAX_VALUE, maxLength));
        childrenList.add(new Property("condition", "id", "A reference to an invariant that may make additional statements about the cardinality or value in the instance.", 0, java.lang.Integer.MAX_VALUE, condition));
        childrenList.add(new Property("constraint", "", "Formal constraints such as co-occurrence and other constraints that can be computationally evaluated within the context of the instance.", 0, java.lang.Integer.MAX_VALUE, constraint));
        childrenList.add(new Property("mustSupport", "boolean", "If true, implementations that produce or consume resources SHALL provide \"support\" for the element in some meaningful way.  If false, the element may be ignored and not supported.", 0, java.lang.Integer.MAX_VALUE, mustSupport));
        childrenList.add(new Property("isModifier", "boolean", "If true, the value of this element affects the interpretation of the element or resource that contains it, and the value of the element cannot be ignored. Typically, this is used for status, negation and qualification codes. The effect of this is that the element cannot be ignored by systems: they SHALL either recognize the element and process it, and/or a pre-determination has been made that it is not relevant to their particular system.", 0, java.lang.Integer.MAX_VALUE, isModifier));
        childrenList.add(new Property("isSummary", "boolean", "Whether the element should be included if a client requests a search with the parameter _summary=true.", 0, java.lang.Integer.MAX_VALUE, isSummary));
        childrenList.add(new Property("binding", "", "Binds to a value set if this element is coded (code, Coding, CodeableConcept, Quantity), or the data types (string, uri).", 0, java.lang.Integer.MAX_VALUE, binding));
        childrenList.add(new Property("mapping", "", "Identifies a concept from an external specification that roughly corresponds to this element.", 0, java.lang.Integer.MAX_VALUE, mapping));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3433509: /*path*/ return this.path == null ? new Base[0] : new Base[] {this.path}; // StringType
        case -671065907: /*representation*/ return this.representation == null ? new Base[0] : this.representation.toArray(new Base[this.representation.size()]); // Enumeration<PropertyRepresentation>
        case -825289923: /*sliceName*/ return this.sliceName == null ? new Base[0] : new Base[] {this.sliceName}; // StringType
        case 102727412: /*label*/ return this.label == null ? new Base[0] : new Base[] {this.label}; // StringType
        case 3059181: /*code*/ return this.code == null ? new Base[0] : this.code.toArray(new Base[this.code.size()]); // Coding
        case -2119287345: /*slicing*/ return this.slicing == null ? new Base[0] : new Base[] {this.slicing}; // ElementDefinitionSlicingComponent
        case 109413500: /*short*/ return this.short_ == null ? new Base[0] : new Base[] {this.short_}; // StringType
        case -1014418093: /*definition*/ return this.definition == null ? new Base[0] : new Base[] {this.definition}; // MarkdownType
        case 950398559: /*comment*/ return this.comment == null ? new Base[0] : new Base[] {this.comment}; // MarkdownType
        case -1619874672: /*requirements*/ return this.requirements == null ? new Base[0] : new Base[] {this.requirements}; // MarkdownType
        case 92902992: /*alias*/ return this.alias == null ? new Base[0] : this.alias.toArray(new Base[this.alias.size()]); // StringType
        case 108114: /*min*/ return this.min == null ? new Base[0] : new Base[] {this.min}; // UnsignedIntType
        case 107876: /*max*/ return this.max == null ? new Base[0] : new Base[] {this.max}; // StringType
        case 3016401: /*base*/ return this.base == null ? new Base[0] : new Base[] {this.base}; // ElementDefinitionBaseComponent
        case 1193747154: /*contentReference*/ return this.contentReference == null ? new Base[0] : new Base[] {this.contentReference}; // UriType
        case 3575610: /*type*/ return this.type == null ? new Base[0] : this.type.toArray(new Base[this.type.size()]); // TypeRefComponent
        case -659125328: /*defaultValue*/ return this.defaultValue == null ? new Base[0] : new Base[] {this.defaultValue}; // org.hl7.fhir.dstu3.model.Type
        case 1857257103: /*meaningWhenMissing*/ return this.meaningWhenMissing == null ? new Base[0] : new Base[] {this.meaningWhenMissing}; // MarkdownType
        case 1828196047: /*orderMeaning*/ return this.orderMeaning == null ? new Base[0] : new Base[] {this.orderMeaning}; // StringType
        case 97445748: /*fixed*/ return this.fixed == null ? new Base[0] : new Base[] {this.fixed}; // org.hl7.fhir.dstu3.model.Type
        case -791090288: /*pattern*/ return this.pattern == null ? new Base[0] : new Base[] {this.pattern}; // org.hl7.fhir.dstu3.model.Type
        case -1322970774: /*example*/ return this.example == null ? new Base[0] : this.example.toArray(new Base[this.example.size()]); // ElementDefinitionExampleComponent
        case -1376969153: /*minValue*/ return this.minValue == null ? new Base[0] : new Base[] {this.minValue}; // Type
        case 399227501: /*maxValue*/ return this.maxValue == null ? new Base[0] : new Base[] {this.maxValue}; // Type
        case -791400086: /*maxLength*/ return this.maxLength == null ? new Base[0] : new Base[] {this.maxLength}; // IntegerType
        case -861311717: /*condition*/ return this.condition == null ? new Base[0] : this.condition.toArray(new Base[this.condition.size()]); // IdType
        case -190376483: /*constraint*/ return this.constraint == null ? new Base[0] : this.constraint.toArray(new Base[this.constraint.size()]); // ElementDefinitionConstraintComponent
        case -1402857082: /*mustSupport*/ return this.mustSupport == null ? new Base[0] : new Base[] {this.mustSupport}; // BooleanType
        case -1408783839: /*isModifier*/ return this.isModifier == null ? new Base[0] : new Base[] {this.isModifier}; // BooleanType
        case 1857548060: /*isSummary*/ return this.isSummary == null ? new Base[0] : new Base[] {this.isSummary}; // BooleanType
        case -108220795: /*binding*/ return this.binding == null ? new Base[0] : new Base[] {this.binding}; // ElementDefinitionBindingComponent
        case 837556430: /*mapping*/ return this.mapping == null ? new Base[0] : this.mapping.toArray(new Base[this.mapping.size()]); // ElementDefinitionMappingComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3433509: // path
          this.path = castToString(value); // StringType
          return value;
        case -671065907: // representation
          value = new PropertyRepresentationEnumFactory().fromType(castToCode(value));
          this.getRepresentation().add((Enumeration) value); // Enumeration<PropertyRepresentation>
          return value;
        case -825289923: // sliceName
          this.sliceName = castToString(value); // StringType
          return value;
        case 102727412: // label
          this.label = castToString(value); // StringType
          return value;
        case 3059181: // code
          this.getCode().add(castToCoding(value)); // Coding
          return value;
        case -2119287345: // slicing
          this.slicing = (ElementDefinitionSlicingComponent) value; // ElementDefinitionSlicingComponent
          return value;
        case 109413500: // short
          this.short_ = castToString(value); // StringType
          return value;
        case -1014418093: // definition
          this.definition = castToMarkdown(value); // MarkdownType
          return value;
        case 950398559: // comment
          this.comment = castToMarkdown(value); // MarkdownType
          return value;
        case -1619874672: // requirements
          this.requirements = castToMarkdown(value); // MarkdownType
          return value;
        case 92902992: // alias
          this.getAlias().add(castToString(value)); // StringType
          return value;
        case 108114: // min
          this.min = castToUnsignedInt(value); // UnsignedIntType
          return value;
        case 107876: // max
          this.max = castToString(value); // StringType
          return value;
        case 3016401: // base
          this.base = (ElementDefinitionBaseComponent) value; // ElementDefinitionBaseComponent
          return value;
        case 1193747154: // contentReference
          this.contentReference = castToUri(value); // UriType
          return value;
        case 3575610: // type
          this.getType().add((TypeRefComponent) value); // TypeRefComponent
          return value;
        case -659125328: // defaultValue
          this.defaultValue = castToType(value); // org.hl7.fhir.dstu3.model.Type
          return value;
        case 1857257103: // meaningWhenMissing
          this.meaningWhenMissing = castToMarkdown(value); // MarkdownType
          return value;
        case 1828196047: // orderMeaning
          this.orderMeaning = castToString(value); // StringType
          return value;
        case 97445748: // fixed
          this.fixed = castToType(value); // org.hl7.fhir.dstu3.model.Type
          return value;
        case -791090288: // pattern
          this.pattern = castToType(value); // org.hl7.fhir.dstu3.model.Type
          return value;
        case -1322970774: // example
          this.getExample().add((ElementDefinitionExampleComponent) value); // ElementDefinitionExampleComponent
          return value;
        case -1376969153: // minValue
          this.minValue = castToType(value); // Type
          return value;
        case 399227501: // maxValue
          this.maxValue = castToType(value); // Type
          return value;
        case -791400086: // maxLength
          this.maxLength = castToInteger(value); // IntegerType
          return value;
        case -861311717: // condition
          this.getCondition().add(castToId(value)); // IdType
          return value;
        case -190376483: // constraint
          this.getConstraint().add((ElementDefinitionConstraintComponent) value); // ElementDefinitionConstraintComponent
          return value;
        case -1402857082: // mustSupport
          this.mustSupport = castToBoolean(value); // BooleanType
          return value;
        case -1408783839: // isModifier
          this.isModifier = castToBoolean(value); // BooleanType
          return value;
        case 1857548060: // isSummary
          this.isSummary = castToBoolean(value); // BooleanType
          return value;
        case -108220795: // binding
          this.binding = (ElementDefinitionBindingComponent) value; // ElementDefinitionBindingComponent
          return value;
        case 837556430: // mapping
          this.getMapping().add((ElementDefinitionMappingComponent) value); // ElementDefinitionMappingComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("path")) {
          this.path = castToString(value); // StringType
        } else if (name.equals("representation")) {
          value = new PropertyRepresentationEnumFactory().fromType(castToCode(value));
          this.getRepresentation().add((Enumeration) value);
        } else if (name.equals("sliceName")) {
          this.sliceName = castToString(value); // StringType
        } else if (name.equals("label")) {
          this.label = castToString(value); // StringType
        } else if (name.equals("code")) {
          this.getCode().add(castToCoding(value));
        } else if (name.equals("slicing")) {
          this.slicing = (ElementDefinitionSlicingComponent) value; // ElementDefinitionSlicingComponent
        } else if (name.equals("short")) {
          this.short_ = castToString(value); // StringType
        } else if (name.equals("definition")) {
          this.definition = castToMarkdown(value); // MarkdownType
        } else if (name.equals("comment")) {
          this.comment = castToMarkdown(value); // MarkdownType
        } else if (name.equals("requirements")) {
          this.requirements = castToMarkdown(value); // MarkdownType
        } else if (name.equals("alias")) {
          this.getAlias().add(castToString(value));
        } else if (name.equals("min")) {
          this.min = castToUnsignedInt(value); // UnsignedIntType
        } else if (name.equals("max")) {
          this.max = castToString(value); // StringType
        } else if (name.equals("base")) {
          this.base = (ElementDefinitionBaseComponent) value; // ElementDefinitionBaseComponent
        } else if (name.equals("contentReference")) {
          this.contentReference = castToUri(value); // UriType
        } else if (name.equals("type")) {
          this.getType().add((TypeRefComponent) value);
        } else if (name.equals("defaultValue[x]")) {
          this.defaultValue = castToType(value); // org.hl7.fhir.dstu3.model.Type
        } else if (name.equals("meaningWhenMissing")) {
          this.meaningWhenMissing = castToMarkdown(value); // MarkdownType
        } else if (name.equals("orderMeaning")) {
          this.orderMeaning = castToString(value); // StringType
        } else if (name.equals("fixed[x]")) {
          this.fixed = castToType(value); // org.hl7.fhir.dstu3.model.Type
        } else if (name.equals("pattern[x]")) {
          this.pattern = castToType(value); // org.hl7.fhir.dstu3.model.Type
        } else if (name.equals("example")) {
          this.getExample().add((ElementDefinitionExampleComponent) value);
        } else if (name.equals("minValue[x]")) {
          this.minValue = castToType(value); // Type
        } else if (name.equals("maxValue[x]")) {
          this.maxValue = castToType(value); // Type
        } else if (name.equals("maxLength")) {
          this.maxLength = castToInteger(value); // IntegerType
        } else if (name.equals("condition")) {
          this.getCondition().add(castToId(value));
        } else if (name.equals("constraint")) {
          this.getConstraint().add((ElementDefinitionConstraintComponent) value);
        } else if (name.equals("mustSupport")) {
          this.mustSupport = castToBoolean(value); // BooleanType
        } else if (name.equals("isModifier")) {
          this.isModifier = castToBoolean(value); // BooleanType
        } else if (name.equals("isSummary")) {
          this.isSummary = castToBoolean(value); // BooleanType
        } else if (name.equals("binding")) {
          this.binding = (ElementDefinitionBindingComponent) value; // ElementDefinitionBindingComponent
        } else if (name.equals("mapping")) {
          this.getMapping().add((ElementDefinitionMappingComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3433509:  return getPathElement();
        case -671065907:  return addRepresentationElement();
        case -825289923:  return getSliceNameElement();
        case 102727412:  return getLabelElement();
        case 3059181:  return addCode(); 
        case -2119287345:  return getSlicing(); 
        case 109413500:  return getShortElement();
        case -1014418093:  return getDefinitionElement();
        case 950398559:  return getCommentElement();
        case -1619874672:  return getRequirementsElement();
        case 92902992:  return addAliasElement();
        case 108114:  return getMinElement();
        case 107876:  return getMaxElement();
        case 3016401:  return getBase(); 
        case 1193747154:  return getContentReferenceElement();
        case 3575610:  return addType(); 
        case 587922128:  return getDefaultValue(); 
        case -659125328:  return getDefaultValue(); 
        case 1857257103:  return getMeaningWhenMissingElement();
        case 1828196047:  return getOrderMeaningElement();
        case -391522164:  return getFixed(); 
        case 97445748:  return getFixed(); 
        case -885125392:  return getPattern(); 
        case -791090288:  return getPattern(); 
        case -1322970774:  return addExample(); 
        case -55301663:  return getMinValue(); 
        case -1376969153:  return getMinValue(); 
        case 622130931:  return getMaxValue(); 
        case 399227501:  return getMaxValue(); 
        case -791400086:  return getMaxLengthElement();
        case -861311717:  return addConditionElement();
        case -190376483:  return addConstraint(); 
        case -1402857082:  return getMustSupportElement();
        case -1408783839:  return getIsModifierElement();
        case 1857548060:  return getIsSummaryElement();
        case -108220795:  return getBinding(); 
        case 837556430:  return addMapping(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3433509: /*path*/ return new String[] {"string"};
        case -671065907: /*representation*/ return new String[] {"code"};
        case -825289923: /*sliceName*/ return new String[] {"string"};
        case 102727412: /*label*/ return new String[] {"string"};
        case 3059181: /*code*/ return new String[] {"Coding"};
        case -2119287345: /*slicing*/ return new String[] {};
        case 109413500: /*short*/ return new String[] {"string"};
        case -1014418093: /*definition*/ return new String[] {"markdown"};
        case 950398559: /*comment*/ return new String[] {"markdown"};
        case -1619874672: /*requirements*/ return new String[] {"markdown"};
        case 92902992: /*alias*/ return new String[] {"string"};
        case 108114: /*min*/ return new String[] {"unsignedInt"};
        case 107876: /*max*/ return new String[] {"string"};
        case 3016401: /*base*/ return new String[] {};
        case 1193747154: /*contentReference*/ return new String[] {"uri"};
        case 3575610: /*type*/ return new String[] {};
        case -659125328: /*defaultValue*/ return new String[] {"*"};
        case 1857257103: /*meaningWhenMissing*/ return new String[] {"markdown"};
        case 1828196047: /*orderMeaning*/ return new String[] {"string"};
        case 97445748: /*fixed*/ return new String[] {"*"};
        case -791090288: /*pattern*/ return new String[] {"*"};
        case -1322970774: /*example*/ return new String[] {};
        case -1376969153: /*minValue*/ return new String[] {"date", "dateTime", "instant", "time", "decimal", "integer", "positiveInt", "unsignedInt", "Quantity"};
        case 399227501: /*maxValue*/ return new String[] {"date", "dateTime", "instant", "time", "decimal", "integer", "positiveInt", "unsignedInt", "Quantity"};
        case -791400086: /*maxLength*/ return new String[] {"integer"};
        case -861311717: /*condition*/ return new String[] {"id"};
        case -190376483: /*constraint*/ return new String[] {};
        case -1402857082: /*mustSupport*/ return new String[] {"boolean"};
        case -1408783839: /*isModifier*/ return new String[] {"boolean"};
        case 1857548060: /*isSummary*/ return new String[] {"boolean"};
        case -108220795: /*binding*/ return new String[] {};
        case 837556430: /*mapping*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("path")) {
          throw new FHIRException("Cannot call addChild on a primitive type ElementDefinition.path");
        }
        else if (name.equals("representation")) {
          throw new FHIRException("Cannot call addChild on a primitive type ElementDefinition.representation");
        }
        else if (name.equals("sliceName")) {
          throw new FHIRException("Cannot call addChild on a primitive type ElementDefinition.sliceName");
        }
        else if (name.equals("label")) {
          throw new FHIRException("Cannot call addChild on a primitive type ElementDefinition.label");
        }
        else if (name.equals("code")) {
          return addCode();
        }
        else if (name.equals("slicing")) {
          this.slicing = new ElementDefinitionSlicingComponent();
          return this.slicing;
        }
        else if (name.equals("short")) {
          throw new FHIRException("Cannot call addChild on a primitive type ElementDefinition.short");
        }
        else if (name.equals("definition")) {
          throw new FHIRException("Cannot call addChild on a primitive type ElementDefinition.definition");
        }
        else if (name.equals("comment")) {
          throw new FHIRException("Cannot call addChild on a primitive type ElementDefinition.comment");
        }
        else if (name.equals("requirements")) {
          throw new FHIRException("Cannot call addChild on a primitive type ElementDefinition.requirements");
        }
        else if (name.equals("alias")) {
          throw new FHIRException("Cannot call addChild on a primitive type ElementDefinition.alias");
        }
        else if (name.equals("min")) {
          throw new FHIRException("Cannot call addChild on a primitive type ElementDefinition.min");
        }
        else if (name.equals("max")) {
          throw new FHIRException("Cannot call addChild on a primitive type ElementDefinition.max");
        }
        else if (name.equals("base")) {
          this.base = new ElementDefinitionBaseComponent();
          return this.base;
        }
        else if (name.equals("contentReference")) {
          throw new FHIRException("Cannot call addChild on a primitive type ElementDefinition.contentReference");
        }
        else if (name.equals("type")) {
          return addType();
        }
        else if (name.equals("defaultValueBoolean")) {
          this.defaultValue = new BooleanType();
          return this.defaultValue;
        }
        else if (name.equals("defaultValueInteger")) {
          this.defaultValue = new IntegerType();
          return this.defaultValue;
        }
        else if (name.equals("defaultValueDecimal")) {
          this.defaultValue = new DecimalType();
          return this.defaultValue;
        }
        else if (name.equals("defaultValueBase64Binary")) {
          this.defaultValue = new Base64BinaryType();
          return this.defaultValue;
        }
        else if (name.equals("defaultValueInstant")) {
          this.defaultValue = new InstantType();
          return this.defaultValue;
        }
        else if (name.equals("defaultValueString")) {
          this.defaultValue = new StringType();
          return this.defaultValue;
        }
        else if (name.equals("defaultValueUri")) {
          this.defaultValue = new UriType();
          return this.defaultValue;
        }
        else if (name.equals("defaultValueDate")) {
          this.defaultValue = new DateType();
          return this.defaultValue;
        }
        else if (name.equals("defaultValueDateTime")) {
          this.defaultValue = new DateTimeType();
          return this.defaultValue;
        }
        else if (name.equals("defaultValueTime")) {
          this.defaultValue = new TimeType();
          return this.defaultValue;
        }
        else if (name.equals("defaultValueCode")) {
          this.defaultValue = new CodeType();
          return this.defaultValue;
        }
        else if (name.equals("defaultValueOid")) {
          this.defaultValue = new OidType();
          return this.defaultValue;
        }
        else if (name.equals("defaultValueId")) {
          this.defaultValue = new IdType();
          return this.defaultValue;
        }
        else if (name.equals("defaultValueUnsignedInt")) {
          this.defaultValue = new UnsignedIntType();
          return this.defaultValue;
        }
        else if (name.equals("defaultValuePositiveInt")) {
          this.defaultValue = new PositiveIntType();
          return this.defaultValue;
        }
        else if (name.equals("defaultValueMarkdown")) {
          this.defaultValue = new MarkdownType();
          return this.defaultValue;
        }
        else if (name.equals("defaultValueAnnotation")) {
          this.defaultValue = new Annotation();
          return this.defaultValue;
        }
        else if (name.equals("defaultValueAttachment")) {
          this.defaultValue = new Attachment();
          return this.defaultValue;
        }
        else if (name.equals("defaultValueIdentifier")) {
          this.defaultValue = new Identifier();
          return this.defaultValue;
        }
        else if (name.equals("defaultValueCodeableConcept")) {
          this.defaultValue = new CodeableConcept();
          return this.defaultValue;
        }
        else if (name.equals("defaultValueCoding")) {
          this.defaultValue = new Coding();
          return this.defaultValue;
        }
        else if (name.equals("defaultValueQuantity")) {
          this.defaultValue = new Quantity();
          return this.defaultValue;
        }
        else if (name.equals("defaultValueRange")) {
          this.defaultValue = new Range();
          return this.defaultValue;
        }
        else if (name.equals("defaultValuePeriod")) {
          this.defaultValue = new Period();
          return this.defaultValue;
        }
        else if (name.equals("defaultValueRatio")) {
          this.defaultValue = new Ratio();
          return this.defaultValue;
        }
        else if (name.equals("defaultValueSampledData")) {
          this.defaultValue = new SampledData();
          return this.defaultValue;
        }
        else if (name.equals("defaultValueSignature")) {
          this.defaultValue = new Signature();
          return this.defaultValue;
        }
        else if (name.equals("defaultValueHumanName")) {
          this.defaultValue = new HumanName();
          return this.defaultValue;
        }
        else if (name.equals("defaultValueAddress")) {
          this.defaultValue = new Address();
          return this.defaultValue;
        }
        else if (name.equals("defaultValueContactPoint")) {
          this.defaultValue = new ContactPoint();
          return this.defaultValue;
        }
        else if (name.equals("defaultValueTiming")) {
          this.defaultValue = new Timing();
          return this.defaultValue;
        }
        else if (name.equals("defaultValueReference")) {
          this.defaultValue = new Reference();
          return this.defaultValue;
        }
        else if (name.equals("defaultValueMeta")) {
          this.defaultValue = new Meta();
          return this.defaultValue;
        }
        else if (name.equals("meaningWhenMissing")) {
          throw new FHIRException("Cannot call addChild on a primitive type ElementDefinition.meaningWhenMissing");
        }
        else if (name.equals("orderMeaning")) {
          throw new FHIRException("Cannot call addChild on a primitive type ElementDefinition.orderMeaning");
        }
        else if (name.equals("fixedBoolean")) {
          this.fixed = new BooleanType();
          return this.fixed;
        }
        else if (name.equals("fixedInteger")) {
          this.fixed = new IntegerType();
          return this.fixed;
        }
        else if (name.equals("fixedDecimal")) {
          this.fixed = new DecimalType();
          return this.fixed;
        }
        else if (name.equals("fixedBase64Binary")) {
          this.fixed = new Base64BinaryType();
          return this.fixed;
        }
        else if (name.equals("fixedInstant")) {
          this.fixed = new InstantType();
          return this.fixed;
        }
        else if (name.equals("fixedString")) {
          this.fixed = new StringType();
          return this.fixed;
        }
        else if (name.equals("fixedUri")) {
          this.fixed = new UriType();
          return this.fixed;
        }
        else if (name.equals("fixedDate")) {
          this.fixed = new DateType();
          return this.fixed;
        }
        else if (name.equals("fixedDateTime")) {
          this.fixed = new DateTimeType();
          return this.fixed;
        }
        else if (name.equals("fixedTime")) {
          this.fixed = new TimeType();
          return this.fixed;
        }
        else if (name.equals("fixedCode")) {
          this.fixed = new CodeType();
          return this.fixed;
        }
        else if (name.equals("fixedOid")) {
          this.fixed = new OidType();
          return this.fixed;
        }
        else if (name.equals("fixedId")) {
          this.fixed = new IdType();
          return this.fixed;
        }
        else if (name.equals("fixedUnsignedInt")) {
          this.fixed = new UnsignedIntType();
          return this.fixed;
        }
        else if (name.equals("fixedPositiveInt")) {
          this.fixed = new PositiveIntType();
          return this.fixed;
        }
        else if (name.equals("fixedMarkdown")) {
          this.fixed = new MarkdownType();
          return this.fixed;
        }
        else if (name.equals("fixedAnnotation")) {
          this.fixed = new Annotation();
          return this.fixed;
        }
        else if (name.equals("fixedAttachment")) {
          this.fixed = new Attachment();
          return this.fixed;
        }
        else if (name.equals("fixedIdentifier")) {
          this.fixed = new Identifier();
          return this.fixed;
        }
        else if (name.equals("fixedCodeableConcept")) {
          this.fixed = new CodeableConcept();
          return this.fixed;
        }
        else if (name.equals("fixedCoding")) {
          this.fixed = new Coding();
          return this.fixed;
        }
        else if (name.equals("fixedQuantity")) {
          this.fixed = new Quantity();
          return this.fixed;
        }
        else if (name.equals("fixedRange")) {
          this.fixed = new Range();
          return this.fixed;
        }
        else if (name.equals("fixedPeriod")) {
          this.fixed = new Period();
          return this.fixed;
        }
        else if (name.equals("fixedRatio")) {
          this.fixed = new Ratio();
          return this.fixed;
        }
        else if (name.equals("fixedSampledData")) {
          this.fixed = new SampledData();
          return this.fixed;
        }
        else if (name.equals("fixedSignature")) {
          this.fixed = new Signature();
          return this.fixed;
        }
        else if (name.equals("fixedHumanName")) {
          this.fixed = new HumanName();
          return this.fixed;
        }
        else if (name.equals("fixedAddress")) {
          this.fixed = new Address();
          return this.fixed;
        }
        else if (name.equals("fixedContactPoint")) {
          this.fixed = new ContactPoint();
          return this.fixed;
        }
        else if (name.equals("fixedTiming")) {
          this.fixed = new Timing();
          return this.fixed;
        }
        else if (name.equals("fixedReference")) {
          this.fixed = new Reference();
          return this.fixed;
        }
        else if (name.equals("fixedMeta")) {
          this.fixed = new Meta();
          return this.fixed;
        }
        else if (name.equals("patternBoolean")) {
          this.pattern = new BooleanType();
          return this.pattern;
        }
        else if (name.equals("patternInteger")) {
          this.pattern = new IntegerType();
          return this.pattern;
        }
        else if (name.equals("patternDecimal")) {
          this.pattern = new DecimalType();
          return this.pattern;
        }
        else if (name.equals("patternBase64Binary")) {
          this.pattern = new Base64BinaryType();
          return this.pattern;
        }
        else if (name.equals("patternInstant")) {
          this.pattern = new InstantType();
          return this.pattern;
        }
        else if (name.equals("patternString")) {
          this.pattern = new StringType();
          return this.pattern;
        }
        else if (name.equals("patternUri")) {
          this.pattern = new UriType();
          return this.pattern;
        }
        else if (name.equals("patternDate")) {
          this.pattern = new DateType();
          return this.pattern;
        }
        else if (name.equals("patternDateTime")) {
          this.pattern = new DateTimeType();
          return this.pattern;
        }
        else if (name.equals("patternTime")) {
          this.pattern = new TimeType();
          return this.pattern;
        }
        else if (name.equals("patternCode")) {
          this.pattern = new CodeType();
          return this.pattern;
        }
        else if (name.equals("patternOid")) {
          this.pattern = new OidType();
          return this.pattern;
        }
        else if (name.equals("patternId")) {
          this.pattern = new IdType();
          return this.pattern;
        }
        else if (name.equals("patternUnsignedInt")) {
          this.pattern = new UnsignedIntType();
          return this.pattern;
        }
        else if (name.equals("patternPositiveInt")) {
          this.pattern = new PositiveIntType();
          return this.pattern;
        }
        else if (name.equals("patternMarkdown")) {
          this.pattern = new MarkdownType();
          return this.pattern;
        }
        else if (name.equals("patternAnnotation")) {
          this.pattern = new Annotation();
          return this.pattern;
        }
        else if (name.equals("patternAttachment")) {
          this.pattern = new Attachment();
          return this.pattern;
        }
        else if (name.equals("patternIdentifier")) {
          this.pattern = new Identifier();
          return this.pattern;
        }
        else if (name.equals("patternCodeableConcept")) {
          this.pattern = new CodeableConcept();
          return this.pattern;
        }
        else if (name.equals("patternCoding")) {
          this.pattern = new Coding();
          return this.pattern;
        }
        else if (name.equals("patternQuantity")) {
          this.pattern = new Quantity();
          return this.pattern;
        }
        else if (name.equals("patternRange")) {
          this.pattern = new Range();
          return this.pattern;
        }
        else if (name.equals("patternPeriod")) {
          this.pattern = new Period();
          return this.pattern;
        }
        else if (name.equals("patternRatio")) {
          this.pattern = new Ratio();
          return this.pattern;
        }
        else if (name.equals("patternSampledData")) {
          this.pattern = new SampledData();
          return this.pattern;
        }
        else if (name.equals("patternSignature")) {
          this.pattern = new Signature();
          return this.pattern;
        }
        else if (name.equals("patternHumanName")) {
          this.pattern = new HumanName();
          return this.pattern;
        }
        else if (name.equals("patternAddress")) {
          this.pattern = new Address();
          return this.pattern;
        }
        else if (name.equals("patternContactPoint")) {
          this.pattern = new ContactPoint();
          return this.pattern;
        }
        else if (name.equals("patternTiming")) {
          this.pattern = new Timing();
          return this.pattern;
        }
        else if (name.equals("patternReference")) {
          this.pattern = new Reference();
          return this.pattern;
        }
        else if (name.equals("patternMeta")) {
          this.pattern = new Meta();
          return this.pattern;
        }
        else if (name.equals("example")) {
          return addExample();
        }
        else if (name.equals("minValueDate")) {
          this.minValue = new DateType();
          return this.minValue;
        }
        else if (name.equals("minValueDateTime")) {
          this.minValue = new DateTimeType();
          return this.minValue;
        }
        else if (name.equals("minValueInstant")) {
          this.minValue = new InstantType();
          return this.minValue;
        }
        else if (name.equals("minValueTime")) {
          this.minValue = new TimeType();
          return this.minValue;
        }
        else if (name.equals("minValueDecimal")) {
          this.minValue = new DecimalType();
          return this.minValue;
        }
        else if (name.equals("minValueInteger")) {
          this.minValue = new IntegerType();
          return this.minValue;
        }
        else if (name.equals("minValuePositiveInt")) {
          this.minValue = new PositiveIntType();
          return this.minValue;
        }
        else if (name.equals("minValueUnsignedInt")) {
          this.minValue = new UnsignedIntType();
          return this.minValue;
        }
        else if (name.equals("minValueQuantity")) {
          this.minValue = new Quantity();
          return this.minValue;
        }
        else if (name.equals("maxValueDate")) {
          this.maxValue = new DateType();
          return this.maxValue;
        }
        else if (name.equals("maxValueDateTime")) {
          this.maxValue = new DateTimeType();
          return this.maxValue;
        }
        else if (name.equals("maxValueInstant")) {
          this.maxValue = new InstantType();
          return this.maxValue;
        }
        else if (name.equals("maxValueTime")) {
          this.maxValue = new TimeType();
          return this.maxValue;
        }
        else if (name.equals("maxValueDecimal")) {
          this.maxValue = new DecimalType();
          return this.maxValue;
        }
        else if (name.equals("maxValueInteger")) {
          this.maxValue = new IntegerType();
          return this.maxValue;
        }
        else if (name.equals("maxValuePositiveInt")) {
          this.maxValue = new PositiveIntType();
          return this.maxValue;
        }
        else if (name.equals("maxValueUnsignedInt")) {
          this.maxValue = new UnsignedIntType();
          return this.maxValue;
        }
        else if (name.equals("maxValueQuantity")) {
          this.maxValue = new Quantity();
          return this.maxValue;
        }
        else if (name.equals("maxLength")) {
          throw new FHIRException("Cannot call addChild on a primitive type ElementDefinition.maxLength");
        }
        else if (name.equals("condition")) {
          throw new FHIRException("Cannot call addChild on a primitive type ElementDefinition.condition");
        }
        else if (name.equals("constraint")) {
          return addConstraint();
        }
        else if (name.equals("mustSupport")) {
          throw new FHIRException("Cannot call addChild on a primitive type ElementDefinition.mustSupport");
        }
        else if (name.equals("isModifier")) {
          throw new FHIRException("Cannot call addChild on a primitive type ElementDefinition.isModifier");
        }
        else if (name.equals("isSummary")) {
          throw new FHIRException("Cannot call addChild on a primitive type ElementDefinition.isSummary");
        }
        else if (name.equals("binding")) {
          this.binding = new ElementDefinitionBindingComponent();
          return this.binding;
        }
        else if (name.equals("mapping")) {
          return addMapping();
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "ElementDefinition";

  }

      public ElementDefinition copy() {
        ElementDefinition dst = new ElementDefinition();
        copyValues(dst);
        dst.path = path == null ? null : path.copy();
        if (representation != null) {
          dst.representation = new ArrayList<Enumeration<PropertyRepresentation>>();
          for (Enumeration<PropertyRepresentation> i : representation)
            dst.representation.add(i.copy());
        };
        dst.sliceName = sliceName == null ? null : sliceName.copy();
        dst.label = label == null ? null : label.copy();
        if (code != null) {
          dst.code = new ArrayList<Coding>();
          for (Coding i : code)
            dst.code.add(i.copy());
        };
        dst.slicing = slicing == null ? null : slicing.copy();
        dst.short_ = short_ == null ? null : short_.copy();
        dst.definition = definition == null ? null : definition.copy();
        dst.comment = comment == null ? null : comment.copy();
        dst.requirements = requirements == null ? null : requirements.copy();
        if (alias != null) {
          dst.alias = new ArrayList<StringType>();
          for (StringType i : alias)
            dst.alias.add(i.copy());
        };
        dst.min = min == null ? null : min.copy();
        dst.max = max == null ? null : max.copy();
        dst.base = base == null ? null : base.copy();
        dst.contentReference = contentReference == null ? null : contentReference.copy();
        if (type != null) {
          dst.type = new ArrayList<TypeRefComponent>();
          for (TypeRefComponent i : type)
            dst.type.add(i.copy());
        };
        dst.defaultValue = defaultValue == null ? null : defaultValue.copy();
        dst.meaningWhenMissing = meaningWhenMissing == null ? null : meaningWhenMissing.copy();
        dst.orderMeaning = orderMeaning == null ? null : orderMeaning.copy();
        dst.fixed = fixed == null ? null : fixed.copy();
        dst.pattern = pattern == null ? null : pattern.copy();
        if (example != null) {
          dst.example = new ArrayList<ElementDefinitionExampleComponent>();
          for (ElementDefinitionExampleComponent i : example)
            dst.example.add(i.copy());
        };
        dst.minValue = minValue == null ? null : minValue.copy();
        dst.maxValue = maxValue == null ? null : maxValue.copy();
        dst.maxLength = maxLength == null ? null : maxLength.copy();
        if (condition != null) {
          dst.condition = new ArrayList<IdType>();
          for (IdType i : condition)
            dst.condition.add(i.copy());
        };
        if (constraint != null) {
          dst.constraint = new ArrayList<ElementDefinitionConstraintComponent>();
          for (ElementDefinitionConstraintComponent i : constraint)
            dst.constraint.add(i.copy());
        };
        dst.mustSupport = mustSupport == null ? null : mustSupport.copy();
        dst.isModifier = isModifier == null ? null : isModifier.copy();
        dst.isSummary = isSummary == null ? null : isSummary.copy();
        dst.binding = binding == null ? null : binding.copy();
        if (mapping != null) {
          dst.mapping = new ArrayList<ElementDefinitionMappingComponent>();
          for (ElementDefinitionMappingComponent i : mapping)
            dst.mapping.add(i.copy());
        };
        return dst;
      }

      protected ElementDefinition typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ElementDefinition))
          return false;
        ElementDefinition o = (ElementDefinition) other;
        return compareDeep(path, o.path, true) && compareDeep(representation, o.representation, true) && compareDeep(sliceName, o.sliceName, true)
           && compareDeep(label, o.label, true) && compareDeep(code, o.code, true) && compareDeep(slicing, o.slicing, true)
           && compareDeep(short_, o.short_, true) && compareDeep(definition, o.definition, true) && compareDeep(comment, o.comment, true)
           && compareDeep(requirements, o.requirements, true) && compareDeep(alias, o.alias, true) && compareDeep(min, o.min, true)
           && compareDeep(max, o.max, true) && compareDeep(base, o.base, true) && compareDeep(contentReference, o.contentReference, true)
           && compareDeep(type, o.type, true) && compareDeep(defaultValue, o.defaultValue, true) && compareDeep(meaningWhenMissing, o.meaningWhenMissing, true)
           && compareDeep(orderMeaning, o.orderMeaning, true) && compareDeep(fixed, o.fixed, true) && compareDeep(pattern, o.pattern, true)
           && compareDeep(example, o.example, true) && compareDeep(minValue, o.minValue, true) && compareDeep(maxValue, o.maxValue, true)
           && compareDeep(maxLength, o.maxLength, true) && compareDeep(condition, o.condition, true) && compareDeep(constraint, o.constraint, true)
           && compareDeep(mustSupport, o.mustSupport, true) && compareDeep(isModifier, o.isModifier, true)
           && compareDeep(isSummary, o.isSummary, true) && compareDeep(binding, o.binding, true) && compareDeep(mapping, o.mapping, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ElementDefinition))
          return false;
        ElementDefinition o = (ElementDefinition) other;
        return compareValues(path, o.path, true) && compareValues(representation, o.representation, true) && compareValues(sliceName, o.sliceName, true)
           && compareValues(label, o.label, true) && compareValues(short_, o.short_, true) && compareValues(definition, o.definition, true)
           && compareValues(comment, o.comment, true) && compareValues(requirements, o.requirements, true) && compareValues(alias, o.alias, true)
           && compareValues(min, o.min, true) && compareValues(max, o.max, true) && compareValues(contentReference, o.contentReference, true)
           && compareValues(meaningWhenMissing, o.meaningWhenMissing, true) && compareValues(orderMeaning, o.orderMeaning, true)
           && compareValues(maxLength, o.maxLength, true) && compareValues(condition, o.condition, true) && compareValues(mustSupport, o.mustSupport, true)
           && compareValues(isModifier, o.isModifier, true) && compareValues(isSummary, o.isSummary, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(path, representation, sliceName
          , label, code, slicing, short_, definition, comment, requirements, alias, min
          , max, base, contentReference, type, defaultValue, meaningWhenMissing, orderMeaning
          , fixed, pattern, example, minValue, maxValue, maxLength, condition, constraint
          , mustSupport, isModifier, isSummary, binding, mapping);
      }

// added from java-adornments.txt:
  
  public String toString() {
    if (hasId())
      return getId();
    if (hasSliceName())
      return getPath()+":"+getSliceName();
    else
      return getPath();
  }
    
  public void makeBase(String path, int min, String max) {
    ElementDefinitionBaseComponent self = getBase();
    self.setPath(path);
    self.setMin(min);
    self.setMax(max);
  }
  

// end addition

}

