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
 * The DecisionSupportServiceModule describes a unit of decision support functionality that is made available as a service, such as immunization modules or drug-drug interaction checking.
 */
@ResourceDef(name="DecisionSupportServiceModule", profile="http://hl7.org/fhir/Profile/DecisionSupportServiceModule")
public class DecisionSupportServiceModule extends DomainResource {

    /**
     * The metadata for the decision support service module, including publishing, life-cycle, version, documentation, and supporting evidence.
     */
    @Child(name = "moduleMetadata", type = {ModuleMetadata.class}, order=0, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Metadata for the service module", formalDefinition="The metadata for the decision support service module, including publishing, life-cycle, version, documentation, and supporting evidence." )
    protected ModuleMetadata moduleMetadata;

    /**
     * The trigger element defines when the rule should be invoked. This information is used by consumers of the rule to determine how to integrate the rule into a specific workflow.
     */
    @Child(name = "trigger", type = {TriggerDefinition.class}, order=1, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="\"when\" the module should be invoked", formalDefinition="The trigger element defines when the rule should be invoked. This information is used by consumers of the rule to determine how to integrate the rule into a specific workflow." )
    protected List<TriggerDefinition> trigger;

    /**
     * The parameters to the module. This collection specifies both the input and output parameters. Input parameters are provided by the caller as part of the $evaluate operation. Output parameters are included in the GuidanceResponse.
     */
    @Child(name = "parameter", type = {ParameterDefinition.class}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Parameters to the module", formalDefinition="The parameters to the module. This collection specifies both the input and output parameters. Input parameters are provided by the caller as part of the $evaluate operation. Output parameters are included in the GuidanceResponse." )
    protected List<ParameterDefinition> parameter;

    /**
     * Data requirements are a machine processable description of the data required by the module in order to perform a successful evaluation.
     */
    @Child(name = "dataRequirement", type = {DataRequirement.class}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Data requirements for the module", formalDefinition="Data requirements are a machine processable description of the data required by the module in order to perform a successful evaluation." )
    protected List<DataRequirement> dataRequirement;

    private static final long serialVersionUID = 1154664442L;

  /**
   * Constructor
   */
    public DecisionSupportServiceModule() {
      super();
    }

    /**
     * @return {@link #moduleMetadata} (The metadata for the decision support service module, including publishing, life-cycle, version, documentation, and supporting evidence.)
     */
    public ModuleMetadata getModuleMetadata() { 
      if (this.moduleMetadata == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DecisionSupportServiceModule.moduleMetadata");
        else if (Configuration.doAutoCreate())
          this.moduleMetadata = new ModuleMetadata(); // cc
      return this.moduleMetadata;
    }

    public boolean hasModuleMetadata() { 
      return this.moduleMetadata != null && !this.moduleMetadata.isEmpty();
    }

    /**
     * @param value {@link #moduleMetadata} (The metadata for the decision support service module, including publishing, life-cycle, version, documentation, and supporting evidence.)
     */
    public DecisionSupportServiceModule setModuleMetadata(ModuleMetadata value) { 
      this.moduleMetadata = value;
      return this;
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
    public DecisionSupportServiceModule addTrigger(TriggerDefinition t) { //3
      if (t == null)
        return this;
      if (this.trigger == null)
        this.trigger = new ArrayList<TriggerDefinition>();
      this.trigger.add(t);
      return this;
    }

    /**
     * @return {@link #parameter} (The parameters to the module. This collection specifies both the input and output parameters. Input parameters are provided by the caller as part of the $evaluate operation. Output parameters are included in the GuidanceResponse.)
     */
    public List<ParameterDefinition> getParameter() { 
      if (this.parameter == null)
        this.parameter = new ArrayList<ParameterDefinition>();
      return this.parameter;
    }

    public boolean hasParameter() { 
      if (this.parameter == null)
        return false;
      for (ParameterDefinition item : this.parameter)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #parameter} (The parameters to the module. This collection specifies both the input and output parameters. Input parameters are provided by the caller as part of the $evaluate operation. Output parameters are included in the GuidanceResponse.)
     */
    // syntactic sugar
    public ParameterDefinition addParameter() { //3
      ParameterDefinition t = new ParameterDefinition();
      if (this.parameter == null)
        this.parameter = new ArrayList<ParameterDefinition>();
      this.parameter.add(t);
      return t;
    }

    // syntactic sugar
    public DecisionSupportServiceModule addParameter(ParameterDefinition t) { //3
      if (t == null)
        return this;
      if (this.parameter == null)
        this.parameter = new ArrayList<ParameterDefinition>();
      this.parameter.add(t);
      return this;
    }

    /**
     * @return {@link #dataRequirement} (Data requirements are a machine processable description of the data required by the module in order to perform a successful evaluation.)
     */
    public List<DataRequirement> getDataRequirement() { 
      if (this.dataRequirement == null)
        this.dataRequirement = new ArrayList<DataRequirement>();
      return this.dataRequirement;
    }

    public boolean hasDataRequirement() { 
      if (this.dataRequirement == null)
        return false;
      for (DataRequirement item : this.dataRequirement)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #dataRequirement} (Data requirements are a machine processable description of the data required by the module in order to perform a successful evaluation.)
     */
    // syntactic sugar
    public DataRequirement addDataRequirement() { //3
      DataRequirement t = new DataRequirement();
      if (this.dataRequirement == null)
        this.dataRequirement = new ArrayList<DataRequirement>();
      this.dataRequirement.add(t);
      return t;
    }

    // syntactic sugar
    public DecisionSupportServiceModule addDataRequirement(DataRequirement t) { //3
      if (t == null)
        return this;
      if (this.dataRequirement == null)
        this.dataRequirement = new ArrayList<DataRequirement>();
      this.dataRequirement.add(t);
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("moduleMetadata", "ModuleMetadata", "The metadata for the decision support service module, including publishing, life-cycle, version, documentation, and supporting evidence.", 0, java.lang.Integer.MAX_VALUE, moduleMetadata));
        childrenList.add(new Property("trigger", "TriggerDefinition", "The trigger element defines when the rule should be invoked. This information is used by consumers of the rule to determine how to integrate the rule into a specific workflow.", 0, java.lang.Integer.MAX_VALUE, trigger));
        childrenList.add(new Property("parameter", "ParameterDefinition", "The parameters to the module. This collection specifies both the input and output parameters. Input parameters are provided by the caller as part of the $evaluate operation. Output parameters are included in the GuidanceResponse.", 0, java.lang.Integer.MAX_VALUE, parameter));
        childrenList.add(new Property("dataRequirement", "DataRequirement", "Data requirements are a machine processable description of the data required by the module in order to perform a successful evaluation.", 0, java.lang.Integer.MAX_VALUE, dataRequirement));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 455891387: /*moduleMetadata*/ return this.moduleMetadata == null ? new Base[0] : new Base[] {this.moduleMetadata}; // ModuleMetadata
        case -1059891784: /*trigger*/ return this.trigger == null ? new Base[0] : this.trigger.toArray(new Base[this.trigger.size()]); // TriggerDefinition
        case 1954460585: /*parameter*/ return this.parameter == null ? new Base[0] : this.parameter.toArray(new Base[this.parameter.size()]); // ParameterDefinition
        case 629147193: /*dataRequirement*/ return this.dataRequirement == null ? new Base[0] : this.dataRequirement.toArray(new Base[this.dataRequirement.size()]); // DataRequirement
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 455891387: // moduleMetadata
          this.moduleMetadata = castToModuleMetadata(value); // ModuleMetadata
          break;
        case -1059891784: // trigger
          this.getTrigger().add(castToTriggerDefinition(value)); // TriggerDefinition
          break;
        case 1954460585: // parameter
          this.getParameter().add(castToParameterDefinition(value)); // ParameterDefinition
          break;
        case 629147193: // dataRequirement
          this.getDataRequirement().add(castToDataRequirement(value)); // DataRequirement
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("moduleMetadata"))
          this.moduleMetadata = castToModuleMetadata(value); // ModuleMetadata
        else if (name.equals("trigger"))
          this.getTrigger().add(castToTriggerDefinition(value));
        else if (name.equals("parameter"))
          this.getParameter().add(castToParameterDefinition(value));
        else if (name.equals("dataRequirement"))
          this.getDataRequirement().add(castToDataRequirement(value));
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 455891387:  return getModuleMetadata(); // ModuleMetadata
        case -1059891784:  return addTrigger(); // TriggerDefinition
        case 1954460585:  return addParameter(); // ParameterDefinition
        case 629147193:  return addDataRequirement(); // DataRequirement
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("moduleMetadata")) {
          this.moduleMetadata = new ModuleMetadata();
          return this.moduleMetadata;
        }
        else if (name.equals("trigger")) {
          return addTrigger();
        }
        else if (name.equals("parameter")) {
          return addParameter();
        }
        else if (name.equals("dataRequirement")) {
          return addDataRequirement();
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "DecisionSupportServiceModule";

  }

      public DecisionSupportServiceModule copy() {
        DecisionSupportServiceModule dst = new DecisionSupportServiceModule();
        copyValues(dst);
        dst.moduleMetadata = moduleMetadata == null ? null : moduleMetadata.copy();
        if (trigger != null) {
          dst.trigger = new ArrayList<TriggerDefinition>();
          for (TriggerDefinition i : trigger)
            dst.trigger.add(i.copy());
        };
        if (parameter != null) {
          dst.parameter = new ArrayList<ParameterDefinition>();
          for (ParameterDefinition i : parameter)
            dst.parameter.add(i.copy());
        };
        if (dataRequirement != null) {
          dst.dataRequirement = new ArrayList<DataRequirement>();
          for (DataRequirement i : dataRequirement)
            dst.dataRequirement.add(i.copy());
        };
        return dst;
      }

      protected DecisionSupportServiceModule typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof DecisionSupportServiceModule))
          return false;
        DecisionSupportServiceModule o = (DecisionSupportServiceModule) other;
        return compareDeep(moduleMetadata, o.moduleMetadata, true) && compareDeep(trigger, o.trigger, true)
           && compareDeep(parameter, o.parameter, true) && compareDeep(dataRequirement, o.dataRequirement, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof DecisionSupportServiceModule))
          return false;
        DecisionSupportServiceModule o = (DecisionSupportServiceModule) other;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (moduleMetadata == null || moduleMetadata.isEmpty()) && (trigger == null || trigger.isEmpty())
           && (parameter == null || parameter.isEmpty()) && (dataRequirement == null || dataRequirement.isEmpty())
          ;
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.DecisionSupportServiceModule;
   }

 /**
   * Search parameter: <b>topic</b>
   * <p>
   * Description: <b>Topics associated with the module</b><br>
   * Type: <b>token</b><br>
   * Path: <b>DecisionSupportServiceModule.moduleMetadata.topic</b><br>
   * </p>
   */
  @SearchParamDefinition(name="topic", path="DecisionSupportServiceModule.moduleMetadata.topic", description="Topics associated with the module", type="token" )
  public static final String SP_TOPIC = "topic";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>topic</b>
   * <p>
   * Description: <b>Topics associated with the module</b><br>
   * Type: <b>token</b><br>
   * Path: <b>DecisionSupportServiceModule.moduleMetadata.topic</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam TOPIC = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_TOPIC);

 /**
   * Search parameter: <b>title</b>
   * <p>
   * Description: <b>Text search against the title</b><br>
   * Type: <b>string</b><br>
   * Path: <b>DecisionSupportServiceModule.moduleMetadata.title</b><br>
   * </p>
   */
  @SearchParamDefinition(name="title", path="DecisionSupportServiceModule.moduleMetadata.title", description="Text search against the title", type="string" )
  public static final String SP_TITLE = "title";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>title</b>
   * <p>
   * Description: <b>Text search against the title</b><br>
   * Type: <b>string</b><br>
   * Path: <b>DecisionSupportServiceModule.moduleMetadata.title</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam TITLE = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_TITLE);

 /**
   * Search parameter: <b>status</b>
   * <p>
   * Description: <b>Status of the module</b><br>
   * Type: <b>token</b><br>
   * Path: <b>DecisionSupportServiceModule.moduleMetadata.status</b><br>
   * </p>
   */
  @SearchParamDefinition(name="status", path="DecisionSupportServiceModule.moduleMetadata.status", description="Status of the module", type="token" )
  public static final String SP_STATUS = "status";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>status</b>
   * <p>
   * Description: <b>Status of the module</b><br>
   * Type: <b>token</b><br>
   * Path: <b>DecisionSupportServiceModule.moduleMetadata.status</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam STATUS = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_STATUS);

 /**
   * Search parameter: <b>description</b>
   * <p>
   * Description: <b>Text search against the description</b><br>
   * Type: <b>string</b><br>
   * Path: <b>DecisionSupportServiceModule.moduleMetadata.description</b><br>
   * </p>
   */
  @SearchParamDefinition(name="description", path="DecisionSupportServiceModule.moduleMetadata.description", description="Text search against the description", type="string" )
  public static final String SP_DESCRIPTION = "description";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>description</b>
   * <p>
   * Description: <b>Text search against the description</b><br>
   * Type: <b>string</b><br>
   * Path: <b>DecisionSupportServiceModule.moduleMetadata.description</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam DESCRIPTION = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_DESCRIPTION);

 /**
   * Search parameter: <b>identifier</b>
   * <p>
   * Description: <b>Logical identifier for the module (e.g. CMS-143)</b><br>
   * Type: <b>token</b><br>
   * Path: <b>DecisionSupportServiceModule.moduleMetadata.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="DecisionSupportServiceModule.moduleMetadata.identifier", description="Logical identifier for the module (e.g. CMS-143)", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>Logical identifier for the module (e.g. CMS-143)</b><br>
   * Type: <b>token</b><br>
   * Path: <b>DecisionSupportServiceModule.moduleMetadata.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_IDENTIFIER);

 /**
   * Search parameter: <b>version</b>
   * <p>
   * Description: <b>Version of the module (e.g. 1.0.0)</b><br>
   * Type: <b>string</b><br>
   * Path: <b>DecisionSupportServiceModule.moduleMetadata.version</b><br>
   * </p>
   */
  @SearchParamDefinition(name="version", path="DecisionSupportServiceModule.moduleMetadata.version", description="Version of the module (e.g. 1.0.0)", type="string" )
  public static final String SP_VERSION = "version";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>version</b>
   * <p>
   * Description: <b>Version of the module (e.g. 1.0.0)</b><br>
   * Type: <b>string</b><br>
   * Path: <b>DecisionSupportServiceModule.moduleMetadata.version</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam VERSION = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_VERSION);


}

