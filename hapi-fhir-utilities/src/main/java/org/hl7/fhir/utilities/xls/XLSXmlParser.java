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
package org.hl7.fhir.utilities.xls;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.utilities.Utilities;
import org.hl7.fhir.utilities.xml.XMLUtil;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XLSXmlParser {

  private static final String XLS_NS = "urn:schemas-microsoft-com:office:spreadsheet";

  public class Row extends ArrayList<String> {  private static final long serialVersionUID = 1L; }
  
  public class Sheet {
    public String title;
    public Row columns;
    public List<Row> rows = new ArrayList<Row>();

    public boolean hasColumn(String column)  {
      for (int i = 0; i < columns.size(); i++) {
        if (columns.get(i).equalsIgnoreCase(column))
          return true;
      }
      return false;
    }
    
    public boolean hasColumn(int row, String column)  {
      String s = getColumn(row, column);
      return s != null && !s.equals("");     
    }
    
    public String getColumn(int row, String column)  {
      int c = -1;
      String s = "";
      for (int i = 0; i < columns.size(); i++) {
        s = s + ","+columns.get(i);
        if (columns.get(i).equalsIgnoreCase(column))
          c = i;
      }
      if (c == -1)
        return ""; // throw new FHIRException("unable to find column "+column+" in "+s.substring(1));
      else if (rows.get(row).size() <= c)
        return "";
      else {
        s = rows.get(row).get(c); 
        return s == null ? "" : s.trim();
      }
    }

    public List<String> getColumnNamesBySuffix(String suffix)  {
      List<String> names = new ArrayList<String>();
      for (int i = 0; i < columns.size(); i++) {
        if (columns.get(i).endsWith(suffix))
          names.add(columns.get(i));
      }
      return names;
    }

    public String getByColumnPrefix(int row, String column)  {
      int c = -1;
      String s = "";
      for (int i = 0; i < columns.size(); i++) {
        s = s + ","+columns.get(i);
        if (columns.get(i).startsWith(column))
          c = i;
      }
      if (c == -1)
        return ""; // throw new FHIRException("unable to find column "+column+" in "+s.substring(1));
      else if (rows.get(row).size() <= c)
        return "";
      else
        return rows.get(row).get(c).trim();
    }

    public List<Row> getRows() {
      return rows;
    }

    public int getIntColumn(int row, String column)  {
      String value = getColumn(row, column);
      if (Utilities.noString(value))
        return 0;
      else
        return Integer.parseInt(value);
    }

    public String getNonEmptyColumn(int row, String column) throws FHIRException  {
     String value = getColumn(row, column);
     if (Utilities.noString(value))
       throw new FHIRException("The colummn "+column+" cannot be empty");
     return value;
    }

    public boolean hasColumnContent(String col) {
      int i = columns.indexOf(col);
      if (i == -1)
        return false;
      for (Row r : rows) {
        if (r.size() > i && !Utilities.noString(r.get(i)))
          return true;
      }
      return false;
    }
    
    
  }
  
  private Map<String, Sheet> sheets;
  private Document xml;
  private String name;
  
  public XLSXmlParser(InputStream in, String name) throws FHIRException  {
    this.name = name;
    try {
      xml = parseXml(in);
      sheets = new HashMap<String, Sheet>();
      readXml();
    } catch (Exception e) {
      throw new FHIRException("unable to load "+name+": "+e.getMessage(), e);
    }
  }

  private Document parseXml(InputStream in) throws FHIRException  {
    try {
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      factory.setNamespaceAware(true);
      DocumentBuilder builder = factory.newDocumentBuilder();
      return builder.parse(in);
    } catch (Exception e) {
      throw new FHIRException("Error processing "+name+": "+e.getMessage(), e);
    }
  }

  private void readXml() throws FHIRException  {
    Element root = xml.getDocumentElement();
    check(root.getNamespaceURI().equals(XLS_NS), "Spreadsheet namespace incorrect");
    check(root.getNodeName().equals("Workbook"), "Spreadsheet element name incorrect");
    Node node = root.getFirstChild();
    while (node != null) {
      if (node.getNodeName().equals("Worksheet"))
        processWorksheet((Element)node);
      node = node.getNextSibling();
    }
  }
  
  private Integer rowIndex;
  private void processWorksheet(Element node) throws FHIRException  {
    Sheet sheet = new Sheet();
    sheet.title = node.getAttributeNS(XLS_NS, "Name");
    sheets.put(node.getAttributeNS(XLS_NS, "Name"), sheet);
    NodeList table = node.getElementsByTagNameNS(XLS_NS, "Table");
    check(table.getLength() == 1, "multiple table elements");
    NodeList rows = ((Element)table.item(0)).getElementsByTagNameNS(XLS_NS, "Row");
    if (rows.getLength() == 0) 
      return;
    rowIndex = 1;
    sheet.columns = readRow((Element) rows.item(0));
    for (int i = 1; i < rows.getLength(); i++) {
      rowIndex++;
      sheet.rows.add(readRow((Element) rows.item(i)));
    }
       
    //Remove empty rows at the end of the sheet
    while( sheet.rows.size() != 0 && isEmptyRow(sheet.rows.get(sheet.rows.size()-1) ) )
    	sheet.rows.remove(sheet.rows.size()-1);
  }

  
  private boolean isEmptyRow(Row w)
  { 
	  for( int col=0; col<w.size(); col++ )
		  if( !w.get(col).trim().isEmpty() ) return false;
	  
	  return true;
  }
  
  private Row readRow(Element row) throws DOMException, FHIRException  {
    Row res = new Row();
    int ndx = 1;    
    NodeList cells = row.getElementsByTagNameNS(XLS_NS, "Cell");
    for (int i = 0; i < cells.getLength(); i++) {
      Element cell = (Element) cells.item(i);
      if (cell.hasAttributeNS(XLS_NS, "Index")) {
        int index = Integer.parseInt(cell.getAttributeNS(XLS_NS, "Index"));
        while (ndx < index) {
          res.add("");
          ndx++;
        }
      }
      res.add(readData(cell, ndx, res.size() > 0 ? res.get(0) : "?"));
      ndx++;      
    }
    return res;
  }

  private String readData(Element cell, int col, String s) throws DOMException, FHIRException  {
    List<Element> data = new ArrayList<Element>(); 
    XMLUtil.getNamedChildren(cell, "Data", data); // cell.getElementsByTagNameNS(XLS_NS, "Data");
    if (data.size() == 0)
      return "";
    check(data.size() == 1, "Multiple Data encountered ("+Integer.toString(data.size())+" @ col "+Integer.toString(col)+" - "+cell.getTextContent()+" ("+s+"))");
    Element d = data.get(0);
    String type = d.getAttributeNS(XLS_NS, "Type");
    if ("Boolean".equals(type)) {
      if (d.getTextContent().equals("1"))
        return "True";
      else
        return "False";
    } else if ("String".equals(type)) {
      return d.getTextContent();
    } else if ("Number".equals(type)) {
      return d.getTextContent();
    } else if ("DateTime".equals(type)) {
      return d.getTextContent();
    } else if ("Error".equals(type)) {
      return null;
    } else 
      throw new FHIRException("Cell Type is not known ("+d.getAttributeNodeNS(XLS_NS, "Type")+") in "+getLocation());
  }

  private void check(boolean test, String message) throws FHIRException  {
    if (!test)
      throw new FHIRException(message+" in "+getLocation());
  }
  
  private String getLocation() {
    return name+", row "+rowIndex.toString();
  }

  public Map<String, Sheet> getSheets() {
    return sheets;
  }

  
}
