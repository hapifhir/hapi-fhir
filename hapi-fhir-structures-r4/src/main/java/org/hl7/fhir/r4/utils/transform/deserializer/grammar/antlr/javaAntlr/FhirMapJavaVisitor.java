// Generated from FhirMapJava.g4 by ANTLR 4.7
package org.hl7.fhir.r4.utils.transform.deserializer.grammar.antlr.javaAntlr;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link FhirMapJavaParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface FhirMapJavaVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#mappingUnit}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMappingUnit(FhirMapJavaParser.MappingUnitContext ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#keyMap}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitKeyMap(FhirMapJavaParser.KeyMapContext ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#keyUses}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitKeyUses(FhirMapJavaParser.KeyUsesContext ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#keyUsesName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitKeyUsesName(FhirMapJavaParser.KeyUsesNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#keyUsesNameSource}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitKeyUsesNameSource(FhirMapJavaParser.KeyUsesNameSourceContext ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#keyUsesNameTarget}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitKeyUsesNameTarget(FhirMapJavaParser.KeyUsesNameTargetContext ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#keyUsesNameQueried}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitKeyUsesNameQueried(FhirMapJavaParser.KeyUsesNameQueriedContext ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#keyUsesNameProduced}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitKeyUsesNameProduced(FhirMapJavaParser.KeyUsesNameProducedContext ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#keyImports}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitKeyImports(FhirMapJavaParser.KeyImportsContext ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#group}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGroup(FhirMapJavaParser.GroupContext ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#groupStart}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGroupStart(FhirMapJavaParser.GroupStartContext ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#groupExtends}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGroupExtends(FhirMapJavaParser.GroupExtendsContext ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#groupEnd}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGroupEnd(FhirMapJavaParser.GroupEndContext ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#groupType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGroupType(FhirMapJavaParser.GroupTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#groupTypeValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGroupTypeValue(FhirMapJavaParser.GroupTypeValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#groupTypeType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGroupTypeType(FhirMapJavaParser.GroupTypeTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#groupTypeTypeTypes}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGroupTypeTypeTypes(FhirMapJavaParser.GroupTypeTypeTypesContext ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#groupInput}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGroupInput(FhirMapJavaParser.GroupInputContext ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#groupInputName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGroupInputName(FhirMapJavaParser.GroupInputNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#groupInputType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGroupInputType(FhirMapJavaParser.GroupInputTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#groupInputMode}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGroupInputMode(FhirMapJavaParser.GroupInputModeContext ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#groupInputModes}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGroupInputModes(FhirMapJavaParser.GroupInputModesContext ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#groupInputModesSource}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGroupInputModesSource(FhirMapJavaParser.GroupInputModesSourceContext ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#groupInputModesTarget}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGroupInputModesTarget(FhirMapJavaParser.GroupInputModesTargetContext ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#groupItem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGroupItem(FhirMapJavaParser.GroupItemContext ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#groupCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGroupCall(FhirMapJavaParser.GroupCallContext ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#groupCallParameters}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGroupCallParameters(FhirMapJavaParser.GroupCallParametersContext ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#ruleInstance}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleInstance(FhirMapJavaParser.RuleInstanceContext ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#ruleName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleName(FhirMapJavaParser.RuleNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#ruleSources}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleSources(FhirMapJavaParser.RuleSourcesContext ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#ruleSource}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleSource(FhirMapJavaParser.RuleSourceContext ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#ruleContext}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleContext(FhirMapJavaParser.RuleContextContext ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#ruleContextElement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleContextElement(FhirMapJavaParser.RuleContextElementContext ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#ruleType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleType(FhirMapJavaParser.RuleTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#ruleDefault}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleDefault(FhirMapJavaParser.RuleDefaultContext ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#ruleListOption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleListOption(FhirMapJavaParser.RuleListOptionContext ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#ruleVariable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleVariable(FhirMapJavaParser.RuleVariableContext ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#ruleWherePath}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleWherePath(FhirMapJavaParser.RuleWherePathContext ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#ruleCheckPath}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleCheckPath(FhirMapJavaParser.RuleCheckPathContext ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#ruleMake}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleMake(FhirMapJavaParser.RuleMakeContext ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#ruleMakeTargets}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleMakeTargets(FhirMapJavaParser.RuleMakeTargetsContext ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#ruleMakeDependents}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleMakeDependents(FhirMapJavaParser.RuleMakeDependentsContext ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#ruleMakeDependentsGroupItems}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleMakeDependentsGroupItems(FhirMapJavaParser.RuleMakeDependentsGroupItemsContext ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#ruleTarget}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleTarget(FhirMapJavaParser.RuleTargetContext ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#ruleTargetContext}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleTargetContext(FhirMapJavaParser.RuleTargetContextContext ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#ruleTargetAs}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleTargetAs(FhirMapJavaParser.RuleTargetAsContext ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#ruleTargetAssign}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleTargetAssign(FhirMapJavaParser.RuleTargetAssignContext ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#ruleTargetAssignValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleTargetAssignValue(FhirMapJavaParser.RuleTargetAssignValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#ruleTargetAppend}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleTargetAppend(FhirMapJavaParser.RuleTargetAppendContext ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#ruleTargetAppendSources}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleTargetAppendSources(FhirMapJavaParser.RuleTargetAppendSourcesContext ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#ruleTargetAppendSource}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleTargetAppendSource(FhirMapJavaParser.RuleTargetAppendSourceContext ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#ruleTargetC}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleTargetC(FhirMapJavaParser.RuleTargetCContext ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#ruleTargetCSystem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleTargetCSystem(FhirMapJavaParser.RuleTargetCSystemContext ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#ruleTargetCCode}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleTargetCCode(FhirMapJavaParser.RuleTargetCCodeContext ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#ruleTargetCDisplay}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleTargetCDisplay(FhirMapJavaParser.RuleTargetCDisplayContext ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#ruleTargetCast}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleTargetCast(FhirMapJavaParser.RuleTargetCastContext ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#ruleTargetCastSource}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleTargetCastSource(FhirMapJavaParser.RuleTargetCastSourceContext ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#ruleTargetCastType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleTargetCastType(FhirMapJavaParser.RuleTargetCastTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#ruleTargetCC}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleTargetCC(FhirMapJavaParser.RuleTargetCCContext ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#ruleTargetCC1}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleTargetCC1(FhirMapJavaParser.RuleTargetCC1Context ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#ruleTargetCC1Text}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleTargetCC1Text(FhirMapJavaParser.RuleTargetCC1TextContext ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#ruleTargetCC2}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleTargetCC2(FhirMapJavaParser.RuleTargetCC2Context ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#ruleTargetCC2System}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleTargetCC2System(FhirMapJavaParser.RuleTargetCC2SystemContext ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#ruleTargetCC2Code}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleTargetCC2Code(FhirMapJavaParser.RuleTargetCC2CodeContext ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#ruleTargetCC2Display}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleTargetCC2Display(FhirMapJavaParser.RuleTargetCC2DisplayContext ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#ruleTargetCp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleTargetCp(FhirMapJavaParser.RuleTargetCpContext ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#ruleTargetCpSystem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleTargetCpSystem(FhirMapJavaParser.RuleTargetCpSystemContext ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#ruleTargetCpVariable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleTargetCpVariable(FhirMapJavaParser.RuleTargetCpVariableContext ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#ruleTargetCopy}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleTargetCopy(FhirMapJavaParser.RuleTargetCopyContext ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#ruleTargetCopySource}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleTargetCopySource(FhirMapJavaParser.RuleTargetCopySourceContext ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#ruleTargetCreate}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleTargetCreate(FhirMapJavaParser.RuleTargetCreateContext ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#ruleTargetCreateType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleTargetCreateType(FhirMapJavaParser.RuleTargetCreateTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#ruleTargetDateOp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleTargetDateOp(FhirMapJavaParser.RuleTargetDateOpContext ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#ruleTargetDateOpVariable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleTargetDateOpVariable(FhirMapJavaParser.RuleTargetDateOpVariableContext ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#ruleTargetDateOpOperation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleTargetDateOpOperation(FhirMapJavaParser.RuleTargetDateOpOperationContext ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#ruleTargetDateOpVariable2}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleTargetDateOpVariable2(FhirMapJavaParser.RuleTargetDateOpVariable2Context ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#ruleTargetExtension}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleTargetExtension(FhirMapJavaParser.RuleTargetExtensionContext ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#ruleTargetExtension1}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleTargetExtension1(FhirMapJavaParser.RuleTargetExtension1Context ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#ruleTargetExtension2}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleTargetExtension2(FhirMapJavaParser.RuleTargetExtension2Context ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#ruleTargetExtension2Uri}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleTargetExtension2Uri(FhirMapJavaParser.RuleTargetExtension2UriContext ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#ruleTargetExtension2Title}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleTargetExtension2Title(FhirMapJavaParser.RuleTargetExtension2TitleContext ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#ruleTargetExtension2Mode}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleTargetExtension2Mode(FhirMapJavaParser.RuleTargetExtension2ModeContext ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#ruleTargetExtension2Parent}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleTargetExtension2Parent(FhirMapJavaParser.RuleTargetExtension2ParentContext ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#ruleTargetExtension2Text1}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleTargetExtension2Text1(FhirMapJavaParser.RuleTargetExtension2Text1Context ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#ruleTargetExtension2Text2}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleTargetExtension2Text2(FhirMapJavaParser.RuleTargetExtension2Text2Context ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#ruleTargetExtension2Min}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleTargetExtension2Min(FhirMapJavaParser.RuleTargetExtension2MinContext ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#ruleTargetExtension2Max}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleTargetExtension2Max(FhirMapJavaParser.RuleTargetExtension2MaxContext ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#ruleTargetExtension2Type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleTargetExtension2Type(FhirMapJavaParser.RuleTargetExtension2TypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#ruleTargetEscape}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleTargetEscape(FhirMapJavaParser.RuleTargetEscapeContext ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#ruleTargetEscapeVariable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleTargetEscapeVariable(FhirMapJavaParser.RuleTargetEscapeVariableContext ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#ruleTargetEscapeString1}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleTargetEscapeString1(FhirMapJavaParser.RuleTargetEscapeString1Context ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#ruleTargetEscapeString2}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleTargetEscapeString2(FhirMapJavaParser.RuleTargetEscapeString2Context ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#ruleTargetEvaluate}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleTargetEvaluate(FhirMapJavaParser.RuleTargetEvaluateContext ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#ruleTargetEvaluateObject}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleTargetEvaluateObject(FhirMapJavaParser.RuleTargetEvaluateObjectContext ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#ruleTargetEvaluateObjectElement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleTargetEvaluateObjectElement(FhirMapJavaParser.RuleTargetEvaluateObjectElementContext ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#ruleTargetId}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleTargetId(FhirMapJavaParser.RuleTargetIdContext ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#ruleTargetIdSystem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleTargetIdSystem(FhirMapJavaParser.RuleTargetIdSystemContext ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#ruleTargetIdValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleTargetIdValue(FhirMapJavaParser.RuleTargetIdValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#ruleTargetIdType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleTargetIdType(FhirMapJavaParser.RuleTargetIdTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#ruleTargetPointer}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleTargetPointer(FhirMapJavaParser.RuleTargetPointerContext ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#ruleTargetPointerResource}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleTargetPointerResource(FhirMapJavaParser.RuleTargetPointerResourceContext ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#ruleTargetQty}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleTargetQty(FhirMapJavaParser.RuleTargetQtyContext ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#ruleTargetQty1}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleTargetQty1(FhirMapJavaParser.RuleTargetQty1Context ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#ruleTargetQty1Text}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleTargetQty1Text(FhirMapJavaParser.RuleTargetQty1TextContext ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#ruleTargetQty2}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleTargetQty2(FhirMapJavaParser.RuleTargetQty2Context ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#ruleTargetQty2Value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleTargetQty2Value(FhirMapJavaParser.RuleTargetQty2ValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#ruleTargetQty2UnitString}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleTargetQty2UnitString(FhirMapJavaParser.RuleTargetQty2UnitStringContext ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#ruleTargetQty2System}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleTargetQty2System(FhirMapJavaParser.RuleTargetQty2SystemContext ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#ruleTargetQty3}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleTargetQty3(FhirMapJavaParser.RuleTargetQty3Context ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#ruleTargetQty3Value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleTargetQty3Value(FhirMapJavaParser.RuleTargetQty3ValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#ruleTargetQty3UnitString}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleTargetQty3UnitString(FhirMapJavaParser.RuleTargetQty3UnitStringContext ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#ruleTargetQty3CodeVariable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleTargetQty3CodeVariable(FhirMapJavaParser.RuleTargetQty3CodeVariableContext ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#ruleTargetReference}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleTargetReference(FhirMapJavaParser.RuleTargetReferenceContext ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#ruleTargetReferenceSource}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleTargetReferenceSource(FhirMapJavaParser.RuleTargetReferenceSourceContext ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#ruleTargetTranslate}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleTargetTranslate(FhirMapJavaParser.RuleTargetTranslateContext ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#ruleTargetTranslateSource}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleTargetTranslateSource(FhirMapJavaParser.RuleTargetTranslateSourceContext ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#ruleTargetTranslateMap}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleTargetTranslateMap(FhirMapJavaParser.RuleTargetTranslateMapContext ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#ruleTargetTranslateOutput}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleTargetTranslateOutput(FhirMapJavaParser.RuleTargetTranslateOutputContext ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#ruleTargetTranslateOutputCode}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleTargetTranslateOutputCode(FhirMapJavaParser.RuleTargetTranslateOutputCodeContext ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#ruleTargetTranslateOutputSystem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleTargetTranslateOutputSystem(FhirMapJavaParser.RuleTargetTranslateOutputSystemContext ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#ruleTargetTranslateOutputDisplay}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleTargetTranslateOutputDisplay(FhirMapJavaParser.RuleTargetTranslateOutputDisplayContext ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#ruleTargetTranslateOutputCoding}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleTargetTranslateOutputCoding(FhirMapJavaParser.RuleTargetTranslateOutputCodingContext ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#ruleTargetTranslateOutputCodeableConcept}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleTargetTranslateOutputCodeableConcept(FhirMapJavaParser.RuleTargetTranslateOutputCodeableConceptContext ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#ruleTargetTruncate}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleTargetTruncate(FhirMapJavaParser.RuleTargetTruncateContext ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#ruleTargetTruncateSource}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleTargetTruncateSource(FhirMapJavaParser.RuleTargetTruncateSourceContext ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#ruleTargetTruncateLength}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleTargetTruncateLength(FhirMapJavaParser.RuleTargetTruncateLengthContext ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#ruleTargetUuid}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleTargetUuid(FhirMapJavaParser.RuleTargetUuidContext ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#ruleTargetVariable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleTargetVariable(FhirMapJavaParser.RuleTargetVariableContext ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#fhirPath}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFhirPath(FhirMapJavaParser.FhirPathContext ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#identifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentifier(FhirMapJavaParser.IdentifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#integer}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInteger(FhirMapJavaParser.IntegerContext ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#quotedIdentifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQuotedIdentifier(FhirMapJavaParser.QuotedIdentifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#quotedStringWQuotes}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQuotedStringWQuotes(FhirMapJavaParser.QuotedStringWQuotesContext ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#quotedString}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQuotedString(FhirMapJavaParser.QuotedStringContext ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#quotedUrl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQuotedUrl(FhirMapJavaParser.QuotedUrlContext ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#structureDefinition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStructureDefinition(FhirMapJavaParser.StructureDefinitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link FhirMapJavaParser#structureMap}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStructureMap(FhirMapJavaParser.StructureMapContext ctx);
}
