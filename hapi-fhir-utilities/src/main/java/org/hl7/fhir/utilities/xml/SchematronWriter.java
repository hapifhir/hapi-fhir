package org.hl7.fhir.utilities.xml;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.hl7.fhir.utilities.TextStreamWriter;
import org.hl7.fhir.utilities.Utilities;


public class SchematronWriter  extends TextStreamWriter  {

  public enum SchematronType {
    ALL_RESOURCES,
    RESOURCE,
    PROFILE
  }

  public class Assert {
    private String test;
    private String message; 
  }
  
  public class Rule {
    private String name; 
    private List<Assert> asserts = new ArrayList<Assert>();   
    public void assrt(String test, String message) {
      Assert a = new Assert();
      a.test = test;
      a.message = message;
      asserts.add(a);
    }
    
    public boolean isSpecial() {
      return name.contains("*") || name.contains("[");
    }
  }
  public class Section {
    private String title;
    private List<Rule> rules = new ArrayList<Rule>();
    
    public String getTitle() {
      return title;
    }

    public void setTitle(String title) {
      this.title = title;
    }

    public Rule rule(String name) {
      for (Rule r : rules) {
        if (r.name.equals(name))
          return r;
      }
      Rule r = new Rule();
      r.name = name;
      rules.add(r);
      return r;
    }

    public boolean hasRegularContent() {
      for (Rule r : rules) 
        if (!r.asserts.isEmpty() && !r.isSpecial())
          return true;
      return false;
    }

    public boolean hasSpecialContent() {
      for (Rule r : rules) 
        if (!r.asserts.isEmpty() && r.isSpecial())
          return true;
      return false;
    }
    
    public List<Rule> getRegularRules() {
      List<Rule> regular = new ArrayList<Rule>();
      for (Rule r : rules) 
        if (!r.asserts.isEmpty() && !r.isSpecial())
          regular.add(r);
      return regular;
    }

    public List<Rule> getSpecialRules() {
      List<Rule> regular = new ArrayList<Rule>();
      for (Rule r : rules) 
        if (!r.asserts.isEmpty() && r.isSpecial())
          regular.add(r);
      return regular;
    }
}

  private SchematronType type;
  private String description;
  private List<Section> sections = new ArrayList<Section>();


  public SchematronWriter(OutputStream out, SchematronType type, String description) throws UnsupportedEncodingException {
    super(out);
    this.type = type;
    this.description = description;
  }

  public Section section(String title) {
    for (Section s : sections) {
      if (s.title.equals(title))
        return s;
    }
    Section s = new Section();
    s.title = title;
    sections.add(s);
    return s;
  }
  
  public void dump() throws IOException {
    ln("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
    ln_i("<sch:schema xmlns:sch=\"http://purl.oclc.org/dsdl/schematron\" queryBinding=\"xslt2\">");
    ln("<sch:ns prefix=\"f\" uri=\"http://hl7.org/fhir\"/>");
    ln("<sch:ns prefix=\"h\" uri=\"http://www.w3.org/1999/xhtml\"/>");
    addNote();

    for (Section s : sections) {
      if (s.hasRegularContent()) {
        ln_i("<sch:pattern>");
        ln("<sch:title>"+Utilities.escapeXml(s.title)+"</sch:title>");
        for (Rule r : s.getRegularRules()) {
          if (!r.asserts.isEmpty()) {
            ln_i("<sch:rule context=\""+Utilities.escapeXml(r.name)+"\">");
            for (Assert a : r.asserts) 
              ln("<sch:assert test=\""+Utilities.escapeXml(a.test)+"\">"+Utilities.escapeXml(a.message)+"</sch:assert>");
            ln_o("</sch:rule>");
          }
        }
        ln_o("</sch:pattern>");
      }
      if (s.hasSpecialContent()) {
        int i = 1;
        for (Rule r : s.getSpecialRules()) {
          ln_i("<sch:pattern>");
          ln("<sch:title>"+Utilities.escapeXml(s.title)+" "+i+"</sch:title>");
          i++;
          if (!r.asserts.isEmpty()) {
            ln_i("<sch:rule context=\""+Utilities.escapeXml(r.name)+"\">");
            for (Assert a : r.asserts) 
              ln("<sch:assert test=\""+Utilities.escapeXml(a.test)+"\">"+Utilities.escapeXml(a.message)+"</sch:assert>");
            ln_o("</sch:rule>");
          }
          ln_o("</sch:pattern>");
        }
      }
    }  
    ln_o("</sch:schema>");
    flush();
    close();
  }

  private void addNote() throws IOException {
    switch (type) {
    case ALL_RESOURCES : addAllResourcesNote(); break;
    case RESOURCE : addResourceNote(); break;
    case PROFILE : addProfileNote(); break;
    }
  }

  private void addAllResourcesNote() throws IOException {
    ln("<!-- ");
    ln("  This file contains constraints for all resources");
    ln("  Because of the way containment works, this file should always )");
    ln("  be used for validating resources. Alternatively you can use ");
    ln("  the resource specific files to build a smaller version of");
    ln("  this file (the contents are identical; only include those ");
    ln("  resources relevant to your implementation).");
    ln("-->");
  }

  private void addResourceNote() throws IOException {
    ln("<!-- ");
    ln("  This file contains just the constraints for the resource "+description);
    ln("  It is provided for documentation purposes. When actually validating,");
    ln("  always use fhir-invariants.sch (because of the way containment works)");
    ln("  Alternatively you can use this file to build a smaller version of");
    ln("  fhir-invariants.sch (the contents are identical; only include those ");
    ln("  resources relevant to your implementation).");
    ln("-->");
  }

  private void addProfileNote() throws IOException {
    ln("<!-- ");
    ln("  This file contains just the constraints for the profile "+description);
    ln("  It includes the base constraints for the resource as well.");
    ln("  Because of the way that schematrons and containment work, ");
    ln("  you may need to use this schematron fragment to build a, ");
    ln("  single schematron that validates contained resources (if you have any) ");
    ln("-->");
    
  }

}
