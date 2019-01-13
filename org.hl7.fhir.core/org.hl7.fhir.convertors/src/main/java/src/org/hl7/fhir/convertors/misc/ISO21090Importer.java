package org.hl7.fhir.convertors.misc;

/*-
 * #%L
 * org.hl7.fhir.convertors
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


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.hl7.fhir.dstu3.context.IWorkerContext;
import org.hl7.fhir.dstu3.context.SimpleWorkerContext;
import org.hl7.fhir.dstu3.formats.IParser.OutputStyle;
import org.hl7.fhir.dstu3.formats.XmlParser;
import org.hl7.fhir.dstu3.model.ElementDefinition;
import org.hl7.fhir.dstu3.model.ElementDefinition.PropertyRepresentation;
import org.hl7.fhir.dstu3.model.Enumerations.BindingStrength;
import org.hl7.fhir.dstu3.model.Enumerations.PublicationStatus;
import org.hl7.fhir.dstu3.model.StructureDefinition;
import org.hl7.fhir.dstu3.model.StructureDefinition.StructureDefinitionKind;
import org.hl7.fhir.dstu3.model.StructureDefinition.TypeDerivationRule;
import org.hl7.fhir.dstu3.model.UriType;
import org.hl7.fhir.dstu3.model.ValueSet;
import org.hl7.fhir.dstu3.model.ValueSet.ConceptSetComponent;
import org.hl7.fhir.dstu3.utils.ToolingExtensions;
import org.hl7.fhir.exceptions.FHIRFormatError;
import org.hl7.fhir.utilities.Utilities;
import org.hl7.fhir.utilities.xml.XMLUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class ISO21090Importer {
  
  private class Property {
    private boolean isattr;
    private String name;
    private int min;
    private int max;
    private String type;
    private String doco;
    private String binding;
  }
  
  private class DataType {
    private boolean isAbstract;
    private String name;
    private String doco;
    private String parent;
    private List<Property> properties = new ArrayList<Property>();
    
  }
  
  public class EnumValueSet {
    private String name;
    private String template;
    private String system;
    private List<String> codes = new ArrayList<String>();
    private Map<String, String> members = new HashMap<String, String>();
  }
  
  public static void main(String[] args) throws Exception {
    new ISO21090Importer().process();
  }

  private IWorkerContext ctxt;
  private Element schema;
  private Map<String, EnumValueSet> bindings = new HashMap<String, EnumValueSet>();
  private Map<String, DataType> types = new HashMap<String, DataType>();

  private void process() throws Exception {
    ctxt = SimpleWorkerContext.fromPack("C:\\work\\org.hl7.fhir\\build\\publish\\igpack.zip");
    load();
    processEnums();
    processDataTypes();
    generate();
    
    System.out.print("done");
  }

  private void generate() throws Exception {
    for (EnumValueSet evs : bindings.values()) {
      generateValueSet(evs);
    }
    for (DataType dt : types.values()) {
      generateType(dt);
    }
  }

  private void generateType(DataType dt) throws Exception {
    StructureDefinition sd = new StructureDefinition();
    sd.setId(dt.name);
    sd.setUrl("http://hl7.org/fhir/iso21090/StructureDefinition/"+sd.getId());
    sd.setName(dt.name+" data type");
    sd.setStatus(PublicationStatus.ACTIVE);
    sd.setExperimental(false);
    sd.setPublisher("HL7 / ISO");
    sd.setDate(new Date());
    sd.setDescription(dt.doco);
    sd.setKind(StructureDefinitionKind.LOGICAL);
    sd.setAbstract(Utilities.existsInList(dt.name, "HXIT", "QTY"));
    sd.setType("Element");
    if (dt.parent == null)
      sd.setBaseDefinition("http://hl7.org/fhir/StructureDefinition/Element");
    else
      sd.setBaseDefinition("http://hl7.org/fhir/iso21090/StructureDefinition/"+dt.parent);
    sd.setDerivation(TypeDerivationRule.SPECIALIZATION);
    ElementDefinition ed = sd.getDifferential().addElement();
    ed.setPath(dt.name);
    produceProperties(sd.getDifferential().getElement(), dt.name, dt.properties, true, false);
    produceProperties(sd.getDifferential().getElement(), dt.name, dt.properties, false, false);
    ed = sd.getSnapshot().addElement();
    ed.setPath(dt.name);
    if (dt.parent != null)
      addParentProperties(sd.getSnapshot().getElement(), dt.name, dt.parent, true, true);
    produceProperties(sd.getSnapshot().getElement(), dt.name, dt.properties, true, true);
    if (dt.parent != null)
      addParentProperties(sd.getSnapshot().getElement(), dt.name, dt.parent, false, true);
    produceProperties(sd.getSnapshot().getElement(), dt.name, dt.properties, false, true);
    ed.getBase().setPath(ed.getPath()).setMin(ed.getMin()).setMax(ed.getMax());
    new XmlParser().setOutputStyle(OutputStyle.PRETTY).compose(new FileOutputStream("c:\\temp\\iso21090\\StructureDefinition-"+dt.name+".xml"), sd);    
  }
  
  private void addParentProperties(List<ElementDefinition> elements, String name, String parent, boolean attrMode, boolean snapshot) throws FHIRFormatError {
    DataType dt = types.get(parent);
    if (dt == null)
      throw new Error("No find "+parent);
    if (dt.parent != null)
      addParentProperties(elements, name, dt.parent, attrMode, snapshot);
    produceProperties(elements, name, dt.properties, attrMode, snapshot);
  }

  private void produceProperties(List<ElementDefinition> elements, String name, List<Property> properties, boolean attrMode, boolean snapshot) throws FHIRFormatError {
    for (Property p : properties) {
      if (p.isattr == attrMode) {
        ElementDefinition ed = new ElementDefinition();
        elements.add(ed);
        ed.setPath(name+"."+p.name);
        if (p.type.startsWith("xsd:"))
          ToolingExtensions.addStringExtension(ed.addType(), ToolingExtensions.EXT_XML_TYPE, p.type);
        else
          ed.addType().setCode(p.type);
        ed.setMin(p.min);
        ed.setMax(p.max == Integer.MAX_VALUE ? "*" : Integer.toString(p.max));
        ed.setDefinition(p.doco);
        if (p.isattr)
          ed.addRepresentation(PropertyRepresentation.XMLATTR);
        if (p.binding != null)
          ed.getBinding().setStrength(BindingStrength.REQUIRED).setValueSet(new UriType("http://hl7.org/fhir/iso21090/ValueSet/"+p.binding));
        if (snapshot)
          ed.getBase().setPath(ed.getPath()).setMin(ed.getMin()).setMax(ed.getMax());
      }
    }
  }

  private void generateValueSet(EnumValueSet evs) throws Exception {
    ValueSet bvs = ctxt.fetchResource(ValueSet.class, evs.template);
    if (bvs == null)
      throw new Exception("Did not find template value set "+evs.template);
    ValueSet vs = bvs.copy();
    vs.getCompose().getInclude().clear();
    vs.getIdentifier().clear();
    vs.setName("ISO 20190 "+evs.name+" Enumeration");
    vs.setId(evs.name);
    vs.setUrl("http://hl7.org/fhir/iso21090/ValueSet/"+vs.getId());
    vs.setDate(new Date());
    vs.setExperimental(false);
    ConceptSetComponent inc = vs.getCompose().addInclude().setSystem(evs.system);
    for (String code : evs.codes) {
      inc.addConcept().setCode(code);
    }
    new XmlParser().setOutputStyle(OutputStyle.PRETTY).compose(new FileOutputStream("c:\\temp\\iso21090\\ValueSet-"+evs.name+".xml"), vs);
  }

  private void processDataTypes() {
    Element type = XMLUtil.getFirstChild(schema);
    while (type != null) {
      if (type.getTagName().equals("xsd:complexType")) {
        String n = type.getAttribute("name");
        if (!(n.contains(".") || n.contains("_") || n.equals("XReference")))
            processDataType(n, type);
      }
      type = XMLUtil.getNextSibling(type);
    }
  }

  private void processDataType(String n, Element type) {
    DataType dt = new DataType();
    types.put(n, dt);
    dt.name = n;
    dt.doco = getDoco(type);
    Element cnt;
    Element ext = XMLUtil.getNamedChild(XMLUtil.getNamedChild(type, "xsd:complexContent"), "xsd:extension");
    if (ext != null) {
      dt.parent = ext.getAttribute("base");
      cnt = XMLUtil.getFirstChild(ext);
    } else {
      cnt = XMLUtil.getFirstChild(type);
    }
    if (cnt.getTagName().equals("xsd:annotation"))
      cnt = XMLUtil.getNextSibling(cnt);
    System.out.println(n+" ("+dt.parent+")");
    while (cnt != null) {
      if (cnt.getTagName().equals("xsd:attribute")) {
        processAttribute(dt, cnt);
      } else if (cnt.getTagName().equals("xsd:sequence")) {
        Element e = XMLUtil.getFirstChild(cnt);
        while (e != null) {
          if (e.getTagName().equals("xsd:element")) {
            processElement(dt, e);
          } else
            System.out.println("2. ignore "+e.getTagName());
          
          e = XMLUtil.getNextSibling(e);
        }
      } else
        System.out.println("ignore "+cnt.getTagName());
      cnt = XMLUtil.getNextSibling(cnt);
    }
  }

  private void processElement(DataType dt, Element elem) {
    Property prop = new Property();
    prop.name = elem.getAttribute("name");
    prop.min = Integer.parseInt(elem.getAttribute("minOccurs"));
    prop.max = "unbounded".equals(elem.getAttribute("maxOccurs")) ? Integer.MAX_VALUE : Integer.parseInt(elem.getAttribute("maxOccurs"));
    prop.type = elem.getAttribute("type");
    prop.doco = getDoco(elem);
    dt.properties.add(prop);
    System.out.println("  "+prop.name+" : "+prop.type+" ["+Integer.toString(prop.min)+".."+Integer.toString(prop.max)+"]");
  }

  private void processAttribute(DataType dt, Element attr) {
    Property prop = new Property();
    prop.name = attr.getAttribute("name");
    prop.type = attr.getAttribute("type");
    if (!prop.type.startsWith("xsd:")) {
      if (Utilities.noString(prop.type))
        prop.type = "xsd:string";
      else if (bindings.containsKey(prop.type)) { 
        prop.binding = prop.type;
        prop.type = "xsd:string";
      } else if (prop.type.startsWith("set_") && bindings.containsKey(prop.type.substring(4))) { 
        prop.binding = prop.type.substring(4);
        prop.type = "xsd:string";
        prop.max = Integer.MAX_VALUE;
      } else if ("Uid".equals(prop.type))
        prop.type = "xsd:string";
      else if ("Code".equals(prop.type))
        prop.type = "xsd:token";
      else if ("Decimal".equals(prop.type))
        prop.type = "xsd:decimal";
      else
        throw new Error("Unknown type "+prop.type+" on "+dt.name+"."+prop.name);
    }     
    prop.min = "optional".equals(attr.getAttribute("use")) ? 0 : 1;
    prop.max = 1;
    prop.doco = getDoco(attr);
    prop.isattr = true;
    dt.properties.add(prop);
    System.out.println("  "+prop.name+" : "+prop.type+" ["+Integer.toString(prop.min)+".."+Integer.toString(prop.max)+"]");
  }

  private void processEnums() {
    Element type = XMLUtil.getFirstChild(schema);
    while (type != null) {
      if (type.getTagName().equals("xsd:simpleType")) {
        Element res = XMLUtil.getFirstChild(type);
        Element en = XMLUtil.getFirstChild(res);
        if (en != null && en.getTagName().equals("xsd:enumeration") && !type.getAttribute("name").contains("."))
          processEnum(type.getAttribute("name"), en);
      }
      type = XMLUtil.getNextSibling(type);
    }
  }

  private void processEnum(String n, Element en) {
    EnumValueSet vs = new EnumValueSet();
    bindings.put(n, vs);
    vs.name = n;
    String v3n;
    if (n.contains("EntityName"))
      v3n = n+"R2";
    else if (n.equals("Compression"))
      v3n = "CompressionAlgorithm";
    else if (n.equals("UpdateMode"))
      v3n = "HL7UpdateMode";
    else if (n.equals("UncertaintyType"))
      v3n = "ProbabilityDistributionType";
    else if (n.equals("TelecommunicationAddressUse") || n.equals("PostalAddressUse"))
      v3n = "AddressUse"; 
    else if (n.equals("TelecommunicationCapability"))
      v3n = "TelecommunicationCapabilities"; 
    else
      v3n = n;
    vs.system = "http://hl7.org/fhir/v3-"+v3n;
    vs.template = "http://hl7.org/fhir/ValueSet/v3-"+v3n;
    System.out.println("Enum: "+n+" == "+vs.system);
    while (en != null) {
      vs.codes.add(en.getAttribute("value"));
      vs.members.put(en.getAttribute("value"), getDoco(en));
      en = XMLUtil.getNextSibling(en);       
    }
  }


  private String getDoco(Element en) {
    Element doco = XMLUtil.getNamedChild(XMLUtil.getNamedChild(en, "xsd:annotation"), "xsd:documentation");
    return doco == null ? null : doco.getTextContent();
  }

  private void load() throws ParserConfigurationException, FileNotFoundException, SAXException, IOException {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    factory.setNamespaceAware(false);
    DocumentBuilder builder = factory.newDocumentBuilder();
    Document doc = builder.parse(new FileInputStream("C:\\work\\projects\\org.hl7.v3.dt\\iso\\iso-21090-datatypes.xsd"));
    schema = doc.getDocumentElement();
  }
}
