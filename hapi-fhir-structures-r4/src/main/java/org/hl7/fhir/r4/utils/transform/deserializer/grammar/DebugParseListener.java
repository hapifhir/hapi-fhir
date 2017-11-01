package org.hl7.fhir.r4.utils.transform.deserializer.grammar;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.antlr.v4.runtime.tree.TerminalNode;

// <copyright company="Applicadia LLC">
// Copyright (c) 2017
// by Applicadia LLC
// </copyright>



/**
ANTLR parse listener for debugging.
*/
public class DebugParseListener implements ParseTreeListener
{
  /**
  ANTLR lexer/parser.
  */
  private Recognizer parser;
  /**
  Delegate ot print message.

  */
  @FunctionalInterface
  public interface MsgFcn {
    void invoke(String message);
  }


  /**
  Function for printing message.
  */
  private MsgFcn msgFcn;

  /**
  Constructor.

  @param parser ANTLR parser being executed
  @param msgFcn Message function to call
  */
  public DebugParseListener(Parser parser, MsgFcn msgFcn) {
    this.parser = parser;
    this.msgFcn = msgFcn;
  }

  /**
  Called each time a terminal is visited.

  @param node node being visited
  */
  public final void visitTerminal(TerminalNode node) {
    this.msgFcn.invoke(String.format("Terminal %1$s", node.getSymbol().getText()));
  }

  /**
  Called each time an error node is visited.

  @param node node being visited
  */
  public final void visitErrorNode(ErrorNode node) {
    this.msgFcn.invoke(String.format("Error Node %1$s", node.getSymbol().getText()));
  }

  /**
  Called each time any rule is entered.

  @param ctx Context of rule being entered
  */
  public final void enterEveryRule(ParserRuleContext ctx) {
    this.msgFcn.invoke("Enter: " + this.parser.getRuleNames()[ctx.getRuleIndex()]);
  }

  /**
  Called each time any rule is exited.

  @param ctx Context of rule being exited
  */
  public final void exitEveryRule(ParserRuleContext ctx) {
    this.msgFcn.invoke("Exit: " + this.parser.getRuleNames()[ctx.getRuleIndex()]);
  }
}
