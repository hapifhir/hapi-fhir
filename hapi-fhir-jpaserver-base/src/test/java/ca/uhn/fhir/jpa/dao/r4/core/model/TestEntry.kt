package ca.uhn.fhir.jpa.dao.r4.core.model

import com.google.gson.annotations.SerializedName


data class TestEntry (
   @SerializedName("version") var version: String? = null,
   @SerializedName("explanation") var explanation: String? = null,
   @SerializedName("supporting") val supporting: List<String>? = null,
   @SerializedName("bundle-param") var bundleParam: BundleParam? = null,
   @SerializedName("errorCount") var errorCount: Int? = null,
   @SerializedName("profile") var profile: Profile = Profile(),
   @SerializedName("profiles") var profiles: List<String>? = null,
   @SerializedName("java") var testResult: TestResult? = null,
   @SerializedName("valuesets") var valuesets: List<String>? = null,
   @SerializedName("codesystems") var codesystems: List<String>? = null,
   @SerializedName("use-test") var usesTest: Boolean = true,
   @SerializedName("validate") var validate: String? = null,
   @SerializedName("examples") var allowExamples: Boolean = false
)
