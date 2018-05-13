package org.hl7.fhir.utilities.xhtml;

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

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.utilities.TranslatingUtilities;
import org.hl7.fhir.utilities.Utilities;


public class HierarchicalTableGenerator extends TranslatingUtilities {
  public static final String TEXT_ICON_REFERENCE = "Reference to another Resource";
  public static final String TEXT_ICON_PRIMITIVE = "Primitive Data Type";
  public static final String TEXT_ICON_DATATYPE = "Data Type";
  public static final String TEXT_ICON_RESOURCE = "Resource";
  public static final String TEXT_ICON_ELEMENT = "Element";
  public static final String TEXT_ICON_REUSE = "Reference to another Element";
  public static final String TEXT_ICON_EXTENSION = "Extension";
  public static final String TEXT_ICON_CHOICE = "Choice of Types";
  public static final String TEXT_ICON_SLICE = "Slice Definition";
  public static final String TEXT_ICON_EXTENSION_SIMPLE = "Simple Extension";
  public static final String TEXT_ICON_PROFILE = "Profile";
  public static final String TEXT_ICON_EXTENSION_COMPLEX = "Complex Extension";

  public static final int NEW_REGULAR = 0;
  public static final int CONTINUE_REGULAR = 1;
  public static final int NEW_SLICER = 2;
  public static final int CONTINUE_SLICER = 3;
  public static final int NEW_SLICE = 4;
  public static final int CONTINUE_SLICE = 5;  
  
  private static Map<String, String> files = new HashMap<String, String>();

  public class Piece {
    private String tag;
    private String reference;
    private String text;
    private String hint;
    private String style;
    private Map<String, String> attributes;
    private List<XhtmlNode> children;
    
    public Piece(String tag) {
      super();
      this.tag = tag;
    }
    
    public Piece(String reference, String text, String hint) {
      super();
      this.reference = reference;
      this.text = text;
      this.hint = hint;
    }
    public String getReference() {
      return reference;
    }
    public void setReference(String value) {
      reference = value;
    }
    public String getText() {
      return text;
    }
    public String getHint() {
      return hint;
    }

    public String getTag() {
      return tag;
    }

    public String getStyle() {
      return style;
    }

    public void setTag(String tag) {
      this.tag = tag;
    }

    public Piece setText(String text) {
      this.text = text;
      return this;
    }

    public void setHint(String hint) {
      this.hint = hint;
    }

    public Piece setStyle(String style) {
      this.style = style;
      return this;
    }

    public Piece addStyle(String style) {
      if (this.style != null)
        this.style = this.style+"; "+style;
      else
        this.style = style;
      return this;
    }

    public void addToHint(String text) {
      if (this.hint == null)
        this.hint = text;
      else
        this.hint += (this.hint.endsWith(".") || this.hint.endsWith("?") ? " " : ". ")+text;
    }
    
    public boolean hasChildren() {
      return children != null && !children.isEmpty();
    }

    public List<XhtmlNode> getChildren() {
      if (children == null)
        children = new ArrayList<XhtmlNode>();
      return children;
    }
    
  }
  
  public class Cell {
    private List<Piece> pieces = new ArrayList<HierarchicalTableGenerator.Piece>();

    public Cell() {
      
    }
    public Cell(String prefix, String reference, String text, String hint, String suffix) {
      super();
      if (!Utilities.noString(prefix))
        pieces.add(new Piece(null, prefix, null));
      pieces.add(new Piece(reference, text, hint));
      if (!Utilities.noString(suffix))
        pieces.add(new Piece(null, suffix, null));
    }
    public List<Piece> getPieces() {
      return pieces;
    }
    public Cell addPiece(Piece piece) {
      pieces.add(piece);
      return this;
    }
    public Cell addMarkdown(String md) {
      try {
        Parser parser = Parser.builder().build();
        Node document = parser.parse(md);
        HtmlRenderer renderer = HtmlRenderer.builder().escapeHtml(true).build();
        String html = renderer.render(document);  
        pieces.addAll(htmlToParagraphPieces(html));
      } catch (Exception e) {
        e.printStackTrace();
      }
      return this;
    }
    private List<Piece> htmlToParagraphPieces(String html) throws IOException, FHIRException {
      List<Piece> myPieces = new ArrayList<Piece>();
      String[] paragraphs = html.replace("<p>", "").split("<\\/p>|<br  \\/>");
      for (int i=0;i<paragraphs.length;i++) {
        if (!paragraphs[i].isEmpty()) {
          if (i!=0) {
            myPieces.add(new Piece("br"));
            myPieces.add(new Piece("br"));
          }
          myPieces.addAll(htmlFormattingToPieces(paragraphs[i]));
        }
      }
      
      return myPieces;
    }
    private List<Piece> htmlFormattingToPieces(String html) throws IOException, FHIRException {
      List<Piece> myPieces = new ArrayList<Piece>();
      if (html.contains(("<"))) {
        XhtmlNode node = new XhtmlParser().parseFragment("<p>"+html+"</p>");
        for (XhtmlNode c : node.getChildNodes()) {
          addNode(myPieces, c);
        }
      } else
        myPieces.add(new Piece(null, html, null));        
      return myPieces;
    }
    private void addNode(List<Piece> list, XhtmlNode c) {
      if (c.getNodeType() == NodeType.Text)
        list.add(new Piece(null, c.getContent(), null));
      else if (c.getNodeType() == NodeType.Element) {
        if (c.getName().equals("a")) {
          list.add(new Piece(c.getAttribute("href"), c.allText(), c.getAttribute("title")));                    
        } else if (c.getName().equals("b") || c.getName().equals("em") || c.getName().equals("strong")) {
          list.add(new Piece(null, c.allText(), null).setStyle("font-face: bold"));                    
        } else if (c.getName().equals("code")) {
          list.add(new Piece(null, c.allText(), null).setStyle("padding: 2px 4px; color: #005c00; background-color: #f9f2f4; white-space: nowrap; border-radius: 4px"));                    
        } else if (c.getName().equals("i")) {
          list.add(new Piece(null, c.allText(), null).setStyle("font-style: italic"));
        } else if (c.getName().equals("pre")) {
          Piece p = new Piece(c.getName()).setStyle("white-space: pre; font-family: courier");
          list.add(p);
          p.getChildren().addAll(c.getChildNodes());
        } else if (c.getName().equals("ul") || c.getName().equals("ol")) {
          Piece p = new Piece(c.getName());
          list.add(p);
          p.getChildren().addAll(c.getChildNodes());
        } else if (c.getName().equals("i")) {
          list.add(new Piece(null, c.allText(), null).setStyle("font-style: italic"));                    
        } else if (c.getName().equals("h1")||c.getName().equals("h2")||c.getName().equals("h3")||c.getName().equals("h4")) {
          Piece p = new Piece(c.getName());
          list.add(p);
          p.getChildren().addAll(c.getChildNodes());
        } else if (c.getName().equals("br")) {
          list.add(new Piece(c.getName()));
        } else {
          
          throw new Error("Not handled yet: "+c.getName());
        }
      } else
        throw new Error("Unhandled type "+c.getNodeType().toString());

    }
    public void addStyle(String style) {
      for (Piece p : pieces)
        p.addStyle(style);      
    }
    public void addToHint(String text) {
      for (Piece p : pieces)
        p.addToHint(text);            
    }
    public Piece addStyledText(String hint, String alt, String fgColor, String bgColor, String link, boolean border) {
      Piece p = new Piece(link, alt, hint);
      p.addStyle("padding-left: 3px");
      p.addStyle("padding-right: 3px");
      if (border) {
        p.addStyle("border: 1px grey solid");
        p.addStyle("font-weight: bold");
      }
      if (fgColor != null) {
        p.addStyle("color: "+fgColor);
        p.addStyle("background-color: "+bgColor);
      } else {
        p.addStyle("color: black");
        p.addStyle("background-color: white");       
      }
      pieces.add(p);
      return p;
    }
    public String text() {
      StringBuilder b = new StringBuilder();
      for (Piece p : pieces)
        b.append(p.text);
      return b.toString();
    }
    @Override
    public String toString() {
      return text();
    }
    
    
  }

  public class Title extends Cell {
    private int width;

    public Title(String prefix, String reference, String text, String hint, String suffix, int width) {
      super(prefix, reference, text, hint, suffix);
      this.width = width;
    }
  }
  
  public class Row {
    private List<Row> subRows = new ArrayList<HierarchicalTableGenerator.Row>();
    private List<Cell> cells = new ArrayList<HierarchicalTableGenerator.Cell>();
    private String icon;
    private String anchor;
    private String hint;
    private String color;
    private int lineColor;
    
    public List<Row> getSubRows() {
      return subRows;
    }
    public List<Cell> getCells() {
      return cells;
    }
    public String getIcon() {
      return icon;
    }
    public void setIcon(String icon, String hint) {
      this.icon = icon;
      this.hint = hint;
    }
    public String getAnchor() {
      return anchor;
    }
    public void setAnchor(String anchor) {
      this.anchor = anchor;
    }
    public String getHint() {
      return hint;
    }
    public String getColor() {
      return color;
    }
    public void setColor(String color) {
      this.color = color;
    }
    public int getLineColor() {
      return lineColor;
    }
    public void setLineColor(int lineColor) {
      assert lineColor >= 0;
      assert lineColor <= 2;
      this.lineColor = lineColor;
    }
    
    
  }

  public class TableModel {
    private List<Title> titles = new ArrayList<HierarchicalTableGenerator.Title>();
    private List<Row> rows = new ArrayList<HierarchicalTableGenerator.Row>();
    private String docoRef;
    private String docoImg;
    public List<Title> getTitles() {
      return titles;
    }
    public List<Row> getRows() {
      return rows;
    }
    public String getDocoRef() {
      return docoRef;
    }
    public String getDocoImg() {
      return docoImg;
    }
    public void setDocoRef(String docoRef) {
      this.docoRef = docoRef;
    }
    public void setDocoImg(String docoImg) {
      this.docoImg = docoImg;
    }
    
  }


  private String dest;
  
  /**
   * There are circumstances where the table has to present in the absence of a stable supporting infrastructure.
   * and the file paths cannot be guaranteed. For these reasons, you can tell the builder to inline all the graphics
   * (all the styles are inlined anyway, since the table fbuiler has even less control over the styling
   *  
   */
  private boolean inLineGraphics;
  
  
  public HierarchicalTableGenerator() {
    super();
  }

  public HierarchicalTableGenerator(String dest, boolean inlineGraphics) {
    super();
    this.dest = dest;
    this.inLineGraphics = inlineGraphics;
  }

  public TableModel initNormalTable(String prefix, boolean isLogical) {
    TableModel model = new TableModel();
    
    model.setDocoImg(prefix+"help16.png");
    model.setDocoRef(prefix+"formats.html#table");
    model.getTitles().add(new Title(null, model.getDocoRef(), translate("sd.head", "Name"), translate("sd.hint", "The logical name of the element"), null, 0));
    model.getTitles().add(new Title(null, model.getDocoRef(), translate("sd.head", "Flags"), translate("sd.hint", "Information about the use of the element"), null, 0));
    model.getTitles().add(new Title(null, model.getDocoRef(), translate("sd.head", "Card."), translate("sd.hint", "Minimum and Maximum # of times the the element can appear in the instance"), null, 0));
    model.getTitles().add(new Title(null, model.getDocoRef(), translate("sd.head", "Type"), translate("sd.hint", "Reference to the type of the element"), null, 100));
    model.getTitles().add(new Title(null, model.getDocoRef(), translate("sd.head", "Description & Constraints"), translate("sd.hint", "Additional information about the element"), null, 0));
    if (isLogical) {
      model.getTitles().add(new Title(null, prefix+"structuredefinition.html#logical", "Implemented As", "How this logical data item is implemented in a concrete resource", null, 0));
    }
    return model;
  }


  public TableModel initGridTable(String prefix) {
    TableModel model = new TableModel();
    
    model.getTitles().add(new Title(null, model.getDocoRef(), translate("sd.head", "Name"), translate("sd.hint", "The name of the element (Slice name in brackets).  Mouse-over provides definition"), null, 0));
    model.getTitles().add(new Title(null, model.getDocoRef(), translate("sd.head", "Card."), translate("sd.hint", "Minimum and Maximum # of times the the element can appear in the instance. Super-scripts indicate additional constraints on appearance"), null, 0));
    model.getTitles().add(new Title(null, model.getDocoRef(), translate("sd.head", "Type"), translate("sd.hint", "Reference to the type of the element"), null, 100));
    model.getTitles().add(new Title(null, model.getDocoRef(), translate("sd.head", "Constraints and Usage"), translate("sd.hint", "Fixed values, length limits, vocabulary bindings and other usage notes"), null, 0));
    return model;
  }

  public XhtmlNode generate(TableModel model, String imagePath, int border, Set<String> outputTracker) throws IOException, FHIRException  {
    checkModel(model);
    XhtmlNode table = new XhtmlNode(NodeType.Element, "table").setAttribute("border", Integer.toString(border)).setAttribute("cellspacing", "0").setAttribute("cellpadding", "0");
    table.setAttribute("style", "border: " + border + "px #F0F0F0 solid; font-size: 11px; font-family: verdana; vertical-align: top;");
    XhtmlNode tr = table.addTag("tr");
    tr.setAttribute("style", "border: " + Integer.toString(1 + border) + "px #F0F0F0 solid; font-size: 11px; font-family: verdana; vertical-align: top;");
    XhtmlNode tc = null;
    for (Title t : model.getTitles()) {
      tc = renderCell(tr, t, "th", null, null, null, false, null, "white", 0, imagePath, border, outputTracker);
      if (t.width != 0)
        tc.setAttribute("style", "width: "+Integer.toString(t.width)+"px");
    }
    if (tc != null && model.getDocoRef() != null)
      tc.addTag("span").setAttribute("style", "float: right").addTag("a").setAttribute("title", "Legend for this format").setAttribute("href", model.getDocoRef()).addTag("img").setAttribute("alt", "doco").setAttribute("style", "background-color: inherit").setAttribute("src", model.getDocoImg());
      
    for (Row r : model.getRows()) {
      renderRow(table, r, 0, new ArrayList<Integer>(), imagePath, border, outputTracker);
    }
    if (model.getDocoRef() != null) {
      tr = table.addTag("tr");
      tc = tr.addTag("td");
      tc.setAttribute("class", "hierarchy");
      tc.setAttribute("colspan", Integer.toString(model.getTitles().size()));
      tc.addTag("br");
      XhtmlNode a = tc.addTag("a").setAttribute("title", translate("sd.doco", "Legend for this format")).setAttribute("href", model.getDocoRef());
      if (model.getDocoImg() != null)
        a.addTag("img").setAttribute("alt", "doco").setAttribute("style", "background-color: inherit").setAttribute("src", model.getDocoImg());
      a.addText(" "+translate("sd.doco", "Documentation for this format"));
    }
    return table;
  }


  private void renderRow(XhtmlNode table, Row r, int indent, List<Integer> indents, String imagePath, int border, Set<String> outputTracker) throws IOException  {
    XhtmlNode tr = table.addTag("tr");
    String color = "white";
    if (r.getColor() != null)
      color = r.getColor();
    tr.setAttribute("style", "border: " + border + "px #F0F0F0 solid; padding:0px; vertical-align: top; background-color: "+color+";");
    boolean first = true;
    for (Cell t : r.getCells()) {
      renderCell(tr, t, "td", first ? r.getIcon() : null, first ? r.getHint() : null, first ? indents : null, !r.getSubRows().isEmpty(), first ? r.getAnchor() : null, color, r.getLineColor(), imagePath, border, outputTracker);
      first = false;
    }
    table.addText("\r\n");
    
    for (int i = 0; i < r.getSubRows().size(); i++) {
      Row c = r.getSubRows().get(i);
      List<Integer> ind = new ArrayList<Integer>();
      ind.addAll(indents);
      if (i == r.getSubRows().size() - 1) {
        ind.add(r.getLineColor()*2);
      } else {
        ind.add(r.getLineColor()*2+1);
      }
      renderRow(table, c, indent+1, ind, imagePath, border, outputTracker);
    }
  }


  private XhtmlNode renderCell(XhtmlNode tr, Cell c, String name, String icon, String hint, List<Integer> indents, boolean hasChildren, String anchor, String color, int lineColor, String imagePath, int border, Set<String> outputTracker) throws IOException  {
    XhtmlNode tc = tr.addTag(name);
    tc.setAttribute("class", "hierarchy");
    if (indents != null) {
      tc.addTag("img").setAttribute("src", srcFor(imagePath, "tbl_spacer.png")).setAttribute("style", "background-color: inherit").setAttribute("class", "hierarchy").setAttribute("alt", ".");
      tc.setAttribute("style", "vertical-align: top; text-align : left; background-color: "+color+"; border: "+ border +"px #F0F0F0 solid; padding:0px 4px 0px 4px; white-space: nowrap; background-image: url("+imagePath+checkExists(indents, hasChildren, lineColor, outputTracker)+")");
      for (int i = 0; i < indents.size()-1; i++) {
        switch (indents.get(i)) {
          case NEW_REGULAR:
          case NEW_SLICER:
          case NEW_SLICE:
            tc.addTag("img").setAttribute("src", srcFor(imagePath, "tbl_blank.png")).setAttribute("style", "background-color: inherit").setAttribute("class", "hierarchy").setAttribute("alt", ".");
            break;
          case CONTINUE_REGULAR:
            tc.addTag("img").setAttribute("src", srcFor(imagePath, "tbl_vline.png")).setAttribute("style", "background-color: inherit").setAttribute("class", "hierarchy").setAttribute("alt", ".");
            break;
          case CONTINUE_SLICER:
            tc.addTag("img").setAttribute("src", srcFor(imagePath, "tbl_vline_slicer.png")).setAttribute("style", "background-color: inherit").setAttribute("class", "hierarchy").setAttribute("alt", ".");
            break;
          case CONTINUE_SLICE:
            tc.addTag("img").setAttribute("src", srcFor(imagePath, "tbl_vline_slice.png")).setAttribute("style", "background-color: inherit").setAttribute("class", "hierarchy").setAttribute("alt", ".");
            break;
          default:
            throw new Error("Unrecognized indent level: " + indents.get(i));
        }
      }
      if (!indents.isEmpty())
        switch (indents.get(indents.size()-1)) {
        case NEW_REGULAR:
          tc.addTag("img").setAttribute("src", srcFor(imagePath, "tbl_vjoin_end.png")).setAttribute("style", "background-color: inherit").setAttribute("class", "hierarchy").setAttribute("alt", ".");
          break;
        case NEW_SLICER:
          tc.addTag("img").setAttribute("src", srcFor(imagePath, "tbl_vjoin_end_slicer.png")).setAttribute("style", "background-color: inherit").setAttribute("class", "hierarchy").setAttribute("alt", ".");
          break;
        case NEW_SLICE:
          tc.addTag("img").setAttribute("src", srcFor(imagePath, "tbl_vjoin_end_slice.png")).setAttribute("style", "background-color: inherit").setAttribute("class", "hierarchy").setAttribute("alt", ".");
          break;
        case CONTINUE_REGULAR:
          tc.addTag("img").setAttribute("src", srcFor(imagePath, "tbl_vjoin.png")).setAttribute("style", "background-color: inherit").setAttribute("class", "hierarchy").setAttribute("alt", ".");
          break;
        case CONTINUE_SLICER:
          tc.addTag("img").setAttribute("src", srcFor(imagePath, "tbl_vjoin_slicer.png")).setAttribute("style", "background-color: inherit").setAttribute("class", "hierarchy").setAttribute("alt", ".");
          break;
        case CONTINUE_SLICE:
          tc.addTag("img").setAttribute("src", srcFor(imagePath, "tbl_vjoin_slice.png")).setAttribute("style", "background-color: inherit").setAttribute("class", "hierarchy").setAttribute("alt", ".");
          break;
        default:
          throw new Error("Unrecognized indent level: " + indents.get(indents.size()-1));
        }
    }
    else
      tc.setAttribute("style", "vertical-align: top; text-align : left; background-color: "+color+"; border: "+ border +"px #F0F0F0 solid; padding:0px 4px 0px 4px");
    if (!Utilities.noString(icon)) {
      XhtmlNode img = tc.addTag("img").setAttribute("src", srcFor(imagePath, icon)).setAttribute("class", "hierarchy").setAttribute("style", "background-color: "+color+"; background-color: inherit").setAttribute("alt", ".");
      if (hint != null)
        img.setAttribute("title", hint);
      tc.addText(" ");
    }
    for (Piece p : c.pieces) {
      if (!Utilities.noString(p.getTag())) {
        XhtmlNode tag = tc.addTag(p.getTag());
        if (p.attributes != null)
          for (String n : p.attributes.keySet())
            tag.setAttribute(n, p.attributes.get(n));
        if (p.getHint() != null)
          tag.setAttribute("title", p.getHint());
        addStyle(tag, p);
        if (p.hasChildren())
          tag.getChildNodes().addAll(p.getChildren());
      } else if (!Utilities.noString(p.getReference())) {
        XhtmlNode a = addStyle(tc.addTag("a"), p);
        a.setAttribute("href", p.getReference());
        if (!Utilities.noString(p.getHint()))
          a.setAttribute("title", p.getHint());
        a.addText(p.getText());
        addStyle(a, p);
      } else { 
        if (!Utilities.noString(p.getHint())) {
          XhtmlNode s = addStyle(tc.addTag("span"), p);
          s.setAttribute("title", p.getHint());
          s.addText(p.getText());
        } else if (p.getStyle() != null) {
          XhtmlNode s = addStyle(tc.addTag("span"), p);
          s.addText(p.getText());
        } else
          tc.addText(p.getText());
      }
    }
    if (!Utilities.noString(anchor))
      tc.addTag("a").setAttribute("name", nmTokenize(anchor)).addText(" ");
    return tc;
  }


  private XhtmlNode addStyle(XhtmlNode node, Piece p) {
    if (p.getStyle() != null)
      node.setAttribute("style", p.getStyle());
    return node;
  }

  private String nmTokenize(String anchor) {
    return anchor.replace("[", "_").replace("]", "_");
  }
  
  private String srcFor(String corePrefix, String filename) throws IOException {
    if (inLineGraphics) {
      if (files.containsKey(filename))
        return files.get(filename);
      StringBuilder b = new StringBuilder();
      b.append("data: image/png;base64,");
      byte[] bytes;
      File file = new File(Utilities.path(dest, filename));
      if (!file.exists()) // because sometime this is called real early before the files exist. it will be built again later because of this
    	bytes = new byte[0]; 
      else
        bytes = FileUtils.readFileToByteArray(file);
      b.append(new String(Base64.encodeBase64(bytes)));
//      files.put(filename, b.toString());
      return b.toString();
    } else
      return corePrefix+filename;
  }


  private void checkModel(TableModel model) throws FHIRException  {
    check(!model.getRows().isEmpty(), "Must have rows");
    check(!model.getTitles().isEmpty(), "Must have titles");
    for (Cell c : model.getTitles())
      check(c);
    int i = 0;
    for (Row r : model.getRows()) { 
      check(r, "rows", model.getTitles().size(), Integer.toString(i));
      i++;
    }
  }


  private void check(Cell c) throws FHIRException  {  
    boolean hasText = false;
    for (Piece p : c.pieces)
      if (!Utilities.noString(p.getText()))
        hasText = true;
    check(hasText, "Title cells must have text");    
  }


  private void check(Row r, String string, int size, String path) throws FHIRException  {    
    check(r.getCells().size() == size, "All rows must have the same number of columns ("+Integer.toString(size)+") as the titles but row "+path+" doesn't ("+r.getCells().get(0).text()+"): "+r.getCells());
    int i = 0;
    for (Row c : r.getSubRows()) {
      check(c, "rows", size, path+"."+Integer.toString(i));
      i++;
    }
  }


  private String checkExists(List<Integer> indents, boolean hasChildren, int lineColor, Set<String> outputTracker) throws IOException  {
    String filename = makeName(indents);
    
    StringBuilder b = new StringBuilder();
    if (inLineGraphics) {
      if (files.containsKey(filename))
        return files.get(filename);
      ByteArrayOutputStream bytes = new ByteArrayOutputStream();
      genImage(indents, hasChildren, lineColor, bytes);
      b.append("data: image/png;base64,");
      byte[] encodeBase64 = Base64.encodeBase64(bytes.toByteArray());
      b.append(new String(encodeBase64));
      files.put(filename, b.toString());
      return b.toString();
    } else {
      b.append("tbl_bck");
      for (Integer i : indents)
        b.append(Integer.toString(i));
      int indent = lineColor*2 + (hasChildren?1:0);
      b.append(Integer.toString(indent));
      b.append(".png");
      String file = Utilities.path(dest, b.toString());
      if (!new File(file).exists()) {
        FileOutputStream stream = new FileOutputStream(file);
        genImage(indents, hasChildren, lineColor, stream);
        if (outputTracker!=null)
          outputTracker.add(file);
      }
      return b.toString();
    }
  }


  private void genImage(List<Integer> indents, boolean hasChildren, int lineColor, OutputStream stream) throws IOException {
    BufferedImage bi = new BufferedImage(800, 2, BufferedImage.TYPE_INT_ARGB);
    // i have no idea why this works to make these pixels transparent. It defies logic. 
    // But this combination of INT_ARGB and filling with grey magically worked when nothing else did. So it stays as is.
    Color grey = new Color(99,99,99,0); 
    for (int i = 0; i < 800; i++) {
      bi.setRGB(i, 0, grey.getRGB());
      bi.setRGB(i, 1, grey.getRGB());
    }
    Color black = new Color(0, 0, 0);
    Color green = new Color(14,209,69);
    Color gold = new Color(212,168,21);
    for (int i = 0; i < indents.size(); i++) {
      int indent = indents.get(i).intValue();
      if (indent == CONTINUE_REGULAR)
        bi.setRGB(12+(i*16), 0, black.getRGB());
      else if (indent == CONTINUE_SLICER)
        bi.setRGB(12+(i*16), 0, green.getRGB());
      else if (indent == CONTINUE_SLICE)
        bi.setRGB(12+(i*16), 0, gold.getRGB());
    }
    if (hasChildren) {
      if (lineColor==0)
        bi.setRGB(12+(indents.size()*16), 0, black.getRGB());
      else if (lineColor==1)
        bi.setRGB(12+(indents.size()*16), 0, green.getRGB());
      else if (lineColor==2)
        bi.setRGB(12+(indents.size()*16), 0, gold.getRGB());
    }
    ImageIO.write(bi, "PNG", stream);
  }

  private String makeName(List<Integer> indents) {
    StringBuilder b = new StringBuilder();
    b.append("indents:");
    for (Integer i : indents)
      b.append(Integer.toString(i));
    return b.toString();
  }

  private void check(boolean check, String message) throws FHIRException  {
    if (!check)
      throw new FHIRException(message);
  }
}
