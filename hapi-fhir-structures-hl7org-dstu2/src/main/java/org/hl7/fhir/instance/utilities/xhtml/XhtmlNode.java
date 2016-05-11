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
package org.hl7.fhir.instance.utilities.xhtml;

/*
 * #%L
 * HAPI FHIR Structures - HL7.org DSTU2
 * %%
 * Copyright (C) 2014 - 2015 University Health Network
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


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IBaseXhtml;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import ca.uhn.fhir.model.api.annotation.DatatypeDef;
import ca.uhn.fhir.util.CoverageIgnore;

@DatatypeDef(name="xhtml")
public class XhtmlNode implements IBaseXhtml {

  public static final String NBSP = Character.toString((char)0xa0);
  
  private NodeType nodeType;
  private String name;
  private Map<String, String> Attributes = new HashMap<String, String>();
  private List<XhtmlNode> childNodes = new ArrayList<XhtmlNode>();
  private String content;

  public XhtmlNode() {
    super();
  }

  public XhtmlNode(NodeType nodeType, String name) {
    super();
    this.nodeType = nodeType;
    this.name = name;
  }

  public XhtmlNode(NodeType nodeType) {
    super();
    this.nodeType = nodeType;
  }

  public NodeType getNodeType() {
    return nodeType;
  }

  public void setNodeType(NodeType nodeType) {
    this.nodeType = nodeType;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Map<String, String> getAttributes() {
    return Attributes;
  }

  public List<XhtmlNode> getChildNodes() {
	  if (childNodes == null) {
		  childNodes = new ArrayList<XhtmlNode>();
	  }
    return childNodes;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    if (!(nodeType != NodeType.Text || nodeType != NodeType.Comment)) 
      throw new Error("Wrong node type");
    this.content = content;
  }

  public XhtmlNode addTag(String name)
  {

    if (!(nodeType == NodeType.Element || nodeType == NodeType.Document)) 
      throw new Error("Wrong node type. is "+nodeType.toString());
    XhtmlNode node = new XhtmlNode(NodeType.Element);
    node.setName(name);
    childNodes.add(node);
    return node;
  }

  public XhtmlNode addTag(int index, String name)
  {

    if (!(nodeType == NodeType.Element || nodeType == NodeType.Document)) 
      throw new Error("Wrong node type. is "+nodeType.toString());
    XhtmlNode node = new XhtmlNode(NodeType.Element);
    node.setName(name);
    childNodes.add(index, node);
    return node;
  }

  public XhtmlNode addComment(String content)
  {
    if (!(nodeType == NodeType.Element || nodeType == NodeType.Document)) 
      throw new Error("Wrong node type");
    XhtmlNode node = new XhtmlNode(NodeType.Comment);
    node.setContent(content);
    childNodes.add(node);
    return node;
  }

  public XhtmlNode addDocType(String content)
  {
    if (!(nodeType == NodeType.Document)) 
      throw new Error("Wrong node type");
    XhtmlNode node = new XhtmlNode(NodeType.DocType);
    node.setContent(content);
    childNodes.add(node);
    return node;
  }

  public XhtmlNode addInstruction(String content)
  {
    if (!(nodeType == NodeType.Document)) 
      throw new Error("Wrong node type");
    XhtmlNode node = new XhtmlNode(NodeType.Instruction);
    node.setContent(content);
    childNodes.add(node);
    return node;
  }




  public XhtmlNode addText(String content)
  {
  	if (!(nodeType == NodeType.Element || nodeType == NodeType.Document)) 
  		throw new Error("Wrong node type");
  	if (content != null) {
  		XhtmlNode node = new XhtmlNode(NodeType.Text);
  		node.setContent(content);
  		childNodes.add(node);
  		return node;
    } else 
    	return null;
  }

  public XhtmlNode addText(int index, String content)
  {
    if (!(nodeType == NodeType.Element || nodeType == NodeType.Document)) 
      throw new Error("Wrong node type");
    if (content == null)
      throw new Error("Content cannot be null");
    
    XhtmlNode node = new XhtmlNode(NodeType.Text);
    node.setContent(content);
    childNodes.add(index, node);
    return node;
  }

  public boolean allChildrenAreText()
  {
    boolean res = true;
    for (XhtmlNode n : childNodes)
      res = res && n.getNodeType() == NodeType.Text;
    return res;
  }

  public XhtmlNode getElement(String name)
  {
    for (XhtmlNode n : childNodes)
      if (n.getNodeType() == NodeType.Element && name.equals(n.getName())) 
        return n;
    return null;
  }

  public XhtmlNode getFirstElement()
  {
    for (XhtmlNode n : childNodes)
      if (n.getNodeType() == NodeType.Element) 
        return n;
    return null;
  }

  public String allText() {
    StringBuilder b = new StringBuilder();
    for (XhtmlNode n : childNodes)
      if (n.getNodeType() == NodeType.Text)
        b.append(n.getContent());
      else if (n.getNodeType() == NodeType.Element)
        b.append(n.allText());
    return b.toString();
  }

  public XhtmlNode attribute(String name, String value) {
    if (!(nodeType == NodeType.Element || nodeType == NodeType.Document)) 
      throw new Error("Wrong node type");
    if (name == null)
      throw new Error("name is null");
    if (value == null)
      throw new Error("value is null");
    Attributes.put(name, value);
    return this;
  }

  public String getAttribute(String name) {
    return getAttributes().get(name);
  }

  public XhtmlNode setAttribute(String name, String value) {
    getAttributes().put(name, value);
    return this;    
  }
  
  public XhtmlNode copy() {
  	XhtmlNode dst = new XhtmlNode(nodeType);
  	dst.name = name;
  	for (String n : Attributes.keySet()) {
  		dst.Attributes.put(n, Attributes.get(n));
  	}
    for (XhtmlNode n : childNodes)
    	dst.childNodes.add(n.copy());
    dst.content = content;
    return dst;
  }

	public boolean isEmpty() {
	  return (childNodes == null || childNodes.isEmpty()) && content == null;
  }

	public boolean equalsDeep(XhtmlNode other) {
    if (other instanceof XhtmlNode)
      return false;
    XhtmlNode o = (XhtmlNode) other;
    if (!(nodeType == o.nodeType) || !compare(name, o.name) || !compare(content, o.content))
    	return false;
    if (Attributes.size() != o.Attributes.size())
    	return false;
    for (String an : Attributes.keySet())
    	if (!Attributes.get(an).equals(o.Attributes.get(an)))
    		return false;
    if (childNodes.size() != o.childNodes.size())
    	return false;
		for (int i = 0; i < childNodes.size(); i++) {
			if (!compareDeep(childNodes.get(i), o.childNodes.get(i)))
				return false;
		}
		return true;
  }

	private boolean compare(String s1, String s2) {
		if (s1 == null && s2 == null)
			return true;
		if (s1 == null || s2 == null)
			return false;
		return s1.equals(s2);
  }

	private static boolean compareDeep(XhtmlNode e1, XhtmlNode e2) {
		if (e1 == null && e2 == null)
			return true;
		if (e1 == null || e2 == null)
			return false;
		return e1.equalsDeep(e2);
  }

	public String getValueAsString() {
		if (isEmpty()) {
			return null;
		}
		try {
			return new XhtmlComposer().compose(this);
		} catch (Exception e) {
			// TODO: composer shouldn't throw exception like this
			throw new RuntimeException(e);
		}
	}

	@Override
	public void setValueAsString(String theValue) throws IllegalArgumentException {
		this.Attributes = null;
		this.childNodes = null;
		this.content = null;
		this.name = null;
		this.nodeType= null;
		if (theValue == null) {
			return;
		}
		
		String val = theValue.trim();
		if (StringUtils.isBlank(theValue)) {
			return;
		}
		
		if (!val.startsWith("<")) {
			val = "<div>" + val + "</div>";
		}
		if (val.startsWith("<?") && val.endsWith("?>")) {
			return;
		}

		try {
			// TODO: this is ugly
			XhtmlNode fragment = new XhtmlParser().parseFragment(val);
			this.Attributes = fragment.Attributes;
			this.childNodes = fragment.childNodes;
			this.content = fragment.content;
			this.name = fragment.name;
			this.nodeType= fragment.nodeType;
		} catch (Exception e) {
			// TODO: composer shouldn't throw exception like this
			throw new RuntimeException(e);
		}
		
	}

	@Override
	public String getValue() {
		return getValueAsString();
	}

	@Override
	public IPrimitiveType<String> setValue(String theValue) throws IllegalArgumentException {
		setValueAsString(theValue);
		return this;
	}
	
	
	/**
	 * Returns false
	 */
	@Override
	@CoverageIgnore
	public boolean hasFormatComment() {
	  return false;
	}

	/**
	 * NOT SUPPORTED - Throws {@link UnsupportedOperationException}
	 */
	@Override
	@CoverageIgnore
	public List<String> getFormatCommentsPre() {
	  throw new UnsupportedOperationException();
	}

	/**
	 * NOT SUPPORTED - Throws {@link UnsupportedOperationException}
	 */
	@Override
	@CoverageIgnore
	public List<String> getFormatCommentsPost() {
	  throw new UnsupportedOperationException();
	}

}
