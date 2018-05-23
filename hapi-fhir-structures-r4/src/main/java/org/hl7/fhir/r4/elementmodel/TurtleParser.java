package org.hl7.fhir.r4.elementmodel;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hl7.fhir.r4.context.IWorkerContext;
import org.hl7.fhir.r4.elementmodel.Element.SpecialElement;
import org.hl7.fhir.r4.formats.IParser.OutputStyle;
import org.hl7.fhir.r4.model.ElementDefinition.TypeRefComponent;
import org.hl7.fhir.r4.model.StructureDefinition;
import org.hl7.fhir.r4.utils.SnomedExpressions;
import org.hl7.fhir.r4.utils.SnomedExpressions.Expression;
import org.hl7.fhir.r4.utils.formats.Turtle;
import org.hl7.fhir.r4.utils.formats.Turtle.Complex;
import org.hl7.fhir.r4.utils.formats.Turtle.Section;
import org.hl7.fhir.r4.utils.formats.Turtle.Subject;
import org.hl7.fhir.r4.utils.formats.Turtle.TTLComplex;
import org.hl7.fhir.r4.utils.formats.Turtle.TTLList;
import org.hl7.fhir.r4.utils.formats.Turtle.TTLLiteral;
import org.hl7.fhir.r4.utils.formats.Turtle.TTLObject;
import org.hl7.fhir.r4.utils.formats.Turtle.TTLURL;
import org.hl7.fhir.exceptions.DefinitionException;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.exceptions.FHIRFormatError;
import org.hl7.fhir.utilities.TextFile;
import org.hl7.fhir.utilities.Utilities;
import org.hl7.fhir.utilities.validation.ValidationMessage.IssueSeverity;
import org.hl7.fhir.utilities.validation.ValidationMessage.IssueType;


public class TurtleParser extends ParserBase {

  private String base;

  public static String FHIR_URI_BASE = "http://hl7.org/fhir/";
  public static String FHIR_VERSION_BASE = "http://build.fhir.org/";

  public TurtleParser(IWorkerContext context) {
    super(context);
  }
  @Override
  public Element parse(InputStream input) throws IOException, FHIRException {
    Turtle src = new Turtle();
    if (policy == ValidationPolicy.EVERYTHING) {
      try {
        src.parse(TextFile.streamToString(input));
      } catch (Exception e) {  
        logError(-1, -1, "(document)", IssueType.INVALID, "Error parsing Turtle: "+e.getMessage(), IssueSeverity.FATAL);
        return null;
      }
      return parse(src);  
    } else {
    src.parse(TextFile.streamToString(input));
      return parse(src);  
    } 
  }
  
  private Element parse(Turtle src) throws FHIRException {
    // we actually ignore the stated URL here
    for (TTLComplex cmp : src.getObjects().values()) {
      for (String p : cmp.getPredicates().keySet()) {
        if ((FHIR_URI_BASE + "nodeRole").equals(p) && cmp.getPredicates().get(p).hasValue(FHIR_URI_BASE + "treeRoot")) {
          return parse(src, cmp);
        }
      }
    }
    // still here: well, we didn't find a start point
    String msg = "Error parsing Turtle: unable to find any node maked as the entry point (where " + FHIR_URI_BASE + "nodeRole = " + FHIR_URI_BASE + "treeRoot)";
    if (policy == ValidationPolicy.EVERYTHING) {
      logError(-1, -1, "(document)", IssueType.INVALID, msg, IssueSeverity.FATAL);
      return null;
    } else {
      throw new FHIRFormatError(msg);
    } 
  }
  
  private Element parse(Turtle src, TTLComplex cmp) throws FHIRException {
    TTLObject type = cmp.getPredicates().get("http://www.w3.org/2000/01/rdf-schema#type");
    if (type == null) {
      logError(cmp.getLine(), cmp.getCol(), "(document)", IssueType.INVALID, "Unknown resource type (missing rdfs:type)", IssueSeverity.FATAL);
      return null;
    }
    if (type instanceof TTLList) {
      // this is actually broken - really we have to look through the structure definitions at this point
      for (TTLObject obj : ((TTLList) type).getList()) {
        if (obj instanceof TTLURL && ((TTLURL) obj).getUri().startsWith(FHIR_URI_BASE)) {
          type = obj;
          break;
        }
      }
    }
    if (!(type instanceof TTLURL)) {
      logError(cmp.getLine(), cmp.getCol(), "(document)", IssueType.INVALID, "Unexpected datatype for rdfs:type)", IssueSeverity.FATAL);
      return null;
    }
    String name = ((TTLURL) type).getUri();
    String ns = name.substring(0, name.lastIndexOf("/"));
    name = name.substring(name.lastIndexOf("/")+1);
    String path = "/"+name;

    StructureDefinition sd = getDefinition(cmp.getLine(), cmp.getCol(), ns, name);
    if (sd == null)
      return null;

    Element result = new Element(name, new Property(context, sd.getSnapshot().getElement().get(0), sd));
    result.markLocation(cmp.getLine(), cmp.getCol());
    result.setType(name);
    parseChildren(src, path, cmp, result, false);
    result.numberChildren();
    return result;  
  }
  
  private void parseChildren(Turtle src, String path, TTLComplex object, Element context, boolean primitive) throws FHIRException {

    List<Property> properties = context.getProperty().getChildProperties(context.getName(), null);
    Set<String> processed = new HashSet<String>();
    if (primitive)
      processed.add(FHIR_URI_BASE + "value");

    // note that we do not trouble ourselves to maintain the wire format order here - we don't even know what it was anyway
    // first pass: process the properties
    for (Property property : properties) {
      if (property.isChoice()) {
        for (TypeRefComponent type : property.getDefinition().getType()) {
          String eName = property.getName().substring(0, property.getName().length()-3) + Utilities.capitalize(type.getCode());
          parseChild(src, object, context, processed, property, path, getFormalName(property, eName));
        }
      } else  {
        parseChild(src, object, context, processed, property, path, getFormalName(property));
      } 
    }

    // second pass: check for things not processed
    if (policy != ValidationPolicy.NONE) {
      for (String u : object.getPredicates().keySet()) {
        if (!processed.contains(u)) {
          TTLObject n = object.getPredicates().get(u);
          logError(n.getLine(), n.getCol(), path, IssueType.STRUCTURE, "Unrecognised predicate '"+u+"'", IssueSeverity.ERROR);         
        }
      }
    }
  }
  
  private void parseChild(Turtle src, TTLComplex object, Element context, Set<String> processed, Property property, String path, String name) throws FHIRException {
    processed.add(name);
    String npath = path+"/"+property.getName();
    TTLObject e = object.getPredicates().get(FHIR_URI_BASE + name);
    if (e == null)
      return;
    if (property.isList() && (e instanceof TTLList)) {
      TTLList arr = (TTLList) e;
      for (TTLObject am : arr.getList()) {
        parseChildInstance(src, npath, object, context, property, name, am);
      }
    } else {
      parseChildInstance(src, npath, object, context, property, name, e);
    }
  }

  private void parseChildInstance(Turtle src, String npath, TTLComplex object, Element context, Property property, String name, TTLObject e) throws FHIRException {
    if (property.isResource())
      parseResource(src, npath, object, context, property, name, e);
    else  if (e instanceof TTLComplex) {
      TTLComplex child = (TTLComplex) e;
      Element n = new Element(tail(name), property).markLocation(e.getLine(), e.getCol());
      context.getChildren().add(n);
      if (property.isPrimitive(property.getType(tail(name)))) {
        parseChildren(src, npath, child, n, true);
        TTLObject val = child.getPredicates().get(FHIR_URI_BASE + "value");
        if (val != null) {
          if (val instanceof TTLLiteral) {
            String value = ((TTLLiteral) val).getValue();
            String type = ((TTLLiteral) val).getType();
            // todo: check type
            n.setValue(value);
          } else
            logError(object.getLine(), object.getCol(), npath, IssueType.INVALID, "This property must be a Literal, not a "+e.getClass().getName(), IssueSeverity.ERROR);
        }
      } else 
        parseChildren(src, npath, child, n, false);

    } else 
      logError(object.getLine(), object.getCol(), npath, IssueType.INVALID, "This property must be a URI or bnode, not a "+e.getClass().getName(), IssueSeverity.ERROR);
  }


  private String tail(String name) {
    return name.substring(name.lastIndexOf(".")+1);
  }

  private void parseResource(Turtle src, String npath, TTLComplex object, Element context, Property property, String name, TTLObject e) throws FHIRException {
    TTLComplex obj;
    if (e instanceof TTLComplex) 
      obj = (TTLComplex) e;
    else if (e instanceof TTLURL) {
      String url = ((TTLURL) e).getUri();
      obj = src.getObject(url);
      if (obj == null) {
        logError(e.getLine(), e.getCol(), npath, IssueType.INVALID, "reference to "+url+" cannot be resolved", IssueSeverity.FATAL);
        return;
      }
    } else
      throw new FHIRFormatError("Wrong type for resource");
      
    TTLObject type = obj.getPredicates().get("http://www.w3.org/2000/01/rdf-schema#type");
    if (type == null) {
      logError(object.getLine(), object.getCol(), npath, IssueType.INVALID, "Unknown resource type (missing rdfs:type)", IssueSeverity.FATAL);
      return;
  }
    if (type instanceof TTLList) {
      // this is actually broken - really we have to look through the structure definitions at this point
      for (TTLObject tobj : ((TTLList) type).getList()) {
        if (tobj instanceof TTLURL && ((TTLURL) tobj).getUri().startsWith(FHIR_URI_BASE)) {
          type = tobj;
          break;
        }
      }
    }
    if (!(type instanceof TTLURL)) {
      logError(object.getLine(), object.getCol(), npath, IssueType.INVALID, "Unexpected datatype for rdfs:type)", IssueSeverity.FATAL);
      return;
    }
    String rt = ((TTLURL) type).getUri();
    String ns = rt.substring(0, rt.lastIndexOf("/"));
    rt = rt.substring(rt.lastIndexOf("/")+1);
    
    StructureDefinition sd = getDefinition(object.getLine(), object.getCol(), ns, rt);
    if (sd == null)
      return;
    
    Element n = new Element(tail(name), property).markLocation(object.getLine(), object.getCol());
    context.getChildren().add(n);
    n.updateProperty(new Property(this.context, sd.getSnapshot().getElement().get(0), sd), SpecialElement.fromProperty(n.getProperty()), property);
    n.setType(rt);
    parseChildren(src, npath, obj, n, false);
  }
  
  private String getFormalName(Property property) {
    String en = property.getDefinition().getBase().getPath();
    if (en == null) 
      en = property.getDefinition().getPath();
//    boolean doType = false;
//      if (en.endsWith("[x]")) {
//        en = en.substring(0, en.length()-3);
//        doType = true;        
//      }
//     if (doType || (element.getProperty().getDefinition().getType().size() > 1 && !allReference(element.getProperty().getDefinition().getType())))
//       en = en + Utilities.capitalize(element.getType());
    return en;
  }
  
  private String getFormalName(Property property, String elementName) {
    String en = property.getDefinition().getBase().getPath();
    if (en == null)
      en = property.getDefinition().getPath();
    if (!en.endsWith("[x]")) 
      throw new Error("Attempt to replace element name for a non-choice type");
    return en.substring(0, en.lastIndexOf(".")+1)+elementName;
  }
  
  
  @Override
  public void compose(Element e, OutputStream stream, OutputStyle style, String base) throws IOException, FHIRException {
    this.base = base;
    
		Turtle ttl = new Turtle();
		compose(e, ttl, base);
		ttl.commit(stream, false);
  }



  public void compose(Element e, Turtle ttl, String base) throws FHIRException {
    ttl.prefix("fhir", FHIR_URI_BASE);
    ttl.prefix("rdfs", "http://www.w3.org/2000/01/rdf-schema#");
    ttl.prefix("owl", "http://www.w3.org/2002/07/owl#");
    ttl.prefix("xsd", "http://www.w3.org/2001/XMLSchema#");


    Section section = ttl.section("resource");
    String subjId = genSubjectId(e);

    String ontologyId = subjId.replace(">", ".ttl>");
    Section ontology = ttl.section("ontology header");
    ontology.triple(ontologyId, "a", "owl:Ontology");
    ontology.triple(ontologyId, "owl:imports", "fhir:fhir.ttl");
    if(ontologyId.startsWith("<" + FHIR_URI_BASE))
      ontology.triple(ontologyId, "owl:versionIRI", ontologyId.replace(FHIR_URI_BASE, FHIR_VERSION_BASE));

    Subject subject = section.triple(subjId, "a", "fhir:" + e.getType());
		subject.linkedPredicate("fhir:nodeRole", "fhir:treeRoot", linkResolver == null ? null : linkResolver.resolvePage("rdf.html#tree-root"));

		for (Element child : e.getChildren()) {
			composeElement(section, subject, child, null);
		}

  }
  
  protected String getURIType(String uri) {
    if(uri.startsWith("<" + FHIR_URI_BASE))
      if(uri.substring(FHIR_URI_BASE.length() + 1).contains("/"))
        return uri.substring(FHIR_URI_BASE.length() + 1, uri.indexOf('/', FHIR_URI_BASE.length() + 1));
    return null;
  }

  protected String getReferenceURI(String ref) {
    if (ref != null && (ref.startsWith("http://") || ref.startsWith("https://")))
      return "<" + ref + ">";
    else if (base != null && ref != null && ref.contains("/"))
      return "<" + Utilities.appendForwardSlash(base) + ref + ">";
    else
      return null;
    }

  protected void decorateReference(Complex t, Element coding) {
    String refURI = getReferenceURI(coding.getChildValue("reference"));
    if(refURI != null)
      t.linkedPredicate("fhir:link", refURI, linkResolver == null ? null : linkResolver.resolvePage("rdf.html#reference"));
  }
  
  protected void decorateCanonical(Complex t, Element canonical) {
    String refURI = getReferenceURI(canonical.primitiveValue());
    if(refURI != null)
      t.linkedPredicate("fhir:link", refURI, linkResolver == null ? null : linkResolver.resolvePage("rdf.html#reference"));
  }
  
  private String genSubjectId(Element e) {
    String id = e.getChildValue("id");
    if (base == null || id == null)
      return "";
    else if (base.endsWith("#"))
      return "<" + base + e.getType() + "-" + id + ">";
    else
      return "<" + Utilities.pathURL(base, e.getType(), id) + ">";
  }

	private String urlescape(String s) {
	  StringBuilder b = new StringBuilder();
	  for (char ch : s.toCharArray()) {
	    if (Utilities.charInSet(ch,  ':', ';', '=', ','))
	      b.append("%"+Integer.toHexString(ch));
	    else
	      b.append(ch);
	  }
	  return b.toString();
  }

  private void composeElement(Section section, Complex ctxt, Element element, Element parent) throws FHIRException {
//    "Extension".equals(element.getType())?
//            (element.getProperty().getDefinition().getIsModifier()? "modifierExtension" : "extension") ; 
    String en = getFormalName(element);

	  Complex t;
	  if (element.getSpecial() == SpecialElement.BUNDLE_ENTRY && parent != null && parent.getNamedChildValue("fullUrl") != null) {
	    String url = "<"+parent.getNamedChildValue("fullUrl")+">";
	    ctxt.linkedPredicate("fhir:"+en, url, linkResolver == null ? null : linkResolver.resolveProperty(element.getProperty()));
	    t = section.subject(url);
	  } else {
	    t = ctxt.linkedPredicate("fhir:"+en, linkResolver == null ? null : linkResolver.resolveProperty(element.getProperty()));
	  }
    if (element.getSpecial() != null)
      t.linkedPredicate("a", "fhir:"+element.fhirType(), linkResolver == null ? null : linkResolver.resolveType(element.fhirType()));
	  if (element.hasValue())
	  	t.linkedPredicate("fhir:value", ttlLiteral(element.getValue(), element.getType()), linkResolver == null ? null : linkResolver.resolveType(element.getType()));
	  if (element.getProperty().isList() && (!element.isResource() || element.getSpecial() == SpecialElement.CONTAINED))
	  	t.linkedPredicate("fhir:index", Integer.toString(element.getIndex()), linkResolver == null ? null : linkResolver.resolvePage("rdf.html#index"));

	  if ("Coding".equals(element.getType()))
	  	decorateCoding(t, element, section);
    if (Utilities.existsInList(element.getType(), "Reference"))
      decorateReference(t, element);
    else if (Utilities.existsInList(element.getType(), "canonical"))
      decorateCanonical(t, element);
	  		
    if("canonical".equals(element.getType())) {
      String refURI = element.primitiveValue();
      if (refURI != null) {
        String uriType = getURIType(refURI);
        if(uriType != null && !section.hasSubject(refURI))
          section.triple(refURI, "a", "fhir:" + uriType);
      }
    }

    if("Reference".equals(element.getType())) {
      String refURI = getReferenceURI(element.getChildValue("reference"));
      if (refURI != null) {
        String uriType = getURIType(refURI);
        if(uriType != null && !section.hasSubject(refURI))
          section.triple(refURI, "a", "fhir:" + uriType);
      }
    }

		for (Element child : element.getChildren()) {
      if ("xhtml".equals(child.getType())) {
        String childfn = getFormalName(child);
        t.predicate("fhir:" + childfn, ttlLiteral(child.getValue(), child.getType()));
      } else
			composeElement(section, t, child, element);
		}
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

  static public String ttlLiteral(String value, String type) {
	  String xst = "";
	  if (type.equals("boolean"))
	    xst = "^^xsd:boolean";
    else if (type.equals("integer"))
      xst = "^^xsd:integer";
    else if (type.equals("unsignedInt"))
      xst = "^^xsd:nonNegativeInteger";
    else if (type.equals("positiveInt"))
      xst = "^^xsd:positiveInteger";
    else if (type.equals("decimal"))
      xst = "^^xsd:decimal";
    else if (type.equals("base64Binary"))
      xst = "^^xsd:base64Binary";
    else if (type.equals("instant"))
      xst = "^^xsd:dateTime";
    else if (type.equals("time"))
      xst = "^^xsd:time";
    else if (type.equals("date") || type.equals("dateTime") ) {
      String v = value;
      if (v.length() > 10) {
        int i = value.substring(10).indexOf("-");
        if (i == -1)
          i = value.substring(10).indexOf("+");
        v = i == -1 ? value : value.substring(0,  10+i);
      }
      if (v.length() > 10)
        xst = "^^xsd:dateTime";
      else if (v.length() == 10)
        xst = "^^xsd:date";
      else if (v.length() == 7)
        xst = "^^xsd:gYearMonth";
      else if (v.length() == 4)
        xst = "^^xsd:gYear";
    }
	  
		return "\"" +Turtle.escape(value, true) + "\""+xst;
	}

  protected void decorateCoding(Complex t, Element coding, Section section) throws FHIRException {
    String system = coding.getChildValue("system");
    String code = coding.getChildValue("code");
    
    if (system == null)
      return;
    if ("http://snomed.info/sct".equals(system)) {
      t.prefix("sct", "http://snomed.info/id/");
      if (code.contains(":") || code.contains("="))
        generateLinkedPredicate(t, code);
      else
        t.linkedPredicate("a", "sct:" + urlescape(code), null);
    } else if ("http://loinc.org".equals(system)) {
      t.prefix("loinc", "http://loinc.org/rdf#");
      t.linkedPredicate("a", "loinc:"+urlescape(code).toUpperCase(), null);
    }  
  }
  private void generateLinkedPredicate(Complex t, String code) throws FHIRException {
    Expression expression = SnomedExpressions.parse(code);
    
  }


//    128045006|cellulitis (disorder)|:{363698007|finding site|=56459004|foot structure|}
//    Grahame Grieve: or
//
//    64572001|disease|:{116676008|associated morphology|=72704001|fracture|,363698007|finding site|=(12611008|bone structure of  tibia|:272741003|laterality|=7771000|left|)}
//    Harold Solbrig:
//    a sct:128045006,
//      rdfs:subClassOf [
//          a owl:Restriction;
//          owl:onProperty sct:609096000 ;
//          owl:someValuesFrom [
//                a owl:Restriction;
//                 owl:onProperty sct:363698007 ;
//                owl:someValuesFrom sct:56459004 ] ] ;
//    and
//
//    a sct:64572001,
//       rdfs:subclassOf  [
//           a owl:Restriction ;
//           owl:onProperty sct:60909600 ;
//           owl:someValuesFrom [ 
//                 a owl:Class ;
//                 owl:intersectionOf ( [
//                      a owl:Restriction;
//                      owl:onProperty sct:116676008;
//                     owl:someValuesFrom sct:72704001 ] 
//                 [  a owl:Restriction;
//                      owl:onProperty sct:363698007 
//                      owl:someValuesFrom [
//                            a owl:Class ;
//                            owl:intersectionOf(
//                                 sct:12611008
//                                 owl:someValuesFrom [
//                                         a owl:Restriction;
//                                         owl:onProperty sct:272741003;
//                                         owl:someValuesFrom sct:7771000
//                                  ] ) ] ] ) ] ]
//    (an approximation -- I'll have to feed it into a translator to be sure I've got it 100% right)
//
  
}
