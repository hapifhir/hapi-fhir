package org.hl7.fhir.r4.utils.transform.deserializer;

import org.antlr.v4.runtime.CodePointBuffer;
import org.antlr.v4.runtime.CodePointCharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.hl7.fhir.r4.utils.transform.deserializer.grammar.DebugParseListener;
import org.hl7.fhir.r4.utils.transform.deserializer.grammar.ThrowErrorHandler;
import org.hl7.fhir.r4.utils.transform.deserializer.grammar.ThrowExceptionErrorListener;
import org.hl7.fhir.r4.utils.transform.deserializer.grammar.antlr.javaAntlr.FhirMapLexer;
import org.hl7.fhir.r4.utils.transform.deserializer.grammar.antlr.javaAntlr.FhirMapParser;

import java.nio.CharBuffer;

/**
 * Process Mapping Language data.
 *
 * @author Travis Lukach
 * (c) Applicadia LLC.
 */
public class FhirMapProcessor {
  /**
   * If true, output debug info during parsing.
   */
  private boolean __DebugFlag;

  /**
   * get accessor for __DebugFlag
   *
   * @return value for __DebugFlag
   */
  private boolean getDebugFlag() {
    return __DebugFlag;
  }

  /**
   * set accessor for __DebugFlag
   *
   * @param value value to set __DebugFlag
   */
  @SuppressWarnings("SameParameterValue")
  private void setDebugFlag(boolean value) {
    __DebugFlag = value;
  }

  /**
   * Constructor.
   */
  public FhirMapProcessor() throws Exception {
    setDebugFlag(false);
  }

  /**
   * Method to load string grammar.
   *
   * @param text Adl text
   * @return ANTLR parser
   */
  public FhirMapParser loadGrammar(String text) throws Exception {
    CharBuffer buffer = CharBuffer.allocate(text.length());
    buffer.append(text);
    buffer.position(0);
    FhirMapLexer lexer = new FhirMapLexer(CodePointCharStream.fromBuffer(CodePointBuffer.withChars(buffer)));
    lexer.addErrorListener(new ThrowExceptionErrorListener(text));
    CommonTokenStream commonTokenStream = new CommonTokenStream(lexer);
    FhirMapParser grammar = new FhirMapParser(commonTokenStream);
    if (this.getDebugFlag()) {
      DebugParseListener parseListener = new DebugParseListener(grammar, System.err::println);
      grammar.addParseListener(parseListener);
    }
    grammar.removeErrorListeners();
    grammar.setErrorHandler(new ThrowErrorHandler());
    grammar.addErrorListener(new ThrowExceptionErrorListener(text));
    return grammar;
  }

  /**
   * Parse Adl Definition text.
   *
   * @param FhirMapText Adl Archetype Text to process
   */
  public void parseFhirMap(String FhirMapText, IFhirMapExecutor executor) throws Exception {
    FhirMapParser grammar = this.loadGrammar(FhirMapText);
    ParseTree parseTree = grammar.mappingUnit();
    FhirMapVisitor visitor = new FhirMapVisitor(executor);
    visitor.visit(parseTree);
  }

}
