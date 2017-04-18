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
import java.util.List;

import org.hl7.fhir.instance.model.api.IBaseHasExtensions;
import org.hl7.fhir.instance.model.api.IBaseHasModifierExtensions;
import org.hl7.fhir.instance.model.api.IDomainResource;

import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
/**
 * A resource that includes narrative, extensions, and contained resources.
 */
public abstract class DomainResource extends Resource implements IBaseHasExtensions, IBaseHasModifierExtensions, IDomainResource {

    /**
     * A human-readable narrative that contains a summary of the resource, and may be used to represent the content of the resource to a human. The narrative need not encode all the structured data, but is required to contain sufficient detail to make it "clinically safe" for a human to just read the narrative. Resource definitions may define what content should be represented in the narrative to ensure clinical safety.
     */
    @Child(name = "text", type = {Narrative.class}, order=0, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Text summary of the resource, for human interpretation", formalDefinition="A human-readable narrative that contains a summary of the resource, and may be used to represent the content of the resource to a human. The narrative need not encode all the structured data, but is required to contain sufficient detail to make it \"clinically safe\" for a human to just read the narrative. Resource definitions may define what content should be represented in the narrative to ensure clinical safety." )
    protected Narrative text;

    /**
     * These resources do not have an independent existence apart from the resource that contains them - they cannot be identified independently, and nor can they have their own independent transaction scope.
     */
    @Child(name = "contained", type = {Resource.class}, order=1, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Contained, inline Resources", formalDefinition="These resources do not have an independent existence apart from the resource that contains them - they cannot be identified independently, and nor can they have their own independent transaction scope." )
    protected List<Resource> contained;

    /**
     * May be used to represent additional information that is not part of the basic definition of the resource. In order to make the use of extensions safe and manageable, there is a strict set of governance  applied to the definition and use of extensions. Though any implementer is allowed to define an extension, there is a set of requirements that SHALL be met as part of the definition of the extension.
     */
    @Child(name = "extension", type = {Extension.class}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Additional Content defined by implementations", formalDefinition="May be used to represent additional information that is not part of the basic definition of the resource. In order to make the use of extensions safe and manageable, there is a strict set of governance  applied to the definition and use of extensions. Though any implementer is allowed to define an extension, there is a set of requirements that SHALL be met as part of the definition of the extension." )
    protected List<Extension> extension;

    /**
     * May be used to represent additional information that is not part of the basic definition of the resource, and that modifies the understanding of the element that contains it. Usually modifier elements provide negation or qualification. In order to make the use of extensions safe and manageable, there is a strict set of governance applied to the definition and use of extensions. Though any implementer is allowed to define an extension, there is a set of requirements that SHALL be met as part of the definition of the extension. Applications processing a resource are required to check for modifier extensions.
     */
    @Child(name = "modifierExtension", type = {Extension.class}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=true, summary=false)
    @Description(shortDefinition="Extensions that cannot be ignored", formalDefinition="May be used to represent additional information that is not part of the basic definition of the resource, and that modifies the understanding of the element that contains it. Usually modifier elements provide negation or qualification. In order to make the use of extensions safe and manageable, there is a strict set of governance applied to the definition and use of extensions. Though any implementer is allowed to define an extension, there is a set of requirements that SHALL be met as part of the definition of the extension. Applications processing a resource are required to check for modifier extensions." )
    protected List<Extension> modifierExtension;

    private static final long serialVersionUID = -970285559L;

  /*
   * Constructor
   */
    public DomainResource() {
      super();
    }

    /**
     * @return {@link #text} (A human-readable narrative that contains a summary of the resource, and may be used to represent the content of the resource to a human. The narrative need not encode all the structured data, but is required to contain sufficient detail to make it "clinically safe" for a human to just read the narrative. Resource definitions may define what content should be represented in the narrative to ensure clinical safety.)
     */
    public Narrative getText() { 
      if (this.text == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DomainResource.text");
        else if (Configuration.doAutoCreate())
          this.text = new Narrative(); // cc
      return this.text;
    }

    public boolean hasText() { 
      return this.text != null && !this.text.isEmpty();
    }

    /**
     * @param value {@link #text} (A human-readable narrative that contains a summary of the resource, and may be used to represent the content of the resource to a human. The narrative need not encode all the structured data, but is required to contain sufficient detail to make it "clinically safe" for a human to just read the narrative. Resource definitions may define what content should be represented in the narrative to ensure clinical safety.)
     */
    public DomainResource setText(Narrative value) { 
      this.text = value;
      return this;
    }

    /**
     * @return {@link #contained} (These resources do not have an independent existence apart from the resource that contains them - they cannot be identified independently, and nor can they have their own independent transaction scope.)
     */
    public List<Resource> getContained() { 
      if (this.contained == null)
        this.contained = new ArrayList<Resource>();
      return this.contained;
    }

    public boolean hasContained() { 
      if (this.contained == null)
        return false;
      for (Resource item : this.contained)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #contained} (These resources do not have an independent existence apart from the resource that contains them - they cannot be identified independently, and nor can they have their own independent transaction scope.)
     */
    /**
     * @return {@link #extension} (May be used to represent additional information that is not part of the basic definition of the resource. In order to make the use of extensions safe and manageable, there is a strict set of governance  applied to the definition and use of extensions. Though any implementer is allowed to define an extension, there is a set of requirements that SHALL be met as part of the definition of the extension.)
     */
    public List<Extension> getExtension() { 
      if (this.extension == null)
        this.extension = new ArrayList<Extension>();
      return this.extension;
    }

    public boolean hasExtension() { 
      if (this.extension == null)
        return false;
      for (Extension item : this.extension)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #extension} (May be used to represent additional information that is not part of the basic definition of the resource. In order to make the use of extensions safe and manageable, there is a strict set of governance  applied to the definition and use of extensions. Though any implementer is allowed to define an extension, there is a set of requirements that SHALL be met as part of the definition of the extension.)
     */
    // syntactic sugar
    public Extension addExtension() { //3
      Extension t = new Extension();
      if (this.extension == null)
        this.extension = new ArrayList<Extension>();
      this.extension.add(t);
      return t;
    }

    // syntactic sugar
    public DomainResource addExtension(Extension t) { //3
      if (t == null)
        return this;
      if (this.extension == null)
        this.extension = new ArrayList<Extension>();
      this.extension.add(t);
      return this;
    }

    /**
     * @return {@link #modifierExtension} (May be used to represent additional information that is not part of the basic definition of the resource, and that modifies the understanding of the element that contains it. Usually modifier elements provide negation or qualification. In order to make the use of extensions safe and manageable, there is a strict set of governance applied to the definition and use of extensions. Though any implementer is allowed to define an extension, there is a set of requirements that SHALL be met as part of the definition of the extension. Applications processing a resource are required to check for modifier extensions.)
     */
    public List<Extension> getModifierExtension() { 
      if (this.modifierExtension == null)
        this.modifierExtension = new ArrayList<Extension>();
      return this.modifierExtension;
    }

    public boolean hasModifierExtension() { 
      if (this.modifierExtension == null)
        return false;
      for (Extension item : this.modifierExtension)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #modifierExtension} (May be used to represent additional information that is not part of the basic definition of the resource, and that modifies the understanding of the element that contains it. Usually modifier elements provide negation or qualification. In order to make the use of extensions safe and manageable, there is a strict set of governance applied to the definition and use of extensions. Though any implementer is allowed to define an extension, there is a set of requirements that SHALL be met as part of the definition of the extension. Applications processing a resource are required to check for modifier extensions.)
     */
    // syntactic sugar
    public Extension addModifierExtension() { //3
      Extension t = new Extension();
      if (this.modifierExtension == null)
        this.modifierExtension = new ArrayList<Extension>();
      this.modifierExtension.add(t);
      return t;
    }

    // syntactic sugar
    public DomainResource addModifierExtension(Extension t) { //3
      if (t == null)
        return this;
      if (this.modifierExtension == null)
        this.modifierExtension = new ArrayList<Extension>();
      this.modifierExtension.add(t);
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        childrenList.add(new Property("text", "Narrative", "A human-readable narrative that contains a summary of the resource, and may be used to represent the content of the resource to a human. The narrative need not encode all the structured data, but is required to contain sufficient detail to make it \"clinically safe\" for a human to just read the narrative. Resource definitions may define what content should be represented in the narrative to ensure clinical safety.", 0, java.lang.Integer.MAX_VALUE, text));
        childrenList.add(new Property("contained", "Resource", "These resources do not have an independent existence apart from the resource that contains them - they cannot be identified independently, and nor can they have their own independent transaction scope.", 0, java.lang.Integer.MAX_VALUE, contained));
        childrenList.add(new Property("extension", "Extension", "May be used to represent additional information that is not part of the basic definition of the resource. In order to make the use of extensions safe and manageable, there is a strict set of governance  applied to the definition and use of extensions. Though any implementer is allowed to define an extension, there is a set of requirements that SHALL be met as part of the definition of the extension.", 0, java.lang.Integer.MAX_VALUE, extension));
        childrenList.add(new Property("modifierExtension", "Extension", "May be used to represent additional information that is not part of the basic definition of the resource, and that modifies the understanding of the element that contains it. Usually modifier elements provide negation or qualification. In order to make the use of extensions safe and manageable, there is a strict set of governance applied to the definition and use of extensions. Though any implementer is allowed to define an extension, there is a set of requirements that SHALL be met as part of the definition of the extension. Applications processing a resource are required to check for modifier extensions.", 0, java.lang.Integer.MAX_VALUE, modifierExtension));
      }

      public abstract DomainResource copy();

      public void copyValues(DomainResource dst) {
        dst.text = text == null ? null : text.copy();
        if (contained != null) {
          dst.contained = new ArrayList<Resource>();
          for (Resource i : contained)
            dst.contained.add(i.copy());
        };
        if (extension != null) {
          dst.extension = new ArrayList<Extension>();
          for (Extension i : extension)
            dst.extension.add(i.copy());
        };
        if (modifierExtension != null) {
          dst.modifierExtension = new ArrayList<Extension>();
          for (Extension i : modifierExtension)
            dst.modifierExtension.add(i.copy());
        };
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof DomainResource))
          return false;
        DomainResource o = (DomainResource) other;
        return compareDeep(text, o.text, true) && compareDeep(contained, o.contained, true) && compareDeep(extension, o.extension, true)
           && compareDeep(modifierExtension, o.modifierExtension, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof DomainResource))
          return false;
        DomainResource o = (DomainResource) other;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (text == null || text.isEmpty()) && (contained == null || contained.isEmpty())
           && (extension == null || extension.isEmpty()) && (modifierExtension == null || modifierExtension.isEmpty())
          ;
      }

      public void addContained(Resource theResource) {
        getContained().add(theResource);
      }


}

