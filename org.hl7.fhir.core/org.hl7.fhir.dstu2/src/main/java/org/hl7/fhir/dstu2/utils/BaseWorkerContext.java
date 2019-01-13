package org.hl7.fhir.dstu2.utils;

/*-
 * #%L
 * org.hl7.fhir.dstu2
 * %%
 * Copyright (C) 2014 - 2019 Health Level 7
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


import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hl7.fhir.dstu2.model.BooleanType;
import org.hl7.fhir.dstu2.model.CodeableConcept;
import org.hl7.fhir.dstu2.model.Coding;
import org.hl7.fhir.dstu2.model.ConceptMap;
import org.hl7.fhir.dstu2.model.Conformance;
import org.hl7.fhir.dstu2.model.Extension;
import org.hl7.fhir.dstu2.model.Parameters;
import org.hl7.fhir.dstu2.model.Parameters.ParametersParameterComponent;
import org.hl7.fhir.dstu2.model.Reference;
import org.hl7.fhir.dstu2.model.StringType;
import org.hl7.fhir.dstu2.model.StructureDefinition;
import org.hl7.fhir.dstu2.model.UriType;
import org.hl7.fhir.dstu2.model.ValueSet;
import org.hl7.fhir.dstu2.model.ValueSet.ConceptDefinitionComponent;
import org.hl7.fhir.dstu2.model.ValueSet.ConceptDefinitionDesignationComponent;
import org.hl7.fhir.dstu2.model.ValueSet.ConceptSetComponent;
import org.hl7.fhir.dstu2.model.ValueSet.ValueSetComposeComponent;
import org.hl7.fhir.dstu2.model.ValueSet.ValueSetExpansionComponent;
import org.hl7.fhir.dstu2.model.ValueSet.ValueSetExpansionContainsComponent;
import org.hl7.fhir.dstu2.terminologies.ValueSetExpander.ETooCostly;
import org.hl7.fhir.dstu2.terminologies.ValueSetExpander.ValueSetExpansionOutcome;
import org.hl7.fhir.dstu2.terminologies.ValueSetExpanderFactory;
import org.hl7.fhir.dstu2.terminologies.ValueSetExpansionCache;
import org.hl7.fhir.dstu2.utils.client.FHIRToolingClient;
import org.hl7.fhir.utilities.CommaSeparatedStringBuilder;
import org.hl7.fhir.utilities.Utilities;
import org.hl7.fhir.utilities.validation.ValidationMessage.IssueSeverity;

public abstract class BaseWorkerContext implements IWorkerContext {

  // all maps are to the full URI
  protected Map<String, ValueSet> codeSystems = new HashMap<String, ValueSet>();
  protected Map<String, ValueSet> valueSets = new HashMap<String, ValueSet>();
  protected Map<String, ConceptMap> maps = new HashMap<String, ConceptMap>();
  
  protected ValueSetExpanderFactory expansionCache = new ValueSetExpansionCache(this);
  protected boolean cacheValidation; // if true, do an expansion and cache the expansion
  private Set<String> failed = new HashSet<String>(); // value sets for which we don't try to do expansion, since the first attempt to get a comprehensive expansion was not successful
  protected Map<String, Map<String, ValidationResult>> validationCache = new HashMap<String, Map<String,ValidationResult>>();
  
  // private ValueSetExpansionCache expansionCache; //   

  protected FHIRToolingClient txServer;

  @Override
  public ValueSet fetchCodeSystem(String system) {
    return codeSystems.get(system);
  } 

  @Override
  public boolean supportsSystem(String system) {
    if (codeSystems.containsKey(system))
      return true;
    else {
      Conformance conf = txServer.getConformanceStatement();
      for (Extension ex : ToolingExtensions.getExtensions(conf, "http://hl7.org/fhir/StructureDefinition/conformance-supported-system")) {
        if (system.equals(((UriType) ex.getValue()).getValue())) {
          return true;
        }
      }
    }
    return false;
  }

  @Override
  public ValueSetExpansionOutcome expandVS(ValueSet vs, boolean cacheOk) {
    try {
      Map<String, String> params = new HashMap<String, String>();
      params.put("_limit", "10000");
      params.put("_incomplete", "true");
      params.put("profile", "http://www.healthintersections.com.au/fhir/expansion/no-details");
      ValueSet result = txServer.expandValueset(vs, null, params);
      return new ValueSetExpansionOutcome(result);  
    } catch (Exception e) {
      return new ValueSetExpansionOutcome("Error expanding ValueSet \""+vs.getUrl()+": "+e.getMessage());
    }
  }

  private ValidationResult handleByCache(ValueSet vs, Coding coding, boolean tryCache) {
    String cacheId = cacheId(coding);
    Map<String, ValidationResult> cache = validationCache.get(vs.getUrl());
    if (cache == null) {
      cache = new HashMap<String, IWorkerContext.ValidationResult>();
      validationCache.put(vs.getUrl(), cache);
    }
    if (cache.containsKey(cacheId))
      return cache.get(cacheId);
    if (!tryCache)
      return null;
    if (!cacheValidation)
      return null;
    if (failed.contains(vs.getUrl()))
      return null;
    ValueSetExpansionOutcome vse = expandVS(vs, true);
    if (vse.getValueset() == null || notcomplete(vse.getValueset())) {
      failed.add(vs.getUrl());
      return null;
    }
    
    ValidationResult res = validateCode(coding, vse.getValueset());
    cache.put(cacheId, res);
    return res;
  }

  private boolean notcomplete(ValueSet vs) {
    if (!vs.hasExpansion())
      return true;
    if (!vs.getExpansion().getExtensionsByUrl("http://hl7.org/fhir/StructureDefinition/valueset-unclosed").isEmpty())
      return true;
    if (!vs.getExpansion().getExtensionsByUrl("http://hl7.org/fhir/StructureDefinition/valueset-toocostly").isEmpty())
      return true;
    return false;
  }

  private ValidationResult handleByCache(ValueSet vs, CodeableConcept concept, boolean tryCache) {
    String cacheId = cacheId(concept);
    Map<String, ValidationResult> cache = validationCache.get(vs.getUrl());
    if (cache == null) {
      cache = new HashMap<String, IWorkerContext.ValidationResult>();
      validationCache.put(vs.getUrl(), cache);
    }
    if (cache.containsKey(cacheId))
      return cache.get(cacheId);
    
    if (validationCache.containsKey(vs.getUrl()) && validationCache.get(vs.getUrl()).containsKey(cacheId))
      return validationCache.get(vs.getUrl()).get(cacheId);
    if (!tryCache)
      return null;
    if (!cacheValidation)
      return null;
    if (failed.contains(vs.getUrl()))
      return null;
    ValueSetExpansionOutcome vse = expandVS(vs, true);
    if (vse.getValueset() == null || notcomplete(vse.getValueset())) {
      failed.add(vs.getUrl());
      return null;
    }
    ValidationResult res = validateCode(concept, vse.getValueset());
    cache.put(cacheId, res);
    return res;
  }

  private String cacheId(Coding coding) {
    return "|"+coding.getSystem()+"|"+coding.getVersion()+"|"+coding.getCode()+"|"+coding.getDisplay();
  }
  
  private String cacheId(CodeableConcept cc) {
    StringBuilder b = new StringBuilder();
    for (Coding c : cc.getCoding()) {
      b.append("#");
      b.append(cacheId(c));
    }    
    return b.toString();
  }
  
  private ValidationResult verifyCodeExternal(ValueSet vs, Coding coding, boolean tryCache) {
    ValidationResult res = handleByCache(vs, coding, tryCache);
    if (res != null)
      return res;
    Parameters pin = new Parameters();
    pin.addParameter().setName("coding").setValue(coding);
    pin.addParameter().setName("valueSet").setResource(vs);
    res = serverValidateCode(pin);
    Map<String, ValidationResult> cache = validationCache.get(vs.getUrl());
    cache.put(cacheId(coding), res);
    return res;
  }
  
  private ValidationResult verifyCodeExternal(ValueSet vs, CodeableConcept cc, boolean tryCache) {
    ValidationResult res = handleByCache(vs, cc, tryCache);
    if (res != null)
      return res;
    Parameters pin = new Parameters();
    pin.addParameter().setName("codeableConcept").setValue(cc);
    pin.addParameter().setName("valueSet").setResource(vs);
    res = serverValidateCode(pin);
    Map<String, ValidationResult> cache = validationCache.get(vs.getUrl());
    cache.put(cacheId(cc), res);
    return res;
  }

  private ValidationResult serverValidateCode(Parameters pin) {
  Parameters pout = txServer.operateType(ValueSet.class, "validate-code", pin);
  boolean ok = false;
  String message = "No Message returned";
  String display = null;
  for (ParametersParameterComponent p : pout.getParameter()) {
    if (p.getName().equals("result"))
      ok = ((BooleanType) p.getValue()).getValue().booleanValue();
    else if (p.getName().equals("message"))
      message = ((StringType) p.getValue()).getValue();
    else if (p.getName().equals("display"))
      display = ((StringType) p.getValue()).getValue();
  }
  if (!ok)
    return new ValidationResult(IssueSeverity.ERROR, message);
  else if (display != null)
    return new ValidationResult(new ConceptDefinitionComponent().setDisplay(display));
  else
    return new ValidationResult(null);
  }

  
  @Override
  public ValueSetExpansionComponent expandVS(ConceptSetComponent inc) {
    ValueSet vs = new ValueSet();
    vs.setCompose(new ValueSetComposeComponent());
    vs.getCompose().getInclude().add(inc);
    ValueSetExpansionOutcome vse = expandVS(vs, true);
    return vse.getValueset().getExpansion();
  }

  @Override
  public ValidationResult validateCode(String system, String code, String display) {
    try {
      if (codeSystems.containsKey(system)) 
        return verifyCodeInternal(codeSystems.get(system), system, code, display);
      else 
        return verifyCodeExternal(null, new Coding().setSystem(system).setCode(code).setDisplay(display), true);
    } catch (Exception e) {
      return new ValidationResult(IssueSeverity.FATAL, "Error validating code \""+code+"\" in system \""+system+"\": "+e.getMessage());
    }
  }

  
  @Override
  public ValidationResult validateCode(Coding code, ValueSet vs) {
    try {
      if (codeSystems.containsKey(code.getSystem()) || vs.hasExpansion()) 
        return verifyCodeInternal(codeSystems.get(code.getSystem()), code.getSystem(), code.getCode(), code.getDisplay());
      else 
        return verifyCodeExternal(vs, code, true);
    } catch (Exception e) {
      return new ValidationResult(IssueSeverity.FATAL, "Error validating code \""+code+"\" in system \""+code.getSystem()+"\": "+e.getMessage());
    }
  }

  @Override
  public ValidationResult validateCode(CodeableConcept code, ValueSet vs) {
    try {
      if (vs.hasCodeSystem() || vs.hasExpansion()) 
        return verifyCodeInternal(vs, code);
      else 
        return verifyCodeExternal(vs, code, true);
    } catch (Exception e) {
      return new ValidationResult(IssueSeverity.FATAL, "Error validating code \""+code.toString()+"\": "+e.getMessage());
    }
  }


  @Override
  public ValidationResult validateCode(String system, String code, String display, ValueSet vs) {
    try {
      if (system == null && vs.hasCodeSystem())
        return verifyCodeInternal(vs, vs.getCodeSystem().getSystem(), code, display);
      else if (codeSystems.containsKey(system) || vs.hasExpansion()) 
        return verifyCodeInternal(vs, system, code, display);
      else 
        return verifyCodeExternal(vs, new Coding().setSystem(system).setCode(code).setDisplay(display), true);
    } catch (Exception e) {
      return new ValidationResult(IssueSeverity.FATAL, "Error validating code \""+code+"\" in system \""+system+"\": "+e.getMessage());
    }
  }

  @Override
  public ValidationResult validateCode(String system, String code, String display, ConceptSetComponent vsi) {
    try {
      ValueSet vs = new ValueSet().setUrl(Utilities.makeUuidUrn());
      vs.getCompose().addInclude(vsi);
      return verifyCodeExternal(vs, new Coding().setSystem(system).setCode(code).setDisplay(display), true);
    } catch (Exception e) {
      return new ValidationResult(IssueSeverity.FATAL, "Error validating code \""+code+"\" in system \""+system+"\": "+e.getMessage());
    }
  }

  @Override
  public List<ConceptMap> findMapsForSource(String url) {
    List<ConceptMap> res = new ArrayList<ConceptMap>();
    for (ConceptMap map : maps.values())
      if (((Reference) map.getSource()).getReference().equals(url)) 
        res.add(map);
    return res;
  }

  private ValidationResult verifyCodeInternal(ValueSet vs, CodeableConcept code) throws FileNotFoundException, ETooCostly, IOException {
    for (Coding c : code.getCoding()) {
      ValidationResult res = verifyCodeInternal(vs, c.getSystem(), c.getCode(), c.getDisplay());
      if (res.isOk())
        return res;
    }
    if (code.getCoding().isEmpty())
      return new ValidationResult(IssueSeverity.ERROR, "None code provided");
    else
      return new ValidationResult(IssueSeverity.ERROR, "None of the codes are in the specified value set");
  }

  private ValidationResult verifyCodeInternal(ValueSet vs, String system, String code, String display) throws FileNotFoundException, ETooCostly, IOException {
    if (vs.hasExpansion())
      return verifyCodeInExpansion(vs, system, code, display);
    else if (vs.hasCodeSystem() && !vs.hasCompose()) 
      return verifyCodeInCodeSystem(vs, system, code, display);
    else {
      ValueSetExpansionOutcome vse = expansionCache.getExpander().expand(vs);
      if (vse.getValueset() != null) 
        return verifyCodeExternal(vs, new Coding().setSystem(system).setCode(code).setDisplay(display), false);
      else
        return verifyCodeInExpansion(vse.getValueset(), system, code, display);
    }
  }

  private ValidationResult verifyCodeInCodeSystem(ValueSet vs, String system, String code, String display) {
    ConceptDefinitionComponent cc = findCodeInConcept(vs.getCodeSystem().getConcept(), code);
    if (cc == null)
      return new ValidationResult(IssueSeverity.ERROR, "Unknown Code "+code+" in "+vs.getCodeSystem().getSystem());
    if (display == null)
      return new ValidationResult(cc);
    CommaSeparatedStringBuilder b = new CommaSeparatedStringBuilder();
    if (cc.hasDisplay()) {
      b.append(cc.getDisplay());
      if (display.equalsIgnoreCase(cc.getDisplay()))
        return new ValidationResult(cc);
    }
    for (ConceptDefinitionDesignationComponent ds : cc.getDesignation()) {
      b.append(ds.getValue());
      if (display.equalsIgnoreCase(ds.getValue()))
        return new ValidationResult(cc);
    }
    return new ValidationResult(IssueSeverity.ERROR, "Display Name for "+code+" must be one of '"+b.toString()+"'");
  }


  private ValidationResult verifyCodeInExpansion(ValueSet vs, String system,String code, String display) {
    ValueSetExpansionContainsComponent cc = findCode(vs.getExpansion().getContains(), code);
    if (cc == null)
      return new ValidationResult(IssueSeverity.ERROR, "Unknown Code "+code+" in "+vs.getCodeSystem().getSystem());
    if (display == null)
      return new ValidationResult(new ConceptDefinitionComponent().setCode(code).setDisplay(cc.getDisplay()));
    if (cc.hasDisplay()) {
      if (display.equalsIgnoreCase(cc.getDisplay()))
        return new ValidationResult(new ConceptDefinitionComponent().setCode(code).setDisplay(cc.getDisplay()));
      return new ValidationResult(IssueSeverity.ERROR, "Display Name for "+code+" must be '"+cc.getDisplay()+"'");
    }
    return null;
  }

  private ValueSetExpansionContainsComponent findCode(List<ValueSetExpansionContainsComponent> contains, String code) {
    for (ValueSetExpansionContainsComponent cc : contains) {
      if (code.equals(cc.getCode()))
        return cc;
      ValueSetExpansionContainsComponent c = findCode(cc.getContains(), code);
      if (c != null)
        return c;
    }
    return null;
  }

  private ConceptDefinitionComponent findCodeInConcept(List<ConceptDefinitionComponent> concept, String code) {
    for (ConceptDefinitionComponent cc : concept) {
      if (code.equals(cc.getCode()))
        return cc;
      ConceptDefinitionComponent c = findCodeInConcept(cc.getConcept(), code);
      if (c != null)
        return c;
    }
    return null;
  }

  @Override
  public StructureDefinition fetchTypeDefinition(String typeName) {
    return fetchResource(StructureDefinition.class, "http://hl7.org/fhir/StructureDefinition/"+typeName);
  }

}
