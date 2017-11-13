// Generated from FhirMapJava.g4 by ANTLR 4.7
package org.hl7.fhir.r4.utils.transform.deserializer.grammar.antlr.javaAntlr;

import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link FhirMapParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 *            operations with no return type.
 */
public interface FhirMapVisitor<T> extends ParseTreeVisitor<T> {
  /**
   * Visit a parse tree produced by {@link FhirMapParser#mappingUnit}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitMappingUnit(FhirMapParser.MappingUnitContext ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#keyMap}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitKeyMap(FhirMapParser.KeyMapContext ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#keyUses}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitKeyUses(FhirMapParser.KeyUsesContext ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#keyUsesName}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitKeyUsesName(FhirMapParser.KeyUsesNameContext ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#keyUsesNameSource}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitKeyUsesNameSource(FhirMapParser.KeyUsesNameSourceContext ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#keyUsesNameTarget}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitKeyUsesNameTarget(FhirMapParser.KeyUsesNameTargetContext ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#keyUsesNameQueried}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitKeyUsesNameQueried(FhirMapParser.KeyUsesNameQueriedContext ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#keyUsesNameProduced}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitKeyUsesNameProduced(FhirMapParser.KeyUsesNameProducedContext ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#keyImports}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitKeyImports(FhirMapParser.KeyImportsContext ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#group}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitGroup(FhirMapParser.GroupContext ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#groupStart}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitGroupStart(FhirMapParser.GroupStartContext ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#groupExtends}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitGroupExtends(FhirMapParser.GroupExtendsContext ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#groupEnd}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitGroupEnd(FhirMapParser.GroupEndContext ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#groupType}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitGroupType(FhirMapParser.GroupTypeContext ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#groupTypeValue}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitGroupTypeValue(FhirMapParser.GroupTypeValueContext ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#groupTypeType}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitGroupTypeType(FhirMapParser.GroupTypeTypeContext ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#groupTypeTypeTypes}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitGroupTypeTypeTypes(FhirMapParser.GroupTypeTypeTypesContext ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#groupInput}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitGroupInput(FhirMapParser.GroupInputContext ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#groupInputName}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitGroupInputName(FhirMapParser.GroupInputNameContext ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#groupInputType}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitGroupInputType(FhirMapParser.GroupInputTypeContext ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#groupInputMode}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitGroupInputMode(FhirMapParser.GroupInputModeContext ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#groupInputModes}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitGroupInputModes(FhirMapParser.GroupInputModesContext ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#groupInputModesSource}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitGroupInputModesSource(FhirMapParser.GroupInputModesSourceContext ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#groupInputModesTarget}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitGroupInputModesTarget(FhirMapParser.GroupInputModesTargetContext ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#groupItem}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitGroupItem(FhirMapParser.GroupItemContext ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#groupCall}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitGroupCall(FhirMapParser.GroupCallContext ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#groupCallParameters}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitGroupCallParameters(FhirMapParser.GroupCallParametersContext ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#ruleInstance}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitRuleInstance(FhirMapParser.RuleInstanceContext ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#ruleName}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitRuleName(FhirMapParser.RuleNameContext ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#ruleSources}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitRuleSources(FhirMapParser.RuleSourcesContext ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#ruleSource}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitRuleSource(FhirMapParser.RuleSourceContext ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#ruleContext}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitRuleContext(FhirMapParser.RuleContextContext ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#ruleContextElement}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitRuleContextElement(FhirMapParser.RuleContextElementContext ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#ruleType}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitRuleType(FhirMapParser.RuleTypeContext ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#ruleDefault}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitRuleDefault(FhirMapParser.RuleDefaultContext ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#ruleListOption}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitRuleListOption(FhirMapParser.RuleListOptionContext ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#ruleVariable}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitRuleVariable(FhirMapParser.RuleVariableContext ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#ruleWherePath}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitRuleWherePath(FhirMapParser.RuleWherePathContext ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#ruleCheckPath}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitRuleCheckPath(FhirMapParser.RuleCheckPathContext ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#ruleMake}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitRuleMake(FhirMapParser.RuleMakeContext ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#ruleMakeTargets}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitRuleMakeTargets(FhirMapParser.RuleMakeTargetsContext ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#ruleMakeDependents}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitRuleMakeDependents(FhirMapParser.RuleMakeDependentsContext ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#ruleMakeDependentsGroupItems}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitRuleMakeDependentsGroupItems(FhirMapParser.RuleMakeDependentsGroupItemsContext ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#ruleTarget}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitRuleTarget(FhirMapParser.RuleTargetContext ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#ruleTargetContext}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitRuleTargetContext(FhirMapParser.RuleTargetContextContext ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#ruleTargetAs}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitRuleTargetAs(FhirMapParser.RuleTargetAsContext ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#ruleTargetAssign}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitRuleTargetAssign(FhirMapParser.RuleTargetAssignContext ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#ruleTargetAssignValue}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitRuleTargetAssignValue(FhirMapParser.RuleTargetAssignValueContext ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#ruleTargetAppend}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitRuleTargetAppend(FhirMapParser.RuleTargetAppendContext ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#ruleTargetAppendSources}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitRuleTargetAppendSources(FhirMapParser.RuleTargetAppendSourcesContext ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#ruleTargetAppendSource}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitRuleTargetAppendSource(FhirMapParser.RuleTargetAppendSourceContext ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#ruleTargetC}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitRuleTargetC(FhirMapParser.RuleTargetCContext ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#ruleTargetCSystem}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitRuleTargetCSystem(FhirMapParser.RuleTargetCSystemContext ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#ruleTargetCCode}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitRuleTargetCCode(FhirMapParser.RuleTargetCCodeContext ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#ruleTargetCDisplay}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitRuleTargetCDisplay(FhirMapParser.RuleTargetCDisplayContext ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#ruleTargetCast}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitRuleTargetCast(FhirMapParser.RuleTargetCastContext ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#ruleTargetCastSource}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitRuleTargetCastSource(FhirMapParser.RuleTargetCastSourceContext ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#ruleTargetCastType}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitRuleTargetCastType(FhirMapParser.RuleTargetCastTypeContext ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#ruleTargetCC}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitRuleTargetCC(FhirMapParser.RuleTargetCCContext ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#ruleTargetCC1}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitRuleTargetCC1(FhirMapParser.RuleTargetCC1Context ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#ruleTargetCC1Text}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitRuleTargetCC1Text(FhirMapParser.RuleTargetCC1TextContext ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#ruleTargetCC2}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitRuleTargetCC2(FhirMapParser.RuleTargetCC2Context ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#ruleTargetCC2System}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitRuleTargetCC2System(FhirMapParser.RuleTargetCC2SystemContext ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#ruleTargetCC2Code}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitRuleTargetCC2Code(FhirMapParser.RuleTargetCC2CodeContext ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#ruleTargetCC2Display}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitRuleTargetCC2Display(FhirMapParser.RuleTargetCC2DisplayContext ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#ruleTargetCp}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitRuleTargetCp(FhirMapParser.RuleTargetCpContext ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#ruleTargetCpSystem}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitRuleTargetCpSystem(FhirMapParser.RuleTargetCpSystemContext ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#ruleTargetCpVariable}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitRuleTargetCpVariable(FhirMapParser.RuleTargetCpVariableContext ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#ruleTargetCopy}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitRuleTargetCopy(FhirMapParser.RuleTargetCopyContext ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#ruleTargetCopySource}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitRuleTargetCopySource(FhirMapParser.RuleTargetCopySourceContext ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#ruleTargetCreate}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitRuleTargetCreate(FhirMapParser.RuleTargetCreateContext ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#ruleTargetCreateType}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitRuleTargetCreateType(FhirMapParser.RuleTargetCreateTypeContext ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#ruleTargetDateOp}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitRuleTargetDateOp(FhirMapParser.RuleTargetDateOpContext ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#ruleTargetDateOpVariable}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitRuleTargetDateOpVariable(FhirMapParser.RuleTargetDateOpVariableContext ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#ruleTargetDateOpOperation}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitRuleTargetDateOpOperation(FhirMapParser.RuleTargetDateOpOperationContext ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#ruleTargetDateOpVariable2}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitRuleTargetDateOpVariable2(FhirMapParser.RuleTargetDateOpVariable2Context ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#ruleTargetExtension}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitRuleTargetExtension(FhirMapParser.RuleTargetExtensionContext ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#ruleTargetExtension1}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitRuleTargetExtension1(FhirMapParser.RuleTargetExtension1Context ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#ruleTargetExtension2}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitRuleTargetExtension2(FhirMapParser.RuleTargetExtension2Context ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#ruleTargetExtension2Uri}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitRuleTargetExtension2Uri(FhirMapParser.RuleTargetExtension2UriContext ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#ruleTargetExtension2Title}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitRuleTargetExtension2Title(FhirMapParser.RuleTargetExtension2TitleContext ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#ruleTargetExtension2Mode}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitRuleTargetExtension2Mode(FhirMapParser.RuleTargetExtension2ModeContext ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#ruleTargetExtension2Parent}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitRuleTargetExtension2Parent(FhirMapParser.RuleTargetExtension2ParentContext ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#ruleTargetExtension2Text1}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitRuleTargetExtension2Text1(FhirMapParser.RuleTargetExtension2Text1Context ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#ruleTargetExtension2Text2}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitRuleTargetExtension2Text2(FhirMapParser.RuleTargetExtension2Text2Context ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#ruleTargetExtension2Min}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitRuleTargetExtension2Min(FhirMapParser.RuleTargetExtension2MinContext ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#ruleTargetExtension2Max}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitRuleTargetExtension2Max(FhirMapParser.RuleTargetExtension2MaxContext ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#ruleTargetExtension2Type}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitRuleTargetExtension2Type(FhirMapParser.RuleTargetExtension2TypeContext ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#ruleTargetEscape}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitRuleTargetEscape(FhirMapParser.RuleTargetEscapeContext ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#ruleTargetEscapeVariable}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitRuleTargetEscapeVariable(FhirMapParser.RuleTargetEscapeVariableContext ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#ruleTargetEscapeString1}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitRuleTargetEscapeString1(FhirMapParser.RuleTargetEscapeString1Context ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#ruleTargetEscapeString2}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitRuleTargetEscapeString2(FhirMapParser.RuleTargetEscapeString2Context ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#ruleTargetEvaluate}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitRuleTargetEvaluate(FhirMapParser.RuleTargetEvaluateContext ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#ruleTargetEvaluateObject}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitRuleTargetEvaluateObject(FhirMapParser.RuleTargetEvaluateObjectContext ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#ruleTargetEvaluateObjectElement}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitRuleTargetEvaluateObjectElement(FhirMapParser.RuleTargetEvaluateObjectElementContext ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#ruleTargetId}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitRuleTargetId(FhirMapParser.RuleTargetIdContext ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#ruleTargetIdSystem}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitRuleTargetIdSystem(FhirMapParser.RuleTargetIdSystemContext ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#ruleTargetIdValue}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitRuleTargetIdValue(FhirMapParser.RuleTargetIdValueContext ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#ruleTargetIdType}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitRuleTargetIdType(FhirMapParser.RuleTargetIdTypeContext ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#ruleTargetPointer}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitRuleTargetPointer(FhirMapParser.RuleTargetPointerContext ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#ruleTargetPointerResource}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitRuleTargetPointerResource(FhirMapParser.RuleTargetPointerResourceContext ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#ruleTargetQty}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitRuleTargetQty(FhirMapParser.RuleTargetQtyContext ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#ruleTargetQty1}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitRuleTargetQty1(FhirMapParser.RuleTargetQty1Context ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#ruleTargetQty1Text}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitRuleTargetQty1Text(FhirMapParser.RuleTargetQty1TextContext ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#ruleTargetQty2}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitRuleTargetQty2(FhirMapParser.RuleTargetQty2Context ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#ruleTargetQty2Value}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitRuleTargetQty2Value(FhirMapParser.RuleTargetQty2ValueContext ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#ruleTargetQty2UnitString}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitRuleTargetQty2UnitString(FhirMapParser.RuleTargetQty2UnitStringContext ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#ruleTargetQty2System}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitRuleTargetQty2System(FhirMapParser.RuleTargetQty2SystemContext ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#ruleTargetQty3}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitRuleTargetQty3(FhirMapParser.RuleTargetQty3Context ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#ruleTargetQty3Value}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitRuleTargetQty3Value(FhirMapParser.RuleTargetQty3ValueContext ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#ruleTargetQty3UnitString}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitRuleTargetQty3UnitString(FhirMapParser.RuleTargetQty3UnitStringContext ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#ruleTargetQty3CodeVariable}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitRuleTargetQty3CodeVariable(FhirMapParser.RuleTargetQty3CodeVariableContext ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#ruleTargetReference}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitRuleTargetReference(FhirMapParser.RuleTargetReferenceContext ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#ruleTargetReferenceSource}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitRuleTargetReferenceSource(FhirMapParser.RuleTargetReferenceSourceContext ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#ruleTargetTranslate}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitRuleTargetTranslate(FhirMapParser.RuleTargetTranslateContext ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#ruleTargetTranslateSource}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitRuleTargetTranslateSource(FhirMapParser.RuleTargetTranslateSourceContext ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#ruleTargetTranslateMap}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitRuleTargetTranslateMap(FhirMapParser.RuleTargetTranslateMapContext ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#ruleTargetTranslateOutput}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitRuleTargetTranslateOutput(FhirMapParser.RuleTargetTranslateOutputContext ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#ruleTargetTranslateOutputCode}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitRuleTargetTranslateOutputCode(FhirMapParser.RuleTargetTranslateOutputCodeContext ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#ruleTargetTranslateOutputSystem}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitRuleTargetTranslateOutputSystem(FhirMapParser.RuleTargetTranslateOutputSystemContext ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#ruleTargetTranslateOutputDisplay}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitRuleTargetTranslateOutputDisplay(FhirMapParser.RuleTargetTranslateOutputDisplayContext ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#ruleTargetTranslateOutputCoding}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitRuleTargetTranslateOutputCoding(FhirMapParser.RuleTargetTranslateOutputCodingContext ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#ruleTargetTranslateOutputCodeableConcept}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitRuleTargetTranslateOutputCodeableConcept(FhirMapParser.RuleTargetTranslateOutputCodeableConceptContext ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#ruleTargetTruncate}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitRuleTargetTruncate(FhirMapParser.RuleTargetTruncateContext ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#ruleTargetTruncateSource}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitRuleTargetTruncateSource(FhirMapParser.RuleTargetTruncateSourceContext ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#ruleTargetTruncateLength}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitRuleTargetTruncateLength(FhirMapParser.RuleTargetTruncateLengthContext ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#ruleTargetUuid}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitRuleTargetUuid(FhirMapParser.RuleTargetUuidContext ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#ruleTargetVariable}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitRuleTargetVariable(FhirMapParser.RuleTargetVariableContext ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#fhirPath}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitFhirPath(FhirMapParser.FhirPathContext ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#identifier}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitIdentifier(FhirMapParser.IdentifierContext ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#integer}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitInteger(FhirMapParser.IntegerContext ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#quotedIdentifier}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitQuotedIdentifier(FhirMapParser.QuotedIdentifierContext ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#quotedStringWQuotes}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitQuotedStringWQuotes(FhirMapParser.QuotedStringWQuotesContext ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#quotedString}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitQuotedString(FhirMapParser.QuotedStringContext ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#quotedUrl}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitQuotedUrl(FhirMapParser.QuotedUrlContext ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#structureDefinition}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitStructureDefinition(FhirMapParser.StructureDefinitionContext ctx);

  /**
   * Visit a parse tree produced by {@link FhirMapParser#structureMap}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitStructureMap(FhirMapParser.StructureMapContext ctx);
}
