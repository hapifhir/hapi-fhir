package org.hl7.fhir.dstu3.terminologies;

import org.hl7.fhir.dstu3.terminologies.ValueSetExpander.ETooCostly;
import org.hl7.fhir.dstu3.utils.EOperationOutcome;

public interface ValueSetChecker {

  boolean codeInValueSet(String system, String code) throws ETooCostly, EOperationOutcome, Exception;

}
