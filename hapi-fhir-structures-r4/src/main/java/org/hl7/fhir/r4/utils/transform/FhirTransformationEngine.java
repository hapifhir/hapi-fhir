package org.hl7.fhir.r4.utils.transform;

// remember group resolution
// trace - account for which wasn't transformed in the source

import org.hl7.fhir.exceptions.DefinitionException;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.r4.conformance.ProfileUtilities;
import org.hl7.fhir.r4.conformance.ProfileUtilities.ProfileKnowledgeProvider;
import org.hl7.fhir.r4.context.IWorkerContext;
import org.hl7.fhir.r4.elementmodel.Element;
import org.hl7.fhir.r4.model.*;
import org.hl7.fhir.r4.model.ConceptMap.ConceptMapGroupComponent;
import org.hl7.fhir.r4.model.ConceptMap.SourceElementComponent;
import org.hl7.fhir.r4.model.ConceptMap.TargetElementComponent;
import org.hl7.fhir.r4.model.ElementDefinition.ElementDefinitionMappingComponent;
import org.hl7.fhir.r4.model.Enumerations.ConceptMapEquivalence;
import org.hl7.fhir.r4.model.Enumerations.PublicationStatus;
import org.hl7.fhir.r4.model.Narrative.NarrativeStatus;
import org.hl7.fhir.r4.model.StructureDefinition.StructureDefinitionMappingComponent;
import org.hl7.fhir.r4.model.StructureMap.*;
import org.hl7.fhir.r4.utils.FHIRPathEngine;
import org.hl7.fhir.r4.utils.ToolingExtensions;
import org.hl7.fhir.r4.utils.transform.deserializer.DefaultStructureMapParser;
import org.hl7.fhir.r4.utils.transform.exception.InvalidMapConfigurationException;
import org.hl7.fhir.r4.utils.transform.serializer.StructureMapSerializer;
import org.hl7.fhir.utilities.xhtml.NodeType;
import org.hl7.fhir.utilities.xhtml.XhtmlNode;

import java.io.IOException;
import java.util.*;

/**
 * Services in this class:
 * <p>
 * string render(map) - take a structure and convert it to text
 * map parse(text) - take a text representation and parse it
 * getTargetType(map) - return the definition for the type to create to hand in
 * transform(appInfo, source, map, target) - transform from source to target following the map
 * analyse(appInfo, map) - generate profiles and other analysis artifacts for the targets of the transform
 * map generateMapFromMappings(StructureDefinition) - build a mapping from a structure definition with logical mappings
 *
 * @author Grahame Grieve, modified by Claude Nanjo
 */
@SuppressWarnings("JavaDoc")
public class FhirTransformationEngine extends BaseRunner {

  /**
   *
   */
  public static final String MAP_WHERE_CHECK = "map.where.check";
  /**
   *
   */
  public static final String MAP_WHERE_EXPRESSION = "map.where.expression";
  /**
   *
   */
  public static final String MAP_SEARCH_EXPRESSION = "map.search.expression";
  /**
   *
   */
  public static final String MAP_EXPRESSION = "map.transform.expression";

  /**
   *
   */
  private ProfileKnowledgeProvider pkp;

  /**
   * @param worker
   */
  @SuppressWarnings("WeakerAccess")
  public FhirTransformationEngine(IWorkerContext worker) {
    super();
    setWorker(worker);
    setFhirPathEngine(new FHIRPathEngine(worker));
    getFhirPathEngine().setHostServices(new FFHIRPathHostServices(this));
  }

  /**
   * @param worker
   * @param services
   */
  @SuppressWarnings("unused")
  public FhirTransformationEngine(IWorkerContext worker, ITransformerServices services) {
    this(worker);
    this.initializeLibrary();
    for (org.hl7.fhir.r4.model.MetadataResource bc : worker.allConformanceResources()) {
      if (bc instanceof StructureMap)
        getLibrary().put(bc.getUrl(), (StructureMap) bc);
    }
    setServices(services);
  }

  /**
   * @param worker
   * @param library
   */
  @SuppressWarnings("WeakerAccess")
  public FhirTransformationEngine(IWorkerContext worker, Map<String, StructureMap> library) {
    this(worker);
    setLibrary(library);
  }

  /**
   * @param worker
   * @param library
   * @param services
   */
  @SuppressWarnings("WeakerAccess")
  public FhirTransformationEngine(IWorkerContext worker, Map<String, StructureMap> library, ITransformerServices services) {
    this(worker, library);
    setServices(services);
  }

  /**
   * @param worker
   * @param library
   * @param services
   * @param pkp
   */
  @SuppressWarnings("WeakerAccess")
  public FhirTransformationEngine(IWorkerContext worker, Map<String, StructureMap> library, ITransformerServices services, ProfileKnowledgeProvider pkp) {
    this(worker, library, services);
    this.pkp = pkp;
  }

  /**
   * @param map
   * @return
   * @throws FHIRException
   */
  @SuppressWarnings("unused")
  public StructureDefinition getTargetType(StructureMap map) throws FHIRException {
    boolean found = false;
    StructureDefinition res = null;
    for (StructureMapStructureComponent uses : map.getStructure()) {
      if (uses.getMode() == StructureMapModelMode.TARGET) {
        if (found)
          throw new FHIRException("Multiple targets found in map " + map.getUrl());
        found = true;
        res = getWorker().fetchResource(StructureDefinition.class, uses.getUrl());
        if (res == null)
          throw new FHIRException("Unable to find " + uses.getUrl() + " referenced from map " + map.getUrl());
      }
    }
    if (res == null)
      throw new FHIRException("No targets found in map " + map.getUrl());
    return res;
  }

  /**
   * @param appInfo
   * @param source
   * @param map
   * @param target
   * @throws FHIRException
   */
  public void transform(Object appInfo, Base source, StructureMap map, Base target) throws FHIRException {
    TransformContext context = new TransformContext(appInfo);
    log("Start Transform " + map.getUrl());
    StructureMapGroupComponent ruleGroup = map.getGroup().get(0);

    Variables vars = new Variables();
    vars.add(VariableMode.INPUT, getInputName(ruleGroup, StructureMapInputMode.SOURCE, "source"), source);
    vars.add(VariableMode.OUTPUT, getInputName(ruleGroup, StructureMapInputMode.TARGET, "target"), target);
    StructureMapGroupHandler groupRunner = new StructureMapGroupHandler(map, this, ruleGroup);
    groupRunner.executeGroup("", context, vars);
    if (target instanceof Element)
      ((Element) target).sort();
  }

  /**
   * @param g
   * @param mode
   * @param def
   * @return
   * @throws DefinitionException
   */
  private String getInputName(StructureMapGroupComponent g, StructureMapInputMode mode, String def) throws DefinitionException {
    String name = null;
    for (StructureMapGroupInputComponent inp : g.getInput()) {
      if (inp.getMode() == mode)
        if (name != null)
          throw new DefinitionException("This engine does not support multiple source inputs");
        else
          name = inp.getName();
    }
    return name == null ? def : name;
  }

  /**
   * @param context
   * @param map
   * @param vars
   * @param parameter
   * @return
   * @throws FHIRException
   */
  protected Base translate(TransformContext context, StructureMap map, Variables vars, List<StructureMapGroupRuleTargetParameterComponent> parameter) throws FHIRException {
    Base src = getParam(vars, parameter.get(0));
    String id = getParamString(vars, parameter.get(1));
    String fld = parameter.size() > 2 ? getParamString(vars, parameter.get(2)) : null;
    return translate(context, map, src, id, fld);
  }

  /**
   *
   */
  private class SourceElementComponentWrapper {
    private ConceptMapGroupComponent group;
    private SourceElementComponent comp;

    /**
     * @param group
     * @param comp
     */
    @SuppressWarnings("WeakerAccess")
    public SourceElementComponentWrapper(ConceptMapGroupComponent group, SourceElementComponent comp) {
      super();
      this.group = group;
      this.comp = comp;
    }
  }

  /**
   * @param context
   * @param map
   * @param source
   * @param conceptMapUrl
   * @param fieldToReturn
   * @return
   * @throws FHIRException
   */
  public Base translate(TransformContext context, StructureMap map, Base source, String conceptMapUrl, String fieldToReturn) throws FHIRException {
    Coding src = new Coding();
    if (source.isPrimitive()) {
      src.setCode(source.primitiveValue());
    } else if ("Coding".equals(source.fhirType())) {

      Base[] b = source.getProperty("system".hashCode(), "system", true);
      if (b.length == 1)
        src.setSystem(b[0].primitiveValue());
      b = source.getProperty("code".hashCode(), "code", true);
      if (b.length == 1)
        src.setCode(b[0].primitiveValue());
    } else if ("CE".equals(source.fhirType())) {
      Base[] b = source.getProperty("codeSystem".hashCode(), "codeSystem", true);
      if (b.length == 1)
        src.setSystem(b[0].primitiveValue());
      b = source.getProperty("code".hashCode(), "code", true);
      if (b.length == 1)
        src.setCode(b[0].primitiveValue());
    } else
      throw new FHIRException("Unable to translate source " + source.fhirType());

    String su = conceptMapUrl;
    if (conceptMapUrl.equals("http://hl7.org/fhir/ConceptMap/special-oid2uri")) {
      String uri = getWorker().oid2Uri(src.getCode());
      if (uri == null)
        uri = "urn:oid:" + src.getCode();
      if ("uri".equals(fieldToReturn))
        return new UriType(uri);
      else
        throw new FHIRException("Error in return code");
    } else {
      ConceptMap cMap = null;
      if (conceptMapUrl.startsWith("#")) {
        for (Resource r : map.getContained()) {
          if (r instanceof ConceptMap && r.getId().equals(conceptMapUrl.substring(1))) {
            cMap = (ConceptMap) r;
            su = map.getUrl() + conceptMapUrl;
          }
        }
        if (cMap == null)
          throw new FHIRException("Unable to translate - cannot find map " + conceptMapUrl);
      } else
        cMap = getWorker().fetchResource(ConceptMap.class, conceptMapUrl);
      Coding outcome = null;
      boolean done = false;
      String message = null;
      if (cMap == null) {
        if (getServices() == null)
          message = "No map found for " + conceptMapUrl;
        else {
          outcome = getServices().translate(context.getAppInfo(), src, conceptMapUrl);
          done = true;
        }
      } else {
        List<SourceElementComponentWrapper> list = new ArrayList<>();
        for (ConceptMapGroupComponent g : cMap.getGroup()) {
          for (SourceElementComponent e : g.getElement()) {
            if (!src.hasSystem() && src.getCode().equals(e.getCode()))
              list.add(new SourceElementComponentWrapper(g, e));
            else if (src.hasSystem() && src.getSystem().equals(g.getSource()) && src.getCode().equals(e.getCode()))
              list.add(new SourceElementComponentWrapper(g, e));
          }
        }
        if (list.size() == 0)
          done = true;
        else if (list.get(0).comp.getTarget().size() == 0)
          message = "Concept map " + su + " found no translation for " + src.getCode();
        else {
          for (TargetElementComponent tgt : list.get(0).comp.getTarget()) {
            if (tgt.getEquivalence() == null || EnumSet.of(ConceptMapEquivalence.EQUAL, ConceptMapEquivalence.RELATEDTO, ConceptMapEquivalence.EQUIVALENT, ConceptMapEquivalence.WIDER).contains(tgt.getEquivalence())) {
              if (done) {
                message = "Concept map " + su + " found multiple matches for " + src.getCode();
                done = false;
              } else {
                done = true;
                outcome = new Coding().setCode(tgt.getCode()).setSystem(list.get(0).group.getTarget());
              }
            } else if (tgt.getEquivalence() == ConceptMapEquivalence.UNMATCHED) {
              done = true;
            }
          }
          if (!done)
            message = "Concept map " + su + " found no usable translation for " + src.getCode();
        }
      }
      if (!done)
        throw new FHIRException(message);
      if (outcome == null)
        return null;
      if ("code".equals(fieldToReturn))
        return new CodeType(outcome.getCode());
      else
        return outcome;
    }
  }

  /**
   * Given a structure map, return a set of analyses on it.
   * <p>
   * The method first processes each group contained in the map, then
   * proceeds through each rule declared in the group, and for each rule
   * processes the rule target and/or any nested rule and/or group.
   * <p>
   * Returned:
   * - a list or profiles for what it will create. First profile is the target
   * - a table with a summary (in xhtml) for easy human understanding of the mapping
   *
   * @param batchContext
   * @param appInfo
   * @param map
   * @return
   * @throws Exception
   */
  @SuppressWarnings({"WeakerAccess", "unused"})
  public StructureMapAnalysis analyse(BatchContext batchContext, Object appInfo, StructureMap map) throws Exception {
    setStructureMap(map);
    getIds().clear();
    TransformContext context = batchContext.getTransformationContext(map.getUrl());//In case transform context is added prior to invocation of analyze.
    if (context == null) {
      context = new TransformContext(appInfo);
      batchContext.addTransformationContext(map.getUrl(), context);
    }
    StructureMapAnalysis result = new StructureMapAnalysis();
    VariablesForProfiling vars = new VariablesForProfiling(false, false);
    if (map.getGroup().size() == 0) {
      throw new InvalidMapConfigurationException("A StructureMap must specify at least one group");
    }

    result.setSummary(new XhtmlNode(NodeType.Element, "table").setAttribute("class", "grid"));
    XhtmlNode tr = result.getSummary().addTag("tr");
    tr.addTag("td").addTag("b").addText("Source");
    tr.addTag("td").addTag("b").addText("Target");

    log("Start Profiling Transform " + map.getUrl());
    StructureMapGroupHandler groupRunner = new StructureMapGroupHandler(getStructureMap(), getWorker(), this, map.getGroup().get(0));
    groupRunner.analyzeGroup(batchContext, "", result);
    //analyseGroup("", context, map, vars, start, result);
    ProfileUtilities pu = new ProfileUtilities(getWorker(), null, pkp);
    for (StructureDefinition sd : result.getProfiles())
      pu.cleanUpDifferential(sd);
    return result;
  }

  @SuppressWarnings("unused")
  public StructureMap generateMapFromMappings(StructureDefinition sd) throws IOException, FHIRException {
    String id = getLogicalMappingId(sd);
    if (id == null)
      return null;
    String prefix = ToolingExtensions.readStringExtension(sd, ToolingExtensions.EXT_MAPPING_PREFIX);
    String suffix = ToolingExtensions.readStringExtension(sd, ToolingExtensions.EXT_MAPPING_SUFFIX);
    if (prefix == null || suffix == null)
      return null;
    // we build this by text. Any element that has a mapping, we put it's mappings inside it....
    StringBuilder b = new StringBuilder();
    b.append(prefix);

    ElementDefinition root = sd.getSnapshot().getElementFirstRep();
    String m = getMapping(root, id);
    if (m != null)
      b.append(m).append("\r\n");
    addChildMappings(b, id, "", sd, root, false);
    b.append("\r\n");
    b.append(suffix);
    b.append("\r\n");
    DefaultStructureMapParser mapParser = new DefaultStructureMapParser();
    StructureMap map = mapParser.parse(b.toString());
    map.setId(tail(map.getUrl()));
    if (!map.hasStatus())
      map.setStatus(PublicationStatus.DRAFT);
    map.getText().setStatus(NarrativeStatus.GENERATED);
    map.getText().setDiv(new XhtmlNode(NodeType.Element, "div"));
    map.getText().getDiv().addTag("pre").addText(StructureMapSerializer.render(map));
    return map;
  }


  /**
   * @param url
   * @return
   */
  private String tail(String url) {
    return url.substring(url.lastIndexOf("/") + 1);
  }

  /**
   * @param b
   * @param id
   * @param indent
   * @param sd
   * @param ed
   * @param inner
   * @throws DefinitionException
   */
  @SuppressWarnings("ConstantConditions")
  private void addChildMappings(StringBuilder b, String id, String indent, StructureDefinition sd, ElementDefinition ed, boolean inner) throws DefinitionException {
    boolean first = true;
    List<ElementDefinition> children = ProfileUtilities.getChildMap(sd, ed);
    for (ElementDefinition child : children) {
      if (first && inner) {
        b.append(" then {\r\n");
        first = false;
      }
      String map = getMapping(child, id);
      if (map != null) {
        b.append(indent).append("  ").append(child.getPath()).append(": ").append(map);
        addChildMappings(b, id, indent + "  ", sd, child, true);
        b.append("\r\n");
      }
    }
    if (!first && inner)
      b.append(indent).append("}");

  }

  /**
   * @param ed
   * @param id
   * @return
   */
  private String getMapping(ElementDefinition ed, String id) {
    for (ElementDefinitionMappingComponent map : ed.getMapping())
      if (id.equals(map.getIdentity()))
        return map.getMap();
    return null;
  }

  /**
   * @param sd
   * @return
   */
  private String getLogicalMappingId(StructureDefinition sd) {
    for (StructureDefinitionMappingComponent map : sd.getMapping()) {
      if ("http://hl7.org/fhir/logical".equals(map.getUri()))
        return map.getIdentity();
    }
    return null;
  }

}
