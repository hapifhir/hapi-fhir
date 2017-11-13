// Generated from Url.g4 by ANTLR 4.7
package org.hl7.fhir.r4.utils.transform.deserializer.grammar.antlr.javaAntlr;

import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link UrlParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 *            operations with no return type.
 */
public interface UrlVisitor<T> extends ParseTreeVisitor<T> {
  /**
   * Visit a parse tree produced by {@link UrlParser#fragmentaddress}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitFragmentaddress(UrlParser.FragmentaddressContext ctx);

  /**
   * Visit a parse tree produced by {@link UrlParser#uri}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitUri(UrlParser.UriContext ctx);

  /**
   * Visit a parse tree produced by {@link UrlParser#url}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitUrl(UrlParser.UrlContext ctx);

  /**
   * Visit a parse tree produced by {@link UrlParser#authority}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitAuthority(UrlParser.AuthorityContext ctx);

  /**
   * Visit a parse tree produced by {@link UrlParser#host}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitHost(UrlParser.HostContext ctx);

  /**
   * Visit a parse tree produced by {@link UrlParser#hostname}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitHostname(UrlParser.HostnameContext ctx);

  /**
   * Visit a parse tree produced by {@link UrlParser#hostnumber}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitHostnumber(UrlParser.HostnumberContext ctx);

  /**
   * Visit a parse tree produced by {@link UrlParser#port}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitPort(UrlParser.PortContext ctx);

  /**
   * Visit a parse tree produced by {@link UrlParser#path}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitPath(UrlParser.PathContext ctx);

  /**
   * Visit a parse tree produced by {@link UrlParser#search}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitSearch(UrlParser.SearchContext ctx);

  /**
   * Visit a parse tree produced by {@link UrlParser#searchParameter}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitSearchParameter(UrlParser.SearchParameterContext ctx);

  /**
   * Visit a parse tree produced by {@link UrlParser#searchParameterName}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitSearchParameterName(UrlParser.SearchParameterNameContext ctx);

  /**
   * Visit a parse tree produced by {@link UrlParser#searchParameterValue}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitSearchParameterValue(UrlParser.SearchParameterValueContext ctx);

  /**
   * Visit a parse tree produced by {@link UrlParser#user}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitUser(UrlParser.UserContext ctx);

  /**
   * Visit a parse tree produced by {@link UrlParser#login}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitLogin(UrlParser.LoginContext ctx);

  /**
   * Visit a parse tree produced by {@link UrlParser#password}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitPassword(UrlParser.PasswordContext ctx);

  /**
   * Visit a parse tree produced by {@link UrlParser#fragmentid}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitFragmentid(UrlParser.FragmentidContext ctx);

  /**
   * Visit a parse tree produced by {@link UrlParser#stringVal}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitStringVal(UrlParser.StringValContext ctx);
}
