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
package org.hl7.fhir.utilities.xhtml;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hl7.fhir.instance.model.api.IBaseXhtml;
import org.hl7.fhir.utilities.Utilities;

import ca.uhn.fhir.model.primitive.XhtmlDt;

@ca.uhn.fhir.model.api.annotation.DatatypeDef(name="xhtml")
public class XhtmlNode implements IBaseXhtml {
  private static final long serialVersionUID = -4362547161441436492L;


  public static class Location {
    private int line;
    private int column;
    public Location(int line, int column) {
      super();
      this.line = line;
      this.column = column;
    }
    public int getLine() {
      return line;
    }
    public int getColumn() {
      return column;
    }
    @Override
    public String toString() {
      return "Line "+Integer.toString(line)+", column "+Integer.toString(column);
    }
  }

  public static final String NBSP = Character.toString((char)0xa0);
  private static final String DECL_XMLNS = " xmlns=\"http://www.w3.org/1999/xhtml\"";


  private Location location;
  private NodeType nodeType;
  private String name;
  private Map<String, String> attributes = new HashMap<String, String>();
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

  public XhtmlNode setName(String name) {
    assert name.contains(":") == false : "Name should not contain any : but was " + name;
    this.name = name;
    return this;
  }

  public Map<String, String> getAttributes() {
    return attributes;
  }

  public List<XhtmlNode> getChildNodes() {
    return childNodes;
  }

  public String getContent() {
    return content;
  }

  public XhtmlNode setContent(String content) {
    if (!(nodeType != NodeType.Text || nodeType != NodeType.Comment)) 
      throw new Error("Wrong node type");
    this.content = content;
    return this;
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

  public XhtmlNode getElement(String name) {
    for (XhtmlNode n : childNodes)
      if (n.getNodeType() == NodeType.Element && name.equals(n.getName())) 
        return n;
    return null;
  }

  public XhtmlNode getFirstElement() {
    for (XhtmlNode n : childNodes)
      if (n.getNodeType() == NodeType.Element) 
        return n;
    return null;
  }

  public String allText() {
    if (childNodes == null || childNodes.isEmpty())
      return getContent();
    
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
    attributes.put(name, value);
    return this;
  }

  public boolean hasAttribute(String name) {
    return getAttributes().containsKey(name);
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
    for (String n : attributes.keySet()) {
      dst.attributes.put(n, attributes.get(n));
    }
    for (XhtmlNode n : childNodes)
      dst.childNodes.add(n.copy());
    dst.content = content;
    return dst;
  }

  @Override
  public boolean isEmpty() {
    return (childNodes == null || childNodes.isEmpty()) && content == null;
  }

  public boolean equalsDeep(XhtmlNode other) {
    if (other == null) {
      return false;
    }

    if (!(nodeType == other.nodeType) || !compare(name, other.name) || !compare(content, other.content))
      return false;
    if (attributes.size() != other.attributes.size())
      return false;
    for (String an : attributes.keySet())
      if (!attributes.get(an).equals(other.attributes.get(an)))
        return false;
    if (childNodes.size() != other.childNodes.size())
      return false;
    for (int i = 0; i < childNodes.size(); i++) {
      if (!compareDeep(childNodes.get(i), other.childNodes.get(i)))
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

  public String getNsDecl() {
    for (String an : attributes.keySet()) {
      if (an.equals("xmlns")) {
        return attributes.get(an);
      }
    }
    return null;
  }


  @Override
  public String getValueAsString() {
    if (isEmpty()) {
      return null;
    }
    try {
      String retVal = new XhtmlComposer(XhtmlComposer.HTML).compose(this);
      retVal = XhtmlDt.preprocessXhtmlNamespaceDeclaration(retVal);
      return retVal;
    } catch (Exception e) {
      // TODO: composer shouldn't throw exception like this
      throw new RuntimeException(e);
    }
  }

  @Override
  public void setValueAsString(String theValue) throws IllegalArgumentException {
    this.attributes = null;
    this.childNodes = null;
    this.content = null;
    this.name = null;
    this.nodeType= null;
    if (theValue == null || theValue.length() == 0) {
      return;
    }

    String val = theValue.trim();

    if (!val.startsWith("<")) {
      val = "<div" + DECL_XMLNS +">" + val + "</div>";
    }
    if (val.startsWith("<?") && val.endsWith("?>")) {
      return;
    }

    val = XhtmlDt.preprocessXhtmlNamespaceDeclaration(val);

    try {
      // TODO: this is ugly
      XhtmlNode fragment = new XhtmlParser().parseFragment(val);
      this.attributes = fragment.attributes;
      this.childNodes = fragment.childNodes;
      this.content = fragment.content;
      this.name = fragment.name;
      this.nodeType= fragment.nodeType;
    } catch (Exception e) {
      // TODO: composer shouldn't throw exception like this
      throw new RuntimeException(e);
    }

  }

  public XhtmlNode getElementByIndex(int i) {
    int c = 0;
    for (XhtmlNode n : childNodes)
      if (n.getNodeType() == NodeType.Element) {
        if (c == i)
          return n;
        else
          c++;
      }
    return null;
  }

  @Override
  public String getValue() {
    return getValueAsString();
  }

  @Override
  public XhtmlNode setValue(String theValue) throws IllegalArgumentException {
    setValueAsString(theValue);
    return this;
  }

  /**
   * Returns false
   */
  public boolean hasFormatComment() {
    return false;
  }

  /**
   * NOT SUPPORTED - Throws {@link UnsupportedOperationException}
   */
  public List<String> getFormatCommentsPre() {
    throw new UnsupportedOperationException();
  }

  /**
   * NOT SUPPORTED - Throws {@link UnsupportedOperationException}
   */
  public List<String> getFormatCommentsPost() {
    throw new UnsupportedOperationException();
  }


  public Location getLocation() {
    return location;
  }


  public void setLocation(Location location) {
    this.location = location;
  }

  // xhtml easy adders -----------------------------------------------
  public XhtmlNode h1() {
    return addTag("h1");
  }
  
  public XhtmlNode h2() {
    return addTag("h2");
  }
  
  public XhtmlNode h3() {
    return addTag("h3");
  }
  
  public XhtmlNode h4() {
    return addTag("h4");
  }
  
  public XhtmlNode table(String clss) {
    XhtmlNode res = addTag("table");
    if (!Utilities.noString(clss))
      res.setAttribute("class", clss);
    return res;
  }
  
  public XhtmlNode tr() {
    return addTag("tr");
  }
  
  public XhtmlNode th() {
    return addTag("th");
  }
  
  public XhtmlNode td() {
    return addTag("td");
  }
  
  public XhtmlNode colspan(String n) {
    return setAttribute("colspan", n);
  }
  
  public XhtmlNode para() {
    return addTag("p");
  }

  public XhtmlNode pre() {
    return addTag("pre");
  }

  public void br() {
    addTag("br");
  }

  public void hr() {
    addTag("hr");
  }

  public XhtmlNode ul() {
    return addTag("ul");
  }

  public XhtmlNode li() {
    return addTag("li");
  }

  public XhtmlNode b() {
    return addTag("b");
  }

  public XhtmlNode i() {
    return addTag("i");
  }
  public XhtmlNode tx(String cnt) {
    return addText(cnt);
  }
  public XhtmlNode ah(String href) {
    return addTag("a").attribute("href", href);
  }

  public void an(String href) {
    addTag("a").attribute("name", href).tx(" ");
  }

  public XhtmlNode span(String style, String title) {
    XhtmlNode res = addTag("span");
    if (!Utilities.noString(style))
      res.attribute("style", style);
    if (!Utilities.noString(title))
      res.attribute("title", title);
    return res;
  }


  public void code(String text) {
    addTag("code").tx(text);
  }


  public XhtmlNode blockquote() {
    return addTag("blockquote");
  }


  @Override
  public String toString() {
    switch (nodeType) {
    case Document: 
    case Element:
      try {
        return new XhtmlComposer(XhtmlComposer.HTML).compose(this);
      } catch (IOException e) {
        return super.toString();
      }
    case Text:
      return this.content;
    case Comment:
      return "<!-- "+this.content+" -->";
    case DocType: 
      return "<? "+this.content+" />";
    case Instruction:
      return "<? "+this.content+" />";
    }
    return super.toString();
  }


  public XhtmlNode getNextElement(XhtmlNode c) {
    boolean f = false;
    for (XhtmlNode n : childNodes) {
      if (n == c)
        f = true;
      else if (f && n.getNodeType() == NodeType.Element) 
        return n;
    }
    return null;
  }

}
