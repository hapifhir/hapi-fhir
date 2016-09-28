package org.hl7.fhir.dstu3.terminologies;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.hl7.fhir.dstu3.model.ExpansionProfile;
import org.hl7.fhir.dstu3.model.ValueSet;

public interface ValueSetExpander {
  public enum TerminologyServiceErrorClass {
    UNKNOWN, NOSERVICE, SERVER_ERROR, VALUESET_UNSUPPORTED;

    public boolean isInfrastructure() {
      return this == NOSERVICE || this == SERVER_ERROR || this == VALUESET_UNSUPPORTED;
    }
  }
  
  public class ETooCostly extends Exception {

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
  public class ValueSetExpansionOutcome {
    private ValueSet valueset;
    private ValueSetChecker service;
    private String error;
    private TerminologyServiceErrorClass errorClass;
    
    public ValueSetExpansionOutcome(ValueSet valueset) {
      super();
      this.valueset = valueset;
      this.service = null;
      this.error = null;
    }
    public ValueSetExpansionOutcome(ValueSet valueset, String error, TerminologyServiceErrorClass errorClass) {
      super();
      this.valueset = valueset;
      this.service = null;
      this.error = error;
      this.errorClass = errorClass;
    }
    public ValueSetExpansionOutcome(ValueSetChecker service, String error, TerminologyServiceErrorClass errorClass) {
      super();
      this.valueset = null;
      this.service = service;
      this.error = error;
      this.errorClass = errorClass;
    }
    public ValueSetExpansionOutcome(String error, TerminologyServiceErrorClass errorClass) {
      this.valueset = null;
      this.service = null;
      this.error = error;
      this.errorClass = errorClass;
    }
    public ValueSet getValueset() {
      return valueset;
    }
    public ValueSetChecker getService() {
      return service;
    }
    public String getError() {
      return error;
    }
    public TerminologyServiceErrorClass getErrorClass() {
      return errorClass;
    }


  }
/**
 * 
 * @param source the value set definition to expand
 * @param profile a profile affecting the outcome. If you don't supply a profile, the default internal expansion profile will be used.
 *  
 * @return
 * @throws ETooCostly
 * @throws FileNotFoundException
 * @throws IOException
 */
  public ValueSetExpansionOutcome expand(ValueSet source, ExpansionProfile profile) throws ETooCostly, FileNotFoundException, IOException;
}
