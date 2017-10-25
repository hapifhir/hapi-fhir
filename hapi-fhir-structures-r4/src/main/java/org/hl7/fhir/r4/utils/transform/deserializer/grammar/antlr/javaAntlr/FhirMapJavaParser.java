// Generated from FhirMapJava.g4 by ANTLR 4.7
package org.hl7.fhir.r4.utils.transform.deserializer.grammar.antlr.javaAntlr;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.atn.ATNDeserializer;
import org.antlr.v4.runtime.atn.ParserATNSimulator;
import org.antlr.v4.runtime.atn.PredictionContextCache;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.List;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class FhirMapJavaParser extends Parser {
  static { RuntimeMetaData.checkVersion("4.7", RuntimeMetaData.VERSION); }

  protected static final DFA[] _decisionToDFA;
  protected static final PredictionContextCache _sharedContextCache =
    new PredictionContextCache();
  public static final int
    T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9,
    T__9=10, T__10=11, HEX=12, DIGITS=13, SOURCE=14, TARGET=15, QUERIED=16,
    PRODUCED=17, QIDENTIFIER=18, QSTRING=19, APPEND=20, AS=21, CAST=22, C=23,
    CC=24, CODE=25, CP=26, CHECK=27, CODEABLECONCEPT=28, CODING=29, COPY=30,
    CREATE=31, DATEOP=32, DEFAULT=33, DISPLAY=34, ENDGROUP=35, ESCAPE=36,
    EXTENSION=37, EVALUATE=38, EXTENDS=39, FOR=40, GROUP=41, ID=42, IMPORTS=43,
    INPUT=44, MAKE=45, MAP=46, POINTER=47, QTY=48, REFERENCE=49, SYSTEM=50,
    THEN=51, TRANSLATE=52, TRUNCATE=53, TYPES=54, TYPE_TYPES=55, UUID=56,
    USES=57, WHERE=58, IDENTIFIER=59, WS=60, LINE_COMMENT=61;
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
    RULE_ruleTargetDateOpVariable2 = 75, RULE_ruleTargetExtension = 76, RULE_ruleTargetEscape = 77,
    RULE_ruleTargetEscapeVariable = 78, RULE_ruleTargetEscapeString1 = 79,
    RULE_ruleTargetEscapeString2 = 80, RULE_ruleTargetEvaluate = 81, RULE_ruleTargetEvaluateObject = 82,
    RULE_ruleTargetEvaluateObjectElement = 83, RULE_ruleTargetId = 84, RULE_ruleTargetIdSystem = 85,
    RULE_ruleTargetIdValue = 86, RULE_ruleTargetIdType = 87, RULE_ruleTargetPointer = 88,
    RULE_ruleTargetPointerResource = 89, RULE_ruleTargetQty = 90, RULE_ruleTargetQty1 = 91,
    RULE_ruleTargetQty1Text = 92, RULE_ruleTargetQty2 = 93, RULE_ruleTargetQty2Value = 94,
    RULE_ruleTargetQty2UnitString = 95, RULE_ruleTargetQty2System = 96, RULE_ruleTargetQty3 = 97,
    RULE_ruleTargetQty3Value = 98, RULE_ruleTargetQty3UnitString = 99, RULE_ruleTargetQty3CodeVariable = 100,
    RULE_ruleTargetReference = 101, RULE_ruleTargetReferenceSource = 102,
    RULE_ruleTargetTranslate = 103, RULE_ruleTargetTranslateSource = 104,
    RULE_ruleTargetTranslateMap = 105, RULE_ruleTargetTranslateOutput = 106,
    RULE_ruleTargetTranslateOutputCode = 107, RULE_ruleTargetTranslateOutputSystem = 108,
    RULE_ruleTargetTranslateOutputDisplay = 109, RULE_ruleTargetTranslateOutputCoding = 110,
    RULE_ruleTargetTranslateOutputCodeableConcept = 111, RULE_ruleTargetTruncate = 112,
    RULE_ruleTargetTruncateSource = 113, RULE_ruleTargetTruncateLength = 114,
    RULE_ruleTargetUuid = 115, RULE_ruleTargetVariable = 116, RULE_fhirPath = 117,
    RULE_identifier = 118, RULE_integer = 119, RULE_quotedIdentifier = 120,
    RULE_quotedStringWQuotes = 121, RULE_quotedString = 122, RULE_quotedUrl = 123,
    RULE_structureDefinition = 124, RULE_structureMap = 125;
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
    "ruleTargetEscape", "ruleTargetEscapeVariable", "ruleTargetEscapeString1",
    "ruleTargetEscapeString2", "ruleTargetEvaluate", "ruleTargetEvaluateObject",
    "ruleTargetEvaluateObjectElement", "ruleTargetId", "ruleTargetIdSystem",
    "ruleTargetIdValue", "ruleTargetIdType", "ruleTargetPointer", "ruleTargetPointerResource",
    "ruleTargetQty", "ruleTargetQty1", "ruleTargetQty1Text", "ruleTargetQty2",
    "ruleTargetQty2Value", "ruleTargetQty2UnitString", "ruleTargetQty2System",
    "ruleTargetQty3", "ruleTargetQty3Value", "ruleTargetQty3UnitString", "ruleTargetQty3CodeVariable",
    "ruleTargetReference", "ruleTargetReferenceSource", "ruleTargetTranslate",
    "ruleTargetTranslateSource", "ruleTargetTranslateMap", "ruleTargetTranslateOutput",
    "ruleTargetTranslateOutputCode", "ruleTargetTranslateOutputSystem", "ruleTargetTranslateOutputDisplay",
    "ruleTargetTranslateOutputCoding", "ruleTargetTranslateOutputCodeableConcept",
    "ruleTargetTruncate", "ruleTargetTruncateSource", "ruleTargetTruncateLength",
    "ruleTargetUuid", "ruleTargetVariable", "fhirPath", "identifier", "integer",
    "quotedIdentifier", "quotedStringWQuotes", "quotedString", "quotedUrl",
    "structureDefinition", "structureMap"
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
  public String getGrammarFileName() { return "FhirMapJava.g4"; }

  @Override
  public String[] getRuleNames() { return ruleNames; }

  @Override
  public String getSerializedATN() { return _serializedATN; }

  @Override
  public ATN getATN() { return _ATN; }

  public FhirMapJavaParser(TokenStream input) {
    super(input);
    _interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
  }
  public static class MappingUnitContext extends ParserRuleContext {
    public KeyMapContext keyMap() {
      return getRuleContext(KeyMapContext.class,0);
    }
    public GroupContext group() {
      return getRuleContext(GroupContext.class,0);
    }
    public List<KeyUsesContext> keyUses() {
      return getRuleContexts(KeyUsesContext.class);
    }
    public KeyUsesContext keyUses(int i) {
      return getRuleContext(KeyUsesContext.class,i);
    }
    public List<KeyImportsContext> keyImports() {
      return getRuleContexts(KeyImportsContext.class);
    }
    public KeyImportsContext keyImports(int i) {
      return getRuleContext(KeyImportsContext.class,i);
    }
    public MappingUnitContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_mappingUnit; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitMappingUnit(this);
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
        setState(252);
        keyMap();
        setState(254);
        _errHandler.sync(this);
        _la = _input.LA(1);
        do {
          {
            {
              setState(253);
              keyUses();
            }
          }
          setState(256);
          _errHandler.sync(this);
          _la = _input.LA(1);
        } while ( _la==USES );
        setState(261);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la==IMPORTS) {
          {
            {
              setState(258);
              keyImports();
            }
          }
          setState(263);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
        setState(264);
        group();
      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class KeyMapContext extends ParserRuleContext {
    public TerminalNode MAP() { return getToken(FhirMapJavaParser.MAP, 0); }
    public StructureMapContext structureMap() {
      return getRuleContext(StructureMapContext.class,0);
    }
    public QuotedStringContext quotedString() {
      return getRuleContext(QuotedStringContext.class,0);
    }
    public KeyMapContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_keyMap; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitKeyMap(this);
      else return visitor.visitChildren(this);
    }
  }

  public final KeyMapContext keyMap() throws RecognitionException {
    KeyMapContext _localctx = new KeyMapContext(_ctx, getState());
    enterRule(_localctx, 2, RULE_keyMap);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(266);
        match(MAP);
        setState(267);
        structureMap();
        setState(268);
        match(T__0);
        setState(269);
        quotedString();
      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class KeyUsesContext extends ParserRuleContext {
    public TerminalNode USES() { return getToken(FhirMapJavaParser.USES, 0); }
    public StructureDefinitionContext structureDefinition() {
      return getRuleContext(StructureDefinitionContext.class,0);
    }
    public TerminalNode AS() { return getToken(FhirMapJavaParser.AS, 0); }
    public KeyUsesNameContext keyUsesName() {
      return getRuleContext(KeyUsesNameContext.class,0);
    }
    public KeyUsesContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_keyUses; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitKeyUses(this);
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
        setState(271);
        match(USES);
        setState(272);
        structureDefinition();
        setState(273);
        match(AS);
        setState(274);
        keyUsesName();
        setState(276);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la==T__1) {
          {
            setState(275);
            match(T__1);
          }
        }

      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class KeyUsesNameContext extends ParserRuleContext {
    public KeyUsesNameSourceContext keyUsesNameSource() {
      return getRuleContext(KeyUsesNameSourceContext.class,0);
    }
    public KeyUsesNameTargetContext keyUsesNameTarget() {
      return getRuleContext(KeyUsesNameTargetContext.class,0);
    }
    public KeyUsesNameQueriedContext keyUsesNameQueried() {
      return getRuleContext(KeyUsesNameQueriedContext.class,0);
    }
    public KeyUsesNameProducedContext keyUsesNameProduced() {
      return getRuleContext(KeyUsesNameProducedContext.class,0);
    }
    public KeyUsesNameContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_keyUsesName; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitKeyUsesName(this);
      else return visitor.visitChildren(this);
    }
  }

  public final KeyUsesNameContext keyUsesName() throws RecognitionException {
    KeyUsesNameContext _localctx = new KeyUsesNameContext(_ctx, getState());
    enterRule(_localctx, 6, RULE_keyUsesName);
    try {
      setState(282);
      _errHandler.sync(this);
      switch (_input.LA(1)) {
        case SOURCE:
          enterOuterAlt(_localctx, 1);
        {
          setState(278);
          keyUsesNameSource();
        }
        break;
        case TARGET:
          enterOuterAlt(_localctx, 2);
        {
          setState(279);
          keyUsesNameTarget();
        }
        break;
        case QUERIED:
          enterOuterAlt(_localctx, 3);
        {
          setState(280);
          keyUsesNameQueried();
        }
        break;
        case PRODUCED:
          enterOuterAlt(_localctx, 4);
        {
          setState(281);
          keyUsesNameProduced();
        }
        break;
        default:
          throw new NoViableAltException(this);
      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class KeyUsesNameSourceContext extends ParserRuleContext {
    public TerminalNode SOURCE() { return getToken(FhirMapJavaParser.SOURCE, 0); }
    public KeyUsesNameSourceContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_keyUsesNameSource; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitKeyUsesNameSource(this);
      else return visitor.visitChildren(this);
    }
  }

  public final KeyUsesNameSourceContext keyUsesNameSource() throws RecognitionException {
    KeyUsesNameSourceContext _localctx = new KeyUsesNameSourceContext(_ctx, getState());
    enterRule(_localctx, 8, RULE_keyUsesNameSource);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(284);
        match(SOURCE);
      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class KeyUsesNameTargetContext extends ParserRuleContext {
    public TerminalNode TARGET() { return getToken(FhirMapJavaParser.TARGET, 0); }
    public KeyUsesNameTargetContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_keyUsesNameTarget; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitKeyUsesNameTarget(this);
      else return visitor.visitChildren(this);
    }
  }

  public final KeyUsesNameTargetContext keyUsesNameTarget() throws RecognitionException {
    KeyUsesNameTargetContext _localctx = new KeyUsesNameTargetContext(_ctx, getState());
    enterRule(_localctx, 10, RULE_keyUsesNameTarget);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(286);
        match(TARGET);
      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class KeyUsesNameQueriedContext extends ParserRuleContext {
    public TerminalNode QUERIED() { return getToken(FhirMapJavaParser.QUERIED, 0); }
    public KeyUsesNameQueriedContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_keyUsesNameQueried; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitKeyUsesNameQueried(this);
      else return visitor.visitChildren(this);
    }
  }

  public final KeyUsesNameQueriedContext keyUsesNameQueried() throws RecognitionException {
    KeyUsesNameQueriedContext _localctx = new KeyUsesNameQueriedContext(_ctx, getState());
    enterRule(_localctx, 12, RULE_keyUsesNameQueried);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(288);
        match(QUERIED);
      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class KeyUsesNameProducedContext extends ParserRuleContext {
    public TerminalNode PRODUCED() { return getToken(FhirMapJavaParser.PRODUCED, 0); }
    public KeyUsesNameProducedContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_keyUsesNameProduced; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitKeyUsesNameProduced(this);
      else return visitor.visitChildren(this);
    }
  }

  public final KeyUsesNameProducedContext keyUsesNameProduced() throws RecognitionException {
    KeyUsesNameProducedContext _localctx = new KeyUsesNameProducedContext(_ctx, getState());
    enterRule(_localctx, 14, RULE_keyUsesNameProduced);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(290);
        match(PRODUCED);
      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class KeyImportsContext extends ParserRuleContext {
    public TerminalNode IMPORTS() { return getToken(FhirMapJavaParser.IMPORTS, 0); }
    public StructureMapContext structureMap() {
      return getRuleContext(StructureMapContext.class,0);
    }
    public KeyImportsContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_keyImports; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitKeyImports(this);
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
        setState(292);
        match(IMPORTS);
        setState(293);
        structureMap();
        setState(295);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la==T__1) {
          {
            setState(294);
            match(T__1);
          }
        }

      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class GroupContext extends ParserRuleContext {
    public GroupStartContext groupStart() {
      return getRuleContext(GroupStartContext.class,0);
    }
    public GroupEndContext groupEnd() {
      return getRuleContext(GroupEndContext.class,0);
    }
    public List<GroupInputContext> groupInput() {
      return getRuleContexts(GroupInputContext.class);
    }
    public GroupInputContext groupInput(int i) {
      return getRuleContext(GroupInputContext.class,i);
    }
    public List<GroupItemContext> groupItem() {
      return getRuleContexts(GroupItemContext.class);
    }
    public GroupItemContext groupItem(int i) {
      return getRuleContext(GroupItemContext.class,i);
    }
    public GroupContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_group; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitGroup(this);
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
        setState(297);
        groupStart();
        setState(301);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la==INPUT) {
          {
            {
              setState(298);
              groupInput();
            }
          }
          setState(303);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
        setState(307);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << SOURCE) | (1L << TARGET) | (1L << QUERIED) | (1L << PRODUCED) | (1L << APPEND) | (1L << CAST) | (1L << C) | (1L << CC) | (1L << CODE) | (1L << CP) | (1L << CODEABLECONCEPT) | (1L << CODING) | (1L << COPY) | (1L << CREATE) | (1L << DATEOP) | (1L << DEFAULT) | (1L << DISPLAY) | (1L << ESCAPE) | (1L << EXTENSION) | (1L << EVALUATE) | (1L << ID) | (1L << MAP) | (1L << POINTER) | (1L << QTY) | (1L << REFERENCE) | (1L << SYSTEM) | (1L << TRANSLATE) | (1L << TRUNCATE) | (1L << TYPES) | (1L << UUID) | (1L << IDENTIFIER))) != 0)) {
          {
            {
              setState(304);
              groupItem();
            }
          }
          setState(309);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
        setState(310);
        groupEnd();
      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class GroupStartContext extends ParserRuleContext {
    public TerminalNode GROUP() { return getToken(FhirMapJavaParser.GROUP, 0); }
    public IdentifierContext identifier() {
      return getRuleContext(IdentifierContext.class,0);
    }
    public GroupTypeContext groupType() {
      return getRuleContext(GroupTypeContext.class,0);
    }
    public GroupExtendsContext groupExtends() {
      return getRuleContext(GroupExtendsContext.class,0);
    }
    public GroupStartContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_groupStart; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitGroupStart(this);
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
        setState(312);
        match(GROUP);
        setState(314);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la==FOR) {
          {
            setState(313);
            groupType();
          }
        }

        setState(316);
        identifier();
        setState(318);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la==EXTENDS) {
          {
            setState(317);
            groupExtends();
          }
        }

      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class GroupExtendsContext extends ParserRuleContext {
    public TerminalNode EXTENDS() { return getToken(FhirMapJavaParser.EXTENDS, 0); }
    public IdentifierContext identifier() {
      return getRuleContext(IdentifierContext.class,0);
    }
    public GroupExtendsContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_groupExtends; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitGroupExtends(this);
      else return visitor.visitChildren(this);
    }
  }

  public final GroupExtendsContext groupExtends() throws RecognitionException {
    GroupExtendsContext _localctx = new GroupExtendsContext(_ctx, getState());
    enterRule(_localctx, 22, RULE_groupExtends);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(320);
        match(EXTENDS);
        setState(321);
        identifier();
      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class GroupEndContext extends ParserRuleContext {
    public TerminalNode ENDGROUP() { return getToken(FhirMapJavaParser.ENDGROUP, 0); }
    public GroupEndContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_groupEnd; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitGroupEnd(this);
      else return visitor.visitChildren(this);
    }
  }

  public final GroupEndContext groupEnd() throws RecognitionException {
    GroupEndContext _localctx = new GroupEndContext(_ctx, getState());
    enterRule(_localctx, 24, RULE_groupEnd);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(323);
        match(ENDGROUP);
      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class GroupTypeContext extends ParserRuleContext {
    public TerminalNode FOR() { return getToken(FhirMapJavaParser.FOR, 0); }
    public GroupTypeValueContext groupTypeValue() {
      return getRuleContext(GroupTypeValueContext.class,0);
    }
    public GroupTypeContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_groupType; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitGroupType(this);
      else return visitor.visitChildren(this);
    }
  }

  public final GroupTypeContext groupType() throws RecognitionException {
    GroupTypeContext _localctx = new GroupTypeContext(_ctx, getState());
    enterRule(_localctx, 26, RULE_groupType);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(325);
        match(FOR);
        setState(326);
        groupTypeValue();
      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class GroupTypeValueContext extends ParserRuleContext {
    public GroupTypeTypeContext groupTypeType() {
      return getRuleContext(GroupTypeTypeContext.class,0);
    }
    public GroupTypeTypeTypesContext groupTypeTypeTypes() {
      return getRuleContext(GroupTypeTypeTypesContext.class,0);
    }
    public GroupTypeValueContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_groupTypeValue; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitGroupTypeValue(this);
      else return visitor.visitChildren(this);
    }
  }

  public final GroupTypeValueContext groupTypeValue() throws RecognitionException {
    GroupTypeValueContext _localctx = new GroupTypeValueContext(_ctx, getState());
    enterRule(_localctx, 28, RULE_groupTypeValue);
    try {
      setState(330);
      _errHandler.sync(this);
      switch (_input.LA(1)) {
        case TYPES:
          enterOuterAlt(_localctx, 1);
        {
          setState(328);
          groupTypeType();
        }
        break;
        case TYPE_TYPES:
          enterOuterAlt(_localctx, 2);
        {
          setState(329);
          groupTypeTypeTypes();
        }
        break;
        default:
          throw new NoViableAltException(this);
      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class GroupTypeTypeContext extends ParserRuleContext {
    public TerminalNode TYPES() { return getToken(FhirMapJavaParser.TYPES, 0); }
    public GroupTypeTypeContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_groupTypeType; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitGroupTypeType(this);
      else return visitor.visitChildren(this);
    }
  }

  public final GroupTypeTypeContext groupTypeType() throws RecognitionException {
    GroupTypeTypeContext _localctx = new GroupTypeTypeContext(_ctx, getState());
    enterRule(_localctx, 30, RULE_groupTypeType);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(332);
        match(TYPES);
      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class GroupTypeTypeTypesContext extends ParserRuleContext {
    public TerminalNode TYPE_TYPES() { return getToken(FhirMapJavaParser.TYPE_TYPES, 0); }
    public GroupTypeTypeTypesContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_groupTypeTypeTypes; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitGroupTypeTypeTypes(this);
      else return visitor.visitChildren(this);
    }
  }

  public final GroupTypeTypeTypesContext groupTypeTypeTypes() throws RecognitionException {
    GroupTypeTypeTypesContext _localctx = new GroupTypeTypeTypesContext(_ctx, getState());
    enterRule(_localctx, 32, RULE_groupTypeTypeTypes);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(334);
        match(TYPE_TYPES);
      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class GroupInputContext extends ParserRuleContext {
    public TerminalNode INPUT() { return getToken(FhirMapJavaParser.INPUT, 0); }
    public GroupInputNameContext groupInputName() {
      return getRuleContext(GroupInputNameContext.class,0);
    }
    public GroupInputTypeContext groupInputType() {
      return getRuleContext(GroupInputTypeContext.class,0);
    }
    public GroupInputModeContext groupInputMode() {
      return getRuleContext(GroupInputModeContext.class,0);
    }
    public GroupInputContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_groupInput; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitGroupInput(this);
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
        setState(336);
        match(INPUT);
        setState(337);
        groupInputName();
        setState(338);
        match(T__2);
        setState(339);
        groupInputType();
        setState(341);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la==AS) {
          {
            setState(340);
            groupInputMode();
          }
        }

        setState(344);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la==T__1) {
          {
            setState(343);
            match(T__1);
          }
        }

      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class GroupInputNameContext extends ParserRuleContext {
    public IdentifierContext identifier() {
      return getRuleContext(IdentifierContext.class,0);
    }
    public GroupInputNameContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_groupInputName; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitGroupInputName(this);
      else return visitor.visitChildren(this);
    }
  }

  public final GroupInputNameContext groupInputName() throws RecognitionException {
    GroupInputNameContext _localctx = new GroupInputNameContext(_ctx, getState());
    enterRule(_localctx, 36, RULE_groupInputName);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(346);
        identifier();
      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class GroupInputTypeContext extends ParserRuleContext {
    public IdentifierContext identifier() {
      return getRuleContext(IdentifierContext.class,0);
    }
    public GroupInputTypeContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_groupInputType; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitGroupInputType(this);
      else return visitor.visitChildren(this);
    }
  }

  public final GroupInputTypeContext groupInputType() throws RecognitionException {
    GroupInputTypeContext _localctx = new GroupInputTypeContext(_ctx, getState());
    enterRule(_localctx, 38, RULE_groupInputType);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(348);
        identifier();
      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class GroupInputModeContext extends ParserRuleContext {
    public TerminalNode AS() { return getToken(FhirMapJavaParser.AS, 0); }
    public GroupInputModesContext groupInputModes() {
      return getRuleContext(GroupInputModesContext.class,0);
    }
    public GroupInputModeContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_groupInputMode; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitGroupInputMode(this);
      else return visitor.visitChildren(this);
    }
  }

  public final GroupInputModeContext groupInputMode() throws RecognitionException {
    GroupInputModeContext _localctx = new GroupInputModeContext(_ctx, getState());
    enterRule(_localctx, 40, RULE_groupInputMode);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(350);
        match(AS);
        setState(351);
        groupInputModes();
      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class GroupInputModesContext extends ParserRuleContext {
    public GroupInputModesSourceContext groupInputModesSource() {
      return getRuleContext(GroupInputModesSourceContext.class,0);
    }
    public GroupInputModesTargetContext groupInputModesTarget() {
      return getRuleContext(GroupInputModesTargetContext.class,0);
    }
    public GroupInputModesContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_groupInputModes; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitGroupInputModes(this);
      else return visitor.visitChildren(this);
    }
  }

  public final GroupInputModesContext groupInputModes() throws RecognitionException {
    GroupInputModesContext _localctx = new GroupInputModesContext(_ctx, getState());
    enterRule(_localctx, 42, RULE_groupInputModes);
    try {
      setState(355);
      _errHandler.sync(this);
      switch (_input.LA(1)) {
        case SOURCE:
          enterOuterAlt(_localctx, 1);
        {
          setState(353);
          groupInputModesSource();
        }
        break;
        case TARGET:
          enterOuterAlt(_localctx, 2);
        {
          setState(354);
          groupInputModesTarget();
        }
        break;
        default:
          throw new NoViableAltException(this);
      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class GroupInputModesSourceContext extends ParserRuleContext {
    public TerminalNode SOURCE() { return getToken(FhirMapJavaParser.SOURCE, 0); }
    public GroupInputModesSourceContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_groupInputModesSource; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitGroupInputModesSource(this);
      else return visitor.visitChildren(this);
    }
  }

  public final GroupInputModesSourceContext groupInputModesSource() throws RecognitionException {
    GroupInputModesSourceContext _localctx = new GroupInputModesSourceContext(_ctx, getState());
    enterRule(_localctx, 44, RULE_groupInputModesSource);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(357);
        match(SOURCE);
      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class GroupInputModesTargetContext extends ParserRuleContext {
    public TerminalNode TARGET() { return getToken(FhirMapJavaParser.TARGET, 0); }
    public GroupInputModesTargetContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_groupInputModesTarget; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitGroupInputModesTarget(this);
      else return visitor.visitChildren(this);
    }
  }

  public final GroupInputModesTargetContext groupInputModesTarget() throws RecognitionException {
    GroupInputModesTargetContext _localctx = new GroupInputModesTargetContext(_ctx, getState());
    enterRule(_localctx, 46, RULE_groupInputModesTarget);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(359);
        match(TARGET);
      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class GroupItemContext extends ParserRuleContext {
    public GroupCallContext groupCall() {
      return getRuleContext(GroupCallContext.class,0);
    }
    public RuleInstanceContext ruleInstance() {
      return getRuleContext(RuleInstanceContext.class,0);
    }
    public GroupItemContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_groupItem; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitGroupItem(this);
      else return visitor.visitChildren(this);
    }
  }

  public final GroupItemContext groupItem() throws RecognitionException {
    GroupItemContext _localctx = new GroupItemContext(_ctx, getState());
    enterRule(_localctx, 48, RULE_groupItem);
    try {
      setState(363);
      _errHandler.sync(this);
      switch ( getInterpreter().adaptivePredict(_input,13,_ctx) ) {
        case 1:
          enterOuterAlt(_localctx, 1);
        {
          setState(361);
          groupCall();
        }
        break;
        case 2:
          enterOuterAlt(_localctx, 2);
        {
          setState(362);
          ruleInstance();
        }
        break;
      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class GroupCallContext extends ParserRuleContext {
    public IdentifierContext identifier() {
      return getRuleContext(IdentifierContext.class,0);
    }
    public GroupCallParametersContext groupCallParameters() {
      return getRuleContext(GroupCallParametersContext.class,0);
    }
    public GroupCallContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_groupCall; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitGroupCall(this);
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
        setState(365);
        identifier();
        setState(366);
        match(T__3);
        setState(368);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << SOURCE) | (1L << TARGET) | (1L << QUERIED) | (1L << PRODUCED) | (1L << APPEND) | (1L << CAST) | (1L << C) | (1L << CC) | (1L << CODE) | (1L << CP) | (1L << CODEABLECONCEPT) | (1L << CODING) | (1L << COPY) | (1L << CREATE) | (1L << DATEOP) | (1L << DEFAULT) | (1L << DISPLAY) | (1L << ESCAPE) | (1L << EXTENSION) | (1L << EVALUATE) | (1L << ID) | (1L << MAP) | (1L << POINTER) | (1L << QTY) | (1L << REFERENCE) | (1L << SYSTEM) | (1L << TRANSLATE) | (1L << TRUNCATE) | (1L << TYPES) | (1L << UUID) | (1L << IDENTIFIER))) != 0)) {
          {
            setState(367);
            groupCallParameters();
          }
        }

        setState(370);
        match(T__4);
      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class GroupCallParametersContext extends ParserRuleContext {
    public List<IdentifierContext> identifier() {
      return getRuleContexts(IdentifierContext.class);
    }
    public IdentifierContext identifier(int i) {
      return getRuleContext(IdentifierContext.class,i);
    }
    public GroupCallParametersContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_groupCallParameters; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitGroupCallParameters(this);
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
        setState(372);
        identifier();
        setState(377);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la==T__5) {
          {
            {
              setState(373);
              match(T__5);
              setState(374);
              identifier();
            }
          }
          setState(379);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleInstanceContext extends ParserRuleContext {
    public RuleNameContext ruleName() {
      return getRuleContext(RuleNameContext.class,0);
    }
    public TerminalNode FOR() { return getToken(FhirMapJavaParser.FOR, 0); }
    public RuleSourcesContext ruleSources() {
      return getRuleContext(RuleSourcesContext.class,0);
    }
    public RuleMakeContext ruleMake() {
      return getRuleContext(RuleMakeContext.class,0);
    }
    public RuleInstanceContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_ruleInstance; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitRuleInstance(this);
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
        setState(380);
        ruleName();
        setState(381);
        match(T__2);
        setState(382);
        match(FOR);
        setState(383);
        ruleSources();
        setState(385);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la==MAKE) {
          {
            setState(384);
            ruleMake();
          }
        }

      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleNameContext extends ParserRuleContext {
    public List<IdentifierContext> identifier() {
      return getRuleContexts(IdentifierContext.class);
    }
    public IdentifierContext identifier(int i) {
      return getRuleContext(IdentifierContext.class,i);
    }
    public RuleNameContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_ruleName; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitRuleName(this);
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
        setState(387);
        identifier();
        setState(392);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la==T__6) {
          {
            {
              setState(388);
              match(T__6);
              setState(389);
              identifier();
            }
          }
          setState(394);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleSourcesContext extends ParserRuleContext {
    public List<RuleSourceContext> ruleSource() {
      return getRuleContexts(RuleSourceContext.class);
    }
    public RuleSourceContext ruleSource(int i) {
      return getRuleContext(RuleSourceContext.class,i);
    }
    public RuleSourcesContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_ruleSources; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitRuleSources(this);
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
        setState(395);
        ruleSource();
        setState(400);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la==T__5) {
          {
            {
              setState(396);
              match(T__5);
              setState(397);
              ruleSource();
            }
          }
          setState(402);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleSourceContext extends ParserRuleContext {
    public RuleContextContext ruleContext() {
      return getRuleContext(RuleContextContext.class,0);
    }
    public RuleTypeContext ruleType() {
      return getRuleContext(RuleTypeContext.class,0);
    }
    public RuleDefaultContext ruleDefault() {
      return getRuleContext(RuleDefaultContext.class,0);
    }
    public RuleListOptionContext ruleListOption() {
      return getRuleContext(RuleListOptionContext.class,0);
    }
    public RuleVariableContext ruleVariable() {
      return getRuleContext(RuleVariableContext.class,0);
    }
    public RuleWherePathContext ruleWherePath() {
      return getRuleContext(RuleWherePathContext.class,0);
    }
    public RuleCheckPathContext ruleCheckPath() {
      return getRuleContext(RuleCheckPathContext.class,0);
    }
    public RuleSourceContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_ruleSource; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitRuleSource(this);
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
        setState(403);
        ruleContext();
        setState(405);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la==T__2) {
          {
            setState(404);
            ruleType();
          }
        }

        setState(408);
        _errHandler.sync(this);
        switch ( getInterpreter().adaptivePredict(_input,20,_ctx) ) {
          case 1:
          {
            setState(407);
            ruleDefault();
          }
          break;
        }
        setState(411);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la==T__8) {
          {
            setState(410);
            ruleListOption();
          }
        }

        setState(414);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la==AS) {
          {
            setState(413);
            ruleVariable();
          }
        }

        setState(417);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la==WHERE) {
          {
            setState(416);
            ruleWherePath();
          }
        }

        setState(420);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la==CHECK) {
          {
            setState(419);
            ruleCheckPath();
          }
        }

      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleContextContext extends ParserRuleContext {
    public List<RuleContextElementContext> ruleContextElement() {
      return getRuleContexts(RuleContextElementContext.class);
    }
    public RuleContextElementContext ruleContextElement(int i) {
      return getRuleContext(RuleContextElementContext.class,i);
    }
    public RuleContextContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_ruleContext; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitRuleContext(this);
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
        setState(422);
        ruleContextElement();
        setState(427);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la==T__6) {
          {
            {
              setState(423);
              match(T__6);
              setState(424);
              ruleContextElement();
            }
          }
          setState(429);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleContextElementContext extends ParserRuleContext {
    public IdentifierContext identifier() {
      return getRuleContext(IdentifierContext.class,0);
    }
    public QuotedStringWQuotesContext quotedStringWQuotes() {
      return getRuleContext(QuotedStringWQuotesContext.class,0);
    }
    public RuleContextElementContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_ruleContextElement; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitRuleContextElement(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleContextElementContext ruleContextElement() throws RecognitionException {
    RuleContextElementContext _localctx = new RuleContextElementContext(_ctx, getState());
    enterRule(_localctx, 64, RULE_ruleContextElement);
    try {
      setState(432);
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
          setState(430);
          identifier();
        }
        break;
        case QIDENTIFIER:
        case QSTRING:
          enterOuterAlt(_localctx, 2);
        {
          setState(431);
          quotedStringWQuotes();
        }
        break;
        default:
          throw new NoViableAltException(this);
      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTypeContext extends ParserRuleContext {
    public IdentifierContext identifier() {
      return getRuleContext(IdentifierContext.class,0);
    }
    public List<IntegerContext> integer() {
      return getRuleContexts(IntegerContext.class);
    }
    public IntegerContext integer(int i) {
      return getRuleContext(IntegerContext.class,i);
    }
    public RuleTypeContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_ruleType; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitRuleType(this);
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
        setState(434);
        match(T__2);
        setState(435);
        identifier();
        setState(440);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la==DIGITS) {
          {
            setState(436);
            integer();
            setState(437);
            match(T__7);
            setState(438);
            integer();
          }
        }

      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleDefaultContext extends ParserRuleContext {
    public TerminalNode DEFAULT() { return getToken(FhirMapJavaParser.DEFAULT, 0); }
    public IdentifierContext identifier() {
      return getRuleContext(IdentifierContext.class,0);
    }
    public RuleDefaultContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_ruleDefault; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitRuleDefault(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleDefaultContext ruleDefault() throws RecognitionException {
    RuleDefaultContext _localctx = new RuleDefaultContext(_ctx, getState());
    enterRule(_localctx, 68, RULE_ruleDefault);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(442);
        match(DEFAULT);
        setState(443);
        identifier();
      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleListOptionContext extends ParserRuleContext {
    public RuleListOptionContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_ruleListOption; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitRuleListOption(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleListOptionContext ruleListOption() throws RecognitionException {
    RuleListOptionContext _localctx = new RuleListOptionContext(_ctx, getState());
    enterRule(_localctx, 70, RULE_ruleListOption);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(445);
        match(T__8);
      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleVariableContext extends ParserRuleContext {
    public TerminalNode AS() { return getToken(FhirMapJavaParser.AS, 0); }
    public IdentifierContext identifier() {
      return getRuleContext(IdentifierContext.class,0);
    }
    public RuleVariableContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_ruleVariable; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitRuleVariable(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleVariableContext ruleVariable() throws RecognitionException {
    RuleVariableContext _localctx = new RuleVariableContext(_ctx, getState());
    enterRule(_localctx, 72, RULE_ruleVariable);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(447);
        match(AS);
        setState(448);
        identifier();
      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleWherePathContext extends ParserRuleContext {
    public TerminalNode WHERE() { return getToken(FhirMapJavaParser.WHERE, 0); }
    public FhirPathContext fhirPath() {
      return getRuleContext(FhirPathContext.class,0);
    }
    public RuleWherePathContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_ruleWherePath; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitRuleWherePath(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleWherePathContext ruleWherePath() throws RecognitionException {
    RuleWherePathContext _localctx = new RuleWherePathContext(_ctx, getState());
    enterRule(_localctx, 74, RULE_ruleWherePath);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(450);
        match(WHERE);
        setState(451);
        fhirPath();
      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleCheckPathContext extends ParserRuleContext {
    public TerminalNode CHECK() { return getToken(FhirMapJavaParser.CHECK, 0); }
    public FhirPathContext fhirPath() {
      return getRuleContext(FhirPathContext.class,0);
    }
    public RuleCheckPathContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_ruleCheckPath; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitRuleCheckPath(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleCheckPathContext ruleCheckPath() throws RecognitionException {
    RuleCheckPathContext _localctx = new RuleCheckPathContext(_ctx, getState());
    enterRule(_localctx, 76, RULE_ruleCheckPath);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(453);
        match(CHECK);
        setState(454);
        fhirPath();
      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleMakeContext extends ParserRuleContext {
    public TerminalNode MAKE() { return getToken(FhirMapJavaParser.MAKE, 0); }
    public RuleMakeTargetsContext ruleMakeTargets() {
      return getRuleContext(RuleMakeTargetsContext.class,0);
    }
    public RuleMakeDependentsContext ruleMakeDependents() {
      return getRuleContext(RuleMakeDependentsContext.class,0);
    }
    public RuleMakeContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_ruleMake; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitRuleMake(this);
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
        setState(456);
        match(MAKE);
        setState(457);
        ruleMakeTargets();
        setState(459);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la==THEN) {
          {
            setState(458);
            ruleMakeDependents();
          }
        }

      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleMakeTargetsContext extends ParserRuleContext {
    public List<RuleTargetContext> ruleTarget() {
      return getRuleContexts(RuleTargetContext.class);
    }
    public RuleTargetContext ruleTarget(int i) {
      return getRuleContext(RuleTargetContext.class,i);
    }
    public RuleMakeTargetsContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_ruleMakeTargets; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitRuleMakeTargets(this);
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
        setState(461);
        ruleTarget();
        setState(466);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la==T__5) {
          {
            {
              setState(462);
              match(T__5);
              setState(463);
              ruleTarget();
            }
          }
          setState(468);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleMakeDependentsContext extends ParserRuleContext {
    public TerminalNode THEN() { return getToken(FhirMapJavaParser.THEN, 0); }
    public RuleMakeDependentsGroupItemsContext ruleMakeDependentsGroupItems() {
      return getRuleContext(RuleMakeDependentsGroupItemsContext.class,0);
    }
    public RuleMakeDependentsContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_ruleMakeDependents; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitRuleMakeDependents(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleMakeDependentsContext ruleMakeDependents() throws RecognitionException {
    RuleMakeDependentsContext _localctx = new RuleMakeDependentsContext(_ctx, getState());
    enterRule(_localctx, 82, RULE_ruleMakeDependents);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(469);
        match(THEN);
        setState(470);
        match(T__9);
        setState(471);
        ruleMakeDependentsGroupItems();
        setState(472);
        match(T__10);
      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleMakeDependentsGroupItemsContext extends ParserRuleContext {
    public List<GroupItemContext> groupItem() {
      return getRuleContexts(GroupItemContext.class);
    }
    public GroupItemContext groupItem(int i) {
      return getRuleContext(GroupItemContext.class,i);
    }
    public RuleMakeDependentsGroupItemsContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_ruleMakeDependentsGroupItems; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitRuleMakeDependentsGroupItems(this);
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
        setState(477);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << SOURCE) | (1L << TARGET) | (1L << QUERIED) | (1L << PRODUCED) | (1L << APPEND) | (1L << CAST) | (1L << C) | (1L << CC) | (1L << CODE) | (1L << CP) | (1L << CODEABLECONCEPT) | (1L << CODING) | (1L << COPY) | (1L << CREATE) | (1L << DATEOP) | (1L << DEFAULT) | (1L << DISPLAY) | (1L << ESCAPE) | (1L << EXTENSION) | (1L << EVALUATE) | (1L << ID) | (1L << MAP) | (1L << POINTER) | (1L << QTY) | (1L << REFERENCE) | (1L << SYSTEM) | (1L << TRANSLATE) | (1L << TRUNCATE) | (1L << TYPES) | (1L << UUID) | (1L << IDENTIFIER))) != 0)) {
          {
            {
              setState(474);
              groupItem();
            }
          }
          setState(479);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetContext extends ParserRuleContext {
    public RuleTargetAppendContext ruleTargetAppend() {
      return getRuleContext(RuleTargetAppendContext.class,0);
    }
    public RuleTargetAsContext ruleTargetAs() {
      return getRuleContext(RuleTargetAsContext.class,0);
    }
    public RuleTargetAssignContext ruleTargetAssign() {
      return getRuleContext(RuleTargetAssignContext.class,0);
    }
    public RuleTargetCContext ruleTargetC() {
      return getRuleContext(RuleTargetCContext.class,0);
    }
    public RuleTargetCastContext ruleTargetCast() {
      return getRuleContext(RuleTargetCastContext.class,0);
    }
    public RuleTargetCCContext ruleTargetCC() {
      return getRuleContext(RuleTargetCCContext.class,0);
    }
    public RuleTargetCpContext ruleTargetCp() {
      return getRuleContext(RuleTargetCpContext.class,0);
    }
    public RuleTargetCopyContext ruleTargetCopy() {
      return getRuleContext(RuleTargetCopyContext.class,0);
    }
    public RuleTargetCreateContext ruleTargetCreate() {
      return getRuleContext(RuleTargetCreateContext.class,0);
    }
    public RuleTargetDateOpContext ruleTargetDateOp() {
      return getRuleContext(RuleTargetDateOpContext.class,0);
    }
    public RuleTargetExtensionContext ruleTargetExtension() {
      return getRuleContext(RuleTargetExtensionContext.class,0);
    }
    public RuleTargetEscapeContext ruleTargetEscape() {
      return getRuleContext(RuleTargetEscapeContext.class,0);
    }
    public RuleTargetEvaluateContext ruleTargetEvaluate() {
      return getRuleContext(RuleTargetEvaluateContext.class,0);
    }
    public RuleTargetIdContext ruleTargetId() {
      return getRuleContext(RuleTargetIdContext.class,0);
    }
    public RuleTargetPointerContext ruleTargetPointer() {
      return getRuleContext(RuleTargetPointerContext.class,0);
    }
    public RuleTargetQtyContext ruleTargetQty() {
      return getRuleContext(RuleTargetQtyContext.class,0);
    }
    public RuleTargetReferenceContext ruleTargetReference() {
      return getRuleContext(RuleTargetReferenceContext.class,0);
    }
    public RuleTargetTranslateContext ruleTargetTranslate() {
      return getRuleContext(RuleTargetTranslateContext.class,0);
    }
    public RuleTargetTruncateContext ruleTargetTruncate() {
      return getRuleContext(RuleTargetTruncateContext.class,0);
    }
    public RuleTargetUuidContext ruleTargetUuid() {
      return getRuleContext(RuleTargetUuidContext.class,0);
    }
    public RuleTargetContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_ruleTarget; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitRuleTarget(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetContext ruleTarget() throws RecognitionException {
    RuleTargetContext _localctx = new RuleTargetContext(_ctx, getState());
    enterRule(_localctx, 86, RULE_ruleTarget);
    try {
      setState(500);
      _errHandler.sync(this);
      switch ( getInterpreter().adaptivePredict(_input,31,_ctx) ) {
        case 1:
          enterOuterAlt(_localctx, 1);
        {
          setState(480);
          ruleTargetAppend();
        }
        break;
        case 2:
          enterOuterAlt(_localctx, 2);
        {
          setState(481);
          ruleTargetAs();
        }
        break;
        case 3:
          enterOuterAlt(_localctx, 3);
        {
          setState(482);
          ruleTargetAssign();
        }
        break;
        case 4:
          enterOuterAlt(_localctx, 4);
        {
          setState(483);
          ruleTargetC();
        }
        break;
        case 5:
          enterOuterAlt(_localctx, 5);
        {
          setState(484);
          ruleTargetCast();
        }
        break;
        case 6:
          enterOuterAlt(_localctx, 6);
        {
          setState(485);
          ruleTargetCC();
        }
        break;
        case 7:
          enterOuterAlt(_localctx, 7);
        {
          setState(486);
          ruleTargetCp();
        }
        break;
        case 8:
          enterOuterAlt(_localctx, 8);
        {
          setState(487);
          ruleTargetCopy();
        }
        break;
        case 9:
          enterOuterAlt(_localctx, 9);
        {
          setState(488);
          ruleTargetCreate();
        }
        break;
        case 10:
          enterOuterAlt(_localctx, 10);
        {
          setState(489);
          ruleTargetDateOp();
        }
        break;
        case 11:
          enterOuterAlt(_localctx, 11);
        {
          setState(490);
          ruleTargetExtension();
        }
        break;
        case 12:
          enterOuterAlt(_localctx, 12);
        {
          setState(491);
          ruleTargetEscape();
        }
        break;
        case 13:
          enterOuterAlt(_localctx, 13);
        {
          setState(492);
          ruleTargetEvaluate();
        }
        break;
        case 14:
          enterOuterAlt(_localctx, 14);
        {
          setState(493);
          ruleTargetId();
        }
        break;
        case 15:
          enterOuterAlt(_localctx, 15);
        {
          setState(494);
          ruleTargetPointer();
        }
        break;
        case 16:
          enterOuterAlt(_localctx, 16);
        {
          setState(495);
          ruleTargetQty();
        }
        break;
        case 17:
          enterOuterAlt(_localctx, 17);
        {
          setState(496);
          ruleTargetReference();
        }
        break;
        case 18:
          enterOuterAlt(_localctx, 18);
        {
          setState(497);
          ruleTargetTranslate();
        }
        break;
        case 19:
          enterOuterAlt(_localctx, 19);
        {
          setState(498);
          ruleTargetTruncate();
        }
        break;
        case 20:
          enterOuterAlt(_localctx, 20);
        {
          setState(499);
          ruleTargetUuid();
        }
        break;
      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetContextContext extends ParserRuleContext {
    public RuleContextContext ruleContext() {
      return getRuleContext(RuleContextContext.class,0);
    }
    public RuleTargetContextContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_ruleTargetContext; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitRuleTargetContext(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetContextContext ruleTargetContext() throws RecognitionException {
    RuleTargetContextContext _localctx = new RuleTargetContextContext(_ctx, getState());
    enterRule(_localctx, 88, RULE_ruleTargetContext);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(502);
        ruleContext();
        setState(503);
        match(T__0);
      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetAsContext extends ParserRuleContext {
    public RuleContextContext ruleContext() {
      return getRuleContext(RuleContextContext.class,0);
    }
    public RuleTargetVariableContext ruleTargetVariable() {
      return getRuleContext(RuleTargetVariableContext.class,0);
    }
    public RuleTargetAsContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_ruleTargetAs; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitRuleTargetAs(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetAsContext ruleTargetAs() throws RecognitionException {
    RuleTargetAsContext _localctx = new RuleTargetAsContext(_ctx, getState());
    enterRule(_localctx, 90, RULE_ruleTargetAs);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(505);
        ruleContext();
        setState(506);
        ruleTargetVariable();
      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetAssignContext extends ParserRuleContext {
    public RuleTargetContextContext ruleTargetContext() {
      return getRuleContext(RuleTargetContextContext.class,0);
    }
    public RuleTargetAssignValueContext ruleTargetAssignValue() {
      return getRuleContext(RuleTargetAssignValueContext.class,0);
    }
    public RuleTargetVariableContext ruleTargetVariable() {
      return getRuleContext(RuleTargetVariableContext.class,0);
    }
    public RuleTargetAssignContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_ruleTargetAssign; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitRuleTargetAssign(this);
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
        setState(508);
        ruleTargetContext();
        setState(509);
        ruleTargetAssignValue();
        setState(511);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la==AS) {
          {
            setState(510);
            ruleTargetVariable();
          }
        }

      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetAssignValueContext extends ParserRuleContext {
    public QuotedStringWQuotesContext quotedStringWQuotes() {
      return getRuleContext(QuotedStringWQuotesContext.class,0);
    }
    public IdentifierContext identifier() {
      return getRuleContext(IdentifierContext.class,0);
    }
    public RuleTargetAssignValueContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_ruleTargetAssignValue; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitRuleTargetAssignValue(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetAssignValueContext ruleTargetAssignValue() throws RecognitionException {
    RuleTargetAssignValueContext _localctx = new RuleTargetAssignValueContext(_ctx, getState());
    enterRule(_localctx, 94, RULE_ruleTargetAssignValue);
    try {
      setState(515);
      _errHandler.sync(this);
      switch (_input.LA(1)) {
        case QIDENTIFIER:
        case QSTRING:
          enterOuterAlt(_localctx, 1);
        {
          setState(513);
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
          setState(514);
          identifier();
        }
        break;
        default:
          throw new NoViableAltException(this);
      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetAppendContext extends ParserRuleContext {
    public TerminalNode APPEND() { return getToken(FhirMapJavaParser.APPEND, 0); }
    public RuleTargetAppendSourcesContext ruleTargetAppendSources() {
      return getRuleContext(RuleTargetAppendSourcesContext.class,0);
    }
    public RuleTargetContextContext ruleTargetContext() {
      return getRuleContext(RuleTargetContextContext.class,0);
    }
    public RuleTargetVariableContext ruleTargetVariable() {
      return getRuleContext(RuleTargetVariableContext.class,0);
    }
    public RuleTargetAppendContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_ruleTargetAppend; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitRuleTargetAppend(this);
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
        setState(518);
        _errHandler.sync(this);
        switch ( getInterpreter().adaptivePredict(_input,34,_ctx) ) {
          case 1:
          {
            setState(517);
            ruleTargetContext();
          }
          break;
        }
        setState(520);
        match(APPEND);
        setState(521);
        match(T__3);
        setState(522);
        ruleTargetAppendSources();
        setState(523);
        match(T__4);
        setState(525);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la==AS) {
          {
            setState(524);
            ruleTargetVariable();
          }
        }

      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetAppendSourcesContext extends ParserRuleContext {
    public List<RuleTargetAppendSourceContext> ruleTargetAppendSource() {
      return getRuleContexts(RuleTargetAppendSourceContext.class);
    }
    public RuleTargetAppendSourceContext ruleTargetAppendSource(int i) {
      return getRuleContext(RuleTargetAppendSourceContext.class,i);
    }
    public RuleTargetAppendSourcesContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_ruleTargetAppendSources; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitRuleTargetAppendSources(this);
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
        setState(527);
        ruleTargetAppendSource();
        setState(532);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la==T__5) {
          {
            {
              setState(528);
              match(T__5);
              setState(529);
              ruleTargetAppendSource();
            }
          }
          setState(534);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetAppendSourceContext extends ParserRuleContext {
    public IdentifierContext identifier() {
      return getRuleContext(IdentifierContext.class,0);
    }
    public QuotedStringWQuotesContext quotedStringWQuotes() {
      return getRuleContext(QuotedStringWQuotesContext.class,0);
    }
    public RuleTargetAppendSourceContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_ruleTargetAppendSource; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitRuleTargetAppendSource(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetAppendSourceContext ruleTargetAppendSource() throws RecognitionException {
    RuleTargetAppendSourceContext _localctx = new RuleTargetAppendSourceContext(_ctx, getState());
    enterRule(_localctx, 100, RULE_ruleTargetAppendSource);
    try {
      setState(537);
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
          setState(535);
          identifier();
        }
        break;
        case QIDENTIFIER:
        case QSTRING:
          enterOuterAlt(_localctx, 2);
        {
          setState(536);
          quotedStringWQuotes();
        }
        break;
        default:
          throw new NoViableAltException(this);
      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetCContext extends ParserRuleContext {
    public TerminalNode C() { return getToken(FhirMapJavaParser.C, 0); }
    public RuleTargetCSystemContext ruleTargetCSystem() {
      return getRuleContext(RuleTargetCSystemContext.class,0);
    }
    public RuleTargetCCodeContext ruleTargetCCode() {
      return getRuleContext(RuleTargetCCodeContext.class,0);
    }
    public RuleTargetContextContext ruleTargetContext() {
      return getRuleContext(RuleTargetContextContext.class,0);
    }
    public RuleTargetCDisplayContext ruleTargetCDisplay() {
      return getRuleContext(RuleTargetCDisplayContext.class,0);
    }
    public RuleTargetVariableContext ruleTargetVariable() {
      return getRuleContext(RuleTargetVariableContext.class,0);
    }
    public RuleTargetCContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_ruleTargetC; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitRuleTargetC(this);
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
        setState(540);
        _errHandler.sync(this);
        switch ( getInterpreter().adaptivePredict(_input,38,_ctx) ) {
          case 1:
          {
            setState(539);
            ruleTargetContext();
          }
          break;
        }
        setState(542);
        match(C);
        setState(543);
        match(T__3);
        setState(544);
        ruleTargetCSystem();
        setState(545);
        match(T__5);
        setState(546);
        ruleTargetCCode();
        setState(549);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la==T__5) {
          {
            setState(547);
            match(T__5);
            setState(548);
            ruleTargetCDisplay();
          }
        }

        setState(551);
        match(T__4);
        setState(553);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la==AS) {
          {
            setState(552);
            ruleTargetVariable();
          }
        }

      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetCSystemContext extends ParserRuleContext {
    public QuotedUrlContext quotedUrl() {
      return getRuleContext(QuotedUrlContext.class,0);
    }
    public RuleTargetCSystemContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_ruleTargetCSystem; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitRuleTargetCSystem(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetCSystemContext ruleTargetCSystem() throws RecognitionException {
    RuleTargetCSystemContext _localctx = new RuleTargetCSystemContext(_ctx, getState());
    enterRule(_localctx, 104, RULE_ruleTargetCSystem);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(555);
        quotedUrl();
      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetCCodeContext extends ParserRuleContext {
    public QuotedStringWQuotesContext quotedStringWQuotes() {
      return getRuleContext(QuotedStringWQuotesContext.class,0);
    }
    public IdentifierContext identifier() {
      return getRuleContext(IdentifierContext.class,0);
    }
    public RuleTargetCCodeContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_ruleTargetCCode; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitRuleTargetCCode(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetCCodeContext ruleTargetCCode() throws RecognitionException {
    RuleTargetCCodeContext _localctx = new RuleTargetCCodeContext(_ctx, getState());
    enterRule(_localctx, 106, RULE_ruleTargetCCode);
    try {
      setState(559);
      _errHandler.sync(this);
      switch (_input.LA(1)) {
        case QIDENTIFIER:
        case QSTRING:
          enterOuterAlt(_localctx, 1);
        {
          setState(557);
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
          setState(558);
          identifier();
        }
        break;
        default:
          throw new NoViableAltException(this);
      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetCDisplayContext extends ParserRuleContext {
    public QuotedStringContext quotedString() {
      return getRuleContext(QuotedStringContext.class,0);
    }
    public RuleTargetCDisplayContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_ruleTargetCDisplay; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitRuleTargetCDisplay(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetCDisplayContext ruleTargetCDisplay() throws RecognitionException {
    RuleTargetCDisplayContext _localctx = new RuleTargetCDisplayContext(_ctx, getState());
    enterRule(_localctx, 108, RULE_ruleTargetCDisplay);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(561);
        quotedString();
      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetCastContext extends ParserRuleContext {
    public TerminalNode CAST() { return getToken(FhirMapJavaParser.CAST, 0); }
    public RuleTargetCastSourceContext ruleTargetCastSource() {
      return getRuleContext(RuleTargetCastSourceContext.class,0);
    }
    public RuleTargetContextContext ruleTargetContext() {
      return getRuleContext(RuleTargetContextContext.class,0);
    }
    public RuleTargetCastTypeContext ruleTargetCastType() {
      return getRuleContext(RuleTargetCastTypeContext.class,0);
    }
    public RuleTargetVariableContext ruleTargetVariable() {
      return getRuleContext(RuleTargetVariableContext.class,0);
    }
    public RuleTargetCastContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_ruleTargetCast; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitRuleTargetCast(this);
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
        setState(564);
        _errHandler.sync(this);
        switch ( getInterpreter().adaptivePredict(_input,42,_ctx) ) {
          case 1:
          {
            setState(563);
            ruleTargetContext();
          }
          break;
        }
        setState(566);
        match(CAST);
        setState(567);
        match(T__3);
        setState(568);
        ruleTargetCastSource();
        setState(571);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la==T__5) {
          {
            setState(569);
            match(T__5);
            setState(570);
            ruleTargetCastType();
          }
        }

        setState(573);
        match(T__4);
        setState(575);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la==AS) {
          {
            setState(574);
            ruleTargetVariable();
          }
        }

      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetCastSourceContext extends ParserRuleContext {
    public IdentifierContext identifier() {
      return getRuleContext(IdentifierContext.class,0);
    }
    public RuleTargetCastSourceContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_ruleTargetCastSource; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitRuleTargetCastSource(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetCastSourceContext ruleTargetCastSource() throws RecognitionException {
    RuleTargetCastSourceContext _localctx = new RuleTargetCastSourceContext(_ctx, getState());
    enterRule(_localctx, 112, RULE_ruleTargetCastSource);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(577);
        identifier();
      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetCastTypeContext extends ParserRuleContext {
    public IdentifierContext identifier() {
      return getRuleContext(IdentifierContext.class,0);
    }
    public RuleTargetCastTypeContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_ruleTargetCastType; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitRuleTargetCastType(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetCastTypeContext ruleTargetCastType() throws RecognitionException {
    RuleTargetCastTypeContext _localctx = new RuleTargetCastTypeContext(_ctx, getState());
    enterRule(_localctx, 114, RULE_ruleTargetCastType);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(579);
        identifier();
      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetCCContext extends ParserRuleContext {
    public RuleTargetCC1Context ruleTargetCC1() {
      return getRuleContext(RuleTargetCC1Context.class,0);
    }
    public RuleTargetCC2Context ruleTargetCC2() {
      return getRuleContext(RuleTargetCC2Context.class,0);
    }
    public RuleTargetVariableContext ruleTargetVariable() {
      return getRuleContext(RuleTargetVariableContext.class,0);
    }
    public RuleTargetCCContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_ruleTargetCC; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitRuleTargetCC(this);
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
        setState(583);
        _errHandler.sync(this);
        switch ( getInterpreter().adaptivePredict(_input,45,_ctx) ) {
          case 1:
          {
            setState(581);
            ruleTargetCC1();
          }
          break;
          case 2:
          {
            setState(582);
            ruleTargetCC2();
          }
          break;
        }
        setState(586);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la==AS) {
          {
            setState(585);
            ruleTargetVariable();
          }
        }

      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetCC1Context extends ParserRuleContext {
    public TerminalNode CC() { return getToken(FhirMapJavaParser.CC, 0); }
    public RuleTargetCC1TextContext ruleTargetCC1Text() {
      return getRuleContext(RuleTargetCC1TextContext.class,0);
    }
    public RuleTargetContextContext ruleTargetContext() {
      return getRuleContext(RuleTargetContextContext.class,0);
    }
    public RuleTargetVariableContext ruleTargetVariable() {
      return getRuleContext(RuleTargetVariableContext.class,0);
    }
    public RuleTargetCC1Context(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_ruleTargetCC1; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitRuleTargetCC1(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetCC1Context ruleTargetCC1() throws RecognitionException {
    RuleTargetCC1Context _localctx = new RuleTargetCC1Context(_ctx, getState());
    enterRule(_localctx, 118, RULE_ruleTargetCC1);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(589);
        _errHandler.sync(this);
        switch ( getInterpreter().adaptivePredict(_input,47,_ctx) ) {
          case 1:
          {
            setState(588);
            ruleTargetContext();
          }
          break;
        }
        setState(591);
        match(CC);
        setState(592);
        match(T__3);
        setState(593);
        ruleTargetCC1Text();
        setState(594);
        match(T__4);
        setState(596);
        _errHandler.sync(this);
        switch ( getInterpreter().adaptivePredict(_input,48,_ctx) ) {
          case 1:
          {
            setState(595);
            ruleTargetVariable();
          }
          break;
        }
      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetCC1TextContext extends ParserRuleContext {
    public QuotedStringContext quotedString() {
      return getRuleContext(QuotedStringContext.class,0);
    }
    public RuleTargetCC1TextContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_ruleTargetCC1Text; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitRuleTargetCC1Text(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetCC1TextContext ruleTargetCC1Text() throws RecognitionException {
    RuleTargetCC1TextContext _localctx = new RuleTargetCC1TextContext(_ctx, getState());
    enterRule(_localctx, 120, RULE_ruleTargetCC1Text);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(598);
        quotedString();
      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetCC2Context extends ParserRuleContext {
    public TerminalNode CC() { return getToken(FhirMapJavaParser.CC, 0); }
    public RuleTargetCC2SystemContext ruleTargetCC2System() {
      return getRuleContext(RuleTargetCC2SystemContext.class,0);
    }
    public RuleTargetCC2CodeContext ruleTargetCC2Code() {
      return getRuleContext(RuleTargetCC2CodeContext.class,0);
    }
    public RuleTargetContextContext ruleTargetContext() {
      return getRuleContext(RuleTargetContextContext.class,0);
    }
    public RuleTargetCC2DisplayContext ruleTargetCC2Display() {
      return getRuleContext(RuleTargetCC2DisplayContext.class,0);
    }
    public RuleTargetVariableContext ruleTargetVariable() {
      return getRuleContext(RuleTargetVariableContext.class,0);
    }
    public RuleTargetCC2Context(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_ruleTargetCC2; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitRuleTargetCC2(this);
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
        setState(601);
        _errHandler.sync(this);
        switch ( getInterpreter().adaptivePredict(_input,49,_ctx) ) {
          case 1:
          {
            setState(600);
            ruleTargetContext();
          }
          break;
        }
        setState(603);
        match(CC);
        setState(604);
        match(T__3);
        setState(605);
        ruleTargetCC2System();
        setState(606);
        match(T__5);
        setState(607);
        ruleTargetCC2Code();
        setState(610);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la==T__5) {
          {
            setState(608);
            match(T__5);
            setState(609);
            ruleTargetCC2Display();
          }
        }

        setState(612);
        match(T__4);
        setState(614);
        _errHandler.sync(this);
        switch ( getInterpreter().adaptivePredict(_input,51,_ctx) ) {
          case 1:
          {
            setState(613);
            ruleTargetVariable();
          }
          break;
        }
      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetCC2SystemContext extends ParserRuleContext {
    public QuotedUrlContext quotedUrl() {
      return getRuleContext(QuotedUrlContext.class,0);
    }
    public RuleTargetCC2SystemContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_ruleTargetCC2System; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitRuleTargetCC2System(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetCC2SystemContext ruleTargetCC2System() throws RecognitionException {
    RuleTargetCC2SystemContext _localctx = new RuleTargetCC2SystemContext(_ctx, getState());
    enterRule(_localctx, 124, RULE_ruleTargetCC2System);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(616);
        quotedUrl();
      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetCC2CodeContext extends ParserRuleContext {
    public QuotedStringWQuotesContext quotedStringWQuotes() {
      return getRuleContext(QuotedStringWQuotesContext.class,0);
    }
    public IdentifierContext identifier() {
      return getRuleContext(IdentifierContext.class,0);
    }
    public RuleTargetCC2CodeContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_ruleTargetCC2Code; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitRuleTargetCC2Code(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetCC2CodeContext ruleTargetCC2Code() throws RecognitionException {
    RuleTargetCC2CodeContext _localctx = new RuleTargetCC2CodeContext(_ctx, getState());
    enterRule(_localctx, 126, RULE_ruleTargetCC2Code);
    try {
      setState(620);
      _errHandler.sync(this);
      switch (_input.LA(1)) {
        case QIDENTIFIER:
        case QSTRING:
          enterOuterAlt(_localctx, 1);
        {
          setState(618);
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
          setState(619);
          identifier();
        }
        break;
        default:
          throw new NoViableAltException(this);
      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetCC2DisplayContext extends ParserRuleContext {
    public QuotedStringContext quotedString() {
      return getRuleContext(QuotedStringContext.class,0);
    }
    public RuleTargetCC2DisplayContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_ruleTargetCC2Display; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitRuleTargetCC2Display(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetCC2DisplayContext ruleTargetCC2Display() throws RecognitionException {
    RuleTargetCC2DisplayContext _localctx = new RuleTargetCC2DisplayContext(_ctx, getState());
    enterRule(_localctx, 128, RULE_ruleTargetCC2Display);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(622);
        quotedString();
      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetCpContext extends ParserRuleContext {
    public TerminalNode CP() { return getToken(FhirMapJavaParser.CP, 0); }
    public RuleTargetCpVariableContext ruleTargetCpVariable() {
      return getRuleContext(RuleTargetCpVariableContext.class,0);
    }
    public RuleTargetContextContext ruleTargetContext() {
      return getRuleContext(RuleTargetContextContext.class,0);
    }
    public RuleTargetCpSystemContext ruleTargetCpSystem() {
      return getRuleContext(RuleTargetCpSystemContext.class,0);
    }
    public RuleTargetVariableContext ruleTargetVariable() {
      return getRuleContext(RuleTargetVariableContext.class,0);
    }
    public RuleTargetCpContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_ruleTargetCp; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitRuleTargetCp(this);
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
        setState(625);
        _errHandler.sync(this);
        switch ( getInterpreter().adaptivePredict(_input,53,_ctx) ) {
          case 1:
          {
            setState(624);
            ruleTargetContext();
          }
          break;
        }
        setState(627);
        match(CP);
        setState(628);
        match(T__3);
        setState(632);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la==QIDENTIFIER || _la==QSTRING) {
          {
            setState(629);
            ruleTargetCpSystem();
            setState(630);
            match(T__5);
          }
        }

        setState(634);
        ruleTargetCpVariable();
        setState(635);
        match(T__4);
        setState(637);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la==AS) {
          {
            setState(636);
            ruleTargetVariable();
          }
        }

      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetCpSystemContext extends ParserRuleContext {
    public QuotedUrlContext quotedUrl() {
      return getRuleContext(QuotedUrlContext.class,0);
    }
    public RuleTargetCpSystemContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_ruleTargetCpSystem; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitRuleTargetCpSystem(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetCpSystemContext ruleTargetCpSystem() throws RecognitionException {
    RuleTargetCpSystemContext _localctx = new RuleTargetCpSystemContext(_ctx, getState());
    enterRule(_localctx, 132, RULE_ruleTargetCpSystem);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(639);
        quotedUrl();
      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetCpVariableContext extends ParserRuleContext {
    public IdentifierContext identifier() {
      return getRuleContext(IdentifierContext.class,0);
    }
    public RuleTargetCpVariableContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_ruleTargetCpVariable; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitRuleTargetCpVariable(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetCpVariableContext ruleTargetCpVariable() throws RecognitionException {
    RuleTargetCpVariableContext _localctx = new RuleTargetCpVariableContext(_ctx, getState());
    enterRule(_localctx, 134, RULE_ruleTargetCpVariable);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(641);
        identifier();
      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetCopyContext extends ParserRuleContext {
    public RuleTargetContextContext ruleTargetContext() {
      return getRuleContext(RuleTargetContextContext.class,0);
    }
    public TerminalNode COPY() { return getToken(FhirMapJavaParser.COPY, 0); }
    public RuleTargetCopySourceContext ruleTargetCopySource() {
      return getRuleContext(RuleTargetCopySourceContext.class,0);
    }
    public RuleTargetVariableContext ruleTargetVariable() {
      return getRuleContext(RuleTargetVariableContext.class,0);
    }
    public RuleTargetCopyContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_ruleTargetCopy; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitRuleTargetCopy(this);
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
        setState(643);
        ruleTargetContext();
        setState(644);
        match(COPY);
        setState(645);
        match(T__3);
        setState(646);
        ruleTargetCopySource();
        setState(647);
        match(T__4);
        setState(649);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la==AS) {
          {
            setState(648);
            ruleTargetVariable();
          }
        }

      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetCopySourceContext extends ParserRuleContext {
    public IdentifierContext identifier() {
      return getRuleContext(IdentifierContext.class,0);
    }
    public RuleTargetCopySourceContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_ruleTargetCopySource; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitRuleTargetCopySource(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetCopySourceContext ruleTargetCopySource() throws RecognitionException {
    RuleTargetCopySourceContext _localctx = new RuleTargetCopySourceContext(_ctx, getState());
    enterRule(_localctx, 138, RULE_ruleTargetCopySource);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(651);
        identifier();
      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetCreateContext extends ParserRuleContext {
    public TerminalNode CREATE() { return getToken(FhirMapJavaParser.CREATE, 0); }
    public RuleTargetCreateTypeContext ruleTargetCreateType() {
      return getRuleContext(RuleTargetCreateTypeContext.class,0);
    }
    public RuleTargetContextContext ruleTargetContext() {
      return getRuleContext(RuleTargetContextContext.class,0);
    }
    public RuleTargetVariableContext ruleTargetVariable() {
      return getRuleContext(RuleTargetVariableContext.class,0);
    }
    public RuleTargetCreateContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_ruleTargetCreate; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitRuleTargetCreate(this);
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
        setState(654);
        _errHandler.sync(this);
        switch ( getInterpreter().adaptivePredict(_input,57,_ctx) ) {
          case 1:
          {
            setState(653);
            ruleTargetContext();
          }
          break;
        }
        setState(656);
        match(CREATE);
        setState(657);
        match(T__3);
        setState(658);
        ruleTargetCreateType();
        setState(659);
        match(T__4);
        setState(661);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la==AS) {
          {
            setState(660);
            ruleTargetVariable();
          }
        }

      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetCreateTypeContext extends ParserRuleContext {
    public QuotedIdentifierContext quotedIdentifier() {
      return getRuleContext(QuotedIdentifierContext.class,0);
    }
    public RuleTargetCreateTypeContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_ruleTargetCreateType; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitRuleTargetCreateType(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetCreateTypeContext ruleTargetCreateType() throws RecognitionException {
    RuleTargetCreateTypeContext _localctx = new RuleTargetCreateTypeContext(_ctx, getState());
    enterRule(_localctx, 142, RULE_ruleTargetCreateType);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(663);
        quotedIdentifier();
      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetDateOpContext extends ParserRuleContext {
    public TerminalNode DATEOP() { return getToken(FhirMapJavaParser.DATEOP, 0); }
    public RuleTargetDateOpVariableContext ruleTargetDateOpVariable() {
      return getRuleContext(RuleTargetDateOpVariableContext.class,0);
    }
    public RuleTargetDateOpOperationContext ruleTargetDateOpOperation() {
      return getRuleContext(RuleTargetDateOpOperationContext.class,0);
    }
    public RuleTargetContextContext ruleTargetContext() {
      return getRuleContext(RuleTargetContextContext.class,0);
    }
    public RuleTargetDateOpVariable2Context ruleTargetDateOpVariable2() {
      return getRuleContext(RuleTargetDateOpVariable2Context.class,0);
    }
    public RuleTargetVariableContext ruleTargetVariable() {
      return getRuleContext(RuleTargetVariableContext.class,0);
    }
    public RuleTargetDateOpContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_ruleTargetDateOp; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitRuleTargetDateOp(this);
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
        setState(666);
        _errHandler.sync(this);
        switch ( getInterpreter().adaptivePredict(_input,59,_ctx) ) {
          case 1:
          {
            setState(665);
            ruleTargetContext();
          }
          break;
        }
        setState(668);
        match(DATEOP);
        setState(669);
        match(T__3);
        setState(670);
        ruleTargetDateOpVariable();
        setState(671);
        match(T__5);
        setState(672);
        ruleTargetDateOpOperation();
        setState(675);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la==T__5) {
          {
            setState(673);
            match(T__5);
            setState(674);
            ruleTargetDateOpVariable2();
          }
        }

        setState(677);
        match(T__4);
        setState(679);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la==AS) {
          {
            setState(678);
            ruleTargetVariable();
          }
        }

      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetDateOpVariableContext extends ParserRuleContext {
    public IdentifierContext identifier() {
      return getRuleContext(IdentifierContext.class,0);
    }
    public RuleTargetDateOpVariableContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_ruleTargetDateOpVariable; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitRuleTargetDateOpVariable(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetDateOpVariableContext ruleTargetDateOpVariable() throws RecognitionException {
    RuleTargetDateOpVariableContext _localctx = new RuleTargetDateOpVariableContext(_ctx, getState());
    enterRule(_localctx, 146, RULE_ruleTargetDateOpVariable);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(681);
        identifier();
      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetDateOpOperationContext extends ParserRuleContext {
    public QuotedStringContext quotedString() {
      return getRuleContext(QuotedStringContext.class,0);
    }
    public RuleTargetDateOpOperationContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_ruleTargetDateOpOperation; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitRuleTargetDateOpOperation(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetDateOpOperationContext ruleTargetDateOpOperation() throws RecognitionException {
    RuleTargetDateOpOperationContext _localctx = new RuleTargetDateOpOperationContext(_ctx, getState());
    enterRule(_localctx, 148, RULE_ruleTargetDateOpOperation);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(683);
        quotedString();
      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetDateOpVariable2Context extends ParserRuleContext {
    public IdentifierContext identifier() {
      return getRuleContext(IdentifierContext.class,0);
    }
    public RuleTargetDateOpVariable2Context(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_ruleTargetDateOpVariable2; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitRuleTargetDateOpVariable2(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetDateOpVariable2Context ruleTargetDateOpVariable2() throws RecognitionException {
    RuleTargetDateOpVariable2Context _localctx = new RuleTargetDateOpVariable2Context(_ctx, getState());
    enterRule(_localctx, 150, RULE_ruleTargetDateOpVariable2);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(685);
        identifier();
      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetExtensionContext extends ParserRuleContext {
    public TerminalNode EXTENSION() { return getToken(FhirMapJavaParser.EXTENSION, 0); }
    public RuleTargetContextContext ruleTargetContext() {
      return getRuleContext(RuleTargetContextContext.class,0);
    }
    public RuleTargetVariableContext ruleTargetVariable() {
      return getRuleContext(RuleTargetVariableContext.class,0);
    }
    public RuleTargetExtensionContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_ruleTargetExtension; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitRuleTargetExtension(this);
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
        setState(688);
        _errHandler.sync(this);
        switch ( getInterpreter().adaptivePredict(_input,62,_ctx) ) {
          case 1:
          {
            setState(687);
            ruleTargetContext();
          }
          break;
        }
        setState(690);
        match(EXTENSION);
        setState(691);
        match(T__3);
        setState(692);
        match(T__4);
        setState(694);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la==AS) {
          {
            setState(693);
            ruleTargetVariable();
          }
        }

      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetEscapeContext extends ParserRuleContext {
    public TerminalNode ESCAPE() { return getToken(FhirMapJavaParser.ESCAPE, 0); }
    public RuleTargetEscapeVariableContext ruleTargetEscapeVariable() {
      return getRuleContext(RuleTargetEscapeVariableContext.class,0);
    }
    public RuleTargetEscapeString1Context ruleTargetEscapeString1() {
      return getRuleContext(RuleTargetEscapeString1Context.class,0);
    }
    public RuleTargetContextContext ruleTargetContext() {
      return getRuleContext(RuleTargetContextContext.class,0);
    }
    public RuleTargetEscapeString2Context ruleTargetEscapeString2() {
      return getRuleContext(RuleTargetEscapeString2Context.class,0);
    }
    public RuleTargetVariableContext ruleTargetVariable() {
      return getRuleContext(RuleTargetVariableContext.class,0);
    }
    public RuleTargetEscapeContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_ruleTargetEscape; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitRuleTargetEscape(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetEscapeContext ruleTargetEscape() throws RecognitionException {
    RuleTargetEscapeContext _localctx = new RuleTargetEscapeContext(_ctx, getState());
    enterRule(_localctx, 154, RULE_ruleTargetEscape);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(697);
        _errHandler.sync(this);
        switch ( getInterpreter().adaptivePredict(_input,64,_ctx) ) {
          case 1:
          {
            setState(696);
            ruleTargetContext();
          }
          break;
        }
        setState(699);
        match(ESCAPE);
        setState(700);
        match(T__3);
        setState(701);
        ruleTargetEscapeVariable();
        setState(702);
        match(T__5);
        setState(703);
        ruleTargetEscapeString1();
        setState(706);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la==T__5) {
          {
            setState(704);
            match(T__5);
            setState(705);
            ruleTargetEscapeString2();
          }
        }

        setState(708);
        match(T__4);
        setState(710);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la==AS) {
          {
            setState(709);
            ruleTargetVariable();
          }
        }

      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetEscapeVariableContext extends ParserRuleContext {
    public IdentifierContext identifier() {
      return getRuleContext(IdentifierContext.class,0);
    }
    public RuleTargetEscapeVariableContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_ruleTargetEscapeVariable; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitRuleTargetEscapeVariable(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetEscapeVariableContext ruleTargetEscapeVariable() throws RecognitionException {
    RuleTargetEscapeVariableContext _localctx = new RuleTargetEscapeVariableContext(_ctx, getState());
    enterRule(_localctx, 156, RULE_ruleTargetEscapeVariable);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(712);
        identifier();
      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetEscapeString1Context extends ParserRuleContext {
    public QuotedStringContext quotedString() {
      return getRuleContext(QuotedStringContext.class,0);
    }
    public RuleTargetEscapeString1Context(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_ruleTargetEscapeString1; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitRuleTargetEscapeString1(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetEscapeString1Context ruleTargetEscapeString1() throws RecognitionException {
    RuleTargetEscapeString1Context _localctx = new RuleTargetEscapeString1Context(_ctx, getState());
    enterRule(_localctx, 158, RULE_ruleTargetEscapeString1);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(714);
        quotedString();
      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetEscapeString2Context extends ParserRuleContext {
    public QuotedStringContext quotedString() {
      return getRuleContext(QuotedStringContext.class,0);
    }
    public RuleTargetEscapeString2Context(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_ruleTargetEscapeString2; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitRuleTargetEscapeString2(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetEscapeString2Context ruleTargetEscapeString2() throws RecognitionException {
    RuleTargetEscapeString2Context _localctx = new RuleTargetEscapeString2Context(_ctx, getState());
    enterRule(_localctx, 160, RULE_ruleTargetEscapeString2);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(716);
        quotedString();
      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetEvaluateContext extends ParserRuleContext {
    public TerminalNode EVALUATE() { return getToken(FhirMapJavaParser.EVALUATE, 0); }
    public RuleTargetEvaluateObjectContext ruleTargetEvaluateObject() {
      return getRuleContext(RuleTargetEvaluateObjectContext.class,0);
    }
    public RuleTargetEvaluateObjectElementContext ruleTargetEvaluateObjectElement() {
      return getRuleContext(RuleTargetEvaluateObjectElementContext.class,0);
    }
    public RuleTargetContextContext ruleTargetContext() {
      return getRuleContext(RuleTargetContextContext.class,0);
    }
    public RuleTargetVariableContext ruleTargetVariable() {
      return getRuleContext(RuleTargetVariableContext.class,0);
    }
    public RuleTargetEvaluateContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_ruleTargetEvaluate; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitRuleTargetEvaluate(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetEvaluateContext ruleTargetEvaluate() throws RecognitionException {
    RuleTargetEvaluateContext _localctx = new RuleTargetEvaluateContext(_ctx, getState());
    enterRule(_localctx, 162, RULE_ruleTargetEvaluate);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(719);
        _errHandler.sync(this);
        switch ( getInterpreter().adaptivePredict(_input,67,_ctx) ) {
          case 1:
          {
            setState(718);
            ruleTargetContext();
          }
          break;
        }
        setState(721);
        match(EVALUATE);
        setState(722);
        match(T__3);
        setState(723);
        ruleTargetEvaluateObject();
        setState(724);
        match(T__5);
        setState(725);
        ruleTargetEvaluateObjectElement();
        setState(726);
        match(T__4);
        setState(728);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la==AS) {
          {
            setState(727);
            ruleTargetVariable();
          }
        }

      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetEvaluateObjectContext extends ParserRuleContext {
    public IdentifierContext identifier() {
      return getRuleContext(IdentifierContext.class,0);
    }
    public RuleTargetEvaluateObjectContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_ruleTargetEvaluateObject; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitRuleTargetEvaluateObject(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetEvaluateObjectContext ruleTargetEvaluateObject() throws RecognitionException {
    RuleTargetEvaluateObjectContext _localctx = new RuleTargetEvaluateObjectContext(_ctx, getState());
    enterRule(_localctx, 164, RULE_ruleTargetEvaluateObject);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(730);
        identifier();
      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetEvaluateObjectElementContext extends ParserRuleContext {
    public IdentifierContext identifier() {
      return getRuleContext(IdentifierContext.class,0);
    }
    public RuleTargetEvaluateObjectElementContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_ruleTargetEvaluateObjectElement; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitRuleTargetEvaluateObjectElement(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetEvaluateObjectElementContext ruleTargetEvaluateObjectElement() throws RecognitionException {
    RuleTargetEvaluateObjectElementContext _localctx = new RuleTargetEvaluateObjectElementContext(_ctx, getState());
    enterRule(_localctx, 166, RULE_ruleTargetEvaluateObjectElement);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(732);
        identifier();
      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetIdContext extends ParserRuleContext {
    public TerminalNode ID() { return getToken(FhirMapJavaParser.ID, 0); }
    public RuleTargetIdSystemContext ruleTargetIdSystem() {
      return getRuleContext(RuleTargetIdSystemContext.class,0);
    }
    public RuleTargetIdValueContext ruleTargetIdValue() {
      return getRuleContext(RuleTargetIdValueContext.class,0);
    }
    public RuleTargetContextContext ruleTargetContext() {
      return getRuleContext(RuleTargetContextContext.class,0);
    }
    public RuleTargetIdTypeContext ruleTargetIdType() {
      return getRuleContext(RuleTargetIdTypeContext.class,0);
    }
    public RuleTargetVariableContext ruleTargetVariable() {
      return getRuleContext(RuleTargetVariableContext.class,0);
    }
    public RuleTargetIdContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_ruleTargetId; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitRuleTargetId(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetIdContext ruleTargetId() throws RecognitionException {
    RuleTargetIdContext _localctx = new RuleTargetIdContext(_ctx, getState());
    enterRule(_localctx, 168, RULE_ruleTargetId);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(735);
        _errHandler.sync(this);
        switch ( getInterpreter().adaptivePredict(_input,69,_ctx) ) {
          case 1:
          {
            setState(734);
            ruleTargetContext();
          }
          break;
        }
        setState(737);
        match(ID);
        setState(738);
        match(T__3);
        setState(739);
        ruleTargetIdSystem();
        setState(740);
        match(T__5);
        setState(741);
        ruleTargetIdValue();
        setState(744);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la==T__5) {
          {
            setState(742);
            match(T__5);
            setState(743);
            ruleTargetIdType();
          }
        }

        setState(746);
        match(T__4);
        setState(748);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la==AS) {
          {
            setState(747);
            ruleTargetVariable();
          }
        }

      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetIdSystemContext extends ParserRuleContext {
    public QuotedUrlContext quotedUrl() {
      return getRuleContext(QuotedUrlContext.class,0);
    }
    public RuleTargetIdSystemContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_ruleTargetIdSystem; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitRuleTargetIdSystem(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetIdSystemContext ruleTargetIdSystem() throws RecognitionException {
    RuleTargetIdSystemContext _localctx = new RuleTargetIdSystemContext(_ctx, getState());
    enterRule(_localctx, 170, RULE_ruleTargetIdSystem);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(750);
        quotedUrl();
      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetIdValueContext extends ParserRuleContext {
    public IdentifierContext identifier() {
      return getRuleContext(IdentifierContext.class,0);
    }
    public RuleTargetIdValueContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_ruleTargetIdValue; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitRuleTargetIdValue(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetIdValueContext ruleTargetIdValue() throws RecognitionException {
    RuleTargetIdValueContext _localctx = new RuleTargetIdValueContext(_ctx, getState());
    enterRule(_localctx, 172, RULE_ruleTargetIdValue);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(752);
        identifier();
      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetIdTypeContext extends ParserRuleContext {
    public IdentifierContext identifier() {
      return getRuleContext(IdentifierContext.class,0);
    }
    public RuleTargetIdTypeContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_ruleTargetIdType; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitRuleTargetIdType(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetIdTypeContext ruleTargetIdType() throws RecognitionException {
    RuleTargetIdTypeContext _localctx = new RuleTargetIdTypeContext(_ctx, getState());
    enterRule(_localctx, 174, RULE_ruleTargetIdType);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(754);
        identifier();
      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetPointerContext extends ParserRuleContext {
    public TerminalNode POINTER() { return getToken(FhirMapJavaParser.POINTER, 0); }
    public RuleTargetPointerResourceContext ruleTargetPointerResource() {
      return getRuleContext(RuleTargetPointerResourceContext.class,0);
    }
    public RuleTargetContextContext ruleTargetContext() {
      return getRuleContext(RuleTargetContextContext.class,0);
    }
    public RuleTargetVariableContext ruleTargetVariable() {
      return getRuleContext(RuleTargetVariableContext.class,0);
    }
    public RuleTargetPointerContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_ruleTargetPointer; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitRuleTargetPointer(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetPointerContext ruleTargetPointer() throws RecognitionException {
    RuleTargetPointerContext _localctx = new RuleTargetPointerContext(_ctx, getState());
    enterRule(_localctx, 176, RULE_ruleTargetPointer);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(757);
        _errHandler.sync(this);
        switch ( getInterpreter().adaptivePredict(_input,72,_ctx) ) {
          case 1:
          {
            setState(756);
            ruleTargetContext();
          }
          break;
        }
        setState(759);
        match(POINTER);
        setState(760);
        match(T__3);
        setState(761);
        ruleTargetPointerResource();
        setState(762);
        match(T__4);
        setState(764);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la==AS) {
          {
            setState(763);
            ruleTargetVariable();
          }
        }

      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetPointerResourceContext extends ParserRuleContext {
    public IdentifierContext identifier() {
      return getRuleContext(IdentifierContext.class,0);
    }
    public RuleTargetPointerResourceContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_ruleTargetPointerResource; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitRuleTargetPointerResource(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetPointerResourceContext ruleTargetPointerResource() throws RecognitionException {
    RuleTargetPointerResourceContext _localctx = new RuleTargetPointerResourceContext(_ctx, getState());
    enterRule(_localctx, 178, RULE_ruleTargetPointerResource);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(766);
        identifier();
      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetQtyContext extends ParserRuleContext {
    public RuleTargetQty1Context ruleTargetQty1() {
      return getRuleContext(RuleTargetQty1Context.class,0);
    }
    public RuleTargetQty2Context ruleTargetQty2() {
      return getRuleContext(RuleTargetQty2Context.class,0);
    }
    public RuleTargetQty3Context ruleTargetQty3() {
      return getRuleContext(RuleTargetQty3Context.class,0);
    }
    public RuleTargetQtyContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_ruleTargetQty; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitRuleTargetQty(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetQtyContext ruleTargetQty() throws RecognitionException {
    RuleTargetQtyContext _localctx = new RuleTargetQtyContext(_ctx, getState());
    enterRule(_localctx, 180, RULE_ruleTargetQty);
    try {
      setState(771);
      _errHandler.sync(this);
      switch ( getInterpreter().adaptivePredict(_input,74,_ctx) ) {
        case 1:
          enterOuterAlt(_localctx, 1);
        {
          setState(768);
          ruleTargetQty1();
        }
        break;
        case 2:
          enterOuterAlt(_localctx, 2);
        {
          setState(769);
          ruleTargetQty2();
        }
        break;
        case 3:
          enterOuterAlt(_localctx, 3);
        {
          setState(770);
          ruleTargetQty3();
        }
        break;
      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetQty1Context extends ParserRuleContext {
    public TerminalNode QTY() { return getToken(FhirMapJavaParser.QTY, 0); }
    public RuleTargetQty1TextContext ruleTargetQty1Text() {
      return getRuleContext(RuleTargetQty1TextContext.class,0);
    }
    public RuleTargetContextContext ruleTargetContext() {
      return getRuleContext(RuleTargetContextContext.class,0);
    }
    public RuleTargetVariableContext ruleTargetVariable() {
      return getRuleContext(RuleTargetVariableContext.class,0);
    }
    public RuleTargetQty1Context(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_ruleTargetQty1; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitRuleTargetQty1(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetQty1Context ruleTargetQty1() throws RecognitionException {
    RuleTargetQty1Context _localctx = new RuleTargetQty1Context(_ctx, getState());
    enterRule(_localctx, 182, RULE_ruleTargetQty1);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(774);
        _errHandler.sync(this);
        switch ( getInterpreter().adaptivePredict(_input,75,_ctx) ) {
          case 1:
          {
            setState(773);
            ruleTargetContext();
          }
          break;
        }
        setState(776);
        match(QTY);
        setState(777);
        match(T__3);
        setState(778);
        ruleTargetQty1Text();
        setState(779);
        match(T__4);
        setState(781);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la==AS) {
          {
            setState(780);
            ruleTargetVariable();
          }
        }

      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetQty1TextContext extends ParserRuleContext {
    public QuotedStringContext quotedString() {
      return getRuleContext(QuotedStringContext.class,0);
    }
    public RuleTargetQty1TextContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_ruleTargetQty1Text; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitRuleTargetQty1Text(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetQty1TextContext ruleTargetQty1Text() throws RecognitionException {
    RuleTargetQty1TextContext _localctx = new RuleTargetQty1TextContext(_ctx, getState());
    enterRule(_localctx, 184, RULE_ruleTargetQty1Text);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(783);
        quotedString();
      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetQty2Context extends ParserRuleContext {
    public TerminalNode QTY() { return getToken(FhirMapJavaParser.QTY, 0); }
    public RuleTargetQty2ValueContext ruleTargetQty2Value() {
      return getRuleContext(RuleTargetQty2ValueContext.class,0);
    }
    public RuleTargetQty2UnitStringContext ruleTargetQty2UnitString() {
      return getRuleContext(RuleTargetQty2UnitStringContext.class,0);
    }
    public RuleTargetQty2SystemContext ruleTargetQty2System() {
      return getRuleContext(RuleTargetQty2SystemContext.class,0);
    }
    public RuleTargetContextContext ruleTargetContext() {
      return getRuleContext(RuleTargetContextContext.class,0);
    }
    public RuleTargetVariableContext ruleTargetVariable() {
      return getRuleContext(RuleTargetVariableContext.class,0);
    }
    public RuleTargetQty2Context(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_ruleTargetQty2; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitRuleTargetQty2(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetQty2Context ruleTargetQty2() throws RecognitionException {
    RuleTargetQty2Context _localctx = new RuleTargetQty2Context(_ctx, getState());
    enterRule(_localctx, 186, RULE_ruleTargetQty2);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(786);
        _errHandler.sync(this);
        switch ( getInterpreter().adaptivePredict(_input,77,_ctx) ) {
          case 1:
          {
            setState(785);
            ruleTargetContext();
          }
          break;
        }
        setState(788);
        match(QTY);
        setState(789);
        match(T__3);
        setState(790);
        ruleTargetQty2Value();
        setState(791);
        match(T__5);
        setState(792);
        ruleTargetQty2UnitString();
        setState(793);
        match(T__5);
        setState(794);
        ruleTargetQty2System();
        setState(795);
        match(T__4);
        setState(797);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la==AS) {
          {
            setState(796);
            ruleTargetVariable();
          }
        }

      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetQty2ValueContext extends ParserRuleContext {
    public IdentifierContext identifier() {
      return getRuleContext(IdentifierContext.class,0);
    }
    public RuleTargetQty2ValueContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_ruleTargetQty2Value; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitRuleTargetQty2Value(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetQty2ValueContext ruleTargetQty2Value() throws RecognitionException {
    RuleTargetQty2ValueContext _localctx = new RuleTargetQty2ValueContext(_ctx, getState());
    enterRule(_localctx, 188, RULE_ruleTargetQty2Value);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(799);
        identifier();
      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetQty2UnitStringContext extends ParserRuleContext {
    public QuotedStringContext quotedString() {
      return getRuleContext(QuotedStringContext.class,0);
    }
    public RuleTargetQty2UnitStringContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_ruleTargetQty2UnitString; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitRuleTargetQty2UnitString(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetQty2UnitStringContext ruleTargetQty2UnitString() throws RecognitionException {
    RuleTargetQty2UnitStringContext _localctx = new RuleTargetQty2UnitStringContext(_ctx, getState());
    enterRule(_localctx, 190, RULE_ruleTargetQty2UnitString);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(801);
        quotedString();
      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetQty2SystemContext extends ParserRuleContext {
    public QuotedUrlContext quotedUrl() {
      return getRuleContext(QuotedUrlContext.class,0);
    }
    public RuleTargetQty2SystemContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_ruleTargetQty2System; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitRuleTargetQty2System(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetQty2SystemContext ruleTargetQty2System() throws RecognitionException {
    RuleTargetQty2SystemContext _localctx = new RuleTargetQty2SystemContext(_ctx, getState());
    enterRule(_localctx, 192, RULE_ruleTargetQty2System);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(803);
        quotedUrl();
      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetQty3Context extends ParserRuleContext {
    public TerminalNode QTY() { return getToken(FhirMapJavaParser.QTY, 0); }
    public RuleTargetQty3ValueContext ruleTargetQty3Value() {
      return getRuleContext(RuleTargetQty3ValueContext.class,0);
    }
    public RuleTargetQty3UnitStringContext ruleTargetQty3UnitString() {
      return getRuleContext(RuleTargetQty3UnitStringContext.class,0);
    }
    public RuleTargetQty3CodeVariableContext ruleTargetQty3CodeVariable() {
      return getRuleContext(RuleTargetQty3CodeVariableContext.class,0);
    }
    public RuleTargetContextContext ruleTargetContext() {
      return getRuleContext(RuleTargetContextContext.class,0);
    }
    public RuleTargetVariableContext ruleTargetVariable() {
      return getRuleContext(RuleTargetVariableContext.class,0);
    }
    public RuleTargetQty3Context(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_ruleTargetQty3; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitRuleTargetQty3(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetQty3Context ruleTargetQty3() throws RecognitionException {
    RuleTargetQty3Context _localctx = new RuleTargetQty3Context(_ctx, getState());
    enterRule(_localctx, 194, RULE_ruleTargetQty3);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(806);
        _errHandler.sync(this);
        switch ( getInterpreter().adaptivePredict(_input,79,_ctx) ) {
          case 1:
          {
            setState(805);
            ruleTargetContext();
          }
          break;
        }
        setState(808);
        match(QTY);
        setState(809);
        match(T__3);
        setState(810);
        ruleTargetQty3Value();
        setState(811);
        match(T__5);
        setState(812);
        ruleTargetQty3UnitString();
        setState(813);
        match(T__5);
        setState(814);
        ruleTargetQty3CodeVariable();
        setState(815);
        match(T__4);
        setState(817);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la==AS) {
          {
            setState(816);
            ruleTargetVariable();
          }
        }

      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetQty3ValueContext extends ParserRuleContext {
    public IdentifierContext identifier() {
      return getRuleContext(IdentifierContext.class,0);
    }
    public RuleTargetQty3ValueContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_ruleTargetQty3Value; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitRuleTargetQty3Value(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetQty3ValueContext ruleTargetQty3Value() throws RecognitionException {
    RuleTargetQty3ValueContext _localctx = new RuleTargetQty3ValueContext(_ctx, getState());
    enterRule(_localctx, 196, RULE_ruleTargetQty3Value);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(819);
        identifier();
      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetQty3UnitStringContext extends ParserRuleContext {
    public QuotedStringContext quotedString() {
      return getRuleContext(QuotedStringContext.class,0);
    }
    public RuleTargetQty3UnitStringContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_ruleTargetQty3UnitString; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitRuleTargetQty3UnitString(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetQty3UnitStringContext ruleTargetQty3UnitString() throws RecognitionException {
    RuleTargetQty3UnitStringContext _localctx = new RuleTargetQty3UnitStringContext(_ctx, getState());
    enterRule(_localctx, 198, RULE_ruleTargetQty3UnitString);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(821);
        quotedString();
      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetQty3CodeVariableContext extends ParserRuleContext {
    public IdentifierContext identifier() {
      return getRuleContext(IdentifierContext.class,0);
    }
    public RuleTargetQty3CodeVariableContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_ruleTargetQty3CodeVariable; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitRuleTargetQty3CodeVariable(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetQty3CodeVariableContext ruleTargetQty3CodeVariable() throws RecognitionException {
    RuleTargetQty3CodeVariableContext _localctx = new RuleTargetQty3CodeVariableContext(_ctx, getState());
    enterRule(_localctx, 200, RULE_ruleTargetQty3CodeVariable);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(823);
        identifier();
      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetReferenceContext extends ParserRuleContext {
    public TerminalNode REFERENCE() { return getToken(FhirMapJavaParser.REFERENCE, 0); }
    public RuleTargetReferenceSourceContext ruleTargetReferenceSource() {
      return getRuleContext(RuleTargetReferenceSourceContext.class,0);
    }
    public RuleTargetContextContext ruleTargetContext() {
      return getRuleContext(RuleTargetContextContext.class,0);
    }
    public RuleTargetVariableContext ruleTargetVariable() {
      return getRuleContext(RuleTargetVariableContext.class,0);
    }
    public RuleTargetReferenceContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_ruleTargetReference; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitRuleTargetReference(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetReferenceContext ruleTargetReference() throws RecognitionException {
    RuleTargetReferenceContext _localctx = new RuleTargetReferenceContext(_ctx, getState());
    enterRule(_localctx, 202, RULE_ruleTargetReference);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(826);
        _errHandler.sync(this);
        switch ( getInterpreter().adaptivePredict(_input,81,_ctx) ) {
          case 1:
          {
            setState(825);
            ruleTargetContext();
          }
          break;
        }
        setState(828);
        match(REFERENCE);
        setState(829);
        match(T__3);
        setState(830);
        ruleTargetReferenceSource();
        setState(831);
        match(T__4);
        setState(833);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la==AS) {
          {
            setState(832);
            ruleTargetVariable();
          }
        }

      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetReferenceSourceContext extends ParserRuleContext {
    public IdentifierContext identifier() {
      return getRuleContext(IdentifierContext.class,0);
    }
    public QuotedStringWQuotesContext quotedStringWQuotes() {
      return getRuleContext(QuotedStringWQuotesContext.class,0);
    }
    public RuleTargetReferenceSourceContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_ruleTargetReferenceSource; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitRuleTargetReferenceSource(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetReferenceSourceContext ruleTargetReferenceSource() throws RecognitionException {
    RuleTargetReferenceSourceContext _localctx = new RuleTargetReferenceSourceContext(_ctx, getState());
    enterRule(_localctx, 204, RULE_ruleTargetReferenceSource);
    try {
      setState(837);
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
          setState(835);
          identifier();
        }
        break;
        case QIDENTIFIER:
        case QSTRING:
          enterOuterAlt(_localctx, 2);
        {
          setState(836);
          quotedStringWQuotes();
        }
        break;
        default:
          throw new NoViableAltException(this);
      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetTranslateContext extends ParserRuleContext {
    public TerminalNode TRANSLATE() { return getToken(FhirMapJavaParser.TRANSLATE, 0); }
    public RuleTargetTranslateSourceContext ruleTargetTranslateSource() {
      return getRuleContext(RuleTargetTranslateSourceContext.class,0);
    }
    public RuleTargetTranslateMapContext ruleTargetTranslateMap() {
      return getRuleContext(RuleTargetTranslateMapContext.class,0);
    }
    public RuleTargetTranslateOutputContext ruleTargetTranslateOutput() {
      return getRuleContext(RuleTargetTranslateOutputContext.class,0);
    }
    public RuleTargetContextContext ruleTargetContext() {
      return getRuleContext(RuleTargetContextContext.class,0);
    }
    public RuleTargetVariableContext ruleTargetVariable() {
      return getRuleContext(RuleTargetVariableContext.class,0);
    }
    public RuleTargetTranslateContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_ruleTargetTranslate; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitRuleTargetTranslate(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetTranslateContext ruleTargetTranslate() throws RecognitionException {
    RuleTargetTranslateContext _localctx = new RuleTargetTranslateContext(_ctx, getState());
    enterRule(_localctx, 206, RULE_ruleTargetTranslate);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(840);
        _errHandler.sync(this);
        switch ( getInterpreter().adaptivePredict(_input,84,_ctx) ) {
          case 1:
          {
            setState(839);
            ruleTargetContext();
          }
          break;
        }
        setState(842);
        match(TRANSLATE);
        setState(843);
        match(T__3);
        setState(844);
        ruleTargetTranslateSource();
        setState(845);
        match(T__5);
        setState(846);
        ruleTargetTranslateMap();
        setState(847);
        match(T__5);
        setState(848);
        ruleTargetTranslateOutput();
        setState(849);
        match(T__4);
        setState(851);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la==AS) {
          {
            setState(850);
            ruleTargetVariable();
          }
        }

      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetTranslateSourceContext extends ParserRuleContext {
    public IdentifierContext identifier() {
      return getRuleContext(IdentifierContext.class,0);
    }
    public RuleTargetTranslateSourceContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_ruleTargetTranslateSource; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitRuleTargetTranslateSource(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetTranslateSourceContext ruleTargetTranslateSource() throws RecognitionException {
    RuleTargetTranslateSourceContext _localctx = new RuleTargetTranslateSourceContext(_ctx, getState());
    enterRule(_localctx, 208, RULE_ruleTargetTranslateSource);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(853);
        identifier();
      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetTranslateMapContext extends ParserRuleContext {
    public QuotedUrlContext quotedUrl() {
      return getRuleContext(QuotedUrlContext.class,0);
    }
    public RuleTargetTranslateMapContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_ruleTargetTranslateMap; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitRuleTargetTranslateMap(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetTranslateMapContext ruleTargetTranslateMap() throws RecognitionException {
    RuleTargetTranslateMapContext _localctx = new RuleTargetTranslateMapContext(_ctx, getState());
    enterRule(_localctx, 210, RULE_ruleTargetTranslateMap);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(855);
        quotedUrl();
      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetTranslateOutputContext extends ParserRuleContext {
    public RuleTargetTranslateOutputCodeContext ruleTargetTranslateOutputCode() {
      return getRuleContext(RuleTargetTranslateOutputCodeContext.class,0);
    }
    public RuleTargetTranslateOutputSystemContext ruleTargetTranslateOutputSystem() {
      return getRuleContext(RuleTargetTranslateOutputSystemContext.class,0);
    }
    public RuleTargetTranslateOutputDisplayContext ruleTargetTranslateOutputDisplay() {
      return getRuleContext(RuleTargetTranslateOutputDisplayContext.class,0);
    }
    public RuleTargetTranslateOutputCodingContext ruleTargetTranslateOutputCoding() {
      return getRuleContext(RuleTargetTranslateOutputCodingContext.class,0);
    }
    public RuleTargetTranslateOutputCodeableConceptContext ruleTargetTranslateOutputCodeableConcept() {
      return getRuleContext(RuleTargetTranslateOutputCodeableConceptContext.class,0);
    }
    public RuleTargetTranslateOutputContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_ruleTargetTranslateOutput; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitRuleTargetTranslateOutput(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetTranslateOutputContext ruleTargetTranslateOutput() throws RecognitionException {
    RuleTargetTranslateOutputContext _localctx = new RuleTargetTranslateOutputContext(_ctx, getState());
    enterRule(_localctx, 212, RULE_ruleTargetTranslateOutput);
    try {
      setState(862);
      _errHandler.sync(this);
      switch (_input.LA(1)) {
        case CODE:
          enterOuterAlt(_localctx, 1);
        {
          setState(857);
          ruleTargetTranslateOutputCode();
        }
        break;
        case SYSTEM:
          enterOuterAlt(_localctx, 2);
        {
          setState(858);
          ruleTargetTranslateOutputSystem();
        }
        break;
        case DISPLAY:
          enterOuterAlt(_localctx, 3);
        {
          setState(859);
          ruleTargetTranslateOutputDisplay();
        }
        break;
        case CODING:
          enterOuterAlt(_localctx, 4);
        {
          setState(860);
          ruleTargetTranslateOutputCoding();
        }
        break;
        case CODEABLECONCEPT:
          enterOuterAlt(_localctx, 5);
        {
          setState(861);
          ruleTargetTranslateOutputCodeableConcept();
        }
        break;
        default:
          throw new NoViableAltException(this);
      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetTranslateOutputCodeContext extends ParserRuleContext {
    public TerminalNode CODE() { return getToken(FhirMapJavaParser.CODE, 0); }
    public RuleTargetTranslateOutputCodeContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_ruleTargetTranslateOutputCode; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitRuleTargetTranslateOutputCode(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetTranslateOutputCodeContext ruleTargetTranslateOutputCode() throws RecognitionException {
    RuleTargetTranslateOutputCodeContext _localctx = new RuleTargetTranslateOutputCodeContext(_ctx, getState());
    enterRule(_localctx, 214, RULE_ruleTargetTranslateOutputCode);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(864);
        match(CODE);
      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetTranslateOutputSystemContext extends ParserRuleContext {
    public TerminalNode SYSTEM() { return getToken(FhirMapJavaParser.SYSTEM, 0); }
    public RuleTargetTranslateOutputSystemContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_ruleTargetTranslateOutputSystem; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitRuleTargetTranslateOutputSystem(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetTranslateOutputSystemContext ruleTargetTranslateOutputSystem() throws RecognitionException {
    RuleTargetTranslateOutputSystemContext _localctx = new RuleTargetTranslateOutputSystemContext(_ctx, getState());
    enterRule(_localctx, 216, RULE_ruleTargetTranslateOutputSystem);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(866);
        match(SYSTEM);
      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetTranslateOutputDisplayContext extends ParserRuleContext {
    public TerminalNode DISPLAY() { return getToken(FhirMapJavaParser.DISPLAY, 0); }
    public RuleTargetTranslateOutputDisplayContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_ruleTargetTranslateOutputDisplay; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitRuleTargetTranslateOutputDisplay(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetTranslateOutputDisplayContext ruleTargetTranslateOutputDisplay() throws RecognitionException {
    RuleTargetTranslateOutputDisplayContext _localctx = new RuleTargetTranslateOutputDisplayContext(_ctx, getState());
    enterRule(_localctx, 218, RULE_ruleTargetTranslateOutputDisplay);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(868);
        match(DISPLAY);
      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetTranslateOutputCodingContext extends ParserRuleContext {
    public TerminalNode CODING() { return getToken(FhirMapJavaParser.CODING, 0); }
    public RuleTargetTranslateOutputCodingContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_ruleTargetTranslateOutputCoding; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitRuleTargetTranslateOutputCoding(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetTranslateOutputCodingContext ruleTargetTranslateOutputCoding() throws RecognitionException {
    RuleTargetTranslateOutputCodingContext _localctx = new RuleTargetTranslateOutputCodingContext(_ctx, getState());
    enterRule(_localctx, 220, RULE_ruleTargetTranslateOutputCoding);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(870);
        match(CODING);
      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetTranslateOutputCodeableConceptContext extends ParserRuleContext {
    public TerminalNode CODEABLECONCEPT() { return getToken(FhirMapJavaParser.CODEABLECONCEPT, 0); }
    public RuleTargetTranslateOutputCodeableConceptContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_ruleTargetTranslateOutputCodeableConcept; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitRuleTargetTranslateOutputCodeableConcept(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetTranslateOutputCodeableConceptContext ruleTargetTranslateOutputCodeableConcept() throws RecognitionException {
    RuleTargetTranslateOutputCodeableConceptContext _localctx = new RuleTargetTranslateOutputCodeableConceptContext(_ctx, getState());
    enterRule(_localctx, 222, RULE_ruleTargetTranslateOutputCodeableConcept);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(872);
        match(CODEABLECONCEPT);
      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetTruncateContext extends ParserRuleContext {
    public TerminalNode TRUNCATE() { return getToken(FhirMapJavaParser.TRUNCATE, 0); }
    public RuleTargetTruncateSourceContext ruleTargetTruncateSource() {
      return getRuleContext(RuleTargetTruncateSourceContext.class,0);
    }
    public RuleTargetTruncateLengthContext ruleTargetTruncateLength() {
      return getRuleContext(RuleTargetTruncateLengthContext.class,0);
    }
    public RuleTargetContextContext ruleTargetContext() {
      return getRuleContext(RuleTargetContextContext.class,0);
    }
    public RuleTargetVariableContext ruleTargetVariable() {
      return getRuleContext(RuleTargetVariableContext.class,0);
    }
    public RuleTargetTruncateContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_ruleTargetTruncate; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitRuleTargetTruncate(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetTruncateContext ruleTargetTruncate() throws RecognitionException {
    RuleTargetTruncateContext _localctx = new RuleTargetTruncateContext(_ctx, getState());
    enterRule(_localctx, 224, RULE_ruleTargetTruncate);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(875);
        _errHandler.sync(this);
        switch ( getInterpreter().adaptivePredict(_input,87,_ctx) ) {
          case 1:
          {
            setState(874);
            ruleTargetContext();
          }
          break;
        }
        setState(877);
        match(TRUNCATE);
        setState(878);
        match(T__3);
        setState(879);
        ruleTargetTruncateSource();
        setState(880);
        match(T__5);
        setState(881);
        ruleTargetTruncateLength();
        setState(882);
        match(T__4);
        setState(884);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la==AS) {
          {
            setState(883);
            ruleTargetVariable();
          }
        }

      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetTruncateSourceContext extends ParserRuleContext {
    public IdentifierContext identifier() {
      return getRuleContext(IdentifierContext.class,0);
    }
    public QuotedStringWQuotesContext quotedStringWQuotes() {
      return getRuleContext(QuotedStringWQuotesContext.class,0);
    }
    public RuleTargetTruncateSourceContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_ruleTargetTruncateSource; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitRuleTargetTruncateSource(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetTruncateSourceContext ruleTargetTruncateSource() throws RecognitionException {
    RuleTargetTruncateSourceContext _localctx = new RuleTargetTruncateSourceContext(_ctx, getState());
    enterRule(_localctx, 226, RULE_ruleTargetTruncateSource);
    try {
      setState(888);
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
          setState(886);
          identifier();
        }
        break;
        case QIDENTIFIER:
        case QSTRING:
          enterOuterAlt(_localctx, 2);
        {
          setState(887);
          quotedStringWQuotes();
        }
        break;
        default:
          throw new NoViableAltException(this);
      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetTruncateLengthContext extends ParserRuleContext {
    public IntegerContext integer() {
      return getRuleContext(IntegerContext.class,0);
    }
    public RuleTargetTruncateLengthContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_ruleTargetTruncateLength; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitRuleTargetTruncateLength(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetTruncateLengthContext ruleTargetTruncateLength() throws RecognitionException {
    RuleTargetTruncateLengthContext _localctx = new RuleTargetTruncateLengthContext(_ctx, getState());
    enterRule(_localctx, 228, RULE_ruleTargetTruncateLength);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(890);
        integer();
      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetUuidContext extends ParserRuleContext {
    public TerminalNode UUID() { return getToken(FhirMapJavaParser.UUID, 0); }
    public RuleTargetContextContext ruleTargetContext() {
      return getRuleContext(RuleTargetContextContext.class,0);
    }
    public RuleTargetVariableContext ruleTargetVariable() {
      return getRuleContext(RuleTargetVariableContext.class,0);
    }
    public RuleTargetUuidContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_ruleTargetUuid; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitRuleTargetUuid(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetUuidContext ruleTargetUuid() throws RecognitionException {
    RuleTargetUuidContext _localctx = new RuleTargetUuidContext(_ctx, getState());
    enterRule(_localctx, 230, RULE_ruleTargetUuid);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(893);
        _errHandler.sync(this);
        switch ( getInterpreter().adaptivePredict(_input,90,_ctx) ) {
          case 1:
          {
            setState(892);
            ruleTargetContext();
          }
          break;
        }
        setState(895);
        match(UUID);
        setState(896);
        match(T__3);
        setState(897);
        match(T__4);
        setState(899);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la==AS) {
          {
            setState(898);
            ruleTargetVariable();
          }
        }

      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class RuleTargetVariableContext extends ParserRuleContext {
    public TerminalNode AS() { return getToken(FhirMapJavaParser.AS, 0); }
    public IdentifierContext identifier() {
      return getRuleContext(IdentifierContext.class,0);
    }
    public RuleTargetVariableContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_ruleTargetVariable; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitRuleTargetVariable(this);
      else return visitor.visitChildren(this);
    }
  }

  public final RuleTargetVariableContext ruleTargetVariable() throws RecognitionException {
    RuleTargetVariableContext _localctx = new RuleTargetVariableContext(_ctx, getState());
    enterRule(_localctx, 232, RULE_ruleTargetVariable);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(901);
        match(AS);
        setState(902);
        identifier();
      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class FhirPathContext extends ParserRuleContext {
    public QuotedStringContext quotedString() {
      return getRuleContext(QuotedStringContext.class,0);
    }
    public FhirPathContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_fhirPath; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitFhirPath(this);
      else return visitor.visitChildren(this);
    }
  }

  public final FhirPathContext fhirPath() throws RecognitionException {
    FhirPathContext _localctx = new FhirPathContext(_ctx, getState());
    enterRule(_localctx, 234, RULE_fhirPath);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(904);
        quotedString();
      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class IdentifierContext extends ParserRuleContext {
    public TerminalNode IDENTIFIER() { return getToken(FhirMapJavaParser.IDENTIFIER, 0); }
    public TerminalNode SOURCE() { return getToken(FhirMapJavaParser.SOURCE, 0); }
    public TerminalNode TARGET() { return getToken(FhirMapJavaParser.TARGET, 0); }
    public TerminalNode QUERIED() { return getToken(FhirMapJavaParser.QUERIED, 0); }
    public TerminalNode PRODUCED() { return getToken(FhirMapJavaParser.PRODUCED, 0); }
    public TerminalNode APPEND() { return getToken(FhirMapJavaParser.APPEND, 0); }
    public TerminalNode CAST() { return getToken(FhirMapJavaParser.CAST, 0); }
    public TerminalNode C() { return getToken(FhirMapJavaParser.C, 0); }
    public TerminalNode CC() { return getToken(FhirMapJavaParser.CC, 0); }
    public TerminalNode CP() { return getToken(FhirMapJavaParser.CP, 0); }
    public TerminalNode CODEABLECONCEPT() { return getToken(FhirMapJavaParser.CODEABLECONCEPT, 0); }
    public TerminalNode CODING() { return getToken(FhirMapJavaParser.CODING, 0); }
    public TerminalNode COPY() { return getToken(FhirMapJavaParser.COPY, 0); }
    public TerminalNode CODE() { return getToken(FhirMapJavaParser.CODE, 0); }
    public TerminalNode DISPLAY() { return getToken(FhirMapJavaParser.DISPLAY, 0); }
    public TerminalNode CREATE() { return getToken(FhirMapJavaParser.CREATE, 0); }
    public TerminalNode DATEOP() { return getToken(FhirMapJavaParser.DATEOP, 0); }
    public TerminalNode DEFAULT() { return getToken(FhirMapJavaParser.DEFAULT, 0); }
    public TerminalNode ESCAPE() { return getToken(FhirMapJavaParser.ESCAPE, 0); }
    public TerminalNode EXTENSION() { return getToken(FhirMapJavaParser.EXTENSION, 0); }
    public TerminalNode EVALUATE() { return getToken(FhirMapJavaParser.EVALUATE, 0); }
    public TerminalNode ID() { return getToken(FhirMapJavaParser.ID, 0); }
    public TerminalNode MAP() { return getToken(FhirMapJavaParser.MAP, 0); }
    public TerminalNode POINTER() { return getToken(FhirMapJavaParser.POINTER, 0); }
    public TerminalNode QTY() { return getToken(FhirMapJavaParser.QTY, 0); }
    public TerminalNode REFERENCE() { return getToken(FhirMapJavaParser.REFERENCE, 0); }
    public TerminalNode SYSTEM() { return getToken(FhirMapJavaParser.SYSTEM, 0); }
    public TerminalNode TRANSLATE() { return getToken(FhirMapJavaParser.TRANSLATE, 0); }
    public TerminalNode TRUNCATE() { return getToken(FhirMapJavaParser.TRUNCATE, 0); }
    public TerminalNode TYPES() { return getToken(FhirMapJavaParser.TYPES, 0); }
    public TerminalNode UUID() { return getToken(FhirMapJavaParser.UUID, 0); }
    public IdentifierContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_identifier; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitIdentifier(this);
      else return visitor.visitChildren(this);
    }
  }

  public final IdentifierContext identifier() throws RecognitionException {
    IdentifierContext _localctx = new IdentifierContext(_ctx, getState());
    enterRule(_localctx, 236, RULE_identifier);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(906);
        _la = _input.LA(1);
        if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << SOURCE) | (1L << TARGET) | (1L << QUERIED) | (1L << PRODUCED) | (1L << APPEND) | (1L << CAST) | (1L << C) | (1L << CC) | (1L << CODE) | (1L << CP) | (1L << CODEABLECONCEPT) | (1L << CODING) | (1L << COPY) | (1L << CREATE) | (1L << DATEOP) | (1L << DEFAULT) | (1L << DISPLAY) | (1L << ESCAPE) | (1L << EXTENSION) | (1L << EVALUATE) | (1L << ID) | (1L << MAP) | (1L << POINTER) | (1L << QTY) | (1L << REFERENCE) | (1L << SYSTEM) | (1L << TRANSLATE) | (1L << TRUNCATE) | (1L << TYPES) | (1L << UUID) | (1L << IDENTIFIER))) != 0)) ) {
          _errHandler.recoverInline(this);
        }
        else {
          if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
          _errHandler.reportMatch(this);
          consume();
        }
      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class IntegerContext extends ParserRuleContext {
    public TerminalNode DIGITS() { return getToken(FhirMapJavaParser.DIGITS, 0); }
    public IntegerContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_integer; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitInteger(this);
      else return visitor.visitChildren(this);
    }
  }

  public final IntegerContext integer() throws RecognitionException {
    IntegerContext _localctx = new IntegerContext(_ctx, getState());
    enterRule(_localctx, 238, RULE_integer);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(908);
        match(DIGITS);
      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class QuotedIdentifierContext extends ParserRuleContext {
    public TerminalNode QIDENTIFIER() { return getToken(FhirMapJavaParser.QIDENTIFIER, 0); }
    public QuotedIdentifierContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_quotedIdentifier; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitQuotedIdentifier(this);
      else return visitor.visitChildren(this);
    }
  }

  public final QuotedIdentifierContext quotedIdentifier() throws RecognitionException {
    QuotedIdentifierContext _localctx = new QuotedIdentifierContext(_ctx, getState());
    enterRule(_localctx, 240, RULE_quotedIdentifier);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(910);
        match(QIDENTIFIER);
      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class QuotedStringWQuotesContext extends ParserRuleContext {
    public TerminalNode QSTRING() { return getToken(FhirMapJavaParser.QSTRING, 0); }
    public TerminalNode QIDENTIFIER() { return getToken(FhirMapJavaParser.QIDENTIFIER, 0); }
    public QuotedStringWQuotesContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_quotedStringWQuotes; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitQuotedStringWQuotes(this);
      else return visitor.visitChildren(this);
    }
  }

  public final QuotedStringWQuotesContext quotedStringWQuotes() throws RecognitionException {
    QuotedStringWQuotesContext _localctx = new QuotedStringWQuotesContext(_ctx, getState());
    enterRule(_localctx, 242, RULE_quotedStringWQuotes);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(912);
        _la = _input.LA(1);
        if ( !(_la==QIDENTIFIER || _la==QSTRING) ) {
          _errHandler.recoverInline(this);
        }
        else {
          if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
          _errHandler.reportMatch(this);
          consume();
        }
      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class QuotedStringContext extends ParserRuleContext {
    public TerminalNode QSTRING() { return getToken(FhirMapJavaParser.QSTRING, 0); }
    public TerminalNode QIDENTIFIER() { return getToken(FhirMapJavaParser.QIDENTIFIER, 0); }
    public QuotedStringContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_quotedString; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitQuotedString(this);
      else return visitor.visitChildren(this);
    }
  }

  public final QuotedStringContext quotedString() throws RecognitionException {
    QuotedStringContext _localctx = new QuotedStringContext(_ctx, getState());
    enterRule(_localctx, 244, RULE_quotedString);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(914);
        _la = _input.LA(1);
        if ( !(_la==QIDENTIFIER || _la==QSTRING) ) {
          _errHandler.recoverInline(this);
        }
        else {
          if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
          _errHandler.reportMatch(this);
          consume();
        }
      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class QuotedUrlContext extends ParserRuleContext {
    public QuotedStringContext quotedString() {
      return getRuleContext(QuotedStringContext.class,0);
    }
    public QuotedUrlContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_quotedUrl; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitQuotedUrl(this);
      else return visitor.visitChildren(this);
    }
  }

  public final QuotedUrlContext quotedUrl() throws RecognitionException {
    QuotedUrlContext _localctx = new QuotedUrlContext(_ctx, getState());
    enterRule(_localctx, 246, RULE_quotedUrl);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(916);
        quotedString();
      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class StructureDefinitionContext extends ParserRuleContext {
    public QuotedUrlContext quotedUrl() {
      return getRuleContext(QuotedUrlContext.class,0);
    }
    public StructureDefinitionContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_structureDefinition; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitStructureDefinition(this);
      else return visitor.visitChildren(this);
    }
  }

  public final StructureDefinitionContext structureDefinition() throws RecognitionException {
    StructureDefinitionContext _localctx = new StructureDefinitionContext(_ctx, getState());
    enterRule(_localctx, 248, RULE_structureDefinition);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(918);
        quotedUrl();
      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static class StructureMapContext extends ParserRuleContext {
    public QuotedUrlContext quotedUrl() {
      return getRuleContext(QuotedUrlContext.class,0);
    }
    public StructureMapContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_structureMap; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitStructureMap(this);
      else return visitor.visitChildren(this);
    }
  }

  public final StructureMapContext structureMap() throws RecognitionException {
    StructureMapContext _localctx = new StructureMapContext(_ctx, getState());
    enterRule(_localctx, 250, RULE_structureMap);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(920);
        quotedUrl();
      }
    }
    catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    }
    finally {
      exitRule();
    }
    return _localctx;
  }

  public static final String _serializedATN =
    "\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3?\u039d\4\2\t\2\4"+
      "\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
      "\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
      "\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
      "\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
      "\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"+
      ",\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64\t"+
      "\64\4\65\t\65\4\66\t\66\4\67\t\67\48\t8\49\t9\4:\t:\4;\t;\4<\t<\4=\t="+
      "\4>\t>\4?\t?\4@\t@\4A\tA\4B\tB\4C\tC\4D\tD\4E\tE\4F\tF\4G\tG\4H\tH\4I"+
      "\tI\4J\tJ\4K\tK\4L\tL\4M\tM\4N\tN\4O\tO\4P\tP\4Q\tQ\4R\tR\4S\tS\4T\tT"+
      "\4U\tU\4V\tV\4W\tW\4X\tX\4Y\tY\4Z\tZ\4[\t[\4\\\t\\\4]\t]\4^\t^\4_\t_\4"+
      "`\t`\4a\ta\4b\tb\4c\tc\4d\td\4e\te\4f\tf\4g\tg\4h\th\4i\ti\4j\tj\4k\t"+
      "k\4l\tl\4m\tm\4n\tn\4o\to\4p\tp\4q\tq\4r\tr\4s\ts\4t\tt\4u\tu\4v\tv\4"+
      "w\tw\4x\tx\4y\ty\4z\tz\4{\t{\4|\t|\4}\t}\4~\t~\4\177\t\177\3\2\3\2\6\2"+
      "\u0101\n\2\r\2\16\2\u0102\3\2\7\2\u0106\n\2\f\2\16\2\u0109\13\2\3\2\3"+
      "\2\3\3\3\3\3\3\3\3\3\3\3\4\3\4\3\4\3\4\3\4\5\4\u0117\n\4\3\5\3\5\3\5\3"+
      "\5\5\5\u011d\n\5\3\6\3\6\3\7\3\7\3\b\3\b\3\t\3\t\3\n\3\n\3\n\5\n\u012a"+
      "\n\n\3\13\3\13\7\13\u012e\n\13\f\13\16\13\u0131\13\13\3\13\7\13\u0134"+
      "\n\13\f\13\16\13\u0137\13\13\3\13\3\13\3\f\3\f\5\f\u013d\n\f\3\f\3\f\5"+
      "\f\u0141\n\f\3\r\3\r\3\r\3\16\3\16\3\17\3\17\3\17\3\20\3\20\5\20\u014d"+
      "\n\20\3\21\3\21\3\22\3\22\3\23\3\23\3\23\3\23\3\23\5\23\u0158\n\23\3\23"+
      "\5\23\u015b\n\23\3\24\3\24\3\25\3\25\3\26\3\26\3\26\3\27\3\27\5\27\u0166"+
      "\n\27\3\30\3\30\3\31\3\31\3\32\3\32\5\32\u016e\n\32\3\33\3\33\3\33\5\33"+
      "\u0173\n\33\3\33\3\33\3\34\3\34\3\34\7\34\u017a\n\34\f\34\16\34\u017d"+
      "\13\34\3\35\3\35\3\35\3\35\3\35\5\35\u0184\n\35\3\36\3\36\3\36\7\36\u0189"+
      "\n\36\f\36\16\36\u018c\13\36\3\37\3\37\3\37\7\37\u0191\n\37\f\37\16\37"+
      "\u0194\13\37\3 \3 \5 \u0198\n \3 \5 \u019b\n \3 \5 \u019e\n \3 \5 \u01a1"+
      "\n \3 \5 \u01a4\n \3 \5 \u01a7\n \3!\3!\3!\7!\u01ac\n!\f!\16!\u01af\13"+
      "!\3\"\3\"\5\"\u01b3\n\"\3#\3#\3#\3#\3#\3#\5#\u01bb\n#\3$\3$\3$\3%\3%\3"+
      "&\3&\3&\3\'\3\'\3\'\3(\3(\3(\3)\3)\3)\5)\u01ce\n)\3*\3*\3*\7*\u01d3\n"+
      "*\f*\16*\u01d6\13*\3+\3+\3+\3+\3+\3,\7,\u01de\n,\f,\16,\u01e1\13,\3-\3"+
      "-\3-\3-\3-\3-\3-\3-\3-\3-\3-\3-\3-\3-\3-\3-\3-\3-\3-\3-\5-\u01f7\n-\3"+
      ".\3.\3.\3/\3/\3/\3\60\3\60\3\60\5\60\u0202\n\60\3\61\3\61\5\61\u0206\n"+
      "\61\3\62\5\62\u0209\n\62\3\62\3\62\3\62\3\62\3\62\5\62\u0210\n\62\3\63"+
      "\3\63\3\63\7\63\u0215\n\63\f\63\16\63\u0218\13\63\3\64\3\64\5\64\u021c"+
      "\n\64\3\65\5\65\u021f\n\65\3\65\3\65\3\65\3\65\3\65\3\65\3\65\5\65\u0228"+
      "\n\65\3\65\3\65\5\65\u022c\n\65\3\66\3\66\3\67\3\67\5\67\u0232\n\67\3"+
      "8\38\39\59\u0237\n9\39\39\39\39\39\59\u023e\n9\39\39\59\u0242\n9\3:\3"+
      ":\3;\3;\3<\3<\5<\u024a\n<\3<\5<\u024d\n<\3=\5=\u0250\n=\3=\3=\3=\3=\3"+
      "=\5=\u0257\n=\3>\3>\3?\5?\u025c\n?\3?\3?\3?\3?\3?\3?\3?\5?\u0265\n?\3"+
      "?\3?\5?\u0269\n?\3@\3@\3A\3A\5A\u026f\nA\3B\3B\3C\5C\u0274\nC\3C\3C\3"+
      "C\3C\3C\5C\u027b\nC\3C\3C\3C\5C\u0280\nC\3D\3D\3E\3E\3F\3F\3F\3F\3F\3"+
      "F\5F\u028c\nF\3G\3G\3H\5H\u0291\nH\3H\3H\3H\3H\3H\5H\u0298\nH\3I\3I\3"+
      "J\5J\u029d\nJ\3J\3J\3J\3J\3J\3J\3J\5J\u02a6\nJ\3J\3J\5J\u02aa\nJ\3K\3"+
      "K\3L\3L\3M\3M\3N\5N\u02b3\nN\3N\3N\3N\3N\5N\u02b9\nN\3O\5O\u02bc\nO\3"+
      "O\3O\3O\3O\3O\3O\3O\5O\u02c5\nO\3O\3O\5O\u02c9\nO\3P\3P\3Q\3Q\3R\3R\3"+
      "S\5S\u02d2\nS\3S\3S\3S\3S\3S\3S\3S\5S\u02db\nS\3T\3T\3U\3U\3V\5V\u02e2"+
      "\nV\3V\3V\3V\3V\3V\3V\3V\5V\u02eb\nV\3V\3V\5V\u02ef\nV\3W\3W\3X\3X\3Y"+
      "\3Y\3Z\5Z\u02f8\nZ\3Z\3Z\3Z\3Z\3Z\5Z\u02ff\nZ\3[\3[\3\\\3\\\3\\\5\\\u0306"+
      "\n\\\3]\5]\u0309\n]\3]\3]\3]\3]\3]\5]\u0310\n]\3^\3^\3_\5_\u0315\n_\3"+
      "_\3_\3_\3_\3_\3_\3_\3_\3_\5_\u0320\n_\3`\3`\3a\3a\3b\3b\3c\5c\u0329\n"+
      "c\3c\3c\3c\3c\3c\3c\3c\3c\3c\5c\u0334\nc\3d\3d\3e\3e\3f\3f\3g\5g\u033d"+
      "\ng\3g\3g\3g\3g\3g\5g\u0344\ng\3h\3h\5h\u0348\nh\3i\5i\u034b\ni\3i\3i"+
      "\3i\3i\3i\3i\3i\3i\3i\5i\u0356\ni\3j\3j\3k\3k\3l\3l\3l\3l\3l\5l\u0361"+
      "\nl\3m\3m\3n\3n\3o\3o\3p\3p\3q\3q\3r\5r\u036e\nr\3r\3r\3r\3r\3r\3r\3r"+
      "\5r\u0377\nr\3s\3s\5s\u037b\ns\3t\3t\3u\5u\u0380\nu\3u\3u\3u\3u\5u\u0386"+
      "\nu\3v\3v\3v\3w\3w\3x\3x\3y\3y\3z\3z\3{\3{\3|\3|\3}\3}\3~\3~\3\177\3\177"+
      "\3\177\2\2\u0080\2\4\6\b\n\f\16\20\22\24\26\30\32\34\36 \"$&(*,.\60\62"+
      "\64\668:<>@BDFHJLNPRTVXZ\\^`bdfhjlnprtvxz|~\u0080\u0082\u0084\u0086\u0088"+
      "\u008a\u008c\u008e\u0090\u0092\u0094\u0096\u0098\u009a\u009c\u009e\u00a0"+
      "\u00a2\u00a4\u00a6\u00a8\u00aa\u00ac\u00ae\u00b0\u00b2\u00b4\u00b6\u00b8"+
      "\u00ba\u00bc\u00be\u00c0\u00c2\u00c4\u00c6\u00c8\u00ca\u00cc\u00ce\u00d0"+
      "\u00d2\u00d4\u00d6\u00d8\u00da\u00dc\u00de\u00e0\u00e2\u00e4\u00e6\u00e8"+
      "\u00ea\u00ec\u00ee\u00f0\u00f2\u00f4\u00f6\u00f8\u00fa\u00fc\2\4\f\2\20"+
      "\23\26\26\30\34\36$&(,,\60\64\668::==\3\2\24\25\2\u0392\2\u00fe\3\2\2"+
      "\2\4\u010c\3\2\2\2\6\u0111\3\2\2\2\b\u011c\3\2\2\2\n\u011e\3\2\2\2\f\u0120"+
      "\3\2\2\2\16\u0122\3\2\2\2\20\u0124\3\2\2\2\22\u0126\3\2\2\2\24\u012b\3"+
      "\2\2\2\26\u013a\3\2\2\2\30\u0142\3\2\2\2\32\u0145\3\2\2\2\34\u0147\3\2"+
      "\2\2\36\u014c\3\2\2\2 \u014e\3\2\2\2\"\u0150\3\2\2\2$\u0152\3\2\2\2&\u015c"+
      "\3\2\2\2(\u015e\3\2\2\2*\u0160\3\2\2\2,\u0165\3\2\2\2.\u0167\3\2\2\2\60"+
      "\u0169\3\2\2\2\62\u016d\3\2\2\2\64\u016f\3\2\2\2\66\u0176\3\2\2\28\u017e"+
      "\3\2\2\2:\u0185\3\2\2\2<\u018d\3\2\2\2>\u0195\3\2\2\2@\u01a8\3\2\2\2B"+
      "\u01b2\3\2\2\2D\u01b4\3\2\2\2F\u01bc\3\2\2\2H\u01bf\3\2\2\2J\u01c1\3\2"+
      "\2\2L\u01c4\3\2\2\2N\u01c7\3\2\2\2P\u01ca\3\2\2\2R\u01cf\3\2\2\2T\u01d7"+
      "\3\2\2\2V\u01df\3\2\2\2X\u01f6\3\2\2\2Z\u01f8\3\2\2\2\\\u01fb\3\2\2\2"+
      "^\u01fe\3\2\2\2`\u0205\3\2\2\2b\u0208\3\2\2\2d\u0211\3\2\2\2f\u021b\3"+
      "\2\2\2h\u021e\3\2\2\2j\u022d\3\2\2\2l\u0231\3\2\2\2n\u0233\3\2\2\2p\u0236"+
      "\3\2\2\2r\u0243\3\2\2\2t\u0245\3\2\2\2v\u0249\3\2\2\2x\u024f\3\2\2\2z"+
      "\u0258\3\2\2\2|\u025b\3\2\2\2~\u026a\3\2\2\2\u0080\u026e\3\2\2\2\u0082"+
      "\u0270\3\2\2\2\u0084\u0273\3\2\2\2\u0086\u0281\3\2\2\2\u0088\u0283\3\2"+
      "\2\2\u008a\u0285\3\2\2\2\u008c\u028d\3\2\2\2\u008e\u0290\3\2\2\2\u0090"+
      "\u0299\3\2\2\2\u0092\u029c\3\2\2\2\u0094\u02ab\3\2\2\2\u0096\u02ad\3\2"+
      "\2\2\u0098\u02af\3\2\2\2\u009a\u02b2\3\2\2\2\u009c\u02bb\3\2\2\2\u009e"+
      "\u02ca\3\2\2\2\u00a0\u02cc\3\2\2\2\u00a2\u02ce\3\2\2\2\u00a4\u02d1\3\2"+
      "\2\2\u00a6\u02dc\3\2\2\2\u00a8\u02de\3\2\2\2\u00aa\u02e1\3\2\2\2\u00ac"+
      "\u02f0\3\2\2\2\u00ae\u02f2\3\2\2\2\u00b0\u02f4\3\2\2\2\u00b2\u02f7\3\2"+
      "\2\2\u00b4\u0300\3\2\2\2\u00b6\u0305\3\2\2\2\u00b8\u0308\3\2\2\2\u00ba"+
      "\u0311\3\2\2\2\u00bc\u0314\3\2\2\2\u00be\u0321\3\2\2\2\u00c0\u0323\3\2"+
      "\2\2\u00c2\u0325\3\2\2\2\u00c4\u0328\3\2\2\2\u00c6\u0335\3\2\2\2\u00c8"+
      "\u0337\3\2\2\2\u00ca\u0339\3\2\2\2\u00cc\u033c\3\2\2\2\u00ce\u0347\3\2"+
      "\2\2\u00d0\u034a\3\2\2\2\u00d2\u0357\3\2\2\2\u00d4\u0359\3\2\2\2\u00d6"+
      "\u0360\3\2\2\2\u00d8\u0362\3\2\2\2\u00da\u0364\3\2\2\2\u00dc\u0366\3\2"+
      "\2\2\u00de\u0368\3\2\2\2\u00e0\u036a\3\2\2\2\u00e2\u036d\3\2\2\2\u00e4"+
      "\u037a\3\2\2\2\u00e6\u037c\3\2\2\2\u00e8\u037f\3\2\2\2\u00ea\u0387\3\2"+
      "\2\2\u00ec\u038a\3\2\2\2\u00ee\u038c\3\2\2\2\u00f0\u038e\3\2\2\2\u00f2"+
      "\u0390\3\2\2\2\u00f4\u0392\3\2\2\2\u00f6\u0394\3\2\2\2\u00f8\u0396\3\2"+
      "\2\2\u00fa\u0398\3\2\2\2\u00fc\u039a\3\2\2\2\u00fe\u0100\5\4\3\2\u00ff"+
      "\u0101\5\6\4\2\u0100\u00ff\3\2\2\2\u0101\u0102\3\2\2\2\u0102\u0100\3\2"+
      "\2\2\u0102\u0103\3\2\2\2\u0103\u0107\3\2\2\2\u0104\u0106\5\22\n\2\u0105"+
      "\u0104\3\2\2\2\u0106\u0109\3\2\2\2\u0107\u0105\3\2\2\2\u0107\u0108\3\2"+
      "\2\2\u0108\u010a\3\2\2\2\u0109\u0107\3\2\2\2\u010a\u010b\5\24\13\2\u010b"+
      "\3\3\2\2\2\u010c\u010d\7\60\2\2\u010d\u010e\5\u00fc\177\2\u010e\u010f"+
      "\7\3\2\2\u010f\u0110\5\u00f6|\2\u0110\5\3\2\2\2\u0111\u0112\7;\2\2\u0112"+
      "\u0113\5\u00fa~\2\u0113\u0114\7\27\2\2\u0114\u0116\5\b\5\2\u0115\u0117"+
      "\7\4\2\2\u0116\u0115\3\2\2\2\u0116\u0117\3\2\2\2\u0117\7\3\2\2\2\u0118"+
      "\u011d\5\n\6\2\u0119\u011d\5\f\7\2\u011a\u011d\5\16\b\2\u011b\u011d\5"+
      "\20\t\2\u011c\u0118\3\2\2\2\u011c\u0119\3\2\2\2\u011c\u011a\3\2\2\2\u011c"+
      "\u011b\3\2\2\2\u011d\t\3\2\2\2\u011e\u011f\7\20\2\2\u011f\13\3\2\2\2\u0120"+
      "\u0121\7\21\2\2\u0121\r\3\2\2\2\u0122\u0123\7\22\2\2\u0123\17\3\2\2\2"+
      "\u0124\u0125\7\23\2\2\u0125\21\3\2\2\2\u0126\u0127\7-\2\2\u0127\u0129"+
      "\5\u00fc\177\2\u0128\u012a\7\4\2\2\u0129\u0128\3\2\2\2\u0129\u012a\3\2"+
      "\2\2\u012a\23\3\2\2\2\u012b\u012f\5\26\f\2\u012c\u012e\5$\23\2\u012d\u012c"+
      "\3\2\2\2\u012e\u0131\3\2\2\2\u012f\u012d\3\2\2\2\u012f\u0130\3\2\2\2\u0130"+
      "\u0135\3\2\2\2\u0131\u012f\3\2\2\2\u0132\u0134\5\62\32\2\u0133\u0132\3"+
      "\2\2\2\u0134\u0137\3\2\2\2\u0135\u0133\3\2\2\2\u0135\u0136\3\2\2\2\u0136"+
      "\u0138\3\2\2\2\u0137\u0135\3\2\2\2\u0138\u0139\5\32\16\2\u0139\25\3\2"+
      "\2\2\u013a\u013c\7+\2\2\u013b\u013d\5\34\17\2\u013c\u013b\3\2\2\2\u013c"+
      "\u013d\3\2\2\2\u013d\u013e\3\2\2\2\u013e\u0140\5\u00eex\2\u013f\u0141"+
      "\5\30\r\2\u0140\u013f\3\2\2\2\u0140\u0141\3\2\2\2\u0141\27\3\2\2\2\u0142"+
      "\u0143\7)\2\2\u0143\u0144\5\u00eex\2\u0144\31\3\2\2\2\u0145\u0146\7%\2"+
      "\2\u0146\33\3\2\2\2\u0147\u0148\7*\2\2\u0148\u0149\5\36\20\2\u0149\35"+
      "\3\2\2\2\u014a\u014d\5 \21\2\u014b\u014d\5\"\22\2\u014c\u014a\3\2\2\2"+
      "\u014c\u014b\3\2\2\2\u014d\37\3\2\2\2\u014e\u014f\78\2\2\u014f!\3\2\2"+
      "\2\u0150\u0151\79\2\2\u0151#\3\2\2\2\u0152\u0153\7.\2\2\u0153\u0154\5"+
      "&\24\2\u0154\u0155\7\5\2\2\u0155\u0157\5(\25\2\u0156\u0158\5*\26\2\u0157"+
      "\u0156\3\2\2\2\u0157\u0158\3\2\2\2\u0158\u015a\3\2\2\2\u0159\u015b\7\4"+
      "\2\2\u015a\u0159\3\2\2\2\u015a\u015b\3\2\2\2\u015b%\3\2\2\2\u015c\u015d"+
      "\5\u00eex\2\u015d\'\3\2\2\2\u015e\u015f\5\u00eex\2\u015f)\3\2\2\2\u0160"+
      "\u0161\7\27\2\2\u0161\u0162\5,\27\2\u0162+\3\2\2\2\u0163\u0166\5.\30\2"+
      "\u0164\u0166\5\60\31\2\u0165\u0163\3\2\2\2\u0165\u0164\3\2\2\2\u0166-"+
      "\3\2\2\2\u0167\u0168\7\20\2\2\u0168/\3\2\2\2\u0169\u016a\7\21\2\2\u016a"+
      "\61\3\2\2\2\u016b\u016e\5\64\33\2\u016c\u016e\58\35\2\u016d\u016b\3\2"+
      "\2\2\u016d\u016c\3\2\2\2\u016e\63\3\2\2\2\u016f\u0170\5\u00eex\2\u0170"+
      "\u0172\7\6\2\2\u0171\u0173\5\66\34\2\u0172\u0171\3\2\2\2\u0172\u0173\3"+
      "\2\2\2\u0173\u0174\3\2\2\2\u0174\u0175\7\7\2\2\u0175\65\3\2\2\2\u0176"+
      "\u017b\5\u00eex\2\u0177\u0178\7\b\2\2\u0178\u017a\5\u00eex\2\u0179\u0177"+
      "\3\2\2\2\u017a\u017d\3\2\2\2\u017b\u0179\3\2\2\2\u017b\u017c\3\2\2\2\u017c"+
      "\67\3\2\2\2\u017d\u017b\3\2\2\2\u017e\u017f\5:\36\2\u017f\u0180\7\5\2"+
      "\2\u0180\u0181\7*\2\2\u0181\u0183\5<\37\2\u0182\u0184\5P)\2\u0183\u0182"+
      "\3\2\2\2\u0183\u0184\3\2\2\2\u01849\3\2\2\2\u0185\u018a\5\u00eex\2\u0186"+
      "\u0187\7\t\2\2\u0187\u0189\5\u00eex\2\u0188\u0186\3\2\2\2\u0189\u018c"+
      "\3\2\2\2\u018a\u0188\3\2\2\2\u018a\u018b\3\2\2\2\u018b;\3\2\2\2\u018c"+
      "\u018a\3\2\2\2\u018d\u0192\5> \2\u018e\u018f\7\b\2\2\u018f\u0191\5> \2"+
      "\u0190\u018e\3\2\2\2\u0191\u0194\3\2\2\2\u0192\u0190\3\2\2\2\u0192\u0193"+
      "\3\2\2\2\u0193=\3\2\2\2\u0194\u0192\3\2\2\2\u0195\u0197\5@!\2\u0196\u0198"+
      "\5D#\2\u0197\u0196\3\2\2\2\u0197\u0198\3\2\2\2\u0198\u019a\3\2\2\2\u0199"+
      "\u019b\5F$\2\u019a\u0199\3\2\2\2\u019a\u019b\3\2\2\2\u019b\u019d\3\2\2"+
      "\2\u019c\u019e\5H%\2\u019d\u019c\3\2\2\2\u019d\u019e\3\2\2\2\u019e\u01a0"+
      "\3\2\2\2\u019f\u01a1\5J&\2\u01a0\u019f\3\2\2\2\u01a0\u01a1\3\2\2\2\u01a1"+
      "\u01a3\3\2\2\2\u01a2\u01a4\5L\'\2\u01a3\u01a2\3\2\2\2\u01a3\u01a4\3\2"+
      "\2\2\u01a4\u01a6\3\2\2\2\u01a5\u01a7\5N(\2\u01a6\u01a5\3\2\2\2\u01a6\u01a7"+
      "\3\2\2\2\u01a7?\3\2\2\2\u01a8\u01ad\5B\"\2\u01a9\u01aa\7\t\2\2\u01aa\u01ac"+
      "\5B\"\2\u01ab\u01a9\3\2\2\2\u01ac\u01af\3\2\2\2\u01ad\u01ab\3\2\2\2\u01ad"+
      "\u01ae\3\2\2\2\u01aeA\3\2\2\2\u01af\u01ad\3\2\2\2\u01b0\u01b3\5\u00ee"+
      "x\2\u01b1\u01b3\5\u00f4{\2\u01b2\u01b0\3\2\2\2\u01b2\u01b1\3\2\2\2\u01b3"+
      "C\3\2\2\2\u01b4\u01b5\7\5\2\2\u01b5\u01ba\5\u00eex\2\u01b6\u01b7\5\u00f0"+
      "y\2\u01b7\u01b8\7\n\2\2\u01b8\u01b9\5\u00f0y\2\u01b9\u01bb\3\2\2\2\u01ba"+
      "\u01b6\3\2\2\2\u01ba\u01bb\3\2\2\2\u01bbE\3\2\2\2\u01bc\u01bd\7#\2\2\u01bd"+
      "\u01be\5\u00eex\2\u01beG\3\2\2\2\u01bf\u01c0\7\13\2\2\u01c0I\3\2\2\2\u01c1"+
      "\u01c2\7\27\2\2\u01c2\u01c3\5\u00eex\2\u01c3K\3\2\2\2\u01c4\u01c5\7<\2"+
      "\2\u01c5\u01c6\5\u00ecw\2\u01c6M\3\2\2\2\u01c7\u01c8\7\35\2\2\u01c8\u01c9"+
      "\5\u00ecw\2\u01c9O\3\2\2\2\u01ca\u01cb\7/\2\2\u01cb\u01cd\5R*\2\u01cc"+
      "\u01ce\5T+\2\u01cd\u01cc\3\2\2\2\u01cd\u01ce\3\2\2\2\u01ceQ\3\2\2\2\u01cf"+
      "\u01d4\5X-\2\u01d0\u01d1\7\b\2\2\u01d1\u01d3\5X-\2\u01d2\u01d0\3\2\2\2"+
      "\u01d3\u01d6\3\2\2\2\u01d4\u01d2\3\2\2\2\u01d4\u01d5\3\2\2\2\u01d5S\3"+
      "\2\2\2\u01d6\u01d4\3\2\2\2\u01d7\u01d8\7\65\2\2\u01d8\u01d9\7\f\2\2\u01d9"+
      "\u01da\5V,\2\u01da\u01db\7\r\2\2\u01dbU\3\2\2\2\u01dc\u01de\5\62\32\2"+
      "\u01dd\u01dc\3\2\2\2\u01de\u01e1\3\2\2\2\u01df\u01dd\3\2\2\2\u01df\u01e0"+
      "\3\2\2\2\u01e0W\3\2\2\2\u01e1\u01df\3\2\2\2\u01e2\u01f7\5b\62\2\u01e3"+
      "\u01f7\5\\/\2\u01e4\u01f7\5^\60\2\u01e5\u01f7\5h\65\2\u01e6\u01f7\5p9"+
      "\2\u01e7\u01f7\5v<\2\u01e8\u01f7\5\u0084C\2\u01e9\u01f7\5\u008aF\2\u01ea"+
      "\u01f7\5\u008eH\2\u01eb\u01f7\5\u0092J\2\u01ec\u01f7\5\u009aN\2\u01ed"+
      "\u01f7\5\u009cO\2\u01ee\u01f7\5\u00a4S\2\u01ef\u01f7\5\u00aaV\2\u01f0"+
      "\u01f7\5\u00b2Z\2\u01f1\u01f7\5\u00b6\\\2\u01f2\u01f7\5\u00ccg\2\u01f3"+
      "\u01f7\5\u00d0i\2\u01f4\u01f7\5\u00e2r\2\u01f5\u01f7\5\u00e8u\2\u01f6"+
      "\u01e2\3\2\2\2\u01f6\u01e3\3\2\2\2\u01f6\u01e4\3\2\2\2\u01f6\u01e5\3\2"+
      "\2\2\u01f6\u01e6\3\2\2\2\u01f6\u01e7\3\2\2\2\u01f6\u01e8\3\2\2\2\u01f6"+
      "\u01e9\3\2\2\2\u01f6\u01ea\3\2\2\2\u01f6\u01eb\3\2\2\2\u01f6\u01ec\3\2"+
      "\2\2\u01f6\u01ed\3\2\2\2\u01f6\u01ee\3\2\2\2\u01f6\u01ef\3\2\2\2\u01f6"+
      "\u01f0\3\2\2\2\u01f6\u01f1\3\2\2\2\u01f6\u01f2\3\2\2\2\u01f6\u01f3\3\2"+
      "\2\2\u01f6\u01f4\3\2\2\2\u01f6\u01f5\3\2\2\2\u01f7Y\3\2\2\2\u01f8\u01f9"+
      "\5@!\2\u01f9\u01fa\7\3\2\2\u01fa[\3\2\2\2\u01fb\u01fc\5@!\2\u01fc\u01fd"+
      "\5\u00eav\2\u01fd]\3\2\2\2\u01fe\u01ff\5Z.\2\u01ff\u0201\5`\61\2\u0200"+
      "\u0202\5\u00eav\2\u0201\u0200\3\2\2\2\u0201\u0202\3\2\2\2\u0202_\3\2\2"+
      "\2\u0203\u0206\5\u00f4{\2\u0204\u0206\5\u00eex\2\u0205\u0203\3\2\2\2\u0205"+
      "\u0204\3\2\2\2\u0206a\3\2\2\2\u0207\u0209\5Z.\2\u0208\u0207\3\2\2\2\u0208"+
      "\u0209\3\2\2\2\u0209\u020a\3\2\2\2\u020a\u020b\7\26\2\2\u020b\u020c\7"+
      "\6\2\2\u020c\u020d\5d\63\2\u020d\u020f\7\7\2\2\u020e\u0210\5\u00eav\2"+
      "\u020f\u020e\3\2\2\2\u020f\u0210\3\2\2\2\u0210c\3\2\2\2\u0211\u0216\5"+
      "f\64\2\u0212\u0213\7\b\2\2\u0213\u0215\5f\64\2\u0214\u0212\3\2\2\2\u0215"+
      "\u0218\3\2\2\2\u0216\u0214\3\2\2\2\u0216\u0217\3\2\2\2\u0217e\3\2\2\2"+
      "\u0218\u0216\3\2\2\2\u0219\u021c\5\u00eex\2\u021a\u021c\5\u00f4{\2\u021b"+
      "\u0219\3\2\2\2\u021b\u021a\3\2\2\2\u021cg\3\2\2\2\u021d\u021f\5Z.\2\u021e"+
      "\u021d\3\2\2\2\u021e\u021f\3\2\2\2\u021f\u0220\3\2\2\2\u0220\u0221\7\31"+
      "\2\2\u0221\u0222\7\6\2\2\u0222\u0223\5j\66\2\u0223\u0224\7\b\2\2\u0224"+
      "\u0227\5l\67\2\u0225\u0226\7\b\2\2\u0226\u0228\5n8\2\u0227\u0225\3\2\2"+
      "\2\u0227\u0228\3\2\2\2\u0228\u0229\3\2\2\2\u0229\u022b\7\7\2\2\u022a\u022c"+
      "\5\u00eav\2\u022b\u022a\3\2\2\2\u022b\u022c\3\2\2\2\u022ci\3\2\2\2\u022d"+
      "\u022e\5\u00f8}\2\u022ek\3\2\2\2\u022f\u0232\5\u00f4{\2\u0230\u0232\5"+
      "\u00eex\2\u0231\u022f\3\2\2\2\u0231\u0230\3\2\2\2\u0232m\3\2\2\2\u0233"+
      "\u0234\5\u00f6|\2\u0234o\3\2\2\2\u0235\u0237\5Z.\2\u0236\u0235\3\2\2\2"+
      "\u0236\u0237\3\2\2\2\u0237\u0238\3\2\2\2\u0238\u0239\7\30\2\2\u0239\u023a"+
      "\7\6\2\2\u023a\u023d\5r:\2\u023b\u023c\7\b\2\2\u023c\u023e\5t;\2\u023d"+
      "\u023b\3\2\2\2\u023d\u023e\3\2\2\2\u023e\u023f\3\2\2\2\u023f\u0241\7\7"+
      "\2\2\u0240\u0242\5\u00eav\2\u0241\u0240\3\2\2\2\u0241\u0242\3\2\2\2\u0242"+
      "q\3\2\2\2\u0243\u0244\5\u00eex\2\u0244s\3\2\2\2\u0245\u0246\5\u00eex\2"+
      "\u0246u\3\2\2\2\u0247\u024a\5x=\2\u0248\u024a\5|?\2\u0249\u0247\3\2\2"+
      "\2\u0249\u0248\3\2\2\2\u024a\u024c\3\2\2\2\u024b\u024d\5\u00eav\2\u024c"+
      "\u024b\3\2\2\2\u024c\u024d\3\2\2\2\u024dw\3\2\2\2\u024e\u0250\5Z.\2\u024f"+
      "\u024e\3\2\2\2\u024f\u0250\3\2\2\2\u0250\u0251\3\2\2\2\u0251\u0252\7\32"+
      "\2\2\u0252\u0253\7\6\2\2\u0253\u0254\5z>\2\u0254\u0256\7\7\2\2\u0255\u0257"+
      "\5\u00eav\2\u0256\u0255\3\2\2\2\u0256\u0257\3\2\2\2\u0257y\3\2\2\2\u0258"+
      "\u0259\5\u00f6|\2\u0259{\3\2\2\2\u025a\u025c\5Z.\2\u025b\u025a\3\2\2\2"+
      "\u025b\u025c\3\2\2\2\u025c\u025d\3\2\2\2\u025d\u025e\7\32\2\2\u025e\u025f"+
      "\7\6\2\2\u025f\u0260\5~@\2\u0260\u0261\7\b\2\2\u0261\u0264\5\u0080A\2"+
      "\u0262\u0263\7\b\2\2\u0263\u0265\5\u0082B\2\u0264\u0262\3\2\2\2\u0264"+
      "\u0265\3\2\2\2\u0265\u0266\3\2\2\2\u0266\u0268\7\7\2\2\u0267\u0269\5\u00ea"+
      "v\2\u0268\u0267\3\2\2\2\u0268\u0269\3\2\2\2\u0269}\3\2\2\2\u026a\u026b"+
      "\5\u00f8}\2\u026b\177\3\2\2\2\u026c\u026f\5\u00f4{\2\u026d\u026f\5\u00ee"+
      "x\2\u026e\u026c\3\2\2\2\u026e\u026d\3\2\2\2\u026f\u0081\3\2\2\2\u0270"+
      "\u0271\5\u00f6|\2\u0271\u0083\3\2\2\2\u0272\u0274\5Z.\2\u0273\u0272\3"+
      "\2\2\2\u0273\u0274\3\2\2\2\u0274\u0275\3\2\2\2\u0275\u0276\7\34\2\2\u0276"+
      "\u027a\7\6\2\2\u0277\u0278\5\u0086D\2\u0278\u0279\7\b\2\2\u0279\u027b"+
      "\3\2\2\2\u027a\u0277\3\2\2\2\u027a\u027b\3\2\2\2\u027b\u027c\3\2\2\2\u027c"+
      "\u027d\5\u0088E\2\u027d\u027f\7\7\2\2\u027e\u0280\5\u00eav\2\u027f\u027e"+
      "\3\2\2\2\u027f\u0280\3\2\2\2\u0280\u0085\3\2\2\2\u0281\u0282\5\u00f8}"+
      "\2\u0282\u0087\3\2\2\2\u0283\u0284\5\u00eex\2\u0284\u0089\3\2\2\2\u0285"+
      "\u0286\5Z.\2\u0286\u0287\7 \2\2\u0287\u0288\7\6\2\2\u0288\u0289\5\u008c"+
      "G\2\u0289\u028b\7\7\2\2\u028a\u028c\5\u00eav\2\u028b\u028a\3\2\2\2\u028b"+
      "\u028c\3\2\2\2\u028c\u008b\3\2\2\2\u028d\u028e\5\u00eex\2\u028e\u008d"+
      "\3\2\2\2\u028f\u0291\5Z.\2\u0290\u028f\3\2\2\2\u0290\u0291\3\2\2\2\u0291"+
      "\u0292\3\2\2\2\u0292\u0293\7!\2\2\u0293\u0294\7\6\2\2\u0294\u0295\5\u0090"+
      "I\2\u0295\u0297\7\7\2\2\u0296\u0298\5\u00eav\2\u0297\u0296\3\2\2\2\u0297"+
      "\u0298\3\2\2\2\u0298\u008f\3\2\2\2\u0299\u029a\5\u00f2z\2\u029a\u0091"+
      "\3\2\2\2\u029b\u029d\5Z.\2\u029c\u029b\3\2\2\2\u029c\u029d\3\2\2\2\u029d"+
      "\u029e\3\2\2\2\u029e\u029f\7\"\2\2\u029f\u02a0\7\6\2\2\u02a0\u02a1\5\u0094"+
      "K\2\u02a1\u02a2\7\b\2\2\u02a2\u02a5\5\u0096L\2\u02a3\u02a4\7\b\2\2\u02a4"+
      "\u02a6\5\u0098M\2\u02a5\u02a3\3\2\2\2\u02a5\u02a6\3\2\2\2\u02a6\u02a7"+
      "\3\2\2\2\u02a7\u02a9\7\7\2\2\u02a8\u02aa\5\u00eav\2\u02a9\u02a8\3\2\2"+
      "\2\u02a9\u02aa\3\2\2\2\u02aa\u0093\3\2\2\2\u02ab\u02ac\5\u00eex\2\u02ac"+
      "\u0095\3\2\2\2\u02ad\u02ae\5\u00f6|\2\u02ae\u0097\3\2\2\2\u02af\u02b0"+
      "\5\u00eex\2\u02b0\u0099\3\2\2\2\u02b1\u02b3\5Z.\2\u02b2\u02b1\3\2\2\2"+
      "\u02b2\u02b3\3\2\2\2\u02b3\u02b4\3\2\2\2\u02b4\u02b5\7\'\2\2\u02b5\u02b6"+
      "\7\6\2\2\u02b6\u02b8\7\7\2\2\u02b7\u02b9\5\u00eav\2\u02b8\u02b7\3\2\2"+
      "\2\u02b8\u02b9\3\2\2\2\u02b9\u009b\3\2\2\2\u02ba\u02bc\5Z.\2\u02bb\u02ba"+
      "\3\2\2\2\u02bb\u02bc\3\2\2\2\u02bc\u02bd\3\2\2\2\u02bd\u02be\7&\2\2\u02be"+
      "\u02bf\7\6\2\2\u02bf\u02c0\5\u009eP\2\u02c0\u02c1\7\b\2\2\u02c1\u02c4"+
      "\5\u00a0Q\2\u02c2\u02c3\7\b\2\2\u02c3\u02c5\5\u00a2R\2\u02c4\u02c2\3\2"+
      "\2\2\u02c4\u02c5\3\2\2\2\u02c5\u02c6\3\2\2\2\u02c6\u02c8\7\7\2\2\u02c7"+
      "\u02c9\5\u00eav\2\u02c8\u02c7\3\2\2\2\u02c8\u02c9\3\2\2\2\u02c9\u009d"+
      "\3\2\2\2\u02ca\u02cb\5\u00eex\2\u02cb\u009f\3\2\2\2\u02cc\u02cd\5\u00f6"+
      "|\2\u02cd\u00a1\3\2\2\2\u02ce\u02cf\5\u00f6|\2\u02cf\u00a3\3\2\2\2\u02d0"+
      "\u02d2\5Z.\2\u02d1\u02d0\3\2\2\2\u02d1\u02d2\3\2\2\2\u02d2\u02d3\3\2\2"+
      "\2\u02d3\u02d4\7(\2\2\u02d4\u02d5\7\6\2\2\u02d5\u02d6\5\u00a6T\2\u02d6"+
      "\u02d7\7\b\2\2\u02d7\u02d8\5\u00a8U\2\u02d8\u02da\7\7\2\2\u02d9\u02db"+
      "\5\u00eav\2\u02da\u02d9\3\2\2\2\u02da\u02db\3\2\2\2\u02db\u00a5\3\2\2"+
      "\2\u02dc\u02dd\5\u00eex\2\u02dd\u00a7\3\2\2\2\u02de\u02df\5\u00eex\2\u02df"+
      "\u00a9\3\2\2\2\u02e0\u02e2\5Z.\2\u02e1\u02e0\3\2\2\2\u02e1\u02e2\3\2\2"+
      "\2\u02e2\u02e3\3\2\2\2\u02e3\u02e4\7,\2\2\u02e4\u02e5\7\6\2\2\u02e5\u02e6"+
      "\5\u00acW\2\u02e6\u02e7\7\b\2\2\u02e7\u02ea\5\u00aeX\2\u02e8\u02e9\7\b"+
      "\2\2\u02e9\u02eb\5\u00b0Y\2\u02ea\u02e8\3\2\2\2\u02ea\u02eb\3\2\2\2\u02eb"+
      "\u02ec\3\2\2\2\u02ec\u02ee\7\7\2\2\u02ed\u02ef\5\u00eav\2\u02ee\u02ed"+
      "\3\2\2\2\u02ee\u02ef\3\2\2\2\u02ef\u00ab\3\2\2\2\u02f0\u02f1\5\u00f8}"+
      "\2\u02f1\u00ad\3\2\2\2\u02f2\u02f3\5\u00eex\2\u02f3\u00af\3\2\2\2\u02f4"+
      "\u02f5\5\u00eex\2\u02f5\u00b1\3\2\2\2\u02f6\u02f8\5Z.\2\u02f7\u02f6\3"+
      "\2\2\2\u02f7\u02f8\3\2\2\2\u02f8\u02f9\3\2\2\2\u02f9\u02fa\7\61\2\2\u02fa"+
      "\u02fb\7\6\2\2\u02fb\u02fc\5\u00b4[\2\u02fc\u02fe\7\7\2\2\u02fd\u02ff"+
      "\5\u00eav\2\u02fe\u02fd\3\2\2\2\u02fe\u02ff\3\2\2\2\u02ff\u00b3\3\2\2"+
      "\2\u0300\u0301\5\u00eex\2\u0301\u00b5\3\2\2\2\u0302\u0306\5\u00b8]\2\u0303"+
      "\u0306\5\u00bc_\2\u0304\u0306\5\u00c4c\2\u0305\u0302\3\2\2\2\u0305\u0303"+
      "\3\2\2\2\u0305\u0304\3\2\2\2\u0306\u00b7\3\2\2\2\u0307\u0309\5Z.\2\u0308"+
      "\u0307\3\2\2\2\u0308\u0309\3\2\2\2\u0309\u030a\3\2\2\2\u030a\u030b\7\62"+
      "\2\2\u030b\u030c\7\6\2\2\u030c\u030d\5\u00ba^\2\u030d\u030f\7\7\2\2\u030e"+
      "\u0310\5\u00eav\2\u030f\u030e\3\2\2\2\u030f\u0310\3\2\2\2\u0310\u00b9"+
      "\3\2\2\2\u0311\u0312\5\u00f6|\2\u0312\u00bb\3\2\2\2\u0313\u0315\5Z.\2"+
      "\u0314\u0313\3\2\2\2\u0314\u0315\3\2\2\2\u0315\u0316\3\2\2\2\u0316\u0317"+
      "\7\62\2\2\u0317\u0318\7\6\2\2\u0318\u0319\5\u00be`\2\u0319\u031a\7\b\2"+
      "\2\u031a\u031b\5\u00c0a\2\u031b\u031c\7\b\2\2\u031c\u031d\5\u00c2b\2\u031d"+
      "\u031f\7\7\2\2\u031e\u0320\5\u00eav\2\u031f\u031e\3\2\2\2\u031f\u0320"+
      "\3\2\2\2\u0320\u00bd\3\2\2\2\u0321\u0322\5\u00eex\2\u0322\u00bf\3\2\2"+
      "\2\u0323\u0324\5\u00f6|\2\u0324\u00c1\3\2\2\2\u0325\u0326\5\u00f8}\2\u0326"+
      "\u00c3\3\2\2\2\u0327\u0329\5Z.\2\u0328\u0327\3\2\2\2\u0328\u0329\3\2\2"+
      "\2\u0329\u032a\3\2\2\2\u032a\u032b\7\62\2\2\u032b\u032c\7\6\2\2\u032c"+
      "\u032d\5\u00c6d\2\u032d\u032e\7\b\2\2\u032e\u032f\5\u00c8e\2\u032f\u0330"+
      "\7\b\2\2\u0330\u0331\5\u00caf\2\u0331\u0333\7\7\2\2\u0332\u0334\5\u00ea"+
      "v\2\u0333\u0332\3\2\2\2\u0333\u0334\3\2\2\2\u0334\u00c5\3\2\2\2\u0335"+
      "\u0336\5\u00eex\2\u0336\u00c7\3\2\2\2\u0337\u0338\5\u00f6|\2\u0338\u00c9"+
      "\3\2\2\2\u0339\u033a\5\u00eex\2\u033a\u00cb\3\2\2\2\u033b\u033d\5Z.\2"+
      "\u033c\u033b\3\2\2\2\u033c\u033d\3\2\2\2\u033d\u033e\3\2\2\2\u033e\u033f"+
      "\7\63\2\2\u033f\u0340\7\6\2\2\u0340\u0341\5\u00ceh\2\u0341\u0343\7\7\2"+
      "\2\u0342\u0344\5\u00eav\2\u0343\u0342\3\2\2\2\u0343\u0344\3\2\2\2\u0344"+
      "\u00cd\3\2\2\2\u0345\u0348\5\u00eex\2\u0346\u0348\5\u00f4{\2\u0347\u0345"+
      "\3\2\2\2\u0347\u0346\3\2\2\2\u0348\u00cf\3\2\2\2\u0349\u034b\5Z.\2\u034a"+
      "\u0349\3\2\2\2\u034a\u034b\3\2\2\2\u034b\u034c\3\2\2\2\u034c\u034d\7\66"+
      "\2\2\u034d\u034e\7\6\2\2\u034e\u034f\5\u00d2j\2\u034f\u0350\7\b\2\2\u0350"+
      "\u0351\5\u00d4k\2\u0351\u0352\7\b\2\2\u0352\u0353\5\u00d6l\2\u0353\u0355"+
      "\7\7\2\2\u0354\u0356\5\u00eav\2\u0355\u0354\3\2\2\2\u0355\u0356\3\2\2"+
      "\2\u0356\u00d1\3\2\2\2\u0357\u0358\5\u00eex\2\u0358\u00d3\3\2\2\2\u0359"+
      "\u035a\5\u00f8}\2\u035a\u00d5\3\2\2\2\u035b\u0361\5\u00d8m\2\u035c\u0361"+
      "\5\u00dan\2\u035d\u0361\5\u00dco\2\u035e\u0361\5\u00dep\2\u035f\u0361"+
      "\5\u00e0q\2\u0360\u035b\3\2\2\2\u0360\u035c\3\2\2\2\u0360\u035d\3\2\2"+
      "\2\u0360\u035e\3\2\2\2\u0360\u035f\3\2\2\2\u0361\u00d7\3\2\2\2\u0362\u0363"+
      "\7\33\2\2\u0363\u00d9\3\2\2\2\u0364\u0365\7\64\2\2\u0365\u00db\3\2\2\2"+
      "\u0366\u0367\7$\2\2\u0367\u00dd\3\2\2\2\u0368\u0369\7\37\2\2\u0369\u00df"+
      "\3\2\2\2\u036a\u036b\7\36\2\2\u036b\u00e1\3\2\2\2\u036c\u036e\5Z.\2\u036d"+
      "\u036c\3\2\2\2\u036d\u036e\3\2\2\2\u036e\u036f\3\2\2\2\u036f\u0370\7\67"+
      "\2\2\u0370\u0371\7\6\2\2\u0371\u0372\5\u00e4s\2\u0372\u0373\7\b\2\2\u0373"+
      "\u0374\5\u00e6t\2\u0374\u0376\7\7\2\2\u0375\u0377\5\u00eav\2\u0376\u0375"+
      "\3\2\2\2\u0376\u0377\3\2\2\2\u0377\u00e3\3\2\2\2\u0378\u037b\5\u00eex"+
      "\2\u0379\u037b\5\u00f4{\2\u037a\u0378\3\2\2\2\u037a\u0379\3\2\2\2\u037b"+
      "\u00e5\3\2\2\2\u037c\u037d\5\u00f0y\2\u037d\u00e7\3\2\2\2\u037e\u0380"+
      "\5Z.\2\u037f\u037e\3\2\2\2\u037f\u0380\3\2\2\2\u0380\u0381\3\2\2\2\u0381"+
      "\u0382\7:\2\2\u0382\u0383\7\6\2\2\u0383\u0385\7\7\2\2\u0384\u0386\5\u00ea"+
      "v\2\u0385\u0384\3\2\2\2\u0385\u0386\3\2\2\2\u0386\u00e9\3\2\2\2\u0387"+
      "\u0388\7\27\2\2\u0388\u0389\5\u00eex\2\u0389\u00eb\3\2\2\2\u038a\u038b"+
      "\5\u00f6|\2\u038b\u00ed\3\2\2\2\u038c\u038d\t\2\2\2\u038d\u00ef\3\2\2"+
      "\2\u038e\u038f\7\17\2\2\u038f\u00f1\3\2\2\2\u0390\u0391\7\24\2\2\u0391"+
      "\u00f3\3\2\2\2\u0392\u0393\t\3\2\2\u0393\u00f5\3\2\2\2\u0394\u0395\t\3"+
      "\2\2\u0395\u00f7\3\2\2\2\u0396\u0397\5\u00f6|\2\u0397\u00f9\3\2\2\2\u0398"+
      "\u0399\5\u00f8}\2\u0399\u00fb\3\2\2\2\u039a\u039b\5\u00f8}\2\u039b\u00fd"+
      "\3\2\2\2^\u0102\u0107\u0116\u011c\u0129\u012f\u0135\u013c\u0140\u014c"+
      "\u0157\u015a\u0165\u016d\u0172\u017b\u0183\u018a\u0192\u0197\u019a\u019d"+
      "\u01a0\u01a3\u01a6\u01ad\u01b2\u01ba\u01cd\u01d4\u01df\u01f6\u0201\u0205"+
      "\u0208\u020f\u0216\u021b\u021e\u0227\u022b\u0231\u0236\u023d\u0241\u0249"+
      "\u024c\u024f\u0256\u025b\u0264\u0268\u026e\u0273\u027a\u027f\u028b\u0290"+
      "\u0297\u029c\u02a5\u02a9\u02b2\u02b8\u02bb\u02c4\u02c8\u02d1\u02da\u02e1"+
      "\u02ea\u02ee\u02f7\u02fe\u0305\u0308\u030f\u0314\u031f\u0328\u0333\u033c"+
      "\u0343\u0347\u034a\u0355\u0360\u036d\u0376\u037a\u037f\u0385";
  public static final ATN _ATN =
    new ATNDeserializer().deserialize(_serializedATN.toCharArray());
  static {
    _decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
    for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
      _decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
    }
  }
}
