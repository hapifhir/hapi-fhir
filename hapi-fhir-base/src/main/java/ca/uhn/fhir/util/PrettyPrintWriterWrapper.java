package ca.uhn.fhir.util;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import java.util.HashMap;
import java.util.Map;

import javax.xml.namespace.NamespaceContext;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.apache.commons.lang3.StringUtils;

public class PrettyPrintWriterWrapper implements XMLStreamWriter {

	private static final String INDENT_CHAR = " ";
	private static final String LINEFEED_CHAR = "\n";
	private static final String PRE = "pre";
	private int depth = 0;
	private Map<Integer, Boolean> hasChildElement = new HashMap<Integer, Boolean>();

	private int myInsidePre = 0;
	private XMLStreamWriter myTarget;
	private boolean myFirstIndent=true;

	public PrettyPrintWriterWrapper(XMLStreamWriter target) {
		myTarget = target;
	}

	@Override
	public void close() throws XMLStreamException {
		myTarget.close();
	}

	@Override
	public void flush() throws XMLStreamException {
		myTarget.flush();
	}

	@CoverageIgnore
	@Override
	public NamespaceContext getNamespaceContext() {
		return myTarget.getNamespaceContext();
	}

	@CoverageIgnore
	@Override
	public String getPrefix(String theUri) throws XMLStreamException {
		return myTarget.getPrefix(theUri);
	}

	@CoverageIgnore
	@Override
	public Object getProperty(String theName) throws IllegalArgumentException {
		return myTarget.getProperty(theName);
	}

	@CoverageIgnore
	@Override
	public void setDefaultNamespace(String theUri) throws XMLStreamException {
		myTarget.setDefaultNamespace(theUri);
	}

	@CoverageIgnore
	@Override
	public void setNamespaceContext(NamespaceContext theContext) throws XMLStreamException {
		myTarget.setNamespaceContext(theContext);
	}

	@CoverageIgnore
	@Override
	public void setPrefix(String thePrefix, String theUri) throws XMLStreamException {
		myTarget.setPrefix(thePrefix, theUri);
	}

	@Override
	public void writeAttribute(String theLocalName, String theValue) throws XMLStreamException {
		myTarget.writeAttribute(theLocalName, theValue);
	}

	@CoverageIgnore
	@Override
	public void writeAttribute(String theNamespaceURI, String theLocalName, String theValue) throws XMLStreamException {
		myTarget.writeAttribute(theNamespaceURI, theLocalName, theValue);
	}

	@CoverageIgnore
	@Override
	public void writeAttribute(String thePrefix, String theNamespaceURI, String theLocalName, String theValue) throws XMLStreamException {
		myTarget.writeAttribute(thePrefix, theNamespaceURI, theLocalName, theValue);
	}

	@CoverageIgnore
	@Override
	public void writeCData(String theData) throws XMLStreamException {
		myTarget.writeCData(theData);
	}

	@Override
	public void writeCharacters(char[] theText, int theStart, int theLen) throws XMLStreamException {
		NonPrettyPrintWriterWrapper.writeCharacters(theText, theStart, theLen, myTarget, myInsidePre);
	}

	@Override
	public void writeCharacters(String theText) throws XMLStreamException {
		if (myInsidePre > 0) {
			myTarget.writeCharacters(theText);
		} else {
			writeCharacters(theText.toCharArray(), 0, theText.length());
		}
	}

	@Override
	public void writeComment(String theData) throws XMLStreamException {
		indent();
		myTarget.writeComment(theData);
	}

	@Override
	public void writeDefaultNamespace(String theNamespaceURI) throws XMLStreamException {
		myTarget.writeDefaultNamespace(theNamespaceURI);
	}

	@CoverageIgnore
	@Override
	public void writeDTD(String theDtd) throws XMLStreamException {
		myTarget.writeDTD(theDtd);
	}

	@CoverageIgnore
	@Override
	public void writeEmptyElement(String theLocalName) throws XMLStreamException {
		indent();
		myTarget.writeEmptyElement(theLocalName);
	}

	@CoverageIgnore
	@Override
	public void writeEmptyElement(String theNamespaceURI, String theLocalName) throws XMLStreamException {
		indent();
		myTarget.writeEmptyElement(theNamespaceURI, theLocalName);
	}

	@CoverageIgnore
	@Override
	public void writeEmptyElement(String thePrefix, String theLocalName, String theNamespaceURI) throws XMLStreamException {
		indent();
		myTarget.writeEmptyElement(thePrefix, theLocalName, theNamespaceURI);
	}

	@CoverageIgnore
	@Override
	public void writeEndDocument() throws XMLStreamException {
		decrementAndIndent();
		myTarget.writeEndDocument();
	}

	@Override
	public void writeEndElement() throws XMLStreamException {
		if (myInsidePre > 0) {
			myInsidePre--;
		}
		decrementAndIndent();

		myTarget.writeEndElement();

	}

	@CoverageIgnore
	@Override
	public void writeEntityRef(String theName) throws XMLStreamException {
		myTarget.writeEntityRef(theName);
	}

	@Override
	public void writeNamespace(String thePrefix, String theNamespaceURI) throws XMLStreamException {
		myTarget.writeNamespace(thePrefix, theNamespaceURI);
	}

	@CoverageIgnore
	@Override
	public void writeProcessingInstruction(String theTarget) throws XMLStreamException {
		myTarget.writeProcessingInstruction(theTarget);
	}

	@CoverageIgnore
	@Override
	public void writeProcessingInstruction(String theTarget, String theData) throws XMLStreamException {
		myTarget.writeProcessingInstruction(theTarget, theData);
	}

	@Override
	public void writeStartDocument() throws XMLStreamException {
		myFirstIndent=true;
		myTarget.writeStartDocument();
	}

	@Override
	public void writeStartDocument(String theVersion) throws XMLStreamException {
		myFirstIndent=true;
		myTarget.writeStartDocument(theVersion);
	}

	@Override
	public void writeStartDocument(String theEncoding, String theVersion) throws XMLStreamException {
		myFirstIndent=true;
		myTarget.writeStartDocument(theEncoding, theVersion);
	}

	@Override
	public void writeStartElement(String theLocalName) throws XMLStreamException {
		indentAndAdd();
		myTarget.writeStartElement(theLocalName);
		if (PRE.equals(theLocalName) || myInsidePre > 0) {
			myInsidePre++;
		}
	}

	@Override
	public void writeStartElement(String theNamespaceURI, String theLocalName) throws XMLStreamException {
		indentAndAdd();
		myTarget.writeStartElement(theNamespaceURI, theLocalName);
		if (PRE.equals(theLocalName) || myInsidePre > 0) {
			myInsidePre++;
		}
	}

	@Override
	public void writeStartElement(String thePrefix, String theLocalName, String theNamespaceURI) throws XMLStreamException {
		indentAndAdd();
		myTarget.writeStartElement(thePrefix, theLocalName, theNamespaceURI);
		if (PRE.equals(theLocalName) || myInsidePre > 0) {
			myInsidePre++;
		}
	}

	private void decrementAndIndent() throws XMLStreamException {
		if (myInsidePre > 0) {
			return;
		}
		depth--;

		if (hasChildElement.get(depth) == true) {
			// indent for current depth
			myTarget.writeCharacters(LINEFEED_CHAR + repeat(depth, INDENT_CHAR));
		}
	}

	private void indent() throws XMLStreamException {
		if (myFirstIndent) {
			myFirstIndent = false;
			return;
		}
		myTarget.writeCharacters(LINEFEED_CHAR + repeat(depth, INDENT_CHAR));
	}

	private void indentAndAdd() throws XMLStreamException {
		if (myInsidePre > 0) {
			return;
		}
		indent();

		// update state of parent node
		if (depth > 0) {
			hasChildElement.put(depth - 1, true);
		}

		// reset state of current node
		hasChildElement.put(depth, false);

		depth++;
	}

	private String repeat(int d, String s) {
		return StringUtils.repeat(s, d * 3);
	}

}
