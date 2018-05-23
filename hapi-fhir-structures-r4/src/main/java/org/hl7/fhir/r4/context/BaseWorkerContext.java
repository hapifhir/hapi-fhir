package org.hl7.fhir.r4.context;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

import org.apache.commons.codec.Charsets;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.r4.formats.IParser.OutputStyle;
import org.hl7.fhir.r4.conformance.ProfileUtilities;
import org.hl7.fhir.r4.context.BaseWorkerContext.NullTranslator;
import org.hl7.fhir.r4.context.IWorkerContext.ValidationResult;
import org.hl7.fhir.r4.context.IWorkerContext.ILoggingService.LogCategory;
import org.hl7.fhir.r4.formats.JsonParser;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.CodeSystem.CodeSystemContentMode;
import org.hl7.fhir.r4.model.CodeSystem.CodeSystemHierarchyMeaning;
import org.hl7.fhir.r4.model.CodeSystem.ConceptDefinitionComponent;
import org.hl7.fhir.r4.model.CodeSystem.ConceptDefinitionDesignationComponent;
import org.hl7.fhir.r4.model.ElementDefinition.ElementDefinitionBindingComponent;
import org.hl7.fhir.r4.model.NamingSystem.NamingSystemIdentifierType;
import org.hl7.fhir.r4.model.NamingSystem.NamingSystemUniqueIdComponent;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.ConceptMap;
import org.hl7.fhir.r4.model.Constants;
import org.hl7.fhir.r4.model.ExpansionProfile;
import org.hl7.fhir.r4.model.MetadataResource;
import org.hl7.fhir.r4.model.NamingSystem;
import org.hl7.fhir.r4.model.OperationDefinition;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Parameters.ParametersParameterComponent;
import org.hl7.fhir.r4.model.StructureDefinition.StructureDefinitionKind;
import org.hl7.fhir.r4.model.PrimitiveType;
import org.hl7.fhir.r4.model.Questionnaire;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.Resource;
import org.hl7.fhir.r4.model.SearchParameter;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.StructureDefinition;
import org.hl7.fhir.r4.model.StructureMap;
import org.hl7.fhir.r4.model.UriType;
import org.hl7.fhir.r4.model.ValueSet;
import org.hl7.fhir.r4.model.ValueSet.ConceptSetComponent;
import org.hl7.fhir.r4.model.ValueSet.ConceptSetFilterComponent;
import org.hl7.fhir.r4.model.ValueSet.ValueSetComposeComponent;
import org.hl7.fhir.r4.model.ValueSet.ValueSetExpansionComponent;
import org.hl7.fhir.r4.model.ValueSet.ValueSetExpansionContainsComponent;
import org.hl7.fhir.r4.terminologies.ValueSetExpander.ETooCostly;
import org.hl7.fhir.r4.terminologies.ValueSetExpander.TerminologyServiceErrorClass;
import org.hl7.fhir.r4.terminologies.ValueSetExpander.ValueSetExpansionOutcome;
import org.hl7.fhir.r4.terminologies.ValueSetExpanderFactory;
import org.hl7.fhir.r4.terminologies.ValueSetExpansionCache;
import org.hl7.fhir.r4.utils.ToolingExtensions;
import org.hl7.fhir.r4.utils.client.FHIRToolingClient;
import org.hl7.fhir.exceptions.DefinitionException;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.exceptions.NoTerminologyServiceException;
import org.hl7.fhir.exceptions.TerminologyServiceException;
import org.hl7.fhir.utilities.CommaSeparatedStringBuilder;
import org.hl7.fhir.utilities.OIDUtils;
import org.hl7.fhir.utilities.TextFile;
import org.hl7.fhir.utilities.TranslationServices;
import org.hl7.fhir.utilities.Utilities;
import org.hl7.fhir.utilities.validation.ValidationMessage;
import org.hl7.fhir.utilities.validation.ValidationMessage.IssueSeverity;
import org.hl7.fhir.utilities.validation.ValidationMessage.IssueType;
import org.hl7.fhir.utilities.validation.ValidationMessage.Source;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import ca.uhn.fhir.rest.annotation.Metadata;

public abstract class BaseWorkerContext implements IWorkerContext {

  private Object lock = new Object(); // used as a lock for the data that follows
  
  private Map<String, Map<String, Resource>> allResourcesById = new HashMap<String, Map<String, Resource>>();
  // all maps are to the full URI
  private Map<String, CodeSystem> codeSystems = new HashMap<String, CodeSystem>();
  private Set<String> nonSupportedCodeSystems = new HashSet<String>();
  private Map<String, ValueSet> valueSets = new HashMap<String, ValueSet>();
  private Map<String, ConceptMap> maps = new HashMap<String, ConceptMap>();
  private Map<String, StructureMap> transforms = new HashMap<String, StructureMap>();
//  private Map<String, StructureDefinition> profiles = new HashMap<String, StructureDefinition>();
  private Map<String, StructureDefinition> structures = new HashMap<String, StructureDefinition>();
//  private Map<String, StructureDefinition> extensionDefinitions = new HashMap<String, StructureDefinition>();
  private Map<String, SearchParameter> searchParameters = new HashMap<String, SearchParameter>();
  private Map<String, Questionnaire> questionnaires = new HashMap<String, Questionnaire>();
  private Map<String, OperationDefinition> operations = new HashMap<String, OperationDefinition>();
  private List<NamingSystem> systems = new ArrayList<NamingSystem>();

  
  private ValueSetExpansionCache expansionCache = new ValueSetExpansionCache(this, lock);
  protected boolean cacheValidation; // if true, do an expansion and cache the expansion
  private Set<String> failed = new HashSet<String>(); // value sets for which we don't try to do expansion, since the first attempt to get a comprehensive expansion was not successful
  protected Map<String, Map<String, ValidationResult>> validationCache = new HashMap<String, Map<String,ValidationResult>>();
  protected String tsServer;
  protected String validationCachePath;
  protected String name;
  private boolean allowLoadingDuplicates;

  // private ValueSetExpansionCache expansionCache; //   

  protected FHIRToolingClient txServer;
  private Bundle bndCodeSystems;
  private boolean canRunWithoutTerminology;
  protected boolean noTerminologyServer;
  protected String cache;
  private int expandCodesLimit = 1000;
  protected ILoggingService logger;
  protected ExpansionProfile expProfile;
  private TranslationServices translator = new NullTranslator();

  public BaseWorkerContext() {
    super();
  }

  public BaseWorkerContext(Map<String, CodeSystem> codeSystems, Map<String, ValueSet> valueSets, Map<String, ConceptMap> maps,  Map<String, StructureDefinition> profiles) {
    super();
    this.codeSystems = codeSystems;
    this.valueSets = valueSets;
    this.maps = maps;
    this.structures = profiles;
  }

  protected void copy(BaseWorkerContext other) {
    synchronized (other.lock) { // tricky, because you need to lock this as well, but it's really not in use yet 
      allResourcesById.putAll(other.allResourcesById);
      translator = other.translator;
      codeSystems.putAll(other.codeSystems);
      nonSupportedCodeSystems.addAll(other.nonSupportedCodeSystems);
      valueSets.putAll(other.valueSets);
      maps.putAll(other.maps);
      transforms.putAll(other.transforms);
      structures.putAll(other.structures);
      searchParameters.putAll(other.searchParameters);
      questionnaires.putAll(other.questionnaires);
      operations.putAll(other.operations);
      systems.addAll(other.systems);

      allowLoadingDuplicates = other.allowLoadingDuplicates;
      cacheValidation = other.cacheValidation;
      tsServer = other.tsServer;
      validationCachePath = other.validationCachePath;
      name = other.name;
      txServer = other.txServer;
      bndCodeSystems = other.bndCodeSystems;
      canRunWithoutTerminology = other.canRunWithoutTerminology;
      noTerminologyServer = other.noTerminologyServer;
      cache = other.cache;
      expandCodesLimit = other.expandCodesLimit;
      logger = other.logger;
      expProfile = other.expProfile;
    }
  }
  
  public void cacheResource(Resource r) throws FHIRException {
    synchronized (lock) {
      Map<String, Resource> map = allResourcesById.get(r.fhirType());
      if (map == null) {
        map = new HashMap<String, Resource>();
        allResourcesById.put(r.fhirType(), map);
      }
      map.put(r.getId(), r);

      if (r instanceof MetadataResource) {
        MetadataResource m = (MetadataResource) r;
        String url = m.getUrl();
        if (!allowLoadingDuplicates && hasResource(r.getClass(), url))
          throw new DefinitionException("Duplicate Resource " + url);
        if (r instanceof StructureDefinition)
          seeMetadataResource((StructureDefinition) m, structures, false);
        else if (r instanceof ValueSet)
          seeMetadataResource((ValueSet) m, valueSets, false);
        else if (r instanceof CodeSystem)
          seeMetadataResource((CodeSystem) m, codeSystems, false);
        else if (r instanceof SearchParameter)
          seeMetadataResource((SearchParameter) m, searchParameters, false);
        else if (r instanceof OperationDefinition)
          seeMetadataResource((OperationDefinition) m, operations, false);
        else if (r instanceof Questionnaire)
          seeMetadataResource((Questionnaire) m, questionnaires, true);
        else if (r instanceof ConceptMap)
          seeMetadataResource((ConceptMap) m, maps, false);
        else if (r instanceof StructureMap)
          seeMetadataResource((StructureMap) m, transforms, false);
        else if (r instanceof NamingSystem)
          systems.add((NamingSystem) r);
      }
    }
  }

  /*
   *  Compare business versions, returning "true" if the candidate newer version is in fact newer than the oldVersion
   *  Comparison will work for strictly numeric versions as well as multi-level versions separated by ., -, _, : or space
   *  Failing that, it will do unicode-based character ordering.
   *  E.g. 1.5.3 < 1.14.3
   *       2017-3-10 < 2017-12-7
   *       A3 < T2
   */
  private boolean laterVersion(String newVersion, String oldVersion) {
    // Compare business versions, retur
    newVersion = newVersion.trim();
    oldVersion = oldVersion.trim();
    if (StringUtils.isNumeric(newVersion) && StringUtils.isNumeric(oldVersion))
      return Double.parseDouble(newVersion) > Double.parseDouble(oldVersion);
    else if (hasDelimiter(newVersion, oldVersion, "."))
      return laterDelimitedVersion(newVersion, oldVersion, "\\.");
    else if (hasDelimiter(newVersion, oldVersion, "-"))
      return laterDelimitedVersion(newVersion, oldVersion, "\\-");
    else if (hasDelimiter(newVersion, oldVersion, "_"))
      return laterDelimitedVersion(newVersion, oldVersion, "\\_");
    else if (hasDelimiter(newVersion, oldVersion, ":"))
      return laterDelimitedVersion(newVersion, oldVersion, "\\:");
    else if (hasDelimiter(newVersion, oldVersion, " "))
      return laterDelimitedVersion(newVersion, oldVersion, "\\ ");
    else {
      return newVersion.compareTo(oldVersion) > 0;
    }
  }
  
  /*
   * Returns true if both strings include the delimiter and have the same number of occurrences of it
   */
  private boolean hasDelimiter(String s1, String s2, String delimiter) {
    return s1.contains(delimiter) && s2.contains(delimiter) && s1.split(delimiter).length == s2.split(delimiter).length;
  }

  private boolean laterDelimitedVersion(String newVersion, String oldVersion, String delimiter) {
    String[] newParts = newVersion.split(delimiter);
    String[] oldParts = oldVersion.split(delimiter);
    for (int i = 0; i < newParts.length; i++) {
      if (!newParts[i].equals(oldParts[i]))
        return laterVersion(newParts[i], oldParts[i]);
    }
    // This should never happen
    throw new Error("Delimited versions have exact match for delimiter '"+delimiter+"' : "+ Arrays.asList(newParts)+" vs "+Arrays.asList(oldParts));
  }
  
  protected <T extends MetadataResource> void seeMetadataResource(T r, Map<String, T> map, boolean addId) throws FHIRException {
    if (addId)
      map.put(r.getId(), r); // todo: why?
    if (!map.containsKey(r.getUrl()))
      map.put(r.getUrl(), r);
    else {
      // If this resource already exists, see if it's the newest business version.  The default resource to return if not qualified is the most recent business version
      MetadataResource existingResource = (MetadataResource)map.get(r.getUrl());
      if (r.hasVersion() && existingResource.hasVersion() && !r.getVersion().equals(existingResource.getVersion())) {
        if (laterVersion(r.getVersion(), existingResource.getVersion())) {
          map.remove(r.getUrl());
          map.put(r.getUrl(), r);
        }
      } else
        map.remove(r.getUrl());
        map.put(r.getUrl(), r);
//        throw new FHIRException("Multiple declarations of resource with same canonical URL (" + r.getUrl() + ") and version (" + (r.hasVersion() ? r.getVersion() : "" ) + ")");
    }
    if (r.hasVersion())
      map.put(r.getUrl()+"|"+r.getVersion(), r);
  }  

  @Override
  public CodeSystem fetchCodeSystem(String system) {
    synchronized (lock) {
      return codeSystems.get(system);
    }
  } 

  @Override
  public boolean supportsSystem(String system) throws TerminologyServiceException {
    synchronized (lock) {
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
              log("==============!! Running without terminology server !! ==============");
              log("txServer = "+txServer.getAddress());
              log("Error = "+e.getMessage()+"");
              log("=====================================================================");
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
    res = serverValidateCode(pin, tryCache);
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
    else if (message != null) 
      res = new ValidationResult(IssueSeverity.WARNING, message, new ConceptDefinitionComponent().setDisplay(display));
    else if (display != null)
      res = new ValidationResult(new ConceptDefinitionComponent().setDisplay(display));
    else
      res = new ValidationResult(new ConceptDefinitionComponent());
    saveToCache(res, cacheName);
    return res;
  }


  protected void tlog(String msg) {
    System.out.println("-tx: "+msg);
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
      CodeSystem cs = null;
      synchronized (lock) {
        cs = codeSystems.get(system);
      }
      
      if (cs != null && cs.getContent() == CodeSystemContentMode.COMPLETE)
        return verifyCodeInCodeSystem(cs, system, code, display);
      else 
        return verifyCodeExternal(null, new Coding().setSystem(system).setCode(code).setDisplay(display), false);
    } catch (Exception e) {
      return new ValidationResult(IssueSeverity.FATAL, "Error validating code \""+code+"\" in system \""+system+"\": "+e.getMessage());
    }
  }


  @Override
  public ValidationResult validateCode(Coding code, ValueSet vs) {
    CodeSystem cs = null;
    synchronized (lock) {
      cs = codeSystems.get(code.getSystem());
    }
    if (cs != null) 
      try {
        return verifyCodeInCodeSystem(cs, code.getSystem(), code.getCode(), code.getDisplay());
      } catch (Exception e) {
        return new ValidationResult(IssueSeverity.FATAL, "Error validating code \""+code+"\" in system \""+code.getSystem()+"\": "+e.getMessage());
      }
    else if (vs != null && vs.hasExpansion()) 
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
      CodeSystem cs = null;
      synchronized (lock) {
        cs = codeSystems.get(system);
      }
      
      if (cs != null || vs.hasExpansion()) 
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

  public void initTS(String cachePath) throws Exception {
    cache = cachePath;
    expansionCache = new ValueSetExpansionCache(this, null, lock);
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
  public List<ConceptMap> findMapsForSource(String url) throws FHIRException {
    synchronized (lock) {
      List<ConceptMap> res = new ArrayList<ConceptMap>();
      for (ConceptMap map : maps.values())
        if (((Reference) map.getSource()).getReference().equals(url)) 
          res.add(map);
      return res;
    }
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
    ValueSetExpansionContainsComponent cc = findCode(vs.getExpansion().getContains(), code, system);
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
      ValueSetExpansionContainsComponent cc = findCode(vs.getExpansion().getContains(), code, null);
      if (cc == null)
        return new ValidationResult(IssueSeverity.ERROR, "Unknown Code "+code+" in "+vs.getUrl());
      return null;
    }
  }

  private ValueSetExpansionContainsComponent findCode(List<ValueSetExpansionContainsComponent> contains, String code, String system) {
    for (ValueSetExpansionContainsComponent cc : contains) {
      if (code.equals(cc.getCode()) && (system == null || cc.getSystem().equals(system)))
        return cc;
      ValueSetExpansionContainsComponent c = findCode(cc.getContains(), code, system);
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

  public boolean isAllowLoadingDuplicates() {
    return allowLoadingDuplicates;
  }

  public void setAllowLoadingDuplicates(boolean allowLoadingDuplicates) {
    this.allowLoadingDuplicates = allowLoadingDuplicates;
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T extends Resource> T fetchResourceWithException(Class<T> class_, String uri) throws FHIRException {
       if (class_ == StructureDefinition.class)
      uri = ProfileUtilities.sdNs(uri);
    synchronized (lock) {

      if (uri.startsWith("http:") || uri.startsWith("https:")) {
        String version = null;
        if (uri.contains("#"))
          uri = uri.substring(0, uri.indexOf("#"));
        if (class_ == Resource.class || class_ == null) {
          if (structures.containsKey(uri))
            return (T) structures.get(uri);
          if (valueSets.containsKey(uri))
            return (T) valueSets.get(uri);
          if (codeSystems.containsKey(uri))
            return (T) codeSystems.get(uri);
          if (operations.containsKey(uri))
            return (T) operations.get(uri);
          if (searchParameters.containsKey(uri))
            return (T) searchParameters.get(uri);
          if (maps.containsKey(uri))
            return (T) maps.get(uri);
          if (transforms.containsKey(uri))
            return (T) transforms.get(uri);
          if (questionnaires.containsKey(uri))
            return (T) questionnaires.get(uri);
          return null;      
        } else if (class_ == StructureDefinition.class) {
          return (T) structures.get(uri);
        } else if (class_ == ValueSet.class) {
          return (T) valueSets.get(uri);
        } else if (class_ == CodeSystem.class) {
          return (T) codeSystems.get(uri);
        } else if (class_ == ConceptMap.class) {
          return (T) maps.get(uri);
        } else if (class_ == OperationDefinition.class) {
          OperationDefinition od = operations.get(uri);
          return (T) od;
        } else if (class_ == SearchParameter.class) {
          SearchParameter res = searchParameters.get(uri);
          if (res == null) {
            StringBuilder b = new StringBuilder();
            for (String s : searchParameters.keySet()) {
              b.append(s);
              b.append("\r\n");
            }
          }
          if (res != null)
            return (T) res;
        }
      }
      if (class_ == Questionnaire.class)
        return (T) questionnaires.get(uri);
      if (class_ == null) {
        if (uri.matches(Constants.URI_REGEX) && !uri.contains("ValueSet"))
          return null;

        // it might be a special URL.
        if (Utilities.isAbsoluteUrl(uri) || uri.startsWith("ValueSet/")) {
          Resource res = findTxValueSet(uri);
          if (res != null)
            return (T) res;
        }
        return null;      
      }      
      throw new FHIRException("not done yet: can't fetch "+uri);
    }
  }

  private Set<String> notCanonical = new HashSet<String>();
  
  private MetadataResource findTxValueSet(String uri) {
    MetadataResource res = expansionCache.getStoredResource(uri);
    if (res != null)
      return res;
    synchronized (lock) {
      if (notCanonical.contains(uri))
        return null;
    }
    try {
      tlog("get canonical "+uri);
      res = txServer.getCanonical(ValueSet.class, uri);
    } catch (Exception e) {
      synchronized (lock) {
        notCanonical.add(uri);
      }
      return null;
    }
    if (res != null)
      try {
        expansionCache.storeResource(res);
      } catch (IOException e) {
      }
    return res;
  }

  @Override
  public Resource fetchResourceById(String type, String uri) {
    synchronized (lock) {
      String[] parts = uri.split("\\/");
      if (!Utilities.noString(type) && parts.length == 1)
        return allResourcesById.get(type).get(parts[0]);
      if (parts.length >= 2) {
        if (!Utilities.noString(type))
          if (!type.equals(parts[parts.length-2])) 
            throw new Error("Resource type mismatch for "+type+" / "+uri);
        return allResourcesById.get(parts[parts.length-2]).get(parts[parts.length-1]);
      } else
        throw new Error("Unable to process request for resource for "+type+" / "+uri);
    }
  }

  public <T extends Resource> T fetchResource(Class<T> class_, String uri) {
    try {
      return fetchResourceWithException(class_, uri);
    } catch (FHIRException e) {
      throw new Error(e);
    }
  }
  
  @Override
  public <T extends Resource> boolean hasResource(Class<T> class_, String uri) {
    try {
      return fetchResourceWithException(class_, uri) != null;
    } catch (Exception e) {
      return false;
    }
  }

  @Override
  public ValueSetExpansionOutcome expandVS(ElementDefinitionBindingComponent binding, boolean cacheOk, boolean heirarchical) throws FHIRException {
    ValueSet vs = null;
    if (binding.hasValueSetCanonicalType()) {
      vs = fetchResource(ValueSet.class, binding.getValueSetCanonicalType().getValue());
      if (vs == null)
        throw new FHIRException("Unable to resolve value Set "+binding.getValueSetCanonicalType().getValue());
    } else {
      vs = fetchResource(ValueSet.class, binding.getValueSetUriType().asStringValue());
      if (vs == null)
        throw new FHIRException("Unable to resolve value Set "+binding.getValueSetUriType().asStringValue());
    }
    return expandVS(vs, cacheOk, heirarchical);
  }
  

  public TranslationServices translator() {
    return translator;
  }

  public void setTranslator(TranslationServices translator) {
    this.translator = translator;
  }
  
  public class NullTranslator implements TranslationServices {

    @Override
    public String translate(String context, String value, String targetLang) {
      return value;
    }

    @Override
    public String translate(String context, String value) {
      return value;
    }

    @Override
    public String toStr(float value) {
      return null;
    }

    @Override
    public String toStr(Date value) {
      return null;
    }

    @Override
    public String translateAndFormat(String contest, String lang, String value, Object... args) {
      return String.format(value, args);
    }

    @Override
    public Map<String, String> translations(String value) {
      // TODO Auto-generated method stub
      return null;
    }

    @Override
    public Set<String> listTranslations(String category) {
      // TODO Auto-generated method stub
      return null;
    }

  }
  
  public void reportStatus(JsonObject json) {
    synchronized (lock) {
      json.addProperty("codeystem-count", codeSystems.size());
      json.addProperty("valueset-count", valueSets.size());
      json.addProperty("conceptmap-count", maps.size());
      json.addProperty("transforms-count", transforms.size());
      json.addProperty("structures-count", structures.size());
    }
  }


  public void dropResource(Resource r) throws FHIRException {
    dropResource(r.fhirType(), r.getId());   
  }

  public void dropResource(String fhirType, String id) {
    synchronized (lock) {

      Map<String, Resource> map = allResourcesById.get(fhirType);
      if (map == null) {
        map = new HashMap<String, Resource>();
        allResourcesById.put(fhirType, map);
      }
      if (map.containsKey(id))
        map.remove(id);

      if (fhirType.equals("StructureDefinition"))
        dropMetadataResource(structures, id);
      else if (fhirType.equals("ValueSet"))
        dropMetadataResource(valueSets, id);
      else if (fhirType.equals("CodeSystem"))
        dropMetadataResource(codeSystems, id);
      else if (fhirType.equals("OperationDefinition"))
        dropMetadataResource(operations, id);
      else if (fhirType.equals("Questionnaire"))
        dropMetadataResource(questionnaires, id);
      else if (fhirType.equals("ConceptMap"))
        dropMetadataResource(maps, id);
      else if (fhirType.equals("StructureMap"))
        dropMetadataResource(transforms, id);
      else if (fhirType.equals("NamingSystem"))
        for (int i = systems.size()-1; i >= 0; i--) {
          if (systems.get(i).getId().equals(id))
            systems.remove(i);
        }
    }
  }

  private <T extends MetadataResource> void dropMetadataResource(Map<String, T> map, String id) {
    T res = map.get(id);
    if (res != null) {
      map.remove(id);
      if (map.containsKey(res.getUrl()))
        map.remove(res.getUrl());
      if (res.getVersion() != null)
        if (map.containsKey(res.getUrl()+"|"+res.getVersion()))
          map.remove(res.getUrl()+"|"+res.getVersion());
    }
  }

  @Override
  public List<MetadataResource> allConformanceResources() {
    synchronized (lock) {
      List<MetadataResource> result = new ArrayList<MetadataResource>();
      result.addAll(structures.values());
      result.addAll(codeSystems.values());
      result.addAll(valueSets.values());
      result.addAll(maps.values());
      result.addAll(transforms.values());
      return result;
    }
  }
  
  public void addNonSupportedCodeSystems(String s) {
    synchronized (lock) {
      nonSupportedCodeSystems.add(s);
    }   
   }

  public String listNonSupportedSystems() {
    synchronized (lock) {
      String sl = null;
      for (String s : nonSupportedCodeSystems)
        sl = sl == null ? s : sl + "\r\n" + s;
      return sl;
    }
  }


  public int totalCount() {
    synchronized (lock) {
      return valueSets.size() +  maps.size() + structures.size() + transforms.size();
    }
  }
  
  public List<ConceptMap> listMaps() {
    List<ConceptMap> m = new ArrayList<ConceptMap>();
    synchronized (lock) {
      m.addAll(maps.values());    
    }
    return m;
  }
  
  public List<StructureMap> listTransforms() {
    List<StructureMap> m = new ArrayList<StructureMap>();
    synchronized (lock) {
      m.addAll(transforms.values());    
    }
    return m;
  }
  
  public StructureMap getTransform(String code) {
    synchronized (lock) {
      return transforms.get(code);
    }
  }

  public List<StructureDefinition> listStructures() {
    List<StructureDefinition> m = new ArrayList<StructureDefinition>();
    synchronized (lock) {
      m.addAll(structures.values());    
    }
    return m;
  }

  public StructureDefinition getStructure(String code) {
    synchronized (lock) {
      return structures.get(code);
    }
  }

  public void setCache(ValueSetExpansionCache cache) {
    synchronized (lock) {
      this.expansionCache = cache;
    }
  }

  @Override
  public String oid2Uri(String oid) {
    synchronized (lock) {
      String uri = OIDUtils.getUriForOid(oid);
      if (uri != null)
        return uri;
      for (NamingSystem ns : systems) {
        if (hasOid(ns, oid)) {
          uri = getUri(ns);
          if (uri != null)
            return null;
        }
      }
    }
    return null;
  }
  

  private String getUri(NamingSystem ns) {
    for (NamingSystemUniqueIdComponent id : ns.getUniqueId()) {
      if (id.getType() == NamingSystemIdentifierType.URI)
        return id.getValue();
    }
    return null;
  }

  private boolean hasOid(NamingSystem ns, String oid) {
    for (NamingSystemUniqueIdComponent id : ns.getUniqueId()) {
      if (id.getType() == NamingSystemIdentifierType.OID && id.getValue().equals(oid))
        return true;
    }
    return false;
  }

  public void cacheVS(JsonObject json, Map<String, ValidationResult> t) {
    synchronized (lock) {
      validationCache.put(json.get("url").getAsString(), t);
    }
  }

  public SearchParameter getSearchParameter(String code) {
    synchronized (lock) {
      return searchParameters.get(code);
    }
  }

  
}
