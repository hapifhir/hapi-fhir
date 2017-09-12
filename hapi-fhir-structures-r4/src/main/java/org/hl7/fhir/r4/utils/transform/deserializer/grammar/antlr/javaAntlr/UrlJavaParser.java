// Generated from UrlJava.g4 by ANTLR 4.7
package org.hl7.fhir.r4.utils.transform.deserializer.grammar.antlr.javaAntlr;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import java.util.List;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class UrlJavaParser extends Parser {
  static { RuntimeMetaData.checkVersion("4.7", RuntimeMetaData.VERSION); }

  protected static final DFA[] _decisionToDFA;
  protected static final PredictionContextCache _sharedContextCache =
    new PredictionContextCache();
  public static final int
    T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9,
    HEX=10, STRING=11, DIGITS=12, WS=13;
  public static final int
    RULE_fragmentaddress = 0, RULE_uri = 1, RULE_url = 2, RULE_authority = 3,
    RULE_host = 4, RULE_hostname = 5, RULE_hostnumber = 6, RULE_port = 7,
    RULE_path = 8, RULE_search = 9, RULE_searchParameter = 10, RULE_searchParameterName = 11,
    RULE_searchParameterValue = 12, RULE_user = 13, RULE_login = 14, RULE_password = 15,
    RULE_fragmentid = 16, RULE_stringVal = 17;
  public static final String[] ruleNames = {
    "fragmentaddress", "uri", "url", "authority", "host", "hostname", "hostnumber",
    "port", "path", "search", "searchParameter", "searchParameterName", "searchParameterValue",
    "user", "login", "password", "fragmentid", "stringVal"
  };

  private static final String[] _LITERAL_NAMES = {
    null, "'#'", "'://'", "':'", "'/'", "'?'", "'.'", "'&'", "'='", "'@'"
  };
  private static final String[] _SYMBOLIC_NAMES = {
    null, null, null, null, null, null, null, null, null, null, "HEX", "STRING",
    "DIGITS", "WS"
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
  public String getGrammarFileName() { return "UrlJava.g4"; }

  @Override
  public String[] getRuleNames() { return ruleNames; }

  @Override
  public String getSerializedATN() { return _serializedATN; }

  @Override
  public ATN getATN() { return _ATN; }

  public UrlJavaParser(TokenStream input) {
    super(input);
    _interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
  }
  public static class FragmentaddressContext extends ParserRuleContext {
    public UriContext uri() {
      return getRuleContext(UriContext.class,0);
    }
    public FragmentidContext fragmentid() {
      return getRuleContext(FragmentidContext.class,0);
    }
    public TerminalNode WS() { return getToken(UrlJavaParser.WS, 0); }
    public FragmentaddressContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_fragmentaddress; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof UrlJavaVisitor ) return ((UrlJavaVisitor<? extends T>)visitor).visitFragmentaddress(this);
      else return visitor.visitChildren(this);
    }
  }

  public final FragmentaddressContext fragmentaddress() throws RecognitionException {
    FragmentaddressContext _localctx = new FragmentaddressContext(_ctx, getState());
    enterRule(_localctx, 0, RULE_fragmentaddress);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(36);
        uri();
        setState(39);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la==T__0) {
          {
            setState(37);
            match(T__0);
            setState(38);
            fragmentid();
          }
        }

        setState(42);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la==WS) {
          {
            setState(41);
            match(WS);
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

  public static class UriContext extends ParserRuleContext {
    public UrlContext url() {
      return getRuleContext(UrlContext.class,0);
    }
    public UriContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_uri; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof UrlJavaVisitor ) return ((UrlJavaVisitor<? extends T>)visitor).visitUri(this);
      else return visitor.visitChildren(this);
    }
  }

  public final UriContext uri() throws RecognitionException {
    UriContext _localctx = new UriContext(_ctx, getState());
    enterRule(_localctx, 2, RULE_uri);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(44);
        url();
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

  public static class UrlContext extends ParserRuleContext {
    public AuthorityContext authority() {
      return getRuleContext(AuthorityContext.class,0);
    }
    public HostContext host() {
      return getRuleContext(HostContext.class,0);
    }
    public LoginContext login() {
      return getRuleContext(LoginContext.class,0);
    }
    public PortContext port() {
      return getRuleContext(PortContext.class,0);
    }
    public PathContext path() {
      return getRuleContext(PathContext.class,0);
    }
    public SearchContext search() {
      return getRuleContext(SearchContext.class,0);
    }
    public UrlContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_url; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof UrlJavaVisitor ) {
        try {
          return ((UrlJavaVisitor<? extends T>) visitor).visitUrl(this);
        } catch (InstantiationException e) {
          e.printStackTrace();
        } catch (IllegalAccessException e) {
          e.printStackTrace();
        }
      }
      return visitor.visitChildren(this);
    }
  }

  public final UrlContext url() throws RecognitionException {
    UrlContext _localctx = new UrlContext(_ctx, getState());
    enterRule(_localctx, 4, RULE_url);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(46);
        authority();
        setState(47);
        match(T__1);
        setState(49);
        _errHandler.sync(this);
        switch ( getInterpreter().adaptivePredict(_input,2,_ctx) ) {
          case 1:
          {
            setState(48);
            login();
          }
          break;
        }
        setState(51);
        host();
        setState(54);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la==T__2) {
          {
            setState(52);
            match(T__2);
            setState(53);
            port();
          }
        }

        setState(58);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la==T__3) {
          {
            setState(56);
            match(T__3);
            setState(57);
            path();
          }
        }

        setState(62);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la==T__4) {
          {
            setState(60);
            match(T__4);
            setState(61);
            search();
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

  public static class AuthorityContext extends ParserRuleContext {
    public TerminalNode STRING() { return getToken(UrlJavaParser.STRING, 0); }
    public AuthorityContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_authority; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof UrlJavaVisitor ) return ((UrlJavaVisitor<? extends T>)visitor).visitAuthority(this);
      else return visitor.visitChildren(this);
    }
  }

  public final AuthorityContext authority() throws RecognitionException {
    AuthorityContext _localctx = new AuthorityContext(_ctx, getState());
    enterRule(_localctx, 6, RULE_authority);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(64);
        match(STRING);
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

  public static class HostContext extends ParserRuleContext {
    public HostnameContext hostname() {
      return getRuleContext(HostnameContext.class,0);
    }
    public HostnumberContext hostnumber() {
      return getRuleContext(HostnumberContext.class,0);
    }
    public HostContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_host; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof UrlJavaVisitor ) return ((UrlJavaVisitor<? extends T>)visitor).visitHost(this);
      else return visitor.visitChildren(this);
    }
  }

  public final HostContext host() throws RecognitionException {
    HostContext _localctx = new HostContext(_ctx, getState());
    enterRule(_localctx, 8, RULE_host);
    try {
      setState(68);
      _errHandler.sync(this);
      switch (_input.LA(1)) {
        case STRING:
          enterOuterAlt(_localctx, 1);
        {
          setState(66);
          hostname();
        }
        break;
        case DIGITS:
          enterOuterAlt(_localctx, 2);
        {
          setState(67);
          hostnumber();
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

  public static class HostnameContext extends ParserRuleContext {
    public List<StringValContext> stringVal() {
      return getRuleContexts(StringValContext.class);
    }
    public StringValContext stringVal(int i) {
      return getRuleContext(StringValContext.class,i);
    }
    public HostnameContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_hostname; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof UrlJavaVisitor ) return ((UrlJavaVisitor<? extends T>)visitor).visitHostname(this);
      else return visitor.visitChildren(this);
    }
  }

  public final HostnameContext hostname() throws RecognitionException {
    HostnameContext _localctx = new HostnameContext(_ctx, getState());
    enterRule(_localctx, 10, RULE_hostname);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(70);
        stringVal();
        setState(75);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la==T__5) {
          {
            {
              setState(71);
              match(T__5);
              setState(72);
              stringVal();
            }
          }
          setState(77);
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

  public static class HostnumberContext extends ParserRuleContext {
    public List<TerminalNode> DIGITS() { return getTokens(UrlJavaParser.DIGITS); }
    public TerminalNode DIGITS(int i) {
      return getToken(UrlJavaParser.DIGITS, i);
    }
    public HostnumberContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_hostnumber; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof UrlJavaVisitor ) return ((UrlJavaVisitor<? extends T>)visitor).visitHostnumber(this);
      else return visitor.visitChildren(this);
    }
  }

  public final HostnumberContext hostnumber() throws RecognitionException {
    HostnumberContext _localctx = new HostnumberContext(_ctx, getState());
    enterRule(_localctx, 12, RULE_hostnumber);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(78);
        match(DIGITS);
        setState(79);
        match(T__5);
        setState(80);
        match(DIGITS);
        setState(81);
        match(T__5);
        setState(82);
        match(DIGITS);
        setState(83);
        match(T__5);
        setState(84);
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

  public static class PortContext extends ParserRuleContext {
    public TerminalNode DIGITS() { return getToken(UrlJavaParser.DIGITS, 0); }
    public PortContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_port; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof UrlJavaVisitor ) return ((UrlJavaVisitor<? extends T>)visitor).visitPort(this);
      else return visitor.visitChildren(this);
    }
  }

  public final PortContext port() throws RecognitionException {
    PortContext _localctx = new PortContext(_ctx, getState());
    enterRule(_localctx, 14, RULE_port);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(86);
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

  public static class PathContext extends ParserRuleContext {
    public List<StringValContext> stringVal() {
      return getRuleContexts(StringValContext.class);
    }
    public StringValContext stringVal(int i) {
      return getRuleContext(StringValContext.class,i);
    }
    public PathContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_path; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof UrlJavaVisitor ) return ((UrlJavaVisitor<? extends T>)visitor).visitPath(this);
      else return visitor.visitChildren(this);
    }
  }

  public final PathContext path() throws RecognitionException {
    PathContext _localctx = new PathContext(_ctx, getState());
    enterRule(_localctx, 16, RULE_path);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(88);
        stringVal();
        setState(93);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la==T__3) {
          {
            {
              setState(89);
              match(T__3);
              setState(90);
              stringVal();
            }
          }
          setState(95);
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

  public static class SearchContext extends ParserRuleContext {
    public List<SearchParameterContext> searchParameter() {
      return getRuleContexts(SearchParameterContext.class);
    }
    public SearchParameterContext searchParameter(int i) {
      return getRuleContext(SearchParameterContext.class,i);
    }
    public SearchContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_search; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof UrlJavaVisitor ) return ((UrlJavaVisitor<? extends T>)visitor).visitSearch(this);
      else return visitor.visitChildren(this);
    }
  }

  public final SearchContext search() throws RecognitionException {
    SearchContext _localctx = new SearchContext(_ctx, getState());
    enterRule(_localctx, 18, RULE_search);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(96);
        searchParameter();
        setState(101);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la==T__6) {
          {
            {
              setState(97);
              match(T__6);
              setState(98);
              searchParameter();
            }
          }
          setState(103);
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

  public static class SearchParameterContext extends ParserRuleContext {
    public SearchParameterNameContext searchParameterName() {
      return getRuleContext(SearchParameterNameContext.class,0);
    }
    public SearchParameterValueContext searchParameterValue() {
      return getRuleContext(SearchParameterValueContext.class,0);
    }
    public SearchParameterContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_searchParameter; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof UrlJavaVisitor ) try {
        return ((UrlJavaVisitor<? extends T>)visitor).visitSearchParameter(this);
      } catch (Exception e) {
        e.printStackTrace();
      }
      return visitor.visitChildren(this);
    }
  }

  public final SearchParameterContext searchParameter() throws RecognitionException {
    SearchParameterContext _localctx = new SearchParameterContext(_ctx, getState());
    enterRule(_localctx, 20, RULE_searchParameter);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(104);
        searchParameterName();
        setState(107);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la==T__7) {
          {
            setState(105);
            match(T__7);
            setState(106);
            searchParameterValue();
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

  public static class SearchParameterNameContext extends ParserRuleContext {
    public TerminalNode STRING() { return getToken(UrlJavaParser.STRING, 0); }
    public SearchParameterNameContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_searchParameterName; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof UrlJavaVisitor ) return ((UrlJavaVisitor<? extends T>)visitor).visitSearchParameterName(this);
      else return visitor.visitChildren(this);
    }
  }

  public final SearchParameterNameContext searchParameterName() throws RecognitionException {
    SearchParameterNameContext _localctx = new SearchParameterNameContext(_ctx, getState());
    enterRule(_localctx, 22, RULE_searchParameterName);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(109);
        match(STRING);
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

  public static class SearchParameterValueContext extends ParserRuleContext {
    public TerminalNode STRING() { return getToken(UrlJavaParser.STRING, 0); }
    public TerminalNode DIGITS() { return getToken(UrlJavaParser.DIGITS, 0); }
    public TerminalNode HEX() { return getToken(UrlJavaParser.HEX, 0); }
    public SearchParameterValueContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_searchParameterValue; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof UrlJavaVisitor ) return ((UrlJavaVisitor<? extends T>)visitor).visitSearchParameterValue(this);
      else return visitor.visitChildren(this);
    }
  }

  public final SearchParameterValueContext searchParameterValue() throws RecognitionException {
    SearchParameterValueContext _localctx = new SearchParameterValueContext(_ctx, getState());
    enterRule(_localctx, 24, RULE_searchParameterValue);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(111);
        _la = _input.LA(1);
        if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << HEX) | (1L << STRING) | (1L << DIGITS))) != 0)) ) {
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

  public static class UserContext extends ParserRuleContext {
    public TerminalNode STRING() { return getToken(UrlJavaParser.STRING, 0); }
    public UserContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_user; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof UrlJavaVisitor ) return ((UrlJavaVisitor<? extends T>)visitor).visitUser(this);
      else return visitor.visitChildren(this);
    }
  }

  public final UserContext user() throws RecognitionException {
    UserContext _localctx = new UserContext(_ctx, getState());
    enterRule(_localctx, 26, RULE_user);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(113);
        match(STRING);
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

  public static class LoginContext extends ParserRuleContext {
    public UserContext user() {
      return getRuleContext(UserContext.class,0);
    }
    public PasswordContext password() {
      return getRuleContext(PasswordContext.class,0);
    }
    public LoginContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_login; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof UrlJavaVisitor ) try {
        return ((UrlJavaVisitor<? extends T>)visitor).visitLogin(this);
      } catch (InstantiationException e) {
        e.printStackTrace();
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      }
      return visitor.visitChildren(this);
    }
  }

  public final LoginContext login() throws RecognitionException {
    LoginContext _localctx = new LoginContext(_ctx, getState());
    enterRule(_localctx, 28, RULE_login);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(115);
        user();
        setState(116);
        match(T__2);
        setState(117);
        password();
        setState(118);
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

  public static class PasswordContext extends ParserRuleContext {
    public TerminalNode STRING() { return getToken(UrlJavaParser.STRING, 0); }
    public PasswordContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_password; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof UrlJavaVisitor ) return ((UrlJavaVisitor<? extends T>)visitor).visitPassword(this);
      else return visitor.visitChildren(this);
    }
  }

  public final PasswordContext password() throws RecognitionException {
    PasswordContext _localctx = new PasswordContext(_ctx, getState());
    enterRule(_localctx, 30, RULE_password);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(120);
        match(STRING);
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

  public static class FragmentidContext extends ParserRuleContext {
    public StringValContext stringVal() {
      return getRuleContext(StringValContext.class,0);
    }
    public FragmentidContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_fragmentid; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof UrlJavaVisitor ) return ((UrlJavaVisitor<? extends T>)visitor).visitFragmentid(this);
      else return visitor.visitChildren(this);
    }
  }

  public final FragmentidContext fragmentid() throws RecognitionException {
    FragmentidContext _localctx = new FragmentidContext(_ctx, getState());
    enterRule(_localctx, 32, RULE_fragmentid);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(122);
        stringVal();
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

  public static class StringValContext extends ParserRuleContext {
    public TerminalNode STRING() { return getToken(UrlJavaParser.STRING, 0); }
    public StringValContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }
    @Override public int getRuleIndex() { return RULE_stringVal; }
    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if ( visitor instanceof UrlJavaVisitor ) return ((UrlJavaVisitor<? extends T>)visitor).visitStringVal(this);
      else return visitor.visitChildren(this);
    }
  }

  public final StringValContext stringVal() throws RecognitionException {
    StringValContext _localctx = new StringValContext(_ctx, getState());
    enterRule(_localctx, 34, RULE_stringVal);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(124);
        match(STRING);
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
    "\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\17\u0081\4\2\t\2"+
      "\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
      "\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
      "\4\23\t\23\3\2\3\2\3\2\5\2*\n\2\3\2\5\2-\n\2\3\3\3\3\3\4\3\4\3\4\5\4\64"+
      "\n\4\3\4\3\4\3\4\5\49\n\4\3\4\3\4\5\4=\n\4\3\4\3\4\5\4A\n\4\3\5\3\5\3"+
      "\6\3\6\5\6G\n\6\3\7\3\7\3\7\7\7L\n\7\f\7\16\7O\13\7\3\b\3\b\3\b\3\b\3"+
      "\b\3\b\3\b\3\b\3\t\3\t\3\n\3\n\3\n\7\n^\n\n\f\n\16\na\13\n\3\13\3\13\3"+
      "\13\7\13f\n\13\f\13\16\13i\13\13\3\f\3\f\3\f\5\fn\n\f\3\r\3\r\3\16\3\16"+
      "\3\17\3\17\3\20\3\20\3\20\3\20\3\20\3\21\3\21\3\22\3\22\3\23\3\23\3\23"+
      "\2\2\24\2\4\6\b\n\f\16\20\22\24\26\30\32\34\36 \"$\2\3\3\2\f\16\2y\2&"+
      "\3\2\2\2\4.\3\2\2\2\6\60\3\2\2\2\bB\3\2\2\2\nF\3\2\2\2\fH\3\2\2\2\16P"+
      "\3\2\2\2\20X\3\2\2\2\22Z\3\2\2\2\24b\3\2\2\2\26j\3\2\2\2\30o\3\2\2\2\32"+
      "q\3\2\2\2\34s\3\2\2\2\36u\3\2\2\2 z\3\2\2\2\"|\3\2\2\2$~\3\2\2\2&)\5\4"+
      "\3\2\'(\7\3\2\2(*\5\"\22\2)\'\3\2\2\2)*\3\2\2\2*,\3\2\2\2+-\7\17\2\2,"+
      "+\3\2\2\2,-\3\2\2\2-\3\3\2\2\2./\5\6\4\2/\5\3\2\2\2\60\61\5\b\5\2\61\63"+
      "\7\4\2\2\62\64\5\36\20\2\63\62\3\2\2\2\63\64\3\2\2\2\64\65\3\2\2\2\65"+
      "8\5\n\6\2\66\67\7\5\2\2\679\5\20\t\28\66\3\2\2\289\3\2\2\29<\3\2\2\2:"+
      ";\7\6\2\2;=\5\22\n\2<:\3\2\2\2<=\3\2\2\2=@\3\2\2\2>?\7\7\2\2?A\5\24\13"+
      "\2@>\3\2\2\2@A\3\2\2\2A\7\3\2\2\2BC\7\r\2\2C\t\3\2\2\2DG\5\f\7\2EG\5\16"+
      "\b\2FD\3\2\2\2FE\3\2\2\2G\13\3\2\2\2HM\5$\23\2IJ\7\b\2\2JL\5$\23\2KI\3"+
      "\2\2\2LO\3\2\2\2MK\3\2\2\2MN\3\2\2\2N\r\3\2\2\2OM\3\2\2\2PQ\7\16\2\2Q"+
      "R\7\b\2\2RS\7\16\2\2ST\7\b\2\2TU\7\16\2\2UV\7\b\2\2VW\7\16\2\2W\17\3\2"+
      "\2\2XY\7\16\2\2Y\21\3\2\2\2Z_\5$\23\2[\\\7\6\2\2\\^\5$\23\2][\3\2\2\2"+
      "^a\3\2\2\2_]\3\2\2\2_`\3\2\2\2`\23\3\2\2\2a_\3\2\2\2bg\5\26\f\2cd\7\t"+
      "\2\2df\5\26\f\2ec\3\2\2\2fi\3\2\2\2ge\3\2\2\2gh\3\2\2\2h\25\3\2\2\2ig"+
      "\3\2\2\2jm\5\30\r\2kl\7\n\2\2ln\5\32\16\2mk\3\2\2\2mn\3\2\2\2n\27\3\2"+
      "\2\2op\7\r\2\2p\31\3\2\2\2qr\t\2\2\2r\33\3\2\2\2st\7\r\2\2t\35\3\2\2\2"+
      "uv\5\34\17\2vw\7\5\2\2wx\5 \21\2xy\7\13\2\2y\37\3\2\2\2z{\7\r\2\2{!\3"+
      "\2\2\2|}\5$\23\2}#\3\2\2\2~\177\7\r\2\2\177%\3\2\2\2\r),\638<@FM_gm";
  public static final ATN _ATN =
    new ATNDeserializer().deserialize(_serializedATN.toCharArray());
  static {
    _decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
    for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
      _decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
    }
  }
}
