package ca.uhn.fhir.jpa.dao.r4.core.model

import com.google.gson.annotations.SerializedName

data class Profile (
   @SerializedName("errorCount") val errorCount: String? = null,
   @SerializedName("source") val source: String? = null,
   @SerializedName("supporting") val supporting: List<String>? = null,
   @SerializedName("java") val testResult: TestResult? = null,
   @SerializedName("assumeValidRestReferences") var assumeValidRestReferences: Boolean = false
)
