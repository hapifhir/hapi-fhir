package org.hl7.fhir.dstu2.ctx;

import org.hl7.fhir.dstu2.model.*;
import org.hl7.fhir.dstu2.terminologies.ITerminologyServices;
import org.hl7.fhir.dstu2.terminologies.ValueSetExpander;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
	private Map<String, ValueSet> codeSystems = new HashMap<>();
	private Map<String, DataElement> dataElements = new HashMap<>();
	private Map<String, ValueSet> valueSets = new HashMap<>();
	private Map<String, ConceptMap> maps = new HashMap<>();
	private Map<String, StructureDefinition> profiles = new HashMap<>();
	private Map<String, SearchParameter> searchParameters = new HashMap<>();
	private Map<String, StructureDefinition> extensionDefinitions = new HashMap<>();
	private String version;
	private List<String> resourceNames = new ArrayList<>();
	private Map<String, Questionnaire> questionnaires = new HashMap<>();

	public WorkerContext() {
		super();
	}

	public WorkerContext(ITerminologyServices conceptLocator, Object client, Map<String, ValueSet> codeSystems,
								Map<String, ValueSet> valueSets, Map<String, ConceptMap> maps, Map<String, StructureDefinition> profiles) {
		super();
		if (conceptLocator != null)
			this.terminologyServices = conceptLocator;
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

	public WorkerContext setTerminologyServices(ITerminologyServices terminologyServices) {
		this.terminologyServices = terminologyServices;
		return this;
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

	public void seeExtensionDefinition(String url, StructureDefinition ed) throws Exception {
		if (extensionDefinitions.get(ed.getUrl()) != null)
			throw new Exception("duplicate extension definition: " + ed.getUrl());
		extensionDefinitions.put(ed.getId(), ed);
		extensionDefinitions.put(url, ed);
		extensionDefinitions.put(ed.getUrl(), ed);
	}

	public void seeQuestionnaire(String url, Questionnaire theQuestionnaire) throws Exception {
		if (questionnaires.get(theQuestionnaire.getId()) != null)
			throw new Exception("duplicate extension definition: " + theQuestionnaire.getId());
		questionnaires.put(theQuestionnaire.getId(), theQuestionnaire);
		questionnaires.put(url, theQuestionnaire);
	}

	public void seeValueSet(String url, ValueSet vs) throws Exception {
		if (valueSets.containsKey(vs.getUrl()))
			throw new Exception("Duplicate Profile " + vs.getUrl());
		valueSets.put(vs.getId(), vs);
		valueSets.put(url, vs);
		valueSets.put(vs.getUrl(), vs);
		if (vs.getCodeSystem().isEmpty() == false) {
			codeSystems.put(vs.getCodeSystem().getSystem(), vs);
		}
	}

	public void seeProfile(String url, StructureDefinition p) {
		if (profiles.containsKey(p.getUrl()))
			throw new IllegalArgumentException("Duplicate Profile " + p.getUrl());
		profiles.put(p.getId(), p);
		profiles.put(url, p);
		profiles.put(p.getUrl(), p);
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

	public StructureDefinition getTypeStructure(ElementDefinition.TypeRefComponent type) {
		if (type.hasProfile())
			return profiles.get(type.getProfile().get(0).getValue());
		else
			return profiles.get(type.getCode());
	}

	public Map<String, SearchParameter> getSearchParameters() {
		return searchParameters;
	}

	public class NullTerminologyServices implements ITerminologyServices {

		@Override
		public boolean supportsSystem(String system) {
			return false;
		}

		@Override
		public org.hl7.fhir.dstu2.model.ValueSet.ConceptDefinitionComponent getCodeDefinition(String system, String code) {
			throw new Error("call to NullTerminologyServices");
		}

		@Override
		public ValidationResult validateCode(String system, String code, String display) {
			throw new Error("call to NullTerminologyServices");
		}

		@Override
		public org.hl7.fhir.dstu2.model.ValueSet.ValueSetExpansionComponent expandVS(org.hl7.fhir.dstu2.model.ValueSet.ConceptSetComponent inc) throws Exception {
			throw new Error("call to NullTerminologyServices");
		}

		@Override
		public boolean checkVS(org.hl7.fhir.dstu2.model.ValueSet.ConceptSetComponent vsi, String system, String code) {
			throw new Error("call to NullTerminologyServices");
		}

		@Override
		public boolean verifiesSystem(String system) {
			return false;
		}

		@Override
		public ValueSetExpander.ValueSetExpansionOutcome expand(ValueSet vs) {
			throw new Error("call to NullTerminologyServices");
		}

	}

}
