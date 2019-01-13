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
import org.hl7.fhir.dstu3.conformance.ProfileUtilities;
import org.hl7.fhir.dstu3.conformance.ProfileUtilities.ProfileKnowledgeProvider;
import org.hl7.fhir.dstu3.context.IWorkerContext.ILoggingService.LogCategory;
import org.hl7.fhir.dstu3.formats.IParser;
import org.hl7.fhir.dstu3.formats.JsonParser;
import org.hl7.fhir.dstu3.formats.ParserType;
import org.hl7.fhir.dstu3.formats.XmlParser;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.dstu3.model.CodeSystem;
import org.hl7.fhir.dstu3.model.ConceptMap;
import org.hl7.fhir.dstu3.model.ElementDefinition.ElementDefinitionBindingComponent;
import org.hl7.fhir.dstu3.model.MetadataResource;
import org.hl7.fhir.dstu3.model.NamingSystem;
import org.hl7.fhir.dstu3.model.NamingSystem.NamingSystemIdentifierType;
import org.hl7.fhir.dstu3.model.NamingSystem.NamingSystemUniqueIdComponent;
import org.hl7.fhir.dstu3.model.OperationDefinition;
import org.hl7.fhir.dstu3.model.Questionnaire;
import org.hl7.fhir.dstu3.model.Resource;
import org.hl7.fhir.dstu3.model.ResourceType;
import org.hl7.fhir.dstu3.model.SearchParameter;
import org.hl7.fhir.dstu3.model.StructureDefinition;
import org.hl7.fhir.dstu3.model.StructureDefinition.StructureDefinitionKind;
import org.hl7.fhir.dstu3.model.StructureDefinition.TypeDerivationRule;
import org.hl7.fhir.dstu3.model.StructureMap;
import org.hl7.fhir.dstu3.model.StructureMap.StructureMapModelMode;
import org.hl7.fhir.dstu3.model.StructureMap.StructureMapStructureComponent;
import org.hl7.fhir.dstu3.model.ValueSet;
import org.hl7.fhir.dstu3.terminologies.ValueSetExpansionCache;
import org.hl7.fhir.dstu3.utils.INarrativeGenerator;
import org.hl7.fhir.dstu3.utils.IResourceValidator;
import org.hl7.fhir.dstu3.utils.NarrativeGenerator;
import org.hl7.fhir.dstu3.utils.client.FHIRToolingClient;
import org.hl7.fhir.exceptions.DefinitionException;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.exceptions.FHIRFormatError;
import org.hl7.fhir.utilities.CSFileInputStream;
import org.hl7.fhir.utilities.OIDUtils;
import org.hl7.fhir.utilities.Utilities;
import org.hl7.fhir.utilities.validation.ValidationMessage;
import org.hl7.fhir.utilities.validation.ValidationMessage.IssueType;
import org.hl7.fhir.utilities.validation.ValidationMessage.Source;

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

  // all maps are to the full URI
	private Map<String, StructureDefinition> structures = new HashMap<String, StructureDefinition>();
	private List<NamingSystem> systems = new ArrayList<NamingSystem>();
	private Questionnaire questionnaire;
	private Map<String, byte[]> binaries = new HashMap<String, byte[]>();
  private String version;
  private String revision;
  private String date;
  private IValidatorFactory validatorFactory;
  
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

  public static SimpleWorkerContext fromPack(String path, boolean allowDuplicates) throws FileNotFoundException, IOException, FHIRException {
    SimpleWorkerContext res = new SimpleWorkerContext();
    res.allowLoadingDuplicates = allowDuplicates;
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
	  txServer = new FHIRToolingClient(url);
	  txServer.setTimeout(30000);
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
		    seeResource(e.getFullUrl(), e.getResource());
		  }
		} else if (f instanceof MetadataResource) {
		  MetadataResource m = (MetadataResource) f;
		  seeResource(m.getUrl(), m);
		}
	}

  private void loadFromFileJson(InputStream stream, String name, IContextResourceLoader loader) throws IOException, FHIRException {
    Bundle f;
    try {
      if (loader != null)
        f = loader.loadBundle(stream, true);
      else {
        JsonParser json = new JsonParser();
        f = (Bundle) json.parse(stream);
      }
    } catch (FHIRFormatError e1) {
      throw new org.hl7.fhir.exceptions.FHIRFormatError(e1.getMessage(), e1);
    }
    for (BundleEntryComponent e : f.getEntry()) {

      if (e.getFullUrl() == null) {
        logger.logDebugMessage(LogCategory.CONTEXT, "unidentified resource in " + name+" (no fullUrl)");
      }
      seeResource(e.getFullUrl(), e.getResource());
    }
  }

	public void seeResource(String url, Resource r) throws FHIRException {
    if (r instanceof StructureDefinition)
      seeProfile(url, (StructureDefinition) r);
    else if (r instanceof ValueSet)
      seeValueSet(url, (ValueSet) r);
    else if (r instanceof CodeSystem)
      seeCodeSystem(url, (CodeSystem) r);
    else if (r instanceof OperationDefinition)
      seeOperationDefinition(url, (OperationDefinition) r);
    else if (r instanceof ConceptMap)
      maps.put(((ConceptMap) r).getUrl(), (ConceptMap) r);
    else if (r instanceof StructureMap)
      transforms.put(((StructureMap) r).getUrl(), (StructureMap) r);
    else if (r instanceof NamingSystem)
    	systems.add((NamingSystem) r);
	}
	
	private void seeOperationDefinition(String url, OperationDefinition r) {
    operations.put(r.getUrl(), r);
  }

  public void seeValueSet(String url, ValueSet vs) throws DefinitionException {
	  if (Utilities.noString(url))
	    url = vs.getUrl();
		if (valueSets.containsKey(vs.getUrl()) && !allowLoadingDuplicates)
			throw new DefinitionException("Duplicate Profile " + vs.getUrl());
		valueSets.put(vs.getId(), vs);
		valueSets.put(vs.getUrl(), vs);
		if (!vs.getUrl().equals(url))
			valueSets.put(url, vs);
	}

	public void seeProfile(String url, StructureDefinition p) throws FHIRException {
    if (Utilities.noString(url))
      url = p.getUrl();
    
    if (!p.hasSnapshot() && p.getKind() != StructureDefinitionKind.LOGICAL) {
      if (!p.hasBaseDefinition())
        throw new DefinitionException("Profile "+p.getName()+" ("+p.getUrl()+") has no base and no snapshot");
      StructureDefinition sd = fetchResource(StructureDefinition.class, p.getBaseDefinition());
      if (sd == null)
        throw new DefinitionException("Profile "+p.getName()+" ("+p.getUrl()+") base "+p.getBaseDefinition()+" could not be resolved");
      List<ValidationMessage> msgs = new ArrayList<ValidationMessage>();
      List<String> errors = new ArrayList<String>();
      ProfileUtilities pu = new ProfileUtilities(this, msgs, this);
      pu.sortDifferential(sd, p, url, errors);
      for (String err : errors)
        msgs.add(new ValidationMessage(Source.ProfileValidator, IssueType.EXCEPTION,p.getUserString("path"), "Error sorting Differential: "+err, ValidationMessage.IssueSeverity.ERROR));
      pu.generateSnapshot(sd, p, p.getUrl(), p.getName());
      for (ValidationMessage msg : msgs) {
        if (msg.getLevel() == ValidationMessage.IssueSeverity.ERROR || msg.getLevel() == ValidationMessage.IssueSeverity.FATAL)
          throw new DefinitionException("Profile "+p.getName()+" ("+p.getUrl()+"). Error generating snapshot: "+msg.getMessage());
      }
      if (!p.hasSnapshot())
        throw new DefinitionException("Profile "+p.getName()+" ("+p.getUrl()+"). Error generating snapshot");
      pu = null;
    }
		if (structures.containsKey(p.getUrl()) && !allowLoadingDuplicates)
			throw new DefinitionException("Duplicate structures " + p.getUrl());
		structures.put(p.getId(), p);
		structures.put(p.getUrl(), p);
		if (!p.getUrl().equals(url))
			structures.put(url, p);
	}

	private void loadFromPack(String path, IContextResourceLoader loader) throws FileNotFoundException, IOException, FHIRException {
		loadFromStream(new CSFileInputStream(path), loader);
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
	public <T extends Resource> boolean hasResource(Class<T> class_, String uri) {
		try {
			return fetchResource(class_, uri) != null;
		} catch (Exception e) {
			return false;
		}
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
  public <T extends Resource> T fetchResource(Class<T> class_, String uri) {
    try {
      return fetchResourceWithException(class_, uri);
    } catch (FHIRException e) {
      throw new Error(e);
    }
  }
	@SuppressWarnings("unchecked")
	@Override
	public <T extends Resource> T fetchResourceWithException(Class<T> class_, String uri) throws FHIRException {
    if (class_ == null) {
      return null;      
    }

	  if (class_ == Questionnaire.class)
	    return (T) questionnaire;
	  
		if (class_ == StructureDefinition.class && !uri.contains("/"))
			uri = "http://hl7.org/fhir/StructureDefinition/"+uri;

		if (uri.startsWith("http:") || uri.startsWith("urn:") ) {
			if (uri.contains("#"))
				uri = uri.substring(0, uri.indexOf("#"));
			if (class_ == Resource.class) {
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
        return null;      
			}
			if (class_ == StructureDefinition.class) {
				if (structures.containsKey(uri))
					return (T) structures.get(uri);
				else
					return null;
			} else if (class_ == ValueSet.class) {
				if (valueSets.containsKey(uri))
					return (T) valueSets.get(uri);
				else
					return null;      
			} else if (class_ == CodeSystem.class) {
				if (codeSystems.containsKey(uri))
					return (T) codeSystems.get(uri);
				else
					return null;      
      } else if (class_ == OperationDefinition.class) {
        OperationDefinition od = operations.get(uri);
        return (T) od;
      } else if (class_ == SearchParameter.class) {
        SearchParameter od = searchParameters.get(uri);
        return (T) od;
			} else if (class_ == ConceptMap.class) {
				if (maps.containsKey(uri))
					return (T) maps.get(uri);
				else
					return null;      
      } else if (class_ == StructureMap.class) {
        if (transforms.containsKey(uri))
          return (T) transforms.get(uri);
        else
          return null;      
			}
		}
		
		throw new FHIRException("fetching "+class_.getName()+" not done yet for URI '"+uri+"'");
	}



	public int totalCount() {
		return valueSets.size() +  maps.size() + structures.size() + transforms.size();
	}

	public void setCache(ValueSetExpansionCache cache) {
	  this.expansionCache = cache;	
	}

  @Override
  public List<String> getResourceNames() {
    List<String> result = new ArrayList<String>();
    for (StructureDefinition sd : structures.values()) {
      if (sd.getKind() == StructureDefinitionKind.RESOURCE && sd.getDerivation() == TypeDerivationRule.SPECIALIZATION)
        result.add(sd.getName());
    }
    Collections.sort(result);
    return result;
  }

  @Override
  public List<String> getTypeNames() {
    List<String> result = new ArrayList<String>();
    for (StructureDefinition sd : structures.values()) {
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
    return new HashSet<String>(Arrays.asList("Integer","UnsignedInt","PositiveInt","Decimal","DateTime","Date","Time","Instant","String","Uri","Oid","Uuid","Id","Boolean","Code","Markdown","Base64Binary","Coding","CodeableConcept","Attachment","Identifier","Quantity","SampledData","Range","Period","Ratio","HumanName","Address","ContactPoint","Timing","Reference","Annotation","Signature","Meta"));
  }

  @Override
  public List<StructureDefinition> allStructures() {
    List<StructureDefinition> result = new ArrayList<StructureDefinition>();
    Set<StructureDefinition> set = new HashSet<StructureDefinition>();
    for (StructureDefinition sd : structures.values()) {
      if (!set.contains(sd)) {
        result.add(sd);
        set.add(sd);
      }
    }
    return result;
  }

	@Override
  public List<MetadataResource> allConformanceResources() {
    List<MetadataResource> result = new ArrayList<MetadataResource>();
    result.addAll(structures.values());
    result.addAll(codeSystems.values());
    result.addAll(valueSets.values());
    result.addAll(maps.values());
    result.addAll(transforms.values());
    return result;
  }

	@Override
	public String oid2Uri(String oid) {
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
          seeResource(null, e.getResource());
        }
     } else {
       seeResource(null, r);
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

  public Map<String, StructureMap> getTransforms() {
    return transforms;
  }
  
  public List<StructureMap> findTransformsforSource(String url) {
    List<StructureMap> res = new ArrayList<StructureMap>();
    for (StructureMap map : transforms.values()) {
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

 
  
}
