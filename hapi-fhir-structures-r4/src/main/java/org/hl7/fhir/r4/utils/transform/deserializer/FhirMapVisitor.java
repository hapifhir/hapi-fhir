//
// Translated by Java2J (http://www.cs2j.com): 8/18/2017 3:07:36 PM
//

package org.hl7.fhir.r4.utils.transform.deserializer;


import common.grammar.VisitorExtensions;
import common.grammar.antlr.FhirMapJavaBaseVisitor;
import common.grammar.antlr.FhirMapJavaParser;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
* ANTLR Visitor class.
*/
public class FhirMapVisitor  extends FhirMapJavaBaseVisitor<Object>
{
    /**
     Maximum length of a name.
     */
    private static final int MaxNameLength = 64;

    /**
     Class to execute fhir map commands.
     */
    private IFhirMapExecutor executor;

    /**
     Lazy create url processor.
     */
    private UrlProcessor getUrlProcessor() throws Exception {
        if (this.urlProcessor == null)
        {
            this.urlProcessor = new UrlProcessor();
        }
        return this.urlProcessor;
    }
    private UrlProcessor urlProcessor;

    /**
     Delegate for optional dumping of info.

     */
    @FunctionalInterface
    public interface DumpDelegate
    {
        void invoke(String msg);
    }

    /**
     Set this to callback function to dump parsing messages.
     */
    public DumpDelegate DumpFcn = null;

    /**
     Constructor.

     */
    public FhirMapVisitor(IFhirMapExecutor executor)
    {
        this.executor = executor;
    }

    /**
     Parse antlr rule keyMap.
     This will trigger a Executor.Map callback.

     @param context
     @return null
     */
    @Override
    public Object visitKeyMap(FhirMapJavaParser.KeyMapContext context) {
        UrlData urlData = null;
        try {
            urlData = VisitorExtensions.VisitOrDefault(this, context.structureMap(), UrlData.class);
            String name = VisitorExtensions.VisitOrDefault(this, context.quotedString(), String.class);
            this.executor.map(urlData, name);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     Parse antlr rule structureMap.

     @param context
     @return UrlData instance
     */
    @Override
    public Object visitStructureMap(FhirMapJavaParser.StructureMapContext context) {
        try {
            return VisitorExtensions.<UrlData>VisitOrDefault(this, context.quotedUrl(), UrlData.class);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     Parse antlr rule keyImports.
     This will trigger a Executor.Imports callback.

     @param context
     @return null
     */
    @Override
    public Object visitKeyImports(FhirMapJavaParser.KeyImportsContext context) {
        UrlData urlData = null;
        try {
            urlData = VisitorExtensions.VisitOrDefault(this, context.structureMap(), UrlData.class);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        try {
            this.executor.imports(urlData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     Parse antlr rule structureDefinition.

     @param context
     @return UrlData instance
     */
    @Override
    public Object visitStructureDefinition(FhirMapJavaParser.StructureDefinitionContext context) {
        try {
            return VisitorExtensions.<UrlData>VisitOrDefault(this, context.quotedUrl(),UrlData.class);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     Parse antlr rule identifier
     This verifies that thwe identifier is not too long.

     @param context
     @return String identifier
     */
    @Override
    public Object visitIdentifier(FhirMapJavaParser.IdentifierContext context)
    {
        String retVal = context.getText(); // get string characters
        if (retVal.length() > MaxNameLength)
        {
            throw new RuntimeException("Identifier must be less than {MaxNameLength} characters.  '{retVal}'");
        }
        return retVal;
    }

    /**
     Parse antlr rule quotedIdentifier

     @param context
     @return String without the surrounding quotes
     */
    @Override
    public Object visitQuotedIdentifier(FhirMapJavaParser.QuotedIdentifierContext context)
    {
        String retVal = context.getText(); // get string characters
        retVal = retVal.substring(1, 1 + retVal.length() - 2); // remove surrounding double quotes.
        return retVal;
    }

    /**
     Parse antlr rule quotedString

     @param context
     @return String without the surrounding quotes
     */
    @Override
    public Object visitQuotedString(FhirMapJavaParser.QuotedStringContext context)
    {
        String retVal = context.getText(); // get string characters
        retVal = retVal.substring(1, 1 + retVal.length() - 2); // remove surrounding double quotes.
        return retVal;
    }

    /**
     Parse antlr rule quotedStringWQuotes

     @param context
     @return String without the surrounding quotes
     */
    @Override
    public Object visitQuotedStringWQuotes(FhirMapJavaParser.QuotedStringWQuotesContext context)
    {
        String retVal = context.getText(); // get string characters
        return retVal;
    }

    /**
     Parse antlr rule int
     created.

     @param context
     @return Int32 value
     */
    @Override
    public Object visitInteger(FhirMapJavaParser.IntegerContext context)
    {
        return Integer.parseInt(context.getText());
    }

    /**
     Parse antlr rule quotedUrl
     The url parser is split off from this because of some incompatabilitied between the two
     grammars. Here we pass the url portion to this seperate parser and return the UrlData
     created.

     @param context
     @return UrlData instance
     */
    @Override
    public Object visitQuotedUrl(FhirMapJavaParser.QuotedUrlContext context) {
        String urlStr = null;
        try {
            urlStr = VisitorExtensions.VisitOrDefault(this, context.quotedString(), String.class);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        try {
            return this.getUrlProcessor().parseUrl(urlStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
    ///#region Uses methods
    /**
     Parse antlr rule keyUses.
     This will trigger a Executor.Uses callback.

     @param context
     @return null
     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: public override Object VisitKeyUses([NotNull] KeyUsesContext context)
    @Override
    public Object visitKeyUses(FhirMapJavaParser.KeyUsesContext context) {
        UrlData urlData = null;
        try {
            urlData = VisitorExtensions.VisitOrDefault(this, context.structureDefinition(), UrlData.class);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        FhirMapUseNames name = FhirMapUseNames.forValue((Integer) this.visit(context.keyUsesName()));
        try {
            this.executor.uses(urlData, name);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     Parse antlr rule keyUsesName.

     @param context
     @return null
     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: public override Object VisitKeyUsesName([NotNull] KeyUsesNameContext context)
    @Override
    public Object visitKeyUsesName(FhirMapJavaParser.KeyUsesNameContext context)
    {
        return FhirMapUseNames.forValue((Integer)this.visitChildren(context));
    }

    /**
     Parse antlr rule keyUsesNameSource.

     @param context
     @return UseNames.Source
     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: public override Object VisitKeyUsesNameSource([NotNull] KeyUsesNameSourceContext context)
    @Override
    public Object visitKeyUsesNameSource(FhirMapJavaParser.KeyUsesNameSourceContext context)
    {
        return FhirMapUseNames.Source;
    }

    /**
     Parse antlr rule keyUsesNameTarget.

     @param context
     @return UseNames.Target
     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: public override Object VisitKeyUsesNameTarget([NotNull] KeyUsesNameTargetContext context)
    @Override
    public Object visitKeyUsesNameTarget(FhirMapJavaParser.KeyUsesNameTargetContext context)
    {
        return FhirMapUseNames.Target;
    }

    /**
     Parse antlr rule keyUsesNameQueried.

     @param context
     @return UseNames.Queried
     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: public override Object VisitKeyUsesNameQueried([NotNull] KeyUsesNameQueriedContext context)
    @Override
    public Object visitKeyUsesNameQueried(FhirMapJavaParser.KeyUsesNameQueriedContext context)
    {
        return FhirMapUseNames.Queried;
    }

    /**
     Parse antlr rule keyUsesNameProduced.

     @param context
     @return UseNames.Produced
     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: public override Object VisitKeyUsesNameProduced([NotNull] KeyUsesNameProducedContext context)
    @Override
    public Object visitKeyUsesNameProduced(FhirMapJavaParser.KeyUsesNameProducedContext context)
    {
        return FhirMapUseNames.Produced;
    }
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
    ///#endregion
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
    ///#region Group Methods
    /**
     Parse antlr rule groupStart.

     @param context
     @return GroupTypes
     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: public override Object VisitGroupStart([NotNull] GroupStartContext context)
    @Override
    public Object visitGroupStart(FhirMapJavaParser.GroupStartContext context)  {
        try {
            this.executor.groupStart(VisitorExtensions.VisitOrDefault(this, context.identifier(), String.class), VisitorExtensions.VisitOrDefault(this, context.groupType(), FhirMapGroupTypes.NotSet), VisitorExtensions.VisitOrDefault(this, context.groupExtends(),String.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     Parse antlr rule groupEnd.

     @param context
     @return GroupTypes
     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: public override Object VisitGroupEnd([NotNull] GroupEndContext context)
    @Override
    public Object visitGroupEnd(FhirMapJavaParser.GroupEndContext context) {
        try {
            this.executor.groupEnd();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     Parse antlr rule groupExtends.

     @param context
     @return String[] of group
     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: public override Object VisitGroupExtends([NotNull] GroupExtendsContext context)
    @Override
    public Object visitGroupExtends(FhirMapJavaParser.GroupExtendsContext context) {
        try {
            return VisitorExtensions.<String>VisitOrDefault(this, context.identifier(), String.class);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     Parse antlr rule groupType.

     @param context
     @return GroupTypes
     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: public override Object VisitGroupType([NotNull] GroupTypeContext context)
    @Override
    public Object visitGroupType(FhirMapJavaParser.GroupTypeContext context)
    {
        return FhirMapGroupTypes.forValue((Integer) this.visitChildren(context));
    }

    /**
     Parse antlr rule groupTypeType.

     @param context
     @return GroupTypes.Type
     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: public override Object VisitGroupTypeType([NotNull] GroupTypeTypeContext context)
    @Override
    public Object visitGroupTypeType(FhirMapJavaParser.GroupTypeTypeContext context)
    {
        return FhirMapGroupTypes.Types;
    }

    /**
     Parse antlr rule groupTypeTypeTypes.

     @param context
     @return GroupTypes.TypeTypes
     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: public override Object VisitGroupTypeTypeTypes([NotNull] GroupTypeTypeTypesContext context)
    @Override
    public Object visitGroupTypeTypeTypes(FhirMapJavaParser.GroupTypeTypeTypesContext context)
    {
        return FhirMapGroupTypes.TypeTypes;
    }

    /**
     Parse antlr rule ruleInput.

     @param context
     @return FhirMapRuleInput
     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: public override Object VisitRuleInput([NotNull] RuleInputContext context)
    @Override
    public Object visitRuleInput(FhirMapJavaParser.RuleInputContext context)  {
        try {
            this.executor.groupInput(VisitorExtensions.VisitOrDefault(this, context.ruleInputName(), String.class), VisitorExtensions.VisitOrDefault(this, context.ruleInputType(), String.class), VisitorExtensions.VisitOrDefault(this, context.ruleInputMode(), FhirMapInputModes.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     Parse antlr rule ruleInputModes.

     @param context
     @return FhirMapInputModes
     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: public override Object VisitRuleInputMode([NotNull] RuleInputModeContext context)
    @Override
    public Object visitRuleInputMode(FhirMapJavaParser.RuleInputModeContext context)  {
        try {
            return VisitorExtensions.<FhirMapInputModes>VisitOrDefault(this, context.ruleInputModes(), FhirMapInputModes.class);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     Parse antlr rule ruleInputModesSource.

     @param context
     @return FhirMapInputModes.Source
     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: public override Object VisitRuleInputModesSource([NotNull] RuleInputModesSourceContext context)
    @Override
    public Object visitRuleInputModesSource(FhirMapJavaParser.RuleInputModesSourceContext context)
    {
        return FhirMapInputModes.Source;
    }

    /**
     Parse antlr rule ruleInputModesTarget.

     @param context
     @return FhirMapInputModes.Target
     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: public override Object VisitRuleInputModesTarget([NotNull] RuleInputModesTargetContext context)
    @Override
    public Object visitRuleInputModesTarget(FhirMapJavaParser.RuleInputModesTargetContext context)
    {
        return FhirMapInputModes.Target;
    }
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
    ///#endregion
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
    ///#region Rule Methods

    /**
     Parse antlr rule ruleInstance

     @param context
     @return null
     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: public override Object VisitRuleInstance([NotNull] RuleInstanceContext context)
    @Override
    public Object visitRuleInstance(FhirMapJavaParser.RuleInstanceContext context) {
        try {
            this.executor.ruleStart(VisitorExtensions.VisitOrDefault(this, context.ruleName(), String[].class));
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.visitChildren(context);
        try {
            this.executor.ruleComplete();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     Parse antlr rule ruleTargetReference

     @param context
     @return String[]
     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: public override Object VisitRuleTargetReference([NotNull] RuleTargetReferenceContext context)
    @Override
    public Object visitRuleTargetReference(FhirMapJavaParser.RuleTargetReferenceContext context) {
        try {
            this.executor.transformReference(VisitorExtensions.VisitOrDefault(this, context.ruleTargetContext(), String[].class), VisitorExtensions.VisitOrDefault(this, context.ruleTargetReferenceSource(), String.class), VisitorExtensions.VisitOrDefault(this, context.ruleTargetVariable(), String.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     Parse antlr rule ruleTargetTruncate

     @param context
     @return String[]
     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: public override Object VisitRuleTargetTruncate([NotNull] RuleTargetTruncateContext context)
    @Override
    public Object visitRuleTargetTruncate(FhirMapJavaParser.RuleTargetTruncateContext context) {
        try {
            this.executor.transformTruncate(VisitorExtensions.VisitOrDefault(this, context.ruleTargetContext(), String[].class), VisitorExtensions.VisitOrDefault(this, context.ruleTargetTruncateSource(), String.class), VisitorExtensions.<Integer>VisitOrDefault(this, context.ruleTargetTruncateLength(), Integer.class), VisitorExtensions.VisitOrDefault(this, context.ruleTargetVariable(), String.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     Parse antlr rule ruleTargetCast

     @param context
     @return String[]
     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: public override Object VisitRuleTargetCast([NotNull] RuleTargetCastContext context)
    @Override
    public Object visitRuleTargetCast(FhirMapJavaParser.RuleTargetCastContext context) {
        try {
            this.executor.transformCast(VisitorExtensions.VisitOrDefault(this, context.ruleTargetContext(), String[].class), VisitorExtensions.VisitOrDefault(this, context.ruleTargetCastSource(), String.class), VisitorExtensions.VisitOrDefault(this, context.ruleTargetCastType(), String.class), VisitorExtensions.VisitOrDefault(this, context.ruleTargetVariable(), String.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     Parse antlr rule ruleTargetAs

     @param context
     @return String[]
     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: public override Object VisitRuleTargetAs([NotNull] RuleTargetAsContext context)
    @Override
    public Object visitRuleTargetAs(FhirMapJavaParser.RuleTargetAsContext context) {
        try {
            this.executor.transformAs(VisitorExtensions.VisitOrDefault(this, context.ruleContext(), String[].class), VisitorExtensions.VisitOrDefault(this, context.ruleTargetVariable(), String.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     Parse antlr rule ruleTargetAssign

     @param context
     @return String[]
     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: public override Object VisitRuleTargetAssign([NotNull] RuleTargetAssignContext context)
    @Override
    public Object visitRuleTargetAssign(FhirMapJavaParser.RuleTargetAssignContext context) {
        try {
            this.executor.transformCopy(VisitorExtensions.VisitOrDefault(this, context.ruleTargetContext(), String[].class), VisitorExtensions.VisitOrDefault(this, context.ruleTargetAssignValue(), String.class), VisitorExtensions.VisitOrDefault(this, context.ruleTargetVariable(), String.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     Parse antlr rule ruleTargetCopy

     @param context
     @return String[]
     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: public override Object VisitRuleTargetCopy([NotNull] RuleTargetCopyContext context)
    @Override
    public Object visitRuleTargetCopy(FhirMapJavaParser.RuleTargetCopyContext context) {
        try {
            this.executor.transformCopy(VisitorExtensions.VisitOrDefault(this, context.ruleTargetContext(), String[].class), VisitorExtensions.VisitOrDefault(this, context.ruleTargetCopySource(), String.class), VisitorExtensions.VisitOrDefault(this, context.ruleTargetVariable(), String.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     Parse antlr rule ruleTargetCreate

     @param context
     @return String[]
     */
    @Override
    public Object visitRuleTargetCreate(FhirMapJavaParser.RuleTargetCreateContext context) {
        try {
            this.executor.transformCreate(VisitorExtensions.VisitOrDefault(this, context.ruleTargetContext(), String[].class), VisitorExtensions.VisitOrDefault(this, context.ruleTargetCreateType(), String.class), VisitorExtensions.VisitOrDefault(this, context.ruleTargetVariable(), String.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     Parse antlr rule ruleTargetTranslate

     @param context
     @return String[]
     */
    @Override
    public Object visitRuleTargetTranslate(FhirMapJavaParser.RuleTargetTranslateContext context) {
        try {
            this.executor.transformTranslate(VisitorExtensions.VisitOrDefault(this, context.ruleTargetContext(), String[].class), VisitorExtensions.VisitOrDefault(this, context.ruleTargetTranslateSource(), String.class), VisitorExtensions.VisitOrDefault(this, context.ruleTargetTranslateMap(), UrlData.class), VisitorExtensions.VisitOrDefault(this, context.ruleTargetTranslateOutput(), FhirMapTranslateOutputTypes.class), VisitorExtensions.VisitOrDefault(this, context.ruleTargetVariable(), String.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     Parse antlr rule ruleTargetTranslateOutputCode

     @param context
     @return String[]
     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: public override Object VisitRuleTargetTranslateOutputCode([NotNull] RuleTargetTranslateOutputCodeContext context)
    @Override
    public Object visitRuleTargetTranslateOutputCode(FhirMapJavaParser.RuleTargetTranslateOutputCodeContext context)
    {
        return FhirMapTranslateOutputTypes.Code;
    }

    /**
     Parse antlr rule ruleTargetTranslateOutputSystem

     @param context
     @return String[]
     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: public override Object VisitRuleTargetTranslateOutputSystem([NotNull] RuleTargetTranslateOutputSystemContext context)
    @Override
    public Object visitRuleTargetTranslateOutputSystem(FhirMapJavaParser.RuleTargetTranslateOutputSystemContext context)
    {
        return FhirMapTranslateOutputTypes.System;
    }

    /**
     Parse antlr rule ruleTargetTranslateOutputDisplay

     @param context
     @return String[]
     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: public override Object VisitRuleTargetTranslateOutputDisplay([NotNull] RuleTargetTranslateOutputDisplayContext context)
    @Override
    public Object visitRuleTargetTranslateOutputDisplay(FhirMapJavaParser.RuleTargetTranslateOutputDisplayContext context)
    {
        return FhirMapTranslateOutputTypes.Display;
    }

    /**
     Parse antlr rule ruleTargetTranslateOutputCoding

     @param context
     @return String[]
     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: public override Object VisitRuleTargetTranslateOutputCoding([NotNull] RuleTargetTranslateOutputCodingContext context)
    @Override
    public Object visitRuleTargetTranslateOutputCoding(FhirMapJavaParser.RuleTargetTranslateOutputCodingContext context)
    {
        return FhirMapTranslateOutputTypes.Coding;
    }

    /**
     Parse antlr rule ruleTargetTranslateOutputCodeableConcept

     @param context
     @return String[]
     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: public override Object VisitRuleTargetTranslateOutputCodeableConcept([NotNull] RuleTargetTranslateOutputCodeableConceptContext context)
    @Override
    public Object visitRuleTargetTranslateOutputCodeableConcept(FhirMapJavaParser.RuleTargetTranslateOutputCodeableConceptContext context)
    {
        return FhirMapTranslateOutputTypes.CodeableConcept;
    }

    /**
     Parse antlr rule ruleTargetCp

     @param context
     @return String[]
     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: public override Object VisitRuleTargetCp([NotNull] RuleTargetCpContext context)
    @Override
    public Object visitRuleTargetCp(FhirMapJavaParser.RuleTargetCpContext context) {
        try {
            this.executor.transformCp(VisitorExtensions.VisitOrDefault(this, context.ruleTargetContext(), String[].class), VisitorExtensions.VisitOrDefault(this, context.ruleTargetCpSystem(), UrlData.class), VisitorExtensions.VisitOrDefault(this, context.ruleTargetCpVariable(), String.class), VisitorExtensions.VisitOrDefault(this, context.ruleTargetVariable(), String.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     Parse antlr rule ruleTargetAppend

     @param context
     @return String[]
     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: public override Object VisitRuleTargetAppend([NotNull] RuleTargetAppendContext context)
    @Override
    public Object visitRuleTargetAppend(FhirMapJavaParser.RuleTargetAppendContext context) {
        try {
            this.executor.transformAppend(VisitorExtensions.VisitOrDefault(this, context.ruleTargetContext(), String[].class), VisitorExtensions.VisitOrDefault(this, context.ruleTargetAppendSources(), String[].class), VisitorExtensions.VisitOrDefault(this, context.ruleTargetVariable(), String.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     Parse antlr rule ruleTargetAppendSources

     @param context
     @return String[]
     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: public override Object VisitRuleTargetAppendSources([NotNull] RuleTargetAppendSourcesContext context)
    @Override
    public Object visitRuleTargetAppendSources(FhirMapJavaParser.RuleTargetAppendSourcesContext context)
    {
        ArrayList<String> values = new ArrayList<>();
        return VisitorExtensions.VisitMultiple(this, context.ruleTargetAppendSource(), values);
    }

    /**
     Parse antlr rule ruleTargetC

     @param context
     @return String[]
     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: public override Object VisitRuleTargetC([NotNull] RuleTargetCContext context)
    @Override
    public Object visitRuleTargetC(FhirMapJavaParser.RuleTargetCContext context) {
        try {
            this.executor.transformCoding(VisitorExtensions.VisitOrDefault(this, context.ruleTargetContext(), String[].class), VisitorExtensions.VisitOrDefault(this, context.ruleTargetCSystem(), UrlData.class), VisitorExtensions.VisitOrDefault(this, context.ruleTargetCCode(), String.class), VisitorExtensions.VisitOrDefault(this, context.ruleTargetCDisplay(), String.class), VisitorExtensions.VisitOrDefault(this, context.ruleTargetVariable(), String.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     Parse antlr rule ruleTargetCC1

     @param context
     @return String[]
     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: public override Object VisitRuleTargetCC1([NotNull] RuleTargetCC1Context context)
    @Override
    public Object visitRuleTargetCC1(FhirMapJavaParser.RuleTargetCC1Context context) {
        try {
            this.executor.transformCodeableConcept(VisitorExtensions.VisitOrDefault(this, context.ruleTargetContext(), String[].class), VisitorExtensions.VisitOrDefault(this, context.ruleTargetCC1Text(), String.class), VisitorExtensions.VisitOrDefault(this, context.ruleTargetVariable(), String.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     Parse antlr rule ruleTargetCC2

     @param context
     @return String[]
     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: public override Object VisitRuleTargetCC2([NotNull] RuleTargetCC2Context context)
    @Override
    public Object visitRuleTargetCC2(FhirMapJavaParser.RuleTargetCC2Context context) {
        try {
            this.executor.transformCodeableConcept(VisitorExtensions.VisitOrDefault(this, context.ruleTargetContext(), String[].class), VisitorExtensions.VisitOrDefault(this, context.ruleTargetCC2System(), UrlData.class), VisitorExtensions.VisitOrDefault(this, context.ruleTargetCC2Code(), String.class), VisitorExtensions.VisitOrDefault(this, context.ruleTargetCC2Display(), String.class), VisitorExtensions.VisitOrDefault(this, context.ruleTargetVariable(), String.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     Parse antlr rule ruleTargetContext

     @param context
     @return String[]
     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: public override Object VisitRuleTargetContext([NotNull] RuleTargetContextContext context)
    @Override
    public Object visitRuleTargetContext(FhirMapJavaParser.RuleTargetContextContext context) {
        try {
            return VisitorExtensions.<String[]>VisitOrDefault(this, context.ruleContext(), String[].class);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     Parse antlr rule ruleTargetVariable

     @param context
     @return String[]
     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: public override Object VisitRuleTargetVariable([NotNull] RuleTargetVariableContext context)
    @Override
    public Object visitRuleTargetVariable(FhirMapJavaParser.RuleTargetVariableContext context) {
        try {
            return VisitorExtensions.<String>VisitOrDefault(this, context.identifier(), String.class);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     Parse antlr rule ruleTargetEscape

     @param context
     @return String[]
     */
    @Override
    public Object visitRuleTargetEscape(FhirMapJavaParser.RuleTargetEscapeContext context)
    {
        try {
            this.executor.transformEscape(VisitorExtensions.VisitOrDefault(this, context.ruleTargetContext(), String[].class), VisitorExtensions.VisitOrDefault(this, context.ruleTargetEscapeVariable(), String.class), VisitorExtensions.VisitOrDefault(this, context.ruleTargetEscapeString1(), String.class), VisitorExtensions.VisitOrDefault(this, context.ruleTargetEscapeString2(), String.class), VisitorExtensions.VisitOrDefault(this, context.ruleTargetVariable(), String.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     Parse antlr rule ruleTargetEvaluate

     @param context
     @return String[]
     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: public override Object VisitRuleTargetEvaluate([NotNull] RuleTargetEvaluateContext context)
    @Override
    public Object visitRuleTargetEvaluate(FhirMapJavaParser.RuleTargetEvaluateContext context) {
        try {
            this.executor.transformEvaluate(VisitorExtensions.VisitOrDefault(this, context.ruleTargetContext(), String[].class), VisitorExtensions.VisitOrDefault(this, context.ruleTargetEvaluateObject(), String.class), VisitorExtensions.VisitOrDefault(this, context.ruleTargetEvaluateObjectElement(), String.class), VisitorExtensions.VisitOrDefault(this, context.ruleTargetVariable(), String.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     Parse antlr rule ruleTargetId

     @param context
     @return String[]
     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: public override Object VisitRuleTargetId([NotNull] RuleTargetIdContext context)
    @Override
    public Object visitRuleTargetId(FhirMapJavaParser.RuleTargetIdContext context) {
        try {
            this.executor.transformId(VisitorExtensions.VisitOrDefault(this, context.ruleTargetContext(), String[].class), VisitorExtensions.VisitOrDefault(this, context.ruleTargetIdSystem(), UrlData.class), VisitorExtensions.VisitOrDefault(this, context.ruleTargetIdValue(), String.class), VisitorExtensions.VisitOrDefault(this, context.ruleTargetIdType(), String.class), VisitorExtensions.VisitOrDefault(this, context.ruleTargetVariable(), String.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     Parse antlr rule ruleTargetPointer

     @param context
     @return String[]
     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: public override Object VisitRuleTargetPointer([NotNull] RuleTargetPointerContext context)
    @Override
    public Object visitRuleTargetPointer(FhirMapJavaParser.RuleTargetPointerContext context) {
        try {
            this.executor.transformPointer(VisitorExtensions.VisitOrDefault(this, context.ruleTargetContext(), String[].class), VisitorExtensions.VisitOrDefault(this, context.ruleTargetPointerResource(), String.class), VisitorExtensions.VisitOrDefault(this, context.ruleTargetVariable(), String.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     Parse antlr rule ruleTargetQty1

     @param context
     @return String[]
     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: public override Object VisitRuleTargetQty1([NotNull] RuleTargetQty1Context context)
    @Override
    public Object visitRuleTargetQty1(FhirMapJavaParser.RuleTargetQty1Context context) {
        try {
            this.executor.transformQty(VisitorExtensions.VisitOrDefault(this, context.ruleTargetContext(), String[].class), VisitorExtensions.VisitOrDefault(this, context.ruleTargetQty1Text(), String.class), VisitorExtensions.VisitOrDefault(this, context.ruleTargetVariable(), String.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     Parse antlr rule ruleTargetQty2

     @param context
     @return String[]
     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: public override Object VisitRuleTargetQty2([NotNull] RuleTargetQty2Context context)
    @Override
    public Object visitRuleTargetQty2(FhirMapJavaParser.RuleTargetQty2Context context) {
        try {
            this.executor.transformQty(VisitorExtensions.VisitOrDefault(this, context.ruleTargetContext(), String[].class), VisitorExtensions.VisitOrDefault(this, context.ruleTargetQty2Value(), String.class), VisitorExtensions.VisitOrDefault(this, context.ruleTargetQty2UnitString(), String.class), VisitorExtensions.<UrlData>VisitOrDefault(this, context.ruleTargetQty2System(), UrlData.class), VisitorExtensions.VisitOrDefault(this, context.ruleTargetVariable(), String.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     Parse antlr rule ruleTargetQty3

     @param context
     @return String[]
     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: public override Object VisitRuleTargetQty3([NotNull] RuleTargetQty3Context context)
    @Override
    public Object visitRuleTargetQty3(FhirMapJavaParser.RuleTargetQty3Context context) {
        try {
            this.executor.transformQty(VisitorExtensions.VisitOrDefault(this, context.ruleTargetContext(), String[].class), VisitorExtensions.VisitOrDefault(this, context.ruleTargetQty3Value(), String.class), VisitorExtensions.VisitOrDefault(this, context.ruleTargetQty3UnitString(), String.class), VisitorExtensions.<String>VisitOrDefault(this, context.ruleTargetQty3CodeVariable(), String.class), VisitorExtensions.VisitOrDefault(this, context.ruleTargetVariable(), String.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     Parse antlr rule ruleTargetUuid

     @param context
     @return String[]
     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: public override Object VisitRuleTargetUuid([NotNull] RuleTargetUuidContext context)
    @Override
    public Object visitRuleTargetUuid(FhirMapJavaParser.RuleTargetUuidContext context) {
        try {
            this.executor.transformUuid(VisitorExtensions.VisitOrDefault(this, context.ruleTargetContext(), String[].class), VisitorExtensions.VisitOrDefault(this, context.ruleTargetVariable(), String.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     Parse antlr rule ruleTargetEscape

     @param context
     @return String[]
     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: public override Object VisitRuleTargetDateOp([NotNull] RuleTargetDateOpContext context)
    @Override
    public Object visitRuleTargetDateOp(FhirMapJavaParser.RuleTargetDateOpContext context) {
        try {
            this.executor.transformDateOp(VisitorExtensions.VisitOrDefault(this, context.ruleTargetContext(), String[].class), VisitorExtensions.VisitOrDefault(this, context.ruleTargetDateOpVariable(), String.class), VisitorExtensions.VisitOrDefault(this, context.ruleTargetDateOpOperation(), String.class), VisitorExtensions.VisitOrDefault(this, context.ruleTargetDateOpVariable2(), String.class), VisitorExtensions.VisitOrDefault(this, context.ruleTargetVariable(), String.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     Parse antlr rule ruleName

     @param context
     @return String[]
     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: public override Object VisitRuleName([NotNull] RuleNameContext context)
    @Override
    public Object visitRuleName(FhirMapJavaParser.RuleNameContext context)
    {
        ArrayList<String> values = new ArrayList<String>();
        return VisitorExtensions.VisitMultiple(this, context.identifier(), values);
    }

    /**
     Parse antlr rule ruleSource

     @param context
     @return FhirMapRuleType instance
     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: public override Object VisitRuleSource([NotNull] RuleSourceContext context)
    @Override
    public Object visitRuleSource(FhirMapJavaParser.RuleSourceContext context) {
        try {
            this.executor.ruleSource(VisitorExtensions.VisitOrDefault(this, context.ruleContext(), String[].class), VisitorExtensions.VisitOrDefault(this, context.ruleType(), FhirMapRuleType.class), VisitorExtensions.VisitOrDefault(this, context.ruleDefault(), String.class), VisitorExtensions.VisitOrDefault(this, context.ruleListOption(), FhirMapListOptions.class), VisitorExtensions.VisitOrDefault(this, context.ruleVariable(), String.class), VisitorExtensions.VisitOrDefault(this, context.ruleWherePath(), String.class), VisitorExtensions.VisitOrDefault(this, context.ruleCheckPath(), String.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     Parse antlr rule ruleType

     @param context
     @return FhirMapRuleType instance
     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: public override Object VisitRuleType([NotNull] RuleTypeContext context)
    @Override
    public Object visitRuleType(FhirMapJavaParser.RuleTypeContext context) {
        ArrayList<Integer> values = new ArrayList<Integer>();
        FhirMapRuleType tempVar = new FhirMapRuleType();
        try {
            tempVar.TypeName = VisitorExtensions.VisitOrDefault(this, context.identifier(), String.class);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        for (ParseTree treeItem : context.integer())
            {
                List<Integer> occurances = Arrays.asList(tempVar.Occurances);
                occurances.add((Integer) this.visit(treeItem));
                tempVar.Occurances = occurances.toArray(tempVar.Occurances);
            }

        return tempVar;
    }

    /**
     Parse antlr rule ruleDefault
     #! Verify format of default value. Currently accepts an identifier.
     #! Also write test for this...

     @param context
     @return String
     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: public override Object VisitRuleDefault([NotNull] RuleDefaultContext context)
    @Override
    public Object visitRuleDefault(FhirMapJavaParser.RuleDefaultContext context)  {
        try {
            return VisitorExtensions.<String>VisitOrDefault(this, context.identifier(), String.class);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     Parse antlr rule ruleVariable

     @param context
     @return String
     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: public override Object VisitRuleVariable([NotNull] RuleVariableContext context)
    @Override
    public Object visitRuleVariable(FhirMapJavaParser.RuleVariableContext context)  {
        try {
            return VisitorExtensions.<String>VisitOrDefault(this, context.identifier(), String.class);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     Parse antlr rule ruleContext

     @param context
     @return String[]
     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: public override Object VisitRuleContext([NotNull] RuleContextContext context)
    @Override
    public Object visitRuleContext(FhirMapJavaParser.RuleContextContext context)
    {
        ArrayList<String> values = new ArrayList<String>();
        return VisitorExtensions.VisitMultiple(this, context.ruleContextElement(), values);
    }

    /**
     Parse antlr rule ruleContextElement

     @param context
     @return String
     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: public override Object VisitRuleContextElement([NotNull] RuleContextElementContext context)
    @Override
    public Object visitRuleContextElement(FhirMapJavaParser.RuleContextElementContext context)
    {
        return this.visitChildren(context);
    }
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
    ///#endregion
}


