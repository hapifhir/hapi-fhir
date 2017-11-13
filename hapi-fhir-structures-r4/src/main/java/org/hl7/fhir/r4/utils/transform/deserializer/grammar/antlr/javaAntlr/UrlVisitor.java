// Generated from Url.g4 by ANTLR 4.7
package org.hl7.fhir.r4.utils.transform.deserializer.grammar.antlr.javaAntlr;

import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link UrlJavaParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 *            operations with no return type.
 */
public interface UrlJavaVisitor<T> extends ParseTreeVisitor<T> {
  /**
   * Visit a parse tree produced by {@link UrlJavaParser#fragmentaddress}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitFragmentaddress(UrlJavaParser.FragmentaddressContext ctx);

  /**
   * Visit a parse tree produced by {@link UrlJavaParser#uri}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitUri(UrlJavaParser.UriContext ctx);

  /**
   * Visit a parse tree produced by {@link UrlJavaParser#url}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitUrl(UrlJavaParser.UrlContext ctx);

  /**
   * Visit a parse tree produced by {@link UrlJavaParser#authority}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitAuthority(UrlJavaParser.AuthorityContext ctx);

  /**
   * Visit a parse tree produced by {@link UrlJavaParser#host}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitHost(UrlJavaParser.HostContext ctx);

  /**
   * Visit a parse tree produced by {@link UrlJavaParser#hostname}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitHostname(UrlJavaParser.HostnameContext ctx);

  /**
   * Visit a parse tree produced by {@link UrlJavaParser#hostnumber}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitHostnumber(UrlJavaParser.HostnumberContext ctx);

  /**
   * Visit a parse tree produced by {@link UrlJavaParser#port}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitPort(UrlJavaParser.PortContext ctx);

  /**
   * Visit a parse tree produced by {@link UrlJavaParser#path}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitPath(UrlJavaParser.PathContext ctx);

  /**
   * Visit a parse tree produced by {@link UrlJavaParser#search}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitSearch(UrlJavaParser.SearchContext ctx);

  /**
   * Visit a parse tree produced by {@link UrlJavaParser#searchParameter}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitSearchParameter(UrlJavaParser.SearchParameterContext ctx);

  /**
   * Visit a parse tree produced by {@link UrlJavaParser#searchParameterName}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitSearchParameterName(UrlJavaParser.SearchParameterNameContext ctx);

  /**
   * Visit a parse tree produced by {@link UrlJavaParser#searchParameterValue}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitSearchParameterValue(UrlJavaParser.SearchParameterValueContext ctx);

  /**
   * Visit a parse tree produced by {@link UrlJavaParser#user}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitUser(UrlJavaParser.UserContext ctx);

  /**
   * Visit a parse tree produced by {@link UrlJavaParser#login}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitLogin(UrlJavaParser.LoginContext ctx);

  /**
   * Visit a parse tree produced by {@link UrlJavaParser#password}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitPassword(UrlJavaParser.PasswordContext ctx);

  /**
   * Visit a parse tree produced by {@link UrlJavaParser#fragmentid}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitFragmentid(UrlJavaParser.FragmentidContext ctx);

  /**
   * Visit a parse tree produced by {@link UrlJavaParser#stringVal}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitStringVal(UrlJavaParser.StringValContext ctx);
}
