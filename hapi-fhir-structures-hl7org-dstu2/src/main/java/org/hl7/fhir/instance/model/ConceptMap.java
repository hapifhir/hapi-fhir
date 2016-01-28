package org.hl7.fhir.instance.model;

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

// Generated on Wed, Nov 11, 2015 10:54-0500 for FHIR v1.0.2
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hl7.fhir.instance.model.Enumerations.ConceptMapEquivalence;
import org.hl7.fhir.instance.model.Enumerations.ConceptMapEquivalenceEnumFactory;
import org.hl7.fhir.instance.model.Enumerations.ConformanceResourceStatus;
import org.hl7.fhir.instance.model.Enumerations.ConformanceResourceStatusEnumFactory;
import org.hl7.fhir.instance.model.api.IBaseBackboneElement;
import org.hl7.fhir.instance.utilities.Utilities;

import ca.uhn.fhir.model.api.annotation.Block;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
/**
 * A statement of relationships from one set of concepts to one or more other concepts - either code systems or data elements, or classes in class models.
 */
@ResourceDef(name="ConceptMap", profile="http://hl7.org/fhir/Profile/ConceptMap")
public class ConceptMap extends DomainResource {

    @Block()
    public static class ConceptMapContactComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The name of an individual to contact regarding the concept map.
         */
        @Child(name = "name", type = {StringType.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Name of a individual to contact", formalDefinition="The name of an individual to contact regarding the concept map." )
        protected StringType name;

        /**
         * Contact details for individual (if a name was provided) or the publisher.
         */
        @Child(name = "telecom", type = {ContactPoint.class}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Contact details for individual or publisher", formalDefinition="Contact details for individual (if a name was provided) or the publisher." )
        protected List<ContactPoint> telecom;

        private static final long serialVersionUID = -1179697803L;

    /*
     * Constructor
     */
      public ConceptMapContactComponent() {
        super();
      }

        /**
         * @return {@link #name} (The name of an individual to contact regarding the concept map.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public StringType getNameElement() { 
          if (this.name == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ConceptMapContactComponent.name");
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
         * @param value {@link #name} (The name of an individual to contact regarding the concept map.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public ConceptMapContactComponent setNameElement(StringType value) { 
          this.name = value;
          return this;
        }

        /**
         * @return The name of an individual to contact regarding the concept map.
         */
        public String getName() { 
          return this.name == null ? null : this.name.getValue();
        }

        /**
         * @param value The name of an individual to contact regarding the concept map.
         */
        public ConceptMapContactComponent setName(String value) { 
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
        public ConceptMapContactComponent addTelecom(ContactPoint t) { //3
          if (t == null)
            return this;
          if (this.telecom == null)
            this.telecom = new ArrayList<ContactPoint>();
          this.telecom.add(t);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("name", "string", "The name of an individual to contact regarding the concept map.", 0, java.lang.Integer.MAX_VALUE, name));
          childrenList.add(new Property("telecom", "ContactPoint", "Contact details for individual (if a name was provided) or the publisher.", 0, java.lang.Integer.MAX_VALUE, telecom));
        }

      public ConceptMapContactComponent copy() {
        ConceptMapContactComponent dst = new ConceptMapContactComponent();
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
        if (!(other instanceof ConceptMapContactComponent))
          return false;
        ConceptMapContactComponent o = (ConceptMapContactComponent) other;
        return compareDeep(name, o.name, true) && compareDeep(telecom, o.telecom, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ConceptMapContactComponent))
          return false;
        ConceptMapContactComponent o = (ConceptMapContactComponent) other;
        return compareValues(name, o.name, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (name == null || name.isEmpty()) && (telecom == null || telecom.isEmpty())
          ;
      }

  }

    @Block()
    public static class SourceElementComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * An absolute URI that identifies the Code System (if the source is a value set that crosses more than one code system).
         */
        @Child(name = "codeSystem", type = {UriType.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Code System (if value set crosses code systems)", formalDefinition="An absolute URI that identifies the Code System (if the source is a value set that crosses more than one code system)." )
        protected UriType codeSystem;

        /**
         * Identity (code or path) or the element/item being mapped.
         */
        @Child(name = "code", type = {CodeType.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Identifies element being mapped", formalDefinition="Identity (code or path) or the element/item being mapped." )
        protected CodeType code;

        /**
         * A concept from the target value set that this concept maps to.
         */
        @Child(name = "target", type = {}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Concept in target system for element", formalDefinition="A concept from the target value set that this concept maps to." )
        protected List<TargetElementComponent> target;

        private static final long serialVersionUID = -458143877L;

    /*
     * Constructor
     */
      public SourceElementComponent() {
        super();
      }

        /**
         * @return {@link #codeSystem} (An absolute URI that identifies the Code System (if the source is a value set that crosses more than one code system).). This is the underlying object with id, value and extensions. The accessor "getCodeSystem" gives direct access to the value
         */
        public UriType getCodeSystemElement() { 
          if (this.codeSystem == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SourceElementComponent.codeSystem");
            else if (Configuration.doAutoCreate())
              this.codeSystem = new UriType(); // bb
          return this.codeSystem;
        }

        public boolean hasCodeSystemElement() { 
          return this.codeSystem != null && !this.codeSystem.isEmpty();
        }

        public boolean hasCodeSystem() { 
          return this.codeSystem != null && !this.codeSystem.isEmpty();
        }

        /**
         * @param value {@link #codeSystem} (An absolute URI that identifies the Code System (if the source is a value set that crosses more than one code system).). This is the underlying object with id, value and extensions. The accessor "getCodeSystem" gives direct access to the value
         */
        public SourceElementComponent setCodeSystemElement(UriType value) { 
          this.codeSystem = value;
          return this;
        }

        /**
         * @return An absolute URI that identifies the Code System (if the source is a value set that crosses more than one code system).
         */
        public String getCodeSystem() { 
          return this.codeSystem == null ? null : this.codeSystem.getValue();
        }

        /**
         * @param value An absolute URI that identifies the Code System (if the source is a value set that crosses more than one code system).
         */
        public SourceElementComponent setCodeSystem(String value) { 
          if (Utilities.noString(value))
            this.codeSystem = null;
          else {
            if (this.codeSystem == null)
              this.codeSystem = new UriType();
            this.codeSystem.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #code} (Identity (code or path) or the element/item being mapped.). This is the underlying object with id, value and extensions. The accessor "getCode" gives direct access to the value
         */
        public CodeType getCodeElement() { 
          if (this.code == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SourceElementComponent.code");
            else if (Configuration.doAutoCreate())
              this.code = new CodeType(); // bb
          return this.code;
        }

        public boolean hasCodeElement() { 
          return this.code != null && !this.code.isEmpty();
        }

        public boolean hasCode() { 
          return this.code != null && !this.code.isEmpty();
        }

        /**
         * @param value {@link #code} (Identity (code or path) or the element/item being mapped.). This is the underlying object with id, value and extensions. The accessor "getCode" gives direct access to the value
         */
        public SourceElementComponent setCodeElement(CodeType value) { 
          this.code = value;
          return this;
        }

        /**
         * @return Identity (code or path) or the element/item being mapped.
         */
        public String getCode() { 
          return this.code == null ? null : this.code.getValue();
        }

        /**
         * @param value Identity (code or path) or the element/item being mapped.
         */
        public SourceElementComponent setCode(String value) { 
          if (Utilities.noString(value))
            this.code = null;
          else {
            if (this.code == null)
              this.code = new CodeType();
            this.code.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #target} (A concept from the target value set that this concept maps to.)
         */
        public List<TargetElementComponent> getTarget() { 
          if (this.target == null)
            this.target = new ArrayList<TargetElementComponent>();
          return this.target;
        }

        public boolean hasTarget() { 
          if (this.target == null)
            return false;
          for (TargetElementComponent item : this.target)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #target} (A concept from the target value set that this concept maps to.)
         */
    // syntactic sugar
        public TargetElementComponent addTarget() { //3
          TargetElementComponent t = new TargetElementComponent();
          if (this.target == null)
            this.target = new ArrayList<TargetElementComponent>();
          this.target.add(t);
          return t;
        }

    // syntactic sugar
        public SourceElementComponent addTarget(TargetElementComponent t) { //3
          if (t == null)
            return this;
          if (this.target == null)
            this.target = new ArrayList<TargetElementComponent>();
          this.target.add(t);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("codeSystem", "uri", "An absolute URI that identifies the Code System (if the source is a value set that crosses more than one code system).", 0, java.lang.Integer.MAX_VALUE, codeSystem));
          childrenList.add(new Property("code", "code", "Identity (code or path) or the element/item being mapped.", 0, java.lang.Integer.MAX_VALUE, code));
          childrenList.add(new Property("target", "", "A concept from the target value set that this concept maps to.", 0, java.lang.Integer.MAX_VALUE, target));
        }

      public SourceElementComponent copy() {
        SourceElementComponent dst = new SourceElementComponent();
        copyValues(dst);
        dst.codeSystem = codeSystem == null ? null : codeSystem.copy();
        dst.code = code == null ? null : code.copy();
        if (target != null) {
          dst.target = new ArrayList<TargetElementComponent>();
          for (TargetElementComponent i : target)
            dst.target.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof SourceElementComponent))
          return false;
        SourceElementComponent o = (SourceElementComponent) other;
        return compareDeep(codeSystem, o.codeSystem, true) && compareDeep(code, o.code, true) && compareDeep(target, o.target, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof SourceElementComponent))
          return false;
        SourceElementComponent o = (SourceElementComponent) other;
        return compareValues(codeSystem, o.codeSystem, true) && compareValues(code, o.code, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (codeSystem == null || codeSystem.isEmpty()) && (code == null || code.isEmpty())
           && (target == null || target.isEmpty());
      }

  }

    @Block()
    public static class TargetElementComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * An absolute URI that identifies the code system of the target code (if the target is a value set that cross code systems).
         */
        @Child(name = "codeSystem", type = {UriType.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="System of the target (if necessary)", formalDefinition="An absolute URI that identifies the code system of the target code (if the target is a value set that cross code systems)." )
        protected UriType codeSystem;

        /**
         * Identity (code or path) or the element/item that the map refers to.
         */
        @Child(name = "code", type = {CodeType.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Code that identifies the target element", formalDefinition="Identity (code or path) or the element/item that the map refers to." )
        protected CodeType code;

        /**
         * The equivalence between the source and target concepts (counting for the dependencies and products). The equivalence is read from target to source (e.g. the target is 'wider' than the source).
         */
        @Child(name = "equivalence", type = {CodeType.class}, order=3, min=1, max=1, modifier=true, summary=false)
        @Description(shortDefinition="equivalent | equal | wider | subsumes | narrower | specializes | inexact | unmatched | disjoint", formalDefinition="The equivalence between the source and target concepts (counting for the dependencies and products). The equivalence is read from target to source (e.g. the target is 'wider' than the source)." )
        protected Enumeration<ConceptMapEquivalence> equivalence;

        /**
         * A description of status/issues in mapping that conveys additional information not represented in  the structured data.
         */
        @Child(name = "comments", type = {StringType.class}, order=4, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Description of status/issues in mapping", formalDefinition="A description of status/issues in mapping that conveys additional information not represented in  the structured data." )
        protected StringType comments;

        /**
         * A set of additional dependencies for this mapping to hold. This mapping is only applicable if the specified element can be resolved, and it has the specified value.
         */
        @Child(name = "dependsOn", type = {}, order=5, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Other elements required for this mapping (from context)", formalDefinition="A set of additional dependencies for this mapping to hold. This mapping is only applicable if the specified element can be resolved, and it has the specified value." )
        protected List<OtherElementComponent> dependsOn;

        /**
         * A set of additional outcomes from this mapping to other elements. To properly execute this mapping, the specified element must be mapped to some data element or source that is in context. The mapping may still be useful without a place for the additional data elements, but the equivalence cannot be relied on.
         */
        @Child(name = "product", type = {OtherElementComponent.class}, order=6, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Other concepts that this mapping also produces", formalDefinition="A set of additional outcomes from this mapping to other elements. To properly execute this mapping, the specified element must be mapped to some data element or source that is in context. The mapping may still be useful without a place for the additional data elements, but the equivalence cannot be relied on." )
        protected List<OtherElementComponent> product;

        private static final long serialVersionUID = -804990059L;

    /*
     * Constructor
     */
      public TargetElementComponent() {
        super();
      }

    /*
     * Constructor
     */
      public TargetElementComponent(Enumeration<ConceptMapEquivalence> equivalence) {
        super();
        this.equivalence = equivalence;
      }

        /**
         * @return {@link #codeSystem} (An absolute URI that identifies the code system of the target code (if the target is a value set that cross code systems).). This is the underlying object with id, value and extensions. The accessor "getCodeSystem" gives direct access to the value
         */
        public UriType getCodeSystemElement() { 
          if (this.codeSystem == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TargetElementComponent.codeSystem");
            else if (Configuration.doAutoCreate())
              this.codeSystem = new UriType(); // bb
          return this.codeSystem;
        }

        public boolean hasCodeSystemElement() { 
          return this.codeSystem != null && !this.codeSystem.isEmpty();
        }

        public boolean hasCodeSystem() { 
          return this.codeSystem != null && !this.codeSystem.isEmpty();
        }

        /**
         * @param value {@link #codeSystem} (An absolute URI that identifies the code system of the target code (if the target is a value set that cross code systems).). This is the underlying object with id, value and extensions. The accessor "getCodeSystem" gives direct access to the value
         */
        public TargetElementComponent setCodeSystemElement(UriType value) { 
          this.codeSystem = value;
          return this;
        }

        /**
         * @return An absolute URI that identifies the code system of the target code (if the target is a value set that cross code systems).
         */
        public String getCodeSystem() { 
          return this.codeSystem == null ? null : this.codeSystem.getValue();
        }

        /**
         * @param value An absolute URI that identifies the code system of the target code (if the target is a value set that cross code systems).
         */
        public TargetElementComponent setCodeSystem(String value) { 
          if (Utilities.noString(value))
            this.codeSystem = null;
          else {
            if (this.codeSystem == null)
              this.codeSystem = new UriType();
            this.codeSystem.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #code} (Identity (code or path) or the element/item that the map refers to.). This is the underlying object with id, value and extensions. The accessor "getCode" gives direct access to the value
         */
        public CodeType getCodeElement() { 
          if (this.code == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TargetElementComponent.code");
            else if (Configuration.doAutoCreate())
              this.code = new CodeType(); // bb
          return this.code;
        }

        public boolean hasCodeElement() { 
          return this.code != null && !this.code.isEmpty();
        }

        public boolean hasCode() { 
          return this.code != null && !this.code.isEmpty();
        }

        /**
         * @param value {@link #code} (Identity (code or path) or the element/item that the map refers to.). This is the underlying object with id, value and extensions. The accessor "getCode" gives direct access to the value
         */
        public TargetElementComponent setCodeElement(CodeType value) { 
          this.code = value;
          return this;
        }

        /**
         * @return Identity (code or path) or the element/item that the map refers to.
         */
        public String getCode() { 
          return this.code == null ? null : this.code.getValue();
        }

        /**
         * @param value Identity (code or path) or the element/item that the map refers to.
         */
        public TargetElementComponent setCode(String value) { 
          if (Utilities.noString(value))
            this.code = null;
          else {
            if (this.code == null)
              this.code = new CodeType();
            this.code.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #equivalence} (The equivalence between the source and target concepts (counting for the dependencies and products). The equivalence is read from target to source (e.g. the target is 'wider' than the source).). This is the underlying object with id, value and extensions. The accessor "getEquivalence" gives direct access to the value
         */
        public Enumeration<ConceptMapEquivalence> getEquivalenceElement() { 
          if (this.equivalence == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TargetElementComponent.equivalence");
            else if (Configuration.doAutoCreate())
              this.equivalence = new Enumeration<ConceptMapEquivalence>(new ConceptMapEquivalenceEnumFactory()); // bb
          return this.equivalence;
        }

        public boolean hasEquivalenceElement() { 
          return this.equivalence != null && !this.equivalence.isEmpty();
        }

        public boolean hasEquivalence() { 
          return this.equivalence != null && !this.equivalence.isEmpty();
        }

        /**
         * @param value {@link #equivalence} (The equivalence between the source and target concepts (counting for the dependencies and products). The equivalence is read from target to source (e.g. the target is 'wider' than the source).). This is the underlying object with id, value and extensions. The accessor "getEquivalence" gives direct access to the value
         */
        public TargetElementComponent setEquivalenceElement(Enumeration<ConceptMapEquivalence> value) { 
          this.equivalence = value;
          return this;
        }

        /**
         * @return The equivalence between the source and target concepts (counting for the dependencies and products). The equivalence is read from target to source (e.g. the target is 'wider' than the source).
         */
        public ConceptMapEquivalence getEquivalence() { 
          return this.equivalence == null ? null : this.equivalence.getValue();
        }

        /**
         * @param value The equivalence between the source and target concepts (counting for the dependencies and products). The equivalence is read from target to source (e.g. the target is 'wider' than the source).
         */
        public TargetElementComponent setEquivalence(ConceptMapEquivalence value) { 
            if (this.equivalence == null)
              this.equivalence = new Enumeration<ConceptMapEquivalence>(new ConceptMapEquivalenceEnumFactory());
            this.equivalence.setValue(value);
          return this;
        }

        /**
         * @return {@link #comments} (A description of status/issues in mapping that conveys additional information not represented in  the structured data.). This is the underlying object with id, value and extensions. The accessor "getComments" gives direct access to the value
         */
        public StringType getCommentsElement() { 
          if (this.comments == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TargetElementComponent.comments");
            else if (Configuration.doAutoCreate())
              this.comments = new StringType(); // bb
          return this.comments;
        }

        public boolean hasCommentsElement() { 
          return this.comments != null && !this.comments.isEmpty();
        }

        public boolean hasComments() { 
          return this.comments != null && !this.comments.isEmpty();
        }

        /**
         * @param value {@link #comments} (A description of status/issues in mapping that conveys additional information not represented in  the structured data.). This is the underlying object with id, value and extensions. The accessor "getComments" gives direct access to the value
         */
        public TargetElementComponent setCommentsElement(StringType value) { 
          this.comments = value;
          return this;
        }

        /**
         * @return A description of status/issues in mapping that conveys additional information not represented in  the structured data.
         */
        public String getComments() { 
          return this.comments == null ? null : this.comments.getValue();
        }

        /**
         * @param value A description of status/issues in mapping that conveys additional information not represented in  the structured data.
         */
        public TargetElementComponent setComments(String value) { 
          if (Utilities.noString(value))
            this.comments = null;
          else {
            if (this.comments == null)
              this.comments = new StringType();
            this.comments.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #dependsOn} (A set of additional dependencies for this mapping to hold. This mapping is only applicable if the specified element can be resolved, and it has the specified value.)
         */
        public List<OtherElementComponent> getDependsOn() { 
          if (this.dependsOn == null)
            this.dependsOn = new ArrayList<OtherElementComponent>();
          return this.dependsOn;
        }

        public boolean hasDependsOn() { 
          if (this.dependsOn == null)
            return false;
          for (OtherElementComponent item : this.dependsOn)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #dependsOn} (A set of additional dependencies for this mapping to hold. This mapping is only applicable if the specified element can be resolved, and it has the specified value.)
         */
    // syntactic sugar
        public OtherElementComponent addDependsOn() { //3
          OtherElementComponent t = new OtherElementComponent();
          if (this.dependsOn == null)
            this.dependsOn = new ArrayList<OtherElementComponent>();
          this.dependsOn.add(t);
          return t;
        }

    // syntactic sugar
        public TargetElementComponent addDependsOn(OtherElementComponent t) { //3
          if (t == null)
            return this;
          if (this.dependsOn == null)
            this.dependsOn = new ArrayList<OtherElementComponent>();
          this.dependsOn.add(t);
          return this;
        }

        /**
         * @return {@link #product} (A set of additional outcomes from this mapping to other elements. To properly execute this mapping, the specified element must be mapped to some data element or source that is in context. The mapping may still be useful without a place for the additional data elements, but the equivalence cannot be relied on.)
         */
        public List<OtherElementComponent> getProduct() { 
          if (this.product == null)
            this.product = new ArrayList<OtherElementComponent>();
          return this.product;
        }

        public boolean hasProduct() { 
          if (this.product == null)
            return false;
          for (OtherElementComponent item : this.product)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #product} (A set of additional outcomes from this mapping to other elements. To properly execute this mapping, the specified element must be mapped to some data element or source that is in context. The mapping may still be useful without a place for the additional data elements, but the equivalence cannot be relied on.)
         */
    // syntactic sugar
        public OtherElementComponent addProduct() { //3
          OtherElementComponent t = new OtherElementComponent();
          if (this.product == null)
            this.product = new ArrayList<OtherElementComponent>();
          this.product.add(t);
          return t;
        }

    // syntactic sugar
        public TargetElementComponent addProduct(OtherElementComponent t) { //3
          if (t == null)
            return this;
          if (this.product == null)
            this.product = new ArrayList<OtherElementComponent>();
          this.product.add(t);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("codeSystem", "uri", "An absolute URI that identifies the code system of the target code (if the target is a value set that cross code systems).", 0, java.lang.Integer.MAX_VALUE, codeSystem));
          childrenList.add(new Property("code", "code", "Identity (code or path) or the element/item that the map refers to.", 0, java.lang.Integer.MAX_VALUE, code));
          childrenList.add(new Property("equivalence", "code", "The equivalence between the source and target concepts (counting for the dependencies and products). The equivalence is read from target to source (e.g. the target is 'wider' than the source).", 0, java.lang.Integer.MAX_VALUE, equivalence));
          childrenList.add(new Property("comments", "string", "A description of status/issues in mapping that conveys additional information not represented in  the structured data.", 0, java.lang.Integer.MAX_VALUE, comments));
          childrenList.add(new Property("dependsOn", "", "A set of additional dependencies for this mapping to hold. This mapping is only applicable if the specified element can be resolved, and it has the specified value.", 0, java.lang.Integer.MAX_VALUE, dependsOn));
          childrenList.add(new Property("product", "@ConceptMap.element.target.dependsOn", "A set of additional outcomes from this mapping to other elements. To properly execute this mapping, the specified element must be mapped to some data element or source that is in context. The mapping may still be useful without a place for the additional data elements, but the equivalence cannot be relied on.", 0, java.lang.Integer.MAX_VALUE, product));
        }

      public TargetElementComponent copy() {
        TargetElementComponent dst = new TargetElementComponent();
        copyValues(dst);
        dst.codeSystem = codeSystem == null ? null : codeSystem.copy();
        dst.code = code == null ? null : code.copy();
        dst.equivalence = equivalence == null ? null : equivalence.copy();
        dst.comments = comments == null ? null : comments.copy();
        if (dependsOn != null) {
          dst.dependsOn = new ArrayList<OtherElementComponent>();
          for (OtherElementComponent i : dependsOn)
            dst.dependsOn.add(i.copy());
        };
        if (product != null) {
          dst.product = new ArrayList<OtherElementComponent>();
          for (OtherElementComponent i : product)
            dst.product.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof TargetElementComponent))
          return false;
        TargetElementComponent o = (TargetElementComponent) other;
        return compareDeep(codeSystem, o.codeSystem, true) && compareDeep(code, o.code, true) && compareDeep(equivalence, o.equivalence, true)
           && compareDeep(comments, o.comments, true) && compareDeep(dependsOn, o.dependsOn, true) && compareDeep(product, o.product, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof TargetElementComponent))
          return false;
        TargetElementComponent o = (TargetElementComponent) other;
        return compareValues(codeSystem, o.codeSystem, true) && compareValues(code, o.code, true) && compareValues(equivalence, o.equivalence, true)
           && compareValues(comments, o.comments, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (codeSystem == null || codeSystem.isEmpty()) && (code == null || code.isEmpty())
           && (equivalence == null || equivalence.isEmpty()) && (comments == null || comments.isEmpty())
           && (dependsOn == null || dependsOn.isEmpty()) && (product == null || product.isEmpty());
      }

  }

    @Block()
    public static class OtherElementComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * A reference to a specific concept that holds a coded value. This can be an element in a FHIR resource, or a specific reference to a data element in a different specification (e.g. HL7 v2) or a general reference to a kind of data field, or a reference to a value set with an appropriately narrow definition.
         */
        @Child(name = "element", type = {UriType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Reference to element/field/ValueSet mapping depends on", formalDefinition="A reference to a specific concept that holds a coded value. This can be an element in a FHIR resource, or a specific reference to a data element in a different specification (e.g. HL7 v2) or a general reference to a kind of data field, or a reference to a value set with an appropriately narrow definition." )
        protected UriType element;

        /**
         * An absolute URI that identifies the code system of the dependency code (if the source/dependency is a value set that crosses code systems).
         */
        @Child(name = "codeSystem", type = {UriType.class}, order=2, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Code System (if necessary)", formalDefinition="An absolute URI that identifies the code system of the dependency code (if the source/dependency is a value set that crosses code systems)." )
        protected UriType codeSystem;

        /**
         * Identity (code or path) or the element/item/ValueSet that the map depends on / refers to.
         */
        @Child(name = "code", type = {StringType.class}, order=3, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Value of the referenced element", formalDefinition="Identity (code or path) or the element/item/ValueSet that the map depends on / refers to." )
        protected StringType code;

        private static final long serialVersionUID = 1488522448L;

    /*
     * Constructor
     */
      public OtherElementComponent() {
        super();
      }

    /*
     * Constructor
     */
      public OtherElementComponent(UriType element, UriType codeSystem, StringType code) {
        super();
        this.element = element;
        this.codeSystem = codeSystem;
        this.code = code;
      }

        /**
         * @return {@link #element} (A reference to a specific concept that holds a coded value. This can be an element in a FHIR resource, or a specific reference to a data element in a different specification (e.g. HL7 v2) or a general reference to a kind of data field, or a reference to a value set with an appropriately narrow definition.). This is the underlying object with id, value and extensions. The accessor "getElement" gives direct access to the value
         */
        public UriType getElementElement() { 
          if (this.element == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create OtherElementComponent.element");
            else if (Configuration.doAutoCreate())
              this.element = new UriType(); // bb
          return this.element;
        }

        public boolean hasElementElement() { 
          return this.element != null && !this.element.isEmpty();
        }

        public boolean hasElement() { 
          return this.element != null && !this.element.isEmpty();
        }

        /**
         * @param value {@link #element} (A reference to a specific concept that holds a coded value. This can be an element in a FHIR resource, or a specific reference to a data element in a different specification (e.g. HL7 v2) or a general reference to a kind of data field, or a reference to a value set with an appropriately narrow definition.). This is the underlying object with id, value and extensions. The accessor "getElement" gives direct access to the value
         */
        public OtherElementComponent setElementElement(UriType value) { 
          this.element = value;
          return this;
        }

        /**
         * @return A reference to a specific concept that holds a coded value. This can be an element in a FHIR resource, or a specific reference to a data element in a different specification (e.g. HL7 v2) or a general reference to a kind of data field, or a reference to a value set with an appropriately narrow definition.
         */
        public String getElement() { 
          return this.element == null ? null : this.element.getValue();
        }

        /**
         * @param value A reference to a specific concept that holds a coded value. This can be an element in a FHIR resource, or a specific reference to a data element in a different specification (e.g. HL7 v2) or a general reference to a kind of data field, or a reference to a value set with an appropriately narrow definition.
         */
        public OtherElementComponent setElement(String value) { 
            if (this.element == null)
              this.element = new UriType();
            this.element.setValue(value);
          return this;
        }

        /**
         * @return {@link #codeSystem} (An absolute URI that identifies the code system of the dependency code (if the source/dependency is a value set that crosses code systems).). This is the underlying object with id, value and extensions. The accessor "getCodeSystem" gives direct access to the value
         */
        public UriType getCodeSystemElement() { 
          if (this.codeSystem == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create OtherElementComponent.codeSystem");
            else if (Configuration.doAutoCreate())
              this.codeSystem = new UriType(); // bb
          return this.codeSystem;
        }

        public boolean hasCodeSystemElement() { 
          return this.codeSystem != null && !this.codeSystem.isEmpty();
        }

        public boolean hasCodeSystem() { 
          return this.codeSystem != null && !this.codeSystem.isEmpty();
        }

        /**
         * @param value {@link #codeSystem} (An absolute URI that identifies the code system of the dependency code (if the source/dependency is a value set that crosses code systems).). This is the underlying object with id, value and extensions. The accessor "getCodeSystem" gives direct access to the value
         */
        public OtherElementComponent setCodeSystemElement(UriType value) { 
          this.codeSystem = value;
          return this;
        }

        /**
         * @return An absolute URI that identifies the code system of the dependency code (if the source/dependency is a value set that crosses code systems).
         */
        public String getCodeSystem() { 
          return this.codeSystem == null ? null : this.codeSystem.getValue();
        }

        /**
         * @param value An absolute URI that identifies the code system of the dependency code (if the source/dependency is a value set that crosses code systems).
         */
        public OtherElementComponent setCodeSystem(String value) { 
            if (this.codeSystem == null)
              this.codeSystem = new UriType();
            this.codeSystem.setValue(value);
          return this;
        }

        /**
         * @return {@link #code} (Identity (code or path) or the element/item/ValueSet that the map depends on / refers to.). This is the underlying object with id, value and extensions. The accessor "getCode" gives direct access to the value
         */
        public StringType getCodeElement() { 
          if (this.code == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create OtherElementComponent.code");
            else if (Configuration.doAutoCreate())
              this.code = new StringType(); // bb
          return this.code;
        }

        public boolean hasCodeElement() { 
          return this.code != null && !this.code.isEmpty();
        }

        public boolean hasCode() { 
          return this.code != null && !this.code.isEmpty();
        }

        /**
         * @param value {@link #code} (Identity (code or path) or the element/item/ValueSet that the map depends on / refers to.). This is the underlying object with id, value and extensions. The accessor "getCode" gives direct access to the value
         */
        public OtherElementComponent setCodeElement(StringType value) { 
          this.code = value;
          return this;
        }

        /**
         * @return Identity (code or path) or the element/item/ValueSet that the map depends on / refers to.
         */
        public String getCode() { 
          return this.code == null ? null : this.code.getValue();
        }

        /**
         * @param value Identity (code or path) or the element/item/ValueSet that the map depends on / refers to.
         */
        public OtherElementComponent setCode(String value) { 
            if (this.code == null)
              this.code = new StringType();
            this.code.setValue(value);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("element", "uri", "A reference to a specific concept that holds a coded value. This can be an element in a FHIR resource, or a specific reference to a data element in a different specification (e.g. HL7 v2) or a general reference to a kind of data field, or a reference to a value set with an appropriately narrow definition.", 0, java.lang.Integer.MAX_VALUE, element));
          childrenList.add(new Property("codeSystem", "uri", "An absolute URI that identifies the code system of the dependency code (if the source/dependency is a value set that crosses code systems).", 0, java.lang.Integer.MAX_VALUE, codeSystem));
          childrenList.add(new Property("code", "string", "Identity (code or path) or the element/item/ValueSet that the map depends on / refers to.", 0, java.lang.Integer.MAX_VALUE, code));
        }

      public OtherElementComponent copy() {
        OtherElementComponent dst = new OtherElementComponent();
        copyValues(dst);
        dst.element = element == null ? null : element.copy();
        dst.codeSystem = codeSystem == null ? null : codeSystem.copy();
        dst.code = code == null ? null : code.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof OtherElementComponent))
          return false;
        OtherElementComponent o = (OtherElementComponent) other;
        return compareDeep(element, o.element, true) && compareDeep(codeSystem, o.codeSystem, true) && compareDeep(code, o.code, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof OtherElementComponent))
          return false;
        OtherElementComponent o = (OtherElementComponent) other;
        return compareValues(element, o.element, true) && compareValues(codeSystem, o.codeSystem, true) && compareValues(code, o.code, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (element == null || element.isEmpty()) && (codeSystem == null || codeSystem.isEmpty())
           && (code == null || code.isEmpty());
      }

  }

    /**
     * An absolute URL that is used to identify this concept map when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this concept map is (or will be) published.
     */
    @Child(name = "url", type = {UriType.class}, order=0, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Globally unique logical id for concept map", formalDefinition="An absolute URL that is used to identify this concept map when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this concept map is (or will be) published." )
    protected UriType url;

    /**
     * Formal identifier that is used to identify this concept map when it is represented in other formats, or referenced in a specification, model, design or an instance.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=1, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Additional identifier for the concept map", formalDefinition="Formal identifier that is used to identify this concept map when it is represented in other formats, or referenced in a specification, model, design or an instance." )
    protected Identifier identifier;

    /**
     * The identifier that is used to identify this version of the concept map when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the profile author manually and the value should be a timestamp.
     */
    @Child(name = "version", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Logical id for this version of the concept map", formalDefinition="The identifier that is used to identify this version of the concept map when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the profile author manually and the value should be a timestamp." )
    protected StringType version;

    /**
     * A free text natural language name describing the concept map.
     */
    @Child(name = "name", type = {StringType.class}, order=3, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Informal name for this concept map", formalDefinition="A free text natural language name describing the concept map." )
    protected StringType name;

    /**
     * The status of the concept map.
     */
    @Child(name = "status", type = {CodeType.class}, order=4, min=1, max=1, modifier=true, summary=true)
    @Description(shortDefinition="draft | active | retired", formalDefinition="The status of the concept map." )
    protected Enumeration<ConformanceResourceStatus> status;

    /**
     * This ConceptMap was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.
     */
    @Child(name = "experimental", type = {BooleanType.class}, order=5, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="If for testing purposes, not real usage", formalDefinition="This ConceptMap was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage." )
    protected BooleanType experimental;

    /**
     * The name of the individual or organization that published the concept map.
     */
    @Child(name = "publisher", type = {StringType.class}, order=6, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Name of the publisher (organization or individual)", formalDefinition="The name of the individual or organization that published the concept map." )
    protected StringType publisher;

    /**
     * Contacts to assist a user in finding and communicating with the publisher.
     */
    @Child(name = "contact", type = {}, order=7, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Contact details of the publisher", formalDefinition="Contacts to assist a user in finding and communicating with the publisher." )
    protected List<ConceptMapContactComponent> contact;

    /**
     * The date this version of the concept map was published. The date must change when the business version changes, if it does, and it must change if the status code changes. In addition, it should change when the substantive content of the concept map changes.
     */
    @Child(name = "date", type = {DateTimeType.class}, order=8, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Date for given status", formalDefinition="The date this version of the concept map was published. The date must change when the business version changes, if it does, and it must change if the status code changes. In addition, it should change when the substantive content of the concept map changes." )
    protected DateTimeType date;

    /**
     * A free text natural language description of the use of the concept map - reason for definition, conditions of use, etc.
     */
    @Child(name = "description", type = {StringType.class}, order=9, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Human language description of the concept map", formalDefinition="A free text natural language description of the use of the concept map - reason for definition, conditions of use, etc." )
    protected StringType description;

    /**
     * The content was developed with a focus and intent of supporting the contexts that are listed. These terms may be used to assist with indexing and searching of concept map instances.
     */
    @Child(name = "useContext", type = {CodeableConcept.class}, order=10, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Content intends to support these contexts", formalDefinition="The content was developed with a focus and intent of supporting the contexts that are listed. These terms may be used to assist with indexing and searching of concept map instances." )
    protected List<CodeableConcept> useContext;

    /**
     * Explains why this concept map is needed and why it has been constrained as it has.
     */
    @Child(name = "requirements", type = {StringType.class}, order=11, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Why needed", formalDefinition="Explains why this concept map is needed and why it has been constrained as it has." )
    protected StringType requirements;

    /**
     * A copyright statement relating to the concept map and/or its contents.
     */
    @Child(name = "copyright", type = {StringType.class}, order=12, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Use and/or publishing restrictions", formalDefinition="A copyright statement relating to the concept map and/or its contents." )
    protected StringType copyright;

    /**
     * The source value set that specifies the concepts that are being mapped.
     */
    @Child(name = "source", type = {UriType.class, ValueSet.class, StructureDefinition.class}, order=13, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Identifies the source of the concepts which are being mapped", formalDefinition="The source value set that specifies the concepts that are being mapped." )
    protected Type source;

    /**
     * The target value set provides context to the mappings. Note that the mapping is made between concepts, not between value sets, but the value set provides important context about how the concept mapping choices are made.
     */
    @Child(name = "target", type = {UriType.class, ValueSet.class, StructureDefinition.class}, order=14, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Provides context to the mappings", formalDefinition="The target value set provides context to the mappings. Note that the mapping is made between concepts, not between value sets, but the value set provides important context about how the concept mapping choices are made." )
    protected Type target;

    /**
     * Mappings for an individual concept in the source to one or more concepts in the target.
     */
    @Child(name = "element", type = {}, order=15, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Mappings for a concept from the source set", formalDefinition="Mappings for an individual concept in the source to one or more concepts in the target." )
    protected List<SourceElementComponent> element;

    private static final long serialVersionUID = 1687563642L;

  /*
   * Constructor
   */
    public ConceptMap() {
      super();
    }

  /*
   * Constructor
   */
    public ConceptMap(Enumeration<ConformanceResourceStatus> status, Type source, Type target) {
      super();
      this.status = status;
      this.source = source;
      this.target = target;
    }

    /**
     * @return {@link #url} (An absolute URL that is used to identify this concept map when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this concept map is (or will be) published.). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
     */
    public UriType getUrlElement() { 
      if (this.url == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ConceptMap.url");
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
     * @param value {@link #url} (An absolute URL that is used to identify this concept map when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this concept map is (or will be) published.). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
     */
    public ConceptMap setUrlElement(UriType value) { 
      this.url = value;
      return this;
    }

    /**
     * @return An absolute URL that is used to identify this concept map when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this concept map is (or will be) published.
     */
    public String getUrl() { 
      return this.url == null ? null : this.url.getValue();
    }

    /**
     * @param value An absolute URL that is used to identify this concept map when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this concept map is (or will be) published.
     */
    public ConceptMap setUrl(String value) { 
      if (Utilities.noString(value))
        this.url = null;
      else {
        if (this.url == null)
          this.url = new UriType();
        this.url.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #identifier} (Formal identifier that is used to identify this concept map when it is represented in other formats, or referenced in a specification, model, design or an instance.)
     */
    public Identifier getIdentifier() { 
      if (this.identifier == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ConceptMap.identifier");
        else if (Configuration.doAutoCreate())
          this.identifier = new Identifier(); // cc
      return this.identifier;
    }

    public boolean hasIdentifier() { 
      return this.identifier != null && !this.identifier.isEmpty();
    }

    /**
     * @param value {@link #identifier} (Formal identifier that is used to identify this concept map when it is represented in other formats, or referenced in a specification, model, design or an instance.)
     */
    public ConceptMap setIdentifier(Identifier value) { 
      this.identifier = value;
      return this;
    }

    /**
     * @return {@link #version} (The identifier that is used to identify this version of the concept map when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the profile author manually and the value should be a timestamp.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
     */
    public StringType getVersionElement() { 
      if (this.version == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ConceptMap.version");
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
     * @param value {@link #version} (The identifier that is used to identify this version of the concept map when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the profile author manually and the value should be a timestamp.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
     */
    public ConceptMap setVersionElement(StringType value) { 
      this.version = value;
      return this;
    }

    /**
     * @return The identifier that is used to identify this version of the concept map when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the profile author manually and the value should be a timestamp.
     */
    public String getVersion() { 
      return this.version == null ? null : this.version.getValue();
    }

    /**
     * @param value The identifier that is used to identify this version of the concept map when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the profile author manually and the value should be a timestamp.
     */
    public ConceptMap setVersion(String value) { 
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
     * @return {@link #name} (A free text natural language name describing the concept map.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
     */
    public StringType getNameElement() { 
      if (this.name == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ConceptMap.name");
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
     * @param value {@link #name} (A free text natural language name describing the concept map.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
     */
    public ConceptMap setNameElement(StringType value) { 
      this.name = value;
      return this;
    }

    /**
     * @return A free text natural language name describing the concept map.
     */
    public String getName() { 
      return this.name == null ? null : this.name.getValue();
    }

    /**
     * @param value A free text natural language name describing the concept map.
     */
    public ConceptMap setName(String value) { 
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
     * @return {@link #status} (The status of the concept map.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<ConformanceResourceStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ConceptMap.status");
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
     * @param value {@link #status} (The status of the concept map.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public ConceptMap setStatusElement(Enumeration<ConformanceResourceStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return The status of the concept map.
     */
    public ConformanceResourceStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value The status of the concept map.
     */
    public ConceptMap setStatus(ConformanceResourceStatus value) { 
        if (this.status == null)
          this.status = new Enumeration<ConformanceResourceStatus>(new ConformanceResourceStatusEnumFactory());
        this.status.setValue(value);
      return this;
    }

    /**
     * @return {@link #experimental} (This ConceptMap was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.). This is the underlying object with id, value and extensions. The accessor "getExperimental" gives direct access to the value
     */
    public BooleanType getExperimentalElement() { 
      if (this.experimental == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ConceptMap.experimental");
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
     * @param value {@link #experimental} (This ConceptMap was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.). This is the underlying object with id, value and extensions. The accessor "getExperimental" gives direct access to the value
     */
    public ConceptMap setExperimentalElement(BooleanType value) { 
      this.experimental = value;
      return this;
    }

    /**
     * @return This ConceptMap was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.
     */
    public boolean getExperimental() { 
      return this.experimental == null || this.experimental.isEmpty() ? false : this.experimental.getValue();
    }

    /**
     * @param value This ConceptMap was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.
     */
    public ConceptMap setExperimental(boolean value) { 
        if (this.experimental == null)
          this.experimental = new BooleanType();
        this.experimental.setValue(value);
      return this;
    }

    /**
     * @return {@link #publisher} (The name of the individual or organization that published the concept map.). This is the underlying object with id, value and extensions. The accessor "getPublisher" gives direct access to the value
     */
    public StringType getPublisherElement() { 
      if (this.publisher == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ConceptMap.publisher");
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
     * @param value {@link #publisher} (The name of the individual or organization that published the concept map.). This is the underlying object with id, value and extensions. The accessor "getPublisher" gives direct access to the value
     */
    public ConceptMap setPublisherElement(StringType value) { 
      this.publisher = value;
      return this;
    }

    /**
     * @return The name of the individual or organization that published the concept map.
     */
    public String getPublisher() { 
      return this.publisher == null ? null : this.publisher.getValue();
    }

    /**
     * @param value The name of the individual or organization that published the concept map.
     */
    public ConceptMap setPublisher(String value) { 
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
    public List<ConceptMapContactComponent> getContact() { 
      if (this.contact == null)
        this.contact = new ArrayList<ConceptMapContactComponent>();
      return this.contact;
    }

    public boolean hasContact() { 
      if (this.contact == null)
        return false;
      for (ConceptMapContactComponent item : this.contact)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #contact} (Contacts to assist a user in finding and communicating with the publisher.)
     */
    // syntactic sugar
    public ConceptMapContactComponent addContact() { //3
      ConceptMapContactComponent t = new ConceptMapContactComponent();
      if (this.contact == null)
        this.contact = new ArrayList<ConceptMapContactComponent>();
      this.contact.add(t);
      return t;
    }

    // syntactic sugar
    public ConceptMap addContact(ConceptMapContactComponent t) { //3
      if (t == null)
        return this;
      if (this.contact == null)
        this.contact = new ArrayList<ConceptMapContactComponent>();
      this.contact.add(t);
      return this;
    }

    /**
     * @return {@link #date} (The date this version of the concept map was published. The date must change when the business version changes, if it does, and it must change if the status code changes. In addition, it should change when the substantive content of the concept map changes.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
     */
    public DateTimeType getDateElement() { 
      if (this.date == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ConceptMap.date");
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
     * @param value {@link #date} (The date this version of the concept map was published. The date must change when the business version changes, if it does, and it must change if the status code changes. In addition, it should change when the substantive content of the concept map changes.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
     */
    public ConceptMap setDateElement(DateTimeType value) { 
      this.date = value;
      return this;
    }

    /**
     * @return The date this version of the concept map was published. The date must change when the business version changes, if it does, and it must change if the status code changes. In addition, it should change when the substantive content of the concept map changes.
     */
    public Date getDate() { 
      return this.date == null ? null : this.date.getValue();
    }

    /**
     * @param value The date this version of the concept map was published. The date must change when the business version changes, if it does, and it must change if the status code changes. In addition, it should change when the substantive content of the concept map changes.
     */
    public ConceptMap setDate(Date value) { 
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
     * @return {@link #description} (A free text natural language description of the use of the concept map - reason for definition, conditions of use, etc.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public StringType getDescriptionElement() { 
      if (this.description == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ConceptMap.description");
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
     * @param value {@link #description} (A free text natural language description of the use of the concept map - reason for definition, conditions of use, etc.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public ConceptMap setDescriptionElement(StringType value) { 
      this.description = value;
      return this;
    }

    /**
     * @return A free text natural language description of the use of the concept map - reason for definition, conditions of use, etc.
     */
    public String getDescription() { 
      return this.description == null ? null : this.description.getValue();
    }

    /**
     * @param value A free text natural language description of the use of the concept map - reason for definition, conditions of use, etc.
     */
    public ConceptMap setDescription(String value) { 
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
     * @return {@link #useContext} (The content was developed with a focus and intent of supporting the contexts that are listed. These terms may be used to assist with indexing and searching of concept map instances.)
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
     * @return {@link #useContext} (The content was developed with a focus and intent of supporting the contexts that are listed. These terms may be used to assist with indexing and searching of concept map instances.)
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
    public ConceptMap addUseContext(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.useContext == null)
        this.useContext = new ArrayList<CodeableConcept>();
      this.useContext.add(t);
      return this;
    }

    /**
     * @return {@link #requirements} (Explains why this concept map is needed and why it has been constrained as it has.). This is the underlying object with id, value and extensions. The accessor "getRequirements" gives direct access to the value
     */
    public StringType getRequirementsElement() { 
      if (this.requirements == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ConceptMap.requirements");
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
     * @param value {@link #requirements} (Explains why this concept map is needed and why it has been constrained as it has.). This is the underlying object with id, value and extensions. The accessor "getRequirements" gives direct access to the value
     */
    public ConceptMap setRequirementsElement(StringType value) { 
      this.requirements = value;
      return this;
    }

    /**
     * @return Explains why this concept map is needed and why it has been constrained as it has.
     */
    public String getRequirements() { 
      return this.requirements == null ? null : this.requirements.getValue();
    }

    /**
     * @param value Explains why this concept map is needed and why it has been constrained as it has.
     */
    public ConceptMap setRequirements(String value) { 
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
     * @return {@link #copyright} (A copyright statement relating to the concept map and/or its contents.). This is the underlying object with id, value and extensions. The accessor "getCopyright" gives direct access to the value
     */
    public StringType getCopyrightElement() { 
      if (this.copyright == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ConceptMap.copyright");
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
     * @param value {@link #copyright} (A copyright statement relating to the concept map and/or its contents.). This is the underlying object with id, value and extensions. The accessor "getCopyright" gives direct access to the value
     */
    public ConceptMap setCopyrightElement(StringType value) { 
      this.copyright = value;
      return this;
    }

    /**
     * @return A copyright statement relating to the concept map and/or its contents.
     */
    public String getCopyright() { 
      return this.copyright == null ? null : this.copyright.getValue();
    }

    /**
     * @param value A copyright statement relating to the concept map and/or its contents.
     */
    public ConceptMap setCopyright(String value) { 
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
     * @return {@link #source} (The source value set that specifies the concepts that are being mapped.)
     */
    public Type getSource() { 
      return this.source;
    }

    /**
     * @return {@link #source} (The source value set that specifies the concepts that are being mapped.)
     */
    public UriType getSourceUriType() throws Exception { 
      if (!(this.source instanceof UriType))
        throw new Exception("Type mismatch: the type UriType was expected, but "+this.source.getClass().getName()+" was encountered");
      return (UriType) this.source;
    }

    public boolean hasSourceUriType() throws Exception { 
      return this.source instanceof UriType;
    }

    /**
     * @return {@link #source} (The source value set that specifies the concepts that are being mapped.)
     */
    public Reference getSourceReference() throws Exception { 
      if (!(this.source instanceof Reference))
        throw new Exception("Type mismatch: the type Reference was expected, but "+this.source.getClass().getName()+" was encountered");
      return (Reference) this.source;
    }

    public boolean hasSourceReference() throws Exception { 
      return this.source instanceof Reference;
    }

    public boolean hasSource() { 
      return this.source != null && !this.source.isEmpty();
    }

    /**
     * @param value {@link #source} (The source value set that specifies the concepts that are being mapped.)
     */
    public ConceptMap setSource(Type value) { 
      this.source = value;
      return this;
    }

    /**
     * @return {@link #target} (The target value set provides context to the mappings. Note that the mapping is made between concepts, not between value sets, but the value set provides important context about how the concept mapping choices are made.)
     */
    public Type getTarget() { 
      return this.target;
    }

    /**
     * @return {@link #target} (The target value set provides context to the mappings. Note that the mapping is made between concepts, not between value sets, but the value set provides important context about how the concept mapping choices are made.)
     */
    public UriType getTargetUriType() throws Exception { 
      if (!(this.target instanceof UriType))
        throw new Exception("Type mismatch: the type UriType was expected, but "+this.target.getClass().getName()+" was encountered");
      return (UriType) this.target;
    }

    public boolean hasTargetUriType() throws Exception { 
      return this.target instanceof UriType;
    }

    /**
     * @return {@link #target} (The target value set provides context to the mappings. Note that the mapping is made between concepts, not between value sets, but the value set provides important context about how the concept mapping choices are made.)
     */
    public Reference getTargetReference() throws Exception { 
      if (!(this.target instanceof Reference))
        throw new Exception("Type mismatch: the type Reference was expected, but "+this.target.getClass().getName()+" was encountered");
      return (Reference) this.target;
    }

    public boolean hasTargetReference() throws Exception { 
      return this.target instanceof Reference;
    }

    public boolean hasTarget() { 
      return this.target != null && !this.target.isEmpty();
    }

    /**
     * @param value {@link #target} (The target value set provides context to the mappings. Note that the mapping is made between concepts, not between value sets, but the value set provides important context about how the concept mapping choices are made.)
     */
    public ConceptMap setTarget(Type value) { 
      this.target = value;
      return this;
    }

    /**
     * @return {@link #element} (Mappings for an individual concept in the source to one or more concepts in the target.)
     */
    public List<SourceElementComponent> getElement() { 
      if (this.element == null)
        this.element = new ArrayList<SourceElementComponent>();
      return this.element;
    }

    public boolean hasElement() { 
      if (this.element == null)
        return false;
      for (SourceElementComponent item : this.element)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #element} (Mappings for an individual concept in the source to one or more concepts in the target.)
     */
    // syntactic sugar
    public SourceElementComponent addElement() { //3
      SourceElementComponent t = new SourceElementComponent();
      if (this.element == null)
        this.element = new ArrayList<SourceElementComponent>();
      this.element.add(t);
      return t;
    }

    // syntactic sugar
    public ConceptMap addElement(SourceElementComponent t) { //3
      if (t == null)
        return this;
      if (this.element == null)
        this.element = new ArrayList<SourceElementComponent>();
      this.element.add(t);
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("url", "uri", "An absolute URL that is used to identify this concept map when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this concept map is (or will be) published.", 0, java.lang.Integer.MAX_VALUE, url));
        childrenList.add(new Property("identifier", "Identifier", "Formal identifier that is used to identify this concept map when it is represented in other formats, or referenced in a specification, model, design or an instance.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("version", "string", "The identifier that is used to identify this version of the concept map when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the profile author manually and the value should be a timestamp.", 0, java.lang.Integer.MAX_VALUE, version));
        childrenList.add(new Property("name", "string", "A free text natural language name describing the concept map.", 0, java.lang.Integer.MAX_VALUE, name));
        childrenList.add(new Property("status", "code", "The status of the concept map.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("experimental", "boolean", "This ConceptMap was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.", 0, java.lang.Integer.MAX_VALUE, experimental));
        childrenList.add(new Property("publisher", "string", "The name of the individual or organization that published the concept map.", 0, java.lang.Integer.MAX_VALUE, publisher));
        childrenList.add(new Property("contact", "", "Contacts to assist a user in finding and communicating with the publisher.", 0, java.lang.Integer.MAX_VALUE, contact));
        childrenList.add(new Property("date", "dateTime", "The date this version of the concept map was published. The date must change when the business version changes, if it does, and it must change if the status code changes. In addition, it should change when the substantive content of the concept map changes.", 0, java.lang.Integer.MAX_VALUE, date));
        childrenList.add(new Property("description", "string", "A free text natural language description of the use of the concept map - reason for definition, conditions of use, etc.", 0, java.lang.Integer.MAX_VALUE, description));
        childrenList.add(new Property("useContext", "CodeableConcept", "The content was developed with a focus and intent of supporting the contexts that are listed. These terms may be used to assist with indexing and searching of concept map instances.", 0, java.lang.Integer.MAX_VALUE, useContext));
        childrenList.add(new Property("requirements", "string", "Explains why this concept map is needed and why it has been constrained as it has.", 0, java.lang.Integer.MAX_VALUE, requirements));
        childrenList.add(new Property("copyright", "string", "A copyright statement relating to the concept map and/or its contents.", 0, java.lang.Integer.MAX_VALUE, copyright));
        childrenList.add(new Property("source[x]", "uri|Reference(ValueSet|StructureDefinition)", "The source value set that specifies the concepts that are being mapped.", 0, java.lang.Integer.MAX_VALUE, source));
        childrenList.add(new Property("target[x]", "uri|Reference(ValueSet|StructureDefinition)", "The target value set provides context to the mappings. Note that the mapping is made between concepts, not between value sets, but the value set provides important context about how the concept mapping choices are made.", 0, java.lang.Integer.MAX_VALUE, target));
        childrenList.add(new Property("element", "", "Mappings for an individual concept in the source to one or more concepts in the target.", 0, java.lang.Integer.MAX_VALUE, element));
      }

      public ConceptMap copy() {
        ConceptMap dst = new ConceptMap();
        copyValues(dst);
        dst.url = url == null ? null : url.copy();
        dst.identifier = identifier == null ? null : identifier.copy();
        dst.version = version == null ? null : version.copy();
        dst.name = name == null ? null : name.copy();
        dst.status = status == null ? null : status.copy();
        dst.experimental = experimental == null ? null : experimental.copy();
        dst.publisher = publisher == null ? null : publisher.copy();
        if (contact != null) {
          dst.contact = new ArrayList<ConceptMapContactComponent>();
          for (ConceptMapContactComponent i : contact)
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
        dst.source = source == null ? null : source.copy();
        dst.target = target == null ? null : target.copy();
        if (element != null) {
          dst.element = new ArrayList<SourceElementComponent>();
          for (SourceElementComponent i : element)
            dst.element.add(i.copy());
        };
        return dst;
      }

      protected ConceptMap typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ConceptMap))
          return false;
        ConceptMap o = (ConceptMap) other;
        return compareDeep(url, o.url, true) && compareDeep(identifier, o.identifier, true) && compareDeep(version, o.version, true)
           && compareDeep(name, o.name, true) && compareDeep(status, o.status, true) && compareDeep(experimental, o.experimental, true)
           && compareDeep(publisher, o.publisher, true) && compareDeep(contact, o.contact, true) && compareDeep(date, o.date, true)
           && compareDeep(description, o.description, true) && compareDeep(useContext, o.useContext, true)
           && compareDeep(requirements, o.requirements, true) && compareDeep(copyright, o.copyright, true)
           && compareDeep(source, o.source, true) && compareDeep(target, o.target, true) && compareDeep(element, o.element, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ConceptMap))
          return false;
        ConceptMap o = (ConceptMap) other;
        return compareValues(url, o.url, true) && compareValues(version, o.version, true) && compareValues(name, o.name, true)
           && compareValues(status, o.status, true) && compareValues(experimental, o.experimental, true) && compareValues(publisher, o.publisher, true)
           && compareValues(date, o.date, true) && compareValues(description, o.description, true) && compareValues(requirements, o.requirements, true)
           && compareValues(copyright, o.copyright, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (url == null || url.isEmpty()) && (identifier == null || identifier.isEmpty())
           && (version == null || version.isEmpty()) && (name == null || name.isEmpty()) && (status == null || status.isEmpty())
           && (experimental == null || experimental.isEmpty()) && (publisher == null || publisher.isEmpty())
           && (contact == null || contact.isEmpty()) && (date == null || date.isEmpty()) && (description == null || description.isEmpty())
           && (useContext == null || useContext.isEmpty()) && (requirements == null || requirements.isEmpty())
           && (copyright == null || copyright.isEmpty()) && (source == null || source.isEmpty()) && (target == null || target.isEmpty())
           && (element == null || element.isEmpty());
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.ConceptMap;
   }

  @SearchParamDefinition(name="date", path="ConceptMap.date", description="The concept map publication date", type="date" )
  public static final String SP_DATE = "date";
  @SearchParamDefinition(name="identifier", path="ConceptMap.identifier", description="Additional identifier for the concept map", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
  @SearchParamDefinition(name="product", path="ConceptMap.element.target.product.element", description="Reference to element/field/ValueSet mapping depends on", type="uri" )
  public static final String SP_PRODUCT = "product";
  @SearchParamDefinition(name="dependson", path="ConceptMap.element.target.dependsOn.element", description="Reference to element/field/ValueSet mapping depends on", type="uri" )
  public static final String SP_DEPENDSON = "dependson";
  @SearchParamDefinition(name="description", path="ConceptMap.description", description="Text search in the description of the concept map", type="string" )
  public static final String SP_DESCRIPTION = "description";
  @SearchParamDefinition(name="targetsystem", path="ConceptMap.element.target.codeSystem", description="System of the target (if necessary)", type="uri" )
  public static final String SP_TARGETSYSTEM = "targetsystem";
  @SearchParamDefinition(name="source", path="ConceptMap.sourceReference", description="Identifies the source of the concepts which are being mapped", type="reference" )
  public static final String SP_SOURCE = "source";
  @SearchParamDefinition(name="version", path="ConceptMap.version", description="The version identifier of the concept map", type="token" )
  public static final String SP_VERSION = "version";
  @SearchParamDefinition(name="sourcesystem", path="ConceptMap.element.codeSystem", description="Code System (if value set crosses code systems)", type="uri" )
  public static final String SP_SOURCESYSTEM = "sourcesystem";
  @SearchParamDefinition(name="url", path="ConceptMap.url", description="The URL of the concept map", type="uri" )
  public static final String SP_URL = "url";
  @SearchParamDefinition(name="target", path="ConceptMap.target[x]", description="Provides context to the mappings", type="reference" )
  public static final String SP_TARGET = "target";
  @SearchParamDefinition(name="sourcecode", path="ConceptMap.element.code", description="Identifies element being mapped", type="token" )
  public static final String SP_SOURCECODE = "sourcecode";
  @SearchParamDefinition(name="sourceuri", path="ConceptMap.sourceUri", description="Identifies the source of the concepts which are being mapped", type="reference" )
  public static final String SP_SOURCEURI = "sourceuri";
  @SearchParamDefinition(name="name", path="ConceptMap.name", description="Name of the concept map", type="string" )
  public static final String SP_NAME = "name";
  @SearchParamDefinition(name="context", path="ConceptMap.useContext", description="A use context assigned to the concept map", type="token" )
  public static final String SP_CONTEXT = "context";
  @SearchParamDefinition(name="publisher", path="ConceptMap.publisher", description="Name of the publisher of the concept map", type="string" )
  public static final String SP_PUBLISHER = "publisher";
  @SearchParamDefinition(name="targetcode", path="ConceptMap.element.target.code", description="Code that identifies the target element", type="token" )
  public static final String SP_TARGETCODE = "targetcode";
  @SearchParamDefinition(name="status", path="ConceptMap.status", description="Status of the concept map", type="token" )
  public static final String SP_STATUS = "status";

}

