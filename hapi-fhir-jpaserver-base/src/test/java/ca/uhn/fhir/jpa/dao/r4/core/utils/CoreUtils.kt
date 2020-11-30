package ca.uhn.fhir.jpa.dao.r4.core.utils


import ca.uhn.fhir.jpa.dao.r4.core.model.TestResult
import ca.uhn.fhir.rest.api.EncodingEnum
import org.hl7.fhir.r4.model.OperationOutcome
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.function.Executable

//
///**
// * Pulls the filename of the profile from the test entry. Returns null if no data is set.
// *
// * @param testEntry [TestEntry]
// * @return [String] filename of the profile to use in testing, or null if not set.
// */
//fun getProfileFilename(testEntry: TestEntry): String? {
//   return if (testEntry.profile == null) null else testEntry.profile.source
//}
//

/**
 * Goes through the [OperationOutcome] and counts the error and warning counts, then compares those totals
 * to the expected values within the passes in [TestResult].
 *
 * These result counts are then compared using [Assertions.assertEquals].
 *
 * @param expected [ca.uhn.fhir.validation.core.model.TestEntry] expected validation results.
 * @param actual     [OperationOutcome] actual validation results.
 */
fun verifyOperationOutcome(expected: TestResult, actual: OperationOutcome) {
   println("Expected test output ::\n$expected\n")
   println("Actual test output :: ${actual.prettyPrintString()}")

   val map = (actual.issue ?: emptyList()).groupBy(OperationOutcome.OperationOutcomeIssueComponent::getSeverity, OperationOutcome.OperationOutcomeIssueComponent::getDiagnostics)

   if (expected.warningCount == TestResult.NO_WARNING) {
      Assertions.assertEquals(expected.errorCount, map[OperationOutcome.IssueSeverity.ERROR]?.size ?: 0 ,
         "Error counts should match test results from manifest.xml file...")
   } else {
      Assertions.assertAll("Error counts and warnings should match test results from manifest.xml file...",
         Executable { Assertions.assertEquals(expected.errorCount, map[OperationOutcome.IssueSeverity.ERROR]?.size ?: 0) },
         Executable { Assertions.assertEquals(expected.warningCount, map[OperationOutcome.IssueSeverity.WARNING]?.size ?: 0) })
   }
}

/**
 * Examines the passed in filename and returns the appropriate [EncodingEnum]. Only works for json and xml.
 *
 * @param testFile [String] filename. ie "Person.json"
 * @return [EncodingEnum]
 */
fun getEncoding(testFile: String): EncodingEnum {
   return if (testFile.endsWith(".json")) {
      EncodingEnum.JSON
   } else {
      EncodingEnum.XML
   }
}

fun OperationOutcome.prettyPrintString(): String {
   return issue.groupBy(OperationOutcome.OperationOutcomeIssueComponent::getSeverity, OperationOutcome.OperationOutcomeIssueComponent::getDiagnostics)
      .entries
      .joinToString(separator = "") { entry ->
         "${entry.key.display} <${entry.value.size} entries>\n${entry.value.joinToString(separator = "\n")}\n"
      }
}
