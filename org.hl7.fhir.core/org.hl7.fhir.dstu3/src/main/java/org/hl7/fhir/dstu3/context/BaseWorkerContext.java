package org.hl7.fhir.dstu3.context;

/*-
 * #%L
 * org.hl7.fhir.dstu3
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


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.codec.Charsets;
import org.hl7.fhir.dstu3.formats.IParser.OutputStyle;
import org.hl7.fhir.dstu3.formats.JsonParser;
import org.hl7.fhir.dstu3.model.BooleanType;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.dstu3.model.CodeSystem;
import org.hl7.fhir.dstu3.model.CodeSystem.CodeSystemHierarchyMeaning;
import org.hl7.fhir.dstu3.model.CodeSystem.ConceptDefinitionComponent;
import org.hl7.fhir.dstu3.model.CodeSystem.ConceptDefinitionDesignationComponent;
import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.ConceptMap;
import org.hl7.fhir.dstu3.model.DataElement;
import org.hl7.fhir.dstu3.model.ExpansionProfile;
import org.hl7.fhir.dstu3.model.OperationDefinition;
import org.hl7.fhir.dstu3.model.OperationOutcome;
import org.hl7.fhir.dstu3.model.Parameters;
import org.hl7.fhir.dstu3.model.Parameters.ParametersParameterComponent;
import org.hl7.fhir.dstu3.model.PrimitiveType;
import org.hl7.fhir.dstu3.model.Questionnaire;
import org.hl7.fhir.dstu3.model.Reference;
import org.hl7.fhir.dstu3.model.Resource;
import org.hl7.fhir.dstu3.model.SearchParameter;
import org.hl7.fhir.dstu3.model.StringType;
import org.hl7.fhir.dstu3.model.StructureDefinition;
import org.hl7.fhir.dstu3.model.StructureDefinition.TypeDerivationRule;
import org.hl7.fhir.dstu3.model.StructureMap;
import org.hl7.fhir.dstu3.model.UriType;
import org.hl7.fhir.dstu3.model.ValueSet;
import org.hl7.fhir.dstu3.model.ValueSet.ConceptSetComponent;
import org.hl7.fhir.dstu3.model.ValueSet.ConceptSetFilterComponent;
import org.hl7.fhir.dstu3.model.ValueSet.ValueSetComposeComponent;
import org.hl7.fhir.dstu3.model.ValueSet.ValueSetExpansionComponent;
import org.hl7.fhir.dstu3.model.ValueSet.ValueSetExpansionContainsComponent;
import org.hl7.fhir.dstu3.terminologies.ValueSetExpander.ETooCostly;
import org.hl7.fhir.dstu3.terminologies.ValueSetExpander.TerminologyServiceErrorClass;
import org.hl7.fhir.dstu3.terminologies.ValueSetExpander.ValueSetExpansionOutcome;
import org.hl7.fhir.dstu3.terminologies.ValueSetExpanderFactory;
import org.hl7.fhir.dstu3.terminologies.ValueSetExpansionCache;
import org.hl7.fhir.dstu3.utils.ToolingExtensions;
import org.hl7.fhir.dstu3.utils.client.FHIRToolingClient;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.exceptions.NoTerminologyServiceException;
import org.hl7.fhir.exceptions.TerminologyServiceException;
import org.hl7.fhir.utilities.CommaSeparatedStringBuilder;
import org.hl7.fhir.utilities.TextFile;
import org.hl7.fhir.utilities.Utilities;
import org.hl7.fhir.utilities.validation.ValidationMessage.IssueSeverity;
import org.hl7.fhir.utilities.validation.ValidationMessage.IssueType;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

public abstract class BaseWorkerContext implements IWorkerContext {

  // all maps are to the full URI
  protected Map<String, CodeSystem> codeSystems = new HashMap<String, CodeSystem>();
  protected Set<String> nonSupportedCodeSystems = new HashSet<String>();
  protected Map<String, ValueSet> valueSets = new HashMap<String, ValueSet>();
  protected Map<String, ConceptMap> maps = new HashMap<String, ConceptMap>();
  protected Map<String, StructureMap> transforms = new HashMap<String, StructureMap>();
  protected Map<String, DataElement> dataElements = new HashMap<String, DataElement>();
  protected Map<String, StructureDefinition> profiles = new HashMap<String, StructureDefinition>();
  protected Map<String, SearchParameter> searchParameters = new HashMap<String, SearchParameter>();
  protected Map<String, StructureDefinition> extensionDefinitions = new HashMap<String, StructureDefinition>();
  protected Map<String, Questionnaire> questionnaires = new HashMap<String, Questionnaire>();
  protected Map<String, OperationDefinition> operations = new HashMap<String, OperationDefinition>();

  protected ValueSetExpanderFactory expansionCache = new ValueSetExpansionCache(this);
  protected boolean cacheValidation; // if true, do an expansion and cache the expansion
  private Set<String> failed = new HashSet<String>(); // value sets for which we don't try to do expansion, since the first attempt to get a comprehensive expansion was not successful
  protected Map<String, Map<String, ValidationResult>> validationCache = new HashMap<String, Map<String,ValidationResult>>();
  protected String tsServer;
  protected String validationCachePath;
  protected String name;

  // private ValueSetExpansionCache expansionCache; //   

  protected FHIRToolingClient txServer;
  private Bundle bndCodeSystems;
  private boolean canRunWithoutTerminology;
  protected boolean allowLoadingDuplicates;
  protected boolean noTerminologyServer;
  protected String cache;
  private int expandCodesLimit = 1000;
  protected ILoggingService logger;
  protected ExpansionProfile expProfile;

  public Map<String, CodeSystem> getCodeSystems() {
    return codeSystems;
  }

  public Map<String, DataElement> getDataElements() {
    return dataElements;
  }

  public Map<String, ValueSet> getValueSets() {
    return valueSets;
  }

  public Map<String, ConceptMap> getMaps() {
    return maps;
  }

  public Map<String, StructureDefinition> getProfiles() {
    return profiles;
  }

  public Map<String, StructureDefinition> getExtensionDefinitions() {
    return extensionDefinitions;
  }

  public Map<String, Questionnaire> getQuestionnaires() {
    return questionnaires;
  }

  public Map<String, OperationDefinition> getOperations() {
    return operations;
  }

  public void seeExtensionDefinition(String url, StructureDefinition ed) throws Exception {
    if (extensionDefinitions.get(ed.getUrl()) != null)
      throw new Exception("duplicate extension definition: " + ed.getUrl());
    extensionDefinitions.put(ed.getId(), ed);
    extensionDefinitions.put(url, ed);
    extensionDefinitions.put(ed.getUrl(), ed);
  }

  public void dropExtensionDefinition(String id) {
    StructureDefinition sd = extensionDefinitions.get(id);
    extensionDefinitions.remove(id);
    if (sd!= null)
      extensionDefinitions.remove(sd.getUrl());
  }

  public void seeQuestionnaire(String url, Questionnaire theQuestionnaire) throws Exception {
    if (questionnaires.get(theQuestionnaire.getId()) != null)
      throw new Exception("duplicate extension definition: "+theQuestionnaire.getId());
    questionnaires.put(theQuestionnaire.getId(), theQuestionnaire);
    questionnaires.put(url, theQuestionnaire);
  }

  public void seeOperation(OperationDefinition opd) throws Exception {
    if (operations.get(opd.getUrl()) != null)
      throw new Exception("duplicate extension definition: "+opd.getUrl());
    operations.put(opd.getUrl(), opd);
    operations.put(opd.getId(), opd);
  }

  public void seeValueSet(String url, ValueSet vs) throws Exception {
    if (valueSets.containsKey(vs.getUrl()) && !allowLoadingDuplicates)
      throw new Exception("Duplicate value set "+vs.getUrl());
    valueSets.put(vs.getId(), vs);
    valueSets.put(url, vs);
    valueSets.put(vs.getUrl(), vs);
  }

  public void dropValueSet(String id) {
    ValueSet vs = valueSets.get(id);
    valueSets.remove(id);
    if (vs != null)
      valueSets.remove(vs.getUrl());
  }

  public void seeCodeSystem(String url, CodeSystem cs) throws FHIRException {
    if (codeSystems.containsKey(cs.getUrl()) && !allowLoadingDuplicates)
      throw new FHIRException("Duplicate code system "+cs.getUrl());
    codeSystems.put(cs.getId(), cs);
    codeSystems.put(url, cs);
    codeSystems.put(cs.getUrl(), cs);
  }

  public void dropCodeSystem(String id) {
    CodeSystem cs = codeSystems.get(id);
    codeSystems.remove(id);
    if (cs != null)
      codeSystems.remove(cs.getUrl());
  }

  public void seeProfile(String url, StructureDefinition p) throws Exception {
    if (profiles.containsKey(p.getUrl()))
      throw new Exception("Duplicate Profile "+p.getUrl());
    profiles.put(p.getId(), p);
    profiles.put(url, p);
    profiles.put(p.getUrl(), p);
  }

  public void dropProfile(String id) {
    StructureDefinition sd = profiles.get(id);
    profiles.remove(id);
    if (sd!= null)
      profiles.remove(sd.getUrl());
  }

  @Override
  public CodeSystem fetchCodeSystem(String system) {
    return codeSystems.get(system);
  } 

  @Override
  public boolean supportsSystem(String system) throws TerminologyServiceException {
    if (codeSystems.containsKey(system))
      return true;
    else if (nonSupportedCodeSystems.contains(system))
      return false;
    else if (system.startsWith("http://example.org") || system.startsWith("http://acme.com") || system.startsWith("http://hl7.org/fhir/valueset-") || system.startsWith("urn:oid:"))
      return false;
    else {
      if (noTerminologyServer)
        return false;
      if (bndCodeSystems == null) {
        try {
          tlog("Terminology server: Check for supported code systems for "+system);
          bndCodeSystems = txServer.fetchFeed(txServer.getAddress()+"/CodeSystem?content-mode=not-present&_summary=true&_count=1000");
        } catch (Exception e) {
          if (canRunWithoutTerminology) {
            noTerminologyServer = true;
            log("==============!! Running without terminology server !!============== ("+e.getMessage()+")");
            return false;
          } else
            throw new TerminologyServiceException(e);
        }
      }
      if (bndCodeSystems != null) {
        for (BundleEntryComponent be : bndCodeSystems.getEntry()) {
          CodeSystem cs = (CodeSystem) be.getResource();
          if (!codeSystems.containsKey(cs.getUrl())) {
            codeSystems.put(cs.getUrl(), null);
          }
        }
      }
      if (codeSystems.containsKey(system))
        return true;
    }
    nonSupportedCodeSystems.add(system);
    return false;
  }

  private void log(String message) {
    if (logger != null)
      logger.logMessage(message);
    else
      System.out.println(message);
  }

  @Override
  public ValueSetExpansionOutcome expandVS(ValueSet vs, boolean cacheOk, boolean heirarchical) {
    try {
      if (vs.hasExpansion()) {
        return new ValueSetExpansionOutcome(vs.copy());
      }
      String cacheFn = null;
      if (cache != null) {
        cacheFn = Utilities.path(cache, determineCacheId(vs, heirarchical)+".json");
        if (new File(cacheFn).exists())
          return loadFromCache(vs.copy(), cacheFn);
      }
      if (cacheOk && vs.hasUrl()) {
        if (expProfile == null)
          throw new Exception("No ExpansionProfile provided");
        ValueSetExpansionOutcome vse = expansionCache.getExpander().expand(vs, expProfile.setExcludeNested(!heirarchical));
        if (vse.getValueset() != null) {
          if (cache != null) {
            FileOutputStream s = new FileOutputStream(cacheFn);
            newJsonParser().compose(new FileOutputStream(cacheFn), vse.getValueset());
            s.close();
          }
        }
        return vse;
      } else {
        ValueSetExpansionOutcome res = expandOnServer(vs, cacheFn);
        if (cacheFn != null) {
          if (res.getValueset() != null) {
            saveToCache(res.getValueset(), cacheFn);
          } else { 
            OperationOutcome oo = new OperationOutcome();
            oo.addIssue().getDetails().setText(res.getError());
            saveToCache(oo, cacheFn);
          }
        }
        return res;
      }
    } catch (NoTerminologyServiceException e) {
      return new ValueSetExpansionOutcome(e.getMessage() == null ? e.getClass().getName() : e.getMessage(), TerminologyServiceErrorClass.NOSERVICE);
    } catch (Exception e) {
      return new ValueSetExpansionOutcome(e.getMessage() == null ? e.getClass().getName() : e.getMessage(), TerminologyServiceErrorClass.UNKNOWN);
    }
  }

  private ValueSetExpansionOutcome loadFromCache(ValueSet vs, String cacheFn) throws FileNotFoundException, Exception {
    JsonParser parser = new JsonParser();
    Resource r = parser.parse(new FileInputStream(cacheFn));
    if (r instanceof OperationOutcome)
      return new ValueSetExpansionOutcome(((OperationOutcome) r).getIssue().get(0).getDetails().getText(), TerminologyServiceErrorClass.UNKNOWN);
    else {
      vs.setExpansion(((ValueSet) r).getExpansion()); // because what is cached might be from a different value set
      return new ValueSetExpansionOutcome(vs);
    }
  }

  private void saveToCache(Resource res, String cacheFn) throws FileNotFoundException, Exception {
    JsonParser parser = new JsonParser();
    parser.compose(new FileOutputStream(cacheFn), res);
  }

  private String determineCacheId(ValueSet vs, boolean heirarchical) throws Exception {
    // just the content logical definition is hashed
    ValueSet vsid = new ValueSet();
    vsid.setCompose(vs.getCompose());
    JsonParser parser = new JsonParser();
    parser.setOutputStyle(OutputStyle.NORMAL);
    ByteArrayOutputStream b = new  ByteArrayOutputStream();
    parser.compose(b, vsid);
    b.close();
    String s = new String(b.toByteArray(), Charsets.UTF_8);
    // any code systems we can find, we add these too. 
    for (ConceptSetComponent inc : vs.getCompose().getInclude()) {
      CodeSystem cs = fetchCodeSystem(inc.getSystem());
      if (cs != null) {
        String css = cacheValue(cs);
        s = s + css;
      }
    }
    s = s + "-"+Boolean.toString(heirarchical);
    String r = Integer.toString(s.hashCode());
    //    TextFile.stringToFile(s, Utilities.path(cache, r+".id.json"));
    return r;
  }


  private String cacheValue(CodeSystem cs) throws IOException {
    CodeSystem csid = new CodeSystem();
    csid.setId(cs.getId());
    csid.setVersion(cs.getVersion());
    csid.setContent(cs.getContent());
    csid.setHierarchyMeaning(CodeSystemHierarchyMeaning.GROUPEDBY);
    for (ConceptDefinitionComponent cc : cs.getConcept()) 
      csid.getConcept().add(processCSConcept(cc));
    JsonParser parser = new JsonParser();
    parser.setOutputStyle(OutputStyle.NORMAL);
    ByteArrayOutputStream b = new  ByteArrayOutputStream();
    parser.compose(b, csid);
    b.close();
    return new String(b.toByteArray(), Charsets.UTF_8);
  }


  private ConceptDefinitionComponent processCSConcept(ConceptDefinitionComponent cc) {
    ConceptDefinitionComponent ccid = new ConceptDefinitionComponent();
    ccid.setCode(cc.getCode());
    ccid.setDisplay(cc.getDisplay());
    for (ConceptDefinitionComponent cci : cc.getConcept()) 
      ccid.getConcept().add(processCSConcept(cci));
    return ccid;
  }

  public ValueSetExpansionOutcome expandOnServer(ValueSet vs, String fn) throws Exception {
    if (noTerminologyServer)
      return new ValueSetExpansionOutcome("Error expanding ValueSet: running without terminology services", TerminologyServiceErrorClass.NOSERVICE);
    if (expProfile == null)
      throw new Exception("No ExpansionProfile provided");

    try {
      Map<String, String> params = new HashMap<String, String>();
      params.put("_limit", Integer.toString(expandCodesLimit ));
      params.put("_incomplete", "true");
      tlog("Terminology Server: $expand on "+getVSSummary(vs));
      ValueSet result = txServer.expandValueset(vs, expProfile.setIncludeDefinition(false), params);
      return new ValueSetExpansionOutcome(result);  
    } catch (Exception e) {
      return new ValueSetExpansionOutcome("Error expanding ValueSet \""+vs.getUrl()+": "+e.getMessage(), TerminologyServiceErrorClass.UNKNOWN);
    }
  }

  private String getVSSummary(ValueSet vs) {
    CommaSeparatedStringBuilder b = new CommaSeparatedStringBuilder();
    for (ConceptSetComponent cc : vs.getCompose().getInclude())
      b.append("Include "+getIncSummary(cc));
    for (ConceptSetComponent cc : vs.getCompose().getExclude())
      b.append("Exclude "+getIncSummary(cc));
    return b.toString();
  }

  private String getIncSummary(ConceptSetComponent cc) {
    CommaSeparatedStringBuilder b = new CommaSeparatedStringBuilder();
    for (UriType vs : cc.getValueSet())
      b.append(vs.asStringValue());
    String vsd = b.length() > 0 ? " where the codes are in the value sets ("+b.toString()+")" : "";
    String system = cc.getSystem();
    if (cc.hasConcept())
      return Integer.toString(cc.getConcept().size())+" codes from "+system+vsd;
    if (cc.hasFilter()) {
      String s = "";
      for (ConceptSetFilterComponent f : cc.getFilter()) {
        if (!Utilities.noString(s))
          s = s + " & ";
        s = s + f.getProperty()+" "+f.getOp().toCode()+" "+f.getValue();
      }
      return "from "+system+" where "+s+vsd;
    }
    return "All codes from "+system+vsd;
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
    ValueSetExpansionOutcome vse = expandVS(vs, true, false);
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
    ValueSetExpansionOutcome vse = expandVS(vs, true, false);
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

  private ValidationResult verifyCodeExternal(ValueSet vs, Coding coding, boolean tryCache) throws Exception {
    ValidationResult res = vs == null ? null : handleByCache(vs, coding, tryCache);
    if (res != null)
      return res;
    Parameters pin = new Parameters();
    pin.addParameter().setName("coding").setValue(coding);
    if (vs != null)
      pin.addParameter().setName("valueSet").setResource(vs);
    res = serverValidateCode(pin, vs == null);
    if (vs != null) {
      Map<String, ValidationResult> cache = validationCache.get(vs.getUrl());
      cache.put(cacheId(coding), res);
    }
    return res;
  }

  private ValidationResult verifyCodeExternal(ValueSet vs, CodeableConcept cc, boolean tryCache) throws Exception {
    ValidationResult res = handleByCache(vs, cc, tryCache);
    if (res != null)
      return res;
    Parameters pin = new Parameters();
    pin.addParameter().setName("codeableConcept").setValue(cc);
    pin.addParameter().setName("valueSet").setResource(vs);
    res = serverValidateCode(pin, false);
    Map<String, ValidationResult> cache = validationCache.get(vs.getUrl());
    cache.put(cacheId(cc), res);
    return res;
  }

  private ValidationResult serverValidateCode(Parameters pin, boolean doCache) throws Exception {
    if (noTerminologyServer)
      return new ValidationResult(null, null, TerminologyServiceErrorClass.NOSERVICE);
    String cacheName = doCache ? generateCacheName(pin) : null;
    ValidationResult res = loadFromCache(cacheName);
    if (res != null)
      return res;
    tlog("Terminology Server: $validate-code "+describeValidationParameters(pin));
    for (ParametersParameterComponent pp : pin.getParameter())
      if (pp.getName().equals("profile"))
        throw new Error("Can only specify profile in the context");
    if (expProfile == null)
      throw new Exception("No ExpansionProfile provided");
    pin.addParameter().setName("profile").setResource(expProfile);

    Parameters pout = txServer.operateType(ValueSet.class, "validate-code", pin);
    boolean ok = false;
    String message = "No Message returned";
    String display = null;
    TerminologyServiceErrorClass err = TerminologyServiceErrorClass.UNKNOWN;
    for (ParametersParameterComponent p : pout.getParameter()) {
      if (p.getName().equals("result"))
        ok = ((BooleanType) p.getValue()).getValue().booleanValue();
      else if (p.getName().equals("message"))
        message = ((StringType) p.getValue()).getValue();
      else if (p.getName().equals("display"))
        display = ((StringType) p.getValue()).getValue();
      else if (p.getName().equals("cause")) {
        try {
          IssueType it = IssueType.fromCode(((StringType) p.getValue()).getValue());
          if (it == IssueType.UNKNOWN)
            err = TerminologyServiceErrorClass.UNKNOWN;
          else if (it == IssueType.NOTSUPPORTED)
            err = TerminologyServiceErrorClass.VALUESET_UNSUPPORTED;
        } catch (FHIRException e) {
        }
      }
    }
    if (!ok)
      res = new ValidationResult(IssueSeverity.ERROR, message, err);
    else if (display != null)
      res = new ValidationResult(new ConceptDefinitionComponent().setDisplay(display));
    else
      res = new ValidationResult(new ConceptDefinitionComponent());
    saveToCache(res, cacheName);
    return res;
  }


  private void tlog(String msg) {
    //    log(msg);
  }

  @SuppressWarnings("rawtypes")
  private String describeValidationParameters(Parameters pin) {
    CommaSeparatedStringBuilder b = new CommaSeparatedStringBuilder();
    for (ParametersParameterComponent p : pin.getParameter()) {
      if (p.hasValue() && p.getValue() instanceof PrimitiveType) {
        b.append(p.getName()+"="+((PrimitiveType) p.getValue()).asStringValue());
      } else if (p.hasValue() && p.getValue() instanceof Coding) {
        b.append("system="+((Coding) p.getValue()).getSystem());
        b.append("code="+((Coding) p.getValue()).getCode());
        b.append("display="+((Coding) p.getValue()).getDisplay());
      } else if (p.hasValue() && p.getValue() instanceof CodeableConcept) {
        if (((CodeableConcept) p.getValue()).hasCoding()) {
          Coding c = ((CodeableConcept) p.getValue()).getCodingFirstRep();
          b.append("system="+c.getSystem());
          b.append("code="+c.getCode());
          b.append("display="+c.getDisplay());
        } else if (((CodeableConcept) p.getValue()).hasText()) {
          b.append("text="+((CodeableConcept) p.getValue()).getText());
        }
      } else if (p.hasResource() && (p.getResource() instanceof ValueSet)) {
        b.append("valueset="+getVSSummary((ValueSet) p.getResource()));
      } 
    }
    return b.toString();
  }

  private ValidationResult loadFromCache(String fn) throws FileNotFoundException, IOException {
    if (fn == null)
      return null;
    if (!(new File(fn).exists()))
      return null;
    String cnt = TextFile.fileToString(fn);
    if (cnt.startsWith("!error: "))
      return new ValidationResult(IssueSeverity.ERROR, cnt.substring(8));
    else if (cnt.startsWith("!warning: "))
      return new ValidationResult(IssueSeverity.ERROR, cnt.substring(10));
    else
      return new ValidationResult(new ConceptDefinitionComponent().setDisplay(cnt));
  }

  private void saveToCache(ValidationResult res, String cacheName) throws IOException {
    if (cacheName == null)
      return;
    if (res.getDisplay() != null)
      TextFile.stringToFile(res.getDisplay(), cacheName);
    else if (res.getMessage() != null) {
      if (res.getSeverity() == IssueSeverity.WARNING)
        TextFile.stringToFile("!warning: "+res.getMessage(), cacheName);
      else 
        TextFile.stringToFile("!error: "+res.getMessage(), cacheName);
    }
  }

  private String generateCacheName(Parameters pin) throws IOException {
    if (cache == null)
      return null;
    String json = new JsonParser().composeString(pin);
    return Utilities.path(cache, "vc"+Integer.toString(json.hashCode())+".json");
  }

  @Override
  public ValueSetExpansionComponent expandVS(ConceptSetComponent inc, boolean heirachical) throws TerminologyServiceException {
    ValueSet vs = new ValueSet();
    vs.setCompose(new ValueSetComposeComponent());
    vs.getCompose().getInclude().add(inc);
    ValueSetExpansionOutcome vse = expandVS(vs, true, heirachical);
    ValueSet valueset = vse.getValueset();
    if (valueset == null)
      throw new TerminologyServiceException("Error Expanding ValueSet: "+vse.getError());
    return valueset.getExpansion();
  }

  @Override
  public ValidationResult validateCode(String system, String code, String display) {
    try {
      if (codeSystems.containsKey(system) && codeSystems.get(system) != null)
        return verifyCodeInCodeSystem(codeSystems.get(system), system, code, display);
      else 
        return verifyCodeExternal(null, new Coding().setSystem(system).setCode(code).setDisplay(display), false);
    } catch (Exception e) {
      return new ValidationResult(IssueSeverity.FATAL, "Error validating code \""+code+"\" in system \""+system+"\": "+e.getMessage());
    }
  }


  @Override
  public ValidationResult validateCode(Coding code, ValueSet vs) {
    if (codeSystems.containsKey(code.getSystem()) && codeSystems.get(code.getSystem()) != null) 
      try {
        return verifyCodeInCodeSystem(codeSystems.get(code.getSystem()), code.getSystem(), code.getCode(), code.getDisplay());
      } catch (Exception e) {
        return new ValidationResult(IssueSeverity.FATAL, "Error validating code \""+code+"\" in system \""+code.getSystem()+"\": "+e.getMessage());
      }
    else if (vs.hasExpansion()) 
      try {
        return verifyCodeInternal(vs, code.getSystem(), code.getCode(), code.getDisplay());
      } catch (Exception e) {
        return new ValidationResult(IssueSeverity.FATAL, "Error validating code \""+code+"\" in system \""+code.getSystem()+"\": "+e.getMessage());
      }
    else 
      try {
        return verifyCodeExternal(vs, code, true);
      } catch (Exception e) {
        return new ValidationResult(IssueSeverity.WARNING, "Error validating code \""+code+"\" in system \""+code.getSystem()+"\": "+e.getMessage());
      }
  }

  @Override
  public ValidationResult validateCode(CodeableConcept code, ValueSet vs) {
    try {
      if (vs.hasExpansion()) 
        return verifyCodeInternal(vs, code);
      else {
        // we'll try expanding first; if that doesn't work, then we'll just pass it to the server to validate 
        // ... could be a problem if the server doesn't have the code systems we have locally, so we try not to depend on the server
        try {
          ValueSetExpansionOutcome vse = expandVS(vs, true, false);
          if (vse.getValueset() != null && !hasTooCostlyExpansion(vse.getValueset()))
            return verifyCodeInternal(vse.getValueset(), code);
        } catch (Exception e) {
          // failed? we'll just try the server
        }        
        return verifyCodeExternal(vs, code, true);
      }
    } catch (Exception e) {
      return new ValidationResult(IssueSeverity.FATAL, "Error validating code \""+code.toString()+"\": "+e.getMessage(), TerminologyServiceErrorClass.SERVER_ERROR);
    }
  }


  private boolean hasTooCostlyExpansion(ValueSet valueset) {
    return valueset != null && valueset.hasExpansion() && ToolingExtensions.hasExtension(valueset.getExpansion(), "http://hl7.org/fhir/StructureDefinition/valueset-toocostly");
  }

  @Override
  public ValidationResult validateCode(String system, String code, String display, ValueSet vs) {
    try {
      if (system == null && display == null)
        return verifyCodeInternal(vs, code);
      if ((codeSystems.containsKey(system)  && codeSystems.get(system) != null) || vs.hasExpansion()) 
        return verifyCodeInternal(vs, system, code, display);
      else 
        return verifyCodeExternal(vs, new Coding().setSystem(system).setCode(code).setDisplay(display), true);
    } catch (Exception e) {
      return new ValidationResult(IssueSeverity.FATAL, "Error validating code \""+code+"\" in system \""+system+"\": "+e.getMessage(), TerminologyServiceErrorClass.SERVER_ERROR);
    }
  }

  @Override
  public ValidationResult validateCode(String system, String code, String display, ConceptSetComponent vsi) {
    try {
      ValueSet vs = new ValueSet();
      vs.setUrl(Utilities.makeUuidUrn());
      vs.getCompose().addInclude(vsi);
      return verifyCodeExternal(vs, new Coding().setSystem(system).setCode(code).setDisplay(display), true);
    } catch (Exception e) {
      return new ValidationResult(IssueSeverity.FATAL, "Error validating code \""+code+"\" in system \""+system+"\": "+e.getMessage());
    }
  }

  public void initTS(String cachePath, String tsServer) throws Exception {
    cache = cachePath;
    this.tsServer = tsServer;
    expansionCache = new ValueSetExpansionCache(this, null);
    validationCachePath = Utilities.path(cachePath, "validation.cache");
    try {
      loadValidationCache();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  protected void loadValidationCache() throws JsonSyntaxException, Exception {
  }

  @Override
  public List<ConceptMap> findMapsForSource(String url) {
    List<ConceptMap> res = new ArrayList<ConceptMap>();
    for (ConceptMap map : maps.values())
      if (((Reference) map.getSource()).getReference().equals(url)) 
        res.add(map);
    return res;
  }

  private ValidationResult verifyCodeInternal(ValueSet vs, CodeableConcept code) throws Exception {
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

  private ValidationResult verifyCodeInternal(ValueSet vs, String system, String code, String display) throws Exception {
    if (vs.hasExpansion())
      return verifyCodeInExpansion(vs, system, code, display);
    else {
      ValueSetExpansionOutcome vse = expansionCache.getExpander().expand(vs, null);
      if (vse.getValueset() != null) 
        return verifyCodeExternal(vs, new Coding().setSystem(system).setCode(code).setDisplay(display), false);
      else
        return verifyCodeInExpansion(vse.getValueset(), system, code, display);
    }
  }

  private ValidationResult verifyCodeInternal(ValueSet vs, String code) throws FileNotFoundException, ETooCostly, IOException, FHIRException {
    if (vs.hasExpansion())
      return verifyCodeInExpansion(vs, code);
    else {
      ValueSetExpansionOutcome vse = expansionCache.getExpander().expand(vs, null);
      if (vse.getValueset() == null)
        return new ValidationResult(IssueSeverity.ERROR, vse.getError(), vse.getErrorClass());
      else
        return verifyCodeInExpansion(vse.getValueset(), code);
    }
  }

  private ValidationResult verifyCodeInCodeSystem(CodeSystem cs, String system, String code, String display) throws Exception {
    ConceptDefinitionComponent cc = findCodeInConcept(cs.getConcept(), code);
    if (cc == null)
      if (cs.getContent().equals(CodeSystem.CodeSystemContentMode.COMPLETE))
        return new ValidationResult(IssueSeverity.ERROR, "Unknown Code "+code+" in "+cs.getUrl());
      else if (!cs.getContent().equals(CodeSystem.CodeSystemContentMode.NOTPRESENT))
        return new ValidationResult(IssueSeverity.WARNING, "Unknown Code "+code+" in partial code list of "+cs.getUrl());
      else 
        return verifyCodeExternal(null, new Coding().setSystem(system).setCode(code).setDisplay(display), false);
    //
    //        return new ValidationResult(IssueSeverity.WARNING, "A definition was found for "+cs.getUrl()+", but it has no codes in the definition");
    //      return new ValidationResult(IssueSeverity.ERROR, "Unknown Code "+code+" in "+cs.getUrl());
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
    return new ValidationResult(IssueSeverity.WARNING, "Display Name for "+code+" must be one of '"+b.toString()+"'", cc);
  }


  private ValidationResult verifyCodeInExpansion(ValueSet vs, String system,String code, String display) {
    ValueSetExpansionContainsComponent cc = findCode(vs.getExpansion().getContains(), code);
    if (cc == null)
      return new ValidationResult(IssueSeverity.ERROR, "Unknown Code "+code+" in "+vs.getUrl());
    if (display == null)
      return new ValidationResult(new ConceptDefinitionComponent().setCode(code).setDisplay(cc.getDisplay()));
    if (cc.hasDisplay()) {
      if (display.equalsIgnoreCase(cc.getDisplay()))
        return new ValidationResult(new ConceptDefinitionComponent().setCode(code).setDisplay(cc.getDisplay()));
      return new ValidationResult(IssueSeverity.WARNING, "Display Name for "+code+" must be '"+cc.getDisplay()+"'", new ConceptDefinitionComponent().setCode(code).setDisplay(cc.getDisplay()));
    }
    return null;
  }

  private ValidationResult verifyCodeInExpansion(ValueSet vs, String code) throws FHIRException {
    if (vs.getExpansion().hasExtension("http://hl7.org/fhir/StructureDefinition/valueset-toocostly")) {
      throw new FHIRException("Unable to validate core - value set is too costly to expand"); 
    } else {
      ValueSetExpansionContainsComponent cc = findCode(vs.getExpansion().getContains(), code);
      if (cc == null)
        return new ValidationResult(IssueSeverity.ERROR, "Unknown Code "+code+" in "+vs.getUrl());
      return null;
    }
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

  public Set<String> getNonSupportedCodeSystems() {
    return nonSupportedCodeSystems;
  }

  public boolean isCanRunWithoutTerminology() {
    return canRunWithoutTerminology;
  }

  public void setCanRunWithoutTerminology(boolean canRunWithoutTerminology) {
    this.canRunWithoutTerminology = canRunWithoutTerminology;
  }

  public int getExpandCodesLimit() {
    return expandCodesLimit;
  }

  public void setExpandCodesLimit(int expandCodesLimit) {
    this.expandCodesLimit = expandCodesLimit;
  }

  public void setLogger(ILoggingService logger) {
    this.logger = logger;
  }

  public ExpansionProfile getExpansionProfile() {
    return expProfile;
  }

  public void setExpansionProfile(ExpansionProfile expProfile) {
    this.expProfile = expProfile;
  }

  @Override
  public boolean isNoTerminologyServer() {
    return noTerminologyServer;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public Set<String> getResourceNamesAsSet() {
    Set<String> res = new HashSet<String>();
    res.addAll(getResourceNames());
    return res;
  }

  public void reportStatus(JsonObject json) {
    json.addProperty("codeystem-count", codeSystems.size());
    json.addProperty("valueset-count", valueSets.size());
    json.addProperty("conceptmap-count", maps.size());
    json.addProperty("transforms-count", transforms.size());
    json.addProperty("structures-count", profiles.size());
  }

  public void cacheResource(Resource r) throws Exception {
    if (r instanceof ValueSet)
      seeValueSet(((ValueSet) r).getUrl(), (ValueSet) r);
    else if (r instanceof CodeSystem)
      seeCodeSystem(((CodeSystem) r).getUrl(), (CodeSystem) r);
    else if (r instanceof StructureDefinition) {
      StructureDefinition sd = (StructureDefinition) r;
      if ("http://hl7.org/fhir/StructureDefinition/Extension".equals(sd.getBaseDefinition()))
        seeExtensionDefinition(sd.getUrl(), sd);
      else if (sd.getDerivation() == TypeDerivationRule.CONSTRAINT) 
        seeProfile(sd.getUrl(), sd);
    }
  }

  public void dropResource(String type, String id) throws FHIRException {
    if (type.equals("ValueSet"))
      dropValueSet(id);   
    if (type.equals("CodeSystem"))
      dropCodeSystem(id);   
    if (type.equals("StructureDefinition")) {
      dropProfile(id);   
      dropExtensionDefinition(id);
    }
  }

  public boolean isAllowLoadingDuplicates() {
    return allowLoadingDuplicates;
  }

  public void setAllowLoadingDuplicates(boolean allowLoadingDuplicates) {
    this.allowLoadingDuplicates = allowLoadingDuplicates;
  }

  @Override
  public StructureDefinition fetchTypeDefinition(String typeName) {
    return fetchResource(StructureDefinition.class, "http://hl7.org/fhir/StructureDefinition/"+typeName);
  }


}
