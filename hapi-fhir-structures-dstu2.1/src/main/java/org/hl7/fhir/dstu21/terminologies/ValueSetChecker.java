package org.hl7.fhir.dstu21.terminologies;

import org.hl7.fhir.dstu21.terminologies.ValueSetExpander.ETooCostly;
import org.hl7.fhir.dstu21.utils.EOperationOutcome;

public interface ValueSetChecker {

  boolean codeInValueSet(String system, String code) throws ETooCostly, EOperationOutcome, Exception;

}
