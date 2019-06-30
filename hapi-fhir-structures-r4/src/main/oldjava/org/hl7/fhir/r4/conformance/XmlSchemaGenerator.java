package org.hl7.fhir.r4.conformance;
import java.io.FileOutputStream;
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
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.r4.context.IWorkerContext;
import org.hl7.fhir.r4.model.ElementDefinition;
import org.hl7.fhir.r4.model.ElementDefinition.PropertyRepresentation;
import org.hl7.fhir.r4.model.ElementDefinition.TypeRefComponent;
import org.hl7.fhir.r4.model.StructureDefinition;
import org.hl7.fhir.r4.utils.ToolingExtensions;
import org.hl7.fhir.utilities.CommaSeparatedStringBuilder;
import org.hl7.fhir.utilities.Utilities;


public class XmlSchemaGenerator  {

  public class QName {

    public String type;
    public String typeNs;

    @Override
    public String toString() {
      return typeNs+":"+type;
    }
  }

  public class ElementToGenerate {

    private String tname;
    private StructureDefinition sd;
    private ElementDefinition ed;

    public ElementToGenerate(String tname, StructureDefinition sd, ElementDefinition edc) {
      this.tname = tname;
      this.sd = sd;
      this.ed = edc;
    }


  }


  private String folder;
	private IWorkerContext context;
	private boolean single;
	private String version;
	private String genDate;
	private String license;
	private boolean annotations;

	public XmlSchemaGenerator(String folder, IWorkerContext context) {
    this.folder = folder;
    this.context = context;
	}

  public boolean isSingle() {
    return single;
  }

  public void setSingle(boolean single) {
    this.single = single;
  }
  

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public String getGenDate() {
    return genDate;
  }

  public void setGenDate(String genDate) {
    this.genDate = genDate;
  }

  public String getLicense() {
    return license;
  }

  public void setLicense(String license) {
    this.license = license;
  }


  public boolean isAnnotations() {
    return annotations;
  }

  public void setAnnotations(boolean annotations) {
    this.annotations = annotations;
  }


  private Set<ElementDefinition> processed = new HashSet<ElementDefinition>();
  private Set<StructureDefinition> processedLibs = new HashSet<StructureDefinition>();
  private Set<String> typeNames = new HashSet<String>();
  private OutputStreamWriter writer;
  private Map<String, String> namespaces = new HashMap<String, String>();
  private Queue<ElementToGenerate> queue = new LinkedList<ElementToGenerate>();
  private Queue<StructureDefinition> queueLib = new LinkedList<StructureDefinition>();
  private Map<String, StructureDefinition> library;
  private boolean useNarrative;

  private void w(String s) throws IOException {
    writer.write(s);
  }
  
  private void ln(String s) throws IOException {
    writer.write(s);
    writer.write("\r\n");
  }

  private void close() throws IOException {
    if (writer != null) {
      ln("</xs:schema>");
      writer.flush();
      writer.close();
      writer = null;
    }
  }

  private String start(StructureDefinition sd, String ns) throws IOException, FHIRException {
    String lang = "en";
    if (sd.hasLanguage())
      lang = sd.getLanguage();

    if (single && writer != null) {
      if (!ns.equals(getNs(sd)))
        throw new FHIRException("namespace inconsistency: "+ns+" vs "+getNs(sd));
      return lang;
    }
    close();
    
    writer = new OutputStreamWriter(new FileOutputStream(Utilities.path(folder, tail(sd.getType()+".xsd"))), "UTF-8");
    ln("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
    ln("<!-- ");
    ln(license);
    ln("");
    ln("  Generated on "+genDate+" for FHIR v"+version+" ");
    ln("");
    ln("  Note: this schema does not contain all the knowledge represented in the underlying content model");
    ln("");
    ln("-->");
    ln("<xs:schema xmlns:xs=\"http://www.w3.org/2001/XMLSchema\" xmlns:fhir=\"http://hl7.org/fhir\" xmlns:xhtml=\"http://www.w3.org/1999/xhtml\" "+
        "xmlns:lm=\""+ns+"\" targetNamespace=\""+ns+"\" elementFormDefault=\"qualified\" version=\"1.0\">");
    ln("  <xs:import schemaLocation=\"fhir-common.xsd\" namespace=\"http://hl7.org/fhir\"/>");
    if (useNarrative) {
      if (ns.equals("urn:hl7-org:v3"))
        ln("  <xs:include schemaLocation=\"cda-narrative.xsd\"/>");
      else
        ln("  <xs:import schemaLocation=\"cda-narrative.xsd\" namespace=\"urn:hl7-org:v3\"/>");
    }
    namespaces.clear();
    namespaces.put(ns, "lm");
    namespaces.put("http://hl7.org/fhir", "fhir");
    typeNames.clear();
    
    return lang;
  }


  private String getNs(StructureDefinition sd) {
    String ns = "http://hl7.org/fhir";
    if (sd.hasExtension("http://hl7.org/fhir/StructureDefinition/elementdefinition-namespace"))
      ns = ToolingExtensions.readStringExtension(sd, "http://hl7.org/fhir/StructureDefinition/elementdefinition-namespace");
    return ns;
  }

	public void generate(StructureDefinition entry, Map<String, StructureDefinition> library) throws Exception {
	  processedLibs.clear();
	  
	  this.library = library;
	  checkLib(entry);
	  
	  String ns = getNs(entry);
	  String lang = start(entry, ns);

	  w("  <xs:element name=\""+tail(entry.getType())+"\" type=\"lm:"+tail(entry.getType())+"\"");
    if (annotations) {
      ln(">");
      ln("    <xs:annotation>");
      ln("      <xs:documentation xml:lang=\""+lang+"\">"+Utilities.escapeXml(entry.getDescription())+"</xs:documentation>");
      ln("    </xs:annotation>");
      ln("  </xs:element>");
    } else
      ln("/>");

		produceType(entry, entry.getSnapshot().getElement().get(0), tail(entry.getType()), getQN(entry, entry.getBaseDefinition()), lang);
		while (!queue.isEmpty()) {
		  ElementToGenerate q = queue.poll();
		  produceType(q.sd, q.ed, q.tname, getQN(q.sd, q.ed, "http://hl7.org/fhir/StructureDefinition/Element", false), lang);
		}
		while (!queueLib.isEmpty()) {
		  generateInner(queueLib.poll());
		}
		close();
	}




  private void checkLib(StructureDefinition entry) {
    for (ElementDefinition ed : entry.getSnapshot().getElement()) {
      if (ed.hasRepresentation(PropertyRepresentation.CDATEXT)) {
        useNarrative = true;
      }
    }
    for (StructureDefinition sd : library.values()) {
      for (ElementDefinition ed : sd.getSnapshot().getElement()) {
        if (ed.hasRepresentation(PropertyRepresentation.CDATEXT)) {
          useNarrative = true;
        }
      }
    }
  }

  private void generateInner(StructureDefinition sd) throws IOException, FHIRException {
    if (processedLibs.contains(sd))
      return;
    processedLibs.add(sd);
    
    String ns = getNs(sd);
    String lang = start(sd, ns);

    if (sd.getSnapshot().getElement().isEmpty())
      throw new FHIRException("no snap shot on "+sd.getUrl());
    
    produceType(sd, sd.getSnapshot().getElement().get(0), tail(sd.getType()), getQN(sd, sd.getBaseDefinition()), lang);
    while (!queue.isEmpty()) {
      ElementToGenerate q = queue.poll();
      produceType(q.sd, q.ed, q.tname, getQN(q.sd, q.ed, "http://hl7.org/fhir/StructureDefinition/Element", false), lang);
    }
  }

  private String tail(String url) {
    return url.contains("/") ? url.substring(url.lastIndexOf("/")+1) : url;
  }
  private String root(String url) {
    return url.contains("/") ? url.substring(0, url.lastIndexOf("/")) : "";
  }


  private String tailDot(String url) {
    return url.contains(".") ? url.substring(url.lastIndexOf(".")+1) : url;
  }
  private void produceType(StructureDefinition sd, ElementDefinition ed, String typeName, QName typeParent, String lang) throws IOException, FHIRException {
    if (processed.contains(ed))
      return;
    processed.add(ed);
    
    // ok 
    ln("  <xs:complexType name=\""+typeName+"\">");
    if (annotations) {
      ln("    <xs:annotation>");
      ln("      <xs:documentation xml:lang=\""+lang+"\">"+Utilities.escapeXml(ed.getDefinition())+"</xs:documentation>");
      ln("    </xs:annotation>");
    }
    ln("    <xs:complexContent>");
    ln("      <xs:extension base=\""+typeParent.toString()+"\">");
    ln("        <xs:sequence>");
    
    // hack....
    for (ElementDefinition edc : ProfileUtilities.getChildList(sd,  ed)) {
      if (!(edc.hasRepresentation(PropertyRepresentation.XMLATTR) || edc.hasRepresentation(PropertyRepresentation.XMLTEXT)) && !inheritedElement(edc))
        produceElement(sd, ed, edc, lang);
    }
    ln("        </xs:sequence>");
    for (ElementDefinition edc : ProfileUtilities.getChildList(sd,  ed)) {
      if ((edc.hasRepresentation(PropertyRepresentation.XMLATTR) || edc.hasRepresentation(PropertyRepresentation.XMLTEXT)) && !inheritedElement(edc))
        produceAttribute(sd, ed, edc, lang);
    }
    ln("      </xs:extension>");
    ln("    </xs:complexContent>");
    ln("  </xs:complexType>");    
  }


  private boolean inheritedElement(ElementDefinition edc) {
    return !edc.getPath().equals(edc.getBase().getPath());
  }

  private void produceElement(StructureDefinition sd, ElementDefinition ed, ElementDefinition edc, String lang) throws IOException, FHIRException {
    if (edc.getType().size() == 0) 
      throw new Error("No type at "+edc.getPath());
    
    if (edc.getType().size() > 1 && edc.hasRepresentation(PropertyRepresentation.TYPEATTR)) {
      // first, find the common base type
      StructureDefinition lib = getCommonAncestor(edc.getType());
      if (lib == null)
        throw new Error("Common ancester not found at "+edc.getPath());
      CommaSeparatedStringBuilder b = new CommaSeparatedStringBuilder();
      for (TypeRefComponent t : edc.getType()) {
        b.append(getQN(sd, edc, t.getCode(), true).toString());
      }
      
      String name = tailDot(edc.getPath());
      String min = String.valueOf(edc.getMin());
      String max = edc.getMax();
      if ("*".equals(max))
        max = "unbounded";

      QName qn = getQN(sd, edc, lib.getUrl(), true);

      ln("        <xs:element name=\""+name+"\" minOccurs=\""+min+"\" maxOccurs=\""+max+"\" type=\""+qn.typeNs+":"+qn.type+"\">");
      ln("          <xs:annotation>");
      ln("          <xs:appinfo xml:lang=\"en\">Possible types: "+b.toString()+"</xs:appinfo>");
      if (annotations && edc.hasDefinition()) 
        ln("            <xs:documentation xml:lang=\""+lang+"\">"+Utilities.escapeXml(edc.getDefinition())+"</xs:documentation>");
      ln("          </xs:annotation>");
      ln("        </xs:element>");
    } else for (TypeRefComponent t : edc.getType()) {
      String name = tailDot(edc.getPath());
      if (edc.getType().size() > 1)
        name = name + Utilities.capitalize(t.getCode());
      QName qn = getQN(sd, edc, t.getCode(), true);
      String min = String.valueOf(edc.getMin());
      String max = edc.getMax();
      if ("*".equals(max))
        max = "unbounded";


      w("        <xs:element name=\""+name+"\" minOccurs=\""+min+"\" maxOccurs=\""+max+"\" type=\""+qn.typeNs+":"+qn.type+"\"");
      if (annotations && edc.hasDefinition()) {
        ln(">");
        ln("          <xs:annotation>");
        ln("            <xs:documentation xml:lang=\""+lang+"\">"+Utilities.escapeXml(edc.getDefinition())+"</xs:documentation>");
        ln("          </xs:annotation>");
        ln("        </xs:element>");
      } else
        ln("/>");
    }
  }

  public QName getQN(StructureDefinition sd, String type) throws FHIRException {
    return getQN(sd, sd.getSnapshot().getElementFirstRep(), type, false);
  }
  
  public QName getQN(StructureDefinition sd, ElementDefinition edc, String t, boolean chase) throws FHIRException {
    QName qn = new QName();
    qn.type = Utilities.isAbsoluteUrl(t) ? tail(t) : t;
    if (Utilities.isAbsoluteUrl(t)) {
      String ns = root(t);
      if (ns.equals(root(sd.getUrl())))
        ns = getNs(sd);
      if (ns.equals("http://hl7.org/fhir/StructureDefinition"))
        ns = "http://hl7.org/fhir";
      if (!namespaces.containsKey(ns))
        throw new FHIRException("Unknown type namespace "+ns+" for "+edc.getPath());
      qn.typeNs = namespaces.get(ns);
      StructureDefinition lib = library.get(t);
      if (lib == null && !Utilities.existsInList(t, "http://hl7.org/fhir/cda/StructureDefinition/StrucDoc.Text", "http://hl7.org/fhir/StructureDefinition/Element"))
        throw new FHIRException("Unable to resolve "+t+" for "+edc.getPath());
      if (lib != null) 
        queueLib.add(lib);
    } else
      qn.typeNs = namespaces.get("http://hl7.org/fhir");

    if (chase && qn.type.equals("Element")) {
      String tname = typeNameFromPath(edc);
      if (typeNames.contains(tname)) {
        int i = 1;
        while (typeNames.contains(tname+i)) 
          i++;
        tname = tname+i;
      }
      queue.add(new ElementToGenerate(tname, sd, edc));
      qn.typeNs = "lm";
      qn.type = tname;
    }
    return qn;
  }
  
  private StructureDefinition getCommonAncestor(List<TypeRefComponent> type) throws FHIRException {
    StructureDefinition sd = library.get(type.get(0).getCode());
    if (sd == null)
      throw new FHIRException("Unable to find definition for "+type.get(0).getCode()); 
    for (int i = 1; i < type.size(); i++) {
      StructureDefinition t = library.get(type.get(i).getCode());
      if (t == null)
        throw new FHIRException("Unable to find definition for "+type.get(i).getCode()); 
      sd = getCommonAncestor(sd, t);
    }
    return sd;
  }

  private StructureDefinition getCommonAncestor(StructureDefinition sd1, StructureDefinition sd2) throws FHIRException {
    // this will always return something because everything comes from Element
    List<StructureDefinition> chain1 = new ArrayList<>();
    List<StructureDefinition> chain2 = new ArrayList<>();
    chain1.add(sd1);
    chain2.add(sd2);
    StructureDefinition root = library.get("Element");
    StructureDefinition common = findIntersection(chain1, chain2);
    boolean chain1Done = false;
    boolean chain2Done = false;
    while (common == null) {
       chain1Done = checkChain(chain1, root, chain1Done);
       chain2Done = checkChain(chain2, root, chain2Done);
       if (chain1Done && chain2Done)
         return null;
       common = findIntersection(chain1, chain2);
    }
    return common;
  }

  
  private StructureDefinition findIntersection(List<StructureDefinition> chain1, List<StructureDefinition> chain2) {
    for (StructureDefinition sd1 : chain1)
      for (StructureDefinition sd2 : chain2)
        if (sd1 == sd2)
          return sd1;
    return null;
  }

  public boolean checkChain(List<StructureDefinition> chain1, StructureDefinition root, boolean chain1Done) throws FHIRException {
    if (!chain1Done) {
       StructureDefinition sd = chain1.get(chain1.size()-1);
      String bu = sd.getBaseDefinition();
       if (bu == null)
         throw new FHIRException("No base definition for "+sd.getUrl());
       StructureDefinition t = library.get(bu);
       if (t == null)
         chain1Done = true;
       else
         chain1.add(t);
     }
    return chain1Done;
  }

  private StructureDefinition getBase(StructureDefinition structureDefinition) {
    return null;
  }

  private String typeNameFromPath(ElementDefinition edc) {
    StringBuilder b = new StringBuilder();
    boolean up = true;
    for (char ch : edc.getPath().toCharArray()) {
      if (ch == '.')
        up = true;
      else if (up) {
        b.append(Character.toUpperCase(ch));
        up = false;
      } else
        b.append(ch);
    }
    return b.toString();
  }

  private void produceAttribute(StructureDefinition sd, ElementDefinition ed, ElementDefinition edc, String lang) throws IOException, FHIRException {
    TypeRefComponent t = edc.getTypeFirstRep();
    String name = tailDot(edc.getPath());
    String min = String.valueOf(edc.getMin());
    String max = edc.getMax();
    // todo: check it's a code...
//    if (!max.equals("1"))
//      throw new FHIRException("Illegal cardinality \""+max+"\" for attribute "+edc.getPath());
    
    if (Utilities.isAbsoluteUrl(t.getCode())) 
      throw new FHIRException("Only FHIR primitive types are supported for attributes ("+t.getCode()+")");
    String typeNs = namespaces.get("http://hl7.org/fhir");
    String type = t.getCode(); 
    
    w("        <xs:attribute name=\""+name+"\" use=\""+(min.equals("0") || edc.hasFixed() || edc.hasDefaultValue() ? "optional" : "required")+"\" type=\""+typeNs+":"+type+(typeNs.equals("fhir") ? "-primitive" : "")+"\""+
    (edc.hasFixed() ? " fixed=\""+edc.getFixed().primitiveValue()+"\"" : "")+(edc.hasDefaultValue() && !edc.hasFixed() ? " default=\""+edc.getDefaultValue().primitiveValue()+"\"" : "")+"");
    if (annotations && edc.hasDefinition()) {
      ln(">");
      ln("          <xs:annotation>");
      ln("            <xs:documentation xml:lang=\""+lang+"\">"+Utilities.escapeXml(edc.getDefinition())+"</xs:documentation>");
      ln("          </xs:annotation>");
      ln("        </xs:attribute>");
    } else
      ln("/>");
  }

	
}
