package org.hl7.fhir.dstu2016may.terminologies;

import org.hl7.fhir.dstu2016may.terminologies.ValueSetExpander.ETooCostly;
import org.hl7.fhir.dstu2016may.utils.EOperationOutcome;

public interface ValueSetChecker {

  boolean codeInValueSet(String system, String code) throws ETooCostly, EOperationOutcome, Exception;

}
