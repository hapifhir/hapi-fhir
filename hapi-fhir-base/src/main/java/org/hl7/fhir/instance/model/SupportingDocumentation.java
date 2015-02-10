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

import org.hl7.fhir.instance.model.annotations.ResourceDef;
import org.hl7.fhir.instance.model.annotations.SearchParamDefinition;
import org.hl7.fhir.instance.model.annotations.Block;
import org.hl7.fhir.instance.model.annotations.Child;
import org.hl7.fhir.instance.model.annotations.Description;
/**
 * This resource provides the supporting information for a process, for example clinical or financial  information related to a claim or pre-authorization.
 */
@ResourceDef(name="SupportingDocumentation", profile="http://hl7.org/fhir/Profile/SupportingDocumentation")
public class SupportingDocumentation extends DomainResource {

    @Block()
    public static class SupportingDocumentationDetailComponent extends BackboneElement {
        /**
         * A link Id for the response to reference.
         */
        @Child(name="linkId", type={IntegerType.class}, order=1, min=1, max=1)
        @Description(shortDefinition="LinkId", formalDefinition="A link Id for the response to reference." )
        protected IntegerType linkId;

        /**
         * The attached content.
         */
        @Child(name="content", type={Attachment.class}, order=2, min=1, max=1)
        @Description(shortDefinition="Content", formalDefinition="The attached content." )
        protected Type content;

        /**
         * The date and optionally time when the material was created.
         */
        @Child(name="dateTime", type={DateTimeType.class}, order=3, min=0, max=1)
        @Description(shortDefinition="Creation date and time", formalDefinition="The date and optionally time when the material was created." )
        protected DateTimeType dateTime;

        private static final long serialVersionUID = -132176946L;

      public SupportingDocumentationDetailComponent() {
        super();
      }

      public SupportingDocumentationDetailComponent(IntegerType linkId, Type content) {
        super();
        this.linkId = linkId;
        this.content = content;
      }

        /**
         * @return {@link #linkId} (A link Id for the response to reference.). This is the underlying object with id, value and extensions. The accessor "getLinkId" gives direct access to the value
         */
        public IntegerType getLinkIdElement() { 
          if (this.linkId == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SupportingDocumentationDetailComponent.linkId");
            else if (Configuration.doAutoCreate())
              this.linkId = new IntegerType();
          return this.linkId;
        }

        public boolean hasLinkIdElement() { 
          return this.linkId != null && !this.linkId.isEmpty();
        }

        public boolean hasLinkId() { 
          return this.linkId != null && !this.linkId.isEmpty();
        }

        /**
         * @param value {@link #linkId} (A link Id for the response to reference.). This is the underlying object with id, value and extensions. The accessor "getLinkId" gives direct access to the value
         */
        public SupportingDocumentationDetailComponent setLinkIdElement(IntegerType value) { 
          this.linkId = value;
          return this;
        }

        /**
         * @return A link Id for the response to reference.
         */
        public int getLinkId() { 
          return this.linkId == null ? null : this.linkId.getValue();
        }

        /**
         * @param value A link Id for the response to reference.
         */
        public SupportingDocumentationDetailComponent setLinkId(int value) { 
            if (this.linkId == null)
              this.linkId = new IntegerType();
            this.linkId.setValue(value);
          return this;
        }

        /**
         * @return {@link #content} (The attached content.)
         */
        public Type getContent() { 
          return this.content;
        }

        /**
         * @return {@link #content} (The attached content.)
         */
        public Reference getContentReference() throws Exception { 
          if (!(this.content instanceof Reference))
            throw new Exception("Type mismatch: the type Reference was expected, but "+this.content.getClass().getName()+" was encountered");
          return (Reference) this.content;
        }

        /**
         * @return {@link #content} (The attached content.)
         */
        public Attachment getContentAttachment() throws Exception { 
          if (!(this.content instanceof Attachment))
            throw new Exception("Type mismatch: the type Attachment was expected, but "+this.content.getClass().getName()+" was encountered");
          return (Attachment) this.content;
        }

        public boolean hasContent() { 
          return this.content != null && !this.content.isEmpty();
        }

        /**
         * @param value {@link #content} (The attached content.)
         */
        public SupportingDocumentationDetailComponent setContent(Type value) { 
          this.content = value;
          return this;
        }

        /**
         * @return {@link #dateTime} (The date and optionally time when the material was created.). This is the underlying object with id, value and extensions. The accessor "getDateTime" gives direct access to the value
         */
        public DateTimeType getDateTimeElement() { 
          if (this.dateTime == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SupportingDocumentationDetailComponent.dateTime");
            else if (Configuration.doAutoCreate())
              this.dateTime = new DateTimeType();
          return this.dateTime;
        }

        public boolean hasDateTimeElement() { 
          return this.dateTime != null && !this.dateTime.isEmpty();
        }

        public boolean hasDateTime() { 
          return this.dateTime != null && !this.dateTime.isEmpty();
        }

        /**
         * @param value {@link #dateTime} (The date and optionally time when the material was created.). This is the underlying object with id, value and extensions. The accessor "getDateTime" gives direct access to the value
         */
        public SupportingDocumentationDetailComponent setDateTimeElement(DateTimeType value) { 
          this.dateTime = value;
          return this;
        }

        /**
         * @return The date and optionally time when the material was created.
         */
        public Date getDateTime() { 
          return this.dateTime == null ? null : this.dateTime.getValue();
        }

        /**
         * @param value The date and optionally time when the material was created.
         */
        public SupportingDocumentationDetailComponent setDateTime(Date value) { 
          if (value == null)
            this.dateTime = null;
          else {
            if (this.dateTime == null)
              this.dateTime = new DateTimeType();
            this.dateTime.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("linkId", "integer", "A link Id for the response to reference.", 0, java.lang.Integer.MAX_VALUE, linkId));
          childrenList.add(new Property("content[x]", "Reference(Any)|Attachment", "The attached content.", 0, java.lang.Integer.MAX_VALUE, content));
          childrenList.add(new Property("dateTime", "dateTime", "The date and optionally time when the material was created.", 0, java.lang.Integer.MAX_VALUE, dateTime));
        }

      public SupportingDocumentationDetailComponent copy() {
        SupportingDocumentationDetailComponent dst = new SupportingDocumentationDetailComponent();
        copyValues(dst);
        dst.linkId = linkId == null ? null : linkId.copy();
        dst.content = content == null ? null : content.copy();
        dst.dateTime = dateTime == null ? null : dateTime.copy();
        return dst;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (linkId == null || linkId.isEmpty()) && (content == null || content.isEmpty())
           && (dateTime == null || dateTime.isEmpty());
      }

  }

    /**
     * The Response Business Identifier.
     */
    @Child(name="identifier", type={Identifier.class}, order=-1, min=1, max=Child.MAX_UNLIMITED)
    @Description(shortDefinition="Business Identifier", formalDefinition="The Response Business Identifier." )
    protected List<Identifier> identifier;

    /**
     * The version of the style of resource contents. This should be mapped to the allowable profiles for this and supporting resources.
     */
    @Child(name="ruleset", type={Coding.class}, order=0, min=0, max=1)
    @Description(shortDefinition="Resource version", formalDefinition="The version of the style of resource contents. This should be mapped to the allowable profiles for this and supporting resources." )
    protected Coding ruleset;

    /**
     * The style (standard) and version of the original material which was converted into this resource.
     */
    @Child(name="originalRuleset", type={Coding.class}, order=1, min=0, max=1)
    @Description(shortDefinition="Original version", formalDefinition="The style (standard) and version of the original material which was converted into this resource." )
    protected Coding originalRuleset;

    /**
     * The date when this resource was created.
     */
    @Child(name="created", type={DateTimeType.class}, order=2, min=0, max=1)
    @Description(shortDefinition="Creation date", formalDefinition="The date when this resource was created." )
    protected DateTimeType created;

    /**
     * The Insurer, organization or Provider who is target  of the submission.
     */
    @Child(name="target", type={Organization.class, Practitioner.class}, order=3, min=0, max=1)
    @Description(shortDefinition="Insurer or Provider", formalDefinition="The Insurer, organization or Provider who is target  of the submission." )
    protected Reference target;

    /**
     * The actual object that is the target of the reference (The Insurer, organization or Provider who is target  of the submission.)
     */
    protected Resource targetTarget;

    /**
     * The practitioner who is responsible for the services rendered to the patient.
     */
    @Child(name="provider", type={Practitioner.class}, order=4, min=0, max=1)
    @Description(shortDefinition="Responsible practitioner", formalDefinition="The practitioner who is responsible for the services rendered to the patient." )
    protected Reference provider;

    /**
     * The actual object that is the target of the reference (The practitioner who is responsible for the services rendered to the patient.)
     */
    protected Practitioner providerTarget;

    /**
     * The organization which is responsible for the services rendered to the patient.
     */
    @Child(name="organization", type={Organization.class}, order=5, min=0, max=1)
    @Description(shortDefinition="Responsible organization", formalDefinition="The organization which is responsible for the services rendered to the patient." )
    protected Reference organization;

    /**
     * The actual object that is the target of the reference (The organization which is responsible for the services rendered to the patient.)
     */
    protected Organization organizationTarget;

    /**
     * Original request identifer.
     */
    @Child(name="request", type={}, order=6, min=0, max=1)
    @Description(shortDefinition="Request reference", formalDefinition="Original request identifer." )
    protected Reference request;

    /**
     * The actual object that is the target of the reference (Original request identifer.)
     */
    protected Resource requestTarget;

    /**
     * Original response identifer.
     */
    @Child(name="response", type={}, order=7, min=0, max=1)
    @Description(shortDefinition="Response reference", formalDefinition="Original response identifer." )
    protected Reference response;

    /**
     * The actual object that is the target of the reference (Original response identifer.)
     */
    protected Resource responseTarget;

    /**
     * Person who created the submission.
     */
    @Child(name="author", type={Practitioner.class}, order=8, min=0, max=1)
    @Description(shortDefinition="Author", formalDefinition="Person who created the submission." )
    protected Reference author;

    /**
     * The actual object that is the target of the reference (Person who created the submission.)
     */
    protected Practitioner authorTarget;

    /**
     * The patient who is directly or indirectly the subject of the supporting information.
     */
    @Child(name="subject", type={Patient.class}, order=9, min=0, max=1)
    @Description(shortDefinition="Patient", formalDefinition="The patient who is directly or indirectly the subject of the supporting information." )
    protected Reference subject;

    /**
     * The actual object that is the target of the reference (The patient who is directly or indirectly the subject of the supporting information.)
     */
    protected Patient subjectTarget;

    /**
     * Supporting Files.
     */
    @Child(name="detail", type={}, order=10, min=0, max=Child.MAX_UNLIMITED)
    @Description(shortDefinition="Supporting Files", formalDefinition="Supporting Files." )
    protected List<SupportingDocumentationDetailComponent> detail;

    private static final long serialVersionUID = -1353519836L;

    public SupportingDocumentation() {
      super();
    }

    /**
     * @return {@link #identifier} (The Response Business Identifier.)
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
     * @return {@link #identifier} (The Response Business Identifier.)
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
     * @return {@link #ruleset} (The version of the style of resource contents. This should be mapped to the allowable profiles for this and supporting resources.)
     */
    public Coding getRuleset() { 
      if (this.ruleset == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create SupportingDocumentation.ruleset");
        else if (Configuration.doAutoCreate())
          this.ruleset = new Coding();
      return this.ruleset;
    }

    public boolean hasRuleset() { 
      return this.ruleset != null && !this.ruleset.isEmpty();
    }

    /**
     * @param value {@link #ruleset} (The version of the style of resource contents. This should be mapped to the allowable profiles for this and supporting resources.)
     */
    public SupportingDocumentation setRuleset(Coding value) { 
      this.ruleset = value;
      return this;
    }

    /**
     * @return {@link #originalRuleset} (The style (standard) and version of the original material which was converted into this resource.)
     */
    public Coding getOriginalRuleset() { 
      if (this.originalRuleset == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create SupportingDocumentation.originalRuleset");
        else if (Configuration.doAutoCreate())
          this.originalRuleset = new Coding();
      return this.originalRuleset;
    }

    public boolean hasOriginalRuleset() { 
      return this.originalRuleset != null && !this.originalRuleset.isEmpty();
    }

    /**
     * @param value {@link #originalRuleset} (The style (standard) and version of the original material which was converted into this resource.)
     */
    public SupportingDocumentation setOriginalRuleset(Coding value) { 
      this.originalRuleset = value;
      return this;
    }

    /**
     * @return {@link #created} (The date when this resource was created.). This is the underlying object with id, value and extensions. The accessor "getCreated" gives direct access to the value
     */
    public DateTimeType getCreatedElement() { 
      if (this.created == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create SupportingDocumentation.created");
        else if (Configuration.doAutoCreate())
          this.created = new DateTimeType();
      return this.created;
    }

    public boolean hasCreatedElement() { 
      return this.created != null && !this.created.isEmpty();
    }

    public boolean hasCreated() { 
      return this.created != null && !this.created.isEmpty();
    }

    /**
     * @param value {@link #created} (The date when this resource was created.). This is the underlying object with id, value and extensions. The accessor "getCreated" gives direct access to the value
     */
    public SupportingDocumentation setCreatedElement(DateTimeType value) { 
      this.created = value;
      return this;
    }

    /**
     * @return The date when this resource was created.
     */
    public Date getCreated() { 
      return this.created == null ? null : this.created.getValue();
    }

    /**
     * @param value The date when this resource was created.
     */
    public SupportingDocumentation setCreated(Date value) { 
      if (value == null)
        this.created = null;
      else {
        if (this.created == null)
          this.created = new DateTimeType();
        this.created.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #target} (The Insurer, organization or Provider who is target  of the submission.)
     */
    public Reference getTarget() { 
      if (this.target == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create SupportingDocumentation.target");
        else if (Configuration.doAutoCreate())
          this.target = new Reference();
      return this.target;
    }

    public boolean hasTarget() { 
      return this.target != null && !this.target.isEmpty();
    }

    /**
     * @param value {@link #target} (The Insurer, organization or Provider who is target  of the submission.)
     */
    public SupportingDocumentation setTarget(Reference value) { 
      this.target = value;
      return this;
    }

    /**
     * @return {@link #target} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The Insurer, organization or Provider who is target  of the submission.)
     */
    public Resource getTargetTarget() { 
      return this.targetTarget;
    }

    /**
     * @param value {@link #target} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The Insurer, organization or Provider who is target  of the submission.)
     */
    public SupportingDocumentation setTargetTarget(Resource value) { 
      this.targetTarget = value;
      return this;
    }

    /**
     * @return {@link #provider} (The practitioner who is responsible for the services rendered to the patient.)
     */
    public Reference getProvider() { 
      if (this.provider == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create SupportingDocumentation.provider");
        else if (Configuration.doAutoCreate())
          this.provider = new Reference();
      return this.provider;
    }

    public boolean hasProvider() { 
      return this.provider != null && !this.provider.isEmpty();
    }

    /**
     * @param value {@link #provider} (The practitioner who is responsible for the services rendered to the patient.)
     */
    public SupportingDocumentation setProvider(Reference value) { 
      this.provider = value;
      return this;
    }

    /**
     * @return {@link #provider} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The practitioner who is responsible for the services rendered to the patient.)
     */
    public Practitioner getProviderTarget() { 
      if (this.providerTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create SupportingDocumentation.provider");
        else if (Configuration.doAutoCreate())
          this.providerTarget = new Practitioner();
      return this.providerTarget;
    }

    /**
     * @param value {@link #provider} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The practitioner who is responsible for the services rendered to the patient.)
     */
    public SupportingDocumentation setProviderTarget(Practitioner value) { 
      this.providerTarget = value;
      return this;
    }

    /**
     * @return {@link #organization} (The organization which is responsible for the services rendered to the patient.)
     */
    public Reference getOrganization() { 
      if (this.organization == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create SupportingDocumentation.organization");
        else if (Configuration.doAutoCreate())
          this.organization = new Reference();
      return this.organization;
    }

    public boolean hasOrganization() { 
      return this.organization != null && !this.organization.isEmpty();
    }

    /**
     * @param value {@link #organization} (The organization which is responsible for the services rendered to the patient.)
     */
    public SupportingDocumentation setOrganization(Reference value) { 
      this.organization = value;
      return this;
    }

    /**
     * @return {@link #organization} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The organization which is responsible for the services rendered to the patient.)
     */
    public Organization getOrganizationTarget() { 
      if (this.organizationTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create SupportingDocumentation.organization");
        else if (Configuration.doAutoCreate())
          this.organizationTarget = new Organization();
      return this.organizationTarget;
    }

    /**
     * @param value {@link #organization} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The organization which is responsible for the services rendered to the patient.)
     */
    public SupportingDocumentation setOrganizationTarget(Organization value) { 
      this.organizationTarget = value;
      return this;
    }

    /**
     * @return {@link #request} (Original request identifer.)
     */
    public Reference getRequest() { 
      if (this.request == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create SupportingDocumentation.request");
        else if (Configuration.doAutoCreate())
          this.request = new Reference();
      return this.request;
    }

    public boolean hasRequest() { 
      return this.request != null && !this.request.isEmpty();
    }

    /**
     * @param value {@link #request} (Original request identifer.)
     */
    public SupportingDocumentation setRequest(Reference value) { 
      this.request = value;
      return this;
    }

    /**
     * @return {@link #request} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Original request identifer.)
     */
    public Resource getRequestTarget() { 
      return this.requestTarget;
    }

    /**
     * @param value {@link #request} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Original request identifer.)
     */
    public SupportingDocumentation setRequestTarget(Resource value) { 
      this.requestTarget = value;
      return this;
    }

    /**
     * @return {@link #response} (Original response identifer.)
     */
    public Reference getResponse() { 
      if (this.response == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create SupportingDocumentation.response");
        else if (Configuration.doAutoCreate())
          this.response = new Reference();
      return this.response;
    }

    public boolean hasResponse() { 
      return this.response != null && !this.response.isEmpty();
    }

    /**
     * @param value {@link #response} (Original response identifer.)
     */
    public SupportingDocumentation setResponse(Reference value) { 
      this.response = value;
      return this;
    }

    /**
     * @return {@link #response} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Original response identifer.)
     */
    public Resource getResponseTarget() { 
      return this.responseTarget;
    }

    /**
     * @param value {@link #response} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Original response identifer.)
     */
    public SupportingDocumentation setResponseTarget(Resource value) { 
      this.responseTarget = value;
      return this;
    }

    /**
     * @return {@link #author} (Person who created the submission.)
     */
    public Reference getAuthor() { 
      if (this.author == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create SupportingDocumentation.author");
        else if (Configuration.doAutoCreate())
          this.author = new Reference();
      return this.author;
    }

    public boolean hasAuthor() { 
      return this.author != null && !this.author.isEmpty();
    }

    /**
     * @param value {@link #author} (Person who created the submission.)
     */
    public SupportingDocumentation setAuthor(Reference value) { 
      this.author = value;
      return this;
    }

    /**
     * @return {@link #author} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Person who created the submission.)
     */
    public Practitioner getAuthorTarget() { 
      if (this.authorTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create SupportingDocumentation.author");
        else if (Configuration.doAutoCreate())
          this.authorTarget = new Practitioner();
      return this.authorTarget;
    }

    /**
     * @param value {@link #author} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Person who created the submission.)
     */
    public SupportingDocumentation setAuthorTarget(Practitioner value) { 
      this.authorTarget = value;
      return this;
    }

    /**
     * @return {@link #subject} (The patient who is directly or indirectly the subject of the supporting information.)
     */
    public Reference getSubject() { 
      if (this.subject == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create SupportingDocumentation.subject");
        else if (Configuration.doAutoCreate())
          this.subject = new Reference();
      return this.subject;
    }

    public boolean hasSubject() { 
      return this.subject != null && !this.subject.isEmpty();
    }

    /**
     * @param value {@link #subject} (The patient who is directly or indirectly the subject of the supporting information.)
     */
    public SupportingDocumentation setSubject(Reference value) { 
      this.subject = value;
      return this;
    }

    /**
     * @return {@link #subject} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The patient who is directly or indirectly the subject of the supporting information.)
     */
    public Patient getSubjectTarget() { 
      if (this.subjectTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create SupportingDocumentation.subject");
        else if (Configuration.doAutoCreate())
          this.subjectTarget = new Patient();
      return this.subjectTarget;
    }

    /**
     * @param value {@link #subject} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The patient who is directly or indirectly the subject of the supporting information.)
     */
    public SupportingDocumentation setSubjectTarget(Patient value) { 
      this.subjectTarget = value;
      return this;
    }

    /**
     * @return {@link #detail} (Supporting Files.)
     */
    public List<SupportingDocumentationDetailComponent> getDetail() { 
      if (this.detail == null)
        this.detail = new ArrayList<SupportingDocumentationDetailComponent>();
      return this.detail;
    }

    public boolean hasDetail() { 
      if (this.detail == null)
        return false;
      for (SupportingDocumentationDetailComponent item : this.detail)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #detail} (Supporting Files.)
     */
    // syntactic sugar
    public SupportingDocumentationDetailComponent addDetail() { //3
      SupportingDocumentationDetailComponent t = new SupportingDocumentationDetailComponent();
      if (this.detail == null)
        this.detail = new ArrayList<SupportingDocumentationDetailComponent>();
      this.detail.add(t);
      return t;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "The Response Business Identifier.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("ruleset", "Coding", "The version of the style of resource contents. This should be mapped to the allowable profiles for this and supporting resources.", 0, java.lang.Integer.MAX_VALUE, ruleset));
        childrenList.add(new Property("originalRuleset", "Coding", "The style (standard) and version of the original material which was converted into this resource.", 0, java.lang.Integer.MAX_VALUE, originalRuleset));
        childrenList.add(new Property("created", "dateTime", "The date when this resource was created.", 0, java.lang.Integer.MAX_VALUE, created));
        childrenList.add(new Property("target", "Reference(Organization|Practitioner)", "The Insurer, organization or Provider who is target  of the submission.", 0, java.lang.Integer.MAX_VALUE, target));
        childrenList.add(new Property("provider", "Reference(Practitioner)", "The practitioner who is responsible for the services rendered to the patient.", 0, java.lang.Integer.MAX_VALUE, provider));
        childrenList.add(new Property("organization", "Reference(Organization)", "The organization which is responsible for the services rendered to the patient.", 0, java.lang.Integer.MAX_VALUE, organization));
        childrenList.add(new Property("request", "Reference(Any)", "Original request identifer.", 0, java.lang.Integer.MAX_VALUE, request));
        childrenList.add(new Property("response", "Reference(Any)", "Original response identifer.", 0, java.lang.Integer.MAX_VALUE, response));
        childrenList.add(new Property("author", "Reference(Practitioner)", "Person who created the submission.", 0, java.lang.Integer.MAX_VALUE, author));
        childrenList.add(new Property("subject", "Reference(Patient)", "The patient who is directly or indirectly the subject of the supporting information.", 0, java.lang.Integer.MAX_VALUE, subject));
        childrenList.add(new Property("detail", "", "Supporting Files.", 0, java.lang.Integer.MAX_VALUE, detail));
      }

      public SupportingDocumentation copy() {
        SupportingDocumentation dst = new SupportingDocumentation();
        copyValues(dst);
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        dst.ruleset = ruleset == null ? null : ruleset.copy();
        dst.originalRuleset = originalRuleset == null ? null : originalRuleset.copy();
        dst.created = created == null ? null : created.copy();
        dst.target = target == null ? null : target.copy();
        dst.provider = provider == null ? null : provider.copy();
        dst.organization = organization == null ? null : organization.copy();
        dst.request = request == null ? null : request.copy();
        dst.response = response == null ? null : response.copy();
        dst.author = author == null ? null : author.copy();
        dst.subject = subject == null ? null : subject.copy();
        if (detail != null) {
          dst.detail = new ArrayList<SupportingDocumentationDetailComponent>();
          for (SupportingDocumentationDetailComponent i : detail)
            dst.detail.add(i.copy());
        };
        return dst;
      }

      protected SupportingDocumentation typedCopy() {
        return copy();
      }

      public boolean isEmpty() {
        return super.isEmpty() && (identifier == null || identifier.isEmpty()) && (ruleset == null || ruleset.isEmpty())
           && (originalRuleset == null || originalRuleset.isEmpty()) && (created == null || created.isEmpty())
           && (target == null || target.isEmpty()) && (provider == null || provider.isEmpty()) && (organization == null || organization.isEmpty())
           && (request == null || request.isEmpty()) && (response == null || response.isEmpty()) && (author == null || author.isEmpty())
           && (subject == null || subject.isEmpty()) && (detail == null || detail.isEmpty());
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.SupportingDocumentation;
   }

  @SearchParamDefinition(name="author", path="SupportingDocumentation.author", description="The person who generated this resource", type="reference" )
  public static final String SP_AUTHOR = "author";
  @SearchParamDefinition(name="patient", path="SupportingDocumentation.subject", description="The patient to  whom the documents refer", type="reference" )
  public static final String SP_PATIENT = "patient";
  @SearchParamDefinition(name="subject", path="SupportingDocumentation.subject", description="The patient to  whom the documents refer", type="reference" )
  public static final String SP_SUBJECT = "subject";
  @SearchParamDefinition(name="identifier", path="SupportingDocumentation.identifier", description="The business identifier of the Eligibility", type="token" )
  public static final String SP_IDENTIFIER = "identifier";

}

