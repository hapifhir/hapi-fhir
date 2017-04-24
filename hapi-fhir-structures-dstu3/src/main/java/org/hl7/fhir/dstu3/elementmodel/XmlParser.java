package org.hl7.fhir.dstu3.elementmodel;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.sax.SAXSource;

import org.hl7.fhir.dstu3.context.IWorkerContext;
import org.hl7.fhir.dstu3.elementmodel.Element.SpecialElement;
import org.hl7.fhir.dstu3.formats.FormatUtilities;
import org.hl7.fhir.dstu3.formats.IParser.OutputStyle;
import org.hl7.fhir.dstu3.model.DateTimeType;
import org.hl7.fhir.dstu3.model.ElementDefinition.PropertyRepresentation;
import org.hl7.fhir.dstu3.model.Enumeration;
import org.hl7.fhir.dstu3.model.StructureDefinition;
import org.hl7.fhir.dstu3.utils.ToolingExtensions;
import org.hl7.fhir.dstu3.utils.formats.XmlLocationAnnotator;
import org.hl7.fhir.dstu3.utils.formats.XmlLocationData;
import org.hl7.fhir.exceptions.DefinitionException;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.exceptions.FHIRFormatError;
import org.hl7.fhir.utilities.Utilities;
import org.hl7.fhir.utilities.validation.ValidationMessage.IssueSeverity;
import org.hl7.fhir.utilities.validation.ValidationMessage.IssueType;
import org.hl7.fhir.utilities.xhtml.XhtmlComposer;
import org.hl7.fhir.utilities.xhtml.XhtmlNode;
import org.hl7.fhir.utilities.xhtml.XhtmlParser;
import org.hl7.fhir.utilities.xml.IXMLWriter;
import org.hl7.fhir.utilities.xml.XMLUtil;
import org.hl7.fhir.utilities.xml.XMLWriter;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

public class XmlParser extends ParserBase {
  private boolean allowXsiLocation;

  public XmlParser(IWorkerContext context) {
    super(context);
  }

  
  public boolean isAllowXsiLocation() {
    return allowXsiLocation;
  }

  public void setAllowXsiLocation(boolean allowXsiLocation) {
    this.allowXsiLocation = allowXsiLocation;
  }


  public Element parse(InputStream stream) throws FHIRFormatError, DefinitionException, FHIRException, IOException {
		Document doc = null;
  	try {
  		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
  		// xxe protection
  		factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
  		factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
  		factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
  		factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
  		factory.setXIncludeAware(false);
  		factory.setExpandEntityReferences(false);
  			
  		factory.setNamespaceAware(true);
  		if (policy == ValidationPolicy.EVERYTHING) {
  			// use a slower parser that keeps location data
  			TransformerFactory transformerFactory = TransformerFactory.newInstance();
  			Transformer nullTransformer = transformerFactory.newTransformer();
  			DocumentBuilder docBuilder = factory.newDocumentBuilder();
  			doc = docBuilder.newDocument();
  			DOMResult domResult = new DOMResult(doc);
  			SAXParserFactory spf = SAXParserFactory.newInstance();
  			spf.setNamespaceAware(true);
  			spf.setValidating(false);
    		// xxe protection
  		  spf.setFeature("http://xml.org/sax/features/external-general-entities", false);
        spf.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
  			SAXParser saxParser = spf.newSAXParser();
  			XMLReader xmlReader = saxParser.getXMLReader();
    		// xxe protection
  		  xmlReader.setFeature("http://xml.org/sax/features/external-general-entities", false);
  		  xmlReader.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
  	   			
  			XmlLocationAnnotator locationAnnotator = new XmlLocationAnnotator(xmlReader, doc);
  			InputSource inputSource = new InputSource(stream);
  			SAXSource saxSource = new SAXSource(locationAnnotator, inputSource);
  			nullTransformer.transform(saxSource, domResult);
  		} else {
  			DocumentBuilder builder = factory.newDocumentBuilder();
  			doc = builder.parse(stream);
  		}
  	} catch (Exception e) {
      logError(0, 0, "(syntax)", IssueType.INVALID, e.getMessage(), IssueSeverity.FATAL);
      doc = null;
  	}
  	if (doc == null)
  		return null;
  	else
      return parse(doc);
  }

  private void checkForProcessingInstruction(Document document) throws FHIRFormatError {
    if (policy == ValidationPolicy.EVERYTHING) {
      Node node = document.getFirstChild();
      while (node != null) {
        if (node.getNodeType() == Node.PROCESSING_INSTRUCTION_NODE)
          logError(line(document), col(document), "(document)", IssueType.INVALID, "No processing instructions allowed in resources", IssueSeverity.ERROR);
        node = node.getNextSibling();
      }
    }
  }

  
  private int line(Node node) {
		XmlLocationData loc = (XmlLocationData) node.getUserData(XmlLocationData.LOCATION_DATA_KEY);
		return loc == null ? 0 : loc.getStartLine();
  }

  private int col(Node node) {
		XmlLocationData loc = (XmlLocationData) node.getUserData(XmlLocationData.LOCATION_DATA_KEY);
		return loc == null ? 0 : loc.getStartColumn();
  }

  public Element parse(Document doc) throws FHIRFormatError, DefinitionException, FHIRException, IOException {
    checkForProcessingInstruction(doc);
    org.w3c.dom.Element element = doc.getDocumentElement();
    return parse(element);
  }
  
  public Element parse(org.w3c.dom.Element element) throws FHIRFormatError, DefinitionException, FHIRException, IOException {
    String ns = element.getNamespaceURI();
    String name = element.getLocalName();
    String path = "/"+pathPrefix(ns)+name;
    
    StructureDefinition sd = getDefinition(line(element), col(element), ns, name);
    if (sd == null)
      return null;

    Element result = new Element(element.getLocalName(), new Property(context, sd.getSnapshot().getElement().get(0), sd));
    checkElement(element, path, result.getProperty());
    result.markLocation(line(element), col(element));
    result.setType(element.getLocalName());
    parseChildren(path, element, result);
    result.numberChildren();
    return result;
  }

  private String pathPrefix(String ns) {
    if (Utilities.noString(ns))
      return "";
    if (ns.equals(FormatUtilities.FHIR_NS))
      return "f:";
    if (ns.equals(FormatUtilities.XHTML_NS))
      return "h:";
    if (ns.equals("urn:hl7-org:v3"))
      return "v3:";
    return "?:";
  }

  private boolean empty(org.w3c.dom.Element element) {
    for (int i = 0; i < element.getAttributes().getLength(); i++) {
      String n = element.getAttributes().item(i).getNodeName();
      if (!n.equals("xmlns") && !n.startsWith("xmlns:"))
        return false;
    }
    if (!Utilities.noString(element.getTextContent().trim()))
      return false;
    
    Node n = element.getFirstChild();
    while (n != null) {
      if (n.getNodeType() == Node.ELEMENT_NODE)
        return false;
      n = n.getNextSibling();
    }
    return true;
  }
  
  private void checkElement(org.w3c.dom.Element element, String path, Property prop) throws FHIRFormatError {
    if (policy == ValidationPolicy.EVERYTHING) {
      if (empty(element))
        logError(line(element), col(element), path, IssueType.INVALID, "Element must have some content", IssueSeverity.ERROR);
      String ns = FormatUtilities.FHIR_NS;
      if (ToolingExtensions.hasExtension(prop.getDefinition(), "http://hl7.org/fhir/StructureDefinition/elementdefinition-namespace"))
      	ns = ToolingExtensions.readStringExtension(prop.getDefinition(), "http://hl7.org/fhir/StructureDefinition/elementdefinition-namespace");
      else if (ToolingExtensions.hasExtension(prop.getStructure(), "http://hl7.org/fhir/StructureDefinition/elementdefinition-namespace"))
      	ns = ToolingExtensions.readStringExtension(prop.getStructure(), "http://hl7.org/fhir/StructureDefinition/elementdefinition-namespace");
      if (!element.getNamespaceURI().equals(ns))
        logError(line(element), col(element), path, IssueType.INVALID, "Wrong namespace - expected '"+ns+"'", IssueSeverity.ERROR);
    }
  }

  public Element parse(org.w3c.dom.Element base, String type) throws Exception {
    StructureDefinition sd = getDefinition(0, 0, FormatUtilities.FHIR_NS, type);
    Element result = new Element(base.getLocalName(), new Property(context, sd.getSnapshot().getElement().get(0), sd));
    String path = "/"+pathPrefix(base.getNamespaceURI())+base.getLocalName();
    checkElement(base, path, result.getProperty());
    result.setType(base.getLocalName());
    parseChildren(path, base, result);
    result.numberChildren();
    return result;
  }

  private void parseChildren(String path, org.w3c.dom.Element node, Element context) throws FHIRFormatError, FHIRException, IOException, DefinitionException {
  	// this parsing routine retains the original order in a the XML file, to support validation
  	reapComments(node, context);
    List<Property> properties = context.getProperty().getChildProperties(context.getName(), XMLUtil.getXsiType(node));

  	String text = XMLUtil.getDirectText(node).trim();
    if (!Utilities.noString(text)) {
    	Property property = getTextProp(properties);
    	if (property != null) {
  	    context.getChildren().add(new Element(property.getName(), property, property.getType(), text).markLocation(line(node), col(node)));
    	} else {
        logError(line(node), col(node), path, IssueType.STRUCTURE, "Text should not be present", IssueSeverity.ERROR);
    	}    		
    }
    
    for (int i = 0; i < node.getAttributes().getLength(); i++) {
    	Node attr = node.getAttributes().item(i);
    	if (!(attr.getNodeName().equals("xmlns") || attr.getNodeName().startsWith("xmlns:"))) {
      	Property property = getAttrProp(properties, attr.getNodeName());
      	if (property != null) {
	    	  String av = attr.getNodeValue();
	    	  if (ToolingExtensions.hasExtension(property.getDefinition(), "http://www.healthintersections.com.au/fhir/StructureDefinition/elementdefinition-dateformat"))
	    	  	av = convertForDateFormat(ToolingExtensions.readStringExtension(property.getDefinition(), "http://www.healthintersections.com.au/fhir/StructureDefinition/elementdefinition-dateformat"), av);
	    		if (property.getName().equals("value") && context.isPrimitive())
	    			context.setValue(av);
	    		else
	    	    context.getChildren().add(new Element(property.getName(), property, property.getType(), av).markLocation(line(node), col(node)));
        } else if (!allowXsiLocation || !attr.getNodeName().endsWith(":schemaLocation") ) {
          logError(line(node), col(node), path, IssueType.STRUCTURE, "Undefined attribute '@"+attr.getNodeName()+"' on "+node.getNodeName(), IssueSeverity.ERROR);      		
      	}
    	}
    }
    
    Node child = node.getFirstChild();
    while (child != null) {
    	if (child.getNodeType() == Node.ELEMENT_NODE) {
    		Property property = getElementProp(properties, child.getLocalName());
    		if (property != null) {
    			if (!property.isChoice() && "xhtml".equals(property.getType())) {
          	XhtmlNode xhtml = new XhtmlParser().setValidatorMode(true).parseHtmlNode((org.w3c.dom.Element) child);
						context.getChildren().add(new Element("div", property, "xhtml", new XhtmlComposer().setXmlOnly(true).compose(xhtml)).setXhtml(xhtml).markLocation(line(child), col(child)));
    			} else {
    			  String npath = path+"/"+pathPrefix(child.getNamespaceURI())+child.getLocalName();
    				Element n = new Element(child.getLocalName(), property).markLocation(line(child), col(child));
    				checkElement((org.w3c.dom.Element) child, npath, n.getProperty());
    				boolean ok = true;
    				if (property.isChoice()) {
    					if (property.getDefinition().hasRepresentation(PropertyRepresentation.TYPEATTR)) {
    						String xsiType = ((org.w3c.dom.Element) child).getAttributeNS(FormatUtilities.NS_XSI, "type");
    						if (xsiType == null) {
    		          logError(line(child), col(child), path, IssueType.STRUCTURE, "No type found on '"+child.getLocalName()+'"', IssueSeverity.ERROR);
    		          ok = false;
    						} else {
    							if (xsiType.contains(":"))
    								xsiType = xsiType.substring(xsiType.indexOf(":")+1);
    							n.setType(xsiType);
    						}
    					} else
    					  n.setType(n.getType());
    				}
    				context.getChildren().add(n);
    				if (ok) {
    					if (property.isResource())
                parseResource(npath, (org.w3c.dom.Element) child, n, property);
    					else
    						parseChildren(npath, (org.w3c.dom.Element) child, n);
    				}
    			}
      	} else
          logError(line(child), col(child), path, IssueType.STRUCTURE, "Undefined element '"+child.getLocalName()+'"', IssueSeverity.ERROR);    		
    	} else if (child.getNodeType() == Node.CDATA_SECTION_NODE){
        logError(line(child), col(child), path, IssueType.STRUCTURE, "CDATA is not allowed", IssueSeverity.ERROR);      		
    	} else if (!Utilities.existsInList(child.getNodeType(), 3, 8)) {
        logError(line(child), col(child), path, IssueType.STRUCTURE, "Node type "+Integer.toString(child.getNodeType())+" is not allowed", IssueSeverity.ERROR);
    	}
    	child = child.getNextSibling();
    }
  }

  private Property getElementProp(List<Property> properties, String nodeName) {
		List<Property> propsSortedByLongestFirst = new ArrayList<Property>(properties);
		// sort properties according to their name longest first, so .requestOrganizationReference comes first before .request[x]
		// and therefore the longer property names get evaluated first
		Collections.sort(propsSortedByLongestFirst, new Comparator<Property>() {
			@Override
			public int compare(Property o1, Property o2) {
				return o2.getName().length() - o1.getName().length();
			}
		});
  	for (Property p : propsSortedByLongestFirst)
  		if (!p.getDefinition().hasRepresentation(PropertyRepresentation.XMLATTR) && !p.getDefinition().hasRepresentation(PropertyRepresentation.XMLTEXT)) {
  		  if (p.getName().equals(nodeName)) 
				  return p;
  		  if (p.getName().endsWith("[x]") && nodeName.length() > p.getName().length()-3 && p.getName().substring(0, p.getName().length()-3).equals(nodeName.substring(0, p.getName().length()-3))) 
				  return p;
  		}
  	return null;
	}

	private Property getAttrProp(List<Property> properties, String nodeName) {
  	for (Property p : properties)
  		if (p.getName().equals(nodeName) && p.getDefinition().hasRepresentation(PropertyRepresentation.XMLATTR)) 
				return p;
  	return null;
  }

	private Property getTextProp(List<Property> properties) {
  	for (Property p : properties)
  		if (p.getDefinition().hasRepresentation(PropertyRepresentation.XMLTEXT)) 
				return p;
  	return null;
	}

	private String convertForDateFormat(String fmt, String av) throws FHIRException {
  	if ("v3".equals(fmt)) {
  		DateTimeType d = DateTimeType.parseV3(av);
  		return d.asStringValue();
  	} else
  		throw new FHIRException("Unknown Data format '"+fmt+"'");
	}

  private void parseResource(String string, org.w3c.dom.Element container, Element parent, Property elementProperty) throws FHIRFormatError, DefinitionException, FHIRException, IOException {
  	org.w3c.dom.Element res = XMLUtil.getFirstChild(container);
    String name = res.getLocalName();
    StructureDefinition sd = context.fetchResource(StructureDefinition.class, "http://hl7.org/fhir/StructureDefinition/"+name);
    if (sd == null)
      throw new FHIRFormatError("Contained resource does not appear to be a FHIR resource (unknown name '"+res.getLocalName()+"')");
    parent.updateProperty(new Property(context, sd.getSnapshot().getElement().get(0), sd), SpecialElement.fromProperty(parent.getProperty()), elementProperty);
    parent.setType(name);
    parseChildren(res.getLocalName(), res, parent);
	}

	private void reapComments(org.w3c.dom.Element element, Element context) {
	  Node node = element.getPreviousSibling();
	  while (node != null && node.getNodeType() != Node.ELEMENT_NODE) {
	  	if (node.getNodeType() == Node.COMMENT_NODE)
	  		context.getComments().add(0, node.getTextContent());
	  	node = node.getPreviousSibling();
	  }
		node = element.getLastChild();
		while (node != null && node.getNodeType() != Node.ELEMENT_NODE) {
			node = node.getPreviousSibling();
		}
		while (node != null) {
			if (node.getNodeType() == Node.COMMENT_NODE)
				context.getComments().add(node.getTextContent());
			node = node.getNextSibling();
		}
	}

	private boolean isAttr(Property property) {
		for (Enumeration<PropertyRepresentation> r : property.getDefinition().getRepresentation()) {
			if (r.getValue() == PropertyRepresentation.XMLATTR) {
				return true;
			}
		}
		return false;
	}

  private boolean isText(Property property) {
		for (Enumeration<PropertyRepresentation> r : property.getDefinition().getRepresentation()) {
			if (r.getValue() == PropertyRepresentation.XMLTEXT) {
				return true;
			}
		}
		return false;
  }

	@Override
  public void compose(Element e, OutputStream stream, OutputStyle style, String base) throws IOException {
    XMLWriter xml = new XMLWriter(stream, "UTF-8");
    xml.setPretty(style == OutputStyle.PRETTY);
    xml.start();
    xml.setDefaultNamespace(e.getProperty().getNamespace());
    composeElement(xml, e, e.getType());
    xml.end();

  }

  public void compose(Element e, IXMLWriter xml) throws Exception {
    xml.start();
    xml.setDefaultNamespace(e.getProperty().getNamespace());
    composeElement(xml, e, e.getType());
    xml.end();
  }

  private void composeElement(IXMLWriter xml, Element element, String elementName) throws IOException {
    for (String s : element.getComments()) {
      xml.comment(s, true);
    }
    if (isText(element.getProperty())) {
      if (linkResolver != null)
        xml.link(linkResolver.resolveProperty(element.getProperty()));
      xml.enter(elementName);
      xml.text(element.getValue());
      xml.exit(elementName);      
    } else if (element.isPrimitive() || (element.hasType() && isPrimitive(element.getType()))) {
      if (element.getType().equals("xhtml")) {
        xml.escapedText(element.getValue());
      } else if (isText(element.getProperty())) {
        if (linkResolver != null)
          xml.link(linkResolver.resolveProperty(element.getProperty()));
        xml.text(element.getValue());
      } else {
        if (element.hasValue()) {
          if (linkResolver != null)
            xml.link(linkResolver.resolveType(element.getType()));
        xml.attribute("value", element.getValue());
        }
        if (linkResolver != null)
          xml.link(linkResolver.resolveProperty(element.getProperty()));
				if (element.hasChildren()) {
					xml.enter(elementName);
					for (Element child : element.getChildren()) 
						composeElement(xml, child, child.getName());
					xml.exit(elementName);
				} else
        xml.element(elementName);
      }
    } else {
      for (Element child : element.getChildren()) {
        if (isAttr(child.getProperty())) {
          if (linkResolver != null)
            xml.link(linkResolver.resolveType(child.getType()));
          xml.attribute(child.getName(), child.getValue());
      }
      }
      if (linkResolver != null)
        xml.link(linkResolver.resolveProperty(element.getProperty()));
      xml.enter(elementName);
      if (element.getSpecial() != null) {
        if (linkResolver != null)
          xml.link(linkResolver.resolveProperty(element.getProperty()));
        xml.enter(element.getType());
      }
      for (Element child : element.getChildren()) {
        if (isText(child.getProperty())) {
          if (linkResolver != null)
            xml.link(linkResolver.resolveProperty(element.getProperty()));
          xml.text(child.getValue());
        } else if (!isAttr(child.getProperty()))
          composeElement(xml, child, child.getName());
      }
	    if (element.getSpecial() != null)
        xml.exit(element.getType());
      xml.exit(elementName);
    }
  }

}
