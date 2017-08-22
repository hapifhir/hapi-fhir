package org.hl7.fhir.r4.terminologies;

import org.hl7.fhir.r4.terminologies.ValueSetExpander.ETooCostly;
import org.hl7.fhir.r4.utils.EOperationOutcome;

public interface ValueSetChecker {

  boolean codeInValueSet(String system, String code) throws ETooCostly, EOperationOutcome, Exception;

}
