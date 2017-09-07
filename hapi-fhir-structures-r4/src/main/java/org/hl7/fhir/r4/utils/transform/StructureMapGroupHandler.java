package org.hl7.fhir.r4.utils.transform;

import org.hl7.fhir.exceptions.DefinitionException;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.r4.context.IWorkerContext;
import org.hl7.fhir.r4.elementmodel.Property;
import org.hl7.fhir.r4.model.ExpressionNode;
import org.hl7.fhir.r4.model.StructureDefinition;
import org.hl7.fhir.r4.model.StructureMap;
import org.hl7.fhir.r4.model.TypeDetails;
import org.hl7.fhir.r4.utils.transform.exception.InvalidMapConfigurationException;
import org.hl7.fhir.utilities.xhtml.XhtmlNode;

public class StructureMapGroupHandler extends BaseRunner {
  private StructureMap.StructureMapGroupComponent ruleGroup;
  private VariablesForProfiling vars;
  private FhirTransformationEngine transformationEngine;

  public StructureMapGroupHandler(StructureMap map, FhirTransformationEngine transformationEngine, StructureMap.StructureMapGroupComponent ruleGroup) {
    setTransformationEngine(transformationEngine);
    setStructureMap(map);
    this.ruleGroup = ruleGroup;
  }

  public StructureMapGroupHandler(StructureMap map, IWorkerContext worker, FhirTransformationEngine transformationEngine, StructureMap.StructureMapGroupComponent ruleGroup) {
    this(map, transformationEngine, ruleGroup);
    setWorker(worker);
  }



  protected void initialize(StructureMapAnalysis result) throws DefinitionException {
    vars = new VariablesForProfiling(false, false);
    for (StructureMap.StructureMapGroupInputComponent t : ruleGroup.getInput()) {
      PropertyWithType ti = resolveType(getWorker(), getStructureMap(), t.getType(), t.getMode());
      if (t.getMode() == StructureMap.StructureMapInputMode.SOURCE) {
        vars.add(VariableMode.INPUT, t.getName(), ti);
      } else {
        vars.add(VariableMode.OUTPUT, t.getName(), createProfile(getStructureMap(), result.getProfiles(), ti, ruleGroup.getName(), ruleGroup));
      }
    }
  }

  public void analyzeGroup(String indent, StructureMapAnalysis result, TransformContext context) throws Exception {
    initialize(result);
    log(indent + "Analyse Group : " + getRuleGroup().getName());
    // todo: extends
    // todo: check inputs
    XhtmlNode tr = result.getSummary().addTag("tr").setAttribute("class", "diff-title"); //TODO Find a cleaner way to handle documentation during an execution
    XhtmlNode xs = tr.addTag("td");
    XhtmlNode xt = tr.addTag("td");
    for (StructureMap.StructureMapGroupInputComponent inp : getRuleGroup().getInput()) {
      if (inp.getMode() == StructureMap.StructureMapInputMode.SOURCE)
        noteInput(vars, inp, VariableMode.INPUT, xs);
      if (inp.getMode() == StructureMap.StructureMapInputMode.TARGET)
        noteInput(vars, inp, VariableMode.OUTPUT, xt);
    }
    for (StructureMap.StructureMapGroupRuleComponent rule : getRuleGroup().getRule()) {
      StructureMapRuleRunner ruleRunner = new StructureMapRuleRunner(getStructureMap(), getTransformationEngine(), this, rule, getWorker());
      ruleRunner.analyseRule(indent + "  ", context, result);
    }
  }

  protected void executeGroup(String indent, TransformContext context, Variables vars) throws FHIRException {
    log(indent + "Group : " + getRuleGroup().getName());
    // todo: check inputs
    if (getRuleGroup().hasExtends()) {
      ResolvedGroup rg = resolveGroupReference(getStructureMap(), getRuleGroup(), getRuleGroup().getExtends());
      StructureMapGroupHandler groupRunner = new StructureMapGroupHandler(rg.targetMap, getTransformationEngine(), rg.target);
      executeGroup(indent + " ", context, vars);
    }

    for (StructureMap.StructureMapGroupRuleComponent rule : getRuleGroup().getRule()) {
      StructureMapRuleRunner ruleRunner = new StructureMapRuleRunner(getStructureMap(), getTransformationEngine(), this, rule);
      ruleRunner.executeRule(indent + "  ", context, vars, getRuleGroup(), rule);
    }
  }

  public StructureMap.StructureMapGroupComponent getRuleGroup() {
    return ruleGroup;
  }

  public void setRuleGroup(StructureMap.StructureMapGroupComponent ruleGroup) {
    this.ruleGroup = ruleGroup;
  }

  public VariablesForProfiling getVars() {
    return vars;
  }

  public void setVars(VariablesForProfiling vars) {
    this.vars = vars;
  }

  public FhirTransformationEngine getTransformationEngine() {
    return transformationEngine;
  }

  public void setTransformationEngine(FhirTransformationEngine transformationEngine) {
    this.transformationEngine = transformationEngine;
  }

  /**
   * Method retrieves the structure definition associated with the source and the target StructureMap parameters.
   *
   * @param map  The StructureMap representing this transformation
   * @param type The type declared for the given source or target transformation parameter
   * @param mode Source or Target
   * @return
   * @throws Exception Error thrown when no structure definition cannot be found for a map import or for the source or target type specified
   */
  private PropertyWithType resolveType(IWorkerContext worker, StructureMap map, String type, StructureMap.StructureMapInputMode mode) {
    for (StructureMap.StructureMapStructureComponent imp : map.getStructure()) {
      if ((imp.getMode() == StructureMap.StructureMapModelMode.SOURCE && mode == StructureMap.StructureMapInputMode.SOURCE) ||
        (imp.getMode() == StructureMap.StructureMapModelMode.TARGET && mode == StructureMap.StructureMapInputMode.TARGET)) {
        StructureDefinition sd = worker.fetchResource(StructureDefinition.class, imp.getUrl());
        if (sd == null) {
          throw new InvalidMapConfigurationException("Import " + imp.getUrl() + " cannot be resolved");
        }
        if (sd.getType().equals(type)) {
          return new PropertyWithType(sd.getType(), new Property(worker, sd.getSnapshot().getElement().get(0), sd), null, new TypeDetails(ExpressionNode.CollectionStatus.SINGLETON, sd.getUrl()));
        }
      }
    }
    throw new InvalidMapConfigurationException("Unable to find structure definition for " + type + " in imports");
  }
}
