package org.hl7.fhir.r4.utils;

import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.OperationOutcome.IssueSeverity;
import org.hl7.fhir.r4.model.OperationOutcome.IssueType;
import org.hl7.fhir.r4.model.OperationOutcome.OperationOutcomeIssueComponent;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.utilities.validation.ValidationMessage;

public class OperationOutcomeUtilities {


  public static OperationOutcomeIssueComponent convertToIssue(ValidationMessage message, OperationOutcome op) {
    OperationOutcomeIssueComponent issue = new OperationOutcome.OperationOutcomeIssueComponent();
    issue.setCode(convert(message.getType()));
    if (message.getLocation() != null) {
      StringType s = new StringType();
      s.setValue(message.getLocation()+(message.getLine()>= 0 && message.getCol() >= 0 ? " (line "+Integer.toString(message.getLine())+", col"+Integer.toString(message.getCol())+")" : "") );
      issue.getLocation().add(s);
    }
    issue.setSeverity(convert(message.getLevel()));
    CodeableConcept c = new CodeableConcept();
    c.setText(message.getMessage());
    issue.setDetails(c);
    if (message.getSource() != null) {
      issue.getExtension().add(ToolingExtensions.makeIssueSource(message.getSource()));
    }
    return issue;
  }

  private static IssueSeverity convert(org.hl7.fhir.utilities.validation.ValidationMessage.IssueSeverity level) {
    switch (level) {
    case FATAL : return IssueSeverity.FATAL;
    case ERROR : return IssueSeverity.ERROR;
    case WARNING : return IssueSeverity.WARNING;
    case INFORMATION : return IssueSeverity.INFORMATION;
	 case NULL : return IssueSeverity.NULL;
    }
    return IssueSeverity.NULL;
  }

  private static IssueType convert(org.hl7.fhir.utilities.validation.ValidationMessage.IssueType type) {
    switch (type) {
    case INVALID: 
    case STRUCTURE: return IssueType.STRUCTURE;
    case REQUIRED: return IssueType.REQUIRED;
    case VALUE: return IssueType.VALUE;
    case INVARIANT: return IssueType.INVARIANT;
    case SECURITY: return IssueType.SECURITY;
    case LOGIN: return IssueType.LOGIN;
    case UNKNOWN: return IssueType.UNKNOWN;
    case EXPIRED: return IssueType.EXPIRED;
    case FORBIDDEN: return IssueType.FORBIDDEN;
    case SUPPRESSED: return IssueType.SUPPRESSED;
    case PROCESSING: return IssueType.PROCESSING;
    case NOTSUPPORTED: return IssueType.NOTSUPPORTED;
    case DUPLICATE: return IssueType.DUPLICATE;
    case NOTFOUND: return IssueType.NOTFOUND;
    case TOOLONG: return IssueType.TOOLONG;
    case CODEINVALID: return IssueType.CODEINVALID;
    case EXTENSION: return IssueType.EXTENSION;
    case TOOCOSTLY: return IssueType.TOOCOSTLY;
    case BUSINESSRULE: return IssueType.BUSINESSRULE;
    case CONFLICT: return IssueType.CONFLICT;
    case INCOMPLETE: return IssueType.INCOMPLETE;
    case TRANSIENT: return IssueType.TRANSIENT;
    case LOCKERROR: return IssueType.LOCKERROR;
    case NOSTORE: return IssueType.NOSTORE;
    case EXCEPTION: return IssueType.EXCEPTION;
    case TIMEOUT: return IssueType.TIMEOUT;
    case THROTTLED: return IssueType.THROTTLED;
    case INFORMATIONAL: return IssueType.INFORMATIONAL;
	 case NULL: return IssueType.NULL;
    }
    return IssueType.NULL;
  }
}
