package org.hl7.fhir.dstu2016may.utils;

/*-
 * #%L
 * org.hl7.fhir.dstu2016may
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

import org.hl7.fhir.dstu2016may.formats.IParser;
import org.hl7.fhir.dstu2016may.formats.JsonParser;
import org.hl7.fhir.dstu2016may.formats.ParserType;
import org.hl7.fhir.dstu2016may.formats.XmlParser;
import org.hl7.fhir.dstu2016may.model.Bundle;
import org.hl7.fhir.dstu2016may.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.dstu2016may.model.CodeSystem;
import org.hl7.fhir.dstu2016may.model.ConceptMap;
import org.hl7.fhir.dstu2016may.model.ElementDefinition.ElementDefinitionBindingComponent;
import org.hl7.fhir.dstu2016may.model.NamingSystem;
import org.hl7.fhir.dstu2016may.model.NamingSystem.NamingSystemIdentifierType;
import org.hl7.fhir.dstu2016may.model.NamingSystem.NamingSystemUniqueIdComponent;
import org.hl7.fhir.dstu2016may.model.Questionnaire;
import org.hl7.fhir.dstu2016may.model.Resource;
import org.hl7.fhir.dstu2016may.model.ResourceType;
import org.hl7.fhir.dstu2016may.model.StructureDefinition;
import org.hl7.fhir.dstu2016may.model.StructureDefinition.StructureDefinitionKind;
import org.hl7.fhir.dstu2016may.model.ValueSet;
import org.hl7.fhir.dstu2016may.terminologies.ValueSetExpansionCache;
import org.hl7.fhir.dstu2016may.utils.ProfileUtilities.ProfileKnowledgeProvider;
import org.hl7.fhir.dstu2016may.utils.client.FHIRToolingClient;
import org.hl7.fhir.exceptions.DefinitionException;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.exceptions.FHIRFormatError;
import org.hl7.fhir.utilities.CSFileInputStream;
import org.hl7.fhir.utilities.OIDUtils;
import org.hl7.fhir.utilities.Utilities;
import org.hl7.fhir.utilities.validation.ValidationMessage;
import org.hl7.fhir.utilities.validation.ValidationMessage.IssueSeverity;

/*
 * This is a stand alone implementation of worker context for use inside a tool.
 * It loads from the validation package (validation-min.xml.zip), and has a 
 * very light cient to connect to an open unauthenticated terminology service
 */

public class SimpleWorkerContext extends BaseWorkerContext implements IWorkerContext, ProfileKnowledgeProvider {

	// all maps are to the full URI
	private Map<String, StructureDefinition> structures = new HashMap<String, StructureDefinition>();
	private List<NamingSystem> systems = new ArrayList<NamingSystem>();
	private Questionnaire questionnaire;

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
		res.loadFromPack(path);
		return res;
	}

	public static SimpleWorkerContext fromClassPath() throws IOException, FHIRException {
		SimpleWorkerContext res = new SimpleWorkerContext();
		res.loadFromStream(SimpleWorkerContext.class.getResourceAsStream("validation.zip"));
		return res;
	}

	public static SimpleWorkerContext fromDefinitions(Map<String, byte[]> source) throws IOException, FHIRException {
		SimpleWorkerContext res = new SimpleWorkerContext();
		for (String name : source.keySet()) {
			if (name.endsWith(".xml")) {
				res.loadFromFile(new ByteArrayInputStream(source.get(name)), name);
			}
		}
		return res;
	}

	public void connectToTSServer(String url) throws URISyntaxException {
	  txServer = new FHIRToolingClient(url);
	}

	private void loadFromFile(InputStream stream, String name) throws IOException, FHIRException {
		XmlParser xml = new XmlParser();
		Bundle f;
		try {
			f = (Bundle) xml.parse(stream);
		} catch (FHIRFormatError e1) {
			throw new org.hl7.fhir.exceptions.FHIRFormatError(e1.getMessage(), e1);
		}
		for (BundleEntryComponent e : f.getEntry()) {

			if (e.getFullUrl() == null) {
				System.out.println("unidentified resource in " + name+" (no fullUrl)");
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
    else if (r instanceof ConceptMap)
      maps.put(((ConceptMap) r).getUrl(), (ConceptMap) r);
    else if (r instanceof NamingSystem)
    	systems.add((NamingSystem) r);
	}
	
	private void seeValueSet(String url, ValueSet vs) throws DefinitionException {
	  if (Utilities.noString(url))
	    url = vs.getUrl();
		if (valueSets.containsKey(vs.getUrl()))
			throw new DefinitionException("Duplicate Profile " + vs.getUrl());
		valueSets.put(vs.getId(), vs);
		valueSets.put(vs.getUrl(), vs);
		if (!vs.getUrl().equals(url))
			valueSets.put(url, vs);
	}

	private void seeCodeSystem(String url, CodeSystem cs) throws DefinitionException {
		codeSystems.put(cs.getUrl(), cs);
	}

	private void seeProfile(String url, StructureDefinition p) throws FHIRException {
    if (Utilities.noString(url))
      url = p.getUrl();
    if (!p.hasSnapshot()) {
      if (!p.hasBaseDefinition())
        throw new DefinitionException("Profile "+p.getName()+" ("+p.getUrl()+") has no base and no snapshot");
      StructureDefinition sd = fetchResource(StructureDefinition.class, p.getBaseDefinition());
      if (sd == null)
        throw new DefinitionException("Profile "+p.getName()+" ("+p.getUrl()+") base "+p.getBaseDefinition()+" could not be resolved");
      List<ValidationMessage> msgs = new ArrayList<ValidationMessage>();
      ProfileUtilities pu = new ProfileUtilities(this, msgs, this);
      pu.generateSnapshot(sd, p, p.getUrl(), p.getName());
      for (ValidationMessage msg : msgs) {
        if (msg.getLevel() == IssueSeverity.ERROR || msg.getLevel() == IssueSeverity.FATAL)
          throw new DefinitionException("Profile "+p.getName()+" ("+p.getUrl()+"). Error generating snapshot: "+msg.getMessage());
      }
      if (!p.hasSnapshot())
        throw new DefinitionException("Profile "+p.getName()+" ("+p.getUrl()+"). Error generating snapshot");
      pu = null;
    }
		if (structures.containsKey(p.getUrl()))
			throw new DefinitionException("Duplicate structures " + p.getUrl());
		structures.put(p.getId(), p);
		structures.put(p.getUrl(), p);
		if (!p.getUrl().equals(url))
			structures.put(url, p);
	}

	private void loadFromPack(String path) throws FileNotFoundException, IOException, FHIRException {
		loadFromStream(new CSFileInputStream(path));
	}

	private void loadFromStream(InputStream stream) throws IOException, FHIRException {
		ZipInputStream zip = new ZipInputStream(stream);
		ZipEntry ze;
		while ((ze = zip.getNextEntry()) != null) {
			if (ze.getName().endsWith(".xml")) {
				String name = ze.getName();
				loadFromFile(zip, name);
			}
			zip.closeEntry();
		}
		zip.close();
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


	@SuppressWarnings("unchecked")
	@Override
	public <T extends Resource> T fetchResource(Class<T> class_, String uri) {
	  if (class_ == Questionnaire.class)
	    return (T) questionnaire;
	  
		if (class_ == StructureDefinition.class && !uri.contains("/"))
			uri = "http://hl7.org/fhir/StructureDefinition/"+uri;

		if (uri.startsWith("http:")) {
			if (uri.contains("#"))
				uri = uri.substring(0, uri.indexOf("#"));
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
			} else if (class_ == ConceptMap.class) {
				if (maps.containsKey(uri))
					return (T) maps.get(uri);
				else
					return null;      
			}
		}
		if (class_ == null && uri.contains("/")) {
			return null;      
		}

		throw new Error("not done yet");
	}



	public int totalCount() {
		return valueSets.size() +  maps.size() + structures.size();
	}

	public void setCache(ValueSetExpansionCache cache) {
	  this.expansionCache = cache;	
	}

  @Override
  public List<String> getResourceNames() {
    List<String> result = new ArrayList<String>();
    for (StructureDefinition sd : structures.values()) {
      if (sd.getKind() == StructureDefinitionKind.RESOURCE && !sd.hasBaseType())
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
    if (sd.hasBaseType())
      return false;
    return sd.getKind() == StructureDefinitionKind.RESOURCE;
  }

  @Override
  public boolean hasLinkFor(String typeSimple) {
    return false;
  }

  @Override
  public String getLinkFor(String typeSimple) {
    return null;
  }

  @Override
  public BindingResolution resolveBinding(ElementDefinitionBindingComponent binding) {
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
    result.addAll(structures.values());
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

}
