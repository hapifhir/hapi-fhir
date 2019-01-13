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
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.lang3.CharUtils;
import org.hl7.fhir.dstu3.formats.IParser.OutputStyle;
import org.hl7.fhir.dstu3.formats.XmlParser;
import org.hl7.fhir.dstu3.model.ElementDefinition;
import org.hl7.fhir.dstu3.model.Enumerations.PublicationStatus;
import org.hl7.fhir.dstu3.model.StructureDefinition;
import org.hl7.fhir.utilities.xml.XMLUtil;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;

public class ADLImporter {

	public class Cardinality {
		private String min;
		private String max;
	}

	public class TextSet {

		private String text;
		private String description;
		private String comment;


		public String getText() {
			return text;
		}

		public String getDescription() {
			return description;
		}

		public String getComment() {
			return comment;
		}

		public void setText(String value) {
			this.text = value;
		}

		public void setDescription(String value) {
			this.description = value;
		}
		public void setComment(String value) {
			this.comment = value;
		}

	}

	public class NodeTreeEntry {
		private String name;
		private String atCode;
		private String typeName;
		private Cardinality cardinality;
		private List<NodeTreeEntry> children = new ArrayList<NodeTreeEntry>();
	}
	
	private String source;
	private String dest;
	private String config;
	private String info;
	private Element adl;
	private Map<String, TextSet> texts = new HashMap<String, TextSet>();
	private Element adlConfig;

	public static void main(String[] args) throws Exception {
		ADLImporter self = new ADLImporter();
  	self.source = getParam(args, "source");
  	self.dest = getParam(args, "dest");
  	self.config = getParam(args, "config");
  	self.info = getParam(args, "info");
	  if (self.source == null || self.dest == null || self.config == null) {
	  	System.out.println("ADL to FHIR StructureDefinition Converter");
	  	System.out.println("This tool takes 4 parameters:");
	  	System.out.println("-source: ADL 1.4 XML representation of an archetype (required)");
	  	System.out.println("-dest: filename of structure definition to produce (required)");
	  	System.out.println("-config: filename of OpenEHR/FHIR knowlege base (required)");
	  	System.out.println("-info: filename of additional knowlege for this adl file (optional)");
	  } else {
	  	self.execute();
	  }
	}

	private static String getParam(String[] args, String name) {
	  for (int i = 0; i < args.length - 1; i++) {
	  	if (args[i].equals("-"+name)) {
	  		return args[i+1];
	  	}
	  }
	  return null;
	}

	private void execute() throws Exception {
	  // load config
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		DocumentBuilder builder = factory.newDocumentBuilder();
		adlConfig = builder.parse(new FileInputStream(config)).getDocumentElement();

	  // load ADL
		builder = factory.newDocumentBuilder();
		adl = builder.parse(new FileInputStream(source)).getDocumentElement();

		check("root", adl.getNamespaceURI(), "http://schemas.openehr.org/v1", "Wrong namespace for ADL XML");
		check("root", adl.getNodeName(), "archetype", "Wrong XML for ADL XML");
		check("root", XMLUtil.getNamedChild(adl, "adl_version").getTextContent(), "1.4", "unsupported ADL version");
		
		String id = XMLUtil.getFirstChild(XMLUtil.getNamedChild(adl, "archetype_id")).getTextContent().split("\\.")[1];
		// create structure definition
		StructureDefinition sd = new StructureDefinition();
		sd.setId(id);
		
		// populate metadata
		Element description = XMLUtil.getNamedChild(adl, "description");
		Element details = XMLUtil.getNamedChild(description, "details");
		sd.setDescription(XMLUtil.getNamedChild(details, "purpose").getTextContent());
		sd.setCopyright(XMLUtil.getNamedChild(details, "copyright").getTextContent());
		sd.setPurpose("Use:\r\n"+XMLUtil.getNamedChild(details, "use").getTextContent()+"\r\n\r\nMisuse:\r\n"+XMLUtil.getNamedChild(details, "misuse").getTextContent());
		List<Element> set = new ArrayList<Element>();
		XMLUtil.getNamedChildren(details, "keywords", set);
		for (Element e : set) 
			sd.addKeyword().setDisplay(e.getTextContent());
		String status = XMLUtil.getNamedChild(description, "lifecycle_state").getTextContent();
		if ("CommitteeDraft".equals(status) || "AuthorDraft".equals(status))
			sd.setStatus(PublicationStatus.DRAFT);
		else
			throw new Exception("Unknown life cycle state "+XMLUtil.getNamedChild(description, "lifecycle_state").getTextContent());

		// load texts from ontology
		Element ontology = XMLUtil.getNamedChild(adl, "ontology");
		Element term_definitions = XMLUtil.getNamedChild(ontology, "term_definitions");
		set.clear();
  	XMLUtil.getNamedChildren(term_definitions, "items", set);
		for (Element item : set) {
			processTextItem(item);
		}

		// load data and protocol
		Element definition = XMLUtil.getNamedChild(adl, "definition");
		NodeTreeEntry root = new NodeTreeEntry();
		root.typeName = XMLUtil.getNamedChild(definition, "rm_type_name").getTextContent();
		root.atCode = XMLUtil.getNamedChild(definition, "node_id").getTextContent();
		root.name = generateToken(root.atCode, true);
		sd.setName(root.name);
		root.cardinality = readCardinality("root", XMLUtil.getNamedChild(definition, "occurrences"));
		set.clear();
  	XMLUtil.getNamedChildren(definition, "attributes", set);
		for (Element item : set) {
			// we're actually skipping this level - we don't care about data protocol etc.
			Element attributes = item; // XMLUtil.getNamedChild(XMLUtil.getNamedChild(item, "children"), "attributes");
			loadChildren(root.atCode, root, attributes);
		}
		dumpChildren("", root);
		genElements(sd, root.name, root);
		
		// save
		new XmlParser().setOutputStyle(OutputStyle.PRETTY).compose(new FileOutputStream(dest), sd);
		System.out.println("done. saved as "+dest);
	}

	private void genElements(StructureDefinition sd, String path, NodeTreeEntry item) throws Exception {
	  ElementDefinition ed = sd.getSnapshot().addElement();
	  ed.setPath(path);
	  ed.setMax(item.cardinality.max);
	  ed.setMin(Integer.parseInt(item.cardinality.min));
	  ed.setShort(texts.get(item.atCode).text);
	  ed.setDefinition(texts.get(item.atCode).description);
	  ed.setComment(texts.get(item.atCode).comment);
	  Element te = findTypeElement(item.typeName);
	  if (te.hasAttribute("profile"))
	    ed.addType().setCode(te.getAttribute("fhir")).setProfile(te.getAttribute("profile"));
	  else
	  	ed.addType().setCode(te.getAttribute("fhir"));
    ed.getBase().setPath(ed.getPath()).setMin(ed.getMin()).setMax(ed.getMax());

  	for (NodeTreeEntry child : item.children) {
  		genElements(sd, path+"."+child.name, child);
  	}
	}

	private Element findTypeElement(String typeName) throws Exception {
		Element dataTypes = XMLUtil.getNamedChild(adlConfig, "dataTypes");
		List<Element> set = new ArrayList<Element>();
  	XMLUtil.getNamedChildren(dataTypes, "dataType", set);
  	for (Element e : set) {
  		if (typeName.equals(e.getAttribute("name")))
  			return e;
  	}
  	throw new Exception("No FHIR equivalent found for "+typeName);
	}

	private void dumpChildren(String prefix, NodeTreeEntry item) throws Exception {
	  Element te = findTypeElement(item.typeName);
	  if (te.hasAttribute("profile"))
		  System.out.println(prefix+item.atCode+" ["+item.cardinality.min+".."+item.cardinality.max+"]:"+te.getAttribute("fhir")+"{"+te.getAttribute("profile")+"} // "+item.name +" = " +texts.get(item.atCode).text);
	  else
		  System.out.println(prefix+item.atCode+" ["+item.cardinality.min+".."+item.cardinality.max+"]:"+te.getAttribute("fhir")+" // "+item.name +" = " +texts.get(item.atCode).text);
	  
	  for (NodeTreeEntry child : item.children)
	  	dumpChildren(prefix+"  ", child);
	}

	private void loadChildren(String path, NodeTreeEntry parent, Element attributes) throws DOMException, Exception {
		List<Element> set = new ArrayList<Element>();
  	XMLUtil.getNamedChildren(attributes, "children", set);
		for (Element e : set) {
			NodeTreeEntry item = new NodeTreeEntry();
			item.typeName = XMLUtil.getNamedChild(e, "rm_type_name").getTextContent();
			item.atCode = XMLUtil.getNamedChild(e, "node_id").getTextContent();
			item.name = generateToken(item.atCode, false);
			item.cardinality = readCardinality(path+"/"+item.atCode, XMLUtil.getNamedChild(e, "occurrences"));
			parent.children.add(item);
			Element attr = XMLUtil.getNamedChild(e, "attributes");
			String type = attr.getAttribute("xsi:type");
			if ("C_SINGLE_ATTRIBUTE".equals(type)) {
				check(path, item.typeName, "ELEMENT", "type for simple element: "+item.typeName);
				checkCardSingle(path, XMLUtil.getNamedChild(attr, "existence"));
				Element c = XMLUtil.getNamedChild(attr, "children");
				checkCardSingle(path, XMLUtil.getNamedChild(c, "occurrences"));
				item.typeName = XMLUtil.getNamedChild(c, "rm_type_name").getTextContent();
			} else {
				check(path, item.typeName, "CLUSTER", "type for complex element");
				loadChildren(path+"/"+item.atCode, item, attr);
			}
		}
	}

	private String generateToken(String atCode, boolean upFirst) {
		if (!texts.containsKey(atCode))
			return atCode;
	  String text = texts.get(atCode).getText();
	  boolean lastText = false;
	  StringBuilder b = new StringBuilder();
	  for (char c : text.toCharArray()) {
	  	boolean ok = CharUtils.isAscii(c);
	  	if (ok) 
	  		if (b.length() == 0)
          ok = Character.isAlphabetic(c);
	  		else
	  			ok = Character.isAlphabetic(c) || Character.isDigit(c);
	  	if (!ok) {
	  		lastText = false;
	  	} else {
	  		if (!lastText && (b.length() > 0 || upFirst)) 
	  			b.append(Character.toUpperCase(c));
	  		else
	  			b.append(Character.toLowerCase(c));
	  		lastText = true;
	  	}
	  }
  	return b.toString();
	}

	private void checkCardSingle(String path, Element element) throws DOMException, Exception {
		check(path, XMLUtil.getNamedChild(element, "lower_included").getTextContent(), "true", "Cardinality check");
	  check(path, XMLUtil.getNamedChild(element, "upper_included").getTextContent(), "true", "Cardinality check"); 
		check(path, XMLUtil.getNamedChild(element, "lower_unbounded").getTextContent(), "false", "Cardinality check"); 
		check(path, XMLUtil.getNamedChild(element, "upper_unbounded").getTextContent(), "false", "Cardinality check"); 
		check(path, XMLUtil.getNamedChild(element, "lower").getTextContent(), "1", "Cardinality check"); 
		check(path, XMLUtil.getNamedChild(element, "upper").getTextContent(), "1", "Cardinality check"); 
	}

	private Cardinality readCardinality(String path, Element element) throws DOMException, Exception {
		check(path, XMLUtil.getNamedChild(element, "lower_included").getTextContent(), "true", "Cardinality check");
		if (XMLUtil.getNamedChild(element, "upper_included") != null)
		  check(path, XMLUtil.getNamedChild(element, "upper_included").getTextContent(), "true", "Cardinality check"); 
		check(path, XMLUtil.getNamedChild(element, "lower_unbounded").getTextContent(), "false", "Cardinality check"); 
		Cardinality card = new Cardinality();
		card.min = XMLUtil.getNamedChild(element, "lower").getTextContent();
		if ("true".equals(XMLUtil.getNamedChild(element, "upper_unbounded").getTextContent()))
			card.max = "*";
		else
			card.max = XMLUtil.getNamedChild(element, "upper").getTextContent();
		return card;
	}

	private void processTextItem(Element item) throws Exception {
    String atcode = item.getAttribute("code");
    TextSet ts = new TextSet();
		List<Element> set = new ArrayList<Element>();
  	XMLUtil.getNamedChildren(item, "items", set);
		for (Element e : set) {
			String code = e.getAttribute("id");
			if (code.equals("text"))
				ts.setText(e.getTextContent());
			else if (code.equals("description"))
				ts.setDescription(e.getTextContent());
			else if (code.equals("comment"))
				ts.setComment(e.getTextContent());
			else 
				throw new Exception("unknown code "+code);
		}
    texts.put(atcode, ts);
	}

	private void check(String path, String found, String expected, String message) throws Exception {
	  if (!expected.equals(found.trim())) 
	  	throw new Exception(message+". Expected '"+expected+"' but found '"+found.trim()+"', at "+path);
	}

}
