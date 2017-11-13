package org.hl7.fhir.r4.utils.transform.deserializer.grammar;

import org.antlr.v4.runtime.*;

/**
 * Handles how errors are processed by gracefully shutting down the program.
 * @author Travis Lukach
 */
public class ThrowErrorHandler extends DefaultErrorStrategy implements ANTLRErrorStrategy {

  /**
   * Constructor
   */
  public ThrowErrorHandler() {
    super();
  }

  /**
   * rests the recognizer.
   * @param recognizer target recognizer to be rest.
   */
  @Override
  public void reset(Parser recognizer) {
    super.reset(recognizer);
  }

  /**
   * Gracefully recovers the state of the recognizer
   * @param recognizer target recognizer
   * @return returns the token recovered
   * @throws RecognitionException if recognition fails
   */
  @Override
  public Token recoverInline(Parser recognizer) throws RecognitionException {
    return super.recoverInline(recognizer);
  }

  /**
   * Recovers the parser, on error
   * @param recognizer target parser
   * @param e the exception
   * @throws RecognitionException if the recognizer fails
   */
  @Override
  public void recover(Parser recognizer, RecognitionException e) throws RecognitionException {
    e.printStackTrace();
    System.err.println(e.getMessage());
    System.exit(1);
  }

  /**
   * Synchronizes the parser, calls the super method
   * @param recognizer target parser
   * @throws RecognitionException if Recognition fails
   */
  @Override
  public void sync(Parser recognizer) throws RecognitionException {
    super.sync(recognizer);
  }

  /**
   *
   * @param recognizer
   * @return
   */
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
