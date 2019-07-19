package org.hl7.fhir.dstu2016may.model;

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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hl7.fhir.dstu2016may.model.Enumerations.ConformanceResourceStatus;
import org.hl7.fhir.dstu2016may.model.Enumerations.ConformanceResourceStatusEnumFactory;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.IBaseBackboneElement;
import org.hl7.fhir.utilities.Utilities;

import ca.uhn.fhir.model.api.annotation.Block;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
/**
 * A Map of relationships between 2 structures that can be used to transform data.
 */
@ResourceDef(name="StructureMap", profile="http://hl7.org/fhir/Profile/StructureMap")
public class StructureMap extends DomainResource {

    public enum StructureMapModelMode {
        /**
         * This structure describes an instance passed to the mapping engine that is used a source of data
         */
        SOURCE, 
        /**
         * This structure describes an instance that the mapping engine may ask for that is used a source of data
         */
        QUERIED, 
        /**
         * This structure describes an instance passed to the mapping engine that is used a target of data
         */
        TARGET, 
        /**
         * This structure describes an instance that the mapping engine may ask to create that is used a target of data
         */
        PRODUCED, 
        /**
         * added to help the parsers
         */
        NULL;
        public static StructureMapModelMode fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("source".equals(codeString))
          return SOURCE;
        if ("queried".equals(codeString))
          return QUERIED;
        if ("target".equals(codeString))
          return TARGET;
        if ("produced".equals(codeString))
          return PRODUCED;
        throw new FHIRException("Unknown StructureMapModelMode code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case SOURCE: return "source";
            case QUERIED: return "queried";
            case TARGET: return "target";
            case PRODUCED: return "produced";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case SOURCE: return "http://hl7.org/fhir/map-model-mode";
            case QUERIED: return "http://hl7.org/fhir/map-model-mode";
            case TARGET: return "http://hl7.org/fhir/map-model-mode";
            case PRODUCED: return "http://hl7.org/fhir/map-model-mode";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case SOURCE: return "This structure describes an instance passed to the mapping engine that is used a source of data";
            case QUERIED: return "This structure describes an instance that the mapping engine may ask for that is used a source of data";
            case TARGET: return "This structure describes an instance passed to the mapping engine that is used a target of data";
            case PRODUCED: return "This structure describes an instance that the mapping engine may ask to create that is used a target of data";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case SOURCE: return "Source Structure Definition";
            case QUERIED: return "Queried Structure Definition";
            case TARGET: return "Target Structure Definition";
            case PRODUCED: return "Produced Structure Definition";
            default: return "?";
          }
        }
    }

  public static class StructureMapModelModeEnumFactory implements EnumFactory<StructureMapModelMode> {
    public StructureMapModelMode fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("source".equals(codeString))
          return StructureMapModelMode.SOURCE;
        if ("queried".equals(codeString))
          return StructureMapModelMode.QUERIED;
        if ("target".equals(codeString))
          return StructureMapModelMode.TARGET;
        if ("produced".equals(codeString))
          return StructureMapModelMode.PRODUCED;
        throw new IllegalArgumentException("Unknown StructureMapModelMode code '"+codeString+"'");
        }
        public Enumeration<StructureMapModelMode> fromType(Base code) throws FHIRException {
          if (code == null || code.isEmpty())
            return null;
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("source".equals(codeString))
          return new Enumeration<StructureMapModelMode>(this, StructureMapModelMode.SOURCE);
        if ("queried".equals(codeString))
          return new Enumeration<StructureMapModelMode>(this, StructureMapModelMode.QUERIED);
        if ("target".equals(codeString))
          return new Enumeration<StructureMapModelMode>(this, StructureMapModelMode.TARGET);
        if ("produced".equals(codeString))
          return new Enumeration<StructureMapModelMode>(this, StructureMapModelMode.PRODUCED);
        throw new FHIRException("Unknown StructureMapModelMode code '"+codeString+"'");
        }
    public String toCode(StructureMapModelMode code) {
      if (code == StructureMapModelMode.SOURCE)
        return "source";
      if (code == StructureMapModelMode.QUERIED)
        return "queried";
      if (code == StructureMapModelMode.TARGET)
        return "target";
      if (code == StructureMapModelMode.PRODUCED)
        return "produced";
      return "?";
      }
    public String toSystem(StructureMapModelMode code) {
      return code.getSystem();
      }
    }

    public enum StructureMapInputMode {
        /**
         * Names an input instance used a source for mapping
         */
        SOURCE, 
        /**
         * Names an instance that is being populated
         */
        TARGET, 
        /**
         * added to help the parsers
         */
        NULL;
        public static StructureMapInputMode fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("source".equals(codeString))
          return SOURCE;
        if ("target".equals(codeString))
          return TARGET;
        throw new FHIRException("Unknown StructureMapInputMode code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case SOURCE: return "source";
            case TARGET: return "target";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case SOURCE: return "http://hl7.org/fhir/map-input-mode";
            case TARGET: return "http://hl7.org/fhir/map-input-mode";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case SOURCE: return "Names an input instance used a source for mapping";
            case TARGET: return "Names an instance that is being populated";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case SOURCE: return "Source Instance";
            case TARGET: return "Target Instance";
            default: return "?";
          }
        }
    }

  public static class StructureMapInputModeEnumFactory implements EnumFactory<StructureMapInputMode> {
    public StructureMapInputMode fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("source".equals(codeString))
          return StructureMapInputMode.SOURCE;
        if ("target".equals(codeString))
          return StructureMapInputMode.TARGET;
        throw new IllegalArgumentException("Unknown StructureMapInputMode code '"+codeString+"'");
        }
        public Enumeration<StructureMapInputMode> fromType(Base code) throws FHIRException {
          if (code == null || code.isEmpty())
            return null;
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("source".equals(codeString))
          return new Enumeration<StructureMapInputMode>(this, StructureMapInputMode.SOURCE);
        if ("target".equals(codeString))
          return new Enumeration<StructureMapInputMode>(this, StructureMapInputMode.TARGET);
        throw new FHIRException("Unknown StructureMapInputMode code '"+codeString+"'");
        }
    public String toCode(StructureMapInputMode code) {
      if (code == StructureMapInputMode.SOURCE)
        return "source";
      if (code == StructureMapInputMode.TARGET)
        return "target";
      return "?";
      }
    public String toSystem(StructureMapInputMode code) {
      return code.getSystem();
      }
    }

    public enum StructureMapContextType {
        /**
         * The context specifies a type
         */
        TYPE, 
        /**
         * The context specifies a variable
         */
        VARIABLE, 
        /**
         * added to help the parsers
         */
        NULL;
        public static StructureMapContextType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("type".equals(codeString))
          return TYPE;
        if ("variable".equals(codeString))
          return VARIABLE;
        throw new FHIRException("Unknown StructureMapContextType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case TYPE: return "type";
            case VARIABLE: return "variable";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case TYPE: return "http://hl7.org/fhir/map-context-type";
            case VARIABLE: return "http://hl7.org/fhir/map-context-type";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case TYPE: return "The context specifies a type";
            case VARIABLE: return "The context specifies a variable";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case TYPE: return "Type";
            case VARIABLE: return "Variable";
            default: return "?";
          }
        }
    }

  public static class StructureMapContextTypeEnumFactory implements EnumFactory<StructureMapContextType> {
    public StructureMapContextType fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("type".equals(codeString))
          return StructureMapContextType.TYPE;
        if ("variable".equals(codeString))
          return StructureMapContextType.VARIABLE;
        throw new IllegalArgumentException("Unknown StructureMapContextType code '"+codeString+"'");
        }
        public Enumeration<StructureMapContextType> fromType(Base code) throws FHIRException {
          if (code == null || code.isEmpty())
            return null;
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("type".equals(codeString))
          return new Enumeration<StructureMapContextType>(this, StructureMapContextType.TYPE);
        if ("variable".equals(codeString))
          return new Enumeration<StructureMapContextType>(this, StructureMapContextType.VARIABLE);
        throw new FHIRException("Unknown StructureMapContextType code '"+codeString+"'");
        }
    public String toCode(StructureMapContextType code) {
      if (code == StructureMapContextType.TYPE)
        return "type";
      if (code == StructureMapContextType.VARIABLE)
        return "variable";
      return "?";
      }
    public String toSystem(StructureMapContextType code) {
      return code.getSystem();
      }
    }

    public enum StructureMapListMode {
        /**
         * when the target list is being assembled, the items for this rule go first. If more that one rule defines a first item (for a given instance of mapping) then this is an error
         */
        FIRST, 
        /**
         * the target instance is shared with the target instances generated by another rule (up to the first common n items, then create new ones)
         */
        SHARE, 
        /**
         * when the target list is being assembled, the items for this rule go last. If more that one rule defines a last item (for a given instance of mapping) then this is an error
         */
        LAST, 
        /**
         * added to help the parsers
         */
        NULL;
        public static StructureMapListMode fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("first".equals(codeString))
          return FIRST;
        if ("share".equals(codeString))
          return SHARE;
        if ("last".equals(codeString))
          return LAST;
        throw new FHIRException("Unknown StructureMapListMode code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case FIRST: return "first";
            case SHARE: return "share";
            case LAST: return "last";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case FIRST: return "http://hl7.org/fhir/map-list-mode";
            case SHARE: return "http://hl7.org/fhir/map-list-mode";
            case LAST: return "http://hl7.org/fhir/map-list-mode";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case FIRST: return "when the target list is being assembled, the items for this rule go first. If more that one rule defines a first item (for a given instance of mapping) then this is an error";
            case SHARE: return "the target instance is shared with the target instances generated by another rule (up to the first common n items, then create new ones)";
            case LAST: return "when the target list is being assembled, the items for this rule go last. If more that one rule defines a last item (for a given instance of mapping) then this is an error";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case FIRST: return "First";
            case SHARE: return "Share";
            case LAST: return "Last";
            default: return "?";
          }
        }
    }

  public static class StructureMapListModeEnumFactory implements EnumFactory<StructureMapListMode> {
    public StructureMapListMode fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("first".equals(codeString))
          return StructureMapListMode.FIRST;
        if ("share".equals(codeString))
          return StructureMapListMode.SHARE;
        if ("last".equals(codeString))
          return StructureMapListMode.LAST;
        throw new IllegalArgumentException("Unknown StructureMapListMode code '"+codeString+"'");
        }
        public Enumeration<StructureMapListMode> fromType(Base code) throws FHIRException {
          if (code == null || code.isEmpty())
            return null;
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("first".equals(codeString))
          return new Enumeration<StructureMapListMode>(this, StructureMapListMode.FIRST);
        if ("share".equals(codeString))
          return new Enumeration<StructureMapListMode>(this, StructureMapListMode.SHARE);
        if ("last".equals(codeString))
          return new Enumeration<StructureMapListMode>(this, StructureMapListMode.LAST);
        throw new FHIRException("Unknown StructureMapListMode code '"+codeString+"'");
        }
    public String toCode(StructureMapListMode code) {
      if (code == StructureMapListMode.FIRST)
        return "first";
      if (code == StructureMapListMode.SHARE)
        return "share";
      if (code == StructureMapListMode.LAST)
        return "last";
      return "?";
      }
    public String toSystem(StructureMapListMode code) {
      return code.getSystem();
      }
    }

    public enum StructureMapTransform {
        /**
         * create(type : string) - type is passed through to the application on the standard API, and must be known by it
         */
        CREATE, 
        /**
         * copy(source)
         */
        COPY, 
        /**
         * truncate(source, length) - source must be stringy type
         */
        TRUNCATE, 
        /**
         * escape(source, fmt1, fmt2) - change source from one kind of escaping to another (plain, java, xml, json). note that this is for when the string itself is escaped
         */
        ESCAPE, 
        /**
         * cast(source, type?) - case source from one type to another. target type can be left as implicit if there is one and only one target type known
         */
        CAST, 
        /**
         * append(source...) - source is element or string
         */
        APPEND, 
        /**
         * translate(source, uri_of_map) - use the translate operation
         */
        TRANSLATE, 
        /**
         * reference(source : object) - return a string that references the provided tree properly
         */
        REFERENCE, 
        /**
         * something
         */
        DATEOP, 
        /**
         * something
         */
        UUID, 
        /**
         * something
         */
        POINTER, 
        /**
         * something
         */
        EVALUATE, 
        /**
         * added to help the parsers
         */
        NULL;
        public static StructureMapTransform fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("create".equals(codeString))
          return CREATE;
        if ("copy".equals(codeString))
          return COPY;
        if ("truncate".equals(codeString))
          return TRUNCATE;
        if ("escape".equals(codeString))
          return ESCAPE;
        if ("cast".equals(codeString))
          return CAST;
        if ("append".equals(codeString))
          return APPEND;
        if ("translate".equals(codeString))
          return TRANSLATE;
        if ("reference".equals(codeString))
          return REFERENCE;
        if ("dateOp".equals(codeString))
          return DATEOP;
        if ("uuid".equals(codeString))
          return UUID;
        if ("pointer".equals(codeString))
          return POINTER;
        if ("evaluate".equals(codeString))
          return EVALUATE;
        throw new FHIRException("Unknown StructureMapTransform code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case CREATE: return "create";
            case COPY: return "copy";
            case TRUNCATE: return "truncate";
            case ESCAPE: return "escape";
            case CAST: return "cast";
            case APPEND: return "append";
            case TRANSLATE: return "translate";
            case REFERENCE: return "reference";
            case DATEOP: return "dateOp";
            case UUID: return "uuid";
            case POINTER: return "pointer";
            case EVALUATE: return "evaluate";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case CREATE: return "http://hl7.org/fhir/map-transform";
            case COPY: return "http://hl7.org/fhir/map-transform";
            case TRUNCATE: return "http://hl7.org/fhir/map-transform";
            case ESCAPE: return "http://hl7.org/fhir/map-transform";
            case CAST: return "http://hl7.org/fhir/map-transform";
            case APPEND: return "http://hl7.org/fhir/map-transform";
            case TRANSLATE: return "http://hl7.org/fhir/map-transform";
            case REFERENCE: return "http://hl7.org/fhir/map-transform";
            case DATEOP: return "http://hl7.org/fhir/map-transform";
            case UUID: return "http://hl7.org/fhir/map-transform";
            case POINTER: return "http://hl7.org/fhir/map-transform";
            case EVALUATE: return "http://hl7.org/fhir/map-transform";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case CREATE: return "create(type : string) - type is passed through to the application on the standard API, and must be known by it";
            case COPY: return "copy(source)";
            case TRUNCATE: return "truncate(source, length) - source must be stringy type";
            case ESCAPE: return "escape(source, fmt1, fmt2) - change source from one kind of escaping to another (plain, java, xml, json). note that this is for when the string itself is escaped";
            case CAST: return "cast(source, type?) - case source from one type to another. target type can be left as implicit if there is one and only one target type known";
            case APPEND: return "append(source...) - source is element or string";
            case TRANSLATE: return "translate(source, uri_of_map) - use the translate operation";
            case REFERENCE: return "reference(source : object) - return a string that references the provided tree properly";
            case DATEOP: return "something";
            case UUID: return "something";
            case POINTER: return "something";
            case EVALUATE: return "something";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case CREATE: return "create";
            case COPY: return "copy";
            case TRUNCATE: return "truncate";
            case ESCAPE: return "escape";
            case CAST: return "cast";
            case APPEND: return "append";
            case TRANSLATE: return "translate";
            case REFERENCE: return "reference";
            case DATEOP: return "dateOp";
            case UUID: return "uuid";
            case POINTER: return "pointer";
            case EVALUATE: return "evaluate";
            default: return "?";
          }
        }
    }

  public static class StructureMapTransformEnumFactory implements EnumFactory<StructureMapTransform> {
    public StructureMapTransform fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("create".equals(codeString))
          return StructureMapTransform.CREATE;
        if ("copy".equals(codeString))
          return StructureMapTransform.COPY;
        if ("truncate".equals(codeString))
          return StructureMapTransform.TRUNCATE;
        if ("escape".equals(codeString))
          return StructureMapTransform.ESCAPE;
        if ("cast".equals(codeString))
          return StructureMapTransform.CAST;
        if ("append".equals(codeString))
          return StructureMapTransform.APPEND;
        if ("translate".equals(codeString))
          return StructureMapTransform.TRANSLATE;
        if ("reference".equals(codeString))
          return StructureMapTransform.REFERENCE;
        if ("dateOp".equals(codeString))
          return StructureMapTransform.DATEOP;
        if ("uuid".equals(codeString))
          return StructureMapTransform.UUID;
        if ("pointer".equals(codeString))
          return StructureMapTransform.POINTER;
        if ("evaluate".equals(codeString))
          return StructureMapTransform.EVALUATE;
        throw new IllegalArgumentException("Unknown StructureMapTransform code '"+codeString+"'");
        }
        public Enumeration<StructureMapTransform> fromType(Base code) throws FHIRException {
          if (code == null || code.isEmpty())
            return null;
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("create".equals(codeString))
          return new Enumeration<StructureMapTransform>(this, StructureMapTransform.CREATE);
        if ("copy".equals(codeString))
          return new Enumeration<StructureMapTransform>(this, StructureMapTransform.COPY);
        if ("truncate".equals(codeString))
          return new Enumeration<StructureMapTransform>(this, StructureMapTransform.TRUNCATE);
        if ("escape".equals(codeString))
          return new Enumeration<StructureMapTransform>(this, StructureMapTransform.ESCAPE);
        if ("cast".equals(codeString))
          return new Enumeration<StructureMapTransform>(this, StructureMapTransform.CAST);
        if ("append".equals(codeString))
          return new Enumeration<StructureMapTransform>(this, StructureMapTransform.APPEND);
        if ("translate".equals(codeString))
          return new Enumeration<StructureMapTransform>(this, StructureMapTransform.TRANSLATE);
        if ("reference".equals(codeString))
          return new Enumeration<StructureMapTransform>(this, StructureMapTransform.REFERENCE);
        if ("dateOp".equals(codeString))
          return new Enumeration<StructureMapTransform>(this, StructureMapTransform.DATEOP);
        if ("uuid".equals(codeString))
          return new Enumeration<StructureMapTransform>(this, StructureMapTransform.UUID);
        if ("pointer".equals(codeString))
          return new Enumeration<StructureMapTransform>(this, StructureMapTransform.POINTER);
        if ("evaluate".equals(codeString))
          return new Enumeration<StructureMapTransform>(this, StructureMapTransform.EVALUATE);
        throw new FHIRException("Unknown StructureMapTransform code '"+codeString+"'");
        }
    public String toCode(StructureMapTransform code) {
      if (code == StructureMapTransform.CREATE)
        return "create";
      if (code == StructureMapTransform.COPY)
        return "copy";
      if (code == StructureMapTransform.TRUNCATE)
        return "truncate";
      if (code == StructureMapTransform.ESCAPE)
        return "escape";
      if (code == StructureMapTransform.CAST)
        return "cast";
      if (code == StructureMapTransform.APPEND)
        return "append";
      if (code == StructureMapTransform.TRANSLATE)
        return "translate";
      if (code == StructureMapTransform.REFERENCE)
        return "reference";
      if (code == StructureMapTransform.DATEOP)
        return "dateOp";
      if (code == StructureMapTransform.UUID)
        return "uuid";
      if (code == StructureMapTransform.POINTER)
        return "pointer";
      if (code == StructureMapTransform.EVALUATE)
        return "evaluate";
      return "?";
      }
    public String toSystem(StructureMapTransform code) {
      return code.getSystem();
      }
    }

    @Block()
    public static class StructureMapContactComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The name of an individual to contact regarding the structure map.
         */
        @Child(name = "name", type = {StringType.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Name of an individual to contact", formalDefinition="The name of an individual to contact regarding the structure map." )
        protected StringType name;

        /**
         * Contact details for individual (if a name was provided) or the publisher.
         */
        @Child(name = "telecom", type = {ContactPoint.class}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Contact details for individual or publisher", formalDefinition="Contact details for individual (if a name was provided) or the publisher." )
        protected List<ContactPoint> telecom;

        private static final long serialVersionUID = -1179697803L;

    /**
     * Constructor
     */
      public StructureMapContactComponent() {
        super();
      }

        /**
         * @return {@link #name} (The name of an individual to contact regarding the structure map.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public StringType getNameElement() { 
          if (this.name == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create StructureMapContactComponent.name");
            else if (Configuration.doAutoCreate())
              this.name = new StringType(); // bb
          return this.name;
        }

        public boolean hasNameElement() { 
          return this.name != null && !this.name.isEmpty();
        }

        public boolean hasName() { 
          return this.name != null && !this.name.isEmpty();
        }

        /**
         * @param value {@link #name} (The name of an individual to contact regarding the structure map.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public StructureMapContactComponent setNameElement(StringType value) { 
          this.name = value;
          return this;
        }

        /**
         * @return The name of an individual to contact regarding the structure map.
         */
        public String getName() { 
          return this.name == null ? null : this.name.getValue();
        }

        /**
         * @param value The name of an individual to contact regarding the structure map.
         */
        public StructureMapContactComponent setName(String value) { 
          if (Utilities.noString(value))
            this.name = null;
          else {
            if (this.name == null)
              this.name = new StringType();
            this.name.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #telecom} (Contact details for individual (if a name was provided) or the publisher.)
         */
        public List<ContactPoint> getTelecom() { 
          if (this.telecom == null)
            this.telecom = new ArrayList<ContactPoint>();
          return this.telecom;
        }

        public boolean hasTelecom() { 
          if (this.telecom == null)
            return false;
          for (ContactPoint item : this.telecom)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #telecom} (Contact details for individual (if a name was provided) or the publisher.)
         */
    // syntactic sugar
        public ContactPoint addTelecom() { //3
          ContactPoint t = new ContactPoint();
          if (this.telecom == null)
            this.telecom = new ArrayList<ContactPoint>();
          this.telecom.add(t);
          return t;
        }

    // syntactic sugar
        public StructureMapContactComponent addTelecom(ContactPoint t) { //3
          if (t == null)
            return this;
          if (this.telecom == null)
            this.telecom = new ArrayList<ContactPoint>();
          this.telecom.add(t);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("name", "string", "The name of an individual to contact regarding the structure map.", 0, java.lang.Integer.MAX_VALUE, name));
          childrenList.add(new Property("telecom", "ContactPoint", "Contact details for individual (if a name was provided) or the publisher.", 0, java.lang.Integer.MAX_VALUE, telecom));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3373707: /*name*/ return this.name == null ? new Base[0] : new Base[] {this.name}; // StringType
        case -1429363305: /*telecom*/ return this.telecom == null ? new Base[0] : this.telecom.toArray(new Base[this.telecom.size()]); // ContactPoint
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3373707: // name
          this.name = castToString(value); // StringType
          break;
        case -1429363305: // telecom
          this.getTelecom().add(castToContactPoint(value)); // ContactPoint
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("name"))
          this.name = castToString(value); // StringType
        else if (name.equals("telecom"))
          this.getTelecom().add(castToContactPoint(value));
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3373707: throw new FHIRException("Cannot make property name as it is not a complex type"); // StringType
        case -1429363305:  return addTelecom(); // ContactPoint
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("name")) {
          throw new FHIRException("Cannot call addChild on a primitive type StructureMap.name");
        }
        else if (name.equals("telecom")) {
          return addTelecom();
        }
        else
          return super.addChild(name);
      }

      public StructureMapContactComponent copy() {
        StructureMapContactComponent dst = new StructureMapContactComponent();
        copyValues(dst);
        dst.name = name == null ? null : name.copy();
        if (telecom != null) {
          dst.telecom = new ArrayList<ContactPoint>();
          for (ContactPoint i : telecom)
            dst.telecom.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof StructureMapContactComponent))
          return false;
        StructureMapContactComponent o = (StructureMapContactComponent) other;
        return compareDeep(name, o.name, true) && compareDeep(telecom, o.telecom, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof StructureMapContactComponent))
          return false;
        StructureMapContactComponent o = (StructureMapContactComponent) other;
        return compareValues(name, o.name, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (name == null || name.isEmpty()) && (telecom == null || telecom.isEmpty())
          ;
      }

  public String fhirType() {
    return "StructureMap.contact";

  }

  }

    @Block()
    public static class StructureMapStructureComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The canonical URL that identifies the structure.
         */
        @Child(name = "url", type = {UriType.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Canonical URL for structure definition", formalDefinition="The canonical URL that identifies the structure." )
        protected UriType url;

        /**
         * How the referenced structure is used in this mapping.
         */
        @Child(name = "mode", type = {CodeType.class}, order=2, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="source | queried | target | produced", formalDefinition="How the referenced structure is used in this mapping." )
        protected Enumeration<StructureMapModelMode> mode;

        /**
         * Documentation that describes how the structure is used in the mapping.
         */
        @Child(name = "documentation", type = {StringType.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Documentation on use of structure", formalDefinition="Documentation that describes how the structure is used in the mapping." )
        protected StringType documentation;

        private static final long serialVersionUID = -451631915L;

    /**
     * Constructor
     */
      public StructureMapStructureComponent() {
        super();
      }

    /**
     * Constructor
     */
      public StructureMapStructureComponent(UriType url, Enumeration<StructureMapModelMode> mode) {
        super();
        this.url = url;
        this.mode = mode;
      }

        /**
         * @return {@link #url} (The canonical URL that identifies the structure.). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
         */
        public UriType getUrlElement() { 
          if (this.url == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create StructureMapStructureComponent.url");
            else if (Configuration.doAutoCreate())
              this.url = new UriType(); // bb
          return this.url;
        }

        public boolean hasUrlElement() { 
          return this.url != null && !this.url.isEmpty();
        }

        public boolean hasUrl() { 
          return this.url != null && !this.url.isEmpty();
        }

        /**
         * @param value {@link #url} (The canonical URL that identifies the structure.). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
         */
        public StructureMapStructureComponent setUrlElement(UriType value) { 
          this.url = value;
          return this;
        }

        /**
         * @return The canonical URL that identifies the structure.
         */
        public String getUrl() { 
          return this.url == null ? null : this.url.getValue();
        }

        /**
         * @param value The canonical URL that identifies the structure.
         */
        public StructureMapStructureComponent setUrl(String value) { 
            if (this.url == null)
              this.url = new UriType();
            this.url.setValue(value);
          return this;
        }

        /**
         * @return {@link #mode} (How the referenced structure is used in this mapping.). This is the underlying object with id, value and extensions. The accessor "getMode" gives direct access to the value
         */
        public Enumeration<StructureMapModelMode> getModeElement() { 
          if (this.mode == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create StructureMapStructureComponent.mode");
            else if (Configuration.doAutoCreate())
              this.mode = new Enumeration<StructureMapModelMode>(new StructureMapModelModeEnumFactory()); // bb
          return this.mode;
        }

        public boolean hasModeElement() { 
          return this.mode != null && !this.mode.isEmpty();
        }

        public boolean hasMode() { 
          return this.mode != null && !this.mode.isEmpty();
        }

        /**
         * @param value {@link #mode} (How the referenced structure is used in this mapping.). This is the underlying object with id, value and extensions. The accessor "getMode" gives direct access to the value
         */
        public StructureMapStructureComponent setModeElement(Enumeration<StructureMapModelMode> value) { 
          this.mode = value;
          return this;
        }

        /**
         * @return How the referenced structure is used in this mapping.
         */
        public StructureMapModelMode getMode() { 
          return this.mode == null ? null : this.mode.getValue();
        }

        /**
         * @param value How the referenced structure is used in this mapping.
         */
        public StructureMapStructureComponent setMode(StructureMapModelMode value) { 
            if (this.mode == null)
              this.mode = new Enumeration<StructureMapModelMode>(new StructureMapModelModeEnumFactory());
            this.mode.setValue(value);
          return this;
        }

        /**
         * @return {@link #documentation} (Documentation that describes how the structure is used in the mapping.). This is the underlying object with id, value and extensions. The accessor "getDocumentation" gives direct access to the value
         */
        public StringType getDocumentationElement() { 
          if (this.documentation == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create StructureMapStructureComponent.documentation");
            else if (Configuration.doAutoCreate())
              this.documentation = new StringType(); // bb
          return this.documentation;
        }

        public boolean hasDocumentationElement() { 
          return this.documentation != null && !this.documentation.isEmpty();
        }

        public boolean hasDocumentation() { 
          return this.documentation != null && !this.documentation.isEmpty();
        }

        /**
         * @param value {@link #documentation} (Documentation that describes how the structure is used in the mapping.). This is the underlying object with id, value and extensions. The accessor "getDocumentation" gives direct access to the value
         */
        public StructureMapStructureComponent setDocumentationElement(StringType value) { 
          this.documentation = value;
          return this;
        }

        /**
         * @return Documentation that describes how the structure is used in the mapping.
         */
        public String getDocumentation() { 
          return this.documentation == null ? null : this.documentation.getValue();
        }

        /**
         * @param value Documentation that describes how the structure is used in the mapping.
         */
        public StructureMapStructureComponent setDocumentation(String value) { 
          if (Utilities.noString(value))
            this.documentation = null;
          else {
            if (this.documentation == null)
              this.documentation = new StringType();
            this.documentation.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("url", "uri", "The canonical URL that identifies the structure.", 0, java.lang.Integer.MAX_VALUE, url));
          childrenList.add(new Property("mode", "code", "How the referenced structure is used in this mapping.", 0, java.lang.Integer.MAX_VALUE, mode));
          childrenList.add(new Property("documentation", "string", "Documentation that describes how the structure is used in the mapping.", 0, java.lang.Integer.MAX_VALUE, documentation));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 116079: /*url*/ return this.url == null ? new Base[0] : new Base[] {this.url}; // UriType
        case 3357091: /*mode*/ return this.mode == null ? new Base[0] : new Base[] {this.mode}; // Enumeration<StructureMapModelMode>
        case 1587405498: /*documentation*/ return this.documentation == null ? new Base[0] : new Base[] {this.documentation}; // StringType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 116079: // url
          this.url = castToUri(value); // UriType
          break;
        case 3357091: // mode
          this.mode = new StructureMapModelModeEnumFactory().fromType(value); // Enumeration<StructureMapModelMode>
          break;
        case 1587405498: // documentation
          this.documentation = castToString(value); // StringType
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("url"))
          this.url = castToUri(value); // UriType
        else if (name.equals("mode"))
          this.mode = new StructureMapModelModeEnumFactory().fromType(value); // Enumeration<StructureMapModelMode>
        else if (name.equals("documentation"))
          this.documentation = castToString(value); // StringType
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 116079: throw new FHIRException("Cannot make property url as it is not a complex type"); // UriType
        case 3357091: throw new FHIRException("Cannot make property mode as it is not a complex type"); // Enumeration<StructureMapModelMode>
        case 1587405498: throw new FHIRException("Cannot make property documentation as it is not a complex type"); // StringType
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("url")) {
          throw new FHIRException("Cannot call addChild on a primitive type StructureMap.url");
        }
        else if (name.equals("mode")) {
          throw new FHIRException("Cannot call addChild on a primitive type StructureMap.mode");
        }
        else if (name.equals("documentation")) {
          throw new FHIRException("Cannot call addChild on a primitive type StructureMap.documentation");
        }
        else
          return super.addChild(name);
      }

      public StructureMapStructureComponent copy() {
        StructureMapStructureComponent dst = new StructureMapStructureComponent();
        copyValues(dst);
        dst.url = url == null ? null : url.copy();
        dst.mode = mode == null ? null : mode.copy();
        dst.documentation = documentation == null ? null : documentation.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof StructureMapStructureComponent))
          return false;
        StructureMapStructureComponent o = (StructureMapStructureComponent) other;
        return compareDeep(url, o.url, true) && compareDeep(mode, o.mode, true) && compareDeep(documentation, o.documentation, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof StructureMapStructureComponent))
          return false;
        StructureMapStructureComponent o = (StructureMapStructureComponent) other;
        return compareValues(url, o.url, true) && compareValues(mode, o.mode, true) && compareValues(documentation, o.documentation, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (url == null || url.isEmpty()) && (mode == null || mode.isEmpty())
           && (documentation == null || documentation.isEmpty());
      }

  public String fhirType() {
    return "StructureMap.structure";

  }

  }

    @Block()
    public static class StructureMapGroupComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Descriptive name for a user.
         */
        @Child(name = "name", type = {IdType.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Descriptive name for a user", formalDefinition="Descriptive name for a user." )
        protected IdType name;

        /**
         * Another group that this group adds rules to.
         */
        @Child(name = "extends", type = {IdType.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Another group that this group adds rules to", formalDefinition="Another group that this group adds rules to." )
        protected IdType extends_;

        /**
         * Documentation for this group.
         */
        @Child(name = "documentation", type = {StringType.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Documentation for this group", formalDefinition="Documentation for this group." )
        protected StringType documentation;

        /**
         * A name assigned to an instance of data. The instance must be provided when the mapping is invoked.
         */
        @Child(name = "input", type = {}, order=4, min=1, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Named instance provided when invoking the map", formalDefinition="A name assigned to an instance of data. The instance must be provided when the mapping is invoked." )
        protected List<StructureMapGroupInputComponent> input;

        /**
         * Transform Rule from source to target.
         */
        @Child(name = "rule", type = {}, order=5, min=1, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Transform Rule from source to target", formalDefinition="Transform Rule from source to target." )
        protected List<StructureMapGroupRuleComponent> rule;

        private static final long serialVersionUID = -1311232924L;

    /**
     * Constructor
     */
      public StructureMapGroupComponent() {
        super();
      }

    /**
     * Constructor
     */
      public StructureMapGroupComponent(IdType name) {
        super();
        this.name = name;
      }

        /**
         * @return {@link #name} (Descriptive name for a user.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public IdType getNameElement() { 
          if (this.name == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create StructureMapGroupComponent.name");
            else if (Configuration.doAutoCreate())
              this.name = new IdType(); // bb
          return this.name;
        }

        public boolean hasNameElement() { 
          return this.name != null && !this.name.isEmpty();
        }

        public boolean hasName() { 
          return this.name != null && !this.name.isEmpty();
        }

        /**
         * @param value {@link #name} (Descriptive name for a user.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public StructureMapGroupComponent setNameElement(IdType value) { 
          this.name = value;
          return this;
        }

        /**
         * @return Descriptive name for a user.
         */
        public String getName() { 
          return this.name == null ? null : this.name.getValue();
        }

        /**
         * @param value Descriptive name for a user.
         */
        public StructureMapGroupComponent setName(String value) { 
            if (this.name == null)
              this.name = new IdType();
            this.name.setValue(value);
          return this;
        }

        /**
         * @return {@link #extends_} (Another group that this group adds rules to.). This is the underlying object with id, value and extensions. The accessor "getExtends" gives direct access to the value
         */
        public IdType getExtendsElement() { 
          if (this.extends_ == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create StructureMapGroupComponent.extends_");
            else if (Configuration.doAutoCreate())
              this.extends_ = new IdType(); // bb
          return this.extends_;
        }

        public boolean hasExtendsElement() { 
          return this.extends_ != null && !this.extends_.isEmpty();
        }

        public boolean hasExtends() { 
          return this.extends_ != null && !this.extends_.isEmpty();
        }

        /**
         * @param value {@link #extends_} (Another group that this group adds rules to.). This is the underlying object with id, value and extensions. The accessor "getExtends" gives direct access to the value
         */
        public StructureMapGroupComponent setExtendsElement(IdType value) { 
          this.extends_ = value;
          return this;
        }

        /**
         * @return Another group that this group adds rules to.
         */
        public String getExtends() { 
          return this.extends_ == null ? null : this.extends_.getValue();
        }

        /**
         * @param value Another group that this group adds rules to.
         */
        public StructureMapGroupComponent setExtends(String value) { 
          if (Utilities.noString(value))
            this.extends_ = null;
          else {
            if (this.extends_ == null)
              this.extends_ = new IdType();
            this.extends_.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #documentation} (Documentation for this group.). This is the underlying object with id, value and extensions. The accessor "getDocumentation" gives direct access to the value
         */
        public StringType getDocumentationElement() { 
          if (this.documentation == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create StructureMapGroupComponent.documentation");
            else if (Configuration.doAutoCreate())
              this.documentation = new StringType(); // bb
          return this.documentation;
        }

        public boolean hasDocumentationElement() { 
          return this.documentation != null && !this.documentation.isEmpty();
        }

        public boolean hasDocumentation() { 
          return this.documentation != null && !this.documentation.isEmpty();
        }

        /**
         * @param value {@link #documentation} (Documentation for this group.). This is the underlying object with id, value and extensions. The accessor "getDocumentation" gives direct access to the value
         */
        public StructureMapGroupComponent setDocumentationElement(StringType value) { 
          this.documentation = value;
          return this;
        }

        /**
         * @return Documentation for this group.
         */
        public String getDocumentation() { 
          return this.documentation == null ? null : this.documentation.getValue();
        }

        /**
         * @param value Documentation for this group.
         */
        public StructureMapGroupComponent setDocumentation(String value) { 
          if (Utilities.noString(value))
            this.documentation = null;
          else {
            if (this.documentation == null)
              this.documentation = new StringType();
            this.documentation.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #input} (A name assigned to an instance of data. The instance must be provided when the mapping is invoked.)
         */
        public List<StructureMapGroupInputComponent> getInput() { 
          if (this.input == null)
            this.input = new ArrayList<StructureMapGroupInputComponent>();
          return this.input;
        }

        public boolean hasInput() { 
          if (this.input == null)
            return false;
          for (StructureMapGroupInputComponent item : this.input)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #input} (A name assigned to an instance of data. The instance must be provided when the mapping is invoked.)
         */
    // syntactic sugar
        public StructureMapGroupInputComponent addInput() { //3
          StructureMapGroupInputComponent t = new StructureMapGroupInputComponent();
          if (this.input == null)
            this.input = new ArrayList<StructureMapGroupInputComponent>();
          this.input.add(t);
          return t;
        }

    // syntactic sugar
        public StructureMapGroupComponent addInput(StructureMapGroupInputComponent t) { //3
          if (t == null)
            return this;
          if (this.input == null)
            this.input = new ArrayList<StructureMapGroupInputComponent>();
          this.input.add(t);
          return this;
        }

        /**
         * @return {@link #rule} (Transform Rule from source to target.)
         */
        public List<StructureMapGroupRuleComponent> getRule() { 
          if (this.rule == null)
            this.rule = new ArrayList<StructureMapGroupRuleComponent>();
          return this.rule;
        }

        public boolean hasRule() { 
          if (this.rule == null)
            return false;
          for (StructureMapGroupRuleComponent item : this.rule)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #rule} (Transform Rule from source to target.)
         */
    // syntactic sugar
        public StructureMapGroupRuleComponent addRule() { //3
          StructureMapGroupRuleComponent t = new StructureMapGroupRuleComponent();
          if (this.rule == null)
            this.rule = new ArrayList<StructureMapGroupRuleComponent>();
          this.rule.add(t);
          return t;
        }

    // syntactic sugar
        public StructureMapGroupComponent addRule(StructureMapGroupRuleComponent t) { //3
          if (t == null)
            return this;
          if (this.rule == null)
            this.rule = new ArrayList<StructureMapGroupRuleComponent>();
          this.rule.add(t);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("name", "id", "Descriptive name for a user.", 0, java.lang.Integer.MAX_VALUE, name));
          childrenList.add(new Property("extends", "id", "Another group that this group adds rules to.", 0, java.lang.Integer.MAX_VALUE, extends_));
          childrenList.add(new Property("documentation", "string", "Documentation for this group.", 0, java.lang.Integer.MAX_VALUE, documentation));
          childrenList.add(new Property("input", "", "A name assigned to an instance of data. The instance must be provided when the mapping is invoked.", 0, java.lang.Integer.MAX_VALUE, input));
          childrenList.add(new Property("rule", "", "Transform Rule from source to target.", 0, java.lang.Integer.MAX_VALUE, rule));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3373707: /*name*/ return this.name == null ? new Base[0] : new Base[] {this.name}; // IdType
        case -1305664359: /*extends*/ return this.extends_ == null ? new Base[0] : new Base[] {this.extends_}; // IdType
        case 1587405498: /*documentation*/ return this.documentation == null ? new Base[0] : new Base[] {this.documentation}; // StringType
        case 100358090: /*input*/ return this.input == null ? new Base[0] : this.input.toArray(new Base[this.input.size()]); // StructureMapGroupInputComponent
        case 3512060: /*rule*/ return this.rule == null ? new Base[0] : this.rule.toArray(new Base[this.rule.size()]); // StructureMapGroupRuleComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3373707: // name
          this.name = castToId(value); // IdType
          break;
        case -1305664359: // extends
          this.extends_ = castToId(value); // IdType
          break;
        case 1587405498: // documentation
          this.documentation = castToString(value); // StringType
          break;
        case 100358090: // input
          this.getInput().add((StructureMapGroupInputComponent) value); // StructureMapGroupInputComponent
          break;
        case 3512060: // rule
          this.getRule().add((StructureMapGroupRuleComponent) value); // StructureMapGroupRuleComponent
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("name"))
          this.name = castToId(value); // IdType
        else if (name.equals("extends"))
          this.extends_ = castToId(value); // IdType
        else if (name.equals("documentation"))
          this.documentation = castToString(value); // StringType
        else if (name.equals("input"))
          this.getInput().add((StructureMapGroupInputComponent) value);
        else if (name.equals("rule"))
          this.getRule().add((StructureMapGroupRuleComponent) value);
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3373707: throw new FHIRException("Cannot make property name as it is not a complex type"); // IdType
        case -1305664359: throw new FHIRException("Cannot make property extends as it is not a complex type"); // IdType
        case 1587405498: throw new FHIRException("Cannot make property documentation as it is not a complex type"); // StringType
        case 100358090:  return addInput(); // StructureMapGroupInputComponent
        case 3512060:  return addRule(); // StructureMapGroupRuleComponent
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("name")) {
          throw new FHIRException("Cannot call addChild on a primitive type StructureMap.name");
        }
        else if (name.equals("extends")) {
          throw new FHIRException("Cannot call addChild on a primitive type StructureMap.extends");
        }
        else if (name.equals("documentation")) {
          throw new FHIRException("Cannot call addChild on a primitive type StructureMap.documentation");
        }
        else if (name.equals("input")) {
          return addInput();
        }
        else if (name.equals("rule")) {
          return addRule();
        }
        else
          return super.addChild(name);
      }

      public StructureMapGroupComponent copy() {
        StructureMapGroupComponent dst = new StructureMapGroupComponent();
        copyValues(dst);
        dst.name = name == null ? null : name.copy();
        dst.extends_ = extends_ == null ? null : extends_.copy();
        dst.documentation = documentation == null ? null : documentation.copy();
        if (input != null) {
          dst.input = new ArrayList<StructureMapGroupInputComponent>();
          for (StructureMapGroupInputComponent i : input)
            dst.input.add(i.copy());
        };
        if (rule != null) {
          dst.rule = new ArrayList<StructureMapGroupRuleComponent>();
          for (StructureMapGroupRuleComponent i : rule)
            dst.rule.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof StructureMapGroupComponent))
          return false;
        StructureMapGroupComponent o = (StructureMapGroupComponent) other;
        return compareDeep(name, o.name, true) && compareDeep(extends_, o.extends_, true) && compareDeep(documentation, o.documentation, true)
           && compareDeep(input, o.input, true) && compareDeep(rule, o.rule, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof StructureMapGroupComponent))
          return false;
        StructureMapGroupComponent o = (StructureMapGroupComponent) other;
        return compareValues(name, o.name, true) && compareValues(extends_, o.extends_, true) && compareValues(documentation, o.documentation, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (name == null || name.isEmpty()) && (extends_ == null || extends_.isEmpty())
           && (documentation == null || documentation.isEmpty()) && (input == null || input.isEmpty())
           && (rule == null || rule.isEmpty());
      }

  public String fhirType() {
    return "StructureMap.group";

  }

  }

    @Block()
    public static class StructureMapGroupInputComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Name for this instance of data.
         */
        @Child(name = "name", type = {IdType.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Name for this instance of data", formalDefinition="Name for this instance of data." )
        protected IdType name;

        /**
         * Type for this instance of data.
         */
        @Child(name = "type", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Type for this instance of data", formalDefinition="Type for this instance of data." )
        protected StringType type;

        /**
         * Mode for this instance of data.
         */
        @Child(name = "mode", type = {CodeType.class}, order=3, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="source | target", formalDefinition="Mode for this instance of data." )
        protected Enumeration<StructureMapInputMode> mode;

        /**
         * Documentation for this instance of data.
         */
        @Child(name = "documentation", type = {StringType.class}, order=4, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Documentation for this instance of data", formalDefinition="Documentation for this instance of data." )
        protected StringType documentation;

        private static final long serialVersionUID = -25050724L;

    /**
     * Constructor
     */
      public StructureMapGroupInputComponent() {
        super();
      }

    /**
     * Constructor
     */
      public StructureMapGroupInputComponent(IdType name, Enumeration<StructureMapInputMode> mode) {
        super();
        this.name = name;
        this.mode = mode;
      }

        /**
         * @return {@link #name} (Name for this instance of data.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public IdType getNameElement() { 
          if (this.name == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create StructureMapGroupInputComponent.name");
            else if (Configuration.doAutoCreate())
              this.name = new IdType(); // bb
          return this.name;
        }

        public boolean hasNameElement() { 
          return this.name != null && !this.name.isEmpty();
        }

        public boolean hasName() { 
          return this.name != null && !this.name.isEmpty();
        }

        /**
         * @param value {@link #name} (Name for this instance of data.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public StructureMapGroupInputComponent setNameElement(IdType value) { 
          this.name = value;
          return this;
        }

        /**
         * @return Name for this instance of data.
         */
        public String getName() { 
          return this.name == null ? null : this.name.getValue();
        }

        /**
         * @param value Name for this instance of data.
         */
        public StructureMapGroupInputComponent setName(String value) { 
            if (this.name == null)
              this.name = new IdType();
            this.name.setValue(value);
          return this;
        }

        /**
         * @return {@link #type} (Type for this instance of data.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public StringType getTypeElement() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create StructureMapGroupInputComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new StringType(); // bb
          return this.type;
        }

        public boolean hasTypeElement() { 
          return this.type != null && !this.type.isEmpty();
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (Type for this instance of data.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public StructureMapGroupInputComponent setTypeElement(StringType value) { 
          this.type = value;
          return this;
        }

        /**
         * @return Type for this instance of data.
         */
        public String getType() { 
          return this.type == null ? null : this.type.getValue();
        }

        /**
         * @param value Type for this instance of data.
         */
        public StructureMapGroupInputComponent setType(String value) { 
          if (Utilities.noString(value))
            this.type = null;
          else {
            if (this.type == null)
              this.type = new StringType();
            this.type.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #mode} (Mode for this instance of data.). This is the underlying object with id, value and extensions. The accessor "getMode" gives direct access to the value
         */
        public Enumeration<StructureMapInputMode> getModeElement() { 
          if (this.mode == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create StructureMapGroupInputComponent.mode");
            else if (Configuration.doAutoCreate())
              this.mode = new Enumeration<StructureMapInputMode>(new StructureMapInputModeEnumFactory()); // bb
          return this.mode;
        }

        public boolean hasModeElement() { 
          return this.mode != null && !this.mode.isEmpty();
        }

        public boolean hasMode() { 
          return this.mode != null && !this.mode.isEmpty();
        }

        /**
         * @param value {@link #mode} (Mode for this instance of data.). This is the underlying object with id, value and extensions. The accessor "getMode" gives direct access to the value
         */
        public StructureMapGroupInputComponent setModeElement(Enumeration<StructureMapInputMode> value) { 
          this.mode = value;
          return this;
        }

        /**
         * @return Mode for this instance of data.
         */
        public StructureMapInputMode getMode() { 
          return this.mode == null ? null : this.mode.getValue();
        }

        /**
         * @param value Mode for this instance of data.
         */
        public StructureMapGroupInputComponent setMode(StructureMapInputMode value) { 
            if (this.mode == null)
              this.mode = new Enumeration<StructureMapInputMode>(new StructureMapInputModeEnumFactory());
            this.mode.setValue(value);
          return this;
        }

        /**
         * @return {@link #documentation} (Documentation for this instance of data.). This is the underlying object with id, value and extensions. The accessor "getDocumentation" gives direct access to the value
         */
        public StringType getDocumentationElement() { 
          if (this.documentation == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create StructureMapGroupInputComponent.documentation");
            else if (Configuration.doAutoCreate())
              this.documentation = new StringType(); // bb
          return this.documentation;
        }

        public boolean hasDocumentationElement() { 
          return this.documentation != null && !this.documentation.isEmpty();
        }

        public boolean hasDocumentation() { 
          return this.documentation != null && !this.documentation.isEmpty();
        }

        /**
         * @param value {@link #documentation} (Documentation for this instance of data.). This is the underlying object with id, value and extensions. The accessor "getDocumentation" gives direct access to the value
         */
        public StructureMapGroupInputComponent setDocumentationElement(StringType value) { 
          this.documentation = value;
          return this;
        }

        /**
         * @return Documentation for this instance of data.
         */
        public String getDocumentation() { 
          return this.documentation == null ? null : this.documentation.getValue();
        }

        /**
         * @param value Documentation for this instance of data.
         */
        public StructureMapGroupInputComponent setDocumentation(String value) { 
          if (Utilities.noString(value))
            this.documentation = null;
          else {
            if (this.documentation == null)
              this.documentation = new StringType();
            this.documentation.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("name", "id", "Name for this instance of data.", 0, java.lang.Integer.MAX_VALUE, name));
          childrenList.add(new Property("type", "string", "Type for this instance of data.", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("mode", "code", "Mode for this instance of data.", 0, java.lang.Integer.MAX_VALUE, mode));
          childrenList.add(new Property("documentation", "string", "Documentation for this instance of data.", 0, java.lang.Integer.MAX_VALUE, documentation));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3373707: /*name*/ return this.name == null ? new Base[0] : new Base[] {this.name}; // IdType
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // StringType
        case 3357091: /*mode*/ return this.mode == null ? new Base[0] : new Base[] {this.mode}; // Enumeration<StructureMapInputMode>
        case 1587405498: /*documentation*/ return this.documentation == null ? new Base[0] : new Base[] {this.documentation}; // StringType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3373707: // name
          this.name = castToId(value); // IdType
          break;
        case 3575610: // type
          this.type = castToString(value); // StringType
          break;
        case 3357091: // mode
          this.mode = new StructureMapInputModeEnumFactory().fromType(value); // Enumeration<StructureMapInputMode>
          break;
        case 1587405498: // documentation
          this.documentation = castToString(value); // StringType
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("name"))
          this.name = castToId(value); // IdType
        else if (name.equals("type"))
          this.type = castToString(value); // StringType
        else if (name.equals("mode"))
          this.mode = new StructureMapInputModeEnumFactory().fromType(value); // Enumeration<StructureMapInputMode>
        else if (name.equals("documentation"))
          this.documentation = castToString(value); // StringType
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3373707: throw new FHIRException("Cannot make property name as it is not a complex type"); // IdType
        case 3575610: throw new FHIRException("Cannot make property type as it is not a complex type"); // StringType
        case 3357091: throw new FHIRException("Cannot make property mode as it is not a complex type"); // Enumeration<StructureMapInputMode>
        case 1587405498: throw new FHIRException("Cannot make property documentation as it is not a complex type"); // StringType
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("name")) {
          throw new FHIRException("Cannot call addChild on a primitive type StructureMap.name");
        }
        else if (name.equals("type")) {
          throw new FHIRException("Cannot call addChild on a primitive type StructureMap.type");
        }
        else if (name.equals("mode")) {
          throw new FHIRException("Cannot call addChild on a primitive type StructureMap.mode");
        }
        else if (name.equals("documentation")) {
          throw new FHIRException("Cannot call addChild on a primitive type StructureMap.documentation");
        }
        else
          return super.addChild(name);
      }

      public StructureMapGroupInputComponent copy() {
        StructureMapGroupInputComponent dst = new StructureMapGroupInputComponent();
        copyValues(dst);
        dst.name = name == null ? null : name.copy();
        dst.type = type == null ? null : type.copy();
        dst.mode = mode == null ? null : mode.copy();
        dst.documentation = documentation == null ? null : documentation.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof StructureMapGroupInputComponent))
          return false;
        StructureMapGroupInputComponent o = (StructureMapGroupInputComponent) other;
        return compareDeep(name, o.name, true) && compareDeep(type, o.type, true) && compareDeep(mode, o.mode, true)
           && compareDeep(documentation, o.documentation, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof StructureMapGroupInputComponent))
          return false;
        StructureMapGroupInputComponent o = (StructureMapGroupInputComponent) other;
        return compareValues(name, o.name, true) && compareValues(type, o.type, true) && compareValues(mode, o.mode, true)
           && compareValues(documentation, o.documentation, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (name == null || name.isEmpty()) && (type == null || type.isEmpty())
           && (mode == null || mode.isEmpty()) && (documentation == null || documentation.isEmpty())
          ;
      }

  public String fhirType() {
    return "StructureMap.group.input";

  }

  }

    @Block()
    public static class StructureMapGroupRuleComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Name of the rule for internal references.
         */
        @Child(name = "name", type = {IdType.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Name of the rule for internal references", formalDefinition="Name of the rule for internal references." )
        protected IdType name;

        /**
         * Source inputs to the mapping.
         */
        @Child(name = "source", type = {}, order=2, min=1, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Source inputs to the mapping", formalDefinition="Source inputs to the mapping." )
        protected List<StructureMapGroupRuleSourceComponent> source;

        /**
         * Content to create because of this mapping rule.
         */
        @Child(name = "target", type = {}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Content to create because of this mapping rule", formalDefinition="Content to create because of this mapping rule." )
        protected List<StructureMapGroupRuleTargetComponent> target;

        /**
         * Rules contained in this rule.
         */
        @Child(name = "rule", type = {StructureMapGroupRuleComponent.class}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Rules contained in this rule", formalDefinition="Rules contained in this rule." )
        protected List<StructureMapGroupRuleComponent> rule;

        /**
         * Which other rules to apply in the context of this rule.
         */
        @Child(name = "dependent", type = {}, order=5, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Which other rules to apply in the context of this rule", formalDefinition="Which other rules to apply in the context of this rule." )
        protected List<StructureMapGroupRuleDependentComponent> dependent;

        /**
         * Documentation for this instance of data.
         */
        @Child(name = "documentation", type = {StringType.class}, order=6, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Documentation for this instance of data", formalDefinition="Documentation for this instance of data." )
        protected StringType documentation;

        private static final long serialVersionUID = 773925517L;

    /**
     * Constructor
     */
      public StructureMapGroupRuleComponent() {
        super();
      }

    /**
     * Constructor
     */
      public StructureMapGroupRuleComponent(IdType name) {
        super();
        this.name = name;
      }

        /**
         * @return {@link #name} (Name of the rule for internal references.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public IdType getNameElement() { 
          if (this.name == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create StructureMapGroupRuleComponent.name");
            else if (Configuration.doAutoCreate())
              this.name = new IdType(); // bb
          return this.name;
        }

        public boolean hasNameElement() { 
          return this.name != null && !this.name.isEmpty();
        }

        public boolean hasName() { 
          return this.name != null && !this.name.isEmpty();
        }

        /**
         * @param value {@link #name} (Name of the rule for internal references.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public StructureMapGroupRuleComponent setNameElement(IdType value) { 
          this.name = value;
          return this;
        }

        /**
         * @return Name of the rule for internal references.
         */
        public String getName() { 
          return this.name == null ? null : this.name.getValue();
        }

        /**
         * @param value Name of the rule for internal references.
         */
        public StructureMapGroupRuleComponent setName(String value) { 
            if (this.name == null)
              this.name = new IdType();
            this.name.setValue(value);
          return this;
        }

        /**
         * @return {@link #source} (Source inputs to the mapping.)
         */
        public List<StructureMapGroupRuleSourceComponent> getSource() { 
          if (this.source == null)
            this.source = new ArrayList<StructureMapGroupRuleSourceComponent>();
          return this.source;
        }

        public boolean hasSource() { 
          if (this.source == null)
            return false;
          for (StructureMapGroupRuleSourceComponent item : this.source)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #source} (Source inputs to the mapping.)
         */
    // syntactic sugar
        public StructureMapGroupRuleSourceComponent addSource() { //3
          StructureMapGroupRuleSourceComponent t = new StructureMapGroupRuleSourceComponent();
          if (this.source == null)
            this.source = new ArrayList<StructureMapGroupRuleSourceComponent>();
          this.source.add(t);
          return t;
        }

    // syntactic sugar
        public StructureMapGroupRuleComponent addSource(StructureMapGroupRuleSourceComponent t) { //3
          if (t == null)
            return this;
          if (this.source == null)
            this.source = new ArrayList<StructureMapGroupRuleSourceComponent>();
          this.source.add(t);
          return this;
        }

        /**
         * @return {@link #target} (Content to create because of this mapping rule.)
         */
        public List<StructureMapGroupRuleTargetComponent> getTarget() { 
          if (this.target == null)
            this.target = new ArrayList<StructureMapGroupRuleTargetComponent>();
          return this.target;
        }

        public boolean hasTarget() { 
          if (this.target == null)
            return false;
          for (StructureMapGroupRuleTargetComponent item : this.target)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #target} (Content to create because of this mapping rule.)
         */
    // syntactic sugar
        public StructureMapGroupRuleTargetComponent addTarget() { //3
          StructureMapGroupRuleTargetComponent t = new StructureMapGroupRuleTargetComponent();
          if (this.target == null)
            this.target = new ArrayList<StructureMapGroupRuleTargetComponent>();
          this.target.add(t);
          return t;
        }

    // syntactic sugar
        public StructureMapGroupRuleComponent addTarget(StructureMapGroupRuleTargetComponent t) { //3
          if (t == null)
            return this;
          if (this.target == null)
            this.target = new ArrayList<StructureMapGroupRuleTargetComponent>();
          this.target.add(t);
          return this;
        }

        /**
         * @return {@link #rule} (Rules contained in this rule.)
         */
        public List<StructureMapGroupRuleComponent> getRule() { 
          if (this.rule == null)
            this.rule = new ArrayList<StructureMapGroupRuleComponent>();
          return this.rule;
        }

        public boolean hasRule() { 
          if (this.rule == null)
            return false;
          for (StructureMapGroupRuleComponent item : this.rule)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #rule} (Rules contained in this rule.)
         */
    // syntactic sugar
        public StructureMapGroupRuleComponent addRule() { //3
          StructureMapGroupRuleComponent t = new StructureMapGroupRuleComponent();
          if (this.rule == null)
            this.rule = new ArrayList<StructureMapGroupRuleComponent>();
          this.rule.add(t);
          return t;
        }

    // syntactic sugar
        public StructureMapGroupRuleComponent addRule(StructureMapGroupRuleComponent t) { //3
          if (t == null)
            return this;
          if (this.rule == null)
            this.rule = new ArrayList<StructureMapGroupRuleComponent>();
          this.rule.add(t);
          return this;
        }

        /**
         * @return {@link #dependent} (Which other rules to apply in the context of this rule.)
         */
        public List<StructureMapGroupRuleDependentComponent> getDependent() { 
          if (this.dependent == null)
            this.dependent = new ArrayList<StructureMapGroupRuleDependentComponent>();
          return this.dependent;
        }

        public boolean hasDependent() { 
          if (this.dependent == null)
            return false;
          for (StructureMapGroupRuleDependentComponent item : this.dependent)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #dependent} (Which other rules to apply in the context of this rule.)
         */
    // syntactic sugar
        public StructureMapGroupRuleDependentComponent addDependent() { //3
          StructureMapGroupRuleDependentComponent t = new StructureMapGroupRuleDependentComponent();
          if (this.dependent == null)
            this.dependent = new ArrayList<StructureMapGroupRuleDependentComponent>();
          this.dependent.add(t);
          return t;
        }

    // syntactic sugar
        public StructureMapGroupRuleComponent addDependent(StructureMapGroupRuleDependentComponent t) { //3
          if (t == null)
            return this;
          if (this.dependent == null)
            this.dependent = new ArrayList<StructureMapGroupRuleDependentComponent>();
          this.dependent.add(t);
          return this;
        }

        /**
         * @return {@link #documentation} (Documentation for this instance of data.). This is the underlying object with id, value and extensions. The accessor "getDocumentation" gives direct access to the value
         */
        public StringType getDocumentationElement() { 
          if (this.documentation == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create StructureMapGroupRuleComponent.documentation");
            else if (Configuration.doAutoCreate())
              this.documentation = new StringType(); // bb
          return this.documentation;
        }

        public boolean hasDocumentationElement() { 
          return this.documentation != null && !this.documentation.isEmpty();
        }

        public boolean hasDocumentation() { 
          return this.documentation != null && !this.documentation.isEmpty();
        }

        /**
         * @param value {@link #documentation} (Documentation for this instance of data.). This is the underlying object with id, value and extensions. The accessor "getDocumentation" gives direct access to the value
         */
        public StructureMapGroupRuleComponent setDocumentationElement(StringType value) { 
          this.documentation = value;
          return this;
        }

        /**
         * @return Documentation for this instance of data.
         */
        public String getDocumentation() { 
          return this.documentation == null ? null : this.documentation.getValue();
        }

        /**
         * @param value Documentation for this instance of data.
         */
        public StructureMapGroupRuleComponent setDocumentation(String value) { 
          if (Utilities.noString(value))
            this.documentation = null;
          else {
            if (this.documentation == null)
              this.documentation = new StringType();
            this.documentation.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("name", "id", "Name of the rule for internal references.", 0, java.lang.Integer.MAX_VALUE, name));
          childrenList.add(new Property("source", "", "Source inputs to the mapping.", 0, java.lang.Integer.MAX_VALUE, source));
          childrenList.add(new Property("target", "", "Content to create because of this mapping rule.", 0, java.lang.Integer.MAX_VALUE, target));
          childrenList.add(new Property("rule", "@StructureMap.group.rule", "Rules contained in this rule.", 0, java.lang.Integer.MAX_VALUE, rule));
          childrenList.add(new Property("dependent", "", "Which other rules to apply in the context of this rule.", 0, java.lang.Integer.MAX_VALUE, dependent));
          childrenList.add(new Property("documentation", "string", "Documentation for this instance of data.", 0, java.lang.Integer.MAX_VALUE, documentation));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3373707: /*name*/ return this.name == null ? new Base[0] : new Base[] {this.name}; // IdType
        case -896505829: /*source*/ return this.source == null ? new Base[0] : this.source.toArray(new Base[this.source.size()]); // StructureMapGroupRuleSourceComponent
        case -880905839: /*target*/ return this.target == null ? new Base[0] : this.target.toArray(new Base[this.target.size()]); // StructureMapGroupRuleTargetComponent
        case 3512060: /*rule*/ return this.rule == null ? new Base[0] : this.rule.toArray(new Base[this.rule.size()]); // StructureMapGroupRuleComponent
        case -1109226753: /*dependent*/ return this.dependent == null ? new Base[0] : this.dependent.toArray(new Base[this.dependent.size()]); // StructureMapGroupRuleDependentComponent
        case 1587405498: /*documentation*/ return this.documentation == null ? new Base[0] : new Base[] {this.documentation}; // StringType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3373707: // name
          this.name = castToId(value); // IdType
          break;
        case -896505829: // source
          this.getSource().add((StructureMapGroupRuleSourceComponent) value); // StructureMapGroupRuleSourceComponent
          break;
        case -880905839: // target
          this.getTarget().add((StructureMapGroupRuleTargetComponent) value); // StructureMapGroupRuleTargetComponent
          break;
        case 3512060: // rule
          this.getRule().add((StructureMapGroupRuleComponent) value); // StructureMapGroupRuleComponent
          break;
        case -1109226753: // dependent
          this.getDependent().add((StructureMapGroupRuleDependentComponent) value); // StructureMapGroupRuleDependentComponent
          break;
        case 1587405498: // documentation
          this.documentation = castToString(value); // StringType
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("name"))
          this.name = castToId(value); // IdType
        else if (name.equals("source"))
          this.getSource().add((StructureMapGroupRuleSourceComponent) value);
        else if (name.equals("target"))
          this.getTarget().add((StructureMapGroupRuleTargetComponent) value);
        else if (name.equals("rule"))
          this.getRule().add((StructureMapGroupRuleComponent) value);
        else if (name.equals("dependent"))
          this.getDependent().add((StructureMapGroupRuleDependentComponent) value);
        else if (name.equals("documentation"))
          this.documentation = castToString(value); // StringType
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3373707: throw new FHIRException("Cannot make property name as it is not a complex type"); // IdType
        case -896505829:  return addSource(); // StructureMapGroupRuleSourceComponent
        case -880905839:  return addTarget(); // StructureMapGroupRuleTargetComponent
        case 3512060:  return addRule(); // StructureMapGroupRuleComponent
        case -1109226753:  return addDependent(); // StructureMapGroupRuleDependentComponent
        case 1587405498: throw new FHIRException("Cannot make property documentation as it is not a complex type"); // StringType
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("name")) {
          throw new FHIRException("Cannot call addChild on a primitive type StructureMap.name");
        }
        else if (name.equals("source")) {
          return addSource();
        }
        else if (name.equals("target")) {
          return addTarget();
        }
        else if (name.equals("rule")) {
          return addRule();
        }
        else if (name.equals("dependent")) {
          return addDependent();
        }
        else if (name.equals("documentation")) {
          throw new FHIRException("Cannot call addChild on a primitive type StructureMap.documentation");
        }
        else
          return super.addChild(name);
      }

      public StructureMapGroupRuleComponent copy() {
        StructureMapGroupRuleComponent dst = new StructureMapGroupRuleComponent();
        copyValues(dst);
        dst.name = name == null ? null : name.copy();
        if (source != null) {
          dst.source = new ArrayList<StructureMapGroupRuleSourceComponent>();
          for (StructureMapGroupRuleSourceComponent i : source)
            dst.source.add(i.copy());
        };
        if (target != null) {
          dst.target = new ArrayList<StructureMapGroupRuleTargetComponent>();
          for (StructureMapGroupRuleTargetComponent i : target)
            dst.target.add(i.copy());
        };
        if (rule != null) {
          dst.rule = new ArrayList<StructureMapGroupRuleComponent>();
          for (StructureMapGroupRuleComponent i : rule)
            dst.rule.add(i.copy());
        };
        if (dependent != null) {
          dst.dependent = new ArrayList<StructureMapGroupRuleDependentComponent>();
          for (StructureMapGroupRuleDependentComponent i : dependent)
            dst.dependent.add(i.copy());
        };
        dst.documentation = documentation == null ? null : documentation.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof StructureMapGroupRuleComponent))
          return false;
        StructureMapGroupRuleComponent o = (StructureMapGroupRuleComponent) other;
        return compareDeep(name, o.name, true) && compareDeep(source, o.source, true) && compareDeep(target, o.target, true)
           && compareDeep(rule, o.rule, true) && compareDeep(dependent, o.dependent, true) && compareDeep(documentation, o.documentation, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof StructureMapGroupRuleComponent))
          return false;
        StructureMapGroupRuleComponent o = (StructureMapGroupRuleComponent) other;
        return compareValues(name, o.name, true) && compareValues(documentation, o.documentation, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (name == null || name.isEmpty()) && (source == null || source.isEmpty())
           && (target == null || target.isEmpty()) && (rule == null || rule.isEmpty()) && (dependent == null || dependent.isEmpty())
           && (documentation == null || documentation.isEmpty());
      }

  public String fhirType() {
    return "StructureMap.group.rule";

  }

  }

    @Block()
    public static class StructureMapGroupRuleSourceComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Whether this rule applies if the source isn't found.
         */
        @Child(name = "required", type = {BooleanType.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Whether this rule applies if the source isn't found", formalDefinition="Whether this rule applies if the source isn't found." )
        protected BooleanType required;

        /**
         * Type or variable this rule applies to.
         */
        @Child(name = "context", type = {IdType.class}, order=2, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Type or variable this rule applies to", formalDefinition="Type or variable this rule applies to." )
        protected IdType context;

        /**
         * How to interpret the context.
         */
        @Child(name = "contextType", type = {CodeType.class}, order=3, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="type | variable", formalDefinition="How to interpret the context." )
        protected Enumeration<StructureMapContextType> contextType;

        /**
         * Optional field for this source.
         */
        @Child(name = "element", type = {StringType.class}, order=4, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Optional field for this source", formalDefinition="Optional field for this source." )
        protected StringType element;

        /**
         * How to handle the list mode for this element.
         */
        @Child(name = "listMode", type = {CodeType.class}, order=5, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="first | share | last", formalDefinition="How to handle the list mode for this element." )
        protected Enumeration<StructureMapListMode> listMode;

        /**
         * Named context for field, if a field is specified.
         */
        @Child(name = "variable", type = {IdType.class}, order=6, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Named context for field, if a field is specified", formalDefinition="Named context for field, if a field is specified." )
        protected IdType variable;

        /**
         * FluentPath expression  - must be true or the rule does not apply.
         */
        @Child(name = "condition", type = {StringType.class}, order=7, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="FluentPath expression  - must be true or the rule does not apply", formalDefinition="FluentPath expression  - must be true or the rule does not apply." )
        protected StringType condition;

        /**
         * FluentPath expression  - must be true or the mapping engine throws an error instead of completing.
         */
        @Child(name = "check", type = {StringType.class}, order=8, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="FluentPath expression  - must be true or the mapping engine throws an error instead of completing", formalDefinition="FluentPath expression  - must be true or the mapping engine throws an error instead of completing." )
        protected StringType check;

        private static final long serialVersionUID = -1039728628L;

    /**
     * Constructor
     */
      public StructureMapGroupRuleSourceComponent() {
        super();
      }

    /**
     * Constructor
     */
      public StructureMapGroupRuleSourceComponent(BooleanType required, IdType context, Enumeration<StructureMapContextType> contextType) {
        super();
        this.required = required;
        this.context = context;
        this.contextType = contextType;
      }

        /**
         * @return {@link #required} (Whether this rule applies if the source isn't found.). This is the underlying object with id, value and extensions. The accessor "getRequired" gives direct access to the value
         */
        public BooleanType getRequiredElement() { 
          if (this.required == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create StructureMapGroupRuleSourceComponent.required");
            else if (Configuration.doAutoCreate())
              this.required = new BooleanType(); // bb
          return this.required;
        }

        public boolean hasRequiredElement() { 
          return this.required != null && !this.required.isEmpty();
        }

        public boolean hasRequired() { 
          return this.required != null && !this.required.isEmpty();
        }

        /**
         * @param value {@link #required} (Whether this rule applies if the source isn't found.). This is the underlying object with id, value and extensions. The accessor "getRequired" gives direct access to the value
         */
        public StructureMapGroupRuleSourceComponent setRequiredElement(BooleanType value) { 
          this.required = value;
          return this;
        }

        /**
         * @return Whether this rule applies if the source isn't found.
         */
        public boolean getRequired() { 
          return this.required == null || this.required.isEmpty() ? false : this.required.getValue();
        }

        /**
         * @param value Whether this rule applies if the source isn't found.
         */
        public StructureMapGroupRuleSourceComponent setRequired(boolean value) { 
            if (this.required == null)
              this.required = new BooleanType();
            this.required.setValue(value);
          return this;
        }

        /**
         * @return {@link #context} (Type or variable this rule applies to.). This is the underlying object with id, value and extensions. The accessor "getContext" gives direct access to the value
         */
        public IdType getContextElement() { 
          if (this.context == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create StructureMapGroupRuleSourceComponent.context");
            else if (Configuration.doAutoCreate())
              this.context = new IdType(); // bb
          return this.context;
        }

        public boolean hasContextElement() { 
          return this.context != null && !this.context.isEmpty();
        }

        public boolean hasContext() { 
          return this.context != null && !this.context.isEmpty();
        }

        /**
         * @param value {@link #context} (Type or variable this rule applies to.). This is the underlying object with id, value and extensions. The accessor "getContext" gives direct access to the value
         */
        public StructureMapGroupRuleSourceComponent setContextElement(IdType value) { 
          this.context = value;
          return this;
        }

        /**
         * @return Type or variable this rule applies to.
         */
        public String getContext() { 
          return this.context == null ? null : this.context.getValue();
        }

        /**
         * @param value Type or variable this rule applies to.
         */
        public StructureMapGroupRuleSourceComponent setContext(String value) { 
            if (this.context == null)
              this.context = new IdType();
            this.context.setValue(value);
          return this;
        }

        /**
         * @return {@link #contextType} (How to interpret the context.). This is the underlying object with id, value and extensions. The accessor "getContextType" gives direct access to the value
         */
        public Enumeration<StructureMapContextType> getContextTypeElement() { 
          if (this.contextType == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create StructureMapGroupRuleSourceComponent.contextType");
            else if (Configuration.doAutoCreate())
              this.contextType = new Enumeration<StructureMapContextType>(new StructureMapContextTypeEnumFactory()); // bb
          return this.contextType;
        }

        public boolean hasContextTypeElement() { 
          return this.contextType != null && !this.contextType.isEmpty();
        }

        public boolean hasContextType() { 
          return this.contextType != null && !this.contextType.isEmpty();
        }

        /**
         * @param value {@link #contextType} (How to interpret the context.). This is the underlying object with id, value and extensions. The accessor "getContextType" gives direct access to the value
         */
        public StructureMapGroupRuleSourceComponent setContextTypeElement(Enumeration<StructureMapContextType> value) { 
          this.contextType = value;
          return this;
        }

        /**
         * @return How to interpret the context.
         */
        public StructureMapContextType getContextType() { 
          return this.contextType == null ? null : this.contextType.getValue();
        }

        /**
         * @param value How to interpret the context.
         */
        public StructureMapGroupRuleSourceComponent setContextType(StructureMapContextType value) { 
            if (this.contextType == null)
              this.contextType = new Enumeration<StructureMapContextType>(new StructureMapContextTypeEnumFactory());
            this.contextType.setValue(value);
          return this;
        }

        /**
         * @return {@link #element} (Optional field for this source.). This is the underlying object with id, value and extensions. The accessor "getElement" gives direct access to the value
         */
        public StringType getElementElement() { 
          if (this.element == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create StructureMapGroupRuleSourceComponent.element");
            else if (Configuration.doAutoCreate())
              this.element = new StringType(); // bb
          return this.element;
        }

        public boolean hasElementElement() { 
          return this.element != null && !this.element.isEmpty();
        }

        public boolean hasElement() { 
          return this.element != null && !this.element.isEmpty();
        }

        /**
         * @param value {@link #element} (Optional field for this source.). This is the underlying object with id, value and extensions. The accessor "getElement" gives direct access to the value
         */
        public StructureMapGroupRuleSourceComponent setElementElement(StringType value) { 
          this.element = value;
          return this;
        }

        /**
         * @return Optional field for this source.
         */
        public String getElement() { 
          return this.element == null ? null : this.element.getValue();
        }

        /**
         * @param value Optional field for this source.
         */
        public StructureMapGroupRuleSourceComponent setElement(String value) { 
          if (Utilities.noString(value))
            this.element = null;
          else {
            if (this.element == null)
              this.element = new StringType();
            this.element.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #listMode} (How to handle the list mode for this element.). This is the underlying object with id, value and extensions. The accessor "getListMode" gives direct access to the value
         */
        public Enumeration<StructureMapListMode> getListModeElement() { 
          if (this.listMode == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create StructureMapGroupRuleSourceComponent.listMode");
            else if (Configuration.doAutoCreate())
              this.listMode = new Enumeration<StructureMapListMode>(new StructureMapListModeEnumFactory()); // bb
          return this.listMode;
        }

        public boolean hasListModeElement() { 
          return this.listMode != null && !this.listMode.isEmpty();
        }

        public boolean hasListMode() { 
          return this.listMode != null && !this.listMode.isEmpty();
        }

        /**
         * @param value {@link #listMode} (How to handle the list mode for this element.). This is the underlying object with id, value and extensions. The accessor "getListMode" gives direct access to the value
         */
        public StructureMapGroupRuleSourceComponent setListModeElement(Enumeration<StructureMapListMode> value) { 
          this.listMode = value;
          return this;
        }

        /**
         * @return How to handle the list mode for this element.
         */
        public StructureMapListMode getListMode() { 
          return this.listMode == null ? null : this.listMode.getValue();
        }

        /**
         * @param value How to handle the list mode for this element.
         */
        public StructureMapGroupRuleSourceComponent setListMode(StructureMapListMode value) { 
          if (value == null)
            this.listMode = null;
          else {
            if (this.listMode == null)
              this.listMode = new Enumeration<StructureMapListMode>(new StructureMapListModeEnumFactory());
            this.listMode.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #variable} (Named context for field, if a field is specified.). This is the underlying object with id, value and extensions. The accessor "getVariable" gives direct access to the value
         */
        public IdType getVariableElement() { 
          if (this.variable == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create StructureMapGroupRuleSourceComponent.variable");
            else if (Configuration.doAutoCreate())
              this.variable = new IdType(); // bb
          return this.variable;
        }

        public boolean hasVariableElement() { 
          return this.variable != null && !this.variable.isEmpty();
        }

        public boolean hasVariable() { 
          return this.variable != null && !this.variable.isEmpty();
        }

        /**
         * @param value {@link #variable} (Named context for field, if a field is specified.). This is the underlying object with id, value and extensions. The accessor "getVariable" gives direct access to the value
         */
        public StructureMapGroupRuleSourceComponent setVariableElement(IdType value) { 
          this.variable = value;
          return this;
        }

        /**
         * @return Named context for field, if a field is specified.
         */
        public String getVariable() { 
          return this.variable == null ? null : this.variable.getValue();
        }

        /**
         * @param value Named context for field, if a field is specified.
         */
        public StructureMapGroupRuleSourceComponent setVariable(String value) { 
          if (Utilities.noString(value))
            this.variable = null;
          else {
            if (this.variable == null)
              this.variable = new IdType();
            this.variable.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #condition} (FluentPath expression  - must be true or the rule does not apply.). This is the underlying object with id, value and extensions. The accessor "getCondition" gives direct access to the value
         */
        public StringType getConditionElement() { 
          if (this.condition == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create StructureMapGroupRuleSourceComponent.condition");
            else if (Configuration.doAutoCreate())
              this.condition = new StringType(); // bb
          return this.condition;
        }

        public boolean hasConditionElement() { 
          return this.condition != null && !this.condition.isEmpty();
        }

        public boolean hasCondition() { 
          return this.condition != null && !this.condition.isEmpty();
        }

        /**
         * @param value {@link #condition} (FluentPath expression  - must be true or the rule does not apply.). This is the underlying object with id, value and extensions. The accessor "getCondition" gives direct access to the value
         */
        public StructureMapGroupRuleSourceComponent setConditionElement(StringType value) { 
          this.condition = value;
          return this;
        }

        /**
         * @return FluentPath expression  - must be true or the rule does not apply.
         */
        public String getCondition() { 
          return this.condition == null ? null : this.condition.getValue();
        }

        /**
         * @param value FluentPath expression  - must be true or the rule does not apply.
         */
        public StructureMapGroupRuleSourceComponent setCondition(String value) { 
          if (Utilities.noString(value))
            this.condition = null;
          else {
            if (this.condition == null)
              this.condition = new StringType();
            this.condition.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #check} (FluentPath expression  - must be true or the mapping engine throws an error instead of completing.). This is the underlying object with id, value and extensions. The accessor "getCheck" gives direct access to the value
         */
        public StringType getCheckElement() { 
          if (this.check == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create StructureMapGroupRuleSourceComponent.check");
            else if (Configuration.doAutoCreate())
              this.check = new StringType(); // bb
          return this.check;
        }

        public boolean hasCheckElement() { 
          return this.check != null && !this.check.isEmpty();
        }

        public boolean hasCheck() { 
          return this.check != null && !this.check.isEmpty();
        }

        /**
         * @param value {@link #check} (FluentPath expression  - must be true or the mapping engine throws an error instead of completing.). This is the underlying object with id, value and extensions. The accessor "getCheck" gives direct access to the value
         */
        public StructureMapGroupRuleSourceComponent setCheckElement(StringType value) { 
          this.check = value;
          return this;
        }

        /**
         * @return FluentPath expression  - must be true or the mapping engine throws an error instead of completing.
         */
        public String getCheck() { 
          return this.check == null ? null : this.check.getValue();
        }

        /**
         * @param value FluentPath expression  - must be true or the mapping engine throws an error instead of completing.
         */
        public StructureMapGroupRuleSourceComponent setCheck(String value) { 
          if (Utilities.noString(value))
            this.check = null;
          else {
            if (this.check == null)
              this.check = new StringType();
            this.check.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("required", "boolean", "Whether this rule applies if the source isn't found.", 0, java.lang.Integer.MAX_VALUE, required));
          childrenList.add(new Property("context", "id", "Type or variable this rule applies to.", 0, java.lang.Integer.MAX_VALUE, context));
          childrenList.add(new Property("contextType", "code", "How to interpret the context.", 0, java.lang.Integer.MAX_VALUE, contextType));
          childrenList.add(new Property("element", "string", "Optional field for this source.", 0, java.lang.Integer.MAX_VALUE, element));
          childrenList.add(new Property("listMode", "code", "How to handle the list mode for this element.", 0, java.lang.Integer.MAX_VALUE, listMode));
          childrenList.add(new Property("variable", "id", "Named context for field, if a field is specified.", 0, java.lang.Integer.MAX_VALUE, variable));
          childrenList.add(new Property("condition", "string", "FluentPath expression  - must be true or the rule does not apply.", 0, java.lang.Integer.MAX_VALUE, condition));
          childrenList.add(new Property("check", "string", "FluentPath expression  - must be true or the mapping engine throws an error instead of completing.", 0, java.lang.Integer.MAX_VALUE, check));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -393139297: /*required*/ return this.required == null ? new Base[0] : new Base[] {this.required}; // BooleanType
        case 951530927: /*context*/ return this.context == null ? new Base[0] : new Base[] {this.context}; // IdType
        case -102839927: /*contextType*/ return this.contextType == null ? new Base[0] : new Base[] {this.contextType}; // Enumeration<StructureMapContextType>
        case -1662836996: /*element*/ return this.element == null ? new Base[0] : new Base[] {this.element}; // StringType
        case 1345445729: /*listMode*/ return this.listMode == null ? new Base[0] : new Base[] {this.listMode}; // Enumeration<StructureMapListMode>
        case -1249586564: /*variable*/ return this.variable == null ? new Base[0] : new Base[] {this.variable}; // IdType
        case -861311717: /*condition*/ return this.condition == null ? new Base[0] : new Base[] {this.condition}; // StringType
        case 94627080: /*check*/ return this.check == null ? new Base[0] : new Base[] {this.check}; // StringType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -393139297: // required
          this.required = castToBoolean(value); // BooleanType
          break;
        case 951530927: // context
          this.context = castToId(value); // IdType
          break;
        case -102839927: // contextType
          this.contextType = new StructureMapContextTypeEnumFactory().fromType(value); // Enumeration<StructureMapContextType>
          break;
        case -1662836996: // element
          this.element = castToString(value); // StringType
          break;
        case 1345445729: // listMode
          this.listMode = new StructureMapListModeEnumFactory().fromType(value); // Enumeration<StructureMapListMode>
          break;
        case -1249586564: // variable
          this.variable = castToId(value); // IdType
          break;
        case -861311717: // condition
          this.condition = castToString(value); // StringType
          break;
        case 94627080: // check
          this.check = castToString(value); // StringType
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("required"))
          this.required = castToBoolean(value); // BooleanType
        else if (name.equals("context"))
          this.context = castToId(value); // IdType
        else if (name.equals("contextType"))
          this.contextType = new StructureMapContextTypeEnumFactory().fromType(value); // Enumeration<StructureMapContextType>
        else if (name.equals("element"))
          this.element = castToString(value); // StringType
        else if (name.equals("listMode"))
          this.listMode = new StructureMapListModeEnumFactory().fromType(value); // Enumeration<StructureMapListMode>
        else if (name.equals("variable"))
          this.variable = castToId(value); // IdType
        else if (name.equals("condition"))
          this.condition = castToString(value); // StringType
        else if (name.equals("check"))
          this.check = castToString(value); // StringType
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -393139297: throw new FHIRException("Cannot make property required as it is not a complex type"); // BooleanType
        case 951530927: throw new FHIRException("Cannot make property context as it is not a complex type"); // IdType
        case -102839927: throw new FHIRException("Cannot make property contextType as it is not a complex type"); // Enumeration<StructureMapContextType>
        case -1662836996: throw new FHIRException("Cannot make property element as it is not a complex type"); // StringType
        case 1345445729: throw new FHIRException("Cannot make property listMode as it is not a complex type"); // Enumeration<StructureMapListMode>
        case -1249586564: throw new FHIRException("Cannot make property variable as it is not a complex type"); // IdType
        case -861311717: throw new FHIRException("Cannot make property condition as it is not a complex type"); // StringType
        case 94627080: throw new FHIRException("Cannot make property check as it is not a complex type"); // StringType
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("required")) {
          throw new FHIRException("Cannot call addChild on a primitive type StructureMap.required");
        }
        else if (name.equals("context")) {
          throw new FHIRException("Cannot call addChild on a primitive type StructureMap.context");
        }
        else if (name.equals("contextType")) {
          throw new FHIRException("Cannot call addChild on a primitive type StructureMap.contextType");
        }
        else if (name.equals("element")) {
          throw new FHIRException("Cannot call addChild on a primitive type StructureMap.element");
        }
        else if (name.equals("listMode")) {
          throw new FHIRException("Cannot call addChild on a primitive type StructureMap.listMode");
        }
        else if (name.equals("variable")) {
          throw new FHIRException("Cannot call addChild on a primitive type StructureMap.variable");
        }
        else if (name.equals("condition")) {
          throw new FHIRException("Cannot call addChild on a primitive type StructureMap.condition");
        }
        else if (name.equals("check")) {
          throw new FHIRException("Cannot call addChild on a primitive type StructureMap.check");
        }
        else
          return super.addChild(name);
      }

      public StructureMapGroupRuleSourceComponent copy() {
        StructureMapGroupRuleSourceComponent dst = new StructureMapGroupRuleSourceComponent();
        copyValues(dst);
        dst.required = required == null ? null : required.copy();
        dst.context = context == null ? null : context.copy();
        dst.contextType = contextType == null ? null : contextType.copy();
        dst.element = element == null ? null : element.copy();
        dst.listMode = listMode == null ? null : listMode.copy();
        dst.variable = variable == null ? null : variable.copy();
        dst.condition = condition == null ? null : condition.copy();
        dst.check = check == null ? null : check.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof StructureMapGroupRuleSourceComponent))
          return false;
        StructureMapGroupRuleSourceComponent o = (StructureMapGroupRuleSourceComponent) other;
        return compareDeep(required, o.required, true) && compareDeep(context, o.context, true) && compareDeep(contextType, o.contextType, true)
           && compareDeep(element, o.element, true) && compareDeep(listMode, o.listMode, true) && compareDeep(variable, o.variable, true)
           && compareDeep(condition, o.condition, true) && compareDeep(check, o.check, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof StructureMapGroupRuleSourceComponent))
          return false;
        StructureMapGroupRuleSourceComponent o = (StructureMapGroupRuleSourceComponent) other;
        return compareValues(required, o.required, true) && compareValues(context, o.context, true) && compareValues(contextType, o.contextType, true)
           && compareValues(element, o.element, true) && compareValues(listMode, o.listMode, true) && compareValues(variable, o.variable, true)
           && compareValues(condition, o.condition, true) && compareValues(check, o.check, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (required == null || required.isEmpty()) && (context == null || context.isEmpty())
           && (contextType == null || contextType.isEmpty()) && (element == null || element.isEmpty())
           && (listMode == null || listMode.isEmpty()) && (variable == null || variable.isEmpty()) && (condition == null || condition.isEmpty())
           && (check == null || check.isEmpty());
      }

  public String fhirType() {
    return "StructureMap.group.rule.source";

  }

  }

    @Block()
    public static class StructureMapGroupRuleTargetComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Type or variable this rule applies to.
         */
        @Child(name = "context", type = {IdType.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Type or variable this rule applies to", formalDefinition="Type or variable this rule applies to." )
        protected IdType context;

        /**
         * How to interpret the context.
         */
        @Child(name = "contextType", type = {CodeType.class}, order=2, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="type | variable", formalDefinition="How to interpret the context." )
        protected Enumeration<StructureMapContextType> contextType;

        /**
         * Field to create in the context.
         */
        @Child(name = "element", type = {StringType.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Field to create in the context", formalDefinition="Field to create in the context." )
        protected StringType element;

        /**
         * Named context for field, if desired, and a field is specified.
         */
        @Child(name = "variable", type = {IdType.class}, order=4, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Named context for field, if desired, and a field is specified", formalDefinition="Named context for field, if desired, and a field is specified." )
        protected IdType variable;

        /**
         * If field is a list, how to manage the list.
         */
        @Child(name = "listMode", type = {CodeType.class}, order=5, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="first | share | last", formalDefinition="If field is a list, how to manage the list." )
        protected List<Enumeration<StructureMapListMode>> listMode;

        /**
         * Internal rule reference for shared list items.
         */
        @Child(name = "listRuleId", type = {IdType.class}, order=6, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Internal rule reference for shared list items", formalDefinition="Internal rule reference for shared list items." )
        protected IdType listRuleId;

        /**
         * How the data is copied / created.
         */
        @Child(name = "transform", type = {CodeType.class}, order=7, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="create | copy +", formalDefinition="How the data is copied / created." )
        protected Enumeration<StructureMapTransform> transform;

        /**
         * Parameters to the transform.
         */
        @Child(name = "parameter", type = {}, order=8, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Parameters to the transform", formalDefinition="Parameters to the transform." )
        protected List<StructureMapGroupRuleTargetParameterComponent> parameter;

        private static final long serialVersionUID = 775400884L;

    /**
     * Constructor
     */
      public StructureMapGroupRuleTargetComponent() {
        super();
      }

    /**
     * Constructor
     */
      public StructureMapGroupRuleTargetComponent(IdType context, Enumeration<StructureMapContextType> contextType) {
        super();
        this.context = context;
        this.contextType = contextType;
      }

        /**
         * @return {@link #context} (Type or variable this rule applies to.). This is the underlying object with id, value and extensions. The accessor "getContext" gives direct access to the value
         */
        public IdType getContextElement() { 
          if (this.context == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create StructureMapGroupRuleTargetComponent.context");
            else if (Configuration.doAutoCreate())
              this.context = new IdType(); // bb
          return this.context;
        }

        public boolean hasContextElement() { 
          return this.context != null && !this.context.isEmpty();
        }

        public boolean hasContext() { 
          return this.context != null && !this.context.isEmpty();
        }

        /**
         * @param value {@link #context} (Type or variable this rule applies to.). This is the underlying object with id, value and extensions. The accessor "getContext" gives direct access to the value
         */
        public StructureMapGroupRuleTargetComponent setContextElement(IdType value) { 
          this.context = value;
          return this;
        }

        /**
         * @return Type or variable this rule applies to.
         */
        public String getContext() { 
          return this.context == null ? null : this.context.getValue();
        }

        /**
         * @param value Type or variable this rule applies to.
         */
        public StructureMapGroupRuleTargetComponent setContext(String value) { 
            if (this.context == null)
              this.context = new IdType();
            this.context.setValue(value);
          return this;
        }

        /**
         * @return {@link #contextType} (How to interpret the context.). This is the underlying object with id, value and extensions. The accessor "getContextType" gives direct access to the value
         */
        public Enumeration<StructureMapContextType> getContextTypeElement() { 
          if (this.contextType == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create StructureMapGroupRuleTargetComponent.contextType");
            else if (Configuration.doAutoCreate())
              this.contextType = new Enumeration<StructureMapContextType>(new StructureMapContextTypeEnumFactory()); // bb
          return this.contextType;
        }

        public boolean hasContextTypeElement() { 
          return this.contextType != null && !this.contextType.isEmpty();
        }

        public boolean hasContextType() { 
          return this.contextType != null && !this.contextType.isEmpty();
        }

        /**
         * @param value {@link #contextType} (How to interpret the context.). This is the underlying object with id, value and extensions. The accessor "getContextType" gives direct access to the value
         */
        public StructureMapGroupRuleTargetComponent setContextTypeElement(Enumeration<StructureMapContextType> value) { 
          this.contextType = value;
          return this;
        }

        /**
         * @return How to interpret the context.
         */
        public StructureMapContextType getContextType() { 
          return this.contextType == null ? null : this.contextType.getValue();
        }

        /**
         * @param value How to interpret the context.
         */
        public StructureMapGroupRuleTargetComponent setContextType(StructureMapContextType value) { 
            if (this.contextType == null)
              this.contextType = new Enumeration<StructureMapContextType>(new StructureMapContextTypeEnumFactory());
            this.contextType.setValue(value);
          return this;
        }

        /**
         * @return {@link #element} (Field to create in the context.). This is the underlying object with id, value and extensions. The accessor "getElement" gives direct access to the value
         */
        public StringType getElementElement() { 
          if (this.element == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create StructureMapGroupRuleTargetComponent.element");
            else if (Configuration.doAutoCreate())
              this.element = new StringType(); // bb
          return this.element;
        }

        public boolean hasElementElement() { 
          return this.element != null && !this.element.isEmpty();
        }

        public boolean hasElement() { 
          return this.element != null && !this.element.isEmpty();
        }

        /**
         * @param value {@link #element} (Field to create in the context.). This is the underlying object with id, value and extensions. The accessor "getElement" gives direct access to the value
         */
        public StructureMapGroupRuleTargetComponent setElementElement(StringType value) { 
          this.element = value;
          return this;
        }

        /**
         * @return Field to create in the context.
         */
        public String getElement() { 
          return this.element == null ? null : this.element.getValue();
        }

        /**
         * @param value Field to create in the context.
         */
        public StructureMapGroupRuleTargetComponent setElement(String value) { 
          if (Utilities.noString(value))
            this.element = null;
          else {
            if (this.element == null)
              this.element = new StringType();
            this.element.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #variable} (Named context for field, if desired, and a field is specified.). This is the underlying object with id, value and extensions. The accessor "getVariable" gives direct access to the value
         */
        public IdType getVariableElement() { 
          if (this.variable == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create StructureMapGroupRuleTargetComponent.variable");
            else if (Configuration.doAutoCreate())
              this.variable = new IdType(); // bb
          return this.variable;
        }

        public boolean hasVariableElement() { 
          return this.variable != null && !this.variable.isEmpty();
        }

        public boolean hasVariable() { 
          return this.variable != null && !this.variable.isEmpty();
        }

        /**
         * @param value {@link #variable} (Named context for field, if desired, and a field is specified.). This is the underlying object with id, value and extensions. The accessor "getVariable" gives direct access to the value
         */
        public StructureMapGroupRuleTargetComponent setVariableElement(IdType value) { 
          this.variable = value;
          return this;
        }

        /**
         * @return Named context for field, if desired, and a field is specified.
         */
        public String getVariable() { 
          return this.variable == null ? null : this.variable.getValue();
        }

        /**
         * @param value Named context for field, if desired, and a field is specified.
         */
        public StructureMapGroupRuleTargetComponent setVariable(String value) { 
          if (Utilities.noString(value))
            this.variable = null;
          else {
            if (this.variable == null)
              this.variable = new IdType();
            this.variable.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #listMode} (If field is a list, how to manage the list.)
         */
        public List<Enumeration<StructureMapListMode>> getListMode() { 
          if (this.listMode == null)
            this.listMode = new ArrayList<Enumeration<StructureMapListMode>>();
          return this.listMode;
        }

        public boolean hasListMode() { 
          if (this.listMode == null)
            return false;
          for (Enumeration<StructureMapListMode> item : this.listMode)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #listMode} (If field is a list, how to manage the list.)
         */
    // syntactic sugar
        public Enumeration<StructureMapListMode> addListModeElement() {//2 
          Enumeration<StructureMapListMode> t = new Enumeration<StructureMapListMode>(new StructureMapListModeEnumFactory());
          if (this.listMode == null)
            this.listMode = new ArrayList<Enumeration<StructureMapListMode>>();
          this.listMode.add(t);
          return t;
        }

        /**
         * @param value {@link #listMode} (If field is a list, how to manage the list.)
         */
        public StructureMapGroupRuleTargetComponent addListMode(StructureMapListMode value) { //1
          Enumeration<StructureMapListMode> t = new Enumeration<StructureMapListMode>(new StructureMapListModeEnumFactory());
          t.setValue(value);
          if (this.listMode == null)
            this.listMode = new ArrayList<Enumeration<StructureMapListMode>>();
          this.listMode.add(t);
          return this;
        }

        /**
         * @param value {@link #listMode} (If field is a list, how to manage the list.)
         */
        public boolean hasListMode(StructureMapListMode value) { 
          if (this.listMode == null)
            return false;
          for (Enumeration<StructureMapListMode> v : this.listMode)
            if (v.getValue().equals(value)) // code
              return true;
          return false;
        }

        /**
         * @return {@link #listRuleId} (Internal rule reference for shared list items.). This is the underlying object with id, value and extensions. The accessor "getListRuleId" gives direct access to the value
         */
        public IdType getListRuleIdElement() { 
          if (this.listRuleId == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create StructureMapGroupRuleTargetComponent.listRuleId");
            else if (Configuration.doAutoCreate())
              this.listRuleId = new IdType(); // bb
          return this.listRuleId;
        }

        public boolean hasListRuleIdElement() { 
          return this.listRuleId != null && !this.listRuleId.isEmpty();
        }

        public boolean hasListRuleId() { 
          return this.listRuleId != null && !this.listRuleId.isEmpty();
        }

        /**
         * @param value {@link #listRuleId} (Internal rule reference for shared list items.). This is the underlying object with id, value and extensions. The accessor "getListRuleId" gives direct access to the value
         */
        public StructureMapGroupRuleTargetComponent setListRuleIdElement(IdType value) { 
          this.listRuleId = value;
          return this;
        }

        /**
         * @return Internal rule reference for shared list items.
         */
        public String getListRuleId() { 
          return this.listRuleId == null ? null : this.listRuleId.getValue();
        }

        /**
         * @param value Internal rule reference for shared list items.
         */
        public StructureMapGroupRuleTargetComponent setListRuleId(String value) { 
          if (Utilities.noString(value))
            this.listRuleId = null;
          else {
            if (this.listRuleId == null)
              this.listRuleId = new IdType();
            this.listRuleId.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #transform} (How the data is copied / created.). This is the underlying object with id, value and extensions. The accessor "getTransform" gives direct access to the value
         */
        public Enumeration<StructureMapTransform> getTransformElement() { 
          if (this.transform == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create StructureMapGroupRuleTargetComponent.transform");
            else if (Configuration.doAutoCreate())
              this.transform = new Enumeration<StructureMapTransform>(new StructureMapTransformEnumFactory()); // bb
          return this.transform;
        }

        public boolean hasTransformElement() { 
          return this.transform != null && !this.transform.isEmpty();
        }

        public boolean hasTransform() { 
          return this.transform != null && !this.transform.isEmpty();
        }

        /**
         * @param value {@link #transform} (How the data is copied / created.). This is the underlying object with id, value and extensions. The accessor "getTransform" gives direct access to the value
         */
        public StructureMapGroupRuleTargetComponent setTransformElement(Enumeration<StructureMapTransform> value) { 
          this.transform = value;
          return this;
        }

        /**
         * @return How the data is copied / created.
         */
        public StructureMapTransform getTransform() { 
          return this.transform == null ? null : this.transform.getValue();
        }

        /**
         * @param value How the data is copied / created.
         */
        public StructureMapGroupRuleTargetComponent setTransform(StructureMapTransform value) { 
          if (value == null)
            this.transform = null;
          else {
            if (this.transform == null)
              this.transform = new Enumeration<StructureMapTransform>(new StructureMapTransformEnumFactory());
            this.transform.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #parameter} (Parameters to the transform.)
         */
        public List<StructureMapGroupRuleTargetParameterComponent> getParameter() { 
          if (this.parameter == null)
            this.parameter = new ArrayList<StructureMapGroupRuleTargetParameterComponent>();
          return this.parameter;
        }

        public boolean hasParameter() { 
          if (this.parameter == null)
            return false;
          for (StructureMapGroupRuleTargetParameterComponent item : this.parameter)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #parameter} (Parameters to the transform.)
         */
    // syntactic sugar
        public StructureMapGroupRuleTargetParameterComponent addParameter() { //3
          StructureMapGroupRuleTargetParameterComponent t = new StructureMapGroupRuleTargetParameterComponent();
          if (this.parameter == null)
            this.parameter = new ArrayList<StructureMapGroupRuleTargetParameterComponent>();
          this.parameter.add(t);
          return t;
        }

    // syntactic sugar
        public StructureMapGroupRuleTargetComponent addParameter(StructureMapGroupRuleTargetParameterComponent t) { //3
          if (t == null)
            return this;
          if (this.parameter == null)
            this.parameter = new ArrayList<StructureMapGroupRuleTargetParameterComponent>();
          this.parameter.add(t);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("context", "id", "Type or variable this rule applies to.", 0, java.lang.Integer.MAX_VALUE, context));
          childrenList.add(new Property("contextType", "code", "How to interpret the context.", 0, java.lang.Integer.MAX_VALUE, contextType));
          childrenList.add(new Property("element", "string", "Field to create in the context.", 0, java.lang.Integer.MAX_VALUE, element));
          childrenList.add(new Property("variable", "id", "Named context for field, if desired, and a field is specified.", 0, java.lang.Integer.MAX_VALUE, variable));
          childrenList.add(new Property("listMode", "code", "If field is a list, how to manage the list.", 0, java.lang.Integer.MAX_VALUE, listMode));
          childrenList.add(new Property("listRuleId", "id", "Internal rule reference for shared list items.", 0, java.lang.Integer.MAX_VALUE, listRuleId));
          childrenList.add(new Property("transform", "code", "How the data is copied / created.", 0, java.lang.Integer.MAX_VALUE, transform));
          childrenList.add(new Property("parameter", "", "Parameters to the transform.", 0, java.lang.Integer.MAX_VALUE, parameter));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 951530927: /*context*/ return this.context == null ? new Base[0] : new Base[] {this.context}; // IdType
        case -102839927: /*contextType*/ return this.contextType == null ? new Base[0] : new Base[] {this.contextType}; // Enumeration<StructureMapContextType>
        case -1662836996: /*element*/ return this.element == null ? new Base[0] : new Base[] {this.element}; // StringType
        case -1249586564: /*variable*/ return this.variable == null ? new Base[0] : new Base[] {this.variable}; // IdType
        case 1345445729: /*listMode*/ return this.listMode == null ? new Base[0] : this.listMode.toArray(new Base[this.listMode.size()]); // Enumeration<StructureMapListMode>
        case 337117045: /*listRuleId*/ return this.listRuleId == null ? new Base[0] : new Base[] {this.listRuleId}; // IdType
        case 1052666732: /*transform*/ return this.transform == null ? new Base[0] : new Base[] {this.transform}; // Enumeration<StructureMapTransform>
        case 1954460585: /*parameter*/ return this.parameter == null ? new Base[0] : this.parameter.toArray(new Base[this.parameter.size()]); // StructureMapGroupRuleTargetParameterComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 951530927: // context
          this.context = castToId(value); // IdType
          break;
        case -102839927: // contextType
          this.contextType = new StructureMapContextTypeEnumFactory().fromType(value); // Enumeration<StructureMapContextType>
          break;
        case -1662836996: // element
          this.element = castToString(value); // StringType
          break;
        case -1249586564: // variable
          this.variable = castToId(value); // IdType
          break;
        case 1345445729: // listMode
          this.getListMode().add(new StructureMapListModeEnumFactory().fromType(value)); // Enumeration<StructureMapListMode>
          break;
        case 337117045: // listRuleId
          this.listRuleId = castToId(value); // IdType
          break;
        case 1052666732: // transform
          this.transform = new StructureMapTransformEnumFactory().fromType(value); // Enumeration<StructureMapTransform>
          break;
        case 1954460585: // parameter
          this.getParameter().add((StructureMapGroupRuleTargetParameterComponent) value); // StructureMapGroupRuleTargetParameterComponent
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("context"))
          this.context = castToId(value); // IdType
        else if (name.equals("contextType"))
          this.contextType = new StructureMapContextTypeEnumFactory().fromType(value); // Enumeration<StructureMapContextType>
        else if (name.equals("element"))
          this.element = castToString(value); // StringType
        else if (name.equals("variable"))
          this.variable = castToId(value); // IdType
        else if (name.equals("listMode"))
          this.getListMode().add(new StructureMapListModeEnumFactory().fromType(value));
        else if (name.equals("listRuleId"))
          this.listRuleId = castToId(value); // IdType
        else if (name.equals("transform"))
          this.transform = new StructureMapTransformEnumFactory().fromType(value); // Enumeration<StructureMapTransform>
        else if (name.equals("parameter"))
          this.getParameter().add((StructureMapGroupRuleTargetParameterComponent) value);
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 951530927: throw new FHIRException("Cannot make property context as it is not a complex type"); // IdType
        case -102839927: throw new FHIRException("Cannot make property contextType as it is not a complex type"); // Enumeration<StructureMapContextType>
        case -1662836996: throw new FHIRException("Cannot make property element as it is not a complex type"); // StringType
        case -1249586564: throw new FHIRException("Cannot make property variable as it is not a complex type"); // IdType
        case 1345445729: throw new FHIRException("Cannot make property listMode as it is not a complex type"); // Enumeration<StructureMapListMode>
        case 337117045: throw new FHIRException("Cannot make property listRuleId as it is not a complex type"); // IdType
        case 1052666732: throw new FHIRException("Cannot make property transform as it is not a complex type"); // Enumeration<StructureMapTransform>
        case 1954460585:  return addParameter(); // StructureMapGroupRuleTargetParameterComponent
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("context")) {
          throw new FHIRException("Cannot call addChild on a primitive type StructureMap.context");
        }
        else if (name.equals("contextType")) {
          throw new FHIRException("Cannot call addChild on a primitive type StructureMap.contextType");
        }
        else if (name.equals("element")) {
          throw new FHIRException("Cannot call addChild on a primitive type StructureMap.element");
        }
        else if (name.equals("variable")) {
          throw new FHIRException("Cannot call addChild on a primitive type StructureMap.variable");
        }
        else if (name.equals("listMode")) {
          throw new FHIRException("Cannot call addChild on a primitive type StructureMap.listMode");
        }
        else if (name.equals("listRuleId")) {
          throw new FHIRException("Cannot call addChild on a primitive type StructureMap.listRuleId");
        }
        else if (name.equals("transform")) {
          throw new FHIRException("Cannot call addChild on a primitive type StructureMap.transform");
        }
        else if (name.equals("parameter")) {
          return addParameter();
        }
        else
          return super.addChild(name);
      }

      public StructureMapGroupRuleTargetComponent copy() {
        StructureMapGroupRuleTargetComponent dst = new StructureMapGroupRuleTargetComponent();
        copyValues(dst);
        dst.context = context == null ? null : context.copy();
        dst.contextType = contextType == null ? null : contextType.copy();
        dst.element = element == null ? null : element.copy();
        dst.variable = variable == null ? null : variable.copy();
        if (listMode != null) {
          dst.listMode = new ArrayList<Enumeration<StructureMapListMode>>();
          for (Enumeration<StructureMapListMode> i : listMode)
            dst.listMode.add(i.copy());
        };
        dst.listRuleId = listRuleId == null ? null : listRuleId.copy();
        dst.transform = transform == null ? null : transform.copy();
        if (parameter != null) {
          dst.parameter = new ArrayList<StructureMapGroupRuleTargetParameterComponent>();
          for (StructureMapGroupRuleTargetParameterComponent i : parameter)
            dst.parameter.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof StructureMapGroupRuleTargetComponent))
          return false;
        StructureMapGroupRuleTargetComponent o = (StructureMapGroupRuleTargetComponent) other;
        return compareDeep(context, o.context, true) && compareDeep(contextType, o.contextType, true) && compareDeep(element, o.element, true)
           && compareDeep(variable, o.variable, true) && compareDeep(listMode, o.listMode, true) && compareDeep(listRuleId, o.listRuleId, true)
           && compareDeep(transform, o.transform, true) && compareDeep(parameter, o.parameter, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof StructureMapGroupRuleTargetComponent))
          return false;
        StructureMapGroupRuleTargetComponent o = (StructureMapGroupRuleTargetComponent) other;
        return compareValues(context, o.context, true) && compareValues(contextType, o.contextType, true) && compareValues(element, o.element, true)
           && compareValues(variable, o.variable, true) && compareValues(listMode, o.listMode, true) && compareValues(listRuleId, o.listRuleId, true)
           && compareValues(transform, o.transform, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (context == null || context.isEmpty()) && (contextType == null || contextType.isEmpty())
           && (element == null || element.isEmpty()) && (variable == null || variable.isEmpty()) && (listMode == null || listMode.isEmpty())
           && (listRuleId == null || listRuleId.isEmpty()) && (transform == null || transform.isEmpty())
           && (parameter == null || parameter.isEmpty());
      }

  public String fhirType() {
    return "StructureMap.group.rule.target";

  }

  }

    @Block()
    public static class StructureMapGroupRuleTargetParameterComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Parameter value - variable or literal.
         */
        @Child(name = "value", type = {IdType.class, StringType.class, BooleanType.class, IntegerType.class, DecimalType.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Parameter value - variable or literal", formalDefinition="Parameter value - variable or literal." )
        protected Type value;

        private static final long serialVersionUID = -732981989L;

    /**
     * Constructor
     */
      public StructureMapGroupRuleTargetParameterComponent() {
        super();
      }

    /**
     * Constructor
     */
      public StructureMapGroupRuleTargetParameterComponent(Type value) {
        super();
        this.value = value;
      }

        /**
         * @return {@link #value} (Parameter value - variable or literal.)
         */
        public Type getValue() { 
          return this.value;
        }

        /**
         * @return {@link #value} (Parameter value - variable or literal.)
         */
        public IdType getValueIdType() throws FHIRException { 
          if (!(this.value instanceof IdType))
            throw new FHIRException("Type mismatch: the type IdType was expected, but "+this.value.getClass().getName()+" was encountered");
          return (IdType) this.value;
        }

        public boolean hasValueIdType() { 
          return this.value instanceof IdType;
        }

        /**
         * @return {@link #value} (Parameter value - variable or literal.)
         */
        public StringType getValueStringType() throws FHIRException { 
          if (!(this.value instanceof StringType))
            throw new FHIRException("Type mismatch: the type StringType was expected, but "+this.value.getClass().getName()+" was encountered");
          return (StringType) this.value;
        }

        public boolean hasValueStringType() { 
          return this.value instanceof StringType;
        }

        /**
         * @return {@link #value} (Parameter value - variable or literal.)
         */
        public BooleanType getValueBooleanType() throws FHIRException { 
          if (!(this.value instanceof BooleanType))
            throw new FHIRException("Type mismatch: the type BooleanType was expected, but "+this.value.getClass().getName()+" was encountered");
          return (BooleanType) this.value;
        }

        public boolean hasValueBooleanType() { 
          return this.value instanceof BooleanType;
        }

        /**
         * @return {@link #value} (Parameter value - variable or literal.)
         */
        public IntegerType getValueIntegerType() throws FHIRException { 
          if (!(this.value instanceof IntegerType))
            throw new FHIRException("Type mismatch: the type IntegerType was expected, but "+this.value.getClass().getName()+" was encountered");
          return (IntegerType) this.value;
        }

        public boolean hasValueIntegerType() { 
          return this.value instanceof IntegerType;
        }

        /**
         * @return {@link #value} (Parameter value - variable or literal.)
         */
        public DecimalType getValueDecimalType() throws FHIRException { 
          if (!(this.value instanceof DecimalType))
            throw new FHIRException("Type mismatch: the type DecimalType was expected, but "+this.value.getClass().getName()+" was encountered");
          return (DecimalType) this.value;
        }

        public boolean hasValueDecimalType() { 
          return this.value instanceof DecimalType;
        }

        public boolean hasValue() { 
          return this.value != null && !this.value.isEmpty();
        }

        /**
         * @param value {@link #value} (Parameter value - variable or literal.)
         */
        public StructureMapGroupRuleTargetParameterComponent setValue(Type value) { 
          this.value = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("value[x]", "id|string|boolean|integer|decimal", "Parameter value - variable or literal.", 0, java.lang.Integer.MAX_VALUE, value));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 111972721: /*value*/ return this.value == null ? new Base[0] : new Base[] {this.value}; // Type
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 111972721: // value
          this.value = (Type) value; // Type
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("value[x]"))
          this.value = (Type) value; // Type
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1410166417:  return getValue(); // Type
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("valueId")) {
          this.value = new IdType();
          return this.value;
        }
        else if (name.equals("valueString")) {
          this.value = new StringType();
          return this.value;
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
        else
          return super.addChild(name);
      }

      public StructureMapGroupRuleTargetParameterComponent copy() {
        StructureMapGroupRuleTargetParameterComponent dst = new StructureMapGroupRuleTargetParameterComponent();
        copyValues(dst);
        dst.value = value == null ? null : value.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof StructureMapGroupRuleTargetParameterComponent))
          return false;
        StructureMapGroupRuleTargetParameterComponent o = (StructureMapGroupRuleTargetParameterComponent) other;
        return compareDeep(value, o.value, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof StructureMapGroupRuleTargetParameterComponent))
          return false;
        StructureMapGroupRuleTargetParameterComponent o = (StructureMapGroupRuleTargetParameterComponent) other;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (value == null || value.isEmpty());
      }

  public String fhirType() {
    return "StructureMap.group.rule.target.parameter";

  }

  }

    @Block()
    public static class StructureMapGroupRuleDependentComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Name of a rule or group to apply.
         */
        @Child(name = "name", type = {IdType.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Name of a rule or group to apply", formalDefinition="Name of a rule or group to apply." )
        protected IdType name;

        /**
         * Names of variables to pass to the rule or group.
         */
        @Child(name = "variable", type = {StringType.class}, order=2, min=1, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Names of variables to pass to the rule or group", formalDefinition="Names of variables to pass to the rule or group." )
        protected List<StringType> variable;

        private static final long serialVersionUID = 1021661591L;

    /**
     * Constructor
     */
      public StructureMapGroupRuleDependentComponent() {
        super();
      }

    /**
     * Constructor
     */
      public StructureMapGroupRuleDependentComponent(IdType name) {
        super();
        this.name = name;
      }

        /**
         * @return {@link #name} (Name of a rule or group to apply.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public IdType getNameElement() { 
          if (this.name == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create StructureMapGroupRuleDependentComponent.name");
            else if (Configuration.doAutoCreate())
              this.name = new IdType(); // bb
          return this.name;
        }

        public boolean hasNameElement() { 
          return this.name != null && !this.name.isEmpty();
        }

        public boolean hasName() { 
          return this.name != null && !this.name.isEmpty();
        }

        /**
         * @param value {@link #name} (Name of a rule or group to apply.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public StructureMapGroupRuleDependentComponent setNameElement(IdType value) { 
          this.name = value;
          return this;
        }

        /**
         * @return Name of a rule or group to apply.
         */
        public String getName() { 
          return this.name == null ? null : this.name.getValue();
        }

        /**
         * @param value Name of a rule or group to apply.
         */
        public StructureMapGroupRuleDependentComponent setName(String value) { 
            if (this.name == null)
              this.name = new IdType();
            this.name.setValue(value);
          return this;
        }

        /**
         * @return {@link #variable} (Names of variables to pass to the rule or group.)
         */
        public List<StringType> getVariable() { 
          if (this.variable == null)
            this.variable = new ArrayList<StringType>();
          return this.variable;
        }

        public boolean hasVariable() { 
          if (this.variable == null)
            return false;
          for (StringType item : this.variable)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #variable} (Names of variables to pass to the rule or group.)
         */
    // syntactic sugar
        public StringType addVariableElement() {//2 
          StringType t = new StringType();
          if (this.variable == null)
            this.variable = new ArrayList<StringType>();
          this.variable.add(t);
          return t;
        }

        /**
         * @param value {@link #variable} (Names of variables to pass to the rule or group.)
         */
        public StructureMapGroupRuleDependentComponent addVariable(String value) { //1
          StringType t = new StringType();
          t.setValue(value);
          if (this.variable == null)
            this.variable = new ArrayList<StringType>();
          this.variable.add(t);
          return this;
        }

        /**
         * @param value {@link #variable} (Names of variables to pass to the rule or group.)
         */
        public boolean hasVariable(String value) { 
          if (this.variable == null)
            return false;
          for (StringType v : this.variable)
            if (v.equals(value)) // string
              return true;
          return false;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("name", "id", "Name of a rule or group to apply.", 0, java.lang.Integer.MAX_VALUE, name));
          childrenList.add(new Property("variable", "string", "Names of variables to pass to the rule or group.", 0, java.lang.Integer.MAX_VALUE, variable));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3373707: /*name*/ return this.name == null ? new Base[0] : new Base[] {this.name}; // IdType
        case -1249586564: /*variable*/ return this.variable == null ? new Base[0] : this.variable.toArray(new Base[this.variable.size()]); // StringType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3373707: // name
          this.name = castToId(value); // IdType
          break;
        case -1249586564: // variable
          this.getVariable().add(castToString(value)); // StringType
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("name"))
          this.name = castToId(value); // IdType
        else if (name.equals("variable"))
          this.getVariable().add(castToString(value));
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3373707: throw new FHIRException("Cannot make property name as it is not a complex type"); // IdType
        case -1249586564: throw new FHIRException("Cannot make property variable as it is not a complex type"); // StringType
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("name")) {
          throw new FHIRException("Cannot call addChild on a primitive type StructureMap.name");
        }
        else if (name.equals("variable")) {
          throw new FHIRException("Cannot call addChild on a primitive type StructureMap.variable");
        }
        else
          return super.addChild(name);
      }

      public StructureMapGroupRuleDependentComponent copy() {
        StructureMapGroupRuleDependentComponent dst = new StructureMapGroupRuleDependentComponent();
        copyValues(dst);
        dst.name = name == null ? null : name.copy();
        if (variable != null) {
          dst.variable = new ArrayList<StringType>();
          for (StringType i : variable)
            dst.variable.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof StructureMapGroupRuleDependentComponent))
          return false;
        StructureMapGroupRuleDependentComponent o = (StructureMapGroupRuleDependentComponent) other;
        return compareDeep(name, o.name, true) && compareDeep(variable, o.variable, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof StructureMapGroupRuleDependentComponent))
          return false;
        StructureMapGroupRuleDependentComponent o = (StructureMapGroupRuleDependentComponent) other;
        return compareValues(name, o.name, true) && compareValues(variable, o.variable, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (name == null || name.isEmpty()) && (variable == null || variable.isEmpty())
          ;
      }

  public String fhirType() {
    return "StructureMap.group.rule.dependent";

  }

  }

    /**
     * An absolute URL that is used to identify this structure map when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this structure map is (or will be) published.
     */
    @Child(name = "url", type = {UriType.class}, order=0, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Absolute URL used to reference this StructureMap", formalDefinition="An absolute URL that is used to identify this structure map when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this structure map is (or will be) published." )
    protected UriType url;

    /**
     * Formal identifier that is used to identify this StructureMap when it is represented in other formats, or referenced in a specification, model, design or an instance  (should be globally unique OID, UUID, or URI), (if it's not possible to use the literal URI).
     */
    @Child(name = "identifier", type = {Identifier.class}, order=1, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Other identifiers for the StructureMap", formalDefinition="Formal identifier that is used to identify this StructureMap when it is represented in other formats, or referenced in a specification, model, design or an instance  (should be globally unique OID, UUID, or URI), (if it's not possible to use the literal URI)." )
    protected List<Identifier> identifier;

    /**
     * The identifier that is used to identify this version of the StructureMap when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the StructureMap author manually.
     */
    @Child(name = "version", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Logical id for this version of the StructureMap", formalDefinition="The identifier that is used to identify this version of the StructureMap when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the StructureMap author manually." )
    protected StringType version;

    /**
     * A free text natural language name identifying the StructureMap.
     */
    @Child(name = "name", type = {StringType.class}, order=3, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Informal name for this StructureMap", formalDefinition="A free text natural language name identifying the StructureMap." )
    protected StringType name;

    /**
     * The status of the StructureMap.
     */
    @Child(name = "status", type = {CodeType.class}, order=4, min=1, max=1, modifier=true, summary=true)
    @Description(shortDefinition="draft | active | retired", formalDefinition="The status of the StructureMap." )
    protected Enumeration<ConformanceResourceStatus> status;

    /**
     * This StructureMap was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.
     */
    @Child(name = "experimental", type = {BooleanType.class}, order=5, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="If for testing purposes, not real usage", formalDefinition="This StructureMap was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage." )
    protected BooleanType experimental;

    /**
     * The name of the individual or organization that published the structure map.
     */
    @Child(name = "publisher", type = {StringType.class}, order=6, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Name of the publisher (Organization or individual)", formalDefinition="The name of the individual or organization that published the structure map." )
    protected StringType publisher;

    /**
     * Contacts to assist a user in finding and communicating with the publisher.
     */
    @Child(name = "contact", type = {}, order=7, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Contact details of the publisher", formalDefinition="Contacts to assist a user in finding and communicating with the publisher." )
    protected List<StructureMapContactComponent> contact;

    /**
     * The date this version of the structure map was published. The date must change when the business version changes, if it does, and it must change if the status code changes. In addition, it should change when the substantive content of the structure map changes.
     */
    @Child(name = "date", type = {DateTimeType.class}, order=8, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Date for this version of the StructureMap", formalDefinition="The date this version of the structure map was published. The date must change when the business version changes, if it does, and it must change if the status code changes. In addition, it should change when the substantive content of the structure map changes." )
    protected DateTimeType date;

    /**
     * A free text natural language description of the StructureMap and its use.
     */
    @Child(name = "description", type = {StringType.class}, order=9, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Natural language description of the StructureMap", formalDefinition="A free text natural language description of the StructureMap and its use." )
    protected StringType description;

    /**
     * The content was developed with a focus and intent of supporting the contexts that are listed. These terms may be used to assist with indexing and searching of structure maps.
     */
    @Child(name = "useContext", type = {CodeableConcept.class}, order=10, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Content intends to support these contexts", formalDefinition="The content was developed with a focus and intent of supporting the contexts that are listed. These terms may be used to assist with indexing and searching of structure maps." )
    protected List<CodeableConcept> useContext;

    /**
     * Explains why this structure map is needed and why it's been designed as it has.
     */
    @Child(name = "requirements", type = {StringType.class}, order=11, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Scope and Usage this structure map is for", formalDefinition="Explains why this structure map is needed and why it's been designed as it has." )
    protected StringType requirements;

    /**
     * A copyright statement relating to the structure map and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the details of the constraints and mappings.
     */
    @Child(name = "copyright", type = {StringType.class}, order=12, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Use and/or publishing restrictions", formalDefinition="A copyright statement relating to the structure map and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the details of the constraints and mappings." )
    protected StringType copyright;

    /**
     * A structure definition used by this map. The structure definition may describe instances that are converted, or the instances that are produced.
     */
    @Child(name = "structure", type = {}, order=13, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Structure Definition used by this map", formalDefinition="A structure definition used by this map. The structure definition may describe instances that are converted, or the instances that are produced." )
    protected List<StructureMapStructureComponent> structure;

    /**
     * Other maps used by this map (canonical URLs).
     */
    @Child(name = "import", type = {UriType.class}, order=14, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Other maps used by this map (canonical URLs)", formalDefinition="Other maps used by this map (canonical URLs)." )
    protected List<UriType> import_;

    /**
     * Named sections for reader convenience.
     */
    @Child(name = "group", type = {}, order=15, min=1, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Named sections for reader convenience", formalDefinition="Named sections for reader convenience." )
    protected List<StructureMapGroupComponent> group;

    private static final long serialVersionUID = 710892955L;

  /**
   * Constructor
   */
    public StructureMap() {
      super();
    }

  /**
   * Constructor
   */
    public StructureMap(UriType url, StringType name, Enumeration<ConformanceResourceStatus> status) {
      super();
      this.url = url;
      this.name = name;
      this.status = status;
    }

    /**
     * @return {@link #url} (An absolute URL that is used to identify this structure map when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this structure map is (or will be) published.). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
     */
    public UriType getUrlElement() { 
      if (this.url == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create StructureMap.url");
        else if (Configuration.doAutoCreate())
          this.url = new UriType(); // bb
      return this.url;
    }

    public boolean hasUrlElement() { 
      return this.url != null && !this.url.isEmpty();
    }

    public boolean hasUrl() { 
      return this.url != null && !this.url.isEmpty();
    }

    /**
     * @param value {@link #url} (An absolute URL that is used to identify this structure map when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this structure map is (or will be) published.). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
     */
    public StructureMap setUrlElement(UriType value) { 
      this.url = value;
      return this;
    }

    /**
     * @return An absolute URL that is used to identify this structure map when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this structure map is (or will be) published.
     */
    public String getUrl() { 
      return this.url == null ? null : this.url.getValue();
    }

    /**
     * @param value An absolute URL that is used to identify this structure map when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this structure map is (or will be) published.
     */
    public StructureMap setUrl(String value) { 
        if (this.url == null)
          this.url = new UriType();
        this.url.setValue(value);
      return this;
    }

    /**
     * @return {@link #identifier} (Formal identifier that is used to identify this StructureMap when it is represented in other formats, or referenced in a specification, model, design or an instance  (should be globally unique OID, UUID, or URI), (if it's not possible to use the literal URI).)
     */
    public List<Identifier> getIdentifier() { 
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      return this.identifier;
    }

    public boolean hasIdentifier() { 
      if (this.identifier == null)
        return false;
      for (Identifier item : this.identifier)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #identifier} (Formal identifier that is used to identify this StructureMap when it is represented in other formats, or referenced in a specification, model, design or an instance  (should be globally unique OID, UUID, or URI), (if it's not possible to use the literal URI).)
     */
    // syntactic sugar
    public Identifier addIdentifier() { //3
      Identifier t = new Identifier();
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      this.identifier.add(t);
      return t;
    }

    // syntactic sugar
    public StructureMap addIdentifier(Identifier t) { //3
      if (t == null)
        return this;
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      this.identifier.add(t);
      return this;
    }

    /**
     * @return {@link #version} (The identifier that is used to identify this version of the StructureMap when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the StructureMap author manually.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
     */
    public StringType getVersionElement() { 
      if (this.version == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create StructureMap.version");
        else if (Configuration.doAutoCreate())
          this.version = new StringType(); // bb
      return this.version;
    }

    public boolean hasVersionElement() { 
      return this.version != null && !this.version.isEmpty();
    }

    public boolean hasVersion() { 
      return this.version != null && !this.version.isEmpty();
    }

    /**
     * @param value {@link #version} (The identifier that is used to identify this version of the StructureMap when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the StructureMap author manually.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
     */
    public StructureMap setVersionElement(StringType value) { 
      this.version = value;
      return this;
    }

    /**
     * @return The identifier that is used to identify this version of the StructureMap when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the StructureMap author manually.
     */
    public String getVersion() { 
      return this.version == null ? null : this.version.getValue();
    }

    /**
     * @param value The identifier that is used to identify this version of the StructureMap when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the StructureMap author manually.
     */
    public StructureMap setVersion(String value) { 
      if (Utilities.noString(value))
        this.version = null;
      else {
        if (this.version == null)
          this.version = new StringType();
        this.version.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #name} (A free text natural language name identifying the StructureMap.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
     */
    public StringType getNameElement() { 
      if (this.name == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create StructureMap.name");
        else if (Configuration.doAutoCreate())
          this.name = new StringType(); // bb
      return this.name;
    }

    public boolean hasNameElement() { 
      return this.name != null && !this.name.isEmpty();
    }

    public boolean hasName() { 
      return this.name != null && !this.name.isEmpty();
    }

    /**
     * @param value {@link #name} (A free text natural language name identifying the StructureMap.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
     */
    public StructureMap setNameElement(StringType value) { 
      this.name = value;
      return this;
    }

    /**
     * @return A free text natural language name identifying the StructureMap.
     */
    public String getName() { 
      return this.name == null ? null : this.name.getValue();
    }

    /**
     * @param value A free text natural language name identifying the StructureMap.
     */
    public StructureMap setName(String value) { 
        if (this.name == null)
          this.name = new StringType();
        this.name.setValue(value);
      return this;
    }

    /**
     * @return {@link #status} (The status of the StructureMap.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<ConformanceResourceStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create StructureMap.status");
        else if (Configuration.doAutoCreate())
          this.status = new Enumeration<ConformanceResourceStatus>(new ConformanceResourceStatusEnumFactory()); // bb
      return this.status;
    }

    public boolean hasStatusElement() { 
      return this.status != null && !this.status.isEmpty();
    }

    public boolean hasStatus() { 
      return this.status != null && !this.status.isEmpty();
    }

    /**
     * @param value {@link #status} (The status of the StructureMap.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public StructureMap setStatusElement(Enumeration<ConformanceResourceStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return The status of the StructureMap.
     */
    public ConformanceResourceStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value The status of the StructureMap.
     */
    public StructureMap setStatus(ConformanceResourceStatus value) { 
        if (this.status == null)
          this.status = new Enumeration<ConformanceResourceStatus>(new ConformanceResourceStatusEnumFactory());
        this.status.setValue(value);
      return this;
    }

    /**
     * @return {@link #experimental} (This StructureMap was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.). This is the underlying object with id, value and extensions. The accessor "getExperimental" gives direct access to the value
     */
    public BooleanType getExperimentalElement() { 
      if (this.experimental == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create StructureMap.experimental");
        else if (Configuration.doAutoCreate())
          this.experimental = new BooleanType(); // bb
      return this.experimental;
    }

    public boolean hasExperimentalElement() { 
      return this.experimental != null && !this.experimental.isEmpty();
    }

    public boolean hasExperimental() { 
      return this.experimental != null && !this.experimental.isEmpty();
    }

    /**
     * @param value {@link #experimental} (This StructureMap was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.). This is the underlying object with id, value and extensions. The accessor "getExperimental" gives direct access to the value
     */
    public StructureMap setExperimentalElement(BooleanType value) { 
      this.experimental = value;
      return this;
    }

    /**
     * @return This StructureMap was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.
     */
    public boolean getExperimental() { 
      return this.experimental == null || this.experimental.isEmpty() ? false : this.experimental.getValue();
    }

    /**
     * @param value This StructureMap was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.
     */
    public StructureMap setExperimental(boolean value) { 
        if (this.experimental == null)
          this.experimental = new BooleanType();
        this.experimental.setValue(value);
      return this;
    }

    /**
     * @return {@link #publisher} (The name of the individual or organization that published the structure map.). This is the underlying object with id, value and extensions. The accessor "getPublisher" gives direct access to the value
     */
    public StringType getPublisherElement() { 
      if (this.publisher == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create StructureMap.publisher");
        else if (Configuration.doAutoCreate())
          this.publisher = new StringType(); // bb
      return this.publisher;
    }

    public boolean hasPublisherElement() { 
      return this.publisher != null && !this.publisher.isEmpty();
    }

    public boolean hasPublisher() { 
      return this.publisher != null && !this.publisher.isEmpty();
    }

    /**
     * @param value {@link #publisher} (The name of the individual or organization that published the structure map.). This is the underlying object with id, value and extensions. The accessor "getPublisher" gives direct access to the value
     */
    public StructureMap setPublisherElement(StringType value) { 
      this.publisher = value;
      return this;
    }

    /**
     * @return The name of the individual or organization that published the structure map.
     */
    public String getPublisher() { 
      return this.publisher == null ? null : this.publisher.getValue();
    }

    /**
     * @param value The name of the individual or organization that published the structure map.
     */
    public StructureMap setPublisher(String value) { 
      if (Utilities.noString(value))
        this.publisher = null;
      else {
        if (this.publisher == null)
          this.publisher = new StringType();
        this.publisher.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #contact} (Contacts to assist a user in finding and communicating with the publisher.)
     */
    public List<StructureMapContactComponent> getContact() { 
      if (this.contact == null)
        this.contact = new ArrayList<StructureMapContactComponent>();
      return this.contact;
    }

    public boolean hasContact() { 
      if (this.contact == null)
        return false;
      for (StructureMapContactComponent item : this.contact)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #contact} (Contacts to assist a user in finding and communicating with the publisher.)
     */
    // syntactic sugar
    public StructureMapContactComponent addContact() { //3
      StructureMapContactComponent t = new StructureMapContactComponent();
      if (this.contact == null)
        this.contact = new ArrayList<StructureMapContactComponent>();
      this.contact.add(t);
      return t;
    }

    // syntactic sugar
    public StructureMap addContact(StructureMapContactComponent t) { //3
      if (t == null)
        return this;
      if (this.contact == null)
        this.contact = new ArrayList<StructureMapContactComponent>();
      this.contact.add(t);
      return this;
    }

    /**
     * @return {@link #date} (The date this version of the structure map was published. The date must change when the business version changes, if it does, and it must change if the status code changes. In addition, it should change when the substantive content of the structure map changes.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
     */
    public DateTimeType getDateElement() { 
      if (this.date == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create StructureMap.date");
        else if (Configuration.doAutoCreate())
          this.date = new DateTimeType(); // bb
      return this.date;
    }

    public boolean hasDateElement() { 
      return this.date != null && !this.date.isEmpty();
    }

    public boolean hasDate() { 
      return this.date != null && !this.date.isEmpty();
    }

    /**
     * @param value {@link #date} (The date this version of the structure map was published. The date must change when the business version changes, if it does, and it must change if the status code changes. In addition, it should change when the substantive content of the structure map changes.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
     */
    public StructureMap setDateElement(DateTimeType value) { 
      this.date = value;
      return this;
    }

    /**
     * @return The date this version of the structure map was published. The date must change when the business version changes, if it does, and it must change if the status code changes. In addition, it should change when the substantive content of the structure map changes.
     */
    public Date getDate() { 
      return this.date == null ? null : this.date.getValue();
    }

    /**
     * @param value The date this version of the structure map was published. The date must change when the business version changes, if it does, and it must change if the status code changes. In addition, it should change when the substantive content of the structure map changes.
     */
    public StructureMap setDate(Date value) { 
      if (value == null)
        this.date = null;
      else {
        if (this.date == null)
          this.date = new DateTimeType();
        this.date.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #description} (A free text natural language description of the StructureMap and its use.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public StringType getDescriptionElement() { 
      if (this.description == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create StructureMap.description");
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
     * @param value {@link #description} (A free text natural language description of the StructureMap and its use.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public StructureMap setDescriptionElement(StringType value) { 
      this.description = value;
      return this;
    }

    /**
     * @return A free text natural language description of the StructureMap and its use.
     */
    public String getDescription() { 
      return this.description == null ? null : this.description.getValue();
    }

    /**
     * @param value A free text natural language description of the StructureMap and its use.
     */
    public StructureMap setDescription(String value) { 
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
     * @return {@link #useContext} (The content was developed with a focus and intent of supporting the contexts that are listed. These terms may be used to assist with indexing and searching of structure maps.)
     */
    public List<CodeableConcept> getUseContext() { 
      if (this.useContext == null)
        this.useContext = new ArrayList<CodeableConcept>();
      return this.useContext;
    }

    public boolean hasUseContext() { 
      if (this.useContext == null)
        return false;
      for (CodeableConcept item : this.useContext)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #useContext} (The content was developed with a focus and intent of supporting the contexts that are listed. These terms may be used to assist with indexing and searching of structure maps.)
     */
    // syntactic sugar
    public CodeableConcept addUseContext() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.useContext == null)
        this.useContext = new ArrayList<CodeableConcept>();
      this.useContext.add(t);
      return t;
    }

    // syntactic sugar
    public StructureMap addUseContext(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.useContext == null)
        this.useContext = new ArrayList<CodeableConcept>();
      this.useContext.add(t);
      return this;
    }

    /**
     * @return {@link #requirements} (Explains why this structure map is needed and why it's been designed as it has.). This is the underlying object with id, value and extensions. The accessor "getRequirements" gives direct access to the value
     */
    public StringType getRequirementsElement() { 
      if (this.requirements == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create StructureMap.requirements");
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
     * @param value {@link #requirements} (Explains why this structure map is needed and why it's been designed as it has.). This is the underlying object with id, value and extensions. The accessor "getRequirements" gives direct access to the value
     */
    public StructureMap setRequirementsElement(StringType value) { 
      this.requirements = value;
      return this;
    }

    /**
     * @return Explains why this structure map is needed and why it's been designed as it has.
     */
    public String getRequirements() { 
      return this.requirements == null ? null : this.requirements.getValue();
    }

    /**
     * @param value Explains why this structure map is needed and why it's been designed as it has.
     */
    public StructureMap setRequirements(String value) { 
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
     * @return {@link #copyright} (A copyright statement relating to the structure map and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the details of the constraints and mappings.). This is the underlying object with id, value and extensions. The accessor "getCopyright" gives direct access to the value
     */
    public StringType getCopyrightElement() { 
      if (this.copyright == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create StructureMap.copyright");
        else if (Configuration.doAutoCreate())
          this.copyright = new StringType(); // bb
      return this.copyright;
    }

    public boolean hasCopyrightElement() { 
      return this.copyright != null && !this.copyright.isEmpty();
    }

    public boolean hasCopyright() { 
      return this.copyright != null && !this.copyright.isEmpty();
    }

    /**
     * @param value {@link #copyright} (A copyright statement relating to the structure map and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the details of the constraints and mappings.). This is the underlying object with id, value and extensions. The accessor "getCopyright" gives direct access to the value
     */
    public StructureMap setCopyrightElement(StringType value) { 
      this.copyright = value;
      return this;
    }

    /**
     * @return A copyright statement relating to the structure map and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the details of the constraints and mappings.
     */
    public String getCopyright() { 
      return this.copyright == null ? null : this.copyright.getValue();
    }

    /**
     * @param value A copyright statement relating to the structure map and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the details of the constraints and mappings.
     */
    public StructureMap setCopyright(String value) { 
      if (Utilities.noString(value))
        this.copyright = null;
      else {
        if (this.copyright == null)
          this.copyright = new StringType();
        this.copyright.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #structure} (A structure definition used by this map. The structure definition may describe instances that are converted, or the instances that are produced.)
     */
    public List<StructureMapStructureComponent> getStructure() { 
      if (this.structure == null)
        this.structure = new ArrayList<StructureMapStructureComponent>();
      return this.structure;
    }

    public boolean hasStructure() { 
      if (this.structure == null)
        return false;
      for (StructureMapStructureComponent item : this.structure)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #structure} (A structure definition used by this map. The structure definition may describe instances that are converted, or the instances that are produced.)
     */
    // syntactic sugar
    public StructureMapStructureComponent addStructure() { //3
      StructureMapStructureComponent t = new StructureMapStructureComponent();
      if (this.structure == null)
        this.structure = new ArrayList<StructureMapStructureComponent>();
      this.structure.add(t);
      return t;
    }

    // syntactic sugar
    public StructureMap addStructure(StructureMapStructureComponent t) { //3
      if (t == null)
        return this;
      if (this.structure == null)
        this.structure = new ArrayList<StructureMapStructureComponent>();
      this.structure.add(t);
      return this;
    }

    /**
     * @return {@link #import_} (Other maps used by this map (canonical URLs).)
     */
    public List<UriType> getImport() { 
      if (this.import_ == null)
        this.import_ = new ArrayList<UriType>();
      return this.import_;
    }

    public boolean hasImport() { 
      if (this.import_ == null)
        return false;
      for (UriType item : this.import_)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #import_} (Other maps used by this map (canonical URLs).)
     */
    // syntactic sugar
    public UriType addImportElement() {//2 
      UriType t = new UriType();
      if (this.import_ == null)
        this.import_ = new ArrayList<UriType>();
      this.import_.add(t);
      return t;
    }

    /**
     * @param value {@link #import_} (Other maps used by this map (canonical URLs).)
     */
    public StructureMap addImport(String value) { //1
      UriType t = new UriType();
      t.setValue(value);
      if (this.import_ == null)
        this.import_ = new ArrayList<UriType>();
      this.import_.add(t);
      return this;
    }

    /**
     * @param value {@link #import_} (Other maps used by this map (canonical URLs).)
     */
    public boolean hasImport(String value) { 
      if (this.import_ == null)
        return false;
      for (UriType v : this.import_)
        if (v.equals(value)) // uri
          return true;
      return false;
    }

    /**
     * @return {@link #group} (Named sections for reader convenience.)
     */
    public List<StructureMapGroupComponent> getGroup() { 
      if (this.group == null)
        this.group = new ArrayList<StructureMapGroupComponent>();
      return this.group;
    }

    public boolean hasGroup() { 
      if (this.group == null)
        return false;
      for (StructureMapGroupComponent item : this.group)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #group} (Named sections for reader convenience.)
     */
    // syntactic sugar
    public StructureMapGroupComponent addGroup() { //3
      StructureMapGroupComponent t = new StructureMapGroupComponent();
      if (this.group == null)
        this.group = new ArrayList<StructureMapGroupComponent>();
      this.group.add(t);
      return t;
    }

    // syntactic sugar
    public StructureMap addGroup(StructureMapGroupComponent t) { //3
      if (t == null)
        return this;
      if (this.group == null)
        this.group = new ArrayList<StructureMapGroupComponent>();
      this.group.add(t);
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("url", "uri", "An absolute URL that is used to identify this structure map when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this structure map is (or will be) published.", 0, java.lang.Integer.MAX_VALUE, url));
        childrenList.add(new Property("identifier", "Identifier", "Formal identifier that is used to identify this StructureMap when it is represented in other formats, or referenced in a specification, model, design or an instance  (should be globally unique OID, UUID, or URI), (if it's not possible to use the literal URI).", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("version", "string", "The identifier that is used to identify this version of the StructureMap when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the StructureMap author manually.", 0, java.lang.Integer.MAX_VALUE, version));
        childrenList.add(new Property("name", "string", "A free text natural language name identifying the StructureMap.", 0, java.lang.Integer.MAX_VALUE, name));
        childrenList.add(new Property("status", "code", "The status of the StructureMap.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("experimental", "boolean", "This StructureMap was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.", 0, java.lang.Integer.MAX_VALUE, experimental));
        childrenList.add(new Property("publisher", "string", "The name of the individual or organization that published the structure map.", 0, java.lang.Integer.MAX_VALUE, publisher));
        childrenList.add(new Property("contact", "", "Contacts to assist a user in finding and communicating with the publisher.", 0, java.lang.Integer.MAX_VALUE, contact));
        childrenList.add(new Property("date", "dateTime", "The date this version of the structure map was published. The date must change when the business version changes, if it does, and it must change if the status code changes. In addition, it should change when the substantive content of the structure map changes.", 0, java.lang.Integer.MAX_VALUE, date));
        childrenList.add(new Property("description", "string", "A free text natural language description of the StructureMap and its use.", 0, java.lang.Integer.MAX_VALUE, description));
        childrenList.add(new Property("useContext", "CodeableConcept", "The content was developed with a focus and intent of supporting the contexts that are listed. These terms may be used to assist with indexing and searching of structure maps.", 0, java.lang.Integer.MAX_VALUE, useContext));
        childrenList.add(new Property("requirements", "string", "Explains why this structure map is needed and why it's been designed as it has.", 0, java.lang.Integer.MAX_VALUE, requirements));
        childrenList.add(new Property("copyright", "string", "A copyright statement relating to the structure map and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the details of the constraints and mappings.", 0, java.lang.Integer.MAX_VALUE, copyright));
        childrenList.add(new Property("structure", "", "A structure definition used by this map. The structure definition may describe instances that are converted, or the instances that are produced.", 0, java.lang.Integer.MAX_VALUE, structure));
        childrenList.add(new Property("import", "uri", "Other maps used by this map (canonical URLs).", 0, java.lang.Integer.MAX_VALUE, import_));
        childrenList.add(new Property("group", "", "Named sections for reader convenience.", 0, java.lang.Integer.MAX_VALUE, group));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 116079: /*url*/ return this.url == null ? new Base[0] : new Base[] {this.url}; // UriType
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : this.identifier.toArray(new Base[this.identifier.size()]); // Identifier
        case 351608024: /*version*/ return this.version == null ? new Base[0] : new Base[] {this.version}; // StringType
        case 3373707: /*name*/ return this.name == null ? new Base[0] : new Base[] {this.name}; // StringType
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // Enumeration<ConformanceResourceStatus>
        case -404562712: /*experimental*/ return this.experimental == null ? new Base[0] : new Base[] {this.experimental}; // BooleanType
        case 1447404028: /*publisher*/ return this.publisher == null ? new Base[0] : new Base[] {this.publisher}; // StringType
        case 951526432: /*contact*/ return this.contact == null ? new Base[0] : this.contact.toArray(new Base[this.contact.size()]); // StructureMapContactComponent
        case 3076014: /*date*/ return this.date == null ? new Base[0] : new Base[] {this.date}; // DateTimeType
        case -1724546052: /*description*/ return this.description == null ? new Base[0] : new Base[] {this.description}; // StringType
        case -669707736: /*useContext*/ return this.useContext == null ? new Base[0] : this.useContext.toArray(new Base[this.useContext.size()]); // CodeableConcept
        case -1619874672: /*requirements*/ return this.requirements == null ? new Base[0] : new Base[] {this.requirements}; // StringType
        case 1522889671: /*copyright*/ return this.copyright == null ? new Base[0] : new Base[] {this.copyright}; // StringType
        case 144518515: /*structure*/ return this.structure == null ? new Base[0] : this.structure.toArray(new Base[this.structure.size()]); // StructureMapStructureComponent
        case -1184795739: /*import*/ return this.import_ == null ? new Base[0] : this.import_.toArray(new Base[this.import_.size()]); // UriType
        case 98629247: /*group*/ return this.group == null ? new Base[0] : this.group.toArray(new Base[this.group.size()]); // StructureMapGroupComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 116079: // url
          this.url = castToUri(value); // UriType
          break;
        case -1618432855: // identifier
          this.getIdentifier().add(castToIdentifier(value)); // Identifier
          break;
        case 351608024: // version
          this.version = castToString(value); // StringType
          break;
        case 3373707: // name
          this.name = castToString(value); // StringType
          break;
        case -892481550: // status
          this.status = new ConformanceResourceStatusEnumFactory().fromType(value); // Enumeration<ConformanceResourceStatus>
          break;
        case -404562712: // experimental
          this.experimental = castToBoolean(value); // BooleanType
          break;
        case 1447404028: // publisher
          this.publisher = castToString(value); // StringType
          break;
        case 951526432: // contact
          this.getContact().add((StructureMapContactComponent) value); // StructureMapContactComponent
          break;
        case 3076014: // date
          this.date = castToDateTime(value); // DateTimeType
          break;
        case -1724546052: // description
          this.description = castToString(value); // StringType
          break;
        case -669707736: // useContext
          this.getUseContext().add(castToCodeableConcept(value)); // CodeableConcept
          break;
        case -1619874672: // requirements
          this.requirements = castToString(value); // StringType
          break;
        case 1522889671: // copyright
          this.copyright = castToString(value); // StringType
          break;
        case 144518515: // structure
          this.getStructure().add((StructureMapStructureComponent) value); // StructureMapStructureComponent
          break;
        case -1184795739: // import
          this.getImport().add(castToUri(value)); // UriType
          break;
        case 98629247: // group
          this.getGroup().add((StructureMapGroupComponent) value); // StructureMapGroupComponent
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("url"))
          this.url = castToUri(value); // UriType
        else if (name.equals("identifier"))
          this.getIdentifier().add(castToIdentifier(value));
        else if (name.equals("version"))
          this.version = castToString(value); // StringType
        else if (name.equals("name"))
          this.name = castToString(value); // StringType
        else if (name.equals("status"))
          this.status = new ConformanceResourceStatusEnumFactory().fromType(value); // Enumeration<ConformanceResourceStatus>
        else if (name.equals("experimental"))
          this.experimental = castToBoolean(value); // BooleanType
        else if (name.equals("publisher"))
          this.publisher = castToString(value); // StringType
        else if (name.equals("contact"))
          this.getContact().add((StructureMapContactComponent) value);
        else if (name.equals("date"))
          this.date = castToDateTime(value); // DateTimeType
        else if (name.equals("description"))
          this.description = castToString(value); // StringType
        else if (name.equals("useContext"))
          this.getUseContext().add(castToCodeableConcept(value));
        else if (name.equals("requirements"))
          this.requirements = castToString(value); // StringType
        else if (name.equals("copyright"))
          this.copyright = castToString(value); // StringType
        else if (name.equals("structure"))
          this.getStructure().add((StructureMapStructureComponent) value);
        else if (name.equals("import"))
          this.getImport().add(castToUri(value));
        else if (name.equals("group"))
          this.getGroup().add((StructureMapGroupComponent) value);
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 116079: throw new FHIRException("Cannot make property url as it is not a complex type"); // UriType
        case -1618432855:  return addIdentifier(); // Identifier
        case 351608024: throw new FHIRException("Cannot make property version as it is not a complex type"); // StringType
        case 3373707: throw new FHIRException("Cannot make property name as it is not a complex type"); // StringType
        case -892481550: throw new FHIRException("Cannot make property status as it is not a complex type"); // Enumeration<ConformanceResourceStatus>
        case -404562712: throw new FHIRException("Cannot make property experimental as it is not a complex type"); // BooleanType
        case 1447404028: throw new FHIRException("Cannot make property publisher as it is not a complex type"); // StringType
        case 951526432:  return addContact(); // StructureMapContactComponent
        case 3076014: throw new FHIRException("Cannot make property date as it is not a complex type"); // DateTimeType
        case -1724546052: throw new FHIRException("Cannot make property description as it is not a complex type"); // StringType
        case -669707736:  return addUseContext(); // CodeableConcept
        case -1619874672: throw new FHIRException("Cannot make property requirements as it is not a complex type"); // StringType
        case 1522889671: throw new FHIRException("Cannot make property copyright as it is not a complex type"); // StringType
        case 144518515:  return addStructure(); // StructureMapStructureComponent
        case -1184795739: throw new FHIRException("Cannot make property import as it is not a complex type"); // UriType
        case 98629247:  return addGroup(); // StructureMapGroupComponent
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("url")) {
          throw new FHIRException("Cannot call addChild on a primitive type StructureMap.url");
        }
        else if (name.equals("identifier")) {
          return addIdentifier();
        }
        else if (name.equals("version")) {
          throw new FHIRException("Cannot call addChild on a primitive type StructureMap.version");
        }
        else if (name.equals("name")) {
          throw new FHIRException("Cannot call addChild on a primitive type StructureMap.name");
        }
        else if (name.equals("status")) {
          throw new FHIRException("Cannot call addChild on a primitive type StructureMap.status");
        }
        else if (name.equals("experimental")) {
          throw new FHIRException("Cannot call addChild on a primitive type StructureMap.experimental");
        }
        else if (name.equals("publisher")) {
          throw new FHIRException("Cannot call addChild on a primitive type StructureMap.publisher");
        }
        else if (name.equals("contact")) {
          return addContact();
        }
        else if (name.equals("date")) {
          throw new FHIRException("Cannot call addChild on a primitive type StructureMap.date");
        }
        else if (name.equals("description")) {
          throw new FHIRException("Cannot call addChild on a primitive type StructureMap.description");
        }
        else if (name.equals("useContext")) {
          return addUseContext();
        }
        else if (name.equals("requirements")) {
          throw new FHIRException("Cannot call addChild on a primitive type StructureMap.requirements");
        }
        else if (name.equals("copyright")) {
          throw new FHIRException("Cannot call addChild on a primitive type StructureMap.copyright");
        }
        else if (name.equals("structure")) {
          return addStructure();
        }
        else if (name.equals("import")) {
          throw new FHIRException("Cannot call addChild on a primitive type StructureMap.import");
        }
        else if (name.equals("group")) {
          return addGroup();
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "StructureMap";

  }

      public StructureMap copy() {
        StructureMap dst = new StructureMap();
        copyValues(dst);
        dst.url = url == null ? null : url.copy();
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        dst.version = version == null ? null : version.copy();
        dst.name = name == null ? null : name.copy();
        dst.status = status == null ? null : status.copy();
        dst.experimental = experimental == null ? null : experimental.copy();
        dst.publisher = publisher == null ? null : publisher.copy();
        if (contact != null) {
          dst.contact = new ArrayList<StructureMapContactComponent>();
          for (StructureMapContactComponent i : contact)
            dst.contact.add(i.copy());
        };
        dst.date = date == null ? null : date.copy();
        dst.description = description == null ? null : description.copy();
        if (useContext != null) {
          dst.useContext = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : useContext)
            dst.useContext.add(i.copy());
        };
        dst.requirements = requirements == null ? null : requirements.copy();
        dst.copyright = copyright == null ? null : copyright.copy();
        if (structure != null) {
          dst.structure = new ArrayList<StructureMapStructureComponent>();
          for (StructureMapStructureComponent i : structure)
            dst.structure.add(i.copy());
        };
        if (import_ != null) {
          dst.import_ = new ArrayList<UriType>();
          for (UriType i : import_)
            dst.import_.add(i.copy());
        };
        if (group != null) {
          dst.group = new ArrayList<StructureMapGroupComponent>();
          for (StructureMapGroupComponent i : group)
            dst.group.add(i.copy());
        };
        return dst;
      }

      protected StructureMap typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof StructureMap))
          return false;
        StructureMap o = (StructureMap) other;
        return compareDeep(url, o.url, true) && compareDeep(identifier, o.identifier, true) && compareDeep(version, o.version, true)
           && compareDeep(name, o.name, true) && compareDeep(status, o.status, true) && compareDeep(experimental, o.experimental, true)
           && compareDeep(publisher, o.publisher, true) && compareDeep(contact, o.contact, true) && compareDeep(date, o.date, true)
           && compareDeep(description, o.description, true) && compareDeep(useContext, o.useContext, true)
           && compareDeep(requirements, o.requirements, true) && compareDeep(copyright, o.copyright, true)
           && compareDeep(structure, o.structure, true) && compareDeep(import_, o.import_, true) && compareDeep(group, o.group, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof StructureMap))
          return false;
        StructureMap o = (StructureMap) other;
        return compareValues(url, o.url, true) && compareValues(version, o.version, true) && compareValues(name, o.name, true)
           && compareValues(status, o.status, true) && compareValues(experimental, o.experimental, true) && compareValues(publisher, o.publisher, true)
           && compareValues(date, o.date, true) && compareValues(description, o.description, true) && compareValues(requirements, o.requirements, true)
           && compareValues(copyright, o.copyright, true) && compareValues(import_, o.import_, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (url == null || url.isEmpty()) && (identifier == null || identifier.isEmpty())
           && (version == null || version.isEmpty()) && (name == null || name.isEmpty()) && (status == null || status.isEmpty())
           && (experimental == null || experimental.isEmpty()) && (publisher == null || publisher.isEmpty())
           && (contact == null || contact.isEmpty()) && (date == null || date.isEmpty()) && (description == null || description.isEmpty())
           && (useContext == null || useContext.isEmpty()) && (requirements == null || requirements.isEmpty())
           && (copyright == null || copyright.isEmpty()) && (structure == null || structure.isEmpty())
           && (import_ == null || import_.isEmpty()) && (group == null || group.isEmpty());
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.StructureMap;
   }

 /**
   * Search parameter: <b>status</b>
   * <p>
   * Description: <b>The current status of the profile</b><br>
   * Type: <b>token</b><br>
   * Path: <b>StructureMap.status</b><br>
   * </p>
   */
  @SearchParamDefinition(name="status", path="StructureMap.status", description="The current status of the profile", type="token" )
  public static final String SP_STATUS = "status";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>status</b>
   * <p>
   * Description: <b>The current status of the profile</b><br>
   * Type: <b>token</b><br>
   * Path: <b>StructureMap.status</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam STATUS = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_STATUS);

 /**
   * Search parameter: <b>description</b>
   * <p>
   * Description: <b>Text search in the description of the profile</b><br>
   * Type: <b>string</b><br>
   * Path: <b>StructureMap.description</b><br>
   * </p>
   */
  @SearchParamDefinition(name="description", path="StructureMap.description", description="Text search in the description of the profile", type="string" )
  public static final String SP_DESCRIPTION = "description";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>description</b>
   * <p>
   * Description: <b>Text search in the description of the profile</b><br>
   * Type: <b>string</b><br>
   * Path: <b>StructureMap.description</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam DESCRIPTION = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_DESCRIPTION);

 /**
   * Search parameter: <b>name</b>
   * <p>
   * Description: <b>Name of the profile</b><br>
   * Type: <b>string</b><br>
   * Path: <b>StructureMap.name</b><br>
   * </p>
   */
  @SearchParamDefinition(name="name", path="StructureMap.name", description="Name of the profile", type="string" )
  public static final String SP_NAME = "name";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>name</b>
   * <p>
   * Description: <b>Name of the profile</b><br>
   * Type: <b>string</b><br>
   * Path: <b>StructureMap.name</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam NAME = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_NAME);

 /**
   * Search parameter: <b>context</b>
   * <p>
   * Description: <b>A use context assigned to the structure</b><br>
   * Type: <b>token</b><br>
   * Path: <b>StructureMap.useContext</b><br>
   * </p>
   */
  @SearchParamDefinition(name="context", path="StructureMap.useContext", description="A use context assigned to the structure", type="token" )
  public static final String SP_CONTEXT = "context";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>context</b>
   * <p>
   * Description: <b>A use context assigned to the structure</b><br>
   * Type: <b>token</b><br>
   * Path: <b>StructureMap.useContext</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam CONTEXT = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_CONTEXT);

 /**
   * Search parameter: <b>experimental</b>
   * <p>
   * Description: <b>Whether the map is defined purely for experimental reasons</b><br>
   * Type: <b>token</b><br>
   * Path: <b>StructureMap.experimental</b><br>
   * </p>
   */
  @SearchParamDefinition(name="experimental", path="StructureMap.experimental", description="Whether the map is defined purely for experimental reasons", type="token" )
  public static final String SP_EXPERIMENTAL = "experimental";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>experimental</b>
   * <p>
   * Description: <b>Whether the map is defined purely for experimental reasons</b><br>
   * Type: <b>token</b><br>
   * Path: <b>StructureMap.experimental</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam EXPERIMENTAL = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_EXPERIMENTAL);

 /**
   * Search parameter: <b>date</b>
   * <p>
   * Description: <b>The profile publication date</b><br>
   * Type: <b>date</b><br>
   * Path: <b>StructureMap.date</b><br>
   * </p>
   */
  @SearchParamDefinition(name="date", path="StructureMap.date", description="The profile publication date", type="date" )
  public static final String SP_DATE = "date";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>date</b>
   * <p>
   * Description: <b>The profile publication date</b><br>
   * Type: <b>date</b><br>
   * Path: <b>StructureMap.date</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam DATE = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_DATE);

 /**
   * Search parameter: <b>identifier</b>
   * <p>
   * Description: <b>The identifier of the profile</b><br>
   * Type: <b>token</b><br>
   * Path: <b>StructureMap.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="StructureMap.identifier", description="The identifier of the profile", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>The identifier of the profile</b><br>
   * Type: <b>token</b><br>
   * Path: <b>StructureMap.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_IDENTIFIER);

 /**
   * Search parameter: <b>url</b>
   * <p>
   * Description: <b>The url that identifies the structure map</b><br>
   * Type: <b>uri</b><br>
   * Path: <b>StructureMap.url</b><br>
   * </p>
   */
  @SearchParamDefinition(name="url", path="StructureMap.url", description="The url that identifies the structure map", type="uri" )
  public static final String SP_URL = "url";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>url</b>
   * <p>
   * Description: <b>The url that identifies the structure map</b><br>
   * Type: <b>uri</b><br>
   * Path: <b>StructureMap.url</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.UriClientParam URL = new ca.uhn.fhir.rest.gclient.UriClientParam(SP_URL);

 /**
   * Search parameter: <b>publisher</b>
   * <p>
   * Description: <b>Name of the publisher of the profile</b><br>
   * Type: <b>string</b><br>
   * Path: <b>StructureMap.publisher</b><br>
   * </p>
   */
  @SearchParamDefinition(name="publisher", path="StructureMap.publisher", description="Name of the publisher of the profile", type="string" )
  public static final String SP_PUBLISHER = "publisher";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>publisher</b>
   * <p>
   * Description: <b>Name of the publisher of the profile</b><br>
   * Type: <b>string</b><br>
   * Path: <b>StructureMap.publisher</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam PUBLISHER = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_PUBLISHER);

 /**
   * Search parameter: <b>version</b>
   * <p>
   * Description: <b>The version identifier of the profile</b><br>
   * Type: <b>token</b><br>
   * Path: <b>StructureMap.version</b><br>
   * </p>
   */
  @SearchParamDefinition(name="version", path="StructureMap.version", description="The version identifier of the profile", type="token" )
  public static final String SP_VERSION = "version";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>version</b>
   * <p>
   * Description: <b>The version identifier of the profile</b><br>
   * Type: <b>token</b><br>
   * Path: <b>StructureMap.version</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam VERSION = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_VERSION);


}

