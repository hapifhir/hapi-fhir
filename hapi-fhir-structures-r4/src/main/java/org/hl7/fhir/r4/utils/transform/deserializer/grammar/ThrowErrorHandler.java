package org.hl7.fhir.r4.utils.transform.deserializer.grammar;

import org.antlr.v4.runtime.*;

/**
 * Handles how errors are processed by gracefully shutting down the program.
 */
public class ThrowErrorHandler extends DefaultErrorStrategy implements ANTLRErrorStrategy {

  /**
   * Constructor
   */
  public ThrowErrorHandler() {
    super();
  }

  @Override
  public void reset(Parser recognizer) {
    super.reset(recognizer);
  }

  @Override
  public Token recoverInline(Parser recognizer) throws RecognitionException {
    return super.recoverInline(recognizer);
  }

  @Override
  public void recover(Parser recognizer, RecognitionException e) throws RecognitionException {
    e.printStackTrace();
    System.err.println(e.getMessage());
    System.exit(1);
  }

  @Override
  public void sync(Parser recognizer) throws RecognitionException {
    super.sync(recognizer);
  }

  @Override
  public boolean inErrorRecoveryMode(Parser recognizer) {
    return false;
  }

  @Override
  public void reportMatch(Parser recognizer) {
    super.reportMatch(recognizer);
  }

  @Override
  public void reportError(Parser recognizer, RecognitionException e) {
    e.printStackTrace();
    System.exit(1);
  }
}
