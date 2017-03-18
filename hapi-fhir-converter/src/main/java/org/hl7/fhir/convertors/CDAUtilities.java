package org.hl7.fhir.convertors;

/*
 * #%L
 * HAPI FHIR - Converter
 * %%
 * Copyright (C) 2014 - 2017 University Health Network
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



/*
  Copyright (c) 2011+, HL7, Inc.
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

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.hl7.fhir.utilities.CommaSeparatedStringBuilder;
import org.hl7.fhir.utilities.xml.XMLUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class CDAUtilities {

	private Document doc;

	public CDAUtilities(InputStream stream) throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		DocumentBuilder builder = factory.newDocumentBuilder();

		doc = builder.parse(stream);
		basicChecks();
	}

	private void basicChecks() throws Exception {
		Element e = doc.getDocumentElement(); 
		rule(e.getNamespaceURI().equals("urn:hl7-org:v3"), "CDA namespace must be ");
		rule(e.getNodeName().equals("ClinicalDocument"), "CDA root name must be ClinicalDocument");

	}

	private void rule(boolean test, String message) throws Exception {
		if (!test) 
			throw new Exception(message);

	}

	public Element getElement() {
		return doc.getDocumentElement();
	}

	public void checkTemplateId(Element e, String templateId) throws Exception {
		rule(hasTemplateId(e, templateId), "Template Id '"+templateId+"' not found");
		
	}

	public Element getChild(Element e, String[] names) throws Exception {
		for (String n : names) {
			if (e == null)
				return null;
			e = getChild(e, n);
		}
		return e;
	}

	public Element getChild(Element element, String name) throws Exception {
		if (element == null)
			return null;
		
		Element e = null;
		Node n = element.getFirstChild();
		while (n != null) {
			if (n.getNodeType() == Node.ELEMENT_NODE && n.getNodeName().equals(name)) {
				if (e == null) {
					e = (Element) n;
				} else {
					throw new Exception("multiple matches found for "+name);
				}
			}
			n = n.getNextSibling();
		}
		return e;
	}

  public Element getChildByAttribute(Element element, String name, String attrname, String value) throws Exception {
		if (element == null)
			return null;
		
		Element e = null;
		Node n = element.getFirstChild();
		while (n != null) {
			if (n.getNodeType() == Node.ELEMENT_NODE && n.getNodeName().equals(name) && value.equals(((Element) n).getAttribute(attrname))) {
				if (e == null) {
					e = (Element) n;
				} else {
					throw new Exception("multiple matches found for "+name);
				}
			}
			n = n.getNextSibling();
		}
		return e;
  }


	public List<Element> getChildren(Element element, String name) {
		List<Element> l = new ArrayList<Element>();
		if (element != null) {
			Node n = element.getFirstChild();
			while (n != null) {
				if (n.getNodeType() == Node.ELEMENT_NODE && n.getNodeName().equals(name)) {
					l.add((Element) n);
				}
				n = n.getNextSibling();
			}
		}
		return l;
  }

	public Element getDescendent(Element element, String path) throws Exception {
		String[] p = path.split("\\/");
		return getDescendent(element, p);
  }

	public Element getDescendent(Element e, String[] path) throws Exception {
		for (String n : path) {
			if (e == null)
				return e;
			e = getChild(e, n);
		}
		return e;
  }

	public boolean hasTemplateId(Element e, String tid) {
		if (e == null)
			return false;
		boolean found = false;
		Node n = e.getFirstChild();
		while (n != null && !found) {
			if (n.getNodeType() == Node.ELEMENT_NODE && n.getNodeName().equals("templateId") && tid.equals(((Element) n).getAttribute("root")))
				found = true;
			n = n.getNextSibling();
		}
		return found;
  }

	public String getStatus(Element act) throws Exception {
		if (act == null)
			return null;
	  Element sc = getChild(act, "statusCode");
	  if (sc == null)
	    return null;
	  else
	  	return sc.getAttribute("code");
  }

	public String getSeverity(Element observation) throws Exception {
	  for (Element e : getChildren(observation,  "entryRelationship")) {
	  	Element child = getChild(e, "observation");
	  	if (hasTemplateId(child, "2.16.840.1.113883.10.20.22.4.8"))
	  		return getChild(child,  "value").getAttribute("code");
	  }
	  return null;
  }

	public String showTemplateIds(Element element) {
	  List<Element> list = getChildren(element, "templateId");
	  CommaSeparatedStringBuilder b = new CommaSeparatedStringBuilder();
	  for (Element e : list) {
	  	if (e.hasAttribute("extension")) 
	  		b.append(e.getAttribute("root")+"::"+e.getAttribute("extension"));
	  	else
	  		b.append(e.getAttribute("root"));	  	
	  }
	  return b.toString();
  }

	public Element getlastChild(Element e) {
	  Node n = e.getLastChild();
	  while (n != null && n.getNodeType() != Node.ELEMENT_NODE)
	  	n = n.getPreviousSibling();
	  return n == null ? null : (Element) n;
  }

	/**
	 * This method looks up an object by it's id, and only returns it if has a child by the given name
	 * (resolving identifier based cross references)
	 * 
	 * @param id
	 * @param childName
	 * @return
	 * @throws Exception 
	 */
	public Element getById(Element id, String childName) throws Exception {
	  return getById(doc.getDocumentElement(), id, childName);
  }

	private Element getById(Element e, Element id, String childName) throws Exception {
	  Element c = XMLUtil.getFirstChild(e);
	  while (c != null) {
	  	Element i = getChild(c, "id");
	  	if (i != null && matchesAsId(i, id) && getChild(c, childName) != null) 
	  		return c;
	  	Element m = getById(c, id, childName);
	  	if (m != null)
	  		return m;
	  	c = XMLUtil.getNextSibling(c);
	  }
	  return null;
  }

	private boolean matchesAsId(Element i1, Element i2) {
		String r1 = i1.getAttribute("root"); 
		String r2 = i2.getAttribute("root"); 
		String e1 = i1.getAttribute("extension"); 
		String e2 = i2.getAttribute("extension");
	  return (r1 != null && r1.equals(r2)) && ((e1 == null && e2 == null) || (e1 != null && e1.equals(e2)));
  }

	public Element getByXmlId(String id) {
	  return getByXmlId(doc.getDocumentElement(), id);
  }

	private Element getByXmlId(Element e, String value) {
	  Element c = XMLUtil.getFirstChild(e);
	  while (c != null) {
	  	String id = c.getAttribute("ID");
	  	if (id != null && id.equals(value)) 
	  		return c;
	  	Element m = getByXmlId(c, value);
	  	if (m != null)
	  		return m;
	  	c = XMLUtil.getNextSibling(c);
	  }
	  return null;
  }

}
