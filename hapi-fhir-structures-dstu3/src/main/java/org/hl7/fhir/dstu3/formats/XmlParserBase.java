package org.hl7.fhir.dstu3.formats;
/*
Copyright (c) 2011+, HL7, Inc
All rights reserved.

Redistribution and use in source and binary forms, with or without modification, 
are permitted provided that the following conditions are met:

 * Redistributions of source code must retain the above copyright notice, this 
   list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, 
   this list of conditions and the following disclaimer in the documentation 
   and/or other materials provided with the distribution.
 * Neither the name of HL7 nor the names of its contributors may be used to 
   endorse or promote products derived from this software without specific 
   prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND 
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. 
IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, 
INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT 
NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR 
PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, 
WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
POSSIBILITY OF SUCH DAMAGE.

 */

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;

import org.hl7.fhir.exceptions.FHIRFormatError;
import org.hl7.fhir.dstu3.model.*;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.utilities.Utilities;
import org.hl7.fhir.utilities.xhtml.NodeType;
import org.hl7.fhir.utilities.xhtml.XhtmlComposer;
import org.hl7.fhir.utilities.xhtml.XhtmlNode;
import org.hl7.fhir.utilities.xhtml.XhtmlParser;
import org.hl7.fhir.utilities.xml.IXMLWriter;
import org.hl7.fhir.utilities.xml.XMLWriter;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/**
 * General parser for XML content. You instantiate an XmlParser of these, but you 
 * actually use parse or parseGeneral defined on this class
 * 
 * The two classes are separated to keep generated and manually maintained code apart.
 */
public abstract class XmlParserBase extends ParserBase implements IParser {

	@Override
	public ParserType getType() {
		return ParserType.XML;
	}

	// -- in descendent generated code --------------------------------------

	abstract protected Resource parseResource(XmlPullParser xpp) throws XmlPullParserException, IOException, FHIRFormatError ;
	abstract protected Type parseType(XmlPullParser xml, String type) throws XmlPullParserException, IOException, FHIRFormatError ;
	abstract protected void composeType(String prefix, Type type) throws IOException ;

	/* -- entry points --------------------------------------------------- */

	/**
	 * Parse content that is known to be a resource
	 * @ 
	 */
	@Override
	public Resource parse(InputStream input) throws IOException, FHIRFormatError {
		try {
			XmlPullParser xpp = loadXml(input);
			return parse(xpp);
		} catch (XmlPullParserException e) {
			throw new FHIRFormatError(e.getMessage(), e);
		}
	}

	/**
	 * parse xml that is known to be a resource, and that is already being read by an XML Pull Parser
	 * This is if a resource is in a bigger piece of XML.   
	 * @ 
	 */
	public Resource parse(XmlPullParser xpp)  throws IOException, FHIRFormatError, XmlPullParserException {
		if (xpp.getNamespace() == null)
			throw new FHIRFormatError("This does not appear to be a FHIR resource (no namespace '"+xpp.getNamespace()+"') (@ /) "+Integer.toString(xpp.getEventType()));
		if (!xpp.getNamespace().equals(FHIR_NS))
			throw new FHIRFormatError("This does not appear to be a FHIR resource (wrong namespace '"+xpp.getNamespace()+"') (@ /)");
		return parseResource(xpp);
	}

	@Override
	public Type parseType(InputStream input, String knownType) throws IOException, FHIRFormatError  {
		try {
			XmlPullParser xml = loadXml(input);
			return parseType(xml, knownType);
		} catch (XmlPullParserException e) {
			throw new FHIRFormatError(e.getMessage(), e);
		}
	}


	/**
	 * Compose a resource to a stream, possibly using pretty presentation for a human reader (used in the spec, for example, but not normally in production)
	 * @ 
	 */
	@Override
	public void compose(OutputStream stream, Resource resource)  throws IOException {
		XMLWriter writer = new XMLWriter(stream, "UTF-8");
		writer.setPretty(style == OutputStyle.PRETTY);
		writer.start();
		compose(writer, resource, writer.isPretty());
		writer.end();
	}

	/**
	 * Compose a resource to a stream, possibly using pretty presentation for a human reader, and maybe a different choice in the xhtml narrative (used in the spec in one place, but should not be used in production)
	 * @ 
	 */
	public void compose(OutputStream stream, Resource resource, boolean htmlPretty)  throws IOException {
		XMLWriter writer = new XMLWriter(stream, "UTF-8");
		writer.setPretty(style == OutputStyle.PRETTY);
		writer.start();
		compose(writer, resource, htmlPretty);
		writer.end();
	}


	/**
	 * Compose a type to a stream (used in the spec, for example, but not normally in production)
	 * @ 
	 */
	public void compose(OutputStream stream, String rootName, Type type)  throws IOException {
		xml = new XMLWriter(stream, "UTF-8");
		xml.setPretty(style == OutputStyle.PRETTY);
		xml.start();
		xml.setDefaultNamespace(FHIR_NS);
		composeType(Utilities.noString(rootName) ? "value" : rootName, type);
		xml.end();
	}

	@Override
	public void compose(OutputStream stream, Type type, String rootName)  throws IOException {
		xml = new XMLWriter(stream, "UTF-8");
		xml.setPretty(style == OutputStyle.PRETTY);
		xml.start();
		xml.setDefaultNamespace(FHIR_NS);
		composeType(Utilities.noString(rootName) ? "value" : rootName, type);
		xml.end();
	}



	/* -- xml routines --------------------------------------------------- */

	protected XmlPullParser loadXml(String source) throws UnsupportedEncodingException, XmlPullParserException, IOException {
		return loadXml(new ByteArrayInputStream(source.getBytes("UTF-8")));
	}

	protected XmlPullParser loadXml(InputStream stream) throws XmlPullParserException, IOException {
		BufferedInputStream input = new BufferedInputStream(stream);
//		XmlPullParserFactory factory = XmlPullParserFactory.newInstance(System.getProperty(XmlPullParserFactory.PROPERTY_NAME), null);
//		factory.setNamespaceAware(true);
//		factory.setFeature(XmlPullParser.FEATURE_PROCESS_DOCDECL, false);
//		XmlPullParser xpp = factory.newPullParser();
//		xpp.setInput(input, "UTF-8");
//		next(xpp);
//		nextNoWhitespace(xpp);
//
//		return xpp;
		throw new UnsupportedOperationException();
	}

	protected int next(XmlPullParser xpp) throws XmlPullParserException, IOException {
		if (handleComments)
			return xpp.nextToken();
		else
			return xpp.next();    
	}

	protected List<String> comments = new ArrayList<String>();

	protected int nextNoWhitespace(XmlPullParser xpp) throws XmlPullParserException, IOException {
		int eventType = xpp.getEventType();
		while ((eventType == XmlPullParser.TEXT && xpp.isWhitespace()) || (eventType == XmlPullParser.COMMENT) 
				|| (eventType == XmlPullParser.CDSECT) || (eventType == XmlPullParser.IGNORABLE_WHITESPACE)
				|| (eventType == XmlPullParser.PROCESSING_INSTRUCTION) || (eventType == XmlPullParser.DOCDECL)) {
			if (eventType == XmlPullParser.COMMENT) {
				comments.add(xpp.getText());
			} else if (eventType == XmlPullParser.DOCDECL) {
	      throw new XmlPullParserException("DTD declarations are not allowed"); 
      }  
			eventType = next(xpp);
		}
		return eventType;
	}


	protected void skipElementWithContent(XmlPullParser xpp) throws XmlPullParserException, IOException  {
		// when this is called, we are pointing an element that may have content
		while (xpp.getEventType() != XmlPullParser.END_TAG) {
			next(xpp);
			if (xpp.getEventType() == XmlPullParser.START_TAG) 
				skipElementWithContent(xpp);
		}
		next(xpp);
	}

	protected void skipEmptyElement(XmlPullParser xpp) throws XmlPullParserException, IOException {
		while (xpp.getEventType() != XmlPullParser.END_TAG) 
			next(xpp);
		next(xpp);
	}

	protected IXMLWriter xml;
	protected boolean htmlPretty;



	/* -- worker routines --------------------------------------------------- */

	protected void parseTypeAttributes(XmlPullParser xpp, Type t) {
		parseElementAttributes(xpp, t);
	}

	protected void parseElementAttributes(XmlPullParser xpp, Element e) {
		if (xpp.getAttributeValue(null, "id") != null) {
			e.setId(xpp.getAttributeValue(null, "id"));
			idMap.put(e.getId(), e);
		}
		if (!comments.isEmpty()) {
			e.getFormatCommentsPre().addAll(comments);
			comments.clear();
		}
	}

	protected void parseElementClose(Base e) {
		if (!comments.isEmpty()) {
			e.getFormatCommentsPost().addAll(comments);
			comments.clear();
		}
	}

	protected void parseBackboneAttributes(XmlPullParser xpp, Element e) {
		parseElementAttributes(xpp, e);
	}

	private String pathForLocation(XmlPullParser xpp) {
		return xpp.getPositionDescription();
	}


	protected void unknownContent(XmlPullParser xpp) throws FHIRFormatError {
		if (!isAllowUnknownContent())
			throw new FHIRFormatError("Unknown Content "+xpp.getName()+" @ "+pathForLocation(xpp));
	}

	protected XhtmlNode parseXhtml(XMLEventReader xpp) throws IOException, FHIRFormatError, XMLStreamException {
		XhtmlParser prsr = new XhtmlParser();
		try {
			return prsr.parseHtmlNode(xpp);
		} catch (org.hl7.fhir.exceptions.FHIRFormatError e) {
			throw new FHIRFormatError(e.getMessage(), e);
		}
	}

	private String parseString(XmlPullParser xpp) throws XmlPullParserException, FHIRFormatError, IOException {
		StringBuilder res = new StringBuilder();
		next(xpp);
		while (xpp.getEventType() == XmlPullParser.TEXT || xpp.getEventType() == XmlPullParser.IGNORABLE_WHITESPACE || xpp.getEventType() == XmlPullParser.ENTITY_REF) {
			res.append(xpp.getText());
			next(xpp);
		}
		if (xpp.getEventType() != XmlPullParser.END_TAG)
			throw new FHIRFormatError("Bad String Structure - parsed "+res.toString()+" now found "+Integer.toString(xpp.getEventType()));
		next(xpp);
		return res.length() == 0 ? null : res.toString();
	}

	private int parseInt(XmlPullParser xpp) throws FHIRFormatError, XmlPullParserException, IOException {
		int res = -1;
		String textNode = parseString(xpp);
		res = java.lang.Integer.parseInt(textNode);
		return res;
	}

	protected DomainResource parseDomainResourceContained(XmlPullParser xpp)  throws IOException, FHIRFormatError, XmlPullParserException {
		next(xpp);
		int eventType = nextNoWhitespace(xpp);
		if (eventType == XmlPullParser.START_TAG) { 
			DomainResource dr = (DomainResource) parseResource(xpp);
			nextNoWhitespace(xpp);
			next(xpp);
			return dr;
		} else {
			unknownContent(xpp);
			return null;
		}
	} 
	protected Resource parseResourceContained(XmlPullParser xpp) throws IOException, FHIRFormatError, XmlPullParserException  {
		next(xpp);
		int eventType = nextNoWhitespace(xpp);
		if (eventType == XmlPullParser.START_TAG) { 
			Resource r = (Resource) parseResource(xpp);
			nextNoWhitespace(xpp);
			next(xpp);
			return r;
		} else {
			unknownContent(xpp);
			return null;
		}
	}

	public void compose(IXMLWriter writer, Resource resource, boolean htmlPretty)  throws IOException   {
		this.htmlPretty = htmlPretty;
		xml = writer;
		xml.setDefaultNamespace(FHIR_NS);
		composeResource(resource);
	}

	protected abstract void composeResource(Resource resource) throws IOException ;

	protected void composeElementAttributes(Element element) throws IOException {
		if (style != OutputStyle.CANONICAL)
			for (String comment : element.getFormatCommentsPre())
				xml.comment(comment, getOutputStyle() == OutputStyle.PRETTY);
		if (element.getId() != null) 
			xml.attribute("id", element.getId());
	}

	protected void composeElementClose(Base base) throws IOException {
		if (style != OutputStyle.CANONICAL)
			for (String comment : base.getFormatCommentsPost())
				xml.comment(comment, getOutputStyle() == OutputStyle.PRETTY);
	}
	protected void composeTypeAttributes(Type type) throws IOException {
		composeElementAttributes(type);
	}

	protected void composeXhtml(String name, XhtmlNode html) throws IOException {
		if (!Utilities.noString(xhtmlMessage)) {
			xml.enter(XhtmlComposer.XHTML_NS, name);
			xml.comment(xhtmlMessage, false);
			xml.exit(XhtmlComposer.XHTML_NS, name);
		} else {
			XhtmlComposer comp = new XhtmlComposer();
			// name is also found in the html and should the same
			// ? check that
			boolean oldPretty = xml.isPretty();
			xml.setPretty(htmlPretty);
			comp.setXmlOnly(true);
			if (html.getNodeType() != NodeType.Text && html.getNsDecl() == null)
				xml.namespace(XhtmlComposer.XHTML_NS, null);
			comp.compose(xml, html);
			xml.setPretty(oldPretty);
		}
	}


	abstract protected void composeString(String name, StringType value) throws IOException ;

	protected void composeString(String name, IIdType value) throws IOException  {
		composeString(name, new StringType(value.getValue()));
	}    


	protected void composeDomainResource(String name, DomainResource res) throws IOException  {
		xml.enter(FHIR_NS, name);
		composeResource(res.getResourceType().toString(), res);
		xml.exit(FHIR_NS, name);
	}

	protected abstract void composeResource(String name, Resource res) throws IOException ;

}
