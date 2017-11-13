package org.hl7.fhir.r4.utils.transform.deserializer;


import org.antlr.v4.runtime.tree.ParseTree;
import org.hl7.fhir.r4.utils.transform.deserializer.grammar.antlr.javaAntlr.FhirMapBaseVisitor;
import org.hl7.fhir.r4.utils.transform.deserializer.grammar.antlr.javaAntlr.FhirMapParser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * ANTLR Visitor class.
 *
 * @author Travis Lukach
 * (c) Applicadia LLC.
 */
@SuppressWarnings("unchecked")
public class FhirMapVisitor extends FhirMapBaseVisitor<Object> {
  /**
   * Maximum length of a name.
   */
  private static final int MaxNameLength = 64;

  /**
   * Class to execute fhir map commands.
   */
  private IFhirMapExecutor executor;

  public IFhirMapExecutor getExecutor() {
    return executor;
  }

  public void setExecutor(IFhirMapExecutor executor) {
    this.executor = executor;
  }

  /**
   * Lazy create url processor.
   */
  private UrlProcessor getUrlProcessor() throws Exception {
    if (this.urlProcessor == null) {
      this.urlProcessor = new UrlProcessor();
    }
    return this.urlProcessor;
  }

  private UrlProcessor urlProcessor;

  /**
   * Delegate for optional dumping of info.
   */
  @FunctionalInterface
  public interface DumpDelegate {
    void invoke(String msg);
  }

  /**
   * Set this to callback function to dump parsing messages.
   */
  public DumpDelegate DumpFcn = null;

  /**
   * Constructor.
   */
  public FhirMapVisitor(IFhirMapExecutor executor) {
    this.executor = executor;
  }

  /**
   * Parse grammar rule keyMap.
   * This will trigger a Executor.Map callback.
   *
   * @param context contextual value containing the data to be processed
   * @return null
   */
  @Override
  public Object visitKeyMap(FhirMapParser.KeyMapContext context) {
    UrlData urlData;
    try {
      urlData = (UrlData) this.visit(context.structureMap());
      String name = (String) this.visit(context.quotedString());
      this.executor.map(urlData, name);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Parse grammar rule structureMap.
   *
   * @param context contextual value containing the data to be processed
   * @return UrlData instance
   */
  @Override
  public Object visitStructureMap(FhirMapParser.StructureMapContext context) {
    return this.visit(context.quotedUrl());
  }

  /**
   * Parse grammar rule keyImports.
   * This will trigger a Executor.Imports callback.
   *
   * @param context contextual value containing the data to be processed
   * @return null
   */
  @Override
  public Object visitKeyImports(FhirMapParser.KeyImportsContext context) {
    UrlData urlData;
    urlData = (UrlData) this.visit(context.structureMap());
    try {
      this.executor.imports(urlData);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Parse grammar rule structureDefinition.
   *
   * @param context contextual value containing the data to be processed
   * @return UrlData instance
   */
  @Override
  public Object visitStructureDefinition(FhirMapParser.StructureDefinitionContext context) {
    return this.visit(context.quotedUrl());
  }

  /**
   * Parse grammar rule identifier
   * This verifies that thwe identifier is not too long.
   *
   * @param context contextual value containing the data to be processed
   * @return String identifier
   */
  @Override
  public Object visitIdentifier(FhirMapParser.IdentifierContext context) {
    String retVal = context.getText(); // get string characters
    if (retVal.length() > MaxNameLength) {
      throw new RuntimeException("Identifier must be less than {MaxNameLength} characters.  '{retVal}'");
    }
    return retVal;
  }

  /**
   * Parse grammar rule quotedIdentifier
   *
   * @param context contextual value containing the data to be processed
   * @return String without the surrounding quotes
   */
  @Override
  public Object visitQuotedIdentifier(FhirMapParser.QuotedIdentifierContext context) {
    String retVal = context.getText(); // get string characters
    retVal = retVal.substring(1, 1 + retVal.length() - 2); // remove surrounding double quotes.
    return retVal;
  }

  /**
   * Parse grammar rule quotedString
   *
   * @param context contextual value containing the data to be processed
   * @return String without the surrounding quotes
   */
  @Override
  public Object visitQuotedString(FhirMapParser.QuotedStringContext context) {
    String retVal = context.getText(); // get string characters
    retVal = retVal.substring(1, 1 + retVal.length() - 2); // remove surrounding double quotes.
    return retVal;
  }

  /**
   * Parse grammar rule quotedStringWQuotes
   *
   * @param context contextual value containing the data to be processed
   * @return String without the surrounding quotes
   */
  @Override
  public Object visitQuotedStringWQuotes(FhirMapParser.QuotedStringWQuotesContext context) {
    return context.getText();
  }

  /**
   * Parse grammar rule int
   * created.
   *
   * @param context contextual value containing the data to be processed
   * @return Int32 value
   */
  @Override
  public Object visitInteger(FhirMapParser.IntegerContext context) {
    return Integer.parseInt(context.getText());
  }

  /**
   * Parse grammar rule quotedUrl
   * The url parser is split off from this because of some incompatabilitied between the two
   * grammars. Here we pass the url portion to this seperate parser and return the UrlData
   * created.
   *
   * @param context contextual value containing the data to be processed
   * @return UrlData instance
   */
  @Override
  public Object visitQuotedUrl(FhirMapParser.QuotedUrlContext context) {
    String urlStr = null;
    urlStr = (String) this.visit(context.quotedString());
    try {
      return this.getUrlProcessor().parseUrl(urlStr);
    } catch (Exception e) {
      e.printStackTrace();
    }

    return null;
  }

  /**
   * Parse grammar rule keyUses.
   * This will trigger a Executor.Uses callback.
   *
   * @param context contextual value containing the data to be processed
   * @return null
   */
  @Override
  public Object visitKeyUses(FhirMapParser.KeyUsesContext context) {
    UrlData urlData = null;
    urlData = (UrlData) this.visit(context.structureDefinition());
    FhirMapUseNames name = (FhirMapUseNames) this.visit(context.keyUsesName());
    try {
      this.executor.uses(urlData, name);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Parse grammar rule keyUsesName.
   *
   * @param context contextual value containing the data to be processed
   * @return null
   */
  @Override
  public Object visitKeyUsesName(FhirMapParser.KeyUsesNameContext context) {
    return this.visitChildren(context);
  }

  /**
   * Parse grammar rule keyUsesNameSource.
   *
   * @param context contextual value containing the data to be processed
   * @return UseNames.Source
   */
  @Override
  public Object visitKeyUsesNameSource(FhirMapParser.KeyUsesNameSourceContext context) {
    return FhirMapUseNames.Source;
  }

  /**
   * Parse grammar rule keyUsesNameTarget.
   *
   * @param context contextual value containing the data to be processed
   * @return UseNames.Target
   */
  @Override
  public Object visitKeyUsesNameTarget(FhirMapParser.KeyUsesNameTargetContext context) {
    return FhirMapUseNames.Target;
  }

  /**
   * Parse grammar rule keyUsesNameQueried.
   *
   * @param context contextual value containing the data to be processed
   * @return UseNames.Queried
   */
  @Override
  public Object visitKeyUsesNameQueried(FhirMapParser.KeyUsesNameQueriedContext context) {
    return FhirMapUseNames.Queried;
  }

  /**
   * Parse grammar rule keyUsesNameProduced.
   *
   * @param context contextual value containing the data to be processed
   * @return UseNames.Produced
   */
  @Override
  public Object visitKeyUsesNameProduced(FhirMapParser.KeyUsesNameProducedContext context) {
    return FhirMapUseNames.Produced;
  }

  /**
   * Parse grammar rule groupStart.
   *
   * @param context contextual value containing the data to be processed
   * @return GroupTypes
   */
  @Override
  public Object visitGroupStart(FhirMapParser.GroupStartContext context) {
    try {
      String identifier = null;
      if (context.identifier() != null) {
        identifier = (String) this.visit(context.identifier());
      }
      FhirMapGroupTypes group = FhirMapGroupTypes.NotSet;
      if (context.groupType() != null) {
        group = (FhirMapGroupTypes) this.visit(context.groupType());
      }
      String extension = null;
      if (context.groupExtends() != null) {
        extension = (String) this.visit(context.groupExtends());
      }
      this.executor.groupStart(identifier, group, extension);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Parse grammar rule groupEnd.
   *
   * @param context contextual value containing the data to be processed
   * @return GroupTypes
   */
  @Override
  public Object visitGroupEnd(FhirMapParser.GroupEndContext context) {
    try {
      this.executor.groupEnd();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Parse grammar rule groupExtends.
   *
   * @param context contextual value containing the data to be processed
   * @return List<String> of group
   */
  @Override
  public Object visitGroupExtends(FhirMapParser.GroupExtendsContext context) {
    return this.visit(context.identifier());
  }

  /**
   * Parse grammar rule groupType.
   *
   * @param context contextual value containing the data to be processed
   * @return GroupTypes
   */
  @Override
  public Object visitGroupType(FhirMapParser.GroupTypeContext context) {
    return this.visitChildren(context);
  }

  /**
   * Parse grammar rule groupTypeType.
   *
   * @param context contextual value containing the data to be processed
   * @return GroupTypes.Type
   */
  @Override
  public Object visitGroupTypeType(FhirMapParser.GroupTypeTypeContext context) {
    return FhirMapGroupTypes.Types;
  }

  /**
   * Parse grammar rule groupTypeTypeTypes.
   *
   * @param context contextual value containing the data to be processed
   * @return GroupTypes.TypeTypes
   */
  @Override
  public Object visitGroupTypeTypeTypes(FhirMapParser.GroupTypeTypeTypesContext context) {
    return FhirMapGroupTypes.TypeTypes;
  }

  /**
   * Parse grammar rule GroupInput.
   *
   * @param context contextual value containing the data to be processed
   * @return FhirMapGroupInput
   */
  @Override
  public Object visitGroupInput(FhirMapParser.GroupInputContext context) {
    try {
      this.executor.groupInput((String) this.visit(context.groupInputName()), (String) this.visit(context.groupInputType()), (FhirMapInputModes) this.visit(context.groupInputMode()));
    } catch (Exception e) {
      System.err.println(Arrays.toString(e.getStackTrace()));
    }
    return null;
  }

  /**
   * Parse grammar rule GroupInputModes.
   *
   * @param context contextual value containing the data to be processed
   * @return FhirMapInputModes
   */
  @Override
  public Object visitGroupInputMode(FhirMapParser.GroupInputModeContext context) {
    return this.visit(context.groupInputModes());
  }

  /**
   * Parse grammar rule GroupInputModesSource.
   *
   * @param context contextual value containing the data to be processed
   * @return FhirMapInputModes.Source
   */
  @Override
  public Object visitGroupInputModesSource(FhirMapParser.GroupInputModesSourceContext context) {
    return FhirMapInputModes.Source;
  }

  /**
   * Parse grammar rule GroupInputModesTarget.
   *
   * @param context contextual value containing the data to be processed
   * @return FhirMapInputModes.Target
   */
  @Override
  public Object visitGroupInputModesTarget(FhirMapParser.GroupInputModesTargetContext context) {
    return FhirMapInputModes.Target;
  }

  @Override
  public Object visitGroupCall(FhirMapParser.GroupCallContext context) {
    String id = null;
    List<String> params = null;
    this.executor.groupCall((String) this.visit(context.identifier()), (List<String>) this.visit(context.groupCallParameters()));
    return null;
  }

  @Override
  public Object visitGroupCallParameters(FhirMapParser.GroupCallParametersContext context) {
    return null;
    //TODO:not implemented
  }

  /**
   * Parse grammar rule ruleInstance
   *
   * @param context contextual value containing the data to be processed
   * @return null
   */
  @Override
  public Object visitRuleInstance(FhirMapParser.RuleInstanceContext context) {
    try {
      this.executor.ruleStart((List<String>) this.visit(context.ruleName()));
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
   * Parse grammar rule ruleTargetReference
   *
   * @param context contextual value containing the data to be processed
   * @return List<String>
   */
  @Override
  public Object visitRuleTargetReference(FhirMapParser.RuleTargetReferenceContext context) {
    try {
      List<String> ctx = null;
      String refSource = null;
      String targetVar = null;
      if (context.ruleTargetContext() != null) {
        ctx = (List<String>) this.visit(context.ruleTargetContext());
      }
      if (context.ruleTargetReferenceSource() != null) {
        refSource = (String) this.visit(context.ruleTargetReferenceSource());
      }
      if (context.ruleTargetVariable() != null) {
        targetVar = (String) this.visit(context.ruleTargetVariable());
      }

      this.executor.transformReference(ctx,
        refSource,
        targetVar);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Parse grammar rule ruleTargetTruncate
   *
   * @param context contextual value containing the data to be processed
   * @return List<String>
   */
  @Override
  public Object visitRuleTargetTruncate(FhirMapParser.RuleTargetTruncateContext context) {
    List<String> ctx = (List<String>) this.visit(context.ruleTargetContext());
    String truncateSource = (String) this.visit(context.ruleTargetTruncateSource());
    Integer truncateLength = (Integer) this.visit(context.ruleTargetTruncateLength());
    String targetVar = null;
    if (context.ruleTargetVariable() != null) {
      targetVar = (String) this.visit(context.ruleTargetVariable());
    }
    try {
      //Not implented in fhir code
      this.executor.transformTruncate(ctx, truncateSource, truncateLength, targetVar);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Parse grammar rule ruleTargetCast
   *
   * @param context contextual value containing the data to be processed
   * @return List<String>
   */
  @Override
  public Object visitRuleTargetCast(FhirMapParser.RuleTargetCastContext context) {
    List<String> ctx = (List<String>) this.visit(context.ruleTargetContext());
    String castSource = (String) this.visit(context.ruleTargetCastSource());
    String castType = (String) this.visit(context.ruleTargetCastType());
    String targetVar = null;
    if (context.ruleTargetVariable() != null) {
      targetVar = (String) this.visit(context.ruleTargetVariable());
    }
    try {
      this.executor.transformCast(ctx, castSource, castType, targetVar);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Parse grammar rule ruleTargetAs
   *
   * @param context contextual value containing the data to be processed
   * @return List<String>
   */
  @Override
  public Object visitRuleTargetAs(FhirMapParser.RuleTargetAsContext context) {
    try {
      this.executor.transformAs((List<String>) this.visit(context.ruleContext()), (String) this.visit(context.ruleTargetVariable()));
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Parse grammar rule ruleTargetAssign
   *
   * @param context contextual value containing the data to be processed
   * @return List<String>
   */
  @Override
  public Object visitRuleTargetAssign(FhirMapParser.RuleTargetAssignContext context) {
    try {
      List<String> ctx = null;
      String assignVal = null;
      String targetVar = null;
      if (context.ruleTargetContext() != null) {
        ctx = (List<String>) (this.visit(context.ruleTargetContext()));
      }
      if (context.ruleTargetAssignValue() != null) {
        assignVal = (String) (this.visit(context.ruleTargetAssignValue()));
      }
      if (context.ruleTargetVariable() != null) {
        targetVar = (String) (this.visit(context.ruleTargetVariable()));
      }
      this.executor.transformCopy(ctx, assignVal, targetVar);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Parse grammar rule ruleTargetCopy
   *
   * @param context contextual value containing the data to be processed
   * @return List<String>
   */
  @Override
  public Object visitRuleTargetCopy(FhirMapParser.RuleTargetCopyContext context) {
    List<String> ctx = (List<String>) this.visit(context.ruleTargetContext());
    String sourceVar = (String) this.visit(context.ruleTargetCopySource());
    String targetVar = null;
    if (context.ruleTargetVariable() != null) {
      targetVar = (String) this.visit(context.ruleTargetVariable());
    }
    try {
      this.executor.transformCopy(ctx, sourceVar, targetVar);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Parse grammar rule ruleTargetCreate
   *
   * @param context contextual value containing the data to be processed
   * @return List<String>
   */
  @Override
  public Object visitRuleTargetCreate(FhirMapParser.RuleTargetCreateContext context) {
    try {
      List<String> ctx = null;
      String createType = null;
      String ruleTarget = null;
      if (context.ruleTargetContext() != null) {
        ctx = (List<String>) this.visit(context.ruleTargetContext());
      }
      if (context.ruleTargetCreateType() != null) {
        createType = (String) this.visit(context.ruleTargetCreateType());
      }
      if (context.ruleTargetVariable() != null) {
        ruleTarget = (String) this.visit(context.ruleTargetVariable());
      }
      this.executor.transformCreate(ctx, createType, ruleTarget);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Parse grammar rule ruleTargetTranslate
   *
   * @param context contextual value containing the data to be processed
   * @return List<String>
   */
  @Override
  public Object visitRuleTargetTranslate(FhirMapParser.RuleTargetTranslateContext context) {
    try {
      List<String> ctx = null;
      String source = null;
      UrlData map = null;
      FhirMapTranslateOutputTypes types = null;
      String targetVar = null;

      if (context.ruleTargetContext() != null) {
        ctx = (List<String>) this.visit(context.ruleTargetContext());
      }
      if (context.ruleTargetTranslateSource() != null) {
        source = (String) this.visit(context.ruleTargetTranslateSource());
      }
      if (context.ruleTargetTranslateMap() != null) {
        map = (UrlData) this.visit(context.ruleTargetTranslateMap());
      }
      if (context.ruleTargetTranslateOutput() != null) {
        types = (FhirMapTranslateOutputTypes) this.visit(context.ruleTargetTranslateOutput());
      }
      if (context.ruleTargetVariable() != null) {
        targetVar = (String) this.visit(context.ruleTargetVariable());
      }


      this.executor.transformTranslate(ctx, source, map, types, targetVar);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Parse grammar rule ruleTargetTranslateOutputCode
   *
   * @param context contextual value containing the data to be processed
   * @return List<String>
   */
  @Override
  public Object visitRuleTargetTranslateOutputCode(FhirMapParser.RuleTargetTranslateOutputCodeContext context) {
    return FhirMapTranslateOutputTypes.Code;
  }

  /**
   * Parse grammar rule ruleTargetTranslateOutputSystem
   *
   * @param context contextual value containing the data to be processed
   * @return List<String>
   */
  @Override
  public Object visitRuleTargetTranslateOutputSystem(FhirMapParser.RuleTargetTranslateOutputSystemContext context) {
    return FhirMapTranslateOutputTypes.System;
  }

  /**
   * Parse grammar rule ruleTargetTranslateOutputDisplay
   *
   * @param context contextual value containing the data to be processed
   * @return List<String>
   */
  @Override
  public Object visitRuleTargetTranslateOutputDisplay(FhirMapParser.RuleTargetTranslateOutputDisplayContext context) {
    return FhirMapTranslateOutputTypes.Display;
  }

  /**
   * Parse grammar rule ruleTargetTranslateOutputCoding
   *
   * @param context contextual value containing the data to be processed
   * @return List<String>
   */
  @Override
  public Object visitRuleTargetTranslateOutputCoding(FhirMapParser.RuleTargetTranslateOutputCodingContext context) {
    return FhirMapTranslateOutputTypes.Coding;
  }

  /**
   * Parse grammar rule ruleTargetTranslateOutputCodeableConcept
   *
   * @param context contextual value containing the data to be processed
   * @return List<String>
   */
  @Override
  public Object visitRuleTargetTranslateOutputCodeableConcept(FhirMapParser.RuleTargetTranslateOutputCodeableConceptContext context) {
    return FhirMapTranslateOutputTypes.CodeableConcept;
  }

  /**
   * Parse grammar rule ruleTargetCp
   *
   * @param context contextual value containing the data to be processed
   * @return List<String>
   */
  @Override
  public Object visitRuleTargetCp(FhirMapParser.RuleTargetCpContext context) {
    List<String> ctx = (List<String>) this.visit(context.ruleTargetContext());
    UrlData cpSystem = null;
    if (context.ruleTargetCpSystem() != null) {
      cpSystem = (UrlData) this.visit(context.ruleTargetCpSystem());
    }
    String cpVar = (String) this.visit(context.ruleTargetCpVariable());
    String targetVar = null;
    if (context.ruleTargetVariable() != null) {
      targetVar = (String) this.visit(context.ruleTargetVariable());
    }
    try {
      this.executor.transformCp(ctx, cpSystem, cpVar, targetVar);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Parse grammar rule ruleTargetAppend
   *
   * @param context contextual value containing the data to be processed
   * @return List<String>
   */
  @Override
  public Object visitRuleTargetAppend(FhirMapParser.RuleTargetAppendContext context) {
    List<String> ctx = (List<String>) this.visit(context.ruleTargetContext());
    List<String> appendSource = (List<String>) this.visit(context.ruleTargetAppendSources());
    String targetVar = null;
    if (context.ruleTargetVariable() != null) {
      targetVar = (String) this.visit(context.ruleTargetVariable());
    }
    try {
      this.executor.transformAppend(ctx, appendSource, targetVar);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Parse grammar rule ruleTargetAppendSources
   *
   * @param context contextual value containing the data to be processed
   * @return List<String>
   */
  @Override
  public Object visitRuleTargetAppendSources(FhirMapParser.RuleTargetAppendSourcesContext context) {
    ArrayList<String> values = new ArrayList<String>();
    if (context.ruleTargetAppendSource() != null) {
      for (ParseTree treeItem : context.ruleTargetAppendSource()) {
        values.add((String) this.visit(treeItem));
      }
    }
    return values;
    //return VisitorExtensions.VisitMultiple(this, context.ruleTargetAppendSource(), values);
  }

  /**
   * Parse grammar rule ruleTargetC
   *
   * @param context contextual value containing the data to be processed
   * @return List<String>
   */
  @Override
  public Object visitRuleTargetC(FhirMapParser.RuleTargetCContext context) {
    try {
      List<String> ctx = null;
      UrlData system = null;
      String code = null;
      String display = null;
      String targetVar = null;

      if (context.ruleTargetContext() != null) {
        ctx = (List<String>) this.visit(context.ruleTargetContext());
      }
      if (context.ruleTargetCSystem() != null) {
        system = (UrlData) this.visit(context.ruleTargetCSystem());
      }
      if (context.ruleTargetCCode() != null) {
        code = (String) this.visit(context.ruleTargetCCode());
      }
      if (context.ruleTargetCDisplay() != null) {
        display = (String) this.visit(context.ruleTargetCDisplay());
      }
      if (context.ruleTargetVariable() != null) {
        targetVar = (String) this.visit(context.ruleTargetVariable());
      }
      this.executor.transformCoding(ctx, system, code, display, targetVar);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Parse grammar rule ruleTargetCC1
   *
   * @param context contextual value containing the data to be processed
   * @return List<String>
   */
  @Override
  public Object visitRuleTargetCC1(FhirMapParser.RuleTargetCC1Context context) {
    List<String> ctx = (List<String>) this.visit(context.ruleTargetContext());
    String ccText = (String) this.visit(context.ruleTargetCC1Text());
    String targetVar = null;
    if (context.ruleTargetVariable() != null) {
      targetVar = (String) this.visit(context.ruleTargetVariable());
    }
    try {
      this.executor.transformCodeableConcept(ctx, ccText, targetVar);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Parse grammar rule ruleTargetCC2
   *
   * @param context contextual value containing the data to be processed
   * @return List<String>
   */
  @Override
  public Object visitRuleTargetCC2(FhirMapParser.RuleTargetCC2Context context) {
    try {
      List<String> ctx = null;
      UrlData system = null;
      String code = null;
      String display = null;
      String var = null;
      if (context.ruleTargetContext() != null) {
        ctx = (List<String>) this.visit(context.ruleTargetContext());
      }
      if (context.ruleTargetCC2System() != null) {
        system = (UrlData) this.visit(context.ruleTargetCC2System());
      }
      if (context.ruleTargetCC2Code() != null) {
        code = (String) this.visit(context.ruleTargetCC2Code());
      }
      if (context.ruleTargetCC2Display() != null) {
        display = (String) this.visit(context.ruleTargetCC2Display());
      }
      if (context.ruleTargetVariable() != null) {
        var = (String) this.visit(context.ruleTargetVariable());
      }
      this.executor.transformCodeableConcept(ctx, system, code, display, var);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Parse grammar rule ruleTargetContext
   *
   * @param context contextual value containing the data to be processed
   * @return List<String>
   */
  @Override
  public Object visitRuleTargetContext(FhirMapParser.RuleTargetContextContext context) {
    return this.visit(context.ruleContext());
  }

  /**
   * Parse grammar rule ruleTargetVariable
   *
   * @param context contextual value containing the data to be processed
   * @return List<String>
   */
  @Override
  public Object visitRuleTargetVariable(FhirMapParser.RuleTargetVariableContext context) {
    return this.visit(context.identifier());
  }

  /**
   * Parse grammar rule ruleTargetEscape
   *
   * @param context contextual value containing the data to be processed
   * @return List<String>
   */
  @Override
  public Object visitRuleTargetEscape(FhirMapParser.RuleTargetEscapeContext context) {
    try {
      List<String> ctx = null;
      String var = null;
      String str1 = null;
      String str2 = null;
      String targetVar = null;
      if (context.ruleTargetContext() != null) {
        ctx = (List<String>) this.visit(context.ruleTargetContext());
      }
      if (context.ruleTargetEscapeVariable() != null) {
        var = (String) this.visit(context.ruleTargetEscapeVariable());
      }
      if (context.ruleTargetEscapeString1() != null) {
        str1 = (String) this.visit(context.ruleTargetEscapeString1());
      }
      if (context.ruleTargetEscapeString2() != null) {
        str2 = (String) this.visit(context.ruleTargetEscapeString2());
      }
      if (context.ruleTargetVariable() != null) {
        targetVar = (String) this.visit(context.ruleTargetVariable());
      }

      this.executor.transformEscape(ctx, var, str1, str2, targetVar);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Parse grammar rule ruleTargetExtension1 (no arguments)
   * @param context contextual value containing the data to be processed
   * @return null
   */
  @Override
  public Object visitRuleTargetExtension1(FhirMapParser.RuleTargetExtension1Context context) {
    try {
      String variable = null;
      if (context.ruleTargetVariable() != null) {
        variable = (String) this.visit(context.ruleTargetVariable());
      }
      this.executor.transformExtension((List<String>) this.visit(context.ruleTargetContext()), variable);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Parse grammar rule ruleTargetExtension2 (with arguments arguments)
   * @param context contextual value containing the data to be processed
   * @return null
   */
  @Override
  public Object visitRuleTargetExtension2(FhirMapParser.RuleTargetExtension2Context context) {
    try {
      String variable = null;
      if (context.ruleTargetVariable() != null) {
        variable = (String) this.visit(context.ruleTargetVariable());
      }
      this.executor.transformExtension((List<String>) this.visit(context.ruleTargetContext()),
        (UrlData) this.visit(context.ruleTargetExtension2Uri()),
        (String) this.visit(context.ruleTargetExtension2Title()),
        (String) this.visit(context.ruleTargetExtension2Mode()),
        (String) this.visit(context.ruleTargetExtension2Parent()),
        (String) this.visit(context.ruleTargetExtension2Text1()),
        (String) this.visit(context.ruleTargetExtension2Text2()),
        (Integer) this.visit(context.ruleTargetExtension2Min()),
        (String) this.visit(context.ruleTargetExtension2Max()),
        (String) this.visit(context.ruleTargetExtension2Type()),
        variable);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Parse grammar rule ruleTargetEvaluate
   *
   * @param context contextual value containing the data to be processed
   * @return List<String>
   */
  @Override
  public Object visitRuleTargetEvaluate(FhirMapParser.RuleTargetEvaluateContext context) {
    try {
      List<String> ctx = null;
      String obj = null;
      String element = null;
      String targetVar = null;
      if (context.ruleTargetContext() != null) {
        ctx = (List<String>) this.visit(context.ruleTargetContext());
      }
      if (context.ruleTargetEvaluateObject() != null) {
        obj = (String) this.visit(context.ruleTargetEvaluateObject());
      }
      if (context.ruleTargetEvaluateObjectElement() != null) {
        element = (String) this.visit(context.ruleTargetEvaluateObjectElement());
      }
      if (context.ruleTargetVariable() != null) {
        targetVar = (String) this.visit(context.ruleTargetVariable());
      }
      this.executor.transformEvaluate(ctx, obj, element, targetVar);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Parse grammar rule ruleTargetId
   *
   * @param context contextual value containing the data to be processed
   * @return List<String>
   */
  @Override
  public Object visitRuleTargetId(FhirMapParser.RuleTargetIdContext context) {
    List<String> ctx = (List<String>) this.visit(context.ruleTargetContext());
    UrlData idSystem = (UrlData) this.visit(context.ruleTargetIdSystem());
    String idValue = (String) this.visit(context.ruleTargetIdValue());
    String idType = null;
    if (context.ruleTargetIdType() != null) {
      idType = (String) this.visit(context.ruleTargetIdType());
    }
    String targetVar = null;
    if (context.ruleTargetVariable() != null) {
      targetVar = (String) this.visit(context.ruleTargetVariable());
    }
    try {
      this.executor.transformId(ctx, idSystem, idValue, idType, targetVar);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Parse grammar rule ruleTargetPointer
   *
   * @param context contextual value containing the data to be processed
   * @return List<String>
   */
  @Override
  public Object visitRuleTargetPointer(FhirMapParser.RuleTargetPointerContext context) {
    List<String> ctx = null;
    String pointerResource = null;
    String tgtVar = null;
    if (context.ruleTargetContext() != null)
      ctx = (List<String>) this.visit(context.ruleTargetContext());
    if (context.ruleTargetPointerResource() != null)
      pointerResource = (String) this.visit(context.ruleTargetPointerResource());
    if (context.ruleTargetVariable() != null)
      tgtVar = (String) this.visit(context.ruleTargetVariable());
    try {
      this.executor.transformPointer(ctx, pointerResource, tgtVar);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Parse grammar rule ruleTargetQty1
   *
   * @param context contextual value containing the data to be processed
   * @return List<String>
   */
  @Override
  public Object visitRuleTargetQty1(FhirMapParser.RuleTargetQty1Context context) {
    try {
      List<String> ctx = null;
      String text = null;
      String targetVar = null;
      if (context.ruleTargetContext() != null) {
        ctx = (List<String>) this.visit(context.ruleTargetContext());
      }
      if (context.ruleTargetQty1Text() != null) {
        text = (String) this.visit(context.ruleTargetQty1Text());
      }
      if (context.ruleTargetVariable() != null) {
        targetVar = (String) this.visit(context.ruleTargetVariable());
      }
      this.executor.transformQty(ctx, text, targetVar);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Parse grammar rule ruleTargetQty2
   *
   * @param context contextual value containing the data to be processed
   * @return List<String>
   */
  @Override
  public Object visitRuleTargetQty2(FhirMapParser.RuleTargetQty2Context context) {
    try {
      List<String> ctx = null;
      String value = null;
      String unitSystem = null;
      UrlData system = null;
      String targetVar = null;
      if (context.ruleTargetContext() != null) {
        ctx = (List<String>) this.visit(context.ruleTargetContext());
      }
      if (context.ruleTargetQty2Value() != null) {
        value = (String) this.visit(context.ruleTargetQty2Value());
      }
      if (context.ruleTargetQty2UnitString() != null) {
        unitSystem = (String) this.visit(context.ruleTargetQty2UnitString());
      }
      if (context.ruleTargetQty2System() != null) {
        system = (UrlData) this.visit(context.ruleTargetQty2System());
      }
      if (context.ruleTargetVariable() != null) {
        targetVar = (String) this.visit(context.ruleTargetVariable());
      }
      this.executor.transformQty(ctx, value, unitSystem, system, targetVar);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Parse grammar rule ruleTargetQty3
   *
   * @param context contextual value containing the data to be processed
   * @return List<String>
   */
  @Override
  public Object visitRuleTargetQty3(FhirMapParser.RuleTargetQty3Context context) {
    try {
      List<String> ctx = null;
      ctx = (List<String>) this.visit(context.ruleTargetContext());
      String value = (String) this.visit(context.ruleTargetQty3Value());

      String unitString = (String) this.visit(context.ruleTargetQty3UnitString());
      String codeVar = (String) this.visit(context.ruleTargetQty3CodeVariable());
      String targetVar = null;
      if (context.ruleTargetVariable() != null) {
        targetVar = (String) this.visit(context.ruleTargetVariable());
      }
      this.executor.transformQty(ctx, value, unitString, codeVar, targetVar);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Parse grammar rule ruleTargetUuid
   *
   * @param context contextual value containing the data to be processed
   * @return List<String>
   */
  @Override
  public Object visitRuleTargetUuid(FhirMapParser.RuleTargetUuidContext context) {
    List<String> ctx = (List<String>) this.visit(context.ruleTargetContext());
    String targetVar = null;
    if (context.ruleTargetVariable() != null) {
      targetVar = (String) this.visit(context.ruleTargetVariable());
    }
    try {
      //not implemented
      this.executor.transformUuid(ctx, targetVar);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Parse grammar rule ruleTargetEscape
   *
   * @param context contextual value containing the data to be processed
   * @return List<String>
   */
  @Override
  public Object visitRuleTargetDateOp(FhirMapParser.RuleTargetDateOpContext context) {
    List<String> ctx = (List<String>) this.visit(context.ruleTargetContext());
    String dateOpVar = (String) this.visit(context.ruleTargetDateOpVariable());
    String operation = (String) this.visit(context.ruleTargetDateOpOperation());
    String dateOpVar2 = null;
    if (context.ruleTargetDateOpVariable2() != null) {
      dateOpVar2 = (String) this.visit(context.ruleTargetDateOpVariable2());
    }
    String targetVar = null;
    if (context.ruleTargetVariable() != null) {
      targetVar = (String) this.visit(context.ruleTargetVariable());
    }
    try {
      this.executor.transformDateOp(ctx, dateOpVar, operation, dateOpVar2, targetVar);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Parse grammar rule ruleName
   *
   * @param context contextual value containing the data to be processed
   * @return List<String>
   */
  @Override
  public Object visitRuleName(FhirMapParser.RuleNameContext context) {
    ArrayList<String> values = new ArrayList<String>();
    if (context.identifier() != null) {
      for (ParseTree treeItem : context.identifier()) {
        values.add((String) this.visit(treeItem));
      }
    }
    return values;
    //return VisitorExtensions.VisitMultiple(this, context.identifier(), values);
  }

  /**
   * Parse grammar rule ruleSource
   *
   * @param context contextual value containing the data to be processed
   * @return FhirMapRuleType instance
   */
  @Override
  public Object visitRuleSource(FhirMapParser.RuleSourceContext context) {
    try {
      List<String> ctx;
      FhirMapParser.RuleTypeContext typeContext = context.ruleType();
      FhirMapRuleType type = null;
      FhirMapParser.RuleDefaultContext defaultContext = context.ruleDefault();
      String defaultVal = null;
      FhirMapParser.RuleListOptionContext listOptionContext = context.ruleListOption();
      FhirMapListOptions listOptions = null;
      FhirMapParser.RuleVariableContext variableContext = context.ruleVariable();
      String var = null;
      FhirMapParser.RuleWherePathContext wherePathContext = context.ruleWherePath();
      String where = null;
      FhirMapParser.RuleCheckPathContext checkPathContext = context.ruleCheckPath();
      String check = null;
      ctx = (List<String>) this.visit(context.ruleContext());
      if (typeContext != null) {
        type = (FhirMapRuleType) this.visit(typeContext);
      }
      if (defaultContext != null) {
        defaultVal = (String) this.visit(defaultContext);
      }
      if (listOptionContext != null) {
        listOptions = (FhirMapListOptions) this.visit(listOptionContext);
      }
      if (variableContext != null) {
        var = (String) this.visit(variableContext);
      }
      if (wherePathContext != null) {
        where = (String) this.visit(wherePathContext);
      }
      if (checkPathContext != null) {
        check = (String) this.visit(checkPathContext);
      }
      this.executor.ruleSource(ctx,
        type,
        defaultVal,
        listOptions,
        var,
        where,
        check
      );
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Parse grammar rule ruleType
   *
   * @param context contextual value containing the data to be processed
   * @return FhirMapRuleType instance
   */
  @Override
  public Object visitRuleType(FhirMapParser.RuleTypeContext context) {
    FhirMapRuleType tempVar = new FhirMapRuleType();
    tempVar.TypeName = (String) this.visit(context.identifier());
    for (ParseTree treeItem : context.integer()) {
      List<Integer> occurances;
      if (tempVar.Occurrences == null) {
        occurances = new ArrayList<>();
      } else {
        occurances = tempVar.Occurrences;

      }
      int i = (int) this.visit(treeItem);
      occurances.add(i);
      tempVar.Occurrences = occurances;
    }

    return tempVar;
  }

  /**
   * Parse grammar rule ruleDefault
   * #! Verify format of default value. Currently accepts an identifier.
   * #! Also write test for this...
   *
   * @param context contextual value containing the data to be processed
   * @return String
   */
  @Override
  public Object visitRuleDefault(FhirMapParser.RuleDefaultContext context) {
    String identifier = null;

    try {
//      return VisitorExtensions.<String>VisitOrDefault(this, context.identifier(), String.class);
      if (context.identifier() != null) {
        identifier = (String) this.visit(context.identifier());
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Parse grammar rule ruleVariable
   *
   * @param context contextual value containing the data to be processed
   * @return String
   */
  @Override
  public Object visitRuleVariable(FhirMapParser.RuleVariableContext context) {
    return this.visit(context.identifier());
  }

  /**
   * Parse grammar rule ruleContext
   *
   * @param context contextual value containing the data to be processed
   * @return List<String>
   */
  @Override
  public Object visitRuleContext(FhirMapParser.RuleContextContext context) {
    ArrayList<String> values = new ArrayList<String>();
    if (context.ruleContextElement() != null) {
      for (ParseTree treeItem : context.ruleContextElement()) {
        values.add((String) this.visit(treeItem));
      }
    }
    return values;
  }

  /**
   * Parse grammar rule ruleContextElement
   *
   * @param context contextual value containing the data to be processed
   * @return String
   */
  @Override
  public Object visitRuleContextElement(FhirMapParser.RuleContextElementContext context) {
    return this.visitChildren(context);
  }
}
