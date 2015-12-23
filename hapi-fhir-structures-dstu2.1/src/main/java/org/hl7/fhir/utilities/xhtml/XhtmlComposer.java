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
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.utilities.Utilities;
import org.hl7.fhir.utilities.xml.IXMLWriter;
import org.w3c.dom.Element;

public class XhtmlComposer {

  public static final String XHTML_NS = "http://www.w3.org/1999/xhtml";
  private boolean pretty;
  private boolean xmlOnly;
  
  
  public boolean isPretty() {
    return pretty;
  }

  public void setPretty(boolean pretty) {
    this.pretty = pretty;
  }

  public boolean isXmlOnly() {
    return xmlOnly;
  }

  public XhtmlComposer setXmlOnly(boolean xmlOnly) {
    this.xmlOnly = xmlOnly;
    return this;
  }


  private Writer dst;

  public String compose(XhtmlDocument doc) throws IOException, FHIRException  {
    StringWriter sdst = new StringWriter();
    dst = sdst;
    composeDoc(doc);
    return sdst.toString();
  }

  public String compose(XhtmlNode node) throws IOException  {
    StringWriter sdst = new StringWriter();
    dst = sdst;
    writeNode("", node);
    return sdst.toString();
  }

  public void compose(OutputStream stream, XhtmlDocument doc) throws IOException, FHIRException  {
    byte[] bom = new byte[] { (byte)0xEF, (byte)0xBB, (byte)0xBF };
    stream.write(bom);
    dst = new OutputStreamWriter(stream, "UTF-8");
    composeDoc(doc);
    dst.flush();
  }

  private void composeDoc(XhtmlDocument doc) throws IOException, FHIRException  {
    // headers....
//    dst.append("<html>" + (isPretty() ? "\r\n" : ""));
    for (XhtmlNode c : doc.getChildNodes())
      writeNode("  ", c);
//    dst.append("</html>" + (isPretty() ? "\r\n" : ""));
  }

  private void writeNode(String indent, XhtmlNode node) throws IOException  {
    if (node.getNodeType() == NodeType.Comment)
      writeComment(indent, node);
    else if (node.getNodeType() == NodeType.DocType)
      writeDocType(node);
    else if (node.getNodeType() == NodeType.Instruction)
      writeInstruction(node);
    else if (node.getNodeType() == NodeType.Element)
      writeElement(indent, node);
    else if (node.getNodeType() == NodeType.Text)
      writeText(node);
    else
      throw new Error("Unknown node type: "+node.getNodeType().toString());
  }

  private void writeText(XhtmlNode node) throws IOException  {
    for (char c : node.getContent().toCharArray())
    {
      if (c == '&')
        dst.append("&amp;");
      else if (c == '<')
        dst.append("&lt;");
      else if (c == '>')
        dst.append("&gt;");
      else if (c == '"')
        dst.append("&quot;");
      else if (!xmlOnly) {
        dst.append(c);
      } else {
        if (c == XhtmlNode.NBSP.charAt(0))
          dst.append("&nbsp;");
        else if (c == (char) 0xA7)
          dst.append("&sect;");
        else if (c == (char) 169)
          dst.append("&copy;");
        else if (c == (char) 8482)
          dst.append("&trade;");
        else if (c == (char) 956)
          dst.append("&mu;");
        else if (c == (char) 174)
          dst.append("&reg;");
        else 
          dst.append(c);
      }
    }
  }

  private void writeComment(String indent, XhtmlNode node) throws IOException {
    dst.append(indent + "<!-- " + node.getContent().trim() + " -->" + (isPretty() ? "\r\n" : ""));
}

  private void writeDocType(XhtmlNode node) throws IOException {
    dst.append("<!" + node.getContent() + ">\r\n");
}

  private void writeInstruction(XhtmlNode node) throws IOException {
    dst.append("<?" + node.getContent() + "?>\r\n");
}

  private String escapeHtml(String s)  {
    if (s == null || s.equals(""))
      return null;
    StringBuilder b = new StringBuilder();
    for (char c : s.toCharArray())
      if (c == '<')
        b.append("&lt;");
      else if (c == '>')
        b.append("&gt;");
      else if (c == '"')
        b.append("&quot;");
      else if (c == '&')
        b.append("&amp;");
      else
        b.append(c);
    return b.toString();
  }
  
  private String attributes(XhtmlNode node) {
    StringBuilder s = new StringBuilder();
    for (String n : node.getAttributes().keySet())
      s.append(" " + n + "=\"" + escapeHtml(node.getAttributes().get(n)) + "\"");
    return s.toString();
  }
  
  private void writeElement(String indent, XhtmlNode node) throws IOException  {
    if (!pretty)
      indent = "";
    
    if (node.getChildNodes().size() == 0)
      dst.append(indent + "<" + node.getName() + attributes(node) + "/>" + (isPretty() ? "\r\n" : ""));
    else {
    boolean act = node.allChildrenAreText();
    if (act || !pretty)
      dst.append(indent + "<" + node.getName() + attributes(node)+">");
    else
      dst.append(indent + "<" + node.getName() + attributes(node) + ">\r\n");
    if (node.getName() == "head" && node.getElement("meta") == null)
      dst.append(indent + "  <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/>" + (isPretty() ? "\r\n" : ""));


    for (XhtmlNode c : node.getChildNodes())
      writeNode(indent + "  ", c);
    if (act)
      dst.append("</" + node.getName() + ">" + (isPretty() ? "\r\n" : ""));
    else if (node.getChildNodes().get(node.getChildNodes().size() - 1).getNodeType() == NodeType.Text)
      dst.append((isPretty() ? "\r\n"+ indent : "")  + "</" + node.getName() + ">" + (isPretty() ? "\r\n" : ""));
    else
      dst.append(indent + "</" + node.getName() + ">" + (isPretty() ? "\r\n" : ""));
    }
  }

  public void compose(IXMLWriter xml, XhtmlNode node) throws IOException  {
    if (node.getNodeType() == NodeType.Comment)
      xml.comment(node.getContent(), isPretty());
    else if (node.getNodeType() == NodeType.Element)
      composeElement(xml, node);
    else if (node.getNodeType() == NodeType.Text)
      xml.text(node.getContent());
    else
      throw new Error("Unhandled node type: "+node.getNodeType().toString());
  }

  private void composeElement(IXMLWriter xml, XhtmlNode node) throws IOException  {
    for (String n : node.getAttributes().keySet())
      xml.attribute(n, node.getAttributes().get(n));
    xml.enter(XHTML_NS, node.getName());
    for (XhtmlNode n : node.getChildNodes())
      compose(xml, n);
    xml.exit(XHTML_NS, node.getName());
  }

  public String composePlainText(XhtmlNode x) {
    StringBuilder b = new StringBuilder();
    composePlainText(x, b, false);
    return b.toString().trim();
  }

  private boolean composePlainText(XhtmlNode x, StringBuilder b, boolean lastWS) {
    if (x.getNodeType() == NodeType.Text) {
      String s = x.getContent();
      if (!lastWS & (s.startsWith(" ") || s.startsWith("\r") || s.startsWith("\n") || s.endsWith("\t"))) {
        b.append(" ");
        lastWS = true;
      }
      String st = s.trim().replace("\r", " ").replace("\n", " ").replace("\t", " ");
      while (st.contains("  "))
        st = st.replace("  ", " ");
      if (!Utilities.noString(st)) {
        b.append(st);
        lastWS = false;
        if (!lastWS & (s.endsWith(" ") || s.endsWith("\r") || s.endsWith("\n") || s.endsWith("\t"))) {
          b.append(" ");
          lastWS = true;
        }
      }
      return lastWS;
    } else if (x.getNodeType() == NodeType.Element) {
      if (x.getName().equals("li")) {
        b.append("* ");
        lastWS = true;
      }
      
      for (XhtmlNode n : x.getChildNodes()) {
        lastWS = composePlainText(n, b, lastWS);
      }
      if (x.getName().equals("p")) {
        b.append("\r\n\r\n");
        lastWS = true;
      }
      if (x.getName().equals("br") || x.getName().equals("li")) {
        b.append("\r\n");
        lastWS = true;
      }
      return lastWS;
    } else
      return lastWS;
  }

  public void compose(Element div, XhtmlNode x) {
    for (XhtmlNode child : x.getChildNodes()) {
      appendChild(div, child);
    }
  }

  private void appendChild(Element e, XhtmlNode node) {
    if (node.getNodeType() == NodeType.Comment)
      e.appendChild(e.getOwnerDocument().createComment(node.getContent()));
    else if (node.getNodeType() == NodeType.DocType)
      throw new Error("not done yet");
    else if (node.getNodeType() == NodeType.Instruction)
      e.appendChild(e.getOwnerDocument().createProcessingInstruction("", node.getContent()));
    else if (node.getNodeType() == NodeType.Text)
      e.appendChild(e.getOwnerDocument().createTextNode(node.getContent()));
    else if (node.getNodeType() == NodeType.Element) {
      Element child = e.getOwnerDocument().createElementNS(XHTML_NS, node.getName());
      e.appendChild(child);
      for (XhtmlNode c : node.getChildNodes()) {
        appendChild(child, c);
      }
    } else
      throw new Error("Unknown node type: "+node.getNodeType().toString());
  }
  
}
