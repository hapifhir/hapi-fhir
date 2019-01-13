package org.hl7.fhir.dstu2016may.metamodel;

/*-
 * #%L
 * org.hl7.fhir.dstu2016may
 * %%
 * Copyright (C) 2014 - 2019 Health Level 7
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


import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.lang3.NotImplementedException;
import org.hl7.fhir.dstu2016may.formats.IParser.OutputStyle;
import org.hl7.fhir.dstu2016may.formats.RdfGenerator;
import org.hl7.fhir.dstu2016may.formats.RdfGenerator.Complex;
import org.hl7.fhir.dstu2016may.formats.RdfGenerator.Section;
import org.hl7.fhir.dstu2016may.formats.RdfGenerator.Subject;
import org.hl7.fhir.dstu2016may.utils.IWorkerContext;
import org.hl7.fhir.utilities.Utilities;

public class TurtleParser extends ParserBase {

  private String base;
  
  public TurtleParser(IWorkerContext context) {
    super(context);
  }
  @Override
  public Element parse(InputStream stream) throws Exception {
    throw new NotImplementedException("not done yet");
  }
  @Override
  public void compose(Element e, OutputStream stream, OutputStyle style, String base) throws Exception {
    this.base = base;
    
		RdfGenerator ttl = new RdfGenerator(stream);
		//      ttl.setFormat(FFormat);
		ttl.prefix("fhir", "http://hl7.org/fhir/");
		ttl.prefix("rdfs", "http://www.w3.org/2000/01/rdf-schema#");
		ttl.prefix("owl", "http://www.w3.org/2002/07/owl#");
		ttl.prefix("xs", "http://www.w3.org/2001/XMLSchema#");
		
		Section section = ttl.section("resource");
		Subject subject;
		String id = e.getChildValue("id");
		if (base != null && id != null) 
			subject = section.triple("<"+base+"/"+e.getType()+"/"+id+">", "a", "fhir:"+e.getType());
		else
		  subject = section.triple("_", "a", "fhir:"+e.getType());
		subject.predicate("fhir:nodeRole", "fhir:treeRoot");

		for (Element child : e.getChildren()) {
			composeElement(subject, child);
		}
		ttl.commit(false);
  }
  
  protected void decorateReference(Complex t, Element coding) {
    String ref = coding.getChildValue("reference");
    if (ref != null && (ref.startsWith("http://") || ref.startsWith("https://")))
      t.predicate("fhir:reference", "<"+ref+">");
    else if (base != null && ref != null && ref.contains("/")) {
      t.predicate("fhir:reference", "<"+Utilities.appendForwardSlash(base)+ref+">");
    }
  }
  
	protected void decorateCoding(Complex t, Element coding) {
		String system = coding.getChildValue("system");
		String code = coding.getChildValue("code");
		
		if (system == null)
			return;
		if ("http://snomed.info/sct".equals(system)) {
			t.prefix("sct", "http://snomed.info/sct/");
			t.predicate("a", "sct:"+code);
		} else if ("http://loinc.org".equals(system)) {
			t.prefix("loinc", "http://loinc.org/rdf#");
			t.predicate("a", "loinc:"+code);
		}  
	}

	private void decorateCodeableConcept(Complex t, Element element) {
	  for (Element c : element.getChildren("coding")) {
	  	decorateCoding(t, c);
	  }
	}
	
	private void composeElement(Complex ctxt, Element element) {
		if ("xhtml".equals(element.getType())) // need to decide what to do with this
			return;
		String en = element.getProperty().getDefinition().getBase().getPath();
    if (en == null) 
      en = element.getProperty().getDefinition().getPath();
		boolean doType = false;
			if (en.endsWith("[x]")) {
				en = en.substring(0, en.length()-3);
				doType = true;				
			}
	   if (doType || element.getProperty().getDefinition().getType().size() > 1)
	     en = en + Utilities.capitalize(element.getType());

	  Complex t = ctxt.predicate("fhir:"+en);
	  if (element.hasValue())
	  	t.predicate("fhir:value", ttlLiteral(element.getValue(), element.getType()));
	  if (element.hasIndex())
	  	t.predicate("fhir:index", Integer.toString(element.getIndex()));

	  if ("Coding".equals(element.getType()))
	  	decorateCoding(t, element);
	  if ("CodeableConcept".equals(element.getType()))
	  	decorateCodeableConcept(t, element);
    if ("Reference".equals(element.getType()))
      decorateReference(t, element);
	  		
		for (Element child : element.getChildren()) {
			composeElement(t, child);
		}
	}
	
	protected String ttlLiteral(String value, String type) {
	  String xst = "";
	  if (type.equals("boolean"))
	    xst = "^^xs:boolean";
	  else if (type.equals("integer") || type.equals("unsignedInt") || type.equals("positiveInt"))
      xst = "^^xs:int";
    else if (type.equals("decimal"))
      xst = "^^xs:decimal";
    else if (type.equals("base64Binary"))
      xst = "^^xs:base64Binary";
    else if (type.equals("instant"))
      xst = "^^xs:dateTime";
    else if (type.equals("time"))
      xst = "^^xs:time";
    else if (type.equals("date") || type.equals("dateTime") ) {
      String v = value;
      if (v.length() > 10) {
        int i = value.substring(10).indexOf("-");
        if (i == -1)
          i = value.substring(10).indexOf("+");
        v = i == -1 ? value : value.substring(0,  10+i);
      }
      if (v.length() > 10)
        xst = "^^xs:dateTime";
      else if (v.length() == 10)
        xst = "^^xs:date";
      else if (v.length() == 7)
        xst = "^^xs:gYearMonth";
      else if (v.length() == 4)
        xst = "^^xs:gYear";
    }
	  
		return "\"" +RdfGenerator.escape(value, true) + "\""+xst;
	}

}
