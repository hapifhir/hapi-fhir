package org.hl7.fhir.r4.utils.transform;

import org.hl7.fhir.r4.model.*;
import org.hl7.fhir.r4.utils.transform.deserializer.*;

import java.util.List;
import java.util.Stack;

/**
* Handles the data of a structure map while ANTLR parses its raw text from the mapping language.
* @Author Travis Lukach
*/
public class MapHandler implements IFhirMapExecutor {

  /**
  * The Structure Map object that will be populated as ANTLR visits each element.
  */
  private StructureMap structureMap = new StructureMap();

  /**
  * The current group being populated
  */
  private StructureMap.StructureMapGroupComponent currentGroup;

  /**
  * The current source that is being populated
  */
  private StructureMap.StructureMapGroupRuleSourceComponent currentSource;

  /**
  * The current target that is being populated
  */
  private StructureMap.StructureMapGroupRuleTargetComponent currentTarget;

  /**
  * For handling rules, if a rule has nested rules within it, it will remain on the stack below the child rules until they are resolved, then the parent can resolve itself and be added to the group or even a parent rule.
  */
  private Stack<StructureMap.StructureMapGroupRuleComponent> ruleComponentStack = new Stack<>();

  /**
  * Read accessor for the Structure Map
  * @return the StructureMap
  */
  public StructureMap getStructureMap() {
    return structureMap;
  }

  /**
  * Populates the basic values in a structure map
  * @param structureMap prospected Url value for structure map
  * @param name prospected name for structure map
  * @throws Exception if it fails to populate correctly
  */
  @Override
  public void map(UrlData structureMap, String name) throws Exception {
    this.structureMap = new StructureMap();
    this.structureMap.setUrl(structureMap.toString());
    this.structureMap.setName(name);
  }

  /**
  * populates a structure used within the Structure Map
  * @param structureDefinition Url of the Structure Definition
  * @param name name of the Structure Definition which will aos determine its mode. The name determined in this statement will dictate the behavior of the structure.
  * @throws Exception if it fails to populate correctly
  */
  @Override
  public void uses(UrlData structureDefinition, FhirMapUseNames name) throws Exception {
    StructureMap.StructureMapStructureComponent structureComponent = new StructureMap.StructureMapStructureComponent();
    structureComponent.setUrl(structureDefinition.toString());
    structureComponent.setMode(StructureMap.StructureMapModelMode.fromCode(name.getValue()));
    this.structureMap.addStructure(structureComponent);
  }

  /**
  * Populates an import value for the structure
  * @param structureMap the import value
  * @throws Exception if it fails to populate correctly
  */
  @Override
  public void imports(UrlData structureMap) throws Exception {
    this.structureMap.addImport(structureMap.toString());
  }

  /**
  * Initializes the group value and allows base properties to be populated.
  * @param groupName name of group
  * @param groupType Group type. In grammar this is optional in which case this will be set to GroupTypesUnset)
  * @param groupExtendName extends value fo rthe group
  * @throws Exception if it fails to populate correctly
  */
  @Override
  public void groupStart(String groupName, FhirMapGroupTypes groupType, String groupExtendName) throws Exception {
    this.currentGroup =  new StructureMap.StructureMapGroupComponent();
    this.currentGroup.setName(groupName);
    this.currentGroup.setTypeMode(StructureMap.StructureMapGroupTypeMode.fromCode(groupType.getValue()));
    this.currentGroup.setExtends(groupExtendName);
    this.ruleComponentStack.clear(); //Clear the stack for good measure, anything that isn't resolved gets lost to the aether.
  }

  /**
  * Signals to perform final operations and add the group to the structure map.
  * @throws Exception if it fails to populate correctly
  */
  @Override
  public void groupEnd() throws Exception {
    this.structureMap.addGroup(this.currentGroup);
  }

  /**
  * Populates the input
  * @param name name of input
  * @param type type of input
  * @param mode input mode
  * @throws Exception if it fails to populate correctly
  */
  @Override
  public void groupInput(String name, String type, FhirMapInputModes mode) throws Exception {
    /*
    The current input being populated
    */
    StructureMap.StructureMapGroupInputComponent currentInput = new StructureMap.StructureMapGroupInputComponent();
    currentInput.setName(name);
    currentInput.setType(type);
    currentInput.setMode(StructureMap.StructureMapInputMode.fromCode(mode.getValue()));
    this.currentGroup.addInput(currentInput);
  }

  /**
  * Creates a rule putting it on the stack while populating base values.
  * @param ruleName Name of rule
  * @throws Exception if it fails to populate correctly
  */
  @Override
  public void ruleStart(List<String> ruleName) throws Exception {
    this.ruleComponentStack.push(new StructureMap.StructureMapGroupRuleComponent());
    StringBuilder name = new StringBuilder();
    for (String s : ruleName) {
      if (name.length()==0) {
        name = new StringBuilder(s);
      }
      else {
        name.append(".").append(s);
      }
    }
    this.ruleComponentStack.peek().setName(name.toString());
  }

  /**
  * Populates a rule source and adds it to the top rule in the stack.
  * @param context source context
  * @param type optional type name and cardinality. Null if unset
  * @param defaultValue optional default value.
  * @param listOptions Optional list options. FhirMappingListOpeions.NotSet if unset
  * @param variable Optional assignment variable. Null if unset
  * @param wherePath Optional where fhir path. Null if unset
  * @param checkPath Optional check fhir path. Null if unset
  * @throws Exception if it fails to populate correctly
  */
  @Override
  public void ruleSource(List<String> context,  FhirMapRuleType type,  String defaultValue,  FhirMapListOptions listOptions,  String variable,  String wherePath,  String checkPath) throws Exception {
    this.currentSource = new StructureMap.StructureMapGroupRuleSourceComponent();
    this.currentSource.setContext(context.get(0));
    if(context.size() == 2) {
      this.currentSource.setElement(context.get(1));
    }
    if (type != null) {
      this.currentSource.setType(type.TypeName);
    }
    if (defaultValue != null) {
      this.currentSource.setDefaultValue(new StringType(defaultValue));
    }
    if (variable != null) {
      this.currentSource.setVariable(variable);
    }
    if (wherePath != null) {
      this.currentSource.setCondition(wherePath);
    }
    if (checkPath != null) {
      this.currentSource.setCheck(checkPath);
    }
    this.ruleComponentStack.peek().addSource(this.currentSource);
  }

  /**
  * Populates the target and adds it to the top rule on the stack.
  * @param context Target Context
  * @param targetVariable variable being assigned
  * @throws Exception if it fails to populate correctly
  */
  @Override
  public void transformAs(List<String> context, String targetVariable) throws Exception {
    this.currentTarget = new StructureMap.StructureMapGroupRuleTargetComponent();
    this.currentTarget.setContext(context.get(0));
    this.currentTarget.setElement(context.get(1));
    this.currentTarget.setVariable(targetVariable);
    this.ruleComponentStack.peek().addTarget(this.currentTarget);
  }

  /**
  * Populates the target for an Append Transform, and adds it to the top rule on the stack
  * @param context Target context
  * @param appendVariables value appended in transform
  * @param targetVariable target variable assigned, can be null
  * @throws Exception if it fails to populate correctly
  */
  @Override
  public void transformAppend(List<String> context, List<String> appendVariables, String targetVariable) throws Exception {
    this.currentTarget = new StructureMap.StructureMapGroupRuleTargetComponent();
    StringBuilder ctx = new StringBuilder();
    for (String s : context){
      if (s.length()==0){
        ctx = new StringBuilder(s);
      }
      else {
        ctx.append(".").append(s);
      }
    }
    this.currentSource.setContext(ctx.toString());
    for (String appendVar : appendVariables){
      StructureMap.StructureMapGroupRuleTargetParameterComponent param = new StructureMap.StructureMapGroupRuleTargetParameterComponent();
      param.setValue(new StringType(appendVar));
      this.currentTarget.addParameter(param);
    }
    if (targetVariable != null){
      this.currentTarget.setVariable(targetVariable);
    }
    this.currentTarget.setTransform(StructureMap.StructureMapTransform.APPEND);
    this.ruleComponentStack.peek().addTarget(this.currentTarget);
  }

  /**
  * Populates a target for a Cast Transform, adds it ot the top rule on the stack
  * @param context Target context
  * @param sourceVariable Source variable
  * @param typeName Type of value to cast to, may be null
  * @param targetVariable variable to assign value to
  * @throws Exception if it fails to populate correctly
  */
  @Override
  public void transformCast(List<String> context, String sourceVariable, String typeName, String targetVariable) throws Exception {
    this.currentTarget = new StructureMap.StructureMapGroupRuleTargetComponent();
    StringBuilder ctx = new StringBuilder();
    for (String s : context){
      if (s.length()==0){
        ctx = new StringBuilder(s);
      }
      else {
        ctx.append(".").append(s);
      }
    }
    this.currentSource.setContext(ctx.toString());

    this.currentTarget.addParameter(new StructureMap.StructureMapGroupRuleTargetParameterComponent(new IdType(sourceVariable)));
    this.currentTarget.addParameter(new StructureMap.StructureMapGroupRuleTargetParameterComponent(new IdType(typeName)));
    if (targetVariable != null){
      this.currentTarget.setVariable(targetVariable);
    }
    this.currentTarget.setTransform(StructureMap.StructureMapTransform.CAST);
    this.ruleComponentStack.peek().addTarget(this.currentTarget);
  }

  /**
  * Not soundly implemented.
  * @param id Group name
  * @param params parameters to go into the gorup
  */
  @Override
  public void groupCall(String id, List<String> params) {
    //TODO: is there a property in the mapping that allows another group to be target on a call
    return;
  }

  /**
  * Populates a target for a Coding Transform, adds it to the top rule on the stack
  * @param context Target context.
  * @param system URL dictating the Code system
  * @param code the code value
  * @param display display value for the Coding object
  * @param targetVariable variable to be assigned the Coding object, may be left null
  * @throws Exception if it fails to populate correctly
  */
  @Override
  public void transformCoding(List<String> context, UrlData system, String code, String display, String targetVariable) throws Exception {
    this.currentTarget = new StructureMap.StructureMapGroupRuleTargetComponent();
    this.currentTarget.setContext(context.get(0));
    this.currentTarget.setElement(context.get(1));
    this.currentTarget.addParameter(new StructureMap.StructureMapGroupRuleTargetParameterComponent(new StringType(system.toString())));
    this.currentTarget.addParameter(new StructureMap.StructureMapGroupRuleTargetParameterComponent(new StringType(code)));
    if (targetVariable != null) {
      this.currentTarget.addParameter(new StructureMap.StructureMapGroupRuleTargetParameterComponent(new StringType(display)));
    }
    if (targetVariable != null) {
      this.currentTarget.setVariable(targetVariable);
    }
    this.currentTarget.setTransform(StructureMap.StructureMapTransform.C);
    this.ruleComponentStack.peek().addTarget(this.currentTarget);
  }

  /**
  * Populates a target for a Codeable Concept Transform, adds it to the top rule on the stack
  * @param context Target context
  * @param system System used in the Codeable COncept
  * @param code Code in the Codeable Concept
  * @param display Display value for the Codeable Concept, may be left null
  * @param targetVariable Variable to hold the Codeable Concept object
  * @throws Exception if it fails to populate correctly
  */
  @Override
  public void transformCodeableConcept(List<String> context, UrlData system, String code, String display, String targetVariable) throws Exception {
    this.currentTarget = new StructureMap.StructureMapGroupRuleTargetComponent();
    this.currentTarget.setContext(context.get(0));
    this.currentTarget.setElement(context.get(1));
    this.currentTarget.addParameter(new StructureMap.StructureMapGroupRuleTargetParameterComponent(new StringType(system.toString())));
    this.currentTarget.addParameter(new StructureMap.StructureMapGroupRuleTargetParameterComponent(new StringType(code)));
    if (display != null) {
      this.currentTarget.addParameter(new StructureMap.StructureMapGroupRuleTargetParameterComponent(new StringType(display)));
    }
    if (targetVariable != null){
      this.currentTarget.setVariable(targetVariable);
    }
    this.currentTarget.setTransform(StructureMap.StructureMapTransform.CC);
    this.ruleComponentStack.peek().addTarget(this.currentTarget);
  }

  /**
  * Populates a target for a Codeable Concept Transform, adds it to the top rule on the stack
  * @param context Target context
  * @param text text for Codeable Concept
  * @param targetVariable Variable to hold the Codeable Concept object
  * @throws Exception if it fails to populate correctly
  */
  @Override
  public void transformCodeableConcept(List<String> context, String text, String targetVariable) throws Exception {
    this.currentTarget = new StructureMap.StructureMapGroupRuleTargetComponent();
    this.currentTarget.setContext(context.get(0));
    this.currentTarget.setElement(context.get(1));
    this.currentTarget.addParameter(new StructureMap.StructureMapGroupRuleTargetParameterComponent(new StringType(text)));
    if (targetVariable != null){
      this.currentTarget.setVariable(targetVariable);
    }
    this.currentTarget.setTransform(StructureMap.StructureMapTransform.CC);
    this.ruleComponentStack.peek().addTarget(this.currentTarget);
  }

  /**
  * Populates a target for a Copy transform, adds it to the top rule on the stack
  * @param context Target context
  * @param copyVariable Varaible or value copied
  * @param targetVariable Variable that will hold the result of the transform
  * @throws Exception if it fails to populate correctly
  */
  @Override
  public void transformCopy(List<String> context, String copyVariable, String targetVariable) throws Exception {
    this.currentTarget = new StructureMap.StructureMapGroupRuleTargetComponent();
    this.currentTarget.setContext(context.get(0));
    this.currentTarget.setElement(context.get(1));

    if (copyVariable.contains("\"")) {
      this.currentTarget.addParameter(new StructureMap.StructureMapGroupRuleTargetParameterComponent(new StringType(copyVariable.substring(1, copyVariable.length() - 1))));
    }
    else {
      this.currentTarget.addParameter(new StructureMap.StructureMapGroupRuleTargetParameterComponent(new IdType(copyVariable)));
    }
    if (targetVariable != null){
      this.currentTarget.setVariable(targetVariable);
    }
    this.currentTarget.setTransform(StructureMap.StructureMapTransform.COPY);
    this.ruleComponentStack.peek().addTarget(this.currentTarget);
  }

  /**
  * Populates a target for a Cp transform, adds it to the top rule on the stack
  * @param context Target context
  * @param system System value for the transform
  * @param cpVariable Cp variable to be added
  * @param targetVariable Variable that will hold the result of the transform, may be left null
  * @throws Exception if it fails to populate correctly
  */
  @Override
  public void transformCp(List<String> context,  UrlData system, String cpVariable, String targetVariable) throws Exception {
    this.currentTarget = new StructureMap.StructureMapGroupRuleTargetComponent();
    this.currentTarget.setContext(context.get(0));
    this.currentTarget.setElement(context.get(1));
    if (system != null) {
      this.currentTarget.addParameter(new StructureMap.StructureMapGroupRuleTargetParameterComponent(new StringType(system.toString())));
    }
    this.currentTarget.addParameter(new StructureMap.StructureMapGroupRuleTargetParameterComponent(new StringType(cpVariable)));
    if (targetVariable != null) {
      this.currentTarget.setVariable(targetVariable);
    }
    this.currentTarget.setTransform(StructureMap.StructureMapTransform.CP);
    this.ruleComponentStack.peek().addTarget(this.currentTarget);

  }

  /**
  * Populates a target for a Create transform, adds it to the top rule on the stack
  * @param context Target Context
  * @param createVariable type of object to be created
  * @param targetVariable Variable that will hold the result of the transform, may be left null
  * @throws Exception if it fails to populate correctly
  */
  @Override
  public void transformCreate(List<String> context, String createVariable, String targetVariable) throws Exception {
    this.currentTarget = new StructureMap.StructureMapGroupRuleTargetComponent();
    if (context!= null) {
      this.currentTarget.setContext(context.get(0));
      if (context.size() == 2) {
        this.currentTarget.setElement(context.get(1));
      }
    }
    this.currentTarget.setTransform(StructureMap.StructureMapTransform.CREATE);
    this.currentTarget.addParameter(new StructureMap.StructureMapGroupRuleTargetParameterComponent(new StringType(createVariable)));
    this.currentTarget.setVariable(targetVariable);
    this.ruleComponentStack.peek().addTarget(this.currentTarget);

  }

  /**
  * Populates a target for a DateOp transform, adds it to the top rule on the stack
  * @param context Target Context
  * @param variable variable to be fed into the transform
  * @param operation type of operation performed
  * @param variable2 optional variable to be fed into the transform
  * @param targetVariable Variable that will hold the result of the transform, may be left null
  * @throws Exception if it fails to populate correctly
  */
  @Override
  public void transformDateOp(List<String> context, String variable, String operation, String variable2, String targetVariable) throws Exception {
    this.currentTarget = new StructureMap.StructureMapGroupRuleTargetComponent();
    this.currentTarget.setContext(context.get(0));
    this.currentTarget.setElement(context.get(1));
    this.currentTarget.addParameter(new StructureMap.StructureMapGroupRuleTargetParameterComponent(new IdType(variable)));
    this.currentTarget.addParameter(new StructureMap.StructureMapGroupRuleTargetParameterComponent(new IdType(operation)));
    this.currentTarget.addParameter(new StructureMap.StructureMapGroupRuleTargetParameterComponent(new IdType(variable2)));
    if (targetVariable != null) {
      this.currentTarget.setVariable(targetVariable);
    }
    this.currentTarget.setTransform(StructureMap.StructureMapTransform.DATEOP);
    this.ruleComponentStack.peek().addTarget(this.currentTarget);

  }

  /**
  * Populates a target for an Escape transform, adds it to the top rule on the stack
  * @param context Target Context
  * @param variable Variable fed into the transform
  * @param string1 String fed into the transform
  * @param string2 Second string fed into the transform, may be left null
  * @param targetVariable Variable that will hold the result of the transform, may be left null
  * @throws Exception if it fails to populate correctly
  */
  @Override
  public void transformEscape(List<String> context, String variable, String string1, String string2, String targetVariable) throws Exception {
    this.currentTarget = new StructureMap.StructureMapGroupRuleTargetComponent();
    this.currentTarget.setContext(context.get(0));
    this.currentTarget.setElement(context.get(1));
    this.currentTarget.addParameter(new StructureMap.StructureMapGroupRuleTargetParameterComponent(new IdType(variable)));
    this.currentTarget.addParameter(new StructureMap.StructureMapGroupRuleTargetParameterComponent(new StringType(string1)));
    this.currentTarget.addParameter(new StructureMap.StructureMapGroupRuleTargetParameterComponent(new StringType(string2)));
    if (targetVariable != null) {
      this.currentTarget.setVariable(targetVariable);
    }
    this.currentTarget.setTransform(StructureMap.StructureMapTransform.ESCAPE);
    this.ruleComponentStack.peek().addTarget(this.currentTarget);
  }

  /**
  * Populates a target for an Extension transform, adds it to the top rule on the stack
  * @param context Target Context
  * @param targetVariable Variable that will hold the result of the transform, may be left null
  */
  @Override
  public void transformExtension(List<String> context, String targetVariable){
    this.currentTarget = new StructureMap.StructureMapGroupRuleTargetComponent();
    this.currentTarget.setContext(context.get(0));
    this.currentTarget.setElement(context.get(1));
    this.currentTarget.setTransform(StructureMap.StructureMapTransform.EXTENSION);
    if (targetVariable != null){
      this.currentTarget.setVariable(targetVariable);
    }
    this.ruleComponentStack.peek().addTarget(this.currentTarget);
  }

  /**
  * Populates a target for an Extension transform, adds it to the top rule on the stack
  * @param context Target Context
  * @param targetVariable Variable that will hold the result of the transform, may be left null
  */
  @Override
  public void transformExtension(List<String> context, UrlData extUri, String title, String mode, String parent, String text1, String text2, int min, String max, String type, String targetVariable){
    this.currentTarget = new StructureMap.StructureMapGroupRuleTargetComponent();
    this.currentTarget.setContext(context.get(0));
    this.currentTarget.setElement(context.get(1));
    this.currentTarget.setTransform(StructureMap.StructureMapTransform.EXTENSION);
    this.currentTarget.addParameter(new StructureMap.StructureMapGroupRuleTargetParameterComponent(new StringType(extUri.CompleteUrl)));
    this.currentTarget.addParameter(new StructureMap.StructureMapGroupRuleTargetParameterComponent(new StringType(title)));
    this.currentTarget.addParameter(new StructureMap.StructureMapGroupRuleTargetParameterComponent(new StringType(mode)));
    this.currentTarget.addParameter(new StructureMap.StructureMapGroupRuleTargetParameterComponent(new StringType(parent)));
    this.currentTarget.addParameter(new StructureMap.StructureMapGroupRuleTargetParameterComponent(new StringType(text1)));
    this.currentTarget.addParameter(new StructureMap.StructureMapGroupRuleTargetParameterComponent(new StringType(text2)));
    this.currentTarget.addParameter(new StructureMap.StructureMapGroupRuleTargetParameterComponent(new IntegerType(min)));
    this.currentTarget.addParameter(new StructureMap.StructureMapGroupRuleTargetParameterComponent(new StringType(max)));
    this.currentTarget.addParameter(new StructureMap.StructureMapGroupRuleTargetParameterComponent(new StringType(type)));
    if (targetVariable != null) {
      this.currentTarget.setVariable(targetVariable);
    }
    this.ruleComponentStack.peek().addTarget(this.currentTarget);
  }

  /**
  * Populates a target for an Evaluate transform, adds it to the top rule on the stack
  * @param context Target Context
  * @param obj object type
  * @param objElement element within object
  * @param targetVariable Variable that will hold the result of the transform, may be left null
  * @throws Exception if it fails to populate correctly
  */
  @Override
  public void transformEvaluate(List<String> context, String obj, String objElement, String targetVariable) throws Exception {
    this.currentTarget = new StructureMap.StructureMapGroupRuleTargetComponent();
    this.currentTarget.setContext(context.get(0));
    this.currentTarget.setElement(context.get(1));
    this.currentTarget.addParameter(new StructureMap.StructureMapGroupRuleTargetParameterComponent(new IdType(objElement)));
    if (targetVariable != null) {
      this.currentTarget.setVariable(targetVariable);
    }
    this.currentTarget.setTransform(StructureMap.StructureMapTransform.EVALUATE);
    this.ruleComponentStack.peek().addTarget(this.currentTarget);
  }

  /**
  * Populates a target for an Id transform, adds it to the top rule on the stack
  * @param context Target Context
  * @param system Url dictating the system used for the Id
  * @param value The Id Value
  * @param type type value
  * @param targetVariable Variable that will hold the result of the transform, may be left null
  * @throws Exception if it fails to populate correctly
  */
  @Override
  public void transformId(List<String> context, UrlData system, String value, String type, String targetVariable) throws Exception {
    this.currentTarget = new StructureMap.StructureMapGroupRuleTargetComponent();
    this.currentTarget.setContext(context.get(0));
    this.currentTarget.setElement(context.get(1));
    this.currentTarget.addParameter(new StructureMap.StructureMapGroupRuleTargetParameterComponent(new UriType(system.toString())));
    this.currentTarget.addParameter(new StructureMap.StructureMapGroupRuleTargetParameterComponent(new IdType(value)));
    this.currentTarget.addParameter(new StructureMap.StructureMapGroupRuleTargetParameterComponent(new IdType(type)));
    if (targetVariable != null) {
      this.currentTarget.setVariable(targetVariable);
    }
    this.currentTarget.setTransform(StructureMap.StructureMapTransform.ID);
    this.ruleComponentStack.peek().addTarget(this.currentTarget);
  }

  /**
  * Populates a target for a Pointer transform, adds it to the top rule on the stack
  * @param context Target Context
  * @param resource Resource to be pointed to
  * @param targetVariable Variable that will hold the result of the transform, may be left null
  * @throws Exception if it fails to populate correctly
  */
  @Override
  public void transformPointer(List<String> context, String resource, String targetVariable) throws Exception {
    this.currentTarget = new StructureMap.StructureMapGroupRuleTargetComponent();
    this.currentTarget.setContext(context.get(0));
    this.currentTarget.setElement(context.get(1));
    this.currentTarget.addParameter(new StructureMap.StructureMapGroupRuleTargetParameterComponent(new IdType(resource)));
    this.currentTarget.setTransform(StructureMap.StructureMapTransform.POINTER);
    if (targetVariable != null) {
      this.currentTarget.setVariable(targetVariable);
    }
    this.ruleComponentStack.peek().addTarget(this.currentTarget);
  }

  /**
  * Populates a target for a Qty transform, adds it to the top rule on the stack
  * @param context Target Context
  * @param text Text of quantity
  * @param targetVariable Variable that will hold the result of the transform, may be left null
  * @throws Exception if it fails to populate correctly
  */
  @Override
  public void transformQty(List<String> context, String text, String targetVariable) throws Exception {
    this.currentTarget = new StructureMap.StructureMapGroupRuleTargetComponent();
    this.currentTarget.setContext(context.get(0));
    this.currentTarget.setElement(context.get(1));
    this.currentTarget.addParameter(new StructureMap.StructureMapGroupRuleTargetParameterComponent(new StringType(text)));
    this.currentTarget.setTransform(StructureMap.StructureMapTransform.QTY);
    if (targetVariable != null) {
      this.currentTarget.setVariable(targetVariable);
    }
    this.ruleComponentStack.peek().addTarget(this.currentTarget);
  }

  /**
  * Populates a target for a Qty transform, adds it to the top rule on the stack
  * @param context Target Context
  * @param value value of the quantity
  * @param unitString Unit of measure
  * @param system system of the quantity
  * @param targetVariable Variable that will hold the result of the transform, may be left null
  * @throws Exception if it fails to populate correctly
  */
  @Override
  public void transformQty(List<String> context, String value, String unitString, UrlData system, String targetVariable) throws Exception {
    this.currentTarget = new StructureMap.StructureMapGroupRuleTargetComponent();
    this.currentTarget.setContext(context.get(0));
    this.currentTarget.setElement(context.get(1));
    this.currentTarget.addParameter(new StructureMap.StructureMapGroupRuleTargetParameterComponent(new IdType(value)));
    this.currentTarget.addParameter(new StructureMap.StructureMapGroupRuleTargetParameterComponent(new StringType(unitString)));
    this.currentTarget.addParameter(new StructureMap.StructureMapGroupRuleTargetParameterComponent(new UriType(system.toString())));
    if (targetVariable != null) {
      this.currentTarget.setVariable(targetVariable);
    }
    this.currentTarget.setTransform(StructureMap.StructureMapTransform.QTY);
    this.ruleComponentStack.peek().addTarget(this.currentTarget);
  }

  /**
  * Populates a target for a Qty transform, adds it to the top rule on the stack
  * @param context Target Context
  * @param value value of the quantity
  * @param unitString unit of measure
  * @param type type
  * @param targetVariable Variable that will hold the result of the transform, may be left null
  * @throws Exception if it fails to populate correctly
  */
  @Override
  public void transformQty(List<String> context, String value, String unitString, String type, String targetVariable) throws Exception {
    this.currentTarget = new StructureMap.StructureMapGroupRuleTargetComponent();
    this.currentTarget.setContext(context.get(0));
    this.currentTarget.setElement(context.get(1));
    this.currentTarget.setTransform(StructureMap.StructureMapTransform.QTY);
    this.currentTarget.addParameter(new StructureMap.StructureMapGroupRuleTargetParameterComponent(new IdType(value)));
    this.currentTarget.addParameter(new StructureMap.StructureMapGroupRuleTargetParameterComponent(new StringType(unitString)));
    this.currentTarget.addParameter(new StructureMap.StructureMapGroupRuleTargetParameterComponent(new IdType(type)));
    if (targetVariable != null) {
      this.currentTarget.setVariable(targetVariable);
    }
    this.ruleComponentStack.peek().addTarget(this.currentTarget);


  }

  /**
  * Populates a target for a Reference transform, adds it to the top rule on the stack
  * @param context Target context
  * @param text reference value
  * @param targetVariable Variable that will hold the result of the transform, may be left null
  * @throws Exception if it fails to populate correctly
  */
  @Override
  public void transformReference(List<String> context, String text, String targetVariable) throws Exception {
    this.currentTarget = new StructureMap.StructureMapGroupRuleTargetComponent();
    this.currentTarget.setContext(context.get(0));
    this.currentTarget.setElement(context.get(1));
    this.currentTarget.addParameter(new StructureMap.StructureMapGroupRuleTargetParameterComponent(new StringType(text)));
    this.currentTarget.setTransform(StructureMap.StructureMapTransform.REFERENCE);
    this.ruleComponentStack.peek().addTarget(this.currentTarget);

  }

  /**
  * Populates a target for a Translate transform, adds it to the top rule on the stack
  * @param context Target context
  * @param variable variable of the translate
  * @param mapUri the Map URL of the translation
  * @param outputType the type of output expected
  * @param targetVariable Variable that will hold the result of the transform, may be left null
  * @throws Exception if it fails to populate correctly
  */
  @Override
  public void transformTranslate(List<String> context, String variable, UrlData mapUri, FhirMapTranslateOutputTypes outputType, String targetVariable) throws Exception {
    this.currentTarget = new StructureMap.StructureMapGroupRuleTargetComponent();
    this.currentTarget.setContext(context.get(0));
    this.currentTarget.setElement(context.get(1));
    this.currentTarget.addParameter(new StructureMap.StructureMapGroupRuleTargetParameterComponent(new IdType(variable)));
    this.currentTarget.addParameter(new StructureMap.StructureMapGroupRuleTargetParameterComponent(new UriType(mapUri.toString())));
    this.currentTarget.addParameter(new StructureMap.StructureMapGroupRuleTargetParameterComponent(new IdType(outputType.getValue())));
    if (targetVariable != null){
      this.currentTarget.setVariable(targetVariable);
    }
    this.currentTarget.setTransform(StructureMap.StructureMapTransform.TRANSLATE);
    this.ruleComponentStack.peek().addTarget(this.currentTarget);
  }

  /**
  * Populates a target for a Translate transform, adds it to the top rule on the stack
  * @param context Target context
  * @param variable variable to be truncated
  * @param length length of string to truncate to
  * @param targetVariable Variable that will hold the result of the transform, may be left null
  * @throws Exception if it fails to populate correctly
  */
  @Override
  public void transformTruncate(List<String> context, String variable, int length, String targetVariable) throws Exception {
    this.currentTarget = new StructureMap.StructureMapGroupRuleTargetComponent();
    this.currentTarget.setContext(context.get(0));
    this.currentTarget.setElement(context.get(1));
    this.currentTarget.addParameter(new StructureMap.StructureMapGroupRuleTargetParameterComponent(new IdType(variable)));
    this.currentTarget.addParameter(new StructureMap.StructureMapGroupRuleTargetParameterComponent(new IntegerType(length)));
    if (targetVariable != null){
      this.currentTarget.setVariable(targetVariable);
    }
    this.currentTarget.setTransform(StructureMap.StructureMapTransform.TRUNCATE);
    this.ruleComponentStack.peek().addTarget(this.currentTarget);

  }

  /**
  * Populates a target for a UUID transform, adds it to the top rule on the stack
  * @param context Target context
  * @param targetVariable Variable that will hold the result of the transform, may be left null
  * @throws Exception if it fails to populate correctly
  */
  @Override
  public void transformUuid(List<String> context, String targetVariable) throws Exception {
    this.currentTarget = new StructureMap.StructureMapGroupRuleTargetComponent();
    this.currentTarget.setContext(context.get(0));
    this.currentTarget.setElement(context.get(1));
    if (targetVariable != null){
      this.currentTarget.setVariable(targetVariable);
    }
    this.currentTarget.setTransform(StructureMap.StructureMapTransform.UUID);
    this.ruleComponentStack.peek().addTarget(this.currentTarget);
  }

  /**
  * Signals to complete the rule, add it to its respective parent, and remove it from the stack.
  * @throws Exception if it fails to populate correctly
  */
  @Override
  public void ruleComplete() throws Exception {
    StructureMap.StructureMapGroupRuleComponent rule = this.ruleComponentStack.pop(); //resolve a rule from the stack
    if (this.ruleComponentStack.empty()) {
      this.currentGroup.addRule(rule);
    }
    else {
      this.ruleComponentStack.peek().addRule(rule);
    }
  }
}
