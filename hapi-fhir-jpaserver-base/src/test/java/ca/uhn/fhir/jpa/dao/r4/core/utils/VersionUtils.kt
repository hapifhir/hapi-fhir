package ca.uhn.fhir.jpa.dao.r4.core.utils

import ca.uhn.fhir.jpa.dao.r4.core.CoreValidationTests
import ca.uhn.fhir.jpa.dao.r4.core.model.TestEntry
import org.hl7.fhir.r4.model.codesystems.FHIRVersion

private const val FOUR_POINT_01 = "4.0.1"
private const val FOUR_POINT_0 = "4.0"
private const val THREE_POINT_0 = "3.0"
private const val ONE_POINT_0 = "1.0"

private const val DEFAULT_VALIDATION_VERSION = "5.0"

private val VALID_FHIR_VERSION = listOf(FHIRVersion._3_0_0, FHIRVersion._3_0_1, FHIRVersion._3_3_0, FHIRVersion._3_5_0, FHIRVersion._4_0_0)

fun extractVersion(testEntry: TestEntry): String {
   return testEntry.version ?: DEFAULT_VALIDATION_VERSION
}

/**
 * Returns true, if the provided version in the [TestEntry] is the correct version.
 *
 * @param entry [TestEntry] for the resource. The resource version is stored in this.
 * @return true, if [TestEntry.version] is valid
 */
fun validVersion(entry: TestEntry): Boolean {
   val version = cleanVersionString(entry.version)
   return when (FHIRVersion.fromCode(version)) {
      in VALID_FHIR_VERSION -> true
      else -> false
   }
}

/**
 * Some of the versions in the manifest.xml file for tests are not standard FHIR version numbers. This method
 * converts those version codes to the corresponding [FHIRVersion].
 *
 * @param version [String] version from the [TestEntry]
 * @return updated version corresponding to one of [FHIRVersion]
 */
fun cleanVersionString(version: String?): String {
   return if (version == null) FHIRVersion._4_0_0.toCode()
   else when (version) {
      ONE_POINT_0 -> FHIRVersion._1_0_0.toCode()
      THREE_POINT_0 -> FHIRVersion._3_0_0.toCode()
      FOUR_POINT_0 -> FHIRVersion._4_0_0.toCode()
      FOUR_POINT_01 -> FHIRVersion._4_0_0.toCode()
      else -> version
   }
}
