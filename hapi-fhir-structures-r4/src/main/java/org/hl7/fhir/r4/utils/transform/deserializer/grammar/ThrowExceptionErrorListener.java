package org.hl7.fhir.r4.utils.transform.deserializer.grammar;

import org.antlr.v4.runtime.*;

// <copyright company="Applicadia LLC">
// Copyright (c) 2017
// by Applicadia LLC
// </copyright>



/**
This class implements an ANTLR error listener. When it herars an error, it will throw an exception and abort the
ANTLR processing.
*/
public class ThrowExceptionErrorListener extends BaseErrorListener implements ANTLRErrorListener
{
  /**

  */
  private String text;

  /**
  Constructor.

  @param text
  */
  public ThrowExceptionErrorListener(String text)
  {
    this.text = text;
  }

  /**


  @param sb
  @param line
  @param charPositionInLine
  */
  private void AppendInfo(StringBuilder sb, int line, int charPositionInLine)
  {
    line = line - 1; // line is one based....
    String[] lines = this.text.split("[\\n]", -1);
    if (line - 3 >= 0)
    {
      sb.append(lines[line - 3]).append("\r\n");
    }
    if (line - 2 >= 0)
    {
      sb.append(lines[line - 2]).append("\r\n");
    }
    if (line - 1 >= 0)
    {
      sb.append(lines[line - 1]).append("\r\n");
    }
    if (charPositionInLine == 0)
    {
      sb.append(String.format("-->%1$s", lines[line])).append("\r\n");
    }
    else if (charPositionInLine == lines[line].length())
    {
      sb.append(String.format("%1$s<--", lines[line])).append("\r\n");
    }
    else
    {
      sb.append(String.format("%1$s-->%2$s", lines[line].substring(0, charPositionInLine), lines[line].substring(charPositionInLine))).append("\r\n");
    }
    if (line + 1 < lines.length)
    {
      sb.append(lines[line + 1]).append("\r\n");
    }
    if (line + 2 < lines.length)
    {
      sb.append(lines[line + 2]).append("\r\n");
    }
    if (line + 3 < lines.length)
    {
      sb.append(lines[line + 3]).append("\r\n");
    }
  }

  /**
  Called when a syntax error occurs.

  @param output Output writer
  @param recognizer Parser/lexer being used
  @param offendingSymbol Symbol where error occured
  @param line Line where error occured
  @param charPositionInLine Character position in line where error occured.
  @param msg Error message
  @param e Recognition exception for error
  */

  public void SyntaxError(StringBuilder output, Recognizer recognizer, Token offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e)
  {
    StringBuilder sb = new StringBuilder();
    sb.append(String.format("Rule %1$s token %2$s", e.getCause(), offendingSymbol.getText())).append("\r\n");
    sb.append(String.format("Parse error: Line %1$s Pos %2$s. Msg %3$s ", line, charPositionInLine, msg)).append("\r\n");
    this.AppendInfo(sb, line, charPositionInLine);
    throw new IllegalArgumentException(sb.toString());
  }

  /**
  Called when a syntax error occurs.

  @param output Output writer
  @param recognizer Parser/lexer being used
  @param offendingSymbol Symbol where error occured
  @param line Line where error occured
  @param charPositionInLine Character position in line where error occured.
  @param msg Error message
  @param e Recognition exception for error
  */
  public final void SyntaxError(StringBuilder output, Recognizer recognizer, int offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e)
  {
    StringBuilder sb = new StringBuilder();
    sb.append(String.format("Rule %1$s", e.getCtx().getText())).append("\r\n");
    sb.append(String.format("Parse error: Line %1$s Pos %2$s. Msg %3$s ", line, charPositionInLine, msg)).append("\r\n");
    this.AppendInfo(sb, line, charPositionInLine);

    throw new IllegalArgumentException(sb.toString());
  }
}
