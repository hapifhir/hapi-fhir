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

import javax.xml.namespace.NamespaceContext;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

public class NonPrettyPrintWriterWrapper implements XMLStreamWriter {

	private static final String PRE = "pre";
	private XMLStreamWriter myTarget;
	private int myInsidePre = 0;

	public NonPrettyPrintWriterWrapper(XMLStreamWriter target) {
		myTarget = target;
	}

	@Override
	public void flush() throws XMLStreamException {
		myTarget.flush();
	}

	@Override
	public void close() throws XMLStreamException {
		myTarget.close();
	}

	@Override
	@CoverageIgnore
	public String getPrefix(String theUri) throws XMLStreamException {
		return myTarget.getPrefix(theUri);
	}

	@Override
	@CoverageIgnore
	public void setPrefix(String thePrefix, String theUri) throws XMLStreamException {
		myTarget.setPrefix(thePrefix, theUri);
	}

	@Override
	@CoverageIgnore
	public void setDefaultNamespace(String theUri) throws XMLStreamException {
		myTarget.setDefaultNamespace(theUri);
	}

	@Override
	@CoverageIgnore
	public void setNamespaceContext(NamespaceContext theContext) throws XMLStreamException {
		myTarget.setNamespaceContext(theContext);
	}

	@Override
	@CoverageIgnore
	public NamespaceContext getNamespaceContext() {
		return myTarget.getNamespaceContext();
	}

	@Override
	public void writeStartElement(String theLocalName) throws XMLStreamException {
		if (PRE.equals(theLocalName) || myInsidePre > 0) {
			myInsidePre++;
		}
		myTarget.writeStartElement(theLocalName);
	}

	@Override
	public void writeStartElement(String theNamespaceURI, String theLocalName) throws XMLStreamException {
		if (PRE.equals(theLocalName) || myInsidePre > 0) {
			myInsidePre++;
		}
		myTarget.writeStartElement(theNamespaceURI, theLocalName);
	}

	@Override
	public void writeStartElement(String thePrefix, String theLocalName, String theNamespaceURI) throws XMLStreamException {
		if (PRE.equals(theLocalName) || myInsidePre > 0) {
			myInsidePre++;
		}
		myTarget.writeStartElement(thePrefix, theLocalName, theNamespaceURI);
	}

	@Override
	@CoverageIgnore
	public void writeEmptyElement(String theNamespaceURI, String theLocalName) throws XMLStreamException {
		myTarget.writeEmptyElement(theNamespaceURI, theLocalName);
	}

	@Override
	@CoverageIgnore
	public void writeEmptyElement(String thePrefix, String theLocalName, String theNamespaceURI) throws XMLStreamException {
		myTarget.writeEmptyElement(thePrefix, theLocalName, theNamespaceURI);
	}

	@Override
	@CoverageIgnore
	public void writeEmptyElement(String theLocalName) throws XMLStreamException {
		myTarget.writeEmptyElement(theLocalName);
	}

	@Override
	public void writeEndElement() throws XMLStreamException {
		if (myInsidePre > 0) {
			myInsidePre--;
		}
		myTarget.writeEndElement();
	}

	@Override
	public void writeEndDocument() throws XMLStreamException {
		myTarget.writeEndDocument();
	}

	@Override
	public void writeAttribute(String theLocalName, String theValue) throws XMLStreamException {
		myTarget.writeAttribute(theLocalName, theValue);
	}

	@Override
	@CoverageIgnore
	public void writeAttribute(String thePrefix, String theNamespaceURI, String theLocalName, String theValue) throws XMLStreamException {
		myTarget.writeAttribute(thePrefix, theNamespaceURI, theLocalName, theValue);
	}

	@Override
	@CoverageIgnore
	public void writeAttribute(String theNamespaceURI, String theLocalName, String theValue) throws XMLStreamException {
		myTarget.writeAttribute(theNamespaceURI, theLocalName, theValue);
	}

	@Override
	public void writeNamespace(String thePrefix, String theNamespaceURI) throws XMLStreamException {
		myTarget.writeNamespace(thePrefix, theNamespaceURI);
	}

	@Override
	public void writeDefaultNamespace(String theNamespaceURI) throws XMLStreamException {
		myTarget.writeDefaultNamespace(theNamespaceURI);
	}

	@Override
	public void writeComment(String theData) throws XMLStreamException {
		myTarget.writeComment(theData);
	}

	@Override
	@CoverageIgnore
	public void writeProcessingInstruction(String theTarget) throws XMLStreamException {
		myTarget.writeProcessingInstruction(theTarget);
	}

	@Override
	@CoverageIgnore
	public void writeProcessingInstruction(String theTarget, String theData) throws XMLStreamException {
		myTarget.writeProcessingInstruction(theTarget, theData);
	}

	@Override
	@CoverageIgnore
	public void writeCData(String theData) throws XMLStreamException {
		myTarget.writeCData(theData);
	}

	@Override
	@CoverageIgnore
	public void writeDTD(String theDtd) throws XMLStreamException {
		myTarget.writeDTD(theDtd);
	}

	@Override
	@CoverageIgnore
	public void writeEntityRef(String theName) throws XMLStreamException {
		myTarget.writeEntityRef(theName);
	}

	@Override
	@CoverageIgnore
	public void writeStartDocument() throws XMLStreamException {
		myTarget.writeStartDocument();
	}

	@Override
	@CoverageIgnore
	public void writeStartDocument(String theVersion) throws XMLStreamException {
		myTarget.writeStartDocument(theVersion);
	}

	@Override
	public void writeStartDocument(String theEncoding, String theVersion) throws XMLStreamException {
		myTarget.writeStartDocument(theEncoding, theVersion);
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
	public void writeCharacters(char[] theText, int theStart, int theLen) throws XMLStreamException {
		writeCharacters(theText, theStart, theLen, myTarget, myInsidePre);
	}

	static void writeCharacters(char[] theText, int theStart, int theLen, XMLStreamWriter target, int insidePre) throws XMLStreamException {
		if (theLen > 0) {
			if (insidePre > 0) {
				target.writeCharacters(theText, theStart, theLen);
			} else {
				int initialEnd = theStart + (theLen - 1);
				int start = theStart;
				int end = initialEnd;
				while (Character.isWhitespace(theText[start]) && start < end) {
					start++;
				}
				while (Character.isWhitespace(theText[end]) && end > start) {
					end--;
				}
				if (start == end) {
					if (Character.isWhitespace(theText[start])) {
						target.writeCharacters(" ");
						return;
					}
				}
				if (start > theStart) {
					target.writeCharacters(" ");
				}
				target.writeCharacters(theText, start, (end - start) + 1);
				if (end < initialEnd) {
					target.writeCharacters(" ");
				}

			}
		}
	}

	@Override
	@CoverageIgnore
	public Object getProperty(String theName) throws IllegalArgumentException {
		return myTarget.getProperty(theName);
	}

}
