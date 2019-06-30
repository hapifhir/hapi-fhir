package org.hl7.fhir.r4.conformance;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.r4.context.IWorkerContext;
import org.hl7.fhir.r4.elementmodel.TurtleParser;
import org.hl7.fhir.r4.model.DomainResource;
import org.hl7.fhir.r4.model.ElementDefinition;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.Resource;
import org.hl7.fhir.r4.model.StructureDefinition;
import org.hl7.fhir.r4.model.Type;
import org.hl7.fhir.r4.model.UriType;
import org.hl7.fhir.r4.model.ValueSet;
import org.hl7.fhir.r4.terminologies.ValueSetExpander;
import org.hl7.fhir.r4.utils.ToolingExtensions;
import org.stringtemplate.v4.ST;

public class ShExGenerator {

  public enum HTMLLinkPolicy {
    NONE, EXTERNAL, INTERNAL
  }
  public boolean doDatatypes = true;                 // add data types
  public boolean withComments = true;                // include comments
  public boolean completeModel = false;              // doing complete build (fhir.shex)


  private static String SHEX_TEMPLATE = "$header$\n\n" +
          "$shapeDefinitions$";

  // A header is a list of prefixes, a base declaration and a start node
  private static String FHIR = "http://hl7.org/fhir/";
  private static String FHIR_VS = FHIR + "ValueSet/";
  private static String HEADER_TEMPLATE =
          "PREFIX fhir: <$fhir$> \n" +
                  "PREFIX fhirvs: <$fhirvs$>\n" +
                  "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> \n" +
                  "BASE <http://hl7.org/fhir/shape/>\n$start$";

  // Start template for single (open) entry
  private static String START_TEMPLATE = "\n\nstart=@<$id$> AND {fhir:nodeRole [fhir:treeRoot]}\n";

  // Start template for complete (closed) model
  private static String ALL_START_TEMPLATE = "\n\nstart=@<All>\n";

  private static String ALL_TEMPLATE = "\n<All> $all_entries$\n";

  private static String ALL_ENTRY_TEMPLATE = "(NOT { fhir:nodeRole [fhir:treeRoot] ; a [fhir:$id$] } OR @<$id$>)";


  // Shape Definition
  //      the shape name
  //      an optional resource declaration (type + treeRoot)
  //      the list of element declarations
  //      an optional index element (for appearances inside ordered lists)
  private static String SHAPE_DEFINITION_TEMPLATE =
          "$comment$\n<$id$> CLOSED {\n    $resourceDecl$" +
                  "\n    $elements$" +
                  "\n    fhir:index xsd:integer?                 # Relative position in a list\n}\n";

  // Resource Definition
  //      an open shape of type Resource.  Used when completeModel = false.
  private static String RESOURCE_SHAPE_TEMPLATE =
          "$comment$\n<Resource> {a .+;" +
                  "\n    $elements$" +
                  "\n    fhir:index xsd:integer?" +
                  "\n}\n";

  // If we have knowledge of all of the possible resources available to us (completeModel = true), we can build
  // a model of all possible resources.
  private static String COMPLETE_RESOURCE_TEMPLATE =
          "<Resource>  @<$resources$>" +
                  "\n\n";

  // Resource Declaration
  //      a type node
  //      an optional treeRoot declaration (identifies the entry point)
  private static String RESOURCE_DECL_TEMPLATE = "\na [fhir:$id$];$root$";

  // Root Declaration.
  private static String ROOT_TEMPLATE = "\nfhir:nodeRole [fhir:treeRoot]?;";

  // Element
  //    a predicate, type and cardinality triple
  private static String ELEMENT_TEMPLATE = "$id$$defn$$card$$comment$";
  private static int COMMENT_COL = 40;
  private static int MAX_CHARS = 35;
  private static int MIN_COMMENT_SEP = 2;

  // Inner Shape Definition
  private static String INNER_SHAPE_TEMPLATE = "($comment$\n    $defn$\n)$card$";

  // Simple Element
  //    a shape reference
  private static String SIMPLE_ELEMENT_DEFN_TEMPLATE = "@<$typ$>$vsdef$";

  // Value Set Element
  private static String VALUESET_DEFN_TEMPLATE = " AND\n\t{fhir:value @$vsn$}";

  // Fixed Value Template
  private static String FIXED_VALUE_TEMPLATE = " AND\n\t{fhir:value [\"$val$\"]}";

  // A primitive element definition
  //    the actual type reference
  private static String PRIMITIVE_ELEMENT_DEFN_TEMPLATE = "$typ$$facets$";

  // Facets
  private static String MINVALUE_TEMPLATE = " MININCLUSIVE $val$";
  private static String MAXVALUE_TEMPLATE = " MAXINCLUSIVE $val$";
  private static String MAXLENGTH_TEMPLATE = " MAXLENGTH $val$";
  private static String PATTERN_TEMPLATE = " PATTERN \"$val$\"";

  // A choice of alternative shape definitions
  //  rendered as an inner anonymous shape
  private static String ALTERNATIVE_SHAPES_TEMPLATE = "fhir:$id$$comment$\n(   $altEntries$\n)$card$";

  // A typed reference definition
  private static String REFERENCE_DEFN_TEMPLATE = "@<$ref$Reference>";

  // What we emit for an xhtml
  private static String XHTML_TYPE_TEMPLATE = "xsd:string";

  // Additional type for Coding
  private static String CONCEPT_REFERENCE_TEMPLATE = "a NONLITERAL?;";

  // Additional type for CodedConcept
  private static String CONCEPT_REFERENCES_TEMPLATE = "a NONLITERAL*;";

  // Untyped resource has the extra link entry
  private static String RESOURCE_LINK_TEMPLATE = "fhir:link IRI?;";

  // Extension template
  // No longer used -- we emit the actual definition
//  private static String EXTENSION_TEMPLATE = "<Extension> {fhir:extension @<Extension>*;" +
//          "\n    fhir:index xsd:integer?" +
//          "\n}\n";

  // A typed reference -- a fhir:uri with an optional type and the possibility of a resolvable shape
  private static String TYPED_REFERENCE_TEMPLATE = "\n<$refType$Reference> CLOSED {" +
          "\n    fhir:Element.id @<id>?;" +
          "\n    fhir:Element.extension @<Extension>*;" +
          "\n    fhir:link @<$refType$> OR CLOSED {a [fhir:$refType$]}?;" +
          "\n    fhir:Reference.reference @<string>?;" +
          "\n    fhir:Reference.display @<string>?;" +
          "\n    fhir:index xsd:integer?" +
          "\n}";

  private static String TARGET_REFERENCE_TEMPLATE = "\n<$refType$> {" +
          "\n    a [fhir:$refType$];" +
          "\n    fhir:nodeRole [fhir:treeRoot]?" +
          "\n}";

  // A value set definition
  private static String VALUE_SET_DEFINITION = "# $comment$\n$vsuri$$val_list$\n";


  /**
   * this makes internal metadata services available to the generator - retrieving structure definitions, and value set expansion etc
   */
  private IWorkerContext context;

  /**
   * innerTypes -- inner complex types.  Currently flattened in ShEx (doesn't have to be, btw)
   * emittedInnerTypes -- set of inner types that have been generated
   * datatypes, emittedDatatypes -- types used in the definition, types that have been generated
   * references -- Reference types (Patient, Specimen, etc)
   * uniq_structures -- set of structures on the to be generated list...
   * doDataTypes -- whether or not to emit the data types.
   */
  private HashSet<Pair<StructureDefinition, ElementDefinition>> innerTypes, emittedInnerTypes;
  private HashSet<String> datatypes, emittedDatatypes;
  private HashSet<String> references;
  private LinkedList<StructureDefinition> uniq_structures;
  private HashSet<String> uniq_structure_urls;
  private HashSet<ValueSet> required_value_sets;
  private HashSet<String> known_resources;          // Used when generating a full definition

  public ShExGenerator(IWorkerContext context) {
    super();
    this.context = context;
    innerTypes = new HashSet<Pair<StructureDefinition, ElementDefinition>>();
    emittedInnerTypes = new HashSet<Pair<StructureDefinition, ElementDefinition>>();
    datatypes = new HashSet<String>();
    emittedDatatypes = new HashSet<String>();
    references = new HashSet<String>();
    required_value_sets = new HashSet<ValueSet>();
    known_resources = new HashSet<String>();
  }

  public String generate(HTMLLinkPolicy links, StructureDefinition structure) {
    List<StructureDefinition> list = new ArrayList<StructureDefinition>();
    list.add(structure);
    innerTypes.clear();
    emittedInnerTypes.clear();
    datatypes.clear();
    emittedDatatypes.clear();
    references.clear();
    required_value_sets.clear();
    known_resources.clear();
    return generate(links, list);
  }

  public class SortById implements Comparator<StructureDefinition> {

    @Override
    public int compare(StructureDefinition arg0, StructureDefinition arg1) {
      return arg0.getId().compareTo(arg1.getId());
    }

  }

  private ST tmplt(String template) {
    return new ST(template, '$', '$');
  }

  /**
   * this is called externally to generate a set of structures to a single ShEx file
   * generally, it will be called with a single structure, or a long list of structures (all of them)
   *
   * @param links HTML link rendering policy
   * @param structures list of structure definitions to render
   * @return ShEx definition of structures
   */
  public String generate(HTMLLinkPolicy links, List<StructureDefinition> structures) {

    ST shex_def = tmplt(SHEX_TEMPLATE);
    String start_cmd;
    if(completeModel || structures.get(0).getKind().equals(StructureDefinition.StructureDefinitionKind.RESOURCE))
//            || structures.get(0).getKind().equals(StructureDefinition.StructureDefinitionKind.COMPLEXTYPE))
      start_cmd = completeModel? tmplt(ALL_START_TEMPLATE).render() :
              tmplt(START_TEMPLATE).add("id", structures.get(0).getId()).render();
    else
      start_cmd = "";
    shex_def.add("header", tmplt(HEADER_TEMPLATE).
            add("start", start_cmd).
            add("fhir", FHIR).
            add("fhirvs", FHIR_VS).render());

    Collections.sort(structures, new SortById());
    StringBuilder shapeDefinitions = new StringBuilder();

    // For unknown reasons, the list of structures carries duplicates.  We remove them
    // Also, it is possible for the same sd to have multiple hashes...
    uniq_structures = new LinkedList<StructureDefinition>();
    uniq_structure_urls = new HashSet<String>();
    for (StructureDefinition sd : structures) {
      if (!uniq_structure_urls.contains(sd.getUrl())) {
        uniq_structures.add(sd);
        uniq_structure_urls.add(sd.getUrl());
      }
    }


    for (StructureDefinition sd : uniq_structures) {
      shapeDefinitions.append(genShapeDefinition(sd, true));
    }

    shapeDefinitions.append(emitInnerTypes());
    if(doDatatypes) {
      shapeDefinitions.append("\n#---------------------- Data Types -------------------\n");
      while (emittedDatatypes.size() < datatypes.size() ||
              emittedInnerTypes.size() < innerTypes.size()) {
        shapeDefinitions.append(emitDataTypes());
        shapeDefinitions.append(emitInnerTypes());
      }
    }

    shapeDefinitions.append("\n#---------------------- Reference Types -------------------\n");
    for(String r: references) {
      shapeDefinitions.append("\n").append(tmplt(TYPED_REFERENCE_TEMPLATE).add("refType", r).render()).append("\n");
      if (!"Resource".equals(r) && !known_resources.contains(r))
        shapeDefinitions.append("\n").append(tmplt(TARGET_REFERENCE_TEMPLATE).add("refType", r).render()).append("\n");
    }
    shex_def.add("shapeDefinitions", shapeDefinitions);

    if(completeModel && known_resources.size() > 0) {
      shapeDefinitions.append("\n").append(tmplt(COMPLETE_RESOURCE_TEMPLATE)
              .add("resources", StringUtils.join(known_resources, "> OR\n\t@<")).render());
      List<String> all_entries = new ArrayList<String>();
      for(String kr: known_resources)
        all_entries.add(tmplt(ALL_ENTRY_TEMPLATE).add("id", kr).render());
      shapeDefinitions.append("\n").append(tmplt(ALL_TEMPLATE)
              .add("all_entries", StringUtils.join(all_entries, " OR\n\t")).render());
    }

    shapeDefinitions.append("\n#---------------------- Value Sets ------------------------\n");
    for(ValueSet vs: required_value_sets)
      shapeDefinitions.append("\n").append(genValueSet(vs));
    return shex_def.render();
  }


  /**
   * Emit a ShEx definition for the supplied StructureDefinition
   * @param sd Structure definition to emit
   * @param top_level True means outermost type, False means recursively called
   * @return ShEx definition
   */
  private String genShapeDefinition(StructureDefinition sd, boolean top_level) {
    // xhtml is treated as an atom
    if("xhtml".equals(sd.getName()) || (completeModel && "Resource".equals(sd.getName())))
      return "";

    ST shape_defn;
    // Resources are either incomplete items or consist of everything that is defined as a resource (completeModel)
    if("Resource".equals(sd.getName())) {
      shape_defn = tmplt(RESOURCE_SHAPE_TEMPLATE);
      known_resources.add(sd.getName());
    } else {
      shape_defn = tmplt(SHAPE_DEFINITION_TEMPLATE).add("id", sd.getId());
      if (sd.getKind().equals(StructureDefinition.StructureDefinitionKind.RESOURCE)) {
//              || sd.getKind().equals(StructureDefinition.StructureDefinitionKind.COMPLEXTYPE)) {
        known_resources.add(sd.getName());
        ST resource_decl = tmplt(RESOURCE_DECL_TEMPLATE).
                add("id", sd.getId()).
                add("root", tmplt(ROOT_TEMPLATE));
//                add("root", top_level ? tmplt(ROOT_TEMPLATE) : "");
        shape_defn.add("resourceDecl", resource_decl.render());
      } else {
        shape_defn.add("resourceDecl", "");
      }
    }

    // Generate the defining elements
    List<String> elements = new ArrayList<String>();

    // Add the additional entries for special types
    String sdn = sd.getName();
    if (sdn.equals("Coding"))
      elements.add(tmplt(CONCEPT_REFERENCE_TEMPLATE).render());
    else if (sdn.equals("CodeableConcept"))
      elements.add(tmplt(CONCEPT_REFERENCES_TEMPLATE).render());
    else if (sdn.equals("Reference"))
      elements.add(tmplt(RESOURCE_LINK_TEMPLATE).render());
//    else if (sdn.equals("Extension"))
//      return tmplt(EXTENSION_TEMPLATE).render();

    String root_comment = null;
    for (ElementDefinition ed : sd.getSnapshot().getElement()) {
      if(!ed.getPath().contains("."))
        root_comment = ed.getShort();
      else if (StringUtils.countMatches(ed.getPath(), ".") == 1 && !"0".equals(ed.getMax())) {
        elements.add(genElementDefinition(sd, ed));
      }
    }
    shape_defn.add("elements", StringUtils.join(elements, "\n"));
    shape_defn.add("comment", root_comment == null? " " : "# " + root_comment);
    return shape_defn.render();
  }


  /**
   * Generate a flattened definition for the inner types
   * @return stringified inner type definitions
   */
  private String emitInnerTypes() {
    StringBuilder itDefs = new StringBuilder();
    while(emittedInnerTypes.size() < innerTypes.size()) {
      for (Pair<StructureDefinition, ElementDefinition> it : new HashSet<Pair<StructureDefinition, ElementDefinition>>(innerTypes)) {
        if (!emittedInnerTypes.contains(it)) {
          itDefs.append("\n").append(genInnerTypeDef(it.getLeft(), it.getRight()));
          emittedInnerTypes.add(it);
        }
      }
    }
    return itDefs.toString();
  }

  /**
   * Generate a shape definition for the current set of datatypes
   * @return stringified data type definitions
   */
  private String emitDataTypes() {
    StringBuilder dtDefs = new StringBuilder();
    while (emittedDatatypes.size() < datatypes.size()) {
      for (String dt : new HashSet<String>(datatypes)) {
        if (!emittedDatatypes.contains(dt)) {
          StructureDefinition sd = context.fetchResource(StructureDefinition.class,
              ProfileUtilities.sdNs(dt, null));
          // TODO: Figure out why the line below doesn't work
          // if (sd != null && !uniq_structures.contains(sd))
          if(sd != null && !uniq_structure_urls.contains(sd.getUrl()))
            dtDefs.append("\n").append(genShapeDefinition(sd, false));
          emittedDatatypes.add(dt);
        }
      }
    }
    return dtDefs.toString();
  }

  private ArrayList<String> split_text(String text, int max_col) {
    int pos = 0;
    ArrayList<String> rval = new ArrayList<String>();
    if (text.length() <= max_col) {
      rval.add(text);
    } else {
      String[] words = text.split(" ");
      int word_idx = 0;
      while(word_idx < words.length) {
        StringBuilder accum = new StringBuilder();
        while (word_idx < words.length && accum.length() + words[word_idx].length() < max_col)
          accum.append(words[word_idx++] + " ");
        if (accum.length() == 0) {
          accum.append(words[word_idx].substring(0, max_col - 3) + "-");
          words[word_idx] = words[word_idx].substring(max_col - 3);
        }
        rval.add(accum.toString());
        accum = new StringBuilder();
      }
    }
    return rval;
  }

  private void addComment(ST tmplt, ElementDefinition ed) {
    if(withComments && ed.hasShort() && !ed.getId().startsWith("Extension.")) {
      int nspaces;
      char[] sep;
      nspaces = Integer.max(COMMENT_COL - tmplt.add("comment", "#").render().indexOf('#'), MIN_COMMENT_SEP);
      tmplt.remove("comment");
      sep = new char[nspaces];
      Arrays.fill(sep, ' ');
      ArrayList<String> comment_lines = split_text(ed.getShort().replace("\n", " "), MAX_CHARS);
      StringBuilder comment = new StringBuilder("# ");
      char[] indent = new char[COMMENT_COL];
      Arrays.fill(indent, ' ');
      for(int i = 0; i < comment_lines.size();) {
        comment.append(comment_lines.get(i++));
        if(i < comment_lines.size())
          comment.append("\n" + new String(indent) + "# ");
      }
      tmplt.add("comment", new String(sep) + comment.toString());
    } else {
      tmplt.add("comment", " ");
    }
  }


  /**
   * Generate a ShEx element definition
   * @param sd Containing structure definition
   * @param ed Containing element definition
   * @return ShEx definition
   */
  private String genElementDefinition(StructureDefinition sd, ElementDefinition ed) {
    String id = ed.hasBase() ? ed.getBase().getPath() : ed.getPath();
    String shortId = id.substring(id.lastIndexOf(".") + 1);
    String defn;
    ST element_def;
    String card = ("*".equals(ed.getMax()) ? (ed.getMin() == 0 ? "*" : "+") : (ed.getMin() == 0 ? "?" : "")) + ";";

    if(id.endsWith("[x]")) {
      element_def = ed.getType().size() > 1? tmplt(INNER_SHAPE_TEMPLATE) : tmplt(ELEMENT_TEMPLATE);
      element_def.add("id", "");
    } else {
      element_def = tmplt(ELEMENT_TEMPLATE);
      element_def.add("id", "fhir:" + (id.charAt(0) == id.toLowerCase().charAt(0)? shortId : id) + " ");
    }

    List<ElementDefinition> children = ProfileUtilities.getChildList(sd, ed);
    if (children.size() > 0) {
      innerTypes.add(new ImmutablePair<StructureDefinition, ElementDefinition>(sd, ed));
      defn = simpleElement(sd, ed, id);
    } else if(id.endsWith("[x]")) {
      defn = genChoiceTypes(sd, ed, id);
    }
    else if (ed.getType().size() == 1) {
      // Single entry
      defn = genTypeRef(sd, ed, id, ed.getType().get(0));
    } else if (ed.getContentReference() != null) {
      // Reference to another element
      String ref = ed.getContentReference();
      if(!ref.startsWith("#"))
        throw new AssertionError("Not equipped to deal with absolute path references: " + ref);
      String refPath = null;
      for(ElementDefinition ed1: sd.getSnapshot().getElement()) {
        if(ed1.getId() != null && ed1.getId().equals(ref.substring(1))) {
          refPath = ed1.getPath();
          break;
        }
      }
      if(refPath == null)
        throw new AssertionError("Reference path not found: " + ref);
      // String typ = id.substring(0, id.indexOf(".") + 1) + ed.getContentReference().substring(1);
      defn = simpleElement(sd, ed, refPath);
    } else if(id.endsWith("[x]")) {
      defn = genChoiceTypes(sd, ed, id);
    } else {
      // TODO: Refactoring required here
      element_def = genAlternativeTypes(ed, id, shortId);
      element_def.add("id", id.charAt(0) == id.toLowerCase().charAt(0)? shortId : id);
      element_def.add("card", card);
      addComment(element_def, ed);
      return element_def.render();
    }
    element_def.add("defn", defn);
    element_def.add("card", card);
    addComment(element_def, ed);
    return element_def.render();
  }

  /**
   * Generate a type reference and optional value set definition
   * @param sd Containing StructureDefinition
   * @param ed Element being defined
   * @param typ Element type
   * @return Type definition
   */
  private String simpleElement(StructureDefinition sd, ElementDefinition ed, String typ) {
    String addldef = "";
    ElementDefinition.ElementDefinitionBindingComponent binding = ed.getBinding();
    if(binding.hasStrength() && binding.getStrength() == Enumerations.BindingStrength.REQUIRED && "code".equals(typ)) {
      ValueSet vs = resolveBindingReference(sd, binding.getValueSet());
      if (vs != null) {
        addldef = tmplt(VALUESET_DEFN_TEMPLATE).add("vsn", vsprefix(vs.getUrl())).render();
        required_value_sets.add(vs);
      }
    }
    // TODO: check whether value sets and fixed are mutually exclusive
    if(ed.hasFixed()) {
      addldef = tmplt(FIXED_VALUE_TEMPLATE).add("val", ed.getFixed().primitiveValue()).render();
    }
    return tmplt(SIMPLE_ELEMENT_DEFN_TEMPLATE).add("typ", typ).add("vsdef", addldef).render();
  }

  private String vsprefix(String uri) {
    if(uri.startsWith(FHIR_VS))
      return "fhirvs:" + uri.replace(FHIR_VS, "");
    return "<" + uri + ">";
  }

  /**
   * Generate a type reference
   * @param sd Containing structure definition
   * @param ed Containing element definition
   * @param id Element id
   * @param typ Element type
   * @return Type reference string
   */
  private String genTypeRef(StructureDefinition sd, ElementDefinition ed, String id, ElementDefinition.TypeRefComponent typ) {

    if(typ.hasProfile()) {
      if(typ.getCode().equals("Reference"))
        return genReference("", typ);
      else if(ProfileUtilities.getChildList(sd, ed).size() > 0) {
        // inline anonymous type - give it a name and factor it out
        innerTypes.add(new ImmutablePair<StructureDefinition, ElementDefinition>(sd, ed));
        return simpleElement(sd, ed, id);
      }
      else {
        String ref = getTypeName(typ);
        datatypes.add(ref);
        return simpleElement(sd, ed, ref);
      }

    } else if (typ.getCodeElement().getExtensionsByUrl(ToolingExtensions.EXT_RDF_TYPE).size() > 0) {
      String xt = null;
      try {
        xt = typ.getCodeElement().getExtensionString(ToolingExtensions.EXT_RDF_TYPE);
      } catch (FHIRException e) {
        e.printStackTrace();
      }
      // TODO: Remove the next line when the type of token gets switched to string
      // TODO: Add a rdf-type entry for valueInteger to xsd:integer (instead of int)
      ST td_entry = tmplt(PRIMITIVE_ELEMENT_DEFN_TEMPLATE).add("typ",
              xt.replace("xsd:token", "xsd:string").replace("xsd:int", "xsd:integer"));
      StringBuilder facets =  new StringBuilder();
      if(ed.hasMinValue()) {
        Type mv = ed.getMinValue();
        facets.append(tmplt(MINVALUE_TEMPLATE).add("val", mv.primitiveValue()).render());
      }
      if(ed.hasMaxValue()) {
        Type mv = ed.getMaxValue();
        facets.append(tmplt(MAXVALUE_TEMPLATE).add("val", mv.primitiveValue()).render());
      }
      if(ed.hasMaxLength()) {
        int ml = ed.getMaxLength();
        facets.append(tmplt(MAXLENGTH_TEMPLATE).add("val", ml).render());
      }
      if(ed.hasPattern()) {
        Type pat = ed.getPattern();
        facets.append(tmplt(PATTERN_TEMPLATE).add("val",pat.primitiveValue()).render());
      }
      td_entry.add("facets", facets.toString());
      return td_entry.render();

    } else if (typ.getCode() == null) {
      ST primitive_entry = tmplt(PRIMITIVE_ELEMENT_DEFN_TEMPLATE);
      primitive_entry.add("typ", "xsd:string");
      return primitive_entry.render();

    } else if(typ.getCode().equals("xhtml")) {
      return tmplt(XHTML_TYPE_TEMPLATE).render();
    } else {
      datatypes.add(typ.getCode());
      return simpleElement(sd, ed, typ.getCode());
    }
  }

  /**
   * Generate a set of alternative shapes
   * @param ed Containing element definition
   * @param id Element definition identifier
   * @param shortId id to use in the actual definition
   * @return ShEx list of alternative anonymous shapes separated by "OR"
   */
  private ST genAlternativeTypes(ElementDefinition ed, String id, String shortId) {
    ST shex_alt = tmplt(ALTERNATIVE_SHAPES_TEMPLATE);
    List<String> altEntries = new ArrayList<String>();


    for(ElementDefinition.TypeRefComponent typ : ed.getType())  {
      altEntries.add(genAltEntry(id, typ));
    }
    shex_alt.add("altEntries", StringUtils.join(altEntries, " OR\n    "));
    return shex_alt;
  }



  /**
   * Generate an alternative shape for a reference
   * @param id reference name
   * @param typ shape type
   * @return ShEx equivalent
   */
  private String genAltEntry(String id, ElementDefinition.TypeRefComponent typ) {
    if(!typ.getCode().equals("Reference"))
      throw new AssertionError("We do not handle " + typ.getCode() + " alternatives");

    return genReference(id, typ);
  }

  /**
   * Generate a list of type choices for a "name[x]" style id
   * @param sd Structure containing ed
   * @param ed element definition
   * @param id choice identifier
   * @return ShEx fragment for the set of choices
   */
  private String genChoiceTypes(StructureDefinition sd, ElementDefinition ed, String id) {
    List<String> choiceEntries = new ArrayList<String>();
    String base = id.replace("[x]", "");

    for(ElementDefinition.TypeRefComponent typ : ed.getType())
      choiceEntries.add(genChoiceEntry(sd, ed, id, base, typ));

    return StringUtils.join(choiceEntries, " |\n");
  }

  /**
   * Generate an entry in a choice list
   * @param base base identifier
   * @param typ type/discriminant
   * @return ShEx fragment for choice entry
   */
  private String genChoiceEntry(StructureDefinition sd, ElementDefinition ed, String id, String base, ElementDefinition.TypeRefComponent typ) {
    ST shex_choice_entry = tmplt(ELEMENT_TEMPLATE);

    String ext = typ.getCode();
    shex_choice_entry.add("id", "fhir:" + base+Character.toUpperCase(ext.charAt(0)) + ext.substring(1) + " ");
    shex_choice_entry.add("card", "");
    shex_choice_entry.add("defn", genTypeRef(sd, ed, id, typ));
    shex_choice_entry.add("comment", " ");
    return shex_choice_entry.render();
  }

  /**
   * Generate a definition for a referenced element
   * @param sd Containing structure definition
   * @param ed Inner element
   * @return ShEx representation of element reference
   */
  private String genInnerTypeDef(StructureDefinition sd, ElementDefinition ed) {
    String path = ed.hasBase() ? ed.getBase().getPath() : ed.getPath();;
    ST element_reference = tmplt(SHAPE_DEFINITION_TEMPLATE);
    element_reference.add("resourceDecl", "");  // Not a resource
    element_reference.add("id", path);
    String comment = ed.getShort();
    element_reference.add("comment", comment == null? " " : "# " + comment);

    List<String> elements = new ArrayList<String>();
    for (ElementDefinition child: ProfileUtilities.getChildList(sd, path, null))
      elements.add(genElementDefinition(sd, child));

    element_reference.add("elements", StringUtils.join(elements, "\n"));
    return element_reference.render();
  }

  /**
   * Generate a reference to a resource
   * @param id attribute identifier
   * @param typ possible reference types
   * @return string that represents the result
   */
  private String genReference(String id, ElementDefinition.TypeRefComponent typ) {
    ST shex_ref = tmplt(REFERENCE_DEFN_TEMPLATE);

    String ref = getTypeName(typ);
    shex_ref.add("id", id);
    shex_ref.add("ref", ref);
    references.add(ref);
    return shex_ref.render();
  }

  /**
   * Return the type name for typ
   * @param typ type to get name for
   * @return name
   */
  private String getTypeName(ElementDefinition.TypeRefComponent typ) {
    // TODO: This is brittle. There has to be a utility to do this...
    if (typ.hasTargetProfile()) {
      String[] els = typ.getTargetProfile().get(0).getValue().split("/");
      return els[els.length - 1];
    } else if (typ.hasProfile()) {
      String[] els = typ.getProfile().get(0).getValue().split("/");
      return els[els.length - 1];
    } else {
      return typ.getCode();
    }
  }

  private String genValueSet(ValueSet vs) {
    ST vsd = tmplt(VALUE_SET_DEFINITION).add("vsuri", vsprefix(vs.getUrl())).add("comment", vs.getDescription());
    ValueSetExpander.ValueSetExpansionOutcome vse = context.expandVS(vs, true, false);
    List<String> valid_codes = new ArrayList<String>();
    if(vse != null &&
            vse.getValueset() != null &&
            vse.getValueset().hasExpansion() &&
            vse.getValueset().getExpansion().hasContains()) {
      for(ValueSet.ValueSetExpansionContainsComponent vsec : vse.getValueset().getExpansion().getContains())
        valid_codes.add("\"" + vsec.getCode() + "\"");
    }
    return vsd.add("val_list", valid_codes.size() > 0? " [" + StringUtils.join(valid_codes, " ") + ']' : " EXTERNAL").render();
  }


  // TODO: find a utility that implements this
  private ValueSet resolveBindingReference(DomainResource ctxt, String reference) {
    try {
      return context.fetchResource(ValueSet.class, reference);
    } catch (Throwable e) {
      return null;
    }
  }
}
