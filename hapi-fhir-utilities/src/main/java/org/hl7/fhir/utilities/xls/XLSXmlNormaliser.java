package org.hl7.fhir.utilities.xls;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.utilities.TextFile;
import org.hl7.fhir.utilities.Utilities;
import org.hl7.fhir.utilities.xml.XMLUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class XLSXmlNormaliser {
  
  private static final String XLS_NS = "urn:schemas-microsoft-com:office:spreadsheet";

  private Document xml;

  private String source;
  private String dest;
  private boolean exceptionIfExcelNotNormalised;
  
  public XLSXmlNormaliser(String source, String dest, boolean exceptionIfExcelNotNormalised) {
    super();
    this.source = source;
    this.dest = dest;
    this.exceptionIfExcelNotNormalised = exceptionIfExcelNotNormalised;
  }
  
  public XLSXmlNormaliser(String source, boolean exceptionIfExcelNotNormalised) {
    super();
    this.source = source;
    this.dest = source;
    this.exceptionIfExcelNotNormalised = exceptionIfExcelNotNormalised;
  }
  
  public void go() throws FHIRException, TransformerException, ParserConfigurationException, SAXException, IOException {
    File inp = new File(source);
    long time = inp.lastModified();
    xml = parseXml(new FileInputStream(inp));
    
    Element root = xml.getDocumentElement();

    boolean hasComment = false;
    Node n = root.getFirstChild();
    while (n != null) {
      if (n.getNodeType() == Node.COMMENT_NODE && "canonicalized".equals(n.getTextContent())) {
        hasComment = true;
        break;
      }
      n = n.getNextSibling();
    }
    if (hasComment)
      return;
    if (exceptionIfExcelNotNormalised)
      throw new FHIRException("The spreadsheet "+dest+" was committed after editing in excel, but before the build could run *after Excel was closed*");
    
    System.out.println("normalise: "+source);
    
    XMLUtil.deleteByName(root, "ActiveSheet");
    Element xw = XMLUtil.getNamedChild(root, "ExcelWorkbook");
    XMLUtil.deleteByName(xw, "WindowHeight");
    XMLUtil.deleteByName(xw, "WindowWidth");
    XMLUtil.deleteByName(xw, "WindowTopX");
    XMLUtil.deleteByName(xw, "WindowTopY");

    for (Element wk : XMLUtil.getNamedChildren(root, "Worksheet"))
      processWorksheet(wk);
    
    if (!hasComment)
      root.appendChild(xml.createComment("canonicalized"));
    try {
      saveXml(new FileOutputStream(dest));
      String s = TextFile.fileToString(dest);
      s = s.replaceAll("\r\n","\n");
      s = replaceSignificantEoln(s);
      TextFile.stringToFile(s, dest, false);
      new File(dest).setLastModified(time);
    } catch (Exception e) {
      System.out.println("The file "+dest+" is still open in Excel, and you will have to run the build after closing Excel before committing");
    }
  }

  private String replaceSignificantEoln(String s) {
    StringBuilder b = new StringBuilder();
    boolean hasText = false;
    for (char c : s.toCharArray()) {
      if (c == '>' || c == '<' ) {
        hasText = false;
        b.append(c);
      } else if (c == '\n') {
        if (hasText) {
          b.append("&#10;");
        } else
          b.append(c);
        
      } else if (!Character.isWhitespace(c)) {
        b.append(c);
        hasText = true;
      } else 
        b.append(c);
    }
    
    return b.toString();
  }

  private void processWorksheet(Element wk) throws FHIRException  {
    Element tbl = XMLUtil.getNamedChild(wk, "Table");
    processTable(tbl);
    for (Element row : XMLUtil.getNamedChildren(tbl, "Row"))
      processRow(row);      
    for (Element col : XMLUtil.getNamedChildren(tbl, "Column"))
      processCol(col);      
    for (Element wo : XMLUtil.getNamedChildren(wk, "WorksheetOptions"))
      processOptions(wo);      
  }
  
  private void processOptions(Element wo) {
    XMLUtil.deleteByName(wo, "Unsynced");
    XMLUtil.deleteByName(wo, "Panes");
    for (Element panes : XMLUtil.getNamedChildren(wo, "Panes"))
      processPanes(panes);      
  }

  private void processPanes(Element panes) {
    for (Element pane : XMLUtil.getNamedChildren(panes, "Pane"))
      processPane(pane);        
  }

  private void processPane(Element pane) {
    XMLUtil.deleteByName(pane, "ActiveRow");
    XMLUtil.deleteByName(pane, "ActiveCol");    
  }

//  private void setTextElement(Element e, String name, String text) {
//    Element te = XMLUtil.getNamedChild(e, name);
//    if (te != null)
//      te.setTextContent(text);
//  }

  private void processTable(Element col) {
    XMLUtil.deleteAttr(col, "urn:schemas-microsoft-com:office:spreadsheet", "DefaultColumnWidth");
    XMLUtil.deleteAttr(col, "urn:schemas-microsoft-com:office:spreadsheet", "DefaultRowHeight");
  }


  private void processCol(Element col) {
    String width = col.getAttributeNS("urn:schemas-microsoft-com:office:spreadsheet", "Width");
    if (!Utilities.noString(width)) {
      Double d = Double.valueOf(width);
      width = Double.toString(Math.round(d*2)/2);
      col.setAttributeNS("urn:schemas-microsoft-com:office:spreadsheet", "ss:Width", width);
    }        
  }

  private void processRow(Element row) {
    String height = row.getAttributeNS("urn:schemas-microsoft-com:office:spreadsheet", "Height");
    if (!Utilities.noString(height) && height.contains(".")) {
      Double d = Double.valueOf(height);
      row.setAttributeNS("urn:schemas-microsoft-com:office:spreadsheet", "ss:Height", Long.toString(Math.round(d)));
    }    
  }

  private void check(boolean test, String message) throws FHIRException  {
    if (!test)
      throw new FHIRException(message+" in "+getLocation());
  }
  

  private Document parseXml(InputStream in) throws FHIRException, ParserConfigurationException, SAXException, IOException  {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    factory.setNamespaceAware(true);
    DocumentBuilder builder = factory.newDocumentBuilder();
    return builder.parse(in);
  }

  private void saveXml(FileOutputStream stream) throws TransformerException, IOException {

    TransformerFactory factory = TransformerFactory.newInstance();
    Transformer transformer = factory.newTransformer();
    Result result = new StreamResult(stream);
    Source source = new DOMSource(xml);
    transformer.transform(source, result);    
    stream.flush();
  }

  private String getLocation() {
    return source; //+", row "+rowIndex.toString();
  }


}
