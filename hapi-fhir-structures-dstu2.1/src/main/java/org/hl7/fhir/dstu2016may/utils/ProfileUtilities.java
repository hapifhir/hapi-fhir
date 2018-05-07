package org.hl7.fhir.dstu2016may.utils;

import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

import org.hl7.fhir.dstu2016may.formats.IParser;
import org.hl7.fhir.dstu2016may.model.*;
import org.hl7.fhir.dstu2016may.model.ElementDefinition.*;
import org.hl7.fhir.dstu2016may.model.Enumerations.BindingStrength;
import org.hl7.fhir.dstu2016may.model.StructureDefinition.*;
import org.hl7.fhir.dstu2016may.model.ValueSet.ValueSetExpansionComponent;
import org.hl7.fhir.dstu2016may.model.ValueSet.ValueSetExpansionContainsComponent;
import org.hl7.fhir.dstu2016may.terminologies.ValueSetExpander.ValueSetExpansionOutcome;
import org.hl7.fhir.dstu2016may.utils.ProfileUtilities.ProfileKnowledgeProvider.BindingResolution;
import org.hl7.fhir.utilities.validation.ValidationMessage;
import org.hl7.fhir.utilities.validation.ValidationMessage.IssueType;
import org.hl7.fhir.utilities.validation.ValidationMessage.IssueSeverity;
import org.hl7.fhir.utilities.validation.ValidationMessage.Source;
import org.hl7.fhir.exceptions.DefinitionException;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.utilities.CommaSeparatedStringBuilder;
import org.hl7.fhir.utilities.Utilities;
import org.hl7.fhir.utilities.xhtml.HierarchicalTableGenerator;
import org.hl7.fhir.utilities.xhtml.HierarchicalTableGenerator.*;
import org.hl7.fhir.utilities.xhtml.XhtmlNode;
import org.hl7.fhir.utilities.xml.SchematronWriter;
import org.hl7.fhir.utilities.xml.SchematronWriter.*;

/**
 * This class provides a set of utility operations for working with Profiles.
 * Key functionality:
 *  * getChildMap --?
 *  * getChildList
 *  * generateSnapshot: Given a base (snapshot) profile structure, and a differential profile, generate a new snapshot profile
 *  * generateExtensionsTable: generate the HTML for a hierarchical table presentation of the extensions
 *  * generateTable: generate  the HTML for a hierarchical table presentation of a structure
 *  * summarise: describe the contents of a profile
 * @author Grahame
 *
 */
public class ProfileUtilities {

  public static final int STATUS_OK = 0;
  public static final int STATUS_HINT = 1;
  public static final int STATUS_WARNING = 2;
  public static final int STATUS_ERROR = 3;
  public static final int STATUS_FATAL = 4;
  public static final String DERIVATION_POINTER = "derived.pointer";
  public static final String IS_DERIVED = "derived.fact";
  public static final String UD_ERROR_STATUS = "error-status";
  private static final String ROW_COLOR_ERROR = "#ffcccc";
  private static final String ROW_COLOR_FATAL = "#ff9999";
  private static final String ROW_COLOR_WARNING = "#ffebcc";
  private static final String ROW_COLOR_HINT = "#ebf5ff";
  private static final String DERIVATION_EQUALS = "derivation.equals";
  private final boolean ADD_REFERENCE_TO_TABLE = true;
  // note that ProfileUtilities are used re-entrantly internally, so nothing with process state can be here
  private final IWorkerContext context;
  private List<ValidationMessage> messages;
  private List<String> snapshotStack = new ArrayList<String>();
  private ProfileKnowledgeProvider pkp;
  public ProfileUtilities(IWorkerContext context, List<ValidationMessage> messages, ProfileKnowledgeProvider pkp) {
    super();
    this.context = context;
    this.messages = messages;
    this.pkp = pkp;
  }

  private boolean allTypesAre(List<TypeRefComponent> types, String name) {
    for (TypeRefComponent t : types) {
      if (!t.getCode().equals(name))
        return false;
    }
    return true;
  }

  private String buildJson(Type value) throws IOException {
    if (value instanceof PrimitiveType)
      return ((PrimitiveType) value).asStringValue();

    IParser json = context.newJsonParser();
    return json.composeString(value, null);
  }

  private boolean checkExtensionDoco(ElementDefinition base) {
    // see task 3970. For an extension, there's no point copying across all the underlying definitional stuff
    boolean isExtension = base.getPath().equals("Extension") || base.getPath().endsWith(".extension") || base.getPath().endsWith(".modifierExtension");
    if (isExtension) {
      base.setDefinition("An Extension");
      base.setShort("Extension");
      base.setCommentsElement(null);
      base.setRequirementsElement(null);
      base.getAlias().clear();
      base.getMapping().clear();
    }
    return isExtension;
  }

  private Piece checkForNoChange(Element source, Piece piece) {
    if (source.hasUserData(DERIVATION_EQUALS)) {
      piece.addStyle("opacity: 0.4");
    }
    return piece;
  }

  private Piece checkForNoChange(Element src1, Element src2, Piece piece) {
    if (src1.hasUserData(DERIVATION_EQUALS) && src2.hasUserData(DERIVATION_EQUALS)) {
      piece.addStyle("opacity: 0.5");
    }
    return piece;
  }

  private boolean codesInExpansion(List<ValueSetExpansionContainsComponent> contains, ValueSetExpansionComponent expansion) {
    for (ValueSetExpansionContainsComponent cc : contains) {
      if (!inExpansion(cc, expansion.getContains()))
        return false;
      if (!codesInExpansion(cc.getContains(), expansion))
        return false;
    }
    return true;
  }

  private String commas(List<StringType> discriminator) {
    CommaSeparatedStringBuilder c = new CommaSeparatedStringBuilder();
    for (StringType id : discriminator)
      c.append(id.asStringValue());
    return c.toString();
  }

  private String describe(SlicingRules rules) {
    switch (rules) {
    case CLOSED : return "Closed";
    case OPEN : return "Open";
    case OPENATEND : return "Open At End";
    default:
      return "??";
    }
  }

  private String describeCardinality(ElementDefinition definition, ElementDefinition fallback, UnusedTracker tracker) {
    IntegerType min = definition.hasMinElement() ? definition.getMinElement() : new IntegerType();
    StringType max = definition.hasMaxElement() ? definition.getMaxElement() : new StringType();
    if (min.isEmpty() && fallback != null)
      min = fallback.getMinElement();
    if (max.isEmpty() && fallback != null)
      max = fallback.getMaxElement();

    tracker.used = !max.isEmpty() && !max.getValue().equals("0");

    if (min.isEmpty() && max.isEmpty())
      return null;
    else
      return (!min.hasValue() ? "" : Integer.toString(min.getValue())) + ".." + (!max.hasValue() ? "" : max.getValue());
  }

  public String describeSlice(ElementDefinitionSlicingComponent slicing) {
    return (slicing.getOrdered() ? "Ordered, " : "Unordered, ")+describe(slicing.getRules())+", by "+commas(slicing.getDiscriminator());
  }

  private boolean discriiminatorMatches(List<StringType> diff, List<StringType> base) {
    if (diff.isEmpty() || base.isEmpty())
    	return true;
    if (diff.size() != base.size())
    	return false;
    for (int i = 0; i < diff.size(); i++)
    	if (!diff.get(i).getValue().equals(base.get(i).getValue()))
    		return false;
    return true;
  }

  private boolean extensionIsComplex(String value) {
    if (value.contains("#")) {
      StructureDefinition ext = context.fetchResource(StructureDefinition.class, value.substring(0, value.indexOf("#")));
    if (ext == null)
      return false;
      String tail = value.substring(value.indexOf("#")+1);
      ElementDefinition ed = null;
      for (ElementDefinition ted : ext.getSnapshot().getElement()) {
        if (tail.equals(ted.getName())) {
          ed = ted;
          break;
        }
      }
      if (ed == null)
        return false;
      int i = ext.getSnapshot().getElement().indexOf(ed);
      int j = i+1;
      while (j < ext.getSnapshot().getElement().size() && !ext.getSnapshot().getElement().get(j).getPath().equals(ed.getPath()))
        j++;
      return j - i > 5;
    } else {
      StructureDefinition ext = context.fetchResource(StructureDefinition.class, value);
      return ext != null && ext.getSnapshot().getElement().size() > 5;
    }
  }

  private int findEndOfElement(StructureDefinitionDifferentialComponent context, int cursor) {
	    int result = cursor;
	    String path = context.getElement().get(cursor).getPath()+".";
	    while (result < context.getElement().size()- 1 && context.getElement().get(result+1).getPath().startsWith(path))
	      result++;
	    return result;
	  }

  private int findEndOfElement(StructureDefinitionSnapshotComponent context, int cursor) {
	    int result = cursor;
	    String path = context.getElement().get(cursor).getPath()+".";
	    while (result < context.getElement().size()- 1 && context.getElement().get(result+1).getPath().startsWith(path))
	      result++;
	    return result;
	  }

  private String fixedPath(String contextPath, String pathSimple) {
    if (contextPath == null)
      return pathSimple;
    return contextPath+"."+pathSimple.substring(pathSimple.indexOf(".")+1);
  }

  private void genCardinality(HierarchicalTableGenerator gen, ElementDefinition definition, Row row, boolean hasDef, UnusedTracker tracker, ElementDefinition fallback) {
    IntegerType min = !hasDef ? new IntegerType() : definition.hasMinElement() ? definition.getMinElement() : new IntegerType();
    StringType max = !hasDef ? new StringType() : definition.hasMaxElement() ? definition.getMaxElement() : new StringType();
    if (min.isEmpty() && definition.getUserData(DERIVATION_POINTER) != null) {
      ElementDefinition base = (ElementDefinition) definition.getUserData(DERIVATION_POINTER);
      min = base.getMinElement().copy();
      min.setUserData(DERIVATION_EQUALS, true);
    }
    if (max.isEmpty() && definition.getUserData(DERIVATION_POINTER) != null) {
      ElementDefinition base = (ElementDefinition) definition.getUserData(DERIVATION_POINTER);
      max = base.getMaxElement().copy();
      max.setUserData(DERIVATION_EQUALS, true);
    }
    if (min.isEmpty() && fallback != null)
      min = fallback.getMinElement();
    if (max.isEmpty() && fallback != null)
      max = fallback.getMaxElement();

    if (!max.isEmpty())
      tracker.used = !max.getValue().equals("0");

    Cell cell = gen.new Cell(null, null, null, null, null);
    row.getCells().add(cell);
    if (!min.isEmpty() || !max.isEmpty()) {
      cell.addPiece(checkForNoChange(min, gen.new Piece(null, !min.hasValue() ? "" : Integer.toString(min.getValue()), null)));
      cell.addPiece(checkForNoChange(min, max, gen.new Piece(null, "..", null)));
      cell.addPiece(checkForNoChange(min, gen.new Piece(null, !max.hasValue() ? "" : max.getValue(), null)));
    }
  }

  private Cell genTypes(HierarchicalTableGenerator gen, Row r, ElementDefinition e, String profileBaseFileName, StructureDefinition profile, String corePath) {
    Cell c = gen.new Cell();
    r.getCells().add(c);
    List<TypeRefComponent> types = e.getType();
    if (!e.hasType()) {
      if (e.hasContentReference()) {
        return c;
      } else {
        ElementDefinition d = (ElementDefinition) e.getUserData(DERIVATION_POINTER);
        if (d != null && d.hasType()) {
          types = new ArrayList<ElementDefinition.TypeRefComponent>();
          for (TypeRefComponent tr : d.getType()) {
            TypeRefComponent tt = tr.copy();
            tt.setUserData(DERIVATION_EQUALS, true);
            types.add(tt);
          }
        } else
          return c;
      }
    }

    boolean first = true;
    Element source = types.get(0); // either all types are the same, or we don't consider any of them the same

    boolean allReference = ADD_REFERENCE_TO_TABLE && !types.isEmpty();
    for (TypeRefComponent t : types) {
      if (!(t.getCode().equals("Reference") && t.hasProfile()))
        allReference = false;
    }
    if (allReference) {
      c.getPieces().add(gen.new Piece(corePath+"references.html", "Reference", null));
      c.getPieces().add(gen.new Piece(null, "(", null));
    }
    TypeRefComponent tl = null;
    for (TypeRefComponent t : types) {
      if (first)
        first = false;
      else if (allReference)
        c.addPiece(checkForNoChange(tl, gen.new Piece(null," | ", null)));
      else
        c.addPiece(checkForNoChange(tl, gen.new Piece(null,", ", null)));
      tl = t;
      if (t.getCode().equals("Reference") || (t.getCode().equals("Resource") && t.hasProfile())) {
        if (ADD_REFERENCE_TO_TABLE && !allReference) {
          c.getPieces().add(gen.new Piece(corePath+"references.html", "Reference", null));
          c.getPieces().add(gen.new Piece(null, "(", null));
        }
        if (t.hasProfile() && t.getProfile().get(0).getValue().startsWith("http://hl7.org/fhir/StructureDefinition/")) {
          StructureDefinition sd = context.fetchResource(StructureDefinition.class, t.getProfile().get(0).getValue());
          if (sd != null) {
            String disp = sd.hasDisplay() ? sd.getDisplay() : sd.getName();
            c.addPiece(checkForNoChange(t, gen.new Piece(corePath+sd.getUserString("path"), disp, null)));
          } else {
            String rn = t.getProfile().get(0).getValue().substring(40);
            c.addPiece(checkForNoChange(t, gen.new Piece(corePath+pkp.getLinkFor(rn), rn, null)));
          }
        } else if (t.getProfile().size() == 0) {
          c.addPiece(checkForNoChange(t, gen.new Piece(null, t.getCode(), null)));
        } else if (t.getProfile().get(0).getValue().startsWith("#"))
          c.addPiece(checkForNoChange(t, gen.new Piece(corePath+profileBaseFileName+"."+t.getProfile().get(0).getValue().substring(1).toLowerCase()+".html", t.getProfile().get(0).getValue(), null)));
        else
          c.addPiece(checkForNoChange(t, gen.new Piece(corePath+t.getProfile().get(0).getValue(), t.getProfile().get(0).getValue(), null)));
        if (ADD_REFERENCE_TO_TABLE && !allReference) {
          c.getPieces().add(gen.new Piece(null, ")", null));
        }
      } else if (t.hasProfile()) { // a profiled type
        String ref;
        ref = pkp.getLinkForProfile(profile, t.getProfile().get(0).getValue());
        if (ref != null) {
          String[] parts = ref.split("\\|");
          c.addPiece(checkForNoChange(t, gen.new Piece(corePath+parts[0], parts[1], t.getCode())));
        } else
          c.addPiece(checkForNoChange(t, gen.new Piece(corePath+ref, t.getCode(), null)));
      } else if (pkp.hasLinkFor(t.getCode())) {
        c.addPiece(checkForNoChange(t, gen.new Piece(corePath+pkp.getLinkFor(t.getCode()), t.getCode(), null)));
      } else
        c.addPiece(checkForNoChange(t, gen.new Piece(null, t.getCode(), null)));
    }
    if (allReference) {
      c.getPieces().add(gen.new Piece(null, ")", null));
    }
    return c;
  }

  private Cell generateDescription(HierarchicalTableGenerator gen, Row row, ElementDefinition definition, ElementDefinition fallback, boolean used, String baseURL, String url, StructureDefinition profile, String corePath, boolean root, boolean logicalModel) throws IOException {
    Cell c = gen.new Cell();
    row.getCells().add(c);

    if (used) {
      if (logicalModel && ToolingExtensions.hasExtension(profile, "http://hl7.org/fhir/StructureDefinition/elementdefinition-namespace")) {
        if (root) {
          c.getPieces().add(gen.new Piece(null, "XML Namespace: ", null).addStyle("font-weight:bold"));
          c.getPieces().add(gen.new Piece(null, ToolingExtensions.readStringExtension(profile, "http://hl7.org/fhir/StructureDefinition/elementdefinition-namespace"), null));
        } else if (!root && ToolingExtensions.hasExtension(definition, "http://hl7.org/fhir/StructureDefinition/elementdefinition-namespace") &&
            !ToolingExtensions.readStringExtension(definition, "http://hl7.org/fhir/StructureDefinition/elementdefinition-namespace").equals(ToolingExtensions.readStringExtension(profile, "http://hl7.org/fhir/StructureDefinition/elementdefinition-namespace"))) {
          c.getPieces().add(gen.new Piece(null, "XML Namespace: ", null).addStyle("font-weight:bold"));
          c.getPieces().add(gen.new Piece(null, ToolingExtensions.readStringExtension(definition, "http://hl7.org/fhir/StructureDefinition/elementdefinition-namespace"), null));
        }
      }

      if (definition.hasContentReference()) {
        ElementDefinition ed = getElementByName(profile.getSnapshot().getElement(), definition.getContentReference());
        if (ed == null)
          c.getPieces().add(gen.new Piece(null, "Unknown reference to "+definition.getContentReference(), null));
        else
          c.getPieces().add(gen.new Piece("#"+ed.getPath(), "See "+ed.getPath(), null));
      }
      if (definition.getPath().endsWith("url") && definition.hasFixed()) {
        c.getPieces().add(checkForNoChange(definition.getFixed(), gen.new Piece(null, "\""+buildJson(definition.getFixed())+"\"", null).addStyle("color: darkgreen")));
      } else {
        if (definition != null && definition.hasShort()) {
          if (!c.getPieces().isEmpty()) c.addPiece(gen.new Piece("br"));
          c.addPiece(checkForNoChange(definition.getShortElement(), gen.new Piece(null, definition.getShort(), null)));
        } else if (fallback != null && fallback.hasShort()) {
          if (!c.getPieces().isEmpty()) c.addPiece(gen.new Piece("br"));
          c.addPiece(checkForNoChange(fallback.getShortElement(), gen.new Piece(null, fallback.getShort(), null)));
        }
        if (url != null) {
          if (!c.getPieces().isEmpty()) c.addPiece(gen.new Piece("br"));
          String fullUrl = url.startsWith("#") ? baseURL+url : url;
          StructureDefinition ed = context.fetchResource(StructureDefinition.class, url);
          String ref = ed == null ? null : (String) corePath+ed.getUserData("path");
          c.getPieces().add(gen.new Piece(null, "URL: ", null).addStyle("font-weight:bold"));
          c.getPieces().add(gen.new Piece(ref, fullUrl, null));
        }

        if (definition.hasSlicing()) {
          if (!c.getPieces().isEmpty()) c.addPiece(gen.new Piece("br"));
          c.getPieces().add(gen.new Piece(null, "Slice: ", null).addStyle("font-weight:bold"));
          c.getPieces().add(gen.new Piece(null, describeSlice(definition.getSlicing()), null));
        }
        if (definition != null) {
          if (definition.hasBinding()) {
            if (!c.getPieces().isEmpty()) c.addPiece(gen.new Piece("br"));
            BindingResolution br = pkp.resolveBinding(definition.getBinding());
            c.getPieces().add(checkForNoChange(definition.getBinding(), gen.new Piece(null, "Binding: ", null).addStyle("font-weight:bold")));
            c.getPieces().add(checkForNoChange(definition.getBinding(), gen.new Piece(br.url == null ? null : Utilities.isAbsoluteUrl(br.url)? br.url : corePath+br.url, br.display, null)));
            if (definition.getBinding().hasStrength()) {
              c.getPieces().add(checkForNoChange(definition.getBinding(), gen.new Piece(null, " (", null)));
              c.getPieces().add(checkForNoChange(definition.getBinding(), gen.new Piece(corePath+"terminologies.html#"+definition.getBinding().getStrength().toCode(), definition.getBinding().getStrength().toCode(), definition.getBinding().getStrength().getDefinition())));
              c.getPieces().add(gen.new Piece(null, ")", null));
            }
          }
          for (ElementDefinitionConstraintComponent inv : definition.getConstraint()) {
            if (!c.getPieces().isEmpty()) c.addPiece(gen.new Piece("br"));
            c.getPieces().add(checkForNoChange(inv, gen.new Piece(null, inv.getKey()+": ", null).addStyle("font-weight:bold")));
            c.getPieces().add(checkForNoChange(inv, gen.new Piece(null, inv.getHuman(), null)));
          }
          if (definition.hasFixed()) {
            if (!c.getPieces().isEmpty()) c.addPiece(gen.new Piece("br"));
            c.getPieces().add(checkForNoChange(definition.getFixed(), gen.new Piece(null, "Fixed Value: ", null).addStyle("font-weight:bold")));
            c.getPieces().add(checkForNoChange(definition.getFixed(), gen.new Piece(null, buildJson(definition.getFixed()), null).addStyle("color: darkgreen")));
          } else if (definition.hasPattern()) {
            if (!c.getPieces().isEmpty()) c.addPiece(gen.new Piece("br"));
            c.getPieces().add(checkForNoChange(definition.getPattern(), gen.new Piece(null, "Required Pattern: ", null).addStyle("font-weight:bold")));
            c.getPieces().add(checkForNoChange(definition.getPattern(), gen.new Piece(null, buildJson(definition.getPattern()), null).addStyle("color: darkgreen")));
          } else if (definition.hasExample()) {
            if (!c.getPieces().isEmpty()) c.addPiece(gen.new Piece("br"));
            c.getPieces().add(checkForNoChange(definition.getExample(), gen.new Piece(null, "Example: ", null).addStyle("font-weight:bold")));
            c.getPieces().add(checkForNoChange(definition.getExample(), gen.new Piece(null, buildJson(definition.getExample()), null).addStyle("color: darkgreen")));
          }
        }
      }
    }
    return c;
  }

  private void generateForChildren(SchematronWriter sch, String xpath, ElementDefinition ed, StructureDefinition structure, StructureDefinition base) throws IOException {
    //    generateForChild(txt, structure, child);
    List<ElementDefinition> children = getChildList(structure, ed);
    String sliceName = null;
    ElementDefinitionSlicingComponent slicing = null;
    for (ElementDefinition child : children) {
      String name = tail(child.getPath());
      if (child.hasSlicing()) {
        sliceName = name;
        slicing = child.getSlicing();
      } else if (!name.equals(sliceName))
        slicing = null;

      ElementDefinition based = getByPath(base, child.getPath());
      boolean doMin = (child.getMin() > 0) && (based == null || (child.getMin() != based.getMin()));
      boolean doMax =  !child.getMax().equals("*") && (based == null || (!child.getMax().equals(based.getMax())));
      Slicer slicer = slicing == null ? new Slicer(true) : generateSlicer(child, slicing, structure);
      if (slicer.check) {
        if (doMin || doMax) {
          Section s = sch.section(xpath);
          Rule r = s.rule(xpath);
          if (doMin)
            r.assrt("count(f:"+name+slicer.criteria+") >= "+Integer.toString(child.getMin()), name+slicer.name+": minimum cardinality of '"+name+"' is "+Integer.toString(child.getMin()));
          if (doMax)
            r.assrt("count(f:"+name+slicer.criteria+") <= "+child.getMax(), name+slicer.name+": maximum cardinality of '"+name+"' is "+child.getMax());
          }
        }
      }
    for (ElementDefinitionConstraintComponent inv : ed.getConstraint()) {
      if (inv.hasXpath()) {
        Section s = sch.section(ed.getPath());
        Rule r = s.rule(xpath);
        r.assrt(inv.getXpath(), (inv.hasId() ? inv.getId()+": " : "")+inv.getHuman()+(inv.hasUserData(IS_DERIVED) ? " (inherited)" : ""));
      }
    }
    for (ElementDefinition child : children) {
      String name = tail(child.getPath());
      generateForChildren(sch, xpath+"/f:"+name, child, structure, base);
    }
  }

  public void generateSchematrons(OutputStream dest, StructureDefinition structure) throws IOException, DefinitionException {
    if (structure.getDerivation() != TypeDerivationRule.CONSTRAINT)
      throw new DefinitionException("not the right kind of structure to generate schematrons for");
    if (!structure.hasSnapshot())
      throw new DefinitionException("needs a snapshot");

  	StructureDefinition base = context.fetchResource(StructureDefinition.class, structure.getBaseDefinition());

  	SchematronWriter sch = new SchematronWriter(dest, SchematronType.PROFILE, base.getName());

    ElementDefinition ed = structure.getSnapshot().getElement().get(0);
    generateForChildren(sch, "f:"+ed.getPath(), ed, structure, base);
    sch.dump();
  }

  private Slicer generateSlicer(ElementDefinition child, ElementDefinitionSlicingComponent slicing, StructureDefinition structure) {
    // given a child in a structure, it's sliced. figure out the slicing xpath
    if (child.getPath().endsWith(".extension")) {
      ElementDefinition ued = getUrlFor(structure, child);
      if ((ued == null || !ued.hasFixed()) && !(child.getType().get(0).hasProfile()))
        return new Slicer(false);
      else {
      Slicer s = new Slicer(true);
      String url = (ued == null || !ued.hasFixed()) ? child.getType().get(0).getProfile().get(0).asStringValue() : ((UriType) ued.getFixed()).asStringValue();
      s.name = " with URL = '"+url+"'";
      s.criteria = "[@url = '"+url+"']";
      return s;
      }
    } else
      return new Slicer(false);
  }

  public void generateSnapshot(StructureDefinition base, StructureDefinition derived, String url, String profileName) throws DefinitionException, FHIRException {
    if (base == null)
      throw new DefinitionException("no base profile provided");
    if (derived == null)
      throw new DefinitionException("no derived structure provided");

    if (snapshotStack.contains(derived.getUrl()))
      throw new DefinitionException("Circular snapshot references detected; cannot generate snapshot (stack = "+snapshotStack.toString()+")");
    snapshotStack.add(derived.getUrl());

//    System.out.println("Generate Snapshot for "+derived.getUrl());

    derived.setSnapshot(new StructureDefinitionSnapshotComponent());

    // so we have two lists - the base list, and the differential list
    // the differential list is only allowed to include things that are in the base list, but
    // is allowed to include them multiple times - thereby slicing them

    // our approach is to walk through the base list, and see whether the differential
    // says anything about them.
    int baseCursor = 0;
    int diffCursor = 0; // we need a diff cursor because we can only look ahead, in the bound scoped by longer paths

    // we actually delegate the work to a subroutine so we can re-enter it with a different cursors
    processPaths(derived.getSnapshot(), base.getSnapshot(), derived.getDifferential(), baseCursor, diffCursor, base.getSnapshot().getElement().size()-1, derived.getDifferential().getElement().size()-1, url, derived.getId(), null, false, base.getUrl(), null, false);
  }

  private ElementDefinition getByPath(StructureDefinition base, String path) {
		for (ElementDefinition ed : base.getSnapshot().getElement()) {
			if (ed.getPath().equals(path))
				return ed;
			if (ed.getPath().endsWith("[x]") && ed.getPath().length() <= path.length()-3 &&  ed.getPath().substring(0, ed.getPath().length()-3).equals(path.substring(0, ed.getPath().length()-3)))
				return ed;
    }
	  return null;
  }

  private List<ElementDefinition> getChildren(List<ElementDefinition> all, ElementDefinition element) {
    List<ElementDefinition> result = new ArrayList<ElementDefinition>();
    int i = all.indexOf(element)+1;
    while (i < all.size() && all.get(i).getPath().length() > element.getPath().length()) {
      if ((all.get(i).getPath().substring(0, element.getPath().length()+1).equals(element.getPath()+".")) && !all.get(i).getPath().substring(element.getPath().length()+1).contains("."))
        result.add(all.get(i));
      i++;
    }
    return result;
  }

  private List<ElementDefinition> getDiffMatches(StructureDefinitionDifferentialComponent context, String path, int start, int end, String profileName) {
    List<ElementDefinition> result = new ArrayList<ElementDefinition>();
    for (int i = start; i <= end; i++) {
      String statedPath = context.getElement().get(i).getPath();
      if (statedPath.equals(path) || (path.endsWith("[x]") && statedPath.length() > path.length() - 2 && statedPath.substring(0, path.length()-3).equals(path.substring(0, path.length()-3)) && !statedPath.substring(path.length()).contains("."))) {
        result.add(context.getElement().get(i));
      } else if (result.isEmpty()) {
//        System.out.println("ignoring "+statedPath+" in differential of "+profileName);
      }
    }
    return result;
  }

  private ElementDefinition getElementByName(List<ElementDefinition> elements, String contentReference) {
    for (ElementDefinition ed : elements)
      if (ed.hasName() && ("#"+ed.getName()).equals(contentReference))
        return ed;
    return null;
  }

  public StructureDefinition getProfile(StructureDefinition source, String url) {
  	StructureDefinition profile;
  	String code;
  	if (url.startsWith("#")) {
  		profile = source;
  		code = url.substring(1);
  	} else {
  		String[] parts = url.split("\\#");
  		profile = context.fetchResource(StructureDefinition.class, parts[0]);
      code = parts.length == 1 ? null : parts[1];
  	}
  	if (profile == null)
  		return null;
  	if (code == null)
  		return profile;
  	for (Resource r : profile.getContained()) {
  		if (r instanceof StructureDefinition && r.getId().equals(code))
  			return (StructureDefinition) r;
  	}
  	return null;
  }

  private StructureDefinition getProfileForDataType(TypeRefComponent type)  {
    StructureDefinition sd = null;
    if (type.hasProfile())
      sd = context.fetchResource(StructureDefinition.class, type.getProfile().get(0).asStringValue());
    if (sd == null)
      sd = context.fetchResource(StructureDefinition.class, "http://hl7.org/fhir/StructureDefinition/"+type.getCode());
    if (sd == null)
      System.out.println("XX: failed to find profle for type: " + type.getCode()); // debug GJM
    return sd;
  }

  private String getRowColor(ElementDefinition element) {
    switch (element.getUserInt(UD_ERROR_STATUS)) {
    case STATUS_OK: return null;
    case STATUS_HINT: return ROW_COLOR_HINT;
    case STATUS_WARNING: return ROW_COLOR_WARNING;
    case STATUS_ERROR: return ROW_COLOR_ERROR;
    case STATUS_FATAL: return ROW_COLOR_FATAL;
    default: return null;
    }
  }

  private List<ElementDefinition> getSiblings(List<ElementDefinition> list, ElementDefinition current) {
    List<ElementDefinition> result = new ArrayList<ElementDefinition>();
    String path = current.getPath();
    int cursor = list.indexOf(current)+1;
    while (cursor < list.size() && list.get(cursor).getPath().length() >= path.length()) {
      if (pathMatches(list.get(cursor).getPath(), path))
        result.add(list.get(cursor));
      cursor++;
    }
    return result;
  }

  private ElementDefinition getUrlFor(StructureDefinition ed, ElementDefinition c) {
    int i = ed.getSnapshot().getElement().indexOf(c) + 1;
    while (i < ed.getSnapshot().getElement().size() && ed.getSnapshot().getElement().get(i).getPath().startsWith(c.getPath()+".")) {
      if (ed.getSnapshot().getElement().get(i).getPath().equals(c.getPath()+".url"))
        return ed.getSnapshot().getElement().get(i);
      i++;
    }
    return null;
  }

  private ElementDefinition getValueFor(StructureDefinition ed, ElementDefinition c) {
    int i = ed.getSnapshot().getElement().indexOf(c) + 1;
    while (i < ed.getSnapshot().getElement().size() && ed.getSnapshot().getElement().get(i).getPath().startsWith(c.getPath()+".")) {
      if (ed.getSnapshot().getElement().get(i).getPath().startsWith(c.getPath()+".value"))
        return ed.getSnapshot().getElement().get(i);
      i++;
    }
    return null;
  }

  private boolean inExpansion(ValueSetExpansionContainsComponent cc, List<ValueSetExpansionContainsComponent> contains) {
    for (ValueSetExpansionContainsComponent cc1 : contains) {
      if (cc.getSystem().equals(cc1.getSystem()) && cc.getCode().equals(cc1.getCode()))
        return true;
      if (inExpansion(cc,  cc1.getContains()))
        return true;
    }
    return false;
  }

  private boolean isAbstract(String code) {
    return code.equals("Element") || code.equals("BackboneElement") || code.equals("Resource") || code.equals("DomainResource");
  }

  private boolean isDataType(List<TypeRefComponent> types) {
    if (types.isEmpty())
      return false;
    for (TypeRefComponent type : types) {
      String t = type.getCode();
      if (!isDataType(t) && !t.equals("Reference") && !t.equals("Narrative") && !t.equals("Extension") && !t.equals("ElementDefinition") && !isPrimitive(t))
        return false;
    }
    return true;
  }

  private boolean isDataType(String value) {
    return Utilities.existsInList(value, "Identifier", "HumanName", "Address", "ContactPoint", "Timing", "SimpleQuantity", "Quantity", "Attachment", "Range",
          "Period", "Ratio", "CodeableConcept", "Coding", "SampledData", "Age", "Distance", "Duration", "Count", "Money");
  }

  private boolean isExtension(ElementDefinition currentBase) {
    return currentBase.getPath().endsWith(".extension") || currentBase.getPath().endsWith(".modifierExtension");
  }

  private boolean isLargerMax(String derived, String base) {
    if ("*".equals(base))
      return false;
    if ("*".equals(derived))
      return true;
    return Integer.parseInt(derived) > Integer.parseInt(base);
  }

  private boolean isReference(String value) {
    return value.equals("Reference");
  }

  private boolean isSlicedToOneOnly(ElementDefinition e) {
    return (e.hasSlicing() && e.hasMaxElement() && e.getMax().equals("1"));
  }

  private boolean isSubset(ValueSet expBase, ValueSet expDerived) {
    return codesInExpansion(expDerived.getExpansion().getContains(), expBase.getExpansion());
  }

  private ExtensionContext locateExtension(Class<StructureDefinition> class1, String value)  {
    if (value.contains("#")) {
      StructureDefinition ext = context.fetchResource(StructureDefinition.class, value.substring(0, value.indexOf("#")));
      if (ext == null)
        return null;
      String tail = value.substring(value.indexOf("#")+1);
      ElementDefinition ed = null;
      for (ElementDefinition ted : ext.getSnapshot().getElement()) {
        if (tail.equals(ted.getName())) {
          ed = ted;
          return new ExtensionContext(ext, ed);
        }
      }
      return null;
    } else {
      StructureDefinition ext = context.fetchResource(StructureDefinition.class, value);
      if (ext == null)
        return null;
      else
        return new ExtensionContext(ext, ext.getSnapshot().getElement().get(0));
    }
  }

  private ElementDefinitionSlicingComponent makeExtensionSlicing() {
  	ElementDefinitionSlicingComponent slice = new ElementDefinitionSlicingComponent();
    slice.addDiscriminator("url");
    slice.setOrdered(false);
    slice.setRules(SlicingRules.OPEN);
    return slice;
  }

  private String makePathLink(ElementDefinition element) {
    if (!element.hasName())
      return element.getPath();
    if (!element.getPath().contains("."))
      return element.getName();
    return element.getPath().substring(0, element.getPath().lastIndexOf("."))+"."+element.getName();

  }

  private void markDerived(ElementDefinition outcome) {
    for (ElementDefinitionConstraintComponent inv : outcome.getConstraint())
      inv.setUserData(IS_DERIVED, true);
  }

  private boolean onlyInformationIsMapping(List<ElementDefinition> list, ElementDefinition e) {
    return (!e.hasName() && !e.hasSlicing() && (onlyInformationIsMapping(e))) &&
        getChildren(list, e).isEmpty();
  }

  private boolean onlyInformationIsMapping(ElementDefinition d) {
    return !d.hasShort() && !d.hasDefinition() &&
        !d.hasRequirements() && !d.getAlias().isEmpty() && !d.hasMinElement() &&
        !d.hasMax() && !d.getType().isEmpty() && !d.hasContentReference() &&
        !d.hasExample() && !d.hasFixed() && !d.hasMaxLengthElement() &&
        !d.getCondition().isEmpty() && !d.getConstraint().isEmpty() && !d.hasMustSupportElement() &&
        !d.hasBinding();
  }

  private boolean orderMatches(BooleanType diff, BooleanType base) {
    return (diff == null) || (base == null) || (diff.getValue() == base.getValue());
  }

  private ElementDefinition overWriteWithCurrent(ElementDefinition profile, ElementDefinition usage) {
    ElementDefinition res = profile.copy();
    if (usage.hasName())
      res.setName(usage.getName());
    if (usage.hasLabel())
      res.setLabel(usage.getLabel());
    for (Coding c : usage.getCode())
      res.addCode(c);

    if (usage.hasDefinition())
      res.setDefinition(usage.getDefinition());
    if (usage.hasShort())
      res.setShort(usage.getShort());
    if (usage.hasComments())
      res.setComments(usage.getComments());
    if (usage.hasRequirements())
      res.setRequirements(usage.getRequirements());
    for (StringType c : usage.getAlias())
      res.addAlias(c.getValue());
    if (usage.hasMin())
      res.setMin(usage.getMin());
    if (usage.hasMax())
      res.setMax(usage.getMax());

    if (usage.hasFixed())
      res.setFixed(usage.getFixed());
    if (usage.hasPattern())
      res.setPattern(usage.getPattern());
    if (usage.hasExample())
      res.setExample(usage.getExample());
    if (usage.hasMinValue())
      res.setMinValue(usage.getMinValue());
    if (usage.hasMaxValue())
      res.setMaxValue(usage.getMaxValue());
    if (usage.hasMaxLength())
      res.setMaxLength(usage.getMaxLength());
    if (usage.hasMustSupport())
      res.setMustSupport(usage.getMustSupport());
    if (usage.hasBinding())
      res.setBinding(usage.getBinding().copy());
    for (ElementDefinitionConstraintComponent c : usage.getConstraint())
      res.addConstraint(c);

    return res;
  }

  private boolean pathMatches(String p1, String p2) {
    return p1.equals(p2) || (p2.endsWith("[x]") && p1.startsWith(p2.substring(0, p2.length()-3)) && !p1.substring(p2.length()-3).contains("."));
  }

  private boolean pathStartsWith(String p1, String p2) {
    return p1.startsWith(p2);
  }

  private String pathTail(List<ElementDefinition> diffMatches, int i) {

    ElementDefinition d = diffMatches.get(i);
    String s = d.getPath().contains(".") ? d.getPath().substring(d.getPath().lastIndexOf(".")+1) : d.getPath();
    return "."+s + (d.hasType() && d.getType().get(0).hasProfile() ? "["+d.getType().get(0).getProfile().get(0).asStringValue()+"]" : "");
  }

  private int processElementsIntoTree(ElementDefinitionHolder edh, int i, List<ElementDefinition> list) {
    String path = edh.getSelf().getPath();
    final String prefix = path + ".";
    while (i < list.size() && list.get(i).getPath().startsWith(prefix)) {
      ElementDefinitionHolder child = new ElementDefinitionHolder(list.get(i));
      edh.getChildren().add(child);
      i = processElementsIntoTree(child, i+1, list);
    }
    return i;
  }

  /**
   * @param trimDifferential
   * @throws DefinitionException, FHIRException
   * @throws Exception
   */
  private void processPaths(StructureDefinitionSnapshotComponent result, StructureDefinitionSnapshotComponent base, StructureDefinitionDifferentialComponent differential, int baseCursor, int diffCursor, int baseLimit,
      int diffLimit, String url, String profileName, String contextPath, boolean trimDifferential, String contextName, String resultPathBase, boolean slicingDone) throws DefinitionException, FHIRException {

    // just repeat processing entries until we run out of our allowed scope (1st entry, the allowed scope is all the entries)
    while (baseCursor <= baseLimit) {
      // get the current focus of the base, and decide what to do
      ElementDefinition currentBase = base.getElement().get(baseCursor);
      String cpath = fixedPath(contextPath, currentBase.getPath());
      List<ElementDefinition> diffMatches = getDiffMatches(differential, cpath, diffCursor, diffLimit, profileName); // get a list of matching elements in scope

      // in the simple case, source is not sliced.
      if (!currentBase.hasSlicing()) {
        if (diffMatches.isEmpty()) { // the differential doesn't say anything about this item
          // so we just copy it in
          ElementDefinition outcome = updateURLs(url, currentBase.copy());
          outcome.setPath(fixedPath(contextPath, outcome.getPath()));
          updateFromBase(outcome, currentBase);
          markDerived(outcome);
          if (resultPathBase == null)
            resultPathBase = outcome.getPath();
          else if (!outcome.getPath().startsWith(resultPathBase))
            throw new DefinitionException("Adding wrong path");
          result.getElement().add(outcome);
          baseCursor++;
        } else if (diffMatches.size() == 1 && (slicingDone || (!diffMatches.get(0).hasSlicing() && !(isExtension(diffMatches.get(0)) && !diffMatches.get(0).hasName())))) {// one matching element in the differential
          ElementDefinition template = null;
          if (diffMatches.get(0).hasType() && diffMatches.get(0).getType().size() == 1 && diffMatches.get(0).getType().get(0).hasProfile() && !diffMatches.get(0).getType().get(0).getCode().equals("Reference")) {
            String p = diffMatches.get(0).getType().get(0).getProfile().get(0).asStringValue();
            StructureDefinition sd = context.fetchResource(StructureDefinition.class, p);
            if (sd != null) {
              if (!sd.hasSnapshot()) {
                StructureDefinition sdb = context.fetchResource(StructureDefinition.class, sd.getBaseDefinition());
                if (sdb == null)
                  throw new DefinitionException("no base for "+sd.getBaseDefinition());
                generateSnapshot(sdb, sd, sd.getUrl(), sd.getName());
              }
              template = sd.getSnapshot().getElement().get(0).copy().setPath(currentBase.getPath());
              // temporary work around
              if (!diffMatches.get(0).getType().get(0).getCode().equals("Extension")) {
                template.setMin(currentBase.getMin());
                template.setMax(currentBase.getMax());
              }
            }
          }
          if (template == null)
            template = currentBase.copy();
          else
            // some of what's in currentBase overrides template
            template = overWriteWithCurrent(template, currentBase);
          ElementDefinition outcome = updateURLs(url, template);
          outcome.setPath(fixedPath(contextPath, outcome.getPath()));
          updateFromBase(outcome, currentBase);
          if (diffMatches.get(0).hasName())
          outcome.setName(diffMatches.get(0).getName());
          outcome.setSlicing(null);
          updateFromDefinition(outcome, diffMatches.get(0), profileName, trimDifferential, url);
          if (outcome.getPath().endsWith("[x]") && outcome.getType().size() == 1 && !outcome.getType().get(0).getCode().equals("*")) // if the base profile allows multiple types, but the profile only allows one, rename it
            outcome.setPath(outcome.getPath().substring(0, outcome.getPath().length()-3)+Utilities.capitalize(outcome.getType().get(0).getCode()));
          if (resultPathBase == null)
            resultPathBase = outcome.getPath();
          else if (!outcome.getPath().startsWith(resultPathBase))
            throw new DefinitionException("Adding wrong path");
          result.getElement().add(outcome);
          baseCursor++;
          diffCursor = differential.getElement().indexOf(diffMatches.get(0))+1;
          if (differential.getElement().size() > diffCursor && outcome.getPath().contains(".") && isDataType(outcome.getType())) {  // don't want to do this for the root, since that's base, and we're already processing it
            if (pathStartsWith(differential.getElement().get(diffCursor).getPath(), diffMatches.get(0).getPath()+".")) {
              if (outcome.getType().size() > 1)
                throw new DefinitionException(diffMatches.get(0).getPath()+" has children ("+differential.getElement().get(diffCursor).getPath()+") and multiple types ("+typeCode(outcome.getType())+") in profile "+profileName);
              StructureDefinition dt = getProfileForDataType(outcome.getType().get(0));
              if (dt == null)
                throw new DefinitionException(diffMatches.get(0).getPath()+" has children ("+differential.getElement().get(diffCursor).getPath()+") for type "+typeCode(outcome.getType())+" in profile "+profileName+", but can't find type");
              contextName = dt.getUrl();
              int start = diffCursor;
              while (differential.getElement().size() > diffCursor && pathStartsWith(differential.getElement().get(diffCursor).getPath(), diffMatches.get(0).getPath()+"."))
                diffCursor++;
              processPaths(result, dt.getSnapshot(), differential, 1 /* starting again on the data type, but skip the root */, start-1, dt.getSnapshot().getElement().size()-1,
                  diffCursor - 1, url, profileName+pathTail(diffMatches, 0), diffMatches.get(0).getPath(), trimDifferential, contextName, resultPathBase, false);
            }
          }
        } else {
          // ok, the differential slices the item. Let's check our pre-conditions to ensure that this is correct
          if (!unbounded(currentBase) && !isSlicedToOneOnly(diffMatches.get(0)))
            // you can only slice an element that doesn't repeat if the sum total of your slices is limited to 1
            // (but you might do that in order to split up constraints by type)
            throw new DefinitionException("Attempt to a slice an element that does not repeat: "+currentBase.getPath()+"/"+currentBase.getName()+" from "+contextName);
          if (!diffMatches.get(0).hasSlicing() && !isExtension(currentBase)) // well, the diff has set up a slice, but hasn't defined it. this is an error
            throw new DefinitionException("differential does not have a slice: "+currentBase.getPath());

          // well, if it passed those preconditions then we slice the dest.
          // we're just going to accept the differential slicing at face value
          ElementDefinition outcome = updateURLs(url, currentBase.copy());
          outcome.setPath(fixedPath(contextPath, outcome.getPath()));
          updateFromBase(outcome, currentBase);

          if (!diffMatches.get(0).hasSlicing())
            outcome.setSlicing(makeExtensionSlicing());
          else
            outcome.setSlicing(diffMatches.get(0).getSlicing().copy());
          if (!outcome.getPath().startsWith(resultPathBase))
            throw new DefinitionException("Adding wrong path");
          result.getElement().add(outcome);

          // differential - if the first one in the list has a name, we'll process it. Else we'll treat it as the base definition of the slice.
          int start = 0;
          if (!diffMatches.get(0).hasName()) {
            updateFromDefinition(outcome, diffMatches.get(0), profileName, trimDifferential, url);
            if (!outcome.hasType()) {
              throw new DefinitionException("not done yet");
            }
            start = 1;
          } else
            checkExtensionDoco(outcome);

          // now, for each entry in the diff matches, we're going to process the base item
          // our processing scope for base is all the children of the current path
          int nbl = findEndOfElement(base, baseCursor);
          int ndc = diffCursor;
          int ndl = diffCursor;
          for (int i = start; i < diffMatches.size(); i++) {
            // our processing scope for the differential is the item in the list, and all the items before the next one in the list
            ndc = differential.getElement().indexOf(diffMatches.get(i));
            ndl = findEndOfElement(differential, ndc);
            // now we process the base scope repeatedly for each instance of the item in the differential list
            processPaths(result, base, differential, baseCursor, ndc, nbl, ndl, url, profileName+pathTail(diffMatches, i), contextPath, trimDifferential, contextName, resultPathBase, true);
          }
          // ok, done with that - next in the base list
          baseCursor = nbl+1;
          diffCursor = ndl+1;
        }
      } else {
        // the item is already sliced in the base profile.
        // here's the rules
        //  1. irrespective of whether the slicing is ordered or not, the definition order must be maintained
        //  2. slice element names have to match.
        //  3. new slices must be introduced at the end
        // corallory: you can't re-slice existing slices. is that ok?

        // we're going to need this:
        String path = currentBase.getPath();
        ElementDefinition original = currentBase;

        if (diffMatches.isEmpty()) { // the differential doesn't say anything about this item
          // copy across the currentbase, and all of it's children and siblings
          while (baseCursor < base.getElement().size() && base.getElement().get(baseCursor).getPath().startsWith(path)) {
            ElementDefinition outcome = updateURLs(url, base.getElement().get(baseCursor).copy());
            if (!outcome.getPath().startsWith(resultPathBase))
              throw new DefinitionException("Adding wrong path: "+outcome.getPath()+" vs " + resultPathBase);
            result.getElement().add(outcome); // so we just copy it in
            baseCursor++;
          }
        } else {
          // first - check that the slicing is ok
          boolean closed = currentBase.getSlicing().getRules() == SlicingRules.CLOSED;
          int diffpos = 0;
          boolean isExtension = cpath.endsWith(".extension") || cpath.endsWith(".modifierExtension");
          if (diffMatches.get(0).hasSlicing()) { // it might be null if the differential doesn't want to say anything about slicing
            if (!isExtension)
            diffpos++; // if there's a slice on the first, we'll ignore any content it has
            ElementDefinitionSlicingComponent dSlice = diffMatches.get(0).getSlicing();
            ElementDefinitionSlicingComponent bSlice = currentBase.getSlicing();
            if (!orderMatches(dSlice.getOrderedElement(), bSlice.getOrderedElement()))
              throw new DefinitionException("Slicing rules on differential ("+summariseSlicing(dSlice)+") do not match those on base ("+summariseSlicing(bSlice)+") - order @ "+path+" ("+contextName+")");
            if (!discriiminatorMatches(dSlice.getDiscriminator(), bSlice.getDiscriminator()))
             throw new DefinitionException("Slicing rules on differential ("+summariseSlicing(dSlice)+") do not match those on base ("+summariseSlicing(bSlice)+") - disciminator @ "+path+" ("+contextName+")");
            if (!ruleMatches(dSlice.getRules(), bSlice.getRules()))
             throw new DefinitionException("Slicing rules on differential ("+summariseSlicing(dSlice)+") do not match those on base ("+summariseSlicing(bSlice)+") - rule @ "+path+" ("+contextName+")");
          }
          ElementDefinition outcome = updateURLs(url, currentBase.copy());
          outcome.setPath(fixedPath(contextPath, outcome.getPath()));
          updateFromBase(outcome, currentBase);
          if (diffMatches.get(0).hasSlicing() && !isExtension) {
            updateFromSlicing(outcome.getSlicing(), diffMatches.get(0).getSlicing());
            updateFromDefinition(outcome, diffMatches.get(0), profileName, closed, url); // if there's no slice, we don't want to update the unsliced description
          }
          if (diffMatches.get(0).hasSlicing() && !diffMatches.get(0).hasName())
            diffpos++;

          result.getElement().add(outcome);

          // now, we have two lists, base and diff. we're going to work through base, looking for matches in diff.
          List<ElementDefinition> baseMatches = getSiblings(base.getElement(), currentBase);
          for (ElementDefinition baseItem : baseMatches) {
            baseCursor = base.getElement().indexOf(baseItem);
            outcome = updateURLs(url, baseItem.copy());
            updateFromBase(outcome, currentBase);
            outcome.setPath(fixedPath(contextPath, outcome.getPath()));
            outcome.setSlicing(null);
            if (!outcome.getPath().startsWith(resultPathBase))
              throw new DefinitionException("Adding wrong path");
            if (diffpos < diffMatches.size() && diffMatches.get(diffpos).getName().equals(outcome.getName())) {
              // if there's a diff, we update the outcome with diff
              // no? updateFromDefinition(outcome, diffMatches.get(diffpos), profileName, closed, url);
              //then process any children
              int nbl = findEndOfElement(base, baseCursor);
              int ndc = differential.getElement().indexOf(diffMatches.get(diffpos));
              int ndl = findEndOfElement(differential, ndc);
              // now we process the base scope repeatedly for each instance of the item in the differential list
              processPaths(result, base, differential, baseCursor, ndc, nbl, ndl, url, profileName+pathTail(diffMatches, diffpos), contextPath, closed, contextName, resultPathBase, true);
              // ok, done with that - now set the cursors for if this is the end
              baseCursor = nbl+1;
              diffCursor = ndl+1;
              diffpos++;
            } else {
              result.getElement().add(outcome);
              baseCursor++;
              // just copy any children on the base
              while (baseCursor < base.getElement().size() && base.getElement().get(baseCursor).getPath().startsWith(path) && !base.getElement().get(baseCursor).getPath().equals(path)) {
                outcome = updateURLs(url, currentBase.copy());
                outcome.setPath(fixedPath(contextPath, outcome.getPath()));
                if (!outcome.getPath().startsWith(resultPathBase))
                  throw new DefinitionException("Adding wrong path");
                result.getElement().add(outcome);
                baseCursor++;
              }
            }
          }
          // finally, we process any remaining entries in diff, which are new (and which are only allowed if the base wasn't closed
          if (closed && diffpos < diffMatches.size())
            throw new DefinitionException("The base snapshot marks a slicing as closed, but the differential tries to extend it in "+profileName+" at "+path+" ("+cpath+")");
          while (diffpos < diffMatches.size()) {
            ElementDefinition diffItem = diffMatches.get(diffpos);
            for (ElementDefinition baseItem : baseMatches)
              if (baseItem.getName().equals(diffItem.getName()))
                throw new DefinitionException("Named items are out of order in the slice");
            outcome = updateURLs(url, original.copy());
            outcome.setPath(fixedPath(contextPath, outcome.getPath()));
            updateFromBase(outcome, currentBase);
            outcome.setSlicing(null);
            if (!outcome.getPath().startsWith(resultPathBase))
              throw new DefinitionException("Adding wrong path");
            result.getElement().add(outcome);
            updateFromDefinition(outcome, diffItem, profileName, trimDifferential, url);
            diffpos++;
          }
        }
      }
    }
  }

  private boolean ruleMatches(SlicingRules diff, SlicingRules base) {
    return (diff == null) || (base == null) || (diff == base) || (diff == SlicingRules.OPEN) ||
        ((diff == SlicingRules.OPENATEND && base == SlicingRules.CLOSED));
  }

  public void sortDifferential(StructureDefinition base, StructureDefinition diff, String name, List<String> errors)  {

    final List<ElementDefinition> diffList = diff.getDifferential().getElement();
    // first, we move the differential elements into a tree
    ElementDefinitionHolder edh = new ElementDefinitionHolder(diffList.get(0));

    boolean hasSlicing = false;
    List<String> paths = new ArrayList<String>(); // in a differential, slicing may not be stated explicitly
    for(ElementDefinition elt : diffList) {
      if (elt.hasSlicing() || paths.contains(elt.getPath())) {
        hasSlicing = true;
        break;
      }
      paths.add(elt.getPath());
    }
    if(!hasSlicing) {
      // if Differential does not have slicing then safe to pre-sort the list
      // so elements and subcomponents are together
      Collections.sort(diffList, new ElementNameCompare());
    }

    int i = 1;
    processElementsIntoTree(edh, i, diff.getDifferential().getElement());

    // now, we sort the siblings throughout the tree
    ElementDefinitionComparer cmp = new ElementDefinitionComparer(true, base.getSnapshot().getElement(), "", 0, name);
    sortElements(edh, cmp, errors);

    // now, we serialise them back to a list
    diffList.clear();
    writeElements(edh, diffList);
  }

  private void sortElements(ElementDefinitionHolder edh, ElementDefinitionComparer cmp, List<String> errors) {
    if (edh.getChildren().size() == 1)
      // special case - sort needsto allocate base numbers, but there'll be no sort if there's only 1 child. So in that case, we just go ahead and allocated base number directly
      edh.getChildren().get(0).baseIndex = cmp.find(edh.getChildren().get(0).getSelf().getPath());
    else
      Collections.sort(edh.getChildren(), cmp);
    cmp.checkForErrors(errors);

    for (ElementDefinitionHolder child : edh.getChildren()) {
      if (child.getChildren().size() > 0) {
        // what we have to check for here is running off the base profile into a data type profile
        ElementDefinition ed = cmp.snapshot.get(child.getBaseIndex());
        ElementDefinitionComparer ccmp;
        if (ed.getType().isEmpty() || isAbstract(ed.getType().get(0).getCode()) || ed.getType().get(0).getCode().equals(ed.getPath())) {
          ccmp = new ElementDefinitionComparer(true, cmp.snapshot, cmp.base, cmp.prefixLength, cmp.name);
        } else if (ed.getType().get(0).getCode().equals("Extension") && child.getSelf().getType().size() == 1 && child.getSelf().getType().get(0).hasProfile()) {
          ccmp = new ElementDefinitionComparer(true, context.fetchResource(StructureDefinition.class, child.getSelf().getType().get(0).getProfile().get(0).getValue()).getSnapshot().getElement(), ed.getType().get(0).getCode(), child.getSelf().getPath().length(), cmp.name);
        } else if (ed.getType().size() == 1 && !ed.getType().get(0).getCode().equals("*")) {
          ccmp = new ElementDefinitionComparer(false, context.fetchResource(StructureDefinition.class, "http://hl7.org/fhir/StructureDefinition/"+ed.getType().get(0).getCode()).getSnapshot().getElement(), ed.getType().get(0).getCode(), child.getSelf().getPath().length(), cmp.name);
        } else if (child.getSelf().getType().size() == 1) {
          ccmp = new ElementDefinitionComparer(false, context.fetchResource(StructureDefinition.class, "http://hl7.org/fhir/StructureDefinition/"+child.getSelf().getType().get(0).getCode()).getSnapshot().getElement(), child.getSelf().getType().get(0).getCode(), child.getSelf().getPath().length(), cmp.name);
        } else if (ed.getPath().endsWith("[x]") && !child.getSelf().getPath().endsWith("[x]")) {
          String p = child.getSelf().getPath().substring(ed.getPath().length()-3);
          StructureDefinition sd = context.fetchResource(StructureDefinition.class, "http://hl7.org/fhir/StructureDefinition/"+p);
          if (sd == null)
            throw new Error("Unable to find profile "+p);
          ccmp = new ElementDefinitionComparer(false, sd.getSnapshot().getElement(), p, child.getSelf().getPath().length(), cmp.name);
        } else {
          throw new Error("Not handled yet (sortElements: "+ed.getPath()+":"+typeCode(ed.getType())+")");
        }
        sortElements(child, ccmp, errors);
      }
    }
  }

  private boolean standardExtensionSlicing(ElementDefinition element) {
    String t = tail(element.getPath());
    return (t.equals("extension") || t.equals("modifierExtension"))
          && element.getSlicing().getRules() != SlicingRules.CLOSED && element.getSlicing().getDiscriminator().size() == 1 && element.getSlicing().getDiscriminator().get(0).getValue().equals("url");
  }

  private String summariseSlicing(ElementDefinitionSlicingComponent slice) {
    StringBuilder b = new StringBuilder();
    boolean first = true;
    for (StringType d : slice.getDiscriminator()) {
      if (first)
        first = false;
      else
        b.append(", ");
      b.append(d);
    }
    b.append("(");
    if (slice.hasOrdered())
      b.append(slice.getOrderedElement().asStringValue());
    b.append("/");
    if (slice.hasRules())
      b.append(slice.getRules().toCode());
    b.append(")");
    if (slice.hasDescription()) {
      b.append(" \"");
      b.append(slice.getDescription());
      b.append("\"");
    }
    return b.toString();
  }

  private String tail(String path) {
    if (path.contains("."))
      return path.substring(path.lastIndexOf('.')+1);
    else
      return path;
  }

  private boolean unbounded(ElementDefinition definition) {
    StringType max = definition.getMaxElement();
    if (max == null)
      return false; // this is not valid
    if (max.getValue().equals("1"))
      return false;
    if (max.getValue().equals("0"))
      return false;
    return true;
  }

  private void updateFromBase(ElementDefinition derived, ElementDefinition base) {
    if (base.hasBase()) {
      derived.getBase().setPath(base.getBase().getPath());
      derived.getBase().setMin(base.getBase().getMin());
      derived.getBase().setMax(base.getBase().getMax());
    } else {
      derived.getBase().setPath(base.getPath());
      derived.getBase().setMin(base.getMin());
      derived.getBase().setMax(base.getMax());
    }
  }

  private void updateFromDefinition(ElementDefinition dest, ElementDefinition source, String pn, boolean trimDifferential, String purl) throws DefinitionException, FHIRException {
    // we start with a clone of the base profile ('dest') and we copy from the profile ('source')
    // over the top for anything the source has
    ElementDefinition base = dest;
    ElementDefinition derived = source;
    derived.setUserData(DERIVATION_POINTER, base);

    if (derived != null) {
      boolean isExtension = checkExtensionDoco(base);

      if (derived.hasShortElement()) {
        if (!Base.compareDeep(derived.getShortElement(), base.getShortElement(), false))
          base.setShortElement(derived.getShortElement().copy());
        else if (trimDifferential)
          derived.setShortElement(null);
        else if (derived.hasShortElement())
          derived.getShortElement().setUserData(DERIVATION_EQUALS, true);
      }

      if (derived.hasDefinitionElement()) {
        if (derived.getDefinition().startsWith("..."))
          base.setDefinition(base.getDefinition()+"\r\n"+derived.getDefinition().substring(3));
        else if (!Base.compareDeep(derived.getDefinitionElement(), base.getDefinitionElement(), false))
          base.setDefinitionElement(derived.getDefinitionElement().copy());
        else if (trimDifferential)
          derived.setDefinitionElement(null);
        else if (derived.hasDefinitionElement())
          derived.getDefinitionElement().setUserData(DERIVATION_EQUALS, true);
      }

      if (derived.hasCommentsElement()) {
        if (derived.getComments().startsWith("..."))
          base.setComments(base.getComments()+"\r\n"+derived.getComments().substring(3));
        else if (!Base.compareDeep(derived.getCommentsElement(), base.getCommentsElement(), false))
          base.setCommentsElement(derived.getCommentsElement().copy());
        else if (trimDifferential)
          base.setCommentsElement(derived.getCommentsElement().copy());
        else if (derived.hasCommentsElement())
          derived.getCommentsElement().setUserData(DERIVATION_EQUALS, true);
      }

      if (derived.hasLabelElement()) {
        if (derived.getLabel().startsWith("..."))
          base.setLabel(base.getLabel()+"\r\n"+derived.getLabel().substring(3));
        else if (!Base.compareDeep(derived.getLabelElement(), base.getLabelElement(), false))
          base.setLabelElement(derived.getLabelElement().copy());
        else if (trimDifferential)
          base.setLabelElement(derived.getLabelElement().copy());
        else if (derived.hasLabelElement())
          derived.getLabelElement().setUserData(DERIVATION_EQUALS, true);
      }

      if (derived.hasRequirementsElement()) {
        if (derived.getRequirements().startsWith("..."))
          base.setRequirements(base.getRequirements()+"\r\n"+derived.getRequirements().substring(3));
        else if (!Base.compareDeep(derived.getRequirementsElement(), base.getRequirementsElement(), false))
          base.setRequirementsElement(derived.getRequirementsElement().copy());
        else if (trimDifferential)
          base.setRequirementsElement(derived.getRequirementsElement().copy());
        else if (derived.hasRequirementsElement())
          derived.getRequirementsElement().setUserData(DERIVATION_EQUALS, true);
      }
      // sdf-9
      if (derived.hasRequirements() && !base.getPath().contains("."))
        derived.setRequirements(null);
      if (base.hasRequirements() && !base.getPath().contains("."))
        base.setRequirements(null);

      if (derived.hasAlias()) {
        if (!Base.compareDeep(derived.getAlias(), base.getAlias(), false))
          for (StringType s : derived.getAlias()) {
            if (!base.hasAlias(s.getValue()))
              base.getAlias().add(s.copy());
          }
        else if (trimDifferential)
          derived.getAlias().clear();
        else
          for (StringType t : derived.getAlias())
            t.setUserData(DERIVATION_EQUALS, true);
      }

      if (derived.hasMinElement()) {
        if (!Base.compareDeep(derived.getMinElement(), base.getMinElement(), false)) {
          if (derived.getMin() < base.getMin())
            messages.add(new ValidationMessage(Source.ProfileValidator, IssueType.BUSINESSRULE, pn+"."+derived.getPath(), "Derived min  ("+Integer.toString(derived.getMin())+") cannot be less than base min ("+Integer.toString(base.getMin())+")", IssueSeverity.ERROR));
          base.setMinElement(derived.getMinElement().copy());
        } else if (trimDifferential)
          derived.setMinElement(null);
        else
          derived.getMinElement().setUserData(DERIVATION_EQUALS, true);
      }

      if (derived.hasMaxElement()) {
        if (!Base.compareDeep(derived.getMaxElement(), base.getMaxElement(), false)) {
          if (isLargerMax(derived.getMax(), base.getMax()))
            messages.add(new ValidationMessage(Source.ProfileValidator, IssueType.BUSINESSRULE, pn+"."+derived.getPath(), "Derived max ("+derived.getMax()+") cannot be greater than base max ("+base.getMax()+")", IssueSeverity.ERROR));
          base.setMaxElement(derived.getMaxElement().copy());
        } else if (trimDifferential)
          derived.setMaxElement(null);
        else
          derived.getMaxElement().setUserData(DERIVATION_EQUALS, true);
      }

      if (derived.hasFixed()) {
        if (!Base.compareDeep(derived.getFixed(), base.getFixed(), true)) {
          base.setFixed(derived.getFixed().copy());
        } else if (trimDifferential)
          derived.setFixed(null);
        else
          derived.getFixed().setUserData(DERIVATION_EQUALS, true);
      }

      if (derived.hasPattern()) {
        if (!Base.compareDeep(derived.getPattern(), base.getPattern(), false)) {
          base.setPattern(derived.getPattern().copy());
        } else
          if (trimDifferential)
            derived.setPattern(null);
          else
            derived.getPattern().setUserData(DERIVATION_EQUALS, true);
      }

      if (derived.hasExample()) {
        if (!Base.compareDeep(derived.getExample(), base.getExample(), false))
          base.setExample(derived.getExample().copy());
        else if (trimDifferential)
          derived.setExample(null);
        else
          derived.getExample().setUserData(DERIVATION_EQUALS, true);
      }

      if (derived.hasMaxLengthElement()) {
        if (!Base.compareDeep(derived.getMaxLengthElement(), base.getMaxLengthElement(), false))
          base.setMaxLengthElement(derived.getMaxLengthElement().copy());
        else if (trimDifferential)
          derived.setMaxLengthElement(null);
        else
          derived.getMaxLengthElement().setUserData(DERIVATION_EQUALS, true);
      }

      // todo: what to do about conditions?
      // condition : id 0..*

      if (derived.hasMustSupportElement()) {
        if (!Base.compareDeep(derived.getMustSupportElement(), base.getMustSupportElement(), false))
          base.setMustSupportElement(derived.getMustSupportElement().copy());
        else if (trimDifferential)
          derived.setMustSupportElement(null);
        else
          derived.getMustSupportElement().setUserData(DERIVATION_EQUALS, true);
      }


      // profiles cannot change : isModifier, defaultValue, meaningWhenMissing
      // but extensions can change isModifier
      if (isExtension) {
        if (!Base.compareDeep(derived.getIsModifierElement(), base.getIsModifierElement(), false))
          base.setIsModifierElement(derived.getIsModifierElement().copy());
        else if (trimDifferential)
          derived.setIsModifierElement(null);
        else
          derived.getIsModifierElement().setUserData(DERIVATION_EQUALS, true);
      }

      if (derived.hasBinding()) {
        if (!Base.compareDeep(derived.getBinding(), base.getBinding(), false)) {
          if (base.hasBinding() && base.getBinding().getStrength() == BindingStrength.REQUIRED && derived.getBinding().getStrength() != BindingStrength.REQUIRED)
            messages.add(new ValidationMessage(Source.ProfileValidator, IssueType.BUSINESSRULE, pn+"."+derived.getPath(), "illegal attempt to change a binding from "+base.getBinding().getStrength().toCode()+" to "+derived.getBinding().getStrength().toCode(), IssueSeverity.ERROR));
//            throw new DefinitionException("StructureDefinition "+pn+" at "+derived.getPath()+": illegal attempt to change a binding from "+base.getBinding().getStrength().toCode()+" to "+derived.getBinding().getStrength().toCode());
          else if (base.hasBinding() && derived.hasBinding() && base.getBinding().getStrength() == BindingStrength.REQUIRED && base.getBinding().hasValueSetReference() && derived.getBinding().hasValueSetReference()) {
            ValueSetExpansionOutcome expBase = context.expandVS(context.fetchResource(ValueSet.class, base.getBinding().getValueSetReference().getReference()), true);
            ValueSetExpansionOutcome expDerived = context.expandVS(context.fetchResource(ValueSet.class, derived.getBinding().getValueSetReference().getReference()), true);
            if (expBase.getValueset() == null)
              messages.add(new ValidationMessage(Source.ProfileValidator, IssueType.BUSINESSRULE, pn+"."+base.getPath(), "Binding "+base.getBinding().getValueSetReference().getReference()+" could not be expanded", IssueSeverity.WARNING));
            else if (expDerived.getValueset() == null)
              messages.add(new ValidationMessage(Source.ProfileValidator, IssueType.BUSINESSRULE, pn+"."+derived.getPath(), "Binding "+derived.getBinding().getValueSetReference().getReference()+" could not be expanded", IssueSeverity.WARNING));
            else if (!isSubset(expBase.getValueset(), expDerived.getValueset()))
              messages.add(new ValidationMessage(Source.ProfileValidator, IssueType.BUSINESSRULE, pn+"."+derived.getPath(), "Binding "+derived.getBinding().getValueSetReference().getReference()+" is not a subset of binding "+base.getBinding().getValueSetReference().getReference(), IssueSeverity.ERROR));
          }
          base.setBinding(derived.getBinding().copy());
        } else if (trimDifferential)
          derived.setBinding(null);
        else
          derived.getBinding().setUserData(DERIVATION_EQUALS, true);
      } // else if (base.hasBinding() && doesn't have bindable type )
        //  base

      if (derived.hasIsSummaryElement()) {
        if (!Base.compareDeep(derived.getIsSummaryElement(), base.getIsSummaryElement(), false))
          base.setIsSummaryElement(derived.getIsSummaryElement().copy());
        else if (trimDifferential)
          derived.setIsSummaryElement(null);
        else
          derived.getIsSummaryElement().setUserData(DERIVATION_EQUALS, true);
      }

      if (derived.hasType()) {
        if (!Base.compareDeep(derived.getType(), base.getType(), false)) {
          if (base.hasType()) {
            for (TypeRefComponent ts : derived.getType()) {
              boolean ok = false;
              CommaSeparatedStringBuilder b = new CommaSeparatedStringBuilder();
              for (TypeRefComponent td : base.getType()) {
                b.append(td.getCode());
                if (td.hasCode() && (td.getCode().equals(ts.getCode()) || td.getCode().equals("Extension") ||
                    td.getCode().equals("Element") || td.getCode().equals("*") ||
                    ((td.getCode().equals("Resource") || (td.getCode().equals("DomainResource")) && pkp.isResource(ts.getCode())))))
                  ok = true;
              }
              if (!ok)
                throw new DefinitionException("StructureDefinition "+pn+" at "+derived.getPath()+": illegal constrained type "+ts.getCode()+" from "+b.toString());
            }
          }
          base.getType().clear();
          for (TypeRefComponent t : derived.getType()) {
            TypeRefComponent tt = t.copy();
//            tt.setUserData(DERIVATION_EQUALS, true);
            base.getType().add(tt);
          }
        }
        else if (trimDifferential)
          derived.getType().clear();
        else
          for (TypeRefComponent t : derived.getType())
            t.setUserData(DERIVATION_EQUALS, true);
      }

      if (derived.hasMapping()) {
        // todo: mappings are not cumulative - one replaces another
        if (!Base.compareDeep(derived.getMapping(), base.getMapping(), false)) {
          for (ElementDefinitionMappingComponent s : derived.getMapping()) {
            boolean found = false;
            for (ElementDefinitionMappingComponent d : base.getMapping()) {
              found = found || (d.getIdentity().equals(s.getIdentity()) && d.getMap().equals(s.getMap()));
            }
            if (!found)
              base.getMapping().add(s);
          }
        }
        else if (trimDifferential)
          derived.getMapping().clear();
        else
          for (ElementDefinitionMappingComponent t : derived.getMapping())
            t.setUserData(DERIVATION_EQUALS, true);
      }

      // todo: constraints are cumulative. there is no replacing
      for (ElementDefinitionConstraintComponent s : base.getConstraint())
        s.setUserData(IS_DERIVED, true);
      if (derived.hasConstraint()) {
      	for (ElementDefinitionConstraintComponent s : derived.getConstraint()) {
      	  base.getConstraint().add(s.copy());
      	}
      }
    }
  }

  private void updateFromSlicing(ElementDefinitionSlicingComponent dst, ElementDefinitionSlicingComponent src) {
    if (src.hasOrderedElement())
      dst.setOrderedElement(src.getOrderedElement().copy());
    if (src.hasDiscriminator())
      dst.getDiscriminator().addAll(src.getDiscriminator());
    if (src.hasRulesElement())
      dst.setRulesElement(src.getRulesElement().copy());
  }

  /**
   * Finds internal references in an Element's Binding and StructureDefinition references (in TypeRef) and bases them on the given url
   * @param url - the base url to use to turn internal references into absolute references
   * @param element - the Element to update
   * @return - the updated Element
   */
  private ElementDefinition updateURLs(String url, ElementDefinition element) {
    if (element != null) {
      ElementDefinition defn = element;
      if (defn.hasBinding() && defn.getBinding().getValueSet() instanceof Reference && ((Reference)defn.getBinding().getValueSet()).getReference().startsWith("#"))
        ((Reference)defn.getBinding().getValueSet()).setReference(url+((Reference)defn.getBinding().getValueSet()).getReference());
      for (TypeRefComponent t : defn.getType()) {
        for (UriType tp : t.getProfile()) {
        	if (tp.getValue().startsWith("#"))
            tp.setValue(url+t.getProfile());
        }
      }
    }
    return element;
  }

  private String urltail(String path) {
    if (path.contains("#"))
      return path.substring(path.lastIndexOf('#')+1);
    if (path.contains("/"))
      return path.substring(path.lastIndexOf('/')+1);
    else
      return path;

  }

  private void writeElements(ElementDefinitionHolder edh, List<ElementDefinition> list) {
    list.add(edh.getSelf());
    for (ElementDefinitionHolder child : edh.getChildren()) {
      writeElements(child, list);
    }
  }

//  private static String listStructures(StructureDefinition p) {
//    StringBuilder b = new StringBuilder();
//    boolean first = true;
//    for (ProfileStructureComponent s : p.getStructure()) {
//      if (first)
//        first = false;
//      else
//        b.append(", ");
//      if (pkp != null && pkp.hasLinkFor(s.getType()))
//        b.append("<a href=\""+pkp.getLinkFor(s.getType())+"\">"+s.getType()+"</a>");
//      else
//        b.append(s.getType());
//    }
//    return b.toString();
//  }

  public static String describeExtensionContext(StructureDefinition ext) {
    CommaSeparatedStringBuilder b = new CommaSeparatedStringBuilder();
    for (StringType t : ext.getContext())
      b.append(t.getValue());
    if (!ext.hasContextType())
      throw new Error("no context type on "+ext.getUrl());
    switch (ext.getContextType()) {
    case DATATYPE: return "Use on data type: "+b.toString();
    case EXTENSION: return "Use on extension: "+b.toString();
    case RESOURCE: return "Use on element: "+b.toString();
    default:
      return "??";
    }
  }

  public static List<ElementDefinition> getChildList(StructureDefinition structure, ElementDefinition element) {
	  	return getChildList(structure, element.getPath());
	  }

  /**
   * Given a Structure, navigate to the element given by the path and return the direct children of that element
   *
   * @param path The path of the element within the structure to get the children for
   * @return A List containing the element children (all of them are Elements)
   */
  public static List<ElementDefinition> getChildList(StructureDefinition profile, String path) {
    List<ElementDefinition> res = new ArrayList<ElementDefinition>();

    for (ElementDefinition e : profile.getSnapshot().getElement())
    {
      String p = e.getPath();

      if (!Utilities.noString(e.getContentReference()) && path.startsWith(p))
      {
        if (path.length() > p.length())
          return getChildList(profile, e.getContentReference()+"."+path.substring(p.length()+1));
        else
          return getChildList(profile, e.getContentReference());
      }
      else if (p.startsWith(path+".") && !p.equals(path))
      {
          String tail = p.substring(path.length()+1);
          if (!tail.contains(".")) {
            res.add(e);
          }
        }

      }

    return res;
  }

  public static List<ElementDefinition> getChildMap(StructureDefinition profile, ElementDefinition element) throws DefinitionException {
	  	return getChildMap(profile, element.getName(), element.getPath(), element.getContentReference());
  }

/**
 * Given a Structure, navigate to the element given by the path and return the direct children of that element
 *
 * @param path The path of the element within the structure to get the children for
 * @return A Map containing the name of the element child (not the path) and the child itself (an Element)
 * @throws DefinitionException
 * @throws Exception
 */
  public static List<ElementDefinition> getChildMap(StructureDefinition profile, String name, String path, String contentReference) throws DefinitionException {
    List<ElementDefinition> res = new ArrayList<ElementDefinition>();

    // if we have a name reference, we have to find it, and iterate it's children
    if (contentReference != null) {
    	boolean found = false;
      for (ElementDefinition e : profile.getSnapshot().getElement()) {
      	if (contentReference.equals("#"+e.getId())) {
      		found = true;
      		path = e.getPath();
      	}
      }
      if (!found)
      	throw new DefinitionException("Unable to resolve name reference "+contentReference+" at path "+path);
    }

    for (ElementDefinition e : profile.getSnapshot().getElement())
    {
      String p = e.getPath();

      if (path != null && !Utilities.noString(e.getContentReference()) && path.startsWith(p))
      {
    	/* The path we are navigating to is on or below this element, but the element defers its definition to another named part of the
    	 * structure.
    	 */
        if (path.length() > p.length())
        {
          // The path navigates further into the referenced element, so go ahead along the path over there
          return getChildMap(profile, name, e.getContentReference()+"."+path.substring(p.length()+1), null);
        }
        else
        {
          // The path we are looking for is actually this element, but since it defers it definition, go get the referenced element
          return getChildMap(profile, name, e.getContentReference(), null);
        }
      }
      else if (p.startsWith(path+"."))
      {
    	  // The path of the element is a child of the path we're looking for (i.e. the parent),
    	  // so add this element to the result.
          String tail = p.substring(path.length()+1);

          // Only add direct children, not any deeper paths
          if (!tail.contains(".")) {
            res.add(e);
          }
        }
      }

    return res;
  }

  public static boolean isPrimitive(String value) {
    return value == null || Utilities.existsInListNC(value, "boolean", "integer", "decimal", "base64Binary", "instant", "string", "date", "dateTime", "code", "oid", "uuid", "id", "uri");
  }

  public static String typeCode(List<TypeRefComponent> types) {
    StringBuilder b = new StringBuilder();
    boolean first = true;
    for (TypeRefComponent type : types) {
      if (first) first = false; else b.append(", ");
      b.append(type.getCode());
      if (type.hasProfile())
        b.append("{"+type.getProfile()+"}");
    }
    return b.toString();
  }


  public interface ProfileKnowledgeProvider {
    String getLinkFor(String typeSimple);

    String getLinkForProfile(StructureDefinition profile, String url);

    boolean hasLinkFor(String typeSimple);

    boolean isDatatype(String typeSimple);

    boolean isResource(String typeSimple);

    BindingResolution resolveBinding(ElementDefinitionBindingComponent binding);

    public class BindingResolution {
      public String display;
      public String url;
    }
  }

  public class ExtensionContext {

    private ElementDefinition element;
    private StructureDefinition defn;

    public ExtensionContext(StructureDefinition ext, ElementDefinition ed) {
      this.defn = ext;
      this.element = ed;
    }

    public StructureDefinition getDefn() {
      return defn;
    }

    public ElementDefinition getElement() {
      return element;
    }

    public ElementDefinition getExtensionValueDefinition() {
      int i = defn.getSnapshot().getElement().indexOf(element)+1;
      while (i < defn.getSnapshot().getElement().size()) {
        ElementDefinition ed = defn.getSnapshot().getElement().get(i);
        if (ed.getPath().equals(element.getPath()))
          return null;
        if (ed.getPath().startsWith(element.getPath()+".value"))
          return ed;
        i++;
      }
      return null;
    }

    public String getUrl() {
      if (element == defn.getSnapshot().getElement().get(0))
        return defn.getUrl();
      else
        return element.getName();
    }

  }

  // generate schematroins for the rules in a structure definition

  private class UnusedTracker {
    private boolean used;
  }

  private class Slicer extends ElementDefinitionSlicingComponent {
    String criteria = "";
    String name = "";   
    boolean check;
    public Slicer(boolean cantCheck) {
      super();
      this.check = cantCheck;
    }
  }
  
  public static class ElementDefinitionHolder {
    private String name;
    private ElementDefinition self;
    private int baseIndex = 0;
    private List<ElementDefinitionHolder> children;

    public ElementDefinitionHolder(ElementDefinition self) {
      super();
      this.self = self;
      this.name = self.getPath();
      children = new ArrayList<ElementDefinitionHolder>();
    }

    public int getBaseIndex() {
      return baseIndex;
    }

    public void setBaseIndex(int baseIndex) {
      this.baseIndex = baseIndex;
    }

    public List<ElementDefinitionHolder> getChildren() {
      return children;
    }

    public ElementDefinition getSelf() {
      return self;
    }

  }

  public static class ElementDefinitionComparer implements Comparator<ElementDefinitionHolder> {

    private boolean inExtension;
    private List<ElementDefinition> snapshot;
    private int prefixLength;
    private String base;
    private String name;
    private Set<String> errors = new HashSet<String>();

    public ElementDefinitionComparer(boolean inExtension, List<ElementDefinition> snapshot, String base, int prefixLength, String name) {
      this.inExtension = inExtension;
      this.snapshot = snapshot;
      this.prefixLength = prefixLength;
      this.base = base;
      this.name = name;
    }

    public void checkForErrors(List<String> errorList) {
      if (errors.size() > 0) {
//        CommaSeparatedStringBuilder b = new CommaSeparatedStringBuilder();
//        for (String s : errors)
//          b.append("StructureDefinition "+name+": "+s);
//        throw new DefinitionException(b.toString());
        for (String s : errors)
          if (s.startsWith("!"))
            errorList.add("!StructureDefinition "+name+": "+s.substring(1));
          else
            errorList.add("StructureDefinition "+name+": "+s);
      }
    }

    @Override
    public int compare(ElementDefinitionHolder o1, ElementDefinitionHolder o2) {
      if (o1.getBaseIndex() == 0)
        o1.setBaseIndex(find(o1.getSelf().getPath()));
      if (o2.getBaseIndex() == 0)
        o2.setBaseIndex(find(o2.getSelf().getPath()));
      return o1.getBaseIndex() - o2.getBaseIndex();
    }

    private int find(String path) {
      String actual = base+path.substring(prefixLength);
      for (int i = 0; i < snapshot.size(); i++) {
        String p = snapshot.get(i).getPath();
        if (p.equals(actual))
          return i;
        if (p.endsWith("[x]") && actual.startsWith(p.substring(0, p.length()-3)) && !(actual.endsWith("[x]")) && !actual.substring(p.length()-3).contains("."))
          return i;
      }
      if (prefixLength == 0)
        errors.add("Differential contains path "+path+" which is not found in the base");
      else
        errors.add("Differential contains path "+path+" which is actually "+actual+", which is not found in the base");
      return 0;
    }
  }

  /**
   * First compare element by path then by name if same
   */
  private static class ElementNameCompare implements Comparator<ElementDefinition> {

    @Override
    public int compare(ElementDefinition o1, ElementDefinition o2) {
      String path1 = normalizePath(o1);
      String path2 = normalizePath(o2);
      int cmp = path1.compareTo(path2);
      if (cmp == 0) {
        String name1 = o1.hasName() ? o1.getName() : "";
        String name2 = o2.hasName() ? o2.getName() : "";
        cmp = name1.compareTo(name2);
      }
      return cmp;
    }

    private static String normalizePath(ElementDefinition e) {
      if (!e.hasPath()) return "";
      String path = e.getPath();
      // if sorting element names make sure onset[x] appears before onsetAge, onsetDate, etc.
      // so strip off the [x] suffix when comparing the path names.
      if (path.endsWith("[x]")) {
        path = path.substring(0, path.length()-3);
      }
      return path;
    }

  }

//
//private void generateForChild(TextStreamWriter txt,
//    StructureDefinition structure, ElementDefinition child) {
//  // TODO Auto-generated method stub
//
//}


}
