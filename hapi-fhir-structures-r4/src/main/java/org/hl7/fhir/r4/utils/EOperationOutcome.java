package org.hl7.fhir.r4.utils;

import org.hl7.fhir.r4.model.OperationOutcome;

public class EOperationOutcome extends Exception {

  private static final long serialVersionUID = 8887222532359256131L;

  private OperationOutcome outcome;

  public EOperationOutcome(OperationOutcome outcome) {
    super();
    this.outcome = outcome;
  }

  public OperationOutcome getOutcome() {
    return outcome;
  }
  
  
}
