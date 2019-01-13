package org.hl7.fhir.dstu3.elementmodel;

/*-
 * #%L
 * org.hl7.fhir.dstu3
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


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.NotImplementedException;
import org.hl7.fhir.dstu3.context.IWorkerContext;
import org.hl7.fhir.dstu3.elementmodel.Element.SpecialElement;
import org.hl7.fhir.dstu3.formats.IParser.OutputStyle;
import org.hl7.fhir.dstu3.model.ElementDefinition.TypeRefComponent;
import org.hl7.fhir.dstu3.formats.JsonCreator;
import org.hl7.fhir.dstu3.formats.JsonCreatorCanonical;
import org.hl7.fhir.dstu3.formats.JsonCreatorGson;
import org.hl7.fhir.utilities.Utilities;

public class JsonLDParser extends ParserBase {

	private JsonCreator json;
  private String base;
  private String jsonLDBase = "http://build.fhir.org/";

	public JsonLDParser(IWorkerContext context) {
		super(context);
	}

	@Override
	public Element parse(InputStream stream) {
		throw new NotImplementedException("not done yet");
	}


	protected void prop(String name, String value) throws IOException {
		if (name != null)
			json.name(name);
		json.value(value);
	}

	protected void open(String name) throws IOException {
		if (name != null) 
			json.name(name);
		json.beginObject();
	}

	protected void close() throws IOException {
		json.endObject();
	}

	protected void openArray(String name) throws IOException {
		if (name != null) 
			json.name(name);
		json.beginArray();
	}

	protected void closeArray() throws IOException {
		json.endArray();
	}


	@Override
	public void compose(Element e, OutputStream stream, OutputStyle style, String base) throws IOException {
	  this.base = base;
		OutputStreamWriter osw = new OutputStreamWriter(stream, "UTF-8");
		if (style == OutputStyle.CANONICAL)
			json = new JsonCreatorCanonical(osw);
		else
			json = new JsonCreatorGson(osw);
		json.setIndent(style == OutputStyle.PRETTY ? "  " : "");
		json.beginObject();
    prop("@type", "fhir:"+e.getType());
    prop("@context", jsonLDBase+"fhir.jsonld");
    prop("role", "fhir:treeRoot");
    String id = e.getChildValue("id");
    if (base != null && id != null) {
       if (base.endsWith("#"))
         prop("@id", base + e.getType() + "-" + id + ">");
      else
        prop("@id", Utilities.pathURL(base, e.getType(), id));
    }
		Set<String> done = new HashSet<String>();
		for (Element child : e.getChildren()) {
			compose(e.getName(), e, done, child);
		}
		json.endObject();
		json.finish();
		osw.flush();
	}

	private void compose(String path, Element e, Set<String> done, Element child) throws IOException {
		if (!child.isList()) {
			compose(path, child);
		} else if (!done.contains(child.getName())) {
			done.add(child.getName());
			List<Element> list = e.getChildrenByName(child.getName());
			composeList(path, list);
		}
	}

	private void composeList(String path, List<Element> list) throws IOException {
		// there will be at least one element
    String en = getFormalName(list.get(0));

    openArray(en);
    for (Element item : list) { 
      open(null);
      json.name("index");
      json.value(item.getIndex());
      if (item.isPrimitive() || isPrimitive(item.getType())) {
        if (item.hasValue())
          primitiveValue(item);
      }
      if (item.getProperty().isResource()) {
        prop("@type", "fhir:"+item.getType());
      }
      Set<String> done = new HashSet<String>();
      for (Element child : item.getChildren()) {
        compose(path+"."+item.getName(), item, done, child);
      }
      if ("Coding".equals(item.getType()))
        decorateCoding(item);
      if ("CodeableConcept".equals(item.getType()))
        decorateCodeableConcept(item);
      if ("Reference".equals(item.getType()))
        decorateReference(item);
      
      close();
    }
    closeArray();
	}

	private void primitiveValue(Element item) throws IOException {
	  String type = item.getType();
	  if (Utilities.existsInList(type, "date", "dateTime", "instant")) {
      String v = item.getValue();
      if (v.length() > 10) {
        int i = v.substring(10).indexOf("-");
        if (i == -1)
          i = v.substring(10).indexOf("+");
        v = i == -1 ? v : v.substring(0,  10+i);
      }
      if (v.length() > 10)
        json.name("dateTime");
      else if (v.length() == 10)
        json.name("date");
      else if (v.length() == 7)
        json.name("gYearMonth");
      else if (v.length() == 4)
        json.name("gYear");
      json.value(item.getValue());
	  } else if (Utilities.existsInList(type, "boolean")) {
      json.name("boolean");
      json.value(item.getValue().equals("true") ? new Boolean(true) : new Boolean(false));
	  } else if (Utilities.existsInList(type, "integer", "unsignedInt", "positiveInt")) {
      json.name("integer");
      json.value(new Integer(item.getValue()));
    } else if (Utilities.existsInList(type, "decimal")) {
      json.name("decimal");
      json.value(item.getValue());
    } else if (Utilities.existsInList(type, "base64Binary")) {
      json.name("binary");
      json.value(item.getValue());
    } else {
      json.name("value");
      json.value(item.getValue());
    }
	}

	private void compose(String path, Element element) throws IOException {
	  Property p = element.hasElementProperty() ? element.getElementProperty() : element.getProperty();
    String en = getFormalName(element);

    if (element.fhirType().equals("xhtml")) {
      json.name(en);
      json.value(element.getValue());
    } else if (element.hasChildren() || element.hasComments() || element.hasValue()) {
			open(en);
      if (element.getProperty().isResource()) {
	      prop("@type", "fhir:"+element.getType());
//        element = element.getChildren().get(0);
      }
	    if (element.isPrimitive() || isPrimitive(element.getType())) {
	      if (element.hasValue())
	        primitiveValue(element);
	    }
	    
			Set<String> done = new HashSet<String>();
			for (Element child : element.getChildren()) {
				compose(path+"."+element.getName(), element, done, child);
			}
	    if ("Coding".equals(element.getType()))
	      decorateCoding(element);
      if ("CodeableConcept".equals(element.getType()))
        decorateCodeableConcept(element);
      if ("Reference".equals(element.getType()))
        decorateReference(element);
			
			close();
		}
	}

  private void decorateReference(Element element) throws IOException {
    String ref = element.getChildValue("reference");
    if (ref != null && (ref.startsWith("http://") || ref.startsWith("https://"))) {
      json.name("link");
      json.value(ref);
    } else if (base != null && ref != null && ref.contains("/")) {
      json.name("link");
      json.value(Utilities.pathURL(base, ref));
    }
  }

  protected void decorateCoding(Element coding) throws IOException {
    String system = coding.getChildValue("system");
    String code = coding.getChildValue("code");
    
    if (system == null)
      return;
    if ("http://snomed.info/sct".equals(system)) {
      json.name("concept");
      json.value("http://snomed.info/id/"+code);
    } else if ("http://loinc.org".equals(system)) {
      json.name("concept");
      json.value("http://loinc.org/rdf#"+code);
    }  
  }

  private void decorateCodeableConcept(Element element) throws IOException {
    // nothing here; ITS committee decision
  }

  private String getFormalName(Element element) {
    String en = null;
    if (element.getSpecial() == null) {
      if (element.getProperty().getDefinition().hasBase())
        en = element.getProperty().getDefinition().getBase().getPath();
    }
    else if (element.getSpecial() == SpecialElement.BUNDLE_ENTRY)
      en = "Bundle.entry.resource";
    else if (element.getSpecial() == SpecialElement.BUNDLE_OUTCOME)
      en = "Bundle.entry.response.outcome";
    else if (element.getSpecial() == SpecialElement.PARAMETER)
      en = element.getElementProperty().getDefinition().getPath();
    else // CONTAINED
      en = "DomainResource.contained";
    
    if (en == null) 
      en = element.getProperty().getDefinition().getPath();
    boolean doType = false;
      if (en.endsWith("[x]")) {
        en = en.substring(0, en.length()-3);
        doType = true;        
      }
     if (doType || (element.getProperty().getDefinition().getType().size() > 1 && !allReference(element.getProperty().getDefinition().getType())))
       en = en + Utilities.capitalize(element.getType());
    return en;
  }

  private boolean allReference(List<TypeRefComponent> types) {
    for (TypeRefComponent t : types) {
      if (!t.getCode().equals("Reference"))
        return false;
    }
    return true;
  }

}
