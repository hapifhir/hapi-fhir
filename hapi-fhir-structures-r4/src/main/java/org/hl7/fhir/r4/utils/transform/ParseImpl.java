package org.hl7.fhir.r4.utils.transform;

import org.eclipse.sisu.Nullable;
import org.hl7.fhir.r4.model.*;
import org.hl7.fhir.r4.utils.transform.deserializer.*;

import java.util.List;

public class ParseImpl implements IFhirMapExecutor {

  public StructureMap structureMap = new StructureMap();
  StructureMap.StructureMapGroupComponent currentGroup;
  StructureMap.StructureMapGroupRuleComponent currentRule;
  StructureMap.StructureMapGroupInputComponent currentInput;
  StructureMap.StructureMapGroupRuleSourceComponent currentSource;
  StructureMap.StructureMapGroupRuleTargetComponent currentTarget;

  @Override
  public void map(UrlData structureMap, String name) throws Exception {
    this.structureMap = new StructureMap();
    this.structureMap.setUrl(structureMap.toString());
    this.structureMap.setName(name);
  }

  @Override
  public void uses(UrlData structureDefinition, FhirMapUseNames name) throws Exception {
    StructureMap.StructureMapStructureComponent structureComponent = new StructureMap.StructureMapStructureComponent();
    structureComponent.setUrl(structureDefinition.toString());
    structureComponent.setMode(StructureMap.StructureMapModelMode.fromCode(name.getValue()));
  }

  @Override
  public void imports(UrlData structureMap) throws Exception {
    this.structureMap.addImport(structureMap.toString());
  }

  @Override
  public void groupStart(String groupName, FhirMapGroupTypes groupType, String groupExtendName) throws Exception {
    this.currentGroup =  new StructureMap.StructureMapGroupComponent();
    this.currentGroup.setName(groupName);
    this.currentGroup.setTypeMode(StructureMap.StructureMapGroupTypeMode.fromCode(groupType.getValue()));
    this.currentGroup.setExtends(groupExtendName);
  }

  @Override
  public void groupEnd() throws Exception {
    this.structureMap.addGroup(this.currentGroup);
  }

  @Override
  public void groupInput(String name, String type, FhirMapInputModes mode) throws Exception {
    this.currentInput = new StructureMap.StructureMapGroupInputComponent();
    this.currentInput.setName(name);
    this.currentInput.setType(type);
    this.currentInput.setMode(StructureMap.StructureMapInputMode.fromCode(mode.getValue()));
    this.currentGroup.addInput(this.currentInput);
  }

  @Override
  public void ruleStart(List<String> ruleName) throws Exception {
    this.currentRule = new StructureMap.StructureMapGroupRuleComponent();
    String name = "";
    for (String s : ruleName){
      if (name.length()==0){
        name = s;
      }
      else {
        name += "."+s;
      }
    }
    this.currentRule.setName(name);
  }

  @Override
  public void ruleSource(List<String> context, @Nullable FhirMapRuleType type, @Nullable String defaultValue, @Nullable FhirMapListOptions listOptions, @Nullable String variable, @Nullable String wherePath, @Nullable String checkPath) throws Exception {
    this.currentSource = new StructureMap.StructureMapGroupRuleSourceComponent();

    this.currentSource.setContext(context.get(0));
    if(context.size() == 2)
      this.currentSource.setElement(context.get(1));
    if (type != null)
      this.currentSource.setType(type.TypeName);
    if (defaultValue != null)
      this.currentSource.setDefaultValue(new StringType(defaultValue));
/*    if (listOptions != null)
      this.currentSource.setListMode()*/
    if (variable != null)
      this.currentSource.setVariable(variable);
    if (wherePath != null)
      this.currentSource.setCondition(wherePath);
    if (checkPath != null)
      this.currentSource.setCheck(checkPath);
    this.currentRule.addSource(this.currentSource);
  }

  @Override
  public void transformAs(List<String> context, String targetVariable) throws Exception {
    this.currentTarget = new StructureMap.StructureMapGroupRuleTargetComponent();

    this.currentTarget.setContext(context.get(0));
    this.currentTarget.setElement(context.get(1));

    this.currentTarget.setVariable(targetVariable);
    //this.currentTarget.setTransform(StructureMap.StructureMapTransfor);

    this.currentRule.addTarget(this.currentTarget);
  }

  @Override
  public void transformAppend(List<String> context, List<String> appendVariables, String targetVariable) throws Exception {
    this.currentTarget = new StructureMap.StructureMapGroupRuleTargetComponent();
    String ctx = "";
    for (String s : context){
      if (s.length()==0){
        ctx = s;
      }
      else {
        ctx += "."+s;
      }
    }
    this.currentSource.setContext(ctx);

    for (String appendVar : appendVariables){
      StructureMap.StructureMapGroupRuleTargetParameterComponent param = new StructureMap.StructureMapGroupRuleTargetParameterComponent();
      param.setValue(new StringType(appendVar));
      this.currentTarget.addParameter(param);
    }
    this.currentTarget.setElement(targetVariable);
    this.currentTarget.setTransform(StructureMap.StructureMapTransform.APPEND);

    this.currentRule.addTarget(this.currentTarget);
  }

  @Override
  public void transformCast(List<String> context, String sourceVariable, String typeName, String targetVariable) throws Exception {
    this.currentTarget = new StructureMap.StructureMapGroupRuleTargetComponent();
    String ctx = "";
    for (String s : context){
      if (s.length()==0){
        ctx = s;
      }
      else {
        ctx += "."+s;
      }
    }
    this.currentSource.setContext(ctx);

    this.currentTarget.addParameter(new StructureMap.StructureMapGroupRuleTargetParameterComponent(new IdType(sourceVariable)));
    this.currentTarget.addParameter(new StructureMap.StructureMapGroupRuleTargetParameterComponent(new IdType(typeName)));
    this.currentTarget.setElement(targetVariable);
    this.currentTarget.setTransform(StructureMap.StructureMapTransform.CAST);

    this.currentRule.addTarget(this.currentTarget);
  }

  @Override
  public void transformCoding(List<String> context, UrlData system, String code, String display, String targetVariable) throws Exception {
    this.currentTarget = new StructureMap.StructureMapGroupRuleTargetComponent();

    this.currentTarget.setContext(context.get(0));
    this.currentTarget.setElement(context.get(1));


    this.currentTarget.addParameter(new StructureMap.StructureMapGroupRuleTargetParameterComponent(new StringType(system.toString())));
    this.currentTarget.addParameter(new StructureMap.StructureMapGroupRuleTargetParameterComponent(new StringType(code)));
    if (targetVariable != null)
      this.currentTarget.addParameter(new StructureMap.StructureMapGroupRuleTargetParameterComponent(new StringType(display)));
    this.currentTarget.setElement(targetVariable);
    this.currentTarget.setTransform(StructureMap.StructureMapTransform.C);

    this.currentRule.addTarget(this.currentTarget);
  }

  @Override
  public void transformCodeableConcept(List<String> context, UrlData system, String code, String display, String targetVariable) throws Exception {
    this.currentTarget = new StructureMap.StructureMapGroupRuleTargetComponent();

    this.currentTarget.setContext(context.get(0));
    this.currentTarget.setElement(context.get(1));

    this.currentTarget.addParameter(new StructureMap.StructureMapGroupRuleTargetParameterComponent(new StringType(system.toString())));
    this.currentTarget.addParameter(new StructureMap.StructureMapGroupRuleTargetParameterComponent(new StringType(code)));
    if (targetVariable != null)
      this.currentTarget.addParameter(new StructureMap.StructureMapGroupRuleTargetParameterComponent(new StringType(display)));
    this.currentTarget.setElement(targetVariable);
    this.currentTarget.setTransform(StructureMap.StructureMapTransform.CC);

    this.currentRule.addTarget(this.currentTarget);
  }

  @Override
  public void transformCodeableConcept(List<String> context, String text, String targetVariable) throws Exception {
    this.currentTarget = new StructureMap.StructureMapGroupRuleTargetComponent();

    this.currentTarget.setContext(context.get(0));
    this.currentTarget.setElement(context.get(1));

    this.currentTarget.addParameter(new StructureMap.StructureMapGroupRuleTargetParameterComponent(new StringType(text)));
    this.currentTarget.setElement(targetVariable);
    this.currentTarget.setTransform(StructureMap.StructureMapTransform.CC);

    this.currentRule.addTarget(this.currentTarget);

  }

  @Override
  public void transformCopy(List<String> context, String copyVariable, String targetVariable) throws Exception {
    this.currentTarget = new StructureMap.StructureMapGroupRuleTargetComponent();
    this.currentTarget.setContext(context.get(0));
    this.currentTarget.setElement(context.get(1));

    this.currentTarget.addParameter(new StructureMap.StructureMapGroupRuleTargetParameterComponent(new IdType(copyVariable)));
    this.currentTarget.setElement(targetVariable);
    this.currentTarget.setTransform(StructureMap.StructureMapTransform.COPY);

    this.currentRule.addTarget(this.currentTarget);
  }

  @Override
  public void transformCp(List<String> context, @Nullable UrlData system, String cpVariable, String targetVariable) throws Exception {
    this.currentTarget = new StructureMap.StructureMapGroupRuleTargetComponent();
    this.currentTarget.setContext(context.get(0));
    this.currentTarget.setElement(context.get(1));
    if (system != null){
      this.currentTarget.addParameter(new StructureMap.StructureMapGroupRuleTargetParameterComponent(new StringType(system.toString())));
    }
    this.currentTarget.addParameter(new StructureMap.StructureMapGroupRuleTargetParameterComponent(new StringType(cpVariable)));
    this.currentTarget.setElement(targetVariable);
    this.currentTarget.setTransform(StructureMap.StructureMapTransform.CP);

    this.currentRule.addTarget(this.currentTarget);

  }

  @Override
  public void transformCreate(List<String> context, String createVariable, String targetVariable) throws Exception {
    this.currentTarget = new StructureMap.StructureMapGroupRuleTargetComponent();
    if (context!= null){
      this.currentTarget.setContext(context.get(0));
      if (context.size() == 2)
        this.currentTarget.setElement(context.get(1));
    }

    this.currentTarget.setTransform(StructureMap.StructureMapTransform.CREATE);
    this.currentTarget.addParameter(new StructureMap.StructureMapGroupRuleTargetParameterComponent(new StringType(createVariable)));
    this.currentTarget.setElement(targetVariable);

    this.currentRule.addTarget(this.currentTarget);

  }

  @Override
  public void transformDateOp(List<String> context, String variable, String operation, String variable2, String targetVariable) throws Exception {
    this.currentTarget = new StructureMap.StructureMapGroupRuleTargetComponent();
    this.currentTarget.setContext(context.get(0));
    this.currentTarget.setElement(context.get(1));
    this.currentTarget.addParameter(new StructureMap.StructureMapGroupRuleTargetParameterComponent(new IdType(variable)));
    this.currentTarget.addParameter(new StructureMap.StructureMapGroupRuleTargetParameterComponent(new IdType(operation)));
    this.currentTarget.addParameter(new StructureMap.StructureMapGroupRuleTargetParameterComponent(new IdType(variable2)));
    this.currentTarget.setElement(targetVariable);
    this.currentTarget.setTransform(StructureMap.StructureMapTransform.DATEOP);

    this.currentRule.addTarget(this.currentTarget);

  }

  @Override
  public void transformEscape(List<String> context, String variable, String string1, String string2, String targetVariable) throws Exception {
    this.currentTarget = new StructureMap.StructureMapGroupRuleTargetComponent();
    this.currentTarget.setContext(context.get(0));
    this.currentTarget.setElement(context.get(1));
    this.currentTarget.addParameter(new StructureMap.StructureMapGroupRuleTargetParameterComponent(new IdType(variable)));
    this.currentTarget.addParameter(new StructureMap.StructureMapGroupRuleTargetParameterComponent(new StringType(string1)));
    this.currentTarget.addParameter(new StructureMap.StructureMapGroupRuleTargetParameterComponent(new StringType(string2)));
    this.currentTarget.setElement(targetVariable);
    this.currentTarget.setTransform(StructureMap.StructureMapTransform.ESCAPE);

    this.currentRule.addTarget(this.currentTarget);

  }

  @Override
  public void transformEvaluate(List<String> context, String obj, String objElement, String targetVariable) throws Exception {
    this.currentTarget = new StructureMap.StructureMapGroupRuleTargetComponent();
    this.currentTarget.setContext(context.get(0));
    this.currentTarget.setElement(context.get(1));
    this.currentTarget.addParameter(new StructureMap.StructureMapGroupRuleTargetParameterComponent(new IdType(objElement)));
    this.currentTarget.setElement(targetVariable);
    this.currentTarget.setTransform(StructureMap.StructureMapTransform.EVALUATE);

    this.currentRule.addTarget(this.currentTarget);
  }

  @Override
  public void transformId(List<String> context, UrlData system, String value, String type, String targetVariable) throws Exception {
    this.currentTarget = new StructureMap.StructureMapGroupRuleTargetComponent();
    this.currentTarget.setContext(context.get(0));
    this.currentTarget.setElement(context.get(1));
    this.currentTarget.addParameter(new StructureMap.StructureMapGroupRuleTargetParameterComponent(new UriType(system.toString())));
    this.currentTarget.addParameter(new StructureMap.StructureMapGroupRuleTargetParameterComponent(new IdType(value)));
    this.currentTarget.addParameter(new StructureMap.StructureMapGroupRuleTargetParameterComponent(new IdType(type)));
    this.currentTarget.setElement(targetVariable);
    this.currentTarget.setTransform(StructureMap.StructureMapTransform.ID);

    this.currentRule.addTarget(this.currentTarget);


  }

  @Override
  public void transformPointer(List<String> context, String resource, String targetVariable) throws Exception {
    this.currentTarget = new StructureMap.StructureMapGroupRuleTargetComponent();
    this.currentTarget.setContext(context.get(0));
    this.currentTarget.setElement(context.get(1));
    this.currentTarget.addParameter(new StructureMap.StructureMapGroupRuleTargetParameterComponent(new IdType(resource)));
    this.currentTarget.setTransform(StructureMap.StructureMapTransform.POINTER);
    this.currentTarget.setElement(targetVariable);

    this.currentRule.addTarget(this.currentTarget);


  }

  @Override
  public void transformQty(List<String> context, String text, String targetVariable) throws Exception {
    this.currentTarget = new StructureMap.StructureMapGroupRuleTargetComponent();
    this.currentTarget.setContext(context.get(0));
    this.currentTarget.setElement(context.get(1));
    this.currentTarget.addParameter(new StructureMap.StructureMapGroupRuleTargetParameterComponent(new StringType(text)));
    this.currentTarget.setTransform(StructureMap.StructureMapTransform.QTY);
    this.currentTarget.setElement(targetVariable);

    this.currentRule.addTarget(this.currentTarget);

  }

  @Override
  public void transformQty(List<String> context, String value, String unitString, UrlData system, String targetVariable) throws Exception {
    this.currentTarget = new StructureMap.StructureMapGroupRuleTargetComponent();
    this.currentTarget.setContext(context.get(0));
    this.currentTarget.setElement(context.get(1));
    this.currentTarget.addParameter(new StructureMap.StructureMapGroupRuleTargetParameterComponent(new IdType(value)));
    this.currentTarget.addParameter(new StructureMap.StructureMapGroupRuleTargetParameterComponent(new StringType(unitString)));
    this.currentTarget.addParameter(new StructureMap.StructureMapGroupRuleTargetParameterComponent(new UriType(system.toString())));
    this.currentTarget.setElement(targetVariable);

    this.currentTarget.setTransform(StructureMap.StructureMapTransform.QTY);
    this.currentRule.addTarget(this.currentTarget);

  }

  @Override
  public void transformQty(List<String> context, String value, String unitString, String type, String targetVariable) throws Exception {
    this.currentTarget = new StructureMap.StructureMapGroupRuleTargetComponent();
    this.currentTarget.setContext(context.get(0));
    this.currentTarget.setElement(context.get(1));
    this.currentTarget.setTransform(StructureMap.StructureMapTransform.QTY);
    this.currentTarget.addParameter(new StructureMap.StructureMapGroupRuleTargetParameterComponent(new IdType(value)));
    this.currentTarget.addParameter(new StructureMap.StructureMapGroupRuleTargetParameterComponent(new StringType(unitString)));
    this.currentTarget.addParameter(new StructureMap.StructureMapGroupRuleTargetParameterComponent(new IdType(type)));
    this.currentTarget.setElement(targetVariable);
    this.currentRule.addTarget(this.currentTarget);


  }

  @Override
  public void transformReference(List<String> context, String text, String targetVariable) throws Exception {
    this.currentTarget = new StructureMap.StructureMapGroupRuleTargetComponent();
    this.currentTarget.setContext(context.get(0));
    this.currentTarget.setElement(context.get(1));
    this.currentTarget.addParameter(new StructureMap.StructureMapGroupRuleTargetParameterComponent(new StringType(text)));
    this.currentTarget.setElement(targetVariable);
    this.currentTarget.setTransform(StructureMap.StructureMapTransform.REFERENCE);

    this.currentRule.addTarget(this.currentTarget);

  }

  @Override
  public void transformTranslate(List<String> context, String variable, UrlData mapUri, FhirMapTranslateOutputTypes outputType, String targetVariable) throws Exception {
    this.currentTarget = new StructureMap.StructureMapGroupRuleTargetComponent();
    this.currentTarget.setContext(context.get(0));
    this.currentTarget.setElement(context.get(1));
    this.currentTarget.addParameter(new StructureMap.StructureMapGroupRuleTargetParameterComponent(new IdType(variable)));
    this.currentTarget.addParameter(new StructureMap.StructureMapGroupRuleTargetParameterComponent(new UriType(mapUri.toString())));
    this.currentTarget.addParameter(new StructureMap.StructureMapGroupRuleTargetParameterComponent(new IdType(outputType.getValue())));
    this.currentTarget.setElement(targetVariable);
    this.currentTarget.setTransform(StructureMap.StructureMapTransform.TRANSLATE);

    this.currentRule.addTarget(this.currentTarget);
  }

  @Override
  public void transformTruncate(List<String> context, String variable, int length, String targetVariable) throws Exception {
    this.currentTarget = new StructureMap.StructureMapGroupRuleTargetComponent();
    this.currentTarget.setContext(context.get(0));
    this.currentTarget.setElement(context.get(1));

    this.currentTarget.addParameter(new StructureMap.StructureMapGroupRuleTargetParameterComponent(new IdType(variable)));
    this.currentTarget.addParameter(new StructureMap.StructureMapGroupRuleTargetParameterComponent(new IntegerType(length)));
    this.currentTarget.setElement(targetVariable);
    this.currentTarget.setTransform(StructureMap.StructureMapTransform.TRUNCATE);

    this.currentRule.addTarget(this.currentTarget);

  }

  @Override
  public void transformUuid(List<String> context, String targetVariable) throws Exception {
    this.currentTarget = new StructureMap.StructureMapGroupRuleTargetComponent();
    this.currentTarget.setContext(context.get(0));
    this.currentTarget.setElement(context.get(1));
    this.currentTarget.setElement(targetVariable);
    this.currentTarget.setTransform(StructureMap.StructureMapTransform.UUID);

    this.currentRule.addTarget(this.currentTarget);
  }

  @Override
  public void ruleComplete() throws Exception {
    this.currentGroup.addRule(this.currentRule);
  }
}
