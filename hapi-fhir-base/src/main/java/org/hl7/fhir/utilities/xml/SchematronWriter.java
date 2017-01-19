package org.hl7.fhir.utilities.xml;

/*
 * #%L
 * HAPI FHIR - Core Library
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
      ln_i("<sch:pattern>");
      ln("<sch:title>"+s.title+"</sch:title>");
      for (Rule r : s.rules) {
        if (!r.asserts.isEmpty()) {
          ln_i("<sch:rule context=\""+r.name+"\">");
          for (Assert a : r.asserts) 
            ln("<sch:assert test=\""+Utilities.escapeXml(a.test)+"\">"+Utilities.escapeXml(a.message)+"</sch:assert>");
          ln_o("</sch:rule>");
        }
      }
      ln_o("</sch:pattern>");
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
