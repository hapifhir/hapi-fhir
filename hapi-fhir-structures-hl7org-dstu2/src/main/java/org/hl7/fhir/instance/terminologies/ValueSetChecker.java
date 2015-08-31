package org.hl7.fhir.instance.terminologies;

import org.hl7.fhir.instance.terminologies.ValueSetExpander.ETooCostly;
import org.hl7.fhir.instance.utils.EOperationOutcome;

public interface ValueSetChecker {

  boolean codeInValueSet(String system, String code) throws ETooCostly, EOperationOutcome, Exception;

}
