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
import java.util.List;

import org.hl7.fhir.exceptions.FHIRException;

import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
/**
 * This resource allows for the definition of an order set as a sharable, consumable, and executable artifact in support of clinical decision support.
 */
@ResourceDef(name="OrderSet", profile="http://hl7.org/fhir/Profile/OrderSet")
public class OrderSet extends DomainResource {

    /**
     * A reference to a ModuleMetadata resource containing metadata for the orderset.
     */
    @Child(name = "moduleMetadata", type = {ModuleMetadata.class}, order=0, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The metadata for the orderset", formalDefinition="A reference to a ModuleMetadata resource containing metadata for the orderset." )
    protected ModuleMetadata moduleMetadata;

    /**
     * A reference to a Library resource containing any formal logic used by the orderset.
     */
    @Child(name = "library", type = {Library.class}, order=1, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Logic used by the orderset", formalDefinition="A reference to a Library resource containing any formal logic used by the orderset." )
    protected List<Reference> library;
    /**
     * The actual objects that are the target of the reference (A reference to a Library resource containing any formal logic used by the orderset.)
     */
    protected List<Library> libraryTarget;


    /**
     * The definition of the actions that make up the order set. Order set groups and sections are represented as actions which contain sub-actions.
     */
    @Child(name = "action", type = {ActionDefinition.class}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Groups, sections, and line items of the order set", formalDefinition="The definition of the actions that make up the order set. Order set groups and sections are represented as actions which contain sub-actions." )
    protected List<ActionDefinition> action;

    private static final long serialVersionUID = -728217200L;

  /**
   * Constructor
   */
    public OrderSet() {
      super();
    }

    /**
     * @return {@link #moduleMetadata} (A reference to a ModuleMetadata resource containing metadata for the orderset.)
     */
    public ModuleMetadata getModuleMetadata() { 
      if (this.moduleMetadata == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create OrderSet.moduleMetadata");
        else if (Configuration.doAutoCreate())
          this.moduleMetadata = new ModuleMetadata(); // cc
      return this.moduleMetadata;
    }

    public boolean hasModuleMetadata() { 
      return this.moduleMetadata != null && !this.moduleMetadata.isEmpty();
    }

    /**
     * @param value {@link #moduleMetadata} (A reference to a ModuleMetadata resource containing metadata for the orderset.)
     */
    public OrderSet setModuleMetadata(ModuleMetadata value) { 
      this.moduleMetadata = value;
      return this;
    }

    /**
     * @return {@link #library} (A reference to a Library resource containing any formal logic used by the orderset.)
     */
    public List<Reference> getLibrary() { 
      if (this.library == null)
        this.library = new ArrayList<Reference>();
      return this.library;
    }

    public boolean hasLibrary() { 
      if (this.library == null)
        return false;
      for (Reference item : this.library)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #library} (A reference to a Library resource containing any formal logic used by the orderset.)
     */
    // syntactic sugar
    public Reference addLibrary() { //3
      Reference t = new Reference();
      if (this.library == null)
        this.library = new ArrayList<Reference>();
      this.library.add(t);
      return t;
    }

    // syntactic sugar
    public OrderSet addLibrary(Reference t) { //3
      if (t == null)
        return this;
      if (this.library == null)
        this.library = new ArrayList<Reference>();
      this.library.add(t);
      return this;
    }

    /**
     * @return {@link #library} (The actual objects that are the target of the reference. The reference library doesn't populate this, but you can use this to hold the resources if you resolvethemt. A reference to a Library resource containing any formal logic used by the orderset.)
     */
    public List<Library> getLibraryTarget() { 
      if (this.libraryTarget == null)
        this.libraryTarget = new ArrayList<Library>();
      return this.libraryTarget;
    }

    // syntactic sugar
    /**
     * @return {@link #library} (Add an actual object that is the target of the reference. The reference library doesn't use these, but you can use this to hold the resources if you resolvethemt. A reference to a Library resource containing any formal logic used by the orderset.)
     */
    public Library addLibraryTarget() { 
      Library r = new Library();
      if (this.libraryTarget == null)
        this.libraryTarget = new ArrayList<Library>();
      this.libraryTarget.add(r);
      return r;
    }

    /**
     * @return {@link #action} (The definition of the actions that make up the order set. Order set groups and sections are represented as actions which contain sub-actions.)
     */
    public List<ActionDefinition> getAction() { 
      if (this.action == null)
        this.action = new ArrayList<ActionDefinition>();
      return this.action;
    }

    public boolean hasAction() { 
      if (this.action == null)
        return false;
      for (ActionDefinition item : this.action)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #action} (The definition of the actions that make up the order set. Order set groups and sections are represented as actions which contain sub-actions.)
     */
    // syntactic sugar
    public ActionDefinition addAction() { //3
      ActionDefinition t = new ActionDefinition();
      if (this.action == null)
        this.action = new ArrayList<ActionDefinition>();
      this.action.add(t);
      return t;
    }

    // syntactic sugar
    public OrderSet addAction(ActionDefinition t) { //3
      if (t == null)
        return this;
      if (this.action == null)
        this.action = new ArrayList<ActionDefinition>();
      this.action.add(t);
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("moduleMetadata", "ModuleMetadata", "A reference to a ModuleMetadata resource containing metadata for the orderset.", 0, java.lang.Integer.MAX_VALUE, moduleMetadata));
        childrenList.add(new Property("library", "Reference(Library)", "A reference to a Library resource containing any formal logic used by the orderset.", 0, java.lang.Integer.MAX_VALUE, library));
        childrenList.add(new Property("action", "ActionDefinition", "The definition of the actions that make up the order set. Order set groups and sections are represented as actions which contain sub-actions.", 0, java.lang.Integer.MAX_VALUE, action));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 455891387: /*moduleMetadata*/ return this.moduleMetadata == null ? new Base[0] : new Base[] {this.moduleMetadata}; // ModuleMetadata
        case 166208699: /*library*/ return this.library == null ? new Base[0] : this.library.toArray(new Base[this.library.size()]); // Reference
        case -1422950858: /*action*/ return this.action == null ? new Base[0] : this.action.toArray(new Base[this.action.size()]); // ActionDefinition
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 455891387: // moduleMetadata
          this.moduleMetadata = castToModuleMetadata(value); // ModuleMetadata
          break;
        case 166208699: // library
          this.getLibrary().add(castToReference(value)); // Reference
          break;
        case -1422950858: // action
          this.getAction().add(castToActionDefinition(value)); // ActionDefinition
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("moduleMetadata"))
          this.moduleMetadata = castToModuleMetadata(value); // ModuleMetadata
        else if (name.equals("library"))
          this.getLibrary().add(castToReference(value));
        else if (name.equals("action"))
          this.getAction().add(castToActionDefinition(value));
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 455891387:  return getModuleMetadata(); // ModuleMetadata
        case 166208699:  return addLibrary(); // Reference
        case -1422950858:  return addAction(); // ActionDefinition
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("moduleMetadata")) {
          this.moduleMetadata = new ModuleMetadata();
          return this.moduleMetadata;
        }
        else if (name.equals("library")) {
          return addLibrary();
        }
        else if (name.equals("action")) {
          return addAction();
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "OrderSet";

  }

      public OrderSet copy() {
        OrderSet dst = new OrderSet();
        copyValues(dst);
        dst.moduleMetadata = moduleMetadata == null ? null : moduleMetadata.copy();
        if (library != null) {
          dst.library = new ArrayList<Reference>();
          for (Reference i : library)
            dst.library.add(i.copy());
        };
        if (action != null) {
          dst.action = new ArrayList<ActionDefinition>();
          for (ActionDefinition i : action)
            dst.action.add(i.copy());
        };
        return dst;
      }

      protected OrderSet typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof OrderSet))
          return false;
        OrderSet o = (OrderSet) other;
        return compareDeep(moduleMetadata, o.moduleMetadata, true) && compareDeep(library, o.library, true)
           && compareDeep(action, o.action, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof OrderSet))
          return false;
        OrderSet o = (OrderSet) other;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (moduleMetadata == null || moduleMetadata.isEmpty()) && (library == null || library.isEmpty())
           && (action == null || action.isEmpty());
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.OrderSet;
   }

 /**
   * Search parameter: <b>topic</b>
   * <p>
   * Description: <b>Topics associated with the module</b><br>
   * Type: <b>token</b><br>
   * Path: <b>OrderSet.moduleMetadata.topic</b><br>
   * </p>
   */
  @SearchParamDefinition(name="topic", path="OrderSet.moduleMetadata.topic", description="Topics associated with the module", type="token" )
  public static final String SP_TOPIC = "topic";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>topic</b>
   * <p>
   * Description: <b>Topics associated with the module</b><br>
   * Type: <b>token</b><br>
   * Path: <b>OrderSet.moduleMetadata.topic</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam TOPIC = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_TOPIC);

 /**
   * Search parameter: <b>title</b>
   * <p>
   * Description: <b>Text search against the title</b><br>
   * Type: <b>string</b><br>
   * Path: <b>OrderSet.moduleMetadata.title</b><br>
   * </p>
   */
  @SearchParamDefinition(name="title", path="OrderSet.moduleMetadata.title", description="Text search against the title", type="string" )
  public static final String SP_TITLE = "title";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>title</b>
   * <p>
   * Description: <b>Text search against the title</b><br>
   * Type: <b>string</b><br>
   * Path: <b>OrderSet.moduleMetadata.title</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam TITLE = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_TITLE);

 /**
   * Search parameter: <b>status</b>
   * <p>
   * Description: <b>Status of the module</b><br>
   * Type: <b>token</b><br>
   * Path: <b>OrderSet.moduleMetadata.status</b><br>
   * </p>
   */
  @SearchParamDefinition(name="status", path="OrderSet.moduleMetadata.status", description="Status of the module", type="token" )
  public static final String SP_STATUS = "status";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>status</b>
   * <p>
   * Description: <b>Status of the module</b><br>
   * Type: <b>token</b><br>
   * Path: <b>OrderSet.moduleMetadata.status</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam STATUS = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_STATUS);

 /**
   * Search parameter: <b>description</b>
   * <p>
   * Description: <b>Text search against the description</b><br>
   * Type: <b>string</b><br>
   * Path: <b>OrderSet.moduleMetadata.description</b><br>
   * </p>
   */
  @SearchParamDefinition(name="description", path="OrderSet.moduleMetadata.description", description="Text search against the description", type="string" )
  public static final String SP_DESCRIPTION = "description";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>description</b>
   * <p>
   * Description: <b>Text search against the description</b><br>
   * Type: <b>string</b><br>
   * Path: <b>OrderSet.moduleMetadata.description</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam DESCRIPTION = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_DESCRIPTION);

 /**
   * Search parameter: <b>identifier</b>
   * <p>
   * Description: <b>Logical identifier for the module (e.g. CMS-143)</b><br>
   * Type: <b>token</b><br>
   * Path: <b>OrderSet.moduleMetadata.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="OrderSet.moduleMetadata.identifier", description="Logical identifier for the module (e.g. CMS-143)", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>Logical identifier for the module (e.g. CMS-143)</b><br>
   * Type: <b>token</b><br>
   * Path: <b>OrderSet.moduleMetadata.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_IDENTIFIER);

 /**
   * Search parameter: <b>version</b>
   * <p>
   * Description: <b>Version of the module (e.g. 1.0.0)</b><br>
   * Type: <b>string</b><br>
   * Path: <b>OrderSet.moduleMetadata.version</b><br>
   * </p>
   */
  @SearchParamDefinition(name="version", path="OrderSet.moduleMetadata.version", description="Version of the module (e.g. 1.0.0)", type="string" )
  public static final String SP_VERSION = "version";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>version</b>
   * <p>
   * Description: <b>Version of the module (e.g. 1.0.0)</b><br>
   * Type: <b>string</b><br>
   * Path: <b>OrderSet.moduleMetadata.version</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam VERSION = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_VERSION);


}

