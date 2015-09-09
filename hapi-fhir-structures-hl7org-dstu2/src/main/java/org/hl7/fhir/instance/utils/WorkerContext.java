package org.hl7.fhir.instance.utils;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hl7.fhir.instance.client.FeedFormat;
import org.hl7.fhir.instance.client.IFHIRClient;
import org.hl7.fhir.instance.client.ResourceFormat;
import org.hl7.fhir.instance.model.Bundle;
import org.hl7.fhir.instance.model.ConceptMap;
import org.hl7.fhir.instance.model.Conformance;
import org.hl7.fhir.instance.model.DataElement;
import org.hl7.fhir.instance.model.ElementDefinition.TypeRefComponent;
import org.hl7.fhir.instance.model.OperationOutcome;
import org.hl7.fhir.instance.model.Parameters;
import org.hl7.fhir.instance.model.Questionnaire;
import org.hl7.fhir.instance.model.Resource;
import org.hl7.fhir.instance.model.SearchParameter;
import org.hl7.fhir.instance.model.StructureDefinition;
import org.hl7.fhir.instance.model.ValueSet;
import org.hl7.fhir.instance.model.ValueSet.ConceptDefinitionComponent;
import org.hl7.fhir.instance.model.ValueSet.ConceptSetComponent;
import org.hl7.fhir.instance.model.ValueSet.ValueSetExpansionComponent;
import org.hl7.fhir.instance.terminologies.ITerminologyServices;
import org.hl7.fhir.instance.terminologies.ValueSetExpander.ValueSetExpansionOutcome;

/*
 *  private static Map<String, StructureDefinition> loadProfiles() throws Exception {
 HashMap<String, StructureDefinition> result = new HashMap<String, StructureDefinition>();
 Bundle feed = new XmlParser().parseGeneral(new FileInputStream(PROFILES)).getFeed();
 for (AtomEntry<? extends Resource> e : feed.getEntryList()) {
 if (e.getReference() instanceof StructureDefinition) {
 result.put(e.getId(), (StructureDefinition) e.getReference());
 }
 }
 return result;
 }

 private static final String TEST_PROFILE = "C:\\work\\org.hl7.fhir\\build\\publish\\namespace.profile.xml";
 private static final String PROFILES = "C:\\work\\org.hl7.fhir\\build\\publish\\profiles-resources.xml";

 igtodo - things to add: 
 - version
 - list of resource names

 */
public class WorkerContext implements NameResolver {

  private ITerminologyServices terminologyServices = new NullTerminologyServices();
  private IFHIRClient client = new NullClient();
  private Map<String, ValueSet> codeSystems = new HashMap<String, ValueSet>();
  private Map<String, DataElement> dataElements = new HashMap<String, DataElement>();
  private Map<String, ValueSet> valueSets = new HashMap<String, ValueSet>();
  private Map<String, ConceptMap> maps = new HashMap<String, ConceptMap>();
  private Map<String, StructureDefinition> profiles = new HashMap<String, StructureDefinition>();
  private Map<String, SearchParameter> searchParameters = new HashMap<String, SearchParameter>();
  private Map<String, StructureDefinition> extensionDefinitions = new HashMap<String, StructureDefinition>();
  private String version;
  private List<String> resourceNames = new ArrayList<String>();
  private Map<String, Questionnaire> questionnaires = new HashMap<String, Questionnaire>();

  public WorkerContext() {
    super();
  }

  public WorkerContext(ITerminologyServices conceptLocator, IFHIRClient client, Map<String, ValueSet> codeSystems,
      Map<String, ValueSet> valueSets, Map<String, ConceptMap> maps, Map<String, StructureDefinition> profiles) {
    super();
    if (conceptLocator != null)
      this.terminologyServices = conceptLocator;
    if (client != null)
      this.client = client;
    if (codeSystems != null)
      this.codeSystems = codeSystems;
    if (valueSets != null)
      this.valueSets = valueSets;
    if (maps != null)
      this.maps = maps;
    if (profiles != null)
      this.profiles = profiles;
  }

  public ITerminologyServices getTerminologyServices() {
    return terminologyServices;
  }

  public boolean hasClient() {
    return !(client == null || client instanceof NullClient);
  }

  public IFHIRClient getClient() {
    return client;
  }

  public Map<String, ValueSet> getCodeSystems() {
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

  public StructureDefinition getProfile(String theId) {
    return profiles.get(theId);
  }

  /**
   * @deprecated Use {@link #getProfile(String)} instead
   */
  @Deprecated
  public Map<String, StructureDefinition> getProfiles() {
    return profiles;
  }

  public Map<String, StructureDefinition> getExtensionDefinitions() {
    return extensionDefinitions;
  }

  public Map<String, Questionnaire> getQuestionnaires() {
    return questionnaires;
  }

  public WorkerContext setTerminologyServices(ITerminologyServices terminologyServices) {
    this.terminologyServices = terminologyServices;
    return this;
  }

  public WorkerContext clone(IFHIRClient altClient) {
    WorkerContext res = new WorkerContext(terminologyServices, null, codeSystems, valueSets, maps, profiles);
    res.extensionDefinitions.putAll(extensionDefinitions);
    res.version = version;
    res.client = altClient;
    return res;
  }

  public void seeExtensionDefinition(String url, StructureDefinition ed) throws Exception {
    if (extensionDefinitions.get(ed.getUrl()) != null)
      throw new Exception("duplicate extension definition: " + ed.getUrl());
    extensionDefinitions.put(ed.getId(), ed);
    extensionDefinitions.put(url, ed);
    extensionDefinitions.put(ed.getUrl(), ed);
  }

  public void seeQuestionnaire(String url, Questionnaire theQuestionnaire) throws Exception {
    if (questionnaires.get(theQuestionnaire.getId()) != null)
      throw new Exception("duplicate extension definition: "+theQuestionnaire.getId());
    questionnaires.put(theQuestionnaire.getId(), theQuestionnaire);
    questionnaires.put(url, theQuestionnaire);
  }

  public void seeValueSet(String url, ValueSet vs) throws Exception {
    if (valueSets.containsKey(vs.getUrl()))
      throw new Exception("Duplicate Profile "+vs.getUrl());
    valueSets.put(vs.getId(), vs);
    valueSets.put(url, vs);
    valueSets.put(vs.getUrl(), vs);
	  if (vs.hasCodeSystem()) {
	    codeSystems.put(vs.getCodeSystem().getSystem().toString(), vs);
    }
  }

  public void seeProfile(String url, StructureDefinition p) {
    if (profiles.containsKey(p.getUrl()))
      throw new IllegalArgumentException("Duplicate Profile "+p.getUrl());
    profiles.put(p.getId(), p);
    profiles.put(url, p);
    profiles.put(p.getUrl(), p);
  }

  public class NullClient implements IFHIRClient {

    @Override
    public VersionInfo getVersions() {
      throw new Error("call to NullClient");
    }

    @Override
    public IFHIRClient initialize(String baseServiceUrl) throws URISyntaxException {
      throw new Error("call to NullClient");
    }

    @Override
    public void initialize(String baseServiceUrl, int recordCount) throws URISyntaxException {
      throw new Error("call to NullClient");
    }

    @Override
    public void setPreferredResourceFormat(ResourceFormat resourceFormat) {
      throw new Error("call to NullClient");
    }

    @Override
    public String getPreferredResourceFormat() {
      throw new Error("call to NullClient");
    }

    @Override
    public void setPreferredFeedFormat(FeedFormat feedFormat) {
      throw new Error("call to NullClient");
    }

    @Override
    public String getPreferredFeedFormat() {
      throw new Error("call to NullClient");
    }

    @Override
    public int getMaximumRecordCount() {
      throw new Error("call to NullClient");
    }

    @Override
    public void setMaximumRecordCount(int recordCount) {
      throw new Error("call to NullClient");
    }

    @Override
    public Conformance getConformanceStatement() {
      throw new Error("call to NullClient");
    }

    @Override
    public Conformance getConformanceStatement(boolean useOptionsVerb) {
      throw new Error("call to NullClient");
    }

    @Override
    public <T extends Resource> T read(Class<T> resource, String id) {
      throw new Error("call to NullClient");
    }

    @Override
    public <T extends Resource> T vread(Class<T> resource, String id, String versionid) {
      throw new Error("call to NullClient");
    }

    @Override
    public <T extends Resource> T update(Class<T> resourceClass, T resource, String id) {
      throw new Error("call to NullClient");
    }

    @Override
    public <T extends Resource> boolean delete(Class<T> resourceClass, String id) {
      throw new Error("call to NullClient");
    }

    @Override
    public <T extends Resource> OperationOutcome create(Class<T> resourceClass, T resource) {
      throw new Error("call to NullClient");
    }

    @Override
    public <T extends Resource> Bundle history(Calendar lastUpdate, Class<T> resourceClass, String id) {
      throw new Error("call to NullClient");
    }

    @Override
    public <T extends Resource> Bundle history(Date lastUpdate, Class<T> resourceClass, String id) {
      throw new Error("call to NullClient");
    }

    @Override
    public <T extends Resource> Bundle history(Class<T> resource, String id) {
      throw new Error("call to NullClient");
    }

    @Override
    public <T extends Resource> Bundle history(Calendar lastUpdate, Class<T> resourceClass) {
      throw new Error("call to NullClient");
    }

    @Override
    public <T extends Resource> Bundle history(Date lastUpdate, Class<T> resourceClass) {
      throw new Error("call to NullClient");
    }

    @Override
    public <T extends Resource> Bundle history(Class<T> resourceClass) {
      throw new Error("call to NullClient");
    }

    @Override
    public <T extends Resource> Bundle history(Calendar lastUpdate) {
      throw new Error("call to NullClient");
    }

    @Override
    public <T extends Resource> Bundle history(Date lastUpdate) {
      throw new Error("call to NullClient");
    }

    @Override
    public <T extends Resource> Bundle history() {
      throw new Error("call to NullClient");
    }

    @Override
    public <T extends Resource> OperationOutcome validate(Class<T> resourceClass, T resource, String id) {
      throw new Error("call to NullClient");
    }

    @Override
    public <T extends Resource> Bundle search(Class<T> resourceClass, Map<String, String> params) {
      throw new Error("call to NullClient");
    }

    @Override
    public <T extends Resource> Bundle searchPost(Class<T> resourceClass, T resource, Map<String, String> params) {
      throw new Error("call to NullClient");
    }

    @Override
    public Bundle transaction(Bundle batch) {
      throw new Error("call to NullClient");
    }

    @Override
    public Bundle fetchFeed(String url) {
      throw new Error("call to NullClient");
    }

    @Override
    public ValueSet expandValueset(ValueSet source) throws Exception {
      throw new Error("call to NullClient");
    }

    @Override
    public <T extends Resource> Parameters operateType(Class<T> resourceClass, String name, Parameters params) {
      throw new Error("call to NullClient");
    }

    @Override
    public Conformance getConformanceStatementQuick() {
      throw new Error("call to NullClient");
    }

    @Override
    public Conformance getConformanceStatementQuick(boolean useOptionsVerb) {
      throw new Error("call to NullClient");
    }

    @Override
    public String getAddress() {
      throw new Error("call to NullClient");
    }

  }

  public StructureDefinition getExtensionStructure(StructureDefinition context, String url) throws Exception {
    if (url.startsWith("#")) {
      throw new Error("Contained extensions not done yet");
    } else {
      if (url.contains("#"))
        url = url.substring(0, url.indexOf("#"));
      StructureDefinition res = extensionDefinitions.get(url);
      if (res == null)
        res = profiles.get(url);
      if (res == null)
        return null;
      if (res.getSnapshot() == null || res.getSnapshot().getElement().isEmpty())
        throw new Exception("no snapshot on extension for url " + url);
      return res;
    }
  }

  public class NullTerminologyServices implements ITerminologyServices {

    @Override
    public boolean supportsSystem(String system) {
      return false;
    }

    @Override
    public ConceptDefinitionComponent getCodeDefinition(String system, String code) {
      throw new Error("call to NullTerminologyServices");
    }

    @Override
    public ValidationResult validateCode(String system, String code, String display) {
      throw new Error("call to NullTerminologyServices");
    }

    @Override
    public ValueSetExpansionComponent expandVS(ConceptSetComponent inc) throws Exception {
      throw new Error("call to NullTerminologyServices");
    }

    @Override
    public boolean checkVS(ConceptSetComponent vsi, String system, String code) {
      throw new Error("call to NullTerminologyServices");
    }

    @Override
    public boolean verifiesSystem(String system) {
      return false;
    }

    @Override
    public ValueSetExpansionOutcome expand(ValueSet vs) {
      throw new Error("call to NullTerminologyServices");
    }

  }

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  @Override
  public boolean isResource(String name) {
    if (resourceNames.contains(name))
      return true;
    StructureDefinition sd = profiles.get("http://hl7.org/fhir/StructureDefinition/" + name);
    return sd != null && (sd.getBase().endsWith("Resource") || sd.getBase().endsWith("DomainResource"));
  }

  public List<String> getResourceNames() {
    return resourceNames;
  }

  public StructureDefinition getTypeStructure(TypeRefComponent type) {
    if (type.hasProfile())
      return profiles.get(type.getProfile().get(0).getValue());
    else
      return profiles.get(type.getCode());
  }

  public Map<String, SearchParameter> getSearchParameters() {
    return searchParameters;
  }

}
