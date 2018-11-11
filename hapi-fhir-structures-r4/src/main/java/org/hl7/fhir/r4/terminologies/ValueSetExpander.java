package org.hl7.fhir.r4.terminologies;

import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.ValueSet;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface ValueSetExpander {
  /**
   * @param source  the value set definition to expand
   * @param profile a profile affecting the outcome. If you don't supply a profile, the default internal expansion profile will be used.
   * @return
   * @throws ETooCostly
   * @throws FileNotFoundException
   * @throws IOException
   */
  ValueSetExpansionOutcome expand(ValueSet source, Parameters parameters) throws ETooCostly, FileNotFoundException, IOException;

  enum TerminologyServiceErrorClass {
    UNKNOWN, NOSERVICE, SERVER_ERROR, VALUESET_UNSUPPORTED;

    public boolean isInfrastructure() {
      return this == NOSERVICE || this == SERVER_ERROR || this == VALUESET_UNSUPPORTED;
    }
  }

  class ETooCostly extends Exception {

    public ETooCostly(String msg) {
      super(msg);
    }

  }

  /**
   * Some value sets are just too big to expand. Instead of an expanded value set,
   * you get back an interface that can test membership - usually on a server somewhere
   *
   * @author Grahame
   */
  class ValueSetExpansionOutcome {
    private ValueSet valueset;
    private String error;
    private TerminologyServiceErrorClass errorClass;

    public ValueSetExpansionOutcome(ValueSet valueset) {
      super();
      this.valueset = valueset;
      this.error = null;
    }

    public ValueSetExpansionOutcome(ValueSet valueset, String error, TerminologyServiceErrorClass errorClass) {
      super();
      this.valueset = valueset;
      this.error = error;
      this.errorClass = errorClass;
    }

    public ValueSetExpansionOutcome(ValueSetChecker service, String error, TerminologyServiceErrorClass errorClass) {
      super();
      this.valueset = null;
      this.error = error;
      this.errorClass = errorClass;
    }

    public ValueSetExpansionOutcome(String error, TerminologyServiceErrorClass errorClass) {
      this.valueset = null;
      this.error = error;
      this.errorClass = errorClass;
    }

    public ValueSet getValueset() {
      return valueset;
    }

    public String getError() {
      return error;
    }

    public TerminologyServiceErrorClass getErrorClass() {
      return errorClass;
    }


  }
}
