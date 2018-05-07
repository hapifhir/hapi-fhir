package org.hl7.fhir.r4.conformance;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hl7.fhir.r4.context.IWorkerContext;
import org.hl7.fhir.r4.formats.IParser;
import org.hl7.fhir.r4.model.Base;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.ElementDefinition;
import org.hl7.fhir.r4.model.ElementDefinition.ElementDefinitionBindingComponent;
import org.hl7.fhir.r4.model.ElementDefinition.ElementDefinitionConstraintComponent;
import org.hl7.fhir.r4.model.ElementDefinition.ElementDefinitionMappingComponent;
import org.hl7.fhir.r4.model.ElementDefinition.TypeRefComponent;
import org.hl7.fhir.r4.model.Enumerations.BindingStrength;
import org.hl7.fhir.r4.model.Enumerations.PublicationStatus;
import org.hl7.fhir.r4.model.IntegerType;
import org.hl7.fhir.r4.model.PrimitiveType;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.StructureDefinition;
import org.hl7.fhir.r4.model.StructureDefinition.TypeDerivationRule;
import org.hl7.fhir.r4.model.Type;
import org.hl7.fhir.r4.model.UriType;
import org.hl7.fhir.r4.model.ValueSet;
import org.hl7.fhir.r4.model.ValueSet.ConceptReferenceComponent;
import org.hl7.fhir.r4.model.ValueSet.ConceptSetComponent;
import org.hl7.fhir.r4.model.ValueSet.ValueSetExpansionContainsComponent;
import org.hl7.fhir.r4.terminologies.ValueSetExpander.ValueSetExpansionOutcome;
import org.hl7.fhir.r4.utils.DefinitionNavigator;
import org.hl7.fhir.r4.utils.ToolingExtensions;
import org.hl7.fhir.exceptions.DefinitionException;
import org.hl7.fhir.exceptions.FHIRFormatError;
import org.hl7.fhir.utilities.CommaSeparatedStringBuilder;
import org.hl7.fhir.utilities.Utilities;
import org.hl7.fhir.utilities.validation.ValidationMessage;
import org.hl7.fhir.utilities.validation.ValidationMessage.Source;

/**
 * A engine that generates difference analysis between two sets of structure 
 * definitions, typically from 2 different implementation guides. 
 * 
 * How this class works is that you create it with access to a bunch of underying
 * resources that includes all the structure definitions from both implementation 
 * guides 
 * 
 * Once the class is created, you repeatedly pass pairs of structure definitions,
 * one from each IG, building up a web of difference analyses. This class will
 * automatically process any internal comparisons that it encounters
 * 
 * When all the comparisons have been performed, you can then generate a variety
 * of output formats
 * 
 * @author Grahame Grieve
 *
 */
public class ProfileComparer {

  private IWorkerContext context;
  
  public ProfileComparer(IWorkerContext context) {
    super();
    this.context = context;
  }

  private static final int BOTH_NULL = 0;
  private static final int EITHER_NULL = 1;

  public class ProfileComparison {
    private String id;
    /**
     * the first of two structures that were compared to generate this comparison
     * 
     *   In a few cases - selection of example content and value sets - left gets 
     *   preference over right
     */
    private StructureDefinition left;

    /**
     * the second of two structures that were compared to generate this comparison
     * 
     *   In a few cases - selection of example content and value sets - left gets 
     *   preference over right
     */
    private StructureDefinition right;

    
    public String getId() {
      return id;
    }
    private String leftName() {
      return left.getName();
    }
    private String rightName() {
      return right.getName();
    }

    /**
     * messages generated during the comparison. There are 4 grades of messages:
     *   information - a list of differences between structures
     *   warnings - notifies that the comparer is unable to fully compare the structures (constraints differ, open value sets)
     *   errors - where the structures are incompatible
     *   fatal errors - some error that prevented full analysis 
     * 
     * @return
     */
    private List<ValidationMessage> messages = new ArrayList<ValidationMessage>();

    /**
     * The structure that describes all instances that will conform to both structures 
     */
    private StructureDefinition subset;

    /**
     * The structure that describes all instances that will conform to either structures 
     */
    private StructureDefinition superset;

    public StructureDefinition getLeft() {
      return left;
    }

    public StructureDefinition getRight() {
      return right;
    }

    public List<ValidationMessage> getMessages() {
      return messages;
    }

    public StructureDefinition getSubset() {
      return subset;
    }

    public StructureDefinition getSuperset() {
      return superset;
    }
    
    private boolean ruleEqual(String path, ElementDefinition ed, String vLeft, String vRight, String description, boolean nullOK) {
      if (vLeft == null && vRight == null && nullOK)
        return true;
      if (vLeft == null && vRight == null) {
        messages.add(new ValidationMessage(Source.ProfileComparer, ValidationMessage.IssueType.STRUCTURE, path, description+" and not null (null/null)", ValidationMessage.IssueSeverity.ERROR));
        if (ed != null)
          status(ed, ProfileUtilities.STATUS_ERROR);
      }
      if (vLeft == null || !vLeft.equals(vRight)) {
        messages.add(new ValidationMessage(Source.ProfileComparer, ValidationMessage.IssueType.STRUCTURE, path, description+" ("+vLeft+"/"+vRight+")", ValidationMessage.IssueSeverity.ERROR));
        if (ed != null)
          status(ed, ProfileUtilities.STATUS_ERROR);
      }
      return true;
    }
    
    private boolean ruleCompares(ElementDefinition ed, Type vLeft, Type vRight, String path, int nullStatus) throws IOException {
      if (vLeft == null && vRight == null && nullStatus == BOTH_NULL)
        return true;
      if (vLeft == null && vRight == null) {
        messages.add(new ValidationMessage(Source.ProfileComparer, ValidationMessage.IssueType.STRUCTURE, path, "Must be the same and not null (null/null)", ValidationMessage.IssueSeverity.ERROR));
        status(ed, ProfileUtilities.STATUS_ERROR);
      }
      if (vLeft == null && nullStatus == EITHER_NULL)
        return true;
      if (vRight == null && nullStatus == EITHER_NULL)
        return true;
      if (vLeft == null || vRight == null || !Base.compareDeep(vLeft, vRight, false)) {
        messages.add(new ValidationMessage(Source.ProfileComparer, ValidationMessage.IssueType.STRUCTURE, path, "Must be the same ("+toString(vLeft)+"/"+toString(vRight)+")", ValidationMessage.IssueSeverity.ERROR));
        status(ed, ProfileUtilities.STATUS_ERROR);
      }
      return true;
    }

    private boolean rule(ElementDefinition ed, boolean test, String path, String message) {
      if (!test)  {
        messages.add(new ValidationMessage(Source.ProfileComparer, ValidationMessage.IssueType.STRUCTURE, path, message, ValidationMessage.IssueSeverity.ERROR));
        status(ed, ProfileUtilities.STATUS_ERROR);
      }
      return test;
    }

    private boolean ruleEqual(ElementDefinition ed, boolean vLeft, boolean vRight, String path, String elementName) {
      if (vLeft != vRight) {
        messages.add(new ValidationMessage(Source.ProfileComparer, ValidationMessage.IssueType.STRUCTURE, path, elementName+" must be the same ("+vLeft+"/"+vRight+")", ValidationMessage.IssueSeverity.ERROR));
        status(ed, ProfileUtilities.STATUS_ERROR);
      }
      return true;
    }

    private String toString(Type val) throws IOException {
      if (val instanceof PrimitiveType) 
        return "\"" + ((PrimitiveType) val).getValueAsString()+"\"";
      
      IParser jp = context.newJsonParser();
      return jp.composeString(val, "value");
    }
    
    public String getErrorCount() {
      int c = 0;
      for (ValidationMessage vm : messages)
        if (vm.getLevel() == ValidationMessage.IssueSeverity.ERROR)
          c++;
      return Integer.toString(c);
    }

    public String getWarningCount() {
      int c = 0;
      for (ValidationMessage vm : messages)
        if (vm.getLevel() == ValidationMessage.IssueSeverity.WARNING)
          c++;
      return Integer.toString(c);
    }
    
    public String getHintCount() {
      int c = 0;
      for (ValidationMessage vm : messages)
        if (vm.getLevel() == ValidationMessage.IssueSeverity.INFORMATION)
          c++;
      return Integer.toString(c);
    }
  }
  
  /**
   * Value sets used in the subset and superset
   */
  private List<ValueSet> valuesets = new ArrayList<ValueSet>();
  private List<ProfileComparison> comparisons = new ArrayList<ProfileComparison>();
  private String id; 
  private String title;
  private String leftLink;
  private String leftName;
  private String rightLink;
  private String rightName;
  
  
  public List<ValueSet> getValuesets() {
    return valuesets;
  }

  public void status(ElementDefinition ed, int value) {
    ed.setUserData(ProfileUtilities.UD_ERROR_STATUS, Math.max(value, ed.getUserInt("error-status")));
  }

  public List<ProfileComparison> getComparisons() {
    return comparisons;
  }

  /**
   * Compare left and right structure definitions to see whether they are consistent or not
   * 
   * Note that left and right are arbitrary choices. In one respect, left 
   * is 'preferred' - the left's example value and data sets will be selected 
   * over the right ones in the common structure definition
   * @throws DefinitionException 
   * @throws IOException 
   * @throws FHIRFormatError 
   *  
   * @
   */
  public ProfileComparison compareProfiles(StructureDefinition left, StructureDefinition right) throws DefinitionException, IOException, FHIRFormatError {
    ProfileComparison outcome = new ProfileComparison();
    outcome.left = left;
    outcome.right = right;
    
    if (left == null)
      throw new DefinitionException("No StructureDefinition provided (left)");
    if (right == null)
      throw new DefinitionException("No StructureDefinition provided (right)");
    if (!left.hasSnapshot())
      throw new DefinitionException("StructureDefinition has no snapshot (left: "+outcome.leftName()+")");
    if (!right.hasSnapshot())
      throw new DefinitionException("StructureDefinition has no snapshot (right: "+outcome.rightName()+")");
    if (left.getSnapshot().getElement().isEmpty())
      throw new DefinitionException("StructureDefinition snapshot is empty (left: "+outcome.leftName()+")");
    if (right.getSnapshot().getElement().isEmpty())
      throw new DefinitionException("StructureDefinition snapshot is empty (right: "+outcome.rightName()+")");

    for (ProfileComparison pc : comparisons) 
      if (pc.left.getUrl().equals(left.getUrl()) && pc.right.getUrl().equals(right.getUrl()))
        return pc;

    outcome.id = Integer.toString(comparisons.size()+1);
    comparisons.add(outcome);
    
    DefinitionNavigator ln = new DefinitionNavigator(context, left);
    DefinitionNavigator rn = new DefinitionNavigator(context, right);
    
    // from here on in, any issues go in messages
    outcome.superset = new StructureDefinition();
    outcome.subset = new StructureDefinition();
    if (outcome.ruleEqual(ln.path(), null,ln.path(), rn.path(), "Base Type is not compatible", false)) {
      if (compareElements(outcome, ln.path(), ln, rn)) {
        outcome.subset.setName("intersection of "+outcome.leftName()+" and "+outcome.rightName());
        outcome.subset.setStatus(PublicationStatus.DRAFT);
        outcome.subset.setKind(outcome.left.getKind());
        outcome.subset.setType(outcome.left.getType());
        outcome.subset.setBaseDefinition("http://hl7.org/fhir/StructureDefinition/"+outcome.subset.getType());
        outcome.subset.setDerivation(TypeDerivationRule.CONSTRAINT);
        outcome.subset.setAbstract(false);
        outcome.superset.setName("union of "+outcome.leftName()+" and "+outcome.rightName());
        outcome.superset.setStatus(PublicationStatus.DRAFT);
        outcome.superset.setKind(outcome.left.getKind());
        outcome.superset.setType(outcome.left.getType());
        outcome.superset.setBaseDefinition("http://hl7.org/fhir/StructureDefinition/"+outcome.subset.getType());
        outcome.superset.setAbstract(false);
        outcome.superset.setDerivation(TypeDerivationRule.CONSTRAINT);
      } else {
        outcome.subset = null;
        outcome.superset = null;
      }
    }
    return outcome;
  }

  /**
   * left and right refer to the same element. Are they compatible?   
   * @param outcome 
   * @param outcome
   * @param path
   * @param left
   * @param right
   * @- if there's a problem that needs fixing in this code
   * @throws DefinitionException 
   * @throws IOException 
   * @throws FHIRFormatError 
   */
  private boolean compareElements(ProfileComparison outcome, String path, DefinitionNavigator left, DefinitionNavigator right) throws DefinitionException, IOException, FHIRFormatError {
//    preconditions:
    assert(path != null);
    assert(left != null);
    assert(right != null);
    assert(left.path().equals(right.path()));
    
    // we ignore slicing right now - we're going to clone the root one anyway, and then think about clones 
    // simple stuff
    ElementDefinition subset = new ElementDefinition();
    subset.setPath(left.path());
    
    // not allowed to be different: 
    subset.getRepresentation().addAll(left.current().getRepresentation()); // can't be bothered even testing this one
    if (!outcome.ruleCompares(subset, left.current().getDefaultValue(), right.current().getDefaultValue(), path+".defaultValue[x]", BOTH_NULL))
      return false;
    subset.setDefaultValue(left.current().getDefaultValue());
    if (!outcome.ruleEqual(path, subset, left.current().getMeaningWhenMissing(), right.current().getMeaningWhenMissing(), "meaningWhenMissing Must be the same", true))
      return false;
    subset.setMeaningWhenMissing(left.current().getMeaningWhenMissing());
    if (!outcome.ruleEqual(subset, left.current().getIsModifier(), right.current().getIsModifier(), path, "isModifier"))
      return false;
    subset.setIsModifier(left.current().getIsModifier());
    if (!outcome.ruleEqual(subset, left.current().getIsSummary(), right.current().getIsSummary(), path, "isSummary"))
      return false;
    subset.setIsSummary(left.current().getIsSummary());
    
    // descriptive properties from ElementDefinition - merge them:
    subset.setLabel(mergeText(subset, outcome, path, "label", left.current().getLabel(), right.current().getLabel()));
    subset.setShort(mergeText(subset, outcome, path, "short", left.current().getShort(), right.current().getShort()));
    subset.setDefinition(mergeText(subset, outcome, path, "definition", left.current().getDefinition(), right.current().getDefinition()));
    subset.setComment(mergeText(subset, outcome, path, "comments", left.current().getComment(), right.current().getComment()));
    subset.setRequirements(mergeText(subset, outcome, path, "requirements", left.current().getRequirements(), right.current().getRequirements()));
    subset.getCode().addAll(mergeCodings(left.current().getCode(), right.current().getCode()));
    subset.getAlias().addAll(mergeStrings(left.current().getAlias(), right.current().getAlias()));
    subset.getMapping().addAll(mergeMappings(left.current().getMapping(), right.current().getMapping()));
    // left will win for example
    subset.setExample(left.current().hasExample() ? left.current().getExample() : right.current().getExample());

    subset.setMustSupport(left.current().getMustSupport() || right.current().getMustSupport());
    ElementDefinition superset = subset.copy();


    // compare and intersect
    superset.setMin(unionMin(left.current().getMin(), right.current().getMin()));
    superset.setMax(unionMax(left.current().getMax(), right.current().getMax()));
    subset.setMin(intersectMin(left.current().getMin(), right.current().getMin()));
    subset.setMax(intersectMax(left.current().getMax(), right.current().getMax()));
    outcome.rule(subset, subset.getMax().equals("*") || Integer.parseInt(subset.getMax()) >= subset.getMin(), path, "Cardinality Mismatch: "+card(left)+"/"+card(right));
    
    superset.getType().addAll(unionTypes(path, left.current().getType(), right.current().getType()));
    subset.getType().addAll(intersectTypes(subset, outcome, path, left.current().getType(), right.current().getType()));
    outcome.rule(subset, !subset.getType().isEmpty() || (!left.current().hasType() && !right.current().hasType()), path, "Type Mismatch:\r\n  "+typeCode(left)+"\r\n  "+typeCode(right));
//    <fixed[x]><!-- ?? 0..1 * Value must be exactly this --></fixed[x]>
//    <pattern[x]><!-- ?? 0..1 * Value must have at least these property values --></pattern[x]>
    superset.setMaxLengthElement(unionMaxLength(left.current().getMaxLength(), right.current().getMaxLength()));
    subset.setMaxLengthElement(intersectMaxLength(left.current().getMaxLength(), right.current().getMaxLength()));
    if (left.current().hasBinding() || right.current().hasBinding()) {
      compareBindings(outcome, subset, superset, path, left.current(), right.current());
    }

    // note these are backwards
    superset.getConstraint().addAll(intersectConstraints(path, left.current().getConstraint(), right.current().getConstraint()));
    subset.getConstraint().addAll(unionConstraints(subset, outcome, path, left.current().getConstraint(), right.current().getConstraint()));

    // now process the slices
    if (left.current().hasSlicing() || right.current().hasSlicing()) {
      if (isExtension(left.path()))
        return compareExtensions(outcome, path, superset, subset, left, right);
//      return true;
      else
        throw new DefinitionException("Slicing is not handled yet");
    // todo: name 
    }

    // add the children
    outcome.subset.getSnapshot().getElement().add(subset);
    outcome.superset.getSnapshot().getElement().add(superset);
    return compareChildren(subset, outcome, path, left, right);
  }

  private class ExtensionUsage {
    private DefinitionNavigator defn;
    private int minSuperset;
    private int minSubset;
    private String maxSuperset;
    private String maxSubset;
    private boolean both = false;
    
    public ExtensionUsage(DefinitionNavigator defn, int min, String max) {
      super();
      this.defn = defn;
      this.minSubset = min;
      this.minSuperset = min;
      this.maxSubset = max;
      this.maxSuperset = max;
    }
    
  }
  private boolean compareExtensions(ProfileComparison outcome, String path, ElementDefinition superset, ElementDefinition subset, DefinitionNavigator left, DefinitionNavigator right) throws DefinitionException {
    // for now, we don't handle sealed (or ordered) extensions
    
    // for an extension the superset is all extensions, and the subset is.. all extensions - well, unless thay are sealed. 
    // but it's not useful to report that. instead, we collate the defined ones, and just adjust the cardinalities
    Map<String, ExtensionUsage> map = new HashMap<String, ExtensionUsage>();
    
    if (left.slices() != null)
      for (DefinitionNavigator ex : left.slices()) {
        String url = ex.current().getType().get(0).getProfile().get(0).getValue();
        if (map.containsKey(url))
          throw new DefinitionException("Duplicate Extension "+url+" at "+path);
        else
          map.put(url, new ExtensionUsage(ex, ex.current().getMin(), ex.current().getMax()));
      }
    if (right.slices() != null)
      for (DefinitionNavigator ex : right.slices()) {
        String url = ex.current().getType().get(0).getProfile().get(0).getValue();
        if (map.containsKey(url)) {
          ExtensionUsage exd = map.get(url);
          exd.minSuperset = unionMin(exd.defn.current().getMin(), ex.current().getMin());
          exd.maxSuperset = unionMax(exd.defn.current().getMax(), ex.current().getMax());
          exd.minSubset = intersectMin(exd.defn.current().getMin(), ex.current().getMin());
          exd.maxSubset = intersectMax(exd.defn.current().getMax(), ex.current().getMax());
          exd.both = true;
          outcome.rule(subset, exd.maxSubset.equals("*") || Integer.parseInt(exd.maxSubset) >= exd.minSubset, path, "Cardinality Mismatch on extension: "+card(exd.defn)+"/"+card(ex));
        } else {
          map.put(url, new ExtensionUsage(ex, ex.current().getMin(), ex.current().getMax()));
        }
      }
    List<String> names = new ArrayList<String>();
    names.addAll(map.keySet());
    Collections.sort(names);
    for (String name : names) {
      ExtensionUsage exd = map.get(name);
      if (exd.both)
        outcome.subset.getSnapshot().getElement().add(exd.defn.current().copy().setMin(exd.minSubset).setMax(exd.maxSubset));
      outcome.superset.getSnapshot().getElement().add(exd.defn.current().copy().setMin(exd.minSuperset).setMax(exd.maxSuperset));
    }    
    return true;
  }

  private boolean isExtension(String path) {
    return path.endsWith(".extension") || path.endsWith(".modifierExtension");
  }

  private boolean compareChildren(ElementDefinition ed, ProfileComparison outcome, String path, DefinitionNavigator left, DefinitionNavigator right) throws DefinitionException, IOException, FHIRFormatError {
    List<DefinitionNavigator> lc = left.children();
    List<DefinitionNavigator> rc = right.children();
    // it's possible that one of these profiles walks into a data type and the other doesn't
    // if it does, we have to load the children for that data into the profile that doesn't 
    // walk into it
    if (lc.isEmpty() && !rc.isEmpty() && right.current().getType().size() == 1 && left.hasTypeChildren(right.current().getType().get(0)))
      lc = left.childrenFromType(right.current().getType().get(0));
    if (rc.isEmpty() && !lc.isEmpty() && left.current().getType().size() == 1 && right.hasTypeChildren(left.current().getType().get(0)))
      rc = right.childrenFromType(left.current().getType().get(0));
    if (lc.size() != rc.size()) {
      outcome.messages.add(new ValidationMessage(Source.ProfileComparer, ValidationMessage.IssueType.STRUCTURE, path, "Different number of children at "+path+" ("+Integer.toString(lc.size())+"/"+Integer.toString(rc.size())+")", ValidationMessage.IssueSeverity.ERROR));
      status(ed, ProfileUtilities.STATUS_ERROR);
      return false;      
    } else {
      for (int i = 0; i < lc.size(); i++) {
        DefinitionNavigator l = lc.get(i);
        DefinitionNavigator r = rc.get(i);
        String cpath = comparePaths(l.path(), r.path(), path, l.nameTail(), r.nameTail());
        if (cpath != null) {
          if (!compareElements(outcome, cpath, l, r))
            return false;
        } else {
          outcome.messages.add(new ValidationMessage(Source.ProfileComparer, ValidationMessage.IssueType.STRUCTURE, path, "Different path at "+path+"["+Integer.toString(i)+"] ("+l.path()+"/"+r.path()+")", ValidationMessage.IssueSeverity.ERROR));
          status(ed, ProfileUtilities.STATUS_ERROR);
          return false;
        }
      }
    }
    return true;
  }

  private String comparePaths(String path1, String path2, String path, String tail1, String tail2) {
    if (tail1.equals(tail2)) {
      return path+"."+tail1;
    } else if (tail1.endsWith("[x]") && tail2.startsWith(tail1.substring(0, tail1.length()-3))) {
      return path+"."+tail1;
    } else if (tail2.endsWith("[x]") && tail1.startsWith(tail2.substring(0, tail2.length()-3))) {
      return path+"."+tail2;
    } else 
      return null;
  }

  private boolean compareBindings(ProfileComparison outcome, ElementDefinition subset, ElementDefinition superset, String path, ElementDefinition lDef, ElementDefinition rDef) throws FHIRFormatError {
    assert(lDef.hasBinding() || rDef.hasBinding());
    if (!lDef.hasBinding()) {
      subset.setBinding(rDef.getBinding());
      // technically, the super set is unbound, but that's not very useful - so we use the provided on as an example
      superset.setBinding(rDef.getBinding().copy());
      superset.getBinding().setStrength(BindingStrength.EXAMPLE);
      return true;
    }
    if (!rDef.hasBinding()) {
      subset.setBinding(lDef.getBinding());
      superset.setBinding(lDef.getBinding().copy());
      superset.getBinding().setStrength(BindingStrength.EXAMPLE);
      return true;
    }
    ElementDefinitionBindingComponent left = lDef.getBinding();
    ElementDefinitionBindingComponent right = rDef.getBinding();
    if (Base.compareDeep(left, right, false)) {
      subset.setBinding(left);
      superset.setBinding(right);      
    }
    
    // if they're both examples/preferred then:
    // subset: left wins if they're both the same
    // superset: 
    if (isPreferredOrExample(left) && isPreferredOrExample(right)) {
      if (right.getStrength() == BindingStrength.PREFERRED && left.getStrength() == BindingStrength.EXAMPLE && !Base.compareDeep(left.getValueSet(), right.getValueSet(), false)) { 
        outcome.messages.add(new ValidationMessage(Source.ProfileComparer, ValidationMessage.IssueType.STRUCTURE, path, "Example/preferred bindings differ at "+path+" using binding from "+outcome.rightName(), ValidationMessage.IssueSeverity.INFORMATION));
        status(subset, ProfileUtilities.STATUS_HINT);
        subset.setBinding(right);
        superset.setBinding(unionBindings(superset, outcome, path, left, right));
      } else {
        if ((right.getStrength() != BindingStrength.EXAMPLE || left.getStrength() != BindingStrength.EXAMPLE) && !Base.compareDeep(left.getValueSet(), right.getValueSet(), false) ) { 
          outcome.messages.add(new ValidationMessage(Source.ProfileComparer, ValidationMessage.IssueType.STRUCTURE, path, "Example/preferred bindings differ at "+path+" using binding from "+outcome.leftName(), ValidationMessage.IssueSeverity.INFORMATION));
          status(subset, ProfileUtilities.STATUS_HINT);
        }
        subset.setBinding(left);
        superset.setBinding(unionBindings(superset, outcome, path, left, right));
      }
      return true;
    }
    // if either of them are extensible/required, then it wins
    if (isPreferredOrExample(left)) {
      subset.setBinding(right);
      superset.setBinding(unionBindings(superset, outcome, path, left, right));
      return true;
    }
    if (isPreferredOrExample(right)) {
      subset.setBinding(left);
      superset.setBinding(unionBindings(superset, outcome, path, left, right));
      return true;
    }
    
    // ok, both are extensible or required.
    ElementDefinitionBindingComponent subBinding = new ElementDefinitionBindingComponent();
    subset.setBinding(subBinding);
    ElementDefinitionBindingComponent superBinding = new ElementDefinitionBindingComponent();
    superset.setBinding(superBinding);
    subBinding.setDescription(mergeText(subset, outcome, path, "description", left.getDescription(), right.getDescription()));
    superBinding.setDescription(mergeText(subset, outcome, null, "description", left.getDescription(), right.getDescription()));
    if (left.getStrength() == BindingStrength.REQUIRED || right.getStrength() == BindingStrength.REQUIRED)
      subBinding.setStrength(BindingStrength.REQUIRED);
    else
      subBinding.setStrength(BindingStrength.EXTENSIBLE);
    if (left.getStrength() == BindingStrength.EXTENSIBLE || right.getStrength() == BindingStrength.EXTENSIBLE)
      superBinding.setStrength(BindingStrength.EXTENSIBLE);
    else
      superBinding.setStrength(BindingStrength.REQUIRED);
    
    if (Base.compareDeep(left.getValueSet(), right.getValueSet(), false)) {
      subBinding.setValueSet(left.getValueSet());
      superBinding.setValueSet(left.getValueSet());
      return true;
    } else if (!left.hasValueSet()) {
      outcome.messages.add(new ValidationMessage(Source.ProfileComparer, ValidationMessage.IssueType.STRUCTURE, path, "No left Value set at "+path, ValidationMessage.IssueSeverity.ERROR));
      return true;      
    } else if (!right.hasValueSet()) {
      outcome.messages.add(new ValidationMessage(Source.ProfileComparer, ValidationMessage.IssueType.STRUCTURE, path, "No right Value set at "+path, ValidationMessage.IssueSeverity.ERROR));
      return true;      
    } else {
      // ok, now we compare the value sets. This may be unresolvable. 
      ValueSet lvs = resolveVS(outcome.left, left.getValueSet());
      ValueSet rvs = resolveVS(outcome.right, right.getValueSet());
      if (lvs == null) {
        outcome.messages.add(new ValidationMessage(Source.ProfileComparer, ValidationMessage.IssueType.STRUCTURE, path, "Unable to resolve left value set "+left.getValueSet().toString()+" at "+path, ValidationMessage.IssueSeverity.ERROR));
        return true;
      } else if (rvs == null) {
        outcome.messages.add(new ValidationMessage(Source.ProfileComparer, ValidationMessage.IssueType.STRUCTURE, path, "Unable to resolve right value set "+right.getValueSet().toString()+" at "+path, ValidationMessage.IssueSeverity.ERROR));
        return true;        
      } else {
        // first, we'll try to do it by definition
        ValueSet cvs = intersectByDefinition(lvs, rvs);
        if(cvs == null) {
          // if that didn't work, we'll do it by expansion
          ValueSetExpansionOutcome le;
          ValueSetExpansionOutcome re;
          try {
            le = context.expandVS(lvs, true, false);
            re = context.expandVS(rvs, true, false);
            if (!closed(le.getValueset()) || !closed(re.getValueset())) 
              throw new DefinitionException("unclosed value sets are not handled yet");
            cvs = intersectByExpansion(lvs, rvs);
            if (!cvs.getCompose().hasInclude()) {
              outcome.messages.add(new ValidationMessage(Source.ProfileComparer, ValidationMessage.IssueType.STRUCTURE, path, "The value sets "+lvs.getUrl()+" and "+rvs.getUrl()+" do not intersect", ValidationMessage.IssueSeverity.ERROR));
              status(subset, ProfileUtilities.STATUS_ERROR);
              return false;
            }
          } catch (Exception e){
            outcome.messages.add(new ValidationMessage(Source.ProfileComparer, ValidationMessage.IssueType.STRUCTURE, path, "Unable to expand or process value sets "+lvs.getUrl()+" and "+rvs.getUrl()+": "+e.getMessage(), ValidationMessage.IssueSeverity.ERROR));
            status(subset, ProfileUtilities.STATUS_ERROR);
            return false;          
          }
        }
        subBinding.setValueSet(new Reference().setReference("#"+addValueSet(cvs)));
        superBinding.setValueSet(new Reference().setReference("#"+addValueSet(unite(superset, outcome, path, lvs, rvs))));
      }
    }
    return false;
  }

  private ElementDefinitionBindingComponent unionBindings(ElementDefinition ed, ProfileComparison outcome, String path, ElementDefinitionBindingComponent left, ElementDefinitionBindingComponent right) throws FHIRFormatError {
    ElementDefinitionBindingComponent union = new ElementDefinitionBindingComponent();
    if (left.getStrength().compareTo(right.getStrength()) < 0)
      union.setStrength(left.getStrength());
    else
      union.setStrength(right.getStrength());
    union.setDescription(mergeText(ed, outcome, path, "binding.description", left.getDescription(), right.getDescription()));
    if (Base.compareDeep(left.getValueSet(), right.getValueSet(), false))
      union.setValueSet(left.getValueSet());
    else {
      ValueSet lvs = resolveVS(outcome.left, left.getValueSet());
      ValueSet rvs = resolveVS(outcome.left, right.getValueSet());
      if (lvs != null && rvs != null)
        union.setValueSet(new Reference().setReference("#"+addValueSet(unite(ed, outcome, path, lvs, rvs))));
      else if (lvs != null)
        union.setValueSet(new Reference().setReference("#"+addValueSet(lvs)));
      else if (rvs != null)
        union.setValueSet(new Reference().setReference("#"+addValueSet(rvs)));
    }
    return union;
  }

  
  private ValueSet unite(ElementDefinition ed, ProfileComparison outcome, String path, ValueSet lvs, ValueSet rvs) {
    ValueSet vs = new ValueSet();
    if (lvs.hasCompose()) {
      for (ConceptSetComponent inc : lvs.getCompose().getInclude()) 
        vs.getCompose().getInclude().add(inc);
      if (lvs.getCompose().hasExclude()) {
        outcome.messages.add(new ValidationMessage(Source.ProfileComparer, ValidationMessage.IssueType.STRUCTURE, path, "The value sets "+lvs.getUrl()+" has exclude statements, and no union involving it can be correctly determined", ValidationMessage.IssueSeverity.ERROR));
        status(ed, ProfileUtilities.STATUS_ERROR);
      }
    }
    if (rvs.hasCompose()) {
      for (ConceptSetComponent inc : rvs.getCompose().getInclude())
        if (!mergeIntoExisting(vs.getCompose().getInclude(), inc))
          vs.getCompose().getInclude().add(inc);
      if (rvs.getCompose().hasExclude()) {
        outcome.messages.add(new ValidationMessage(Source.ProfileComparer, ValidationMessage.IssueType.STRUCTURE, path, "The value sets "+lvs.getUrl()+" has exclude statements, and no union involving it can be correctly determined", ValidationMessage.IssueSeverity.ERROR));
        status(ed, ProfileUtilities.STATUS_ERROR);
      }
    }    
    return vs;
  }

  private boolean mergeIntoExisting(List<ConceptSetComponent> include, ConceptSetComponent inc) {
    for (ConceptSetComponent dst : include) {
      if (Base.compareDeep(dst,  inc, false))
        return true; // they're actually the same
      if (dst.getSystem().equals(inc.getSystem())) {
        if (inc.hasFilter() || dst.hasFilter()) {
          return false; // just add the new one as a a parallel
        } else if (inc.hasConcept() && dst.hasConcept()) {
          for (ConceptReferenceComponent cc : inc.getConcept()) {
            boolean found = false;
            for (ConceptReferenceComponent dd : dst.getConcept()) {
              if (dd.getCode().equals(cc.getCode()))
                found = true;
              if (found) {
                if (cc.hasDisplay() && !dd.hasDisplay())
                  dd.setDisplay(cc.getDisplay());
                break;
              }
            }
            if (!found)
              dst.getConcept().add(cc.copy());
          }
        } else
          dst.getConcept().clear(); // one of them includes the entire code system 
      }
    }
    return false;
  }

  private ValueSet resolveVS(StructureDefinition ctxtLeft, Type vsRef) {
    if (vsRef == null)
      return null;
    if (vsRef instanceof UriType)
      return null;
    else {
      Reference ref = (Reference) vsRef;
      if (!ref.hasReference())
        return null;
      return context.fetchResource(ValueSet.class, ref.getReference());
    }
  }

  private ValueSet intersectByDefinition(ValueSet lvs, ValueSet rvs) {
    // this is just a stub. The idea is that we try to avoid expanding big open value sets from SCT, RxNorm, LOINC.
    // there's a bit of long hand logic coming here, but that's ok.
    return null;
  }

  private ValueSet intersectByExpansion(ValueSet lvs, ValueSet rvs) {
    // this is pretty straight forward - we intersect the lists, and build a compose out of the intersection
    ValueSet vs = new ValueSet();
    vs.setStatus(PublicationStatus.DRAFT);
    
    Map<String, ValueSetExpansionContainsComponent> left = new HashMap<String, ValueSetExpansionContainsComponent>();
    scan(lvs.getExpansion().getContains(), left);
    Map<String, ValueSetExpansionContainsComponent> right = new HashMap<String, ValueSetExpansionContainsComponent>();
    scan(rvs.getExpansion().getContains(), right);
    Map<String, ConceptSetComponent> inc = new HashMap<String, ConceptSetComponent>();
    
    for (String s : left.keySet()) {
      if (right.containsKey(s)) {
        ValueSetExpansionContainsComponent cc = left.get(s);
        ConceptSetComponent c = inc.get(cc.getSystem());
        if (c == null) {
          c = vs.getCompose().addInclude().setSystem(cc.getSystem());
          inc.put(cc.getSystem(), c);
        }
        c.addConcept().setCode(cc.getCode()).setDisplay(cc.getDisplay());
      }
    }
    return vs;
  }

  private void scan(List<ValueSetExpansionContainsComponent> list, Map<String, ValueSetExpansionContainsComponent> map) {
    for (ValueSetExpansionContainsComponent cc : list) {
      if (cc.hasSystem() && cc.hasCode()) {
        String s = cc.getSystem()+"::"+cc.getCode();
        if (!map.containsKey(s))
          map.put(s,  cc);
      }
      if (cc.hasContains())
        scan(cc.getContains(), map);
    }
  }

  private boolean closed(ValueSet vs) {
    return !ToolingExtensions.findBooleanExtension(vs.getExpansion(), ToolingExtensions.EXT_UNCLOSED);
  }

  private boolean isPreferredOrExample(ElementDefinitionBindingComponent binding) {
    return binding.getStrength() == BindingStrength.EXAMPLE || binding.getStrength() == BindingStrength.PREFERRED;
  }

  private Collection<? extends TypeRefComponent> intersectTypes(ElementDefinition ed, ProfileComparison outcome, String path, List<TypeRefComponent> left, List<TypeRefComponent> right) throws DefinitionException, IOException, FHIRFormatError {
    List<TypeRefComponent> result = new ArrayList<TypeRefComponent>();
    for (TypeRefComponent l : left) {
      if (l.hasAggregation())
        throw new DefinitionException("Aggregation not supported: "+path);
      boolean pfound = false;
      boolean tfound = false;
      TypeRefComponent c = l.copy();
      for (TypeRefComponent r : right) {
        if (r.hasAggregation())
          throw new DefinitionException("Aggregation not supported: "+path);
        if (!l.hasProfile() && !r.hasProfile()) {
          pfound = true;    
        } else if (!r.hasProfile()) {
          pfound = true; 
        } else if (!l.hasProfile()) {
          pfound = true;
          c.setProfile(r.getProfile());
        } else {
          StructureDefinition sdl = resolveProfile(ed, outcome, path, l.getProfile().get(0).getValue(), outcome.leftName());
          StructureDefinition sdr = resolveProfile(ed, outcome, path, r.getProfile().get(0).getValue(), outcome.rightName());
          if (sdl != null && sdr != null) {
            if (sdl == sdr) {
              pfound = true;
            } else if (derivesFrom(sdl, sdr)) {
              pfound = true;
            } else if (derivesFrom(sdr, sdl)) {
              c.setProfile(r.getProfile());
              pfound = true;
            } else if (sdl.getType().equals(sdr.getType())) {
              ProfileComparison comp = compareProfiles(sdl, sdr);
              if (comp.getSubset() != null) {
                pfound = true;
                c.addProfile("#"+comp.id);
              }
            }
          }
        }
        if (!l.hasTargetProfile() && !r.hasTargetProfile()) {
          tfound = true;    
        } else if (!r.hasTargetProfile()) {
          tfound = true; 
        } else if (!l.hasTargetProfile()) {
          tfound = true;
          c.setTargetProfile(r.getTargetProfile());
        } else {
          StructureDefinition sdl = resolveProfile(ed, outcome, path, l.getProfile().get(0).getValue(), outcome.leftName());
          StructureDefinition sdr = resolveProfile(ed, outcome, path, r.getProfile().get(0).getValue(), outcome.rightName());
          if (sdl != null && sdr != null) {
            if (sdl == sdr) {
              tfound = true;
            } else if (derivesFrom(sdl, sdr)) {
              tfound = true;
            } else if (derivesFrom(sdr, sdl)) {
              c.setTargetProfile(r.getTargetProfile());
              tfound = true;
            } else if (sdl.getType().equals(sdr.getType())) {
              ProfileComparison comp = compareProfiles(sdl, sdr);
              if (comp.getSubset() != null) {
                tfound = true;
                c.addTargetProfile("#"+comp.id);
              }
            }
          }
        }
      }
      if (pfound && tfound)
        result.add(c);
    }
    return result;
  }

  private StructureDefinition resolveProfile(ElementDefinition ed, ProfileComparison outcome, String path, String url, String name) {
    StructureDefinition res = context.fetchResource(StructureDefinition.class, url);
    if (res == null) {
      outcome.messages.add(new ValidationMessage(Source.ProfileComparer, ValidationMessage.IssueType.INFORMATIONAL, path, "Unable to resolve profile "+url+" in profile "+name, ValidationMessage.IssueSeverity.WARNING));
      status(ed, ProfileUtilities.STATUS_HINT);
    }
    return res;
  }

  private Collection<? extends TypeRefComponent> unionTypes(String path, List<TypeRefComponent> left, List<TypeRefComponent> right) throws DefinitionException, IOException, FHIRFormatError {
    List<TypeRefComponent> result = new ArrayList<TypeRefComponent>();
    for (TypeRefComponent l : left) 
      checkAddTypeUnion(path, result, l);
    for (TypeRefComponent r : right) 
      checkAddTypeUnion(path, result, r);
    return result;
  }    
    
  private void checkAddTypeUnion(String path, List<TypeRefComponent> results, TypeRefComponent nw) throws DefinitionException, IOException, FHIRFormatError {
    boolean pfound = false;
    boolean tfound = false;
    nw = nw.copy();
    if (nw.hasAggregation())
      throw new DefinitionException("Aggregation not supported: "+path);
    for (TypeRefComponent ex : results) {
      if (Utilities.equals(ex.getCode(), nw.getCode())) {
        if (!ex.hasProfile() && !nw.hasProfile())
          pfound = true;
        else if (!ex.hasProfile()) {
          pfound = true; 
        } else if (!nw.hasProfile()) {
          pfound = true;
          ex.setProfile(null);
        } else {
          // both have profiles. Is one derived from the other? 
          StructureDefinition sdex = context.fetchResource(StructureDefinition.class, ex.getProfile().get(0).getValue());
          StructureDefinition sdnw = context.fetchResource(StructureDefinition.class, nw.getProfile().get(0).getValue());
          if (sdex != null && sdnw != null) {
            if (sdex == sdnw) {
              pfound = true;
            } else if (derivesFrom(sdex, sdnw)) {
              ex.setProfile(nw.getProfile());
              pfound = true;
            } else if (derivesFrom(sdnw, sdex)) {
              pfound = true;
            } else if (sdnw.getSnapshot().getElement().get(0).getPath().equals(sdex.getSnapshot().getElement().get(0).getPath())) {
              ProfileComparison comp = compareProfiles(sdex, sdnw);
              if (comp.getSuperset() != null) {
                pfound = true;
                ex.addProfile("#"+comp.id);
              }
            }
          }
        }        
        if (!ex.hasTargetProfile() && !nw.hasTargetProfile())
          tfound = true;
        else if (!ex.hasTargetProfile()) {
          tfound = true; 
        } else if (!nw.hasTargetProfile()) {
          tfound = true;
          ex.setTargetProfile(null);
        } else {
          // both have profiles. Is one derived from the other? 
          StructureDefinition sdex = context.fetchResource(StructureDefinition.class, ex.getTargetProfile().get(0).getValue());
          StructureDefinition sdnw = context.fetchResource(StructureDefinition.class, nw.getTargetProfile().get(0).getValue());
          if (sdex != null && sdnw != null) {
            if (sdex == sdnw) {
              tfound = true;
            } else if (derivesFrom(sdex, sdnw)) {
              ex.setTargetProfile(nw.getTargetProfile());
              tfound = true;
            } else if (derivesFrom(sdnw, sdex)) {
              tfound = true;
            } else if (sdnw.getSnapshot().getElement().get(0).getPath().equals(sdex.getSnapshot().getElement().get(0).getPath())) {
              ProfileComparison comp = compareProfiles(sdex, sdnw);
              if (comp.getSuperset() != null) {
                tfound = true;
                ex.addTargetProfile("#"+comp.id);
              }
            }
          }
        }        
      }
    }
    if (!tfound || !pfound)
      results.add(nw);      
  }

  
  private boolean derivesFrom(StructureDefinition left, StructureDefinition right) {
    // left derives from right if it's base is the same as right
    // todo: recursive...
    return left.hasBaseDefinition() && left.getBaseDefinition().equals(right.getUrl());
  }


  private String mergeText(ElementDefinition ed, ProfileComparison outcome, String path, String name, String left, String right) {
    if (left == null && right == null)
      return null;
    if (left == null)
      return right;
    if (right == null)
      return left;
    if (left.equalsIgnoreCase(right))
      return left;
    if (path != null) {
      outcome.messages.add(new ValidationMessage(Source.ProfileComparer, ValidationMessage.IssueType.INFORMATIONAL, path, "Elements differ in definition for "+name+":\r\n  \""+left+"\"\r\n  \""+right+"\"", 
          "Elements differ in definition for "+name+":<br/>\""+Utilities.escapeXml(left)+"\"<br/>\""+Utilities.escapeXml(right)+"\"", ValidationMessage.IssueSeverity.INFORMATION));
      status(ed, ProfileUtilities.STATUS_HINT);
    }
    return "left: "+left+"; right: "+right;
  }

  private List<Coding> mergeCodings(List<Coding> left, List<Coding> right) {
    List<Coding> result = new ArrayList<Coding>();
    result.addAll(left);
    for (Coding c : right) {
      boolean found = false;
      for (Coding ct : left)
        if (Utilities.equals(c.getSystem(), ct.getSystem()) && Utilities.equals(c.getCode(), ct.getCode()))
          found = true;
      if (!found)
        result.add(c);
    }
    return result;
  }

  private List<StringType> mergeStrings(List<StringType> left, List<StringType> right) {
    List<StringType> result = new ArrayList<StringType>();
    result.addAll(left);
    for (StringType c : right) {
      boolean found = false;
      for (StringType ct : left)
        if (Utilities.equals(c.getValue(), ct.getValue()))
          found = true;
      if (!found)
        result.add(c);
    }
    return result;
  }

  private List<ElementDefinitionMappingComponent> mergeMappings(List<ElementDefinitionMappingComponent> left, List<ElementDefinitionMappingComponent> right) {
    List<ElementDefinitionMappingComponent> result = new ArrayList<ElementDefinitionMappingComponent>();
    result.addAll(left);
    for (ElementDefinitionMappingComponent c : right) {
      boolean found = false;
      for (ElementDefinitionMappingComponent ct : left)
        if (Utilities.equals(c.getIdentity(), ct.getIdentity()) && Utilities.equals(c.getLanguage(), ct.getLanguage()) && Utilities.equals(c.getMap(), ct.getMap()))
          found = true;
      if (!found)
        result.add(c);
    }
    return result;
  }

  // we can't really know about constraints. We create warnings, and collate them 
  private List<ElementDefinitionConstraintComponent> unionConstraints(ElementDefinition ed, ProfileComparison outcome, String path, List<ElementDefinitionConstraintComponent> left, List<ElementDefinitionConstraintComponent> right) {
    List<ElementDefinitionConstraintComponent> result = new ArrayList<ElementDefinitionConstraintComponent>();
    for (ElementDefinitionConstraintComponent l : left) {
      boolean found = false;
      for (ElementDefinitionConstraintComponent r : right)
        if (Utilities.equals(r.getId(), l.getId()) || (Utilities.equals(r.getXpath(), l.getXpath()) && r.getSeverity() == l.getSeverity()))
          found = true;
      if (!found) {
        outcome.messages.add(new ValidationMessage(Source.ProfileComparer, ValidationMessage.IssueType.STRUCTURE, path, "StructureDefinition "+outcome.leftName()+" has a constraint that is not found in "+outcome.rightName()+" and it is uncertain whether they are compatible ("+l.getXpath()+")", ValidationMessage.IssueSeverity.INFORMATION));
        status(ed, ProfileUtilities.STATUS_WARNING);
      }
      result.add(l);
    }
    for (ElementDefinitionConstraintComponent r : right) {
      boolean found = false;
      for (ElementDefinitionConstraintComponent l : left)
        if (Utilities.equals(r.getId(), l.getId()) || (Utilities.equals(r.getXpath(), l.getXpath()) && r.getSeverity() == l.getSeverity()))
          found = true;
      if (!found) {
        outcome.messages.add(new ValidationMessage(Source.ProfileComparer, ValidationMessage.IssueType.STRUCTURE, path, "StructureDefinition "+outcome.rightName()+" has a constraint that is not found in "+outcome.leftName()+" and it is uncertain whether they are compatible ("+r.getXpath()+")", ValidationMessage.IssueSeverity.INFORMATION));
        status(ed, ProfileUtilities.STATUS_WARNING);
        result.add(r);
      }
    }
    return result;
  }


  private List<ElementDefinitionConstraintComponent> intersectConstraints(String path, List<ElementDefinitionConstraintComponent> left, List<ElementDefinitionConstraintComponent> right) {
  List<ElementDefinitionConstraintComponent> result = new ArrayList<ElementDefinitionConstraintComponent>();
  for (ElementDefinitionConstraintComponent l : left) {
    boolean found = false;
    for (ElementDefinitionConstraintComponent r : right)
      if (Utilities.equals(r.getId(), l.getId()) || (Utilities.equals(r.getXpath(), l.getXpath()) && r.getSeverity() == l.getSeverity()))
        found = true;
    if (found)
      result.add(l);
  }
  return result;
}

  private String card(DefinitionNavigator defn) {
    return Integer.toString(defn.current().getMin())+".."+defn.current().getMax();
  }
  
  private String typeCode(DefinitionNavigator defn) {
    CommaSeparatedStringBuilder b = new CommaSeparatedStringBuilder();
    for (TypeRefComponent t : defn.current().getType())
      b.append(t.getCode()+(t.hasProfile() ? "("+t.getProfile()+")" : "")+(t.hasTargetProfile() ? "("+t.getTargetProfile()+")" : "")); // todo: other properties
    return b.toString();
  }

  private int intersectMin(int left, int right) {
    if (left > right)
      return left;
    else
      return right;
  }

  private int unionMin(int left, int right) {
    if (left > right)
      return right;
    else
      return left;
  }

  private String intersectMax(String left, String right) {
    int l = "*".equals(left) ? Integer.MAX_VALUE : Integer.parseInt(left);
    int r = "*".equals(right) ? Integer.MAX_VALUE : Integer.parseInt(right);
    if (l < r)
      return left;
    else
      return right;
  }

  private String unionMax(String left, String right) {
    int l = "*".equals(left) ? Integer.MAX_VALUE : Integer.parseInt(left);
    int r = "*".equals(right) ? Integer.MAX_VALUE : Integer.parseInt(right);
    if (l < r)
      return right;
    else
      return left;
  }

  private IntegerType intersectMaxLength(int left, int right) {
    if (left == 0) 
      left = Integer.MAX_VALUE;
    if (right == 0) 
      right = Integer.MAX_VALUE;
    if (left < right)
      return left == Integer.MAX_VALUE ? null : new IntegerType(left);
    else
      return right == Integer.MAX_VALUE ? null : new IntegerType(right);
  }

  private IntegerType unionMaxLength(int left, int right) {
    if (left == 0) 
      left = Integer.MAX_VALUE;
    if (right == 0) 
      right = Integer.MAX_VALUE;
    if (left < right)
      return right == Integer.MAX_VALUE ? null : new IntegerType(right);
    else
      return left == Integer.MAX_VALUE ? null : new IntegerType(left);
  }

  
  public String addValueSet(ValueSet cvs) {
    String id = Integer.toString(valuesets.size()+1);
    cvs.setId(id);
    valuesets.add(cvs);
    return id;
  }

  
  
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getLeftLink() {
    return leftLink;
  }

  public void setLeftLink(String leftLink) {
    this.leftLink = leftLink;
  }

  public String getLeftName() {
    return leftName;
  }

  public void setLeftName(String leftName) {
    this.leftName = leftName;
  }

  public String getRightLink() {
    return rightLink;
  }

  public void setRightLink(String rightLink) {
    this.rightLink = rightLink;
  }

  public String getRightName() {
    return rightName;
  }

  public void setRightName(String rightName) {
    this.rightName = rightName;
  }


  
  
}
