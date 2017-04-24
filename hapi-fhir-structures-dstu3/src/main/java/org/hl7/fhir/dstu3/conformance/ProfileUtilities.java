package org.hl7.fhir.dstu3.conformance;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hl7.fhir.dstu3.conformance.ProfileUtilities.ProfileKnowledgeProvider.BindingResolution;
import org.hl7.fhir.dstu3.context.IWorkerContext;
import org.hl7.fhir.dstu3.context.IWorkerContext.ValidationResult;
import org.hl7.fhir.dstu3.elementmodel.ObjectConverter;
import org.hl7.fhir.dstu3.elementmodel.Property;
import org.hl7.fhir.dstu3.formats.IParser;
import org.hl7.fhir.dstu3.model.Base;
import org.hl7.fhir.dstu3.model.BooleanType;
import org.hl7.fhir.dstu3.model.CodeType;
import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.Element;
import org.hl7.fhir.dstu3.model.ElementDefinition;
import org.hl7.fhir.dstu3.model.ElementDefinition.AggregationMode;
import org.hl7.fhir.dstu3.model.ElementDefinition.DiscriminatorType;
import org.hl7.fhir.dstu3.model.ElementDefinition.ElementDefinitionBaseComponent;
import org.hl7.fhir.dstu3.model.ElementDefinition.ElementDefinitionBindingComponent;
import org.hl7.fhir.dstu3.model.ElementDefinition.ElementDefinitionConstraintComponent;
import org.hl7.fhir.dstu3.model.ElementDefinition.ElementDefinitionExampleComponent;
import org.hl7.fhir.dstu3.model.ElementDefinition.ElementDefinitionMappingComponent;
import org.hl7.fhir.dstu3.model.ElementDefinition.ElementDefinitionSlicingComponent;
import org.hl7.fhir.dstu3.model.ElementDefinition.ElementDefinitionSlicingDiscriminatorComponent;
import org.hl7.fhir.dstu3.model.ElementDefinition.SlicingRules;
import org.hl7.fhir.dstu3.model.ElementDefinition.TypeRefComponent;
import org.hl7.fhir.dstu3.model.Enumeration;
import org.hl7.fhir.dstu3.model.Enumerations.BindingStrength;
import org.hl7.fhir.dstu3.model.Extension;
import org.hl7.fhir.dstu3.model.IntegerType;
import org.hl7.fhir.dstu3.model.PrimitiveType;
import org.hl7.fhir.dstu3.model.Quantity;
import org.hl7.fhir.dstu3.model.Reference;
import org.hl7.fhir.dstu3.model.Resource;
import org.hl7.fhir.dstu3.model.StringType;
import org.hl7.fhir.dstu3.model.StructureDefinition;
import org.hl7.fhir.dstu3.model.StructureDefinition.StructureDefinitionDifferentialComponent;
import org.hl7.fhir.dstu3.model.StructureDefinition.StructureDefinitionKind;
import org.hl7.fhir.dstu3.model.StructureDefinition.StructureDefinitionMappingComponent;
import org.hl7.fhir.dstu3.model.StructureDefinition.StructureDefinitionSnapshotComponent;
import org.hl7.fhir.dstu3.model.StructureDefinition.TypeDerivationRule;
import org.hl7.fhir.dstu3.model.Type;
import org.hl7.fhir.dstu3.model.UriType;
import org.hl7.fhir.dstu3.model.ValueSet;
import org.hl7.fhir.dstu3.model.ValueSet.ValueSetExpansionComponent;
import org.hl7.fhir.dstu3.model.ValueSet.ValueSetExpansionContainsComponent;
import org.hl7.fhir.dstu3.terminologies.ValueSetExpander.ValueSetExpansionOutcome;
import org.hl7.fhir.dstu3.utils.NarrativeGenerator;
import org.hl7.fhir.dstu3.utils.ToolingExtensions;
import org.hl7.fhir.dstu3.utils.TranslatingUtilities;
import org.hl7.fhir.exceptions.DefinitionException;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.utilities.CommaSeparatedStringBuilder;
import org.hl7.fhir.utilities.Utilities;
import org.hl7.fhir.utilities.validation.ValidationMessage;
import org.hl7.fhir.utilities.validation.ValidationMessage.Source;
import org.hl7.fhir.utilities.xhtml.HierarchicalTableGenerator;
import org.hl7.fhir.utilities.xhtml.HierarchicalTableGenerator.Cell;
import org.hl7.fhir.utilities.xhtml.HierarchicalTableGenerator.Piece;
import org.hl7.fhir.utilities.xhtml.HierarchicalTableGenerator.Row;
import org.hl7.fhir.utilities.xhtml.HierarchicalTableGenerator.TableModel;
import org.hl7.fhir.utilities.xml.SchematronWriter;
import org.hl7.fhir.utilities.xml.SchematronWriter.Rule;
import org.hl7.fhir.utilities.xml.SchematronWriter.SchematronType;
import org.hl7.fhir.utilities.xml.SchematronWriter.Section;

/**
 * This class provides a set of utility operations for working with Profiles.
 * Key functionality:
 *  * getChildMap --?
 *  * getChildList
 *  * generateSnapshot: Given a base (snapshot) profile structure, and a differential profile, generate a new snapshot profile
 *  * closeDifferential: fill out a differential by excluding anything not mentioned
 *  * generateExtensionsTable: generate the HTML for a hierarchical table presentation of the extensions
 *  * generateTable: generate  the HTML for a hierarchical table presentation of a structure
 *  * generateSpanningTable: generate the HTML for a table presentation of a network of structures, starting at a nominated point
 *  * summarise: describe the contents of a profile
 *  
 * note to maintainers: Do not make modifications to the snapshot generation without first changing the snapshot generation test cases to demonstrate the grounds for your change
 *  
 * @author Grahame
 *
 */
public class ProfileUtilities extends TranslatingUtilities {

  private static int nextSliceId = 0;
  
  public class ExtensionContext {

    private ElementDefinition element;
    private StructureDefinition defn;

    public ExtensionContext(StructureDefinition ext, ElementDefinition ed) {
      this.defn = ext;
      this.element = ed;
    }

    public ElementDefinition getElement() {
      return element;
    }

    public StructureDefinition getDefn() {
      return defn;
    }

    public String getUrl() {
      if (element == defn.getSnapshot().getElement().get(0))
        return defn.getUrl();
      else
        return element.getSliceName();
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
    
  }

  private static final String ROW_COLOR_ERROR = "#ffcccc";
  private static final String ROW_COLOR_FATAL = "#ff9999";
  private static final String ROW_COLOR_WARNING = "#ffebcc";
  private static final String ROW_COLOR_HINT = "#ebf5ff";
  private static final String ROW_COLOR_NOT_MUST_SUPPORT = "#d6eaf8";
  public static final int STATUS_OK = 0;
  public static final int STATUS_HINT = 1;
  public static final int STATUS_WARNING = 2;
  public static final int STATUS_ERROR = 3;
  public static final int STATUS_FATAL = 4;


  private static final String DERIVATION_EQUALS = "derivation.equals";
  public static final String DERIVATION_POINTER = "derived.pointer";
  public static final String IS_DERIVED = "derived.fact";
  public static final String UD_ERROR_STATUS = "error-status";
  private static final String GENERATED_IN_SNAPSHOT = "profileutilities.snapshot.processed";

  // note that ProfileUtilities are used re-entrantly internally, so nothing with process state can be here
  private final IWorkerContext context;
  private List<ValidationMessage> messages;
  private List<String> snapshotStack = new ArrayList<String>();
  private ProfileKnowledgeProvider pkp;
  private boolean igmode;

  public ProfileUtilities(IWorkerContext context, List<ValidationMessage> messages, ProfileKnowledgeProvider pkp) {
    super();
    this.context = context;
    this.messages = messages;
    this.pkp = pkp;
  }

  private class UnusedTracker {
    private boolean used;
  }

  public boolean isIgmode() {
    return igmode;
  }


  public void setIgmode(boolean igmode) {
    this.igmode = igmode;
  }

  public interface ProfileKnowledgeProvider {
    public class BindingResolution {
      public String display;
      public String url;
    }
    boolean isDatatype(String typeSimple);
    boolean isResource(String typeSimple);
    boolean hasLinkFor(String typeSimple);
    String getLinkFor(String corePath, String typeSimple);
    BindingResolution resolveBinding(StructureDefinition def, ElementDefinitionBindingComponent binding, String path);
    String getLinkForProfile(StructureDefinition profile, String url);
    boolean prependLinks();
  }



  public static List<ElementDefinition> getChildMap(StructureDefinition profile, ElementDefinition element) throws DefinitionException {
    if (element.getContentReference()!=null) {
      for (ElementDefinition e : profile.getSnapshot().getElement()) {
        if (element.getContentReference().equals("#"+e.getId()))
          return getChildMap(profile, e);
      }
      throw new DefinitionException("Unable to resolve name reference "+element.getContentReference()+" at path "+element.getPath());

    } else {
      List<ElementDefinition> res = new ArrayList<ElementDefinition>();
      List<ElementDefinition> elements = profile.getSnapshot().getElement();
      String path = element.getPath();
      for (int index = elements.indexOf(element) + 1; index < elements.size(); index++) {
        ElementDefinition e = elements.get(index);
        if (e.getPath().startsWith(path + ".")) {
          // We only want direct children, not all descendants
          if (!e.getPath().substring(path.length()+1).contains("."))
            res.add(e);
        } else
          break;
      }
      return res;
    }
  }


  public static List<ElementDefinition> getSliceList(StructureDefinition profile, ElementDefinition element) throws DefinitionException {
    if (!element.hasSlicing())
      throw new Error("getSliceList should only be called when the element has slicing");

    List<ElementDefinition> res = new ArrayList<ElementDefinition>();
    List<ElementDefinition> elements = profile.getSnapshot().getElement();
    String path = element.getPath();
    for (int index = elements.indexOf(element) + 1; index < elements.size(); index++) {
      ElementDefinition e = elements.get(index);
      if (e.getPath().startsWith(path + ".") || e.getPath().equals(path)) {
        // We want elements with the same path (until we hit an element that doesn't start with the same path)
        if (e.getPath().equals(element.getPath()))
          res.add(e);
      } else
        break;
    }
    return res;
  }


  /**
   * Given a Structure, navigate to the element given by the path and return the direct children of that element
   *
   * @param structure The structure to navigate into
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


  public static List<ElementDefinition> getChildList(StructureDefinition structure, ElementDefinition element) {
    return getChildList(structure, element.getPath());
	}

  public void updateMaps(StructureDefinition base, StructureDefinition derived) throws DefinitionException {
    if (base == null)
        throw new DefinitionException("no base profile provided");
    if (derived == null)
      throw new DefinitionException("no derived structure provided");
    
    for (StructureDefinitionMappingComponent baseMap : base.getMapping()) {
      boolean found = false;
      for (StructureDefinitionMappingComponent derivedMap : derived.getMapping()) {
        if (derivedMap.getUri().equals(baseMap.getUri())) {
          found = true;
          break;
        }
      }
      if (!found)
        derived.getMapping().add(baseMap);
    }
  }
  
  /**
   * Given a base (snapshot) profile structure, and a differential profile, generate a new snapshot profile
   *
   * @param base - the base structure on which the differential will be applied
   * @param differential - the differential to apply to the base
   * @param url - where the base has relative urls for profile references, these need to be converted to absolutes by prepending this URL
   * @param trimDifferential - if this is true, then the snap short generator will remove any material in the element definitions that is not different to the base
   * @return
   * @throws FHIRException 
   * @throws DefinitionException 
   * @throws Exception
   */
  public void generateSnapshot(StructureDefinition base, StructureDefinition derived, String url, String profileName) throws DefinitionException, FHIRException {
    if (base == null)
      throw new DefinitionException("no base profile provided");
    if (derived == null)
      throw new DefinitionException("no derived structure provided");

    if (snapshotStack.contains(derived.getUrl()))
      throw new DefinitionException("Circular snapshot references detected; cannot generate snapshot (stack = "+snapshotStack.toString()+")");
    snapshotStack.add(derived.getUrl());
    

    derived.setSnapshot(new StructureDefinitionSnapshotComponent());

    // so we have two lists - the base list, and the differential list
    // the differential list is only allowed to include things that are in the base list, but
    // is allowed to include them multiple times - thereby slicing them

    // our approach is to walk through the base list, and see whether the differential
    // says anything about them.
    int baseCursor = 0;
    int diffCursor = 0; // we need a diff cursor because we can only look ahead, in the bound scoped by longer paths

    if (derived.hasDifferential() && !derived.getDifferential().getElementFirstRep().getPath().contains(".") && !derived.getDifferential().getElementFirstRep().getType().isEmpty())
      throw new Error("type on first differential element!");

    for (ElementDefinition e : derived.getDifferential().getElement()) 
      e.clearUserData(GENERATED_IN_SNAPSHOT);
    
    // we actually delegate the work to a subroutine so we can re-enter it with a different cursors
    processPaths("", derived.getSnapshot(), base.getSnapshot(), derived.getDifferential(), baseCursor, diffCursor, base.getSnapshot().getElement().size()-1, 
        derived.getDifferential().hasElement() ? derived.getDifferential().getElement().size()-1 : -1, url, derived.getId(), null, null, false, base.getUrl(), null, false);
    if (!derived.getSnapshot().getElementFirstRep().getType().isEmpty())
      throw new Error("type on first snapshot element for "+derived.getSnapshot().getElementFirstRep().getPath()+" in "+derived.getUrl()+" from "+base.getUrl());
    updateMaps(base, derived);
    setIds(derived, false);
    
    //Check that all differential elements have a corresponding snapshot element
    for (ElementDefinition e : derived.getDifferential().getElement()) {
      if (!e.hasUserData(GENERATED_IN_SNAPSHOT)) {
        System.out.println("Error in snapshot generation: Snapshot for "+derived.getUrl()+" does not contain differential element with id: " + e.getId());
        System.out.println("Differential: ");
        for (ElementDefinition ed : derived.getDifferential().getElement())
          System.out.println("  "+ed.getPath()+" : "+typeSummary(ed)+"["+ed.getMin()+".."+ed.getMax()+"]"+sliceSummary(ed)+"  id = "+ed.getId());
        System.out.println("Snapshot: ");
        for (ElementDefinition ed : derived.getSnapshot().getElement())
          System.out.println("  "+ed.getPath()+" : "+typeSummary(ed)+"["+ed.getMin()+".."+ed.getMax()+"]"+sliceSummary(ed)+"  id = "+ed.getId());
        throw new DefinitionException("Snapshot for "+derived.getUrl()+" does not contain differential element with id: " + e.getId());
//        System.out.println("**BAD Differential element: " + profileName + ":" + e.getId());
    }
  }
  }

  private String sliceSummary(ElementDefinition ed) {
    if (!ed.hasSlicing() && !ed.hasSliceName())
      return "";
    if (ed.hasSliceName())
      return " (slicename = "+ed.getSliceName()+")";
    
    StringBuilder b = new StringBuilder();
    boolean first = true;
    for (ElementDefinitionSlicingDiscriminatorComponent d : ed.getSlicing().getDiscriminator()) {
      if (first) 
        first = false;
      else
        b.append("|");
      b.append(d.getPath());
    }
    return " (slicing by "+b.toString()+")";
  }


  private String typeSummary(ElementDefinition ed) {
    StringBuilder b = new StringBuilder();
    boolean first = true;
    for (TypeRefComponent tr : ed.getType()) {
      if (first) 
        first = false;
      else
        b.append("|");
      b.append(tr.getCode());
    }
    return b.toString();
  }


  private boolean findMatchingElement(String id, List<ElementDefinition> list) {
    for (ElementDefinition ed : list) {
      if (ed.getId().equals(id))
        return true;
      if (id.endsWith("[x]")) {
        if (ed.getId().startsWith(id.substring(0, id.length()-3)) && !ed.getId().substring(id.length()-3).contains("."))
          return true;
      }
    }
    return false;
  }


  /**
   * @param trimDifferential
   * @throws DefinitionException, FHIRException 
   * @throws Exception
   */
  private ElementDefinition processPaths(String indent, StructureDefinitionSnapshotComponent result, StructureDefinitionSnapshotComponent base, StructureDefinitionDifferentialComponent differential, int baseCursor, int diffCursor, int baseLimit,
      int diffLimit, String url, String profileName, String contextPathSrc, String contextPathDst, boolean trimDifferential, String contextName, String resultPathBase, boolean slicingDone) throws DefinitionException, FHIRException {

//    System.out.println(indent+"PP @ "+resultPathBase+": base = "+baseCursor+" to "+baseLimit+", diff = "+diffCursor+" to "+diffLimit+" (slicing = "+slicingDone+")");
    ElementDefinition res = null; 
    // just repeat processing entries until we run out of our allowed scope (1st entry, the allowed scope is all the entries)
    while (baseCursor <= baseLimit) {
      // get the current focus of the base, and decide what to do
      ElementDefinition currentBase = base.getElement().get(baseCursor);
      String cpath = fixedPath(contextPathSrc, currentBase.getPath());
//      System.out.println(indent+" - "+cpath+": base = "+baseCursor+" to "+baseLimit+", diff = "+diffCursor+" to "+diffLimit+" (slicingDone = "+slicingDone+")");
      List<ElementDefinition> diffMatches = getDiffMatches(differential, cpath, diffCursor, diffLimit, profileName, url); // get a list of matching elements in scope

      // in the simple case, source is not sliced.
      if (!currentBase.hasSlicing()) {
        if (diffMatches.isEmpty()) { // the differential doesn't say anything about this item
          // so we just copy it in
          ElementDefinition outcome = updateURLs(url, currentBase.copy());
          outcome.setPath(fixedPath(contextPathDst, outcome.getPath()));
          updateFromBase(outcome, currentBase);
          markDerived(outcome);
          if (resultPathBase == null)
            resultPathBase = outcome.getPath();
          else if (!outcome.getPath().startsWith(resultPathBase))
            throw new DefinitionException("Adding wrong path");
          result.getElement().add(outcome);
          if (hasInnerDiffMatches(differential, cpath, diffCursor, diffLimit, base.getElement())) {
            // well, the profile walks into this, so we need to as well
            if (outcome.getType().size() > 1) {
              for (TypeRefComponent t : outcome.getType()) {
                if (!t.getCode().equals("Reference"))
                  throw new DefinitionException(diffMatches.get(0).getPath()+" has children ("+differential.getElement().get(diffCursor).getPath()+") and multiple types ("+typeCode(outcome.getType())+") in profile "+profileName);
              }
            }
            StructureDefinition dt = getProfileForDataType(outcome.getType().get(0));
            if (dt == null)
              throw new DefinitionException(cpath+" has children for type "+typeCode(outcome.getType())+" in profile "+profileName+", but can't find type");
            contextName = dt.getUrl();
            int start = diffCursor;
            while (differential.getElement().size() > diffCursor && pathStartsWith(differential.getElement().get(diffCursor).getPath(), cpath+"."))
              diffCursor++;
            processPaths(indent+"  ", result, dt.getSnapshot(), differential, 1 /* starting again on the data type, but skip the root */, start, dt.getSnapshot().getElement().size()-1,
                diffCursor-1, url, profileName, cpath, outcome.getPath(), trimDifferential, contextName, resultPathBase, false);
          }
          baseCursor++;
        } else if (diffMatches.size() == 1 && (slicingDone || !(diffMatches.get(0).hasSlicing() || (isExtension(diffMatches.get(0)) && diffMatches.get(0).hasSliceName())))) {// one matching element in the differential
          ElementDefinition template = null;
          if (diffMatches.get(0).hasType() && diffMatches.get(0).getType().size() == 1 && diffMatches.get(0).getType().get(0).hasProfile() && !diffMatches.get(0).getType().get(0).getCode().equals("Reference")) {
            String p = diffMatches.get(0).getType().get(0).getProfile();
            StructureDefinition sd = context.fetchResource(StructureDefinition.class, p);
            if (sd != null) {
              if (!sd.hasSnapshot()) {
                StructureDefinition sdb = context.fetchResource(StructureDefinition.class, sd.getBaseDefinition());
                if (sdb == null)
                  throw new DefinitionException("no base for "+sd.getBaseDefinition());
                generateSnapshot(sdb, sd, sd.getUrl(), sd.getName());
              }
              template = sd.getSnapshot().getElement().get(0).copy().setPath(currentBase.getPath());
              template.setSliceName(null);
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
          outcome.setPath(fixedPath(contextPathDst, outcome.getPath()));
          res = outcome;
          updateFromBase(outcome, currentBase);
          if (diffMatches.get(0).hasSliceName())
          outcome.setSliceName(diffMatches.get(0).getSliceName());
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
          if (differential.getElement().size() > diffCursor && outcome.getPath().contains(".") && (isDataType(outcome.getType()) || outcome.hasContentReference())) {  // don't want to do this for the root, since that's base, and we're already processing it
            if (pathStartsWith(differential.getElement().get(diffCursor).getPath(), diffMatches.get(0).getPath()+".") && !baseWalksInto(base.getElement(), baseCursor)) {
              if (outcome.getType().size() > 1) {
                for (TypeRefComponent t : outcome.getType()) {
                  if (!t.getCode().equals("Reference"))
                    throw new DefinitionException(diffMatches.get(0).getPath()+" has children ("+differential.getElement().get(diffCursor).getPath()+") and multiple types ("+typeCode(outcome.getType())+") in profile "+profileName);
                }
              }
              int start = diffCursor;
              while (differential.getElement().size() > diffCursor && pathStartsWith(differential.getElement().get(diffCursor).getPath(), diffMatches.get(0).getPath()+"."))
                diffCursor++;
              if (outcome.hasContentReference()) {
                ElementDefinition tgt = getElementById(base.getElement(), outcome.getContentReference());
                if (tgt == null)
                  throw new DefinitionException("Unable to resolve reference to "+outcome.getContentReference());
                replaceFromContentReference(outcome, tgt);
                int nbc = base.getElement().indexOf(tgt)+1;
                int nbl = nbc;
                while (nbl < base.getElement().size() && base.getElement().get(nbl).getPath().startsWith(tgt.getPath()+"."))
                  nbl++;
                processPaths(indent+"  ", result, base, differential, nbc, start - 1, nbl-1, diffCursor - 1, url, profileName, tgt.getPath(), diffMatches.get(0).getPath(), trimDifferential, contextName, resultPathBase, false);
              } else {
              StructureDefinition dt = getProfileForDataType(outcome.getType().get(0));
              if (dt == null)
                throw new DefinitionException(diffMatches.get(0).getPath()+" has children ("+differential.getElement().get(diffCursor).getPath()+") for type "+typeCode(outcome.getType())+" in profile "+profileName+", but can't find type");
              contextName = dt.getUrl();
              processPaths(indent+"  ", result, dt.getSnapshot(), differential, 1 /* starting again on the data type, but skip the root */, start-1, dt.getSnapshot().getElement().size()-1,
                  diffCursor - 1, url, profileName+pathTail(diffMatches, 0), diffMatches.get(0).getPath(), outcome.getPath(), trimDifferential, contextName, resultPathBase, false);
            }
          }
          }
        } else {
          // ok, the differential slices the item. Let's check our pre-conditions to ensure that this is correct
          if (!unbounded(currentBase) && !isSlicedToOneOnly(diffMatches.get(0)))
            // you can only slice an element that doesn't repeat if the sum total of your slices is limited to 1
            // (but you might do that in order to split up constraints by type)
            throw new DefinitionException("Attempt to a slice an element that does not repeat: "+currentBase.getPath()+"/"+currentBase.getSliceName()+" from "+contextName+" in "+url);
          if (!diffMatches.get(0).hasSlicing() && !isExtension(currentBase)) // well, the diff has set up a slice, but hasn't defined it. this is an error
            throw new DefinitionException("differential does not have a slice: "+currentBase.getPath()+" in profile "+url);

          // well, if it passed those preconditions then we slice the dest.
          int start = 0;
          int nbl = findEndOfElement(base, baseCursor);
          if (diffMatches.size() > 1 && diffMatches.get(0).hasSlicing() && differential.getElement().indexOf(diffMatches.get(1)) > differential.getElement().indexOf(diffMatches.get(0))+1) {
            int ndc = differential.getElement().indexOf(diffMatches.get(0));
            int ndl = findEndOfElement(differential, ndc);
            processPaths(indent+"  ", result, base, differential, baseCursor, ndc, nbl, ndl, url, profileName+pathTail(diffMatches, 0), contextPathSrc, contextPathDst, trimDifferential, contextName, resultPathBase, true).setSlicing(diffMatches.get(0).getSlicing());
            start++;
          } else {
          // we're just going to accept the differential slicing at face value
          ElementDefinition outcome = updateURLs(url, currentBase.copy());
          outcome.setPath(fixedPath(contextPathDst, outcome.getPath()));
          updateFromBase(outcome, currentBase);

          if (!diffMatches.get(0).hasSlicing())
            outcome.setSlicing(makeExtensionSlicing());
          else
            outcome.setSlicing(diffMatches.get(0).getSlicing().copy());
          if (!outcome.getPath().startsWith(resultPathBase))
            throw new DefinitionException("Adding wrong path");
          result.getElement().add(outcome);

          // differential - if the first one in the list has a name, we'll process it. Else we'll treat it as the base definition of the slice.
          if (!diffMatches.get(0).hasSliceName()) {
            updateFromDefinition(outcome, diffMatches.get(0), profileName, trimDifferential, url);
              if (!outcome.hasContentReference() && !outcome.hasType()) {
              throw new DefinitionException("not done yet");
            }
              start++;
              // result.getElement().remove(result.getElement().size()-1);
          } else 
            checkExtensionDoco(outcome);
          }
          // now, for each entry in the diff matches, we're going to process the base item
          // our processing scope for base is all the children of the current path
          int ndc = diffCursor;
          int ndl = diffCursor;
          for (int i = start; i < diffMatches.size(); i++) {
            // our processing scope for the differential is the item in the list, and all the items before the next one in the list
            ndc = differential.getElement().indexOf(diffMatches.get(i));
            ndl = findEndOfElement(differential, ndc);
/*            if (skipSlicingElement && i == 0) {
              ndc = ndc + 1;
              if (ndc > ndl)
                continue;
            }*/
            // now we process the base scope repeatedly for each instance of the item in the differential list
            processPaths(indent+"  ", result, base, differential, baseCursor, ndc, nbl, ndl, url, profileName+pathTail(diffMatches, i), contextPathSrc, contextPathDst, trimDifferential, contextName, resultPathBase, true);
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
          // copy across the currentbase, and all of its children and siblings
          while (baseCursor < base.getElement().size() && base.getElement().get(baseCursor).getPath().startsWith(path)) {
            ElementDefinition outcome = updateURLs(url, base.getElement().get(baseCursor).copy());
            outcome.setPath(fixedPath(contextPathDst, outcome.getPath()));
            if (!outcome.getPath().startsWith(resultPathBase))
              throw new DefinitionException("Adding wrong path in profile " + profileName + ": "+outcome.getPath()+" vs " + resultPathBase);
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
            if (dSlice.hasOrderedElement() && bSlice.hasOrderedElement() && !orderMatches(dSlice.getOrderedElement(), bSlice.getOrderedElement()))
              throw new DefinitionException("Slicing rules on differential ("+summariseSlicing(dSlice)+") do not match those on base ("+summariseSlicing(bSlice)+") - order @ "+path+" ("+contextName+")");
            if (!discriminatorMatches(dSlice.getDiscriminator(), bSlice.getDiscriminator()))
             throw new DefinitionException("Slicing rules on differential ("+summariseSlicing(dSlice)+") do not match those on base ("+summariseSlicing(bSlice)+") - disciminator @ "+path+" ("+contextName+")");
            if (!ruleMatches(dSlice.getRules(), bSlice.getRules()))
             throw new DefinitionException("Slicing rules on differential ("+summariseSlicing(dSlice)+") do not match those on base ("+summariseSlicing(bSlice)+") - rule @ "+path+" ("+contextName+")");
          }
          if (diffMatches.size() > 1 && diffMatches.get(0).hasSlicing() && differential.getElement().indexOf(diffMatches.get(1)) > differential.getElement().indexOf(diffMatches.get(0))+1) {
            throw new Error("Not done yet");
          }
          ElementDefinition outcome = updateURLs(url, currentBase.copy());
          outcome.setPath(fixedPath(contextPathDst, outcome.getPath()));
          updateFromBase(outcome, currentBase);
          if (diffMatches.get(0).hasSlicing() /*&& !isExtension*/) {
            updateFromSlicing(outcome.getSlicing(), diffMatches.get(0).getSlicing());
            updateFromDefinition(outcome, diffMatches.get(0), profileName, closed, url); // if there's no slice, we don't want to update the unsliced description
          } else if (!diffMatches.get(0).hasSliceName())
            diffMatches.get(0).setUserData(GENERATED_IN_SNAPSHOT, true); // because of updateFromDefinition isn't called 
          
          result.getElement().add(outcome);

          if (!diffMatches.get(0).hasSliceName()) { // it's not real content, just the slice
              diffpos++;
          }

          // now, we have two lists, base and diff. we're going to work through base, looking for matches in diff.
          List<ElementDefinition> baseMatches = getSiblings(base.getElement(), currentBase);
          for (ElementDefinition baseItem : baseMatches) {
            baseCursor = base.getElement().indexOf(baseItem);
            outcome = updateURLs(url, baseItem.copy());
            updateFromBase(outcome, currentBase);
            outcome.setPath(fixedPath(contextPathDst, outcome.getPath()));
            outcome.setSlicing(null);
            if (!outcome.getPath().startsWith(resultPathBase))
              throw new DefinitionException("Adding wrong path");
            if (diffpos < diffMatches.size() && diffMatches.get(diffpos).getSliceName().equals(outcome.getSliceName())) {
              // if there's a diff, we update the outcome with diff
              // no? updateFromDefinition(outcome, diffMatches.get(diffpos), profileName, closed, url);
              //then process any children
              int nbl = findEndOfElement(base, baseCursor);
              int ndc = differential.getElement().indexOf(diffMatches.get(diffpos));
              int ndl = findEndOfElement(differential, ndc);
              // now we process the base scope repeatedly for each instance of the item in the differential list
              processPaths(indent+"  ", result, base, differential, baseCursor, ndc, nbl, ndl, url, profileName+pathTail(diffMatches, diffpos), contextPathSrc, contextPathDst, closed, contextName, resultPathBase, true);
              // ok, done with that - now set the cursors for if this is the end
              baseCursor = nbl;
              diffCursor = ndl+1;
              diffpos++;
            } else {
              result.getElement().add(outcome);
              baseCursor++;
              // just copy any children on the base
              while (baseCursor < base.getElement().size() && base.getElement().get(baseCursor).getPath().startsWith(path) && !base.getElement().get(baseCursor).getPath().equals(path)) {
                outcome = updateURLs(url, base.getElement().get(baseCursor).copy());
                outcome.setPath(fixedPath(contextPathDst, outcome.getPath()));
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
          if (diffpos == diffMatches.size()) {
            diffCursor++;
          } else {
            while (diffpos < diffMatches.size()) {
              ElementDefinition diffItem = diffMatches.get(diffpos);
              for (ElementDefinition baseItem : baseMatches)
                if (baseItem.getSliceName().equals(diffItem.getSliceName()))
                  throw new DefinitionException("Named items are out of order in the slice");
              outcome = updateURLs(url, currentBase.copy());
              //            outcome = updateURLs(url, diffItem.copy());
              outcome.setPath(fixedPath(contextPathDst, outcome.getPath()));
              updateFromBase(outcome, currentBase);
              outcome.setSlicing(null);
              if (!outcome.getPath().startsWith(resultPathBase))
                throw new DefinitionException("Adding wrong path");
              result.getElement().add(outcome);
              updateFromDefinition(outcome, diffItem, profileName, trimDifferential, url);
              // --- LM Added this
              diffCursor = differential.getElement().indexOf(diffItem)+1;
              if (!outcome.getType().isEmpty() && (/*outcome.getType().get(0).getCode().equals("Extension") || */differential.getElement().size() > diffCursor) && outcome.getPath().contains(".") && isDataType(outcome.getType())) {  // don't want to do this for the root, since that's base, and we're already processing it
                if (!baseWalksInto(base.getElement(), baseCursor)) {
                  if (differential.getElement().size() > diffCursor && pathStartsWith(differential.getElement().get(diffCursor).getPath(), diffMatches.get(0).getPath()+".")) {
                    if (outcome.getType().size() > 1)
                      for (TypeRefComponent t : outcome.getType()) {
                        if (!t.getCode().equals("Reference"))
                          throw new DefinitionException(diffMatches.get(0).getPath()+" has children ("+differential.getElement().get(diffCursor).getPath()+") and multiple types ("+typeCode(outcome.getType())+") in profile "+profileName);
                      }
                    TypeRefComponent t = outcome.getType().get(0);
                    StructureDefinition dt = getProfileForDataType(outcome.getType().get(0));
                    //                if (t.getCode().equals("Extension") && t.hasProfile() && !t.getProfile().contains(":")) {
                    // lloydfix                  dt = 
                    //                }
                    if (dt == null)
                      throw new DefinitionException(diffMatches.get(0).getPath()+" has children ("+differential.getElement().get(diffCursor).getPath()+") for type "+typeCode(outcome.getType())+" in profile "+profileName+", but can't find type");
                    contextName = dt.getUrl();
                    int start = diffCursor;
                    while (differential.getElement().size() > diffCursor && pathStartsWith(differential.getElement().get(diffCursor).getPath(), diffMatches.get(0).getPath()+"."))
                      diffCursor++;
                    processPaths(indent+"  ", result, dt.getSnapshot(), differential, 1 /* starting again on the data type, but skip the root */, start-1, dt.getSnapshot().getElement().size()-1,
                        diffCursor - 1, url, profileName+pathTail(diffMatches, 0), diffMatches.get(0).getPath(), outcome.getPath(), trimDifferential, contextName, resultPathBase, false);
                  } else if (outcome.getType().get(0).getCode().equals("Extension")) {
                    // Force URL to appear if we're dealing with an extension.  (This is a kludge - may need to drill down in other cases where we're slicing and the type has a profile declaration that could be setting the fixed value)
                    StructureDefinition dt = getProfileForDataType(outcome.getType().get(0));
                    for (ElementDefinition extEd : dt.getSnapshot().getElement()) {
                      // We only want the children that aren't the root
                      if (extEd.getPath().contains(".")) {
                        ElementDefinition extUrlEd = updateURLs(url, extEd.copy());
                        extUrlEd.setPath(fixedPath(outcome.getPath(), extUrlEd.getPath()));
                        //                      updateFromBase(extUrlEd, currentBase);
                        markDerived(extUrlEd);
                        result.getElement().add(extUrlEd);
                      }
                    }                  
                  }
                }
              }
              // ---
              diffpos++;
            }
          }
          baseCursor++;
        }
      }
    }

    int i = 0;
    for (ElementDefinition e : result.getElement()) {
      i++;
      if (e.hasMinElement() && e.getMinElement().getValue()==null)
        throw new Error("null min");
    }
    return res;
  }


  private void replaceFromContentReference(ElementDefinition outcome, ElementDefinition tgt) {
    outcome.setContentReference(null);
    outcome.getType().clear(); // though it should be clear anyway
    outcome.getType().addAll(tgt.getType());    
  }


  private boolean baseWalksInto(List<ElementDefinition> elements, int cursor) {
    if (cursor >= elements.size())
      return false;
    String path = elements.get(cursor).getPath();
    String prevPath = elements.get(cursor - 1).getPath();
    return path.startsWith(prevPath + ".");
  }


  private ElementDefinition overWriteWithCurrent(ElementDefinition profile, ElementDefinition usage) {
    ElementDefinition res = profile.copy();
    if (usage.hasSliceName())
      res.setSliceName(usage.getSliceName());
    if (usage.hasLabel())
      res.setLabel(usage.getLabel());
    for (Coding c : usage.getCode())
      res.addCode(c);
    
    if (usage.hasDefinition())
      res.setDefinition(usage.getDefinition());
    if (usage.hasShort())
      res.setShort(usage.getShort());
    if (usage.hasComment())
      res.setComment(usage.getComment());
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


  private boolean checkExtensionDoco(ElementDefinition base) {
    // see task 3970. For an extension, there's no point copying across all the underlying definitional stuff
    boolean isExtension = base.getPath().equals("Extension") || base.getPath().endsWith(".extension") || base.getPath().endsWith(".modifierExtension");
    if (isExtension) {
      base.setDefinition("An Extension");
      base.setShort("Extension");
      base.setCommentElement(null);
      base.setRequirementsElement(null);
      base.getAlias().clear();
      base.getMapping().clear();
    }
    return isExtension;
  }


  private String pathTail(List<ElementDefinition> diffMatches, int i) {
    
    ElementDefinition d = diffMatches.get(i);
    String s = d.getPath().contains(".") ? d.getPath().substring(d.getPath().lastIndexOf(".")+1) : d.getPath();
    return "."+s + (d.hasType() && d.getType().get(0).hasProfile() ? "["+d.getType().get(0).getProfile()+"]" : "");
  }


  private void markDerived(ElementDefinition outcome) {
    for (ElementDefinitionConstraintComponent inv : outcome.getConstraint())
      inv.setUserData(IS_DERIVED, true);
  }


  private String summariseSlicing(ElementDefinitionSlicingComponent slice) {
    StringBuilder b = new StringBuilder();
    boolean first = true;
    for (ElementDefinitionSlicingDiscriminatorComponent d : slice.getDiscriminator()) {
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


  private void updateFromBase(ElementDefinition derived, ElementDefinition base) {
    if (base.hasBase()) {
      if (!derived.hasBase())
        derived.setBase(new ElementDefinitionBaseComponent());
      derived.getBase().setPath(base.getBase().getPath());
      derived.getBase().setMin(base.getBase().getMin());
      derived.getBase().setMax(base.getBase().getMax());
    } else {
      if (!derived.hasBase())
        derived.setBase(new ElementDefinitionBaseComponent());
      derived.getBase().setPath(base.getPath());
      derived.getBase().setMin(base.getMin());
      derived.getBase().setMax(base.getMax());
    }
  }


  private boolean pathStartsWith(String p1, String p2) {
    return p1.startsWith(p2);
  }

  private boolean pathMatches(String p1, String p2) {
    return p1.equals(p2) || (p2.endsWith("[x]") && p1.startsWith(p2.substring(0, p2.length()-3)) && !p1.substring(p2.length()-3).contains("."));
  }


  private String fixedPath(String contextPath, String pathSimple) {
    if (contextPath == null)
      return pathSimple;
    return contextPath+"."+pathSimple.substring(pathSimple.indexOf(".")+1);
  }


  private StructureDefinition getProfileForDataType(TypeRefComponent type)  {
    StructureDefinition sd = null;
    if (type.hasProfile() && !type.getCode().equals("Reference"))  
      sd = context.fetchResource(StructureDefinition.class, type.getProfile()); 
    if (sd == null)
      sd = context.fetchResource(StructureDefinition.class, "http://hl7.org/fhir/StructureDefinition/"+type.getCode());
    if (sd == null)
      System.out.println("XX: failed to find profle for type: " + type.getCode()); // debug GJM
    return sd;
  }


  public static String typeCode(List<TypeRefComponent> types) {
    StringBuilder b = new StringBuilder();
    boolean first = true;
    for (TypeRefComponent type : types) {
      if (first) first = false; else b.append(", ");
      b.append(type.getCode());
      if (type.hasTargetProfile())
        b.append("{"+type.getTargetProfile()+"}");
      else if (type.hasProfile())
        b.append("{"+type.getProfile()+"}");
    }
    return b.toString();
  }


  private boolean isDataType(List<TypeRefComponent> types) {
    if (types.isEmpty())
      return false;
    for (TypeRefComponent type : types) {
      String t = type.getCode();
      if (!isDataType(t) && !isPrimitive(t))
        return false;
    }
    return true;
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
        if (t.hasProfile()) {
          if (t.getProfile().startsWith("#"))
            t.setProfile(url+t.getProfile());
        }
        if (t.hasTargetProfile()) {
          if (t.getTargetProfile().startsWith("#"))
            t.setTargetProfile(url+t.getTargetProfile());
        }
      }
    }
    return element;
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

  private void updateFromSlicing(ElementDefinitionSlicingComponent dst, ElementDefinitionSlicingComponent src) {
    if (src.hasOrderedElement())
      dst.setOrderedElement(src.getOrderedElement().copy());
    if (src.hasDiscriminator()) {
      //    dst.getDiscriminator().addAll(src.getDiscriminator());  Can't use addAll because it uses object equality, not string equality
      for (ElementDefinitionSlicingDiscriminatorComponent s : src.getDiscriminator()) {
        boolean found = false;
        for (ElementDefinitionSlicingDiscriminatorComponent d : dst.getDiscriminator()) {
          if (matches(d, s)) {
            found = true;
            break;
          }
        }
        if (!found)
          dst.getDiscriminator().add(s);
      }
    }
    if (src.hasRulesElement())
      dst.setRulesElement(src.getRulesElement().copy());
  }

  private boolean orderMatches(BooleanType diff, BooleanType base) {
    return (diff == null) || (base == null) || (diff.getValue() == base.getValue());
  }

  private boolean discriminatorMatches(List<ElementDefinitionSlicingDiscriminatorComponent> diff, List<ElementDefinitionSlicingDiscriminatorComponent> base) {
    if (diff.isEmpty() || base.isEmpty())
    	return true;
    if (diff.size() != base.size())
    	return false;
    for (int i = 0; i < diff.size(); i++)
    	if (!matches(diff.get(i), base.get(i)))
    		return false;
    return true;
  }

  private boolean matches(ElementDefinitionSlicingDiscriminatorComponent c1, ElementDefinitionSlicingDiscriminatorComponent c2) {
    return c1.getType().equals(c2.getType()) && c1.getPath().equals(c2.getPath());
  }


  private boolean ruleMatches(SlicingRules diff, SlicingRules base) {
    return (diff == null) || (base == null) || (diff == base) || (diff == SlicingRules.OPEN) ||
        ((diff == SlicingRules.OPENATEND && base == SlicingRules.CLOSED));
  }

  private boolean isSlicedToOneOnly(ElementDefinition e) {
    return (e.hasSlicing() && e.hasMaxElement() && e.getMax().equals("1"));
  }

  private ElementDefinitionSlicingComponent makeExtensionSlicing() {
  	ElementDefinitionSlicingComponent slice = new ElementDefinitionSlicingComponent();
  	nextSliceId++;
  	slice.setId(Integer.toString(nextSliceId));
    slice.addDiscriminator().setPath("url").setType(DiscriminatorType.VALUE);
    slice.setOrdered(false);
    slice.setRules(SlicingRules.OPEN);
    return slice;
  }

  private boolean isExtension(ElementDefinition currentBase) {
    return currentBase.getPath().endsWith(".extension") || currentBase.getPath().endsWith(".modifierExtension");
  }

  private boolean hasInnerDiffMatches(StructureDefinitionDifferentialComponent context, String path, int start, int end, List<ElementDefinition> base) throws DefinitionException {
    for (int i = start; i <= end; i++) {
      String statedPath = context.getElement().get(i).getPath();
      if (statedPath.startsWith(path+".") && !statedPath.substring(path.length()+1).contains(".")) {
        boolean found = false;
        for (ElementDefinition ed : base) {
          String ep = ed.getPath();
          if (ep.equals(statedPath) || (ep.endsWith("[x]") && statedPath.length() > ep.length() - 2 && statedPath.substring(0, ep.length()-3).equals(ep.substring(0, ep.length()-3)) && !statedPath.substring(ep.length()).contains(".")))
            found = true;
        }
        if (!found)
          return true;
      }
    }
    return false;
  }

  private List<ElementDefinition> getDiffMatches(StructureDefinitionDifferentialComponent context, String path, int start, int end, String profileName, String url) throws DefinitionException {
    List<ElementDefinition> result = new ArrayList<ElementDefinition>();
    for (int i = start; i <= end; i++) {
      String statedPath = context.getElement().get(i).getPath();
      if (statedPath.equals(path) || (path.endsWith("[x]") && statedPath.length() > path.length() - 2 && statedPath.substring(0, path.length()-3).equals(path.substring(0, path.length()-3)) && (statedPath.length() < path.length() || !statedPath.substring(path.length()).contains(".")))) {
        /* 
         * Commenting this out because it raises warnings when profiling inherited elements.  For example,
         * Error: unknown element 'Bundle.meta.profile' (or it is out of order) in profile ... (looking for 'Bundle.entry')
         * Not sure we have enough information here to do the check properly.  Might be better done when we're sorting the profile?

        if (i != start && result.isEmpty() && !path.startsWith(context.getElement().get(start).getPath()))
          messages.add(new ValidationMessage(Source.ProfileValidator, IssueType.VALUE, "StructureDefinition.differential.element["+Integer.toString(start)+"]", "Error: unknown element '"+context.getElement().get(start).getPath()+"' (or it is out of order) in profile '"+url+"' (looking for '"+path+"')", IssueSeverity.WARNING));

         */
        result.add(context.getElement().get(i));
      }
    }
    return result;
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

  private void updateFromDefinition(ElementDefinition dest, ElementDefinition source, String pn, boolean trimDifferential, String purl) throws DefinitionException, FHIRException {
    source.setUserData(GENERATED_IN_SNAPSHOT, true);
    // we start with a clone of the base profile ('dest') and we copy from the profile ('source')
    // over the top for anything the source has
    ElementDefinition base = dest;
    ElementDefinition derived = source;
    derived.setUserData(DERIVATION_POINTER, base);

    // Before applying changes, apply them to what's in the profile
    // TODO: follow Chris's rules
    StructureDefinition profile = source.getType().size() == 1 && source.getTypeFirstRep().hasProfile() ? context.fetchResource(StructureDefinition.class, source.getTypeFirstRep().getProfile()) : null;
    if (profile != null) {
      ElementDefinition e = profile.getSnapshot().getElement().get(0);
      base.setDefinition(e.getDefinition());
      base.setShort(e.getShort());
      if (e.hasCommentElement())
        base.setCommentElement(e.getCommentElement());
      if (e.hasRequirementsElement())
        base.setRequirementsElement(e.getRequirementsElement());
      base.getAlias().clear();
      base.getAlias().addAll(e.getAlias());
      base.getMapping().clear();
      base.getMapping().addAll(e.getMapping());
    }
    
    if (derived != null) {
      boolean isExtension = checkExtensionDoco(base);

      if (derived.hasSliceName()) {
        base.setSliceName(derived.getSliceName());
      }
      
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

      if (derived.hasCommentElement()) {
        if (derived.getComment().startsWith("..."))
          base.setComment(base.getComment()+"\r\n"+derived.getComment().substring(3));
        else if (derived.hasCommentElement()!= base.hasCommentElement() || !Base.compareDeep(derived.getCommentElement(), base.getCommentElement(), false))
          base.setCommentElement(derived.getCommentElement().copy());
        else if (trimDifferential)
          base.setCommentElement(derived.getCommentElement().copy());
        else if (derived.hasCommentElement())
          derived.getCommentElement().setUserData(DERIVATION_EQUALS, true);
      }

      if (derived.hasLabelElement()) {
        if (derived.getLabel().startsWith("..."))
          base.setLabel(base.getLabel()+"\r\n"+derived.getLabel().substring(3));
        else if (!base.hasLabelElement() || !Base.compareDeep(derived.getLabelElement(), base.getLabelElement(), false))
          base.setLabelElement(derived.getLabelElement().copy());
        else if (trimDifferential)
          base.setLabelElement(derived.getLabelElement().copy());
        else if (derived.hasLabelElement())
          derived.getLabelElement().setUserData(DERIVATION_EQUALS, true);
      }

      if (derived.hasRequirementsElement()) {
        if (derived.getRequirements().startsWith("..."))
          base.setRequirements(base.getRequirements()+"\r\n"+derived.getRequirements().substring(3));
        else if (!base.hasRequirementsElement() || !Base.compareDeep(derived.getRequirementsElement(), base.getRequirementsElement(), false))
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
            messages.add(new ValidationMessage(Source.ProfileValidator, ValidationMessage.IssueType.BUSINESSRULE, pn+"."+source.getPath(), "Derived min  ("+Integer.toString(derived.getMin())+") cannot be less than base min ("+Integer.toString(base.getMin())+")", ValidationMessage.IssueSeverity.ERROR));
          base.setMinElement(derived.getMinElement().copy());
        } else if (trimDifferential)
          derived.setMinElement(null);
        else
          derived.getMinElement().setUserData(DERIVATION_EQUALS, true);
      }

      if (derived.hasMaxElement()) {
        if (!Base.compareDeep(derived.getMaxElement(), base.getMaxElement(), false)) {
          if (isLargerMax(derived.getMax(), base.getMax()))
            messages.add(new ValidationMessage(Source.ProfileValidator, ValidationMessage.IssueType.BUSINESSRULE, pn+"."+source.getPath(), "Derived max ("+derived.getMax()+") cannot be greater than base max ("+base.getMax()+")", ValidationMessage.IssueSeverity.ERROR));
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

      for (ElementDefinitionExampleComponent ex : derived.getExample()) {
        boolean found = false;
        for (ElementDefinitionExampleComponent exS : base.getExample())
          if (Base.compareDeep(ex, exS, false))
            found = true;
        if (!found)
          base.addExample(ex.copy());
        else if (trimDifferential)
          derived.getExample().remove(ex);
        else
          ex.setUserData(DERIVATION_EQUALS, true);
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
        if (!(base.hasMustSupportElement() && Base.compareDeep(derived.getMustSupportElement(), base.getMustSupportElement(), false)))
          base.setMustSupportElement(derived.getMustSupportElement().copy());
        else if (trimDifferential)
          derived.setMustSupportElement(null);
        else
          derived.getMustSupportElement().setUserData(DERIVATION_EQUALS, true);
      }


      // profiles cannot change : isModifier, defaultValue, meaningWhenMissing
      // but extensions can change isModifier
      if (isExtension) {
        if (derived.hasIsModifierElement() && !(base.hasIsModifierElement() && Base.compareDeep(derived.getIsModifierElement(), base.getIsModifierElement(), false)))
          base.setIsModifierElement(derived.getIsModifierElement().copy());
        else if (trimDifferential)
          derived.setIsModifierElement(null);
        else if (derived.hasIsModifierElement())
          derived.getIsModifierElement().setUserData(DERIVATION_EQUALS, true);
      }

      if (derived.hasBinding()) {
        if (!base.hasBinding() || !Base.compareDeep(derived.getBinding(), base.getBinding(), false)) {
          if (base.hasBinding() && base.getBinding().getStrength() == BindingStrength.REQUIRED && derived.getBinding().getStrength() != BindingStrength.REQUIRED)
            messages.add(new ValidationMessage(Source.ProfileValidator, ValidationMessage.IssueType.BUSINESSRULE, pn+"."+derived.getPath(), "illegal attempt to change the binding on "+derived.getPath()+" from "+base.getBinding().getStrength().toCode()+" to "+derived.getBinding().getStrength().toCode(), ValidationMessage.IssueSeverity.ERROR));
//            throw new DefinitionException("StructureDefinition "+pn+" at "+derived.getPath()+": illegal attempt to change a binding from "+base.getBinding().getStrength().toCode()+" to "+derived.getBinding().getStrength().toCode());
          else if (base.hasBinding() && derived.hasBinding() && base.getBinding().getStrength() == BindingStrength.REQUIRED && base.getBinding().hasValueSetReference() && derived.getBinding().hasValueSetReference()) {
            ValueSetExpansionOutcome expBase = context.expandVS(context.fetchResource(ValueSet.class, base.getBinding().getValueSetReference().getReference()), true, false);
            ValueSetExpansionOutcome expDerived = context.expandVS(context.fetchResource(ValueSet.class, derived.getBinding().getValueSetReference().getReference()), true, false);
            if (expBase.getValueset() == null)
              messages.add(new ValidationMessage(Source.ProfileValidator, ValidationMessage.IssueType.BUSINESSRULE, pn+"."+base.getPath(), "Binding "+base.getBinding().getValueSetReference().getReference()+" could not be expanded", ValidationMessage.IssueSeverity.WARNING));
            else if (expDerived.getValueset() == null)
              messages.add(new ValidationMessage(Source.ProfileValidator, ValidationMessage.IssueType.BUSINESSRULE, pn+"."+derived.getPath(), "Binding "+derived.getBinding().getValueSetReference().getReference()+" could not be expanded", ValidationMessage.IssueSeverity.WARNING));
            else if (!isSubset(expBase.getValueset(), expDerived.getValueset()))
              messages.add(new ValidationMessage(Source.ProfileValidator, ValidationMessage.IssueType.BUSINESSRULE, pn+"."+derived.getPath(), "Binding "+derived.getBinding().getValueSetReference().getReference()+" is not a subset of binding "+base.getBinding().getValueSetReference().getReference(), ValidationMessage.IssueSeverity.ERROR));
          }
          base.setBinding(derived.getBinding().copy());
        } else if (trimDifferential)
          derived.setBinding(null);
        else
          derived.getBinding().setUserData(DERIVATION_EQUALS, true);
      } // else if (base.hasBinding() && doesn't have bindable type )
        //  base

      if (derived.hasIsSummaryElement()) {
        if (!Base.compareDeep(derived.getIsSummaryElement(), base.getIsSummaryElement(), false)) {
          if (base.hasIsSummary())
            throw new Error("Error in profile "+pn+" at "+derived.getPath()+": Base isSummary = "+base.getIsSummaryElement().asStringValue()+", derived isSummary = "+derived.getIsSummaryElement().asStringValue());
          base.setIsSummaryElement(derived.getIsSummaryElement().copy());
        } else if (trimDifferential)
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
              for (TypeRefComponent td : base.getType()) {;
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
      for (ElementDefinitionConstraintComponent s : base.getConstraint()) { 
        s.setUserData(IS_DERIVED, true);
        if (!s.hasSource())
          s.setSource(base.getId());
      }
      if (derived.hasConstraint()) {
      	for (ElementDefinitionConstraintComponent s : derived.getConstraint()) {
      	  ElementDefinitionConstraintComponent inv = s.copy();
          base.getConstraint().add(inv);
      	}
      }
      
      // now, check that we still have a bindable type; if not, delete the binding - see task 8477
      if (dest.hasBinding() && !hasBindableType(dest))
        dest.setBinding(null);
        
      // finally, we copy any extensions from source to dest
      for (Extension ex : base.getExtension()) {
        StructureDefinition sd  = context.fetchResource(StructureDefinition.class, ex.getUrl());
        if (sd == null || sd.getSnapshot() == null || sd.getSnapshot().getElementFirstRep().getMax().equals("1"))
          ToolingExtensions.removeExtension(dest, ex.getUrl());
        dest.addExtension(ex);
      }
    }
  }

  private boolean hasBindableType(ElementDefinition ed) {
    for (TypeRefComponent tr : ed.getType()) {
      if (Utilities.existsInList(tr.getCode(), "Coding", "CodeableConcept", "Quantity", "url", "string", "code"))
        return true;
    }
    return false;
  }


  private boolean isLargerMax(String derived, String base) {
    if ("*".equals(base))
      return false;
    if ("*".equals(derived))
      return true;
    return Integer.parseInt(derived) > Integer.parseInt(base);
  }


  private boolean isSubset(ValueSet expBase, ValueSet expDerived) {
    return codesInExpansion(expDerived.getExpansion().getContains(), expBase.getExpansion());
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


  private boolean inExpansion(ValueSetExpansionContainsComponent cc, List<ValueSetExpansionContainsComponent> contains) {
    for (ValueSetExpansionContainsComponent cc1 : contains) {
      if (cc.getSystem().equals(cc1.getSystem()) && cc.getCode().equals(cc1.getCode()))
        return true;
      if (inExpansion(cc,  cc1.getContains()))
        return true;
    }
    return false;
  }

  public void closeDifferential(StructureDefinition base, StructureDefinition derived) throws FHIRException {
    for (ElementDefinition edb : base.getSnapshot().getElement()) {
      if (isImmediateChild(edb) && !edb.getPath().endsWith(".id")) {
        ElementDefinition edm = getMatchInDerived(edb, derived.getDifferential().getElement());
        if (edm == null) {
          ElementDefinition edd = derived.getDifferential().addElement();
          edd.setPath(edb.getPath());
          edd.setMax("0");
        } else if (edb.hasSlicing()) {
          closeChildren(base, edb, derived, edm);
        }
      }
    }
    sortDifferential(base, derived, derived.getName(), new ArrayList<String>());
  }

  private void closeChildren(StructureDefinition base, ElementDefinition edb, StructureDefinition derived, ElementDefinition edm) {
    String path = edb.getPath()+".";
    int baseStart = base.getSnapshot().getElement().indexOf(edb);
    int baseEnd = findEnd(base.getSnapshot().getElement(), edb, baseStart+1);
    int diffStart = derived.getDifferential().getElement().indexOf(edm);
    int diffEnd = findEnd(derived.getDifferential().getElement(), edm, diffStart+1);
    
    for (int cBase = baseStart; cBase < baseEnd; cBase++) {
      ElementDefinition edBase = base.getSnapshot().getElement().get(cBase);
      if (isImmediateChild(edBase, edb)) {
        ElementDefinition edMatch = getMatchInDerived(edBase, derived.getDifferential().getElement(), diffStart, diffEnd);
        if (edMatch == null) {
          ElementDefinition edd = derived.getDifferential().addElement();
          edd.setPath(edBase.getPath());
          edd.setMax("0");
        } else {
          closeChildren(base, edBase, derived, edMatch);
        }        
      }
    }
  }




  private int findEnd(List<ElementDefinition> list, ElementDefinition ed, int cursor) {
    String path = ed.getPath()+".";
    while (cursor < list.size() && list.get(cursor).getPath().startsWith(path))
      cursor++;
    return cursor;
  }


  private ElementDefinition getMatchInDerived(ElementDefinition ed, List<ElementDefinition> list) {
    for (ElementDefinition t : list)
      if (t.getPath().equals(ed.getPath()))
        return t;
    return null;
  }

  private ElementDefinition getMatchInDerived(ElementDefinition ed, List<ElementDefinition> list, int start, int end) {
    for (int i = start; i < end; i++) {
      ElementDefinition t = list.get(i);
      if (t.getPath().equals(ed.getPath()))
        return t;
    }
    return null;
  }


  private boolean isImmediateChild(ElementDefinition ed) {
    String p = ed.getPath();
    if (!p.contains("."))
      return false;
    p = p.substring(p.indexOf(".")+1);
    return !p.contains(".");
  }

  private boolean isImmediateChild(ElementDefinition candidate, ElementDefinition base) {
    String p = candidate.getPath();
    if (!p.contains("."))
      return false;
    if (!p.startsWith(base.getPath()+"."))
      return false;
    p = p.substring(base.getPath().length()+1);
    return !p.contains(".");
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


  private static final int AGG_NONE = 0;
  private static final int AGG_IND = 1;
  private static final int AGG_GR = 2;
  private Cell genTypes(HierarchicalTableGenerator gen, Row r, ElementDefinition e, String profileBaseFileName, StructureDefinition profile, String corePath, String imagePath) {
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
    int aggMode = AGG_NONE;

    boolean allReference = !types.isEmpty();
    Set<AggregationMode> aggs = new HashSet<ElementDefinition.AggregationMode>();
    for (TypeRefComponent t : types) {
      if (t.getCode()!=null && t.getCode().equals("Reference") && t.hasProfile()) {
        for (Enumeration<AggregationMode> en : t.getAggregation())
          aggs.add(en.getValue());
      } else
        allReference = false;
      
    }
    if (allReference) {
      if (aggs.size() > 0) {
        boolean allSame = true;
        for (TypeRefComponent t : types) {
          for (AggregationMode agg : aggs) {
            boolean found = false;
            for (Enumeration<AggregationMode> en : t.getAggregation())
              if (en.getValue() == agg)
                found = true;
            if (!found)
              allSame = false;
          }
        }
        aggMode = allSame ? AGG_GR : AGG_IND;
        if (aggMode != AGG_GR)
          allReference = false;
      }
    } else 
      aggMode = aggs.size() == 0 ? AGG_NONE : AGG_IND;

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
      if (t.getCode()!= null && t.getCode().equals("Reference")) {
        if (!allReference) {
          c.getPieces().add(gen.new Piece(corePath+"references.html", "Reference", null));
          c.getPieces().add(gen.new Piece(null, "(", null));
        }
        if (t.hasTargetProfile() && t.getTargetProfile().startsWith("http://hl7.org/fhir/StructureDefinition/")) {
          StructureDefinition sd = context.fetchResource(StructureDefinition.class, t.getTargetProfile());
          if (sd != null) {
            String disp = sd.hasTitle() ? sd.getTitle() : sd.getName();
            c.addPiece(checkForNoChange(t, gen.new Piece(checkPrepend(corePath, sd.getUserString("path")), disp, null)));
          } else {
            String rn = t.getTargetProfile().substring(40);
            c.addPiece(checkForNoChange(t, gen.new Piece(pkp.getLinkFor(corePath, rn), rn, null)));
          }
        } else if (t.hasTargetProfile() && Utilities.isAbsoluteUrl(t.getTargetProfile())) {
          StructureDefinition sd = context.fetchResource(StructureDefinition.class, t.getTargetProfile());
          if (sd != null) {
            String disp = sd.hasTitle() ? sd.getTitle() : sd.getName();
            String ref = pkp.getLinkForProfile(null, sd.getUrl());
            if (ref.contains("|"))
              ref = ref.substring(0,  ref.indexOf("|"));
            c.addPiece(checkForNoChange(t, gen.new Piece(ref, disp, null)));
          } else
            c.addPiece(checkForNoChange(t, gen.new Piece(null, t.getTargetProfile(), null)));
        } else if (t.hasTargetProfile() && t.getTargetProfile().startsWith("#"))
          c.addPiece(checkForNoChange(t, gen.new Piece(corePath+profileBaseFileName+"."+t.getTargetProfile().substring(1).toLowerCase()+".html", t.getTargetProfile(), null)));
        else if (t.hasTargetProfile())
          c.addPiece(checkForNoChange(t, gen.new Piece(corePath+t.getTargetProfile(), t.getTargetProfile(), null)));
        if (!allReference) {
          c.getPieces().add(gen.new Piece(null, ")", null));
          if (t.getAggregation().size() > 0) {
            c.getPieces().add(gen.new Piece(corePath+"valueset-resource-aggregation-mode.html", " {", null));
            boolean firstA = true;
            for (Enumeration<AggregationMode> a : t.getAggregation()) {
              if (firstA = true)
                firstA = false;
              else
                c.getPieces().add(gen.new Piece(corePath+"valueset-resource-aggregation-mode.html", ", ", null));
              c.getPieces().add(gen.new Piece(corePath+"valueset-resource-aggregation-mode.html", codeForAggregation(a.getValue()), null));
            }
            c.getPieces().add(gen.new Piece(corePath+"valueset-resource-aggregation-mode.html", "}", null));
          }
        }
      } else if (t.hasProfile()) { // a profiled type
        String ref;
        ref = pkp.getLinkForProfile(profile, t.getProfile());
        if (ref != null) {
          String[] parts = ref.split("\\|");
          if (parts[0].startsWith("http:") || parts[0].startsWith("https:"))
            c.addPiece(checkForNoChange(t, gen.new Piece(parts[0], parts[1], t.getCode())));
          else
            c.addPiece(checkForNoChange(t, gen.new Piece((t.getProfile().startsWith(corePath)? corePath: "")+parts[0], parts[1], t.getCode())));
        } else
          c.addPiece(checkForNoChange(t, gen.new Piece((t.getProfile().startsWith(corePath)? corePath: "")+ref, t.getCode(), null)));
      } else if (pkp.hasLinkFor(t.getCode())) {
        c.addPiece(checkForNoChange(t, gen.new Piece(pkp.getLinkFor(corePath, t.getCode()), t.getCode(), null)));
      } else
        c.addPiece(checkForNoChange(t, gen.new Piece(null, t.getCode(), null)));
    }
    if (allReference) {
      c.getPieces().add(gen.new Piece(null, ")", null));
      if (aggs.size() > 0) {
        c.getPieces().add(gen.new Piece(corePath+"valueset-resource-aggregation-mode.html", " {", null));
        boolean firstA = true;
        for (AggregationMode a : aggs) {
          if (firstA = true)
            firstA = false;
          else
            c.getPieces().add(gen.new Piece(corePath+"valueset-resource-aggregation-mode.html", ", ", null));
          c.getPieces().add(gen.new Piece(corePath+"valueset-resource-aggregation-mode.html", codeForAggregation(a), null));
        }
        c.getPieces().add(gen.new Piece(corePath+"valueset-resource-aggregation-mode.html", "}", null));
      }
    }
    return c;
  }

  private String codeForAggregation(AggregationMode a) {
    switch (a) {
    case BUNDLED : return "b";
    case CONTAINED : return "c";
    case REFERENCED: return "r";
    }
    return "?";
  }


  private String checkPrepend(String corePath, String path) {
    if (pkp.prependLinks() && !(path.startsWith("http:") || path.startsWith("https:")))
      return corePath+path;
    else 
      return path;
  }


  private ElementDefinition getElementByName(List<ElementDefinition> elements, String contentReference) {
    for (ElementDefinition ed : elements)
      if (ed.hasSliceName() && ("#"+ed.getSliceName()).equals(contentReference))
        return ed;
    return null;
  }

  private ElementDefinition getElementById(List<ElementDefinition> elements, String contentReference) {
    for (ElementDefinition ed : elements)
      if (ed.hasId() && ("#"+ed.getId()).equals(contentReference))
        return ed;
    return null;
  }


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

  private void genCardinality(HierarchicalTableGenerator gen, ElementDefinition definition, Row row, boolean hasDef, UnusedTracker tracker, ElementDefinition fallback) {
    IntegerType min = !hasDef ? new IntegerType() : definition.hasMinElement() ? definition.getMinElement() : new IntegerType();
    StringType max = !hasDef ? new StringType() : definition.hasMaxElement() ? definition.getMaxElement() : new StringType();
    if (min.isEmpty() && definition.getUserData(DERIVATION_POINTER) != null) {
      ElementDefinition base = (ElementDefinition) definition.getUserData(DERIVATION_POINTER);
      if (base.hasMinElement()) {
        min = base.getMinElement().copy();
        min.setUserData(DERIVATION_EQUALS, true);
      }
    }
    if (max.isEmpty() && definition.getUserData(DERIVATION_POINTER) != null) {
      ElementDefinition base = (ElementDefinition) definition.getUserData(DERIVATION_POINTER);
      if (base.hasMaxElement()) {
        max = base.getMaxElement().copy();
        max.setUserData(DERIVATION_EQUALS, true);
      }
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



  private boolean usesMustSupport(List<ElementDefinition> list) {
    for (ElementDefinition ed : list)
      if (ed.hasMustSupport() && ed.getMustSupport())
        return true;
    return false;
  }


  private void genElement(String defPath, HierarchicalTableGenerator gen, List<Row> rows, ElementDefinition element, List<ElementDefinition> all, List<StructureDefinition> profiles, boolean showMissing, String profileBaseFileName, Boolean extensions, boolean snapshot, String corePath, String imagePath, boolean root, boolean logicalModel, boolean isConstraintMode, boolean allInvariants) throws IOException {
    StructureDefinition profile = profiles == null ? null : profiles.get(profiles.size()-1);
    String s = tail(element.getPath());
    List<ElementDefinition> children = getChildren(all, element);
    boolean isExtension = (s.equals("extension") || s.equals("modifierExtension"));
    if (!snapshot && isExtension && extensions != null && extensions != isExtension)
      return;

    if (!onlyInformationIsMapping(all, element)) {
      Row row = gen.new Row();
      row.setAnchor(element.getPath());
      row.setColor(getRowColor(element, isConstraintMode));
      boolean hasDef = element != null;
      boolean ext = false;
      if (s.equals("extension")) {
        if (element.hasType() && element.getType().get(0).hasProfile() && extensionIsComplex(element.getType().get(0).getProfile()))
          row.setIcon("icon_extension_complex.png", HierarchicalTableGenerator.TEXT_ICON_EXTENSION_COMPLEX);
        else
          row.setIcon("icon_extension_simple.png", HierarchicalTableGenerator.TEXT_ICON_EXTENSION_SIMPLE);
        ext = true;
      } else if (s.equals("modifierExtension")) {
        if (element.hasType() && element.getType().get(0).hasProfile() && extensionIsComplex(element.getType().get(0).getProfile()))
          row.setIcon("icon_modifier_extension_complex.png", HierarchicalTableGenerator.TEXT_ICON_EXTENSION_COMPLEX);
        else
          row.setIcon("icon_modifier_extension_simple.png", HierarchicalTableGenerator.TEXT_ICON_EXTENSION_SIMPLE);
      } else if (!hasDef || element.getType().size() == 0)
        row.setIcon("icon_element.gif", HierarchicalTableGenerator.TEXT_ICON_ELEMENT);
      else if (hasDef && element.getType().size() > 1) {
        if (allTypesAre(element.getType(), "Reference"))
          row.setIcon("icon_reference.png", HierarchicalTableGenerator.TEXT_ICON_REFERENCE);
        else
          row.setIcon("icon_choice.gif", HierarchicalTableGenerator.TEXT_ICON_CHOICE);
      } else if (hasDef && element.getType().get(0).getCode() != null && element.getType().get(0).getCode().startsWith("@"))
        row.setIcon("icon_reuse.png", HierarchicalTableGenerator.TEXT_ICON_REUSE);
      else if (hasDef && isPrimitive(element.getType().get(0).getCode()))
        row.setIcon("icon_primitive.png", HierarchicalTableGenerator.TEXT_ICON_PRIMITIVE);
      else if (hasDef && isReference(element.getType().get(0).getCode()))
        row.setIcon("icon_reference.png", HierarchicalTableGenerator.TEXT_ICON_REFERENCE);
      else if (hasDef && isDataType(element.getType().get(0).getCode()))
        row.setIcon("icon_datatype.gif", HierarchicalTableGenerator.TEXT_ICON_DATATYPE);
      else
        row.setIcon("icon_resource.png", HierarchicalTableGenerator.TEXT_ICON_RESOURCE);
      String ref = defPath == null ? null : defPath + element.getId();
      UnusedTracker used = new UnusedTracker();
      used.used = true;
      Cell left = gen.new Cell(null, ref, s, (element.hasSliceName() ? translate("sd.table", "Slice")+" "+element.getSliceName() : "")+(hasDef && element.hasSliceName() ? ": " : "")+(!hasDef ? null : gt(element.getDefinitionElement())), null);
      row.getCells().add(left);
      Cell gc = gen.new Cell();
      row.getCells().add(gc);
      if (element != null && element.getIsModifier())
        checkForNoChange(element.getIsModifierElement(), gc.addImage(imagePath+"modifier.png", translate("sd.table", "This element is a modifier element"), "?!", null, null));
      if (element != null && element.getMustSupport())
        checkForNoChange(element.getMustSupportElement(), gc.addImage(imagePath+"mustsupport.png", translate("sd.table", "This element must be supported"), "S", "white", "red"));
      if (element != null && element.getIsSummary())
        checkForNoChange(element.getIsSummaryElement(), gc.addImage(imagePath+"summary.png", translate("sd.table", "This element is included in summaries"), "Σ", null, null));
      if (element != null && (!element.getConstraint().isEmpty() || !element.getCondition().isEmpty()))
        gc.addImage(imagePath+"lock.png", translate("sd.table", "This element has or is affected by some invariants"), "I", null, null);

      ExtensionContext extDefn = null;
      if (ext) {
        if (element != null && element.getType().size() == 1 && element.getType().get(0).hasProfile()) {
        extDefn = locateExtension(StructureDefinition.class, element.getType().get(0).getProfile());
          if (extDefn == null) {
            genCardinality(gen, element, row, hasDef, used, null);
            row.getCells().add(gen.new Cell(null, null, "?? "+element.getType().get(0).getProfile(), null, null));
            generateDescription(gen, row, element, null, used.used, profile.getUrl(), element.getType().get(0).getProfile(), profile, corePath, imagePath, root, logicalModel, allInvariants);
          } else {
            String name = urltail(element.getType().get(0).getProfile());
            left.getPieces().get(0).setText(name);
            // left.getPieces().get(0).setReference((String) extDefn.getExtensionStructure().getTag("filename"));
            left.getPieces().get(0).setHint(translate("sd.table", "Extension URL")+" = "+extDefn.getUrl());
            genCardinality(gen, element, row, hasDef, used, extDefn.getElement());
            ElementDefinition valueDefn = extDefn.getExtensionValueDefinition();
            if (valueDefn != null && !"0".equals(valueDefn.getMax()))
               genTypes(gen, row, valueDefn, profileBaseFileName, profile, corePath, imagePath);
             else // if it's complex, we just call it nothing
                // genTypes(gen, row, extDefn.getSnapshot().getElement().get(0), profileBaseFileName, profile);
              row.getCells().add(gen.new Cell(null, null, "("+translate("sd.table", "Complex")+")", null, null));
            generateDescription(gen, row, element, extDefn.getElement(), used.used, null, extDefn.getUrl(), profile, corePath, imagePath, root, logicalModel, allInvariants, valueDefn);
          }
        } else {
          genCardinality(gen, element, row, hasDef, used, null);
          if ("0".equals(element.getMax()))
            row.getCells().add(gen.new Cell());            
          else
            genTypes(gen, row, element, profileBaseFileName, profile, corePath, imagePath);
          generateDescription(gen, row, element, null, used.used, null, null, profile, corePath, imagePath, root, logicalModel, allInvariants);
        }
      } else {
        genCardinality(gen, element, row, hasDef, used, null);
        if (hasDef && !"0".equals(element.getMax()))
          genTypes(gen, row, element, profileBaseFileName, profile, corePath, imagePath);
        else
          row.getCells().add(gen.new Cell());
        generateDescription(gen, row, element, null, used.used, null, null, profile, corePath, imagePath, root, logicalModel, allInvariants);
      }
      if (element.hasSlicing()) {
        if (standardExtensionSlicing(element)) {
          used.used = element.hasType() && element.getType().get(0).hasProfile();
          showMissing = false;
        } else {
          row.setIcon("icon_slice.png", HierarchicalTableGenerator.TEXT_ICON_SLICE);
          row.getCells().get(2).getPieces().clear();
          for (Cell cell : row.getCells())
            for (Piece p : cell.getPieces()) {
              p.addStyle("font-style: italic");
            }
        }
      }
      if (used.used || showMissing)
        rows.add(row);
      if (!used.used && !element.hasSlicing()) {
        for (Cell cell : row.getCells())
          for (Piece p : cell.getPieces()) {
            p.setStyle("text-decoration:line-through");
            p.setReference(null);
          }
      } else{
        for (ElementDefinition child : children)
          if (logicalModel || !child.getPath().endsWith(".id") || (child.getPath().endsWith(".id") && (profile != null) && (profile.getDerivation() == TypeDerivationRule.CONSTRAINT)))  
            genElement(defPath, gen, row.getSubRows(), child, all, profiles, showMissing, profileBaseFileName, isExtension, snapshot, corePath, imagePath, false, logicalModel, isConstraintMode, allInvariants);
        if (!snapshot && (extensions == null || !extensions))
          for (ElementDefinition child : children)
            if (child.getPath().endsWith(".extension") || child.getPath().endsWith(".modifierExtension"))
              genElement(defPath, gen, row.getSubRows(), child, all, profiles, showMissing, profileBaseFileName, true, false, corePath, imagePath, false, logicalModel, isConstraintMode, allInvariants);
      }
    }
  }


  private ExtensionContext locateExtension(Class<StructureDefinition> class1, String value)  {
    if (value.contains("#")) {
      StructureDefinition ext = context.fetchResource(StructureDefinition.class, value.substring(0, value.indexOf("#")));
      if (ext == null)
        return null;
      String tail = value.substring(value.indexOf("#")+1);
      ElementDefinition ed = null;
      for (ElementDefinition ted : ext.getSnapshot().getElement()) {
        if (tail.equals(ted.getSliceName())) {
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


  private boolean extensionIsComplex(String value) {
    if (value.contains("#")) {
      StructureDefinition ext = context.fetchResource(StructureDefinition.class, value.substring(0, value.indexOf("#")));
    if (ext == null)
      return false;
      String tail = value.substring(value.indexOf("#")+1);
      ElementDefinition ed = null;
      for (ElementDefinition ted : ext.getSnapshot().getElement()) {
        if (tail.equals(ted.getSliceName())) {
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


  private String getRowColor(ElementDefinition element, boolean isConstraintMode) {
    switch (element.getUserInt(UD_ERROR_STATUS)) {
    case STATUS_HINT: return ROW_COLOR_HINT;
    case STATUS_WARNING: return ROW_COLOR_WARNING;
    case STATUS_ERROR: return ROW_COLOR_ERROR;
    case STATUS_FATAL: return ROW_COLOR_FATAL;
    }
    if (isConstraintMode && !element.getMustSupport() && !element.getIsModifier() && element.getPath().contains("."))
      return null; // ROW_COLOR_NOT_MUST_SUPPORT;
    else
      return null;
  }


  private String urltail(String path) {
    if (path.contains("#"))
      return path.substring(path.lastIndexOf('#')+1);
    if (path.contains("/"))
      return path.substring(path.lastIndexOf('/')+1);
    else
      return path;

  }

  private boolean standardExtensionSlicing(ElementDefinition element) {
    String t = tail(element.getPath());
    return (t.equals("extension") || t.equals("modifierExtension"))
          && element.getSlicing().getRules() != SlicingRules.CLOSED && element.getSlicing().getDiscriminator().size() == 1 && element.getSlicing().getDiscriminator().get(0).getPath().equals("url") && element.getSlicing().getDiscriminator().get(0).getType().equals(DiscriminatorType.VALUE);
  }

  private Cell generateDescription(HierarchicalTableGenerator gen, Row row, ElementDefinition definition, ElementDefinition fallback, boolean used, String baseURL, String url, StructureDefinition profile, String corePath, String imagePath, boolean root, boolean logicalModel, boolean allInvariants) throws IOException {
    return generateDescription(gen, row, definition, fallback, used, baseURL, url, profile, corePath, imagePath, root, logicalModel, allInvariants, null);
  }
  
  private Cell generateDescription(HierarchicalTableGenerator gen, Row row, ElementDefinition definition, ElementDefinition fallback, boolean used, String baseURL, String url, StructureDefinition profile, String corePath, String imagePath, boolean root, boolean logicalModel, boolean allInvariants, ElementDefinition valueDefn) throws IOException {
    Cell c = gen.new Cell();
    row.getCells().add(c);

    if (used) {
      if (logicalModel && ToolingExtensions.hasExtension(profile, "http://hl7.org/fhir/StructureDefinition/elementdefinition-namespace")) {
        if (root) {
          c.getPieces().add(gen.new Piece(null, translate("sd.table", "XML Namespace")+": ", null).addStyle("font-weight:bold"));
          c.getPieces().add(gen.new Piece(null, ToolingExtensions.readStringExtension(profile, "http://hl7.org/fhir/StructureDefinition/elementdefinition-namespace"), null));        
        } else if (!root && ToolingExtensions.hasExtension(definition, "http://hl7.org/fhir/StructureDefinition/elementdefinition-namespace") && 
            !ToolingExtensions.readStringExtension(definition, "http://hl7.org/fhir/StructureDefinition/elementdefinition-namespace").equals(ToolingExtensions.readStringExtension(profile, "http://hl7.org/fhir/StructureDefinition/elementdefinition-namespace"))) {
          c.getPieces().add(gen.new Piece(null, translate("sd.table", "XML Namespace")+": ", null).addStyle("font-weight:bold"));
          c.getPieces().add(gen.new Piece(null, ToolingExtensions.readStringExtension(definition, "http://hl7.org/fhir/StructureDefinition/elementdefinition-namespace"), null));        
        }
      }
      
      if (definition.hasContentReference()) {
        ElementDefinition ed = getElementByName(profile.getSnapshot().getElement(), definition.getContentReference());
        if (ed == null)
          c.getPieces().add(gen.new Piece(null, translate("sd.table", "Unknown reference to %s", definition.getContentReference()), null));
        else
          c.getPieces().add(gen.new Piece("#"+ed.getPath(), translate("sd.table", "See %s", ed.getPath()), null));
      }
      if (definition.getPath().endsWith("url") && definition.hasFixed()) {
        c.getPieces().add(checkForNoChange(definition.getFixed(), gen.new Piece(null, "\""+buildJson(definition.getFixed())+"\"", null).addStyle("color: darkgreen")));
      } else {
        if (definition != null && definition.hasShort()) {
          if (!c.getPieces().isEmpty()) c.addPiece(gen.new Piece("br"));
          c.addPiece(checkForNoChange(definition.getShortElement(), gen.new Piece(null, gt(definition.getShortElement()), null)));
        } else if (fallback != null && fallback != null && fallback.hasShort()) {
          if (!c.getPieces().isEmpty()) c.addPiece(gen.new Piece("br"));
          c.addPiece(checkForNoChange(fallback.getShortElement(), gen.new Piece(null, gt(fallback.getShortElement()), null)));
        }
        if (url != null) {
          if (!c.getPieces().isEmpty()) c.addPiece(gen.new Piece("br"));
          String fullUrl = url.startsWith("#") ? baseURL+url : url;
          StructureDefinition ed = context.fetchResource(StructureDefinition.class, url);
          String ref = null;
          if (ed != null) {
            String p = ed.getUserString("path");
            if (p != null) {
              ref = p.startsWith("http:") || igmode ? p : Utilities.pathReverse(corePath, p);
            }
          }
          c.getPieces().add(gen.new Piece(null, translate("sd.table", "URL")+": ", null).addStyle("font-weight:bold"));
          c.getPieces().add(gen.new Piece(ref, fullUrl, null));
        }

        if (definition.hasSlicing()) {
          if (!c.getPieces().isEmpty()) c.addPiece(gen.new Piece("br"));
          c.getPieces().add(gen.new Piece(null, translate("sd.table", "Slice")+": ", null).addStyle("font-weight:bold"));
          c.getPieces().add(gen.new Piece(null, describeSlice(definition.getSlicing()), null));
        }
        if (definition != null) {
          ElementDefinitionBindingComponent binding = null;
          if (valueDefn != null && valueDefn.hasBinding() && !valueDefn.getBinding().isEmpty())
            binding = valueDefn.getBinding();
          else if (definition.hasBinding())
            binding = definition.getBinding();
          if (binding!=null && !binding.isEmpty()) {
            if (!c.getPieces().isEmpty()) 
              c.addPiece(gen.new Piece("br"));
            BindingResolution br = pkp.resolveBinding(profile, binding, definition.getPath());
            c.getPieces().add(checkForNoChange(binding, gen.new Piece(null, translate("sd.table", "Binding")+": ", null).addStyle("font-weight:bold")));
            c.getPieces().add(checkForNoChange(binding, gen.new Piece(br.url == null ? null : Utilities.isAbsoluteUrl(br.url) || !pkp.prependLinks() ? br.url : corePath+br.url, br.display, null)));
            if (binding.hasStrength()) {
              c.getPieces().add(checkForNoChange(binding, gen.new Piece(null, " (", null)));
              c.getPieces().add(checkForNoChange(binding, gen.new Piece(corePath+"terminologies.html#"+binding.getStrength().toCode(), egt(binding.getStrengthElement()), binding.getStrength().getDefinition())));              
              c.getPieces().add(gen.new Piece(null, ")", null));
            }
          }
          for (ElementDefinitionConstraintComponent inv : definition.getConstraint()) {
            if (!inv.hasSource() || allInvariants) {
              if (!c.getPieces().isEmpty()) 
                c.addPiece(gen.new Piece("br"));
              c.getPieces().add(checkForNoChange(inv, gen.new Piece(null, inv.getKey()+": ", null).addStyle("font-weight:bold")));
              c.getPieces().add(checkForNoChange(inv, gen.new Piece(null, gt(inv.getHumanElement()), null)));
            }
          }
          if ((definition.hasBase() && definition.getBase().getMax().equals("*")) || (definition.hasMax() && definition.getMax().equals("*"))) {
            if (c.getPieces().size() > 0)
              c.addPiece(gen.new Piece("br"));
            if (definition.hasOrderMeaning()) {
              c.getPieces().add(gen.new Piece(null, "This repeating element order: "+definition.getOrderMeaning(), null));
            } else {
              // don't show this, this it's important: c.getPieces().add(gen.new Piece(null, "This repeating element has no defined order", null));
            }           
          }

          if (definition.hasFixed()) {
            if (!c.getPieces().isEmpty()) c.addPiece(gen.new Piece("br"));
            c.getPieces().add(checkForNoChange(definition.getFixed(), gen.new Piece(null, translate("sd.table", "Fixed Value")+": ", null).addStyle("font-weight:bold")));
            c.getPieces().add(checkForNoChange(definition.getFixed(), gen.new Piece(null, buildJson(definition.getFixed()), null).addStyle("color: darkgreen")));
            if (isCoded(definition.getFixed()) && !hasDescription(definition.getFixed())) {
              Piece p = describeCoded(gen, definition.getFixed());
              if (p != null)
                c.getPieces().add(p);
            }
          } else if (definition.hasPattern()) {
            if (!c.getPieces().isEmpty()) c.addPiece(gen.new Piece("br"));
            c.getPieces().add(checkForNoChange(definition.getPattern(), gen.new Piece(null, translate("sd.table", "Required Pattern")+": ", null).addStyle("font-weight:bold")));
            c.getPieces().add(checkForNoChange(definition.getPattern(), gen.new Piece(null, buildJson(definition.getPattern()), null).addStyle("color: darkgreen")));
          } else if (definition.hasExample()) {
            for (ElementDefinitionExampleComponent ex : definition.getExample()) {
              if (!c.getPieces().isEmpty()) c.addPiece(gen.new Piece("br"));
              c.getPieces().add(checkForNoChange(ex, gen.new Piece(null, translate("sd.table", "Example")+("".equals("General")? "" : " "+ex.getLabel()+"'")+": ", null).addStyle("font-weight:bold")));
              c.getPieces().add(checkForNoChange(ex, gen.new Piece(null, buildJson(ex.getValue()), null).addStyle("color: darkgreen")));
            }
          }
          if (definition.hasMaxLength() && definition.getMaxLength()!=0) {
            if (!c.getPieces().isEmpty()) c.addPiece(gen.new Piece("br"));
            c.getPieces().add(checkForNoChange(definition.getMaxLengthElement(), gen.new Piece(null, "Max Length: ", null).addStyle("font-weight:bold")));
            c.getPieces().add(checkForNoChange(definition.getMaxLengthElement(), gen.new Piece(null, Integer.toString(definition.getMaxLength()), null).addStyle("color: darkgreen")));
          }
          if (profile != null) {
            for (StructureDefinitionMappingComponent md : profile.getMapping()) {
              if (md.hasExtension(ToolingExtensions.EXT_TABLE_NAME)) {
                ElementDefinitionMappingComponent map = null;
                for (ElementDefinitionMappingComponent m : definition.getMapping()) 
                  if (m.getIdentity().equals(md.getIdentity()))
                    map = m;
                if (map != null) {
                  for (int i = 0; i<definition.getMapping().size(); i++){
                    c.addPiece(gen.new Piece("br"));
                    c.getPieces().add(gen.new Piece(null, ToolingExtensions.readStringExtension(md, ToolingExtensions.EXT_TABLE_NAME)+": " + map.getMap(), null));
                  }
                }
              }
            }
          }
        }
      }
    }
    return c;
  }

  private Piece describeCoded(HierarchicalTableGenerator gen, Type fixed) {
    if (fixed instanceof Coding) {
      Coding c = (Coding) fixed;
      ValidationResult vr = context.validateCode(c.getSystem(), c.getCode(), c.getDisplay());
      if (vr.getDisplay() != null)
        return gen.new Piece(null, " ("+vr.getDisplay()+")", null).addStyle("color: darkgreen");
    } else if (fixed instanceof CodeableConcept) {
      CodeableConcept cc = (CodeableConcept) fixed;
      for (Coding c : cc.getCoding()) {
        ValidationResult vr = context.validateCode(c.getSystem(), c.getCode(), c.getDisplay());
        if (vr.getDisplay() != null)
          return gen.new Piece(null, " ("+vr.getDisplay()+")", null).addStyle("color: darkgreen");
      }
    }
    return null;
  }


  private boolean hasDescription(Type fixed) {
    if (fixed instanceof Coding) {
      return ((Coding) fixed).hasDisplay();
    } else if (fixed instanceof CodeableConcept) {
      CodeableConcept cc = (CodeableConcept) fixed;
      if (cc.hasText())
        return true;
      for (Coding c : cc.getCoding())
        if (c.hasDisplay())
         return true;
    } // (fixed instanceof CodeType) || (fixed instanceof Quantity);
    return false;
  }


  private boolean isCoded(Type fixed) {
    return (fixed instanceof Coding) || (fixed instanceof CodeableConcept) || (fixed instanceof CodeType) || (fixed instanceof Quantity);
  }


  /*
  private List<Piece> markdownToPieces(String markdown) throws FHIRException {
    String htmlString = Processor.process(markdown);
    XhtmlParser parser = new XhtmlParser();
    try {
      XhtmlNode node = parser.parseFragment(htmlString);
      return htmlToPieces(node);
    } catch (IOException e) {
    }
    return null;
  }

  private List<Piece> htmlToPieces(XhtmlNode n) {
    
  }*/

  private String buildJson(Type value) throws IOException {
    if (value instanceof PrimitiveType)
      return ((PrimitiveType) value).asStringValue();

    IParser json = context.newJsonParser();
    return json.composeString(value, null);
  }


  public String describeSlice(ElementDefinitionSlicingComponent slicing) {
    return translate("sd.table", "%s, %s by %s", slicing.getOrdered() ? translate("sd.table", "Ordered") : translate("sd.table", "Unordered"), describe(slicing.getRules()), commas(slicing.getDiscriminator()));
  }

  private String commas(List<ElementDefinitionSlicingDiscriminatorComponent> list) {
    CommaSeparatedStringBuilder c = new CommaSeparatedStringBuilder();
    for (ElementDefinitionSlicingDiscriminatorComponent id : list)
      c.append(id.getType().toCode()+":"+id.getPath());
    return c.toString();
  }


  private String describe(SlicingRules rules) {
    if (rules == null)
      return translate("sd.table", "Unspecified");
    switch (rules) {
    case CLOSED : return translate("sd.table", "Closed");
    case OPEN : return translate("sd.table", "Open");
    case OPENATEND : return translate("sd.table", "Open At End");
    default:
      return "??";
    }
  }

  private boolean onlyInformationIsMapping(List<ElementDefinition> list, ElementDefinition e) {
    return (!e.hasSliceName() && !e.hasSlicing() && (onlyInformationIsMapping(e))) &&
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

  private boolean allTypesAre(List<TypeRefComponent> types, String name) {
    for (TypeRefComponent t : types) {
      if (!t.getCode().equals(name))
        return false;
    }
    return true;
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

  private String tail(String path) {
    if (path.contains("."))
      return path.substring(path.lastIndexOf('.')+1);
    else
      return path;
  }

  private boolean isDataType(String value) {
    StructureDefinition sd = context.fetchResource(StructureDefinition.class, "http://hl7.org/fhir/StructureDefinition/"+value);
    return sd != null && sd.getKind() == StructureDefinitionKind.COMPLEXTYPE;
  }

  private boolean isReference(String value) {
    return "Reference".equals(value);
  }

  public boolean isPrimitive(String value) {
    StructureDefinition sd = context.fetchResource(StructureDefinition.class, "http://hl7.org/fhir/StructureDefinition/"+value);
    return sd != null && sd.getKind() == StructureDefinitionKind.PRIMITIVETYPE;
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


  public StructureDefinition getProfile(StructureDefinition source, String url) {
  	StructureDefinition profile = null;
  	String code = null;
  	if (url.startsWith("#")) {
  		profile = source;
  		code = url.substring(1);
  	} else if (context != null) {
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

    public ElementDefinition getSelf() {
      return self;
    }

    public List<ElementDefinitionHolder> getChildren() {
      return children;
    }

    public int getBaseIndex() {
      return baseIndex;
    }

    public void setBaseIndex(int baseIndex) {
      this.baseIndex = baseIndex;
    }

    @Override
    public String toString() {
      if (self.hasSliceName())
        return self.getPath()+"("+self.getSliceName()+")";
      else
        return self.getPath();
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
        if (p.equals(actual)) {
          return i;
        }
        if (p.endsWith("[x]") && actual.startsWith(p.substring(0, p.length()-3)) && !(actual.endsWith("[x]")) && !actual.substring(p.length()-3).contains(".")) {
          return i;
      }
        if (path.startsWith(p+".") && snapshot.get(i).hasContentReference()) {
          actual = base+(snapshot.get(i).getContentReference().substring(1)+"."+path.substring(p.length()+1)).substring(prefixLength);
          i = 0;
        }
      }
      if (prefixLength == 0)
        errors.add("Differential contains path "+path+" which is not found in the base");
      else
        errors.add("Differential contains path "+path+" which is actually "+actual+", which is not found in the base");
      return 0;
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
  }


  public void sortDifferential(StructureDefinition base, StructureDefinition diff, String name, List<String> errors) throws FHIRException  {

    final List<ElementDefinition> diffList = diff.getDifferential().getElement();
    // first, we move the differential elements into a tree
    if (diffList.isEmpty())
      return;
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

  private void sortElements(ElementDefinitionHolder edh, ElementDefinitionComparer cmp, List<String> errors) throws FHIRException {
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
          StructureDefinition profile = context.fetchResource(StructureDefinition.class, child.getSelf().getType().get(0).getProfile());
          if (profile==null)
            ccmp = null; // this might happen before everything is loaded. And we don't so much care about sot order in this case
          else
          ccmp = new ElementDefinitionComparer(true, profile.getSnapshot().getElement(), ed.getType().get(0).getCode(), child.getSelf().getPath().length(), cmp.name);
        } else if (ed.getType().size() == 1 && !ed.getType().get(0).getCode().equals("*")) {
          StructureDefinition profile = context.fetchResource(StructureDefinition.class, "http://hl7.org/fhir/StructureDefinition/"+ed.getType().get(0).getCode());
          if (profile==null)
            throw new FHIRException("Unable to resolve profile " + "http://hl7.org/fhir/StructureDefinition/"+ed.getType().get(0).getCode() + " in element " + ed.getPath());
          ccmp = new ElementDefinitionComparer(false, profile.getSnapshot().getElement(), ed.getType().get(0).getCode(), child.getSelf().getPath().length(), cmp.name);
        } else if (child.getSelf().getType().size() == 1) {
          StructureDefinition profile = context.fetchResource(StructureDefinition.class, "http://hl7.org/fhir/StructureDefinition/"+child.getSelf().getType().get(0).getCode());
          if (profile==null)
            throw new FHIRException("Unable to resolve profile " + "http://hl7.org/fhir/StructureDefinition/"+ed.getType().get(0).getCode() + " in element " + ed.getPath());
          ccmp = new ElementDefinitionComparer(false, profile.getSnapshot().getElement(), child.getSelf().getType().get(0).getCode(), child.getSelf().getPath().length(), cmp.name);
        } else if (ed.getPath().endsWith("[x]") && !child.getSelf().getPath().endsWith("[x]")) {
          String edLastNode = ed.getPath().replaceAll("(.*\\.)*(.*)", "$2");
          String childLastNode = child.getSelf().getPath().replaceAll("(.*\\.)*(.*)", "$2");
          String p = childLastNode.substring(edLastNode.length()-3);
          StructureDefinition sd = context.fetchResource(StructureDefinition.class, "http://hl7.org/fhir/StructureDefinition/"+p);
          if (sd == null)
            throw new Error("Unable to find profile "+p);
          ccmp = new ElementDefinitionComparer(false, sd.getSnapshot().getElement(), p, child.getSelf().getPath().length(), cmp.name);
        } else {
          throw new Error("Not handled yet (sortElements: "+ed.getPath()+":"+typeCode(ed.getType())+")");
        }
        if (ccmp != null)
        sortElements(child, ccmp, errors);
      }
    }
  }

  private boolean isAbstract(String code) {
    return code.equals("Element") || code.equals("BackboneElement") || code.equals("Resource") || code.equals("DomainResource");
  }


  private void writeElements(ElementDefinitionHolder edh, List<ElementDefinition> list) {
    list.add(edh.getSelf());
    for (ElementDefinitionHolder child : edh.getChildren()) {
      writeElements(child, list);
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
        String name1 = o1.hasSliceName() ? o1.getSliceName() : "";
        String name2 = o2.hasSliceName() ? o2.getSliceName() : "";
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


  // generate schematrons for the rules in a structure definition
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

  
  private class Slicer extends ElementDefinitionSlicingComponent {
    String criteria = "";
    String name = "";   
    boolean check;
    public Slicer(boolean cantCheck) {
      super();
      this.check = cantCheck;
    }
  }
  
  private Slicer generateSlicer(ElementDefinition child, ElementDefinitionSlicingComponent slicing, StructureDefinition structure) {
    // given a child in a structure, it's sliced. figure out the slicing xpath
    if (child.getPath().endsWith(".extension")) {
      ElementDefinition ued = getUrlFor(structure, child);
      if ((ued == null || !ued.hasFixed()) && !(child.hasType() && (child.getType().get(0).hasProfile())))
        return new Slicer(false);
      else {
      Slicer s = new Slicer(true);
      String url = (ued == null || !ued.hasFixed()) ? child.getType().get(0).getProfile() : ((UriType) ued.getFixed()).asStringValue();
      s.name = " with URL = '"+url+"'";
      s.criteria = "[@url = '"+url+"']";
      return s;
      }
    } else
      return new Slicer(false);
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
      boolean doMax = child.hasMax() && !child.getMax().equals("*") && (based == null || (!child.getMax().equals(based.getMax())));
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




  private ElementDefinition getByPath(StructureDefinition base, String path) {
		for (ElementDefinition ed : base.getSnapshot().getElement()) {
			if (ed.getPath().equals(path))
				return ed;
			if (ed.getPath().endsWith("[x]") && ed.getPath().length() <= path.length()-3 &&  ed.getPath().substring(0, ed.getPath().length()-3).equals(path.substring(0, ed.getPath().length()-3)))
				return ed;
    }
	  return null;
  }


  public void setIds(StructureDefinition sd, boolean checkFirst) throws DefinitionException  {
    if (!checkFirst || !sd.hasDifferential() || hasMissingIds(sd.getDifferential().getElement())) {
      if (!sd.hasDifferential())
        sd.setDifferential(new StructureDefinitionDifferentialComponent());
      generateIds(sd.getDifferential().getElement(), sd.getName());
    }
    if (!checkFirst || !sd.hasSnapshot() || hasMissingIds(sd.getSnapshot().getElement())) {
      if (!sd.hasSnapshot())
        sd.setSnapshot(new StructureDefinitionSnapshotComponent());
      generateIds(sd.getSnapshot().getElement(), sd.getName());
    }
  }


  private boolean hasMissingIds(List<ElementDefinition> list) {
    for (ElementDefinition ed : list) {
      if (!ed.hasId())
        return true;
    }    
    return false;
  }


  private void generateIds(List<ElementDefinition> list, String name) throws DefinitionException  {
    if (list.isEmpty())
      return;
    
    Map<String, String> idMap = new HashMap<String, String>();
    
    List<String> paths = new ArrayList<String>();
    // first pass, update the element ids
    for (ElementDefinition ed : list) {
      if (!ed.hasPath())
        throw new DefinitionException("No path on element Definition "+Integer.toString(list.indexOf(ed))+" in "+name);
      int depth = charCount(ed.getPath(), '.');
      String tail = tail(ed.getPath());

      if (depth > paths.size()) {
        // this means that we've jumped into a sparse thing. 
        String[] pl = ed.getPath().split("\\.");
        for (int i = paths.size(); i < pl.length-1; i++) // -1 because the last path is in focus
          paths.add(pl[i]);
      }
      while (depth < paths.size() && paths.size() > 0)
        paths.remove(paths.size() - 1);
      
      String t = ed.hasSliceName() ? tail+":"+checkName(ed.getSliceName()) : /* why do this? name != null ? tail + ":"+checkName(name) : */ tail;
//      if (isExtension(ed))
//        t = t + describeExtension(ed);
      name = null;
      StringBuilder b = new StringBuilder();
      for (String s : paths) {
        b.append(s);
        b.append(".");
      }
      b.append(t);
      String bs = b.toString();
      idMap.put(ed.hasId() ? ed.getId() : ed.getPath(), bs);
      ed.setId(bs);
      paths.add(t);
      if (ed.hasContentReference()) {
        String s = ed.getContentReference().substring(1);
        if (idMap.containsKey(s))
          ed.setContentReference("#"+idMap.get(s));
        
      }
    }  
    // second path - fix up any broken path based id references
    
  }


//  private String describeExtension(ElementDefinition ed) {
//    if (!ed.hasType() || !ed.getTypeFirstRep().hasProfile())
//      return "";
//    return "$"+urlTail(ed.getTypeFirstRep().getProfile());
//  }
//

  private String urlTail(String profile) {
    return profile.contains("/") ? profile.substring(profile.lastIndexOf("/")+1) : profile;
  }


  private String checkName(String name) {
//    if (name.contains("."))
////      throw new Exception("Illegal name "+name+": no '.'");
//    if (name.contains(" "))
//      throw new Exception("Illegal name "+name+": no spaces");
    StringBuilder b = new StringBuilder();
    for (char c : name.toCharArray()) {
      if (!Utilities.existsInList(c, '.', ' ', ':', '"', '\'', '(', ')', '&', '[', ']'))
        b.append(c);
    }
    return b.toString().toLowerCase();
  }


  private int charCount(String path, char t) {
    int res = 0;
    for (char ch : path.toCharArray()) {
      if (ch == t)
        res++;
    }
    return res;
  }

//
//private void generateForChild(TextStreamWriter txt,
//    StructureDefinition structure, ElementDefinition child) {
//  // TODO Auto-generated method stub
//
//}

  private interface ExampleValueAccessor {
    Type getExampleValue(ElementDefinition ed);
    String getId();
  }

  private class BaseExampleValueAccessor implements ExampleValueAccessor {
    @Override
    public Type getExampleValue(ElementDefinition ed) {
      if (ed.hasFixed())
        return ed.getFixed();
      if (ed.hasExample())
        return ed.getExample().get(0).getValue();
      else
        return null;
    }

    @Override
    public String getId() {
      return "-genexample";
    }
  }
  
  private class ExtendedExampleValueAccessor implements ExampleValueAccessor {
    private String index;

    public ExtendedExampleValueAccessor(String index) {
      this.index = index;
    }
    @Override
    public Type getExampleValue(ElementDefinition ed) {
      if (ed.hasFixed())
        return ed.getFixed();
      for (Extension ex : ed.getExtension()) {
       String ndx = ToolingExtensions.readStringExtension(ex, "index");
       Type value = ToolingExtensions.getExtension(ex, "exValue").getValue();
       if (index.equals(ndx) && value != null)
         return value;
      }
      return null;
    }
    @Override
    public String getId() {
      return "-genexample-"+index;
    }
  }
  
  public List<org.hl7.fhir.dstu3.elementmodel.Element> generateExamples(StructureDefinition sd, boolean evenWhenNoExamples) throws FHIRException {
    List<org.hl7.fhir.dstu3.elementmodel.Element> examples = new ArrayList<org.hl7.fhir.dstu3.elementmodel.Element>();
    if (sd.hasSnapshot()) {
      if (evenWhenNoExamples || hasAnyExampleValues(sd)) 
        examples.add(generateExample(sd, new BaseExampleValueAccessor()));
      for (int i = 1; i <= 50; i++) {
        if (hasAnyExampleValues(sd, Integer.toString(i))) 
          examples.add(generateExample(sd, new ExtendedExampleValueAccessor(Integer.toString(i))));
      }
    }
    return examples;
  }

  private org.hl7.fhir.dstu3.elementmodel.Element generateExample(StructureDefinition profile, ExampleValueAccessor accessor) throws FHIRException {
    ElementDefinition ed = profile.getSnapshot().getElementFirstRep();
    org.hl7.fhir.dstu3.elementmodel.Element r = new org.hl7.fhir.dstu3.elementmodel.Element(ed.getPath(), new Property(context, ed, profile));
    List<ElementDefinition> children = getChildMap(profile, ed);
    for (ElementDefinition child : children) {
      if (child.getPath().endsWith(".id")) {
        org.hl7.fhir.dstu3.elementmodel.Element id = new org.hl7.fhir.dstu3.elementmodel.Element("id", new Property(context, child, profile));
        id.setValue(profile.getId()+accessor.getId());
        r.getChildren().add(id);
      } else { 
        org.hl7.fhir.dstu3.elementmodel.Element e = createExampleElement(profile, child, accessor);
        if (e != null)
          r.getChildren().add(e);
      }
    }
    return r;
  }

  private org.hl7.fhir.dstu3.elementmodel.Element createExampleElement(StructureDefinition profile, ElementDefinition ed, ExampleValueAccessor accessor) throws FHIRException {
    Type v = accessor.getExampleValue(ed);
    if (v != null) {
      return new ObjectConverter(context).convert(new Property(context, ed, profile), v);
    } else {
      org.hl7.fhir.dstu3.elementmodel.Element res = new org.hl7.fhir.dstu3.elementmodel.Element(tail(ed.getPath()), new Property(context, ed, profile));
      boolean hasValue = false;
      List<ElementDefinition> children = getChildMap(profile, ed);
      for (ElementDefinition child : children) {
        if (!child.hasContentReference()) {
        org.hl7.fhir.dstu3.elementmodel.Element e = createExampleElement(profile, child, accessor);
        if (e != null) {
          hasValue = true;
          res.getChildren().add(e);
        }
      }
      }
      if (hasValue)
        return res;
      else
        return null;
    }
  }

  private boolean hasAnyExampleValues(StructureDefinition sd, String index) {
    for (ElementDefinition ed : sd.getSnapshot().getElement())
      for (Extension ex : ed.getExtension()) {
        String ndx = ToolingExtensions.readStringExtension(ex, "index");
        Extension exv = ToolingExtensions.getExtension(ex, "exValue");
        if (exv != null) {
          Type value = exv.getValue();
        if (index.equals(ndx) && value != null)
          return true;
        }
       }
    return false;
  }


  private boolean hasAnyExampleValues(StructureDefinition sd) {
    for (ElementDefinition ed : sd.getSnapshot().getElement())
      if (ed.hasExample())
        return true;
    return false;
  }


  public void populateLogicalSnapshot(StructureDefinition sd) throws FHIRException {
    sd.getSnapshot().getElement().add(sd.getDifferential().getElementFirstRep().copy());
    
    if (sd.hasBaseDefinition()) {
    StructureDefinition base = context.fetchResource(StructureDefinition.class, sd.getBaseDefinition());
    if (base == null)
      throw new FHIRException("Unable to find base definition for logical model: "+sd.getBaseDefinition()+" from "+sd.getUrl());
    copyElements(sd, base.getSnapshot().getElement());
    }
    copyElements(sd, sd.getDifferential().getElement());
  }


  private void copyElements(StructureDefinition sd, List<ElementDefinition> list) {
    for (ElementDefinition ed : list) {
      if (ed.getPath().contains(".")) {
        ElementDefinition n = ed.copy();
        n.setPath(sd.getSnapshot().getElementFirstRep().getPath()+"."+ed.getPath().substring(ed.getPath().indexOf(".")+1));
        sd.getSnapshot().addElement(n);
      }
    }
  }

    
  public void cleanUpDifferential(StructureDefinition sd) {
    if (sd.getDifferential().getElement().size() > 1)
      cleanUpDifferential(sd, 1);
  }
  
  private void cleanUpDifferential(StructureDefinition sd, int start) {
    int level = Utilities.charCount(sd.getDifferential().getElement().get(start).getPath(), '.');
    int c = start;
    int len = sd.getDifferential().getElement().size();
    HashSet<String> paths = new HashSet<String>();
    while (c < len && Utilities.charCount(sd.getDifferential().getElement().get(c).getPath(), '.') == level) {
      ElementDefinition ed = sd.getDifferential().getElement().get(c);
      if (!paths.contains(ed.getPath())) {
        paths.add(ed.getPath());
        int ic = c+1; 
        while (ic < len && Utilities.charCount(sd.getDifferential().getElement().get(ic).getPath(), '.') > level) 
          ic++;
        ElementDefinition slicer = null;
        List<ElementDefinition> slices = new ArrayList<ElementDefinition>();
        slices.add(ed);
        while (ic < len && Utilities.charCount(sd.getDifferential().getElement().get(ic).getPath(), '.') == level) {
          ElementDefinition edi = sd.getDifferential().getElement().get(ic);
          if (ed.getPath().equals(edi.getPath())) {
            if (slicer == null) {
              slicer = new ElementDefinition();
              slicer.setPath(edi.getPath());
              slicer.getSlicing().setRules(SlicingRules.OPEN);
              sd.getDifferential().getElement().add(c, slicer);
              c++;
              ic++;
            }
            slices.add(edi);
          }
          ic++;
          while (ic < len && Utilities.charCount(sd.getDifferential().getElement().get(ic).getPath(), '.') > level) 
            ic++;
        }
        // now we're at the end, we're going to figure out the slicing discriminator
        if (slicer != null)
          determineSlicing(slicer, slices);
      }
      c++;
      if (c < len && Utilities.charCount(sd.getDifferential().getElement().get(c).getPath(), '.') > level) {
        cleanUpDifferential(sd, c);
        c++;
        while (c < len && Utilities.charCount(sd.getDifferential().getElement().get(c).getPath(), '.') > level) 
          c++;
      }
  }
  }


  private void determineSlicing(ElementDefinition slicer, List<ElementDefinition> slices) {
    // first, name them
    int i = 0;
    for (ElementDefinition ed : slices) {
      if (ed.hasUserData("slice-name")) {
        ed.setSliceName(ed.getUserString("slice-name"));
      } else {
        i++;
        ed.setSliceName("slice-"+Integer.toString(i));
      }
    }
    // now, the hard bit, how are they differentiated? 
    // right now, we hard code this...
    if (slicer.getPath().endsWith(".extension") || slicer.getPath().endsWith(".modifierExtension"))
      slicer.getSlicing().addDiscriminator().setType(DiscriminatorType.VALUE).setPath("url");
    else if (slicer.getPath().equals("DiagnosticReport.result"))
      slicer.getSlicing().addDiscriminator().setType(DiscriminatorType.VALUE).setPath("reference.code");
    else if (slicer.getPath().equals("Observation.related"))
      slicer.getSlicing().addDiscriminator().setType(DiscriminatorType.VALUE).setPath("target.reference.code");
    else if (slicer.getPath().equals("Bundle.entry"))
      slicer.getSlicing().addDiscriminator().setType(DiscriminatorType.VALUE).setPath("resource.@profile");
    else  
      throw new Error("No slicing for "+slicer.getPath()); 
  }

  public class SpanEntry {
    private List<SpanEntry> children = new ArrayList<SpanEntry>();
    private boolean profile;
    private String id;
    private String name;
    private String resType;
    private String cardinality;
    private String description;
    private String profileLink;
    private String resLink;
    private String type;
    
    public String getName() {
      return name;
    }
    public void setName(String name) {
      this.name = name;
    }
    public String getResType() {
      return resType;
    }
    public void setResType(String resType) {
      this.resType = resType;
    }
    public String getCardinality() {
      return cardinality;
    }
    public void setCardinality(String cardinality) {
      this.cardinality = cardinality;
    }
    public String getDescription() {
      return description;
    }
    public void setDescription(String description) {
      this.description = description;
    }
    public String getProfileLink() {
      return profileLink;
    }
    public void setProfileLink(String profileLink) {
      this.profileLink = profileLink;
    }
    public String getResLink() {
      return resLink;
    }
    public void setResLink(String resLink) {
      this.resLink = resLink;
    }
    public String getId() {
      return id;
    }
    public void setId(String id) {
      this.id = id;
    }
    public boolean isProfile() {
      return profile;
    }
    public void setProfile(boolean profile) {
      this.profile = profile;
    }
    public List<SpanEntry> getChildren() {
      return children;
    }
    public String getType() {
      return type;
    }
    public void setType(String type) {
      this.type = type;
    }
    
  }


  private String getCardinality(ElementDefinition ed, List<ElementDefinition> list) {
    int min = ed.getMin();
    int max = !ed.hasMax() || ed.getMax().equals("*") ? Integer.MAX_VALUE : Integer.parseInt(ed.getMax());
    while (ed != null && ed.getPath().contains(".")) {
      ed = findParent(ed, list);
      if (ed.getMax().equals("0"))
        max = 0;
      else if (!ed.getMax().equals("1") && !ed.hasSlicing())
        max = Integer.MAX_VALUE;
      if (ed.getMin() == 0)
        min = 0;
    }
    return Integer.toString(min)+".."+(max == Integer.MAX_VALUE ? "*" : Integer.toString(max));
  }


  private ElementDefinition findParent(ElementDefinition ed, List<ElementDefinition> list) {
    int i = list.indexOf(ed)-1;
    while (i >= 0 && !ed.getPath().startsWith(list.get(i).getPath()+"."))
      i--;
    if (i == -1)
      return null;
    else
      return list.get(i);
  }


  private List<String> listReferenceProfiles(ElementDefinition ed) {
    List<String> res = new ArrayList<String>();
    for (TypeRefComponent tr : ed.getType()) {
      // code is null if we're dealing with "value" and profile is null if we just have Reference()
      if (tr.getCode()!= null && "Reference".equals(tr.getCode()) && tr.getTargetProfile() != null)
        res.add(tr.getTargetProfile());
    }
    return res ;
  }


  private String nameForElement(ElementDefinition ed) {
    return ed.getPath().substring(ed.getPath().indexOf(".")+1);
  }


  private SpanEntry buildSpanEntryFromProfile(String name, String cardinality, StructureDefinition profile) throws IOException {
    SpanEntry res = new SpanEntry();
    res.setName(name);
    res.setCardinality(cardinality);
    res.setProfileLink(profile.getUserString("path"));
    res.setResType(profile.getType());
    StructureDefinition base = context.fetchResource(StructureDefinition.class, res.getResType());
    if (base != null)
      res.setResLink(base.getUserString("path"));
    res.setId(profile.getId());
    res.setProfile(profile.getDerivation() == TypeDerivationRule.CONSTRAINT);
    StringBuilder b = new StringBuilder();
    b.append(res.getResType());
    boolean first = true;
    boolean open = false;
    if (profile.getDerivation() == TypeDerivationRule.CONSTRAINT) {
      res.setDescription(profile.getName());
      for (ElementDefinition ed : profile.getSnapshot().getElement()) {
        if (isKeyProperty(ed.getBase().getPath()) && ed.hasFixed()) {
          if (first) {
            open = true;
            first = false;
            b.append("[");
          } else {
            b.append(", ");
          }
          b.append(tail(ed.getBase().getPath()));
          b.append("=");
          b.append(summarise(ed.getFixed()));
        }
      }
      if (open)
        b.append("]");
    } else
      res.setDescription("Base FHIR "+profile.getName());
    res.setType(b.toString());
    return res ;
  }


  private String summarise(Type value) throws IOException {
    if (value instanceof Coding)
      return summariseCoding((Coding) value);
    else if (value instanceof CodeableConcept)
      return summariseCodeableConcept((CodeableConcept) value);
    else
      return buildJson(value);
  }


  private String summariseCoding(Coding value) {
    String uri = value.getSystem();
    String system = NarrativeGenerator.describeSystem(uri);
    if (Utilities.isURL(system)) {
      if (system.equals("http://cap.org/protocols"))
        system = "CAP Code";
    }
    return system+" "+value.getCode();
  }


  private String summariseCodeableConcept(CodeableConcept value) {
    if (value.hasCoding())
      return summariseCoding(value.getCodingFirstRep());
    else
      return value.getText();
  }


  private boolean isKeyProperty(String path) {
    return Utilities.existsInList(path, "Observation.code");
  }


  public TableModel initSpanningTable(HierarchicalTableGenerator gen, String prefix, boolean isLogical) {
    TableModel model = gen.new TableModel();
    
    model.setDocoImg(prefix+"help16.png");
    model.setDocoRef(prefix+"formats.html#table"); // todo: change to graph definition
    model.getTitles().add(gen.new Title(null, model.getDocoRef(), "Property", "A profiled resource", null, 0));
    model.getTitles().add(gen.new Title(null, model.getDocoRef(), "Card.", "Minimum and Maximum # of times the the element can appear in the instance", null, 0));
    model.getTitles().add(gen.new Title(null, model.getDocoRef(), "Content", "What goes here", null, 0));
    model.getTitles().add(gen.new Title(null, model.getDocoRef(), "Description", "Description of the profile", null, 0));
    return model;
  }

  private void genSpanEntry(HierarchicalTableGenerator gen, List<Row> rows, SpanEntry span) throws IOException {
    Row row = gen.new Row();
    rows.add(row);
    row.setAnchor(span.getId());
    //row.setColor(..?);
    if (span.isProfile()) 
      row.setIcon("icon_profile.png", HierarchicalTableGenerator.TEXT_ICON_PROFILE);
    else
      row.setIcon("icon_resource.png", HierarchicalTableGenerator.TEXT_ICON_RESOURCE);
    
    row.getCells().add(gen.new Cell(null, null, span.getName(), null, null));
    row.getCells().add(gen.new Cell(null, null, span.getCardinality(), null, null));
    row.getCells().add(gen.new Cell(null, span.getProfileLink(), span.getType(), null, null));
    row.getCells().add(gen.new Cell(null, null, span.getDescription(), null, null));

    for (SpanEntry child : span.getChildren())
      genSpanEntry(gen, row.getSubRows(), child);
  }


  public static ElementDefinitionSlicingDiscriminatorComponent interpretR2Discriminator(String discriminator) {
    if (discriminator.endsWith("@profile"))
      return makeDiscriminator(DiscriminatorType.PROFILE, discriminator.length() == 8 ? "" : discriminator.substring(discriminator.length()-9)); 
    if (discriminator.endsWith("@type")) 
      return makeDiscriminator(DiscriminatorType.TYPE, discriminator.length() == 5 ? "" : discriminator.substring(discriminator.length()-6)); 
    return new ElementDefinitionSlicingDiscriminatorComponent().setType(DiscriminatorType.VALUE).setPath(discriminator);
  }


  private static ElementDefinitionSlicingDiscriminatorComponent makeDiscriminator(DiscriminatorType profile, String str) {
    return new ElementDefinitionSlicingDiscriminatorComponent().setType(DiscriminatorType.VALUE).setPath(Utilities.noString(str)? "$this" : str);
  }


  public static String buildR2Discriminator(ElementDefinitionSlicingDiscriminatorComponent t) throws FHIRException {
    switch (t.getType()) {
    case PROFILE: return t.getPath()+"/@profile";
    case TYPE: return t.getPath()+"/@type";
    case VALUE: return t.getPath();
    default: throw new FHIRException("Unable to represent "+t.getType().toCode()+":"+t.getPath()+" in R2");    
    }
  }






}
