// Generated from FhirMapJava.g4 by ANTLR 4.7
package org.hl7.fhir.r4.utils.transform.deserializer.grammar.antlr.javaAntlr;

import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;

import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class FhirMapJavaParser extends Parser {
  static {
    RuntimeMetaData.checkVersion("4.7", RuntimeMetaData.VERSION);
  }

  protected static final DFA[] _decisionToDFA;
  protected static final PredictionContextCache _sharedContextCache =
    new PredictionContextCache();
  public static final int
    T__0 = 1, T__1 = 2, T__2 = 3, T__3 = 4, T__4 = 5, T__5 = 6, T__6 = 7, T__7 = 8, T__8 = 9,
    T__9 = 10, T__10 = 11, HEX = 12, DIGITS = 13, SOURCE = 14, TARGET = 15, QUERIED = 16,
    PRODUCED = 17, QIDENTIFIER = 18, QSTRING = 19, APPEND = 20, AS = 21, CAST = 22, C = 23,
    CC = 24, CODE = 25, CP = 26, CHECK = 27, CODEABLECONCEPT = 28, CODING = 29, COPY = 30,
    CREATE = 31, DATEOP = 32, DEFAULT = 33, DISPLAY = 34, ENDGROUP = 35, ESCAPE = 36,
    EXTENSION = 37, EVALUATE = 38, EXTENDS = 39, FOR = 40, GROUP = 41, ID = 42, IMPORTS = 43,
    INPUT = 44, MAKE = 45, MAP = 46, POINTER = 47, QTY = 48, REFERENCE = 49, SYSTEM = 50,
    THEN = 51, TRANSLATE = 52, TRUNCATE = 53, TYPES = 54, TYPE_TYPES = 55, UUID = 56,
    USES = 57, WHERE = 58, IDENTIFIER = 59, WS = 60, LINE_COMMENT = 61;
  public static final int
    RULE_mappingUnit = 0, RULE_keyMap = 1, RULE_keyUses = 2, RULE_keyUsesName = 3,
    RULE_keyUsesNameSource = 4, RULE_keyUsesNameTarget = 5, RULE_keyUsesNameQueried = 6,
    RULE_keyUsesNameProduced = 7, RULE_keyImports = 8, RULE_group = 9, RULE_groupStart = 10,
    RULE_groupExtends = 11, RULE_groupEnd = 12, RULE_groupType = 13, RULE_groupTypeValue = 14,
    RULE_groupTypeType = 15, RULE_groupTypeTypeTypes = 16, RULE_groupInput = 17,
    RULE_groupInputName = 18, RULE_groupInputType = 19, RULE_groupInputMode = 20,
    RULE_groupInputModes = 21, RULE_groupInputModesSource = 22, RULE_groupInputModesTarget = 23,
    RULE_groupItem = 24, RULE_groupCall = 25, RULE_groupCallParameters = 26,
    RULE_ruleInstance = 27, RULE_ruleName = 28, RULE_ruleSources = 29, RULE_ruleSource = 30,
    RULE_ruleContext = 31, RULE_ruleContextElement = 32, RULE_ruleType = 33,
    RULE_ruleDefault = 34, RULE_ruleListOption = 35, RULE_ruleVariable = 36,
    RULE_ruleWherePath = 37, RULE_ruleCheckPath = 38, RULE_ruleMake = 39,
    RULE_ruleMakeTargets = 40, RULE_ruleMakeDependents = 41, RULE_ruleMakeDependentsGroupItems = 42,
    RULE_ruleTarget = 43, RULE_ruleTargetContext = 44, RULE_ruleTargetAs = 45,
    RULE_ruleTargetAssign = 46, RULE_ruleTargetAssignValue = 47, RULE_ruleTargetAppend = 48,
    RULE_ruleTargetAppendSources = 49, RULE_ruleTargetAppendSource = 50, RULE_ruleTargetC = 51,
    RULE_ruleTargetCSystem = 52, RULE_ruleTargetCCode = 53, RULE_ruleTargetCDisplay = 54,
    RULE_ruleTargetCast = 55, RULE_ruleTargetCastSource = 56, RULE_ruleTargetCastType = 57,
    RULE_ruleTargetCC = 58, RULE_ruleTargetCC1 = 59, RULE_ruleTargetCC1Text = 60,
    RULE_ruleTargetCC2 = 61, RULE_ruleTargetCC2System = 62, RULE_ruleTargetCC2Code = 63,
    RULE_ruleTargetCC2Display = 64, RULE_ruleTargetCp = 65, RULE_ruleTargetCpSystem = 66,
    RULE_ruleTargetCpVariable = 67, RULE_ruleTargetCopy = 68, RULE_ruleTargetCopySource = 69,
    RULE_ruleTargetCreate = 70, RULE_ruleTargetCreateType = 71, RULE_ruleTargetDateOp = 72,
    RULE_ruleTargetDateOpVariable = 73, RULE_ruleTargetDateOpOperation = 74,
    RULE_ruleTargetDateOpVariable2 = 75, RULE_ruleTargetExtension = 76, RULE_ruleTargetExtension1 = 77,
    RULE_ruleTargetExtension2 = 78, RULE_ruleTargetExtension2Uri = 79, RULE_ruleTargetExtension2Title = 80,
    RULE_ruleTargetExtension2Mode = 81, RULE_ruleTargetExtension2Parent = 82,
    RULE_ruleTargetExtension2Text1 = 83, RULE_ruleTargetExtension2Text2 = 84,
    RULE_ruleTargetExtension2Min = 85, RULE_ruleTargetExtension2Max = 86,
    RULE_ruleTargetExtension2Type = 87, RULE_ruleTargetEscape = 88, RULE_ruleTargetEscapeVariable = 89,
    RULE_ruleTargetEscapeString1 = 90, RULE_ruleTargetEscapeString2 = 91,
    RULE_ruleTargetEvaluate = 92, RULE_ruleTargetEvaluateObject = 93, RULE_ruleTargetEvaluateObjectElement = 94,
    RULE_ruleTargetId = 95, RULE_ruleTargetIdSystem = 96, RULE_ruleTargetIdValue = 97,
    RULE_ruleTargetIdType = 98, RULE_ruleTargetPointer = 99, RULE_ruleTargetPointerResource = 100,
    RULE_ruleTargetQty = 101, RULE_ruleTargetQty1 = 102, RULE_ruleTargetQty1Text = 103,
    RULE_ruleTargetQty2 = 104, RULE_ruleTargetQty2Value = 105, RULE_ruleTargetQty2UnitString = 106,
    RULE_ruleTargetQty2System = 107, RULE_ruleTargetQty3 = 108, RULE_ruleTargetQty3Value = 109,
    RULE_ruleTargetQty3UnitString = 110, RULE_ruleTargetQty3CodeVariable = 111,
    RULE_ruleTargetReference = 112, RULE_ruleTargetReferenceSource = 113,
    RULE_ruleTargetTranslate = 114, RULE_ruleTargetTranslateSource = 115,
    RULE_ruleTargetTranslateMap = 116, RULE_ruleTargetTranslateOutput = 117,
    RULE_ruleTargetTranslateOutputCode = 118, RULE_ruleTargetTranslateOutputSystem = 119,
    RULE_ruleTargetTranslateOutputDisplay = 120, RULE_ruleTargetTranslateOutputCoding = 121,
    RULE_ruleTargetTranslateOutputCodeableConcept = 122, RULE_ruleTargetTruncate = 123,
    RULE_ruleTargetTruncateSource = 124, RULE_ruleTargetTruncateLength = 125,
    RULE_ruleTargetUuid = 126, RULE_ruleTargetVariable = 127, RULE_fhirPath = 128,
    RULE_identifier = 129, RULE_integer = 130, RULE_quotedIdentifier = 131,
    RULE_quotedStringWQuotes = 132, RULE_quotedString = 133, RULE_quotedUrl = 134,
    RULE_structureDefinition = 135, RULE_structureMap = 136;
  public static final String[] ruleNames = {
    "mappingUnit", "keyMap", "keyUses", "keyUsesName", "keyUsesNameSource",
    "keyUsesNameTarget", "keyUsesNameQueried", "keyUsesNameProduced", "keyImports",
    "group", "groupStart", "groupExtends", "groupEnd", "groupType", "groupTypeValue",
    "groupTypeType", "groupTypeTypeTypes", "groupInput", "groupInputName",
    "groupInputType", "groupInputMode", "groupInputModes", "groupInputModesSource",
    "groupInputModesTarget", "groupItem", "groupCall", "groupCallParameters",
    "ruleInstance", "ruleName", "ruleSources", "ruleSource", "ruleContext",
    "ruleContextElement", "ruleType", "ruleDefault", "ruleListOption", "ruleVariable",
    "ruleWherePath", "ruleCheckPath", "ruleMake", "ruleMakeTargets", "ruleMakeDependents",
    "ruleMakeDependentsGroupItems", "ruleTarget", "ruleTargetContext", "ruleTargetAs",
    "ruleTargetAssign", "ruleTargetAssignValue", "ruleTargetAppend", "ruleTargetAppendSources",
    "ruleTargetAppendSource", "ruleTargetC", "ruleTargetCSystem", "ruleTargetCCode",
    "ruleTargetCDisplay", "ruleTargetCast", "ruleTargetCastSource", "ruleTargetCastType",
    "ruleTargetCC", "ruleTargetCC1", "ruleTargetCC1Text", "ruleTargetCC2",
    "ruleTargetCC2System", "ruleTargetCC2Code", "ruleTargetCC2Display", "ruleTargetCp",
    "ruleTargetCpSystem", "ruleTargetCpVariable", "ruleTargetCopy", "ruleTargetCopySource",
    "ruleTargetCreate", "ruleTargetCreateType", "ruleTargetDateOp", "ruleTargetDateOpVariable",
    "ruleTargetDateOpOperation", "ruleTargetDateOpVariable2", "ruleTargetExtension",
    "ruleTargetExtension1", "ruleTargetExtension2", "ruleTargetExtension2Uri",
    "ruleTargetExtension2Title", "ruleTargetExtension2Mode", "ruleTargetExtension2Parent",
    "ruleTargetExtension2Text1", "ruleTargetExtension2Text2", "ruleTargetExtension2Min",
    "ruleTargetExtension2Max", "ruleTargetExtension2Type", "ruleTargetEscape",
    "ruleTargetEscapeVariable", "ruleTargetEscapeString1", "ruleTargetEscapeString2",
    "ruleTargetEvaluate", "ruleTargetEvaluateObject", "ruleTargetEvaluateObjectElement",
    "ruleTargetId", "ruleTargetIdSystem", "ruleTargetIdValue", "ruleTargetIdType",
    "ruleTargetPointer", "ruleTargetPointerResource", "ruleTargetQty", "ruleTargetQty1",
    "ruleTargetQty1Text", "ruleTargetQty2", "ruleTargetQty2Value", "ruleTargetQty2UnitString",
    "ruleTargetQty2System", "ruleTargetQty3", "ruleTargetQty3Value", "ruleTargetQty3UnitString",
    "ruleTargetQty3CodeVariable", "ruleTargetReference", "ruleTargetReferenceSource",
    "ruleTargetTranslate", "ruleTargetTranslateSource", "ruleTargetTranslateMap",
    "ruleTargetTranslateOutput", "ruleTargetTranslateOutputCode", "ruleTargetTranslateOutputSystem",
    "ruleTargetTranslateOutputDisplay", "ruleTargetTranslateOutputCoding",
    "ruleTargetTranslateOutputCodeableConcept", "ruleTargetTruncate", "ruleTargetTruncateSource",
    "ruleTargetTruncateLength", "ruleTargetUuid", "ruleTargetVariable", "fhirPath",
    "identifier", "integer", "quotedIdentifier", "quotedStringWQuotes", "quotedString",
    "quotedUrl", "structureDefinition", "structureMap"
  };

  private static final String[] _LITERAL_NAMES = {
    null, "'='", "';'", "':'", "'('", "')'", "','", "'.'", "'..'", "'xxxxyyyyyzzzzzz'",
    "'{'", "'}'", null, null, "'source'", "'target'", "'queried'", "'produced'",
    null, null, "'append'", "'as'", "'cast'", "'c'", "'cc'", "'code'", "'cp'",
    "'check'", "'codeableConcept'", "'coding'", "'copy'", "'create'", "'dateOp'",
    "'default'", "'display'", "'endgroup'", "'escape'", "'extension'", "'evaluate'",
    "'extends'", "'for'", "'group'", "'id'", "'imports'", "'input'", "'make'",
    "'map'", "'pointer'", "'qty'", "'reference'", "'system'", "'then'", "'translate'",
    "'truncate'", "'types'", "'type+types'", "'uuid'", "'uses'", "'where'"
  };
  private static final String[] _SYMBOLIC_NAMES = {
    null, null, null, null, null, null, null, null, null, null, null, null,
    "HEX", "DIGITS", "SOURCE", "TARGET", "QUERIED", "PRODUCED", "QIDENTIFIER",
    "QSTRING", "APPEND", "AS", "CAST", "C", "CC", "CODE", "CP", "CHECK", "CODEABLECONCEPT",
    "CODING", "COPY", "CREATE", "DATEOP", "DEFAULT", "DISPLAY", "ENDGROUP",
    "ESCAPE", "EXTENSION", "EVALUATE", "EXTENDS", "FOR", "GROUP", "ID", "IMPORTS",
    "INPUT", "MAKE", "MAP", "POINTER", "QTY", "REFERENCE", "SYSTEM", "THEN",
    "TRANSLATE", "TRUNCATE", "TYPES", "TYPE_TYPES", "UUID", "USES", "WHERE",
    "IDENTIFIER", "WS", "LINE_COMMENT"
  };
  public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

  /**
   * @deprecated Use {@link #VOCABULARY} instead.
   */
  @Deprecated
  public static final String[] tokenNames;

  static {
    tokenNames = new String[_SYMBOLIC_NAMES.length];
    for (int i = 0; i < tokenNames.length; i++) {
      tokenNames[i] = VOCABULARY.getLiteralName(i);
      if (tokenNames[i] == null) {
        tokenNames[i] = VOCABULARY.getSymbolicName(i);
      }

      if (tokenNames[i] == null) {
        tokenNames[i] = "<INVALID>";
      }
    }
  }

  @Override
  @Deprecated
  public String[] getTokenNames() {
    return tokenNames;
  }

  @Override

  public Vocabulary getVocabulary() {
    return VOCABULARY;
  }

  @Override
  public String getGrammarFileName() {
    return "FhirMapJava.g4";
  }

  @Override
  public String[] getRuleNames() {
    return ruleNames;
  }

  @Override
  public String getSerializedATN() {
    return _serializedATN;
  }

  @Override
  public ATN getATN() {
    return _ATN;
  }

  public FhirMapJavaParser(TokenStream input) {
    super(input);
    _interp = new ParserATNSimulator(this, _ATN, _decisionToDFA, _sharedContextCache);
  }

  public static class MappingUnitContext extends ParserRuleContext {
    public KeyMapContext keyMap() {
      return getRuleContext(KeyMapContext.class, 0);
    }

    public GroupContext group() {
      return getRuleContext(GroupContext.class, 0);
    }

    public List<KeyUsesContext> keyUses() {
      return getRuleContexts(KeyUsesContext.class);
    }

    public KeyUsesContext keyUses(int i) {
      return getRuleContext(KeyUsesContext.class, i);
    }

    public List<KeyImportsContext> keyImports() {
      return getRuleContexts(KeyImportsContext.class);
    }

    public KeyImportsContext keyImports(int i) {
      return getRuleContext(KeyImportsContext.class, i);
    }

    public MappingUnitContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_mappingUnit;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor)
        return ((FhirMapJavaVisitor<? extends T>) visitor).visitMappingUnit(this);
      else return visitor.visitChildren(this);
    }
  }

  public final MappingUnitContext mappingUnit() throws RecognitionException {
    MappingUnitContext _localctx = new MappingUnitContext(_ctx, getState());
    enterRule(_localctx, 0, RULE_mappingUnit);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(274);
        keyMap();
        setState(276);
        _errHandler.sync(this);
        _la = _input.LA(1);
        do {
          {
            {
              setState(275);
              keyUses();
            }
          }
          setState(278);
          _errHandler.sync(this);
          _la = _input.LA(1);
        } while (_la == USES);
        setState(283);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la == IMPORTS) {
          {
            {
              setState(280);
              keyImports();
            }
          }
          setState(285);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
        setState(286);
        group();
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class KeyMapContext extends ParserRuleContext {
    public TerminalNode MAP() {
      return getToken(FhirMapJavaParser.MAP, 0);
    }

    public StructureMapContext structureMap() {
      return getRuleContext(StructureMapContext.class, 0);
    }

    public QuotedStringContext quotedString() {
      return getRuleContext(QuotedStringContext.class, 0);
    }

    public KeyMapContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_keyMap;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor) return ((FhirMapJavaVisitor<? extends T>) visitor).visitKeyMap(this);
      else return visitor.visitChildren(this);
    }
  }

  public final KeyMapContext keyMap() throws RecognitionException {
    KeyMapContext _localctx = new KeyMapContext(_ctx, getState());
    enterRule(_localctx, 2, RULE_keyMap);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(288);
        match(MAP);
        setState(289);
        structureMap();
        setState(290);
        match(T__0);
        setState(291);
        quotedString();
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class KeyUsesContext extends ParserRuleContext {
    public TerminalNode USES() {
      return getToken(FhirMapJavaParser.USES, 0);
    }

    public StructureDefinitionContext structureDefinition() {
      return getRuleContext(StructureDefinitionContext.class, 0);
    }

    public TerminalNode AS() {
      return getToken(FhirMapJavaParser.AS, 0);
    }

    public KeyUsesNameContext keyUsesName() {
      return getRuleContext(KeyUsesNameContext.class, 0);
    }

    public KeyUsesContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_keyUses;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor) return ((FhirMapJavaVisitor<? extends T>) visitor).visitKeyUses(this);
      else return visitor.visitChildren(this);
    }
  }

  public final KeyUsesContext keyUses() throws RecognitionException {
    KeyUsesContext _localctx = new KeyUsesContext(_ctx, getState());
    enterRule(_localctx, 4, RULE_keyUses);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(293);
        match(USES);
        setState(294);
        structureDefinition();
        setState(295);
        match(AS);
        setState(296);
        keyUsesName();
        setState(298);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la == T__1) {
          {
            setState(297);
            match(T__1);
          }
        }

      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class KeyUsesNameContext extends ParserRuleContext {
    public KeyUsesNameSourceContext keyUsesNameSource() {
      return getRuleContext(KeyUsesNameSourceContext.class, 0);
    }

    public KeyUsesNameTargetContext keyUsesNameTarget() {
      return getRuleContext(KeyUsesNameTargetContext.class, 0);
    }

    public KeyUsesNameQueriedContext keyUsesNameQueried() {
      return getRuleContext(KeyUsesNameQueriedContext.class, 0);
    }

    public KeyUsesNameProducedContext keyUsesNameProduced() {
      return getRuleContext(KeyUsesNameProducedContext.class, 0);
    }

    public KeyUsesNameContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_keyUsesName;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor)
        return ((FhirMapJavaVisitor<? extends T>) visitor).visitKeyUsesName(this);
      else return visitor.visitChildren(this);
    }
  }

  public final KeyUsesNameContext keyUsesName() throws RecognitionException {
    KeyUsesNameContext _localctx = new KeyUsesNameContext(_ctx, getState());
    enterRule(_localctx, 6, RULE_keyUsesName);
    try {
      setState(304);
      _errHandler.sync(this);
      switch (_input.LA(1)) {
        case SOURCE:
          enterOuterAlt(_localctx, 1);
        {
          setState(300);
          keyUsesNameSource();
        }
        break;
        case TARGET:
          enterOuterAlt(_localctx, 2);
        {
          setState(301);
          keyUsesNameTarget();
        }
        break;
        case QUERIED:
          enterOuterAlt(_localctx, 3);
        {
          setState(302);
          keyUsesNameQueried();
        }
        break;
        case PRODUCED:
          enterOuterAlt(_localctx, 4);
        {
          setState(303);
          keyUsesNameProduced();
        }
        break;
        default:
          throw new NoViableAltException(this);
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class KeyUsesNameSourceContext extends ParserRuleContext {
    public TerminalNode SOURCE() {
      return getToken(FhirMapJavaParser.SOURCE, 0);
    }

    public KeyUsesNameSourceContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_keyUsesNameSource;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor)
        return ((FhirMapJavaVisitor<? extends T>) visitor).visitKeyUsesNameSource(this);
      else return visitor.visitChildren(this);
    }
  }

  public final KeyUsesNameSourceContext keyUsesNameSource() throws RecognitionException {
    KeyUsesNameSourceContext _localctx = new KeyUsesNameSourceContext(_ctx, getState());
    enterRule(_localctx, 8, RULE_keyUsesNameSource);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(306);
        match(SOURCE);
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class KeyUsesNameTargetContext extends ParserRuleContext {
    public TerminalNode TARGET() {
      return getToken(FhirMapJavaParser.TARGET, 0);
    }

    public KeyUsesNameTargetContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_keyUsesNameTarget;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor)
        return ((FhirMapJavaVisitor<? extends T>) visitor).visitKeyUsesNameTarget(this);
      else return visitor.visitChildren(this);
    }
  }

  public final KeyUsesNameTargetContext keyUsesNameTarget() throws RecognitionException {
    KeyUsesNameTargetContext _localctx = new KeyUsesNameTargetContext(_ctx, getState());
    enterRule(_localctx, 10, RULE_keyUsesNameTarget);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(308);
        match(TARGET);
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class KeyUsesNameQueriedContext extends ParserRuleContext {
    public TerminalNode QUERIED() {
      return getToken(FhirMapJavaParser.QUERIED, 0);
    }

    public KeyUsesNameQueriedContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_keyUsesNameQueried;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor)
        return ((FhirMapJavaVisitor<? extends T>) visitor).visitKeyUsesNameQueried(this);
      else return visitor.visitChildren(this);
    }
  }

  public final KeyUsesNameQueriedContext keyUsesNameQueried() throws RecognitionException {
    KeyUsesNameQueriedContext _localctx = new KeyUsesNameQueriedContext(_ctx, getState());
    enterRule(_localctx, 12, RULE_keyUsesNameQueried);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(310);
        match(QUERIED);
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class KeyUsesNameProducedContext extends ParserRuleContext {
    public TerminalNode PRODUCED() {
      return getToken(FhirMapJavaParser.PRODUCED, 0);
    }

    public KeyUsesNameProducedContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_keyUsesNameProduced;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor)
        return ((FhirMapJavaVisitor<? extends T>) visitor).visitKeyUsesNameProduced(this);
      else return visitor.visitChildren(this);
    }
  }

  public final KeyUsesNameProducedContext keyUsesNameProduced() throws RecognitionException {
    KeyUsesNameProducedContext _localctx = new KeyUsesNameProducedContext(_ctx, getState());
    enterRule(_localctx, 14, RULE_keyUsesNameProduced);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(312);
        match(PRODUCED);
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class KeyImportsContext extends ParserRuleContext {
    public TerminalNode IMPORTS() {
      return getToken(FhirMapJavaParser.IMPORTS, 0);
    }

    public StructureMapContext structureMap() {
      return getRuleContext(StructureMapContext.class, 0);
    }

    public KeyImportsContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_keyImports;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor)
        return ((FhirMapJavaVisitor<? extends T>) visitor).visitKeyImports(this);
      else return visitor.visitChildren(this);
    }
  }

  public final KeyImportsContext keyImports() throws RecognitionException {
    KeyImportsContext _localctx = new KeyImportsContext(_ctx, getState());
    enterRule(_localctx, 16, RULE_keyImports);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(314);
        match(IMPORTS);
        setState(315);
        structureMap();
        setState(317);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la == T__1) {
          {
            setState(316);
            match(T__1);
          }
        }

      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class GroupContext extends ParserRuleContext {
    public GroupStartContext groupStart() {
      return getRuleContext(GroupStartContext.class, 0);
    }

    public GroupEndContext groupEnd() {
      return getRuleContext(GroupEndContext.class, 0);
    }

    public List<GroupInputContext> groupInput() {
      return getRuleContexts(GroupInputContext.class);
    }

    public GroupInputContext groupInput(int i) {
      return getRuleContext(GroupInputContext.class, i);
    }

    public List<GroupItemContext> groupItem() {
      return getRuleContexts(GroupItemContext.class);
    }

    public GroupItemContext groupItem(int i) {
      return getRuleContext(GroupItemContext.class, i);
    }

    public GroupContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_group;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor) return ((FhirMapJavaVisitor<? extends T>) visitor).visitGroup(this);
      else return visitor.visitChildren(this);
    }
  }

  public final GroupContext group() throws RecognitionException {
    GroupContext _localctx = new GroupContext(_ctx, getState());
    enterRule(_localctx, 18, RULE_group);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(319);
        groupStart();
        setState(323);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la == INPUT) {
          {
            {
              setState(320);
              groupInput();
            }
          }
          setState(325);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
        setState(329);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << SOURCE) | (1L << TARGET) | (1L << QUERIED) | (1L << PRODUCED) | (1L << APPEND) | (1L << CAST) | (1L << C) | (1L << CC) | (1L << CODE) | (1L << CP) | (1L << CODEABLECONCEPT) | (1L << CODING) | (1L << COPY) | (1L << CREATE) | (1L << DATEOP) | (1L << DEFAULT) | (1L << DISPLAY) | (1L << ESCAPE) | (1L << EXTENSION) | (1L << EVALUATE) | (1L << ID) | (1L << MAP) | (1L << POINTER) | (1L << QTY) | (1L << REFERENCE) | (1L << SYSTEM) | (1L << TRANSLATE) | (1L << TRUNCATE) | (1L << TYPES) | (1L << UUID) | (1L << IDENTIFIER))) != 0)) {
          {
            {
              setState(326);
              groupItem();
            }
          }
          setState(331);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
        setState(332);
        groupEnd();
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class GroupStartContext extends ParserRuleContext {
    public TerminalNode GROUP() {
      return getToken(FhirMapJavaParser.GROUP, 0);
    }

    public IdentifierContext identifier() {
      return getRuleContext(IdentifierContext.class, 0);
    }

    public GroupTypeContext groupType() {
      return getRuleContext(GroupTypeContext.class, 0);
    }

    public GroupExtendsContext groupExtends() {
      return getRuleContext(GroupExtendsContext.class, 0);
    }

    public GroupStartContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_groupStart;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor)
        return ((FhirMapJavaVisitor<? extends T>) visitor).visitGroupStart(this);
      else return visitor.visitChildren(this);
    }
  }

  public final GroupStartContext groupStart() throws RecognitionException {
    GroupStartContext _localctx = new GroupStartContext(_ctx, getState());
    enterRule(_localctx, 20, RULE_groupStart);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(334);
        match(GROUP);
        setState(336);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la == FOR) {
          {
            setState(335);
            groupType();
          }
        }

        setState(338);
        identifier();
        setState(340);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la == EXTENDS) {
          {
            setState(339);
            groupExtends();
          }
        }

      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class GroupExtendsContext extends ParserRuleContext {
    public TerminalNode EXTENDS() {
      return getToken(FhirMapJavaParser.EXTENDS, 0);
    }

    public IdentifierContext identifier() {
      return getRuleContext(IdentifierContext.class, 0);
    }

    public GroupExtendsContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_groupExtends;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor)
        return ((FhirMapJavaVisitor<? extends T>) visitor).visitGroupExtends(this);
      else return visitor.visitChildren(this);
    }
  }

  public final GroupExtendsContext groupExtends() throws RecognitionException {
    GroupExtendsContext _localctx = new GroupExtendsContext(_ctx, getState());
    enterRule(_localctx, 22, RULE_groupExtends);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(342);
        match(EXTENDS);
        setState(343);
        identifier();
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class GroupEndContext extends ParserRuleContext {
    public TerminalNode ENDGROUP() {
      return getToken(FhirMapJavaParser.ENDGROUP, 0);
    }

    public GroupEndContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_groupEnd;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor) return ((FhirMapJavaVisitor<? extends T>) visitor).visitGroupEnd(this);
      else return visitor.visitChildren(this);
    }
  }

  public final GroupEndContext groupEnd() throws RecognitionException {
    GroupEndContext _localctx = new GroupEndContext(_ctx, getState());
    enterRule(_localctx, 24, RULE_groupEnd);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(345);
        match(ENDGROUP);
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class GroupTypeContext extends ParserRuleContext {
    public TerminalNode FOR() {
      return getToken(FhirMapJavaParser.FOR, 0);
    }

    public GroupTypeValueContext groupTypeValue() {
      return getRuleContext(GroupTypeValueContext.class, 0);
    }

    public GroupTypeContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_groupType;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor)
        return ((FhirMapJavaVisitor<? extends T>) visitor).visitGroupType(this);
      else return visitor.visitChildren(this);
    }
  }

  public final GroupTypeContext groupType() throws RecognitionException {
    GroupTypeContext _localctx = new GroupTypeContext(_ctx, getState());
    enterRule(_localctx, 26, RULE_groupType);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(347);
        match(FOR);
        setState(348);
        groupTypeValue();
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class GroupTypeValueContext extends ParserRuleContext {
    public GroupTypeTypeContext groupTypeType() {
      return getRuleContext(GroupTypeTypeContext.class, 0);
    }

    public GroupTypeTypeTypesContext groupTypeTypeTypes() {
      return getRuleContext(GroupTypeTypeTypesContext.class, 0);
    }

    public GroupTypeValueContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_groupTypeValue;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor)
        return ((FhirMapJavaVisitor<? extends T>) visitor).visitGroupTypeValue(this);
      else return visitor.visitChildren(this);
    }
  }

  public final GroupTypeValueContext groupTypeValue() throws RecognitionException {
    GroupTypeValueContext _localctx = new GroupTypeValueContext(_ctx, getState());
    enterRule(_localctx, 28, RULE_groupTypeValue);
    try {
      setState(352);
      _errHandler.sync(this);
      switch (_input.LA(1)) {
        case TYPES:
          enterOuterAlt(_localctx, 1);
        {
          setState(350);
          groupTypeType();
        }
        break;
        case TYPE_TYPES:
          enterOuterAlt(_localctx, 2);
        {
          setState(351);
          groupTypeTypeTypes();
        }
        break;
        default:
          throw new NoViableAltException(this);
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class GroupTypeTypeContext extends ParserRuleContext {
    public TerminalNode TYPES() {
      return getToken(FhirMapJavaParser.TYPES, 0);
    }

    public GroupTypeTypeContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_groupTypeType;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor)
        return ((FhirMapJavaVisitor<? extends T>) visitor).visitGroupTypeType(this);
      else return visitor.visitChildren(this);
    }
  }

  public final GroupTypeTypeContext groupTypeType() throws RecognitionException {
    GroupTypeTypeContext _localctx = new GroupTypeTypeContext(_ctx, getState());
    enterRule(_localctx, 30, RULE_groupTypeType);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(354);
        match(TYPES);
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class GroupTypeTypeTypesContext extends ParserRuleContext {
    public TerminalNode TYPE_TYPES() {
      return getToken(FhirMapJavaParser.TYPE_TYPES, 0);
    }

    public GroupTypeTypeTypesContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_groupTypeTypeTypes;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor)
        return ((FhirMapJavaVisitor<? extends T>) visitor).visitGroupTypeTypeTypes(this);
      else return visitor.visitChildren(this);
    }
  }

  public final GroupTypeTypeTypesContext groupTypeTypeTypes() throws RecognitionException {
    GroupTypeTypeTypesContext _localctx = new GroupTypeTypeTypesContext(_ctx, getState());
    enterRule(_localctx, 32, RULE_groupTypeTypeTypes);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(356);
        match(TYPE_TYPES);
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class GroupInputContext extends ParserRuleContext {
    public TerminalNode INPUT() {
      return getToken(FhirMapJavaParser.INPUT, 0);
    }

    public GroupInputNameContext groupInputName() {
      return getRuleContext(GroupInputNameContext.class, 0);
    }

    public GroupInputTypeContext groupInputType() {
      return getRuleContext(GroupInputTypeContext.class, 0);
    }

    public GroupInputModeContext groupInputMode() {
      return getRuleContext(GroupInputModeContext.class, 0);
    }

    public GroupInputContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_groupInput;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor)
        return ((FhirMapJavaVisitor<? extends T>) visitor).visitGroupInput(this);
      else return visitor.visitChildren(this);
    }
  }

  public final GroupInputContext groupInput() throws RecognitionException {
    GroupInputContext _localctx = new GroupInputContext(_ctx, getState());
    enterRule(_localctx, 34, RULE_groupInput);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(358);
        match(INPUT);
        setState(359);
        groupInputName();
        setState(360);
        match(T__2);
        setState(361);
        groupInputType();
        setState(363);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la == AS) {
          {
            setState(362);
            groupInputMode();
          }
        }

        setState(366);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la == T__1) {
          {
            setState(365);
            match(T__1);
          }
        }

      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class GroupInputNameContext extends ParserRuleContext {
    public IdentifierContext identifier() {
      return getRuleContext(IdentifierContext.class, 0);
    }

    public GroupInputNameContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_groupInputName;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor)
        return ((FhirMapJavaVisitor<? extends T>) visitor).visitGroupInputName(this);
      else return visitor.visitChildren(this);
    }
  }

  public final GroupInputNameContext groupInputName() throws RecognitionException {
    GroupInputNameContext _localctx = new GroupInputNameContext(_ctx, getState());
    enterRule(_localctx, 36, RULE_groupInputName);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(368);
        identifier();
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class GroupInputTypeContext extends ParserRuleContext {
    public IdentifierContext identifier() {
      return getRuleContext(IdentifierContext.class, 0);
    }

    public GroupInputTypeContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_groupInputType;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor)
        return ((FhirMapJavaVisitor<? extends T>) visitor).visitGroupInputType(this);
      else return visitor.visitChildren(this);
    }
  }

  public final GroupInputTypeContext groupInputType() throws RecognitionException {
    GroupInputTypeContext _localctx = new GroupInputTypeContext(_ctx, getState());
    enterRule(_localctx, 38, RULE_groupInputType);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(370);
        identifier();
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class GroupInputModeContext extends ParserRuleContext {
    public TerminalNode AS() {
      return getToken(FhirMapJavaParser.AS, 0);
    }

    public GroupInputModesContext groupInputModes() {
      return getRuleContext(GroupInputModesContext.class, 0);
    }

    public GroupInputModeContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_groupInputMode;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor)
        return ((FhirMapJavaVisitor<? extends T>) visitor).visitGroupInputMode(this);
      else return visitor.visitChildren(this);
    }
  }

  public final GroupInputModeContext groupInputMode() throws RecognitionException {
    GroupInputModeContext _localctx = new GroupInputModeContext(_ctx, getState());
    enterRule(_localctx, 40, RULE_groupInputMode);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(372);
        match(AS);
        setState(373);
        groupInputModes();
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class GroupInputModesContext extends ParserRuleContext {
    public GroupInputModesSourceContext groupInputModesSource() {
      return getRuleContext(GroupInputModesSourceContext.class, 0);
    }

    public GroupInputModesTargetContext groupInputModesTarget() {
      return getRuleContext(GroupInputModesTargetContext.class, 0);
    }

    public GroupInputModesContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_groupInputModes;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor)
        return ((FhirMapJavaVisitor<? extends T>) visitor).visitGroupInputModes(this);
      else return visitor.visitChildren(this);
    }
  }

  public final GroupInputModesContext groupInputModes() throws RecognitionException {
    GroupInputModesContext _localctx = new GroupInputModesContext(_ctx, getState());
    enterRule(_localctx, 42, RULE_groupInputModes);
    try {
      setState(377);
      _errHandler.sync(this);
      switch (_input.LA(1)) {
        case SOURCE:
          enterOuterAlt(_localctx, 1);
        {
          setState(375);
          groupInputModesSource();
        }
        break;
        case TARGET:
          enterOuterAlt(_localctx, 2);
        {
          setState(376);
          groupInputModesTarget();
        }
        break;
        default:
          throw new NoViableAltException(this);
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class GroupInputModesSourceContext extends ParserRuleContext {
    public TerminalNode SOURCE() {
      return getToken(FhirMapJavaParser.SOURCE, 0);
    }

    public GroupInputModesSourceContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_groupInputModesSource;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor)
        return ((FhirMapJavaVisitor<? extends T>) visitor).visitGroupInputModesSource(this);
      else return visitor.visitChildren(this);
    }
  }

  public final GroupInputModesSourceContext groupInputModesSource() throws RecognitionException {
    GroupInputModesSourceContext _localctx = new GroupInputModesSourceContext(_ctx, getState());
    enterRule(_localctx, 44, RULE_groupInputModesSource);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(379);
        match(SOURCE);
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class GroupInputModesTargetContext extends ParserRuleContext {
    public TerminalNode TARGET() {
      return getToken(FhirMapJavaParser.TARGET, 0);
    }

    public GroupInputModesTargetContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_groupInputModesTarget;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor)
        return ((FhirMapJavaVisitor<? extends T>) visitor).visitGroupInputModesTarget(this);
      else return visitor.visitChildren(this);
    }
  }

  public final GroupInputModesTargetContext groupInputModesTarget() throws RecognitionException {
    GroupInputModesTargetContext _localctx = new GroupInputModesTargetContext(_ctx, getState());
    enterRule(_localctx, 46, RULE_groupInputModesTarget);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(381);
        match(TARGET);
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class GroupItemContext extends ParserRuleContext {
    public GroupCallContext groupCall() {
      return getRuleContext(GroupCallContext.class, 0);
    }

    public RuleInstanceContext ruleInstance() {
      return getRuleContext(RuleInstanceContext.class, 0);
    }

    public GroupItemContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_groupItem;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor)
        return ((FhirMapJavaVisitor<? extends T>) visitor).visitGroupItem(this);
      else return visitor.visitChildren(this);
    }
  }

  public final GroupItemContext groupItem() throws RecognitionException {
    GroupItemContext _localctx = new GroupItemContext(_ctx, getState());
    enterRule(_localctx, 48, RULE_groupItem);
    try {
      setState(385);
      _errHandler.sync(this);
      switch (getInterpreter().adaptivePredict(_input, 13, _ctx)) {
        case 1:
          enterOuterAlt(_localctx, 1);
        {
          setState(383);
          groupCall();
        }
        break;
        case 2:
          enterOuterAlt(_localctx, 2);
        {
          setState(384);
          ruleInstance();
        }
        break;
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class GroupCallContext extends ParserRuleContext {
    public IdentifierContext identifier() {
      return getRuleContext(IdentifierContext.class, 0);
    }

    public GroupCallParametersContext groupCallParameters() {
      return getRuleContext(GroupCallParametersContext.class, 0);
    }

    public GroupCallContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_groupCall;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor)
        return ((FhirMapJavaVisitor<? extends T>) visitor).visitGroupCall(this);
      else return visitor.visitChildren(this);
    }
  }

  public final GroupCallContext groupCall() throws RecognitionException {
    GroupCallContext _localctx = new GroupCallContext(_ctx, getState());
    enterRule(_localctx, 50, RULE_groupCall);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(387);
        identifier();
        setState(388);
        match(T__3);
        setState(390);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << SOURCE) | (1L << TARGET) | (1L << QUERIED) | (1L << PRODUCED) | (1L << APPEND) | (1L << CAST) | (1L << C) | (1L << CC) | (1L << CODE) | (1L << CP) | (1L << CODEABLECONCEPT) | (1L << CODING) | (1L << COPY) | (1L << CREATE) | (1L << DATEOP) | (1L << DEFAULT) | (1L << DISPLAY) | (1L << ESCAPE) | (1L << EXTENSION) | (1L << EVALUATE) | (1L << ID) | (1L << MAP) | (1L << POINTER) | (1L << QTY) | (1L << REFERENCE) | (1L << SYSTEM) | (1L << TRANSLATE) | (1L << TRUNCATE) | (1L << TYPES) | (1L << UUID) | (1L << IDENTIFIER))) != 0)) {
          {
            setState(389);
            groupCallParameters();
          }
        }

        setState(392);
        match(T__4);
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class GroupCallParametersContext extends ParserRuleContext {
    public List<IdentifierContext> identifier() {
      return getRuleContexts(IdentifierContext.class);
    }

    public IdentifierContext identifier(int i) {
      return getRuleContext(IdentifierContext.class, i);
    }

    public GroupCallParametersContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_groupCallParameters;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor)
        return ((FhirMapJavaVisitor<? extends T>) visitor).visitGroupCallParameters(this);
      else return visitor.visitChildren(this);
    }
  }

  public final GroupCallParametersContext groupCallParameters() throws RecognitionException {
    GroupCallParametersContext _localctx = new GroupCallParametersContext(_ctx, getState());
    enterRule(_localctx, 52, RULE_groupCallParameters);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(394);
        identifier();
        setState(399);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la == T__5) {
          {
            {
              setState(395);
              match(T__5);
              setState(396);
              identifier();
            }
          }
          setState(401);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleInstanceContext extends ParserRuleContext {
    public RuleNameContext ruleName() {
      return getRuleContext(RuleNameContext.class, 0);
    }

    public TerminalNode FOR() {
      return getToken(FhirMapJavaParser.FOR, 0);
    }

    public RuleSourcesContext ruleSources() {
      return getRuleContext(RuleSourcesContext.class, 0);
    }

    public RuleMakeContext ruleMake() {
      return getRuleContext(RuleMakeContext.class, 0);
    }

    public RuleInstanceContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_ruleInstance;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor)
        return ((FhirMapJavaVisitor<? extends T>) visitor).visitRuleInstance(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleInstanceContext ruleInstance() throws RecognitionException {
    RuleInstanceContext _localctx = new RuleInstanceContext(_ctx, getState());
    enterRule(_localctx, 54, RULE_ruleInstance);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(402);
        ruleName();
        setState(403);
        match(T__2);
        setState(404);
        match(FOR);
        setState(405);
        ruleSources();
        setState(407);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la == MAKE) {
          {
            setState(406);
            ruleMake();
          }
        }

      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleNameContext extends ParserRuleContext {
    public List<IdentifierContext> identifier() {
      return getRuleContexts(IdentifierContext.class);
    }

    public IdentifierContext identifier(int i) {
      return getRuleContext(IdentifierContext.class, i);
    }

    public RuleNameContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_ruleName;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor) return ((FhirMapJavaVisitor<? extends T>) visitor).visitRuleName(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleNameContext ruleName() throws RecognitionException {
    RuleNameContext _localctx = new RuleNameContext(_ctx, getState());
    enterRule(_localctx, 56, RULE_ruleName);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(409);
        identifier();
        setState(414);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la == T__6) {
          {
            {
              setState(410);
              match(T__6);
              setState(411);
              identifier();
            }
          }
          setState(416);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleSourcesContext extends ParserRuleContext {
    public List<RuleSourceContext> ruleSource() {
      return getRuleContexts(RuleSourceContext.class);
    }

    public RuleSourceContext ruleSource(int i) {
      return getRuleContext(RuleSourceContext.class, i);
    }

    public RuleSourcesContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_ruleSources;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor)
        return ((FhirMapJavaVisitor<? extends T>) visitor).visitRuleSources(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleSourcesContext ruleSources() throws RecognitionException {
    RuleSourcesContext _localctx = new RuleSourcesContext(_ctx, getState());
    enterRule(_localctx, 58, RULE_ruleSources);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(417);
        ruleSource();
        setState(422);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la == T__5) {
          {
            {
              setState(418);
              match(T__5);
              setState(419);
              ruleSource();
            }
          }
          setState(424);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleSourceContext extends ParserRuleContext {
    public RuleContextContext ruleContext() {
      return getRuleContext(RuleContextContext.class, 0);
    }

    public RuleTypeContext ruleType() {
      return getRuleContext(RuleTypeContext.class, 0);
    }

    public RuleDefaultContext ruleDefault() {
      return getRuleContext(RuleDefaultContext.class, 0);
    }

    public RuleListOptionContext ruleListOption() {
      return getRuleContext(RuleListOptionContext.class, 0);
    }

    public RuleVariableContext ruleVariable() {
      return getRuleContext(RuleVariableContext.class, 0);
    }

    public RuleWherePathContext ruleWherePath() {
      return getRuleContext(RuleWherePathContext.class, 0);
    }

    public RuleCheckPathContext ruleCheckPath() {
      return getRuleContext(RuleCheckPathContext.class, 0);
    }

    public RuleSourceContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_ruleSource;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor)
        return ((FhirMapJavaVisitor<? extends T>) visitor).visitRuleSource(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleSourceContext ruleSource() throws RecognitionException {
    RuleSourceContext _localctx = new RuleSourceContext(_ctx, getState());
    enterRule(_localctx, 60, RULE_ruleSource);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(425);
        ruleContext();
        setState(427);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la == T__2) {
          {
            setState(426);
            ruleType();
          }
        }

        setState(430);
        _errHandler.sync(this);
        switch (getInterpreter().adaptivePredict(_input, 20, _ctx)) {
          case 1: {
            setState(429);
            ruleDefault();
          }
          break;
        }
        setState(433);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la == T__8) {
          {
            setState(432);
            ruleListOption();
          }
        }

        setState(436);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la == AS) {
          {
            setState(435);
            ruleVariable();
          }
        }

        setState(439);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la == WHERE) {
          {
            setState(438);
            ruleWherePath();
          }
        }

        setState(442);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la == CHECK) {
          {
            setState(441);
            ruleCheckPath();
          }
        }

      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleContextContext extends ParserRuleContext {
    public List<RuleContextElementContext> ruleContextElement() {
      return getRuleContexts(RuleContextElementContext.class);
    }

    public RuleContextElementContext ruleContextElement(int i) {
      return getRuleContext(RuleContextElementContext.class, i);
    }

    public RuleContextContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_ruleContext;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor)
        return ((FhirMapJavaVisitor<? extends T>) visitor).visitRuleContext(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleContextContext ruleContext() throws RecognitionException {
    RuleContextContext _localctx = new RuleContextContext(_ctx, getState());
    enterRule(_localctx, 62, RULE_ruleContext);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(444);
        ruleContextElement();
        setState(449);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la == T__6) {
          {
            {
              setState(445);
              match(T__6);
              setState(446);
              ruleContextElement();
            }
          }
          setState(451);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleContextElementContext extends ParserRuleContext {
    public IdentifierContext identifier() {
      return getRuleContext(IdentifierContext.class, 0);
    }

    public QuotedStringWQuotesContext quotedStringWQuotes() {
      return getRuleContext(QuotedStringWQuotesContext.class, 0);
    }

    public RuleContextElementContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_ruleContextElement;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor)
        return ((FhirMapJavaVisitor<? extends T>) visitor).visitRuleContextElement(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleContextElementContext ruleContextElement() throws RecognitionException {
    RuleContextElementContext _localctx = new RuleContextElementContext(_ctx, getState());
    enterRule(_localctx, 64, RULE_ruleContextElement);
    try {
      setState(454);
      _errHandler.sync(this);
      switch (_input.LA(1)) {
        case SOURCE:
        case TARGET:
        case QUERIED:
        case PRODUCED:
        case APPEND:
        case CAST:
        case C:
        case CC:
        case CODE:
        case CP:
        case CODEABLECONCEPT:
        case CODING:
        case COPY:
        case CREATE:
        case DATEOP:
        case DEFAULT:
        case DISPLAY:
        case ESCAPE:
        case EXTENSION:
        case EVALUATE:
        case ID:
        case MAP:
        case POINTER:
        case QTY:
        case REFERENCE:
        case SYSTEM:
        case TRANSLATE:
        case TRUNCATE:
        case TYPES:
        case UUID:
        case IDENTIFIER:
          enterOuterAlt(_localctx, 1);
        {
          setState(452);
          identifier();
        }
        break;
        case QIDENTIFIER:
        case QSTRING:
          enterOuterAlt(_localctx, 2);
        {
          setState(453);
          quotedStringWQuotes();
        }
        break;
        default:
          throw new NoViableAltException(this);
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTypeContext extends ParserRuleContext {
    public IdentifierContext identifier() {
      return getRuleContext(IdentifierContext.class, 0);
    }

    public List<IntegerContext> integer() {
      return getRuleContexts(IntegerContext.class);
    }

    public IntegerContext integer(int i) {
      return getRuleContext(IntegerContext.class, i);
    }

    public RuleTypeContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_ruleType;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor) return ((FhirMapJavaVisitor<? extends T>) visitor).visitRuleType(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTypeContext ruleType() throws RecognitionException {
    RuleTypeContext _localctx = new RuleTypeContext(_ctx, getState());
    enterRule(_localctx, 66, RULE_ruleType);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(456);
        match(T__2);
        setState(457);
        identifier();
        setState(462);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la == DIGITS) {
          {
            setState(458);
            integer();
            setState(459);
            match(T__7);
            setState(460);
            integer();
          }
        }

      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleDefaultContext extends ParserRuleContext {
    public TerminalNode DEFAULT() {
      return getToken(FhirMapJavaParser.DEFAULT, 0);
    }

    public IdentifierContext identifier() {
      return getRuleContext(IdentifierContext.class, 0);
    }

    public RuleDefaultContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_ruleDefault;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor)
        return ((FhirMapJavaVisitor<? extends T>) visitor).visitRuleDefault(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleDefaultContext ruleDefault() throws RecognitionException {
    RuleDefaultContext _localctx = new RuleDefaultContext(_ctx, getState());
    enterRule(_localctx, 68, RULE_ruleDefault);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(464);
        match(DEFAULT);
        setState(465);
        identifier();
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleListOptionContext extends ParserRuleContext {
    public RuleListOptionContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_ruleListOption;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor)
        return ((FhirMapJavaVisitor<? extends T>) visitor).visitRuleListOption(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleListOptionContext ruleListOption() throws RecognitionException {
    RuleListOptionContext _localctx = new RuleListOptionContext(_ctx, getState());
    enterRule(_localctx, 70, RULE_ruleListOption);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(467);
        match(T__8);
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleVariableContext extends ParserRuleContext {
    public TerminalNode AS() {
      return getToken(FhirMapJavaParser.AS, 0);
    }

    public IdentifierContext identifier() {
      return getRuleContext(IdentifierContext.class, 0);
    }

    public RuleVariableContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_ruleVariable;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor)
        return ((FhirMapJavaVisitor<? extends T>) visitor).visitRuleVariable(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleVariableContext ruleVariable() throws RecognitionException {
    RuleVariableContext _localctx = new RuleVariableContext(_ctx, getState());
    enterRule(_localctx, 72, RULE_ruleVariable);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(469);
        match(AS);
        setState(470);
        identifier();
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleWherePathContext extends ParserRuleContext {
    public TerminalNode WHERE() {
      return getToken(FhirMapJavaParser.WHERE, 0);
    }

    public FhirPathContext fhirPath() {
      return getRuleContext(FhirPathContext.class, 0);
    }

    public RuleWherePathContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_ruleWherePath;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor)
        return ((FhirMapJavaVisitor<? extends T>) visitor).visitRuleWherePath(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleWherePathContext ruleWherePath() throws RecognitionException {
    RuleWherePathContext _localctx = new RuleWherePathContext(_ctx, getState());
    enterRule(_localctx, 74, RULE_ruleWherePath);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(472);
        match(WHERE);
        setState(473);
        fhirPath();
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleCheckPathContext extends ParserRuleContext {
    public TerminalNode CHECK() {
      return getToken(FhirMapJavaParser.CHECK, 0);
    }

    public FhirPathContext fhirPath() {
      return getRuleContext(FhirPathContext.class, 0);
    }

    public RuleCheckPathContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_ruleCheckPath;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor)
        return ((FhirMapJavaVisitor<? extends T>) visitor).visitRuleCheckPath(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleCheckPathContext ruleCheckPath() throws RecognitionException {
    RuleCheckPathContext _localctx = new RuleCheckPathContext(_ctx, getState());
    enterRule(_localctx, 76, RULE_ruleCheckPath);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(475);
        match(CHECK);
        setState(476);
        fhirPath();
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleMakeContext extends ParserRuleContext {
    public TerminalNode MAKE() {
      return getToken(FhirMapJavaParser.MAKE, 0);
    }

    public RuleMakeTargetsContext ruleMakeTargets() {
      return getRuleContext(RuleMakeTargetsContext.class, 0);
    }

    public RuleMakeDependentsContext ruleMakeDependents() {
      return getRuleContext(RuleMakeDependentsContext.class, 0);
    }

    public RuleMakeContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_ruleMake;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor) return ((FhirMapJavaVisitor<? extends T>) visitor).visitRuleMake(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleMakeContext ruleMake() throws RecognitionException {
    RuleMakeContext _localctx = new RuleMakeContext(_ctx, getState());
    enterRule(_localctx, 78, RULE_ruleMake);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(478);
        match(MAKE);
        setState(479);
        ruleMakeTargets();
        setState(481);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la == THEN) {
          {
            setState(480);
            ruleMakeDependents();
          }
        }

      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleMakeTargetsContext extends ParserRuleContext {
    public List<RuleTargetContext> ruleTarget() {
      return getRuleContexts(RuleTargetContext.class);
    }

    public RuleTargetContext ruleTarget(int i) {
      return getRuleContext(RuleTargetContext.class, i);
    }

    public RuleMakeTargetsContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_ruleMakeTargets;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor)
        return ((FhirMapJavaVisitor<? extends T>) visitor).visitRuleMakeTargets(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleMakeTargetsContext ruleMakeTargets() throws RecognitionException {
    RuleMakeTargetsContext _localctx = new RuleMakeTargetsContext(_ctx, getState());
    enterRule(_localctx, 80, RULE_ruleMakeTargets);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(483);
        ruleTarget();
        setState(488);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la == T__5) {
          {
            {
              setState(484);
              match(T__5);
              setState(485);
              ruleTarget();
            }
          }
          setState(490);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleMakeDependentsContext extends ParserRuleContext {
    public TerminalNode THEN() {
      return getToken(FhirMapJavaParser.THEN, 0);
    }

    public RuleMakeDependentsGroupItemsContext ruleMakeDependentsGroupItems() {
      return getRuleContext(RuleMakeDependentsGroupItemsContext.class, 0);
    }

    public RuleMakeDependentsContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_ruleMakeDependents;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor)
        return ((FhirMapJavaVisitor<? extends T>) visitor).visitRuleMakeDependents(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleMakeDependentsContext ruleMakeDependents() throws RecognitionException {
    RuleMakeDependentsContext _localctx = new RuleMakeDependentsContext(_ctx, getState());
    enterRule(_localctx, 82, RULE_ruleMakeDependents);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(491);
        match(THEN);
        setState(492);
        match(T__9);
        setState(493);
        ruleMakeDependentsGroupItems();
        setState(494);
        match(T__10);
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleMakeDependentsGroupItemsContext extends ParserRuleContext {
    public List<GroupItemContext> groupItem() {
      return getRuleContexts(GroupItemContext.class);
    }

    public GroupItemContext groupItem(int i) {
      return getRuleContext(GroupItemContext.class, i);
    }

    public RuleMakeDependentsGroupItemsContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_ruleMakeDependentsGroupItems;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor)
        return ((FhirMapJavaVisitor<? extends T>) visitor).visitRuleMakeDependentsGroupItems(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleMakeDependentsGroupItemsContext ruleMakeDependentsGroupItems() throws RecognitionException {
    RuleMakeDependentsGroupItemsContext _localctx = new RuleMakeDependentsGroupItemsContext(_ctx, getState());
    enterRule(_localctx, 84, RULE_ruleMakeDependentsGroupItems);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(499);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << SOURCE) | (1L << TARGET) | (1L << QUERIED) | (1L << PRODUCED) | (1L << APPEND) | (1L << CAST) | (1L << C) | (1L << CC) | (1L << CODE) | (1L << CP) | (1L << CODEABLECONCEPT) | (1L << CODING) | (1L << COPY) | (1L << CREATE) | (1L << DATEOP) | (1L << DEFAULT) | (1L << DISPLAY) | (1L << ESCAPE) | (1L << EXTENSION) | (1L << EVALUATE) | (1L << ID) | (1L << MAP) | (1L << POINTER) | (1L << QTY) | (1L << REFERENCE) | (1L << SYSTEM) | (1L << TRANSLATE) | (1L << TRUNCATE) | (1L << TYPES) | (1L << UUID) | (1L << IDENTIFIER))) != 0)) {
          {
            {
              setState(496);
              groupItem();
            }
          }
          setState(501);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetContext extends ParserRuleContext {
    public RuleTargetAppendContext ruleTargetAppend() {
      return getRuleContext(RuleTargetAppendContext.class, 0);
    }

    public RuleTargetAsContext ruleTargetAs() {
      return getRuleContext(RuleTargetAsContext.class, 0);
    }

    public RuleTargetAssignContext ruleTargetAssign() {
      return getRuleContext(RuleTargetAssignContext.class, 0);
    }

    public RuleTargetCContext ruleTargetC() {
      return getRuleContext(RuleTargetCContext.class, 0);
    }

    public RuleTargetCastContext ruleTargetCast() {
      return getRuleContext(RuleTargetCastContext.class, 0);
    }

    public RuleTargetCCContext ruleTargetCC() {
      return getRuleContext(RuleTargetCCContext.class, 0);
    }

    public RuleTargetCpContext ruleTargetCp() {
      return getRuleContext(RuleTargetCpContext.class, 0);
    }

    public RuleTargetCopyContext ruleTargetCopy() {
      return getRuleContext(RuleTargetCopyContext.class, 0);
    }

    public RuleTargetCreateContext ruleTargetCreate() {
      return getRuleContext(RuleTargetCreateContext.class, 0);
    }

    public RuleTargetDateOpContext ruleTargetDateOp() {
      return getRuleContext(RuleTargetDateOpContext.class, 0);
    }

    public RuleTargetExtensionContext ruleTargetExtension() {
      return getRuleContext(RuleTargetExtensionContext.class, 0);
    }

    public RuleTargetEscapeContext ruleTargetEscape() {
      return getRuleContext(RuleTargetEscapeContext.class, 0);
    }

    public RuleTargetEvaluateContext ruleTargetEvaluate() {
      return getRuleContext(RuleTargetEvaluateContext.class, 0);
    }

    public RuleTargetIdContext ruleTargetId() {
      return getRuleContext(RuleTargetIdContext.class, 0);
    }

    public RuleTargetPointerContext ruleTargetPointer() {
      return getRuleContext(RuleTargetPointerContext.class, 0);
    }

    public RuleTargetQtyContext ruleTargetQty() {
      return getRuleContext(RuleTargetQtyContext.class, 0);
    }

    public RuleTargetReferenceContext ruleTargetReference() {
      return getRuleContext(RuleTargetReferenceContext.class, 0);
    }

    public RuleTargetTranslateContext ruleTargetTranslate() {
      return getRuleContext(RuleTargetTranslateContext.class, 0);
    }

    public RuleTargetTruncateContext ruleTargetTruncate() {
      return getRuleContext(RuleTargetTruncateContext.class, 0);
    }

    public RuleTargetUuidContext ruleTargetUuid() {
      return getRuleContext(RuleTargetUuidContext.class, 0);
    }

    public RuleTargetContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_ruleTarget;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor)
        return ((FhirMapJavaVisitor<? extends T>) visitor).visitRuleTarget(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetContext ruleTarget() throws RecognitionException {
    RuleTargetContext _localctx = new RuleTargetContext(_ctx, getState());
    enterRule(_localctx, 86, RULE_ruleTarget);
    try {
      setState(522);
      _errHandler.sync(this);
      switch (getInterpreter().adaptivePredict(_input, 31, _ctx)) {
        case 1:
          enterOuterAlt(_localctx, 1);
        {
          setState(502);
          ruleTargetAppend();
        }
        break;
        case 2:
          enterOuterAlt(_localctx, 2);
        {
          setState(503);
          ruleTargetAs();
        }
        break;
        case 3:
          enterOuterAlt(_localctx, 3);
        {
          setState(504);
          ruleTargetAssign();
        }
        break;
        case 4:
          enterOuterAlt(_localctx, 4);
        {
          setState(505);
          ruleTargetC();
        }
        break;
        case 5:
          enterOuterAlt(_localctx, 5);
        {
          setState(506);
          ruleTargetCast();
        }
        break;
        case 6:
          enterOuterAlt(_localctx, 6);
        {
          setState(507);
          ruleTargetCC();
        }
        break;
        case 7:
          enterOuterAlt(_localctx, 7);
        {
          setState(508);
          ruleTargetCp();
        }
        break;
        case 8:
          enterOuterAlt(_localctx, 8);
        {
          setState(509);
          ruleTargetCopy();
        }
        break;
        case 9:
          enterOuterAlt(_localctx, 9);
        {
          setState(510);
          ruleTargetCreate();
        }
        break;
        case 10:
          enterOuterAlt(_localctx, 10);
        {
          setState(511);
          ruleTargetDateOp();
        }
        break;
        case 11:
          enterOuterAlt(_localctx, 11);
        {
          setState(512);
          ruleTargetExtension();
        }
        break;
        case 12:
          enterOuterAlt(_localctx, 12);
        {
          setState(513);
          ruleTargetEscape();
        }
        break;
        case 13:
          enterOuterAlt(_localctx, 13);
        {
          setState(514);
          ruleTargetEvaluate();
        }
        break;
        case 14:
          enterOuterAlt(_localctx, 14);
        {
          setState(515);
          ruleTargetId();
        }
        break;
        case 15:
          enterOuterAlt(_localctx, 15);
        {
          setState(516);
          ruleTargetPointer();
        }
        break;
        case 16:
          enterOuterAlt(_localctx, 16);
        {
          setState(517);
          ruleTargetQty();
        }
        break;
        case 17:
          enterOuterAlt(_localctx, 17);
        {
          setState(518);
          ruleTargetReference();
        }
        break;
        case 18:
          enterOuterAlt(_localctx, 18);
        {
          setState(519);
          ruleTargetTranslate();
        }
        break;
        case 19:
          enterOuterAlt(_localctx, 19);
        {
          setState(520);
          ruleTargetTruncate();
        }
        break;
        case 20:
          enterOuterAlt(_localctx, 20);
        {
          setState(521);
          ruleTargetUuid();
        }
        break;
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetContextContext extends ParserRuleContext {
    public RuleContextContext ruleContext() {
      return getRuleContext(RuleContextContext.class, 0);
    }

    public RuleTargetContextContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_ruleTargetContext;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor)
        return ((FhirMapJavaVisitor<? extends T>) visitor).visitRuleTargetContext(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetContextContext ruleTargetContext() throws RecognitionException {
    RuleTargetContextContext _localctx = new RuleTargetContextContext(_ctx, getState());
    enterRule(_localctx, 88, RULE_ruleTargetContext);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(524);
        ruleContext();
        setState(525);
        match(T__0);
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetAsContext extends ParserRuleContext {
    public RuleContextContext ruleContext() {
      return getRuleContext(RuleContextContext.class, 0);
    }

    public RuleTargetVariableContext ruleTargetVariable() {
      return getRuleContext(RuleTargetVariableContext.class, 0);
    }

    public RuleTargetAsContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_ruleTargetAs;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor)
        return ((FhirMapJavaVisitor<? extends T>) visitor).visitRuleTargetAs(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetAsContext ruleTargetAs() throws RecognitionException {
    RuleTargetAsContext _localctx = new RuleTargetAsContext(_ctx, getState());
    enterRule(_localctx, 90, RULE_ruleTargetAs);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(527);
        ruleContext();
        setState(528);
        ruleTargetVariable();
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetAssignContext extends ParserRuleContext {
    public RuleTargetContextContext ruleTargetContext() {
      return getRuleContext(RuleTargetContextContext.class, 0);
    }

    public RuleTargetAssignValueContext ruleTargetAssignValue() {
      return getRuleContext(RuleTargetAssignValueContext.class, 0);
    }

    public RuleTargetVariableContext ruleTargetVariable() {
      return getRuleContext(RuleTargetVariableContext.class, 0);
    }

    public RuleTargetAssignContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_ruleTargetAssign;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor)
        return ((FhirMapJavaVisitor<? extends T>) visitor).visitRuleTargetAssign(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetAssignContext ruleTargetAssign() throws RecognitionException {
    RuleTargetAssignContext _localctx = new RuleTargetAssignContext(_ctx, getState());
    enterRule(_localctx, 92, RULE_ruleTargetAssign);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(530);
        ruleTargetContext();
        setState(531);
        ruleTargetAssignValue();
        setState(533);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la == AS) {
          {
            setState(532);
            ruleTargetVariable();
          }
        }

      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetAssignValueContext extends ParserRuleContext {
    public QuotedStringWQuotesContext quotedStringWQuotes() {
      return getRuleContext(QuotedStringWQuotesContext.class, 0);
    }

    public IdentifierContext identifier() {
      return getRuleContext(IdentifierContext.class, 0);
    }

    public RuleTargetAssignValueContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_ruleTargetAssignValue;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor)
        return ((FhirMapJavaVisitor<? extends T>) visitor).visitRuleTargetAssignValue(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetAssignValueContext ruleTargetAssignValue() throws RecognitionException {
    RuleTargetAssignValueContext _localctx = new RuleTargetAssignValueContext(_ctx, getState());
    enterRule(_localctx, 94, RULE_ruleTargetAssignValue);
    try {
      setState(537);
      _errHandler.sync(this);
      switch (_input.LA(1)) {
        case QIDENTIFIER:
        case QSTRING:
          enterOuterAlt(_localctx, 1);
        {
          setState(535);
          quotedStringWQuotes();
        }
        break;
        case SOURCE:
        case TARGET:
        case QUERIED:
        case PRODUCED:
        case APPEND:
        case CAST:
        case C:
        case CC:
        case CODE:
        case CP:
        case CODEABLECONCEPT:
        case CODING:
        case COPY:
        case CREATE:
        case DATEOP:
        case DEFAULT:
        case DISPLAY:
        case ESCAPE:
        case EXTENSION:
        case EVALUATE:
        case ID:
        case MAP:
        case POINTER:
        case QTY:
        case REFERENCE:
        case SYSTEM:
        case TRANSLATE:
        case TRUNCATE:
        case TYPES:
        case UUID:
        case IDENTIFIER:
          enterOuterAlt(_localctx, 2);
        {
          setState(536);
          identifier();
        }
        break;
        default:
          throw new NoViableAltException(this);
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetAppendContext extends ParserRuleContext {
    public TerminalNode APPEND() {
      return getToken(FhirMapJavaParser.APPEND, 0);
    }

    public RuleTargetAppendSourcesContext ruleTargetAppendSources() {
      return getRuleContext(RuleTargetAppendSourcesContext.class, 0);
    }

    public RuleTargetContextContext ruleTargetContext() {
      return getRuleContext(RuleTargetContextContext.class, 0);
    }

    public RuleTargetVariableContext ruleTargetVariable() {
      return getRuleContext(RuleTargetVariableContext.class, 0);
    }

    public RuleTargetAppendContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_ruleTargetAppend;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor)
        return ((FhirMapJavaVisitor<? extends T>) visitor).visitRuleTargetAppend(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetAppendContext ruleTargetAppend() throws RecognitionException {
    RuleTargetAppendContext _localctx = new RuleTargetAppendContext(_ctx, getState());
    enterRule(_localctx, 96, RULE_ruleTargetAppend);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(540);
        _errHandler.sync(this);
        switch (getInterpreter().adaptivePredict(_input, 34, _ctx)) {
          case 1: {
            setState(539);
            ruleTargetContext();
          }
          break;
        }
        setState(542);
        match(APPEND);
        setState(543);
        match(T__3);
        setState(544);
        ruleTargetAppendSources();
        setState(545);
        match(T__4);
        setState(547);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la == AS) {
          {
            setState(546);
            ruleTargetVariable();
          }
        }

      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetAppendSourcesContext extends ParserRuleContext {
    public List<RuleTargetAppendSourceContext> ruleTargetAppendSource() {
      return getRuleContexts(RuleTargetAppendSourceContext.class);
    }

    public RuleTargetAppendSourceContext ruleTargetAppendSource(int i) {
      return getRuleContext(RuleTargetAppendSourceContext.class, i);
    }

    public RuleTargetAppendSourcesContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_ruleTargetAppendSources;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor)
        return ((FhirMapJavaVisitor<? extends T>) visitor).visitRuleTargetAppendSources(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetAppendSourcesContext ruleTargetAppendSources() throws RecognitionException {
    RuleTargetAppendSourcesContext _localctx = new RuleTargetAppendSourcesContext(_ctx, getState());
    enterRule(_localctx, 98, RULE_ruleTargetAppendSources);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(549);
        ruleTargetAppendSource();
        setState(554);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la == T__5) {
          {
            {
              setState(550);
              match(T__5);
              setState(551);
              ruleTargetAppendSource();
            }
          }
          setState(556);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetAppendSourceContext extends ParserRuleContext {
    public IdentifierContext identifier() {
      return getRuleContext(IdentifierContext.class, 0);
    }

    public QuotedStringWQuotesContext quotedStringWQuotes() {
      return getRuleContext(QuotedStringWQuotesContext.class, 0);
    }

    public RuleTargetAppendSourceContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_ruleTargetAppendSource;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor)
        return ((FhirMapJavaVisitor<? extends T>) visitor).visitRuleTargetAppendSource(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetAppendSourceContext ruleTargetAppendSource() throws RecognitionException {
    RuleTargetAppendSourceContext _localctx = new RuleTargetAppendSourceContext(_ctx, getState());
    enterRule(_localctx, 100, RULE_ruleTargetAppendSource);
    try {
      setState(559);
      _errHandler.sync(this);
      switch (_input.LA(1)) {
        case SOURCE:
        case TARGET:
        case QUERIED:
        case PRODUCED:
        case APPEND:
        case CAST:
        case C:
        case CC:
        case CODE:
        case CP:
        case CODEABLECONCEPT:
        case CODING:
        case COPY:
        case CREATE:
        case DATEOP:
        case DEFAULT:
        case DISPLAY:
        case ESCAPE:
        case EXTENSION:
        case EVALUATE:
        case ID:
        case MAP:
        case POINTER:
        case QTY:
        case REFERENCE:
        case SYSTEM:
        case TRANSLATE:
        case TRUNCATE:
        case TYPES:
        case UUID:
        case IDENTIFIER:
          enterOuterAlt(_localctx, 1);
        {
          setState(557);
          identifier();
        }
        break;
        case QIDENTIFIER:
        case QSTRING:
          enterOuterAlt(_localctx, 2);
        {
          setState(558);
          quotedStringWQuotes();
        }
        break;
        default:
          throw new NoViableAltException(this);
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetCContext extends ParserRuleContext {
    public TerminalNode C() {
      return getToken(FhirMapJavaParser.C, 0);
    }

    public RuleTargetCSystemContext ruleTargetCSystem() {
      return getRuleContext(RuleTargetCSystemContext.class, 0);
    }

    public RuleTargetCCodeContext ruleTargetCCode() {
      return getRuleContext(RuleTargetCCodeContext.class, 0);
    }

    public RuleTargetContextContext ruleTargetContext() {
      return getRuleContext(RuleTargetContextContext.class, 0);
    }

    public RuleTargetCDisplayContext ruleTargetCDisplay() {
      return getRuleContext(RuleTargetCDisplayContext.class, 0);
    }

    public RuleTargetVariableContext ruleTargetVariable() {
      return getRuleContext(RuleTargetVariableContext.class, 0);
    }

    public RuleTargetCContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_ruleTargetC;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor)
        return ((FhirMapJavaVisitor<? extends T>) visitor).visitRuleTargetC(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetCContext ruleTargetC() throws RecognitionException {
    RuleTargetCContext _localctx = new RuleTargetCContext(_ctx, getState());
    enterRule(_localctx, 102, RULE_ruleTargetC);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(562);
        _errHandler.sync(this);
        switch (getInterpreter().adaptivePredict(_input, 38, _ctx)) {
          case 1: {
            setState(561);
            ruleTargetContext();
          }
          break;
        }
        setState(564);
        match(C);
        setState(565);
        match(T__3);
        setState(566);
        ruleTargetCSystem();
        setState(567);
        match(T__5);
        setState(568);
        ruleTargetCCode();
        setState(571);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la == T__5) {
          {
            setState(569);
            match(T__5);
            setState(570);
            ruleTargetCDisplay();
          }
        }

        setState(573);
        match(T__4);
        setState(575);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la == AS) {
          {
            setState(574);
            ruleTargetVariable();
          }
        }

      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetCSystemContext extends ParserRuleContext {
    public QuotedUrlContext quotedUrl() {
      return getRuleContext(QuotedUrlContext.class, 0);
    }

    public RuleTargetCSystemContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_ruleTargetCSystem;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor)
        return ((FhirMapJavaVisitor<? extends T>) visitor).visitRuleTargetCSystem(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetCSystemContext ruleTargetCSystem() throws RecognitionException {
    RuleTargetCSystemContext _localctx = new RuleTargetCSystemContext(_ctx, getState());
    enterRule(_localctx, 104, RULE_ruleTargetCSystem);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(577);
        quotedUrl();
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetCCodeContext extends ParserRuleContext {
    public QuotedStringWQuotesContext quotedStringWQuotes() {
      return getRuleContext(QuotedStringWQuotesContext.class, 0);
    }

    public IdentifierContext identifier() {
      return getRuleContext(IdentifierContext.class, 0);
    }

    public RuleTargetCCodeContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_ruleTargetCCode;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor)
        return ((FhirMapJavaVisitor<? extends T>) visitor).visitRuleTargetCCode(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetCCodeContext ruleTargetCCode() throws RecognitionException {
    RuleTargetCCodeContext _localctx = new RuleTargetCCodeContext(_ctx, getState());
    enterRule(_localctx, 106, RULE_ruleTargetCCode);
    try {
      setState(581);
      _errHandler.sync(this);
      switch (_input.LA(1)) {
        case QIDENTIFIER:
        case QSTRING:
          enterOuterAlt(_localctx, 1);
        {
          setState(579);
          quotedStringWQuotes();
        }
        break;
        case SOURCE:
        case TARGET:
        case QUERIED:
        case PRODUCED:
        case APPEND:
        case CAST:
        case C:
        case CC:
        case CODE:
        case CP:
        case CODEABLECONCEPT:
        case CODING:
        case COPY:
        case CREATE:
        case DATEOP:
        case DEFAULT:
        case DISPLAY:
        case ESCAPE:
        case EXTENSION:
        case EVALUATE:
        case ID:
        case MAP:
        case POINTER:
        case QTY:
        case REFERENCE:
        case SYSTEM:
        case TRANSLATE:
        case TRUNCATE:
        case TYPES:
        case UUID:
        case IDENTIFIER:
          enterOuterAlt(_localctx, 2);
        {
          setState(580);
          identifier();
        }
        break;
        default:
          throw new NoViableAltException(this);
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetCDisplayContext extends ParserRuleContext {
    public QuotedStringContext quotedString() {
      return getRuleContext(QuotedStringContext.class, 0);
    }

    public RuleTargetCDisplayContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_ruleTargetCDisplay;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor)
        return ((FhirMapJavaVisitor<? extends T>) visitor).visitRuleTargetCDisplay(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetCDisplayContext ruleTargetCDisplay() throws RecognitionException {
    RuleTargetCDisplayContext _localctx = new RuleTargetCDisplayContext(_ctx, getState());
    enterRule(_localctx, 108, RULE_ruleTargetCDisplay);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(583);
        quotedString();
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetCastContext extends ParserRuleContext {
    public TerminalNode CAST() {
      return getToken(FhirMapJavaParser.CAST, 0);
    }

    public RuleTargetCastSourceContext ruleTargetCastSource() {
      return getRuleContext(RuleTargetCastSourceContext.class, 0);
    }

    public RuleTargetContextContext ruleTargetContext() {
      return getRuleContext(RuleTargetContextContext.class, 0);
    }

    public RuleTargetCastTypeContext ruleTargetCastType() {
      return getRuleContext(RuleTargetCastTypeContext.class, 0);
    }

    public RuleTargetVariableContext ruleTargetVariable() {
      return getRuleContext(RuleTargetVariableContext.class, 0);
    }

    public RuleTargetCastContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_ruleTargetCast;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor)
        return ((FhirMapJavaVisitor<? extends T>) visitor).visitRuleTargetCast(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetCastContext ruleTargetCast() throws RecognitionException {
    RuleTargetCastContext _localctx = new RuleTargetCastContext(_ctx, getState());
    enterRule(_localctx, 110, RULE_ruleTargetCast);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(586);
        _errHandler.sync(this);
        switch (getInterpreter().adaptivePredict(_input, 42, _ctx)) {
          case 1: {
            setState(585);
            ruleTargetContext();
          }
          break;
        }
        setState(588);
        match(CAST);
        setState(589);
        match(T__3);
        setState(590);
        ruleTargetCastSource();
        setState(593);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la == T__5) {
          {
            setState(591);
            match(T__5);
            setState(592);
            ruleTargetCastType();
          }
        }

        setState(595);
        match(T__4);
        setState(597);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la == AS) {
          {
            setState(596);
            ruleTargetVariable();
          }
        }

      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetCastSourceContext extends ParserRuleContext {
    public IdentifierContext identifier() {
      return getRuleContext(IdentifierContext.class, 0);
    }

    public RuleTargetCastSourceContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_ruleTargetCastSource;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor)
        return ((FhirMapJavaVisitor<? extends T>) visitor).visitRuleTargetCastSource(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetCastSourceContext ruleTargetCastSource() throws RecognitionException {
    RuleTargetCastSourceContext _localctx = new RuleTargetCastSourceContext(_ctx, getState());
    enterRule(_localctx, 112, RULE_ruleTargetCastSource);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(599);
        identifier();
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetCastTypeContext extends ParserRuleContext {
    public IdentifierContext identifier() {
      return getRuleContext(IdentifierContext.class, 0);
    }

    public RuleTargetCastTypeContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_ruleTargetCastType;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor)
        return ((FhirMapJavaVisitor<? extends T>) visitor).visitRuleTargetCastType(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetCastTypeContext ruleTargetCastType() throws RecognitionException {
    RuleTargetCastTypeContext _localctx = new RuleTargetCastTypeContext(_ctx, getState());
    enterRule(_localctx, 114, RULE_ruleTargetCastType);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(601);
        identifier();
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetCCContext extends ParserRuleContext {
    public RuleTargetCC1Context ruleTargetCC1() {
      return getRuleContext(RuleTargetCC1Context.class, 0);
    }

    public RuleTargetCC2Context ruleTargetCC2() {
      return getRuleContext(RuleTargetCC2Context.class, 0);
    }

    public RuleTargetVariableContext ruleTargetVariable() {
      return getRuleContext(RuleTargetVariableContext.class, 0);
    }

    public RuleTargetCCContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_ruleTargetCC;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor)
        return ((FhirMapJavaVisitor<? extends T>) visitor).visitRuleTargetCC(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetCCContext ruleTargetCC() throws RecognitionException {
    RuleTargetCCContext _localctx = new RuleTargetCCContext(_ctx, getState());
    enterRule(_localctx, 116, RULE_ruleTargetCC);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(605);
        _errHandler.sync(this);
        switch (getInterpreter().adaptivePredict(_input, 45, _ctx)) {
          case 1: {
            setState(603);
            ruleTargetCC1();
          }
          break;
          case 2: {
            setState(604);
            ruleTargetCC2();
          }
          break;
        }
        setState(608);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la == AS) {
          {
            setState(607);
            ruleTargetVariable();
          }
        }

      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetCC1Context extends ParserRuleContext {
    public TerminalNode CC() {
      return getToken(FhirMapJavaParser.CC, 0);
    }

    public RuleTargetCC1TextContext ruleTargetCC1Text() {
      return getRuleContext(RuleTargetCC1TextContext.class, 0);
    }

    public RuleTargetContextContext ruleTargetContext() {
      return getRuleContext(RuleTargetContextContext.class, 0);
    }

    public RuleTargetVariableContext ruleTargetVariable() {
      return getRuleContext(RuleTargetVariableContext.class, 0);
    }

    public RuleTargetCC1Context(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_ruleTargetCC1;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor)
        return ((FhirMapJavaVisitor<? extends T>) visitor).visitRuleTargetCC1(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetCC1Context ruleTargetCC1() throws RecognitionException {
    RuleTargetCC1Context _localctx = new RuleTargetCC1Context(_ctx, getState());
    enterRule(_localctx, 118, RULE_ruleTargetCC1);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(611);
        _errHandler.sync(this);
        switch (getInterpreter().adaptivePredict(_input, 47, _ctx)) {
          case 1: {
            setState(610);
            ruleTargetContext();
          }
          break;
        }
        setState(613);
        match(CC);
        setState(614);
        match(T__3);
        setState(615);
        ruleTargetCC1Text();
        setState(616);
        match(T__4);
        setState(618);
        _errHandler.sync(this);
        switch (getInterpreter().adaptivePredict(_input, 48, _ctx)) {
          case 1: {
            setState(617);
            ruleTargetVariable();
          }
          break;
        }
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetCC1TextContext extends ParserRuleContext {
    public QuotedStringContext quotedString() {
      return getRuleContext(QuotedStringContext.class, 0);
    }

    public RuleTargetCC1TextContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_ruleTargetCC1Text;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor)
        return ((FhirMapJavaVisitor<? extends T>) visitor).visitRuleTargetCC1Text(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetCC1TextContext ruleTargetCC1Text() throws RecognitionException {
    RuleTargetCC1TextContext _localctx = new RuleTargetCC1TextContext(_ctx, getState());
    enterRule(_localctx, 120, RULE_ruleTargetCC1Text);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(620);
        quotedString();
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetCC2Context extends ParserRuleContext {
    public TerminalNode CC() {
      return getToken(FhirMapJavaParser.CC, 0);
    }

    public RuleTargetCC2SystemContext ruleTargetCC2System() {
      return getRuleContext(RuleTargetCC2SystemContext.class, 0);
    }

    public RuleTargetCC2CodeContext ruleTargetCC2Code() {
      return getRuleContext(RuleTargetCC2CodeContext.class, 0);
    }

    public RuleTargetContextContext ruleTargetContext() {
      return getRuleContext(RuleTargetContextContext.class, 0);
    }

    public RuleTargetCC2DisplayContext ruleTargetCC2Display() {
      return getRuleContext(RuleTargetCC2DisplayContext.class, 0);
    }

    public RuleTargetVariableContext ruleTargetVariable() {
      return getRuleContext(RuleTargetVariableContext.class, 0);
    }

    public RuleTargetCC2Context(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_ruleTargetCC2;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor)
        return ((FhirMapJavaVisitor<? extends T>) visitor).visitRuleTargetCC2(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetCC2Context ruleTargetCC2() throws RecognitionException {
    RuleTargetCC2Context _localctx = new RuleTargetCC2Context(_ctx, getState());
    enterRule(_localctx, 122, RULE_ruleTargetCC2);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(623);
        _errHandler.sync(this);
        switch (getInterpreter().adaptivePredict(_input, 49, _ctx)) {
          case 1: {
            setState(622);
            ruleTargetContext();
          }
          break;
        }
        setState(625);
        match(CC);
        setState(626);
        match(T__3);
        setState(627);
        ruleTargetCC2System();
        setState(628);
        match(T__5);
        setState(629);
        ruleTargetCC2Code();
        setState(632);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la == T__5) {
          {
            setState(630);
            match(T__5);
            setState(631);
            ruleTargetCC2Display();
          }
        }

        setState(634);
        match(T__4);
        setState(636);
        _errHandler.sync(this);
        switch (getInterpreter().adaptivePredict(_input, 51, _ctx)) {
          case 1: {
            setState(635);
            ruleTargetVariable();
          }
          break;
        }
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetCC2SystemContext extends ParserRuleContext {
    public QuotedUrlContext quotedUrl() {
      return getRuleContext(QuotedUrlContext.class, 0);
    }

    public RuleTargetCC2SystemContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_ruleTargetCC2System;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor)
        return ((FhirMapJavaVisitor<? extends T>) visitor).visitRuleTargetCC2System(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetCC2SystemContext ruleTargetCC2System() throws RecognitionException {
    RuleTargetCC2SystemContext _localctx = new RuleTargetCC2SystemContext(_ctx, getState());
    enterRule(_localctx, 124, RULE_ruleTargetCC2System);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(638);
        quotedUrl();
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetCC2CodeContext extends ParserRuleContext {
    public QuotedStringWQuotesContext quotedStringWQuotes() {
      return getRuleContext(QuotedStringWQuotesContext.class, 0);
    }

    public IdentifierContext identifier() {
      return getRuleContext(IdentifierContext.class, 0);
    }

    public RuleTargetCC2CodeContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_ruleTargetCC2Code;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor)
        return ((FhirMapJavaVisitor<? extends T>) visitor).visitRuleTargetCC2Code(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetCC2CodeContext ruleTargetCC2Code() throws RecognitionException {
    RuleTargetCC2CodeContext _localctx = new RuleTargetCC2CodeContext(_ctx, getState());
    enterRule(_localctx, 126, RULE_ruleTargetCC2Code);
    try {
      setState(642);
      _errHandler.sync(this);
      switch (_input.LA(1)) {
        case QIDENTIFIER:
        case QSTRING:
          enterOuterAlt(_localctx, 1);
        {
          setState(640);
          quotedStringWQuotes();
        }
        break;
        case SOURCE:
        case TARGET:
        case QUERIED:
        case PRODUCED:
        case APPEND:
        case CAST:
        case C:
        case CC:
        case CODE:
        case CP:
        case CODEABLECONCEPT:
        case CODING:
        case COPY:
        case CREATE:
        case DATEOP:
        case DEFAULT:
        case DISPLAY:
        case ESCAPE:
        case EXTENSION:
        case EVALUATE:
        case ID:
        case MAP:
        case POINTER:
        case QTY:
        case REFERENCE:
        case SYSTEM:
        case TRANSLATE:
        case TRUNCATE:
        case TYPES:
        case UUID:
        case IDENTIFIER:
          enterOuterAlt(_localctx, 2);
        {
          setState(641);
          identifier();
        }
        break;
        default:
          throw new NoViableAltException(this);
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetCC2DisplayContext extends ParserRuleContext {
    public QuotedStringContext quotedString() {
      return getRuleContext(QuotedStringContext.class, 0);
    }

    public RuleTargetCC2DisplayContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_ruleTargetCC2Display;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor)
        return ((FhirMapJavaVisitor<? extends T>) visitor).visitRuleTargetCC2Display(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetCC2DisplayContext ruleTargetCC2Display() throws RecognitionException {
    RuleTargetCC2DisplayContext _localctx = new RuleTargetCC2DisplayContext(_ctx, getState());
    enterRule(_localctx, 128, RULE_ruleTargetCC2Display);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(644);
        quotedString();
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetCpContext extends ParserRuleContext {
    public TerminalNode CP() {
      return getToken(FhirMapJavaParser.CP, 0);
    }

    public RuleTargetCpVariableContext ruleTargetCpVariable() {
      return getRuleContext(RuleTargetCpVariableContext.class, 0);
    }

    public RuleTargetContextContext ruleTargetContext() {
      return getRuleContext(RuleTargetContextContext.class, 0);
    }

    public RuleTargetCpSystemContext ruleTargetCpSystem() {
      return getRuleContext(RuleTargetCpSystemContext.class, 0);
    }

    public RuleTargetVariableContext ruleTargetVariable() {
      return getRuleContext(RuleTargetVariableContext.class, 0);
    }

    public RuleTargetCpContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_ruleTargetCp;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor)
        return ((FhirMapJavaVisitor<? extends T>) visitor).visitRuleTargetCp(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetCpContext ruleTargetCp() throws RecognitionException {
    RuleTargetCpContext _localctx = new RuleTargetCpContext(_ctx, getState());
    enterRule(_localctx, 130, RULE_ruleTargetCp);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(647);
        _errHandler.sync(this);
        switch (getInterpreter().adaptivePredict(_input, 53, _ctx)) {
          case 1: {
            setState(646);
            ruleTargetContext();
          }
          break;
        }
        setState(649);
        match(CP);
        setState(650);
        match(T__3);
        setState(654);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la == QIDENTIFIER || _la == QSTRING) {
          {
            setState(651);
            ruleTargetCpSystem();
            setState(652);
            match(T__5);
          }
        }

        setState(656);
        ruleTargetCpVariable();
        setState(657);
        match(T__4);
        setState(659);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la == AS) {
          {
            setState(658);
            ruleTargetVariable();
          }
        }

      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetCpSystemContext extends ParserRuleContext {
    public QuotedUrlContext quotedUrl() {
      return getRuleContext(QuotedUrlContext.class, 0);
    }

    public RuleTargetCpSystemContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_ruleTargetCpSystem;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor)
        return ((FhirMapJavaVisitor<? extends T>) visitor).visitRuleTargetCpSystem(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetCpSystemContext ruleTargetCpSystem() throws RecognitionException {
    RuleTargetCpSystemContext _localctx = new RuleTargetCpSystemContext(_ctx, getState());
    enterRule(_localctx, 132, RULE_ruleTargetCpSystem);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(661);
        quotedUrl();
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetCpVariableContext extends ParserRuleContext {
    public IdentifierContext identifier() {
      return getRuleContext(IdentifierContext.class, 0);
    }

    public RuleTargetCpVariableContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_ruleTargetCpVariable;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor)
        return ((FhirMapJavaVisitor<? extends T>) visitor).visitRuleTargetCpVariable(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetCpVariableContext ruleTargetCpVariable() throws RecognitionException {
    RuleTargetCpVariableContext _localctx = new RuleTargetCpVariableContext(_ctx, getState());
    enterRule(_localctx, 134, RULE_ruleTargetCpVariable);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(663);
        identifier();
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetCopyContext extends ParserRuleContext {
    public RuleTargetContextContext ruleTargetContext() {
      return getRuleContext(RuleTargetContextContext.class, 0);
    }

    public TerminalNode COPY() {
      return getToken(FhirMapJavaParser.COPY, 0);
    }

    public RuleTargetCopySourceContext ruleTargetCopySource() {
      return getRuleContext(RuleTargetCopySourceContext.class, 0);
    }

    public RuleTargetVariableContext ruleTargetVariable() {
      return getRuleContext(RuleTargetVariableContext.class, 0);
    }

    public RuleTargetCopyContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_ruleTargetCopy;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor)
        return ((FhirMapJavaVisitor<? extends T>) visitor).visitRuleTargetCopy(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetCopyContext ruleTargetCopy() throws RecognitionException {
    RuleTargetCopyContext _localctx = new RuleTargetCopyContext(_ctx, getState());
    enterRule(_localctx, 136, RULE_ruleTargetCopy);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(665);
        ruleTargetContext();
        setState(666);
        match(COPY);
        setState(667);
        match(T__3);
        setState(668);
        ruleTargetCopySource();
        setState(669);
        match(T__4);
        setState(671);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la == AS) {
          {
            setState(670);
            ruleTargetVariable();
          }
        }

      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetCopySourceContext extends ParserRuleContext {
    public IdentifierContext identifier() {
      return getRuleContext(IdentifierContext.class, 0);
    }

    public RuleTargetCopySourceContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_ruleTargetCopySource;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor)
        return ((FhirMapJavaVisitor<? extends T>) visitor).visitRuleTargetCopySource(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetCopySourceContext ruleTargetCopySource() throws RecognitionException {
    RuleTargetCopySourceContext _localctx = new RuleTargetCopySourceContext(_ctx, getState());
    enterRule(_localctx, 138, RULE_ruleTargetCopySource);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(673);
        identifier();
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetCreateContext extends ParserRuleContext {
    public TerminalNode CREATE() {
      return getToken(FhirMapJavaParser.CREATE, 0);
    }

    public RuleTargetCreateTypeContext ruleTargetCreateType() {
      return getRuleContext(RuleTargetCreateTypeContext.class, 0);
    }

    public RuleTargetContextContext ruleTargetContext() {
      return getRuleContext(RuleTargetContextContext.class, 0);
    }

    public RuleTargetVariableContext ruleTargetVariable() {
      return getRuleContext(RuleTargetVariableContext.class, 0);
    }

    public RuleTargetCreateContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_ruleTargetCreate;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor)
        return ((FhirMapJavaVisitor<? extends T>) visitor).visitRuleTargetCreate(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetCreateContext ruleTargetCreate() throws RecognitionException {
    RuleTargetCreateContext _localctx = new RuleTargetCreateContext(_ctx, getState());
    enterRule(_localctx, 140, RULE_ruleTargetCreate);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(676);
        _errHandler.sync(this);
        switch (getInterpreter().adaptivePredict(_input, 57, _ctx)) {
          case 1: {
            setState(675);
            ruleTargetContext();
          }
          break;
        }
        setState(678);
        match(CREATE);
        setState(679);
        match(T__3);
        setState(680);
        ruleTargetCreateType();
        setState(681);
        match(T__4);
        setState(683);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la == AS) {
          {
            setState(682);
            ruleTargetVariable();
          }
        }

      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetCreateTypeContext extends ParserRuleContext {
    public QuotedIdentifierContext quotedIdentifier() {
      return getRuleContext(QuotedIdentifierContext.class, 0);
    }

    public RuleTargetCreateTypeContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_ruleTargetCreateType;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor)
        return ((FhirMapJavaVisitor<? extends T>) visitor).visitRuleTargetCreateType(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetCreateTypeContext ruleTargetCreateType() throws RecognitionException {
    RuleTargetCreateTypeContext _localctx = new RuleTargetCreateTypeContext(_ctx, getState());
    enterRule(_localctx, 142, RULE_ruleTargetCreateType);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(685);
        quotedIdentifier();
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetDateOpContext extends ParserRuleContext {
    public TerminalNode DATEOP() {
      return getToken(FhirMapJavaParser.DATEOP, 0);
    }

    public RuleTargetDateOpVariableContext ruleTargetDateOpVariable() {
      return getRuleContext(RuleTargetDateOpVariableContext.class, 0);
    }

    public RuleTargetDateOpOperationContext ruleTargetDateOpOperation() {
      return getRuleContext(RuleTargetDateOpOperationContext.class, 0);
    }

    public RuleTargetContextContext ruleTargetContext() {
      return getRuleContext(RuleTargetContextContext.class, 0);
    }

    public RuleTargetDateOpVariable2Context ruleTargetDateOpVariable2() {
      return getRuleContext(RuleTargetDateOpVariable2Context.class, 0);
    }

    public RuleTargetVariableContext ruleTargetVariable() {
      return getRuleContext(RuleTargetVariableContext.class, 0);
    }

    public RuleTargetDateOpContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_ruleTargetDateOp;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor)
        return ((FhirMapJavaVisitor<? extends T>) visitor).visitRuleTargetDateOp(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetDateOpContext ruleTargetDateOp() throws RecognitionException {
    RuleTargetDateOpContext _localctx = new RuleTargetDateOpContext(_ctx, getState());
    enterRule(_localctx, 144, RULE_ruleTargetDateOp);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(688);
        _errHandler.sync(this);
        switch (getInterpreter().adaptivePredict(_input, 59, _ctx)) {
          case 1: {
            setState(687);
            ruleTargetContext();
          }
          break;
        }
        setState(690);
        match(DATEOP);
        setState(691);
        match(T__3);
        setState(692);
        ruleTargetDateOpVariable();
        setState(693);
        match(T__5);
        setState(694);
        ruleTargetDateOpOperation();
        setState(697);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la == T__5) {
          {
            setState(695);
            match(T__5);
            setState(696);
            ruleTargetDateOpVariable2();
          }
        }

        setState(699);
        match(T__4);
        setState(701);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la == AS) {
          {
            setState(700);
            ruleTargetVariable();
          }
        }

      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetDateOpVariableContext extends ParserRuleContext {
    public IdentifierContext identifier() {
      return getRuleContext(IdentifierContext.class, 0);
    }

    public RuleTargetDateOpVariableContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_ruleTargetDateOpVariable;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor)
        return ((FhirMapJavaVisitor<? extends T>) visitor).visitRuleTargetDateOpVariable(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetDateOpVariableContext ruleTargetDateOpVariable() throws RecognitionException {
    RuleTargetDateOpVariableContext _localctx = new RuleTargetDateOpVariableContext(_ctx, getState());
    enterRule(_localctx, 146, RULE_ruleTargetDateOpVariable);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(703);
        identifier();
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetDateOpOperationContext extends ParserRuleContext {
    public QuotedStringContext quotedString() {
      return getRuleContext(QuotedStringContext.class, 0);
    }

    public RuleTargetDateOpOperationContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_ruleTargetDateOpOperation;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor)
        return ((FhirMapJavaVisitor<? extends T>) visitor).visitRuleTargetDateOpOperation(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetDateOpOperationContext ruleTargetDateOpOperation() throws RecognitionException {
    RuleTargetDateOpOperationContext _localctx = new RuleTargetDateOpOperationContext(_ctx, getState());
    enterRule(_localctx, 148, RULE_ruleTargetDateOpOperation);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(705);
        quotedString();
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetDateOpVariable2Context extends ParserRuleContext {
    public IdentifierContext identifier() {
      return getRuleContext(IdentifierContext.class, 0);
    }

    public RuleTargetDateOpVariable2Context(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_ruleTargetDateOpVariable2;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor)
        return ((FhirMapJavaVisitor<? extends T>) visitor).visitRuleTargetDateOpVariable2(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetDateOpVariable2Context ruleTargetDateOpVariable2() throws RecognitionException {
    RuleTargetDateOpVariable2Context _localctx = new RuleTargetDateOpVariable2Context(_ctx, getState());
    enterRule(_localctx, 150, RULE_ruleTargetDateOpVariable2);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(707);
        identifier();
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetExtensionContext extends ParserRuleContext {
    public RuleTargetExtension1Context ruleTargetExtension1() {
      return getRuleContext(RuleTargetExtension1Context.class, 0);
    }

    public RuleTargetExtension2Context ruleTargetExtension2() {
      return getRuleContext(RuleTargetExtension2Context.class, 0);
    }

    public RuleTargetVariableContext ruleTargetVariable() {
      return getRuleContext(RuleTargetVariableContext.class, 0);
    }

    public RuleTargetExtensionContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_ruleTargetExtension;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor)
        return ((FhirMapJavaVisitor<? extends T>) visitor).visitRuleTargetExtension(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetExtensionContext ruleTargetExtension() throws RecognitionException {
    RuleTargetExtensionContext _localctx = new RuleTargetExtensionContext(_ctx, getState());
    enterRule(_localctx, 152, RULE_ruleTargetExtension);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(711);
        _errHandler.sync(this);
        switch (getInterpreter().adaptivePredict(_input, 62, _ctx)) {
          case 1: {
            setState(709);
            ruleTargetExtension1();
          }
          break;
          case 2: {
            setState(710);
            ruleTargetExtension2();
          }
          break;
        }
        setState(714);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la == AS) {
          {
            setState(713);
            ruleTargetVariable();
          }
        }

      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetExtension1Context extends ParserRuleContext {
    public TerminalNode EXTENSION() {
      return getToken(FhirMapJavaParser.EXTENSION, 0);
    }

    public RuleTargetContextContext ruleTargetContext() {
      return getRuleContext(RuleTargetContextContext.class, 0);
    }

    public RuleTargetVariableContext ruleTargetVariable() {
      return getRuleContext(RuleTargetVariableContext.class, 0);
    }

    public RuleTargetExtension1Context(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_ruleTargetExtension1;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor)
        return ((FhirMapJavaVisitor<? extends T>) visitor).visitRuleTargetExtension1(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetExtension1Context ruleTargetExtension1() throws RecognitionException {
    RuleTargetExtension1Context _localctx = new RuleTargetExtension1Context(_ctx, getState());
    enterRule(_localctx, 154, RULE_ruleTargetExtension1);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(717);
        _errHandler.sync(this);
        switch (getInterpreter().adaptivePredict(_input, 64, _ctx)) {
          case 1: {
            setState(716);
            ruleTargetContext();
          }
          break;
        }
        setState(719);
        match(EXTENSION);
        setState(720);
        match(T__3);
        setState(721);
        match(T__4);
        setState(723);
        _errHandler.sync(this);
        switch (getInterpreter().adaptivePredict(_input, 65, _ctx)) {
          case 1: {
            setState(722);
            ruleTargetVariable();
          }
          break;
        }
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetExtension2Context extends ParserRuleContext {
    public TerminalNode EXTENSION() {
      return getToken(FhirMapJavaParser.EXTENSION, 0);
    }

    public RuleTargetExtension2UriContext ruleTargetExtension2Uri() {
      return getRuleContext(RuleTargetExtension2UriContext.class, 0);
    }

    public RuleTargetExtension2TitleContext ruleTargetExtension2Title() {
      return getRuleContext(RuleTargetExtension2TitleContext.class, 0);
    }

    public RuleTargetExtension2ModeContext ruleTargetExtension2Mode() {
      return getRuleContext(RuleTargetExtension2ModeContext.class, 0);
    }

    public RuleTargetExtension2ParentContext ruleTargetExtension2Parent() {
      return getRuleContext(RuleTargetExtension2ParentContext.class, 0);
    }

    public RuleTargetExtension2Text1Context ruleTargetExtension2Text1() {
      return getRuleContext(RuleTargetExtension2Text1Context.class, 0);
    }

    public RuleTargetExtension2Text2Context ruleTargetExtension2Text2() {
      return getRuleContext(RuleTargetExtension2Text2Context.class, 0);
    }

    public RuleTargetExtension2MinContext ruleTargetExtension2Min() {
      return getRuleContext(RuleTargetExtension2MinContext.class, 0);
    }

    public RuleTargetExtension2MaxContext ruleTargetExtension2Max() {
      return getRuleContext(RuleTargetExtension2MaxContext.class, 0);
    }

    public RuleTargetExtension2TypeContext ruleTargetExtension2Type() {
      return getRuleContext(RuleTargetExtension2TypeContext.class, 0);
    }

    public RuleTargetContextContext ruleTargetContext() {
      return getRuleContext(RuleTargetContextContext.class, 0);
    }

    public RuleTargetVariableContext ruleTargetVariable() {
      return getRuleContext(RuleTargetVariableContext.class, 0);
    }

    public RuleTargetExtension2Context(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_ruleTargetExtension2;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor)
        return ((FhirMapJavaVisitor<? extends T>) visitor).visitRuleTargetExtension2(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetExtension2Context ruleTargetExtension2() throws RecognitionException {
    RuleTargetExtension2Context _localctx = new RuleTargetExtension2Context(_ctx, getState());
    enterRule(_localctx, 156, RULE_ruleTargetExtension2);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(726);
        _errHandler.sync(this);
        switch (getInterpreter().adaptivePredict(_input, 66, _ctx)) {
          case 1: {
            setState(725);
            ruleTargetContext();
          }
          break;
        }
        setState(728);
        match(EXTENSION);
        setState(729);
        match(T__3);
        setState(730);
        ruleTargetExtension2Uri();
        setState(731);
        match(T__5);
        setState(732);
        ruleTargetExtension2Title();
        setState(733);
        match(T__5);
        setState(734);
        ruleTargetExtension2Mode();
        setState(735);
        match(T__5);
        setState(736);
        ruleTargetExtension2Parent();
        setState(737);
        match(T__5);
        setState(738);
        ruleTargetExtension2Text1();
        setState(739);
        match(T__5);
        setState(740);
        ruleTargetExtension2Text2();
        setState(741);
        match(T__5);
        setState(742);
        ruleTargetExtension2Min();
        setState(743);
        match(T__5);
        setState(744);
        ruleTargetExtension2Max();
        setState(745);
        match(T__5);
        setState(746);
        ruleTargetExtension2Type();
        setState(747);
        match(T__4);
        setState(749);
        _errHandler.sync(this);
        switch (getInterpreter().adaptivePredict(_input, 67, _ctx)) {
          case 1: {
            setState(748);
            ruleTargetVariable();
          }
          break;
        }
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetExtension2UriContext extends ParserRuleContext {
    public QuotedUrlContext quotedUrl() {
      return getRuleContext(QuotedUrlContext.class, 0);
    }

    public RuleTargetExtension2UriContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_ruleTargetExtension2Uri;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor)
        return ((FhirMapJavaVisitor<? extends T>) visitor).visitRuleTargetExtension2Uri(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetExtension2UriContext ruleTargetExtension2Uri() throws RecognitionException {
    RuleTargetExtension2UriContext _localctx = new RuleTargetExtension2UriContext(_ctx, getState());
    enterRule(_localctx, 158, RULE_ruleTargetExtension2Uri);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(751);
        quotedUrl();
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetExtension2TitleContext extends ParserRuleContext {
    public QuotedStringContext quotedString() {
      return getRuleContext(QuotedStringContext.class, 0);
    }

    public RuleTargetExtension2TitleContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_ruleTargetExtension2Title;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor)
        return ((FhirMapJavaVisitor<? extends T>) visitor).visitRuleTargetExtension2Title(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetExtension2TitleContext ruleTargetExtension2Title() throws RecognitionException {
    RuleTargetExtension2TitleContext _localctx = new RuleTargetExtension2TitleContext(_ctx, getState());
    enterRule(_localctx, 160, RULE_ruleTargetExtension2Title);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(753);
        quotedString();
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetExtension2ModeContext extends ParserRuleContext {
    public QuotedStringContext quotedString() {
      return getRuleContext(QuotedStringContext.class, 0);
    }

    public RuleTargetExtension2ModeContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_ruleTargetExtension2Mode;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor)
        return ((FhirMapJavaVisitor<? extends T>) visitor).visitRuleTargetExtension2Mode(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetExtension2ModeContext ruleTargetExtension2Mode() throws RecognitionException {
    RuleTargetExtension2ModeContext _localctx = new RuleTargetExtension2ModeContext(_ctx, getState());
    enterRule(_localctx, 162, RULE_ruleTargetExtension2Mode);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(755);
        quotedString();
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetExtension2ParentContext extends ParserRuleContext {
    public QuotedStringContext quotedString() {
      return getRuleContext(QuotedStringContext.class, 0);
    }

    public RuleTargetExtension2ParentContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_ruleTargetExtension2Parent;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor)
        return ((FhirMapJavaVisitor<? extends T>) visitor).visitRuleTargetExtension2Parent(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetExtension2ParentContext ruleTargetExtension2Parent() throws RecognitionException {
    RuleTargetExtension2ParentContext _localctx = new RuleTargetExtension2ParentContext(_ctx, getState());
    enterRule(_localctx, 164, RULE_ruleTargetExtension2Parent);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(757);
        quotedString();
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetExtension2Text1Context extends ParserRuleContext {
    public QuotedStringContext quotedString() {
      return getRuleContext(QuotedStringContext.class, 0);
    }

    public RuleTargetExtension2Text1Context(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_ruleTargetExtension2Text1;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor)
        return ((FhirMapJavaVisitor<? extends T>) visitor).visitRuleTargetExtension2Text1(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetExtension2Text1Context ruleTargetExtension2Text1() throws RecognitionException {
    RuleTargetExtension2Text1Context _localctx = new RuleTargetExtension2Text1Context(_ctx, getState());
    enterRule(_localctx, 166, RULE_ruleTargetExtension2Text1);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(759);
        quotedString();
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetExtension2Text2Context extends ParserRuleContext {
    public QuotedStringContext quotedString() {
      return getRuleContext(QuotedStringContext.class, 0);
    }

    public RuleTargetExtension2Text2Context(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_ruleTargetExtension2Text2;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor)
        return ((FhirMapJavaVisitor<? extends T>) visitor).visitRuleTargetExtension2Text2(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetExtension2Text2Context ruleTargetExtension2Text2() throws RecognitionException {
    RuleTargetExtension2Text2Context _localctx = new RuleTargetExtension2Text2Context(_ctx, getState());
    enterRule(_localctx, 168, RULE_ruleTargetExtension2Text2);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(761);
        quotedString();
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetExtension2MinContext extends ParserRuleContext {
    public IntegerContext integer() {
      return getRuleContext(IntegerContext.class, 0);
    }

    public RuleTargetExtension2MinContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_ruleTargetExtension2Min;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor)
        return ((FhirMapJavaVisitor<? extends T>) visitor).visitRuleTargetExtension2Min(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetExtension2MinContext ruleTargetExtension2Min() throws RecognitionException {
    RuleTargetExtension2MinContext _localctx = new RuleTargetExtension2MinContext(_ctx, getState());
    enterRule(_localctx, 170, RULE_ruleTargetExtension2Min);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(763);
        integer();
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetExtension2MaxContext extends ParserRuleContext {
    public QuotedStringContext quotedString() {
      return getRuleContext(QuotedStringContext.class, 0);
    }

    public RuleTargetExtension2MaxContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_ruleTargetExtension2Max;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor)
        return ((FhirMapJavaVisitor<? extends T>) visitor).visitRuleTargetExtension2Max(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetExtension2MaxContext ruleTargetExtension2Max() throws RecognitionException {
    RuleTargetExtension2MaxContext _localctx = new RuleTargetExtension2MaxContext(_ctx, getState());
    enterRule(_localctx, 172, RULE_ruleTargetExtension2Max);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(765);
        quotedString();
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetExtension2TypeContext extends ParserRuleContext {
    public QuotedStringContext quotedString() {
      return getRuleContext(QuotedStringContext.class, 0);
    }

    public RuleTargetExtension2TypeContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_ruleTargetExtension2Type;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor)
        return ((FhirMapJavaVisitor<? extends T>) visitor).visitRuleTargetExtension2Type(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetExtension2TypeContext ruleTargetExtension2Type() throws RecognitionException {
    RuleTargetExtension2TypeContext _localctx = new RuleTargetExtension2TypeContext(_ctx, getState());
    enterRule(_localctx, 174, RULE_ruleTargetExtension2Type);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(767);
        quotedString();
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetEscapeContext extends ParserRuleContext {
    public TerminalNode ESCAPE() {
      return getToken(FhirMapJavaParser.ESCAPE, 0);
    }

    public RuleTargetEscapeVariableContext ruleTargetEscapeVariable() {
      return getRuleContext(RuleTargetEscapeVariableContext.class, 0);
    }

    public RuleTargetEscapeString1Context ruleTargetEscapeString1() {
      return getRuleContext(RuleTargetEscapeString1Context.class, 0);
    }

    public RuleTargetContextContext ruleTargetContext() {
      return getRuleContext(RuleTargetContextContext.class, 0);
    }

    public RuleTargetEscapeString2Context ruleTargetEscapeString2() {
      return getRuleContext(RuleTargetEscapeString2Context.class, 0);
    }

    public RuleTargetVariableContext ruleTargetVariable() {
      return getRuleContext(RuleTargetVariableContext.class, 0);
    }

    public RuleTargetEscapeContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_ruleTargetEscape;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor)
        return ((FhirMapJavaVisitor<? extends T>) visitor).visitRuleTargetEscape(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetEscapeContext ruleTargetEscape() throws RecognitionException {
    RuleTargetEscapeContext _localctx = new RuleTargetEscapeContext(_ctx, getState());
    enterRule(_localctx, 176, RULE_ruleTargetEscape);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(770);
        _errHandler.sync(this);
        switch (getInterpreter().adaptivePredict(_input, 68, _ctx)) {
          case 1: {
            setState(769);
            ruleTargetContext();
          }
          break;
        }
        setState(772);
        match(ESCAPE);
        setState(773);
        match(T__3);
        setState(774);
        ruleTargetEscapeVariable();
        setState(775);
        match(T__5);
        setState(776);
        ruleTargetEscapeString1();
        setState(779);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la == T__5) {
          {
            setState(777);
            match(T__5);
            setState(778);
            ruleTargetEscapeString2();
          }
        }

        setState(781);
        match(T__4);
        setState(783);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la == AS) {
          {
            setState(782);
            ruleTargetVariable();
          }
        }

      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetEscapeVariableContext extends ParserRuleContext {
    public IdentifierContext identifier() {
      return getRuleContext(IdentifierContext.class, 0);
    }

    public RuleTargetEscapeVariableContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_ruleTargetEscapeVariable;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor)
        return ((FhirMapJavaVisitor<? extends T>) visitor).visitRuleTargetEscapeVariable(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetEscapeVariableContext ruleTargetEscapeVariable() throws RecognitionException {
    RuleTargetEscapeVariableContext _localctx = new RuleTargetEscapeVariableContext(_ctx, getState());
    enterRule(_localctx, 178, RULE_ruleTargetEscapeVariable);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(785);
        identifier();
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetEscapeString1Context extends ParserRuleContext {
    public QuotedStringContext quotedString() {
      return getRuleContext(QuotedStringContext.class, 0);
    }

    public RuleTargetEscapeString1Context(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_ruleTargetEscapeString1;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor)
        return ((FhirMapJavaVisitor<? extends T>) visitor).visitRuleTargetEscapeString1(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetEscapeString1Context ruleTargetEscapeString1() throws RecognitionException {
    RuleTargetEscapeString1Context _localctx = new RuleTargetEscapeString1Context(_ctx, getState());
    enterRule(_localctx, 180, RULE_ruleTargetEscapeString1);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(787);
        quotedString();
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetEscapeString2Context extends ParserRuleContext {
    public QuotedStringContext quotedString() {
      return getRuleContext(QuotedStringContext.class, 0);
    }

    public RuleTargetEscapeString2Context(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_ruleTargetEscapeString2;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor)
        return ((FhirMapJavaVisitor<? extends T>) visitor).visitRuleTargetEscapeString2(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetEscapeString2Context ruleTargetEscapeString2() throws RecognitionException {
    RuleTargetEscapeString2Context _localctx = new RuleTargetEscapeString2Context(_ctx, getState());
    enterRule(_localctx, 182, RULE_ruleTargetEscapeString2);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(789);
        quotedString();
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetEvaluateContext extends ParserRuleContext {
    public TerminalNode EVALUATE() {
      return getToken(FhirMapJavaParser.EVALUATE, 0);
    }

    public RuleTargetEvaluateObjectContext ruleTargetEvaluateObject() {
      return getRuleContext(RuleTargetEvaluateObjectContext.class, 0);
    }

    public RuleTargetEvaluateObjectElementContext ruleTargetEvaluateObjectElement() {
      return getRuleContext(RuleTargetEvaluateObjectElementContext.class, 0);
    }

    public RuleTargetContextContext ruleTargetContext() {
      return getRuleContext(RuleTargetContextContext.class, 0);
    }

    public RuleTargetVariableContext ruleTargetVariable() {
      return getRuleContext(RuleTargetVariableContext.class, 0);
    }

    public RuleTargetEvaluateContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_ruleTargetEvaluate;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor)
        return ((FhirMapJavaVisitor<? extends T>) visitor).visitRuleTargetEvaluate(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetEvaluateContext ruleTargetEvaluate() throws RecognitionException {
    RuleTargetEvaluateContext _localctx = new RuleTargetEvaluateContext(_ctx, getState());
    enterRule(_localctx, 184, RULE_ruleTargetEvaluate);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(792);
        _errHandler.sync(this);
        switch (getInterpreter().adaptivePredict(_input, 71, _ctx)) {
          case 1: {
            setState(791);
            ruleTargetContext();
          }
          break;
        }
        setState(794);
        match(EVALUATE);
        setState(795);
        match(T__3);
        setState(796);
        ruleTargetEvaluateObject();
        setState(797);
        match(T__5);
        setState(798);
        ruleTargetEvaluateObjectElement();
        setState(799);
        match(T__4);
        setState(801);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la == AS) {
          {
            setState(800);
            ruleTargetVariable();
          }
        }

      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetEvaluateObjectContext extends ParserRuleContext {
    public IdentifierContext identifier() {
      return getRuleContext(IdentifierContext.class, 0);
    }

    public RuleTargetEvaluateObjectContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_ruleTargetEvaluateObject;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor)
        return ((FhirMapJavaVisitor<? extends T>) visitor).visitRuleTargetEvaluateObject(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetEvaluateObjectContext ruleTargetEvaluateObject() throws RecognitionException {
    RuleTargetEvaluateObjectContext _localctx = new RuleTargetEvaluateObjectContext(_ctx, getState());
    enterRule(_localctx, 186, RULE_ruleTargetEvaluateObject);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(803);
        identifier();
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetEvaluateObjectElementContext extends ParserRuleContext {
    public IdentifierContext identifier() {
      return getRuleContext(IdentifierContext.class, 0);
    }

    public RuleTargetEvaluateObjectElementContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_ruleTargetEvaluateObjectElement;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor)
        return ((FhirMapJavaVisitor<? extends T>) visitor).visitRuleTargetEvaluateObjectElement(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetEvaluateObjectElementContext ruleTargetEvaluateObjectElement() throws RecognitionException {
    RuleTargetEvaluateObjectElementContext _localctx = new RuleTargetEvaluateObjectElementContext(_ctx, getState());
    enterRule(_localctx, 188, RULE_ruleTargetEvaluateObjectElement);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(805);
        identifier();
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetIdContext extends ParserRuleContext {
    public TerminalNode ID() {
      return getToken(FhirMapJavaParser.ID, 0);
    }

    public RuleTargetIdSystemContext ruleTargetIdSystem() {
      return getRuleContext(RuleTargetIdSystemContext.class, 0);
    }

    public RuleTargetIdValueContext ruleTargetIdValue() {
      return getRuleContext(RuleTargetIdValueContext.class, 0);
    }

    public RuleTargetContextContext ruleTargetContext() {
      return getRuleContext(RuleTargetContextContext.class, 0);
    }

    public RuleTargetIdTypeContext ruleTargetIdType() {
      return getRuleContext(RuleTargetIdTypeContext.class, 0);
    }

    public RuleTargetVariableContext ruleTargetVariable() {
      return getRuleContext(RuleTargetVariableContext.class, 0);
    }

    public RuleTargetIdContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_ruleTargetId;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor)
        return ((FhirMapJavaVisitor<? extends T>) visitor).visitRuleTargetId(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetIdContext ruleTargetId() throws RecognitionException {
    RuleTargetIdContext _localctx = new RuleTargetIdContext(_ctx, getState());
    enterRule(_localctx, 190, RULE_ruleTargetId);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(808);
        _errHandler.sync(this);
        switch (getInterpreter().adaptivePredict(_input, 73, _ctx)) {
          case 1: {
            setState(807);
            ruleTargetContext();
          }
          break;
        }
        setState(810);
        match(ID);
        setState(811);
        match(T__3);
        setState(812);
        ruleTargetIdSystem();
        setState(813);
        match(T__5);
        setState(814);
        ruleTargetIdValue();
        setState(817);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la == T__5) {
          {
            setState(815);
            match(T__5);
            setState(816);
            ruleTargetIdType();
          }
        }

        setState(819);
        match(T__4);
        setState(821);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la == AS) {
          {
            setState(820);
            ruleTargetVariable();
          }
        }

      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetIdSystemContext extends ParserRuleContext {
    public QuotedUrlContext quotedUrl() {
      return getRuleContext(QuotedUrlContext.class, 0);
    }

    public RuleTargetIdSystemContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_ruleTargetIdSystem;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor)
        return ((FhirMapJavaVisitor<? extends T>) visitor).visitRuleTargetIdSystem(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetIdSystemContext ruleTargetIdSystem() throws RecognitionException {
    RuleTargetIdSystemContext _localctx = new RuleTargetIdSystemContext(_ctx, getState());
    enterRule(_localctx, 192, RULE_ruleTargetIdSystem);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(823);
        quotedUrl();
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetIdValueContext extends ParserRuleContext {
    public IdentifierContext identifier() {
      return getRuleContext(IdentifierContext.class, 0);
    }

    public RuleTargetIdValueContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_ruleTargetIdValue;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor)
        return ((FhirMapJavaVisitor<? extends T>) visitor).visitRuleTargetIdValue(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetIdValueContext ruleTargetIdValue() throws RecognitionException {
    RuleTargetIdValueContext _localctx = new RuleTargetIdValueContext(_ctx, getState());
    enterRule(_localctx, 194, RULE_ruleTargetIdValue);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(825);
        identifier();
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetIdTypeContext extends ParserRuleContext {
    public IdentifierContext identifier() {
      return getRuleContext(IdentifierContext.class, 0);
    }

    public RuleTargetIdTypeContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_ruleTargetIdType;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor)
        return ((FhirMapJavaVisitor<? extends T>) visitor).visitRuleTargetIdType(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetIdTypeContext ruleTargetIdType() throws RecognitionException {
    RuleTargetIdTypeContext _localctx = new RuleTargetIdTypeContext(_ctx, getState());
    enterRule(_localctx, 196, RULE_ruleTargetIdType);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(827);
        identifier();
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetPointerContext extends ParserRuleContext {
    public TerminalNode POINTER() {
      return getToken(FhirMapJavaParser.POINTER, 0);
    }

    public RuleTargetPointerResourceContext ruleTargetPointerResource() {
      return getRuleContext(RuleTargetPointerResourceContext.class, 0);
    }

    public RuleTargetContextContext ruleTargetContext() {
      return getRuleContext(RuleTargetContextContext.class, 0);
    }

    public RuleTargetVariableContext ruleTargetVariable() {
      return getRuleContext(RuleTargetVariableContext.class, 0);
    }

    public RuleTargetPointerContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_ruleTargetPointer;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor)
        return ((FhirMapJavaVisitor<? extends T>) visitor).visitRuleTargetPointer(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetPointerContext ruleTargetPointer() throws RecognitionException {
    RuleTargetPointerContext _localctx = new RuleTargetPointerContext(_ctx, getState());
    enterRule(_localctx, 198, RULE_ruleTargetPointer);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(830);
        _errHandler.sync(this);
        switch (getInterpreter().adaptivePredict(_input, 76, _ctx)) {
          case 1: {
            setState(829);
            ruleTargetContext();
          }
          break;
        }
        setState(832);
        match(POINTER);
        setState(833);
        match(T__3);
        setState(834);
        ruleTargetPointerResource();
        setState(835);
        match(T__4);
        setState(837);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la == AS) {
          {
            setState(836);
            ruleTargetVariable();
          }
        }

      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetPointerResourceContext extends ParserRuleContext {
    public IdentifierContext identifier() {
      return getRuleContext(IdentifierContext.class, 0);
    }

    public RuleTargetPointerResourceContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_ruleTargetPointerResource;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor)
        return ((FhirMapJavaVisitor<? extends T>) visitor).visitRuleTargetPointerResource(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetPointerResourceContext ruleTargetPointerResource() throws RecognitionException {
    RuleTargetPointerResourceContext _localctx = new RuleTargetPointerResourceContext(_ctx, getState());
    enterRule(_localctx, 200, RULE_ruleTargetPointerResource);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(839);
        identifier();
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetQtyContext extends ParserRuleContext {
    public RuleTargetQty1Context ruleTargetQty1() {
      return getRuleContext(RuleTargetQty1Context.class, 0);
    }

    public RuleTargetQty2Context ruleTargetQty2() {
      return getRuleContext(RuleTargetQty2Context.class, 0);
    }

    public RuleTargetQty3Context ruleTargetQty3() {
      return getRuleContext(RuleTargetQty3Context.class, 0);
    }

    public RuleTargetQtyContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_ruleTargetQty;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor)
        return ((FhirMapJavaVisitor<? extends T>) visitor).visitRuleTargetQty(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetQtyContext ruleTargetQty() throws RecognitionException {
    RuleTargetQtyContext _localctx = new RuleTargetQtyContext(_ctx, getState());
    enterRule(_localctx, 202, RULE_ruleTargetQty);
    try {
      setState(844);
      _errHandler.sync(this);
      switch (getInterpreter().adaptivePredict(_input, 78, _ctx)) {
        case 1:
          enterOuterAlt(_localctx, 1);
        {
          setState(841);
          ruleTargetQty1();
        }
        break;
        case 2:
          enterOuterAlt(_localctx, 2);
        {
          setState(842);
          ruleTargetQty2();
        }
        break;
        case 3:
          enterOuterAlt(_localctx, 3);
        {
          setState(843);
          ruleTargetQty3();
        }
        break;
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetQty1Context extends ParserRuleContext {
    public TerminalNode QTY() {
      return getToken(FhirMapJavaParser.QTY, 0);
    }

    public RuleTargetQty1TextContext ruleTargetQty1Text() {
      return getRuleContext(RuleTargetQty1TextContext.class, 0);
    }

    public RuleTargetContextContext ruleTargetContext() {
      return getRuleContext(RuleTargetContextContext.class, 0);
    }

    public RuleTargetVariableContext ruleTargetVariable() {
      return getRuleContext(RuleTargetVariableContext.class, 0);
    }

    public RuleTargetQty1Context(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_ruleTargetQty1;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor)
        return ((FhirMapJavaVisitor<? extends T>) visitor).visitRuleTargetQty1(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetQty1Context ruleTargetQty1() throws RecognitionException {
    RuleTargetQty1Context _localctx = new RuleTargetQty1Context(_ctx, getState());
    enterRule(_localctx, 204, RULE_ruleTargetQty1);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(847);
        _errHandler.sync(this);
        switch (getInterpreter().adaptivePredict(_input, 79, _ctx)) {
          case 1: {
            setState(846);
            ruleTargetContext();
          }
          break;
        }
        setState(849);
        match(QTY);
        setState(850);
        match(T__3);
        setState(851);
        ruleTargetQty1Text();
        setState(852);
        match(T__4);
        setState(854);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la == AS) {
          {
            setState(853);
            ruleTargetVariable();
          }
        }

      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetQty1TextContext extends ParserRuleContext {
    public QuotedStringContext quotedString() {
      return getRuleContext(QuotedStringContext.class, 0);
    }

    public RuleTargetQty1TextContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_ruleTargetQty1Text;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor)
        return ((FhirMapJavaVisitor<? extends T>) visitor).visitRuleTargetQty1Text(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetQty1TextContext ruleTargetQty1Text() throws RecognitionException {
    RuleTargetQty1TextContext _localctx = new RuleTargetQty1TextContext(_ctx, getState());
    enterRule(_localctx, 206, RULE_ruleTargetQty1Text);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(856);
        quotedString();
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetQty2Context extends ParserRuleContext {
    public TerminalNode QTY() {
      return getToken(FhirMapJavaParser.QTY, 0);
    }

    public RuleTargetQty2ValueContext ruleTargetQty2Value() {
      return getRuleContext(RuleTargetQty2ValueContext.class, 0);
    }

    public RuleTargetQty2UnitStringContext ruleTargetQty2UnitString() {
      return getRuleContext(RuleTargetQty2UnitStringContext.class, 0);
    }

    public RuleTargetQty2SystemContext ruleTargetQty2System() {
      return getRuleContext(RuleTargetQty2SystemContext.class, 0);
    }

    public RuleTargetContextContext ruleTargetContext() {
      return getRuleContext(RuleTargetContextContext.class, 0);
    }

    public RuleTargetVariableContext ruleTargetVariable() {
      return getRuleContext(RuleTargetVariableContext.class, 0);
    }

    public RuleTargetQty2Context(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_ruleTargetQty2;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor)
        return ((FhirMapJavaVisitor<? extends T>) visitor).visitRuleTargetQty2(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetQty2Context ruleTargetQty2() throws RecognitionException {
    RuleTargetQty2Context _localctx = new RuleTargetQty2Context(_ctx, getState());
    enterRule(_localctx, 208, RULE_ruleTargetQty2);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(859);
        _errHandler.sync(this);
        switch (getInterpreter().adaptivePredict(_input, 81, _ctx)) {
          case 1: {
            setState(858);
            ruleTargetContext();
          }
          break;
        }
        setState(861);
        match(QTY);
        setState(862);
        match(T__3);
        setState(863);
        ruleTargetQty2Value();
        setState(864);
        match(T__5);
        setState(865);
        ruleTargetQty2UnitString();
        setState(866);
        match(T__5);
        setState(867);
        ruleTargetQty2System();
        setState(868);
        match(T__4);
        setState(870);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la == AS) {
          {
            setState(869);
            ruleTargetVariable();
          }
        }

      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetQty2ValueContext extends ParserRuleContext {
    public IdentifierContext identifier() {
      return getRuleContext(IdentifierContext.class, 0);
    }

    public RuleTargetQty2ValueContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_ruleTargetQty2Value;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor)
        return ((FhirMapJavaVisitor<? extends T>) visitor).visitRuleTargetQty2Value(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetQty2ValueContext ruleTargetQty2Value() throws RecognitionException {
    RuleTargetQty2ValueContext _localctx = new RuleTargetQty2ValueContext(_ctx, getState());
    enterRule(_localctx, 210, RULE_ruleTargetQty2Value);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(872);
        identifier();
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetQty2UnitStringContext extends ParserRuleContext {
    public QuotedStringContext quotedString() {
      return getRuleContext(QuotedStringContext.class, 0);
    }

    public RuleTargetQty2UnitStringContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_ruleTargetQty2UnitString;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor)
        return ((FhirMapJavaVisitor<? extends T>) visitor).visitRuleTargetQty2UnitString(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetQty2UnitStringContext ruleTargetQty2UnitString() throws RecognitionException {
    RuleTargetQty2UnitStringContext _localctx = new RuleTargetQty2UnitStringContext(_ctx, getState());
    enterRule(_localctx, 212, RULE_ruleTargetQty2UnitString);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(874);
        quotedString();
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetQty2SystemContext extends ParserRuleContext {
    public QuotedUrlContext quotedUrl() {
      return getRuleContext(QuotedUrlContext.class, 0);
    }

    public RuleTargetQty2SystemContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_ruleTargetQty2System;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor)
        return ((FhirMapJavaVisitor<? extends T>) visitor).visitRuleTargetQty2System(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetQty2SystemContext ruleTargetQty2System() throws RecognitionException {
    RuleTargetQty2SystemContext _localctx = new RuleTargetQty2SystemContext(_ctx, getState());
    enterRule(_localctx, 214, RULE_ruleTargetQty2System);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(876);
        quotedUrl();
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetQty3Context extends ParserRuleContext {
    public TerminalNode QTY() {
      return getToken(FhirMapJavaParser.QTY, 0);
    }

    public RuleTargetQty3ValueContext ruleTargetQty3Value() {
      return getRuleContext(RuleTargetQty3ValueContext.class, 0);
    }

    public RuleTargetQty3UnitStringContext ruleTargetQty3UnitString() {
      return getRuleContext(RuleTargetQty3UnitStringContext.class, 0);
    }

    public RuleTargetQty3CodeVariableContext ruleTargetQty3CodeVariable() {
      return getRuleContext(RuleTargetQty3CodeVariableContext.class, 0);
    }

    public RuleTargetContextContext ruleTargetContext() {
      return getRuleContext(RuleTargetContextContext.class, 0);
    }

    public RuleTargetVariableContext ruleTargetVariable() {
      return getRuleContext(RuleTargetVariableContext.class, 0);
    }

    public RuleTargetQty3Context(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_ruleTargetQty3;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor)
        return ((FhirMapJavaVisitor<? extends T>) visitor).visitRuleTargetQty3(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetQty3Context ruleTargetQty3() throws RecognitionException {
    RuleTargetQty3Context _localctx = new RuleTargetQty3Context(_ctx, getState());
    enterRule(_localctx, 216, RULE_ruleTargetQty3);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(879);
        _errHandler.sync(this);
        switch (getInterpreter().adaptivePredict(_input, 83, _ctx)) {
          case 1: {
            setState(878);
            ruleTargetContext();
          }
          break;
        }
        setState(881);
        match(QTY);
        setState(882);
        match(T__3);
        setState(883);
        ruleTargetQty3Value();
        setState(884);
        match(T__5);
        setState(885);
        ruleTargetQty3UnitString();
        setState(886);
        match(T__5);
        setState(887);
        ruleTargetQty3CodeVariable();
        setState(888);
        match(T__4);
        setState(890);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la == AS) {
          {
            setState(889);
            ruleTargetVariable();
          }
        }

      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetQty3ValueContext extends ParserRuleContext {
    public IdentifierContext identifier() {
      return getRuleContext(IdentifierContext.class, 0);
    }

    public RuleTargetQty3ValueContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_ruleTargetQty3Value;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor)
        return ((FhirMapJavaVisitor<? extends T>) visitor).visitRuleTargetQty3Value(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetQty3ValueContext ruleTargetQty3Value() throws RecognitionException {
    RuleTargetQty3ValueContext _localctx = new RuleTargetQty3ValueContext(_ctx, getState());
    enterRule(_localctx, 218, RULE_ruleTargetQty3Value);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(892);
        identifier();
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetQty3UnitStringContext extends ParserRuleContext {
    public QuotedStringContext quotedString() {
      return getRuleContext(QuotedStringContext.class, 0);
    }

    public RuleTargetQty3UnitStringContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_ruleTargetQty3UnitString;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor)
        return ((FhirMapJavaVisitor<? extends T>) visitor).visitRuleTargetQty3UnitString(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetQty3UnitStringContext ruleTargetQty3UnitString() throws RecognitionException {
    RuleTargetQty3UnitStringContext _localctx = new RuleTargetQty3UnitStringContext(_ctx, getState());
    enterRule(_localctx, 220, RULE_ruleTargetQty3UnitString);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(894);
        quotedString();
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetQty3CodeVariableContext extends ParserRuleContext {
    public IdentifierContext identifier() {
      return getRuleContext(IdentifierContext.class, 0);
    }

    public RuleTargetQty3CodeVariableContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_ruleTargetQty3CodeVariable;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor)
        return ((FhirMapJavaVisitor<? extends T>) visitor).visitRuleTargetQty3CodeVariable(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetQty3CodeVariableContext ruleTargetQty3CodeVariable() throws RecognitionException {
    RuleTargetQty3CodeVariableContext _localctx = new RuleTargetQty3CodeVariableContext(_ctx, getState());
    enterRule(_localctx, 222, RULE_ruleTargetQty3CodeVariable);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(896);
        identifier();
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetReferenceContext extends ParserRuleContext {
    public TerminalNode REFERENCE() {
      return getToken(FhirMapJavaParser.REFERENCE, 0);
    }

    public RuleTargetReferenceSourceContext ruleTargetReferenceSource() {
      return getRuleContext(RuleTargetReferenceSourceContext.class, 0);
    }

    public RuleTargetContextContext ruleTargetContext() {
      return getRuleContext(RuleTargetContextContext.class, 0);
    }

    public RuleTargetVariableContext ruleTargetVariable() {
      return getRuleContext(RuleTargetVariableContext.class, 0);
    }

    public RuleTargetReferenceContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_ruleTargetReference;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor)
        return ((FhirMapJavaVisitor<? extends T>) visitor).visitRuleTargetReference(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetReferenceContext ruleTargetReference() throws RecognitionException {
    RuleTargetReferenceContext _localctx = new RuleTargetReferenceContext(_ctx, getState());
    enterRule(_localctx, 224, RULE_ruleTargetReference);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(899);
        _errHandler.sync(this);
        switch (getInterpreter().adaptivePredict(_input, 85, _ctx)) {
          case 1: {
            setState(898);
            ruleTargetContext();
          }
          break;
        }
        setState(901);
        match(REFERENCE);
        setState(902);
        match(T__3);
        setState(903);
        ruleTargetReferenceSource();
        setState(904);
        match(T__4);
        setState(906);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la == AS) {
          {
            setState(905);
            ruleTargetVariable();
          }
        }

      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetReferenceSourceContext extends ParserRuleContext {
    public IdentifierContext identifier() {
      return getRuleContext(IdentifierContext.class, 0);
    }

    public QuotedStringWQuotesContext quotedStringWQuotes() {
      return getRuleContext(QuotedStringWQuotesContext.class, 0);
    }

    public RuleTargetReferenceSourceContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_ruleTargetReferenceSource;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor)
        return ((FhirMapJavaVisitor<? extends T>) visitor).visitRuleTargetReferenceSource(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetReferenceSourceContext ruleTargetReferenceSource() throws RecognitionException {
    RuleTargetReferenceSourceContext _localctx = new RuleTargetReferenceSourceContext(_ctx, getState());
    enterRule(_localctx, 226, RULE_ruleTargetReferenceSource);
    try {
      setState(910);
      _errHandler.sync(this);
      switch (_input.LA(1)) {
        case SOURCE:
        case TARGET:
        case QUERIED:
        case PRODUCED:
        case APPEND:
        case CAST:
        case C:
        case CC:
        case CODE:
        case CP:
        case CODEABLECONCEPT:
        case CODING:
        case COPY:
        case CREATE:
        case DATEOP:
        case DEFAULT:
        case DISPLAY:
        case ESCAPE:
        case EXTENSION:
        case EVALUATE:
        case ID:
        case MAP:
        case POINTER:
        case QTY:
        case REFERENCE:
        case SYSTEM:
        case TRANSLATE:
        case TRUNCATE:
        case TYPES:
        case UUID:
        case IDENTIFIER:
          enterOuterAlt(_localctx, 1);
        {
          setState(908);
          identifier();
        }
        break;
        case QIDENTIFIER:
        case QSTRING:
          enterOuterAlt(_localctx, 2);
        {
          setState(909);
          quotedStringWQuotes();
        }
        break;
        default:
          throw new NoViableAltException(this);
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetTranslateContext extends ParserRuleContext {
    public TerminalNode TRANSLATE() {
      return getToken(FhirMapJavaParser.TRANSLATE, 0);
    }

    public RuleTargetTranslateSourceContext ruleTargetTranslateSource() {
      return getRuleContext(RuleTargetTranslateSourceContext.class, 0);
    }

    public RuleTargetTranslateMapContext ruleTargetTranslateMap() {
      return getRuleContext(RuleTargetTranslateMapContext.class, 0);
    }

    public RuleTargetTranslateOutputContext ruleTargetTranslateOutput() {
      return getRuleContext(RuleTargetTranslateOutputContext.class, 0);
    }

    public RuleTargetContextContext ruleTargetContext() {
      return getRuleContext(RuleTargetContextContext.class, 0);
    }

    public RuleTargetVariableContext ruleTargetVariable() {
      return getRuleContext(RuleTargetVariableContext.class, 0);
    }

    public RuleTargetTranslateContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_ruleTargetTranslate;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor)
        return ((FhirMapJavaVisitor<? extends T>) visitor).visitRuleTargetTranslate(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetTranslateContext ruleTargetTranslate() throws RecognitionException {
    RuleTargetTranslateContext _localctx = new RuleTargetTranslateContext(_ctx, getState());
    enterRule(_localctx, 228, RULE_ruleTargetTranslate);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(913);
        _errHandler.sync(this);
        switch (getInterpreter().adaptivePredict(_input, 88, _ctx)) {
          case 1: {
            setState(912);
            ruleTargetContext();
          }
          break;
        }
        setState(915);
        match(TRANSLATE);
        setState(916);
        match(T__3);
        setState(917);
        ruleTargetTranslateSource();
        setState(918);
        match(T__5);
        setState(919);
        ruleTargetTranslateMap();
        setState(920);
        match(T__5);
        setState(921);
        ruleTargetTranslateOutput();
        setState(922);
        match(T__4);
        setState(924);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la == AS) {
          {
            setState(923);
            ruleTargetVariable();
          }
        }

      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetTranslateSourceContext extends ParserRuleContext {
    public IdentifierContext identifier() {
      return getRuleContext(IdentifierContext.class, 0);
    }

    public RuleTargetTranslateSourceContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_ruleTargetTranslateSource;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor)
        return ((FhirMapJavaVisitor<? extends T>) visitor).visitRuleTargetTranslateSource(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetTranslateSourceContext ruleTargetTranslateSource() throws RecognitionException {
    RuleTargetTranslateSourceContext _localctx = new RuleTargetTranslateSourceContext(_ctx, getState());
    enterRule(_localctx, 230, RULE_ruleTargetTranslateSource);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(926);
        identifier();
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetTranslateMapContext extends ParserRuleContext {
    public QuotedUrlContext quotedUrl() {
      return getRuleContext(QuotedUrlContext.class, 0);
    }

    public RuleTargetTranslateMapContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_ruleTargetTranslateMap;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor)
        return ((FhirMapJavaVisitor<? extends T>) visitor).visitRuleTargetTranslateMap(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetTranslateMapContext ruleTargetTranslateMap() throws RecognitionException {
    RuleTargetTranslateMapContext _localctx = new RuleTargetTranslateMapContext(_ctx, getState());
    enterRule(_localctx, 232, RULE_ruleTargetTranslateMap);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(928);
        quotedUrl();
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetTranslateOutputContext extends ParserRuleContext {
    public RuleTargetTranslateOutputCodeContext ruleTargetTranslateOutputCode() {
      return getRuleContext(RuleTargetTranslateOutputCodeContext.class, 0);
    }

    public RuleTargetTranslateOutputSystemContext ruleTargetTranslateOutputSystem() {
      return getRuleContext(RuleTargetTranslateOutputSystemContext.class, 0);
    }

    public RuleTargetTranslateOutputDisplayContext ruleTargetTranslateOutputDisplay() {
      return getRuleContext(RuleTargetTranslateOutputDisplayContext.class, 0);
    }

    public RuleTargetTranslateOutputCodingContext ruleTargetTranslateOutputCoding() {
      return getRuleContext(RuleTargetTranslateOutputCodingContext.class, 0);
    }

    public RuleTargetTranslateOutputCodeableConceptContext ruleTargetTranslateOutputCodeableConcept() {
      return getRuleContext(RuleTargetTranslateOutputCodeableConceptContext.class, 0);
    }

    public RuleTargetTranslateOutputContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_ruleTargetTranslateOutput;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor)
        return ((FhirMapJavaVisitor<? extends T>) visitor).visitRuleTargetTranslateOutput(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetTranslateOutputContext ruleTargetTranslateOutput() throws RecognitionException {
    RuleTargetTranslateOutputContext _localctx = new RuleTargetTranslateOutputContext(_ctx, getState());
    enterRule(_localctx, 234, RULE_ruleTargetTranslateOutput);
    try {
      setState(935);
      _errHandler.sync(this);
      switch (_input.LA(1)) {
        case CODE:
          enterOuterAlt(_localctx, 1);
        {
          setState(930);
          ruleTargetTranslateOutputCode();
        }
        break;
        case SYSTEM:
          enterOuterAlt(_localctx, 2);
        {
          setState(931);
          ruleTargetTranslateOutputSystem();
        }
        break;
        case DISPLAY:
          enterOuterAlt(_localctx, 3);
        {
          setState(932);
          ruleTargetTranslateOutputDisplay();
        }
        break;
        case CODING:
          enterOuterAlt(_localctx, 4);
        {
          setState(933);
          ruleTargetTranslateOutputCoding();
        }
        break;
        case CODEABLECONCEPT:
          enterOuterAlt(_localctx, 5);
        {
          setState(934);
          ruleTargetTranslateOutputCodeableConcept();
        }
        break;
        default:
          throw new NoViableAltException(this);
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetTranslateOutputCodeContext extends ParserRuleContext {
    public TerminalNode CODE() {
      return getToken(FhirMapJavaParser.CODE, 0);
    }

    public RuleTargetTranslateOutputCodeContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_ruleTargetTranslateOutputCode;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor)
        return ((FhirMapJavaVisitor<? extends T>) visitor).visitRuleTargetTranslateOutputCode(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetTranslateOutputCodeContext ruleTargetTranslateOutputCode() throws RecognitionException {
    RuleTargetTranslateOutputCodeContext _localctx = new RuleTargetTranslateOutputCodeContext(_ctx, getState());
    enterRule(_localctx, 236, RULE_ruleTargetTranslateOutputCode);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(937);
        match(CODE);
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetTranslateOutputSystemContext extends ParserRuleContext {
    public TerminalNode SYSTEM() {
      return getToken(FhirMapJavaParser.SYSTEM, 0);
    }

    public RuleTargetTranslateOutputSystemContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_ruleTargetTranslateOutputSystem;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor)
        return ((FhirMapJavaVisitor<? extends T>) visitor).visitRuleTargetTranslateOutputSystem(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetTranslateOutputSystemContext ruleTargetTranslateOutputSystem() throws RecognitionException {
    RuleTargetTranslateOutputSystemContext _localctx = new RuleTargetTranslateOutputSystemContext(_ctx, getState());
    enterRule(_localctx, 238, RULE_ruleTargetTranslateOutputSystem);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(939);
        match(SYSTEM);
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetTranslateOutputDisplayContext extends ParserRuleContext {
    public TerminalNode DISPLAY() {
      return getToken(FhirMapJavaParser.DISPLAY, 0);
    }

    public RuleTargetTranslateOutputDisplayContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_ruleTargetTranslateOutputDisplay;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor)
        return ((FhirMapJavaVisitor<? extends T>) visitor).visitRuleTargetTranslateOutputDisplay(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetTranslateOutputDisplayContext ruleTargetTranslateOutputDisplay() throws RecognitionException {
    RuleTargetTranslateOutputDisplayContext _localctx = new RuleTargetTranslateOutputDisplayContext(_ctx, getState());
    enterRule(_localctx, 240, RULE_ruleTargetTranslateOutputDisplay);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(941);
        match(DISPLAY);
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetTranslateOutputCodingContext extends ParserRuleContext {
    public TerminalNode CODING() {
      return getToken(FhirMapJavaParser.CODING, 0);
    }

    public RuleTargetTranslateOutputCodingContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_ruleTargetTranslateOutputCoding;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor)
        return ((FhirMapJavaVisitor<? extends T>) visitor).visitRuleTargetTranslateOutputCoding(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetTranslateOutputCodingContext ruleTargetTranslateOutputCoding() throws RecognitionException {
    RuleTargetTranslateOutputCodingContext _localctx = new RuleTargetTranslateOutputCodingContext(_ctx, getState());
    enterRule(_localctx, 242, RULE_ruleTargetTranslateOutputCoding);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(943);
        match(CODING);
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetTranslateOutputCodeableConceptContext extends ParserRuleContext {
    public TerminalNode CODEABLECONCEPT() {
      return getToken(FhirMapJavaParser.CODEABLECONCEPT, 0);
    }

    public RuleTargetTranslateOutputCodeableConceptContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_ruleTargetTranslateOutputCodeableConcept;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor)
        return ((FhirMapJavaVisitor<? extends T>) visitor).visitRuleTargetTranslateOutputCodeableConcept(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetTranslateOutputCodeableConceptContext ruleTargetTranslateOutputCodeableConcept() throws RecognitionException {
    RuleTargetTranslateOutputCodeableConceptContext _localctx = new RuleTargetTranslateOutputCodeableConceptContext(_ctx, getState());
    enterRule(_localctx, 244, RULE_ruleTargetTranslateOutputCodeableConcept);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(945);
        match(CODEABLECONCEPT);
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetTruncateContext extends ParserRuleContext {
    public TerminalNode TRUNCATE() {
      return getToken(FhirMapJavaParser.TRUNCATE, 0);
    }

    public RuleTargetTruncateSourceContext ruleTargetTruncateSource() {
      return getRuleContext(RuleTargetTruncateSourceContext.class, 0);
    }

    public RuleTargetTruncateLengthContext ruleTargetTruncateLength() {
      return getRuleContext(RuleTargetTruncateLengthContext.class, 0);
    }

    public RuleTargetContextContext ruleTargetContext() {
      return getRuleContext(RuleTargetContextContext.class, 0);
    }

    public RuleTargetVariableContext ruleTargetVariable() {
      return getRuleContext(RuleTargetVariableContext.class, 0);
    }

    public RuleTargetTruncateContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_ruleTargetTruncate;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor)
        return ((FhirMapJavaVisitor<? extends T>) visitor).visitRuleTargetTruncate(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetTruncateContext ruleTargetTruncate() throws RecognitionException {
    RuleTargetTruncateContext _localctx = new RuleTargetTruncateContext(_ctx, getState());
    enterRule(_localctx, 246, RULE_ruleTargetTruncate);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(948);
        _errHandler.sync(this);
        switch (getInterpreter().adaptivePredict(_input, 91, _ctx)) {
          case 1: {
            setState(947);
            ruleTargetContext();
          }
          break;
        }
        setState(950);
        match(TRUNCATE);
        setState(951);
        match(T__3);
        setState(952);
        ruleTargetTruncateSource();
        setState(953);
        match(T__5);
        setState(954);
        ruleTargetTruncateLength();
        setState(955);
        match(T__4);
        setState(957);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la == AS) {
          {
            setState(956);
            ruleTargetVariable();
          }
        }

      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetTruncateSourceContext extends ParserRuleContext {
    public IdentifierContext identifier() {
      return getRuleContext(IdentifierContext.class, 0);
    }

    public QuotedStringWQuotesContext quotedStringWQuotes() {
      return getRuleContext(QuotedStringWQuotesContext.class, 0);
    }

    public RuleTargetTruncateSourceContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_ruleTargetTruncateSource;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor)
        return ((FhirMapJavaVisitor<? extends T>) visitor).visitRuleTargetTruncateSource(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetTruncateSourceContext ruleTargetTruncateSource() throws RecognitionException {
    RuleTargetTruncateSourceContext _localctx = new RuleTargetTruncateSourceContext(_ctx, getState());
    enterRule(_localctx, 248, RULE_ruleTargetTruncateSource);
    try {
      setState(961);
      _errHandler.sync(this);
      switch (_input.LA(1)) {
        case SOURCE:
        case TARGET:
        case QUERIED:
        case PRODUCED:
        case APPEND:
        case CAST:
        case C:
        case CC:
        case CODE:
        case CP:
        case CODEABLECONCEPT:
        case CODING:
        case COPY:
        case CREATE:
        case DATEOP:
        case DEFAULT:
        case DISPLAY:
        case ESCAPE:
        case EXTENSION:
        case EVALUATE:
        case ID:
        case MAP:
        case POINTER:
        case QTY:
        case REFERENCE:
        case SYSTEM:
        case TRANSLATE:
        case TRUNCATE:
        case TYPES:
        case UUID:
        case IDENTIFIER:
          enterOuterAlt(_localctx, 1);
        {
          setState(959);
          identifier();
        }
        break;
        case QIDENTIFIER:
        case QSTRING:
          enterOuterAlt(_localctx, 2);
        {
          setState(960);
          quotedStringWQuotes();
        }
        break;
        default:
          throw new NoViableAltException(this);
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetTruncateLengthContext extends ParserRuleContext {
    public IntegerContext integer() {
      return getRuleContext(IntegerContext.class, 0);
    }

    public RuleTargetTruncateLengthContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_ruleTargetTruncateLength;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor)
        return ((FhirMapJavaVisitor<? extends T>) visitor).visitRuleTargetTruncateLength(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetTruncateLengthContext ruleTargetTruncateLength() throws RecognitionException {
    RuleTargetTruncateLengthContext _localctx = new RuleTargetTruncateLengthContext(_ctx, getState());
    enterRule(_localctx, 250, RULE_ruleTargetTruncateLength);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(963);
        integer();
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetUuidContext extends ParserRuleContext {
    public TerminalNode UUID() {
      return getToken(FhirMapJavaParser.UUID, 0);
    }

    public RuleTargetContextContext ruleTargetContext() {
      return getRuleContext(RuleTargetContextContext.class, 0);
    }

    public RuleTargetVariableContext ruleTargetVariable() {
      return getRuleContext(RuleTargetVariableContext.class, 0);
    }

    public RuleTargetUuidContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_ruleTargetUuid;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor)
        return ((FhirMapJavaVisitor<? extends T>) visitor).visitRuleTargetUuid(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetUuidContext ruleTargetUuid() throws RecognitionException {
    RuleTargetUuidContext _localctx = new RuleTargetUuidContext(_ctx, getState());
    enterRule(_localctx, 252, RULE_ruleTargetUuid);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(966);
        _errHandler.sync(this);
        switch (getInterpreter().adaptivePredict(_input, 94, _ctx)) {
          case 1: {
            setState(965);
            ruleTargetContext();
          }
          break;
        }
        setState(968);
        match(UUID);
        setState(969);
        match(T__3);
        setState(970);
        match(T__4);
        setState(972);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la == AS) {
          {
            setState(971);
            ruleTargetVariable();
          }
        }

      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetVariableContext extends ParserRuleContext {
    public TerminalNode AS() {
      return getToken(FhirMapJavaParser.AS, 0);
    }

    public IdentifierContext identifier() {
      return getRuleContext(IdentifierContext.class, 0);
    }

    public RuleTargetVariableContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_ruleTargetVariable;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor)
        return ((FhirMapJavaVisitor<? extends T>) visitor).visitRuleTargetVariable(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetVariableContext ruleTargetVariable() throws RecognitionException {
    RuleTargetVariableContext _localctx = new RuleTargetVariableContext(_ctx, getState());
    enterRule(_localctx, 254, RULE_ruleTargetVariable);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(974);
        match(AS);
        setState(975);
        identifier();
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class FhirPathContext extends ParserRuleContext {
    public QuotedStringContext quotedString() {
      return getRuleContext(QuotedStringContext.class, 0);
    }

    public FhirPathContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_fhirPath;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor) return ((FhirMapJavaVisitor<? extends T>) visitor).visitFhirPath(this);
      else return visitor.visitChildren(this);
    }
  }

  public final FhirPathContext fhirPath() throws RecognitionException {
    FhirPathContext _localctx = new FhirPathContext(_ctx, getState());
    enterRule(_localctx, 256, RULE_fhirPath);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(977);
        quotedString();
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class IdentifierContext extends ParserRuleContext {
    public TerminalNode IDENTIFIER() {
      return getToken(FhirMapJavaParser.IDENTIFIER, 0);
    }

    public TerminalNode SOURCE() {
      return getToken(FhirMapJavaParser.SOURCE, 0);
    }

    public TerminalNode TARGET() {
      return getToken(FhirMapJavaParser.TARGET, 0);
    }

    public TerminalNode QUERIED() {
      return getToken(FhirMapJavaParser.QUERIED, 0);
    }

    public TerminalNode PRODUCED() {
      return getToken(FhirMapJavaParser.PRODUCED, 0);
    }

    public TerminalNode APPEND() {
      return getToken(FhirMapJavaParser.APPEND, 0);
    }

    public TerminalNode CAST() {
      return getToken(FhirMapJavaParser.CAST, 0);
    }

    public TerminalNode C() {
      return getToken(FhirMapJavaParser.C, 0);
    }

    public TerminalNode CC() {
      return getToken(FhirMapJavaParser.CC, 0);
    }

    public TerminalNode CP() {
      return getToken(FhirMapJavaParser.CP, 0);
    }

    public TerminalNode CODEABLECONCEPT() {
      return getToken(FhirMapJavaParser.CODEABLECONCEPT, 0);
    }

    public TerminalNode CODING() {
      return getToken(FhirMapJavaParser.CODING, 0);
    }

    public TerminalNode COPY() {
      return getToken(FhirMapJavaParser.COPY, 0);
    }

    public TerminalNode CODE() {
      return getToken(FhirMapJavaParser.CODE, 0);
    }

    public TerminalNode DISPLAY() {
      return getToken(FhirMapJavaParser.DISPLAY, 0);
    }

    public TerminalNode CREATE() {
      return getToken(FhirMapJavaParser.CREATE, 0);
    }

    public TerminalNode DATEOP() {
      return getToken(FhirMapJavaParser.DATEOP, 0);
    }

    public TerminalNode DEFAULT() {
      return getToken(FhirMapJavaParser.DEFAULT, 0);
    }

    public TerminalNode ESCAPE() {
      return getToken(FhirMapJavaParser.ESCAPE, 0);
    }

    public TerminalNode EXTENSION() {
      return getToken(FhirMapJavaParser.EXTENSION, 0);
    }

    public TerminalNode EVALUATE() {
      return getToken(FhirMapJavaParser.EVALUATE, 0);
    }

    public TerminalNode ID() {
      return getToken(FhirMapJavaParser.ID, 0);
    }

    public TerminalNode MAP() {
      return getToken(FhirMapJavaParser.MAP, 0);
    }

    public TerminalNode POINTER() {
      return getToken(FhirMapJavaParser.POINTER, 0);
    }

    public TerminalNode QTY() {
      return getToken(FhirMapJavaParser.QTY, 0);
    }

    public TerminalNode REFERENCE() {
      return getToken(FhirMapJavaParser.REFERENCE, 0);
    }

    public TerminalNode SYSTEM() {
      return getToken(FhirMapJavaParser.SYSTEM, 0);
    }

    public TerminalNode TRANSLATE() {
      return getToken(FhirMapJavaParser.TRANSLATE, 0);
    }

    public TerminalNode TRUNCATE() {
      return getToken(FhirMapJavaParser.TRUNCATE, 0);
    }

    public TerminalNode TYPES() {
      return getToken(FhirMapJavaParser.TYPES, 0);
    }

    public TerminalNode UUID() {
      return getToken(FhirMapJavaParser.UUID, 0);
    }

    public IdentifierContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_identifier;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor)
        return ((FhirMapJavaVisitor<? extends T>) visitor).visitIdentifier(this);
      else return visitor.visitChildren(this);
    }
  }

  public final IdentifierContext identifier() throws RecognitionException {
    IdentifierContext _localctx = new IdentifierContext(_ctx, getState());
    enterRule(_localctx, 258, RULE_identifier);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(979);
        _la = _input.LA(1);
        if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << SOURCE) | (1L << TARGET) | (1L << QUERIED) | (1L << PRODUCED) | (1L << APPEND) | (1L << CAST) | (1L << C) | (1L << CC) | (1L << CODE) | (1L << CP) | (1L << CODEABLECONCEPT) | (1L << CODING) | (1L << COPY) | (1L << CREATE) | (1L << DATEOP) | (1L << DEFAULT) | (1L << DISPLAY) | (1L << ESCAPE) | (1L << EXTENSION) | (1L << EVALUATE) | (1L << ID) | (1L << MAP) | (1L << POINTER) | (1L << QTY) | (1L << REFERENCE) | (1L << SYSTEM) | (1L << TRANSLATE) | (1L << TRUNCATE) | (1L << TYPES) | (1L << UUID) | (1L << IDENTIFIER))) != 0))) {
          _errHandler.recoverInline(this);
        } else {
          if (_input.LA(1) == Token.EOF) matchedEOF = true;
          _errHandler.reportMatch(this);
          consume();
        }
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class IntegerContext extends ParserRuleContext {
    public TerminalNode DIGITS() {
      return getToken(FhirMapJavaParser.DIGITS, 0);
    }

    public IntegerContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_integer;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor) return ((FhirMapJavaVisitor<? extends T>) visitor).visitInteger(this);
      else return visitor.visitChildren(this);
    }
  }

  public final IntegerContext integer() throws RecognitionException {
    IntegerContext _localctx = new IntegerContext(_ctx, getState());
    enterRule(_localctx, 260, RULE_integer);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(981);
        match(DIGITS);
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class QuotedIdentifierContext extends ParserRuleContext {
    public TerminalNode QIDENTIFIER() {
      return getToken(FhirMapJavaParser.QIDENTIFIER, 0);
    }

    public QuotedIdentifierContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_quotedIdentifier;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor)
        return ((FhirMapJavaVisitor<? extends T>) visitor).visitQuotedIdentifier(this);
      else return visitor.visitChildren(this);
    }
  }

  public final QuotedIdentifierContext quotedIdentifier() throws RecognitionException {
    QuotedIdentifierContext _localctx = new QuotedIdentifierContext(_ctx, getState());
    enterRule(_localctx, 262, RULE_quotedIdentifier);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(983);
        match(QIDENTIFIER);
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class QuotedStringWQuotesContext extends ParserRuleContext {
    public TerminalNode QSTRING() {
      return getToken(FhirMapJavaParser.QSTRING, 0);
    }

    public TerminalNode QIDENTIFIER() {
      return getToken(FhirMapJavaParser.QIDENTIFIER, 0);
    }

    public QuotedStringWQuotesContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_quotedStringWQuotes;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor)
        return ((FhirMapJavaVisitor<? extends T>) visitor).visitQuotedStringWQuotes(this);
      else return visitor.visitChildren(this);
    }
  }

  public final QuotedStringWQuotesContext quotedStringWQuotes() throws RecognitionException {
    QuotedStringWQuotesContext _localctx = new QuotedStringWQuotesContext(_ctx, getState());
    enterRule(_localctx, 264, RULE_quotedStringWQuotes);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(985);
        _la = _input.LA(1);
        if (!(_la == QIDENTIFIER || _la == QSTRING)) {
          _errHandler.recoverInline(this);
        } else {
          if (_input.LA(1) == Token.EOF) matchedEOF = true;
          _errHandler.reportMatch(this);
          consume();
        }
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class QuotedStringContext extends ParserRuleContext {
    public TerminalNode QSTRING() {
      return getToken(FhirMapJavaParser.QSTRING, 0);
    }

    public TerminalNode QIDENTIFIER() {
      return getToken(FhirMapJavaParser.QIDENTIFIER, 0);
    }

    public QuotedStringContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_quotedString;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor)
        return ((FhirMapJavaVisitor<? extends T>) visitor).visitQuotedString(this);
      else return visitor.visitChildren(this);
    }
  }

  public final QuotedStringContext quotedString() throws RecognitionException {
    QuotedStringContext _localctx = new QuotedStringContext(_ctx, getState());
    enterRule(_localctx, 266, RULE_quotedString);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(987);
        _la = _input.LA(1);
        if (!(_la == QIDENTIFIER || _la == QSTRING)) {
          _errHandler.recoverInline(this);
        } else {
          if (_input.LA(1) == Token.EOF) matchedEOF = true;
          _errHandler.reportMatch(this);
          consume();
        }
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class QuotedUrlContext extends ParserRuleContext {
    public QuotedStringContext quotedString() {
      return getRuleContext(QuotedStringContext.class, 0);
    }

    public QuotedUrlContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_quotedUrl;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor)
        return ((FhirMapJavaVisitor<? extends T>) visitor).visitQuotedUrl(this);
      else return visitor.visitChildren(this);
    }
  }

  public final QuotedUrlContext quotedUrl() throws RecognitionException {
    QuotedUrlContext _localctx = new QuotedUrlContext(_ctx, getState());
    enterRule(_localctx, 268, RULE_quotedUrl);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(989);
        quotedString();
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class StructureDefinitionContext extends ParserRuleContext {
    public QuotedUrlContext quotedUrl() {
      return getRuleContext(QuotedUrlContext.class, 0);
    }

    public StructureDefinitionContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_structureDefinition;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor)
        return ((FhirMapJavaVisitor<? extends T>) visitor).visitStructureDefinition(this);
      else return visitor.visitChildren(this);
    }
  }

  public final StructureDefinitionContext structureDefinition() throws RecognitionException {
    StructureDefinitionContext _localctx = new StructureDefinitionContext(_ctx, getState());
    enterRule(_localctx, 270, RULE_structureDefinition);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(991);
        quotedUrl();
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class StructureMapContext extends ParserRuleContext {
    public QuotedUrlContext quotedUrl() {
      return getRuleContext(QuotedUrlContext.class, 0);
    }

    public StructureMapContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    @Override
    public int getRuleIndex() {
      return RULE_structureMap;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof FhirMapJavaVisitor)
        return ((FhirMapJavaVisitor<? extends T>) visitor).visitStructureMap(this);
      else return visitor.visitChildren(this);
    }
  }

  public final StructureMapContext structureMap() throws RecognitionException {
    StructureMapContext _localctx = new StructureMapContext(_ctx, getState());
    enterRule(_localctx, 272, RULE_structureMap);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(993);
        quotedUrl();
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static final String _serializedATN =
    "\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3?\u03e6\4\2\t\2\4" +
      "\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t" +
      "\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22" +
      "\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31" +
      "\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!" +
      "\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4" +
      ",\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64\t" +
      "\64\4\65\t\65\4\66\t\66\4\67\t\67\48\t8\49\t9\4:\t:\4;\t;\4<\t<\4=\t=" +
      "\4>\t>\4?\t?\4@\t@\4A\tA\4B\tB\4C\tC\4D\tD\4E\tE\4F\tF\4G\tG\4H\tH\4I" +
      "\tI\4J\tJ\4K\tK\4L\tL\4M\tM\4N\tN\4O\tO\4P\tP\4Q\tQ\4R\tR\4S\tS\4T\tT" +
      "\4U\tU\4V\tV\4W\tW\4X\tX\4Y\tY\4Z\tZ\4[\t[\4\\\t\\\4]\t]\4^\t^\4_\t_\4" +
      "`\t`\4a\ta\4b\tb\4c\tc\4d\td\4e\te\4f\tf\4g\tg\4h\th\4i\ti\4j\tj\4k\t" +
      "k\4l\tl\4m\tm\4n\tn\4o\to\4p\tp\4q\tq\4r\tr\4s\ts\4t\tt\4u\tu\4v\tv\4" +
      "w\tw\4x\tx\4y\ty\4z\tz\4{\t{\4|\t|\4}\t}\4~\t~\4\177\t\177\4\u0080\t\u0080" +
      "\4\u0081\t\u0081\4\u0082\t\u0082\4\u0083\t\u0083\4\u0084\t\u0084\4\u0085" +
      "\t\u0085\4\u0086\t\u0086\4\u0087\t\u0087\4\u0088\t\u0088\4\u0089\t\u0089" +
      "\4\u008a\t\u008a\3\2\3\2\6\2\u0117\n\2\r\2\16\2\u0118\3\2\7\2\u011c\n" +
      "\2\f\2\16\2\u011f\13\2\3\2\3\2\3\3\3\3\3\3\3\3\3\3\3\4\3\4\3\4\3\4\3\4" +
      "\5\4\u012d\n\4\3\5\3\5\3\5\3\5\5\5\u0133\n\5\3\6\3\6\3\7\3\7\3\b\3\b\3" +
      "\t\3\t\3\n\3\n\3\n\5\n\u0140\n\n\3\13\3\13\7\13\u0144\n\13\f\13\16\13" +
      "\u0147\13\13\3\13\7\13\u014a\n\13\f\13\16\13\u014d\13\13\3\13\3\13\3\f" +
      "\3\f\5\f\u0153\n\f\3\f\3\f\5\f\u0157\n\f\3\r\3\r\3\r\3\16\3\16\3\17\3" +
      "\17\3\17\3\20\3\20\5\20\u0163\n\20\3\21\3\21\3\22\3\22\3\23\3\23\3\23" +
      "\3\23\3\23\5\23\u016e\n\23\3\23\5\23\u0171\n\23\3\24\3\24\3\25\3\25\3" +
      "\26\3\26\3\26\3\27\3\27\5\27\u017c\n\27\3\30\3\30\3\31\3\31\3\32\3\32" +
      "\5\32\u0184\n\32\3\33\3\33\3\33\5\33\u0189\n\33\3\33\3\33\3\34\3\34\3" +
      "\34\7\34\u0190\n\34\f\34\16\34\u0193\13\34\3\35\3\35\3\35\3\35\3\35\5" +
      "\35\u019a\n\35\3\36\3\36\3\36\7\36\u019f\n\36\f\36\16\36\u01a2\13\36\3" +
      "\37\3\37\3\37\7\37\u01a7\n\37\f\37\16\37\u01aa\13\37\3 \3 \5 \u01ae\n" +
      " \3 \5 \u01b1\n \3 \5 \u01b4\n \3 \5 \u01b7\n \3 \5 \u01ba\n \3 \5 \u01bd" +
      "\n \3!\3!\3!\7!\u01c2\n!\f!\16!\u01c5\13!\3\"\3\"\5\"\u01c9\n\"\3#\3#" +
      "\3#\3#\3#\3#\5#\u01d1\n#\3$\3$\3$\3%\3%\3&\3&\3&\3\'\3\'\3\'\3(\3(\3(" +
      "\3)\3)\3)\5)\u01e4\n)\3*\3*\3*\7*\u01e9\n*\f*\16*\u01ec\13*\3+\3+\3+\3" +
      "+\3+\3,\7,\u01f4\n,\f,\16,\u01f7\13,\3-\3-\3-\3-\3-\3-\3-\3-\3-\3-\3-" +
      "\3-\3-\3-\3-\3-\3-\3-\3-\3-\5-\u020d\n-\3.\3.\3.\3/\3/\3/\3\60\3\60\3" +
      "\60\5\60\u0218\n\60\3\61\3\61\5\61\u021c\n\61\3\62\5\62\u021f\n\62\3\62" +
      "\3\62\3\62\3\62\3\62\5\62\u0226\n\62\3\63\3\63\3\63\7\63\u022b\n\63\f" +
      "\63\16\63\u022e\13\63\3\64\3\64\5\64\u0232\n\64\3\65\5\65\u0235\n\65\3" +
      "\65\3\65\3\65\3\65\3\65\3\65\3\65\5\65\u023e\n\65\3\65\3\65\5\65\u0242" +
      "\n\65\3\66\3\66\3\67\3\67\5\67\u0248\n\67\38\38\39\59\u024d\n9\39\39\3" +
      "9\39\39\59\u0254\n9\39\39\59\u0258\n9\3:\3:\3;\3;\3<\3<\5<\u0260\n<\3" +
      "<\5<\u0263\n<\3=\5=\u0266\n=\3=\3=\3=\3=\3=\5=\u026d\n=\3>\3>\3?\5?\u0272" +
      "\n?\3?\3?\3?\3?\3?\3?\3?\5?\u027b\n?\3?\3?\5?\u027f\n?\3@\3@\3A\3A\5A" +
      "\u0285\nA\3B\3B\3C\5C\u028a\nC\3C\3C\3C\3C\3C\5C\u0291\nC\3C\3C\3C\5C" +
      "\u0296\nC\3D\3D\3E\3E\3F\3F\3F\3F\3F\3F\5F\u02a2\nF\3G\3G\3H\5H\u02a7" +
      "\nH\3H\3H\3H\3H\3H\5H\u02ae\nH\3I\3I\3J\5J\u02b3\nJ\3J\3J\3J\3J\3J\3J" +
      "\3J\5J\u02bc\nJ\3J\3J\5J\u02c0\nJ\3K\3K\3L\3L\3M\3M\3N\3N\5N\u02ca\nN" +
      "\3N\5N\u02cd\nN\3O\5O\u02d0\nO\3O\3O\3O\3O\5O\u02d6\nO\3P\5P\u02d9\nP" +
      "\3P\3P\3P\3P\3P\3P\3P\3P\3P\3P\3P\3P\3P\3P\3P\3P\3P\3P\3P\3P\3P\5P\u02f0" +
      "\nP\3Q\3Q\3R\3R\3S\3S\3T\3T\3U\3U\3V\3V\3W\3W\3X\3X\3Y\3Y\3Z\5Z\u0305" +
      "\nZ\3Z\3Z\3Z\3Z\3Z\3Z\3Z\5Z\u030e\nZ\3Z\3Z\5Z\u0312\nZ\3[\3[\3\\\3\\\3" +
      "]\3]\3^\5^\u031b\n^\3^\3^\3^\3^\3^\3^\3^\5^\u0324\n^\3_\3_\3`\3`\3a\5" +
      "a\u032b\na\3a\3a\3a\3a\3a\3a\3a\5a\u0334\na\3a\3a\5a\u0338\na\3b\3b\3" +
      "c\3c\3d\3d\3e\5e\u0341\ne\3e\3e\3e\3e\3e\5e\u0348\ne\3f\3f\3g\3g\3g\5" +
      "g\u034f\ng\3h\5h\u0352\nh\3h\3h\3h\3h\3h\5h\u0359\nh\3i\3i\3j\5j\u035e" +
      "\nj\3j\3j\3j\3j\3j\3j\3j\3j\3j\5j\u0369\nj\3k\3k\3l\3l\3m\3m\3n\5n\u0372" +
      "\nn\3n\3n\3n\3n\3n\3n\3n\3n\3n\5n\u037d\nn\3o\3o\3p\3p\3q\3q\3r\5r\u0386" +
      "\nr\3r\3r\3r\3r\3r\5r\u038d\nr\3s\3s\5s\u0391\ns\3t\5t\u0394\nt\3t\3t" +
      "\3t\3t\3t\3t\3t\3t\3t\5t\u039f\nt\3u\3u\3v\3v\3w\3w\3w\3w\3w\5w\u03aa" +
      "\nw\3x\3x\3y\3y\3z\3z\3{\3{\3|\3|\3}\5}\u03b7\n}\3}\3}\3}\3}\3}\3}\3}" +
      "\5}\u03c0\n}\3~\3~\5~\u03c4\n~\3\177\3\177\3\u0080\5\u0080\u03c9\n\u0080" +
      "\3\u0080\3\u0080\3\u0080\3\u0080\5\u0080\u03cf\n\u0080\3\u0081\3\u0081" +
      "\3\u0081\3\u0082\3\u0082\3\u0083\3\u0083\3\u0084\3\u0084\3\u0085\3\u0085" +
      "\3\u0086\3\u0086\3\u0087\3\u0087\3\u0088\3\u0088\3\u0089\3\u0089\3\u008a" +
      "\3\u008a\3\u008a\2\2\u008b\2\4\6\b\n\f\16\20\22\24\26\30\32\34\36 \"$" +
      "&(*,.\60\62\64\668:<>@BDFHJLNPRTVXZ\\^`bdfhjlnprtvxz|~\u0080\u0082\u0084" +
      "\u0086\u0088\u008a\u008c\u008e\u0090\u0092\u0094\u0096\u0098\u009a\u009c" +
      "\u009e\u00a0\u00a2\u00a4\u00a6\u00a8\u00aa\u00ac\u00ae\u00b0\u00b2\u00b4" +
      "\u00b6\u00b8\u00ba\u00bc\u00be\u00c0\u00c2\u00c4\u00c6\u00c8\u00ca\u00cc" +
      "\u00ce\u00d0\u00d2\u00d4\u00d6\u00d8\u00da\u00dc\u00de\u00e0\u00e2\u00e4" +
      "\u00e6\u00e8\u00ea\u00ec\u00ee\u00f0\u00f2\u00f4\u00f6\u00f8\u00fa\u00fc" +
      "\u00fe\u0100\u0102\u0104\u0106\u0108\u010a\u010c\u010e\u0110\u0112\2\4" +
      "\f\2\20\23\26\26\30\34\36$&(,,\60\64\668::==\3\2\24\25\2\u03d4\2\u0114" +
      "\3\2\2\2\4\u0122\3\2\2\2\6\u0127\3\2\2\2\b\u0132\3\2\2\2\n\u0134\3\2\2" +
      "\2\f\u0136\3\2\2\2\16\u0138\3\2\2\2\20\u013a\3\2\2\2\22\u013c\3\2\2\2" +
      "\24\u0141\3\2\2\2\26\u0150\3\2\2\2\30\u0158\3\2\2\2\32\u015b\3\2\2\2\34" +
      "\u015d\3\2\2\2\36\u0162\3\2\2\2 \u0164\3\2\2\2\"\u0166\3\2\2\2$\u0168" +
      "\3\2\2\2&\u0172\3\2\2\2(\u0174\3\2\2\2*\u0176\3\2\2\2,\u017b\3\2\2\2." +
      "\u017d\3\2\2\2\60\u017f\3\2\2\2\62\u0183\3\2\2\2\64\u0185\3\2\2\2\66\u018c" +
      "\3\2\2\28\u0194\3\2\2\2:\u019b\3\2\2\2<\u01a3\3\2\2\2>\u01ab\3\2\2\2@" +
      "\u01be\3\2\2\2B\u01c8\3\2\2\2D\u01ca\3\2\2\2F\u01d2\3\2\2\2H\u01d5\3\2" +
      "\2\2J\u01d7\3\2\2\2L\u01da\3\2\2\2N\u01dd\3\2\2\2P\u01e0\3\2\2\2R\u01e5" +
      "\3\2\2\2T\u01ed\3\2\2\2V\u01f5\3\2\2\2X\u020c\3\2\2\2Z\u020e\3\2\2\2\\" +
      "\u0211\3\2\2\2^\u0214\3\2\2\2`\u021b\3\2\2\2b\u021e\3\2\2\2d\u0227\3\2" +
      "\2\2f\u0231\3\2\2\2h\u0234\3\2\2\2j\u0243\3\2\2\2l\u0247\3\2\2\2n\u0249" +
      "\3\2\2\2p\u024c\3\2\2\2r\u0259\3\2\2\2t\u025b\3\2\2\2v\u025f\3\2\2\2x" +
      "\u0265\3\2\2\2z\u026e\3\2\2\2|\u0271\3\2\2\2~\u0280\3\2\2\2\u0080\u0284" +
      "\3\2\2\2\u0082\u0286\3\2\2\2\u0084\u0289\3\2\2\2\u0086\u0297\3\2\2\2\u0088" +
      "\u0299\3\2\2\2\u008a\u029b\3\2\2\2\u008c\u02a3\3\2\2\2\u008e\u02a6\3\2" +
      "\2\2\u0090\u02af\3\2\2\2\u0092\u02b2\3\2\2\2\u0094\u02c1\3\2\2\2\u0096" +
      "\u02c3\3\2\2\2\u0098\u02c5\3\2\2\2\u009a\u02c9\3\2\2\2\u009c\u02cf\3\2" +
      "\2\2\u009e\u02d8\3\2\2\2\u00a0\u02f1\3\2\2\2\u00a2\u02f3\3\2\2\2\u00a4" +
      "\u02f5\3\2\2\2\u00a6\u02f7\3\2\2\2\u00a8\u02f9\3\2\2\2\u00aa\u02fb\3\2" +
      "\2\2\u00ac\u02fd\3\2\2\2\u00ae\u02ff\3\2\2\2\u00b0\u0301\3\2\2\2\u00b2" +
      "\u0304\3\2\2\2\u00b4\u0313\3\2\2\2\u00b6\u0315\3\2\2\2\u00b8\u0317\3\2" +
      "\2\2\u00ba\u031a\3\2\2\2\u00bc\u0325\3\2\2\2\u00be\u0327\3\2\2\2\u00c0" +
      "\u032a\3\2\2\2\u00c2\u0339\3\2\2\2\u00c4\u033b\3\2\2\2\u00c6\u033d\3\2" +
      "\2\2\u00c8\u0340\3\2\2\2\u00ca\u0349\3\2\2\2\u00cc\u034e\3\2\2\2\u00ce" +
      "\u0351\3\2\2\2\u00d0\u035a\3\2\2\2\u00d2\u035d\3\2\2\2\u00d4\u036a\3\2" +
      "\2\2\u00d6\u036c\3\2\2\2\u00d8\u036e\3\2\2\2\u00da\u0371\3\2\2\2\u00dc" +
      "\u037e\3\2\2\2\u00de\u0380\3\2\2\2\u00e0\u0382\3\2\2\2\u00e2\u0385\3\2" +
      "\2\2\u00e4\u0390\3\2\2\2\u00e6\u0393\3\2\2\2\u00e8\u03a0\3\2\2\2\u00ea" +
      "\u03a2\3\2\2\2\u00ec\u03a9\3\2\2\2\u00ee\u03ab\3\2\2\2\u00f0\u03ad\3\2" +
      "\2\2\u00f2\u03af\3\2\2\2\u00f4\u03b1\3\2\2\2\u00f6\u03b3\3\2\2\2\u00f8" +
      "\u03b6\3\2\2\2\u00fa\u03c3\3\2\2\2\u00fc\u03c5\3\2\2\2\u00fe\u03c8\3\2" +
      "\2\2\u0100\u03d0\3\2\2\2\u0102\u03d3\3\2\2\2\u0104\u03d5\3\2\2\2\u0106" +
      "\u03d7\3\2\2\2\u0108\u03d9\3\2\2\2\u010a\u03db\3\2\2\2\u010c\u03dd\3\2" +
      "\2\2\u010e\u03df\3\2\2\2\u0110\u03e1\3\2\2\2\u0112\u03e3\3\2\2\2\u0114" +
      "\u0116\5\4\3\2\u0115\u0117\5\6\4\2\u0116\u0115\3\2\2\2\u0117\u0118\3\2" +
      "\2\2\u0118\u0116\3\2\2\2\u0118\u0119\3\2\2\2\u0119\u011d\3\2\2\2\u011a" +
      "\u011c\5\22\n\2\u011b\u011a\3\2\2\2\u011c\u011f\3\2\2\2\u011d\u011b\3" +
      "\2\2\2\u011d\u011e\3\2\2\2\u011e\u0120\3\2\2\2\u011f\u011d\3\2\2\2\u0120" +
      "\u0121\5\24\13\2\u0121\3\3\2\2\2\u0122\u0123\7\60\2\2\u0123\u0124\5\u0112" +
      "\u008a\2\u0124\u0125\7\3\2\2\u0125\u0126\5\u010c\u0087\2\u0126\5\3\2\2" +
      "\2\u0127\u0128\7;\2\2\u0128\u0129\5\u0110\u0089\2\u0129\u012a\7\27\2\2" +
      "\u012a\u012c\5\b\5\2\u012b\u012d\7\4\2\2\u012c\u012b\3\2\2\2\u012c\u012d" +
      "\3\2\2\2\u012d\7\3\2\2\2\u012e\u0133\5\n\6\2\u012f\u0133\5\f\7\2\u0130" +
      "\u0133\5\16\b\2\u0131\u0133\5\20\t\2\u0132\u012e\3\2\2\2\u0132\u012f\3" +
      "\2\2\2\u0132\u0130\3\2\2\2\u0132\u0131\3\2\2\2\u0133\t\3\2\2\2\u0134\u0135" +
      "\7\20\2\2\u0135\13\3\2\2\2\u0136\u0137\7\21\2\2\u0137\r\3\2\2\2\u0138" +
      "\u0139\7\22\2\2\u0139\17\3\2\2\2\u013a\u013b\7\23\2\2\u013b\21\3\2\2\2" +
      "\u013c\u013d\7-\2\2\u013d\u013f\5\u0112\u008a\2\u013e\u0140\7\4\2\2\u013f" +
      "\u013e\3\2\2\2\u013f\u0140\3\2\2\2\u0140\23\3\2\2\2\u0141\u0145\5\26\f" +
      "\2\u0142\u0144\5$\23\2\u0143\u0142\3\2\2\2\u0144\u0147\3\2\2\2\u0145\u0143" +
      "\3\2\2\2\u0145\u0146\3\2\2\2\u0146\u014b\3\2\2\2\u0147\u0145\3\2\2\2\u0148" +
      "\u014a\5\62\32\2\u0149\u0148\3\2\2\2\u014a\u014d\3\2\2\2\u014b\u0149\3" +
      "\2\2\2\u014b\u014c\3\2\2\2\u014c\u014e\3\2\2\2\u014d\u014b\3\2\2\2\u014e" +
      "\u014f\5\32\16\2\u014f\25\3\2\2\2\u0150\u0152\7+\2\2\u0151\u0153\5\34" +
      "\17\2\u0152\u0151\3\2\2\2\u0152\u0153\3\2\2\2\u0153\u0154\3\2\2\2\u0154" +
      "\u0156\5\u0104\u0083\2\u0155\u0157\5\30\r\2\u0156\u0155\3\2\2\2\u0156" +
      "\u0157\3\2\2\2\u0157\27\3\2\2\2\u0158\u0159\7)\2\2\u0159\u015a\5\u0104" +
      "\u0083\2\u015a\31\3\2\2\2\u015b\u015c\7%\2\2\u015c\33\3\2\2\2\u015d\u015e" +
      "\7*\2\2\u015e\u015f\5\36\20\2\u015f\35\3\2\2\2\u0160\u0163\5 \21\2\u0161" +
      "\u0163\5\"\22\2\u0162\u0160\3\2\2\2\u0162\u0161\3\2\2\2\u0163\37\3\2\2" +
      "\2\u0164\u0165\78\2\2\u0165!\3\2\2\2\u0166\u0167\79\2\2\u0167#\3\2\2\2" +
      "\u0168\u0169\7.\2\2\u0169\u016a\5&\24\2\u016a\u016b\7\5\2\2\u016b\u016d" +
      "\5(\25\2\u016c\u016e\5*\26\2\u016d\u016c\3\2\2\2\u016d\u016e\3\2\2\2\u016e" +
      "\u0170\3\2\2\2\u016f\u0171\7\4\2\2\u0170\u016f\3\2\2\2\u0170\u0171\3\2" +
      "\2\2\u0171%\3\2\2\2\u0172\u0173\5\u0104\u0083\2\u0173\'\3\2\2\2\u0174" +
      "\u0175\5\u0104\u0083\2\u0175)\3\2\2\2\u0176\u0177\7\27\2\2\u0177\u0178" +
      "\5,\27\2\u0178+\3\2\2\2\u0179\u017c\5.\30\2\u017a\u017c\5\60\31\2\u017b" +
      "\u0179\3\2\2\2\u017b\u017a\3\2\2\2\u017c-\3\2\2\2\u017d\u017e\7\20\2\2" +
      "\u017e/\3\2\2\2\u017f\u0180\7\21\2\2\u0180\61\3\2\2\2\u0181\u0184\5\64" +
      "\33\2\u0182\u0184\58\35\2\u0183\u0181\3\2\2\2\u0183\u0182\3\2\2\2\u0184" +
      "\63\3\2\2\2\u0185\u0186\5\u0104\u0083\2\u0186\u0188\7\6\2\2\u0187\u0189" +
      "\5\66\34\2\u0188\u0187\3\2\2\2\u0188\u0189\3\2\2\2\u0189\u018a\3\2\2\2" +
      "\u018a\u018b\7\7\2\2\u018b\65\3\2\2\2\u018c\u0191\5\u0104\u0083\2\u018d" +
      "\u018e\7\b\2\2\u018e\u0190\5\u0104\u0083\2\u018f\u018d\3\2\2\2\u0190\u0193" +
      "\3\2\2\2\u0191\u018f\3\2\2\2\u0191\u0192\3\2\2\2\u0192\67\3\2\2\2\u0193" +
      "\u0191\3\2\2\2\u0194\u0195\5:\36\2\u0195\u0196\7\5\2\2\u0196\u0197\7*" +
      "\2\2\u0197\u0199\5<\37\2\u0198\u019a\5P)\2\u0199\u0198\3\2\2\2\u0199\u019a" +
      "\3\2\2\2\u019a9\3\2\2\2\u019b\u01a0\5\u0104\u0083\2\u019c\u019d\7\t\2" +
      "\2\u019d\u019f\5\u0104\u0083\2\u019e\u019c\3\2\2\2\u019f\u01a2\3\2\2\2" +
      "\u01a0\u019e\3\2\2\2\u01a0\u01a1\3\2\2\2\u01a1;\3\2\2\2\u01a2\u01a0\3" +
      "\2\2\2\u01a3\u01a8\5> \2\u01a4\u01a5\7\b\2\2\u01a5\u01a7\5> \2\u01a6\u01a4" +
      "\3\2\2\2\u01a7\u01aa\3\2\2\2\u01a8\u01a6\3\2\2\2\u01a8\u01a9\3\2\2\2\u01a9" +
      "=\3\2\2\2\u01aa\u01a8\3\2\2\2\u01ab\u01ad\5@!\2\u01ac\u01ae\5D#\2\u01ad" +
      "\u01ac\3\2\2\2\u01ad\u01ae\3\2\2\2\u01ae\u01b0\3\2\2\2\u01af\u01b1\5F" +
      "$\2\u01b0\u01af\3\2\2\2\u01b0\u01b1\3\2\2\2\u01b1\u01b3\3\2\2\2\u01b2" +
      "\u01b4\5H%\2\u01b3\u01b2\3\2\2\2\u01b3\u01b4\3\2\2\2\u01b4\u01b6\3\2\2" +
      "\2\u01b5\u01b7\5J&\2\u01b6\u01b5\3\2\2\2\u01b6\u01b7\3\2\2\2\u01b7\u01b9" +
      "\3\2\2\2\u01b8\u01ba\5L\'\2\u01b9\u01b8\3\2\2\2\u01b9\u01ba\3\2\2\2\u01ba" +
      "\u01bc\3\2\2\2\u01bb\u01bd\5N(\2\u01bc\u01bb\3\2\2\2\u01bc\u01bd\3\2\2" +
      "\2\u01bd?\3\2\2\2\u01be\u01c3\5B\"\2\u01bf\u01c0\7\t\2\2\u01c0\u01c2\5" +
      "B\"\2\u01c1\u01bf\3\2\2\2\u01c2\u01c5\3\2\2\2\u01c3\u01c1\3\2\2\2\u01c3" +
      "\u01c4\3\2\2\2\u01c4A\3\2\2\2\u01c5\u01c3\3\2\2\2\u01c6\u01c9\5\u0104" +
      "\u0083\2\u01c7\u01c9\5\u010a\u0086\2\u01c8\u01c6\3\2\2\2\u01c8\u01c7\3" +
      "\2\2\2\u01c9C\3\2\2\2\u01ca\u01cb\7\5\2\2\u01cb\u01d0\5\u0104\u0083\2" +
      "\u01cc\u01cd\5\u0106\u0084\2\u01cd\u01ce\7\n\2\2\u01ce\u01cf\5\u0106\u0084" +
      "\2\u01cf\u01d1\3\2\2\2\u01d0\u01cc\3\2\2\2\u01d0\u01d1\3\2\2\2\u01d1E" +
      "\3\2\2\2\u01d2\u01d3\7#\2\2\u01d3\u01d4\5\u0104\u0083\2\u01d4G\3\2\2\2" +
      "\u01d5\u01d6\7\13\2\2\u01d6I\3\2\2\2\u01d7\u01d8\7\27\2\2\u01d8\u01d9" +
      "\5\u0104\u0083\2\u01d9K\3\2\2\2\u01da\u01db\7<\2\2\u01db\u01dc\5\u0102" +
      "\u0082\2\u01dcM\3\2\2\2\u01dd\u01de\7\35\2\2\u01de\u01df\5\u0102\u0082" +
      "\2\u01dfO\3\2\2\2\u01e0\u01e1\7/\2\2\u01e1\u01e3\5R*\2\u01e2\u01e4\5T" +
      "+\2\u01e3\u01e2\3\2\2\2\u01e3\u01e4\3\2\2\2\u01e4Q\3\2\2\2\u01e5\u01ea" +
      "\5X-\2\u01e6\u01e7\7\b\2\2\u01e7\u01e9\5X-\2\u01e8\u01e6\3\2\2\2\u01e9" +
      "\u01ec\3\2\2\2\u01ea\u01e8\3\2\2\2\u01ea\u01eb\3\2\2\2\u01ebS\3\2\2\2" +
      "\u01ec\u01ea\3\2\2\2\u01ed\u01ee\7\65\2\2\u01ee\u01ef\7\f\2\2\u01ef\u01f0" +
      "\5V,\2\u01f0\u01f1\7\r\2\2\u01f1U\3\2\2\2\u01f2\u01f4\5\62\32\2\u01f3" +
      "\u01f2\3\2\2\2\u01f4\u01f7\3\2\2\2\u01f5\u01f3\3\2\2\2\u01f5\u01f6\3\2" +
      "\2\2\u01f6W\3\2\2\2\u01f7\u01f5\3\2\2\2\u01f8\u020d\5b\62\2\u01f9\u020d" +
      "\5\\/\2\u01fa\u020d\5^\60\2\u01fb\u020d\5h\65\2\u01fc\u020d\5p9\2\u01fd" +
      "\u020d\5v<\2\u01fe\u020d\5\u0084C\2\u01ff\u020d\5\u008aF\2\u0200\u020d" +
      "\5\u008eH\2\u0201\u020d\5\u0092J\2\u0202\u020d\5\u009aN\2\u0203\u020d" +
      "\5\u00b2Z\2\u0204\u020d\5\u00ba^\2\u0205\u020d\5\u00c0a\2\u0206\u020d" +
      "\5\u00c8e\2\u0207\u020d\5\u00ccg\2\u0208\u020d\5\u00e2r\2\u0209\u020d" +
      "\5\u00e6t\2\u020a\u020d\5\u00f8}\2\u020b\u020d\5\u00fe\u0080\2\u020c\u01f8" +
      "\3\2\2\2\u020c\u01f9\3\2\2\2\u020c\u01fa\3\2\2\2\u020c\u01fb\3\2\2\2\u020c" +
      "\u01fc\3\2\2\2\u020c\u01fd\3\2\2\2\u020c\u01fe\3\2\2\2\u020c\u01ff\3\2" +
      "\2\2\u020c\u0200\3\2\2\2\u020c\u0201\3\2\2\2\u020c\u0202\3\2\2\2\u020c" +
      "\u0203\3\2\2\2\u020c\u0204\3\2\2\2\u020c\u0205\3\2\2\2\u020c\u0206\3\2" +
      "\2\2\u020c\u0207\3\2\2\2\u020c\u0208\3\2\2\2\u020c\u0209\3\2\2\2\u020c" +
      "\u020a\3\2\2\2\u020c\u020b\3\2\2\2\u020dY\3\2\2\2\u020e\u020f\5@!\2\u020f" +
      "\u0210\7\3\2\2\u0210[\3\2\2\2\u0211\u0212\5@!\2\u0212\u0213\5\u0100\u0081" +
      "\2\u0213]\3\2\2\2\u0214\u0215\5Z.\2\u0215\u0217\5`\61\2\u0216\u0218\5" +
      "\u0100\u0081\2\u0217\u0216\3\2\2\2\u0217\u0218\3\2\2\2\u0218_\3\2\2\2" +
      "\u0219\u021c\5\u010a\u0086\2\u021a\u021c\5\u0104\u0083\2\u021b\u0219\3" +
      "\2\2\2\u021b\u021a\3\2\2\2\u021ca\3\2\2\2\u021d\u021f\5Z.\2\u021e\u021d" +
      "\3\2\2\2\u021e\u021f\3\2\2\2\u021f\u0220\3\2\2\2\u0220\u0221\7\26\2\2" +
      "\u0221\u0222\7\6\2\2\u0222\u0223\5d\63\2\u0223\u0225\7\7\2\2\u0224\u0226" +
      "\5\u0100\u0081\2\u0225\u0224\3\2\2\2\u0225\u0226\3\2\2\2\u0226c\3\2\2" +
      "\2\u0227\u022c\5f\64\2\u0228\u0229\7\b\2\2\u0229\u022b\5f\64\2\u022a\u0228" +
      "\3\2\2\2\u022b\u022e\3\2\2\2\u022c\u022a\3\2\2\2\u022c\u022d\3\2\2\2\u022d" +
      "e\3\2\2\2\u022e\u022c\3\2\2\2\u022f\u0232\5\u0104\u0083\2\u0230\u0232" +
      "\5\u010a\u0086\2\u0231\u022f\3\2\2\2\u0231\u0230\3\2\2\2\u0232g\3\2\2" +
      "\2\u0233\u0235\5Z.\2\u0234\u0233\3\2\2\2\u0234\u0235\3\2\2\2\u0235\u0236" +
      "\3\2\2\2\u0236\u0237\7\31\2\2\u0237\u0238\7\6\2\2\u0238\u0239\5j\66\2" +
      "\u0239\u023a\7\b\2\2\u023a\u023d\5l\67\2\u023b\u023c\7\b\2\2\u023c\u023e" +
      "\5n8\2\u023d\u023b\3\2\2\2\u023d\u023e\3\2\2\2\u023e\u023f\3\2\2\2\u023f" +
      "\u0241\7\7\2\2\u0240\u0242\5\u0100\u0081\2\u0241\u0240\3\2\2\2\u0241\u0242" +
      "\3\2\2\2\u0242i\3\2\2\2\u0243\u0244\5\u010e\u0088\2\u0244k\3\2\2\2\u0245" +
      "\u0248\5\u010a\u0086\2\u0246\u0248\5\u0104\u0083\2\u0247\u0245\3\2\2\2" +
      "\u0247\u0246\3\2\2\2\u0248m\3\2\2\2\u0249\u024a\5\u010c\u0087\2\u024a" +
      "o\3\2\2\2\u024b\u024d\5Z.\2\u024c\u024b\3\2\2\2\u024c\u024d\3\2\2\2\u024d" +
      "\u024e\3\2\2\2\u024e\u024f\7\30\2\2\u024f\u0250\7\6\2\2\u0250\u0253\5" +
      "r:\2\u0251\u0252\7\b\2\2\u0252\u0254\5t;\2\u0253\u0251\3\2\2\2\u0253\u0254" +
      "\3\2\2\2\u0254\u0255\3\2\2\2\u0255\u0257\7\7\2\2\u0256\u0258\5\u0100\u0081" +
      "\2\u0257\u0256\3\2\2\2\u0257\u0258\3\2\2\2\u0258q\3\2\2\2\u0259\u025a" +
      "\5\u0104\u0083\2\u025as\3\2\2\2\u025b\u025c\5\u0104\u0083\2\u025cu\3\2" +
      "\2\2\u025d\u0260\5x=\2\u025e\u0260\5|?\2\u025f\u025d\3\2\2\2\u025f\u025e" +
      "\3\2\2\2\u0260\u0262\3\2\2\2\u0261\u0263\5\u0100\u0081\2\u0262\u0261\3" +
      "\2\2\2\u0262\u0263\3\2\2\2\u0263w\3\2\2\2\u0264\u0266\5Z.\2\u0265\u0264" +
      "\3\2\2\2\u0265\u0266\3\2\2\2\u0266\u0267\3\2\2\2\u0267\u0268\7\32\2\2" +
      "\u0268\u0269\7\6\2\2\u0269\u026a\5z>\2\u026a\u026c\7\7\2\2\u026b\u026d" +
      "\5\u0100\u0081\2\u026c\u026b\3\2\2\2\u026c\u026d\3\2\2\2\u026dy\3\2\2" +
      "\2\u026e\u026f\5\u010c\u0087\2\u026f{\3\2\2\2\u0270\u0272\5Z.\2\u0271" +
      "\u0270\3\2\2\2\u0271\u0272\3\2\2\2\u0272\u0273\3\2\2\2\u0273\u0274\7\32" +
      "\2\2\u0274\u0275\7\6\2\2\u0275\u0276\5~@\2\u0276\u0277\7\b\2\2\u0277\u027a" +
      "\5\u0080A\2\u0278\u0279\7\b\2\2\u0279\u027b\5\u0082B\2\u027a\u0278\3\2" +
      "\2\2\u027a\u027b\3\2\2\2\u027b\u027c\3\2\2\2\u027c\u027e\7\7\2\2\u027d" +
      "\u027f\5\u0100\u0081\2\u027e\u027d\3\2\2\2\u027e\u027f\3\2\2\2\u027f}" +
      "\3\2\2\2\u0280\u0281\5\u010e\u0088\2\u0281\177\3\2\2\2\u0282\u0285\5\u010a" +
      "\u0086\2\u0283\u0285\5\u0104\u0083\2\u0284\u0282\3\2\2\2\u0284\u0283\3" +
      "\2\2\2\u0285\u0081\3\2\2\2\u0286\u0287\5\u010c\u0087\2\u0287\u0083\3\2" +
      "\2\2\u0288\u028a\5Z.\2\u0289\u0288\3\2\2\2\u0289\u028a\3\2\2\2\u028a\u028b" +
      "\3\2\2\2\u028b\u028c\7\34\2\2\u028c\u0290\7\6\2\2\u028d\u028e\5\u0086" +
      "D\2\u028e\u028f\7\b\2\2\u028f\u0291\3\2\2\2\u0290\u028d\3\2\2\2\u0290" +
      "\u0291\3\2\2\2\u0291\u0292\3\2\2\2\u0292\u0293\5\u0088E\2\u0293\u0295" +
      "\7\7\2\2\u0294\u0296\5\u0100\u0081\2\u0295\u0294\3\2\2\2\u0295\u0296\3" +
      "\2\2\2\u0296\u0085\3\2\2\2\u0297\u0298\5\u010e\u0088\2\u0298\u0087\3\2" +
      "\2\2\u0299\u029a\5\u0104\u0083\2\u029a\u0089\3\2\2\2\u029b\u029c\5Z.\2" +
      "\u029c\u029d\7 \2\2\u029d\u029e\7\6\2\2\u029e\u029f\5\u008cG\2\u029f\u02a1" +
      "\7\7\2\2\u02a0\u02a2\5\u0100\u0081\2\u02a1\u02a0\3\2\2\2\u02a1\u02a2\3" +
      "\2\2\2\u02a2\u008b\3\2\2\2\u02a3\u02a4\5\u0104\u0083\2\u02a4\u008d\3\2" +
      "\2\2\u02a5\u02a7\5Z.\2\u02a6\u02a5\3\2\2\2\u02a6\u02a7\3\2\2\2\u02a7\u02a8" +
      "\3\2\2\2\u02a8\u02a9\7!\2\2\u02a9\u02aa\7\6\2\2\u02aa\u02ab\5\u0090I\2" +
      "\u02ab\u02ad\7\7\2\2\u02ac\u02ae\5\u0100\u0081\2\u02ad\u02ac\3\2\2\2\u02ad" +
      "\u02ae\3\2\2\2\u02ae\u008f\3\2\2\2\u02af\u02b0\5\u0108\u0085\2\u02b0\u0091" +
      "\3\2\2\2\u02b1\u02b3\5Z.\2\u02b2\u02b1\3\2\2\2\u02b2\u02b3\3\2\2\2\u02b3" +
      "\u02b4\3\2\2\2\u02b4\u02b5\7\"\2\2\u02b5\u02b6\7\6\2\2\u02b6\u02b7\5\u0094" +
      "K\2\u02b7\u02b8\7\b\2\2\u02b8\u02bb\5\u0096L\2\u02b9\u02ba\7\b\2\2\u02ba" +
      "\u02bc\5\u0098M\2\u02bb\u02b9\3\2\2\2\u02bb\u02bc\3\2\2\2\u02bc\u02bd" +
      "\3\2\2\2\u02bd\u02bf\7\7\2\2\u02be\u02c0\5\u0100\u0081\2\u02bf\u02be\3" +
      "\2\2\2\u02bf\u02c0\3\2\2\2\u02c0\u0093\3\2\2\2\u02c1\u02c2\5\u0104\u0083" +
      "\2\u02c2\u0095\3\2\2\2\u02c3\u02c4\5\u010c\u0087\2\u02c4\u0097\3\2\2\2" +
      "\u02c5\u02c6\5\u0104\u0083\2\u02c6\u0099\3\2\2\2\u02c7\u02ca\5\u009cO" +
      "\2\u02c8\u02ca\5\u009eP\2\u02c9\u02c7\3\2\2\2\u02c9\u02c8\3\2\2\2\u02ca" +
      "\u02cc\3\2\2\2\u02cb\u02cd\5\u0100\u0081\2\u02cc\u02cb\3\2\2\2\u02cc\u02cd" +
      "\3\2\2\2\u02cd\u009b\3\2\2\2\u02ce\u02d0\5Z.\2\u02cf\u02ce\3\2\2\2\u02cf" +
      "\u02d0\3\2\2\2\u02d0\u02d1\3\2\2\2\u02d1\u02d2\7\'\2\2\u02d2\u02d3\7\6" +
      "\2\2\u02d3\u02d5\7\7\2\2\u02d4\u02d6\5\u0100\u0081\2\u02d5\u02d4\3\2\2" +
      "\2\u02d5\u02d6\3\2\2\2\u02d6\u009d\3\2\2\2\u02d7\u02d9\5Z.\2\u02d8\u02d7" +
      "\3\2\2\2\u02d8\u02d9\3\2\2\2\u02d9\u02da\3\2\2\2\u02da\u02db\7\'\2\2\u02db" +
      "\u02dc\7\6\2\2\u02dc\u02dd\5\u00a0Q\2\u02dd\u02de\7\b\2\2\u02de\u02df" +
      "\5\u00a2R\2\u02df\u02e0\7\b\2\2\u02e0\u02e1\5\u00a4S\2\u02e1\u02e2\7\b" +
      "\2\2\u02e2\u02e3\5\u00a6T\2\u02e3\u02e4\7\b\2\2\u02e4\u02e5\5\u00a8U\2" +
      "\u02e5\u02e6\7\b\2\2\u02e6\u02e7\5\u00aaV\2\u02e7\u02e8\7\b\2\2\u02e8" +
      "\u02e9\5\u00acW\2\u02e9\u02ea\7\b\2\2\u02ea\u02eb\5\u00aeX\2\u02eb\u02ec" +
      "\7\b\2\2\u02ec\u02ed\5\u00b0Y\2\u02ed\u02ef\7\7\2\2\u02ee\u02f0\5\u0100" +
      "\u0081\2\u02ef\u02ee\3\2\2\2\u02ef\u02f0\3\2\2\2\u02f0\u009f\3\2\2\2\u02f1" +
      "\u02f2\5\u010e\u0088\2\u02f2\u00a1\3\2\2\2\u02f3\u02f4\5\u010c\u0087\2" +
      "\u02f4\u00a3\3\2\2\2\u02f5\u02f6\5\u010c\u0087\2\u02f6\u00a5\3\2\2\2\u02f7" +
      "\u02f8\5\u010c\u0087\2\u02f8\u00a7\3\2\2\2\u02f9\u02fa\5\u010c\u0087\2" +
      "\u02fa\u00a9\3\2\2\2\u02fb\u02fc\5\u010c\u0087\2\u02fc\u00ab\3\2\2\2\u02fd" +
      "\u02fe\5\u0106\u0084\2\u02fe\u00ad\3\2\2\2\u02ff\u0300\5\u010c\u0087\2" +
      "\u0300\u00af\3\2\2\2\u0301\u0302\5\u010c\u0087\2\u0302\u00b1\3\2\2\2\u0303" +
      "\u0305\5Z.\2\u0304\u0303\3\2\2\2\u0304\u0305\3\2\2\2\u0305\u0306\3\2\2" +
      "\2\u0306\u0307\7&\2\2\u0307\u0308\7\6\2\2\u0308\u0309\5\u00b4[\2\u0309" +
      "\u030a\7\b\2\2\u030a\u030d\5\u00b6\\\2\u030b\u030c\7\b\2\2\u030c\u030e" +
      "\5\u00b8]\2\u030d\u030b\3\2\2\2\u030d\u030e\3\2\2\2\u030e\u030f\3\2\2" +
      "\2\u030f\u0311\7\7\2\2\u0310\u0312\5\u0100\u0081\2\u0311\u0310\3\2\2\2" +
      "\u0311\u0312\3\2\2\2\u0312\u00b3\3\2\2\2\u0313\u0314\5\u0104\u0083\2\u0314" +
      "\u00b5\3\2\2\2\u0315\u0316\5\u010c\u0087\2\u0316\u00b7\3\2\2\2\u0317\u0318" +
      "\5\u010c\u0087\2\u0318\u00b9\3\2\2\2\u0319\u031b\5Z.\2\u031a\u0319\3\2" +
      "\2\2\u031a\u031b\3\2\2\2\u031b\u031c\3\2\2\2\u031c\u031d\7(\2\2\u031d" +
      "\u031e\7\6\2\2\u031e\u031f\5\u00bc_\2\u031f\u0320\7\b\2\2\u0320\u0321" +
      "\5\u00be`\2\u0321\u0323\7\7\2\2\u0322\u0324\5\u0100\u0081\2\u0323\u0322" +
      "\3\2\2\2\u0323\u0324\3\2\2\2\u0324\u00bb\3\2\2\2\u0325\u0326\5\u0104\u0083" +
      "\2\u0326\u00bd\3\2\2\2\u0327\u0328\5\u0104\u0083\2\u0328\u00bf\3\2\2\2" +
      "\u0329\u032b\5Z.\2\u032a\u0329\3\2\2\2\u032a\u032b\3\2\2\2\u032b\u032c" +
      "\3\2\2\2\u032c\u032d\7,\2\2\u032d\u032e\7\6\2\2\u032e\u032f\5\u00c2b\2" +
      "\u032f\u0330\7\b\2\2\u0330\u0333\5\u00c4c\2\u0331\u0332\7\b\2\2\u0332" +
      "\u0334\5\u00c6d\2\u0333\u0331\3\2\2\2\u0333\u0334\3\2\2\2\u0334\u0335" +
      "\3\2\2\2\u0335\u0337\7\7\2\2\u0336\u0338\5\u0100\u0081\2\u0337\u0336\3" +
      "\2\2\2\u0337\u0338\3\2\2\2\u0338\u00c1\3\2\2\2\u0339\u033a\5\u010e\u0088" +
      "\2\u033a\u00c3\3\2\2\2\u033b\u033c\5\u0104\u0083\2\u033c\u00c5\3\2\2\2" +
      "\u033d\u033e\5\u0104\u0083\2\u033e\u00c7\3\2\2\2\u033f\u0341\5Z.\2\u0340" +
      "\u033f\3\2\2\2\u0340\u0341\3\2\2\2\u0341\u0342\3\2\2\2\u0342\u0343\7\61" +
      "\2\2\u0343\u0344\7\6\2\2\u0344\u0345\5\u00caf\2\u0345\u0347\7\7\2\2\u0346" +
      "\u0348\5\u0100\u0081\2\u0347\u0346\3\2\2\2\u0347\u0348\3\2\2\2\u0348\u00c9" +
      "\3\2\2\2\u0349\u034a\5\u0104\u0083\2\u034a\u00cb\3\2\2\2\u034b\u034f\5" +
      "\u00ceh\2\u034c\u034f\5\u00d2j\2\u034d\u034f\5\u00dan\2\u034e\u034b\3" +
      "\2\2\2\u034e\u034c\3\2\2\2\u034e\u034d\3\2\2\2\u034f\u00cd\3\2\2\2\u0350" +
      "\u0352\5Z.\2\u0351\u0350\3\2\2\2\u0351\u0352\3\2\2\2\u0352\u0353\3\2\2" +
      "\2\u0353\u0354\7\62\2\2\u0354\u0355\7\6\2\2\u0355\u0356\5\u00d0i\2\u0356" +
      "\u0358\7\7\2\2\u0357\u0359\5\u0100\u0081\2\u0358\u0357\3\2\2\2\u0358\u0359" +
      "\3\2\2\2\u0359\u00cf\3\2\2\2\u035a\u035b\5\u010c\u0087\2\u035b\u00d1\3" +
      "\2\2\2\u035c\u035e\5Z.\2\u035d\u035c\3\2\2\2\u035d\u035e\3\2\2\2\u035e" +
      "\u035f\3\2\2\2\u035f\u0360\7\62\2\2\u0360\u0361\7\6\2\2\u0361\u0362\5" +
      "\u00d4k\2\u0362\u0363\7\b\2\2\u0363\u0364\5\u00d6l\2\u0364\u0365\7\b\2" +
      "\2\u0365\u0366\5\u00d8m\2\u0366\u0368\7\7\2\2\u0367\u0369\5\u0100\u0081" +
      "\2\u0368\u0367\3\2\2\2\u0368\u0369\3\2\2\2\u0369\u00d3\3\2\2\2\u036a\u036b" +
      "\5\u0104\u0083\2\u036b\u00d5\3\2\2\2\u036c\u036d\5\u010c\u0087\2\u036d" +
      "\u00d7\3\2\2\2\u036e\u036f\5\u010e\u0088\2\u036f\u00d9\3\2\2\2\u0370\u0372" +
      "\5Z.\2\u0371\u0370\3\2\2\2\u0371\u0372\3\2\2\2\u0372\u0373\3\2\2\2\u0373" +
      "\u0374\7\62\2\2\u0374\u0375\7\6\2\2\u0375\u0376\5\u00dco\2\u0376\u0377" +
      "\7\b\2\2\u0377\u0378\5\u00dep\2\u0378\u0379\7\b\2\2\u0379\u037a\5\u00e0" +
      "q\2\u037a\u037c\7\7\2\2\u037b\u037d\5\u0100\u0081\2\u037c\u037b\3\2\2" +
      "\2\u037c\u037d\3\2\2\2\u037d\u00db\3\2\2\2\u037e\u037f\5\u0104\u0083\2" +
      "\u037f\u00dd\3\2\2\2\u0380\u0381\5\u010c\u0087\2\u0381\u00df\3\2\2\2\u0382" +
      "\u0383\5\u0104\u0083\2\u0383\u00e1\3\2\2\2\u0384\u0386\5Z.\2\u0385\u0384" +
      "\3\2\2\2\u0385\u0386\3\2\2\2\u0386\u0387\3\2\2\2\u0387\u0388\7\63\2\2" +
      "\u0388\u0389\7\6\2\2\u0389\u038a\5\u00e4s\2\u038a\u038c\7\7\2\2\u038b" +
      "\u038d\5\u0100\u0081\2\u038c\u038b\3\2\2\2\u038c\u038d\3\2\2\2\u038d\u00e3" +
      "\3\2\2\2\u038e\u0391\5\u0104\u0083\2\u038f\u0391\5\u010a\u0086\2\u0390" +
      "\u038e\3\2\2\2\u0390\u038f\3\2\2\2\u0391\u00e5\3\2\2\2\u0392\u0394\5Z" +
      ".\2\u0393\u0392\3\2\2\2\u0393\u0394\3\2\2\2\u0394\u0395\3\2\2\2\u0395" +
      "\u0396\7\66\2\2\u0396\u0397\7\6\2\2\u0397\u0398\5\u00e8u\2\u0398\u0399" +
      "\7\b\2\2\u0399\u039a\5\u00eav\2\u039a\u039b\7\b\2\2\u039b\u039c\5\u00ec" +
      "w\2\u039c\u039e\7\7\2\2\u039d\u039f\5\u0100\u0081\2\u039e\u039d\3\2\2" +
      "\2\u039e\u039f\3\2\2\2\u039f\u00e7\3\2\2\2\u03a0\u03a1\5\u0104\u0083\2" +
      "\u03a1\u00e9\3\2\2\2\u03a2\u03a3\5\u010e\u0088\2\u03a3\u00eb\3\2\2\2\u03a4" +
      "\u03aa\5\u00eex\2\u03a5\u03aa\5\u00f0y\2\u03a6\u03aa\5\u00f2z\2\u03a7" +
      "\u03aa\5\u00f4{\2\u03a8\u03aa\5\u00f6|\2\u03a9\u03a4\3\2\2\2\u03a9\u03a5" +
      "\3\2\2\2\u03a9\u03a6\3\2\2\2\u03a9\u03a7\3\2\2\2\u03a9\u03a8\3\2\2\2\u03aa" +
      "\u00ed\3\2\2\2\u03ab\u03ac\7\33\2\2\u03ac\u00ef\3\2\2\2\u03ad\u03ae\7" +
      "\64\2\2\u03ae\u00f1\3\2\2\2\u03af\u03b0\7$\2\2\u03b0\u00f3\3\2\2\2\u03b1" +
      "\u03b2\7\37\2\2\u03b2\u00f5\3\2\2\2\u03b3\u03b4\7\36\2\2\u03b4\u00f7\3" +
      "\2\2\2\u03b5\u03b7\5Z.\2\u03b6\u03b5\3\2\2\2\u03b6\u03b7\3\2\2\2\u03b7" +
      "\u03b8\3\2\2\2\u03b8\u03b9\7\67\2\2\u03b9\u03ba\7\6\2\2\u03ba\u03bb\5" +
      "\u00fa~\2\u03bb\u03bc\7\b\2\2\u03bc\u03bd\5\u00fc\177\2\u03bd\u03bf\7" +
      "\7\2\2\u03be\u03c0\5\u0100\u0081\2\u03bf\u03be\3\2\2\2\u03bf\u03c0\3\2" +
      "\2\2\u03c0\u00f9\3\2\2\2\u03c1\u03c4\5\u0104\u0083\2\u03c2\u03c4\5\u010a" +
      "\u0086\2\u03c3\u03c1\3\2\2\2\u03c3\u03c2\3\2\2\2\u03c4\u00fb\3\2\2\2\u03c5" +
      "\u03c6\5\u0106\u0084\2\u03c6\u00fd\3\2\2\2\u03c7\u03c9\5Z.\2\u03c8\u03c7" +
      "\3\2\2\2\u03c8\u03c9\3\2\2\2\u03c9\u03ca\3\2\2\2\u03ca\u03cb\7:\2\2\u03cb" +
      "\u03cc\7\6\2\2\u03cc\u03ce\7\7\2\2\u03cd\u03cf\5\u0100\u0081\2\u03ce\u03cd" +
      "\3\2\2\2\u03ce\u03cf\3\2\2\2\u03cf\u00ff\3\2\2\2\u03d0\u03d1\7\27\2\2" +
      "\u03d1\u03d2\5\u0104\u0083\2\u03d2\u0101\3\2\2\2\u03d3\u03d4\5\u010c\u0087" +
      "\2\u03d4\u0103\3\2\2\2\u03d5\u03d6\t\2\2\2\u03d6\u0105\3\2\2\2\u03d7\u03d8" +
      "\7\17\2\2\u03d8\u0107\3\2\2\2\u03d9\u03da\7\24\2\2\u03da\u0109\3\2\2\2" +
      "\u03db\u03dc\t\3\2\2\u03dc\u010b\3\2\2\2\u03dd\u03de\t\3\2\2\u03de\u010d" +
      "\3\2\2\2\u03df\u03e0\5\u010c\u0087\2\u03e0\u010f\3\2\2\2\u03e1\u03e2\5" +
      "\u010e\u0088\2\u03e2\u0111\3\2\2\2\u03e3\u03e4\5\u010e\u0088\2\u03e4\u0113" +
      "\3\2\2\2b\u0118\u011d\u012c\u0132\u013f\u0145\u014b\u0152\u0156\u0162" +
      "\u016d\u0170\u017b\u0183\u0188\u0191\u0199\u01a0\u01a8\u01ad\u01b0\u01b3" +
      "\u01b6\u01b9\u01bc\u01c3\u01c8\u01d0\u01e3\u01ea\u01f5\u020c\u0217\u021b" +
      "\u021e\u0225\u022c\u0231\u0234\u023d\u0241\u0247\u024c\u0253\u0257\u025f" +
      "\u0262\u0265\u026c\u0271\u027a\u027e\u0284\u0289\u0290\u0295\u02a1\u02a6" +
      "\u02ad\u02b2\u02bb\u02bf\u02c9\u02cc\u02cf\u02d5\u02d8\u02ef\u0304\u030d" +
      "\u0311\u031a\u0323\u032a\u0333\u0337\u0340\u0347\u034e\u0351\u0358\u035d" +
      "\u0368\u0371\u037c\u0385\u038c\u0390\u0393\u039e\u03a9\u03b6\u03bf\u03c3" +
      "\u03c8\u03ce";
  public static final ATN _ATN =
    new ATNDeserializer().deserialize(_serializedATN.toCharArray());

  static {
    _decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
    for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
      _decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
    }
  }
}
