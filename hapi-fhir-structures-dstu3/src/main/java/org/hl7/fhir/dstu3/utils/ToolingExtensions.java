package org.hl7.fhir.dstu3.utils;

import java.net.URISyntaxException;

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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.dstu3.exceptions.FHIRFormatError;
import org.hl7.fhir.dstu3.model.BooleanType;
import org.hl7.fhir.dstu3.model.CodeType;
import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.DataElement;
import org.hl7.fhir.dstu3.model.DomainResource;
import org.hl7.fhir.dstu3.model.Element;
import org.hl7.fhir.dstu3.model.ElementDefinition;
import org.hl7.fhir.dstu3.model.Extension;
import org.hl7.fhir.dstu3.model.ExtensionHelper;
import org.hl7.fhir.dstu3.model.Factory;
import org.hl7.fhir.dstu3.model.Identifier;
import org.hl7.fhir.dstu3.model.IntegerType;
import org.hl7.fhir.dstu3.model.MarkdownType;
import org.hl7.fhir.dstu3.model.PrimitiveType;
import org.hl7.fhir.dstu3.model.StringType;
import org.hl7.fhir.dstu3.model.Type;
import org.hl7.fhir.dstu3.model.UriType;
import org.hl7.fhir.dstu3.model.ValueSet;
import org.hl7.fhir.dstu3.model.Questionnaire.QuestionnaireItemComponent;
import org.hl7.fhir.dstu3.model.Questionnaire.QuestionnaireItemType;
import org.hl7.fhir.dstu3.model.ValueSet.ConceptDefinitionComponent;
import org.hl7.fhir.dstu3.model.ValueSet.ValueSetCodeSystemComponent;
import org.hl7.fhir.dstu3.validation.ValidationMessage.Source;


public class ToolingExtensions {

  // validated
  public static final String EXT_SUBSUMES = "http://hl7.org/fhir/StructureDefinition/valueset-subsumes"; 
  private static final String EXT_OID = "http://hl7.org/fhir/StructureDefinition/valueset-oid";
  public static final String EXT_DEPRECATED = "http://hl7.org/fhir/StructureDefinition/valueset-deprecated";
  public static final String EXT_DEFINITION = "http://hl7.org/fhir/StructureDefinition/valueset-definition";
  public static final String EXT_COMMENT = "http://hl7.org/fhir/StructureDefinition/valueset-comments";
  private static final String EXT_IDENTIFIER = "http://hl7.org/fhir/StructureDefinition/identifier";
  private static final String EXT_TRANSLATION = "http://hl7.org/fhir/StructureDefinition/translation";
  public static final String EXT_ISSUE_SOURCE = "http://hl7.org/fhir/StructureDefinition/operationoutcome-issue-source";
  public static final String EXT_DISPLAY_HINT = "http://hl7.org/fhir/StructureDefinition/structuredefinition-display-hint"; 
  public static final String EXT_REPLACED_BY = "http://hl7.org/fhir/StructureDefinition/valueset-replacedby";
  public static final String EXT_JSON_TYPE = "http://hl7.org/fhir/StructureDefinition/structuredefinition-json-type"; 
  public static final String EXT_XML_TYPE = "http://hl7.org/fhir/StructureDefinition/structuredefinition-xml-type"; 
  public static final String EXT_REGEX = "http://hl7.org/fhir/StructureDefinition/structuredefinition-regex"; 
  public static final String EXT_CONTROL = "http://hl7.org/fhir/StructureDefinition/questionnaire-itemControl"; 
  public static final String EXT_MINOCCURS = "http://hl7.org/fhir/StructureDefinition/questionnaire-minOccurs"; 
  public static final String EXT_MAXOCCURS = "http://hl7.org/fhir/StructureDefinition/questionnaire-maxOccurs";
  public static final String EXT_ALLOWEDRESOURCE = "http://hl7.org/fhir/StructureDefinition/questionnaire-allowedResource";
  public static final String EXT_REFERENCEFILTER = "http://hl7.org/fhir/StructureDefinition/questionnaire-referenceFilter";
  public static final String EXT_EXPRESSION = "http://hl7.org/fhir/StructureDefinition/structuredefinition-expression";
  public static final String EXT_SEARCH_EXPRESSION = "http://hl7.org/fhir/StructureDefinition/searchparameter-expression";

  // unregistered?

//  public static final String EXT_FLYOVER = "http://hl7.org/fhir/Profile/questionnaire-extensions#flyover";
//  private static final String EXT_QTYPE = "http://www.healthintersections.com.au/fhir/Profile/metadata#type";
//  private static final String EXT_QREF = "http://www.healthintersections.com.au/fhir/Profile/metadata#reference";
//  private static final String EXTENSION_FILTER_ONLY = "http://www.healthintersections.com.au/fhir/Profile/metadata#expandNeedsFilter";
//  private static final String EXT_TYPE = "http://www.healthintersections.com.au/fhir/Profile/metadata#type";
//  private static final String EXT_REFERENCE = "http://www.healthintersections.com.au/fhir/Profile/metadata#reference";
  private static final String EXT_FHIRTYPE = "http://hl7.org/fhir/StructureDefinition/questionnaire-fhirType";
  private static final String EXT_ALLOWABLE_UNITS = "http://hl7.org/fhir/StructureDefinition/elementdefinition-allowedUnits";
  public static final String EXT_CIMI_REFERENCE = "http://hl7.org/fhir/StructureDefinition/cimi-reference";
  public static final String EXT_UNCLOSED = "http://hl7.org/fhir/StructureDefinition/valueset-unclosed";
  public static final String EXT_FMM_LEVEL = "http://hl7.org/fhir/StructureDefinition/structuredefinition-fmm";


  // specific extension helpers

  public static Extension makeIssueSource(Source source) {
    Extension ex = new Extension();
    // todo: write this up and get it published with the pack (and handle the redirect?)
    ex.setUrl(ToolingExtensions.EXT_ISSUE_SOURCE);
    CodeType c = new CodeType();
    c.setValue(source.toString());
    ex.setValue(c);
    return ex;
  }

  public static boolean hasExtension(DomainResource de, String url) {
    return getExtension(de, url) != null;
  }

  public static boolean hasExtension(Element e, String url) {
    return getExtension(e, url) != null;
  }

  public static void addStringExtension(DomainResource dr, String url, String content) {
    if (!StringUtils.isBlank(content)) {
      Extension ex = getExtension(dr, url);
      if (ex != null)
        ex.setValue(new StringType(content));
      else
        dr.getExtension().add(Factory.newExtension(url, new StringType(content), true));   
    }
  }

  public static void addStringExtension(Element e, String url, String content) {
    if (!StringUtils.isBlank(content)) {
      Extension ex = getExtension(e, url);
      if (ex != null)
        ex.setValue(new StringType(content));
      else
        e.getExtension().add(Factory.newExtension(url, new StringType(content), true));   
    }
  }

  public static void addIntegerExtension(DomainResource dr, String url, int value) {
    Extension ex = getExtension(dr, url);
    if (ex != null)
      ex.setValue(new IntegerType(value));
    else
      dr.getExtension().add(Factory.newExtension(url, new IntegerType(value), true));   
  }

  public static void addComment(Element nc, String comment) {
    if (!StringUtils.isBlank(comment))
      nc.getExtension().add(Factory.newExtension(EXT_COMMENT, Factory.newString_(comment), true));   
  }

  public static void markDeprecated(Element nc) {
    setDeprecated(nc);   
  }

  public static void addSubsumes(ConceptDefinitionComponent nc, String code) {
    nc.getModifierExtension().add(Factory.newExtension(EXT_SUBSUMES, Factory.newCode(code), true));   
  }

  public static void addDefinition(Element nc, String definition) {
    if (!StringUtils.isBlank(definition))
      nc.getExtension().add(Factory.newExtension(EXT_DEFINITION, Factory.newString_(definition), true));   
  }

  public static void addDisplayHint(Element def, String hint) {
    if (!StringUtils.isBlank(hint))
      def.getExtension().add(Factory.newExtension(EXT_DISPLAY_HINT, Factory.newString_(hint), true));   
  }

  public static String getDisplayHint(Element def) {
    return readStringExtension(def, EXT_DISPLAY_HINT);    
  }

  public static String readStringExtension(Element c, String uri) {
    Extension ex = ExtensionHelper.getExtension(c, uri);
    if (ex == null)
      return null;
    if (ex.getValue() instanceof UriType)
      return ((UriType) ex.getValue()).getValue();
    if (!(ex.getValue() instanceof StringType))
      return null;
    return ((StringType) ex.getValue()).getValue();
  }

  public static String readStringExtension(DomainResource c, String uri) {
    Extension ex = getExtension(c, uri);
    if (ex == null)
      return null;
    if ((ex.getValue() instanceof StringType))
      return ((StringType) ex.getValue()).getValue();
    if ((ex.getValue() instanceof UriType))
      return ((UriType) ex.getValue()).getValue();
    if ((ex.getValue() instanceof MarkdownType))
      return ((MarkdownType) ex.getValue()).getValue();
    return null;
  }

  @SuppressWarnings("unchecked")
  public static PrimitiveType<Type> readPrimitiveExtension(DomainResource c, String uri) {
    Extension ex = getExtension(c, uri);
    if (ex == null)
      return null;
    return (PrimitiveType<Type>) ex.getValue();
  }

  public static boolean findStringExtension(Element c, String uri) {
    Extension ex = ExtensionHelper.getExtension(c, uri);
    if (ex == null)
      return false;
    if (!(ex.getValue() instanceof StringType))
      return false;
    return !StringUtils.isBlank(((StringType) ex.getValue()).getValue());
  }

  public static Boolean readBooleanExtension(Element c, String uri) {
    Extension ex = ExtensionHelper.getExtension(c, uri);
    if (ex == null)
      return null;
    if (!(ex.getValue() instanceof BooleanType))
      return null;
    return ((BooleanType) ex.getValue()).getValue();
  }

  public static boolean findBooleanExtension(Element c, String uri) {
    Extension ex = ExtensionHelper.getExtension(c, uri);
    if (ex == null)
      return false;
    if (!(ex.getValue() instanceof BooleanType))
      return false;
    return true;
  }

  public static String getComment(ConceptDefinitionComponent c) {
    return readStringExtension(c, EXT_COMMENT);    
  }

  public static Boolean getDeprecated(Element c) {
    return readBooleanExtension(c, EXT_DEPRECATED);    
  }

  public static boolean hasComment(ConceptDefinitionComponent c) {
    return findStringExtension(c, EXT_COMMENT);    
  }

  public static boolean hasDeprecated(Element c) {
    return findBooleanExtension(c, EXT_DEPRECATED);    
  }

  public static List<CodeType> getSubsumes(ConceptDefinitionComponent c) {
    List<CodeType> res = new ArrayList<CodeType>();

    for (Extension e : c.getExtension()) {
      if (EXT_SUBSUMES.equals(e.getUrl()))
        res.add((CodeType) e.getValue());
    }
    return res;
  }

  public static void addFlyOver(QuestionnaireItemComponent item, String text){
    if (!StringUtils.isBlank(text)) {
    	QuestionnaireItemComponent display = item.addItem();
    	display.setType(QuestionnaireItemType.DISPLAY);
    	display.setText(text);
    	display.getExtension().add(Factory.newExtension(EXT_CONTROL, Factory.newCodeableConcept("flyover", "http://hl7.org/fhir/questionnaire-item-control", "Fly-over"), true));
    }
  }

  public static void addMin(QuestionnaireItemComponent item, int min) {
    item.getExtension().add(Factory.newExtension(EXT_MINOCCURS, Factory.newInteger(min), true));
  }
  
  public static void addMax(QuestionnaireItemComponent item, int max) {
    item.getExtension().add(Factory.newExtension(EXT_MAXOCCURS, Factory.newInteger(max), true));
  }
  
  public static void addFhirType(QuestionnaireItemComponent group, String value) {
    group.getExtension().add(Factory.newExtension(EXT_FHIRTYPE, Factory.newString_(value), true));       
  }

  public static void addControl(QuestionnaireItemComponent group, String value) {
    group.getExtension().add(Factory.newExtension(EXT_CONTROL, Factory.newCodeableConcept(value, "http://hl7.org/fhir/questionnaire-item-control", value), true));
  }

  public static void addAllowedResource(QuestionnaireItemComponent group, String value) {
    group.getExtension().add(Factory.newExtension(EXT_ALLOWEDRESOURCE, Factory.newCode(value), true));       
  }

  public static void addReferenceFilter(QuestionnaireItemComponent group, String value) {
    group.getExtension().add(Factory.newExtension(EXT_REFERENCEFILTER, Factory.newString_(value), true));       
  }

  public static void addIdentifier(Element element, Identifier value) {
    element.getExtension().add(Factory.newExtension(EXT_IDENTIFIER, value, true));       
  }

  /**
   * @param name the identity of the extension of interest
   * @return The extension, if on this element, else null
   */
  public static Extension getExtension(DomainResource resource, String name) {
    if (name == null)
      return null;
    if (!resource.hasExtension())
      return null;
    for (Extension e : resource.getExtension()) {
      if (name.equals(e.getUrl()))
        return e;
    }
    return null;
  }

  public static Extension getExtension(Element el, String name) {
    if (name == null)
      return null;
    if (!el.hasExtension())
      return null;
    for (Extension e : el.getExtension()) {
      if (name.equals(e.getUrl()))
        return e;
    }
    return null;
  }

  public static void setStringExtension(DomainResource resource, String uri, String value) {
    Extension ext = getExtension(resource, uri);
    if (ext != null)
      ext.setValue(new StringType(value));
    else
      resource.getExtension().add(new Extension(new UriType(uri)).setValue(new StringType(value)));
  }

  public static String getOID(ValueSetCodeSystemComponent define) {
    return readStringExtension(define, EXT_OID);    
  }

  public static String getOID(ValueSet vs) {
    return readStringExtension(vs, EXT_OID);    
  }

  public static void setOID(ValueSetCodeSystemComponent define, String oid) throws FHIRFormatError, URISyntaxException {
    if (!oid.startsWith("urn:oid:"))
      throw new FHIRFormatError("Error in OID format");
    if (oid.startsWith("urn:oid:urn:oid:"))
      throw new FHIRFormatError("Error in OID format");
    define.getExtension().add(Factory.newExtension(EXT_OID, Factory.newUri(oid), false));       
  }
  public static void setOID(ValueSet vs, String oid) throws FHIRFormatError, URISyntaxException {
    if (!oid.startsWith("urn:oid:"))
      throw new FHIRFormatError("Error in OID format");
    if (oid.startsWith("urn:oid:urn:oid:"))
      throw new FHIRFormatError("Error in OID format");
    vs.getExtension().add(Factory.newExtension(EXT_OID, Factory.newUri(oid), false));       
  }

  public static boolean hasLanguageTranslation(Element element, String lang) {
    for (Extension e : element.getExtension()) {
      if (e.getUrl().equals(EXT_TRANSLATION)) {
        Extension e1 = ExtensionHelper.getExtension(e, "lang");

        if (e1 != null && e1.getValue() instanceof CodeType && ((CodeType) e.getValue()).getValue().equals(lang))
          return true;
      }
    }
    return false;
  }

  public static String getLanguageTranslation(Element element, String lang) {
    for (Extension e : element.getExtension()) {
      if (e.getUrl().equals(EXT_TRANSLATION)) {
        Extension e1 = ExtensionHelper.getExtension(e, "lang");

        if (e1 != null && e1.getValue() instanceof CodeType && ((CodeType) e.getValue()).getValue().equals(lang)) {
          e1 = ExtensionHelper.getExtension(e, "content");
          return ((StringType) e.getValue()).getValue();
        }
      }
    }
    return null;
  }

  public static void addLanguageTranslation(Element element, String lang, String value) {
    Extension extension = new Extension().setUrl(EXT_TRANSLATION);
    extension.addExtension().setUrl("lang").setValue(new StringType(lang));
    extension.addExtension().setUrl("content").setValue(new StringType(value));
    element.getExtension().add(extension);
  }

  public static Type getAllowedUnits(ElementDefinition eld) {
    for (Extension e : eld.getExtension()) 
      if (e.getUrl().equals(EXT_ALLOWABLE_UNITS)) 
        return e.getValue();
    return null;
  }

  public static void setAllowableUnits(ElementDefinition eld, CodeableConcept cc) {
    for (Extension e : eld.getExtension()) 
      if (e.getUrl().equals(EXT_ALLOWABLE_UNITS)) {
        e.setValue(cc);
        return;
      }
    eld.getExtension().add(new Extension().setUrl(EXT_ALLOWABLE_UNITS).setValue(cc));
  }

  public static List<Extension> getExtensions(Element element, String url) {
    List<Extension> results = new ArrayList<Extension>();
    for (Extension ex : element.getExtension())
      if (ex.getUrl().equals(url))
        results.add(ex);
    return results;
  }

  public static List<Extension> getExtensions(DomainResource resource, String url) {
    List<Extension> results = new ArrayList<Extension>();
    for (Extension ex : resource.getExtension())
      if (ex.getUrl().equals(url))
        results.add(ex);
    return results;
  }

  public static void addDEReference(DataElement de, String value) {
    for (Extension e : de.getExtension()) 
      if (e.getUrl().equals(EXT_CIMI_REFERENCE)) {
        e.setValue(new UriType(value));
        return;
      }
    de.getExtension().add(new Extension().setUrl(EXT_CIMI_REFERENCE).setValue(new UriType(value)));
  }

  public static void setDeprecated(Element nc) {
    for (Extension e : nc.getExtension()) 
      if (e.getUrl().equals(EXT_DEPRECATED)) {
        e.setValue(new BooleanType(true));
        return;
      }
    nc.getExtension().add(new Extension().setUrl(EXT_DEPRECATED).setValue(new BooleanType(true)));    
  }

  public static void setExtension(Element focus, String url, Coding c) {
    for (Extension e : focus.getExtension()) 
      if (e.getUrl().equals(url)) {
        e.setValue(c);
        return;
      }
    focus.getExtension().add(new Extension().setUrl(url).setValue(c));    
  }

  public static void removeExtension(DomainResource focus, String url) {
    Iterator<Extension> i = focus.getExtension().iterator();
    while (i.hasNext()) {
      Extension e = i.next(); // must be called before you can call i.remove()
      if (e.getUrl().equals(url)) {
        i.remove();
      }
    }
  }
  
  public static void removeExtension(Element focus, String url) {
    Iterator<Extension> i = focus.getExtension().iterator();
    while (i.hasNext()) {
      Extension e = i.next(); // must be called before you can call i.remove()
      if (e.getUrl().equals(url)) {
        i.remove();
      }
    }
  }
  
  
}
