package org.hl7.fhir.r4.context;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.IOUtils;
import org.fhir.ucum.UcumService;
import org.hl7.fhir.r4.conformance.ProfileUtilities;
import org.hl7.fhir.r4.conformance.ProfileUtilities.ProfileKnowledgeProvider;
import org.hl7.fhir.r4.context.IWorkerContext.ILoggingService.LogCategory;
import org.hl7.fhir.r4.formats.IParser;
import org.hl7.fhir.r4.formats.JsonParser;
import org.hl7.fhir.r4.formats.ParserType;
import org.hl7.fhir.r4.formats.XmlParser;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.ConceptMap;
import org.hl7.fhir.r4.model.ElementDefinition.ElementDefinitionBindingComponent;
import org.hl7.fhir.r4.model.MetadataResource;
import org.hl7.fhir.r4.model.NamingSystem;
import org.hl7.fhir.r4.model.NamingSystem.NamingSystemIdentifierType;
import org.hl7.fhir.r4.model.NamingSystem.NamingSystemUniqueIdComponent;
import org.hl7.fhir.r4.model.OperationDefinition;
import org.hl7.fhir.r4.model.OperationOutcome.IssueSeverity;
import org.hl7.fhir.r4.model.Questionnaire;
import org.hl7.fhir.r4.model.Resource;
import org.hl7.fhir.r4.model.ResourceType;
import org.hl7.fhir.r4.model.SearchParameter;
import org.hl7.fhir.r4.model.StructureDefinition;
import org.hl7.fhir.r4.model.StructureDefinition.StructureDefinitionKind;
import org.hl7.fhir.r4.model.StructureDefinition.TypeDerivationRule;
import org.hl7.fhir.r4.model.StructureMap;
import org.hl7.fhir.r4.model.StructureMap.StructureMapModelMode;
import org.hl7.fhir.r4.model.StructureMap.StructureMapStructureComponent;
import org.hl7.fhir.r4.model.ValueSet;
import org.hl7.fhir.r4.terminologies.ValueSetExpander.ValueSetExpansionOutcome;
import org.hl7.fhir.r4.terminologies.ValueSetExpansionCache;
import org.hl7.fhir.r4.utils.INarrativeGenerator;
import org.hl7.fhir.r4.utils.IResourceValidator;
import org.hl7.fhir.r4.utils.NarrativeGenerator;
import org.hl7.fhir.r4.utils.client.FHIRToolingClient;
import org.hl7.fhir.exceptions.DefinitionException;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.exceptions.FHIRFormatError;
import org.hl7.fhir.utilities.CSFileInputStream;
import org.hl7.fhir.utilities.OIDUtils;
import org.hl7.fhir.utilities.Utilities;
import org.hl7.fhir.utilities.cache.NpmPackage;
import org.hl7.fhir.utilities.validation.ValidationMessage;
import org.hl7.fhir.utilities.validation.ValidationMessage.IssueType;
import org.hl7.fhir.utilities.validation.ValidationMessage.Source;

import com.google.gson.JsonObject;

import ca.uhn.fhir.parser.DataFormatException;

/*
 * This is a stand alone implementation of worker context for use inside a tool.
 * It loads from the validation package (validation-min.xml.zip), and has a 
 * very light client to connect to an open unauthenticated terminology service
 */

public class SimpleWorkerContext extends BaseWorkerContext implements IWorkerContext, ProfileKnowledgeProvider {

  public interface IContextResourceLoader {
    Bundle loadBundle(InputStream stream, boolean isJson) throws FHIRException, IOException;
  }

  public interface IValidatorFactory {
    IResourceValidator makeValidator(IWorkerContext ctxts) throws FHIRException;
  }

	private Questionnaire questionnaire;
	private Map<String, byte[]> binaries = new HashMap<String, byte[]>();
  private String version;
  private String revision;
  private String date;
  private IValidatorFactory validatorFactory;
  private UcumService ucumService;
  private boolean ignoreProfileErrors;
  
  public SimpleWorkerContext() throws FileNotFoundException, IOException, FHIRException {
    super();
  }
  
  public SimpleWorkerContext(SimpleWorkerContext other) throws FileNotFoundException, IOException, FHIRException {
    super();
    copy(other);
  }
  
  protected void copy(SimpleWorkerContext other) {
    super.copy(other);
    questionnaire = other.questionnaire;
    binaries.putAll(other.binaries);
    version = other.version;
    revision = other.revision;
    date = other.date;
    validatorFactory = other.validatorFactory;
  }

  // -- Initializations
	/**
	 * Load the working context from the validation pack
	 * 
	 * @param path
	 *           filename of the validation pack
	 * @return
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 * @throws FHIRException 
	 * @throws Exception
	 */
  public static SimpleWorkerContext fromPack(String path) throws FileNotFoundException, IOException, FHIRException {
    SimpleWorkerContext res = new SimpleWorkerContext();
    res.loadFromPack(path, null);
    return res;
  }

  public static SimpleWorkerContext fromPackage(NpmPackage pi, boolean allowDuplicates) throws FileNotFoundException, IOException, FHIRException {
    SimpleWorkerContext res = new SimpleWorkerContext();
    res.setAllowLoadingDuplicates(allowDuplicates);
    res.loadFromPackage(pi, null);
    return res;
  }

  public static SimpleWorkerContext fromPackage(NpmPackage pi) throws FileNotFoundException, IOException, FHIRException {
    SimpleWorkerContext res = new SimpleWorkerContext();
    res.loadFromPackage(pi, null);
    return res;
  }

  public static SimpleWorkerContext fromPackage(NpmPackage pi, IContextResourceLoader loader) throws FileNotFoundException, IOException, FHIRException {
    SimpleWorkerContext res = new SimpleWorkerContext();
    res.setAllowLoadingDuplicates(true);
    res.loadFromPackage(pi, loader);
    return res;
  }

  public static SimpleWorkerContext fromPack(String path, boolean allowDuplicates) throws FileNotFoundException, IOException, FHIRException {
    SimpleWorkerContext res = new SimpleWorkerContext();
    res.setAllowLoadingDuplicates(allowDuplicates);
    res.loadFromPack(path, null);
    return res;
  }

  public static SimpleWorkerContext fromPack(String path, IContextResourceLoader loader) throws FileNotFoundException, IOException, FHIRException {
    SimpleWorkerContext res = new SimpleWorkerContext();
    res.loadFromPack(path, loader);
    return res;
  }

	public static SimpleWorkerContext fromClassPath() throws IOException, FHIRException {
		SimpleWorkerContext res = new SimpleWorkerContext();
		res.loadFromStream(SimpleWorkerContext.class.getResourceAsStream("validation.json.zip"), null);
		return res;
	}

	 public static SimpleWorkerContext fromClassPath(String name) throws IOException, FHIRException {
	   InputStream s = SimpleWorkerContext.class.getResourceAsStream("/"+name);
	    SimpleWorkerContext res = new SimpleWorkerContext();
	   res.loadFromStream(s, null);
	    return res;
	  }

	public static SimpleWorkerContext fromDefinitions(Map<String, byte[]> source) throws IOException, FHIRException {
		SimpleWorkerContext res = new SimpleWorkerContext();
		for (String name : source.keySet()) {
		  res.loadDefinitionItem(name, new ByteArrayInputStream(source.get(name)), null);
		}
		return res;
	}

  public static SimpleWorkerContext fromDefinitions(Map<String, byte[]> source, IContextResourceLoader loader) throws IOException, FHIRException {
    SimpleWorkerContext res = new SimpleWorkerContext();
    for (String name : source.keySet()) {
      res.loadDefinitionItem(name, new ByteArrayInputStream(source.get(name)), loader);
    }
    return res;
  }
	private void loadDefinitionItem(String name, InputStream stream, IContextResourceLoader loader) throws IOException, FHIRException {
    if (name.endsWith(".xml"))
      loadFromFile(stream, name, loader);
    else if (name.endsWith(".json"))
      loadFromFileJson(stream, name, loader);
    else if (name.equals("version.info"))
      readVersionInfo(stream);
    else
      loadBytes(name, stream);
  }

  public String connectToTSServer(String url) throws URISyntaxException {
    tlog("Connect to "+url);
    txServer = new FHIRToolingClient(url);
    txServer.setTimeout(30000);
    return txServer.getCapabilitiesStatementQuick().getSoftware().getVersion();
  }

  public String connectToTSServer(FHIRToolingClient client) throws URISyntaxException {
    tlog("Connect to "+client.getAddress());
    txServer = client;
    return txServer.getCapabilitiesStatementQuick().getSoftware().getVersion();
  }

	public void loadFromFile(InputStream stream, String name, IContextResourceLoader loader) throws IOException, FHIRException {
		Resource f;
		try {
		  if (loader != null)
		    f = loader.loadBundle(stream, false);
		  else {
		    XmlParser xml = new XmlParser();
		    f = xml.parse(stream);
		  }
    } catch (DataFormatException e1) {
      throw new org.hl7.fhir.exceptions.FHIRFormatError("Error parsing "+name+":" +e1.getMessage(), e1);
    } catch (Exception e1) {
			throw new org.hl7.fhir.exceptions.FHIRFormatError("Error parsing "+name+":" +e1.getMessage(), e1);
		}
		if (f instanceof Bundle) {
		  Bundle bnd = (Bundle) f;
		  for (BundleEntryComponent e : bnd.getEntry()) {
		    if (e.getFullUrl() == null) {
		      logger.logDebugMessage(LogCategory.CONTEXT, "unidentified resource in " + name+" (no fullUrl)");
		    }
		    cacheResource(e.getResource());
		  }
		} else if (f instanceof MetadataResource) {
		  MetadataResource m = (MetadataResource) f;
		  cacheResource(m);
		}
	}

  private void loadFromFileJson(InputStream stream, String name, IContextResourceLoader loader) throws IOException, FHIRException {
    Bundle f = null;
    try {
      if (loader != null)
        f = loader.loadBundle(stream, true);
      else {
        JsonParser json = new JsonParser();
        Resource r = json.parse(stream);
        if (r instanceof Bundle)
          f = (Bundle) r;
        else
          cacheResource(r);
      }
    } catch (FHIRFormatError e1) {
      throw new org.hl7.fhir.exceptions.FHIRFormatError(e1.getMessage(), e1);
    }
    if (f != null)
      for (BundleEntryComponent e : f.getEntry()) {
        cacheResource(e.getResource());
    }
  }

	private void loadFromPack(String path, IContextResourceLoader loader) throws FileNotFoundException, IOException, FHIRException {
		loadFromStream(new CSFileInputStream(path), loader);
	}
  
	public void loadFromPackage(NpmPackage pi, IContextResourceLoader loader, String... types) throws FileNotFoundException, IOException, FHIRException {
	  if (types.length == 0)
	    types = new String[] { "StructureDefinition", "ValueSet", "CodeSystem", "SearchParameter", "OperationDefinition", "Questionnaire","ConceptMap","StructureMap", "NamingSystem"};
	  for (String s : pi.list("package")) {
	    if (s.contains("-") && s.endsWith(".json")) {
	      String t = s.substring(0, s.indexOf("-"));
	      if (Utilities.existsInList(t, types)) {
	        loadDefinitionItem(s, pi.load("package", s), loader);
	      }
	    }
	  }
	}

  public void loadFromFile(String file, IContextResourceLoader loader) throws IOException, FHIRException {
    loadDefinitionItem(file, new CSFileInputStream(file), loader);
  }
  
	private void loadFromStream(InputStream stream, IContextResourceLoader loader) throws IOException, FHIRException {
		ZipInputStream zip = new ZipInputStream(stream);
		ZipEntry ze;
		while ((ze = zip.getNextEntry()) != null) {
      loadDefinitionItem(ze.getName(), zip, loader);
			zip.closeEntry();
		}
		zip.close();
	}

  private void readVersionInfo(InputStream stream) throws IOException, DefinitionException {
    byte[] bytes = IOUtils.toByteArray(stream);
    binaries.put("version.info", bytes);

    String[] vi = new String(bytes).split("\\r?\\n");
    for (String s : vi) {
      if (s.startsWith("version=")) {
        if (version == null)
        version = s.substring(8);
        else if (!version.equals(s.substring(8))) 
          throw new DefinitionException("Version mismatch. The context has version "+version+" loaded, and the new content being loaded is version "+s.substring(8));
      }
      if (s.startsWith("revision="))
        revision = s.substring(9);
      if (s.startsWith("date="))
        date = s.substring(5);
    }
  }

	private void loadBytes(String name, InputStream stream) throws IOException {
    byte[] bytes = IOUtils.toByteArray(stream);
	  binaries.put(name, bytes);
  }

	@Override
	public IParser getParser(ParserType type) {
		switch (type) {
		case JSON: return newJsonParser();
		case XML: return newXmlParser();
		default:
			throw new Error("Parser Type "+type.toString()+" not supported");
		}
	}

	@Override
	public IParser getParser(String type) {
		if (type.equalsIgnoreCase("JSON"))
			return new JsonParser();
		if (type.equalsIgnoreCase("XML"))
			return new XmlParser();
		throw new Error("Parser Type "+type.toString()+" not supported");
	}

	@Override
	public IParser newJsonParser() {
		return new JsonParser();
	}
	@Override
	public IParser newXmlParser() {
		return new XmlParser();
	}

	@Override
	public INarrativeGenerator getNarrativeGenerator(String prefix, String basePath) {
		return new NarrativeGenerator(prefix, basePath, this);
	}

	@Override
	public IResourceValidator newValidator() throws FHIRException {
	  if (validatorFactory == null)
	    throw new Error("No validator configured");
	  return validatorFactory.makeValidator(this);
	}




  @Override
  public List<String> getResourceNames() {
    List<String> result = new ArrayList<String>();
    for (StructureDefinition sd : listStructures()) {
      if (sd.getKind() == StructureDefinitionKind.RESOURCE && sd.getDerivation() == TypeDerivationRule.SPECIALIZATION)
        result.add(sd.getName());
    }
    Collections.sort(result);
    return result;
  }

  @Override
  public List<String> getTypeNames() {
    List<String> result = new ArrayList<String>();
    for (StructureDefinition sd : listStructures()) {
      if (sd.getKind() != StructureDefinitionKind.LOGICAL && sd.getDerivation() == TypeDerivationRule.SPECIALIZATION)
        result.add(sd.getName());
    }
    Collections.sort(result);
    return result;
  }

  @Override
  public String getAbbreviation(String name) {
    return "xxx";
  }

  @Override
  public boolean isDatatype(String typeSimple) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean isResource(String t) {
    StructureDefinition sd;
    try {
      sd = fetchResource(StructureDefinition.class, "http://hl7.org/fhir/StructureDefinition/"+t);
    } catch (Exception e) {
      return false;
    }
    if (sd == null)
      return false;
    if (sd.getDerivation() == TypeDerivationRule.CONSTRAINT)
      return false;
    return sd.getKind() == StructureDefinitionKind.RESOURCE;
  }

  @Override
  public boolean hasLinkFor(String typeSimple) {
    return false;
  }

  @Override
  public String getLinkFor(String corePath, String typeSimple) {
    return null;
  }

  @Override
  public BindingResolution resolveBinding(StructureDefinition profile, ElementDefinitionBindingComponent binding, String path) {
    return null;
  }

  @Override
  public String getLinkForProfile(StructureDefinition profile, String url) {
    return null;
  }

  public Questionnaire getQuestionnaire() {
    return questionnaire;
  }

  public void setQuestionnaire(Questionnaire questionnaire) {
    this.questionnaire = questionnaire;
  }

  @Override
  public Set<String> typeTails() {
    return new HashSet<String>(Arrays.asList("Integer","UnsignedInt","PositiveInt","Decimal","DateTime","Date","Time","Instant","String","Uri","Url","Canonical","Oid","Uuid","Id","Boolean","Code","Markdown","Base64Binary","Coding","CodeableConcept","Attachment","Identifier","Quantity","SampledData","Range","Period","Ratio","HumanName","Address","ContactPoint","Timing","Reference","Annotation","Signature","Meta"));
  }

  @Override
  public List<StructureDefinition> allStructures() {
    List<StructureDefinition> result = new ArrayList<StructureDefinition>();
    Set<StructureDefinition> set = new HashSet<StructureDefinition>();
    for (StructureDefinition sd : listStructures()) {
      if (!set.contains(sd)) {
        result.add(sd);
        set.add(sd);
      }
    }
    return result;
  }

  public void loadBinariesFromFolder(String folder) throws FileNotFoundException, Exception {
    for (String n : new File(folder).list()) {
      loadBytes(n, new FileInputStream(Utilities.path(folder, n)));
    }
  }
  
  public void loadBinariesFromFolder(NpmPackage pi) throws FileNotFoundException, Exception {
    for (String n : pi.list("other")) {
      loadBytes(n, pi.load("other", n));
    }
  }
  
  public void loadFromFolder(String folder) throws FileNotFoundException, Exception {
    for (String n : new File(folder).list()) {
      if (n.endsWith(".json")) 
        loadFromFile(Utilities.path(folder, n), new JsonParser());
      else if (n.endsWith(".xml")) 
        loadFromFile(Utilities.path(folder, n), new XmlParser());
    }
  }
  
  private void loadFromFile(String filename, IParser p) throws FileNotFoundException, Exception {
  	Resource r; 
  	try {
  		r = p.parse(new FileInputStream(filename));
      if (r.getResourceType() == ResourceType.Bundle) {
        for (BundleEntryComponent e : ((Bundle) r).getEntry()) {
          cacheResource(e.getResource());
        }
     } else {
       cacheResource(r);
     }
  	} catch (Exception e) {
    	return;
    }
  }

  public Map<String, byte[]> getBinaries() {
    return binaries;
  }

  @Override
  public boolean prependLinks() {
    return false;
  }

  @Override
  public boolean hasCache() {
    return false;
  }

  @Override
  public String getVersion() {
    return version+"-"+revision;
  }

  
  public List<StructureMap> findTransformsforSource(String url) {
    List<StructureMap> res = new ArrayList<StructureMap>();
    for (StructureMap map : listTransforms()) {
      boolean match = false;
      boolean ok = true;
      for (StructureMapStructureComponent t : map.getStructure()) {
        if (t.getMode() == StructureMapModelMode.SOURCE) {
          match = match || t.getUrl().equals(url);
          ok = ok && t.getUrl().equals(url);
        }
      }
      if (match && ok)
        res.add(map);
    }
    return res;
  }

  public IValidatorFactory getValidatorFactory() {
    return validatorFactory;
  }

  public void setValidatorFactory(IValidatorFactory validatorFactory) {
    this.validatorFactory = validatorFactory;
  }

  @Override
  protected void seeMetadataResource(MetadataResource r, Map map, boolean addId) throws FHIRException {
    if (r instanceof StructureDefinition) {
      StructureDefinition p = (StructureDefinition)r;
      
      if (!p.hasSnapshot() && p.getKind() != StructureDefinitionKind.LOGICAL) {
        if (!p.hasBaseDefinition())
          throw new DefinitionException("Profile "+p.getName()+" ("+p.getUrl()+") has no base and no snapshot");
        StructureDefinition sd = fetchResource(StructureDefinition.class, p.getBaseDefinition());
        if (sd == null)
          throw new DefinitionException("Profile "+p.getName()+" ("+p.getUrl()+") base "+p.getBaseDefinition()+" could not be resolved");
        List<ValidationMessage> msgs = new ArrayList<ValidationMessage>();
        List<String> errors = new ArrayList<String>();
        ProfileUtilities pu = new ProfileUtilities(this, msgs, this);
        pu.setThrowException(false);
        pu.sortDifferential(sd, p, p.getUrl(), errors);
        for (String err : errors)
          msgs.add(new ValidationMessage(Source.ProfileValidator, IssueType.EXCEPTION, p.getUserString("path"), "Error sorting Differential: "+err, ValidationMessage.IssueSeverity.ERROR));
        pu.generateSnapshot(sd, p, p.getUrl(), p.getName());
        for (ValidationMessage msg : msgs) {
          if ((!ignoreProfileErrors && msg.getLevel() == ValidationMessage.IssueSeverity.ERROR) || msg.getLevel() == ValidationMessage.IssueSeverity.FATAL)
            throw new DefinitionException("Profile "+p.getName()+" ("+p.getUrl()+"). Error generating snapshot: "+msg.getMessage());
        }
        if (!p.hasSnapshot())
          throw new FHIRException("Profile "+p.getName()+" ("+p.getUrl()+"). Error generating snapshot");
        pu = null;
      }
    }
    super.seeMetadataResource(r, map, addId);
  }

  public UcumService getUcumService() {
    return ucumService;
  }

  public void setUcumService(UcumService ucumService) {
    this.ucumService = ucumService;
  }

  public boolean isIgnoreProfileErrors() {
    return ignoreProfileErrors;
  }

  public void setIgnoreProfileErrors(boolean ignoreProfileErrors) {
    this.ignoreProfileErrors = ignoreProfileErrors;
  }




}
