package org.hl7.fhir.convertors;

/*-
 * #%L
 * HAPI FHIR - Converter
 * %%
 * Copyright (C) 2014 - 2018 University Health Network
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


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.hl7.fhir.dstu3.formats.IParser.OutputStyle;
import org.hl7.fhir.dstu3.formats.XmlParser;
import org.hl7.fhir.dstu3.model.CodeSystem;
import org.hl7.fhir.dstu3.model.CodeSystem.CodeSystemHierarchyMeaning;
import org.hl7.fhir.dstu3.model.CodeSystem.ConceptDefinitionComponent;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.DateTimeType;
import org.hl7.fhir.dstu3.model.Enumerations.PublicationStatus;
import org.hl7.fhir.dstu3.model.Identifier;
import org.hl7.fhir.dstu3.model.ValueSet;
import org.hl7.fhir.dstu3.terminologies.CodeSystemUtilities;
import org.hl7.fhir.utilities.xml.XMLUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


/**
 * This is defined as a prototype ClaML importer
 * 
 * @author Grahame
 *
 */

public class ICPC2Importer {

  public static void main(String[] args) {
    try {
      ICPC2Importer r = new ICPC2Importer();
      r.setSourceFileName("c:\\temp\\ICPC-2e-v5.0.xml");
      r.setTargetFileNameCS("C:\\temp\\icpc2.xml");
      r.setTargetFileNameVS("C:\\temp\\icpc2-vs.xml");
      r.go();
      System.out.println("Completed OK");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private String sourceFileName; // the ICPC2 ClaML file
  private String targetFileNameVS; // the value set to produce
  private String targetFileNameCS; // the value set to produce
  
  public ICPC2Importer() {
    super();
  }
  public ICPC2Importer(String sourceFileName, String targetFileNameCS, String targetFileNameVS) {
    super();
    this.sourceFileName = sourceFileName;
    this.targetFileNameCS = targetFileNameCS;
    this.targetFileNameVS = targetFileNameVS;
  }
  public String getSourceFileName() {
    return sourceFileName;
  }
  public void setSourceFileName(String sourceFileName) {
    this.sourceFileName = sourceFileName;
  }
  public String getTargetFileNameCS() {
    return targetFileNameCS;
  }
  public void setTargetFileNameCS(String targetFileName) {
    this.targetFileNameCS = targetFileName;
  }

  public String getTargetFileNameVS() {
    return targetFileNameVS;
  }
  public void setTargetFileNameVS(String targetFileName) {
    this.targetFileNameVS = targetFileName;
  }

  public void go() throws Exception {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    factory.setNamespaceAware(false);
    DocumentBuilder builder = factory.newDocumentBuilder();
    Document doc = builder.parse(new FileInputStream(sourceFileName));

    ValueSet vs = new ValueSet();
    vs.setUrl("http://hl7.org/fhir/sid/icpc2/vs");
    Element title = XMLUtil.getNamedChild(doc.getDocumentElement(), "Title");
    vs.setVersion(title.getAttribute("version"));
    vs.setName(title.getAttribute("name"));
    vs.setImmutable(true);
    Element identifier = XMLUtil.getNamedChild(doc.getDocumentElement(), "Identifier");
    vs.setPublisher(identifier.getAttribute("authority"));
    vs.addIdentifier(new Identifier().setValue(identifier.getAttribute("uid")));
    List<Element> authors = new ArrayList<Element>(); 
    XMLUtil.getNamedChildren(XMLUtil.getNamedChild(doc.getDocumentElement(), "Authors"), "Author", authors);
    for (Element a : authors)
      if (!a.getAttribute("name").contains("+"))
        vs.addContact().setName(a.getTextContent());
    vs.setCopyright("The copyright of ICPC, both in hard copy and in electronic form, is owned by Wonca. See http://www.kith.no/templates/kith_WebPage____1110.aspx");
    vs.setStatus(PublicationStatus.ACTIVE);
    vs.setDateElement(new DateTimeType(title.getAttribute("date")));
    
    vs.getCompose().addInclude().setSystem("http://hl7.org/fhir/sid/icpc2");
    CodeSystem cs = new CodeSystem();
    cs.setUrl("http://hl7.org/fhir/sid/icpc2");
    cs.setVersion(title.getAttribute("version"));
    cs.setName(title.getAttribute("name"));
    identifier = XMLUtil.getNamedChild(doc.getDocumentElement(), "Identifier");
    cs.setPublisher(identifier.getAttribute("authority"));
    cs.setIdentifier(new Identifier().setValue(identifier.getAttribute("uid")));
    cs.setHierarchyMeaning(CodeSystemHierarchyMeaning.CLASSIFIEDWITH);
    authors = new ArrayList<Element>(); 
    XMLUtil.getNamedChildren(XMLUtil.getNamedChild(doc.getDocumentElement(), "Authors"), "Author", authors);
    for (Element a : authors)
      if (!a.getAttribute("name").contains("+"))
        cs.addContact().setName(a.getTextContent());
    cs.setCopyright("The copyright of ICPC, both in hard copy and in electronic form, is owned by Wonca. See http://www.kith.no/templates/kith_WebPage____1110.aspx");
    cs.setStatus(PublicationStatus.ACTIVE);
    cs.setDateElement(new DateTimeType(title.getAttribute("date")));
    cs.setValueSet(vs.getUrl());
    
    Map<String, ConceptDefinitionComponent> concepts = new HashMap<String, ConceptDefinitionComponent>();
    List<Element> classes = new ArrayList<Element>(); 
    XMLUtil.getNamedChildren(doc.getDocumentElement(), "Class", classes);
    for (Element cls : classes) {
      processClass(cls, concepts, cs);
    }
    
    XmlParser xml = new XmlParser();
    xml.setOutputStyle(OutputStyle.PRETTY);
    xml.compose(new FileOutputStream(targetFileNameVS), vs);
    xml.compose(new FileOutputStream(targetFileNameCS), cs);
  }
  
  private void processClass(Element cls, Map<String, ConceptDefinitionComponent> concepts, CodeSystem define) {
    ConceptDefinitionComponent concept = new ConceptDefinitionComponent();
    concept.setCode(cls.getAttribute("code"));
    concept.setDefinition(getRubric(cls, "preferred"));
    String s = getRubric(cls, "shortTitle");
    if (s != null && !s.equals(concept.getDefinition()))
      concept.addDesignation().setUse(new Coding().setSystem("http://hl7.org/fhir/sid/icpc2/rubrics").setCode("shortTitle")).setValue(s);
    s = getRubric(cls, "inclusion");
    if (s != null)
      concept.addDesignation().setUse(new Coding().setSystem("http://hl7.org/fhir/sid/icpc2/rubrics").setCode("inclusion")).setValue(s);
    s = getRubric(cls, "exclusion");
    if (s != null)
      concept.addDesignation().setUse(new Coding().setSystem("http://hl7.org/fhir/sid/icpc2/rubrics").setCode("exclusion")).setValue(s);
    s = getRubric(cls, "criteria");
    if (s != null)
      concept.addDesignation().setUse(new Coding().setSystem("http://hl7.org/fhir/sid/icpc2/rubrics").setCode("criteria")).setValue(s);
    s = getRubric(cls, "consider");
    if (s != null)
      concept.addDesignation().setUse(new Coding().setSystem("http://hl7.org/fhir/sid/icpc2/rubrics").setCode("consider")).setValue(s);
    s = getRubric(cls, "note");
    if (s != null)
      concept.addDesignation().setUse(new Coding().setSystem("http://hl7.org/fhir/sid/icpc2/rubrics").setCode("note")).setValue(s);
    
    concepts.put(concept.getCode(), concept);
    List<Element> children = new ArrayList<Element>(); 
    XMLUtil.getNamedChildren(cls, "SubClass", children);
    if (children.size() > 0)
      CodeSystemUtilities.setNotSelectable(define, concept);
    
    Element parent = XMLUtil.getNamedChild(cls, "SuperClass");
    if (parent == null) {
      define.addConcept(concept);
    } else {
      ConceptDefinitionComponent p = concepts.get(parent.getAttribute("code"));
      p.getConcept().add(concept);
    }
  }
  
  private String getRubric(Element cls, String kind) {
    List<Element> rubrics = new ArrayList<Element>(); 
    XMLUtil.getNamedChildren(cls, "Rubric", rubrics);
    for (Element r : rubrics) {
      if (r.getAttribute("kind").equals(kind))
        return XMLUtil.getNamedChild(r,  "Label").getTextContent();
    }
    return null;
  }
  
}
