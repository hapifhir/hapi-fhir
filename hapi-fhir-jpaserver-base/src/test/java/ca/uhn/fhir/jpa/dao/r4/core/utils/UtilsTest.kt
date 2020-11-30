package ca.uhn.fhir.jpa.dao.r4.core.utils

import org.hl7.fhir.r4.model.OperationOutcome
import org.junit.jupiter.api.Test

class UtilsTest {

   @Test
   fun testUtilsFunction() {
      val outcome = OperationOutcome()

      val error1 = OperationOutcome.OperationOutcomeIssueComponent()
      error1.severity = OperationOutcome.IssueSeverity.ERROR
      error1.diagnostics = "error1 error"
      val error2 = OperationOutcome.OperationOutcomeIssueComponent()
      error2.severity = OperationOutcome.IssueSeverity.ERROR
      error2.diagnostics = "error2 error"
      val error3 = OperationOutcome.OperationOutcomeIssueComponent()
      error3.severity = OperationOutcome.IssueSeverity.ERROR
      error3.diagnostics = "error3 error"

      val info1 = OperationOutcome.OperationOutcomeIssueComponent()
      info1.severity = OperationOutcome.IssueSeverity.INFORMATION
      info1.diagnostics = "info1 error"
      val info2 = OperationOutcome.OperationOutcomeIssueComponent()
      info2.severity = OperationOutcome.IssueSeverity.INFORMATION
      info2.diagnostics = "info2 error"

      outcome.addIssue(error1)
         .addIssue(error2)
         .addIssue(error3)
         .addIssue(info1)
         .addIssue(info2)

      println(outcome.prettyPrintString())
   }
}
