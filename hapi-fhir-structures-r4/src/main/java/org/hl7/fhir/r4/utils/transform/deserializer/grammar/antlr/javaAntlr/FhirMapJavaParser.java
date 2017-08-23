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
		EVALUATE=37, EXTENDS=38, FOR=39, GROUP=40, ID=41, IMPORTS=42, INPUT=43, 
		MAKE=44, MAP=45, POINTER=46, QTY=47, REFERENCE=48, SYSTEM=49, THEN=50, 
		TRANSLATE=51, TRUNCATE=52, TYPES=53, TYPE_TYPES=54, UUID=55, USES=56, 
		WHERE=57, IDENTIFIER=58, WS=59, LINE_COMMENT=60;
	public static final int
		RULE_mappingUnit = 0, RULE_keyMap = 1, RULE_keyUses = 2, RULE_keyUsesName = 3, 
		RULE_keyUsesNameSource = 4, RULE_keyUsesNameTarget = 5, RULE_keyUsesNameQueried = 6, 
		RULE_keyUsesNameProduced = 7, RULE_keyImports = 8, RULE_group = 9, RULE_groupStart = 10, 
		RULE_groupExtends = 11, RULE_groupEnd = 12, RULE_groupType = 13, RULE_groupTypeValue = 14, 
		RULE_groupTypeType = 15, RULE_groupTypeTypeTypes = 16, RULE_ruleInput = 17, 
		RULE_ruleInputName = 18, RULE_ruleInputType = 19, RULE_ruleInputMode = 20, 
		RULE_ruleInputModes = 21, RULE_ruleInputModesSource = 22, RULE_ruleInputModesTarget = 23, 
		RULE_ruleInstance = 24, RULE_ruleName = 25, RULE_ruleSources = 26, RULE_ruleSource = 27, 
		RULE_ruleType = 28, RULE_ruleDefault = 29, RULE_ruleListOption = 30, RULE_ruleVariable = 31, 
		RULE_ruleContext = 32, RULE_ruleContextElement = 33, RULE_ruleWherePath = 34, 
		RULE_ruleCheckPath = 35, RULE_ruleMake = 36, RULE_ruleTargets = 37, RULE_ruleDependents = 38, 
		RULE_ruleTarget = 39, RULE_ruleTargetContext = 40, RULE_ruleTargetAs = 41, 
		RULE_ruleTargetAssign = 42, RULE_ruleTargetAssignValue = 43, RULE_ruleTargetAppend = 44, 
		RULE_ruleTargetAppendSources = 45, RULE_ruleTargetAppendSource = 46, RULE_ruleTargetC = 47, 
		RULE_ruleTargetCSystem = 48, RULE_ruleTargetCCode = 49, RULE_ruleTargetCDisplay = 50, 
		RULE_ruleTargetCast = 51, RULE_ruleTargetCastSource = 52, RULE_ruleTargetCastType = 53, 
		RULE_ruleTargetCC = 54, RULE_ruleTargetCC1 = 55, RULE_ruleTargetCC1Text = 56, 
		RULE_ruleTargetCC2 = 57, RULE_ruleTargetCC2System = 58, RULE_ruleTargetCC2Code = 59, 
		RULE_ruleTargetCC2Display = 60, RULE_ruleTargetCp = 61, RULE_ruleTargetCpSystem = 62, 
		RULE_ruleTargetCpVariable = 63, RULE_ruleTargetCopy = 64, RULE_ruleTargetCopySource = 65, 
		RULE_ruleTargetCreate = 66, RULE_ruleTargetCreateType = 67, RULE_ruleTargetDateOp = 68, 
		RULE_ruleTargetDateOpVariable = 69, RULE_ruleTargetDateOpOperation = 70, 
		RULE_ruleTargetDateOpVariable2 = 71, RULE_ruleTargetEscape = 72, RULE_ruleTargetEscapeVariable = 73, 
		RULE_ruleTargetEscapeString1 = 74, RULE_ruleTargetEscapeString2 = 75, 
		RULE_ruleTargetEvaluate = 76, RULE_ruleTargetEvaluateObject = 77, RULE_ruleTargetEvaluateObjectElement = 78, 
		RULE_ruleTargetId = 79, RULE_ruleTargetIdSystem = 80, RULE_ruleTargetIdValue = 81, 
		RULE_ruleTargetIdType = 82, RULE_ruleTargetPointer = 83, RULE_ruleTargetPointerResource = 84, 
		RULE_ruleTargetQty = 85, RULE_ruleTargetQty1 = 86, RULE_ruleTargetQty1Text = 87, 
		RULE_ruleTargetQty2 = 88, RULE_ruleTargetQty2Value = 89, RULE_ruleTargetQty2UnitString = 90, 
		RULE_ruleTargetQty2System = 91, RULE_ruleTargetQty3 = 92, RULE_ruleTargetQty3Value = 93, 
		RULE_ruleTargetQty3UnitString = 94, RULE_ruleTargetQty3CodeVariable = 95, 
		RULE_ruleTargetReference = 96, RULE_ruleTargetReferenceSource = 97, RULE_ruleTargetTranslate = 98, 
		RULE_ruleTargetTranslateSource = 99, RULE_ruleTargetTranslateMap = 100, 
		RULE_ruleTargetTranslateOutput = 101, RULE_ruleTargetTranslateOutputCode = 102, 
		RULE_ruleTargetTranslateOutputSystem = 103, RULE_ruleTargetTranslateOutputDisplay = 104, 
		RULE_ruleTargetTranslateOutputCoding = 105, RULE_ruleTargetTranslateOutputCodeableConcept = 106, 
		RULE_ruleTargetTruncate = 107, RULE_ruleTargetTruncateSource = 108, RULE_ruleTargetTruncateLength = 109, 
		RULE_ruleTargetUuid = 110, RULE_ruleTargetVariable = 111, RULE_fhirPath = 112, 
		RULE_identifier = 113, RULE_integer = 114, RULE_quotedIdentifier = 115, 
		RULE_quotedStringWQuotes = 116, RULE_quotedString = 117, RULE_quotedUrl = 118, 
		RULE_structureDefinition = 119, RULE_structureMap = 120;
	public static final String[] ruleNames = {
		"mappingUnit", "keyMap", "keyUses", "keyUsesName", "keyUsesNameSource", 
		"keyUsesNameTarget", "keyUsesNameQueried", "keyUsesNameProduced", "keyImports", 
		"group", "groupStart", "groupExtends", "groupEnd", "groupType", "groupTypeValue", 
		"groupTypeType", "groupTypeTypeTypes", "ruleInput", "ruleInputName", "ruleInputType", 
		"ruleInputMode", "ruleInputModes", "ruleInputModesSource", "ruleInputModesTarget", 
		"ruleInstance", "ruleName", "ruleSources", "ruleSource", "ruleType", "ruleDefault", 
		"ruleListOption", "ruleVariable", "ruleContext", "ruleContextElement", 
		"ruleWherePath", "ruleCheckPath", "ruleMake", "ruleTargets", "ruleDependents", 
		"ruleTarget", "ruleTargetContext", "ruleTargetAs", "ruleTargetAssign", 
		"ruleTargetAssignValue", "ruleTargetAppend", "ruleTargetAppendSources", 
		"ruleTargetAppendSource", "ruleTargetC", "ruleTargetCSystem", "ruleTargetCCode", 
		"ruleTargetCDisplay", "ruleTargetCast", "ruleTargetCastSource", "ruleTargetCastType", 
		"ruleTargetCC", "ruleTargetCC1", "ruleTargetCC1Text", "ruleTargetCC2", 
		"ruleTargetCC2System", "ruleTargetCC2Code", "ruleTargetCC2Display", "ruleTargetCp", 
		"ruleTargetCpSystem", "ruleTargetCpVariable", "ruleTargetCopy", "ruleTargetCopySource", 
		"ruleTargetCreate", "ruleTargetCreateType", "ruleTargetDateOp", "ruleTargetDateOpVariable", 
		"ruleTargetDateOpOperation", "ruleTargetDateOpVariable2", "ruleTargetEscape", 
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
		null, "'='", "';'", "':'", "'.'", "','", "'..'", "'xxxxyyyyyzzzzzz'", 
		"'{'", "'}'", "'('", "')'", null, null, "'source'", "'target'", "'queried'", 
		"'produced'", null, null, "'append'", "'as'", "'cast'", "'c'", "'cc'", 
		"'code'", "'cp'", "'check'", "'codeableConcept'", "'coding'", "'copy'", 
		"'create'", "'dateOp'", "'default'", "'display'", "'endgroup'", "'escape'", 
		"'evaluate'", "'extends'", "'for'", "'group'", "'id'", "'imports'", "'input'", 
		"'make'", "'map'", "'pointer'", "'qty'", "'reference'", "'system'", "'then'", 
		"'translate'", "'truncate'", "'types'", "'type+types'", "'uuid'", "'uses'", 
		"'where'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, null, null, null, null, null, null, null, null, null, null, null, 
		"HEX", "DIGITS", "SOURCE", "TARGET", "QUERIED", "PRODUCED", "QIDENTIFIER", 
		"QSTRING", "APPEND", "AS", "CAST", "C", "CC", "CODE", "CP", "CHECK", "CODEABLECONCEPT", 
		"CODING", "COPY", "CREATE", "DATEOP", "DEFAULT", "DISPLAY", "ENDGROUP", 
		"ESCAPE", "EVALUATE", "EXTENDS", "FOR", "GROUP", "ID", "IMPORTS", "INPUT", 
		"MAKE", "MAP", "POINTER", "QTY", "REFERENCE", "SYSTEM", "THEN", "TRANSLATE", 
		"TRUNCATE", "TYPES", "TYPE_TYPES", "UUID", "USES", "WHERE", "IDENTIFIER", 
		"WS", "LINE_COMMENT"
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
			setState(242);
			keyMap();
			setState(244); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(243);
				keyUses();
				}
				}
				setState(246); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==USES );
			setState(251);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==IMPORTS) {
				{
				{
				setState(248);
				keyImports();
				}
				}
				setState(253);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(254);
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
			if ( visitor instanceof FhirMapJavaVisitor ) try {
				return ((FhirMapJavaVisitor<? extends T>)visitor).visitKeyMap(this);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return visitor.visitChildren(this);
		}
	}

	public final KeyMapContext keyMap() throws RecognitionException {
		KeyMapContext _localctx = new KeyMapContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_keyMap);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(256);
			match(MAP);
			setState(257);
			structureMap();
			setState(258);
			match(T__0);
			setState(259);
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
			if ( visitor instanceof FhirMapJavaVisitor ) try {
				return ((FhirMapJavaVisitor<? extends T>)visitor).visitKeyUses(this);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return visitor.visitChildren(this);
		}
	}

	public final KeyUsesContext keyUses() throws RecognitionException {
		KeyUsesContext _localctx = new KeyUsesContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_keyUses);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(261);
			match(USES);
			setState(262);
			structureDefinition();
			setState(263);
			match(AS);
			setState(264);
			keyUsesName();
			setState(266);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__1) {
				{
				setState(265);
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
			if ( visitor instanceof FhirMapJavaVisitor ) try {
				return ((FhirMapJavaVisitor<? extends T>)visitor).visitKeyUsesName(this);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return visitor.visitChildren(this);
		}
	}

	public final KeyUsesNameContext keyUsesName() throws RecognitionException {
		KeyUsesNameContext _localctx = new KeyUsesNameContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_keyUsesName);
		try {
			setState(272);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case SOURCE:
				enterOuterAlt(_localctx, 1);
				{
				setState(268);
				keyUsesNameSource();
				}
				break;
			case TARGET:
				enterOuterAlt(_localctx, 2);
				{
				setState(269);
				keyUsesNameTarget();
				}
				break;
			case QUERIED:
				enterOuterAlt(_localctx, 3);
				{
				setState(270);
				keyUsesNameQueried();
				}
				break;
			case PRODUCED:
				enterOuterAlt(_localctx, 4);
				{
				setState(271);
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
			setState(274);
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
			setState(276);
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
			setState(278);
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
			setState(280);
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
			if ( visitor instanceof FhirMapJavaVisitor ) try {
				return ((FhirMapJavaVisitor<? extends T>)visitor).visitKeyImports(this);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return visitor.visitChildren(this);
		}
	}

	public final KeyImportsContext keyImports() throws RecognitionException {
		KeyImportsContext _localctx = new KeyImportsContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_keyImports);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(282);
			match(IMPORTS);
			setState(283);
			structureMap();
			setState(285);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__1) {
				{
				setState(284);
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
		public List<RuleInputContext> ruleInput() {
			return getRuleContexts(RuleInputContext.class);
		}
		public RuleInputContext ruleInput(int i) {
			return getRuleContext(RuleInputContext.class,i);
		}
		public List<RuleInstanceContext> ruleInstance() {
			return getRuleContexts(RuleInstanceContext.class);
		}
		public RuleInstanceContext ruleInstance(int i) {
			return getRuleContext(RuleInstanceContext.class,i);
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
			setState(287);
			groupStart();
			setState(291);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==INPUT) {
				{
				{
				setState(288);
				ruleInput();
				}
				}
				setState(293);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(297);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << SOURCE) | (1L << TARGET) | (1L << QUERIED) | (1L << PRODUCED) | (1L << APPEND) | (1L << CAST) | (1L << C) | (1L << CC) | (1L << CODE) | (1L << CP) | (1L << CODEABLECONCEPT) | (1L << CODING) | (1L << COPY) | (1L << CREATE) | (1L << DATEOP) | (1L << DEFAULT) | (1L << DISPLAY) | (1L << ESCAPE) | (1L << EVALUATE) | (1L << ID) | (1L << MAP) | (1L << POINTER) | (1L << QTY) | (1L << REFERENCE) | (1L << SYSTEM) | (1L << TRANSLATE) | (1L << TRUNCATE) | (1L << TYPES) | (1L << UUID) | (1L << IDENTIFIER))) != 0)) {
				{
				{
				setState(294);
				ruleInstance();
				}
				}
				setState(299);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(300);
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
			if ( visitor instanceof FhirMapJavaVisitor ) try {
				return ((FhirMapJavaVisitor<? extends T>)visitor).visitGroupStart(this);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return visitor.visitChildren(this);
		}
	}

	public final GroupStartContext groupStart() throws RecognitionException {
		GroupStartContext _localctx = new GroupStartContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_groupStart);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(302);
			match(GROUP);
			setState(304);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==FOR) {
				{
				setState(303);
				groupType();
				}
			}

			setState(306);
			identifier();
			setState(308);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==EXTENDS) {
				{
				setState(307);
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
			if ( visitor instanceof FhirMapJavaVisitor ) try {
				return ((FhirMapJavaVisitor<? extends T>)visitor).visitGroupExtends(this);
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			return visitor.visitChildren(this);
		}
	}

	public final GroupExtendsContext groupExtends() throws RecognitionException {
		GroupExtendsContext _localctx = new GroupExtendsContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_groupExtends);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(310);
			match(EXTENDS);
			setState(311);
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
			if ( visitor instanceof FhirMapJavaVisitor ) try {
				return ((FhirMapJavaVisitor<? extends T>)visitor).visitGroupEnd(this);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return visitor.visitChildren(this);
		}
	}

	public final GroupEndContext groupEnd() throws RecognitionException {
		GroupEndContext _localctx = new GroupEndContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_groupEnd);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(313);
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
			setState(315);
			match(FOR);
			setState(316);
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
			setState(320);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case TYPES:
				enterOuterAlt(_localctx, 1);
				{
				setState(318);
				groupTypeType();
				}
				break;
			case TYPE_TYPES:
				enterOuterAlt(_localctx, 2);
				{
				setState(319);
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
			setState(322);
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
			setState(324);
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

	public static class RuleInputContext extends ParserRuleContext {
		public TerminalNode INPUT() { return getToken(FhirMapJavaParser.INPUT, 0); }
		public RuleInputNameContext ruleInputName() {
			return getRuleContext(RuleInputNameContext.class,0);
		}
		public RuleInputTypeContext ruleInputType() {
			return getRuleContext(RuleInputTypeContext.class,0);
		}
		public RuleInputModeContext ruleInputMode() {
			return getRuleContext(RuleInputModeContext.class,0);
		}
		public RuleInputContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ruleInput; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FhirMapJavaVisitor ) try {
				return ((FhirMapJavaVisitor<? extends T>)visitor).visitRuleInput(this);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return visitor.visitChildren(this);
		}
	}

	public final RuleInputContext ruleInput() throws RecognitionException {
		RuleInputContext _localctx = new RuleInputContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_ruleInput);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(326);
			match(INPUT);
			setState(327);
			ruleInputName();
			setState(328);
			match(T__2);
			setState(329);
			ruleInputType();
			setState(331);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==AS) {
				{
				setState(330);
				ruleInputMode();
				}
			}

			setState(334);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__1) {
				{
				setState(333);
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

	public static class RuleInputNameContext extends ParserRuleContext {
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public RuleInputNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ruleInputName; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitRuleInputName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RuleInputNameContext ruleInputName() throws RecognitionException {
		RuleInputNameContext _localctx = new RuleInputNameContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_ruleInputName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(336);
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

	public static class RuleInputTypeContext extends ParserRuleContext {
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public RuleInputTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ruleInputType; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitRuleInputType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RuleInputTypeContext ruleInputType() throws RecognitionException {
		RuleInputTypeContext _localctx = new RuleInputTypeContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_ruleInputType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(338);
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

	public static class RuleInputModeContext extends ParserRuleContext {
		public TerminalNode AS() { return getToken(FhirMapJavaParser.AS, 0); }
		public RuleInputModesContext ruleInputModes() {
			return getRuleContext(RuleInputModesContext.class,0);
		}
		public RuleInputModeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ruleInputMode; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FhirMapJavaVisitor ) try {
				return ((FhirMapJavaVisitor<? extends T>)visitor).visitRuleInputMode(this);
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			return visitor.visitChildren(this);
		}
	}

	public final RuleInputModeContext ruleInputMode() throws RecognitionException {
		RuleInputModeContext _localctx = new RuleInputModeContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_ruleInputMode);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(340);
			match(AS);
			setState(341);
			ruleInputModes();
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

	public static class RuleInputModesContext extends ParserRuleContext {
		public RuleInputModesSourceContext ruleInputModesSource() {
			return getRuleContext(RuleInputModesSourceContext.class,0);
		}
		public RuleInputModesTargetContext ruleInputModesTarget() {
			return getRuleContext(RuleInputModesTargetContext.class,0);
		}
		public RuleInputModesContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ruleInputModes; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitRuleInputModes(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RuleInputModesContext ruleInputModes() throws RecognitionException {
		RuleInputModesContext _localctx = new RuleInputModesContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_ruleInputModes);
		try {
			setState(345);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case SOURCE:
				enterOuterAlt(_localctx, 1);
				{
				setState(343);
				ruleInputModesSource();
				}
				break;
			case TARGET:
				enterOuterAlt(_localctx, 2);
				{
				setState(344);
				ruleInputModesTarget();
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

	public static class RuleInputModesSourceContext extends ParserRuleContext {
		public TerminalNode SOURCE() { return getToken(FhirMapJavaParser.SOURCE, 0); }
		public RuleInputModesSourceContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ruleInputModesSource; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitRuleInputModesSource(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RuleInputModesSourceContext ruleInputModesSource() throws RecognitionException {
		RuleInputModesSourceContext _localctx = new RuleInputModesSourceContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_ruleInputModesSource);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(347);
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

	public static class RuleInputModesTargetContext extends ParserRuleContext {
		public TerminalNode TARGET() { return getToken(FhirMapJavaParser.TARGET, 0); }
		public RuleInputModesTargetContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ruleInputModesTarget; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitRuleInputModesTarget(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RuleInputModesTargetContext ruleInputModesTarget() throws RecognitionException {
		RuleInputModesTargetContext _localctx = new RuleInputModesTargetContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_ruleInputModesTarget);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(349);
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
			if ( visitor instanceof FhirMapJavaVisitor ) try {
				return ((FhirMapJavaVisitor<? extends T>)visitor).visitRuleInstance(this);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return visitor.visitChildren(this);
		}
	}

	public final RuleInstanceContext ruleInstance() throws RecognitionException {
		RuleInstanceContext _localctx = new RuleInstanceContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_ruleInstance);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(351);
			ruleName();
			setState(352);
			match(T__2);
			setState(353);
			match(FOR);
			setState(354);
			ruleSources();
			setState(356);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==MAKE) {
				{
				setState(355);
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
		enterRule(_localctx, 50, RULE_ruleName);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(358);
			identifier();
			setState(363);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__3) {
				{
				{
				setState(359);
				match(T__3);
				setState(360);
				identifier();
				}
				}
				setState(365);
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
		enterRule(_localctx, 52, RULE_ruleSources);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(366);
			ruleSource();
			setState(371);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__4) {
				{
				{
				setState(367);
				match(T__4);
				setState(368);
				ruleSource();
				}
				}
				setState(373);
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
			if ( visitor instanceof FhirMapJavaVisitor ) try {
				return ((FhirMapJavaVisitor<? extends T>)visitor).visitRuleSource(this);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return visitor.visitChildren(this);
		}
	}

	public final RuleSourceContext ruleSource() throws RecognitionException {
		RuleSourceContext _localctx = new RuleSourceContext(_ctx, getState());
		enterRule(_localctx, 54, RULE_ruleSource);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(374);
			ruleContext();
			setState(376);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__2) {
				{
				setState(375);
				ruleType();
				}
			}

			setState(379);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,17,_ctx) ) {
			case 1:
				{
				setState(378);
				ruleDefault();
				}
				break;
			}
			setState(382);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__6) {
				{
				setState(381);
				ruleListOption();
				}
			}

			setState(385);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==AS) {
				{
				setState(384);
				ruleVariable();
				}
			}

			setState(388);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==WHERE) {
				{
				setState(387);
				ruleWherePath();
				}
			}

			setState(391);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==CHECK) {
				{
				setState(390);
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
			if ( visitor instanceof FhirMapJavaVisitor ) try {
				return ((FhirMapJavaVisitor<? extends T>)visitor).visitRuleType(this);
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			return visitor.visitChildren(this);
		}
	}

	public final RuleTypeContext ruleType() throws RecognitionException {
		RuleTypeContext _localctx = new RuleTypeContext(_ctx, getState());
		enterRule(_localctx, 56, RULE_ruleType);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(393);
			match(T__2);
			setState(394);
			identifier();
			setState(399);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==DIGITS) {
				{
				setState(395);
				integer();
				setState(396);
				match(T__5);
				setState(397);
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
			if ( visitor instanceof FhirMapJavaVisitor ) try {
				return ((FhirMapJavaVisitor<? extends T>)visitor).visitRuleDefault(this);
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			return visitor.visitChildren(this);
		}
	}

	public final RuleDefaultContext ruleDefault() throws RecognitionException {
		RuleDefaultContext _localctx = new RuleDefaultContext(_ctx, getState());
		enterRule(_localctx, 58, RULE_ruleDefault);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(401);
			match(DEFAULT);
			setState(402);
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
		enterRule(_localctx, 60, RULE_ruleListOption);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(404);
			match(T__6);
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
			if ( visitor instanceof FhirMapJavaVisitor ) try {
				return ((FhirMapJavaVisitor<? extends T>)visitor).visitRuleVariable(this);
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			return visitor.visitChildren(this);
		}
	}

	public final RuleVariableContext ruleVariable() throws RecognitionException {
		RuleVariableContext _localctx = new RuleVariableContext(_ctx, getState());
		enterRule(_localctx, 62, RULE_ruleVariable);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(406);
			match(AS);
			setState(407);
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
		enterRule(_localctx, 64, RULE_ruleContext);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(409);
			ruleContextElement();
			setState(414);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__3) {
				{
				{
				setState(410);
				match(T__3);
				setState(411);
				ruleContextElement();
				}
				}
				setState(416);
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
		public QuotedStringContext quotedString() {
			return getRuleContext(QuotedStringContext.class,0);
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
		enterRule(_localctx, 66, RULE_ruleContextElement);
		try {
			setState(419);
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
				setState(417);
				identifier();
				}
				break;
			case QIDENTIFIER:
			case QSTRING:
				enterOuterAlt(_localctx, 2);
				{
				setState(418);
				quotedString();
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
		enterRule(_localctx, 68, RULE_ruleWherePath);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(421);
			match(WHERE);
			setState(422);
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
		enterRule(_localctx, 70, RULE_ruleCheckPath);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(424);
			match(CHECK);
			setState(425);
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
		public RuleTargetsContext ruleTargets() {
			return getRuleContext(RuleTargetsContext.class,0);
		}
		public RuleDependentsContext ruleDependents() {
			return getRuleContext(RuleDependentsContext.class,0);
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
		enterRule(_localctx, 72, RULE_ruleMake);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(427);
			match(MAKE);
			setState(428);
			ruleTargets();
			setState(430);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==THEN) {
				{
				setState(429);
				ruleDependents();
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

	public static class RuleTargetsContext extends ParserRuleContext {
		public List<RuleTargetContext> ruleTarget() {
			return getRuleContexts(RuleTargetContext.class);
		}
		public RuleTargetContext ruleTarget(int i) {
			return getRuleContext(RuleTargetContext.class,i);
		}
		public RuleTargetsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ruleTargets; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitRuleTargets(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RuleTargetsContext ruleTargets() throws RecognitionException {
		RuleTargetsContext _localctx = new RuleTargetsContext(_ctx, getState());
		enterRule(_localctx, 74, RULE_ruleTargets);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(432);
			ruleTarget();
			setState(437);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__4) {
				{
				{
				setState(433);
				match(T__4);
				setState(434);
				ruleTarget();
				}
				}
				setState(439);
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

	public static class RuleDependentsContext extends ParserRuleContext {
		public TerminalNode THEN() { return getToken(FhirMapJavaParser.THEN, 0); }
		public List<RuleInstanceContext> ruleInstance() {
			return getRuleContexts(RuleInstanceContext.class);
		}
		public RuleInstanceContext ruleInstance(int i) {
			return getRuleContext(RuleInstanceContext.class,i);
		}
		public RuleDependentsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ruleDependents; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FhirMapJavaVisitor ) return ((FhirMapJavaVisitor<? extends T>)visitor).visitRuleDependents(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RuleDependentsContext ruleDependents() throws RecognitionException {
		RuleDependentsContext _localctx = new RuleDependentsContext(_ctx, getState());
		enterRule(_localctx, 76, RULE_ruleDependents);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(440);
			match(THEN);
			setState(441);
			match(T__7);
			setState(445);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << SOURCE) | (1L << TARGET) | (1L << QUERIED) | (1L << PRODUCED) | (1L << APPEND) | (1L << CAST) | (1L << C) | (1L << CC) | (1L << CODE) | (1L << CP) | (1L << CODEABLECONCEPT) | (1L << CODING) | (1L << COPY) | (1L << CREATE) | (1L << DATEOP) | (1L << DEFAULT) | (1L << DISPLAY) | (1L << ESCAPE) | (1L << EVALUATE) | (1L << ID) | (1L << MAP) | (1L << POINTER) | (1L << QTY) | (1L << REFERENCE) | (1L << SYSTEM) | (1L << TRANSLATE) | (1L << TRUNCATE) | (1L << TYPES) | (1L << UUID) | (1L << IDENTIFIER))) != 0)) {
				{
				{
				setState(442);
				ruleInstance();
				}
				}
				setState(447);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(448);
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
		enterRule(_localctx, 78, RULE_ruleTarget);
		try {
			setState(469);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,28,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(450);
				ruleTargetAppend();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(451);
				ruleTargetAs();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(452);
				ruleTargetAssign();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(453);
				ruleTargetC();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(454);
				ruleTargetCast();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(455);
				ruleTargetCC();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(456);
				ruleTargetCp();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(457);
				ruleTargetCopy();
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(458);
				ruleTargetCreate();
				}
				break;
			case 10:
				enterOuterAlt(_localctx, 10);
				{
				setState(459);
				ruleTargetDateOp();
				}
				break;
			case 11:
				enterOuterAlt(_localctx, 11);
				{
				setState(460);
				ruleTargetEscape();
				}
				break;
			case 12:
				enterOuterAlt(_localctx, 12);
				{
				setState(461);
				ruleTargetEvaluate();
				}
				break;
			case 13:
				enterOuterAlt(_localctx, 13);
				{
				setState(462);
				ruleTargetId();
				}
				break;
			case 14:
				enterOuterAlt(_localctx, 14);
				{
				setState(463);
				ruleTargetPointer();
				}
				break;
			case 15:
				enterOuterAlt(_localctx, 15);
				{
				setState(464);
				ruleTargetQty();
				}
				break;
			case 16:
				enterOuterAlt(_localctx, 16);
				{
				setState(465);
				ruleTargetReference();
				}
				break;
			case 17:
				enterOuterAlt(_localctx, 17);
				{
				setState(466);
				ruleTargetTranslate();
				}
				break;
			case 18:
				enterOuterAlt(_localctx, 18);
				{
				setState(467);
				ruleTargetTruncate();
				}
				break;
			case 19:
				enterOuterAlt(_localctx, 19);
				{
				setState(468);
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
			if ( visitor instanceof FhirMapJavaVisitor ) try {
				return ((FhirMapJavaVisitor<? extends T>)visitor).visitRuleTargetContext(this);
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			return visitor.visitChildren(this);
		}
	}

	public final RuleTargetContextContext ruleTargetContext() throws RecognitionException {
		RuleTargetContextContext _localctx = new RuleTargetContextContext(_ctx, getState());
		enterRule(_localctx, 80, RULE_ruleTargetContext);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(471);
			ruleContext();
			setState(472);
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
			if ( visitor instanceof FhirMapJavaVisitor ) try {
				return ((FhirMapJavaVisitor<? extends T>)visitor).visitRuleTargetAs(this);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return visitor.visitChildren(this);
		}
	}

	public final RuleTargetAsContext ruleTargetAs() throws RecognitionException {
		RuleTargetAsContext _localctx = new RuleTargetAsContext(_ctx, getState());
		enterRule(_localctx, 82, RULE_ruleTargetAs);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(474);
			ruleContext();
			setState(475);
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
			if ( visitor instanceof FhirMapJavaVisitor ) try {
				return ((FhirMapJavaVisitor<? extends T>)visitor).visitRuleTargetAssign(this);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return visitor.visitChildren(this);
		}
	}

	public final RuleTargetAssignContext ruleTargetAssign() throws RecognitionException {
		RuleTargetAssignContext _localctx = new RuleTargetAssignContext(_ctx, getState());
		enterRule(_localctx, 84, RULE_ruleTargetAssign);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(477);
			ruleTargetContext();
			setState(478);
			ruleTargetAssignValue();
			setState(480);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==AS) {
				{
				setState(479);
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
		enterRule(_localctx, 86, RULE_ruleTargetAssignValue);
		try {
			setState(484);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case QIDENTIFIER:
			case QSTRING:
				enterOuterAlt(_localctx, 1);
				{
				setState(482);
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
				setState(483);
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
			if ( visitor instanceof FhirMapJavaVisitor ) try {
				return ((FhirMapJavaVisitor<? extends T>)visitor).visitRuleTargetAppend(this);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return visitor.visitChildren(this);
		}
	}

	public final RuleTargetAppendContext ruleTargetAppend() throws RecognitionException {
		RuleTargetAppendContext _localctx = new RuleTargetAppendContext(_ctx, getState());
		enterRule(_localctx, 88, RULE_ruleTargetAppend);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(487);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,31,_ctx) ) {
			case 1:
				{
				setState(486);
				ruleTargetContext();
				}
				break;
			}
			setState(489);
			match(APPEND);
			setState(490);
			match(T__9);
			setState(491);
			ruleTargetAppendSources();
			setState(492);
			match(T__10);
			setState(494);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==AS) {
				{
				setState(493);
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
		enterRule(_localctx, 90, RULE_ruleTargetAppendSources);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(496);
			ruleTargetAppendSource();
			setState(501);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__4) {
				{
				{
				setState(497);
				match(T__4);
				setState(498);
				ruleTargetAppendSource();
				}
				}
				setState(503);
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
		enterRule(_localctx, 92, RULE_ruleTargetAppendSource);
		try {
			setState(506);
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
				setState(504);
				identifier();
				}
				break;
			case QIDENTIFIER:
			case QSTRING:
				enterOuterAlt(_localctx, 2);
				{
				setState(505);
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
			if ( visitor instanceof FhirMapJavaVisitor ) try {
				return ((FhirMapJavaVisitor<? extends T>)visitor).visitRuleTargetC(this);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return visitor.visitChildren(this);
		}
	}

	public final RuleTargetCContext ruleTargetC() throws RecognitionException {
		RuleTargetCContext _localctx = new RuleTargetCContext(_ctx, getState());
		enterRule(_localctx, 94, RULE_ruleTargetC);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(509);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,35,_ctx) ) {
			case 1:
				{
				setState(508);
				ruleTargetContext();
				}
				break;
			}
			setState(511);
			match(C);
			setState(512);
			match(T__9);
			setState(513);
			ruleTargetCSystem();
			setState(514);
			match(T__4);
			setState(515);
			ruleTargetCCode();
			setState(518);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__4) {
				{
				setState(516);
				match(T__4);
				setState(517);
				ruleTargetCDisplay();
				}
			}

			setState(520);
			match(T__10);
			setState(522);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==AS) {
				{
				setState(521);
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
		enterRule(_localctx, 96, RULE_ruleTargetCSystem);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(524);
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
		enterRule(_localctx, 98, RULE_ruleTargetCCode);
		try {
			setState(528);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case QIDENTIFIER:
			case QSTRING:
				enterOuterAlt(_localctx, 1);
				{
				setState(526);
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
				setState(527);
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
		enterRule(_localctx, 100, RULE_ruleTargetCDisplay);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(530);
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
			if ( visitor instanceof FhirMapJavaVisitor ) try {
				return ((FhirMapJavaVisitor<? extends T>)visitor).visitRuleTargetCast(this);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return visitor.visitChildren(this);
		}
	}

	public final RuleTargetCastContext ruleTargetCast() throws RecognitionException {
		RuleTargetCastContext _localctx = new RuleTargetCastContext(_ctx, getState());
		enterRule(_localctx, 102, RULE_ruleTargetCast);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(533);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,39,_ctx) ) {
			case 1:
				{
				setState(532);
				ruleTargetContext();
				}
				break;
			}
			setState(535);
			match(CAST);
			setState(536);
			match(T__9);
			setState(537);
			ruleTargetCastSource();
			setState(540);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__4) {
				{
				setState(538);
				match(T__4);
				setState(539);
				ruleTargetCastType();
				}
			}

			setState(542);
			match(T__10);
			setState(544);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==AS) {
				{
				setState(543);
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
		enterRule(_localctx, 104, RULE_ruleTargetCastSource);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(546);
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
		enterRule(_localctx, 106, RULE_ruleTargetCastType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(548);
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
		enterRule(_localctx, 108, RULE_ruleTargetCC);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(552);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,42,_ctx) ) {
			case 1:
				{
				setState(550);
				ruleTargetCC1();
				}
				break;
			case 2:
				{
				setState(551);
				ruleTargetCC2();
				}
				break;
			}
			setState(555);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==AS) {
				{
				setState(554);
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
			if ( visitor instanceof FhirMapJavaVisitor ) try {
				return ((FhirMapJavaVisitor<? extends T>)visitor).visitRuleTargetCC1(this);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return visitor.visitChildren(this);
		}
	}

	public final RuleTargetCC1Context ruleTargetCC1() throws RecognitionException {
		RuleTargetCC1Context _localctx = new RuleTargetCC1Context(_ctx, getState());
		enterRule(_localctx, 110, RULE_ruleTargetCC1);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(558);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,44,_ctx) ) {
			case 1:
				{
				setState(557);
				ruleTargetContext();
				}
				break;
			}
			setState(560);
			match(CC);
			setState(561);
			match(T__9);
			setState(562);
			ruleTargetCC1Text();
			setState(563);
			match(T__10);
			setState(565);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,45,_ctx) ) {
			case 1:
				{
				setState(564);
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
		enterRule(_localctx, 112, RULE_ruleTargetCC1Text);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(567);
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
			if ( visitor instanceof FhirMapJavaVisitor ) try {
				return ((FhirMapJavaVisitor<? extends T>)visitor).visitRuleTargetCC2(this);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return visitor.visitChildren(this);
		}
	}

	public final RuleTargetCC2Context ruleTargetCC2() throws RecognitionException {
		RuleTargetCC2Context _localctx = new RuleTargetCC2Context(_ctx, getState());
		enterRule(_localctx, 114, RULE_ruleTargetCC2);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(570);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,46,_ctx) ) {
			case 1:
				{
				setState(569);
				ruleTargetContext();
				}
				break;
			}
			setState(572);
			match(CC);
			setState(573);
			match(T__9);
			setState(574);
			ruleTargetCC2System();
			setState(575);
			match(T__4);
			setState(576);
			ruleTargetCC2Code();
			setState(579);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__4) {
				{
				setState(577);
				match(T__4);
				setState(578);
				ruleTargetCC2Display();
				}
			}

			setState(581);
			match(T__10);
			setState(583);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,48,_ctx) ) {
			case 1:
				{
				setState(582);
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
		enterRule(_localctx, 116, RULE_ruleTargetCC2System);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(585);
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
		enterRule(_localctx, 118, RULE_ruleTargetCC2Code);
		try {
			setState(589);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case QIDENTIFIER:
			case QSTRING:
				enterOuterAlt(_localctx, 1);
				{
				setState(587);
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
				setState(588);
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
		enterRule(_localctx, 120, RULE_ruleTargetCC2Display);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(591);
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
			if ( visitor instanceof FhirMapJavaVisitor ) try {
				return ((FhirMapJavaVisitor<? extends T>)visitor).visitRuleTargetCp(this);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return visitor.visitChildren(this);
		}
	}

	public final RuleTargetCpContext ruleTargetCp() throws RecognitionException {
		RuleTargetCpContext _localctx = new RuleTargetCpContext(_ctx, getState());
		enterRule(_localctx, 122, RULE_ruleTargetCp);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(594);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,50,_ctx) ) {
			case 1:
				{
				setState(593);
				ruleTargetContext();
				}
				break;
			}
			setState(596);
			match(CP);
			setState(597);
			match(T__9);
			setState(601);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==QIDENTIFIER || _la==QSTRING) {
				{
				setState(598);
				ruleTargetCpSystem();
				setState(599);
				match(T__4);
				}
			}

			setState(603);
			ruleTargetCpVariable();
			setState(604);
			match(T__10);
			setState(606);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==AS) {
				{
				setState(605);
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
		enterRule(_localctx, 124, RULE_ruleTargetCpSystem);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(608);
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
		enterRule(_localctx, 126, RULE_ruleTargetCpVariable);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(610);
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
			if ( visitor instanceof FhirMapJavaVisitor ) try {
				return ((FhirMapJavaVisitor<? extends T>)visitor).visitRuleTargetCopy(this);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return visitor.visitChildren(this);
		}
	}

	public final RuleTargetCopyContext ruleTargetCopy() throws RecognitionException {
		RuleTargetCopyContext _localctx = new RuleTargetCopyContext(_ctx, getState());
		enterRule(_localctx, 128, RULE_ruleTargetCopy);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(612);
			ruleTargetContext();
			setState(613);
			match(COPY);
			setState(614);
			match(T__9);
			setState(615);
			ruleTargetCopySource();
			setState(616);
			match(T__10);
			setState(618);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==AS) {
				{
				setState(617);
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
		enterRule(_localctx, 130, RULE_ruleTargetCopySource);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(620);
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
			if ( visitor instanceof FhirMapJavaVisitor ) try {
				return ((FhirMapJavaVisitor<? extends T>)visitor).visitRuleTargetCreate(this);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return visitor.visitChildren(this);
		}
	}

	public final RuleTargetCreateContext ruleTargetCreate() throws RecognitionException {
		RuleTargetCreateContext _localctx = new RuleTargetCreateContext(_ctx, getState());
		enterRule(_localctx, 132, RULE_ruleTargetCreate);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(623);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,54,_ctx) ) {
			case 1:
				{
				setState(622);
				ruleTargetContext();
				}
				break;
			}
			setState(625);
			match(CREATE);
			setState(626);
			match(T__9);
			setState(627);
			ruleTargetCreateType();
			setState(628);
			match(T__10);
			setState(630);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==AS) {
				{
				setState(629);
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
		enterRule(_localctx, 134, RULE_ruleTargetCreateType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(632);
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
			if ( visitor instanceof FhirMapJavaVisitor ) try {
				return ((FhirMapJavaVisitor<? extends T>)visitor).visitRuleTargetDateOp(this);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return visitor.visitChildren(this);
		}
	}

	public final RuleTargetDateOpContext ruleTargetDateOp() throws RecognitionException {
		RuleTargetDateOpContext _localctx = new RuleTargetDateOpContext(_ctx, getState());
		enterRule(_localctx, 136, RULE_ruleTargetDateOp);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(635);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,56,_ctx) ) {
			case 1:
				{
				setState(634);
				ruleTargetContext();
				}
				break;
			}
			setState(637);
			match(DATEOP);
			setState(638);
			match(T__9);
			setState(639);
			ruleTargetDateOpVariable();
			setState(640);
			match(T__4);
			setState(641);
			ruleTargetDateOpOperation();
			setState(644);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__4) {
				{
				setState(642);
				match(T__4);
				setState(643);
				ruleTargetDateOpVariable2();
				}
			}

			setState(646);
			match(T__10);
			setState(648);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==AS) {
				{
				setState(647);
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
		enterRule(_localctx, 138, RULE_ruleTargetDateOpVariable);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(650);
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
		enterRule(_localctx, 140, RULE_ruleTargetDateOpOperation);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(652);
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
		enterRule(_localctx, 142, RULE_ruleTargetDateOpVariable2);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(654);
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
		enterRule(_localctx, 144, RULE_ruleTargetEscape);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(657);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,59,_ctx) ) {
			case 1:
				{
				setState(656);
				ruleTargetContext();
				}
				break;
			}
			setState(659);
			match(ESCAPE);
			setState(660);
			match(T__9);
			setState(661);
			ruleTargetEscapeVariable();
			setState(662);
			match(T__4);
			setState(663);
			ruleTargetEscapeString1();
			setState(666);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__4) {
				{
				setState(664);
				match(T__4);
				setState(665);
				ruleTargetEscapeString2();
				}
			}

			setState(668);
			match(T__10);
			setState(670);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==AS) {
				{
				setState(669);
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
		enterRule(_localctx, 146, RULE_ruleTargetEscapeVariable);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(672);
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
		enterRule(_localctx, 148, RULE_ruleTargetEscapeString1);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(674);
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
		enterRule(_localctx, 150, RULE_ruleTargetEscapeString2);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(676);
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
			if ( visitor instanceof FhirMapJavaVisitor ) try {
				return ((FhirMapJavaVisitor<? extends T>)visitor).visitRuleTargetEvaluate(this);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return visitor.visitChildren(this);
		}
	}

	public final RuleTargetEvaluateContext ruleTargetEvaluate() throws RecognitionException {
		RuleTargetEvaluateContext _localctx = new RuleTargetEvaluateContext(_ctx, getState());
		enterRule(_localctx, 152, RULE_ruleTargetEvaluate);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(679);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,62,_ctx) ) {
			case 1:
				{
				setState(678);
				ruleTargetContext();
				}
				break;
			}
			setState(681);
			match(EVALUATE);
			setState(682);
			match(T__9);
			setState(683);
			ruleTargetEvaluateObject();
			setState(684);
			match(T__4);
			setState(685);
			ruleTargetEvaluateObjectElement();
			setState(686);
			match(T__10);
			setState(688);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==AS) {
				{
				setState(687);
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
		enterRule(_localctx, 154, RULE_ruleTargetEvaluateObject);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(690);
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
		enterRule(_localctx, 156, RULE_ruleTargetEvaluateObjectElement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(692);
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
			if ( visitor instanceof FhirMapJavaVisitor ) try {
				return ((FhirMapJavaVisitor<? extends T>)visitor).visitRuleTargetId(this);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return visitor.visitChildren(this);
		}
	}

	public final RuleTargetIdContext ruleTargetId() throws RecognitionException {
		RuleTargetIdContext _localctx = new RuleTargetIdContext(_ctx, getState());
		enterRule(_localctx, 158, RULE_ruleTargetId);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(695);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,64,_ctx) ) {
			case 1:
				{
				setState(694);
				ruleTargetContext();
				}
				break;
			}
			setState(697);
			match(ID);
			setState(698);
			match(T__9);
			setState(699);
			ruleTargetIdSystem();
			setState(700);
			match(T__4);
			setState(701);
			ruleTargetIdValue();
			setState(704);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__4) {
				{
				setState(702);
				match(T__4);
				setState(703);
				ruleTargetIdType();
				}
			}

			setState(706);
			match(T__10);
			setState(708);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==AS) {
				{
				setState(707);
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
		enterRule(_localctx, 160, RULE_ruleTargetIdSystem);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(710);
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
		enterRule(_localctx, 162, RULE_ruleTargetIdValue);
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
		enterRule(_localctx, 164, RULE_ruleTargetIdType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(714);
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
			if ( visitor instanceof FhirMapJavaVisitor ) try {
				return ((FhirMapJavaVisitor<? extends T>)visitor).visitRuleTargetPointer(this);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return visitor.visitChildren(this);
		}
	}

	public final RuleTargetPointerContext ruleTargetPointer() throws RecognitionException {
		RuleTargetPointerContext _localctx = new RuleTargetPointerContext(_ctx, getState());
		enterRule(_localctx, 166, RULE_ruleTargetPointer);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(717);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,67,_ctx) ) {
			case 1:
				{
				setState(716);
				ruleTargetContext();
				}
				break;
			}
			setState(719);
			match(POINTER);
			setState(720);
			match(T__9);
			setState(721);
			ruleTargetPointerResource();
			setState(722);
			match(T__10);
			setState(724);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==AS) {
				{
				setState(723);
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
		enterRule(_localctx, 168, RULE_ruleTargetPointerResource);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(726);
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
		enterRule(_localctx, 170, RULE_ruleTargetQty);
		try {
			setState(731);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,69,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(728);
				ruleTargetQty1();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(729);
				ruleTargetQty2();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(730);
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
			if ( visitor instanceof FhirMapJavaVisitor ) try {
				return ((FhirMapJavaVisitor<? extends T>)visitor).visitRuleTargetQty1(this);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return visitor.visitChildren(this);
		}
	}

	public final RuleTargetQty1Context ruleTargetQty1() throws RecognitionException {
		RuleTargetQty1Context _localctx = new RuleTargetQty1Context(_ctx, getState());
		enterRule(_localctx, 172, RULE_ruleTargetQty1);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(734);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,70,_ctx) ) {
			case 1:
				{
				setState(733);
				ruleTargetContext();
				}
				break;
			}
			setState(736);
			match(QTY);
			setState(737);
			match(T__9);
			setState(738);
			ruleTargetQty1Text();
			setState(739);
			match(T__10);
			setState(741);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==AS) {
				{
				setState(740);
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
		enterRule(_localctx, 174, RULE_ruleTargetQty1Text);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(743);
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
			if ( visitor instanceof FhirMapJavaVisitor ) try {
				return ((FhirMapJavaVisitor<? extends T>)visitor).visitRuleTargetQty2(this);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return visitor.visitChildren(this);
		}
	}

	public final RuleTargetQty2Context ruleTargetQty2() throws RecognitionException {
		RuleTargetQty2Context _localctx = new RuleTargetQty2Context(_ctx, getState());
		enterRule(_localctx, 176, RULE_ruleTargetQty2);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(746);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,72,_ctx) ) {
			case 1:
				{
				setState(745);
				ruleTargetContext();
				}
				break;
			}
			setState(748);
			match(QTY);
			setState(749);
			match(T__9);
			setState(750);
			ruleTargetQty2Value();
			setState(751);
			match(T__4);
			setState(752);
			ruleTargetQty2UnitString();
			setState(753);
			match(T__4);
			setState(754);
			ruleTargetQty2System();
			setState(755);
			match(T__10);
			setState(757);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==AS) {
				{
				setState(756);
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
		enterRule(_localctx, 178, RULE_ruleTargetQty2Value);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(759);
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
		enterRule(_localctx, 180, RULE_ruleTargetQty2UnitString);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(761);
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
		enterRule(_localctx, 182, RULE_ruleTargetQty2System);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(763);
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
			if ( visitor instanceof FhirMapJavaVisitor ) try {
				return ((FhirMapJavaVisitor<? extends T>)visitor).visitRuleTargetQty3(this);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return visitor.visitChildren(this);
		}
	}

	public final RuleTargetQty3Context ruleTargetQty3() throws RecognitionException {
		RuleTargetQty3Context _localctx = new RuleTargetQty3Context(_ctx, getState());
		enterRule(_localctx, 184, RULE_ruleTargetQty3);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(766);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,74,_ctx) ) {
			case 1:
				{
				setState(765);
				ruleTargetContext();
				}
				break;
			}
			setState(768);
			match(QTY);
			setState(769);
			match(T__9);
			setState(770);
			ruleTargetQty3Value();
			setState(771);
			match(T__4);
			setState(772);
			ruleTargetQty3UnitString();
			setState(773);
			match(T__4);
			setState(774);
			ruleTargetQty3CodeVariable();
			setState(775);
			match(T__10);
			setState(777);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==AS) {
				{
				setState(776);
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
		enterRule(_localctx, 186, RULE_ruleTargetQty3Value);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(779);
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
		enterRule(_localctx, 188, RULE_ruleTargetQty3UnitString);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(781);
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
		enterRule(_localctx, 190, RULE_ruleTargetQty3CodeVariable);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(783);
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
			if ( visitor instanceof FhirMapJavaVisitor ) try {
				return ((FhirMapJavaVisitor<? extends T>)visitor).visitRuleTargetReference(this);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return visitor.visitChildren(this);
		}
	}

	public final RuleTargetReferenceContext ruleTargetReference() throws RecognitionException {
		RuleTargetReferenceContext _localctx = new RuleTargetReferenceContext(_ctx, getState());
		enterRule(_localctx, 192, RULE_ruleTargetReference);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(786);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,76,_ctx) ) {
			case 1:
				{
				setState(785);
				ruleTargetContext();
				}
				break;
			}
			setState(788);
			match(REFERENCE);
			setState(789);
			match(T__9);
			setState(790);
			ruleTargetReferenceSource();
			setState(791);
			match(T__10);
			setState(793);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==AS) {
				{
				setState(792);
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
		enterRule(_localctx, 194, RULE_ruleTargetReferenceSource);
		try {
			setState(797);
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
				setState(795);
				identifier();
				}
				break;
			case QIDENTIFIER:
			case QSTRING:
				enterOuterAlt(_localctx, 2);
				{
				setState(796);
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
			if ( visitor instanceof FhirMapJavaVisitor ) try {
				return ((FhirMapJavaVisitor<? extends T>)visitor).visitRuleTargetTranslate(this);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return visitor.visitChildren(this);
		}
	}

	public final RuleTargetTranslateContext ruleTargetTranslate() throws RecognitionException {
		RuleTargetTranslateContext _localctx = new RuleTargetTranslateContext(_ctx, getState());
		enterRule(_localctx, 196, RULE_ruleTargetTranslate);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(800);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,79,_ctx) ) {
			case 1:
				{
				setState(799);
				ruleTargetContext();
				}
				break;
			}
			setState(802);
			match(TRANSLATE);
			setState(803);
			match(T__9);
			setState(804);
			ruleTargetTranslateSource();
			setState(805);
			match(T__4);
			setState(806);
			ruleTargetTranslateMap();
			setState(807);
			match(T__4);
			setState(808);
			ruleTargetTranslateOutput();
			setState(809);
			match(T__10);
			setState(811);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==AS) {
				{
				setState(810);
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
		enterRule(_localctx, 198, RULE_ruleTargetTranslateSource);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(813);
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
		enterRule(_localctx, 200, RULE_ruleTargetTranslateMap);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(815);
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
		enterRule(_localctx, 202, RULE_ruleTargetTranslateOutput);
		try {
			setState(822);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case CODE:
				enterOuterAlt(_localctx, 1);
				{
				setState(817);
				ruleTargetTranslateOutputCode();
				}
				break;
			case SYSTEM:
				enterOuterAlt(_localctx, 2);
				{
				setState(818);
				ruleTargetTranslateOutputSystem();
				}
				break;
			case DISPLAY:
				enterOuterAlt(_localctx, 3);
				{
				setState(819);
				ruleTargetTranslateOutputDisplay();
				}
				break;
			case CODING:
				enterOuterAlt(_localctx, 4);
				{
				setState(820);
				ruleTargetTranslateOutputCoding();
				}
				break;
			case CODEABLECONCEPT:
				enterOuterAlt(_localctx, 5);
				{
				setState(821);
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
		enterRule(_localctx, 204, RULE_ruleTargetTranslateOutputCode);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(824);
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
		enterRule(_localctx, 206, RULE_ruleTargetTranslateOutputSystem);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(826);
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
		enterRule(_localctx, 208, RULE_ruleTargetTranslateOutputDisplay);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(828);
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
		enterRule(_localctx, 210, RULE_ruleTargetTranslateOutputCoding);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(830);
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
		enterRule(_localctx, 212, RULE_ruleTargetTranslateOutputCodeableConcept);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(832);
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
			if ( visitor instanceof FhirMapJavaVisitor ) try {
				return ((FhirMapJavaVisitor<? extends T>)visitor).visitRuleTargetTruncate(this);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return visitor.visitChildren(this);
		}
	}

	public final RuleTargetTruncateContext ruleTargetTruncate() throws RecognitionException {
		RuleTargetTruncateContext _localctx = new RuleTargetTruncateContext(_ctx, getState());
		enterRule(_localctx, 214, RULE_ruleTargetTruncate);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(835);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,82,_ctx) ) {
			case 1:
				{
				setState(834);
				ruleTargetContext();
				}
				break;
			}
			setState(837);
			match(TRUNCATE);
			setState(838);
			match(T__9);
			setState(839);
			ruleTargetTruncateSource();
			setState(840);
			match(T__4);
			setState(841);
			ruleTargetTruncateLength();
			setState(842);
			match(T__10);
			setState(844);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==AS) {
				{
				setState(843);
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
		enterRule(_localctx, 216, RULE_ruleTargetTruncateSource);
		try {
			setState(848);
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
				setState(846);
				identifier();
				}
				break;
			case QIDENTIFIER:
			case QSTRING:
				enterOuterAlt(_localctx, 2);
				{
				setState(847);
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
		enterRule(_localctx, 218, RULE_ruleTargetTruncateLength);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(850);
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
			if ( visitor instanceof FhirMapJavaVisitor ) try {
				return ((FhirMapJavaVisitor<? extends T>)visitor).visitRuleTargetUuid(this);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return visitor.visitChildren(this);
		}
	}

	public final RuleTargetUuidContext ruleTargetUuid() throws RecognitionException {
		RuleTargetUuidContext _localctx = new RuleTargetUuidContext(_ctx, getState());
		enterRule(_localctx, 220, RULE_ruleTargetUuid);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(853);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,85,_ctx) ) {
			case 1:
				{
				setState(852);
				ruleTargetContext();
				}
				break;
			}
			setState(855);
			match(UUID);
			setState(856);
			match(T__9);
			setState(857);
			match(T__10);
			setState(859);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==AS) {
				{
				setState(858);
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
			if ( visitor instanceof FhirMapJavaVisitor ) try {
				return ((FhirMapJavaVisitor<? extends T>)visitor).visitRuleTargetVariable(this);
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			return visitor.visitChildren(this);
		}
	}

	public final RuleTargetVariableContext ruleTargetVariable() throws RecognitionException {
		RuleTargetVariableContext _localctx = new RuleTargetVariableContext(_ctx, getState());
		enterRule(_localctx, 222, RULE_ruleTargetVariable);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(861);
			match(AS);
			setState(862);
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
		enterRule(_localctx, 224, RULE_fhirPath);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(864);
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
		enterRule(_localctx, 226, RULE_identifier);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(866);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << SOURCE) | (1L << TARGET) | (1L << QUERIED) | (1L << PRODUCED) | (1L << APPEND) | (1L << CAST) | (1L << C) | (1L << CC) | (1L << CODE) | (1L << CP) | (1L << CODEABLECONCEPT) | (1L << CODING) | (1L << COPY) | (1L << CREATE) | (1L << DATEOP) | (1L << DEFAULT) | (1L << DISPLAY) | (1L << ESCAPE) | (1L << EVALUATE) | (1L << ID) | (1L << MAP) | (1L << POINTER) | (1L << QTY) | (1L << REFERENCE) | (1L << SYSTEM) | (1L << TRANSLATE) | (1L << TRUNCATE) | (1L << TYPES) | (1L << UUID) | (1L << IDENTIFIER))) != 0)) ) {
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
		enterRule(_localctx, 228, RULE_integer);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(868);
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
		enterRule(_localctx, 230, RULE_quotedIdentifier);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(870);
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
		enterRule(_localctx, 232, RULE_quotedStringWQuotes);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(872);
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
		enterRule(_localctx, 234, RULE_quotedString);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(874);
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
			if ( visitor instanceof FhirMapJavaVisitor ) try {
				return ((FhirMapJavaVisitor<? extends T>)visitor).visitQuotedUrl(this);
			} catch (Exception e) {
				e.printStackTrace();
			}
			else return visitor.visitChildren(this);
			return null;
		}
	}

	public final QuotedUrlContext quotedUrl() throws RecognitionException {
		QuotedUrlContext _localctx = new QuotedUrlContext(_ctx, getState());
		enterRule(_localctx, 236, RULE_quotedUrl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(876);
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
			if ( visitor instanceof FhirMapJavaVisitor ) try {
				return ((FhirMapJavaVisitor<? extends T>)visitor).visitStructureDefinition(this);
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			return visitor.visitChildren(this);
		}
	}

	public final StructureDefinitionContext structureDefinition() throws RecognitionException {
		StructureDefinitionContext _localctx = new StructureDefinitionContext(_ctx, getState());
		enterRule(_localctx, 238, RULE_structureDefinition);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(878);
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
			if ( visitor instanceof FhirMapJavaVisitor ) {
				try {
					return ((FhirMapJavaVisitor<? extends T>) visitor).visitStructureMap(this);
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
			else{
				return visitor.visitChildren(this);
			}
			return null;
		}
	}

	public final StructureMapContext structureMap() throws RecognitionException {
		StructureMapContext _localctx = new StructureMapContext(_ctx, getState());
		enterRule(_localctx, 240, RULE_structureMap);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(880);
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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3>\u0375\4\2\t\2\4"+
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
		"w\tw\4x\tx\4y\ty\4z\tz\3\2\3\2\6\2\u00f7\n\2\r\2\16\2\u00f8\3\2\7\2\u00fc"+
		"\n\2\f\2\16\2\u00ff\13\2\3\2\3\2\3\3\3\3\3\3\3\3\3\3\3\4\3\4\3\4\3\4\3"+
		"\4\5\4\u010d\n\4\3\5\3\5\3\5\3\5\5\5\u0113\n\5\3\6\3\6\3\7\3\7\3\b\3\b"+
		"\3\t\3\t\3\n\3\n\3\n\5\n\u0120\n\n\3\13\3\13\7\13\u0124\n\13\f\13\16\13"+
		"\u0127\13\13\3\13\7\13\u012a\n\13\f\13\16\13\u012d\13\13\3\13\3\13\3\f"+
		"\3\f\5\f\u0133\n\f\3\f\3\f\5\f\u0137\n\f\3\r\3\r\3\r\3\16\3\16\3\17\3"+
		"\17\3\17\3\20\3\20\5\20\u0143\n\20\3\21\3\21\3\22\3\22\3\23\3\23\3\23"+
		"\3\23\3\23\5\23\u014e\n\23\3\23\5\23\u0151\n\23\3\24\3\24\3\25\3\25\3"+
		"\26\3\26\3\26\3\27\3\27\5\27\u015c\n\27\3\30\3\30\3\31\3\31\3\32\3\32"+
		"\3\32\3\32\3\32\5\32\u0167\n\32\3\33\3\33\3\33\7\33\u016c\n\33\f\33\16"+
		"\33\u016f\13\33\3\34\3\34\3\34\7\34\u0174\n\34\f\34\16\34\u0177\13\34"+
		"\3\35\3\35\5\35\u017b\n\35\3\35\5\35\u017e\n\35\3\35\5\35\u0181\n\35\3"+
		"\35\5\35\u0184\n\35\3\35\5\35\u0187\n\35\3\35\5\35\u018a\n\35\3\36\3\36"+
		"\3\36\3\36\3\36\3\36\5\36\u0192\n\36\3\37\3\37\3\37\3 \3 \3!\3!\3!\3\""+
		"\3\"\3\"\7\"\u019f\n\"\f\"\16\"\u01a2\13\"\3#\3#\5#\u01a6\n#\3$\3$\3$"+
		"\3%\3%\3%\3&\3&\3&\5&\u01b1\n&\3\'\3\'\3\'\7\'\u01b6\n\'\f\'\16\'\u01b9"+
		"\13\'\3(\3(\3(\7(\u01be\n(\f(\16(\u01c1\13(\3(\3(\3)\3)\3)\3)\3)\3)\3"+
		")\3)\3)\3)\3)\3)\3)\3)\3)\3)\3)\3)\3)\5)\u01d8\n)\3*\3*\3*\3+\3+\3+\3"+
		",\3,\3,\5,\u01e3\n,\3-\3-\5-\u01e7\n-\3.\5.\u01ea\n.\3.\3.\3.\3.\3.\5"+
		".\u01f1\n.\3/\3/\3/\7/\u01f6\n/\f/\16/\u01f9\13/\3\60\3\60\5\60\u01fd"+
		"\n\60\3\61\5\61\u0200\n\61\3\61\3\61\3\61\3\61\3\61\3\61\3\61\5\61\u0209"+
		"\n\61\3\61\3\61\5\61\u020d\n\61\3\62\3\62\3\63\3\63\5\63\u0213\n\63\3"+
		"\64\3\64\3\65\5\65\u0218\n\65\3\65\3\65\3\65\3\65\3\65\5\65\u021f\n\65"+
		"\3\65\3\65\5\65\u0223\n\65\3\66\3\66\3\67\3\67\38\38\58\u022b\n8\38\5"+
		"8\u022e\n8\39\59\u0231\n9\39\39\39\39\39\59\u0238\n9\3:\3:\3;\5;\u023d"+
		"\n;\3;\3;\3;\3;\3;\3;\3;\5;\u0246\n;\3;\3;\5;\u024a\n;\3<\3<\3=\3=\5="+
		"\u0250\n=\3>\3>\3?\5?\u0255\n?\3?\3?\3?\3?\3?\5?\u025c\n?\3?\3?\3?\5?"+
		"\u0261\n?\3@\3@\3A\3A\3B\3B\3B\3B\3B\3B\5B\u026d\nB\3C\3C\3D\5D\u0272"+
		"\nD\3D\3D\3D\3D\3D\5D\u0279\nD\3E\3E\3F\5F\u027e\nF\3F\3F\3F\3F\3F\3F"+
		"\3F\5F\u0287\nF\3F\3F\5F\u028b\nF\3G\3G\3H\3H\3I\3I\3J\5J\u0294\nJ\3J"+
		"\3J\3J\3J\3J\3J\3J\5J\u029d\nJ\3J\3J\5J\u02a1\nJ\3K\3K\3L\3L\3M\3M\3N"+
		"\5N\u02aa\nN\3N\3N\3N\3N\3N\3N\3N\5N\u02b3\nN\3O\3O\3P\3P\3Q\5Q\u02ba"+
		"\nQ\3Q\3Q\3Q\3Q\3Q\3Q\3Q\5Q\u02c3\nQ\3Q\3Q\5Q\u02c7\nQ\3R\3R\3S\3S\3T"+
		"\3T\3U\5U\u02d0\nU\3U\3U\3U\3U\3U\5U\u02d7\nU\3V\3V\3W\3W\3W\5W\u02de"+
		"\nW\3X\5X\u02e1\nX\3X\3X\3X\3X\3X\5X\u02e8\nX\3Y\3Y\3Z\5Z\u02ed\nZ\3Z"+
		"\3Z\3Z\3Z\3Z\3Z\3Z\3Z\3Z\5Z\u02f8\nZ\3[\3[\3\\\3\\\3]\3]\3^\5^\u0301\n"+
		"^\3^\3^\3^\3^\3^\3^\3^\3^\3^\5^\u030c\n^\3_\3_\3`\3`\3a\3a\3b\5b\u0315"+
		"\nb\3b\3b\3b\3b\3b\5b\u031c\nb\3c\3c\5c\u0320\nc\3d\5d\u0323\nd\3d\3d"+
		"\3d\3d\3d\3d\3d\3d\3d\5d\u032e\nd\3e\3e\3f\3f\3g\3g\3g\3g\3g\5g\u0339"+
		"\ng\3h\3h\3i\3i\3j\3j\3k\3k\3l\3l\3m\5m\u0346\nm\3m\3m\3m\3m\3m\3m\3m"+
		"\5m\u034f\nm\3n\3n\5n\u0353\nn\3o\3o\3p\5p\u0358\np\3p\3p\3p\3p\5p\u035e"+
		"\np\3q\3q\3q\3r\3r\3s\3s\3t\3t\3u\3u\3v\3v\3w\3w\3x\3x\3y\3y\3z\3z\3z"+
		"\2\2{\2\4\6\b\n\f\16\20\22\24\26\30\32\34\36 \"$&(*,.\60\62\64\668:<>"+
		"@BDFHJLNPRTVXZ\\^`bdfhjlnprtvxz|~\u0080\u0082\u0084\u0086\u0088\u008a"+
		"\u008c\u008e\u0090\u0092\u0094\u0096\u0098\u009a\u009c\u009e\u00a0\u00a2"+
		"\u00a4\u00a6\u00a8\u00aa\u00ac\u00ae\u00b0\u00b2\u00b4\u00b6\u00b8\u00ba"+
		"\u00bc\u00be\u00c0\u00c2\u00c4\u00c6\u00c8\u00ca\u00cc\u00ce\u00d0\u00d2"+
		"\u00d4\u00d6\u00d8\u00da\u00dc\u00de\u00e0\u00e2\u00e4\u00e6\u00e8\u00ea"+
		"\u00ec\u00ee\u00f0\u00f2\2\4\f\2\20\23\26\26\30\34\36$&\'++/\63\65\67"+
		"99<<\3\2\24\25\2\u0369\2\u00f4\3\2\2\2\4\u0102\3\2\2\2\6\u0107\3\2\2\2"+
		"\b\u0112\3\2\2\2\n\u0114\3\2\2\2\f\u0116\3\2\2\2\16\u0118\3\2\2\2\20\u011a"+
		"\3\2\2\2\22\u011c\3\2\2\2\24\u0121\3\2\2\2\26\u0130\3\2\2\2\30\u0138\3"+
		"\2\2\2\32\u013b\3\2\2\2\34\u013d\3\2\2\2\36\u0142\3\2\2\2 \u0144\3\2\2"+
		"\2\"\u0146\3\2\2\2$\u0148\3\2\2\2&\u0152\3\2\2\2(\u0154\3\2\2\2*\u0156"+
		"\3\2\2\2,\u015b\3\2\2\2.\u015d\3\2\2\2\60\u015f\3\2\2\2\62\u0161\3\2\2"+
		"\2\64\u0168\3\2\2\2\66\u0170\3\2\2\28\u0178\3\2\2\2:\u018b\3\2\2\2<\u0193"+
		"\3\2\2\2>\u0196\3\2\2\2@\u0198\3\2\2\2B\u019b\3\2\2\2D\u01a5\3\2\2\2F"+
		"\u01a7\3\2\2\2H\u01aa\3\2\2\2J\u01ad\3\2\2\2L\u01b2\3\2\2\2N\u01ba\3\2"+
		"\2\2P\u01d7\3\2\2\2R\u01d9\3\2\2\2T\u01dc\3\2\2\2V\u01df\3\2\2\2X\u01e6"+
		"\3\2\2\2Z\u01e9\3\2\2\2\\\u01f2\3\2\2\2^\u01fc\3\2\2\2`\u01ff\3\2\2\2"+
		"b\u020e\3\2\2\2d\u0212\3\2\2\2f\u0214\3\2\2\2h\u0217\3\2\2\2j\u0224\3"+
		"\2\2\2l\u0226\3\2\2\2n\u022a\3\2\2\2p\u0230\3\2\2\2r\u0239\3\2\2\2t\u023c"+
		"\3\2\2\2v\u024b\3\2\2\2x\u024f\3\2\2\2z\u0251\3\2\2\2|\u0254\3\2\2\2~"+
		"\u0262\3\2\2\2\u0080\u0264\3\2\2\2\u0082\u0266\3\2\2\2\u0084\u026e\3\2"+
		"\2\2\u0086\u0271\3\2\2\2\u0088\u027a\3\2\2\2\u008a\u027d\3\2\2\2\u008c"+
		"\u028c\3\2\2\2\u008e\u028e\3\2\2\2\u0090\u0290\3\2\2\2\u0092\u0293\3\2"+
		"\2\2\u0094\u02a2\3\2\2\2\u0096\u02a4\3\2\2\2\u0098\u02a6\3\2\2\2\u009a"+
		"\u02a9\3\2\2\2\u009c\u02b4\3\2\2\2\u009e\u02b6\3\2\2\2\u00a0\u02b9\3\2"+
		"\2\2\u00a2\u02c8\3\2\2\2\u00a4\u02ca\3\2\2\2\u00a6\u02cc\3\2\2\2\u00a8"+
		"\u02cf\3\2\2\2\u00aa\u02d8\3\2\2\2\u00ac\u02dd\3\2\2\2\u00ae\u02e0\3\2"+
		"\2\2\u00b0\u02e9\3\2\2\2\u00b2\u02ec\3\2\2\2\u00b4\u02f9\3\2\2\2\u00b6"+
		"\u02fb\3\2\2\2\u00b8\u02fd\3\2\2\2\u00ba\u0300\3\2\2\2\u00bc\u030d\3\2"+
		"\2\2\u00be\u030f\3\2\2\2\u00c0\u0311\3\2\2\2\u00c2\u0314\3\2\2\2\u00c4"+
		"\u031f\3\2\2\2\u00c6\u0322\3\2\2\2\u00c8\u032f\3\2\2\2\u00ca\u0331\3\2"+
		"\2\2\u00cc\u0338\3\2\2\2\u00ce\u033a\3\2\2\2\u00d0\u033c\3\2\2\2\u00d2"+
		"\u033e\3\2\2\2\u00d4\u0340\3\2\2\2\u00d6\u0342\3\2\2\2\u00d8\u0345\3\2"+
		"\2\2\u00da\u0352\3\2\2\2\u00dc\u0354\3\2\2\2\u00de\u0357\3\2\2\2\u00e0"+
		"\u035f\3\2\2\2\u00e2\u0362\3\2\2\2\u00e4\u0364\3\2\2\2\u00e6\u0366\3\2"+
		"\2\2\u00e8\u0368\3\2\2\2\u00ea\u036a\3\2\2\2\u00ec\u036c\3\2\2\2\u00ee"+
		"\u036e\3\2\2\2\u00f0\u0370\3\2\2\2\u00f2\u0372\3\2\2\2\u00f4\u00f6\5\4"+
		"\3\2\u00f5\u00f7\5\6\4\2\u00f6\u00f5\3\2\2\2\u00f7\u00f8\3\2\2\2\u00f8"+
		"\u00f6\3\2\2\2\u00f8\u00f9\3\2\2\2\u00f9\u00fd\3\2\2\2\u00fa\u00fc\5\22"+
		"\n\2\u00fb\u00fa\3\2\2\2\u00fc\u00ff\3\2\2\2\u00fd\u00fb\3\2\2\2\u00fd"+
		"\u00fe\3\2\2\2\u00fe\u0100\3\2\2\2\u00ff\u00fd\3\2\2\2\u0100\u0101\5\24"+
		"\13\2\u0101\3\3\2\2\2\u0102\u0103\7/\2\2\u0103\u0104\5\u00f2z\2\u0104"+
		"\u0105\7\3\2\2\u0105\u0106\5\u00ecw\2\u0106\5\3\2\2\2\u0107\u0108\7:\2"+
		"\2\u0108\u0109\5\u00f0y\2\u0109\u010a\7\27\2\2\u010a\u010c\5\b\5\2\u010b"+
		"\u010d\7\4\2\2\u010c\u010b\3\2\2\2\u010c\u010d\3\2\2\2\u010d\7\3\2\2\2"+
		"\u010e\u0113\5\n\6\2\u010f\u0113\5\f\7\2\u0110\u0113\5\16\b\2\u0111\u0113"+
		"\5\20\t\2\u0112\u010e\3\2\2\2\u0112\u010f\3\2\2\2\u0112\u0110\3\2\2\2"+
		"\u0112\u0111\3\2\2\2\u0113\t\3\2\2\2\u0114\u0115\7\20\2\2\u0115\13\3\2"+
		"\2\2\u0116\u0117\7\21\2\2\u0117\r\3\2\2\2\u0118\u0119\7\22\2\2\u0119\17"+
		"\3\2\2\2\u011a\u011b\7\23\2\2\u011b\21\3\2\2\2\u011c\u011d\7,\2\2\u011d"+
		"\u011f\5\u00f2z\2\u011e\u0120\7\4\2\2\u011f\u011e\3\2\2\2\u011f\u0120"+
		"\3\2\2\2\u0120\23\3\2\2\2\u0121\u0125\5\26\f\2\u0122\u0124\5$\23\2\u0123"+
		"\u0122\3\2\2\2\u0124\u0127\3\2\2\2\u0125\u0123\3\2\2\2\u0125\u0126\3\2"+
		"\2\2\u0126\u012b\3\2\2\2\u0127\u0125\3\2\2\2\u0128\u012a\5\62\32\2\u0129"+
		"\u0128\3\2\2\2\u012a\u012d\3\2\2\2\u012b\u0129\3\2\2\2\u012b\u012c\3\2"+
		"\2\2\u012c\u012e\3\2\2\2\u012d\u012b\3\2\2\2\u012e\u012f\5\32\16\2\u012f"+
		"\25\3\2\2\2\u0130\u0132\7*\2\2\u0131\u0133\5\34\17\2\u0132\u0131\3\2\2"+
		"\2\u0132\u0133\3\2\2\2\u0133\u0134\3\2\2\2\u0134\u0136\5\u00e4s\2\u0135"+
		"\u0137\5\30\r\2\u0136\u0135\3\2\2\2\u0136\u0137\3\2\2\2\u0137\27\3\2\2"+
		"\2\u0138\u0139\7(\2\2\u0139\u013a\5\u00e4s\2\u013a\31\3\2\2\2\u013b\u013c"+
		"\7%\2\2\u013c\33\3\2\2\2\u013d\u013e\7)\2\2\u013e\u013f\5\36\20\2\u013f"+
		"\35\3\2\2\2\u0140\u0143\5 \21\2\u0141\u0143\5\"\22\2\u0142\u0140\3\2\2"+
		"\2\u0142\u0141\3\2\2\2\u0143\37\3\2\2\2\u0144\u0145\7\67\2\2\u0145!\3"+
		"\2\2\2\u0146\u0147\78\2\2\u0147#\3\2\2\2\u0148\u0149\7-\2\2\u0149\u014a"+
		"\5&\24\2\u014a\u014b\7\5\2\2\u014b\u014d\5(\25\2\u014c\u014e\5*\26\2\u014d"+
		"\u014c\3\2\2\2\u014d\u014e\3\2\2\2\u014e\u0150\3\2\2\2\u014f\u0151\7\4"+
		"\2\2\u0150\u014f\3\2\2\2\u0150\u0151\3\2\2\2\u0151%\3\2\2\2\u0152\u0153"+
		"\5\u00e4s\2\u0153\'\3\2\2\2\u0154\u0155\5\u00e4s\2\u0155)\3\2\2\2\u0156"+
		"\u0157\7\27\2\2\u0157\u0158\5,\27\2\u0158+\3\2\2\2\u0159\u015c\5.\30\2"+
		"\u015a\u015c\5\60\31\2\u015b\u0159\3\2\2\2\u015b\u015a\3\2\2\2\u015c-"+
		"\3\2\2\2\u015d\u015e\7\20\2\2\u015e/\3\2\2\2\u015f\u0160\7\21\2\2\u0160"+
		"\61\3\2\2\2\u0161\u0162\5\64\33\2\u0162\u0163\7\5\2\2\u0163\u0164\7)\2"+
		"\2\u0164\u0166\5\66\34\2\u0165\u0167\5J&\2\u0166\u0165\3\2\2\2\u0166\u0167"+
		"\3\2\2\2\u0167\63\3\2\2\2\u0168\u016d\5\u00e4s\2\u0169\u016a\7\6\2\2\u016a"+
		"\u016c\5\u00e4s\2\u016b\u0169\3\2\2\2\u016c\u016f\3\2\2\2\u016d\u016b"+
		"\3\2\2\2\u016d\u016e\3\2\2\2\u016e\65\3\2\2\2\u016f\u016d\3\2\2\2\u0170"+
		"\u0175\58\35\2\u0171\u0172\7\7\2\2\u0172\u0174\58\35\2\u0173\u0171\3\2"+
		"\2\2\u0174\u0177\3\2\2\2\u0175\u0173\3\2\2\2\u0175\u0176\3\2\2\2\u0176"+
		"\67\3\2\2\2\u0177\u0175\3\2\2\2\u0178\u017a\5B\"\2\u0179\u017b\5:\36\2"+
		"\u017a\u0179\3\2\2\2\u017a\u017b\3\2\2\2\u017b\u017d\3\2\2\2\u017c\u017e"+
		"\5<\37\2\u017d\u017c\3\2\2\2\u017d\u017e\3\2\2\2\u017e\u0180\3\2\2\2\u017f"+
		"\u0181\5> \2\u0180\u017f\3\2\2\2\u0180\u0181\3\2\2\2\u0181\u0183\3\2\2"+
		"\2\u0182\u0184\5@!\2\u0183\u0182\3\2\2\2\u0183\u0184\3\2\2\2\u0184\u0186"+
		"\3\2\2\2\u0185\u0187\5F$\2\u0186\u0185\3\2\2\2\u0186\u0187\3\2\2\2\u0187"+
		"\u0189\3\2\2\2\u0188\u018a\5H%\2\u0189\u0188\3\2\2\2\u0189\u018a\3\2\2"+
		"\2\u018a9\3\2\2\2\u018b\u018c\7\5\2\2\u018c\u0191\5\u00e4s\2\u018d\u018e"+
		"\5\u00e6t\2\u018e\u018f\7\b\2\2\u018f\u0190\5\u00e6t\2\u0190\u0192\3\2"+
		"\2\2\u0191\u018d\3\2\2\2\u0191\u0192\3\2\2\2\u0192;\3\2\2\2\u0193\u0194"+
		"\7#\2\2\u0194\u0195\5\u00e4s\2\u0195=\3\2\2\2\u0196\u0197\7\t\2\2\u0197"+
		"?\3\2\2\2\u0198\u0199\7\27\2\2\u0199\u019a\5\u00e4s\2\u019aA\3\2\2\2\u019b"+
		"\u01a0\5D#\2\u019c\u019d\7\6\2\2\u019d\u019f\5D#\2\u019e\u019c\3\2\2\2"+
		"\u019f\u01a2\3\2\2\2\u01a0\u019e\3\2\2\2\u01a0\u01a1\3\2\2\2\u01a1C\3"+
		"\2\2\2\u01a2\u01a0\3\2\2\2\u01a3\u01a6\5\u00e4s\2\u01a4\u01a6\5\u00ec"+
		"w\2\u01a5\u01a3\3\2\2\2\u01a5\u01a4\3\2\2\2\u01a6E\3\2\2\2\u01a7\u01a8"+
		"\7;\2\2\u01a8\u01a9\5\u00e2r\2\u01a9G\3\2\2\2\u01aa\u01ab\7\35\2\2\u01ab"+
		"\u01ac\5\u00e2r\2\u01acI\3\2\2\2\u01ad\u01ae\7.\2\2\u01ae\u01b0\5L\'\2"+
		"\u01af\u01b1\5N(\2\u01b0\u01af\3\2\2\2\u01b0\u01b1\3\2\2\2\u01b1K\3\2"+
		"\2\2\u01b2\u01b7\5P)\2\u01b3\u01b4\7\7\2\2\u01b4\u01b6\5P)\2\u01b5\u01b3"+
		"\3\2\2\2\u01b6\u01b9\3\2\2\2\u01b7\u01b5\3\2\2\2\u01b7\u01b8\3\2\2\2\u01b8"+
		"M\3\2\2\2\u01b9\u01b7\3\2\2\2\u01ba\u01bb\7\64\2\2\u01bb\u01bf\7\n\2\2"+
		"\u01bc\u01be\5\62\32\2\u01bd\u01bc\3\2\2\2\u01be\u01c1\3\2\2\2\u01bf\u01bd"+
		"\3\2\2\2\u01bf\u01c0\3\2\2\2\u01c0\u01c2\3\2\2\2\u01c1\u01bf\3\2\2\2\u01c2"+
		"\u01c3\7\13\2\2\u01c3O\3\2\2\2\u01c4\u01d8\5Z.\2\u01c5\u01d8\5T+\2\u01c6"+
		"\u01d8\5V,\2\u01c7\u01d8\5`\61\2\u01c8\u01d8\5h\65\2\u01c9\u01d8\5n8\2"+
		"\u01ca\u01d8\5|?\2\u01cb\u01d8\5\u0082B\2\u01cc\u01d8\5\u0086D\2\u01cd"+
		"\u01d8\5\u008aF\2\u01ce\u01d8\5\u0092J\2\u01cf\u01d8\5\u009aN\2\u01d0"+
		"\u01d8\5\u00a0Q\2\u01d1\u01d8\5\u00a8U\2\u01d2\u01d8\5\u00acW\2\u01d3"+
		"\u01d8\5\u00c2b\2\u01d4\u01d8\5\u00c6d\2\u01d5\u01d8\5\u00d8m\2\u01d6"+
		"\u01d8\5\u00dep\2\u01d7\u01c4\3\2\2\2\u01d7\u01c5\3\2\2\2\u01d7\u01c6"+
		"\3\2\2\2\u01d7\u01c7\3\2\2\2\u01d7\u01c8\3\2\2\2\u01d7\u01c9\3\2\2\2\u01d7"+
		"\u01ca\3\2\2\2\u01d7\u01cb\3\2\2\2\u01d7\u01cc\3\2\2\2\u01d7\u01cd\3\2"+
		"\2\2\u01d7\u01ce\3\2\2\2\u01d7\u01cf\3\2\2\2\u01d7\u01d0\3\2\2\2\u01d7"+
		"\u01d1\3\2\2\2\u01d7\u01d2\3\2\2\2\u01d7\u01d3\3\2\2\2\u01d7\u01d4\3\2"+
		"\2\2\u01d7\u01d5\3\2\2\2\u01d7\u01d6\3\2\2\2\u01d8Q\3\2\2\2\u01d9\u01da"+
		"\5B\"\2\u01da\u01db\7\3\2\2\u01dbS\3\2\2\2\u01dc\u01dd\5B\"\2\u01dd\u01de"+
		"\5\u00e0q\2\u01deU\3\2\2\2\u01df\u01e0\5R*\2\u01e0\u01e2\5X-\2\u01e1\u01e3"+
		"\5\u00e0q\2\u01e2\u01e1\3\2\2\2\u01e2\u01e3\3\2\2\2\u01e3W\3\2\2\2\u01e4"+
		"\u01e7\5\u00eav\2\u01e5\u01e7\5\u00e4s\2\u01e6\u01e4\3\2\2\2\u01e6\u01e5"+
		"\3\2\2\2\u01e7Y\3\2\2\2\u01e8\u01ea\5R*\2\u01e9\u01e8\3\2\2\2\u01e9\u01ea"+
		"\3\2\2\2\u01ea\u01eb\3\2\2\2\u01eb\u01ec\7\26\2\2\u01ec\u01ed\7\f\2\2"+
		"\u01ed\u01ee\5\\/\2\u01ee\u01f0\7\r\2\2\u01ef\u01f1\5\u00e0q\2\u01f0\u01ef"+
		"\3\2\2\2\u01f0\u01f1\3\2\2\2\u01f1[\3\2\2\2\u01f2\u01f7\5^\60\2\u01f3"+
		"\u01f4\7\7\2\2\u01f4\u01f6\5^\60\2\u01f5\u01f3\3\2\2\2\u01f6\u01f9\3\2"+
		"\2\2\u01f7\u01f5\3\2\2\2\u01f7\u01f8\3\2\2\2\u01f8]\3\2\2\2\u01f9\u01f7"+
		"\3\2\2\2\u01fa\u01fd\5\u00e4s\2\u01fb\u01fd\5\u00eav\2\u01fc\u01fa\3\2"+
		"\2\2\u01fc\u01fb\3\2\2\2\u01fd_\3\2\2\2\u01fe\u0200\5R*\2\u01ff\u01fe"+
		"\3\2\2\2\u01ff\u0200\3\2\2\2\u0200\u0201\3\2\2\2\u0201\u0202\7\31\2\2"+
		"\u0202\u0203\7\f\2\2\u0203\u0204\5b\62\2\u0204\u0205\7\7\2\2\u0205\u0208"+
		"\5d\63\2\u0206\u0207\7\7\2\2\u0207\u0209\5f\64\2\u0208\u0206\3\2\2\2\u0208"+
		"\u0209\3\2\2\2\u0209\u020a\3\2\2\2\u020a\u020c\7\r\2\2\u020b\u020d\5\u00e0"+
		"q\2\u020c\u020b\3\2\2\2\u020c\u020d\3\2\2\2\u020da\3\2\2\2\u020e\u020f"+
		"\5\u00eex\2\u020fc\3\2\2\2\u0210\u0213\5\u00eav\2\u0211\u0213\5\u00e4"+
		"s\2\u0212\u0210\3\2\2\2\u0212\u0211\3\2\2\2\u0213e\3\2\2\2\u0214\u0215"+
		"\5\u00ecw\2\u0215g\3\2\2\2\u0216\u0218\5R*\2\u0217\u0216\3\2\2\2\u0217"+
		"\u0218\3\2\2\2\u0218\u0219\3\2\2\2\u0219\u021a\7\30\2\2\u021a\u021b\7"+
		"\f\2\2\u021b\u021e\5j\66\2\u021c\u021d\7\7\2\2\u021d\u021f\5l\67\2\u021e"+
		"\u021c\3\2\2\2\u021e\u021f\3\2\2\2\u021f\u0220\3\2\2\2\u0220\u0222\7\r"+
		"\2\2\u0221\u0223\5\u00e0q\2\u0222\u0221\3\2\2\2\u0222\u0223\3\2\2\2\u0223"+
		"i\3\2\2\2\u0224\u0225\5\u00e4s\2\u0225k\3\2\2\2\u0226\u0227\5\u00e4s\2"+
		"\u0227m\3\2\2\2\u0228\u022b\5p9\2\u0229\u022b\5t;\2\u022a\u0228\3\2\2"+
		"\2\u022a\u0229\3\2\2\2\u022b\u022d\3\2\2\2\u022c\u022e\5\u00e0q\2\u022d"+
		"\u022c\3\2\2\2\u022d\u022e\3\2\2\2\u022eo\3\2\2\2\u022f\u0231\5R*\2\u0230"+
		"\u022f\3\2\2\2\u0230\u0231\3\2\2\2\u0231\u0232\3\2\2\2\u0232\u0233\7\32"+
		"\2\2\u0233\u0234\7\f\2\2\u0234\u0235\5r:\2\u0235\u0237\7\r\2\2\u0236\u0238"+
		"\5\u00e0q\2\u0237\u0236\3\2\2\2\u0237\u0238\3\2\2\2\u0238q\3\2\2\2\u0239"+
		"\u023a\5\u00ecw\2\u023as\3\2\2\2\u023b\u023d\5R*\2\u023c\u023b\3\2\2\2"+
		"\u023c\u023d\3\2\2\2\u023d\u023e\3\2\2\2\u023e\u023f\7\32\2\2\u023f\u0240"+
		"\7\f\2\2\u0240\u0241\5v<\2\u0241\u0242\7\7\2\2\u0242\u0245\5x=\2\u0243"+
		"\u0244\7\7\2\2\u0244\u0246\5z>\2\u0245\u0243\3\2\2\2\u0245\u0246\3\2\2"+
		"\2\u0246\u0247\3\2\2\2\u0247\u0249\7\r\2\2\u0248\u024a\5\u00e0q\2\u0249"+
		"\u0248\3\2\2\2\u0249\u024a\3\2\2\2\u024au\3\2\2\2\u024b\u024c\5\u00ee"+
		"x\2\u024cw\3\2\2\2\u024d\u0250\5\u00eav\2\u024e\u0250\5\u00e4s\2\u024f"+
		"\u024d\3\2\2\2\u024f\u024e\3\2\2\2\u0250y\3\2\2\2\u0251\u0252\5\u00ec"+
		"w\2\u0252{\3\2\2\2\u0253\u0255\5R*\2\u0254\u0253\3\2\2\2\u0254\u0255\3"+
		"\2\2\2\u0255\u0256\3\2\2\2\u0256\u0257\7\34\2\2\u0257\u025b\7\f\2\2\u0258"+
		"\u0259\5~@\2\u0259\u025a\7\7\2\2\u025a\u025c\3\2\2\2\u025b\u0258\3\2\2"+
		"\2\u025b\u025c\3\2\2\2\u025c\u025d\3\2\2\2\u025d\u025e\5\u0080A\2\u025e"+
		"\u0260\7\r\2\2\u025f\u0261\5\u00e0q\2\u0260\u025f\3\2\2\2\u0260\u0261"+
		"\3\2\2\2\u0261}\3\2\2\2\u0262\u0263\5\u00eex\2\u0263\177\3\2\2\2\u0264"+
		"\u0265\5\u00e4s\2\u0265\u0081\3\2\2\2\u0266\u0267\5R*\2\u0267\u0268\7"+
		" \2\2\u0268\u0269\7\f\2\2\u0269\u026a\5\u0084C\2\u026a\u026c\7\r\2\2\u026b"+
		"\u026d\5\u00e0q\2\u026c\u026b\3\2\2\2\u026c\u026d\3\2\2\2\u026d\u0083"+
		"\3\2\2\2\u026e\u026f\5\u00e4s\2\u026f\u0085\3\2\2\2\u0270\u0272\5R*\2"+
		"\u0271\u0270\3\2\2\2\u0271\u0272\3\2\2\2\u0272\u0273\3\2\2\2\u0273\u0274"+
		"\7!\2\2\u0274\u0275\7\f\2\2\u0275\u0276\5\u0088E\2\u0276\u0278\7\r\2\2"+
		"\u0277\u0279\5\u00e0q\2\u0278\u0277\3\2\2\2\u0278\u0279\3\2\2\2\u0279"+
		"\u0087\3\2\2\2\u027a\u027b\5\u00e8u\2\u027b\u0089\3\2\2\2\u027c\u027e"+
		"\5R*\2\u027d\u027c\3\2\2\2\u027d\u027e\3\2\2\2\u027e\u027f\3\2\2\2\u027f"+
		"\u0280\7\"\2\2\u0280\u0281\7\f\2\2\u0281\u0282\5\u008cG\2\u0282\u0283"+
		"\7\7\2\2\u0283\u0286\5\u008eH\2\u0284\u0285\7\7\2\2\u0285\u0287\5\u0090"+
		"I\2\u0286\u0284\3\2\2\2\u0286\u0287\3\2\2\2\u0287\u0288\3\2\2\2\u0288"+
		"\u028a\7\r\2\2\u0289\u028b\5\u00e0q\2\u028a\u0289\3\2\2\2\u028a\u028b"+
		"\3\2\2\2\u028b\u008b\3\2\2\2\u028c\u028d\5\u00e4s\2\u028d\u008d\3\2\2"+
		"\2\u028e\u028f\5\u00ecw\2\u028f\u008f\3\2\2\2\u0290\u0291\5\u00e4s\2\u0291"+
		"\u0091\3\2\2\2\u0292\u0294\5R*\2\u0293\u0292\3\2\2\2\u0293\u0294\3\2\2"+
		"\2\u0294\u0295\3\2\2\2\u0295\u0296\7&\2\2\u0296\u0297\7\f\2\2\u0297\u0298"+
		"\5\u0094K\2\u0298\u0299\7\7\2\2\u0299\u029c\5\u0096L\2\u029a\u029b\7\7"+
		"\2\2\u029b\u029d\5\u0098M\2\u029c\u029a\3\2\2\2\u029c\u029d\3\2\2\2\u029d"+
		"\u029e\3\2\2\2\u029e\u02a0\7\r\2\2\u029f\u02a1\5\u00e0q\2\u02a0\u029f"+
		"\3\2\2\2\u02a0\u02a1\3\2\2\2\u02a1\u0093\3\2\2\2\u02a2\u02a3\5\u00e4s"+
		"\2\u02a3\u0095\3\2\2\2\u02a4\u02a5\5\u00ecw\2\u02a5\u0097\3\2\2\2\u02a6"+
		"\u02a7\5\u00ecw\2\u02a7\u0099\3\2\2\2\u02a8\u02aa\5R*\2\u02a9\u02a8\3"+
		"\2\2\2\u02a9\u02aa\3\2\2\2\u02aa\u02ab\3\2\2\2\u02ab\u02ac\7\'\2\2\u02ac"+
		"\u02ad\7\f\2\2\u02ad\u02ae\5\u009cO\2\u02ae\u02af\7\7\2\2\u02af\u02b0"+
		"\5\u009eP\2\u02b0\u02b2\7\r\2\2\u02b1\u02b3\5\u00e0q\2\u02b2\u02b1\3\2"+
		"\2\2\u02b2\u02b3\3\2\2\2\u02b3\u009b\3\2\2\2\u02b4\u02b5\5\u00e4s\2\u02b5"+
		"\u009d\3\2\2\2\u02b6\u02b7\5\u00e4s\2\u02b7\u009f\3\2\2\2\u02b8\u02ba"+
		"\5R*\2\u02b9\u02b8\3\2\2\2\u02b9\u02ba\3\2\2\2\u02ba\u02bb\3\2\2\2\u02bb"+
		"\u02bc\7+\2\2\u02bc\u02bd\7\f\2\2\u02bd\u02be\5\u00a2R\2\u02be\u02bf\7"+
		"\7\2\2\u02bf\u02c2\5\u00a4S\2\u02c0\u02c1\7\7\2\2\u02c1\u02c3\5\u00a6"+
		"T\2\u02c2\u02c0\3\2\2\2\u02c2\u02c3\3\2\2\2\u02c3\u02c4\3\2\2\2\u02c4"+
		"\u02c6\7\r\2\2\u02c5\u02c7\5\u00e0q\2\u02c6\u02c5\3\2\2\2\u02c6\u02c7"+
		"\3\2\2\2\u02c7\u00a1\3\2\2\2\u02c8\u02c9\5\u00eex\2\u02c9\u00a3\3\2\2"+
		"\2\u02ca\u02cb\5\u00e4s\2\u02cb\u00a5\3\2\2\2\u02cc\u02cd\5\u00e4s\2\u02cd"+
		"\u00a7\3\2\2\2\u02ce\u02d0\5R*\2\u02cf\u02ce\3\2\2\2\u02cf\u02d0\3\2\2"+
		"\2\u02d0\u02d1\3\2\2\2\u02d1\u02d2\7\60\2\2\u02d2\u02d3\7\f\2\2\u02d3"+
		"\u02d4\5\u00aaV\2\u02d4\u02d6\7\r\2\2\u02d5\u02d7\5\u00e0q\2\u02d6\u02d5"+
		"\3\2\2\2\u02d6\u02d7\3\2\2\2\u02d7\u00a9\3\2\2\2\u02d8\u02d9\5\u00e4s"+
		"\2\u02d9\u00ab\3\2\2\2\u02da\u02de\5\u00aeX\2\u02db\u02de\5\u00b2Z\2\u02dc"+
		"\u02de\5\u00ba^\2\u02dd\u02da\3\2\2\2\u02dd\u02db\3\2\2\2\u02dd\u02dc"+
		"\3\2\2\2\u02de\u00ad\3\2\2\2\u02df\u02e1\5R*\2\u02e0\u02df\3\2\2\2\u02e0"+
		"\u02e1\3\2\2\2\u02e1\u02e2\3\2\2\2\u02e2\u02e3\7\61\2\2\u02e3\u02e4\7"+
		"\f\2\2\u02e4\u02e5\5\u00b0Y\2\u02e5\u02e7\7\r\2\2\u02e6\u02e8\5\u00e0"+
		"q\2\u02e7\u02e6\3\2\2\2\u02e7\u02e8\3\2\2\2\u02e8\u00af\3\2\2\2\u02e9"+
		"\u02ea\5\u00ecw\2\u02ea\u00b1\3\2\2\2\u02eb\u02ed\5R*\2\u02ec\u02eb\3"+
		"\2\2\2\u02ec\u02ed\3\2\2\2\u02ed\u02ee\3\2\2\2\u02ee\u02ef\7\61\2\2\u02ef"+
		"\u02f0\7\f\2\2\u02f0\u02f1\5\u00b4[\2\u02f1\u02f2\7\7\2\2\u02f2\u02f3"+
		"\5\u00b6\\\2\u02f3\u02f4\7\7\2\2\u02f4\u02f5\5\u00b8]\2\u02f5\u02f7\7"+
		"\r\2\2\u02f6\u02f8\5\u00e0q\2\u02f7\u02f6\3\2\2\2\u02f7\u02f8\3\2\2\2"+
		"\u02f8\u00b3\3\2\2\2\u02f9\u02fa\5\u00e4s\2\u02fa\u00b5\3\2\2\2\u02fb"+
		"\u02fc\5\u00ecw\2\u02fc\u00b7\3\2\2\2\u02fd\u02fe\5\u00eex\2\u02fe\u00b9"+
		"\3\2\2\2\u02ff\u0301\5R*\2\u0300\u02ff\3\2\2\2\u0300\u0301\3\2\2\2\u0301"+
		"\u0302\3\2\2\2\u0302\u0303\7\61\2\2\u0303\u0304\7\f\2\2\u0304\u0305\5"+
		"\u00bc_\2\u0305\u0306\7\7\2\2\u0306\u0307\5\u00be`\2\u0307\u0308\7\7\2"+
		"\2\u0308\u0309\5\u00c0a\2\u0309\u030b\7\r\2\2\u030a\u030c\5\u00e0q\2\u030b"+
		"\u030a\3\2\2\2\u030b\u030c\3\2\2\2\u030c\u00bb\3\2\2\2\u030d\u030e\5\u00e4"+
		"s\2\u030e\u00bd\3\2\2\2\u030f\u0310\5\u00ecw\2\u0310\u00bf\3\2\2\2\u0311"+
		"\u0312\5\u00e4s\2\u0312\u00c1\3\2\2\2\u0313\u0315\5R*\2\u0314\u0313\3"+
		"\2\2\2\u0314\u0315\3\2\2\2\u0315\u0316\3\2\2\2\u0316\u0317\7\62\2\2\u0317"+
		"\u0318\7\f\2\2\u0318\u0319\5\u00c4c\2\u0319\u031b\7\r\2\2\u031a\u031c"+
		"\5\u00e0q\2\u031b\u031a\3\2\2\2\u031b\u031c\3\2\2\2\u031c\u00c3\3\2\2"+
		"\2\u031d\u0320\5\u00e4s\2\u031e\u0320\5\u00eav\2\u031f\u031d\3\2\2\2\u031f"+
		"\u031e\3\2\2\2\u0320\u00c5\3\2\2\2\u0321\u0323\5R*\2\u0322\u0321\3\2\2"+
		"\2\u0322\u0323\3\2\2\2\u0323\u0324\3\2\2\2\u0324\u0325\7\65\2\2\u0325"+
		"\u0326\7\f\2\2\u0326\u0327\5\u00c8e\2\u0327\u0328\7\7\2\2\u0328\u0329"+
		"\5\u00caf\2\u0329\u032a\7\7\2\2\u032a\u032b\5\u00ccg\2\u032b\u032d\7\r"+
		"\2\2\u032c\u032e\5\u00e0q\2\u032d\u032c\3\2\2\2\u032d\u032e\3\2\2\2\u032e"+
		"\u00c7\3\2\2\2\u032f\u0330\5\u00e4s\2\u0330\u00c9\3\2\2\2\u0331\u0332"+
		"\5\u00eex\2\u0332\u00cb\3\2\2\2\u0333\u0339\5\u00ceh\2\u0334\u0339\5\u00d0"+
		"i\2\u0335\u0339\5\u00d2j\2\u0336\u0339\5\u00d4k\2\u0337\u0339\5\u00d6"+
		"l\2\u0338\u0333\3\2\2\2\u0338\u0334\3\2\2\2\u0338\u0335\3\2\2\2\u0338"+
		"\u0336\3\2\2\2\u0338\u0337\3\2\2\2\u0339\u00cd\3\2\2\2\u033a\u033b\7\33"+
		"\2\2\u033b\u00cf\3\2\2\2\u033c\u033d\7\63\2\2\u033d\u00d1\3\2\2\2\u033e"+
		"\u033f\7$\2\2\u033f\u00d3\3\2\2\2\u0340\u0341\7\37\2\2\u0341\u00d5\3\2"+
		"\2\2\u0342\u0343\7\36\2\2\u0343\u00d7\3\2\2\2\u0344\u0346\5R*\2\u0345"+
		"\u0344\3\2\2\2\u0345\u0346\3\2\2\2\u0346\u0347\3\2\2\2\u0347\u0348\7\66"+
		"\2\2\u0348\u0349\7\f\2\2\u0349\u034a\5\u00dan\2\u034a\u034b\7\7\2\2\u034b"+
		"\u034c\5\u00dco\2\u034c\u034e\7\r\2\2\u034d\u034f\5\u00e0q\2\u034e\u034d"+
		"\3\2\2\2\u034e\u034f\3\2\2\2\u034f\u00d9\3\2\2\2\u0350\u0353\5\u00e4s"+
		"\2\u0351\u0353\5\u00eav\2\u0352\u0350\3\2\2\2\u0352\u0351\3\2\2\2\u0353"+
		"\u00db\3\2\2\2\u0354\u0355\5\u00e6t\2\u0355\u00dd\3\2\2\2\u0356\u0358"+
		"\5R*\2\u0357\u0356\3\2\2\2\u0357\u0358\3\2\2\2\u0358\u0359\3\2\2\2\u0359"+
		"\u035a\79\2\2\u035a\u035b\7\f\2\2\u035b\u035d\7\r\2\2\u035c\u035e\5\u00e0"+
		"q\2\u035d\u035c\3\2\2\2\u035d\u035e\3\2\2\2\u035e\u00df\3\2\2\2\u035f"+
		"\u0360\7\27\2\2\u0360\u0361\5\u00e4s\2\u0361\u00e1\3\2\2\2\u0362\u0363"+
		"\5\u00ecw\2\u0363\u00e3\3\2\2\2\u0364\u0365\t\2\2\2\u0365\u00e5\3\2\2"+
		"\2\u0366\u0367\7\17\2\2\u0367\u00e7\3\2\2\2\u0368\u0369\7\24\2\2\u0369"+
		"\u00e9\3\2\2\2\u036a\u036b\t\3\2\2\u036b\u00eb\3\2\2\2\u036c\u036d\t\3"+
		"\2\2\u036d\u00ed\3\2\2\2\u036e\u036f\5\u00ecw\2\u036f\u00ef\3\2\2\2\u0370"+
		"\u0371\5\u00eex\2\u0371\u00f1\3\2\2\2\u0372\u0373\5\u00eex\2\u0373\u00f3"+
		"\3\2\2\2Y\u00f8\u00fd\u010c\u0112\u011f\u0125\u012b\u0132\u0136\u0142"+
		"\u014d\u0150\u015b\u0166\u016d\u0175\u017a\u017d\u0180\u0183\u0186\u0189"+
		"\u0191\u01a0\u01a5\u01b0\u01b7\u01bf\u01d7\u01e2\u01e6\u01e9\u01f0\u01f7"+
		"\u01fc\u01ff\u0208\u020c\u0212\u0217\u021e\u0222\u022a\u022d\u0230\u0237"+
		"\u023c\u0245\u0249\u024f\u0254\u025b\u0260\u026c\u0271\u0278\u027d\u0286"+
		"\u028a\u0293\u029c\u02a0\u02a9\u02b2\u02b9\u02c2\u02c6\u02cf\u02d6\u02dd"+
		"\u02e0\u02e7\u02ec\u02f7\u0300\u030b\u0314\u031b\u031f\u0322\u032d\u0338"+
		"\u0345\u034e\u0352\u0357\u035d";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}
