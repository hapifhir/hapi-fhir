package org.hl7.fhir.instance.model;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2015 University Health Network
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

// Generated on Sun, Dec 7, 2014 21:45-0500 for FHIR v0.3.0

import java.util.*;

import org.hl7.fhir.utilities.Utilities;
import org.hl7.fhir.instance.model.annotations.ResourceDef;
import org.hl7.fhir.instance.model.annotations.SearchParamDefinition;
import org.hl7.fhir.instance.model.annotations.Block;
import org.hl7.fhir.instance.model.annotations.Child;
import org.hl7.fhir.instance.model.annotations.Description;
/**
 * Prospective warnings of potential issues when providing care to the patient.
 */
@ResourceDef(name="Alert", profile="http://hl7.org/fhir/Profile/Alert")
public class Alert extends DomainResource {

    public enum AlertStatus implements FhirEnum {
        /**
         * A current alert that should be displayed to a user. A system may use the category to determine which roles should view the alert.
         */
        ACTIVE, 
        /**
         * The alert does not need to be displayed any more.
         */
        INACTIVE, 
        /**
         * The alert was added in error, and should no longer be displayed.
         */
        ENTEREDINERROR, 
        /**
         * added to help the parsers
         */
        NULL;

      public static final AlertStatusEnumFactory ENUM_FACTORY = new AlertStatusEnumFactory();

        public static AlertStatus fromCode(String codeString) throws IllegalArgumentException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("active".equals(codeString))
          return ACTIVE;
        if ("inactive".equals(codeString))
          return INACTIVE;
        if ("entered in error".equals(codeString))
          return ENTEREDINERROR;
        throw new IllegalArgumentException("Unknown AlertStatus code '"+codeString+"'");
        }
        @Override
        public String toCode() {
          switch (this) {
            case ACTIVE: return "active";
            case INACTIVE: return "inactive";
            case ENTEREDINERROR: return "entered in error";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case ACTIVE: return "";
            case INACTIVE: return "";
            case ENTEREDINERROR: return "";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case ACTIVE: return "A current alert that should be displayed to a user. A system may use the category to determine which roles should view the alert.";
            case INACTIVE: return "The alert does not need to be displayed any more.";
            case ENTEREDINERROR: return "The alert was added in error, and should no longer be displayed.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case ACTIVE: return "active";
            case INACTIVE: return "inactive";
            case ENTEREDINERROR: return "entered in error";
            default: return "?";
          }
        }
    }

  public static class AlertStatusEnumFactory implements EnumFactory<AlertStatus> {
    public AlertStatus fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("active".equals(codeString))
          return AlertStatus.ACTIVE;
        if ("inactive".equals(codeString))
          return AlertStatus.INACTIVE;
        if ("entered in error".equals(codeString))
          return AlertStatus.ENTEREDINERROR;
        throw new IllegalArgumentException("Unknown AlertStatus code '"+codeString+"'");
        }
    public String toCode(AlertStatus code) throws IllegalArgumentException {
      if (code == AlertStatus.ACTIVE)
        return "active";
      if (code == AlertStatus.INACTIVE)
        return "inactive";
      if (code == AlertStatus.ENTEREDINERROR)
        return "entered in error";
      return "?";
      }
    }

    /**
     * Identifier assigned to the alert for external use (outside the FHIR environment).
     */
    @Child(name="identifier", type={Identifier.class}, order=-1, min=0, max=Child.MAX_UNLIMITED)
    @Description(shortDefinition="Business identifier", formalDefinition="Identifier assigned to the alert for external use (outside the FHIR environment)." )
    protected List<Identifier> identifier;

    /**
     * Allows an alert to be divided into different categories like clinical, administrative etc.
     */
    @Child(name="category", type={CodeableConcept.class}, order=0, min=0, max=1)
    @Description(shortDefinition="Clinical, administrative, etc.", formalDefinition="Allows an alert to be divided into different categories like clinical, administrative etc." )
    protected CodeableConcept category;

    /**
     * Supports basic workflow.
     */
    @Child(name="status", type={CodeType.class}, order=1, min=1, max=1)
    @Description(shortDefinition="active | inactive | entered in error", formalDefinition="Supports basic workflow." )
    protected Enumeration<AlertStatus> status;

    /**
     * The person who this alert concerns.
     */
    @Child(name="subject", type={Patient.class}, order=2, min=1, max=1)
    @Description(shortDefinition="Who is alert about?", formalDefinition="The person who this alert concerns." )
    protected Reference subject;

    /**
     * The actual object that is the target of the reference (The person who this alert concerns.)
     */
    protected Patient subjectTarget;

    /**
     * The person or device that created the alert.
     */
    @Child(name="author", type={Practitioner.class, Patient.class, Device.class}, order=3, min=0, max=1)
    @Description(shortDefinition="Alert creator", formalDefinition="The person or device that created the alert." )
    protected Reference author;

    /**
     * The actual object that is the target of the reference (The person or device that created the alert.)
     */
    protected Resource authorTarget;

    /**
     * The textual component of the alert to display to the user.
     */
    @Child(name="note", type={StringType.class}, order=4, min=1, max=1)
    @Description(shortDefinition="Text of alert", formalDefinition="The textual component of the alert to display to the user." )
    protected StringType note;

    private static final long serialVersionUID = -655685828L;

    public Alert() {
      super();
    }

    public Alert(Enumeration<AlertStatus> status, Reference subject, StringType note) {
      super();
      this.status = status;
      this.subject = subject;
      this.note = note;
    }

    /**
     * @return {@link #identifier} (Identifier assigned to the alert for external use (outside the FHIR environment).)
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
     * @return {@link #identifier} (Identifier assigned to the alert for external use (outside the FHIR environment).)
     */
    // syntactic sugar
    public Identifier addIdentifier() { //3
      Identifier t = new Identifier();
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      this.identifier.add(t);
      return t;
    }

    /**
     * @return {@link #category} (Allows an alert to be divided into different categories like clinical, administrative etc.)
     */
    public CodeableConcept getCategory() { 
      if (this.category == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Alert.category");
        else if (Configuration.doAutoCreate())
          this.category = new CodeableConcept();
      return this.category;
    }

    public boolean hasCategory() { 
      return this.category != null && !this.category.isEmpty();
    }

    /**
     * @param value {@link #category} (Allows an alert to be divided into different categories like clinical, administrative etc.)
     */
    public Alert setCategory(CodeableConcept value) { 
      this.category = value;
      return this;
    }

    /**
     * @return {@link #status} (Supports basic workflow.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<AlertStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Alert.status");
        else if (Configuration.doAutoCreate())
          this.status = new Enumeration<AlertStatus>();
      return this.status;
    }

    public boolean hasStatusElement() { 
      return this.status != null && !this.status.isEmpty();
    }

    public boolean hasStatus() { 
      return this.status != null && !this.status.isEmpty();
    }

    /**
     * @param value {@link #status} (Supports basic workflow.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Alert setStatusElement(Enumeration<AlertStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return Supports basic workflow.
     */
    public AlertStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value Supports basic workflow.
     */
    public Alert setStatus(AlertStatus value) { 
        if (this.status == null)
          this.status = new Enumeration<AlertStatus>(AlertStatus.ENUM_FACTORY);
        this.status.setValue(value);
      return this;
    }

    /**
     * @return {@link #subject} (The person who this alert concerns.)
     */
    public Reference getSubject() { 
      if (this.subject == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Alert.subject");
        else if (Configuration.doAutoCreate())
          this.subject = new Reference();
      return this.subject;
    }

    public boolean hasSubject() { 
      return this.subject != null && !this.subject.isEmpty();
    }

    /**
     * @param value {@link #subject} (The person who this alert concerns.)
     */
    public Alert setSubject(Reference value) { 
      this.subject = value;
      return this;
    }

    /**
     * @return {@link #subject} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The person who this alert concerns.)
     */
    public Patient getSubjectTarget() { 
      if (this.subjectTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Alert.subject");
        else if (Configuration.doAutoCreate())
          this.subjectTarget = new Patient();
      return this.subjectTarget;
    }

    /**
     * @param value {@link #subject} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The person who this alert concerns.)
     */
    public Alert setSubjectTarget(Patient value) { 
      this.subjectTarget = value;
      return this;
    }

    /**
     * @return {@link #author} (The person or device that created the alert.)
     */
    public Reference getAuthor() { 
      if (this.author == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Alert.author");
        else if (Configuration.doAutoCreate())
          this.author = new Reference();
      return this.author;
    }

    public boolean hasAuthor() { 
      return this.author != null && !this.author.isEmpty();
    }

    /**
     * @param value {@link #author} (The person or device that created the alert.)
     */
    public Alert setAuthor(Reference value) { 
      this.author = value;
      return this;
    }

    /**
     * @return {@link #author} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The person or device that created the alert.)
     */
    public Resource getAuthorTarget() { 
      return this.authorTarget;
    }

    /**
     * @param value {@link #author} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The person or device that created the alert.)
     */
    public Alert setAuthorTarget(Resource value) { 
      this.authorTarget = value;
      return this;
    }

    /**
     * @return {@link #note} (The textual component of the alert to display to the user.). This is the underlying object with id, value and extensions. The accessor "getNote" gives direct access to the value
     */
    public StringType getNoteElement() { 
      if (this.note == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Alert.note");
        else if (Configuration.doAutoCreate())
          this.note = new StringType();
      return this.note;
    }

    public boolean hasNoteElement() { 
      return this.note != null && !this.note.isEmpty();
    }

    public boolean hasNote() { 
      return this.note != null && !this.note.isEmpty();
    }

    /**
     * @param value {@link #note} (The textual component of the alert to display to the user.). This is the underlying object with id, value and extensions. The accessor "getNote" gives direct access to the value
     */
    public Alert setNoteElement(StringType value) { 
      this.note = value;
      return this;
    }

    /**
     * @return The textual component of the alert to display to the user.
     */
    public String getNote() { 
      return this.note == null ? null : this.note.getValue();
    }

    /**
     * @param value The textual component of the alert to display to the user.
     */
    public Alert setNote(String value) { 
        if (this.note == null)
          this.note = new StringType();
        this.note.setValue(value);
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "Identifier assigned to the alert for external use (outside the FHIR environment).", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("category", "CodeableConcept", "Allows an alert to be divided into different categories like clinical, administrative etc.", 0, java.lang.Integer.MAX_VALUE, category));
        childrenList.add(new Property("status", "code", "Supports basic workflow.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("subject", "Reference(Patient)", "The person who this alert concerns.", 0, java.lang.Integer.MAX_VALUE, subject));
        childrenList.add(new Property("author", "Reference(Practitioner|Patient|Device)", "The person or device that created the alert.", 0, java.lang.Integer.MAX_VALUE, author));
        childrenList.add(new Property("note", "string", "The textual component of the alert to display to the user.", 0, java.lang.Integer.MAX_VALUE, note));
      }

      public Alert copy() {
        Alert dst = new Alert();
        copyValues(dst);
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        dst.category = category == null ? null : category.copy();
        dst.status = status == null ? null : status.copy();
        dst.subject = subject == null ? null : subject.copy();
        dst.author = author == null ? null : author.copy();
        dst.note = note == null ? null : note.copy();
        return dst;
      }

      protected Alert typedCopy() {
        return copy();
      }

      public boolean isEmpty() {
        return super.isEmpty() && (identifier == null || identifier.isEmpty()) && (category == null || category.isEmpty())
           && (status == null || status.isEmpty()) && (subject == null || subject.isEmpty()) && (author == null || author.isEmpty())
           && (note == null || note.isEmpty());
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.Alert;
   }

  @SearchParamDefinition(name="patient", path="Alert.subject", description="The identity of a subject to list alerts for", type="reference" )
  public static final String SP_PATIENT = "patient";
  @SearchParamDefinition(name="subject", path="Alert.subject", description="The identity of a subject to list alerts for", type="reference" )
  public static final String SP_SUBJECT = "subject";

}

