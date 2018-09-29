package org.hl7.fhir.utilities.xhtml;

import java.io.IOException;

import javax.print.Doc;

import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.utilities.xml.IXMLWriter;
import org.hl7.fhir.utilities.xml.XMLUtil;
import org.hl7.fhir.utilities.xml.IXMLWriter;
import org.hl7.fhir.utilities.Utilities;
import org.hl7.fhir.utilities.xhtml.NodeType;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class CDANarrativeFormat {

  /** 
   * for a CDA narrative, return the matching XHTML. 
   * 
   * For further information, see http://wiki.hl7.org/index.php?title=CDA_Narrative_to_html_mapping
   * 
   * @param ed
   * @return
   * @throws FHIRException
   */
  public XhtmlNode convert(Element ed) throws FHIRException {
    XhtmlNode div = new XhtmlNode(NodeType.Element, "div");
    processAttributes(ed, div, "ID", "language", "styleCode");
    processChildren(ed, div);
    return div;
  }

  private void processChildren(Element ed, XhtmlNode x) throws FHIRException {
    for (Node n : XMLUtil.children(ed)) 
      processChildNode(n, x);
  }
  
  private void processChildNode(Node n, XhtmlNode xn) throws FHIRException {
    switch (n.getNodeType()) {
    case Node.ATTRIBUTE_NODE: 
    case Node.CDATA_SECTION_NODE:
    case Node.DOCUMENT_FRAGMENT_NODE: 
    case Node.DOCUMENT_TYPE_NODE: 
    case Node.DOCUMENT_NODE: 
    case Node.ENTITY_NODE: 
    case Node.PROCESSING_INSTRUCTION_NODE:
    case Node.NOTATION_NODE:
      return;
    case Node.ENTITY_REFERENCE_NODE: 
      throw new Error("Not handled yet");
    case Node.COMMENT_NODE: 
      xn.addComment(n.getTextContent());
      return;
    case Node.TEXT_NODE: 
      if (!Utilities.isWhitespace(n.getTextContent()))
        xn.addText(n.getTextContent());
      return;
    case Node.ELEMENT_NODE:
      Element e = (Element) n;
      if (n.getNodeName().equals("br"))
        processBreak(e, xn);
      else if (n.getNodeName().equals("caption"))
        processCaption(e, xn);
      else if (n.getNodeName().equals("col"))
        processCol(e, xn);
      else if (n.getNodeName().equals("colgroup"))
        processColGroup(e, xn);
      else if (n.getNodeName().equals("content"))
        processContent(e, xn);
      else if (n.getNodeName().equals("footnote"))
        processFootNote(e, xn);
      else if (n.getNodeName().equals("footnoteRef"))
        processFootNodeRef(e, xn);
      else if (n.getNodeName().equals("item"))
        processItem(e, xn);
      else if (n.getNodeName().equals("linkHtml"))
        processlinkHtml(e, xn);
      else if (n.getNodeName().equals("list"))
        processList(e, xn);
      else if (n.getNodeName().equals("paragraph"))
        processParagraph(e, xn);
      else if (n.getNodeName().equals("renderMultiMedia"))
        processRenderMultiMedia(e, xn);
      else if (n.getNodeName().equals("sub"))
        processSub(e, xn);
      else if (n.getNodeName().equals("sup"))
        processSup(e, xn);
      else if (n.getNodeName().equals("table"))
        processTable(e, xn);
      else if (n.getNodeName().equals("tbody"))
        processTBody(e, xn);
      else if (n.getNodeName().equals("td"))
        processTd(e, xn);
      else if (n.getNodeName().equals("tfoot"))
        processTFoot(e, xn);
      else if (n.getNodeName().equals("th"))
        processTh(e, xn);
      else if (n.getNodeName().equals("thead"))
        processTHead(e, xn);
      else if (n.getNodeName().equals("tr"))
        processTr(e, xn);
      else
        throw new FHIRException("Unknown element "+n.getNodeName());
    }
  }

  private void processBreak(Element e, XhtmlNode xn) {
    xn.addTag("br");
  }

  private void processCaption(Element e, XhtmlNode xn) throws FHIRException {
    XhtmlNode xc = xn.addTag("h2");
    processAttributes(e, xc, "ID", "language", "styleCode");
    processChildren(e, xc);
  }

  private void processCol(Element e, XhtmlNode xn) throws FHIRException {
    XhtmlNode xc = xn.addTag("col");
    processAttributes(e, xc, "ID", "language", "styleCode", "span", "width", "align", "char", "charoff", "valign");
    processChildren(e, xc);
  }

  private void processColGroup(Element e, XhtmlNode xn) throws FHIRException {
    XhtmlNode xc = xn.addTag("colgroup");
    processAttributes(e, xc, "ID", "language", "styleCode", "span", "width", "align", "char", "charoff", "valign");
    processChildren(e, xc);
  }

  private void processContent(Element e, XhtmlNode xn) throws FHIRException {
    XhtmlNode xc = xn.addTag("span");
    processAttributes(e, xc, "ID", "language", "styleCode");
    // todo: do something with revised..., "revised"
    processChildren(e, xc);
  }

  private void processFootNote(Element e, XhtmlNode xn) {
    throw new Error("element "+e.getNodeName()+" not handled yet");
  }

  private void processFootNodeRef(Element e, XhtmlNode xn) {
    throw new Error("element "+e.getNodeName()+" not handled yet");
  }

  private void processItem(Element e, XhtmlNode xn) throws FHIRException {
    XhtmlNode xc = xn.addTag("li");
    processAttributes(e, xc, "ID", "language", "styleCode");
    processChildren(e, xc);
  }

  private void processlinkHtml(Element e, XhtmlNode xn) throws FHIRException {
    XhtmlNode xc = xn.addTag("a");
    processAttributes(e, xc, "name", "href", "rel", "rev", "title", "ID", "language", "styleCode");
    processChildren(e, xc);
  }

  private void processList(Element e, XhtmlNode xn) throws FHIRException {
    String lt = e.getAttribute("listType");
    XhtmlNode xc = xn.addTag("ordered".equals(lt) ? "ol" : "ul");
    processAttributes(e, xc, "ID", "language", "styleCode");
    processChildren(e, xc);
  }

  private void processParagraph(Element e, XhtmlNode xn) throws FHIRException {
    XhtmlNode xc = xn.addTag("p");
    processAttributes(e, xc, "ID", "language", "styleCode");
    processChildren(e, xc);
  }

  private void processRenderMultiMedia(Element e, XhtmlNode xn) throws FHIRException {
    XhtmlNode xc = xn.addTag("img");
    String v = e.getAttribute("referencedObject");
    xn.attribute("src", v);
    processAttributes(e, xc, "ID", "language", "styleCode");
    processChildren(e, xc);
  }

  private void processSub(Element e, XhtmlNode xn) throws FHIRException {
    XhtmlNode xc = xn.addTag("sub");
    processChildren(e, xc);
  }

  private void processSup(Element e, XhtmlNode xn) throws FHIRException {
    XhtmlNode xc = xn.addTag("sup");
    processChildren(e, xc);
  }

  private void processTable(Element e, XhtmlNode xn) throws FHIRException {
    XhtmlNode xc = xn.addTag("table");
    processAttributes(e, xc, "ID", "language", "styleCode", "summary", "width", "border", "frame", "rules", "cellspacing", "cellpadding");
    processChildren(e, xc);
  }

  private void processTBody(Element e, XhtmlNode xn) throws FHIRException {
    XhtmlNode xc = xn.addTag("tbody");
    processAttributes(e, xc, "ID", "language", "styleCode", "align", "char", "charoff", "valign");
    processChildren(e, xc);
  }

  private void processTd(Element e, XhtmlNode xn) throws FHIRException {
    XhtmlNode xc = xn.addTag("td");
    processAttributes(e, xc, "ID", "language", "styleCode", "abbr", "axis", "headers", "scope", "rowspan", "colspan", "align", "char", "charoff", "valign");
    processChildren(e, xc);
  }

  private void processTFoot(Element e, XhtmlNode xn) {
    throw new Error("element "+e.getNodeName()+" not handled yet");
  }

  private void processTh(Element e, XhtmlNode xn) throws FHIRException {
    XhtmlNode xc = xn.addTag("th");
    processAttributes(e, xc, "ID", "language", "styleCode", "abbr", "axis", "headers", "scope", "rowspan", "colspan", "align", "char", "charoff", "valign");
    processChildren(e, xc);
  }

  private void processTHead(Element e, XhtmlNode xn) throws FHIRException {
    XhtmlNode xc = xn.addTag("thead");
    processAttributes(e, xc, "ID", "language", "styleCode", "align", "char", "charoff", "valign");
    processChildren(e, xc);
  }

  private void processTr(Element e, XhtmlNode xn) throws FHIRException {
    XhtmlNode xc = xn.addTag("tr");
    processAttributes(e, xc, "ID", "language", "styleCode", "align", "char", "charoff", "valign");
    processChildren(e, xc);
  }

  private void processAttributes(Element element, XhtmlNode xn, String... names) {
    for (String n : names) {
      if (element.hasAttribute(n)) {
        String v = element.getAttribute(n);
        if (n.equals("ID"))
          xn.attribute("id", v);
        else
          xn.attribute(n, v);
      }
    }
  }

  /**
   * For XHTML return the matching CDA narrative. This is only guaranteed to work for XML produced from CDA, but will try whatever
   * @param node
   * @return
   * @throws IOException 
   * @throws FHIRException 
   */
  public void convert(IXMLWriter xml, XhtmlNode div) throws IOException, FHIRException {
    processAttributes(div, xml, "ID", "language", "styleCode");
    xml.enter("text");
    processChildren(xml, div);
    xml.exit("text");
  }

  private void processChildren(IXMLWriter xml, XhtmlNode x) throws IOException, FHIRException {
    for (XhtmlNode n : x.getChildNodes()) 
      processChildNode(xml, n);
  }
  
  private void processChildNode(IXMLWriter xml, XhtmlNode n) throws IOException, FHIRException {
    switch (n.getNodeType()) {
    case DocType: 
    case Document: 
    case Instruction: 
      return;
    case Comment: 
      xml.comment(n.getContent(), true);
      return;
    case Text: 
      xml.text(n.getContent());
      return;
    case Element:
      if (n.getName().equals("br"))
        processBreak(xml, n);
      else if (n.getName().equals("h2"))
        processCaption(xml, n);
      else if (n.getName().equals("col"))
        processCol(xml, n);
      else if (n.getName().equals("colgroup"))
        processColGroup(xml, n);
      else if (n.getName().equals("span"))
        processContent(xml, n);
      else if (n.getName().equals("footnote"))
        processFootNote(xml, n);
      else if (n.getName().equals("footnoteRef"))
        processFootNodeRef(xml, n);
      else if (n.getName().equals("li"))
        processItem(xml, n);
      else if (n.getName().equals("linkHtml"))
        processlinkHtml(xml, n);
      else if (n.getName().equals("ul") || n.getName().equals("ol"))
        processList(xml, n);
      else if (n.getName().equals("p"))
        processParagraph(xml, n);
      else if (n.getName().equals("img"))
        processRenderMultiMedia(xml, n);
      else if (n.getName().equals("sub"))
        processSub(xml, n);
      else if (n.getName().equals("sup"))
        processSup(xml, n);
      else if (n.getName().equals("table"))
        processTable(xml, n);
      else if (n.getName().equals("tbody"))
        processTBody(xml, n);
      else if (n.getName().equals("td"))
        processTd(xml, n);
      else if (n.getName().equals("tfoot"))
        processTFoot(xml, n);
      else if (n.getName().equals("th"))
        processTh(xml, n);
      else if (n.getName().equals("thead"))
        processTHead(xml, n);
      else if (n.getName().equals("tr"))
        processTr(xml, n);
      else
        throw new FHIRException("Unknown element "+n.getName());
    }
  }

  private void processBreak(IXMLWriter xml, XhtmlNode n) throws IOException {
    xml.element("br");
  }

  private void processCaption(IXMLWriter xml, XhtmlNode n) throws IOException, FHIRException {
    processAttributes(n, xml, "id", "language", "styleCode");
    xml.enter("caption");
    processChildren(xml, n);
    xml.exit("caption");
  }

  private void processCol(IXMLWriter xml, XhtmlNode n) throws IOException, FHIRException {
    processAttributes(n, xml, "id", "language", "styleCode", "span", "width", "align", "char", "charoff", "valign");
    xml.enter("col");
    processChildren(xml, n);
    xml.exit("col");
  }

  private void processColGroup(IXMLWriter xml, XhtmlNode n) throws IOException, FHIRException {
    processAttributes(n, xml, "id", "language", "styleCode", "span", "width", "align", "char", "charoff", "valign");
    xml.enter("colgroup");
    processChildren(xml, n);
    xml.exit("colgroup");
  }

  private void processContent(IXMLWriter xml, XhtmlNode n) throws IOException, FHIRException {
    processAttributes(n, xml, "id", "language", "styleCode");
    xml.enter("content");
    // todo: do something with revised..., "revised"
    processChildren(xml, n);
    xml.exit("content");
  }

  private void processFootNote(IXMLWriter xml, XhtmlNode n) {
    throw new Error("element "+n.getName()+" not handled yet");
  }

  private void processFootNodeRef(IXMLWriter xml, XhtmlNode n) {
    throw new Error("element "+n.getName()+" not handled yet");
  }

  private void processItem(IXMLWriter xml, XhtmlNode n) throws IOException, FHIRException {
    processAttributes(n, xml, "id", "language", "styleCode");
    xml.enter("item");
    processChildren(xml, n);
    xml.exit("item");
  }

  private void processlinkHtml(IXMLWriter xml, XhtmlNode n) throws IOException, FHIRException {
    String v = n.getAttribute("src");
    xml.attribute("referencedObject", v);
    processAttributes(n, xml, "name", "href", "rel", "rev", "title", "id", "language", "styleCode");
    xml.enter("linkHtml");
    processChildren(xml, n);
    xml.exit("linkHtml");
  }

  private void processList(IXMLWriter xml, XhtmlNode n) throws IOException, FHIRException {
    if (n.getName().equals("ol"))
      xml.attribute("listType", "ordered");
    else
      xml.attribute("listType", "unordered");
    processAttributes(n, xml, "id", "language", "styleCode");
    xml.enter("list");
    processChildren(xml, n);
    xml.exit("list");
  }

  private void processParagraph(IXMLWriter xml, XhtmlNode n) throws IOException, FHIRException {
    processAttributes(n, xml, "id", "language", "styleCode");
    xml.enter("paragraph");
    processChildren(xml, n);
    xml.exit("paragraph");
  }

  private void processRenderMultiMedia(IXMLWriter xml, XhtmlNode n) throws IOException, FHIRException {
    String v = n.getAttribute("src");
    xml.attribute("referencedObject", v);
    processAttributes(n, xml, "id", "language", "styleCode");
    xml.enter("renderMultiMedia");
    processChildren(xml, n);
    xml.exit("renderMultiMedia");
  }

  private void processSub(IXMLWriter xml, XhtmlNode n) throws IOException, FHIRException {
    xml.enter("sub");
    processChildren(xml, n);
    xml.exit("sub");
  }

  private void processSup(IXMLWriter xml, XhtmlNode n) throws IOException, FHIRException {
    xml.enter("sup");
    processChildren(xml, n);
    xml.exit("sup");
  }

  private void processTable(IXMLWriter xml, XhtmlNode n) throws IOException, FHIRException {
    processAttributes(n, xml, "id", "language", "styleCode", "summary", "width", "border", "frame", "rules", "cellspacing", "cellpadding");
    xml.enter("table");
    processChildren(xml, n);
    xml.exit("table");
  }

  private void processTBody(IXMLWriter xml, XhtmlNode n) throws IOException, FHIRException {
    processAttributes(n, xml, "id", "language", "styleCode", "align", "char", "charoff", "valign");
    xml.enter("tbody");
    processChildren(xml, n);
    xml.exit("tbody");
  }

  private void processTd(IXMLWriter xml, XhtmlNode n) throws IOException, FHIRException {
    processAttributes(n, xml, "id", "language", "styleCode", "abbr", "axis", "headers", "scope", "rowspan", "colspan", "align", "char", "charoff", "valign");
    xml.enter("td");
    processChildren(xml, n);
    xml.exit("td");
  }

  private void processTFoot(IXMLWriter xml, XhtmlNode n) {
    throw new Error("element "+n.getName()+" not handled yet");
  }

  private void processTh(IXMLWriter xml, XhtmlNode n) throws IOException, FHIRException {
    processAttributes(n, xml, "id", "language", "styleCode", "abbr", "axis", "headers", "scope", "rowspan", "colspan", "align", "char", "charoff", "valign");
    xml.enter("th");
    processChildren(xml, n);
    xml.exit("th");
  }

  private void processTHead(IXMLWriter xml, XhtmlNode n) throws IOException, FHIRException {
    processAttributes(n, xml, "id", "language", "styleCode", "align", "char", "charoff", "valign");
    xml.enter("thead");
    processChildren(xml, n);
    xml.exit("thead");
  }

  private void processTr(IXMLWriter xml, XhtmlNode n) throws IOException, FHIRException {
    processAttributes(n, xml, "id", "language", "styleCode", "align", "char", "charoff", "valign");
    xml.enter("tr");
    processChildren(xml, n);
    xml.exit("tr");
  }

  private void processAttributes(XhtmlNode xn, IXMLWriter xml, String... names) throws IOException {
    for (String n : names) {
      if (xn.hasAttribute(n)) {
        String v = xn.getAttribute(n);
        if (n.equals("id"))
          xml.attribute("ID", v);
        else
          xml.attribute(n, v);
      }
    }
  }


}
