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
import org.hl7.fhir.utilities.Utilities;

import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
/**
 * This resource defines a decision support rule of the form [on Event] if Condition then Action. It is intended to be a shareable, computable definition of a actions that should be taken whenever some condition is met in response to a particular event or events.
 */
@ResourceDef(name="DecisionSupportRule", profile="http://hl7.org/fhir/Profile/DecisionSupportRule")
public class DecisionSupportRule extends DomainResource {

    /**
     * The metadata for the decision support rule, including publishing, life-cycle, version, documentation, and supporting evidence.
     */
    @Child(name = "moduleMetadata", type = {ModuleMetadata.class}, order=0, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Module information for the rule", formalDefinition="The metadata for the decision support rule, including publishing, life-cycle, version, documentation, and supporting evidence." )
    protected ModuleMetadata moduleMetadata;

    /**
     * A reference to a Library containing the formal logic used by the rule.
     */
    @Child(name = "library", type = {Library.class}, order=1, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Logic used within the rule", formalDefinition="A reference to a Library containing the formal logic used by the rule." )
    protected List<Reference> library;
    /**
     * The actual objects that are the target of the reference (A reference to a Library containing the formal logic used by the rule.)
     */
    protected List<Library> libraryTarget;


    /**
     * The trigger element defines when the rule should be invoked. This information is used by consumers of the rule to determine how to integrate the rule into a specific workflow.
     */
    @Child(name = "trigger", type = {TriggerDefinition.class}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="\"when\" the rule should be invoked", formalDefinition="The trigger element defines when the rule should be invoked. This information is used by consumers of the rule to determine how to integrate the rule into a specific workflow." )
    protected List<TriggerDefinition> trigger;

    /**
     * The condition element describes he "if" portion of the rule that determines whether or not the rule "fires". The condition must be the name of an expression in a referenced library.
     */
    @Child(name = "condition", type = {StringType.class}, order=3, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="\"if\" some condition is true", formalDefinition="The condition element describes he \"if\" portion of the rule that determines whether or not the rule \"fires\". The condition must be the name of an expression in a referenced library." )
    protected StringType condition;

    /**
     * The action element defines the "when" portion of the rule that determines what actions should be performed if the condition evaluates to true.
     */
    @Child(name = "action", type = {ActionDefinition.class}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="\"then\" perform these actions", formalDefinition="The action element defines the \"when\" portion of the rule that determines what actions should be performed if the condition evaluates to true." )
    protected List<ActionDefinition> action;

    private static final long serialVersionUID = -810482843L;

  /**
   * Constructor
   */
    public DecisionSupportRule() {
      super();
    }

    /**
     * @return {@link #moduleMetadata} (The metadata for the decision support rule, including publishing, life-cycle, version, documentation, and supporting evidence.)
     */
    public ModuleMetadata getModuleMetadata() { 
      if (this.moduleMetadata == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DecisionSupportRule.moduleMetadata");
        else if (Configuration.doAutoCreate())
          this.moduleMetadata = new ModuleMetadata(); // cc
      return this.moduleMetadata;
    }

    public boolean hasModuleMetadata() { 
      return this.moduleMetadata != null && !this.moduleMetadata.isEmpty();
    }

    /**
     * @param value {@link #moduleMetadata} (The metadata for the decision support rule, including publishing, life-cycle, version, documentation, and supporting evidence.)
     */
    public DecisionSupportRule setModuleMetadata(ModuleMetadata value) { 
      this.moduleMetadata = value;
      return this;
    }

    /**
     * @return {@link #library} (A reference to a Library containing the formal logic used by the rule.)
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
     * @return {@link #library} (A reference to a Library containing the formal logic used by the rule.)
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
    public DecisionSupportRule addLibrary(Reference t) { //3
      if (t == null)
        return this;
      if (this.library == null)
        this.library = new ArrayList<Reference>();
      this.library.add(t);
      return this;
    }

    /**
     * @return {@link #library} (The actual objects that are the target of the reference. The reference library doesn't populate this, but you can use this to hold the resources if you resolvethemt. A reference to a Library containing the formal logic used by the rule.)
     */
    public List<Library> getLibraryTarget() { 
      if (this.libraryTarget == null)
        this.libraryTarget = new ArrayList<Library>();
      return this.libraryTarget;
    }

    // syntactic sugar
    /**
     * @return {@link #library} (Add an actual object that is the target of the reference. The reference library doesn't use these, but you can use this to hold the resources if you resolvethemt. A reference to a Library containing the formal logic used by the rule.)
     */
    public Library addLibraryTarget() { 
      Library r = new Library();
      if (this.libraryTarget == null)
        this.libraryTarget = new ArrayList<Library>();
      this.libraryTarget.add(r);
      return r;
    }

    /**
     * @return {@link #trigger} (The trigger element defines when the rule should be invoked. This information is used by consumers of the rule to determine how to integrate the rule into a specific workflow.)
     */
    public List<TriggerDefinition> getTrigger() { 
      if (this.trigger == null)
        this.trigger = new ArrayList<TriggerDefinition>();
      return this.trigger;
    }

    public boolean hasTrigger() { 
      if (this.trigger == null)
        return false;
      for (TriggerDefinition item : this.trigger)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #trigger} (The trigger element defines when the rule should be invoked. This information is used by consumers of the rule to determine how to integrate the rule into a specific workflow.)
     */
    // syntactic sugar
    public TriggerDefinition addTrigger() { //3
      TriggerDefinition t = new TriggerDefinition();
      if (this.trigger == null)
        this.trigger = new ArrayList<TriggerDefinition>();
      this.trigger.add(t);
      return t;
    }

    // syntactic sugar
    public DecisionSupportRule addTrigger(TriggerDefinition t) { //3
      if (t == null)
        return this;
      if (this.trigger == null)
        this.trigger = new ArrayList<TriggerDefinition>();
      this.trigger.add(t);
      return this;
    }

    /**
     * @return {@link #condition} (The condition element describes he "if" portion of the rule that determines whether or not the rule "fires". The condition must be the name of an expression in a referenced library.). This is the underlying object with id, value and extensions. The accessor "getCondition" gives direct access to the value
     */
    public StringType getConditionElement() { 
      if (this.condition == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DecisionSupportRule.condition");
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
     * @param value {@link #condition} (The condition element describes he "if" portion of the rule that determines whether or not the rule "fires". The condition must be the name of an expression in a referenced library.). This is the underlying object with id, value and extensions. The accessor "getCondition" gives direct access to the value
     */
    public DecisionSupportRule setConditionElement(StringType value) { 
      this.condition = value;
      return this;
    }

    /**
     * @return The condition element describes he "if" portion of the rule that determines whether or not the rule "fires". The condition must be the name of an expression in a referenced library.
     */
    public String getCondition() { 
      return this.condition == null ? null : this.condition.getValue();
    }

    /**
     * @param value The condition element describes he "if" portion of the rule that determines whether or not the rule "fires". The condition must be the name of an expression in a referenced library.
     */
    public DecisionSupportRule setCondition(String value) { 
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
     * @return {@link #action} (The action element defines the "when" portion of the rule that determines what actions should be performed if the condition evaluates to true.)
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
     * @return {@link #action} (The action element defines the "when" portion of the rule that determines what actions should be performed if the condition evaluates to true.)
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
    public DecisionSupportRule addAction(ActionDefinition t) { //3
      if (t == null)
        return this;
      if (this.action == null)
        this.action = new ArrayList<ActionDefinition>();
      this.action.add(t);
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("moduleMetadata", "ModuleMetadata", "The metadata for the decision support rule, including publishing, life-cycle, version, documentation, and supporting evidence.", 0, java.lang.Integer.MAX_VALUE, moduleMetadata));
        childrenList.add(new Property("library", "Reference(Library)", "A reference to a Library containing the formal logic used by the rule.", 0, java.lang.Integer.MAX_VALUE, library));
        childrenList.add(new Property("trigger", "TriggerDefinition", "The trigger element defines when the rule should be invoked. This information is used by consumers of the rule to determine how to integrate the rule into a specific workflow.", 0, java.lang.Integer.MAX_VALUE, trigger));
        childrenList.add(new Property("condition", "string", "The condition element describes he \"if\" portion of the rule that determines whether or not the rule \"fires\". The condition must be the name of an expression in a referenced library.", 0, java.lang.Integer.MAX_VALUE, condition));
        childrenList.add(new Property("action", "ActionDefinition", "The action element defines the \"when\" portion of the rule that determines what actions should be performed if the condition evaluates to true.", 0, java.lang.Integer.MAX_VALUE, action));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 455891387: /*moduleMetadata*/ return this.moduleMetadata == null ? new Base[0] : new Base[] {this.moduleMetadata}; // ModuleMetadata
        case 166208699: /*library*/ return this.library == null ? new Base[0] : this.library.toArray(new Base[this.library.size()]); // Reference
        case -1059891784: /*trigger*/ return this.trigger == null ? new Base[0] : this.trigger.toArray(new Base[this.trigger.size()]); // TriggerDefinition
        case -861311717: /*condition*/ return this.condition == null ? new Base[0] : new Base[] {this.condition}; // StringType
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
        case -1059891784: // trigger
          this.getTrigger().add(castToTriggerDefinition(value)); // TriggerDefinition
          break;
        case -861311717: // condition
          this.condition = castToString(value); // StringType
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
        else if (name.equals("trigger"))
          this.getTrigger().add(castToTriggerDefinition(value));
        else if (name.equals("condition"))
          this.condition = castToString(value); // StringType
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
        case -1059891784:  return addTrigger(); // TriggerDefinition
        case -861311717: throw new FHIRException("Cannot make property condition as it is not a complex type"); // StringType
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
        else if (name.equals("trigger")) {
          return addTrigger();
        }
        else if (name.equals("condition")) {
          throw new FHIRException("Cannot call addChild on a primitive type DecisionSupportRule.condition");
        }
        else if (name.equals("action")) {
          return addAction();
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "DecisionSupportRule";

  }

      public DecisionSupportRule copy() {
        DecisionSupportRule dst = new DecisionSupportRule();
        copyValues(dst);
        dst.moduleMetadata = moduleMetadata == null ? null : moduleMetadata.copy();
        if (library != null) {
          dst.library = new ArrayList<Reference>();
          for (Reference i : library)
            dst.library.add(i.copy());
        };
        if (trigger != null) {
          dst.trigger = new ArrayList<TriggerDefinition>();
          for (TriggerDefinition i : trigger)
            dst.trigger.add(i.copy());
        };
        dst.condition = condition == null ? null : condition.copy();
        if (action != null) {
          dst.action = new ArrayList<ActionDefinition>();
          for (ActionDefinition i : action)
            dst.action.add(i.copy());
        };
        return dst;
      }

      protected DecisionSupportRule typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof DecisionSupportRule))
          return false;
        DecisionSupportRule o = (DecisionSupportRule) other;
        return compareDeep(moduleMetadata, o.moduleMetadata, true) && compareDeep(library, o.library, true)
           && compareDeep(trigger, o.trigger, true) && compareDeep(condition, o.condition, true) && compareDeep(action, o.action, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof DecisionSupportRule))
          return false;
        DecisionSupportRule o = (DecisionSupportRule) other;
        return compareValues(condition, o.condition, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (moduleMetadata == null || moduleMetadata.isEmpty()) && (library == null || library.isEmpty())
           && (trigger == null || trigger.isEmpty()) && (condition == null || condition.isEmpty()) && (action == null || action.isEmpty())
          ;
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.DecisionSupportRule;
   }

 /**
   * Search parameter: <b>topic</b>
   * <p>
   * Description: <b>Topics associated with the module</b><br>
   * Type: <b>token</b><br>
   * Path: <b>DecisionSupportRule.moduleMetadata.topic</b><br>
   * </p>
   */
  @SearchParamDefinition(name="topic", path="DecisionSupportRule.moduleMetadata.topic", description="Topics associated with the module", type="token" )
  public static final String SP_TOPIC = "topic";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>topic</b>
   * <p>
   * Description: <b>Topics associated with the module</b><br>
   * Type: <b>token</b><br>
   * Path: <b>DecisionSupportRule.moduleMetadata.topic</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam TOPIC = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_TOPIC);

 /**
   * Search parameter: <b>title</b>
   * <p>
   * Description: <b>Text search against the title</b><br>
   * Type: <b>string</b><br>
   * Path: <b>DecisionSupportRule.moduleMetadata.title</b><br>
   * </p>
   */
  @SearchParamDefinition(name="title", path="DecisionSupportRule.moduleMetadata.title", description="Text search against the title", type="string" )
  public static final String SP_TITLE = "title";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>title</b>
   * <p>
   * Description: <b>Text search against the title</b><br>
   * Type: <b>string</b><br>
   * Path: <b>DecisionSupportRule.moduleMetadata.title</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam TITLE = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_TITLE);

 /**
   * Search parameter: <b>status</b>
   * <p>
   * Description: <b>Status of the module</b><br>
   * Type: <b>token</b><br>
   * Path: <b>DecisionSupportRule.moduleMetadata.status</b><br>
   * </p>
   */
  @SearchParamDefinition(name="status", path="DecisionSupportRule.moduleMetadata.status", description="Status of the module", type="token" )
  public static final String SP_STATUS = "status";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>status</b>
   * <p>
   * Description: <b>Status of the module</b><br>
   * Type: <b>token</b><br>
   * Path: <b>DecisionSupportRule.moduleMetadata.status</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam STATUS = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_STATUS);

 /**
   * Search parameter: <b>description</b>
   * <p>
   * Description: <b>Text search against the description</b><br>
   * Type: <b>string</b><br>
   * Path: <b>DecisionSupportRule.moduleMetadata.description</b><br>
   * </p>
   */
  @SearchParamDefinition(name="description", path="DecisionSupportRule.moduleMetadata.description", description="Text search against the description", type="string" )
  public static final String SP_DESCRIPTION = "description";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>description</b>
   * <p>
   * Description: <b>Text search against the description</b><br>
   * Type: <b>string</b><br>
   * Path: <b>DecisionSupportRule.moduleMetadata.description</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam DESCRIPTION = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_DESCRIPTION);

 /**
   * Search parameter: <b>identifier</b>
   * <p>
   * Description: <b>Logical identifier for the module (e.g. CMS-143)</b><br>
   * Type: <b>token</b><br>
   * Path: <b>DecisionSupportRule.moduleMetadata.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="DecisionSupportRule.moduleMetadata.identifier", description="Logical identifier for the module (e.g. CMS-143)", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>Logical identifier for the module (e.g. CMS-143)</b><br>
   * Type: <b>token</b><br>
   * Path: <b>DecisionSupportRule.moduleMetadata.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_IDENTIFIER);

 /**
   * Search parameter: <b>version</b>
   * <p>
   * Description: <b>Version of the module (e.g. 1.0.0)</b><br>
   * Type: <b>string</b><br>
   * Path: <b>DecisionSupportRule.moduleMetadata.version</b><br>
   * </p>
   */
  @SearchParamDefinition(name="version", path="DecisionSupportRule.moduleMetadata.version", description="Version of the module (e.g. 1.0.0)", type="string" )
  public static final String SP_VERSION = "version";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>version</b>
   * <p>
   * Description: <b>Version of the module (e.g. 1.0.0)</b><br>
   * Type: <b>string</b><br>
   * Path: <b>DecisionSupportRule.moduleMetadata.version</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam VERSION = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_VERSION);


}

